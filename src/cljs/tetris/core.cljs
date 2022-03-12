(ns tetris.core)

(enable-console-print!)

(println "Hello, World.")

(let [paragraph (.createElement js/document "h2")]
  (set! (.-textContent paragraph) "Clojurescript Tetris!")
  (.appendChild (.getElementById js/document "app") paragraph))

;; --- setup ---

(defn add-ui
  []
  (let [lines-ui (.createElement js/document "h3")]
    (set! (.-id lines-ui) "lines")
    (set! (.-textContent lines-ui) "LINES - 000")
    (.appendChild (.getElementById js/document "app") lines-ui)))

(defn add-canvas
  []
  (let [canvas (.createElement js/document "canvas")] 
        (set! (.-id canvas) "board")
        (set! (.-width canvas) 210)
        (set! (.-height canvas) 420)
        (set! (.-outline (.-style canvas)) "2px solid darkgrey")
        (set! (.-outlineOffset (.-style canvas)) "2px")
        (.appendChild (.getElementById js/document "app") canvas)))

(defn new-grid
  []
  (into [] (take 200 (repeat 0))))

(def blocks
  {:t {:indices [14 4 13 15] :color-id 1}
   :j {:indices [14 3 13 15] :color-id 2}
   :z {:indices [4 3 14 15] :color-id 3}
   :o {:indices [4 5 14 15] :color-id 1}
   :s {:indices [4 5 13 14] :color-id 2}
   :l {:indices [14 5 13 15] :color-id 3}
   :i {:indices [4 3 5 6] :color-id 1}})

(defn new-block
  []
  (let [block (get blocks (rand-nth (keys blocks)))
        indices (:indices block)]
    {:indices indices :prev-indices indices :color-id (:color-id block)}))

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
  [grid]
  (let [canvas (.getElementById js/document "board")
        ctx (.getContext canvas "2d")]
    (doseq [index (range 0 (count grid))]
      (let [coords (index-to-coord index)]
        (if (= (nth grid index) 1)
          (set! (.-fillStyle ctx) "lime")
          (if (= (nth grid index) 2)
            (set! (.-fillStyle ctx) "blue")
            (if (= (nth grid index) 3)
              (set! (.-fillStyle ctx) "red")
              (set! (.-fillStyle ctx) "darkgrey"))))
        (let [cell-size (- (/ (.-width canvas) 10) 1)]
          (.fillRect 
           ctx 
           (coord-to-position cell-size (:x coords)) 
           (coord-to-position cell-size (:y coords)) 
           cell-size 
           cell-size))))))

(defn format-line-count
  [line-count]
  (if (< line-count 10)
    (str "00" line-count)
    (if (< line-count 100)
      (str "0" line-count)
      (str line-count))))

(defn render-stats
  [stats]
  (let [lines-ui (.getElementById js/document "lines")]
    (set! (.-textContent lines-ui) (str "LINES-" (format-line-count (:lines stats))))))
  
(defn render-board
  [grid stats]
  (render-stats stats)
  (render-grid grid))

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
  [grid block stats step-timeout timestep]
  (let [input-listener (listen-for-input grid block stats step-timeout)]
        (js/setTimeout #(.removeEventListener js/window "keydown" input-listener) timestep)))

(defn update-loop
  [grid block stats]
  (.log js/console "update")
  (let [updated-grid (apply-block-to-grid grid block)
        timestep 1000]
    (render-board updated-grid stats)
    (let [step-timeout (js/setTimeout #(step updated-grid block stats) timestep)]
      (run-input-listener updated-grid block stats step-timeout timestep))))

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
           completed-rows (if is-row-complete (conj completed-rows row) completed-rows)]
       (check-for-completed-rows (drop 10 grid) (inc row) completed-rows)))))

(defn drop-rows-above
  [grid stats completed-rows]
  (if (empty? completed-rows)
    (do
      (render-board grid stats)
      (js/setTimeout #(update-loop grid (new-block) stats) 1000))
    (drop-rows-above 
     (let [row (first completed-rows)]
      (into [] (concat (take 10 (repeat 0)) (concat (subvec grid 0 (* row 10)) (subvec grid (+ (* row 10) 10)))))) 
     {:lines (inc (:lines stats))}
     (rest completed-rows))))

(defn clear-completed-rows
  ([grid stats completed-rows]
   (clear-completed-rows grid stats completed-rows 0))
  ([grid stats completed-rows column-pair]
   (if (= column-pair 5)
     (drop-rows-above grid stats completed-rows)
     (let [grid-with-cleared-column 
           (reduce (fn [grid row]
                     (apply-indices-to-grid 
                      grid 
                      [(coord-to-index {:x (- 4 column-pair) :y row}) (coord-to-index {:x (+ 5 column-pair) :y row})] 
                      0))
                   grid
                   completed-rows)]
       (render-board grid-with-cleared-column stats)
       (js/setTimeout #(clear-completed-rows grid-with-cleared-column stats completed-rows (inc column-pair)) 200)))))

(defn place-block
  [grid stats]
  (let [completed-rows (check-for-completed-rows grid)]
    (if (empty? completed-rows)
      (do 
        (.log js/console "no completed rows")
        (update-loop grid (new-block) stats)
        )
      (do
        (.log js/console "COMPLETED ROWS")
        (clear-completed-rows grid stats completed-rows)))))

(defn check-new-block-coords
  [grid block stats new-block-coords is-moving-down]
  (if (valid? grid block new-block-coords)
    (let [new-block (update-block block new-block-coords)]
      (update-loop grid new-block stats))
    (if is-moving-down
      (place-block grid stats)
      (update-loop grid block stats))))

(defn step
  [grid block stats]
  (let [new-coords (new-coords-from-input "ArrowDown" block)]
    (check-new-block-coords grid block stats new-coords true)))

;; --- listen for input ---

(def new-coords-fns
  ;; could maybe change this to return a partial, which gets used in new-coords-from-input
  {:ArrowLeft  (fn [coords] (map (fn [coord] (update coord :x #(- %1 1))) coords))
   :ArrowRight (fn [coords] (map (fn [coord] (update coord :x #(+ %1 1))) coords))
   :ArrowDown (fn [coords] (map (fn [coord] (update coord :y #(+ %1 1))) coords))
   :x (fn [coords] 
        (let [x-pivot (:x (first coords)) ; first coord is the one to pivot about 
              y-pivot (:y (first coords))]
          (map (fn [coord] (let [x-delta (- (:x coord) x-pivot)
                                 y-delta (- (:y coord) y-pivot)]
                             {:x (- x-pivot y-delta) :y (+ y-pivot x-delta)}))
               coords)))
   :z (fn [coords] 
        (let [x-pivot (:x (first coords)) ; first coord is the one to pivot about 
              y-pivot (:y (first coords))]
          (map (fn [coord] (let [x-delta (- (:x coord) x-pivot)
                                 y-delta (- (:y coord) y-pivot)]
                             {:x (+ x-pivot y-delta) :y (- y-pivot x-delta)}))
               coords)))})

(defn new-coords-from-input
  [key-code block]
  ((get new-coords-fns (keyword key-code) (fn[coords] coords))
   (indices-to-coords (:indices block))))

(defn handle-input
  [grid block stats event]
  (let [key-code (.-key event)
        new-coords (new-coords-from-input key-code block)]
    (check-new-block-coords grid block stats new-coords (= key-code "ArrowDown"))))

(defn listen-for-input
  "if key pressed: clear prev step-timeout, handle input, add next input listener, call step, remove self"
  [grid block stats step-timeout]
  (let [input-listener 
        (fn [event] 
          (.clearTimeout js/window step-timeout)
          (handle-input grid block stats event))]
    (.addEventListener js/window "keydown" input-listener (clj->js {:once true}))
    input-listener))

;; --- main ---

(defn play-game
  []
  (add-ui)
  (add-canvas)
  (let [stats {:lines 0}
        grid (new-grid)
        block (new-block)]
    (update-loop grid block stats)))

(play-game)
