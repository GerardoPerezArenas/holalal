/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide61.util;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #86969 Edición inicial</li>
 * </ol> 
 */
public class ConstantesMeLanbide61 {
    
    //Fichero de propiedades del módulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE61";
    
    //Barra separadora
    public static final String BARRA_SEPARADORA = "/";
    // pipe
    public static final String PIPE = "|";
    //Separador nombre archivos
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";   
    //Barra separadora Doble Idioma Desplegables
    public static final String BARRA_SEPARADORA_IDIOMA_DESPLEGABLES = "BARRA_SEPARADORA_IDIOMA_DESPLEGABLES";
    public static final int CODIGO_IDIOMA_CASTELLANO = 1;
    public static final int CODIGO_IDIOMA_EUSKERA = 4;
    
        
    //Nombres de las tablas en el fichero de propiedades.
    public static final String RPLEM_PEXCO = "TABLA_REPLE_PEXCO";
    public static final String TABLA_J1201V00 = "TABLA_J1201V00";
    public static final String TABLA_PORCENTAJE_PAGO = "TABLA_PORCENTAJE_PAGO";
    public static final String TABLA_IMPORTES = "TABLA_IMPORTES";
    public static final String TABLA_E_DES_VAL = "TABLA_E_DES_VAL";
    public static final String TABLA_E_CRO = "TABLA_E_CRO";
    public static final String TABLA_E_TNU = "TABLA_E_TNU";
    public static final String TABLA_CCCCAPV = "TABLA_CCCCAPV";
    
    //public static final String TABLA_TIPO_DOCUMENTO = "TABLA_TIPO_DOCUMENTO;
    public static final String TABLA_SUBVENCIONES = "TABLA_SUBVENCIONES";    
    //nombres de secuencias ID para las tablas del módulo
    public static final String SEQ_CCCCAPV = "SEQ_CCCCAPV";

    
  
    
    //Constante para la clave del fichero de propiedades que indica el nombre del módulo.
    public static final String NOMBRE_MODULO = "NOMBRE_MODULO";
    
    //Separadores usados para la separación de los datos que vienen de la JSP en a operación de guardar.
    
    //Constantes para las claves de los campos suplementarios utilizados en el módulo.
    public static final String CAMPO_SUPLEMENTARIO_ESTUDIOS = "CAMPO_SUPLEMENTARIO_ESTUDIOS";
    public static final String CAMPO_SUPLEMENTARIO_COLECTIVOS = "CAMPO_SUPLEMENTARIO_COLECTIVOS";
    public static final String CAMPO_SUPLEMENTARIO_TIP_CON_DUR = "CAMPO_SUPLEMENTARIO_TCD";
    public static final String CAMPO_SUPLEMENTARIO_TIPO_JORNADA = "CAMPO_SUPLEMENTARIO_TIPO_JORNADA";
    public static final String CAMPO_SUPLEMENTARIO_CNOE = "CAMPO_SUPLEMENTARIO_CNOE";
    public static final String CAMPO_SUPLEMENTARIO_TIPOCON = "CAMPO_SUPLEMENTARIO_TIPOCON";
    public static final String CAMPO_SUPLEMENTARIO_COLEC2 = "CAMPO_SUPLEMENTARIO_COLEC2";
    public static final String CAMPO_SUPLEMENTARIO_TCON = "CAMPO_SUPLEMENTARIO_TCON";
    public static final String CAMPO_NUMEXP_P29 = "NUMEXPP29";
    public static final String CAMPO_IMPORTE_TOTAL = "CAMPO_IMPORTE_TOTAL";
    public static final String CAMPO_IMPORTE_PRIMERO = "CAMPO_IMPORTE_PRIMERO";
    public static final String CAMPO_IMPORTE_SEGUNDO = "CAMPO_IMPORTE_SEGUNDO";
    public static final String CAMPO_ESTADO = "CAMPO_ESTADO";
    public static final String SEQ_SUBSOLIC = "SEQ_SUBSOLIC";
    public static final String TABLA_SUBSOLIC = "TABLA_SUBSOLIC";
    //Pares de claves para indicar si un centro está acreditado o no.
    
    //Constantes para los resultados de la operacion de generar PDF.
    
    //Constantes para las claves de la tabla y del codigo de campo del campo suplementario de fecha de nacimiento del tercero.
    
    //Constante para definir la ruta en la que se guardan los informes del modulo.
    
    //OTROS
    public static final String CHECK_S = "S";
    public static final String CHECK_N = "N";
    public static final String SEXO_H = "H";
    public static final String SEXO_M = "M";
    public static final String COD_PROC = "REPLE";
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String TIPO_AYUDA_RPLEM = "L79";
    
    public static final String FEC20MESESREL = "FEC20MESESREL";
    
    public static final int ESTADO_EXPEDIENTE_CERRADO = 9;
    public static final int ESTADO_EXPEDIENTE_ANULADO = 1;
    
    //JOB
    public static String CAMPO_SERVIDOR = "SERVIDOR";
    public static String COD_ORG = "COD_ORG";    
    public static String DOS_ENTORNOS = "DOS_ENTORNOS";
}//class