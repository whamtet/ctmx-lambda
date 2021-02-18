(ns serverless.examples.delete-raw
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(def data
  [{:name "Joe Smith" :email "joe@smith.org"}
   {:name "Angie MacDowell" :email "angie@macdowell.org"}
   {:name "Fuqua Tarkenton" :email "fuqua@tarkenton.org"}
   {:name "Kim Yee"	:email "kim@yee.org"}])

(defcomponent ^:endpoint tr [{:keys [request-method]} i {:keys [name email]}]
  (if (= :delete request-method)
    ""
    [:tr
      [:td name]
      [:td email]
      [:td "Active"]
      [:td [:button.btn.btn-danger {:hx-delete "tr"} "Delete"]]]))

(make-routes
  "/demo"
  (fn [req]
    [:table.table.delete-row-example
      [:thead
        [:tr [:th "Name"] [:th "Email"] [:th "Status"]]]
      [:tbody {:hx-confirm "Are you sure?" :hx-target "closest tr" :hx-swap "outerHTML swap:1s"}
        (ctmx.rt/map-indexed tr req data)]]))
