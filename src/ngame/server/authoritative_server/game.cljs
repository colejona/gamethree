(ns ngame.server.authoritative-server.game
  (:require [ngame.common.constants :as const])
  (:use [ngame.common.phaser-util :only [main-scene add-image load-image]])
  (:require ["phaser" :as phaser])
  (:require ["datauri" :as datauri]))

(defonce used-positions #{})
(defonce max-position-count
  (* (/ const/width const/spacing)
     (/ const/height const/spacing)))

(defn preload-fn []
  (load-image js/game "player" "assets/red_square.png"))

(defn create-fn [])

(defn update-fn [])

(defn log
  [& args]
  (js/console.log (apply str (concat "[authoritative-server] " args))))

(declare start)

(defn init []
  (log "init")
  (js/fixMissingUrlFunctions (datauri.))
  (start))

(defn start []
  (log "start")
  (set! js/game (js/Phaser.Game. (clj->js {:type js/Phaser.HEADLESS
                                           :width const/width
                                           :height const/height
                                           :scene {:preload preload-fn
                                                   :create create-fn
                                                   :update update-fn}
                                           :autoFocus false}))))

(defn random-position []
  {:x (* (rand-int (/ const/width const/spacing)) const/spacing)
   :y (* (rand-int (/ const/width const/spacing)) const/spacing)})

(defn position-used? [pos]
  (contains? used-positions pos))

(defn new-position []
  (let [pos (random-position)]
    (if (position-used? pos)
      (new-position)
      pos)))

(defn place-player! []
  (if (>= (count used-positions) max-position-count)
    (set! used-positions #{}))
  (let [pos (new-position)]
    (set! used-positions (conj used-positions pos))
    pos))

(defn stop []
  (log "stop")
  (.destroy js/game true))

(defn ^:export add-player
  [id player-established-callback]
  (let [ {x :x, y :y}
         (place-player!) ]
    (log (str "Adding player `" id "` at " x "," y))
    (add-image js/game x y "player")
    (player-established-callback x y)))
