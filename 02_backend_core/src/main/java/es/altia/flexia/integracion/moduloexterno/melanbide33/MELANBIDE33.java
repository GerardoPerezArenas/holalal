/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide33;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide33.manager.MeLanbide33Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide33.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide33.util.ConstantesMeLanbide33;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide33.i18n.MeLanbide33I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide33.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide33.vo.FilaMinimisVO;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MELANBIDE33 extends ModuloIntegracionExterno
{
    private static Logger log = LogManager.getLogger(MELANBIDE33.class);
    private final String BARRA                              = "/";
     
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception{
        log.info("cargarExpedienteExtension begin");
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }
     
    public String cargarPantalla(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            String urlConceptos = ConfigurationParameter.getParameter("URL_EXTERNA_CONCEPTOS", ConstantesMeLanbide33.FICHERO_PROPIEDADES);
            String urlETecnico = ConfigurationParameter.getParameter("URL_EXTERNA_ESTUDIO_TECNICO", ConstantesMeLanbide33.FICHERO_PROPIEDADES);
            String urlPagos = ConfigurationParameter.getParameter("URL_EXTERNA_PAGOS", ConstantesMeLanbide33.FICHERO_PROPIEDADES);

            request.setAttribute("urlConceptos", urlConceptos);
            request.setAttribute("urlETecnico", urlETecnico);
            request.setAttribute("urlPagos", urlPagos);

            Integer ejercicio = null;
            String[] datos = numExpediente.split(ConstantesMeLanbide33.BARRA_SEPARADORA);
            ejercicio = Integer.parseInt(datos[0]);
            
            boolean tramEncontrado = false;
            request.setAttribute("tramPagos", false);
            request.setAttribute("tramETecnico", false);
            request.setAttribute("tramConceptos", false);
            
            Long codTramPagos = MeLanbide33Manager.getInstance().obtenerCodigoInternoTramite(codOrganizacion, ConfigurationParameter.getParameter("TRAM_PAGOS", ConstantesMeLanbide33.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(codTramPagos != null)
            {
                if(MeLanbide33Manager.getInstance().tieneTramiteIniciado(codOrganizacion, ejercicio, numExpediente, codTramPagos, this.getAdaptSQLBD(String.valueOf(codOrganizacion))))
                {
                    request.setAttribute("tramPagos", true);
                    tramEncontrado = true;
                }
            }
            
            if(!tramEncontrado)
            {
                Long codTramETecnico = MeLanbide33Manager.getInstance().obtenerCodigoInternoTramite(codOrganizacion, ConfigurationParameter.getParameter("TRAM_ESTUDIO_TECNICO", ConstantesMeLanbide33.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(codTramETecnico != null)
                {
                    if(MeLanbide33Manager.getInstance().tieneTramiteIniciado(codOrganizacion, ejercicio, numExpediente, codTramETecnico, this.getAdaptSQLBD(String.valueOf(codOrganizacion))))
                    {
                        request.setAttribute("tramETecnico", true);
                        tramEncontrado = true;
                    }
                }
            }
            else
            {
                request.setAttribute("tramETecnico", true);
            }
            
            if(!tramEncontrado)
            {
                Long codTramConceptos = MeLanbide33Manager.getInstance().obtenerCodigoInternoTramite(codOrganizacion, ConfigurationParameter.getParameter("TRAM_CONCEPTOS", ConstantesMeLanbide33.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(codTramConceptos != null)
                {
                    if(MeLanbide33Manager.getInstance().tieneTramiteIniciado(codOrganizacion, ejercicio, numExpediente, codTramConceptos, this.getAdaptSQLBD(String.valueOf(codOrganizacion))))
                    {
                        request.setAttribute("tramConceptos", true);
                        tramEncontrado = true;
                    }
                }
            }
            else
            {
                request.setAttribute("tramConceptos", true);
            }
        }
        catch(Exception ex)
        {
            
        }
        return "/jsp/extension/melanbide33/lanzadera.jsp";
    }
public String cargarPantallaSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaSubvenciones de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide33/subSolic.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaMinimisVO> listaMinimis = MeLanbide33Manager.getInstance().getDatosMinimis(numExpediente, codOrganizacion, adapt);
                if (listaMinimis.size() > 0) {
                    for (FilaMinimisVO lm : listaMinimis) {
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                    request.setAttribute("listaMinimis", listaMinimis);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de minimis - MELANBIDE33 - cargarPantallaSubvenciones", ex);
            }
        }
        return url;
    }    
    
    public String eliminarValorSubvencion (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente){
        if(log.isDebugEnabled()) log.debug("eliminarValorSubvencion ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        //IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide33Manager meLanbide33Manager = MeLanbide33Manager.getInstance();
        
        Boolean resultado= false;
        String codigoOperacion = "0";
        String mensaje = "";
        
        try{           
            if(numExpediente!=null && !"".equals(numExpediente)){
                String[] datos          = numExpediente.split(BARRA);
                String ejercicio        = datos[0];
                String codProcedimiento = datos[1];
                //redireccion = es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + this.getNombreModulo() + BARRA + codProcedimiento + PANTALLA_EXPEDIENTE_SALIDA, this.getNombreModulo());
                resultado = MeLanbide33Manager.getInstance().eliminarSubvencion(codOrganizacion,Integer.parseInt(ejercicio),numExpediente,Long.valueOf(codTramite),this.getAdaptSQLBD(String.valueOf(codOrganizacion)));                                
            }            
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            mensaje = MeLanbide33I18n.getInstance().getMensaje(1, "error.errorGen");
        }
        
        if(log.isDebugEnabled()) log.debug("eliminarValorSubvencion() : END");
        return codigoOperacion;
    }
    
    public String actualizarPagos(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente){
         if(log.isDebugEnabled()) log.debug("actualizarPagos ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
         String mensaje = ""; 
         String codigoOperacion = "0";
         int resultado= 0;
         MeLanbide33Manager meLanbide33Manager = MeLanbide33Manager.getInstance();
           try{  
                if(numExpediente!=null && !"".equals(numExpediente)){
                    String[] datos          = numExpediente.split(BARRA);
                    String ejercicio        = datos[0];
                    String codProcedimiento = datos[1];
                    resultado = MeLanbide33Manager.getInstance().actualizarPagos(codOrganizacion,Integer.parseInt(ejercicio),numExpediente,Long.valueOf(ConfigurationParameter.getParameter("TRAM_RESOL", ConstantesMeLanbide33.FICHERO_PROPIEDADES)),this.getAdaptSQLBD(String.valueOf(codOrganizacion)));                                
                }
               }
            catch(Exception ex)
            {
                codigoOperacion = "2";
                mensaje = MeLanbide33I18n.getInstance().getMensaje(1, "error.errorGen");
            }
           
         if(log.isDebugEnabled()) log.debug("actualizarPagos() : END");
          return codigoOperacion;
    }
    
    public String relac2ConvoAnteriores(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente){
        if(log.isDebugEnabled()) log.debug("TrazaControl - relac2ConvoAnteriores ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String mensaje = ""; 
        String codigoOperacion = "0";
        int nroRelacionados= 0; 
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide33Manager meLanbide33Manager = MeLanbide33Manager.getInstance();
        try{  
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            con=adapt.getConnection();
            if (numExpediente != null && !"".equals(numExpediente)) {
              String[] datos = numExpediente.split(BARRA);
              String ejercicio = datos[0];
              //String codProcedimiento = datos[1];
              String documentoInteresado = meLanbide33Manager.getDocumentoInteresado(numExpediente, con);
              if(!"".equals(documentoInteresado)){
                  nroRelacionados = meLanbide33Manager.relacionarAutomDosConvocAnteriores(codOrganizacion, ejercicio, documentoInteresado, numExpediente, con);
                  log.info("relac2ConvoAnteriores - Se han relacionado correctamente " + nroRelacionados + " Convocatorias. Al expediente :  " + numExpediente);
              }else{
                  log.error("relac2ConvoAnteriores - No se ha detectado un interesado en el expediente " +numExpediente+ ". No relacionamos" );
              }
            } else {
                log.error("relac2ConvoAnteriores - No se ha recibido correctamente el Numero de Expediente");
            }
        }catch(Exception ex){
            // solo pintamos trazas no devolvemos errores para que el expediente se cree y no de errores al dar de alta el primer tramite
            log.error("Exception al relacionar las convocatorias anteriores ",ex);
            //codigoOperacion = "2";
            //mensaje = MeLanbide33I18n.getInstance().getMensaje(1, "error.errorGen");
        }finally{
            try{
                if(adapt!=null)
                    adapt.devolverConexion(con);
            }catch (Exception e) {
                // solo pintamos trazas no devolvemos errores para que el expediente se cree y no de errores al dar de alta el primer tramite
             log.error("relac2ConvoAnteriores - Error Cerrando Conexion BD", e);
             //codigoOperacion = "3";
             //mensaje = e.getMessage();
            }
        }
      log.info("TrazaControl - relac2ConvoAnteriores() : END");
      return codigoOperacion;
   }
    
    public String comprobarDatosConv_ConcepGuardados(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente){
        if(log.isDebugEnabled()) log.debug("TrazaControl - comprobarDatosConv_ConcepGuardados ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String mensaje = ""; 
        String codigoOperacion = "0";
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide33Manager meLanbide33Manager = MeLanbide33Manager.getInstance();
        try{  
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            con=adapt.getConnection();
            if (numExpediente != null && !"".equals(numExpediente)) {
              String[] datos = numExpediente.split(BARRA);
              String ejercicio = datos[0];
              String codProcedimiento = datos[1];
              Integer numeroExpInt = Integer.valueOf(datos[2]);
              boolean resultado = meLanbide33Manager.comprobarDatosConv_ConcepGuardadosManager(codOrganizacion,ejercicio,codProcedimiento,numeroExpInt,numExpediente, con);
              if(!resultado){
                  codigoOperacion="DATOS-NO-GUARDADOS"; //"1";
                  mensaje="Datos de Convocatoria, Conceptos ó Puestos no guardados. No se puede avanzar el expediente.";
                  log.error("comprobarDatosConv_ConcepGuardados - NO se han encontrado datos guardados para convocatoria/Canceptos/Puestos del expediente :  " + numExpediente + " No permitimos el avance.");
              }else{
                  log.info("comprobarDatosConv_ConcepGuardados - Si se han guardado datos, dejamos avanzar el expediente : " +numExpediente );
              }
            } else {
                log.error("comprobarDatosConv_ConcepGuardados - No se ha recibido correctamente el Numero de Expediente");
            }
        }catch(Exception ex){
            log.error("Exception al comprobar si se han guardado datos de Convocatoria y Conceptos/Puestos ",ex);
            codigoOperacion = "ERROR-GENERAL";//"2";
            mensaje = MeLanbide33I18n.getInstance().getMensaje(1, "error.errorGen");
        }finally{
            try{
                if(adapt!=null)
                    adapt.devolverConexion(con);
            }catch (Exception e) {
             log.error("comprobarDatosConv_ConcepGuardados - Error Cerrando Conexion BD", e);
             mensaje = e.getMessage();
             codigoOperacion="Error Cerrando Conexion BD : ".concat(mensaje);
            }
        }
        log.info("TrazaControl - comprobarDatosConv_ConcepGuardados() : END");
      return codigoOperacion;
   }
    
    public String guardarTotalPago3Anualidad3Pago(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente){
        if(log.isDebugEnabled()) log.info("TrazaControl - guardarTotalPago3Anualidad3Pago ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codigoOperacion = "0";
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide33Manager meLanbide33Manager = MeLanbide33Manager.getInstance();
        try{  
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            con=adapt.getConnection();
            if (numExpediente != null && !"".equals(numExpediente)) {
              String[] datos = numExpediente.split(BARRA);
              String ejercicio = datos[0];
              String codProcedimiento = datos[1];
              boolean result  = meLanbide33Manager.guardarTotalPago3Anualidad3PagoManager(codOrganizacion, ejercicio, codProcedimiento, numExpediente, codTramite, con);
              if(result)
                  log.info("guardarTotalPago3Anualidad3Pago - Campo Suplementario TOTIMPCON3A3P(TOTAL IMPORTE CONCEDIDO 3 ANUALIDAD 3 PAGO) guardado Correctamente : " + numExpediente);
              else
                  log.error("guardarTotalPago3Anualidad3Pago - No se ha podido guardar el Campo Suplementario TOTIMPCON3A3P(TOTAL IMPORTE CONCEDIDO 3 ANUALIDAD 3 PAGO) - " + numExpediente);
            } else {
                log.error("guardarTotalPago3Anualidad3Pago - No se ha recibido correctamente el Numero de Expediente");
            }
        }catch(Exception ex){
            log.error("Exception al guardar el campo suplementario TOTIMPCON3A3P(TOTAL IMPORTE CONCEDIDO 3 ANUALIDAD 3 PAGO) ",ex);
            codigoOperacion = "1";
        }finally{
            try{
                if(adapt!=null)
                    adapt.devolverConexion(con);
            }catch (Exception e) {
             log.error("guardarTotalPago3Anualidad3Pago - Error Cerrando Conexion BD", e);
             codigoOperacion = "2";
            }
        }
      log.error("TrazaControl - guardarTotalPago3Anualidad3Pago() : END");
      return codigoOperacion;
   }
    
   public String validarPasoaET_SEI(int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente){
        if(log.isDebugEnabled()) log.info("TrazaControl - validarPasoaET_SEI ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codigoOperacion = "0";
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide33Manager meLanbide33Manager = MeLanbide33Manager.getInstance();
        try{  
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            con=adapt.getConnection();
            if (numExpediente != null && !"".equals(numExpediente)) {
              String[] datos = numExpediente.split(BARRA);
              String ejercicio = datos[0];
              String codProcedimiento = datos[1];
              boolean result  = meLanbide33Manager.validarPasoaET_SEI_Manager(codOrganizacion, ejercicio, codProcedimiento, numExpediente, codTramite, con);
              if(result)
                  log.info("validarPasoaET_SEI - Campo Suplementario PASOETSEI(Comprobación paso a ET procedimiento SEI 0(Negativo) 1(Positivo)) guardado Correctamente : " + numExpediente);
              else
                  log.error("validarPasoaET_SEI - No se ha podido guardar el Campo Suplementario PASOETSEI(Comprobación paso a ET procedimiento SEI 0(Negativo) 1(Positivo)) - " + numExpediente);
            } else {
                log.error("validarPasoaET_SEI - No se ha recibido correctamente el Numero de Expediente - Vacio o null" );
            }
        }catch(Exception ex){
            log.error("Exception al guardar el campo suplementario PASOETSEI(Comprobación paso a ET procedimiento SEI 0(Negativo) 1(Positivo)) ",ex);
            codigoOperacion = "1-"+ex.getMessage();
        }finally{
            try{
                if(adapt!=null)
                    adapt.devolverConexion(con);
            }catch (Exception e) {
             log.error("validarPasoaET_SEI - Error Cerrando Conexion BD", e);
             codigoOperacion = "2-"+e.getMessage();
            }
        }
      log.error("TrazaControl - validarPasoaET_SEI() : END");
      return codigoOperacion;
   }
    
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
    
    public String cargarNuevoMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoMinimis - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevoMinimis = "/jsp/extension/melanbide33/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }

            cargarDesplegablesSubvencion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva minimis : " + ex.getMessage());
        }
        return urlnuevoMinimis;
    }

    public String cargarModificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarMinimis - " + numExpediente);
        String nuevo = "0";
        String urlnuevoMinimis = "/jsp/extension/melanbide33/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }
            String id = request.getParameter("id");
            if (id != null && !id.equals("")) {
                FilaMinimisVO datModif = MeLanbide33Manager.getInstance().getMinimisPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            cargarDesplegablesSubvencion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificaci?n : " + ex.getMessage());
        }
        return urlnuevoMinimis;

    }

    public void crearNuevoMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO nuevaMinimis = new FilaMinimisVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");

            String estado = (String) request.getParameter("estado");
            String organismo = (String) request.getParameter("organismo");
            String objeto = (String) request.getParameter("objeto");
            String importe = (String) request.getParameter("importe").replace(",", ".");
            String fecha = (String) request.getParameter("fecha");

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            nuevaMinimis.setNumExp(numExp);

            nuevaMinimis.setEstado(estado);
            nuevaMinimis.setOrganismo(organismo);
            nuevaMinimis.setObjeto(objeto);
            if (importe != null && !"".equals(importe)) {
                nuevaMinimis.setImporte(Double.valueOf(importe));
            }
            if (fecha != null && !"".equals(fecha)) {
                nuevaMinimis.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
            }

            MeLanbide33Manager meLanbide33Manager = MeLanbide33Manager.getInstance();
            boolean insertOK = meLanbide33Manager.crearNuevoMinimis(nuevaMinimis, adapt);
            if (insertOK) {
                log.debug("minimis insertada correctamente");
                codigoOperacion = "0";
                lista = meLanbide33Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                if (lista.size() > 0) {
                    for (FilaMinimisVO lm : lista) {
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                }
            } else {
                log.debug("NO se ha insertado correctamente la nueva minimis");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los par?metros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("numExp");

            String estado = (String) request.getParameter("estado");
            String organismo = (String) request.getParameter("organismo");
            String objeto = (String) request.getParameter("objeto");
            String importe = (String) request.getParameter("importe").replace(",", ".");
            String fecha = (String) request.getParameter("fecha");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaMinimisVO datModif = new FilaMinimisVO();

                datModif.setId(Integer.valueOf(id));

                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

                datModif.setNumExp(numExp);

                datModif.setEstado(estado);
                datModif.setOrganismo(organismo);
                datModif.setObjeto(objeto);
                if (importe != null && !"".equals(importe)) {
                    datModif.setImporte(Double.valueOf(importe));
                }
                if (fecha != null && !"".equals(fecha)) {
                    datModif.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
                }

                MeLanbide33Manager meLanbide33Manager = MeLanbide33Manager.getInstance();
                boolean modOK = meLanbide33Manager.modificarMinimis(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = meLanbide33Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de minimis despu?s de Modificar una minimis : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de minimis despu?s de Modificar una minimis : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "3";
                }
            }

        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);

    }

    public void eliminarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide33Manager.getInstance().eliminarMinimis(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide33Manager.getInstance().getDatosMinimis(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de minimis despu?s de eliminar una minimis");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una minimis: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    
 /**
     * M?todo que retorna el valor de un desplegable en el idiona del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide33.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide33.FICHERO_PROPIEDADES);
        
        try {
            if (!descripcion.isEmpty()) {

                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide33.CODIGO_IDIOMA_EUSKERA) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                }

            } else {
                descripcion = "-";
            }
            return descripcion;
        } catch (Exception e) {
            return descripcion;
        }
        
    }
 /**
     * M?todo que recupera el Idioma de la request para la gestion de Desplegables
     *
     * @param request
     * @return int idioma 1-castellano 4-euskera
     */
    private int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide33.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {

            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide33.CODIGO_IDIOMA_CASTELLANO;
        }

        return idioma;
    }
    
 /**
     * M?todo que recupera los valores de los desplegables del m?dulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegablesSubvencion(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaEstado = MeLanbide33Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide33.COD_DES_DTSV, ConstantesMeLanbide33.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaEstado.isEmpty()) {
            listaEstado = traducirDesplegable(request, listaEstado);
            request.setAttribute("listaEstado", listaEstado);
        }
    }

    /**
     * M?todo que extrae la descripci?n de los desplegables en el idioma del
     * usuario, en BBDD est?n en un campo separadas por Pipeline |
     *
     * @param request
     * @param desplegable
     * @return la lista en el idioma de usu
     */
    private List<DesplegableAdmonLocalVO> traducirDesplegable(HttpServletRequest request, List<DesplegableAdmonLocalVO> desplegable) {
        for (DesplegableAdmonLocalVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().isEmpty()) {
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }
        return desplegable;
    }

    
    /**
     * M?todo llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petici?n a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a trav?s del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json, HttpServletResponse response) {
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
    }
   
    
}