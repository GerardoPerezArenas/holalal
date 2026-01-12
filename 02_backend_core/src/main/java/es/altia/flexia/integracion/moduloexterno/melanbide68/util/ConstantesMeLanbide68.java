/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide68.util;

/**
 *
 * @author santiagoc
 */
public class ConstantesMeLanbide68 
{
    //Fichero de propiedades del módulo.
    public static final String FICHERO_PROPIEDADES = "MELANBIDE68"; 
    
    //Barra separadora
    public static final String BARRA_SEPARADORA = "/";
    public static final String SEPARADOR_VALORES_CONF = ",";
    
    //Nombre del procedimiento
    public static final String NOMBRE_PROCEDIMIENTO = "NOMBRE_PROCEDIMIENTO";
    
    //Tipos de datos requeridos validaciones
    public final class TiposRequeridos
    {
        public static final String NUMERICO = "expectedType.number";
        public static final String STRING = "expectedType.string";
    } 
    
    public static final String CARACTERES_ESPECIALES = "[a-zA-Z0-9,.ºª_@#!()=¿?¡*-]*";

    //Tablas
    public static final String TABLA_PRUEBAS_SGA = "PRUEBAS_SGA";
    public static final String TABLA_REGISTRO_RES = "R_RES";
    
    //Secuencias
    public static final String SEQ_REGISTRO_CARGA_SGA = "SEQ_REGISTRO_CARGA_SGA";
    public static final String SEQ_PROCESO_CARGA_SGA = "SEQ_PROCESO_CARGA_SGA";
    
}
