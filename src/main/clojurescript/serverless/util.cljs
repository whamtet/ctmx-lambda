(ns serverless.util
  (:require
    [cljs.nodejs :as nodejs]
    clojure.pprint))

(nodejs/enable-util-print!)

(defn pprint [& x]
  (with-out-str
    (clojure.pprint/pprint x)))
