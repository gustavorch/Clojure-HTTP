# Requisições HTTP com Clojure


Projeto feito em Clojure, com uma simples interface em Java, que permite a realização de requisições HTTP, exibindo informações detalhadas sobre o mercado de ações brasileiras por meio da API da [Brapi](https://brapi.dev).
Todas as transações realizadas (compras e vendas) assim como o saldo total das ações acumuladas ficam armazenadas dentro de um banco de dados por meio de [atoms](https://clojure.org/reference/atoms).


## Tecnologias utilizadas

* Clojure
* Java
* Dependências Clojure: Compojure, Cheshire e Lein-Ring
