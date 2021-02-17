(ns serverless.functions.core
  (:require
    ctmx.rt
    [serverless.util :as util])
  (:require-macros
    [ctmx.core :as ctmx]))

(ctmx/defcomponent ^:endpoint subcomponent [req])

(ctmx/defcomponent ^:endpoint hello [req]
  subcomponent
  [:pre
   (util/pprint req)])
