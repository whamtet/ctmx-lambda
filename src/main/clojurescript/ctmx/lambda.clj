(ns ctmx.lambda
  (:require
    [clojure.string :as string]
    [ctmx.core :as ctmx]))

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

(defmacro export-to-serverless [& methods]
  (let [all-endpoints (ctmx/extract-endpoints-root methods)]
    (->> all-endpoints
         keys
         (map str)
         spit-config)
    `(set! (.-exports js/module)
           (cljs.core/clj->js
             ~(into {}
                    (for [[f-name ns-name] all-endpoints]
                      [(keyword f-name)
                       `(ctmx.lambda.middleware/wrap-handler
                          ~(symbol (str ns-name) (str f-name)))]))))))
