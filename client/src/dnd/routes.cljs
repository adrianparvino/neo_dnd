(ns dnd.routes
  (:require [reitit.core :as r]
            [dnd.home :refer [home]]
            [dnd.battle :refer [battle]]))

(def router
  (r/router [["/" {:name :home :page [home]}]]))
