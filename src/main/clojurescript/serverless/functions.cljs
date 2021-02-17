(ns serverless.functions
  (:require
    ctmx.lambda.middleware
    [serverless.functions.core :as core])
  (:require-macros
    [ctmx.lambda :as lambda]))

(lambda/export-to-serverless
  core/hello)
