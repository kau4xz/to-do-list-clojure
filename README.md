# To-do List - Clojure & ClojureScript

> **Atividade:** Desenvolvimento de um Todo List Fullstack
> **Disciplina:** Programação Funcional
> **Aluno:** Kauã Ferreira Galeno

## 📄 Descrição
Esta aplicação é uma lista de tarefas (Todo List) completa (Fullstack), desenvolvida para aplicar conceitos de Programação Funcional com Clojure. O sistema permite criar, listar, completar e excluir tarefas, com persistência de dados real e uma interface moderna com suporte a **Tema Escuro (Dark Mode)**.

**Tutorial Base:** [Tutorial Clojure/ClojureScript - Prof. Sergio Costa](https://profsergiocosta.notion.site/Tutorial-Clojure-ClojureScript-Construindo-uma-Aplica-o-Persistente-e-Reativa-2a5cce975093807aa9f0f0cb0cf69645)

## 🚀 Funcionalidades Implementadas
* **Frontend Reativo:** Interface Single Page Application (SPA) construída com Reagent.
* **API REST:** Backend em Clojure servindo JSON.
* **Persistência:** Dados salvos em banco de dados **SQLite** (`todo.db`).
* **CRUD Completo:** Adicionar, Visualizar, Concluir (Toggle) e Excluir tarefas.
* **Bônus UI:** Toggle Switch para alternar entre **Dark Mode** e Light Mode.

## 🛠 Tecnologias
* **Backend:** Clojure, Leiningen, Ring, Reitit, Next.jdbc, SQLite-jdbc.
* **Frontend:** ClojureScript, Shadow-CLJS, Reagent (React Wrapper).

## ⚙️ Como Rodar o Projeto

Para executar a aplicação, você precisará de dois terminais abertos.

### Pré-requisitos
* Java JDK (8 ou superior)
* Leiningen (`lein`)
* Node.js e NPM

### 1. Instalação
Clone este repositório e instale as dependências do ecossistema JavaScript:

bash
git clone https://github.com/kau4xz/to-do-list-clojure.git
cd nome-da-pasta
npm install

### 2. Backend
Na raiz do projeto execute "lein run"
para encerrar execute Ctrl + C

### 3. Frontend
Em um novo terminal, na raiz do projeto, execute: "npx shadow-cljs watch app"

### 4. Acessando a Aplicação
Abra seu navegador e acesse o endereço do frontend (geralmente porta 8080): http://localhost:8080
