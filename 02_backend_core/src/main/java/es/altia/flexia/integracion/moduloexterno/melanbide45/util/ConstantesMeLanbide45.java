/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide45.util;

/**
 *
 * @author davidg
 */
public class ConstantesMeLanbide45 {

    //Nombre del procedimiento
    public static final String NOMBRE_PROCEDIMIENTO_MELANBIDE41 = "RGEF";

    //Fichero de propiedades del modulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE45";

    //Barra separadora
    public static final String BARRA_SEPARADORA = "/";
    public static final String SEPARADOR_VALORES_CONF = ",";

    //Separador nombre archivos
    public static final String SEPARADOR_NOMBRE_ARCHIVOS = "-";

    //Nombres de las tablas en el fichero de propiedades.
    public static final String TABLA_RGEF_MODFORMATIVO = "TABLA_RGEF_MODFORMATIVO";
    public static final String TABLA_RGEF_ESPFORMATIVO = "TABLA_RGCF_ESPFORMATIVO";
    public static final String TABLA_RGEF_MATERIALCONSU = "TABLA_RGEF_MATERIALCONSU";

    public static final String TABLA_CENFOR_CENTROS = "TABLA_CENFOR_CENTROS";
    public static final String TABLA_CENFOR_UBICACION = "TABLA_CENFOR_UBICACION";
    public static final String TABLA_CENFOR_SUBSERV = "TABLA_CENFOR_SUBSERV";

    //Nombres de las secuencias en el fichero melanbide de propiedades
    public static final String SEQ_RGEF_MODFOR = "SEQ_RGEF_MODFOR";
    public static final String SEQ_RGEF_ESPFOR = "SEQ_RGEF_ESPFOR";
    public static final String SEQ_RGEF_MATCON = "SEQ_RGEF_MATCON";

    //Constante para la clave del fichero de propiedades que indica el nombre del módulo.
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

    public final class ROLES {

        public static final int INTERESADO = 1;
    }

}
