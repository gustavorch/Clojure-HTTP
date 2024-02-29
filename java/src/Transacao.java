import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Transacao {
    String nome;
    String codigo;
    double precoAtual;
    int quantidade;
    double valorTotal;
    String tipo;

    @JsonCreator
    public Transacao(@JsonProperty("nome") String nome, @JsonProperty("codigo") String codigo, @JsonProperty("precoAtual") double precoAtual, @JsonProperty("quantidade") int quantidade, @JsonProperty("valorTotal") double valorTotal, @JsonProperty("tipo") String tipo) {
        this.nome = nome;
        this.codigo = codigo;
        this.precoAtual = precoAtual;
        this.quantidade = quantidade;
        this.valorTotal = valorTotal;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Nome = " + nome + "\n" +
                "Codigo = " + codigo + "\n" +
                "Quantidade = " + quantidade + "\n" +
                "Valor Total = " + valorTotal + "\n" +
                "Preco Atual = " + precoAtual + "\n" +
                "Tipo = " + tipo + "\n";
    }
}
