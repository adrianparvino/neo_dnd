(ns dnd.core
  (:require [dnd.routes :as routes]
            [reagent.dom :as rdom]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 ::initialize
 (fn [_ _]
   {:fx [[:dispatch [::routes/initialize]]
         [:dispatch [:dnd.overview/initialize]]]}))

(defn root []
  (let [view @(rf/subscribe [::routes/view])]
    [:div {:class [:flex :flex-col :w-screen :h-screen :overflow-hidden]}
     [:div {:class [:p-4 :w-full :flex :flex-row :gap-4]}
      [:div {:class [:text-2xl :text-blue-500 :font-bold]} "Forest and Felines"]
      [:div {:class [:my-auto]} "Overview"]
      [:div {:class [:my-auto]} "Initiative Tracker"]
      [:div {:class [:my-auto]} "Notes"]]
     [view]]))

(defn mount []
  (rf/clear-subscription-cache!)
  (rdom/render [root] (js/document.getElementById "react-app")))

(defn ^:export init []
  (rf/dispatch [::initialize])
  (mount))
