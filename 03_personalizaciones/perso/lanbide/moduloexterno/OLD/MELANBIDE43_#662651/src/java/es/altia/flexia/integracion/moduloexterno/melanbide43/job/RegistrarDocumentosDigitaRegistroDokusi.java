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
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.config.Lan6Config;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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

public class RegistrarDocumentosDigitaRegistroDokusi implements Job{
    
    class OID_AuditUserVO{
        String oid;
        String auditUser;
        int ejercicioAnotacion;
        int numeroAnotacion;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getAuditUser() {
            return auditUser;
        }

        public void setAuditUser(String auditUser) {
            this.auditUser = auditUser;
        }

        public int getEjercicioAnotacion() {
            return ejercicioAnotacion;
        }

        public void setEjercicioAnotacion(int ejercicioAnotacion) {
            this.ejercicioAnotacion = ejercicioAnotacion;
        }

        public int getNumeroAnotacion() {
            return numeroAnotacion;
        }

        public void setNumeroAnotacion(int numeroAnotacion) {
            this.numeroAnotacion = numeroAnotacion;
        }
        
    }
    
    private Logger log = Logger.getLogger(RegistrarDocumentosDigitaRegistroDokusi.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            log.info("Inicia job RegistrarDocumentosDigitaRegistroDokusi - execute()");
            int contador = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.info("jec.getRefireCount(): " + contador);
            Trigger trigger = jec.getTrigger();

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);

            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.info("servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    
                    Connection con = null;
                    int numIntentos = 0;
                    try {
                        log.info("Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS",
                                ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.info("en el while de tokens codOrg: " + codOrg);
                            
                            log.info("Recogemos los OID a Tratar");
                            List <OID_AuditUserVO> OIDsAuditUsers = this.getOIDsRegistrarDokusi(con);
                            log.info("OIDsAuditUsers (Numero de OIDs):" + OIDsAuditUsers.size());

                            for (OID_AuditUserVO auditUserVO :OIDsAuditUsers) {
                                log.info("Vamos a tratar el siguiente Documento : " + auditUserVO.getOid() + " " + auditUserVO.getEjercicioAnotacion() + "/"+auditUserVO.getNumeroAnotacion() + " " + auditUserVO.getAuditUser());
                                try {
                                    // Registrar de Dokusi
                                    String idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA,
                                            "PROCEDIMIENTO_ID_" + "LANRE");
                                    log.info("idProcedimiento: " + idProcedimiento);
                                    Lan6DokusiServicios servicios = new Lan6DokusiServicios(idProcedimiento);
                                    String oid = auditUserVO.getOid(); //"0901a0fe82846957";
                                    String auditUser = auditUserVO.getAuditUser();  //"18206585S#MIREN EDURNE BASARTE ARRUTI#MBASARTE";
                                    log.info("parametros antes de llamada:oid auditUser:"+oid+" "+auditUser);
                                    
// comentar para compilar  
/*
                                 String rs = servicios.registraDocumento(oid, auditUser);
                                    log.info("Salida llamada servicio adaptadores registraDocumento " + rs);
*/                                    
// hasta aqui                                    
                                } catch (Lan6Excepcion e) {
                                    ArrayList<String> codes = e.getCodes();
                                    ArrayList<String> messages = e.getMessages();
                                    String codesException="";
                                    String messagesException = "";
                                    for (String code : codes) {
                                        if(codesException!="")
                                            codesException = codesException + "," + code;
                                        else
                                            codesException = code;
                                    }
                                    for (String message : messages) {
                                        if(codesException!="")
                                            messagesException = codesException + "," + message;
                                        else
                                            messagesException = message;
                                    }
                                    log.error("Lan6Excepcion  Arreglo de Codigos y mensajes de error : " + codesException + " " + messagesException);
                                    log.error("Lan6Excepcion  al Registrar OIDs de Documentos de registro escaneados en Dokusi " + e.getMessage() + " -*- " + e.getMensajeExcepcion(), e);
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
                        log.error(RegistrarDocumentosDigitaRegistroDokusi.class.getName() + " Error en el job : ", e);
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
                                log.error(RegistrarDocumentosDigitaRegistroDokusi.class.getName()+" - Error cerrando BBDD", ex);
                            }
                        }
                    }
                }//para local quitar
            }
        log.info(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
        } catch (Exception ex) {
            log.error(this.getClass().getName()  + " Error: " + ex);
        }
    }

    private List<OID_AuditUserVO> getOIDsRegistrarDokusi(Connection con) throws Exception {
        log.info("getOIDsRegistrarDokusi - Begin()");
        //Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<OID_AuditUserVO> oID_AuditUserVOs = new ArrayList<OID_AuditUserVO>();
        OID_AuditUserVO oID_AuditUserVO = new OID_AuditUserVO();
        String numTarea ="";
        try {
            numTarea=ConfigurationParameter.getParameter("NUM_TAREA_REGISTRO_DOCESCANEADOS_DOKUSI", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            StringBuffer sb = new StringBuffer("SELECT COD_MUNICIPIO RED_DEP,ANO RED_EJE,NUM_EXP RED_NUM,COD_PROC PROCEDIMIENTO,DOCUMENTO RED_IDDOC_GESTOR,NOMBRE AUDIT_USER,NUM_TAREA ");
            sb.append(" FROM Cepap_Migracion_Excel ");
            sb.append(" WHERE NUM_TAREA=? ");
            sb.append(" ORDER BY RED_NUM");
            log.info("sql = " + sb.toString());
            ps = con.prepareStatement(sb.toString());
            log.info("sql parameters= " + numTarea);
            ps.setString(1, numTarea);
            rs = ps.executeQuery();

            while (rs.next()) {
                oID_AuditUserVO.setEjercicioAnotacion(rs.getInt("RED_EJE"));
                log.info("EjercicioAnotacion= " + oID_AuditUserVO.getEjercicioAnotacion());
                oID_AuditUserVO.setNumeroAnotacion(rs.getInt("RED_NUM"));
                log.info("NumeroAnotacion= " + oID_AuditUserVO.getNumeroAnotacion());
                oID_AuditUserVO.setOid(rs.getString("RED_IDDOC_GESTOR"));
                log.info("Oid= " + oID_AuditUserVO.getOid());
                oID_AuditUserVO.setAuditUser(rs.getString("AUDIT_USER"));
                log.info("AuditUser= " + oID_AuditUserVO.getAuditUser());
                oID_AuditUserVOs.add(oID_AuditUserVO);
                oID_AuditUserVO=new OID_AuditUserVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando getOIDsRegistrarDokusi ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.error("Procedemos a cerrar el statement y el resultset - JOB getOIDsRegistrarDokusi");
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - JOB getOIDsRegistrarDokusi", e);
            }
        }
        log.info("getOIDsRegistrarDokusi oID_AuditUserVOs.size - End() " + oID_AuditUserVOs.size());
        return oID_AuditUserVOs;
    }

}
