(ns ngame.client.client
  (:use [ngame.client.movement :only [setup-movement]])
  (:require [clojure.string :as string])
  (:require ["phaser" :as phaser])
  (:require ["socket.io-client" :as socket]))

(defn main-scene [game]
  (nth game.scene.scenes 0))

(defn log
  [message]
  (js/console.log (str "[client] " message)))

(defn preload-fn []
  (-> (main-scene js/game) .-load (.image "player" "assets/red_square.png")))

(defn on-vertical-movement-change
  [value]
  (log (str "Vertical movement changed: " value)))

(defn on-horizontal-movement-change
  [value]
  (log (str "Horizontal movement changed: " value)))

(defn create-fn []
  (setup-movement (main-scene js/game) on-vertical-movement-change on-horizontal-movement-change))

(defn update-fn [])

(defn get-socket-address []
  (str (if (= js/window.location.protocol "https:") "wss" "ws")
       (str "://" js/window.location.hostname ":")
       (if (string/includes? js/window.location.host ":")
         (if (exists? js/process.env.PORT)
           js/process.env.PORT
           "3000"))))

(defn listen-for-player-established
  [io-socket io]
  (.on io "player-established"
       (fn [x y]
         (log "Player established on server.")
         (-> (main-scene js/game) .-add (.image x y "player")) ;; TODO: if already there, update its position
         (.emit io "client-player-ready" "Hello World"))))

(defn create-socket-listeners
  [io]
  (.once io "connect" #(listen-for-player-established % io)))

(defonce socket-ref
  (volatile! nil))

(defn start []
  (log "start")

  (set! js/game (js/Phaser.Game. (clj->js {:type js/Phaser.AUTO
                                           :width 600
                                           :height 480
                                           :scene {:preload preload-fn
                                                   :create create-fn
                                                   :update update-fn}})))

  (if (nil? @socket-ref)
    (let [io (socket (get-socket-address) (clj->js {:autoConnect false}))]
      (vreset! socket-ref io)
      (create-socket-listeners io)))

  (.open @socket-ref))

(defn init []
  (log "init")
  (start))

(defn stop []
  (log "stop")
  (.destroy js/game true)
  (when-some [socket @socket-ref]
    (.close socket)))
