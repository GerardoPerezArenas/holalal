package es.altia.flexia.integracion.moduloexterno.lanbide01.util;
/**
 * @author david.caamano
 * @version 25/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 25/10/2012 * Edición inicial</li>
 * </ol> 
 */
public class MeLanbide01Constantes {
    
    public final static String FICHERO_CONFIGURACION="MELANBIDE01";
    public final static String BARRA="/";
    public final static String MODULO_INTEGRACION="/MODULO_INTEGRACION/";
    public final static String TABLA_DATOS_CALCULO="TABLA_DATOS_CALCULO";
    public final static String TABLA_DATOS_PERIODO="TABLA_DATOS_PERIODO";
    
    public final static String SEPARADOR_LINEA_PERIODO = "@M01@";
    public final static String PANTALLA_EXPEDIENTE = "/PANTALLA_EXPEDIENTE/";
    public final static String PANTALLA_EXPEDIENTE_HISTORIAL_SUBV = "/PANTALLA_EXPEDIENTE_HISTORIAL_SUBV/";
    public final static String PANTALLA_EXPEDIENTE_CAUSANTES_SUBV = "/PANTALLA_EXPEDIENTE_CAUSANTES_SUBV/";
    //public final static String SEPARADOR_REGISTRO = "@#@";
    public final static String SEPARADOR_REGISTRO = "POP";
    public final static String SEPARADOR_ELEMENTO = "#@#";
    public final static String ALMOHADILLA = "#";

    //Campos suplementarios del expediente.
    public final static String CAMPO_INICIO_CONTRATO = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/INICONTRATO";
    public final static String TIPO_CAMPO_INICIO_CONTRATO = "/PANTALLA_EXPEDIENTE/TIPO/INICONTRATO";
    public final static String CAMPO_FIN_CONTRATO = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/FINCONTRATO";
    public final static String TIPO_CAMPO_FIN_CONTRATO = "/PANTALLA_EXPEDIENTE/TIPO/FINCONTRATO";
    public final static String IMPORTE_SUBVENCION = "/PANTALLA_EXPEDIENTE/NOMBRECAMPO/IMPORTE_SUBVENCION";
    public final static String TIPO_IMPORTE_SUBVENCION = "/PANTALLA_EXPEDIENTE/TIPO/IMPORTE_SUBVENCION";
    public final static String REDUCPERSSUST = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/REDUCPERSSUST";
    public final static String TIPO_REDUCPERSSUST = "/PANTALLA_EXPEDIENTE/TIPO/REDUCPERSSUST";
    public final static String JORNPERSSUST = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/JORNPERSSUST";
    public final static String TIPO_JORNPERSSUST = "/PANTALLA_EXPEDIENTE/TIPO/JORNPERSSUST";
    public final static String JORNPERSCONT = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/JORNPERSCONT";
    public final static String TIPO_JORNPERSCONT = "/PANTALLA_EXPEDIENTE/TIPO/JORNPERSCONT";
    public final static String NUMERO_MAXIMO_INTERVALOS = "/NUMERO_MAXIMO_INTERVALOS";
    public final static String NUMERO_MINIMO_DIAS = "/NUMERO_MINIMO_DIAS";
    public final static String MINIMO_REDUCPERSSUST = "/MINIMO_REDUCPERSSUST";
    public final static String ACTIVIDAD_SUBVENCIONADA = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/ACTIVIDAD_SUBVENCIONADA";
    public final static String TIPO_ACTIVIDAD_SUBVENCIONADA = "/PANTALLA_EXPEDIENTE/TIPO/ACTIVIDAD_SUBVENCIONADA";
    public final static String RELACPERSDEPEN = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/RELACPERSDEPEN";
    public final static String TIPO_RELACPERSDEPEN = "/PANTALLA_EXPEDIENTE/TIPO/RELACPERSDEPEN";
    public final static String FECNAPDEPEN = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/FECNAPDEPEN";
    public final static String TIPO_FECNAPDEPEN = "/PANTALLA_EXPEDIENTE/TIPO/FECNAPDEPEN";
    public final static String RESULTESTTEC = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/RESULTESTTEC";
    public final static String TIPO_RESULTESTTEC = "/PANTALLA_EXPEDIENTE/TIPO/RESULTESTTEC";
    public final static String GRADMINUSDEPEN = "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/GRADMINUSDEPEN";

    //Rol persona contratada
    public final static String ROL_PERSONA_CONTRATADA = "/ROL_PERSONA_CONTRATADA";

    //Valor que indica si tiene una persona dependiente
    public final static String VALOR_TIENE_PERSONA_DEPENDIENTE = "/VALOR_TIENE_PERSONA_DEPENDIENTE";

    //Datos de las actividades subvencionadas
    public final static String TIPOS_ACTIVIDAD_SUBVENCIONADA = "/TIPOS_ACTIVIDAD_SUBVENCIONADA";
    public final static String MAX_DIAS_TIPOS_ACTIVIDAD_SUBVENCIONADA = "/MAX_DIAS_TIPOS_ACTIVIDAD_SUBVENCIONADA";
    public final static String PERSONA_CONTRATADA_MUJER = "/PERSONA_CONTRATADA_MUJER";
    public final static String PLUS_POR_HIJO = "/PLUS_POR_HIJO";
    public final static String MAX_ANHOS_DEPENDENCIA_ACTIVIDAD_SUBVENCIONADA = "/MAX_ANHOS_DEPENDENCIA_ACTIVIDAD_SUBVENCIONADA";
    public final static String GRADO_MINUSVALIA_ACTIVIDAD_SUBVENCIONADA = "/GRADO_MINUSVALIA_ACTIVIDAD_SUBVENCIONADA";

    //Campo que indica si el resultado del estudio tecnico es negativo
    public final static String ESTUDIO_TECNICO_NEGATIVO = "/ESTUDIO_TECNICO_NEGATIVO";

    //Constantes para los errores
    public final static String TODO_CORRECTO = "0";
    public final static String ERROR_FECHA_INICIO_POSTERIOR_FIN = "1";
    public final static String ERROR_FECHA_INICIO_NULA = "2";
    public final static String ERROR_FECHA_FIN_NULA = "3";
    public final static String ERROR_CALCULANDO_PORC_SUBVENC = "4";
    public final static String ERROR_CALCULANDO_GASTO = "5";
    public final static String OTRO_ERROR = "10";
    public final static String ERROR_CAMPO_BASE_COTIZACION = "17";
    public final static String ERROR_CAMPO_JORN_PERS_SUST = "18";
    public final static String ERROR_CAMPO_JORN_PERS_CONT = "19";
    public final static String ERROR_CAMPO_BONIFICACION = "20";
    public final static String ERROR_CAMPO_REDUC_PERS_SUST = "22";

    //Constantes para los errores añadiendo y modificando periodos
    public final static String ERROR_NUEVO_PERIODO_FECHA_INICIO_INCORRECTA = "6";
    public final static String ERROR_NUEVO_PERIODO_INICIO_FIN_MES_DISTINTO = "7";
    public final static String ERROR_NUEVO_PERIODO_NO_CONSECUTIVO = "14";

    //Numero maximo de periodos superado
    public final static String ERROR_SUPERA_NUMERO_MAXIMO_PERIODOS = "8";

    //Error recuperando campos suplementarios
    public final static String ERROR_RECUPERANDO_CAMPOS_SUPLEMENTARIOS = "9";

    //Error grabando datos en los campos suplementarios
    public final static String ERROR_GRABANDO_CAMPOS_SUPLEMENTARIOS = "11";

    //Error por intentar eliminar una fila de un periodo intermedio
    public final static String ERROR_BORRAR_FILA_PERIODO_INTERMEDIO = "12";

    //Error por intentar borrar la ultima fila
    public final static String ERROR_BORRAR_ULTIMA_FILA = "13";

    //Error por que el numero total de dias es menor que el numero minimo
    public final static String ERROR_NUM_MINIMO_DIAS = "15";

    //Error por que el campo REDUCPERSSUST es menor que lo indicado en el properties
    public final static String ERROR_MIN_REDUCPERSSUST = "16";

    //Error calculando si hay que mostrar la alarma del numero maximo de dias
    public final static String ERROR_CALCULANDO_ALARMA_MAX_DIAS = "23";

    //Error calculando si hay que mostrar la alarma del numero maximo de años
    public final static String ERROR_CALCULANDO_ALARMA_MAX_ANHOS = "24";

    //Mostrar alarma max dias
    public final static String MOSTRAR_ALARMA_MAX_DIAS = "25";

    //Mostrar alarma mas anhos
    public final static String MOSTRAR_ALARMA_MAX_ANHOS = "26";

    //Error calculando los dias restantes subvencionables para el interesado contratado
    public final static String ERROR_CALCULANDO_DIAS_RESTANTES = "27";

    //Error la fecha de relacion de persona dependiente no puede estar vacia
    public final static String ERROR_FECHA_NACIMIENTO_PERSONA_DEPENDIENTE_NULA = "28";

    //Error la fecha de relacion de persona dependiente no puede estar vacia
    public final static String ERROR_EXPEDIENTE_FUERA_PLAZO = "30";

    //Milisegundos al día
    public final long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
    
    
    
    public final static String CODIGO_CAMPOSUPLEMENTARIO_NUMEROTOTALDIASCONCEDEDIDOS="CODIGO_CAMPOSUPLEMENTARIO_NUMEROTOTALDIASCONCEDEDIDOS";
    public final static String TIPO_CAMPOSUPLEMENTARIO_NUMEROTOTALDIASCONCEDEDIDOS="TIPO_CAMPOSUPLEMENTARIO_NUMEROTOTALDIASCONCEDEDIDOS";
    public final static String TABLA_GUARDA_CAMPO_NUMEROTOTALDIASCONCEDIDOS="TABLA_GUARDA_CAMPO_NUMEROTOTALDIASCONCEDIDOS";
    
    public final static String  COD_SUPENSION_PLAZOS_COVID19="COVID19";
    public final static String  FECHA_INICIO_PERIODO_SUSPENCION_PLAZOS="FECHA_INICIO_PERIODO_SUSPENCION_PLAZOS";
    public final static String  FECHA_FIN_PERIODO_SUSPENCION_PLAZOS="FECHA_FIN_PERIODO_SUSPENCION_PLAZOS";
    
    
    public final static String  CODIGO_CAMPO_SUPLEMENTARIO_FECINICONTRATO_INTERINIDAD="CODIGO_CAMPO_SUPLEMENTARIO_FECINICONTRATO";
    public final static String  CODIGO_CAMPO_SUPLEMENTARIO_FECFINCONTRATO_INTERINIDAD="CODIGO_CAMPO_SUPLEMENTARIO_FECFINCONTRATO";
    public final static String  TIPO_CAMPO_SUPLEMENTARIO_FECINICONTRATO_INTERINIDAD="TIPO_CAMPO_SUPLEMENTARIO_FECINICONTRATO";
    public final static String  TIPO_CAMPO_SUPLEMENTARIO_FECFINCONTRATO_INTERINIDAD="TIPO_CAMPO_SUPLEMENTARIO_FECFINCONTRATO";
    public final static String  CODIGO_CAMPO_SUPLEMENTARIO_PERCONTJLCONTPORC="CODIGO_CAMPO_SUPLEMENTARIO_PERCONTJLCONTPORC";
    public final static String  TIPO_CAMPO_SUPLEMENTARIO_PERCONTJLCONTPORC="TIPO_CAMPO_SUPLEMENTARIO_PERCONTJLCONTPORC";
    public final static String  CONTRATO_INTERINIDAD_NUMERO_MINIMO_DIAS="CONTRATO_INTERINIDAD_NUMERO_MINIMO_DIAS";
    
    public final static String  SUFIJO_MSG_ERROR="_MSG_ERROR";
    public final static String  COD_MSG_ERROR_NO_FECHA_INICIO_PERIODOS="35";
    public final static String  COD_MSG_ERROR_NO_PORC_REDUC_PERS_SUST="36";
    
    public final static String REGLA_MINIMIS_MAXIMO_IMPORTE="REGLA_MINIMIS_MAXIMO_IMPORTE";
    
}//MeLanbide01Constantes
