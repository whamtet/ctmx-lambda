(ns serverless.examples.active-search
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

(defn tr [{:keys [name email]}]
  [:tr [:td name] [:td email]])

(defcomponent ^:endpoint active-search [req search]
  (let [search (.toLowerCase search)]
    (->> data
      (filter #(-> % :name .toLowerCase (.includes search)))
      (map tr))))

(make-routes
  "/demo"
  (fn [req]
    [:div
      [:h3 "Search Contacts"]
      [:input.mr
        {:type "text" :name "search" :placeholder "Search e.g. Joe"
         :hx-post "active-search" :hx-trigger "keyup changed delay:500ms"
         :hx-target "#search-results"}]
      [:span.htmx-indicator
          [:img {:src "../../bars.svg"}] " Searching..."]
      [:table.table
        [:thead
          [:tr [:th "Name"] [:th "Email"]]]
        [:tbody#search-results
          (active-search req "")]]]))
