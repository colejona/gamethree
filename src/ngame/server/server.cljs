(ns ngame.server.server
  (:require [ngame.common.constants :as const])
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
  (let [dom-add-player-fn dom.window.ngame.server.authoritative_server.game.add-player
        id (.-id client-socket)
        on-player-established #(.emit client-socket "player-established" %1 %2)]
    (log (str "Adding player: " id))
    (dom-add-player-fn id on-player-established)
    (.on client-socket "client-player-ready" #(log (str "Client says " %)))
    (.on client-socket const/key-evt
         (fn [event]
           (log (str "Key event: " (js/JSON.stringify event)))))))

(defn remove-player
  [client-socket dom]
  (let [dom-remove-player-fn dom.window.ngame.server.authoritative_server.game.remove-player
        id (.-id client-socket)]
    (dom-remove-player-fn id)))

(defn schedule-game-broadcast
  [client-socket dom]
  (let [dom-get-game-state dom.window.ngame.server.authoritative_server.game.get-game-state]
    (js/setInterval
      (fn []
        (.emit client-socket const/game-update-evt (dom-get-game-state)))
      const/game-update-interval)))

(defn start-accepting-connections
  [io dom]
  (.on io "connect" (fn [socket]
    (set-up-new-player socket dom)
    (schedule-game-broadcast socket dom)
    (.on socket "disconnect" (fn [] (remove-player socket dom))))))

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
