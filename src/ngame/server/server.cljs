(ns ngame.server.server
    (:require [cljs.nodejs :as nodejs]))

(defonce express (nodejs/require "express"))
(defonce http (nodejs/require "http"))
(defonce serve-static (nodejs/require "serve-static"))

(def app (express))

(. app (use (serve-static "public" #js {:index "index.html"})))

(defn -main []
    (doto (.createServer http #(app %1 %2)) (.listen js/process.env.PORT)))
