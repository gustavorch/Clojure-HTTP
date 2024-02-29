(ns trabalho.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body]]
            [trabalho.db :as db]
            [clj-http.client :as http-client]))

;; (def url-api "https://brapi.dev/api/quote/list?token=58iNus1bh51Ymw9gXmzuop")
(def url-api "https://brapi.dev/api/quote/list?modules=summaryProfile&token=58iNus1bh51Ymw9gXmzuop")

(defn transformar-json [conteudo & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json; charset=utf-8"}
   :body (json/generate-string conteudo)})

(defn informacoes [acao]
  {:nome (:longName acao)
   :codigo (:symbol acao)
   :precoAtual (:regularMarketPrice acao)
   :precoAbertura (:regularMarketOpen acao)
   :precoFechamento (:regularMarketPreviousClose acao)
   :precoMaximo (:regularMarketDayHigh acao)
   :precoMinimo (:regularMarketDayLow acao)
   :horaFechamento (:regularMarketTime acao)
   :variacaoDia (:regularMarketChange acao)
   :variacaoDiaPercentual (:regularMarketChangePercent acao)
   :descricao (:longBusinessSummary (:summaryProfile acao))})

(defn lista-companhias []
  (let [body (:body (http-client/get url-api))
        stocks (get-in (json/parse-string body true) [:stocks])]
    (transformar-json
     (map (fn [stock]
            {:nome (:name stock)
             :codigo (:stock stock)})
          stocks))))

(defn detalhesAcao [codigo-da-acao]
  (let [url (str "https://brapi.dev/api/quote/" codigo-da-acao "?modules=summaryProfile&token=58iNus1bh51Ymw9gXmzuop")
        response (http-client/get url)
        body (:body response)
        parsed-body (json/parse-string body true)
        results (get-in parsed-body [:results])]
    (if (seq results)
      (first results)
      {})))

(defroutes app-routes
  (GET "/" [] "Olá, mundo!")
  ;; (GET "/q" {params :params} (str params))

  (GET "/lista" []
    (try
      (lista-companhias)
      (catch Exception e (transformar-json {:mensagem "Erro ao processar a lista de ações."} 422))))

  (GET "/detalhes/:codigo" [codigo]
    (try
      (transformar-json (informacoes (detalhesAcao codigo)))
      (catch Exception e (transformar-json {:mensagem "Erro ao processar a ação."} 422))))

  (POST "/fazerCompra/:codigoAcao/:quantidade" [codigoAcao quantidade]
    (try
      (let [result (db/realizar-compra codigoAcao (Integer/parseInt quantidade))]
        (transformar-json result))
      (catch Exception e
        (transformar-json {:mensagem "Erro ao processar a compra."} 422))))

  (POST "/fazerVenda/:codigoAcao/:quantidade" [codigoAcao quantidade]
    (try
      (let [result (db/realizar-venda codigoAcao (Integer/parseInt quantidade))]
        (transformar-json result))
      (catch Exception e
        (transformar-json {:mensagem "Erro ao processar a venda."} 422))))

  (GET "/saldo" []
    (try
      (transformar-json {:total-acumulado (db/saldo)})
      (catch Exception e
        (transformar-json {:mensagem "Erro ao processar o saldo."} 422))))

  (GET "/transacoes" []
    (try
      (transformar-json {:transacoes (db/transacoes)})
      (catch Exception e
        (transformar-json {:mensagem "Erro ao processar as transações."} 422))))

  (GET "/transacoes/vendas" []
    (try
      (transformar-json {:transacoes (db/transacoes-do-tipo "venda")})
      (catch Exception e
        (transformar-json {:mensagem "Erro ao processar as transações do tipo 'venda'."} 422))))

  (GET "/transacoes/compras" []
    (try
      (transformar-json {:transacoes (db/transacoes-do-tipo "compra")})
      (catch Exception e
        (transformar-json {:mensagem "Erro ao processar as transações do tipo 'saldo'."} 422))))

  (route/not-found "Recurso não encontrado."))

(def app
  (-> (wrap-defaults app-routes api-defaults)
      (wrap-json-body {:keywords? true :bigdecimals? true})))