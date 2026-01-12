package es.altia.flexia.integracion.moduloexterno.melanbide42.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide42.util.MELanbide42Exception;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author mikel
 */
public class MELanbide42DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MELanbide42DAO.class);

    //Instancia
    private static MELanbide42DAO instance = null;

    public void insertarSQL(String sql, Connection con) throws MELanbide42Exception, SQLException {
        PreparedStatement ps = null;
        try {
            log.debug("Realizando insert: " + sql);
            ps = con.prepareStatement(sql);
            ps.execute();

            if (!con.getAutoCommit()) {
                con.commit();
            }

            log.debug("Finalizada insert correctamente");

        } catch (SQLException e) {
            log.debug("Error al realizar insertarSQL", e);
            String trace = ExceptionUtils.getStackTrace(e);;//ExceptionUtils.getRootCauseMessage(e);
            throw new MELanbide42Exception("Error al realizar insertarSQL " +  e.getMessage() + " - " + trace, e);
        } finally {
            ps.close();
            con.close();
        }
    }

    public String getSeqNextValue(String seqName, Connection con) throws MELanbide42Exception, SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String result = null;
        try {
            String sql = "SELECT " + seqName + ".NEXTVAL FROM DUAL";
            log.debug("Realizando insert: " + sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                result = rs.getString(1);
            }
            log.debug("Finalizada nextval correctamente");
            return result;

        } catch (SQLException e) {
            log.error("Error al realizar insertarSQL", e);
            throw new MELanbide42Exception("Error al realizar insertarSQL", e);
        } catch (Exception e) {
            log.error("Error general al realizar insertarSQL", e);
            throw new MELanbide42Exception("Error general al realizar insertarSQL", e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public String recogerSecuenciaCal(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String nextVal = "";
        try {
            String query = null;
            query = "SELECT to_char(SEQ_ME_LANBIDE01_DATOS_CALCULO.NEXTVAL) AS VAL FROM DUAL";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                nextVal = rs.getString("VAL");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en recogerSecuencia", ex);
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return nextVal;
    }

    public String recogerSecuenciaPer(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String nextVal = "";
        try {
            String query = null;
            query = "SELECT to_char(SEQ_ME_LANBIDE01_PERIODO.NEXTVAL) AS VAL FROM DUAL";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                nextVal = rs.getString("VAL");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en recogerSecuencia", ex);
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
        return nextVal;
    }

    public String recogerId_S75PUESTOS(String numExp, String concepto, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String nextVal = "";
        int id = 1;
        try {
            String query = null;
            query = "SELECT MAX(PST_NUMCON) AS VAL FROM S75PUESTOS WHERE PST_NUM = '" + numExp + "' AND PST_CONCEP = '" + concepto + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("VAL") + 1;
            }
            nextVal = String.valueOf(id);
        } catch (Exception ex) {
            log.error("Se ha producido un error en recogerSecuencia", ex);
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return nextVal;
    }

    public String recogerId_S75CONCEPTOS(String numExp, String concepto, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String nextVal = "";
        int id = 1;
        try {
            String query = null;
            query = "SELECT MAX(CNP_NUMCON) AS VAL FROM S75CONCEPTOS WHERE CNP_NUM = '" + numExp + "' AND CNP_CONCEP = '" + concepto + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("VAL") + 1;
            }
            nextVal = String.valueOf(id);
        } catch (Exception ex) {
            log.error("Se ha producido un error en recogerSecuencia", ex);
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return nextVal;
    }

    public String recalcularSMI(String numExp, String anio, Connection con) throws Exception {
        CallableStatement st = null;
        String rdo = "";
        try {
            String query = null;
            query = "{?=call recalculoSubvCEESC(?,?,?)}";	//p_numExp, p_numLinea, p_anio_sol
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.prepareCall(query);
            st.registerOutParameter(1, Types.VARCHAR);
            st.setString(2, numExp);
            //En el parametro 2 se le pasa null a numLinea para que de esta forma trate todas las líneas del módulo de extensión
            st.setString(3, null);
            st.setString(4, anio);
            st.executeQuery();
            rdo = st.getString(1);

            return rdo;
        } catch (SQLException ex) {
            log.error("Se ha producido un error recalculando el SMI", ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public String obtenerAnio(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String anioSMI = "";
        try {
            String query = null;
            query = "select * from melanbide58_valor_smi "
                    + "where anio in(select tde_valor from "
                    + "E_TDE where "
                    + "TDE_NUM='" + numExp + "' "
                    + "and TDE_COD='ANIOAYUDA')";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                anioSMI = rs.getString("ANIO");
            }

            return anioSMI;
        } catch (SQLException ex) {
            log.error("Se ha producido un error en obtenerAnio", ex);
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
    }

    public String borrarDatosTabla(String tabla, String campoNumExp, String numExp, Connection con) throws MELanbide42Exception, SQLException {
        PreparedStatement ps = null;
        int borrado = 0;
        String query;

        try {
            query = "DELETE FROM " + tabla + " WHERE " + campoNumExp + "=?";
            log.debug("query = " + query);
            log.debug("parametros pasados a la query = " + numExp);

            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, numExp);

            borrado = ps.executeUpdate();
        } catch (SQLException sqle) {
            log.error("Ha ocurrido un error al eliminar los datos de " + tabla + " para expediente " + numExp);
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return (borrado > 0) + "";
    }

    public static MELanbide42DAO getInstance() {
        if (instance == null) {
            synchronized (MELanbide42DAO.class) {
                instance = new MELanbide42DAO();
            }
        }
        return instance;
    }

    public Map<String, String> getTerceroxNumExpxRolDAO(String numExp, int rol, Connection con) throws Exception {
        Map<String, String> datosTercero = new HashMap<String, String>();
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "SELECT * "
                    + "FROM E_EXT "
                    + "INNER JOIN T_HTE ON E_EXT.EXT_TER=t_hte.hte_ter AND e_ext.ext_nvr=t_hte.hte_nvr "
                    + "WHERE e_ext.ext_num='" + numExp + "' AND e_ext.ext_rol=" + rol;

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                /* Hay que probarlo, lo hacemos manual mientras tanto
                ResultSetMetaData metaData = rs.getMetaData();
                for(int i=1; i <=metaData.getColumnCount();i++){
                    datosTercero.put(metaData.getColumnLabel(i),rs.getString(metaData.getColumnLabel(i)));
                }
                 */
                datosTercero.put("EXT_MUN", rs.getString("EXT_MUN"));
                datosTercero.put("EXT_EJE", rs.getString("EXT_EJE"));
                datosTercero.put("EXT_NUM", rs.getString("EXT_NUM"));
                datosTercero.put("EXT_TER", rs.getString("EXT_TER"));
                datosTercero.put("EXT_NVR", rs.getString("EXT_NVR"));
                datosTercero.put("EXT_DOT", rs.getString("EXT_DOT"));
                datosTercero.put("EXT_ROL", rs.getString("EXT_ROL"));
                datosTercero.put("EXT_PRO", rs.getString("EXT_PRO"));
                datosTercero.put("MOSTRAR", rs.getString("MOSTRAR"));
                datosTercero.put("EXT_NOTIFICACION_ELECTRONICA", rs.getString("EXT_NOTIFICACION_ELECTRONICA"));
                datosTercero.put("HTE_TER", rs.getString("HTE_TER"));
                datosTercero.put("HTE_NVR", rs.getString("HTE_NVR"));
                datosTercero.put("HTE_DOT", rs.getString("HTE_DOT"));
                datosTercero.put("HTE_TID", rs.getString("HTE_TID"));
                datosTercero.put("HTE_DOC", rs.getString("HTE_DOC"));
                datosTercero.put("HTE_NOM", rs.getString("HTE_NOM"));
                datosTercero.put("HTE_AP1", rs.getString("HTE_AP1"));
                datosTercero.put("HTE_PA1", rs.getString("HTE_PA1"));
                datosTercero.put("HTE_AP2", rs.getString("HTE_AP2"));
                datosTercero.put("HTE_PA2", rs.getString("HTE_PA2"));
                datosTercero.put("HTE_NOC", rs.getString("HTE_NOC"));
                datosTercero.put("HTE_NML", rs.getString("HTE_NML"));
                datosTercero.put("HTE_TLF", rs.getString("HTE_TLF"));
                datosTercero.put("HTE_DCE", rs.getString("HTE_DCE"));
                datosTercero.put("HTE_FOP", rs.getString("HTE_FOP"));
                datosTercero.put("HTE_USU", rs.getString("HTE_USU"));
                datosTercero.put("HTE_APL", rs.getString("HTE_APL"));
                datosTercero.put("EXTERNAL_CODE", rs.getString("EXTERNAL_CODE"));
            }
        } catch (Exception e) {
            log.error("getTerceroxNumExpxRolDAO - Error al recupera los datos del tercero : ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("getTerceroxNumExpxRolDAO - Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return datosTercero;
    }

    public String getTerceroFechaNacimientoDAO(String documento, Connection con) throws Exception {
        log.info("getTerceroFechaNacimientoDAO - Begin () - " + documento);
        String datosTerceroFechaNacimiento = "";
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            String query = null;
            query = "select T_HTE.* ,T_CAMPOS_FECHA.*,to_char(VALOR,'dd/mm/yyyy') valor_format "
                    + " from T_HTE "
                    + " INNER JOIN T_CAMPOS_FECHA ON COD_CAMPO='TFECNACIMIENTO' AND COD_TERCERO=HTE_TER "
                    + " WHERE "
                    + " HTE_DOC=? "
                    + " ORDER BY HTE_TER,HTE_NVR,HTE_FOP ";

            log.info("sql = " + query);
            log.info("parameter = " + documento);

            ps = con.prepareStatement(query);
            ps.setString(1, documento);
            rs = ps.executeQuery();
            if (rs.next()) {
                log.info(rs.getString("HTE_DOC") + " " + rs.getString("HTE_TER") + " " + rs.getString("HTE_NVR") + " " + rs.getString("HTE_NOC"));
                datosTerceroFechaNacimiento = rs.getString("valor_format");
            }
        } catch (Exception e) {
            log.error("getTerceroFechaNacimientoDAO - Error al recupera los datos del tercero : ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("getTerceroFechaNacimientoDAO - Procedemos a cerrar el statement y el resultset");
            }
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.info("getTerceroFechaNacimientoDAO - End () - " + datosTerceroFechaNacimiento);
        return datosTerceroFechaNacimiento;
    }

    public String getCodAmbitoxNombrexAnioConvocxCodPro(String nombreAmbito, String ejercicio, String codProc, Connection con) throws Exception {
        log.info("getCodAmbitoxNombrexAnioConvocxCodPro - Begin()");
        String datosCodAmbitos = "";
        Statement st = null;
        ResultSet rs = null;
        try {
            String nombreTabla = "";
            if ("ORI14".equalsIgnoreCase(codProc)) {
                nombreTabla = "ORI14_AMBITOS_HORAS";
            } else if ("CEMP".equalsIgnoreCase(codProc)) {
                nombreTabla = "ORI_AMBITOS_CE";
            }
            String query = null;
            query = "SELECT ORI_AMB_COD     "
                    + ", ORI_AMB_AMBITO "
                    + ", COUNT(ORI_AMB_COD) OVER(PARTITION BY ori_amb_anoconv) NRO_LINEAS "
                    + /*"--ORI14_AMBITOS_HORAS.*\n" +
                    "--,replace(TRIM(ORI_AMB_AMBITO),' ','') ORI_AMB_AMBITO_FORM \n" +
                    "--,TRANSLATE(UPPER(replace(TRIM(ORI_AMB_AMBITO),' ','')),'áéíóúÁÉÍÓÚ','aeiouAEIOU') ORI_AMB_AMBITO_FORM_1\n" +
                     */ "FROM " + nombreTabla + "  WHERE ori_amb_anoconv=" + ejercicio
                    + " AND TRANSLATE(UPPER(replace(TRIM(ORI_AMB_AMBITO),' ','')),'áéíóúÁÉÍÓÚ','aeiouAEIOU') LIKE '%'||TRANSLATE(UPPER(replace(TRIM('" + nombreAmbito + "'),' ','')),'áéíóúÁÉÍÓÚ','aeiouAEIOU')||'%'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                int nroLineas = rs.getInt("NRO_LINEAS");
                // Lohacemos de modo para trazear la operacion en el log y lanzar la correspondiente exception
                if (nroLineas > 1) {
                    log.debug("getCodAmbitoxNombrexAnioConvoc - Mas de un codigo de ambitos recuperados con el nombre recibido. " + nombreAmbito + " - Ambitos " + nroLineas + " Codigo de ambito  donde se lanza la exception " + rs.getInt("ORI_AMB_COD") + "-" + rs.getString("ORI_AMB_AMBITO"));
                    throw new Exception("Mas de un codigo de ambitos recuperados con el nombre recibido. " + nombreAmbito + " - Ambitos " + nroLineas + " Codigo de ambito donde se lanza la exception : " + rs.getInt("ORI_AMB_COD") + "-" + rs.getString("ORI_AMB_AMBITO"));
                } else if (nroLineas < 1) {
                    // Si no hay un fallo en la SQL no deberia entrar aqui, hay que hacer la comprobacion despues del rs.next()
                    log.debug("getCodAmbitoxNombrexAnioConvoc - Ningun codigo ambitos recuperados con el nombre recibido. " + nombreAmbito + " - Ambitos " + nroLineas);
                } else {// Es igual a 1 
                    datosCodAmbitos = rs.getString("ORI_AMB_COD");
                }
            }
            if (datosCodAmbitos == null || datosCodAmbitos == "") {
                log.debug("getCodAmbitoxNombrexAnioConvoc - Ningun codigo ambito recuperado con el nombre recibido. " + nombreAmbito);
                // No lanzamos exception la lanzará al hacer la insert en ubicaciones sino encuentra la FK en Ambito.
            }
        } catch (Exception e) {
            log.error("getCodAmbitoxNombrexAnioConvocxCodPro - Error al recupera los datos del tercero : ", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("getCodAmbitoxNombrexAnioConvocxCodPro - Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        log.debug("getCodAmbitoxNombrexAnioConvocxCodPro - Codigo Recuperado y devuelto : " + nombreAmbito + " : " + datosCodAmbitos);
        log.debug("getCodAmbitoxNombrexAnioConvocxCodPro - End()");
        return datosCodAmbitos;
    }

    public String getCampoSuplementarioTipoNumerico(String codigoCampo, String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valorCampo = "";
        log.info(" getCampoSuplementarioTipoNumerico - BEGIN " + codigoCampo + " / " + numExp);
        try {
            String query = null;
            query = "select TNU_VALOR VALOR from e_tnu "
                    + " where TNU_NUM='" + numExp + "'"
                    + " AND TNU_COD='" + codigoCampo + "'";
            log.info("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valorCampo = rs.getString("VALOR");
            }
            log.info(" - END " + valorCampo);
            return valorCampo;
        } catch (SQLException ex) {
            log.error("Se ha producido un error en getCampoSuplementarioTipoNumerico", ex);
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
    }
    
    public String prepararDatosGetStringByQuery(String query,String nombreColumnaRespuesta, Connection con) throws MELanbide42Exception, SQLException {
        Statement st = null;
        ResultSet rs = null;
        String respuesta = null;
        try {
            log.info("prepararDatosGetStringByQuery: sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                if(nombreColumnaRespuesta!=null && !nombreColumnaRespuesta.isEmpty())
                    respuesta  = rs.getString(nombreColumnaRespuesta);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error en prepararDatosGetStringByQuery", ex);
            throw new MELanbide42Exception("SQLException Se ha producido un error en prepararDatosGetStringByQuery", ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return respuesta;
    }
}
