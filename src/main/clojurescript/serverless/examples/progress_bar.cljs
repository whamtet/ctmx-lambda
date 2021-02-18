(ns serverless.examples.progress-bar
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defn json [s]
  (-> s clj->js js/JSON.stringify))

(defn- progress [width]
  [:div.progress
    [:div#pb.progress-bar {:style (str "width:" width "%")}]])

(defcomponent ^:endpoint start [req ^:float width]
  (let [width (if width (-> width (+ (rand 30)) (min 100)) 0)]
    (if (= width 100)
      [:div {:hx-target "this"}
        [:h3 "Complete"]
        (progress 100)
        [:button.btn {:hx-post "start"} "Restart"]]
      [:div {:hx-target "this"
             :hx-get "start"
             :hx-trigger "load delay:600ms"
             :hx-vals (json {:width width})}
        [:h3 "Running"]
        (progress width)])))

(make-routes
  "/demo"
  (fn [req]
    [:div {:style "height: 200px"}
      [:div {:hx-target "this"}
        [:h3 "Start Progress"]
        [:button.btn {:hx-post "start"} "Start Job"]]]))
