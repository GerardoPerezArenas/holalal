/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.exception;

import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6ErrorBean;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;

/**
 *
 * @author davidg
 */
public class ErrorLan6ExcepcionBean extends Lan6Excepcion{
    
    private static final String CLASS_NAME = "ErrorLan6ExcepcionBean";

    public ErrorLan6ExcepcionBean(String codigo, String mensaje) {
        super(codigo, mensaje);
    }
    
    public ErrorLan6ExcepcionBean( Lan6ErrorBean bean, Throwable e) {
        super (ErrorLan6ExcepcionBean.CLASS_NAME,null, bean, e);
    }
    
}
