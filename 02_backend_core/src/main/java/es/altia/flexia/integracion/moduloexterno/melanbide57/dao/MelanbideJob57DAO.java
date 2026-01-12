/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide57.dao;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide57.manager.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide57.manager.util.ConstantesMelanbide57;
import es.altia.flexia.integracion.moduloexterno.melanbide57.util.MeLanbide57MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeGeneralVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeInternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeRGIVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.NoCatalogadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.TramiteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MelanbideJob57DAO {

    private static MelanbideJob57DAO instance =	null;
    protected   static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Logger m_Log = LogManager.getLogger(MelanbideJob57DAO.class.getName());
    //Constantes de la clase
    private final int codTraGestionAreaAsignada1     = 301;
    private final int codTraGestionAreaAsignada2     = 302;
    private final int codTraGestionAreaAsignada3     = 303;
    private final int codTraGestionAreaAsignada4     = 304;
    private final String nombreProcedimiento     = "QUEJA";

    protected MelanbideJob57DAO() {
                m_CommonProperties = ConfigServiceHelper.getConfig("common");
		// Queremos usar el	fichero de configuración technical
		m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
		// Queremos tener acceso a los mensajes de error localizados
		m_ConfigError	= ConfigServiceHelper.getConfig("error");


	}    
     
    public static MelanbideJob57DAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (MelanbideJob57DAO.class) {
            if (instance == null) {
                instance = new MelanbideJob57DAO();
            }

        }
        return instance;
    }     
    

    public List<NoCatalogadoVO> getListaTramitesEliminar(Connection con) throws Exception
    {
        Statement st = null;
        ResultSet rs = null;
        List<NoCatalogadoVO> listaCatalogados  = new ArrayList<NoCatalogadoVO>();
        NoCatalogadoVO tramite;
        try
        {
            StringBuilder query = new StringBuilder("SELECT nocat.* FROM CAT_AUT_NOCATALOG nocat, R_RED cat WHERE nocat.oid_dokusi=cat.RED_IDDOC_GESTOR AND nocat.REGISTRO_EJERCICIO=cat.RED_EJE AND nocat.REGISTRO_NUMERO=cat.RED_NUM AND cat.RED_TIPODOC_ID IS NOT NULL");
        
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query.toString());
            while(rs.next())
            {
                tramite = (NoCatalogadoVO)MeLanbide57MappingUtils.getInstance().map(rs, NoCatalogadoVO.class);
                listaCatalogados.add(tramite);
            }
        }
        catch(Exception ex)
        {
            m_Log.error("Se ha producido un error recuperando tramitesAreas", ex);
            throw new Exception(ex);
        }
        finally
        {
            if(m_Log.isDebugEnabled()) 
                m_Log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) 
                st.close();
            if(rs!=null) 
                rs.close();
        }
        return listaCatalogados;
    }  
    
    
    public int insertarLineaJobEliminarCatalogados(Connection con, NoCatalogadoVO catalogado) throws Exception {
        m_Log.info("insertarLineaJobEliminarCatalogados() - Begin () ");
        int id = 0;
       CallableStatement  pt = null;
        ResultSet rs = null;

        try {
            String query = "INSERT INTO MELANBIDE57_JOB_ELIM_CAT"
                    + " (REGISTRO_EJERCICIO, REGISTRO_NUMERO, OID_DOKUSI) "
                    + " VALUES (?,?,?)";
                m_Log.info("query: " +query);
                m_Log.info("----Fin aplicar condiciones----");
            
          

            pt = con.prepareCall(query);
            pt.setString(1, catalogado.getRegistroEjercicio());
            pt.setString(2, catalogado.getRegistroNumero());
            pt.setString(3, catalogado.getOidDokusi());
           
            m_Log.info("sql = " + query);
            pt.executeUpdate();
       
            
           
             
            
        } catch (Exception ex) {
            m_Log.info("Se ha producido un error al registrar la linea en el log del job ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                m_Log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }

        m_Log.info("insertarLineaJobEliminarCatalogados  - End () ");
        return id;
    }
    public int eliminarCatalogado(Connection con, NoCatalogadoVO catalogado) throws Exception {
        m_Log.info("EliminarCatalogados() - Begin () ");
        int id = 0;
       CallableStatement  pt = null;
        ResultSet rs = null;

        try {
            String query = "DELETE FROM CAT_AUT_NOCATALOG"
                    + " WHERE REGISTRO_EJERCICIO = ? AND REGISTRO_NUMERO = ? AND OID_DOKUSI = ? ";
                m_Log.info("query: " +query);
                m_Log.info("----Fin aplicar condiciones----");
            
          

            pt = con.prepareCall(query);
            pt.setString(1, catalogado.getRegistroEjercicio());
            pt.setString(2, catalogado.getRegistroNumero());
            pt.setString(3, catalogado.getOidDokusi());
           
            m_Log.info("sql = " + query);
            pt.executeUpdate();
       
            
           
             
            
        } catch (Exception ex) {
            m_Log.info("Se ha producido un error al EliminarCatalogados ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                m_Log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }

        m_Log.info("EliminarCatalogados  - End () ");
        return id;
    }
    
}
