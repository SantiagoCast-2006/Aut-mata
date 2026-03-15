
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class LectorJSON {

    private String json;

    public void cargarArchivo(String rutaArchivo) throws IOException {
        json = new String(Files.readAllBytes(Paths.get(rutaArchivo)));
        json = json.replaceAll("\\s+", " ");
    }

    public String extraerValor(String clave) {
        Pattern p = Pattern.compile("\"" + clave + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher m = p.matcher(json);
        return m.find() ? m.group(1) : null;
    }

    public List<String> extraerArray(String clave) {
        Pattern p = Pattern.compile("\"" + clave + "\"\\s*:\\s*\\[([^\\]]+)\\]");
        Matcher m = p.matcher(json);
        if (!m.find()) return null;
        List<String> resultado = new ArrayList<>();
        Matcher items = Pattern.compile("\"([^\"]+)\"").matcher(m.group(1));
        while (items.find()) resultado.add(items.group(1));
        return resultado;
    }

    public List<Map<String, String>> extraerTransiciones() {
        List<Map<String, String>> resultado = new ArrayList<>();

        Pattern bloqueP = Pattern.compile("\"transiciones\"\\s*:\\s*\\[(.+?)\\]\\s*[,}]");
        Matcher bloqueM = bloqueP.matcher(json);
        if (!bloqueM.find()) return resultado;

        Matcher objM = Pattern.compile("\\{([^}]+)\\}").matcher(bloqueM.group(1));
        while (objM.find()) {
            String obj     = objM.group(1);
            String origen  = extraerValorLocal("{" + obj + "}", "origen");
            String simbolo = extraerValorLocal("{" + obj + "}", "simbolo");
            String destino = extraerValorLocal("{" + obj + "}", "destino");
            if (origen == null || simbolo == null || destino == null) continue;

            Map<String, String> t = new HashMap<>();
            t.put("origen",  origen);
            t.put("simbolo", simbolo);
            t.put("destino", destino);
            resultado.add(t);
        }
        return resultado;
    }

    private String extraerValorLocal(String fragmento, String clave) {
        Pattern p = Pattern.compile("\"" + clave + "\"\\s*:\\s*\"([^\"]+)\"");
        Matcher m = p.matcher(fragmento);
        return m.find() ? m.group(1) : null;
    }
}