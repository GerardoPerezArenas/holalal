/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide77.job;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.i18n.MeLanbide43I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.melanbide77.dao.MeLanbide77DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide77.manager.MeLanbide77Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide77.util.ConstantesMeLanbide77;
import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.RegistroAerteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.RegistroBatchVO;
import es.altia.flexia.integracion.moduloexterno.melanbide77.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide77.vo.SolicitudAerteVO;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionBindingStub;
import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionServiceLocator;
import es.altia.webservice.client.tramitacion.ws.wto.CondicionFinalizacionVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.FlujoFinalizacionVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdTramiteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InfoConexionVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.RegistroAsociadoVO;
import es.altia.webservice.client.tramitacion.ws.wto.RespuestasTramitacionVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import es.altia.webservice.client.tramitacion.ws.wto.TramiteVO;
import es.altia.technical.PortableContext;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import javax.xml.rpc.ServiceException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 *
 * @author kepa
 */
public class InicioAutomaticoExpedientesAERTEJob implements Job {

    private static final Integer OP_INICIO_EXPEDIENTE = 1;
    private static final Integer OP_ASOCIAR_REGISTRO = 2;
    private static final Integer OP_BUSCAR_SOLICITUD = 3;
    private static final Integer OP_GRABAR_DATOS_SUPLEMENTARIOS = 4;
    private static final Integer OP_AVANZAR_TRAMITE = 5;
    private static final Integer OP_MISGESTIONES_INICIO_EXPEDIENTE = 2;

    protected static Config conf = ConfigServiceHelper.getConfig("notificaciones");
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    private final Logger log = LogManager.getLogger(InicioAutomaticoExpedientesAERTEJob.class);

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Connection con = null;
        try {
            int pepe = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.info("jec.getRefireCount(): " + pepe);
            Trigger pepito = jec.getTrigger();

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide77.CAMPO_SERVIDOR, ConstantesMeLanbide77.FICHERO_PROPIEDADES);
            log.info("servidor: " + servidor);

            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    int numIntentos = 0;
                    int anoRegistro = 0;
                    int numRegistro = 0;
                    int id = 0;
                    String[] params = new String[]{"ORACLE"};
                    try {
                        log.info("Execute lanzado " + System.getProperty("weblogic.Name"));
//Recogemos los codigos del properties: 
                        setCodOrganizacion(ConfigurationParameter.getParameter(ConstantesMeLanbide77.COD_ORG, ConstantesMeLanbide77.FICHERO_PROPIEDADES));
                        setCodOrg(Integer.parseInt(codOrganizacion));
                        setCodProcedimiento(ConfigurationParameter.getParameter(ConstantesMeLanbide77.COD_PROCEDIMIENTO, ConstantesMeLanbide77.FICHERO_PROPIEDADES));
                        setCodigoUOR(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide77.COD_UOR, ConstantesMeLanbide77.FICHERO_PROPIEDADES)));
                        log.info("Organizacion: " + codOrganizacion);
                        log.info("codigo UOR " + codigoUOR);
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter(ConstantesMeLanbide77.DOS_ENTORNOS, ConstantesMeLanbide77.FICHERO_PROPIEDADES));
                        setCodTramInicio(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide77.INICIO_EXPEDIENTE, ConstantesMeLanbide77.FICHERO_PROPIEDADES)));
                        setCodTramRevision(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide77.REVISION_DOCUMENTACION, ConstantesMeLanbide77.FICHERO_PROPIEDADES)));

// Se declara el objeto binding para las llamadas a los SW de WSTramitacion
                        String wsUrl = ConfigurationParameter.getParameter(ConstantesMeLanbide77.URL_WS_TRAMITACION, ConstantesMeLanbide77.FICHERO_PROPIEDADES);
                        URL url = new URL(wsUrl);
                        WSTramitacionBindingStub binding = null;
                        try {
                            binding = (WSTramitacionBindingStub) new WSTramitacionServiceLocator().getWSTramitacionPort(url);
                            log.info("resultado binding : " + binding);
                            binding.setTimeout(60000);
                        } catch (ServiceException ex) {
                            log.error("eee", ex);
                        }
//Informacion de la conexion;
                        InfoConexionVO inf = new InfoConexionVO();
                        inf.setOrganizacion(codOrganizacion);
                        inf.setAplicacion("RGI");
                        log.info("------------Info conexion----------");
                        log.info("Organizacion info " + inf.getOrganizacion());
                        log.info("Aplicacion inf " + inf.getAplicacion());
                        log.info("------------Info conexion----------");

                        RegistroBatchVO registroBatch = new RegistroBatchVO();
                        boolean continuar = true;
                        while (codOrg < 2) {
                            con = this.getAdaptSQLBD(codOrganizacion).getConnection();
                            log.info("en el while de tokens codOrg: " + codOrg);

                            ExpedienteVO expediente = new ExpedienteVO();
                            String numExpediente = null;
                            String numExpedientePrevio = null;
                            String numExpedientePend = "";
                            String respuestaMisGest = "";
                            String cif = "";
                            String email = "";
                            String asunto = "";
                            int numSolicitud = 0;
                            boolean tieneExpediente = false;
                            boolean yaProcesado = false;
                            TerceroVO tercero = new TerceroVO();
                            SolicitudAerteVO solicitud = new SolicitudAerteVO();
                            int codTramite = 0;
                            int anoExp = 0;
                            int numExp = 0;

                            TramiteVO idTramite = new TramiteVO();
                            IdTramiteVO idTram = new IdTramiteVO();
                            IdTramiteVO idTramAvanzar = new IdTramiteVO();
                            CondicionFinalizacionVO condFinalizacion = new CondicionFinalizacionVO();
//Buscar los registros pendientes
                            MeLanbide77DAO meLanbide77DAO = MeLanbide77DAO.getInstance();
                            List<RegistroAerteVO> registros = meLanbide77DAO.getListaRegistrosAERTE(con);
                            log.debug("Registros AERTE: " + registros.size());
                            int idRegBatch = 0;
                            List<RegistroBatchVO> expedientesRelanzar = meLanbide77DAO.getExpedientesRelanzarAERTE(con);
                            log.debug("getExpedientesRelanzarAERTE: " + expedientesRelanzar.size());
                            // control hay registros pendientes
                            if (!registros.isEmpty()) {
                                /*  
                            for (RegistroAerteVO registro : registros) {*/
                                for (int i = 0; i < 500; i++) {// limitar numero registros
                                    log.info("           =====> InicioAutomaticoExpedientesAERTEJob - Ciclo " + (i + 1));
                                    RegistroAerteVO registroAERTE = registros.get(i);

                                    /* Por cada registro hay que comprobar si el tercero tiene otro expediente AERTE.
                                Si tiene expediente asociar el registro al expediente.
                                Si no tiene expediente iniciar uno y asociar el registro al expediente.
                                     */
                                    continuar = true;
                                    tieneExpediente = false;
                                    yaProcesado = false;
                                    numExpedientePrevio = null;
                                    anoRegistro = registroAERTE.getResEje();
                                    numRegistro = registroAERTE.getResNum();
                                    asunto = registroAERTE.getResAsu();
                                    log.info("numRegistro: " + numRegistro + " Ejercicio: " + anoRegistro);
                                    //extraer del registro el numero de solicitud
                                    numSolicitud = Integer.parseInt(asunto.substring(33, 38));
                                    log.info("Num Solicitud asociada al registro: " + numSolicitud);
                                    log.debug(registroAERTE.getNumSolicitud());
                                    registroAERTE.setNumSolicitud(numSolicitud);

                                    if (!expedientesRelanzar.isEmpty()) {
                                        for (RegistroBatchVO registroBatchVO : expedientesRelanzar) {
                                            if (registroBatchVO.getEjerRegistro() == anoRegistro && registroBatchVO.getNumRegistro() == numRegistro) {
                                                // registro AERTE ya procesado
                                                yaProcesado = true;
                                            }
                                        }
                                    }

                                    if (!yaProcesado) {
                                        tercero = MeLanbide77DAO.getTercero(codOrg, registroAERTE.getResEje(), registroAERTE, con);
                                        cif = tercero.getDoc();

                                        numExpedientePrevio = meLanbide77DAO.tieneExpedienteAERTE(codOrg, cif, con);

                                        if (numExpedientePrevio == null) {
//iniciar expediente
                                            log.info("Iniciamos nuevo expediente AERTE");
                                            expediente = inicioExpedienteAERTE(registroAERTE, inf, binding);
                                            log.debug("sale de inicioExpedienteAERTE");
                                            numExpediente = expediente.getIdExpedienteVO().getNumeroExpediente();
                                            log.info("numeroExpediente: " + numExpediente);
                                            if (expediente == null) {
                                                // error en inicio
                                                continuar = false;
                                            } else {
                                                log.info("generarMisGestionesInicio() : BEGIN");
                                                respuestaMisGest = misGestInicioOficio(numExpediente);
                                                registroBatch.setEjerRegistro(registroAERTE.getResEje());
                                                registroBatch.setNumRegistro(registroAERTE.getResNum());
                                                registroBatch.setNumSolicitud(numSolicitud);
                                                registroBatch.setNumExpediente(numExpediente);
                                                registroBatch.setCodTramite(codTramInicio);
                                                registroBatch.setOperacion("generarMisGestInicio");
                                                registroBatch.setRelanzar(0);
                                                registroBatch.setCodOperacion(OP_MISGESTIONES_INICIO_EXPEDIENTE);
                                                if (respuestaMisGest.equalsIgnoreCase("0")) {
                                                    registroBatch.setResultado("OK");
                                                    registroBatch.setObservaciones("Iniciado expediente en Mi Carpeta");
                                                    // registrar operacion expediente?
                                                } else {
                                                    // error en misgestiones
                                                    registroBatch.setResultado("KO");
                                                    registroBatch.setObservaciones("Error en generarMisGestionesInicio al iniciar el expediente, al avanzar el tramite se llama a comprobarExpGenerado. ");
                                                }
                                                MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
                                            }

                                        } else {
                                            log.info("El interesado con documento nº: " + cif + " tiene expediente AERTE previo. ");
                                            numExpediente = numExpedientePrevio;
                                            log.info("numeroExpediente: " + numExpediente);
                                            tieneExpediente = true;
                                            String[] numeroExpediente = numExpediente.split(ConstantesMeLanbide77.BARRA_SEPARADORA);

                                            IdExpedienteVO idexpediente = new IdExpedienteVO(Integer.parseInt(numeroExpediente[0]), Integer.parseInt(numeroExpediente[2]), numExpediente, codProcedimiento);
                                            expediente.setIdExpedienteVO(idexpediente);
                                        }

//asociar registro expediente
                                        if (continuar) {

                                            if (tieneExpediente) {
                                                if (asociarExpedienteRegistro(registroAERTE, expediente, inf, binding)) {
                                                    //operacion expediente
                                                    try {
                                                        registrarAsociar(numExpediente, registroAERTE, con);
                                                    } catch (Exception e) {
                                                        log.error("Ha ocurrido un error al grabar la operacion en Historico Operaciones. ", e);
                                                    }
                                                }
                                            } else {
                                                log.info("No tiene expediente previo, no es necesario llamar a asociarExpRegistro.");
                                            }
                                            continuar = true;
                                            // SOLICITUD FORMULARIO WEB AERTE - 32019, 28/07/2020 08:58:59
                                            log.debug("antes de buscar SOLICITUD ");
                                            solicitud = meLanbide77DAO.cargarDatosSolicitud(numSolicitud, con);
                                            log.debug("Recupera el: " + solicitud.getNumSolicitud());
                                            log.debug("despues de buscar SOLICITUD ");

                                            registroBatch.setEjerRegistro(registroAERTE.getResEje());
                                            registroBatch.setNumRegistro(registroAERTE.getResNum());
                                            registroBatch.setNumSolicitud(numSolicitud);
                                            registroBatch.setNumExpediente(numExpediente);
                                            registroBatch.setCodTramite(codTramInicio);
                                            registroBatch.setOperacion("buscar SOLICITUD");
                                            registroBatch.setRelanzar(0);
                                            registroBatch.setCodOperacion(OP_BUSCAR_SOLICITUD);
                                            registroBatch.setObservaciones("");
                                            if (solicitud.getNumSolicitud() == null) {
                                                registroBatch.setResultado("KO");
                                                registroBatch.setObservaciones("No existe solicitud con número " + numSolicitud + " en la tabla " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_SOLICITUDES, ConstantesMeLanbide77.FICHERO_PROPIEDADES));
                                                MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);

                                                ErrorBean err = new ErrorBean();
                                                err.setIdError("TRAMITACION_AERTE_003");
                                                err.setMensajeError("Error en el job de inicio automático de expedientes AERTE. No existe solicitud " + numSolicitud + " en el registro: " + anoRegistro + "/" + numRegistro);
                                                err.setSituacion("JobExecute");
                                                String causa = "";
                                                String error = "";
                                                grabarError(err, error, causa, numExpediente);
                                            } else {
                                                registroBatch.setNumSolicitud(solicitud.getNumSolicitud());
//                                        if (numSolicitud != numSolRecuperado) {
//                                            log.debug("DISTINTOS");
//                                            // error solicitud recuperada no es la del registro
//
//                                            registroBatch.setResultado("KO");
//                                            registroBatch.setObservaciones("El número de solicitud: " + numSolRecuperado + " recuperado no coincide con el asociado al registro: " + numSolicitud);
//                                            MeLanbide77DAO.getInstance().insertarRegistroBatch(con, registroBatch);
//
//                                            ErrorBean err = new ErrorBean();
//                                            err.setIdError("TRAMITACION_AERTE_006");
//                                            err.setMensajeError("Error en el job de Tramitación automática de agencias AERTE. El número de solicitud: " + numSolRecuperado + " recuperado no coincide con el asociado al registro: " + numSolicitud);
//                                            err.setSituacion("JobExecute");
//                                            String causa = "";
//                                            String error = "";
//                                            grabarError(err, error, causa, numExpediente);
//
//                                            continuar = false;
//                                        } else {
//                                            log.debug("IGUALES");
                                                registroBatch.setResultado("OK");
                                                registroBatch.setObservaciones("Recuperados los datos de la solicitud " + numSolicitud);
                                                MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
//                                        }

// Grabado de datos suplementarios a partir de solicitud
                                                if (tieneExpediente) {
                                                    int tramiteExpPrevio = meLanbide77DAO.buscarTramite(numExpediente, con);
                                                    registroBatch.setCodTramite(tramiteExpPrevio);
                                                    registroBatch.setResultado("OK");
                                                    registroBatch.setOperacion("Saltar grabado");
                                                    registroBatch.setRelanzar(0);
                                                    registroBatch.setCodOperacion(OP_GRABAR_DATOS_SUPLEMENTARIOS);
                                                    registroBatch.setObservaciones("Tiene datos de un registro anterior");
                                                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
                                                    continuar = true;
                                                } else {
                                                    continuar = grabadoDatosSuplementariosSolicitud(solicitud, numExpediente, registroAERTE, con);
                                                }

                                            }
//avanzar trámite
                                            idTramite = new TramiteVO();
                                            idTram = new IdTramiteVO();
                                            idTramAvanzar = new IdTramiteVO();
                                            condFinalizacion = new CondicionFinalizacionVO();
                                            FlujoFinalizacionVO flujo = new FlujoFinalizacionVO();
                                            IdTramiteVO[] listaRespuesta = new IdTramiteVO[1];
                                            String tipoFin = "";
                                            if (continuar) {
//Buscar en qué trámite está
                                                codTramite = meLanbide77DAO.buscarTramite(numExpediente, con);
                                                if (codTramite == codTramInicio) {
                                                    idTram.setCodTramite(codTramite);
                                                    idTram.setOcurrencia(1);
                                                    idTramite.setId(idTram);
                                                    idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.
                                                    idTramite.setUsuarioFin(registroAERTE.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.
                                                    idTramite.setFechaFin(Calendar.getInstance());
                                                    tipoFin = meLanbide77DAO.buscarTipoFinalizacion(codOrg, codProcedimiento, codTramite, con);
                                                    condFinalizacion.setTipoFinalizacion(tipoFin);
                                                    //Recogemos el Código interno del trámite a iniciar del properties:
                                                    idTramAvanzar.setCodTramite(codTramRevision);
                                                    idTramAvanzar.setOcurrencia(1);
                                                    listaRespuesta[0] = idTramAvanzar;
                                                    flujo.setListaRespuesta(listaRespuesta);
                                                    flujo.setTipoApertura(1);   // 1--> Obligatoria
                                                    condFinalizacion.setFlujoSI(flujo);
                                                    idTramite.setCondFinalizacion(condFinalizacion);
                                                    // finalizar trámite 10 y avanzar al 20
                                                    if (!finalizarTramite(registroAERTE, expediente, idTramite, condFinalizacion, inf, binding)) {
                                                        continuar = false;
                                                        log.error("no finaliza tramite. Reg: " + registroAERTE.getResEje() + "/" + registroAERTE.getResNum() + " Exp.: " + numExpediente + " Tramite: " + codTramite + " Tipo Fin: " + tipoFin + " Cod.FinS: " + condFinalizacion + " - " + inf);
                                                    }
                                                } else {
                                                    log.info("El expediente " + numExpediente + " ya está en " + codTramite + " - REVISION DOCUMENTACION");
                                                }
                                            }

                                        }
                                    } else {
                                        log.info("Registro ya procesado en Barch previo");
                                    }

                                } //for RegistroAerteVO
                            }// if registros ! vacio
                            else {
                                log.info("NO hay registros a tratar");
                            }
//---------BUCLE PARA RELANZAR EL PROCESO------------------------------------------------------------------------------------

                            if (!expedientesRelanzar.isEmpty()) {
                                int i = 0;
                                for (RegistroBatchVO expedientePendiente : expedientesRelanzar) {
                                    /* for (int i = 0; i < 2; i++) {// para limitar registros
                                    RegistroBatchVO expedientePendiente = expedientesRelanzar.get(i);*/
                                    i++;
                                    log.info("           =====> RELANZAR REGISTROS - ciclo " + i);
                                    numExpedientePend = "";
                                    tieneExpediente = false;
                                    boolean libre = false;
                                    log.debug("idRegBatch: " + expedientePendiente.getId());
                                    log.debug("anoRegistro: " + expedientePendiente.getEjerRegistro());
                                    log.debug("numRegistro: " + expedientePendiente.getNumRegistro());
                                    if (expedientePendiente.getNumExpediente() != null) {
                                        log.debug("numExpedientePend: " + expedientePendiente.getNumExpediente());
                                        numExpedientePend = expedientePendiente.getNumExpediente();
                                    }

                                    log.debug("codtramite: " + expedientePendiente.getCodTramite());
                                    idRegBatch = expedientePendiente.getId();
                                    anoRegistro = expedientePendiente.getEjerRegistro();
                                    numRegistro = expedientePendiente.getNumRegistro();

                                    codTramite = expedientePendiente.getCodTramite();

                                    continuar = true;

                                    RegistroAerteVO registroAERTE = meLanbide77DAO.getRegistroAERTE(anoRegistro, numRegistro, con);
                                    //Se pone el campo 'Relanzar' de la tabla MELANBIDE77_REG_BATCH a 2.
                                    continuar = meLanbide77DAO.ponerEstadoRelanzado(idRegBatch, con);

                                    if (continuar) {
                                        // compruebo si el registro ya está asociado a algun expediente
                                        libre = meLanbide77DAO.esRegistroLibre(anoRegistro, numRegistro, con);

                                        if ("".equals(numExpedientePend)) {// habia fallado al iniciar expediente
                                            //expediente no iniciado
                                            if (libre) {
                                                log.info("Iniciamos nuevo expediente AERTE");
                                                expediente = inicioExpedienteAERTE(registroAERTE, inf, binding);
                                                log.debug("sale de inicioExpedienteAERTE");
                                                if (expediente.getIdExpedienteVO().getNumeroExpediente() == null) {
                                                    continuar = false;
                                                } else {
                                                    numExpedientePend = expediente.getIdExpedienteVO().getNumeroExpediente();
                                                    // tercero = expediente.getInteresados()[0].getTercero();
                                                    log.info("generarMisGestionesInicio() : BEGIN");
                                                    respuestaMisGest = misGestInicioOficio(numExpedientePend);
                                                    log.info("generarMisGestionesInicio() : END");
                                                    registroBatch.setEjerRegistro(registroAERTE.getResEje());
                                                    registroBatch.setNumRegistro(registroAERTE.getResNum());
                                                    registroBatch.setNumExpediente(numExpediente);
                                                    registroBatch.setCodTramite(codTramInicio);
                                                    registroBatch.setOperacion("generarMisGestInicio");
                                                    registroBatch.setRelanzar(0);
                                                    registroBatch.setCodOperacion(OP_MISGESTIONES_INICIO_EXPEDIENTE);
                                                    if (respuestaMisGest.equalsIgnoreCase("0")) {
                                                        registroBatch.setResultado("OK");
                                                        registroBatch.setObservaciones("");
                                                    } else {
                                                        // error en misgestiones
                                                        registroBatch.setResultado("KO");
                                                        registroBatch.setObservaciones("Error en generarMisGestionesInicio al iniciar el expediente, al avanzar el tramite se llama a comprobarExpGenerado. ");
                                                    }
                                                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
                                                }
                                            } else {
                                                log.info("El registro " + anoRegistro + "/" + numRegistro + " está asociado a un expediente");
                                                continuar = false;
                                            }
                                        } else {

                                            String[] numeroExpediente = numExpedientePend.split(ConstantesMeLanbide77.BARRA_SEPARADORA);
                                            anoExp = Integer.parseInt(numeroExpediente[0]);
                                            numExp = Integer.parseInt(numeroExpediente[2]);
                                            log.debug("anoExp: " + anoExp);
                                            log.debug("numExp: " + numExp);
                                            IdExpedienteVO idexpediente2 = new IdExpedienteVO(anoExp, numExp, numExpedientePend, codProcedimiento);
                                            expediente.setIdExpedienteVO(idexpediente2);
                                            //  tercero = MeLanbide77DAO.getInstance().getTercero(codOrg, anoRegistro, registro, con);
                                            tieneExpediente = true;
                                        }
                                        tercero = MeLanbide77DAO.getTercero(codOrg, anoRegistro, registroAERTE, con);

                                    }

                                    //pregunta si el expediente está asociado al registro (buscar si existe en la tabla E_EXR con expediente y registro)
                                    if (continuar) {
                                        if (!meLanbide77DAO.buscarRegistroAsociado(numExpedientePend, anoRegistro, numRegistro, con)) {
                                            log.debug("No tiene registro asociado al expediente ");
                                            if (!asociarExpedienteRegistro(registroAERTE, expediente, inf, binding)) {
                                                continuar = false;
                                            } else {
                                                // grabo la operacion expediente
                                                try {
                                                    registrarAsociar(numExpedientePend, registroAERTE, con);
                                                } catch (Exception e) {
                                                    log.error("Ha ocurrido un error al grabar la operacion en Historico Operaciones. ", e);
                                                }
                                            }// ! asocia
                                        }// biscar registro
                                    }

                                    if (continuar) {
                                        //Buscar en qué trámite está
                                        codTramite = meLanbide77DAO.buscarTramite(numExpedientePend, con);
                                        asunto = registroAERTE.getResAsu();
                                        if ((codTramite == codTramInicio) || (codTramite == codTramRevision)) {
                                            numSolicitud = Integer.parseInt(asunto.substring(33, 38));
                                            log.debug("Num Solicitud: " + numSolicitud);
                                            log.debug("antes de buscar SOLICITUD ");
                                            solicitud = meLanbide77DAO.cargarDatosSolicitud(numSolicitud, con);
                                            int numSolRecuperado = solicitud.getNumSolicitud();
                                            log.debug("Recupera el: " + numSolRecuperado);
                                            log.debug("despues de buscar SOLICITUD ");
                                            solicitud.setNumSolicitud(numSolicitud);

                                            registroBatch.setEjerRegistro(registroAERTE.getResEje());
                                            registroBatch.setNumRegistro(registroAERTE.getResNum());
                                            registroBatch.setNumExpediente(numExpedientePend);
                                            registroBatch.setCodTramite(codTramite);
                                            registroBatch.setOperacion("buscar SOLICITUD");
                                            registroBatch.setRelanzar(0);
                                            registroBatch.setCodOperacion(OP_BUSCAR_SOLICITUD);
                                            registroBatch.setObservaciones("");

                                            if (solicitud.getNumSolicitud() == null) {
                                                registroBatch.setNumSolicitud(numSolicitud);
                                                registroBatch.setResultado("KO");
                                                registroBatch.setObservaciones("No existe solicitud con número " + numSolicitud + " en la tabla " + ConfigurationParameter.getParameter(ConstantesMeLanbide77.TABLA_SOLICITUDES, ConstantesMeLanbide77.FICHERO_PROPIEDADES));
                                                MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);

                                                ErrorBean err = new ErrorBean();
                                                err.setIdError("TRAMITACION_AERTE_003");
                                                err.setMensajeError("Error en el job de Tramitación automática de agencias AERTE.  No existe solicitud " + numSolicitud + " en el registro: " + anoRegistro + "/" + numRegistro);
                                                err.setSituacion("JobExecute");
                                                String causa = "";
                                                String error = "";
                                                grabarError(err, error, causa, numExpediente);

                                                continuar = false;
                                            } else {
                                                registroBatch.setObservaciones("Recuperados los datos de la solicitud " + numSolicitud);
                                                registroBatch.setResultado("OK");
                                                registroBatch.setNumSolicitud(numSolRecuperado);
                                                MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);

                                                solicitud.setNumSolicitud(numSolRecuperado);
                                            }
                                        } else {

                                        }
                                    }

                                    if (continuar) {
                                        //Buscar en qué trámite está
                                        codTramite = meLanbide77DAO.buscarTramite(numExpedientePend, con);
                                        if ((codTramite == codTramInicio) || (codTramite == codTramRevision)) {
                                            if (!grabadoDatosSuplementariosSolicitud(solicitud, numExpedientePend, registroAERTE, con)) {
                                                continuar = false;
                                            }

                                        }
                                    }

                                    idTramite = new TramiteVO();
                                    idTram = new IdTramiteVO();
                                    idTramAvanzar = new IdTramiteVO();
                                    condFinalizacion = new CondicionFinalizacionVO();
                                    FlujoFinalizacionVO flujo = new FlujoFinalizacionVO();
                                    IdTramiteVO[] listaRespuesta = new IdTramiteVO[1];
                                    String tipoFin = "";

                                    if (continuar) {
                                        codTramite = meLanbide77DAO.buscarTramite(numExpedientePend, con);
                                        if (codTramite == codTramInicio) {
                                            idTram.setCodTramite(codTramite);
                                            idTram.setOcurrencia(1);
                                            idTramite.setId(idTram);
                                            idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                            idTramite.setUsuarioFin(registroAERTE.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   						
                                            idTramite.setFechaFin(Calendar.getInstance());
                                            tipoFin = meLanbide77DAO.buscarTipoFinalizacion(codOrg, codProcedimiento, codTramite, con);
                                            condFinalizacion.setTipoFinalizacion(tipoFin);
                                            flujo.setTipoApertura(1);   // 1--> Obligatoria
                                            //Recogemos el Código interno del trámite a iniciar del properties:
                                            idTramAvanzar.setCodTramite(codTramRevision);
                                            idTramAvanzar.setOcurrencia(1);
                                            listaRespuesta[0] = idTramAvanzar;
                                            flujo.setListaRespuesta(listaRespuesta);
                                            flujo.setTipoApertura(1);   // 1--> Obligatoria     
                                            condFinalizacion.setFlujoSI(flujo);
                                            idTramite.setCondFinalizacion(condFinalizacion);
                                            // finalizar trámite 10 y avanzar al 20
                                            if (!finalizarTramite(registroAERTE, expediente, idTramite, condFinalizacion, inf, binding)) {
                                                continuar = false;
                                                log.error("no finaliza tramite. Reg :" + registroAERTE + " Exp.: " + numExpedientePend + " Tramite" + idTramite + " Tipo Fin: " + tipoFin + " Cod.FinS: " + condFinalizacion + " - " + inf);
                                            }
                                        } else {
                                            log.info("El expediente " + numExpedientePend + " ya está en " + codTramite + "-REVISION DOCUMENTACION");
                                        }
                                    }

                                    if (continuar) {

                                    }
                                }//for int i - para limitar registros
                                //    }// for registros pendientes
                            }// !expedientesRelanzar.isEmpty
                            else {
                                log.info("NO hay expedientes a relanzar");
                            }
//---------------------------------------------------------------------------------------------
                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }

                            try {
                                if (con != null && !con.isClosed()) {
                                    con.close();
                                }
                                log.debug("Conexión cerrada en el OAD");
                            } catch (SQLException sqle) {
                                log.error("*** ConexionBD: " + sqle.toString());
                                throw new BDException(999, "Error, no se pudo cerrar la conexion", sqle.toString());
                            }
                        }//while
                    } catch (Exception e) {
                        log.error("Error en el job de INICIO AUTOMATICO EXPEDIENTE  de AERTE: ", e);
                        e.printStackTrace();

                        try {
                        } catch (Exception i) {
                            log.error("Error en la función actualizarError: " + i.getMessage());
                        }

                        try {
                            throw e;
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(InicioAutomaticoExpedientesAERTEJob.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                java.util.logging.Logger.getLogger(InicioAutomaticoExpedientesAERTEJob.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }//synchronized
            }//para local quitar
        } catch (Exception ex) {
            log.error("Error: " + ex);
        }

    }

    public ExpedienteVO inicioExpedienteAERTE(RegistroAerteVO registro, InfoConexionVO inf, WSTramitacionBindingStub binding) {
        log.info("begin inicioExpedienteAERTE()");
        RespuestasTramitacionVO value = null;
        int versionTercero = 1;
        int codDomTercero = 0;

        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(this.codOrganizacion);
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;

        try {
            con = adaptador.getConnection();

            ExpedienteVO exp = new ExpedienteVO();
            IdExpedienteVO idexpediente = new IdExpedienteVO();
            idexpediente.setEjercicio(Calendar.getInstance().get(Calendar.YEAR));//año actual
            int anioActual = idexpediente.getEjercicio();
            idexpediente.setProcedimiento(codProcedimiento);
            int usuarioRegistro = registro.getResUsu();
            log.info("Usuario Registro " + usuarioRegistro);

            exp.setIdExpedienteVO(idexpediente);
            exp.setUor(codigoUOR);
            exp.setUorTramiteInicio(codigoUOR);
            exp.setUsuario(usuarioRegistro);
            exp.setAsunto(registro.getResAsu());
            log.info("Asunto registro: " + exp.getAsunto());

            InteresadoExpedienteVO interesado = new InteresadoExpedienteVO();
            interesado = MeLanbide77Manager.getInstance().getDatosInteresado(codOrg, anioActual, registro, con);
            List<InteresadoExpedienteVO> interesados = new ArrayList<InteresadoExpedienteVO>();
            interesados.add(interesado);
            exp.setInteresados(interesados.toArray(new InteresadoExpedienteVO[]{}));

            versionTercero = MeLanbide77DAO.getInstance().getVersionTerceroRegistro(registro.getResTer(), registro.getResEje(), registro.getResNum(), con);
            codDomTercero = MeLanbide77DAO.getInstance().getCodDomTercero(registro.getResTer(), versionTercero, con);

            RegistroAsociadoVO registroAsociado = new RegistroAsociadoVO();
//obligatorios
            registroAsociado.setCodDepartamento(registro.getResDep());
            registroAsociado.setEjercicioAnotacion(registro.getResEje());
            registroAsociado.setNumAsiento(registro.getResNum());
            registroAsociado.setTipoEntrada(registro.getResTip());
            registroAsociado.setUorRegistro(registro.getResUor());
            registroAsociado.setTipoOrigen(0);

            ///Datos Registro
            log.info("------------Datos Registro----------");
            log.info("codDepartamento " + registroAsociado.getCodDepartamento());
            log.info("ejercicioAnotacion " + registroAsociado.getEjercicioAnotacion());
            log.info("numAsiento " + registroAsociado.getNumAsiento());
            log.info("tipoEntrada " + registroAsociado.getTipoEntrada());
            log.info("uorRegistro " + registroAsociado.getUorRegistro());

            log.info("------------Datos Interesado----------");
            log.info("Rol: " + interesado.getRol());
            log.info("-----------------Datos de remitente/Datos del Tercero---------------");
            log.info("Código: " + registro.getResTer());
            log.info("Versión: " + versionTercero);
            log.info("Apellido 1 remitente: " + interesado.getTercero().getAp1());
            log.info("Apellido 2 remitente: " + interesado.getTercero().getAp2());
            log.info("Nombre: " + interesado.getTercero().getNombre());
            log.info("Documento: " + interesado.getTercero().getDoc());
            log.info("TipoDoc: " + interesado.getTercero().getTipoDoc());
            log.info("Email: " + interesado.getTercero().getEmail());
            log.info("Telefono: " + interesado.getTercero().getTelefono());
            log.info("----------------------fin Datos de remitente/Datos del Tercero-----------");

            log.info("------------Datos Domicilio----------");
            log.info("Domicilio Código: " + codDomTercero);
            log.info("Domicilio Bloque: " + interesado.getTercero().getDomicilio().getBloque());
            log.info("Domicilio COdMunicipio: " + interesado.getTercero().getDomicilio().getCodMunicipio());
            log.info("Domicilio CodPais: " + interesado.getTercero().getDomicilio().getCodPais());
            log.info("Domicilio Cpostal: " + interesado.getTercero().getDomicilio().getCodPostal());
            log.info("Domicilio CodProvincia: " + interesado.getTercero().getDomicilio().getCodProvincia());
            log.info("Domicilio Emplazamiento: " + interesado.getTercero().getDomicilio().getEmplazamiento());
            log.info("Domicilio Esprincipal : " + interesado.getTercero().getDomicilio().isEsPrincipal());
            log.info("Domicilio Escalera: " + interesado.getTercero().getDomicilio().getEscalera());
            log.info("Domicilio Nombre: " + interesado.getTercero().getDomicilio().getNombreVia());
            log.info("Domicilio Planta: " + interesado.getTercero().getDomicilio().getPlanta());
            log.info("Domicilio Portal: " + interesado.getTercero().getDomicilio().getPortal());
            log.info("Domicilio PrimerNumero: " + interesado.getTercero().getDomicilio().getPrimerNumero());
            log.info("Domicilio PrimeraLetra: " + interesado.getTercero().getDomicilio().getPrimeraLetra());
            log.info("Domicilio Puerta: " + interesado.getTercero().getDomicilio().getPuerta());
            log.info("Domicilio TipoVia: " + interesado.getTercero().getDomicilio().getTipoVia());
            log.info("Domicilio UltimaLetra: " + interesado.getTercero().getDomicilio().getUltimaLetra());
            log.info("Domicilio UltimoNumero: " + interesado.getTercero().getDomicilio().getUltimoNumero());
            log.info("------------FIN Datos Domicilio----------");

            log.info("------------datos que enviamos al expediente ----------");

            log.info("------------UOR----------");
            log.info("Uor: " + exp.getUor());
            log.info("uorTramiteInicio: " + exp.getUorTramiteInicio());
            log.info("Usuario: " + exp.getUsuario());
            log.info("Asunto entrada: " + exp.getAsunto());
            log.info("------------FIN DATOS UOR----------");
            ///Expediente 
            log.info("------------Datos Expediente----------");
            //IdExpedientesVO            
            log.info("------------ID EXPEDIENTE1----------");
            log.info("Ejercicio " + idexpediente.getEjercicio());
            log.info("PROCEDIMIENTO " + idexpediente.getProcedimiento());
            log.info("------------FIN ID EXPEDIENTE1----------");

            log.info("------------Llamamos a ws tramitacion iniciarExpedienteRegistro() ----------");
            RegistroBatchVO registroBatch = new RegistroBatchVO();
            String numExpediente = "";
            try {
//                value = binding.iniciarExpedienteConOperacion(exp, inf);
//                value = binding.iniciarExpediente(exp, inf);
                value = binding.iniciarExpedienteRegistro(exp, registroAsociado, inf);
                log.info("------------Respuesta de iniciarExpedienteRegistro() ----------");
                log.info("Documento: " + value.getDocumento());
                log.info("Expediente: " + value.getExpediente());
                log.info("Ejercicio: " + value.getIdExpediente().getEjercicio());
                log.info("Número: " + value.getIdExpediente().getNumero());
                log.info("Numero_Expediente: " + value.getIdExpediente().getNumeroExpediente());
                log.info("Procedimiento: " + value.getIdExpediente().getProcedimiento());
                //casca porque getExpediente() == null
                log.info("------------Fin Respuesta de iniciarExpedienteRegistro----------");
                log.info("idTramite: " + value.getIdtramite());
                log.info("Tramite: " + value.getTramite());
                log.info("Status: " + value.getStatus());
                log.info("Error: " + value.getError());
                if (value.getIdExpediente().getNumeroExpediente() != null) {
//                if (value.getStatus() == 0) {
                    numExpediente = value.getIdExpediente().getNumeroExpediente();
                    registroBatch.setResultado("OK");
                    registroBatch.setObservaciones("Iniciado el expediente " + numExpediente);
                    registroBatch.setEjerRegistro(registro.getResEje());
                    registroBatch.setNumRegistro(registro.getResNum());
                    registroBatch.setNumSolicitud(registro.getNumSolicitud());
                    registroBatch.setNumExpediente(numExpediente);
                    registroBatch.setCodTramite(codTramInicio);
                    registroBatch.setOperacion("iniciarExpedienteRegistro");
                    registroBatch.setRelanzar(0);
                    registroBatch.setCodOperacion(OP_INICIO_EXPEDIENTE);
                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
                    log.info("------------Fin Llamada a ws tramitacion iniciarExpedienteRegistro ----------");

                    ExpedienteVO expediente = new ExpedienteVO();
                    IdExpedienteVO idexpediente2 = new IdExpedienteVO(value.getIdExpediente().getEjercicio(), value.getIdExpediente().getNumero(), value.getIdExpediente().getNumeroExpediente(), value.getIdExpediente().getProcedimiento());
                    expediente.setIdExpedienteVO(idexpediente2);

                    //observaciones
                    // direccion tercero
                    // operacion expediente
                    GeneralValueObject infoExp = new GeneralValueObject();

                    // ALTA
                    infoExp.setAtributo("codMunicipio", codOrganizacion);
                    infoExp.setAtributo("codProcedimiento", codProcedimiento);
                    infoExp.setAtributo("ejercicio", String.valueOf(registro.getResEje()));
                    infoExp.setAtributo("numero", numExpediente);
                    infoExp.setAtributo("usuario", String.valueOf(registro.getResUsu()));
                    infoExp.setAtributo("nomUsuario", "ADMINISTRADOR");
                    //codUOR 
                    infoExp.setAtributo("asunto", registro.getResAsu());
                    String observaciones = " ";
                    infoExp.setAtributo("observaciones", observaciones);
                    infoExp.setAtributo("tipoAlta", ConstantesDatos.TIPO_ALTA_EXP_WEBSERVICE_FLEXIA);
                    infoExp.setAtributo("codTramite", String.valueOf(codTramInicio));
                    infoExp.setAtributo("codTercero", String.valueOf(registro.getResTer()));
                    // ASIENTO
                    // procedimientoAsiento - munProcedimiento - tipoAsiento - ejercicioAsiento - numeroAsiento - codUnidadRegistro - codDepartamento
                    infoExp.setAtributo("procedimientoAsiento", codProcedimiento);
                    infoExp.setAtributo("munProcedimiento", codOrganizacion);
                    infoExp.setAtributo("tipoAsiento", registro.getResTip());
                    infoExp.setAtributo("ejercicioAsiento", String.valueOf(registro.getResEje()));
                    infoExp.setAtributo("numeroAsiento", String.valueOf(registro.getResNum()));
                    infoExp.setAtributo("codUnidadRegistro", String.valueOf(registro.getResUor()));
                    infoExp.setAtributo("codDepartamento", String.valueOf(registro.getResDep()));
                    // TERCERO
                    // codDomicilio - version - codRol
                    infoExp.setAtributo("codDomicilio", String.valueOf(codDomTercero));
                    infoExp.setAtributo("version", String.valueOf(versionTercero));
                    infoExp.setAtributo("codRol", String.valueOf(interesado.getRol()));
                    try {
                        OperacionesExpedienteDAO.getInstance().registrarAltaExpediente(infoExp, con);
                        log.debug("Grabado en Operaciones Expedientes el Alta de expediente");
                    } catch (TechnicalException ex) {
                        log.error("Ha ocurrido un error al grabar la operacion Alta de expediente en Historico Operaciones. " + ex);
                    }

                    // alta tercero
                    TercerosValueObject tercero = new TercerosValueObject();
                    tercero.setIdentificador(String.valueOf(registro.getResTer()));
                    tercero.setVersion(String.valueOf(versionTercero));
                    tercero.setIdDomicilio(String.valueOf(codDomTercero));
                    tercero.setCodRol(interesado.getRol());
                    tercero.setNombre(interesado.getTercero().getNombre());
                    tercero.setApellido1(interesado.getTercero().getAp1());
                    tercero.setApellido2(interesado.getTercero().getAp2());
                    tercero.setTipoDocumento(interesado.getTercero().getTipoDoc());
                    tercero.setDocumento(interesado.getTercero().getDoc());

                    if (interesado.getTercero().getTelefono() != null) {
                        tercero.setTelefono(interesado.getTercero().getTelefono());
                    }
                    if (interesado.getTercero().getEmail() != null) {
                        tercero.setEmail(interesado.getTercero().getEmail());
                    }

                    try {
                        OperacionesExpedienteDAO.getInstance().registrarAltaInteresado(codOrg, numExpediente, registro.getResUsu(), "ADMINISTRADOR", tercero, con);
                        log.info("Se ha registrado lel alta del interesado en el Histórico de Operaciones.");
                    } catch (TechnicalException e) {
                        log.error("Ha ocurrido un error al grabar la operacion Alta Interesadoen Historico Operaciones. ", e);
                    }
                    // tercero
                    return expediente;

                } else {
                    log.error("------------Error en iniciarExpedienteRegistro() ----------");
                    registroBatch.setResultado("KO");
                    registroBatch.setRelanzar(1);
                    registroBatch.setNumExpediente(numExpediente);
                    registroBatch.setObservaciones("Fallo al iniciarExpedienteRegistro. Volverá a intentarlo en la siguiente ejecución");
                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AERTE_001");
                    err.setMensajeError("Error en el job de inicio automático de expedientes AERTE. Error al iniciar el expediente. Status: " + value.getStatus() + ". Error: " + value.getError());
                    err.setSituacion("JobExecute");

                    String causa = "";
                    String error = value.getError();

                    numExpediente = value.getIdExpediente().getNumeroExpediente();
                    grabarError(err, error, causa, numExpediente);
                    return null;
                }

            } catch (Exception e) {
                registroBatch.setResultado("KO");
                registroBatch.setObservaciones("Excepción en iniciarExpedienteRegistro");
                registroBatch.setEjerRegistro(registro.getResEje());
                registroBatch.setNumRegistro(registro.getResNum());
                registroBatch.setNumSolicitud(registro.getNumSolicitud());
                registroBatch.setNumExpediente("");
                registroBatch.setCodTramite(codTramInicio);
                registroBatch.setOperacion("iniciarExpediente");
                registroBatch.setCodOperacion(OP_INICIO_EXPEDIENTE);
                registroBatch.setRelanzar(1);
                MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);

                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AERTE_001");
                err.setMensajeError("Error en el job de inicio automático de expedientes AERTE. Error al llamar al SW iniciarExpedienteRegistro ");
                err.setSituacion("JobExecute");
                String causa = "";
                //String causa = e.getCause().getMessage();
                String error = e.getMessage();

                numExpediente = "";
                grabarError(err, error, causa, numExpediente);
                return null;
            }

        } catch (javax.xml.rpc.ServiceException jre) {
            log.error("Error en INICIO EXPEDIENTE AERTE: ", jre);
            return null;
        } catch (Exception ex) {
            log.error("Error en INICIO EXPEDIENTE AERTE: ", ex);
            return null;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

    }

    public boolean asociarExpedienteRegistro(RegistroAerteVO registro, ExpedienteVO expediente, InfoConexionVO inf, WSTramitacionBindingStub binding) {
        log.info("begin asociarExpedienteRegistro()");
        RegistroBatchVO registroBatch = new RegistroBatchVO();
        int tramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide77.INICIO_EXPEDIENTE, ConstantesMeLanbide77.FICHERO_PROPIEDADES));
        String numExpediente = expediente.getIdExpedienteVO().getNumeroExpediente();
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(codOrganizacion);
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();

            log.info("------------Llamamos al procedure asociarexpregistro() ----------");

            registroBatch.setEjerRegistro(registro.getResEje());
            registroBatch.setNumRegistro(registro.getResNum());
            registroBatch.setNumSolicitud(registro.getNumSolicitud());
            registroBatch.setNumExpediente(numExpediente);
            registroBatch.setCodTramite(tramite);
            registroBatch.setOperacion("asociarExpRegistro");
            registroBatch.setRelanzar(0);
            registroBatch.setCodOperacion(OP_ASOCIAR_REGISTRO);
            registroBatch.setObservaciones("");

            try {
                String[] salida = MeLanbide77DAO.getInstance().asociarExpedienteRegistroBBDD(registro.getResEje(), registro.getResNum(), numExpediente, con);
                String resultado = salida[0];
                String mensajeProc = salida[1];

                log.info("------------Respuesta de asociarexpregistro----------");
                log.info("resultado: " + resultado);
                log.info("mensaje: " + mensajeProc);

                if ("OK".equalsIgnoreCase(resultado)) {
                    registroBatch.setObservaciones(mensajeProc);
                    registroBatch.setResultado("OK");
                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
                    return true;
                } else {
                    registroBatch.setResultado("KO");
                    registroBatch.setObservaciones(mensajeProc);
                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);

                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AERTE_002");
                    err.setMensajeError("Error al ejecutar asociarexpregistro para expediente " + numExpediente + " y registro " + registro.getResEje() + "/" + registro.getResNum() + ". Mensaje: " + mensajeProc);
                    err.setSituacion("JobExecute");
                    grabarError(err, mensajeProc, "", numExpediente);
                    return false;
                }

            } catch (Exception e) {
                registroBatch.setResultado("KO");
                registroBatch.setRelanzar(1);
                registroBatch.setObservaciones("Excepcin en asociarexpregistro");
                MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);

                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AERTE_002");
                err.setMensajeError("Error en el job de inicio automtico de expedientes AERTE. Error al ejecutar asociarexpregistro para expediente " + numExpediente + " y registro " + registro.getResEje() + "/" + registro.getResNum());
                err.setSituacion("JobExecute");
                String causa = "";
                if (e.getCause() != null) {
                    causa = e.getCause().getMessage();
                }
                String error = e.getMessage();

                grabarError(err, error, causa, numExpediente);
                return false;
            }
        } catch (Exception ex) {
            log.error("Error en ASOCIAR EXPEDIENTE AL REGISTRO AERTE: ", ex);
            return false;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean grabadoDatosSuplementariosSolicitud(SolicitudAerteVO solicitud, String numExpediente, RegistroAerteVO registro, Connection con) {
        log.info("Entra en grabadoDatosSuplementariosSolicitud");
        RegistroBatchVO registroBatch = new RegistroBatchVO();
        List<GeneralValueObject> listaCamposExpediente = new ArrayList<GeneralValueObject>();
        GeneralValueObject infoExp = new GeneralValueObject();

        boolean ret = false;
        int resSup = 1;
        String codCampo = "";
        String valorCampo = "";
        String tipoCampo = "";
        String nombreTabla = "";
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

// cargar lista
        GeneralValueObject generalVO;
        if (solicitud.getFecCaducidad() != null) {
            codCampo = "FECCADUCIDAD";
            valorCampo = formatoFecha.format(solicitud.getFecCaducidad());
            generalVO = new GeneralValueObject();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getNumCuenta() != null && solicitud.getNumCuenta().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "NUMCUENTA";
            valorCampo = solicitud.getNumCuenta();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
// EMPRESA1
        if (solicitud.getNomEmp1() != null && solicitud.getNomEmp1().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "NOMEMP1";
            valorCampo = solicitud.getNomEmp1();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getCifEmp1() != null && solicitud.getCifEmp1().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "CIFEMP1";
            valorCampo = solicitud.getCifEmp1();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getTpJor1() != null && solicitud.getTpJor1().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "TPJOR1";
            valorCampo = solicitud.getTpJor1();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getPorParc1() != null && solicitud.getPorParc1().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "PORPARC1";
            valorCampo = solicitud.getPorParc1();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
// EMPRESA2       
        if (solicitud.getNomEmp2() != null && solicitud.getNomEmp2().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "NOMEMP2";
            valorCampo = solicitud.getNomEmp2();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getCifEmp2() != null && solicitud.getCifEmp2().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "CIFEMP2";
            valorCampo = solicitud.getCifEmp2();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getTpJor2() != null && solicitud.getTpJor2().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "TPJOR2";
            valorCampo = solicitud.getTpJor2();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getPorParc2() != null && solicitud.getPorParc2().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "PORPARC2";
            valorCampo = solicitud.getPorParc2();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
// EMPRESA3       
        if (solicitud.getNomEmp3() != null && solicitud.getNomEmp3().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "NOMEMP3";
            valorCampo = solicitud.getNomEmp3();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getCifEmp3() != null && solicitud.getCifEmp3().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "CIFEMP3";
            valorCampo = solicitud.getCifEmp3();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getTpJor3() != null && solicitud.getTpJor3().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "TPJOR3";
            valorCampo = solicitud.getTpJor3();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }
        if (solicitud.getPorParc3() != null && solicitud.getPorParc3().length() > 0) {
            generalVO = new GeneralValueObject();
            codCampo = "PORPARC3";
            valorCampo = solicitud.getPorParc3();
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_CODIGO, codCampo);
            generalVO.setAtributo(ConstantesMeLanbide77.TAG_VALOR, valorCampo);
            listaCamposExpediente.add(generalVO);
        }

        boolean hayNuevos = false;
        String listaCamposGrabados = "";
        int camposGrabados = 0;
        if (listaCamposExpediente.isEmpty()) {
            log.error("grabadoDatosSuplementariosSolicitud: Error al leer el fichero Solicitud");
        } else {
            //Guarda los datos suplementarios 
            if (!listaCamposExpediente.isEmpty()) {
                registroBatch.setEjerRegistro(registro.getResEje());
                registroBatch.setNumRegistro(registro.getResNum());
                registroBatch.setNumSolicitud(registro.getNumSolicitud());
                registroBatch.setNumExpediente(numExpediente);
                registroBatch.setCodTramite(Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide77.INICIO_EXPEDIENTE, ConstantesMeLanbide77.FICHERO_PROPIEDADES)));
                registroBatch.setOperacion("grabadoDatosSuplementariosSolicitud");
                registroBatch.setCodOperacion(OP_GRABAR_DATOS_SUPLEMENTARIOS);
                registroBatch.setRelanzar(0);
                registroBatch.setObservaciones("");

                try {

                    for (GeneralValueObject gv : listaCamposExpediente) {
                        codCampo = (String) gv.getAtributo(ConstantesMeLanbide77.TAG_CODIGO);
                        log.info("Codigo campo: " + codCampo);
                        valorCampo = (String) gv.getAtributo(ConstantesMeLanbide77.TAG_VALOR);
                        tipoCampo = MeLanbide77DAO.getInstance().getTipoCampo(codOrg, codProcedimiento, codCampo, con);
                        log.info("Tipo campo: " + tipoCampo);
                        nombreTabla = MeLanbide77DAO.getInstance().getNombreTabla(Integer.parseInt(tipoCampo), con);
                        log.info("Nombre tabla: " + nombreTabla);
                        if (!MeLanbide77DAO.getInstance().existeCampoSuplementarioAERTE(codOrg, numExpediente, codCampo, valorCampo, nombreTabla, con)) {
                            if (resSup == 1) {
                                resSup = MeLanbide77DAO.getInstance().grabarCampoSuplementarioAERTE(codOrg, numExpediente, codCampo, valorCampo, nombreTabla, con);
                                listaCamposGrabados += codCampo;
                                hayNuevos = true;
                                if (camposGrabados < listaCamposExpediente.size() - 2) {
                                    listaCamposGrabados += " \n - ";
                                } else if (camposGrabados == listaCamposExpediente.size() - 2) {
                                    listaCamposGrabados += " \n y ";
                                }
                            }
                        } else {
                            log.info("Ya existe en el expediente");
                        }
                        ret = true;
                        camposGrabados++;
                    }
                } catch (Exception ex) {
                    log.error("Error en grabadoDatosSuplementariosSOLICITUD en expediente: " + numExpediente + " ,código: " + codCampo + " valor: " + valorCampo + " nombre tabla: " + nombreTabla, ex);
                    ret = false;
                    //GRABAR EN LA NUEVA TABLA   
                    registroBatch.setRelanzar(1);
                    registroBatch.setResultado("KO");
                    registroBatch.setObservaciones("Excepción al grabar " + codCampo + " valor: " + valorCampo);
                    try {
                        MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
                    } catch (Exception ex1) {
                        log.error("Error al InsertarRegistroBatch" + ex1);
                    }

                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AERTE_004");
                    err.setMensajeError("Error en el job de Tramitación automática de agencias AERTE. Error al grabar " + codCampo + " valor: " + valorCampo + " con el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum());
                    err.setSituacion("JobExecute");
                    String causa = ex.getCause().getMessage();
                    String error = ex.getMessage();

                    grabarError(err, error, causa, numExpediente);
                }
            }
            if (resSup == 0) {
                log.error("grabadoDatosSuplementariosSOLICITUD: No ha guardado los datos suplementarios del fichero. Puede que no hayan o que ya existían");
                registroBatch.setResultado("--");
                registroBatch.setRelanzar(0);
                registroBatch.setObservaciones("No ha grabado los Datos Suplementarios de la SOLICITUD. Puede ser que no haya datos o que ya existían.");
                try {
                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
                } catch (Exception ex1) {
                    log.error("Error al InsertarRegistroBatch " + ex1);
                }
            } else {
                registroBatch.setResultado("OK");
                registroBatch.setRelanzar(0);
                registroBatch.setObservaciones("Se ha(n) grabado " + camposGrabados + " campo(s) suplementario(s) de " + listaCamposExpediente.size() + " campo(s) de la solicitud");
                try {
                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);
                } catch (Exception ex1) {
                    log.error("Error al InsertarRegistroBatch " + ex1);
                }
                if (hayNuevos) //operacion expediente
                {
                    infoExp.setAtributo("codMunicipio", codOrganizacion);

                    infoExp.setAtributo("codProcedimiento", codProcedimiento);
                    infoExp.setAtributo("ejercicio", String.valueOf(registro.getResEje()));
                    infoExp.setAtributo("numero", numExpediente);
                    infoExp.setAtributo("usuario", String.valueOf(registro.getResUsu()));
                    infoExp.setAtributo("nomUsuario", "ADMINISTRADOR");
                    infoExp.setAtributo("asunto", registro.getResAsu());
                    String observaciones = "Se han grabado los campos suplementarios: \n" + listaCamposGrabados + " \nde la solicitud Web " + solicitud.getNumSolicitud() + ".";
                    infoExp.setAtributo("observaciones", observaciones);
                    try {
                        OperacionesExpedienteDAO.getInstance().registrarGrabarExpediente(infoExp, con);
                        log.info("Se ha registrado la grabación de suplementarios en el Histórico de Operaciones.");
                    } catch (TechnicalException ex) {
                        log.error("Ha ocurrido un error al grabar la operacion en Historico Operaciones. " + ex);
                    }
                }
            }
        }
        return ret;
    }

    private boolean finalizarTramite(RegistroAerteVO registro, ExpedienteVO expediente, TramiteVO idTramite, CondicionFinalizacionVO condFinalizacion, InfoConexionVO inf, WSTramitacionBindingStub binding) {
        log.info("begin finalizarTramite()");
        RespuestasTramitacionVO value;
        RegistroBatchVO registroBatch = new RegistroBatchVO();
        String numExpediente = expediente.getIdExpedienteVO().getNumeroExpediente();

        String nomTramInicio = null;
        String nomTramRevision = null;

        int ocuTramIniciar = 1;
        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fechaFin = null;
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(codOrganizacion);
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();
            try {
                nomTramInicio = MeLanbide77DAO.getInstance().getNombreTramite(codProcedimiento, codTramInicio, con);
                nomTramRevision = MeLanbide77DAO.getInstance().getNombreTramite(codProcedimiento, codTramRevision, con);
            } catch (Exception e) {
            }
            log.info("------------datos que enviamos a finalizar trámite ----------");
            ///Expediente 
            log.info("------------Datos Expediente----------");
            //IdExpedientesVO            
            log.info("------------idExpedienteVO:----------");
            log.info("PROCEDIMIENTO " + expediente.getIdExpedienteVO().getProcedimiento());
            log.info("Ejercicio " + expediente.getIdExpedienteVO().getEjercicio());
            log.info("EXPEDIENTE " + expediente.getIdExpedienteVO().getNumeroExpediente());
            log.info("------------FIN ID EXPEDIENTE1----------");
            //TramiteVO:            
            log.info("------------TramiteVO:----------");
            log.info("Código interno " + idTramite.getId().getCodTramite());
            log.info("Ocurrencia " + idTramite.getId().getOcurrencia());
            log.info("Utr " + idTramite.getUtr());
            log.info("Usuario fin " + idTramite.getUsuarioFin());
            log.info("------------condicionFinalizacionVO:----------");
            log.info("tipoFinalizacion " + idTramite.getCondFinalizacion().getTipoFinalizacion());
            if (idTramite.getCondFinalizacion().getFlujoSI() != null) {
                log.info("flujoSI:  ");
                log.info("tipoApertura " + idTramite.getCondFinalizacion().getFlujoSI().getTipoApertura());
                for (int i = 0; i < idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta().length; i++) {
                    log.info("Cod.Trámite a iniciar " + i + ": " + idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta()[i].getCodTramite());
                }
                log.info("Ocurrencia Trámite a iniciar " + idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta()[0].getOcurrencia());
//                ocuTramIniciar = idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta()[0].getOcurrencia();
            }
            if (idTramite.getCondFinalizacion().getFlujoNO() != null) {
                log.info("flujoNO:  ");
                log.info("tipoApertura " + idTramite.getCondFinalizacion().getFlujoNO().getTipoApertura());
                log.info("Cod.Trámite a iniciar " + idTramite.getCondFinalizacion().getFlujoNO().getListaRespuesta()[0].getCodTramite());
                log.info("Ocurrencia Trámite a iniciar " + idTramite.getCondFinalizacion().getFlujoNO().getListaRespuesta()[0].getOcurrencia());
//                ocuTramIniciar = idTramite.getCondFinalizacion().getFlujoNO().getListaRespuesta()[0].getOcurrencia();
            }
            //InfoConexionVO:            
            log.info("------------InfoConexionVO:----------");
            log.info("organizacion " + inf.getOrganizacion());
            log.info("aplicacion " + inf.getAplicacion());

            registroBatch.setEjerRegistro(registro.getResEje());
            registroBatch.setNumRegistro(registro.getResNum());
            registroBatch.setNumSolicitud(registro.getNumSolicitud());
            registroBatch.setNumExpediente(expediente.getIdExpedienteVO().getNumeroExpediente());
            registroBatch.setCodTramite(idTramite.getId().getCodTramite());
            registroBatch.setCodOperacion(OP_AVANZAR_TRAMITE);
            registroBatch.setRelanzar(0);
            registroBatch.setObservaciones("");
            registroBatch.setOperacion("finalizaTramite");
            try {
                if ("S".equals(idTramite.getCondFinalizacion().getTipoFinalizacion())) {
                    log.info("------------Llamamos a ws tramitacion finalizaTramite() ----------");
                    value = binding.finalizaTramite(expediente, idTramite, condFinalizacion, inf);
                } else {
                    log.info("------------Llamamos a ws tramitacion finalizaTramiteConOperacion() ----------");
                    value = binding.finalizaTramiteConOperacion(expediente, idTramite, condFinalizacion, inf);
                }
                log.info("------------Respuesta de finalizaTramite----------");

                log.info("Status: " + value.getStatus());
                log.info("------------Fin Respuesta de finalizaTramite----------");

                if (value.getStatus() == 0) {
                    registroBatch.setResultado("OK");
                    registroBatch.setObservaciones("Se ha finalizado el trámite " + nomTramInicio);
                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);

                    // operacion expediente
                    fechaFin = formateadorFecha.format(Calendar.getInstance().getTime());
                    log.debug("Fecha Fin: " + fechaFin);
                    // Finaliza tramite
                    log.debug("Grabar en Operaciones Expedientes el fin e inicio de trámites");
                    TramitacionExpedientesValueObject tramiteFin = new TramitacionExpedientesValueObject();
                    tramiteFin.setCodMunicipio(codOrganizacion);
                    tramiteFin.setCodProcedimiento(codProcedimiento);
                    tramiteFin.setEjercicio(String.valueOf(expediente.getIdExpedienteVO().getEjercicio()));
                    tramiteFin.setNumero(numExpediente);
                    tramiteFin.setCodTramite(String.valueOf(idTramite.getId().getCodTramite()));
                    tramiteFin.setOcurrenciaTramite(String.valueOf(idTramite.getId().getOcurrencia()));
                    tramiteFin.setTramite(nomTramInicio);
                    tramiteFin.setNumeroExpediente(numExpediente);
                    tramiteFin.setCodUsuario(String.valueOf(idTramite.getUsuarioFin()));
                    tramiteFin.setNombreUsuario("ADMINISTRADOR");
                    //nomUsuario 
                    tramiteFin.setFechaFin(fechaFin);

                    try {
                        OperacionesExpedienteDAO.getInstance().registrarFinalizarTramite(tramiteFin, false, con);
                        log.debug("Grabado en Operaciones Expedientes el fin de trámite");
                    } catch (TechnicalException ex) {
                        log.error("Ha ocurrido un error al grabar la operacion Finalizar Trámite en Historico Operaciones. ", ex);
                    }
                    // Inicia tramite
                    GeneralValueObject tramiteIni = new GeneralValueObject();
                    tramiteIni.setAtributo("codMunicipio", codOrganizacion);
                    tramiteIni.setAtributo("codProcedimiento", codProcedimiento);
                    tramiteIni.setAtributo("ejercicio", String.valueOf(expediente.getIdExpedienteVO().getEjercicio()));
                    tramiteIni.setAtributo("numero", numExpediente);
                    tramiteIni.setAtributo("usuario", String.valueOf(idTramite.getUsuarioFin()));
                    tramiteIni.setAtributo("nombreUsuario", "ADMINISTRADOR");
                    tramiteIni.setAtributo("codTramite", String.valueOf(codTramRevision));
                    tramiteIni.setAtributo("nomTramite", nomTramRevision);
                    tramiteIni.setAtributo("ocurrTramite", String.valueOf(ocuTramIniciar));
                    tramiteIni.setAtributo("fechaInicioTramite", fechaFin);

                    try {
                        OperacionesExpedienteDAO.getInstance().registrarIniciarTramite(tramiteIni, false, con);
                        log.debug("Grabado en Operaciones Expedientes el inicio de trámite");
                    } catch (TechnicalException e) {
                        log.error("Ha ocurrido un error al grabar la operacion Finalizar Trámite en Historico Operaciones. ", e);
                    }
                    return true;
                } else {
//                    registroBatch.setRelanzar(1);
                    registroBatch.setResultado("KO");
                    registroBatch.setObservaciones("Error al finalizarTramite " + nomTramInicio + ": " + value.getError());
                    MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);

                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AERTE_005");
                    err.setMensajeError("Error en el job de inicio automático de expedientes AERTE. Error al avanzar el trámite " + nomTramInicio + ": " + value.getError());
                    err.setSituacion("JobExecute");
                    String causa = "";
                    String error = value.getError();
                    grabarError(err, error, causa, numExpediente);
                    return false;
                }

            } catch (Exception e) {
                registroBatch.setRelanzar(1);
                registroBatch.setResultado("KO");
                registroBatch.setObservaciones("Excepción en finalizarTramite");
                MeLanbide77DAO.getInstance().insertarRegistroBatch(registroBatch, con);

                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AERTE_005");
                err.setMensajeError("Error en el job de inicio automático de expedientes AERTE. Error al avanzar el trámite " + idTramite.getId().getCodTramite());
                err.setSituacion("JobExecute");
                String causa = e.getCause().getMessage();
                String error = e.getMessage();
                grabarError(err, error, causa, numExpediente);
                return false;
            }

        } catch (Exception ex) {
            log.error("Error al AVANZAR EL TRÁMITE ", ex);
            return false;

        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {

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
                // Conexiï¿½n al esquema genï¿½rico
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
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conGenerico != null && !conGenerico.isClosed()) {
                    conGenerico.close();
                }
            }// finally

        }// synchronized
        return adapt;
    }//getConnection

    private void grabarError(ErrorBean error, String excepError, String causa, String numExp) {
        try {
            log.error("grabando el error");
            error.setMensajeExcepError(excepError); //churro e.getException
            error.setTraza(excepError);
            error.setCausa(causa);//causa    .getMensaje.getcausa
            log.error("causa: " + causa);
            log.error("numExp: " + numExp);
            if ("".equals(numExp)) {
                numExp = "0000/ERR/000000";
            }

            String idProcedimiento = "";
//            idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide77.ID_PROC_AERTE, ConstantesMeLanbide77.FICHERO_ADAPTADORES);
            idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(numExp.split(ConstantesMeLanbide77.BARRA_SEPARADORA)[1]); //convierteProcedimiento(codProcedimiento);
            log.error("idProcedimiento: " + idProcedimiento);
            error.setIdProcedimiento(idProcedimiento);
            error.setSistemaOrigen("REGEXLAN");
            error.setIdClave("");
            error.setErrorLog("flexia_debug");
            error.setIdFlexia(numExp);

            String respGrabarError = RegistroErrores.registroError(error);
            log.error("Respuesta RegistroErrores.registroError(): " + respGrabarError);
        } catch (Exception ex) {
            log.error("Error al grabarError" + ex);
        }
    }

    private String misGestInicioOficio(String numExpediente) throws Exception {
        log.info("begin misGestInicioOficio()");
        AdaptadorSQLBD adaptador = null;
        String codigoOperacion = "";

        try {
            adaptador = this.getAdaptSQLBD(codOrganizacion);
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        String mensaje = "";
        String idGest = null;
        MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();

        try {
            //LAMAR A MIS GESTIONES INICIO                            

            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adaptador);
            date = dateFormat.parse(fechaIni);
            String fecha = meLanbide43Manager.verificarFecha(codProcedimiento, adaptador);
            Participantes par = meLanbide43Manager.obtenerDatosParticipantes(numExpediente, adaptador);
            log.debug("PARTICIPANTES: " + par);
            log.info("tipo id: " + par.getTipoID());
            if (par.getTipoID() != 0) {
                Date fec = null;
                if (!fecha.isEmpty()) {
                    fec = dateFormat.parse(fecha);
                    if (date.after(fec)) {
                        log.info("Llama a guardarGestiones()");
                        idGest = MeLanbide43Manager.getInstance().guardarGestiones(numExpediente, codOrg, adaptador, codTramInicio, "I");
                        log.info("Id generado: " + idGest);
                        log.info("Llama a avanzarGestiones()");
                        codigoOperacion = MeLanbide43Manager.getInstance().avanzarGestiones(idGest, codOrganizacion, adaptador);
                    }
                }
            }

        } catch (Exception ex) {
            codigoOperacion = "1";
            meLanbide43Manager.borrarProcesado(idGest, adaptador);
            mensaje = MeLanbide43I18n.getInstance().getMensaje(1, "error.errorGen");
            log.error(mensaje);
            //insertar error en registro de errores           
            log.error("Error en la función generarMisGestionesOficio: ", ex);
            String error = "Error en la función generarMisGestionesOficio: " + ex.getMessage() != null ? ex.getMessage() : "null";

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TRAMITACION_AERTE_006");
            errorB.setMensajeError("Error en el job de inicio automático de expedientes AERTE. Error al iniciar un expediente en la función generarMisGestionesOficio");
            errorB.setSituacion("JobExecute");

            grabarError(errorB, error, ex.toString(), numExpediente);
            //fin 

            throw ex;
        }
        return codigoOperacion;
    }

    private void registrarAsociar(String numExpediente, RegistroAerteVO registroAERTE, Connection con) throws Exception {
        //operacion expediente
        String[] numeroExpediente = numExpediente.split(ConstantesMeLanbide77.BARRA_SEPARADORA);
        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
        String fechaOper = null;
        operacion.setCodMunicipio(codOrg);
        operacion.setEjercicio(Integer.parseInt(numeroExpediente[0]));
        operacion.setNumExpediente(numExpediente);
        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ANHADIR_RELACION);// TIPO_MOV_ANHADIR_RELACION
        operacion.setFechaOperacion(new GregorianCalendar());
        operacion.setCodUsuario(registroAERTE.getResUsu());
        try {
            fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()))
                    + " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
        } catch (Exception e) {
            log.error("Ha ocurrido un error al convertir la fecha de la operacion a String. ", e);
        }
        // CREAR DESCRIPCION
        String descripcion = "<div class=\"movExpC1\">ASOCIAR ENTRADA DE REGISTRO</div>"
                + "<div class=\"movExpOr\">{eMovExpOperWSTram}</div>"
                + "<div class=\"movExpC2\">{gEtiq_exped}</div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpCodProc}:</div><div class=\"movExpVal\">" + codProcedimiento + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpEjercicio}:</div><div class=\"movExpVal\">" + numeroExpediente[0] + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpNumExp}:</div><div class=\"movExpVal\">" + numExpediente + "</div></div>"
                + "<div class=\"movExpC2\">{etiqRegistro}</div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpTipoAsiento}:</div><div class=\"movExpVal\">" + registroAERTE.getResTip() + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpAnoAsiento}:</div><div class=\"movExpVal\">" + String.valueOf(registroAERTE.getResEje()) + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpNumAsiento}:</div><div class=\"movExpVal\">" + String.valueOf(registroAERTE.getResNum()) + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpAsunto}:</div><div class=\"movExpVal\">" + registroAERTE.getResAsu() + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">Solicitud:</div><div class=\"movExpVal\">" + String.valueOf(registroAERTE.getNumSolicitud()) + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpIntCodTer}:</div><div class=\"movExpVal\">" + String.valueOf(registroAERTE.getResTer()) + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpCodUORAsiento}:</div><div class=\"movExpVal\">" + String.valueOf(codigoUOR) + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpCodDepAsiento}:</div><div class=\"movExpVal\">" + String.valueOf(registroAERTE.getResDep()) + "</div></div>"
                + "<div class=\"movExpC2\">{eMovExpUsuFec}</div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{eMovExpUsuario}:</div><div class=\"movExpVal\">" + String.valueOf(registroAERTE.getResUsu()) + "</div></div>"
                + "<div class=\"movExpLin\"><div class=\"movExpEtiq\">{gEtiqFecOpe}:</div><div class=\"movExpVal\">" + fechaOper + "</div></div>";

        operacion.setDescripcionOperacion(descripcion);
        try {
            OperacionesExpedienteDAO.getInstance().insertarOperacionExpediente(operacion, con);
            log.info("Registrada la operación Asociar Expediente Registro");
        } catch (TechnicalException ex) {
            log.error("Ha ocurrido un error al grabar la operacion en Historico Operaciones. ", ex);
        }
    }
    private String codOrganizacion;
    private int codOrg;
    private String codProcedimiento;
    private int codigoUOR;
    private int codTramInicio;
    private int codTramRevision;

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public int getCodOrg() {
        return codOrg;
    }

    public void setCodOrg(int codOrg) {
        this.codOrg = codOrg;
    }

    public String getCodProcedimiento() {
        return codProcedimiento;
    }

    public void setCodProcedimiento(String codProcedimiento) {
        this.codProcedimiento = codProcedimiento;
    }

    public int getCodigoUOR() {
        return codigoUOR;
    }

    public void setCodigoUOR(int codigoUOR) {
        this.codigoUOR = codigoUOR;
    }

    public int getCodTramInicio() {
        return codTramInicio;
    }

    public void setCodTramInicio(int codTramInicio) {
        this.codTramInicio = codTramInicio;
    }

    public int getCodTramRevision() {
        return codTramRevision;
    }

    public void setCodTramRevision(int codTramRevision) {
        this.codTramRevision = codTramRevision;
    }

}
