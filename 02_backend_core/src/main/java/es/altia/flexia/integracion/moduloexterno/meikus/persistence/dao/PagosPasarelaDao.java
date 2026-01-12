package es.altia.flexia.integracion.moduloexterno.meikus.persistence.dao;

import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.PagoPasarelaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01Constantes;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.TerceroPasarela;
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
public class PagosPasarelaDao {

    //Logger
    private static final Logger log = LogManager.getLogger(PagosPasarelaDao.class);

    //Instance
    private static PagosPasarelaDao instance;

    /**
     * Devuelve una unica instancia (Singleton) de la clase PagosPasarelaDao
     *
     * @return
     */
    public static PagosPasarelaDao getInstance() {
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : BEGIN");
        }
        if (instance == null) {
            synchronized (PagosPasarelaDao.class) {
                if (instance == null) {
                    instance = new PagosPasarelaDao();
                }//if(instance == null)
            }//synchronized(PagosPasarelaDao.class)
        }//if(instance == null)
        if (log.isDebugEnabled()) {
            log.debug("getInstance() : END");
        }
        return instance;
    }//getInstance

    /**
     * Comprueba si ya existen datos para el expediente y lo da de alta o lo
     * modifica en funcion de si existe previamente o no
     *
     * @param pago
     * @param con
     * @throws SQLException
     * @throws Exception
     */
    public void altaExpediente(PagoPasarelaVO pago, Connection con) throws SQLException, Exception {
        log.info("altaExpediente() : BEGIN");
        try {
            log.debug("Comprobamos si existe ya un expediente");

            PagoPasarelaVO pagoExpediente = getExpediente(pago.getNumExpediente(), pago.getCodOrganizacion(), con);
            if (pagoExpediente != null && pagoExpediente.getNumExpediente() != null) {
                log.info("Existe expediente, actualizamos los datos");
                modificarExpediente(pago, con);
            } else {
                log.info("No existe expediente, damos de alta uno nuevo");
                anhadirExpediente(pago, con);
            }//if(pagoExpediente != null && pagoExpediente.getNumExpediente() != null)
        } catch (SQLException ex) {
            log.error("Se ha producido un error dando de alta un expediente " + ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            log.error("Se ha producido un error dando de alta un expediente " + ex.getMessage());
            throw ex;
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("altaExpediente() : END");
        }
    }//anhadirExpediente

    /**
     * Devuelve los datos asociados a un expediente
     *
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public PagoPasarelaVO getExpediente(String numExpediente, Integer codOrganizacion, Connection con) throws SQLException, Exception {
        log.info("getExpediente() : BEGIN");
        PagoPasarelaVO pagoPasarelaVO = new PagoPasarelaVO();
        Statement st = null;
        ResultSet rs = null;
        try {
            String nombreTabla = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.TABLA_PAGOS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            String sql = "Select ID_EXPEDIENTE, COD_ORGANIZACION, COD_PROCEDIMIENTO, EJERCICIO, NUMERO_PAGO, ID_EXPEDIENTE_PASARELA_PAGO from "
                    + nombreTabla + " " + " where ID_EXPEDIENTE = '" + numExpediente + "' and COD_ORGANIZACION = " + codOrganizacion;
            log.debug("sql = " + sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                log.debug("Existe expediente");

                pagoPasarelaVO.setNumExpediente(rs.getString("ID_EXPEDIENTE"));
                pagoPasarelaVO.setCodOrganizacion(rs.getInt("COD_ORGANIZACION"));
                pagoPasarelaVO.setCodProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                pagoPasarelaVO.setEjercicio(String.valueOf(rs.getInt("EJERCICIO")));
                pagoPasarelaVO.setNumPago(String.valueOf(rs.getInt("NUMERO_PAGO")));
                pagoPasarelaVO.setIdExpedientePasarela(String.valueOf(rs.getString("ID_EXPEDIENTE_PASARELA_PAGO")));
            }//while(rs.next())
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando el expediente " + e.getMessage());
            throw e;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
            }//try-catch
        }//try-catch-finally
        log.debug("getExpediente() : END");

        return pagoPasarelaVO;
    }//getExpediente

    /**
     * Anhade un nuevo registro para un expediente
     *
     * @param pago
     * @param con
     * @throws SQLException
     * @throws Exception
     */
    private void anhadirExpediente(PagoPasarelaVO pago, Connection con) throws SQLException, Exception {
        log.info("anhadirExpediente() : BEGIN");
        PreparedStatement ps = null;
        try {
            String nombreTabla = MeIkus01ConfigurationParameter.getParameter(pago.getCodOrganizacion() + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.TABLA_PAGOS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            String sql = "Insert into " + nombreTabla + " (ID_EXPEDIENTE, COD_ORGANIZACION, COD_PROCEDIMIENTO, EJERCICIO, NUMERO_PAGO) "
                    + " values (?, ?, ?, ?, ?)";
            log.debug("sql = " + sql);

            ps = con.prepareStatement(sql);
            ps.setString(1, pago.getNumExpediente());
            ps.setInt(2, pago.getCodOrganizacion());
            ps.setString(3, pago.getCodProcedimiento());
            ps.setInt(4, Integer.parseInt(pago.getEjercicio()));
            ps.setInt(5, Integer.parseInt(pago.getNumPago()));
            ps.executeQuery();
        } catch (SQLException e) {
            log.error("Se ha producido un error anhadiendo el expediente " + e.getMessage());
            throw e;
        } catch (NumberFormatException e) {
            log.error("Se ha producido un error anhadiendo el expediente " + e.getMessage());
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
            }//try-catch
        }//try-catch-finally
        if (log.isDebugEnabled()) {
            log.debug("anhadirExpediente() : END");
        }
    }//anhadirExpediente

    /**
     * Modifica el numero de pago de un expediente
     *
     * @param pago
     * @param con
     * @throws SQLException
     * @throws Exception
     */
    public void modificarExpediente(PagoPasarelaVO pago, Connection con) throws SQLException, Exception {
        log.info("modificarExpediente() : BEGIN");
        PreparedStatement ps = null;
        try {
            String nombreTabla = MeIkus01ConfigurationParameter.getParameter(pago.getCodOrganizacion() + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.TABLA_PAGOS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            String sql = "Update " + nombreTabla + " set NUMERO_PAGO = ? where ID_EXPEDIENTE = ?";
            log.debug("sql = " + sql);

            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(pago.getNumPago()));
            ps.setString(2, pago.getNumExpediente());
            ps.executeQuery();
        } catch (SQLException e) {
            log.error("Se ha producido un error modificando el resultado de la operacion de la pasarela de pagos " + e.getMessage());
            throw e;
        } catch (NumberFormatException e) {
            log.error("Se ha producido un error modificando el resultado de la operacion de la pasarela de pagos " + e.getMessage());
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
            }//try-catch
        }//try-catch-finally
        log.debug("modificarExpediente() : END");

    }//modificarExpediente

    /**
     * Anhade al registro el id expediente generado por la pasarela de pagos.
     *
     * @param pago
     * @param con
     * @throws SQLException
     * @throws Exception
     */
    public void anhadirIdExpedientePasarela(PagoPasarelaVO pago, Connection con) throws SQLException, Exception {
        log.info("anhadirIdExpedientePasarela() : BEGIN");
        PreparedStatement ps = null;
        try {
            String nombreTabla = MeIkus01ConfigurationParameter.getParameter(pago.getCodOrganizacion() + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TABLA_PAGOS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            String sql = "Update " + nombreTabla + " set ID_EXPEDIENTE_PASARELA_PAGO = ? where ID_EXPEDIENTE = ?";
            log.debug("sql = " + sql);

            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(pago.getIdExpedientePasarela()));
            ps.setString(2, pago.getNumExpediente());
            ps.executeQuery();
        } catch (SQLException e) {
            log.error("Se ha producido un error anhadiendo el id del expediente generado por la pasarela de pagos", e);
            throw e;
        } catch (NumberFormatException e) {
            log.error("Se ha producido un error anhadiendo el id del expediente generado por la pasarela de pagos", e);
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
            }//try-catch
        }//try-catch-finally
        log.debug("anhadirIdExpedientePasarela() : END");
    }//anhadirIdExpedientePasarela

    public Integer getOcurrencia(String numExpediente, Integer codOrganizacion, Integer codTramite, Connection con) throws SQLException, Exception {
        log.debug("getOcurrencia() : BEGIN");
        Integer ocurrencia = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "Select max(CRO_OCU) from E_CRO where CRO_NUM = '" + numExpediente + "' and "
                    + "CRO_TRA = " + codTramite + " and CRO_MUN = " + codOrganizacion;
            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                ocurrencia = rs.getInt(1);
            }//while(rs.next())
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando la ocurrencia " + e.getMessage());
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }//try-catch-finally
        log.debug("getOcurrencia() : END");
        return ocurrencia;
    }//getExpediente

    public void grabarIdTerceroPasikus(TerceroPasarela datosTercero, String codProcedimiento, Integer codOrganizacion, Connection con) throws SQLException {
        log.info("grabarIdTercero() : BEGIN");
        Statement st = null;
        PreparedStatement ps = null;
        String codigoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                + MeIkus01Constantes.CAMPO_IKUS_TER_ID, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

        try {
            String sql = "merge into T_CAMPOS_TEXTO using (select 1 from DUAL) "
                    + " on (COD_CAMPO=? and COD_MUNICIPIO=? and COD_TERCERO=?)"
                    + " when matched then update set VALOR=?"
                    + " when not matched then insert (COD_CAMPO, COD_MUNICIPIO, COD_TERCERO, VALOR)"
                    + " values(?,?,?,?)";
            log.debug("sql = " + sql);
            ps = con.prepareStatement(sql);
            ps.setString(1, codigoCampo);
            ps.setInt(2, codOrganizacion);
            ps.setInt(3, datosTercero.getCodTercero());
            ps.setString(4, datosTercero.getIdTerceroIkus());
            ps.setString(5, codigoCampo);
            ps.setInt(6, codOrganizacion);
            ps.setInt(7, datosTercero.getCodTercero());
            ps.setString(8, datosTercero.getIdTerceroIkus());
            ps.executeQuery();
        } catch (SQLException e) {
            log.error("Se ha producido un error anhadiendo el id del tercero en PASIKUS generado por la pasarela de pagos", e);
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    public String getCodigoProvincia(String codTercero, String numExpediente, Integer codOrganizacion, Connection con) throws Exception {
        log.info("getCodigoProvincia() : BEGIN - " + numExpediente + " - Cod 3º: " + codTercero);
        ResultSet rs = null;
        PreparedStatement ps = null;

        String codProv = "";
        try {
            String sql = "select DNN_PRV from E_EXT "
                    + "left join T_DNN DOM on DNN_DOM=EXT_DOT "
                    + " where EXT_TER= ? and EXT_NUM= ?";
            log.info(sql);
            ps = con.prepareStatement(sql);
            ps.setString(1, codTercero);
            ps.setString(2, numExpediente);
            rs = ps.executeQuery();

            while (rs.next()) {
                codProv = rs.getString("DNN_PRV");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el código de Provincia - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info("getCodigoProvincia() :  " + codProv);
        return codProv;
    }

    
    public String getCodigoPostal(String codTercero, String numExpediente, Integer codOrganizacion, Connection con) throws Exception {
        log.info("getCodigoPostal() : BEGIN - " + numExpediente + " - Cod 3º: " + codTercero);
        ResultSet rs = null;
        PreparedStatement ps = null;

        String codPostal = "";
        try {
            String sql = "select DNN_CPO from E_EXT "
                    + "left join T_DNN DOM on DNN_DOM=EXT_DOT "
                    + " where EXT_TER= ? and EXT_NUM= ? and EXT_ROL=1";
            log.info(sql);
            ps = con.prepareStatement(sql);
            ps.setString(1, codTercero);
            ps.setString(2, numExpediente);
            rs = ps.executeQuery();

            while (rs.next()) {
                codPostal = rs.getString("DNN_CPO");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el código de Provincia - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info("getCodigoPostal() :  " + codPostal);
        return codPostal;
    }
}//class
