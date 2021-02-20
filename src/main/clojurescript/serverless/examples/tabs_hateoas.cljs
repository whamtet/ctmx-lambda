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

(defn- static-tab [i]
  [:a {:class (if (= 1 i) "tab selected" "tab")
       :_ (str "on click take .selected from .tab then add .d-none to .tab-content then remove .d-none from #content" i)}
   "Tab " i])

(defn- static-content [i]
  [:div {:class (if (= 1 i) "tab-content" "tab-content d-none")
         :id (str "content" i)}
   "This is the content for tab " i])

(make-routes
  "/demo2"
  (fn [req]
    [:div
     [:div.tab-list
      (map static-tab (range 1 4))]
     (map static-content (range 1 4))]))
