(ns ngame.server.server
  (:use [ngame.common.constants :only [hor-move-evt vert-move-evt]])
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

(defonce socket-ref
  (volatile! nil))

(defn log
  [message]
  (js/console.log (str "[server] " message)))

(defn set-up-new-player
  [client-socket dom]
  (.emit client-socket "player-established" 300 240)
  (.on client-socket "client-player-ready"
       (fn [event]
         (log (str "Client says " event))))
  (.on client-socket hor-move-evt
       (fn [event]
         (log (str "Horizontal movement: " (js/JSON.stringify event)))))
  (.on client-socket vert-move-evt
       (fn [event]
         (log (str "Vertical movement: " (js/JSON.stringify event))))))

(defn start-accepting-connections
  [io dom]
  (.on io "connect" #(set-up-new-player % dom)))

(defn -main []
  (log "starting server")
  (let [server (http/createServer #(app %1 %2))
        virtualConsole (new (aget jsdom "VirtualConsole"))
        io (socket server)]
    (.listen server (or js/process.env.PORT 3000))
    (.set io "origins" "*:*")
    (vreset! server-ref server)
    (vreset! socket-ref io)
    (.sendTo virtualConsole js/console)
    (-> (JSDOM.fromFile (str js/__dirname "/authoritative_server/index.html")
                        #js {:virtualConsole virtualConsole
                             :runScripts "dangerously"
                             :resources "usable"
                             :pretendToBeVisual true})
        (.then (fn [dom]
                 (start-accepting-connections io dom))))))

(defn start
  "Hot code reload hook."
  []
  (log "start called")
  (-main))

(defn stop
  "Hot code reload hook to shut down resources."
  [done]
  (log "stop called")
  (when-some [socket @socket-ref]
    (.close socket))
  (when-some [srv @server-ref]
    (.close srv (fn [err]
                  (log "stop completed")
                  (done)))))
