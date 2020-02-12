(ns ngame.client.client
  (:use [ngame.client.keyboard-input :only [setup-input]])
  (:use [ngame.client.movement :only [setup-movement]])
  (:use [ngame.common.phaser-util :only [main-scene add-sprite load-image]])
  (:require [ngame.common.constants :as const])
  (:require [clojure.string :as string])
  (:require [clojure.data :as data])
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
         (add-sprite js/game x y "player")
         (.emit io "client-player-ready" "Hello World"))))

(defn add-player-to-map!
  [id pos game-object]
  (set! player-map (conj player-map {id {:position pos
                                         :game-object game-object}})))

(defn add-new-player!
  [id player-pos]
  (let [x (get player-pos "x")
        y (get player-pos "y")]
    (let [game-object (add-sprite js/game x y "player")]
      (add-player-to-map! id player-pos game-object))))

(defn update-player-pos!
  [player-id player-pos]
  (let [game-object (get (get player-map player-id) :game-object)
        x (get player-pos "x")
        y (get player-pos "y")]
    (set! (.-x game-object) x)
    (set! (.-y game-object) y)
    (add-player-to-map! player-id player-pos game-object)))

(defn clean-up-player!
  [player-id]
  (let [player-to-clean-up (get player-map player-id)
        game-object (get player-to-clean-up :game-object)]
    (if (not (nil? game-object))
      (.destroy game-object))
    (set! player-map (dissoc player-map player-id))))

(defn update-player-map!
  [game-state]
  (let [player-positions (get (js->clj game-state) "player-positions")]
    (doseq [el (seq player-positions)]
        (let [player-id (key el)
              player-pos (nth el 1)]
          (if (contains? player-map player-id)
            (update-player-pos! player-id player-pos)
            (add-new-player! player-id player-pos))))
    (let [player-diffs (data/diff (set (keys player-positions)) (set (keys player-map)))
          removed-players (nth player-diffs 1)]
      (doseq [player (seq removed-players)] (clean-up-player! player)))))

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
                                           :physics {:default "arcade"}
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
