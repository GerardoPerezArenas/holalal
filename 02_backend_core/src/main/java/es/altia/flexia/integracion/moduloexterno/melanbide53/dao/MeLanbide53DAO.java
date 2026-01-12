/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide53.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53;
import es.altia.flexia.integracion.moduloexterno.melanbide53.util.MeLanbide53MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleErrorVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroIdenErrorCriteriosFiltroVO;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleEstadisticaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EstadisticasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EstadisticasVO_EXCEL;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EventoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.FiltroEstadisticasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.InformeInternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO_EXCEL;
import es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO_EXCEL2;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author davidg
 */
public class MeLanbide53DAO {
    //Constantes de la clase

    private final int codTraGestionAreaAsignada1 = 301;
    private final int codTraGestionAreaAsignada2 = 302;
    private final int codTraGestionAreaAsignada3 = 303;
    private final int codTraGestionAreaAsignada4 = 304;
    private final String nombreProcedimiento = "QUEJA";
    //Logger
    protected static Logger log = LogManager.getLogger(MeLanbide53DAO.class.getName());
    //Instancia
    private static MeLanbide53DAO instance = null;

    private MeLanbide53DAO() {

    }

    public static MeLanbide53DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide53DAO.class) {
                instance = new MeLanbide53DAO();
            }
        }
        return instance;
    }

    // Metodos de Recupracion de datos
    public List<RegistroErrorVO_EXCEL> getListaDatosInformeInternoDerivadas(String strFechaDesde, String strFechaHasta, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<RegistroErrorVO_EXCEL> listTramites = new ArrayList<RegistroErrorVO_EXCEL>();
        RegistroErrorVO_EXCEL datosInformeInterno;
        log.error("FECHA DESDE" + strFechaDesde);
        log.error("FECHA HASTA" + strFechaHasta);
        try {
            String query = "select REG_ERR_ID,\n"
                    + "  tO_CHAR(REG_ERR_FEC_ERROR, 'DD/MM/YYYY')AS REG_ERR_FEC_ERROR,\n"
                    + "  REG_ERR_ID	,\n"
                    + "REG_ERR_FEC_ERROR	,\n"
                    + "REG_ERR_MEN	,\n"
                    + "REG_ERR_ID_PROC	,\n"
                    + "REG_ERR_IDEN_ERR_ID	,\n"
                    + "REG_ERR_ID_CLAVE	,\n"
                    + "REG_ERR_NOT	,\n"
                    + "REG_ERR_REV	,\n"
                    + "REG_ERR_FEC_REV	,\n"
                    + "REG_ERR_OBS	,\n"
                    + "REG_ERR_SIS_ORIG,	\n"
                    + "REG_ERR_SITU	,\n"
                    + "REG_ERR_LOG	,\n"
                    + "REG_ERR_EVEN	,\n"
                    + "REG_ERR_ID_FLEXIA		,\n"
                    + "REG_ERR_RETRAMIT_SN	,\n"
                    + "REG_ERR_FEC_RETRAMIT	,\n"
                    + "REG_ERR_RESULT_WS, \n"
                    + "REG_ERR_IMPACTO, \n"
                    + "REG_ERR_TRATADO, \n"
                    + "from reg_err\n"
                    + "where reg_err_fec_error BETWEEN NVL(TO_DATE('" + strFechaDesde + "'),'') AND NVL(TO_DATE('" + strFechaHasta + "','DD/MM/YYYY'),'')";
            log.debug("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                log.error("fila informe");
                datosInformeInterno = (RegistroErrorVO_EXCEL) MeLanbide53MappingUtils.getInstance().map(rs, RegistroErrorVO_EXCEL.class);
                listTramites.add(datosInformeInterno);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
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
        return listTramites;
    }

    private String getWhereComunInfInt(int codOrganizacion) {
        StringBuilder whereComun = new StringBuilder("E_TRA.TRA_PRO='");
        whereComun.append(nombreProcedimiento);
        whereComun.append("' AND (E_TRA.TRA_COU=");
        whereComun.append(codTraGestionAreaAsignada1);
        whereComun.append(" OR E_TRA.TRA_COU   =");
        whereComun.append(codTraGestionAreaAsignada2);
        whereComun.append(" OR E_TRA.TRA_COU   =");
        whereComun.append(codTraGestionAreaAsignada3);
        whereComun.append(" OR E_TRA.TRA_COU   =");
        whereComun.append(codTraGestionAreaAsignada4);
        whereComun.append(") and E_TRA.TRA_MUN=");
        whereComun.append(codOrganizacion);
        return whereComun.toString();
    }

    public List<RegistroErrorVO_EXCEL2> getListaDatosInformeInternoResueltas(String strFechaDesde, String strFechaHasta, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<RegistroErrorVO_EXCEL2> listTramites = new ArrayList<RegistroErrorVO_EXCEL2>();
        RegistroErrorVO_EXCEL2 datosInformeInterno;
        log.error("FECHA DESDE" + strFechaDesde);
        log.error("FECHA HASTA" + strFechaHasta);
        try {
            String query = "SELECT reg_err.reg_err_iden_err_id,iden_err.iden_err_desc, COUNT(*) as num\n"
                    + "FROM reg_err\n"
                    + "INNER JOIN iden_err\n"
                    + "ON iden_err.iden_err_id=reg_err.reg_err_iden_err_id\n"
                    + "where reg_err_fec_error \n"
                    + "BETWEEN NVL(TO_DATE('" + strFechaDesde + "'),'') \n"
                    + "AND NVL(TO_DATE('" + strFechaHasta + "','DD/MM/YYYY'),'') \n"
                    + "GROUP BY reg_err.reg_err_iden_err_id,iden_err.iden_err_desc \n"
                    + "order by reg_err.reg_err_iden_err_id asc";
            log.debug("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                log.error("fila informe");
                datosInformeInterno = (RegistroErrorVO_EXCEL2) MeLanbide53MappingUtils.getInstance().map(rs, RegistroErrorVO_EXCEL2.class);
                listTramites.add(datosInformeInterno);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos del informe interno", ex);
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
        return listTramites;
    }

    public List<RegistroErrorVO> recogeRegistroErrores(Connection con, Integer lineaRegInicial, Integer lineaRegistroFinal) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        List<RegistroErrorVO> listResgistroError = new ArrayList<RegistroErrorVO>();
        try {
            query = " SELECT * FROM ("
                    + " SELECT ROWNUM AS NUMREG , COUNT(0) OVER (PARTITION BY 0) AS NUMTOTALREGISTROS, DATOS.* "
                    + " FROM("
                    + " SELECT REG_ERR_ID,TO_CHAR(REG_ERR_FEC_ERROR, 'dd/MM/yyyy HH24:MI:SS') AS REG_ERR_FEC_ERROR, "
                    + " REG_ERR_MEN,REG_ERR_EXCEP_MEN,REG_ERR_CAUSA,REG_ERR_TRAZA,REG_ERR_ID_PROC,"
                    + " REG_ERR_IDEN_ERR_ID,REG_ERR_ID_CLAVE,REG_ERR_NOT,REG_ERR_REV,"
                    + " TO_CHAR(REG_ERR_FEC_REV, 'dd/MM/yyyy HH24:MI:SS') AS REG_ERR_FEC_REV,REG_ERR_OBS,REG_ERR_SIS_ORIG,REG_ERR_SITU,"
                    + " REG_ERR_LOG,REG_ERR_EVEN,REG_ERR_ID_FLEXIA, "
                    + " REG_ERR_SOLICITUD, REG_ERR_RETRAMIT_SN, TO_CHAR(REG_ERR_FEC_RETRAMIT, 'dd/MM/yyyy HH24:MI:SS') as REG_ERR_FEC_RETRAMIT, REG_ERR_RESULT_WS, "
                    + " REG_ERR_IMPACTO, REG_ERR_TRATADO "
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_REG_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " ORDER BY REG_ERR.REG_ERR_FEC_ERROR DESC ) DATOS "
                    + " ) ";
//DavidL             
            if (lineaRegistroFinal != null && lineaRegistroFinal > 0) {
                query = query + " WHERE (NUMREG > " + lineaRegInicial + " AND NUMREG <= " + lineaRegistroFinal + ")";
            }
            //String numMaximoLineas = ConfigurationParameter.getParameter(ConstantesMeLanbide53.NUMERO_MAX_LINEAS_TABLAINICIAL, ConstantesMeLanbide53.FICHERO_PROPIEDADES);
            //if(numMaximoLineas!=null && !numMaximoLineas.isEmpty() && !numMaximoLineas.equals("0")){
            //    query += " WHERE ROWNUM<=" + numMaximoLineas;
            //}
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                RegistroErrorVO registroError = new RegistroErrorVO();
                registroError = (RegistroErrorVO) MeLanbide53MappingUtils.getInstance().map_listado(rs, RegistroErrorVO.class);
                // Para paginacion numero de registro dentro de la consulta y total obtenidos en la cosulta
                registroError.setNoRegEnLaConsulta(rs.getInt("NUMREG"));
                registroError.setNoTotalRegConsulta(rs.getInt("NUMTOTALREGISTROS"));
                listResgistroError.add(registroError);
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.error("Se ha producido un error en recogeRegistroErrores - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listResgistroError;
    }

    public List<DetalleErrorVO> recogeRegistroIdentError(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        List<DetalleErrorVO> listResgistroError = new ArrayList<DetalleErrorVO>();
        try {
            query = " SELECT * FROM ("
                    + " SELECT " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES) + ".*, "
                    + "CASE WHEN IDEN_ERR_TIPO = 1 THEN 'Sistema' "
                    + "WHEN IDEN_ERR_TIPO = 2 THEN 'Funcional' ELSE ' ' END AS TIPO, "
                    + "CASE WHEN IDEN_ERR_CRIT = 1 THEN 'Bloqueante'"
                    + "WHEN IDEN_ERR_CRIT = 2 THEN 'Grave' "
                    + "WHEN IDEN_ERR_CRIT = 3 THEN 'Leve' "
                    + "WHEN IDEN_ERR_CRIT = 4 THEN 'Informativo' "
                    + "ELSE ' ' END AS CRITICO "
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    //+ " ORDER BY REG_ERR.REG_ERR_FEC_ERROR DESC "
                    + ")";
            String numMaximoLineas = ConfigurationParameter.getParameter(ConstantesMeLanbide53.NUMERO_MAX_LINEAS_TABLAINICIAL, ConstantesMeLanbide53.FICHERO_PROPIEDADES);
            if (numMaximoLineas != null && !numMaximoLineas.isEmpty() && !numMaximoLineas.equals("0")) {
                query += " WHERE ROWNUM<=" + numMaximoLineas;
            }
            query += " ORDER BY IDEN_ERR_ID";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                DetalleErrorVO registroError = new DetalleErrorVO();
                registroError = (DetalleErrorVO) MeLanbide53MappingUtils.getInstance().map(rs, DetalleErrorVO.class);
                listResgistroError.add(registroError);
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.error("Se ha producido un error en recogeRegistroErrores - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listResgistroError;
    }

    public List<RegistroErrorVO> busquedaFiltrandoListaErrores(RegistroErrorCriteriosFiltroVO _criterioBusqueda, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        List<RegistroErrorVO> listResgistroError = new ArrayList<RegistroErrorVO>();
        try {
            String where1 = preparaClausulaWhere(_criterioBusqueda);
            query = "SELECT * FROM ("
                    + " SELECT ROWNUM AS NUMREG ,COUNT(0) OVER (PARTITION BY 0) AS NUMTOTALREGISTROS, DATOS.* "
                    + " FROM ("
                    + " SELECT REG_ERR_ID,TO_CHAR(REG_ERR_FEC_ERROR, 'dd/MM/yyyy HH24:MI:SS') AS REG_ERR_FEC_ERROR, "
                    + " REG_ERR_MEN,REG_ERR_EXCEP_MEN,REG_ERR_CAUSA,REG_ERR_TRAZA,REG_ERR_ID_PROC,"
                    + " REG_ERR_IDEN_ERR_ID,REG_ERR_ID_CLAVE,REG_ERR_NOT,REG_ERR_REV,"
                    + " TO_CHAR(REG_ERR_FEC_REV, 'dd/MM/yyyy HH24:MI:SS') AS REG_ERR_FEC_REV,REG_ERR_OBS,REG_ERR_SIS_ORIG,REG_ERR_SITU,"
                    + " REG_ERR_LOG,REG_ERR_EVEN,REG_ERR_ID_FLEXIA, "
                    + " REG_ERR_SOLICITUD, REG_ERR_RETRAMIT_SN, TO_CHAR(REG_ERR_FEC_RETRAMIT, 'dd/MM/yyyy HH24:MI:SS') as REG_ERR_FEC_RETRAMIT, REG_ERR_RESULT_WS, "
                    + " REG_ERR_IMPACTO, REG_ERR_TRATADO "
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_REG_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " " + where1
                    + " ORDER BY REG_ERR.REG_ERR_FEC_ERROR DESC ) DATOS"
                    + " ) WHERE ROWNUM <= 500";

            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                RegistroErrorVO registroError = new RegistroErrorVO();
                registroError = (RegistroErrorVO) MeLanbide53MappingUtils.getInstance().map(rs, RegistroErrorVO.class);
                // Para paginacion numero de registro dentro de la consulta y total obtenidos en la consulta
                // No lo ponemos en mapeo pq no es un criterio general
                registroError.setNoRegEnLaConsulta(rs.getInt("NUMREG"));
                registroError.setNoTotalRegConsulta(rs.getInt("NUMTOTALREGISTROS"));
                listResgistroError.add(registroError);
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.error("Se ha producido un error en recogeRegistroErrores al filtrar - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listResgistroError;
    }

    public List<DetalleErrorVO> busquedaIdentificacionErrores(RegistroIdenErrorCriteriosFiltroVO _criterioBusqueda, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        List<DetalleErrorVO> listResgistroError = new ArrayList<DetalleErrorVO>();
        try {
            String where1 = preparaClausulaWhereIdError(_criterioBusqueda);
            query = " SELECT * FROM ("
                    + " SELECT ROWNUM AS NUMREG ,COUNT(0) OVER (PARTITION BY 0) AS NUMTOTALREGISTROS, DATOS.* "
                    + " FROM ("
                    + " SELECT " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES) + ".*, "
                    + "CASE WHEN IDEN_ERR_TIPO = 1 THEN 'Sistema' "
                    + "WHEN IDEN_ERR_TIPO = 2 THEN 'Funcional' ELSE ' ' END AS TIPO, "
                    + "CASE WHEN IDEN_ERR_CRIT = 1 THEN 'Bloqueante'"
                    + "WHEN IDEN_ERR_CRIT = 2 THEN 'Grave' "
                    + "WHEN IDEN_ERR_CRIT = 3 THEN 'Leve' "
                    + "WHEN IDEN_ERR_CRIT = 4 THEN 'Informativo' "
                    + "ELSE ' ' END AS CRITICO "
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " " + where1
                    + "  ORDER BY IDEN_ERR_ID ) DATOS )";

            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                DetalleErrorVO registroError = new DetalleErrorVO();
                registroError = (DetalleErrorVO) MeLanbide53MappingUtils.getInstance().map(rs, DetalleErrorVO.class);
                registroError.setNoRegEnLaConsulta(rs.getInt("NUMREG"));
                registroError.setNoTotalRegConsulta(rs.getInt("NUMTOTALREGISTROS"));
                listResgistroError.add(registroError);
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.error("Se ha producido un error en recogeRegistroErrores al filtrar - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listResgistroError;
    }

    public RegistroErrorVO getErrorPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        RegistroErrorVO gestionError = new RegistroErrorVO();
        try {
            String query = null;
            query = "SELECT REG_ERR_ID,TO_CHAR(REG_ERR_FEC_ERROR, 'dd/MM/yyyy HH24:MI:SS') AS REG_ERR_FEC_ERROR, "
                    + " REG_ERR_MEN,REG_ERR_EXCEP_MEN,REG_ERR_CAUSA,REG_ERR_TRAZA,REG_ERR_ID_PROC,"
                    + " REG_ERR_IDEN_ERR_ID,REG_ERR_ID_CLAVE,REG_ERR_NOT,REG_ERR_REV,"
                    + " TO_CHAR(REG_ERR_FEC_REV, 'dd/MM/yyyy HH24:MI:SS') AS REG_ERR_FEC_REV,REG_ERR_OBS,REG_ERR_SIS_ORIG,REG_ERR_SITU,"
                    + " REG_ERR_LOG,REG_ERR_EVEN,REG_ERR_ID_FLEXIA, "
                    + " REG_ERR_SOLICITUD, REG_ERR_RETRAMIT_SN, TO_CHAR(REG_ERR_FEC_RETRAMIT, 'dd/MM/yyyy HH24:MI:SS') as REG_ERR_FEC_RETRAMIT, REG_ERR_RESULT_WS"
                    + ", REG_ERR_IMPACTO, REG_ERR_TRATADO "
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_REG_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " WHERE REG_ERR_ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                gestionError = (RegistroErrorVO) MeLanbide53MappingUtils.getInstance().map(rs, RegistroErrorVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un error para Gestion De Errores : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return gestionError;
    }

    public DetalleErrorVO getIdErrorPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DetalleErrorVO gestionError = new DetalleErrorVO();
        try {
            String query = null;
            query = " SELECT " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES) + ".*, "
                    + "CASE WHEN IDEN_ERR_TIPO = 1 THEN 'Sistema' "
                    + "WHEN IDEN_ERR_TIPO = 2 THEN 'Funcional' ELSE ' ' END AS TIPO, "
                    + "CASE WHEN IDEN_ERR_CRIT = 1 THEN 'Bloqueante'"
                    + "WHEN IDEN_ERR_CRIT = 2 THEN 'Grave' "
                    + "WHEN IDEN_ERR_CRIT = 3 THEN 'Leve' "
                    + "WHEN IDEN_ERR_CRIT = 4 THEN 'Informativo' "
                    + "ELSE ' ' END AS CRITICO "
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " WHERE IDEN_ERR_ID='" + (id != null && !id.equals("") ? id : "null") + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                gestionError = (DetalleErrorVO) MeLanbide53MappingUtils.getInstance().map(rs, DetalleErrorVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un error para Gestion De Errores : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return gestionError;
    }

    public boolean getErrorABorrar(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        boolean registro = true;
        try {
            String query = null;
            query = " SELECT *  FROM "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_REG_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " WHERE REG_ERR_IDEN_ERR_ID = '" + id + "' ";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                registro = false;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getErrorABorrar : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return registro;
    }

    public boolean modificarGestionError(RegistroErrorVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaError = "";
        String fechaVerificado = "";
        String valorCheckRevisado = "";
        String valorCheckImpacto = "";
        String valorCheckTratado = "";
        String mensaje = datModif.getObservacionesError().toString().replace("\"", " ");
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        if (datModif != null && datModif.getFechaError() != null && !datModif.getFechaError().equals("")) {
            fechaError = formatoFecha.format(datModif.getFechaError());
        }
        if (datModif != null && datModif.getFechaRevisionError() != null && !datModif.getFechaRevisionError().equals("")) {
            fechaVerificado = datModif.getFechaRevisionError().substring(0, 10); //formatoFecha.format(datModif.getFechaRevisionError());
        }
        if (datModif != null && datModif.getErrorRevisado() != null && !datModif.getErrorRevisado().equals("")) {
            valorCheckRevisado = mapearValorCheck(datModif.getErrorRevisado()); //formatoFecha.format(datModif.getFechaRevisionError());
        }
        if (datModif != null && datModif.getImpacto() != null && !datModif.getImpacto().equals("")) {
            valorCheckImpacto = mapearValorCheck(datModif.getImpacto());
        }
        if (datModif != null && datModif.getTratado() != null && !datModif.getTratado().equals("")) {
            valorCheckTratado = mapearValorCheck(datModif.getTratado());
        }

        try {

            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_REG_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " SET "
                    //                    + " REG_ERR_FEC_ERROR=TO_DATE('" + fechaError + "','dd/mm/yyyy')" //+ (datModif.getFecha() != null ? "TO_DATE('" + datModif.getFecha() + "','dd/mm/yyyy')" : "null")
                    //                    + ", REG_ERR_MEN='" + datModif.getMensajeError()+ "'"
                    //                    + ", REG_ERR_EXCEP_MEN='" + datModif.getMensajeException()+ "'"
                    //                    + ", REG_ERR_CAUSA='" + datModif.getCausaException()+ "'"
                    //                    + ", REG_ERR_TRAZA='" + datModif.getTrazaError()+ "'"
                    //                    + ", REG_ERR_ID_PROC='" + datModif.getIdProcedimiento()+ "'"
                    //                    + ", REG_ERR_IDEN_ERR_ID='" + datModif.getIdErrorFK()+ "'"
                    //                    + ", REG_ERR_ID_CLAVE='" + datModif.getClave()+ "'"
                    //                    + ", REG_ERR_NOT='" + datModif.getErrorNotificado()+ "'"
                    + " REG_ERR_REV='" + valorCheckRevisado + "'"
                    + " ,REG_ERR_FEC_REV=" + (fechaVerificado != null && !fechaVerificado.isEmpty() ? "TO_TIMESTAMP('" + fechaVerificado + "' ||' " + String.valueOf((Calendar.getInstance()).get(Calendar.HOUR_OF_DAY)) + "'||':'||'" + String.valueOf((Calendar.getInstance()).get(Calendar.MINUTE)) + "'||':'||'" + String.valueOf((Calendar.getInstance()).get(Calendar.SECOND))
                    + "','DD/MM/YYYY HH24.MI.SS')" : "null")
                    + ", REG_ERR_OBS='" + mensaje + "'"
                    //                    + ", REG_ERR_SIS_ORIG='" + datModif.getSistemaOrigen()+ "'"
                    //                    + ", REG_ERR_SITU='" + datModif.getUbicacionError()+ "'"
                    //                    + ", REG_ERR_LOG='" + datModif.getFicheroLog()+"'"
                    //                    + ", REG_ERR_EVEN='" + datModif.getEvento()+ "'"
                    //                    + ", REG_ERR_ID_FLEXIA='" + datModif.getNumeroExpediente()+ "'"
                    + ", REG_ERR_IMPACTO='" + valorCheckImpacto + "'"
                    + ", REG_ERR_TRATADO='" + valorCheckTratado + "'"
                    + " WHERE REG_ERR_ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            int update = st.executeUpdate(query);
            if (update > 0) {
                log.info("Registro actualizado correctamente");
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al Modificar un registro de Control de acceso - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarIdError(DetalleErrorVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " SET "
                    + " IDEN_ERR_DESC_C = '" + datModif.getDescripcionCorta() + "' "
                    + ", IDEN_ERR_DESC='" + datModif.getDescripcion() + "'"
                    + ", IDEN_ERR_TIPO='" + datModif.getTipo() + "'"
                    + ", IDEN_ERR_CRIT='" + datModif.getCritico() + "'"
                    + ", IDEN_ERR_MEN_AVISO='" + datModif.getAviso() + "'"
                    + ", IDEN_ERR_MAILS='" + datModif.getMails() + "'"
                    + ", IDEN_ERR_ACC='" + datModif.getAcciones() + "'"
                    + ", IDEN_ERR_MOD='" + datModif.getModulo() + "'"
                    + " WHERE IDEN_ERR_ID='" + datModif.getId() + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            int update = st.executeUpdate(query);
            if (update > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al Modificar un registro de Control de acceso - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean eliminarIdError(String id, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " WHERE IDEN_ERR_ID='" + id + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            int update = st.executeUpdate(query);
            if (update > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al Modificar un registro de Control de acceso - "
                    + id + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean insertarIdError(DetalleErrorVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " ( IDEN_ERR_ID, IDEN_ERR_DESC_C, IDEN_ERR_DESC, IDEN_ERR_TIPO, IDEN_ERR_CRIT, "
                    + "IDEN_ERR_MEN_AVISO, IDEN_ERR_MAILS, IDEN_ERR_ACC, IDEN_ERR_MOD) VALUES ("
                    + " '" + datModif.getId() + "', '" + datModif.getDescripcionCorta() + "' "
                    + ", '" + datModif.getDescripcion() + "'"
                    + ", '" + datModif.getTipo() + "'"
                    + ", '" + datModif.getCritico() + "'"
                    + ", '" + datModif.getAviso() + "'"
                    + ", '" + datModif.getMails() + "'"
                    + ", '" + datModif.getAcciones() + "'"
                    + ", '" + datModif.getModulo() + "')";
            //+ " WHERE IDEN_ERR_ID='" + datModif.getId()+ "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            int update = st.executeUpdate(query);
            if (update > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al Modificar un registro de Control de acceso - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    private String preparaClausulaWhere(RegistroErrorCriteriosFiltroVO _criterioBusqueda) throws Exception {
        String where = "";
        try {
            if (_criterioBusqueda.getId() != null && !_criterioBusqueda.getId().equals(0)) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (REG_ERR_ID=" + _criterioBusqueda.getId() + ")";
                } else {
                    where += "WHERE (REG_ERR_ID=" + _criterioBusqueda.getId() + ")";
                }
            }
            if (_criterioBusqueda.getIdProcedimiento() != null && !_criterioBusqueda.getIdProcedimiento().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (UPPER(REG_ERR_ID_PROC) LIKE'%" + _criterioBusqueda.getIdProcedimiento().toUpperCase() + "%')";
                } else {
                    where += "WHERE (UPPER(REG_ERR_ID_PROC) LIKE'%" + _criterioBusqueda.getIdProcedimiento().toUpperCase() + "%')";
                }
            }
            if (_criterioBusqueda.getIdErrorFK() != null && !_criterioBusqueda.getIdErrorFK().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (UPPER(REG_ERR_IDEN_ERR_ID) LIKE'%" + _criterioBusqueda.getIdErrorFK().toUpperCase() + "%')";
                } else {
                    where += "WHERE (UPPER(REG_ERR_IDEN_ERR_ID) LIKE'%" + _criterioBusqueda.getIdErrorFK().toUpperCase() + "%')";
                }
            }
            if (_criterioBusqueda.getClave() != null && !_criterioBusqueda.getClave().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (UPPER(REG_ERR_ID_CLAVE) LIKE'%" + _criterioBusqueda.getClave().toUpperCase() + "%')";
                } else {
                    where += "WHERE (UPPER(REG_ERR_ID_CLAVE) LIKE'%" + _criterioBusqueda.getClave().toUpperCase() + "%')";
                }
            }
            if (_criterioBusqueda.getErrorNotificado() != null && !_criterioBusqueda.getErrorNotificado().equals("")) {
                String valorCheck = mapearValorCheck(_criterioBusqueda.getErrorNotificado());
                if (valorCheck.isEmpty()) {
                    // deberia funcionar si en BD esta definido valor Default N , sin embargo comentamos para que saque todo por si acaso
//                    if (where.startsWith("WHERE")) {    // Será muy poco probable que se de
//                        where += " AND  (REG_ERR_NOT " + ConstantesMeLanbide53.IS_NOT_NULL + ")";
//                    } else {
//                        where += "WHERE (REG_ERR_NOT " + ConstantesMeLanbide53.IS_NOT_NULL + ")";
//                    }
                } else {
                    if (where.startsWith("WHERE")) {
                        where += " AND  (REG_ERR_NOT='" + valorCheck + "')";
                    } else {
                        where += "WHERE (REG_ERR_NOT='" + valorCheck + "')";
                    }
                }
            }
            if (_criterioBusqueda.getErrorRevisado() != null && !_criterioBusqueda.getErrorRevisado().equals("")) {
                String valorCheck = mapearValorCheck(_criterioBusqueda.getErrorRevisado());
                if (valorCheck.isEmpty()) {
                    // deberia funcionar si en BD esta definido valor Default N , sin embargo comentamos para que saque todo por si acaso
//                    if (where.startsWith("WHERE")) {    // Será muy poco probable que se de
//                        where += " AND  (REG_ERR_REV " + ConstantesMeLanbide53.IS_NOT_NULL + ")";
//                    } else {
//                        where += "WHERE (REG_ERR_REV " + ConstantesMeLanbide53.IS_NOT_NULL + ")";
//                    }
                } else {
                    if (where.startsWith("WHERE")) {
                        where += " AND  (REG_ERR_REV='" + valorCheck + "')";
                    } else {
                        where += "WHERE (REG_ERR_REV='" + valorCheck + "')";
                    }
                }
            }

            if (_criterioBusqueda.getErrorRetramitado() != null && !_criterioBusqueda.getErrorRetramitado().equals("")) {
                String valorCheckRetra = mapearValorCheck(_criterioBusqueda.getErrorRetramitado());
                if (valorCheckRetra.isEmpty()) {

                } else {
                    if (where.startsWith("WHERE")) {
                        where += " AND  (REG_ERR_RETRAMIT_SN='" + valorCheckRetra + "')";
                    } else {
                        where += "WHERE (REG_ERR_RETRAMIT_SN='" + valorCheckRetra + "')";
                    }
                }
            }

            if (_criterioBusqueda.getSistemaOrigen() != null && !_criterioBusqueda.getSistemaOrigen().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (UPPER(REG_ERR_SIS_ORIG) LIKE'%" + _criterioBusqueda.getSistemaOrigen().toUpperCase() + "%')";
                } else {
                    where += "WHERE (UPPER(REG_ERR_SIS_ORIG) LIKE'%" + _criterioBusqueda.getSistemaOrigen().toUpperCase() + "%')";
                }
            }
            if (_criterioBusqueda.getNumeroExpediente() != null && !_criterioBusqueda.getNumeroExpediente().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (UPPER(REG_ERR_ID_FLEXIA) LIKE'%" + _criterioBusqueda.getNumeroExpediente().toUpperCase() + "%')";
                } else {
                    where += "WHERE (UPPER(REG_ERR_ID_FLEXIA) LIKE'%" + _criterioBusqueda.getNumeroExpediente().toUpperCase() + "%')";
                }
            }
            // para filtros de Fechas y Horas
            boolean conClausulaWhere = true;
            if (where.startsWith("WHERE")) {
                conClausulaWhere = false;
            }
            where += preparaWhereFechas(conClausulaWhere, ConfigurationParameter.getParameter(ConstantesMeLanbide53.CAMPO_FECHA_ERROR, ConstantesMeLanbide53.FICHERO_PROPIEDADES),
                    _criterioBusqueda.getFechaErrorInicio(), _criterioBusqueda.getFechaErrorFin());
            if (where.startsWith("WHERE")) {
                conClausulaWhere = false;
            }
            where += preparaWhereFechas(conClausulaWhere, ConfigurationParameter.getParameter(ConstantesMeLanbide53.CAMPO_FECHA_REVISION, ConstantesMeLanbide53.FICHERO_PROPIEDADES),
                    _criterioBusqueda.getFechaRevisionErrorInicio(), _criterioBusqueda.getFechaRevisionErrorFin());
            if (where.startsWith("WHERE")) {
                conClausulaWhere = false;
            }
            where += preparaWhereHoras(conClausulaWhere, ConfigurationParameter.getParameter(ConstantesMeLanbide53.CAMPO_FECHA_ERROR, ConstantesMeLanbide53.FICHERO_PROPIEDADES),
                    _criterioBusqueda.getHoraErrorInicio(), _criterioBusqueda.getHoraErrorFin(), _criterioBusqueda.getFechaErrorInicio());
        } catch (Exception ex) {
            log.error("Se ha producido un error preparando la clausula where de la sentencia de consulta con filtros para la gestion de errores", ex);
            throw new Exception(ex);
        }
        return where;
    }

    private String preparaClausulaWhereIdError(RegistroIdenErrorCriteriosFiltroVO _criterioBusqueda) throws Exception {
        String where = "";
        try {
            if (_criterioBusqueda.getId() != null && !_criterioBusqueda.getId().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (UPPER(IDEN_ERR_ID) LIKE'%" + _criterioBusqueda.getId().toUpperCase() + "%')";
                } else {
                    where += "WHERE (UPPER(IDEN_ERR_ID) LIKE'%" + _criterioBusqueda.getId().toUpperCase() + "%')";
                }
            }
            if (_criterioBusqueda.getDescripcion() != null && !_criterioBusqueda.getDescripcion().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (UPPER(IDEN_ERR_DESC_C) LIKE'%" + _criterioBusqueda.getDescripcion().toUpperCase() + "%')";
                } else {
                    where += "WHERE (UPPER(IDEN_ERR_DESC_C) LIKE'%" + _criterioBusqueda.getDescripcion().toUpperCase() + "%')";
                }
            }
            if (_criterioBusqueda.getCodTipo() != null && !_criterioBusqueda.getCodTipo().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  IDEN_ERR_TIPO =" + _criterioBusqueda.getCodTipo() + " ";
                } else {
                    where += "WHERE IDEN_ERR_TIPO =" + _criterioBusqueda.getCodTipo() + " ";
                }
            }

            if (_criterioBusqueda.getCodCritico() != null && !_criterioBusqueda.getCodCritico().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  IDEN_ERR_CRIT = " + _criterioBusqueda.getCodCritico().toUpperCase() + " ";
                } else {
                    where += "WHERE IDEN_ERR_CRIT = " + _criterioBusqueda.getCodCritico().toUpperCase() + " ";
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error preparando la clausula where de la sentencia de consulta con filtros para la gestion de errores", ex);
            throw new Exception(ex);
        }
        return where;
    }

    private String mapearValorCheck(String errorNotificado) {
        if (errorNotificado.equals(ConstantesMeLanbide53.BOOLEAN_TRUE) || errorNotificado.equals(ConstantesMeLanbide53.STANDFOR_SI_S)) {
            return ConstantesMeLanbide53.STANDFOR_SI_S;
        }
        if (errorNotificado.equals(ConstantesMeLanbide53.BOOLEAN_FALSE) || errorNotificado.equals(ConstantesMeLanbide53.STANDFOR_NO_N)) {
            return ConstantesMeLanbide53.STANDFOR_NO_N;
        }
        return "";
    }

    private String preparaWhereFechas(boolean conWhere, String nombreCampoBD, String fechaInicio, String fechaFin) {
        String where = "";
        if (fechaInicio != null && !fechaInicio.equals("") && fechaFin != null && !fechaFin.equals("")) {
            if (conWhere) {
                where += "WHERE (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') BETWEEN TO_DATE('" + fechaInicio + "','dd/mm/yyyy') AND TO_DATE('" + fechaFin + "','dd/mm/yyyy'))";
            } else {
                where += " AND  (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') BETWEEN TO_DATE('" + fechaInicio + "','dd/mm/yyyy') AND TO_DATE('" + fechaFin + "','dd/mm/yyyy'))";
            }
        } else if (fechaInicio != null && !fechaInicio.equals("")) {
            if (conWhere) {
                where += "WHERE (TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy')='" + fechaInicio + "')";
            } else {
                where += " AND  (TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy')='" + fechaInicio + "')";
            }
        } else if (fechaFin != null && !fechaFin.equals("")) {
            if (conWhere) {
                where += "WHERE (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') <= TO_DATE('" + fechaFin + "','dd/mm/yyyy'))";
            } else {
                where += " AND  (TO_DATE(TO_CHAR(" + nombreCampoBD + ",'dd/mm/yyyy'),'dd/mm/yyyy') <= TO_DATE('" + fechaFin + "','dd/mm/yyyy'))";
            }
        }
        return where;
    }

    private String preparaWhereHoras(boolean conWhere, String nombreCampoBD, String horaInicio, String horaFin, String fecha) {
        String where = "";
        if (horaInicio != null && !horaInicio.equals("") && horaFin != null && !horaFin.equals("")) {
            if (conWhere) {
                where += "WHERE (TO_CHAR(" + nombreCampoBD + ",'HH24:MI:SS') BETWEEN '" + horaInicio + "' AND '" + horaFin + "')";
            } else {
                where += " AND  (TO_CHAR(" + nombreCampoBD + ",'HH24:MI:SS') BETWEEN '" + horaInicio + "' AND '" + horaFin + "')";
            }
        } else if (horaInicio != null && !horaInicio.equals("")) {
            if (conWhere) {
                where += "WHERE (TO_CHAR(" + nombreCampoBD + ",'HH24:MI:SS') = '" + horaInicio + "')";

            } else {
                where += " AND  " + nombreCampoBD + " = TO_TIMESTAMP_TZ('" + fecha + " " + horaInicio + "','dd/mm/yyyy HH24:MI:SS')"; //" AND  (TO_CHAR(" + nombreCampoBD + ",'HH24:MI:SS') = '" + horaInicio + "')";
            }
        } else if (horaFin != null && !horaFin.equals("")) {
            if (conWhere) {
                where += "WHERE (TO_CHAR(" + nombreCampoBD + ",'HH24:MI:SS') <= '" + horaFin + "')";

            } else {
                where += " AND  (TO_CHAR(" + nombreCampoBD + ",'HH24:MI:SS') <= '" + horaFin + "')";
            }
        }
        return where;
    }

    public DetalleErrorVO getDetalleErrorPorID(String idError, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DetalleErrorVO detalleError = new DetalleErrorVO();
        try {
            String query = null;
            query = "SELECT  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES) + ".*, "
                    + "CASE WHEN IDEN_ERR_TIPO = 1 THEN 'Sistema' ELSE 'Funcional' END AS TIPO, "
                    + "CASE WHEN IDEN_ERR_CRIT = 1 THEN 'Bloqueante'"
                    + "WHEN IDEN_ERR_CRIT = 2 THEN 'Grave' "
                    + "WHEN IDEN_ERR_CRIT = 3 THEN 'Leve' "
                    + "WHEN IDEN_ERR_CRIT = 4 THEN 'Informativo' END AS CRITICO "
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_IDEN_ERR, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " WHERE IDEN_ERR_ID='" + idError + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                detalleError = (DetalleErrorVO) MeLanbide53MappingUtils.getInstance().map(rs, DetalleErrorVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando detalles del error para Gestion De Errores : " + idError, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return detalleError;
    }
//#292920

    public List<EstadisticasVO> getDatosEstadisticas(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EstadisticasVO> listaEstadisticas = new ArrayList<EstadisticasVO>();
        EstadisticasVO estadistica = new EstadisticasVO();
        try {
            String query = null;
            // datos de tabla ESTADISTICAS
            /*query = " SELECT ID, TO_CHAR(FEC_REGISTRO, 'dd/MM/yyyy HH24:MI:SS') AS FECHA_REGISTRO, "
                    + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,FEC_REGISTRO"
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " ORDER BY FEC_REGISTRO DESC ";*/
            //se sacan las últimas 50 filas
            query = " SELECT * FROM (SELECT ID, TO_CHAR(FEC_REGISTRO, 'dd/MM/yyyy HH24:MI:SS') AS FECHA_REGISTRO, "
                    + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,FEC_REGISTRO"
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " ORDER BY FEC_REGISTRO DESC) WHERE ROWNUM <= 50 ";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                estadistica = (EstadisticasVO) MeLanbide53MappingUtils.getInstance().map(rs, EstadisticasVO.class);
                listaEstadisticas.add(estadistica);
                estadistica = new EstadisticasVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las estadisticas ", ex);
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
        return listaEstadisticas;
    }

    public List<EstadisticasVO> getFiltroEstadisticas(FiltroEstadisticasVO filtroEstad, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EstadisticasVO> listaEstadisticas = new ArrayList<EstadisticasVO>();
        EstadisticasVO estadistica = new EstadisticasVO();
        try {
            String query = null;
            // datos de tabla ESTADISTICAS
            if ("false".equals(filtroEstad.getHistorico())) {
                query = " SELECT ID, TO_CHAR(FEC_REGISTRO, 'dd/MM/yyyy HH24:MI:SS') AS FECHA_REGISTRO,"
                        + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,FEC_REGISTRO"
                        + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                        + " WHERE TRUNC(FEC_REGISTRO) >= NVL(TO_DATE('" + filtroEstad.getFechaDesde() + "', 'dd/mm/rrrr'),TO_DATE('01/01/1900', 'dd/mm/rrrr'))"
                        + "   AND TRUNC(FEC_REGISTRO) <= NVL(TO_DATE('" + filtroEstad.getFechaHasta() + "', 'dd/mm/rrrr'),TO_DATE('31/12/2099', 'dd/mm/rrrr'))";

                if (!"".equals(filtroEstad.getIdProcedimiento())) {
                    query = query + " AND ID_PROCEDIMIENTO ='" + filtroEstad.getIdProcedimiento() + "'";
                }
                if (!"".equals(filtroEstad.getNumeroExpediente())) {
                    query = query + " AND NUM_EXPEDIENTE ='" + filtroEstad.getNumeroExpediente() + "'";
                }
                if (!"".equals(filtroEstad.getClave())) {
                    query = query + " AND OID_SOLICITUD ='" + filtroEstad.getClave() + "'";
                }
                if (!"".equals(filtroEstad.getResultado())) {
                    query = query + " AND RESULTADO ='" + filtroEstad.getResultado() + "'";
                }
                if (!"".equals(filtroEstad.getIdError())) {
                    query = query + " AND ID_ERROR like '%" + filtroEstad.getIdError() + "%'";
                }
                if (!"".equals(filtroEstad.getEvento())) {
                    query = query + " AND EVENTO ='" + filtroEstad.getEvento() + "'";
                }
                query = query + " ORDER BY FEC_REGISTRO DESC ";
            } else {
                query = " SELECT ID, TO_CHAR(FEC_REGISTRO, 'dd/MM/yyyy HH24:MI:SS') AS FECHA_REGISTRO,"
                        + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,FEC_REGISTRO"
                        + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                        + " WHERE TRUNC(FEC_REGISTRO) >= NVL(TO_DATE('" + filtroEstad.getFechaDesde() + "', 'dd/mm/rrrr'),TO_DATE('01/01/1900', 'dd/mm/rrrr'))"
                        + "   AND TRUNC(FEC_REGISTRO) <= NVL(TO_DATE('" + filtroEstad.getFechaHasta() + "', 'dd/mm/rrrr'),TO_DATE('31/12/2099', 'dd/mm/rrrr'))";
                if (!"".equals(filtroEstad.getIdProcedimiento())) {
                    query = query + " AND ID_PROCEDIMIENTO ='" + filtroEstad.getIdProcedimiento() + "'";
                }
                if (!"".equals(filtroEstad.getNumeroExpediente())) {
                    query = query + " AND NUM_EXPEDIENTE ='" + filtroEstad.getNumeroExpediente() + "'";
                }
                if (!"".equals(filtroEstad.getClave())) {
                    query = query + " AND OID_SOLICITUD ='" + filtroEstad.getClave() + "'";
                }
                if (!"".equals(filtroEstad.getResultado())) {
                    query = query + " AND RESULTADO ='" + filtroEstad.getResultado() + "'";
                }
                if (!"".equals(filtroEstad.getIdError())) {
                    query = query + " AND ID_ERROR like '%" + filtroEstad.getIdError() + "%'";
                }
                if (!"".equals(filtroEstad.getEvento())) {
                    query = query + " AND EVENTO ='" + filtroEstad.getEvento() + "'";
                }

                query = query + " UNION ALL"
                        + " SELECT ID, TO_CHAR(FEC_REGISTRO, 'dd/MM/yyyy HH24:MI:SS') AS FECHA_REGISTRO,"
                        + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,FEC_REGISTRO"
                        + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS_HIST, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                        + " WHERE TRUNC(FEC_REGISTRO) >= NVL(TO_DATE('" + filtroEstad.getFechaDesde() + "', 'dd/mm/rrrr'),TO_DATE('01/01/1900', 'dd/mm/rrrr'))"
                        + "   AND TRUNC(FEC_REGISTRO) <= NVL(TO_DATE('" + filtroEstad.getFechaHasta() + "', 'dd/mm/rrrr'),TO_DATE('31/12/2099', 'dd/mm/rrrr'))";
                if (!"".equals(filtroEstad.getIdProcedimiento())) {
                    query = query + " AND ID_PROCEDIMIENTO ='" + filtroEstad.getIdProcedimiento() + "'";
                }
                if (!"".equals(filtroEstad.getNumeroExpediente())) {
                    query = query + " AND NUM_EXPEDIENTE ='" + filtroEstad.getNumeroExpediente() + "'";
                }
                if (!"".equals(filtroEstad.getClave())) {
                    query = query + " AND OID_SOLICITUD ='" + filtroEstad.getClave() + "'";
                }
                if (!"".equals(filtroEstad.getResultado())) {
                    query = query + " AND RESULTADO ='" + filtroEstad.getResultado() + "'";
                }
                if (!"".equals(filtroEstad.getIdError())) {
                    query = query + " AND ID_ERROR like '%" + filtroEstad.getIdError() + "%'";
                }
                if (!"".equals(filtroEstad.getEvento())) {
                    query = query + " AND EVENTO ='" + filtroEstad.getEvento() + "'";
                }
                query = query + " ORDER BY FEC_REGISTRO DESC ";
            }

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                estadistica = (EstadisticasVO) MeLanbide53MappingUtils.getInstance().map(rs, EstadisticasVO.class);
                listaEstadisticas.add(estadistica);
                estadistica = new EstadisticasVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las estadisticas ", ex);
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
        return listaEstadisticas;
    }

    public DetalleEstadisticaVO getDetalleEstadisticaPorID(int idEstadistica, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DetalleEstadisticaVO detalleEstadistica = new DetalleEstadisticaVO();
        try {
            String query = null;
            // datos de tabla ESTADISTICAS accediendo por ID
            query = " SELECT ID, TO_CHAR(FEC_REGISTRO) AS FECHA_REGISTRO, "
                    + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,REGLAS_SUSCRIPCION"
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + idEstadistica;

            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                detalleEstadistica = (DetalleEstadisticaVO) MeLanbide53MappingUtils.getInstance().map(rs, DetalleEstadisticaVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando detalles de la estadistica para Gestion De Estadisticas : " + idEstadistica, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return detalleEstadistica;
    }

    public DetalleEstadisticaVO getDetalleEstadisticaHistPorID(int idEstadistica, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DetalleEstadisticaVO detalleEstadistica = new DetalleEstadisticaVO();
        try {
            String query = null;
            // datos de tabla ESTADISTICAS_HIST accediendo por ID
            query = " SELECT ID, TO_CHAR(FEC_REGISTRO) AS FECHA_REGISTRO, "
                    + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,REGLAS_SUSCRIPCION"
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS_HIST, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + idEstadistica;

            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                detalleEstadistica = (DetalleEstadisticaVO) MeLanbide53MappingUtils.getInstance().map(rs, DetalleEstadisticaVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando detalles de la estadistica histórico para Gestion De Estadisticas : " + idEstadistica, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return detalleEstadistica;
    }

    public List<EstadisticasVO_EXCEL> getListaDatosInformeEstadisticas(FiltroEstadisticasVO filtroEstad, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EstadisticasVO_EXCEL> listEstadisticas = new ArrayList<EstadisticasVO_EXCEL>();
        EstadisticasVO_EXCEL datosInformeEstadisticas;
        try {
            String query = null;
            if ("false".equals(filtroEstad.getHistorico())) {
                query = " SELECT ID, TO_CHAR(FEC_REGISTRO, 'dd/MM/yyyy HH24:MI:SS') AS FECHA_REGISTRO,"
                        + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,FEC_REGISTRO"
                        + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                        + " WHERE TRUNC(FEC_REGISTRO) >= NVL(TO_DATE('" + filtroEstad.getFechaDesde() + "', 'dd/mm/rrrr'),TO_DATE('01/01/1900', 'dd/mm/rrrr'))"
                        + "   AND TRUNC(FEC_REGISTRO) <= NVL(TO_DATE('" + filtroEstad.getFechaHasta() + "', 'dd/mm/rrrr'),TO_DATE('31/12/2099', 'dd/mm/rrrr'))";

                if (!"".equals(filtroEstad.getIdProcedimiento())) {
                    query = query + " AND ID_PROCEDIMIENTO ='" + filtroEstad.getIdProcedimiento() + "'";
                }
                if (!"".equals(filtroEstad.getNumeroExpediente())) {
                    query = query + " AND NUM_EXPEDIENTE ='" + filtroEstad.getNumeroExpediente() + "'";
                }
                if (!"".equals(filtroEstad.getClave())) {
                    query = query + " AND OID_SOLICITUD ='" + filtroEstad.getClave() + "'";
                }
                if (!"".equals(filtroEstad.getResultado())) {
                    query = query + " AND RESULTADO ='" + filtroEstad.getResultado() + "'";
                }
                if (!"".equals(filtroEstad.getIdError())) {
                    query = query + " AND ID_ERROR like '%" + filtroEstad.getIdError() + "%'";
                }
                if (!"".equals(filtroEstad.getEvento())) {
                    query = query + " AND EVENTO ='" + filtroEstad.getEvento() + "'";
                }
                query = query + " ORDER BY FEC_REGISTRO DESC ";

            } else {
                query = " SELECT ID, TO_CHAR(FEC_REGISTRO, 'dd/MM/yyyy HH24:MI:SS') AS FECHA_REGISTRO,"
                        + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,FEC_REGISTRO"
                        + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                        + " WHERE TRUNC(FEC_REGISTRO) >= NVL(TO_DATE('" + filtroEstad.getFechaDesde() + "', 'dd/mm/rrrr'),TO_DATE('01/01/1900', 'dd/mm/rrrr'))"
                        + "   AND TRUNC(FEC_REGISTRO) <= NVL(TO_DATE('" + filtroEstad.getFechaHasta() + "', 'dd/mm/rrrr'),TO_DATE('31/12/2099', 'dd/mm/rrrr'))";
                if (!"".equals(filtroEstad.getIdProcedimiento())) {
                    query = query + " AND ID_PROCEDIMIENTO ='" + filtroEstad.getIdProcedimiento() + "'";
                }
                if (!"".equals(filtroEstad.getNumeroExpediente())) {
                    query = query + " AND NUM_EXPEDIENTE ='" + filtroEstad.getNumeroExpediente() + "'";
                }
                if (!"".equals(filtroEstad.getClave())) {
                    query = query + " AND OID_SOLICITUD ='" + filtroEstad.getClave() + "'";
                }
                if (!"".equals(filtroEstad.getResultado())) {
                    query = query + " AND RESULTADO ='" + filtroEstad.getResultado() + "'";
                }
                if (!"".equals(filtroEstad.getIdError())) {
                    query = query + " AND ID_ERROR like '%" + filtroEstad.getIdError() + "%'";
                }
                if (!"".equals(filtroEstad.getEvento())) {
                    query = query + " AND EVENTO ='" + filtroEstad.getEvento() + "'";
                }
                query = query + " UNION ALL"
                        + " SELECT ID, TO_CHAR(FEC_REGISTRO, 'dd/MM/yyyy HH24:MI:SS') AS FECHA_REGISTRO,"
                        + " ID_PROCEDIMIENTO,NUM_EXPEDIENTE,OID_SOLICITUD,RESULTADO,ID_ERROR,EVENTO,OBSERVACIONES,FEC_REGISTRO"
                        + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide53.TABLA_MELANBIDE53_ESTADISTICAS_HIST, ConstantesMeLanbide53.FICHERO_PROPIEDADES)
                        + " WHERE TRUNC(FEC_REGISTRO) >= NVL(TO_DATE('" + filtroEstad.getFechaDesde() + "', 'dd/mm/rrrr'),TO_DATE('01/01/1900', 'dd/mm/rrrr'))"
                        + "   AND TRUNC(FEC_REGISTRO) <= NVL(TO_DATE('" + filtroEstad.getFechaHasta() + "', 'dd/mm/rrrr'),TO_DATE('31/12/2099', 'dd/mm/rrrr'))";
                if (!"".equals(filtroEstad.getIdProcedimiento())) {
                    query = query + " AND ID_PROCEDIMIENTO ='" + filtroEstad.getIdProcedimiento() + "'";
                }
                if (!"".equals(filtroEstad.getNumeroExpediente())) {
                    query = query + " AND NUM_EXPEDIENTE ='" + filtroEstad.getNumeroExpediente() + "'";
                }
                if (!"".equals(filtroEstad.getClave())) {
                    query = query + " AND OID_SOLICITUD ='" + filtroEstad.getClave() + "'";
                }
                if (!"".equals(filtroEstad.getResultado())) {
                    query = query + " AND RESULTADO ='" + filtroEstad.getResultado() + "'";
                }
                if (!"".equals(filtroEstad.getIdError())) {
                    query = query + " AND ID_ERROR like '%" + filtroEstad.getIdError() + "%'";
                }
                if (!"".equals(filtroEstad.getEvento())) {
                    query = query + " AND EVENTO ='" + filtroEstad.getEvento() + "'";
                }
                query = query + " ORDER BY FEC_REGISTRO DESC ";
            }

            log.debug("SQL CREACION  EXCEL = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                log.error("fila informe");
                datosInformeEstadisticas = (EstadisticasVO_EXCEL) MeLanbide53MappingUtils.getInstance().map(rs, EstadisticasVO_EXCEL.class);
                listEstadisticas.add(datosInformeEstadisticas);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos del informe de estadisticas", ex);
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
        return listEstadisticas;
    }
    
    public int oidEsOK(String oidDocumento, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int esOK = 0;
        try {
            String query = null;
            query = " SELECT RESULTADO FROM ESTADISTICAS"
                    + " WHERE OID_SOLICITUD = '" + oidDocumento + "'"
                    + " AND RESULTADO='OK'";
            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                esOK = 1;
            }
            log.info("esOK = " + esOK + " de oid = " + oidDocumento);
        } catch (Exception ex) {
            log.error("Se ha producido un error en oidEsOK : " + oidDocumento, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return esOK;
    }
    
    public String getRegistroDeOid(String oidDocumento, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String registro = "no_existe";
        try {
            String query = null;
            query = "SELECT RED_EJE,RED_NUM FROM R_RED"
                    + " WHERE RED_IDDOC_GESTOR = '" + oidDocumento + "' ORDER BY RED_EJE,RED_NUM DESC";
            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                String ejercicio = String.valueOf(rs.getInt("RED_EJE"));
                String numRegistro = String.valueOf(rs.getInt("RED_NUM"));
                log.info("ejercicio = " + ejercicio);
                log.info("numRegistro = " + numRegistro);
                registro = ejercicio + "/" + numRegistro;
            }
            log.info("Registro = " + registro + " de oid = " + oidDocumento);
        } catch (Exception ex) {
            log.error("Se ha producido un error en documentoEnRegistro : " + oidDocumento, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return registro;
    }
    
}
