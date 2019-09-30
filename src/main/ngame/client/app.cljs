(ns ngame.client.app
  (:require ["socket.io-client" :refer [io]]))

(enable-console-print!)

(defn log
  [message]
  (println (str "[gamethree][client] " message)))

(defn get-socket-address
  []
  (str (if (= (-> js/window .location .protocol) "https:") "wss" "ws")
       "://" (-> js/window .location .hostname)
       (if (-> js/window .location .host (.includes ":")) ":" (or (-> js/process .env .PORT) "5000"))))

(defn connect-to-server
  []
  (js/io. get-socket-address))

(defn init
  []
  (log "Client init.")
  (let [socket connect-to-server]
    (log "Connected to server!")))