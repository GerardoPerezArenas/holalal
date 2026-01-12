/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide54.util;

/**
 *
 * @author davidg
 */
public class ConstantesMeLanbide54 {
    
    public static String FICHERO_ADAPTADORES = "AdaptadoresPlateaLan6";
    //Nombre del procedimiento
    public static String ID_PROC_AACC = "PROCEDIMIENTO_ID_LAN65_AACC";    
    
    //Fichero de propiedades del modulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE54";  
    
    //Barra separadora
    public static final String BARRA_SEPARADORA = "/";
    public static final String SEPARADOR_VALORES_CONF = ",";
    
    //Separador nombre archivos
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";
    
    //Nombres de las tablas en el fichero de propiedades.

public static final String TABLA_RGEF_MODFORMATIVO="TABLA_RGEF_MODFORMATIVO";
public static final String TABLA_RGEF_ESPFORMATIVO="TABLA_RGCF_ESPFORMATIVO";
public static final String TABLA_RGEF_MATERIALCONSU="TABLA_RGEF_MATERIALCONSU";



    //Nombres de las secuencias en el fichero melanbide de propiedades

public static final String SEQ_RGEF_MODFOR="SEQ_RGEF_MODFOR";
public static final String SEQ_RGEF_ESPFOR="SEQ_RGEF_ESPFOR";
public static final String SEQ_RGEF_MATCON="SEQ_RGEF_MATCON";



    //Constante para la clave del fichero de propiedades que indica el nombre del módulo.
    public static final String NOMBRE_MODULO = "NOMBRE_MODULO";
    
    public static final String COD_PROCEDIMIENTO = "COD_PROCEDIMIENTO";      
    
   
    //Otros
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String OK = "OK";
    public static final String ERROR = "ERROR";
    public static final String CARACTERES_ESPECIALES = "[a-zA-Z0-9,.ºª_@#!()=¿?¡*-]*";
    //JOB
    public static final String CAMPO_SERVIDOR = "SERVIDOR";
    public static final String COD_ORG = "COD_ORG";    
    public static final String DOS_ENTORNOS = "DOS_ENTORNOS";   
    
    public static final String URL_WS_REGISTRO = "URL_WS_REGISTRO";    
    public static final String COD_UOR = "COD_UOR";      //El mismo para los 3 entornos
    public static final String TABLA_DOMICILIO = "TABLA_DOMICILIO";
    public static final String TABLA_T_VIA = "TABLA_T_VIA";    
   
    public static final String COMPROBACION_AGENCIA = "COMPROBACION_AGENCIA";
    public static final String GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB = "GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB";
    public static final String COMPROBACION_NUEVO_CENTRO = "COMPROBACION_NUEVO_CENTRO";
    public static final String COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO = "COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO";
    public static final String COMUNICACION_NUEVA_AGENCIA_CENTRO = "COMUNICACION_NUEVA_AGENCIA_CENTRO";
    public static final String NUEVA_VISITA = "NUEVA_VISITA";
    public static final String COMUNICACION_AGENCIA_YA_REGISTRADA = "COMUNICACION_AGENCIA_YA_REGISTRADA";
    public static final String CIERRE_DE_EXPEDIENTE = "CIERRE_DE_EXPEDIENTE";   
   
//Nombre de tags XML
   public static final String TAG_XML_CAMPOSVARIABLES = "CamposVariables";    
   public static final String TAG_XML_CAMPO = "Campo";
   public static final String TAG_XML_CODCAMPO = "CodCampo";
   public static final String TAG_XML_VALORCAMPO = "ValorCampo";    
   public static final String TAG_XML_CODIGO = "Codigo";
   public static final String TAG_XML_VALOR = "Valor";   

   public static final String CODIFICACION_ISO_8859_1 ="ISO-8859-1";     

//Servicios Web del SEPE
//Consulta Agencia   
    public static final String URL_SW_IWACCIFS="URL_SW_IWACCIFS";
    public static final String NAMESPACE_SW_IWACCIFS="NAMESPACE_SW_IWACCIFS";
    public static final String NAME_SW_IWACCIFS="NAME_SW_IWACCIFS";   

//Alta Agencia
    public static final String URL_SW_IWACALAS="URL_SW_IWACALAS";
    public static final String NAMESPACE_SW_IWACALAS="NAMESPACE_SW_IWACALAS";
    public static final String NAME_SW_IWACALAS="NAME_SW_IWACALAS";     
//Alta Centro
    public static final String URL_SW_IWACACTS="URL_SW_IWACACTS";
    public static final String NAMESPACE_SW_IWACACTS="NAMESPACE_SW_IWACACTS";
    public static final String NAME_SW_IWACACTS="NAME_SW_IWACACTS";         
//-----    
    public static final String USUARIO_SWSEPE="USUARIO_SWSEPE";    
    public static final String EXISTE_AGENCIA="EXISTE_AGENCIA";       
    public static final String NO_EXISTE_AGENCIA="NO_EXISTE_AGENCIA"; 

    public static final String TABLA_CAMPOS_TEXTO="TABLA_CAMPOS_TEXTO";  
    public static final String TABLA_CAMPOS_DESPLEGABLES="TABLA_CAMPOS_DESPLEGABLES";
    public static final String TABLA_AACC_CENTROS="TABLA_AACC_CENTROS";
    public static final String CAMPO_COD_AGENCIA="CAMPO_COD_AGENCIA";
    public static final String CAMPO_COD_CENTRO="CAMPO_COD_CENTRO";
    public static final String CAMPO_PAGWEB="CAMPO_PAGWEB";  
    public static final String CAMPO_ACTREALIZ="CAMPO_ACTREALIZ";  
    public static final String CAMPO_CORREOWEB="CAMPO_CORREOWEB";  
    public static final String CAMPO_ETT="CAMPO_ETT";      
    
    //Colores
    //public static final String ROJO = "red";
    
    //Tipos de datos requeridos validaciones
    public final class TiposRequeridos
    {
        public static final String NUMERICO_DECIMAL = "expectedType.decimalNumber";
        public static final String NUMERICO = "expectedType.number";
        public static final String FECHA = "expectedType.date";
        public static final String STRING = "expectedType.string";
        public static final String STRING_NIF = "expectedType.string_nif";
        public static final String STRING_CIF = "expectedType.string_cif";
    }


    public final class ROLES
    {
        public static final int INTERESADO = 1;
    }
    
}
