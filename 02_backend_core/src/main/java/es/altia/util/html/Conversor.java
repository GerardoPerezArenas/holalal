package es.altia.util.html;

public class Conversor {

    public static String ConversorHTML(String stringHtml) {
        if (stringHtml == null || stringHtml.isEmpty()) return "";

        // Elimina <script> y <style> con su contenido
        stringHtml = stringHtml.replaceAll("(?s)<script.*?>.*?</script>", "");
        stringHtml = stringHtml.replaceAll("(?s)<style.*?>.*?</style>", "");

        // Agrega un espacio después de etiquetas de cierre o autosuficientes
        stringHtml = stringHtml.replaceAll("(?i)</[^>]+>", " ");
        stringHtml = stringHtml.replaceAll("(?i)<br\\s*/?>", " "); // maneja <br> y <br/>

        // Elimina todas las etiquetas HTML restantes
        stringHtml = stringHtml.replaceAll("<[^>]+>", "");

        // Reemplaza entidades HTML comunes
        stringHtml = stringHtml.replaceAll("&nbsp;", " ");
        stringHtml = stringHtml.replaceAll("&amp;", "&");
        stringHtml = stringHtml.replaceAll("&lt;", "<");
        stringHtml = stringHtml.replaceAll("&gt;", ">");

        // Limpia espacios duplicados
        return stringHtml.trim().replaceAll("\\s{2,}", " ");
    }

}