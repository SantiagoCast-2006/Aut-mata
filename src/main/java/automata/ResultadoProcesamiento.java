
import java.util.List;

public class ResultadoProcesamiento {

    public final String       cadena;
    public final boolean      acepta;
    public final String       estadoFinal;
    public final List<String> recorrido;
    public final String       error;

    public ResultadoProcesamiento(String cadena, boolean acepta, String estadoFinal,
                                  List<String> recorrido, String error) {
        this.cadena      = cadena;
        this.acepta      = acepta;
        this.estadoFinal = estadoFinal;
        this.recorrido   = recorrido;
        this.error       = error;
    }

    public void imprimir() {
        String etiq    = acepta ? "ACEPTA" : "RECHAZA";
        String cadenaM = cadena.isEmpty() ? "(vacía)" : "'" + cadena + "'";
        System.out.printf("  %-12s -> %-8s  [%s]%n",
             cadenaM, etiq, String.join(" -> ", recorrido));
        if (error != null)
            System.out.println("    ⚠ " + error);
    }
}