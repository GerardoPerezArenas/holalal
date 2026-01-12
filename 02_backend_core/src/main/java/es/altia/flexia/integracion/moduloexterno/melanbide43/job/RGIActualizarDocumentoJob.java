package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.RGI_JobDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.DatosAvisoCSRegexlan;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpActualizarDocumentoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.TerceroModuloIntegracionVO;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Expediente;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Interesado;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Participacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.servicios.Lan6InformarConsultaServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author kepa.gonzalez
 */
public class RGIActualizarDocumentoJob implements Job {

    private final Logger log = LogManager.getLogger(RGIActualizarDocumentoJob.class);
    GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
    Gson gson = gsonB.serializeNulls().create();

    /**
     *
     * @param jec
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    Connection con = null;
                    AdaptadorSQLBD adaptador = null;
                    Connection conTransaccion = null;
                    String numExpediente = "";

                    try {
                        long inicio = System.currentTimeMillis();
                        log.info("=====================> RGIActualizarDocumentoJob() - Tiempo inicio " + inicio);
                        int tratados = 0;
                        int correctos = 0;
                        int historicos = 0;
                        RGI_JobDAO rgiJobDAO = RGI_JobDAO.getInstance();
                        MeLanbide43BDUtil m43BDUtil = new MeLanbide43BDUtil();
                        List<ExpActualizarDocumentoVO> expedientes = null;
                        log.info(" ======================================= RGIActualizarDocumentoJob() -  Execute lanzado " + System.getProperty("weblogic.Name") + "  =======================================  ");

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        while (codOrg < 2) {
                            adaptador = m43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();

                            // DESA
                            /*      expedientes = new ArrayList<ExpActualizarDocumentoVO>();
                            ExpActualizarDocumentoVO expedienteTruqi = new ExpActualizarDocumentoVO();
                            expedienteTruqi.setNumExp("2024/RGI/000015");
                            expedienteTruqi.setNombre("ALEJANDRA MARIA");
                            expedienteTruqi.setApellido1("GOMEZ");
                            expedienteTruqi.setApellido2("VILLADA");
                            expedienteTruqi.setNumDocRegex("10000068J");
                            expedienteTruqi.setTipoDocRegex("D");
                            expedienteTruqi.setNumDocRGI("Y5088847L");
                            expedienteTruqi.setTipoDocRGI("E");
                            expedientes.add(expedienteTruqi);
                            
                            expedienteTruqi = new ExpActualizarDocumentoVO();
                            expedienteTruqi.setNumExp("2024/RGI/000016");
                            expedienteTruqi.setNombre("GERARDINA");
                            expedienteTruqi.setApellido1("ROSARIO OZORIA");
                            expedienteTruqi.setNumDocRegex("10000068J");
                            expedienteTruqi.setTipoDocRegex("D");
                            expedienteTruqi.setNumDocRGI("Z0112480S");
                            expedienteTruqi.setTipoDocRGI("E");
                            expedientes.add(expedienteTruqi);
                             */
                            // PRE y PRO
                            expedientes = rgiJobDAO.getExpedientesDistintoDoc(con);

                            //  proceso
                            log.info(" =======> RGIActualizarDocumentoJob() - Hay " + expedientes.size() + " expediente(s)");
                            //Operaciones en Regexlan las hacemos transaccionales para cada expedientes.
                            conTransaccion = adaptador.getConnection();
                            if (!expedientes.isEmpty()) {
                                String cambioRegex;
                                String cambioSede;
                                String observacionesJob = "";
                                ArrayList<String> expedientesActualizados = rgiJobDAO.getExpedientesActualizadosOK(con);
                                for (ExpActualizarDocumentoVO expediente : expedientes) {
                                    cambioRegex = "OK";
                                    cambioSede = "KO";
                                    observacionesJob = "";
                                    numExpediente = expediente.getNumExp();
                                    log.info(" =======> RGIActualizarDocumentoJob() - Expediente " + numExpediente);
                                    // comprobamos que el expediente no ha sido tratado previamente
                                    boolean estaActualizado = false;

                                    if (!expedientesActualizados.isEmpty()) {
                                        for (String expActualizado : expedientesActualizados) {
                                            if (expActualizado.equalsIgnoreCase(numExpediente)) {
                                                estaActualizado = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (!estaActualizado) {
                                        tratados++;
                                        // compruebo si el expediente esta Activo o es Historico
                                        boolean esActivo = true;
                                        boolean seguir = true;
                                        boolean registrar = true;
                                        if (!rgiJobDAO.expedienteActivo(numExpediente, con)) {
                                            if (rgiJobDAO.expedienteHistorico(numExpediente, con)) {
                                                esActivo = false;
                                                log.info("====> HISTÓRICO");
                                                // hasta que no solucione todos los problemas con los historicos solo se van a tratar activos
                                                historicos++;

                                            } else {
                                                seguir = false;
                                                cambioRegex = "KO";
                                                observacionesJob = "";
                                            }
                                        } else {
                                            log.info("====> ACTIVO");
                                        }

                                        if (seguir) {
                                            // busco si existe el tercero con el  nuevo documento
                                            TerceroModuloIntegracionVO ter = new TerceroModuloIntegracionVO();
                                            int codTercero = -1;
                                            int version = 1;
                                            int nuevoCodTercero = -1;
                                            int codTerceroSede = -1;
                                            int tipoDocFlexia = -1;
                                            int tipoDocRGI = -1;
                                            String codDomicilio = null;

                                            if (expediente.getTipoDocRegex().equalsIgnoreCase("D")) {
                                                tipoDocFlexia = 1;
                                            } else if (expediente.getTipoDocRegex().equalsIgnoreCase("E")) {
                                                tipoDocFlexia = 3;
                                            } else if (expediente.getTipoDocRegex().equalsIgnoreCase("P")) {
                                                tipoDocFlexia = 2;
                                            } else if (expediente.getTipoDocRegex().equalsIgnoreCase("W")) {
                                                tipoDocFlexia = 8;
                                            } else {
                                                log.error(" =======> RGIActualizarDocumentoJob() - Tipo doc incorrecto en REGEXLAN para iniciar expediente en Sede");
                                                seguir = false;
                                                cambioRegex = "KO";
                                                observacionesJob = "Tipo doc incorrecto en REGEXLAN para iniciar expediente en Sede.";

                                            }

                                            if (expediente.getTipoDocRGI().equalsIgnoreCase("D")) {
                                                tipoDocRGI = 1;
                                            } else if (expediente.getTipoDocRGI().equalsIgnoreCase("E")) {
                                                tipoDocRGI = 3;
                                            }

                                            codTercero = rgiJobDAO.getCodTercero(expediente.getNumDocRGI(), con);
                                            if (codTercero > 0) {
                                                //existe
                                                log.info(" =======> RGIActualizarDocumentoJob() - Existe el tercero: " + codTercero + " con mismo doc " + expediente.getNumDocRGI());
                                                // comprobar si está en el expediente
                                                if (rgiJobDAO.esInteresado(numExpediente, codTercero, esActivo, con)) {
                                                    //nada
                                                    log.info(" =======> RGIActualizarDocumentoJob() - El tercero ya está como Interesado en el expediente ");
                                                    observacionesJob = "El tercero ya está como Interesado en el expediente. ";
                                                    registrar = false;
                                                } else {
                                                    version = rgiJobDAO.getMaxVersionTercero(codTercero, con);
                                                    codDomicilio = rgiJobDAO.getDomicilioTercero(codTercero, con);
                                                    if (codDomicilio == null || codDomicilio.isEmpty() || codDomicilio.equalsIgnoreCase(" ")) {
                                                        // si el tercero no tiene domicilio  cojo el del interesado del expediente
                                                        codDomicilio = rgiJobDAO.getDomicilioInteresadoExpediente(numExpediente, esActivo, con);
                                                    }
                                                    if (rgiJobDAO.actualizarInteresadoExpediente(codOrg, numExpediente, codTercero, version, codDomicilio, esActivo, con)) {
                                                        log.info(" =======> RGIActualizarDocumentoJob() - añadido el tercero como Interesado en E_EXT ");
                                                    } else {
                                                        log.error(" =======> RGIActualizarDocumentoJob() - Error al añadir el tercero como Interesado en E_EXT");
                                                        seguir = false;
                                                    }

                                                    /*  if (esActivo) {
                                                        // alta tercero en E_EXT
                                                        if (rgiJobDAO.agregarInteresdoExpediente(codOrg, numExpediente, codTercero, version, codDomicilio, con)) {
                                                            log.info(" =======> RGIActualizarDocumentoJob() - añadido el tercero como Interesado en E_EXT ");
                                                        } else {
                                                            log.error(" =======> RGIActualizarDocumentoJob() - Error al añadir el tercero como Interesado en E_EXT");
                                                            seguir = false;
                                                        }
                                                    } else {
                                                        // alta tercero en HIST_E_EXT
                                                        if (rgiJobDAO.agregarInteresdoExpedienteHist(codOrg, numExpediente, codTercero, version, codDomicilio, con)) {
                                                            log.info(" =======> RGIActualizarDocumentoJob() - añadido el tercero como Interesado en HIST_E_EXT ");
                                                        } else {
                                                            log.error(" =======> RGIActualizarDocumentoJob() - Error al añadir el tercero como Interesado en HIST_E_EXT");
                                                            seguir = false;
                                                        }
                                                    }*/
                                                    if (seguir) {
                                                        observacionesJob = "Se ha modificado el documento del interesado del expediente de " + expediente.getNumDocRegex() + " por el " + expediente.getNumDocRGI() + ". Añadido con el nuevo documento como Interesado en el expediente. ";
                                                    } else {
                                                        observacionesJob = "Error al añadir el tercero como Interesado";
                                                    }
                                                }
                                                if (seguir) {
                                                    codTerceroSede = codTercero;
                                                } else {
                                                    cambioRegex = "KO";
                                                }
                                            } else {
                                                /* no existe
                                                    Dar de baja en T_TER
                                                    Dar de alta a la persona con el nuevo documento en 
                                                     T_TER,
                                                     T_DOT,
                                                     T_HTE 
                                                 */
                                                log.info(" =======> RGIActualizarDocumentoJob() - No existe el tercero con el nº documento de RGI");
                                                codTercero = rgiJobDAO.getCodTercero(expediente.getNumDocRegex(), con);
                                                codDomicilio = rgiJobDAO.getDomicilioInteresadoExpediente(numExpediente, esActivo, con);

                                                ter.setCodTercero(String.valueOf(codTercero));
                                                if (expediente.getTipoDocRGI().equalsIgnoreCase("D")) {
                                                    ter.setTipoDocumentoTercero("1");
                                                } else if (expediente.getTipoDocRGI().equalsIgnoreCase("E")) {
                                                    ter.setTipoDocumentoTercero("3");
                                                } else {
                                                    log.error(" =======> RGIActualizarDocumentoJob() - Tipo doc incorrecto");
                                                    observacionesJob = "Tipo documento incorrecto";
                                                    seguir = false;
                                                }
                                                if (seguir) {
                                                    ter.setDocumentoTercero(expediente.getNumDocRGI());
                                                    ter.setNombreTercero(expediente.getNombre());
                                                    ter.setApellido1Tercero(expediente.getApellido1());
                                                    ter.setApellido2Tercero(expediente.getApellido2() != null ? expediente.getApellido2() : null);
                                                    ter.setNombreCompleto(ter.getApellido1Tercero() + (ter.getApellido2Tercero() != null ? " " + ter.getApellido2Tercero() : "") + ", " + ter.getNombreTercero());
                                                    ter.setDomPrincipal(codDomicilio);
                                                    ter.setUsuarioAlta("5");
                                                    ter.setModuloAlta("4");
                                                    ter.setVersionTercero("1");

                                                    try {
                                                        adaptador.inicioTransaccion(conTransaccion);
                                                        nuevoCodTercero = rgiJobDAO.altaTercero(ter, conTransaccion);
                                                        if (nuevoCodTercero != -1) {
                                                            log.info(" =======> RGIActualizarDocumentoJob() - Alta del nuevo tercero " + nuevoCodTercero + " correcta. Procedemos a hacer commit de los cambios en Regexlan.");
                                                            adaptador.finTransaccion(conTransaccion);
                                                        } else {
                                                            log.error("No se ha podido dar de alta al tercero con el nuevo documento");
                                                            adaptador.rollBack(conTransaccion);
                                                            seguir = false;
                                                            observacionesJob = "Error  al dar de alta al tercero con el nuevo documento.";
                                                        }
                                                    } catch (BDException e) {
                                                        log.error("ERROR al dar de alta al tercero con el nuevo documento. ", e);
                                                        adaptador.rollBack(conTransaccion);
                                                        seguir = false;
                                                        observacionesJob = "EXCEPCIÓN al dar de alta al tercero con el nuevo documento. ";
                                                    }
                                                    if (seguir) {
                                                        if (rgiJobDAO.actualizarInteresadoExpediente(codOrg, numExpediente, codTercero, version, codDomicilio, esActivo, con)) {
                                                            log.info(" =======> RGIActualizarDocumentoJob() - añadido el tercero como Interesado en E_EXT ");
                                                        } else {
                                                            log.error(" =======> RGIActualizarDocumentoJob() - Error al añadir el tercero como Interesado en E_EXT");
                                                            seguir = false;
                                                        } // seguir - alta correcta
                                                        
                                                        /* 
                                                        if (esActivo) {
                                                            // alta tercero en E_EXT
                                                            if (rgiJobDAO.agregarInteresdoExpediente(codOrg, numExpediente, nuevoCodTercero, 1, codDomicilio, con)) {
                                                                log.info(" =======> RGIActualizarDocumentoJob() - añadido el tercero como Interesado en E_EXT ");
                                                            } else {
                                                                log.error(" =======> RGIActualizarDocumentoJob() - Error al añadir el tercero como Interesado en E_EXT");
                                                                seguir = false;
                                                                observacionesJob = "Error al añadir el tercero como Interesado en E_EXT. ";
                                                            }
                                                        } else {
                                                            // alta tercero en HIST_E_EXT
                                                            if (rgiJobDAO.agregarInteresdoExpedienteHist(codOrg, numExpediente, nuevoCodTercero, 1, codDomicilio, con)) {
                                                                log.info(" =======> RGIActualizarDocumentoJob() - añadido el tercero como Interesado en HIST_E_EXT ");
                                                            } else {
                                                                log.error(" =======> RGIActualizarDocumentoJob() - Error al añadir el tercero como Interesado en HIST_E_EXT");
                                                                seguir = false;
                                                                observacionesJob = "Error al añadir el tercero como Interesado en HIST_E_EXT. ";
                                                            }
                                                        }*/
                                                    } // seguir - alta correcta
                                                } // seguir - tipo doc correcto

                                                if (seguir) {
                                                    codTerceroSede = nuevoCodTercero;
                                                    observacionesJob = " Se ha modificado el documento del interesado del expediente de " + expediente.getNumDocRegex() + " por el " + expediente.getNumDocRGI() + ". Alta del nuevo tercero " + nuevoCodTercero + " correcta. - Añadido el tercero como Interesado en el expediente.";
                                                } else {
                                                    cambioRegex = "KO";
                                                }
                                            } //existe / no existe tercero
                                            // grabo OK/KO
                                            if (registrar) {
                                                int grabaResultadoRegex = rgiJobDAO.insertarLineasLogCambioDoc(numExpediente, observacionesJob, cambioRegex, con);
                                            }
                                            if (seguir) {

                                                if (registrar) {
                                                    // registrar en historico operaciones y en observaciones
                                                    String[] datosExp = numExpediente.split(ConstantesMeLanbide43.BARRA_SEPARADORA);

                                                    try {
                                                        //operacion expediente
                                                        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
                                                        String fechaOper = null;
                                                        operacion.setCodMunicipio(codOrg);
                                                        operacion.setEjercicio(Integer.parseInt(datosExp[0]));
                                                        operacion.setNumExpediente(numExpediente);
                                                        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_MODIFICAR_INTERESADO);
                                                        operacion.setFechaOperacion(new GregorianCalendar());
                                                        operacion.setCodUsuario(5);
                                                        try {
                                                            fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()))
                                                                    + " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
                                                        } catch (Exception e) {
                                                            log.error("Ha ocurrido un error al convertir la fecha de la operacion a String. ", e);
                                                        }
                                                        // CREAR DESCRIPCION
                                                        log.info("Creo el XML de la operacion");

                                                        String descripcion = null;
                                                        StringBuilder textoXml = new StringBuilder("");
                                                        textoXml.append("<div class=\"movExpC1\">{eMovExpIntModificar}</div>");
                                                        textoXml.append("<div class=\"movExpC2\">{eMovExpIntCambDatPer}</div>");
                                                        // datos anteriores
                                                        textoXml.append("<div class=\"movExpC3\">{eMovExpIntCambDatPerA}</div>");
                                                        textoXml.append("<div class=\"movExpLin\">"
                                                                + "<div class=\"movExpEtiq\">{eMovExpIntTipoDocA}:</div>"
                                                                + "<div class=\"movExpVal\">").append(tipoDocFlexia).append("</div>" // tipo doc 
                                                                + "</div>");
                                                        textoXml.append("<div class=\"movExpLin\">"
                                                                + "<div class=\"movExpEtiq\">{eMovExpIntDocA}:</div>"
                                                                + "<div class=\"movExpVal\">").append(expediente.getNumDocRegex()).append("</div>" // num doc
                                                                + "</div>");
                                                        // nuevos datos
                                                        textoXml.append("<div class=\"movExpC3\">{eMovExpIntCambDatPerN}</div>");
                                                        textoXml.append("<div class=\"movExpLin\">"
                                                                + "<div class=\"movExpEtiq\">{eMovExpIntTipoDocN}:</div>"
                                                                + "<div class=\"movExpVal\">").append(tipoDocRGI).append("</div>" // tipo doc 
                                                                + "</div>");
                                                        textoXml.append("<div class=\"movExpLin\">"
                                                                + "<div class=\"movExpEtiq\">{eMovExpIntDocN}:</div>"
                                                                + "<div class=\"movExpVal\">").append(expediente.getNumDocRGI()).append("</div>" // num doc
                                                                + "</div>");
                                                        textoXml.append("<div class=\"movExpC2\">{eMovExpUsuFec}</div>");
                                                        textoXml.append("<div class=\"movExpLin\">"
                                                                + "<div class=\"movExpEtiq\">{eMovExpUsuario}:</div>"
                                                                + "<div class=\"movExpVal\">").append(operacion.getCodUsuario()).append("</div>"
                                                                + "</div>");
                                                        textoXml.append("<div class=\"movExpLin\">"
                                                                + "<div class=\"movExpEtiq\">{eMovExpNomUsuario}:</div>"
                                                                + "<div class=\"movExpVal\">ADMINISTRADOR</div>"
                                                                + "</div>");
                                                        textoXml.append("<div class=\"movExpLin\">"
                                                                + "<div class=\"movExpEtiq\">{gEtiqFecOpe}:</div>"
                                                                + "<div class=\"movExpVal\">").append(fechaOper).append("</div>"
                                                                + "</div>");
                                                        descripcion = textoXml.toString();
                                                        operacion.setDescripcionOperacion(descripcion);

                                                        try {
                                                            if (rgiJobDAO.insertarHistoricoExp(operacion, esActivo, con)) {
                                                                log.info(" =======> RGIActualizarDocumentoJob() -  Registrada la operación Actualizar documento Interesado");
                                                            } else {
                                                                log.error(" =======> RGIActualizarDocumentoJob() -  Ha ocurrido un error al grabar la operacion en Historico Operaciones. ");
                                                            }
                                                        } catch (TechnicalException ex) {
                                                            log.error(" =======> RGIActualizarDocumentoJob() -  Ha ocurrido un error al grabar la operacion en Historico Operaciones. ", ex);
                                                        }

                                                        // grabar en observaciones
                                                        String observacionesExp = " Se ha modificado el documento del interesado del expediente de " + expediente.getNumDocRegex() + " por el " + expediente.getNumDocRGI() + ".";
                                                        if (rgiJobDAO.grabarObservacionesExpediente(codOrg, numExpediente, esActivo, observacionesExp, con)) {
                                                            log.info(" =======> RGIActualizarDocumentoJob() -  Observaciones del expediente actualizadas");
                                                        } else {
                                                            log.error(" =======> RGIActualizarDocumentoJob() -  Ha ocurrido un error al actualizar las observaciones del expediente. ");
                                                        }

                                                    } catch (Exception ex) {
                                                        log.error(" =======> RGIActualizarDocumentoJob() -  Error al registar el cambio en el expediente ", ex);
                                                    }
                                                }

                                                // actualizar en sede
                                                String respuestaActualizarInteresados = "0";
                                                try {
                                                    log.info(" =======> RGIActualizarDocumentoJob() - se llama a Actualizar Interesados: ");
                                                    respuestaActualizarInteresados = actualizaInteresadosRGI(codOrg, numExpediente, esActivo, codTerceroSede, con);
                                                    correctos++;

                                                } catch (Exception e) {
                                                    log.error(" =======> RGIActualizarDocumentoJob() -  Error al tratar de actualizar interesados en Mi Carpeta. ", e);
                                                    respuestaActualizarInteresados = "1";
                                                }

                                                if (respuestaActualizarInteresados.equalsIgnoreCase("0")) {
                                                    log.info(" =======> RGIActualizarDocumentoJob() - Actualizar Interesados: OK");
                                                    cambioSede = "OK";
                                                    observacionesJob = "Actualizado el Interesado en Mi Carpeta";
                                                } else {
                                                    log.error(" Error al tratar de actualizar interesados en Mi Carpeta. ");
                                                    observacionesJob = " Error al tratar de actualizar interesados en Mi Carpeta. ";
                                                }
                                                // grabar resultado Sede
                                                int grabaResultadoSede = rgiJobDAO.guardarResultadoActualizarInteresados(numExpediente, observacionesJob, cambioSede, con);
                                            } else {
                                                log.error(" =======> RGIActualizarDocumentoJob() - No se ha actualizado el 3º del expediente " + numExpediente);
                                            }

                                        } else {
                                            log.error(" =======> RGIActualizarDocumentoJob() - El expediente no existe");
                                            // hacer algo??
                                        }

                                    } else {
                                        log.info(" =======> RGIActualizarDocumentoJob() - Ya se actualizó el documento en el expediente " + numExpediente);
                                    }

                                } // for

                            } else {
                                log.info(" =======> RGIActualizarDocumentoJob() - No hay expedientes para tratar");
                            }

                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }

                            if (con != null) {
                                con.close();
                            }

                            int errores = tratados - correctos - historicos;
                            log.info(" =====================> RGIActualizarDocumentoJob() - END - Tratados:  " + tratados);
                            log.info(" =====================> RGIActualizarDocumentoJob() - END - Correctos: " + correctos);
                            log.info(" =====================> RGIActualizarDocumentoJob() - END - Errores:   " + errores);
                            long fin = System.currentTimeMillis();
                            long transcurrido = fin - inicio;
                            log.info(" =======> RGIActualizarDocumentoJob() - Tiempo inicio - " + new SimpleDateFormat("hh:mm:ss:SSS").format(new Date(inicio)));
                            log.info(" =======> RGIActualizarDocumentoJob() - Tiempo fin    - " + new SimpleDateFormat("hh:mm:ss:SSS").format(new Date(fin)));
                            log.info(" =======> RGIActualizarDocumentoJob() - Transcurrido  -    " + new SimpleDateFormat("mm:ss:SSS").format(new Date(transcurrido)));
                        }
                    } catch (BDException ex) {
                        log.error(" =======> RGIActualizarDocumentoJob() -  Error en el job : ", ex);
                    } catch (NumberFormatException ex) {
                        log.error(" =======> RGIActualizarDocumentoJob() -  Error en el job : ", ex);
                    } finally {
                        if (con != null) {
                            con.close();
                        }
                        if (adaptador != null) {
                            try {
                                adaptador.devolverConexion(conTransaccion);
                            } catch (BDException e) {
                                log.error(" =======> RGIActualizarDocumentoJob() - Error devolviendo la conexion transaccional... " + e.getErrorCode() + " " + e.getDescripcion() + " " + e.getMensaje(), e);
                            }
                        }
                    }

                }//synchro
            }//PARA LOCAL QUITAR

        } catch (Exception ex) {
            log.error(" =======> RGIActualizarDocumentoJob() -  Error en el job : ", ex);
        }

    }

    /**
     *
     * @param codOrg
     * @param numExpediente
     * @param esActivo
     * @param con
     * @return
     */
    private String actualizaInteresadosRGI(int codOrg, String numExpediente, boolean esActivo, int codTercero, Connection con) throws Exception {
        log.info("actualizaInteresados ");
        String mensaje = "0";
        int id = 0;
        int result = 0;
        String[] datosExp = numExpediente.split(ConstantesMeLanbide43.BARRA_SEPARADORA);
        RGI_JobDAO rgiJobDAO = RGI_JobDAO.getInstance();
        try {
            result = rgiJobDAO.guardarMiGestionRGI(codOrg, numExpediente, esActivo, con);
            if (result > 0) {
                id = rgiJobDAO.getIdGestiones(con);
                log.info("Id generado al  guardar MiGestion RGI: " + id);
                String idProcedimiento = "";
                try {
//                    idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(datosExp[1]);
                    ArrayList<Participantes> listaParticipantes = rgiJobDAO.obtenerLDatosInteresadoRGI(numExpediente, esActivo, codTercero, con);
                    log.info("despues de recoger participantes ");

                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(datosExp[1]);
                    // Expediente
                    Lan6Expediente lan6Expediente = new Lan6Expediente();
                    lan6Expediente.setNumero(numExpediente);
                    lan6Expediente.setEjercicio(datosExp[0]);
                    log.info("despues de recoger expediente ");

                    // Participacion
                    Lan6Participacion lan6participacion = new Lan6Participacion();
                    //Interesados
                    ArrayList<Lan6Interesado> interesados = new ArrayList<Lan6Interesado>();
                    for (Participantes participante : listaParticipantes) {
                        log.info("nombre interesado: " + participante.getNombre());
                        log.info("nif interesado: " + participante.getNif());
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
                    DatosAvisoCSRegexlan datosAvisoCSRegexlan = getDatosAviso(numExpediente, esActivo, con);
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

                    servicios.actualizarInteresados(lan6Expediente);

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
     *
     * @param numExpediente
     * @param esActivo
     * @param con
     * @return
     */
    private DatosAvisoCSRegexlan getDatosAviso(String numExpediente, boolean esActivo, Connection con) {
        RGI_JobDAO rgiJobDAO = RGI_JobDAO.getInstance();
        DatosAvisoCSRegexlan datosNotif = new DatosAvisoCSRegexlan();
        try {
            datosNotif.setAvisoIdCuentaInteresadoRepresentante(rgiJobDAO.getDatosNotificacionElectronica(numExpediente, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTE, con));
            datosNotif.setAvisoEmailRepresentante(rgiJobDAO.getDatosNotificacionElectronica(numExpediente, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAIL, con));
            datosNotif.setAvisoSmsRepresentante(rgiJobDAO.getDatosNotificacionElectronica(numExpediente, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMS, con));
            datosNotif.setAvisoIdCuentaInteresadoTitular(rgiJobDAO.getDatosNotificacionElectronica(numExpediente, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTETIT, con));
            datosNotif.setAvisoEmailTitular(rgiJobDAO.getDatosNotificacionElectronica(numExpediente, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAILTIT, con));
            datosNotif.setAvisoSmsTitular(rgiJobDAO.getDatosNotificacionElectronica(numExpediente, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMSTIT, con));
            datosNotif.setAvisoIdioma(rgiJobDAO.getDatosNotificacionElectronica(numExpediente, esActivo, DatosAvisoCSRegexlan.COD_CAMPO_IDIOMAAVISOS, con));
        } catch (Exception ex) {
            log.error("Error - getDatosAviso ", ex);
            datosNotif = null;
        }
        return datosNotif;
    }

    /**
     *
     * @param interesados
     * @return
     */
    private boolean hayRepresentanteLegalEnListaInteresado(ArrayList<Lan6Interesado> interesados) {
        if (interesados != null && !interesados.isEmpty()) {
            for (Lan6Interesado interesado : interesados) {
                if (Lan6Constantes.TIPO_INTERESADO_REPRESENTANTE.equalsIgnoreCase(interesado.getTipo())) {
                    return true;
                }
            }
        }
        return false;
    }
}
