(ns serverless.static
  (:require
    [clojure.string :as string]))

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
  (->> methods
       (map #(-> % str (.split "/") last))
       spit-config))

