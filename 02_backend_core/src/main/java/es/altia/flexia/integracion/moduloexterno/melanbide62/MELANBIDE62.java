package es.altia.flexia.integracion.moduloexterno.melanbide62;

import com.google.gson.Gson;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide62.manager.MeLanbide62Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide62.util.ConstantesMeLanbide62;
import es.altia.flexia.integracion.moduloexterno.melanbide62.util.MeLanbide62Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.InfoExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide62.vo.InfoTramiteVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MELANBIDE62 extends ModuloIntegracionExterno {
    private static Logger log = LogManager.getLogger(MELANBIDE62.class);
    
    public String cargarDatosFichaTecnica(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        ArrayList<InfoTramiteVO> datosTram = new ArrayList<InfoTramiteVO>();
        InfoExpedienteVO datos = new InfoExpedienteVO();
        AdaptadorSQLBD adapt = null;
        String strCodTramite;
        ArrayList<String> codsCS = new ArrayList<String>();
        
        try
        {
            codsCS.add(ConstantesMeLanbide62.DIAS_SEPE);
            codsCS.add(ConstantesMeLanbide62.OBSERV_CS);
            
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String[] partes = numExpediente.split("/");
            int ejercicio = Integer.parseInt(partes[0]);
            ResourceBundle properties = ResourceBundle.getBundle("MELANBIDE62");
            
            MeLanbide62Manager manager = MeLanbide62Manager.getInstance();
            datos = manager.getDatosSuplementariosExpediente(codsCS, codOrganizacion, ejercicio, partes[1], numExpediente, adapt);
            if(datos!=null){
                strCodTramite = properties.getString("CODIGO_TRAMITE_PAGOS");
                datosTram = manager.getDatosSuplementariosTramite(codOrganizacion, ejercicio, numExpediente, Integer.parseInt(strCodTramite), adapt);
                datos.setListaTramites(datosTram);
                datos.calcularTotalDiasSol();
                datos.setDiasRestantes();
                
                // Calculamos el campo suplementario FECFINSEPE en funcion de FECINISEPE y DIASSEPE
                Date fechaFinSepe = manager.setValorCampoFechaCalculada(codOrganizacion, ejercicio, partes[1], numExpediente, adapt);
                datos.setCampoSuplementario(ConstantesMeLanbide62.FECHAFIN_SEPE, fechaFinSepe);
                obtenerMensajeValidacion(datos);
            }
        } catch (NumberFormatException nfe){
            log.error("Hay un error de formato en los datos de entrada", nfe);
        } catch(Exception ex) {
            log.error("Ha ocurrido un error al recuperar valores de datos suplementarios para el expediente "+numExpediente, ex);
        }
        request.setAttribute("datosFicha", datos);
        request.setAttribute("mensajeValidacion", datos.getMensajeValidaciones());
        return "/jsp/extension/melanbide62/datosFichaTecnica.jsp";
    }
    
    public void recargarDatosFichaTecnica(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        ArrayList<InfoTramiteVO> datosTram = new ArrayList<InfoTramiteVO>();
        InfoExpedienteVO datos = new InfoExpedienteVO();
        AdaptadorSQLBD adapt = null;
        String strCodTramite;
        ArrayList<String> codsCS = new ArrayList<String>();
        
        
        try
        {
            codsCS.add(ConstantesMeLanbide62.DIAS_SEPE);
            codsCS.add(ConstantesMeLanbide62.OBSERV_CS);
            //Recojo los parametros
            String numExp = request.getParameter("expediente");
            String organizacion = request.getParameter("organizacion");
            String[] partes = numExp.split("/");
            int ejercicio = Integer.parseInt(partes[0]);
            int codOrg = Integer.parseInt(organizacion);
            
            adapt = this.getAdaptSQLBD(organizacion);
            ResourceBundle properties = ResourceBundle.getBundle("MELANBIDE62");
            
            MeLanbide62Manager manager = MeLanbide62Manager.getInstance();
            datos = manager.getDatosSuplementariosExpediente(codsCS, codOrg, ejercicio, partes[1], numExp, adapt);
            if(datos!=null){
                strCodTramite = properties.getString("CODIGO_TRAMITE_PAGOS");
                datosTram = manager.getDatosSuplementariosTramite(codOrg, ejercicio, numExp, Integer.parseInt(strCodTramite), adapt);
                datos.setListaTramites(datosTram);
                datos.calcularTotalDiasSol();
                datos.setDiasRestantes();
                
                
                // Calculamos el campo suplementario FECFINSEPE en funcion de FECINISEPE y DIASSEPE
                Date fechaFin = manager.setValorCampoFechaCalculada(codOrg, ejercicio, partes[1], numExp, adapt);
                datos.setCampoSuplementario(ConstantesMeLanbide62.FECHAFIN_SEPE, fechaFin);
                obtenerMensajeValidacion(datos);
                String valorFecFin = "";
                if(fechaFin!=null) {
                    valorFecFin = MeLanbide62Utils.dateToFormattedString(fechaFin);
                }
                datos.setCampoSuplementario(ConstantesMeLanbide62.FECHAFIN_SEPE, valorFecFin);
            }
        } catch (NumberFormatException nfe){
            log.error("Hay un error de formato en los datos de entrada", nfe);
        } catch(Exception ex) {
            log.error("Ha ocurrido un error al recuperar valores de datos suplementarios para el expediente "+numExpediente, ex);
        } finally {
            // Devolvemos los datos en formato json   
            retornarResult(new Gson().toJson(datos), response);
        }
    }
    
    public void grabarCampoObservaciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "-1";
        AdaptadorSQLBD adapt = null;
        
        try
        {   
            //Recojo los parametros
            String numExp = request.getParameter("expediente");
            String organizacion = request.getParameter("organizacion");
            String valor = request.getParameter("observaciones");
            String[] partes = numExp.split("/");
            
            adapt = this.getAdaptSQLBD(organizacion);
            if(adapt==null){
                codigoOperacion = "1";
            }
            
            MeLanbide62Manager meLanbide62Manager = MeLanbide62Manager.getInstance();
            boolean updateOK = meLanbide62Manager.grabarValorCampoSup(ConstantesMeLanbide62.OBSERV_CS, valor, organizacion, Integer.parseInt(partes[0]), numExp, adapt);
            if(updateOK){
                log.debug("Campo modificado correctamente");
                codigoOperacion = "0";
                
            }else{
                log.debug("NO se ha podido realizar la modificaciï¿½n");
                codigoOperacion = "1";
            }
        } catch (BDException ex) {
            log.error(ex.getMessage());
            codigoOperacion = "2";
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error al modificar los datos. " + ex.getMessage());
            codigoOperacion = "1";
        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar los datos. " + ex.getMessage());
            codigoOperacion = "1";
        } finally {
            // Devolvemos el codigo de la operaciï¿½n   
            retornarResult(codigoOperacion, response);
        }
    }
    
    /**
     * Mï¿½todo que da un valor por defecto al campo suplementario REFIKUS del trï¿½mite 4 - EMISIï¿½N RESOLUCIï¿½N POSITIVA
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return Uno de los siguientes cï¿½digos: 0:todo correcto. 1: No se ha grabado el valor de campo suplementario
     * 2: Ha ocurrido un error al grabar el valor en la base de datos. 3:Ha ocurrido un error al obtener la conexiï¿½n a la base de datos. 
     */
    public String grabarValorPorDefectoCS(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {

        String salida = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE62.class.getName()).log(Level.SEVERE, null, ex);
        }

        String codCampo = ConstantesMeLanbide62.REFERENCIA_IKUS;
        String[] datosExp = numExpediente.split("/");
        int ejercicio = Integer.parseInt(datosExp[0]);
        String codPro = datosExp[1];
        String valor = numExpediente + "_";
        if(ocurrenciaTramite<10) {
            valor += "0";
        }
        valor += ocurrenciaTramite;
        
        log.debug("CODIGO ORGANIZACION: " + codOrganizacion );
        log.debug("CODIGO TRAMITE: " + codTramite );
        log.debug("OCURRENCIA TRAMITE: " + ocurrenciaTramite );
        log.debug("NUMERO EXPEDIENTE: " + numExpediente );
        log.debug("EJERCICIO: " + ejercicio );
        log.debug("CODIGO CAMPO SUPLEMENTARIO: " + codCampo );
        log.debug("VALOR CAMPO SUPLEMENTARIO: " + valor );
        
        try {
            boolean grabado = MeLanbide62Manager.getInstance().setValorDefectoCS(codCampo, valor, codOrganizacion, ejercicio, codPro, numExpediente, codTramite, ocurrenciaTramite, adapt);
            if(grabado) {
                salida = "0";
            } else {
                salida = "1";
            }
        } catch (BDException ex) {
            salida = "3";
            log.error(ex.getMessage());
        } catch (SQLException ex) {
            salida = "2";
            log.error(ex.getMessage());
        }
        return salida;
    }
    
    private void obtenerMensajeValidacion(InfoExpedienteVO infoExp){
        String mensaje = "";
        List<InfoTramiteVO> tramites = infoExp.getListaTramites();
        
        if(infoExp.getDiasRestantes().startsWith("-")) {
            mensaje += "<p class=sencillo>No quedan d&#237;as disponibles</p>";
        }
        if(tramites!=null && !tramites.isEmpty()){
            boolean cumple1 = false;
            boolean cumple2 = false;
            for(InfoTramiteVO tram : tramites){
                Object valorCampo = infoExp.getCampoSuplementario(ConstantesMeLanbide62.FECHAFIN_SEPE);
                if(valorCampo==null || tram.getfHastaPago().after((Date) valorCampo)){
                    cumple1 = true;
                    break;
                }
            }
            if(cumple1) {
                mensaje += "<p class=sencillo>La fecha fin del per&#237;odo solicitado es mayor que la fecha fin SEPE</p>";
            }
            
            for(InfoTramiteVO tram : tramites){
                Calendar fechaDesde = Calendar.getInstance();
                fechaDesde.setTime(tram.getfDesdePago());
                fechaDesde.add(Calendar.MONTH, 7);
                Date fechaDesdePost = fechaDesde.getTime();
                if(tram.getfSolPago().after(fechaDesdePost)){
                    cumple2 = true;
                    break;
                }
            }
            if(cumple2) {
                mensaje += "<p class=sencillo>La fecha solicitud es posterior en m&#225;s de 6 meses a la fecha desde el per&#237;odo solicitado</p>";
            }
            
        }
        if(!mensaje.isEmpty()) {
            mensaje = "<p>"+mensaje+"</p>";
        }
        infoExp.setMensajeValidaciones(mensaje);
    }
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     * @throws java.sql.SQLException
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
        if(log.isDebugEnabled()) {
            log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        
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
                if(log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // conexión al esquema genérico
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
                if(st!=null) {
                    st.close();
                }
                if(rs!=null) {
                    rs.close();
                }
                if(conGenerico!=null && !conGenerico.isClosed()) {
                    conGenerico.close();
                }
            }// finally
            if(log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
         }// synchronized
        return adapt;
     }//getConnection
    
    /**
     * Mï¿½todo llamado para devolver un String en formato JSON o como texto al cliente que ha realiza la peticiï¿½n 
     * a alguna de las operaciones del mï¿½dulo
     * @param result: String a devolver
     * @param response: Objeto de tipo HttpServletResponse a travï¿½s del cual se devuelve la salida
     * al cliente que ha realizado la solicitud
     */
    private void retornarResult(String result,HttpServletResponse response){
        
        try{
            if(result!=null){
                response.setCharacterEncoding("UTF-8");                
                PrintWriter out = response.getWriter();
                out.print(result);
                out.flush();
                out.close();
            }
            
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    public String actualizarDatosSuplementariosCUOTS(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException{
        log.info("actualizarDatosSuplementariosCUOTS - Expediente: "+ numExpediente + " - BEGIN");
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide62Manager meLanbide62Manager;
        ExpedienteVO datosExpediente;
        Integer edad;        

        try{
            try{
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                con   = adapt.getConnection();
            }catch(BDException e){
                log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                return "2";
            } catch (SQLException e) {
                log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                return "2";
            }
            meLanbide62Manager = MeLanbide62Manager.getInstance();
            // ini DATOS TERCERO
            try{
                datosExpediente = meLanbide62Manager.getDatosExpediente(codOrganizacion, numExpediente, con);
            }catch(Exception e){
                log.error("Error al obtener datos del expediente y tercero: " + e.getMessage());
                return "3";
            }
            //se calcula la edad
            if(datosExpediente!=null && datosExpediente.getFecPresentacion()!=null && datosExpediente.getTercero()!=null && datosExpediente.getTercero().getTFecNacimiento()!=null){
                edad = MeLanbide62Utils.calcularEdad(datosExpediente.getTercero().getTFecNacimiento(), datosExpediente.getFecPresentacion());
                adapt.inicioTransaccion(con);
                meLanbide62Manager.actualizaEdad(codOrganizacion, numExpediente, edad, con);                
            // fin DATOS TERCERO            
                adapt.finTransaccion(con);
            }
            else{
                return "4";
            }
            
        }catch(Exception e){
            log.error("actualizarDatosSuplementariosCUOTS():  Error: " + e.getMessage());
            if(con!=null) {
                adapt.rollBack(con);
            }
            return "1";
        }
        finally{
            if (adapt != null){
                adapt.devolverConexion(con);                        
            }
        }    
        
        return "0";
    }
}
