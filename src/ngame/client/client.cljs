(ns ngame.client.client
  (:use [ngame.client.input :only [setup-input]])
  (:require [clojure.string :as string])
  (:require ["phaser" :as phaser])
  (:require ["socket.io-client" :as socket])
  (:use [ngame.common.constants :only [movement-speed]]))

(defn main-scene [game]
  (nth game.scene.scenes 0))

(defn log
  [message]
  (js/console.log (str "[client] " message)))

(defn preload-fn []
  (-> (main-scene js/game) .-load (.image "logo" "assets/phaser.png")))

(def up-movement (atom 0))
(def down-movement (atom 0))
(def left-movement (atom 0))
(def right-movement (atom 0))
(def vertical-movement (atom 0))
(def horizontal-movement (atom 0))

(defn make-movement-watcher
  [opposite-direction composite-direction directional-factor]
  (fn [key atom old-state new-state]
    (if (not= old-state new-state)
      (case new-state
        0 (case opposite-direction
            0 (reset! composite-direction 0)
            (reset! composite-direction (* (- directional-factor) @opposite-direction)))
        (reset! composite-direction (* directional-factor new-state))))))

(add-watch up-movement nil
           (make-movement-watcher down-movement vertical-movement -1))

(add-watch down-movement nil
           (make-movement-watcher up-movement vertical-movement 1))

(add-watch left-movement nil
           (make-movement-watcher right-movement horizontal-movement -1))

(add-watch right-movement nil
           (make-movement-watcher left-movement horizontal-movement 1))

(add-watch vertical-movement nil
           (fn [key atom old-state new-state]
             (if (not= old-state new-state) (log (str "Vertical movement changed: " new-state)))))

(add-watch horizontal-movement nil
           (fn [key atom old-state new-state]
             (if (not= old-state new-state) (log (str "Horizontal movement changed: " new-state)))))

(defn handle-key-down
  [key]
  (case key
    :w_key (reset! up-movement movement-speed)
    :a_key (reset! left-movement movement-speed)
    :s_key (reset! down-movement movement-speed)
    :d_key (reset! right-movement movement-speed)))

(defn handle-key-up
  [key]
  (case key
    :w_key (reset! up-movement 0)
    :a_key (reset! left-movement 0)
    :s_key (reset! down-movement 0)
    :d_key (reset! right-movement 0)))

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
