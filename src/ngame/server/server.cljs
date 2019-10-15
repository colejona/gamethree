(ns ngame.server.server
  (:require ["express" :as express])
  (:require ["http" :as http])
  (:require ["serve-static" :as serve-static])
  (:require ["jsdom" :as jsdom]))

(def app (express))
(def JSDOM (aget jsdom "JSDOM"))

(. app (use (serve-static "public" #js {:index "index.html"})))

(defonce server-ref
  (volatile! nil))

(defn log
  [message]
  (js/console.log (str "[server] " message)))

(defn -main []
  (log "starting server")
  (let [server (http/createServer #(app %1 %2))
        virtualConsole (new (aget jsdom "VirtualConsole"))]

    (.listen server js/process.env.PORT)
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
    (.close srv
            #((log "stop completed")
              (done)))))
