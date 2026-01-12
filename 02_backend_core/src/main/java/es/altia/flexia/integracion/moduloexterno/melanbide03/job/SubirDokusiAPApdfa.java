package es.altia.flexia.integracion.moduloexterno.melanbide03.job;

import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide03.manager.MeLanbide03Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.MeLanbide03ReportVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class SubirDokusiAPApdfa implements Job {
    private final Logger log = LogManager.getLogger(SubirDokusiAPApdfa.class);

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
                    try
                    {
                        log.info("Execute lanzado SubirDokusiAPApdfa " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide03.COD_ORG,ConstantesMeLanbide03.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean((ConfigurationParameter.getParameter(ConstantesMeLanbide03.DOS_ENTORNOS,ConstantesMeLanbide03.FICHERO_PROPIEDADES)));

                        MeLanbide03Manager meLanbide03Manager = MeLanbide03Manager.getInstance();
                        while (codOrg < 2) {
                            adaptador= this.getAdaptSQLBD(String.valueOf(codOrg));
                            log.info("en el while de tokens codOrg: " + codOrg);

                            ArrayList<MeLanbide03ReportVO> registros = meLanbide03Manager.getReportsNoDokusi(Integer.valueOf(codOrg), adaptador);
                            if (!registros.isEmpty()) {
                                for (MeLanbide03ReportVO registro : registros) {
                                    try
                                    {
                                        log.info("Iniciamos la subida del documento " + registro.getNombre() + " del expediente " + registro.getNumExpediente());
                                        //String idProcedimiento = Lan6Config.getProperty("AdaptadoresPlateaLan6.properties", "PROCEDIMIENTO_ID_LAN11_CEPAP");
//                                        String procedimientoPlatea = "LAN11_CEPAP";
//                                        String idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_"+procedimientoPlatea);
                                        Lan6DokusiServicios servicios = new Lan6DokusiServicios("CEPAP"); // 2.2


                                        Lan6Documento lan6Documento = new Lan6Documento();

                                        lan6Documento.setTipoDocumental("ejgv_d_certificado");
                                        lan6Documento.setOrigen("ADMINISTRACION");
                                        lan6Documento.setTitulo(registro.getNombre());
                                        lan6Documento.setNombre(registro.getNombre());

                                        lan6Documento.setFormat("pdf");
                                        lan6Documento.setConvertirAPdf(true);
                                        lan6Documento.setSincrono(true);
                                        lan6Documento.setNaturaleza("ELECTRONICO");


                                        MeLanbide03ReportVO infoReport = meLanbide03Manager.getReport(registro.getNombre(), registro.getNumExpediente(), Integer.valueOf(codOrg), adaptador);
                                        if (infoReport.getIdDokusi() == null)
                                        {
                                            lan6Documento.setContenido(infoReport.getReport());

                                            String id = servicios.guardarDocumento(lan6Documento);
                                            lan6Documento.setSincrono(false);
                                            log.info("El documento tiene el localizador '" + id + "' ");


                                            registro.setIdDokusi(id);
                                            meLanbide03Manager.updateIdDokusiReport(registro, adaptador);

                                            log.info("Guardado en dokusi " + id + " del documento " + registro.getNombre() + " del expediente " + registro.getNumExpediente());
                                        }
                                        else
                                        {
                                            log.error("El documento:" + registro.getNombre() + " del expediente " + registro.getNumExpediente() + " ya estaba subido a dokusi");
                                        }
                                    }
                                    catch (Lan6Excepcion e)
                                    {
                                        ArrayList<String> codes = e.getCodes();
                                        ArrayList<String> messages = e.getMessages();

                                        e.printStackTrace();

                                        log.error("No se ha podido subir a dokusi el documento:" + registro.getNombre() + " del expediente " + registro.getNumExpediente());
                                    }
                                }
                            } else {
                                log.info("No existen documentos APA que no estén subidos a dokusi");
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
                        log.error("Error en el job de subir APA dokusi : ", e);
                        try {
                            throw e;
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(SubirDokusiAPApdfa.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            con.close();
                        } catch (SQLException ex) {
                            java.util.logging.Logger.getLogger(SubirDokusiAPApdfa.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                java.util.logging.Logger.getLogger(SubirDokusiAPApdfa.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            } else { this.log.error("Servidor desconocido.");}  //PARA LOCAL QUITAR
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
