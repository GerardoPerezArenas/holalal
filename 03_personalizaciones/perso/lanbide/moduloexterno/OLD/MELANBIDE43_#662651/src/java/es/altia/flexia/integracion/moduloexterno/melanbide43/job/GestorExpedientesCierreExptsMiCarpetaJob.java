/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 *
 * @author INGDGC
 */

public class GestorExpedientesCierreExptsMiCarpetaJob implements Job{
    
    private final Logger log = Logger.getLogger(GestorExpedientesCierreExptsMiCarpetaJob.class);
    
    private class DatosCierreMCVO{
        private String procedimiento;
        private int ejercicioLimite;
        private int tramiteInicio;
        private int tramiteCierre;

        public String getProcedimiento() {
            return procedimiento;
        }

        public void setProcedimiento(String procedimiento) {
            this.procedimiento = procedimiento;
        }

        public int getEjercicioLimite() {
            return ejercicioLimite;
        }

        public void setEjercicioLimite(int ejercicioLimite) {
            this.ejercicioLimite = ejercicioLimite;
        }

        public int getTramiteInicio() {
            return tramiteInicio;
        }

        public void setTramiteInicio(int tramiteInicio) {
            this.tramiteInicio = tramiteInicio;
        }

        public int getTramiteCierre() {
            return tramiteCierre;
        }

        public void setTramiteCierre(int tramiteCierre) {
            this.tramiteCierre = tramiteCierre;
        }
        
    }
    
    private class ExpedienteCierreMCVO{
        private int codOrganizacion;
        private int ejercicio;
        private String codProcedimiento;
        private String numeroExpediente;
        private int codTramiteActal;
        private int ocurrenciaTramiteActual;

        public int getCodOrganizacion() {
            return codOrganizacion;
        }

        public void setCodOrganizacion(int codOrganizacion) {
            this.codOrganizacion = codOrganizacion;
        }

        public int getEjercicio() {
            return ejercicio;
        }

        public void setEjercicio(int ejercicio) {
            this.ejercicio = ejercicio;
        }

        public String getCodProcedimiento() {
            return codProcedimiento;
        }

        public void setCodProcedimiento(String codProcedimiento) {
            this.codProcedimiento = codProcedimiento;
        }

        public String getNumeroExpediente() {
            return numeroExpediente;
        }

        public void setNumeroExpediente(String numeroExpediente) {
            this.numeroExpediente = numeroExpediente;
        }

        public int getCodTramiteActal() {
            return codTramiteActal;
        }

        public void setCodTramiteActal(int codTramiteActal) {
            this.codTramiteActal = codTramiteActal;
        }

        public int getOcurrenciaTramiteActual() {
            return ocurrenciaTramiteActual;
        }

        public void setOcurrenciaTramiteActual(int ocurrenciaTramiteActual) {
            this.ocurrenciaTramiteActual = ocurrenciaTramiteActual;
        }
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int contador = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.info(this.getClass().getName() + " jec.getRefireCount(): " + contador);
            Trigger trigger = jec.getTrigger();

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);

            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.info(this.getClass().getName() + " servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    
                    Connection con = null;
                    boolean dev = false;
                    int numIntentos = 0;
                    String numExpediente = "";
                    int id = 0;
                    String[] params = new String[]{"ORACLE"};
                    try {
                        log.info(this.getClass().getSimpleName()+" Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS",
                                ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.info(" En el while de tokens codOrg: " + codOrg);
                           
                            DatosCierreMCVO datosCierreMC = this.getDatosCerrarMC(con);
                            
                            List<ExpedienteCierreMCVO> expedientes = this.getExpedientesACerrarEnMiCarpeta(datosCierreMC,con);
//                            List<ExpedienteCierreMCVO> expedientes = this.getExpedientesCONCMACerrarEnMiCarpeta(con);
                                log.info("numexpedientes  a Tratar :" + expedientes.size());

                            for (Iterator<ExpedienteCierreMCVO> i = expedientes.iterator(); i.hasNext();) {
                                ExpedienteCierreMCVO item = i.next();
                                numExpediente = item.getNumeroExpediente();
                                log.info("numexpediente:" + numExpediente);
                                //LAMAR A MIS GESTIONES CIERRE EXPEDIENTE                            
                                MELANBIDE43 m43 = new MELANBIDE43();
                                String resultado="";
                                try {
                                    resultado = m43.generarMisGestCierre(codOrg,item.getCodTramiteActal(),item.getOcurrenciaTramiteActual(),numExpediente);                                    
                                } catch (Exception e) {
                                    log.error(" Error al Cerrar el expediente "+ numExpediente +" en Mi Carperta : " + e.getMessage(),e);
                                }
                                log.info("Respuesta de la llamada expediente "+numExpediente+" : " +resultado);
                            }
                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }
                            if (con != null) {
                                con.close();
                            }

                        }
                    } catch (Exception e) {
                        log.error(GestorExpedientesCierreExptsMiCarpetaJob.class.getName() + " Error en el job : ", e);
                        try {
                            int intentos = numIntentos + 1;
                        } catch (Exception i) {
                            log.error("Error en int intentos = numIntentos + 1 : " + i.getMessage());
                        }
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error(GestorExpedientesCierreExptsMiCarpetaJob.class.getName()+" - Error cerrando BBDD", ex);
                            }
                        }
                    }
                }
            }
        log.error(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
        } catch (Exception ex) {
            log.error(this.getClass().getName()  + " Error: "+ex.getMessage(),ex);
        }
    }
    
    /*private List<ExpedienteCierreMCVO> getExpedientesACerrarEnMiCarpeta(Connection con) throws Exception {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String numExp = null;
        int codTram = 0;
        List<ExpedienteCierreMCVO> expedientes = new ArrayList<ExpedienteCierreMCVO>();

        try {
            // -- 1-Anulado  9-Finalizado 0-Pendiente\n / Cambia el cod de tramite en la SQL Superior por el de Cierre para recoger la fase correcta.
            String sql = "select distinct EXP_PRO, EXP_EJE, EXP_NUM, EXP_FEI, EXP_FEF, EXP_EST, EXP_MUN, EXP_USU, EXP_UOR, EXP_PEND, EXP_TRA, EXP_TOCU, EXP_LOC, EXP_CLO, EXP_OBS, EXP_ASU, EXP_REF, EXP_IMP, EXP_FPA, EXP_UBICACION_DOC " +
                        " , (SELECT TRA_COD FROM E_TRA WHERE TRA_COU='999' AND TRA_PRO=EXP_PRO) as CRO_TRA, 1 as CRO_OCU "+ 
                        " , TML_VALOR, CODIGO_MAXTRA, NUMERO_TRA_ABIERTOS, TIENE_TRAMITE_ALTA_PERSONAL " +
                        " from (" +
                        " SELECT distinct e.* " +
                        " ,cro_tra,cro_ocu,tml_valor " +
                        " ,max(t.cro_tra) over(partition by t.cro_num) codigo_maxTra " +
                        " ,count(t.cro_tra) over(partition by t.cro_num) numero_tra_abiertos " +
                        " ,sum(case when dt.tra_cou=400 then 1 else 0 end)  over(partition by t.cro_num) tiene_tramite_alta_personal " +
                        " FROM " +
                        " E_EXP E" +
                        " inner JOIN melanbide43_integmisgest mg ON  mg.EXp_NUM=E.EXP_NUM and MG.tipo_operacion='I' and MG.RESULTADO_PROCESO=1 and MG.COD_TRAMITE_INICIO=1 " +
                        " left join e_cro t on t.cro_pro=e.exp_pro and t.cro_eje=e.exp_eje and t.cro_num=e.exp_num  and t.cro_fef is null " +
                        " left join e_tra dt on dt.tra_pro=t.cro_pro and dt.tra_cod=t.cro_tra " +
                        " LEFT JOIN E_TML ON tml_pro=t.cro_PRO AND tml_tra=t.cro_tra " +
                        " WHERE " +
                        " E.EXP_EST=0 " +
                        " AND ( " +
                        "  E.EXP_PRO='UAAP' " +
//                        "  E.EXP_PRO='UAAP' " +
//                        "  OR E.EXP_PRO='ORI14' " +
//                        "  OR E.EXP_PRO='CEMP' " +
//                        "  OR E.EXP_PRO='COLEC' " +
                        "  ) " +
                        " AND E.EXP_EJE=2020 " +
                        " and t.cro_fef is null " +
                        " ORDER BY e.exp_pro,E.EXP_NUM DESC " +
                        " ) datos " +
                        " where DATOS.CRO_TRA=DATOS.CODIGO_MAXTRA " +
//                        " and TIENE_TRAMITE_ALTA_PERSONAL=1 " +
                        " order by exp_pro,EXP_NUM DESC";
            log.info("sql = " + sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExpedienteCierreMCVO expte = new ExpedienteCierreMCVO();
                numExp = rs.getString("EXP_NUM");
                log.info("numExp = " + numExp);
                expte.setCodOrganizacion(rs.getInt("exp_mun"));
                expte.setCodProcedimiento(rs.getString("exp_PRO"));
                expte.setCodTramiteActal(rs.getInt("cro_tra"));
                expte.setEjercicio(rs.getInt("exp_eje"));
                expte.setNumeroExpediente(rs.getString("exp_num"));
                expte.setOcurrenciaTramiteActual(rs.getInt("cro_ocu"));
                expedientes.add(expte);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los expedientes en el Job de cierre en mi Carpeta  ", ex);
            throw ex;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.info("Procedemos a cerrar el statement y el resultset - JOB Cierre Expedientes en Mi Carpeta");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - JOB Cierre Expedientes en Mi Carpeta", e);
            }
        }
        return expedientes;
    }*/

    private List<ExpedienteCierreMCVO> getExpedientesCONCMACerrarEnMiCarpeta(Connection con) throws Exception {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String numExp = null;
        List<ExpedienteCierreMCVO> expedientes = new ArrayList<ExpedienteCierreMCVO>();

        try {
            // -- 1-Anulado  9-Finalizado 0-Pendiente - Cambia el cod de tramite en la SQL Superior por el de Cierre para recoger la fase correcta.
            String sql = "SELECT DISTINCT exp_pro, exp_eje, exp_num, exp_fei, exp_fef, exp_est, exp_mun, exp_usu, exp_uor, exp_pend, exp_tra, exp_tocu, exp_loc, exp_clo, exp_obs, exp_asu, exp_ref, exp_imp, exp_fpa, exp_ubicacion_doc "
                    + ", (SELECT tra_cod FROM e_tra WHERE tra_cou='801' AND tra_pro=exp_pro) AS cro_tra, 1 AS cro_ocu"
                    + ", tml_valor, codigo_maxtra, numero_tra_abiertos"
                    + " FROM (SELECT DISTINCT e.*"
                    + ", cro_tra,cro_ocu,tml_valor"
                    + ", MAX(t.cro_tra) OVER(PARTITION BY t.cro_num) codigo_maxtra"
                    + ", COUNT(t.cro_tra) OVER(PARTITION BY t.cro_num) numero_tra_abiertos"
                    + " FROM e_exp e"
                    + " INNER JOIN melanbide43_integmisgest mg ON mg.exp_num=e.exp_num AND mg.tipo_operacion='I' AND mg.resultado_proceso=1 AND mg.cod_tramite_inicio=1"
                    + " LEFT JOIN e_cro t ON t.cro_pro=e.exp_pro AND t.cro_eje=e.exp_eje AND t.cro_num=e.exp_num AND t.cro_mun=e.exp_mun AND t.cro_fef IS NULL"
                    + " LEFT JOIN e_tra dt ON dt.tra_pro=t.cro_pro AND dt.tra_cod=t.cro_tra AND dt.tra_mun=t.cro_mun"
                    + " LEFT JOIN e_tml ON tml_pro=t.cro_pro AND tml_tra=t.cro_tra AND tml_mun=t.cro_mun"
                    + " WHERE e.exp_est = 0"
                    + " AND e.exp_pro ='CONCM'"
                    + " AND e.exp_eje < 2020"
                    + " AND t.cro_fef IS NULL"
                    + " ORDER BY e.exp_num DESC"
                    + " ) datos"
                    + " WHERE datos.cro_tra=datos.codigo_maxtra"
                    + " ORDER BY exp_num DESC";
            log.info("sql = " + sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExpedienteCierreMCVO expte = new ExpedienteCierreMCVO();
                numExp = rs.getString("exp_num");
                log.info("numExp = " + numExp);
                expte.setCodOrganizacion(rs.getInt("exp_mun"));
                expte.setCodProcedimiento(rs.getString("exp_pro"));
                expte.setCodTramiteActal(rs.getInt("cro_tra"));
                expte.setEjercicio(rs.getInt("exp_eje"));
                expte.setNumeroExpediente(rs.getString("exp_num"));
                expte.setOcurrenciaTramiteActual(rs.getInt("cro_ocu"));
                expedientes.add(expte);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los expedientes en el Job de cierre en mi Carpeta  ", ex);
            throw ex;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.info("Procedemos a cerrar el statement y el resultset - JOB Cierre Expedientes en Mi Carpeta");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - JOB Cierre Expedientes en Mi Carpeta", e);
            }
        }
        return expedientes;
    }
    
    private DatosCierreMCVO getDatosCerrarMC(Connection con) throws Exception {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DatosCierreMCVO datosCierreMC = new DatosCierreMCVO();

        try {
            String sql = "select procedimiento,eje_limite,tra_inicio,tra_cierre from exp_pro_eje_cerrar_micarpeta where rownum=1";
            log.info("sql datosCierreMC = " + sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                datosCierreMC.setProcedimiento(rs.getString("procedimiento"));
                datosCierreMC.setEjercicioLimite(rs.getInt("eje_limite"));
                datosCierreMC.setTramiteInicio(rs.getInt("tra_inicio"));
                datosCierreMC.setTramiteCierre(rs.getInt("tra_cierre"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los expedientes en el Job de cierre en mi Carpeta - getDatosCerrarMC ", ex);
            throw ex;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.info("Procedemos a cerrar el statement y el resultset - JOB Cierre Expedientes en Mi Carpeta - getDatosCerrarMC");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - JOB Cierre Expedientes en Mi Carpeta - getDatosCerrarMC", e);
            }
        }
        return datosCierreMC;
    }
    
    private List<ExpedienteCierreMCVO> getExpedientesACerrarEnMiCarpeta(DatosCierreMCVO datosCierreMC,Connection con) throws Exception {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String numExp = null;
        List<ExpedienteCierreMCVO> expedientes = new ArrayList<ExpedienteCierreMCVO>();
        
        /*String procedimiento = "CUOTS";
        int ejercicioLimite = 2019;
        int tramiteCierre = 998;*/
        
        //trámite INICIO EXPEDIENTE
            //DESA - 80
            //int tramiteInicio = 80;
            //PRE, PRO - 2
            //int tramiteInicio = 2;
        
        String procedimiento = datosCierreMC.getProcedimiento();
        int ejercicioLimite = datosCierreMC.getEjercicioLimite();
        int tramiteInicio = datosCierreMC.getTramiteInicio();
        int tramiteCierre = datosCierreMC.getTramiteCierre();
          
        try {
            String sql = "select distinct exp_pro,exp_eje,exp_num,exp_fei,exp_fef,exp_est,exp_mun,exp_usu,exp_uor,exp_pend,exp_tra,exp_tocu,exp_loc,exp_clo,exp_obs,exp_asu,exp_ref,exp_imp,exp_fpa,exp_ubicacion_doc \n" +
                "    ,(select tra_cod from e_tra where tra_cou=" + tramiteCierre +" and tra_pro=exp_pro) as cro_tra, 1 as cro_ocu \n" +
                "    ,tml_valor,codigo_maxtra,numero_tra_abiertos\n" +
                "from (select distinct e.* \n" +
                "        ,cro_tra,cro_ocu,tml_valor \n" +
                "        ,MAX(t.cro_tra) OVER(PARTITION BY t.cro_num) codigo_maxtra \n" +
                "        ,COUNT(t.cro_tra) OVER(PARTITION BY t.cro_num) numero_tra_abiertos\n" +
                "      from e_exp e\n" +
                "        inner join melanbide43_integmisgest mg on  mg.exp_num=e.exp_num and mg.tipo_operacion='I' and mg.resultado_proceso=1 and mg.cod_tramite_inicio=" + tramiteInicio +"\n" +
                "        left join e_cro t on t.cro_pro=e.exp_pro and t.cro_eje=e.exp_eje and t.cro_num=e.exp_num and t.cro_mun=e.exp_mun and t.cro_fef IS NULL \n" +
                "        left join e_tra dt on dt.tra_pro=t.cro_pro and dt.tra_cod=t.cro_tra and dt.tra_mun=t.cro_mun\n" +
                "        left join e_tml on tml_pro=t.cro_pro and tml_tra=t.cro_tra and tml_mun=t.cro_mun\n" +
                "      where e.exp_est = 0 \n" +
                "      and e.exp_pro = '" + procedimiento +"'\n" +
                "      and e.exp_eje < " + ejercicioLimite + "\n" +
                "      and t.cro_fef IS NULL \n" +
                "      order by e.exp_num \n" +
                "     ) datos \n" +
                "where datos.cro_tra=datos.codigo_maxtra \n" +
                "order by exp_num";
            log.info("sql = " + sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExpedienteCierreMCVO expte = new ExpedienteCierreMCVO();
                numExp = rs.getString("exp_num");
                log.info("numExp = " + numExp);
                expte.setCodOrganizacion(rs.getInt("exp_mun"));
                expte.setCodProcedimiento(rs.getString("exp_pro"));
                expte.setCodTramiteActal(rs.getInt("cro_tra"));
                expte.setEjercicio(rs.getInt("exp_eje"));
                expte.setNumeroExpediente(rs.getString("exp_num"));
                expte.setOcurrenciaTramiteActual(rs.getInt("cro_ocu"));
                expedientes.add(expte);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los expedientes en el Job de cierre en mi Carpeta  ", ex);
            throw ex;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.info("Procedemos a cerrar el statement y el resultset - JOB Cierre Expedientes en Mi Carpeta");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - JOB Cierre Expedientes en Mi Carpeta", e);
            }
        }
        return expedientes;
    }

}
