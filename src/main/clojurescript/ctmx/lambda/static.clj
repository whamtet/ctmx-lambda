(ns ctmx.lambda.static
  (:require
    [clojure.java.shell :refer [sh]]
    [clojure.string :as string])
  (:import
    java.io.File))

(def ^:private in "console.log(require('./build/clojurescript/main/functions').static);")

(defn static-data []
  (->
    (sh "node" :in in)
    :out
    read-string))

(defn spit-pair [prefix [k v]]
  (let [segments (-> k
                     str
                     (.replace ":" "")
                     (.replace "-" "_")
                     (.replace "/" ".")
                     (.split "\\."))
        dir (File. (str prefix (string/join "/" (butlast segments))))]
    (.mkdirs dir)
    (spit (File. dir (str (last segments) ".html")) v)))

(defn -main [& args]
  (if-let [prefix (first args)]
    (let [prefix (if (.endsWith prefix "/") prefix (str prefix "/"))]
      (dorun
        (map (partial spit-pair prefix) (static-data)))
      (shutdown-agents))
    (println "Usage: lein run prefix")))
