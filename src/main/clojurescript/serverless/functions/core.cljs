(ns serverless.functions.core
  (:require
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
