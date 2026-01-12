package es.altia.flexia.integracion.moduloexterno.melanbide69.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ConstantesMeLanbide69 {
    //Fichero de propiedades del modulo.
    private static final String FICHERO_PROPIEDADES = "MELANBIDE69";
    
    //Caracteres separadores
    private static final String BARRA_SEPARADORA = "/";
    private static final String SEPARADOR_VALORES_CONF = ";";
    private static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";
    //Barra separadora Doble Idioma Desplegables
    public static final String BARRA_SEPARADORA_IDIOMA_DESPLEGABLES = "BARRA_SEPARADORA_IDIOMA_DESPLEGABLES";
    public static final int CODIGO_IDIOMA_CASTELLANO = 1;
    public static final int CODIGO_IDIOMA_EUSKERA = 4;
    // pipe
    public static final String PIPE = "|";
    //Formatos
    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final String FORMATO_DECIMAL = "#,###,##0.00";
    
    //Codigo del procedimiento
    private static final String COD_PROCEDIMIENTO = "APES";
    
    // Constantes en el fichero de propiedades para los codigos de desplegables
    private static final String COD_DESPL_ESTADO = "COD_DESPL_ESTADO";
    private static final String COD_DESPL_CONCEPTO = "COD_DESPL_CONCEPTO";
    private static final String COD_DESPL_DESGLOSE_CPTO = "COD_DESPL_DESGLOSE_CPTO";
    private static final String COD_DESPL_SN = "COD_DESPL_SN";
    
    // Constantes en el fichero de propiedades para los codigos de campos suplementarios
    private static final String CS_SUBSANAR = "SUBSANAR";
    private static final String CS_FECSUBSANAR = "FECSUBSANAR";
    private static final String CS_PRIMERPAGO = "PRIMERPAGO";
    private static final String CS_PRIMERPAGOCB = "PRIMERPAGOCB";
    private static final String CS_IMPREINTEGRO = "IMPREINTEGRO";
    private static final String CS_REINTEGRAR = "REINTEGRAR";
    private static final String CS_SEGPAGO2 = "SEGPAGO2";
    private static final String CS_FECACUSERECIBO = "FECACUSERECIBO";
    private static final String CS_FECENVIORESOL = "FECENVIORESOL";
    private static final String CS_FECRESOL = "FECRESOLUCION";

    private static final String CS_FECALTAIAE = "FECALTAIAE";
    private static final String CS_FECHAPRESENTACION = "FECHAPRESENTACION";
    private static final String CS_FECPRIMERPAGO = "FECPRIMERPAGO";
    private static final String CS_SUBV = "SUBV";
    private static final String CS_SUBVCB = "SUBVCB";
    private static final String CS_CAUSAREINT = "CAUSAREINT";
    private static final String CS_CANTMIN = "CANTMINORADA";
    //Motivo de reintegro
    private static final String COD_MOTIVOREINT = "6";
    private static final String VAL_MOTIVOREINT = "JUSTIFICACI�N < AL PAGO";
    //Motivo de reintegro
    private static final String COD_AREINTEGRAR = "X";
    
    // Recuperamos el valor de la propiedad codigo de desplegable para el estado
    public static String getPropVal_COD_DESPL_EST(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/DESPLEGABLES/" + COD_DESPL_ESTADO);
    }
    
    // Recuperamos el valor de la propiedad codigo de desplegable para el concepto
    public static String getPropVal_COD_DESPL_CPTO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/DESPLEGABLES/" + COD_DESPL_CONCEPTO);
    }
    
    // Recuperamos el valor de la propiedad codigo de desplegable para el desglose de conceptos
    public static String getPropVal_COD_DESPL_DESGLOSE_CPTO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/DESPLEGABLES/" + COD_DESPL_DESGLOSE_CPTO);
    }
    
    // Recuperamos el valor de la propiedad codigo de desplegable con valores SI/NO
    public static String getPropVal_COD_DESPL_SN(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/DESPLEGABLES/" + COD_DESPL_SN);
    }
    
    private static final ResourceBundle PROPERTIES = ResourceBundle.getBundle("MELANBIDE69");
    
    //Nombre del m�dulo en el properties
    private static final String NOMBRE_MODULO = PROPERTIES.getString("NOMBRE_MODULO");
    
    //Nombre de las tablas y secuencias en BBDD en el properties
    private static final String PREF_SECUENCIA = "SEQ_";
    private static final String SUF_FACTURAS = "_FACTURAS";
    
    private static final String NOMBRE_TABLA_FACTURAS = getPROPERTIES().getString(getNOMBRE_MODULO() + getSUF_FACTURAS());
    private static final String NOMBRE_SEQ_FACTURAS = getPROPERTIES().getString(getPREF_SECUENCIA() + getNOMBRE_MODULO() + getSUF_FACTURAS());

    private static final String NOMBRE_TABLA_SOCIOS = "MELANBIDE69_SOCIOS";
    private static final String NOMBRE_SEQ_SOCIOS = "SEQ_M69_SOCIOS";
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
     * @return the COD_DESPL_DESGLOSE_CPTO
     */
    public static String getCOD_DESPL_DESGLOSE_CPTO() {
        return COD_DESPL_DESGLOSE_CPTO;
    }

    /**
     * @return the COD_DESPL_SN
     */
    public static String getCOD_DESPL_SN() {
        return COD_DESPL_SN;
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_SUBSANAR(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_SUBSANAR);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECSUBSANAR(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_FECSUBSANAR);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_PRIMERPAGO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_PRIMERPAGO);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_PRIMERPAGOCB(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_PRIMERPAGOCB);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECPRIMERPAGO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_FECPRIMERPAGO);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_IMPREINTEGRO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_IMPREINTEGRO);
    }

    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_REINTEGRAR(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_REINTEGRAR);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_SEGPAGO2(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_SEGPAGO2);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECACUSERECIBO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_FECACUSERECIBO);
    }
     
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECALTAIAE(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_FECALTAIAE);
    }
     
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECHAPRESENTACION(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_FECHAPRESENTACION);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_SUBV(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_SUBV);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_SUBVCB(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_SUBVCB);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_CAUSAREINT(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_CAUSAREINT);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_SUBSANAR_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_SUBSANAR);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECSUBSANAR_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_FECSUBSANAR);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_PRIMERPAGO_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_PRIMERPAGO);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_PRIMERPAGOCB_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_PRIMERPAGOCB);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_IMPREINTEGRO_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_IMPREINTEGRO);
    }

    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_REINTEGRAR_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_REINTEGRAR);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_SEGPAGO2_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_SEGPAGO2);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECACUSERECIBO_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_FECACUSERECIBO);
    }
       
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECALTAIAE_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_FECALTAIAE);
    }
    
        // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECHAPRESENTACION_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_FECHAPRESENTACION);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_SUBV_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_SUBV);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_SUBVCB_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_SUBVCB);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_CAUSAREINT_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_CAUSAREINT);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECPRIMERPAGO_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_FECPRIMERPAGO);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_CANTMIN(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_CANTMIN);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_CANTMIN_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/TIPO/" + CS_CANTMIN);
    }
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_FECENVIO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + BARRA_SEPARADORA + codProc + "/CAMPO_SUP/CODIGO/" + CS_FECENVIORESOL);
    }

    public static String getPropVal_CS_FECRESOL(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + BARRA_SEPARADORA + codProc + "/CAMPO_SUP/CODIGO/" + CS_FECRESOL);
    }

    public static String getPropVal_CS_FECENVIO_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + BARRA_SEPARADORA + codProc + "/CAMPO_SUP/TIPO/" + CS_FECENVIORESOL);
    }

    public static String getPropVal_CS_FECRESOL_TIPO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + BARRA_SEPARADORA + codProc + "/CAMPO_SUP/TIPO/" + CS_FECRESOL);
    }

    //Recuperamos todos los c�digos de campos suplementarios
    public static ArrayList<String> getPropVal_CamposSuplementariosLeer(String codOrg, String codProc) {
        ArrayList<String> codigos = new ArrayList<String>();
        codigos.add(getPropVal_CS_SUBSANAR(codOrg, codProc));
        codigos.add(getPropVal_CS_FECSUBSANAR(codOrg, codProc));
        codigos.add(getPropVal_CS_PRIMERPAGO(codOrg, codProc));
        codigos.add(getPropVal_CS_SEGPAGO2(codOrg, codProc));
        codigos.add(getPropVal_CS_IMPREINTEGRO(codOrg, codProc));
        codigos.add(getPropVal_CS_SUBV(codOrg, codProc));
        codigos.add(getPropVal_CS_CAUSAREINT(codOrg, codProc));
        
        return codigos;
    }
    
    //Recuperamos todos los c�digos de campos suplementarios
    public static HashMap<String, String> getPropVal_CS_CodYTipoLeer(String codOrg, String codProc) {
        HashMap<String, String> valores = new HashMap<String, String>();
        valores.put(getPropVal_CS_SUBSANAR(codOrg, codProc), getPropVal_CS_SUBSANAR_TIPO(codOrg, codProc));
        valores.put(getPropVal_CS_FECSUBSANAR(codOrg, codProc), getPropVal_CS_FECSUBSANAR_TIPO(codOrg, codProc));
        valores.put(getPropVal_CS_PRIMERPAGO(codOrg, codProc), getPropVal_CS_PRIMERPAGO_TIPO(codOrg, codProc));
        valores.put(getPropVal_CS_SEGPAGO2(codOrg, codProc), getPropVal_CS_SEGPAGO2_TIPO(codOrg, codProc));
        valores.put(getPropVal_CS_IMPREINTEGRO(codOrg, codProc), getPropVal_CS_IMPREINTEGRO_TIPO(codOrg, codProc));
        valores.put(getPropVal_CS_SUBV(codOrg, codProc), getPropVal_CS_SUBV_TIPO(codOrg, codProc));
        valores.put(getPropVal_CS_CAUSAREINT(codOrg, codProc), getPropVal_CS_CAUSAREINT_TIPO(codOrg, codProc));
        valores.put(getPropVal_CS_FECPRIMERPAGO(codOrg, codProc), getPropVal_CS_FECPRIMERPAGO_TIPO(codOrg, codProc));
        valores.put(getPropVal_CS_PRIMERPAGOCB(codOrg, codProc), getPropVal_CS_PRIMERPAGOCB_TIPO(codOrg, codProc));
        valores.put(getPropVal_CS_SUBVCB(codOrg, codProc), getPropVal_CS_SUBVCB_TIPO(codOrg, codProc));
        
        return valores;
    }
    
    //Recuperamos todos los c�digos de campos suplementarios
    public static ArrayList<String> getPropVal_CamposSuplementariosEscrib(String codOrg, String codProc) {
        ArrayList<String> codigos = new ArrayList<String>();
        codigos.add(getPropVal_CS_SEGPAGO2(codOrg, codProc));
        codigos.add(getPropVal_CS_IMPREINTEGRO(codOrg, codProc));
        //codigos.add(getPropVal_CS_REINTEGRAR(codOrg, codProc));
        codigos.add(getPropVal_CS_CAUSAREINT(codOrg, codProc));
        
        return codigos;
    }

    /**
     * @return the COD_MOTIVOREINT
     */
    public static String getCOD_MOTIVOREINT() {
        return COD_MOTIVOREINT;
    }

    /**
     * @return the VAL_MOTIVOREINT
     */
    public static String getVAL_MOTIVOREINT() {
        return VAL_MOTIVOREINT;
    }

    /**
     * @return the COD_AREINTEGRAR
     */
    public static String getCOD_AREINTEGRAR() {
        return COD_AREINTEGRAR;
    }

    public class ROLES {
        public static final int INTERESADO = 1;
    }

    public static String getNOMBRE_TABLA_SOCIOS() {
        return NOMBRE_TABLA_SOCIOS;
    }

    public static String getNOMBRE_SEQ_SOCIOS() {
        return NOMBRE_SEQ_SOCIOS;
    }

    private static final String TABLANOTIF = "TABLANOTIF";
    private static final String TABLADOCS = "TABLADOCS";
    private static final String TABLADOCFIR = "TABLADOCFIR";
    private static final String TABLAFET = "TABLAFET";
    private static final String TABLAOCU = "TABLAOCU";
    private static final String COD_TRAMITE_NOTIF = "COD_TRAMITE_NOTIF";
    private static final String COD_TRAMITE_ACUSE = "COD_TRAMITE_ACUSE";
    private static final String ESTADO_FIRMA = "ESTADO_FIRMA";
    
    public static String getPropVal_TABLANOTIF() {
        return getPROPERTIES().getString(TABLANOTIF);
}

    public static String getPropVal_TABLADOCS() {
        return getPROPERTIES().getString(TABLADOCS);
    }

    public static String getPropVal_TABLADOCFIR() {
        return getPROPERTIES().getString(TABLADOCFIR);
    }

    public static String getPropVal_TABLAOCU() {
        return getPROPERTIES().getString(TABLAOCU);
    }
    public static String getPropVal_TABLAFET() {
        return getPROPERTIES().getString(TABLAFET);
    }

    public static String getPropVal_COD_TRAMITE_NOTIF() {
        return getPROPERTIES().getString(COD_TRAMITE_NOTIF);
    }

    public static String getPropVal_COD_TRAMITE_ACUSE() {
        return getPROPERTIES().getString(COD_TRAMITE_ACUSE);
    }

    public static String getPropVal_ESTADO_FIRMA() {
        return getPROPERTIES().getString(ESTADO_FIRMA);
    }
}
