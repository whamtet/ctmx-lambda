(ns serverless.examples.inline-validation
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defcomponent ^:endpoint email [req email]
  [:div {:hx-target "this" :hx-swap "outerHTML"}
    [:label.mr "Email Address"]
    [:input {:name "email" :value email :hx-get "email" :hx-indicator "#ind"}]
    [:img#ind.htmx-indicator {:src "img/bars.svg"}]
    (when-not (contains? #{"" "test@test.com"} email)
      [:div.error-message "That email is already taken.  Please enter another email."])])

(defn- input-group [label name]
  [:div.form-group
    [:label.mr label]
    [:input.form-control {:type "text" :name name}]])

(make-routes
  "/demo"
  (fn [req]
    [:div
      [:h3 "Signup Form"]
      [:form
        (email req "")
        (input-group "First Name" "first-name")
        (input-group "Last Name" "last-name")]]))
