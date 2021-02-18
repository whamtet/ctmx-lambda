(ns serverless.examples.tabs-hateoas
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defn json [s]
  (-> s clj->js js/JSON.stringify))

(defn- tab [i val]
  [:a {:hx-get "content"
       :hx-vals (json {:tab-index i})
       :class (when (= i val) "selected")}
    "Tab " i])

(defcomponent ^:endpoint content [req ^:int tab-index]
  [:div {:hx-target "this"}
    [:div.tab-list
      (map #(tab % tab-index) (range 1 4))]
    [:div.tab-content
      "This is the content for tab " tab-index]])

(make-routes
  "/demo"
  (fn [req]
    (content req 1)))
