(ns db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(def db {:dbtype "sqlite" :dbname "todo.db"})
(def ds (jdbc/get-datasource db))

(defn init-db []
  (jdbc/execute! ds ["CREATE TABLE IF NOT EXISTS todo (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, is_done BOOLEAN DEFAULT 0)"]))

(defn get-all []
  (jdbc/execute! ds ["SELECT id, title, is_done as done FROM todo"] {:builder-fn rs/as-unqualified-lower-maps}))

(defn insert [title]
  (jdbc/execute! ds ["INSERT INTO todo (title, is_done) VALUES (?, 0)" title]))

;; --- NOVAS FUNÇÕES ---

(defn toggle [id]
  ;; Inverte o valor de is_done (0 vira 1, 1 vira 0)
  (jdbc/execute! ds ["UPDATE todo SET is_done = NOT is_done WHERE id = ?" id]))

(defn delete [id]
  (jdbc/execute! ds ["DELETE FROM todo WHERE id = ?" id]))