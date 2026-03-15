
import java.io.*;
import java.util.*;

public class Automata {

    private Set<String>                      alfabeto;
    private Set<String>                      estados;
    private String                           estadoInicial;
    private Set<String>                      estadosAceptacion;
    private Map<String, Map<String, String>> transiciones;

    public Automata() {
        alfabeto          = new LinkedHashSet<>();
        estados           = new LinkedHashSet<>();
        estadosAceptacion = new LinkedHashSet<>();
        transiciones      = new HashMap<>();
    }

    public Set<String> getAlfabeto() {
        return alfabeto;
    }
    public Set<String> getEstados() {
        return estados;
    }
    public String getEstadoInicial() {
        return estadoInicial;
    }
    public Set<String> getEstadosAceptacion() {
        return estadosAceptacion;
    }
    public Map<String, Map<String, String>> getTransiciones() {
        return transiciones;
    }

    public void cargarDesdeJSON(String rutaArchivo) throws IOException {
        LectorJSON lector = new LectorJSON();
        lector.cargarArchivo(rutaArchivo);

        // Alfabeto
        List<String> listAlf = lector.extraerArray("alfabeto");
        if (listAlf == null) throw new IllegalStateException("Falta 'alfabeto' en el JSON.");
        alfabeto.addAll(listAlf);

        // Estados
        List<String> listEst = lector.extraerArray("estados");
        if (listEst == null) throw new IllegalStateException("Falta 'estados' en el JSON.");
        for (String e : listEst) {
            estados.add(e);
            transiciones.put(e, new HashMap<>());
        }

        // Estado inicial
        estadoInicial = lector.extraerValor("estado_inicial");
        if (estadoInicial == null) throw new IllegalStateException("Falta 'estado_inicial' en el JSON.");

        // Estados de aceptación
        List<String> listAcep = lector.extraerArray("estados_aceptacion");
        if (listAcep == null) throw new IllegalStateException("Falta 'estados_aceptacion' en el JSON.");
        estadosAceptacion.addAll(listAcep);

        // Transiciones
        for (Map<String, String> t : lector.extraerTransiciones()) {
            String origen  = t.get("origen");
            String simbolo = t.get("simbolo");
            String destino = t.get("destino");
            if (transiciones.containsKey(origen))
                transiciones.get(origen).put(simbolo, destino);
        }

        validarConfiguracion();
    }

    public ResultadoProcesamiento procesar(String cadena) {
        String estadoActual = estadoInicial;
        List<String> recorrido = new ArrayList<>();
        recorrido.add(estadoActual);

        for (int i = 0; i < cadena.length(); i++) {
            String simbolo = String.valueOf(cadena.charAt(i));

            if (!alfabeto.contains(simbolo))
                return new ResultadoProcesamiento(cadena, false, estadoActual, recorrido,
                    "Símbolo '" + simbolo + "' no pertenece al alfabeto.");

            Map<String, String> trans = transiciones.get(estadoActual);
            if (trans == null || !trans.containsKey(simbolo))
                return new ResultadoProcesamiento(cadena, false, estadoActual, recorrido,
                    "No hay transición desde '" + estadoActual + "' con '" + simbolo + "'.");

            estadoActual = trans.get(simbolo);
            recorrido.add(estadoActual);
        }

        return new ResultadoProcesamiento(
            cadena,
            estadosAceptacion.contains(estadoActual),
            estadoActual,
            recorrido,
            null
        );
    }

    private void validarConfiguracion() {
        List<String> errores = new ArrayList<>();
        if (alfabeto.isEmpty())    errores.add("El alfabeto está vacío.");
        if (estados.isEmpty())     errores.add("No hay estados definidos.");
        if (estadoInicial == null) errores.add("No se definió estado inicial.");
        else if (!estados.contains(estadoInicial))
            errores.add("Estado inicial '" + estadoInicial + "' no existe.");
        for (String ea : estadosAceptacion)
            if (!estados.contains(ea))
                errores.add("Estado de aceptación '" + ea + "' no existe.");
        if (!errores.isEmpty()) {
            System.out.println("\n❌ Errores en la configuración:");
            errores.forEach(e -> System.out.println("   • " + e));
            throw new IllegalStateException("Configuración inválida.");
        }
    }
}