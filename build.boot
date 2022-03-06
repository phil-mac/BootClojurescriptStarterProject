(set-env!
 :source-paths #{"src/cljs"}
 :resource-paths #{"html"}

 :dependencies '[[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.473"]
                 [adzerk/boot-cljs "1.7.228-2"]
                 [javax.xml.bind/jaxb-api "2.3.1"]
                 [pandeiro/boot-http "0.8.3"]
                 [adzerk/boot-reload "0.5.1"]
                 [adzerk/boot-cljs-repl "0.3.3"]
                 [com.cemerick/piggieback "0.2.1"]
                 [weasel "0.7.0"]])

(require '[adzerk.boot-cljs :refer [cljs]]
         '[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-reload :refer [reload]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]])

;; define dev task as composition of subtasks
(deftask dev
  "Launch Development Environment"
  []
  (comp
   (serve :dir "target")
   (watch)
   ;; (reload) ;; commented out, since this was starting on the repl port before the Jetty web server
   (cljs-repl :secure true)
   (cljs)
   (target :dir #{"target"})))
