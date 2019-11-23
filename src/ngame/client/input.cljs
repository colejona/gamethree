(ns ngame.client.input
  (:require [ngame.common.constants :as const]))

(defn set-handlers
  [keyboard letter key key-down-handler key-up-handler socket]
  (.on keyboard (str "keydown-" letter) #(key-down-handler key))
  (.on keyboard (str "keydown-" letter) #(.emit socket const/key-evt
                                                       #js {:event const/key-down-evt
                                                            :key letter}))
  (.on keyboard (str "keyup-" letter) #(key-up-handler key))
  (.on keyboard (str "keyup-" letter) #(.emit socket const/key-evt
                                                     #js {:event const/key-up-evt
                                                          :key letter})))

(defn setup-input [scene key-down-handler key-up-handler socket]
  (let [keyboard (.-keyboard (.-input scene))]
    (doseq [[letter key] [["W" :w_key] ["A" :a_key] ["S" :s_key] ["D" :d_key]]]
      (set-handlers keyboard letter key key-down-handler key-up-handler socket))))
