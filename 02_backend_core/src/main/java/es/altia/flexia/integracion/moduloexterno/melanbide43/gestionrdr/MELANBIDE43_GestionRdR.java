/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr;

import com.ejie.rdr.xml.ApoderadoType;
import com.ejie.rdr.xml.IsValidRepresentationDocument;
import com.ejie.rdr.xml.PoderdanteInfoDocument;
import com.ejie.rdr.xml.PoderdanteInfoType;
import com.ejie.rdr.xml.ValidRepresentationType;
import com.ejie.x43A.rri.ws.xml.TContacto;
import com.ejie.x43A.rri.ws.xml.TCuenta;
import com.ejie.x43A.rri.ws.xml.TInteresado;
import es.altia.agora.business.sge.persistence.FichaExpedienteManager;
import es.altia.agora.business.sge.persistence.OperacionesExpedienteManager;
import es.altia.agora.business.terceros.DomicilioSimpleValueObject;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.mantenimiento.persistence.manual.DomiciliosDAO;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.business.terceros.persistence.manual.TercerosDAO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.exception.MELANBIDE43_GestionErroresRdR;
import es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.util.MELANBIDE43_GestionRdR_Constantes;
import es.altia.flexia.integracion.moduloexterno.melanbide43.gestionrdr.util.MELANBIDE43_GestionRdR_Util;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.DatosAvisoCSRegexlan;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.notificacion.persistence.AutorizadoNotificacionManager;
import es.altia.flexia.notificacion.vo.AutorizadoNotificacionVO;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.rdr.beans.Lan6Apoderado;
import es.lanbide.lan6.adaptadoresPlatea.rdr.beans.Lan6DatosAvisos;
import es.lanbide.lan6.adaptadoresPlatea.rdr.beans.Lan6DatosInteresados;
import es.lanbide.lan6.adaptadoresPlatea.rdr.beans.Lan6ResultGrantorRepresentations;
import es.lanbide.lan6.adaptadoresPlatea.rdr.servicios.Lan6RdRServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



/**
 *
 * @author INGDGC
 */
public class MELANBIDE43_GestionRdR {
    
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE43_GestionRdR.class);
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();
    //Instancia
    private static MELANBIDE43_GestionRdR instance = null;
    public static MELANBIDE43_GestionRdR getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MELANBIDE43_GestionRdR.class){
                if(instance == null){
                    instance = new MELANBIDE43_GestionRdR();
                }//if(instance == null)
            }//synchronized(MELANBIDE43_GestionRdR.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    public MELANBIDE43_GestionRdR() {
    }
    
    
    /***
     * Metodo que permite validar si un representate legal de regexlan esta correctamente dado de alta en Registro de Representantes (RdR). 
     * Ademas sincroniza datos de Aviso de el titular y de representante en regexlan - Mi carpeta.
     * Da de alta o elimina un representante en Regexlan si este no existe en regexlan o no es valido en RdR. Las estas operacion se reflejan en Mi carpeta 
     * @param codOrganizacion
     * @param numExpediente
     * @param idioma
     * @param codIdNotificacion  0 Es null en plugin notificaicion y -1 viene invocado desde la ficha expediente
     * @return repreRespuestaValidacion  Mapa de codigo de respuesta y descripcion para  poder mostrar al usuario.
     */
    public Map<String,String> validarExistActualRolRepresContraRdR(int codOrganizacion,String numExpediente, int idioma, int codIdNotificacion){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        log.info("validarExistActualRolRepresContraRdR - Begin () " + sdf.format(new Date()));
        Map<String,String> repreRespuestaValidacion  =  new HashMap<String, String>();
        String arregloParamMensaRespuesta[] = null;
        AdaptadorSQLBD adaptador = null;
        String paramsAdaptador[] = null;
        Connection con = null;
        MELANBIDE43_GestionRdR_Util MELANBIDE43_GestionRdR_Util_Instace = MELANBIDE43_GestionRdR_Util.getInstance();
        String InteresadoSolicitanteIdPoderdante="";
        ArrayList<Lan6Apoderado> listaRespuestaRepresentante = new ArrayList<Lan6Apoderado>();
        String idProcedimientoEnPlatea = "";
        Lan6DatosInteresados lan6DatosInteresados = new Lan6DatosInteresados();
        TInteresado lan6DatosCuentaInteresado = null;
        // Validamos si esta o no activo la llamada a RdR desde el properties
        Boolean servicioActivo = false;
        try {
            String servicioActivoStr = ConfigurationParameter.getParameter(MELANBIDE43_GestionRdR_Constantes.VALIDACION_RDR_ACTIVA, MELANBIDE43_GestionRdR_Constantes.FICHERO_PROPIEDADES_RdR);
            servicioActivo=servicioActivoStr!=null && !servicioActivoStr.isEmpty() ? servicioActivoStr.equalsIgnoreCase("1") : false;
        } catch (Exception e) {
            log.error("Error leer la propiedad VALIDACION_RDR_ACTIVA, no invocamos servicios de validacion RdR ",e);
        }
        if(servicioActivo){
            try {
                adaptador = MELANBIDE43_GestionRdR_Util_Instace.getAdaptSQLBD(String.valueOf(codOrganizacion));
                paramsAdaptador = MELANBIDE43_GestionRdR_Util_Instace.getParamsAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                log.error("Error al preparar el adaptador para conexion a BD" + ex.getMessage(), ex);
                arregloParamMensaRespuesta = null;
                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("101", idioma, arregloParamMensaRespuesta);
                return repreRespuestaValidacion;
            }
            try {
                con = adaptador.getConnection();
                // Si esta en el historico no debe validar RdA
                // 0 --> Expediente activo, 1 --> Expediente historico, 2 --> Expediente no existe
                int ejercicio = numExpediente != null && !numExpediente.isEmpty() && numExpediente.split("/").length >0 ? Integer.parseInt(numExpediente.split("/")[0]):0;
                if (FichaExpedienteManager.getInstance().estaExpedienteHistorico(ejercicio,numExpediente, paramsAdaptador) == 1) {
                    log.info("Expediente en Historico : " + numExpediente + " No se valida la representacion legal.");
                    arregloParamMensaRespuesta =null;
                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("1", idioma, arregloParamMensaRespuesta);
                    return repreRespuestaValidacion;
                }
                if (MeLanbide43Manager.getInstance().comprobarExpGenerado(codOrganizacion, 0, 0, numExpediente, con)) {

                    String codProcedimiento = MELANBIDE43_GestionRdR_Util_Instace.getCodigoProcedimientoDsdNumExpediente(numExpediente);

                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(numExpediente, con);
                    if (esDesarrolloCero > 0) {
                        idProcedimientoEnPlatea = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    } else {
//                        idProcedimientoEnPlatea = MeLanbide43Manager.getInstance().convierteProcedimiento(codProcedimiento);
                        idProcedimientoEnPlatea = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(codProcedimiento); //convierteProcedimiento(codProcedimiento);
                    }
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0

                    if (codProcedimiento != null && !codProcedimiento.isEmpty() && idProcedimientoEnPlatea != null && !idProcedimientoEnPlatea.isEmpty()) {
                        String codRolRepreProcedimiento = ConfigurationParameter.getParameter(MELANBIDE43_GestionRdR_Constantes.CODIGO_ROL_REPRE_EXPTS_TELETRAMITACION, MELANBIDE43_GestionRdR_Constantes.FICHERO_PROPIEDADES_RdR);
                        String codRolInteresadoProcedimiento = ConfigurationParameter.getParameter(MELANBIDE43_GestionRdR_Constantes.CODIGO_ROL_INTERESADO_EXPTS_TELETRAMITACION, MELANBIDE43_GestionRdR_Constantes.FICHERO_PROPIEDADES_RdR);
                        int codRolRepreProcedimientoInt = codRolRepreProcedimiento != null && !codRolRepreProcedimiento.isEmpty() ? Integer.parseInt(codRolRepreProcedimiento) : 0;
                        int codRolInteresadoProcedimientoInt = codRolInteresadoProcedimiento != null && !codRolInteresadoProcedimiento.isEmpty() ? Integer.parseInt(codRolInteresadoProcedimiento) : 0;

                        ArrayList<Participantes> participantesRepre = MeLanbide43Manager.getInstance().leerListaParticipantesRepresentantesRdRxExp(numExpediente, codRolRepreProcedimientoInt, con);
                        ArrayList<Participantes> participantesInteresados = MeLanbide43Manager.getInstance().leerListaParticipantesRepresentantesRdRxExp(numExpediente, codRolInteresadoProcedimientoInt, con);

                        // Solo deberia ser un interesado como solicitante sino hay o hay mas de 1 no se puede validar la representacion
                        if (participantesInteresados != null && participantesInteresados.size() > 0) {
                            if (participantesInteresados.size() == 1) {
                                InteresadoSolicitanteIdPoderdante = participantesInteresados.get(0).getNif();
                            } else {
                                log.info("Hay mas de 1 Interesados principal como solicitantes en el expediente : " + numExpediente + " No se puede validar la representacion legal.");
                                arregloParamMensaRespuesta = new String[]{numExpediente};
                                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("12", idioma, arregloParamMensaRespuesta);
                                return repreRespuestaValidacion;
                            }
                        } else {
                            log.info("No hay Interesados principales como solicitantes en el expediente : " + numExpediente + " No se puede validar la representacion legal.");
                            arregloParamMensaRespuesta = new String[]{numExpediente};
                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("11", idioma, arregloParamMensaRespuesta);
                            return repreRespuestaValidacion;
                        }

                        try {
                            lan6DatosInteresados = this.invocarRecuperarDatosInteresados(InteresadoSolicitanteIdPoderdante, idProcedimientoEnPlatea, numExpediente, codIdNotificacion, idioma);
                            if (lan6DatosInteresados == null) {
                                arregloParamMensaRespuesta = new String[]{InteresadoSolicitanteIdPoderdante, numExpediente, "Respuesta del servicios recibida a Null."};
                                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("107", idioma, arregloParamMensaRespuesta);
                                return repreRespuestaValidacion;
                            }
                        } catch (Exception e) {
                            log.error("Error al llamar a los Servicios de platea para recuperar los datos de titular y representacion para validar el  representante: " + e.getMessage(), e);
                            arregloParamMensaRespuesta = new String[]{InteresadoSolicitanteIdPoderdante, numExpediente, e.getMessage()};
                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("107", idioma, arregloParamMensaRespuesta);
                            return repreRespuestaValidacion;
                        }
                        Lan6DatosAvisos lan6DatosAvisos = lan6DatosInteresados.getDatosAvisos();

                        if (lan6DatosInteresados.getResultGrantorRepresentationsLan6() != null) {
                            listaRespuestaRepresentante = lan6DatosInteresados.getResultGrantorRepresentationsLan6().getListaRepresentantes();
                        } else {
                            log.info("Datos del titular recuperados. Pero no se recibe la lista de Representantes. getResultGrantorRepresentationsLan6");
                        }
                        if (participantesRepre != null && participantesRepre.size() > 0) {
                            // Hemos recogido los datos de los representantes del expediente. Iniciamos comprobacion contra platea
                            // Caso en el que existen en regexlan  
                            if (listaRespuestaRepresentante != null && listaRespuestaRepresentante.size() > 0) {
                                for (Participantes participante : participantesRepre) {
                                    ApoderadoType repre = getLan6ApoderadoFromListaRespuestaxNIF(participante.getNif(), listaRespuestaRepresentante);
                                    boolean isValidRepresentation = (repre != null && repre.getIdApoderado() != null && repre.getIdApoderado().equalsIgnoreCase(participante.getNif()));
                                    if (!isValidRepresentation) {
                                        log.info("El representante registrado en Rgexlan no es valido o no coincide con el que esta en RdR - Procedemos a borrar de Regexlan el Representante y marcar la actualizacion.");
                                        // En caso de que exista mas de un repre(no deberia pasar pero tecnicamente se puede), una vez borrado el no valido. leer todo los correctos de RdR y ponerlos en regexlan
                                        //comprobarYactualizarRegexlanRepreFromRdR=true;
                                        // No es valido el representante que tenemos en regexlan, hay que recuperar los de RdR
                                        // Actualizar el bueno y borrar el que no es valido
                                        // ### Paso 1 Leer Y dar de alta todos los correctos que nos devuelvvan el correcto.
                                        // Suponiendo que nos devuelvan una lista
                                        log.info(" Paso 1. - Damos de alta los Representantes buenos recibidos desde RdR - Cuando no se ha validado uno existente en Regexlan. ");
                                        ApoderadoType representanteDadoAlta = null;
                                        for (Lan6Apoderado lan6Apoderado : listaRespuestaRepresentante) {
                                            ApoderadoType apoderadoType = lan6Apoderado.getApoderadoType();
                                            if (apoderadoType != null) {
                                                repreRespuestaValidacion = this.comprobarActualizarRepreFromRdRToRegexlan(apoderadoType, codOrganizacion, numExpediente, codProcedimiento, codRolRepreProcedimientoInt, "1", idioma, codIdNotificacion, con);
                                                representanteDadoAlta=apoderadoType;
                                                log.info("Respuesta Invocacion Caso con Repre En Regexlan : " + apoderadoType.getIdApoderado() + " --> " + repreRespuestaValidacion.get("codigo") + "-" + repreRespuestaValidacion.get("descripcion"));
                                                //Actualizamos los datos de notificacion 
                                                //if(codIdNotificacion>=0){
                                                try {
                                                    this.comprobarDatosAVISORegexlanVsRdr(codOrganizacion, numExpediente, lan6DatosAvisos, con);
                                                } catch (Exception e) {
                                                    log.error("Error al cmoprobar los datos de notificacion despues de validado una representacion, invocado desde la plugin de notificaciones. "
                                                            + " continua la operacion de validacion representante .. " + numExpediente, e);
                                                    //arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente, e.getMessage()};
                                                    //repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("106", idioma, arregloParamMensaRespuesta);
                                                    //return repreRespuestaValidacion;
                                                }
                                                //}
                                            }
                                        }
                                        // Paso 2 Quitar de Regexlan el que no esta validado
                                        // Se eliminara solo si no hay representantes vigentes en RdR 
                                        log.info("Paso 2. - Procedemos a dar de baja en Regexlan el representante en el Expediente - Cuando no se ha validado uno existente en Regexlan. y existe uno valido ");
                                        // Al eliminar necesitamos el id Especifico del Tercero, con su codigo y numero de version.
                                        TercerosValueObject tercero = MeLanbide43Manager.getInstance().getDatosTerceroRdRxNifExpteRol(numExpediente, participante.getNif(), codRolRepreProcedimientoInt, con);
                                        if (MeLanbide43Manager.getInstance().eliminarRepresentanteLegalExpedienteRdR(tercero, codOrganizacion, numExpediente, codProcedimiento, codRolRepreProcedimientoInt, con)) {

                                            // Actualizamos la tabla  AUTORIZADO_NOTIFICACION con los datos del nuevo representante, numero de expediente e id notificacion asociada
                                            // La actualizamos solo en caso de que vengamos desde el Batch de notificaciones, donde existe la notificacion y ademas la conocemos al irla tratando
                                            if (codIdNotificacion >= 0) {
                                                try {
                                                    AutorizadoNotificacionVO autorizadoEnvioNOtificacion = new AutorizadoNotificacionVO();
                                                    autorizadoEnvioNOtificacion.setNumeroExpediente(numExpediente);
                                                    autorizadoEnvioNOtificacion.setEjercicio(MELANBIDE43_GestionRdR_Util_Instace.getEjercicioDsdNumExpediente(numExpediente));
                                                    autorizadoEnvioNOtificacion.setCodMunicipio(String.valueOf(codOrganizacion));
                                                    autorizadoEnvioNOtificacion.setCodigoTercero(Integer.valueOf(tercero.getIdentificador()));
                                                    autorizadoEnvioNOtificacion.setNumeroVersionTercero(Integer.valueOf(tercero.getVersion()));
                                                    autorizadoEnvioNOtificacion.setCodigoNotificacion(codIdNotificacion);
                                                    AutorizadoNotificacionManager autorizadoNotificaiconManager = AutorizadoNotificacionManager.getInstance();
                                                    if (autorizadoNotificaiconManager.eliminarAutorizado(autorizadoEnvioNOtificacion, paramsAdaptador)) {
                                                        log.info("Dato del nuevo representante guardado correctamente en la tabla de autorizados para el envio de la notificaicon." + tercero.getDocumento() + "/" + tercero.getIdentificador() + "/" + tercero.getVersion());
                                                    } else {
                                                        log.error("NO se ha podido insertar el nuevo representante en la tabla de autorizados para el envio de la notificacion");
                                                    }
                                                } catch (Exception e) {
                                                    log.error("Error al actualizar la tabla AUTORIZADO_NOTIFICACION con un nuevo representante dado de alta en el expediente " + numExpediente + " - " + tercero.getDocumento(), e);
                                                }
                                            } else {
                                                log.info("NO actualizamos la tabla autorizados notificacion, porque Cod NOtificaiocn es " + codIdNotificacion);
                                            }
                                            // Procedemos a actualizar Mi Carpeta y Registrar la operacion en el Historico de el Expediente
                                            String respuestaActualizaInteresadosCod = "";
                                            String MensajeErrorActInteresado = "";
                                            String[] respuestaActualizaInteresados = this.actualizarInteresadosMiCarpetaGestionRdR(codOrganizacion, numExpediente);
                                            respuestaActualizaInteresadosCod = respuestaActualizaInteresados[0];
                                            MensajeErrorActInteresado = respuestaActualizaInteresados[1];
                                            if ("0".equalsIgnoreCase(respuestaActualizaInteresadosCod)) {
                                                if (!registrarOperacionHistoricoExpedienteGestionRdR(codOrganizacion, numExpediente, tercero, 2, con)) {
                                                    log.error("-- Error al regitrar la operacion en historico del expediente, continuamos con el proceSo. Solo registramos en el Log");
                                                }
                                                log.info("Eliminado el ROL de REPRESENTANTE LEGAL del expediente : " + numExpediente + " NIF : " + participante.getNif());
                                                arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente};
                                                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("8", idioma, arregloParamMensaRespuesta);
                                                if(representanteDadoAlta!=null){
                                                    arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente,representanteDadoAlta.getIdApoderado()};
                                                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("15", idioma, arregloParamMensaRespuesta);
                                                }
                                            } else {
                                                log.info(" Representante no existe en RdR, eliminado en Regexlan Correctamente. Pero no actualizado en Mi carpeta." + numExpediente + "/" + participante.getNif() + "/ CodTercero: " + tercero.getCodTerceroOrigen() + " - " + MensajeErrorActInteresado);
                                                arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente, MensajeErrorActInteresado};
                                                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("9", idioma, arregloParamMensaRespuesta);
                                            }
                                        } else {
                                            log.info("No se ha podido eliminar el ROL de REPRESENTANTE LEGAL del expediente : " + numExpediente + " NIF : " + participante.getNif());
                                            arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente};
                                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("7", idioma, arregloParamMensaRespuesta);
                                        }
                                    } else {
                                        // Si venimos de la ficha (IdNotificaion=-1) No ejecutamos ninguna accion
                                        // En otro caso comprobamos datos de notificaciones del representante
                                        // Version 2.1.6 Adaptadores hay que actualizar datos de aviso cada vez que se entra al expediente
                                        //if (codIdNotificacion >= 0) {
                                        try {
                                            //ApoderadoType aT = this.getLan6ApoderadoFromListaRespuestaxNIF(participante.getNif(), listaRespuestaRepresentante);
                                            //if (aT != null) {
                                            this.comprobarDatosAVISORegexlanVsRdr(codOrganizacion, numExpediente, lan6DatosAvisos, con);
                                            //} else {
                                            //   log.info("-- No se ha recuperado el Representante de la lista respuesta, no actualizamos los datos. ");
                                            //}
                                            //arregloParamMensaRespuesta = null;
                                            //repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("0", idioma, arregloParamMensaRespuesta);
                                        } catch (Exception e) {
                                            log.error("Error al comprobar los datos de notificacion despues de validado un representacion. ", e);
                                            arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente, e.getMessage()};
                                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("106", idioma, arregloParamMensaRespuesta);
                                            return repreRespuestaValidacion;
                                        }
                                        //} else {
                                        log.info(" Existe Representante en el Expediente y es Valido en RdR - No ejecutamos nada mas.");
                                        arregloParamMensaRespuesta = null;
                                        repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("0", idioma, arregloParamMensaRespuesta);
                                        //}
                                    }
                                }
                            } else {
                                log.info("No Damos de baja, aunque no es valido el de Regexlan, no existe en RdR ninguno, por tanto se notificara a la empresa para que de de alta uno valido en RdR ");
                                // Sino hay en RDR invocamos recuperarCuentaInteresado - 
                                // Hay que pasar el Documento del Representante
                                for (Participantes participante : participantesRepre) {
                                    try {
                                        lan6DatosCuentaInteresado = this.invocarRecuperarCuentaInteresado(participante.getNif(), idProcedimientoEnPlatea, numExpediente, codIdNotificacion, idioma);
                                        /**
                                        * Un representante, tanto si es puntual del expediente o es vigente en el RdR, puede no
                                        * tener una cuenta con consentimiento. Se conservaran los datos si son de solicitud, es decir si no
                                        * hay datos en el campo AVISOIDCTAINTE se geestiona en el metodo de comprobarDatosAVISORegexlanVsRdR
                                        */
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
//                                                        arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente, "Respuesta del servicios recibida a Null."};
//                                                        repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("108", idioma, arregloParamMensaRespuesta);
//                                                        return repreRespuestaValidacion;
                                                    }
                                                }
                                            } else {
                                                log.info("Representante Sin cuenta de Interesado con consentimiento");
                                                //arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente, "Respuesta del servicios recibida a Null."};
                                                //repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("108", idioma, arregloParamMensaRespuesta);
                                                //return repreRespuestaValidacion;
                                            }
                                        } else {
                                            log.info("Sin Datos cuenta de Interesado con consentimiento");
//                                            arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente, "Respuesta del servicios recibida a Null."};
//                                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("108", idioma, arregloParamMensaRespuesta);
//                                            return repreRespuestaValidacion;
                                        }
                                    } catch (Exception e) {
                                        log.error("Error al llamar a los Servicios de platea para recuperar los datos de cuenta interesado en Regexlan para validar el  representante: " + e.getMessage(), e);
                                        arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente, e.getMessage()};
                                        repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("108", idioma, arregloParamMensaRespuesta);
                                        return repreRespuestaValidacion;
                                    }

                                    // Actualizamos Datos de Aviso
                                    try {
                                        this.comprobarDatosAVISORegexlanVsRdr(codOrganizacion, numExpediente, lan6DatosAvisos, con);
                                        // Procedemos a actualizar Mi Carpeta y Registrar la operacion en el Historico de el Expediente
                                        String respuestaActualizaInteresadosCod = "";
                                        String MensajeErrorActInteresado = "";
                                        String[] respuestaActualizaInteresados = this.actualizarInteresadosMiCarpetaGestionRdR(codOrganizacion, numExpediente);
                                        respuestaActualizaInteresadosCod = respuestaActualizaInteresados[0];
                                        MensajeErrorActInteresado = respuestaActualizaInteresados[1];
                                        if ("0".equalsIgnoreCase(respuestaActualizaInteresadosCod)) {
                                            log.info("Actualizado datos en Mi carpeta." + numExpediente + "/" + participante.getNif() + "/  " + MensajeErrorActInteresado);
                                            TercerosValueObject tercero = MeLanbide43Manager.getInstance().getDatosTerceroRdRxNifExpteRol(numExpediente, participante.getNif(), codRolRepreProcedimientoInt, con);
                                            //if (!registrarOperacionHistoricoExpedienteGestionRdR(codOrganizacion, numExpediente, tercero, 2, con)) {
                                            //log.error("-- Error al regitrar la operacion en historico del expediente, continuamos con el proceSo. Solo registramos en el Log");
                                            //}
                                            //log.info("Eliminado el ROL de REPRESENTANTE LEGAL del expediente : " + numExpediente + " NIF : " + participante.getNif());
                                            //arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente};
                                            //repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("8", idioma, arregloParamMensaRespuesta);
                                        } else {
                                            log.info("No actualizado datos en Mi carpeta." + numExpediente + "/" + participante.getNif() + "/  " + MensajeErrorActInteresado);
                                            //arregloParamMensaRespuesta = new String[]{participante.getNif(), numExpediente, MensajeErrorActInteresado};
                                            //repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("9", idioma, arregloParamMensaRespuesta);
                                        }
                                    } catch (Exception e) {
                                        log.error("Error al cmoprobar los datos de notificacion despues obtener no obtener representantes vigentes en RDR, pero si en Regexlan - Representacion Parcial. "
                                                + " continua la operacion de validacion representante .. " + numExpediente, e);
                                    }

                                }
                                String documento = (participantesRepre.size() == 1 ? participantesRepre.get(0).getNif() : "-");
                                log.info("participantesRepre.get(0).getNif() : " + participantesRepre.get(0).getNif());
                                // Verificamos si ya se ha validado la docu para no mostrar el mensaje
                                String codDOCACREDRDAVAL = ConfigurationParameter.getParameter(MELANBIDE43_GestionRdR_Constantes.CODIGO_CAMPOSUP_EXPTE_DOCACREDITATIVA_VALIDADA, MELANBIDE43_GestionRdR_Constantes.FICHERO_PROPIEDADES_RdR);
                                String valorCodDOCACREDRDAVAL = null;
                                if (codDOCACREDRDAVAL != null && !codDOCACREDRDAVAL.isEmpty()) {
                                    valorCodDOCACREDRDAVAL = MeLanbide43Manager.getInstance().getValorCampoSuplementarioExpedienteTDESP(numExpediente, codDOCACREDRDAVAL, con);
                                } else {
                                    log.info("NO se ha recuperado el codigo de campo suplementario Documentacion Acreditativa RdA ");
                                }
                                if(valorCodDOCACREDRDAVAL!=null && !valorCodDOCACREDRDAVAL.isEmpty()
                                        && "S".equalsIgnoreCase(valorCodDOCACREDRDAVAL)){
                                    arregloParamMensaRespuesta = new String[]{"Documento Acreditativo Adjuntado y validado en Expediente."};
                                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("0", idioma, arregloParamMensaRespuesta);
                                }else{
                                    arregloParamMensaRespuesta = new String[]{numExpediente, documento};
                                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("13", idioma, arregloParamMensaRespuesta);
                                }
                            }
                        } else {
                            // Sino hay representantes en el expediente tenemos que consultar si lo hay en el RdR para darlo de alta en Regexlan.
                            log.info(" No se han recuperado Interesados con el ROL REPRESENTANTE para el expediente en REGEXLAN. Comprobamos si lo hay en RdR." + numExpediente);
                            // Suponiendo que nos devuelvan una lista
                            log.info(" Paso 1. - Damos de alta los Representantes buenos recibidos desde RdR - Cuando no se ha validado uno existente en Regexlan. ");
                            if (listaRespuestaRepresentante != null && listaRespuestaRepresentante.size() > 0) {
                                for (Lan6Apoderado lan6Apoderado : listaRespuestaRepresentante) {
                                    ApoderadoType apoderadoType = lan6Apoderado.getApoderadoType();
                                    if (apoderadoType != null) {
                                        // Actualizamos datos de aviso, para que al invocar actualizacion Mi carpeta se pasen los actuales
                                        try {
                                            this.comprobarDatosAVISORegexlanVsRdr(codOrganizacion, numExpediente, lan6DatosAvisos, con);
                                        } catch (Exception e) {
                                            log.error("Error al cmoprobar los datos de notificacion despues de validado una representacion, invocado desde la plugin de notificaciones. "
                                                    + " continua la operacion de validacion representante .. " + numExpediente, e);
                                        }
                                        repreRespuestaValidacion = this.comprobarActualizarRepreFromRdRToRegexlan(apoderadoType, codOrganizacion, numExpediente, codProcedimiento, codRolRepreProcedimientoInt, "1", idioma, codIdNotificacion, con);
                                        log.info("Respuesta Invocacion Caso SIN Repre En Regexlan : " + apoderadoType.getIdApoderado() + " --> " + repreRespuestaValidacion.get("codigo") + "-" + repreRespuestaValidacion.get("descripcion"));
                                    }
                                }
                            } else {
                                log.info(" No se han recuperado Interesados con el ROL REPRESENTANTE para el expediente, ni en Regexlan ni en RdR. " + numExpediente);
                                // Actualizamos datos de Aviso del titular
                                try {
                                    // Procedemos a actualizar Mi Carpeta, si se han realizado cambios en regexlan;
                                    if(this.comprobarDatosAVISORegexlanVsRdr(codOrganizacion, numExpediente, lan6DatosAvisos, con)){
                                        String respuestaActualizaInteresadosCod = "";
                                        String MensajeErrorActInteresado = "";
                                        String[] respuestaActualizaInteresados = this.actualizarInteresadosMiCarpetaGestionRdR(codOrganizacion, numExpediente);
                                        respuestaActualizaInteresadosCod = respuestaActualizaInteresados[0];
                                        MensajeErrorActInteresado = respuestaActualizaInteresados[1];
                                        if ("0".equalsIgnoreCase(respuestaActualizaInteresadosCod)) {
                                            log.info("Actualizado datos en Mi carpeta." + numExpediente + " / " + lan6DatosAvisos.getMailTitular() + " / " + " / " + lan6DatosAvisos.getSmsTitular() + " / " + MensajeErrorActInteresado);
                                        } else {
                                            log.info("No se ha Actualizado datos en Mi carpeta." + numExpediente + " / " + lan6DatosAvisos.getMailTitular() + " / " + " / " + lan6DatosAvisos.getSmsTitular() + " / " + MensajeErrorActInteresado);
                                        }
                                    }else{
                                        log.info("No se han modificado los datos de aviso en regexlan, no se invoca a actualizarInteresados");
                                    }

                                } catch (Exception e) {
                                    log.error("Error al actualizar datos de aviso e interesados en mi carpeta despues de validar. No hay representante ni enregexlan ni en RDR."
                                            + " continua la carga del expediente     de validacion representante .. " + numExpediente, e);
                                }
                                arregloParamMensaRespuesta = null;
                                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("2", idioma, arregloParamMensaRespuesta);
                                return repreRespuestaValidacion;
                            }
                        }
                    } else {
                        log.error(" Error -  No se ha recuperado el codigo de procedimiento desde el Numero de expediente "
                                + " O el Identificador del procedimiento en Platea. NO se puede validar el representante. " + numExpediente + " - " + codProcedimiento);
                        if (codProcedimiento == null || codProcedimiento.isEmpty()) {
                            arregloParamMensaRespuesta = new String[]{numExpediente};
                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("102", idioma, arregloParamMensaRespuesta);
                        } else if (idProcedimientoEnPlatea == null || idProcedimientoEnPlatea.isEmpty()) {
                            arregloParamMensaRespuesta = new String[]{codProcedimiento, numExpediente};
                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("104", idioma, arregloParamMensaRespuesta);
                        }
                    }
                } else {
                    log.info("No esta creado el expediente en Mi Carpeta. NO se realizan operacion con el RdR. - Retornamos como validado el Representante. " + numExpediente);
                    arregloParamMensaRespuesta = null;
                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("1", idioma, arregloParamMensaRespuesta);
                }
            } catch (Exception e) {
                log.error("Error al validar el rol de representante del expediente " + numExpediente + " contra el Registro Electronico de Representante RdR : " + e.getMessage(), e);
                //repreRespuestaValidacion.put("codigo","100");
                //repreRespuestaValidacion.put("descripcion", String.format(MELANBIDE43_GestionRdR_Util_Instace.getDescRespuestaPorCodigoIdioma("100",idioma),e.getMessage()));
                arregloParamMensaRespuesta = new String[]{e.getMessage()};
                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("100", idioma, arregloParamMensaRespuesta);
                try {
                    Integer idError = 0;
                    String descripcionErrorDetalle = repreRespuestaValidacion.get("descripcion");
                    descripcionErrorDetalle = (descripcionErrorDetalle != null && !descripcionErrorDetalle.isEmpty() ? descripcionErrorDetalle.replaceAll("\\r\\n|\\r|\\n", " ") : "");
                    repreRespuestaValidacion.put("descripcion", descripcionErrorDetalle);
                    Lan6Excepcion exLan6 = new Lan6Excepcion(MELANBIDE43_GestionRdR_Constantes.PREFIJO_COD_ERROR
                            + "100", "Excepcion General al validar los representantes legales de " + InteresadoSolicitanteIdPoderdante + " : " + e.getMessage());
                    exLan6.setStackTrace(e.getStackTrace());
                    exLan6.setTrazaExcepcion(e.getLocalizedMessage());
                    idError = MELANBIDE43_GestionErroresRdR.grabarError(exLan6, MELANBIDE43_GestionRdR_Constantes.PREFIJO_COD_ERROR
                            + "100", descripcionErrorDetalle, numExpediente, codIdNotificacion, idProcedimientoEnPlatea, InteresadoSolicitanteIdPoderdante, "validarExistActualRolRepresContraRdR - invocargrantorRepresentationsLan6");
                    log.error("Error Registrado en el gestor de errores con ID  : " + idError);
                } catch (Exception ex) {
                    log.error("Error al registrar errores en validacion RdR " + ex.getMessage(), ex);
                }
                return repreRespuestaValidacion;
            } finally {
                try {
                    adaptador.devolverConexion(con);
                } catch (BDException ex) {
                    log.error("Error al cerrar la conexion  : " + ex.getMensaje(), ex);
                }
                log.info("validarExistActualRolRepresContraRdR - End () " + sdf.format(new Date()) + " - " + (repreRespuestaValidacion.size() > 0 ? repreRespuestaValidacion.get("codigo") : ""));
            }
        }else{
            log.info(" Servicios de validcion RdR Desactivado en la propiedad  VALIDACION_RDR_ACTIVA del fichero de configuracion MELANBIDE43_GestionRdR.properties");
            arregloParamMensaRespuesta = null;
            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("0", idioma, arregloParamMensaRespuesta);
        }
        return repreRespuestaValidacion;
    }

    /**
     * 
     * @param participanteRepreRespuesta
     * @param caso : Caso desde donde se invoca : 1 = Con Representantes en Regexlan 0 Sin representantes
     * @return 
     */
    private Map<String, String> comprobarActualizarRepreFromRdRToRegexlan(ApoderadoType apoderadoType,int codOrganizacion, String numExpediente, String codProcedimiento, int codRolRepreProcedimientoInt, String caso,int idioma,Integer codIdNotificacion ,Connection con) {
        Map<String,String> repreRespuestaValidacion  =  new HashMap<String, String>();
        String lan6ApoderadoTypeID = apoderadoType!=null?apoderadoType.getIdApoderado():"";
        String arregloParamMensaRespuesta[] = null;
        MELANBIDE43_GestionRdR_Util MELANBIDE43_GestionRdR_Util_Instace = MELANBIDE43_GestionRdR_Util.getInstance();
        try {
//            if (participanteRepreRespuesta != null && participanteRepreRespuesta.size() > 0) {
                // En principio deberia ser solo uno.
//                log.info("Numero de represenantes recuperados desde RdR :" + participanteRepreRespuesta.size());
                //Procedemos a crear el respresentante en el Expediente. 
                // Que mirar si existe en Regexlan, recupear su codigoTercero y numero de version sino, darlo de alta.
//                for (Lan6Apoderado lan6Apoderado : participanteRepreRespuesta) {
                    log.info("lan6ApoderadoTypeID : " + lan6ApoderadoTypeID);
                    log.info("IdNotificacion : " + codIdNotificacion);
                    TercerosValueObject tercero = MeLanbide43Manager.getInstance().getDatosBasicosterceroRepreRdR(lan6ApoderadoTypeID, con);
                    if (apoderadoType!=null && !(tercero != null && tercero.getCodTerceroOrigen() != null && !tercero.getCodTerceroOrigen().isEmpty())) {
                        // Procedemos a crear el tercero En regexlan
                        log.info("Invocamos dar de alta tercero regexlan " +lan6ApoderadoTypeID + " " + apoderadoType.getNombreApoderado());
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
                        String nombreCompleto = (apoderadoType.getApellido1()!=null && !apoderadoType.getApellido1().isEmpty()? apoderadoType.getApellido1() : "");
                        nombreCompleto+=(nombreCompleto.isEmpty()?"":" ").concat(apoderadoType.getApellido2()!=null && !apoderadoType.getApellido2().isEmpty()? apoderadoType.getApellido2(): "");
                        nombreCompleto+=(nombreCompleto.isEmpty()?"":", ").concat(apoderadoType.getNombre()!=null && !apoderadoType.getNombre().isEmpty()? apoderadoType.getNombre() : "");
                        log.info("Nombre preparado en M43_RdR " + nombreCompleto );
                        terceroVO.setNombreCompleto(apoderadoType.getNombreApoderado());
                        int idTercero = TercerosManager.getInstance().setTercero(terceroVO, MELANBIDE43_GestionRdR_Util_Instace.getParamsAdaptSQLBD(String.valueOf(codOrganizacion)));
                        log.info("Hemos datos de alta el tercero: " +
                                idTercero
                        );
                        // Generar Domicilio desconocido del tercero
                        DomicilioSimpleValueObject domicilio = new DomicilioSimpleValueObject();
                        Config m_ConfigCommon = ConfigServiceHelper.getConfig("common");
                        domicilio.setIdPais(MELANBIDE43_GestionRdR_Constantes.CODIGO_PAIS_ESPANA);
                        domicilio.setIdProvincia(m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO));
                        domicilio.setIdMunicipio(m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO));
                        domicilio.setDescVia(m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA));
                        domicilio.setTipoVia(Integer.toString(es.altia.agora.technical.ConstantesDatos.TIPO_VIA_SINVIA));
                        domicilio.setEsDomPrincipal("true");
                        log.info("Vamos a dar de alta el Domicilio del tercero ");
                        int idDomicilio = DomiciliosDAO.getInstance().altaDomicilio(domicilio,String.valueOf(MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN), con);
                        log.info("Domicilio Creado: " + idDomicilio);
                        log.info("Relacionar DomicilioTercero : " + 
                                TercerosDAO.getInstance().altaDomicilioTercero(idTercero, idDomicilio,String.valueOf(MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN), con)
                        );
                        // volvemos a recoger los datos del tercero
                        tercero=MeLanbide43Manager.getInstance().getDatosBasicosterceroRepreRdR(lan6ApoderadoTypeID, con);
                    }
                    if (tercero != null && tercero.getCodTerceroOrigen() != null && !tercero.getCodTerceroOrigen().isEmpty()) {
                        if(!existeNIFRepresentanteLegalEnExpedienteRdR(lan6ApoderadoTypeID,codOrganizacion,codRolRepreProcedimientoInt,codProcedimiento,numExpediente,con)){
                            // Si existe el tercero creamos el ROL para el expediente y lo relacionamos :  insertar una linea en E_EXT
                            if (MeLanbide43Manager.getInstance().insertarRepresentanteLegalExpedienteRdR(tercero, codOrganizacion, numExpediente, codProcedimiento, codRolRepreProcedimientoInt, con)) {
                                // Actualizamos la tabla  AUTORIZADO_NOTIFICACION con los datos del nuevo representante, numero de expediente e id notificacion asociada
                                // La actualizamos solo en caso de que vengamos desde el Batch de notificaciones, donde existe la notificacion y ademas la conocemos al irla tratando
                                if(codIdNotificacion>=0){
                                    try {
                                        AutorizadoNotificacionVO autorizadoEnvioNOtificacion = new AutorizadoNotificacionVO();
                                        autorizadoEnvioNOtificacion.setNumeroExpediente(numExpediente);
                                        autorizadoEnvioNOtificacion.setEjercicio(MELANBIDE43_GestionRdR_Util_Instace.getEjercicioDsdNumExpediente(numExpediente));
                                        autorizadoEnvioNOtificacion.setCodMunicipio(String.valueOf(codOrganizacion));
                                        autorizadoEnvioNOtificacion.setCodigoTercero(Integer.valueOf(tercero.getIdentificador()));
                                        autorizadoEnvioNOtificacion.setNumeroVersionTercero(Integer.valueOf(tercero.getVersion()));
                                        autorizadoEnvioNOtificacion.setCodigoNotificacion((codIdNotificacion != null ? codIdNotificacion : -1));
                                        AutorizadoNotificacionManager autorizadoNotificaiconManager = AutorizadoNotificacionManager.getInstance();
                                        if (autorizadoNotificaiconManager.insertarAutorizado(autorizadoEnvioNOtificacion, con)) {
                                            log.info("Dato del nuevo representante guardado correctamente en la tabla de autorizados para el envio de la notificaicon." + tercero.getDocumento() + "/" + tercero.getIdentificador() + "/" + tercero.getVersion());
                                        } else {
                                            log.error("NO se ha podido insertar el nuevo representante en la tabla de autorizados para el envio de la notificacion");
                                        }
                                    } catch (Exception e) {
                                        log.error("Error al actualizar la tabla AUTORIZADO_NOTIFICACION con un nuevo representante dado de alta en el expediente " + numExpediente + " - " + lan6ApoderadoTypeID, e);
                                    }
                                }else{
                                    log.info("NO actualizamos la tabla autorizados notificacion, porque Cod NOtificaiocn es " + codIdNotificacion);
                                }
                                
                                // Ejecutar llamada a Actualizar Interesado en Mi Carpeta
                                MELANBIDE43 melanbide43Instance = new MELANBIDE43();
                                String respuestaActualizaInteresados = "0";
                                String MensajeErrorActInteresado = "";
                                try {
                                    respuestaActualizaInteresados = melanbide43Instance.actualizaInteresados(codOrganizacion, 0, 0, numExpediente);
                                } catch (Exception e) {
                                    log.error(" Error al tratar de actualizar interesados en Mi Carpeta al relacionar en Regexlan el representante incrito en RdR ", e);
                                    respuestaActualizaInteresados = "1";
                                    MensajeErrorActInteresado = e.getMessage();
                                }
                                if ("0".equalsIgnoreCase(respuestaActualizaInteresados)) {
                                    log.info(" Representante recuperado en RdR , creado en Regexlan Correctamente y Actualizado en Mi Carpeta. " + numExpediente + "/" + lan6ApoderadoTypeID + "/ CodTercero: " + tercero.getCodTerceroOrigen());
                                    log.info(" -- Vamos a registrar la operacion en el Historico ");
                                    try {
                                        //OperacionesExpedienteManager.getInstance().registrarAltaInteresado(codOrganizacion, numExpediente, MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN, MELANBIDE43_GestionRdR_Constantes.LOGIN_NOMBRE_USUARIO_ADMIN, tercero, con);
                                        registrarOperacionHistoricoExpedienteGestionRdR(codOrganizacion, numExpediente, tercero, 1, con);
                                    } catch (Exception e) {
                                        log.error("Error al registrar la operacion en el historico de operaciones del Expediente : " + e.getMessage(), e);
                                    }
                                    //repreRespuestaValidacion.put("codigo","3");
                                    //repreRespuestaValidacion.put("descripcion", String.format(MELANBIDE43_GestionRdR_Util_Instace.getDescRespuestaPorCodigoIdioma("3", idioma), participante.getNif()));
                                    arregloParamMensaRespuesta = new String[]{lan6ApoderadoTypeID};
                                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("3", idioma, arregloParamMensaRespuesta);
                                    //return repreRespuestaValidacion;
                                } else {
                                    log.info(" Represenante recuperado en RdR y creado en Regexlan Correctamente. Pero no actualizado en Mi carpeta." + numExpediente + "/" + lan6ApoderadoTypeID + "/ CodTercero: " + tercero.getCodTerceroOrigen() + " - " + MensajeErrorActInteresado);
                                    //repreRespuestaValidacion.put("codigo","5");
                                    //repreRespuestaValidacion.put("descripcion", String.format(MELANBIDE43_GestionRdR_Util_Instace.getDescRespuestaPorCodigoIdioma("5", idioma), participante.getNif(),MensajeErrorActInteresado));
                                    arregloParamMensaRespuesta = new String[]{lan6ApoderadoTypeID, numExpediente, MensajeErrorActInteresado};
                                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("5", idioma, arregloParamMensaRespuesta);
                                    //return repreRespuestaValidacion;
                                }
                            } else {
                                log.info(" Represenante recuperado en RdR - Error al insertar el ROL en Regexlan." + numExpediente + "/" + lan6ApoderadoTypeID);
                                //repreRespuestaValidacion.put("codigo","6");
                                //repreRespuestaValidacion.put("descripcion", String.format(MELANBIDE43_GestionRdR_Util_Instace.getDescRespuestaPorCodigoIdioma("6", idioma),participante.getNif()));
                                arregloParamMensaRespuesta = new String[]{lan6ApoderadoTypeID};
                                repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("6", idioma, arregloParamMensaRespuesta);
                            }
                        }else{
                            // Este puede ser un caso especial, por ejemplo si hay mas de un representa legal en regexlan, el primero que se valida no es corrrecto 
                            // entra aqui porque al leer todos ls buenos de rdr puede ser que el segundo en regexlan sea valido, evitamos volverlo a insertar
                            log.info(" Existe Representante RdR en el Expediente y es Valido en RdR - No ejecutamos la insercion mas.");
                            arregloParamMensaRespuesta = null;
                            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("0", idioma, arregloParamMensaRespuesta);
                        }
                    } else {
                        log.info(" Representante recuperado en RdR no encontrado en Regexlan." + numExpediente + "/" + lan6ApoderadoTypeID);
                        arregloParamMensaRespuesta = new String[]{lan6ApoderadoTypeID};
                        repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("4", idioma, arregloParamMensaRespuesta);
                        
                    }
//                }
//            } else {
//                if("0".equalsIgnoreCase(caso)){
//                    log.info(" No se han recuperado Interesados con el ROL REPRESENTANTE para el expediente, ni en Regexlan ni en RdR. " + numExpediente);
//                    //repreRespuestaValidacion.put("codigo","2");
//                    //repreRespuestaValidacion.put("descripcion", MELANBIDE43_GestionRdR_Util_Instace.getDescRespuestaPorCodigoIdioma("2", idioma));
//                    arregloParamMensaRespuesta = null;
//                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("2", idioma, arregloParamMensaRespuesta);
//                    //return repreRespuestaValidacion;
//                }else if("1".equalsIgnoreCase(caso)){
//                    log.info(" No se han recuperado Interesados con el ROL REPRESENTANTE en RdR. -  Se han eliminado los existentes en regexlan " + numExpediente);
//                    arregloParamMensaRespuesta = new String[]{numExpediente};;
//                    repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("10", idioma, arregloParamMensaRespuesta);
//                }
//            }
        } catch (Exception e) {
            log.error("Error al actualizar representante en Regexlan con datos desde RdR", e);
            arregloParamMensaRespuesta = new String[]{e.getMessage()};
            repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util_Instace.prepararMapRespuesta("100", idioma, arregloParamMensaRespuesta);
        }
        return repreRespuestaValidacion;
    }

    private String[] actualizarInteresadosMiCarpetaGestionRdR(int codOrganizacion, String numExpediente) {
        log.info("actualizarInteresadosMiCarpetaGestionRdR - Begin");
        String[] respuestaActualizaInteresados = new String[2];
        // Ejecutar llamada a Actualizar Interesado en Mi Carpeta
        MELANBIDE43 melanbide43Instance = new MELANBIDE43();
        try {
            respuestaActualizaInteresados[0] = melanbide43Instance.actualizaInteresados(codOrganizacion, 0, 0, numExpediente);
            respuestaActualizaInteresados[1] = "OK";
        } catch (Exception e) {
            log.error(" Error al tratar de actualizar interesados en Mi Carpeta al relacionar en Regexlan el representante incrito en RdR ", e);
            respuestaActualizaInteresados[0] = "1";
            respuestaActualizaInteresados[1] = e.getMessage();
        }
        log.info("actualizarInteresadosMiCarpetaGestionRdR - END " );
        return respuestaActualizaInteresados;
    }

    /**
     * 
     * @param codOrganizacion
     * @param numExpediente
     * @param tercero
     * @param tipoOperacion : (1) Alta Interesado, (2) Baja Interesado del Expediente
     * @param con
     * @return 
     */
    private boolean registrarOperacionHistoricoExpedienteGestionRdR(int codOrganizacion, String numExpediente,TercerosValueObject tercero, int tipoOperacion, Connection con) {
        log.info(" -- Vamos a registrar la operacion en el Historico ");
        try {
            switch(tipoOperacion){
                case 1:
                    tercero.setCodRol(2); // Asignamos el rol con el que se dio de alta el interesado en la relacion con el expediente
                    tercero.setNotificacionElectronica("1"); // Asignamos el Notificacion electronica con la que se dio de alta el interesado en la relacion con el expediente
                    OperacionesExpedienteManager.getInstance().registrarAltaInteresado(codOrganizacion, numExpediente, MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN, MELANBIDE43_GestionRdR_Constantes.LOGIN_NOMBRE_USUARIO_ADMIN, tercero, con);
                    break;
                case 2:
                    OperacionesExpedienteManager.getInstance().registrarEliminacionInteresado(codOrganizacion, numExpediente, MELANBIDE43_GestionRdR_Constantes.CODIGO_USUARIO_ADMIN, MELANBIDE43_GestionRdR_Constantes.LOGIN_NOMBRE_USUARIO_ADMIN, tercero, con);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("Error al registrar la operacion en el historico de operaciones del Expediente : " + e.getMessage(), e);
            return  Boolean.FALSE;
        }
        return  Boolean.TRUE;
    }
    
    // NO SE USA
    private boolean invocarIsValidRepresentationLan6(String InteresadoSolicitanteIdPoderdante, String IdApoderado, String idProcedimientoEnPlatea, String idActuacion) throws Lan6Excepcion{
        boolean respuesta = false;
        try {
            Lan6RdRServicios servicios = new Lan6RdRServicios(idProcedimientoEnPlatea);
            IsValidRepresentationDocument isValidRepresentationDocument = IsValidRepresentationDocument.Factory.newInstance();
            ValidRepresentationType validRepresentationType = isValidRepresentationDocument.addNewIsValidRepresentation();
            validRepresentationType.setIdPoderdante(InteresadoSolicitanteIdPoderdante);  //("10000069Z");
            validRepresentationType.setIdApoderado(IdApoderado);       //"A41474651"
            //10000068JÂ 
            //A41474651 es representante de 10000069Z
            //10000070S es empleado de  A41474651
            //Tercero de confianza 44100104L

            validRepresentationType.setIdProcedimiento(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ID_PROC_ARCHIVO_DIGITAL_"+idProcedimientoEnPlatea));
            validRepresentationType.addNewListaActuaciones().addIdActuacion(idActuacion); //"5"
            log.info("parametros Asignados al ejecutar la  llamada isValidRepresentationLan6 : "
                    + " setIdPoderdante : " + InteresadoSolicitanteIdPoderdante
                    + " setIdApoderado : " + IdApoderado
                    + " setIdProcedimiento : " + (gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ID_PROC_ARCHIVO_DIGITAL_"+idProcedimientoEnPlatea))
                    + " addNewListaActuaciones : " + Arrays.toString(validRepresentationType.getListaActuaciones().getIdActuacionArray())
            );

            //Para una persona empleada devuelve false, no es válida la representación
            respuesta = servicios.isValidRepresentationLan6(isValidRepresentationDocument);
            log.info("llamada ejecutada -  Respuesta " + respuesta);

        } catch (Lan6Excepcion e) {
            log.error(" - invocarIsValidRepresentationLan6 - Error al validar la representacion " + e.getMensajeExcepcion(), e);
            throw e;
        }        
        return respuesta;
    }
    
        // NO SE USA
    private ArrayList<Lan6Apoderado>  invocargrantorRepresentationsLan6(String InteresadoSolicitanteIdPoderdante, String idProcedimientoEnPlatea) throws Lan6Excepcion{
        log.info(" Inciamos la llamada a grantorRepresentationsLan6 " + idProcedimientoEnPlatea);
        ArrayList<Lan6Apoderado> listaRepresentantes = new ArrayList<Lan6Apoderado>();
        try {
            
            Lan6RdRServicios servicios = new Lan6RdRServicios(idProcedimientoEnPlatea);
            PoderdanteInfoDocument poderdanteInfoDocument = PoderdanteInfoDocument.Factory.newInstance();
            PoderdanteInfoType poderdanteInfoType = poderdanteInfoDocument.addNewPoderdanteInfo();

            poderdanteInfoType.setIdPoderdante(InteresadoSolicitanteIdPoderdante);
            //poderdanteInfoType.setIdPoderdante("10000069Z");
            //poderdanteInfoType.setIdPoderdante("A41474651");

            poderdanteInfoType.setSoloVigente(true);
            poderdanteInfoType.setObtenerTerceros(false);
            poderdanteInfoType.addNewListaProcedimientos().addIdProcedimiento(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("ID_PROC_ARCHIVO_DIGITAL_"+idProcedimientoEnPlatea));
            poderdanteInfoType.addNewListaActuaciones().addIdActuacion(Lan6Constantes.RDR_ACTUACION_NOTIFICAR);
            log.info(
                    "Vamos a ejecutar la llamada grantorRepresentationsLan6 con parametros : "
                    + " setIdPoderdante : " + poderdanteInfoType.getIdPoderdante()
                    + " setSoloVigente : " + poderdanteInfoType.getSoloVigente()
                    + " setObtenerTerceros : " + poderdanteInfoType.getObtenerTerceros()
                    + " addNewListaProcedimientos : " + Arrays.toString(poderdanteInfoType.getListaProcedimientos().getIdProcedimientoArray())
                    + " addNewListaActuaciones : " + Arrays.toString(poderdanteInfoType.getListaActuaciones().getIdActuacionArray())
            );
            Lan6ResultGrantorRepresentations resultGrantorRepresentationsLan6 = servicios.grantorRepresentationsLan6(poderdanteInfoDocument);
            log.info("llamada ejecutada -  Respuesta " + (resultGrantorRepresentationsLan6 != null && resultGrantorRepresentationsLan6.getListaRepresentantes() != null ? resultGrantorRepresentationsLan6.getListaRepresentantes().size() : "respuesta o lista de representantes recibida a Null"));
            if(resultGrantorRepresentationsLan6!=null && resultGrantorRepresentationsLan6.getListaRepresentantes()!=null){
                listaRepresentantes = resultGrantorRepresentationsLan6.getListaRepresentantes();                
            }else{
                log.error("-- Respuesta/getListaRepresentantes del grantorRepresentationsLan6 recibida a null -- Retornamos la Instacia de lista creada, sin representantes.");
            }
            //ArrayList<Lan6Tercero> listaTerceros = resultGrantorRepresentationsLan6.getListaTerceros();
            log.info("Numero de terceros recuperados pueden ser o no de confianza, Solo informativo :" + (resultGrantorRepresentationsLan6 != null && resultGrantorRepresentationsLan6.getListaTerceros() != null ? resultGrantorRepresentationsLan6.getListaTerceros().size() : "respuesta o lista de terceros recibida a Null"));
            //ListaTercerosDeConfianza listaTercerosConfianza = resultGrantorRepresentationsLan6.getListaTercerosDeConfianza();
            log.info("Numero de terceros de confianza recuperados, Solo informativo :" + (resultGrantorRepresentationsLan6 != null && resultGrantorRepresentationsLan6.getListaTercerosDeConfianza() != null ? resultGrantorRepresentationsLan6.getListaTercerosDeConfianza().sizeOfTerceroDeConfianzaArray() : "respuesta o lista de terceros de confianza  recibida a Null"));
            
        } catch (Lan6Excepcion e) {
            log.error(" - invocarIsValidRepresentationLan6 - Error al validar la representacion " + e.getMensajeExcepcion(), e);
            throw e;
        }
        return listaRepresentantes;
    }
    
    /**
     * Recupera la informacion de representacion como en el grantor Lan6ResultGrantorRepresentations resultGrantorRepresentationsLan6 = datosInteresados.getResultGrantorRepresentationsLan6()
     * Tiene como parometro de entrada el identificador del titular. El objeto de retorno de motodo DatosInteresados.
     * Tambien recupera en el objeto Lan6DatosAvisos la informacion para actualizar los datos de avisos del expediente.
     * @param InteresadoSolicitanteIdPoderdante
     * @param idProcedimientoEnPlatea
     * @return Lan6DatosAvisos 
     * @throws Lan6Excepcion 
     */
    private Lan6DatosInteresados  invocarRecuperarDatosInteresados (String InteresadoSolicitanteIdPoderdante, String idProcedimientoEnPlatea,String numExpediente, Integer codIdNotificacion,int idioma) throws Lan6Excepcion{
        log.info(" invocarRecuperarDatosInteresados " + idProcedimientoEnPlatea 
                + " " + InteresadoSolicitanteIdPoderdante 
                + " " + numExpediente 
        );
        Lan6DatosInteresados respuesta = null;
        try {
            String[] datosExp = numExpediente.split(ConstantesDatos.BARRA);
            Lan6RdRServicios servicios = new Lan6RdRServicios(datosExp[1]); // 2.2
            log.debug("Vamos a ejecutar la llamada recuperarDatosInteresados  con parametros : "
                    + " InteresadoSolicitanteIdPoderdante : " + InteresadoSolicitanteIdPoderdante
            );
            respuesta = servicios.recuperarDatosInteresados(InteresadoSolicitanteIdPoderdante);
            log.debug("llamada ejecutada -  Respuesta(Lan6DatosInteresados) " + (respuesta != null ? " Correcta." : " Recibida a Null."));
        } catch (Lan6Excepcion e) {
            log.error("Error al llamar a los Servicios de platea para verificar la validez de los representantes : " + e.getMessage(), e);
            String codigosError =  ( e.getCodes()!=null ? Arrays.toString(e.getCodes().toArray()) : "");
            String mensajeError =  ( e.getMessages()!=null ? Arrays.toString(e.getMessages().toArray()) : "");
            log.error("Mensaje de Excepcion compuesto : " + ("["+codigosError+"]-["+mensajeError+"]"));
            String []arregloParamMensaRespuesta = new String[]{InteresadoSolicitanteIdPoderdante, numExpediente, codigosError + " : " + mensajeError};
            Map<String,String>repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("107", idioma, arregloParamMensaRespuesta);
            try {
                String descripcionErrorDetalle = repreRespuestaValidacion.get("descripcion");
                Integer idError = MELANBIDE43_GestionErroresRdR.grabarError(e, MELANBIDE43_GestionRdR_Constantes.PREFIJO_COD_ERROR
                        + "107", descripcionErrorDetalle, numExpediente, codIdNotificacion, idProcedimientoEnPlatea, InteresadoSolicitanteIdPoderdante, "validarExistActualRolRepresContraRdR - recuperarDatosInteresados");
                log.error("Error Registrado ID  : " + idError);
            } catch (Exception ex) {
                log.error("Error al registrar errores en validacion RdR " + ex.getMessage(), ex);
            }
        }
        return respuesta;
    }

    private boolean existeNIFRepresentanteLegalEnExpedienteRdR(String lan6ApoderadoTypeID, int codOrganizacion,int codRolRepreProcedimientoInt, String codProcedimiento, String numExpediente, Connection con) throws Exception {
        log.info("existeNIFRepresentanteLegalEnExpedienteRdR Begin "  );
        try {
            return MeLanbide43Manager.getInstance().existeNIFRepresentanteLegalEnExpedienteRdR(lan6ApoderadoTypeID,codOrganizacion,codRolRepreProcedimientoInt,codProcedimiento,numExpediente,con);
        } catch (Exception e) {
            log.error(" Error al comprobar si existe un NIF como rol representante legal en un expediente ", e);
            throw  e;
        }
    }

    /**
     * 
     * @param nif
     * @param listaRespuestaRepresentante
     * @return ApoderadoType En la lista de representantes RdR o null sino hay representante en la lista o si se presenta un error.
     */
    private ApoderadoType getLan6ApoderadoFromListaRespuestaxNIF(String nif, ArrayList<Lan6Apoderado> listaRespuestaRepresentante) {
        log.info("getLan6ApoderadoFromListaRespuestaxNIF - Begin ()  -  " + nif );
        try {
            if(nif!=null && !nif.isEmpty()){
                if (listaRespuestaRepresentante != null && listaRespuestaRepresentante.size() > 0) {
                    for (Lan6Apoderado lan6Apoderado : listaRespuestaRepresentante) {
                        if(lan6Apoderado.getApoderadoType() != null){
                            ApoderadoType a = lan6Apoderado.getApoderadoType();
                            log.debug( "getListaProcedimientos " +
                                    (a.getListaProcedimientos()!=null ?
                                            Arrays.toString(a.getListaProcedimientos().getIdProcedimientoArray()):"Lista a null")
                            );
                            log.debug( "getListaActuaciones " +
                                    (a.getListaActuaciones()!=null ?
                                            Arrays.toString(a.getListaActuaciones().getIdActuacionArray()):"Lista a null")
                            );
                            log.debug( "getListaAreasActuaciones " +
                                    (a.getListaAreasActuaciones()!=null ?
                                            Arrays.toString(a.getListaAreasActuaciones().getIdAreaActuacionArray()):"Lista a null")
                            );
                        }
                        String nifRdR = (lan6Apoderado.getApoderadoType() != null ? lan6Apoderado.getApoderadoType().getIdApoderado() : "");
                        if (nif.equalsIgnoreCase(nifRdR)) {
                            return lan6Apoderado.getApoderadoType();
                        }
                    }
                } else {
                    log.info("getLan6ApoderadoFromListaRespuestaxNIF - La lista de representantes RdR es null, o no tiene ningun representante. ");
                }
            }else{
                log.info("No podemos recuperar el datos de representante RdR desde la lista repsuesta, el DNI Buscar viene a null o vacio ");
            }
        } catch (Exception e) {
            log.error("Error al obtener el apoderado de la lista repuesta de RdR por DNI " + e.getMessage());
        }
        return null;
    }

    private boolean comprobarDatosAVISORegexlanVsRdr(int codOrganizacion, String numExpediente, Lan6DatosAvisos datosAviso, Connection con) throws Exception {
        boolean respuesta = false;
        try {
            DatosAvisoCSRegexlan datosAvisoCSRegexlan = MeLanbide43Manager.getInstance().getMapaDatosSuplementarioAvisosExpediente(numExpediente, con);
            String valorCampoAvisoEMAIL_ACTUAL = datosAvisoCSRegexlan != null && datosAvisoCSRegexlan.getAvisoEmailRepresentante() != null ? datosAvisoCSRegexlan.getAvisoEmailRepresentante() : "";
            String valorCampoAvisoSMS_ACTUAL = datosAvisoCSRegexlan != null && datosAvisoCSRegexlan.getAvisoSmsRepresentante() != null ? datosAvisoCSRegexlan.getAvisoSmsRepresentante() : "";
            String valorCampoAvisoIDCTAINTE_ACTUAL = datosAvisoCSRegexlan != null && datosAvisoCSRegexlan.getAvisoIdCuentaInteresadoRepresentante() != null ? datosAvisoCSRegexlan.getAvisoIdCuentaInteresadoRepresentante() : "";
            String valorCampoAvisoEMAIL_ACTUAL_TITULAR = datosAvisoCSRegexlan != null && datosAvisoCSRegexlan.getAvisoEmailTitular() != null ? datosAvisoCSRegexlan.getAvisoEmailTitular() : "";
            String valorCampoAvisoSMS_ACTUAL_TITULAR = datosAvisoCSRegexlan != null && datosAvisoCSRegexlan.getAvisoSmsTitular() != null ? datosAvisoCSRegexlan.getAvisoSmsTitular() : "";
            String valorCampoAvisoIDCTAINTE_ACTUAL_TITULAR = datosAvisoCSRegexlan != null && datosAvisoCSRegexlan.getAvisoIdCuentaInteresadoTitular() != null ? datosAvisoCSRegexlan.getAvisoIdCuentaInteresadoTitular() : "";
            String valorCampoAvisoEMAIL_RDR = "";
            String valorCampoAvisoSMS_RDR = "";
            String valorCampoAvisoEMAIL_RDR_TITULAR = "";
            String valorCampoAvisoSMS_RDR_TITULAR = "";
            String valorCampoAvisoIDCTAINTE_RDR_TITULAR = "";
            String valorCampoAvisoIDCTAINTE_RDR = "";
            if (datosAviso != null) {
                valorCampoAvisoEMAIL_RDR = datosAviso.getMailRepreVigente();
                valorCampoAvisoSMS_RDR = datosAviso.getSmsRepreVigente();
                valorCampoAvisoEMAIL_RDR_TITULAR = datosAviso.getMailTitular();
                valorCampoAvisoSMS_RDR_TITULAR = datosAviso.getSmsTitular();
                valorCampoAvisoIDCTAINTE_RDR_TITULAR = datosAviso.getIdCuentaTitular() != null ? String.valueOf(datosAviso.getIdCuentaTitular()) : "";
                valorCampoAvisoIDCTAINTE_RDR = datosAviso.getIdCuentaRepreVigente();
            }
            // Comprobacion campo EMAIL REPRESENTANTE
            if (valorCampoAvisoEMAIL_ACTUAL != null && !valorCampoAvisoEMAIL_ACTUAL.isEmpty()) {
                if (valorCampoAvisoEMAIL_RDR != null && !valorCampoAvisoEMAIL_RDR.isEmpty()) {
                    log.debug("Comparamos los datos EMAIL Representante ... Regexlan / RdR : ");
                    if (valorCampoAvisoEMAIL_ACTUAL.trim().equalsIgnoreCase(valorCampoAvisoEMAIL_RDR.trim())) {
                        log.debug(" Son Iguales los datos EMAIL... Regexlan / RdR - No Hacemos nada ");
                    } else {
                        // Actualizamos Regexlan con los datos de RdR
                        log.debug(" NO Son Iguales los datos EMAIL... Regexlan / RdR - Actualizamos "
                                + valorCampoAvisoEMAIL_ACTUAL + " / " + valorCampoAvisoEMAIL_RDR);
                        log.debug("-- Actualizando Representante el valor EMAIL : "
                                + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAIL, valorCampoAvisoEMAIL_RDR.trim(), con)
                        );
                        respuesta = true;
                    }
                } else {
                    log.debug("Hay datos en Regexlan EMAIL pero no en RdR. No ejecutamos ninguna accion."
                    // + MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, codCampoAvisoEMAIL, con)
                    );
                }
            } else {
                // En Regexlan no hay datos EMAIL comprobamos si hay en RDR
                log.debug("No datos de aviso Representante Email en Regexlan - comprobamos los de le RdR");
                if (valorCampoAvisoEMAIL_RDR != null && !valorCampoAvisoEMAIL_RDR.isEmpty()) {
                    log.debug("-- Actualizado Representante el valor EMAIL : "
                            + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAIL, valorCampoAvisoEMAIL_RDR.trim(), con)
                    );
                    respuesta = true;
                } else {
                    log.debug("No datos de aviso EMAIL en RdR Tampoco. - No ejecutamos Ninguna accion. ");
                }
            }
            // Comprobacion campo EMAIL TITULAR
            if (valorCampoAvisoEMAIL_ACTUAL_TITULAR != null && !valorCampoAvisoEMAIL_ACTUAL_TITULAR.isEmpty()) {
                if (valorCampoAvisoEMAIL_RDR_TITULAR != null && !valorCampoAvisoEMAIL_RDR_TITULAR.isEmpty()) {
                    log.debug("Comparamos los datos EMAIL Titular ... Regexlan / RdR : ");
                    if (valorCampoAvisoEMAIL_ACTUAL_TITULAR.trim().equalsIgnoreCase(valorCampoAvisoEMAIL_RDR_TITULAR.trim())) {
                        log.debug(" Son Iguales los datos EMAIL... Regexlan / RdR - No Hacemos nada ");
                    } else {
                        // Actualizamos Regexlan con los datos de RdR
                        log.debug(" NO Son Iguales los datos EMAIL... Regexlan / RdR - Actualizamos "
                                + valorCampoAvisoEMAIL_ACTUAL_TITULAR + " / " + valorCampoAvisoEMAIL_RDR_TITULAR);
                        log.debug("-- Actualizando Titular el valor EMAIL : "
                                + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAILTIT, valorCampoAvisoEMAIL_RDR_TITULAR.trim(), con)
                        );
                        respuesta = true;
                    }
                } else {
                    log.debug("Hay datos en Regexlan EMAIL pero no en RdR. No ejecutamos ninguna accion."
                    // + MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, codCampoAvisoEMAIL_TITULAR, con)
                    );
                }
            } else {
                // En Regexlan no hay datos EMAIL comprobamos si hay en RDR
                log.debug("No datos de aviso Titular Email en Regexlan - comprobamos los de le RdR");
                if (valorCampoAvisoEMAIL_RDR_TITULAR != null && !valorCampoAvisoEMAIL_RDR_TITULAR.isEmpty()) {
                    log.debug("-- Actualizando Representante el valor EMAIL : "
                            + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAILTIT, valorCampoAvisoEMAIL_RDR_TITULAR.trim(), con)
                    );
                    respuesta = true;
                } else {
                    log.debug("No datos de aviso EMAIL en RdR Tampoco. - No ejecutamos Ninguna accion. ");
                }
            }
            // Comprobacion campo SMS Representante
            if (valorCampoAvisoSMS_ACTUAL != null && !valorCampoAvisoSMS_ACTUAL.isEmpty()) {
                if (valorCampoAvisoSMS_RDR != null && !valorCampoAvisoSMS_RDR.isEmpty()) {
                    log.debug("Comparamos los datos SMS Representante ... Regexlan / RdR : ");
                    if (valorCampoAvisoSMS_ACTUAL.trim().equalsIgnoreCase(valorCampoAvisoSMS_RDR.trim())) {
                        log.debug(" Son Iguales los datos SMS... Regexlan / RdR - No Hacemos nada ");
                    } else {
                        // Actualizamos Regexlan con los datos de RdR
                        log.debug(" NO Son Iguales los datos... Regexlan / RdR - Actualizamos "
                                + valorCampoAvisoSMS_ACTUAL + " / " + valorCampoAvisoSMS_RDR);
                        log.info("-- Actualizando Representante el valor SMS : "
                                + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMS, valorCampoAvisoSMS_RDR.trim(), con)
                        );
                        respuesta = true;
                    }
                } else {
                    log.debug("Hay datos en Regexlan EMAIL pero no en RdR. No ejecutamos ninguna accion. "
                    //+ MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, codCampoAvisoSMS,con)
                    );
                }
            } else {
                // En Regexlan no hay datos SMS comprobamos si hay en RDR
                log.debug("No datos de aviso SMS en Regexlan - comprobamos los de le RdR");
                if (valorCampoAvisoSMS_RDR != null && !valorCampoAvisoSMS_RDR.isEmpty()) {
                    log.info("-- Actualizando Representante el valor SMS : "
                            + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMS, valorCampoAvisoSMS_RDR.trim(), con)
                    );
                    respuesta = true;
                } else {
                    log.debug("No datos de aviso SMS en RdR Tampoco. - No ejecutamos Ninguna accion. ");
                }
            }
            // Comprobacion campo SMS Titular
            if (valorCampoAvisoSMS_ACTUAL_TITULAR != null && !valorCampoAvisoSMS_ACTUAL_TITULAR.isEmpty()) {
                if (valorCampoAvisoSMS_RDR_TITULAR != null && !valorCampoAvisoSMS_RDR_TITULAR.isEmpty()) {
                    log.debug("Comparamos los datos SMS Titular ... Regexlan / RdR : ");
                    if (valorCampoAvisoSMS_ACTUAL_TITULAR.trim().equalsIgnoreCase(valorCampoAvisoSMS_RDR_TITULAR.trim())) {
                        log.debug(" Son Iguales los datos SMS... Regexlan / RdR - No Hacemos nada ");
                    } else {
                        // Actualizamos Regexlan con los datos de RdR
                        log.debug(" NO Son Iguales los datos... Regexlan / RdR - Actualizamos "
                                + valorCampoAvisoSMS_ACTUAL_TITULAR + " / " + valorCampoAvisoSMS_RDR_TITULAR);
                        log.info("-- Actualizando Titular el valor SMS : "
                                + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMSTIT, valorCampoAvisoSMS_RDR_TITULAR.trim(), con)
                        );
                        respuesta = true;
                    }
                } else {
                    log.debug("Hay datos en Regexlan EMAIL pero no en RdR. No ejecutamos ninguna accion. "
                    //+ MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, codCampoAvisoSMS_TITULAR,con)
                    );
                }
            } else {
                // En Regexlan no hay datos SMS comprobamos si hay en RDR
                log.debug("No datos de aviso SMS en Regexlan - comprobamos los de le RdR");
                if (valorCampoAvisoSMS_RDR_TITULAR != null && !valorCampoAvisoSMS_RDR_TITULAR.isEmpty()) {
                    log.info("-- Actualizando Titular el valor SMS : "
                            + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMSTIT, valorCampoAvisoSMS_RDR_TITULAR.trim(), con)
                    );
                    respuesta = true;
                } else {
                    log.debug("No datos de aviso SMS en RdR Tampoco. - No ejecutamos Ninguna accion. ");
                }
            }
            // Comprobacion campo ID Cuenta Interesado Titular
            if (valorCampoAvisoIDCTAINTE_ACTUAL_TITULAR != null && !valorCampoAvisoIDCTAINTE_ACTUAL_TITULAR.isEmpty()) {
                if (valorCampoAvisoIDCTAINTE_RDR_TITULAR != null && !valorCampoAvisoIDCTAINTE_RDR_TITULAR.isEmpty()) {
                    log.debug("Comparamos los datos ID CUENTA INTERESADO Titular ... Regexlan / RdR : ");
                    if (valorCampoAvisoIDCTAINTE_ACTUAL_TITULAR.trim().equalsIgnoreCase(valorCampoAvisoIDCTAINTE_RDR_TITULAR.trim())) {
                        log.debug(" Son Iguales los datos ID CUENTA INTERESADO... Regexlan / RdR - No Hacemos nada ");
                    } else {
                        // Actualizamos Regexlan con los datos de RdR
                        log.debug(" NO Son Iguales los datos... Regexlan / RdR - Actualizamos "
                                + valorCampoAvisoIDCTAINTE_ACTUAL_TITULAR + " / " + valorCampoAvisoIDCTAINTE_RDR_TITULAR);
                        log.info("-- Actualizando Titular el valor ID CUENTA INTERESADO : "
                                + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTETIT, valorCampoAvisoIDCTAINTE_RDR_TITULAR.trim(), con)
                        );
                        respuesta = true;
                    }
                } else {
                    log.debug("Hay datos en Regexlan Titular ID CUENTA INTERESADO pero no en RdR. Borramos datos porque se ha quitado la cuenta de consentimieto. "
                    //+ MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, codCampoAvisoSMS_TITULAR,con)
                    );
                    log.debug("Borramos datos ID Cuenta Interesado - Titular : "
                            + MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTETIT, con)
                    );
                    log.debug("Borramos datos EMAIL Interesado - Titular : "
                            + MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAILTIT, con)
                    );
                    log.debug("Borramos datos SMS Interesado - Titular : "
                            + MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMSTIT, con)
                    );
                    respuesta = true;
                }
            } else {
                // En Regexlan no hay datos ID CUENTA INTERESADO comprobamos si hay en RDR
                log.debug("No datos de aviso ID CUENTA INTERESADO en Regexlan - comprobamos los de le RdR");
                if (valorCampoAvisoIDCTAINTE_RDR_TITULAR != null && !valorCampoAvisoIDCTAINTE_RDR_TITULAR.isEmpty()) {
                    log.info("-- Actualizando Titular el valor ID CUENTA INTERESADO : "
                            + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTETIT, valorCampoAvisoIDCTAINTE_RDR_TITULAR.trim(), con)
                    );
                    respuesta = true;
                } else {
                    log.debug("No datos de aviso ID CUENTA INTERESADO en RdR Tampoco. - No ejecutamos Ninguna accion. ");
                }
            }
            // Comprobacion campo ID Cuenta Interesado Representante
            if (valorCampoAvisoIDCTAINTE_ACTUAL != null && !valorCampoAvisoIDCTAINTE_ACTUAL.isEmpty()) {
                if (valorCampoAvisoIDCTAINTE_RDR != null && !valorCampoAvisoIDCTAINTE_RDR.isEmpty()) {
                    log.debug("Comparamos los datos ID CUENTA INTERESADO Representante ... Regexlan / RdR : ");
                    if (valorCampoAvisoIDCTAINTE_ACTUAL.trim().equalsIgnoreCase(valorCampoAvisoIDCTAINTE_RDR.trim())) {
                        log.debug(" Son Iguales los datos ID CUENTA INTERESADO... Regexlan / RdR - No Hacemos nada ");
                    } else {
                        // Actualizamos Regexlan con los datos de RdR
                        log.debug(" NO Son Iguales los datos... Regexlan / RdR - Actualizamos "
                                + valorCampoAvisoIDCTAINTE_ACTUAL + " / " + valorCampoAvisoIDCTAINTE_RDR);
                        log.info("-- Actualizando Representante el valor ID CUENTA INTERESADO : "
                                + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTE, valorCampoAvisoIDCTAINTE_RDR.trim(), con)
                        );
                        respuesta = true;
                    }
                } else {
                    log.debug("Hay datos en Regexlan Representante ID CUENTA INTERESADO pero no en RdR. Borramos datos porque se ha quitado la cuenta de consentimieto. "
                    //+ MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, codCampoAvisoSMS_TITULAR,con)
                    );
                    log.debug("Borramos datos ID Cuenta Interesado - Representante : "
                            + MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTE, con)
                    );
                    log.debug("Borramos datos EMAIL Interesado - Representante : "
                            + MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAIL, con)
                    );
                    log.debug("Borramos datos SMS Interesado - Representante : "
                            + MeLanbide43Manager.getInstance().borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMS, con)
                    );
                    respuesta = true;
                }
            } else {
                // En Regexlan no hay datos ID CUENTA INTERESADO comprobamos si hay en RDR
                log.debug("No datos de aviso ID CUENTA INTERESADO en Regexlan - comprobamos los de le RdR");
                if (valorCampoAvisoIDCTAINTE_RDR != null && !valorCampoAvisoIDCTAINTE_RDR.isEmpty()) {
                    log.info("-- Actualizando Representante el valor ID CUENTA INTERESADO : "
                            + MeLanbide43Manager.getInstance().updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion, numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTE, valorCampoAvisoIDCTAINTE_RDR.trim(), con)
                    );
                    respuesta = true;
                } else {
                    log.debug("No datos de aviso ID CUENTA INTERESADO en RdR Tampoco. - No ejecutamos Ninguna accion. ");
                }
            }
        } catch (Exception e) {
            log.error(" Error al comprobar y actualizar los datos de avisos entre Regexlan/Rdr", e);
            throw e;
        }
        return respuesta;
    }
    
    /**
     * Recupera los datos del interesado desde el sistema de regexlan, Dato de Aviso.
     * @param InteresadoSolicitanteIdPoderdante
     * @param idProcedimientoEnPlatea
     * @param numExpediente
     * @param codIdNotificacion
     * @param idioma
     * @return TInteresado Con los datos de Aviso en Regexlan del representante con DNI Solicitado
     * @throws Lan6Excepcion 
     */
    private TInteresado invocarRecuperarCuentaInteresado(String InteresadoRepresentanteId, String idProcedimientoEnPlatea, String numExpediente, Integer codIdNotificacion, int idioma) throws Lan6Excepcion {
        log.info(" invocarRecuperarCuentaInteresado " + idProcedimientoEnPlatea
                + " " + InteresadoRepresentanteId
                + " " + numExpediente
        );
        TInteresado respuesta = null;
        try {
            String[] datosExp = numExpediente.split(ConstantesDatos.BARRA);
            Lan6RdRServicios servicios = new Lan6RdRServicios(datosExp[1]); // 2.2
            log.debug("Vamos a ejecutar la llamada invocarRecuperarCuentaInteresado  con parametros : "
                    + " InteresadoRepresentanteId : " + InteresadoRepresentanteId
            );
            respuesta = servicios.recuperarCuentaInteresado(InteresadoRepresentanteId);
            log.debug("llamada ejecutada -  Respuesta(TInteresado) " + (respuesta != null ? " Correcta." : " Recibida a Null."));
        } catch (Lan6Excepcion e) {
            log.error("Error al llamar a los Servicios de platea para verificar la validez de los representantes : " + e.getMessage(), e);
            String codigosError = (e.getCodes() != null ? Arrays.toString(e.getCodes().toArray()) : "");
            String mensajeError = (e.getMessages() != null ? Arrays.toString(e.getMessages().toArray()) : "");
            log.error("Mensaje de Excepcion compuesto : " + ("[" + codigosError + "]-[" + mensajeError + "]"));
            String[] arregloParamMensaRespuesta = new String[]{InteresadoRepresentanteId, numExpediente, codigosError + " : " + mensajeError};
            Map<String, String> repreRespuestaValidacion = MELANBIDE43_GestionRdR_Util.getInstance().prepararMapRespuesta("108", idioma, arregloParamMensaRespuesta);
            try {
                String descripcionErrorDetalle = repreRespuestaValidacion.get("descripcion");
                Integer idError = MELANBIDE43_GestionErroresRdR.grabarError(e, MELANBIDE43_GestionRdR_Constantes.PREFIJO_COD_ERROR
                        + "108", descripcionErrorDetalle, numExpediente, codIdNotificacion, idProcedimientoEnPlatea, InteresadoRepresentanteId, "validarExistActualRolRepresContraRdR - recuperarCuentaInteresado");
                log.error("Error Registrado ID  : " + idError);
            } catch (Exception ex) {
                log.error("Error al registrar errores en validacion RdR " + ex.getMessage(), ex);
            }
        }
        return respuesta;
    }
    
}
