(ns dnd.re-frame-phoenix
  (:require [re-frame.core :as rf]
            [phoenix.interop]))

(def socket-atom (atom nil))
(def topics-atom (atom {}))

(rf/reg-fx
 :phoenix/connect
 (fn [[url connect-event]]
   (let [connect-cb #(rf/dispatch [connect-event])
         socket (phoenix.interop/connect url connect-cb)]
     (reset! socket-atom socket))))

(rf/reg-fx
 :phoenix/join
 (fn [[socket topic handlers payload joined-event]]
   (let [events (for [[event-name event] handlers]
                  [event-name #(rf/dispatch [event %])])
         connected-callbacks (when joined-event [#(rf/dispatch [joined-event :ok %])
                                                 #(rf/dispatch [joined-event :error %])
                                                 #(rf/dispatch [joined-event :timeout %])])
         handle (apply phoenix.interop/join socket topic events payload connected-callbacks)]
     (swap! topics-atom assoc topic handle))))

(rf/reg-fx
 :phoenix/push
 (fn [[topic event payload response-event]]
   (let [handle (topic @topics-atom)
         response-callbacks (when response-event [#(rf/dispatch [response-event :ok %])
                                                  #(rf/dispatch [response-event :error %])
                                                  #(rf/dispatch [response-event :timeout %])])]
     (apply phoenix.interop/push handle event payload response-callbacks))))

(rf/reg-fx
 :phoenix/leave
 (fn [[topic response-event]]
   (let [handle (topic @topics-atom)
         response-callbacks (when response-event [#(rf/dispatch [response-event :ok %])
                                                  #(rf/dispatch [response-event :error %])
                                                  #(rf/dispatch [response-event :timeout %])])]

     (apply phoenix.interop/leave handle response-callbacks))))
