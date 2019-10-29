(ns ngame.client.input)

(declare keyboard)

(defn key-is-down [key]
  (-> (key keyboard) .-isDown))

(defn add-key [scene key]
  (.input.keyboard.addKey scene key))

(defn setup-keys [scene]
  (let [keycode js/Phaser.Input.Keyboard.KeyCodes]
    { :w_key (add-key scene keycode.W)
      :a_key (add-key scene keycode.A)
      :s_key (add-key scene keycode.S)
      :d_key (add-key scene keycode.D)}))

(defn setup-input [scene]
  (def keyboard (setup-keys scene)))
