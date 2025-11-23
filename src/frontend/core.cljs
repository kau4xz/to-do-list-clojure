(ns frontend.core
  (:require [reagent.core :as r]
            [reagent.dom :as d]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

;; --- ESTADO GLOBAL ---
(def state (r/atom []))
(def input-val (r/atom ""))
(def is-dark (r/atom false))

;; --- AUXILIAR: LÓGICA SQLITE ---
;; O SQLite retorna 0 ou 1, mas o frontend espera true ou false.
;; Essa função resolve o problema do "riscado" não funcionar.
(defn done? [val]
  (or (= val true) (= val 1)))

;; --- API DO BACKEND ---
(defn fetch-todos []
  (go (let [response (<! (http/get "http://localhost:3000/api/todos"))]
        (reset! state (:body response)))))

(defn add-todo []
  (when (seq @input-val)
    (go (let [response (<! (http/post "http://localhost:3000/api/todos"
                                      {:json-params {:title @input-val}}))]
          (reset! state (:body response))
          (reset! input-val "")))))

(defn toggle [id]
  (go (let [resp (<! (http/put (str "http://localhost:3000/api/todos/" id "/toggle")))]
        (reset! state (:body resp)))))

(defn delete [id]
  (go (let [resp (<! (http/delete (str "http://localhost:3000/api/todos/" id)))]
        (reset! state (:body resp)))))

;; --- TEMA DARK/LIGHT ---
(defn toggle-theme []
  (swap! is-dark not)
  ;; Manipula a classe 'dark' direto na tag <body>
  (if @is-dark
    (-> js/document .-body .-classList (.add "dark"))
    (-> js/document .-body .-classList (.remove "dark"))))

;; --- COMPONENTE: ITEM DA LISTA ---
(defn todo-item [todo]
  (let [is-completed (done? (:done todo))]
    [:li {:class "todo-item"
          :style {:display "flex"
                  :justify-content "space-between"
                  :align-items "center"
                  :padding "12px"
                  :border-bottom "1px solid var(--border-color)"}}
     
     ;; Checkbox + Texto
     [:div {:style {:display "flex" 
                    :align-items "center"
                    :gap "12px"}}
      
      [:input {:type "checkbox" 
               :checked is-completed
               :on-change #(toggle (:id todo))
               :style {:cursor "pointer" 
                       :width "20px" 
                       :height "20px"
                       :margin "0"
                       :position "relative"
                       :top "-1px"}}]
      
      [:span {:style {:font-size "1.1rem"
                      :text-decoration (if is-completed "line-through" "none")
                      :color (if is-completed "#777" "var(--text-color)")
                      :transition "all 0.2s"
                      :line-height "1.2"}} 
       (:title todo)]]

     ;; Botão Deletar
     [:button {:on-click #(delete (:id todo))
               :style {:background "#ff4444" 
                       :color "white" 
                       :border "none" 
                       :padding "0"
                       :width "32px"
                       :height "32px"
                       :cursor "pointer" 
                       :border-radius "6px"
                       :font-weight "bold"
                       :display "flex"
                       :align-items "center"
                       :justify-content "center"}} 
      "X"]]))

;; --- COMPONENTE: APP PRINCIPAL ---
(defn app []
  [:div {:style {:padding "40px 20px" 
                 :max-width "600px" 
                 :margin "0 auto" 
                 :font-family "sans-serif"}}
   
   ;; Cabeçalho
   [:div {:style {:display "flex" 
                  :justify-content "space-between" 
                  :align-items "center"
                  :margin-bottom "30px"}}
    
    [:h1 {:style {:margin 0 :font-size "2rem"}} "To-do List"]
    
    ;; NOVO TOGGLE SWITCH (Requer o CSS que adicionamos)
    [:div {:class "theme-switch-wrapper"}
     [:span {:class "theme-label" :style {:font-size "0.8rem"}} "DARK"]
     [:label {:class "theme-switch"}
      [:input {:type "checkbox"
               ;; Se não for dark, está checked (Light)
               :checked (not @is-dark) 
               :on-change toggle-theme}]
      [:span {:class "slider"}]]
     [:span {:class "theme-label" :style {:font-size "0.8rem"}} "LIGHT"]]]

   ;; Container Branco/Cinza
   [:div {:class "todo-container" :style {:padding "25px"}}
    
    ;; Área de Input + Botão (Correção de Altura)
    [:div {:style {:display "flex" 
                   :gap "10px" 
                   :margin-bottom "25px"
                   :height "48px"       ;; Altura Mestra
                   :align-items "stretch"}}
     
     [:input {:type "text" 
              :value @input-val 
              :on-change #(reset! input-val (-> % .-target .-value)) 
              :placeholder "Nova tarefa..." 
              :style {:flex 1 
                      :height "100%"
                      :margin "0"
                      :padding "0 15px" 
                      :border "1px solid var(--border-color)" 
                      :background "var(--bg-color)" 
                      :color "var(--text-color)"
                      :border-radius "6px"
                      :font-size "1rem"
                      :box-sizing "border-box"}}]
     
     [:button {:on-click add-todo 
               :style {:height "100%"
                       :margin "0"
                       :padding "0 25px"
                       :background "var(--primary-color)" 
                       :color "white" 
                       :border "none" 
                       :border-radius "6px"
                       :cursor "pointer" 
                       :font-weight "bold"
                       :font-size "1rem"
                       :box-sizing "border-box"
                       :display "flex"
                       :align-items "center"}} 
      "Adicionar"]]
    
    ;; Lista de Tarefas
    [:ul {:style {:list-style "none" :padding 0 :margin 0}}
     (if (empty? @state)
       [:p {:style {:text-align "center" :color "#888" :margin-top "20px"}} 
        "Nenhuma tarefa por enquanto..."]
       (for [todo @state]
         ^{:key (:id todo)} [todo-item todo]))]]])

;; --- INICIALIZAÇÃO ---
(defn init []
  (fetch-todos)
  (d/render [app] (.getElementById js/document "app")))