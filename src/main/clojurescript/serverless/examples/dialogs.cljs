(ns serverless.examples.dialogs
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defcomponent ^:endpoint reply [{:keys [headers]}]
  [:div#response "You entered " (headers "hx-prompt")])

(make-routes
  "/demo"
  (fn [req]
    [:div
      [:button.btn.mb
        {:hx-post "reply"
         :hx-prompt "Enter a string"
         :hx-confirm "Are you sure?"
         :hx-target "#response"}
        "Prompt Submission"]
      [:div#response]]))
