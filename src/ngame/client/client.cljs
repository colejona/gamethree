(ns ngame.client.client
    (:require ["phaser" :as phaser]))

(defn main-scene [game]
    (nth game.scene.scenes 0))

(defn preload-fn []
    (-> (main-scene js/game) .-load (.image "logo" "assets/phaser.png")))

(defn create-fn []
    (-> (main-scene js/game) .-add (.image 300 240 "logo")))

(defn init []
    (set! js/game (js/Phaser.Game. (js-obj
        "type" js/Phaser.AUTO
        "width" 600
        "height" 480
        "scene" (js-obj
            "preload" preload-fn
            "create" create-fn )))))
