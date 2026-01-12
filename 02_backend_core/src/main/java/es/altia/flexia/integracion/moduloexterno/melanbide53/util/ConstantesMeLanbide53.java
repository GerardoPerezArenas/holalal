/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide53.util;

/**
 *
 * @author davidg
 */
public class ConstantesMeLanbide53 {

    public ConstantesMeLanbide53() {
    }

    //Fichero de propiedades del modulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE53";

    //Separadores
    public static final String BARRA_SEPARADORA = "/";
    public static final String SEPARADOR_VALORES_CONF = ",";
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";
    public static final String DOT = ".";

    //Organización
    public static final String COD_ORGANIZACION = "COD_ORGANIZACION";

    //Nombres de las tablas en el fichero de propiedades.
    public static final String TABLA_MELANBIDE53_REG_ERR = "TABLA_MELANBIDE53_REG_ERR";
    public static final String TABLA_MELANBIDE53_IDEN_ERR = "TABLA_MELANBIDE53_IDEN_ERR";
    public static final String TABLA_MELANBIDE53_ESTADISTICAS = "TABLA_MELANBIDE53_ESTADISTICAS";
    public static final String TABLA_MELANBIDE53_ESTADISTICAS_HIST = "TABLA_MELANBIDE53_ESTADISTICAS_HIST";

    //Constante para la clave del fichero de propiedades que indica el nombre del modulo.
    public static final String NOMBRE_MODULO = "NOMBRE_MODULO";

    // Otros
    public static final String CARACTERES_ESPECIALES = "CARACTERES_ESPECIALES";
    public static final String CARACTERES_ESPECIALES_XML = "CARACTERES_ESPECIALES_XML";
    public static final String ESCAPE_CARACTERES_ESPECIALES_XML = "ESCAPE_CARACTERES_ESPECIALES_XML";
    public static final String NUMERO_MAX_LINEAS_TABLAINICIAL = "NUMERO_MAX_LINEAS_TABLAINICIAL";
    public static final String BOOLEAN_TRUE = "true";
    public static final String BOOLEAN_FALSE = "false";
    public static final String STANDFOR_SI_S = "S";
    public static final String STANDFOR_NO_N = "N";
    public static final String IS_NOT_NULL = "IS NOT NULL";
    public static final String CAMPO_FECHA_ERROR = "CAMPO_FECHA_ERROR_REG_ERR";
    public static final String CAMPO_FECHA_REVISION = "CAMPO_FECHA_REVISION_REG_ERR";
    public static final String CAMPO_FECHA_REGISTRO = "CAMPO_FECHA_REGISTRO_ESTADISTICAS";

    public static final String NUMERO_LINEAS_PAGINA = "NUMERO_LINEAS_PAGINA";

    // PARA EL DESPLEGABLE DE NOTIFICADO Y REVISADO --  COMPLETADOS LA S Y N MAS DESCRIPCIONES 
    // LAS DESCRIPCIONES LAS COGEMOS DEL PROPERTIES POR SI HAY QUE TRADUCIRLAS
    public static final String STANDFOR_TODOS_T = "T";
    public static final String DESC_DESP_OPCION_T_TODOS = "DESC_DESP_OPCION_T_TODOS";
    public static final String DESC_DESP_OPCION_S_SI = "DESC_DESP_OPCION_S_SI";
    public static final String DESC_DESP_OPCION_N_NO = "DESC_DESP_OPCION_N_NO";

    public static final String SYS_NO_PROCEDE = "NO PROCEDE";

    public static final String OK = "OK";
    public static final String KO = "KO";

    //Constantes de equivalencia de eventos
    public static final String PRESENTACION_SOLICITUD = "PRESENTACION DE SOLICITUD - 1";
    public static final String APORTACION_DOCUMENTOS_2 = "APORTACION DE DOCUMENTOS - 2";
    public static final String APORTACION_DOCUMENTOS_3 = "APORTACION DE DOCUMENTOS - 3";
    public static final String APORTACION_DOCUMENTOS_5 = "APORTACION DE DOCUMENTOS - 5";
    public static final String APORTACION_DOCUMENTOS_6 = "APORTACION DE DOCUMENTOS - 6";
    public static final String APORTACION_DOCUMENTOS_7 = "APORTACION DE DOCUMENTOS - 7";
    public static final String APORTACION_DOCUMENTOS_9 = "APORTACION DE DOCUMENTOS - 9";
    public static final String APORTACION_DOCUMENTOS_13 = "APORTACION DE DOCUMENTOS - 13";
    public static final String APORTACION_DOCUMENTOS_16 = "APORTACION DE DOCUMENTOS - 16";
    public static final String APORTACION_DOCUMENTOS_17 = "APORTACION DE DOCUMENTOS - 17";
    public static final String APORTACION_DOCUMENTOS_18 = "APORTACION DE DOCUMENTOS - 18";
    public static final String LOCATOR_RESULT = "LOCATOR_RESULT";
    public static final String PUBLISH_NOTIFICATION_DELIVERY_RESOL = "PUBLISH_NOTIFICATION_DELIVERY - RESOL";
    public static final String PUBLISH_NOTIFICATION_DELIVERY_REQ_SUB = "PUBLISH_NOTIFICATION_DELIVERY - REQ_SUB";
    public static final String PUBLISH_NOTIFICATION_DELIVERY_ACEPT = "PUBLISH_NOTIFICATION_DELIVERY - ACEPT";
    public static final String PUBLISH_NOTIFICATION_DELIVERY_RESOL_RECUR = "PUBLISH_NOTIFICATION_DELIVERY - RESOL_RECUR";
    public static final String TRANSFORMATION_RESULT = "TRANSFORMATION_RESULT";
    public static final String MANAGE_NOTIFICATION_EXPIRATION = "MANAGE_NOTIFICATION_EXPIRATION";
    public static final String REGISTRATION_DELIVERY = "REGISTRATION_DELIVERY";
    public static final String R02_SIGN = "R02_SIGN - OK";

    //Constantes de equivalencia de eventos textos.
    public static final String PRESENTACION_SOLICITUD_TXT = "Presentación de Solicitud";
    public static final String APORTACION_DOCUMENTOS_2_TXT = "Adjuntar documentación al expediente estándar";
    public static final String APORTACION_DOCUMENTOS_3_TXT = "Presentación de Alegaciones";
    public static final String APORTACION_DOCUMENTOS_5_TXT = "Presentación de Recurso Alzada";
    public static final String APORTACION_DOCUMENTOS_6_TXT = "Presentación de Desistimiento";
    public static final String APORTACION_DOCUMENTOS_7_TXT = "Presentación de Renuncia";
    public static final String APORTACION_DOCUMENTOS_9_TXT = "Modificación de los Datos de Notificación";
    public static final String APORTACION_DOCUMENTOS_13_TXT = "Modificación de los Datos de Aviso";
    public static final String APORTACION_DOCUMENTOS_16_TXT = "Presentación de Recurso Potestativo de Reposición";
    public static final String APORTACION_DOCUMENTOS_17_TXT = "Aportación de Documentación en N Instancia";
    public static final String APORTACION_DOCUMENTOS_18_TXT = "Subsanación de Documentación";
    public static final String LOCATOR_RESULT_TXT = "Generación de Localizador de documento";
    public static final String PUBLISH_NOTIFICATION_DELIVERY_REQ_SUB_TXT = "Notificación de Requerimiento de Subsanación";
    public static final String PUBLISH_NOTIFICATION_DELIVERY_RESOL_TXT = "Notificación de Resolución";
    public static final String PUBLISH_NOTIFICATION_DELIVERY_ACEPT_TXT = "Notificación de Resolución de Renuncia";
    public static final String PUBLISH_NOTIFICATION_DELIVERY_RESOL_RECUR_TXT = "Notificación de Resolución de Recurso";
    public static final String TRANSFORMATION_RESULT_TXT = "Transformación de documento a PDF/A";
    public static final String MANAGE_NOTIFICATION_EXPIRATION_TXT = "Expiración de Notificación (Notificación no acusada)";
    public static final String REGISTRATION_DELIVERY_TXT = "Registro procedente del SIR";
    public static final String R02_SIGN_TXT = "Firma";
}
