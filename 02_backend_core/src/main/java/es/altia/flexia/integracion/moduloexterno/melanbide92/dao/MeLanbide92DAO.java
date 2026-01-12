package es.altia.flexia.integracion.moduloexterno.melanbide92.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConstantesMeLanbide92;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.MeLanbide92MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.MeLanbide92Utilidades;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ProcedimientoLireiVO;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kepa
 */
public class MeLanbide92DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide92DAO.class);
	private final MeLanbide92Utilidades m92Utils = new MeLanbide92Utilidades();
    private final MeLanbide92MappingUtils m92Map = new MeLanbide92MappingUtils();
    SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    //Instancia
    private static MeLanbide92DAO instance = null;

    private MeLanbide92DAO() {
    }

    public static MeLanbide92DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide92DAO.class) {
                instance = new MeLanbide92DAO();
            }
        }
        return instance;
    }

    /**
     * Método que recupera el código interno de un trámite
     *
     * @param codOrg
     * @param procedimiento
     * @param codExterno
     * @param con
     * @return
     * @throws Exception
     */
    public int getCodigoInternoTramite(int codOrg, String procedimiento, String codExterno, Connection con) throws Exception {
        log.info("getCodigoInternoTramite ( procedimiento = " + procedimiento + " codigo = " + codExterno + " )");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int codInterno = 0;
        try {
            String query = "select TRA_COD from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TRA, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TRA_MUN=? and TRA_PRO=? and TRA_COU=?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, procedimiento);
            ps.setString(i++, codExterno);
            rs = ps.executeQuery();
            if (rs.next()) {
                codInterno = rs.getInt("TRA_COD");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el código interno del trámite externo: " + codExterno + " del procedimiento: " + procedimiento, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return codInterno;
    }

    /**
     * Método que recupera el código externo de un trámite
     *
     * @param codOrg
     * @param procedimiento
     * @param codInterno
     * @param con
     * @return
     * @throws Exception
     */
    public int getCodigoExternoTramite(int codOrg, String procedimiento, String codInterno, Connection con) throws Exception {
        log.info("getCodigoExternoTramite ( procedimiento = " + procedimiento + " codigo = " + codInterno + " )");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int codExterno = 0;
        try {
            String query = "select TRA_COU from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TRA, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TRA_MUN=? and TRA_PRO=? and TRA_COD=?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, procedimiento);
            ps.setString(i++, codInterno);
            rs = ps.executeQuery();
            if (rs.next()) {
                codExterno = rs.getInt("TRA_COU");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el código interno del trámite externo: " + codInterno + " del procedimiento: " + procedimiento, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return codExterno;
    }

    public boolean tieneTramiteAbierto(int codOrg, String numExp, int codTramite, Connection con) throws Exception {
        log.info("tieneTramiteAbierto - org:  " + codOrg + " - Exp: " + numExp + " - Tram: " + codTramite);
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = 0;
        try {
            String query = "select count(*) as CONT from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_CRO, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where CRO_MUN = ? and CRO_PRO=?  and CRO_EJE=? and CRO_NUM=? and CRO_TRA=? and CRO_FEI is NOT NULL";
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(i++, numExp);
            ps.setInt(i++, codTramite);
            log.debug(query);
            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
            return resultado > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " tiene un trámite abierto. ", ex.getCause());
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
    }

    /**
     * Método que recupera la ultima ocurrencia de un trámite en un expediente
     *
     * @param codOrg
     * @param numExp
     * @param codTramite
     * @param con
     * @return
     * @throws Exception
     */
    public int getMaxOcurrenciaTramitexCodigo(int codOrg, String numExp, int codTramite, Connection con) throws Exception {
        log.info("getMaxOcurrenciaTramitexCodigo - org:  " + codOrg + " - Exp: " + numExp + " - Tram: " + codTramite);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        int ocurrencia = 1;
        try {
            query = "SELECT MAX(CRO_OCU) AS ocurrencia FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_CRO, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " WHERE CRO_NUM=? AND CRO_PRO=? AND CRO_MUN=? AND CRO_EJE=? AND CRO_TRA=?";

            log.info("getMaxOcurrenciaTramitexCodigo - sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setInt(i++, codOrg);
            ps.setInt(i++, Integer.parseInt(numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]));
            ps.setInt(i++, codTramite);
            rs = ps.executeQuery();
            while (rs.next()) {
                ocurrencia = rs.getInt("ocurrencia");
            }
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error recuperando la ocurrencia del trámite : " + codTramite + " del expediente: " + numExp, ex);
            throw new Exception(ex);
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la ocurrencia del trámite : " + codTramite + " del expediente: " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return ocurrencia;
    }

    public int cerrarTramite(int codOrg, String numExp, int codTramite, Connection con) throws Exception {
        log.info("cerrarTramite BEGIN");
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_CRO, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " set CRO_FEF=SYSDATE, CRO_OBS = cro_obs  || chr(13) || 'Trámite cerrado por job de Envío a Vía Ejecutiva', CRO_USF = 5"
                    + " where CRO_MUN = ? AND CRO_EJE=? AND CRO_PRO= ? and  CRO_NUM = ?  AND CRO_TRA=?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(i++, numExp);
            ps.setInt(i++, codTramite);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error cerrando un trámite del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarStatement(ps);
        }
    }

    public int abrirTramite(int codOrg, String numExp, int codTramite, int ocurrencia, String uor, Connection con) throws Exception {
        log.info("abrirTramite BEGIN");
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_CRO, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " (CRO_MUN, CRO_EJE, CRO_PRO, CRO_NUM, CRO_TRA, CRO_OCU, CRO_FEI, CRO_USU, CRO_UTR ) "
                    + " values(?, ?, ?, ?, ?, ?, SYSDATE, 5, ? )";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(i++, numExp);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, ocurrencia);
            ps.setString(i++, uor);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error abriendo un trámite del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarStatement(ps);
        }
    }

    /**
     * Método que recupera la UOR de un expediente
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public String getUnidadExpediente(int codOrg, String numExp, Connection con) throws Exception {
        log.info("getUnidadExpediente - org:  " + codOrg + " - Exp: " + numExp);
        String cod = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql = "select EXP_UOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_EXP, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where EXP_MUN = ? and EXP_PRO = ? and EXP_EJE = ? and EXP_NUM = ? and EXP_FEF is null";
            log.debug("sql = " + sql);
            int contador = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);

            rs = ps.executeQuery();
            if (rs.next()) {
                cod = rs.getString("EXP_UOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la unidad organizativa : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
                }
        return cod;
                }

    /**
     * Método que recupera el Usuario de un trámite
     *
     * @param codOrg
     * @param numExpediente
     * @param codProcedimiento
     * @param codTramite
     * @param ocurrencia
     * @param con
     * @return
     * @throws Exception
     */
    public int getUsuarioTramite(int codOrg, String numExpediente, String codProcedimiento, int codTramite, int ocurrencia, Connection con) throws Exception {
        log.info("---- ENTRA en getUsuarioTramite - DAO -  " + numExpediente);
        log.info("Numero del tramite en el que estamos:" + codTramite + " - Ocurrencia: " + ocurrencia);
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query = null;
        int resultado = 0;
        try {
            query = "select CRO_USU from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_CRO, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where CRO_MUN=? and CRO_PRO=? and CRO_EJE=? and CRO_NUM=? and CRO_TRA=? and CRO_OCU=?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codOrg);
            ps.setString(2, numExpediente.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(3, numExpediente.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(4, numExpediente);
            ps.setInt(5, codTramite);
            ps.setInt(6, ocurrencia);
            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
            log.debug("Usuario: " + resultado);
            } catch (SQLException e) {
            log.error("Se ha producido un error recuperando el usuario ", e);
                throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
            }
        return resultado;
    }

    /**
     * Método que recupera el nombre del Usuario de un trámite
     *
     * @param usuario
     * @param con
     * @return
     * @throws Exception
     */
    public String getNombreUsuario(int usuario, Connection con) throws Exception {
        log.info("getNombreUsuario - ini()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String nombre = null;
        String query = null;
        try {
            query = "select USU_NOM from " + GlobalNames.ESQUEMA_GENERICO + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_USUARIOS, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where USU_COD = ?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, usuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                nombre = rs.getString("USU_NOM");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando el nombre del usuario ", e);
            throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return nombre;
    }

    /**
     *
     * @param seqName
     * @param con
     * @return
     * @throws Exception
     */
    private Integer getNextId(String seqName, Connection con) throws Exception {
        log.debug("getNextId - seq:  " + seqName);
        Statement st = null;
        ResultSet rs = null;
        Integer numSec = null;
        String query = null;
        try {
            //Creo el id con la secuencia
            query = "select " + seqName + ".nextval from dual";
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
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(st);
        }
        return numSec;
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
    public int borrarRelacionDokusiCSE(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("borrarRelacionDokusiCSE BEGIN");
        PreparedStatement ps = null;
        String query = null;
        try {
            if (existeRelacionDokusiCSE(codOrg, numExp, codCampo, con)) {
                query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_RELDOC_CSE, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                        + " where RELDOC_MUN = ? and RELDOC_EJE = ? and RELDOC_NUM= ? and RELDOC_COD= ?";
                log.debug("sql = " + query);
                int i = 1;
            ps = con.prepareStatement(query);
                ps.setInt(i++, codOrg);
                ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
                ps.setString(i++, numExp);
                ps.setString(i++, codCampo);
                return ps.executeUpdate();
            } else {
                return 1;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando de RELDOC_DOCCSE la carta de pago " + codCampo + " del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarStatement(ps);
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
            query = "select count(*) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_RELDOC_CSE, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where RELDOC_MUN = ? and RELDOC_EJE = ? and RELDOC_NUM= ? and RELDOC_COD= ?";
            log.debug("sql = " + query);
            log.debug(codOrg + " - " + numExp + " - " + codCampo);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setString(contador++, codCampo);

            rs = ps.executeQuery();
            while (rs.next()) {
            resultado = rs.getInt(1);
            }
            //    return resultado > 0;
            } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si el documento " + codCampo + "  del expediente " + numExp + " está en DOKUSI", e);
                throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
            }
        return resultado > 0;
    }//existe documento

    //////////////////  TEXTO  ///////////////////////
    /**
     *
     * @param codOrg
     * @param numExp
     * @param ejercicio
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public String getValorCampoTextoExpediente(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("getValorCampoTextoExpediente BEGIN " + codCampo);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String valor = null;
        String query = null;
        try {
            query = "select TXT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TXT_MUN = ? and TXT_EJE = ? and TXT_NUM= ? and TXT_COD= ?";
            log.debug("sql = " + query);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setString(contador++, codCampo);
            rs = ps.executeQuery();
            while (rs.next()) {
                valor = rs.getString(1);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario texto " + codCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return valor;
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
    public int borrarTextoExpediente(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("borrarTextoExpediente BEGIN");
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TXT_MUN = ? and TXT_EJE = ? and TXT_NUM= ? and TXT_COD= ?";
            log.debug("sql = " + query);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setString(contador++, codCampo);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando el id de la carta de pago " + codCampo + " del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarStatement(ps);
        }
    }

    public BigDecimal getValorCampoNumericoTramite(int codOrg, String numExp, String ejercicio, int tramite, int ocurrencia, String codCampo, Connection con) throws Exception {
        log.info("getValorCampoNumericoTramite BEGIN " + codCampo);
        PreparedStatement ps = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        String query = null;
        try {
            query = "select TNUT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TNUT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TNUT_MUN = ? and TNUT_EJE = ? and TNUT_NUM= ? and TNUT_TRA = ? and TNUT_OCU = ? and TNUT_COD= ?";
            log.debug("sql = " + query);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setInt(contador++, tramite);
            ps.setInt(contador++, ocurrencia);
            ps.setString(contador++, codCampo);
            rs = ps.executeQuery();
            while (rs.next()) {
                valor = rs.getBigDecimal("TNUT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario Numerico " + codCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return valor;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codCampo
     * @param valor
     * @param con
     * @return
     * @throws Exception
     */
    public int guardarValorCampoNumericoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, BigDecimal valor, Connection con) throws Exception {
        log.info("guardarValorCampoNumericoTramite BEGIN");
        PreparedStatement ps = null;
        String query = null;
        try {
            boolean nuevo = false;
            if (this.getValorCampoNumericoTramite(codOrg, numExp, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0], tramite, ocurrencia, codCampo, con) == null) {
                nuevo = true;
            }
            if (nuevo) {
            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TNUT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + "  (TNUT_MUN, TNUT_PRO, TNUT_EJE, TNUT_NUM, TNUT_TRA,TNUT_OCU, TNUT_COD, TNUT_VALOR) "
                    + " values(?,?,?,?,?,?,?,?)";
            } else {
                query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TNUT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                        + " set TNUT_VALOR = ? where TNUT_MUN = ? and TNUT_PRO = ?  and TNUT_EJE = ?  and TNUT_NUM = ?  and TNUT_TRA = ? and TNUT_OCU = ? and TNUT_COD =?";
            }
            log.debug("sql = " + query);
            log.debug("CAMPO" + codCampo + " - " + valor + " - tram/ocu: " + tramite + "/" + ocurrencia);

            int contador = 1;
            ps = con.prepareStatement(query);
            if (!nuevo) {
                ps.setBigDecimal(contador++, valor);
            }
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setInt(contador++, tramite);
            ps.setInt(contador++, ocurrencia);
            ps.setString(contador++, codCampo);
            if (nuevo) {
            ps.setBigDecimal(contador++, valor);
            }
            return ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando numérico de trámite " + codCampo + " del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarStatement(ps);
                }
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public int borrarNumericoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TNUT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TNUT_MUN = ? and TNUT_PRO = ? and TNUT_EJE = ? and TNUT_NUM = ? and TNUT_TRA = ? and TNUT_OCU = ? and TNUT_COD = ?";
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setInt(contador++, tramite);
            ps.setInt(contador++, ocurrencia);
            ps.setString(contador++, codCampo);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando el campo " + codCampo + ":  del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarStatement(ps);
        }
    }

    //////////////////  FECHA  ///////////////////////
    public boolean existeFechaExp(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("existeFechaExp ( numExpediente = " + numExp + " - codigo = " + codCampo + " )");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = 0;
        String query = null;
        try {
            query = "select count(*) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TFE, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TFE_NUM = ? and TFE_COD = ?";
            log.debug("sql = " + query);

            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, codCampo);
            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
            return resultado > 0;
        } catch (SQLException e) {
            log.error("Se ha producido un error al recuperar " + codCampo + " del expediente " + numExp, e);
            throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
    }

    public boolean existeFechaTram(int codOrg, String numExp, int codTramite, int ocuTramite, String codCampo, Connection con) throws Exception {
        log.info("existeFechaTram ( numExpediente = " + numExp + " - codigo = " + codCampo + " )");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = 0;
        String query = null;
        try {
            query = "select count(*) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TFET, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where tfet_num = ? and TFET_COD = ? and TFET_TRA = ? and TFET_OCU = ? and TFET_MUN = ?";
            log.debug("sql = " + query);

            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, codCampo);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, ocuTramite);
            ps.setInt(i++, codOrg);
            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
            return resultado > 0;
        } catch (SQLException e) {
            log.error("Se ha producido un error al recuperar " + codCampo + " del expediente " + numExp, e);
            throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
    }

    public Date obtenerFechaExp(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("obtenerFechaExp ( numExpediente = " + numExp + " - codigo = " + codCampo + " )");
        PreparedStatement ps = null;
        ResultSet rs = null;
        Date fechaResultado = null;
        String query = null;
        try {
            query = "select TFE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TFE, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TFE_NUM = ? and TFE_COD = ?";
            log.debug("sql = " + query);

            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, codCampo);
            rs = ps.executeQuery();
            rs.next();
            fechaResultado = rs.getDate(1);
        } catch (SQLException e) {
            log.error("Se ha producido un error al recuperar " + codCampo + " del expediente " + numExp, e);
            throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return fechaResultado;
    }

    public Date obtenerFechaTram(int codOrg, String numExp, int codTramite, int ocuTramite, String codCampo, Connection con) throws Exception {
        log.info("obtenerFechaTram ( numExpediente = " + numExp + " - Tram = " + codTramite + " - codigo = " + codCampo + " )");
        PreparedStatement ps = null;
        ResultSet rs = null;
        Date fechaResultado = null;
        String query = null;
        try {
            query = "select TFET_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TFET, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TFET_NUM = ? and TFET_COD = ? and TFET_TRA = ? and TFET_OCU = ? and TFET_MUN = ?";
            log.debug("sql = " + query);

            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, codCampo);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, ocuTramite);
            ps.setInt(i++, codOrg);
            rs = ps.executeQuery();
            rs.next();
            fechaResultado = rs.getDate(1);
        } catch (SQLException e) {
            log.error("Se ha producido un error al recuperar " + codCampo + " del expediente " + numExp, e);
            throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return fechaResultado;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param codTramite
     * @param ocuTramite
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public String getFechaTramite(int codOrg, String numExp, int codTramite, int ocuTramite, String codCampo, Connection con) throws Exception {
        log.info("getFechaTramite ( numExpediente = " + numExp + " tramite = " + codTramite + " ocurrencia = " + ocuTramite + " codigo = " + codCampo + " )");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String fecha = null;
        String query = "";
        try {
            String datos[] = numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA);
            query = "select TFET_VALOR from  " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TFET, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TFET_MUN = ? and TFET_PRO = ? and TFET_EJE = ? and TFET_NUM = ? and TFET_TRA = ? and TFET_OCU = ? and TFET_COD = ? ";
            log.debug("sql = " + query);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, datos[1]);
            ps.setString(contador++, datos[0]);
            ps.setString(contador++, numExp);
            ps.setInt(contador++, codTramite);
            ps.setInt(contador++, ocuTramite);
            ps.setString(contador++, codCampo);
            rs = ps.executeQuery();
            rs.next();
            fecha = rs.getString(1);
        } catch (SQLException ex) {
            log.error("Se ha producido un error obteniendo la fecha tramite del Expediente : " + numExp + ", tramite: " + codTramite + " - " + ocuTramite + ", codigo: " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return fecha;
    }

    ///////////////////  FICHERO  /////////////////////////////////////    
    /**
     *
     * @param codOrg
     * @param numExp
     * @param codCampo
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public boolean existeFicheroExpediente(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("existeFicheroExpediente BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = -1;
        String query = "";
        try {
            query = "select count(*) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TFI, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TFI_MUN = ? and TFI_EJE = ? and TFI_NUM = ? and TFI_COD = ?";
            log.debug("sql = " + query);
            log.debug(codOrg + " - " + numExp + " - " + codCampo);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setString(contador++, codCampo);

            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
            return resultado > 0;
            } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si existe el documento " + codCampo + " de la tabla E_TFI del expediente " + numExp, e);

                throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
            }
//        return resultado > 0;
    }//existe documento

    /**
     *
     * @param codOrg
     * @param numExp
     * @param codCampo
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public int borrarFicheroExpediente(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("borrarFicheroExpediente BEGIN");
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TFI, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TFI_MUN = ? and TFI_EJE = ? and TFI_NUM= ? and TFI_COD= ?";
            log.debug("sql = " + query);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setString(contador++, codCampo);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error Eliminando la carta de pago " + codCampo + " del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarStatement(ps);
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
    public String getNombreFicheroExpediente(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("getNombreFicheroExpediente BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String valor = null;
        String query = null;
        try {
            query = "select TFI_NOMFICH from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TFI, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TFI_MUN = ? and TFI_EJE = ? and TFI_NUM= ? and TFI_COD= ?";
            log.debug("sql = " + query);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setString(contador++, codCampo);
            rs = ps.executeQuery();
            rs.next();
            valor = rs.getString(1);
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario numerico " + codCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
            }
        return valor;
        }

    public boolean grabarCartaDokusi(int codOrg, String numExp, String codCampo, String nombreFichero, String oid, Connection con) throws Exception {
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        boolean grabada = false;
        int resultado = 0;
        try {
            String query = "INSERT into " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TFI, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " (TFI_MUN, TFI_EJE, TFI_NUM, TFI_COD, TFI_MIME, TFI_NOMFICH) values (?,?,?,?,?,?)";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, codCampo);
            ps.setString(i++, "application/pdf");
            ps.setString(i++, nombreFichero);
            resultado = ps.executeUpdate();
            if (resultado > 0) {
                query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_RELDOC_CSE, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                        + " (RELDOC_MUN, RELDOC_EJE, RELDOC_NUM, RELDOC_COD, RELDOC_OID) values (?, ?, ?, ?, ?)";
                log.debug("sql = " + query);
                ps1 = con.prepareStatement(query);
                i = 1;
                ps1.setInt(i++, codOrg);
                ps1.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
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
            m92Utils.cerrarStatement(ps1);
            m92Utils.cerrarStatement(ps);
        }
        return grabada;
    }

    ///////////////////  DESPLEGABLES   /////////////////////////////////////   
    /**
     * 
     * @param codOrg
     * @param numExp
     * @param codTramite
     * @param ocurrencia
     * @param codCampo
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean existeDesplegableTramite(int codOrg, String numExp, int codTramite, int ocurrencia, String codCampo, Connection con) throws Exception {
        log.info("existeDesplegableTramite - BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        int resultado = 0;
        try {
            String query = "select count(*) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TDET, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TDET_MUN = ? AND TDET_PRO=? AND TDET_EJE=? and TDET_NUM = ? and TDET_TRA = ? and TDET_OCU = ? and TDET_COD=? ";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, ocurrencia);
            ps.setString(i++, codCampo);
            rs = ps.executeQuery();
            rs.next();
            resultado = rs.getInt(1);
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si existeDesplegableTramite en el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return resultado > 0;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public String getValorDesplegableTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, Connection con) throws Exception {
        log.info("getValorDesplegableTramite - ini()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String valorCampo = null;
        String sql = null;
        try {
            sql = "SELECT TDET_VALOR FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TDET, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " WHERE TDET_MUN=? AND TDET_PRO=? AND TDET_EJE=? AND TDET_NUM=? "
                    + " AND TDET_TRA=? AND TDET_OCU=? AND TDET_COD=?";
            log.debug("getValorDesplegableTramite - sql =>  " + sql);
            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setInt(i++, tramite);
            ps.setInt(i++, ocurrencia);
            ps.setString(i++, codCampo);
            rs = ps.executeQuery();
            if (rs.next()) {
                valorCampo = rs.getString("TDET_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Excepcion - getValorDesplegableExp : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
            if (rs != null) {
                rs.close();
            }
        }
        return valorCampo;
    }

    /**
     * 
     * @param codOrg
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codCampo
     * @param valor
     * @param con
     * @return
     * @throws Exception 
     */
    public int guardarValorDesplegableTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, String valor, Connection con) throws Exception {
        log.info("guardarValorDesplegableTramite BEGIN");
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "MERGE INTO E_TDET USING (SELECT 1 FROM DUAL) ON (TDET_NUM = ? AND TDET_TRA=? AND TDET_COD=?) WHEN MATCHED THEN UPDATE SET TDET_VALOR = ? "
                    + "WHEN NOT MATCHED THEN INSERT (TDET_MUN, TDET_PRO, TDET_EJE, TDET_NUM, TDET_TRA, TDET_OCU, TDET_COD, TDET_VALOR) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setInt(i++, tramite);
            ps.setString(i++, codCampo);
            ps.setString(i++, valor);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setInt(i++, tramite);
            ps.setInt(i++, ocurrencia);
            ps.setString(i++, codCampo);
            ps.setString(i++, valor);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Excepcion - guardarValorDesplegableTramite : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarStatement(ps);
        }
    }

    ///////////////////  DESPLEGABLE EXTERNO  /////////////////////////////////////   
    /**
     *
     * @param codOrg
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public String getCodigoDespExternoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, Connection con) throws Exception {
        log.info("getCodigoDespExternoTramite ( numExpediente = " + numExp + " tramite = " + tramite + " ocurrencia = " + ocurrencia + " codigo = " + codCampo + " )");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String valor = null;
        String query = "";
        try {
            String datos[] = numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA);
            query = "select TDEXT_CODSEL from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TDEXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TDEXT_MUN=? and TDEXT_PRO = ? and TDEXT_EJE = ? and TDEXT_NUM = ? and TDEXT_TRA = ? and TDEXT_OCU = ? and TDEXT_COD = ? ";
            log.debug("sql = " + query);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, datos[1]);
            ps.setString(contador++, datos[0]);
            ps.setString(contador++, numExp);
            ps.setInt(contador++, tramite);
            ps.setInt(contador++, ocurrencia);
            ps.setString(contador++, codCampo);
            rs = ps.executeQuery();
            rs.next();
            valor = rs.getString(1);
        } catch (SQLException ex) {
            log.error("Se ha producido un error obteniendo el código del desplegable externo de tramite del Expediente : " + numExp + ", tramite: " + tramite + " - " + ocurrencia + ", codigo: " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
            }
        return valor;
        }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public String getDescripcionDespExternoTramite(int codOrg, String numExp, int tramite, int ocurrencia, String codCampo, Connection con) throws Exception {
        log.info("getDescripcionDespExternoTramite ( numExpediente = " + numExp + " tramite = " + tramite + " ocurrencia = " + ocurrencia + " codigo = " + codCampo + " )");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String valor = null;
        String query = "";
        try {
            String datos[] = numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA);
            query = "select TDEXT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TDEXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TDEXT_MUN=? and TDEXT_PRO = ? and TDEXT_EJE = ? and TDEXT_NUM = ? and TDEXT_TRA = ? and TDEXT_OCU = ? and TDEXT_COD = ? ";
            log.debug("sql = " + query);
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, datos[1]);
            ps.setString(contador++, datos[0]);
            ps.setString(contador++, numExp);
            ps.setInt(contador++, tramite);
            ps.setInt(contador++, ocurrencia);
            ps.setString(contador++, codCampo);
            rs = ps.executeQuery();
            rs.next();
            valor = rs.getString(1);
        } catch (SQLException ex) {
            log.error("Se ha producido un error obteniendo el código del desplegable externo de tramite del Expediente : " + numExp + ", tramite: " + tramite + " - " + ocurrencia + ", codigo: " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return valor;
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
    public boolean tieneCheckMarcado(int codOrg, String numExp, String codCampo, Connection con) throws Exception {
        log.info("tieneCheckMarcado - ini()");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        boolean marcado = false;
        String resultado = null;
        try {
            query = "select TDE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TDE, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where TDE_MUN=? and TDE_EJE=? and TDE_NUM=? and TDE_COD=?";
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp);
            ps.setString(contador++, codCampo);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getString("TDE_VALOR");
            }
            if (resultado != null && resultado.equalsIgnoreCase("X")) {
                marcado = true;
            }
        } catch (SQLException e) {
            log.error("Excepcion - tieneCheckMarcado : " + e.getMessage(), e);
            throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return marcado;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean tieneMarcaTelematica(int codOrg, String numExp, Connection con) throws Exception {
        log.info("tieneMarcaTelematica ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean tiene = false;
        String query = null;
        try {
            query = "select count(EXT_NOTIFICACION_ELECTRONICA) TIENE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_EXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where EXT_NUM = ? and EXT_EJE = ? and EXT_PRO = ? and EXT_MUN = ? and EXT_NOTIFICACION_ELECTRONICA = 1";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setInt(i++, codOrg);
            rs = ps.executeQuery();
            if (rs.next()) {
                tiene = (rs.getInt("TIENE") > 0);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si hay marca de notificación telemática - " + e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tiene;
    }

    ///////////////////////////////////////////
    /**
     *
     * @param idDeuda
     * @param con
     * @return
     * @throws Exception
     */
    public String getEstadoDeudaZORKU(String idDeuda, Connection con) throws Exception {
        log.info("getEstadoDeudaZORKU -  BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = null;
        try {
            String query = null;
            query = "select COD_ESTADO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.VISTA_DEUDAS_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where NUM_LIQUIDACION = ?";
            log.debug("sql = " + query);
            log.debug("parametros pasados a la query = " + idDeuda);
            ps = con.prepareStatement(query);
            ps.setString(1, idDeuda);
            rs = ps.executeQuery();

            while (rs.next()) {
                resultado = rs.getString("COD_ESTADO");
                log.debug("Estado = " + resultado);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el ESTADO de la deuda", ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return resultado;
    }

    /**
     *
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean deudaPagadaZORKU(int codOrg, String numExp, Connection con) throws Exception {
        log.info("deudaPagada BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = "";
        try {
            String query = "select FECHA_PAGO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_EXP, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " on TXT_NUM=EXP_NUM and TXT_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES) + "' "
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.VISTA_DEUDAS_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " ON NUM_LIQUIDACION=TXT_VALOR "
                    + "  where EXP_mun=? AND EXP_NUM = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codOrg);
            ps.setString(2, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getString("FECHA_PAGO");
                log.debug("Resultado = " + resultado);
            }
            return resultado != null;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " está pagado. ", ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean deudaAnuladaZORKU(int codOrg, String numExp, Connection con) throws Exception {
        log.info("deudaAnuladaZORKU BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = "";
        try {
            String query = "SELECT FECHA_ANULACION FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_EXP, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " on TXT_NUM=EXP_NUM and TXT_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES) + "' "
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.VISTA_DEUDAS_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " ON NUM_LIQUIDACION=TXT_VALOR AND COD_ESTADO !=28"
                    + " where  EXP_mun=? AND EXP_NUM = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codOrg);
            ps.setString(2, numExp);
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
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean deudaEnEjecutivaZORKU(int codOrg, String numExp, Connection con) throws Exception {
        log.info("deudaEnEjecutivaZORKU BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultado = "";
        try {
            String query = "SELECT FECHA_EJECUTIVA FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_EXP, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " ON TXT_NUM=EXP_NUM and TXT_MUN=EXP_MUN AND TXT_COD='IDDEUDARES' "
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.VISTA_DEUDAS_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " ON NUM_LIQUIDACION=TXT_VALOR "
                    + " WHERE EXP_mun=? AND EXP_NUM = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codOrg);
            ps.setString(2, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getString("FECHA_EJECUTIVA");
            }
            return resultado != null;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " está en Via Ejecutiva. ", ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean tienePagoFraccionadoZORKU(int codOrg, String numExp, Connection con) throws Exception {
        log.info("tienePagoFraccionadoZORKU - BEGIN");
        PreparedStatement ps = null;
        ResultSet rs = null;
        Integer resultado = 0;
        try {
            String query = "SELECT COD_TIPOPAGO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_EXP, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " ON TXT_NUM=EXP_NUM and TXT_MUN=EXP_MUN AND TXT_COD='IDDEUDARES' "
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.VISTA_DEUDAS_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " ON NUM_LIQUIDACION=TXT_VALOR "
                    + " WHERE EXP_mun=? AND EXP_NUM = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(1, codOrg);
            ps.setString(2, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getInt("COD_TIPOPAGO");
            }
            return resultado == 3;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al comprobar si el expediente " + numExp + " está fraccionado. ", ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
            }
    }

    /**
     * metodo que recupera el importe pendiente (importe + intereses) de una
     * deuda en GEP
     *
     * @param numExpediente
     * @param idDeuda
     * @param con
     * @return
     * @throws Exception
     */
    public String getImportePendienteZORKU(int codOrg, String numExpediente, String idDeuda, Connection con) throws Exception {
        log.info("getImportePendienteZORKU - BEGIN");
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query = null;
        String resultado = "";
        try {
            query = "select IMPORTE_PENDIENTE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.VISTA_DEUDAS_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " where NUM_LIQUIDACION =?";
            log.debug("sql = " + query + " - NUM LIQUIDACION: " + idDeuda);
            ps = con.prepareStatement(query);
            ps.setString(1, idDeuda);
                        rs = ps.executeQuery();
            rs.next();
            resultado = rs.getString("IMPORTE_PENDIENTE");
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el importe pendiente de la deuda: " + idDeuda, ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return resultado;
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
            m92Utils.cerrarStatement(ps);
            m92Utils.cerrarResultSet(rs);
        }
        return codigoSubv;
    }

    public String getDocumentoInteresado(int codOrg, String numExp, Connection con) throws Exception {
        log.info("getDocumentoInteresado " + numExp);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String numDoc = null;
            try {
            query = "select HTE_DOC from " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_EXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_T_HTE, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " on EXT_TER = HTE_TER and EXT_NVR = HTE_NVR "
                    + "where EXT_MUN = ? and EXT_EJE = ? and EXT_PRO = ? and EXT_NUM = ?  and EXT_ROL = 1";
            int i = 1;
            int contador = 1;
            ps = con.prepareStatement(query);
            ps.setInt(contador++, codOrg);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[0]);
            ps.setString(contador++, numExp.split(ConstantesMeLanbide92.BARRA_SEPARADORA)[1]);
            ps.setString(contador++, numExp);
            rs = ps.executeQuery();
            if (rs.next()) {
                numDoc = rs.getString("HTE_DOC");
                }
            } catch (SQLException e) {
            log.error("Excepcion - getCodigoSubvencionEika : " + e.getMessage(), e);
                throw new Exception(e);
        } finally {
            m92Utils.cerrarStatement(ps);
            m92Utils.cerrarResultSet(rs);
            }
        return numDoc;
    }

    /**
     *
     * @param con
     * @throws Exception
     */
    public void actualizarPagoDeuda(String procedimiento, Connection con) throws Exception {
        CallableStatement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "call UPDATE_PAGO_DEUDA_EXPTS_LIREI('" + procedimiento + "')";
            log.debug("sql = " + query);
            st = con.prepareCall(query);
            int i = st.executeUpdate();
            log.info("Se ha actualizado el pago de la deuda en " + i + " expedientes");
        } catch (SQLException ex) {
            log.error("Se ha producido un error en actualizarPagoDeuda ", ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarStatement(st);
            m92Utils.cerrarResultSet(rs);
        }
    }

    /**
     *
     * @param con
     * @return
     * @throws Exception
     */
    public List<ProcedimientoLireiVO> getTramitesProcedimientos(Connection con) throws Exception {
        log.info("Entramos en getTramitesProcedimientos DAO");
        List<ProcedimientoLireiVO> lista = new ArrayList<ProcedimientoLireiVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_PROCEDIMIENTOS_LIREI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(m92Map.mapearProcedimiento(rs));
            }
        } catch (SQLException e) {
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return lista;
    }

    /**
     *
     * @param fechaJob
     * @param con
     * @return
     * @throws Exception
     */
    public int insertarRegistroProcesosProgramadosEjec(Date fechaJob, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idSecuencia;
        try {
            idSecuencia = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide92.SEQ_PROCESOS_PROGRAMADOS_EJEC, ConstantesMeLanbide92.FICHERO_PROPIEDADES), con);
            String query = "INSERT into  " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_PROCESOS_PROGRAMADOS_EJEC, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + "  (ID, COD_PROCESO,INICIO,FIN,RESULTADO,REGISTROS_PROCESADOS,DETALLE_ERROR,OBSERVACIONES) "
                    + "values (?,'VIA_EJEC',?,null,null,null,null,null)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            log.debug("parametro 1 = " + idSecuencia);
            ps.setInt(1, idSecuencia);
            log.debug("parametro 2 = " + formateadorFecha.format(fechaJob));
            ps.setTimestamp(2, new java.sql.Timestamp(fechaJob.getTime()));
            rs = ps.executeQuery();
        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar el proceso programado VIA_EJEC ", ex);
            throw new Exception(ex);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
        return idSecuencia;
    }

    public void modificarRegistroProcesosProgramadosEjec(int idSecuencia, Date fechaFinJob, String resultadoJob, int registrosProcesadosOK, int registrosProcesadosNOOK, String mensajeErrorJob, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int registrosProcesados = registrosProcesadosOK + registrosProcesadosNOOK;
        String observacionesJob = "Nº Expedientes tratados: " + registrosProcesados + ". ";
        if (registrosProcesadosOK > 0) {
            observacionesJob = observacionesJob + "Nº Expedientes correctos: " + registrosProcesadosOK + ". ";
        }
        if (registrosProcesadosNOOK > 0) {
            observacionesJob = observacionesJob + "Nº Expedientes incorrectos: " + registrosProcesadosNOOK + ". ";
        }

        try {
            String query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_PROCESOS_PROGRAMADOS_EJEC, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + " SET FIN=?, RESULTADO=?, REGISTROS_PROCESADOS=?, DETALLE_ERROR=?, OBSERVACIONES=? where ID=?";
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
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
    }

    public void insertarRegistroProcProgViaEjeLIREI(Integer idSecuenciaProcesoProgramado, Date fecha, String numExp, String idDeuda, String detalleError, int error, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int idSecuencia;
        try {
            idSecuencia = this.getNextId(ConfigurationParameter.getParameter(ConstantesMeLanbide92.SEQ_PROCESO_VIAEJE, ConstantesMeLanbide92.FICHERO_PROPIEDADES), con);
            String query = "INSERT into  " + ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_PROC_PROG_VIAEJE, ConstantesMeLanbide92.FICHERO_PROPIEDADES)
                    + "  (ID, ID_PROCESO, FECHA_OPERACION, NUMERO_EXPEDIENTE, ID_DEUDA, DETALLE_ERROR, ERROR)values (?,?,?,?,?,?,?)";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            log.debug("parametro 1 = " + idSecuencia);
            ps.setInt(1, idSecuencia);
            log.debug("parametro 2 = " + idSecuenciaProcesoProgramado);
            ps.setInt(2, idSecuenciaProcesoProgramado);
            log.debug("parametro 3 = " + formateadorFecha.format(fecha));
            ps.setTimestamp(3, new java.sql.Timestamp(fecha.getTime()));
            log.debug("parametro 4 = " + numExp);
            ps.setString(4, numExp);
            log.debug("parametro 5 = " + idDeuda);
            ps.setString(5, idDeuda);
            log.debug("parametro 6 = " + detalleError);
            ps.setString(6, detalleError);
            log.debug("parametro 7 = " + error);
            ps.setInt(7, error);

            rs = ps.executeQuery();
        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar el proceso programado via ejecutiva reint: PROC_PROG_VIAEJE_LIREI ", ex);
            throw new Exception(ex);

        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(ps);
        }
    }

    public List<String> getExpedientesEnEspera(int codOrg, ProcedimientoLireiVO procedimiento, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<String> expedientes = new ArrayList<String>();
        String codProc = procedimiento.getCodProcedimiento();
        int tramPago = procedimiento.getPago();
        int tramFraccion = procedimiento.getFracciona();
        int tramSuspension = procedimiento.getSuspension();
        String tEcro = ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_CRO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        try {
            StringBuilder sb = new StringBuilder("SELECT ecro1.CRO_TRA, ecro1.CRO_NUM, TXT_VALOR AS IDDEUDA, SYSDATE, FECHA_LIMITE_PAGO ");
            sb.append(" FROM ").append(tEcro).append(" ecro1 ");
            sb.append(" INNER JOIN ").append(ConfigurationParameter.getParameter(ConstantesMeLanbide92.TABLA_E_TXT, ConstantesMeLanbide92.FICHERO_PROPIEDADES)).append(" ON TXT_NUM = ecro1.CRO_NUM AND TXT_COD='IDDEUDARES' AND txt_mun = ecro1.cro_mun AND txt_eje = ecro1.cro_eje");
            sb.append(" INNER JOIN ").append(ConfigurationParameter.getParameter(ConstantesMeLanbide92.VISTA_DEUDAS_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES)).append(" ON NUM_LIQUIDACION = txt_valor ");
            sb.append(" WHERE ecro1.CRO_PRO='").append(codProc).append("'  AND ecro1.CRO_FEF IS NULL ");
            sb.append(" AND ecro1.CRO_TRA='").append(tramPago).append("' AND ecro1.CRO_MUN=").append(codOrg);
            sb.append(" AND SYSDATE> FECHA_LIMITE_PAGO + 14  ");
            sb.append(" AND INTEGRA_SIPCA='1' ");
            // en estado 1 pendiente o 28 caducada 
            sb.append(" AND COD_ESTADO IN (1,28)  ");
            // no FRACCIONADA 
            sb.append("  and COD_TIPOPAGO = 1 ");
            // NO en SUSPENSION PERIODO PAGO
            sb.append(" AND ecro1.CRO_NUM NOT IN (SELECT ecro3.CRO_NUM FROM ").append(tEcro).append(" ecro3 WHERE ecro3.CRO_TRA =").append(tramSuspension);
            sb.append(" AND ecro3.CRO_PRO = '").append(codProc).append("' ");
            sb.append(" AND ecro3.CRO_OCU = 1 AND ecro3.cro_eje = ecro1.cro_eje AND ecro3.cro_mun = ecro1.cro_mun AND ecro3.cro_fef IS NULL)");
            sb.append(" ORDER BY ecro1.cro_num");
            log.debug("sql = " + sb.toString());
            st = con.createStatement();
            rs = st.executeQuery(sb.toString());
            while (rs.next()) {
                expedientes.add(rs.getString("CRO_NUM"));
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los expedientes", e);
            throw new Exception(e);
        } finally {
            m92Utils.cerrarResultSet(rs);
            m92Utils.cerrarStatement(st);
        }
        return expedientes;
    }

}
