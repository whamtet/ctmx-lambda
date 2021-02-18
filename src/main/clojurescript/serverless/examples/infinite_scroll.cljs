(ns serverless.examples.infinite-scroll
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defn json [s]
  (-> s clj->js js/JSON.stringify))

(def src "0123456789ABCDEF")
(defn rand-str []
  (clojure.string/join (repeatedly 15 #(rand-nth src))))

(defn tr [i]
    [:tr
      (when (= 9 (mod i 10))
        {:hx-get "rows" :hx-trigger "revealed" :hx-swap "afterend" :hx-vals (json {:page (inc i)})})
      [:td "Agent Smith"]
      [:td (str "void" i "@null.org")]
      [:td (rand-str)]])

(defcomponent ^:endpoint rows [req ^:int page]
  (map tr (range page (+ 10 page))))

(make-routes
  "/demo"
  (fn [req]
    [:table
      [:thead
        [:tr [:th "Name"] [:th "Email"] [:th "ID"]]]
      [:tbody (rows req 0)]]))
