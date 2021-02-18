(ns ctmx.lambda
  (:require
    [cljs.env :as env]
    [clojure.string :as string]
    [ctmx.core :as ctmx]))

(defn- mapmerge [f s]
  (apply merge (map f s)))

(defn get-lines []
  (->> (-> "serverless.format.yml" slurp (.split "\n"))
       (drop-while #(-> % .trim (.startsWith "hello:") not))
       (take-while #(-> % .trim not-empty))
       (split-at 3)
       (map #(string/join "\n" %))))

(defn section [[preface method-section] function-name]
  (let [preface (.replace preface "hello" function-name)
        method-section (.replace method-section "hello" function-name)]
    (string/join
      "\n"
      (list*
        preface
        (for [method ["get" "post" "put" "patch" "delete"]]
          (.replace method-section "get" method))))))

(defn spit-config [methods]
  (let [lines (get-lines)]
    (spit "serverless.yml"
          (.replace
            (slurp "serverless.format.yml")
            (string/join "\n" lines)
            (->> methods
                 (map #(section lines %))
                 (string/join "\n"))))))

(defn endpoints-in-ns [ns]
  (->> ns
       ((ctmx/namespaces))
       :defs
       vals
       (filter :endpoint)
       (map #(let [[a b] (-> % :name str (.split "/"))]
               [(symbol b) (symbol a)]))
       (into {})))

(defn static-endpoints [ns]
  (->> ns
       ((ctmx/namespaces))
       :defs
       vals
       (filter :html)
       (map (fn [{n :name}] [(keyword n) n]))))

(defmacro export-to-serverless [& namespaces]
  (let [flat-endpoints (mapmerge endpoints-in-ns namespaces)
        static-endpoint-map (into {} (mapcat static-endpoints namespaces))]
    (->> flat-endpoints
         keys
         (map str)
         spit-config)
    `(set! (.-exports js/module)
           (cljs.core/clj->js
             ~(into {:static `(ctmx.lambda.middleware/static ~static-endpoint-map)}
                    (for [[f-name ns-name] flat-endpoints]
                      [(keyword f-name)
                       `(ctmx.lambda.middleware/wrap-handler
                          ~(symbol (str ns-name) (str f-name)))]))))))

(defmacro make-routes [url f]
  `(def ~(-> url (.replaceAll "/" "") symbol (with-meta {:html true})) ~f))
