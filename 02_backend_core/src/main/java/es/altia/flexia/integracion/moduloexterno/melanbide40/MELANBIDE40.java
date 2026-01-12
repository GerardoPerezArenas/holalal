/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide40;


import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.i18n.MeLanbide40I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide40.manager.MeLanbide40Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConstantesMeLanbide40;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CerModuloFormativoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CertificadoMPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.ValoresCalculo;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MELANBIDE40 extends ModuloIntegracionExterno
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE40.class);
    
    private String msgValidacion = "";
    
    public String cargarPantalla(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
       
        String ejercicio = "";
        String mensaje = "";
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        
        MeLanbide40Manager meLanbide40Manager = MeLanbide40Manager.getInstance();
        
        //Cargamos los certificados para rellenar el combo.
        ArrayList<CerCertificadoVO> listaCertificados = new ArrayList<CerCertificadoVO>();
        try {
            listaCertificados = meLanbide40Manager.getCertificados(getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (BDException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE40.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE40.class.getName()).log(Level.SEVERE, null, ex);
        }//try-catch
        //Guardamos los certificados recuperados en la request
        request.setAttribute("listaCertificados", listaCertificados);
       
        
        String cargarDatos = "N";
        CerCertificadoVO certificado = new CerCertificadoVO();
        certificado.setNumExpediente(numExpediente);
        certificado.setCodOrganizacion(codOrganizacion);
        try{
            if(meLanbide40Manager.existeCertificadoExpediente(certificado, getAdaptSQLBD(String.valueOf(codOrganizacion)))){
                certificado = meLanbide40Manager.getCertificadoExpediente(numExpediente, codOrganizacion, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                cargarDatos = "S";
            }//if(meLanbide03Manager.existeCertificadoExpediente(certificado, getAdaptSQLBD(String.valueOf(codOrganizacion))))
        }catch (BDException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE40.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE40.class.getName()).log(Level.SEVERE, null, ex);
        }//try-catch
        
        
        request.setAttribute("mensaje", mensaje);
        request.setAttribute("cargarDatos", cargarDatos);
        request.setAttribute("certificado",certificado);
        
        return "/jsp/extension/melanbide40/melanbide40.jsp";
    }
    
    
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
        MeLanbide40Manager meLanbide40Manager = MeLanbide40Manager.getInstance();
        
        if(log.isDebugEnabled()) log.debug("Recuperamos los parametros de la request");
        String codCertificado = (String) request.getParameter("codCertificado");
        if(log.isDebugEnabled()) log.debug("Certificado seleccionado con código = " + codCertificado);
        CerCertificadoVO certificado = null;
        String[] expedientesCEPAP=null;
        ArrayList<CerModuloFormativoVO> listaExpedientesCEPAP=null;
        String codIdioma = (String) request.getParameter("idioma");
        String codigoOperacion = "70";
        try{
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos del certificado de la tabla CER_CERTIFICADOS");
            certificado = meLanbide40Manager.getCertificado(codCertificado, getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        }//try-catch
        
        try{
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos de expedientes CEPAP de la persona con ese certificado");
            expedientesCEPAP = meLanbide40Manager.getExpedientesCEPAP(codCertificado, numExpediente, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            
            
            listaExpedientesCEPAP = meLanbide40Manager.getInfoExpedientesCEPAP(codOrganizacion,codCertificado, expedientesCEPAP, getAdaptSQLBD(String.valueOf(codOrganizacion)));
            
            
            
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando datos de expedientes CEPAP de la persona con ese certificado de la BBDD", ex);
            codigoOperacion = "71";
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos de expedientes CEPAP de la persona con ese certificado de la BBDD", ex);
            codigoOperacion = "71";
        }//try-catch
        
        if(!codigoOperacion.equalsIgnoreCase("71")){
            if(!codigoOperacion.equalsIgnoreCase("72")){        
                if(!codigoOperacion.equalsIgnoreCase("74")){
                    try{
                        if(certificado.getCodCertificado() != null){
                            certificado.setModulosFormativos(meLanbide40Manager.getModulosFormativos(certificado.getCodCertificado(), 
                                    getAdaptSQLBD(String.valueOf(codOrganizacion))));
                        }//if(certificado.getCodCertificado() != null)
                    }catch (BDException ex) {
                        java.util.logging.Logger.getLogger(MELANBIDE40.class.getName()).log(Level.SEVERE, null, ex);
                        codigoOperacion = "75";
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(MELANBIDE40.class.getName()).log(Level.SEVERE, null, ex);
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
                        xmlSalida.append("</MODULO_FORMATIVO>");
                    }//for(CerModuloFormativoVO modulo : certificado.getModulosFormativos())
                xmlSalida.append("</MODULOS_FORMATIVOS>");
            }//if(certificado.getModulosFormativos().size() > 0)
            
            //expedientes
            /*xmlSalida.append("<EXP_CEPAP>");
            if (expedientesCEPAP!=null)
                for(String expediente : expedientesCEPAP){
                    xmlSalida.append("<EXPEDIENTE>");
                    xmlSalida.append(expediente);
                    xmlSalida.append("</EXPEDIENTE>");
                }*/
            xmlSalida.append("<EXP_CEPAP>");
            if (listaExpedientesCEPAP!=null)
                for(CerModuloFormativoVO infoExpediente : listaExpedientesCEPAP){
                    xmlSalida.append("<EXPEDIENTE>");
                        xmlSalida.append("<NUMEXPEDIENTE>");
                        xmlSalida.append(infoExpediente.getNumExpediente());
                        xmlSalida.append("</NUMEXPEDIENTE>");
                        xmlSalida.append("<ACREDITADO>");
                        xmlSalida.append(infoExpediente.getModuloAcreditado());
                        xmlSalida.append("</ACREDITADO>");
                        xmlSalida.append("<MOTACREDITADO>");
                        xmlSalida.append(infoExpediente.getCodMotivoAcreditacion());
                        xmlSalida.append("</MOTACREDITADO>");
                    xmlSalida.append("</EXPEDIENTE>");
                }
            
            xmlSalida.append("</EXP_CEPAP>");
            
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
    
    
    
    
   
    
    
    
    public void guardar(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    { 
        String mensaje = "";
        String codigoOperacion = "0";
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        
        String codCertificado = (String)request.getParameter("codCertificado");
        String codModPrac = (String)request.getParameter("codModPrac");
        
        CerCertificadoVO certificado=null;
        CerModuloFormativoVO modulo=null;
        
        MeLanbide40Manager meLanbide40Manager = MeLanbide40Manager.getInstance();
        try{
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos del certificado de la tabla CER_CERTIFICADOS");
                certificado = meLanbide40Manager.getCertificado(codCertificado, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                certificado.setNumExpediente(numExpediente);
                certificado.setCodOrganizacion(codOrganizacion);
             
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el certificado de la BBDD", ex);
            codigoOperacion = "71";
        }//try-catch
        
        try{ 
            ArrayList<CerModuloFormativoVO> modulos =meLanbide40Manager.getModulosFormativos(codCertificado,getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (modulos.size()>0)
                modulo=modulos.get(0);           
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando modulos del certificado de la BBDD", ex);
            codigoOperacion = "71";
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando modulos del certificado de la BBDD", ex);
            codigoOperacion = "71";
        }//try-catch
         
         
        try
        {
            
            boolean correcto = MeLanbide40Manager.getInstance().guardarDatos(codOrganizacion, certificado, modulo, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(correcto)
            {
                mensaje = MeLanbide40I18n.getInstance().getMensaje(codIdioma, "msg.datosGuardados");
            }
            else
            {
                mensaje = MeLanbide40I18n.getInstance().getMensaje(codIdioma, "msg.error.datosNoGuardados");
                codigoOperacion = "3";
            }
               
        }
        catch(Exception ex)
        {
            codigoOperacion = "3";
            mensaje = MeLanbide40I18n.getInstance().getMensaje(codIdioma, "error.errorGen");
        }
        
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
        
        
       // escribirRespuesta(valoresCalculo, codigoOperacion, mensaje, request, response);
    }
    
    
    public void acreditar(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    { 
        String mensaje = "";
        String codigoOperacion = "0";
        int codIdioma = 1;
        try
        {
            if(request.getParameter("idioma") != null)
            {
                codIdioma = Integer.parseInt((String)request.getParameter("idioma"));
            }
        }
        catch(Exception ex)
        {

        }
        
        String codCertificado = (String)request.getParameter("codCertificado");
        String modulo = (String)request.getParameter("codModPrac");
        String[] expedientesCEPAP=null;
        MeLanbide40Manager meLanbide40Manager = MeLanbide40Manager.getInstance();
        try{
            if(log.isDebugEnabled()) log.debug("Rellenamos los datos de expedientes CEPAP de la persona con ese certificado");
            expedientesCEPAP = meLanbide40Manager.getExpedientesCEPAP(codCertificado, numExpediente, getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando datos de expedientes CEPAP de la persona con ese certificado de la BBDD", ex);
            codigoOperacion = "71";
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos de expedientes CEPAP de la persona con ese certificado de la BBDD", ex);
            codigoOperacion = "71";
        }//try-catch
        
        //acreditar
        try
        {
            
            boolean correcto = meLanbide40Manager.acreditar(codOrganizacion, expedientesCEPAP,codCertificado,modulo, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(correcto)
            {
                mensaje = MeLanbide40I18n.getInstance().getMensaje(codIdioma, "msg.datosGuardados");
            }
            else
            {
                mensaje = MeLanbide40I18n.getInstance().getMensaje(codIdioma, "msg.error.datosNoGuardados");
                codigoOperacion = "3";
            }
               
        }
        catch(Exception ex)
        {
            codigoOperacion = "3";
            mensaje = MeLanbide40I18n.getInstance().getMensaje(codIdioma, "error.errorGen");
        }
        
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
        
   
    }
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
        if(log.isDebugEnabled()) log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

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
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(conGenerico!=null && !conGenerico.isClosed())
                conGenerico.close();
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
}