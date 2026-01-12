/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58;
import es.altia.flexia.integracion.moduloexterno.melanbide58.util.MeLanbide58MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.AltaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.BajaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.CausaAltaBajaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.PlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DiscapacitadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.SMIVO;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide58DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide58DAO.class);
    //Instancia
    private static MeLanbide58DAO instance = null;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Constructor
    private MeLanbide58DAO() {
    }

    public static MeLanbide58DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide58DAO.class) {
                instance = new MeLanbide58DAO();
            }
        }
        return instance;
    }

    // ----------------------------------------------------------------------------------------------------------
    // --------------------------  ANEXO A     ------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------   
    // ------------------  ALTAS  -------------------------------------------------------------------------------
    public List<AltaVO> getDatosAlta(String numExp, int codOrganizacion, Connection con) throws Exception {
        log.info("  >>>> Entramos en getDatosAlta de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<AltaVO> lista = new ArrayList<AltaVO>();
        AltaVO alta = new AltaVO();
        try {
            String query = null;
            query = "SELECT A.*,C.DES_NOM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " C"
                    + " ON  DES_COD='CEES' AND ('ALTA'||A.CAUSA=C.DES_VAL_COD)"
                    + " WHERE NUM_EXP ='" + numExp
                    + "' ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                alta = (AltaVO) MeLanbide58MappingUtils.getInstance().map(rs, AltaVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(alta);
                alta = new AltaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Altas ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public List<AltaVO> getRegistroAlta(String numExp, String nif, String numLinea, String apellidos, int codOrganizacion, Connection con) throws Exception {
        log.info("  >>>> Entramos en getRegistroAlta de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<AltaVO> lista = new ArrayList<AltaVO>();
        AltaVO alta = new AltaVO();

        try {
            String query = null;
            query = "SELECT A.*,C.DES_NOM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " C"
                    + " ON  DES_COD='CEES' AND ('ALTA'||A.CAUSA=C.DES_VAL_COD)"
                    + "Where 1=1 "
                    + "And (Upper(A.Apellidos) Like '%'||Nvl(Upper('" + apellidos + "'), '')||'%' Or '" + apellidos + "' Is Null) "
                    + "And (Upper(A.Nif) Like '%'||Nvl(Upper('" + nif + "'), '')||'%' Or '" + nif + "' Is Null) "
                    + "And A.Num_Exp= '" + numExp + "' "
                    + "And a.num_linea = Nvl('" + numLinea + "', a.num_linea) Order By A.Id";

            if (log.isDebugEnabled()) {
                log.debug("sql getRegistro = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                alta = (AltaVO) MeLanbide58MappingUtils.getInstance().map(rs, AltaVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(alta);
                alta = new AltaVO();
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Registros del AnexoA - alta ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public AltaVO getAltaPorID(String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en getAltaPorID de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        AltaVO alta = new AltaVO();
        try {
            String query = "SELECT A.*,C.DES_NOM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " C"
                    + " ON  DES_COD='CEES' AND ('ALTA'||A.CAUSA=C.DES_VAL_COD)"
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                alta = (AltaVO) MeLanbide58MappingUtils.getInstance().mapVers2(rs, AltaVO.class, false);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un alta : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return alta;
    }

    public boolean crearNuevoAlta(AltaVO alta, Connection con) throws Exception {
        log.info("  >>>> Entramos en crearNuevoAlta de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = "";
        String fechaAlta = "";

        try {
            if (alta != null && alta.getFechaAlta() != null && !"".equals(alta.getFechaAlta().toString())) {
                fechaAlta = dateFormat.format(alta.getFechaAlta());
            }

            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + "(ID, NUM_EXP, APELLIDOS, NOMBRE,NUM_LINEA, FECHA_ALTA, NUM_SS, NIF,CAUSA) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.SEQ_MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + ".nextVal"
                    + ", '" + alta.getNumExp()
                    + "', '" + alta.getApellidos()
                    + "', '" + alta.getNombre()
                    + "', " + alta.getNumLinea()
                    + ", TO_DATE('" + fechaAlta + "','dd/mm/yyyy')"
                    + ", '" + alta.getNumSS()
                    + "', '" + alta.getNif()
                    + "', '" + alta.getCausa()
                    + "')";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No Se ha podido guardar un nuevo registro de alta ");
                return false;
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Insertar un nuevo registro de alta" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarAlta(AltaVO datModif, Connection con) throws Exception {
        log.info("  >>>> Entramos en modificarAlta de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        String fechaAlta = "";
        try {
            if (datModif != null && datModif.getFechaAlta() != null && !"".equals(datModif.getFechaAlta().toString())) {
                fechaAlta = dateFormat.format(datModif.getFechaAlta());
            }

            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET APELLIDOS='" + datModif.getApellidos() + "'"
                    + ", NOMBRE='" + datModif.getNombre() + "'"
                    + ", NUM_LINEA=" + datModif.getNumLinea()
                    + ", FECHA_ALTA=TO_DATE('" + fechaAlta + "','dd/mm/yyyy')"
                    + ", NUM_SS='" + datModif.getNumSS() + "'"
                    + ", NIF='" + datModif.getNif() + "'"
                    + ", CAUSA='" + datModif.getCausa() + "'"
                    + " WHERE ID=" + datModif.getId();

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No Se ha podido Modificar un registro de Ata ");
                return false;
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar un registro de alta - " + ex.getMessage());
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

    public int eliminarAlta(String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en eliminarAlta de " + this.getClass().getSimpleName());
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando alta ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /* Metodos de obtención de la lista de altas*/
    public List<CausaAltaBajaVO> getListAltas(String numExpediente, Connection con) throws Exception {
        log.info("  >>>> Entramos en getListAltas de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<CausaAltaBajaVO> listAltas = new ArrayList<CausaAltaBajaVO>();
        CausaAltaBajaVO Alta = new CausaAltaBajaVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " where DES_COD = 'CEES' AND DES_VAL_COD like 'ALTA%' and DES_VAL_ESTADO='A' order by DES_NOM ";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Alta = (CausaAltaBajaVO) MeLanbide58MappingUtils.getInstance().map(rs, CausaAltaBajaVO.class);
                listAltas.add(Alta);
                Alta = new CausaAltaBajaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la Lista de Altas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listAltas;
    }

    // ------------------  BAJAS  -------------------------------------------------------------------------------
    public List<BajaVO> getDatosBaja(String numExp, int codOrganizacion, Connection con) throws Exception {
        log.info("  >>>> Entramos en getDatosBaja de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<BajaVO> lista = new ArrayList<BajaVO>();
        BajaVO baja = new BajaVO();
        try {
            String query = null;
            query = "SELECT A.*,C.DES_NOM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " C"
                    + " ON  DES_COD='CEES' AND ('BAJA'||A.CAUSA=C.DES_VAL_COD)"
                    + " WHERE NUM_EXP ='" + numExp
                    + "' ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                baja = (BajaVO) MeLanbide58MappingUtils.getInstance().map(rs, BajaVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(baja);
                baja = new BajaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Bajas ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public List<BajaVO> getRegistroBaja(String numExp, String nif, String numLinea, String apellidos, int codOrganizacion, Connection con) throws Exception {
        log.info("  >>>> Entramos en getRegistroBaja de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<BajaVO> lista = new ArrayList<BajaVO>();
        BajaVO baja = new BajaVO();
        try {
            String query = null;
            query = "SELECT A.*,C.DES_NOM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " C"
                    + " ON  DES_COD='CEES' AND ('BAJA'||A.CAUSA=C.DES_VAL_COD)"
                    + " Where 1=1 "
                    + " And (Upper(A.Apellidos) Like '%'||Nvl(Upper('" + apellidos + "'), '')||'%' Or '" + apellidos + "' Is Null) "
                    + " And (Upper(A.Nif) Like '%'||Nvl(Upper('" + nif + "'), '')||'%' Or '" + nif + "' Is Null) "
                    + " And A.Num_Exp= '" + numExp + "' "
                    + " And a.num_linea = Nvl('" + numLinea + "', a.num_linea) Order By A.Id";

            if (log.isDebugEnabled()) {
                log.debug("sql getRegistro = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                baja = (BajaVO) MeLanbide58MappingUtils.getInstance().map(rs, BajaVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(baja);
                baja = new BajaVO();
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Registros del AnexoA - baja ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public BajaVO getBajaPorID(String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en getBajaPorID de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        BajaVO baja = new BajaVO();
        try {
            String query = "SELECT A.*,C.DES_NOM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " A"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " C"
                    + " ON  DES_COD='CEES' AND ('BAJA'||A.CAUSA=C.DES_VAL_COD)"
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                baja = (BajaVO) MeLanbide58MappingUtils.getInstance().mapVers2(rs, BajaVO.class, false);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un baja : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return baja;
    }

    public boolean crearNuevoBaja(BajaVO baja, Connection con) throws Exception {
        log.info("  >>>> Entramos en crearNuevoBaja de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = "";
        String fechaBaja = "";

        try {
            if (baja != null && baja.getFechaBaja() != null && !"".equals(baja.getFechaBaja().toString())) {
                fechaBaja = dateFormat.format(baja.getFechaBaja());
            }

            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + "(ID, NUM_EXP, APELLIDOS, NOMBRE,NUM_LINEA, FECHA_BAJA, NUM_SS, NIF, CAUSA) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.SEQ_MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + ".nextVal"
                    + ", '" + baja.getNumExp()
                    + "', '" + baja.getApellidos()
                    + "', '" + baja.getNombre()
                    + "', " + baja.getNumLinea()
                    + ", TO_DATE('" + fechaBaja + "','dd/mm/yyyy')"
                    + ", '" + baja.getNumSS()
                    + "', '" + baja.getNif()
                    + "', '" + baja.getCausa()
                    + "')";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar un nuevo registro de baja ");
                return false;
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Insertar un nuevo registro de baja" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarBaja(BajaVO datModif, Connection con) throws Exception {
        log.info("  >>>> Entramos en modificarBaja de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = "";
        String fechaBaja = "";
        try {
            if (datModif != null && datModif.getFechaBaja() != null && !"".equals(datModif.getFechaBaja().toString())) {
                fechaBaja = dateFormat.format(datModif.getFechaBaja());
            }

            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET APELLIDOS='" + datModif.getApellidos() + "'"
                    + ", NOMBRE='" + datModif.getNombre() + "'"
                    + ", NUM_LINEA=" + datModif.getNumLinea()
                    + ", FECHA_BAJA=TO_DATE('" + fechaBaja + "','dd/mm/yyyy')"
                    + ", NUM_SS='" + datModif.getNumSS() + "'"
                    + ", NIF='" + datModif.getNif() + "'"
                    + ", CAUSA='" + datModif.getCausa() + "'"
                    + " WHERE ID=" + datModif.getId();

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar un registro de baja - "
                    + datModif != null ? datModif.getId() : "" + " - " + ex);
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
    }

    public int eliminarBaja(String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en eliminarBaja de " + this.getClass().getSimpleName());
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando baja ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /* Metodos de obtención de la lista de bajas*/
    public List<CausaAltaBajaVO> getListBajas(String numExpediente, Connection con) throws Exception {
        log.info("  >>>> Entramos en getListBajas de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<CausaAltaBajaVO> listBajas = new ArrayList<CausaAltaBajaVO>();
        CausaAltaBajaVO Baja = new CausaAltaBajaVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " where DES_COD = 'CEES' AND DES_VAL_COD like 'BAJA%' and DES_VAL_ESTADO='A' order by DES_NOM";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Baja = (CausaAltaBajaVO) MeLanbide58MappingUtils.getInstance().map(rs, CausaAltaBajaVO.class);
                listBajas.add(Baja);
                Baja = new CausaAltaBajaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Lista Bajas", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listBajas;
    }

    // ----------------------------------------------------------------------------------------------------------
    // --------------------------  ANEXO B -  SMI    ------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
    public List<SMIVO> getDatosAnexoB(String numExp, int codOrganizacion, Connection con) throws Exception {
        log.info("  >>>> Entramos en getDatosSMI de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<SMIVO> lista = new ArrayList<SMIVO>();
        SMIVO smi = new SMIVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY IDSMI";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                smi = (SMIVO) MeLanbide58MappingUtils.getInstance().mapVers2(rs, SMIVO.class, false);
                lista.add(smi);
                smi = new SMIVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando smi", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

   
    public List<SMIVO> getRegistroSMI(String numExp, String numLinea, String apellidos, String nif, Connection con) throws Exception {
        log.info("  >>>> Entramos en getRegistroSMI de " + this.getClass().getSimpleName());
        Statement st1 = null;
        ResultSet rs1 = null;
        Statement st = null;
        ResultSet rs = null;
        List<SMIVO> lista = new ArrayList<SMIVO>();
        SMIVO smi = new SMIVO();
        if (numLinea.trim().isEmpty() && apellidos.trim().isEmpty() && nif.isEmpty()) {
            try {
                String query = null;
//                query = "Select distinct a.*,e.des_nom "
                query = "Select distinct a.*,e.des_nom DES_CAUSA"
                        + " From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a,"
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "  e"
                        + " where a.Num_Exp = '" + numExp + "' "
                        + " and a.causa = e.des_val_cod(+) "
                        + " and e.DES_COD (+)='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_CAUSA_INCIDENCIA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "'"
                        + " Order By a.Idsmi";
                if (log.isDebugEnabled()) {
                    log.debug("sql getRegistro = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);

                while (rs.next()) {
                    smi = (SMIVO) MeLanbide58MappingUtils.getInstance().map(rs, SMIVO.class);
                    //Cargamos el el request los valores  de los desplegables
                    lista.add(smi);
                    smi = new SMIVO();
                }

            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando Registros del SMI ", ex);
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
            try {
                String query1 = null;
                String query = null;
                String variable = "";
                query1 = "Select "
                        + "Selectsmiceesc('" + numExp + "', '" + numLinea + "', '" + apellidos + "') filasFiltro from dual";

                if (log.isDebugEnabled()) {
                    log.debug("sql1  = " + query1);
                }

                st1 = con.createStatement();
                rs1 = st1.executeQuery(query1);
                while (rs1.next()) {
                    variable = rs1.getString("filasFiltro");
                }
                query = "Select distinct a.*,e.des_nom DES_CAUSA"
                        + " From  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a,"
                        + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "  e "
                        + " Where Instr(('" + variable + "'), ',' || a.Idsmi || ',') > 0 "
                        + " And a.Num_Exp = '" + numExp + "' "
                        + " and a.causa = e.des_val_cod(+) "
                        + " and e.DES_COD   (+)='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_CAUSA_INCIDENCIA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "'"
                        + " Order By  a.Idsmi";

                if (log.isDebugEnabled()) {
                    log.debug("sql getRegistro = " + query);
                }
                st = con.createStatement();
                rs = st.executeQuery(query);
                while (rs.next()) {
                    smi = (SMIVO) MeLanbide58MappingUtils.getInstance().map(rs, SMIVO.class);
                    //Cargamos el el request los valores  de los desplegables
                    lista.add(smi);
                    smi = new SMIVO();
                }

            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando Registros del SMI ", ex);
                throw new Exception(ex);
            } finally {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st1 != null) {
                    st1.close();
                }
                if (rs1 != null) {
                    rs1.close();
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
        }//fin else
        return lista;
    }

    public List<SMIVO> getRegistrosSMIporNIF(String numExp, String nif, Connection con) throws Exception {
        log.info(">>>> ENTRA en getRegistrosSMIporNIF");
        Statement st = null;
        ResultSet rs = null;
        List<SMIVO> lista = new ArrayList<SMIVO>();
        SMIVO smi = new SMIVO();
        try {
            String query = null;
            query = "select idsmi, por_jornada from " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " where num_exp='" + numExp + "'"
                    + " and nif='" + nif + "'";
            log.debug("Query= " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                smi.setNif(nif);
                smi.setNumExp(numExp);
                smi.setId(rs.getInt("IDSM"));
                smi.setPorcJornada(rs.getDouble("POR_JORNADA"));
                lista.add(smi);
                smi = new SMIVO();
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Registros AnexoB x NIF", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public List<SMIVO> getRegistroSubImpInc(String numExp, String numLinea, String apellidos, Connection con) throws Exception {
        log.info("  >>>> Entramos en getRegistroSubImpInc de " + this.getClass().getSimpleName());
        Statement st1 = null;
        ResultSet rs1 = null;
        Statement st = null;
        ResultSet rs = null;
        List<SMIVO> lista = new ArrayList<SMIVO>();
        SMIVO smi = new SMIVO();

        try {
            String query1 = null;
            String query = null;
            String variable = "";
            query1 = "Select "
                    + "SelectsLineasErroneas('" + numExp + "') filasErr from dual";

            if (log.isDebugEnabled()) {
                log.debug("sql1  = " + query1);
            }
            st1 = con.createStatement();
            rs1 = st1.executeQuery(query1);

            while (rs1.next()) {
                variable = rs1.getString("filasErr");
            }

            query = "Select a.*, e.des_nom DES_CAUSA "
                    + " From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a, "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " e"
                    + " Where Instr('" + variable + "', ',' || a.Idsmi || ',') > 0 "
                    + " and a.causa = e.des_val_cod(+) "
                    + " And a.Num_Exp = '" + numExp + "' "
                    + " and e.DES_COD (+)='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_CAUSA_INCIDENCIA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "'"
                    + " Order By a.Idsmi";

            if (log.isDebugEnabled()) {
                log.debug("sql getRegistroSubImpInc = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                smi = (SMIVO) MeLanbide58MappingUtils.getInstance().map(rs, SMIVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(smi);
                smi = new SMIVO();
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Registros de Subvención Impporte Incorrecto ", ex);
            throw new Exception(ex);
        } finally {
            if (st1 != null) {
                st1.close();
            }
            if (rs1 != null) {
                rs1.close();
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public SMIVO getSMIPorID(String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en getSMIPorID de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        SMIVO smi = new SMIVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE IDSMI=" + (id != null && !id.isEmpty() ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                smi = (SMIVO) MeLanbide58MappingUtils.getInstance().mapVers2(rs, SMIVO.class, false);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un smi : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return smi;
    }

    public Integer crearNuevoSMI(SMIVO smi, Connection con) throws Exception {
        log.info("  >>>> Entramos en crearNuevoSMI de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = "";
        try {
            String fecha = "";
            if (smi != null && smi.getFecha() != null && !"".equalsIgnoreCase(smi.getFecha().toString())) {
                fecha = dateFormat.format(smi.getFecha());
            }
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide58.SEQ_MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + "(IDSMI,NUM_EXP,APELLIDOS,NOMBRE,NUM_LINEA,NUM_DIAS_SIN, NUM_DIAS, CAUSA, IMPORTE, FECHA, POR_JORNADA, POR_REDUCCION, OBSERVACIONES,NIF) "
                    + " VALUES (" + id
                    + ", '" + smi.getNumExp()
                    + "', '" + smi.getApellidos()
                    + "', '" + smi.getNombre()
                    + "', " + smi.getNumLinea()
                    + ", " + smi.getNumDiasSinIncidencias()
                    + ", " + smi.getNumDiasIncidencia()
                    + ",'" + smi.getCausaIncidencia()
                    + "', " + smi.getImporteSolicitado()
                    + ", TO_DATE('" + fecha + "','dd/mm/yyyy')"
                    + "," + smi.getPorcJornada()
                    + "," + smi.getPorcReduccion()
                    + ", '" + smi.getObservaciones() + "'"
                    + ", '" + smi.getNif() + "'"
                    + ")";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return id;
            } else {
                log.debug("No Se ha podido guardar un nuevo registro de smi ");
                return null;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error al Insertar un nuevo registro de smi" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarSMI(SMIVO datModif, Connection con) throws Exception {
        log.info("  >>>> Entramos en modificarSMI de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = "";
        try {
            String fecha = "";
            if (datModif != null && datModif.getFecha() != null && !"".equals(datModif.getFecha().toString())) {
                fecha = dateFormat.format(datModif.getFecha());
            }

            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET APELLIDOS='" + datModif.getApellidos() + "'"
                    + ", NOMBRE='" + datModif.getNombre() + "'"
                    + ", CAUSA='" + datModif.getCausaIncidencia() + "'"
                    + ", NUM_LINEA=" + datModif.getNumLinea()
                    + ", NUM_DIAS_SIN=" + datModif.getNumDiasSinIncidencias()
                    + ", NUM_DIAS=" + datModif.getNumDiasIncidencia()
                    + ", IMPORTE=" + datModif.getImporteSolicitado()
                    + ", FECHA=TO_DATE('" + fecha + "','dd/mm/yyyy')"
                    + ", POR_JORNADA=" + datModif.getPorcJornada()
                    + ", POR_REDUCCION=" + datModif.getPorcReduccion()
                    + ", OBSERVACIONES='" + datModif.getObservaciones() + "'"
                    // + ", NIF='" + datModif.getNif() + "'"
                    + " WHERE IDSMI=" + datModif.getId();

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar un registro de smi - "
                    + (datModif.getId() != null ? datModif.getId() : "") + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public int eliminarSMI(String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en eliminarSMI de " + this.getClass().getSimpleName());
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE IDSMI=" + (id != null && !id.isEmpty() ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando smi ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public String obtenerAnio(String numExp, Connection con) throws Exception {
        log.info("  >>>> Entramos en obtenerAnio de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        String anioSMI = "";
        try {
            String query = null;
            query = "select * from  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_VALOR_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + "where anio in(select tde_valor from " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + "where TDE_NUM='" + numExp + "' "
                    + "and TDE_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ANIOAYUDA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "')";

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
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    public String recalcularSMI(String numExp, String numLinea, String anio, Connection con) throws Exception {
        log.info("  >>>> Entramos en recalcularSMI de " + this.getClass().getSimpleName());
        CallableStatement st = null;
        String rdo = "";
        try {
            String query = null;
            query = "{?=call recalculoSubvCEESC(?,?,?)}";	//p_numExp, p_numLinea, p_anio_sol
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
                log.debug("Exp: " + numExp + " - Linea: " + numLinea + " - Año: " + anio);
            }
            st = con.prepareCall(query);
            st.registerOutParameter(1, Types.VARCHAR);
            st.setString(2, numExp);
            st.setString(3, numLinea);
            st.setString(4, anio);
            st.executeQuery();
            rdo = st.getString(1);

            return rdo;
        } catch (SQLException ex) {
            log.error("Se ha producido un error recalculando el SMI", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public ArrayList getSmiMesDia(String numExpediente, Integer codOrg, Connection con) throws Exception {
        log.info("  >>>> Entramos en getSmiMesDia de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        ArrayList smiMesDia = new ArrayList();
        try {
            String query = null;
            query = "select imp_mensual_smi, imp_diario_smi "
                    + "from  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_VALOR_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " where anio in (select tde_valor from " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " where tde_num='" + numExpediente + "' and  tde_mun=" + codOrg + " and tde_cod ='"
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ANIOAYUDA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "')";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                smiMesDia.add(rs.getString("imp_mensual_smi"));
                smiMesDia.add(rs.getString("imp_diario_smi"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el Salario Mínimo Interprofesional(SMI): ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return smiMesDia;
    }

    public ArrayList getTotalSubvencion(String numExp, Connection con) throws Exception {
        log.info("  >>>> Entramos en getTotalSubvencion de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        ArrayList totalS = new ArrayList();
        try {
            String query = null;
            query = "Select Nvl(Sum(Nvl(Importe, 0)),0) as IMPORTE, Nvl(Sum(Nvl(Importe_Calculado, 0)),0) AS IMPORTECALCULADO "
                    + " From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " Where Num_Exp = '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                totalS.add(rs.getString("IMPORTE"));
                totalS.add(rs.getString("IMPORTECALCULADO"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el total de la Subvención ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return totalS;
    }

    /*Devuelve los datos del padre que está asociado al hijo, pasandole el id del hijo*/
    public SMIVO getDatosPadrePorId(String numExp, String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en getDatosPadrePorId de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        SMIVO padre = new SMIVO();
        try {
            String query = null;
            query = "Select a.*,e.des_nom DES_CAUSA From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " A,"
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " e "
                    + " Where A.Num_Exp ='" + numExp + "'"
                    + " And A.causa = e.des_val_cod(+) "
                    + " And A.Idsmi < " + id
                    + " And A.Num_Linea Is Not Null"
                    + " And A.Num_Linea = (Select Max(B.Num_Linea)"
                    + " From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " B"
                    + " Where B.Num_Exp = A.Num_Exp"
                    + " And B.Idsmi < " + id
                    + " And B.Num_Linea Is Not Null)";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                padre = (SMIVO) MeLanbide58MappingUtils.getInstance().map(rs, SMIVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Altas ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return padre;
    }

    /*Devuelve el numero de linea del padre*/
    public Integer getNumLineaPadre(String numExp, String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en getNumLineaPadre de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        int numLineaPadre = 0;
        try {
            String query = null;
            query = "Select Max(Num_Linea) maxNumLinea From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " A"
                    + " Where A.Num_Exp ='" + numExp + "'"
                    + " And A.Idsmi < " + id
                    + " And A.Num_Linea Is Not Null";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                numLineaPadre = rs.getInt("maxNumLinea");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el numLinea del padre ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return numLineaPadre;
    }

    /*Devuelve los datos del padre que está asociado al hijo, pasandole el id del hijo*/
    public Integer getLineaPadrePorID(String numExp, String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en getLineaPadrePorID de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        Integer lineaPadre = null;
        try {
            String query = null;
            query = "Select Max(Num_Linea) From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " Where Num_Exp ='" + numExp + "'"
                    + " And Idsmi < " + id
                    + " And Num_Linea Is Not Null";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                lineaPadre = rs.getInt(1);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la lineaPadre por ID ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lineaPadre;
    }

    public String getNifSMIPorId(String numExp, String id, Connection con) throws Exception {
        log.info("Entra en getNifSMIPorId - DAO");
        Statement st = null;
        ResultSet rs = null;
        String nif = null;
        try {
            String query = null;
            query = "SELECT NIF FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "'"
                    + " AND IDSMI=" + id;
            log.debug(query);
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                nif = rs.getString("NIF");
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el NIF por ID ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return nif;
    }

    public boolean copiaImportesSMI(String numExp, Connection con) throws Exception {
        log.info("  >>>> Entramos en copiaImportesSMI de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET IMPORTE_LANBIDE=IMPORTE_CALCULADO "
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " AND IMPORTE_LANBIDE IS  NULL"
                    + " OR IMPORTE_LANBIDE=0";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error al copiar Importes SMI del expediente " + numExp + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean actualizaJornadaB(String numExp, String apellidos, String dni_nif, Double porcJornada, String porcOriginal, Connection con) throws Exception {
        log.info("Entra en actualizaJornadaB - DAO ");
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET POR_JORNADA=" + porcJornada
                    + " WHERE NUM_EXP='" + numExp + "'"
                    + " AND NIF='" + dni_nif + "'"
                    + " AND POR_JORNADA=" + porcOriginal;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error actualizando % jornada en Anexo B " + numExp + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public Integer getLineaPadreBporDNI(String numExp, String dni_nif, Connection con) throws Exception {
        log.info("Entra en getLineaPadreBporDNI - Manager");
        Statement st = null;
        ResultSet rs = null;
        Integer lineaPadre = null;
        try {
            String query = null;
            query = "Select Max(Num_Linea) From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " Where Num_Exp ='" + numExp + "'"
                    + " And nif ='" + dni_nif + "'"
                    + " And Num_Linea Is Not Null";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                lineaPadre = rs.getInt(1);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la lineaPadre por ID ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lineaPadre;
    }

    public Integer getLineaPadreBValidadaDNI(String numExp, String dni_nif, Connection con) throws Exception {
        log.info("Entra en getLineaPadreBValidadaDNI - Manager");
        Statement st = null;
        ResultSet rs = null;
        Integer lineaPadre = null;
        try {
            String query = null;
            query = "Select Max(a.Num_Linea) From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a"
                    + " join " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " b on b.NUM_EXP=a.NUM_EXP"
                    + " AND b.nif='" + dni_nif + "' "
                    + " AND  b.DATOS_PENDIENTES !='S'"
                    + " OR b.datos_pendientes IS NULL"
                    + " Where a.Num_Exp ='" + numExp + "'"
                    + " And a.nif ='" + dni_nif + "'"
                    + " And a.Num_Linea Is Not Null";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                lineaPadre = rs.getInt(1);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la lineaPadre por ID ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lineaPadre;
    }

    public Integer getLineaPadreValidadaID(String numExp, String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en getLineaPadreValidadaID de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        Integer lineaPadre = null;
        try {
            String query = null;
            query = "Select Max(a.Num_Linea) From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a"
                    + " join " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " b on b.nif=a.nif "
                    + " AND  b.DATOS_PENDIENTES!='S' AND b.NUM_EXP=a.NUM_EXP"
                    + " Where a.Num_Exp ='" + numExp + "'"
                    + " And a.Idsmi < " + id
                    + " And a.Num_Linea Is Not Null";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                lineaPadre = rs.getInt(1);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la lineaPadre por ID ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lineaPadre;
    }

    public boolean borrarImportesSMI(String numExp, String dni, Connection con) throws Exception {
        log.info("  >>>> Entramos en borrarImportesSMI de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET IMPORTE_LANBIDE=NULL, IMPORTE_CALCULADO=NULL"
                    + " WHERE NUM_EXP='" + numExp + "'"
                    + " AND NIF='" + dni + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error eliminando los importes en Anexo B " + numExp + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------
    // --------------------------  ANEXO C -  PLANTILLA    ------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
    /* para la tabla/JSP */
    public List<PlantillaVO> getDatosControlAcceso(String numExp, int codOrganizacion, Connection con) throws Exception {
        log.info("  >>>> Entramos en getDatosControlAcceso de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        PlantillaVO plantilla = new PlantillaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
//                    + " ORDER BY NIF,ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                plantilla = (PlantillaVO) MeLanbide58MappingUtils.getInstance().map(rs, PlantillaVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(plantilla);
                plantilla = new PlantillaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Controles Accesos ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /* para la busqueda */
    public List<PlantillaVO> getRegistroAcceso(String numExp, String nif, String numLinea, String apellidos, Connection con) throws Exception {
        log.info("  >>>> Entramos en getRegistroAcceso de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        PlantillaVO plantilla = new PlantillaVO();
        try {
            String query = null;
            query = "Select * From " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " A "
                    + "Where 1=1 "
                    + "And (Upper(A.Apellidos) Like '%'||Nvl(Upper('" + apellidos + "'), '')||'%' Or '" + apellidos + "' Is Null) "
                    + "And (Upper(A.Nif) Like '%'||Nvl(Upper('" + nif + "'), '')||'%' Or '" + nif + "' Is Null) "
                    + "And A.Num_Exp= '" + numExp + "' "
                    + "And a.num_linea = Nvl('" + numLinea + "', a.num_linea) Order By A.Id";
            if (log.isDebugEnabled()) {
                log.debug("sql getRegistro= " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                plantilla = (PlantillaVO) MeLanbide58MappingUtils.getInstance().map(rs, PlantillaVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(plantilla);
                plantilla = new PlantillaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando registros del Controles Accesos ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public PlantillaVO getControlAccesoPorID(String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en getControlAccesoPorID de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        PlantillaVO controlAcceso = new PlantillaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (PlantillaVO) MeLanbide58MappingUtils.getInstance().map(rs, PlantillaVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un control de acceso : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return controlAcceso;
    }

    /* para la actualizar Severa */
    public PlantillaVO getControlAccesoPorDNI(String dni, String numExp, Connection con) throws Exception {
        log.info("  >>>> Entramos en getControlAccesoPorDNI de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        PlantillaVO controlAcceso = new PlantillaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE nif='" + dni + "' "
                    + " and num_exp='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (PlantillaVO) MeLanbide58MappingUtils.getInstance().map(rs, PlantillaVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un control de acceso : " + dni, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return controlAcceso;
    }

    /* para la actualizar Severa */
    public List<PlantillaVO> getPlantillaPorDni(String numExp, Connection con) throws Exception {
        log.info("  >>>> Entramos en getPlantillaPorDni de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        PlantillaVO controlAcceso = new PlantillaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a"
                    + " WHERE a.NUM_EXP ='" + numExp + "'"
                    + " ORDER BY a.NIF";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (PlantillaVO) MeLanbide58MappingUtils.getInstance().map(rs, PlantillaVO.class);
                lista.add(controlAcceso);
                controlAcceso = new PlantillaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Controles Accesos ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /* para la Procesar Anexo C */
    public List<PlantillaVO> getPlantillaSinProcesar(String numExp, Connection con) throws Exception {
        log.info("  >>>> Entramos en getPlantillaSinProcesar de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        PlantillaVO controlAcceso = new PlantillaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a"
                    + " WHERE a.NUM_EXP ='" + numExp + "'"
                    + " AND a.nif NOT IN  (select nif from " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE num_exp ='" + numExp + "')"
                    + " AND a.nif NOT IN  (select nif from " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE num_exp ='" + numExp + "')"
                    + ""
                    // + " AND datos_pendientes IS NULL"
                    + " ORDER BY a.NIF";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (PlantillaVO) MeLanbide58MappingUtils.getInstance().map(rs, PlantillaVO.class);
                lista.add(controlAcceso);
                controlAcceso = new PlantillaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Controles Accesos ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public int eliminarAcceso(String id, Connection con) throws Exception {
        log.info("  >>>> Entramos en eliminarAcceso de " + this.getClass().getSimpleName());
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.isEmpty() ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Control de Acceso ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoAcceso(PlantillaVO nuevoAcceso, Connection con) throws Exception {
        log.info("Entra en crearNuevoAcceso - DAO ");
        Statement st = null;
        String query = "";
        String fechaNac = "";
        String fechaIni = "";

        if (nuevoAcceso != null && nuevoAcceso.getFecNaci() != null && !nuevoAcceso.getFecNaci().toString().isEmpty()) {
            fechaNac = dateFormat.format(nuevoAcceso.getFecNaci());
        }
        if (nuevoAcceso != null && nuevoAcceso.getFecCertificado() != null && !nuevoAcceso.getFecCertificado().toString().isEmpty()) {
            fechaIni = dateFormat.format(nuevoAcceso.getFecCertificado());
        }
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide58.SEQ_MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,APELLIDOS,NOMBRE,NUM_LINEA,SEXO,NIF, FEC_NACI, NUM_SS, FECHA_CERT, GRADO_DISC, TIPO_DISC, DISC_SEVERA_EMP, DISC_SEVERA_LAN, COD_CONTRATO) "
                    + " VALUES (" + id
                    + ", '" + nuevoAcceso.getNumExp() + "'"
                    + ", '" + nuevoAcceso.getApellidos() + "'"
                    + ", '" + nuevoAcceso.getNombre() + "'"
                    + ", " + nuevoAcceso.getNumLinea()
                    + ", '" + nuevoAcceso.getSexo() + "'"
                    + ", '" + nuevoAcceso.getNif_Dni()
                    + "' , TO_DATE('" + fechaNac + "','dd/mm/yyyy')"
                    + ", '" + nuevoAcceso.getNumSS() + "'"
                    + ", TO_DATE('" + fechaIni + "','dd/mm/yyyy')"
                    + ", " + (nuevoAcceso.getGrado()!= null && !nuevoAcceso.getGrado().toString().isEmpty() ? nuevoAcceso.getGrado() : "null") 
                    + ",'" + nuevoAcceso.getTipoDis() + "'"
                    + ",'" + nuevoAcceso.getDiscSevera() + "'"
                    + ",'" + nuevoAcceso.getDiscValidada() + "'"
                    + ",'" + nuevoAcceso.getCodContrato() + "'"
//                    + "," + nuevoAcceso.getPorcJornada()
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar un nuevo registro de Plantilla ");
                return false;
            }
        } catch (Exception ex) {
            //opeCorrecta = false;
            log.error("Se ha producido un error al Insertar un nuevo registro de Plantilla" + ex.getMessage());
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public int modificarAccesosXpersona(PlantillaVO datModif, String numExp, Connection con) throws Exception {
        log.info("Entra en modificarAccesosXpersona - DAO ");
        Statement st = null;
        String query = "";
        String fechaNac = "";
        String fechaIni = "";

        if (datModif != null && datModif.getFecNaci() != null && !datModif.getFecNaci().toString().isEmpty()) {
            fechaNac = dateFormat.format(datModif.getFecNaci());
        }
        if (datModif != null && datModif.getFecCertificado() != null && !datModif.getFecCertificado().toString().isEmpty()) {
            fechaIni = dateFormat.format(datModif.getFecCertificado());
        }

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET APELLIDOS='" + datModif.getApellidos() + "'"
                    + ", NOMBRE='" + datModif.getNombre() + "'"
                    + ", SEXO='" + datModif.getSexo() + "'"
                    //    + ", NIF='" + datModif.getNif_Dni() + "'"
                    + ", FEC_NACI=TO_DATE('" + fechaNac + "','dd/mm/yyyy')"
                    + ", NUM_SS='" + datModif.getNumSS() + "'"
                    + ", FECHA_CERT=TO_DATE('" + fechaIni + "','dd/mm/yyyy')"
                    + ", TIPO_DISC='" + datModif.getTipoDis() + "'";
            if (!datModif.getTipoDis().equalsIgnoreCase("P") && !datModif.getTipoDis().equalsIgnoreCase("PA") && !datModif.getTipoDis().equalsIgnoreCase("PG") && !datModif.getTipoDis().equalsIgnoreCase("PT")) {
                query = query + ", GRADO_DISC=" + (datModif.getGrado() != null && !datModif.getGrado().toString().isEmpty() ? datModif.getGrado() : "") + "";
            }
            query = query + ", DISC_SEVERA_EMP='" + datModif.getDiscSevera() + "'"
                    + ", DISC_SEVERA_LAN='" + datModif.getDiscValidada() + "'"
                    + ", COD_CONTRATO='" + datModif.getCodContrato() + "'"
                    + " WHERE NIF='" + datModif.getNif_Dni() + "'"
                    + " AND NUM_EXP='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar un registro de Plantilla - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarAccesoSeleccionado(PlantillaVO datModif, Connection con) throws Exception {
        log.info("Entra en modificarAccesoSeleccionado - DAO ");
        Statement st = null;
        String query = "";
        String fechaNac = "";
        String fechaIni = "";
        if (datModif != null && datModif.getFecNaci() != null && !datModif.getFecNaci().toString().isEmpty()) {
            fechaNac = dateFormat.format(datModif.getFecNaci());
        }
        if (datModif != null && datModif.getFecCertificado() != null && !datModif.getFecCertificado().toString().isEmpty()) {
            fechaIni = dateFormat.format(datModif.getFecCertificado());
        }

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET APELLIDOS='" + datModif.getApellidos() + "'"
                    + ", NOMBRE='" + datModif.getNombre() + "'"
                    + ", NUM_LINEA=" + datModif.getNumLinea()
                    + ", SEXO='" + datModif.getSexo() + "'"
                    //  + ", NIF='" + datModif.getNif_Dni() + "'"
                    + ", FEC_NACI=TO_DATE('" + fechaNac + "','dd/mm/yyyy')"
                    + ", NUM_SS='" + datModif.getNumSS() + "'"
                    + ", FECHA_CERT=TO_DATE('" + fechaIni + "','dd/mm/yyyy')"
                    + ", TIPO_DISC='" + datModif.getTipoDis() + "'"
                    + ", DISC_SEVERA_EMP='" + datModif.getDiscSevera() + "'"
                    + ", DISC_SEVERA_LAN='" + datModif.getDiscValidada() + "'"
                    + ", COD_CONTRATO='" + datModif.getCodContrato() + "'"
//                    + ", POR_JORNADA=" + datModif.getPorcJornada()
                    ;
            if (!datModif.getTipoDis().equalsIgnoreCase("P") && !datModif.getTipoDis().equalsIgnoreCase("PA") && !datModif.getTipoDis().equalsIgnoreCase("PG") && !datModif.getTipoDis().equalsIgnoreCase("PT")) {
                query = query + ", GRADO_DISC=" + (datModif.getGrado() != null && !datModif.getGrado().toString().isEmpty() ? datModif.getGrado() : "") + "";
            }
            query = query + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.debug("Se ha producido un error al Modificar un registro de Plantilla - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

// Funciones comunes
    private int recogerIDInsertar(String sequence, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            String query = "SELECT " + sequence + ".NextVal AS PROXID FROM DUAL ";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("PROXID");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando El numero de ID para insercion al llamar la sequencia " + sequence + " : ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return id;
    }

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegabe = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "' and DES_VAL_ESTADO='A' order by DES_NOM";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegabe = (DesplegableAdmonLocalVO) MeLanbide58MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegabe);
                valoresDesplegabe = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un valores de un desplegable : " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public void grabarImporteResolCEESC(int codOrganizacion, int codTramite, String numExpediente, Connection con, String importeFinal) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        String query = "";
        log.debug("codigo Organizacion = " + codOrganizacion);
        log.debug("codigo codTramite = " + codTramite);

        try {
            //RECOGEMOS EL AÑO DEL EXPEDIENTE RECIBIDO
            int numero = revisarcampo(numExpediente, con);
            int ano = getAnio(numExpediente, con);
            if (numero > 0) {
                query = "UPDATE e_tnu SET TNU_VALOR=" + importeFinal + " WHERE TNU_NUM='" + numExpediente + "' and TNU_COD='IMPRESOLUCION' and tnu_eje=" + ano + "";
            } else {
                query = "INSERT INTO E_TNU VALUES (" + codOrganizacion + "," + ano + ",'" + numExpediente + "','IMPRESOLUCION'," + importeFinal + ")";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            st.executeUpdate(query);

        } catch (Exception ex) {
            log.error("Se ha producido un error algrabar el importe CEESC " + " - " + ex.getMessage() + ex);
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

    private int getAnio(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int numero = 0;
        try {
            String query = null;
            query = "select ext_eje from E_EXT where EXT_NUM='" + numExpediente + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                numero = rs.getInt("ext_eje");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el ejercicio de e_ext - ", ex);
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
        return numero;
    }

    private int revisarcampo(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int numero = 0;
        try {
            String query = null;
            query = "SELECT count(*)as numero FROM E_TNU WHERE TNU_NUM='" + numExpediente + "' and tnu_cod='IMPRESOLUCION'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                numero = rs.getInt("numero");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la Lista de Altas", ex);
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
        return numero;
    }

    // -------------------------------------------------------------
    // ---------------------   PERSONAS DISCP   --------------------
    // -------------------------------------------------------------    
    public boolean actualizaJornadaC(String numExp, String dni_nif, Double porcJornada, String porcOriginal, Connection con) throws Exception {
        log.info("Entra en actualizaJornadaC - DAO ");
        Statement st = null;
        String query = "";
        try {
            /* query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET POR_JORNADA=" + porcJornada
                    + " WHERE NUM_EXP='" + numExp + "'"
                    + " AND APELLIDOS='" + apellidos + "'"
                    + " AND NIF='" + dni_nif + "'"
                    + " And Num_Linea Is Not Null"; */
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET POR_JORNADA=" + porcJornada
                    + " WHERE NUM_EXP='" + numExp + "'"
                    + " AND NIF='" + dni_nif + "'"
                    + " AND POR_JORNADA=" + porcOriginal;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error actualizando % jornada en Anexo B " + numExp + " - " + ex);
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

    public Boolean actualizaDatoDiscSevera(String numExp, int id, String dni, String severa, Connection con) throws Exception {
        log.debug("Entra en actualizaDatoDiscSevera - DAO ");
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET DISC_SEVERA_EMP='" + severa + "'"
                    + " WHERE NUM_EXP='" + numExp + "'"
                    + " AND NIF='" + dni + "'"
                    + " AND id=" + id;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error actualizando Discapacidad Severa  " + numExp + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean actualizaDiscValidada(String numExp, String dni, String validada, Connection con) throws SQLException, Exception {
        log.info("Entramos en actualizaDiscValidada de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET DISC_SEVERA_LAN='" + validada + "'"
                    + " WHERE NIF='" + dni + "'"
                    + " AND NUM_EXP='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error actualizando Discapacidad Severa  " + numExp + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean actualizaDatosIncompletos(String numExp, String dni, String pendiente, Connection con) throws SQLException, Exception {
        log.info("Entramos en actualizaDatosIncompletos de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET DATOS_PENDIENTES='" + pendiente + "'"
                    + " WHERE upper((TRIM(TRANSLATE(nif, ',/./-', ' '))))=UPPER((TRIM(TRANSLATE('" + dni + "', ',/./-', ' '))))"
                    + " AND NUM_EXP='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error actualizando DATOS_PENDIENTES  " + numExp + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public int getMesAyuda(int codOrganizacion, String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int mes = 0;
        String query = null;
        try {
            query = "SELECT tde_valor FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE tde_mun=" + codOrganizacion
                    + " AND tde_num='" + numExp + "'"
                    + " AND tde_cod='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_MES_AYUDA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                mes = rs.getInt("TDE_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el mes de la ayuda", ex);
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
        return mes;
    }

    public int getAnioAyuda(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int anio = 0;
        String query = null;
        try {
            query = "SELECT tde_valor FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE tde_num='" + numExp + "'"
                    + " AND tde_cod='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ANIOAYUDA, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                anio = rs.getInt("TDE_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el año de la ayuda", ex);
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
        return anio;
    }

    public ArrayList getDniABajas(String numExp, Connection con) throws Exception {
        log.info("Entramos en getDniABajas de " + this.getClass().getSimpleName());
        ArrayList<String> lista = new ArrayList<String>();
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT nif FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE num_exp='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                lista.add(rs.getString("NIF"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la lista de NIFs de Anexo A - BAJAS ", ex);
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
        return lista;
    }

    public Date getUltimaFechaAAltas(String numExp, String dni, Connection con) throws Exception {
        log.info("Entramos en getUltimaFechaAAlta de " + this.getClass().getSimpleName());
        Date fecha = null;
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT MAX(FECHA_ALTA) AS FECHA_ALTA FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE num_exp='" + numExp + "'"
                    + " AND nif='" + dni + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                fecha = rs.getDate("FECHA_ALTA");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la fecha de alta de Anexo A - Altas ", ex);
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
        return fecha;
    }

    public Date getUltimaFechaABajas(String numExp, String dni, Connection con) throws Exception {
        log.info("Entramos en getUltimaFechaABajas de " + this.getClass().getSimpleName());
        Date fecha = null;
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT MAX(FECHA_BAJA) AS FECHA_BAJA FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE num_exp='" + numExp + "'"
                    + " AND nif='" + dni + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                fecha = rs.getDate("FECHA_BAJA");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la fecha de alta de Anexo A - Bajas ", ex);
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
        return fecha;
    }

    public int existePersonaDisc(String dni, Connection con) throws Exception {
        log.info(">>>> ENTRA en existePersonaDiscp " + this.getClass().getSimpleName());
        int idPersona = 0;
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT MAX(ID) as ID FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE DNI='" + dni + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next() && rs.getObject("ID") != null) {
                idPersona = rs.getInt("ID");
                log.debug("EXISTE - ID:" + idPersona);
            } else {
                log.debug("No existe");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando existePersona en la taba de personas discapacitadas: ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return idPersona;
    }

    public boolean tieneFechaBajaDisc(int idPersona, String dni, Connection con) throws Exception {
        log.info("Entramos en tieneFechaBajaDisc - " + this.getClass().getSimpleName());
        boolean baja = false;
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "SELECT FECH_BAJA FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + idPersona
                    + " AND DNI='" + dni + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                if (rs.getDate("FECH_BAJA") != null) {
                    log.debug("Esta dado de baja");
                    baja = true;
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando FECH_BAJA en la tabla de personas discapacitadas: ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return baja;
    }

    public boolean nuevaPersonaDisc(DiscapacitadoVO persona, Connection con) throws Exception {
        log.info("Entramos en nuevaPersonaDisc - " + this.getClass().getSimpleName());
        Statement st = null;
        String query = "";
        String fechaRes = "";
        String fechaEmi = "";
        String fechaVal = "";
        String fechaBaj = "";
        String severa = "";
        String validez = "";
        String oid = "";
        String certi = "";
        String grado = "";

        if (persona != null && persona.getFechResolucion() != null && !persona.getFechResolucion().toString().isEmpty()) {
            fechaRes = dateFormat.format(persona.getFechResolucion());
        }
        if (persona != null && persona.getFechEmision() != null && !persona.getFechEmision().toString().isEmpty()) {
            fechaEmi = dateFormat.format(persona.getFechEmision());
        }
        fechaVal = dateFormat.format(System.currentTimeMillis());
        if (persona.getPorcDisc() != null && !persona.getPorcDisc().toString().isEmpty()) {
            grado = persona.getPorcDisc().toString();
        }
        if (persona != null && persona.getDiscSevera() != null && !persona.getDiscSevera().isEmpty()) {
            severa = persona.getDiscSevera();
        }
        if (persona != null && persona.getValidez() != null && !persona.getValidez().isEmpty()) {
            validez = persona.getValidez();
        }
        if (persona != null && persona.getFechBaja() != null && !persona.getFechBaja().toString().isEmpty()) {
            fechaBaj = dateFormat.format(persona.getFechBaja());
        }
        if (persona != null && persona.getOidCertificado() != null && !persona.getOidCertificado().isEmpty()) {
            oid = persona.getOidCertificado();
        }
        if (persona != null && persona.getNombreCertificado() != null && !persona.getNombreCertificado().isEmpty()) {
            certi = persona.getNombreCertificado();
        }
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide58.SEQ_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " (ID, DNI,APELLIDOS, NOMBRE, TIPO_DISCP, PORC_DISCP, FECH_RESOLUCION, FECH_EMISION, DISC_SEVERA, FECH_VALIDACION, VALIDEZ, FECH_BAJA, "
                    + "OID_CERTIFICADO, NOMBRE_CERTIFICADO"
                    + ")"
                    + " VALUES(" + id
                    + ", '" + persona.getDni()
                    + "', '" + persona.getApellidos()
                    + "', '" + persona.getNombre()
                    + "', '" + persona.getTipoDisc()
                    + "', " + grado
                    + ", TO_DATE('" + fechaRes + "','dd/mm/yyyy')"
                    + ", TO_DATE('" + fechaEmi + "','dd/mm/yyyy')"
                    + " , '" + severa
                    + "', TO_DATE('" + fechaVal + "','dd/mm/yyyy')"
                    + ", '" + validez + "'"
                    + ", TO_DATE('" + fechaBaj + "','dd/mm/yyyy')"
                    + ", '" + oid + "'"
                    + ", '" + certi + "'"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar un nuevo registro de Persona Discapacitada - DNI: " + persona.getDni());
                return false;
            }
        } catch (Exception ex) {
            log.debug("Se ha producido un error al Insertar un nuevo registro de Persona Discapacitada " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean actualizarPersonaDisc(DiscapacitadoVO persona, Connection con) throws Exception {
        log.info("Entramos en actualizarPersonaDisc - " + this.getClass().getSimpleName());
        Statement st = null;
        String query = "";
        String fechaRes = "";
        String fechaEmi = "";
        String fechaVal = "";
        String grado = "";
        if (persona.getPorcDisc() != null && !persona.getPorcDisc().toString().isEmpty()) {
            grado = persona.getPorcDisc().toString();
        }
        if (persona != null && persona.getFechResolucion() != null && !persona.getFechResolucion().toString().isEmpty()) {
            fechaRes = dateFormat.format(persona.getFechResolucion());
        }
        if (persona != null && persona.getFechEmision() != null && !persona.getFechEmision().toString().isEmpty()) {
            fechaEmi = dateFormat.format(persona.getFechEmision());
        }
        fechaVal = dateFormat.format(System.currentTimeMillis());

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET APELLIDOS='" + persona.getApellidos() + "'"
                    + ", NOMBRE='" + persona.getNombre() + "'"
                    + ", TIPO_DISCP='" + grado + "'"
                    + ", PORC_DISCP=" + persona.getPorcDisc()
                    + ", FECH_EMISION=TO_DATE('" + fechaEmi + "','dd/mm/yyyy')"
                    + ", FECH_RESOLUCION=TO_DATE('" + fechaRes + "','dd/mm/yyyy')"
                    + ", DISC_SEVERA='" + persona.getDiscSevera() + "'"
                    + ", FECH_VALIDACION=TO_DATE('" + fechaVal + "','dd/mm/yyyy')"
                    + " WHERE ID=" + persona.getId()
                    + " AND DNI='" + persona.getDni() + "'"
                    + " AND FECH_BAJA IS NULL";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);

            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido Actualizar un registro de Persona Discapacitada - DNI: " + persona.getDni());
                return false;
            }
        } catch (SQLException ex) {
            log.debug("Se ha producido un error al Actualizar un registro de Persona Discapacitada  " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean borrarFechaBajaDisc(int codigo, String dni, Connection con) throws Exception {
        log.info("Entramos en borrarFechaBajaDisc de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET FECH_BAJA=NULL,"
                    + " FECH_VALIDACION=SYSDATE "
                    + " WHERE id=" + codigo
                    + " AND dni='" + dni + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando la fecha de baja en PERS_DISCP ", ex);
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
    }

    public boolean grabarHoyFechaBajaDisc(int codigo, String dni, Connection con) throws Exception {
        log.info("Entramos en grabaFechaBajaDisc de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET FECH_BAJA=SYSDATE, "
                    + " FECH_VALIDACION=SYSDATE "
                    + " WHERE id=" + codigo
                    + " AND dni='" + dni + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando la fecha de baja en PERS_DISCP ", ex);
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
    }

    public boolean grabaFechaBajaDisc(int codigo, String dni, java.util.Date fechaBaja, Connection con) throws Exception {
        log.info("Entramos en grabaFechaBajaDisc de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;

        String fechaGrabar = dateFormat.format(fechaBaja);
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET FECH_BAJA=TO_DATE('" + fechaGrabar + "' ,'dd/mm/yyyy'), "
                    + " FECH_VALIDACION=SYSDATE "
                    + " WHERE id=" + codigo
                    + " AND dni='" + dni + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando la fecha de baja en PERS_DISCP ", ex);
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
    }

    public HashMap<String, Date> getFechasAltas(String numExp, Connection con) throws Exception {
        log.info("Entramos en getFechasAltas de " + this.getClass().getSimpleName());
        HashMap<String, Date> fechasAltas = new HashMap<String, Date>();
        String dni = null;
        Date fechaAlta = null;
        ResultSet rs = null;
        Statement st = null;
        String query = null;
        try {
            //
            query = "SELECT MAX(fecha_alta) AS FECHA_ALTA,nif as NIF FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE num_exp='" + numExp + "' group by nif ORDER BY nif";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                fechaAlta = rs.getDate("FECHA_ALTA");
                dni = rs.getString("NIF");
                fechasAltas.put(dni, fechaAlta);
            }
            return fechasAltas;
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la fecha de baja de Anexo A - ALTAS ", ex);
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

    public HashMap<String, Date> getFechasBajas(String numExp, Connection con) throws Exception {
        log.info("Entramos en getFechasBajas de " + this.getClass().getSimpleName());
        HashMap<String, Date> fechasAltas = new HashMap<String, Date>();
        String dni = null;
        Date fechaBaja = null;
        ResultSet rs = null;
        Statement st = null;
        String query = null;
        try {
            //
            query = "SELECT MAX(fecha_baja) AS FECHA_BAJA,nif as NIF FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a"
                    + " WHERE a.num_exp='" + numExp + "'"
                    + " AND a.nif NOT IN  (select nif from " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE num_exp ='" + numExp + "')"
                    + " group by nif"
                    + " ORDER BY nif";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                fechaBaja = rs.getDate("FECHA_BAJA");
                dni = rs.getString("NIF");
                fechasAltas.put(dni, fechaBaja);
            }
            return fechasAltas;
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la fecha de baja de Anexo A - BAJAS ", ex);
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

    /* obtiene el ultimo registro de una persona. Si el codigo recibido es 0 solo busca por DNI */
    public DiscapacitadoVO getPersonaDisc(int codigo, String dniLimpio, Connection con) throws Exception {
        log.info("Entramos en getPersonaDisc de " + this.getClass().getSimpleName());
        DiscapacitadoVO persona = new DiscapacitadoVO();
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT a.*, b.DES_NOM desc_validez, c.DES_NOM desc_tipoD, d.DES_NOM desc_severa,  f.ter_doc centro,"
                    + " g.tde_valor AS territorio, h.des_nom desc_territorio"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a"
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_EXPEDIENTES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " z"
                    + " ON z.exp_num=(SELECT MAX(num_exp) FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE  upper((TRIM(TRANSLATE(nif, ',/./-', ' '))))=UPPER(a.dni))"
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DOMICILIO_TERCERO, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " y"
                    + " ON z.exp_num = y.ext_num AND y.ext_rol = 1"
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_TERCEROS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " f ON y.ext_ter = f.ter_cod"
                    + " JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " g"
                    + " ON z.exp_num = g.tde_num AND g.tde_cod = 'TERRHIST'"
                    + " LEFT JOIN  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " b"
                    + " ON b.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_VALIDISC, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND a.VALIDEZ=b.DES_VAL_COD"
                    + " LEFT JOIN  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " c"
                    + " ON c.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TIPODIS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND a.TIPO_DISCP=c.DES_VAL_COD"
                    + " LEFT JOIN  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " d"
                    + " ON d.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_BOOL, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND a.DISC_SEVERA=d.DES_VAL_COD"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " h"
                    + " ON h.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TERRITORIO, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND g.tde_valor=h.DES_VAL_COD"
                    + " WHERE UPPER(a.DNI)=UPPER('" + dniLimpio + "')";
            if (codigo != 0) {
                query += " AND a.ID=" + codigo;
            }
            query += " ORDER BY a.ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                persona = (DiscapacitadoVO) MeLanbide58MappingUtils.getInstance().map(rs, DiscapacitadoVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Persona Discapacitada ", ex);
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
        return persona;
    }

    public List<DiscapacitadoVO> getDatosPersona(String dni, Connection con) throws Exception {
        log.info(">>>> ENTRA en getDatosPersona - DAO");
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        List<DiscapacitadoVO> lista = new ArrayList<DiscapacitadoVO>();
        DiscapacitadoVO persona = new DiscapacitadoVO();
        try {
            query = "SELECT a.*, b.des_nom desc_validez, c.des_nom desc_tipod, d.des_nom desc_severa, f.ter_doc centro,"
                    + " g.tde_valor AS territorio, h.des_nom desc_territorio"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a"
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_EXPEDIENTES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " z"
                    + " ON z.exp_num=(SELECT MAX(num_exp) FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE  upper((TRIM(TRANSLATE(nif, ',/./-', ' '))))=UPPER(a.dni))"
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DOMICILIO_TERCERO, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " y"
                    + " ON z.exp_num = y.ext_num AND y.ext_rol = 1"
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_TERCEROS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " f ON y.ext_ter = f.ter_cod"
                    + " JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " g"
                    + " ON z.exp_num = g.tde_num AND g.tde_cod = 'TERRHIST'"
                    + " LEFT JOIN  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " b"
                    + " ON b.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_VALIDISC, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND a.VALIDEZ=b.DES_VAL_COD"
                    + " LEFT JOIN  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " c"
                    + " ON c.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TIPODIS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND a.TIPO_DISCP=c.DES_VAL_COD"
                    + " LEFT JOIN  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " d"
                    + " ON d.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_BOOL, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND a.DISC_SEVERA=d.DES_VAL_COD"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " h"
                    + " ON h.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TERRITORIO, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND g.tde_valor=h.DES_VAL_COD"
                    + " WHERE UPPER(a.DNI)=UPPER((TRIM(TRANSLATE('" + dni + "', ',/./-', ' '))))"
                    + " ORDER BY a.ID";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                persona = (DiscapacitadoVO) MeLanbide58MappingUtils.getInstance().map(rs, DiscapacitadoVO.class);
                lista.add(persona);
                persona = new DiscapacitadoVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos de una Persona Discapacitada ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    public List<DiscapacitadoVO> getPersonasBusqueda(String dni, String nombre, String apellidos, String tipo, String grado, String severa, String validez, String centro, String territorio, Connection con) throws Exception {
        log.info("Entramos en getPersonasBusqueda de " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        List<DiscapacitadoVO> lista = new ArrayList<DiscapacitadoVO>();
        DiscapacitadoVO persona = new DiscapacitadoVO();
        String query = null;
        try {
            query = "Select a.* , b.DES_NOM desc_validez, c.DES_NOM desc_tipoD, d.DES_NOM desc_severa, f.ter_doc centro,"
                    + " g.tde_valor AS territorio, h.des_nom desc_territorio"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " a"
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_EXPEDIENTES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " z"
                    + " ON z.exp_num=(SELECT MAX(num_exp) FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE  upper((TRIM(TRANSLATE(nif, ',/./-', ' '))))=UPPER(a.dni))"
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DOMICILIO_TERCERO, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " y"
                    + " ON z.exp_num = y.ext_num AND y.ext_rol = 1"
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_TERCEROS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " f ON y.ext_ter = f.ter_cod"
                    + " JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " g"
                    + " ON z.exp_num = g.tde_num AND g.tde_cod = 'TERRHIST'"
                    + " LEFT JOIN  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " b"
                    + " ON b.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_VALIDISC, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND a.VALIDEZ=b.DES_VAL_COD"
                    + " LEFT JOIN  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " c"
                    + " ON c.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TIPODIS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND a.TIPO_DISCP=c.DES_VAL_COD"
                    + " LEFT JOIN  " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " d"
                    + " ON d.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_BOOL, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND a.DISC_SEVERA=d.DES_VAL_COD"
                    + " LEFT JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + " h"
                    + " ON h.DES_COD='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TERRITORIO, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "' AND g.tde_valor=h.DES_VAL_COD"
                    + " Where 1=1 "
                    + " And (Upper(A.APELLIDOS) Like '%'||Nvl(Upper('" + apellidos + "'), '')||'%' Or '" + apellidos + "' Is Null) "
                    + " And (Upper(A.NOMBRE) Like '%'||Nvl(Upper('" + nombre + "'), '')||'%' Or '" + nombre + "' Is Null) "
                    + " And (Upper(A.DNI) Like '%'||Nvl(Upper('" + dni + "'), '')||'%' Or '" + dni + "' Is Null) "
                    + " And (Upper(A.porc_discp) Like '%'||Nvl(Upper('" + grado + "'), '')||'%' Or '" + grado + "' Is Null) "
                    + " And (Upper(A.tipo_discp) Like '%'||Nvl(Upper('" + tipo + "'), '')||'%' Or '" + tipo + "' Is Null) "
                    + " And (Upper(A.disc_severa) Like '%'||Nvl(Upper('" + severa + "'), '')||'%' Or '" + severa + "' Is Null) "
                    + " And (Upper(A.validez) Like '%'||Nvl(Upper('" + validez + "'), '')||'%' Or '" + validez + "' Is Null)"
                    + " And (Upper(f.ter_doc) Like '%'||Nvl(Upper('" + centro + "'), '')||'%' Or '" + centro + "' Is Null) "
                    + " And (Upper(g.tde_valor) Like '%'||Nvl(Upper('" + territorio + "'), '')||'%' Or '" + territorio + "' Is Null) "
                    + " Order By A.dni,A.Id";

            if (log.isDebugEnabled()) {
                log.debug("sql= " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                persona = (DiscapacitadoVO) MeLanbide58MappingUtils.getInstance().map(rs, DiscapacitadoVO.class);
                lista.add(persona);
                persona = new DiscapacitadoVO();
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error buscando Personas Discapacitadas ", ex);
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
        return lista;
    }

    public String getNifAnexoCPorId(String numExp, String id, Connection con) throws Exception {
        log.info("Entra en getNifAnexoCPorId - DAO");
        Statement st = null;
        ResultSet rs = null;
        String nif = null;
        try {
            String query = null;
            query = "SELECT NIF FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP='" + numExp + "'"
                    + " AND ID=" + id;
            log.debug(query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                nif = rs.getString("NIF");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el NIF por ID ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return nif;
    }

    public int modificarPersDiscp(DiscapacitadoVO persona, Connection con) throws Exception {
        log.info(">>>> ENTRA en modificarPersDiscp - DAO");
        Statement st = null;
        String query = "";
        String fechaEmi = "";
        String fechaRes = "";
        String fechaCad = "";
        String fechaVal = "";
        String fechaBaj = "";

        if (persona.getFechEmision() != null) {
            fechaEmi = dateFormat.format(persona.getFechEmision());
        }
        if (persona.getFechResolucion() != null) {
            fechaRes = dateFormat.format(persona.getFechResolucion());
        }
        if (persona.getFechCaducidad() != null) {
            fechaCad = dateFormat.format(persona.getFechCaducidad());
        }
        if (persona.getFechValidacion() != null) {
            fechaVal = dateFormat.format(persona.getFechValidacion());
        }
        if (persona.getFechBaja() != null) {
            fechaBaj = dateFormat.format(persona.getFechBaja());
        }

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET "
                    /*   + " DNI=UPPER('" + persona.getDni() + "')"
                    + ", NOMBRE='" + persona.getNombre() + "'"
                    + ", APELLIDOS='" + persona.getApellidos() + "'"
                    + ", "*/
                    + " TIPO_DISCP='" + persona.getTipoDisc() + "'"
                    + ", PORC_DISCP=" + persona.getPorcDisc()
                    + ", FECH_EMISION=TO_DATE('" + fechaEmi + "','dd/mm/yyyy')"
                    + ", FECH_RESOLUCION=TO_DATE('" + fechaRes + "','dd/mm/yyyy')"
                    + ", VALIDEZ='" + persona.getValidez() + "'"
                    + ", FECH_CADUCIDAD=TO_DATE('" + fechaCad + "','dd/mm/yyyy')"
                    + ", DISC_SEVERA='" + persona.getDiscSevera() + "'"
                    + ", FECH_VALIDACION=TO_DATE('" + fechaVal + "','dd/mm/yyyy')"
                    + ", FECH_BAJA=TO_DATE('" + fechaBaj + "','dd/mm/yyyy')"
                    + ", OID_CERTIFICADO='" + persona.getOidCertificado() + "'"
                    + ", NOMBRE_CERTIFICADO='" + persona.getNombreCertificado() + "'"
                    + " WHERE ID=" + persona.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar una Persona Discapacitada  - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public int insertarPersDiscp(DiscapacitadoVO persona, Connection con) throws Exception {
        log.info(">>>> ENTRA en insertarPersDiscp - DAO");
        Statement st = null;
        String query = "";
        String fechaEmi = "";
        String fechaRes = "";
        String fechaCad = "";
        String fechaVal = "";
        String fechaBaj = "";

        if (persona.getFechEmision() != null && !persona.getFechEmision().toString().isEmpty()) {
            fechaEmi = dateFormat.format(persona.getFechEmision());
        }
        if (persona.getFechResolucion() != null && !persona.getFechResolucion().toString().isEmpty()) {
            fechaRes = dateFormat.format(persona.getFechResolucion());
        }
        if (persona.getFechCaducidad() != null && !persona.getFechCaducidad().toString().isEmpty()) {
            fechaCad = dateFormat.format(persona.getFechCaducidad());
        }
        if (persona.getFechValidacion() != null && !persona.getFechValidacion().toString().isEmpty()) {
            fechaVal = dateFormat.format(persona.getFechValidacion());
        }
        if (persona.getFechBaja() != null && !persona.getFechBaja().toString().isEmpty()) {
            fechaBaj = dateFormat.format(persona.getFechBaja());
        }

        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide58.SEQ_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " (ID, DNI,NOMBRE, APELLIDOS, TIPO_DISCP, PORC_DISCP, FECH_EMISION, FECH_RESOLUCION, VALIDEZ, FECH_CADUCIDAD, DISC_SEVERA, FECH_VALIDACION, FECH_BAJA)"
                    + " VALUES (" + id
                    + ", UPPER('" + persona.getDni()
                    + "'), '" + persona.getNombre()
                    + "', '" + persona.getApellidos()
                    + "', '" + persona.getTipoDisc() + "'"
                    + ", " + persona.getPorcDisc()
                    + ",  TO_DATE('" + fechaEmi + "','dd/mm/yyyy')"
                    + ",  TO_DATE('" + fechaRes + "','dd/mm/yyyy')"
                    + ", '" + persona.getValidez()
                    + "', TO_DATE('" + fechaCad + "','dd/mm/yyyy')"
                    + ", '" + persona.getDiscSevera()
                    + "', TO_DATE('" + fechaVal + "','dd/mm/yyyy')"
                    + ",  TO_DATE('" + fechaBaj + "','dd/mm/yyyy')"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar una Persona Discapacitada - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean tieneCertificado(String idRegistro, String dniPersona, Connection con) throws Exception {
        log.info("Entramos en tieneCertificado - " + this.getClass().getSimpleName());
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "SELECT OID_CERTIFICADO FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE DNI='" + dniPersona + "'"
                    + " AND ID=" + idRegistro;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            return rs.next();
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando OID en la tabla de personas discapacitadas: ", ex);
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

    public boolean actualizarOid(String idRegistro, String dniPersona, String idDocumento, String nombreFichero, Connection con) throws Exception {
        log.info("Entramos en actualizarOid de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET OID_CERTIFICADO='" + idDocumento + "',"
                    + " NOMBRE_CERTIFICADO='" + nombreFichero + "'"
                    + " WHERE dni='" + dniPersona + "'"
                    + " AND id=" + idRegistro;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error actualizando el OID en PERS_DISCP ", ex);
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
    }

    public boolean insertarOid(String idRegistro, String idPersona, String idDocumento, String nombreFichero, Connection con) throws Exception {
        log.info("Entramos en insertarOid de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " (OID_CERTIFICADO, NOMBRE_CERTIFICADO)"
                    + " VALUES('" + idDocumento + "',"
                    + "'" + nombreFichero + "'"
                    + ") WHERE DNI='" + idPersona + "'"
                    + " AND ID=" + idRegistro
                    + "";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando el OID en PERS_DISCP ", ex);
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
    }

    public String ultimaIdPorDni(String dniPersona, Connection con) throws Exception {
        log.info("Entra en ultimaIdPorDni - DAO");
        Statement st = null;
        ResultSet rs = null;
        String idRegistro = null;
        try {
            String query = null;
            query = "SELECT MAX(ID) FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_PERS_DISCP, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE  DNI='" + dniPersona + "'";
            log.debug(query);
            st = con.createStatement();
            rs = st.executeQuery(query);

            if (rs.next()) {
                idRegistro = rs.getString("ID");
            }

        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el Id Persona por DNI ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return idRegistro;
    }

    public boolean actualizaPersonaAnexoC(String expediente, String dni, String fecEmision, String tipoD, Double grado, String severo, Connection con) throws Exception {
        log.info("Entramos en actualizaPersonaAnexoC de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET fecha_cert='" + fecEmision + "'"
                    + ", tipo_disc='" + tipoD + "'"
                    + ", grado_disc=" + grado
                    + ", disc_severa_emp='" + severo + "'"
                    + " WHERE upper((TRIM(TRANSLATE(nif, ',/./-', ' '))))=UPPER((TRIM(TRANSLATE('" + dni + "', ',/./-', ' '))))"
                    + " AND num_exp='" + expediente + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error actualizando datos  en Anexo C ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement ", e);
                throw new Exception(e);
            }
        }
    }

    public boolean marcarNuevaAlta(String numExp, String dniAlta, Connection con) throws Exception {
        log.info("Entramos en marcarNuevaAlta de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " SET NUEVA_ALTA='S'"
                    + " WHERE upper((TRIM(TRANSLATE(nif, ',/./-', ' '))))=UPPER((TRIM(TRANSLATE('" + dniAlta + "', ',/./-', ' '))))"
                    + " AND NUM_EXP='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error actualizando NUEVA_ALTA  " + numExp + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean esNuevaAlta(String numExp, String dni, Connection con) throws Exception {
        log.info("Entramos en esNuevaAlta - " + this.getClass().getSimpleName());
        boolean esAlta = false;
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "SELECT NUEVA_ALTA FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE NIF='" + dni + "'"
                    + " AND NUM_EXP='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                if (rs.getString("NUEVA_ALTA") != null && rs.getString("NUEVA_ALTA").equalsIgnoreCase("S")) {
                    log.debug("Es nueva alta");
                    esAlta = true;
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando si esNuevaAlta: ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return esAlta;
    }

    public boolean faltanDatos(String numExp, String dni, Connection con) throws Exception {
        log.info("Entramos en faltanDatos - " + this.getClass().getSimpleName());
        boolean faltan = false;
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "SELECT DATOS_PENDIENTES FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE NIF='" + dni + "'"
                    + " AND NUM_EXP='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                if (rs.getString("DATOS_PENDIENTES") != null && rs.getString("DATOS_PENDIENTES").equalsIgnoreCase("S")) {
                    log.debug("FALTAN DATOS");
                    faltan = true;
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando si esNuevaAlta: ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return faltan;
    }

    public int getUltimoNumLinea(String numExp, String tabla, Connection con) throws Exception {
        log.info("Entramos en getUltimoNumLinea - " + this.getClass().getSimpleName());
        int numero = 999;
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT MAX(NUM_LINEA) LINEA FROM " + tabla
                    + " WHERE NUM_EXP='" + numExp
                    + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numero = rs.getInt("LINEA");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el último número de línea de " + tabla + ": ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return numero;
    }

    public boolean existeAnexoC(String numExp, String dni, Connection con) throws Exception {
        log.info("Entramos en existeAnexoC - " + this.getClass().getSimpleName());
        boolean existe = false;
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            query = "SELECT ID FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE NIF='" + dni + "'"
                    + " AND NUM_EXP='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                existe = true;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando comprobando si " + dni + " existe en el anexo C  ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return existe;
    }

    public boolean coincideJornada(String numExp, String dni, Double jornada, Connection con) throws Exception {
        log.info("Entramos en coincideJornada - " + this.getClass().getSimpleName());
        boolean coincide = false;
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        try {
            //  select por_jornada from  melanbide58_plantilla where id =(select max(id) from melanbide58_plantilla where nif ='14927697F' and num_exp='2020/CEESC/000058');
            query = "SELECT por_jornada FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE id=(select max(id) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " where nif ='" + dni + "'"
                    + " AND NUM_EXP='" + numExp + "')";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                Double dato = rs.getDouble("POR_JORNADA");
                log.debug("Dato: " + dato);
                log.debug("Jornada: " + jornada);
                if (dato.compareTo(jornada) == 0) {
                    coincide = true;
                }
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando comprobando si coincide el % de jornada en el anexo C  ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return coincide;
    }

    public Map<String, String> getDocIncorrectosAnexo(String numExp, int codTabla, Connection con) throws Exception {
        log.info("Entramos en getDocIncorrectosAnexo de " + this.getClass().getSimpleName());
        HashMap<String, String> dnisIncorrectos = new HashMap<String, String>();
        String id = null;
        String dni = null;
        ResultSet rs = null;
        Statement st = null;
        String query = null;
        String claveP = "ID";
        String tabla = null;

        switch (codTabla) {
            case 1:
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                break;
            case 2:
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                break;
            case 3:
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                claveP = "IDSMI";
                break;
            case 4:
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                break;
            default:
                dnisIncorrectos.put("X", "0");
                log.error("El código  " + tabla + " no es correcto");
                return dnisIncorrectos;
        }
        try {
            query = "SELECT " + claveP + ", NIF FROM " + tabla
                    + " WHERE REGEXP_LIKE(nif,'[abcdefghjklmnpqrsttvwxyz .-]')"
                    + " AND num_exp ='" + numExp + "'"
                    + " ORDER BY " + claveP;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                id = rs.getString(claveP);
                dni = rs.getString("NIF");
                dnisIncorrectos.put(id, dni);
            }

            if (dnisIncorrectos.isEmpty()) {
                dnisIncorrectos.put("0", "0");
            }
            return dnisIncorrectos;

        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando comprobando si hay DNIs incorrectos en  " + tabla, ex);
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

    public boolean grabarDniCorrecto(String numExp, String id, String dni, int codTabla, Connection con) throws Exception {
        log.debug("Entramos en grabarDniCorrecto de " + this.getClass().getSimpleName());
        String claveP = "ID";
        String tabla = null;
        Statement st = null;
        String query = null;

        switch (codTabla) {
            case 1:
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                break;
            case 2:
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                break;
            case 3:
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                claveP = "IDSMI";
                break;
            case 4:
                tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                break;
            default:
                return false;
        }

        try {
            query = "UPDATE " + tabla + " SET NIF='" + dni + "'"
                    + " WHERE " + claveP + "=" + id
                    + " AND NUM_EXP='" + numExp + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando comprobando si hay DNIs incorrectos en  " + tabla, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean estanProcesadosAnexos(int codOrg, String numExp, Connection con) throws Exception {
        log.info("Entramos en estanProcesadosAnexos de " + this.getClass().getSimpleName());
        ResultSet rs = null;
        Statement st = null;
        String query = null;
        try {
            query = "SELECT COUNT(*) FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE tde_mun=" + codOrg
                    + " AND tde_eje=" + numExp.split(ConstantesMeLanbide58.BARRA_SEPARADORA)[0]
                    + " AND tde_cod='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.CAMPO_ANEXOS_PROCESADOS, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "'"
                    + " AND tde_num='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException ex) {
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

    public boolean grabarAnexosProcesados(int codOrg, String numExp, Connection con) throws Exception {
        log.info("Entramos en grabarAnexosProcesados de " + this.getClass().getSimpleName());
        Statement st = null;
        String query = null;
        String tablaDesp = ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_DESPLEGABLES_EXPEDIENTE, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
        String campo = ConfigurationParameter.getParameter(ConstantesMeLanbide58.CAMPO_ANEXOS_PROCESADOS, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
        try {
            if (estanProcesadosAnexos(codOrg, numExp, con)) {
                query = "UPDATE " + tablaDesp
                        + " SET tde_valor='S'"
                        + " WHERE tde_mun=" + codOrg
                        + " AND tde_eje=" + numExp.split(ConstantesMeLanbide58.BARRA_SEPARADORA)[0]
                        + " AND tde_num='" + numExp + "'"
                        + " AND tde_cod='" + campo + "'";
            } else {
                query = "INSERT INTO " + tablaDesp
                        + " (tde_mun,tde_eje, tde_num, tde_cod, tde_valor) VALUES"
                        + " (" + codOrg
                        + ", " + numExp.split("/")[0]
                        + ", '" + numExp + "'"
                        + ", '" + campo + "'"
                        + ", 'S')";
            }
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;
        } catch (SQLException ex) {
            con.rollback();
            log.error("Se ha producido un error actualizando Anexos Procesados  " + numExp + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean estaGrabadoResultado(int codOrg, String numExp, Connection con) throws Exception {
        log.info("Entramos en estaGrabadoResultado de " + this.getClass().getSimpleName());
        ResultSet rs = null;
        Statement st = null;
        String query = null;
        try {
            query = "SELECT COUNT(*) FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_TEXTO_LARGO, ConstantesMeLanbide58.FICHERO_PROPIEDADES)
                    + " WHERE ttl_mun=" + codOrg
                    + " AND ttl_eje=" + numExp.split(ConstantesMeLanbide58.BARRA_SEPARADORA)[0]
                    + " AND ttl_cod='" + ConfigurationParameter.getParameter(ConstantesMeLanbide58.CAMPO_RESULTADO_PROCESO, ConstantesMeLanbide58.FICHERO_PROPIEDADES) + "'"
                    + " AND ttl_num='" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando si está grabado el resultado de procesarAnexos  " + numExp + " - " + ex);
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

    public boolean grabarResultadoProcesar(int codOrg, String numExp, String resultado, Connection con) throws Exception {
        log.info("Entramos en grabarResultadoProcesar de " + this.getClass().getSimpleName());
        PreparedStatement ps = null;
        StringReader cr = new StringReader(resultado);
        int contbd = 1;
        String query = null;
        String[] partes = numExp.split(ConstantesMeLanbide58.BARRA_SEPARADORA);
        String tablaTl = ConfigurationParameter.getParameter(ConstantesMeLanbide58.TABLA_TEXTO_LARGO, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
        String campo = ConfigurationParameter.getParameter(ConstantesMeLanbide58.CAMPO_RESULTADO_PROCESO, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
        int graba = 0;
        try {
            if (estaGrabadoResultado(codOrg, numExp, con)) {
                query = "DELETE FROM " + tablaTl + " WHERE TTL_MUN=? AND TTL_EJE=? AND TTL_NUM=? AND TTL_COD=?";
                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                    log.debug("Parámetors pasados a la query: " + codOrg + "-" + Integer.valueOf(partes[0]) + "-" + numExp + "-" + campo);
                }
                ps = con.prepareStatement(query);
                ps.setInt(contbd++, codOrg);
                ps.setInt(contbd++, Integer.parseInt(partes[0]));
                ps.setString(contbd++, numExp);
                ps.setString(contbd++, campo);
                ps.executeUpdate();
            }
            if (resultado != null && !"".equals(resultado)) {
                query = "INSERT INTO " + tablaTl + " (TTL_MUN,TTL_EJE,TTL_NUM,TTL_COD,TTL_VALOR) VALUES (?,?,?,?,?)";
                if (log.isDebugEnabled()) {
                    log.debug("sql 2 = " + query);
                    log.debug("Parámetors pasados a la query: " + codOrg + "-" + Integer.valueOf(partes[0]) + "-" + numExp + "-" + campo);
                    log.debug(resultado);
                    log.debug(resultado.length());
                }
                ps = con.prepareStatement(query);
                contbd = 1;
                ps.setInt(contbd++, codOrg);
                ps.setInt(contbd++, Integer.parseInt(partes[0]));
                ps.setString(contbd++, numExp);
                ps.setString(contbd++, campo);
                ps.setCharacterStream(contbd++, cr, resultado.length());

                graba = ps.executeUpdate();

            }
        } catch (SQLException ex) {
            //          con.rollback();
            log.error("Se ha producido un error grabando el resultado de Procesar Anexos  " + numExp + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (cr != null) {
                cr.close();
            }
        }
        return graba > 0;
    }
}
