(ns ctmx.lambda.middleware
  (:require
    [ctmx.render :as render]))

(defn key-map [f m]
  (zipmap (map f (keys m)) (vals m)))
(defn value-map [f m]
  (zipmap (keys m) (map f (vals m))))
(defn keywordize [m]
  (key-map keyword m))
(defn keywordize-lower [m]
  (key-map #(-> % .toLowerCase keyword) m))

(defn singularize-vals [m]
  (value-map #(if (-> % count (= 1)) (first %) %) m))

(defn make-params [body query-params]
  (let [body-lines
        (when body
          (as-> body $
                (.trim $)
                (.split $ "\n")
                (for [line $
                      :let [[k v] (.split line "=")]]
                  [(-> k js/decodeURIComponent keyword)
                   (js/decodeURIComponent v)])))]
    (singularize-vals
      (reduce
        (fn [m [k v]]
          (update m k #(conj (or % []) v)))
        (keywordize query-params)
        body-lines))))

(defn e->ring [event]
  (let [{:keys [headers requestContext body resource multiValueQueryStringParameters]} (-> event js->clj keywordize)
        headers-kw (keywordize-lower headers)]
    {:server-port (some-> headers-kw :x-forwarded-port js/Number)
     :server-name (:host headers-kw)
     :remote-addr (:x-forwarded-for headers-kw)
     :uri resource
     :scheme (if (-> headers-kw :x-forwarded-proto (= "https"))
               :https :http)
     :request-method (some-> requestContext (get "httpMethod") .toLowerCase keyword)
     :headers headers
     :params (make-params body multiValueQueryStringParameters)}))

(def cors-headers
  {"Access-Control-Allow-Origin" "*"
   "Access-Control-Allow-Credentials" true})
(defn merge-cors [r]
  (update r :headers #(merge cors-headers %)))

(defn rename-status [m]
  (update m :statusCode #(or % (:status m))))

(defn response [r]
  (-> r render/snippet-response rename-status merge-cors))

(defn wrap-handler [f]
  (fn [event ctx cb]
    (->> event e->ring f response clj->js (cb nil))))
