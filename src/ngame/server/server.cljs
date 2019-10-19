(ns ngame.server.server
  (:require ["express" :as express])
  (:require ["http" :as http])
  (:require ["serve-static" :as serve-static])
  (:require ["jsdom" :as jsdom])
  (:require ["socket.io" :as socket]))

(def app (express))
(def JSDOM (aget jsdom "JSDOM"))

(. app (use (serve-static "public" #js {:index "index.html"})))

(defonce server-ref
  (volatile! nil))

(defn log
  [message]
  (js/console.log (str "[server] " message)))

(defn create-socket-listeners [client-socket]
  (.emit client-socket "wave" "Server says hello!")
  (.on client-socket "wave-back"
  (fn [event]
    (log (str "Client says " event)))))

(defn -main []
  (log "starting server")
  (let [server (http/createServer #(app %1 %2))
        virtualConsole (new (aget jsdom "VirtualConsole"))
        io (socket server)]
    (.listen server (or js/process.env.PORT 3000))
    (.set io "origins" "*:*")
    (.on io "connection" create-socket-listeners)
    (vreset! server-ref server)

    (.sendTo virtualConsole js/console)
    (JSDOM.fromFile (str js/__dirname "/authoritative_server/index.html")
                    #js {:virtualConsole virtualConsole
                         :runScripts "dangerously"
                         :resources "usable"
                         :pretendToBeVisual true})))

(defn start
  "Hot code reload hook."
  []
  (log "start called")
  (-main))

(defn stop
  "Hot code reload hook to shut down resources."
  [done]
  (log "stop called")
  (when-some [srv @server-ref]
    (.close srv (fn [err]
                  (log "stop completed")
                  (done)))))
