(ns ngame.client.client
  (:use [ngame.client.input :only [setup-input]])
  (:require [clojure.string :as string])
  (:require ["phaser" :as phaser])
  (:require ["socket.io-client" :as socket])
  (:use [ngame.constants :only [movement-speed]]))

(defn main-scene [game]
  (nth game.scene.scenes 0))

(defn log
  [message]
  (js/console.log (str "[client] " message)))

(defn preload-fn []
  (-> (main-scene js/game) .-load (.image "logo" "assets/phaser.png")))

(def vertical-movement (atom 0))
(add-watch vertical-movement :vertical-movement-watcher
           (fn [key atom old-state new-state]
             (if (not= old-state new-state) (log (str "Vertical movement changed: " new-state)))))

(def horizontal-movement (atom 0))
(add-watch horizontal-movement :horizontal-movement-watcher
           (fn [key atom old-state new-state]
             (if (not= old-state new-state) (log (str "Horizontal movement changed: " new-state)))))

(defn request-movement
  [direction value]
  (case direction
    :vertical (if (not= @vertical-movement value) (reset! vertical-movement value))
    :horizontal (if (not= @horizontal-movement value) (reset! horizontal-movement value))))

(defn handle-key-down
  [key]
  (case key
    :w_key (request-movement :vertical (- movement-speed))
    :a_key (request-movement :horizontal (- movement-speed))
    :s_key (request-movement :vertical movement-speed)
    :d_key (request-movement :horizontal movement-speed)))

(defn handle-key-up 
  [key]
  (case key
    (:w_key :s_key) (request-movement :vertical 0)
    (:a_key :d_key) (request-movement :horizontal 0)))

(defn create-fn []
  (-> (main-scene js/game) .-add (.image 300 240 "logo"))
  (setup-input (main-scene js/game) handle-key-down handle-key-up))

(defn update-fn [])

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
