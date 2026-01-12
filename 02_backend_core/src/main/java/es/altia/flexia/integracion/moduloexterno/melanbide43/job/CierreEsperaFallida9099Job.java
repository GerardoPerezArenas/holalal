package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tram9099CerrarVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoParamAdicionales;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class CierreEsperaFallida9099Job implements Job{
    
      private final Logger log = LogManager.getLogger(CierreEsperaFallida9099Job.class);

     @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int contador = jec.getRefireCount();
            log.info(this.getClass().getName() + " jec.getRefireCount(): " + contador);
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            log.info(this.getClass().getName() + " servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    
                    Connection con = null;
                    int numIntentos = 0;
                    try {
                        log.info(this.getClass().getSimpleName()+" CierreEsperaFallida9099Job - Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.info("CierreEsperaFallida9099Job - En el while de codOrg: " + codOrg);

                            //expedientes que se trataron por job CierreTramiteEspera9099Job anterior pero no se cerraron en MiCarpeta
                            List<Tram9099CerrarVO> expedientes = MeLanbide43Manager.getInstance().getExpedientesFallidos9099(adaptador);


                            //Recorremos los expedientes a cerrar
                            for (Iterator<Tram9099CerrarVO> x = expedientes.iterator(); x.hasNext();) {
                                Tram9099CerrarVO expediente = x.next();
                                log.info("CierreEsperaFallida9099Job - Tratamos el expediente "+ expediente.getNumExpediente());

                                //Procedemos a realizar el cierre de la espera
                                MELANBIDE43 melanbide43 = new MELANBIDE43();
                                ModuloIntegracionExternoParamAdicionales adicionales = new ModuloIntegracionExternoParamAdicionales();
                                adicionales.setOrigenLlamada("IN");
                                String resultadoCerrarEspera = "1";
                                try {
                                    //resultadoCerrarEspera =  melanbide43.cerrarEsperaMisGest(codOrg, expediente.getCodTramite(), expediente.getOcuTramite(), expediente.getNumExpediente(), adicionales);
                                    resultadoCerrarEspera =  melanbide43.cerrarEspRecursoStd(codOrg, expediente.getCodTramite(), expediente.getOcuTramite(), expediente.getNumExpediente(), adicionales);
                                } catch (Exception e) {

                                }
                                if (resultadoCerrarEspera.equalsIgnoreCase("0")){
                                    log.info("CierreEsperaFallida9099Job - Cerrada la espera para el expediente: "+expediente.getNumExpediente());
                                } else {
                                    log.error("CierreEsperaFallida9099Job - Error al cerrar la espera para el expediente: "+expediente.getNumExpediente());
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
                        log.error(CierreEsperaFallida9099Job.class.getName() + " Error en el job : ", e);
                        try {
                            int intentos = numIntentos + 1;
                        } catch (Exception i) {
                            log.error("Error en CierreEsperaFallida9099Job int intentos = numIntentos + 1 : " + i.getMessage());
                        }
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error(CierreEsperaFallida9099Job.class.getName()+" - Error CierreEsperaFallida9099Job cerrando BBDD", ex);
                            }
                        }
                    }
                }
            
                log.info(this.getClass().getName() + ".execute CierreEsperaFallida9099Job -  Fin " + new Date().toString());
            }//PARA LOCAL QUITAR
        } catch (Exception ex) {
            log.error(this.getClass().getName()  + " Error: "+ex.getMessage(),ex);
        }
        
    }

}
