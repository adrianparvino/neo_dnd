(ns dnd.routes
  (:require [reitit.core :as r]
            [re-frame.core :as rf]
            [dnd.overview :refer [overview]]))

(def router
  (r/router
   [["/" {:view overview}]]))

(rf/reg-cofx
 ::current-path
 (fn [coeffects _]
   (assoc coeffects :current-path js/window.location.pathname)))

(rf/reg-event-fx
 ::initialize
 [(rf/inject-cofx ::current-path)]
 (fn [{:keys [current-path db]} _]
   {:db (assoc db :path current-path)}))

(rf/reg-sub
 ::view
 (fn [{:keys [path]} _]
   (-> (r/match-by-path router "/")
       :data
       :view)))
