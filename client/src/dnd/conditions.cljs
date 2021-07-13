(ns dnd.conditions
  (:require [clojure.string :refer [replace-first]]
            ["dnd5-srd/conditions.json" :as conditions-json]))

(def conditions
  (for [{:keys [desc] :as condition} (js->clj conditions-json :keywordize-keys true)]
    (assoc condition :desc (mapv #(replace-first % #"^\- " "") desc))))
