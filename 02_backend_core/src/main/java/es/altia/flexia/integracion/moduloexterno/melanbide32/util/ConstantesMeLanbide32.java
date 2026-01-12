/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.util;

/**
 *
 * @author santiagoc
 */
public class ConstantesMeLanbide32 
{    
    //Fichero de propiedades del modulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE32";
    public static final String FICHERO_CONF_PANTALLAS = "configuracionPantallasAYORI";
    
    //Barra separadora
    public static final String BARRA_SEPARADORA = "/";
    public static final String SEPARADOR_VALORES_CONF = ",";
    
    //Separador nombre archivos
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";
    
    //Codigo procedimiento
    public static final String CODIGO_PROCEDIMIENTO_AYORI = "AYORI";
    public static final String CODIGO_PROCEDIMIENTO_CEMP = "CEMP";
    
    //Nombres de las tablas en el fichero de propiedades.
    public static final String TABLA_ORI_AMBITOS_HORAS = "TABLA_ORI_AMBITOS_HORAS";
    public static final String TABLA_ORI_AMBITOS_CE = "TABLA_ORI_AMBITOS_CE";
    public static final String TABLA_ORI_ENTIDAD = "TABLA_ORI_ENTIDAD";
    public static final String TABLA_ORI_ESPECIALIDADES = "TABLA_ORI_ESPECIALIDADES";
    public static final String TABLA_ORI_CE_TRAYECTORIA = "TABLA_ORI_CE_TRAYECTORIA";
    public static final String TABLA_ORI_CE_UBIC = "TABLA_ORI_CE_UBIC";
    public static final String TABLA_ORI_ORI_TRAYECTORIA = "TABLA_ORI_ORI_TRAYECTORIA";
    public static final String TABLA_ORI_ORI_UBIC = "TABLA_ORI_ORI_UBIC";
    public static final String TABLA_ORI_TMP_ADJUDICACION_CENTROS = "TABLA_ORI_TMP_ADJUDICACION_CENTROS";
    public static final String TABLA_ORI_TMP_ADJUDICACION_CENTROS_DSD2017 = "TABLA_ORI_TMP_ADJUDICACION_CENTROS_DSD2017";
    public static final String TABLA_ORI_TMP_ADJUDICACION = "TABLA_ORI_TMP_ADJUDICACION";
    public static final String TABLA_PROVINCIAS = "TABLA_PROVINCIAS";
    public static final String TABLA_MUNICIPIOS = "TABLA_MUNICIPIOS";
    public static final String TABLA_REL_TERCERO_DOMICILIO = "TABLA_REL_TERCERO_DOMICILIO";
    public static final String TABLA_DOMICILIO = "TABLA_DOMICILIO";
    public static final String TABLA_ORI_UBICACIONES_HORAS = "TABLA_ORI_UBICACIONES_HORAS";
    public static final String TABLA_ORI_UBICACIONES_CE = "TABLA_ORI_UBICACIONES_CE";
    public static final String TABLA_ORI_AUDITORIA = "TABLA_ORI_AUDITORIA";
    public static final String TABLA_USUARIOS = "TABLA_USUARIOS";
    public static final String TABLA_E_CRO = "TABLA_E_CRO";
    public static final String TABLA_TERCEROS = "TABLA_TERCEROS";
    public static final String TABLA_T_DOT = "TABLA_T_DOT";
    public static final String TABLA_TIPO_VIA = "TABLA_TIPO_VIA";
    public static final String TABLA_VIAS = "TABLA_VIAS";
    public static final String TABLA_TRAMITES = "TABLA_TRAMITES";
    public static final String TABLA_EXPEDIENTES = "TABLA_EXPEDIENTES";
    public static final String TABLA_CAMPOSSUPTXT_TRA = "TABLA_CAMPOSSUPTXT_TRA";
    public static final String TABLA_ORI_AMBITOS_CE_SOLICITADOS="TABLA_ORI_AMBITOS_CE_SOLICITADOS";
    
    //Nombres de las secuencias en el fichero de propiedades
    public static final String SEQ_ORI_ENTIDAD = "SEQ_ORI_ENTIDAD";
    public static final String SEQ_ORI_ORI_UBICACION = "SEQ_ORI_ORI_UBICACION";
    public static final String SEQ_ORI_ORI_TRAYECTORIA = "SEQ_ORI_ORI_TRAYECTORIA";
    public static final String SEQ_ORI_CE_UBICACION = "SEQ_ORI_CE_UBICACION";
    public static final String SEQ_ORI_CE_TRAYECTORIA = "SEQ_ORI_CE_TRAYECTORIA";
    public static final String SEQ_ORI_AMBITOS_CE_SOLICITADOS = "SEQ_ORI_AMBITOS_CE_SOLICITADOS";
    
    //Constante para la clave del fichero de propiedades que indica el nombre del m√≥dulo.
    public static final String NOMBRE_MODULO = "NOMBRE_MODULO";
    
    //Otros
    public static final String CODIGO_PAIS_ESPANA = "108";
    
    public static final String SI = "S";
    public static final String NO = "N";
    
    public static final String OK = "OK";
    public static final String ERROR = "ERROR";
    
    //CODIGOS DE LOS PROCEDIMIENTOS (PARA AUDITORÕA)
    public static final int COD_PROC_ADJUDICA_HORAS = 0;
    public static final int COD_PROC_DOCUMENTACION_HORAS = 1;
    public static final int COD_PROC_CONSOLIDA_HORAS = 2;
    public static final int COD_PROC_DESHACER_CONSOLIDACION_HORAS = 3;
    public static final int COD_PROC_ADJUDICA_CENTROS = 4;
    public static final int COD_PROC_DOCUMENTACION_CENTROS = 5;
    public static final int COD_PROC_CONSOLIDA_CENTROS = 6;
    public static final int COD_PROC_DESHACER_CONSOLIDACION_CENTROS = 7;
    
    //CODIGOS DE TRAMITES
    public static final String COD_TRAMITE_DENEGACION = "12";
    public static final String COD_TRAMITE_CIERRE = "999";
    public static final String COD_TRAMITE_ANULADO = "12";
    public static final String COD_TRAMITE_RESOLUCION_PROVISIONAL = "3";
    public static final String COD_TRAMITE_EVALUACION_DOCUMENTACION_APORTADA = "2";
    
    //ROLES
    public static final String COD_ROL_REP_LEGAL = "1";
    public static final String COD_ROL_ENTIDAD = "2";
    
    public static final int ESTADO_CERRADO = 9;
    public static final int ESTADO_ANULADO = 1;
    
    // Trayectyarias Cargadas Automaticamente
    //public static final String TRAYECTORIA_PRECARGA_1 = "1. 327/2003 Dekretua, abenduraen 23koa. Enplegu Zentroak \n Decreto 327/2003, de 23 de diciembre. Centros de empleo";
    //public static final String TRAYECTORIA_PRECARGA_2 = "2. 2011Ko Lanbide-euskal Enplegu Zerbitzuaren Orientaziorako - Enplegu Zentroetarako deladia \n Convocatoria de orientacion- dentros de empleo Lanbide - Servicio Vasco de Empleo 2011";
    //public static final String TRAYECTORIA_PRECARGA_3 = "3. 2013Ko Lanbide-euskal Enplegu Zerbitzuaren Orientaziorako - Enplegu Zentroetarako deladia \n Convocatoria de orientacion- dentros de empleo Lanbide - Servicio Vasco de Empleo 2013";
    //public static final String TRAYECTORIA_PRECARGA_4 = "4. 2014Ko Lanbide-euskal Enplegu Zerbitzuaren - Enplegu Zentroetarako deladia \n Convocatoria de orientacion- dentros de empleo Lanbide - Servicio Vasco de Empleo 2014";
    //public static final String TRAYECTORIA_PRECARGA_5 = "5. 2015ko Lanbide-Euskal Enplegu Zerbitzuaren - Enplegu Zentroetarako deialdia \n Convocatoria de Centros de Empleo Lanbide - Servicio Vasco de Empleo 2015";
    
    // ***   Otros **** Nuevo Valor Campo Adjudicado en CEMP
    public static final String ADJUDICADO="ADJUDICADO";
    public static final String SUPLENTE="SUPLENTE";
    public static final String DENEGADO="DENEGADO";
    public static final String AMBITODIST="DISTRIBUIDO"; 
    
    // Cod Campos Splementarios Tramites
    public static final String COD_CAMPO_SUP_TRA_DENEGAR="DENEGAR";
    public static final String COD_CAMPO_SUP_TRA_MOTIVODENEGACION="MOTIVODENEGACION";
    public static final String COD_DESPLE_TIPO_AMBITO_CEMP="COD_DESPLE_TIPO_AMBITO_CEMP";
    
    
    
}
