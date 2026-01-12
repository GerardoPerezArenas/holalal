package es.altia.flexia.integracion.moduloexterno.melanbide90.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConstantesMeLanbide90;
import es.altia.flexia.integracion.moduloexterno.melanbide90.util.MeLanbide90MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FamiliaSolicitadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FilaMinimisVO;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.jdbc.core.SqlTypeValue;

public class MeLanbide90DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide90DAO.class);
    //Instancia
    private static MeLanbide90DAO instance = null;

    // Constructor
    private MeLanbide90DAO() {

    }

    public static MeLanbide90DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide90DAO.class) {
                instance = new MeLanbide90DAO();
            }
        }
        return instance;
    }

    public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.MELANBIDE90_SUBSOLIC, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide90MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(minimis);
                minimis = new FilaMinimisVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Minimis ", ex);
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

    public FilaMinimisVO getMinimisPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.MELANBIDE90_SUBSOLIC, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide90MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una Minimis : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return minimis;
    }

    public int eliminarMinimis(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.MELANBIDE90_SUBSOLIC, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Minimis ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (nuevaMinimis != null && nuevaMinimis.getFecha() != null && !nuevaMinimis.getFecha().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(nuevaMinimis.getFecha());
        }
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide90.SEQ_MELANBIDE90_SUBSOLIC, ConstantesMeLanbide90.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.MELANBIDE90_SUBSOLIC, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,ESTADO,ORGANISMO,OBJETO,IMPORTE,FECHA) "
                    + " VALUES (" + id
                    + ", '" + nuevaMinimis.getNumExp()
                    + "', '" + nuevaMinimis.getEstado()
                    + "', '" + nuevaMinimis.getOrganismo()
                    + "', '" + nuevaMinimis.getObjeto()
                    + "', " + nuevaMinimis.getImporte()
                    + " , TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No Se ha podido guardar una nueva Minimis ");
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar una nueva Minimis" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarMinimis(FilaMinimisVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (datModif != null && datModif.getFecha() != null && !datModif.getFecha().toString().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(datModif.getFecha());
        }

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.MELANBIDE90_SUBSOLIC, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " SET ESTADO='" + datModif.getEstado() + "'"
                    + ", ORGANISMO='" + datModif.getOrganismo() + "'"
                    + ", OBJETO='" + datModif.getObjeto() + "'"
                    + ", IMPORTE=" + datModif.getImporte()
                    + ", FECHA=TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una Minimis - " + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * metodo que recoge todas las facturas de un expediente
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return lista de FacturaVO con las facturas de un expediente
     * @throws Exception
     */
    public List<FacturaVO> getDatosFacturas(int codOrg, String numExp, Connection con) throws Exception {
        log.info("getDatosFacturas - " + numExp);
        List<FacturaVO> listaFacturas = new ArrayList<FacturaVO>();
        FacturaVO factura = new FacturaVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select fac.*, "
                    //  + "(fac.IMPORTEBASE + fac.IMPORTEIVA)IMPORTETOTAL, "
                    + "fam.DESFAMILIA_C, fam.DESFAMILIA_E, gas.DES_NOM  DES_GASTO, val.DES_NOM DES_VALIDADA, iva.DES_NOM DES_IVASUB, mot.DES_NOM DES_MOTIVO, porc.DES_NOM DES_PORCENTAJE, porcVal.DES_NOM DES_PORCENTAJE_VALIDADO "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fac "
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FAMILIAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fam "
                    + " on fam.CODFAMILIA = fac.FAMILIA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " gas "
                    + " on gas.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_GASTO, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and gas.DES_VAL_COD = fac.TIPOGASTO "
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " val "
                    + " on val.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_BOOL, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and val.DES_VAL_COD = fac.VALIDADA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " iva "
                    + " on iva.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_BOOL, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and iva.DES_VAL_COD = fac.IVASUB"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " mot "
                    + " on mot.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_MOTIVO, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and mot.DES_VAL_COD = fac.MOTNOVAL"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " porc "
                    + " on porc.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_PORCIVA, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and porc.DES_VAL_COD = fac.PORCIVA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " porcVal "
                    + " on porcVal.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_PORCIVA, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and porcVal.DES_VAL_COD = fac.PORCIVA_VALIDADO"
                    + " where NUM_EXP = ?"
                    + " order by FAC.FAMILIA, FAC.NUMORDEN";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                factura = (FacturaVO) MeLanbide90MappingUtils.getInstance().map(rs, FacturaVO.class);
                listaFacturas.add(factura);
                factura = new FacturaVO();
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las FACTURAS ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listaFacturas;
    }

    /**
     * metodo que recoge las facturas correspondientes a una FAMILIA de un
     * expediente
     *
     * @param codOrg
     * @param numExp
     * @param familia
     * @param con
     * @return lista de FacturaVO con las facturas de una familia
     * @throws Exception
     */
    public List<FacturaVO> getDatosFacturasFamilia(int codOrg, String numExp, String familia, Connection con) throws Exception {
        log.info("getDatosFacturasFamilia - " + numExp + " - " + familia);
        List<FacturaVO> listaFacturas = new ArrayList<FacturaVO>();
        FacturaVO factura = new FacturaVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select fac.*"
                    // + ", (fac.IMPORTEBASE + fac.IMPORTEIVA)IMPORTETOTAL"
                    + ", fam.DESFAMILIA_C, fam.DESFAMILIA_E, gas.DES_NOM  DES_GASTO, val.DES_NOM DES_VALIDADA, iva.DES_NOM DES_IVASUB, mot.DES_NOM DES_MOTIVO, porc.DES_NOM DES_PORCENTAJE, porcVal.DES_NOM DES_PORCENTAJE_VALIDADO "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fac "
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FAMILIAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fam "
                    + " on fam.CODFAMILIA = fac.FAMILIA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " gas "
                    + " on gas.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_GASTO, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and gas.DES_VAL_COD = fac.TIPOGASTO "
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " val "
                    + " on val.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_BOOL, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and val.DES_VAL_COD = fac.VALIDADA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " iva "
                    + " on iva.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_BOOL, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and iva.DES_VAL_COD = fac.IVASUB"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " mot "
                    + " on mot.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_MOTIVO, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and mot.DES_VAL_COD = fac.MOTNOVAL"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " porc "
                    + " on porc.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_PORCIVA, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and porc.DES_VAL_COD = fac.PORCIVA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " porcVal "
                    + " on porcVal.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_PORCIVA, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and porcVal.DES_VAL_COD = fac.PORCIVA_VALIDADO"
                    + " where fac.NUM_EXP = ? and FAC.FAMILIA = ?"
                    + " order by fac.NUMORDEN";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, familia);
            rs = ps.executeQuery();
            while (rs.next()) {
                factura = (FacturaVO) MeLanbide90MappingUtils.getInstance().map(rs, FacturaVO.class);
                listaFacturas.add(factura);
                factura = new FacturaVO();
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las FACTURAS ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listaFacturas;
    }

    /**
     * metodo que recupera los datos de una factura
     *
     * @param id
     * @param con
     * @return el objeto FacturaVO
     * @throws Exception
     */
    public FacturaVO getFacturaPorId(String id, Connection con) throws Exception {
        log.info("getFacturaPorId - " + id);
        FacturaVO factura = new FacturaVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select fac.*, "
//                    + "(fac.IMPORTEBASE + fac.IMPORTEIVA)IMPORTETOTAL,"
                    + " fam.DESFAMILIA_C, fam.DESFAMILIA_E, gas.DES_NOM  DES_GASTO, val.DES_NOM DES_VALIDADA, iva.DES_NOM DES_IVASUB, mot.DES_NOM DES_MOTIVO, porc.DES_NOM DES_PORCENTAJE, porcVal.DES_NOM DES_PORCENTAJE_VALIDADO "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fac "
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FAMILIAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fam "
                    + " on fam.CODFAMILIA = fac.FAMILIA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " gas "
                    + " on gas.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_GASTO, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and gas.DES_VAL_COD = fac.TIPOGASTO "
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " val "
                    + " on val.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_BOOL, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and val.DES_VAL_COD = fac.VALIDADA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " iva "
                    + " on iva.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_BOOL, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and iva.DES_VAL_COD = fac.IVASUB"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " mot "
                    + " on mot.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_MOTIVO, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and mot.DES_VAL_COD = fac.MOTNOVAL"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " porc "
                    + " on porc.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_PORCIVA, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and porc.DES_VAL_COD = fac.PORCIVA"
                     + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " porcVal "
                    + " on porcVal.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_PORCIVA, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + "'"
                    + " and porcVal.DES_VAL_COD = fac.PORCIVA_VALIDADO"
                    + " where ID = ?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                factura = (FacturaVO) MeLanbide90MappingUtils.getInstance().map(rs, FacturaVO.class);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las FACTURAS ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return factura;
    }

    /**
     *
     * Operacion que crea una nueva factura
     *
     * @param factura
     * @param con
     * @return
     * @throws Exception
     */
    public boolean nuevaFactura(FacturaVO factura, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        try {
            int i = 1;
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide90.SEQ_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES), con);

            query = "Insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " (ID, NUM_EXP, FAMILIA, NUMORDEN, TIPOGASTO, PROVEEDOR, NUMFACTURA, FECEMISION, FECPAGO, IMPORTEBASE, IMPORTEIVA, IMPORTEVALI, VALIDADA, IVASUB, MOTNOVAL "
                    + " ,IMPORTEBASE_VALIDADO, PORCIVA_VALIDADO, IMPORTEIVA_VALIDADO, PRORRATA_VALIDADO, TOTAL "
                    + ") "
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
                    + " ,?,?,?,?,? "
                    + ")";

            ps = con.prepareStatement(query);
            ps.setInt(i++, id);
            ps.setString(i++, factura.getNumExp());
            ps.setString(i++, factura.getFamilia());
            ps.setInt(i++, factura.getNumOrden());
            ps.setString(i++, factura.getTipoGasto());
            ps.setString(i++, factura.getProveedor());
            ps.setString(i++, factura.getNumFactura());
            ps.setDate(i++, factura.getFecEmision());
            ps.setDate(i++, factura.getFecPago());
            ps.setDouble(i++, factura.getImporteBase());
            ps.setDouble(i++, factura.getImporteIva());
            ps.setDouble(i++, factura.getImporteVali());
            ps.setString(i++, factura.getValidada());
            ps.setString(i++, factura.getIvaSub());
            ps.setString(i++, factura.getMotNoVal());
            if(factura.getImporteBaseValidado() != null)
                ps.setDouble(i++, factura.getImporteBaseValidado());
            else
                ps.setNull(i++, Types.DOUBLE);
            ps.setString(i++, factura.getPorcentajeIvaValidado());
            if(factura.getImporteIvaValidado() != null)
                ps.setDouble(i++, factura.getImporteIvaValidado());
            else
                ps.setNull(i++, Types.DOUBLE);
            if(factura.getProrrataValidado() != null)
                ps.setInt(i++, factura.getProrrataValidado());
            else
                ps.setNull(i++, Types.INTEGER);
            if(factura.getImporteTotal() != null)
                ps.setDouble(i++, factura.getImporteTotal());
            else
                ps.setNull(i++, Types.DOUBLE);
            log.debug("sql = " + query);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al insertar una nueva Factura" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Operacion que modifica una factura
     *
     * @param factura
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarFactura(FacturaVO factura, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        try {
            int i = 1;
            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " set FAMILIA = ?, NUMORDEN = ?, TIPOGASTO = ?, PROVEEDOR = ?, NUMFACTURA = ?, FECEMISION = ?, FECPAGO = ?, IMPORTEBASE = ?, IMPORTEIVA = ?,  IMPORTEVALI = ?, VALIDADA = ?, IVASUB = ?, MOTNOVAL = ?"
                    + " , IMPORTEBASE_VALIDADO = ? , PORCIVA_VALIDADO = ? , IMPORTEIVA_VALIDADO = ? , PRORRATA_VALIDADO = ? , TOTAL=?"
                    + " where ID=? and NUM_EXP=?";

            ps = con.prepareStatement(query);
            ps.setString(i++, factura.getFamilia());
            ps.setInt(i++, factura.getNumOrden());
            ps.setString(i++, factura.getTipoGasto());
            ps.setString(i++, factura.getProveedor());
            ps.setString(i++, factura.getNumFactura());
            ps.setDate(i++, factura.getFecEmision());
            ps.setDate(i++, factura.getFecPago());
            ps.setDouble(i++, factura.getImporteBase());
            ps.setDouble(i++, factura.getImporteIva());
            ps.setDouble(i++, factura.getImporteVali());
            ps.setString(i++, factura.getValidada());
            ps.setString(i++, factura.getIvaSub());
            ps.setString(i++, factura.getMotNoVal());
            if(factura.getImporteBaseValidado() != null)
                ps.setDouble(i++, factura.getImporteBaseValidado());
            else
                ps.setNull(i++, Types.DOUBLE);
            ps.setString(i++, factura.getPorcentajeIvaValidado());
            if(factura.getImporteIvaValidado() != null)
                ps.setDouble(i++, factura.getImporteIvaValidado());
            else
                ps.setNull(i++, Types.DOUBLE);
            if(factura.getProrrataValidado() != null)
                ps.setInt(i++, factura.getProrrataValidado());
            else
                ps.setNull(i++, Types.INTEGER);
            if(factura.getImporteTotal() != null)
                ps.setDouble(i++, factura.getImporteTotal());
            else
                ps.setNull(i++, Types.DOUBLE);
            ps.setInt(i++, factura.getId());
            ps.setString(i++, factura.getNumExp());
            log.debug("sql = " + query);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una : " + factura.getId(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
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
    public boolean eliminarFactura(String id, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " WHERE ID = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            log.debug("PARÁMETROS: " + id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando SubSolic ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * metodo que devuelve el numero de FAMILIAS en las facturas presentadas
     *
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public int getNumeroFamilias(String numExp, Connection con) throws Exception {
        log.info("getNumeroFamilias - " + numExp);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        int familias = 0;
        try {
            query = "select count(distinct FAMILIA)FAMILIAS from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " where NUM_EXP = ?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();
            if (rs.next()) {
                familias = rs.getInt("FAMILIAS");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el número de Familias ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return familias;

    }

    /**
     * metodo que devuelve la suma de los importes de cada FAMILIA solicitada en
     * un expediente
     *
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public List<FamiliaSolicitadaVO> getFamiliasSolicitadas(String numExp, Connection con) throws Exception {
        log.info("getFamiliasSolicitadas - " + numExp);
        List<FamiliaSolicitadaVO> listaFamiliasSol = new ArrayList<FamiliaSolicitadaVO>();
        FamiliaSolicitadaVO familia = new FamiliaSolicitadaVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select fac.FAMILIA, fam.DESFAMILIA_C, fam.DESFAMILIA_E, sum(fac.IMPORTEBASE)BASETOTAL, sum(fac.IMPORTEIVA)IVATOTAL, sum(fac.TOTAL)IMPORTETOTAL, sum(fac.IMPORTEVALI)VALITOTAL"
                    + " ,sum(fac.IMPORTEBASE_VALIDADO) BASETOTAL_VALIDADO, sum(fac.IMPORTEIVA_VALIDADO) IVATOTAL_VALIDADO, sum(NVL(fac.IMPORTEBASE_VALIDADO,0) + NVL(fac.IMPORTEIVA_VALIDADO,0)) IMPORTETOTAL_VALIDADO   "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fac"
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FAMILIAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fam "
                    + " on fam.CODFAMILIA = fac.FAMILIA"
                    + " where fac.NUM_EXP = ?"
                    + " group by fac.FAMILIA, fam.DESFAMILIA_C, fam.DESFAMILIA_E"
                    + " order by fac.FAMILIA";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                familia = (FamiliaSolicitadaVO) MeLanbide90MappingUtils.getInstance().map(rs, FamiliaSolicitadaVO.class);
                listaFamiliasSol.add(familia);
                familia = new FamiliaSolicitadaVO();
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las FAMILIAS ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listaFamiliasSol;
    }

    /**
     * metodo que devuelve la suma de los importes de una FAMILIA solicitada en
     * un expediente
     *
     * @param numExp
     * @param codFamilia
     * @param con
     * @return
     * @throws Exception
     */
    public FamiliaSolicitadaVO getFamiliaSolicitadaExpedienteCodigo(String numExp, String codFamilia, Connection con) throws Exception {
        log.info("getFamiliaSolicitadaExpedienteCodigo - " + numExp + " - " + codFamilia);
        FamiliaSolicitadaVO familia = new FamiliaSolicitadaVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select fac.FAMILIA, fam.DESFAMILIA_C, fam.DESFAMILIA_E, sum(fac.IMPORTEBASE)BASETOTAL, sum(fac.IMPORTEIVA)IVATOTAL, sum(fac.TOTAL)IMPORTETOTAL, sum(fac.IMPORTEVALI)VALITOTAL"
                    + " ,sum(fac.IMPORTEBASE_VALIDADO) BASETOTAL_VALIDADO, sum(fac.IMPORTEIVA_VALIDADO) IVATOTAL_VALIDADO, sum(NVL(fac.IMPORTEBASE_VALIDADO,0) + NVL(fac.IMPORTEIVA_VALIDADO,0)) IMPORTETOTAL_VALIDADO   "
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FACTURAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fac"
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_FAMILIAS, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " fam "
                    + " on fam.CODFAMILIA = fac.FAMILIA"
                    + " where fac.NUM_EXP = ? and fac.FAMILIA = ?"
                    + " group by fac.FAMILIA, fam.DESFAMILIA_C, fam.DESFAMILIA_E";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, codFamilia);

            rs = ps.executeQuery();
            while (rs.next()) {
                familia = (FamiliaSolicitadaVO) MeLanbide90MappingUtils.getInstance().map(rs, FamiliaSolicitadaVO.class);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los datos de la FAMILIA ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return familia;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public String getDocumentoInteresado(int codOrg, String numExp, Connection con) throws Exception {
        log.info("getDocumentoInteresado - " + numExp);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String documento = null;
        try {
            query = "select HTE_DOC from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_TERCERO_EXP, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + " a "
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_TERCEROS, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " on HTE_TER = A.EXT_TER and HTE_NVR = A.EXT_NVR "
                    + " where a.EXT_ROL = 1 and a.EXT_MUN =? and a.EXT_PRO = ? and a.EXT_EJE = ? and a.EXT_NUM = ?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide90.BARRA_SEPARADORA)[1]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide90.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();
            if (rs.next()) {
                documento = rs.getString("HTE_DOC");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el documento del interesado ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return documento;
    }

    /**
     *
     * @param familia
     * @param numDocumento
     * @param ejercicio
     * @param con
     * @return
     * @throws Exception
     */
    public int getEspecialidadesFamiliaEjer(String familia, String numDocumento, int ejercicio, Connection con) throws Exception {
        log.info("getEspecialidadesFamiliaEjer - " + familia + " - " + ejercicio);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        int especialidades = 0;
        try {
            query = "select " + familia + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.VISTA_ESP_FAMILIA, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " where GEN_USU_NUM_DOC = ? and ANO = ?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numDocumento);
            ps.setInt(i++, ejercicio);
            rs = ps.executeQuery();
            if (rs.next()) {
                especialidades = rs.getInt(familia);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las Especialidades Inscritas de una familia ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return especialidades;
    }

    /**
     *
     * @param familia
     * @param numDocumento
     * @param ejercicio
     * @param con
     * @return
     * @throws Exception
     */
    public Map<Integer, Integer> getAFsFamiliaPeriodo(String familia, String numDocumento, int ejercicio, Connection con) throws Exception {
        log.info("getAFsFamiliaPeriodo - " + familia + " - " + ejercicio);
        HashMap<Integer, Integer> afs = new HashMap<Integer, Integer>();
        Integer ano = null;
        Integer especialidades = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select ANO, " + familia + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.VISTA_AFS_FAMILIA, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " where GEN_USU_NUM_DOC = ? and ANO between ? and ? order by ANO";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numDocumento);
            ps.setInt(i++, (ejercicio - 4));
            ps.setInt(i++, ejercicio);
            rs = ps.executeQuery();
            while (rs.next()) {
                ano = rs.getInt("ANO");
                especialidades = rs.getInt(familia);
                afs.put(ano, especialidades);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando las AAFF de un periodo para una familia ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return afs;
    }

    /**
     *
     * @param ejercicio
     * @param con
     * @return
     * @throws Exception
     */
    public Double[] getImportesMaximosEjer(int ejercicio, Connection con) throws Exception {
        log.info("getImportesMaximosEjer - " + ejercicio);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        Double[] impMaximos= {null,null};
      //  impMaximos = new ArrayList;
        try {
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_MAXIMOS, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " where ejercicio = ?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, ejercicio);
            rs = ps.executeQuery();
            if (rs.next()) {
                log.debug("saco valores ");
                impMaximos[0] = rs.getDouble("MAX_FAMILIA");
                impMaximos[1] = rs.getDouble("MAX_MINIMIS");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando importes máximos del ejercicio " + ejercicio, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return impMaximos;
    }

    /**
     *
     * @param sequence
     * @param con
     * @return
     * @throws Exception
     */
    private int recogerIDInsertar(String sequence, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            String query = "SELECT " + sequence + ".NextVal AS PROXID FROM DUAL ";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("PROXID");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el número de ID para inserción al llamar la secuencia " + sequence + " : ", ex);
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

    /**
     *
     * @param des_cod
     * @param con
     * @return
     * @throws Exception
     */
    public List<DesplegableAdmonLocalVO> getValoresDesplegablesXdes_cod(String des_cod, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD= ? AND DES_VAL_ESTADO = 'A' order by DES_VAL_COD";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, des_cod);
            rs = ps.executeQuery();

            while (rs.next()) {
                valoresDesplegable = (DesplegableAdmonLocalVO) MeLanbide90MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegable);
                valoresDesplegable = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando valores de desplegable : " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     * Metodo que recupera el valor de un campo desplegabe de un expediente
     *
     * @param codOrg
     * @param numExp
     * @param codDesplegable
     * @param con
     * @return
     * @throws Exception
     */
    public String getValorDesplegableExpediente(int codOrg, String numExp, String codDesplegable, Connection con) throws Exception {
        log.info("getValorDesplegableExpediente - " + numExp + " - " + codDesplegable);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String valor = null;
        try {
            query = "SELECT TDE_VALOR FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_DESPLEGABLES_EXP, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " WHERE TDE_MUN=? AND TDE_EJE=? AND TDE_NUM=? AND TDE_COD=?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide90.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, codDesplegable);
            rs = ps.executeQuery();
            while (rs.next()) {
                valor = rs.getString("TDE_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Excepcion - getValorDesplegableExpediente : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return valor;
    }

    /**
     * Metodo que recupera el valor de un campo desplegabe externo de un
     * expediente
     *
     * @param codOrg
     * @param numExp
     * @param codDesplegable
     * @param con
     * @return
     * @throws Exception
     */
    public String getCodSelDesplegableExterno(int codOrg, String numExp, String codDesplegable, Connection con) throws Exception {
        log.info("getCodSelDesplegableExterno - " + numExp + " - " + codDesplegable);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String valor = null;
        try {
            query = "SELECT TDEX_CODSEL FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide90.TABLA_DESPLEGABLES_EXT, ConstantesMeLanbide90.FICHERO_PROPIEDADES)
                    + " WHERE TDEX_MUN=? AND TDEX_EJE=? AND TDEX_NUM=? AND TDEX_COD=?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide90.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, codDesplegable);
            rs = ps.executeQuery();
            if (rs.next()) {
                valor = rs.getString("TDEX_CODSEL");
            }
        } catch (SQLException ex) {
            log.error("Excepcion - getCodSelDesplegableExterno : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return valor;

    }

}
