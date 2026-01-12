/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
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

public class gestorExpedientesATASEissue428911 implements Job{
    
    //protected static Config conf =ConfigServiceHelper.getConfig("notificaciones");    
    
    private static final Logger LOG = LogManager.getLogger(gestorExpedientesATASEissue428911.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            LOG.info(this.getClass().getName() + ".execute -  Inicio " + new Date().toString());
            int contador = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            LOG.info("jec.getRefireCount(): " + contador);
            Trigger trigger = jec.getTrigger();

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);

            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            LOG.info("servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    
                    Connection con = null;
                    boolean dev = false;
                    int numIntentos = 0;
                    String numExpediente = "";
                    int id = 0;
                    String[] params = new String[]{"ORACLE"};
                    try {
                        LOG.info("Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS",
                                ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                        while (codOrg < 2) {
//                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
//                            AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
//                            con = adaptador.getConnection();
                            LOG.info("en el while de tokens codOrg: - vamos a invocar la clase  AppAportacionesPendientes_Ibermatica " + codOrg);
                            AppAportacionesPendientes_Ibermatica aportacionesPendientes_Ibermatica = new AppAportacionesPendientes_Ibermatica();
                            LOG.info("Creamos una instancia .. Y Procedemos a invocar el main ");
                            aportacionesPendientes_Ibermatica.main(new String[1]);
                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }
//                            if (con != null) {
//                                con.close();
//                            }

                        }
                    } catch (Exception e) {
                        LOG.error(gestorExpedientesATASEissue428911.class.getName() + " Error en el job : ", e);
                        try {
                            int intentos = numIntentos + 1;
                        } catch (Exception i) {
                            LOG.error("Error en int intentos = numIntentos + 1 : " + i.getMessage());
                        }
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                LOG.error(gestorExpedientesATASEissue428911.class.getName()+" - Error cerrando BBDD", ex);
                            }
                        }
                    }
                }//para local quitar
            }
        LOG.info(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
        } catch (Exception ex) {
            LOG.error(this.getClass().getName()  + " Error: " + ex);
        }
    }    
}
