package es.altia.flexia.integracion.moduloexterno.melanbide13.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide13.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide13.util.ConstantesMeLanbide13;
import es.altia.flexia.integracion.moduloexterno.melanbide13.util.MeLanbide13MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide13.vo.ListaExpedientesVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide13DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide13DAO.class);
    //Instancia
    private static MeLanbide13DAO instance = null;

    // Constructor
    private MeLanbide13DAO() {

    }

    public static MeLanbide13DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide13DAO.class) {
                instance = new MeLanbide13DAO();
            }
        }
        return instance;
    }

    /**
     * Obtiene la lista de expedientes.
     *
     * @param numExp Número de expediente.
     * @param con Conexión a la base de datos.
     * @return Lista de objetos ListaExpedientesVO.
     * @throws Exception Excepción en caso de error.
     */
    public List<ListaExpedientesVO> getListaExpedientes(String numExp, String anio, Connection con) throws Exception {
        ResultSet rs = null;
        Statement st = null;
        List<ListaExpedientesVO> lista = new ArrayList<ListaExpedientesVO>();
        ListaExpedientesVO listaExpedientesVO = new ListaExpedientesVO();
        String query = null;
        try {
        /*    query = "SELECT datospagos.*, datosexpteactual.* FROM "
                    + "(SELECT ext_num AS expediente, edv2.des_nom AS territorio_historico, "
                    + "mes.des_nom AS mes, importe.tnu_valor AS importe, hte_doc "
                    + "FROM e_ext "
                    + "INNER JOIN t_hte ON ext_ter = hte_ter AND ext_nvr = hte_nvr "
                    + "INNER JOIN e_tde edv2 ON e_ext.ext_num = edv2.tde_num "
                    + "AND edv2.tde_cod = 'TERRHIST' AND e_ext.ext_mun = edv2.tde_mun "
                    + "LEFT JOIN e_des_val edv2 ON edv2.des_cod = 'TTHH' "
                    + "AND edv2.des_val_cod = edv2.tde_valor "
                    + "INNER JOIN e_tde anio ON anio.tde_num = e_ext.ext_num "
                    + "AND anio.tde_cod = 'ANIOAYUDA' AND anio.tde_valor = '" + anio + "' "
                    + "AND e_ext.ext_mun = anio.tde_mun "
                    + "LEFT JOIN e_des_val anio ON anio.des_cod = 'ANCE' "
                    + "AND anio.des_val_cod = anio.tde_valor "
                    + "INNER JOIN e_tde mes ON mes.tde_num = e_ext.ext_num "
                    + "AND mes.tde_cod = 'MESAYUDA' AND e_ext.ext_mun = mes.tde_mun "
                    + "LEFT JOIN e_des_val mes ON mes.des_cod = 'MES' "
                    + "AND mes.des_val_cod = mes.tde_valor "
                    + "LEFT JOIN e_tnu importe ON importe.tnu_num = e_ext.ext_num "
                    + "AND importe.tnu_cod = 'IMPRESOLUCION' "
                    + "AND e_ext.ext_mun = importe.tnu_mun "
                    + "WHERE ext_rol = 1 AND ext_pro = 'CEESC' "
                    + "ORDER BY edv2.des_nom, mes.tde_valor) datospagos "
                    + "INNER JOIN "
                    + "(SELECT hte_doc FROM e_ext "
                    + "INNER JOIN t_hte ON ext_ter = hte_ter AND ext_nvr = hte_nvr "
                    + "WHERE ext_rol = 1 AND ext_pro = 'CEESA' AND ext_num = '" + numExp + "') datosexpteactual "
                    + "ON datospagos.hte_doc = datosexpteactual.hte_doc";*/

            query = "select EXT_NUM as EXPEDIENTE, TTHH.DES_NOM as TERRITORIO_HISTORICO, MES.DES_NOM as MES, IMP.TNU_VALOR as IMPORTE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_TERCERO_EXP, ConstantesMeLanbide13.FICHERO_PROPIEDADES)
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_TERCEROS, ConstantesMeLanbide13.FICHERO_PROPIEDADES)
                    + " on E_EXT.EXT_TER = T_HTE.HTE_TER and E_EXT.EXT_NVR = T_HTE.HTE_NVR and T_HTE.HTE_DOC=("
                    + "     select HTE_DOC from " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_TERCERO_EXP, ConstantesMeLanbide13.FICHERO_PROPIEDADES)
                    + "     inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_TERCEROS, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + " on EXT_TER = HTE_TER and EXT_NVR = HTE_NVR "
                    + "     where EXT_ROL = 1 and EXT_PRO = 'CEESA' and EXT_NUM = '" + numExp + "')"
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_DESPLEGABLES_EXP, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + " DES1"
                    + " on E_EXT.EXT_NUM = DES1.TDE_NUM and E_EXT.EXT_MUN = DES1.TDE_MUN and E_EXT.EXT_EJE = DES1.TDE_EJE and DES1.TDE_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide13.COD_TERRITORIO, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + "'"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_VALORES_DESPLEGABLE, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + " TTHH"
                    + " on TTHH.DES_VAL_COD = DES1.TDE_VALOR and TTHH.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide13.COD_DES_TERRITORIO, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + "'"
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_DESPLEGABLES_EXP, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + " DES2"
                    + " on DES2.TDE_NUM = E_EXT.EXT_NUM and E_EXT.EXT_MUN = DES2.TDE_MUN and E_EXT.EXT_EJE = DES2.TDE_EJE and DES2.TDE_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide13.COD_ANIO, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + "' and DES2.TDE_VALOR = '" + anio + "'"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_VALORES_DESPLEGABLE, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + " ANIO"
                    + " on ANIO.DES_VAL_COD = DES2.TDE_VALOR and ANIO.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide13.COD_DES_ANIO, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + "'"
                    + " inner join " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_DESPLEGABLES_EXP, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + " DES3"
                    + " on DES3.TDE_NUM = E_EXT.EXT_NUM and E_EXT.EXT_MUN = DES3.TDE_MUN and E_EXT.EXT_EJE = DES3.TDE_EJE and DES3.TDE_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide13.COD_MES, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + "'"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_VALORES_DESPLEGABLE, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + " MES"
                    + " on MES.DES_VAL_COD = DES3.TDE_VALOR and MES.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide13.COD_DES_MES, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + "'"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_NUMERICOS_EXP, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + " IMP"
                    + " on IMP.TNU_NUM = E_EXT.EXT_NUM and E_EXT.EXT_MUN = IMP.TNU_MUN and E_EXT.EXT_EJE = IMP.TNU_EJE and IMP.TNU_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide13.COD_IMPORTE, ConstantesMeLanbide13.FICHERO_PROPIEDADES) + "'"
                    + " where EXT_ROL = 1 and EXT_PRO = 'CEESC'"
                    + " order by TTHH.DES_NOM, DES3.TDE_VALOR";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                listaExpedientesVO = (ListaExpedientesVO) MeLanbide13MappingUtils.getInstance().map(rs, ListaExpedientesVO.class);
                lista.add(listaExpedientesVO);
                listaExpedientesVO = new ListaExpedientesVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Expedientes ", ex);
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

    /**
     *
     * Metodo que recupera el valor de un campo desplegabe de un expediente
     *
     * @param codOrg
     * @param numExp
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
            query = "SELECT TDE_VALOR FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide13.TABLA_DESPLEGABLES_EXP, ConstantesMeLanbide13.FICHERO_PROPIEDADES)
                    + " WHERE TDE_MUN=? AND TDE_EJE=? AND TDE_NUM=? AND TDE_COD=?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide13.BARRA_SEPARADORA)[0]);
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

}
