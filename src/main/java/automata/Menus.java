import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Scanner;

public class Menus {

    public void mostrarMenuPrincipal(String[] args) throws IOException {
        String rutaArchivo = args.length > 0 ? args[0] : "config/automata.json";

        System.out.println("\n══════════════════════════════════════════");
        System.out.println("         AUTÓMATA FINITO DETERMINISTA");
        System.out.println("══════════════════════════════════════════\n");

        Automata afd = new Automata();
        try {
            System.out.println(" Cargando: " + rutaArchivo + "\n");
            afd.cargarDesdeJSON(rutaArchivo);
        } catch (FileNotFoundException e) {
            System.out.println(" Archivo no encontrado: " + rutaArchivo);
            return;
        } catch (IllegalStateException e) {
            return;
        }

        mostrarConfiguracion(afd.getAlfabeto(), afd.getEstados(), afd.getEstadoInicial(),
                            afd.getEstadosAceptacion(), afd.getTransiciones());
        menuInteractivo(afd);
    }

    public void mostrarConfiguracion(
        java.util.Set<String> alfabeto,
        java.util.Set<String> estados,
        String estadoInicial,
        java.util.Set<String> estadosAceptacion,
        java.util.Map<String, java.util.Map<String, String>> transiciones
    ) {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│         CONFIGURACIÓN DEL AUTÓMATA      │");
        System.out.println("└─────────────────────────────────────────┘");
        System.out.println("  Alfabeto          : " + alfabeto);
        System.out.println("  Estados           : " + estados);
        System.out.println("  Estado inicial    : " + estadoInicial);
        System.out.println("  Estados aceptación: " + estadosAceptacion);
        System.out.println("\n  Tabla de transiciones:");
        System.out.printf("  %-12s", "Estado");
        for (String s : alfabeto)
            System.out.printf("  %-10s", "'" + s + "'");
        System.out.println();
        System.out.println("  " + "─".repeat(12 + alfabeto.size() * 12));
        for (String estado : estados) {
            String marcador = estadoInicial.equals(estado) ? "-> " :
                              estadosAceptacion.contains(estado) ? "* " : "  ";
            System.out.printf("  %s%-10s", marcador, estado);
            for (String s : alfabeto) {
                String dest = transiciones.getOrDefault(estado, Collections.emptyMap())
                                          .getOrDefault(s, "-");
                System.out.printf("  %-10s", dest);
            }
            System.out.println();
        }
        System.out.println("  (-> = inicial, * = aceptación)\n");
    }

    private static void menuInteractivo(Automata afd) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("══════════════════════════════════════════");
        System.out.println("  MODO INTERACTIVO  ('salir' para terminar)");
        System.out.println("══════════════════════════════════════════\n");

        while (true) {
            System.out.print("  Cadena: ");
            String entrada = scanner.nextLine().trim();
            if (entrada.equalsIgnoreCase("salir")) {
                System.out.println("\n  ¡Hasta luego!");
                break;
            }
            afd.procesar(entrada).imprimir();
            System.out.println();
        }

        scanner.close();
    }
}
