(ns server.core
  (:require ["express"]
            ["socket.io" :as socket-io]
            ["path"]))

(enable-console-print!)

(println "Starting server...")

(def app (express))

(def server
  (let [port (or (-> js/process .-env .-PORT) 5000)]
    (.listen app port (fn [] (println (str "Example app listening on port " port "!"))))))

(def io (socket-io server))

(defn serve-client
  [req resp]
  (.sendFile resp (.resolve path (str js/__dirname "../../../build/index.html"))))

(.get app "/" serve-client)

(.set io "origins" "*:*")

(defn setup-connection
  "Sets up a new socket connection."
  [socket]
  ; TODO -- this doesn't appear to be called yet
  (println "Totally setting up a new socket connection."))

(.on io "connection" setup-connection)