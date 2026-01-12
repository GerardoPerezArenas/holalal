/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide36;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide36.manager.MeLanbide36Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.FilaExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MELANBIDE36 extends ModuloIntegracionExterno
{
    private static Logger log = LogManager.getLogger(MELANBIDE36.class);
    
//    public String cargarListaExpedientes(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
//    {
//        List<FilaExpedienteVO> expedientesRelacionados = new ArrayList<FilaExpedienteVO>();
//        try
//        {
//            expedientesRelacionados = MeLanbide36Manager.getInstance().getListaExpedientesRelacionados(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
//        }
//        catch(Exception ex)
//        {
//            log.error("Ha ocurrido un error al recuperar la lista de expedientes relacionados con "+numExpediente, ex);
//        }
//        request.setAttribute("listaExpedientesRelacionados", expedientesRelacionados);
//        
//        // -- llamada a los metodos que obtienen los NIF de persona sustituida y contratada  -- Rosa 2018/01/16 ----------->
//        AdaptadorSQLBD adapt = null;
//        Connection con = null;
//        
//        String NIFSustituida = null;      
//        try {
//            try{
//                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
//                con   = adapt.getConnection();
//                }catch(Exception e){
//                    log.error("Error al obtener una conexi�n a la BBDD en getNIFSustituida: " + e.getMessage());
//                    return "0";  
//            }
//            NIFSustituida = MeLanbide36Manager.getInstance().getNIFSustituida(codOrganizacion, numExpediente, con);
//            if (NIFSustituida == null){
//                NIFSustituida ="";
//            } 
//        }
//        catch(Exception ex)
//        {
//            log.error("Ha ocurrido un error al recuperar el rol de persona sustituida del expediente "+numExpediente, ex);
//        }
//        request.setAttribute("NIFSustituida", NIFSustituida);
//        
//        String NIFContratada = null;
//        try
//        {
//            NIFContratada = MeLanbide36Manager.getInstance().getNIFContratada(codOrganizacion, numExpediente, con);
//            if (NIFContratada == null){
//                NIFContratada ="";
//            } 
//        }
//        catch(Exception ex)
//        {
//            log.error("Ha ocurrido un error al recuperar el rol de persona contratada del expediente "+numExpediente, ex);
//        }
//        request.setAttribute("NIFContratada", NIFContratada);
//        
//        request.setAttribute("numExpediente", numExpediente);
//      
//        return "/jsp/extension/melanbide36/listaExpedientes.jsp";
//    }   
    
    public String cargarListaExpedientes(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response) throws BDException
    {
        List<FilaExpedienteVO> expedientesRelacionados = new ArrayList<FilaExpedienteVO>();
        try
        {
            expedientesRelacionados = MeLanbide36Manager.getInstance().getListaExpedientesRelacionados(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            log.error("Ha ocurrido un error al recuperar la lista de expedientes relacionados con "+numExpediente, ex);
        }
        request.setAttribute("listaExpedientesRelacionados", expedientesRelacionados);
        
        // -- llamada a los metodos que obtienen los NIF de persona sustituida y contratada  -- Rosa 2018/01/16 ----------->
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
        String NIFSustituida ="";   
        String NIFContratada = "";
        try {
            adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
            con   = adapt.getConnection();
            
            try {
                NIFSustituida = MeLanbide36Manager.getInstance().getNIFSustituida(codOrganizacion, numExpediente, con);             
            }
            catch(Exception ex)
            {
                log.error("Ha ocurrido un error al recuperar el rol de persona sustituida del expediente "+numExpediente, ex);
            }
         
            try
            {
                NIFContratada = MeLanbide36Manager.getInstance().getNIFContratada(codOrganizacion, numExpediente, con);

            }
            catch(Exception ex)
            {
                log.error("Ha ocurrido un error al recuperar el rol de persona contratada del expediente "+numExpediente, ex);
            }   
                        
        }catch(Exception e){
            log.error("Error al obtener una conexi�n a la BBDD en getNIFSustituida: " + e.getMessage());
        } finally {
            if (adapt!=null)  adapt.devolverConexion(con);
        }
        
            
        request.setAttribute("NIFSustituida", NIFSustituida);      
                
        request.setAttribute("NIFContratada", NIFContratada);
        
        request.setAttribute("numExpediente", numExpediente);
      
        return "/jsp/extension/melanbide36/listaExpedientes.jsp";
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
    
    public String cargaDatosCONCMContacto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente)  throws BDException{
        log.error("cargaDatosCONCMContacto. INICIO - Expediente: "+ numExpediente + "  =================>");
        
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide36Manager meLanbide36Manager = MeLanbide36Manager.getInstance();
        try{
            try{
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                con   = adapt.getConnection();
            }catch(Exception e){
                log.error("Error al obtener una conexi�n a la BBDD en cargaDatosCONCMContacto(): " + e.getMessage());
                return "0";  
            }
            try{
                meLanbide36Manager.getDatosContacto(codOrganizacion, numExpediente, adapt);
            }catch(Exception e){
                log.error("Error al obtener datos del tercero en cargaDatosCONCMContacto(): " + e.getMessage());
                return "0";  
            }
        }catch(Exception e){
            log.error("ERROR.- cargaDatosCONCMContacto(). Error: " + e.getMessage());
            if(con!=null) adapt.rollBack(con);
            return "0";  
        }
        finally{
            if (adapt != null){
                adapt.devolverConexion(con);                        
            }
        }    
        log.error(""
                + " "+ numExpediente + "  =================>");
        return "0";   
    }
    
    // -- metodo que valida y carga los NIF de persona sustituida y contratada  -- Rosa 2018/01/16 ----------->
    public void cargarTerceros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)throws Exception{            
        String numExp = request.getParameter("numExpediente");
        String NIFSust = request.getParameter("NIFSust");
        String NIFContr = request.getParameter("NIFContr");
        String codigoOperacion="4";
        
        AdaptadorSQLBD adaptador = null;
        Connection con = null;
        MeLanbide36Manager meLanbide36Manager = MeLanbide36Manager.getInstance();
        
        try {        
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adaptador.getConnection();

            // validar el NIF del tercero  sustituido que me pasan y si es correcto los grabo en el expediente
            TerceroVO tercero = new TerceroVO();
            if (!NIFSust.equals(null) && !NIFSust.equals("") && !NIFSust.equals(' ')){
                if (NIFSust.equals(NIFContr)){
                    codigoOperacion="5";
                }else {
                    tercero.setCodDocumento(NIFSust);
                    tercero = meLanbide36Manager.existeTercero(tercero, con);
                    String NIFSustAnt = meLanbide36Manager.getNIFSustituida(codOrganizacion, numExp, con);

                    if (tercero.isExisteTer()){
                        if ( NIFSustAnt == null || (NIFSustAnt != null && !NIFSustAnt.equals(NIFSust)))  {
                            int grabaNIFSust = meLanbide36Manager.grabaNIFSust(codOrganizacion, numExp, tercero, con);
                            if (grabaNIFSust == 1){
                                codigoOperacion="0";                 // --------------      todo bien
                            }else{
                                codigoOperacion="1";                 // --------------      no ha grabado nada
                            }
                        }else{
                            codigoOperacion="2";                     // --------------      el que se ha escrito es el mismo que está grabado
                        }
                    } else{                                           
                        codigoOperacion="3";                         // --------------      no existe el NIF que han escrito
                    }
                }
            }
            // Si ha habido un error no se valida el NIF del tercero contratado
            if (codigoOperacion == "0" || codigoOperacion == "2" || codigoOperacion == "4") {
                // validar el NIF del tercero contratado que me pasan y si es correcto los grabo en el expediente
                tercero = new TerceroVO();
                if (!NIFContr.equals(null) && !NIFContr.equals("") && !NIFContr.equals(' ')){
                    if (NIFContr.equals(NIFSust)){
                        codigoOperacion="5";
                    }else {
                        tercero.setCodDocumento(NIFContr);
                        tercero = meLanbide36Manager.existeTercero(tercero, con);
                        String NIFContAnt = meLanbide36Manager.getNIFContratada(codOrganizacion, numExp, con);

                        if (tercero.isExisteTer()){
                            if ( NIFContAnt == null || (NIFContAnt != null && !NIFContAnt.equals(NIFContr)))  {
                                int grabaNIFContr = meLanbide36Manager.grabaNIFContr(codOrganizacion, numExp, tercero, con);
                                if (grabaNIFContr == 1){
                                    codigoOperacion="0";                     // --------------      todo bien
                                }else{
                                    codigoOperacion="6";                     // --------------      no ha grabado nada
                                }
                            }else{
                                if (codigoOperacion!="0"){                   // --------------      lo dejo en "0" para que salga el mensaje de NIFs grabados del sustituido
                                    codigoOperacion="7";                     // --------------      el que se ha escrito es el mismo que está grabado
                                }
                            }                                               
                        }else{
                            codigoOperacion="8";                         // --------------      no existe el NIF que han escrito
                        }
                    }
                }
            }
            if (codigoOperacion != "0" && codigoOperacion != "2" && codigoOperacion != "7") {
                if(con!=null) adaptador.rollBack(con);
            }
        }catch (Exception e){
            log.error("ERROR.- cargarTerceros(). Error: " + e.getMessage());
            if(con!=null) adaptador.rollBack(con);
            codigoOperacion="1";  
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
                
        // grabar XML con el código de operacion 
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
        }
    }
}
