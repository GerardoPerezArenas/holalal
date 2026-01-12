package es.altia.flexia.integracion.moduloexterno.melanbide65.util;

import java.util.ResourceBundle;

public class ConstantesMeLanbide65 {

    private static final ResourceBundle PROPERTIES = ResourceBundle.getBundle("MELANBIDE65");

    public static final String FICHERO_PROPIEDADES = "MELANBIDE65";

    public static final String TABLA_CODIGOS_DESPLEGABLES = "TABLA_CODIGOS_DESPLEGABLES";
    public static final String TABLA_VALORES_DESPLEGABLES = "TABLA_VALORES_DESPLEGABLES";

    //Barra separadora Doble Idioma Desplegables
    public static final String BARRA_SEPARADORA_IDIOMA_DESPLEGABLES = "BARRA_SEPARADORA_IDIOMA_DESPLEGABLES";
    public static final int CODIGO_IDIOMA_CASTELLANO = 1;
    public static final int CODIGO_IDIOMA_EUSKERA = 4;

    //Nombre del m�dulo en el properties
    private static final String NOMBRE_MODULO = PROPERTIES.getString("NOMBRE_MODULO");

    //Nombre de las tablas y secuencias en BBDD en el properties
    private static final String PREF_SECUENCIA = "SEQ_";
    private static final String SUF_TRABAJADOR = "_TRABAJADOR";
    private static final String SUF_TECNICO = "_TECNICO";
    private static final String SUF_ENCARGADO = "_ENCARGADO";

    private static final String NOMBRE_TABLA_TRABAJADOR = getPROPERTIES().getString(getNOMBRE_MODULO() + getSUF_TRABAJADOR());
    private static final String NOMBRE_TABLA_TECNICO = getPROPERTIES().getString(getNOMBRE_MODULO() + getSUF_TECNICO());
    private static final String NOMBRE_TABLA_ENCARGADO = getPROPERTIES().getString(getNOMBRE_MODULO() + getSUF_ENCARGADO());

    private static final String NOMBRE_SEQ_TRABAJADOR = getPROPERTIES().getString(getPREF_SECUENCIA() + getNOMBRE_MODULO() + getSUF_TRABAJADOR());
    private static final String NOMBRE_SEQ_TECNICO = getPROPERTIES().getString(getPREF_SECUENCIA() + getNOMBRE_MODULO() + getSUF_TECNICO());
    private static final String NOMBRE_SEQ_ENCARGADO = getPROPERTIES().getString(getPREF_SECUENCIA() + getNOMBRE_MODULO() + getSUF_ENCARGADO());

    private static final String NOMBRE_LISTA_CONTRATO = getPROPERTIES().getString("LISTA_CONTRATO");
    private static final String NOMBRE_LISTA_JORNADA = getPROPERTIES().getString("LISTA_JORNADA");
    private static final String NOMBRE_LISTA_SEXO = getPROPERTIES().getString("LISTA_SEXO");
    private static final String NOMBRE_LISTA_CONTRATO_B = getPROPERTIES().getString("LISTA_CONTRATO_B");
    private static final String NOMBRE_LISTA_PENSIONISTA = getPROPERTIES().getString("LISTA_PENSIONISTA");
    /**
     * @return the FICHERO_PROPIEDADES
     */
    public static String getFICHERO_PROPIEDADES() {
        return FICHERO_PROPIEDADES;
    }

    /**
     * @return the NOMBRE_LISTA_CONTRATO
     */
    public static String getNOMBRE_LISTA_CONTRATO() {
        return NOMBRE_LISTA_CONTRATO;
    }

    /**
     * @return the NOMBRE_LISTA_CONTRATO
     */
    public static String getNOMBRE_LISTA_CONTRATO_B() {
        return NOMBRE_LISTA_CONTRATO_B;
    }

    /**
     * @return the NOMBRE_LISTA_JORNADA
     */
    public static String getNOMBRE_LISTA_JORNADA() {
        return NOMBRE_LISTA_JORNADA;
    }

    /**
     * @return the NOMBRE_LISTA_SEXO
     */
    public static String getNOMBRE_LISTA_SEXO() {
        return NOMBRE_LISTA_SEXO;
    }
    
     /**
     * @return the NOMBRE_LISTA_PENSIONISTA
     */
    public static String getNOMBRE_LISTA_PENSIONISTA() {
        return NOMBRE_LISTA_PENSIONISTA;
    }

    /**
     * @return the NOMBRE_TABLA_TRABAJADOR
     */
    public static String getNOMBRE_TABLA_TRABAJADOR() {
        return NOMBRE_TABLA_TRABAJADOR;
    }

    /**
     * @return the NOMBRE_TABLA_TECNICO
     */
    public static String getNOMBRE_TABLA_TECNICO() {
        return NOMBRE_TABLA_TECNICO;
    }

    /**
     * @return the NOMBRE_TABLA_ENCARGADO
     */
    public static String getNOMBRE_TABLA_ENCARGADO() {
        return NOMBRE_TABLA_ENCARGADO;
    }

    /**
     * @return the NOMBRE_SEQ_TRABAJADOR
     */
    public static String getNOMBRE_SEQ_TRABAJADOR() {
        return NOMBRE_SEQ_TRABAJADOR;
    }

    /**
     * @return the NOMBRE_SEQ_TECNICO
     */
    public static String getNOMBRE_SEQ_TECNICO() {
        return NOMBRE_SEQ_TECNICO;
    }

    /**
     * @return the NOMBRE_SEQ_ENCARGADO
     */
    public static String getNOMBRE_SEQ_ENCARGADO() {
        return NOMBRE_SEQ_ENCARGADO;
    }

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
     * @return the PREF_SECUENCIA
     */
    public static String getPREF_SECUENCIA() {
        return PREF_SECUENCIA;
    }

    /**
     * @return the SUF_TRABAJADOR
     */
    public static String getSUF_TRABAJADOR() {
        return SUF_TRABAJADOR;
    }

    /**
     * @return the SUF_TECNICO
     */
    public static String getSUF_TECNICO() {
        return SUF_TECNICO;
    }

    /**
     * @return the SUF_ENCARGADO
     */
    public static String getSUF_ENCARGADO() {
        return SUF_ENCARGADO;
    }

    //Otros
    public static final String NOT_FOUND_IMP_UAAP = "NOT_FOUND_IMP_UAAP";
    public static final String NOT_FOUND_REG_UAAP = "NOT_FOUND_REG_UAAP";
    public static final String ERROR_RECAL_UAAP = "ERROR_RECAL_UAAP";

    public static final String IMPTOTRECAL = "IMPTOTRECAL";
    public static final String IMPTOTCONCESION = "IMPCONINI";
    public static final String E_TNU = "E_TNU";
    public static final String COD_PROCEDIMIENTO_TRAMITADOR = "COD_PROCEDIMIENTO_TRAMITADOR";
    public static final String NOMBRE_PROCEDIMIENTO_TRAMITADOR = "NOMBRE_PROCEDIMIENTO_TRAMITADOR";
    public static final String FINALIDAD_PROCEDIMIENTO_TRAMITADOR = "FINALIDAD_PROCEDIMIENTO_TRAMITADOR";
    public static final String CONSENTIMIENTO_FIRMADO = "CONSENTIMIENTO_FIRMADO";
    public static final String NIF_USUARIO_TRAMITADOR = "NIF_USUARIO_TRAMITADOR";
    public static final String USUARIO_TRAMITADOR = "USUARIO_TRAMITADOR";
}
