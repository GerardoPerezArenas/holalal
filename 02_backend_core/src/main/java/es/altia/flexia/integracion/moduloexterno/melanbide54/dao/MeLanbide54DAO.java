/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide54.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide54.util.MeLanbide54MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.CentroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.RegistroAACCVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.RegistroBatchVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.SelectItem;
import es.altia.flexia.notificacion.vo.NotificacionVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide54DAO {

    protected static String pca_mun;
    protected static String pca_pro;
    protected static String pca_cod;
    protected static String pca_des;
    protected static String pca_plt;
    protected static String pca_tda;
    protected static String pca_tam;
    protected static String pca_mas;
    protected static String pca_obl;
    protected static String pca_nor;
    protected static String pca_rot;
    protected static String pca_activo;
    protected static String pca_desplegable;

    protected static String plt_cod;
    protected static String plt_des;
    protected static String plt_url;

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide54DAO.class);

    //Instancia
    private static MeLanbide54DAO instance = null;

    private MeLanbide54DAO() {

    }

    public static MeLanbide54DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide54DAO.class) {
                instance = new MeLanbide54DAO();
            }
        }
        return instance;
    }

    // Metodos de Recupracion de datos
    public List<CentroVO> recogeCentros(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        List<CentroVO> centros = new ArrayList<CentroVO>();
        CentroVO centro = new CentroVO();
        boolean paso = false;
        try {
            query = "SELECT ID, CODTTHH,  "
                    + "CASE WHEN CODTTHH = 1 THEN 'Álava' "
                    + "WHEN CODTTHH = 20 THEN 'Guipuzcoa' "
                    + "WHEN CODTTHH = 48 THEN 'Vizcaya' END as DESCODTTHH, "
                    + "CODMUN, (SELECT MUN_NOM FROM FLBGEN.T_MUN WHERE MUN_PRV=CODTTHH AND MUN_COD = CODMUN) AS DESCODMUN, "
                    //                    + "CALLE, POR, PLT, PTA, CPO, TLF, EMAIL "
                    + "CALLE, POR, PLT, PTA, CPO, TLF, EMAIL, CODSEPE "
                    + "FROM MELANBIDE54_CENTROS WHERE EXP_NUM = '" + numExp + "' ";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                centro.setId(rs.getString("ID"));
                centro.setCodTH(rs.getString("CODTTHH"));
                centro.setDesCodTH(rs.getString("DESCODTTHH"));
                centro.setCodMun(rs.getString("CODMUN"));
                centro.setDesCodMun(rs.getString("DESCODMUN"));
                centro.setCalle(rs.getString("CALLE"));
                centro.setPortal(rs.getString("POR"));
                centro.setPiso(rs.getString("PLT"));
                centro.setLetra(rs.getString("PTA"));
                centro.setCp(rs.getString("CPO"));
                centro.setTlef(rs.getString("TLF"));
                centro.setEmail(rs.getString("EMAIL"));
                centro.setCodsepe(rs.getString("CODSEPE"));
                centros.add(centro);
                centro = new CentroVO();
            }
        } catch (Exception ex) {
            //opeCorrecta = false;
            log.error("Se ha producido un error en recogeCentros - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return centros;
    }

    public CentroVO getCentroPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        CentroVO centro = new CentroVO();
        try {
            String query = null;
            query = "SELECT * FROM MELANBIDE54_CENTROS "
                    + " WHERE EXP_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY EXP_NUM";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                centro.setId(rs.getString("ID"));
                centro.setCodTH(rs.getString("CODTTHH"));
                centro.setCodMun(rs.getString("CODMUN"));
                centro.setCalle(rs.getString("CALLE"));
                centro.setPortal(rs.getString("POR"));
                centro.setPiso(rs.getString("PLT"));
                centro.setLetra(rs.getString("PTA"));
                centro.setCp(rs.getString("CPO"));
                centro.setTlef(rs.getString("TLF"));
                centro.setEmail(rs.getString("EMAIL"));
                centro.setCodsepe(rs.getString("CODSEPE"));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Datos centros por codigo" + numExpediente + " -" + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return centro;
    }

    public List<SelectItem> recogeMunicipios(String th, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        List<SelectItem> lista = new ArrayList<SelectItem>();
        SelectItem muni = new SelectItem();
        boolean paso = false;
        try {
            query = "SELECT * FROM FLBGEN.T_MUN WHERE MUN_PRV = " + th + " ORDER BY MUN_NOM ";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                muni.setId(rs.getString("MUN_COD"));
                muni.setLabel(rs.getString("MUN_NOM"));

                lista.add(muni);
                muni = new SelectItem();
            }
        } catch (Exception ex) {
            //opeCorrecta = false;
            log.error("Se ha producido un error en recogeMunicipios - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return lista;
    }

    public boolean crearNuevoCentro(CentroVO centro, String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            int id = recogeIdInsertar(numExp, con);
            query = "INSERT INTO MELANBIDE54_CENTROS "
                    + "(ID, EXP_NUM, CODTTHH, CODMUN, CALLE, POR, PLT, PTA, CPO, TLF, EMAIL) "
                    + " VALUES (" + id + ",'" + numExp + "'," + centro.getCodTH() + ","
                    + centro.getCodMun() + ",'" + centro.getCalle() + "', '" + centro.getPortal()
                    + "', '" + centro.getPiso() + "', '" + centro.getLetra() + "', '"
                    + centro.getCp() + "', '" + centro.getTlef() + "', '" + centro.getEmail() + "')";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar un nueva Especialidad" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean actualizaCentro(CentroVO centro, String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE MELANBIDE54_CENTROS  SET "
                    + " CODTTHH = " + centro.getCodTH() + ", CODMUN = " + centro.getCodMun()
                    + ", CALLE = '" + centro.getCalle() + "', POR= '" + centro.getPortal()
                    + "', PLT = '" + centro.getPiso() + "', PTA = '" + centro.getLetra()
                    + "', CPO = '" + centro.getCp() + "', TLF = '" + centro.getTlef()
                    + "', EMAIL = '" + centro.getEmail()
                    + "', CODSEPE = '" + centro.getCodsepe()
                    + "' WHERE ID = " + centro.getId() + " AND  EXP_NUM = '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error en actualizaCentro" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean eliminaCentro(String id, String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "DELETE FROM MELANBIDE54_CENTROS  "
                    + " WHERE ID = " + id + " AND  EXP_NUM = '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error en eliminaCentro" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int recogeIdInsertar(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        int id = 1;
        try {
            query = "SELECT MAX(ID) as num FROM MELANBIDE54_CENTROS WHERE EXP_NUM = '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("num") + 1;
            }
        } catch (Exception ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error en recogeIdInsertar" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return id;
    }
//#

    public List<RegistroAACCVO> getRegistrosTelematicosAACC(Connection con) throws Exception {
        log.debug(" BEGIN getRegistrosTelematicosAACC  ");
        Statement st = null;
        ResultSet rs = null;
        List<RegistroAACCVO> listReg = new ArrayList<RegistroAACCVO>();
        try {
            String query = null;

            query = "SELECT RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM, RES_USU, RES_TER, RES_ASU"
                    + " FROM R_RES "
                    + "WHERE RES_TIP = 'E'"
                    + "  AND REGISTRO_TELEMATICO = 1"
                    + "  AND ASUNTO = 'AUAC'"
                    + "  AND PROCEDIMIENTO = 'AACC'"
                    + "  AND RES_EST = 0"
                    + " ORDER BY RES_FEC DESC, RES_NUM DESC";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                listReg.add((RegistroAACCVO) MeLanbide54MappingUtils.getInstance().map(rs, RegistroAACCVO.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getRegistrosTelematicosAACC", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getRegistrosTelematicosAACC  ");
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
        return listReg;
    }

    public static InteresadoExpedienteVO getDatosInteresado(int codOrganizacion, int ejercicio, RegistroAACCVO registro, Connection con) throws SQLException, Exception {
        PreparedStatement st = null;
        ResultSet rs = null;

        String query = null;
//        TerceroVO tercero = null;
//        InteresadoExpedienteVO interesado = null;
        TerceroVO tercero = new TerceroVO();
        InteresadoExpedienteVO interesado = new InteresadoExpedienteVO();
        interesado.setRol(1);

        try {
            tercero = MeLanbide54DAO.getTercero(codOrganizacion, ejercicio, registro, con);
            interesado.setTercero(tercero);
        } catch (Exception ex) {
            log.error("Se ha producido un error al recuperar el domicilio del tercero " + tercero.getDomicilio() + " para el registro " + registro.getResEje() + "/" + registro.getResNum(), ex);
        }
        return interesado;
    }

    public static TerceroVO getTercero(int codMunicipio, int ejercicio, RegistroAACCVO registro, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        TerceroVO tercero = null;

        try {
            String query = null;
            query = "SELECT TER_AP1 AP1, TER_AP2 AP2, TER_DOC DOC,TER_DCE DCE,TER_NOM NOM,TER_TLF TLF, TER_TID TID,"
                    + "DNN_BLQ, DNN_MUN, DNN_PAI, DNN_CPO, DNN_PRV, DNN_DMC, DNN_ESC, VIA_NOM, DNN_PLT, DNN_POR, DNN_NUD,"
                    + "DNN_LED, DNN_PTA, DNN_TVI, DNN_LEH, DNN_NUH"
                    + " FROM T_TER"
                    + " LEFT JOIN T_DNN ON (DNN_DOM=TER_DOM)"
                    + " LEFT JOIN T_TVI ON (DNN_TVI=TVI_COD)"
                    + " LEFT JOIN T_VIA ON (DNN_VIA=VIA_COD and VIA_PAI=DNN_PAI and VIA_PRV=DNN_PRV and VIA_MUN=DNN_MUN)"
                    + " LEFT JOIN T_PRV ON (DNN_PRV=PRV_COD)"
                    + " LEFT JOIN T_MUN ON (DNN_MUN=MUN_COD and DNN_PRV=MUN_PRV)"
                    + " WHERE TER_COD = ?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            log.debug("cod.Tercero = " + registro.getResTer());
            st = con.prepareStatement(query);
            st.setInt(1, registro.getResTer());
            rs = st.executeQuery();

            while (rs.next()) {
                tercero = (TerceroVO) MeLanbide54MappingUtils.getInstance().map(rs, TerceroVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos del tercero " + registro.getResTer(), ex);
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
        return tercero;

    }

    // public String BuscarXML(Connection con, RegistroAACCVO registro) throws Exception
    public byte[] BuscarXML(Connection con, RegistroAACCVO registro) throws Exception {
        log.debug(" BEGIN BuscarXML  ");
        PreparedStatement st = null;
        //Statement st = null;
        ResultSet rs = null;
        Blob blob = null;
        //String salida = null;
        byte[] blobAsBytes = null;
        try {
            String query = null;

            query = "SELECT RED_DOC"
                    + " FROM R_RED "
                    + "WHERE RED_DEP = ?"
                    + " AND RED_UOR = ?"
                    + " AND RED_EJE = ?"
                    + " AND RED_NUM = ?"
                    + " AND RED_TIP = 'E'"
                    + " AND RED_TIP_DOC = 'application/xml'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            log.debug("Res Dep = " + registro.getResDep());
            log.debug("Res Uor = " + registro.getResUor());
            log.debug("Res Eje = " + registro.getResEje());
            log.debug("Res Num = " + registro.getResNum());
            //st = con.createStatement();
            st = con.prepareStatement(query);
            st.setInt(1, registro.getResDep());
            st.setInt(2, registro.getResUor());
            st.setInt(3, registro.getResEje());
            st.setInt(4, registro.getResNum());
            rs = st.executeQuery();
            while (rs.next()) {
                blob = rs.getBlob("RED_DOC");
                if (blob != null) {
//                    salida = new String(IOUtils.toByteArray(blob.getBinaryStream())); 
//                    log.debug(" salida XML: " + salida);
                    int blobLength = (int) blob.length();
                    blobAsBytes = blob.getBytes(1, blobLength);
                    blob.free();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en BuscarXML", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END BuscarXML  ");
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
        //return salida;
        return blobAsBytes;
    }

    public String BuscarTipoFinalizacion(Connection con, int codOrg, String codProc, int codIntTram) throws Exception {
        log.debug(" BEGIN BuscarTipoFinalizacion  ");
        PreparedStatement st = null;

        ResultSet rs = null;
        String salida = null;
        try {
            String query = null;

            query = "SELECT SAL_TAC"
                    + " FROM E_SAL "
                    + "WHERE SAL_MUN = ?"
                    + " AND SAL_PRO = ?"
                    + " AND SAL_TRA = ?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            log.debug("SAL_MUN = " + codOrg);
            log.debug("SAL_PRO = " + codProc);
            log.debug("SAL_TRA = " + codIntTram);

            st = con.prepareStatement(query);
            st.setInt(1, codOrg);
            st.setString(2, codProc);
            st.setInt(3, codIntTram);
            rs = st.executeQuery();
            while (rs.next()) {
                salida = rs.getString("SAL_TAC");
                if (salida == null) {
                    salida = "S";
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en BuscarTipoFinalizacion", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END BuscarTipoFinalizacion  ");
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
        return salida;
    }

//Da de alta una notificación en base de datos. La notificación está asociada a un trámite y ocurrencia del mismo determinada
    public boolean insertarNotificacion(NotificacionVO notificacion, Connection con) throws Exception {

        log.debug(" BEGIN insertarNotificacion  ");
        int resultado = 0;
        PreparedStatement ps = null;
        String sql = "";
        ResultSet rs = null;

        try {
            String numeroExpediente = notificacion.getNumExpediente();
            String codProcedimiento = notificacion.getCodigoProcedimiento();
            int ejercicio = notificacion.getEjercicio();
            int codMunicipio = notificacion.getCodigoMunicipio();
            int codTramite = notificacion.getCodigoTramite();
            int ocuTramite = notificacion.getOcurrenciaTramite();
            String actoNotificado = notificacion.getActoNotificado();
            String textoNotificacion = notificacion.getTextoNotificacion();

            sql = "INSERT INTO NOTIFICACION(CODIGO_NOTIFICACION,NUM_EXPEDIENTE,"
                    + "COD_PROCEDIMIENTO,EJERCICIO,COD_MUNICIPIO,COD_TRAMITE,OCU_TRAMITE,ACTO_NOTIFICADO,"
                    + "CADUCIDAD_NOTIFICACION,FIRMA,TEXTO_NOTIFICACION,FIRMADA,XML_NOTIFICACION,"
                    + "FECHA_ENVIO, REGISTRO_RT, COD_NOTIFICACION_PLATEA, FECHA_SOL_ENVIO)"
                    + " VALUES (SEQ_NOTIFICACION.nextval,?,?,?,?,?,?,?,0,null,?,null,null,null,null,null,SYSDATE)";

            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setString(i++, numeroExpediente);
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, ejercicio);
            ps.setInt(i++, codMunicipio);
            ps.setInt(i++, codTramite);
            ps.setInt(i++, ocuTramite);
            ps.setString(i++, actoNotificado);
            ps.setString(i++, textoNotificacion);

            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            resultado = ps.executeUpdate();
            if (resultado > 0) {
                log.debug("inserta en la tabla NOTIFICACION");
                log.debug("Nº Expediente: " + numeroExpediente);
                log.debug("Procedimiento: " + codProcedimiento);
                log.debug("Ejercicio: " + ejercicio);
                log.debug("Municipio: " + codMunicipio);
                log.debug("Trámite: " + codTramite);
                log.debug("Ocurrencia: " + ocuTramite);
                log.debug("Notificado: " + actoNotificado + " - " + textoNotificacion);
                return true;
            } else {
                log.error("No inserta en NOTIFICACION sin dar ERROR");
                log.debug("Nº Expediente: " + numeroExpediente);
                log.debug("Procedimiento: " + codProcedimiento);
                log.debug("Ejercicio: " + ejercicio);
                log.debug("Municipio: " + codMunicipio);
                log.debug("Trámite: " + codTramite);
                log.debug("Ocurrencia: " + ocuTramite);
                log.debug("Notificado: " + actoNotificado + " - " + textoNotificacion);
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error en insertarNotificacion" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END insertarNotificacion  ");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int buscarCodigoNotificacion(String numExp, Connection con) throws Exception {
        log.debug(" BEGIN buscarCodigoNotificacion");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        int id = 1;
        try {
            query = "SELECT MAX(CODIGO_NOTIFICACION) as num FROM NOTIFICACION WHERE NUM_EXPEDIENTE = '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                if (rs.getInt("num") != 0) {
                    id = rs.getInt("num");
                    log.debug("El expediente ya tiene notificación con nº: " + id);

                } else {
                    log.debug("Expediente sin notificación previa");
                }
            }
        } catch (Exception ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error en buscarCodigoNotificacion" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return id;
    }

    public boolean modificarNotificacion(int codigoNotif, Connection con) throws Exception {

        log.debug(" BEGIN modificarNotificacion  ");
        String sql = "";
        Statement st = null;
        ResultSet rs = null;

        try {
            sql = "UPDATE NOTIFICACION SET "
                    + " FECHA_SOL_ENVIO = SYSDATE"
                    + " WHERE CODIGO_NOTIFICACION = " + codigoNotif
                    + " AND COD_NOTIFICACION_PLATEA is null";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(sql);
            if (insert > 0) {
                log.debug("Actualizado en NOTIFICACION");
                return true;
            } else {
                log.error("No actualiza en NOTIFICACION");
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error en modificarNotificacion" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END modificarNotificacion  ");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int buscarVersionTercero(int codTercero, Connection con) throws Exception {
        log.debug("BEGIN buscarVersionTercero");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        int id = 0;
        try {
            query = "SELECT TER_NVE FROM T_TER WHERE TER_COD = '" + codTercero + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("TER_NVE");
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error en buscarVersionTercero" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("END buscarVersionTercero");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return id;
    }

    public boolean buscaAutorizadoNotificacion(NotificacionVO notificacion, int codTercero, int verTercero, Connection con) throws Exception {
        log.debug("BEGIN buscaAutorizadoNotificacion");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        int id = 0;
        try {
            String numeroExpediente = notificacion.getNumExpediente();
            int ejercicio = notificacion.getEjercicio();
            int codMunicipio = notificacion.getCodigoMunicipio();
            int codNotif = notificacion.getCodigoNotificacion();

            query = "SELECT CODIGO_NOTIFICACION FROM AUTORIZADO_NOTIFICACION "
                    + "WHERE CODIGO_NOTIFICACION = " + codNotif
                    + " AND COD_MUNICIPIO = " + codMunicipio
                    + " AND EJERCICIO = " + ejercicio
                    + " AND NUM_EXPEDIENTE = '" + numeroExpediente + "'"
                    + " AND COD_TERCERO =" + codTercero
                    + " AND VER_TERCERO =" + verTercero;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("CODIGO_NOTIFICACION");
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error en buscaAutorizadoNotificacion" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("END buscaAutorizadoNotificacion");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        if (id == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertaAutorizadoNotificacion(NotificacionVO notificacion, int codTercero, int verTercero, Connection con) throws Exception {

        log.debug(" BEGIN insertaAutorizadoNotificacion  ");
        int resultado = 0;
        PreparedStatement ps = null;
        String sql = "";
        ResultSet rs = null;

        try {
            String numeroExpediente = notificacion.getNumExpediente();
            int ejercicio = notificacion.getEjercicio();
            int codMunicipio = notificacion.getCodigoMunicipio();
            int codNotif = notificacion.getCodigoNotificacion();
            log.debug("------ DATOS A INSERTAR -----");
            log.debug("Código Notificacion: " + codNotif);
            log.debug("Cod Municipio: " + codMunicipio);
            log.debug("Ejercicio: " + ejercicio);
            log.debug("Nº Expediente: " + numeroExpediente);
            log.debug("Cod Tercero: " + codTercero);
            log.debug("Ver Tercero: " + verTercero);

            sql = "INSERT INTO AUTORIZADO_NOTIFICACION(CODIGO_NOTIFICACION,COD_MUNICIPIO,EJERCICIO,NUM_EXPEDIENTE,COD_TERCERO,VER_TERCERO)"
                    + " VALUES (?,?,?,?,?,?)";

            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++, codNotif);
            ps.setInt(i++, codMunicipio);
            ps.setInt(i++, ejercicio);
            ps.setString(i++, numeroExpediente);
            ps.setInt(i++, codTercero);
            ps.setInt(i++, verTercero);
            //david

            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            resultado = ps.executeUpdate();
            if (resultado > 0) {
                log.debug("Insertada Autorización");
                return true;
            } else {
                log.debug("Autorización NO Insertada ");
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error en insertaAutorizadoNotificacion" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END insertaAutorizadoNotificacion  ");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public void permitirNotificacionElectronica(String numExpediente, Connection con) throws Exception {
        log.debug(" BEGIN permitirNotificacionElectronica ");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "UPDATE E_EXT SET "
                    + " EXT_NOTIFICACION_ELECTRONICA = 1"
                    + " WHERE EXT_NUM = '" + numExpediente + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int update = st.executeUpdate(query);
            if (update > 0) {
                log.debug(" modificación realizada  ");
            } else {
                log.debug(" modificación NO realizada  ");
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error en permitirNotificacionElectronica" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END permitirNotificacionElectronica  ");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public String getTipoCampo(Connection con, int codOrg, String codProc, String codCampo) throws Exception {
        log.debug(" BEGIN getTipoCampo  ");
        PreparedStatement st = null;

        ResultSet rs = null;
        String salida = null;
        try {
            String query = null;

            query = "SELECT PCA_TDA"
                    + " FROM E_PCA "
                    + "WHERE PCA_MUN = ?"
                    + " AND PCA_PRO = ?"
                    + " AND PCA_COD = ?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            log.debug("PCA_MUN = " + codOrg);
            log.debug("PCA_PRO = " + codProc);
            log.debug("PCA_COD = " + codCampo);

            st = con.prepareStatement(query);
            st.setInt(1, codOrg);
            st.setString(2, codProc);
            st.setString(3, codCampo);
            rs = st.executeQuery();
            while (rs.next()) {
                salida = rs.getString("PCA_TDA");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getTipoCampo", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getTipoCampo  ");
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
        return salida;
    }

    public String getNombreTabla(Connection con, int tipCampo) throws Exception {
        log.debug(" BEGIN getNombreTabla  ");
        PreparedStatement st = null;

        ResultSet rs = null;
        String salida = null;
        try {
            String query = null;

            query = "SELECT TDA_TAB"
                    + " FROM E_TDA "
                    + "WHERE TDA_COD = ?";
            log.debug("TDA_COD = " + tipCampo);

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.prepareStatement(query);
            st.setInt(1, tipCampo);
            rs = st.executeQuery();
            while (rs.next()) {
                salida = rs.getString("TDA_TAB");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getNombreTabla", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getNombreTabla  ");
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
        return salida;
    }

    public boolean existeCampoSuplementarioAACC(Connection con, int codOrg, String numExpediente, String codCampo, String valorCampo, String nombreTabla) throws Exception {
        log.debug(" BEGIN existeCampoSuplementarioAACC  ");
        PreparedStatement st = null;

        ResultSet rs = null;
        boolean existe = false;
        try {
            String query = null;
            String[] partes = nombreTabla.split("_");
            query = "SELECT " + partes[1] + "_VALOR"
                    + " FROM E_" + partes[1]
                    + " WHERE " + partes[1] + "_MUN = ?"
                    + " AND " + partes[1] + "_EJE = ?"
                    + " AND " + partes[1] + "_NUM = ?"
                    + " AND " + partes[1] + "_COD = ?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            log.debug(partes[1] + "_MUN = " + codOrg);
            log.debug(partes[1] + "_EJE = " + Integer.parseInt(numExpediente.substring(0, 4)));
            log.debug(partes[1] + "_NUM = " + numExpediente);
            log.debug(partes[1] + "_COD = " + codCampo);

            st = con.prepareStatement(query);
            st.setInt(1, codOrg);
            st.setInt(2, Integer.parseInt(numExpediente.substring(0, 4)));
            st.setString(3, numExpediente);
            st.setString(4, codCampo);
            rs = st.executeQuery();
            while (rs.next()) {
                existe = true;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en existeCampoSuplementarioAACC", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END existeCampoSuplementarioAACC  ");
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
        return existe;
    }

    public int grabarCampoSuplementarioAACC(Connection con, int codOrg, String numExpediente, String codCampo, String valorCampo, String nombreTabla) throws Exception {
        log.debug(" BEGIN grabarCampoSuplementarioAACC  ");
        int resultado = 0;
        PreparedStatement ps = null;
        String sql = "";
        ResultSet rs = null;

        try {
            String[] partes = nombreTabla.split("_");

            sql = "INSERT INTO E_" + partes[1] + "(" + partes[1] + "_MUN," + partes[1] + "_EJE," + partes[1] + "_NUM," + partes[1] + "_COD," + partes[1] + "_VALOR)"
                    + " VALUES (?,?,?,?,?)";

            int i = 1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++, codOrg);
            ps.setInt(i++, Integer.parseInt(numExpediente.substring(0, 4)));
            ps.setString(i++, numExpediente);
            ps.setString(i++, codCampo);
            ps.setString(i++, valorCampo);

            if (log.isDebugEnabled()) {
                log.debug("sql = " + sql);
            }
            log.debug(partes[1] + "_MUN = " + codOrg);
            log.debug(partes[1] + "_EJE = " + Integer.parseInt(numExpediente.substring(0, 4)));
            log.debug(partes[1] + "_NUM = " + numExpediente);
            log.debug(partes[1] + "_COD = " + codCampo);
            log.debug(partes[1] + "_VALOR = " + valorCampo);
            resultado = ps.executeUpdate();
            if (resultado > 0) {
                return 1;
            } else {
                return 0;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error en grabarCampoSuplementarioAACC" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END grabarCampoSuplementarioAACC  ");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean buscarOperacionesPendientes(Connection con, String numExpediente, int codTramite) throws Exception {
        log.debug(" BEGIN buscarOperacionesPendientes  ");
        Statement st = null;
        ResultSet rs = null;
        boolean existe = false;
        try {
            String query = null;
            query = "SELECT ID"
                    + " FROM TAREAS_PENDIENTES_INICIO"
                    + " WHERE NUM_EXPEDIENTE = '" + numExpediente + "'"
                    + " AND COD_TRAMITE = " + codTramite;

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                existe = true;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en buscarOperacionesPendientes", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END buscarOperacionesPendientes  ");
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
        return existe;
    }

    public void InsertarRegistroBatch(Connection con, RegistroBatchVO registroBatch) throws Exception {
        log.debug(" BEGIN InsertarRegistroBatch  ");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "INSERT INTO MELANBIDE54_REG_BATCH "
                    + "(ID,FEC_REGISTRO,EJER_REGISTRO,NUM_REGISTRO,NUM_EXPEDIENTE,COD_TRA,OPERACION, RESULTADO, COD_OPERACION, RELANZAR, OBSERVACIONES) "
                    + " VALUES (SEQ_MELANBIDE54_REG_BATCH.nextval,SYSDATE," + registroBatch.getEjerRegistro() + ","
                    + registroBatch.getNumRegistro() + ",'" + registroBatch.getNumExpediente() + "'," + registroBatch.getCodTramite()
                    + ", '" + registroBatch.getOperacion() + "', '" + registroBatch.getResultado() + "', "
                    + registroBatch.getCodOperacion() + ", " + registroBatch.getRelanzar() + ", '" + registroBatch.getObservaciones() + "')";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                log.error("Registro insertado correctamente en MELANBIDE54_REG_BATCH ");
            } else {
                log.error("Error al insertar en MELANBIDE54_REG_BATCH ");
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error al InsertarRegistroBatch" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END InsertarRegistroBatch  ");
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean existenDatosExtension(Connection con, String numExpediente) throws Exception {
        log.debug(" BEGIN existenDatosExtension  ");
        Statement st = null;
        ResultSet rs = null;
        boolean existe = false;
        try {
            String query = null;
            query = "SELECT ID"
                    + " FROM MELANBIDE54_CENTROS"
                    + " WHERE EXP_NUM = '" + numExpediente + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                existe = true;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en existenDatosExtension", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END existenDatosExtension  ");
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
        return existe;
    }

    public List<RegistroBatchVO> getExpedientesTelematicosAACC(Connection con) throws Exception {
        log.debug(" BEGIN getExpedientesTelematicosAACC  ");
        Statement st = null;
        ResultSet rs = null;
        List<RegistroBatchVO> listReg = new ArrayList<RegistroBatchVO>();
        try {
            String query = null;

            query = "SELECT ID,FEC_REGISTRO,EJER_REGISTRO,NUM_REGISTRO,NUM_EXPEDIENTE,COD_TRA,OPERACION, RESULTADO, COD_OPERACION, RELANZAR, OBSERVACIONES"
                    + " FROM MELANBIDE54_REG_BATCH "
                    + "WHERE RELANZAR = 1 "
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                listReg.add((RegistroBatchVO) MeLanbide54MappingUtils.getInstance().map(rs, RegistroBatchVO.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getExpedientesTelematicosAACC", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getExpedientesTelematicosAACC  ");
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
        return listReg;
    }

    public boolean ponerEstadoRelanzado(int idRegBatch, Connection con) throws Exception {
        log.debug(" BEGIN ponerEstadoRelanzado  ");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "UPDATE MELANBIDE54_REG_BATCH SET "
                    + " RELANZAR = 2"
                    + " WHERE ID = " + idRegBatch;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int update = st.executeUpdate(query);
            if (update > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error en ponerEstadoRelanzado" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END ponerEstadoRelanzado  ");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public RegistroAACCVO getRegistroTelematicoAACC(Connection con, int resEje, int resNum) throws Exception {
        log.debug(" BEGIN getRegistroTelematicoAACC  ");
        Statement st = null;
        ResultSet rs = null;
        RegistroAACCVO reg = new RegistroAACCVO();
        try {
            String query = null;

            query = "SELECT RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM, RES_USU, RES_TER, RES_ASU"
                    + " FROM R_RES "
                    + "WHERE RES_TIP = 'E'"
                    + "  AND REGISTRO_TELEMATICO = 1"
                    + "  AND ASUNTO = 'AUAC'"
                    + "  AND PROCEDIMIENTO = 'AACC'"
                    + "  AND RES_EJE = " + resEje
                    + "  AND RES_NUM = " + resNum;

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                reg = (RegistroAACCVO) MeLanbide54MappingUtils.getInstance().map(rs, RegistroAACCVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getRegistroTelematicoAACC", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getRegistroTelematicoAACC  ");
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
        return reg;
    }

    public boolean buscarRegistroAsociado(Connection con, String numExpediente, int anoRegistro, int numRegistro) throws Exception {
        log.debug(" BEGIN buscarRegistroAsociado  ");
        Statement st = null;
        ResultSet rs = null;
        boolean existe = false;
        try {
            String query = null;
            query = "SELECT EXR_DEP"
                    + " FROM E_EXR"
                    + " WHERE EXR_NUM = '" + numExpediente + "'"
                    + " AND EXR_EJR = " + anoRegistro
                    + " AND EXR_NRE = " + numRegistro
                    + " AND EXR_TIP = 'E'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                existe = true;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en buscarRegistroAsociado", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END buscarRegistroAsociado  ");
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
        return existe;
    }

    public int buscarTramite(Connection con, String numExpediente) throws Exception {
        log.debug("BEGIN buscarTramite");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        int codtra = 0;
        try {
            query = "SELECT CRO_TRA FROM E_CRO"
                    + " WHERE CRO_PRO = 'AACC'"
                    + " AND CRO_NUM = '" + numExpediente + "'"
                    + " AND CRO_FEF is null";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                codtra = rs.getInt("CRO_TRA");
                log.debug("Código trámite:" + codtra);
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error en buscarTramite" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("END buscarTramite");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return codtra;
    }

    public boolean existeTramitePendiente(Connection con, String numExpediente, int codTramite) throws Exception {
        log.debug("BEGIN existeTramitePendiente");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        boolean existe = false;
        try {
            query = "SELECT CRO_TRA FROM E_CRO"
                    + " WHERE CRO_PRO = 'AACC'"
                    + " AND CRO_NUM = '" + numExpediente + "'"
                    + " AND CRO_TRA = " + codTramite
                    + " AND CRO_FEF is null";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                existe = true;
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error en existeTramitePendiente" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("END existeTramitePendiente");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return existe;
    }

    public boolean existeCodigoPlatea(Connection con, String numExpediente) throws Exception {
        log.debug(" BEGIN existeCodigoPlatea  ");
        Statement st = null;
        ResultSet rs = null;
        boolean existe = false;
        try {
            String query = null;
            query = "SELECT CODIGO_NOTIFICACION"
                    + " FROM NOTIFICACION"
                    + " WHERE NUM_EXPEDIENTE = '" + numExpediente + "'"
                    + " AND COD_NOTIFICACION_PLATEA is not null";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                existe = true;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en existeCodigoPlatea", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END existeCodigoPlatea  ");
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
        return existe;
    }

    public int buscarNumeroIntentos(Connection con, String numExpediente) throws Exception {
        log.debug("BEGIN buscarNumeroIntentos");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        int numIntentos = 0;
        try {
            // COALESCE(NUM_INTENTOS,0) --> Si el NUM_INTENTOS es null pone 0.
            query = "SELECT COALESCE(NUM_INTENTOS,0) AS NUM_INTENTOS"
                    + " FROM NOTIFICACION"
                    + " WHERE NUM_EXPEDIENTE = '" + numExpediente + "'"
                    + " AND COD_NOTIFICACION_PLATEA is null";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numIntentos = rs.getInt("NUM_INTENTOS");
                log.debug("Expediente: " + numExpediente);
                log.debug("Nº intentos= " + numIntentos);
            } else {
                log.debug("El expediente: " + numExpediente + " no tiene ningun intento de notificación");
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error en buscarNumeroIntentos" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("END buscarNumeroIntentos");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return numIntentos;
    }

    public String BuscarComunidadAut(Connection con, String codProvincia) throws Exception {
        log.debug("BEGIN BuscarComunidadAut");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        String comunidadAutonoma = "";
        try {
            query = "SELECT GEN_CCAA_CODIGO"
                    + " FROM GEN_PROVINCIAS"
                    + " WHERE GEN_PRO_COD_PROV = " + codProvincia;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                comunidadAutonoma = rs.getString("GEN_CCAA_CODIGO");
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error en BuscarComunidadAut" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug("END BuscarComunidadAut");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return comunidadAutonoma;
    }

    public String buscarValorCampoSuplementario(Connection con, int codOrg, String numExpediente, String codCampo, String nombreTabla) throws Exception {
        log.debug(" BEGIN buscarValorCampoSuplementario  ");
        PreparedStatement st = null;

        ResultSet rs = null;
        String valor = "";
        try {
            String query = null;
            String[] partes = nombreTabla.split("_");
            query = "SELECT " + partes[1] + "_VALOR"
                    + " FROM E_" + partes[1]
                    + " WHERE " + partes[1] + "_MUN = ?"
                    + " AND " + partes[1] + "_EJE = ?"
                    + " AND " + partes[1] + "_NUM = ?"
                    + " AND " + partes[1] + "_COD = ?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            log.debug(partes[1] + "_MUN = " + codOrg);
            log.debug(partes[1] + "_EJE = " + Integer.parseInt(numExpediente.substring(0, 4)));
            log.debug(partes[1] + "_NUM = " + numExpediente);
            log.debug(partes[1] + "_COD = " + codCampo);

            st = con.prepareStatement(query);
            st.setInt(1, codOrg);
            st.setInt(2, Integer.parseInt(numExpediente.substring(0, 4)));
            st.setString(3, numExpediente);
            st.setString(4, codCampo);
            rs = st.executeQuery();
            while (rs.next()) {
                valor = rs.getString(partes[1] + "_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en buscarValorCampoSuplementario", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END buscarValorCampoSuplementario  ");
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
        return valor;
    }
}
