/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide73.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide73.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide73.util.ConstantesMeLanbide73;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.GrupoUnidadesOrganicas;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.UnidadOrganica;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.UnidadTramitadoraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.Usuario;
import es.altia.util.commons.DateOperations;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import java.util.List;

/**
 *
 * @author Kepa
 */
public class MeLanbide73DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide73DAO.class);

    //Instancia
    private static MeLanbide73DAO instance = null;

    private MeLanbide73DAO() {
    }

    public static MeLanbide73DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide73DAO.class) {
                instance = new MeLanbide73DAO();
            }
        }
        return instance;
    }

    public ArrayList<UnidadTramitadoraVO> cargaUnidadesExpediente(String numExp, Connection con) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA cargarUnidades DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        ArrayList<UnidadTramitadoraVO> listaReturn = new ArrayList<UnidadTramitadoraVO>();
        try {
            String sql = "Select UOR_COD, UOR_COD_VIS, UOR_NOM from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.A_UOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOR_COD in (Select EXP_UOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide73.E_EXP, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where EXP_NUM='" + numExp + "')";
            if (log.isInfoEnabled()) {
                log.debug("Carga Unidades Expediente: " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                UnidadTramitadoraVO unidadTramitadora = new UnidadTramitadoraVO();
                unidadTramitadora.setCodUnidad(rs.getString("UOR_COD"));
                unidadTramitadora.setNomUnidad(rs.getString("UOR_NOM"));
                unidadTramitadora.setCodVisible(rs.getString("UOR_COD_VIS"));

                listaReturn.add(unidadTramitadora);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando las unidades tramitadoras del expediente", e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("FINALIZA  cargarUnidades DAO ");
        }
        return listaReturn;
    }// cargarUnidades

    public ArrayList<UnidadTramitadoraVO> cargarListaUnidades(Connection con) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA cargarListaUnidades DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        ArrayList<UnidadTramitadoraVO> listaReturn = new ArrayList<UnidadTramitadoraVO>();
        try {
            String sql = "Select UOR_COD, UOR_COD_VIS, UOR_NOM from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.A_UOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOR_COD_VIS like 'RGI%' and UOR_FECHA_BAJA is null order by UOR_COD ";
            if (log.isInfoEnabled()) {
                log.debug("Carga Lista Unidades RGI" + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                UnidadTramitadoraVO unidadTramitadora = new UnidadTramitadoraVO();
                unidadTramitadora.setCodUnidad(rs.getString("UOR_COD"));
                unidadTramitadora.setNomUnidad(rs.getString("UOR_NOM"));
                unidadTramitadora.setCodVisible(rs.getString("UOR_COD_VIS"));

                listaReturn.add(unidadTramitadora);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando las unidades tramitadoras - ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return listaReturn;
    }// cargar lista Unidades

    public ArrayList<UnidadTramitadoraVO> cargaUnidadesUsuario(String codUsuario, Connection con) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA cargarUnidades USUARIO DAO ");
        }

        Statement st = null;
        ResultSet rs = null;
        ArrayList<UnidadTramitadoraVO> listaReturn = new ArrayList<UnidadTramitadoraVO>();
        try {
            String sql = "Select UOR_COD, UOR_COD_VIS, UOR_NOM from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.A_UOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOR_COD in (Select UOU_UOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide73.A_UOU, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOU_USU='" + codUsuario + "')";
            if (log.isInfoEnabled()) {
                log.debug("Carga Unidades USUARIO: " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                UnidadTramitadoraVO unidadTramitadora = new UnidadTramitadoraVO();
                unidadTramitadora.setCodUnidad(rs.getString("UOR_COD"));
                unidadTramitadora.setNomUnidad(rs.getString("UOR_NOM"));
                unidadTramitadora.setCodVisible(rs.getString("UOR_COD_VIS"));

                listaReturn.add(unidadTramitadora);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando las unidades tramitadoras del expediente", e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        if (log.isInfoEnabled()) {
            log.debug("FINALIZA  cargarUnidades USUARIO DAO ");
        }
        return listaReturn;
    }// cargarUnidades USUARIO

    public boolean cambiarUnidad(UnidadTramitadoraVO nueva, String numExp, Connection con) throws Exception {
        log.debug("=============== cambiarUnidad DAO");
        log.debug("Unidad nueva " + nueva.getNomUnidad());
        log.debug("Expediente " + numExp);
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        String sql = "";
        String selectTram = "";
        String updateTram = "";

        try {
            sql = "Select UOR_COD from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.A_UOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOR_NOM='" + nueva.getNomUnidad() + "'";
            log.debug("Cojo el codigo de la unidad nueva: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                nueva.setCodUnidad(rs.getString("UOR_COD"));
            }

            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide73.E_EXP, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " SET EXP_UOR=" + nueva.getCodUnidad()
                    + " WHERE EXP_NUM='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            int insert = st.executeUpdate(query);
            //       con.commit();
            actualizaUnidadTramite(nueva, numExp, con);
            if (insert > 0) {
                log.debug("================= Cambio de unidad de inicio OK");

                return true;

            } else {
                return false;
            }

        } catch (Exception ex) {
            con.rollback();
            log.debug("Se ha producido un error al Modificar la Unidad - "
                    + nueva.getNomUnidad() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
                rs.close();
            }
        }
    }//cambiarUnidad

    private void actualizaUnidadTramite(UnidadTramitadoraVO nueva, String numExp, Connection con) throws Exception {
        log.debug("============= actualizar tramites");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "SELECT CRO_MUN,CRO_PRO,CRO_EJE,CRO_NUM,CRO_TRA,CRO_OCU FROM "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.E_CRO, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where CRO_NUM='" + numExp + "'";
            log.debug(query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                con.setAutoCommit(false);
                Statement sta = null;
                String tramite = rs.getString("CRO_TRA");
                log.debug("tramite " + tramite);

                String queryUpdate = "update "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide73.E_CRO, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                        + " set CRO_UTR=" + nueva.getCodUnidad()
                        + " where CRO_NUM='" + numExp + "'";

                log.debug("UPDATE= " + queryUpdate);
                sta = con.createStatement();
                int insert2 = sta.executeUpdate(queryUpdate);
                if (insert2 > 0) {
                    log.debug("================= Cambio de unidad de inicio en tramite OK");

                }

            }//rs.next

        } catch (Exception e) {
            con.rollback();
            log.error("Se ha producido un error al Modificar la Unidad - "
                    + nueva.getNomUnidad() + " - " + e);
            throw new Exception(e);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            con.commit();
            if (st != null) {
                st.close();
                rs.close();
            }
        }
    }// actualizaUnidadTramite

    public boolean insertarHistorico(int codMunicipio, String ejercicio, String numExpediente, String fechaOperacion, String codUsuario, String descripcion, Calendar fechaAct, Connection con) throws Exception {
        boolean insertOK = false;
        log.debug("============= Insertar Operaci√≥n Expediente");
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        String codOpe = "28";
        Timestamp tim = DateOperations.toTimestamp(fechaAct);
        try {
            int id_operacion = this.getNextId("SEQ_OPERACIONES_EXPEDIENTE", con);
            query = "INSERT INTO "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.OPERACIONES_EXPEDIENTE, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " ("
                    + "ID_OPERACION, COD_MUNICIPIO, EJERCICIO, NUM_EXPEDIENTE, TIPO_OPERACION, FECHA_OPERACION, COD_USUARIO, DESCRIPCION_OPERACION)"
                    + " VALUES ('"
                    + id_operacion + "', "
                    + "'" + codMunicipio + "', '"
                    + ejercicio + "', '"
                    + numExpediente + "', '"
                    + codOpe + "', "
                    + "TO_DATE('" + fechaOperacion + "' ,'DD/MM/YYYY HH24:MI:SS'), '"
                    + codUsuario + "', '"
                    + descripcion + "')";
            log.debug("Query: " + query);
            st = con.createStatement();

            int ok = st.executeUpdate(query);
            if (ok > 0) {
                log.debug("Ha insertado en la tabla OPERACIONES_EXPEDIENTE");
                insertOK = true;
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error insertando el registro de la Operaci√≥n de cambio de UNIDAD del expediente " + numExpediente, e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return insertOK;
    }//insertar historico

    private Integer getNextId(String seqName, Connection con) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        Integer numSec = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select " + seqName + ".nextval from dual";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
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
        return numSec;
    }

    public List<GeneralComboVO> cargarListaUsuarios(Connection con) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA cargarListaUsuarios DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        final List<GeneralComboVO> listaReturn = new ArrayList<GeneralComboVO>();
        try {
            final String sql = "select USU_COD, USU_LOG, USU_NOM from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_USU, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where USU_FBA IS NULL"                    
                    + " and USU_COD != "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOU_USU_ADMIN,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " order by USU_NOM";
            if (log.isInfoEnabled()) {
                log.debug("Carga Lista Usuarios" + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                final GeneralComboVO generalComboVO = new GeneralComboVO();
                generalComboVO.setCodigo(rs.getString("USU_COD"));
                generalComboVO.setDescripcion(rs.getString("USU_NOM"));
                listaReturn.add(generalComboVO);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los usuarios - ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return listaReturn;
    }// cargar lista Unidades

    public List<GeneralComboVO> cargarListaUORS(Connection con) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA cargarListaUORs DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        final List<GeneralComboVO> listaReturn = new ArrayList<GeneralComboVO>();
        try {
            final String sql = "select UOR_COD, UOR_NOM from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where OFICINA_REGISTRO != " 
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_OFICINA_REGISTRO,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + "/* where UOR_ESTADO = 'A'*/ order by UOR_NOM";
            if (log.isInfoEnabled()) {
                log.debug("Carga Lista UORs" + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                final GeneralComboVO generalComboVO = new GeneralComboVO();
                generalComboVO.setCodigo(rs.getString("UOR_COD"));
                generalComboVO.setDescripcion(rs.getString("UOR_NOM"));
                listaReturn.add(generalComboVO);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando las UORs - ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return listaReturn;
    }// cargar lista Unidades        

    public List<GeneralComboVO> cargarGruposUORS(Connection con, int codOrganizacion) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA cargarGruposUORs DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        final List<GeneralComboVO> listaReturn = new ArrayList<GeneralComboVO>();
        try {
            final String sql = "select GRPUOR_COD, GRPUOR_NOMBRE from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_GRPUOR,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where GRPUOR_ORG = " + codOrganizacion
                    + " order by GRPUOR_NOMBRE";
            if (log.isInfoEnabled()) {
                log.debug("Carga grupos UORs" + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                GeneralComboVO generalComboVO = new GeneralComboVO();
                generalComboVO.setCodigo(rs.getString("GRPUOR_COD"));
                generalComboVO.setDescripcion(rs.getString("GRPUOR_NOMBRE"));
                listaReturn.add(generalComboVO);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando los grupos de UORs - ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return listaReturn;
    }// cargar lista OUR            

    public boolean existeGrupo(Connection con, final String nombreGrupoUOR,
            int codOrganizacion) throws Exception {
        boolean existe = false;
        if (log.isInfoEnabled()) {
            log.debug("INICIA existeGrupo DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            final String sql = "select GRPUOR_COD from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_GRPUOR,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where GRPUOR_ORG = " + codOrganizacion
                    + " and upper(" + "GRPUOR_NOMBRE)= upper('" + nombreGrupoUOR + "')";
            if (log.isInfoEnabled()) {
                log.debug("Existe grupo" + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            existe = rs.next();
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando el grupo - ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return existe;
    }

    public boolean existeUO(Connection con, int idUOR) throws Exception {
        boolean existe = false;
        if (log.isInfoEnabled()) {
            log.debug("INICIA existeUO DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        try {
            final String sql = "select UOR_COD from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.A_UOR,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOR_COD = " + idUOR;
            if (log.isInfoEnabled()) {
                log.debug("Existe UO " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            existe = rs.next();
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando la unidad org·nica - ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return existe;
    }

    public int insertarGrupoUOR(final Connection con, final String nombreGrupoUOR,
            int codOrganizacion) throws Exception {
        int seq = -1;
        log.debug("============= Insertar grupo UOR");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            st = con.createStatement();
            int id_grupoUOR = this.getNextId("SEQ_A_GRPUOR", con);
            query = "INSERT INTO "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_GRPUOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " ("
                    + "GRPUOR_COD, GRPUOR_NOMBRE, GRPUOR_ORG)"
                    + " VALUES ("
                    + id_grupoUOR + ", "
                    + "'" + nombreGrupoUOR + "',"
                    + codOrganizacion + ")";
            log.debug("Query: " + query);
            if (st.executeUpdate(query) > 0) {
                seq = id_grupoUOR;
                log.debug("Ha insertado en la tabla A_GRPUOR");
            }
        } catch (SQLException e) {
            seq = -2;
            log.error("Se ha producido un error insertando un grupo de UORs " + nombreGrupoUOR, e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return seq;
    }//insertar grupoUOR

    public boolean borrarGrupoUOR(final Connection con, int id, int codOrganizacion) throws Exception {
        boolean borrarOK = false;
        log.debug("============= Eliminar grupo UOR");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "delete from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_GRPUOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where GRPUOR_COD = " + id
                    + " and GRPUOR_ORG = " + codOrganizacion;
            log.debug("Query: " + query);
            st = con.createStatement();

            borrarOK = st.executeUpdate(query) > 0;
            if (borrarOK) {
                log.debug("Ha eliminado en la tabla A_GRPUOR");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error eliminado un grupo de UORs " + id, e);
        } catch (Exception e) {
            log.error("Se ha producido un error de integridad referencial eliminando un grupo de UORs " + id, e);
            throw new Exception(e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return borrarOK;
    }//eliminar grupoUOR

    public boolean borrarUORGrupoUOR(final Connection con, int idGrupo,
            int idUOR, int codOrganizacion) throws Exception {
        boolean borrarOK = false;
        log.debug("============= Eliminar UOR_GRPUOR");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "delete from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR_GRPUOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOR_COD = " + idUOR + " and UOR_GRPUOR_COD = " + idGrupo
                    + " and UOR_GRPUOR_ORG = " + codOrganizacion;
            log.debug("Query: " + query);
            st = con.createStatement();

            borrarOK = st.executeUpdate(query) > 0;
            if (borrarOK) {
                log.debug("Ha eliminado en la tabla A_UOR_GRPUOR");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error eliminado un grupo de UORs " + idGrupo + ", " + idUOR, e);
        } catch (Exception e) {
            log.error("Se ha producido un error de integridad referencial eliminando un grupo de UORs " + idGrupo + ", " + idUOR, e);
            throw new Exception(e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return borrarOK;
    }//borrar UORGrupoUOR

    public int modificarGrupoUOR(final Connection con, int id, int codOrganizacion,
            final String nombreGrupo) throws Exception {
        int modificarOK = -1;
        log.debug("============= Modificar grupo UOR");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "update "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_GRPUOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " set GRPUOR_NOMBRE = '" + nombreGrupo + "'"
                    + " where GRPUOR_COD = " + id
                    + " and GRPUOR_ORG = " + codOrganizacion;
            log.debug("Query: " + query);
            st = con.createStatement();
            modificarOK = st.executeUpdate(query) > 0 ? id : -1;
        } catch (SQLException e) {
            modificarOK = -2;
            log.error("Se ha producido un error modificando un grupo de UORs " + id, e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch 
        if (modificarOK != -1) {
            log.debug("Ha modificado en la tabla A_GRPUOR");
        }
        return modificarOK;
    }//modificar grupoUOR

    public boolean insertarUorGrpuor(final Connection con, int idGrupo, int idUOR,
            int codOrganizacion) throws Exception {
        boolean insertUOROk = false;
        log.debug("============= Insertar UOR en grupo UOR");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "insert into "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR_GRPUOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " (UOR_COD, UOR_GRPUOR_COD, UOR_GRPUOR_ORG) values("
                    + idUOR + ", "
                    + idGrupo + ", " + codOrganizacion + ")";
            log.debug("Query: " + query);
            st = con.createStatement();

            insertUOROk = st.executeUpdate(query) > 0;
            if (insertUOROk) {
                log.debug("Ha modificado en la tabla A_UOR_GRPUOR");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error insertando en la tabla A_UOR_GRPUOR" + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return insertUOROk;
    }//insertar en A_UOR_GRPUOR    

    public boolean eliminarUorGrpuor(final Connection con, int idGrupo, int idUOR,
            int codOrganizacion) throws Exception {
        boolean eliminarOURFromGrupoOK = false;
        log.debug("============= Eliminar UOR en grupo UOR");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            query = "delete from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR_GRPUOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOR_COD = " + idUOR
                    + " and UOR_GRPUOR_COD = " + idGrupo
                    + " and UOR_GRPUOR_ORG = " + codOrganizacion;
            log.debug("Query: " + query);
            st = con.createStatement();

            eliminarOURFromGrupoOK = st.executeUpdate(query) > 0;
            if (eliminarOURFromGrupoOK) {
                log.debug("Ha modificado en la tabla A_UOR_GRPUOR");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error eliminando en la tabla A_UOR_GRPUOR" + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return eliminarOURFromGrupoOK;
    }//eliminar en A_UOR_GRPUOR        

    public boolean insertarUsuarioGrpuor(final Connection con, int idGrupo,
            int idUsuario, int codOrganizacion) throws Exception {
        boolean insertUsuarioOk = false;
        log.debug("============= Insertar Usuario en grupo UOR");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            if (idUsuario != new Integer(
                    ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOU_USU_ADMIN,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)).intValue()) {
                query = "insert into "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_USU_GRPUOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                        + " (USU_COD, USU_GRPUOR_COD, USU_GRPUOR_ORG) values("
                        + idUsuario + ", "
                        + idGrupo + ", " + codOrganizacion + ")";
                log.debug("Query: " + query);
                st = con.createStatement();

                insertUsuarioOk = st.executeUpdate(query) > 0;
            }
            if (log.isInfoEnabled() && insertUsuarioOk) {
                log.debug("Ha modificado en la tabla A_USU_GRPUOR");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error insertando en la tabla A_USU_GRPUOR" + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return insertUsuarioOk;
    }//insertar en A_USU_GRPUOR    

    public boolean eliminarUsuarioGrpuor(final Connection con, int idGrupo, int idUsuario,
            int codOrganizacion) throws Exception {
        boolean eliminarUsuarioFromGrupoOK = false;
        log.debug("============= Eliminar UOR en grupo UOR");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            if (idUsuario != new Integer(
                    ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOU_USU_ADMIN,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)).intValue()) {
                query = "delete from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_USU_GRPUOR, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                        + " where USU_COD = " + idUsuario
                        + " and USU_GRPUOR_COD = " + idGrupo
                        + " and USU_GRPUOR_ORG = " + codOrganizacion;
                log.debug("Query: " + query);
                st = con.createStatement();

                eliminarUsuarioFromGrupoOK = st.executeUpdate(query) > 0;
            }
            if (log.isInfoEnabled() && eliminarUsuarioFromGrupoOK) {
                log.debug("Ha modificado en la tabla A_USU_GRPUOR");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error eliminando en la tabla A_USU_GRPUOR" + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return eliminarUsuarioFromGrupoOK;
    }//eliminar en A_UOR_GRPUOR        

    public boolean insertarUsuarioEnUOU(final Connection con, int idUOR, int idUsuario,
            int codOrganizacion) throws Exception {
        boolean result = false;
        log.debug("============= Insertar usuario en unidad org·nica");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            if (idUsuario != new Integer(
                    ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOU_USU_ADMIN,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)).intValue()) {
                query = "insert into "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide73.A_UOU, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                        + " (UOU_ORG, UOU_ENT, UOU_UOR, UOU_USU) values ("
                        + codOrganizacion
                        + ", " + new Integer(ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOU_ENT, ConstantesMeLanbide73.FICHERO_PROPIEDADES))
                        + ", " + idUOR + ", " + idUsuario
                        //                    + ", " + new Integer(ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOU_CAR, ConstantesMeLanbide73.FICHERO_PROPIEDADES))
                        + ")";
                log.debug("Query: " + query);
                st = con.createStatement();

                result = st.executeUpdate(query) > 0;
            }
            if (result) {
                log.debug("Ha modificado en la tabla A_UOU");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error insertando en la tabla A_UOU " + idUOR + ", " + idUsuario + ", " + result + ", "
                    + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return result;
    }//insertar en A_UOU

    public boolean eliminarUsuarioEnUOU(final Connection con, int idUOR, int idUsuario,
            int codOrganizacion) throws Exception {
        boolean result = false;
        log.debug("============= Eliminar usuario de unidad org·nica");
        Statement st = null;
        ResultSet rs = null;
        String query = "";

        try {
            if (idUsuario != new Integer(
                    ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOU_USU_ADMIN,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)).intValue()) {
                query = "delete from  "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide73.A_UOU, ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                        + " where UOU_ORG = " + codOrganizacion
                        + " and UOU_ENT = " + new Integer(ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOU_ENT, ConstantesMeLanbide73.FICHERO_PROPIEDADES))
                        + " and UOU_UOR = " + idUOR
                        + " and UOU_USU = " + idUsuario;
//                    + " AND UOU_CAR = " + new Integer(ConfigurationParameter.getParameter(ConstantesMeLanbide73.VALUE_UOU_CAR, ConstantesMeLanbide73.FICHERO_PROPIEDADES));
                log.debug("Query: " + query);
                st = con.createStatement();

                result = st.executeUpdate(query) > 0;
            }
            if (result) {
                log.debug("Ha eliminado 1 fila en la tabla A_UOU");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error eliminando en la tabla A_UOU" + idUOR + ", " + idUsuario + ", " + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return result;
    }//eliminar en A_UOU   

    public List<UnidadOrganica> getUORsAfectadas(final Connection con,
            final List<UnidadOrganica> uors, int idGrupo, int codOrganizacion) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA getUORsAfectadas DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        List<UnidadOrganica> listaReturn = new ArrayList<UnidadOrganica>();
        try {
            String setUors = convertirListaUorsASqlString(uors);
            if (!setUors.equals("")) {
                String sql = "select UOR_COD, UOR_PAD, UOR_NOM, UOR_ESTADO, UOR_COD_VIS from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR,
                                ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                        + " where UOR_COD in (select UOR_COD FROM "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR_GRPUOR,
                                ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                        + " where UOR_GRPUOR_COD != " + idGrupo + " AND UOR_COD in "
                        + setUors + " AND UOR_GRPUOR_ORG = " + codOrganizacion + ")"
                        + " order by UOR_NOM";

                log.debug("Query: " + sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    final UnidadOrganica unidadOrganica = new UnidadOrganica(rs.getInt("UOR_COD"),
                            rs.getInt("UOR_PAD"), rs.getString("UOR_NOM"),
                            rs.getString("UOR_ESTADO"), rs.getString("UOR_COD_VIS"));
                    listaReturn.add(unidadOrganica);
                }
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error consultando en la tabla A_UOR_GRPUOR " + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return listaReturn;
    }

    public List<GrupoUnidadesOrganicas> getGruposUORsAfectados(final Connection con,
            final List<UnidadOrganica> uors, int idGrupo, int codOrganizacion) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA getGruposUORsAfectados DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        List<GrupoUnidadesOrganicas> listaReturn = new ArrayList<GrupoUnidadesOrganicas>();
        try {
            String setUors = convertirListaUorsASqlString(uors);
            if (!setUors.equals("")) {
                String sql = "select GRPUOR_COD, GRPUOR_NOMBRE from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_GRPUOR,
                                ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                        + " where GRPUOR_COD in (select UOR_GRPUOR_COD from "
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR_GRPUOR,
                                ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                        + " where UOR_GRPUOR_COD != " + idGrupo + " and UOR_COD in "
                        + setUors + " and UOR_GRPUOR_ORG = " + codOrganizacion + ")"
                        + " order by GRPUOR_NOMBRE";

                log.debug("Query: " + sql + ", " + uors);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    final GrupoUnidadesOrganicas grupo = new GrupoUnidadesOrganicas(rs.getInt("GRPUOR_COD"),
                            rs.getString("GRPUOR_NOMBRE"));
                    listaReturn.add(grupo);
                }
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error consultando en la tabla A_UOR_GRPUOR " + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return listaReturn;
    }

    public List<UnidadOrganica> getUnidadesOrganicasGrupo(final Connection con, int idGrupoUOR,
            int codOrganizacion) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA getUnidadesOrganicas DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        List<UnidadOrganica> listaReturn = new ArrayList<UnidadOrganica>();
        try {
            String sql = "select UOR_COD, UOR_PAD, UOR_NOM, UOR_ESTADO, UOR_COD_VIS from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOR_COD in (select UOR_COD FROM "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR_GRPUOR,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where UOR_GRPUOR_COD = " + idGrupoUOR + " AND UOR_GRPUOR_ORG = " + codOrganizacion + ")"
                    + " order by UOR_NOM";

            log.debug("Query: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                final UnidadOrganica unidadOrganica = new UnidadOrganica(rs.getInt("UOR_COD"),
                        rs.getInt("UOR_PAD"), rs.getString("UOR_NOM"),
                        rs.getString("UOR_ESTADO"), rs.getString("UOR_COD_VIS"));
                listaReturn.add(unidadOrganica);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error consultando en la tabla A_UOR_GRPUOR " + idGrupoUOR + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return listaReturn;
    }

    public List<Usuario> getUsuariosGrupo(final Connection con, int idGrupoUOR,
            int codOrganizacion) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA cargarUSUsGrupo DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        List<Usuario> listaReturn = new ArrayList<Usuario>();
        try {
            String sql = "select USU_COD, USU_LOG, USU_NOM from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_USU,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where USU_COD in (select USU_COD FROM "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_USU_GRPUOR,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + " where USU_GRPUOR_COD = " + idGrupoUOR + " and USU_GRPUOR_ORG = " + codOrganizacion + ")"
                    + " and USU_BLQ = 0"
                    + " order by USU_NOM";

            log.debug("Query: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                final Usuario usuario = new Usuario(rs.getInt("USU_COD"),
                        rs.getString("USU_LOG"), rs.getString("USU_NOM"));
                listaReturn.add(usuario);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error consultando en la tabla A_USU " + idGrupoUOR + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return listaReturn;
    }

    public boolean existeUOREnOtroGrupo(final Connection con,
            int idGrupo, int idUOR, int codOrganizacion) throws Exception {
        if (log.isInfoEnabled()) {
            log.debug("INICIA existeUOREnOtroGrupoYTieneUsuarios DAO ");
        }
        Statement st = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            String sql = "select UOR_GRPUOR_COD from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide73.TABLA_A_UOR_GRPUOR,
                            ConstantesMeLanbide73.FICHERO_PROPIEDADES)
                    + "  where UOR_COD = " + idUOR + " and UOR_GRPUOR_COD != " + idGrupo
                    + " and UOR_GRPUOR_ORG = " + codOrganizacion;
            log.debug("Query: " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            if (rs.next()) {
                result = true;
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error consultando en la tabla A_UOR_GRPUOR " + e);
        } finally {
            if (log.isInfoEnabled()) {
                log.debug("Procedemos a cerrar el statemet y el resultSet");
            }
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }// try-catch  
        return result;
    }

    private String convertirListaUorsASqlString(final List<UnidadOrganica> uors) {

        if (uors == null || uors.isEmpty()) {
            return "";
        } else if (uors.size() == 1) {
            return "(" + uors.get(0).getUorCod() + ")";
        } else {
            String result = "";
            for (int i = 0; i < uors.size(); i++) {
                if (i == 0) {
                    result += "(" + uors.get(i).getUorCod() + ", ";
                } else if (i < uors.size() - 1) {
                    result += uors.get(i).getUorCod() + ", ";
                } else {
                    result += uors.get(i).getUorCod() + ")";
                }
            }
            return result;
        }
    }
}
