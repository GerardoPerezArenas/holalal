package es.altia.flexia.integracion.moduloexterno.melanbide71.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide71.util.ConstantesMeLanbide71;
import es.altia.flexia.integracion.moduloexterno.melanbide71.util.MeLanbide71MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide71.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide71.vo.ExpedienteVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide71DAO {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide71DAO.class);
    //Instancia
    private static MeLanbide71DAO instance = null;
    // Constructor
    private MeLanbide71DAO() {   
        
    }
    
    //Devolvemos una única instancia de la clase a través de este método ya que el constructor es privado
    public static MeLanbide71DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide71DAO.class) {
                instance = new MeLanbide71DAO();
            }
        }
        return instance;
    }

    public ExpedienteVO getDatosExpediente(int codOrg, String numExpediente, Connection con) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query;
        try{
            //CIFCBSC EN E_TXT
            query = new StringBuilder("SELECT T_CAMPOS_DESPLEGABLE.VALOR as sexo, "
                    + "       T_CAMPOS_FECHA.VALOR       as fecnacimiento,"
                    + "       TFE_VALOR                  as fecPresentacion,"
                    + "       TXT_VALOR                  AS cifCBSC"
                    + "  FROM e_exp"
                    + "  INNER JOIN e_ext on e_exp.EXP_MUN=e_ext.EXT_MUN"
                    + "                  and e_exp.EXP_EJE=e_ext.EXT_EJE"
                    + "                  and e_exp.EXP_NUM=e_ext.EXT_NUM"
                    + "                  and e_exp.EXP_PRO=e_ext.EXT_PRO"
                    + "   left join T_CAMPOS_DESPLEGABLE on e_ext.EXT_TER=T_CAMPOS_DESPLEGABLE.COD_TERCERO"
                    + "                                 and e_ext.EXT_MUN=T_CAMPOS_DESPLEGABLE.COD_MUNICIPIO"
                    + "   left join T_CAMPOS_FECHA on  e_ext.EXT_TER=T_CAMPOS_FECHA.COD_TERCERO"
                    + "                           and e_ext.EXT_MUN=T_CAMPOS_FECHA.COD_MUNICIPIO"
                    + "   left join (select TFE_EJE,TFE_NUM,TFE_MUN,TFE_VALOR"
                    + "                from e_tfe"
                    + "               where TFE_COD='FECHAPRESENTACION') e_tfe on e_exp.EXP_EJE=e_tfe.TFE_EJE"
                    + "                                                       and e_exp.EXP_NUM=e_tfe.TFE_NUM"
                    + "                                                       and e_exp.EXP_MUN=e_tfe.TFE_MUN"
                    + "   LEFT JOIN (SELECT TXT_EJE, TXT_NUM, TXT_MUN, TXT_VALOR"
                    + "                FROM e_txt"
                    + "               WHERE TXT_COD='CIFCBSC') e_txt ON e_exp.EXP_EJE=e_txt.TXT_EJE "
                    + "                                             AND e_exp.EXP_NUM=e_txt.TXT_NUM"
                    + "                                             AND e_exp.EXP_MUN=e_txt.TXT_MUN"
                    + " WHERE e_exp.EXP_NUM=?"
                    + "   and e_exp.EXP_MUN=?"
                    + "   and e_ext.EXT_ROL=?"
                    + "   and T_CAMPOS_DESPLEGABLE.COD_CAMPO='TSEXOTERCERO'"
                    + "   and T_CAMPOS_FECHA.COD_CAMPO='TFECNACIMIENTO'");
            log.debug("query = "+query);
            log.debug("parametros de la query - EXP_NUM: "+numExpediente);
            log.debug("parametros de la query - EXT_ROL: "+ConstantesMeLanbide71.ROLES.INTERESADO);
            
            ps = con.prepareStatement(query.toString());
            ps.setString(1, numExpediente);
            ps.setString(2, String.valueOf(codOrg));
            ps.setString(3, String.valueOf(ConstantesMeLanbide71.ROLES.INTERESADO));
            rs = ps.executeQuery();
            if (rs.next()) {
                return (ExpedienteVO) MeLanbide71MappingUtils.getInstance().map(rs, ExpedienteVO.class);
            }
        } catch(Exception ex){
            log.error("Ha ocurrido un error al obtener los datos del tercero del expediente: "+numExpediente);
            ex.printStackTrace();
            throw ex;
        } finally{
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
        }
        }
        
        return null;
    }

    public ExpedienteVO getDatosExpAPEAI(int codOrg, String numExpediente, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        StringBuilder query;
        try {
            query = new StringBuilder("select T_CAMPOS_DESPLEGABLE.VALOR as SEXO, T_CAMPOS_FECHA.VALOR as FECNACIMIENTO, TFE_VALOR as FECPRESENTACION\n"
                    + "from E_EXP\n"
                    + "inner join E_EXT on E_EXP.EXP_MUN = E_EXT.EXT_MUN \n"
                    + "                    and E_EXP.EXP_EJE = E_EXT.EXT_EJE\n"
                    + "                    and E_EXP.EXP_NUM = E_EXT.EXT_NUM\n"
                    + "                    and E_EXP.EXP_PRO = E_EXT.EXT_PRO\n"
                    + "left join T_CAMPOS_DESPLEGABLE on E_EXT.EXT_TER = T_CAMPOS_DESPLEGABLE.COD_TERCERO\n"
                    + "                                  and E_EXT.EXT_MUN = T_CAMPOS_DESPLEGABLE.COD_MUNICIPIO\n"
                    + "left join T_CAMPOS_FECHA on E_EXT.EXT_TER = T_CAMPOS_FECHA.COD_TERCERO\n"
                    + "                            and E_EXT.EXT_MUN = T_CAMPOS_FECHA.COD_MUNICIPIO\n"
                    + "left join(select TFE_EJE, TFE_NUM, TFE_MUN, TFE_VALOR from E_TFE where TFE_COD = 'FECHAPRESENTACION')E_TFE \n"
                    + "         on E_EXP.EXP_EJE = E_TFE.TFE_EJE\n"
                    + "                   and E_EXP.EXP_NUM = E_TFE.TFE_NUM\n"
                    + "                   and E_EXP.EXP_MUN = E_TFE.TFE_MUN\n"
                    + "where E_EXP.EXP_NUM = ? \n"
                    + "      and E_EXP.EXP_MUN =? \n"
                    + "      and E_EXT.EXT_ROL =? \n"
                    + "      and T_CAMPOS_DESPLEGABLE.COD_CAMPO = 'TSEXOTERCERO'\n"
                    + "      and T_CAMPOS_FECHA.COD_CAMPO = 'TFECNACIMIENTO'");

            log.debug("getDatosExpAPEAI = " + query);
            log.debug("parametros de la query - EXP_NUM: " + numExpediente);
            log.debug("parametros de la query - EXT_ROL: " + ConstantesMeLanbide71.ROLES.INTERESADO);
            ps = con.prepareStatement(query.toString());
            int contbd = 1;
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setInt(contbd++, ConstantesMeLanbide71.ROLES.INTERESADO);
            rs = ps.executeQuery();
            if (rs.next()) {
                return (ExpedienteVO) MeLanbide71MappingUtils.getInstance().map(rs, ExpedienteVO.class);
            }
        } catch (Exception ex) {
            log.error("Ha ocurrido un error al obtener los datos del tercero del expediente: " + ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return null;
    }

    public DatosEconomicosExpVO getImporteSubvencion(Integer edad, String sexo, String pro, String eje, Connection con) throws Exception{
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query;
        try{
            query = "SELECT SUB_IMPORTE "
                    + "FROM MELANBIDE71_SUBV_COLEC_SEX "
                    + "INNER JOIN MELANBIDE71_COLECTIVO "
                    + "ON MELANBIDE71_COLECTIVO.COL_ID=MELANBIDE71_SUBV_COLEC_SEX.COL_ID "
                    + "AND MELANBIDE71_COLECTIVO.COL_PRO=MELANBIDE71_SUBV_COLEC_SEX.SCS_PRO "
                    + "AND MELANBIDE71_COLECTIVO.COL_EJE=MELANBIDE71_SUBV_COLEC_SEX.SCS_EJE "
                    + "inner join MELANBIDE71_SUBVENCION "
                    + "on MELANBIDE71_SUBVENCION.SUB_ID=MELANBIDE71_SUBV_COLEC_SEX.SUB_ID "
                    + "AND MELANBIDE71_SUBVENCION.SUB_PRO=MELANBIDE71_SUBV_COLEC_SEX.SCS_PRO "
                    + "AND MELANBIDE71_SUBVENCION.SUB_EJE=MELANBIDE71_SUBV_COLEC_SEX.SCS_EJE "
                    + "WHERE SCS_PRO=? AND SCS_EJE=? AND SCS_SEXO=? "
                    + "AND MELANBIDE71_COLECTIVO.COL_EDADI<=? "
                    + "AND (MELANBIDE71_COLECTIVO.COL_EDADF IS NULL "
                    + "OR MELANBIDE71_COLECTIVO.COL_EDADF>=?)";
            log.debug("query = "+query);
            log.debug("parametros de la query - edad: "+edad);
            log.debug("parametros de la query - sexo: "+sexo);
            
            ps = con.prepareStatement(query);
            ps.setString(1, pro);
            ps.setString(2, eje);
            ps.setString(3, sexo);
            ps.setInt(4, edad);
            ps.setInt(5, edad);
            rs = ps.executeQuery();
            if (rs.next()) {
                return (DatosEconomicosExpVO) MeLanbide71MappingUtils.getInstance().map(rs, DatosEconomicosExpVO.class);
            }
        } catch(Exception ex){
            log.error("Ha ocurrido un error al obtener el importe de la subvencion: "+ex);
            throw ex;
        } finally{
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        
        return null;
    }

    public void insertSuplNumerico(int codOrg, String numExpediente, Integer valor, String nombreCampo, Connection con) throws Exception{
        PreparedStatement ps = null;
        String query;
        try{
            query = "INSERT INTO E_TNU (TNU_VALOR,TNU_NUM,TNU_MUN,TNU_COD,TNU_EJE) VALUES(?,?,?,?,?)";
            log.debug("query = "+query);
            log.debug("parametros pasados a la query TNU_VALOR= "+valor);
            log.debug("parametros pasados a la query TNU_COD= "+nombreCampo);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setInt(contbd++, valor);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);
            ps.setString(contbd++, numExpediente.substring(0,4));
            
            ps.executeUpdate();
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al guardar datos suplementarios");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void deleteSuplNumerico(int codOrg, String numExpediente, String nombreCampo, Connection con) throws Exception{
        PreparedStatement ps = null;
        String query;
        try{
            query = "DELETE FROM E_TNU where TNU_NUM=? and TNU_MUN=? and TNU_COD=?";
            log.debug("query = "+query);
            log.debug("parametros pasados a la query TNU_COD= "+nombreCampo);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);
            
            ps.executeUpdate();
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al borrar datos suplementarios");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void deleteSuplDespl(int codOrg, String numExpediente, String nombreCampo, Connection con) throws Exception{
        PreparedStatement ps = null;
        String query;
        try{
            query = "DELETE FROM E_TDE where TDE_NUM=? and TDE_MUN=? and TDE_COD=?";
            log.debug("query = "+query);
            log.debug("parametros pasados a la query TDE_COD= "+nombreCampo);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);
            
            ps.executeUpdate();
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al borrar datos suplementarios");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void insertSuplDespl(int codOrg, String numExpediente, String valor, String nombreCampo, Connection con) throws Exception{
        PreparedStatement ps = null;
        String query;
        try{
            query = "INSERT INTO E_TDE (TDE_VALOR,TDE_NUM,TDE_MUN,TDE_COD,TDE_EJE) VALUES(?,?,?,?,?)";
            log.debug("query = "+query);
            log.debug("parametros pasados a la query TDE_VALOR= "+valor);
            log.debug("parametros pasados a la query TDE_COD= "+nombreCampo);
            
            ps = con.prepareStatement(query);
            int contbd = 1;
            ps.setString(contbd++, valor);
            ps.setString(contbd++, numExpediente);
            ps.setInt(contbd++, codOrg);
            ps.setString(contbd++, nombreCampo);
            ps.setString(contbd++, numExpediente.substring(0,4));
            
            ps.executeUpdate();
        } catch (SQLException sqle){
            log.error("Ha ocurrido un error al guardar datos suplementarios");
            throw sqle;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

}
