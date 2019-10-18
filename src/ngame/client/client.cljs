(ns ngame.client.client
  (:require [clojure.string :as string])
  (:require ["phaser" :as phaser])
  (:require ["socket.io-client" :as socket]))

(defn main-scene [game]
  (nth game.scene.scenes 0))

(defn preload-fn []
  (-> (main-scene js/game) .-load (.image "logo" "assets/phaser.png")))

(defn create-fn []
  (-> (main-scene js/game) .-add (.image 300 240 "logo")))

(defn log
  [message]
  (js/console.log (str "[client] " message)))

(declare start)

(defn init []
  (log "init")
  (start))

(defn start []
  (log "start")
  (def io (socket (str
      (if (= js/window.location.protocol "https:") "wss" "ws")
      (str "://" js/window.location.hostname ":")
      (if (string/includes? js/window.location.host ":")
        (if (boolean (exists? js/process.env.PORT))
            js/process.env.PORT
            "3000")
        "")
      )))
  (set! js/game (js/Phaser.Game. (clj->js {:type js/Phaser.AUTO
                                           :width 600
                                           :height 480
                                           :scene {:preload preload-fn
                                                   :create create-fn}})))
  (.on io "connect"
    (fn [io-socket]
        (.on io "wave"
            (fn [event]
                (log event)
                (.emit io "wave-back" "Hello World"))))))

(defn stop []
  (log "stop")
  (.destroy js/game true))
