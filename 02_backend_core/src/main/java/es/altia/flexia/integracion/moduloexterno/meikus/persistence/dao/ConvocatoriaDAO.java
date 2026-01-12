package es.altia.flexia.integracion.moduloexterno.meikus.persistence.dao;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01Constantes;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.ConvocatoriaVO;
import es.altia.util.conexion.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author paz.rodriguez
 */
public class ConvocatoriaDAO {

    //Instancia
    private static ConvocatoriaDAO instance = null;

    //Ficheros de configuracion
    protected static Config campos; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados

    //Logger
    protected static Logger m_Log = LogManager.getLogger(ConvocatoriaDAO.class);

    /**
     * Constructor por defecto
     */
    protected ConvocatoriaDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        campos = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }//ConvocatoriaDAO

    /**
     * Devuelve una unica instancia (Singleton) de la clase ConvocatoriaDAO
     *
     * @return ConvocatoriaDAO
     */
    public static ConvocatoriaDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized (ConvocatoriaDAO.class) {
                if (instance == null) {
                    m_Log.debug("Creamos una instancia de la clase");
                    instance = new ConvocatoriaDAO();
                }//if (instance == null) 
            }//synchronized (ConvocatoriaDAO.class) 
        }//if (instance == null) 
        return instance;
    }//getInstance;

    /**
     * Método que obtiene las convocatorias de un procedimiento dado
     *
     * @param codProcedimiento Identificador único de la comunicación para la
     * que se recuperan los adjuntos
     * @param codOrganizacion
     * @param c
     * @return Lista de objectos AdjuntoComunicacionVO con los datos de los
     * adjuntos
     * @throws java.sql.SQLException
     * @throws java.lang.Exception
     */
    public Vector<ConvocatoriaVO> getConvocatorias(String codProcedimiento, Integer codOrganizacion, Connection c) throws SQLException, Exception {
        m_Log.debug("getConvocatorias() : BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        Vector salida = new Vector();

        try {
            m_Log.debug("Recuperamos el nombre de la tabla");
            String nombreTabla = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.TABLA_CONVOCATORIAS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            m_Log.debug("Creamos la sentencia SQL");
            String sql = "SELECT ID, CONVOCATORIA, NUM_PAGOS, JUSTIFICACION, PLURIANUALIDADES, NUM_ANOS, COD_PROCEDIMIENTO ";
            sql = sql + " FROM " + nombreTabla;
            sql = sql + " WHERE COD_PROCEDIMIENTO  = ?";
            sql = sql + " AND COD_ORGANIZACION = ? order by CONVOCATORIA";
            m_Log.debug("sql = " + sql);

            ps = c.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            ps.setInt(2, codOrganizacion);
            m_Log.debug(this.getClass().getName() + "Ejecutando query....");
            rs = ps.executeQuery();

            while (rs.next()) {
                ConvocatoriaVO convocatoria = new ConvocatoriaVO();
                m_Log.debug(this.getClass().getName() + ".getListaConvocatorias(): -> Seteando valores de convocatorias  ");

                convocatoria.setId(rs.getLong("ID"));
                m_Log.debug(this.getClass().getName() + ".getListaConvocatorias(): -> ID " + convocatoria.getId());

                convocatoria.setConvocatoria(rs.getInt("CONVOCATORIA"));
                m_Log.debug(this.getClass().getName() + ".getListaConvocatorias(): -> CONVOCATORIA " + convocatoria.getConvocatoria());

                convocatoria.setNumPagos(rs.getInt("NUM_PAGOS"));
                m_Log.debug(this.getClass().getName() + ".getListaConvocatorias(): -> NUM_PAGOS " + convocatoria.getNumPagos());

                convocatoria.setJustificacion(rs.getInt("JUSTIFICACION"));
                m_Log.debug(this.getClass().getName() + ".getListaConvocatorias(): -> JUSTIFICACION " + convocatoria.getJustificacion());

                convocatoria.setPlurianualidades(rs.getInt("PLURIANUALIDADES"));
                m_Log.debug(this.getClass().getName() + ".getListaConvocatorias(): -> PLURIANUALIDADES " + convocatoria.getPlurianualidades());

                convocatoria.setNumAnos(rs.getInt("NUM_ANOS"));
                m_Log.debug(this.getClass().getName() + ".getListaConvocatorias(): -> NUM_ANOS" + convocatoria.getNumAnos());

                salida.add(convocatoria);
            }//while(rs.next())
        } catch (SQLException e) {
            m_Log.error("Se ha producido un error buscando la lista de convocatorias ", e);
            throw new SQLException("Se ha producido un error buscando la lista de convocatorias" + e.getMessage());
        } finally {
            try {
                m_Log.debug("Cerramos el resultset y el prepared statement");
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                m_Log.error("Se ha producido un error al cerrar el prepared statement y el resultset" + e.getMessage());
            }//try-catch
        }//try-catch-finally
        m_Log.debug(this.getClass().getName() + ".getListaConvocatorias(): -> Devolviendo " + salida.size() + "convocatorias");
        m_Log.debug("getConvocatorias() : END");
        return salida;
    }//getConvocatorias

    /**
     * Metodo que da de alta una convocatoria determinada
     *
     * @param convocatoriaVO: Objeto de tipo ConvocatoriaVO con el contenido de
     * convocatoria
     * @param c: Conexión a la BBDD
     */
    public boolean insertarConvocatoria(ConvocatoriaVO convocatoriaVO, Connection c) throws SQLException, Exception {
        m_Log.debug("insertarConvocatoria() : BEGIN");
        PreparedStatement ps = null;
        String sql = "";
        boolean exito = false;
        try {
            m_Log.debug("Recuperamos el nombre de la tabla");
            String nombreTabla = MeIkus01ConfigurationParameter.getParameter(convocatoriaVO.getCodOrganizacion() + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.TABLA_CONVOCATORIAS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            m_Log.debug("Creamos la sentencia SQL");
            sql = "INSERT INTO " + nombreTabla
                    + " (ID, CONVOCATORIA, NUM_PAGOS, JUSTIFICACION, PLURIANUALIDADES, NUM_ANOS, COD_PROCEDIMIENTO, COD_ORGANIZACION ) "
                    + " VALUES          (SQ_MEIKUS_CONVOCATORIA.NextVal,?,?,?,?,?,?,?)";
            m_Log.debug("sql = " + sql);

            ps = c.prepareStatement(sql);
            m_Log.debug("Valor convocatoria a insertar:" + convocatoriaVO.getConvocatoria());
            ps.setInt(1, convocatoriaVO.getConvocatoria());
            m_Log.debug("Valor numPagos a insertar:" + convocatoriaVO.getNumPagos());
            ps.setInt(2, convocatoriaVO.getNumPagos());
            m_Log.debug("Valor justificacion a insertar:" + convocatoriaVO.getJustificacion());
            ps.setInt(3, convocatoriaVO.getJustificacion());
            m_Log.debug("Valor plurianualidades a insertar:" + convocatoriaVO.getPlurianualidades());
            ps.setInt(4, convocatoriaVO.getPlurianualidades());
            m_Log.debug("Valor numAnos a insertar:" + convocatoriaVO.getNumAnos());
            ps.setInt(5, convocatoriaVO.getNumAnos());
            m_Log.debug("Valor codProcedimiento a insertar:" + convocatoriaVO.getCodProcedimiento());
            ps.setString(6, convocatoriaVO.getCodProcedimiento());
            m_Log.debug("Valor codOrganizacion a insertar:" + convocatoriaVO.getCodOrganizacion());
            ps.setInt(7, convocatoriaVO.getCodOrganizacion());
            m_Log.debug("ps:" + ps);
            int rowsInserted = ps.executeUpdate();
            m_Log.debug(" Filas insertadas:  " + rowsInserted);
            if (rowsInserted == 1) {
                exito = true;
            }
        } catch (SQLException ex) {
            m_Log.error("Se ha producido un error insertando la convocatoria " + ex.getMessage());
            throw new SQLException("Se ha producido un error insertando la convocatoria " + ex.getMessage());
        } finally {
            try {
                m_Log.debug("Cerramos el prepared statement");
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException bde) {
                m_Log.error("Se ha producido un error cerrando el prepared statement y el resultset " + bde.getMessage());
                throw new Exception("Se ha producido un error cerrando el prepared statement y el resultset " + bde.getMessage());
            }//try-catch
        }//finally
        m_Log.debug("insertarConvocatoria() : END");
        return exito;
    }//insertarConvocatoria

    /**
     * Metodo que comprueba si existe una determinada convocatoria en BD
     *
     * @param ejercicio: Ejercicio
     * @param codProcedimiento: Codigo de procedimiento
     * @param con: Conexión a la BBDD
     */
    public boolean existeConvocatoria(int ejercicio, String codProcedimiento, Integer codOrganizacion, Connection con) throws SQLException, Exception {
        m_Log.debug("existeConvocatoria() : BEGIN");
        boolean retorno = false;
        ResultSet rs = null;
        Statement st = null;
        try {
            m_Log.debug("Recuperamos el nombre de la tabla");
            String nombreTabla = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.TABLA_CONVOCATORIAS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            m_Log.debug("Creamos la sentencia SQL");
            String sql = "SELECT ID, CONVOCATORIA, NUM_PAGOS, JUSTIFICACION, PLURIANUALIDADES, NUM_ANOS, COD_PROCEDIMIENTO ";
            sql = sql + " FROM " + nombreTabla;
            sql = sql + " WHERE COD_PROCEDIMIENTO='" + codProcedimiento + "' AND CONVOCATORIA=" + ejercicio;
            sql = sql + " AND COD_ORGANIZACION = " + codOrganizacion;
            m_Log.debug("sql = " + sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                retorno = true;
            }//if(rs.next())
            m_Log.debug("Existe la convocatoria = " + retorno);
        } catch (SQLException ex) {
            m_Log.error("Se ha producido un error comprobando si existe la convocatoria " + ex.getMessage());
            throw new SQLException("Se ha producido un error comprobando si existe la convocatoria " + ex.getMessage());
        } finally {
            try {
                m_Log.debug("Cerramos el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                m_Log.error("Se ha producido un error cerrando el prepared statement y el resultset");
            }//try-catch
        }//try-catch-finally
        m_Log.debug("existeConvocatoria() : END");
        return retorno;
    }//existeConvocatoria

    /**
     * Metodo que elimina una convocatoria determinada
     *
     * @param convocatoriaVO: Objeto de tipo ConvocatoriaVO con el contenido de
     * convocatoria
     * @param c: Conexión a la BBDD
     */
    public boolean eliminarConvocatoria(ConvocatoriaVO convocatoriaVO, Connection c) throws SQLException, Exception {
        m_Log.debug("eliminarConvocatoria() : BEGIN");
        boolean retorno = false;
        ResultSet rs = null;
        Statement st = null;
        Integer filasInsertadas = 0;
        try {
            m_Log.debug("Recuperamos el nombre de la tabla");
            String nombreTabla = MeIkus01ConfigurationParameter.getParameter(convocatoriaVO.getCodOrganizacion() + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.TABLA_CONVOCATORIAS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            String sql = "DELETE FROM " + nombreTabla;
            sql = sql + " WHERE COD_PROCEDIMIENTO='" + convocatoriaVO.getCodProcedimiento()
                    + "' AND CONVOCATORIA=" + convocatoriaVO.getConvocatoria() + " AND NUM_PAGOS="
                    + convocatoriaVO.getNumPagos() + " AND JUSTIFICACION=" + convocatoriaVO.getJustificacion()
                    + " AND PLURIANUALIDADES=" + convocatoriaVO.getPlurianualidades()
                    + " AND NUM_ANOS=" + convocatoriaVO.getNumAnos();
            m_Log.debug("sql = " + sql);
            st = c.createStatement();
            filasInsertadas = st.executeUpdate(sql);
            if (filasInsertadas > 0) {
                retorno = true;
            }
        } catch (SQLException ex) {
            m_Log.error("Se ha producido un error eliminando los datos de la convocatoria " + ex.getMessage());
            throw new SQLException("Se ha producido un erro eliminando los datos de la convocatoria " + ex.getMessage());
        } finally {
            try {
                m_Log.debug("Cerramos el statement y el resultset");
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                //obd.devolverConexion(con);
            } catch (SQLException e) {
                m_Log.error("Se ha producido un error cerrando el statement y el prepared statement " + e.getMessage());
                throw new Exception("Se ha producido un error cerrando el statement y el prepared statement " + e.getMessage());
            }//try-catch
        }//finally
        m_Log.debug("eliminarConvocatoria() : END");
        return retorno;
    }//eliminarConvocatoria

}//class
