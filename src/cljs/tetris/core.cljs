(ns tetris.core)

(enable-console-print!)

(println "Hello, World.")

(let [paragraph (.createElement js/document "h2")]
  (set! (.-textContent paragraph) "Clojurescript Tetris")
  (.appendChild (.getElementById js/document "app") paragraph))

;; --- setup ---

(defn new-grid
  []
  (take 0 (repeat 0)))

(defn new-canvas
  []
  (let [canvas (.createElement js/document "canvas")] 
        (set! (.-width canvas) 210)
        (set! (.-height canvas) 420)
        (set! (.-outline (.-style canvas)) "2px solid darkgrey")
        (set! (.-outlineOffset (.-style canvas)) "2px")
        (.appendChild (.getElementById js/document "app") canvas)))

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
          (.fillRect ctx 
                     (coord-to-position cell-size (:x coords)) 
                     (coord-to-position cell-size (:y coords)) 
                     cell-size 
                     cell-size))))))
  
(defn render-board
  [grid canvas]
  (render-grid grid canvas))

;; --- update ---

(defn step
  [grid canvas test-index]
  (.log js/console "step")
  (let [updated-test-index (+ test-index 1)
        updated-grid (step-grid grid updated-test-index)]
    ;; (listen-for-input updated-test-index)
    (render-board updated-grid canvas)
    (let [timeout (js/setTimeout #(step %1 %2 %3) 1000 updated-grid canvas updated-test-index)]
      (let [input-fn (listen-for-input grid canvas test-index timeout)]
        ;; (js/setTimeout #(.log js/console "remove input fn: " input-fn) 1000)
        (js/setTimeout #(.removeEventListener js/window "keydown" input-fn) 1000))
      ;; return "input-fn" from listen-for-input, then remove in a settimeout here...
      ;; to ensure if you don't press anything there is never more than one listener
      )))

(defn step-grid
  [grid test-index]
  ;; (.log js/console "test index: " test-index)
  (take test-index (repeat 0)))

;; --- listen for input ---

(defn listen-for-input
  "if key is pressed, incs index, clears the prev step timeout, removes itself (by using {:once true}), and calls step"
  [grid canvas test-index timeout]
  (let [input-fn (fn [e] 
                   (do 
                     (.log js/console (.-key e) test-index)
                     (.clearTimeout js/window timeout)
                     (let [updated-test-index (+ test-index 1)]
                       (step grid canvas updated-test-index))))]
    (.addEventListener js/window "keydown" input-fn (clj->js {:once true}))
    input-fn))

;; --- main ---

(defn play-game
  []
  (let [grid (new-grid)
        test-index 0
        canvas (new-canvas)]
    (step grid canvas test-index)
    ;; (listen-for-input grid canvas)
    (.log js/console "play game")))

(play-game)



;; make a an event listener function that when called removes itself adds another
;; but it needs a reference to itself, which seems impossible? or not:
;; https://stackoverflow.com/questions/4936324/javascript-remove-an-event-listener-from-within-that-listener

;; (defn print-key
;;   [i]
;;   (fn [e] 
;;     (.log js/console (.-key e) i)))

;; (let [p1 (print-key 6)]
;;   (.addEventListener js/window "keydown" p1 (clj->js {:once true})))


;; (defn print-key-1
;;   [e]
;;   (.log js/console (.-key e) 1))

;; (defn print-key-2
;;   [e]
;;   (.log js/console (.-key e) 2))

;; (defn print-key-3
;;   [e]
;;   (.log js/console (.-key e) 3))

;; (defn print-key
;;   [i]
;;   (fn [e] (.log js/console (.-key e) i)))

;; (let [p1 (print-key 1)
;;       p2 (print-key 2)
;;       p3 (print-key 3)]
;;   (.addEventListener js/window "keydown" p1)
;;   (.addEventListener js/window "keydown" p2)
;;   (.addEventListener js/window "keydown" p3)
;;   (.removeEventListener js/window "keydown" p2))


;; (defn print-key
;;   [e i]
;;   (fn [e i] (.log js/console (.-key e) i)))

;; (let [listener1 (.addEventListener js/window "keydown" #(print-key %1 1))
;;       listener2 (.addEventListener js/window "keydown" #(print-key %1 2))])


