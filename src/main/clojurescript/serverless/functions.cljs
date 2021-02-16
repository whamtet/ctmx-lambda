(ns serverless.functions
  (:require
    [cljs.nodejs :as nodejs]
    clojure.pprint))

(nodejs/enable-util-print!)
(defonce dayjs (nodejs/require "dayjs"))

(defn pprint [& x]
  (with-out-str
    (clojure.pprint/pprint x)))

(defn hello [event ctx cb]
  (println ctx)
  (cb nil (clj->js
            {:statusCode 200
             :headers    {"Content-Type" "text/html"}
             :body       (str "<pre>" (pprint (js->clj event) (js->clj ctx)) "</pre>")})))


(defn now [event ctx cb]
  (println ctx)
  (cb nil (clj->js
            {:statusCode 200
             :headers    {"Content-Type" "text/html"}
             :body       (str "<h1>"(.format (dayjs.) "dddd, MMMM D, YYYY h:mm A")"</h1>")}))) ; call nodejs package

(set! (.-exports js/module) #js
    {:hello hello
     :now now})
