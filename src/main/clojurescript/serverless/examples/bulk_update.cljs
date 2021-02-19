(ns serverless.examples.bulk-update
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent defn-parse]]
    [ctmx.lambda :refer [make-routes]]))

(defn json [s]
  (-> s clj->js js/JSON.stringify))

(def init-data
  [{:name "Joe Smith" :email "joe@smith.org" :status "Inactive"}
   {:name "Angie MacDowell" :email "angie@macdowell.org" :status "Inactive"}
   {:name "Fuqua Tarkenton" :email "fuqua@tarkenton.org" :status "Inactive"}
   {:name "Kim Yee"	:email "kim@yee.org"	:status "Inactive"}])

(defn- set-status [status data i]
  (update data i assoc :status status))

(defn-parse update-data [{:keys [^:edn data ^:ints ids status]} _]
  {:ids (set ids)
   :data (reduce (partial set-status status) data ids)
   :status status})

(defn tr [ids action i {:keys [name email status]}]
  [:tr {:class (when (contains? ids i) action)}
   [:td [:input {:type "checkbox" :name "ids" :value i :checked (contains? ids i)}]]
   [:td name]
   [:td email]
   [:td status]])

(defcomponent ^:endpoint ^{:middleware update-data} update-form [req ids ^:json data status]
  [:form {:id id :hx-target "this"}
   [:input {:type "hidden" :name "data" :value (pr-str data)}]
   [:table
    [:thead
     [:tr [:th] [:th "Name"] [:th "Email"] [:th "Status"]]]
    [:tbody (map-indexed (partial tr ids status) data)]]
   [:button.mmargin
    {:hx-put "update-form"
     :hx-vals (json {:status "Active"})}
    "Activate"]
   [:button.mmargin
    {:hx-put "update-form"
     :hx-vals (json {:status "Inactive"})}
    "Deactivate"]])

(make-routes
  "/demo"
  (fn [req] (update-form req #{} init-data nil)))
