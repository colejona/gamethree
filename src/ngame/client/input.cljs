(ns ngame.client.input)

(defn add-key [scene key]
  (.input.keyboard.addKey scene key))

(defn setup-input [scene]
  (let [keycode js/Phaser.Input.Keyboard.KeyCodes]
    { :w_key (add-key scene keycode.W)
      :a_key (add-key scene keycode.A)
      :s_key (add-key scene keycode.S)
      :d_key (add-key scene keycode.D)}))
