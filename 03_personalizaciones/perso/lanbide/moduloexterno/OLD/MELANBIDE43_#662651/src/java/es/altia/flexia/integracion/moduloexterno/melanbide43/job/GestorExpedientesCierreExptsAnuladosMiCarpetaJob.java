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
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpedienteCierreAnuladosMCVO;
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


public class GestorExpedientesCierreExptsAnuladosMiCarpetaJob implements Job{
    
    private final Logger log = Logger.getLogger(GestorExpedientesCierreExptsAnuladosMiCarpetaJob.class);
    


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
                            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
                                                        
                            List<ExpedienteCierreAnuladosMCVO> expedientes = this.getExpedientesACerrarEnMiCarpeta(con);

                                log.info("numexpedientes  a Tratar :" + expedientes.size());

                            for (Iterator<ExpedienteCierreAnuladosMCVO> i = expedientes.iterator(); i.hasNext();) {
                                ExpedienteCierreAnuladosMCVO item = i.next();
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
                        log.error(GestorExpedientesCierreExptsAnuladosMiCarpetaJob.class.getName() + " Error en el job : ", e);
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
                                log.error(GestorExpedientesCierreExptsAnuladosMiCarpetaJob.class.getName()+" - Error cerrando BBDD", ex);
                            }
                        }
                    }
                }
            
        log.error(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
            }} catch (Exception ex) {
            log.error(this.getClass().getName()  + " Error: "+ex.getMessage(),ex);
        }
    }
    
    private List<ExpedienteCierreAnuladosMCVO> getExpedientesACerrarEnMiCarpeta(Connection con) throws Exception {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String numExp = null;
        int codTram = 0;
        List<ExpedienteCierreAnuladosMCVO> expedientes = new ArrayList<ExpedienteCierreAnuladosMCVO>();

        try {
            // -- 1-Anulado  9-Finalizado 0-Pendiente\n / Cambia el cod de tramite en la SQL Superior por el de Cierre para recoger la fase correcta.
            String sql = "select distinct EXP_PRO, EXP_EJE, EXP_NUM, EXP_FEI, EXP_FEF, EXP_EST, EXP_MUN, EXP_USU, EXP_UOR, EXP_PEND, EXP_TRA, EXP_TOCU, EXP_LOC, EXP_CLO, EXP_OBS, EXP_ASU, EXP_REF, EXP_IMP, EXP_FPA, EXP_UBICACION_DOC  "
                        +" , (SELECT MAX(COD_TRAMITE) FROM MELANBIDE43_FASE WHERE COD_FASE='99' AND COD_PROC=EXP_PRO) AS CRO_TRA , 1 as CRO_OCU "
                        +" , CODIGO_MAXTRA, NUMERO_TRA_ABIERTOS, TIENE_TRAMITE_ALTA_PERSONAL  "
                        +" from ( "
                        +" SELECT distinct e.*  "
                        +" ,cro_tra,cro_ocu  "
                        +" ,max(t.cro_tra) over(partition by t.cro_num) codigo_maxTra  "
                        +" ,count(t.cro_tra) over(partition by t.cro_num) numero_tra_abiertos  "
                        +" ,sum(case when dt.tra_cou=400 then 1 else 0 end)  over(partition by t.cro_num) tiene_tramite_alta_personal  " 
                        +" FROM  "
                        +" E_EXP E "
                        +" inner JOIN melanbide43_integmisgest mg ON  mg.EXp_NUM=E.EXP_NUM and MG.tipo_operacion='I' and MG.RESULTADO_PROCESO=1 AND E.EXp_NUM NOT IN (SELECT EXp_NUM FROM melanbide43_integmisgest WHERE EXp_NUM=E.exp_num and tipo_operacion='C' and RESULTADO_PROCESO=1) " 
                        +" left join e_cro t on t.cro_pro=e.exp_pro and t.cro_eje=e.exp_eje and t.cro_num=e.exp_num   "
                        +" left join e_tra dt on dt.tra_pro=t.cro_pro and dt.tra_cod=t.cro_tra   "
                        +" WHERE  "
                        +" E.EXP_EST=1 "
                    //Quitar para buscar bien 
                     //   +" AND E.EXP_EJE=2023 "
                        +" ORDER BY e.exp_pro,E.EXP_NUM DESC  "
                        +" ) datos  "
                        +" where DATOS.CRO_TRA=DATOS.CODIGO_MAXTRA  "
                    //Quitar para pro y pre que no habra ninguno mal    
                  //  + " and EXP_TRA IS NOT NULL "
                    
                        +" order by exp_pro,EXP_NUM DESC";
            log.info("sql = " + sql);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ExpedienteCierreAnuladosMCVO expte = new ExpedienteCierreAnuladosMCVO();
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
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - JOB Cierre Expedientes en Mi Carpeta", e);
            }
        }
        return expedientes;
    }


}
