/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide31.exception;

/**
 *
 * @author santiagoc
 */
public class ValidarCuentaBancariaException extends Exception
{
    public ValidarCuentaBancariaException()
    {
        super("No se ha podido validar el número de cuenta");
    }
    
    public ValidarCuentaBancariaException(String msg)
    {
        super(msg);
    }
}
