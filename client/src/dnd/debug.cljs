(ns dnd.debug
  (:require [re-frame.core :as rf]))

(rf/reg-event-fx
 :print
 (fn [_ event]
   {:fx [[:print event]]}))

(rf/reg-fx
 :print
 (fn [event] (print event)))
