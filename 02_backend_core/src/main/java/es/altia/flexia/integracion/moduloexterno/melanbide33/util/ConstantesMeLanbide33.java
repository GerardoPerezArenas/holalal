/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide33.util;

/**
 *
 * @author santiagoc
 */
public class ConstantesMeLanbide33 
{
    //Fichero de propiedades del módulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE33"; 
    
    //Barra separadora
    public static final String BARRA_SEPARADORA = "/";
    public static final String SEPARADOR_VALORES_CONF = ",";
    
    //Barra separadora Doble Idioma Desplegables
    public static final String BARRA_SEPARADORA_IDIOMA_DESPLEGABLES = "BARRA_SEPARADORA_IDIOMA_DESPLEGABLES";
    public static final int CODIGO_IDIOMA_CASTELLANO = 1;
    public static final int CODIGO_IDIOMA_EUSKERA = 4;
    
    //Nombre del procedimiento
    public static final String NOMBRE_PROCEDIMIENTO = "NOMBRE_PROCEDIMIENTO";
    
    //Nombres de las tablas en el fichero de propiedades.
    public static final String TABLA_E_CRO = "TABLA_E_CRO";
    public static final String TABLA_TRAMITES = "TABLA_TRAMITES";
    public static final String TABLA_VALORDESPLEGCAMPOSUPL = "TABLA_VALORDESPLEGCAMPOSUPL";
    public static final String TABLA_VALORNUMCAMPOSUPL = "TABLA_VALORNUMCAMPOSUPL";
    public static final String TABLA_S75PAGOS = "TABLA_S75PAGOS";
    public static final String TABLA_S75ETIQUETAS = "TABLA_S75ETIQUETAS";
    public static final String TABLA_S75RELACIONTECNICOPUESTOS = "TABLA_S75RELACIONTECNICOPUESTOS";
    public static final String TABLA_S75CONCEPTOS = "TABLA_S75CONCEPTOS";
    public static final String TABLA_RELACIONEXPTSFLEXIA = "TABLA_RELACIONEXPTSFLEXIA";
    public static final String TABLA_RELACIONTERCEROSXEXPTS = "TABLA_RELACIONTERCEROSXEXPTS";
    public static final String MELANBIDE33_SUBSOLIC = "MELANBIDE33_SUBSOLIC";
    
    //TRAMITERESOLUCION
    public static final String TRAM_RESOL = "TRAM_RESOL";
    
    // Codigo campo Suplementario total importe pagar en 3 pago 3 anio  
    public static final String COD_CAMPOSUP_IMPTOTA_3ANIO3PAGO = "COD_CAMPOSUP_IMPTOTA_3ANIO3PAGO";
    // Codigo campo Suplementario  para validar paso a ET de SEI
    public static final String COD_CAMPOSUP_PASOETSEI = "COD_CAMPOSUP_PASOETSEI";
    // CODIGO DE LOS TRAMITES QUE VA A VALIDAR PASO A ET - SE GUARDA CAMPO SUPLEMENTARIO CON VALOR 1, 
    // sino esta en esta lista en properties y se integra la operación, se guardará 0, no validará el paso a ET
    public static final String COD_VIS_TRAM_VALIDAPASO_ET = "COD_VIS_TRAM_VALIDAPASO_ET";
    public static final String DOT_COMMA = ";";

    //Nombre de Sequencias ID 
    public static final String SEQ_MELANBIDE33_SUBSOLIC = "SEQ_MELANBIDE33_SUBSOLIC";    
    // Desplegables
    public static final String TABLA_VALORES_DESPLEGABLES = "TABLA_VALORES_DESPLEGABLES";
    public static final String COD_DES_DTSV = "COD_DES_DTSV";
}
