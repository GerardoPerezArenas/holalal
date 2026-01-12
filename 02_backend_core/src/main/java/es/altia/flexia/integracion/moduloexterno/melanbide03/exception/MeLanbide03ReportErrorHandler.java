package es.altia.flexia.integracion.moduloexterno.melanbide03.exception;

import es.altia.flexia.integracion.moduloexterno.melanbide03.util.MeLanbide03ReportHelper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Clase para manejar los errores producidos al generar un report
 * 
 * @author david.caamano
 * @version 16/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-10-2012 * </li>
 * </ol> 
 */
public class MeLanbide03ReportErrorHandler implements ErrorHandler {
    //Logger
    private static final Logger _log = LogManager.getLogger(MeLanbide03ReportHelper.class);
    
    public void fatalError(SAXParseException e) throws SAXException {
        _log.error(new StringBuffer("CustomErrorHandler.fatalError() -  line ").append(e.getLineNumber()).
        append(", uri ").append(e.getSystemId()).append(", message=").append(e.getMessage()), e);
        throw e;
    }//fatalError
     
    public void error(SAXParseException e) throws SAXParseException {
        _log.error(new StringBuffer("CustomErrorHandler.fatalError() -  line ").append(e.getLineNumber()).
        append(", uri ").append(e.getSystemId()).append(", message=").append(e.getMessage()), e);
        throw e;
    }//error
     
    public void warning(SAXParseException e) throws SAXParseException {
        _log.error(new StringBuffer("CustomErrorHandler.fatalError() -  line ").append(e.getLineNumber()).
        append(", uri ").append(e.getSystemId()).append(", message=").append(e.getMessage()), e);
        throw e;
    }//warning
    
}//class
