(ns starter-project.core)

(enable-console-print!)

(println "Hello, World.")

(let [paragraph (.createElement js/document "h2")]
  (set! (.-textContent paragraph) "And this is from core.cljs!!!")
  (.appendChild (.getElementById js/document "app") paragraph))

(.addEventListener js/window "keydown" (fn [e] (.log js/console (.-key e))))