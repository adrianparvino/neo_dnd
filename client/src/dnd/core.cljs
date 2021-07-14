(ns dnd.core
  (:require [superstructor.re-frame.fetch-fx]
            [dnd.debug]
            [dnd.re-frame-phoenix]
            [dnd.dm.routes :as routes]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 ::initialize
 (fn [{:keys [db]} _]
   {:fx [[:phoenix/connect ["/socket" ::connected]]
         [:fetch {:method                 :get
                  :url                    "/api/session"
                  :response-content-types {#"application/.*json" :json}
                  :on-success             [::session-changed]}]
         [:dispatch [::routes/initialize]]
         [:dispatch [:dnd.dm.overview/initialize]]]}))

(rf/reg-event-fx
 ::connected
 (fn [{{socket :phoenix/socket} :db} event]
   nil))

(rf/reg-event-fx
 ::create-session
 (fn [_ _]
   {:fx [[:fetch {:method                 :post
                  :url                    "/api/session"
                  :response-content-types {#"application/.*json" :json}
                  :on-success             [::session-changed]}]]}))

(rf/reg-event-fx
 ::logout-session
 (fn [_ _]
   {:fx [[:fetch {:method                 :post
                  :url                    "/api/session/logout"
                  :response-content-types {#"application/.*json" :json}
                  :on-success             [::session-removed]}]]}))

(rf/reg-event-fx
 ::session-changed
 (fn [{:keys [db]} [_ {:keys [body]}]]
   {:db (assoc db :session (-> body js/JSON.parse js->clj))
    :fx [[:dispatch [:dnd.dm.notes/initialize]]]}))

(rf/reg-event-db
 ::session-removed
 (fn [db [_ {:keys [body]}]]
   (dissoc db :session nil)))

(rf/reg-sub
 :session
 (fn [db _]
   (:session db)))

(defn navbar []
  (let [session @(rf/subscribe [:session])]
    [:nav {:class [:p-4 :w-full :flex :flex-row :gap-4]}
     [:a {:class [:text-2xl :text-blue-500 :font-bold]} "Forest and Felines"]
     (when session
       (list
        [:a {:class [:my-auto] :href "/" :on-click routes/navigate} "Overview"]
        [:a {:class [:my-auto]} "Initiative Tracker"]
        [:a {:class [:my-auto] :href "/notes" :on-click routes/navigate} "Notes"]))
     [:div {:class [:mx-auto]}]
     (when session [:button {:class [:my-auto] :on-click #(rf/dispatch [::logout-session])} "Exit Session"])]))

(defn create-session-view []
  [:div {:class [:px-4 :gap-4 :flex :flex-row :flex-wrap :w-full :h-full :overflow-hidden]}
   [:button {:class [:rounded :m-auto :border :shadow-lg :p-4 :flex-initial] :on-click #(rf/dispatch [::create-session])}
    "Create Session"]])

(defn root []
  (let [view @(rf/subscribe [::routes/view])
        session @(rf/subscribe [:session])]
    [:div {:class [:flex :flex-col :w-screen :h-screen :overflow-hidden]}
     [navbar]
     [(if session
        view
        create-session-view)]]))

(defn mount []
  (rf/clear-subscription-cache!)
  (rdom/render [root] (js/document.getElementById "react-app")))

(defn ^:export init []
  (rf/dispatch [::initialize])
  (mount))
