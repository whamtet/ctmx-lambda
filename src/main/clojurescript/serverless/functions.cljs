(ns serverless.functions
  (:require
    [cljs.nodejs :as nodejs]
    [serverless.functions.core :as core])
  (:require-macros
    [serverless.static :as static]))

(set! (.-exports js/module) #js
    {:hello core/hello})

(static/export-to-serverless core/hello)
