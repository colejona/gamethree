(ns ngame.server.server
    (:require [cljs.nodejs :as nodejs]))

(defonce express (nodejs/require "express"))
(defonce http (nodejs/require "http"))
(defonce serve-static (nodejs/require "serve-static"))

(def app (express))

(. app (get "/hello"
    (fn [req res] (. res (send "Hello World!")))))

(. app (use (serve-static "public" #js {:index "index.html"})))

(def -main
    (fn []
        (doto (.createServer http #(app %1 %2)) (.listen (or js/process.env.PORT 3000)))))
