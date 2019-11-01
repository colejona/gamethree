(ns ngame.client.input)

(defn set-handlers
  [keyboard keycode key key-down-handler key-up-handler]
  (.on keyboard (str "keydown-" keycode) #(key-down-handler key))
  (.on keyboard (str "keyup-" keycode) #(key-up-handler key)))

(defn setup-input [scene key-down-handler key-up-handler]
  (let [keyboard (.-keyboard (.-input scene))]
    (def foo #(set-handlers keyboard %1 %2 key-down-handler key-up-handler))
    ;; There has to be a better way to do this. `apply`?
    (foo "W" :w_key)
    (foo "A" :a_key)
    (foo "S" :s_key)
    (foo "D" :d_key)))
