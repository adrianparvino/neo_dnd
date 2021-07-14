(ns dnd.dm.notes
  (:require [re-frame.core :as rf]))

(rf/reg-event-fx
 ::initialize
 (fn [{:keys [db]} _]
   {:fx [[:fetch {:method                 :get
                  :url                    "/api/session/me/notes"
                  :response-content-types {#"application/.*json" :json}
                  :on-success             [::notes-set]}]]}))

(rf/reg-event-db
 ::notes-set
 (fn [db [_ {:keys [body]}]]
   (assoc db ::notes body)))

(rf/reg-event-fx
 ::add-note
 (fn [{:keys [db]} [_ content]]
   {:fx [[:fetch {:method                 :post
                  :url                    "/api/session/me/notes"
                  :request-content-type   :json
                  :response-content-types {#"application/.*json" :json}
                  :body                   content
                  :on-success             [::note-added]}]]}))

(rf/reg-event-db
 ::note-added
 (fn [db [_ {:keys [body]}]]
   (update db ::notes conj body)))

(rf/reg-sub
 ::notes
 (fn [db _]
   (::notes db)))

(defn add-note []
  (letfn [(on-submit [e]
            (.preventDefault e)
            (rf/dispatch [::add-note (-> e .-target js/FormData. .entries js/Object.fromEntries (js->clj :keywordize-keys true))]))]
    [:form {:on-submit on-submit}
     [:input {:on-submit on-submit :name "content"}]
     [:button {:class [:rounded :m-auto :border :shadow-lg :p-4 :flex-initial]} "Add note"]]))

(defn notes []
  (let [notes @(rf/subscribe [::notes])]
    [:div {:class [:px-4 :gap-4 :flex :flex-col :flex-wrap :w-full]}
     [:h1 {:class [:text-xl :font-bold]} "Notes"]
     [:div
      (for [{:keys [content]} notes]
        [:div {:class [:border :p-4]}
         [:p content]])
      [add-note]]]))
