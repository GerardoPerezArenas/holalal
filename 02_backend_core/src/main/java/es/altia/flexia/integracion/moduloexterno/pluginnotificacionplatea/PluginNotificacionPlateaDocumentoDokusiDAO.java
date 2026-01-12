package es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea;

import es.altia.agora.business.util.*;
import es.altia.agora.business.util.jdbc.SigpGeneralOperations;
import es.altia.common.exception.*;
import es.altia.common.service.config.*;
import static es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.PluginNotificacionPlateaUsuarioDAO.usu_cod;
import static es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.PluginNotificacionPlateaUsuarioDAO.usu_nif;
import static es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.PluginNotificacionPlateaUsuarioDAO.usu_nom;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.util.conexion.*;
import java.sql.*;

public class PluginNotificacionPlateaDocumentoDokusiDAO {

	protected static Config m_ConfigTechnical;
	protected static Config m_ConfigError;
	protected static Logger m_Log = LogManager.getLogger(PluginNotificacionPlateaDocumentoDokusiDAO.class.getName());

        private static PluginNotificacionPlateaDocumentoDokusiDAO instance = null;

	protected PluginNotificacionPlateaDocumentoDokusiDAO() {
		super();
	}
        
        
        /**
	* Factory method para el<code>Singelton</code>.
	 * @return La unica instancia de UsuarioDAO.The	only CustomerDAO instance.
	*/
	public static PluginNotificacionPlateaDocumentoDokusiDAO getInstance() {
		if (instance == null) {
			synchronized (PluginNotificacionPlateaUsuarioDAO.class) {
				if (instance == null) instance = new PluginNotificacionPlateaDocumentoDokusiDAO();
			}
		}
		return instance;
	}
        
        
     /**
     * Recupera el nombre completo de un usuario
     * @param entorno: ENTORNO BD :  0-PRUEBAS , 1-REAL
     * @param ejercicio: ANO-EJERCICIO DEL EXPEDIENTE
     * @param procedimiento: CODIGO DEL PROCEDIMIENTO
     * @param expediente: NUMERO DE EXPEDIENTE
     * @param tramite: CODIGO DE TRAMITE ASOCIADO AL DOCUMENTO
     * @param ocurrencia: CODIGO DE OCURRENCIA DEL TRAMITE 
     * @param numero: NUMERO DE DOCUMENTO
     * @param params
     * @return Dokusi ID
     * @throws es.altia.common.exception.TechnicalException
     */
     public String getIdDokusiDocumentoTramite(int entorno, int ejercicio, String procedimiento,
                                            String expediente, int tramite, int ocurrencia, int numero,
                                            AdaptadorSQLBD oad)
            throws TechnicalException, SQLException {
         
        m_Log.debug("Llama a getIdDokusiDocumentoTramite con estos parametros");
        m_Log.debug("entorno: " + entorno);
        m_Log.debug("ejercicio: " + ejercicio);
        m_Log.debug("procedimiento: " + procedimiento);
        m_Log.debug("expediente: " + expediente);
        m_Log.debug("tramite: " + tramite);
        m_Log.debug("ocurrencia: " + ocurrencia);
        m_Log.debug("numero: " + numero);
         
        //AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String result = "";
        String sql = "";
        ResultSet rs = null;

	try {
            //oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT RELDOC_OID " + 
                " FROM MELANBIDE_DOKUSI_RELDOC_TRAMIT " +
                " WHERE RELDOC_MUN=" + entorno + 
                " AND RELDOC_EJE=" + ejercicio +
                " AND RELDOC_PRO='" + procedimiento + "'" + 
                " AND RELDOC_NUM='" + expediente + "'" + 
                " AND RELDOC_TRA=" + tramite +
                " AND RELDOC_OCU=" + ocurrencia +
                " AND RELDOC_NUD=" + numero;

            if (m_Log.isDebugEnabled()) m_Log.debug("getIdDokusiDocumentoTramite: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                result = rs.getString("RELDOC_OID");
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al recuperar Id de Dokusi del documento: " + e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            if(rs != null) rs.close();
            if(st != null) st.close();
            
            if(con != null)
                con.close();
            return result;
        }

        
    }/**
     * Recupera el nombre completo de un usuario
     * @param entorno: ENTORNO BD :  0-PRUEBAS , 1-REAL
     * @param expediente: NUMERO DE EXPEDIENTE
     * @param tramite: CODIGO DE TRAMITE ASOCIADO AL DOCUMENTO
     * @param ocurrencia: CODIGO DE OCURRENCIA DEL TRAMITE 
     * @param idDocExterno: ID DE DOCUMENTO DE NOTIFICACION
     * @param params
     * @return Dokusi ID
     * @throws es.altia.common.exception.TechnicalException
     */
     public String getIdDokusiDocumentoExterno(int idDocExterno, AdaptadorSQLBD oad) throws TechnicalException, SQLException {
         
        m_Log.debug("Llama a getIdDokusiDocumentoExterno con estos parametros");
        m_Log.debug("idDocExterno: " + idDocExterno);
         
        //AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String result = "";
        String sql = "";

	try {
            //oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "SELECT RELDOC_OID " + 
                " FROM MELANBIDE_DOKUSI_RELDOC_EXTNOT " +
                " WHERE RELDOC_ID=" + idDocExterno;

            if (m_Log.isDebugEnabled()) m_Log.debug("getIdDokusiDocumentoExterno: " + sql);
            st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                result = rs.getString("RELDOC_OID");
            }
            
            SigpGeneralOperations.closeResultSet(rs);
            SigpGeneralOperations.closeStatement(st);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al recuperar Id de Dokusi del documento: " + e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            if(con != null)
                con.close();
            return result;
        }
     }
    
     /**
    * Recupera el nombre completo de un usuario
     * @param idDocExterno
     * @param oidDokusi
     * @return 
     * @throws es.altia.common.exception.TechnicalException
     */
     public String insertarRelacionDocExternoDokusi(int entorno, String expediente, int tramite, int ocurrencia, 
                                                    int idDocExterno, String oidDokusi,
                                                    String[] params) throws TechnicalException {
         
        m_Log.debug("Llama a insertarRelacionDocExternoDokusi con estos parametros");
        m_Log.debug("entorno: " + entorno);
        m_Log.debug("expediente: " + expediente);
        m_Log.debug("tramite: " + tramite);
        m_Log.debug("ocurrencia: " + ocurrencia);
        m_Log.debug("idDocExterno: " + idDocExterno);
        m_Log.debug("oidDokusi: " + oidDokusi);
         
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String result = "";
        String sql = "";

	try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "Insert into MELANBIDE_DOKUSI_RELDOC_EXTNOT (RELDOC_MUN, RELDOC_NUM, RELDOC_TRA, RELDOC_OCU, RELDOC_ID, RELDOC_OID) " + 
                    " values " +
                    " (" + entorno + ", '" + expediente + "', " + tramite + ", " + ocurrencia + ", "+ idDocExterno + ", '" + oidDokusi + "')";

            if (m_Log.isDebugEnabled()) m_Log.debug("insertarRelacionDocExternoDokusi: " + sql);
            st = con.createStatement();
            int rs = st.executeUpdate(sql);
            
            SigpGeneralOperations.closeStatement(st);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al insertar la relación entre el documento externo y el Id de Dokusi: " + e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return result;
        }

        
    }  
     
     
      /**
    * Recupera el nombre completo de un usuario
     * @param idDocExterno
     * @return 
     * @throws es.altia.common.exception.TechnicalException
     */
     public String vaciarContenidoDocExterno(int idDocExterno, String[] params)
            throws TechnicalException {
         
        m_Log.debug("Llama a vaciarContenidoDocExterno con estos parametros");
        m_Log.debug("idDocExterno: " + idDocExterno);
         
        AdaptadorSQLBD oad = null;
        Connection con = null;
        Statement st = null;
        String result = "";
        String sql = "";

	try {
            oad = new AdaptadorSQLBD(params);
            con = oad.getConnection();

            sql = "update ADJUNTO_EXT_NOTIFICACION set contenido=null where id='" + idDocExterno+ "'";

            if (m_Log.isDebugEnabled()) m_Log.debug("vaciarContenidoDocExterno: " + sql);
            st = con.createStatement();
            int rs = st.executeUpdate(sql);
            
            SigpGeneralOperations.closeStatement(st);

        } catch (Exception e) {
            e.printStackTrace();
            m_Log.error("Error al vaciar el contenido del documento externo: " + e.getMessage());
        } finally {
            SigpGeneralOperations.devolverConexion(oad, con);
            return result;
        }

        
    } 
    
}//class
