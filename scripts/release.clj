(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src/main/clojurescript"
    {:output-to "build/clojurescript/main/functions.js"
     :output-dir "build/clojurescript/main"
     :optimizations :simple
     :target :nodejs
     :main 'serverless.functions
     :pretty-print false
     :optimize-constants true
     :static-fns true
     :verbose true})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
