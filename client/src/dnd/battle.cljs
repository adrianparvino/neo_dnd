(ns dnd.battle
  (:require [phoenix.interop :refer [join]]
            [re-frame.core :as rf]
            ["react-bootstrap" :refer [Col ListGroup ListGroup.Item]]))

(rf/reg-event-fx
 ::initialize
 (fn [cofx _]
   (let [{:keys [db]} cofx
         {:keys [socket]} db]
     {:fx [[::join [socket "battle"]]]})))

(rf/reg-event-db
 ::set-characters
 (fn [db [_ characters]]
   (assoc db :characters characters)))

(rf/reg-fx
 ::join
 (fn [[socket channel]]
   (join socket channel {} (fn [{:keys [characters]}] (rf/dispatch [::set-characters characters])))))

(rf/reg-sub
 ::characters)

(defn battle []
  [:> Col {:md 4}
   [:> ListGroup
    [:> ListGroup.Item "Test"]
    [:> ListGroup.Item "Test"]
    [:> ListGroup.Item "Test"]]])
