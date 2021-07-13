(ns dnd.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 :initialize
 (fn [_ _]
   {:db {:led false}}))

(rf/reg-event-db
 :toggle-led
 (fn [db _]
   (update db :led not)))

(rf/reg-sub
 :led
 (fn [db _]
   (:led db)))

(defn root []
  [:div {:class [:flex :flex-col :w-screen :h-screen]}
   [:div {:class [:p-4 :w-full :flex :flex-row :gap-4]}
    [:div {:class [:text-2xl :text-blue-500 :font-bold]} "Forest and Felines"]
    [:div {:class [:my-auto]} "Overview"]
    [:div {:class [:my-auto]} "Initiative Tracker"]
    [:div {:class [:my-auto]} "Notes"]]
   [:div {:class [:w-60 :p-4 :h-full]}
    "Test"]])

(defn mount []
  (rdom/render [root] (js/document.getElementById "react-app")))

(defn ^:export init []
  (rf/dispatch-sync [:initialize])
  (mount))
