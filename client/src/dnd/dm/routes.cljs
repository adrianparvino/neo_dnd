(ns dnd.dm.routes
  (:require [dnd.dm.overview :refer [overview]]
            [dnd.dm.notes :refer [notes]]
            [reitit.core :as r]
            [re-frame.core :as rf]))

(def router
  (r/router
   [["/" {:view overview}]
    ["/notes" {:view notes}]]))

(rf/reg-cofx
 ::current-path
 (fn [coeffects _]
   (assoc coeffects :current-path js/window.location.pathname)))

(rf/reg-event-fx
 ::initialize
 [(rf/inject-cofx ::current-path)]
 (fn [{:keys [current-path db]} _]
   {:db (assoc db :path current-path)}))

(rf/reg-event-fx
 :router/navigate
 (fn [{:keys [db]} [_ new-location]]
   {:db (assoc db :path new-location)
    :fx [[::push-state new-location]]}))

(rf/reg-fx
 ::push-state
 (fn [path]
   (js/history.pushState nil "" path)))

(rf/reg-sub
 ::view
 (fn [{:keys [path]} _]
   (-> (r/match-by-path router path)
       :data
       :view)))

(defn navigate [e]
  (.preventDefault e)
  (rf/dispatch [:router/navigate (.. e -target -pathname)]))
