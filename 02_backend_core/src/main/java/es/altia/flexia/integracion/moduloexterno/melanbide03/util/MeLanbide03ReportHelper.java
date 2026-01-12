package es.altia.flexia.integracion.moduloexterno.melanbide03.util;

import es.altia.flexia.integracion.moduloexterno.melanbide03.exception.MeLanbide03ReportErrorHandler;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.MeLanbide03GenerateReportVO;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.MeLanbide03InteresadoGenerateReportVO;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.MeLanbide03UnidadCompetencialGenerateReportVO;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.reports.Subreport;

import es.altia.util.commons.DateOperations;

import es.altia.util.io.IOOperations;
import java.awt.GraphicsEnvironment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
//import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
//import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



/**
 * Clase helper para la generación de recibos PDF
 * 
 * @author david.caamano
 * @version 11/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 11-10-2012 * </li>
 * </ol> 
 */
public class MeLanbide03ReportHelper {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide03ReportHelper.class);
    
    //Constantes para los nodos del report
    private static String XML_DOCTYPE = "XML_DOCTYPE";
    private static String XML_NODO_PRINCIPAL = "XML_NODO_PRINCIPAL";
    private static String XML_NODO_PADRE_CERTIFICADO = "XML_NODO_PADRE_CERTIFICADO";
    private static String XML_NODO_NOMBRE = "XML_NODO_NOMBRE";
    private static String XML_NODO_FECHA_NACIMIENTO = "XML_NODO_FECHA_NACIMIENTO";
    private static String XML_NODO_FECHA_NACIMIENTO_ALT = "XML_NODO_FECHA_NACIMIENTO_ALT";
    private static String XML_NODO_CODIGO_CU = "XML_NODO_CODIGO_CU";
    private static String XML_NODO_DESCRIPCION_CUC = "XML_NODO_DESCRIPCION_CUC";
    private static String XML_NODO_DESCRIPCION_CUE = "XML_NODO_DESCRIPCION_CUE";
    private static String XML_NODO_CODIGO_CP = "XML_NODO_CODIGO_CP";
    private static String XML_NODO_DESCRIPCION_CPC = "XML_NODO_DESCRIPCION_CPC";
    private static String XML_NODO_DESCRIPCION_CPE = "XML_NODO_DESCRIPCION_CPE";
    private static String XML_NODO_FECHA_RD = "XML_NODO_FECHA_RD";
    private static String XML_NODO_FECHA_RD_ALT = "XML_NODO_FECHA_RD_ALT";
    private static String XML_NODO_NUMERO_RD = "XML_NODO_NUMERO_RD";
    private static String XML_NODO_FECHA_MODIF_RD = "XML_NODO_FECHA_MODIF_RD";
    private static String XML_NODO_FECHA_MODIF_RD_ALT = "XML_NODO_FECHA_MODIF_RD_ALT";
    private static String XML_NODO_FECHA_CREACION = "XML_NODO_FECHA_CREACION";
    private static String XML_NODO_FECHA_CREACION_ALT = "XML_NODO_FECHA_CREACION_ALT";
    private static String XML_NODO_CLAVE_REGISTRAL = "XML_NODO_CLAVE_REGISTRAL";
    private static String XML_NODO_DECRETO_MOD = "XML_NODO_DECRETO_MOD";
    private static String XML_NODO_NIVEL = "XML_NODO_NIVEL";
    private static String XML_NODO_FLAG="XML_NODO_FLAG";
    private static String XML_NODO_OFICINA = "XML_NODO_COD_OFICINA";

    /**
     * Metodo al que le pasamos el id del report, un map con los parametros, un string con el xml de los datos, un string con el xpath y 
     * el tipo de archivo al que queremos exportar el report.
     * 
     * @param reportId
     * @param reportParams
     * @param xml
     * @param xpathRootExpr
     * @param reportFormat
     * @return byte[]
     */
    public static byte[] runReport(String reportId, Map reportParams, String xml, String xpathRootExpr, MeLanbide03ReportFormatEnum reportFormat) {
        if(log.isDebugEnabled()) log.debug("runReport() : BEGIN ");
        byte[] result = null;
        final ByteArrayOutputStream outAux = new ByteArrayOutputStream();
        
        log.debug("MELANBIDE03.RUN_REPORT. FONTES DISPOÑIBLES:"+ Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        log.debug("=================> runReport reportId:: " + reportId);
        log.debug("=================> runReport xml: " + xml);
        log.debug("=================> runReport xpathRootExpr: " + xpathRootExpr);
        log.debug("=================> runReport ConstantesMeLanbide03.RUTA_INFORMES:: " + ConstantesMeLanbide03.RUTA_INFORMES);
        log.debug("=================> runReport ConstantesMeLanbide03.FICHERO_PROPIEDADES:: " + ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        
        final String reportName = ConfigurationParameter.getParameter(ConstantesMeLanbide03.RUTA_INFORMES, 
                ConstantesMeLanbide03.FICHERO_PROPIEDADES) +reportId+".jasper";
        
        log.debug(" ************ MeLanbide03ReportHelper.runReport  reportName: " + reportName);
        if (reportParams==null) reportParams = new HashMap();
        
       
        JasperPrint jasperPrint = null;
        try {
            final InputStream jasperReportAsStream = MeLanbide03ReportHelper.class.getResourceAsStream(reportName);
            //FileInputStream jasperReportAsStream = new FileInputStream(prueba);
            log.debug(" =============> antes de añadir java.awt.head");
            System.setProperty("java.awt.headless", "true"); 
            
            final JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperReportAsStream);
            log.debug(" ************ MeLanbide03ReportHelper.runReport  1");
            JRXmlDataSource ds = null;
            if (xml!=null) {
                final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                factory.setNamespaceAware(false);
                factory.setIgnoringComments(true);
                final DocumentBuilder db = factory.newDocumentBuilder();
                final InputSource inStream = new InputSource(new StringReader(xml));
                db.setErrorHandler(new MeLanbide03ReportErrorHandler());
                final Document doc = db.parse(inStream);
                ds = new JRXmlDataSource(doc, xpathRootExpr);
            }//if
            
            log.debug(" ************ MeLanbide03ReportHelper.runReport  2");
            //Mai
             //if ("true".equals(flagHaiModif)){
               //reportParams.put("ES_RD_MODIFICADO", "TRUE");
             //} else 
               // log.debug("RunReport. Pasamos el parametro a false");
                //reportParams.put("ES_RD_MODIFICADO", "FALSE");
            //Mai
            if (ds!=null)
                jasperPrint = JasperFillManager.fillReport(jasperReport, reportParams, ds);
            else
                jasperPrint = JasperFillManager.fillReport(jasperReport, reportParams);
            
            log.debug(" ************ MeLanbide03ReportHelper.runReport  3");
        } catch (Exception e) {
            log.debug(" =====================> runReport() - Excepción no esperada al intentar ejecutar el report." +  e.getMessage());
            log.error("Error : ", e);        	
            jasperPrint = null;
            
        }//try-catch
        
        if (jasperPrint!=null) {
            log.info(" ************ MeLanbide03ReportHelper.runReport  4 -  No AGREGAMOS FUENTES - Nueva version jasper ");
            try{
                JRAbstractExporter exporter = null;
            	switch (reportFormat) {
                    case PDF:
                        exporter = new JRPdfExporter();
                        /*FontKey keyArial = new FontKey("Arial", false, false);  
                        PdfFont fontArial = new PdfFont("Helvetica","Cp1252",false); 
 
                        FontKey keyArialBold = new FontKey("Arial", true, false);  
                        PdfFont fontArialBold = new PdfFont("Helvetica-Bold","Cp1252",false); 
 
                        FontKey keyDialog = new FontKey("Dialog", false, false);  
                        PdfFont fontDialog = new PdfFont("Helvetica","Cp1252",false);
                       
                        FontKey keyDialogBold = new FontKey("Dialog", false, false);  
                        PdfFont fontDialogBold = new PdfFont("Helvetica-Bold","Cp1252",false);
                        
                                               
                        Map fontMap = new HashMap();
                        fontMap.put(keyArial,fontArial);
                        fontMap.put(keyArialBold,fontArialBold);
                        fontMap.put(keyDialog,fontDialog);
                        fontMap.put(keyDialogBold,fontDialogBold);
                        exporter.setParameter(JRExporterParameter.FONT_MAP,fontMap);
                        */
                       
                        break;
                    case RTF:
                        exporter = new JRRtfExporter();
                        break;
                    case ODT:
                        exporter = new JROdtExporter();
                        break;
                    default:
                        exporter = new JRPdfExporter();
                        /*
                        FontKey keyArial1 = new FontKey("Arial", false, false);  
                        PdfFont fontArial1 = new PdfFont("Helvetica","Cp1252",false); 
 
                        FontKey keyArialBold1 = new FontKey("Arial", true, false);  
                        PdfFont fontArialBold1 = new PdfFont("Helvetica-Bold","Cp1252",false); 
                        
                        FontKey keyDialog1 = new FontKey("Dialog", false, false);  
                        PdfFont fontDialog1 = new PdfFont("Helvetica","Cp1252",false); 
                        
                        FontKey keyDialogBold1 = new FontKey("Dialog", true, false);  
                        PdfFont fontDialogBold1 = new PdfFont("Helvetica-Bold","Cp1252",false);
 
                        Map fontMap1 = new HashMap();
                        fontMap1.put(keyDialog1,fontDialog1);
                        fontMap1.put(keyDialogBold1, fontDialogBold1);
                        fontMap1.put(keyArial1,fontArial1);
                        fontMap1.put(keyArialBold1,fontArialBold1);
                        
                       
                        
                        exporter.setParameter(JRExporterParameter.FONT_MAP,fontMap1);
                        */
                        
                        break;
                }//switch
                
                
                
                
                
                log.debug(" ************ MeLanbide03ReportHelper.runReport 5");
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                log.debug(" ************ MeLanbide03ReportHelper.runReport 6");
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, (outAux));
                log.debug(" ************ MeLanbide03ReportHelper.runReport 7");
                exporter.exportReport();
                log.debug(" ************ MeLanbide03ReportHelper.runReport 8");
                result = (outAux.toByteArray());
                log.debug(" ************ MeLanbide03ReportHelper.runReport 9");
            } catch(Exception e) {
                 log.error("Error : ", e);
                 log.debug(" ====================> runReport() - Excepción no esperada al intentar exportar el report." + e.getMessage());
            } finally {
                IOOperations.closeOutputStreamSilently(outAux);
            }//try-catch
        }//if (jasperPrint!=null) 
        
        if(log.isDebugEnabled()) log.debug(" =======================> runReport() : END ");
        return result;
    }//runReport

    /**
     * Genera el XML del report para el certificado APA
     * 
     * @param reportVO
     * @return String
     */
    public static String generarXML (MeLanbide03GenerateReportVO reportVO){
        if(log.isDebugEnabled()) log.debug("generarXML() : BEGIN ");
        String xml = new String();
        StringBuilder certificados = new StringBuilder();
        
        ArrayList<MeLanbide03InteresadoGenerateReportVO> interesados = new ArrayList<MeLanbide03InteresadoGenerateReportVO>();
        ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO> unidades = new ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO>();
        
        interesados = reportVO.getInteresados();
        unidades = reportVO.getUnidades();
        
        for(MeLanbide03InteresadoGenerateReportVO interesado : interesados){
            for(MeLanbide03UnidadCompetencialGenerateReportVO unidad : unidades){
                certificados.append(getCertificado(interesado, unidad));
            }//for(MeLanbide03UnidadCompetencialGenerateReportVO unidad : unidades)
        }//for(MeLanbide03InteresadoGenerateReportVO interesado : interesados)
        
        xml += getDoctype();
        xml += getNodoPrincipal(certificados.toString());
        if(log.isDebugEnabled()) log.debug("generarXML() : END ");
        return xml;
    }//generarXML
    
    
    private static String getFechaFormatoEuskera(String fecha){
        String salida = "";
        if(fecha!=null && !"".equals(fecha)){            
            
            try{
                Calendar calendar = DateOperations.toCalendar(fecha,"yyyy/MM/dd");
                if(calendar!=null){
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
                    salida = sf.format(calendar.getTime());
                }
                        
            }catch(Exception e){
                log.error(" Error en getFechaFormatoEuskera: " + e.getMessage());
                salida = "";
            }
        }
        
        return salida;
    }
    
    /**
     * Crea un fragmento XML con los datos del certificado y la unidad competencial
     * 
     * @param interesado
     * @param unidad
     * @return String
     */
    private static String getCertificado (MeLanbide03InteresadoGenerateReportVO interesado, MeLanbide03UnidadCompetencialGenerateReportVO unidad){
        if(log.isDebugEnabled()) log.debug("getCertificado() : BEGIN ");
        if(log.isDebugEnabled()) log.debug("getCertificado() : Veamos que es decretoMod."+ unidad.getDecretoMod());
        StringBuffer certificado = new StringBuffer();
            certificado.append(getNodoNombre(interesado.getNombre()));
            certificado.append(getNodoFechaNacimiento(interesado.getFechaNacimiento()));
            certificado.append(getNodoFechaNacimientoAlt(interesado.getFechaNacimientoAlt()));
            certificado.append(getNodoCodigoCU(unidad.getCodigoUC()));
            certificado.append(getNodoDescripcionCUC(unidad.getDenominacion_C()));
            certificado.append(getNodoDescripcionCUE(unidad.getDenominacion_E()));
            certificado.append(getNodoCodigoCP(unidad.getCodigoCP()));
            certificado.append(getNodoDescripcionCPC(unidad.getDescripcionCP_C()));
            certificado.append(getNodoDescripcionCPE(unidad.getDescripcionCP_E()));
            certificado.append(getFechaRD(unidad.getFechaRD_C()));
            certificado.append(getNodoOficina(interesado.getCodOficina()));
            // original
            //certificado.append(getFechaRDAlt(unidad.getFechaRD_E()));
            //String fechaRDAlt = getFechaFormatoEuskera(unidad.getFechaRD_C());
            certificado.append(getFechaRDAlt(unidad.getFechaRD_E()));            
            certificado.append(getNumeroRD(unidad.getNumeroRD()));
            certificado.append(getFechaModifRD(unidad.getModificadoRD_C()));
            log.debug("FechaModifcadoRD: "+ unidad.getModificadoRD_C());
            // original
            //certificado.append(getFechaModifRDAlt(unidad.getModificadoRD_E()));
            certificado.append(getFechaModifRDAlt(unidad.getModificadoRD_E()));
            
            certificado.append(getFechaCreacion(unidad.getFechaCreacion()));
            certificado.append(getFlag(unidad.getModificadoRD_C()));
            certificado.append(getFechaCreacionAlt(unidad.getFechaCreacionAlt()));
            certificado.append(getClaveRegistral(unidad.getClaveRegistral()));
            
            certificado.append(getNivel(unidad.getNivel()));
            certificado.append(getModifRD(unidad.getDecretoMod()));
        if(log.isDebugEnabled()) log.debug("getCertificado() : END ");
        return getNodoPadreCertificado(certificado.toString());
    }//getCertificado
    
    /**
     * Recupera el doctype para el XML de un archivo de propiedades
     * 
     * @return String
     */
    private static String getDoctype(){
        String doctype = ConfigurationParameter.getParameter(XML_DOCTYPE, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        return doctype;
    }//getDoctype
    
    private static String getNodoPrincipal(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_PRINCIPAL, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append(">").append(valor).append("</").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append(">").append(" ").append("</").append(nodo).append(">");
        return xml.toString();
    }//getNodoPrincipal
    
    private static String getNodoPadreCertificado(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_PADRE_CERTIFICADO, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append(">").append(valor).append("</").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append(">").append(" ").append("</").append(nodo).append(">");
        return xml.toString();
    }//getNodoPadreCertificado
    
    private static String getNodoNombre(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_NOMBRE, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
            if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
                xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
            else
                xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoNombre
    
    private static String getNodoFechaNacimiento(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_FECHA_NACIMIENTO, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
            if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor)){
                String fecNac = getFechaCastellano(valor);
                xml.append("<").append(nodo).append("><![CDATA[").append(fecNac).append("]]></").append(nodo).append(">");
            }else
                xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoFechaNacimiento
    
    private static String getNodoFechaNacimientoAlt(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_FECHA_NACIMIENTO_ALT, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
            if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor)){
                String fecNac = getFechaNacEuskera(valor);
                xml.append("<").append(nodo).append("><![CDATA[").append(fecNac).append("]]></").append(nodo).append(">");
            }else
                xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoFechaNacimientoAlt
    
    private static String getNodoCodigoCU(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_CODIGO_CU, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
            if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
                xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
            else
                xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoCodigoCU
    
    private static String getNodoDescripcionCUC(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_DESCRIPCION_CUC, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoDescripcionCUC
    
    private static String getNodoDescripcionCUE(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_DESCRIPCION_CUE, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoDescripcionCUC
    
    private static String getNodoCodigoCP(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_CODIGO_CP, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoCodigoCP
    
    private static String getNodoOficina(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_OFICINA, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoCodigoCP
    
    private static String getNodoDescripcionCPC(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_DESCRIPCION_CPC, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoDescripcionCPC
    
    private static String getNodoDescripcionCPE(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_DESCRIPCION_CPE, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNodoDescripcionCPE
    
    private static String getFechaRD(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_FECHA_RD, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor)){
            String fechaRD = getFechaRdCastellano(valor);
            xml.append("<").append(nodo).append("><![CDATA[").append(fechaRD).append("]]></").append(nodo).append(">");
        }else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getFechaRD
    
    private static String getFechaRDAlt(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_FECHA_RD_ALT, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor)){
            String fechaRD = getFechaRdEuskera(valor);
            xml.append("<").append(nodo).append("><![CDATA[").append(fechaRD).append("]]></").append(nodo).append(">");
        }else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
          
        log.debug("getFechaRDAlt:" + xml.toString()); 
        return xml.toString();
    }//getFechaRDAlt 
    
    private static String getNumeroRD(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_NUMERO_RD, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNumeroRD 
    
    private static String getFechaModifRD(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_FECHA_MODIF_RD, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor)){
            String fecha = getFechaRdCastellano(valor);
            xml.append("<").append(nodo).append("><![CDATA[").append(fecha).append("]]></").append(nodo).append(">");
        }else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        log.debug("getFechaModifRD: "+ xml.toString());
        return xml.toString();
    }//getFechaModifRD 
    
    
      //Este nodo, se crea para cuando no hai realDecretoModificado
      // En ese caso esa linea no puede salir, utilizamos este nodo como un 
      //flag, para la propiedad PrintWhenExpression
      private static String getFlag(String valor){
         
       log.debug("getFlag. Valor recibido como parametro: "+ valor);   
        String resultado="FALSE";  
        if((valor!=null) && (!"".equals(valor.trim()))){
           resultado="TRUE";
        }  
        
        String nodo = ConfigurationParameter.getParameter(XML_NODO_FLAG, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        xml.append("<").append(nodo).append("><![CDATA[").append(resultado).append("]]></").append(nodo).append(">");
        log.debug("MELANBIDE03REPORTHELPER.FLAG: "+ xml.toString());
        return xml.toString();
    }//getFechaModifRD 
    
    
    private static String getFechaModifRDAlt(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_FECHA_MODIF_RD_ALT, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor)){
            String fecha = getFechaRdEuskera(valor);
            xml.append("<").append(nodo).append("><![CDATA[").append(fecha).append("]]></").append(nodo).append(">");
        }else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        
        log. debug("getFechaModifRDAlt: "+ xml.toString());
        return xml.toString();
    }//getFechaModifRDAlt 
    
    private static String getFechaCreacion(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_FECHA_CREACION, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor)){
            String fechaCreacion = getFechaCastellano(valor);
            xml.append("<").append(nodo).append("><![CDATA[").append(fechaCreacion).append("]]></").append(nodo).append(">");
        }else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getFechaCreacion
    
    private static String getFechaCreacionAlt(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_FECHA_CREACION_ALT, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor)){
            String fechaCreacion = getFechaEuskeraCreacion(valor);
            xml.append("<").append(nodo).append("><![CDATA[").append(fechaCreacion).append("]]></").append(nodo).append(">");
        }else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        log. debug("getFechaCreacionAlt: "+ xml.toString());
        return xml.toString();
    }//getFechaCreacionAlt
    
    private static String getClaveRegistral (String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_CLAVE_REGISTRAL, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getClaveRegistral
    
    private static String getNivel(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_NIVEL, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNivel
    
    private static String getModifRD(String valor){
        String nodo = ConfigurationParameter.getParameter(XML_NODO_DECRETO_MOD, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append("><![CDATA[").append(valor).append("]]></").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append("><![CDATA[").append(" ").append("]]></").append(nodo).append(">");
        return xml.toString();
    }//getNivel
    
    private static final Map<Integer, String> MESES_CASTELLANO = Collections.synchronizedMap(new HashMap<Integer, String>());
    static {
        MESES_CASTELLANO.put(1,"enero");
        MESES_CASTELLANO.put(2,"febrero");
        MESES_CASTELLANO.put(3,"marzo");
        MESES_CASTELLANO.put(4,"abril");
        MESES_CASTELLANO.put(5,"mayo");
        MESES_CASTELLANO.put(6,"junio");
        MESES_CASTELLANO.put(7,"julio");
        MESES_CASTELLANO.put(8,"agosto");
        MESES_CASTELLANO.put(9,"septiembre");
        MESES_CASTELLANO.put(10,"octubre");
        MESES_CASTELLANO.put(11,"noviembre");
        MESES_CASTELLANO.put(12,"diciembre");
    }//MONTHS
    
    private static final Map<Integer, String> MESES_EUSKERA = Collections.synchronizedMap(new HashMap<Integer, String>());
    static {
        MESES_EUSKERA.put(1,"urtarrila");
        MESES_EUSKERA.put(2,"otsaila");
        MESES_EUSKERA.put(3,"martxoa");
        MESES_EUSKERA.put(4,"apirila");
        MESES_EUSKERA.put(5,"maiatza");
        MESES_EUSKERA.put(6,"ekaina");
        MESES_EUSKERA.put(7,"uztaila");
        MESES_EUSKERA.put(8,"abuztua");
        MESES_EUSKERA.put(9,"iraila");
        MESES_EUSKERA.put(10,"urria");
        MESES_EUSKERA.put(11,"azaroa");
        MESES_EUSKERA.put(12,"abendua");
    }//MONTHS
    
    
    //URTARRILA, OTSAILA, MARTXOA, APIRILA, MAIATZA, EKAINA, UZTAILA, ABUZTUA, IRAILA, URRIA, AZAROA, ABENDUA
    
    private static String getFechaRdCastellano (String fecha){
        String fechaRD = new String();
        if(fecha != null && !"".equalsIgnoreCase(fecha)){
            String[] propsFecha = fecha.split("/");
                String dia = propsFecha[0];
                String mes = propsFecha[1];
                String anho = propsFecha[2];
                
            fechaRD = dia + " de " + MESES_CASTELLANO.get(Integer.valueOf(mes));
            
            if(anho!=null){
              if( !"".equals(anho.trim())){
                 fechaRD =fechaRD + " de " + anho;
              }
            
            }
       
        }//if(fecha != null && !"".equalsIgnoreCase(fecha))
        log.debug("getFechaRdCastellano: "+ fechaRD);
        return fechaRD;
    }//getFechaRdCastellano
    
    //<AÑO>(e)Ko <MES_EUSK> (a)ren <DÍA> (e)ko <RD>
    //Por ejemplo 1997(e)ko martxoa(a)ren 7(e)ko 332/1997
    private static String getFechaRdEuskera (String fecha){
        String fechaRD = new String();
        if(fecha != null && !"".equalsIgnoreCase(fecha)){
            String[] propsFecha = fecha.split("/");
                String anho = propsFecha[0];    
                String mes = propsFecha[1];
                String dia = propsFecha[2];
                
            //fechaRD = dia + " ko " + MESES_EUSKERA.get(Integer.valueOf(mes));
              fechaRD= anho +" (e)ko " + MESES_EUSKERA.get(Integer.valueOf(mes))+ "ren "+ dia+ " (e)ko ";
        }//if(fecha != null && !"".equalsIgnoreCase(fecha))
        return fechaRD;
    }//getFechaRdEuskera
    
    private static String getFechaCastellano(String fecha){
        String fechaComp = new String();
        if(fecha != null && !"".equalsIgnoreCase(fecha)){
            String[] propsFecha = fecha.split("/");
                String dia = propsFecha[0];
                String mes = propsFecha[1];
                String anho = propsFecha[2];
                
            fechaComp = dia + " de " + MESES_CASTELLANO.get(Integer.valueOf(mes)) + " de " + anho;
        }//if(fecha != null && !"".equalsIgnoreCase(fecha))
        return fechaComp;
    }//getFechaCastellano
    
    

    //La fecha de nacimiento en euskera sale "<AÑO> ko <MES> ko jaiotakoa", hay que ponerla
    //<AÑO>(e)ko <MES_EUSK> (a)ren <DÍA> (e)(a)n jaiotakoa
    //Por ejemplo, 1973(e)ko otsaila (a)ren 12 (e)(a)n jaiotakoa
   
    private static String getFechaNacEuskera(String fecha){
        String fechaComp = new String();
        if(fecha != null && !"".equalsIgnoreCase(fecha)){
            String[] propsFecha = fecha.split("/");
                String anho = propsFecha[0];    
                String mes = propsFecha[1];
                String dia = propsFecha[2];
                
            fechaComp = anho + " (e)ko " + MESES_EUSKERA.get(Integer.valueOf(mes)) + "ren " + dia + " (e)(a)n jaiotakoa"; 
        }//if(fecha != null && !"".equalsIgnoreCase(fecha))
        log.debug("Melanbide03ReportHelper. getFechaNacEuskera: "+ fechaComp);
        return fechaComp;
    }//getFechaEuskera
    
    
    
     private static String getFechaEuskera(String fecha){
        String fechaComp = new String();
        if(fecha != null && !"".equalsIgnoreCase(fecha)){
            String[] propsFecha = fecha.split("/");
                String anho = propsFecha[0];    
                String mes = propsFecha[1];
                String dia = propsFecha[2];
                
            fechaComp = anho + " ko " + MESES_EUSKERA.get(Integer.valueOf(mes)) + " ko " + dia;
        }//if(fecha != null && !"".equalsIgnoreCase(fecha))
        return fechaComp;
    }//getFechaEuskera
    
     
      //Vitoria-Gasteizen, <AÑO>(e)ko <MES_EUSK> (a)ren <DÍA>(e)(a)
      //Por ejemplo Vitoria-Gasteizen, 2012(e)ko otsaila (a)ren 14(e)(a)
       private static String getFechaEuskeraCreacion(String fecha){
        String fechaComp = new String();
        if(fecha != null && !"".equalsIgnoreCase(fecha)){
            String[] propsFecha = fecha.split("/");
                String anho = propsFecha[0];    
                String mes = propsFecha[1];
                String dia = propsFecha[2];
                
            fechaComp = anho + " (e)ko " + MESES_EUSKERA.get(Integer.valueOf(mes)) + "ren " + dia + " (e)(a)n ";
        }//if(fecha != null && !"".equalsIgnoreCase(fecha))
        return fechaComp;
    }//getFechaEuskera
     

    public static Subreport inicializarSubreport(String subreportName, String xmlReport, String xpathRootExpr, Map<String, Object> subreportParams) throws JRException, ParserConfigurationException, SAXException, IOException
    {
        String urlSubreport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.RUTA_INFORMES, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                      + subreportName
                      + ".jasper";
        InputStream jasperReportAsStream = MeLanbide03ReportHelper.class.getResourceAsStream(urlSubreport);
        JasperReport subreportJR = (JasperReport) JRLoader.loadObject(jasperReportAsStream);
        
        JRXmlDataSource ds = null;
        if(xmlReport != null)
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource inStream = new InputSource(new StringReader(xmlReport));
            db.setErrorHandler(new MeLanbide03ReportErrorHandler());
            Document doc = db.parse(inStream);
            ds = new JRXmlDataSource(doc, xpathRootExpr);
        }
        Subreport subreport = new Subreport(subreportJR, ds, subreportParams);
        return subreport;
    }
     
    public static byte[] runMasterReportWithSubreports(String masterReportName, Map<String, Object> reportParams, List<Subreport> subreportList, MeLanbide03ReportFormatEnum reportFormat)
    {
        byte[] result = null;
        String urlMasterReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.RUTA_INFORMES, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                                    + masterReportName
                                    + ".jasper";
        
        if(reportParams == null)
        {
            reportParams = new HashMap();
        }
        
        JasperPrint jasperPrint = null;
        try 
        {
            InputStream jasperReportAsStream = MeLanbide03ReportHelper.class.getResourceAsStream(urlMasterReport);
            System.setProperty("java.awt.headless", "true"); 
            
            JasperReport masterReport = (JasperReport) JRLoader.loadObject(jasperReportAsStream);
            
            if(subreportList == null || subreportList.isEmpty())
            {
                jasperPrint = JasperFillManager.fillReport(masterReport, reportParams, new JREmptyDataSource(1));
            }
            else
            {
                jasperPrint = JasperFillManager.fillReport(masterReport, reportParams, new JRBeanCollectionDataSource(subreportList));
            }
        } 
        catch (Exception e) 
        {
            log.error("Error : ", e);        	
            jasperPrint = null;
            
        }
        
        if (jasperPrint!=null) 
        {
            final ByteArrayOutputStream outAux = new ByteArrayOutputStream();
            try
            {
                JRAbstractExporter exporter = null;
            	switch (reportFormat) 
                {
                    case PDF:
                        exporter = new JRPdfExporter();
                        log.info("Desactivamos fuentes personalizadas en el report - nueva version jasperrepor 6");
                        /*
                        FontKey keyArial = new FontKey("Arial", false, false);  
                        PdfFont fontArial = new PdfFont("Helvetica","Cp1252",false); 
 
                        FontKey keyArialBold = new FontKey("Arial", true, false);  
                        PdfFont fontArialBold = new PdfFont("Helvetica-Bold","Cp1252",false); 
 
                        FontKey keyDialog = new FontKey("Dialog", false, false);  
                        PdfFont fontDialog = new PdfFont("Helvetica","Cp1252",false);
                       
                        FontKey keyDialogBold = new FontKey("Dialog", false, false);  
                        PdfFont fontDialogBold = new PdfFont("Helvetica-Bold","Cp1252",false);
                       
                        
                        Map fontMap = new HashMap();
                        fontMap.put(keyArial,fontArial);
                        fontMap.put(keyArialBold,fontArialBold);
                        fontMap.put(keyDialog,fontDialog);
                        fontMap.put(keyDialogBold,fontDialogBold);
                        exporter.setParameter(JRExporterParameter.FONT_MAP,fontMap);
                        */
                        break;
                    case RTF:
                        exporter = new JRRtfExporter();
                        break;
                    case ODT:
                        exporter = new JROdtExporter();
                        break;
                    default:
                        exporter = new JRPdfExporter();
                        log.info("Desactivamos fuentes personalizadas en el report - nueva version jasperrepor 6");
                        /*
                        FontKey keyArial1 = new FontKey("Arial", false, false);  
                        PdfFont fontArial1 = new PdfFont("Helvetica","Cp1252",false); 
 
                        FontKey keyArialBold1 = new FontKey("Arial", true, false);  
                        PdfFont fontArialBold1 = new PdfFont("Helvetica-Bold","Cp1252",false); 
                        
                        FontKey keyDialog1 = new FontKey("Dialog", false, false);  
                        PdfFont fontDialog1 = new PdfFont("Helvetica","Cp1252",false); 
                        
                        FontKey keyDialogBold1 = new FontKey("Dialog", true, false);  
                        PdfFont fontDialogBold1 = new PdfFont("Helvetica-Bold","Cp1252",false);
 
                        Map fontMap1 = new HashMap();
                        fontMap1.put(keyDialog1,fontDialog1);
                        fontMap1.put(keyDialogBold1, fontDialogBold1);
                        fontMap1.put(keyArial1,fontArial1);
                        fontMap1.put(keyArialBold1,fontArialBold1);
                        
                       
                        
                        exporter.setParameter(JRExporterParameter.FONT_MAP,fontMap1);
                        */
                        
                        break;
                }
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, (outAux));
                exporter.exportReport();
                result = (outAux.toByteArray());
            } 
            catch(Exception e) 
            {
                log.error("Error : ", e);
            } 
            finally 
            {
                IOOperations.closeOutputStreamSilently(outAux);
            }
        }
        return result;
    }
}//MeLanbide03ReportHelper