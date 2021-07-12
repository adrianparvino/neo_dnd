(ns dnd.home
  (:require ["react-bootstrap" :refer [Col Jumbotron Button Form Form.Control]]))

(defn home []
  [:> Col {:md 12}
   [:> Jumbotron {:class [:text-center]}
    [:h1 "Welcome to Forest and Felines"]
    [:p "Forest and Felines is a Dungeons and Dragons session manager"]
    [:p [:> Button {:variant "primary"} "New Session"]]
    [:hr]
    [:p "or"]
    [:p {:class [:mx-auto] :style {:width "10em"}} [:> Form.Control { :placeholder "Session ID"} ]]
    [:p [:> Button {:variant "secondary"} "Join An Existing Session"]]]])
