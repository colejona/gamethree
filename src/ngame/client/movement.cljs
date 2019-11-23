(ns ngame.client.movement
  (:use [ngame.client.input :only [setup-input]])
  (:use [ngame.common.constants :only [movement-speed]]))

(defn setup-movement
  [scene vertical-movement-handler horizontal-movement-handler socket]
  (let [up-movement (atom 0)
        down-movement (atom 0)
        left-movement (atom 0)
        right-movement (atom 0)
        vertical-movement (atom 0)
        horizontal-movement (atom 0)]

    (doseq [[direction opposite-direction composite-direction directional-factor]
            [[up-movement down-movement vertical-movement -1]
             [down-movement up-movement vertical-movement 1]
             [left-movement right-movement horizontal-movement -1]
             [right-movement left-movement horizontal-movement 1]]]
      (add-watch direction nil
                 (fn [key atom old-state new-state]
                   (if (not= old-state new-state)
                     (case new-state
                       0 (case opposite-direction
                           0 (reset! composite-direction 0)
                           (reset! composite-direction (* (- directional-factor) @opposite-direction)))
                       (reset! composite-direction (* directional-factor new-state)))))))

    (doseq [[composite-direction handler]
            [[vertical-movement vertical-movement-handler]
             [horizontal-movement horizontal-movement-handler]]]
      (add-watch composite-direction nil
                 (fn [key atom old-state new-state]
                   (if (not= old-state new-state) (handler new-state)))))

    (defn handle-key-down
      [key]
      (case key
        :w_key (reset! up-movement movement-speed)
        :a_key (reset! left-movement movement-speed)
        :s_key (reset! down-movement movement-speed)
        :d_key (reset! right-movement movement-speed)))

    (defn handle-key-up
      [key]
      (case key
        :w_key (reset! up-movement 0)
        :a_key (reset! left-movement 0)
        :s_key (reset! down-movement 0)
        :d_key (reset! right-movement 0)))

    (setup-input scene handle-key-down handle-key-up socket)))
