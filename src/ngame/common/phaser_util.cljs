(ns ngame.common.phaser-util)

(defn main-scene [game]
  (nth game.scene.scenes 0))

(defn add-image
  [game x y id]
  (-> (main-scene game) .-add (.image x y id)))

(defn load-image
  [game id path]
  (-> (main-scene game) .-load (.image id path)))
