(ns ngame.client.client
  (:use [ngame.client.keyboard-input :only [setup-input]])
  (:use [ngame.client.movement :only [setup-movement]])
  (:use [ngame.common.phaser-util :only [main-scene add-image load-image]])
  (:require [ngame.common.constants :as const])
  (:require [clojure.string :as string])
  (:require ["phaser" :as phaser])
  (:require ["socket.io-client" :as socket]))

(defonce socket-ref
  (volatile! nil))

(defonce player-map {})

(defn log
  [message]
  (js/console.log (str "[client] " message)))

(defn movement-event
  [value]
  #js {:axis value
       :date (js/Date.now)})

(defn emit-key-event
  [event key]
  (.emit @socket-ref const/key-evt
         #js {:event event
              :key (name key)}))

(defn on-key-down
  [key]
  (emit-key-event const/key-down-evt key))

(defn on-key-up
  [key]
  (emit-key-event const/key-up-evt key))

(defn on-vertical-movement-change
  [value]
  (log (str "Vertical movement changed: " value)))

(defn on-horizontal-movement-change
  [value]
  (log (str "Horizontal movement changed: " value)))

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
         (log (str "Player established on server at " x "," y))
         (add-image js/game x y "player")
         (.emit io "client-player-ready" "Hello World"))))

(defn update-player-map!
  [game-state]
  (log player-map)
  (let [player-positions (get (js->clj game-state) "player-positions")]
    (reduce
      (fn [acc el]
        (let [player-id (key el)
              player-pos (nth el 1)]
          (if (contains? player-map player-id)
            (log (str "already seen this player " player-id))
            ((set! player-map (conj player-map {player-id player-pos}))
             (log (str "new player "  player-id))))))
      (seq player-positions))))

(defn listen-for-game-update
  [io-socket io]
  (.on io const/game-update-evt
    (fn [game-state]
      (update-player-map! game-state))))

(defn create-socket-listeners
  [io]
  (.once io "connect" (fn [socket]
    (listen-for-player-established socket io)
    (listen-for-game-update socket io))))

(defn preload-fn []
  (-> (main-scene js/game) .-load (.image "player" "assets/red_square.png")))

(defn setup-socket
  []
  (if (nil? @socket-ref)
    (let [io (socket (get-socket-address) (clj->js {:autoConnect false}))]
      (vreset! socket-ref io)
      (create-socket-listeners io)))
  (.open @socket-ref))

(defn setup-key-event-emitters []
  (setup-input (main-scene js/game) on-key-down on-key-up))

(defn setup-local-movement []
  (setup-movement (partial setup-input (main-scene js/game))
                  on-vertical-movement-change
                  on-horizontal-movement-change))

(defn create-fn []
  (setup-socket)
  (setup-key-event-emitters)
  (setup-local-movement))

(defn update-fn [])

(defn start []
  (log "start")

  (set! js/game (js/Phaser.Game. (clj->js {:type js/Phaser.AUTO
                                           :width const/width
                                           :height const/height
                                           :scene {:preload preload-fn
                                                   :create create-fn
                                                   :update update-fn}}))))

(defn init []
  (log "init")
  (start))

(defn stop []
  (log "stop")
  (.destroy js/game true)
  (when-some [socket @socket-ref]
    (.close socket)))
