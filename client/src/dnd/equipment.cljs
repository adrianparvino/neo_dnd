(ns dnd.equipment
  (:require ["dnd5-srd/equipment.json" :as equipment-json]))

(def weapons
  (for [{:keys [equipment_category] :as equipment} (js->clj equipment-json :keywordize-keys true)
        :when (= equipment_category "Weapon")]
    equipment))
