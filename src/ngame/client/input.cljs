(ns ngame.client.input)

(defn setup-input [scene]
  { :w_key (.input.keyboard.addKey scene js/Phaser.Input.Keyboard.KeyCodes.W)
    :a_key (.input.keyboard.addKey scene js/Phaser.Input.Keyboard.KeyCodes.A)
    :s_key (.input.keyboard.addKey scene js/Phaser.Input.Keyboard.KeyCodes.S)
    :d_key (.input.keyboard.addKey scene js/Phaser.Input.Keyboard.KeyCodes.D)})
