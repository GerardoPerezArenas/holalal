
package es.altia.flexia.integracion.moduloexterno.melanbide72.util;

public class ConstantesMeLanbide72 
{
    public ConstantesMeLanbide72() {
    }
    
    //Fichero de propiedades del modulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE72";
    
    //Separadores
    
    public static final String BARRA_SEPARADORA = "/";
    public final static String MODULO_INTEGRACION="/MODULO_INTEGRACION/";
    public static final String SEPARADOR_VALORES_CONF = ",";
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";
    
    //Barra separadora Doble Idioma Desplegables
    public static final String BARRA_SEPARADORA_IDIOMA_DESPLEGABLES = "BARRA_SEPARADORA_IDIOMA_DESPLEGABLES";
    public static final int CODIGO_IDIOMA_CASTELLANO = 1;
    public static final int CODIGO_IDIOMA_EUSKERA = 4;
    
    //Nombres de las tablas en el fichero de propiedades.
    public static final String MELANBIDE72_MEDIDAS_ALT_A1="MELANBIDE72_MEDIDAS_ALT_A1";
    public static final String MELANBIDE72_MEDIDAS_ALT_A2="MELANBIDE72_MEDIDAS_ALT_A2";
    public static final String MELANBIDE72_MEDIDAS_ALT_B="MELANBIDE72_MEDIDAS_ALT_B";
    public static final String MELANBIDE72_MEDIDAS_ALT_C="MELANBIDE72_MEDIDAS_ALT_C";
    //public static final String MELANBIDE72_MEDIDAS_ALT="MELANBIDE72_MEDIDAS_ALT";
    public static final String TABLA_VALORES_CAMPOS_SUPLEMENTARIOS_NUMERICOS="TABLA_VALORES_CAMPOS_SUPLEMENTARIOS_NUMERICOS";
    
    //Nombre de Sequencia ID 
    public static final String SEQ_MELANBIDE72_MEDIDAS_ALT_A1="SEQ_MELANBIDE72_MEDIDAS_ALT_A1";
    public static final String SEQ_MELANBIDE72_MEDIDAS_ALT_A2="SEQ_MELANBIDE72_MEDIDAS_ALT_A2";
    public static final String SEQ_MELANBIDE72_MEDIDAS_ALT_B="SEQ_MELANBIDE72_MEDIDAS_ALT_B";
    public static final String SEQ_MELANBIDE72_MEDIDAS_ALT_C="SEQ_MELANBIDE72_MEDIDAS_ALT_C";
    //public static final String SEQ_MELANBIDE72_MEDIDAS_ALT="SEQ_MELANBIDE72_MEDIDAS_ALT";
    
    //Constante para la clave del fichero de propiedades que indica el nombre del módulo.
    public static final String NOMBRE_MODULO = "NOMBRE_MODULO";
    
    //Tablas
     public static final String TABLA_OCURRENCIAS = "TABLA_OCURRENCIAS";
     public static final String TABLA_EXPEDIENTES = "TABLA_EXPEDIENTES";
    
    
    //Campos suplementarios
    public final static String NUMERO_MAXIMO_INTERVALOS = "/NUMERO_MAXIMO_INTERVALOS";
    public final static String NUMERO_MINIMO_DIAS = "/NUMERO_MINIMO_DIAS";
    
    // Otros
    public static final String CARACTERES_ESPECIALES = "CARACTERES_ESPECIALES";
    
    //Tramites
     public static final String ACUSE_RECIBO_NOTIFICACION_RESOLUCION = "ACUSE_RECIBO_NOTIFICACION_RESOLUCION";
     public static final String TRAM_DECEX_RECEP_REG_ENT_3M = "TRAM_DECEX_RECEP_REG_ENT_3M";
    public static final String  TRAM_DECEX_RECEP_REG_ENT_A1 = "TRAM_DECEX_RECEP_REG_ENT_A1";
    public static final String TRAM_DECEX_RECEP_REG_ENT_A2 = "TRAM_DECEX_RECEP_REG_ENT_A2";
    public static final String TRAM_DECEX_RECEP_REG_ENT_A3 = "TRAM_DECEX_RECEP_REG_ENT_A3";

    //JOB
    public static final String CAMPO_SERVIDOR = "SERVIDOR";
    public static final String COD_ORG = "COD_ORG";
    public static final String DOS_ENTORNOS = "DOS_ENTORNOS";
    public static final String COD_PROCEDIMIENTO = "COD_PROCEDIMIENTO";

    public static final String URL_WS_TRAMITACION = "URL_WS_TRAMITACION";
    public static final String COD_UOR = "COD_UOR";      //El mismo para los 3 entornos
    
//Constantes para los errores
    public final static String TODO_CORRECTO = "0";
    
    
 //Constantes para las operaciones
    public static final String OP_EMAIL_UOR = "envioCorreoUnidadTramitadora";
    public static final String OP_EMAIL_USUARIOS_UOR = "envioCorreoUsuariosTramitadora";
    public static final String OP_EMAIL_USUARIO_INICIA = "envioCorreoUsuarioIniciaExpediente";
    public static final String OP_EMAIL_UOR_INICIA_EXPEDIENTE = "envioCorreoUnidadIniciaExpediente";
    public static final String OP_EMAIL_USUARIOS_UOR_INICIA_EXPEDIENTE = "envioCorreoUsuariosUnidadIniciaExpediente";
    
}
