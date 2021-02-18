(ns serverless.examples.sortable
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defn- content [items]
  (list*
    [:div.htmx-indicator "Updating..."]
    (for [item items]
      [:div
        [:input {:type "hidden" :name "order" :value item}]
        "Item " item])))

(defcomponent ^:endpoint sortable [req ^:ints order]
  (if (not-empty order)
    (content order)
    [:form#sortable {:hx-post "sortable" :hx-trigger "end"}
      (content (range 1 6))]))

(make-routes
  "/demo"
  (fn [req]
    (sortable req nil)))
