package es.altia.flexia.integracion.moduloexterno.melanbide43.util;

import com.ejie.rdr.xml.ApoderadoType;
import com.ejie.x43A.rri.ws.xml.TContacto;
import com.ejie.x43A.rri.ws.xml.TCuenta;
import com.ejie.x43A.rri.ws.xml.TInteresado;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.DomiciliosDAO;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.business.util.OperacionesExpedienteHelper;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.RGI_JobDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.util.MELANBIDE43_GestionRdR_Constantes;
import es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.util.MELANBIDE43_GestionRdR_Util;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.DatosAvisoCSRegexlan;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.NotificacionBVO;
import es.altia.flexia.notificacion.persistence.AutorizadoNotificacionManager;
import es.altia.flexia.notificacion.vo.AutorizadoNotificacionVO;
import es.altia.flexiaWS.documentos.bd.util.Configuracion;
import es.altia.util.commons.DateOperations;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Expediente;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Interesado;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Participacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.servicios.Lan6InformarConsultaServicios;
import es.lanbide.lan6.adaptadoresPlatea.notificaciones.beans.Lan6TramiteNotificacion;
import es.lanbide.lan6.adaptadoresPlatea.rdr.beans.Lan6Apoderado;
import es.lanbide.lan6.adaptadoresPlatea.rdr.beans.Lan6DatosAvisos;
import es.lanbide.lan6.adaptadoresPlatea.rdr.beans.Lan6DatosInteresados;
import es.lanbide.lan6.adaptadoresPlatea.rdr.servicios.Lan6RdRServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author kigonzalez
 */
public class RGI_IMV_JobUtils {

    private final Logger log = LogManager.getLogger(RGI_IMV_JobUtils.class);
    GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
    Gson gson = gsonB.serializeNulls().create();
    //Instancia
    private static RGI_IMV_JobUtils instance = null;

    public static RGI_IMV_JobUtils getInstance() {
        if (instance == null) {
            instance = new RGI_IMV_JobUtils();
        }
        return instance;
    }

    public RGI_IMV_JobUtils() {
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param esActivo
     * @param idioma
     * @param codIdNotificacion
     * @param codProcPlatea
     * @param adapt
     * @return
     */
    public Map<String, String> validarREA(int codOrg, String numExp, boolean esActivo, int idioma, int codIdNotificacion, String codProcPlatea, AdaptadorSQLBD adapt) {
        log.info("validarREA - Begin () ");
        Map<String, String> repreRespuestaValidacion = new HashMap<String, String>();
        String arregloParamMensaRespuesta[] = null;
        MELANBIDE43_GestionRdR_Util MELANBIDE43_GestionRdR_Util_Instace = MELANBIDE43_GestionRdR_Util.getInstance();
        Connection conRea = null;
        String paramsAdaptador[] = null;
        String codProcedimiento = numExp.split(ConstantesDatos.BARRA)[1];
        String docTercero = "";
        ArrayList<Lan6Apoderado> listaRespuestaRepresentante = new ArrayList<Lan6Apoderado>();
        Lan6DatosInteresados lan6DatosInteresados = new Lan6DatosInteresados();
        TInteresado lan6DatosCuentaInteresado = null;
        paramsAdaptador = Configuracion.getParamsBD(String.valueOf(codOrg));
        try {
            int rolInteresado = Integer.parseInt(ConfigurationParameter.getParameter(MELANBIDE43_GestionRdR_Constantes.CODIGO_ROL_INTERESADO_EXPTS_TELETRAMITACION, MELANBIDE43_GestionRdR_Constantes.FICHERO_PROPIEDADES_RdR));
            int rolRepresentante = Integer.parseInt(ConfigurationParameter.getParameter(MELANBIDE43_GestionRdR_Constantes.CODIGO_ROL_REPRE_EXPTS_TELETRAMITACION, MELANBIDE43_GestionRdR_Constantes.FICHERO_PROPIEDADES_RdR));
            conRea = adapt.getConnection();
            ArrayList<Participantes> interesados = RGI_JobDAO.getInstance().obtenerTerceroPorRolRGI(numExp, esActivo, rolInteresado, conRea);
            ArrayList<Participantes> representantes = RGI_JobDAO.getInstance().obtenerTerceroPorRolRGI(numExp, esActivo, rolRepresentante, conRea);
            // Solo deberia ser un interesado como solicitante si no hay o hay mas de 1 no se puede validar la representacion
            if (interesados != null && !interesados.isEmpty()) {
                if (interesados.size() == 1) {
                    docTercero = interesados.get(0).getNif();
//                    log.info("Validamos al interesado con documento nº " + docTercero);
                } else {
                    log.info("Hay mas de 1 Interesados principal como solicitantes en el expediente : " + numExp + " No se puede validar la representacion legal.");
                    arregloParamMensaRespuesta = new String[]{numExp};
                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("12", idioma, arregloParamMensaRespuesta);
                    return repreRespuestaValidacion;
                }
            } else {
//                log.info("No hay Interesados principales como solicitantes en el expediente : " + numExp + " No se puede validar la representacion legal.");
                arregloParamMensaRespuesta = new String[]{numExp};
                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("11", idioma, arregloParamMensaRespuesta);
                return repreRespuestaValidacion;
            }

            try {
                Lan6RdRServicios servicios = new Lan6RdRServicios(codProcedimiento);
                lan6DatosInteresados = servicios.recuperarDatosInteresados(docTercero);
                log.debug("llamada ejecutada a recuperarDatosInteresados -  Respuesta(Lan6DatosInteresados) " + (lan6DatosInteresados != null ? " Correcta." : " Recibida a Null."));
                if (lan6DatosInteresados == null) {
                    arregloParamMensaRespuesta = new String[]{docTercero, numExp, "Respuesta del servicios recibida a Null."};
                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("107", idioma, arregloParamMensaRespuesta);
                    return repreRespuestaValidacion;
                }
            } catch (Lan6Excepcion e) {
                log.error("Error al llamar a los Servicios de platea para verificar la validez de los representantes : " + e.getMessage(), e);
                String codigosError = (e.getCodes() != null ? Arrays.toString(e.getCodes().toArray()) : "");
                String mensajeError = (e.getMessages() != null ? Arrays.toString(e.getMessages().toArray()) : "");
                log.error("Mensaje de Excepcion compuesto : " + ("[" + codigosError + "]-[" + mensajeError + "]"));
                arregloParamMensaRespuesta = new String[]{docTercero, numExp, codigosError + " : " + mensajeError};
                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("107", idioma, arregloParamMensaRespuesta);
                    return repreRespuestaValidacion;
            }

            Lan6DatosAvisos lan6DatosAvisos = lan6DatosInteresados.getDatosAvisos();

            if (lan6DatosInteresados.getResultGrantorRepresentationsLan6() != null) {
                listaRespuestaRepresentante = lan6DatosInteresados.getResultGrantorRepresentationsLan6().getListaRepresentantes();
            } else {
                log.info("Datos del titular recuperados. Pero no se recibe la lista de Representantes. getResultGrantorRepresentationsLan6");
            }
            if (representantes != null && !representantes.isEmpty()) {
                log.info("Hay " + representantes.size() + " REPRESENTANTE(s)");
                // Hemos recogido los datos de los representantes del expediente. Iniciamos comprobacion contra platea
                // Caso en el que existen en regexlan  
                if (listaRespuestaRepresentante != null && !listaRespuestaRepresentante.isEmpty()) {
                    for (Participantes participante : representantes) {
                        log.info("Validamos al REPRESENTANTE con documento nº " + participante.getNif());
                        ApoderadoType repre = getLan6ApoderadoFromListaRespuestaxNIF(participante.getNif(), listaRespuestaRepresentante);
                        boolean isValidRepresentation = (repre != null && repre.getIdApoderado() != null && repre.getIdApoderado().equalsIgnoreCase(participante.getNif()));
                        if (!isValidRepresentation) {
                            log.info("El representante registrado en Rgexlan no es valido o no coincide con el que esta en REA - Procedemos a borrar de Regexlan el Representante y marcar la actualizacion.");
                            log.info(" Paso 1. - Damos de alta los Representantes buenos recibidos desde REA - Cuando no se ha validado uno existente en Regexlan. ");
                            ApoderadoType representanteDadoAlta = null;
                            for (Lan6Apoderado lan6Apoderado : listaRespuestaRepresentante) {
                                ApoderadoType apoderadoType = lan6Apoderado.getApoderadoType();
                                if (apoderadoType != null) {
                                    repreRespuestaValidacion = comprobarActualizarRepreFromRdRToRegexlan(apoderadoType, codOrg, numExp, esActivo, rolInteresado, idioma, codIdNotificacion, conRea);
                                    representanteDadoAlta = apoderadoType;
                                    log.info("Respuesta Invocacion Caso con Repre En Regexlan : " + apoderadoType.getIdApoderado() + " --> " + repreRespuestaValidacion.get("codigo") + "-" + repreRespuestaValidacion.get("descripcion"));
                                    //Actualizamos los datos de notificacion 
                                    try {
                                        comprobarDatosAvisoNotificacionVsREA(codOrg, numExp, esActivo, lan6DatosAvisos, conRea);
                                    } catch (Exception e) {
                                        log.error("Error al cmoprobar los datos de notificacion despues de validado una representacion, invocado desde JOB notificaciones de RGI / IMV. "
                                                + " continua la operacion de validacion representante .. " + numExp, e);
                                    }
                                }
                            }
                            // Paso 2 Quitar de Regexlan el que no esta validado
                            // Se eliminara solo si no hay representantes vigentes en RdR 
                            log.info("Paso 2. - Procedemos a dar de baja en Regexlan el representante en el Expediente - Cuando no se ha validado uno existente en Regexlan. y existe uno valido ");
                            // Al eliminar necesitamos el id Especifico del Tercero, con su codigo y numero de version.
                            TercerosValueObject tercero = RGI_JobDAO.getInstance().getDatosTerceroRdRxNifExpteRol(numExp, esActivo, participante.getNif(), rolRepresentante, conRea);
                            if (RGI_JobDAO.getInstance().eliminarRepresentanteLegalExpedienteRdR(tercero, codOrg, numExp, esActivo, rolRepresentante, conRea)) {
                                // Actualizamos la tabla  AUTORIZADO_NOTIFICACION con los datos del nuevo representante, numero de expediente e id notificacion asociada
                                AutorizadoNotificacionVO autorizadoEnvioNotificacion = new AutorizadoNotificacionVO();
                                autorizadoEnvioNotificacion.setNumeroExpediente(numExp);
                                autorizadoEnvioNotificacion.setEjercicio(MELANBIDE43_GestionRdR_Util_Instace.getEjercicioDsdNumExpediente(numExp));
                                autorizadoEnvioNotificacion.setCodMunicipio(String.valueOf(codOrg));
                                autorizadoEnvioNotificacion.setCodigoTercero(Integer.parseInt(tercero.getIdentificador()));
                                autorizadoEnvioNotificacion.setNumeroVersionTercero(Integer.parseInt(tercero.getVersion()));
                                autorizadoEnvioNotificacion.setCodigoNotificacion(codIdNotificacion);
                                AutorizadoNotificacionManager autorizadoNotificacionManager = AutorizadoNotificacionManager.getInstance();
                                if (autorizadoNotificacionManager.eliminarAutorizado(autorizadoEnvioNotificacion, paramsAdaptador)) {
                                    log.info("Dato del nuevo representante guardado correctamente en la tabla de autorizados para el envio de la notificaicon." + tercero.getDocumento() + "/" + tercero.getIdentificador() + "/" + tercero.getVersion());
                                } else {
                                    log.error("NO se ha podido insertar el nuevo representante en la tabla de autorizados para el envio de la notificacion");
                                }
                                // Procedemos a actualizar Mi Carpeta y Registrar la operacion en el Historico de el Expediente
                                String respuestaActualizaInteresadosCod = "";
                                String MensajeErrorActInteresado = "";
                                String[] respuestaActualizaInteresados = actualizarInteresadosMiCarpetaGestionRdR(codOrg, numExp, esActivo, conRea);
                                respuestaActualizaInteresadosCod = respuestaActualizaInteresados[0];
                                MensajeErrorActInteresado = respuestaActualizaInteresados[1];
                                if ("0".equalsIgnoreCase(respuestaActualizaInteresadosCod)) {
                                    OperacionExpedienteVO operacion = new OperacionExpedienteVO();
                                    operacion.setCodMunicipio(codOrg);
                                    operacion.setEjercicio(Integer.parseInt(numExp.split(ConstantesDatos.BARRA)[0]));
                                    operacion.setNumExpediente(numExp);
                                    operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ELIMINAR_INTERESADO);
                                    operacion.setCodUsuario(MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN);
                                    String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()))
                                            + " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
                                    operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionAltaInteresado(tercero, MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN, MELANBIDE43_GestionRdR_Constantes.LOGIN_NOMBRE_USUARIO_ADMIN, fechaOper));

                                    RGI_JobDAO.getInstance().insertarHistoricoExp(operacion, esActivo, conRea);

                                    log.info("Eliminado el ROL de REPRESENTANTE LEGAL del expediente : " + numExp + " NIF : " + participante.getNif());
                                    arregloParamMensaRespuesta = new String[]{participante.getNif(), numExp};
                                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("8", idioma, arregloParamMensaRespuesta);
                                    if (representanteDadoAlta != null) {
                                        arregloParamMensaRespuesta = new String[]{participante.getNif(), numExp, representanteDadoAlta.getIdApoderado()};
                                        repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("15", idioma, arregloParamMensaRespuesta);
                                    }
                                } else {
                                    log.info(" Representante no existe en RdR, eliminado en Regexlan Correctamente. Pero no actualizado en Mi carpeta." + numExp + "/" + participante.getNif() + "/ CodTercero: " + tercero.getCodTerceroOrigen() + " - " + MensajeErrorActInteresado);
                                    arregloParamMensaRespuesta = new String[]{participante.getNif(), numExp, MensajeErrorActInteresado};
                                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("9", idioma, arregloParamMensaRespuesta);
                                }
                            } else {
                                log.info("No se ha podido eliminar el ROL de REPRESENTANTE LEGAL del expediente : " + numExp + " NIF : " + participante.getNif());
                                arregloParamMensaRespuesta = new String[]{participante.getNif(), numExp};
                                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("7", idioma, arregloParamMensaRespuesta);
                            }

                        } else {
                            try {
                                comprobarDatosAvisoNotificacionVsREA(codOrg, numExp, esActivo, lan6DatosAvisos, conRea);
                            } catch (Exception e) {
                                log.error("Error al comprobar los datos de notificacion despues de validado un representacion. ", e);
                                arregloParamMensaRespuesta = new String[]{participante.getNif(), numExp, e.getMessage()};
                                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("106", idioma, arregloParamMensaRespuesta);
                                return repreRespuestaValidacion;
                            }
                        }
                    }
                } else {
                    log.info("No Damos de baja, aunque no es valido el de Regexlan, no existe en REA ninguno, por tanto se notificara a la empresa para que de de alta uno valido en REA ");
                    // Sino hay en REA invocamos recuperarCuentaInteresado - 
                    // Hay que pasar el Documento del Representante
                    for (Participantes participante : representantes) {
                        try {
                            lan6DatosCuentaInteresado = invocarRecuperarCuentaInteresado(participante.getNif(), codProcPlatea, numExp, codIdNotificacion, idioma);
                            if (lan6DatosCuentaInteresado != null) {
                                TCuenta[] arregloDatos = lan6DatosCuentaInteresado.getCuentaList() != null ? lan6DatosCuentaInteresado.getCuentaList().getCuentaArray() : null;
                                if (arregloDatos != null && arregloDatos.length > 0) {
                                    for (TCuenta cuentaInteresado : arregloDatos) {
                                        lan6DatosAvisos.setIdCuentaRepreVigente(String.valueOf(cuentaInteresado.getCuentaID()));
                                        TContacto[] tContactoList = cuentaInteresado.getContactoList() != null ? cuentaInteresado.getContactoList().getContactoArray() : null;
                                        if (tContactoList != null) {
                                            for (TContacto tContacto : tContactoList) {
                                                if ("EMAIL".equalsIgnoreCase(String.valueOf(tContacto.getTipoContacto()))) {
                                                    log.info("email-RepreRegexlan-CuentaINteresado: " + tContacto.getInformacionContacto());
                                                    lan6DatosAvisos.setMailRepreVigente(tContacto.getInformacionContacto());

                                                } else if ("SMS".equalsIgnoreCase(String.valueOf(tContacto.getTipoContacto()))) {
                                                    log.info("sms-RepreRegexlan-CuentaINteresado: " + tContacto.getInformacionContacto());
                                                    lan6DatosAvisos.setSmsRepreVigente(tContacto.getInformacionContacto());
                                                }
                                            }
                                        } else {
                                            log.info("Sin Datos de contacto en la cuenta interesado - representante :  " + participante.getNif());
                                        }
                                    }
                                } else {
                                    log.info("Representante Sin cuenta de Interesado con consentimiento");
                                }
                            } else {
                                log.info("Sin Datos cuenta de Interesado con consentimiento");
                            }
                        } catch (Lan6Excepcion e) {
                            log.error("Error al llamar a los Servicios de platea para recuperar los datos de cuenta interesado en Regexlan para validar el  representante: " + e.getMessage(), e);
                            arregloParamMensaRespuesta = new String[]{participante.getNif(), numExp, e.getMessage()};
                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("108", idioma, arregloParamMensaRespuesta);
                            return repreRespuestaValidacion;
                        }

                        // Actualizamos Datos de Aviso
                        try {
                            comprobarDatosAvisoNotificacionVsREA(codOrg, numExp, esActivo, lan6DatosAvisos, conRea);
                            String respuestaActualizaInteresadosCod = "";
                            String MensajeErrorActInteresado = "";
                            String[] respuestaActualizaInteresados = actualizarInteresadosMiCarpetaGestionRdR(codOrg, numExp, esActivo, conRea);
                            respuestaActualizaInteresadosCod = respuestaActualizaInteresados[0];
                            MensajeErrorActInteresado = respuestaActualizaInteresados[1];
                            if ("0".equalsIgnoreCase(respuestaActualizaInteresadosCod)) {
                                TercerosValueObject tercero = RGI_JobDAO.getInstance().getDatosTerceroRdRxNifExpteRol(numExp, esActivo, participante.getNif(), rolRepresentante, conRea);
                                log.info("Actualizado datos en Mi carpeta." + numExp + "/" + participante.getNif() + "/  " + MensajeErrorActInteresado);
                            } else {
                                log.info("No actualizado datos en Mi carpeta." + numExp + "/" + participante.getNif() + "/  " + MensajeErrorActInteresado);
                            }
                        } catch (Exception e) {
                            log.error("Error al comprobar los datos de notificacion despues obtener no obtener representantes vigentes en REA, pero si en Regexlan - Representacion Parcial. "
                                    + " continua la operacion de validacion representante .. " + numExp, e);
                        }
                    }
                    String documento = (representantes.size() == 1 ? representantes.get(0).getNif() : "-");
                    log.info("participantesRepre.get(0).getNif() : " + representantes.get(0).getNif());
                    // Verificamos si ya se ha validado la docu para no mostrar el mensaje
                    String codDOCACREDRDAVAL = ConfigurationParameter.getParameter(MELANBIDE43_GestionRdR_Constantes.CODIGO_CAMPOSUP_EXPTE_DOCACREDITATIVA_VALIDADA, MELANBIDE43_GestionRdR_Constantes.FICHERO_PROPIEDADES_RdR);
                    String valorCodDOCACREDRDAVAL = null;
                    if (codDOCACREDRDAVAL != null && !codDOCACREDRDAVAL.isEmpty()) {
                        valorCodDOCACREDRDAVAL = RGI_JobDAO.getInstance().getDatosNotificacionElectronica(numExp, esActivo, codDOCACREDRDAVAL, conRea);
                    }
                    if (valorCodDOCACREDRDAVAL != null && !valorCodDOCACREDRDAVAL.isEmpty() && "S".equalsIgnoreCase(valorCodDOCACREDRDAVAL)) {
                        arregloParamMensaRespuesta = new String[]{"Documento Acreditativo Adjuntado y validado en Expediente."};
                        repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("0", idioma, arregloParamMensaRespuesta);
                    } else {
                        arregloParamMensaRespuesta = new String[]{numExp, documento};
                        repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("13", idioma, arregloParamMensaRespuesta);
                    }
                }
            } else {
                // Sino hay representantes en el expediente tenemos que consultar si lo hay en el REA para darlo de alta en Regexlan.
//                log.info(" No se han recuperado Interesados con el ROL REPRESENTANTE para el expediente en REGEXLAN. Comprobamos si lo hay en REA." + numExp);
                // Suponiendo que nos devuelvan una lista
                //     log.info(" Paso 1. - Damos de alta los Representantes buenos recibidos desde RdR - Cuando no se ha validado uno existente en Regexlan. ");
                if (listaRespuestaRepresentante != null && !listaRespuestaRepresentante.isEmpty()) {
                    for (Lan6Apoderado lan6Apoderado : listaRespuestaRepresentante) {
                        ApoderadoType apoderadoType = lan6Apoderado.getApoderadoType();
                        if (apoderadoType != null) {
                            // Actualizamos datos de aviso, para que al invocar actualizacion Mi carpeta se pasen los actuales
                            try {
                                comprobarDatosAvisoNotificacionVsREA(codOrg, numExp, esActivo, lan6DatosAvisos, conRea);
                            } catch (Exception e) {
                                log.error("Error al cmoprobar los datos de notificacion despues de validado una representacion, invocado desde la plugin de notificaciones. "
                                        + " continua la operacion de validacion representante .. " + numExp, e);
                            }
                            repreRespuestaValidacion = comprobarActualizarRepreFromRdRToRegexlan(apoderadoType, codOrg, numExp, esActivo, rolRepresentante, idioma, codIdNotificacion, conRea);
//                            log.info("Respuesta Invocacion Caso SIN Repre En Regexlan : " + apoderadoType.getIdApoderado() + " --> " + repreRespuestaValidacion.get("codigo") + "-" + repreRespuestaValidacion.get("descripcion"));
                        }
                    }
                } else {
//                    log.info(" No se han recuperado Interesados con el ROL REPRESENTANTE para el expediente, ni en Regexlan ni en RdR. " + numExp);
                    // Actualizamos datos de Aviso del titular
                    try {
                        if (comprobarDatosAvisoNotificacionVsREA(codOrg, numExp, esActivo, lan6DatosAvisos, conRea)) {
                            String respuestaActualizaInteresadosCod = "";
                            String MensajeErrorActInteresado = "";
                            String[] respuestaActualizaInteresados = actualizarInteresadosMiCarpetaGestionRdR(codOrg, numExp, esActivo, conRea);
                            respuestaActualizaInteresadosCod = respuestaActualizaInteresados[0];
                            MensajeErrorActInteresado = respuestaActualizaInteresados[1];
                            if ("0".equalsIgnoreCase(respuestaActualizaInteresadosCod)) {
                                log.info("Actualizado datos en Mi carpeta." + numExp + " / " + lan6DatosAvisos.getMailTitular() + " / " + " / " + lan6DatosAvisos.getSmsTitular() + " / " + MensajeErrorActInteresado);
                            } else {
//                                log.info("No se ha Actualizado datos en Mi carpeta." + numExp + " / " + lan6DatosAvisos.getMailTitular() + " / " + " / " + lan6DatosAvisos.getSmsTitular() + " / " + MensajeErrorActInteresado);
                            }
                        } else {
//                            log.info("No se han modificado los datos de aviso en regexlan, no se invoca a actualizarInteresados");

                        }

                    } catch (Exception e) {
                        log.error("Error al actualizar datos de aviso e interesados en mi carpeta despues de validar. No hay representante ni enregexlan ni en REA."
                                + " continua la carga del expediente     de validacion representante .. " + numExp, e);
                    }
                    arregloParamMensaRespuesta = null;
                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("2", idioma, arregloParamMensaRespuesta);
                    return repreRespuestaValidacion;
                }
            }
        } catch (Exception e) {
            log.error("Error al validar el rol de representante del expediente " + numExp + " contra el Registro Electronico de Representante RdR : " + e.getMessage(), e);
            arregloParamMensaRespuesta = new String[]{e.getMessage()};
            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("100", idioma, arregloParamMensaRespuesta);
            return repreRespuestaValidacion;
        } finally {
            try {
                adapt.devolverConexion(conRea);
            } catch (BDException ex) {
                log.error("Error al cerrar la conexion  : " + ex.getMensaje(), ex);
            }
        }
        return repreRespuestaValidacion;
    }

    private ApoderadoType getLan6ApoderadoFromListaRespuestaxNIF(String nif, ArrayList<Lan6Apoderado> listaRespuestaRepresentante) {
        log.info("getLan6ApoderadoFromListaRespuestaxNIF - Begin ()  -  " + nif);
        try {
            for (Lan6Apoderado lan6Apoderado : listaRespuestaRepresentante) {
                if (lan6Apoderado.getApoderadoType() != null) {
                    ApoderadoType a = lan6Apoderado.getApoderadoType();
                    log.debug("getListaProcedimientos " + (a.getListaProcedimientos() != null ? Arrays.toString(a.getListaProcedimientos().getIdProcedimientoArray()) : "Lista a null"));
                    log.debug("getListaActuaciones " + (a.getListaActuaciones() != null ? Arrays.toString(a.getListaActuaciones().getIdActuacionArray()) : "Lista a null"));
                    log.debug("getListaAreasActuaciones " + (a.getListaAreasActuaciones() != null ? Arrays.toString(a.getListaAreasActuaciones().getIdAreaActuacionArray()) : "Lista a null"));
                }
                String nifREA = (lan6Apoderado.getApoderadoType() != null ? lan6Apoderado.getApoderadoType().getIdApoderado() : "");
                if (nif.equalsIgnoreCase(nifREA)) {
                    return lan6Apoderado.getApoderadoType();
                }
            }
        } catch (Exception e) {
            log.error("Error al obtener el apoderado de la lista repuesta de REA por DNI " + e.getMessage());
        }
        return null;
    }

    private Map<String, String> comprobarActualizarRepreFromRdRToRegexlan(ApoderadoType apoderadoType, int codOrg, String numExp, boolean esActivo, int codRol, int idioma, int codIdNotificacion, Connection con) {
        Map<String, String> repreRespuestaValidacion = new HashMap<String, String>();
        String lan6ApoderadoTypeID = apoderadoType != null ? apoderadoType.getIdApoderado() : "";
        String arregloParamMensaRespuesta[] = null;
        MELANBIDE43_GestionRdR_Util MELANBIDE43_GestionRdR_Util_Instace = MELANBIDE43_GestionRdR_Util.getInstance();
        RGI_JobDAO RGI_DAO = RGI_JobDAO.getInstance();
        try {
            log.info("lan6ApoderadoTypeID : " + lan6ApoderadoTypeID);
            log.info("IdNotificacion : " + codIdNotificacion);
            TercerosValueObject tercero = MeLanbide43Manager.getInstance().getDatosBasicosterceroRepreRdR(lan6ApoderadoTypeID, con);
            if (apoderadoType != null && !(tercero != null && tercero.getCodTerceroOrigen() != null && !tercero.getCodTerceroOrigen().isEmpty())) {
                // Procedemos a crear el tercero En regexlan
                log.info("Invocamos dar de alta tercero regexlan " + lan6ApoderadoTypeID + " " + apoderadoType.getNombreApoderado());
                TercerosValueObject terceroVO = new TercerosValueObject();
                terceroVO.setDocumento(lan6ApoderadoTypeID);
                terceroVO.setTipoDocumento(String.valueOf(MELANBIDE43_GestionRdR_Util_Instace.getTipoDocumentoFlexiaFromDocumento(lan6ApoderadoTypeID)));
                terceroVO.setNombre(apoderadoType.getNombre());
                terceroVO.setApellido1(apoderadoType.getApellido1());
                terceroVO.setApellido2(apoderadoType.getApellido2());
                terceroVO.setNormalizado("1");
                terceroVO.setSituacion('A');
                terceroVO.setOrigen("SGE");
                terceroVO.setUsuarioAlta(String.valueOf(MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN));
                terceroVO.setModuloAlta(String.valueOf(MELANBIDE43_GestionRdR_Constantes.CODIGO_APP_GESTOR_EXPEDIENTES));
                terceroVO.setTelefono(apoderadoType.getTelefono());
                terceroVO.setEmail(apoderadoType.getEmail());
                String nombreCompleto = (apoderadoType.getApellido1() != null && !apoderadoType.getApellido1().isEmpty() ? apoderadoType.getApellido1() : "");
                nombreCompleto += (nombreCompleto.isEmpty() ? "" : " ").concat(apoderadoType.getApellido2() != null && !apoderadoType.getApellido2().isEmpty() ? apoderadoType.getApellido2() : "");
                nombreCompleto += (nombreCompleto.isEmpty() ? "" : ", ").concat(apoderadoType.getNombre() != null && !apoderadoType.getNombre().isEmpty() ? apoderadoType.getNombre() : "");
                log.info("Nombre preparado en RGI-IMV REA " + nombreCompleto);
                terceroVO.setNombreCompleto(apoderadoType.getNombreApoderado());
                int idTercero = TercerosManager.getInstance().setTercero(terceroVO, MELANBIDE43_GestionRdR_Util_Instace.getParamsAdaptSQLBD(String.valueOf(codOrg)));
                log.info("Hemos datos de alta el tercero: " + idTercero);

                // Generar Domicilio desconocido del tercero
                DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
                Config m_ConfigCommon = ConfigServiceHelper.getConfig("common");
                domicilio.setIdPais(MELANBIDE43_GestionRdR_Constantes.CODIGO_PAIS_ESPANA);
                domicilio.setIdProvincia(m_ConfigCommon.getString(codOrg + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO));
                domicilio.setIdMunicipio(m_ConfigCommon.getString(codOrg + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO));
                domicilio.setDescVia(m_ConfigCommon.getString(codOrg + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA));
                domicilio.setTipoVia(Integer.toString(es.altia.agora.technical.ConstantesDatos.TIPO_VIA_SINVIA));
                domicilio.setEsDomPrincipal("true");
                log.info("Vamos a dar de alta el Domicilio del tercero ");
                int idDomicilio = DomiciliosDAO.getInstance().altaDomicilio(domicilio, String.valueOf(MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN), con);
                log.info("Domicilio Creado: " + idDomicilio);
                log.info("Relacionar DomicilioTercero : " + TercerosDAO.getInstance().altaDomicilioTercero(idTercero, idDomicilio, String.valueOf(MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN), con));
                // volvemos a recoger los datos del tercero
                tercero = MeLanbide43Manager.getInstance().getDatosBasicosterceroRepreRdR(lan6ApoderadoTypeID, con);
            }

            if (tercero != null && tercero.getCodTerceroOrigen() != null && !tercero.getCodTerceroOrigen().isEmpty()) {
                if (!RGI_DAO.existeDocumentoTerceroExpediente(codOrg, numExp, esActivo, lan6ApoderadoTypeID, codRol, con)) {
                    // Si existe el tercero creamos el ROL para el expediente y lo relacionamos :  insertar una linea en E_EXT
                    boolean insertado = RGI_DAO.insertarTerceroExpediente(codOrg, numExp, esActivo, 2, Integer.parseInt(tercero.getCodTerceroOrigen()), Integer.parseInt(tercero.getVersion()), tercero.getIdDomicilio(), con);

                    if (insertado) {
                        // Actualizamos la tabla  AUTORIZADO_NOTIFICACION con los datos del nuevo representante, numero de expediente e id notificacion asociada
                        try {
                            AutorizadoNotificacionVO autorizadoEnvioNOtificacion = new AutorizadoNotificacionVO();
                            autorizadoEnvioNOtificacion.setNumeroExpediente(numExp);
                            autorizadoEnvioNOtificacion.setEjercicio(MELANBIDE43_GestionRdR_Util_Instace.getEjercicioDsdNumExpediente(numExp));
                            autorizadoEnvioNOtificacion.setCodMunicipio(String.valueOf(codOrg));
                            autorizadoEnvioNOtificacion.setCodigoTercero(Integer.parseInt(tercero.getIdentificador()));
                            autorizadoEnvioNOtificacion.setNumeroVersionTercero(Integer.parseInt(tercero.getVersion()));
                            autorizadoEnvioNOtificacion.setCodigoNotificacion(codIdNotificacion);
                            AutorizadoNotificacionManager autorizadoNotificaiconManager = AutorizadoNotificacionManager.getInstance();
                            if (autorizadoNotificaiconManager.insertarAutorizado(autorizadoEnvioNOtificacion, con)) {
                                log.info("Dato del nuevo representante guardado correctamente en la tabla de autorizados para el envio de la notificaicon." + tercero.getDocumento() + "/" + tercero.getIdentificador() + "/" + tercero.getVersion());
                            } else {
                                log.error("NO se ha podido insertar el nuevo representante en la tabla de autorizados para el envio de la notificacion");
                            }
                        } catch (NumberFormatException e) {
                            log.error("Error al actualizar la tabla AUTORIZADO_NOTIFICACION con un nuevo representante dado de alta en el expediente " + numExp + " - " + lan6ApoderadoTypeID, e);
                        }

                        // Ejecutar llamada a Actualizar Interesado en Mi Carpeta
                        String respuestaActualizaInteresados = "0";
                        String MensajeErrorActInteresado = "";
                        try {
                            log.info(" =======>  se llama a Actualizar Interesados: ");
                            respuestaActualizaInteresados = actualizaInteresadosRGI(codOrg, numExp, esActivo, con);
                        } catch (Exception e) {
                            log.error(" Error al tratar de actualizar interesados en Mi Carpeta al relacionar en Regexlan el representante incrito en REA ", e);
                            respuestaActualizaInteresados = "1";
                            MensajeErrorActInteresado = e.getMessage();
                        }
                        if ("0".equalsIgnoreCase(respuestaActualizaInteresados)) {
                            log.info(" Representante recuperado en REA , creado en Regexlan Correctamente y Actualizado en Mi Carpeta. " + numExp + "/" + lan6ApoderadoTypeID + "/ CodTercero: " + tercero.getCodTerceroOrigen());
                            log.info(" -- Vamos a registrar la operacion en el Historico ");
                            try {
                                tercero.setCodRol(2);
                                tercero.setNotificacionElectronica("1");

                                OperacionExpedienteVO operacion = new OperacionExpedienteVO();
                                operacion.setCodMunicipio(codOrg);
                                operacion.setEjercicio(Integer.parseInt(numExp.split(ConstantesDatos.BARRA)[0]));
                                operacion.setNumExpediente(numExp);
                                operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ALTA_INTERESADO);
                                operacion.setFechaOperacion(new GregorianCalendar());
                                operacion.setCodUsuario(MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN);
                                String fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()))
                                        + " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
                                operacion.setDescripcionOperacion(OperacionesExpedienteHelper.generarDescripcionAltaInteresado(tercero, MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN, MELANBIDE43_GestionRdR_Constantes.LOGIN_NOMBRE_USUARIO_ADMIN, fechaOper));

                                RGI_DAO.insertarHistoricoExp(operacion, esActivo, con);
                            } catch (TechnicalException e) {
                                log.error("Error al registrar la operacion en el historico de operaciones del Expediente : " + e.getMessage(), e);
                            }
                            arregloParamMensaRespuesta = new String[]{lan6ApoderadoTypeID};
                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("3", idioma, arregloParamMensaRespuesta);
                        } else {
                            log.info(" Represenante recuperado en REA y creado en Regexlan Correctamente. Pero no actualizado en Mi carpeta." + numExp + "/" + lan6ApoderadoTypeID + "/ CodTercero: " + tercero.getCodTerceroOrigen() + " - " + MensajeErrorActInteresado);
                            arregloParamMensaRespuesta = new String[]{lan6ApoderadoTypeID, numExp, MensajeErrorActInteresado};
                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("5", idioma, arregloParamMensaRespuesta);
                        }
                    } else {
                        log.info(" Represenante recuperado en REA - Error al insertar el ROL en Regexlan." + numExp + "/" + lan6ApoderadoTypeID);
                        arregloParamMensaRespuesta = new String[]{lan6ApoderadoTypeID};
                        repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("6", idioma, arregloParamMensaRespuesta);
                    }
                } else {
                    // Este puede ser un caso especial, por ejemplo si hay mas de un representa legal en regexlan, el primero que se valida no es corrrecto 
                    // entra aqui porque al leer todos ls buenos de rdr puede ser que el segundo en regexlan sea valido, evitamos volverlo a insertar
                    log.info(" Existe Representante REA en el Expediente y es Valido en REA - No ejecutamos la insercion mas.");
                    arregloParamMensaRespuesta = null;
                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("0", idioma, arregloParamMensaRespuesta);
                }
            } else {
                log.info(" Representante recuperado en REA no encontrado en Regexlan." + numExp + "/" + lan6ApoderadoTypeID);
                arregloParamMensaRespuesta = new String[]{lan6ApoderadoTypeID};
                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("4", idioma, arregloParamMensaRespuesta);
            }
        } catch (Exception e) {
            log.error("Error al actualizar representante en Regexlan con datos desde REA", e);
            arregloParamMensaRespuesta = new String[]{e.getMessage()};
            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("100", idioma, arregloParamMensaRespuesta);
        }
        return repreRespuestaValidacion;
    }

    private String[] actualizarInteresadosMiCarpetaGestionRdR(int codOrganizacion, String numExpediente, boolean esActivo, Connection con) {
        log.info("actualizarInteresadosMiCarpetaGestionRdR - Begin");
        String[] respuestaActualizaInteresados = new String[2];
        try {
            respuestaActualizaInteresados[0] = actualizaInteresadosRGI(codOrganizacion, numExpediente, esActivo, con);
            respuestaActualizaInteresados[1] = "OK";
        } catch (Exception e) {
            log.error(" Error al tratar de actualizar interesados en Mi Carpeta al relacionar en Regexlan el representante incrito en RdR ", e);
            respuestaActualizaInteresados[0] = "1";
            respuestaActualizaInteresados[1] = e.getMessage();
        }
        log.info("actualizarInteresadosMiCarpetaGestionRdR - END ");
        return respuestaActualizaInteresados;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param esActivo
     * @param con
     * @return
     */
    public String actualizaInteresadosRGI(int codOrg, String numExp, boolean esActivo, Connection con) throws Exception {
        log.info("actualizaInteresadosRGI ");
        String mensaje = "0";
        int id = 0;
        int result = 0;
        String[] datosExp = numExp.split(ConstantesDatos.BARRA);
        RGI_JobDAO rgiJobDAO = RGI_JobDAO.getInstance();
        try {
            result = rgiJobDAO.guardarMiGestionRGI(codOrg, numExp, esActivo, con);
            if (result > 0) {
                id = rgiJobDAO.getIdGestiones(con);
                log.info("Id generado al  guardar MiGestion RGI: " + id);
                try {
                    ArrayList<Participantes> listaParticipantes = rgiJobDAO.obtenerInteresadoRepresentanteRGI(numExp, esActivo, con);
                    log.info("despues de recoger participantes ");

                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(datosExp[1]);
                    // Expediente
                    Lan6Expediente lan6Expediente = new Lan6Expediente();
                    lan6Expediente.setNumero(numExp);
                    lan6Expediente.setEjercicio(datosExp[0]);
                    log.info("despues de recoger expediente ");

                    // Participacion
                    Lan6Participacion lan6participacion = new Lan6Participacion();
                    //Interesados
                    ArrayList<Lan6Interesado> interesados = new ArrayList<Lan6Interesado>();
                    for (Participantes participante : listaParticipantes) {
                        log.info("ROL : " + participante.getRol());
                        log.info("nombre : " + participante.getNombre());
                        log.info("nif : " + participante.getNif());
                        Lan6Interesado lan6Interesado = new Lan6Interesado();
                        lan6Interesado.setNumIdentificacion(participante.getNif());
                        //Si es presencial se recogerá el nif de EJIE y no será persona física o no tiene apellido (AUTONOMO)
                        if (participante.getApe1() == null || participante.getApe1().isEmpty() || participante.getTipoID() == 4 || participante.getTipoID() == 5) {
                            lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_CIF);
                            lan6Interesado.setRazonSocial(participante.getNombre());
                        } else {
                            lan6Interesado.setNombre(participante.getNombre());
                            lan6Interesado.setApellido1(participante.getApe1());
                            log.info("apellido1 interesado: " + participante.getApe1());
                            lan6Interesado.setApellido2(participante.getApe2());
                            log.info("apellido2 interesado: " + participante.getApe2());
                            switch (participante.getTipoID()) {
                                case 1:
                                    lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_NIF);
                                    break;
                                case 2:
                                    lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_PASAPORTE);
                                    break;
                                case 3:
                                    lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_NIE);
                                    break;

                                default:
                                    log.error("El tipo " + participante.getTipoID() + " no tiene correspondencia en PLATEA");
                            }
                        }
                        String tipo = "";
                        if (participante.getRol().equals("1")) {
                            tipo = Lan6Constantes.TIPO_INTERESADO_TITULAR;
                        } else {
                            tipo = Lan6Constantes.TIPO_INTERESADO_REPRESENTANTE;
                        }
                        lan6Interesado.setTipo(tipo);
                        log.info("despues de recoger el interesado ");
                        interesados.add(lan6Interesado);

                    }
                    // Leemos datos de Avisos Campos suplementarios Regexlan
                    DatosAvisoCSRegexlan datosAvisoCSRegexlan = getDatosAvisoRGI(numExp, esActivo, con);
                    if (datosAvisoCSRegexlan != null) {
                        if (hayRepresentanteLegalEnListaInteresado(interesados)) {
                            if (datosAvisoCSRegexlan.getAvisoEmailRepresentante() != null && !datosAvisoCSRegexlan.getAvisoEmailRepresentante().isEmpty()) {
                                lan6participacion.setMailsAvisos(Arrays.asList(datosAvisoCSRegexlan.getAvisoEmailRepresentante()));
                            }
                            if (datosAvisoCSRegexlan.getAvisoSmsRepresentante() != null && !datosAvisoCSRegexlan.getAvisoSmsRepresentante().isEmpty()) {
                                lan6participacion.setTfnosAvisos(Arrays.asList(datosAvisoCSRegexlan.getAvisoSmsRepresentante()));
                            }
                        } else {
                            if (datosAvisoCSRegexlan.getAvisoEmailTitular() != null && !datosAvisoCSRegexlan.getAvisoEmailTitular().isEmpty()) {
                                lan6participacion.setMailsAvisos(Arrays.asList(datosAvisoCSRegexlan.getAvisoEmailTitular()));
                            }
                            if (datosAvisoCSRegexlan.getAvisoSmsTitular() != null && !datosAvisoCSRegexlan.getAvisoSmsTitular().isEmpty()) {
                                lan6participacion.setTfnosAvisos(Arrays.asList(datosAvisoCSRegexlan.getAvisoSmsTitular()));
                            }
                        }
                        lan6participacion.setIdioma(datosAvisoCSRegexlan.getAvisoIdioma() != null && !datosAvisoCSRegexlan.getAvisoIdioma().isEmpty() ? datosAvisoCSRegexlan.getAvisoIdioma().toLowerCase() : "es");
                    }
                    lan6participacion.setIdParticipacion(Lan6Constantes.PARTICIPACION_UNICA);
                    lan6participacion.setInteresados(interesados);
                    lan6Expediente.setParticipacion(lan6participacion);

                    // Llamada metodo
                    log.info("Parametros enviados actualizarInteresados - lan6Expediente : " + gson.toJson(lan6Expediente));
                    String res = servicios.actualizarInteresados(lan6Expediente);
                    log.info("Respuesta del metodo actualizarInteresados: " + res);

                    // update en MELANBIDE43_INTEGMISGEST
                    int gestionActualizada = rgiJobDAO.actualizarGestionActInteresados(id, esActivo, con);

                } catch (Exception ex) {
                    log.error("Error en actualizaInteresados: " + ex);
                    mensaje = "2";
                    throw ex;
                }
            } else {
            }

        } catch (Exception ex) {
            log.error(" =======> actualizaInteresadosRGI() -  Error  : ", ex);
            mensaje = "2";
            throw ex;
        }
        return mensaje;
    }

    /**
     * Recupera los datos del interesado desde el sistema de regexlan, Dato de
     * Aviso.
     *
     * @param InteresadoSolicitanteIdPoderdante
     * @param codProcPlatea
     * @param numExp
     * @param codIdNotificacion
     * @param idioma
     * @return TInteresado Con los datos de Aviso en Regexlan del representante
     * con DNI Solicitado
     * @throws Lan6Excepcion
     */
    private TInteresado invocarRecuperarCuentaInteresado(String InteresadoRepresentanteId, String codProcPlatea, String numExp, Integer codIdNotificacion, int idioma) throws Lan6Excepcion {
        log.info(" invocarRecuperarCuentaInteresado " + codProcPlatea + " " + InteresadoRepresentanteId + " " + numExp);
        TInteresado respuesta = null;
        try {
            String[] datosExp = numExp.split(ConstantesDatos.BARRA);
            Lan6RdRServicios servicios = new Lan6RdRServicios(datosExp[1]); // 2.2
            log.debug("Vamos a ejecutar la llamada invocarRecuperarCuentaInteresado  con parametros : " + " InteresadoRepresentanteId : " + InteresadoRepresentanteId);
            respuesta = servicios.recuperarCuentaInteresado(InteresadoRepresentanteId);
            log.debug("llamada ejecutada -  Respuesta(TInteresado) " + (respuesta != null ? " Correcta." : " Recibida a Null."));
        } catch (Lan6Excepcion e) {
            log.error("Error al llamar a los Servicios de platea para verificar la validez de los representantes : " + e.getMessage(), e);
            String codigosError = (e.getCodes() != null ? Arrays.toString(e.getCodes().toArray()) : "");
            String mensajeError = (e.getMessages() != null ? Arrays.toString(e.getMessages().toArray()) : "");
            log.error("Mensaje de Excepcion compuesto : " + ("[" + codigosError + "]-[" + mensajeError + "]"));
            String[] arregloParamMensaRespuesta = new String[]{InteresadoRepresentanteId, numExp, codigosError + " : " + mensajeError};
            Map<String, String> repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("108", idioma, arregloParamMensaRespuesta);
        }
        return respuesta;
    }

    /**
     *
     * @param numExp
     * @param esActivo
     * @param con
     * @return
     */
    public DatosAvisoCSRegexlan getDatosAvisoRGI(String numExp, boolean esActivo, Connection con) {
        RGI_JobDAO rgiJobDAO = RGI_JobDAO.getInstance();
        DatosAvisoCSRegexlan camposDAN = new DatosAvisoCSRegexlan();
        try {
            camposDAN.setAvisoIdCuentaInteresadoRepresentante(rgiJobDAO.getDatosNotificacionElectronica(numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTE, con));
            camposDAN.setAvisoEmailRepresentante(rgiJobDAO.getDatosNotificacionElectronica(numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAIL, con));
            camposDAN.setAvisoSmsRepresentante(rgiJobDAO.getDatosNotificacionElectronica(numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMS, con));
            camposDAN.setAvisoIdCuentaInteresadoTitular(rgiJobDAO.getDatosNotificacionElectronica(numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTETIT, con));
            camposDAN.setAvisoEmailTitular(rgiJobDAO.getDatosNotificacionElectronica(numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAILTIT, con));
            camposDAN.setAvisoSmsTitular(rgiJobDAO.getDatosNotificacionElectronica(numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMSTIT, con));
            camposDAN.setAvisoIdioma(rgiJobDAO.getDatosNotificacionElectronica(numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_IDIOMAAVISOS, con));
        } catch (Exception ex) {
            log.error("Error - getDatosAvisoRGI ", ex);
            camposDAN = null;
        }
        return camposDAN;
    }

    /**
     *
     * @param codOrg
     * @param numExp
     * @param esActivo
     * @param datosAviso
     * @param con
     * @return
     * @throws Exception
     */
    public boolean comprobarDatosAvisoNotificacionVsREA(int codOrg, String numExp, boolean esActivo, Lan6DatosAvisos datosAviso, Connection con) throws Exception {
        log.info("comprobarDatosAvisoNotificacionVsREA");
        RGI_JobDAO rgiJobDAO = RGI_JobDAO.getInstance();
        boolean respuesta = false;
        try {
            DatosAvisoCSRegexlan camposDAN = getDatosAvisoRGI(numExp, esActivo, con);
            String valorCampoAvisoEMAIL_ACTUAL = camposDAN != null && camposDAN.getAvisoEmailRepresentante() != null ? camposDAN.getAvisoEmailRepresentante() : "";
            String valorCampoAvisoSMS_ACTUAL = camposDAN != null && camposDAN.getAvisoSmsRepresentante() != null ? camposDAN.getAvisoSmsRepresentante() : "";
            String valorCampoAvisoIDCTAINTE_ACTUAL = camposDAN != null && camposDAN.getAvisoIdCuentaInteresadoRepresentante() != null ? camposDAN.getAvisoIdCuentaInteresadoRepresentante() : "";
            String valorCampoAvisoEMAIL_ACTUAL_TITULAR = camposDAN != null && camposDAN.getAvisoEmailTitular() != null ? camposDAN.getAvisoEmailTitular() : "";
            String valorCampoAvisoSMS_ACTUAL_TITULAR = camposDAN != null && camposDAN.getAvisoSmsTitular() != null ? camposDAN.getAvisoSmsTitular() : "";
            String valorCampoAvisoIDCTAINTE_ACTUAL_TITULAR = camposDAN != null && camposDAN.getAvisoIdCuentaInteresadoTitular() != null ? camposDAN.getAvisoIdCuentaInteresadoTitular() : "";
            String valorCampoAvisoEMAIL_REA = "";
            String valorCampoAvisoSMS_REA = "";
            String valorCampoAvisoEMAIL_REA_TITULAR = "";
            String valorCampoAvisoSMS_REA_TITULAR = "";
            String valorCampoAvisoIDCTAINTE_REA_TITULAR = "";
            String valorCampoAvisoIDCTAINTE_REA = "";
            if (datosAviso != null) {
                valorCampoAvisoEMAIL_REA = datosAviso.getMailRepreVigente();
                valorCampoAvisoSMS_REA = datosAviso.getSmsRepreVigente();
                valorCampoAvisoEMAIL_REA_TITULAR = datosAviso.getMailTitular();
                valorCampoAvisoSMS_REA_TITULAR = datosAviso.getSmsTitular();
                valorCampoAvisoIDCTAINTE_REA_TITULAR = datosAviso.getIdCuentaTitular() != null ? String.valueOf(datosAviso.getIdCuentaTitular()) : "";
                valorCampoAvisoIDCTAINTE_REA = datosAviso.getIdCuentaRepreVigente();
            }

            // Comprobacion campo EMAIL REPRESENTANTE
            if (valorCampoAvisoEMAIL_ACTUAL != null && !valorCampoAvisoEMAIL_ACTUAL.isEmpty()) {
                if (valorCampoAvisoEMAIL_REA != null && !valorCampoAvisoEMAIL_REA.isEmpty()) {
                    log.debug("Comparamos los datos EMAIL Representante ... Regexlan / REA : ");
                    if (!valorCampoAvisoEMAIL_ACTUAL.trim().equalsIgnoreCase(valorCampoAvisoEMAIL_REA.trim())) {
                        // Actualizamos Regexlan con los datos de REA
                        log.debug("NO Son Iguales los datos -- Actualizado Representante el valor EMAIL : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAIL, valorCampoAvisoEMAIL_REA.trim(), con));
                        respuesta = true;
                    }
                }
            } else {
                // En Regexlan no hay datos EMAIL comprobamos si hay en REA
//                log.debug("No datos de aviso Representante Email en Regexlan - comprobamos los de REA");
                if (valorCampoAvisoEMAIL_REA != null && !valorCampoAvisoEMAIL_REA.isEmpty()) {
                    log.debug("NO Son Iguales los datos -- Actualizando Representante el valor EMAIL : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAIL, valorCampoAvisoEMAIL_REA.trim(), con));
                    respuesta = true;
                }
            }

            // Comprobacion campo EMAIL TITULAR
            if (valorCampoAvisoEMAIL_ACTUAL_TITULAR != null && !valorCampoAvisoEMAIL_ACTUAL_TITULAR.isEmpty()) {
                if (valorCampoAvisoEMAIL_REA_TITULAR != null && !valorCampoAvisoEMAIL_REA_TITULAR.isEmpty()) {
//                    log.debug("Comparamos los datos EMAIL Titular ... Regexlan / REA : ");
                    if (!valorCampoAvisoEMAIL_ACTUAL_TITULAR.trim().equalsIgnoreCase(valorCampoAvisoEMAIL_REA_TITULAR.trim())) {
                        // Actualizamos Regexlan con los datos de REA
                        log.debug("NO Son Iguales los datos -- Actualizando Interesado el valor EMAIL : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAILTIT, valorCampoAvisoEMAIL_REA_TITULAR.trim(), con));
                        respuesta = true;
                    }
                }
            } else {
                // En Regexlan no hay datos EMAIL comprobamos si hay en REA
//                log.debug("No datos de aviso Titular Email en Regexlan - comprobamos los de REA");
                if (valorCampoAvisoEMAIL_REA_TITULAR != null && !valorCampoAvisoEMAIL_REA_TITULAR.isEmpty()) {
                    log.debug("-- Actualizado Interesado el valor EMAIL : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAILTIT, valorCampoAvisoEMAIL_REA_TITULAR.trim(), con));
                    respuesta = true;
                }
            }

            // Comprobacion campo SMS Representante
            if (valorCampoAvisoSMS_ACTUAL != null && !valorCampoAvisoSMS_ACTUAL.isEmpty()) {
                if (valorCampoAvisoSMS_REA != null && !valorCampoAvisoSMS_REA.isEmpty()) {
//                    log.debug("Comparamos los datos SMS Representante ... Regexlan / REA : ");
                    if (!valorCampoAvisoSMS_ACTUAL.trim().equalsIgnoreCase(valorCampoAvisoSMS_REA.trim())) {
                        // Actualizamos Regexlan con los datos de REA
                        log.debug("NO Son Iguales los datos -- Actualizado Representante el valor SMS : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMS, valorCampoAvisoSMS_REA.trim(), con));
                        respuesta = true;
                    }
                }
            } else {
                // En Regexlan no hay datos SMS comprobamos si hay en REA
//                log.debug("No datos de aviso SMS en Regexlan - comprobamos los de le REA");
                if (valorCampoAvisoSMS_REA != null && !valorCampoAvisoSMS_REA.isEmpty()) {
                    log.debug("NO Son Iguales los datos -- Actualizado Representante el valor SMS : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMS, valorCampoAvisoSMS_REA.trim(), con));
                    respuesta = true;
                }
            }

            // Comprobacion campo SMS Titular
            if (valorCampoAvisoSMS_ACTUAL_TITULAR != null && !valorCampoAvisoSMS_ACTUAL_TITULAR.isEmpty()) {
                if (valorCampoAvisoSMS_REA_TITULAR != null && !valorCampoAvisoSMS_REA_TITULAR.isEmpty()) {
//                    log.debug("Comparamos los datos SMS Titular ... Regexlan / REA : ");
                    if (!valorCampoAvisoSMS_ACTUAL_TITULAR.trim().equalsIgnoreCase(valorCampoAvisoSMS_REA_TITULAR.trim())) {
                        // Actualizamos Regexlan con los datos de REA
                        log.debug("NO Son Iguales los datos -- Actualizado Titular el valor SMS : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMSTIT, valorCampoAvisoSMS_REA_TITULAR.trim(), con));
                        respuesta = true;
                    }
                }
            } else {
                // En Regexlan no hay datos SMS comprobamos si hay en REA
//                log.debug("No datos de aviso SMS en Regexlan - comprobamos los de le REA");
                if (valorCampoAvisoSMS_REA_TITULAR != null && !valorCampoAvisoSMS_REA_TITULAR.isEmpty()) {
                    log.debug("NO Son Iguales los datos -- Actualizando Titular el valor SMS : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMSTIT, valorCampoAvisoSMS_REA_TITULAR.trim(), con));
                    respuesta = true;
                }
            }

            // Comprobacion campo ID Cuenta Interesado Titular
            if (valorCampoAvisoIDCTAINTE_ACTUAL_TITULAR != null && !valorCampoAvisoIDCTAINTE_ACTUAL_TITULAR.isEmpty()) {
                if (valorCampoAvisoIDCTAINTE_REA_TITULAR != null && !valorCampoAvisoIDCTAINTE_REA_TITULAR.isEmpty()) {
//                    log.debug("Comparamos los datos ID CUENTA INTERESADO Titular ... Regexlan / REA : ");
                    if (!valorCampoAvisoIDCTAINTE_ACTUAL_TITULAR.trim().equalsIgnoreCase(valorCampoAvisoIDCTAINTE_REA_TITULAR.trim())) {
                        // Actualizamos Regexlan con los datos de REA
                        log.info("NO Son Iguales los datos -- Actualizando Titular el valor ID CUENTA INTERESADO : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTETIT, valorCampoAvisoIDCTAINTE_REA_TITULAR.trim(), con));
                        respuesta = true;
                    }
                } else {
                    log.debug("Hay datos en Regexlan Titular ID CUENTA INTERESADO pero no en REA. - No ejecutamos Ninguna accion. ");
                    respuesta = true;
                }
            } else {
                // En Regexlan no hay datos ID CUENTA INTERESADO comprobamos si hay en REA
//                log.debug("No datos de aviso ID CUENTA INTERESADO en Regexlan - comprobamos los de le REA");
                if (valorCampoAvisoIDCTAINTE_REA_TITULAR != null && !valorCampoAvisoIDCTAINTE_REA_TITULAR.isEmpty()) {
                    log.info("-- Actualizando Titular el valor ID CUENTA INTERESADO : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTETIT, valorCampoAvisoIDCTAINTE_REA_TITULAR.trim(), con));
                    respuesta = true;
                }
            }

            // Comprobacion campo ID Cuenta Interesado Representante
            if (valorCampoAvisoIDCTAINTE_ACTUAL != null && !valorCampoAvisoIDCTAINTE_ACTUAL.isEmpty()) {
                if (valorCampoAvisoIDCTAINTE_REA != null && !valorCampoAvisoIDCTAINTE_REA.isEmpty()) {
//                    log.debug("Comparamos los datos ID CUENTA INTERESADO Representante ... Regexlan / REA : ");
                    if (!valorCampoAvisoIDCTAINTE_ACTUAL.trim().equalsIgnoreCase(valorCampoAvisoIDCTAINTE_REA.trim())) {
                        // Actualizamos Regexlan con los datos de REA
                        log.info("NO Son Iguales los datos -- Actualizando Representante el valor ID CUENTA INTERESADO : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTE, valorCampoAvisoIDCTAINTE_REA.trim(), con));
                        respuesta = true;
                    }
                } else {
                    log.debug("Hay datos en Regexlan Representante ID CUENTA INTERESADO pero no en REA. Borramos datos porque se ha quitado la cuenta de consentimieto. ");
                    log.debug("Borramos datos ID Cuenta Interesado - Representante : " + rgiJobDAO.borrarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTE, con));
                    log.debug("Borramos datos EMAIL Interesado - Representante : " + rgiJobDAO.borrarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAIL, con));
                    log.debug("Borramos datos SMS Interesado - Representante : " + rgiJobDAO.borrarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMS, con));
                    respuesta = true;
                }
            } else {
                // En Regexlan no hay datos ID CUENTA INTERESADO comprobamos si hay en REA
//                log.debug("No datos de aviso ID CUENTA INTERESADO en Regexlan - comprobamos los de le REA");
                if (valorCampoAvisoIDCTAINTE_REA != null && !valorCampoAvisoIDCTAINTE_REA.isEmpty()) {
                    log.info("-- Actualizando Representante el valor ID CUENTA INTERESADO : " + rgiJobDAO.actualizarDANxCodCampo(codOrg, numExp, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTE, valorCampoAvisoIDCTAINTE_REA.trim(), con));
                    respuesta = true;
                }
            }
        } catch (Exception e) {
            log.error(" Error al comprobar y actualizar los datos de avisos entre Regexlan/ReA", e);
            throw e;
        }
        return respuesta;
    }

    /**
     *
     * @param interesados
     * @return
     */
    public boolean hayRepresentanteLegalEnListaInteresado(ArrayList<Lan6Interesado> interesados) {
        if (interesados != null && !interesados.isEmpty()) {
            for (Lan6Interesado interesado : interesados) {
                if (Lan6Constantes.TIPO_INTERESADO_REPRESENTANTE.equalsIgnoreCase(interesado.getTipo())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Convierte el objeto a un string para ser guardado en la bbdd
     *
     * @param lan6TramiteNotificacion
     * @return
     */
    public String objetoLan6TramiteNotificacionTexto(Lan6TramiteNotificacion lan6TramiteNotificacion) {
        try {
            if (lan6TramiteNotificacion.getNombreDestinatario().contains("'")) {
                lan6TramiteNotificacion.setNombreDestinatario(lan6TramiteNotificacion.getNombreDestinatario().replace("'", "´"));
            }
            String x = "numExpediente: " + lan6TramiteNotificacion.getNumExpediente()
                    + "| ejercicioExpediente: " + lan6TramiteNotificacion.getEjercicioExpediente()
                    + "| idActoNotificado: " + lan6TramiteNotificacion.getIdActoNotificado()
                    + "| descActoNotificado: " + lan6TramiteNotificacion.getDescActoNotificado()
                    + "| descActoNotificado_eu: " + lan6TramiteNotificacion.getDescActoNotificadoEu()
                    + "| descTramiteNotificacion: " + lan6TramiteNotificacion.getDescTramiteNotificacion();
            log.debug("Destinatario PRINCIPAL: " + lan6TramiteNotificacion.getNombreDestinatario());

            x = x + "| Destinatarios: ["
                    + "| idDestinatario: " + lan6TramiteNotificacion.getIdDestinatario()
                    + "| nombreDestinatario: " + lan6TramiteNotificacion.getNombreDestinatario();
            for (Lan6Interesado destinatario : lan6TramiteNotificacion.getAutorizados()) {
                log.debug("OTRO Destinatario: " + destinatario.toString());
                if (destinatario.getNombre().contains("'")) {
                    destinatario.setNombre(destinatario.getNombre().replace("'", "´"));
                }
                if (destinatario.getApellido1() != null) {
                    if (destinatario.getApellido1().contains("'")) {
                        destinatario.setApellido1(destinatario.getApellido1().replace("'", "´"));
                    }
                }
                if (destinatario.getApellido2() != null) {
                    if (destinatario.getApellido2().contains("'")) {
                        destinatario.setApellido2(destinatario.getApellido2().replace("'", "´"));
                    }
                }
                String destinatarioString = "("
                        + "| idDestinatario: " + destinatario.getNumIdentificacion()
                        + "| nombreDestinatario: " + destinatario.getNombre() + (destinatario.getApellido1() != null ? (" " + destinatario.getApellido1()) : "") + (destinatario.getApellido2() != null ? (" " + destinatario.getApellido2()) : "")
                        + ")";
                x = x + destinatarioString;
            }
            x = x + "]| idiomaNotificacion: " + lan6TramiteNotificacion.getIdiomaNotificacion()
                    + "| textoNotificacion: " + lan6TramiteNotificacion.getTextoNotificacion()
                    + "| idEmisor: " + lan6TramiteNotificacion.getIdEmisor()
                    + "| nombreEmisor: " + lan6TramiteNotificacion.getNombreEmisor()
                    + "| mailsAvisos: " + lan6TramiteNotificacion.getMailsAvisos()
                    + "| tfnosAvisos: " + lan6TramiteNotificacion.getTfnosAvisos();

            x = x + "| documentos: [";
            try {
                for (Lan6Documento documento : lan6TramiteNotificacion.getDocumentos()) {
                    String documentoString = "("
                            + "| naturaleza: " + documento.getNaturaleza()
                            + "| idDocumento: " + documento.getIdDocumento()
                            + "| format: " + documento.getFormat()
                            + "| nombre: " + documento.getNombre()
                            + "| titulo: " + documento.getTitulo()
                            + "| serieDocumental: " + documento.getSerieDocumental()
                            + "| origen: " + documento.getOrigen()
                            + "| tipoDocumental: " + documento.getTipoDocumental()
                            + "| asuntoDocumental: " + documento.getAsuntoDocumental()
                            + "| version: " + documento.getVersion()
                            + "| convertirAPdf: " + documento.isConvertirAPdf()
                            + "| conLocalizador: " + documento.isConLocalizador()
                            + "| sincrono: " + documento.isSincrono()
                            + "| PDFA: " + documento.isPDFA()
                            + "| reducirQR: " + documento.isReducirQR()
                            + "| idProcArchivoDigital: " + documento.getIdProcArchivoDigital()
                            + "| numTitular: " + documento.getNumTitular()
                            + "| tipoFirma: " + documento.getTipoFirma()
                            + "| fechaCreacion: " + documento.getFechaCreacion()
                            + "| idExpediente: " + documento.getIdExpediente()
                            + "| idRutaPif: " + documento.getIdRutaPif()
                            + ")";
                    x = x + documentoString;
                }
            } catch (Exception e) {
                x = x + "Error al intentar parsear los documentos:" + e.getLocalizedMessage();
            }
            x = x + "]";
            return x;
        } catch (Exception e) {
            //Mostramos error por si el parseo ha ido mal
            String y = "Error al intentar parsear el objeto Lan6TramiteNotificacion para guardar en bbdd: " + e.getMessage();
            log.error(y);
            return y;
        }
    }

    /**
     *
     * @param notificacionVO
     * @param rol
     * @return
     */
    public String formatearTlfnosAvisoNotificacionDsdNotificacionVO(NotificacionBVO notificacionVO, int rol) {
        String respuesta = "";
        if (notificacionVO != null) {
            if (rol == 2) {
                if (notificacionVO.getSms() != null && !notificacionVO.getSms().isEmpty()) {
                    respuesta += (respuesta.isEmpty() ? notificacionVO.getSms() : ";" + notificacionVO.getSms());
                }
            } else {
                if (notificacionVO.getSmsTitular() != null && !notificacionVO.getSmsTitular().isEmpty()) {
                    respuesta += (respuesta.isEmpty() ? notificacionVO.getSmsTitular() : ";" + notificacionVO.getSmsTitular());
                }
            }
        }
        return respuesta;
    }

    /**
     * Devuelve la extension de fichero correspondiente al tipo MIME pasado.
     * Utilizamos la clase de flexia para no reescribir funciones
     */
    public String getMimeTypeFromExtension(String extension) {
        return MimeTypes.guessMimeTypeFromExtension(extension);
    }

    /**
     *
     * @param notificacionVO
     * @param rol
     * @return
     */
    public String formatearEmailsAvisoNotificacionDsdNotificacionVO(NotificacionBVO notificacionVO, int rol) {
        String respuesta = "";
        if (notificacionVO != null) {
            if (rol == 2) {
                if (notificacionVO.getEmails() != null && !notificacionVO.getEmails().isEmpty()) {
                    respuesta += (respuesta.isEmpty() ? notificacionVO.getEmails() : ";" + notificacionVO.getEmails());
                }
            } else {
                if (notificacionVO.getEmailsTitular() != null && !notificacionVO.getEmailsTitular().isEmpty()) {
                    respuesta += (respuesta.isEmpty() ? notificacionVO.getEmailsTitular() : ";" + notificacionVO.getEmailsTitular());
                }
            }
        }
        return respuesta;
    }

}
