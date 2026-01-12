package es.altia.util.xml;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class ExtractorDatosXMLIntegracion {
    private static final Logger log = LogManager.getLogger(ExtractorDatosXMLIntegracion.class.getName());
    private String nombreDocumento;
    private byte[] contenidoDocumento;
    private String etiquetaPadre;

    public ExtractorDatosXMLIntegracion(String nombreDocumento, byte[] contenidoDocumento) {
        this.nombreDocumento = nombreDocumento;
        this.contenidoDocumento = contenidoDocumento;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public byte[] getContenidoDocumento() {
        return contenidoDocumento;
    }

    public void setContenidoDocumento(byte[] contenidoDocumento) {
        this.contenidoDocumento = contenidoDocumento;
    }

    public String getEtiquetaPadre() {
        return etiquetaPadre;
    }

    public void setEtiquetaPadre(String etiquetaPadre) {
        this.etiquetaPadre = etiquetaPadre;
    }

    /**
     * Lee los datos de campos suplementarios (de expediente y/o trámite) que contiene un fichero XML
     * dentro de un tag específico
     * @return
     */
    public Map<String, List<GeneralValueObject>> leerDatosCamposSuplementarios() {
        if (log.isDebugEnabled()) {
            log.debug("ExtractorDatosXMLIntegracion.leerDatosCamposSuplementarios(): Inicio lectura tags fichero " + nombreDocumento);
        }

        List<GeneralValueObject> listaCamposExpediente = new ArrayList<GeneralValueObject>();
        List<GeneralValueObject> listaCamposTramite = new ArrayList<GeneralValueObject>();

        Map<String, List<GeneralValueObject>> salida = new HashMap<String, List<GeneralValueObject>>();
        try {
            byte[] ficheroBytes = contenidoDocumento;
            String fichero = new String(ficheroBytes, ConstantesDatos.CODIFICACION_ISO_8859_1);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();

            if (nodoRaiz != null) {
                Element nodoPadre = nodoRaiz.getChild(etiquetaPadre);
                if (nodoPadre != null) {
                    List listaNodosHijos = nodoPadre.getChildren();
                    for (int i = 0; i < listaNodosHijos.size(); i++) {
                        Element nodoHijo = (Element) listaNodosHijos.get(i);
                        String codCampo = nodoHijo.getChildTextTrim(ConstantesDatos.TAG_XML_CODCAMPO);
                        String valorCampo = nodoHijo.getChildTextTrim(ConstantesDatos.TAG_XML_VALORCAMPO);
                        GeneralValueObject generalVO = new GeneralValueObject();
                        generalVO.setAtributo(ConstantesDatos.TAG_XML_CODIGO, codCampo);
                        generalVO.setAtributo(ConstantesDatos.TAG_XML_VALOR, valorCampo);

                        String tramite = nodoHijo.getChildTextTrim(ConstantesDatos.TAG_XML_TRAMITE);
                        if (tramite != null && "S".equalsIgnoreCase(tramite)) {
                            listaCamposTramite.add(generalVO);
                        } else {
                            // No existe el tag <Tramite> o no tiene valor
                            listaCamposExpediente.add(generalVO);
                        }
                    }
                }
            }
        } catch (IOException io) {
            listaCamposExpediente = null;
            listaCamposTramite = null;
            log.error("ExtractorDatosXMLIntegracion.leerDatosSuplementarios(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());
        } catch (JDOMException jdo) {
            listaCamposExpediente = null;
            listaCamposTramite = null;
            log.error("ExtractorDatosXMLIntegracion.leerDatosSuplementarios(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());
        } catch (Exception e) {
            listaCamposExpediente = null;
            listaCamposTramite = null;
            log.error("ExtractorDatosXMLIntegracion.leerDatosSuplementarios(): Error al procesar el fichero XML. Exception: " + e.getMessage());
        }

        if (log.isDebugEnabled()) {
            log.debug("ExtractorDatosXMLIntegracion.leerDatosSuplementarios(): Fin lectura tags fichero " + nombreDocumento);
        }

        salida.put("CAMPOS_EXPEDIENTE", listaCamposExpediente);
        salida.put("CAMPOS_TRAMITE", listaCamposTramite);

        return salida;

    }

    /**
     * Lee los datos de  interesados (estos pueden ser también de su domicilio y/o campos suplementarios) que contiene un fichero XML
     * dentro de un tag específico
     * @return
     */
    public List<GeneralValueObject> leerDatosInteresados() {

        if (log.isDebugEnabled()) {
            log.debug("ExtractorDatosXMLIntegracion.leerDatosInteresados(): Inicio lectura tags fichero " + nombreDocumento);
        }

        GeneralValueObject generalVO = new GeneralValueObject();
        Vector lista = new Vector();
        List<GeneralValueObject> listaFinal = new ArrayList<GeneralValueObject>();

        try {
            byte[] ficheroBytes = contenidoDocumento;
            String fichero = new String(ficheroBytes, ConstantesDatos.CODIFICACION_ISO_8859_1);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();

            if (nodoRaiz != null) {
                Element nodoNivel1 = nodoRaiz.getChild(etiquetaPadre);
                if (nodoNivel1 != null) {
                    List listaNivel2 = nodoNivel1.getChildren();
                    for (int j = 0; j < listaNivel2.size(); j++) {
                        Element nodoNivel2 = (Element) listaNivel2.get(j);
                        if (nodoNivel2 != null) {
                            List listaNivel3 = nodoNivel2.getChildren();
                            for (int k = 0; k < listaNivel3.size(); k++) {
                                Element nodoNivel3 = (Element) listaNivel3.get(k);
                                String nombreNodoNivel3 = (String) nodoNivel3.getName();

                                if (nombreNodoNivel3.equals(ConstantesDatos.TAG_XML_DOMICILIO)) {
                                    List listaNivel41 = nodoNivel3.getChildren();
                                    for (int l = 0; l < listaNivel41.size(); l++) {
                                        Element nodoNivel41 = (Element) listaNivel41.get(l);
                                        String nombreNodoNivel41 = (String) nodoNivel41.getName();
                                        String codCampo = nodoNivel41.getValue();
                                        generalVO.setAtributo(nombreNodoNivel41, codCampo);
                                    }
                                } else {
                                    if (nombreNodoNivel3.equals(ConstantesDatos.TAG_XML_DATOSSUPLEMENTARIOS)) {
                                        List listaNivel41 = nodoNivel3.getChildren();
                                        for (int m = 0; m < listaNivel41.size(); m++) {
                                            Element nodoNivel41 = (Element) listaNivel41.get(m);
                                            String codCampo = nodoNivel41.getChildTextTrim(ConstantesDatos.TAG_XML_CODCAMPO);
                                            String valorCampo = nodoNivel41.getChildTextTrim(ConstantesDatos.TAG_XML_VALORCAMPO);
                                            GeneralValueObject generalVO1 = new GeneralValueObject();
                                            generalVO1.setAtributo(ConstantesDatos.TAG_XML_CODIGO, codCampo);
                                            generalVO1.setAtributo(ConstantesDatos.TAG_XML_VALOR, valorCampo);
                                            lista.add(generalVO1);
                                        }
                                    } else {
                                        String codCampo = nodoNivel3.getValue();
                                        generalVO.setAtributo(nombreNodoNivel3, codCampo);
                                    }
                                }
                            }
                            GeneralValueObject generalSalidaVO = new GeneralValueObject();
                            generalSalidaVO.setAtributo("datosInteresado", generalVO);
                            generalSalidaVO.setAtributo("datosSuplementarios", lista);
                            generalVO = new GeneralValueObject();
                            lista = new Vector();
                            listaFinal.add(generalSalidaVO);
                        }
                    }
                }
            }
        } catch (IOException io) {
            listaFinal = null;
            if (log.isErrorEnabled()) {
                log.error("ExtractorDatosXMLIntegracion.leerDatosInteresados(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());
            }
        } catch (JDOMException jdo) {
            listaFinal = null;
            if (log.isErrorEnabled()) {
                log.error("ExtractorDatosXMLIntegracion.leerDatosInteresados(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());
            }
        } catch (Exception e) {
            listaFinal = null;
            if (log.isErrorEnabled()) {
                log.error("ExtractorDatosXMLIntegracion.leerDatosInteresados(): Error al procesar el fichero XML. Exception: " + e.getMessage());
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("ExtractorDatosXMLIntegracion.leerDatosInteresados(): Fin lectura tags fichero " + nombreDocumento);
        }

        return listaFinal;
    }

    /**
     * Lee los todos los datos que contiene un fichero XML
     * dentro de diferentes tags específicos
     * @return
     */
    public Map<String, List<GeneralValueObject>> leerDatosFichero() {
        if (log.isDebugEnabled()) {
            log.debug("ExtractorDatosXMLIntegracion.leerDatosFichero(): Inicio lectura tags fichero " + nombreDocumento);
        }

        Map<String, List<GeneralValueObject>> salida = new HashMap<String, List<GeneralValueObject>>();

        try {
            byte[] ficheroBytes = contenidoDocumento;
            String fichero = new String(ficheroBytes, ConstantesDatos.CODIFICACION_ISO_8859_1);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();

            if (nodoRaiz != null) {
                leerDatosCamposSuplementarios(nodoRaiz, salida);
                leerDatosInteresados(nodoRaiz, salida);
            }
        } catch (IOException io) {
            log.error("ExtractorDatosXMLIntegracion.leerDatosFichero(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());
            io.printStackTrace();
        } catch (JDOMException jdo) {
            log.error("ExtractorDatosXMLIntegracion.leerDatosFichero(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());
            jdo.printStackTrace();
        } catch (Exception e) {
            log.error("ExtractorDatosXMLIntegracion.leerDatosFichero(): Error al procesar el fichero XML. Exception: " + e.getMessage());
            e.printStackTrace();
        }

        if (log.isDebugEnabled()) {
            log.debug("ExtractorDatosXMLIntegracion.leerDatosFichero(): Fin lectura tags fichero " + nombreDocumento);
        }

        return salida;
    }

    /**
     * Lee el valor de un atributo de un tag de un fichero xml
     * @param nomAtributo Atributo a leer
     * @return Valor del atributo
     */
    public String leerValorAtributo(String nomAtributo){

        if(log.isDebugEnabled()){
            log.debug("ExtractorDatosXMLIntegracion.leerValorAtributo(): Inicio lectura tags fichero " + nombreDocumento);
        }

        String atributo = "";

        try{
            byte[] ficheroBytes = contenidoDocumento;
            String fichero = new String(ficheroBytes,ConstantesDatos.CODIFICACION_ISO_8859_1);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();

            if (nodoRaiz != null){
                Element nodoPadre = nodoRaiz.getChild(etiquetaPadre);
                if (nodoPadre != null){
                    Element nodoHijo = nodoPadre.getChild(ConstantesDatos.TAG_XML_CODIGOMODULO);
                    if (nodoHijo != null){
                        atributo = nodoHijo.getAttributeValue(nomAtributo);
                    }
                }
            }
        }catch ( IOException io ) {
            atributo = null;
            log.error("ExtractorDatosXMLIntegracion.leerValorAtributo(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());
        }catch ( JDOMException jdo ) {
            atributo = null;
            log.error("ExtractorDatosXMLIntegracion.leerValorAtributo(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());
        }catch ( Exception e) {
            atributo = null;
            log.error("ExtractorDatosXMLIntegracion.leerValorAtributo(): Error al procesar el fichero XML. Exception: " + e.getMessage());
        }

        if(log.isDebugEnabled()){
            log.debug("ExtractorDatosXMLIntegracion.leerValorAtributo(): Fin lectura tags fichero " + nombreDocumento);
        }

        return atributo;

    }

    /**
     * Lee el valor de un tag de un fichero xml
     * @return Valor del atributo
     */
    public String leerValorNodo(){

        if(log.isDebugEnabled()){
            log.debug("ExtractorDatosXMLIntegracion.leerValorNodo(): Inicio lectura tags fichero " + nombreDocumento);
        }
        String valor = "";

        try{
            byte[] ficheroBytes = contenidoDocumento;
            String fichero = new String(ficheroBytes,ConstantesDatos.CODIFICACION_ISO_8859_1);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();

            if (nodoRaiz != null){
                Element nodoPadre = nodoRaiz.getChild(etiquetaPadre);
                if (nodoPadre != null){
                    valor = nodoPadre.getValue();
                }
            }
        }catch ( IOException io ) {
            log.error("ExtractorDatosXMLIntegracion.leerValorNodo(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());
            valor = null;
        }catch ( JDOMException jdo ) {
            valor = null;
            log.error("ExtractorDatosXMLIntegracion.leerValorNodo(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());
        }catch ( Exception e) {
            valor = null;
            log.error("ExtractorDatosXMLIntegracion.leerValorNodo(): Error al procesar el fichero XML. Exception: " + e.getMessage());
        }

        if(log.isDebugEnabled()){
            log.debug("ExtractorDatosXMLIntegracion.leerValorNodoFicheroXML(): Fin lectura tags fichero " + contenidoDocumento);
        }
        return valor;

    }

    /**
     * Lee los datos de campos suplementarios (de expediente y/o trámite) de un fichero XML
     * @return
     */
    private void leerDatosCamposSuplementarios(Element nodoRaiz, Map<String, List<GeneralValueObject>> datos) throws Exception {
        List<GeneralValueObject> listaCamposExpediente = new ArrayList<GeneralValueObject>();
        List<GeneralValueObject> listaCamposTramite = new ArrayList<GeneralValueObject>();
        try {
            Element nodoPadre = nodoRaiz.getChild(ConstantesDatos.TAG_XML_CAMPOSVARIABLES);
            if (nodoPadre != null) {
                List listaNodosHijos = nodoPadre.getChildren();
                for (int i = 0; i < listaNodosHijos.size(); i++) {
                    Element nodoHijo = (Element) listaNodosHijos.get(i);
                    String codCampo = nodoHijo.getChildTextTrim(ConstantesDatos.TAG_XML_CODCAMPO);
                    String valorCampo = nodoHijo.getChildTextTrim(ConstantesDatos.TAG_XML_VALORCAMPO);
                    GeneralValueObject generalVO = new GeneralValueObject();
                    generalVO.setAtributo(ConstantesDatos.TAG_XML_CODIGO, codCampo);
                    generalVO.setAtributo(ConstantesDatos.TAG_XML_VALOR, valorCampo);

                    String tramite = nodoHijo.getChildTextTrim(ConstantesDatos.TAG_XML_TRAMITE);
                    if (tramite != null && "S".equalsIgnoreCase(tramite)) {
                        listaCamposTramite.add(generalVO);
                    } else {
                        // No existe el tag <Tramite> o no tiene valor
                        listaCamposExpediente.add(generalVO);
                    }
                }
            }
        } catch (Exception e) {
            log.error("ExtractorDatosXMLIntegracion.leerDatosCamposSuplementarios(): Error al procesar el fichero XML. Exception: " + e.getMessage());
            throw e;
        }

        datos.put("CAMPOS_EXPEDIENTE", listaCamposExpediente);
        datos.put("CAMPOS_TRAMITE", listaCamposTramite);

    }

    /**
     * Lee los datos de interesados (estos pueden ser también de su domicilio y/o campos suplementarios) de un fichero XML
     * @return
     */
    private void leerDatosInteresados(Element nodoRaiz, Map<String, List<GeneralValueObject>> datos) throws Exception {

        GeneralValueObject generalVO = new GeneralValueObject();
        Vector lista = new Vector();
        List<GeneralValueObject> listaFinal = new ArrayList<GeneralValueObject>();

        try {
            Element nodoNivel1 = nodoRaiz.getChild(ConstantesDatos.TAG_XML_INTERESADOS);
            if (nodoNivel1 != null) {
                List listaNivel2 = nodoNivel1.getChildren();
                for (int j = 0; j < listaNivel2.size(); j++) {
                    Element nodoNivel2 = (Element) listaNivel2.get(j);
                    if (nodoNivel2 != null) {
                        List listaNivel3 = nodoNivel2.getChildren();
                        for (int k = 0; k < listaNivel3.size(); k++) {
                            Element nodoNivel3 = (Element) listaNivel3.get(k);
                            String nombreNodoNivel3 = (String) nodoNivel3.getName();

                            if (nombreNodoNivel3.equals(ConstantesDatos.TAG_XML_DOMICILIO)) {
                                List listaNivel41 = nodoNivel3.getChildren();
                                for (int l = 0; l < listaNivel41.size(); l++) {
                                    Element nodoNivel41 = (Element) listaNivel41.get(l);
                                    String nombreNodoNivel41 = (String) nodoNivel41.getName();
                                    String codCampo = nodoNivel41.getValue();
                                    generalVO.setAtributo(nombreNodoNivel41, codCampo);
                                }
                            } else {
                                if (nombreNodoNivel3.equals(ConstantesDatos.TAG_XML_DATOSSUPLEMENTARIOS)) {
                                    List listaNivel41 = nodoNivel3.getChildren();
                                    for (int m = 0; m < listaNivel41.size(); m++) {
                                        Element nodoNivel41 = (Element) listaNivel41.get(m);
                                        String codCampo = nodoNivel41.getChildTextTrim(ConstantesDatos.TAG_XML_CODCAMPO);
                                        String valorCampo = nodoNivel41.getChildTextTrim(ConstantesDatos.TAG_XML_VALORCAMPO);
                                        GeneralValueObject generalVO1 = new GeneralValueObject();
                                        generalVO1.setAtributo(ConstantesDatos.TAG_XML_CODIGO, codCampo);
                                        generalVO1.setAtributo(ConstantesDatos.TAG_XML_VALOR, valorCampo);
                                        lista.add(generalVO1);
                                    }
                                } else {
                                    String codCampo = nodoNivel3.getValue();
                                    generalVO.setAtributo(nombreNodoNivel3, codCampo);
                                }
                            }
                        }
                        GeneralValueObject generalSalidaVO = new GeneralValueObject();
                        generalSalidaVO.setAtributo("datosInteresado", generalVO);
                        generalSalidaVO.setAtributo("datosSuplementarios", lista);
                        generalVO = new GeneralValueObject();
                        lista = new Vector();
                        listaFinal.add(generalSalidaVO);
                    }
                }
            }
        } catch (Exception e) {
            log.error("ExtractorDatosXMLIntegracion.leerDatosInteresados(): Error al procesar el fichero XML. Exception: " + e.getMessage());
            throw e;
        }

        datos.put("DATOS_INTERESADOS", listaFinal);
    }
}
