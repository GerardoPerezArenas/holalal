/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.exception;

import java.util.List;

/**
 *
 * @author santiagoc
 */
public class ListaDatosExcelNoValidosException extends Exception
{
    public ListaDatosExcelNoValidosException()
    {
        super();
    }
    
    public ListaDatosExcelNoValidosException(String msg)
    {
        super(msg);
    }
    
    public ListaDatosExcelNoValidosException(List<ExcelRowMappingException> errores)
    {
       // String msg="";
        super(errores.toString());
    }
}
