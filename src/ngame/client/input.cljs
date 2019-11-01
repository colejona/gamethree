(ns ngame.client.input)

(defn set-handlers
  [keyboard keycode key key-down-handler key-up-handler]
  (.on keyboard (str "keydown-" keycode) #(key-down-handler key))
  (.on keyboard (str "keyup-" keycode) #(key-up-handler key)))

(defn setup-input [scene key-down-handler key-up-handler]
  (let [keyboard (.-keyboard (.-input scene))]
    (set-handlers keyboard "W" :w_key key-down-handler key-up-handler)
    (set-handlers keyboard "A" :a_key key-down-handler key-up-handler)
    (set-handlers keyboard "S" :s_key key-down-handler key-up-handler)
    (set-handlers keyboard "D" :d_key key-down-handler key-up-handler)))
