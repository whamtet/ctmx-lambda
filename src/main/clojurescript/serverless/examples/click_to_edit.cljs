(ns serverless.examples.click-to-edit
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defn- input [type name value]
  [:input {:type type :name name :value value}])
(def text (partial input "text"))
(def emaili (partial input "email"))
(def hidden (partial input "hidden"))

(defcomponent ^:endpoint form-ro [req first-name last-name email]
  [:form {:id id :hx-target "this"}
    (hidden "first-name" first-name)
    [:div [:label "First Name"] ": " first-name]
    (hidden "last-name" last-name)
    [:div [:label "Last Name"] ": " last-name]
    (hidden "email" email)
    [:div [:label "Email"] ": " email]
    [:button.btn.margin
     {:hx-put "form-edit"}
      "Click To Edit"]])

(defcomponent ^:endpoint form-edit [req first-name last-name email]
  [:form {:id id :hx-put "form-ro" :hx-target "this"}
    [:div
      [:label.mr "First Name"]
      (text "first-name" first-name)]
    [:div.form-group
      [:label.mr "Last Name"]
      (text "last-name" last-name)]
    [:div.form-group
      [:label.mr "Email Address"]
      (emaili "email" email)]
    [:button.btn.margin "Save"]
    [:button.btn.margin {:hx-get "form-ro"} "Cancel"]])

(make-routes "/edit-demo"
  (fn [req]
    (form-ro req "Joe" "Blow" "joe@blow.com")))
