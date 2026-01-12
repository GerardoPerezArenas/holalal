/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexiaWS.tramitacion.bd.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;



/**
 *
 * @author mjesus.lopez
 */
public class XMLTraductor {

    protected static Logger m_Log = LogManager.getLogger(XMLTraductor.class.getName());
    
    public static Map<String, Map<String, String>> traduccionCSXmlToHashMap (String xmlCampos){

        // Creamos el builder basado en SAX
        SAXBuilder builder = new SAXBuilder();
        Map <String, Map <String, String>> datos = new HashMap<String, Map <String, String>>();
        Map <String, String> datosCamposSuplementarios = new HashMap<String, String>();
        Map <String, String> datosNodoExtra = new HashMap<String, String>();
         // m_Log.debug("\nxmlCampos: "+xmlCampos);
        String nombre ="";
        String texto="";
        try {
            // Construimos el arbol DOM a partir del fichero xml
            Document doc = builder.build(new StringReader(xmlCampos));
            // Obtenemos la etiqueta raíz
            Element raiz = doc.getRootElement();
             m_Log.debug("\nraiz: "+raiz.toString());
            // Recorremos los hijos de la etiqueta raíz
            List<Element> hijosRaiz = raiz.getChildren();
            for(Element hijo: hijosRaiz){
                 texto="";
                 // Obtenemos el atributo tit si lo hubiera
                String tit = hijo.getAttributeValue("tit");
                if(tit!=null){
                   m_Log.debug("\tit: "+tit);
                   texto=tit+"|";
                }
                 // Obtenemos el atributo tit si lo hubiera
                String tip = hijo.getAttributeValue("tip");
                if(tip!=null){
                   m_Log.debug("\tip: "+tip);
                   texto=texto+tip+"|";
                }

                // Obtenemos el nombre y su contenido de tipo texto
                nombre = hijo.getName();
                if(!nombre.equals(ConstantesDatos.TAG_XML_TRAMITE)) { // Nos aseguramos que el campo sea de expediente
                    String valor = hijo.getValue();
                    texto = texto + valor;
                    datosCamposSuplementarios.put(nombre, texto);
                } else { //Si lo hay, el nodo Tramite es el ultimo del xml
                    //Tratar campos de tramite
                    datosNodoExtra = traduccionCSElementToHashMap(hijo);
                }

                //m_Log.debug("\nEtiqueta: "+nombre+". Texto: "+texto);
                
         }
            datos.put("datosCampos", datosCamposSuplementarios);
            datos.put("datosExtra", datosNodoExtra);

        } catch (JDOMException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

     return datos;
    }

    /**
     * Genera un string con estructura de fichero xml a partir de los campos suplementarios (de expediente y de trámite) del fichero FLX_DATOS_INTEGRACION_SOLICITUD
     * @param listaCampos
     * @return
     */
    public static String generarXMLdeCSfromFicheroDatosIntegracion(List<GeneralValueObject> listaCampos) {
        m_Log.debug("generarXMLdeCSfromFicheroDatosIntegracion init");

        return generarXMLdeCSfromFicheroDatosIntegracion(listaCampos, null);
    }

    /**
     * Genera un string con estructura de fichero xml a partir de los campos suplementarios (de expediente y de trámite) del fichero FLX_DATOS_INTEGRACION_SOLICITUD
     * @param listaCampos
     * @param listaCamposExtra
     * @return
     */
    public static String generarXMLdeCSfromFicheroDatosIntegracion(List<GeneralValueObject> listaCampos, List<GeneralValueObject> listaCamposExtra) {
        m_Log.debug("generarXMLdeCSfromFicheroDatosIntegracion init");

        StringBuilder sb = new StringBuilder();

        sb.append("<suplementarios>");
        if(listaCampos != null && !listaCampos.isEmpty()) {
            sb.append(generaXMLdeCampos(listaCampos));
        }
        if(listaCamposExtra != null && !listaCamposExtra.isEmpty()) {
            sb.append(generaXMLdeCamposExtra(listaCamposExtra));
        }
        sb.append("</suplementarios>");

        return sb.toString();
    }

    /**
     * Genera un string con estructura de tags de un fichero xml a partir de los campos suplementarios de expediente del fichero FLX_DATOS_INTEGRACION_SOLICITUD
     * @param listaCampos
     * @return
     */
    private static String generaXMLdeCampos(List<GeneralValueObject> listaCampos) {
        m_Log.debug("generaXMLdeCampos init - tamano listaCampos: " + listaCampos.size());

        StringBuilder sb = new StringBuilder();
        String LESS_THAN = "<";
        String GREATER_THAN = ">";
        String LESS_THAN_CLOSE = "</";

        for (GeneralValueObject campo : listaCampos) {
            String codCampo = (String) campo.getAtributo(ConstantesDatos.TAG_XML_CODIGO);
            String valCampo = (String) campo.getAtributo(ConstantesDatos.TAG_XML_VALOR);

            //En el fichero de datos de integración no vienen campos de tipo fichero
            if(codCampo!=null && !codCampo.isEmpty()) {
                sb.append(LESS_THAN).append(codCampo).append(GREATER_THAN);
                sb.append(valCampo);
                sb.append(LESS_THAN_CLOSE).append(codCampo).append(GREATER_THAN);
            }

        }// for

        return sb.toString();
    }

    /**
     * Genera un string con estructura de tags de un fichero xml a partir de los campos suplementarios de expediente del fichero FLX_DATOS_INTEGRACION_SOLICITUD
     * @param listaCampos
     * @return
     */
    public static String generaXMLdeCamposExtra(List<GeneralValueObject> listaCampos) {
        m_Log.debug("generaXMLdeCamposExtra init - tamano listaCampos: " + listaCampos.size());

        StringBuilder sb = new StringBuilder();
        String LESS_THAN = "<";
        String GREATER_THAN = ">";
        String LESS_THAN_CLOSE = "</";

        sb.append(LESS_THAN).append(ConstantesDatos.TAG_XML_TRAMITE).append(GREATER_THAN);
        for (GeneralValueObject campo : listaCampos) {
            String codCampo = (String) campo.getAtributo(ConstantesDatos.TAG_XML_CODIGO);
            String valCampo = (String) campo.getAtributo(ConstantesDatos.TAG_XML_VALOR);

            //En el fichero de datos de integración no vienen campos de tipo fichero
            if(codCampo!=null && !codCampo.isEmpty()) {
                sb.append(LESS_THAN).append(codCampo).append(GREATER_THAN);
                sb.append(valCampo);
                sb.append(LESS_THAN_CLOSE).append(codCampo).append(GREATER_THAN);
            }

        }// for
        sb.append(LESS_THAN_CLOSE).append(ConstantesDatos.TAG_XML_TRAMITE).append(GREATER_THAN);

        return sb.toString();
    }

    private static Map<String, String> traduccionCSElementToHashMap (Element nodoRaiz){
        Map <String, String> datos = new HashMap<String, String>();
        String nombre ="";
        String texto="";
        try {
            m_Log.debug("\nraiz: "+nodoRaiz.toString());
            // Recorremos los hijos de la etiqueta raíz
            List<Element> nodosHijo = nodoRaiz.getChildren();
            for(Element hijo: nodosHijo){
                texto="";
                // Obtenemos el atributo tit si lo hubiera
                String tit = hijo.getAttributeValue("tit");
                if(tit!=null){
                    m_Log.debug("\tit: "+tit);
                    texto=tit+"|";
                }
                // Obtenemos el atributo tit si lo hubiera
                String tip = hijo.getAttributeValue("tip");
                if(tip!=null){
                    m_Log.debug("\tip: "+tip);
                    texto=texto+tip+"|";
                }

                // Obtenemos el nombre y su contenido de tipo texto
                nombre = hijo.getName();
                String valor = hijo.getValue();
                texto = texto + valor;
                datos.put(nombre, texto);


                //m_Log.debug("\nEtiqueta: "+nombre+". Texto: "+texto);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return datos;
    }

}

