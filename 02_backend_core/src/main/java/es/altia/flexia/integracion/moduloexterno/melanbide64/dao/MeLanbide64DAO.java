package es.altia.flexia.integracion.moduloexterno.melanbide64.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide64.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide64.util.ConstantesMeLanbide64;
import es.altia.flexia.integracion.moduloexterno.melanbide64.util.MeLanbide64MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide64.vo.TramiteVO;
import es.altia.webservice.client.tramitacion.ws.wto.DireccionVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide64DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide64DAO.class);
    //Instancia
    private static MeLanbide64DAO instance = null;

    // Constructor
    private MeLanbide64DAO() {

    }

    //Devolvemos una única instancia de la clase a través de este método ya que el constructor es privado
    public static MeLanbide64DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide64DAO.class) {
                instance = new MeLanbide64DAO();
            }
        }
        return instance;
    }

    /**
     * recuperamos el usuario y la unidad org.del trámite que llega a la
     * operación para iniciar el expediente REINT con esos datos
     *
     * @param codMunicipio
     * @param numExpediente
     * @param procedimientoPadre
     * @param con
     * @param codTramite
     * @return
     * @throws Exception
     */
    public static ExpedienteVO getDatosExpedienteUsuUor(int codMunicipio, String numExpediente, String procedimientoPadre, int codTramite, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        ExpedienteVO exp = null;

        try {
            String query = "select CRO_UTR, CRO_USU from E_CRO where cro_pro=? and CRO_NUM=? and CRO_TRA=?";
            st = con.prepareStatement(query);
            st.setString(1, procedimientoPadre);
            st.setString(2, numExpediente);
            st.setInt(3, codTramite);
            log.debug("sql: " + query);
            log.debug("sql parametros procedimientoPadre: " + procedimientoPadre);
            log.debug("sql parametros numExpediente: " + numExpediente);
            log.debug("sql parametros codTramite: " + codTramite);
            rs = st.executeQuery();
            if (rs.next()) {
                exp = (ExpedienteVO) MeLanbide64MappingUtils.getInstance().map(rs, ExpedienteVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return exp;
    }

    public static List<InteresadoExpedienteVO> getDatosInteresado(int codOrganizacion, Integer ejercicio, String numExpediente, Connection con) throws SQLException, Exception {
        PreparedStatement st = null;
        ResultSet rs = null;

        String query = null;
        TerceroVO tercero = null;
        InteresadoExpedienteVO interesado = null;
        List<InteresadoExpedienteVO> interesados = new ArrayList<InteresadoExpedienteVO>();

        try {
            query = "SELECT EXT_ROL FROM E_EXT WHERE EXT_NUM='" + numExpediente + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                interesado = (InteresadoExpedienteVO) MeLanbide64MappingUtils.getInstance().map(rs, InteresadoExpedienteVO.class);

                if (interesado != null) {
                    try {
                        tercero = MeLanbide64DAO.getTercerosExpediente(codOrganizacion, ejercicio, numExpediente, con, rs.getString("EXT_ROL"));
                        interesado.setTercero(tercero);
                        interesados.add(interesado);
                        // domicilio = MeLanbide64DAO.getDomicilioTercero(tercero,codTercero, con);
                        // tercero.setDomicilio(domicilio);
                    } catch (Exception ex) {
                        log.error("Se ha producido un error al recuperar el domicilio del tercero " + tercero.getDomicilio() + " para el expediente " + numExpediente, ex);
                    }

                }
                //interesados.add(interesado);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return interesados;
    }

    public static TerceroVO getTercerosExpediente(int codMunicipio, Integer ejercicio, String numExp, Connection con, String rol) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;

        String query = null;
        int historico = 0;
        String codTercero = null;

        int version = 0;
        TerceroVO tercero = null;
        DireccionVO domicilio = null;

        try {
            /*query = "select t.ter_cod, t.ter_doc, t.ter_nve, h.hte_ter, h.hte_doc, h.hte_nvr,h.hte_tlf,t.ter_dce,"
                   +" case when t.ter_cod is not null and t.ter_doc is not null then 0 else 1 end as HISTORICO"
                   +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide64.FICHERO_PROPIEDADES)+" rel"
                   +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_TERCEROS, ConstantesMeLanbide64.FICHERO_PROPIEDADES)+" t on t.ter_cod = rel.ext_ter and t.ter_nve = rel.ext_nvr"
                   +" left join "+ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_TERCEROS_HIST, ConstantesMeLanbide64.FICHERO_PROPIEDADES)+" h on h.hte_ter = rel.ext_ter and h.hte_nvr = rel.ext_nvr"
                   +" where ext_rol = 1 and ext_mun = ? and ext_eje = ? and ext_num = ?";*/
            query = "SELECT h.hte_ter codigo,h.hte_doc documento, h.hte_nvr nvr, h.hte_tlf tlf, h.hte_dce dce, 1 as HISTORICO"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide64.FICHERO_PROPIEDADES) + " rel"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_TERCEROS_HIST, ConstantesMeLanbide64.FICHERO_PROPIEDADES) + " h on h.hte_ter = rel.ext_ter and h.hte_nvr = rel.ext_nvr"
                    + " where  ext_mun = ? and ext_eje = ? and ext_num = ? and ext_rol = ?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            log.debug("VALORES DE getTercerosExpediente = " + codMunicipio + " " + ejercicio + " " + numExp + " " + rol);
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setInt(2, ejercicio);
            st.setString(3, numExp);
            st.setString(4,rol);
            rs = st.executeQuery();

            while (rs.next()) {
                codTercero = rs.getString("codigo");
                historico = rs.getInt("HISTORICO");
                version = rs.getInt("nvr");

                //codTercero = rs.getString("hte_ter");
                //  historico = rs.getInt("HISTORICO");
                // version = rs.getString("hte_nvr");
                tercero = MeLanbide64DAO.getTerceroHistorico(codMunicipio, ejercicio, numExp, codTercero, version, con);
                if (tercero != null) {
                    try {
                        domicilio = MeLanbide64DAO.getDomicilioTercero(tercero, codTercero, con);
                        tercero.setDomicilio(domicilio);
                    } catch (Exception ex) {
                        log.error("Se ha producido un error al recuperar el domicilio del tercero " + tercero.getDomicilio() + " para el expediente " + numExp, ex);
                    }

                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tercero;

    }

    private static TerceroVO getTerceroHistorico(int codMunicipio, Integer ejercicio, String numExpediente, String codTercero, int version, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        TerceroVO ter = null;
        try {
            String query = null;
            query = "select "
                    + " HTE_TER COD, HTE_NVR VER, HTE_TID TID, HTE_DOC DOC, HTE_NOM NOM,"
                    + " HTE_AP1 AP1, HTE_PA1 PA1, HTE_AP2 AP2, HTE_PA2 PA2, HTE_NOC NOC, HTE_NML NML,"
                    + " HTE_TLF TLF, HTE_DCE DCE, HTE_APL APL, EXTERNAL_CODE EXTERNAL_CODE,"
                    + " null SIT, null FAL, null UAL, null FBJ, null UBJ, null DOM, EXT_ROL ROL"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_TERCEROS_HIST, ConstantesMeLanbide64.FICHERO_PROPIEDADES) + " h"
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_REL_TERCERO_DOMICILIO, ConstantesMeLanbide64.FICHERO_PROPIEDADES) + " r"
                    + " on h.hte_ter = r.ext_ter and h.hte_nvr = r.ext_nvr"
                    + " where r.ext_mun = ? and r.ext_eje = ? and r.ext_num = ? and r.ext_ter = ? and r.ext_nvr = ?";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setInt(2, ejercicio);
            st.setString(3, numExpediente);
            st.setString(4, codTercero);
            log.debug("codTercero: " + codTercero);
            st.setInt(5, version);
            log.debug("version: " + version);
            rs = st.executeQuery();
            if (rs.next()) {
                ter = (TerceroVO) MeLanbide64MappingUtils.getInstance().map(rs, TerceroVO.class);
            }
            return ter;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando tercero " + codTercero, ex);
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

    private static DireccionVO getDomicilioTercero(TerceroVO ter, String codTercero, Connection con) throws Exception {

        if (ter != null && codTercero != null) {
            String codigoTercero;
            PreparedStatement st = null;
            ResultSet rs = null;
            DireccionVO dom = null;
            try {
                String query = null;
                /* query = "select dom.*, via.VIA_NOM from "+ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_DOMICILIO, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                        +" dom inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_T_DOT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                        +" dot on dom.DNN_DOM = dot.DOT_DOM"
                        +" inner join "+ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_T_VIA, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                        +" via on dom.DNN_VIA = via.VIA_COD"
                        +" where dot.DOT_TER = ?";
                 */

                query = "select dom.*,via.VIA_NOM "
                        + "from t_ter "
                        + "left join  " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_DOMICILIO, ConstantesMeLanbide64.FICHERO_PROPIEDADES) + " dom on dnn_dom=ter_dom"
                        + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_T_VIA, ConstantesMeLanbide64.FICHERO_PROPIEDADES) + " via on dom.DNN_VIA = via.VIA_COD  and dom.DNN_PRV=via.VIA_PRV and dom.DNN_MUN=via.VIA_MUN "
                        + " where ter_cod=? ";

                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                st = con.prepareStatement(query);

                st.setString(1, codTercero);
                log.debug("codigo tercero: " + codTercero);
                rs = st.executeQuery();
                //dom.setEsPrincipal(true);
                if (rs.next()) {
                    dom = (DireccionVO) MeLanbide64MappingUtils.getInstance().map(rs, DireccionVO.class);
                }
                return dom;
            } catch (Exception ex) {
                // log.error("Se ha producido un error recuperando domicilio tercero "+ter.get, ex);
                throw new Exception(ex);
            } finally {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        } else {
            return null;
        }
    }
    
    //#361396 Se modifica el método para quitar envío procedimiento REINT
    public static ArrayList<String> getDatoSuplementariosNuevosMELANBIDE64_PROC_REINT(int codOrganizacion, String numExpediente, String procedimientoPadre, int ano, Connection con) throws SQLException, Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        ArrayList<String> datos = new ArrayList();
        String query = null;
        
        try {
            //query = "select REI_PRO_DEUDA,REI_AREA_DEUDA from MELANBIDE64_PROC_REINT where REI_PRO='" + procedimientoPadre + "' AND REI_EJE='" + ano + "'";
            query = "select REI_AREA_DEUDA from MELANBIDE64_PROC_REINT where REI_PRO='" + procedimientoPadre + "' AND REI_EJE='" + ano + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                //datos.add(rs.getString("REI_PRO_DEUDA"));
                datos.add(rs.getString("REI_AREA_DEUDA"));

            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando getAnoActual ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return datos;
    }

    public static void getInsertREI_AREA_DEUDA(int codOrganizacion, ExpedienteVO exp2, String REI_AREA_DEUDA, Connection con) throws Exception {
        log.debug("getInsertREI_PRO_DEUDA() - BEGIN()");
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO e_tde  VALUES (" + codOrganizacion + "," + exp2.getIdExpedienteVO().getEjercicio() + ",'" + exp2.getIdExpedienteVO().getNumeroExpediente() + "','CODAREA','" + REI_AREA_DEUDA + "')";
            log.debug("getInsertREI_AREA_DEUDA - sql insert relacion convocatorias anteriores s75 = " + sql);
            st = con.createStatement();
            st.executeUpdate(sql);
            log.debug("SQL:" + sql);
        } catch (SQLException ex) {
            log.error("Se ha producido un error insertando la relacion de expediente getInsertREI_PRO_DEUDA");
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
        log.debug("getInsertREI_AREA_DEUDA - END()");
    }

    public static void getInsert_NUMEXPSUPLEMENTARIO(int codOrganizacion, String numExpediente, ExpedienteVO exp2, String procedimientoPadre, int ano, Connection con) throws Exception {
        log.debug("getInsert_NUMEXPSUPLEMENTARIO() - BEGIN()");
        Statement st = null;
        ResultSet rs = null;
        try {
            String sql = "INSERT INTO e_txt  VALUES (" + codOrganizacion + "," + exp2.getIdExpedienteVO().getEjercicio() + ",'" + exp2.getIdExpedienteVO().getNumeroExpediente() + "','NUMEXPSUPLEMENTARIO','" + numExpediente + "')";
            log.debug("getInsert_NUMEXPSUPLEMENTARIO - sql insert relacion convocatorias anteriores s75 = " + sql);
            st = con.createStatement();
            st.executeUpdate(sql);
            log.debug("SQL:" + sql);
        } catch (SQLException ex) {
            log.error("Se ha producido un error insertando la relacion de expediente getInsertREI_PRO_DEUDA");
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
        log.debug("getInsert_NUMEXPSUPLEMENTARIO - END()");
    }

    public static ExpedienteVO getDatosExpediente(int codMunicipio, String numExp, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        ExpedienteVO exp = null;

        try {
            /*String query = "select EXP_PRO, EXP_EJE, EXP_NUM, EXP_MUN, EXP_UOR, EXP_OBS, EXP_ASU"
                          +" from "+ConfigurationParameter.getParameter(ConstantesMeLanbide56.TABLA_E_EXP, ConstantesMeLanbide56.FICHERO_PROPIEDADES)
                          +" where EXP_MUN = ? and EXP_NUM = ?";*/

            String query = "select distinct exp.EXP_PRO PRO, exp.EXP_EJE EJE, exp.EXP_NUM NUM, exp.EXP_MUN MUN, exp.EXP_UOR UOR, exp.EXP_OBS OBS, exp.EXP_ASU ASU,exp.EXP_USU USU,"
                    + " exr.EXR_DEP DEP,"
                    + " res.RES_TDO TDO, res.RES_NTR NTR, res.RES_AUT AUT,ecro.cro_utr utr"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_EXP, ConstantesMeLanbide64.FICHERO_PROPIEDADES) + " exp"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_EXR, ConstantesMeLanbide64.FICHERO_PROPIEDADES) + " exr"
                    + " on exp.exp_pro = exr.exr_pro and exp.exp_eje = exr.exr_eje and exp.exp_num = exr.exr_num"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_R_RES, ConstantesMeLanbide64.FICHERO_PROPIEDADES) + " res"
                    + " on exr.exr_nre = res.res_num and exr.exr_eje = res.res_eje"
                    + " left join E_cro ecro on ecro.CRO_NUM=exp.exp_num and  exp.EXP_EJE=ecro.CRO_EJE"
                    + " where exp.EXP_MUN = ? and exp.EXP_NUM = ?";
            st = con.prepareStatement(query);
            st.setInt(1, codMunicipio);
            st.setString(2, numExp);
            log.debug("sql: " + query);
            rs = st.executeQuery();
            if (rs.next()) {
                exp = (ExpedienteVO) MeLanbide64MappingUtils.getInstance().map(rs, ExpedienteVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando datos del expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return exp;
    }

    public static IdExpedienteVO getDatosIdExpediente(String numExpediente, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;

        String query = null;
        IdExpedienteVO idexpediente = null;
        try {
            query = "SELECT EXP_EJE,EXP_PRO,EXP_NUM,EXP_TRA FROM E_EXP WHERE EXP_NUM='" + numExpediente + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                idexpediente = (IdExpedienteVO) MeLanbide64MappingUtils.getInstance().map(rs, IdExpedienteVO.class);
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando getDatosIdExpediente el expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return idexpediente;
    }

    public static int getcodigoUORrecuperadodebd(int codOrganizacion, String numExpediente, String procedimientoPadre, int codTramite, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = null;
        int respuesta = 0;

        try {
            query = "select CRO_UTR from E_CRO where cro_pro='" + procedimientoPadre + "' AND CRO_NUM='" + numExpediente + "' and CRO_TRA=" + codTramite;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                respuesta = rs.getInt("CRO_UTR");

            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando getAnoActual ", ex);
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

    public static int getUsuarioExpediente(int codOrganizacion, String numExpediente, String procedimientoPadre, int codTramite, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = null;
        int respuesta = 0;
        log.debug("Numero del tramite en el que estamos:" + codTramite);
        try {
            query = "select CRO_USU from E_CRO where cro_pro='" + procedimientoPadre + "' AND CRO_NUM='" + numExpediente + "' and CRO_TRA=" + codTramite + "";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            rs = st.executeQuery();
            while (rs.next()) {
                respuesta = rs.getInt("CRO_USU");
                log.debug(" usuario del tamite que estamos:" + respuesta);
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando getAnoActual ", ex);
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

    public boolean tieneNotificacionElectronica(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int resultado = 0;
        try {
            String sql = "select count(*) from E_EXT "
                    + " where EXT_NOTIFICACION_ELECTRONICA =1 "
                    + " and EXT_NUM = '" + numExpediente + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }

            st = con.createStatement();
            rs = st.executeQuery(sql);
            rs.next();
            resultado = rs.getInt(1);

        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando si tiene notificación electrónica el Expediente : " + numExpediente, ex);
            throw new Exception(ex);
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

    public void cerrarTramite(String numExp, int codTramite, Connection con) throws Exception {

        Statement st = null;
        try {

            /*            String sql = "UPDATE "+ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_CRO, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " SET CRO_FEF = NVL(CRO_FFP, SYSDATE), CRO_USF = NVL(CRO_USF, '5') "
                    + "WHERE cro_num = '"+numExp+"' "
                    + "and cro_tra not in '"+codTramite+"'";                   
             */
            String sql = "UPDATE E_CRO SET CRO_FEF = SYSDATE, CRO_USF='5'  WHERE cro_num = '" + numExp + "' AND CRO_FEF IS NULL "
                    + " and cro_tra not in '" + codTramite + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            st = con.createStatement();
            st.executeUpdate(sql);
            int rowsUpdated = st.executeUpdate(sql);
            log.debug("Número de tramites cerrados " + rowsUpdated);
        } catch (SQLException ex) {
            log.error("Se ha producido un error cerrando los tramites " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }

        }
    }

    public ArrayList<TramiteVO> getCamposTramite(TramiteVO tramite, Connection con) throws SQLException, Exception {
        log.debug("INICIA getCAmposTramite DAO ");

        Statement st = null;
        ResultSet rs = null;
        String query = null;
        ArrayList<TramiteVO> listaCamposTramite = new ArrayList<TramiteVO>();

        // ---- tabla numericos -- codigo tabla = 1
        try {
            query = "select TNUT_COD, TNUT_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TNUT_NUM='" + tramite.getNumExpediente() + "'"
                    + " and TNUT_TRA=" + tramite.getCodTramite()
                    + " and TNUT_OCU=" + tramite.getOcurrenciaTramite();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TNUT_COD"));
                tram.setValorCampo(rs.getString("TNUT_VALOR"));
                tram.setCodigoTabla(1);
                log.debug("Campo numérico recuperado: " + tram.getNomCampo());
                listaCamposTramite.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo NUMÉRICO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        // ---------- tabla texto -- codigo tabla = 2
        try {
            query = "select TXTT_COD, TXTT_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TXTT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TXTT_NUM='" + tramite.getNumExpediente() + "'"
                    + " and TXTT_TRA=" + tramite.getCodTramite()
                    + " and Txtt_OCU=" + tramite.getOcurrenciaTramite();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TXTT_COD"));
                tram.setValorCampo(rs.getString("TXTT_VALOR"));
                tram.setCodigoTabla(2);
                log.debug("Campo TEXTO recuperado: " + tram.getNomCampo());
                listaCamposTramite.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo TEXTO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
// ---------- tabla FECHA -- codigo tabla = 3
        try {
            query = "select TFET_COD, TFET_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFET, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFET_NUM='" + tramite.getNumExpediente() + "'"
                    + " and TFET_TRA=" + tramite.getCodTramite()
                    + " and TFET_OCU=" + tramite.getOcurrenciaTramite();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TFET_COD"));
                tram.setValorCampo(rs.getString("TFET_VALOR"));
                tram.setCodigoTabla(3);
                log.debug("Campo FECHA recuperado: " + tram.getNomCampo());
                listaCamposTramite.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo FECHA", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
// ---------- tabla texto LARGO -- codigo tabla = 4
        try {
            query = "select TTLT_COD, TTLT_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TTLT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TTLT_NUM='" + tramite.getNumExpediente() + "'"
                    + " and TTLT_TRA=" + tramite.getCodTramite()
                    + " and Ttlt_OCU=" + tramite.getOcurrenciaTramite();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TTLT_COD"));
                tram.setValorCampo(rs.getString("TTLT_VALOR"));
                tram.setCodigoTabla(4);
                log.debug("Campo TEXTO LARGO recuperado: " + tram.getNomCampo());
                listaCamposTramite.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo TEXTO LARGO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
// ---------- tabla FICHERO -- codigo tabla = 5
        try {
            query = "select TFIT_COD, TFIT_NOMFICH from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFIT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFIT_NUM='" + tramite.getNumExpediente() + "'"
                    + " and TFIT_TRA=" + tramite.getCodTramite()
                    + " and TFIT_ocu=" + tramite.getOcurrenciaTramite();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TFIT_COD"));
                tram.setValorCampo(rs.getString("TFIT_NOMFICH"));
                tram.setCodigoTabla(2);
                log.debug("Campo FICHERO recuperado: " + tram.getNomCampo());
                listaCamposTramite.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo FICHERO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        // ---------- tabla DESPLEGABLES -- codigo tabla = 6
        try {
            query = "select TDET_COD, TDET_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDET, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TDET_NUM='" + tramite.getNumExpediente() + "'"
                    + " and TDET_TRA=" + tramite.getCodTramite()
                    + " and Tdet_OCU=" + tramite.getOcurrenciaTramite();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TDET_COD"));
                tram.setValorCampo(rs.getString("TDET_VALOR"));
                tram.setCodigoTabla(6);
                log.debug("Campo DESPLEGABLE recuperado: " + tram.getNomCampo());
                listaCamposTramite.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        // ---------- tabla NUMERICO CALCULADO -- codigo tabla = 8
        try {
            query = "select TNUCT_COD, TNUCT_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUCT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TNUCT_NUM='" + tramite.getNumExpediente() + "'"
                    + " and TNUCT_TRA=" + tramite.getCodTramite()
                    + " and TNUct_OCU=" + tramite.getOcurrenciaTramite();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TNUCT_COD"));
                tram.setValorCampo(rs.getString("TNUCT_VALOR"));
                tram.setCodigoTabla(8);
                log.debug("Campo NUMÉRICO CALCULADO recuperado: " + tram.getNomCampo());
                listaCamposTramite.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo NUMÉRICO CALCULADO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        // ---------- tabla FECHA CALCULADA -- codigo tabla = 9
        try {
            query = "select TFECT_COD, TFECT_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFECT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFECT_NUM='" + tramite.getNumExpediente() + "'"
                    + " and TFECT_TRA=" + tramite.getCodTramite()
                    + " and Tfect_OCU=" + tramite.getOcurrenciaTramite();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TFECT_COD"));
                tram.setValorCampo(rs.getString("TFECT_VALOR"));
                tram.setCodigoTabla(9);
                log.debug("Campo FECHA CALCULADA recuperado: " + tram.getNomCampo());
                listaCamposTramite.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo FECHA CALCULADA", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        // ---------- tabla DESPLEGABLE EXTERNO-- codigo tabla = 10
        try {
            query = "select TDEXT_COD, TDEXT_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDEXT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TDEXT_NUM='" + tramite.getNumExpediente() + "'"
                    + " and TDEXT_TRA=" + tramite.getCodTramite()
                    + " and Tdext_OCU=" + tramite.getOcurrenciaTramite();
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TDEXT_COD"));
                tram.setValorCampo(rs.getString("TDEXT_VALOR"));
                tram.setCodigoTabla(10);
                log.debug("Campo DESPLEGABLE EXTERNO recuperado: " + tram.getNomCampo());
                listaCamposTramite.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        log.debug("FINALIZA getCAmposTramite DAO ");

        return listaCamposTramite;
    }// getCamposTramite

    public boolean existeCampoExpediente(TramiteVO tramite, Connection con) throws SQLException, Exception {
        log.debug("==========existe campo");
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        boolean existe = false;
        try {
            query = "select PCA_COD from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_SUPLEMENTARIOS_PROCEDIMIENTO, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where PCA_PRO= (select EXP_PRO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_EXP, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where EXP_NUM='" + tramite.getNumExpediente() + "')";
            log.debug("Query: "+query);
            
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                if (rs.getString("PCA_COD").equals(tramite.getNomCampo())) {
                    existe = true;
                }
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando las unidades tramitadoras del expediente", e);
            existe = false;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return existe;
    }// existeCampoExpediente

    public void grabarCamposExpediente(int codOrganizacion, TramiteVO tramite, Connection con) throws SQLException, Exception {
        int codigoTabla = tramite.getCodigoTabla();

        try {
            boolean nuevo = false;
            String ejerSt = tramite.getNumExpediente().substring(0, 4);
            Integer ejer = Integer.valueOf(tramite.getNumExpediente().substring(0, 4));
            Statement st = null;
            ResultSet rs = null;
            String query = null;
            String fechaCortada = "";
            String[] partes = null;
            String fechaVolteada = "";

            if (codigoTabla > 0) {
                switch (codigoTabla) {
                    case 1:
                        log.debug("Campo numerico");
                        if (this.getValorCampoNumerico(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNU, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", "
                                    + ejer + ", '"
                                    + tramite.getNumExpediente() + "', '"
                                    + tramite.getNomCampo() + "', "
                                    + tramite.getValorCampo() + ")";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNU, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TNU_VALOR = " + tramite.getValorCampo()
                                    + " where TNU_MUN = '" + codOrganizacion + "'"
                                    + " and TNU_EJE = " + ejer
                                    + " and TNU_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TNU_COD = '" + tramite.getNomCampo() + "'";
                        }
                        break;

                    case 2:
                        log.debug("Campo Texto.");
                        if (this.getValorCampoTexto(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TXT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", "
                                    + ejer + ", '" + tramite.getNumExpediente() + "', '"
                                    + tramite.getNomCampo() + "', '"
                                    + tramite.getValorCampo() + "')";

                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TXT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TXT_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TXT_MUN = '" + codOrganizacion + "'"
                                    + " and TXT_EJE = " + ejer
                                    + " and TXT_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TXT_COD = '" + tramite.getNomCampo() + "'";
                        }
                        break;

                    case 3:
                        log.debug("Campo Fecha.");

                        log.debug("Tramite.getValor: " + tramite.getValorCampo());
                        fechaCortada = tramite.getValorCampo().substring(0, 10);
                        log.debug("Fecha cortada: " + fechaCortada);
                        partes = fechaCortada.split("-");
                        fechaVolteada = partes[2] + "/" + partes[1] + "/" + partes[0];
                        log.debug("Fecha Volteada: " + fechaVolteada);

                        if (this.getValorCampoFecha(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }

                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFE, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", " + ejer + ", '" + tramite.getNumExpediente() + "', '" + tramite.getNomCampo() + "', '" + fechaVolteada + "','','')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFE, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TFE_VALOR = '" + fechaVolteada + "'"
                                    + " where TFE_MUN = '" + codOrganizacion + "'"
                                    + " and TFE_EJE = " + ejer
                                    + " and TFE_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TFE_COD = '" + tramite.getNomCampo() + "'";
                        }
                        break;

                    case 4:
                        log.debug("Texto largo");
                        if (this.getValorCampoTextoLargo(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TTL, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", " + ejer + ", '" + tramite.getNumExpediente() + "', '" + tramite.getNomCampo() + "', '" + tramite.getValorCampo() + "')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TTL, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TTL_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TTL_MUN = '" + codOrganizacion + "'"
                                    + " and TTL_EJE = " + ejer
                                    + " and TTL_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TTL_COD = '" + tramite.getNomCampo() + "'";
                        }
                        break;

                    case 5:
                        log.debug("Fichero");
                        if (this.getValorCampoFichero(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            //insert into E_TFI values (0,2010,'2010/PRUEB/000001','DATO',null, '','fichero',50);
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFI, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", " + ejer + ", '" + tramite.getNumExpediente() + "', '" + tramite.getNomCampo() + "', '" + tramite.getValorCampo() + "',' ','')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFI, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TFI_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TFI_MUN = '" + codOrganizacion + "'"
                                    + " and TFI_EJE = " + ejer
                                    + " and TFI_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TFI_COD = '" + tramite.getNomCampo() + "'";
                        }
                        break;

                    case 6:
                        log.debug("Desplegable");
                        if (this.getValorCampoDesplegable(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDE, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", " + ejer + ", '" + tramite.getNumExpediente() + "', '" + tramite.getNomCampo() + "', '" + tramite.getValorCampo() + "')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDE, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TDE_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TDE_MUN = '" + codOrganizacion + "'"
                                    + " and TDE_EJE = " + ejer
                                    + " and TDE_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TDE_COD = '" + tramite.getNomCampo() + "'";
                        }
                        break;

                    case 8:
                        log.debug("Numérico calculado");
                        if (this.getValorCampoNumericoCalculado(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUC, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", " + ejer + ", '" + tramite.getNumExpediente() + "', '" + tramite.getNomCampo() + "', " + tramite.getValorCampo() + ")";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUC, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TNUC_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TNUC_MUN = '" + codOrganizacion + "'"
                                    + " and TNUC_EJE = " + ejer
                                    + " and TNUC_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TNUC_COD = '" + tramite.getNomCampo() + "'";
                        }
                        break;

                    case 9:
                        log.debug("Fecha calculada");
                        fechaCortada = tramite.getValorCampo().substring(0, 10);
                        partes = fechaCortada.split("-");
                        fechaVolteada = partes[2] + "/" + partes[1] + "/" + partes[0];
                        if (this.getValorCampoFechaCalculada(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFEC, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", " + ejer + ", '" + tramite.getNumExpediente() + "', '" + tramite.getNomCampo() + "', '" + fechaVolteada + "','','')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFEC, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TFEC_VALOR = '" + fechaVolteada + "'"
                                    + " where TFEC_MUN = '" + codOrganizacion + "'"
                                    + " and TFEC_EJE = " + ejer
                                    + " and TFEC_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TFEC_COD = '" + tramite.getNomCampo() + "'";
                        }
                        break;

                    case 10:
                        log.debug("Desplegable externo");
                        if (this.getValorCampoDesplegableExterno(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDEX, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", " + ejer + ", '" + tramite.getNumExpediente() + "', '" + tramite.getNomCampo() + "', '" + tramite.getValorCampo() + "')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDEX, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TDEX_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TDEX_MUN = '" + codOrganizacion + "'"
                                    + " and TDEX_EJE = " + ejer
                                    + " and TDEX_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TDEX_COD = '" + tramite.getNomCampo() + "'";
                        }
                        break;

                    default:
                        log.error("Se ha recibido un código de tabla erroneo");
                        break;
                }//switch

                if (query.isEmpty()) {
                    log.error("no se ha cargado la query");

                } else {
                    log.debug("INSERT a ejecutar: " + query);
                    try {
                        st = con.createStatement();
                        st.executeUpdate(query);

                    } catch (SQLException e) {
                        log.error("Se ha producido un error insertando el campo suplementario del expediente", e);
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                        if (st != null) {
                            st.close();
                        }
                    }// try-catch  
                }// if query.equals("")
            } else {
                log.error("El trámite no trae código de tabla");
            }// if codigoTabla>0

        } catch (Exception ex) {
            log.error("ERROR INDETERMINADO: " + ex);
        }
    }// grabar campos tramite

    public ArrayList<TramiteVO> getCamposExpediente(TramiteVO tramite, Connection con) throws SQLException, Exception {
        log.debug("INICIA getCAmposExpediente DAO ");
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        ArrayList<TramiteVO> listaCamposExpediente = new ArrayList<TramiteVO>();
        // --- tabla NUMERICOS -- codigo tabla = 1
        try {
            query = "select TNU_COD, TNU_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNU, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TNU_NUM='" + tramite.getNumExpediente() + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TNU_COD"));
                tram.setValorCampo(rs.getString("TNU_VALOR"));
                tram.setCodigoTabla(1);
                log.debug("Campo numérico recuperado: " + tram.getNomCampo());
                listaCamposExpediente.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo NUMÉRICO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        // --- tabla texto -- codigo tabla = 2
        try {
            query = "select TXT_COD, TXT_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TXT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TXT_NUM='" + tramite.getNumExpediente() + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TXT_COD"));
                tram.setValorCampo(rs.getString("TXT_VALOR"));
                tram.setCodigoTabla(2);
                log.debug("Campo texto recuperado: " + tram.getNomCampo());
                listaCamposExpediente.add(tram);
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo TEXTO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        // --- tabla fecha -- codigo tabla = 3
        try {
            query = "select TFE_COD, TFE_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFE, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFE_NUM='" + tramite.getNumExpediente() + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TFE_COD"));
                tram.setValorCampo(rs.getString("TFE_VALOR"));
                tram.setCodigoTabla(3);
                log.debug("Campo FECHA recuperado: " + tram.getNomCampo());
                listaCamposExpediente.add(tram);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo FECHA", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        // --- tabla TEXTO LARGO -- codigo tabla = 4
        try {
            query = "select TTL_COD, TTL_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TTL, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TTL_NUM='" + tramite.getNumExpediente() + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TTL_COD"));
                tram.setValorCampo(rs.getString("TTL_VALOR"));
                tram.setCodigoTabla(4);
                log.debug("Campo TEXTO LARGO recuperado: " + tram.getNomCampo());
                listaCamposExpediente.add(tram);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo TEXTO LARGO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        // --- tabla FICHERO -- codigo tabla = 5
        try {
            query = "select TFI_COD, TFI_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFI, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFI_NUM='" + tramite.getNumExpediente() + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TFI_COD"));
                tram.setValorCampo(rs.getString("TFI_VALOR"));
                tram.setCodigoTabla(5);
                log.debug("Campo FICHERO recuperado: " + tram.getNomCampo());
                listaCamposExpediente.add(tram);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo FICHERO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        // --- tabla DESPLEGABLE -- codigo tabla = 6
        try {
            query = "select TDE_COD, TDE_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDE, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TDE_NUM='" + tramite.getNumExpediente() + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TDE_COD"));
                tram.setValorCampo(rs.getString("TDE_VALOR"));
                tram.setCodigoTabla(6);
                log.debug("Campo DESPLEGABLE recuperado: " + tram.getNomCampo());
                listaCamposExpediente.add(tram);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo DESPLEGABLE", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        // --- tabla NUMERICO CALCULADO -- codigo tabla = 8
        try {
            query = "select TNUC_COD, TNUC_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUC, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TNUC_NUM='" + tramite.getNumExpediente() + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TNUC_COD"));
                tram.setValorCampo(rs.getString("TNUC_VALOR"));
                tram.setCodigoTabla(8);
                log.debug("Campo NUMERICO CALCULADO recuperado: " + tram.getNomCampo());
                listaCamposExpediente.add(tram);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo NUMERICO CALCULADO", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        // --- tabla FECHA CALCULADA -- codigo tabla = 9
        try {
            query = "select TFEC_COD, TFEC_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFEC, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFEC_NUM='" + tramite.getNumExpediente() + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TFEC_COD"));
                tram.setValorCampo(rs.getString("TFEC_VALOR"));
                tram.setCodigoTabla(9);
                log.debug("Campo FECHA CALCULADA recuperado: " + tram.getNomCampo());
                listaCamposExpediente.add(tram);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo FECHA CALCULADA ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        // --- tabla DESPLEGABLE EXTERNO -- codigo tabla = 10
        try {
            query = "select TDEX_COD, TDEX_VALOR from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDEX, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TDEX_NUM='" + tramite.getNumExpediente() + "'";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                TramiteVO tram = new TramiteVO();
                tram.setNumExpediente(tramite.getNumExpediente());
                tram.setCodTramite(tramite.getCodTramite());
                tram.setOcurrenciaTramite(tramite.getOcurrenciaTramite());
                tram.setNomCampo(rs.getString("TDEX_COD"));
                tram.setValorCampo(rs.getString("TDEX_VALOR"));
                tram.setCodigoTabla(4);
                log.debug("Campo DESPLEGABLE EXTERNO recuperado: " + tram.getNomCampo());
                listaCamposExpediente.add(tram);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los campos suplementarios tipo DESPLEGABLE EXTERNO ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }

        return listaCamposExpediente;
    }// getCamposExpediente

    public boolean existeCampoTramite(TramiteVO tramite, Connection con) throws SQLException, Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        boolean existe = false;
        try {
            query = "select TCA_COD from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TCA, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TCA_PRO = (select EXP_PRO from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_EXP, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where EXP_NUM='" + tramite.getNumExpediente() + "') and TCA_TRA ='" + tramite.getCodTramite() + "'";

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                if (rs.getString("TCA_COD").equals(tramite.getNomCampo())) {
                    existe = true;
                }
            }

        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando las unidades tramitadoras del expediente", e);

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return existe;
    }// existeCampoTramite

    public void grabarCamposTramite(int codOrganizacion, TramiteVO tramite, Connection con) throws SQLException, Exception {
        int codigoTabla = tramite.getCodigoTabla();
        Statement st = null;
        ResultSet rs = null;
        try {
            boolean nuevo = false;
            String numExpediente = tramite.getNumExpediente();
            String ejerSt = numExpediente.substring(0, 4);
            Integer ejer = Integer.valueOf(tramite.getNumExpediente().substring(0, 4));

            String query = null;
            String fechaCortada = "";
            String fechaVolteada = "";
            String[] partes = numExpediente.split("/");
            String codProcedimiento = partes[1];
            String[] partesT = null;
            if (codigoTabla > 0) {
                switch (codigoTabla) {
                    case 1:

                        log.debug("Campo numerico tramite: " + tramite.getNomCampo());
                        if (this.getValorCampoNumericoTramite(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getCodTramite(),tramite.getOcurrenciaTramite(), tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " (TNUT_MUN, TNUT_PRO, TNUT_EJE, TNUT_NUM, TNUT_TRA,TNUT_OCU, TNUT_COD, TNUT_VALOR)"
                                    + " values (" + codOrganizacion + ", '"
                                    + codProcedimiento + "', "
                                    + ejer + ", '"
                                    + tramite.getNumExpediente() + "', "
                                    + tramite.getCodTramite() + ", "
                                    + tramite.getOcurrenciaTramite()
                                    + ", '"
                                    + tramite.getNomCampo() + "', "
                                    + tramite.getValorCampo() + ")";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TNUT_VALOR = " + tramite.getValorCampo()
                                    + " where TNUT_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TNUT_TRA ='" + tramite.getCodTramite() + "'"
                                    + " and TNUT_COD ='" + tramite.getNomCampo() + "'";
                        }
                        break;
                    case 2:
                        log.debug("Campo Texto. " + tramite.getNomCampo());
                        if (this.getValorCampoTextoTramite(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getCodTramite(),tramite.getOcurrenciaTramite(), tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TXTT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " (TXTT_MUN, TXTT_PRO, TXTT_EJE, TXTT_NUM, TXTT_TRA,TXTT_OCU, TXTT_COD, TXTT_VALOR)"
                                    + " values (" + codOrganizacion
                                    + ", '" + codProcedimiento + "', "
                                    + ejer 
                                    + ", '" + tramite.getNumExpediente() + "', "
                                    + tramite.getCodTramite() + ", " 
                                    + tramite.getOcurrenciaTramite()
                                    + ", '" + tramite.getNomCampo() + "', '" + tramite.getValorCampo() + "')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TXTT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TXTT_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TXTT_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TXTT_TRA ='" + tramite.getCodTramite() + "'"
                                    + " and TXTT_COD ='" + tramite.getNomCampo() + "'";
                        }
                        break;

                    case 3:
                        log.debug("Campo Fecha." + tramite.getValorCampo());
                        fechaCortada = tramite.getValorCampo().substring(0, 10);
                        partesT = fechaCortada.split("-");
                        fechaVolteada = partesT[2] + "/" + partesT[1] + "/" + partesT[0];
                        if (this.getValorCampoFechaTramite(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getCodTramite(),tramite.getOcurrenciaTramite(), tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFET, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", '" + codProcedimiento + "', " + ejer + ", '" + tramite.getNumExpediente() + "', " + tramite.getCodTramite() + ", " + tramite.getOcurrenciaTramite() + ", '" + tramite.getNomCampo() + "', '" + fechaVolteada + "','','')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFET, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TFET_VALOR = '" + fechaVolteada + "'"
                                    + " where TFET_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TFET_TRA ='" + tramite.getCodTramite() + "'"
                                    + " and TFET_COD ='" + tramite.getNomCampo() + "'";
                        }
                        break;
                    case 4:
                        log.debug("Texto largo " + tramite.getNomCampo());
                        if (this.getValorCampoTextoLargoTramite(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getCodTramite(),tramite.getOcurrenciaTramite(), tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TTLT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", '" + codProcedimiento + "', " + ejer + ", '" + tramite.getNumExpediente() + "', " + tramite.getCodTramite() + ", " + tramite.getOcurrenciaTramite() + ", '" + tramite.getNomCampo() + "', '" + tramite.getValorCampo() + "')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TTLT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TTLT_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TTLT_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TTLT_TRA ='" + tramite.getCodTramite() + "'"
                                    + " and TTLT_COD ='" + tramite.getNomCampo() + "'";
                        }
                        break;
                    case 5:
                        log.debug("Fichero " + tramite.getNomCampo());
//                  query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFIT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
//                        + " values (" + codOrganizacion + ", '" + codProcedimiento + "', " + ejer + ", '" + tramite.getNumExpediente() + "', " + tramite.getCodTramite() + ", " + tramite.getOcurrenciaTramite() + ", '" + tramite.getNomCampo() + "', '" + tramite.getValorCampo() + "','', '', '', 'FLEXIA')";
                        break;
                    case 6:
                        log.debug("Desplegable " + tramite.getNomCampo());
                        if (this.getValorCampoDesplegableTramite(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getCodTramite(),tramite.getOcurrenciaTramite(), tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDET, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", '" + codProcedimiento + "', " + ejer + ", '" + tramite.getNumExpediente() + "', " + tramite.getCodTramite() + ", " + tramite.getOcurrenciaTramite() + ", '" + tramite.getNomCampo() + "', '" + tramite.getValorCampo() + "')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDET, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TDET_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TDET_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TDET_TRA ='" + tramite.getCodTramite() + "'"
                                    + " and TDET_COD ='" + tramite.getNomCampo() + "'";
                        }
                        break;
                    case 8:
                        log.debug("Némerico calculado " + tramite.getNomCampo());
                         if (this.getValorCampoNumericoCalculadoTramite(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getCodTramite(),tramite.getOcurrenciaTramite(), tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUCT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", '" + codProcedimiento + "', " + ejer + ", '" + tramite.getNumExpediente() + "', " + tramite.getCodTramite() + ", " + tramite.getOcurrenciaTramite() + ", '" + tramite.getNomCampo() + "', " + tramite.getValorCampo() + ")";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUCT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TNUCT_VALOR = " + tramite.getValorCampo()
                                    + " where TNUCT_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TNUCT_TRA ='" + tramite.getCodTramite() + "'"
                                    + " and TNUCT_COD ='" + tramite.getNomCampo() + "'";
                        }
                        break;
                    case 9:
                        log.debug("Fecha calculada " + tramite.getNomCampo());
                        fechaCortada = tramite.getValorCampo().substring(0, 10);
                        partesT = fechaCortada.split("-");
                        fechaVolteada = partesT[2] + "/" + partesT[1] + "/" + partesT[0];
                         if (this.getValorCampoFechaTramite(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getCodTramite(),tramite.getOcurrenciaTramite(),tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFECT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", '"
                                    + codProcedimiento + "', "
                                    + ejer + ", '"
                                    + tramite.getNumExpediente() + "', "
                                    + tramite.getCodTramite() + ", "
                                    + tramite.getOcurrenciaTramite() + ", '"
                                    + tramite.getNomCampo() + "', '"
                                    + fechaVolteada + "','','')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFECT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TFECT_VALOR = '" + fechaVolteada + "'"
                                    + " where TFECT_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TFECT_TRA ='" + tramite.getCodTramite() + "'"
                                    + " and TFECT_COD ='" + tramite.getNomCampo() + "'";
                        }
                        break;
                    case 10:
                        log.debug("Desplegable externo " + tramite.getNomCampo());
                          if (this.getValorCampoDesplegableExternoTramite(codOrganizacion, tramite.getNumExpediente(), ejerSt, tramite.getCodTramite(),tramite.getOcurrenciaTramite(), tramite.getNomCampo(), con) == null) {
                            nuevo = true;
                        }
                        if (nuevo) {
                            query = "insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDEXT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " values (" + codOrganizacion + ", '" + codProcedimiento + "', " + ejer + ", '" + tramite.getNumExpediente() + "', " + tramite.getCodTramite() + ", " + tramite.getOcurrenciaTramite() + ", '" + tramite.getNomCampo() + "', '" + tramite.getValorCampo() + "','')";
                        } else {
                            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDEXT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                                    + " set TDEXT_VALOR = '" + tramite.getValorCampo() + "'"
                                    + " where TDEXT_NUM = '" + tramite.getNumExpediente() + "'"
                                    + " and TDEXT_TRA ='" + tramite.getCodTramite() + "'"
                                    + " and TDEXT_COD ='" + tramite.getNomCampo() + "'";
                        }

                        break;
                    default:
                        log.error("codigo de tabla erroneo");
                        break;
                }//switch
                if (query.isEmpty()) {
                    log.error("no se ha cargado la query");
                } else {
                    log.debug("INSERT a ejecutar: " + query);
                    try {
                        st = con.createStatement();
                         rs = st.executeQuery(query);
//                        int result = st.executeUpdate(query);

                    } catch (SQLException e) {
                        log.error("Se ha producido un error insertando el campo suplementario del expediente", e);
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                        if (st != null) {
                            st.close();
                        }
                    }// try-catch  
                }// if query.equals("")

            } else {
                log.error("El trámite no trae código de tabla");
            }

        } catch (Exception ex) {
            log.error("ERROR INDETERMINADO: " + ex);
        }

    }// grabar campos expediente

    public BigDecimal getValorCampoNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try {
            String query = null;
            query = "select TNU_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNU, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TNU_MUN = '" + codOrganizacion + "' and TNU_EJE = '" + ejercicio
                    + "' and TNU_NUM = '" + numExp + "' and TNU_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getBigDecimal("TNU_VALOR");
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
        return valor;
    }

    public String getValorCampoTexto(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;

        try {
            String query = null;
            query = "select TXT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TXT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TXT_MUN = '" + codOrganizacion + "' and TXT_EJE = '" + ejercicio
                    + "' and TXT_NUM = '" + numExp + "' and TXT_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TXT_VALOR");
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

        return valor;
    }

    public String getValorCampoFecha(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TFE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFE, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFE_MUN = '" + codOrganizacion + "' and TFE_EJE = '" + ejercicio
                    + "' and TFE_NUM = '" + numExp + "' and TFE_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TFE_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario FECHA " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoTextoLargo(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TTL_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TTL, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TTL_MUN = '" + codOrganizacion + "' and TTL_EJE = '" + ejercicio
                    + "' and TTL_NUM = '" + numExp + "' and TTL_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TTL_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario TEXTO LARGO " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoFichero(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TFI_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFI, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFI_MUN = '" + codOrganizacion + "' and TFI_EJE = '" + ejercicio
                    + "' and TFI_NUM = '" + numExp + "' and TFI_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TFI_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario FICHERO " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDE, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TDE_MUN = '" + codOrganizacion + "' and TDE_EJE = '" + ejercicio
                    + "' and TDE_NUM = '" + numExp + "' and TDE_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDE_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario desplegable " + codigoCampo + " para el expediente " + numExp, ex);
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

    public BigDecimal getValorCampoNumericoCalculado(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try {
            String query = null;
            query = "select TNUC_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUC, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TNUC_MUN = '" + codOrganizacion + "' and TNUC_EJE = '" + ejercicio
                    + "' and TNUC_NUM = '" + numExp + "' and TNUC_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getBigDecimal("TNUC_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario NUMERICO CALCULADO " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoFechaCalculada(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TFEC_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFEC, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFEC_MUN = '" + codOrganizacion + "' and TFEC_EJE = '" + ejercicio
                    + "' and TFEC_NUM = '" + numExp + "' and TFEC_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TFEC_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario FECHA CALCULADA " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoDesplegableExterno(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDEX_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDEX, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TDEX_MUN = '" + codOrganizacion + "' and TDEX_EJE = '" + ejercicio
                    + "' and TDEX_NUM = '" + numExp + "' and TDEX_COD = '" + codigoCampo + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDEX_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario FECHA " + codigoCampo + " para el expediente " + numExp, ex);
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

    public BigDecimal getValorCampoNumericoTramite(int codOrganizacion, String numExp, String ejercicio, int tramite,int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;

        try {
            String query = null;
            query = "select TNUT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TNUT_MUN = '" + codOrganizacion + "' and TNUT_EJE = '" + ejercicio
                    + "' and TNUT_NUM = '" + numExp + "' and TNUT_TRA ='" + tramite + "' and TNUT_COD = '" + codigoCampo + "'"
                    + " and tnut_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
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
        return valor;
    }// get numerico tramite

    public String getValorCampoTextoTramite(int codOrganizacion, String numExp, String ejercicio, int tramite,int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TXTT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TXTT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TXTT_MUN = '" + codOrganizacion + "' and TXTT_EJE = '" + ejercicio
                    + "' and TXTT_NUM = '" + numExp + "' and TXTT_TRA ='" + tramite + "' and TXTT_COD = '" + codigoCampo + "'"
                    + " and tnut_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
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
        return valor;
    }//get valor campo texto tramite

    public String getValorCampoFechaTramite(int codOrganizacion, String numExp, String ejercicio, int tramite,int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TFET_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFET, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFET_MUN = '" + codOrganizacion + "' and TFET_EJE = '" + ejercicio
                    + "' and TFET_NUM = '" + numExp + "' and TFET_COD = '" + codigoCampo + "'"
                    + " and tfet_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TFET_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario FECHA " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoTextoLargoTramite(int codOrganizacion, String numExp, String ejercicio, int tramite,int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TTLT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TTLT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TTLT_MUN = '" + codOrganizacion + "' and TTLT_EJE = '" + ejercicio
                    + "' and TTLT_NUM = '" + numExp + "' and TTLT_COD = '" + codigoCampo + "'"
                    + " and ttlt_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TTLT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario TEXTO LARGO " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoFicheroTramite(int codOrganizacion, String numExp, String ejercicio, int tramite,int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TFIT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFIT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFIT_MUN = '" + codOrganizacion + "' and TFIT_EJE = '" + ejercicio
                    + "' and TFIT_NUM = '" + numExp + "' and TFIT_COD = '" + codigoCampo + "'"
                    + " and tfit_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TFIT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario FICHERO " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoDesplegableTramite(int codOrganizacion, String numExp, String ejercicio, int tramite,int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDET_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDET, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TDET_MUN = '" + codOrganizacion + "' and TDET_EJE = '" + ejercicio
                    + "' and TDET_NUM = '" + numExp + "' and TDET_COD = '" + codigoCampo + "'"
                    + " and tdet_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDET_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario desplegable " + codigoCampo + " para el expediente " + numExp, ex);
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

    public BigDecimal getValorCampoNumericoCalculadoTramite(int codOrganizacion, String numExp, String ejercicio, int tramite,int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try {
            String query = null;
            query = "select TNUCT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TNUCT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TNUCT_MUN = '" + codOrganizacion + "' and TNUCT_EJE = '" + ejercicio
                    + "' and TNUCT_NUM = '" + numExp + "' and TNUCT_COD = '" + codigoCampo + "'"
                    + " and tnuct_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getBigDecimal("TNUCT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario NUMERICO CALCULADO " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoFechaCalculadaTramite(int codOrganizacion, String numExp, String ejercicio, int tramite,int  ocurrencia,String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TFECT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TFECT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TFECT_MUN = '" + codOrganizacion + "' and TFECT_EJE = '" + ejercicio
                    + "' and TFECT_NUM = '" + numExp + "' and TFECT_COD = '" + codigoCampo + "'"
                    + " and tfect_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TFECT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario FECHA CALCULADA " + codigoCampo + " para el expediente " + numExp, ex);
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

    public String getValorCampoDesplegableExternoTramite(int codOrganizacion, String numExp, String ejercicio, int tramite,int ocurrencia, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDEXT_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_TDEXT, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " where TDEXT_MUN = '" + codOrganizacion + "' and TDEXT_EJE = '" + ejercicio
                    + "' and TDEXT_NUM = '" + numExp + "' and TDEXT_COD = '" + codigoCampo + "'"
                    + " and tdext_ocu=" + ocurrencia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDEXT_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el campo suplementario FECHA " + codigoCampo + " para el expediente " + numExp, ex);
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

    public boolean tramiteImpideAbierto(int codOrganizacion, String ejercicio, String codProcedimiento, String numExpediente, int codTramite, Connection con) throws Exception{
        log.debug(">> ENTRA en tramiteImpideAbierto() - Exp: "+numExpediente);
        boolean result = false;
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query= "SELECT CRO_TRA " 
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide64.TABLA_E_CRO, ConstantesMeLanbide64.FICHERO_PROPIEDADES)
                    + " WHERE CRO_NUM ='"+numExpediente+"'"
                    + " AND CRO_EJE="+ejercicio
                    + " AND CRO_PRO='"+codProcedimiento+"'"
                    + " AND CRO_FEF IS NULL";
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                if(codTramite==rs.getInt("CRO_TRA")){
                    result=true;
                }
                
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los códigos de trámites");
        }finally {            
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);                
            }
        }  
        log.debug("SALE de tramiteImpideAbierto() >>");
        return result;
    }
    
    
    public boolean grabarPlazoRecurso(int codOrganizacion, String ejercicio, String codProcedimiento, String numExpediente, String codTramite, String codOcurrencia, Connection con) throws Exception{
        log.debug(">> ENTRA en grabarPlazoRecurso() - Exp: "+numExpediente);
        boolean result = false;
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        Date fechaAcuse = null;
        //Recuperar fecha acuse del 
         try{
            query = "SELECT CRO_FIP "
                    + "FROM E_CRO "
                    + "WHERE CRO_NUM = '" + numExpediente + "' "
                    + "AND CRO_TRA = " + codTramite;
            log.debug("SQL: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                fechaAcuse = rs.getDate(1);
            }
            if( fechaAcuse == null){
                log.error("No se ha podido recuperar la fecha de acuse, fechaInicio = null");
            }else{
                log.debug("Formatear fecha recuperada");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fechaAcuseFormat = simpleDateFormat.format(fechaAcuse);
                log.debug("Datos del expediente " + numExpediente + " recuperados, fecha inicio plazo: " + fechaAcuseFormat);
                
                query = "UPDATE E_CRO "
                        + "SET E_CRO.CRO_FLI = (SELECT ADD_MONTHS(TO_DATE(SUBSTR('"+fechaAcuseFormat+"',0,10),'DD/MM/YYYY'),1) FROM DUAL) "
                        + "WHERE CRO_NUM = '"+numExpediente+"' " 
                        + "AND CRO_TRA = " + codTramite + " "
                        + "AND CRO_OCU = " + codOcurrencia;
                
                log.debug("SQL: " + query);
                st = con.createStatement();
                st.executeUpdate(query);
                result = true;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al intentar grabar el plazo de recurso", ex);
            throw new Exception(ex);
        }finally{
              try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);                
            }
         }
        log.debug(">> SALE de grabarPlazoRecurso() - Exp: "+numExpediente);
        return result;
    }
}// class
