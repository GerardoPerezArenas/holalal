/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide77.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide77.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide77.util.ConstantesMeLanbide77;
import es.altia.flexia.integracion.moduloexterno.melanbide77.util.MeLanbide77MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.RegistroAerteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.RegistroBatchVO;
import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.SolicitudAerteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kepa
 */
public class MeLanbide77DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide77DAO.class);

    //Instancia
    private static MeLanbide77DAO instance = null;

    private MeLanbide77DAO() {

    }

    public static MeLanbide77DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide77DAO.class) {
                instance = new MeLanbide77DAO();
            }
        }
        return instance;
    }

    public RegistroAerteVO getRegistroAERTE(int resEje, int resNum, Connection con) throws Exception {
        log.info(" BEGIN getRegistroAERTE  ");
        Statement st = null;
        ResultSet rs = null;
        RegistroAerteVO reg = new RegistroAerteVO();
        try {
            String query = null;

            query = "SELECT RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM, RES_USU, RES_TER, RES_ASU"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_REGISTRO, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE RES_TIP = 'E'"
                    + " AND REGISTRO_TELEMATICO = 0"
                    + " AND ASUNTO = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide77.ASUNTO_PROCEDIMIENT0, ConstantesMeLanbide77.FICHERO_PROPIEDADES) + "'"
                    + " AND PROCEDIMIENTO = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide77.COD_PROCEDIMIENTO, ConstantesMeLanbide77.FICHERO_PROPIEDADES) + "'"
                    + " AND RES_EJE = " + resEje
                    + " AND RES_NUM = " + resNum;

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                reg = (RegistroAerteVO) MeLanbide77MappingUtils.getInstance().map(rs, RegistroAerteVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getRegistroAERTE", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getRegistroAERTE  ");
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

    public List<RegistroAerteVO> getListaRegistrosAERTE(Connection con) throws Exception {
        log.debug(" BEGIN getListaRegistrosAERTE  ");
        Statement st = null;
        ResultSet rs = null;
        List<RegistroAerteVO> listReg = new ArrayList<RegistroAerteVO>();
        try {
            String query = null;

            query = "SELECT RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM, RES_USU, RES_TER, RES_ASU"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_REGISTRO, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE RES_TIP = 'E'"
                    + " AND REGISTRO_TELEMATICO = 0"
                    + " AND ASUNTO = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide77.ASUNTO_PROCEDIMIENT0, ConstantesMeLanbide77.FICHERO_PROPIEDADES) + "'"
                    + " AND PROCEDIMIENTO = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide77.COD_PROCEDIMIENTO, ConstantesMeLanbide77.FICHERO_PROPIEDADES) + "'"
                    + " AND RES_EST = 0"
                    + " AND RES_ASU LIKE 'SOLICITUD FORMULARIO WEB AERTE%'"
                    + " ORDER BY RES_FEC, RES_NUM";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                listReg.add((RegistroAerteVO) MeLanbide77MappingUtils.getInstance().map(rs, RegistroAerteVO.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getListaRegistrosAERTE", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getListaRegistrosAERTE  ");
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

    public static InteresadoExpedienteVO getDatosInteresado(int codOrganizacion, int ejercicio, RegistroAerteVO registro, Connection con) throws SQLException, Exception {
        TerceroVO tercero = new TerceroVO();
        InteresadoExpedienteVO interesado = new InteresadoExpedienteVO();
        interesado.setRol(1);

        try {
            tercero = MeLanbide77DAO.getTercero(codOrganizacion, ejercicio, registro, con);
            interesado.setTercero(tercero);
        } catch (Exception ex) {
            log.error("Se ha producido un error al recuperar el domicilio del tercero " + tercero.getDomicilio() + " para el registro " + registro.getResEje() + "/" + registro.getResNum(), ex);
        }
        return interesado;
    }

    public static TerceroVO getTercero(int codMunicipio, int ejercicio, RegistroAerteVO registro, Connection con) throws Exception {
        PreparedStatement st = null;
        ResultSet rs = null;
        TerceroVO tercero = null;

        try {
            String query = null;
            query = "SELECT TER_AP1 AP1, TER_AP2 AP2, TER_DOC DOC,TER_DCE DCE,TER_NOM NOM,TER_TLF TLF, TER_TID TID,"
                    + "DNN_BLQ, DNN_MUN, DNN_PAI, DNN_CPO, DNN_PRV, DNN_DMC, DNN_ESC, VIA_NOM, DNN_PLT, DNN_POR, DNN_NUD,"
                    + "DNN_LED, DNN_PTA, DNN_TVI, DNN_LEH, DNN_NUH"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_TERCEROS, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_DOMICILIO, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " ON (DNN_DOM=TER_DOM)"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_TIPO_VIA, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " ON (DNN_TVI=TVI_COD)"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_VIA, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " ON (DNN_VIA=VIA_COD and VIA_PAI=DNN_PAI and VIA_PRV=DNN_PRV and VIA_MUN=DNN_MUN)"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_PROVINCIAS, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " ON (DNN_PRV=PRV_COD)"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_MUNICIPIOS, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " ON (DNN_MUN=MUN_COD and DNN_PRV=MUN_PRV)"
                    + " WHERE TER_COD = ?";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            log.debug("cod.Tercero = " + registro.getResTer());
            st = con.prepareStatement(query);
            st.setInt(1, registro.getResTer());
            rs = st.executeQuery();

            while (rs.next()) {
                tercero = (TerceroVO) MeLanbide77MappingUtils.getInstance().map(rs, TerceroVO.class);
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

    public void insertarRegistroBatch(RegistroBatchVO registroBatch, Connection con) throws Exception {
        log.debug(" BEGIN InsertarRegistroBatch  ");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_REG_BATCH, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + "(ID,FEC_REGISTRO,EJER_REGISTRO,NUM_REGISTRO,NUM_SOLICITUD, NUM_EXPEDIENTE,COD_TRA,OPERACION, RESULTADO, COD_OPERACION, RELANZAR, OBSERVACIONES) "
                    + " VALUES (SEQ_MELANBIDE77_REG_BATCH.nextval,SYSDATE," + registroBatch.getEjerRegistro() + ","
                    + registroBatch.getNumRegistro() + "," + registroBatch.getNumSolicitud() + ",'" + registroBatch.getNumExpediente() + "',"
                    + registroBatch.getCodTramite() + ", '" + registroBatch.getOperacion() + "', '" + registroBatch.getResultado() + "', "
                    + registroBatch.getCodOperacion() + ", " + registroBatch.getRelanzar() + ", '" + registroBatch.getObservaciones() + "')";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                log.info("Registro insertado correctamente en MELANBIDE77_REG_BATCH ");
            } else {
                log.error("Error al insertar en MELANBIDE77_REG_BATCH ");
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al InsertarRegistroBatch" + " - " + ex.getMessage() + ex);
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


    public String[] asociarExpedienteRegistroBBDD(int ejercicioRegistro, int numeroRegistro, String numeroExpediente, Connection con) throws Exception {
        log.info(" BEGIN asociarExpedienteRegistroBBDD " );
        CallableStatement st = null;
        String[] salida = new String[]{"KO", "No se ha ejecutado el procedure asociarexpregistro"};
        try {
            String query = "call asociarexpregistro(?,?,?,?,?)";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareCall(query);
            st.setInt("ejercicio", ejercicioRegistro);
            st.setInt("numregistro", numeroRegistro);
            st.setString("numexpediente", numeroExpediente);
            st.registerOutParameter("resultado", java.sql.Types.VARCHAR);
            st.registerOutParameter("mensaje", java.sql.Types.VARCHAR);
            st.executeUpdate();

            salida[0] = st.getString("resultado");
            salida[1] = st.getString("mensaje");
        } catch (Exception ex) {
            log.error("Se ha producido un error en asociarExpedienteRegistroBBDD", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END asociarExpedienteRegistroBBDD  " );
            if (st != null) {
                st.close();
            }
        }
        return salida;
    }

    public String getTipoCampo(int codOrg, String codProc, String codCampo, Connection con) throws Exception {
        log.debug(" BEGIN getTipoCampo  ");
        PreparedStatement st = null;
        ResultSet rs = null;
        String salida = null;
        try {
            String query = null;

            query = "SELECT PCA_TDA"
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_SUPLEMENTARIOS_PROCEDIMIENTO, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE PCA_MUN = ?"
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

    public String getNombreTabla(int tipCampo, Connection con) throws Exception {
        log.debug(" BEGIN getNombreTabla  ");
        PreparedStatement st = null;
        ResultSet rs = null;
        String salida = null;
        try {
            String query = null;

            query = "SELECT TDA_TAB"
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_TIPO_SUPLEMENTARIO, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE TDA_COD = ?";
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

    public boolean existeCampoSuplementarioAERTE(int codOrg, String numExpediente, String codCampo, String valorCampo, String nombreTabla, Connection con) throws Exception {
        log.debug(" BEGIN existeCampoSuplementarioAERTE  ");
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
            log.error("Se ha producido un error en existeCampoSuplementarioAERTE", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END existeCampoSuplementarioAERTE  ");
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

    public int grabarCampoSuplementarioAERTE(int codOrg, String numExpediente, String codCampo, String valorCampo, String nombreTabla, Connection con) throws Exception {
        log.info(" BEGIN grabarCampoSuplementarioAERTE  ");
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
            log.info(partes[1] + "_NUM = " + numExpediente);
            log.info(partes[1] + "_COD = " + codCampo);
            log.info(partes[1] + "_VALOR = " + valorCampo);
            resultado = ps.executeUpdate();
            if (resultado > 0) {
                log.info("Grabado ");
                return 1;
            } else {
                log.error("NO grabado");
                return 0;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error en grabarCampoSuplementarioAERTE" + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END grabarCampoSuplementarioAERTE  ");
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

    public List<RegistroBatchVO> getExpedientesRelanzarAERTE(Connection con) throws Exception {
        log.info(" BEGIN getExpedientesRelanzarAERTE  ");
        Statement st = null;
        ResultSet rs = null;
        List<RegistroBatchVO> listReg = new ArrayList<RegistroBatchVO>();
        try {
            String query = null;

            query = "SELECT ID,FEC_REGISTRO,EJER_REGISTRO,NUM_REGISTRO,NUM_SOLICITUD,NUM_EXPEDIENTE,COD_TRA,OPERACION, RESULTADO, COD_OPERACION, RELANZAR, OBSERVACIONES"
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_REG_BATCH, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE RELANZAR = 1 "
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                listReg.add((RegistroBatchVO) MeLanbide77MappingUtils.getInstance().map(rs, RegistroBatchVO.class));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getExpedientesRelanzarAERTE", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getExpedientesRelanzarAERTE  ");
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

    public String tieneExpedienteAERTE(int codOrg, String cif, Connection con) throws Exception {
        log.info(" BEGIN tieneExpedienteAERTE  ");
        String numExpediente = "";
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "SELECT max(EXT_NUM)EXPEDIENTE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_TERCERO_EXPEDIENTE, ConstantesMeLanbide77.FICHERO_PROPIEDADES) + "," + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_TERCEROS_HIST, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE EXT_MUN=" + ConfigurationParameter.getParameter(ConstantesMeLanbide77.COD_ORG, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " AND EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR"
                    + " AND EXT_PRO ='" + ConfigurationParameter.getParameter(ConstantesMeLanbide77.COD_PROCEDIMIENTO, ConstantesMeLanbide77.FICHERO_PROPIEDADES) + "'"
                    + " AND EXT_ROL = 1 AND HTE_DOC = '" + cif + "'"
                    // para no recoger los de la convovatoria anterior 
                    + " AND EXT_NUM >'" + ConfigurationParameter.getParameter(ConstantesMeLanbide77.ULTIMO_EXP_CONVOCATORIA, ConstantesMeLanbide77.FICHERO_PROPIEDADES) + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while ((rs.next())) {
                numExpediente = rs.getString("EXPEDIENTE");
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error en tieneExpedienteAERTE", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END tieneExpedienteAERTE  ");
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
            return numExpediente;
        }
    }

    public int buscarTramite(String numExpediente, Connection con) throws Exception {
        log.debug("BEGIN buscarTramite");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        int codtra = 0;
        try {
            query = "SELECT CRO_TRA FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_OCURRENCIAS, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE CRO_PRO = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide77.COD_PROCEDIMIENTO, ConstantesMeLanbide77.FICHERO_PROPIEDADES) + "'"
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

    public String buscarTipoFinalizacion(int codOrg, String codProc, int codIntTram, Connection con) throws Exception {
        log.info(" BEGIN BuscarTipoFinalizacion  ");
        PreparedStatement st = null;
        ResultSet rs = null;
        String salida = null;
        try {
            String query = null;

            query = "SELECT SAL_TAC"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_CONDICION_SALIDA, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE SAL_MUN = ?"
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

    public boolean buscarRegistroAsociado(String numExpediente, int anoRegistro, int numRegistro, Connection con) throws Exception {
        log.info(" BEGIN buscarRegistroAsociado  ");
        Statement st = null;
        ResultSet rs = null;
        boolean existe = false;
        try {
            String query = null;
            query = "SELECT EXR_DEP"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_EXPEDIENTE_REGISTRO, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
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

    public boolean esRegistroLibre(int anoRegistro, int numRegistro, Connection con) throws Exception {
        log.info(" BEGIN esRegistroLibre  ");
        Statement st = null;
        ResultSet rs = null;
        boolean libre = true;
        String query = "";
        try {
           /* query = "SELECT RES_EST"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_REGISTRO, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE RES_EJE = " + anoRegistro
                    + " AND RES_NUM = " + numRegistro
                    + " AND RES_TIP = 'E'";*/
           query = "SELECT exr_num"
                   + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_EXPEDIENTE_REGISTRO, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                   + " WHERE exr_ejr=" + anoRegistro
                   + " AND exr_nre=" + numRegistro
                   + " AND exr_tip='E'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {               
                    libre = false;                
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en esRegistroLibre", ex);
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
        return libre;
    }

    public boolean ponerEstadoRelanzado(int idRegBatch, Connection con) throws Exception {
        log.info(" BEGIN ponerEstadoRelanzado  ");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_REG_BATCH, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " SET RELANZAR = 2"
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
            log.error("Se ha producido un error en ponerEstadoRelanzado" + " - " + ex.getMessage() + ex);
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

    public SolicitudAerteVO cargarDatosSolicitud(int numSolicitud, Connection con) throws Exception {
        log.info(" BEGIN cargarDatosSolicitud  ");
        SolicitudAerteVO solicitud = new SolicitudAerteVO();
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "SELECT CORR_REG, NUM_DOC,FVIG, NUMCUENTA, NOMEMP1, CIFEMP1, TPJOR1, PORPARC1, NOMEMP2, CIFEMP2, TPJOR2, PORPARC2, NOMEMP3, CIFEMP3, TPJOR3, PORPARC3 "
                    + "FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_SOLICITUDES, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE CORR_REG= ?";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            st.setInt(1, numSolicitud);
            rs = st.executeQuery();
            while (rs.next()) {
                solicitud = (SolicitudAerteVO) MeLanbide77MappingUtils.getInstance().map(rs, SolicitudAerteVO.class);
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al cargar los datos de la solicitud  - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END cargarDatosSolicitud  ");
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
        return solicitud;
    }

    public String getNombreTramite(String codProcedimiento, int codTramite, Connection con) throws Exception {
        log.info(" BEGIN getNombreTramite  " + codTramite + "-" + codProcedimiento);
        String nombre = "";
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "SELECT TML_VALOR NOMBRE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_NOMBRE_TRAMITES, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE TML_PRO= ?"
                    + " AND TML_TRA= ?";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            st.setString(1, codProcedimiento);
            st.setInt(2, codTramite);
            rs = st.executeQuery();
            while (rs.next()) {
                nombre = rs.getString("NOMBRE");
                log.debug("tramite :" + nombre);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error al recuperar el nombre del trámite - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END getNombreTramite  ");
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
        return nombre;
    }

    public int getVersionTerceroRegistro(int codTercero, int ejerReg, int numReg, Connection con) throws Exception {
        log.info(" BEGIN getVersionTerceroRegistro  ");
        int version = 1;
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "";
        try {//SELECT MAX(EXT_NVR) version from R_EXT where EXT_EJE=ejerReg AND EXT_NUM=numReg AND EXT_TIP='E' AND EXT_TER=codTercero;

            query = "SELECT MAX(EXT_NVR) VERTERCERO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_TERCERO_REGISTRO, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE EXT_EJE= ?"
                    + " AND EXT_NUM= ?"
                    + " AND EXT_TER= ?"
                    + " AND EXT_TIP='E'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            st.setInt(1, ejerReg);
            st.setInt(2, numReg);
            st.setInt(3, codTercero);
            rs = st.executeQuery();
            while (rs.next()) {
                version = rs.getInt("VERTERCERO");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error al recuperar la version del tercero - " + ex.getMessage() + ex);
            throw new Exception(ex);
        } finally {
            try {
                log.debug(" END getVersionTerceroRegistro  ");
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
        return version;
    }

    public int getCodDomTercero(int codTercero, int version, Connection con) throws Exception {
        log.info(" BEGIN getCodDomTercero  ");
        int codDomicilio = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "SELECT hte_dot CODIGO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_TERCEROS_HIST, ConstantesMeLanbide77.FICHERO_PROPIEDADES)
                    + " WHERE hte_ter= ?"
                    + " AND hte_nvr= ?";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareStatement(query);
            st.setInt(1, codTercero);
            st.setInt(2, version);
            rs = st.executeQuery();
            while (rs.next()) {
                codDomicilio = rs.getInt("CODIGO");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar el código del domicilio del tercero - " + ex.getMessage() + ex);
            throw new SQLException(ex);
        } finally {
            try {
                log.debug(" END getCodDomTercero  ");
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
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
        return codDomicilio;
    }
}
