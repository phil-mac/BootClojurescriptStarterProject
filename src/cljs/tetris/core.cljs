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
  (let [indices [4 12 13 14 15 16 22 23 24 25 26]]
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
  [grid block new-block-coords]
  (not-any? 
   #(let [x (:x %1)
          y (:y %1)
          index (coord-to-index %1)
          is-grid-index-available (and (< index 200) (> (nth grid index) 0))
          is-current-block-index (some (fn [i] (= i index)) (:indices block))]
      (or
       (or (< x 0) (> x 9))
       (or (< y 0) (> y 19))
       (and (not is-current-block-index) is-grid-index-available))) 
   new-block-coords))

(defn update-block 
  [block new-block-coords]
  {:indices (coords-to-indices new-block-coords)
     :prev-indices (:indices block) 
     :color-id (:color-id block)})

(defn check-for-completed-rows
  ([grid] (check-for-completed-rows grid 0 []))
  ([grid row completed-rows]
   (if (empty? grid)
     completed-rows
     (let [is-row-complete (not-any? #(= % 0) (take 10 grid))
           completed-rows (if is-row-complete (conj completed-rows row) completed-row)]
       (check-for-completed-rows (drop 10 grid) (inc row) completed-rows)))))

(defn drop-rows-above
  [canvas grid completed-rows]
    ;; move rows above down, then call this on a timeout:
    (update-loop canvas grid (new-block)))

(defn clear-completed-rows
  ([canvas grid completed-rows]
   (clear-completed-rows canvas grid completed-rows 0))
  ([canvas grid completed-rows column-pair]
   (if (= column-pair 5)
     (js/setTimeout #(drop-rows-above canvas grid completed-rows) 1000)
     (do
       (let [grid-with-cleared-column 
             (reduce (fn [grid row]
                       (apply-indices-to-grid 
                        grid 
                        [(coord-to-index {:x (- 4 column-pair) :y row}) (coord-to-index {:x (+ 5 column-pair) :y row})] 
                        0))
                     grid
                     completed-rows)]
         (render-board grid-with-cleared-column canvas)
         (js/setTimeout #(clear-completed-rows canvas grid-with-cleared-column completed-rows (inc column-pair)) 200)
         )))))

(defn place-block
  [canvas grid block]
  (let [completed-rows (check-for-completed-rows grid)]
    (if (empty? completed-rows)
      (do 
        (.log js/console "no completed rows")
        (update-loop canvas grid (new-block))
        )
      (do
        (.log js/console "COMPLETED ROWS")
        (clear-completed-rows canvas grid completed-rows)
        ))))

(defn check-new-block-coords
  [canvas grid block new-block-coords is-moving-down]
  (if (valid? grid block new-block-coords)
    (let [new-block (update-block block new-block-coords)]
      (update-loop canvas grid new-block))
    (if is-moving-down
      (place-block canvas grid block)
      (update-loop canvas grid block))))

(defn step
  [canvas grid block]
  (let [new-coords (new-coords-from-input "ArrowDown" block)]
    (check-new-block-coords canvas grid block new-coords true)))

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
    (check-new-block-coords canvas grid block new-coords (= key-code "ArrowDown"))))

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
