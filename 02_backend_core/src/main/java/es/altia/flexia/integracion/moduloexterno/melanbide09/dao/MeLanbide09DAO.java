package es.altia.flexia.integracion.moduloexterno.melanbide09.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConstantesMeLanbide07;
import es.altia.flexia.integracion.moduloexterno.melanbide09.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide09.util.ConstantesMeLanbide09;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.ComboVO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.FiltrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.ResultadoConsultaVO;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 *
 * @author alexandrep
 */
public class MeLanbide09DAO {
    
      

    private static final Logger log = LogManager.getLogger(MeLanbide09DAO.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    //Instancia
    private static MeLanbide09DAO instance = null;

    // Constructor
    private MeLanbide09DAO() {
        
    }

    
    public static MeLanbide09DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide09DAO.class) {
                instance = new MeLanbide09DAO();
            }
        }
        return instance;
    }

    public List<ComboVO> getComboProcedimiento(int codOrganizacion,int codUsuario, Connection con) throws Exception {
        log.info("getComboProcedimiento - Begin () " + formatDate.format(new Date()));
        List<ComboVO> retorno = new ArrayList<ComboVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT DISTINCT PRO_COD AS PROC, pro_des AS PML_VALOR "
                    + "FROM E_PUI, E_PRO "
                    + "WHERE "
                     + " pro_cod != 'REINT' AND "
                    +" (PUI_MUN=PRO_MUN AND PUI_PRO=PRO_COD  "
                    + "    AND (EXISTS (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=1 AND UOU_UOR=PUI_COD))  "
                    + "    AND PRO_FLD<=  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'')  AND ( PRO_FLH IS NULL  OR  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'') <=PRO_FLH )  "
                    + "    AND PRO_EST=1 AND PRO_LIBRERIA <> 1  AND pro_cod != 'REINT' )  "
                    + "UNION  "
                    + "SELECT DISTINCT PRO_COD  AS PROC , pro_des AS PML_VALOR "
                    + "FROM E_PRO "
                    + "WHERE (( NOT EXISTS (  SELECT DISTINCT PUI_COD FROM E_PUI  WHERE PUI_PRO=PRO_COD)  AND EXISTS (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=1)) "   
                    + "    AND PRO_FLD<=  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'')  AND ( PRO_FLH IS NULL  OR  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'') <=PRO_FLH )  "
                    + "    AND PRO_EST=1 AND PRO_LIBRERIA <> 1  AND pro_cod != 'REINT' )  "
                    + "UNION  "
                    + "SELECT DISTINCT e_tra.tra_pro  AS PROC , pro_des AS PML_VALOR "
                    + "FROM e_tra  "
                    + "INNER JOIN E_PRO ON (E_TRA.tra_pro = pro_cod and E_TRA.tra_mun = pro_mun)  "
                    + "RIGHT JOIN e_tra_utr on (e_tra.tra_cod = e_tra_utr.tra_cod and e_tra.tra_mun = e_tra_utr.tra_mun and e_tra.tra_pro = e_tra_utr.tra_pro )   "
                    + "where e_tra.tra_utr =0 and exists (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=1 AND UOU_UOR=tra_utr_cod) "
                    + "    AND PRO_FLD<=  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'')   "
                    + "    AND ( PRO_FLH IS NULL  OR  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'') <=PRO_FLH )   "
                    + "    AND PRO_EST=1 "
                    + "    AND PRO_LIBRERIA <> 1 "
                     + "     AND pro_cod != 'REINT'  "
                    + "UNION "
                    + "SELECT DISTINCT tra_pro AS PROC , pro_des AS PML_VALOR "
                    + "FROM e_tra "
                    + "INNER JOIN E_PRO ON (tra_pro = pro_cod and tra_mun = pro_mun  )  "
                    + "where ("
                    + "        ((tra_utr =1 or tra_uin = -99998 ) and  EXISTS (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=? AND UOU_ORG=? AND UOU_ENT=1)) "
                    + "        or exists (  SELECT DISTINCT UOU_UOR FROM flbgen.A_UOU  WHERE UOU_USU=5 AND UOU_ORG=1 AND UOU_ENT=1 AND UOU_UOR=tra_uin) "
                    + "		) "
                    + "    AND PRO_FLD<=  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'')  "
                    + "    AND ( PRO_FLH IS NULL  OR  NVL(TO_DATE( NVL(TO_CHAR( SYSDATE ,'DD/MM/YYYY'),'') ,'DD/MM/YYYY'),'') <=PRO_FLH )  "
                    + "    AND PRO_EST=1 "
                    + "    AND PRO_LIBRERIA<>1 "
                    + "     AND pro_cod != 'REINT'  "
                    + "ORDER BY PROC ASC";

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setInt(1, codUsuario);
            pt.setInt(2, codOrganizacion);
            pt.setInt(3, codUsuario);
            pt.setInt(4, codOrganizacion);
            pt.setInt(5, codUsuario);
            pt.setInt(6, codOrganizacion);
            pt.setInt(7, codUsuario);
            pt.setInt(8, codOrganizacion);
            log.info("Param ? : " + codUsuario);
            rs = pt.executeQuery();
            while (rs.next()) {
                ComboVO elementoListaRetorno = new ComboVO();
                elementoListaRetorno.setId(rs.getString("proc"));
                elementoListaRetorno.setValor(rs.getString("PML_VALOR"));
                retorno.add(elementoListaRetorno);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger combos ... ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getComboProcedimiento - End () " + formatDate.format(new Date()));
        return retorno;
    }

    
      public List<ComboVO> getComboTramites(int codOrganizacion,int codUsuario,String codProcedimiento, Connection con) throws Exception {
        log.info("getComboProcedimiento - Begin () " + formatDate.format(new Date()));
        List<ComboVO> retorno = new ArrayList<ComboVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            String query = "SELECT TRA_COD, TML_VALOR FROM "+ConfigurationParameter.getParameter(
                                ConstantesMeLanbide09.TABLA_TRAMITES,
                                ConstantesMeLanbide09.FICHERO_PROPIEDADES)+" tra " +
            "INNER JOIN "+ConfigurationParameter.getParameter(
                                ConstantesMeLanbide09.TABLA_TRAMITES_DES,
                                ConstantesMeLanbide09.FICHERO_PROPIEDADES)+" des ON tra.TRA_COD=des.TML_TRA AND tra.TRA_PRO=des.TML_PRO"
                    + "  WHERE tra.TRA_NOTIFICACION_ELECTRONICA=1 AND tra.TRA_PRO=?";

            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            pt.setString(1, codProcedimiento);
            log.info("Param ? : " + codUsuario);
            rs = pt.executeQuery();
            while (rs.next()) {
                ComboVO elementoListaRetorno = new ComboVO();
                elementoListaRetorno.setId(rs.getString("TRA_COD"));
                elementoListaRetorno.setValor(rs.getString("TML_VALOR"));
                retorno.add(elementoListaRetorno);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error recoger combos ... ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getComboProcedimiento - End () " + formatDate.format(new Date()));
        return retorno;
    }
      
      
         
    public List<ResultadoConsultaVO> getExpedientesSinNotificarSinPaginar(int codOrganizacion, Connection con, FiltrosVO filtros, boolean exportar,List<ComboVO> listaProcedimiento) throws Exception {
        log.info("getExpedientesSinNotificarSinPaginar - Begin () " + formatDate.format(new Date()));
        List<ResultadoConsultaVO> retorno = new ArrayList<ResultadoConsultaVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            //Dejamos preparado por si nunca se quiere exportar los resultados
            String query = "";
            if (exportar) {
                query = " ";
            } else {
                query = "SELECT " +
            "    ocu.cro_pro, " +
            "    ocu.cro_num, " +
            "    ocu.cro_eje, " +
            "    ocu.cro_tra, " +
           "    ocu.CRO_MUN, " +    
             "    ocu.CRO_UTR, " +               
                   "    ocu.CRO_OCU, " +         
            "    des.tml_valor,"
            + " cro_fei " +
                        
            "FROM " +
            "         e_cro ocu " +
            "    INNER JOIN e_tml des ON ocu.cro_pro = des.tml_pro " +
            "                            AND ocu.cro_tra =  des.tml_tra " +
            "    INNER JOIN E_TRA tra ON tra.TRA_COD=des.TML_TRA AND tra.TRA_PRO=des.TML_PRO "
                        + "INNER JOIN e_exp exp ON ocu.cro_num = exp.exp_num AND exp.exp_est <> 9 "
                         + " LEFT OUTER JOIN (SELECT DISTINCT NUMEXPEDIENTE,CODTRAMITE,ESTADOLOG FROM "+ConfigurationParameter.getParameter(
                                ConstantesMeLanbide09.TABLA_LOG_JOB,
                                ConstantesMeLanbide09.FICHERO_PROPIEDADES)+" WHERE estadolog IN ( 'PENDIENTE', 'TRAMITANDO' )) log on log.NUMEXPEDIENTE=ocu.cro_num AND log.CODTRAMITE = ocu.cro_tra "
                        + " WHERE tra.TRA_NOTIFICACION_ELECTRONICA=1 AND CRO_FEF IS NULL  AND (log.estadolog IS NULL OR log.estadolog NOT IN ('PENDIENTE','TRAMITANDO')  )";

                }
            if (filtros != null) {
                log.info("params : " + filtros.getStart() + ", "
                        + filtros.getFinish() + ", "
                        + filtros.getEjercicio() + ", "
                        + filtros.getProcedimiento() + ", "
                        + filtros.getTramite() + ", "
                        + filtros.getTipoDocumento() + ", "
                        + filtros.getFechaEnvioPeticion() + "."
                );

                //Por si se buscan no enviados al portafirmas
                if (filtros.getTramite() != null && !filtros.getTramite().isEmpty()) {
                    log.info("aplicando condición --> Tramite");
                    query += " AND ocu.cro_tra = "+ filtros.getTramite();
                } 

                if (filtros.getEjercicio() != null && !filtros.getEjercicio().isEmpty()) {
                    log.info("aplicando condición --> ejercicio");
                    query += " AND cro_eje = '" + filtros.getEjercicio() + "'";
                }
                if (filtros.getProcedimiento() != null && !filtros.getProcedimiento().isEmpty()) {
                    log.info("aplicando condición --> procedimiento");

                    query += " AND ocu.cro_pro = '" + filtros.getProcedimiento() + "'";

                }else {
                    //Obtenemos los procedimientos que puede consultar el usuario
                  String  listaProcedimientos ="(";
                for (int i = 0; i < listaProcedimiento.size(); i++) {
                String procedimiento = listaProcedimiento.get(i).getId();
                if (listaProcedimientos == "(") {
                    listaProcedimientos = listaProcedimientos + "'" + procedimiento + "'";
                } else {
                    listaProcedimientos = listaProcedimientos + "," + "'" + procedimiento + "'";
                }

            }
            listaProcedimientos = listaProcedimientos + ")";
                    
                     query += " AND ocu.cro_pro IN " + listaProcedimientos + " ";
                }

                if (filtros.getNumeroExpediente() != null && !filtros.getNumeroExpediente().isEmpty()) {
                    log.info("aplicando condición --> NumeroExpediente");

                    query += " AND ocu.cro_num = '" + filtros.getNumeroExpediente() + "'";

                }

                if (filtros.getFechaEnvioTramiteDesde() != null && !filtros.getFechaEnvioTramiteDesde().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioTramiteDesde");

                    query += " AND to_char(ocu.cro_fei,'dd/mm/yyyy')>='" + filtros.getFechaEnvioTramiteDesde() + "'";

                }

                if (filtros.getFechaEnvioTramiteHasta() != null && !filtros.getFechaEnvioTramiteHasta().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioTramiteHasta");

                    query += " AND to_char(ocu.cro_fei,'dd/mm/yyyy')<='" + filtros.getFechaEnvioTramiteHasta() + "'";

                }

                if (filtros.getTipoDocumento() != null && !filtros.getTipoDocumento().isEmpty()) {
                    log.info("aplicando condición --> tipoDocumento");
                    //Desa va mas rapido dejamos el LIKE, dejamos el substr por si se debe modificar mas adelante
                    // query += " substr(crd.crd_des,-3) ='" + filtros.getTipoDocumento() + "'";
                    query += " AND crd.crd_des LIKE '%" + filtros.getTipoDocumento() + "'";
                }
                
                //TODO para cuando esten en el job
         

                log.info("----Fin aplicar condiciones----");

            }
            if (exportar) {
                query += " ORDER BY ocu.cro_fei";
            } else {
                query += " ORDER BY ocu.cro_fei DESC";
            }
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
                ResultadoConsultaVO elementoListaRetorno = new ResultadoConsultaVO();

                elementoListaRetorno.setEjercicio(rs.getString("cro_eje"));
                elementoListaRetorno.setCodTramite(rs.getString("CRO_TRA"));
                elementoListaRetorno.setDesTramite(rs.getString("tml_valor"));
                elementoListaRetorno.setProcedimiento(rs.getString("cro_PRO"));
                elementoListaRetorno.setNumExpediente(rs.getString("Cro_NUM"));
                elementoListaRetorno.setFechaRegistrado(rs.getString("cro_fei"));
                elementoListaRetorno.setCodigoMunicipio(rs.getString("CRO_MUN"));
                  elementoListaRetorno.setOcurrenciaTramite(rs.getString("CRO_OCU"));
                   elementoListaRetorno.setUtr(rs.getString("CRO_UTR"));
                
                retorno.add(elementoListaRetorno);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error consultando los registros ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getExpedientesSinNotificarSinPaginar - End () " + formatDate.format(new Date()));
        return retorno;
    }

    public List<ResultadoConsultaVO> getExpedientesSinNotificar(int codOrganizacion, Connection con, FiltrosVO filtros, boolean exportar,List<ComboVO> listaProcedimiento) throws Exception {
        log.info("getExpedientesSinNotificar - Begin () " + formatDate.format(new Date()));
        List<ResultadoConsultaVO> retorno = new ArrayList<ResultadoConsultaVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try {
            //Dejamos preparado por si nunca se quiere exportar los resultados
            String query = "";
            if (exportar) {
                query = " ";
            } else {
                query = "SELECT * FROM ("
                        + " SELECT I.*, ROWNUM rnum FROM ("
                        + " SELECT " +
            "    ocu.cro_pro, " +
            "    ocu.cro_num, " +
            "    ocu.cro_eje, " +
            "    ocu.cro_tra, " +
            "    des.tml_valor,"
            + " cro_fei " +
                        
            "FROM " +
            "         e_cro ocu " +
            "    INNER JOIN e_tml des ON ocu.cro_pro = des.tml_pro " +
            "                            AND ocu.cro_tra =  des.tml_tra " +
            "    INNER JOIN E_TRA tra ON tra.TRA_COD=des.TML_TRA AND tra.TRA_PRO=des.TML_PRO "
                        + "INNER JOIN e_exp exp ON ocu.cro_num = exp.exp_num AND exp.exp_est <> 9 "
                          + " LEFT OUTER JOIN (SELECT DISTINCT NUMEXPEDIENTE,CODTRAMITE,ESTADOLOG FROM "+ConfigurationParameter.getParameter(
                                ConstantesMeLanbide09.TABLA_LOG_JOB,
                                ConstantesMeLanbide09.FICHERO_PROPIEDADES)+" WHERE estadolog IN ( 'PENDIENTE', 'TRAMITANDO' )) log on log.NUMEXPEDIENTE=ocu.cro_num AND log.CODTRAMITE = ocu.cro_tra "
                        + " WHERE tra.TRA_NOTIFICACION_ELECTRONICA=1 AND CRO_FEF IS NULL  AND (log.estadolog IS NULL OR log.estadolog NOT IN ('PENDIENTE','TRAMITANDO')  )" ;
           

            }
           if (filtros != null) {
                log.info("params : " + filtros.getStart() + ", "
                        + filtros.getFinish() + ", "
                        + filtros.getEjercicio() + ", "
                        + filtros.getProcedimiento() + ", "
                        + filtros.getTramite() + ", "
                        + filtros.getTipoDocumento() + ", "
                        + filtros.getFechaEnvioPeticion() + "."
                );

                //Por si se buscan no enviados al portafirmas
  if (filtros.getTramite() != null && !filtros.getTramite().isEmpty()) {
      log.info("aplicando condición --> Tramite");
                    query += " AND ocu.cro_tra = "+ filtros.getTramite();
                } 

                if (filtros.getEjercicio() != null && !filtros.getEjercicio().isEmpty()) {
                    log.info("aplicando condición --> ejercicio");
                    query += " AND cro_eje = '" + filtros.getEjercicio() + "'";
                }
                if (filtros.getProcedimiento() != null && !filtros.getProcedimiento().isEmpty()) {
                    log.info("aplicando condición --> procedimiento");

                    query += " AND ocu.cro_pro = '" + filtros.getProcedimiento() + "'";

                }else {
                    //Obtenemos los procedimientos que puede consultar el usuario
                  String  listaProcedimientos ="(";
                for (int i = 0; i < listaProcedimiento.size(); i++) {
                String procedimiento = listaProcedimiento.get(i).getId();
                if (listaProcedimientos == "(") {
                    listaProcedimientos = listaProcedimientos + "'" + procedimiento + "'";
                } else {
                    listaProcedimientos = listaProcedimientos + "," + "'" + procedimiento + "'";
                }

            }
            listaProcedimientos = listaProcedimientos + ")";
                    
                     query += " AND ocu.cro_pro IN " + listaProcedimientos + " ";
                }

                if (filtros.getNumeroExpediente() != null && !filtros.getNumeroExpediente().isEmpty()) {
                    log.info("aplicando condición --> NumeroExpediente");

                    query += " AND ocu.cro_num = '" + filtros.getNumeroExpediente() + "'";

                }

                if (filtros.getFechaEnvioTramiteDesde() != null && !filtros.getFechaEnvioTramiteDesde().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioTramiteDesde");

                    query += " AND to_char(ocu.cro_fei,'dd/mm/yyyy')>='" + filtros.getFechaEnvioTramiteDesde() + "'";

                }

                if (filtros.getFechaEnvioTramiteHasta() != null && !filtros.getFechaEnvioTramiteHasta().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioTramiteHasta");

                    query += " AND to_char(ocu.cro_fei,'dd/mm/yyyy')<='" + filtros.getFechaEnvioTramiteHasta() + "'";

                }

          
      

                log.info("----Fin aplicar condiciones----");

            }
            if (exportar) {
                query += " ORDER BY ocu.cro_fei";
            } else {
                query += " ORDER BY ocu.cro_fei DESC) I "
                        + "WHERE";

                query += " ROWNUM <= " + filtros.getFinish() + ") "
                        + "WHERE rnum > " + filtros.getStart();
            }
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
                ResultadoConsultaVO elementoListaRetorno = new ResultadoConsultaVO();

                  elementoListaRetorno.setEjercicio(rs.getString("cro_eje"));
                elementoListaRetorno.setCodTramite(rs.getString("CRO_TRA"));
                elementoListaRetorno.setDesTramite(rs.getString("tml_valor"));
                elementoListaRetorno.setProcedimiento(rs.getString("cro_PRO"));
                elementoListaRetorno.setNumExpediente(rs.getString("Cro_NUM"));
                elementoListaRetorno.setFechaRegistrado(rs.getString("cro_fei"));
                retorno.add(elementoListaRetorno);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error consultando los registros ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getExpedientesSinNotificar - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
    public int getNumRegistrosTotalExpedientesSinNotificar(int codOrganizacion, Connection con, FiltrosVO filtros, List<ComboVO> listaProcedimiento) throws Exception {
        log.info("getNumRegistrosTotalExpedientesSinNotificar - Begin () " + formatDate.format(new Date()));
        int retorno = 0;
        PreparedStatement pt = null;
        ResultSet rs = null;

        try {
            String query = " SELECT COUNT(*) FROM ("
                 
                        + " SELECT " +
            "    ocu.cro_pro, " +
            "    ocu.cro_num, " +
            "    ocu.cro_eje, " +
            "    ocu.cro_tra, " +
            "    des.tml_valor,"
            + " cro_fei " +
                        
            "FROM " +
            "         e_cro ocu " +
            "    INNER JOIN e_tml des ON ocu.cro_pro = des.tml_pro " +
            "                            AND ocu.cro_tra =  des.tml_tra " +
            "    INNER JOIN E_TRA tra ON tra.TRA_COD=des.TML_TRA AND tra.TRA_PRO=des.TML_PRO "
                    + " INNER JOIN e_exp exp ON ocu.cro_num = exp.exp_num AND exp.exp_est <> 9 "
                    + " LEFT OUTER JOIN (SELECT DISTINCT NUMEXPEDIENTE,CODTRAMITE,ESTADOLOG FROM "+ConfigurationParameter.getParameter(
                                ConstantesMeLanbide09.TABLA_LOG_JOB,
                                ConstantesMeLanbide09.FICHERO_PROPIEDADES)+" WHERE estadolog IN ( 'PENDIENTE', 'TRAMITANDO' )) log on log.NUMEXPEDIENTE=ocu.cro_num AND log.CODTRAMITE = ocu.cro_tra "
                        + " WHERE tra.TRA_NOTIFICACION_ELECTRONICA=1 AND CRO_FEF IS NULL  AND (log.estadolog IS NULL OR log.estadolog NOT IN ('PENDIENTE','TRAMITANDO')  )";


            
           if (filtros != null) {
                log.info("params : " + filtros.getStart() + ", "
                        + filtros.getFinish() + ", "
                        + filtros.getEjercicio() + ", "
                        + filtros.getProcedimiento() + ", "
                        + filtros.getTramite() + ", "
                        + filtros.getTipoDocumento() + ", "
                        + filtros.getFechaEnvioPeticion() + "."
                );

                //Por si se buscan no enviados al portafirmas
  if (filtros.getTramite() != null && !filtros.getTramite().isEmpty()) {
      log.info("aplicando condición --> Tramite");
                    query += " AND ocu.cro_tra = "+ filtros.getTramite();
                } 

                if (filtros.getEjercicio() != null && !filtros.getEjercicio().isEmpty()) {
                    log.info("aplicando condición --> ejercicio");
                    query += " AND cro_eje = '" + filtros.getEjercicio() + "'";
                }
                if (filtros.getProcedimiento() != null && !filtros.getProcedimiento().isEmpty()) {
                    log.info("aplicando condición --> procedimiento");

                    query += " AND ocu.cro_pro = '" + filtros.getProcedimiento() + "'";

                }else {
                    //Obtenemos los procedimientos que puede consultar el usuario
                  String  listaProcedimientos ="(";
                for (int i = 0; i < listaProcedimiento.size(); i++) {
                String procedimiento = listaProcedimiento.get(i).getId();
                if (listaProcedimientos == "(") {
                    listaProcedimientos = listaProcedimientos + "'" + procedimiento + "'";
                } else {
                    listaProcedimientos = listaProcedimientos + "," + "'" + procedimiento + "'";
                }

            }
            listaProcedimientos = listaProcedimientos + ")";
                    
                     query += " AND ocu.cro_pro IN " + listaProcedimientos + " ";
                }

                if (filtros.getNumeroExpediente() != null && !filtros.getNumeroExpediente().isEmpty()) {
                    log.info("aplicando condición --> NumeroExpediente");

                    query += " AND ocu.cro_num = '" + filtros.getNumeroExpediente() + "'";

                }

                if (filtros.getFechaEnvioTramiteDesde() != null && !filtros.getFechaEnvioTramiteDesde().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioTramiteDesde");

                    query += " AND to_char(ocu.cro_fei,'dd/mm/yyyy')>='" + filtros.getFechaEnvioTramiteDesde() + "'";

                }

                if (filtros.getFechaEnvioTramiteHasta() != null && !filtros.getFechaEnvioTramiteHasta().isEmpty()) {
                    log.info("aplicando condición --> getFechaEnvioTramiteHasta");

                    query += " AND to_char(ocu.cro_fei,'dd/mm/yyyy')<='" + filtros.getFechaEnvioTramiteHasta() + "'";

                }

          query +=" )";
                //TODO para cuando esten en el job
               // query += "  AND (log.estadolog IS NULL OR log.estadolog NOT IN ('PENDIENTE','TRAMITANDO')  ) ";
                log.info("----Fin aplicar condiciones----");
            }
         
            pt = con.prepareStatement(query);

         
            log.info("sql = " + query);
              rs = pt.executeQuery();
            while (rs.next()) {
                retorno = rs.getInt(1);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error consultando los registros ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }

        log.info("getNumRegistrosTotalExpedientesSinNotificar - END () " + formatDate.format(new Date()));
        return retorno;
    }

    
    
    public String cerrarTramite(int codOrganizacion, String numExp, String codTramite,String procedimiento, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try {
            String query = "UPDATE " + ConfigurationParameter.getParameter(
                                ConstantesMeLanbide09.TABLA_E_CRO,
                                ConstantesMeLanbide09.FICHERO_PROPIEDADES)
                    + " set CRO_FEF=SYSDATE"
                    + ", CRO_OBS = cro_obs  || chr(13) || 'Tramite cerrado por job de Envio de notificaciones melanbide 09'"
                    + ", CRO_USF = 5"
                    + " where CRO_MUN = " + codOrganizacion
                    + " and CRO_PRO = '" + procedimiento + "'"
                    + " and CRO_NUM = '" + numExp + "'"
                    + " and CRO_TRA = " + codTramite;
            log.debug("sql = " + query);

            st = con.createStatement();
            rs = st.executeQuery(query);

        } catch (SQLException ex) {
            log.error("Se ha producido un error al cerrar el tr?mite " + codTramite, ex);
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
        return valor != null ? valor.toString() : null;
    }
    
    
    public String abrirTramite(int codOrganizacion, String numExp, String codTramite,String procedimiento, String uor, Integer usuario, Connection con) throws Exception {
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        Long valor = null;
        try {
            if (numExp != null && !"".equals(numExp)) {
                String datos[] = numExp.split("/");
                String query = "INSERT into " + ConfigurationParameter.getParameter(
                                ConstantesMeLanbide09.TABLA_E_CRO,
                                ConstantesMeLanbide09.FICHERO_PROPIEDADES)
                        + " (CRO_PRO,CRO_EJE,CRO_NUM, CRO_TRA,CRO_FEI,CRO_USU,CRO_UTR,CRO_MUN, CRO_OCU)values (?,?,?,?,SYSDATE,?,?,?,1)";
                log.debug("sql = " + query);

                ps = con.prepareStatement(query);
                  log.debug("parametro 1 = " + procedimiento);
                ps.setString(1, procedimiento);
                log.debug("parametro 2 = " + Integer.valueOf(datos[0]));
                ps.setInt(2, Integer.parseInt(datos[0]));
                log.debug("parametro 3 = " + numExp);
                ps.setString(3, numExp);
                log.debug("parametro 4 = " + codTramite);
                ps.setString(4, codTramite);
                log.debug("parametro 5 = " + usuario);
                ps.setInt(5, usuario);
                log.debug("parametro 6 = " + uor);
                ps.setString(6, uor);
                log.debug("parametro 7 = " + codOrganizacion);
                ps.setInt(7, codOrganizacion);
                rs = ps.executeQuery();
            }
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error al insertar el tr?mite " + codTramite, ex);
            throw new Exception(ex);
        } catch (SQLException ex) {
            log.error("Se ha producido un error al insertar el tr?mite " + codTramite, ex);
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
        return valor != null ? valor.toString() : null;
    }
    
    public List<ResultadoConsultaVO> getTramitesSalida(int codOrganizacion, Connection con, String procedimiento, int codigoTramite ) throws Exception {
        log.info("getExpedientesSinNotificar - Begin () " + formatDate.format(new Date()));
        List<ResultadoConsultaVO> retorno = new ArrayList<ResultadoConsultaVO>();
        PreparedStatement pt = null;
        ResultSet rs = null;
        try{
            //Dejamos preparado por si nunca se quiere exportar los resultados
            String query = "SELECT FLS_TRA,FLS_NUS,TRA_COU,FLS_CTS,TML_VALOR,SAL_OBL FROM E_FLS,e_tra,e_tml,e_sal WHERE FLS_PRO='"+procedimiento+"'	AND FLS_TRA="+codigoTramite+" AND FLS_MUN="+codOrganizacion+"	AND FLS_NUC=0 AND TRA_FBA IS	null AND e_fls.FLS_MUN=e_tra.TRA_MUN AND e_fls.FLS_PRO=e_tra.TRA_PRO AND e_fls.FLS_CTS=e_tra.TRA_COD AND e_fls.FLS_MUN=e_tml.TML_MUN AND e_fls.FLS_PRO=e_tml.TML_PRO AND e_fls.FLS_CTS=e_tml.TML_TRA AND e_tml.TML_CMP='NOM' AND e_tml.TML_LENG='1' AND e_fls.FLS_MUN=e_sal.SAL_MUN AND e_fls.FLS_PRO=e_sal.SAL_PRO AND e_fls.FLS_TRA=e_sal.SAL_TRA ORDER BY 1" ;


            
            log.info("sql = " + query);
            pt = con.prepareStatement(query);
            rs = pt.executeQuery();

            while (rs.next()) {
                ResultadoConsultaVO elementoListaRetorno = new ResultadoConsultaVO();

                
                elementoListaRetorno.setCodTramite(rs.getString("FLS_CTS"));
              
                retorno.add(elementoListaRetorno);
            }
        } catch (Exception ex) {
            log.info("Se ha producido un error consultando los registros ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (pt != null) {
                    pt.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el preparedstatement y el resulset", e);
                throw new Exception(e);
            }
        }
        log.info("getExpedientesSinNotificar - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
}
