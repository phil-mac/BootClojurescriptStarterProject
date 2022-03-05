(set-env!
 :source-paths #{"src/cljs"}
 :resource-paths #{"html"}

 :dependencies '[[adzerk/boot-cljs "1.7.228-2"]
                 [javax.xml.bind/jaxb-api "2.3.1"]
                 [pandeiro/boot-http "0.8.3"]
                 [adzerk/boot-reload "0.5.1"]]) ;; add boot-reload

(require '[adzerk.boot-cljs :refer [cljs]]
         '[pandeiro.boot-http :refer [serve]]
         '[adzerk.boot-reload :refer [reload]]) ;; make reload visible

;; define dev task as composition of subtasks
(deftask dev
  "Launch Development Environment"
  []
  (comp
   (serve :dir "target")
   (watch)
   (reload)
   (cljs)
   (target :dir #{"target"})))
