(ns ngame.client.client
    (:require ["phaser" :as phaser]))

(defn preload-fn []
    (. (. (nth js/game.scene.scenes 0) -load) image "logo" "assets/phaser.png"))

(defn create-fn []
    (. (. (nth js/game.scene.scenes 0) -add) image 300 240 "logo"))

(defn init []
    (set! js/game (js/Phaser.Game. (js-obj
        "type" js/Phaser.AUTO
        "width" 600
        "height" 480
        "scene" (js-obj
            "preload" preload-fn
            "create" create-fn )))))
