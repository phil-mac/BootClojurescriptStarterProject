(ns tetris.core)

(enable-console-print!)

(println "Hello, World.")

(let [paragraph (.createElement js/document "h2")]
  (set! (.-textContent paragraph) "Clojurescript Tetris")
  (.appendChild (.getElementById js/document "app") paragraph))

;; --- setup ---

(defn new-grid
  []
  (into [] (take 200 (repeat 0))))

(defn new-canvas
  []
  (let [canvas (.createElement js/document "canvas")] 
        (set! (.-width canvas) 210)
        (set! (.-height canvas) 420)
        (set! (.-outline (.-style canvas)) "2px solid darkgrey")
        (set! (.-outlineOffset (.-style canvas)) "2px")
        (.appendChild (.getElementById js/document "app") canvas)))

(defn new-block
  []
  (let [indices [4 13 14 15]]
    {:indices indices :prev-indices indices :color-id 1}))

;; --- utils ---

(defn index-to-coord
  [index]
  {:x (rem index 10) 
   :y (.floor js/Math (/ index 10))})

(defn indices-to-coords
  [indices]
  (into [] (map index-to-coord indices)))

(defn coord-to-index
  [coord]
  (+ (* (:y coord) 10) (:x coord)))

(defn coords-to-indices
  [coords]
  (into [] (map coord-to-index coords)))

;; --- render ---

(defn coord-to-position
  [cell-size coord]
  (* (+ cell-size 1) coord))

(defn render-grid
  "applies grid to canvas"
  [grid canvas]
  (let [ctx (.getContext canvas "2d")]
    (doseq [index (range 0 (count grid))]
      (let [coords (index-to-coord index)]
        (if (= (nth grid index) 0)
          (set! (.-fillStyle ctx) "darkgrey")
          (set! (.-fillStyle ctx) "lime"))
        (let [cell-size (- (/ (.-width canvas) 10) 1)]
          (.fillRect 
           ctx 
           (coord-to-position cell-size (:x coords)) 
           (coord-to-position cell-size (:y coords)) 
           cell-size 
           cell-size))))))
  
(defn render-board
  [grid canvas]
  (render-grid grid canvas))

;; --- update ---
(defn apply-indices-to-grid
  [grid indices value]
  (if (empty? indices)
      grid
      (assoc (apply-indices-to-grid grid (rest indices) value) (first indices) value)))

(defn apply-block-to-grid
  [grid block]
  (let [cleared-grid (apply-indices-to-grid grid (:prev-indices block) 0)]
    (apply-indices-to-grid cleared-grid (:indices block) (:color-id block))))

(defn run-input-listener
  [grid block canvas step-timeout timestep]
  (let [input-listener (listen-for-input grid block canvas step-timeout)]
        (js/setTimeout #(.removeEventListener js/window "keydown" input-listener) timestep)))

(defn update-loop
  [canvas grid block]
  (.log js/console "update")
  (let [updated-grid (apply-block-to-grid grid block)
        timestep 1000]
    (render-board updated-grid canvas)
    (let [step-timeout (js/setTimeout #(step canvas updated-grid block) timestep)]
      (run-input-listener updated-grid block canvas step-timeout timestep))))

(defn valid?
  [grid new-block-coords]
  (let [x (:x (first new-block-coords))]
    ; convert block index to coords
    (.log js/console "x: " x " valid?: " (>= x 1))
    (>= x 1))
  ; get the actual xy coords from coords, confirm that none are out of bounds
  )

(defn hit-bottom?
  [grid new-block-coords]
  false)

(defn update-block 
  [block new-block-coords]
  {:indices (coords-to-indices new-block-coords)
     :prev-indices (:indices block) 
     :color-id (:color-id block)})

(defn check-new-block-coords
  [canvas grid block new-block-coords]
  (if (valid? grid new-block-coords)
    (let [new-block (update-block block new-block-coords)]
      (update-loop canvas grid new-block))
    (if (hit-bottom? grid new-block-coords)
      ;; (place-it)
      (update-loop canvas grid block)
      (update-loop canvas grid block))))

(defn step
  [canvas grid block]
  (let [new-coords (new-coords-from-input "ArrowDown" block)]
    (check-new-block-coords canvas grid block new-coords)))

;; --- listen for input ---

(def new-coords-fns
  ;; could maybe change this to return a partial, which gets used in new-coords-from-input
  {:ArrowLeft  (fn [coords] (map (fn [coord] (update coord :x #(- %1 1))) coords))
   :ArrowRight (fn [coords] (map (fn [coord] (update coord :x #(+ %1 1))) coords))
   :ArrowDown (fn [coords] (map (fn [coord] (update coord :y #(+ %1 1))) coords))})

(defn new-coords-from-input
  [key-code block]
  ((get new-coords-fns (keyword key-code) (fn[coords] coords))
   (indices-to-coords (:indices block))))

(defn handle-input
  [grid block canvas event]
  (let [key-code (.-key event)
        new-coords (new-coords-from-input key-code block)]
    (.log js/console "new-coords: " new-coords)
    (check-new-block-coords canvas grid block new-coords)))

(defn listen-for-input
  "if key pressed: clear prev step-timeout, handle input, add next input listener, call step, remove self"
  [grid block canvas step-timeout]
  (let [input-listener 
        (fn [event] 
          (.clearTimeout js/window step-timeout)
          (handle-input grid block canvas event))]
    (.addEventListener js/window "keydown" input-listener (clj->js {:once true}))
    input-listener))

;; --- main ---

(defn play-game
  []
  (let [canvas (new-canvas)
        grid (new-grid)
        block (new-block)]
    (update-loop canvas grid block)))

(play-game)
