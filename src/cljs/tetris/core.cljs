(ns tetris.core)

(enable-console-print!)

(println "Hello, World.")

(let [paragraph (.createElement js/document "h2")]
  (set! (.-textContent paragraph) "Clojurescript Tetris!!!")
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
  [4 13 14 15])

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
          (set! (.-fillStyle ctx) "plum"))
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
(defn update-grid
  ; [0 0 0 0 0 0 0 1 1 1 0 0 ...]
  ; and
  ; (3 4 5 14)
  ; => 
  ; [0 0 0 1 1 1 0 1 1 1 0 0 ...]
  [grid pb block]
  ; Replace this nonsense with recursive solution
  (let [cleared-grid (assoc (assoc (assoc (assoc grid (first pb) 0) (second pb) 0) (nth pb 2) 0) (nth pb 3) 0)]
    (assoc (assoc (assoc (assoc cleared-grid (first block) 1) (second block) 1) (nth block 2) 1) (nth block 3) 1)))

(defn step-block
  [block]
  (into [] (map #(+ %1 10) block)))

(defn run-input-listener
  [grid block canvas step-timeout timestep]
  (let [input-listener (listen-for-input grid block canvas step-timeout)]
        (js/setTimeout #(.removeEventListener js/window "keydown" input-listener) timestep)))

(defn update-loop
  [canvas grid pb block]
  (.log js/console "update")
  (let [updated-grid (update-grid grid pb block)
        timestep 1000]
    (render-board updated-grid canvas)
    (let [step-timeout (js/setTimeout #(step canvas updated-grid block) timestep)]
      (run-input-listener updated-grid block canvas step-timeout timestep))))

(defn step
  [canvas grid block]
  (.log js/console "step")
  (let [updated-block (step-block block)]
    (update-loop canvas grid block updated-block)))

;; --- listen for input ---

(defn handle-input
  [grid block canvas event]
  (.log js/console (.-key event))
  (let [updated-block (step-block block)]
    (update-loop canvas grid block updated-block)))

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
    (update-loop canvas grid block block)))

(play-game)
