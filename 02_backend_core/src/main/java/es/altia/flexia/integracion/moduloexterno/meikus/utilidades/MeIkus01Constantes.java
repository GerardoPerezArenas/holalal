package es.altia.flexia.integracion.moduloexterno.meikus.utilidades;

/**
 * @author david.caamano
 * @version 03/12/2012 1.0 Historial de cambios:
 * <ol>
 * <li>david.caamano * 03/12/2012 * Edición inicial</li>
 * </ol>
 */
public class MeIkus01Constantes {
    
    //Fichero de propiedades del mÃ³dulo.
    public static final String FICHERO_PROPIEDADES = "MEIKUS01";

    public static final String LISTADO_CONVOCATORIAS = "/LISTADO_CONVOCATORIAS";
    public static final String FICHERO_CONFIGURACION_MEIKUS = "MEIKUS01";
    public static final String NOMBRE_MODULO = "MEIKUS01";
    public static final String BARRA = "/";
    public static final String TUBO ="TUBO";
    public static final String MODULO_INTEGRACION = "MODULO_INTEGRACION";
    public static final String PANTALLA_EXPEDIENTE = "PANTALLA_EXPEDIENTE";
    public static final String NOMBRE_CAMPO = "NOMBRE_CAMPO";
    public static final String TIPO_CAMPO = "TIPO_CAMPO";
    public static final String PASARELA_PAGOS = "PASARELA_PAGOS";
    public static final String TRAMITE = "TRAMITE";

    //Campos suplementarios del modulo
    public static final String CAMPO_IMPORTE_RESERVA_TOTAL = "IMPORTE_RESERVA_TOTAL";
    //Prefijo que llevaran los campos de los importes
    public static final String PREF_IMPORTE_RESERVA = "IMPORTE_RESERVA_ANIO";
    public static final String CAMPO_IMPORTE_RESERVA_RESTO = "IMPORTE_RESERVA_RESTO";
    //Prefijo que llevaran los campos de los importes
    public static final String PREF_IMPORTE_CONCEDIDO = "IMPORTE_ANIO";
    public static final String CAMPO_IMPORTE_RESTO_ANIOS = "IMPORTE_RESTO_ANIOS";
    public static final String CAMPO_COD_LINEA_AYUDA = "COD_LINEA_AYUDA";
    public static final String CAMPO_ANIO_CONVOCATORIA = "ANIO_CONVOCATORIA";
    public static final String CAMPO_IMPORTE_CONCEDIDO_TOTAL = "IMPORTE_CONCEDIDO";
    public static final String CAMPO_FECHA_RESOLUCION = "FECHA_RESOLUCION";
    public static final String CAMPO_ID_RESERVA = "ID_RESERVA";
    public static final String CAMPO_ID_EXPEDIENTE = "ID_EXP_IKUS";
    public static final String CAMPO_IKUS_TER_ID = "IKUS_TER_ID";
    public static final String CAMPO_EXP_EIKA_D = "EXP_EIKA_D";
    //Prefijo que llevaran los campos ID de los expedientes EIKA D en los pagos
    public static final String PREF_EXP_EIKA_O = "EXP_EIKA_O";
    //Prefijo que llevaran los campos ID de los pagos
    public static final String PREF_ID_PAGO = "ID_PAGO";
    public static final String CAMPO_ENVIO_EIKA = "ENVIO_EIKA";
    // Variable para paremetrizar en properties el codigo de la linea de ayuda y no leer de campos suplementarios, este codigo es por procedimiento
    public static final String CODIGO_LINEA_AYUDA = "CODIGO_LINEA_AYUDA";

    //Tabla donde se guardan los resultados de las operaciones de la pasarela electronica
    public static final String TABLA_RESULTADOS = "TABLA_RESULTADOS";

    //Tabla donde se guardan la logica de los pagos de la pasarela
    public static final String TABLA_PAGOS = "TABLA_PAGOS";

    //Tabla donde se guardan los datos de las convocatorias del procedimiento
    public static final String TABLA_CONVOCATORIAS = "TABLA_CONVOCATORIAS";

    //Mapeo de provincias a territorio historico
    public static final String TERRITORIO_HISTORICO = "TERRITORIO_HISTORICO";

    //Rol del beneficiario del expediente
    public static final String ROL_BENEFICIARIO = "ROL_BENEFICIARIO";

    //Envio a EIKA 
    public static final String ENVIO_EIKA = "ENVIO_EIKA";
    
    public static final String TABLA_TIPO_DATO_NUMERICO = "TABLA_TIPO_DATO_NUMERICO";
    
    public static final String TABLA_TIPO_DATO_TEXTO = "TABLA_TIPO_DATO_TEXTO";

    public static final String TABLA_TIPO_DATO_DESPLEGABLE = "TABLA_TIPO_DATO_DESPLEGABLE";
}//class
