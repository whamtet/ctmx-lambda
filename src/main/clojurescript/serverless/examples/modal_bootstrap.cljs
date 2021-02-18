(ns serverless.examples.modal-bootstrap
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :refer [defcomponent]]
    [ctmx.lambda :refer [make-routes]]))

(defcomponent ^:endpoint modal [req]
  (list
    [:div#modal-backdrop.modal-backdrop.fade {:style "display:block"}]
    [:div#modal.modal.fade {:tabindex -1 :style "display:block"}
      [:div.modal-dialog.modal-dialog-centered
        [:div.modal-content
          [:div.modal-header
            [:h5.modal-title "Modal title"]]
          [:div.modal-body
            [:p "Modal body text goes here."]]
          [:div.modal-footer
            [:button.btn.btn-secondary {:type "button" :onclick "closeModal()"}
              "Close"]]]]]))

(make-routes
  "/demo"
  (fn [req]
    modal ;; need to include modal in list of endpoints
    [:div
      [:button.btn.btn-primary
        {:hx-get "modal"
         :hx-target "#modals-here"
         :_ "on htmx:afterOnLoad wait 10ms then add .show to #modal then add .show to #modal-backdrop"}
        "Open Modal"]
      [:div#modals-here]]))
