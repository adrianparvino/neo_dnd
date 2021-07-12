(ns dnd.core
  (:require [phoenix.interop :refer [connect]]
            [reagent.dom :as rdom]
            [dnd.battle :as battle]
            [dnd.routes :refer [router]]
            [re-frame.core :as rf]
            [reitit.core :as r]
            [reitit.frontend.easy :as rfe]
            ["bootstrap-switch-button-react"
             :rename {default BootstrapSwitchButton}]
            ["react-bootstrap" :refer [Navbar Nav Row Navbar.Brand Navbar.Collapse Nav.Link]]))

(rf/reg-event-fx
 :initialize
 (fn [_ _]
   {:fx [[:connect "/socket"] [:dispatch [:navigated (r/match-by-path router js/window.location.pathname)]]]
    :db {:is-dm false}}))

(rf/reg-event-db
 :toggle-dm-clicked
 (fn [db _]
   (update db :is-dm not)))

(rf/reg-event-db
 :navigated
 (fn [db [_ match]]
   (merge db (:data match))))

(rf/reg-event-fx
 :connected
 (fn [{:keys [db]} [_ socket]]
   {:db (assoc db :socket socket)
    :fx [[:dispatch [::battle/initialize]]]}))

(rf/reg-fx
 :connect
 (fn [url]
   (rf/dispatch [:connected (connect url)])))

(rf/reg-sub
 :is-dm
 (fn [db _]
   (:is-dm db)))

(rf/reg-sub
 :name
 (fn [db _]
   (:name db)))

(rf/reg-sub
 :page
 (fn [db _]
   (:page db)))

(defn root []
  [:<>
   [:> Navbar
    [:> Navbar.Brand "Forest and Felines"]
    [:> Navbar.Collapse
     [:> Nav {:activeKey @(rf/subscribe [:name])}
      [:> Nav.Link {:eventKey :home :href (rfe/href :home)} "Home"]]
     [:> Navbar.Collapse {:class [:justify-content-end]}
      [:> BootstrapSwitchButton
       {:offlabel "Player"
        :onlabel "DM"
        :checked @(rf/subscribe [:is-dm])
        :onChange #(rf/dispatch [:toggle-dm-clicked])
        :width 100}]]]]
   [:main {:role "main" :class [:container-fluid :flex-grow-1 :overflow-auto]}
    [:> Row {:class [:h-100]}
     @(rf/subscribe [:page])]]])

(defn mount []
  (rfe/start! router
              (fn [match history] (rf/dispatch [:navigated match]))
              {:use-fragment false})
  (rdom/render [root] (js/document.getElementById "react-app")))

(defn ^:export init []
  (rf/dispatch-sync [:initialize])
  (mount))
