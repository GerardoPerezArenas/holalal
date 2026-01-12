
package es.altia.flexia.integracion.moduloexterno.melanbide13.manager;
import es.altia.flexia.integracion.moduloexterno.melanbide13.vo.ListaExpedientesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide13.dao.MeLanbide13DAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide13Manager {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide13Manager.class);

    //Instancia
    private static MeLanbide13Manager instance = null;

    public static MeLanbide13Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide13Manager.class) {
                instance = new MeLanbide13Manager();
            }
        }
        return instance;
    }

    public List<ListaExpedientesVO> getListaExpedientes(String numExp, String anio, AdaptadorSQLBD adapt) throws Exception {
        List<ListaExpedientesVO> listaExpedientes = new ArrayList<ListaExpedientesVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide13DAO meLanbide13DAO = MeLanbide13DAO.getInstance();
            listaExpedientes = meLanbide13DAO.getListaExpedientes(numExp, anio, con);
            
            return listaExpedientes;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre los Expedientes ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos sobre los Expedientes ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }

   public String getValorDesplegableExpediente(int codOrg, String numExp, String codCampo, AdaptadorSQLBD adapt) throws Exception {
        log.debug("==> getValorDesplegableExpediente Manager");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide13DAO.getInstance().getValorDesplegableExpediente(codOrg, numExp, codCampo, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando un desplegable de expediente ", ex);
            throw new Exception(ex);
        } finally {
            adapt.devolverConexion(con);
        }
    }
      
    
}
