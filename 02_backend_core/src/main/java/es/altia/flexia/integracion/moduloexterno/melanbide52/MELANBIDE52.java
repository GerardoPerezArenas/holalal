package es.altia.flexia.integracion.moduloexterno.melanbide52;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide52.exception.MeLanbide52Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide52.utils.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide52.dao.MeLanbide52DAO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
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
import es.altia.flexia.integracion.moduloexterno.melanbide52.vo.ubicacion.UbicacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import net.lanbide.formacion.ws.regexlan.BajaCentroConEspecialidades;
import net.lanbide.formacion.ws.regexlan.BajaCentroConEspecialidadesRequest;
import net.lanbide.formacion.ws.regexlan.ExpedienteVO;
import net.lanbide.formacion.ws.regexlan.WSRegistroCentrosFormacionPortBindingStub;
import net.lanbide.formacion.ws.regexlan.WSRegistroCentrosFormacionServiceLocator;

public class MELANBIDE52 extends ModuloIntegracionExterno
{
        
    private static Logger log = LogManager.getLogger(MELANBIDE52.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    
    
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
    
    
    
    /**
     * Método que es invocado para cargar la pantalla principal 
     */
    public String cargarPantallaPrincipal(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        log.debug("MELANBIDE52.cargarPantallaPrincipal =======>");
        String redireccion = null;
        
        try{
            String[] datosExp = numExpediente.split("/");
            String codProcedimiento = datosExp[1];
            
            if(codProcedimiento!=null){
                ResourceBundle config = ResourceBundle.getBundle(this.getNombreModulo());                
                redireccion = config.getString(codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/SALIDA");
            }
        }catch(Exception e){
            e.printStackTrace();            
        }        

        return redireccion;
        
    }
    
    
    
    
     /**
     * 
     * Recupera las ubicaciones de un determinado centro y las devuelve en un XML para que pueda ser visualizado
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     */
    public void consultarUbicaciones(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        
        ArrayList<UbicacionVO> ubicaciones = null;        
        StringBuffer xml = new StringBuffer();
        String codError = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String codCentro = null;
        log.info(this.getClass().getName() + ".consultarUbicaciones =====>");
        
        try{
            adapt = this.getAdaptSQLBD(Integer.toString(codOrganizacion));
            con = adapt.getConnection();
            
            /**
             * Mensaje puede tomar los siguientes valores:
             *      1 --> Error al recuperar el documento de interesado con rol por defecto del expediente
             *      2 --> El expediente no tiene un interesado con rol por defecto y con un CIF como documento
             *      3 --> Se ha producido un error técnico al intentar recuperar el código de centro
             *      4 --> No se ha podido recuperar el código de centro asociado al documento del interesado con rol 
             *            por defecto en el expediente
             *      5 --> Error técnico al recuperar las ubicaciones del centro
             *      6 --> El centro no tienen asignada ninguna ubicación
             *      7 --> Ha ocurrido un error técnico al ejecutar la operación 
             *      9 --> Existe más de un centro para ese CIF
             */            
            String documento = null;
            boolean continuar = false;
            
            MeLanbide52DAO melanbide51DAO = new MeLanbide52DAO();
            try{
                // Se busca el interesado con rol principal en el expediente
                documento = melanbide51DAO.getDocumentoInteresadoRolDefecto(codOrganizacion, numExpediente,con);
                continuar = true;
            }catch(MeLanbide52Exception e){
                documento = null;
            }
                                    
            if(!continuar){
                codError ="1"; // Error al recuperar el documento de interesado con rol por defecto del expediente
            }else{
            
                if(documento==null || "".equals(documento)){
                     codError ="2"; // El expediente no tiene un interesado con rol por defecto y con un CIF como documento                
                }else{            

                    ArrayList<String> codigosCentros = null;                    
                    continuar = false;
                    try{
                        
                        codigosCentros = melanbide51DAO.getCodCentro(documento, con);
                        
                        continuar = true;
                    }catch(MeLanbide52Exception e){                    
                        continuar = false;
                    }

                    if(!continuar){
                        codError = "3"; // Se ha producido un error técnico al recuperar el código de centro
                    }else{
                       
                        
                        if(codigosCentros!=null && codigosCentros.size()>1){
                            codError = "9";                            
                        }else 
                        if(codigosCentros==null || codigosCentros.size()==0){
                            codError = "4"; 
                        }else
                        if(codigosCentros!=null && codigosCentros.size()==1){                                                
                            codCentro = codigosCentros.get(0);
                            continuar = false;
                            try{
                                // Se procede a recuperar las ubicaciones del centro
                                ubicaciones = melanbide51DAO.getUbicacionesCentro(codCentro, con);
                                continuar = true;
                            }catch(MeLanbide52Exception e){
                                continuar = false;
                            }

                            if(!continuar){
                                codError ="5"; // Error técnico al recuperar las ubicaciones del centro
                            }else{
                                if(ubicaciones==null || ubicaciones.size()==0){
                                    codError = "6"; // El centro no tienen asignada ninguna ubicación
                                }else{
                                    codError = "0"; // Se ha ejecutado la operación correctamente
                                }
                            }
                        }                    
                    }
                }
            }
            
            
            xml.append("<RESPUESTA>");
            xml.append("<STATUS>");
            xml.append(codError);
            xml.append("</STATUS>");
            if(codError.equals("0")){
                xml.append("<CODCENTRO>");
                xml.append(codCentro);
                xml.append("</CODCENTRO>");
            }
            
            if(codError.equals("0") && ubicaciones!=null){
                xml.append("<UBICACIONES>");
                for(int i=0;i<ubicaciones.size();i++){
                    UbicacionVO ubicacion = ubicaciones.get(i);
                    xml.append("<UBICACION>");
                        xml.append("<CODUBICACION>");
                            xml.append(ubicacion.getCodUbicacion());
                        xml.append("</CODUBICACION>");

                        xml.append("<NOMBREUBICACION>");
                            xml.append(ubicacion.getNombreUbicacion());
                        xml.append("</NOMBREUBICACION>");

                        xml.append("<TIPOCALLE>");
                            xml.append(ubicacion.getTipoCalle());
                        xml.append("</TIPOCALLE>");

                        xml.append("<NOMBRECALLE>");
                            xml.append(ubicacion.getNombreCalle());
                        xml.append("</NOMBRECALLE>");
                        
                        xml.append("<NUMEROCALLE>");
                            xml.append(ubicacion.getNumeroCalle());
                        xml.append("</NUMEROCALLE>");

                        xml.append("<BIS>");
                            if(ubicacion.getBis()!=null)
                                xml.append(ubicacion.getBis());
                            else
                                xml.append("-");
                        xml.append("</BIS>");

                        xml.append("<ESCALERA>");
                            if(ubicacion.getEscalera()!=null)
                                xml.append(ubicacion.getEscalera());
                            else
                                xml.append("-");
                        xml.append("</ESCALERA>");

                        xml.append("<PISO>");
                            if(ubicacion.getPiso()!=null)
                                xml.append(ubicacion.getPiso());
                            else
                                xml.append("-");
                        xml.append("</PISO>");

                        xml.append("<PUERTA>");
                            if(ubicacion.getPuerta()!=null)
                                xml.append(ubicacion.getPuerta());
                            else
                                xml.append("-");
                        xml.append("</PUERTA>");

                        xml.append("<CODPROVINCIA>");
                            xml.append(ubicacion.getCodProvincia());
                        xml.append("</CODPROVINCIA>");
                        
                        xml.append("<DESCPROVINCIA>");
                            xml.append(ubicacion.getDescProvincia());
                        xml.append("</DESCPROVINCIA>");

                        xml.append("<CODMUNICIPIO>");
                            xml.append(ubicacion.getCodMunicipio());
                        xml.append("</CODMUNICIPIO>");
                        
                        xml.append("<DESCMUNICIPIO>");
                            xml.append(ubicacion.getDescMunicipio());
                        xml.append("</DESCMUNICIPIO>");

                        xml.append("<CODPOSTAL>");
                            xml.append(ubicacion.getCodPostal());
                        xml.append("</CODPOSTAL>");
                    xml.append("</UBICACION>");
                }

                xml.append("</UBICACIONES>");
            }
            xml.append("</RESPUESTA>");
            log.info("XML: " + xml.toString());
           
            
        }
        catch(Exception e){
            e.printStackTrace();
            
            xml.append("<RESPUESTA>");
            xml.append("<STATUS>");
            xml.append("7");
            xml.append("</STATUS>");
            xml.append("</RESPUESTA>");                
                
            
        }finally{
            try{
                adapt.devolverConexion(con);
                
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xml.toString());
                out.flush();
                out.close();
                
            }catch(BDException e){
                e.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
        log.info(this.getClass().getName() + ".consultarUbicaciones <=====");        
        
    }
    

    
    /**
     * 
     * Para un trámite de un  expediente, se graba el código de centro y el código de ubicación seleccionados, en 
     * determinados campos suplementarios de tipo texto
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     */
    public void grabarUbicacionSeleccionada(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        
        log.info(this.getClass().getName() + ".grabarUbicacionSeleccionada =====>");
        StringBuffer xml = new StringBuffer();
        String codError = null;        
        String codigoUbicacion = null;
        String codigoCentro = null;
        
        String codigoCampoCodCentro = null;
        String codigoCampoCodUbicacion = null;
        String codigoNumRegistroCenso = null;
        String numCenso = null;        
        Connection con = null;
        boolean connection = false;
        
        try{
            
            codigoUbicacion = request.getParameter("codigoUbicacion");
            codigoCentro = request.getParameter("codigoCentro");
            
            /**
             * Valores posibles de la variable codError
             *      0 --> OK
             *      1 --> Error al recuperar codigoUbicacion o el código de centro
             *      2 --> No se ha podido recuperar el código del campo "Código de centro" para grabar su valor
             *      3 --> No se ha podido recuperar el código del campo "Código de ubicación" para grabar su valor
             *      4 --> No se ha podido grabar el valor del código del centro
             *      5 --> No se ha podido grabar el valor del código de la ubicación
             *      6 --> Se ha producido un error técnico
             *      7 --> No se ha podido obtener una conexión a la BBDD
             *      8 --> No se ha podido recuperar el número de censo
             *      9 --> No se ha podido recuperar el código del campo "Número de censo del registro" para grabar su valor
             *     10 --> No se ha podido grabar el valor el número de censo del centro
             */            
            if(codigoUbicacion==null || codigoCentro==null || codigoCentro.length()==0 || codigoUbicacion.length()==0){
                codError = "1";
            }else{
                
                codigoCampoCodCentro    = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO",this.getNombreModulo());
                codigoCampoCodUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION",this.getNombreModulo());
                codigoNumRegistroCenso  = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMCENSO_CENTRO",this.getNombreModulo());
                        
                if(codigoCampoCodCentro==null || codigoCampoCodCentro.length()==0){
                    codError = "2";
                }else
                if(codigoCampoCodUbicacion==null || codigoCampoCodUbicacion.length()==0){
                    codError = "3";
                }else
                if(codigoNumRegistroCenso==null || codigoNumRegistroCenso.length()==0){
                    codError = "9";                    
                }
                else{
                    
                    String[] datosExpediente = numExpediente.split("/");
                    String ejercicio = datosExpediente[0];
                    String codProcedimiento = datosExpediente[1];
                    
                    try{
                        con = this.getAdaptSQLBD(Integer.toString(codOrganizacion)).getConnection();
                        connection = true;
                        
                    }catch(Exception e){
                        e.printStackTrace();
                        connection = false;
                    }
                    
                    if(!connection){
                        // No se ha podido obtener una conexión a la BBDD
                        codError = "7";
                    }else{
                    
                        // Se procede a recuperar el número de censo, a partir del código de centro y de
                        // la ubicación del mismo
                        MeLanbide52DAO melanbide51DAO = new MeLanbide52DAO();
                        numCenso = melanbide51DAO.getNumeroCenso(codigoCentro, codigoUbicacion, con);
                        if(numCenso==null || "".equals(numCenso)){
                            codError = "8";                            
                        }else{
                        
                            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
                            
                            
                            // Se graba el valor del código de centro en el campo suplementario de "Código de centro", que es 
                            // de tipo texto
                            CampoSuplementarioModuloIntegracionVO campo = new CampoSuplementarioModuloIntegracionVO();
                            campo.setCodOrganizacion(Integer.toString(codOrganizacion));
                            campo.setCodProcedimiento(codProcedimiento);
                            campo.setEjercicio(ejercicio);
                            campo.setCodigoCampo(codigoCampoCodCentro);
                            campo.setNumExpediente(numExpediente);
                            campo.setValorTexto(codigoCentro);                    
                            campo.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);                    
                            campo.setTramite(false);

                            SalidaIntegracionVO salidaCampoCodCentro =el.grabarCampoSuplementario(campo);


                            if(salidaCampoCodCentro.getStatus()!=0){
                                codError = "4";
                            }else{

                                // Se graba el valor del código de centro en el campo suplementario de "Código de ubicación", que es 
                                // de tipo texto
                                CampoSuplementarioModuloIntegracionVO campo2 = new CampoSuplementarioModuloIntegracionVO();
                                campo2.setCodOrganizacion(Integer.toString(codOrganizacion));
                                campo2.setCodProcedimiento(codProcedimiento);
                                campo2.setEjercicio(ejercicio);
                                campo2.setCodigoCampo(codigoNumRegistroCenso);
                                campo2.setNumExpediente(numExpediente);
                                campo2.setValorTexto(codigoUbicacion);                        
                                campo2.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                campo2.setTramite(false);

                                SalidaIntegracionVO salidaCampoCodUbicacion =el.grabarCampoSuplementario(campo2);   
                                if(salidaCampoCodUbicacion.getStatus()!=0){
                                    codError = "5";
                                }else{
                                    
                                    // Se graba el valor del número de censo en el campo suplementario de "Número de registro en el censo", 
                                    // que es  de tipo texto
                                    CampoSuplementarioModuloIntegracionVO campoCenso = new CampoSuplementarioModuloIntegracionVO();
                                    campoCenso.setCodOrganizacion(Integer.toString(codOrganizacion));
                                    campoCenso.setCodProcedimiento(codProcedimiento);
                                    campoCenso.setEjercicio(ejercicio);
                                    campoCenso.setCodigoCampo(codigoNumRegistroCenso);
                                    campoCenso.setNumExpediente(numExpediente);
                                    campoCenso.setValorTexto(numCenso);                    
                                    campoCenso.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);                    
                                    campoCenso.setTramite(false);

                                    SalidaIntegracionVO salidaCampoCenso =el.grabarCampoSuplementario(campoCenso);
                                    if(salidaCampoCenso.getStatus()!=0){
                                        codError ="10";
                                    }else
                                        codError = "0";      
                                }
                            }
                        }
                    }
                }    
            }
            
            xml.append("<RESPUESTA>");
            xml.append("<STATUS>");
            xml.append(codError);
            xml.append("</STATUS>"); 
            
            if(codError.equals("0")){
                // Si se ha grabado el número de censo, se pasa en el XML para que esté 
                // disponible para /jsp/extension/melanbide51/seleccionUbicacionCentro.jsp
                xml.append("<NUMEROCENSO>");     
                xml.append(numCenso);     
                xml.append("</NUMEROCENSO>");     
            }
            
            
            xml.append("</RESPUESTA>");
            log.info("XML: " + xml.toString());
           
            
        }
        catch(Exception e){
            e.printStackTrace();            
            xml.append("<RESPUESTA>");
            xml.append("<STATUS>");
            xml.append("6");
            xml.append("</STATUS>");
            xml.append("</RESPUESTA>");    
            
        }finally{
            try{
                if(con!=null) con.close();
                
            }catch(SQLException e){
                e.printStackTrace();
            }
            
            try{
                               
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xml.toString());
                out.flush();
                out.close();
                
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
        
        log.info(this.getClass().getName() + ".grabarUbicacionSeleccionada <=====");        
        
    }
    
    
    /**
     * Método que llama a la operación bajaCentroConEspecialidades del servicio web de registro de centros
     * de formación.  Esta operación se podrá enganchar al iniciar/avanzar/retroceder un trámite de un expediente del 
     * procedimiento RGCFB
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @return Un String que puede tomar los siguientes valores:
     *   "1" --> ERROR AL RECUPERAR PROPIEDADES DE CONFIGURACIÓN DEL FICHERO DE CONFIGURACIÓN DEL MODULO
     *   "2" --> DETERMINADAS PROPIEDADES DEL FICHERO DE CONFIGURACIÓN ESTÁN VACÍAS Y NO SE PUEDE DAR DE BAJA EL CENTRO
     *   "3" --> ERROR AL ESTABLECER CONEXIÓN CON EL SERVICIO WEB DE REGISTRO DE CENTROS DE FORMACION
     *   "4" --> NO SE HA PODIDO RECUPERAR LA FECHA DE RESOLUCION
     *   "5" --> ERROR AL REALIZAR LA LLAMADA A LA OPERACION DE BAJA DE CENTROS DEL SERVICIO WEB DE REGISTRO DE CENTROS DE FORMACION
     *   "6" --> EL SERVICIO WEB DE REGISTRO DE CENTROS DE FORMACION LANZA UN ERROR AL DAR DE BAJA EL CENTRO
     *   "7" --> SE HA PRODUCIDO UN ERROR TÉCNICO DURANTE LA EJECUCIÓN DE LA OPERACIÓN
     *   "8" --> SE HA PRODUCIDO UN ERROR TÉCNICO DURANTE LA EJECUCIÓN DE LA OPERACIÓN
     */
    /*public String bajaCentroConEspecialidades(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente){
        String salida = "-1";
        
        log.info(this.getClass().getName() + ".bajaCentroConEspecialidades  =================>");
        boolean conexionWS = false;
        boolean propiedadesRecuperadas = false;
        AdaptadorSQLBD adapt = null;
       // Connection con = null;
        WSRegistroCentrosFormacionPortBindingStub binding  =null;                                    
        String codigoCampoFechaResolucion = null;        
        String urlServicioWeb = null;        
        String codigoCampoNumCenso = null; 
        String codigoTramiteNotificacionResolucionBaja= null;
        
        try{
           
           codigoCampoFechaResolucion =ConfigurationParameter.getParameter("CODIGO_CAMPO_TRAMITE_FECHA_RESOLUCION",this.getNombreModulo());                                  
           urlServicioWeb = ConfigurationParameter.getParameter("URL_WS_REGISTROCENTROS",this.getNombreModulo());
           // Se recupera el código del trámite de consulta y selección de centro del que se lee el código de centro y código de ubicación
           
           // Se recupera el código del campo en el que se grabará el número de registro devuelto por la operación altaCentroConEspecialidades
           codigoCampoNumCenso = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMCENSO_CENTRO",this.getNombreModulo());
           // Se recupera el código del trámite de notificación de resolución de baja
           codigoTramiteNotificacionResolucionBaja = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_BAJA",this.getNombreModulo());
                      
                      
           log.info("codigoCampoNumCenso: " + codigoCampoNumCenso);
           log.info("codigoCampoFechaResolucion: " + codigoCampoFechaResolucion);        
           log.info("codigoTramiteNotificacionResolucionBaja " + codigoTramiteNotificacionResolucionBaja);                 
           
           log.info("urlServicioWeb: " + urlServicioWeb);
           
           propiedadesRecuperadas = true;
        }catch(Exception e){            
            log.error(this.getClass().getName() + ".bajaCentroConEspecialidades():  Error al recuperar valores de propiedades del fichero de configuración: " + e.getMessage());
            salida = "1";
        }
        
        
        *//**
         * "1" --> ERROR AL RECUPERAR PROPIEDADES DE CONFIGURACIÓN DEL FICHERO DE CONFIGURACIÓN DEL MODULO
         * "2" --> DETERMINADAS PROPIEDADES DEL FICHERO DE CONFIGURACIÓN ESTÁN VACÍAS Y NO SE PUEDE DAR DE BAJA EL CENTRO
         * "3" --> ERROR AL ESTABLECER CONEXIÓN CON EL SERVICIO WEB DE REGISTRO DE CENTROS DE FORMACION
         * "4" --> NO SE HA PODIDO RECUPERAR LA FECHA DE RESOLUCION
         * "5" --> ERROR AL REALIZAR LA LLAMADA A LA OPERACION DE BAJA DE CENTROS DEL SERVICIO WEB DE REGISTRO DE CENTROS DE FORMACION
         * "6" --> EL SERVICIO WEB DE REGISTRO DE CENTROS DE FORMACION LANZA UN ERROR AL DAR DE BAJA EL CENTRO
         * "7" --> SE HA PRODUCIDO UN ERROR TÉCNICO DURANTE LA EJECUCIÓN DE LA OPERACIÓN
         *//*
        if(!propiedadesRecuperadas){
            salida = "1";
        }else{        
        
            if(codigoCampoFechaResolucion==null ||
               codigoCampoNumCenso==null || codigoTramiteNotificacionResolucionBaja==null){                                
                    salida = "2";                
            }else{
                 
                try{            
                   binding = (WSRegistroCentrosFormacionPortBindingStub)new WSRegistroCentrosFormacionServiceLocator().getWSRegistroCentrosFormacionPort(new java.net.URL(urlServicioWeb));
                   conexionWS=true;                 
                }catch(Exception e){                                         
                    log.error(this.getClass().getName() + ".bajaCentroConEspecialidades(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                }
                
                 
                if(!conexionWS){  
                    salida = "3";
                }else{
                    *//*********** LLAMADA AL SERVICIO WEB ***********//*
                    try{
                        
                        String[] datosExp      = numExpediente.split("/");
                        String ejercicio       = datosExp[0];
                        String codProcedimiento = datosExp[1];
                        
                        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

                        // Se recupera la ocurrencia más reciente del trámite de "INSCRIPCION O ACREDITACIÓN DEL CENTRO O ENTIDAD EN EL REGISTRO"
                        //int ocurrencia2 = melanbide51DAO.getOcurrenciaMasRecienteTramite(Integer.parseInt(codigoTramiteNotificacionResolucionBaja), numExpediente,Integer.toString(codOrganizacion), con);
                        
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION DESISTIMIENTO NEGATIVA"
                        SalidaIntegracionVO salidaFecResolucionTramite = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion),ejercicio,numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteNotificacionResolucionBaja), 1, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        
                        if(salidaFecResolucionTramite.getStatus()!=0){
                            salida = "4";
                        }else{
                            
                            //codigoCampoNumCenso                            
                            SalidaIntegracionVO salidaCampoNumCenso = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion),ejercicio, numExpediente, codProcedimiento,codigoCampoNumCenso,IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                            if(salidaCampoNumCenso.getStatus()!=0){
                                salida = "8";                                
                            }else{
                                                             
                                *//******** ExpedienteVO ********//*
                                ExpedienteVO expVO = new ExpedienteVO();
                                expVO.setNumExpediente(numExpediente);

                                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");    
                                if(salidaFecResolucionTramite.getStatus()==0 && salidaFecResolucionTramite.getCampoSuplementario()!=null)                                
                                  expVO.setFechaResolucion(sf.format(salidaFecResolucionTramite.getCampoSuplementario().getValorFecha().getTime()));


                                    // BAJA CENTRO CON ESPECIALIDADES

                                    net.lanbide.formacion.ws.regexlan.BajaCentroConEspecialidades bajaCentroConEspecialidades = new BajaCentroConEspecialidades();                                        
                                    net.lanbide.formacion.ws.regexlan.BajaCentroConEspecialidadesRequest bajaCentroConEspecialidadesRequest = new BajaCentroConEspecialidadesRequest();
                                    bajaCentroConEspecialidadesRequest.setExpediente(expVO);
                                    // Número de censo
                                    bajaCentroConEspecialidadesRequest.setNumCensoLB(numExpediente);
                                    bajaCentroConEspecialidades.setBajaCentroConEspecialidadesRequest(bajaCentroConEspecialidadesRequest);
                                    net.lanbide.formacion.ws.regexlan.BajaCentroConEspecialidadesResponse respuesta = binding.bajaCentroConEspecialidades(bajaCentroConEspecialidades);


                                    if(respuesta!=null){

                                        log.info("respuesta.get_return().getCodRdo(): " + respuesta.get_return().getCodRdo());
                                        log.info("respuesta.get_return().getDescRdo(): " + respuesta.get_return().getDescRdo());
                                        log.info("respuesta.get_return().getNumCensoLB(): " + respuesta.get_return().getNumCensoLB());

                                        if(respuesta.get_return().getCodRdo().equals(0)){
                                            salida = "0";
                                        }
                                        else
                                            salida = "6";
                                    }else
                                        salida = "5";
                               }                                    
                          }
                
                    }catch(Exception e){                        
                        log.error("Error técnico durante la ejecución de la operación de bajaCentroConEspecialidades: " + e.getMessage());
                        salida = "7";
                    }                
                    *//**********************************************//*
                }
                
            }// else    
        }// if(propiedadesRecuperadas)
        
        log.debug("bajaCentroConEspecialidades devolviendo " + salida);                
        
        return salida;
    }*/
    
}
