(ns server
  (:require [ring.adapter.jetty :as jetty]
            [reitit.ring :as ring]
            [ring.util.response :as response]
            [clojure.data.json :as json]
            [ring.middleware.cors :refer [wrap-cors]]))

;; --- BANCO DE DADOS (SIMULADO) ---
;; Um átomo que guarda uma lista de mapas.
(def todos (atom [{:id 1 :title "Configurar Ambiente" :done true}
                  {:id 2 :title "Criar API" :done false}]))

;; --- HANDLERS (Funções que lidam com as requisições) ---

(defn get-todos [_]
  (-> (response/response (json/write-str @todos))
      (response/header "Content-Type" "application/json")))

(defn create-todo [request]
  ;; Lê o corpo da requisição (JSON), converte para mapa Clojure
  (let [body (json/read-str (slurp (:body request)) :key-fn keyword)
        ;; Cria a nova tarefa com ID aleatório
        new-todo {:id (rand-int 9999)
                  :title (:title body)
                  :done false}]
    ;; Adiciona na lista (atom)
    (swap! todos conj new-todo)
    ;; Retorna a lista atualizada
    (get-todos nil)))

;; --- ROTEAMENTO ---

(def app
  (ring/ring-handler
   (ring/router
    ["/api"
     ["/todos" {:get get-todos
                :post create-todo}]])
   (ring/routes
    (ring/create-default-handler))))

;; --- MAIN ---

(defn -main []
  (println "Servidor API rodando na porta 3000")
  (jetty/run-jetty app {:port 3000 :join? false}))