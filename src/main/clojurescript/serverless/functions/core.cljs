(ns serverless.functions.core
  (:require
    ctmx.form
    ctmx.rt
    [serverless.util :as util])
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defcomponent ^:endpoint hello [req my-name]
  [:div#hello "Hello " my-name])

(make-routes
  "/demo"
  (fn [req]
    [:div {:style "padding: 10px"}
     [:label {:style "margin-right: 10px"}
      "What is your name?"]
     [:input {:type "text"
              :name "my-name"
              :hx-patch "hello"
              :hx-target "#hello"}]
     (hello req "")]))

(defcomponent ^:endpoint form [req ^:path first-name ^:path last-name]
  [:form {:id id :hx-post "form"}
    [:input {:type "text" :name (path "first-name") :value first-name}] [:br]
    [:input {:type "text" :name (path "last-name") :value last-name}] [:br]
    (when (= ["Barry" "Crump"] [first-name last-name])
      [:div "A good keen man!"])
    [:input {:type "submit"}]])

(make-routes
  "/data-flow"
  (fn [req]
    (form req "Barry" "")))

(defcomponent table-row [req i person]
  [:tr
    [:td (:first-name person)] [:td (:last-name person)]])

(defcomponent table [req]
  [:table
    (ctmx.rt/map-indexed
      table-row
      req
      [{:first-name "Matthew" :last-name "Molloy"}
       {:first-name "Chad" :last-name "Thomson"}])])

(make-routes
  "/nesting-components"
  (fn [req]
    (table req)))

(defcomponent ^:endpoint click-div [req ^:int num-clicks]
  [:form {:id id :hx-get "click-div" :hx-trigger "click"}
    [:input {:type "hidden" :name "num-clicks" :value (inc num-clicks)}]
    "You have clicked me " num-clicks " times!"])

(make-routes
  "/parameter-casting"
  (fn [req]
    (click-div req 0)))


(defn add-customer [{:keys [first-name last-name customer-list]}]
  {:customer-list
    (update customer-list
      :customer
      #(conj (or % []) {:first-name first-name :last-name last-name}))})

(defn- text [name value]
  [:input {:type "text"
           :name name
           :value value
           :required true
           :style "margin-right: 5px"}])

(defcomponent customer [req i {:keys [first-name last-name]}]
  [:div
    [:input {:type "hidden" :name (path "first-name") :value first-name}]
    [:input {:type "hidden" :name (path "last-name") :value last-name}]])

(defcomponent ^:endpoint ^{:middleware add-customer} customer-list
  [req first-name last-name ^:json customer]
  [:form {:id id :hx-post "customer-list"}
    ;; display the nested params
    [:pre (-> req :params ctmx.form/json-params util/pprint)]
    [:br]

    (ctmx.rt/map-indexed serverless.functions.core/customer req customer)
    (text "first-name" first-name)
    (text "last-name" last-name)
    [:input {:type "submit" :value "Add Customer"}]])

(make-routes
  "/transforming"
  (fn [req]
    (customer-list req "Joe" "Stewart" [])))
