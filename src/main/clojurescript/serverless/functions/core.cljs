(ns serverless.functions.core
  (:require
    [ctmx.lambda.middleware :as middleware]
    [serverless.util :as util]))

(defn hello [event ctx cb]
  (cb nil (clj->js
            {:statusCode 200
             :headers    {"Content-Type" "text/html"
                          "Access-Control-Allow-Origin" "*"
                          "Access-Control-Allow-Credentials" true}
             :body       (str "<pre>" (util/pprint (js->clj event) (middleware/e->ring event)) "</pre>")})))
