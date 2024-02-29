import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String urlBase = "http://localhost:3000";
    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {

        System.out.println("Bem vindo ao sistema de compras de acoes");

        int valor;
        do {
            System.out.println("----------------------------------------");
            System.out.println("1 - Para acessar a lista de acoes da B3");
            System.out.println("2 - Para acessar uma acao especifica");
            System.out.println("3 - Para comprar uma acao");
            System.out.println("4 - Para vender uma acao");
            System.out.println("5 - Para acessar o historico de transacoes");
            System.out.println("6 - Para acessar o historico de compras");
            System.out.println("7 - Para acessar o historico de vendas");
            System.out.println("8 - Para verificar o valor total da sua carteira");
            System.out.println("0 - Para sair");
            System.out.println("----------------------------------------");

            valor = scanner.nextInt();

            if (valor == 1) {

                String endpoint = "/lista";

                String json = requisicaoGET(endpoint);

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    AcaoLista[] acoes = objectMapper.readValue(json, AcaoLista[].class);
                    for (AcaoLista acoe : acoes) {
                        System.out.println(acoe);
                    }
                } catch (IOException ignored) {

                }
            } else if (valor == 2) {

                System.out.println("Digite o codigo da acao: ");
                String codigo = scanner.next();

                String endpoint = "/detalhes/" + codigo;

                String json = requisicaoGET(endpoint);

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    Acao acao = objectMapper.readValue(json, Acao.class);
                    System.out.println(acao);
                } catch (IOException ignored) {

                }

            }else if (valor == 3) {

                System.out.println("Digite o codigo da acao: ");
                String codigo = scanner.next();

                System.out.println("Digite a quantidade: ");
                int quantidade = scanner.nextInt();

                String endpoint = "/fazerCompra/" + codigo + "/" + quantidade;
                String json = requisicaoPOST(endpoint);

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode jsonNode = objectMapper.readTree(json);

                    String mensagem = jsonNode.get("mensagem").asText();
                    System.out.println(mensagem);
                } catch (Exception ignored) {

                }

            }else if (valor == 4) {

                System.out.println("Digite o codigo da acao: ");
                String codigo = scanner.next();
                System.out.println("Digite a quantidade: ");
                int quantidade = scanner.nextInt();

                String endpoint = "/fazerVenda/" + codigo + "/" + quantidade;

                String json = requisicaoPOST(endpoint);

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode jsonNode = objectMapper.readTree(json);

                    String mensagem = jsonNode.get("mensagem").asText();
                    System.out.println(mensagem);
                } catch (Exception ignored) {

                }

            }else if (valor == 5) {
                String endpoint = "/transacoes";
                String json = requisicaoGET(endpoint);

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode jsonNode = objectMapper.readTree(json).get("transacoes");

                    List<Transacao> transacoes = objectMapper.readValue(
                            jsonNode.toString(),
                            new TypeReference<List<Transacao>>() {}
                    );

                    for (Transacao transacao : transacoes) {
                        System.out.println(transacao);
                    }

                } catch (IOException ignored) {

                }

            }else if (valor == 6) {
                String endpoint = "/transacoes/compras";

                String json = requisicaoGET(endpoint);

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode jsonNode = objectMapper.readTree(json).get("transacoes");

                    List<Transacao> transacoes = objectMapper.readValue(
                            jsonNode.toString(),
                            new TypeReference<List<Transacao>>() {}
                    );

                    for (Transacao transacao : transacoes) {
                        System.out.println(transacao);
                    }

                } catch (IOException ignored) {

                }

            }else if (valor == 7) {
                String endpoint = "/transacoes/vendas";

                String json = requisicaoGET(endpoint);

                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode jsonNode = objectMapper.readTree(json).get("transacoes");

                    List<Transacao> transacoes = objectMapper.readValue(
                            jsonNode.toString(),
                            new TypeReference<List<Transacao>>() {}
                    );

                    for (Transacao transacao : transacoes) {
                        System.out.println(transacao);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else if (valor == 8) {
                String endpoint = "/saldo";

                String json = requisicaoGET(endpoint);
                ObjectMapper objectMapper = new ObjectMapper();

                try {
                    JsonNode jsonNode = objectMapper.readTree(json);

                    double totalAcumulado = jsonNode.get("total-acumulado").asDouble();
                    System.out.println("Saldo da carteira: " + totalAcumulado);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (valor != 0) {
                System.out.println("Valor invalido");
            }
        } while (valor != 0);

        System.out.println("Programa encerrado.");
        scanner.close();


    }

    private static String requisicaoGET(String endpoint) {

      String endpointUrl = urlBase + endpoint;

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return ("Falha na requisição. Código de resposta: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao fazer a requisição: " + e.getMessage());
        }
        return "Falha na requisição";
    }

    private static String requisicaoPOST(String endpoint) {

      String endpointUrl = urlBase + endpoint;

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return ("Falha na requisição. Código de resposta: " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao fazer a requisição: " + e.getMessage());
        }
        return "Falha na requisição";
    }
}
