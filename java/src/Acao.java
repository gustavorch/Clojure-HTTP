import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Acao {
    public String nome;
    public String simbolo;
    public double precoFechamento;
    public double precoAbertura;
    public double precoMinimo;
    public double precoMaximo;
    public double precoAtual;
    public double variacaoDia;
    public double variacaoDiaPercentual;
    public String descricao;
    public String horaFechamento;

    @JsonCreator
    public Acao(@JsonProperty("nome") String nome,
                @JsonProperty("codigo") String simbolo,
                @JsonProperty("precoFechamento") double precoFechamento,
                @JsonProperty("precoAbertura") double precoAbertura,
                @JsonProperty("precoMinimo") double precoMinimo,
                @JsonProperty("precoMaximo") double precoMaximo,
                @JsonProperty("precoAtual") double precoAtual,
                @JsonProperty("variacaoDia") double variacaoDia,
                @JsonProperty("variacaoDiaPercentual") double variacaoDiaPercentual,
                @JsonProperty("descricao") String descricao,
                @JsonProperty("horaFechamento") String horaFechamento) {
        this.nome = nome;
        this.simbolo = simbolo;
        this.precoFechamento = precoFechamento;
        this.precoAbertura = precoAbertura;
        this.precoMinimo = precoMinimo;
        this.precoMaximo = precoMaximo;
        this.precoAtual = precoAtual;
        this.variacaoDia = variacaoDia;
        this.variacaoDiaPercentual = variacaoDiaPercentual;
        this.descricao = descricao;
        this.horaFechamento = horaFechamento;
    }

    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Nome = " + nome + "\n" +
                "Codigo = " + simbolo + "\n" +
                "Preco Fechamento = " + precoFechamento + "\n" +
                "Preco Abertura = " + precoAbertura + "\n" +
                "Preco Minimo = " + precoMinimo + "\n" +
                "Preco Atual = " + precoAtual + "\n" +
                "Variacao Dia (%) = " + variacaoDiaPercentual + "\n" +
                "Variacao Dia = " + variacaoDia + "\n" +
                "Hora Fechamento = " + horaFechamento + "\n" +
                "Descricao = " + descricao + "\n";

    }
}
