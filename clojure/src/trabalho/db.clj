(ns trabalho.db
  (:require [clj-http.client :as http-client]
            [cheshire.core :as json]))

(def registros
  (atom []))

(defn transacoes []
  @registros)

(defn registrar [transacao]
  (let [colecao-atualizada (swap! registros conj transacao)]
    (merge transacao {:id (count colecao-atualizada)})))

;; (defn- compra? [transacao]
;;   (= (:tipo transacao) "compra"))

;; (defn- calcular [acumulado transacao]
;;   (let [valor (:valor transacao)]
;;     (if (compra? transacao)
;;       (- acumulado valor)
;;       (+ acumulado valor))))

;; (defn saldo []
;;   999)

;; (defn valida? [transacao]
;;   (and (contains? transacao :valor)
;;        (number? (:valor transacao))
;;        (pos? (:valor transacao))
;;        (contains? transacao :tipo)
;;        (or (= "compra" (:tipo transacao))
;;            (= "venda" (:tipo transacao)))))

(defn transacoes-do-tipo [tipo]
  (filter #(= tipo (:tipo %)) (transacoes)))


;; obter informações detalhadas de uma ação específica
(defn obter-informacoes-acao [codigo-acao]
  (let [url (str "https://brapi.dev/api/quote/" codigo-acao "?modules=summaryProfile&token=58iNus1bh51Ymw9gXmzuop")]
    (try
      (-> (http-client/get url)
          :body (json/parse-string true)
          :results first)
      (catch Exception e
        (println "Erro ao obter informações da ação:" e)
        nil))))

(def minhas-acoes (atom {}))

(def total-acumulado (atom 0))

(defn saldo []
  @total-acumulado)

(defn realizar-compra [codigo-acao quantidade]
  (let [info-acao (obter-informacoes-acao codigo-acao)]
    (if info-acao
      (let [preco-unitario (:regularMarketPrice info-acao)
            valor-total (* preco-unitario quantidade)]
        (swap! registros conj {:nome (:longName info-acao)
                               :codigo codigo-acao
                               :precoAtual preco-unitario
                               :quantidade quantidade
                               :valorTotal valor-total
                               :tipo "compra"})
        (swap! minhas-acoes update codigo-acao (fnil + 0) quantidade) ; adiciona ou atualiza a quantidade de uma acao especifica
        (swap! total-acumulado + valor-total) ; adiciona o valor da compra ao total acumulado
        {:mensagem "Compra realizada com sucesso."})
      {:mensagem "Não foi possível obter informações da ação."})))

(defn realizar-venda [codigo-acao quantidade]
  (let [info-acao (obter-informacoes-acao codigo-acao)
        quantidade-existente (get @minhas-acoes codigo-acao 0)] ; verifica a quantidade existente da acao
    (if (and info-acao (>= quantidade-existente quantidade)) ; verifica se a quantidade a ser vendida eh menor ou igual a quantidade existente
      (let [preco-unitario (:regularMarketPrice info-acao)
            valor-total (* preco-unitario quantidade)]
        (registrar {:nome (:longName info-acao)
                    :codigo codigo-acao
                    :precoAtual preco-unitario
                    :quantidade quantidade
                    :valorTotal valor-total
                    :tipo "venda"})
        (swap! minhas-acoes update codigo-acao (fnil - 0) quantidade) ; diminui a quantidade de uma acao especifica
        (swap! total-acumulado - valor-total) ; diminui o valor da venda do total acumulado
        {:mensagem "Venda realizada com sucesso"})
      {:mensagem "Não foi possível realizar a venda. Quantidade insuficiente ou ação não encontrada."})))