(ns server
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [ring.util.response :as response]
            [clojure.data.json :as json]
            [ring.middleware.cors :refer [wrap-cors]]
            [db :as db]))

(defn get-todos [_]
  (let [todos (db/get-all)]
    (-> (response/response (json/write-str todos))
        (response/header "Content-Type" "application/json"))))

(defn create-todo [request]
  (let [body (json/read-str (slurp (:body request)) :key-fn keyword)]
    (db/insert (:title body))
    (get-todos nil)))

;; --- NOVOS HANDLERS ---

(defn toggle-todo [request]
  (let [id (get-in request [:path-params :id])]
    (db/toggle id)
    (get-todos nil))) ;; Retorna a lista atualizada

(defn delete-todo [request]
  (let [id (get-in request [:path-params :id])]
    (db/delete id)
    (get-todos nil))) ;; Retorna a lista atualizada

(def app
  (-> (ring/ring-handler
       (ring/router
        ["/api"
         ["/todos" {:get get-todos :post create-todo}]
         ;; Novas Rotas Dinâmicas (:id)
         ["/todos/:id/toggle" {:put toggle-todo}]
         ["/todos/:id"        {:delete delete-todo}]])
       (ring/routes
        (ring/create-default-handler)))
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :put :post :delete]
                 :access-control-allow-headers ["Content-Type"]
                 :access-control-allow-credentials "true")))

(defn -main []
  (db/init-db)
  (println "Servidor Completo rodando na porta 3000")
  (jetty/run-jetty app {:port 3000 :join? false}))