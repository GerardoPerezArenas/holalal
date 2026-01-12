/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide07.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConstantesMeLanbide07;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide07DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide07DAO.class);

    //Instancia
    private static MeLanbide07DAO instance = null;

    private MeLanbide07DAO() {

    }

    public static MeLanbide07DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide07DAO.class) {
                instance = new MeLanbide07DAO();
            }
        }
        return instance;
    }

    public Calendar getValorCampoFechaExpediente(int codOrg, String numExp, String ejercicio, String codCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Calendar resultado = null;
            String query = null;
        try {
            query = "SELECT TFE_VALOR FROM " + ConstantesMeLanbide07.TABLA_E_TFE
                    + " WHERE tfe_cod='" + codCampo + "'"
                    + " AND tfe_num='" + numExp + "'"
                    + " AND tfe_mun=" + codOrg
                    + " AND tfe_eje='" + ejercicio + "'";
            log.debug("getValorCampoFechaExpediente. sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                if (rs.getDate("TFE_VALOR") != null) {
                    resultado = Calendar.getInstance();
                    resultado.setTime(rs.getDate("TFE_VALOR"));
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la Fecha de Vencimiento de la Carta de Pago", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return resultado;
    }

    public String getValorCampoTextoExpediente(int codOrg, String numExp, String ejercicio, String codCampo, String codProc, Connection con) throws Exception {
        log.debug("getValorCampoTextoExpediente BEGIN " + codCampo);
        Statement st = null;
        ResultSet rs = null;
        String resultado = null;
        try {
            String query = null;
            query = "SELECT TXT_VALOR FROM " + ConstantesMeLanbide07.TABLA_E_TXT
                    + " WHERE TXT_MUN='" + codOrg + "' "
                    + " AND TXT_EJE='" + ejercicio + "' "
                    + " AND TXT_NUM='" + numExp + "'"
                    + " AND TXT_COD='" + codCampo + "' ";
            log.debug("sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                resultado = rs.getString("TXT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el Id de la deuda", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
            log.debug("dameIdDeuda END");
        }
        return resultado;
    }

    public BigDecimal getValorCampoNumericoTramite(int codOrg, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;

        try {
            String query = null;
            query = "select TNUT_VALOR from " + ConstantesMeLanbide07.TABLA_E_TNUT
                    + " where TNUT_MUN = '" + codOrg + "' and TNUT_EJE = '" + ejercicio
                    + "' and TNUT_NUM = '" + numExp + "' and TNUT_TRA ='" + tramite + "' and TNUT_COD = '" + codigoCampo + "'"
                    + " and tnut_ocu=" + ocurrencia;
            log.debug("sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getBigDecimal("TNUT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
    }
        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }// get numerico tramite

    public String getValorCampoTextoTramite(int codOrg, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;

        try {
            String query = null;
            query = "select TXTT_VALOR from " + ConstantesMeLanbide07.TABLA_E_TXTT
                    + " where TXTT_MUN = '" + codOrg + "' and TXTT_EJE = '" + ejercicio
                    + "' and TXTT_NUM = '" + numExp + "' and TXTT_TRA ='" + tramite + "' and TXTT_COD = '" + codigoCampo + "'"
                    + " and TXTT_ocu=" + ocurrencia;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TXTT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario texto " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }// get numerico tramite

    /**
     *
     * @param codOrg
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codigo
     * @param con
     * @return VALOR DEL DESPLEGABLE
     * @throws Exception
     */
    public String getValorDesplegableTramite(int codOrg, String numExp, String tramite, String ocurrencia, String codigo, Connection con) throws Exception {
        log.info("getValorDesplegableExp - ini()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String valorCampo = null;
        String sql = null;
        try {
            sql = "SELECT TDET_VALOR FROM  " + ConstantesMeLanbide07.TABLA_E_TDET
                    + " WHERE TDET_MUN=? AND TDET_PRO=? AND TDET_EJE=? AND TDET_NUM=? "
                    + " AND TDET_TRA=? AND TDET_OCU=? AND TDET_COD=?";
            log.debug("getValorDesplegableExp - sql =>  " + sql);
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[1]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, tramite);
            ps.setString(i++, ocurrencia);
            ps.setString(i++, codigo);
            rs = ps.executeQuery();
            if (rs.next()) {
                valorCampo = rs.getString("TDET_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Excepcion - getValorDesplegableExp : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
                if (ps != null) {
                    ps.close();
                }
            if (rs != null) {
                rs.close();
            }
        }
        return valorCampo;
    }

    public String getValorCampoDesplegableExternoTramite(int codOrg, String numExp, String ejercicio, int tramite, int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDEXT_VALOR from " + ConstantesMeLanbide07.TABLA_E_TDEXT
                    + " where TDEXT_MUN = '" + codOrg + "' and TDEXT_EJE = '" + ejercicio
                    + "' and TDEXT_NUM = '" + numExp + "' and TDEXT_COD = '" + codigoCampo + "'"
                    + " and tdext_ocu=" + ocurrencia;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDEXT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario  " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor;
    }

    public String getNombreFicheroExpediente(int codOrg, String codProcedimiento, String ejercicio, String numExpediente, String codCampo, Connection con) throws Exception {
        log.debug("ENTRA en getNombreFicheroExpediente DAO");
        String nombreCarta = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = "";
        try {
            sql = "select TFI_NOMFICH from " + ConstantesMeLanbide07.TABLA_E_TFI
                    + " where TFI_MUN=" + codOrg
                    + " and TFI_EJE =" + ejercicio
                    + " and TFI_NUM='" + numExpediente + "'"
                    + " and TFI_COD='" + codCampo + "'";
            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                nombreCarta = rs.getString("TFI_NOMFICH");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el nombre de la carta de pago ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("getNombreFicheroExpediente DAO nos devuelve: " + nombreCarta);
        return nombreCarta;
    }

    public String getEstadoDeudaZORKU(String idDeuda, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = null;
        try {
            String query = null;
            query = "select COD_ESTADO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.VISTA_DEUDAS_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " where NUM_LIQUIDACION = ?";

            log.debug("sql = " + query);
            log.debug("parametros pasados a la query = " + idDeuda);
            ps = con.prepareStatement(query);
            ps.setString(1, idDeuda);
            rs = ps.executeQuery();

            while (rs.next()) {
                resultado = rs.getString("COD_ESTADO");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el Id de la deuda", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return resultado;
    }

    public List<String> getExpedientesEnEspera(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String resultado = null;
        List<String> expedientes = new ArrayList<String>();
        try {
            StringBuilder sb = new StringBuilder("SELECT ecro1.CRO_TRA, ecro1.CRO_NUM, TXT_VALOR AS IDDEUDA, SYSDATE, FECHA_LIMITE_PAGO  ");
            sb.append(" FROM ").append(ConstantesMeLanbide07.TABLA_E_CRO).append(" ecro1 ");
            sb.append(" INNER JOIN ").append(ConstantesMeLanbide07.TABLA_E_TXT).append(" ON TXT_NUM = ecro1.CRO_NUM AND TXT_COD='IDDEUDA' AND txt_mun = ecro1.cro_mun AND txt_eje = ecro1.cro_eje");
            sb.append(" INNER JOIN ").append(ConfigurationParameter.getParameter(ConstantesMeLanbide07.VISTA_DEUDAS_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES)).append(" ON NUM_LIQUIDACION = txt_valor ");
            sb.append(" WHERE ecro1.CRO_PRO='REINT'  AND ecro1.CRO_FEF IS NULL ");
            sb.append(" AND ecro1.CRO_TRA='").append(ConfigurationParameter.getParameter(ConstantesMeLanbide07.TRAMITE_PAGO_VOLUNTARIO, ConstantesMeLanbide07.FICHERO_PROPIEDADES)).append("'");
            sb.append(" AND SYSDATE> FECHA_LIMITE_PAGO + 14  ");
            sb.append(" AND INTEGRA_SIPCA='1' ");
            // en estado 1 pendiente o 28 caducada 
            sb.append(" AND COD_ESTADO IN (1,28)  ");
            // no FRACCIONADA 
            sb.append("  and COD_TIPOPAGO = 1 ");
//            sb.append(" AND ecro1.CRO_NUM NOT IN (SELECT ecro2.CRO_NUM FROM E_CRO ecro2 WHERE ecro2.CRO_TRA =");
//            sb.append(ConfigurationParameter.getParameter(ConstantesMeLanbide07.TRAMITE_SOL_FRACC, ConstantesMeLanbide07.FICHERO_PROPIEDADES));
//            sb.append(" AND ecro2.CRO_PRO = 'REINT' ");
//            sb.append(" AND ecro2.CRO_OCU = 1 AND ecro2.cro_eje = ecro1.cro_eje AND ecro2.cro_mun = ecro1.cro_mun)");
            // En SUSPENSION PERIODO PAGO
            sb.append(" AND ecro1.CRO_NUM NOT IN (SELECT ecro3.CRO_NUM FROM E_CRO ecro3 WHERE ecro3.CRO_TRA =");
            sb.append(ConfigurationParameter.getParameter(ConstantesMeLanbide07.TRAMITE_SUSPENSION, ConstantesMeLanbide07.FICHERO_PROPIEDADES));
            sb.append(" AND ecro3.CRO_PRO = 'REINT' ");
            sb.append(" AND ecro3.CRO_OCU = 1 AND ecro3.cro_eje = ecro1.cro_eje AND ecro3.cro_mun = ecro1.cro_mun AND ecro3.cro_fef IS NULL)");
            sb.append(" ORDER BY ecro1.cro_num");
            log.debug("sql = " + sb.toString());

            st = con.createStatement();
            rs = st.executeQuery(sb.toString());
            while (rs.next()) {
                resultado = rs.getString("CRO_NUM");
                expedientes.add(resultado);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los expedientes", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return expedientes;
    }

    public int guardarValorCampoNumericoTramite(int codOrg, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, BigDecimal valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;

        try {
            boolean nuevo = false;
            if (this.getValorCampoNumericoTramite(codOrg, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConstantesMeLanbide07.TABLA_E_TNUT
                        + " (TNUT_MUN, TNUT_PRO, TNUT_EJE, TNUT_NUM, TNUT_TRA,TNUT_OCU, TNUT_COD, TNUT_VALOR)"
                        + " values(" + codOrg
                        + ", '" + procedimiento + "'"
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", " + tramite
                        + ", " + ocurrencia
                        + ", '" + codigoCampo + "'"
                        + ", " + valor
                        + ")";
            } else {
                query = "update " + ConstantesMeLanbide07.TABLA_E_TNUT
                        + " set TNUT_VALOR = " + valor
                        + " where TNUT_MUN = '" + codOrg + "'"
                        + " and TNUT_EJE = " + ejercicio
                        + " and TNUT_NUM = '" + numExp + "'"
                        + " and TNUT_TRA = " + tramite
                        + " and TNUT_OCU = " + ocurrencia
                        + " and TNUT_COD = '" + codigoCampo + "'";
            }
            log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result > 0) {
                log.debug("Se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario de trámite tipo numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }// guardat valor numerico tramite

    public int guardarValorCampoTextoTramite(int codOrg, String procedimiento, String ejercicio, String numExp, int tramite, int ocurrencia, String codigoCampo, String valor, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        try {
            boolean nuevo = false;
            if (this.getValorCampoTextoTramite(codOrg, numExp, ejercicio, tramite, ocurrencia, codigoCampo, con) == null) {
                nuevo = true;
            }
            String query = null;
            if (nuevo) {
                query = "insert into " + ConstantesMeLanbide07.TABLA_E_TXTT
                        + " (TXTT_MUN, TXTT_PRO, TXTT_EJE, TXTT_NUM, TXTT_TRA,TXTT_OCU, TXTT_COD, TXTT_VALOR)"
                        + " values(" + codOrg
                        + ", '" + procedimiento + "'"
                        + ", " + ejercicio
                        + ", '" + numExp + "'"
                        + ", " + tramite
                        + ", " + ocurrencia
                        + ", '" + codigoCampo + "'"
                        + ", " + valor
                        + ")";
            } else {
                query = "update " + ConstantesMeLanbide07.TABLA_E_TXTT
                        + " set TXTT_VALOR = " + valor
                        + " where TXTT_MUN = '" + codOrg + "'"
                        + " and TXTT_EJE = " + ejercicio
                        + " and TXTT_NUM = '" + numExp + "'"
                        + " and TXTT_TRA = " + tramite
                        + " and TXTT_OCU = " + ocurrencia
                        + " and TXTT_COD = '" + codigoCampo + "'";
            }
            log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
            if (result != 0) {
                log.debug("Se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            } else {
                log.error("NO se ha grabado el campo suplementario " + codigoCampo + " del trámite " + tramite);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando el campo suplementario de trámite tipo texto " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return result;
    }// guardat valor numerico tramite

    public /*String*/ void actualizarPagoDeuda(Connection con) throws Exception {
        CallableStatement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "call UPDATE_PAGO_DEUDA_EXPTS_REINT()";
            log.debug("sql = " + query);

            st = con.prepareCall(query);
            int i = st.executeUpdate();

        } catch (SQLException ex) {
            log.error("Se ha producido un error en actualizarPagoDeuda ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public String getCodigoInternoTramite(int codOrg, String codProc, String codTramite, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try {
            String query = "select TRA_COD from " + ConstantesMeLanbide07.TABLA_E_TRA
                    + " where TRA_MUN = " + codOrg
                    + " and TRA_PRO = '" + codProc + "'"
                    + " and TRA_COU = " + codTramite
                    + " and TRA_FBA IS NULL";
            log.debug("sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getLong("TRA_COD");
                if (rs.wasNull()) {
                    valor = null;
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el código interno del trámite " + codTramite, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor != null ? valor.toString() : null;
    }

    public Long obtenerCodigoInternoTramite(String procedimiento, String codExternoTramite, Connection con) throws Exception {
        Long cod = 0L;
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "select tra_cod from " + ConstantesMeLanbide07.TABLA_E_TRA
                    + " where tra_pro='" + procedimiento + "' and tra_cou=" + codExternoTramite;
            log.debug("sql = " + sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                cod = rs.getLong("tra_cod");
                if (rs.wasNull()) {
                    cod = 0L;
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el código interno del trámite externo: " + codExternoTramite + " del procedimiento: " + procedimiento, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return cod;
    }

    /**
     * Metodo que recupera la ultima ocurrencia de un tramite para un expediente
     *
     * @param codOrg
     * @param procedimiento
     * @param ejercicio
     * @param numExp
     * @param tram
     * @param con
     * @return
     * @throws Exception
     */
    public int getMaxOcurrenciaTramitexCodigo(int codOrg, String procedimiento, String ejercicio, String numExp, int tram, Connection con) throws Exception {
        log.info("getMaxOcurrenciaTramitexCodigo - ini()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int ocurrencia = 1;
        try {
            String sql = "SELECT MAX(CRO_OCU) AS ocurrencia FROM " + ConstantesMeLanbide07.TABLA_E_CRO
                    + " WHERE CRO_NUM=? AND CRO_PRO=? AND CRO_MUN=? AND CRO_EJE=? AND CRO_TRA=?";

            //  log.debug("getMaxOcurrenciaTramitexCodigo - sql = " + sql);
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setString(i++, numExp);
            ps.setString(i++, procedimiento);
            ps.setInt(i++, codOrg);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setInt(i++, tram);
            rs = ps.executeQuery();
            while (rs.next()) {
                ocurrencia = rs.getInt("ocurrencia");
            }
        } catch (NumberFormatException ex) {
            log.error("Excepcion - getMaxOcurrenciaTramitexCodigo : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } catch (SQLException ex) {
            log.error("Excepcion - getMaxOcurrenciaTramitexCodigo : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error(" getMaxOcurrenciaTramitexCodigo - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.debug("getMaxOcurrenciaTramitexCodigo - end() - " + ocurrencia);
        return ocurrencia;
    }

    public String cerrarTramite(int codOrg, String numExp, String codTramite, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try {
            String query = "UPDATE " + ConstantesMeLanbide07.TABLA_E_CRO
                    + " set CRO_FEF=SYSDATE"
                    + ", CRO_OBS = cro_obs  || chr(13) || 'Trámite cerrado por job de Envío a Vía Ejecutiva'"
                    + ", CRO_USF = 5"
                    + " where CRO_MUN = " + codOrg
                    + " and CRO_PRO = 'REINT' "
                    + " and CRO_NUM = '" + numExp + "'"
                    + " and CRO_TRA = " + codTramite;
            log.debug("sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);

        } catch (SQLException ex) {
            log.error("Se ha producido un error al cerrar el trámite " + codTramite, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor != null ? valor.toString() : null;
    }

    public String abrirTramite(int codOrg, String numExp, String codTramite, String uor, Connection con) throws Exception {
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try {
            if (numExp != null && !"".equals(numExp)) {
                String datos[] = numExp.split("/");
                String query = "INSERT into " + ConstantesMeLanbide07.TABLA_E_CRO
                        + " (CRO_PRO,CRO_EJE,CRO_NUM, CRO_TRA,CRO_FEI,CRO_USU,CRO_UTR,CRO_MUN, CRO_OCU) "
                        + " values ('REINT',?,?,?,SYSDATE,5,?,?,1)";
                log.debug("sql = " + query);

                ps = con.prepareStatement(query);
                log.debug("parametro 1 = " + Integer.valueOf(datos[0]));
                ps.setInt(1, Integer.parseInt(datos[0]));
                log.debug("parametro 2 = " + numExp);
                ps.setString(2, numExp);
                log.debug("parametro 3 = " + codTramite);
                ps.setString(3, codTramite);
                log.debug("parametro 4 = " + uor);
                ps.setString(4, uor);
                log.debug("parametro 5 = " + codOrg);
                ps.setInt(5, codOrg);
                rs = ps.executeQuery();
            }
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error al insertar el trámite " + codTramite, ex);
            throw new Exception(ex);
        } catch (SQLException ex) {
            log.error("Se ha producido un error al insertar el trámite " + codTramite, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return valor != null ? valor.toString() : null;
    }

    public String cerrarExpediente(int codOrg, String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try {
            StringBuffer query = new StringBuffer("UPDATE ").append(ConstantesMeLanbide07.TABLA_E_EXP).append(" set EXP_EST=9, EXP_FEF=SYSDATE, EXP_OBS=EXP_OBS || chr(13) || 'Expediente cerrado por job de Envío a Vía Ejecutiva' ");
            query.append(" where EXP_MUN = ").append(codOrg);
            query.append(" and EXP_PRO = 'REINT' ");
            query.append(" and EXP_NUM = '").append(numExp).append("'");
            log.debug("sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query.toString());

        } catch (SQLException ex) {
            log.error("Se ha producido un error al cerrar el EXPEDIENTE " + numExp, ex);
            throw new Exception(ex);
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
                throw new Exception(e);
            }
        }
        return valor != null ? valor.toString() : null;
    }

    public String obtenerUnidadExpediente(String numExp, Connection con) throws Exception {
        String cod = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select EXP_UOR from " + ConstantesMeLanbide07.TABLA_E_EXP + " where EXP_NUM = ?";
            log.debug("sql = " + sql);
            ps = con.prepareStatement(sql);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            if (rs.next()) {
                cod = rs.getString("EXP_UOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la unidad organizativa : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return cod;
    }

    public Date getValorFechaExpREINT(String numExp, String codigo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Date fechaResultado = null;
        String sql = "";
        try {
            sql = "SELECT tfe_valor FROM " + ConstantesMeLanbide07.TABLA_E_TFE
                    + " WHERE tfe_cod='" + codigo + "'"
                    + " AND tfe_num='" + numExp + "'";
            log.debug("sql = " + sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            fechaResultado = rs.getDate(1);

        } catch (SQLException ex) {
            log.error("Se ha producido un error obteniendo la fecha expediente REINT del Expediente : " + numExp + ", codigo: " + codigo, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return fechaResultado;
    }

    public Date getValorFechaTramREINT(String numExp, String tabla, String tramite, String codigo, Connection con) throws Exception {
        if (log.isDebugEnabled()) {
            log.info("obtenerFechaTramREINT ( numExpediente = " + numExp + " tabla = " + tabla + " tramite = " + tramite + " codigo = " + codigo + " )");
        }
        Statement st = null;
        ResultSet rs = null;
        Date fechaResultado = null;
        String sql = "";
        try {
            if (ConstantesMeLanbide07.TABLA_E_TFET.equals(tabla)) {
                sql = "select tfet_valor from " + tabla
                        + " where tfet_tra = " + tramite
                        + " and tfet_cod ='" + codigo + "'"
                        + " and tfet_num = '" + numExp + "'";
            } else if (ConstantesMeLanbide07.TABLA_E_TFECT.equals(tabla)) {
                sql = "select tfect_valor from " + tabla
                        + " where tfect_tra = " + tramite
                        + " and tfect_cod ='" + codigo + "'"
                        + " and tfect_num = '" + numExp + "'";
            }
            log.debug("sql = " + sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            fechaResultado = rs.getDate(1);

        } catch (SQLException ex) {
            log.error("Se ha producido un error obteniendo la fecha tramite REINT del Expediente : " + numExp + ", tramite: " + tramite + ", codigo: " + codigo, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return fechaResultado;
    }

    public int insertarRegistroProcesosProgramadosEjec(Date fechaInicioJob, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        int idSecuencia;
        try {
            idSecuencia = this.getNextId(ConstantesMeLanbide07.SEQ_PROCESOS_PROGRAMADOS_EJEC, con);
            String query = "INSERT into  " + ConstantesMeLanbide07.TABLA_PROCESOS_PROGRAMADOS + "  (ID, COD_PROCESO,INICIO,FIN,RESULTADO,REGISTROS_PROCESADOS,DETALLE_ERROR,OBSERVACIONES) "
                    + "values (?,'P_VIA_EJEC',?,null,null,null,null,null)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            log.debug("parametro 1 = " + idSecuencia);
            ps.setInt(1, idSecuencia);
            log.debug("parametro 2 = " + formateadorFecha.format(fechaInicioJob));
            ps.setTimestamp(2, new java.sql.Timestamp(fechaInicioJob.getTime()));
            rs = ps.executeQuery();
        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar el proceso programado P_VIA_EJEC ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return idSecuencia;
    }

    public void modificarRegistroProcesosProgramadosEjec(int idSecuencia, Date fechaFinJob, String resultadoJob, int registrosProcesadosOK, int registrosProcesadosNOOK, String mensajeErrorJob, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        int registrosProcesados = registrosProcesadosOK + registrosProcesadosNOOK;
        String observacionesJob = "Nº Expedientes tratados: " + registrosProcesados + ". ";
        if (registrosProcesadosOK > 0) {
            observacionesJob = observacionesJob + "Nº Expedientes correctos: " + registrosProcesadosOK + ". ";
        }
        if (registrosProcesadosNOOK > 0) {
            observacionesJob = observacionesJob + "Nº Expedientes incorrectos: " + registrosProcesadosNOOK + ". ";
        }

        try {
            String query = "UPDATE " + ConstantesMeLanbide07.TABLA_PROCESOS_PROGRAMADOS + " SET FIN=?, RESULTADO=?, REGISTROS_PROCESADOS=?, DETALLE_ERROR=?, OBSERVACIONES=? where ID=?";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);

            log.debug("parametro 1 = " + formateadorFecha.format(fechaFinJob));
            ps.setTimestamp(1, new java.sql.Timestamp(fechaFinJob.getTime()));
            log.debug("parametro 2 = " + resultadoJob);
            ps.setString(2, resultadoJob);
            log.debug("parametro 3 = " + registrosProcesados);
            ps.setInt(3, registrosProcesados);
            log.debug("parametro 4 = " + mensajeErrorJob);
            ps.setString(4, mensajeErrorJob);
            log.debug("parametro 5 = " + observacionesJob);
            ps.setString(5, observacionesJob);
            log.debug("parametro 6 = " + idSecuencia);
            ps.setInt(6, idSecuencia);
            rs = ps.executeQuery();
        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar el proceso programado P_VIA_EJEC ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    private Integer getNextId(String seqName, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Integer numSec = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select " + seqName + ".nextval from dual";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numSec = rs.getInt(1);
                if (rs.wasNull()) {
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error generando el siguiente valor de la secuencia " + seqName, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return numSec;
    }

    public void insertarRegistroProcesosProgramadosViaEjeREINT(Integer idSecuenciaProcesoProgramado, Date fecha, String numExpediente, String idDeuda, String detalleError, int error, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        int idSecuencia;
        try {
            idSecuencia = this.getNextId(ConstantesMeLanbide07.SEQ_PROC_PROG_VIAEJE_REINT, con);
            String query = "INSERT into  " + ConstantesMeLanbide07.TABLA_PROC_PROG_VIAEJE + "  (ID, ID_PROCESO, FECHA_OPERACION, NUMERO_EXPEDIENTE, ID_DEUDA, DETALLE_ERROR, ERROR)values (?,?,?,?,?,?,?)";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            log.debug("parametro 1 = " + idSecuencia);
            ps.setInt(1, idSecuencia);
            log.debug("ID_PROCESO      = " + idSecuenciaProcesoProgramado);
            ps.setInt(2, idSecuenciaProcesoProgramado);
            log.debug("FECHA_OPERACION = " + formateadorFecha.format(fecha));
            ps.setTimestamp(3, new java.sql.Timestamp(fecha.getTime()));
            log.debug("EXPEDIENTE      = " + numExpediente);
            ps.setString(4, numExpediente);
            log.debug("ID_DEUDA        = " + idDeuda);
            ps.setString(5, idDeuda);
            log.debug("parametro 6 = " + detalleError);
            ps.setString(6, detalleError);
            log.debug("parametro 7 = " + error);
            ps.setInt(7, error);

            rs = ps.executeQuery();
        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar el proceso programado via ejecutiva reint: PROC_PROG_VIAEJE_REINT ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean expedienteTieneTipoNotificacion(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = "";

        try {
            String query = "SELECT TDET_VALOR FROM " + ConstantesMeLanbide07.TABLA_E_EXP
                    + " LEFT JOIN " + ConstantesMeLanbide07.TABLA_E_TDET + " TN  ON TN.TDET_NUM=EXP_NUM AND TN.TDET_COD='TIPONOTIF' "
                    + " WHERE EXP_NUM = '" + numExp + "'";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                resultado = rs.getString("TDET_VALOR");
            }
            return resultado != null;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " tiene Tipo Notificacion. ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean existeFechaTramREINT(String numExp, String tabla, String tramite, int ocurrencia, String codigo, Connection con) throws Exception {
        log.info("existeFechaTramREINT ( numExpediente = " + numExp + " tabla = " + tabla + " tramite = " + tramite + " codigo = " + codigo + " )");
        Statement st = null;
        ResultSet rs = null;
        int resultado = 0;
        String sql = "";

        try {
            if (ConstantesMeLanbide07.TABLA_E_TFET.equals(tabla)) {
                sql = "select count(*) from " + tabla
                        + " where tfet_tra = " + tramite
                        + " and TFET_OCU = " + ocurrencia
                        + " and tfet_cod ='" + codigo + "'"
                        + " and tfet_num = '" + numExp + "'";
            } else if (ConstantesMeLanbide07.TABLA_E_TFECT.equals(tabla)) {
                sql = "select count(*) from " + tabla
                        + " where tfect_tra = " + tramite
                        + " and TFECT_OCU = " + ocurrencia
                        + " and tfect_cod ='" + codigo + "'"
                        + " and tfect_num = '" + numExp + "'";
            }
            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            resultado = rs.getInt(1);
            return resultado > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar " + codigo + " de la tabla " + tabla + "del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean expedientePagadoZORKU(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = "";
        try {
            String query = "SELECT FECHA_PAGO FROM " + ConstantesMeLanbide07.TABLA_E_EXP
                    + " INNER JOIN " + ConstantesMeLanbide07.TABLA_E_TXT + " ON TXT_NUM=EXP_NUM AND TXT_COD='IDDEUDA' "
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.VISTA_DEUDAS_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " ON NUM_LIQUIDACION=TXT_VALOR "
                    + " WHERE EXP_NUM = '" + numExp + "'";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getString("FECHA_PAGO");
            }
            return resultado != null;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " estï¿½ pagado. ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean expedienteEnViaEjecutivaZORKU(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = "";
        try {
            String query = "SELECT FECHA_EJECUTIVA FROM " + ConstantesMeLanbide07.TABLA_E_EXP
                    + " INNER JOIN " + ConstantesMeLanbide07.TABLA_E_TXT + " ON TXT_NUM=EXP_NUM AND TXT_COD='IDDEUDA' "
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.VISTA_DEUDAS_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " ON NUM_LIQUIDACION=TXT_VALOR "
                    + " WHERE EXP_NUM = '" + numExp + "'";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getString("FECHA_EJECUTIVA");
            }
            return resultado != null;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " está en Via Ejecutiva. ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean tienePagoFraccionadoZORKU(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer resultado = 0;
        try {
            String query = "SELECT COD_TIPOPAGO FROM " + ConstantesMeLanbide07.TABLA_E_EXP
                    + " INNER JOIN " + ConstantesMeLanbide07.TABLA_E_TXT + " ON TXT_NUM=EXP_NUM AND TXT_COD='IDDEUDA' "
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.VISTA_DEUDAS_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " ON NUM_LIQUIDACION=TXT_VALOR "
                    + " WHERE EXP_NUM = '" + numExp + "'";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getInt("COD_TIPOPAGO");
            }
            return resultado == 3;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " está fraccionado. ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean expedienteAnuladoZORKU(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = "";
        try {
            String query = "SELECT FECHA_ANULACION FROM " + ConstantesMeLanbide07.TABLA_E_EXP
                    + " INNER JOIN " + ConstantesMeLanbide07.TABLA_E_TXT + " ON TXT_NUM=EXP_NUM AND TXT_COD='IDDEUDA' "
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.VISTA_DEUDAS_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " ON NUM_LIQUIDACION=TXT_VALOR AND COD_ESTADO !=28"
                    + " WHERE EXP_NUM = '" + numExp + "'";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                resultado = rs.getString("FECHA_ANULACION");
                log.debug("Resultado = " + resultado);
            } else {
                log.debug("NO esta anulada:");
                return false;
            }
            if (resultado == null || !"".equalsIgnoreCase(resultado)) {
                log.debug("NO esta anulada:");
                return false;
            } else {
                log.debug(" esta anulada:");
                return true;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " está anulado. ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public boolean tieneTramiteAbierto(int codOrg, String numExp, String tramite, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = 0;
        try {
            String query = "SELECT COUNT(*) AS CONT FROM E_CRO WHERE CRO_MUN = ? and CRO_PRO=? AND CRO_NUM=? AND CRO_TRA=? AND CRO_FEI IS NOT NULL";
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, tramite);
            log.debug(query);
            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
            return resultado > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " tiene un trámite abierto. ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public String getCampoMotivoAnulacionDesplegable(String numExp, int ocurrencia, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String motdeuda = "";
        try {
            String query = "select TDEXT_CODSEL from " + ConstantesMeLanbide07.TABLA_E_TDEXT
                    + " where TDEXT_NUM='" + numExp + "' and TDEXT_OCU=" + ocurrencia + " and TDEXT_COD = 'MOTANULDEUDA'";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                motdeuda = rs.getString("TDEXT_CODSEL");
            }
            return motdeuda;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recumerar el motivo de anulación en el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

//    public String getCampoFECSOLANDEUINI(String numExp, Connection con) throws SQLException, Exception {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        String fecha = "";
//        try {
//            String query = "select tfe_valor from " + ConstantesMeLanbide07.TABLA_E_TFE
//                    + " where tfe_cod='FECSOLANDEUINI' and tfe_num='" + numExp + "'";
//            log.debug("sql = " + query);
//
//            ps = con.prepareStatement(query);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                fecha = rs.getString("tfe_valor");
//            }
//            return fecha;
//        } catch (SQLException ex) {
//            log.error("Se ha producido un error getcampo FECSOLANDEUINI ", ex);
//            throw new Exception(ex);
//        } finally {
//            if (ps != null) {
//                ps.close();
//            }
//            if (rs != null) {
//                rs.close();
//            }
//        }
//    }
    public String getFechaAnulacionZORKU(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String fecha = "";
        try {
            String query = "SELECT TXT_VALOR, FECHA_ANULACION FROM " + ConstantesMeLanbide07.TABLA_E_TXT
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.VISTA_DEUDAS_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " ON NUM_LIQUIDACION=TXT_VALOR "
                    + " WHERE TXT_NUM='" + numExp + "' AND TXT_COD='IDDEUDAINI'";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                fecha = rs.getString("FECHA_ANULACION");
            }
            return fecha;
        } catch (SQLException ex) {
            log.error("Se ha producido un error get FECHA_ANULACION ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     *
     * Metodo que graba un campo tipo fichero de expediente
     *
     * @param codOrg
     * @param codProcedimiento
     * @param ejercicio
     * @param numExpediente
     * @param codCampo
     * @param pdf
     * @param mime
     * @param nombreFichero
     * @param tamanhoFichero
     * @param con
     * @throws Exception
     */
    public void insertarDocumentoTFI(int codOrg, String codProcedimiento, String ejercicio, String numExpediente, String codCampo, byte[] pdf, String mime, String nombreFichero, int tamanhoFichero, Connection con) throws Exception {
        PreparedStatement ps = null;
        int resultado = 0;

        try {
            String query = "INSERT into " + ConstantesMeLanbide07.TABLA_E_TFI
                    + " (TFI_MUN, TFI_EJE, TFI_NUM, TFI_COD, TFI_VALOR, TFI_MIME, TFI_NOMFICH, TFI_TAMANHO) values (?,?,?,?,?,?,?,?)";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);

            log.debug("parametro 1 = " + codOrg);
            ps.setInt(1, codOrg);
            log.debug("parametro 2 = " + ejercicio);
            ps.setString(2, ejercicio);
            log.debug("parametro 3 = " + numExpediente);
            ps.setString(3, numExpediente);
            log.debug("parametro 4 = " + codCampo);
            ps.setString(4, codCampo);
            log.debug("parametro 5 = FICHERO" + nombreFichero);
            ps.setBytes(5, pdf);
            log.debug("parametro 6 = " + mime);
            ps.setString(6, mime);
            log.debug("parametro 7 = " + nombreFichero);
            ps.setString(7, nombreFichero);
            log.debug("parametro 8 = " + tamanhoFichero);
            ps.setInt(8, tamanhoFichero);
            resultado = ps.executeUpdate();

        } catch (SQLException e) {
            log.error("Se ha producido un error al insertar el documento ", e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        if (resultado != 0) {
            log.debug("CARTAPAGO grabada O.K.");
        }
    } // insertar documento

    public boolean grabarCartaDokusi(int codOrg, String numExp, String codCampo, String nombreFichero, String oid, Connection con) throws Exception {
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        boolean grabada = false;
        int resultado = 0;
        try {
            String query = "INSERT into " + ConstantesMeLanbide07.TABLA_E_TFI
                    + " (TFI_MUN, TFI_EJE, TFI_NUM, TFI_COD, TFI_MIME, TFI_NOMFICH) values (?,?,?,?,?,?)";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, codCampo);
            ps.setString(i++, "application/pdf");
            ps.setString(i++, nombreFichero);
            resultado = ps.executeUpdate();
            if (resultado > 0) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.TABLA_DOKUSI_DOCCSE, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                        + " (RELDOC_MUN, RELDOC_EJE, RELDOC_NUM, RELDOC_COD, RELDOC_OID) values (?, ?, ?, ?, ?)";
                log.debug("sql = " + query);
                ps1 = con.prepareStatement(query);
                i = 1;
                ps1.setInt(i++, codOrg);
                ps1.setString(i++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]);
                ps1.setString(i++, numExp);
                ps1.setString(i++, codCampo);
                ps1.setString(i++, oid);
                resultado = ps1.executeUpdate();
                if (resultado > 0) {
                    log.info("Carta grabada correctamente");
                    grabada = true;
                } else {
                    log.error("Se ha producido un error al insertar la relación del documento en DOKUSI");
                }
            } else {
                log.error("Se ha producido un error al insertar el documento ");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error al insertar el documento ", e);
            throw new Exception(e);
        } finally {
            if (ps1 != null) {
                ps1.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return grabada;
    }

    public boolean existeDocumentoTFI(String numExp, String codCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int resultado = -1;
        String sql = "";
        try {
            sql = "select count(*) from " + ConstantesMeLanbide07.TABLA_E_TFI
                    + " where tfi_num = '" + numExp + "'"
                    + " and tfi_cod = '" + codCampo + "'";
            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            resultado = rs.getInt(1);
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si existe el documento " + codCampo + " de la tabla E_TFI del expediente " + numExp, e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return resultado > 0;
    }//existe documento

    public int borrarRelacionDokusiCSE(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("borrarRelacionDokusiCSE BEGIN");
        PreparedStatement ps = null;
        String query = null;
        try {
            if (existeRelacionDokusiCSE(codOrg, numExp, codCampo, con)) {
                query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.TABLA_DOKUSI_DOCCSE, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                        + " where RELDOC_MUN = ? and RELDOC_EJE = ? and RELDOC_NUM= ? and RELDOC_COD= ?";
                log.debug("sql = " + query);
                int contador = 1;
                ps = con.prepareStatement(query);
                ps.setInt(contador++, codOrg);
                ps.setString(contador++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]);
                ps.setString(contador++, numExp);
                ps.setString(contador++, codCampo);
                return ps.executeUpdate();
            } else {
                log.debug("No está en DOKUSI");
                return 1;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando de RELDOC_DOCCSE la carta de pago " + codCampo + " del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public boolean existeRelacionDokusiCSE(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("existeRelacionDokusiCSE BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = -1;
        String query = "";
        try {
            query = "select count(*) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.TABLA_DOKUSI_DOCCSE, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " where RELDOC_MUN = ? and RELDOC_EJE = ? and RELDOC_NUM= ? and RELDOC_COD= ?";
            log.debug("sql = " + query);
            log.debug(codOrg + " - " + numExp + " - " + codCampo);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setString(contador++, codCampo);
            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
            //    return resultado > 0;
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si el documento " + codCampo + "  del expediente " + numExp + " está en DOKUSI", e);
            throw new Exception(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return resultado > 0;
    }//existe documento

    public String dameCodigoViaDomicilio(int codDomicilio, Connection con) throws Exception {
        log.info("---- ENTRA en dameCodigoViaDAO");
        String codigo = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        try {
            sql = "select TVI_ABR,TVI_COD"
                    + "	from T_TVI"
                    + "	where TVI_COD in"
                    + "	(select DNN_TVI"
                    + "		from T_DNN"
                    + "		where DNN_DOM = '"
                    + codDomicilio + "')";
            log.debug("SQL: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt("TVI_COD") != 0) {
                    codigo = rs.getString("TVI_ABR");
                }
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando el codigo del Tipo de Via. ", e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("dameCodigoViaDAO devuelve: " + codigo);
        return codigo;
    }

    public boolean coincideCodigo(String codigoVia, Connection con) throws Exception {
        log.info("---- ENTRA en coincideCodigo - DAO");
        Statement st = null;
        ResultSet rs = null;
        String sql = null;

        int resultado = -1;
        try {
            sql = "select count(*) from MELANBIDE07_VIA_NORA "
                    + " where CODIGO='" + codigoVia + "'";
            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            resultado = rs.getInt(1);
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si existe el codigo " + codigoVia + " ", e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return resultado > 0;
    }

    public int borrarNumericoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "delete from " + ConstantesMeLanbide07.TABLA_E_TNUT
                    + " where TNUT_MUN = ? and TNUT_PRO = ? and TNUT_EJE = ? and TNUT_NUM = ? and TNUT_TRA = ? and TNUT_OCU = ? and TNUT_COD = ?";
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[1]);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setInt(contador++, tramite);
            ps.setInt(contador++, ocurrencia);
            ps.setString(contador++, codCampo);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando el campo " + codCampo + ":  del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public int borrarFicheroExpediente(String numExp, String codCampoFichero, Connection con) throws SQLException, Exception {
        Statement st = null;
        String query = null;
        try {
            query = "delete from " + ConstantesMeLanbide07.TABLA_E_TFI
                    + " where TFI_NUM='" + numExp + "'"
                    + " and TFI_COD='" + codCampoFichero + "'";
            log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error Eliminando la carta de pago " + codCampoFichero + " del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public int borrarTextoExpediente(String numExp, String codDeudaABorrar, String iddeuda, Connection con) throws SQLException, Exception {
        Statement st = null;
        String query = null;
        try {
            query = "delete from " + ConstantesMeLanbide07.TABLA_E_TXT
                    + " where TXT_NUM='" + numExp + "'"
                    + " and TXT_COD='" + codDeudaABorrar + "'"
                    + " and TXT_VALOR='" + iddeuda + "'";
            log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error Eliminando el campo " + codDeudaABorrar + ": " + iddeuda + " del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public int borrarFechaExpediente(String numExp, String codCampoFec, Connection con) throws SQLException, Exception {
        Statement st = null;
        String query = null;
        try {
            query = "delete from " + ConstantesMeLanbide07.TABLA_E_TFE
                    + " where TFE_NUM='" + numExp + "' "
                    + "and TFE_COD='" + codCampoFec + "' ";
            log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error Eliminando el campo " + codCampoFec + " del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public static int getUsuarioExpediente(int codOrg, String numExp, String codProcedimiento, int codTramite, int ocurrenciaTramite, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        int respuesta = 0;
        log.debug("Numero del tramite en el que estamos:" + codTramite + " - Ocurrencia: " + ocurrenciaTramite);
        try {
            query = "select CRO_USU from " + ConstantesMeLanbide07.TABLA_E_CRO
                    + " where cro_pro='" + codProcedimiento + "' "
                    + "AND CRO_NUM='" + numExp + "'"
                    + " and CRO_TRA=" + codTramite + ""
                    + " and CRO_OCU=" + ocurrenciaTramite;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                respuesta = rs.getInt("CRO_USU");
                log.debug(" usuario del tamite que estamos:" + respuesta);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando getUsuarioExpediente ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return respuesta;
    }

    public String getNombreUsuario(int usuario, Connection con) throws Exception {
        log.info("getNombreUsuario - ini()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String nombre = null;
            String query = null;
        try {
            query = "select USU_NOM from " + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide07.TABLA_USUARIOS, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " where USU_COD = ?";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, usuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                nombre = rs.getString("USU_NOM");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el nombre del usuario: " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
        }
        return nombre;
    }

    public String getImportePendienteZORKU(String numExp, String idDeuda, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        String valor = null;
        try {
            query = "select IMPORTE_PENDIENTE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.VISTA_DEUDAS_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " where NUM_LIQUIDACION =" + idDeuda;
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("IMPORTE_PENDIENTE");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el importe pendiente de la deuda: " + idDeuda, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return valor;
    }

    public boolean existeFechaExpREINT(String numExp, String tabla, String codigo, Connection con) throws Exception {
        log.info("existeFechaExpREINT ( numExpediente = " + numExp + " tabla = " + tabla + " codigo = " + codigo + " )");
        Statement st = null;
        ResultSet rs = null;
        int resultado = 0;
        String sql = "";

        try {
            if (ConstantesMeLanbide07.TABLA_E_TFE.equals(tabla)) {
                sql = "select count(*) from " + tabla
                        + " where tfe_cod ='" + codigo + "'"
                        + " and tfe_num = '" + numExp + "'";
            } else if (ConstantesMeLanbide07.TABLA_E_TFEC.equals(tabla)) {
                sql = "select count(*) from " + tabla
                        + " where tfec_cod ='" + codigo + "'"
                        + " and tfec_num = '" + numExp + "'";
            }

            log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            resultado = rs.getInt(1);
            return resultado > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recupera " + codigo + " de la tabla " + tabla + "del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * operacion que comprueba si la deuda esta abonada o no
     *
     * @param codOrg
     * @param ejercicio
     * @param numExp
     * @param con
     * @return booleano deuda abonada
     * @throws Exception
     */
    public boolean deudaAbonada(int codOrg, int ejercicio, String numExp, Connection con) throws Exception {
        log.info("deudaAbonada - ini()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean abonada = true;
        String resultado = null;
        try {
            String query = "SELECT TDE_VALOR FROM " + ConstantesMeLanbide07.TABLA_E_TDE
                    + " WHERE TDE_MUN=? AND TDE_EJE=? AND TDE_NUM=? AND TDE_COD='AYUDANOABO'";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setInt(i++, ejercicio);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getString("TDE_VALOR");
            }
            if (resultado != null && resultado.equalsIgnoreCase("X")) {
                abonada = false;
            }
        } catch (SQLException e) {
            log.error("Excepcion - deudaAbonada : " + e.getMessage(), e);
            throw new Exception(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error(" deudaAbonada - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return abonada;
    }

    // operacion que valida el número de expediente
    public String validarNumeroExpediente(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = "0";

        try {
            String query = "SELECT EXP_NUM FROM " + ConstantesMeLanbide07.TABLA_E_EXP
                    + " WHERE EXP_NUM = '" + numExp + "' union all SELECT EXP_NUM FROM " + ConstantesMeLanbide07.TABLA_HIST_E_EXP
                    + " WHERE EXP_NUM = '" + numExp + "'";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = "1";
            }
            return resultado;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " es válido ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     *
     * Metodo que comprueba si esta marcado un desplegable boolean del tipo SINO
     *
     * @param codOrg
     * @param numExp
     * @param codDesplegable
     * @param con
     * @return
     * @throws Exception
     */
    public boolean desplegableMarcadoConX(int codOrg, String numExp, String codDesplegable, Connection con) throws Exception {
        log.info("desplegableMarcadoConX - ini()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        boolean marcado = false;
        String resultado = null;
        try {
            query = "SELECT TDE_VALOR FROM " + ConstantesMeLanbide07.TABLA_E_TDE
                    + " WHERE TDE_MUN=? AND TDE_EJE=? AND TDE_NUM=? AND TDE_COD=?";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, codDesplegable);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getString("TDE_VALOR");
            }
            if (resultado != null && resultado.equalsIgnoreCase("X")) {
                marcado = true;
            }
        } catch (SQLException e) {
            log.error("Excepcion - desplegableMarcadoConX : " + e.getMessage(), e);
            throw new Exception(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error(" desplegableMarcadoConX - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return marcado;
    }

    /**
     * Metodo que elimina un campo desplegable de expediente
     *
     * @param codOrg
     * @param numExp
     * @param codDesplegable
     * @param con
     * @return
     * @throws Exception
     */
    public int borrarDesplegableExpediente(int codOrg, String numExp, String codDesplegable, Connection con) throws Exception {
        log.info("borrarDesplegableExpediente - ini()");
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "delete FROM " + ConstantesMeLanbide07.TABLA_E_TDE
                    + " WHERE TDE_MUN=? AND TDE_EJE=? AND TDE_NUM=? AND TDE_COD=?";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, codDesplegable);
            return ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Excepcion - borrarDesplegableExpediente : " + e.getMessage(), e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public String getCodigoSubvencionEika(String ejerSubvencion, String codProcedimiento, Connection con) throws Exception {
        log.info("getCodigoSubvencionEika - " + codProcedimiento + " - " + ejerSubvencion);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String codigoSubv = null;
        try {
            query = "select REI_PRO_DEUDA from MELANBIDE64_PROC_REINT where REI_EJE = ? and REI_PRO = ?";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, ejerSubvencion);
            ps.setString(i++, codProcedimiento);
            rs = ps.executeQuery();
            if (rs.next()) {
                codigoSubv = rs.getString("REI_PRO_DEUDA");
            }
        } catch (SQLException e) {
            log.error("Excepcion - getCodigoSubvencionEika : " + e.getMessage(), e);
            throw new Exception(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error(" getCodigoSubvencionEika - Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return codigoSubv;
    }

    public String getDocumentoInteresado(int codOrg, String numExp, Connection con) throws Exception {
        log.debug("getDocumentoInteresado " + numExp);
        Statement st = null;
        ResultSet rs = null;
        String numDoc = "";
        try {
            String query = "select HTE_DOC from " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.TABLA_E_EXT, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide07.TABLA_T_HTE, ConstantesMeLanbide07.FICHERO_PROPIEDADES)
                    + " on EXT_TER = HTE_TER and EXT_NVR = HTE_NVR "
                    + " where EXT_MUN = " + codOrg + "  and EXT_EJE = " + numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[0]
                    + " and EXT_PRO = '" + numExp.split(ConstantesMeLanbide07.BARRA_SEPARADORA)[1] + "' and EXT_NUM = '" + numExp + "'"
                    + " and EXT_ROL = 1";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numDoc = rs.getString("HTE_DOC");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el documento del interesado ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return numDoc;
    }
} //class
