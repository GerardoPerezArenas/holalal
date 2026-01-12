package es.altia.flexia.integracion.moduloexterno.melanbide71.util;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class ConstantesMeLanbide71 {
    //Fichero de propiedades del modulo.
    private static final String FICHERO_PROPIEDADES = "MELANBIDE71";
    
    //Caracteres separadores
    private static final String BARRA_SEPARADORA = "/";
    
    //Formatos
    private static final String FORMATO_FECHA = "dd/MM/yyyy";
    private static final String FORMATO_DECIMAL = "#,###,##0.00";
    
    //Codigo del procedimiento
    private static final String COD_PROCEDIMIENTO = "APEA";
    
    // Constantes en el fichero de propiedades para los codigos de campos suplementarios
    private static final String CS_PRIMERPAGO = "PRIMERPAGO";
    private static final String CS_SUBV = "SUBV";
    private static final String CS_EDAD = "CS_EDAD";
    private static final String CS_SEXO = "CS_SEXO";
    private static final ResourceBundle PROPERTIES = ResourceBundle.getBundle("MELANBIDE71");
    //Nombre del módulo en el properties
    private static final String NOMBRE_MODULO = PROPERTIES.getString("NOMBRE_MODULO");

//    private static final String CS_SUBSANAR = "SUBSANAR";
//    private static final String CS_FECSUBSANAR = "FECSUBSANAR";
//    private static final String CS_PRIMERPAGOCB = "PRIMERPAGOCB";
//    private static final String CS_IMPREINTEGRO = "IMPREINTEGRO";
//    private static final String CS_REINTEGRAR = "REINTEGRAR";
//    private static final String CS_SEGPAGO2 = "SEGPAGO2";
//    private static final String CS_FECACUSERECIBO = "FECACUSERECIBO";
//    private static final String CS_FECPRIMERPAGO = "FECPRIMERPAGO";
//    private static final String CS_SUBVCB = "SUBVCB";
//    private static final String CS_CAUSAREINT = "CAUSAREINT";
//    private static final String CS_CANTMIN = "CANTMINORADA";
    /**
     * @return the PROPERTIES
     */
    public static ResourceBundle getPROPERTIES() {
        return PROPERTIES;
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
    
    // Recuperamos el valor de la propiedad codigo de campo suplementario
    public static String getPropVal_CS_PRIMERPAGO(String codOrg, String codProc){
        return getPROPERTIES().getString(codOrg+"/MODULO_INTEGRACION/"+getNOMBRE_MODULO()+"/"+codProc+"/CAMPO_SUP/CODIGO/"+CS_PRIMERPAGO);
    }

    public static String getPropVal_CS_SUBV(String codOrg, String codProc){
        return getPROPERTIES().getString(codOrg+"/MODULO_INTEGRACION/"+getNOMBRE_MODULO()+"/"+codProc+"/CAMPO_SUP/CODIGO/"+CS_SUBV);
    }
    
    public static String getPropVal_CS_EDAD(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_EDAD);
    }

    public static String getPropVal_CS_SEXO(String codOrg, String codProc) {
        return getPROPERTIES().getString(codOrg + "/MODULO_INTEGRACION/" + getNOMBRE_MODULO() + "/" + codProc + "/CAMPO_SUP/CODIGO/" + CS_SEXO);
    }

    //Recuperamos todos los cï¿½digos de campos suplementarios
    public static ArrayList<String> getPropVal_CamposSuplementariosLeer(String codOrg, String codProc){
        ArrayList<String> codigos = new ArrayList<String>();
        codigos.add(getPropVal_CS_PRIMERPAGO(codOrg, codProc));
        codigos.add(getPropVal_CS_SUBV(codOrg, codProc));
        codigos.add(getPropVal_CS_EDAD(codOrg, codProc));
        codigos.add(getPropVal_CS_SEXO(codOrg, codProc));
        return codigos;
    }

    public class ROLES {

        public static final int INTERESADO = 1;
    }
}
