(ns serverless.functions
  (:require
    ctmx.lambda.middleware
    serverless.examples.active-search
    serverless.examples.bulk-update
    serverless.examples.click-to-edit
    serverless.examples.click-to-load
    serverless.examples.delete-row
    serverless.examples.dialogs
    serverless.examples.infinite-scroll
    serverless.examples.inline-validation
    serverless.examples.modal-bootstrap
    serverless.examples.progress-bar
    serverless.examples.sortable
    serverless.examples.tabs-hateoas
    serverless.examples.value-select
    serverless.functions.core)
  (:require-macros
    [ctmx.lambda :as lambda]))

(lambda/export-to-serverless
  serverless.examples.active-search
  serverless.examples.bulk-update
  serverless.examples.click-to-edit
  serverless.examples.click-to-load
  serverless.examples.delete-row
  serverless.examples.dialogs
  serverless.examples.infinite-scroll
  serverless.examples.inline-validation
  serverless.examples.modal-bootstrap
  serverless.examples.progress-bar
  serverless.examples.sortable
  serverless.examples.tabs-hateoas
  serverless.examples.value-select
  serverless.functions.core)
