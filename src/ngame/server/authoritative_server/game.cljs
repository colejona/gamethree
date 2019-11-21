(ns ngame.server.authoritative-server.game
  (:use [ngame.common.phaser :only [main-scene add-image load-image]])
  (:require ["phaser" :as phaser]))

(defn preload-fn []
  (load-image js/game "player" "assets/red_square.png"))

(defn create-fn [])

(defn update-fn [])

(defn log
  [message]
  (js/console.log (str "[authoritative-server] " message)))

(declare start)

(defn init []
  (log "init")
  (start))

(defn start []
  (log "start")
  (set! js/game (js/Phaser.Game. (clj->js {:type js/Phaser.HEADLESS
                                           :width 600
                                           :height 480
                                           :scene {:preload preload-fn
                                                   :create create-fn
                                                   :update update-fn}
                                           :autoFocus false}))))

(defn stop []
  (log "stop")
  (.destroy js/game true))

(defn ^:export add-player
  [id player-established-callback]
  (let [x 300
        y 240]
    (log (str "Adding player `" id "` at " x "," y))
    (add-image js/game x y "player")
    (player-established-callback x y)))
