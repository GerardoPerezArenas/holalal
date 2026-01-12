/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.validator;

import es.altia.flexia.integracion.moduloexterno.lanbide01.exception.MeLanbide01Exception;
import es.altia.flexia.integracion.moduloexterno.lanbide01.i18n.MeLanbide01I18n;
import es.altia.flexia.integracion.moduloexterno.lanbide01.manager.MeLanbide01Manager;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConnectionUtils;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Melanbide01DecretoExpedienteEnum;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.MeLanbide01Constantes;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Utilities;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.DatosCalculoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.MeLanbide01ValidatorResult;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01DepenPerSut;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MeLanbide01Validator {
    
    private final Logger log = LogManager.getLogger(MeLanbide01Validator.class);
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFecha_dd_MM_yyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final MeLanbide01I18n meLanbide01I18n = MeLanbide01I18n.getInstance();
    private final MeLanbide01ValidatorUtils meLanbide01ValidatorUtils = new MeLanbide01ValidatorUtils();
    
    public List<MeLanbide01ValidatorResult> validacionesAlarmasCONCM(int codigoOrganizacion, String codDecreto, String numeroExpediente, Integer idioma) {
        List<MeLanbide01ValidatorResult> respuesta = new ArrayList<MeLanbide01ValidatorResult>();
        if (numeroExpediente != null && !numeroExpediente.isEmpty()) {
            respuesta=validacionAlarmasCONCMGenerales(codigoOrganizacion,numeroExpediente,idioma,codDecreto);
            if (Melanbide01DecretoExpedienteEnum.D2010_177.getCodigoDecreto().equalsIgnoreCase(codDecreto)) {
                respuesta.addAll(this.validacionAlarmasCONCMdecreto2010_177(codigoOrganizacion,numeroExpediente,idioma));
            } else if (Melanbide01DecretoExpedienteEnum.D2019_164.getCodigoDecreto().equalsIgnoreCase(codDecreto)) {
                respuesta.addAll(this.validacionAlarmasCONCMdecreto2019_164(codigoOrganizacion,numeroExpediente,idioma));
            } else {
                MeLanbide01ValidatorResult meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.COD_MSG_ERROR_NO_FECHA_INICIO_PERIODOS));
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.COD_MSG_ERROR_NO_FECHA_INICIO_PERIODOS + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                respuesta.add(meLanbide01ValidatorResult);
            }
        } else {
            MeLanbide01ValidatorResult meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
            meLanbide01ValidatorResult.setCodigo(-999);
            meLanbide01ValidatorResult.setDescripcion("Numero de expediente no recibido en el proceso de validacion, datos del expediente sin validar.");
            respuesta.add(meLanbide01ValidatorResult);
        }
        if(respuesta.size()>0){
           MeLanbide01ValidatorResult meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
           meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma,"label.texto.validaciones.alarmas.expediente"));
           respuesta.add(0,meLanbide01ValidatorResult);
        }
        return respuesta;
    }
        

    private List<MeLanbide01ValidatorResult> validacionAlarmasCONCMGenerales(Integer codigoOrganizacion,String numeroExpediente,Integer idioma, String codDecretoAplica) {
        List<MeLanbide01ValidatorResult> respuesta = new ArrayList<MeLanbide01ValidatorResult>();
        MeLanbide01ValidatorResult meLanbide01ValidatorResult = null;
        // Campos Suplementarios Comunes en mas de una Validacion. Rellenamos en la primera y en las demas reutilizamos para no ejeuctar varias llamasdas a BD
        String reducPersSust=null;
        List<Melanbide01DepenPerSut> causantesSubvencion = new ArrayList<Melanbide01DepenPerSut>();
        Calendar fechaNacCasuSubv = null;
        try {
            Integer ejercicio = Utilities.getEjercicioFromNumExpediente(numeroExpediente);
            String codProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numeroExpediente);
            AdaptadorSQLBD adaptador = ConnectionUtils.getAdaptSQLBD(String.valueOf(codigoOrganizacion));
            causantesSubvencion = MeLanbide01Manager.getInstance().getTodosCausantesSubvencion(numeroExpediente, adaptador);
            fechaNacCasuSubv = meLanbide01ValidatorUtils.getCampoSuplementarioFecNacPersonaDependiente(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);            
            // Control 1 - Expedientes Fuera de Plazo
            try {
                String campoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codigoOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_SOLICITUD", MeLanbide01Constantes.FICHERO_CONFIGURACION);
                String tipoCampoSuplementarioFechaSolicitud = ConfigurationParameter.getParameter(codigoOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_SOLICITUD", MeLanbide01Constantes.FICHERO_CONFIGURACION);
                String campoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codigoOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", MeLanbide01Constantes.FICHERO_CONFIGURACION);
                String tipoCampoSuplementarioFechaFinActuacionConcedido = ConfigurationParameter.getParameter(codigoOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "TIPO_CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", MeLanbide01Constantes.FICHERO_CONFIGURACION);
                if (meLanbide01ValidatorUtils.compruebaSiExpedienteFueraPlazo(codigoOrganizacion, String.valueOf(ejercicio), codProcedimiento, numeroExpediente,
                        campoSuplementarioFechaSolicitud, tipoCampoSuplementarioFechaSolicitud,
                        campoSuplementarioFechaFinActuacionConcedido, tipoCampoSuplementarioFechaFinActuacionConcedido)) {
                    meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO));
                    meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                    respuesta.add(meLanbide01ValidatorResult);
                }
            } catch (Exception e) {
                log.error("Error al validar si expediente Fuera de plazo : " + e.getMessage(), e);
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO));
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_EXPEDIENTE_FUERA_PLAZO + MeLanbide01Constantes.SUFIJO_MSG_ERROR)
                        + " : " + e.getMessage());
                respuesta.add(meLanbide01ValidatorResult);
            }

            //Control 2 : Fecha Inicio Accion Subvencionable Concedida
            try {
                Calendar fecha = meLanbide01ValidatorUtils.getCampoSuplementarioFecInicioAccionSubvConcedida(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
                if (fecha == null) {
                    meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA));
                    meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                    respuesta.add(meLanbide01ValidatorResult);
                }//if(fecha == null)
            } catch (MeLanbide01Exception meEx) {
                log.error("Se ha producido un error comprobando Fecha Inicio Accion Subvencionable Concedida"
                        + " : " + meEx.getMessage(), meEx);
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA));
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_FECHA_INICIO_NULA + MeLanbide01Constantes.SUFIJO_MSG_ERROR)
                        + " : " + meEx.getMessage());
                respuesta.add(meLanbide01ValidatorResult);
            }

            //Control 3 : Fecha Fin Accion Subvencionable Concedida
            try {
                Calendar fecha = meLanbide01ValidatorUtils.getCampoSuplementarioFecFinAccionSubvConcedida(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
                if (fecha == null) {
                    meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_FECHA_FIN_NULA));
                    meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_FECHA_FIN_NULA + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                    respuesta.add(meLanbide01ValidatorResult);
                }//if(fecha == null)
            } catch (MeLanbide01Exception meEx) {
                log.error("Se ha producido un error leyendo  Fecha Fin Accion Subvencionable Concedida"
                        + " : " + meEx.getMessage(), meEx);
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_FECHA_FIN_NULA));
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_FECHA_FIN_NULA + MeLanbide01Constantes.SUFIJO_MSG_ERROR)
                        + " : " + meEx.getMessage());
                respuesta.add(meLanbide01ValidatorResult);
            }

            // Control 4: Minimo porcentaje de Reduccion persona Sustituida
            try {
                //Recuperamos el campo MeLanbide01Constantes.REDUCPERSSUST
                reducPersSust = meLanbide01ValidatorUtils.getCampoSuplementarioReducPersSust(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
                if (reducPersSust != null && !reducPersSust.isEmpty()) {
                    //Comprobamos que este campo sea mayor que el porcentaje indicado en el properties
                    Double minReducPersSust = Double.valueOf(meLanbide01ValidatorUtils.getMinReducPersSust(codigoOrganizacion, codProcedimiento));
                    Double suplReducPersSust = Double.valueOf(reducPersSust);
                    if (minReducPersSust > suplReducPersSust) {
                        meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                        meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_MIN_REDUCPERSSUST));
                        meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_MIN_REDUCPERSSUST + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                        respuesta.add(meLanbide01ValidatorResult);
                    }//if(minReducPersSust > suplReducPersSust)
                } else {
                    meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.COD_MSG_ERROR_NO_PORC_REDUC_PERS_SUST));
                    meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.COD_MSG_ERROR_NO_PORC_REDUC_PERS_SUST + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                    respuesta.add(meLanbide01ValidatorResult);
                }
            } catch (MeLanbide01Exception ex) {
                log.error("Se ha producido un error cargando el campo suplementario de la reducción de la persona sustituida: " + ex.getMessage(), ex);
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_CAMPO_REDUC_PERS_SUST));
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_CAMPO_REDUC_PERS_SUST + MeLanbide01Constantes.SUFIJO_MSG_ERROR)
                        + " : " + ex.getMessage());
                respuesta.add(meLanbide01ValidatorResult);
            }

            // Control 5: Numero de Intervalos Guardados en BD (No Calculados) periodos Datos Calculo
            try {
                DatosCalculoVO datosCalculo = MeLanbide01Manager.getInstance().getDatosCalculo(numeroExpediente, String.valueOf(codigoOrganizacion),
                        adaptador);
                if (datosCalculo.getPeriodos() != null && datosCalculo.getPeriodos().size() > 0) {
                    Integer numMaximoIntervalos = Integer.valueOf(meLanbide01ValidatorUtils.getNumMaximoIntervalosPermitidos(codigoOrganizacion, codProcedimiento));
                    if (datosCalculo.getPeriodos().size() > numMaximoIntervalos) {
                        meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                        meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_SUPERA_NUMERO_MAXIMO_PERIODOS));
                        meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_SUPERA_NUMERO_MAXIMO_PERIODOS + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                        respuesta.add(meLanbide01ValidatorResult);
                    }//if(datosCalculo.getPeriodos().size() > numMaximoIntervalos)
                }//if(daosCalculo.getPeriodos() != null && datosCalculo.getPeriodos().size()>0)
            } catch (MeLanbide01Exception ex) {
                log.error("Se ha producido un error cargando validando numero de intervalos: " + ex.getMessage(), ex);
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_SUPERA_NUMERO_MAXIMO_PERIODOS));
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_SUPERA_NUMERO_MAXIMO_PERIODOS + MeLanbide01Constantes.SUFIJO_MSG_ERROR)
                        + " : " + ex.getMessage());
                respuesta.add(meLanbide01ValidatorResult);
            }

            // Control 6: Porcentaje de subvencion : % a subvencionar resulta mayor de 100%.
            try {
                //Recuperamos el campo MeLanbide01Constantes.REDUCPERSSUST En validacion  4
                String porcJornLabPersonaSustituida = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersSust(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
                String porcJornSustitucionContratoInterinidad = meLanbide01ValidatorUtils.getCampoSuplementarioPorcJornadaSustitucionContratoInterinidad(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
                if (reducPersSust != null && !reducPersSust.isEmpty()
                        && porcJornLabPersonaSustituida != null && !porcJornLabPersonaSustituida.isEmpty()
                        && porcJornSustitucionContratoInterinidad != null && !porcJornSustitucionContratoInterinidad.isEmpty()) {
                    //POrcentaje de Subvencion >100% ?
                    String porcenajeSubvencion = meLanbide01ValidatorUtils.calcularPorcSubvenc(reducPersSust, porcJornLabPersonaSustituida, porcJornSustitucionContratoInterinidad);
                    // Quitamos la coma, devuelve string formato con separador , decimal
                    porcenajeSubvencion = (porcenajeSubvencion != null && !porcenajeSubvencion.isEmpty() ? porcenajeSubvencion.replaceAll(",", ".") : "0");
                    if ((Double.valueOf(porcenajeSubvencion)) > (new Double("100"))) {
                        meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                        meLanbide01ValidatorResult.setCodigo(38);
                        meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, 38 + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                        respuesta.add(meLanbide01ValidatorResult);
                    }
                } else {
                    log.info("Campos: porcJornLabPersonaSustituida,reducPersSust y porcJornSustitucionContratoInterinidad. NO CUMPLIMNETADOS. No se puede calcular/validar Porcentaje subvencion. " + numeroExpediente);
                }
            } catch (MeLanbide01Exception ex) {
                log.error("Se ha producido un error validando el porcentaje de subvecion. " + ex.getMessage(), ex);
                // NO retornamos error e este punto, probablemente ya se haya lanzado en la carga de datos de la pantalla.
            }
            
            // Control 7 Fecha nacimiento Persona Dependiente
            try {
                if (Melanbide01DecretoExpedienteEnum.D2010_177.getCodigoDecreto().equalsIgnoreCase(codDecretoAplica) && fechaNacCasuSubv == null) {
                    meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_FECHA_NACIMIENTO_PERSONA_DEPENDIENTE_NULA));
                    meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_FECHA_NACIMIENTO_PERSONA_DEPENDIENTE_NULA + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                    respuesta.add(meLanbide01ValidatorResult);
                }else if (Melanbide01DecretoExpedienteEnum.D2019_164.getCodigoDecreto().equalsIgnoreCase(codDecretoAplica) && (causantesSubvencion == null || causantesSubvencion.size()==0)) {
                    meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(40);
                    meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, "40" + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                    respuesta.add(meLanbide01ValidatorResult);
                }//if(fecha == null)
            } catch (Exception meEx) {
                log.error("Se ha producido un error comprobando si la fecha de nacimiento de la persona dependiente está cubierta"
                        + " : " + meEx.getMessage(), meEx);
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_FECHA_NACIMIENTO_PERSONA_DEPENDIENTE_NULA));
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_FECHA_NACIMIENTO_PERSONA_DEPENDIENTE_NULA + MeLanbide01Constantes.SUFIJO_MSG_ERROR)
                        + " : " + meEx.getMessage());
                respuesta.add(meLanbide01ValidatorResult);
            }

            // Control 8: Numero Maximo Dias
            try {
                String codAlarmaMaxDias = meLanbide01ValidatorUtils.alarmaMaximoDias(String.valueOf(codigoOrganizacion), numeroExpediente, codProcedimiento, String.valueOf(ejercicio), codDecretoAplica);
                if (codAlarmaMaxDias != null && !"".equalsIgnoreCase(codAlarmaMaxDias)) {
                    meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.MOSTRAR_ALARMA_MAX_DIAS));
                    meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.MOSTRAR_ALARMA_MAX_DIAS + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                    respuesta.add(meLanbide01ValidatorResult);
                }//if(codAlarmaMaxDias != null && !"".equalsIgnoreCase(codAlarmaMaxDias))
            } catch (Exception ex) {
                log.error("Se ha producido un error validando numero Maximo de Dias: " + ex.getMessage(), ex);
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_CALCULANDO_ALARMA_MAX_DIAS));
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_CALCULANDO_ALARMA_MAX_DIAS + MeLanbide01Constantes.SUFIJO_MSG_ERROR)
                        + " : " + ex.getMessage());
                respuesta.add(meLanbide01ValidatorResult);
            }
            
            // Control 9: Duracion Maxima Ayuda : Nuevos Hijos
            List<String> resultadoValidacion = new ArrayList<String>();
            List<String> fechaNacCausExpteBase = new ArrayList<String>();
            // Leemos la lista Dependientes de el Expdientes consultado y preparamos la lista para comparar
            if (causantesSubvencion != null && causantesSubvencion.size() > 0) {
                Collections.sort(causantesSubvencion, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        Melanbide01DepenPerSut _o1 = (Melanbide01DepenPerSut) o1;
                        Melanbide01DepenPerSut _o2 = (Melanbide01DepenPerSut) o2;
                        return (_o1.getFechaNacimiento() != null ? _o1.getFechaNacimiento().compareTo(_o2.getFechaNacimiento()) : 0);
                    }
                });
                for (Melanbide01DepenPerSut dependiente : causantesSubvencion) {
                    if (dependiente.getFechaNacimiento() != null) {
                        fechaNacCausExpteBase.add(formatFecha_dd_MM_yyyy.format(dependiente.getFechaNacimiento().getTime()));
                    }
                }
            }
            // Expedientes Releacionados con causante ordenados por fecha, no icluye el expediente tratado
            List<Map<String, String>> expedientesRelacionados = MeLanbide01Manager.getInstance().getExpedientesRelacionados(numeroExpediente, adaptador);
            if (expedientesRelacionados != null && expedientesRelacionados.size() > 0) {
                for (Map<String, String> expedienteRelacionado : expedientesRelacionados) {
                    List<String> fechaNacCausExpteRela = new ArrayList<String>();
                    if(expedienteRelacionado.get("listMECausantes") != null && !expedienteRelacionado.get("listMECausantes").isEmpty())
                        fechaNacCausExpteRela=new ArrayList(Arrays.asList(expedienteRelacionado.get("listMECausantes").split(";")));
                    if (Melanbide01DecretoExpedienteEnum.D2019_164.getCodigoDecreto().equalsIgnoreCase(codDecretoAplica)) {
                        // Leemos la lista Dependientes de el Expdientes consultado
                        if (fechaNacCausExpteBase.size() > 0) {
                            if ("1".equalsIgnoreCase(expedienteRelacionado.get("mismoDecretoExpteConsultado"))) {
                                if (fechaNacCausExpteBase.size() == fechaNacCausExpteRela.size()) {
                                    String validacionDetalle = "";
                                    for (String fechaNac : fechaNacCausExpteRela) {
                                        if (!fechaNacCausExpteBase.contains(fechaNac)) {
                                            validacionDetalle += " No coincide " + fechaNac + ". ";
                                        }
                                    }
                                    if (!validacionDetalle.isEmpty()) {
                                        resultadoValidacion.add("[" + expedienteRelacionado.get("numExpediente") + " : "
                                                + validacionDetalle + "]");
                                    }
                                } else {
                                    log.debug("El expediente relacionado " + expedienteRelacionado.get("numExpediente")
                                            + " No tiene el mismo numero de hijos " + fechaNacCausExpteRela.size()
                                            + " que el expediente consultado: " + fechaNacCausExpteBase.size());
                                    resultadoValidacion.add("[" + expedienteRelacionado.get("numExpediente") + " : "
                                            + "No tiene el mismo numero de hijos: " + fechaNacCausExpteRela.size() + "]");
                                }
                            } else if (expedienteRelacionado.get("decretoCodigo") != null && !expedienteRelacionado.get("decretoCodigo").isEmpty()) {
                                if (!(fechaNacCausExpteBase.size() == 1 && fechaNacCausExpteBase.contains(expedienteRelacionado.get("datosCSCausante")))) {
                                    resultadoValidacion.add("[" + expedienteRelacionado.get("numExpediente") + " : "
                                            + "No coinciden " + expedienteRelacionado.get("datosCSCausante") + "]"
                                    );
                                }
                            } else {
                                resultadoValidacion.add("[" + expedienteRelacionado.get("numExpediente") + " : "
                                        + "Sin Decreto aplicable." + "]");
                            }
                        } else {
                            log.debug("Duracion Maxima Ayuda : Nuevos Hijos - Validacion No Disponible: No se han dado de alta Personas causantes de la subvencion...");
                        }
                    } else if (Melanbide01DecretoExpedienteEnum.D2010_177.getCodigoDecreto().equalsIgnoreCase(codDecretoAplica)) {
                        if(fechaNacCasuSubv!=null){
                            if ("1".equalsIgnoreCase(expedienteRelacionado.get("mismoDecretoExpteConsultado"))) {
                                if(!formatFecha_dd_MM_yyyy.format(fechaNacCasuSubv.getTime()).equalsIgnoreCase(expedienteRelacionado.get("datosCSCausante"))){
                                    resultadoValidacion.add("[" + expedienteRelacionado.get("numExpediente") + " : "
                                            + "No coinciden " + expedienteRelacionado.get("datosCSCausante") + " : "+formatFecha_dd_MM_yyyy.format(fechaNacCasuSubv.getTime()) + "]"
                                    );
                                }
                            }else{
                                if (!(fechaNacCausExpteRela.size() == 1 && fechaNacCausExpteRela.contains(formatFecha_dd_MM_yyyy.format(fechaNacCasuSubv.getTime())))) {
                                    resultadoValidacion.add("[" + expedienteRelacionado.get("numExpediente") + " : "
                                            + "No coinciden " + expedienteRelacionado.get("datosCSCausante") + " : " +formatFecha_dd_MM_yyyy.format(fechaNacCasuSubv.getTime())+ "]"
                                    );
                                }
                            }
                        }
                    } else {
                        log.info("Codigo decreto a aplicar no recibido en Validaciones Generales - No valida Nuevos Hijos. " + numeroExpediente);
                    }
                }
                if (resultadoValidacion.size() > 0) {
                    meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(39);
                    meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, "39" + MeLanbide01Constantes.SUFIJO_MSG_ERROR)
                            + " : " + Arrays.toString(resultadoValidacion.toArray())
                    );
                    respuesta.add(meLanbide01ValidatorResult);
                }
            } else {
                log.debug("No se han recuperado Expedientes relacionados - No hay Validacion nuevos Hijos : " + numeroExpediente);
            }
            
            // Control 10 Regla Minimis EU (Limite maximo ayuda en los ultimos 3 anios para la empresa)
            double impTotalRecibidoEmpreReglaMinimis = 0;
            impTotalRecibidoEmpreReglaMinimis = MeLanbide01Manager.getInstance().getImporteTotalSubvencionEmpresaReglaMinimisUltimos3Anios(codigoOrganizacion, codProcedimiento, numeroExpediente);
            log.info("limiteReglaMinimis: " + impTotalRecibidoEmpreReglaMinimis);
            String limiteReglaMinimis = ConfigurationParameter.getParameter(codigoOrganizacion + "/REGLA_MINIMIS_MAXIMO_IMPORTE", MeLanbide01Constantes.FICHERO_CONFIGURACION);
            limiteReglaMinimis = (limiteReglaMinimis != null && !limiteReglaMinimis.isEmpty() ? limiteReglaMinimis : "0");
            log.info("limiteReglaMinimis: " + limiteReglaMinimis);
            if (!(impTotalRecibidoEmpreReglaMinimis <= Double.valueOf(limiteReglaMinimis))) {
                log.info("Empresa no cumple reglamento Minimis UE : Limite : " + limiteReglaMinimis + " Importes recibidos Ult. 3 Anios: " + impTotalRecibidoEmpreReglaMinimis);
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(41);
                meLanbide01ValidatorResult.setDescripcion(MeLanbide01I18n.getInstance().getMensaje(idioma, "41" + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                respuesta.add(meLanbide01ValidatorResult);
            }

        } catch (Exception e) {
            log.error("Error en el proceso de validacion y generacion de alarmas expedientes - Validaciones Generales - CONCM : " + e.getMessage(), e);
            meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
            meLanbide01ValidatorResult.setCodigo(-997);
            meLanbide01ValidatorResult.setDescripcion("Error no controlado al ejecutar validaciones comunes en el expediente y obtener alarmas : " + e.getMessage());
            respuesta.add(meLanbide01ValidatorResult);
        }
                
        return respuesta;
    }
    
    private List<MeLanbide01ValidatorResult> validacionAlarmasCONCMdecreto2010_177(Integer codigoOrganizacion,String numeroExpediente,Integer idioma) {
        List<MeLanbide01ValidatorResult> respuesta = new ArrayList<MeLanbide01ValidatorResult>();
        MeLanbide01ValidatorResult meLanbide01ValidatorResult = null;
        try {
            Integer ejercicio = Utilities.getEjercicioFromNumExpediente(numeroExpediente);
            String codProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numeroExpediente);
            // Control 1 Maximo Anios
            try {
                String codAlarmaMaxAnhos = meLanbide01ValidatorUtils.alarmaMaxAnosDependencia(String.valueOf(codigoOrganizacion), numeroExpediente, codProcedimiento, String.valueOf(ejercicio));
                if (codAlarmaMaxAnhos != null && !"".equalsIgnoreCase(codAlarmaMaxAnhos)) {
                    if ("1".equals(codAlarmaMaxAnhos)) {
                        // ERROR => La fecha de actuación concedida es anterior a la fecha de nacimiento de la 
                        // persona dependiente
                        meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                        meLanbide01ValidatorResult.setCodigo(29);
                        meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, "29" + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                        respuesta.add(meLanbide01ValidatorResult);
                    } else if ("2".equals(codAlarmaMaxAnhos)) {
                        // ERROR => Hay alarma porque la resta entre la fecha de actuación concedida y
                        // la fecha de nacimiento de la persona dependiente, excede el límite establecido
                        // para la actuación subvencionable del expediente
                        meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                        meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.MOSTRAR_ALARMA_MAX_ANHOS));
                        meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.MOSTRAR_ALARMA_MAX_ANHOS + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                        respuesta.add(meLanbide01ValidatorResult);
                    }
                }
            } catch (MeLanbide01Exception ex) {
                log.error("Se ha producido un error comprobando si hay que mostrar la advertencia del numero maximo de anhos " + ex.getMessage(), ex);
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(Integer.valueOf(MeLanbide01Constantes.ERROR_CALCULANDO_ALARMA_MAX_ANHOS));
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, MeLanbide01Constantes.ERROR_CALCULANDO_ALARMA_MAX_ANHOS + MeLanbide01Constantes.SUFIJO_MSG_ERROR)
                        + " : " + ex.getMessage());
                respuesta.add(meLanbide01ValidatorResult);
            }//try-catch
             
        } catch (Exception e) {
            log.error("Error en el proceso de validacion y generacion de alarmas expedientes CONCM : " + e.getMessage(), e);
            meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
            meLanbide01ValidatorResult.setCodigo(-997);
            meLanbide01ValidatorResult.setDescripcion("Error no controlado al validar el expediente y obtener alarmas : " + e.getMessage());
            respuesta.add(meLanbide01ValidatorResult);
        }
        return respuesta;
    }

    private List<MeLanbide01ValidatorResult> validacionAlarmasCONCMdecreto2019_164(Integer codigoOrganizacion,String numeroExpediente,Integer idioma) {
        List<MeLanbide01ValidatorResult> respuesta = new ArrayList<MeLanbide01ValidatorResult>();
        MeLanbide01ValidatorResult meLanbide01ValidatorResult = null;
        try {
            // Preparamos el Adaptador para conectar a BD
            AdaptadorSQLBD adaptador = ConnectionUtils.getAdaptSQLBD(String.valueOf(codigoOrganizacion));
            Integer ejercicio = Utilities.getEjercicioFromNumExpediente(numeroExpediente);
            String codProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numeroExpediente);
            String codDecretoAplica = Melanbide01DecretoExpedienteEnum.D2019_164.getCodigoDecreto();
            // Control 1 Contrato Interinidad : Duracion continuada
            String numeroMinimoDiasContratoInterinidad = ConfigurationParameter.getParameter(String.valueOf(codigoOrganizacion) + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.CONTRATO_INTERINIDAD_NUMERO_MINIMO_DIAS,MeLanbide01Constantes.FICHERO_CONFIGURACION);
            int numeroDiasRestantes=0;
            Calendar fechaInicio=null;
            Calendar fechaFin=null;
            // Recuperamos fecha inicio accion concedida y numero dias restantes
            // Podra ser menos de 59 si son solo los dias restantes de la subvencion
            fechaInicio=meLanbide01ValidatorUtils.getCampoSuplementarioFecInicioAccionSubvConcedida(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
            fechaFin=meLanbide01ValidatorUtils.getCampoSuplementarioFecFinAccionSubvConcedida(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
            numeroDiasRestantes=meLanbide01ValidatorUtils.diasRestantesSubvencionables(String.valueOf(codigoOrganizacion),numeroExpediente, codProcedimiento, String.valueOf(ejercicio),codDecretoAplica);
            // Calculamos los dias 
            log.info("Fechas Inicio/Fin Accion Concedida = " 
                    + (fechaInicio != null ? formatFecha_dd_MM_yyyy.format(fechaInicio.getTime()): "")
                    + " ** " + (fechaFin != null ? formatFecha_dd_MM_yyyy.format(fechaFin.getTime()): "")
            );
            if(fechaInicio!=null && fechaFin!=null){
                 Integer numeroDiasContrato = meLanbide01ValidatorUtils.getNumeroDiasEntreDosFecha(fechaInicio,fechaFin);
                 Integer numeroMinimoDiasContrato = numeroMinimoDiasContratoInterinidad!=null ? Integer.valueOf(numeroMinimoDiasContratoInterinidad) : 0;
                 log.info("numeroDiasContrato: " + numeroDiasContrato
                         + " numeroMinimoDiasContrato: " + numeroMinimoDiasContrato                 
                 );
                 if(numeroMinimoDiasContrato >  numeroDiasContrato 
                         && numeroDiasRestantes > numeroDiasContrato){
                     meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                     meLanbide01ValidatorResult.setCodigo(32);
                     meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma,"32"+MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                     respuesta.add(meLanbide01ValidatorResult);
                 }
            }else{
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(31);
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma,"31"+MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                respuesta.add(meLanbide01ValidatorResult);
            }
            
            // Control 2 Contrato Interinidad : Jornada de Suscripcion
            String actividadSubvencionada=null;
            String porcJornLabPersonaSustituida=null;
            String porcReduccionJornLabPersonaSustituida=null;
            String porcJornLabContraContratoInterinidad=null;
            
            porcJornLabContraContratoInterinidad=meLanbide01ValidatorUtils.getCampoSuplementarioPorcJornLabContratadaContratoInterinidad(codigoOrganizacion,String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
            
            if(porcJornLabContraContratoInterinidad!=null && !porcJornLabContraContratoInterinidad.isEmpty()){
                actividadSubvencionada = meLanbide01ValidatorUtils.getCampoSuplementarioActividadSubvencionada(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
                if ("REDUCC".equalsIgnoreCase(actividadSubvencionada)){
                    porcReduccionJornLabPersonaSustituida = meLanbide01ValidatorUtils.getCampoSuplementarioReducPersSust(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
                    if(porcReduccionJornLabPersonaSustituida!=null && !porcReduccionJornLabPersonaSustituida.isEmpty()){
                        if (!(Double.valueOf(porcJornLabContraContratoInterinidad) >= Double.valueOf(porcReduccionJornLabPersonaSustituida))) {
                            meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                            meLanbide01ValidatorResult.setCodigo(34);
                            meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, "34" + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                            respuesta.add(meLanbide01ValidatorResult);
                        }
                    }
                } else if ("EXCED".equalsIgnoreCase(actividadSubvencionada)) {
                    porcJornLabPersonaSustituida = meLanbide01ValidatorUtils.getCampoSuplementarioJornPersSust(codigoOrganizacion, String.valueOf(ejercicio), numeroExpediente, codProcedimiento);
                    if(porcJornLabPersonaSustituida!=null && !porcJornLabPersonaSustituida.isEmpty()){
                        if(!(Double.valueOf(porcJornLabContraContratoInterinidad)>=Double.valueOf(porcJornLabPersonaSustituida))){
                            meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                            meLanbide01ValidatorResult.setCodigo(34);
                            meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, "34" + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                            respuesta.add(meLanbide01ValidatorResult);
                        }
                    }
                }
            }else{
                meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                meLanbide01ValidatorResult.setCodigo(33);
                meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, "33" + MeLanbide01Constantes.SUFIJO_MSG_ERROR));
                respuesta.add(meLanbide01ValidatorResult);
            }
            // Control 3 :  Control Subvención según Jornada Laboral Sustitución - Numero personas sustituidas por persona contratada.
            Map<String,String> datosDocsRolesExpte = MeLanbide01Manager.getInstance().getDatosDocumentoRolExpediente(numeroExpediente, adaptador);
            if(datosDocsRolesExpte!=null && datosDocsRolesExpte.size()>0){
                log.debug("Documentos Roles: " + datosDocsRolesExpte.toString());
                List<String> expedientePConSustituyeDifPerMisEmp = MeLanbide01Manager.getInstance().getExptsMismaEmpresaPerContratadaDifePerSust(numeroExpediente, datosDocsRolesExpte, adaptador); 
                if(expedientePConSustituyeDifPerMisEmp!=null && expedientePConSustituyeDifPerMisEmp.size()>0){
                    meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
                    meLanbide01ValidatorResult.setCodigo(37);
                    meLanbide01ValidatorResult.setDescripcion(meLanbide01I18n.getMensaje(idioma, "37" + MeLanbide01Constantes.SUFIJO_MSG_ERROR) 
                                                                + " : " + Arrays.toString(expedientePConSustituyeDifPerMisEmp.toArray())
                                                            );
                    respuesta.add(meLanbide01ValidatorResult);
                }
            }else{
                log.error("Validacion No Disponible: P.Contratada Susutituye mas de una persona para la misma Empresa. Documentos de roles Empresa,PContratada y PSustituida no recuperados.");
            }
            
        } catch (Exception e) {
            log.error("Error en el proceso de validacion y generacion de alarmas expedientes CONCM : " + e.getMessage(), e);
            meLanbide01ValidatorResult = new MeLanbide01ValidatorResult();
            meLanbide01ValidatorResult.setCodigo(-997);
            meLanbide01ValidatorResult.setDescripcion("Error no controlado al validar el expediente y obtener alarmas : " + e.getMessage());
            respuesta.add(meLanbide01ValidatorResult);
        }
        return respuesta;
    }
}
