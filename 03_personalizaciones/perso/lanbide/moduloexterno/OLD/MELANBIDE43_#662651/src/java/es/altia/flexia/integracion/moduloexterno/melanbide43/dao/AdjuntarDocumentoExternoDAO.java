
package es.altia.flexia.integracion.moduloexterno.melanbide43.dao;

import es.altia.common.exception.TechnicalException;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.util.commons.DateOperations;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author 
 */
public class AdjuntarDocumentoExternoDAO {

    private static AdjuntarDocumentoExternoDAO instance = null;
    private final Logger log = Logger.getLogger(AdjuntarDocumentoExternoDAO.class.getName());

    public static AdjuntarDocumentoExternoDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
            synchronized (AdjuntarDocumentoExternoDAO.class) {
                if (instance == null) {
                    instance = new AdjuntarDocumentoExternoDAO();
                }
            }
        }
        return instance;
    }

   public boolean insertarAdjuntoExterno(AdjuntoNotificacionVO adjuntoNotificacionVO, String oid, String TIPO_GESTOR, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        String sql = "";
        boolean exito = false;
        try {
            int codigoMunicipio = adjuntoNotificacionVO.getCodigoMunicipio();
            String numeroExpediente = adjuntoNotificacionVO.getNumeroExpediente();
            int codTramite = adjuntoNotificacionVO.getCodigoTramite();
            int ocuTramite = adjuntoNotificacionVO.getOcurrenciaTramite();
            int codigo = adjuntoNotificacionVO.getCodigoNotificacion();
            String nombre = adjuntoNotificacionVO.getNombre();
            String tipoMime = adjuntoNotificacionVO.getContentType();
            sql = "INSERT INTO ADJUNTO_EXT_NOTIFICACION(";
            if (TIPO_GESTOR.equalsIgnoreCase("ORACLE")) {
                sql = sql + "ID,";
            }
            sql = sql + "COD_MUNICIPIO,NUM_EXPEDIENTE,COD_TRAMITE,OCU_TRAMITE,FECHA,CONTENIDO,ID_NOTIFICACION,NOMBRE,TIPO_MIME,ESTADO_FIRMA) ";
            sql = TIPO_GESTOR.equalsIgnoreCase("ORACLE") ? sql + " VALUES(SEQ_FILE_EXT_NOTIFICACION.NextVal,?,?,?,?,?,?,?,?,?,?)" : sql + " VALUES(?,?,?,?,?,?,?,?,?,?)";
            log.debug((Object)sql);
            int i = 1;
            ps = con.prepareStatement(sql, new String[]{"ID"});
            ps.setInt(i++, codigoMunicipio);
            ps.setString(i++, numeroExpediente);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, ocuTramite);
            ps.setTimestamp(i++, DateOperations.toTimestamp(Calendar.getInstance()));
            ByteArrayInputStream st = new ByteArrayInputStream(adjuntoNotificacionVO.getContenido());
            ps.setBinaryStream(i++, (InputStream)st, adjuntoNotificacionVO.getContenido().length);
            st.close();
            ps.setInt(i++, codigo);
            ps.setString(i++, nombre);
            ps.setString(i++, tipoMime);
            ps.setString(i++, "O");
            int rowsInserted = ps.executeUpdate();
            log.debug((Object)(" Filas insertadas:  " + rowsInserted));
            if (rowsInserted == 1) {
                exito = true;
            }
            if (exito){
            int primkey = 0 ;
            java.sql.ResultSet generatedKeys = ps.getGeneratedKeys();
            if ( generatedKeys.next() ) {
                primkey = generatedKeys.getInt(1);
            }
             log.debug(" Id generado:  " + primkey);
             i = 1;
             sql = "INSERT INTO MELANBIDE_DOKUSI_RELDOC_EXTNOT (RELDOC_MUN, RELDOC_NUM, RELDOC_TRA, RELDOC_OCU, RELDOC_ID, RELDOC_OID) VALUES (?, ?, ?, ?, ?, ?)";
              log.debug((Object)sql); 
             ps = con.prepareStatement(sql);
              ps.setInt(i++, codigoMunicipio);
              ps.setString(i++, numeroExpediente);
              ps.setInt(i++, adjuntoNotificacionVO.getCodigoTramite());
              ps.setInt(i++, adjuntoNotificacionVO.getOcurrenciaTramite());
              ps.setInt(i++, primkey);
              ps.setString(i++, oid);
               ps.executeUpdate();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
          
                log.error((Object)("Excepcion capturada en: " + this.getClass().getName()+ e.getMessage()));
            
            exito = false;
        }
        finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            }
            catch (Exception bde) {
                bde.printStackTrace();
               
                    log.error((Object)("Excepcion capturada en: " + this.getClass().getName()));
                
                return false;
            }
        }
        return exito;
    }
    
}
