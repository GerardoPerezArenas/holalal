/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide37.util;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #86969 Edici�n inicial</li>
 * </ol> 
 */
public class ConstantesMeLanbide37 {
    
    //Fichero de propiedades del m�dulo.
    public static String FICHERO_PROPIEDADES = "MELANBIDE37";
    
    //Barra separadora
    public static String BARRA_SEPARADORA = "/";
    
    //Separador nombre archivos
    public static String SEPARADOR_NOMBRE_ARCHIVOS = "-";
    

    //Constante para la clave del fichero de propiedades que indica el nombre del m�dulo.
    public static String NOMBRE_MODULO = "NOMBRE_MODULO";
    
    public static String MELANBIDE37_CERTIFICADO = "TABLA_MELANBIDE37_CERTIFICADO";
    public static String MELANBIDE37_CERT_CENTRO = "TABLA_MELANBIDE37_CERT_CENTRO";
    public static String CERT_CENTROS = "TABLA_CER_CENTROS";
    public static String MELANBIDE37_S_CERTIFICADOS = "TABLA_MELANBIDE37_S_CERTIFICADOS";
    
    public static String TABLA_EXPEDIENTES="TABLA_EXPEDIENTES";
    public static String TABLA_TERCEROS="TABLA_TERCEROS";
    public static String TABLA_HIST_TERCEROS="TABLA_HIST_TERCEROS";
    public static String TABLA_EXPTERCEROS="TABLA_EXPTERCEROS";
    
    public static final String TABLA_TIPO_DATO_NUMERICO = "TABLA_TIPO_DATO_NUMERICO";
    public static final String TABLA_TIPO_DATO_TEXTO = "TABLA_TIPO_DATO_TEXTO";
    public static final String TABLA_CONTADORES = "TABLA_CONTADORES";
    public static final String TABLA_VALORES_DESPLEGABLE = "TABLA_VALORES_DESPLEGABLE";
    
       
     //Constantes para acceder a los ficheros de propiedades
    public static final String CAMPO_SUPLEMENTARIO_CLAVE_REGISTRAL_CP = "CLAVEREGISTRALCP";
    public static final String CAMPO_SUPLEMENTARIO_CLAVE_REGISTRAL_APA = "CLAVEREGISTRALAPA";
    
    //Pares de claves para indicar si un centro está acreditado o no.
    public static Integer CENTRO_ACREDITADO_SI = 0;
    public static Integer CENTRO_ACREDITADO_NO = 1;
    
    
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String FORMATO_FECHA_HORA24 = "dd/MM/yyyy HH:mm:ss";
    
    public final class CODIGOS_DESPLEGABLE
     {
         public static final String TIPO_ACREDITACION = "CPAP";
         public static final String VALORACION = "VPAP";

     }

    public final class ROLES
     {
         public static final int INTERESADO = 1;
     }
    
    // Constantes para consulta EMPNL
    public static final String DOT_PUNTO = ".";
    
    public static final String EMPNL_CODIGO_PROCEDIMIENTO = "EMPNL_CODIGO_PROCEDIMIENTO";
    public static final String EMPNL_CODIGO_INTERNO_TRAMITE_VALORACION = "EMPNL_CODIGO_INTERNO_TRAMITE_VALORACION";
    public static final String EMPNL_NOMBRE_FICHERO_GENERADO = "EMPNL_NOMBRE_FICHERO_GENERADO";
    public static final String EMPNL_EXTENSION_FICHERO_GENERADO = "EMPNL_EXTENSION_FICHERO_GENERADO";
    
        // Campos suplementarios
    public static final String EMPNL_CAMPO_SUPL_FECHA_PRESENTACION = "EMPNL_CAMPO_SUPL_FECHA_PRESENTACION";
    public static final String EMPNL_CAMPO_SUPL_MOTIVO_EXENCION = "EMPNL_CAMPO_SUPL_MOTIVO_EXENCION";
    public static final String EMPNL_CAMPO_SUPL_ORIGEN_SOLICITUD = "EMPNL_CAMPO_SUPL_ORIGEN_SOLICITUD";
    public static final String EMPNL_CAMPO_SUPL_CENTRO = "EMPNL_CAMPO_SUPL_CENTRO";
    public static final String EMPNL_CAMPO_SUPL_OFICINA_LANBIDE = "EMPNL_CAMPO_SUPL_OFICINA_LANBIDE";
    public static final String EMPNL_CAMPO_SUPL_VALORACION_EXENCION = "EMPNL_CAMPO_SUPL_VALORACION_EXENCION";
    
        // Codigos de desplegables
    
    public static final String EMPNL_COD_DESP_MOTIV_EXENCION = "EMPNL_COD_DESP_MOTIV_EXENCION";
    public static final String EMPNL_COD_DESP_ORIGEN_SOLICITUD = "EMPNL_COD_DESP_ORIGEN_SOLICITUD";
    public static final String EMPNL_COD_DESP_CENTRO = "EMPNL_COD_DESP_CENTRO";
    public static final String EMPNL_COD_DESP_OFICINA_LANBIDE = "EMPNL_COD_DESP_OFICINA_LANBIDE";
    public static final String EMPNL_COD_DESP_FAVORABLE_DESFAVO = "EMPNL_COD_DESP_FAVORABLE_DESFAVO";

    // Variable properties Numero de Lineas x Hoja en el Excel
    public static final String NUMERO_LINEAS_HOJA_EXCEL = "NUMERO_LINEAS_HOJA_EXCEL";
    
    // Variable nombre de vista materializada
    public static final String NOMBRE_VISTA_MATERIALIZADA_TODAS_UC = "NOMBRE_VISTA_MATERIALIZADA_TODAS_UC";
    
    public static final String NUMERO_LINEAS_PAGINA = "NUMERO_LINEAS_PAGINA";
    
    public static final String TRAMITE_ENVIO_OFICINA = "TRAMITE_ENVIO_OFICINA";
    
    
    public static String TABLA_E_TFE = "E_TFE";
    
}//class