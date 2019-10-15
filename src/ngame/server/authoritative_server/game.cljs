(ns ngame.server.authoritative-server.game
  (:require ["phaser" :as phaser]))

(defn main-scene [game]
  (nth game.scene.scenes 0))

(defn preload-fn [])

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
