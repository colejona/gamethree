(ns ngame.build.util
  (:require [me.raynes.fs :as fs]))

(defn copy-dir
  "A rip-off of fs/copy-dir, except it doesn't make a new dir inside the `to` destination."
  [from to]
  (when (fs/exists? from)
    (if (fs/file? to)
      (throw (IllegalArgumentException. (str to " is a file")))
      (let [from (fs/file from)
            to (fs/file to)
            trim-size (-> from str count inc)
            dest #(fs/file to (subs (str %) trim-size))]
        (fs/mkdirs to)
        (dorun
         (fs/walk (fn [root dirs files]
                 (doseq [dir dirs]
                   (when-not (fs/directory? dir)
                     (-> root (fs/file dir) dest fs/mkdirs)))
                 (doseq [f files]
                   (fs/copy+ (fs/file root f) (dest (fs/file root f)))))
               from))
        to))))

(defn copy-client-static-files
  {:shadow.build/stage :compile-prepare}
  [build-state & args]
  (copy-dir "src/ngame/client/static" "public")
  build-state)

(defn copy-server-static-files
  {:shadow.build/stage :compile-prepare}
  [build-state & args]
  (copy-dir "src/ngame/server/authoritative_server/static" "target/authoritative_server")
  build-state)
