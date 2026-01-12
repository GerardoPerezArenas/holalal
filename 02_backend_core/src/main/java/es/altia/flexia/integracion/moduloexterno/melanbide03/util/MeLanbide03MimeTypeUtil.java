package es.altia.flexia.integracion.moduloexterno.melanbide03.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Clase de utilidades para los mimetypes
 * 
 * @author david.caamano
 * @version 16/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-10-2012 * </li>
 * </ol> 
 */
public class MeLanbide03MimeTypeUtil {
    
    public static final String[] BINARY = {"application/octet-stream", "application/download"};
    public static final String[] PDF = {"application/pdf"};
    public static final String[] RTF = {"application/rtf"};
    public static final String[] ODT = {"application/vnd.oasis.opendocument.text"};
    
    public static final String FILEEXTENSION_PDF = "pdf";
    public static final String FILEEXTENSION_RTF = "rtf";
    public static final String FILEEXTENSION_ODT = "odt";
    
    private static final Map<String, String> MIMETYPES_FROM_EXTENSIONS = Collections.synchronizedMap(new HashMap<String, String>());
    static {
        MIMETYPES_FROM_EXTENSIONS.put(".pdf", "application/pdf");
        MIMETYPES_FROM_EXTENSIONS.put(".rtf", "application/rtf");
        MIMETYPES_FROM_EXTENSIONS.put(".odt", "application/vnd.oasis.opendocument.text");
    }//MIMETYPES_FROM_EXTENSIONS
    
    private static final Map<String, String> EXTENSIONS_FROM_MIMETYPES = Collections.synchronizedMap(new HashMap<String, String>());
    static {
        EXTENSIONS_FROM_MIMETYPES.put("application/pdf",                ".pdf");
        EXTENSIONS_FROM_MIMETYPES.put("application/rtf",                ".rtf");
        EXTENSIONS_FROM_MIMETYPES.put("application/vnd.oasis.opendocument.text",                                     ".odt");
    }//EXTENSIONS_FROM_MIMETYPES
    
    public static String guessMimeTypeFromExtension(String extension){
        String result = null;
        if (extension!=null) {
            extension = extension.trim().toLowerCase();
            result = MIMETYPES_FROM_EXTENSIONS.get(extension);
        }//if
        if (result==null) {
            result = BINARY[0];
        }//if
        return result;
    }//guessMimeTypeFromExtension

    public static String guessExtensionFromMimeType(String mimeType) {
        String result = null;
        if (mimeType!=null) {
            mimeType = mimeType.trim().toLowerCase();
            result = EXTENSIONS_FROM_MIMETYPES.get(mimeType);

            if (result==null) {
                result = internalGetExtensionFromMimeType(mimeType);
            }//if
        }//if
        if (result==null) {
            result = ".dat";
        }//if
        return result;
    }//guessExtensionFromMimeType
    
    private static String internalGetExtensionFromMimeType (String mimeType){
        String resultado="";
        if(mimeType!=null){
            if(mimeType.contains("pdf")){
                resultado=FILEEXTENSION_PDF;
            }//if(mimeType.contains("pdf"))
            if(mimeType.contains("opendocument.text")){
                resultado=FILEEXTENSION_ODT;
            }//if(mimeType.contains("opendocument.text"))
            if(mimeType.contains("rtf")){
                resultado=FILEEXTENSION_RTF;
            }//if(mimeType.contains("rtf"))
        }//if(mimeType!=null)
        
        if(!resultado.equals("")){
            resultado=".".concat(resultado);
    	} else {
            resultado=".dat";
        }//if(!resultado.equals(""))
    	return resultado;
    }//internalGetExtensionFromMimeTypeString resultado="";
}//class
