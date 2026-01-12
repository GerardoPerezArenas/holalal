/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide41.util;

/**
 *
 * @author davidg
 */
public class ConstantesMeLanbide41 {

    //Nombre del procedimiento
    public static final String NOMBRE_PROCEDIMIENTO_MELANBIDE41 = "RGCF";

    //Fichero de propiedades del modulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE41";

    //Barra separadora
    public static final String BARRA_SEPARADORA = "/";
    public static final String SEPARADOR_VALORES_CONF = ",";
    public static final String SEPARADOR_DOT_COMMA = ";";

    //Separador nombre archivos
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";

    //Nombres de las tablas en el fichero de propiedades.
    public static final String TABLA_RGCF_ESPECIALIDADES = "TABLA_RGCF_ESPECIALIDADES";
    public static final String TABLA_RGCF_ESPECIALIDADES_MOTDENEG = "TABLA_RGCF_ESPECIALIDADES_MOTDENEG";
    public static final String TABLA_RGCF_SERVICIOS = "TABLA_RGCF_SERVICIOS";
    public static final String TABLA_RGCF_DISPONRECURSOS = "TABLA_RGCF_DISPONRECURSOS";
    public static final String TABLA_RGCF_CAPACIDADINSTALA = "TABLA_RGCF_CAPACIDADINSTALA";
    public static final String TABLA_RGCF_DOTACION = "TABLA_RGCF_DOTACION";
    public static final String TABLA_RGCF_MATERIALCONSUMO = "TABLA_RGCF_MATERIALCONSUMO";
    public static final String TABLA_RGCF_IDENTIFICACIONESP = "TABLA_RGCF_IDENTIFICACIONESP";
    public static final String TABLA_CER_CERTIFICADOS = "TABLA_CER_CERTIFICADOS";
    public static final String TABLA_CENFOR_CENTROS = "TABLA_CENFOR_CENTROS";
    public static final String TABLA_CENFOR_UBICACION = "TABLA_CENFOR_UBICACION";
    public static final String TABLA_CENFOR_SUBSERV = "TABLA_CENFOR_SUBSERV";

    //Nombres de las secuencias en el fichero melanbide de propiedades
    public static final String SEQ_RGCF_ESPECIALIDADES = "SEQ_RGCF_ESPECIALIDADES";
    public static final String SEQ_RGCF_SERVICIOS = "SEQ_RGCF_SERVICIOS";
    public static final String SEQ_RGCF_DISPONRECURSOS = "SEQ_RGCF_DISPONRECURSOS";
    public static final String SEQ_RGCF_CAPACIDADINSTALA = "SEQ_RGCF_CAPACIDADINSTALA";
    public static final String SEQ_RGCF_DOTACION = "SEQ_RGCF_DOTACION";
    public static final String SEQ_RGCF_MATERIALCONSUMO = "SEQ_RGCF_MATERIALCONSUMO";
    public static final String SEQ_RGCF_IDENTIFICACIONESP = "SEQ_RGCF_IDENTIFICACIONESP";

    //Constante para la clave del fichero de propiedades que indica el nombre del modulo.
    public static final String NOMBRE_MODULO = "NOMBRE_MODULO";

    //Otros
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String OK = "OK";
    public static final String ERROR = "ERROR";
    public static final String CARACTERES_ESPECIALES = "[a-zA-Z0-9,.ºª_@#!()=¿?¡*-]*";

    //Colores
    //public static final String ROJO = "red";
    //Tipos de datos requeridos validaciones
    public final class TiposRequeridos {

        public static final String NUMERICO_DECIMAL = "expectedType.decimalNumber";
        public static final String NUMERICO = "expectedType.number";
        public static final String FECHA = "expectedType.date";
        public static final String STRING = "expectedType.string";
        public static final String STRING_NIF = "expectedType.string_nif";
        public static final String STRING_CIF = "expectedType.string_cif";
    }

    public final class CODIGOS_SEXO {

        public static final int HOMBRE = 0;
        public static final int MUJER = 1;
    }

    public final class ROLES {

        public static final int INTERESADO = 1;
    }

    // VARIABLE PARA ALTA DE EXPEDIETE FORMACION EN IKESLAN 
    public static String URL_WS_IKASLANF_ALTAEXPFORMACION = "URL_WS_IKASLANF_ALTAEXPFORMACION";
    public static String TABLA_LAN_CENTROS_SERVICIOS = "TABLA_LAN_CENTROS_SERVICIOS";

    public static String CODIGO_PROCEDIMIENTO_RGCF = "CODIGO_PROCEDIMIENTO_RGCF";
    public static String CODIGO_PROCEDIMIENTO_RGCFM = "CODIGO_PROCEDIMIENTO_RGCFM";
    public static String CODIGO_PROCEDIMIENTO_RGEF = "CODIGO_PROCEDIMIENTO_RGEF";
    public static String CODIGO_PROCEDIMIENTO_CEPAP = "CODIGO_PROCEDIMIENTO_CEPAP";

    public static String TIPOIKASLANF_PROCEDIMIENTO_RGCF = "TIPOIKASLANF_PROCEDIMIENTO_RGCF";
    public static String TIPOIKASLANF_PROCEDIMIENTO_RGEF = "TIPOIKASLANF_PROCEDIMIENTO_RGEF";

    public static final String CODIGO_CAMPOSUP_TIPOMODIF_RGCFM = "CODIGO_CAMPOSUP_TIPOMODIF_RGCFM";
    public static final String TIPODATO_CAMPOSUP_TIPOMODIF_RGCFM = "TIPODATO_CAMPOSUP_TIPOMODIF_RGCFM";
    public static final String COD_DESP_TIPOMODIF_LISTA_CODVALORES = "COD_DESP_TIPOMODIF_LISTA_CODVALORES";

    public static final String CODIGO_CAMPOSUP_TIPOACREDITACION_CEPAP = "CODIGO_CAMPOSUP_TIPOACREDITACION_CEPAP";
    public static final String TIPODATO_CAMPOSUP_TIPOACREDITACION_CEPAP = "TIPODATO_CAMPOSUP_TIPOACREDITACION_CEPAP";
    public static final String COD_DESP_TIPOACREDITACION_CEPAP_LISTA_CODVALORES = "COD_DESP_TIPOACREDITACION_CEPAP_LISTA_CODVALORES";

    public static final String CODIGO_CAMPOSUP_CODCENTRO_RGCFM = "CODIGO_CAMPOSUP_CODCENTRO_RGCFM";
    public static final String TIPODATO_CAMPOSUP_CODCENTRO_RGCFM = "TIPODATO_CAMPOSUP_CODCENTRO_RGCFM";
    public static final String CODIGO_CAMPOSUP_CODUBICACION_CENTRO_RGCFM = "CODIGO_CAMPOSUP_CODUBICACION_CENTRO_RGCFM";
    public static final String TIPODATO_CAMPOSUP_CODUBICACION_CENTRO_RGCFM = "TIPODATO_CAMPOSUP_CODUBICACION_CENTRO_RGCFM";

    public static final String CODIGO_CAMPOSUP_CODCENTRO_RGEF = "CODIGO_CAMPOSUP_CODCENTRO_RGEF";
    public static final String TIPODATO_CAMPOSUP_CODCENTRO_RGEF = "TIPODATO_CAMPOSUP_CODCENTRO_RGEF";
    public static final String CODIGO_CAMPOSUP_CODUBICACION_CENTRO_RGEF = "CODIGO_CAMPOSUP_CODUBICACION_CENTRO_RGEF";
    public static final String TIPODATO_CAMPOSUP_CODUBICACION_CENTRO_RGEF = "TIPODATO_CAMPOSUP_CODUBICACION_CENTRO_RGEF";

    public static final String VALORFIJO_CONSULTA_NROCENSO_LAN_UBIC_SERVICIO_LMV = "LAN_UBIC_SERVICIO_LMV";

}
