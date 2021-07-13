(ns dnd.overview
  (:require [dnd.conditions :refer [conditions]]
            [dnd.equipment :refer [weapons]]
            [re-frame.core :as rf]))

(rf/reg-event-fx
 ::initialize
 (fn [{:keys [db]} _]
   {:db (assoc db ::show-details false)}))

(rf/reg-event-fx
 ::clicked-toggle-details
 (fn [{:keys [db]} _]
   {:db (update db ::show-details not)}))

(rf/reg-sub
 ::show-details
 (fn [db _]
   (::show-details db)))

(defn conditions-view []
  (let [show-details @(rf/subscribe [::show-details])]
    [:div {:class [:rounded :border :shadow-lg :p-4 :flex-initial]}
     [:h1 {:class [:text-2xl :font-bold]} "Conditions"
      [:a {:class [:ml-4] :on-click #(rf/dispatch [::clicked-toggle-details]) :href "#"}
       (if show-details
         "-"
         "+")]]
     [:div
      (for [{:keys [name desc]} conditions]
        [:div
         [:p {:class [:font-bold]} name]
         (when show-details
           (for [d desc]
             [:p {:class [:text-xs]} d]))])]]))

(defn weapons-view []
  [:div {:class [:rounded :border :shadow-lg :p-4 :flex-initial]}
   [:h1 {:class [:text-2xl :font-bold]} "Weapons"]
   [:table
    [:thead
     [:tr
      (for [h ["Name" "Cost" "Damage" "Properties"]]
        [:th {:class [:px-8]} h])]]
    (for [{:keys [name cost damage properties]} weapons]
      (let [{:keys [quantity unit]} cost
            {:keys [damage_dice]} damage]
        [:tr
         [:td name]
         [:td {:class [:text-center]} (str quantity unit)]
         [:td {:class [:text-center]} damage_dice]
         [:td (clojure.string/join ", " (map #(:name %) properties))]]))]])

(defn action-view []
  ; Taken from https://crobi.github.io/dnd5e-quickref/preview/quickref.html
  [:div {:class [:rounded :border :shadow-lg :p-4 :flex-initial]}
   [:h1 {:class [:text-2xl :font-bold]} "Actions In Combat"]
   [:h1 {:class [:text-xl :font-bold]} "Movement"]
   (for [action ["Move" "Stand up" "Grapple" "Climb" "High Jump" "Swim" "Long Jump" "Drop Prone" "Improvise" "Crawl" "Difficult Terrain"]]
     [:p action])
   [:h1 {:class [:text-xl :font-bold]} "Action"]
   (for [action ["Attack" "Disengage" "Use Shield" "Improvise" "Grapple" "Dodge"
                 "Hide" "Shove" "Escape" "Search" "Cast a spell" "Help"
                 "Ready" "Dash" "Use object" "Use class feature"]]
     [:p action])
   [:h1 {:class [:text-xl :font-bold]} "Bonus Action"]
   (for [action ["Offhand Attack" "Cast a spell" "Use class feature"]]
     [:p action])
   [:h1 {:class [:text-xl :font-bold]} "Reaction"]
   (for [action ["Opportunity attack" "Readied action" "Cast a spell"]]
     [:p action])])

(defn pace-view []
  [:div {:class [:rounded :border :shadow-lg :p-4 :flex-initial]}
   [:h1 {:class [:text-xl :font-bold]} "Travel Pace"]
   [:h2 "Fast"]
   [:p "400 feet / minute, -5 to Wisdom (Perception)"]
   [:h2 "Normal"]
   [:p "300 feet / minute"]
   [:h2 "Slow"]
   [:p "200 feet / minute, able to Stealth"]])

(defn overview []
  (let [show-details @(rf/subscribe [::show-details])]
    (if show-details
      [:div {:class [:flex :flex-col]}
       [conditions-view]]

      [:div {:class [:px-4 :gap-4 :flex :flex-row :flex-wrap :w-full :overflow-scroll]}
       [:div {:class [:flex :flex-col :gap-4]}
        [conditions-view]
        [pace-view]]
       [:div {:class [:flex :flex-col :gap-4]}
        [weapons-view]]
       [:div {:class [:flex :flex-col :gap-4]}
        [action-view]]])))
