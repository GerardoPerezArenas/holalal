package es.altia.flexia.integracion.moduloexterno.melanbide03;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.integracionsw.exception.EjecucionSWException;
import es.altia.agora.business.sge.SiguienteTramiteTO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.agora.webservice.tramitacion.servicios.WSException;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.lanbide.impresion.ExpedienteImpresionDAO;
import es.altia.flexia.integracion.lanbide.impresion.ExpedienteImpresionManager;
import es.altia.flexia.integracion.lanbide.impresion.ImpresionExpedientesLanbideValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide03.dao.MeLanbide03CertCentroDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide03.dao.MeLanbide03ReportDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide03.exception.MELANBIDE03Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide03.manager.MeLanbide03Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.MeLanbide03MimeTypeUtil;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.MeLanbide03ReportFormatEnum;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.MeLanbide03ReportHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.*;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.reports.Subreport;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.config.Lan6Config;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 13-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class MELANBIDE03 extends ModuloIntegracionExterno{
    
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE03.class);
    
    //Constantes de la clase
    private final String BARRA                              = "/";
    private final String DOT_COMMA                          = ";";
    private final String DOT                                = ".";
    
    //Pantalla de salida del módulo
    private final String PANTALLA_EXPEDIENTE_SALIDA = "/PANTALLA_EXPEDIENTE/SALIDA";
    private final String PANTALLA_PDF_SALIDA = "/PANTALLA_EXPEDIENTE/SALIDA_PDF";
    private final String MODULO_INTEGRACION = "/MODULO_INTEGRACION/";
    private static MELANBIDE03 instance = null;
    
    public static MELANBIDE03 getInstance(){
        if(instance == null){
            synchronized (MeLanbide03ReportDAO.class){
                if(instance == null){
                    instance = new MELANBIDE03();
                }//if(instance == null)
            }//synchronized (MeLanbide03ReportDAO.class)
        }//if(instance == null) 
        return instance;
    }//getInstance
    
    /**
     * Prepara la pantalla para el alta de datos y la rellena con los datos en caso de que existan ya.
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return String con el forward a la JSP
     */
    public String inicioCEPAP (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        if(log.isDebugEnabled()) log.debug("prepararPantallaSeleccionCentro ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        cargarParametrosBásicosEnRequest(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request);
        String redireccion = null;
        if(numExpediente!=null && !"".equals(numExpediente)){
            String[] datos          = numExpediente.split(BARRA);
            String ejercicio        = datos[0];
            String codProcedimiento = datos[1];
            redireccion = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_SALIDA, this.getNombreModulo());
        }//if(numExpediente!=null && !"".equals(numExpediente))

        //Cargamos los certificados para rellenar el combo.
        ArrayList<CerCertificadoVO> listaCertificados = new ArrayList<CerCertificadoVO>();
        try {
            listaCertificados = meLanbide03Manager.getCertificados(getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (BDException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        }//try-catch
        //Guardamos los certificados recuperados en la request
        request.setAttribute("listaCertificados", listaCertificados);
        
        //Cargamos los centros para rellenar el combo
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaCentros = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        try{
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + BARRA 
                    + ConstantesMeLanbide03.CAMPO_SUPLEMENTARIO_CENTRO, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            if(log.isDebugEnabled()) log.debug("Nombre del desplegable de centro = " + campoDesplegable);
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaCentros = salidaIntegracion.getCampoDesplegable().getValores();
        }catch(Exception ex){
            log.error("Se ha producido un error al recuperar el campo desplegable de la lista de centros", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("Ordenamos la lista de centros por su código.");
        Collections.sort(listaCentros, new Comparator<ValorCampoDesplegableModuloIntegracionVO>(){
            public int compare(ValorCampoDesplegableModuloIntegracionVO o1, ValorCampoDesplegableModuloIntegracionVO o2) {
                return o1.getDescripcion().compareToIgnoreCase(o2.getDescripcion());
            }//compare
        });
        request.setAttribute("listaCentros", listaCentros);
        
        //Cargamos los centrosLanF para rellenar el combo
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaCentrosLanF = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        try{
            listaCentrosLanF = MeLanbide03Manager.getInstance().getListaCentrosLanF(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }catch(Exception ex){
            log.error("Se ha producido un error al recuperar el campo desplegable de la lista de centrosLanF", ex);
        }
//        if(log.isDebugEnabled()) log.debug("Ordenamos la lista de centros por su código.");
//        Collections.sort(listaCentros, new Comparator<ValorCampoDesplegableModuloIntegracionVO>(){
//            public int compare(ValorCampoDesplegableModuloIntegracionVO o1, ValorCampoDesplegableModuloIntegracionVO o2) {
//                return o1.getDescripcion().compareToIgnoreCase(o2.getDescripcion());
//            }//compare
//        });
        request.setAttribute("listaCentrosLanF", listaCentrosLanF);
        
        // Se recupera los valores del campo desplegable "Origen de acreditación" para generar el combo
        try{
            String campoDesplegableOrigenAcreditacion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + ConstantesMeLanbide03.CAMPO_SUPLEMENTARIO_ORIGEN_ACREDITACION, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            SalidaIntegracionVO salidaOrigenAcreditacion = el.getCampoDesplegable(String.valueOf(codOrganizacion),campoDesplegableOrigenAcreditacion);
            ArrayList<ValorCampoDesplegableModuloIntegracionVO> valoresOrigenAcreditacion = salidaOrigenAcreditacion.getCampoDesplegable().getValores();
            Collections.sort(valoresOrigenAcreditacion, new Comparator<ValorCampoDesplegableModuloIntegracionVO>(){
                public int compare(ValorCampoDesplegableModuloIntegracionVO o1, ValorCampoDesplegableModuloIntegracionVO o2) {
                    return o1.getDescripcion().compareToIgnoreCase(o2.getDescripcion());
                }//compare
            });
            request.setAttribute("VALORES_COMBO_ORIGEN_ACREDITACION",valoresOrigenAcreditacion);
            log.debug("****** recuperados: " + valoresOrigenAcreditacion.size());
        }catch(Exception ex){
            ex.printStackTrace();
            log.error("Se ha producido un error al recuperar los valores del campo desplegable " + ex.getMessage());            
        }//try-catch
        
        //Cargamos los valores para el campo suplementario del motivo de no acreditado
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaMotivosNoAcreditado = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        try{
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + BARRA 
                    + ConstantesMeLanbide03.CAMPO_SUPLEMENTARIO_MOTIVO_NO_ACREDITADO, ConstantesMeLanbide03.FICHERO_PROPIEDADES);            
            if(log.isDebugEnabled()) log.debug("Nombre del desplegable del motivo de no acreditado = " + campoDesplegable);
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaMotivosNoAcreditado = salidaIntegracion.getCampoDesplegable().getValores();
        }catch(Exception ex){
            log.error("Se ha producido un error al recuperar el campo desplegable de motivo no acreditado", ex);
        }//try-catch
        request.setAttribute("listaMotivosNoAcreditado", listaMotivosNoAcreditado);
        
        //Cargamos los valores para el campo suplementario del motivo de acreditacion de los modulos formativos
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaMotivosAcreditacionModForm = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        try{
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + BARRA 
                    + ConstantesMeLanbide03.CAMPO_SUPLEMENTARIO_MOTIVO_ACREDITADO_MOD_FORM, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaMotivosAcreditacionModForm = salidaIntegracion.getCampoDesplegable().getValores();
        }catch(Exception ex){
            log.error("Se ha producido un error al recuperar el campo desplegable de motivo de acreditación de un módulo formativo", ex);
        }//try-catch
        request.setAttribute("listaMotivosAcreditacionModForm", listaMotivosAcreditacionModForm);
        
        //Cargamos los valores para el campo del motivo de NO acreditacion de los modulos formativos
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaMotivosNoAcreditacionModForm = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        try{
            String campoDesplegable = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + BARRA 
                    + ConstantesMeLanbide03.CAMPO_SUPLEMENTARIO_MOTIVO_NO_ACREDITADO_MOD_FORM, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listaMotivosNoAcreditacionModForm = salidaIntegracion.getCampoDesplegable().getValores();
        }catch(Exception ex){
            log.error("Se ha producido un error al recuperar el campo desplegable de motivo de NO acreditación de un módulo formativo", ex);
        }//try-catch
        request.setAttribute("listaMotivosNoAcreditacionModForm", listaMotivosNoAcreditacionModForm);
        
        //Comprobamos si existe un certificado seleccionado para el expediente y si es asi lo recuperamos
        String cargarDatos = "N";
        CerCertificadoVO certificado = new CerCertificadoVO();
        ArrayList<CerUnidadeCompetencialVO> unidades = new ArrayList<CerUnidadeCompetencialVO>();
        CerModuloFormativoVO modulo = new CerModuloFormativoVO();
        certificado.setNumExpediente(numExpediente);
        certificado.setCodOrganizacion(codOrganizacion);
        try{
            if(meLanbide03Manager.existeCertificadoExpediente(certificado, getAdaptSQLBD(String.valueOf(codOrganizacion)))){
                certificado = meLanbide03Manager.getCertificadoExpediente(numExpediente, codOrganizacion, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                cargarDatos = "S";
            }//if(meLanbide03Manager.existeCertificadoExpediente(certificado, getAdaptSQLBD(String.valueOf(codOrganizacion))))
        }catch (BDException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        }//try-catch
        
        try{
            if(certificado.getCodCertificado() != null){
                unidades = getUnidadesCompetenciales(numExpediente, certificado, String.valueOf(codOrganizacion));
            }//if(certificado.getCodCertificado() != null)
        }catch (BDException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        }//try-catch     
        
        try{
            if(certificado.getCodCertificado() != null){
                modulo = meLanbide03Manager.getModuloPracticas(numExpediente, codOrganizacion, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }//if(certificado.getCodCertificado() != null)
        }catch (MELANBIDE03Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        }//try-catch

        request.setAttribute("cargarDatos", cargarDatos);
        request.setAttribute("certificado",certificado);
        request.setAttribute("unidades", unidades);
        request.setAttribute("modulo", modulo);
        
        //Leire. Recojo información para mostrar el botón de consulta de expedientes de EMPNL
        boolean mostrar = false;
        try {
            mostrar = meLanbide03Manager.obetenerDatosExpedientesEMPNL(codOrganizacion, numExpediente, getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (MELANBIDE03Exception ex) {
            ex.printStackTrace();
            log.error("******** Se ha producido un error al obtener datos de expediente EMPNL " + ex);
        }
        request.setAttribute("botonExpedientes", mostrar);
        request.setAttribute("esquemaBBDD", String.valueOf(codOrganizacion));
        //MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        
        
        if(log.isDebugEnabled()) log.debug("prepararPantallaSeleccionCentro() : END");
        return redireccion;
    }//prepararPantallaSeleccionCentro
    
    /**
     * Prepara la pantalla para el alta de datos y la rellena con los datos en caso de que existan ya.
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return String con el forward a la JSP
     */
    
    
    public String inicioPendientesAPA(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    
    {
        cargarPantallaPendientesAPA(codOrganizacion, request);
        return "/jsp/extension/melanbide03/listadoExpedientesAPA.jsp";
    }   
    
    public void actualizarTabla (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        if(log.isDebugEnabled()) log.debug("actualizarTabla() : BEGIN");
        if(log.isDebugEnabled()) log.debug("Recuperamos los parametros de la request");
        String codCertificado = (String) request.getParameter("codCertificado");
        CerCertificadoVO certificado = new CerCertificadoVO();
        ArrayList<CerUnidadeCompetencialVO> unidades = new ArrayList<CerUnidadeCompetencialVO>();
        try{
            certificado.setCodCertificado(codCertificado);
            certificado.setNumExpediente(numExpediente);
            if(codCertificado != null){
                unidades = getUnidadesCompetenciales(numExpediente, certificado, String.valueOf(codOrganizacion));
            }//if(certificado.getCodCertificado() != null)
        }catch (BDException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
        }//try-catch
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("70");
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<UNIDADES_COMPETENCIALES>");
                for(CerUnidadeCompetencialVO unidadCompetencial : unidades){
                    xmlSalida.append("<UNIDAD_COMPETENCIAL>");
                        xmlSalida.append("<CODIGO>");
                            xmlSalida.append(unidadCompetencial.getCodUnidad());
                        xmlSalida.append("</CODIGO>");
                        xmlSalida.append("<DESCRIPCION>");
                            xmlSalida.append(unidadCompetencial.getDescUnidadC());
                        xmlSalida.append("</DESCRIPCION>");
                        xmlSalida.append("<BLOQUEO_PANTALLA>");
                            xmlSalida.append(unidadCompetencial.getBloquearEnPantalla());
                        xmlSalida.append("</BLOQUEO_PANTALLA>");
                        xmlSalida.append("<COD_CENTRO>");
                            xmlSalida.append(unidadCompetencial.getCodCentro());
                        xmlSalida.append("</COD_CENTRO>");
                        xmlSalida.append("<DESC_CENTRO>");
                            xmlSalida.append(unidadCompetencial.getDescCentro());
                        xmlSalida.append("</DESC_CENTRO>");
                        xmlSalida.append("<COD_CENTROLANF>");
                            xmlSalida.append(unidadCompetencial.getCodCentroLanF());
                        xmlSalida.append("</COD_CENTROLANF>");
                        xmlSalida.append("<DESC_CENTROLANF>");
                            xmlSalida.append(unidadCompetencial.getDescCentroLanF());
                        xmlSalida.append("</DESC_CENTROLANF>");
                        xmlSalida.append("<COD_ACREDITACION>");
                            xmlSalida.append(unidadCompetencial.getCentroAcreditado());
                        xmlSalida.append("</COD_ACREDITACION>");
                        xmlSalida.append("<ORIGEN_ACREDITACION>");
                            xmlSalida.append(unidadCompetencial.getCodOrigenAcred());
                        xmlSalida.append("</ORIGEN_ACREDITACION>");
                        xmlSalida.append("<CLAVE_REGISTRAL>");
                        
                        if(unidadCompetencial.getClaveRegistral()==null || "".equals(unidadCompetencial.getClaveRegistral()))
                            xmlSalida.append("VACIO");
                        else
                            xmlSalida.append(unidadCompetencial.getClaveRegistral());
                            
                        xmlSalida.append("</CLAVE_REGISTRAL>");
                        
                    xmlSalida.append("</UNIDAD_COMPETENCIAL>");
                }//for(CerUnidadeCompetencialVO unidadCompetencial : certificado.getUnidades())
            xmlSalida.append("</UNIDADES_COMPETENCIALES>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        if(log.isDebugEnabled()) log.debug("actualizarTabla() : END");
    }//actualizarTabla
    
    /**
     * Método que en función del certificado seleccionado en la pantalla del módulo medianta ajax recupera los datos del certificado, familia,
     * unidades..., y devuelve un XML para su posterior proceso en la JSP
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void rellenarPorCertificado (int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response) {
        if(log.isDebugEnabled()) log.debug("rellenarPorCertificado (codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite 
                + " ocurrenciaTramite = " + ocurrenciaTramite + "numExpediente = " + numExpediente + " ) : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        
        if(log.isDebugEnabled()) log.debug("Recuperamos los parametros de la request");
        String codCertificado = (String) request.getParameter("codCertificado");
        if(log.isDebugEnabled()) log.debug("Certificado seleccionado con código = " + codCertificado);
        CerCertificadoVO certificado = null;
        String codIdioma = (String) request.getParameter("idioma");
        String codigoOperacion = "70";
        try{
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos del certificado de la tabla CER_CERTIFICADOS");
            certificado = meLanbide03Manager.getCertificado(codCertificado, getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        }//try-catch
        
        if(!codigoOperacion.equalsIgnoreCase("71")){
            try{
                if(log.isDebugEnabled()) log.debug("Rellenamos los datos del área del certificado de la tabla CER_AREAS");
                certificado.setArea(meLanbide03Manager.getArea(getAdaptSQLBD(String.valueOf(codOrganizacion)), certificado.getCodArea()));
            } catch (BDException ex) {
                log.error("Se ha producido un error recuperando el área de certificado de la BBDD", ex);
                codigoOperacion = "72";
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando el área de certificado de la BBDD", ex);
                codigoOperacion = "72";
            }//try-catch
            
            if(!codigoOperacion.equalsIgnoreCase("72")){        
                try{
                    if(log.isDebugEnabled()) log.debug("Rellenamos los datos de la familia del certificado de la tabla CER_FAMILIAS");
                    CerAreaVO area = certificado.getArea();
                    area.setFamilia(meLanbide03Manager.getFamilia(area.getCodFamilia(), getAdaptSQLBD(String.valueOf(codOrganizacion))));
                    certificado.setArea(area);
                } catch (BDException ex) {
                    log.error("Se ha producido un error recuperando la familia de certificado de la BBDD", ex);
                    codigoOperacion = "73";
                } catch (Exception ex) {
                    log.error("Se ha producido un error recuperando la familia de certificado de la BBDD", ex);
                    codigoOperacion = "73";
                }//try-catch
                
                if(!codigoOperacion.equalsIgnoreCase("73")){
                    try{
                        if(log.isDebugEnabled()) log.debug("Rellenamos los datos de las unidades de competencia de la tabla CER_UNIDADES_COMPETENCIA");
                        ArrayList<CerUnidadeCompetencialVO> unidades = new ArrayList<CerUnidadeCompetencialVO>();
                        ArrayList<CerUnidadeCompetencialVO> unidadesCertificado = new ArrayList<CerUnidadeCompetencialVO>();
                        unidadesCertificado = meLanbide03Manager.getListaUnidadesCompetenciales(codCertificado, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        ArrayList<CerUnidadeCompetencialVO> unidadesYaAcreditadas = new ArrayList<CerUnidadeCompetencialVO>();
                        unidadesYaAcreditadas = getUnidadesAcreditadas(numExpediente, certificado, String.valueOf(codOrganizacion));
                        
                        //Filtramos
                        if(unidadesYaAcreditadas.size() > 0){
                            for(CerUnidadeCompetencialVO unidadCertificado : unidadesCertificado){
                                Boolean certificada = false;
                                String acreditada = "";
                                String codCentro = "";
                                String descCentro = "";
                                String codCentroLanF = "";
                                String descCentroLanF = "";
                                String origenAcreditacion = "";
                                String claveRegistral = "";
                                for(CerUnidadeCompetencialVO unidadAcreditada : unidadesYaAcreditadas){
                                    if(unidadCertificado.getCodUnidad().equalsIgnoreCase(unidadAcreditada.getCodUnidad())){
                                        certificada = true;
                                        acreditada = unidadAcreditada.getCentroAcreditado();
                                        codCentro = unidadAcreditada.getCodCentro();
                                        descCentro = unidadAcreditada.getDescCentro();
                                        codCentroLanF = unidadAcreditada.getCodCentroLanF();
                                        descCentroLanF = unidadAcreditada.getDescCentroLanF();
                                        origenAcreditacion = unidadAcreditada.getCodOrigenAcred();
                                        claveRegistral = unidadAcreditada.getClaveRegistral();
                                        break;
                                    }//if(unidadCertificado.getCodUnidad().equalsIgnoreCase(unidadAcreditada.getCodUnidad()))
                                }//for(CerUnidadeCompetencialVO unidadAcreditada : unidadesYaAcreditadas)
                                if(certificada){
                                    unidadCertificado.setBloquearEnPantalla("S");
                                    unidadCertificado.setCentroAcreditado(acreditada);
                                    unidadCertificado.setCodCentro(codCentro);
                                    unidadCertificado.setDescCentro(descCentro);
                                    unidadCertificado.setCodOrigenAcred(origenAcreditacion);
                                    unidadCertificado.setClaveRegistral(claveRegistral);
                                    unidadCertificado.setCodCentroLanF(codCentroLanF);
                                    unidadCertificado.setDescCentroLanF(descCentroLanF);
                                }else{
                                    unidadCertificado.setBloquearEnPantalla("N");
                                }//if(certificada)
                                unidades.add(unidadCertificado);
                            }//for(CerUnidadeCompetencialVO unidadCertificado : unidadesCertificado)
                            certificado.setUnidades(unidades);
                        }else{
                            for(CerUnidadeCompetencialVO unidadCertificado : unidadesCertificado){
                                unidadCertificado.setBloquearEnPantalla("N");
                                unidades.add(unidadCertificado);
                            }//for(CerUnidadeCompetencialVO unidadCertificado : unidadesCertificado)
                            certificado.setUnidades(unidades);
                        }//if(unidadesYaAcreditadas.size() > 0)
                    } catch (BDException ex) {
                        log.error("Se ha producido un error recuperando las unidades competenciales de certificado de la BBDD", ex);
                        codigoOperacion = "74";
                    } catch (Exception ex) {
                        log.error("Se ha producido un error recuperando las unidades competenciales de certificado de la BBDD", ex);
                        codigoOperacion = "74";
                    }//try-catch
                }//if(!codigoOperacion.equalsIgnoreCase("73"))
                
                if(!codigoOperacion.equalsIgnoreCase("74")){
                    try{
                        if(certificado.getCodCertificado() != null){
                            certificado.setModulosFormativos(meLanbide03Manager.getModulosFormativos(certificado.getCodCertificado(), 
                                    getAdaptSQLBD(String.valueOf(codOrganizacion))));
                        }//if(certificado.getCodCertificado() != null)
                    }catch (BDException ex) {
                        java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
                        codigoOperacion = "75";
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
                        codigoOperacion = "75";
                    }//try-catch
                }//if(!codigoOperacion.equalsIgnoreCase("74"))
            }//if(!codigoOperacion.equalsIgnoreCase("72"))
        }//if(!codigoOperacion.equalsIgnoreCase("71"))
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
            xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        if(codigoOperacion.equalsIgnoreCase("70")){
            xmlSalida.append("<AREA>");
                String area="";
                if((certificado.getArea().getDescAreaC())!=null) area=certificado.getArea().getDescAreaC();
                xmlSalida.append(area);
            xmlSalida.append("</AREA>");
            xmlSalida.append("<TIENE_MODULO>");
                if(certificado.getTieneModulo().equals("1")){
                    xmlSalida.append("Si");
                }else{
                    xmlSalida.append("No");
                }//if(certificado.getTieneModulo() == 1)
            xmlSalida.append("</TIENE_MODULO>");
            if(certificado.getDecreto() != null){
                xmlSalida.append("<DECRETO>");
                    xmlSalida.append(certificado.getDecreto());
                xmlSalida.append("</DECRETO>");
            }//if(certificado.getDecreto() != null)
            xmlSalida.append("<FAMILIA>");               
                String familia="";
                if((certificado.getArea().getFamilia().getDescFamiliaC())!=null) familia=certificado.getArea().getFamilia().getDescFamiliaC();
                xmlSalida.append(familia);
            xmlSalida.append("</FAMILIA>");
            xmlSalida.append("<ESTADO>");
                if(certificado.getEstado().equalsIgnoreCase("0")){
                    xmlSalida.append("Inactivo");
                }else if(certificado.getEstado().equalsIgnoreCase("1")){
                    xmlSalida.append("Activo incompleto");
                }else if(certificado.getEstado().equalsIgnoreCase("2")){
                    xmlSalida.append("Activo");
                }//if
            xmlSalida.append("</ESTADO>");
            xmlSalida.append("<NIVEL>");
                xmlSalida.append(String.valueOf(certificado.getNivel()));
            xmlSalida.append("</NIVEL>");
            if(certificado.getTipoCP() != null){
                xmlSalida.append("<TIPO_CP>");
                    xmlSalida.append(String.valueOf(certificado.getTipoCP()));
                xmlSalida.append("</TIPO_CP>");
            }//if(certificado.getTipoCP() != null)
            if(certificado.getRDModif() != null){
                xmlSalida.append("<RD_MODIF>");
                    xmlSalida.append(certificado.getRDModif());
                xmlSalida.append("</RD_MODIF>");
            }//if(certificado.getRDModif() != null)
            if(certificado.getRDDeroga() != null){
                xmlSalida.append("<RD_DEROGA>");
                    xmlSalida.append(certificado.getRDDeroga());
                xmlSalida.append("</RD_DEROGA>");
            }//if(certificado.getRDDeroga() != null)
            if(certificado.getFechaRD() != null){
                xmlSalida.append("<FECHA_RD>");
                    xmlSalida.append(MeLanbide03I18n.getInstance().getFechaConvertida(Integer.valueOf(codIdioma), certificado.getFechaRD()));
                xmlSalida.append("</FECHA_RD>");
            }//if(certificado.getFechaRD() != null)
            if(certificado.getFechaRDModif() != null){
                xmlSalida.append("<FECHA_RD_MODIF>");
                    xmlSalida.append(MeLanbide03I18n.getInstance().getFechaConvertida(Integer.valueOf(codIdioma), certificado.getFechaRDModif()));
                xmlSalida.append("</FECHA_RD_MODIF>");
            }//if(certificado.getFechaRDModif())
            if(certificado.getFechaRDDeroga() != null){
                xmlSalida.append("<FECHA_RD_DEROGA>");
                    xmlSalida.append(MeLanbide03I18n.getInstance().getFechaConvertida(Integer.valueOf(codIdioma), certificado.getFechaRDDeroga()));
                xmlSalida.append("</FECHA_RD_DEROGA>");
            }//if(certificado.getFechaRDDeroga() != null)
            if(certificado.getUnidades().size() > 0){
                xmlSalida.append("<UNIDADES_COMPETENCIALES>");
                    for(CerUnidadeCompetencialVO unidadCompetencial : certificado.getUnidades()){
                        xmlSalida.append("<UNIDAD_COMPETENCIAL>");
                            xmlSalida.append("<CODIGO>");
                                xmlSalida.append(unidadCompetencial.getCodUnidad());
                            xmlSalida.append("</CODIGO>");
                            xmlSalida.append("<DESCRIPCION>");
                                xmlSalida.append(unidadCompetencial.getDescUnidadC());
                            xmlSalida.append("</DESCRIPCION>");
                            xmlSalida.append("<NIVEL>");
                                if(unidadCompetencial.getNivel() != null){
                                    xmlSalida.append(Integer.valueOf(unidadCompetencial.getNivel()));
                                }else{
                                    xmlSalida.append(" ");
                                }//if(unidadCompetencial.getNivel() != null)
                            xmlSalida.append("</NIVEL>");
                            xmlSalida.append("<BLOQUEO_PANTALLA>");
                                xmlSalida.append(unidadCompetencial.getBloquearEnPantalla());
                            xmlSalida.append("</BLOQUEO_PANTALLA>");
                            xmlSalida.append("<COD_CENTRO>");
                                xmlSalida.append(unidadCompetencial.getCodCentro());
                            xmlSalida.append("</COD_CENTRO>");
                            xmlSalida.append("<DESC_CENTRO>");
                                xmlSalida.append(unidadCompetencial.getDescCentro());
                            xmlSalida.append("</DESC_CENTRO>");
                            xmlSalida.append("<COD_CENTROLANF>");
                                xmlSalida.append(unidadCompetencial.getCodCentroLanF());
                            xmlSalida.append("</COD_CENTROLANF>");
                            xmlSalida.append("<DESC_CENTROLANF>");
                                xmlSalida.append(unidadCompetencial.getDescCentroLanF());
                            xmlSalida.append("</DESC_CENTROLANF>");
                            xmlSalida.append("<ORIGEN_ACREDITACION>");
                                xmlSalida.append(unidadCompetencial.getCodOrigenAcred());
                            xmlSalida.append("</ORIGEN_ACREDITACION>");
                            
                            String claveRegistral = unidadCompetencial.getClaveRegistral();
                            xmlSalida.append("<CLAVE_REGISTRAL>");                                
                                if(claveRegistral!=null && !"null".equalsIgnoreCase(claveRegistral) && !"".equals(claveRegistral))
                                    xmlSalida.append(claveRegistral);
                                else
                                    xmlSalida.append(" ");
                            xmlSalida.append("</CLAVE_REGISTRAL>");                            
                            
                        xmlSalida.append("</UNIDAD_COMPETENCIAL>");
                    }//for(CerUnidadeCompetencialVO unidadCompetencial : certificado.getUnidades())
                xmlSalida.append("</UNIDADES_COMPETENCIALES>");
            }//if(certificado.getUnidades().size() > 0)
            if(certificado.getModulosFormativos().size() > 0){
                xmlSalida.append("<MODULOS_FORMATIVOS>");
                    for(CerModuloFormativoVO modulo : certificado.getModulosFormativos()){
                        xmlSalida.append("<MODULO_FORMATIVO>");
                            xmlSalida.append("<CODMODULO>");
                                xmlSalida.append(modulo.getCodModulo());
                            xmlSalida.append("</CODMODULO>");
                            xmlSalida.append("<DESMODULO_E>");
                                xmlSalida.append(modulo.getDesModuloC());
                            xmlSalida.append("</DESMODULO_E>");
                            xmlSalida.append("<NIVEL>");
                                if(modulo.getNivel() != null){
                                    xmlSalida.append(Integer.valueOf(modulo.getNivel()));
                                }//if(modulo.getNivel() != null)
                            xmlSalida.append("</NIVEL>");
                            xmlSalida.append("<DURACION>");
                                xmlSalida.append(String.valueOf(modulo.getDuracion()));
                            xmlSalida.append("</DURACION>");
                            Date fecPracticas = null;
                            try {
                                fecPracticas = meLanbide03Manager.getModuloPracticas(numExpediente,codOrganizacion,getAdaptSQLBD(String.valueOf(codOrganizacion)).getConnection()).getFecPracticas();
                            }catch (BDException eb){
                                eb.printStackTrace();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            xmlSalida.append("<FEC_PRACTICAS>");
                                xmlSalida.append(MeLanbide03I18n.getInstance().getFechaConvertida(Integer.valueOf(codIdioma), fecPracticas));
                            xmlSalida.append("</FEC_PRACTICAS>");
                        xmlSalida.append("</MODULO_FORMATIVO>");
                    }//for(CerModuloFormativoVO modulo : certificado.getModulosFormativos())
                xmlSalida.append("</MODULOS_FORMATIVOS>");
            }//if(certificado.getModulosFormativos().size() > 0)
        }//if(codigoOperacion.equalsIgnoreCase("70"))
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        if(log.isDebugEnabled()) log.debug("rellenarPorCertificado() : END");
    }//rellenarPorCertificado
    
    public String recogeDatosExp (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        try
        {
            ArrayList<Melanbide03ExpedientesVO> result = meLanbide03Manager.obtenerListaExpedientes(codOrganizacion, numExpediente, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("listadoExpedientes", result);
            
        }catch (MELANBIDE03Exception ex) {
            ex.printStackTrace();
            log.error("******** Se ha producido un error al obtener listado de expediente EMPNL " + ex);
        }
        return "/jsp/extension/melanbide03/listadoExpedientesRel.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    /**
     * Método que mediante una llamada AJAX desde la JSP del módulo guarda los datos del certificado y los centros y acreditaciones de las
     * unidades tramitadoras del certificado.
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     *
    public void insertarCertificadoCentroExpediente (int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response) {
        if(log.isDebugEnabled()) log.debug("insertarCertificadoCentroExpediente( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        if(log.isDebugEnabled()) log.debug("recuperamos el código del certificado de los parametros de la request");
        String codCertificado = (String) request.getParameter("codCertificado");
        if(log.isDebugEnabled()) log.debug("Recuperamos los valores de las unidades competenciales seleccionadas y sus acreditaciones");
        String unidades = (String) request.getParameter("unidadesDocumentales");
        if(log.isDebugEnabled()) log.debug("Recuperamos los valores del módulo formativo");
        String modForm = request.getParameter("modForm");
        if(log.isDebugEnabled()) log.debug("Buscamos el certificado seleccionado");
        CerCertificadoVO certificado = null;
        String codigoOperacion = "70";
        try{
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos del certificado de la tabla CER_CERTIFICADOS");
            certificado = meLanbide03Manager.getCertificado(codCertificado, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos del área del certificado de la tabla CER_AREAS");
            certificado.setArea(meLanbide03Manager.getArea(getAdaptSQLBD(String.valueOf(codOrganizacion)), certificado.getCodArea()));
            
            String[] datos = numExpediente.split(BARRA);
                String año = datos[0];
                String codProc = datos[1];
                String numExp = datos[2];
            
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos de número expediente, código del procedimiento...");
            certificado.setNumExpediente(numExpediente);
            certificado.setCodOrganizacion(codOrganizacion);
            certificado.setCodProcedimiento(codProc);
            
            if(log.isDebugEnabled()) log.debug("Rellenamos las unidades si las tiene");
            if(unidades != null && !unidades.equalsIgnoreCase("")){
                ArrayList<CerUnidadeCompetencialVO> listaUnidades = new ArrayList<CerUnidadeCompetencialVO>();
                if(log.isDebugEnabled()) log.debug("Hacemos split al parametro de las unidades");
                String[] arrayUnidades = unidades.split(ConstantesMeLanbide03.SPLIT_UNIDADES);
                for(int i=0; i<arrayUnidades.length; i++){
                    String[] propiedadesUnidades = arrayUnidades[i].split(ConstantesMeLanbide03.SPLIT_PROPIEDADES_UNIDADES);
                    String codUnidad = propiedadesUnidades[0];
                    String codCentro = propiedadesUnidades[1];
                    String acreditado = propiedadesUnidades [2];
                    String codMotNoAcreditado = propiedadesUnidades[3];
                    String codOrigenAcred = propiedadesUnidades[4];
                    String claveRegistral = null;
                    //if(propiedadesUnidades.length==6 && !"-".equals(claveRegistral) && !"".equals(claveRegistral))
                    claveRegistral = propiedadesUnidades[5];
                    if("-".equals(claveRegistral) || "".equals(claveRegistral))
                        claveRegistral = null;
                    
                    CerUnidadeCompetencialVO unidad = new CerUnidadeCompetencialVO();
                        unidad.setCodCentro(codCentro);
                        unidad.setCentroAcreditado(acreditado);
                        unidad.setCodUnidad(codUnidad);
                        unidad.setCodCertificado(codCertificado);
                        unidad.setCodOrganizacion(String.valueOf(codOrganizacion));
                        unidad.setCodProcedimiento(codProc);
                        unidad.setNumExpediente(numExpediente);
                        if(!codMotNoAcreditado.equalsIgnoreCase(" ")){
                            unidad.setCodMotNoAcreditado(codMotNoAcreditado);
                        }//if(!codMotNoAcreditado.equalsIgnoreCase(" "))
                        if(!codOrigenAcred.equalsIgnoreCase(" ")){
                            unidad.setCodOrigenAcred(codOrigenAcred);
                        }//if(!codOrigenAcred.equalsIgnoreCase(" "))
                        unidad.setClaveRegistral(claveRegistral);
                    listaUnidades.add(unidad);
                }//for(int i=0; i<arrayUnidades.length; i++)
                certificado.setUnidades(listaUnidades);
            }//if(unidades != null && !unidades.equalsIgnoreCase(""))
            
            if(log.isDebugEnabled()) log.debug("Insertamos el registro en la BBDD");
            boolean exito = false;
            try{
                exito = meLanbide03Manager.insertarCertificadoExpediente(certificado, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            } catch (MELANBIDE03Exception ex) {
                log.error("Se ha producido un error insertando los datos del certificado en la BBDD", ex);
                codigoOperacion = "72";
            }//try-catch
            
            if(exito){
                try{
                    if(modForm != null && !modForm.equalsIgnoreCase("")){
                        if(log.isDebugEnabled()) log.debug("Hacemos un split de las propiedades del modulo formativo");
                        Boolean guardarModuloFormativoCompleto = false;
                        String[] propModForm = modForm.split(ConstantesMeLanbide03.SPLIT_PROPIEDADES_MOD_FORM);
                        
                        if(propModForm.length >= 3){
                            if(log.isDebugEnabled()) log.debug("Grabamos el modulo formativo");
                            guardarModuloFormativoCompleto = true;
                        }//if(propModForm.length >= 3)
                        
                        if(guardarModuloFormativoCompleto){
                            if(log.isDebugEnabled()) log.debug("Recuperamos si el modulo esta acreditado y su motivo");
                            String codModForm = propModForm[0];
                            String modAcreditado = propModForm[1];
                            String motivoAcreditado = propModForm[2];
                            if(log.isDebugEnabled()) log.debug("Construimos el objeto para guardar el modulo formativo");
                            CerModuloFormativoVO modulo = new CerModuloFormativoVO();
                                modulo.setCodCertificado(codCertificado);
                                modulo.setCodModulo(codModForm);
                                modulo.setCodMotivoAcreditacion(motivoAcreditado);
                                modulo.setCodOrganizacion(codOrganizacion);
                                modulo.setCodProcedimiento(codProc);
                                modulo.setModuloAcreditado(Integer.valueOf(modAcreditado));
                                modulo.setNumExpediente(numExpediente);
                            if(log.isDebugEnabled()) log.debug("Insertamos el objeto de la BBDD");
                            meLanbide03Manager.insertarModuloPracticas(modulo, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        }else{
                            if(log.isDebugEnabled()) log.debug("Solo recuperamos el codigo del modulo formativo");
                            String codModForm = propModForm[0];
                            if(log.isDebugEnabled()) log.debug("Construimos el objeto para guardar el modulo formativo");
                            CerModuloFormativoVO modulo = new CerModuloFormativoVO();
                                modulo.setCodCertificado(codCertificado);
                                modulo.setCodModulo(codModForm);
                                modulo.setCodMotivoAcreditacion(null);
                                modulo.setCodOrganizacion(codOrganizacion);
                                modulo.setCodProcedimiento(codProc);
                                modulo.setModuloAcreditado(null);
                                modulo.setNumExpediente(numExpediente);
                            if(log.isDebugEnabled()) log.debug("Insertamos el objeto de la BBDD");
                            meLanbide03Manager.insertarModuloPracticas(modulo, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        }//if(guardarModuloFormativo)
                    }//if(modForm != null && !modForm.equalsIgnoreCase(""))
                }catch (MELANBIDE03Exception ex) {
                    log.error("Se ha producido un error insertando los datos del certificado en la BBDD", ex);
                    codigoOperacion = "72";
                }//try-catch
            }//if(exito)
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        }//try-catch
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        if(log.isDebugEnabled()) log.debug("insertarCertificadoCentroExpediente() : END");
    }//insertarCertificadoCentroExpediente
    */
    
    
    
   /**
     * Método que mediante una llamada AJAX desde la JSP del módulo guarda los datos del certificado y los centros y acreditaciones de las
     * unidades tramitadoras del certificado.
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void insertarCertificadoCentroExpediente (int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response) {
        if(log.isDebugEnabled()) log.debug("insertarCertificadoCentroExpediente( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        if(log.isDebugEnabled()) log.debug("recuperamos el código del certificado de los parametros de la request");
        String codCertificado = (String) request.getParameter("codCertificado");
        if(log.isDebugEnabled()) log.debug("Recuperamos los valores de las unidades competenciales seleccionadas y sus acreditaciones");
        String unidades = (String) request.getParameter("unidadesDocumentales");
        if(log.isDebugEnabled()) log.debug("Recuperamos los valores del módulo formativo");
        String modForm = request.getParameter("modForm");
        if(log.isDebugEnabled()) log.debug("Buscamos el certificado seleccionado");
        CerCertificadoVO certificado = null;
        String codigoOperacion = "70";
        String segundoExp = "0";
        AdaptadorSQLBD adapt = null;
        try{
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos del certificado de la tabla CER_CERTIFICADOS");
            certificado = meLanbide03Manager.getCertificado(codCertificado, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos del área del certificado de la tabla CER_AREAS");
            certificado.setArea(meLanbide03Manager.getArea(getAdaptSQLBD(String.valueOf(codOrganizacion)), certificado.getCodArea()));
            segundoExp = meLanbide03Manager.compruebaExpedientes(numExpediente, codCertificado, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            String[] datos = numExpediente.split(BARRA);
                String año = datos[0];
                String codProc = datos[1];
                String numExp = datos[2];
            
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos de número expediente, código del procedimiento...");
            certificado.setNumExpediente(numExpediente);
            certificado.setCodOrganizacion(codOrganizacion);
            certificado.setCodProcedimiento(codProc);
            
            if(log.isDebugEnabled()) log.debug("Rellenamos las unidades si las tiene");
            if(segundoExp.equals("0")){
                if(unidades != null && !unidades.equalsIgnoreCase("")){
                    ArrayList<CerUnidadeCompetencialVO> listaUnidades = new ArrayList<CerUnidadeCompetencialVO>();
                    if(log.isDebugEnabled()) log.debug("Hacemos split al parametro de las unidades");
                    String[] arrayUnidades = unidades.split(ConstantesMeLanbide03.SPLIT_UNIDADES);
                    String codCentroCodCentroLanF = ConfigurationParameter.getParameter(ConstantesMeLanbide03.CODCENTRO_LANF, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                    for(int i=0; i<arrayUnidades.length; i++){
                        String[] propiedadesUnidades = arrayUnidades[i].split(ConstantesMeLanbide03.SPLIT_PROPIEDADES_UNIDADES);
                        String codUnidad = propiedadesUnidades[0];
                        String codCentro = propiedadesUnidades[1];
                        String acreditado = propiedadesUnidades [2];
                        String codMotNoAcreditado = propiedadesUnidades[3];
                        String codOrigenAcred = propiedadesUnidades[4];
                        String codCentroLanF=null;
                        if (!" ".equals(propiedadesUnidades[6])) codCentroLanF = propiedadesUnidades[6];
                        String claveRegistral = null;
                        //if(propiedadesUnidades.length==6 && !"-".equals(claveRegistral) && !"".equals(claveRegistral))
                        claveRegistral = propiedadesUnidades[5];
                        if("-".equals(claveRegistral) || "".equals(claveRegistral))
                            claveRegistral = null;

                        CerUnidadeCompetencialVO unidad = new CerUnidadeCompetencialVO();
                            //Por el momento se puede meter sólo centroLanF pero codCentro es obligatorio así que cogemos el codCentro del centro que coincide con la descripción "LanF"
                            
                            if((codCentro==null || "".equals(codCentro) || codCentroCodCentroLanF.equals(codCentro)) && codCentroLanF!=null && !"".equals(codCentroLanF.trim())){
                                codCentro = codCentroCodCentroLanF;
                                acreditado = String.valueOf(ConstantesMeLanbide03.CENTRO_ACREDITADO_SI);
                                codOrigenAcred = ConfigurationParameter.getParameter(ConstantesMeLanbide03.CODORIGENACRED_LANF, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                            }
                            unidad.setCodCentro(codCentro);
                            unidad.setCodCentroLanF(codCentroLanF);
                            unidad.setCentroAcreditado(acreditado);
                            unidad.setCodUnidad(codUnidad);
                            unidad.setCodCertificado(codCertificado);
                            unidad.setCodOrganizacion(String.valueOf(codOrganizacion));
                            unidad.setCodProcedimiento(codProc);
                            unidad.setNumExpediente(numExpediente);
                            if(!codMotNoAcreditado.equalsIgnoreCase(" ")){
                                unidad.setCodMotNoAcreditado(codMotNoAcreditado);
                            }//if(!codMotNoAcreditado.equalsIgnoreCase(" "))
                            if(!codOrigenAcred.equalsIgnoreCase(" ")){
                                unidad.setCodOrigenAcred(codOrigenAcred);
                            }//if(!codOrigenAcred.equalsIgnoreCase(" "))
                            unidad.setClaveRegistral(claveRegistral);
                        listaUnidades.add(unidad);
                    }//for(int i=0; i<arrayUnidades.length; i++)
                    certificado.setUnidades(listaUnidades);
                }//if(unidades != null && !unidades.equalsIgnoreCase(""))

                /**** PRUEBA *****/

                if(log.isDebugEnabled()) log.debug("Insertamos el registro en la BBDD");
                boolean exito = false;
                Connection connection = null;
                try{
                    adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
                    connection = adapt.getConnection();

                    //nuevo
                    CerCertificadoVO certificadoActual = meLanbide03Manager.getCertificadoExpediente(certificado.getNumExpediente(), codOrganizacion, adapt);
                    boolean existeCertificado =false;
                    if(log.isDebugEnabled()) log.debug("certificadoActual: "+certificadoActual);
                    if (certificadoActual.getCodCertificado()!=null) existeCertificado=true;
                    if(log.isDebugEnabled()) log.debug("existeCertificado: "+existeCertificado);


                    adapt.inicioTransaccion(connection);

                    exito = meLanbide03Manager.insertarCertificadoExpediente(certificado, existeCertificado, connection);

                    if(log.isDebugEnabled()) log.debug("EXITO: INSERTAR CERTIFICADO EXPEDIENTE");
                    /*******/
                    if(exito){

                        if(modForm != null && !modForm.equalsIgnoreCase("")){
                            if(log.isDebugEnabled()) log.debug("Hacemos un split de las propiedades del modulo formativo");
                            Boolean guardarModuloFormativoCompleto = false;
                            String[] propModForm = modForm.split(ConstantesMeLanbide03.SPLIT_PROPIEDADES_MOD_FORM);

                            if(propModForm.length >= 3){
                                if(log.isDebugEnabled()) log.debug("Grabamos el modulo formativo");
                                guardarModuloFormativoCompleto = true;
                            }//if(propModForm.length >= 3)

                            CerModuloFormativoVO modulo = new CerModuloFormativoVO();
                            if(guardarModuloFormativoCompleto){
                                if(log.isDebugEnabled()) log.debug("Recuperamos si el modulo esta acreditado y su motivo");
                                String codModForm = propModForm[0];
                                String modAcreditado = propModForm[1];
                                String motivoAcreditado = propModForm[2];
                                if(log.isDebugEnabled()) log.debug("Construimos el objeto para guardar el modulo formativo");

                                    modulo.setCodCertificado(codCertificado);
                                    modulo.setCodModulo(codModForm);
                                    modulo.setCodMotivoAcreditacion(motivoAcreditado);
                                    modulo.setCodOrganizacion(codOrganizacion);
                                    modulo.setCodProcedimiento(codProc);
                                    modulo.setModuloAcreditado(Integer.valueOf(modAcreditado));
                                    modulo.setNumExpediente(numExpediente);
                                //if(log.isDebugEnabled()) log.debug("Insertamos el objeto de la BBDD");
                                //meLanbide03Manager.insertarModuloPracticas(modulo, connection);
                            }else{
                                if(log.isDebugEnabled()) log.debug("Solo recuperamos el codigo del modulo formativo");
                                String codModForm = propModForm[0];
                                if(log.isDebugEnabled()) log.debug("Construimos el objeto para guardar el modulo formativo");

                                    modulo.setCodCertificado(codCertificado);
                                    modulo.setCodModulo(codModForm);
                                    modulo.setCodMotivoAcreditacion(null);
                                    modulo.setCodOrganizacion(codOrganizacion);
                                    modulo.setCodProcedimiento(codProc);
                                    modulo.setModuloAcreditado(null);
                                    modulo.setNumExpediente(numExpediente);                            
                            }
                            if(log.isDebugEnabled()) log.debug("Insertamos el Modulo Prácticas de la BBDD");                            
                            meLanbide03Manager.insertarModuloPracticas(modulo, connection);

                            if(log.isDebugEnabled()) log.debug("HACEMOS COMMIT");
                            connection.commit();
                        }

                    }else{

                        connection.rollback();
                        codigoOperacion = "72";
                    }

                    /********/
                } catch (MELANBIDE03Exception ex) {
                    ex.printStackTrace();
                    log.error("Se ha producido un error insertando los datos del certificado en la BBDD: " + ex.getMessage());
                    codigoOperacion = "72";
                     try{
                       if(connection!=null) connection.rollback();
                    }catch(SQLException f){
                        log.error("Error al realizar rollback en la operacion MELANBIDE03.insertarCertificadoCentroExpediente: " + f.getMessage());
                    }
                }catch(BDException e){
                    e.printStackTrace();
                    log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                    codigoOperacion = "72";
                    try{
                       if(connection!=null) connection.rollback();
                    }catch(SQLException f){
                        log.error("Error al realizar rollback en la operacion MELANBIDE03.insertarCertificadoCentroExpediente: " + f.getMessage());
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                    codigoOperacion = "72";
                    log.error("Error al realizar alguna operación contra la BBDD: " + e.getMessage());
                    try{
                       if(connection!=null) connection.rollback();
                    }catch(SQLException f){
                        log.error("Error al realizar rollback en la operacion MELANBIDE03.insertarCertificadoCentroExpediente: " + f.getMessage());
                    }
                }
                finally{
                    try{
                       if(connection!=null) connection.close();
                    }catch(SQLException f){
                        log.error("Error al cerrar conexión contra la BBDD: " + f.getMessage());
                    }
                }
            }else codigoOperacion = "20";            
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        }//try-catch
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<SEGUNDOEXP>");
                xmlSalida.append(segundoExp);
            xmlSalida.append("</SEGUNDOEXP>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        if(log.isDebugEnabled()) log.debug("insertarCertificadoCentroExpediente() : END");
    }//insertarCertificadoCentroExpediente
    
    
    
    
    
    public void borrarCertificadoExpediente (int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response) {
        if(log.isDebugEnabled()) log.debug("borrarCertificadoExpediente( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        if(log.isDebugEnabled()) log.debug("recuperamos el código del certificado de los parametros de la request");
        String codCertificado = (String) request.getParameter("codCertificado");
        if(log.isDebugEnabled()) log.debug("Buscamos el certificado seleccionado");
        CerCertificadoVO certificado = null;
        String codigoOperacion = "70";
        try{
            certificado = new CerCertificadoVO();
            certificado.setNumExpediente(numExpediente);
            certificado.setCodOrganizacion(Integer.valueOf(codOrganizacion));
            certificado.setCodCertificado(codCertificado);
            meLanbide03Manager.borrarCertificadoExpediente(certificado, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            CerModuloFormativoVO modulo = new CerModuloFormativoVO();
            modulo.setNumExpediente(numExpediente);
            modulo.setCodOrganizacion(codOrganizacion);
            meLanbide03Manager.eliminarModuloPracticas(modulo, getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (MELANBIDE03Exception ex) {
            log.error("Se ha producido un error eliminando los datos del certificado del expediente BBDD", ex);
            codigoOperacion = "71";
        }//try-catch
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        
        if(log.isDebugEnabled()) log.debug("borrarCertificadoExpediente() : END");
    }//borrarCertificadoExpediente
    
    /**
     * 
     * @param certificado
     * @param numExpediente
     * @return 
     */
    private ArrayList<CerUnidadeCompetencialVO> getUnidadesCompetenciales(String numExpediente, CerCertificadoVO certificado, String codOrganizacion) throws BDException, Exception{
        Connection con = null;
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try{
            con = adaptador.getConnection();
            return getUnidadesCompetenciales(numExpediente, certificado, codOrganizacion, con);
        }catch(Exception ex){
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
    }
    /**
     * 
     * @param certificado
     * @param numExpediente
     * @return 
     */
    private ArrayList<CerUnidadeCompetencialVO> getUnidadesCompetenciales(String numExpediente, CerCertificadoVO certificado, String codOrganizacion, Connection con) throws BDException, Exception{
        if(log.isDebugEnabled()) log.debug("getUnidadesCompetenciales() : BEGIN");
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        ArrayList<CerUnidadeCompetencialVO> unidades = new ArrayList<CerUnidadeCompetencialVO>();
        try{
            ArrayList<CerUnidadeCompetencialVO> unidadesExpediente = new ArrayList<CerUnidadeCompetencialVO>();
            unidadesExpediente = meLanbide03Manager.getCentrosExpedienteYCertificado(numExpediente, certificado.getCodCertificado(), String.valueOf(codOrganizacion), getAdaptSQLBD(String.valueOf(codOrganizacion)));
            
                //Comprobamos si para ese certificado alguna de las unidades ya está acreditada en algún otro expediente por que si es asi
                //deberemos de acreditarla obligatoriamente en todos y bloquearla.
                if(log.isDebugEnabled()) log.debug("Recuperamos las unidades competenciales acreditadas previamente");
                ArrayList<CerUnidadeCompetencialVO> unidadesCertificado = new ArrayList<CerUnidadeCompetencialVO>();
                unidadesCertificado = meLanbide03Manager.getListaUnidadesCompetenciales(certificado.getCodCertificado(), con);
                ArrayList<CerUnidadeCompetencialVO> unidadesYaAcreditadas = meLanbide03Manager.getUnidadesYaAcreditadasInteresado(unidadesCertificado, numExpediente, certificado.getCodCertificado(), String.valueOf(codOrganizacion), con);

                for(CerUnidadeCompetencialVO unidadCertificado : unidadesCertificado){
                    Boolean existeUnidadExpediente = false;
                    String codUnidadCertificado = unidadCertificado.getCodUnidad();
                    CerUnidadeCompetencialVO unidadSeleccionada = new CerUnidadeCompetencialVO();
                    for(CerUnidadeCompetencialVO unidadExpediente : unidadesExpediente){
                        if(codUnidadCertificado.equalsIgnoreCase(unidadExpediente.getCodUnidad())){
                            existeUnidadExpediente = true;
                            unidadSeleccionada = unidadExpediente;
                            unidadSeleccionada.setBloquearEnPantalla("N");
                            break;
                        }//if(codUnidadCertificado.equalsIgnoreCase(unidadExpediente.getCodUnidad()))
                    }//for(CerUnidadeCompetencialVO unidadExpediente : unidadesExpediente)
                    if(!existeUnidadExpediente){
                        for(CerUnidadeCompetencialVO unidadAcreditada : unidadesYaAcreditadas){
                            if(codUnidadCertificado.equalsIgnoreCase(unidadAcreditada.getCodUnidad())){
                                unidadSeleccionada = unidadAcreditada;
                                unidadSeleccionada.setBloquearEnPantalla("S");
                            }//if(codUnidadCertificado.equalsIgnoreCase(unidadAcreditada.getCodUnidad()))
                        }//for(CerUnidadeCompetencialVO unidadAcreditada : unidadesYaAcreditadas)
                    }//if(!existeUnidadExpediente)
                    unidades.add(unidadSeleccionada);
                }//for(CerUnidadeCompetencialVO unidadCertificado : unidadesCertificado)
        }catch (BDException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
            throw new BDException(ex.getDescripcion());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUnidadesCompetenciales() : END");
        return unidades;
    }//getUnidadesCompetenciales
    
    private ArrayList<CerUnidadeCompetencialVO> getUnidadesAcreditadas(String numExpediente, CerCertificadoVO certificado, 
            String codOrganizacion) throws BDException, Exception{
        if(log.isDebugEnabled()) log.error("getUnidadesAcreditadas() : BEGIN");
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        ArrayList<CerUnidadeCompetencialVO> unidades = new ArrayList<CerUnidadeCompetencialVO>();
        try{
            ArrayList<CerUnidadeCompetencialVO> unidadesExpediente = new ArrayList<CerUnidadeCompetencialVO>();
            unidadesExpediente = meLanbide03Manager.getCentrosExpedienteYCertificado(numExpediente, certificado.getCodCertificado(), String.valueOf(codOrganizacion), getAdaptSQLBD(String.valueOf(codOrganizacion)));
                
                //Comprobamos si para ese certificado alguna de las unidades ya está acreditada en algún otro expediente por que si es asi
                //deberemos de acreditarla obligatoriamente en todos y bloquearla.
                if(log.isDebugEnabled()) log.info("Recuperamos las unidades competenciales acreditadas previamente");
                ArrayList<CerUnidadeCompetencialVO> unidadesCertificado = new ArrayList<CerUnidadeCompetencialVO>();
                unidadesCertificado = meLanbide03Manager.getListaUnidadesCompetenciales(certificado.getCodCertificado(), getAdaptSQLBD(String.valueOf(codOrganizacion)));
                ArrayList<CerUnidadeCompetencialVO> unidadesYaAcreditadas =  
                    meLanbide03Manager.getUnidadesYaAcreditadasInteresado(unidadesCertificado, numExpediente, certificado.getCodCertificado(), 
                    String.valueOf(codOrganizacion), getAdaptSQLBD(String.valueOf(codOrganizacion)));

                for(CerUnidadeCompetencialVO unidadCertificado : unidadesCertificado){
                    Boolean existeUnidadExpediente = false;
                    String codUnidadCertificado = unidadCertificado.getCodUnidad();
                    log.info("codUnidadCertificado: " + codUnidadCertificado);
                    CerUnidadeCompetencialVO unidadSeleccionada = new CerUnidadeCompetencialVO();
                    for(CerUnidadeCompetencialVO unidadExpediente : unidadesExpediente){
                    log.info("unidadExpediente.getCodUnidad(): " + unidadExpediente.getCodUnidad());
                        if(codUnidadCertificado.equalsIgnoreCase(unidadExpediente.getCodUnidad())){
                            existeUnidadExpediente = true; log.info("existeUnidadExpediente");
                        }//if(codUnidadCertificado.equalsIgnoreCase(unidadExpediente.getCodUnidad()))
                    }//for(CerUnidadeCompetencialVO unidadExpediente : unidadesExpediente)
                    
                    if(!existeUnidadExpediente){
                        for(CerUnidadeCompetencialVO unidadAcreditada : unidadesYaAcreditadas){                            
                            log.info("unidadAcreditada.getCodUnidad(): " + unidadAcreditada.getCodUnidad());
                            if(codUnidadCertificado.equalsIgnoreCase(unidadAcreditada.getCodUnidad())){                                
                                log.info("bloqueada");
                                unidadSeleccionada = unidadAcreditada;
                                unidadSeleccionada.setBloquearEnPantalla("S");
                                unidades.add(unidadSeleccionada);
                            }//if(codUnidadCertificado.equalsIgnoreCase(unidadAcreditada.getCodUnidad()))
                        }//for(CerUnidadeCompetencialVO unidadAcreditada : unidadesYaAcreditadas)
                    }//if(!existeUnidadExpediente)
                }//for(CerUnidadeCompetencialVO unidadCertificado : unidadesCertificado)
        }catch (BDException ex) {
            log.error("Error de BD en funcion getUnidadesAcreditadas: " + ex);
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
            throw new BDException(ex.getDescripcion());
        } catch (Exception ex) {
            log.error("Error en funcion getUnidadesAcreditadas: " + ex);
            java.util.logging.Logger.getLogger(MELANBIDE03.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception(ex.getMessage());
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getUnidadesAcreditadas() : END");
        return unidades;
    }//getUnidadesCompetenciales
    
    /**
     * Carga parámetros necesarios en la request.
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request 
     */
    private void cargarParametrosBásicosEnRequest(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request){
        if(log.isDebugEnabled()) log.debug("cargarParametrosBásicosEnRequest() : BEGIN");
            if(log.isDebugEnabled()) log.debug("Almacenamos el nombre del módulo en la request");
            request.setAttribute("nombreModulo", ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_MODULO, ConstantesMeLanbide03.FICHERO_PROPIEDADES));
        if(log.isDebugEnabled()) log.debug("cargarParametrosBásicosEnRequest() : END");
    }//cargarParametrosBásicosEnRequest
/**
     * Operacion a nivel de tramite que genera un report PDF con los certificados acreditados.
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return String. Si el código es 0 todo salio correcto.
     */
    public String generarPDF (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente){
        /*numExpediente = "2014/CEPAP/000006";
        codOrganizacion = 0;*/
        if(log.isDebugEnabled()) log.debug("******** generarPDF() : BEGIN");
        String resultado = ConstantesMeLanbide03.OPERACION_CORRECTA;
        MeLanbide03GenerateReportVO generateReport = new MeLanbide03GenerateReportVO();
        String xmlReport = new String();
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        ArrayList<MeLanbide03InteresadoGenerateReportVO> listaInteresados = new ArrayList<MeLanbide03InteresadoGenerateReportVO>();
        ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO> listaUnidades = new ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO>();
        final MeLanbide03ReportFormatEnum reportFormat = MeLanbide03ReportFormatEnum.PDF;
        byte[] report = null;
        if(log.isDebugEnabled()) log.debug("************ Recuperamos los interesados del expediente para la generación del report");
        try {
            listaInteresados = meLanbide03Manager.getInteresadosReport(numExpediente, codOrganizacion, getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (MELANBIDE03Exception ex) {
            ex.printStackTrace();
            log.error("******** Se ha producido un error recuperando los datos de los interesados del expediente");
            resultado = ConstantesMeLanbide03.ERROR_RECUPERANDO_INTERESADOS;
        }//try-catch
        
        if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA)){
            if(log.isDebugEnabled()) log.debug("Recuperamos los datos del certificado y las unidades competenciales");
            try {
                listaUnidades = meLanbide03Manager.getUnidadesCompetencialesReport(numExpediente, codOrganizacion, 
                        getAdaptSQLBD(String.valueOf(codOrganizacion)));
            } catch (MELANBIDE03Exception ex) {
                log.error("Se ha producido un error recuperando la informacion de certificado y unidades para el report: " + ex.getMessage());
                resultado = ConstantesMeLanbide03.ERROR_UNIDADES_CERTIFICADO;
            }//try-catch
        }//if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA))
        
        
        log.debug(" *********** generarPDF listaUnidades: " + listaUnidades.size());
        if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA)){
            if(log.isDebugEnabled()) log.debug("Creamos el objeto para el report");
            generateReport.setInteresados(listaInteresados);
            generateReport.setUnidades(listaUnidades);
        }//if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA))
        
        if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA)){
            if(log.isDebugEnabled()) log.debug("Generamos el xml para el report");
            xmlReport = MeLanbide03ReportHelper.generarXML(generateReport);
            log.debug(" *********** generarPDF xmlReport: " + xmlReport);
            
        }//if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA))
        
        if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA)){
            if(log.isDebugEnabled()) log.debug("Creamos el report");
            try{
              /*  String nombrePlantillaReport="";
                int anoFechaPresentacion = 0;
                
                anoFechaPresentacion = meLanbide03Manager.getAnoFechaPresentacion(numExpediente,getAdaptSQLBD(String.valueOf(codOrganizacion)));
                
                if(anoFechaPresentacion<=2012){//Javier 
                    nombrePlantillaReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_APAJ, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }
                else if(anoFechaPresentacion>2012){//Adolfo
                    nombrePlantillaReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_APA, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }*/
                
                String nombrePlantillaReport="";
                //int anoFechaPresentacion = 0;
                 String fechaPresentacion="";
               // anoFechaPresentacion = meLanbide03Manager.getAnoFechaPresentacion(numExpediente,getAdaptSQLBD(String.valueOf(codOrganizacion)));
                fechaPresentacion = meLanbide03Manager.getFechaPresentacion(numExpediente,getAdaptSQLBD(String.valueOf(codOrganizacion)));
              
                
                //Debe hacer esto: firma de Javier si la 
                //fecha solicitud es <= 2012 y 
                //la de Adolfo si es >2012 y <=21/01/2017 y
                //la de Borja >=21/01/2017
                if(fechaPresentacion.compareTo("2012/12/31")<= 0){//Javier 
                    nombrePlantillaReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_APAJ, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }
                else if(fechaPresentacion.compareTo("2012/12/31") > 0 && fechaPresentacion.compareTo("2017/01/21") < 0 ){//Adolfo
                    nombrePlantillaReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_APA, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }
                else {//Borja
                    // cambiada plantilla 03/04/2018
                    //nombrePlantillaReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_APAB,
                    nombrePlantillaReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_APA18, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }
                
                
                String xpathReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.XPATH_PLANTILLA_PDF_APA, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                String nombreArchivo = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_ARCHIVO_REPORT_GENERADO_PDF_APA, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                
                log.debug(" ********** MELANBIDE03.generarPDF nombrePlantillaReport: " + nombrePlantillaReport);
                log.debug(" ********** MELANBIDE03.generarPDF xpathReport: " + xpathReport);
                log.debug(" ********** MELANBIDE03.generarPDF nombreArchivo: " + nombreArchivo);
                                
                report = MeLanbide03ReportHelper.runReport(nombrePlantillaReport, null, xmlReport, xpathReport, reportFormat);                                
                log.debug(" ********** MELANBIDE03.generarPDF contenido binario del informe: " + report);
                
                if(log.isDebugEnabled()) log.debug("Creamos el objeto del report");
                MeLanbide03ReportVO reportVO = new MeLanbide03ReportVO();
                    Date fechaReport = new Date();
                    reportVO.setCodOrganizacion(codOrganizacion);
                    reportVO.setNumExpediente(numExpediente);
                    reportVO.setFechaCreacion(fechaReport);
                    reportVO.setReport(report);
                    reportVO.setNombre(generarNombreArchivo(nombreArchivo, numExpediente, fechaReport));
                    reportVO.setMimeType(MeLanbide03MimeTypeUtil.guessMimeTypeFromExtension("." + reportFormat.getCode()));
                meLanbide03Manager.insertarReport(reportVO, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Se ha producido un error generando el report", ex);
                resultado = ConstantesMeLanbide03.ERROR_GENERANDO_REPORT;
            }//try-catch
        }//if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA))
        
        if(log.isDebugEnabled()) log.debug("generarPDF() : END");
        return resultado;
    }//generarPDF
    public String listadoExpedientes(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){        

        try{
            
            log.debug("En funcion listadoExpedientes ");
            String nombreFichero = request.getParameter("fichero");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            log.debug("listadoExpedientes ");
            ArrayList<ImpresionExpedientesLanbideValueObject> salida = MeLanbide03Manager.getInstance().getListaExpedientesDocumento(codOrganizacion, nombreFichero, adapt);
            request.setAttribute("lista_expedientes_documento", salida);
            request.setAttribute("nombreFichero", nombreFichero);
        }
        catch(Exception ex){
            log.debug("Error en listadoExpedientes: " + ex);
        }
        return "/jsp/extension/melanbide03/expedientesRelacionadosExcel.jsp";
    }
                  
    public void generarPdfApaPendientes(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        List<GeneralValueObject> expedientes = new ArrayList<GeneralValueObject>();
        List<GeneralValueObject> documentos = new ArrayList<GeneralValueObject>();
        String nomDoc = "_blank_";
        String resultado = ConstantesMeLanbide03.OPERACION_CORRECTA;
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");
            List<Subreport> subreportList = new ArrayList<Subreport>();

            Map<String, Object> reportParams = new HashMap<String, Object>();
            //String urlLogoLanbide="/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/imagenes/Lanbide_logo.jpg";
            //reportParams.put("logo", getClass().getClassLoader().getResourceAsStream(urlLogoLanbide));
            //String urlLogoGV="/es/altia/flexia/integracion/moduloexterno/melanbide32/reports/imagenes/GV_logo.jpg";
            //reportParams.put("logo2", getClass().getClassLoader().getResourceAsStream(urlLogoGV));
        
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                
            
            String subreportName = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            String expParam = request.getParameter("listaExpedientesSeleccionados");
            String masterReportName = "";
            
            EjecutaImpresion ejecuta = new EjecutaImpresion();            
            ejecuta.start(resultado, expParam, codOrganizacion, subreportName, usuario, adapt);
            
            
            /*Subreport subreport = null;
            byte[] pdf = null;
            
            try
            {
                //Recuperar los datos
                ArrayList<MeLanbide03InteresadoGenerateReportVO> listaInteresados = new ArrayList<MeLanbide03InteresadoGenerateReportVO>();
                ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO> listaUnidades = new ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO>();
                MeLanbide03GenerateReportVO generateReport = null;
                String xmlReport = null;
                
                String[] expedientesPendientes = new String[0];
                //String expParam = request.getParameter("listaExpedientesSeleccionados");
                
                if(expParam != null && !expParam.equals(""))
                {
                    expedientesPendientes = expParam.split("-");
                }
                
                String xpathReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.XPATH_PLANTILLA_PDF_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                
                String numExp = null;
                String claveRegistral = null;
                int cont = 0;
                while(cont < expedientesPendientes.length && resultado.equals(ConstantesMeLanbide03.OPERACION_CORRECTA))
                {
                    numExp = expedientesPendientes[cont];
                    generateReport = new MeLanbide03GenerateReportVO();
                    
                    try
                    {
                        claveRegistral = this.generarClaveRegistral(codOrganizacion, numExp);
                        if(claveRegistral == null  || claveRegistral.equals(""))
                        {
                            resultado = ConstantesMeLanbide03.ERROR_GENERANDO_REPORT;
                        }
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                    
                    xmlReport = "";
                    try 
                    {
                        listaInteresados = MeLanbide03Manager.getInstance().getInteresadosReport(numExp, codOrganizacion, adapt);
                    } 
                    catch (MELANBIDE03Exception ex) 
                    {
                        ex.printStackTrace();
                        log.error("Se ha producido un error recuperando interesados para el inform: "+ex.getMessage());
                        //resultado = ConstantesMeLanbide03.ERROR_RECUPERANDO_INTERESADOS;
                    }
        
                    if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA))
                    {
                        try 
                        {
                            listaUnidades = MeLanbide03Manager.getInstance().getUnidadesCompetencialesReport(numExp, codOrganizacion, adapt);
                        } 
                        catch (MELANBIDE03Exception ex) 
                        {
                            ex.printStackTrace();
                            log.error("Se ha producido un error recuperando la informacion de certificado y unidades para el report: " + ex.getMessage());
                            //resultado = ConstantesMeLanbide03.ERROR_UNIDADES_CERTIFICADO;
                        }
                    }
                    
                    if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA))
                    {
                        generateReport.setInteresados(listaInteresados);
                        generateReport.setUnidades(listaUnidades);
                    }

                    if(resultado.equalsIgnoreCase(ConstantesMeLanbide03.OPERACION_CORRECTA))
                    {
                        log.debug(numExp+" - listaInteresados: "+(listaInteresados != null ? listaInteresados.size() : "null"));
                        log.debug(numExp+" - listaUnidades: "+(listaUnidades != null ? listaUnidades.size() : "null"));
                        xmlReport = MeLanbide03ReportHelper.generarXML(generateReport);
                    }
                    
                    if(generateReport.getInteresados() != null && generateReport.getInteresados().size() > 0)
                    {
                        if(generateReport.getUnidades() != null && generateReport.getUnidades().size() > 0)
                        {
                            try
                            {
                                subreport = MeLanbide03ReportHelper.inicializarSubreport(subreportName, xmlReport, xpathReport, reportParams);
                                subreportList.add(subreport);
                            }
                            catch(Exception ex)
                            {
                                resultado = ConstantesMeLanbide03.ERROR_GENERANDO_REPORT;
                            }
                        }
                    }
                    cont++;
                }
                
                if(!resultado.equals(ConstantesMeLanbide03.OPERACION_CORRECTA))
                {
                    subreportList.clear();
                    log.debug("REPORT VACIO");
                    reportParams.put("mensaje", MeLanbide03I18n.getInstance().getMensaje(usuario.getIdioma(), "msg.error.recuperarDatosPdfPendientes"));
                    masterReportName = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_GENERAL_VACIO, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }
                else if(subreportList.isEmpty())
                {
                    subreportList.clear();
                    log.debug("REPORT VACIO");
                    reportParams.put("mensaje", MeLanbide03I18n.getInstance().getMensaje(usuario.getIdioma(), "msg.informePendientesVacío"));
                    masterReportName = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_GENERAL_VACIO, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }
                else
                {
                    log.debug("REPORT LLENO");
                    masterReportName = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_GENERAL, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }
                
                pdf = MeLanbide03ReportHelper.runMasterReportWithSubreports(masterReportName, reportParams, subreportList, MeLanbide03ReportFormatEnum.PDF);
                
                if(pdf != null && pdf.length > 0)
                {
                    try
                    {
                        ResourceBundle m_Conf = ResourceBundle.getBundle("common");

                        File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                        if(!directorioTemp.exists())
                        {
                            directorioTemp.mkdirs();
                        }
                        File informe = File.createTempFile("expedientesCEPAP", ".pdf", directorioTemp);
                        nomDoc = informe.getName();
                        //FileOutputStream archivoSalida = new FileOutputStream(informe);
                        //archivoSalida.close();


                        FileOutputStream fileOutput = new FileOutputStream(informe);
                        BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
                        bufferedOutput.write(pdf, 0, pdf.length);
                        bufferedOutput.flush();
                        bufferedOutput.close();
                        fileOutput.flush();
                        fileOutput.close();
                        
                        resultado = ConstantesMeLanbide03.OPERACION_CORRECTA;
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        log.error("Se ha producido un error generando pdf expedientes pendientes apa: "+ex.getMessage());
                        resultado = ConstantesMeLanbide03.ERROR_GENERANDO_REPORT;
                    }
                }
                else
                {
                    resultado = ConstantesMeLanbide03.ERROR_GENERANDO_REPORT;
                }
                
                if(resultado.equals(ConstantesMeLanbide03.OPERACION_CORRECTA))
                {
                    int result = MeLanbide03Manager.getInstance().guardarPdfApaPendientes(nomDoc, pdf, adapt);
                    if(result == 0)
                    {
                        resultado = ConstantesMeLanbide03.ERROR_GENERANDO_REPORT;
                    }
                    //int resInsertImpr = inserta_MELANBIDE03_IMPRESION_CEPAP(nomDoc, pdf, adapt);
                    log.error("antes de inserta_MELANBIDE03_EXP_IMPR_CEPAP");
                    int resInsertImprExp = inserta_MELANBIDE03_EXP_IMPR_CEPAP(nomDoc, expedientesPendientes, adapt);
                }
                
                if(resultado.equals(ConstantesMeLanbide03.OPERACION_CORRECTA))
                {
                    //Avanzamos los expedientes
                    int res = 0;
                    log.debug("Resultado antes de avanzarTramite: "+res);
                    String codigoTramite = MeLanbide03Manager.getInstance().
                            getCodigoInternoTramite(codOrganizacion, ConstantesMeLanbide03.CODIGO_PROCEDIMIENTO,
                                                    ConfigurationParameter.getParameter(ConstantesMeLanbide03.CODIGO_TRAMITE_IMPRESION_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES),
                                                    adapt);
                    
                    res = this.finalizarAvanzarTramite(codOrganizacion, codigoTramite, usuario.getParamsCon(), usuario, expedientesPendientes);
                    log.debug("Resultado despues de avanzarTramite: "+res);
                }
                
                if(resultado.equals(ConstantesMeLanbide03.OPERACION_CORRECTA))
                {                    
                    try
                    {
                        expedientes = MeLanbide03Manager.getInstance().getExpedientesApaPendientes(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        log.error("Se ha producido un error recuperando expedientes para actualizr pantalla pendientes APA: "+ex.getMessage());
                    }
                    try
                    {
                        documentos = MeLanbide03Manager.getInstance().getFicherosImpresionGenerados(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        log.error("Se ha producido un error recuperando documentos para actualizr pantalla pendientes APA: "+ex.getMessage());
                    }
                }
                else
                {
                    //Mostrar mensaje de error
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                log.error("Se ha producido un error generando pdf expedientes pendientes apa: "+ex.getMessage());
            }*/
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un error generando pdf expedientes pendientes apa: "+ex.getMessage());
        }
        //escribirDatosPantallaPendientesAPARequest(resultado, expedientes, documentos, nomDoc, response);
    }
    
    
    public void generarExcelOficios(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        List<GeneralValueObject> expedientes = new ArrayList<GeneralValueObject>();
        List<GeneralValueObject> documentos = new ArrayList<GeneralValueObject>();
        String nomDoc = "_blank_";
        String resultado = ConstantesMeLanbide03.OPERACION_CORRECTA;
        try
        {
            log.info("generarExcelOficios");
            UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");
            List<Subreport> subreportList = new ArrayList<Subreport>();

            Map<String, Object> reportParams = new HashMap<String, Object>();
        
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                
            
            String subreportName = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_APA, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            String expParam = request.getParameter("listaExpedientesSeleccionados");
            
            EjecutaImpresionOf ejecuta = new EjecutaImpresionOf();            
            ejecuta.start(resultado, expParam, codOrganizacion, subreportName, usuario, adapt);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Se ha producido un error generando pdf expedientes pendientes apa: "+ex.getMessage());
        }
        //escribirDatosPantallaPendientesAPARequest(resultado, expedientes, documentos, nomDoc, response);
    }
    
    private void escribirDatosPantallaPendientesAPARequest(String codigoOperacion, List<GeneralValueObject> expedientes, List<GeneralValueObject> documentos, String ultFicheroGenerado, HttpServletResponse response)
    {
        String interesados = "";
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            
        if(expedientes != null && !expedientes.isEmpty())
        {
            xmlSalida.append("<EXPEDIENTES>");
         
            for(GeneralValueObject act : expedientes)
            {
                xmlSalida.append("<EXPEDIENTE>");
                
                    xmlSalida.append("<NUM_EXPEDIENTE>");
                        xmlSalida.append(act.getAtributo("numExpediente") != null ? act.getAtributo("numExpediente") : "-");
                    xmlSalida.append("</NUM_EXPEDIENTE>");

                    interesados = act.getAtributo("interesados") != null ? act.getAtributo("interesados").toString() : "-";
                    try
                    {
                        interesados = interesados.replaceAll("<br>", "</br>");
                        interesados = interesados.replaceAll("</br>","<br></br>");
                    }
                    catch(Exception ex)
                    {
                        interesados = act.getAtributo("interesados") != null ? act.getAtributo("interesados").toString() : "-";
                        try
                        {
                            interesados = interesados.replaceAll("<br>", "");
                            interesados = interesados.replaceAll("</br>","");
                        }
                        catch(Exception e)
                        {
                            interesados = act.getAtributo("interesados") != null ? act.getAtributo("interesados").toString() : "-";
                        }
                    }
                    xmlSalida.append("<INTERESADOS>");
                        xmlSalida.append(interesados);
                    xmlSalida.append("</INTERESADOS>");

                    xmlSalida.append("<NUM_REGISTRO>");
                        xmlSalida.append(act.getAtributo("numRegistro") != null ? act.getAtributo("numRegistro") : "-");
                    xmlSalida.append("</NUM_REGISTRO>");

                    xmlSalida.append("<FECHA_ENTRADA>");
                        xmlSalida.append(act.getAtributo("fechaEntrada") != null ? act.getAtributo("fechaEntrada") : "-");
                    xmlSalida.append("</FECHA_ENTRADA>");
                    
                    xmlSalida.append("<FECHA_SOLICITUD>");
                        xmlSalida.append(act.getAtributo("fecPres") != null ? act.getAtributo("fecPres") : "-");
                    xmlSalida.append("</FECHA_SOLICITUD>");
                    
                xmlSalida.append("</EXPEDIENTE>");
            }
            xmlSalida.append("</EXPEDIENTES>");
        }
        
        if(documentos != null && !documentos.isEmpty())
        {
            xmlSalida.append("<DOCUMENTOS>");
            for(GeneralValueObject act : documentos)
            {
                xmlSalida.append("<DOCUMENTO>");
                
                    xmlSalida.append("<NOMBRE_FICHERO>");
                        xmlSalida.append(act.getAtributo("numExpediente") != null ? act.getAtributo("numExpediente") : "-");
                    xmlSalida.append("</NOMBRE_FICHERO>");
                
                    xmlSalida.append("<FECHA_GENERACION>");
                        xmlSalida.append(act.getAtributo("fechaGeneracion") != null ? act.getAtributo("fechaGeneracion") : "-");
                    xmlSalida.append("</FECHA_GENERACION>");
                    xmlSalida.append("<OFICINA>");
                        xmlSalida.append(act.getAtributo("oficina") != null ? act.getAtributo("oficina") : "-");
                    xmlSalida.append("</OFICINA>");
                    
                xmlSalida.append("</DOCUMENTO>");
            }
            xmlSalida.append("</DOCUMENTOS>");
        }
        if(ultFicheroGenerado != null && !ultFicheroGenerado.equals(""))
        {
            xmlSalida.append("<FICHERO_GENERADO>");
                xmlSalida.append(ultFicheroGenerado);
            xmlSalida.append("</FICHERO_GENERADO>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug(xmlSalida);
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
    }
    
    public void descargarPdfApaPendientes(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            String nomDoc = request.getParameter("nomDoc");
            byte[] pdf = MeLanbide03Manager.getInstance().getPdfPorNombre(nomDoc, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment;filename="+nomDoc);
            response.setContentLength(pdf.length);
            response.getOutputStream().write(pdf, 0, pdf.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
	
	/**
     * Metodo que genera un string con el nombre del archivo concatenando el nombre deseado con el numero de expediente y la fecha del report
     * con el formato NOMBREARCHIVO_NUMEXPEDIENTE_FECHA
     * 
     * @param nombrePDF
     * @param numExpediente
     * @param fecha
     * @return 
     */
    private String generarNombreArchivo (String nombrePDF, String numExpediente, Date fecha){
        if(log.isDebugEnabled()) log.debug("generarNombreArchivo() : BEGIN");
        StringBuilder nombreArchivo = new StringBuilder();
            nombreArchivo.append(nombrePDF);
            nombreArchivo.append(ConstantesMeLanbide03.SEPARADOR_NOMBRE_ARCHIVOS);
            nombreArchivo.append(convertirNumExpediente(numExpediente));
            nombreArchivo.append(ConstantesMeLanbide03.SEPARADOR_NOMBRE_ARCHIVOS);
            nombreArchivo.append(convertirFecha(fecha));
        if(log.isDebugEnabled()) log.debug("generarNombreArchivo() : END");
        return nombreArchivo.toString();
    }//generarNombreArchivo
    
    /**
     * Convierte el numero de expediente del formato 0000/AAAAA/00000000 a 0000AAAAA00000000
     * 
     * @param numExpediente
     * @return 
     */
    private String convertirNumExpediente (String numExpediente){
        if(log.isDebugEnabled()) log.debug("convertirNumExpediente() : BEGIN");
        StringBuilder numExpedienteConvertido = new StringBuilder();
        String[] datosExpediente = numExpediente.split(ConstantesMeLanbide03.BARRA_SEPARADORA);
        if(datosExpediente != null){
            numExpedienteConvertido.append(datosExpediente[0]);
            numExpedienteConvertido.append(datosExpediente[1]);
            numExpedienteConvertido.append(datosExpediente[2]);
        }//if(datosExpediente != null)
        if(log.isDebugEnabled()) log.debug("convertirNumExpediente() : END");
        return numExpedienteConvertido.toString();
    }//convertirNumExpediente
    
    private String convertirFecha (Date fecha){
        if(log.isDebugEnabled()) log.debug("convertirFecha() : BEGIN");
        MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
        String fechaConvertida = new String();
        if(fecha != null){
            fechaConvertida = meLanbide03I18n.getFechaConvertida("ddMMyyyy", fecha);
        }//if(calFecha != null)
        if(log.isDebugEnabled()) log.debug("convertirFecha() : END");
        return fechaConvertida;
    }//convertirFecha
	
	/**
     * Metodo que carga el certificado de la BBDD asociado al expediente si este existe.
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return 
     */
    public String inicioPDF (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        if(log.isDebugEnabled()) log.debug("inicioPDF() : BEGIN");
        String redireccion = null;
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        String cargarDatos = "N";
        UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");
        if(numExpediente!=null && !"".equals(numExpediente)){
            String[] datos          = numExpediente.split(BARRA);
            String ejercicio        = datos[0];
            String codProcedimiento = datos[1];
            redireccion = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_PDF_SALIDA, this.getNombreModulo());
        }//if(numExpediente!=null && !"".equals(numExpediente))
        if(log.isDebugEnabled()) log.debug("Recuperamos la redireccion = " + redireccion);
        
        //Recuperamos si hay reports para el expediente.
        if(log.isDebugEnabled()) log.debug("Recuperamos los posibles reports del expediente");
        ArrayList<MeLanbide03ReportVO> listaReports = new ArrayList<MeLanbide03ReportVO>();
        try{
            listaReports = meLanbide03Manager.getReports(numExpediente, usuario.getIdioma(), codOrganizacion, 
                    getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(listaReports.size()>0) cargarDatos = "S";
        }catch(MELANBIDE03Exception ex){
            log.error("Se ha producido un error recuperando la lista de reports del expediente ", ex);
        }//try-catch
        request.setAttribute("listaReports", listaReports);
        request.setAttribute("cargarDatosPDF", cargarDatos);
        
        if(log.isDebugEnabled()) log.debug("inicioPDF() : END");
        return redireccion;
    }//inicioPDF
    
    public void getReport (int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response) {
        if(log.isDebugEnabled()) log.debug("getReport() : BEGIN");
        if(log.isDebugEnabled()) log.debug("Recuperamos los parametros de la request");
        String nomDoc = request.getParameter("nomDoc");
        if(log.isDebugEnabled()) log.debug("Report a recuperar = " + nomDoc);
        try {
            MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
            if (log.isDebugEnabled()) log.debug("Recuperamos el report");
            MeLanbide03ReportVO report = meLanbide03Manager.
                    getReport(nomDoc, numExpediente, codOrganizacion, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (report.getIdDokusi() != null && report.getIdDokusi().length()==16){
                log.info("Documento está en Dokusi con oid " + report.getIdDokusi());
                try {
//                    String procedimientoPlatea = "LAN11_CEPAP";
//                    String idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_"+procedimientoPlatea);
                    Lan6DokusiServicios servicios = new Lan6DokusiServicios("CEPAP"); // 2.2

                    Lan6Documento lan6Documento = servicios.consultarDocumento(report.getIdDokusi());

                    response.setContentType(report.getMimeType());
                    response.setHeader("Content-Disposition", "inline; filename=" + report.getNombre());
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    response.setContentLength(lan6Documento.getContenido().length);
                    response.getOutputStream().write(lan6Documento.getContenido(), 0, lan6Documento.getContenido().length);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
                } catch (Lan6Excepcion e) {
                    ArrayList<String> codes = e.getCodes();
                    ArrayList<String> messages = e.getMessages();
                    e.printStackTrace();
                }
                catch (Exception e) {
                    log.error("Se ha producido un error al descargar el documento");
                }
            } else {
                log.info("Documento no está en Dokusi");
                if (report.getReport() != null) {
                    if (log.isDebugEnabled())
                        log.debug("Si el report tiene contenido creamos un outputStream en la response");
                    log.info("Documento no está en Dokusi, está en BBDD");
                    try {
                        response.setContentType(report.getMimeType());
                        response.setHeader("Content-Disposition", "inline; filename=" + report.getNombre());
                        response.setHeader("Content-Transfer-Encoding", "binary");
                        response.setContentLength(report.getReport().length);
                        response.getOutputStream().write(report.getReport(), 0, report.getReport().length);
                        response.getOutputStream().flush();
                        response.getOutputStream().close();
                    } catch (Exception e) {
                        log.error("Se ha producido un error al descargar el documento");
                    }//try-catch
                }//if(report.getReport() != null)
            }
        }catch(MELANBIDE03Exception ex){
            log.error("Se ha producido un error recuperando el report ", ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getReport() : END");
    }//getReport
    
    private void cargarPantallaPendientesAPA(int codOrganizacion, HttpServletRequest request)
    {
        
        List<GeneralValueObject> consulta = new ArrayList<GeneralValueObject>();
        List<GeneralValueObject> consultaE = new ArrayList<GeneralValueObject>();
        try 
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Expedientes del procedimiento CEPAP en estado pendiente                    
            consultaE = MeLanbide03Manager.getInstance().getExpedientesApaPendientes(adapt);
            consulta = MeLanbide03Manager.getInstance().getFicherosImpresionGenerados(adapt);

        } 
        catch (Exception te) 
        {
            log.debug(te.getMessage());
            consulta = new ArrayList<GeneralValueObject>();
            consultaE = new ArrayList<GeneralValueObject>();
        }
        request.setAttribute("RelacionExpedientesAPA", consultaE);
        request.setAttribute("RelacionFicherosImpresionGenerados", consulta);
    }
    
    
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion){
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;      

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor)){
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            }catch(TechnicalException te){
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }//try-catch
            }// finally
         }// synchronized
        return adapt;
     }//getConnection
    
    
    public int finalizarAvanzarTramite(int codOrg, String codTram, String[] params, UsuarioValueObject usuario, String[] lListaExpedientesSeleccionados) throws BDException, SQLException, TechnicalException, WSException, TramitacionException, RemoteException 
    {

        Connection con = null;
        AdaptadorSQLBD adapt = new AdaptadorSQLBD(params);
        con = adapt.getConnection();
        String codOrgCad = String.valueOf(codOrg);
        Vector devolver = new Vector();
        int resultado = 0;
        //Se recupera la lista de tramites sgtes a abrir una vez que se finaliza el tramite Espera expedicion
        Vector<TramitacionExpedientesValueObject> listaTramites = new Vector<TramitacionExpedientesValueObject>();
        Vector<SiguienteTramiteTO> lista = null;

        try 
        {
            log.info("codOrg: " + codOrgCad);
            log.info("codTram: " + codTram);
            lista = ExpedienteImpresionManager.getInstance().getFlujoSalida(con, codOrgCad, ConstantesMeLanbide03.CODIGO_PROCEDIMIENTO, codTram, 0);

            //Si hay tramites
            if (!lista.isEmpty()) 
            {
                Iterator i = lista.iterator();
                while (i.hasNext()) 
                {
                    SiguienteTramiteTO tramite = (SiguienteTramiteTO) i.next();
                    TramitacionExpedientesValueObject traExp = new TramitacionExpedientesValueObject();
                    traExp.setCodTramite(tramite.getCodigoTramiteFlujoSalida());
                    traExp.setOcurrenciaTramite(tramite.getNumeroSecuencia());
                    traExp.setCodigoTramiteFlujoSalida(tramite.getCodigoTramiteFlujoSalida());
                    traExp.setInsertarCodUnidadTramitadoraTram("si");
                    //traExp.setCodUnidadTramitadoraTram(codTram);

                    listaTramites.add(traExp);
                }// while

                String[] datosExpediente = null;
                //listado de tramites sgtes 
                //por cada expediente seleccionados
                for (int j = 0; j < lListaExpedientesSeleccionados.length; j++) 
                {
                    datosExpediente = lListaExpedientesSeleccionados[j].split(ConstantesMeLanbide03.BARRA_SEPARADORA);
                    String ejercicio = datosExpediente[0];
                    String codProc = datosExpediente[1];
                    //TramitacionExpedientesValueObject tAUX = ExpedienteImpresionManager.getInstance().getInfoTramite(codProc, lListaExpedientesSeleccionados[j], codTram, params);                
                    TramitacionExpedientesValueObject tAUX = ExpedienteImpresionDAO.getInstance().getInfoTramite(codProc, lListaExpedientesSeleccionados[j], codTram, con);
                    log.debug("------------------------------------------------------------");
                    log.debug("------------------------------------------------------------");
                    log.debug("EXPEDIENTE = "+lListaExpedientesSeleccionados[j]);
                    log.debug("CODIGO TRAMITE = "+codTram);
                    log.debug("OcurrenciaTramite = "+tAUX.getOcurrenciaTramite());
                    log.debug("------------------------------------------------------------");
                    log.debug("------------------------------------------------------------");
                    String transformarListaTramitesIniciar = tAUX.getCodUnidadTramitadoraTram();

                    TramitacionExpedientesValueObject traExpVO = new TramitacionExpedientesValueObject();
                    traExpVO.setListaEMailsAlIniciar(new Vector());
                    traExpVO.setListaEMailsAlFinalizar(new Vector());
                    traExpVO.setCodUsuario(Integer.toString(usuario.getIdUsuario()));
                    traExpVO.setCodIdiomaUsuario(usuario.getIdioma());
                    traExpVO.setListaTramitesIniciar(transformarListaTramitesIniciar(listaTramites,transformarListaTramitesIniciar));
                    traExpVO.setCodMunicipio(codOrgCad);
                    traExpVO.setCodOrganizacion(codOrgCad);
                    traExpVO.setCodProcedimiento(codProc);
                    traExpVO.setEjercicio(ejercicio);
                    traExpVO.setNumeroExpediente(lListaExpedientesSeleccionados[j]);
                    traExpVO.setNumero(lListaExpedientesSeleccionados[j]);
                    traExpVO.setCodTramite(codTram);
                    traExpVO.setOcurrenciaTramite(tAUX.getOcurrenciaTramite());// ocurrencia
                    traExpVO.setCodUnidadTramitadoraTram(tAUX.getCodUnidadTramitadoraTram()); // Unidad tramitadora manual
                    traExpVO.setCodEntidad(String.valueOf(usuario.getEntCod()));
                    
                    //Leire 25/10/2014
                    //devolver= null;
                    try 
                    {
                        devolver = ExpedienteImpresionManager.getInstance().finalizarTramitesImpresion(traExpVO, params);
                           //devolver = TramitacionExpedientesDAO.getInstance().finalizarConTramites(traExpVO, null, null, params);
                    } 
                    catch (EjecucionSWException e) 
                    {
                        throw new java.rmi.RemoteException(e.getMensaje(), e);
                    }
                    if (devolver == null) 
                    {
                        log.info("Tramite no grabado");
                        resultado = 0;
                        return resultado;

                    } 
                    else 
                    {
                        log.info("Tramite grabado");
                        resultado = 1;
                    }
                }//cierre for, cada expediente seleccionado
            } 
            else 
            {//no hay tramites sgtes
                resultado = 0;
            }

        } 
        catch (SQLException ex) 
        {
            log.error(ex.getMessage());
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);
            }
            catch(BDException e)
            {
                log.error("ERROR AL DEVOLVER LA CONEXIÓN A LA BBDD: " + e.getMessage());
            }
        }
        return resultado;
    }
    
    private Vector<TramitacionExpedientesValueObject> transformarListaTramitesIniciar(Vector<TramitacionExpedientesValueObject> listaTramitesIniciar,String codUnidadTramitadora)
    {
        for(int i=0;listaTramitesIniciar!=null && i<listaTramitesIniciar.size();i++)
        {
            listaTramitesIniciar.get(i).setCodUnidadTramitadoraTram(codUnidadTramitadora);
        }
    
        return listaTramitesIniciar;
    }
    
    public String generarClaveRegistral(int codOrganizacion, String numExpediente) throws Exception
    {
        String claveGenerada = null;
        String nombreModulo = ConstantesMeLanbide03.NOMBRE_MODULO_GENERADOR_CLAVE_REGISTRAL;
        String tipo = "";
        String codTramite = "0";
        String ocuTramite = "0";
        String operacion = "generarClaveAPA";

        ModuloIntegracionExternoFactoria factoria = ModuloIntegracionExternoFactoria.getInstance();
        ModuloIntegracionExterno clase = factoria.getImplClass(codOrganizacion, nombreModulo);

        if(clase!=null){
            clase.setNombreModulo(nombreModulo);
            if(tipo!=null && "0".equals(tipo)){
                // Si se procesa un formulario que se carga en la vista de expediente, no hay trámite ni ocurrencia.
                codTramite = "-1";
                ocuTramite = "-1";
            }

            Class[] tipoParametros      = null;
            Object[] valoresParametros = null;

            Class[] tipoParametrosPantallaExpTramite = {int.class,int.class,int.class,String.class};
            // La petición procede de una pantalla a nivel de ficha de expediente o // de ficha de trámite
            Object[] valoresParametros2 = {codOrganizacion,Integer.parseInt(codTramite),Integer.parseInt(ocuTramite),numExpediente};
            valoresParametros = valoresParametros2;
            tipoParametros = tipoParametrosPantallaExpTramite;

            claveGenerada = (String)factoria.ejecutarMetodo(clase, operacion, tipoParametros, valoresParametros);
        }
        return claveGenerada;
    }
    public int inserta_MELANBIDE03_EXP_IMPR_CEPAP(String nombreFichero, String[] lListaExpedientesSeleccionados, AdaptadorSQLBD adapt) throws BDException
/*     */   {
/* 463 */     PreparedStatement ps = null;
/* 464 */     Connection con = null;
/* 465 */     //AdaptadorSQLBD adapt = null;
/* 466 */     //adapt = new AdaptadorSQLBD(params);
/* 467 */     int resultado = 0;
/* 468 */     int resultado2 = 0;
/* 469 */     String sql = "";
/* 470 */     String sql1 = "";
/*     */     try
/*     */     {
/* 473 */       con = adapt.getConnection();
/* 474 */       adapt.inicioTransaccion(con);
/*     */       log.info("En MELANBIDE03_EXP_IMPR_APA");
/* 476 */       for (int i = 0; i < lListaExpedientesSeleccionados.length; i++) {
    
/*     */         log.info("Num exp: "+lListaExpedientesSeleccionados.length+" Expediente: " + lListaExpedientesSeleccionados[i]);
/* 477 */         sql = "INSERT INTO MELANBIDE03_EXP_IMPR_APA (NOMBRE_FICHERO, NUM_EXPEDIENTE) VALUES ('" + nombreFichero + "','" + lListaExpedientesSeleccionados[i] + "')";
/*     */ 
/* 479 */         log.debug("MELANBIDE03_EXP_IMPR_APA:" + sql);
/* 480 */         ps = con.prepareStatement(sql);
/* 481 */         resultado = ps.executeUpdate();
/*     */       }
/*     */ 
/* 499 */       adapt.finTransaccion(con);
/*     */     }
/*     */     catch (Exception e) {
                log.error("Error en inserta_MELANBIDE03_EXP_IMPR_CEPAP: "+e);
/* 502 */       adapt.rollBack(con);
/* 503 */       resultado = 0;
/* 504 */       //sqle.printStackTrace();
/*     */     }
/*     */     finally {
/*     */       try {
/* 510 */         adapt.devolverConexion(con);
/*     */       } catch (BDException e) {
/* 512 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */ 
/* 516 */     return resultado;
/*     */   }
    
    
    private int inserta_MELANBIDE03_IMPRESION_CEPAP(String nombreFichero, byte[] fileContent, AdaptadorSQLBD adapt)
/*     */     throws Exception
/*     */   {
/* 522 */     Connection con = null;
/* 525 */     int resultado = 0;
/* 526 */     String sql = "";
/* 527 */     //java.util.Date fecha = c.getTime();
/*     */     try
/*     */     {
/* 530 */       con = adapt.getConnection();
/* 531 */       adapt.inicioTransaccion(con);
/* 532 */       sql = "INSERT INTO MELANBIDE03_IMPRESION_APA (NOMBRE_FICHERO,  CONTENIDO)VALUES (?,?)";
/*     */ 
/* 534 */       PreparedStatement stmt = con.prepareStatement(sql);
/* 535 */       stmt.setString(1, nombreFichero);
/* 536 */       //java.sql.Date sqlDate = new java.sql.Date(fecha.getTime());
/* 537 */       //stmt.setDate(2, sqlDate);
/* 538 */       InputStream st = new ByteArrayInputStream(fileContent);
/* 539 */       stmt.setBinaryStream(2, st, fileContent.length);
/*     */ 
/* 541 */       if (log.isDebugEnabled()) {
/* 542 */         log.debug(sql);
/*     */       }
/* 544 */       resultado = stmt.executeUpdate();
/* 545 */       stmt.close();
/* 546 */       adapt.finTransaccion(con);
/*     */     }
/*     */     catch (Exception e) {
/* 549 */       adapt.rollBack(con);
/* 550 */       resultado = 0;
/* 551 */       //sqle.printStackTrace();
/*     */     }
/*     */     finally {
/*     */       try {
/* 557 */         adapt.devolverConexion(con);
/*     */       } catch (BDException e) {
/* 559 */         e.printStackTrace();
/*     */       }
/*     */     }
/* 562 */     return resultado;
/*     */   }
    
    //Lista de expedientes con relación con EMPNL
    public void listadoRelaciones(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){        

        try{            
            log.debug("En funcion listadoRelaciones ");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            HSSFWorkbook libro = new HSSFWorkbook();
            HSSFFont negrita = libro.createFont();
            negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFSheet hoja = null;
            int numFila = 0;
            int totalesAdjudicadas = 0;
            boolean adjudicada = false;
            String rutaArchivoSalida = null;
            ResourceBundle m_Conf = ResourceBundle.getBundle("common");
            Date control = new Date();
            
            ArrayList<Melanbide03RelacionExpVO> datos = MeLanbide03Manager.getInstance().getRelacionExp(adapt);
            if(datos != null)
            {                
                hoja = libro.createSheet("RELACION_EXPEDIENTES");
                hoja.setColumnWidth(0, 3000);
                hoja.setColumnWidth(1, 3000);
                hoja.setColumnWidth(2, 3000);
                hoja.setColumnWidth(3, 3000);
                hoja.setColumnWidth(4, 7000);
                hoja.setColumnWidth(5, 3000);
                hoja.setColumnWidth(6, 7000);
                HSSFRow fila = hoja.createRow(numFila);
                
                //Cabeceras
                HSSFCellStyle estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                //HSSFFont negrita = libro.createFont();
                //negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                //estiloCelda.setFont(negrita);
                HSSFCell celda = fila.createCell(0);
                celda.setCellValue("NUM EXP");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(1);
                celda.setCellValue("NUM EXP_REL");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(2);
                celda.setCellValue("FECHA SOLICITUD");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(3);
                celda.setCellValue("NIF");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(4);
                celda.setCellValue("NOMBRE");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(5);
                celda.setCellValue("COD CP");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCelda.setWrapText(true);
                celda = fila.createCell(6);
                celda.setCellValue("CP");
                celda.setCellStyle(estiloCelda);
                numFila ++;
                    //HSSFRow fila = hoja.createRow(numFila);
                for(Melanbide03RelacionExpVO rel: datos){
                    fila = hoja.createRow(numFila);
                    estiloCelda = libro.createCellStyle();
                    //estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
                    //estiloCelda.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
                    estiloCelda.setWrapText(true);
                    //HSSFFont negrita = libro.createFont();
                    //negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    //estiloCelda.setFont(negrita);
                    celda = fila.createCell(0);
                    celda.setCellValue(rel.getCodExpediente() != null ? rel.getCodExpediente() : "");
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    celda = fila.createCell(1);
                    celda.setCellValue(rel.getCodExpedienteRel()!= null ? rel.getCodExpedienteRel() : "");
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    celda = fila.createCell(2);
                    celda.setCellValue(rel.getFechaSoli()!= null ? rel.getFechaSoli() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    celda = fila.createCell(3);
                    celda.setCellValue(rel.getNif()!= null ? rel.getNif() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    celda = fila.createCell(4);
                    celda.setCellValue(rel.getNombreApe()!= null ? rel.getNombreApe() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    celda = fila.createCell(5);
                    celda.setCellValue(rel.getCodigoCP()!= null ? rel.getCodigoCP() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    celda = fila.createCell(6);
                    celda.setCellValue(rel.getDescCP()!= null ? rel.getDescCP(): "");
                    celda.setCellStyle(estiloCelda);
                    numFila ++;
                }
                    File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                    File informe = File.createTempFile("resolucionCentros", ".xls", directorioTemp);

                    FileOutputStream archivoSalida = new FileOutputStream(informe);
                    libro.write(archivoSalida);
                    archivoSalida.close();

                    rutaArchivoSalida = informe.getAbsolutePath();

                    FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                    BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

                    int size = (int) informe.length(); 
                    byte[] data = new byte[size]; 
                    bstr.read( data, 0, size ); 
                    bstr.close();

                    response.setContentType("application/vnd.ms-excel");
                    response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                    response.setHeader("Content-Transfer-Encoding", "binary");  
                    response.setContentLength(data.length);
                    response.getOutputStream().write(data, 0, data.length);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();
            }            
        }
        catch(Exception ex){
            log.debug("Error en listadoRelaciones: " + ex);
        }
    }
        
    public void imprimirEtiquetasAPA (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        String fichero = request.getParameter("nombreFichero");
        log.error("Fichero a visualizar: " + fichero);
        try
        {
            Map parameters = new HashMap();
            ArrayList<String> expedientes = MeLanbide03Manager.getInstance().getExpediente(fichero, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List elementos = new ArrayList();
            List listaElementos = new ArrayList();List listaElementos2 = new ArrayList();
            for(String exp: expedientes)
            {
                
                SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new  Locale("ES"));
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                Date hoy = new Date();
                String fechaHoy = hoy.toString();
                fechaHoy = format.format(hoy);
                String fechaEus = format2.format(hoy);
                fechaEus = DateOperations.traducirFechaEuskera(fechaEus);
                //lleno el arraylist elementos con todos los participantes
                ArrayList<Participantes> parti = MeLanbide03Manager.getInstance().getParticipantes(exp, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                for(Participantes par: parti)
                {
//                    if(par.getApe1() == null) par.setApe1("");
//                    if(par.getApe2() == null) par.setApe2("");
                    if(par.getCalle() == null) par.setCalle("");
                    if(par.getNum() == null) par.setNum("");
                    if(par.getMuni() == null) par.setMuni("");
                    if(par.getProv() == null) par.setProv("");
                    if(par.getPlanta() == null) par.setPlanta("");
                    if(par.getPuerta() == null) par.setPuerta("");
                    if(par.getCodPostal() == null) par.setCodPostal("");
//                    String nombreC = par.getNombre() + " " + par.getApe1() + " " + par.getApe2();
                    String nombreC = par.getNomC();
                    String direccion =  par.getCalle();
                    if(!par.getNum().equals(""))
                        direccion += ", "+ par.getNum();
                    if(!par.getPlanta().equals(""))
                        direccion += " - " + par.getPlanta(); 
                    if(!par.getPuerta().equals(""))
                        direccion += " - " + par.getPuerta();
                    String direccion2 =  par.getCodPostal()+ "   "+ par.getMuni() ;
                    String codPostal = par.getProv();

                    HashMap elemento = new HashMap();
                    elemento.put("nombreC", nombreC != null ? nombreC : "");
                    elemento.put("direccion", direccion);
                    elemento.put("direccion2", direccion2);
                    elemento.put("codPostal", codPostal);
                    elemento.put("fechaCas", fechaHoy);
                    elemento.put("fechaEus", fechaEus);
                    elementos.add(elemento);  
                }
            }
            log.error("Participantes recogidos");
            //recorro elementos y los separo entre pares e impares para que quede correctamente
//            for(int i = 0; i< elementos.size(); i = i+2){
//                listaElementos2.add(elementos.get(i));
//            }
            for(int i = 0; i< elementos.size(); i++)
            {                            
                listaElementos.add(elementos.get(i));
            }
            parameters.put("listaElemento2", listaElementos);
//            parameters.put("listaElemento2", listaElementos2);
            List<Subreport> subreportList = new ArrayList<Subreport>();
            Subreport subreport = null;
            String xmlReport = MeLanbide03Manager.getInstance().generarXML("");
            subreport = MeLanbide03Manager.inicializarSubreport("report_main_oficios", xmlReport, "", parameters);
            subreportList.add(subreport);
            byte[] pdf = null;
            pdf = MeLanbide03Manager.getInstance().runMasterReportWithSubreports("report_main_oficios", parameters, subreportList, "PDF");

            log.error("pdf creado");
            if(pdf != null && pdf.length > 0)
            {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "attachment;filename=oficios.pdf");
                response.setContentLength(pdf.length);
                response.getOutputStream().write(pdf, 0, pdf.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();

            }
        }
        catch(Exception ex)
        {
            log.error("Error imprimiendo etiquetas: " + ex.toString());
        }
    }
    public String comprobarUCAcreditadas (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente){
        if(log.isDebugEnabled()) log.debug("******** comprobarUCAcreditadas() : BEGIN");
        String resultado = ConstantesMeLanbide03.CUCA_OPERACION_CORRECTA;
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;
        try{
            con = adapt.getConnection();
            //buscamos el tipo de acreditación del expediente
            String tipoAcreditacion = MeLanbide03Manager.getInstance().getTipoAcreditacion(codOrganizacion, numExpediente,con);
            if (ConstantesMeLanbide03.TIPO_ACREDITACION_APA.equals(tipoAcreditacion)){
                //comprobamos que tenga alguna UC acreditada
                if(!MeLanbide03CertCentroDAO.getInstance().tieneUnidadesAcreditadasExpediente(String.valueOf(codOrganizacion), numExpediente,con)){
                    resultado = ConstantesMeLanbide03.CUCA_ERROR_SINUCACREDITADA;
                }
            }
        }
        catch(Exception ex){
            log.error("Error al comprobar UC Acreditadas en el expediente " + numExpediente + ": " + ex.getMessage());            
            resultado = ConstantesMeLanbide03.CUCA_ERROR;
        }
        finally{
            try{
                adapt.devolverConexion(con);       
            }
            catch(Exception e){
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        
        if(log.isDebugEnabled()) log.debug("comprobarUCAcreditadas() : END");
        return resultado;
    }

    public String comprobarMPUCAcreditadas (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente){
        if(log.isDebugEnabled()) log.debug("******** comprobarUCMPAcreditadas() : BEGIN");
        String resultado = ConstantesMeLanbide03.CUCMPA_OPERACION_CORRECTA;
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        try{
            con = adapt.getConnection();
            //buscamos el tipo de acreditación del expediente
            String tipoAcreditacion = meLanbide03Manager.getTipoAcreditacion(codOrganizacion, numExpediente,con);
            if (ConstantesMeLanbide03.TIPO_ACREDITACION_CP.equals(tipoAcreditacion)){
                //Recupero el certificado del expediente para buscar posteriormente las unidades del mismo
                CerCertificadoVO certificado = null;
                try{
                    certificado = meLanbide03Manager.getCertificadoExpediente(numExpediente, codOrganizacion, con);
                } catch (MELANBIDE03Exception ex) {
                    log.error("Se ha producido un error recuperando la informacion de certificado: " + ex.getMessage());
                    resultado = ConstantesMeLanbide03.CUCMPA_ERROR_CERTIFICADO;
                }
        
                if(certificado!=null && certificado.getCodCertificado()!=null && !"".equals(certificado.getCodCertificado())){
                    //recuperamos la lista de unidades del certificado y si están acreditadas o no tanto en el propio expediente como en otros expedientes del tercero
                    ArrayList<CerUnidadeCompetencialVO> listaUnidades = new ArrayList<CerUnidadeCompetencialVO>();
                    try {
                        listaUnidades = getUnidadesCompetenciales(numExpediente, certificado, String.valueOf(codOrganizacion), con);
                    } catch (MELANBIDE03Exception ex) {
                        log.error("Se ha producido un error recuperando la informacion de las unidades: " + ex.getMessage());
                        resultado = ConstantesMeLanbide03.CUCMPA_ERROR_UNIDADES;
                    }
                    //recorro la lista de unidades para comprobar que estén todas acreditadas
                    boolean todasUCAcreditadas = true;
                    if (listaUnidades!=null && !listaUnidades.isEmpty()) {
                        Iterator i = listaUnidades.iterator();
                        while (todasUCAcreditadas && i.hasNext()) 
                        {
                            CerUnidadeCompetencialVO unidad = (CerUnidadeCompetencialVO)i.next();
                            if(!(ConstantesMeLanbide03.CENTRO_ACREDITADO_SI.toString()).equals(unidad.getCentroAcreditado())){
                                todasUCAcreditadas = false;
                            }
                        }
                    }
                    if(todasUCAcreditadas){
                        //comprobamos que el módulo de prácticas esté acreditado y tenga motivo de acreditación (los CP viejos no tienen módulo de prácticas y no se hará esta comprobación)
                        CerModuloFormativoVO modulo = meLanbide03Manager.getModuloPracticas(numExpediente, codOrganizacion, con);
                        //hay CPs viejos que no tienen módulo. En este caso no hacemos ninguna comprobación
                        if(modulo!=null && modulo.getCodModulo()!=null && !"".equals(modulo.getCodModulo())){
                            //si estamos en un CP con módulo éste tiene que estar acreditado y tener motivo de acreditación (esto último ya se comprueba cuando guarda los datos de la acreditación del módulo en la jsp así que no se controla aquí)
                            if(ConstantesMeLanbide03.MODULO_ACREDITADO_SI!=modulo.getModuloAcreditado()){
                                log.debug("Encontrado MP no acreditado");
                                resultado = ConstantesMeLanbide03.CUCMPA_ERROR_MPNOACREDITADA;
                            }
                        }
                    }
                    else{
                        log.debug("Encontrada UC no acreditada");
                        resultado = ConstantesMeLanbide03.CUCMPA_ERROR_UCNOACREDITADA;
                    }
                }
                else{
                    log.debug("Encontrada UC no acreditada");
                    resultado = ConstantesMeLanbide03.CUCMPA_ERROR_SINCERTIFICADO;
                }
            }
        }
        catch(Exception ex){
            log.error("Error al comprobar UC y MP Acreditados en el expediente " + numExpediente + ": " + ex.getMessage());            
            resultado = ConstantesMeLanbide03.CUCMPA_ERROR;
        }
        finally{
            try{
                adapt.devolverConexion(con);       
            }
            catch(Exception e){
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        
        if(log.isDebugEnabled()) log.debug("comprobarUCMPAcreditadas() : END");
        return resultado;
    }
    
    public void comprobarDatosAvanzarToEsperaExped (int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response) {
        if(log.isDebugEnabled()) log.error("comprobarDatosAvanzarToEsperaExped( numExpediente = " + numExpediente + " ) : BEGIN");
        String codigoOperacion ="0";
        String mensajeOperacion ="OK";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        // Recuperamos el tipo de Expediente Desde BBDD
        String ejercicioExp = "";
        String procedimiExp="";
        String tipoAcreditaExpt = "";
        try{
            if(numExpediente!= null){
                String[] datosExp = numExpediente.split(BARRA);
                ejercicioExp = datosExp[0];
                procedimiExp = datosExp[1];
            }
            if("".equals(ejercicioExp)||"".equals(procedimiExp)){
                log.error("No se ha podido recuperar correctamente el ejecicio o procedimiento del numero de expediente " + numExpediente);
                if("".equals(ejercicioExp)){
                    codigoOperacion="1";
                    mensajeOperacion="Ejercicio no recuperado correctamente del numero de expediente : " + numExpediente;
                }
                if("".equals(procedimiExp)){
                    codigoOperacion="2";
                    mensajeOperacion="Codigo de Procedimiento no recuperado correctamente del numero de expediente : " + numExpediente;
                }
            }else{
                String codCampoSuplTipoAcred = ConfigurationParameter.getParameter(ConstantesMeLanbide03.CAMPO_SUPL_TIPOACREDITACION, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                String tipCampoSuplTipoAcred = ConfigurationParameter.getParameter(ConstantesMeLanbide03.TIPO_CAMPO_SUPL_TIPOACREDITACION, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                SalidaIntegracionVO tipoAcreditacion = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion),ejercicioExp, numExpediente, procedimiExp,codCampoSuplTipoAcred,Integer.valueOf(tipCampoSuplTipoAcred));
                if(tipoAcreditacion!=null && tipoAcreditacion.getStatus()==0){
                    CampoSuplementarioModuloIntegracionVO campoDesp = tipoAcreditacion.getCampoSuplementario();
                    tipoAcreditaExpt = campoDesp.getValorDesplegable();
                    log.error("Tipo Acreditacion Recuperada BBDD " + tipoAcreditaExpt + " Expediente " + numExpediente);
                }
                if("".equals(tipoAcreditaExpt)){
                    codigoOperacion="3";
                    mensajeOperacion="No hay datos guardados sobre el tipo de Acreditación APA/CP. Guarde los datos del Expediente " + numExpediente + " antes de Avanzar. ";
                }
                //Si se ha validado OK hasta aqui continuamos para validar datos de UC guardaos en BBDD
                if("0".equals(codigoOperacion)){
                    String resultValDatUCGuardaBD = "";
                    if(tipoAcreditaExpt.equals(ConstantesMeLanbide03.TIPO_ACREDITACION_CP)){
                        log.error("Vamos a comprobar UC guardadas expediente tipo CP ");
                        resultValDatUCGuardaBD = comprobarMPUCAcreditadas(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente);
                    }
                    else if(tipoAcreditaExpt.equals(ConstantesMeLanbide03.TIPO_ACREDITACION_APA)){
                        log.error("Vamos a comprobar UC guardadas expediente tipo APA ");
                        resultValDatUCGuardaBD = comprobarUCAcreditadas(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente);
                    }else{
                        codigoOperacion="4";
                        mensajeOperacion="Tipo de Acreditación No Recuperado correctamente. No se corresponde con literales APA / CP : " + tipoAcreditaExpt;
                    }
                    log.error("RESULTADO Comprobacion  " +resultValDatUCGuardaBD+ " Expediente" + numExpediente);
                    if(!"0".equals(resultValDatUCGuardaBD)){
                        // hecemos esta comprobación por si se ha rellenado con error 4
                        if("0".equals(codigoOperacion)){ 
                        codigoOperacion="5";
                        mensajeOperacion="No se han guardado los datos sobre las UC acreditadas y/o Modulo Prácticas Acreditado del Expediente " + numExpediente + ". Guarde los datos Acreditando las UC/Modulos Practicas Necesarios,  antes de Avanzar ";
                        }
                    }
                }
                 //Si se ha validado OK hasta aqui continuamos para validar que este en un trámite anterior a 05 - Espera de expedición CP/
                if("0".equals(codigoOperacion)){
                    String codVisiTramitPermitidos = ConfigurationParameter.getParameter(ConstantesMeLanbide03.COD_VISI_TRAMITES_PERMITIR_AVANZAR, ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                    if(!"".equals(codVisiTramitPermitidos)){
                        AdaptadorSQLBD adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
                        ArrayList<String> listaCodVisiTramitesActivos = meLanbide03Manager.getCodigoVisibleTramitesActivosExpt(codOrganizacion, numExpediente, adapt);
                        if (listaCodVisiTramitesActivos != null && listaCodVisiTramitesActivos.size() > 0) {
                            String[] listaPer = codVisiTramitPermitidos.split(DOT_COMMA);
                            List<String> listaPermitida = new ArrayList<String>(Arrays.asList(listaPer));
                            boolean compruebaTramit=false;
                            for(String codVisbleAct : listaCodVisiTramitesActivos){
                                if(listaPermitida.contains(codVisbleAct)){
                                    compruebaTramit = true;
                                    break;
                                }
                            }
                            if(!compruebaTramit){
                                codigoOperacion = "6";
                                mensajeOperacion = "El Expediente " + numExpediente + " no se puede avanzar. No se encuentra en un trámite anterior a la Expedicion de CP o APA / o en un trámite configurado en MELANBIDE03.properties para avanzar.";
                            }  
                        } else {
                            codigoOperacion = "7";
                            mensajeOperacion = "No se ha podido obtener la lista de trámites Activos del Expediente." + numExpediente + ". Consulte con el equipo de Soporte";
                        }
                    }else{
                        codigoOperacion = "8";
                        mensajeOperacion = "No se ha podido obtener del fichero de configuración la lista de trámites permitidos para avanzar el Expediente." + numExpediente + ". Consulte con el equipo de Soporte";
                    }
                    
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error validando los datos antes de avanzar el expediente", ex);
            codigoOperacion = "8";
            mensajeOperacion="Error general al validar los datos para Avanzar el expediente " + numExpediente + ". Detalles :  " + ex.getMessage()!=null&&!"".equals(ex.getMessage())?ex.getMessage():ex.toString();
        }//try-catch
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<MENSAJE_OPERACION>");
                xmlSalida.append(mensajeOperacion);
            xmlSalida.append("</MENSAJE_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("Error al preparar el response de ajax comprobarDatosAvanzarToEsperaExped", e);
        }//try-catch
        if(log.isDebugEnabled()) log.error("comprobarDatosAvanzarToEsperaExped() : END");
    }//comprobarDatosAvanzarToEsperaExped
    
    public void avanzarExpedienteToEsperaExpedicion (int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response) {
        if(log.isDebugEnabled()) log.error("avanzarExpedienteToEsperaExpedicion( numExpediente = " + numExpediente + " ) : BEGIN");
        String codigoOperacion ="0";
        String mensajeOperacion ="Expediente Avanzado Correctamente";
        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
        try{
            AdaptadorSQLBD adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            Map<String,String> resultado = meLanbide03Manager.avanzarExpedienteToEsperaExpedicion(numExpediente,codOrganizacion,adapt);
            codigoOperacion=resultado.get("codigoOperacion");
            mensajeOperacion=resultado.get("mensajeOperacion");
        } catch (Exception ex) {
            log.error("Se ha producido un error general al Avanzar expedientes a Impresion ", ex);
            codigoOperacion = "****";
            mensajeOperacion="Error general al Avanzar el expediente " + numExpediente + ". Detalles :  " + ex.getMessage()!=null&&!"".equals(ex.getMessage())?ex.getMessage():ex.toString();;
        }//try-catch
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<MENSAJE_OPERACION>");
                xmlSalida.append(mensajeOperacion);
            xmlSalida.append("</MENSAJE_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error("Erro al preparar el response de ajax avanzarExpedienteToEsperaExpedicion", e);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("avanzarExpedienteToEsperaExpedicion() : END");
    }//avanzarExpedienteToEsperaExpedicion
    
    //Cargar Codigo oficina segun el cp del cliente
    public void cargarCodigoUsuario(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)throws Exception{
       //En esta funcion recibimos el expediente y obtenemos el codigo interno del usuario
        Statement st = null;
        ResultSet rs = null;
        String sql;
        String codUusario = "";
        String oficinacp="";
        String codUusarioint="";
        String codigopostal="";
        Connection con =null;
        String subStr="";
        int numero=0;
        try{   
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con =adaptador.getConnection();
            //Obtener el codigo interno de tercero segun el expediente
            //sql="select ext_ter from e_ext where ext_num='"+numExpediente+"'"; 
           // sql="SELECT   CASE WHEN COD_POSTAL IS NULL THEN '' ELSE NVL(LPAD(COD_POSTAL,5,'0'),'') END AS COD_POSTAL, 
            sql ="SELECT  LPAD(COD_POSTAL,5,'0') AS COD_POSTAL, "
                    +" NVL(COD_OFICINA,'') AS COD_OFICINA, NVL(DESCRIP,'') AS DESCRIP "
                     + "FROM E_Ext Inner Join T_Hte On Ext_Ter = Hte_Ter And Ext_Nvr = Hte_Nvr Inner Join T_Dot On Dot_Dom = Ext_Dot And Ext_Ter = Hte_Ter "
            
                    + " INNER JOIN T_DNN ON DOT_DOM  = DNN_DOM "
                    + " LEFT Join Cp_Oficinas On Lpad(Cod_Postal,5,'0') = Dnn_Cpo "
                    //+"INNER JOIN T_DNN ON DOT_DOM = DNN_DOM AND EXT_DOT = DNN_DOM INNER Join Cp_Oficinas On Lpad(Cod_Postal,5,'0') = Dnn_Cpo "
                    +" WHERE EXT_NUM ='"+numExpediente+"'";
             log.error(sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                //ext_ter
                 codigopostal=rs.getString("COD_POSTAL")!=null?rs.getString("COD_POSTAL"):"";
                 codUusario  = rs.getString("DESCRIP")!=null?rs.getString("DESCRIP"):"";
               
                
            }
             log.error("codUsuario: "+codUusario);

        
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando el código de la oficina funcion cargarCodigoUsuario ", e);
            throw new Exception(e);
        }finally{
            try{
                con.close();
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }//try-catch
        }//try-catch-finally
        //01474


         //xml
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
             xmlSalida.append("<CODIGO_POSTAL>");
                xmlSalida.append(codigopostal);
            xmlSalida.append("</CODIGO_POSTAL>");  
            xmlSalida.append("<CODIGO_USUARIO>");
                xmlSalida.append(codUusario);
            xmlSalida.append("</CODIGO_USUARIO>");    
           
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
        if(log.isDebugEnabled()) log.debug("cargarCodigoUsuario() : END");
        //return codUusario;
        
        
        
    } 
    
     public String cargarCriteriosFiltroListadoCP_APA(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        
        try
        {
            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
                
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide03/criteriosListado.jsp";
    }
     
     
     
    public void generarPDF_CPAPA (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response){
            String nombrePlantillaReport="";
            String tipoAcreditacion="";
            String valoracion="";
        try
        {
            MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
            
            String numExp = request.getParameter("numExp");
            valoracion = meLanbide03Manager.getValoracion(numExp, codOrganizacion, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            
            if(valoracion.equals("T") || valoracion.equals("C") ){
                tipoAcreditacion = meLanbide03Manager.getTipoAcreditacion(numExp, codOrganizacion, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(tipoAcreditacion.equals("CP")){
                    nombrePlantillaReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_CERTIFICADO_POSITIVO_CP, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }
                else if(tipoAcreditacion.equals("APA")){
                    nombrePlantillaReport = ConfigurationParameter.getParameter(ConstantesMeLanbide03.NOMBRE_PLANTILLA_REPORT_PDF_CERTIFICADO_POSITIVO_APA, 
                    ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                }
            
            
            Map parameters = new HashMap();
            //ArrayList<String> expedientes = MeLanbide03Manager.getInstance().getExpediente(nombrePlantillaReport, getAdaptSQLBD(String.valueOf(codOrganizacion)));
             
            /*List elementos = new ArrayList();
            List listaElementos = new ArrayList();
            List listaElementos2 = new ArrayList();*/
            
            /*for(String exp: expedientes)
            {*/
                SimpleDateFormat format = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new  Locale("ES"));
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
                Date hoy = new Date();
                String fechaHoy = hoy.toString();
                fechaHoy = format.format(hoy);
                String fechaEus = format2.format(hoy);
                fechaEus = DateOperations.traducirFechaEuskera(fechaEus);
                //lleno el arraylist elementos con todos las certificaciones
                ArrayList<CertificacionPositiva> certi = MeLanbide03Manager.getInstance().getCertificaciones(numExp, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                
                for(CertificacionPositiva cer: certi)
                {
               /*
                exp.setFECHARD(rs.getString(""));
                exp.setDECMODTEXT(rs.getString(""));*/
                    if(cer.getINTERESADO_SOLICITANTE()== null) cer.setINTERESADO_SOLICITANTE("");
                    if(cer.getINTERESADO_DocSOLICITANTE()== null) cer.setINTERESADO_DocSOLICITANTE("");
                    if(cer.getINTERESADO_CodPostalSOLICITAN()== null) cer.setINTERESADO_CodPostalSOLICITAN("");
                    if(cer.getINTERESADO_PobSOLICITANTE()== null) cer.setINTERESADO_PobSOLICITANTE("");
                    if(cer.getINTERESADO_ProvinciaSOLICITAN()== null) cer.setINTERESADO_ProvinciaSOLICITAN("");
                    if(cer.getCODIGOCP() == null) cer.setCODIGOCP("");
                    if(cer.getNIVEL() == null) cer.setNIVEL("");
                    if(cer.getDESCRIPCIONE() == null) cer.setDESCRIPCIONE("");
                    if(cer.getDESCRIPCIONC() == null) cer.setDESCRIPCIONC("");
                    if(cer.getRDFECHAEUSK() == null) cer.setRDFECHAEUSK("");
                    if(cer.getDECMODTEXTEUSK() == null) cer.setDECMODTEXTEUSK("");
                    if(cer.getDECRETO() == null) cer.setDECRETO("");
                    if(cer.getFECHARD() == null) cer.setFECHARD("");
                    if(cer.getDECMODTEXT() == null) cer.setDECMODTEXT("");
                    
                    String nomInteresado=cer.getINTERESADO_SOLICITANTE();
                    String dom=cer.getINTERESADO_DomSOLICITANTE();
                    String cp=cer.getINTERESADO_CodPostalSOLICITAN();
                    String poblacion=cer.getINTERESADO_PobSOLICITANTE();
                    String provincia=cer.getINTERESADO_ProvinciaSOLICITAN();
                    String docInteresado=cer.getINTERESADO_DocSOLICITANTE();
                    String codigoCP = cer.getCODIGOCP();
                    String nivel =  cer.getNIVEL();
                    String descripcione = cer.getDESCRIPCIONE();
                    String descripcionc = cer.getDESCRIPCIONC();
                    String rdfechaeusk = cer.getRDFECHAEUSK();
                    String decmodtexteusk = cer.getDECMODTEXTEUSK();
                    String decreto = cer.getDECRETO();
                    String fechard = cer.getFECHARD();
                    String decmodtext = cer.getDECMODTEXT();

                    /*HashMap elemento = new HashMap();
                    elemento.put("INTERESADO_SOLICITANTE", "pepe");
                    elemento.put("INTERESADO_DomSOLICITANTE", "INTERESADO_DomSOLICITANTE");
                    elemento.put("INTERESADO_CodPostalSOLICITAN", "01009");
                    elemento.put("INTERESADO_PobSOLICITANTE", "Vitoria");
                    elemento.put("INTERESADO_ProvinciaSOLICITAN", "Alava"); 
                    elemento.put("FECACTESP", fechaHoy);
                    elemento.put("FECACTEU", fechaEus);
                    elemento.put("CODIGOCP", codigoCP);
                    elemento.put("NIVEL", nivel);
                    elemento.put("DESCRIPCIONE", descripcione);
                    elemento.put("DESCRIPCIONC", descripcionc);
                    elemento.put("RDFECHAEUSK", rdfechaeusk);
                    elemento.put("DECMODTEXTEUSK", decmodtexteusk);
                    elemento.put("DECRETO", decreto);
                    elemento.put("FECHARD", fechard);
                    elemento.put("DECMODTEXT", decmodtext);
                    elementos.add(elemento);*/
                    
                    parameters.put("INTERESADO_SOLICITANTE", nomInteresado);
                    parameters.put("INTERESADO_DomSOLICITANTE", dom);
                    parameters.put("INTERESADO_DocSOLICITANTE", docInteresado);
                    parameters.put("INTERESADO_CodPostalSOLICITAN", cp);
                    parameters.put("INTERESADO_PobSOLICITANTE", poblacion);
                    parameters.put("INTERESADO_ProvinciaSOLICITAN", provincia); 
                    parameters.put("FECACTESP", fechaHoy);
                    parameters.put("FECACTEU", fechaEus);
                    
                    parameters.put("CODIGOCP", codigoCP);
                    parameters.put("NIVEL", nivel);
                    parameters.put("DESCRIPCIONE", descripcione);
                    parameters.put("DESCRIPCIONC", descripcionc);
                    parameters.put("RDFECHAEUSK", rdfechaeusk);
                    parameters.put("DECMODTEXTEUSK", decmodtexteusk);
                    parameters.put("DECRETO", decreto);
                    parameters.put("FECHARD", fechard);
                    parameters.put("DECMODTEXT", decmodtext);
                    parameters.put("NUMEXPEDIENTE", numExp);
                    if(tipoAcreditacion.equals("APA")){                  
                        ArrayList<ArrayList> listaAcreditadas3 = MeLanbide03Manager.getInstance().getListaAcreditadas3(numExp, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        ArrayList<String> listaAcreditadas3Cast = new ArrayList<String>();
                        ArrayList<String> listaAcreditadas3Eusk = new ArrayList<String>();
                        listaAcreditadas3Cast=listaAcreditadas3.get(0);
                        listaAcreditadas3Eusk=listaAcreditadas3.get(1);
                        parameters.put("LISTAACREDITADAS3CAST", listaAcreditadas3Cast);
                        parameters.put("LISTAACREDITADAS3EUSK", listaAcreditadas3Eusk);
                    }
                }
            //}
            //log.error("Participantes recogidos");
            
           /*for(int i = 0; i< elementos.size(); i++)
            {                            
                listaElementos.add(elementos.get(i));
            }
            parameters.put("listaElemento2", listaElementos);*/
            
            List<Subreport> subreportList = new ArrayList<Subreport>();
            Subreport subreport = null;
            String xmlReport = MeLanbide03Manager.getInstance().generarXML("");
            subreport = MeLanbide03Manager.inicializarSubreport(nombrePlantillaReport, xmlReport, "", parameters);
            subreportList.add(subreport);
            byte[] pdf = null;
            pdf = MeLanbide03Manager.getInstance().runMasterReportWithSubreports(nombrePlantillaReport, parameters, subreportList, "PDF");

            log.error("pdf creado");
            if(pdf != null && pdf.length > 0)
            {
                response.setContentType("application/pdf");
                if(tipoAcreditacion.equals("CP")){
                    response.setHeader("Content-Disposition", "attachment;filename=certificacionPositivaCP.pdf");
                }else{
                    response.setHeader("Content-Disposition", "attachment;filename=certificacionPositivaAPA.pdf");
                }
                
                response.setContentLength(pdf.length);
                response.getOutputStream().write(pdf, 0, pdf.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();

            }
            }
        }
        catch(Exception ex)
        {
            log.error("Error imprimiendo etiquetas: " + ex.toString());
        }
    }


}
    
    
