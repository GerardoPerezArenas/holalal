package es.altia.flexia.integracion.moduloexterno.melanbide64;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide64.manager.MeLanbide64Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide64.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide64.util.ConstantesMeLanbide64;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoParamAdicionales;
import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionBindingStub;
import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionServiceLocator;
import es.altia.webservice.client.tramitacion.ws.wto.DireccionVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InfoConexionVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.RespuestasTramitacionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MELANBIDE64 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE64.class);
//    private static final ResourceBundle properties = ResourceBundle.getBundle(ConstantesMeLanbide64.getFICHERO_PROPIEDADES());

    public void cerrarTramites(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("cerrarTramites - numExpediente = " + numExpediente + ", codOrganizacion = " + codOrganizacion + "  : BEGIN");
        }
        MeLanbide64Manager meLanbide64Manager = MeLanbide64Manager.getInstance();
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;

        try {
            con = adaptador.getConnection();
            if (numExpediente != null && !"".equals(numExpediente)) {
                log.info("*** cerrarTramites: codTRAMITE -> " + codTramite);
                meLanbide64Manager.cerrarTramites(numExpediente, codTramite, con);
            }
        } catch (Exception ex) {
            log.error("*** cerrarTramites: " + ex.toString());
        } finally {
            adaptador.devolverConexion(con);
        }
        if (log.isDebugEnabled()) {
            log.debug("cancelarTramites() : END");
        }
    }

    public String impedirAvanceManualNotifTel(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, ModuloIntegracionExternoParamAdicionales parametros) throws BDException, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("impedirAvanceManualNotifTel() : BEGIN");
        }
        String codOperacion = "0";
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        Connection con = null;

        try {

            con = adaptador.getConnection();
            MeLanbide64Manager meLanbide64Manager = MeLanbide64Manager.getInstance();

            log.info("origenLlamada: " + parametros.getOrigenLlamada());
            if (meLanbide64Manager.impedirAvanceManualNotifTel(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, con) && "IN".equals(parametros.getOrigenLlamada())) {
                codOperacion = "1";
                log.info("impedirAvanceManualNotifTel: el expediente tiene notificación electrónica y el origen es INTERFAZ, no se puede avanzar manualmente.");
            }
            if (log.isDebugEnabled()) {
                log.debug("impedirAvanceManualNotifTel() : END");
            }
        } catch (Exception ex) {
            codOperacion = "2";

            log.error("Error en la función impedirAvanceManualNotifTel: ", ex);
        } finally {
            adaptador.devolverConexion(con);
        }
        return codOperacion;
    }

    //#361396 Se modifica el método para quitar envío procedimiento REINT (ya no es necesario en la nueva versión de REINT)
    public String inicioExpedienteREINT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {

        RespuestasTramitacionVO value;
        log.info("begin inicioExpREINT()");
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();
            
            
            String[] numeroExpediente = numExpediente.split(ConstantesMeLanbide64.BARRA_SEPARADORA);
            
            ExpedienteVO exp1 = new ExpedienteVO();
            exp1 = MeLanbide64Manager.getInstance().getDatosExpediente(codOrganizacion, numExpediente, con);
            IdExpedienteVO idexpediente1 = MeLanbide64Manager.getInstance().getDatosIdExpediente(codOrganizacion, numExpediente, con);
            
            //Recuperamos el nuemro para insertarlo en el idtramiteVO
            String res = numeroExpediente[2];
            int numero = Integer.parseInt(res);
            idexpediente1.setNumero(numero);
            
            exp1.setIdExpedienteVO(idexpediente1);
            
            log.info("------------ID EXPEDIENTE1 Numero ----------");
            log.info("Numero " + idexpediente1.getNumero());
            log.debug("------------ID EXPEDIENTE1 Numero fin  ----------");

            log.debug("------------ID EXPEDIENTE1----------");
            log.debug("Ejercicio " + idexpediente1.getEjercicio());
            log.debug("PROCEDIMIENTO " + idexpediente1.getProcedimiento());
            log.debug("EXPEDIENTE " + idexpediente1.getNumeroExpediente());
            log.debug("------------FIN ID EXPEDIENTE1----------");

            log.debug("------------Datos Expediente1----------");
            log.debug("Ejercicio: " + exp1.getIdExpedienteVO().getEjercicio());
            log.debug("Numero: " + exp1.getIdExpedienteVO().getNumero());
            log.debug("Expediente2: " + exp1.getIdExpedienteVO().getNumeroExpediente());
            log.debug("Procedimiento: " + exp1.getIdExpedienteVO().getProcedimiento());
            log.debug("Usuario: " + exp1.getUsuario());
            log.debug("------------Fin Datos Expediente1----------");
            
            int ano = exp1.getIdExpedienteVO().getEjercicio();
            
            //se utiliza el método siquiente para chequear si existen datos en MELANBIDE64_PROC_REINT para este ejercicio
            String resultDatosSuplementariosNuevos = MeLanbide64Manager.getInstance().getDatoSuplementariosNuevosMELANBIDE64_PROC_REINT(codOrganizacion, numExpediente, ano, numeroExpediente[1], null, con);
            if (resultDatosSuplementariosNuevos.equals("5")){
                //mensaje de error: "No se ha dado de alta la convocatoria del ejercicio del expediente para este procedimiento. 
                //Debe hablar con el Área Económica para dar de alta el código de convocatoria para este procedimiento y ejercicio."
                return "6";
            }
            
            
            String wsUrl = ConfigurationParameter.getParameter(ConstantesMeLanbide64.URL_WS_REGISTRO, ConstantesMeLanbide64.FICHERO_PROPIEDADES);
//            log.error("wsUrl: " + wsUrl);
            java.net.URL url = new java.net.URL(wsUrl);
            WSTramitacionBindingStub binding = null;
            try {
                binding = (WSTramitacionBindingStub) new WSTramitacionServiceLocator().getWSTramitacionPort(url);
                log.error("resultado binding : " + binding);
                binding.setTimeout(60000);
            } catch (Exception ex) {
                log.error("eee", ex);
            }
            //Informacion de la conexion
            InfoConexionVO inf = new InfoConexionVO();
            inf.setOrganizacion(String.valueOf(codOrganizacion));
            inf.setAplicacion("RGI");
            log.debug("------------Info conexion----------");
            log.debug("Organizacion info " + inf.getOrganizacion());
            log.debug("Aplicacion inf " + inf.getAplicacion());
            log.debug("------------Info conexion----------");

            //JESS
            //String[] numeroExpediente = numExpediente.split(ConstantesMeLanbide64.BARRA_SEPARADORA);
            //usuario y unidad org de inicio del expediente y del primer trámite (recuperamos el usuario y la unidad org del trámite del expediente padre que llega por parámetros)
            //NOTA IMPORTANTE: esta operación sólo puede llamarse en el avance normal de los trámites del flujo, NO MANUALMENTE ya que en este último caso la insert en E_CRO se hace posterior a la llamada a la operación y aquí se usa para recuperar precisamente el usuario y uor...
//            ExpedienteVO exp = MeLanbide64Manager.getInstance().getDatosExpedienteUsuUor(codOrganizacion, numExpediente, numeroExpediente[1], codTramite, con);
//            log.error("------------Datos recuperados del expediente----------");
//            log.error("------------Usuario de inicio del trámite "+codTramite+"----------> "+exp.getUsuario());
//            log.error("------------UOR de inicio del trámite "+codTramite+"----------> "+exp.getUor());
//
//            exp.setAsunto("Expediente de reintegro asociado al expediente: "+numExpediente);
//            log.error("Asunto: "+exp.getAsunto());
//
//            IdExpedienteVO idexpediente = new IdExpedienteVO();
//            idexpediente.setEjercicio(Calendar.getInstance().get(Calendar.YEAR));//año actual
//            idexpediente.setProcedimiento("REINT");
//            idexpediente.setNumero(codTramite);
//            idexpediente.setNumeroExpediente(numExpediente);
//            exp.setIdExpedienteVO(idexpediente);
//            log.error("Ejercicio: "+idexpediente.getEjercicio());
//            log.error("Procedimiento: "+idexpediente.getProcedimiento());
//            log.error("Numero: "+idexpediente.getNumero());
//            log.error("NumeroExpediente: "+idexpediente.getNumeroExpediente());

            //JAVI
            ExpedienteVO exp = MeLanbide64Manager.getInstance().getDatosExpediente(codOrganizacion, numExpediente, con);
            IdExpedienteVO idexpediente = MeLanbide64Manager.getInstance().getDatosIdExpediente(codOrganizacion, numExpediente, con);
            idexpediente.setEjercicio(Calendar.getInstance().get(Calendar.YEAR));//año actual
            //int añoActual=idexpediente.getEjercicio();
            String procedimientoPadre;
            procedimientoPadre = idexpediente.getProcedimiento();
            //Recogemos el codigoUor del expediente inicial desde BD.
            int codigoUORrecuperadodebd = MeLanbide64Manager.getInstance().getcodigoUORrecuperadodebd(codOrganizacion, numExpediente, procedimientoPadre, codTramite, con);
            log.info("codigo UOR " + codigoUORrecuperadodebd);

            int usuarioExpediente = MeLanbide64Manager.getInstance().getUsuarioExpediente(codOrganizacion, numExpediente, procedimientoPadre, codTramite, con);
            log.info("usuario TRAMITE " + usuarioExpediente);

            //Tenemos que meterle a piÃ±on exp_tra proque no he logrado meterlo correctamente en la bd de lso dos expedientes apec geenrados
            //en desa y pre tieen que estar coemntado.
            //idexpediente.setNumero(1);
            exp.setIdExpedienteVO(idexpediente);
            exp.setUor(codigoUORrecuperadodebd);
            exp.setUorTramiteInicio(codigoUORrecuperadodebd);
            exp.setUsuario(usuarioExpediente);
            exp.getIdExpedienteVO().setProcedimiento("REINT");
            exp.setAsunto("Expediente de reintegro asociado al expediente: " + numExpediente);

            List<InteresadoExpedienteVO> interesados = MeLanbide64Manager.getInstance().getDatosInteresado(codOrganizacion, Integer.parseInt(numeroExpediente[0]), numExpediente, con);
            List<InteresadoExpedienteVO> interesadosModificados = new ArrayList<InteresadoExpedienteVO>();
            for (InteresadoExpedienteVO interesado : interesados) {
                DireccionVO nuevaDireccion = interesado.getTercero().getDomicilio();
                if(nuevaDireccion.getCodPais() == 0){
                    nuevaDireccion.setCodPais(108);
                    nuevaDireccion.setNombreVia("SIN DOMICILIO");
                }
                if(nuevaDireccion.getCodProvincia() == 0){   
                    nuevaDireccion.setCodProvincia(99);
                    nuevaDireccion.setNombreVia("SIN DOMICILIO");
                }
                if(nuevaDireccion.getCodMunicipio() == 0){   
                    nuevaDireccion.setCodMunicipio(999);
                    nuevaDireccion.setNombreVia("SIN DOMICILIO");
                }
                
                interesado.getTercero().setDomicilio(nuevaDireccion);
                
                log.info("------------Datos Interesado----------");
                log.info("Rol: " + interesado.getRol());
                log.debug("-----------------Datos de remitente/Datos del Tercero---------------");
                log.info("Nombre:" + interesado.getTercero().getNombre());
                log.info("Apellido 1 remitente" + interesado.getTercero().getAp1());
                log.info("Apellido2 remitente" + interesado.getTercero().getAp2());
                log.info("TipoDoc:" + interesado.getTercero().getTipoDoc());
                log.info("Documento:" + interesado.getTercero().getDoc());
                log.info("Email:" + interesado.getTercero().getEmail());
                log.info("Telefono:" + interesado.getTercero().getTelefono());
                log.debug("----------------------fin Datos de remitente/Datos del Tercero-----------");

                log.debug("------------Datos Domicilio----------");
                log.info("Domicilio Bloque: " + interesado.getTercero().getDomicilio().getBloque());
                log.info("Domicilio COdMunicipio: " + interesado.getTercero().getDomicilio().getCodMunicipio());
                log.info("Domicilio CodPais: " + interesado.getTercero().getDomicilio().getCodPais());
                log.info("Domicilio Cpostal: " + interesado.getTercero().getDomicilio().getCodPostal());
                log.info("Domicilio CodProvincia: " + interesado.getTercero().getDomicilio().getCodProvincia());
                log.info("Domicilio Emplazamiento: " + interesado.getTercero().getDomicilio().getEmplazamiento());
                log.info("Domicilio Esprincipal : " + interesado.getTercero().getDomicilio().isEsPrincipal());
                log.info("Domicilio Escalera: " + interesado.getTercero().getDomicilio().getEscalera());
                log.info("Domicilio Nombre: " + interesado.getTercero().getDomicilio().getNombreVia());
                log.info("Domicilio Planta: " + interesado.getTercero().getDomicilio().getPlanta());
                log.info("Domicilio Portal: " + interesado.getTercero().getDomicilio().getPortal());
                log.info("Domicilio Puerta: " + interesado.getTercero().getDomicilio().getPuerta());
                log.info("Domicilio TipoVia: " + interesado.getTercero().getDomicilio().getTipoVia());
                log.info("Domicilio UltimaLetra: " + interesado.getTercero().getDomicilio().getUltimaLetra());
                log.info("Domicilio UltimoNumero: " + interesado.getTercero().getDomicilio().getUltimoNumero());
                log.debug("------------FIN Datos Domicilio----------");
                interesadosModificados.add(interesado);
            }

            if (interesadosModificados.size() > 0) {
                exp.setInteresados(interesadosModificados.toArray(new InteresadoExpedienteVO[]{}));
            }
            log.info("------------datos que enviamos al expediente ----------");
            ///Expediente
            log.debug("------------Datos Expediente----------");
            log.info("Uor: " + exp.getUor());
            log.info("Procedimiento: " + exp.getUorTramiteInicio());
            log.info("Asunto: " + exp.getAsunto());
            log.info("Usuario: " + exp.getUsuario());
            log.debug("------------Fin Datos Expediente----------");
            //IdExpedientesVO

            log.debug("------------ID EXPEDIENTE1----------");
            log.debug("Ejercicio " + idexpediente.getEjercicio());
            log.debug("PROCEDIMIENTO " + idexpediente.getProcedimiento());
            log.debug("EXPEDIENTE " + idexpediente.getNumeroExpediente());
            log.debug("------------FIN ID EXPEDIENTE1----------");
            
            log.info("------------Llamamos a ws tramitacion iniciarExpediente() ----------");
        
            value = binding.iniciarExpediente(exp, inf);
            log.info("________________Error WSTramitacion  iniciarExpediente = ________" + value.getError());
            if (value.getStatus() == 1) {
                return "6";
            }
            
            log.info("------------Respuesta de iniciarExpediente----------");
            log.info("Documento: " + value.getDocumento());
            log.info("Expediente: " + value.getExpediente());
            log.info("Numero_Expediente: " + value.getIdExpediente().getNumeroExpediente());
            log.info("Ejercicio: " + value.getIdExpediente().getEjercicio());
            log.info("Procedimiento: " + value.getIdExpediente().getProcedimiento());
            // ojo me devuelve NULL
            log.info("idTramite: " + value.getIdtramite());
            log.info("Tramite: " + value.getTramite());
            log.info("Status: " + value.getStatus());
            log.info("Error: " + value.getError());
            log.info("------------Fin Respuesta de iniciarExpediente----------");

            log.info("------------Fin Llamada a ws tramitacion iniciarExpediente() ----------");

            /*ExpedienteVO exp1 = new ExpedienteVO();
            exp1 = MeLanbide64Manager.getInstance().getDatosExpediente(codOrganizacion, numExpediente, con);
            IdExpedienteVO idexpediente1 = MeLanbide64Manager.getInstance().getDatosIdExpediente(codOrganizacion, numExpediente, con);*/
            //Recuperamos el nuemro para insertarlo en el idtramiteVO

            /*String res = numExpediente.substring(11);
            int numero = Integer.parseInt(res);
            idexpediente1.setNumero(numero);

            exp1.setIdExpedienteVO(idexpediente1);

            log.error("------------ID EXPEDIENTE1 Numero ----------");
            log.error("Numero " + idexpediente1.getNumero());
            log.error("------------ID EXPEDIENTE1 Numero fin  ----------");

            log.error("------------ID EXPEDIENTE1----------");
            log.error("Ejercicio " + idexpediente1.getEjercicio());
            log.error("PROCEDIMIENTO " + idexpediente1.getProcedimiento());
            log.error("EXPEDIENTE " + idexpediente1.getNumeroExpediente());
            log.error("------------FIN ID EXPEDIENTE1----------");

            log.debug("------------Datos Expediente1----------");
            log.debug("Ejercicio: " + exp1.getIdExpedienteVO().getEjercicio());
            log.debug("Numero: " + exp1.getIdExpedienteVO().getNumero());
            log.debug("Expediente2: " + exp1.getIdExpedienteVO().getNumeroExpediente());
            log.debug("Procedimiento: " + exp1.getIdExpedienteVO().getProcedimiento());
            log.debug("Usuario: " + exp1.getUsuario());
            log.debug("------------Fin Datos Expediente1----------");*/

            ExpedienteVO exp2 = new ExpedienteVO();
            IdExpedienteVO idexpediente2 = new IdExpedienteVO(value.getIdExpediente().getEjercicio(), value.getIdExpediente().getNumero(), value.getIdExpediente().getNumeroExpediente(), value.getIdExpediente().getProcedimiento());
            exp2.setIdExpedienteVO(idexpediente2);

            //int ano = exp1.getIdExpedienteVO().getEjercicio();

            //Una vez generado el nuevo expediente necesitamos inseertar unos campos.
            String resultDatosSuplementariosNuevos1 = MeLanbide64Manager.getInstance().getDatoSuplementariosNuevosMELANBIDE64_PROC_REINT(codOrganizacion, numExpediente, ano, numeroExpediente[1], exp2, con);
            if (resultDatosSuplementariosNuevos1.equals("5")){
                //mensaje de error: "No se ha dado de alta la convocatoria del ejercicio del expediente para este procedimiento. 
                //Debe hablar con el Área Económica para dar de alta el código de convocatoria para este procedimiento y ejercicio."
                return "5";
            }
            
            log.info("------------UOR 2----------");
            log.info("Uor: " + exp2.getUor());
            log.info("uorTramiteInicio: " + exp2.getUorTramiteInicio());
            log.info("usuario: " + exp2.getUsuario());
            log.debug("------------FIN DATOS UOR 2----------");

            log.info("------------Datos Expediente2----------");
            log.info("Ejercicio: " + exp2.getIdExpedienteVO().getEjercicio());
            log.info("Numero: " + exp2.getIdExpedienteVO().getNumero());
            log.info("Expediente2: " + exp2.getIdExpedienteVO().getNumeroExpediente());
            log.info("Procedimiento: " + exp2.getIdExpedienteVO().getProcedimiento());
            log.info("Usuario: " + exp2.getUsuario());
            log.info("------------Fin Datos Expediente2----------");

            int codUsuario = exp.getUsuario();
            
            value = binding.relacionarExpedientes(exp1, exp2, codUsuario, inf);
            log.info("________________Error WSTramitacion  relacionarExpediente = ________" + value.getError());
            if (value.getStatus() == 1) {
                return "7";
            }
            
            log.info("------------Respuesta de relacionarExpedientes()----------");
            log.info("Error: " + value.getError());
            log.info("Status: " + value.getStatus());
            log.info("------------Fin Respuesta de relacionarExpedientes()----------");

        } catch (javax.xml.rpc.ServiceException jre) {
            log.error("Error en INICIO EXPEDIENTE REINT: ", jre);
            return "2";
        } catch (Exception ex) {
            log.error("Error en INICIO EXPEDIENTE REINT: ", ex);
            return "3";
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return "0";
        
    }
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     * @throws java.sql.SQLException
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
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
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
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
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conGenerico != null && !conGenerico.isClosed()) {
                    conGenerico.close();
                }
            }// finally
        }// synchronized
        return adapt;
    }//getConnection

    public String copiadoCamposTramiteExp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info(("<============== INICIA copiadoCamposTramiteExp - Expediente: " + numExpediente + "================>"));
        String codOperacion = "0";
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide64Manager meLanbide64Manager = MeLanbide64Manager.getInstance();
        try {
            try {
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                con = adapt.getConnection();
            } catch (BDException e) {
                log.error("Error al obtener una conexion a la BBDD en copiadoCamposTramiteExp(): " + e.getMessage());
                codOperacion = "1";
            } catch (SQLException e) {
                log.error("Error al obtener una conexion a la BBDD en copiadoCamposTramiteExp(): " + e.getMessage());
                codOperacion = "1";
            }
            try {
                meLanbide64Manager.getCamposTramite(codOrganizacion, numExpediente, codTramite, ocurrenciaTramite, adapt);

            } catch (Exception e) {
                log.error("Error al obtener datos en copiadoCamposTramiteExp(): " + e.getMessage());
                codOperacion = "2";
            }
        } catch (Exception e) {
            log.error("ERROR.- copiadoCamposTramiteExp(). Error: " + e.getMessage());
            if (con != null) {
                adapt.rollBack(con);
            }
            codOperacion = "3";
        } finally {
            if (adapt != null) {
                adapt.devolverConexion(con);
            }
        }// try-catch
        return codOperacion;
    }// copiadoCamposTramite

    public String cargaCamposExpTramite(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info(("<============== INICIA cargaCamposExpTramite - Expediente: " + numExpediente + "================>"));
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide64Manager meLanbide64Manager = MeLanbide64Manager.getInstance();
        String codOperacion = "0";
        try {
            try {
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                con = adapt.getConnection();
            } catch (BDException e) {
                log.error("Error al obtener una conexion a la BBDD en cargaCamposExpTramite(): " + e.getMessage());
                codOperacion = "1";
            } catch (SQLException e) {
                log.error("Error al obtener una conexion a la BBDD en cargaCamposExpTramite(): " + e.getMessage());
                codOperacion = "1";
            }
            try {

                meLanbide64Manager.getCamposExpediente(codOrganizacion, numExpediente, codTramite, ocurrenciaTramite, adapt);
            } catch (Exception e) {
                log.error("Error al obtener datos en cargaCamposExpTramite(): " + e.getMessage());
                codOperacion = "2";
            }
        } catch (Exception e) {
            log.error("ERROR.- cargaCamposExpTramite(). Error: " + e.getMessage());
            if (con != null) {
                adapt.rollBack(con);
            }
            codOperacion = "3";
        } finally {
            if (adapt != null) {
                adapt.devolverConexion(con);
            }
        }// try-catch
        return codOperacion;
    }// cargaCamposExpTramite

    public String impedirRetrocederTramiteAbierto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception{
        log.info("===== INICIA impedirRetrocederTramiteAbierto - Exp: "+numExpediente+" Trámite: "+codTramite);
        String codOperacion="0";
        AdaptadorSQLBD adapt = null;
         String ejercicio = "";
        String codProcedimiento = "";

        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(ConstantesMeLanbide64.BARRA_SEPARADORA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];

        }//if(numExpediente!=null && !"".equals(numExpediente))
        //        String urlString = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.URL_SERVICIO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codTramImpide =(ConfigurationParameter.getParameter(ConstantesMeLanbide64.TRAMITES_IMPIDEN + ConstantesMeLanbide64.BARRA_SEPARADORA + codProcedimiento, ConstantesMeLanbide64.FICHERO_PROPIEDADES));
        log.debug("impide el tramite "+codTramImpide);
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            
        }
        MeLanbide64Manager meLanbide64Manager = MeLanbide64Manager.getInstance();
        try {
            if (meLanbide64Manager.tieneTramiteImpide(codOrganizacion,ejercicio, codProcedimiento, numExpediente, codTramImpide,adapt)){
            codOperacion="1";
            }
         
        } catch (Exception e) {
            codOperacion="2";
        }
        return codOperacion;
    }

    /**
     * #910301 - Operación para impedir el avance manual de un trámite
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param parametros
     * @return
     * @throws Exception
     */
    public String impedirAvanceManualTramite(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, ModuloIntegracionExternoParamAdicionales parametros) throws Exception {
        log.info("===== INICIA impedir AvanceManual Tramite - Exp: " + numExpediente + " Trámite: " + codTramite);
        String codOperacion = "0";
        log.info("origenLlamada: " + parametros.getOrigenLlamada());
        if ("IN".equals(parametros.getOrigenLlamada())) {
            codOperacion = "1";
            log.info("impedirAvanceManualTramite: el origen es INTERFAZ, no se puede avanzar manualmente.");
        }
        log.debug("impedirAvanceManualNotifTel() : END - Devuelve " + codOperacion);
        return codOperacion;
    }
    
    /**
     * Prepara la pantalla para el calculo de fechas
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return 
     */
    public String cargarPestanaEspera(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response){
        log.info("===========>  ENTRA EN cargarPestanaEspera " + numExpediente + " - Tr: " + codTramite + "_" + ocurrenciaTramite);
        String ejercicio = "";
        String codProcedimiento = "";
        if(numExpediente != null && !"".equals(numExpediente)){
            String[] datos = numExpediente.split(ConstantesMeLanbide64.BARRA_SEPARADORA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }
        request.setAttribute("numExp", numExpediente);
        request.setAttribute("codigoProcedimiento", codProcedimiento);
        request.setAttribute("ejercicioExp", ejercicio);
        request.setAttribute("codigoTramite", ConfigurationParameter.getParameter(codProcedimiento + ConstantesMeLanbide64.BARRA_SEPARADORA + ConstantesMeLanbide64.TRAM_INICIO_RENUNCIA, ConstantesMeLanbide64.FICHERO_PROPIEDADES));
        log.info("Carga la pestaña ==> /jsp/extension/melanbide64/melanbide64.jsp");
            
        return  "/jsp/extension/melanbide64/melanbide64.jsp";   
    }
    
    public void grabarPlazoRecurso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException, Exception{
        String cTramite = request.getParameter("codTramite");
        String cOcurrencia = request.getParameter("ocurrenciaTramite");
        
        log.info("===========>  ENTRA EN grabarPlazoRecurso " + numExpediente + " - Tr: " + cTramite + "_" + cOcurrencia);
        
        String ejercicio = "";
        String codProcedimiento = "";       
        AdaptadorSQLBD adapt = null;
        
        if(numExpediente != null && !"".equals(numExpediente)){
            String[] datos = numExpediente.split(ConstantesMeLanbide64.BARRA_SEPARADORA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }
            try{
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException e) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD", e);
            }
            
            MeLanbide64Manager meLanbide64Manager = MeLanbide64Manager.getInstance();
            
            try{ 
                meLanbide64Manager.grabarPlazoRecurso(codOrganizacion, ejercicio, codProcedimiento, numExpediente, cTramite, cOcurrencia, adapt);
                
            }catch(Exception e){
                log.error("Error al grabar el plazo recurso en BBDD ", e);
                throw new Exception(e);
            }
        
    }
}//class
