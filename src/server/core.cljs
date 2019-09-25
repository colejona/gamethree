(ns server.core
  (:require ["express"]))

(enable-console-print!)
(set! *warn-on-infer* true)

(println "Starting server...")
(let [app (express)
      port (or (-> js/process .-env .-PORT) 5000)]
  (.get app "/" (fn [req res] (.send res "Hello, world!")))
  (.listen app port (fn [] (println (str "Example app listening on port " port "!")))))
