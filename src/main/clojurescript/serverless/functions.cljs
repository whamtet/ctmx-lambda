(ns serverless.functions
  (:require
    ctmx.lambda.middleware
    serverless.functions.core)
  (:require-macros
    [ctmx.lambda :as lambda]))

(lambda/export-to-serverless
  serverless.functions.core)
