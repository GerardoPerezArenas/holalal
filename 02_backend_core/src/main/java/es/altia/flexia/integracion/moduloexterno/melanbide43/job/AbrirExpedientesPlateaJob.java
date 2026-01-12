/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpRgiMiCarpetaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaListadoMisGestiones;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Justificante;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6InformarConsultaExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Direccion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Expediente;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Interesado;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Participacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Tramite;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.servicios.Lan6InformarConsultaServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AbrirExpedientesPlateaJob implements Job {

    GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
    Gson gson = gsonB.serializeNulls().create();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {

        try {
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);

            log.info("AbrirExpedientesPlateaJob - servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    Connection con = null;
                    AdaptadorSQLBD adaptador = null;
                    String numExpediente = "";
                    String nomDoc = "";
                    String oid = "";
                    MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
                    MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
                    long inicio = System.currentTimeMillis();
                    log.info("=======> AbrirExpedientesPlateaJob() - Tiempo inicio " + inicio);
                    int tratados = 0;
                    int correctos = 0;
                    try {
                        log.info("AbrirExpedientesPlateaJob lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        while (codOrg < 2) {
                            adaptador = this.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.debug("en el while de tokens codOrg: " + codOrg);

                            List<String> listaProc = new ArrayList<String>();

                            listaProc.add("RGI");
                            listaProc.add("IMV");

                            for (String codProc : listaProc) {
                                log.debug("Procedimiento: " + codProc);
                                //se abren todos los expedientes en Platea
                                Long codTramiteInicio = meLanbide43DAO.obtenerCodigoTramiteFase(codProc, "1", con);
                                log.debug("codTramiteInicio: " + codTramiteInicio);
                                List<ExpRgiMiCarpetaVO> expedientes = this.getExpRGI(codProc, con);
                                if (!expedientes.isEmpty()) {
                                    log.info("AbrirExpedientesPlateaJob - Se van a tratar " + expedientes.size() + " expedientes " + codProc);
                                    String fecConcesion = null;
                                    String fecConcesionEus = null;
                                    String desCas = null;
                                    String desEus = null;
                                    for (ExpRgiMiCarpetaVO expRGI : expedientes) {
                                        tratados++;
                                        String resultado = "KO";
                                        String mensaje = "";
                                        numExpediente = expRGI.getNumExpediente();
                                        log.info(" Expediente: " + numExpediente);
                                        String[] datos = numExpediente.split("/");

                                        fecConcesion = expRGI.getFechaConcesion();
                                        log.debug("Fecha Concesión: " + fecConcesion);
                                        String[] fechaCon = fecConcesion.split("/");
                                        fecConcesionEus = fechaCon[2] + "/" + fechaCon[1] + "/" + fechaCon[0];
                                        log.debug("Fecha Concesión EUS: " + fecConcesionEus);

                                        //LAMAR A MIS GESTIONES INICIO                            
                                        desCas = "Tramitación Iniciada";
                                        desEus = "Izapidetzea Hasita";
                                        boolean existe = true;
                                        boolean esHistorico = false;
                                        if (!existe_E_ext(numExpediente, con)) {
                                            if (existe_Hist_E_ext(numExpediente, con)) {
                                                esHistorico = true;
                                                log.info("====> HISTÓRICO");
                                            } else {
                                                existe = false;
                                                log.error("El expediente no existe o no tiene un tercero asociado");
                                            }
                                        }

                                        if (existe) {
                                            Participantes par = null;
                                            if (!esHistorico) {
                                                par = meLanbide43Manager.obtenerDatosParticipantes(numExpediente, adaptador);
                                            } else {
                                                par = this.leerDatosParticipantesHist(numExpediente, con);
                                            }
                                            log.debug("PARTICIPANTES: " + par);
                                            //log.debug("fecha: "+fecha); 
                                            log.info(" tipo id interesado: " + par.getTipoID());
                                            log.info("Documento: " + par.getNif());
                                            if (expRGI.getNumDoc().equalsIgnoreCase(par.getNif())) {
                                                if (par.getTipoID() != 0) {
                                                    String idGest = null;
                                                    if (!esHistorico) {
                                                        idGest = MeLanbide43Manager.getInstance().guardarGestiones(numExpediente, codOrg, adaptador, codTramiteInicio.intValue(), "I");
                                                    } else {
                                                        idGest = this.guardarGestionesHist(numExpediente, adaptador, codTramiteInicio.intValue(), "I");
                                                    }
                                                    log.info(" Id generado: " + idGest);
                                                    try {
                                                        //iniciar expediente                                                                                   
                                                        log.debug("En réplica función avanzarGestiones()");
                                                        ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
                                                        try {
                                                            log.info(" ANTES DE LEER GESTIONES");
                                                            misGestiones = meLanbide43Manager.obtenerDatosGestiones(idGest, adaptador);
                                                            if (!misGestiones.isEmpty()) {
                                                                for (FilaListadoMisGestiones gestion : misGestiones) {
                                                                    log.debug("En réplica función iniciarExpConsulta()");
                                                                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                                                                    Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExpediente, con);
//                                                                    String idProcedimiento = "";
//                                                                    if (esDesarrolloCero > 0) {
//                                                                        idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
//                                                                    } else {
//                                                                        idProcedimiento = meLanbide43Manager.convierteProcedimiento(datos[1]);
//                                                                    }
                                                                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                                                                    String info = meLanbide43Manager.obtenerJustificante(gestion.getNumInicio().toString(), gestion.getRegInicio().toString(), adaptador);

                                                                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(datos[1]);
                                                                    log.debug("new informar consulta servicios - " + datos[1]);
                                                                    Lan6DokusiServicios dokusi = new Lan6DokusiServicios(datos[1]);

                                                                    log.debug("Lan6Expediente: ");
                                                                    Lan6Documento doclan6 = null;

                                                                    if (info != null) {
                                                                        log.info("hay documento. Datos-: " + info);
                                                                        String[] nom = info.split("#");
                                                                        oid = nom[0];
                                                                        nomDoc = nom[1];
                                                                        log.info("oid: " + oid + "nomDoc: " + nomDoc);
                                                                        doclan6 = dokusi.consultarDocumento(oid);
                                                                    }

                                                                    Calendar fechaConcesion = Calendar.getInstance();
                                                                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                                                                    fechaConcesion.setTime(formatoFecha.parse(fecConcesion));
                                                                    Lan6Expediente lan6Expediente = new Lan6Expediente();
                                                                    lan6Expediente.setNumero(numExpediente);
                                                                    lan6Expediente.setEjercicio(datos[0]);
                                                                    lan6Expediente.setDescripcion(numExpediente);
                                                                    lan6Expediente.setDescripcionEu(numExpediente);

                                                                    lan6Expediente.setFechaSolicitud(fechaConcesion);

                                                                    lan6Expediente.setFechaApertura(fechaConcesion);

                                                                    log.info(" Antes de recoger los datos del trámite");
                                                                    ArrayList<Lan6Tramite> listaTramites = new ArrayList<Lan6Tramite>();
                                                                    log.debug("Lan6Tramite: ");
                                                                    Lan6Tramite lan6Tramite = new Lan6Tramite();

                                                                    lan6Tramite.setId(gestion.getTramiteInicio());
                                                                    lan6Tramite.setDescripcion(desCas);
                                                                    lan6Tramite.setDescripcionEu(desEus);
                                                                    lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ABRIR_EXPEDIENTE);
                                                                    lan6Tramite.setIdExpediente(numExpediente);
                                                                    lan6Tramite.setEjercicio(datos[0]);
                                                                    Calendar fecActualizacion = Calendar.getInstance();
                                                                    lan6Tramite.setFechaActualizacion(fecActualizacion);

                                                                    log.debug("Despues de recoger los datos del trámite");
                                                                    log.debug("             ID: " + lan6Tramite.getId());
                                                                    log.info("    Descripción: " + lan6Tramite.getDescripcion());
                                                                    log.info("Descripción EUS: " + lan6Tramite.getDescripcionEu());
                                                                    log.info("      Ejercicio: " + lan6Tramite.getEjercicio());
                                                                    log.info("  ID Expediente: " + lan6Tramite.getIdExpediente());
                                                                    log.info("   Tipo Trámite: " + lan6Tramite.getTipoTramite());
                                                                    log.info("F ACtualización: " + formatoFecha.format(lan6Tramite.getFechaActualizacion().getTime()));

                                                                    listaTramites.add(lan6Tramite);

                                                                    lan6Expediente.setListaTramites(listaTramites);

// Participacion
                                                                    log.debug("Lan6Participacion: ");
                                                                    Lan6Participacion lan6participacion = new Lan6Participacion();

                                                                    ArrayList<String> tfnosAvisos = new ArrayList<String>();
                                                                    tfnosAvisos.add(par.getTlf());

                                                                    ArrayList<String> mailsAvisos = new ArrayList<String>();
                                                                    mailsAvisos.add(par.getMail());

                                                                    Lan6Direccion direccion = new Lan6Direccion();
                                                                    direccion.setTipoDireccion(Lan6Constantes.DIRECCION_ESTATAL);
                                                                    direccion.setIdPais(par.getIdPais());
                                                                    direccion.setNmPais(par.getPais());
                                                                    direccion.setIdProvincia(par.getIdProv());
                                                                    log.debug("direccion.getIdProvincia: " + direccion.getIdProvincia());
                                                                    direccion.setNmProvincia(par.getProv());
                                                                    log.debug("direccion.getNmProvincia: " + direccion.getNmProvincia());
                                                                    direccion.setIdMunicipio(par.getIdMuni());
                                                                    direccion.setNmMunicipio(par.getMuni());
                                                                    direccion.setIdLocalidad(par.getIdMuni());
                                                                    direccion.setNmLocalidad(par.getMuni());
                                                                    direccion.setIdCalle(par.getIdCalle());
                                                                    direccion.setNmCalle(par.getCalle());
                                                                    direccion.setPortal(par.getNum());
                                                                    direccion.setLetra(par.getLetra());

                                                                    if (doclan6 != null) {
                                                                        lan6participacion.setCanalNotificacion(Lan6Constantes.NOTIF_ELECTRONICA);
                                                                        lan6participacion.setTfnosAvisos(tfnosAvisos);
                                                                        lan6participacion.setMailsAvisos(mailsAvisos);
                                                                    } else {
                                                                        lan6participacion.setCanalNotificacion(Lan6Constantes.NOTIF_POSTAL);
                                                                        lan6participacion.setDireccion(direccion);
                                                                        log.debug("Mandamos direccion: " + direccion.getNmCalle());
                                                                    }
                                                                    lan6participacion.setIdioma(Lan6Constantes.IDIOMA_ES);

// Interesados
                                                                    ArrayList<Lan6Interesado> interesados = new ArrayList<Lan6Interesado>();

                                                                    log.info(" Lan6Interesado: ");
                                                                    Lan6Interesado lan6Interesado = new Lan6Interesado();

                                                                    lan6Interesado.setNumIdentificacion(par.getNif());
                                                                    if (par.getApe1() == null || par.getApe1().isEmpty() || par.getTipoID() == 4 || par.getTipoID() == 5) {
                                                                        log.debug("PERSONA_JURIDICA: ");
                                                                        lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_CIF);
                                                                        lan6Interesado.setRazonSocial(par.getNombre());
                                                                    } else {
                                                                        log.debug("PERSONA_FISICA: ");
                                                                        lan6Interesado.setNombre(par.getNombre());
                                                                        lan6Interesado.setApellido1(par.getApe1());
                                                                        lan6Interesado.setApellido2(par.getApe2() != null ? par.getApe2() : "");
                                                                        switch (par.getTipoID()) {
                                                                            case 1:
                                                                                lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_NIF);
                                                                                break;
                                                                            case 2:
                                                                                lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_PASAPORTE);
                                                                                break;
                                                                            case 3:
                                                                                lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_NIE);
                                                                                break;
                                                                            case 8:
                                                                                lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_OTRO);
                                                                                break;
                                                                            case 9:
                                                                                lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_OTRO);
                                                                                break;
                                                                            default:
                                                                                log.error("El tipo " + par.getTipoID() + " no tiene correspondencia en PLATEA");
                                                                        }
                                                                    }
                                                                    lan6Interesado.setTipo(Lan6Constantes.TIPO_INTERESADO_TITULAR);

                                                                    log.info("  Nº Documento : " + lan6Interesado.getNumIdentificacion());
                                                                    log.info("Tipo documento : " + lan6Interesado.getTipoIdentificacion());
                                                                    log.info("         Nombre: " + lan6Interesado.getNombre());
                                                                    log.info("      Apellidos: " + lan6Interesado.getApellido1() + " " + lan6Interesado.getApellido2());
                                                                    log.info("Tipo Interesado: " + lan6Interesado.getTipo());

                                                                    interesados.add(lan6Interesado);

                                                                    lan6participacion.setInteresados(interesados);

                                                                    log.debug(" Despues de recoger los participantes");
                                                                    lan6Expediente.setParticipacion(lan6participacion);

//datos de lan6expediente
                                                                    log.debug("getNumero:" + lan6Expediente.getNumero());
                                                                    log.debug("getEjercicio:" + lan6Expediente.getEjercicio());
                                                                    log.debug("getNumRegistro:" + lan6Expediente.getNumRegistro());

                                                                    if (lan6Expediente.getFechaRegistro() != null) {
                                                                        log.info(" Fecha Registro:" + formatoFecha.format(lan6Expediente.getFechaRegistro().getTime()));
                                                                    }
                                                                    if (lan6Expediente.getFechaSolicitud() != null) {
                                                                        log.info(" Fecha Solicitud:" + formatoFecha.format(lan6Expediente.getFechaSolicitud().getTime()));
                                                                    }
                                                                    if (lan6Expediente.getFechaApertura() != null) {
                                                                        log.info(" Fecha Apertura:" + formatoFecha.format(lan6Expediente.getFechaApertura().getTime()));
                                                                    }

                                                                    log.debug("Participacion:" + lan6Expediente.getParticipacion());
                                                                    if (lan6Expediente.getParticipacion() != null) {
                                                                        log.info("---->Participante canal:" + lan6Expediente.getParticipacion().getCanalNotificacion());
                                                                        log.debug("---->Participante TfnosAvisos:" + lan6Expediente.getParticipacion().getTfnosAvisos());
                                                                        log.debug("---->Participante mailsAvisos:" + lan6Expediente.getParticipacion().getMailsAvisos());
                                                                        log.debug("---->Participante direccion:" + lan6Expediente.getParticipacion().getDireccion());
                                                                        log.debug("---->Participante idioma:" + lan6Expediente.getParticipacion().getIdioma());
                                                                        log.debug("---->Participante interesado:numiden=" + lan6Expediente.getParticipacion().getInteresados().get(0).getNumIdentificacion() + ", tipoiden="
                                                                                + lan6Expediente.getParticipacion().getInteresados().get(0).getTipoIdentificacion() + ", razon="
                                                                                + lan6Expediente.getParticipacion().getInteresados().get(0).getRazonSocial() + ", nombre="
                                                                                + lan6Expediente.getParticipacion().getInteresados().get(0).getNombre() + ", ape1="
                                                                                + lan6Expediente.getParticipacion().getInteresados().get(0).getApellido1() + ", ape2="
                                                                                + lan6Expediente.getParticipacion().getInteresados().get(0).getApellido2() + ", tipo="
                                                                                + lan6Expediente.getParticipacion().getInteresados().get(0).getTipo());
                                                                    }

//llamada al metodo
                                                                    log.debug("-------------FECHA REGISTRO: " + lan6Expediente.getFechaRegistro());
                                                                    log.info(" Parametros enviados iniciarExpediente - lan6Expediente : " + gson.toJson(lan6Expediente));
                                                                    mensaje = servicios.iniciarExpediente(lan6Expediente);
                                                                    log.info(" Respuesta de iniciarExpediente: " + mensaje);
                                                                    meLanbide43Manager.actualizarProcesados(gestion.getId(), adaptador);
                                                                    resultado = "OK";
                                                                    correctos++;

                                                                } // for filaMisGest

                                                            } //misGestiones > 0
                                                            log.info(" DESPUES DE LEER GESTIONES");
                                                        } catch (Lan6InformarConsultaExcepcion e) { // 149
                                                            log.error(" Error en la funcion iniciarExpConsulta: " + e.getMessage());
                                                            mensaje = "Error en la funcion iniciarExpConsulta: " + e.getMessage();
                                                        } catch (Lan6Excepcion ex) {
                                                            log.error(" Error en la función iniciarExpConsulta: " + ex.getException().getMessage());
                                                            mensaje = "Error en la función iniciarExpConsulta: " + ex.getException().getMessage();
                                                        } catch (Exception ex) {
                                                            log.error(" Error en la función iniciarExpConsulta: ", ex);
                                                            mensaje = "Error en la función iniciarExpConsulta: " + ex.getMessage();
                                                        }
                                                    } catch (Exception e) { // 139
                                                        log.error(" Excepcion generica: " + e);
                                                        mensaje = e.getMessage();
                                                    }
                                                } else {
                                                    mensaje = "No se han recuperado el interesado";
                                                    resultado = "NI";
                                                }
                                            } else {
                                                mensaje = "El interesado en REGEXLAN no coincide con el recibido";
                                                resultado = "NI";
                                            }
                                        } else {
                                            mensaje = "El expediente no existe o no tiene un tercero asociado";
                                            resultado = "NE";
                                        }
                                        log.info("AbrirExpedientesPlateaJob - Resultado: " + resultado + " - " + mensaje);
                                        try {
                                            this.actualizaResultado(expRGI.getId(), resultado, mensaje, con);
                                        } catch (Exception e) {
                                            log.error(" Excepcion al actualizaResultado: " + e);
                                        }
                                    }
                                } else {
                                    log.info("No hay expedientes " + codProc + " para tratar");
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
                        int errores = tratados - correctos;
                        log.info(" =======> AbrirExpedientesPlateaJob() - END - Tratados: " + tratados + " - Correctos: " + correctos + " - Errores: " + errores);
                        long fin = System.currentTimeMillis();
                        long transcurrido = fin - inicio;
                        log.info(" =======> AbrirExpedientesPlateaJob() - Tiempo inicio - " + new SimpleDateFormat("hh:mm:ss:SSS").format(new Date(inicio)));
                        log.info(" =======> AbrirExpedientesPlateaJob() - Tiempo fin    - " + new SimpleDateFormat("hh:mm:ss:SSS").format(new Date(fin)));
                        log.info(" =======> AbrirExpedientesPlateaJob() - Transcurrido  - " + new SimpleDateFormat("hh:mm:ss:SSS").format(new Date(transcurrido)));
                    } catch (Exception e) {
                        log.error("Error en el job de AbrirExpedientesPlateaJob: ", e);
                        try {
                            throw e;
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(AbrirExpedientesPlateaJob.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                java.util.logging.Logger.getLogger(AbrirExpedientesPlateaJob.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (adaptador != null) {
                            try {
                                adaptador.devolverConexion(con);
                            } catch (BDException e) {
                                log.error("Error devolviendo la conexion ... " + e.getErrorCode() + " " + e.getDescripcion() + " " + e.getMensaje(), e);
                            }
                        }
                    }
                }//para local quitar
            }

        } catch (Exception ex) {
            log.error(" Error: " + ex);
        }
    }

    protected static Config conf = ConfigServiceHelper.getConfig("notificaciones");

    private final Logger log = LogManager.getLogger(AbrirExpedientesPlateaJob.class);

    private String codOrganizacion;

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) {
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexion al esquema generico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (conGenerico != null && !conGenerico.isClosed()) {
                        conGenerico.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }//try-catch
            }// finally

        }// synchronized
        return adapt;
    }//getConnection

    private List<ExpRgiMiCarpetaVO> getExpRGI(String codProc, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        List<ExpRgiMiCarpetaVO> listaExp = new ArrayList<ExpRgiMiCarpetaVO>();
        try {

            query = "SELECT * FROM ("
                    + "    SELECT ROWNUM, datos.* FROM ("
                    + "        SELECT  id, num_exp, tipo_doc, num_doc, to_char (fecha_concesion, 'dd/MM/yyyy') AS fecha FROM rgi_inicio_carpeta"
                    + "        WHERE num_exp LIKE '%/" + codProc + "/%' "
                    + "        AND (resultado IS NULL OR resultado ='KO')"
                    //                    + " where id = 60488"
                    + "        ORDER BY id)datos"
                    + ")  WHERE ROWNUM <= 10000";

            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                listaExp.add(new ExpRgiMiCarpetaVO(rs.getInt("ID"),
                        rs.getString("NUM_EXP"),
                        rs.getString("TIPO_DOC"),
                        rs.getString("NUM_DOC"),
                        rs.getString("FECHA")));
            }
        } catch (SQLException e) {
            log.error(" Se ha producido un error recuperando los expedientes a tratar ", e);
            throw new Exception(e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listaExp;
    }

    private void actualizaResultado(int id, String resultado, String mensaje, Connection con) throws Exception {
        log.debug(" actualizaResultado ");
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "update RGI_INICIO_CARPETA set RESULTADO = ?, MENSAJE = ? where ID = ?";
            log.debug(query + " Resultado: " + resultado + " - ID: " + id);
            ps = con.prepareStatement(query);
            int contador = 1;
            ps.setString(contador++, resultado);
            ps.setString(contador++, mensaje);
            ps.setInt(contador++, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error(" Se ha producido un error actualizando el resultado del registro ID:  " + id, e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    private boolean existe_E_ext(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select count(*) from E_EXT where EXT_NUM=?";
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error(" Se ha producido un error comprobando si existe en e_ext ", e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    private boolean existe_Hist_E_ext(String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select count(*) from HIST_E_EXT where EXT_NUM=?";
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            log.error(" Se ha producido un error comprobando si existe en HIST_e_ext ", e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    private Participantes leerDatosParticipantesHist(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Participantes part = new Participantes();
        try {
            String query = null;

            query = "SELECT EXT_NUM "
                    + ", HTE_DOC, HTE_NOM, HTE_AP1, HTE_AP2,HTE_TLF, HTE_DCE, HTE_NOC, HTE_TID "
                    + ", DNN_DMC, PAI_COD, PRV_COD, MUN_COD, VIA_COD , PAI_NOM, PRV_NOM, MUN_NOM, VIA_NOM, DNN_LED, DNN_NUD, EXT_ROL  "
                    + " FROM hist_E_EXT "
                    + " INNER JOIN T_hte ON EXT_TER = hte_ter and hte_nvr=ext_nvr "
                    + " LEFT JOIN T_DOT ON EXT_DOT = DOT_DOM AND HTE_TER = DOT_TER  "
                    + " LEFT JOIN T_DNN ON DNN_DOM = DOT_DOM "
                    + " LEFT JOIN FLBGEN.T_PAI ON PAI_COD = DNN_PAI "
                    + " LEFT JOIN FLBGEN.T_PRV ON PRV_PAI = PAI_COD AND PRV_COD = DNN_PRV "
                    + " LEFT JOIN FLBGEN.T_MUN ON MUN_PAI = PAI_COD AND MUN_PRV = PRV_COD AND MUN_COD = DNN_MUN "
                    + " LEFT JOIN T_VIA ON VIA_PAI = PAI_COD AND VIA_PRV = PRV_COD AND VIA_MUN = MUN_COD AND VIA_COD = DNN_VIA "
                    + " WHERE EXT_NUM = '" + numExp + "' AND EXT_ROL=1";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                part.setNumExp(rs.getString("EXT_NUM"));
                part.setNif(rs.getString("HTE_DOC"));
                part.setNombre(rs.getString("HTE_NOM"));
                part.setApe1(rs.getString("HTE_AP1"));
                part.setApe2(rs.getString("HTE_AP2"));
                part.setTlf(rs.getString("HTE_TLF"));
                part.setMail(rs.getString("HTE_DCE"));
                part.setNomC(rs.getString("HTE_NOC"));
                part.setTipoID(rs.getInt("HTE_TID"));
                part.setIdPais(rs.getString("PAI_COD"));
                part.setIdProv(rs.getString("PRV_COD"));
                part.setIdMuni(rs.getString("MUN_COD"));
                part.setIdCalle(rs.getString("VIA_COD"));
                part.setPais(rs.getString("PAI_NOM"));
                part.setProv(rs.getString("PRV_NOM"));
                part.setMuni(rs.getString("MUN_NOM"));
                part.setCalle(rs.getString("VIA_NOM"));
                part.setNum(rs.getString("DNN_NUD"));
                part.setLetra(rs.getString("DNN_LED"));
                //exp.setRol(rs.getString("EXT_ROL"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            log.error(" Error en leerDatosParticipantes " + ex.getMessage());

            ErrorBean error = new ErrorBean();
            error.setIdError("MISGEST_006");
            error.setMensajeError("Error leyendo datos de participantes");
            error.setSituacion("leerDatosParticipantes");

            MeLanbide43Manager.grabarError(error, ex.getMessage(), ex.toString(), numExp);
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return part;
    }

    private String guardarGestionesHist(String numExp, AdaptadorSQLBD adaptador, int codTramite, String evento) throws Exception {
        log.debug(" guardarGestionesHist BEGIN");
        Connection con = null;
        int result = 0;
        int id = 0;
        if (numExp != null && !"".equals(numExp)) {
            try {
                con = adaptador.getConnection();
                result = this.guardarMiGestionHist(numExp, codTramite, evento, con);
                log.debug("Resultado guardar Mi Gestión: " + result);
                if (result != 1) {
                    adaptador.rollBack(con);
                    return "4";//fallo al actualizar cntador
                }
                id = MeLanbide43DAO.getInstance().getIdGestiones(con);
            } catch (Exception e) {
                log.error(" Se ha producido una excepción en la BBDD guardando gestiones para el expediente " + numExp, e);
            } finally {
                adaptador.devolverConexion(con);
            }
        }
        return String.valueOf(id);
    }

    private int guardarMiGestionHist(String numExp, int codTramite, String evento, Connection con) throws Exception {
        log.debug(" guardarMiGestionHist BEGIN");
        Statement st = null;
        ResultSet rs = null;
        String query = null;

        int result = 0;
        int id = 0;
        String[] pepe = numExp.split("/");
        ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();

        try {

            query = "select EXR_EJR ejericicio_anotacion, EXR_NRE numero_registro,TID_COD tipo_doc,"
                    + "HTE_DOC documento, EXP_ASU  "
                    + " from HIST_E_EXR, HIST_E_EXT,T_HTE,T_TID, HIST_E_EXP "
                    + " where EXT_NUM = EXP_NUM AND EXT_NUM=EXR_NUM(+) and "
                    + " HTE_TER=EXT_TER and HTE_NVR=EXT_NVR and "
                    + " HTE_TID=TID_cod  and EXT_ROL=1 and "
                    + " EXT_NUM='" + numExp + "'";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            String asunto = "";
            String[] texto;
            while (rs.next()) {
                FilaListadoMisGestiones gestion = new FilaListadoMisGestiones();
                gestion.setNumExp(numExp);
                if (rs.getString("tipo_doc") != null) {
                    gestion.setTipoCodInte(rs.getInt("tipo_doc"));
                }
                gestion.setCodInteresado(rs.getString("documento"));
                gestion.setTramiteInicio(String.valueOf(codTramite));
                if (rs.getString("numero_registro") != null) {
                    gestion.setNumInicio(rs.getInt("numero_registro"));
                }
                if (rs.getString("ejericicio_anotacion") != null) {
                    gestion.setRegInicio(rs.getInt("ejericicio_anotacion"));
                }
                if (rs.getString("EXP_ASU") != null) {
                    asunto = rs.getString("EXP_ASU");
                }
                log.debug("-------------->asunto: " + asunto);
                if (asunto.toUpperCase().contains("NUMREGGV")) {
                    //creo un substring desde donde aparece la cadena "NumRegGV" hasta el final
                    String subs = (asunto.substring(asunto.toUpperCase().indexOf("NUMREGGV"), asunto.length())).trim();
                    log.debug("-------------->subs: " + subs);
                    texto = subs.split(" ");
                    log.debug("-------------->texto:length: " + texto.length);
                    if (texto.length > 1) {
                        log.debug("-------------->texto_REG: " + texto[1]);
                        gestion.setRegAsun(texto[1]);
                    }
                    //creo un substring desde donde aparece la cadena "FechaRegGV" hasta el final
                    subs = (asunto.substring(asunto.toUpperCase().indexOf("FECHAREGGV"), asunto.length())).trim();
                    texto = subs.split(" ");
                    log.debug("-------------->texto:length: " + texto.length);
                    if (texto.length > 1) {
                        log.debug("-------------->texto_FECHA: " + texto[1]);
                        gestion.setFechaAsun(texto[1]);
                    }

                } else {
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    String hoy = formatoFecha.format(Calendar.getInstance().getTime());
                    log.info("Es expediente RGI/IMV sin fecha en el asunto, se graba la fecha actual con el formato que  los registros: " + hoy);
                    gestion.setFechaAsun(hoy);
                }
                misGestiones.add(gestion);
            }
            Integer regInicio = null;
            Integer numInicio = null;
            Integer tipoCodInte = null;
            String codInte = "";
            String regAsun = "";
            String fechaAsun = "";

            id = MeLanbide43DAO.getInstance().getIdGestiones(con);
            id++;
            if (!misGestiones.isEmpty()) {
                for (FilaListadoMisGestiones unidad : misGestiones) {
                    //id = unidad.getId();
                    tipoCodInte = unidad.getTipoCodInte();
                    codInte = unidad.getCodInteresado();
                    unidad.getTramiteInicio();
                    regInicio = unidad.getRegInicio();
                    numInicio = unidad.getNumInicio();
                    regAsun = unidad.getRegAsun();
                    fechaAsun = unidad.getFechaAsun();
                }

                query = "insert into MELANBIDE43_INTEGMISGEST" //+ ConfigurationParameter.getParameter(ConstantesMeLanbide42.TABLA_CONTADORES, ConstantesMeLanbide42.FICHERO_PROPIEDADES)
                        + " (ID, EXP_NUM, TER_TID, TER_DOC, TIPO_OPERACION, "
                        + " COD_TRAMITE_INICIO,  FECHA_GENERADO,RES_EJE, RES_NUM, EXP_TIPO, REG_TELEMATICO, FECHA_TELEMATICO) "
                        + " values(" + id + ", '" + numExp + "', "
                        + tipoCodInte + ", '" + codInte + "','" + evento + "', '" + codTramite
                        + "',to_date(to_char(sysdate,'dd/mm/yyyy HH24:mi:ss'), 'dd/mm/yyyy HH24:mi:ss'), "
                        + " " + regInicio + ", " + numInicio + ", '" + pepe[1] + "', '" + regAsun + "', '" + fechaAsun + "')";
                log.debug("sql = " + query);
                st = con.createStatement();
                result = st.executeUpdate(query);
            }
        } catch (SQLException ex) {
            log.error(" Se ha producido un error guardando la GESTIÓN ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return result;
    }

}
