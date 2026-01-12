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
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpTram;
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

/**
 *
 * @author INGDGC
 */

public class GestorExpedientesAERTEIssue462107Esperas implements Job{

    private final Logger log = LogManager.getLogger(GestorExpedientesAERTEIssue462107Esperas.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int contador = jec.getRefireCount();
            //Job trab = jec.getJobInstance();
            log.info("jec.getRefireCount(): " + contador);
            //Trigger trigger = jec.getTrigger();
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);

            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.info("servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    
                    Connection con = null;
                    String numExpediente = "";
                    try {
                        log.info("Job lanzado : " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS",
                                ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.info("While de tokens codOrg: " + codOrg);
                            List<ExpTram> expedientes = this.getExpedientesAERTEAbreRenunciaRecursoCierreDesistir(con);
                                log.info("Numexpedientes Tratar :" + expedientes!=null ? expedientes.size(): 0);
                            for (Iterator<ExpTram> i = expedientes.iterator(); i.hasNext();) {
                                ExpTram item = i.next();
                                numExpediente = item.getNumExpediente();
                                log.info("numexpediente:" + numExpediente);
                                MELANBIDE43 melanbide43 = new MELANBIDE43();
                                String respuestaAbrEspRenCierreDes = melanbide43.abrirEsperaRenunciaCerrarEsperaDesestimiento(codOrg,item.getCodTramite(),1,numExpediente);
                                if(respuestaAbrEspRenCierreDes!=null && respuestaAbrEspRenCierreDes.equalsIgnoreCase("0")){
                                    // Si hemos cerrado el desistimiento, abrimos la de recursos
                                    String codTramitePresentacionRecursos=ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_AERTE_ESPERA_PRESENTACION_RECURSOS, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                                    String respuestaAbrirEsperaRecurso = melanbide43.abrirEsperaPresentacionRecursoReposicion(codOrg
                                            ,(codTramitePresentacionRecursos!=null && !codTramitePresentacionRecursos.isEmpty() ? Integer.valueOf(codTramitePresentacionRecursos):0)
                                            ,1,numExpediente);
                                    log.info( respuestaAbrirEsperaRecurso != null && respuestaAbrirEsperaRecurso.equalsIgnoreCase("0") ? 
                                            "Apertura Recurso ejecutada correctamente."
                                            :
                                            "Error al ejecutar la apertura del recurso. Expediente: " + numExpediente
                                    );
                                }else{
                                    log.error("No se ha podido realizar la operacion de apertura/cierre esperas Renuncia/Desistimiento.");
                                }
                                
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
                        log.error(GestorExpedientesAERTEIssue462107Esperas.class.getName() 
                                + " Error en el job : " + e.getMessage(), e);
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error(GestorExpedientesAERTEIssue462107Esperas.class.getName()+" - Error cerrando BBDD", ex);
                            }
                        }
                    }
                }//para local quitar
            }
        log.info(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(this.getClass().getName()  + " Error: " + ex);
        }
    }
        
    private List<ExpTram> getExpedientesAERTEAbreRenunciaRecursoCierreDesistir(Connection con) throws Exception {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String numExp = null;
        int codTram = 0;
        List<ExpTram> expedientes = new ArrayList<ExpTram>();
        try {
            StringBuilder sb = new StringBuilder("SELECT NUM_EXP ");
            sb.append(" FROM CEPAP_MIGRACION_EXCEL   ");
            sb.append(" WHERE NUM_TAREA=462107 ");
            sb.append(" ORDER BY NUM_EXP");
            log.info("sql = " + sb.toString());
            ps = con.prepareStatement(sb.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                numExp = rs.getString("NUM_EXP");
                expedientes.add(new ExpTram(numExp, codTram));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los expedientes AERTE - Batch Apertura de Esperas : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                log.info("Procedemos a cerrar el statement y el resultset - JOB Esperas AERTE -");
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - JOB Esperas AERTE - " + e.getMessage(), e);
            }
        }
        return expedientes;
    }

}
