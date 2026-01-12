package es.altia.flexia.integracion.moduloexterno.melanbide64.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide64.dao.MeLanbide64DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide64.vo.TramiteVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide64Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide64Manager.class);

    //Instancia
    private static MeLanbide64Manager instance = null;

    private MeLanbide64Manager() {
    }

    //Devolvemos una única instancia de la clase a través de este método ya que el constructor es privado
    public static MeLanbide64Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide64Manager.class) {
                instance = new MeLanbide64Manager();
            }
        }
        return instance;
    }

    public ExpedienteVO getDatosExpedienteUsuUor(int codMunicipio, String numExp, String procedimientoPadre, int codTramite, Connection con) throws Exception {
        try {
            return MeLanbide64DAO.getDatosExpedienteUsuUor(codMunicipio, numExp, procedimientoPadre, codTramite, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExp, ex);
            throw new Exception(ex);
        }
    }

    public ExpedienteVO getDatosExpediente(int codMunicipio, String numExp, Connection con) throws Exception {
        try {
            return MeLanbide64DAO.getDatosExpediente(codMunicipio, numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExp, ex);
            throw new Exception(ex);
        }
    }

    public IdExpedienteVO getDatosIdExpediente(int codOrganizacion, String numExpediente, Connection con) throws Exception {
        {
            try {
                return MeLanbide64DAO.getDatosIdExpediente(numExpediente, con);
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, ex);
                throw new Exception(ex);
            }
        }
    }

    public List<InteresadoExpedienteVO> getDatosInteresado(int codOrganizacion, Integer ejercicio, String numExpediente, Connection con) throws Exception {
        {
            try {
                return MeLanbide64DAO.getDatosInteresado(codOrganizacion, ejercicio, numExpediente, con);
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, ex);
                throw new Exception(ex);
            }
        }
    }

    //#361396 Se modifica el método para quitar envío procedimiento REINT
    public String getDatoSuplementariosNuevosMELANBIDE64_PROC_REINT(int codOrganizacion, String numExpediente, int ano, String procedimientoPadre, ExpedienteVO exp2, Connection con) throws Exception {
        ArrayList<String> datos = new ArrayList();
        try {
            
            datos = MeLanbide64DAO.getDatoSuplementariosNuevosMELANBIDE64_PROC_REINT(codOrganizacion, numExpediente, procedimientoPadre, ano, con);
            
            //mensaje genérico cuando no hay datos en MELANBIDE64_PROC_REINT para ese ejercicio
            if (datos.isEmpty()){
                return "5";
            } else {
                if (exp2 != null){
                    log.debug(" datos.get(0); " + datos.get(0));
                    //log.error(" datos.get(1); " + datos.get(1).toString());
                    //String REI_PRO_DEUDA = null;
                    String REI_AREA_DEUDA = null;
                    for (int x = 0; x <= datos.size(); x++) {
                        //REI_PRO_DEUDA = datos.get(0);
                        REI_AREA_DEUDA = datos.get(0);
                    }
                    //Datos devueltos despuesde hacer la sentecnia.
                    log.debug("getDatoSuplementariosNuevosMELANBIDE64_PROC_REINT ");
                    //log.error(" REI_PRO_DEUDA=datos.get(0); " + REI_PRO_DEUDA);
                    log.debug(" REI_REI_AREA_DEUDA=datos.get(0); " + REI_AREA_DEUDA);
                    log.debug("getDatoSuplementariosNuevosMELANBIDE64_PROC_REINT ");

                    //Aqui tendriamos que hacer las insert al correspondeitnes tablas.con
                    log.debug("insertando valores. ");
                    //MeLanbide64DAO.getInsertREI_PRO_DEUDA(codOrganizacion, exp2, REI_PRO_DEUDA, con);
                    log.debug("insert 1 no necesario ");
                    MeLanbide64DAO.getInsertREI_AREA_DEUDA(codOrganizacion, exp2, REI_AREA_DEUDA, con);
                    log.debug("insert 2 ok ");
                    MeLanbide64DAO.getInsert_NUMEXPSUPLEMENTARIO(codOrganizacion, numExpediente, exp2, procedimientoPadre, ano, con);
                    log.debug("insert 3 ok ");
                }
                
                return "0";
            }
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando el getDatoSuplementariosNuevosMELANBIDE64_PROC_REINT ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el ", ex);
            throw new Exception(ex);
        }
    }

    public int getcodigoUORrecuperadodebd(int codOrganizacion, String numExpediente, String procedimientoPadre, int codTramite, Connection con) throws Exception {

        try {
            log.debug("------------Antes getcodigoUORrecuperadodebd MANAGER----------");
            return MeLanbide64DAO.getcodigoUORrecuperadodebd(codOrganizacion, numExpediente, procedimientoPadre, codTramite, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el ", ex);
            throw new Exception(ex);
        }
    }

    public int getUsuarioExpediente(int codOrganizacion, String numExpediente, String procedimientoPadre, int codTramite, Connection con) throws Exception {
        try {
            return MeLanbide64DAO.getUsuarioExpediente(codOrganizacion, numExpediente, procedimientoPadre, codTramite, con);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el ", ex);
            throw new Exception(ex);
        }
    }

    public boolean impedirAvanceManualNotifTel(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, Connection con) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("impedirAvanceManualNotifTel ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        }

        if (log.isDebugEnabled()) {
            log.debug("impedirAvanceManualNotifTel() : END");
        }
        return MeLanbide64DAO.getInstance().tieneNotificacionElectronica(numExpediente, con);
    }

    public void cerrarTramites(String numExp, int codTramite, Connection con) throws Exception {
        MeLanbide64DAO meLanbide64DAO = MeLanbide64DAO.getInstance();
        meLanbide64DAO.cerrarTramite(numExp, codTramite, con);
    }

    public  void getCamposTramite(int codOrganizacion, String numExpediente, int codTramite, int ocurrenciaTramite, AdaptadorSQLBD adapt) throws BDException, SQLException, Exception {
        log.debug("----------- Entra en getCamposTramite");

        Connection con = null;
        ArrayList<TramiteVO> listaCamposTramite = new ArrayList<TramiteVO>();
        TramiteVO tramite = new TramiteVO();

        tramite.setNumExpediente(numExpediente);
        tramite.setCodTramite(codTramite);
        tramite.setOcurrenciaTramite(ocurrenciaTramite);
        MeLanbide64DAO meLanbide64DAO = MeLanbide64DAO.getInstance();
        try {
            con = adapt.getConnection();
            // recojo los campos suplementarios del tramite
            listaCamposTramite = meLanbide64DAO.getCamposTramite(tramite, con);

            int nCampos = listaCamposTramite.size();
            if (nCampos == 0) {
                log.debug("Este trámite no tiene campos suplementarios.");
            } else {
                log.debug("Este trámite tiene " + nCampos + " campo(s) suplementario(s).");
                // busco si los campos recogidos estan a nivel de expediente
                for (TramiteVO tram : listaCamposTramite) {
                    if (meLanbide64DAO.existeCampoExpediente(tram, con)) {
                        log.debug("Existe el campo  " + tram.getNomCampo() + " a nivel de expediente");
                        // grabo el suplementario
                        meLanbide64DAO.grabarCamposExpediente(codOrganizacion, tram, con);

                    } else {
                        log.debug("El campo " + tram.getNomCampo() + " no existe a nivel de expediente.");
                    }// if existeCampoExpediente                                    
                }// for                
            }  // if                            
           
        } catch (BDException e) {
            log.error("Se ha producido un error al obtener la conexion a la BBDD", e);
            throw e;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar informacion de ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar la conexion a la BBDD: " + e.getMessage());
                
            }
        }//try-catch
      
    } // getCamposTramite
    
    public void getCamposExpediente(int codOrganizacion, String numExpediente, int codTramite, int ocurrenciaTramite, AdaptadorSQLBD adapt) throws BDException, SQLException, Exception {
        log.debug("----------- Entra en getCamposExpediente");
        Connection con = null;
        ArrayList<TramiteVO> listaCamposExpediente = new ArrayList<TramiteVO>();
        TramiteVO tramite = new TramiteVO();
        tramite.setNumExpediente(numExpediente);
        tramite.setCodTramite(codTramite);
        tramite.setOcurrenciaTramite(ocurrenciaTramite);
         MeLanbide64DAO meLanbide64DAO = MeLanbide64DAO.getInstance();
          try {
            con = adapt.getConnection();
            // recojo los campos suplementarios del expediente
            listaCamposExpediente = meLanbide64DAO.getCamposExpediente(tramite, con);

            int nCampos = listaCamposExpediente.size();
            if (nCampos == 0) {
                log.debug("Este expediente no tiene campos suplementarios.");
            } else {
                log.debug("Este expediente tiene " + nCampos + " campo(s) suplementario(s).");
                // busco si los campos recogidos estan a nivel de expediente
                for (TramiteVO tram : listaCamposExpediente) {
                    if (meLanbide64DAO.existeCampoTramite(tram, con)) {
                        log.debug("Existe el campo  " + tram.getNomCampo() + " a nivel de tramite");
                        // grabo el suplementario
                        
                        meLanbide64DAO.grabarCamposTramite(codOrganizacion, tram, con);

                    } else {
                        log.debug("El campo " + tram.getNomCampo() + " no existe a nivel de tramite.");
                    }// if existeCampoExpediente                                    
                }// for                
            }  // if                            
            
        } catch (BDException e) {
            log.error("Se ha producido un error al obtener la conexion a la BBDD", e);
            throw e;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar informacion de ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar la conexion a la BBDD: " + e.getMessage());
            }
        }//try-catch
    }// getCAmpoeExpediente

    public boolean tieneTramiteImpide(int codOrganizacion, String ejercicio, String codProcedimiento, String numExpediente, String codTramImpide, AdaptadorSQLBD adapt) throws BDException, SQLException, Exception{
        log.debug(">>>> ENTRA en tieneTramiteImpide.manager");
        Connection con = null;    
        MeLanbide64DAO meLanbide64DAO = MeLanbide64DAO.getInstance();
        try {
            con = adapt.getConnection();
            return meLanbide64DAO.tramiteImpideAbierto(codOrganizacion, ejercicio, codProcedimiento, numExpediente, Integer.parseInt(codTramImpide), con);
        } catch (BDException e) {
            log.error("Se ha producido un error al obtener la conexion a la BBDD", e);
            throw e;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar informacion de ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar la conexion a la BBDD: " + e.getMessage());
            }
        }//try-catch      
    }
    
    public void grabarPlazoRecurso(int codOrganizacion, String ejercicio, String codProcedimiento, String numExpediente, String codTramite, String codOcurrencia , AdaptadorSQLBD adapt) throws Exception{
        log.debug(">>>> ENTRA en grabarPlazoRecurso.manager");
        Connection con = null;
        MeLanbide64DAO meLanbide64DAO = MeLanbide64DAO.getInstance();
        try{
            con = adapt.getConnection();
            if(!meLanbide64DAO.grabarPlazoRecurso(codOrganizacion, ejercicio, codProcedimiento, numExpediente, codTramite, codOcurrencia, con)){
                throw new Exception("Se ha producido un error al intentar grabar el plazo recurso"); 
            }else{
                log.debug("Nueva fecha plazo recurso grabada");
            }
        }catch(BDException e){
            log.error("Se ha producido un error al obtener la conexion a la BBDD", e);
            throw e;
        }catch(SQLException ex){
            log.error("Se ha producido un error al recuperar informacion de ", ex);
            throw ex;
        }
        log.debug(">>>> EXIT en grabarPlazoRecurso.manager");
    }

}// class
