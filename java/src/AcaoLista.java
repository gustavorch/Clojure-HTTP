import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

class AcaoLista {
    private String nome;
    private String codigo;

    @JsonCreator
    public AcaoLista(@JsonProperty("nome") String nome, @JsonProperty("codigo") String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    @Override
    public String toString() {
        return "Nome da Acao: " + nome + "\n" +
                "Codigo: " + codigo + "\n";
    }
}