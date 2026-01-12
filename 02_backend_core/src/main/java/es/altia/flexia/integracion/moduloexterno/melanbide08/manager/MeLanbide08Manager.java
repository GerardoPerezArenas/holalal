
package es.altia.flexia.integracion.moduloexterno.melanbide08.manager;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.persistence.manual.DefinicionTramitesDAO;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide08.dao.MeLanbide08DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.ComboVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.FiltrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.RetornoResultadosFiltros;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.manager.MeLanbideDokusiManager;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.util.ConstantesMeLanbide_Dokusi;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
/**
 *
 * @author alexandrep
 */
public class MeLanbide08Manager {
    
    private static final Logger LOG = LogManager.getLogger(MeLanbide08Manager.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        //Instancia
    private static MeLanbide08Manager instance = null;

    public static MeLanbide08Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide08Manager.class) {
                instance = new MeLanbide08Manager();
            }
        }
        return instance;
    }
    
    /**
     *
     * @param codOrganizacion
     * @param adaptador
     * @param filtros
     * @return
     * @throws Exception
     */
    public RetornoResultadosFiltros getLog(int codOrganizacion, AdaptadorSQLBD adaptador, FiltrosVO filtros,  List<ComboVO> listaProcedimiento) throws Exception {
        LOG.info("getLog - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        RetornoResultadosFiltros retorno = new RetornoResultadosFiltros();     

        try { 
            con = adaptador.getConnection();
            //recuperar registros a devolver
            retorno.setLstRegistros(MeLanbide08DAO.getInstance().getExpedientesFirmados(codOrganizacion, con, filtros, false,listaProcedimiento));
            //recuperar numero total de registros
            int totalRecords = MeLanbide08DAO.getInstance().getNumRegistrosTotalExpedientesFirmados(codOrganizacion, con, filtros,listaProcedimiento);
            retorno.setDraw(filtros.getDraw());
            retorno.setRecordsFiltered(totalRecords);
            retorno.setRecordsTotal(totalRecords);
        } catch (BDException ex){
            LOG.error("Se ha producido una excepcion en la BBDD recuperando los datos " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            LOG.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally {
            try{
                adaptador.devolverConexion(con);
            }catch (Exception e){
                LOG.error("Error al cerrar conexion a la BBDD; " + e.getMessage());
            }
        }
        LOG.info("getLog - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    
     public RetornoResultadosFiltros getLogSinPaginar(int codOrganizacion, AdaptadorSQLBD adaptador, FiltrosVO filtros,  List<ComboVO> listaProcedimiento) throws Exception {
        LOG.info("getLog - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        RetornoResultadosFiltros retorno = new RetornoResultadosFiltros();     

        try { 
            con = adaptador.getConnection();
            //recuperar registros a devolver
            retorno.setLstRegistros(MeLanbide08DAO.getInstance().getExpedientesFirmadosSinPaginar(codOrganizacion, con, filtros, false,listaProcedimiento));
            //recuperar numero total de registros
            int totalRecords = MeLanbide08DAO.getInstance().getNumRegistrosTotalExpedientesFirmados(codOrganizacion, con, filtros,listaProcedimiento);
            retorno.setDraw(filtros.getDraw());
            retorno.setRecordsFiltered(totalRecords);
            retorno.setRecordsTotal(totalRecords);
        } catch (BDException ex){
            LOG.error("Se ha producido una excepcion en la BBDD recuperando los datos " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            LOG.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally {
            try{
                adaptador.devolverConexion(con);
            }catch (Exception e){
                LOG.error("Error al cerrar conexion a la BBDD; " + e.getMessage());
            }
        }
        LOG.info("getLog - End () " + formatDate.format(new Date()));
        return retorno;
    }
 
    public List<ComboVO> getComboProcedimiento(int codOrganizacion,int codUsuario,AdaptadorSQLBD adaptador) throws Exception {
        LOG.info("getComboProcedimiento - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        try {
            con=adaptador.getConnection();
            return MeLanbide08DAO.getInstance().getComboProcedimiento(codOrganizacion,codUsuario,con);
        } catch (Exception e) {
            LOG.error("getComboProcedimiento  " + codOrganizacion + " Error : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
            LOG.info("getComboProcedimiento - End ()" + formatDate.format(new Date()));
        }
    }

     public String obtenerOIDDocumento (Integer codMunicipio, Integer ejercicio, String codProcedimiento, String numExpediente, 
            Integer codTramite, Integer ocurrenciaTramite, String numerDocumento, AdaptadorSQLBD adapt) throws Exception {
        
        String OIDDocumento = "";
        
        //Se obtiene el OID del documento de la tabla MELANBIDE_DOKUSI_RELDOC_TRAMIT 
        try {
            LOG.debug("Se realiza la consulta para obtener el OID del documento");
            
            DocumentoGestor documentoGestor = new DocumentoGestor();
            
            LOG.debug("municipio vale: " + codMunicipio);
            LOG.debug("ejercicio vale: " + ejercicio);
            LOG.debug("procedimiento vale: " + codProcedimiento);
            LOG.debug("numExpediente vale: " + numExpediente);
            LOG.debug("Tramite vale: " + codTramite);
            LOG.debug("Ocurrencia vale: " + ocurrenciaTramite);
            LOG.debug("Num Documento vale: " + numerDocumento);
            
            documentoGestor.setCodMunicipio(codMunicipio);
            documentoGestor.setEjercicio(ejercicio);
            documentoGestor.setCodProcedimiento(codProcedimiento);
            documentoGestor.setCodTramite(codTramite);
            documentoGestor.setNumeroExpediente(numExpediente);
            documentoGestor.setOcurrenciaTramite(ocurrenciaTramite);
            documentoGestor.setNumeroDocumento(numerDocumento);
            
            
            MeLanbideDokusiManager melanbideDokusiManager = new MeLanbideDokusiManager();
            OIDDocumento  = melanbideDokusiManager.recuperaOIDDocumentoConDatosFlexia(documentoGestor, 
                    ConstantesMeLanbide_Dokusi.CODIGO_DOCUMENTO_TRAMITACION, adapt);
   
        } catch (Exception ex) {
            LOG.error("Se ha producido un error al obtener el OID del documento " + ex.getMessage());
            ex.printStackTrace();
            throw new Exception ("Se ha producido un error al obtener el OID del documento: " + ex.getMessage());
        }//try-catch
        
        LOG.debug("OID Documento vale: " + OIDDocumento);
        
        return OIDDocumento;
    }
/**
    public void cambiarEstadoFirmaDocumentoCRD(AdaptadorSQLBD adaptador) {
  LOG.info("cambiarEstadoFirmaDocumentoCRD - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        try {
            con=adaptador.getConnection();
            return MeLanbide08DAO.getInstance().cambiarEstadoFirmaDocumentoCRD(,"LAN",con);
        } catch (Exception e) {
            LOG.error("cambiarEstadoFirmaDocumentoCRD  " + codOrganizacion + " Error : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
            LOG.info("cambiarEstadoFirmaDocumentoCRD - End ()" + formatDate.format(new Date()));
        }
    }

   **/
     
     
     
  public String getCodigoVisibleTramite(String codMunicipio, String codProcedimiento, String codTramite, String[] params,AdaptadorSQLBD adaptador) {
        String codigo = "";
        Connection con = null;
        LOG.debug((Object)"getCodigoVisibleTramite init");
        try {
            con=adaptador.getConnection();
            LOG.debug((Object)"Usando persistencia manual");
            codigo = DefinicionTramitesDAO.getInstance().getCodigoVisibleTramite(codMunicipio, codProcedimiento, codTramite, con);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return codigo;
    }
  
  
      public String getDescripcionProcedimiento(String codProcedimiento, String[] params,AdaptadorSQLBD adaptador) {
        Connection con = null;
        String descripcion = null;
        try {
             con=adaptador.getConnection();
            descripcion = DefinicionProcedimientosDAO.getInstance().getDescripcionProcedimiento(codProcedimiento, con);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return descripcion;
    }
     
      
      public String getEditorTexto(int codMunicipio, String numExpediente, int codTramite, int ocurrencia, int codDocumento, boolean perteneceRelacion, String[] params,AdaptadorSQLBD adaptador) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String editorTexto = "";
        Connection con = null;
        int ejercicio = Integer.parseInt((String)numExpediente.split("/")[0]);
        String procedimiento = numExpediente.split("/")[1];
        try {
          
          con=adaptador.getConnection();
            String sql = perteneceRelacion ? "SELECT PLT_EDITOR_TEXTO FROM A_PLT,E_DOT,G_CRD " : "SELECT PLT_EDITOR_TEXTO FROM A_PLT,E_DOT,E_CRD ";
            sql = sql + "WHERE CRD_MUN = ? AND CRD_PRO = ? AND CRD_EJE = ? AND CRD_NUM = ? AND CRD_TRA = ? AND CRD_OCU = ? AND CRD_NUD = ? AND DOT_MUN = CRD_MUN AND DOT_PRO = CRD_PRO AND DOT_TRA = CRD_TRA AND DOT_COD = CRD_DOT AND PLT_COD = DOT_PLT ";
            LOG.debug((Object)sql);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, codMunicipio);
            ps.setString(i++, procedimiento);
            ps.setInt(i++, ejercicio);
            ps.setString(i++, numExpediente);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, ocurrencia);
            ps.setInt(i++, codDocumento);
            rs = ps.executeQuery();
            while (rs.next()) {
                editorTexto = rs.getString("PLT_EDITOR_TEXTO");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                adaptador.devolverConexion(con);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            catch (BDException e) {
                e.printStackTrace();
            }
        }
        return editorTexto;
    }
      
      
      public int cambiarEstadoFirmaDocumentoCRD(es.altia.flexia.integracion.moduloexterno.melanbide08.vo.TramitacionExpedientesValueObject tEVO, int idUsuario, String portafirmas, String[] params,AdaptadorSQLBD adaptador) {
      int res=0;
      LOG.debug("INIT - cambiarEstadoFirmaDocumentoCRD");
      
      try {
          
        LOG.debug("Usando persistencia manual");
        LOG.debug("tEVO: " + tEVO.toString());
        LOG.debug("portafirmas: " + portafirmas);
        LOG.debug("params: " + params.toString());
        LOG.debug("TramitacionExpedientesDAO.getInstance(): " + TramitacionExpedientesDAO.getInstance());
        
        res = MeLanbide08DAO.getInstance().cambiarEstadoFirmaDocumentoCRD(tEVO, idUsuario, portafirmas, params,adaptador);
        LOG.debug("cambiarEstadoFirmaDocumentoCRD");
      } catch(Exception e) {
        LOG.error("Exception " + e + e.getMessage());
        res = -1;
        e.printStackTrace();
      } finally {
        LOG.debug("FIN - cambiarEstadoFirmaDocumentoCRD");
        }
        return res;
      }
       
      
      public static Calendar getDocumentoFechaModificacion(Integer codDocumento, Integer ejercicio,
            Integer municipio, String numeroExpediente, Integer codTramite,
            Integer ocurrenciaTramite, String procedimiento, String[] params ,AdaptadorSQLBD adaptador)
            throws TechnicalException {

        LOG.debug("getDocumentoFechaModificacion");

        Calendar fechaModificacion = null;
     
        Connection con = null;

        try {
           con=adaptador.getConnection();

            fechaModificacion = TramitacionExpedientesDAO.getInstance().getDocumentoFechaModificacion(
                    codDocumento, ejercicio,
                    municipio, numeroExpediente, codTramite,
                    ocurrenciaTramite, procedimiento, con);
        } catch (BDException bde) {
            throw new TechnicalException(bde.getMensaje(), bde);
        } finally {
            try{
             adaptador.devolverConexion(con);
            }catch (BDException e) {
               LOG.error("Error al cerrar la connexion" + e.getMensaje());
            }
        }
        
        return fechaModificacion;
    }
}
