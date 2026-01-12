/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43;
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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 *
 * @author INGDGC
 */
public class CierreListaExptsMiCarpetaJob implements Job {

    private final Logger log = LogManager.getLogger(CierreListaExptsMiCarpetaJob.class);

    private class ExpedienteCierreMCVO {

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
                    String numExpediente = "";
                                        int id = 0;
                    String[] params = new String[]{"ORACLE"};
                    try {
                        log.info(this.getClass().getSimpleName() + " Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS",
                                ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.info(" En el while de tokens codOrg: " + codOrg);

                            List<ExpedienteCierreMCVO> expedientes = this.getExpedientesTRECOCerradosEnProcedure(con);
                            log.info("numexpedientes  a Tratar :" + expedientes.size());

                         for (Iterator<ExpedienteCierreMCVO> i = expedientes.iterator(); i.hasNext();) {
                                ExpedienteCierreMCVO item = i.next();
                                numExpediente = item.getNumeroExpediente();
                                log.info("numexpediente:" + numExpediente);
                                //LAMAR A MIS GESTIONES CIERRE EXPEDIENTE                            
                                MELANBIDE43 m43 = new MELANBIDE43();
                                String resultado = "";
                                try {
                                    resultado = m43.generarMisGestCierre(codOrg, item.getCodTramiteActal(), item.getOcurrenciaTramiteActual(), numExpediente);
                                } catch (Exception e) {
                                    log.error(" Error al Cerrar el expediente " + numExpediente + " en Mi Carperta : " + e.getMessage(), e);
                                }
                                log.info("Respuesta de la llamada expediente " + numExpediente + " : " + resultado);
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
                        log.error(CierreListaExptsMiCarpetaJob.class.getName() + " Error en el job : ", e);
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error(CierreListaExptsMiCarpetaJob.class.getName() + " - Error cerrando BBDD", ex);
                            }
                        }
                    }
                }
            }
            log.error(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error: " + ex.getMessage(), ex);
        }
    }

    private List<ExpedienteCierreMCVO> getExpedientesTRECOCerradosEnProcedure(Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String numExp = null;
        List<ExpedienteCierreMCVO> expedientes = new ArrayList<ExpedienteCierreMCVO>();
        try {
            String sql = "select  e.exp_pro PROCEDIMIENTO, e.exp_eje EJERCICIO, e.exp_num EXPEDIENTE, e.exp_mun COD_ORG from E_EXP e "
                    + " inner JOIN melanbide43_integmisgest mg ON  mg.EXp_NUM=E.EXP_NUM and MG.tipo_operacion='I' and MG.RESULTADO_PROCESO=1 and MG.COD_TRAMITE_INICIO=1 "
                    + " WHERE e.exp_est = 9"
                    + " AND e.exp_pro ='TRECO'"
                    + " AND  e.EXP_FEF > '02/09/2024' and e.EXP_FEF < '03/09/2024'"
                    + " and  E.EXP_NUM not in(select GES.EXP_NUM from MELANBIDE43_INTEGMISGEST GES where GES.EXP_NUM = E.EXP_NUM and GES.TIPO_OPERACION = 'C' and GES.RESULTADO_PROCESO = 1)"
                    + " ORDER BY e.exp_num";
            log.info("sql = " + sql);

            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                ExpedienteCierreMCVO expte = new ExpedienteCierreMCVO();
                numExp = rs.getString("EXPEDIENTE");
                log.info("numExp = " + numExp);
                expte.setCodOrganizacion(rs.getInt("COD_ORG"));
                expte.setCodProcedimiento(rs.getString("PROCEDIMIENTO"));
                expte.setCodTramiteActal(28);
                expte.setEjercicio(rs.getInt("EJERCICIO"));
                expte.setNumeroExpediente(rs.getString("EXPEDIENTE"));
                expte.setOcurrenciaTramiteActual(1);
                expedientes.add(expte);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los expedientes en el Job de cierre en mi Carpeta  ", ex);
            throw ex;
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return expedientes;
    }

}
