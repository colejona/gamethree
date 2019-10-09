(ns ngame.client.client)

(defn log
  [message]
  (js/console.log (str "[client] " message)))

(declare start)

(defn init []
  (log "init")
  (start))

(defn start []
  (log "start"))

(defn stop []
  (log "stop"))
