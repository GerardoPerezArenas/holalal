/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide42.util;

import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42XMLAltaExpediente;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mikel
 */
public class MELanbide42XMLParser {
    
    private static final Logger log = LogManager.getLogger(MELanbide42XMLParser.class);   

    public MELanbide42XMLAltaExpediente parseXML (String xml) throws MELanbide42Exception {
        
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new MELanbide42ExpElecSAXHandler();
            
            Reader isr = new InputStreamReader(new ByteArrayInputStream(xml.getBytes("ISO-8859-1")));
            InputSource is = new InputSource();
            is.setCharacterStream(isr);
            saxParser.parse(is, handler);
            
            return ((MELanbide42ExpElecSAXHandler)handler).getXMLAltaExpediente();
            
        } catch (ParserConfigurationException pce) {
            log.error("Error al parsear el XML recibido", pce);
            throw new MELanbide42Exception("Error al parsear el XML recibido", pce);
        } catch (SAXException saxe) {
            log.error("Error al parsear el XML recibido", saxe);
            throw new MELanbide42Exception("Error al parsear el XML recibido", saxe);
        } catch (IOException ioe){
            log.error("Error al leer el XML recibido", ioe);
            throw new MELanbide42Exception("Error al leer el XML recibido", ioe);
        }
    }
    
}
