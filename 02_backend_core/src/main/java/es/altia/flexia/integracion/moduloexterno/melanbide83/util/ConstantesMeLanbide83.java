package es.altia.flexia.integracion.moduloexterno.melanbide83.util;

import java.util.ResourceBundle;

public class ConstantesMeLanbide83 {
    //Fichero de propiedades del modulo.
    private static final String FICHERO_PROPIEDADES = "MELANBIDE83";
    
    //Caracteres separadores
    private static final String BARRA_SEPARADORA = "/";
    private static final String SEPARADOR_VALORES_CONF = ";";
    private static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";
    
    //Formatos
    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final String FORMATO_DECIMAL = "#,###,##0.00";
    
    //Codigo del procedimiento
    private static final String COD_PROCEDIMIENTO = "ATASE";
    
    // Constantes en el fichero de propiedades para los codigos de desplegables
    private static final String COD_DESPL_ESTADO = "COD_DESPL_ESTADO";
    private static final String COD_DESPL_CONCEPTO = "COD_DESPL_CONCEPTO";
    private static final String COD_DESPL_SN = "COD_DESPL_SN";

    // Recuperamos el valor de la propiedad codigo de desplegable para el estado
    public static String getPropVal_COD_DESPL_EST(String codOrg,String codProc){
        return getPROPERTIES().getString(codOrg+"/MODULO_INTEGRACION/"+getNOMBRE_MODULO()+"/"+codProc+"/DESPLEGABLES/"+COD_DESPL_ESTADO);
    }
    
    // Recuperamos el valor de la propiedad codigo de desplegable para el concepto
    public static String getPropVal_COD_DESPL_CTAT(String codOrg, String codProc,Integer ejercicio){
        return getPROPERTIES().getString(codOrg+"/MODULO_INTEGRACION/"+getNOMBRE_MODULO()+"/"+codProc+"/DESPLEGABLES/"+COD_DESPL_CONCEPTO);
    }
   
    // Recuperamos el valor de la propiedad codigo de desplegable con valores SI/NO
    public static String getPropVal_COD_DESPL_SN(String codOrg, String codProc){
        return getPROPERTIES().getString(codOrg+"/MODULO_INTEGRACION/"+getNOMBRE_MODULO()+"/"+codProc+"/DESPLEGABLES/"+COD_DESPL_SN);
    }
    
    private static final ResourceBundle PROPERTIES = ResourceBundle.getBundle("MELANBIDE83");
    
    //Nombre del módulo en el properties
    private static final String NOMBRE_MODULO = PROPERTIES.getString("NOMBRE_MODULO");
    
    //Nombre de las tablas y secuencias en BBDD en el properties
    private static final String PREF_SECUENCIA = "SEQ_";
    private static final String SUF_FACTURAS = "_FACTURAS";
    
    private static final String NOMBRE_TABLA_FACTURAS = getPROPERTIES().getString(getNOMBRE_MODULO()+getSUF_FACTURAS());    
    private static final String NOMBRE_SEQ_FACTURAS = getPROPERTIES().getString(getPREF_SECUENCIA()+getNOMBRE_MODULO()+getSUF_FACTURAS());

    /**
     * @return the NOMBRE_TABLA_FACTURAS
     */
    public static String getNOMBRE_TABLA_FACTURAS() {
        return NOMBRE_TABLA_FACTURAS;
    }

    /**
     * @return the NOMBRE_SEQ_FACTURAS
     */
    public static String getNOMBRE_SEQ_FACTURAS() {
        return NOMBRE_SEQ_FACTURAS;
    }

    /**
     * @return the PROPERTIES
     */
    public static ResourceBundle getPROPERTIES() {
        return PROPERTIES;
    }

    /**
     * @return the PREF_SECUENCIA
     */
    public static String getPREF_SECUENCIA() {
        return PREF_SECUENCIA;
    }

    /**
     * @return the SUF_FACTURAS
     */
    public static String getSUF_FACTURAS() {
        return SUF_FACTURAS;
    }

    /**
     * @return the NOMBRE_MODULO
     */
    public static String getNOMBRE_MODULO() {
        return NOMBRE_MODULO;
    }

    /**
     * @return the FICHERO_PROPIEDADES
     */
    public static String getFICHERO_PROPIEDADES() {
        return FICHERO_PROPIEDADES;
    }

    /**
     * @return the BARRA_SEPARADORA
     */
    public static String getBARRA_SEPARADORA() {
        return BARRA_SEPARADORA;
    }
     //Barra separadora Doble Idioma Desplegables
    public static final String BARRA_SEPARADORA_IDIOMA_DESPLEGABLES = "BARRA_SEPARADORA_IDIOMA_DESPLEGABLES";
    public static final int CODIGO_IDIOMA_CASTELLANO = 1;
    public static final int CODIGO_IDIOMA_EUSKERA = 4;

    /**
     * @return the SEPARADOR_VALORES_CONF
     */
    public static String getSEPARADOR_VALORES_CONF() {
        return SEPARADOR_VALORES_CONF;
    }

    /**
     * @return the SEPARADOR_NOMBRE_ARCHIVOS
     */
    public static String getSEPARADOR_NOMBRE_ARCHIVOS() {
        return SEPARADOR_NOMBRE_ARCHIVOS;
    }

    /**
     * @return the FORMATO_FECHA
     */
    public static String getFORMATO_FECHA() {
        return FORMATO_FECHA;
    }

    /**
     * @return the FORMATO_DECIMAL
     */
    public static String getFORMATO_DECIMAL() {
        return FORMATO_DECIMAL;
    }

    /**
     * @return the COD_PROCEDIMIENTO
     */
    public static String getCOD_PROCEDIMIENTO() {
        return COD_PROCEDIMIENTO;
    }

    /**
     * @return the COD_DESPL_ESTADO
     */
    public static String getCOD_DESPL_ESTADO() {
        return COD_DESPL_ESTADO;
    }

    /**
     * @return the COD_DESPL_CONCEPTO
     */
    public static String getCOD_DESPL_CONCEPTO() {
        return COD_DESPL_CONCEPTO;
    }

    /**
     * @return the COD_DESPL_SN
     */
    public static String getCOD_DESPL_SN() {
        return COD_DESPL_SN;
    }

}
