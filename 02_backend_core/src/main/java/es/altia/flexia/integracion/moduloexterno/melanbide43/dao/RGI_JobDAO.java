package es.altia.flexia.integracion.moduloexterno.melanbide43.dao;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpActualizarDocumentoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpedienteNotificarVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaListadoMisGestiones;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TerceroModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.NotificacionBVO;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.PluginNotificacionPlatea;
import es.altia.flexia.notificacion.vo.AdjuntoNotificacionVO;
import es.altia.flexia.notificacion.vo.AutorizadoNotificacionVO;
import es.altia.flexia.notificacion.vo.NotificacionVO;
import es.altia.util.commons.DateOperations;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kepa.gonzalez
 */
public class RGI_JobDAO {

    private static RGI_JobDAO instance = null;
    private final Logger log = LogManager.getLogger(RGI_JobDAO.class);
    SimpleDateFormat formatoFechaAsunto = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");

    public static RGI_JobDAO getInstance() {
        //si no hay ninguna instancia de esta clase tenemos que crear una.

        // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo.
        synchronized (RGI_JobDAO.class) {
            if (instance == null) {
                instance = new RGI_JobDAO();
            }
        }
        return instance;
    }

    ///////////////////////////
    ////    EXPEDIENTES    ////
    ///////////////////////////
    /**
     * recupera los expedientes del procedimiento recibido que se notifican en
     * un tramite y se acusa en el siguiente, tengan el documento de
     * requerimiento y el campo COMNOTIF con valor
     *
     * @param procedimiento
     * @param nombreCampo
     * @param codTramNotificado
     * @param codTramAcuse
     * @param con
     * @return
     * @throws Exception
     */
    public List<ExpedienteNotificarVO> recuperarExpedientesRequerir(String procedimiento, String nombreCampo, String codTramNotificado, String codTramAcuse, Connection con) throws Exception {
        List<ExpedienteNotificarVO> listaExpedientes = new ArrayList<ExpedienteNotificarVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            int tope = Integer.parseInt(ConfigurationParameter.getParameter("TOPE_NOTIFICACIONES_RGI", ConstantesMeLanbide43.FICHERO_PROPIEDADES));

            query = "select * from ("
                    + "SELECT ocu.cro_pro, ocu.cro_num, ocu.cro_eje, ocu.cro_tra, ocu.cro_mun, ocu.cro_utr, ocu.cro_ocu, des.tml_valor, cro_fei, oid.reldoc_oid, tex.txtt_valor"
                    + " FROM e_cro ocu"
                    + " INNER JOIN melanbide_dokusi_reldoc_doccst oid  ON ocu.cro_pro = oid.reldoc_pro AND ocu.cro_mun = oid.reldoc_mun AND ocu.cro_eje = oid.reldoc_eje AND ocu.cro_tra = oid.reldoc_tra"
                    + "      AND ocu.cro_ocu = oid.reldoc_ocu AND ocu.cro_num = oid.reldoc_num AND oid.reldoc_cod = ?"
                    + " INNER JOIN e_tml des ON ocu.cro_mun = des.tml_mun AND ocu.cro_pro = des.tml_pro AND ocu.cro_tra = des.tml_tra"
                    + " INNER JOIN e_tra tra ON tra.tra_mun = des.tml_mun AND tra.tra_pro= des.tml_pro AND tra.tra_cod = des.tml_tra"
                    + " INNER JOIN e_exp expe ON ocu.cro_mun = expe.exp_mun AND ocu.cro_eje = expe.exp_eje AND ocu.cro_pro = expe.exp_pro AND ocu.cro_num = expe.exp_num  "
                    + " INNER JOIN e_txtt tex ON ocu.cro_mun = tex.txtt_mun AND ocu.cro_eje = tex.txtt_eje AND ocu.cro_pro = tex.txtt_pro AND ocu.cro_tra = tex.txtt_tra AND ocu.cro_ocu = tex.txtt_ocu "
                    + "      AND ocu.cro_num = tex.txtt_num AND tex.txtt_cod = 'COMNOTIF'"
                    + " left join notificacion noti ON ocu.cro_mun = noti.cod_municipio AND  ocu.cro_pro = noti.cod_procedimiento AND ocu.cro_eje = noti.ejercicio AND ocu.cro_tra = noti.cod_tramite"
                    + "      AND ocu.cro_ocu = noti.ocu_tramite AND ocu.cro_num = noti.num_expediente "
                    + " WHERE "
                    + "oid.reldoc_oid IS NOT NULL AND ocu.cro_pro= ? "
                    + " AND ocu.cro_tra = (SELECT tra_cod FROM e_tra WHERE tra_cou=? AND tra_fba IS NULL AND tra_pro=?)"
                    + " AND (SELECT ocu2.CRO_NUM FROM E_CRO ocu2 WHERE ocu2.cro_tra = (SELECT tra_cod FROM e_tra WHERE tra_cou = ? AND tra_fba IS NULL AND tra_pro= ?)"
                    + "      AND ocu2.cro_ocu = ocu.cro_ocu  AND  ocu2.cro_mun = ocu.cro_mun AND  ocu2.cro_pro =ocu.cro_pro  AND ocu2.cro_eje = ocu.cro_eje AND ocu2.cro_num=ocu.cro_num "
                    + ") IS NOT NULL "
                    + " AND (noti.cod_notificacion_platea IS NULL OR noti.cod_notificacion_platea = '' OR noti.cod_notificacion_platea = '0')"
                    + " AND noti.num_intentos IS NULL "
                    + " and noti.FECHA_SOL_ENVIO is null "
                    + " ORDER BY cro_num, cro_ocu"
                    + ")where rownum <" + tope;

//            log.debug("sql = " + query);
//            log.debug("Campo: " + nombreCampo + " - Proc: " + procedimiento + " - Trámite notificación : " + codTramNotificado + " - Trámite Acuse : " + codTramAcuse);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, nombreCampo);
            ps.setString(i++, procedimiento);
            ps.setString(i++, codTramNotificado);
            ps.setString(i++, procedimiento);
            ps.setString(i++, codTramAcuse);
            ps.setString(i++, procedimiento);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExpedienteNotificarVO expedienteNotificar = new ExpedienteNotificarVO();

                expedienteNotificar.setEjercicio(rs.getString("cro_eje"));
                expedienteNotificar.setCodTramite(rs.getString("CRO_TRA"));
                expedienteNotificar.setDesTramite(rs.getString("tml_valor"));
                expedienteNotificar.setProcedimiento(rs.getString("cro_PRO"));
                expedienteNotificar.setNumExpediente(rs.getString("Cro_NUM"));
                expedienteNotificar.setFechaRegistrado(rs.getString("cro_fei"));
                expedienteNotificar.setCodigoMunicipio(rs.getString("CRO_MUN"));
                expedienteNotificar.setOcurrenciaTramite(rs.getString("CRO_OCU"));
                expedienteNotificar.setUtr(rs.getString("CRO_UTR"));
                expedienteNotificar.setOidDocumentoNotificar(rs.getString("RELDOC_OID"));
                expedienteNotificar.setTipoEnvio(rs.getString("txtt_valor"));

                listaExpedientes.add(expedienteNotificar);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error consultando los registros ", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new SQLException(e);
            }
        }
        return listaExpedientes;
    }

    /**
     * recupera los expedientes del procedimiento recibido que esten en un
     * tramite en el que se notifica y acusa, tengan el documento de resolucion
     * y el campo COMNOTIF con valor
     *
     * @param procedimiento
     * @param nombreCampo
     * @param codTramNotifAcuse
     * @param con
     * @return
     * @throws Exception
     */
    public List<ExpedienteNotificarVO> recuperarExpResolver(String procedimiento, String nombreCampo, String codTramNotifAcuse, Connection con) throws Exception {
        List<ExpedienteNotificarVO> listaExpedientes = new ArrayList<ExpedienteNotificarVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            int tope = Integer.parseInt(ConfigurationParameter.getParameter("TOPE_NOTIFICACIONES_RGI", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
            query = "select * from ("
                    + "SELECT ocu.cro_pro, ocu.cro_num, ocu.cro_eje, ocu.cro_tra, ocu.cro_mun, ocu.cro_utr, ocu.cro_ocu, ocu.cro_fei, des.tml_valor, oid.reldoc_oid, tex.txtt_valor "
                    + " FROM e_cro ocu "
                    + " INNER JOIN melanbide_dokusi_reldoc_doccst oid  ON ocu.cro_pro = oid.reldoc_pro AND ocu.cro_mun = oid.reldoc_mun AND ocu.cro_eje = oid.reldoc_eje"
                    + "      AND ocu.cro_tra = oid.reldoc_tra AND ocu.cro_ocu = oid.reldoc_ocu AND ocu.cro_num = oid.reldoc_num AND oid.reldoc_cod = ?"
                    + " INNER JOIN e_tml des ON ocu.cro_mun = des.tml_mun AND ocu.cro_pro = des.tml_pro AND ocu.cro_tra = des.tml_tra"
                    + " INNER JOIN e_tra tra ON tra.tra_mun = des.tml_mun AND tra.tra_pro= des.tml_pro AND tra.tra_cod = des.tml_tra"
                    + " INNER JOIN e_exp expe ON ocu.cro_mun = expe.exp_mun AND ocu.cro_eje = expe.exp_eje AND ocu.cro_pro = expe.exp_pro AND ocu.cro_num = expe.exp_num  "
                    + " INNER JOIN e_txtt tex ON ocu.cro_mun = tex.txtt_mun AND ocu.cro_eje = tex.txtt_eje AND ocu.cro_pro = tex.txtt_pro AND ocu.cro_tra = tex.txtt_tra "
                    + "     AND ocu.cro_ocu = tex.txtt_ocu AND ocu.cro_num = tex.txtt_num AND tex.txtt_cod = 'COMNOTIF'"
                    + " left join notificacion noti ON ocu.cro_mun = noti.cod_municipio AND  ocu.cro_pro = noti.cod_procedimiento AND ocu.cro_eje = noti.ejercicio"
                    + "      AND ocu.cro_tra = noti.cod_tramite AND ocu.cro_ocu = noti.ocu_tramite AND ocu.cro_num = noti.num_expediente "
                    + " WHERE "
                    + "oid.reldoc_oid IS NOT NULL AND ocu.cro_pro = ?"
                    + " AND ocu.cro_tra = (SELECT tra_cod FROM e_tra WHERE tra_cou = ? AND tra_fba IS NULL AND tra_pro = ?)"
                    + " AND (noti.cod_notificacion_platea IS NULL OR noti.cod_notificacion_platea = '' OR noti.cod_notificacion_platea = '0')"
                    + " AND noti.num_intentos IS NULL "
                    + " and noti.FECHA_SOL_ENVIO is null "
                    + " ORDER BY cro_num, cro_ocu"
                    + ")where rownum <" + tope;

//            log.debug("sql = " + query);
//            log.debug("Campo: " + nombreCampo + " - Proc: " + procedimiento + " - Cod Tran: " + codTramNotifAcuse);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, nombreCampo);
            ps.setString(i++, procedimiento);
            ps.setString(i++, codTramNotifAcuse);
            ps.setString(i++, procedimiento);

            rs = ps.executeQuery();

            while (rs.next()) {
                ExpedienteNotificarVO expedienteNotificar = new ExpedienteNotificarVO();

                expedienteNotificar.setEjercicio(rs.getString("cro_eje"));
                expedienteNotificar.setCodTramite(rs.getString("CRO_TRA"));
                expedienteNotificar.setDesTramite(rs.getString("tml_valor"));
                expedienteNotificar.setProcedimiento(rs.getString("cro_PRO"));
                expedienteNotificar.setNumExpediente(rs.getString("Cro_NUM"));
                expedienteNotificar.setFechaRegistrado(rs.getString("cro_fei"));
                expedienteNotificar.setCodigoMunicipio(rs.getString("CRO_MUN"));
                expedienteNotificar.setOcurrenciaTramite(rs.getString("CRO_OCU"));
                expedienteNotificar.setUtr(rs.getString("CRO_UTR"));
                expedienteNotificar.setOidDocumentoNotificar(rs.getString("RELDOC_OID"));
                expedienteNotificar.setTipoEnvio(rs.getString("txtt_valor"));
                listaExpedientes.add(expedienteNotificar);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error consultando los registros ", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new SQLException(e);
            }
        }
        return listaExpedientes;
    }

    /**
     * busca el expediente PADRE en la tabla de expedientes relacionados E_EXR
     *
     * @param numExp
     * @param procedimientoPadre
     * @param con
     * @return
     * @throws Exception
     */
    public String buscaExpedientePadre(String numExp, String procedimientoPadre, Connection con) throws Exception {
//        log.info("Buscar expediente Padre de " + numExp);
        String padre = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT rex_num FROM e_rex WHERE rex_numr = ?"
                    + " AND rex_num LIKE '%/" + procedimientoPadre + "/%'";
//            log.debug("sql = " + query );
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                padre = rs.getString("REX_NUM");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error buscando el expediente Padre de  " + numExp, ex);
            throw new SQLException(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return padre;
    }

    /**
     * comprueba si un expediente esta ACTIVO
     *
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean expedienteActivo(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select count(*) from E_EXP where EXP_NUM=?";
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error(" Se ha producido un error comprobando si existe en E_EXP ", e);
            throw new Exception(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * comprueba si un expediente esta en HISTORICO
     *
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean expedienteHistorico(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select count(*) from HIST_E_EXP where EXP_NUM=?";
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error(" Se ha producido un error comprobando si existe en HIST_e_exp ", e);
            throw new Exception(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * comprueba si un expediente tiene la marca PUBSEDE y se debe notificar
     *
     * @param numExp
     * @param esActivo
     * @param con
     * @return
     * @throws Exception
     */
    public boolean publicadoEnSede(String numExp, boolean esActivo, Connection con) throws Exception {
        log.info("Buscar publicado En Sede de " + numExp);
        boolean publicado = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String tabla = "E_TNU";
        try {
            if (!esActivo) {
                tabla = "HIST_" + tabla;
            }
            query = "select tnu_valor from " + tabla + " where TNU_COD ='PUBSEDE' and TNU_NUM = ?";
//            log.info("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                publicado = (rs.getInt("TNU_VALOR") == 1);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error buscando publicadoEnSede de  " + numExp, ex);
            throw new SQLException(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }

        return publicado;
    }

    /**
     * recupera el texto para la notificacion
     *
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param con
     * @return
     * @throws Exception
     */
    public String recuperaTextoNotificacIon(String numExp, String tramite, String ocurrencia, Connection con) throws Exception {
//        log.info("recuperaTextoNotificacon de " + numExp);
        String texto = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT ttlt_valor FROM e_ttlt WHERE ttlt_num = ? AND ttlt_cod='TEXTONOTIF' AND ttlt_tra= ? AND ttlt_ocu = ?";
//            log.info("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, tramite);
            ps.setString(i++, ocurrencia);
            rs = ps.executeQuery();
            while (rs.next()) {
                texto = rs.getString("TTLT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el texto de la notificación en el expediente " + numExp, ex);
            throw new SQLException(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return texto;
    }

    /**
     * Actualiza el texto de las observaciones de un expediente
     *
     * @param codOrganizacion
     * @param numExp
     * @param observaciones
     * @param con
     * @return
     * @throws Exception
     */
    public boolean grabarObservacionesExpediente(int codOrganizacion, String numExp, boolean esActivo, String observaciones, Connection con) throws Exception {
        log.info("grabarObservacionesExpediente - Begin () ");
        PreparedStatement ps = null;
        String query = null;
        String tabla = null;
        int filasActualizadas = 0;
        try {
            if (esActivo) {
                tabla = "E_EXP";
            } else {
                tabla = "HIST_E_EXP";
            }
            String[] datosExpediente = numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
            query = "update " + tabla + " set EXP_OBS = EXP_OBS || chr(13) || '" + observaciones + "' where EXP_PRO = ? and EXP_EJE = ? and EXP_MUN = ? and EXP_NUM=?";
            ps = con.prepareStatement(query);
//            log.debug("query: " + query);
            int i = 1;
            ps.setString(i++, datosExpediente[1]);
            ps.setString(i++, datosExpediente[0]);
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++, numExp);

            filasActualizadas = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error en grabarObservacionesExpediente ", ex);
            throw new SQLException(ex);
        } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        log.info("grabarObservacionesExpediente - Filas actualizadas " + filasActualizadas);
        return filasActualizadas > 0;
    }

    /**
     * Registra una operación en el Hostórico de Operaciones de un expediente
     *
     * @param operacion
     * @param con
     * @return
     * @throws Exception
     */
    public boolean insertarHistoricoExp(OperacionExpedienteVO operacion, boolean esActivo, Connection con) throws Exception {
        PreparedStatement ps = null;
        int resultadoOp = 0;
        String query = null;
        if (esActivo) {
            query = "INSERT INTO OPERACIONES_EXPEDIENTE (ID_OPERACION, COD_MUNICIPIO, EJERCICIO, NUM_EXPEDIENTE, TIPO_OPERACION,"
                    + " FECHA_OPERACION, COD_USUARIO, DESCRIPCION_OPERACION) "
                    + "VALUES (SEQ_OPERACIONES_EXPEDIENTE.NextVal,?,?,?,?,?,?,?)";
        } else {
            query = "insert into HIST_OPERACIONES_EXPEDIENTE "
                    + " (ID_PROCESO, ID_OPERACION, COD_MUNICIPIO, EJERCICIO, NUM_EXPEDIENTE, TIPO_OPERACION,"
                    + " FECHA_OPERACION, COD_USUARIO, DESCRIPCION_OPERACION) "
                    + "VALUES ((select ID_PROCESO from HIST_E_EXP where EXP_NUM='" + operacion.getNumExpediente() + "'), "
                    + "  SEQ_OPERACIONES_EXPEDIENTE.NextVal,?,?,?,?,?,?,?)";
    }
        try {

//            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, operacion.getCodMunicipio());
            ps.setInt(i++, operacion.getEjercicio());
            ps.setString(i++, operacion.getNumExpediente());
            ps.setInt(i++, operacion.getTipoOperacion());
            ps.setTimestamp(i++, DateOperations.toSQLTimestamp(operacion.getFechaOperacion()));
            ps.setInt(i++, operacion.getCodUsuario());
            ps.setString(i++, operacion.getDescripcionOperacion());
            resultadoOp = ps.executeUpdate();

            } catch (SQLException e) {
            log.error("Se ha producido un error insertando el registro de la Operación  " + operacion.getNumExpediente(), e);
        } finally {
                if (ps != null) {
                    ps.close();
                }
        }
        return resultadoOp > 0;
    }

    ////////////////////////
    ////    TRAMITES    ////
    ////////////////////////
    /**
     * recupera el codigo interno correspondiente al codigo externo y el
     * procedimiento recibidos
     *
     * @param procedimiento
     * @param codExterno
     * @param con
     * @return
     * @throws Exception
     */
    public String dameCodigoInterno(String procedimiento, String codExterno, Connection con) throws Exception {
        log.info("Buscar codigo interno de " + codExterno + " - " + procedimiento);
        String codigo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT tra_cod FROM e_tra WHERE tra_cou = ? AND tra_fba IS NULL AND tra_pro = ?";
            log.info("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, codExterno);
            ps.setString(i++, procedimiento);
            rs = ps.executeQuery();
            while (rs.next()) {
                codigo = rs.getString("TRA_COD");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error buscando el codigo interno ", ex);
            throw new SQLException(ex);
        } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        return codigo;
    }

    /**
     * recupera la ultima ocurrencia de un tramite en un expediente
     *
     * @param numExp
     * @param codTram
     * @param con
     * @return
     * @throws Exception
     */
    public String dameMaxOcurrencia(String numExp, String codTram, Connection con) throws Exception {
        log.info("Buscar mayor ocurrencia de " + codTram + " - " + numExp);
        String codigo = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT MAX(cro_ocu) ocurrencia FROM e_cro WHERE cro_num = ? AND cro_tra = ?";
            log.info("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, codTram);
            rs = ps.executeQuery();
            while (rs.next()) {
                codigo = rs.getString("ocurrencia");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error buscando el codigo interno ", ex);
            throw new SQLException(ex);
        } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
        }
        return codigo;
    }

    ////////////////////////
    ////    TERCEROS    ////
    ////////////////////////
    /**
     * Recupera el codigo de T_HTE correspondiente a un documento
     *
     * @param numDoc
     * @param con
     * @return
     * @throws Exception
     */
    public int getCodTercero(String numDoc, Connection con) throws Exception {
        log.info("getCodTercero - Begin () ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        int codTercero = -1;
        try {
            query = "select HTE_TER from T_HTE where HTE_DOC=?";
            ps = con.prepareStatement(query);
            log.info("query: " + query + " - " + numDoc);
            ps.setString(1, numDoc);
            rs = ps.executeQuery();
            while (rs.next()) {
                codTercero = rs.getInt("HTE_TER");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error  ", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedStatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("Cod Tercero: " + codTercero);
        return codTercero;
    }

    /**
     *
     * @param codTercero
     * @param con
     * @return
     * @throws Exception
     */
    public int getMaxVersionTercero(int codTercero, Connection con) throws Exception {
        log.info("getMaxVersionTercero - Begin () ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        int version = 1;
        try {
            query = "select max(HTE_NVR) VERSION from T_HTE where HTE_TER=?";
            ps = con.prepareStatement(query);
            log.info("query: " + query + " - " + codTercero);
            ps.setInt(1, codTercero);
            rs = ps.executeQuery();
            while (rs.next()) {
                version = rs.getInt("VERSION");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error  ", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedStatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("Versión: " + version);
        return version;
    }

    /**
     *
     * @param con
     * @return
     * @throws Exception
     */
    private int getMaxTerCod(Connection con) throws Exception {
        log.info("getMaxTerCod - Begin () ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        int maxCod = 1;
        try {
            query = "select max(TER_COD) CODIGO from T_TER";
            log.info("query: " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                maxCod = rs.getInt("CODIGO");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error  ", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedStatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("Max Cod: " + maxCod);
        return maxCod;
    }

    /**
     *
     * @param codTercero
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public String getDomicilioInteresadoExpediente(String numExp, boolean esActivo, Connection con) throws Exception {
        log.info("getDomicilioInteresadoExpediente - Begin () ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String tabla = "E_EXT";
        String codDomicilio = "";
        try {
            if (!esActivo) {
                tabla = "HIST_" + tabla;
            }
            query = "select EXT_DOT from " + tabla + " where EXT_NUM = ? and EXT_ROL = 1";
            ps = con.prepareStatement(query);
            log.info("query: " + query + " - " + numExp);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                codDomicilio = rs.getString("EXT_DOT");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error en getDomicilioInteresadoExpediente() ", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedStatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("Domicilio: " + codDomicilio);
        return codDomicilio;
    }

    /**
     *
     * @param codTercero
     * @param con
     * @return
     * @throws Exception
     */
    public String getDomicilioTercero(int codTercero, Connection con) throws Exception {
        log.info("getDomicilioTercero - Begin () ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String codDomicilio = null;
        try {
            query = "select TER_DOM from T_TER where TER_COD = ?";
            ps = con.prepareStatement(query);
            log.info("query: " + query + " - " + codTercero);
            ps.setInt(1, codTercero);
            rs = ps.executeQuery();
            while (rs.next()) {
                codDomicilio = rs.getString("TER_DOM");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error en getDomicilioTercero() ", ex);
            throw new SQLException(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedStatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("Domicilio: " + codDomicilio);
        return codDomicilio;
    }

    /**
     * Comprueba si un tercero es el interesado en un expediente - ROL 1
     *
     * @param numExp
     * @param tercero
     * @param esActivo
     * @param con
     * @return
     * @throws Exception
     */
    public boolean esInteresado(String numExp, int tercero, boolean esActivo, Connection con) throws Exception {
    PreparedStatement ps = null;
        ResultSet rs = null;
    String query = null;
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
    try {
            query = "select count(*) from " + tabla + " where EXT_NUM=? and EXT_TER = ? and EXT_ROL = 1";
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            ps.setInt(2, tercero);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error(" Se ha producido un error comprobando si existe el tercero en el expediente ", e);
            throw new Exception(e);
        } finally {
            if (rs != null) {
                rs.close();
    }
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Comprueba si el documento es de un interesado en el expediente
     *
     * @param codOrg
     * @param numExp
     * @param esActivo
     * @param numDoc
     * @param rol
     * @param con
     * @return
     * @throws Exception
     */
    public boolean existeDocumentoTerceroExpediente(int codOrg, String numExp, boolean esActivo, String numDoc, int rol, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            String[] datosExpediente = numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
            query = "select count(*) from " + tabla
                    + " left join T_HTE on EXT_TER=HTE_TER and EXT_NVR=HTE_NVR"
                    + " where EXT_MUN = ? and EXT_EJE = ? and EXT_PRO = ? and EXT_NUM = ? and EXT_ROL = ? and HTE_DOC = ?";
    ps = con.prepareStatement(query);
    int i = 1;
            ps.setInt(i++, codOrg);
            ps.setString(i++, datosExpediente[0]);
            ps.setString(i++, datosExpediente[1]);
    ps.setString(i++, numExp);
            ps.setInt(i++, rol);
            ps.setString(i++, numDoc);

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error(" Se ha producido un error comprobando si existe el tercero en el expediente ", e);
            throw new Exception(e);
    } finally {
            if (rs != null) {
                rs.close();
            }
    if (ps != null) {
    ps.close();
    }
    }
    }

    /**
     *
     * @param numExp
     * @param esActivo
     * @param con
     * @return
     * @throws Exception
     */
    public boolean hayVariosInteresados(String numExp, boolean esActivo, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            query = "select count(*) from " + tabla
                    + " where EXT_NUM=? and EXT_ROL = 1";
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 1;
        } catch (SQLException e) {
            log.error(" Se ha producido un error comprobando si hay más de un interesado en el expediente ", e);
            throw new Exception(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param codTercero
     * @param version
     * @param codDomicilio
     * @param con
     * @return
     * @throws Exception
     */
    public boolean agregarInteresdoExpediente(int codOrg, String numExp, int codTercero, int version, String codDomicilio, Connection con) throws Exception {
        log.info("agregarInteresdoExpediente - Begin () - " + numExp + " - " + codTercero + " v." + version + " - Cod Domicilio: " + codDomicilio);
        PreparedStatement ps = null;
        String query = null;
        int filasActualizadas = 0;
        try {
            String[] datosExpediente = numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
            query = "INSERT INTO E_EXT (EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_DOT, "
                    + "EXT_ROL, EXT_PRO, MOSTRAR, EXT_NOTIFICACION_ELECTRONICA) VALUES (?, ?, ?, ?, ?, ?, 1, ?, 1, 1)";
            ps = con.prepareStatement(query);
//            log.info("query: " + query);
            int i = 1;
            ps.setInt(i++, codOrg);
            ps.setString(i++, datosExpediente[0]);
            ps.setString(i++, numExp);
            ps.setInt(i++, codTercero);
            ps.setInt(i++, version);
            ps.setString(i++, codDomicilio);
            ps.setString(i++, datosExpediente[1]);
            filasActualizadas = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error en agregarInteresdoExpediente ", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        log.info("agregarInteresdoExpediente - Filas actualizadas " + filasActualizadas);
        return filasActualizadas == 1;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param codTercero
     * @param version
     * @param codDomicilio
     * @param con
     * @return
     * @throws Exception
     */
    public boolean agregarInteresdoExpedienteHist(int codOrg, String numExp, int codTercero, int version, String codDomicilio, Connection con) throws Exception {
        log.info("agregarInteresdoExpedienteHist - Begin () " + numExp + " - " + codTercero + " v." + version + " - Cod Domicilio: " + codDomicilio);
        PreparedStatement ps = null;
        String query = null;
        int filasActualizadas = 0;
        try {
            String[] datosExpediente = numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
            query = "INSERT INTO HIST_E_EXT (ID_PROCESO, EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_DOT, "
                    + "EXT_ROL, EXT_PRO, MOSTRAR, EXT_NOTIFICACION_ELECTRONICA) VALUES ("
                    + "(select max(ID_PROCESO) from HIST_E_EXT where EXT_NUM = ?), "
                    + "?, ?, ?, ?, ?, ?, 1, ?, 1, 1)";
            ps = con.prepareStatement(query);
//            log.info("query: " + query);
            int i = 1;
            ps.setString(i++, numExp);
            ps.setInt(i++, codOrg);
            ps.setString(i++, datosExpediente[0]);
            ps.setString(i++, numExp);
            ps.setInt(i++, codTercero);
            ps.setInt(i++, version);
            ps.setString(i++, codDomicilio);
            ps.setString(i++, datosExpediente[1]);
            filasActualizadas = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error en agregarInteresdoExpedienteHist ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        log.info("agregarInteresdoExpedienteHist - Filas actualizadas " + filasActualizadas);
        return filasActualizadas == 1;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param codTercero
     * @param version
     * @param codDomicilio
     * @param esActivo
     * @param con
     * @return
     * @throws Exception
     */
    public boolean actualizarInteresadoExpediente(int codOrg, String numExp, int codTercero, int version, String codDomicilio, boolean esActivo, Connection con) throws Exception {
        log.info("actualizarInteresadoExpediente - Begin () - " + numExp + " - " + codTercero + " v." + version + " - Cod Domicilio: " + codDomicilio);
        PreparedStatement ps = null;
        String query = null;
        int filasActualizadas = 0;
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            String[] datosExp = numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
            query = "update " + tabla + " set EXT_TER = ?, EXT_NVR = ?, EXT_DOT = ?, EXT_NOTIFICACION_ELECTRONICA = 1 "
                    + " where EXT_MUN = ? and EXT_EJE = ? and EXT_NUM = ? and EXT_ROL = 1 and EXT_PRO = ?";
            ps = con.prepareStatement(query);
//            log.info("query: " + query);
            int i = 1;
            ps.setInt(i++, codTercero);
            ps.setInt(i++, version);
            ps.setString(i++, codDomicilio);
            ps.setInt(i++, codOrg);
            ps.setString(i++, datosExp[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, datosExp[1]);

            filasActualizadas = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error en actualizarInteresadoExpediente ", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        log.info("actualizarInteresadoExpediente - Filas actualizadas " + filasActualizadas);
        return filasActualizadas > 0;

    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param esActivo
     * @param codRol
     * @param codTercero
     * @param version
     * @param codDomicilio
     * @param con
     * @return
     * @throws Exception
     */
    public boolean insertarTerceroExpediente(int codOrg, String numExp, boolean esActivo, int codRol, int codTercero, int version, String codDomicilio, Connection con) throws Exception {
        log.info("insertarTerceroExpediente - Begin () - " + numExp + " - " + codTercero + " v." + version + " - Cod Domicilio: " + codDomicilio + " - ROL: " + codRol);
        PreparedStatement ps = null;
        String query = null;
        int filasActualizadas = 0;
        if (esActivo) {
            query = "insert into E_EXT (EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_DOT, "
                    + "EXT_ROL, EXT_PRO, MOSTRAR, EXT_NOTIFICACION_ELECTRONICA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1, 1)";
        } else {
            query = "insert into HIST_E_EXT (ID_PROCESO, EXT_MUN, EXT_EJE, EXT_NUM, EXT_TER, EXT_NVR, EXT_DOT, "
                    + "EXT_ROL, EXT_PRO, MOSTRAR, EXT_NOTIFICACION_ELECTRONICA) VALUES ("
                    + "(select max(ID_PROCESO) from HIST_E_EXT where EXT_NUM ='" + numExp + "'), "
                    + "?, ?, ?, ?, ?, ?, ?, ?, 1, 1)";
        }
        try {
            String[] datosExpediente = numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA);

            ps = con.prepareStatement(query);
//            log.info("query: " + query);
            int i = 1;
            ps.setInt(i++, codOrg);
            ps.setString(i++, datosExpediente[0]);
            ps.setString(i++, numExp);
            ps.setInt(i++, codTercero);
            ps.setInt(i++, version);
            ps.setString(i++, codDomicilio);
            ps.setInt(i++, codRol);
            ps.setString(i++, datosExpediente[1]);
            filasActualizadas = ps.executeUpdate();
        } catch (SQLException ex) {
            log.error("Se ha producido un error en actualizarInteresadoExpediente ", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        log.info("actualizarInteresadoExpediente - Filas actualizadas " + filasActualizadas);
        return filasActualizadas > 0;

    }

    /**
     *
     * @param ter
     * @param con
     * @return
     * @throws Exception
     */
    public int altaTercero(TerceroModuloIntegracionVO ter, Connection con) throws Exception {
        log.info("altaTercero - Begin () - Tercero: " + ter.getCodTercero());
        String query = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;

        int nuevoCodigo = -1;
        try {
            con.setAutoCommit(false);

//  doy de baja
            query = "update T_TER set TER_SIT = 'B', TER_FBJ = ?, TER_UBJ = ? where TER_COD = ?";
            Timestamp ahora = new Timestamp(System.currentTimeMillis());
            ps = con.prepareStatement(query);
//            log.info("query: " + query + " - Tercero: " + ter.getCodTercero());
            int i = 1;
            ps.setTimestamp(i++, ahora);
            ps.setString(i++, ter.getUsuarioAlta());
            ps.setString(i++, ter.getCodTercero());
            if (ps.executeUpdate() == 1) {
                ps.close();
                log.info("BAJA en T_TER correcta");
                // Obtener codigo para el nuevo tercero
                nuevoCodigo = getMaxTerCod(con);
                nuevoCodigo++;
// alta en T_TER
                query = "insert into T_TER("
                        + "TER_COD, TER_TID, TER_DOC, TER_NOM, TER_AP1,  "
                        + "TER_AP2, TER_NOC, TER_NML, TER_SIT, TER_NVE, "
                        + "TER_FAL, TER_UAL, TER_APL, TER_DOM) "
                        + " values ("
                        + "?, ?, ?, ?, ?, "
                        + "?, ?, ?, ?, ?,"
                        + "?, ?, ?, ?)";
                ps1 = con.prepareStatement(query);
//                log.info("query: " + query);
                i = 1;
                ps1.setInt(i++, nuevoCodigo); //ter_cod
                ps1.setInt(i++, Integer.parseInt(ter.getTipoDocumentoTercero())); // TER_TID
                ps1.setString(i++, ter.getDocumentoTercero()); // TER_DOC
                ps1.setString(i++, ter.getNombreTercero());  // TER_NOM
                ps1.setString(i++, ter.getApellido1Tercero());  // TER_AP1 
                if (ter.getApellido2Tercero() != null) {
                    ps1.setString(i++, ter.getApellido2Tercero());  // TER_AP2 
                } else {
                    ps1.setNull(i++, Types.VARCHAR);
                }
                ps1.setString(i++, ter.getNombreCompleto());// TER_NOC
                ps1.setInt(i++, 2); //TER_NML            
                ps1.setString(i++, "A"); // ter_sit
                ps1.setString(i++, ter.getVersionTercero()); // TER_NVE
                ps1.setTimestamp(i++, ahora); // TER_FAL
                ps1.setString(i++, ter.getUsuarioAlta()); // TER_UAL
                ps1.setString(i++, ter.getModuloAlta()); // TER_APL
                ps1.setString(i++, ter.getDomPrincipal()); // TER_DOM
                if (ps1.executeUpdate() == 1) {
                    ps1.close();
                    log.info("ALTA en T_TER correcta " + nuevoCodigo);
// alta en T_DOT
                    query = "insert into T_DOT(DOT_DOM, DOT_TER, DOT_SIT, DOT_FEC, DOT_USU, DOT_DPA) "
                            + " values (?, ?, ?, ?, ?, ?)";
                    ps2 = con.prepareStatement(query);
                    i = 1;
                    ps2.setString(i++, ter.getDomPrincipal());
                    ps2.setInt(i++, nuevoCodigo);
                    ps2.setString(i++, "A");
                    ps2.setTimestamp(i++, ahora);
                    ps2.setString(i++, ter.getUsuarioAlta());
                    ps2.setInt(i++, 0);

                    if (ps2.executeUpdate() == 1) {
                        ps2.close();

                        log.info("ALTA de la relación del tercero " + nuevoCodigo + " con el domicilio " + ter.getDomPrincipal() + " en T_DOT correcta");
// alta en T_HTE
                        query = "insert into T_HTE("
                                + "HTE_TER, HTE_NVR, HTE_DOT, HTE_TID, HTE_DOC, "
                                + "HTE_NOM, HTE_AP1, HTE_AP2, HTE_NOC, HTE_NML, "
                                + "HTE_FOP, HTE_USU, HTE_APL) "
                                + " values ("
                                + "?, ?, ?, ?, ?, "
                                + "?, ?, ?, ?, ?, "
                                + "?, ?, ?)";
                        ps3 = con.prepareStatement(query);
//                        log.info("query: " + query);
                        i = 1;
                        ps3.setInt(i++, nuevoCodigo);
                        ps3.setString(i++, ter.getVersionTercero());
                        ps3.setString(i++, ter.getDomPrincipal());
                        ps3.setInt(i++, Integer.parseInt(ter.getTipoDocumentoTercero()));
                        ps3.setString(i++, ter.getDocumentoTercero());
                        ps3.setString(i++, ter.getNombreTercero());
                        ps3.setString(i++, ter.getApellido1Tercero());
                        if (ter.getApellido2Tercero() != null) {
                            ps3.setString(i++, ter.getApellido2Tercero());
                        } else {
                            ps3.setNull(i++, Types.VARCHAR);
                        }
                        ps3.setString(i++, ter.getNombreCompleto());
                        ps3.setInt(i++, 2);
                        ps3.setTimestamp(i++, ahora);
                        ps3.setString(i++, ter.getUsuarioAlta());
                        ps3.setString(i++, ter.getModuloAlta());
                        if (ps3.executeUpdate() == 1) {
                            log.info("ALTA en T_HTE correcta - " + nuevoCodigo);
                        } else {
                            log.error("Error al dar de ALTA al tercero " + nuevoCodigo + " en T_TER");
                            nuevoCodigo = -1;
                        }
                    } else {
                        log.error("Error al dar de ALTA la relación del tercero " + nuevoCodigo + " con el domicilio " + ter.getDomPrincipal() + " en T_DOT");
                        nuevoCodigo = -1;
                    }
                } else {
                    log.error("Error al dar de ALTA al tercero " + nuevoCodigo + " en T_TER");
                    nuevoCodigo = -1;
                }
            } else {
                log.error("Error al dar de baja al tercero " + ter.getCodTercero());
            }
            con.commit();

        } catch (Exception ex) {
            con.rollback();
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (ps1 != null) {
                ps1.close();
            }
            if (ps2 != null) {
                ps2.close();
            }
            if (ps3 != null) {
                ps3.close();
            }
        }

        return nuevoCodigo;
    }

    /**
     * pone la marca de notificacion electronica al interesado del expediente en
     * E_EXT
     *
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean marcaNotificacionInteresado(String numExp, boolean esActivo, Connection con) throws Exception {
        log.info("marcaNotificacion Interesado  ");
        PreparedStatement ps = null;
        String sql = "";
        int rowsUpdated = 0;
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            sql = "UPDATE " + tabla + " SET ext_notificacion_electronica=1 WHERE ext_num = ? AND ext_rol =1";
//            log.info(sql);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, numExp);
            rowsUpdated = ps.executeUpdate();

        } catch (SQLException ex) {
            log.error("Se ha producido un error actualizando la marca de Notificación del Interesado ", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return rowsUpdated > 0;
    }

    /**
     * pone la marca de notificacion electronica al interesado y al
     * representante del expediente en E_EXT
     *
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean marcaNotificacionInteresadoRepresentante(String numExp, boolean esActivo, Connection con) throws Exception {
//        log.info("marcaNotificacion Interesado y Representante  ");
        PreparedStatement ps = null;
        String query = "";
        int rowsUpdated = 0;
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            query = "UPDATE " + tabla + " SET ext_notificacion_electronica=1 WHERE ext_num = ? AND ext_rol <3";
//            log.info(query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setString(i++, numExp);
            rowsUpdated = ps.executeUpdate();
            log.info("Se ha(n) marcado " + rowsUpdated + " Tercero(s)");
        } catch (SQLException ex) {
            log.error("Se ha producido un error actualizando la marca de Notificación de los Terceros ", ex);
            throw new SQLException(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return rowsUpdated > 0;
    }

    /**
     *
     * @param numExp
     * @param esActivo
     * @param con
     * @return
     * @throws Exception
     */
    public ArrayList<Participantes> obtenerLDatosInteresadoRGI(String numExp, boolean esActivo, int codTercero, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        Participantes participante = new Participantes();
        ArrayList<Participantes> listaParticipantes = new ArrayList<Participantes>();
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            query = "SELECT EXT_NUM "
                    + ",HTE_DOC, HTE_NOM, HTE_AP1, HTE_AP2, HTE_TLF, HTE_DCE, HTE_NOC, HTE_TID "
                    + ", DNN_DMC, PAI_COD, PRV_COD, MUN_COD, VIA_COD "
                    + ", PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD, EXT_ROL"
                    + " FROM " + tabla
                    + " inner join T_HTE ON EXT_TER = HTE_TER and HTE_NVR=EXT_NVR"
                    + " left join T_DOT ON EXT_DOT = DOT_DOM and HTE_TER = DOT_TER  "
                    + " left join T_DNN ON DNN_DOM = DOT_DOM "
                    + " left join FLBGEN.T_PAI ON PAI_COD = DNN_PAI "
                    + " left join FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV "
                    + " left join FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD and MUN_COD = DNN_MUN "
                    + " left join T_VIA ON VIA_PAI = PAI_COD and VIA_PRV = PRV_COD and VIA_MUN = MUN_COD and VIA_COD = DNN_VIA "
                    + " WHERE EXT_NUM = '" + numExp + "' AND EXT_ROL = 1 and EXT_TER = " + codTercero;
//            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                participante.setNumExp(rs.getString("EXT_NUM"));
                participante.setNif(rs.getString("HTE_DOC"));
                participante.setNombre(rs.getString("HTE_NOM"));
                participante.setApe1(rs.getString("HTE_AP1"));
                participante.setApe2(rs.getString("HTE_AP2"));
                participante.setTlf(rs.getString("HTE_TLF"));
                participante.setMail(rs.getString("HTE_DCE"));
                participante.setNomC(rs.getString("HTE_NOC"));
                participante.setTipoID(rs.getInt("HTE_TID"));
                participante.setIdPais(rs.getString("PAI_COD"));
                participante.setIdProv(rs.getString("PRV_COD"));
                participante.setIdMuni(rs.getString("MUN_COD"));
                participante.setIdCalle(rs.getString("VIA_COD"));
                participante.setPais(rs.getString("PAI_NOM"));
                participante.setProv(rs.getString("PRV_NOM"));
                participante.setMuni(rs.getString("MUN_NOM"));
                participante.setCalle(rs.getString("VIA_NOM"));
                participante.setNum(rs.getString("DNN_NUD"));
                participante.setLetra(rs.getString("DNN_LED"));
                participante.setRol(rs.getString("EXT_ROL"));
                listaParticipantes.add(participante);
                participante = new Participantes();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("Error en obtenerLDatosInteresadoRGI " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        return listaParticipantes;
    }

    /**
     *
     * @param numExp
     * @param esActivo
     * @param codRol
     * @param con
     * @return
     * @throws Exception
     */
    public ArrayList<Participantes> obtenerTerceroPorRolRGI(String numExp, boolean esActivo, int codRol, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        Participantes participante = new Participantes();
        ArrayList<Participantes> listaParticipantes = new ArrayList<Participantes>();
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }

        try {
            query = "select EXT_NUM ,HTE_DOC, HTE_NOM, HTE_AP1, HTE_AP2, HTE_TLF, HTE_DCE, HTE_NOC, HTE_TID, DNN_DMC,"
                    + " PAI_COD, PRV_COD, MUN_COD, VIA_COD, PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD, EXT_ROL"
                    + " from " + tabla
                    + " inner join T_HTE ON EXT_TER = HTE_TER and HTE_NVR = EXT_NVR"
                    + " left join T_DOT ON EXT_DOT = DOT_DOM and HTE_TER = DOT_TER  "
                    + " left join T_DNN ON DNN_DOM = DOT_DOM "
                    + " left join FLBGEN.T_PAI ON PAI_COD = DNN_PAI "
                    + " left join FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV "
                    + " left join FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD and MUN_COD = DNN_MUN "
                    + " left join T_VIA ON VIA_PAI = PAI_COD and VIA_PRV = PRV_COD and VIA_MUN = MUN_COD and VIA_COD = DNN_VIA "
                    + " where EXT_NUM =? and EXT_ROL = ?";
//            log.debug("sql = " + query);

            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setInt(i++, codRol);
            rs = ps.executeQuery();
            while (rs.next()) {
                participante.setNumExp(rs.getString("EXT_NUM"));
                participante.setNif(rs.getString("HTE_DOC"));
                participante.setNombre(rs.getString("HTE_NOM"));
                participante.setApe1(rs.getString("HTE_AP1"));
                participante.setApe2(rs.getString("HTE_AP2"));
                participante.setTlf(rs.getString("HTE_TLF"));
                participante.setMail(rs.getString("HTE_DCE"));
                participante.setNomC(rs.getString("HTE_NOC"));
                participante.setTipoID(rs.getInt("HTE_TID"));
                participante.setIdPais(rs.getString("PAI_COD"));
                participante.setIdProv(rs.getString("PRV_COD"));
                participante.setIdMuni(rs.getString("MUN_COD"));
                participante.setIdCalle(rs.getString("VIA_COD"));
                participante.setPais(rs.getString("PAI_NOM"));
                participante.setProv(rs.getString("PRV_NOM"));
                participante.setMuni(rs.getString("MUN_NOM"));
                participante.setCalle(rs.getString("VIA_NOM"));
                participante.setNum(rs.getString("DNN_NUD"));
                participante.setLetra(rs.getString("DNN_LED"));
                participante.setRol(rs.getString("EXT_ROL"));
                listaParticipantes.add(participante);
                participante = new Participantes();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("Error en obtenerTerceroPorRolRGI " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return listaParticipantes;
    }

    /**
     *
     * @param numExp
     * @param esActivo
     * @param con
     * @return
     * @throws Exception
     */
    public ArrayList<Participantes> obtenerInteresadoRepresentanteRGI(String numExp, boolean esActivo, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        Participantes participante = new Participantes();
        ArrayList<Participantes> listaParticipantes = new ArrayList<Participantes>();
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }

        try {
            query = "select EXT_NUM ,HTE_DOC, HTE_NOM, HTE_AP1, HTE_AP2, HTE_TLF, HTE_DCE, HTE_NOC, HTE_TID, DNN_DMC,"
                    + " PAI_COD, PRV_COD, MUN_COD, VIA_COD, PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD, EXT_ROL"
                    + " from " + tabla
                    + " inner join T_HTE ON EXT_TER = HTE_TER and HTE_NVR = EXT_NVR"
                    + " left join T_DOT ON EXT_DOT = DOT_DOM and HTE_TER = DOT_TER  "
                    + " left join T_DNN ON DNN_DOM = DOT_DOM "
                    + " left join FLBGEN.T_PAI ON PAI_COD = DNN_PAI "
                    + " left join FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV "
                    + " left join FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD and MUN_COD = DNN_MUN "
                    + " left join T_VIA ON VIA_PAI = PAI_COD and VIA_PRV = PRV_COD and VIA_MUN = MUN_COD and VIA_COD = DNN_VIA "
                    + " where EXT_NUM =? and  (EXT_ROL = 1 or EXT_ROL = 2)"
                    + " order by EXT_ROL";
//            log.debug("sql = " + query);

            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                participante.setNumExp(rs.getString("EXT_NUM"));
                participante.setNif(rs.getString("HTE_DOC"));
                participante.setNombre(rs.getString("HTE_NOM"));
                participante.setApe1(rs.getString("HTE_AP1"));
                participante.setApe2(rs.getString("HTE_AP2"));
                participante.setTlf(rs.getString("HTE_TLF"));
                participante.setMail(rs.getString("HTE_DCE"));
                participante.setNomC(rs.getString("HTE_NOC"));
                participante.setTipoID(rs.getInt("HTE_TID"));
                participante.setIdPais(rs.getString("PAI_COD"));
                participante.setIdProv(rs.getString("PRV_COD"));
                participante.setIdMuni(rs.getString("MUN_COD"));
                participante.setIdCalle(rs.getString("VIA_COD"));
                participante.setPais(rs.getString("PAI_NOM"));
                participante.setProv(rs.getString("PRV_NOM"));
                participante.setMuni(rs.getString("MUN_NOM"));
                participante.setCalle(rs.getString("VIA_NOM"));
                participante.setNum(rs.getString("DNN_NUD"));
                participante.setLetra(rs.getString("DNN_LED"));
                participante.setRol(rs.getString("EXT_ROL"));
                listaParticipantes.add(participante);
                participante = new Participantes();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error("Error en obtenerInteresadoRepresentanteRGI " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return listaParticipantes;
    }

    /**
     *
     * @param numExp
     * @param esActivo
     * @param codigoNotificacion
     * @param con
     * @return
     * @throws Exception
     */
    public ArrayList<AutorizadoNotificacionVO> getInteresadosExpedientePluginNotificacion(String numExp, boolean esActivo, int codigoNotificacion, Connection con) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        ArrayList<AutorizadoNotificacionVO> arrayAux = new ArrayList<AutorizadoNotificacionVO>();
        ArrayList<AutorizadoNotificacionVO> arrayRetorno = new ArrayList<AutorizadoNotificacionVO>();
        String query = null;
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            query = "select distinct "
                    + " EXT_ROL,EXT_DOT, ROL_DES, ROL_PDE, TID_DES, HTE_TID TER_TID, HTE_TER TER_COD, HTE_NVR TER_NVE, HTE_DOC TER_DOC, HTE_NOM TER_NOM, HTE_AP1 TER_AP1, HTE_AP2 TER_AP2,"
                    + " HTE_TLF TER_TLF, DNN_PAI, HTE_DCE TER_DCE, HTE_NOC TER_NOC, PAI_NOM, DNN_PRV, PRV_COD, PRV_NOM, DNN_MUN, MUN_COD, MUN_NOM, TVI_DES, VIA_COD, VIA_NOM, DNN_DMC, DNN_NUD,"
                    + " DNN_LED, DNN_NUH, DNN_LEH, DNN_BLQ, DNN_POR, DNN_ESC, DNN_PLT ,DNN_PTA, DNN_CPO, EXT_MUN, EXT_EJE, EXT_NOTIFICACION_ELECTRONICA "
                    + " from  " + tabla
                    + " left join T_HTE on EXT_TER = HTE_TER and EXT_NVR = HTE_NVR "
                    + " left join E_ROL on EXT_MUN = ROL_MUN and EXT_PRO = ROL_PRO and EXT_ROL = ROL_COD "
                    + " left join T_DNN on EXT_DOT = DNN_DOM  "
                    + " left join T_TVI on DNN_TVI = TVI_COD "
                    + " left join T_VIA on DNN_VIA = VIA_COD and DNN_PAI = VIA_PAI and DNN_PRV = VIA_PRV and DNN_MUN = VIA_MUN  "
                    + " left join T_TID on HTE_TID = TID_COD  "
                    + " left join FLBGEN.T_PAI T_PAI on DNN_PAI = t_pai.PAI_COD  "
                    + " left join FLBGEN.T_PRV T_PRV on DNN_PRV = T_PRV.PRV_COD and DNN_PAI = T_PRV.PRV_PAI    "
                    + " left join FLBGEN.T_MUN T_MUN on DNN_MUN = T_MUN.MUN_COD and DNN_PRV = T_MUN.MUN_PRV and DNN_PAI = T_MUN.MUN_PAI and DNN_MUN = T_MUN.MUN_COD  "
                    + " where EXT_NUM = ?"
                    + " and(EXT_NOTIFICACION_ELECTRONICA = '1' or((EXT_NOTIFICACION_ELECTRONICA is null or EXT_NOTIFICACION_ELECTRONICA = 0) and EXT_ROL in(1, 2)))"
                    + " order by EXT_NOTIFICACION_ELECTRONICA desc nulls last, EXT_ROL ";

//            log.debug(query + " - " + numExp);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();

            ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
            String codPaisRT = bundle.getString("COUNTRY");
            while (rs.next()) {
                AutorizadoNotificacionVO autorizadoNotifAux = new AutorizadoNotificacionVO();

                autorizadoNotifAux.setCodigoTercero(rs.getInt("TER_COD"));
                autorizadoNotifAux.setNumeroVersionTercero(rs.getInt("TER_NVE"));
                autorizadoNotifAux.setNombre(rs.getString("TER_NOM"));
                autorizadoNotifAux.setNif(rs.getString("TER_DOC"));
                autorizadoNotifAux.setTipoDocumento(rs.getString("TER_TID"));
                autorizadoNotifAux.setEmail(rs.getString("TER_DCE"));
                autorizadoNotifAux.setNumeroExpediente(numExp);
                autorizadoNotifAux.setCodigoMunicipio(rs.getInt("EXT_MUN"));
                autorizadoNotifAux.setEjercicio(rs.getInt("EXT_EJE"));
                autorizadoNotifAux.setSeleccionado("SI");
                autorizadoNotifAux.setApellido1(rs.getString("TER_AP1"));
                autorizadoNotifAux.setApellido2(rs.getString("TER_AP2"));
                autorizadoNotifAux.setTelefono(rs.getString("TER_TLF"));
                autorizadoNotifAux.setCodigoNotificacion(codigoNotificacion);
                String nombreCompleto = "";
                if (autorizadoNotifAux.getApellido1() != null && !"".equals(autorizadoNotifAux.getApellido1()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getApellido1())) {
                    nombreCompleto = autorizadoNotifAux.getApellido1();
                }
                if (autorizadoNotifAux.getApellido2() != null && !"".equals(autorizadoNotifAux.getApellido2()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getApellido2())) {
                    nombreCompleto += (!nombreCompleto.isEmpty() ? " " + autorizadoNotifAux.getApellido2() : autorizadoNotifAux.getApellido2());
                }
                if (autorizadoNotifAux.getNombre() != null && !"".equals(autorizadoNotifAux.getNombre()) && !"null".equalsIgnoreCase(autorizadoNotifAux.getNombre())) {
                    nombreCompleto += (!nombreCompleto.isEmpty() ? ", " + autorizadoNotifAux.getNombre() : autorizadoNotifAux.getNombre());
                }
                autorizadoNotifAux.setNombreCompleto(nombreCompleto);
                autorizadoNotifAux.setCodPais(codPaisRT);
                autorizadoNotifAux.setCodProvincia(rs.getString("PRV_COD"));
                autorizadoNotifAux.setDescProvincia(rs.getString("PRV_NOM"));
                autorizadoNotifAux.setDescMunicipio(rs.getString("MUN_NOM"));
                autorizadoNotifAux.setCodVia(rs.getString("VIA_COD"));
                autorizadoNotifAux.setDescVia(rs.getString("VIA_NOM"));
                String tipoVia = rs.getString("TVI_DES");
                String nombreVia = rs.getString("VIA_NOM");
                String numDesde = rs.getString("DNN_NUD");
                String letraDesde = rs.getString("DNN_LED");
                String numHasta = rs.getString("DNN_NUH");
                String letraHasta = rs.getString("DNN_LEH");
                String bloque = rs.getString("DNN_BLQ");
                String portal = rs.getString("DNN_POR");
                String escalera = rs.getString("DNN_ESC");
                String planta = rs.getString("DNN_PLT");
                String puerta = rs.getString("DNN_PTA");
                autorizadoNotifAux.setDireccion(construirDireccion(tipoVia, nombreVia, numDesde, letraDesde, numHasta, letraHasta, bloque, portal, escalera, planta, puerta));
                autorizadoNotifAux.setCodPostal(rs.getString("DNN_CPO"));
                autorizadoNotifAux.setCodDomicilio(rs.getInt("EXT_DOT"));
                autorizadoNotifAux.setRol(rs.getInt("EXT_ROL"));

                arrayAux.add(autorizadoNotifAux);
            }
            rs.close();
            for (int j = 0; j < arrayAux.size(); j++) {
                AutorizadoNotificacionVO autorizadoNotifVORetorno = new AutorizadoNotificacionVO();
                autorizadoNotifVORetorno = arrayAux.get(j);
                if (estaAutorizadoEnNotificacion(autorizadoNotifVORetorno, codigoNotificacion, con)) {
                    autorizadoNotifVORetorno.setSeleccionado("SI");
                    autorizadoNotifVORetorno.setCodigoNotificacion(codigoNotificacion);
                }
                arrayRetorno.add(autorizadoNotifVORetorno);
            }
        } catch (TechnicalException e) {
            //  e.printStackTrace();
            log.error(" Error al OBtener datos interesado : " + e.getMessage(), e);
        } catch (SQLException e) {
            //  e.printStackTrace();
            log.error(" Error al OBtener datos interesado : " + e.getMessage(), e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
//        log.debug("-- Datos Terceros Expedientes Notificacion - arrayRetorno " + arrayAux.size());
        return arrayRetorno;
    }

    /**
     *
     * @param numExp
     * @param esActivo
     * @param nif
     * @param codRol
     * @param con
     * @return
     * @throws Exception
     */
    public TercerosValueObject getDatosTerceroRdRxNifExpteRol(String numExp, boolean esActivo, String nif, Integer codRol, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        TercerosValueObject tercero = new TercerosValueObject();
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            query = "select "
                    + " HTE_DOC, HTE_NOM, HTE_AP1, HTE_AP2, HTE_TLF, HTE_DCE, HTE_NOC, HTE_TID, HTE_TER, HTE_NVR, HTE_DOT  "
                    + " ,DNN_DMC ,PAI_COD, PRV_COD, MUN_COD, VIA_COD ,PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD "
                    + " ,EXT_ROL, MOSTRAR, EXT_NOTIFICACION_ELECTRONICA "
                    + " from " + tabla
                    + " left join T_HTE on EXT_TER = HTE_TER and EXT_NVR = HTE_NVR  "
                    + " left join T_DOT on HTE_DOT = DOT_DOM and HTE_TER = DOT_TER    "
                    + " left join T_DNN on DNN_DOM = DOT_DOM  "
                    + " left join FLBGEN.T_PAI on PAI_COD = DNN_PAI "
                    + " left join FLBGEN.T_PRV on PRV_PAI = PAI_COD and PRV_COD = DNN_PRV "
                    + " left join FLBGEN.T_MUN on MUN_PAI = PAI_COD and MUN_PRV = PRV_COD and MUN_COD = DNN_MUN "
                    + " left join T_VIA on VIA_PAI = PAI_COD and VIA_PRV = PRV_COD and VIA_MUN = MUN_COD and VIA_COD = DNN_VIA "
                    + " where EXT_NUM = ? and EXT_ROL = ? and HTE_DOC = ?"
                    + " order by (case when HTE_DOT is null then 0 else 1 end) asc, DOT_DPA  asc nulls first , HTE_FOP asc, HTE_TER asc, HTE_nvr asc ";
//            log.debug(query + " - " + numExp);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setInt(i++, codRol);
            ps.setString(i++, nif);
            rs = ps.executeQuery();
            while (rs.next()) {
                tercero.setIdentificador(rs.getString("HTE_TER"));
                tercero.setCodTerceroOrigen(rs.getString("HTE_TER"));
                tercero.setVersion(rs.getString("HTE_NVR"));
                tercero.setDomPrincipal((rs.getString("HTE_DOT") != null && !rs.getString("HTE_DOT").isEmpty() ? rs.getString("HTE_DOT") : "-1"));
                tercero.setIdDomicilio((rs.getString("HTE_DOT") != null && !rs.getString("HTE_DOT").isEmpty() ? rs.getString("HTE_DOT") : "-1"));
                tercero.setNombre(rs.getString("HTE_NOM"));
                tercero.setApellido1(rs.getString("HTE_AP1"));
                tercero.setApellido2(rs.getString("HTE_AP2"));
                tercero.setTipoDocumento(rs.getString("HTE_TID"));
                tercero.setDocumento(rs.getString("HTE_DOC"));
                tercero.setTelefono(rs.getString("HTE_TLF"));
                tercero.setEmail(rs.getString("HTE_DCE"));
                tercero.setNombreCompleto(rs.getString("HTE_NOC"));
                // tercero.setCodRol(rs.getInt("EXT_ROL") );
                //tercero.setNotificacionElectronica(rs.getString("EXT_NOTIFICACION_ELECTRONICA"));
                // Ponemos -1 prque al insertar datos en el historico de operaciones falla si el domicilio es null.
            }
        } catch (SQLException ex) {
            log.error("Error en getDatosTerceroRdRxNifExpteRol " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return tercero;
    }

    /**
     *
     * @param tercero
     * @param codOrg
     * @param numExp
     * @param esActivo
     * @param codRol
     * @param con
     * @return
     * @throws Exception
     */
    public boolean eliminarRepresentanteLegalExpedienteRdR(TercerosValueObject tercero, int codOrg, String numExp, boolean esActivo, int codRol, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        int result = 0;
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            query = "delete from " + tabla
                    + "where EXT_MUN = ? and EXT_EJE = ? and EXT_PRO = ? and EXT_NUM = ? and EXT_TER = ? and EXT_NVR = ? and EXT_ROL = ?";
//            log.debug(query + " - " + numExp);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA)[1]);
            ps.setString(i++, numExp);
            ps.setString(i++, tercero.getIdentificador());
            ps.setString(i++, tercero.getVersion());
            ps.setInt(i++, codRol);
            result = ps.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            log.error("Error al eliminar el ROL REPRESENTANTE en el expediente " + numExp, e);
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    ////////////////////////
    ////    DOCUMENTOS  ////
    ////////////////////////
    /**
     * Da de alta un adjunto externo asociada a una determinada notificación
     * pero el binario no se almacena en base de datos
     *
     * @param adjuntoNotificacionVO
     * @param oid
     * @param con
     * @return
     */
    public boolean insertarAdjuntoExterno(AdjuntoNotificacionVO adjuntoNotificacionVO, String oid, Connection con) {
        log.info("insertarAdjuntoExterno = " + oid);
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        String query = "";
        boolean exito = false;
        try {
            query = "insert into ADJUNTO_EXT_NOTIFICACION(ID, COD_MUNICIPIO, NUM_EXPEDIENTE, COD_TRAMITE, OCU_TRAMITE, FECHA, ID_NOTIFICACION, NOMBRE, TIPO_MIME, ESTADO_FIRMA)"
                    + " values(SEQ_FILE_EXT_NOTIFICACION.nextval,?,?,?,?,?,?,?,?,?)";
//            log.info(query);

            int i = 1;
            ps = con.prepareStatement(query, new String[]{"ID"});
            ps.setInt(i++, adjuntoNotificacionVO.getCodigoMunicipio());
            ps.setString(i++, adjuntoNotificacionVO.getNumeroExpediente());
            ps.setInt(i++, adjuntoNotificacionVO.getCodigoTramite());
            ps.setInt(i++, adjuntoNotificacionVO.getOcurrenciaTramite());
            ps.setTimestamp(i++, DateOperations.toTimestamp(Calendar.getInstance()));
            ps.setInt(i++, adjuntoNotificacionVO.getCodigoNotificacion());
            ps.setString(i++, adjuntoNotificacionVO.getNombre());
            ps.setString(i++, adjuntoNotificacionVO.getContentType());
            ps.setString(i++, "0");
            int rowsInserted = ps.executeUpdate();
//            log.info(" Filas insertadas en adjunto_ext_notificacion:  " + rowsInserted);
            if (rowsInserted == 1) {
                int primkey = 0;
                java.sql.ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    primkey = generatedKeys.getInt(1);
                }
//                log.info(" Id generado:  " + primkey);
                i = 1;
                query = "insert into MELANBIDE_DOKUSI_RELDOC_EXTNOT (RELDOC_MUN, RELDOC_NUM, RELDOC_TRA, RELDOC_OCU, RELDOC_ID, RELDOC_OID)"
                        + " values(?, ?, ?, ?, ?, ?)";
//                log.info(query);
                ps1 = con.prepareStatement(query);
                ps1.setInt(i++, adjuntoNotificacionVO.getCodigoMunicipio());
                ps1.setString(i++, adjuntoNotificacionVO.getNumeroExpediente());
                ps1.setInt(i++, adjuntoNotificacionVO.getCodigoTramite());
                ps1.setInt(i++, adjuntoNotificacionVO.getOcurrenciaTramite());
                ps1.setInt(i++, primkey);
                ps1.setString(i++, oid);
                exito = ps1.executeUpdate() == 1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.error(("Excepcion capturada en: " + this.getClass().getName() + e.getMessage()));
            exito = false;
        } finally {
            try {
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException bde) {
                bde.printStackTrace();
                log.error(("Excepcion capturada en: " + this.getClass().getName()));
                return false;
            }
        }
        return exito;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param tramite
     * @param ocurrencia
     * @param con
     * @return
     * @throws Exception
     */
    public boolean existeAnexoResolucion(int codOrg, String numExp, int tramite, int ocurrencia, Connection con) throws Exception {
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query = null;
        try {
            String[] datosExpediente = numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
            query = "select count(*) from E_TFIT"
                    + " where TFIT_MUN = ? and TFIT_PRO = ? and TFIT_EJE = ? and TFIT_NUM = ? and TFIT_TRA = ? and TFIT_OCU = ?  and TFIT_COD= 'IDDOCANEXO'";
//            log.info("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, datosExpediente[1]);
            ps.setString(i++, datosExpediente[0]);
            ps.setString(i++, numExp);
            ps.setInt(i++, tramite);
            ps.setInt(i++, ocurrencia);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si existe el documento anexo a la resolución del expediente " + numExp, e);
            throw new Exception(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * recupera el documento Anexo a una resolucion IDDOCANEXO del tramite
     * notificado de un expediente y devuelve los datos en el objeto auxiliarVO
     *
     * @param anexoVO
     * @param con
     * @return
     * @throws Exception
     */
    public AdjuntoNotificacionVO recuperaAnexoResolucion(AdjuntoNotificacionVO anexoVO, Connection con) throws Exception {
        AdjuntoNotificacionVO auxiliarVO = anexoVO;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "select TFIT_MIME, TFIT_NOMFICH, RELDOC_OID from E_TFIT "
                    + " inner join MELANBIDE_DOKUSI_RELDOC_DOCCST"
                    + " on TFIT_MUN = RELDOC_MUN and TFIT_EJE = RELDOC_EJE and TFIT_PRO = RELDOC_PRO"
                    + " and TFIT_NUM = RELDOC_NUM and TFIT_TRA = RELDOC_TRA and TFIT_OCU = RELDOC_OCU"
                    + " and TFIT_COD = RELDOC_COD "
                    + " where TFIT_MUN = ? and TFIT_PRO = ? and TFIT_EJE = ?"
                    + " and TFIT_NUM = ? and TFIT_TRA = ? and TFIT_OCU = ?"
                    + " and TFIT_COD= 'IDDOCANEXO'";
//            log.info("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, anexoVO.getCodigoMunicipio());
            ps.setString(i++, anexoVO.getCodigoProcedimiento());
            ps.setInt(i++, anexoVO.getEjercicio());
            ps.setString(i++, anexoVO.getNumeroExpediente());
            ps.setInt(i++, anexoVO.getCodigoTramite());
            ps.setInt(i++, anexoVO.getOcurrenciaTramite());
            rs = ps.executeQuery();
            while (rs.next()) {
                auxiliarVO.setContentType(rs.getString("TFIT_MIME"));
                auxiliarVO.setNombre(rs.getString("TFIT_NOMFICH"));
                // como este objeto no tiene propiedad para el OID lo devuelvo en observacionesRechazo
                // hay que vaciar este campo 
                auxiliarVO.setObservacionesRechazo(rs.getString("RELDOC_OID"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar el Anexo a la Resolucion de  " + anexoVO.getNumeroExpediente(), ex);
            throw new SQLException(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return auxiliarVO;
    }

    /////////////////////////////
    ////    NOTIFICACIONES   ////
    /////////////////////////////
    /**
     * Crea una notificacion electrónica por defecto en el caso de que no exista
     *
     * @param notificacion: Objeto de tipo NotificacionVO
     * @param con
     * @throws TechnicalException
     */
    public boolean crearNotificacionImvRgi(NotificacionVO notificacion, Connection con) throws TechnicalException {
        PreparedStatement ps = null;
        int rowsUpdated = 0;
        try {
            // No existe una notificación para la ocurrencia del trámite que pertenece a un determinado expediente
            String sql = "INSERT INTO notificacion(codigo_notificacion,num_expediente,cod_procedimiento,ejercicio,cod_municipio,cod_tramite,ocu_tramite, acto_notificado,texto_notificacion, fecha_sol_envio, en_proceso)  "
                    + " VALUES (seq_notificacion.NEXTVAL,?,?,?,?,?,?,?,?,sysdate,'S')";
//            log.info(sql);

            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, notificacion.getNumExpediente());
            ps.setString(i++, notificacion.getCodigoProcedimiento());
            ps.setInt(i++, notificacion.getEjercicio());
            ps.setInt(i++, notificacion.getCodigoMunicipio());
            ps.setInt(i++, notificacion.getCodigoTramite());
            ps.setInt(i++, notificacion.getOcurrenciaTramite());
            ps.setString(i++, notificacion.getActoNotificado());
            ps.setString(i++, notificacion.getTextoNotificacion());
            rowsUpdated = ps.executeUpdate();
            if (rowsUpdated != 1) {
                ErrorBean err = new ErrorBean();
                err.setIdError("NOTIFICACIONES_011");
                err.setMensajeError("Error crear la notificación defecto");
                err.setSituacion("RGIIMVNotificarJob");
                err.setEvento("Envío automático de notificación");
                PluginNotificacionPlatea.grabarError(err, "Error crear la notificación defecto", "No se ha dado de alta la notificación por defecto", notificacion.getNumExpediente());
                throw new TechnicalException("No se ha dado de alta la notificación por defecto");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            ErrorBean err = new ErrorBean();
            err.setIdError("NOTIFICACIONES_011");
            err.setMensajeError("Error crear la notificación defecto");
            err.setSituacion("RGIIMVNotificarJob");
            err.setEvento("Envío automático de notificación");
            PluginNotificacionPlatea.grabarError(err, e.getMessage(), e.toString(), notificacion.getNumExpediente());
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowsUpdated == 1;
    }

    /**
     *
     * @param notificacion
     * @param con
     * @return
     * @throws TechnicalException
     */
    public NotificacionVO getNotificacionImvRgi(NotificacionVO notificacion, Connection con) throws TechnicalException {
//        log.info(" -> Entra a getNotificacionImvRgi");
        NotificacionVO notificacionVORetorno = new NotificacionVO();
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String query = "SELECT CODIGO_NOTIFICACION, NUM_EXPEDIENTE, COD_PROCEDIMIENTO, EJERCICIO, COD_MUNICIPIO, COD_TRAMITE, OCU_TRAMITE,"
                    + " ACTO_NOTIFICADO, CADUCIDAD_NOTIFICACION, FIRMA, TEXTO_NOTIFICACION, FIRMADA FROM NOTIFICACION"
                    + " WHERE NUM_EXPEDIENTE= ? AND COD_PROCEDIMIENTO= ? AND EJERCICIO= ? AND COD_MUNICIPIO= ? AND COD_TRAMITE= ? AND OCU_TRAMITE = ?"
                    + " ORDER BY CODIGO_NOTIFICACION DESC";
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, notificacion.getNumExpediente());
            ps.setString(i++, notificacion.getCodigoProcedimiento());
            ps.setInt(i++, notificacion.getEjercicio());
            ps.setInt(i++, notificacion.getCodigoMunicipio());
            ps.setInt(i++, notificacion.getCodigoTramite());
            ps.setInt(i++, notificacion.getOcurrenciaTramite());
            rs = ps.executeQuery();

            if (rs.next()) {
                notificacionVORetorno = notificacion;
                notificacionVORetorno.setCodigoNotificacion(rs.getInt("CODIGO_NOTIFICACION"));
                notificacionVORetorno.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                notificacionVORetorno.setCodigoProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                notificacionVORetorno.setEjercicio(rs.getInt("EJERCICIO"));
                notificacionVORetorno.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                notificacionVORetorno.setCodigoTramite(rs.getInt("COD_TRAMITE"));
                notificacionVORetorno.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                notificacionVORetorno.setActoNotificado(rs.getString("ACTO_NOTIFICADO"));
                notificacionVORetorno.setCaducidadNotificacion(rs.getInt("CADUCIDAD_NOTIFICACION"));
                notificacionVORetorno.setFirma(null);
                notificacionVORetorno.setTextoNotificacion(rs.getString("TEXTO_NOTIFICACION"));
                notificacionVORetorno.setEstadoNotificacion(rs.getString("FIRMADA"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return notificacionVORetorno;
    }

    /**
     *
     * @param codNotificacion
     * @param con
     * @return
     * @throws Exception
     */
    public NotificacionBVO recogeNotificacionImvRgiPreparada(int codNotificacion, String numExpPlatea, boolean esActivo, Connection con) throws Exception {
        NotificacionBVO notificacionVORetorno = new NotificacionBVO();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = null;
        String tabla = "E_TXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            sql = "SELECT codigo_notificacion,num_expediente,cod_procedimiento,ejercicio,cod_municipio,"
                    + " cod_tramite,ocu_tramite,acto_notificado,caducidad_notificacion,firma, i.txt_valor AS idioma,  "
                    + " texto_notificacion,firmada, num_intentos, s.txt_valor AS sms, e.txt_valor AS email  "
                    + " ,st.txt_valor AS smstit, et.txt_valor AS emailtit  "
                    + " FROM notificacion "
                    + " left join " + tabla + " s ON '" + numExpPlatea + "' = s.txt_num AND s.txt_cod = 'AVISOSMS'  "
                    + " left join " + tabla + " e ON '" + numExpPlatea + "' = e.txt_num AND e.txt_cod = 'AVISOEMAIL' "
                    + " left join " + tabla + " st ON '" + numExpPlatea + "' = st.txt_num AND st.txt_cod = 'AVISOSMSTIT' "
                    + " left join " + tabla + " et ON '" + numExpPlatea + "' = et.txt_num AND et.txt_cod = 'AVISOEMAILTIT' "
                    + " left join " + tabla + " i ON '" + numExpPlatea + "' = i.txt_num AND i.txt_cod = 'IDIOMAAVISOS' "
                    + " WHERE codigo_notificacion=?"
                    + " AND "
                    + " (num_intentos IS NULL OR num_intentos < 3) AND "
                    + " (cod_notificacion_platea IS NULL OR cod_notificacion_platea = '' OR cod_notificacion_platea = '0')"
                    + " ORDER BY codigo_notificacion DESC";
            log.info("Recogiendo notificacion preparada: " + codNotificacion);
//            log.info("sql: " + sql);

            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, codNotificacion);
            rs = ps.executeQuery();

            while (rs.next()) {
                notificacionVORetorno.setCodigoNotificacion(rs.getInt("CODIGO_NOTIFICACION"));
                notificacionVORetorno.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                notificacionVORetorno.setCodigoProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                notificacionVORetorno.setEjercicio(rs.getInt("EJERCICIO"));
                notificacionVORetorno.setCodigoMunicipio(rs.getInt("COD_MUNICIPIO"));
                notificacionVORetorno.setCodigoTramite(rs.getInt("COD_TRAMITE"));
                notificacionVORetorno.setOcurrenciaTramite(rs.getInt("OCU_TRAMITE"));
                notificacionVORetorno.setActoNotificado(rs.getString("ACTO_NOTIFICADO"));
                notificacionVORetorno.setTextoNotificacion(rs.getString("TEXTO_NOTIFICACION"));
                notificacionVORetorno.setCaducidadNotificacion(rs.getInt("CADUCIDAD_NOTIFICACION"));
                notificacionVORetorno.setEmails(rs.getString("EMAIL"));
                notificacionVORetorno.setSms(rs.getString("SMS"));
                notificacionVORetorno.setEmailsTitular(rs.getString("EMAILTIT"));
                notificacionVORetorno.setSmsTitular(rs.getString("SMSTIT"));
                notificacionVORetorno.setIdioma(rs.getString("IDIOMA"));
                if (rs.getString("NUM_INTENTOS") != null) {
                    notificacionVORetorno.setNumIntentos(rs.getInt("NUM_INTENTOS"));
                } else {
                    notificacionVORetorno.setNumIntentos(0);
                }

                byte[] contenido = null;
                // Se lee el contenido binario del documento
                java.io.InputStream stream = rs.getBinaryStream("FIRMA");
                java.io.ByteArrayOutputStream ot = new java.io.ByteArrayOutputStream();
                int c;
                if (stream != null) {
                    while ((c = stream.read()) != -1) {
                        ot.write(c);
                    }
                }
                ot.flush();
                contenido = ot.toByteArray();
                ot.close();
                String value = new String(contenido);

                notificacionVORetorno.setFirma(value);
                notificacionVORetorno.setEstadoNotificacion(rs.getString("FIRMADA"));

                //        marcarEnProceso(codNotificacion, con);
            }
        } catch (IOException e) {
            log.error("Error en recogerNotificacionesPendientes: ", e);
            e.printStackTrace();
            ErrorBean err = new ErrorBean();
            err.setIdError("NOTIFICACIONES_012");
            err.setMensajeError("Error en recogerNotificacionesPendientes: " + e);
            err.setSituacion("RGIIMVNotificarJob");
            err.setEvento("Envío automático de notificación");
            PluginNotificacionPlatea.grabarError(err, e.getMessage(), e.toString(), String.valueOf(codNotificacion));
            throw new TechnicalException(e.getMessage(), e);
        } catch (SQLException e) {
            log.error("Error en recogerNotificacionesPendientes: ", e);
            e.printStackTrace();
            ErrorBean err = new ErrorBean();
            err.setIdError("NOTIFICACIONES_012");
            err.setMensajeError("Error en recogerNotificacionesPendientes: " + e);
            err.setSituacion("RGIIMVNotificarJob");
            err.setEvento("Envío automático de notificación");
            PluginNotificacionPlatea.grabarError(err, e.getMessage(), e.toString(), String.valueOf(codNotificacion));
            throw new TechnicalException(e.getMessage(), e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return notificacionVORetorno;
    }

    /**
     *
     * @param codNotif
     * @param con
     */
    private void marcarEnProceso(int codNotif, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "UPDATE notificacion SET en_proceso = 'S' WHERE codigo_notificacion=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, codNotif);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.info("Se ha producido un error al tratar al cambiar la notificiación a 'en tratamiento'", e);
            ErrorBean err = new ErrorBean();
            err.setIdError("NOTIFICACIONES_009");
            err.setMensajeError("Error al tratar al cambiar la notificiacióna a 'en tratamiento': " + e);
            err.setSituacion("RGIIMVNotificarJob");
            err.setEvento("Envío automático de notificación");
            PluginNotificacionPlatea.grabarError(err, e.getMessage(), e.toString(), String.valueOf(codNotif));
            throw new TechnicalException(e.getMessage(), e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int marcarFallida(int id, Connection con) throws Exception {
        PreparedStatement ps = null;
        int result = 0;
        String query = null;
        try {
            query = "UPDATE NOTIFICACION SET EN_PROCESO = 'X', NUM_INTENTOS = 9 WHERE CODIGO_NOTIFICACION =  ?";
//            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, id);
            result = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return result;
    }

    /**
     * Elimina la relacion de los docs adjuntos con DOKUSI, los docs adjuntos,
     * los autorizados y la notificación en caso de error para que se pueda
     * volver a procesar un expediente
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarDatosNotificacion(int id, Connection con) throws Exception {
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        int result = 0;
        String query = null;
        try {
            query = "delete from MELANBIDE_DOKUSI_RELDOC_EXTNOT where RELDOC_ID in (select ID from ADJUNTO_EXT_NOTIFICACION where ID_NOTIFICACION = ?)";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, id);
            result = ps.executeUpdate();

            query = "delete from ADJUNTO_EXT_NOTIFICACION where ID_NOTIFICACION = ?";
            log.debug("sql = " + query);
            ps1 = con.prepareStatement(query);
            i = 1;
            ps1.setInt(i++, id);
            result += ps1.executeUpdate();
            ps1.close();

            query = "delete from AUTORIZADO_NOTIFICACION where CODIGO_NOTIFICACION = ?";
            log.debug("sql = " + query);
            ps2 = con.prepareStatement(query);
            i = 1;
            ps2.setInt(i++, id);
            result += ps2.executeUpdate();

            query = "delete from NOTIFICACION where CODIGO_NOTIFICACION =  ?";
            log.debug("sql = " + query);
            ps3 = con.prepareStatement(query);
            i = 1;
            ps3.setInt(i++, id);
            result += ps3.executeUpdate();

        } catch (SQLException ex) {
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (ps1 != null) {
                ps1.close();
            }
            if (ps2 != null) {
                ps2.close();
            }
            if (ps3 != null) {
                ps3.close();
            }
        }
        return result;
    }

    /**
     *
     * @param con
     * @param numeroExpediente
     * @param estado
     * @param mensaje
     * @return
     * @throws Exception
     */
    public int insertarLineasLogNotificaciones(Connection con, String numeroExpediente, String estado, String mensaje) throws Exception {
        log.info("insertarLineasLogNotificaciones - RGIIMVNotificarJob - Begin () ");
        int id = 0;
        PreparedStatement ps = null;
        try {
            String query = "INSERT INTO melanbide43_rgiimvnotificar_job (numexpediente, estadolog, descripcion) VALUES  (?,?,?)";
//            log.info("query: " + query);
            log.info(numeroExpediente + " - " + estado + " - " + mensaje);
            ps = con.prepareStatement(query);
            ps.setString(1, numeroExpediente);
            ps.setString(2, estado);
            ps.setString(3, mensaje);

            id = ps.executeUpdate();

        } catch (SQLException ex) {
            log.info("Se ha producido un error al registrar la linea en el log del job ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        return id;
    }

    /**
     * para obtener los D.A.N.
     *
     * @param numExpediente
     * @param esActivo
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public String getDatosNotificacionElectronica(String numExpediente, boolean esActivo, String codCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valorCampo = "";
        String query = null;
        String tabla = "E_TXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            query = "SELECT TXT_VALOR from " + tabla
                    + " WHERE TXT_NUM='" + numExpediente + "'"
                    + " AND TXT_COD='" + codCampo + "'";
//            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valorCampo = rs.getString("TXT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Error en getDatosNotificacionElectronica " + ex.getMessage(), ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
//        log.debug("getDatosNotificacionElectronica : " + numExpediente + " - " + valorCampo);
        return valorCampo;
    }

    /**
     *
     * @param codOrganizacion
     * @param numExpediente
     * @param esActivo
     * @param codCampo
     * @param nuevoValorCampo
     * @param con
     * @return
     * @throws Exception
     */
    public boolean actualizarDANxCodCampo(int codOrganizacion, String numExpediente, boolean esActivo, String codCampo, String nuevoValorCampo, Connection con) throws Exception {
        int result = 0;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        String query = null;
        String tabla = "E_TXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            // Quitamos los datos actuales si hay
            int ejercicio = Integer.parseInt(numExpediente.substring(0, 4));
            query = "delete from " + tabla
                    + " where TXT_MUN = ? and TXT_EJE = ? and TXT_NUM = ? and TXT_COD = ?";
            ps1 = con.prepareStatement(query);
            int i = 1;
            ps1.setInt(i++, codOrganizacion);
            ps1.setInt(i++, ejercicio);
            ps1.setString(i++, numExpediente);
            ps1.setString(i++, codCampo);
            result = ps1.executeUpdate();
            // Ahora insertamos el nuevo dato. 
        if (esActivo) {
                query = "insert into " + tabla
                        + " (TXT_MUN, TXT_EJE, TXT_NUM, TXT_COD, TXT_VALOR)"
                        + " values (?, ?, ?, ?, ?)";
        } else {
                query = "insert into " + tabla
                        + " (ID_PROCESO,TXT_MUN, TXT_EJE, TXT_NUM, TXT_COD, TXT_VALOR)"
                        + " values ((select ID_PROCESO from HIST_E_EXP where EXP_NUM='" + numExpediente + "'), ?, ?, ?, ?, ?)";
            }
            ps2 = con.prepareStatement(query);
            i = 1;
            ps2.setInt(i++, codOrganizacion);
            ps2.setInt(i++, ejercicio);
            ps2.setString(i++, numExpediente);
            ps2.setString(i++, codCampo);
            ps2.setString(i++, nuevoValorCampo);
            result += ps2.executeUpdate();
            return result > 0;
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error en actualizarDANxCodCampo ", ex);
            throw new Exception(ex);
        } catch (SQLException ex) {
            log.error("Se ha producido un error en actualizarDANxCodCampo ", ex);
            throw new Exception(ex);
        } finally {
            if (ps1 != null) {
                ps1.close();
            }
            if (ps2 != null) {
                ps2.close();
            }
        }
    }

    /**
     *
     * @param codOrganizacion
     * @param numExpediente
     * @param esActivo
     * @param codCampo
     * @param con
     * @return
     * @throws Exception
     */
    public boolean borrarDANxCodCampo(int codOrganizacion, String numExpediente, boolean esActivo, String codCampo, Connection con) throws Exception {
        int result = 0;
        PreparedStatement ps1 = null;
        String query = null;
        String tabla = "E_TXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            int ejercicio = Integer.parseInt(numExpediente.substring(0, 4));
            query = "delete from " + tabla
                    + " where TXT_MUN = ? and TXT_EJE = ? and TXT_NUM = ? and TXT_COD = ?";
            ps1 = con.prepareStatement(query);
            int i = 1;
            ps1.setInt(i++, codOrganizacion);
            ps1.setInt(i++, ejercicio);
            ps1.setString(i++, numExpediente);
            ps1.setString(i++, codCampo);
            result = ps1.executeUpdate();
            return result > 0;
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error en borrarDANxCodCampo ", ex);
            throw new Exception(ex);
        } catch (SQLException ex) {
            log.error("Se ha producido un error en borrarDANxCodCampo ", ex);
            throw new Exception(ex);
        } finally {
            if (ps1 != null) {
                ps1.close();
            }
        }
    }

    ////////////////////////////////////////
    ////    RGIActualizarDocumentoJob   ////
    ////////////////////////////////////////
    /**
     *
     * @param numeroExpediente
     * @param mensaje
     * @param resultado
     * @param con
     * @return
     * @throws Exception
     */
    public int insertarLineasLogCambioDoc(String numeroExpediente, String mensaje, String resultado, Connection con) throws Exception {
        log.info("insertarLineasLogCambioDoc -  Begin () ");
        int id = 0;
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "insert into MELANBIDE43_RGI_CAMBIODOC_JOB (EXP_NUM, OBSERVACIONES, CAMBIO_REGEX) values (?, ?, ?)";
            log.info("query: " + query);
            log.info(numeroExpediente + " - " + resultado + " - " + mensaje);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setString(i++, numeroExpediente);
            ps.setString(i++, mensaje);
            ps.setString(i++, resultado);

            id = ps.executeUpdate();

        } catch (SQLException ex) {
            log.info("Se ha producido un error al registrar la linea en el log del job ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("insertarLineasLogCambioDoc - End () ");
        return id;
    }

    /**
     *
     * @param numExp
     * @param mensaje
     * @param resultado
     * @param con
     * @return
     * @throws Exception
     */
    public int guardarResultadoActualizarInteresados(String numExp, String mensaje, String resultado, Connection con) throws Exception {
        log.info("guardarResultadoActualizarInteresados -  Begin () ");
        int id = 0;
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "update MELANBIDE43_RGI_CAMBIODOC_JOB set FECHA_SEDE = sysdate, CAMBIO_SEDE = ?, OBSERVACIONES = OBSERVACIONES || chr(45) || ' " + mensaje + "' where EXP_NUM = ?";
            log.info("query: " + query);
            log.info(numExp + " - " + resultado + " - " + mensaje);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setString(i++, resultado);
            ps.setString(i++, numExp);

            id = ps.executeUpdate();

        } catch (SQLException ex) {
            log.info("Se ha producido un error al registrar la linea en el log del job ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("guardarResultadoActualizarInteresados - End () ");
        return id;
    }

    /**
     * operacion que recupera los expedientes en los que ya se ha actualizado el
     * documento en Flexia y en Sede
     *
     * @param con
     * @return
     * @throws Exception
     */
    public ArrayList<String> getExpedientesActualizadosOK(Connection con) throws Exception {
        log.info("getExpedientesActualizadosOK - Begin () ");
        ArrayList<String> listaExpedientes = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select EXP_NUM from MELANBIDE43_RGI_CAMBIODOC_JOB where CAMBIO_REGEX = 'OK' and CAMBIO_SEDE = 'OK'";
            log.info("query: " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                listaExpedientes.add(rs.getString("EXP_NUM"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los expedientes actualizados ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
        } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedStatement y el resulset", e);
            throw new Exception(e);
            }
        }
        log.info("getExpedientesActualizadosOK - End () ");
        return listaExpedientes;
    }

    /**
     *
     * @param con
     * @return
     * @throws Exception
     */
    public List<ExpActualizarDocumentoVO> getExpedientesDistintoDoc(Connection con) throws Exception {
        log.info("getExpedientesDistintoDoc - Begin () ");
        List<ExpActualizarDocumentoVO> listaExpedientes = new ArrayList<ExpActualizarDocumentoVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select * from VW_EXPEDIENTES_RGI_SEDE_TITULAR where TITULARDIFERENTE='N' and DOCUMENTODIFERENTE='S' and PDTECREAR='N'";
            log.info("query: " + query);
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                ExpActualizarDocumentoVO expediente = new ExpActualizarDocumentoVO();
                expediente.setNumExp(rs.getString("EXP_NUM"));
                expediente.setNumDocRegex(rs.getString("NUMDOCREGEX"));
                expediente.setTipoDocRegex(rs.getString("TIPODOCREGEX"));
                expediente.setNumDocRGI(rs.getString("RGI_NUM_DOC"));
                expediente.setTipoDocRGI(rs.getString("RGI_TIPO_DOC"));
                expediente.setNombre(rs.getString("RGI_NOMBRE"));
                expediente.setApellido1(rs.getString("RGI_APELLIDO1"));
                expediente.setApellido2(rs.getString("RGI_APELLIDO2"));

                listaExpedientes.add(expediente);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los expedientes a tratar ", ex);
            throw new SQLException(ex);
        } finally {
            try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el preparedStatement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listaExpedientes;
    }

    ////////////////////////////
    ////    MIS GESTIONES   ////
    ////////////////////////////
    /**
     *
     * @param codOrganizacion
     * @param numExp
     * @param esActivo
     * @param con
     * @return
     * @throws Exception
     */
    public int guardarMiGestionRGI(int codOrganizacion, String numExp, boolean esActivo, Connection con) throws Exception {
        int id = 0;
        int result = 0;
        String query = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
            misGestiones = getInfoGestionesRGI(numExp, esActivo, con);
            Integer regInicio = null;
            Integer numInicio = null;
            Integer tipoCodInte = null;
            String codInte = "";
            String tramIni = "0";
            String regAsun = "";
            String fechaAsun = "";
            String[] datosExp = numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
            id = getIdGestiones(con);
            id++;
            if (!misGestiones.isEmpty()) {
                log.info("Existen datos para insertar en pestaña 'Mis gestiones' de este expediente");
                for (FilaListadoMisGestiones unidad : misGestiones) {
                    tipoCodInte = unidad.getTipoCodInte();
                    codInte = unidad.getCodInteresado();
                    tramIni = unidad.getTramiteInicio();
                    regInicio = unidad.getRegInicio();
                    numInicio = unidad.getNumInicio();
                    regAsun = unidad.getRegAsun();
                    fechaAsun = unidad.getFechaAsun();
                }
                query = "insert into MELANBIDE43_INTEGMISGEST"
                        + " (ID, EXP_NUM, TER_TID, TER_DOC, TIPO_OPERACION, "
                        + " COD_TRAMITE_INICIO,  FECHA_GENERADO,RES_EJE, RES_NUM, EXP_TIPO, REG_TELEMATICO, FECHA_TELEMATICO) "
                        + " values(" + id + ", '" + numExp + "', " + tipoCodInte + ", '" + codInte + "','T', '" + tramIni + "',"
                        + " to_date(to_char(sysdate,'dd/mm/yyyy HH24:mi:ss'), 'dd/mm/yyyy HH24:mi:ss'), "
                        + regInicio + ", " + numInicio + ", '" + datosExp[1] + "', '" + regAsun + "', '" + fechaAsun + "')";
//                log.debug("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
            }
        } catch (Exception ex) {
            log.error(" Se ha producido un error  ", ex);
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }

        }
        return result;
    }

    /**
     *
     * @param numExp
     * @param esActivo
     * @param con
     * @return
     * @throws Exception
     */
    private ArrayList<FilaListadoMisGestiones> getInfoGestionesRGI(String numExp, boolean esActivo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        ArrayList<FilaListadoMisGestiones> datosGestiones = new ArrayList<FilaListadoMisGestiones>();
        String tablas = null;
        if (esActivo) {
            tablas = "E_EXR,E_EXT,T_HTE,T_TID, E_EXP  ";
        } else {
            tablas = "HIST_E_EXR,HIST_E_EXT,T_HTE,T_TID, HIST_E_EXP  ";
        }
        try {
            query = "select EXR_EJR ejericicio_anotacion, EXR_NRE numero_registro, TID_COD tipo_doc,"
                    + " HTE_DOC documento, EXP_ASU "
                    + " from " + tablas
                    + " where EXT_NUM = EXP_NUM AND EXT_NUM = EXR_NUM(+)"
                    + " and HTE_TER = EXT_TER and HTE_NVR = EXT_NVR"
                    + " and HTE_TID = TID_cod and EXT_ROL = 1"
                    + " and EXR_TOP = 0 and EXT_NUM = '" + numExp + "'";
//            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            String asunto = "";
            String[] texto;
            String[] datosExp = numExp.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
            while (rs.next()) {
                FilaListadoMisGestiones unidad = new FilaListadoMisGestiones();
                unidad.setNumExp(numExp);
                if (rs.getString("tipo_doc") != null) {
                    unidad.setTipoCodInte(rs.getInt("tipo_doc"));
                }
                unidad.setCodInteresado(rs.getString("documento"));
                unidad.setTramiteInicio(String.valueOf(0));
                if (rs.getString("numero_registro") != null) {
                    unidad.setNumInicio(rs.getInt("numero_registro"));
                }
                if (rs.getString("ejericicio_anotacion") != null) {
                    unidad.setRegInicio(rs.getInt("ejericicio_anotacion"));
                }
                if (rs.getString("EXP_ASU") != null) {
                    asunto = rs.getString("EXP_ASU");
                }
                log.debug("-------------->asunto: " + asunto);
                if (asunto.toUpperCase().contains("NUMREGGV")) {
                    //creo un substring desde donde aparece la cadena "NumRegGV" hasta el final
                    String subs = (asunto.substring(asunto.toUpperCase().indexOf("NUMREGGV"), asunto.length())).trim();
//                    log.debug("-------------->subs: " + subs);
                    texto = subs.split(" ");
//                    log.debug("-------------->texto:length: " + texto.length);
                    if (texto.length > 1) {
//                        log.debug("-------------->texto_REG: " + texto[1]);
                        unidad.setRegAsun(texto[1]);
                    }
                    //creo un substring desde donde aparece la cadena "FechaRegGV" hasta el final
                    subs = (asunto.substring(asunto.toUpperCase().indexOf("FECHAREGGV"), asunto.length())).trim();
                    texto = subs.split(" ");
//                    log.debug("-------------->texto:length: " + texto.length);
                    if (texto.length > 1) {
                        log.debug("-------------->texto_FECHA: " + texto[1]);
                        unidad.setFechaAsun(texto[1]);
                    }
                } else {
                    String hoy = formatoFechaAsunto.format(Calendar.getInstance().getTime());
                    log.info("Es expediente " + datosExp[1] + " sin fecha en el asunto, se graba la fecha actual con el formato que  los registros: " + hoy);
                    unidad.setFechaAsun(hoy);
                }
                datosGestiones.add(unidad);

            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando getInfoGestiones", ex);
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }

        }
        return datosGestiones;
    }

    /**
     *
     * @param con
     * @return
     * @throws Exception
     */
    public int getIdGestiones(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        int id = 0;
        try {
            query = "select MAX(ID) cod from MELANBIDE43_INTEGMISGEST ";
//            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                id = rs.getInt("cod");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando ID de MELANBIDE43_INTEGMISGEST ", ex);
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return id;
    }

    /**
     *
     * @param id
     * @param esActivo
     * @param con
     * @return
     * @throws Exception
     */
    public int actualizarGestionActInteresados(int id, boolean esActivo, Connection con) throws Exception {
        Statement st = null;
        int result = 0;
        String query = null;
        String tabla = "E_EXT";
        if (!esActivo) {
            tabla = "HIST_" + tabla;
        }
        try {
            query = "update MELANBIDE43_INTEGMISGEST"
                    + " set FECHA_PROCESADO = to_date(to_char(sysdate,'dd/mm/yyyy HH24:mi:ss'), 'dd/mm/yyyy HH24:mi:ss'), "
                    + " TER_DOC = (\n"
                    + "select RTRIM (XMLAGG(XMLELEMENT (E,\n"
                    + " 'ROL=' || ext_rol || ', ',\n"
                    + " 'DOC=' || hte_doc || ', ',\n"
                    + " 'NOM=' || hte_noc || ', ',\n"
                    + " 'MARCA_NOT=' || ext_notificacion_electronica || ', ',\n"
                    + " 'COD_TER=' || ext_ter || ', ',\n"
                    + " 'NVR=' || ext_nvr || ', ',\n"
                    + " 'DOM=' || ext_dot || ' '\n"
                    + "|| CHR(13))).EXTRACT ('//text()').GETCLOBVAL(),',') INTERESADOS \n"
                    + " from " + tabla
                    + " inner join T_HTE on T_HTE.HTE_TER=" + tabla + ".EXT_TER and T_HTE.HTE_NVR=" + tabla + ".EXT_NVR\n"
                    + " where EXT_NUM=(select EXP_NUM from MELANBIDE43_INTEGMISGEST where ID=" + id + ")\n"
                    + "), "
                    + " RESULTADO_PROCESO = '1' WHERE ID =  " + id;
//            log.debug("sql = " + query);
            st = con.createStatement();
            result = st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Error en actualizarGestionActInteresados " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return result;
    }

    /**
     *
     * @param tipoVia
     * @param nombreVia
     * @param numDesde
     * @param letraDesde
     * @param numHasta
     * @param letraHasta
     * @param bloque
     * @param portal
     * @param escalera
     * @param planta
     * @param puerta
     * @return
     */
    private static String construirDireccion(String tipoVia, String nombreVia, String numDesde, String letraDesde, String numHasta, String letraHasta, String bloque, String portal, String escalera, String planta, String puerta) {
        StringBuilder dirBuffer = new StringBuilder();
        if (tipoVia != null && !"".equals(tipoVia.trim())) {
            dirBuffer.append(tipoVia).append(" ");
        }
        dirBuffer.append(nombreVia).append(" ");
        if (numDesde != null && !numDesde.trim().isEmpty()) {
            dirBuffer.append(numDesde).append(" ");
            }
        if (letraDesde != null && !letraDesde.trim().isEmpty()) {
            dirBuffer.append(letraDesde).append(" ");
        }
        if ((numHasta != null && !numHasta.trim().isEmpty()) || (letraHasta != null && !letraHasta.trim().isEmpty())) {
            dirBuffer.append(" - ");
            if (numHasta != null && !numHasta.trim().isEmpty()) {
                dirBuffer.append(numHasta).append(" ");
    }
            if (letraHasta != null && !letraHasta.trim().isEmpty()) {
                dirBuffer.append(letraHasta).append(" ");
            }
        }
        if (bloque != null && !bloque.trim().isEmpty()) {
            dirBuffer.append("BLQ. ").append(bloque).append(" ");
        }
        if (portal != null && !portal.trim().isEmpty()) {
            dirBuffer.append("POR. ").append(portal).append(" ");
        }
        if (escalera != null && !escalera.trim().isEmpty()) {
            dirBuffer.append("ESC. ").append(escalera).append(" ");
        }
        if (planta != null && !planta.trim().isEmpty()) {
            dirBuffer.append(planta).append("º ");
        }
        if (puerta != null && !puerta.trim().isEmpty()) {
            dirBuffer.append(puerta).append(" ");
        }
        return dirBuffer.toString().trim();
    }

    /**
     * Comprueba si un interesado de un expediente está asociado a una
     * notificación
     *
     * @param autorizado
     * @param codNotificacion
     * @param con
     * @return
     * @throws Exception
     */
    private boolean estaAutorizadoEnNotificacion(AutorizadoNotificacionVO autorizado, int codNotificacion, Connection con) throws Exception {
        boolean retorno = false;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select CODIGO_NOTIFICACION from AUTORIZADO_NOTIFICACION "
                    + " where CODIGO_NOTIFICACION = ? AND COD_MUNICIPIO= ? AND EJERCICIO = ? AND NUM_EXPEDIENTE = ? and COD_TERCERO= ? and VER_TERCERO= ?";
//            log.debug(query);
            ps = con.prepareStatement(query);
            int i = 1;
            ps.setInt(i++, codNotificacion);
            ps.setInt(i++, autorizado.getCodigoMunicipio());
            ps.setInt(i++, autorizado.getEjercicio());
            ps.setString(i++, autorizado.getNumeroExpediente());
            ps.setInt(i++, autorizado.getCodigoTercero());
            ps.setInt(i++, autorizado.getNumeroVersionTercero());

            rs = ps.executeQuery();
            if (rs.next()) {
                retorno = true;
            }
        } catch (SQLException e) {
            log.error(" Se ha producido un error comprobando si  el tercero está autorizado en la notificación ", e);
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
                e.printStackTrace();
            }
        }
        return retorno;
    }
}
