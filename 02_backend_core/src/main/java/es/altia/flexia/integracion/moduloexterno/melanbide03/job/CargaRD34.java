package es.altia.flexia.integracion.moduloexterno.melanbide03.job;

import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide03.manager.MeLanbide03Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CertRd34RCabeceraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CertRd34VO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class CargaRD34 implements Job {
    private final Logger log = LogManager.getLogger(CargaRD34.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException
    {
        Connection con = null;
        try
        {
            int pepe = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.info("jec.getRefireCount(): " + pepe);
            Trigger pepito = jec.getTrigger();

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide03.CAMPO_SERVIDOR,ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            log.info("servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {  //PARA LOCAL QUITAR
                synchronized (jec) {
                    AdaptadorSQLBD adaptador = null;
                    try {
                        log.info("Execute lanzado CargaRD34 " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide03.COD_ORG,ConstantesMeLanbide03.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean((ConfigurationParameter.getParameter(ConstantesMeLanbide03.DOS_ENTORNOS,ConstantesMeLanbide03.FICHERO_PROPIEDADES)));

                        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
                        while (codOrg < 2) {
                            adaptador= this.getAdaptSQLBD(String.valueOf(codOrg));
                            log.info("en el while de tokens codOrg: " + codOrg);

                            ArrayList<CertRd34VO> certificadosRD34 = meLanbide03Manager.getCertificadosRD34(adaptador);
                            if (!certificadosRD34.isEmpty() && certificadosRD34!=null && certificadosRD34.size() > 0) {
                                for (CertRd34VO certificado : certificadosRD34) {
                                    try {
                                        //log.info("Iniciamos la carga de datos de certificado CEPAP a RD34 del expediente " + certificado.getNumExpediente());
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                        log.error("No se ha podido recoger el certificado del expediente " + certificado.getNumExpediente());
                                    }
                                }

                                CertRd34RCabeceraVO insertarDatosLlamadaRD34 = meLanbide03Manager.insertarDatosLlamadaRD34(certificadosRD34.size(),adaptador);

                                String xmlRD34 = meLanbide03Manager.crearXML(insertarDatosLlamadaRD34,certificadosRD34);

                                xmlRD34 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" + xmlRD34;
                                //log.info("xmlRD34 : " + xmlRD34);

                                String respuestaWSRD34 = meLanbide03Manager.getRespuestaRD34(xmlRD34);
                                //log.info("respuestaWSRD34 : " + respuestaWSRD34);

                                boolean esXML = meLanbide03Manager.isXML(respuestaWSRD34);

                                if (esXML) {
                                    //obtener datos de la respuesta
                                    CertRd34RCabeceraVO respuestaCabeceraRD34 = meLanbide03Manager.getRespuestaCabeceraRD34(certificadosRD34,respuestaWSRD34);

                                    //comprobrando numExpediente
                                    CertRd34RCabeceraVO respuestaCheckRD34 = meLanbide03Manager.checkNumExpedientes(certificadosRD34,respuestaCabeceraRD34,adaptador);

                                    //para grabar en tabla de cabecera de respuesta (se comprueba tb si coincide idEnvio, total-certificados de la llamada)
                                    boolean updateDatosCabeceraRD34 = meLanbide03Manager.updateDatosCabeceraRD34(insertarDatosLlamadaRD34,respuestaCheckRD34,adaptador);

                                    //y grabar en suplementario BOOL y en tabla de detalles de expedientes
                                    if (updateDatosCabeceraRD34) {
                                        log.info("actualizados datos de cabecera de respuesta RD34");
                                        boolean insertarDatosDetalleRD34 = meLanbide03Manager.insertarDatosDetalleRD34(insertarDatosLlamadaRD34,respuestaCheckRD34,adaptador);
                                    } else {
                                        log.error("Error actualizando datos de cabecera de respuesta RD34");
                                    }

                                } else {
                                    //log.info("No es XML el string de respuesta de RD34");
                                    boolean errorRD34 = meLanbide03Manager.updateErrorDatosCabeceraRD34(insertarDatosLlamadaRD34,respuestaWSRD34,adaptador);
                                }
                            } else {
                                log.info("No existen certificados pendientes de subir a RD34");
                            }

                            if(dosEntornos) codOrg++;
                            else codOrg = 2;
                        }
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error("Error al cerrar la conexión BBDD, Error: " + ex);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        log.error("Error en el job de CargaRD34 : ", e);
                        try {
                            throw e;
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(CargaRD34.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            con.close();
                        } catch (SQLException ex) {
                            java.util.logging.Logger.getLogger(CargaRD34.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                java.util.logging.Logger.getLogger(CargaRD34.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            } else { this.log.error("Servidor desconocido.");}    //PARA LOCAL QUITAR
        }
        catch (Exception ex)
        {
            this.log.error("Error: " + ex);
        }
    }

    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion)
            throws SQLException
    {
        if (this.log.isDebugEnabled()) {
            this.log.info("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        if (this.log.isDebugEnabled())
        {
            this.log.debug("getJndi =========> ");
            this.log.debug("parametro codOrganizacion: " + codOrganizacion);
            this.log.debug("gestor: " + gestor);
            this.log.debug("jndi: " + jndiGenerico);
        }
        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this)
        {
            try
            {
                PortableContext pc = PortableContext.getInstance();
                if (this.log.isDebugEnabled()) {
                    this.log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);

                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=4 AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }
                st.close();
                rs.close();
                conGenerico.close();
                if ((jndi != null) && (gestor != null) && (!"".equals(jndi)) && (!"".equals(gestor)))
                {
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }
            }
            catch (TechnicalException te)
            {
                te.printStackTrace();
                this.log.error("*** AdaptadorSQLBD: " + te.toString());
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if ((conGenerico != null) && (!conGenerico.isClosed())) {
                    conGenerico.close();
                }
            }
            if (this.log.isDebugEnabled()) {
                this.log.debug("getConnection() : END");
            }
        }
        return adapt;
    }
}

