(ns serverless.examples.bulk-update
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defn json [s]
  (-> s clj->js js/JSON.stringify))

(def data
  (atom
    [{:name "Joe Smith" :email "joe@smith.org" :status "Inactive"}
     {:name "Angie MacDowell" :email "angie@macdowell.org" :status "Inactive"}
     {:name "Fuqua Tarkenton" :email "fuqua@tarkenton.org" :status "Inactive"}
     {:name "Kim Yee"	:email "kim@yee.org"	:status "Inactive"}]))

(defn- set-status [status data i]
  (update data i assoc :status status))

(defn tr [ids action i {:keys [name email status]}]
  [:tr {:class (when (contains? ids i) action)}
    [:td [:input {:type "checkbox" :name "ids" :value i :checked (contains? ids i)}]]
    [:td name]
    [:td email]
    [:td status]])

(defcomponent ^:endpoint bulk-update [{:keys [request-method]} ^:ints ids status]
  (when (= :put request-method)
    (swap! data #(reduce (partial set-status status) % ids)))
  [:form {:id id}
    [:table
      [:thead
        [:tr
          [:th]
          [:th "Name"]
          [:th "Email"]
          [:th "Status"]]]
      [:tbody (map-indexed (partial tr (set ids) status) @data)]]
    [:a.btn.mmargin
      {:hx-put "bulk-update"
       :hx-vals (json {:status "Active"})
       :hx-target (hash ".")}
      "Activate"]
    [:a.btn.mmargin
      {:hx-put "bulk-update"
       :hx-vals (json {:status "Inactive"})
       :hx-target (hash ".")}
      "Deactivate"]])
