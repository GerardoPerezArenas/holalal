package es.altia.flexia.integracion.moduloexterno.meikus.persistence.dao;

import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01Constantes;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.RespuestaOperacionVO;
import java.sql.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 07/12/2012 1.0 Historial de cambios:
 * <ol>
 * <li>david.caamano * 07/12/2012 * Edición inicial</li>
 * </ol>
 */
public class RespuestasOperacionesDAO {

    //Logger
    private final static Logger log = LogManager.getLogger(RespuestasOperacionesDAO.class);

    //Instance
    private static RespuestasOperacionesDAO instance = null;

    /**
     * Devuelve una instancia unica del DAO
     *
     * @return
     */
    public static RespuestasOperacionesDAO getInstance() {
        if (instance == null) {
            log.debug(("No existe una instancia de la clase"));
            synchronized (RespuestasOperacionesDAO.class) {
                if (instance == null) {
                    log.debug(("Creamos una instancia de la clase"));
                    instance = new RespuestasOperacionesDAO();
                }//if(instance == null)
            }//synchronized(RespuestasOperacionesDAO.class)
        }//if(instance == null)
        return instance;
    }//getInstance

    /**
     * Graba una respuesta de la operacion de la pasarela electronica en la
     * tabla correspondiente
     *
     * @param respuesta
     * @param con
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    public void grabarRespuestaOperacion(RespuestaOperacionVO respuesta, Connection con) throws SQLException, Exception {
        log.debug("grabarRespuestaOperacion() : BEGIN");
        Statement st = null;
        PreparedStatement ps = null;
        try {
  //        Date fecha = new Date(Calendar.getInstance().getTimeInMillis());
            log.debug("Recuperamos el nombre de la tabla");
            String nombreTabla = MeIkus01ConfigurationParameter.getParameter(respuesta.getCodOrganizacion() + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.TABLA_RESULTADOS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            log.debug("Construimos la sentencia SQL");
            String sql = "Insert into " + nombreTabla + " (ID_RESPUESTA, ID_EXPEDIENTE, COD_ORGANIZACION, COD_PROCEDIMIENTO, EJERCICIO, COD_EVENTO, INFO_ADICIONAL, FECHA_OPERACION)"
                    + " values (SEQ_MEIKUS01_RESPUESTAS_OP.NEXTVAL,?,?,?,?,?,?,?)";
            log.debug("sql = " + sql);

            ps = con.prepareStatement(sql);
            ps.setString(1, respuesta.getIdExpediente());
            ps.setInt(2, respuesta.getCodOrganizacion());
            ps.setString(3, respuesta.getCodProcedimiento());
            ps.setInt(4, Integer.parseInt(respuesta.getEjercicio()));
            ps.setString(5, respuesta.getOperacion().getCode());
            ps.setString(6, respuesta.getInfoAdicional());
            ps.setTimestamp(7, new Timestamp(new java.util.Date().getTime()));

            ps.executeQuery();
        } catch (SQLException e) {
            log.error("Se ha producido un error insertando el resultado de la operacion de la pasarela de pagos", e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("Se ha producido un error insertando el resultado de la operacion de la pasarela de pagos", e);
            throw e;
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
            }//try-catch
        }//try-catch-finally
        log.debug("grabarRespuestaOperacion() : END");
    }//grabarOperacion

}//class
