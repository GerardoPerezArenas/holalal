/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide03.util;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #86969 Edición inicial</li>
 * </ol> 
 */
public class ConstantesMeLanbide03 {
    
    //Fichero de propiedades del módulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE03";
    
    //Barra separadora
    public static final String BARRA_SEPARADORA = "/";
    
    //Separador nombre archivos
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";
    
    //Nombres de las tablas en el fichero de propiedades.
    public static final String CER_CERTIFICADOS = "TABLA_CER_CERTIFICADOS";
    public static final String CER_AREAS = "TABLA_CER_AREAS";
    public static final String CER_FAMILIAS = "TABLA_CER_FAMILIAS";
    public static final String CER_REL_MOD_CERT = "TABLA_CER_REL_MOD_CERT";
    public static final String CER_UNIDADES_COMPETENCIA = "TABLA_CER_UNIDADES_COMPETENCIA";    
    public static final String CER_MODULOS_FORMATIVOS = "TABLA_CER_MODULOS_FORMATIVOS";
    public static String VW_CENTROS_REG_REGEX = "VW_CENTROS_REG_REGEX";
    
    public static final String MELANBIDE03_CERTIFICADO = "TABLA_MELANBIDE03_CERTIFICADO";
    public static final String MELANBIDE03_CERT_CENTRO = "TABLA_MELANBIDE03_CERT_CENTRO";
    public static final String MELANBIDE03_MOD_PRACT = "TABLA_MELANBIDE03_MOD_PRACT";
    public static final String MELANBIDE03_REPORT = "TABLA_MELANBIDE03_REPORT";
    
    //Constante para la clave del fichero de propiedades que indica el nombre del módulo.
    public static final String NOMBRE_MODULO = "NOMBRE_MODULO";
    public static final String CODCENTRO_LANF = "CODCENTRO_LANF";
    public static final String CODORIGENACRED_LANF = "CODORIGENACRED_LANF";
    
    //Separadores usados para la separación de los datos que vienen de la JSP en a operación de guardar.
    public static final String SPLIT_UNIDADES = "@#@";
    public static final String SPLIT_PROPIEDADES_UNIDADES = "#@#";
    public static final String SPLIT_PROPIEDADES_MOD_FORM = "@#@";
    
    //Constantes para las claves de los campos suplementarios utilizados en el módulo.
    public static final String CAMPO_SUPLEMENTARIO_MOTIVO_NO_ACREDITADO = "CAMPO_SUPLEMENTARIO_MOTIVO_NO_ACREDITADO";
    public static final String CAMPO_SUPLEMENTARIO_MOTIVO_ACREDITADO_MOD_FORM = "CAMPO_SUPLEMENTARIO_MOTIVO_ACREDITADO_MOD_FORM";
    public static final String CAMPO_SUPLEMENTARIO_MOTIVO_NO_ACREDITADO_MOD_FORM = "CAMPO_SUPLEMENTARIO_MOTIVO_NO_ACREDITADO_MOD_FORM";
    public static final String CAMPO_SUPLEMENTARIO_CENTRO = "CAMPO_SUPLEMENTARIO_CENTROS";
    public static final String CAMPO_SUPLEMENTARIO_ORIGEN_ACREDITACION = "CAMPO_SUPLEMENTARIO_ORIGEN_ACREDITACION";
    
    //Pares de claves para indicar si un centro está acreditado o no.
    public static final Integer CENTRO_ACREDITADO_SI = 0;
    public static final Integer CENTRO_ACREDITADO_NO = 1;
    public static final Integer MODULO_ACREDITADO_SI = 0;
    public static final Integer MODULO_ACREDITADO_NO = 1;
    
    //Constantes para los resultados de la operacion de generar PDF.
    public static final String OPERACION_CORRECTA = "0";
    public static final String ERROR_RECUPERANDO_INTERESADOS = "1";
    public static final String ERROR_UNIDADES_CERTIFICADO = "2";
    public static final String ERROR_GENERANDO_REPORT = "3";
    
    //Constantes para las claves de la tabla y del codigo de campo del campo suplementario de fecha de nacimiento del tercero.
    public static final String TABLA_FECHA_NACIMIENTO = "TABLA_FEC_NAC_TER";
    public static final String CAMPO_FECHA_NACIMIENTO = "IDENTIFICADOR_FEC_NAC_TER";
    
    //Constante para definir la ruta en la que se guardan los informes del modulo.
    public static final String RUTA_INFORMES = "RUTA_INFORMES";
    public static final String RUTA_RECURSOS_INFORMES = "RUTA_RECURSOS_INFORMES";
    public static final String NOMBRE_PLANTILLA_REPORT_PDF_APA = "NOMBRE_PLANTILLA_REPORT_PDF_APA";
    public static final String NOMBRE_PLANTILLA_REPORT_PDF_APAJ = "NOMBRE_PLANTILLA_REPORT_PDF_APAJ";
    public static final String NOMBRE_PLANTILLA_REPORT_PDF_APAB = "NOMBRE_PLANTILLA_REPORT_PDF_APAB";
    public static final String NOMBRE_PLANTILLA_REPORT_PDF_APAA = "NOMBRE_PLANTILLA_REPORT_PDF_APAA";
    public static final String NOMBRE_PLANTILLA_REPORT_PDF_APA18 = "NOMBRE_PLANTILLA_REPORT_PDF_APA18";
    public static final String NOMBRE_PLANTILLA_REPORT_PDF_CERTIFICADO_POSITIVO_CP = "NOMBRE_PLANTILLA_REPORT_PDF_CERTIFICADO_POSITIVO_CP";
    public static final String NOMBRE_PLANTILLA_REPORT_PDF_CERTIFICADO_POSITIVO_APA = "NOMBRE_PLANTILLA_REPORT_PDF_CERTIFICADO_POSITIVO_APA";
    public static final String XPATH_PLANTILLA_PDF_APA = "XPATH_REPORT_PDF_APA";
    public static final String NOMBRE_ARCHIVO_REPORT_GENERADO_PDF_APA = "NOMBRE_ARCHIVO_REPORT_GENERADO_PDF_APA";
    
    public static final String NOMBRE_PLANTILLA_GENERAL = "NOMBRE_PLANTILLA_GENERAL";
    public static final String NOMBRE_PLANTILLA_GENERAL_VACIO = "NOMBRE_PLANTILLA_GENERAL_VACIO";
    
       
    public static final String CODIGO_TRAMITE_IMPRESION_APA="CODIGO_TRAMITE_IMPRESION_APA";
    public static final String CODIGO_TRAMITE_OFICIOS="CODIGO_TRAMITE_OFICIOS";
    public static final String TABLA_E_EXP = "TABLA_E_EXP";
    public static final String TABLA_E_TDE = "TABLA_E_TDE";
    public static final String TABLA_E_CRO = "TABLA_E_CRO";
    public static final String TABLA_E_TRA = "TABLA_E_TRA";
    public static final String TABLA_E_EXT = "TABLA_E_EXT";
    public static final String TABLA_T_HTE = "TABLA_T_HTE";
    public static final String TABLA_E_TFE = "TABLA_E_TFE";
    public static final String TABLA_MELANBIDE03_IMPRESION_APA = "TABLA_MELANBIDE03_IMPRESION_APA";
    public static final String TABLA_MELANBIDE03_OFICIOS_IMPRESION = "TABLA_MELANBIDE03_OFICIOS_IMPRESION";
    public static final String TABLA_MELANBIDE03_CERT_CENTRO = "TABLA_MELANBIDE03_CERT_CENTRO";
    public static final String TIPO_CAMPO_SUPL_TIPOACREDITACION= "TIPO_CAMPO_SUPL_TIPOACREDITACION";
    public static final String TIPOACREDITACION_APA = "TIPOACREDITACION_APA";
    
    public static final String CODIGO_PROCEDIMIENTO = "CEPAP";
    
    public static final String NOMBRE_MODULO_GENERADOR_CLAVE_REGISTRAL = "MELANBIDE37";

    //Constantes para comprobarUCAcreditadas
    public static final String CUCA_OPERACION_CORRECTA = "0";
    public static final String CUCA_ERROR_SINUCACREDITADA = "1";
    public static final String CUCA_ERROR = "2";
    
    //Constantes para comprobar UC y MP acreditados
    public static final String CUCMPA_OPERACION_CORRECTA = "0";
    public static final String CUCMPA_ERROR_UCNOACREDITADA = "1";
    public static final String CUCMPA_ERROR_MPNOACREDITADA = "2";
    public static final String CUCMPA_ERROR = "3";
    public static final String CUCMPA_ERROR_CERTIFICADO = "4";
    public static final String CUCMPA_ERROR_UNIDADES = "5";
    public static final String CUCMPA_ERROR_SINCERTIFICADO = "6";

    public static final String TIPO_ACREDITACION_APA= "APA";
    public static final String TIPO_ACREDITACION_CP= "CP";    
    
    public static final String CAMPO_SUPL_TIPOACREDITACION = "CAMPO_SUPL_TIPOACREDITACION";
    public static final String COD_VISI_TRAMITES_PERMITIR_AVANZAR = "COD_VISI_TRAMITES_PERMITIR_AVANZAR";
    public static final String PREFIJO_ESQUEMA_BBDD = "PREFIJO_ESQUEMA_BBDD";

    //jobs
    public static final String CAMPO_SERVIDOR = "SERVIDOR";
    public static final String COD_ORG = "COD_ORG";
    public static final String DOS_ENTORNOS = "DOS_ENTORNOS";
    
    //RD34
    public static final String URL_RD34 = "URL_RD34";
    public static final String ID_USUARIO_RD34 = "ID_USUARIO_RD34";
    public static final String PASS_RD34 = "PASS_RD34";
    public static final String RD34_COD_ERROR_CP_SUBIDO = "RD34_COD_ERROR_CP_SUBIDO";
    public static final String RD34_COD_ERROR_APA_SUBIDO = "RD34_COD_ERROR_APA_SUBIDO";
    public static final String RD34_CARGA = "RD34_CARGA";
    public static final String RD34_CARGA_EXP = "RD34_CARGA_EXP";
    public static final String RD34_MAX_NUM_CARGA = "RD34_MAX_NUM_CARGA";
    public static final String VW_REGEXLAN_CPS_FECHA_OBTENCION = "VW_REGEXLAN_CPS_FECHA_OBTENCION";
    public static final String PROXY_HOST = "PROXY_HOST";
    public static final String PROXY_PORT = "PROXY_PORT";
    public static final String COD_TRAMITE_ESPERA_EXPEDICION = "COD_TRAMITE_ESPERA_EXPEDICION";
    public static final String COD_TRAMITE_VALORACION_CP_APA = "COD_TRAMITE_VALORACION_CP_APA";
    
}//class