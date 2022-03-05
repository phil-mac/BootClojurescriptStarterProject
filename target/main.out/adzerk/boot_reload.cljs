(ns adzerk.boot-reload (:require [adzerk.boot-reload.client :as client]))
(client/connect "ws://localhost:45535" {:ws-host nil, :asset-host nil, :on-jsload (fn* [] (+))})