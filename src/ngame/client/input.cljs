(ns ngame.client.input)

(defn set-handlers
  [keyboard keycode key key-down-handler key-up-handler]
  (.on keyboard (str "keydown-" keycode) #(key-down-handler key))
  (.on keyboard (str "keyup-" keycode) #(key-up-handler key)))

(defn setup-input [scene key-down-handler key-up-handler]
  (let [keyboard (.-keyboard (.-input scene))]
    (doseq [[letter key] [["W" :w_key] ["A" :a_key] ["S" :s_key] ["D" :d_key]]]
      (set-handlers keyboard letter key key-down-handler key-up-handler))))
