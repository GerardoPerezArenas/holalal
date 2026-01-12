/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide50.util;

/**
 *
 * @author davidg
 */
public class ConstantesMeLanbide50 {

    //Fichero de propiedades del modulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE50";
    public static final String CODIGO_PROCEDIMIENTO_MELANBIDE50 = "RGCFM";

    //Nombres de las tablas en el fichero de propiedades.
    public static final String TABLA_TERCEROS = "TABLA_TERCEROS";
    public static final String TABLA_TERCEROS_HIST = "TABLA_TERCEROS_HIST";
    public static final String TABLA_REL_TERCERO_DOMICILIO = "TABLA_REL_TERCERO_DOMICILIO";
    public static final String TABLA_DOMICILIO = "TABLA_DOMICILIO";
    public static final String TABLA_T_DOT = "TABLA_T_DOT";
    public static final String TABLA_E_TNU = "TABLA_E_TNU";
    public static final String TABLA_E_TNUT = "TABLA_E_TNUT";
    public static final String TABLA_E_TFE = "TABLA_E_TFE";
    public static final String TABLA_E_TFET = "TABLA_E_TFET";
    public static final String TABLA_E_EXP = "TABLA_E_EXP";
    public static final String TABLA_E_EXR = "TABLA_E_EXR";
    public static final String TABLA_R_RES = "TABLA_R_RES";
    public static final String TABLA_T_VIA = "TABLA_T_VIA";
    public static final String TABLA_E_CRO = "TABLA_E_CRO";
    public static final String TABLA_TRAMITES = "TABLA_TRAMITES";

    //Nombres de los campos suplementarios en el fichero de propiedades
    public static final String CAMPO_SUPL_FECREGSALIDA = "CAMPO_SUPL_FECREGSALIDA";
    public static final String CAMPO_SUPL_NUMREGSALIDA = "CAMPO_SUPL_NUMREGSALIDA";
  

    public static final class TIPOS_DATOS_SUPLEMENTARIOS {

        public static final int NUMERICO = 0;
        public static final int FECHA = 1;
    }

    public static final String BARRA_SEPARADORA = "/";
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String ASUNTO = "ASUNTO";
    public static final String OBSERVACIONES = "OBSERVACIONES";
    public static final String TIPO_DOC = "TIPO_DOC";

    public static final String URL_WS_REGISTRO = "URL_WS_REGISTRO";

    //Nombre del procedimiento
    public static final String NOMBRE_PROCEDIMIENTO_MELANBIDE50 = "RGCFM";

    public static final String SEPARADOR_VALORES_CONF = ",";

    //Separador nombre archivos
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";

    public static final String TIPO_VIA_DEFECTO_PERSONA_FISICA = "CL";

    public static final String TRAM_RESOLUCIONINSP = "TRAM_RESOLUCIONINSP";

    //Nombres de las tablas en el fichero de propiedades.
    public static final String TABLA_RGCFM_ESPECIALIDADES = "TABLA_RGCFM_ESPECIALIDADES";
    public static final String TABLA_RGCFM_ESPECIALIDADES_MOTDENEG = "TABLA_RGCFM_ESPECIALIDADES_MOTDENEG";
    public static final String TABLA_RGCFM_SERVICIOS = "TABLA_RGCFM_SERVICIOS";
    public static final String TABLA_RGCFM_DISPONRECURSOS = "TABLA_RGCFM_DISPONRECURSOS";
    public static final String TABLA_RGCFM_CAPACIDADINSTALA = "TABLA_RGCFM_CAPACIDADINSTALA";
    public static final String TABLA_RGCFM_DOTACION = "TABLA_RGCFM_DOTACION";
    public static final String TABLA_RGCFM_MATERIALCONSUMO = "TABLA_RGCFM_MATERIALCONSUMO";
    public static final String TABLA_RGCFM_IDENTIFICACIONESP = "TABLA_RGCFM_IDENTIFICACIONESP";
    public static final String TABLA_CER_CERTIFICADOS = "TABLA_CER_CERTIFICADOS";
    public static final String TABLA_CENFOR_CENTROS = "TABLA_CENFOR_CENTROS";
    public static final String TABLA_CENFOR_UBICACION = "TABLA_CENFOR_UBICACION";
    public static final String TABLA_CENFOR_SUBSERV = "TABLA_CENFOR_SUBSERV";
    public static final String TABLA_RGCFM_ESPACIO ="TABLA_RGCFM_ESPACIO";

    //Nombres de las secuencias en el fichero melanbide de propiedades
    public static final String SEQ_RGCFM_ESPECIALIDADES = "SEQ_RGCFM_ESPECIALIDADES";
    public static final String SEQ_RGCFM_SERVICIOS = "SEQ_RGCFM_SERVICIOS";
    public static final String SEQ_RGCFM_DISPONRECURSOS = "SEQ_RGCFM_DISPONRECURSOS";
    public static final String SEQ_RGCFM_CAPACIDADINSTALA = "SEQ_RGCFM_CAPACIDADINSTALA";
    public static final String SEQ_RGCFM_DOTACION = "SEQ_RGCFM_DOTACION";
    public static final String SEQ_RGCFM_MATERIALCONSUMO = "SEQ_RGCFM_MATERIALCONSUMO";
    public static final String SEQ_RGCFM_IDENTIFICACIONESP = "SEQ_RGCFM_IDENTIFICACIONESP";
    public static final String SEQ_RGCFM_ESPACIOS = "SEQ_RGCFM_ESPACIOS";

    //Constante para la clave del fichero de propiedades que indica el nombre del módulo.
    public static final String NOMBRE_MODULO = "NOMBRE_MODULO";

    //Otros
    public static final String OK = "OK";
    public static final String ERROR = "ERROR";
    public static final String CARACTERES_ESPECIALES = "[a-zA-Z0-9,.ºª_@#!()=¿?¡*-]*";
    public static final String CERO = "0";
    public static final int LONGITUD_MUNICIPIO = 3;

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

}