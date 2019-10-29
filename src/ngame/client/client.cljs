(ns ngame.client.client
  (:require [ngame.client.input :as game-input])
  (:require [clojure.string :as string])
  (:require ["phaser" :as phaser])
  (:require ["socket.io-client" :as socket]))

(defn main-scene [game]
  (nth game.scene.scenes 0))


(defn log
  [message]
  (js/console.log (str "[client] " message)))

(defn preload-fn []
  (-> (main-scene js/game) .-load (.image "logo" "assets/phaser.png")))

(defn create-fn []
  (-> (main-scene js/game) .-add (.image 300 240 "logo"))
  (game-input/setup-input (main-scene js/game)))

(defn update-fn []
  (if (game-input/key-is-down :w_key) (log "'W' key is down"))
  (if (game-input/key-is-down :a_key) (log "'A' key is down"))
  (if (game-input/key-is-down :s_key) (log "'S' key is down"))
  (if (game-input/key-is-down :d_key) (log "'D' key is down")))

(declare start)

(defn init []
  (log "init")
  (start))

(defn get-socket-address []
  (str (if (= js/window.location.protocol "https:") "wss" "ws")
       (str "://" js/window.location.hostname ":")
       (if (string/includes? js/window.location.host ":")
         (if (exists? js/process.env.PORT)
           js/process.env.PORT
           "3000"))))

(defn create-socket-listeners [io]
  (.on io "connect"
    (fn [io-socket]
      (.on io "wave"
        (fn [event]
          (log event)
          (.emit io "wave-back" "Hello World"))))))

(defn start []
  (log "start")
  (let [io (socket (get-socket-address))]
    (set! js/game (js/Phaser.Game. (clj->js {:type js/Phaser.AUTO
                                             :width 600
                                             :height 480
                                             :scene {:preload preload-fn
                                                     :create create-fn
                                                     :update update-fn}})))
    (create-socket-listeners io)))

(defn stop []
  (log "stop")
  (.destroy js/game true))
