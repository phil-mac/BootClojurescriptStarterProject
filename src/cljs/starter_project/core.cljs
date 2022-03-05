(ns starter-project.core)

(enable-console-print!)

(println "Hello, World.")

(let [paragraph (.createElement js/document "h2")]
  (set! (.-textContent paragraph) "And this is from core.cljs!")
  (.appendChild (.getElementById js/document "app") paragraph))

(defn new-canvas
  []
  (let [canvas (.createElement js/document "canvas")] 
        (set! (.-width canvas) 300)
        (set! (.-height canvas) 300)
        (.appendChild (.getElementById js/document "app") canvas)
        (.getContext canvas "2d")))

(defn render-lambda
  "adds a lambda logo to the canvas"
  [ctx]
    (let [x0 80
          y0 20]
      (set! (.-strokeStyle ctx) "blue")
      (set! (.-lineWidth ctx) 10)
      (.beginPath ctx)
      (.moveTo ctx x0 y0)
      (.lineTo ctx (+ x0 60) (+ y0 80))
      (.moveTo ctx (+ x0 30) (+ y0 40))
      (.lineTo ctx x0 (+ y0 80))
      (.stroke ctx)))

(let [ctx (new-canvas)]
  (render-lambda ctx))