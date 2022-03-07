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
  (let [coords [4 13 14 15]]
    {:coords coords :prev-coords coords :color-id 1}))

;; --- utils ---

(defn index-to-coords
  [index]
  {:x (rem index 10) 
   :y (.floor js/Math (/ index 10))})

;; --- render ---

(defn coord-to-position
  [cell-size coord]
  (* (+ cell-size 1) coord))

(defn render-grid
  "applies grid to canvas"
  [grid canvas]
  (let [ctx (.getContext canvas "2d")]
    (doseq [index (range 0 (count grid))]
      (let [coords (index-to-coords index)]
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
(defn apply-coords-to-grid
  [grid coords value]
  (if (empty? coords)
      grid
      (assoc (apply-coords-to-grid grid (rest coords) value) (first coords) value)))

(defn apply-block-to-grid
  [grid block]
  (let [cleared-grid (apply-coords-to-grid grid (:prev-coords block) 0)]
    (apply-coords-to-grid cleared-grid (:coords block) (:color-id block))))

(defn step-block
  [block]
  {:coords (into [] (map #(+ %1 10) (:coords block))) 
   :prev-coords (:coords block) 
   :color-id (:color-id block)})

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

(defn check-new-block-coords
  [grid block new-block-coords]
  {:coords new-block-coords 
   :prev-coords (:coords block) 
   :color-id (:color-id block)})
  ;; (if (valid)
  ;;   (move)
  ;;   (if (hit-bottom)
  ;;     (place-it)
  ;;     (reject-move))))

(defn step
  [canvas grid block]
  (.log js/console "step")
  (update-loop canvas grid (step-block block)))

;; --- listen for input ---

(def new-coords-fns
  {:ArrowLeft  (fn [coords] (into [] (map #(- %1 1) coords)))
   :ArrowRight (fn [coords] (into [] (map #(+ %1 1) coords)))})

(defn handle-input
  [grid block canvas event]
  ;; (.log js/console (.-key event))
  (let [key-code (.-key event)
        new-coords ((get new-coords-fns (keyword key-code) (fn[coords] coords)) (:coords block))
        updated-block (check-new-block-coords grid block new-coords)]
    ;; replace this with calling variation of "check new block coords", to be able to "place-it" if needed, not always update like this
    (update-loop canvas grid updated-block)))

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
