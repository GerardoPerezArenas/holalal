/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide_interop.jobs;

import es.altia.flexia.integracion.moduloexterno.melanbide_interop.jobs.manager.InteropJobsArchivadoDocsAndExptsAKSSGAService;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.jobs.manager.InteropJobsUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.jobs.util.InteropJobsAKSSGAConstantes;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.jobs.vo.InteropJobsAKSSGAConfig;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.jobs.vo.InteropJobsAKSSGADocumento;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.jobs.vo.InteropJobsAKSSGAExpedienteRequest;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.jobs.vo.InteropJobsFSEPersonaServicio;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.jobs.vo.InteropJobsLog;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.nisae.gestorerrores.ErrorLan6ExcepcionBeanNISAE;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.nisae.gestorerrores.GestionarErroresDokusiNISAE;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.util.ConstantesMeLanbideInterop;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6ErrorBean;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6UtilExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.config.Lan6Config;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.ficheros.Lan6Ficheros;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.pif.Lan6GestionFicherosPif;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author INGDGC
 */
public class InteropJobsArchivadoDocsAndExptsAKSSGA implements Job {

    private static final Logger log = Logger.getLogger(InteropJobsArchivadoDocsAndExptsAKSSGA.class);
    private final SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final SimpleDateFormat formatFechaSGAYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
    private final InteropJobsArchivadoDocsAndExptsAKSSGAService interopJobsArchivadoDocsAndExptsAKSSGAService = new InteropJobsArchivadoDocsAndExptsAKSSGAService();
    private final InteropJobsUtils interopJobsUtils = new InteropJobsUtils();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        log.info(this.getClass().getName() + ".execute -  Begin " + formatFechaLog.format(new Date()));
        try {
            int contador = jec.getRefireCount();
            log.info("jec.getRefireCount(): " + contador);
            String servidor = interopJobsUtils.getParameter(interopJobsUtils.SERVIDOR, interopJobsUtils.FICHERO_PROPIEDADES);
            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.info("servidor: " + servidor);
            String auxMensajeError = "";
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
            synchronized (jec) {
                AdaptadorSQLBD adaptador = null;
                String numExpedienteEnProceso = "";
                String procedimientoEnProceso = "";
                int contadorFicheros = 0;
                int contadorExpedientes = 0;
                try {
                    log.info("Job lanzado : " + System.getProperty("weblogic.Name"));

                    int codOrg = Integer.parseInt(interopJobsUtils.getParameter(interopJobsUtils.ORGANIZACION_ESQUEMA, interopJobsUtils.FICHERO_PROPIEDADES));
                    boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter(interopJobsUtils.DOS_ENTORNOS,
                            interopJobsUtils.FICHERO_PROPIEDADES));
                    // Será en /codigo/flexia/sga/generacion
                    String urlSGAGeneracion = ConfigurationParameter.getParameter(InteropJobsAKSSGAConstantes.URL_PATH_GENERACION_SGA,
                            interopJobsUtils.FICHERO_PROPIEDADES);
                    // Es en /lan06/s54b (donde se subirán los ficheros procesados aquí)
                    String urlSGACargaPIF = ConfigurationParameter.getParameter(InteropJobsAKSSGAConstantes.URL_PATH_CARGA_SGA_PIF,
                            interopJobsUtils.FICHERO_PROPIEDADES);
                    // Será en /codigo/flexia/sga/carga/errores
                    String urlSGACargaErrores = ConfigurationParameter.getParameter(InteropJobsAKSSGAConstantes.URL_PATH_CARGA_SGA_FILES_ERROR,
                            interopJobsUtils.FICHERO_PROPIEDADES);
                    // Será 20
                    String limiteFicheroStr = ConfigurationParameter.getParameter(InteropJobsAKSSGAConstantes.LIMITE_FICHEROS_SGA,
                            interopJobsUtils.FICHERO_PROPIEDADES);
                    // Será 299
                    String limiteExpedientesFicheroStr = ConfigurationParameter.getParameter(InteropJobsAKSSGAConstantes.LIMITE_EXPEDIENTES_SGA,
                            interopJobsUtils.FICHERO_PROPIEDADES);
                    // Por defecto asignamos 20 desde codigo como documento analisis
                    int limiteFicheros = (limiteFicheroStr != null && !limiteFicheroStr.isEmpty() ? Integer.valueOf(limiteFicheroStr) : 20);
                    // Por defecto asignamos 299 desde codigo como documento analisis
                    int limiteExpedientesFichero = (limiteExpedientesFicheroStr != null && !limiteExpedientesFicheroStr.isEmpty() ? Integer.valueOf(limiteExpedientesFicheroStr) : 299);
                    log.info("limiteFicheros: " + limiteFicheros + " - limiteExpedientesFichero: " + limiteExpedientesFichero);
                    while (codOrg < 2) {
                        adaptador = interopJobsUtils.getAdaptSQLBD(String.valueOf(codOrg));
                        log.info("While de tokens codOrg: " + codOrg);
                        // Comprobar existencia de  ficheros errores en carpeta salida y enviar correo en caso de existir
                        if (!interopJobsArchivadoDocsAndExptsAKSSGAService.existeFicherosErrorUrlSGAcargaPIF(urlSGACargaPIF)) {
                            // Obtenemos Procedimientos a Tratar
                            List<InteropJobsAKSSGAConfig> procedimientos = interopJobsArchivadoDocsAndExptsAKSSGAService.getProcedimientosProcesarAKSSGA(adaptador);
                            // Preparamos XML para iRlo creado
                            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                            if (procedimientos != null) {
                                for (InteropJobsAKSSGAConfig procedimiento : procedimientos) {
                                    log.info("Tratando Procedimiento : " + procedimiento.getCodigoProcedimiento());
                                    procedimientoEnProceso = procedimiento.getCodigoProcedimiento();
                                    // root elements
                                    Document doc = docBuilder.newDocument();
                                    Element rootElementDatos = doc.createElement("datos");
                                    rootElementDatos.appendChild(doc.createComment("Tratando Procedimiento : " + procedimiento.getCodigoProcedimiento()));

                                    //contadorFicheros++;
                                    // Se pueden procesar 20 Ficheros
                                    if (contadorFicheros < limiteFicheros) {
                                        try {
                                            // Obtener los expedientes a tratar
                                            List<InteropJobsAKSSGAExpedienteRequest> listaExpedientesRequest = interopJobsArchivadoDocsAndExptsAKSSGAService.getExpedientesTratarAKSSGAByProcedimiento(procedimiento, adaptador);
                                            log.info("Expedientes a Tratar: " + (listaExpedientesRequest != null ? listaExpedientesRequest.size() : 0));
                                            if (listaExpedientesRequest != null && listaExpedientesRequest.size() > 0) {
                                                for (InteropJobsAKSSGAExpedienteRequest interopJobsFSEExpedienteRequest : listaExpedientesRequest) {
                                                    numExpedienteEnProceso = interopJobsFSEExpedienteRequest.getExpedienteNumero();
                                                    contadorExpedientes++;
                                                    if (contadorExpedientes > limiteExpedientesFichero) {
                                                        rootElementDatos.normalize();
                                                        doc.appendChild(rootElementDatos);
                                                        contadorFicheros++;
                                                        crearXML(doc, urlSGAGeneracion, contadorFicheros, procedimiento.getCodigoProcedimiento(), adaptador);
                                                        contadorExpedientes = 1;
                                                        // Resetear el fichero XML
                                                        doc = docBuilder.newDocument();
                                                        rootElementDatos = doc.createElement("datos");
                                                        rootElementDatos.appendChild(doc.createComment("Tratando Procedimiento : " + procedimiento.getCodigoProcedimiento()));
                                                        //contadorFicheros++;
                                                    }
                                                    log.info("Tratando Expediente : " + interopJobsFSEExpedienteRequest.getExpedienteNumero());
                                                    // Se pueden procesar 20 Ficheros >= Porque incializa con 0 y aumenta cuado lo crea, + 20 lo crea y ya deberia parar, 
                                                    if (contadorFicheros >= limiteFicheros) {
                                                        log.error("No se procesa mas (Nivel loop expediente), limite de ficheros alzanzado para la generacion de XML SGA.. Contador : " + contadorFicheros + " - Limite: " + limiteFicheros);
                                                        break;
                                                    }
                                                    rootElementDatos.appendChild(doc.createComment("Tratando Expediente : " + interopJobsFSEExpedienteRequest.getExpedienteNumero()));
                                                    Element archivo = doc.createElement("archivo");
                                                    Element identificacion = doc.createElement("identificacion");
                                                    Element contexto = doc.createElement("contexto");
                                                    Element contenido = doc.createElement("contenido");
                                                    Element condiciones = doc.createElement("condiciones");
                                                    Element otrosdatos = doc.createElement("otrosdatos");
                                                    Element signaturas = doc.createElement("signaturas");
                                                    Element documentoselectronicos = doc.createElement("documentoselectronicos");

                                                    // Rellenar Identificacion
                                                    Element niveldescripcion = doc.createElement("niveldescripcion");
                                                    niveldescripcion.setTextContent(String.valueOf(procedimiento.getNivel()));
                                                    Element cuadroclasificacion = doc.createElement("cuadroclasificacion");
                                                    cuadroclasificacion.setTextContent(String.valueOf(procedimiento.getCuadro()));
                                                    Element titulo = doc.createElement("titulo");
                                                    titulo.setTextContent(interopJobsFSEExpedienteRequest.getExpedienteTitulo());
                                                    Element fechaInicio = doc.createElement("fechaInicio");
                                                    fechaInicio.setTextContent(interopJobsFSEExpedienteRequest.getExpedienteFechaInicio() != null ? formatFechaSGAYYYYMMDD.format(interopJobsFSEExpedienteRequest.getExpedienteFechaInicio()) : "");
                                                    Element fechaFin = doc.createElement("fechaFin");
                                                    fechaFin.setTextContent(interopJobsFSEExpedienteRequest.getExpedienteFechaFin() != null ? formatFechaSGAYYYYMMDD.format(interopJobsFSEExpedienteRequest.getExpedienteFechaFin()) : "");
                                                    Element numeroexpediente = doc.createElement("numeroexpediente");
                                                    numeroexpediente.setTextContent(interopJobsFSEExpedienteRequest.getExpedienteNumero());

                                                    identificacion.appendChild(niveldescripcion);
                                                    identificacion.appendChild(cuadroclasificacion);
                                                    identificacion.appendChild(titulo);
                                                    identificacion.appendChild(fechaInicio);
                                                    identificacion.appendChild(fechaFin);
                                                    identificacion.appendChild(numeroexpediente);
                                                    archivo.appendChild(identificacion);

                                                    // Rellenar contexto
                                                    Element productor = doc.createElement("productor");
                                                    productor.setTextContent(String.valueOf(procedimiento.getProductor()));
                                                    Element formadeingreso = doc.createElement("formadeingreso");
                                                    formadeingreso.setTextContent(procedimiento.getFormaDeIngreso());
                                                    Element formadeingreso_e = doc.createElement("formadeingreso_e");
                                                    formadeingreso_e.setTextContent(procedimiento.getFormaDeIngresoE());
                                                    contexto.appendChild(productor);
                                                    contexto.appendChild(formadeingreso);
                                                    contexto.appendChild(formadeingreso_e);
                                                    archivo.appendChild(contexto);

                                                    // Rellenar contenido
                                                    Element lugares = doc.createElement("lugares");
                                                    lugares.setTextContent(interopJobsFSEExpedienteRequest.getExpedienteLugares() != null ? interopJobsFSEExpedienteRequest.getExpedienteLugares() : "");
                                                    contenido.appendChild(lugares);
                                                    archivo.appendChild(contenido);

                                                    //Rellenar condiciones
                                                    Element lengua = doc.createElement("lengua");
                                                    lengua.setTextContent("Castellano/Euskera");
                                                    Element lengua_e = doc.createElement("lengua_e");
                                                    lengua_e.setTextContent("Euskara/Gaztelania");
                                                    condiciones.appendChild(lengua);
                                                    condiciones.appendChild(lengua_e);
                                                    archivo.appendChild(condiciones);

                                                    // Rellenar Otros datos
                                                    Element fechadescripcion = doc.createElement("fechadescripcion");
                                                    fechadescripcion.setTextContent(formatFechaSGAYYYYMMDD.format(new Date()));
                                                    Element archivero = doc.createElement("archivero");
                                                    archivero.setTextContent(procedimiento.getArchivero());
                                                    otrosdatos.appendChild(fechadescripcion);
                                                    otrosdatos.appendChild(archivero);
                                                    archivo.appendChild(otrosdatos);

                                                    // Rellenar signaturas
                                                    Element signatura = doc.createElement("signatura");
                                                    Element deposito = doc.createElement("deposito");
                                                    deposito.setTextContent(procedimiento.getDeposito());
                                                    Element tipoui = doc.createElement("tipoui");
                                                    tipoui.setTextContent(String.valueOf(procedimiento.getTipoUI()));
                                                    Element identificadorui = doc.createElement("identificadorui");
                                                    identificadorui.setTextContent(procedimiento.getIdUI());
                                                    signatura.appendChild(deposito);
                                                    signatura.appendChild(tipoui);
                                                    signatura.appendChild(identificadorui);
                                                    signaturas.appendChild(signatura);
                                                    archivo.appendChild(signaturas);

                                                    // Rellenar documentos electronicos
                                                    if (interopJobsFSEExpedienteRequest.getDocumentos() != null) {
                                                        for (InteropJobsAKSSGADocumento interopJobsAKSSGADocumento : interopJobsFSEExpedienteRequest.getDocumentos()) {
                                                            Element documento = doc.createElement("documento");
                                                            Element titulodoc = doc.createElement("titulodoc");
                                                            String tituloDocStr = (interopJobsAKSSGADocumento.getNombre() != null && !interopJobsAKSSGADocumento.getNombre().isEmpty() ? (interopJobsAKSSGADocumento.getNombre().length() > 150 ? interopJobsAKSSGADocumento.getNombre().substring(0, 150) : interopJobsAKSSGADocumento.getNombre()) : "");
                                                            titulodoc.setTextContent(tituloDocStr);
                                                            Element versiones = doc.createElement("versiones");
                                                            Element version = doc.createElement("version");
                                                            Element responsable = doc.createElement("responsable");
                                                            responsable.setTextContent(procedimiento.getResponsable());
                                                            Element oid = doc.createElement("oid");
                                                            oid.setTextContent(interopJobsAKSSGADocumento.getOid());
                                                            Element fechafichero = doc.createElement("fechafichero");
                                                            fechafichero.setTextContent(interopJobsAKSSGADocumento.getFechaDocumento() != null ? formatFechaSGAYYYYMMDD.format(interopJobsAKSSGADocumento.getFechaDocumento()) : "");
                                                            version.appendChild(responsable);
                                                            version.appendChild(oid);
                                                            version.appendChild(fechafichero);
                                                            versiones.appendChild(version);
                                                            documento.appendChild(titulodoc);
                                                            documento.appendChild(versiones);
                                                            documentoselectronicos.appendChild(doc.createComment(interopJobsAKSSGADocumento.getTipoDocumento() + " - " + interopJobsAKSSGADocumento.getId()));
                                                            documentoselectronicos.appendChild(documento);
                                                        }
                                                    } else {
                                                        documentoselectronicos.appendChild(doc.createComment("No e han recuperado documentos para archivar"));
                                                    }
                                                    archivo.appendChild(documentoselectronicos);
                                                    rootElementDatos.appendChild(archivo);

                                                    log.info(interopJobsFSEExpedienteRequest.toString());
                                                } // Fin: for (InteropJobsAKSSGAExpedienteRequest interopJobsFSEExpedienteRequest : listaExpedientesRequest) {
                                            } else { // Fin: if (listaExpedientesRequest != null && listaExpedientesRequest.size() > 0) 
                                                log.info("No se han recuperado expedientes para tratar del procedimiento: " + procedimiento.getCodigoProcedimiento());
                                                rootElementDatos.appendChild(doc.createTextNode("No se han recuperado expedientes para tratar del procedimiento"));
                                            }
                                            // Creamos un fichero por procedimiento
                                            // Si aun esta dentro del limite de ficheros XML debe ser menor, se aumenta al crearlo, por tanto si actual contador =20 significa que yas e creo el 20, el limete en este caso ejemplo
                                            if (contadorFicheros < limiteFicheros) {
                                                rootElementDatos.normalize();
                                                doc.appendChild(rootElementDatos);
                                                contadorFicheros++;
                                                crearXML(doc, urlSGAGeneracion, contadorFicheros, procedimiento.getCodigoProcedimiento(), adaptador);
                                            } else {
                                                log.error("No se procesa mas(nivel loop procedimiento), limite de ficheros alzanzado para la generacion de XML SGA.. Contador : " + contadorFicheros + " - Limite: " + limiteFicheros);
                                                break;
                                            }

                                            // Reseteamos el contador de expedientes
                                            contadorExpedientes = 0;
                                        } catch (Exception e) {
                                            log.error("Error procesando expedientes... ", e);
                                        }
                                    } // if (contadorFicheros < limiteFicheros)  
                                    else {
                                        log.error("No se procesa mas(nivel loop procedimiento), limite de ficheros alzanzado para la generacion de XML SGA.. Contador : " + contadorFicheros + " - Limite: " + limiteFicheros);
                                        break;
                                    }
                                } // for (InteropJobsAKSSGAConfig procedimiento : procedimientos)
                            } // if (procedimientos != null) 
                            else {
                                log.info("No hay procedimientos configurados para Tratar ...");
                            }

                        } // if(!interopJobsArchivadoDocsAndExptsAKSSGAService.existeFicherosErrorUrlSGAcargaPIF(urlSGACargaPIF))
                        else {
                            // Enviar correo de error y no generar ningun fichero
                            String mensajeExcepcion = "Existen ficheros de error en la carpeta de respuesta de SGA " + urlSGACargaPIF;
                            log.error(mensajeExcepcion);
                            try {
                                log.info("Vamos a registrar el error en BD : " + mensajeExcepcion);
                                String causaExcepcion = "Ficheros de error encontrados en el directorio " + urlSGACargaPIF;
                                String trazaError = "Ficheros de error encontrados en el directorio " + urlSGACargaPIF;

                                // Copiamos los ficheros al server para ser revisados
                                try {
                                    List<Lan6Documento> listaDocumentosRespuesta = interopJobsArchivadoDocsAndExptsAKSSGAService.getListaDocumentosFromURLPIF(urlSGACargaPIF, true);
                                    // Obtenemos Ficheros
                                    for (Lan6Documento ficherosRespuestaSGAPIF : listaDocumentosRespuesta) {
                                        log.info("Copinado fichero: " + ficherosRespuestaSGAPIF.getNombre());
                                        if (ficherosRespuestaSGAPIF.getNombre() != null && !ficherosRespuestaSGAPIF.getNombre().isEmpty()
                                                && ficherosRespuestaSGAPIF.getNombre().toLowerCase().contains("_erro")) {
                                            // Leemos contenido y lo almcenamos en BD 
                                            File ficherosRespuestaSGA = new File(urlSGACargaErrores + "/" + ficherosRespuestaSGAPIF.getNombre() + "." + ficherosRespuestaSGAPIF.getFormat());
                                            ficherosRespuestaSGA.getParentFile().mkdirs();
                                            FileUtils.writeByteArrayToFile(ficherosRespuestaSGA, ficherosRespuestaSGAPIF.getContenido());
                                            log.info("Fichero copiado en el server correctamente ... ");
                                        } else {
                                            log.info("Fichero no procesado... Nombre no cumple las condiciones: ficherosRespuestaSGAPIF.getNombre() != null && !ficherosRespuestaSGAPIF.getNombre().isEmpty() "
                                                    + "                                                    && ficherosRespuestaSGAPIF.getNombre().toLowerCase().contains(_erro)");
                                        }
                                    }
                                } catch (Exception e) {
                                    log.error("Error al copiar los ficheros de error al server de flexia", e);
                                }

                                Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                                        trazaError, ConstantesMeLanbideInterop.ERROR_NISAE_SISTEMA_ORIGEN_FLEXIA,
                                        InteropJobsAKSSGAConstantes.ERROR_AKSSGA_CODIGO_ERROR_HAY_FICH_ERROR, MessageFormat.format(InteropJobsAKSSGAConstantes.ERROR_AKSSGA_MENSAJE_HAY_FICH_ERROR, urlSGACargaPIF, urlSGACargaErrores), 0);
                                ErrorLan6ExcepcionBeanNISAE errorLan6Bean = new ErrorLan6ExcepcionBeanNISAE(errorBean, new Exception(mensajeExcepcion, new Throwable(trazaError)));
                                int idError = GestionarErroresDokusiNISAE.grabarError(errorLan6Bean, "Procesando... " + numExpedienteEnProceso, "Procesando... " + procedimientoEnProceso, "", this.getClass().getName());
                                log.info("Error Registrado en BD correctamente. " + idError);
                            } catch (Exception ex1) {
                                ex1.printStackTrace();
                                log.error(this.getClass().getCanonicalName()
                                        + " Error al registrar errores mediante servicios de Adaptadores de platea. Error que se intenta Registrar : ", new Throwable(mensajeExcepcion));
                            }
                        }
                        if (dosEntornos) {
                            codOrg++;
                        } else {
                            codOrg = 2;
                        }
                    } // while (codOrg < 2)
                } catch (IOException ex) {
                    // Error al inprimir en disco el fichero XML
                    ex.printStackTrace();
                    log.error("Se ha producido un error al imprimir el fichero XML " + contadorFicheros
                            + "-" + formatFechaSGAYYYYMMDD.format(new Date())
                            + "_" + procedimientoEnProceso + ".xml", ex);
                    String mensajeExcepcion = "";
                    try {
                        log.info("Vamos a registrar el error en BD : " + ex.getMessage());
                        String causaExcepcion = (ex.getCause() != null ? (ex.getCause().getMessage() != null ? ex.getCause().getMessage() + " - " + ex.getCause().toString() : ex.getCause().toString()) : "");
                        mensajeExcepcion = ex.getMessage();
                        String trazaError = ExceptionUtils.getStackTrace(ex);

                        Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                                trazaError, ConstantesMeLanbideInterop.ERROR_NISAE_SISTEMA_ORIGEN_FLEXIA,
                                InteropJobsAKSSGAConstantes.ERROR_AKSSGA_CODIGO_ERROR_IMPRXML, MessageFormat.format(InteropJobsAKSSGAConstantes.ERROR_AKSSGA_MENSAJE_IMPRXML, contadorFicheros
                                        + "-" + formatFechaSGAYYYYMMDD.format(new Date())
                                        + "_" + procedimientoEnProceso + ".xml"), 1);
                        ErrorLan6ExcepcionBeanNISAE errorLan6Bean = new ErrorLan6ExcepcionBeanNISAE(errorBean, ex);
                        int idError = GestionarErroresDokusiNISAE.grabarError(errorLan6Bean, "Procesando ... " + numExpedienteEnProceso, "Procesando... " + procedimientoEnProceso, "", this.getClass().getName());
                        log.info("Error Registrado en BD correctamente. " + idError);
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                        log.error(this.getClass().getCanonicalName()
                                + " Error al registrar errores mediante servicios de Adaptadores de platea. Error que se intenta Registrar : ", new Throwable(mensajeExcepcion));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    log.error(" Error Al ejecutar el job. ", ex);
                    String mensajeExcepcion = "";
                    try {
                        log.info("Vamos a registrar el error en BD : " + ex.getMessage());
                        String causaExcepcion = (ex.getCause() != null ? (ex.getCause().getMessage() != null ? ex.getCause().getMessage() + " - " + ex.getCause().toString() : ex.getCause().toString()) : "");
                        mensajeExcepcion = ex.getMessage();
                        String trazaError = ExceptionUtils.getStackTrace(ex);

                        Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                                trazaError, ConstantesMeLanbideInterop.ERROR_NISAE_SISTEMA_ORIGEN_FLEXIA,
                                InteropJobsAKSSGAConstantes.ERROR_AKSSGA_CODIGO_ERROR_GENERAL, MessageFormat.format(InteropJobsAKSSGAConstantes.ERROR_AKSSGA_MENSAJE_GENERAL, ex.getMessage()), 0);
                        ErrorLan6ExcepcionBeanNISAE errorLan6Bean = new ErrorLan6ExcepcionBeanNISAE(errorBean, ex);
                        int idError = GestionarErroresDokusiNISAE.grabarError(errorLan6Bean, "Procesando ... " + numExpedienteEnProceso, "Procesando... " + procedimientoEnProceso, "", this.getClass().getName());
                        log.info("Error Registrado en BD correctamente. " + idError);
                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                        log.error(this.getClass().getCanonicalName()
                                + " Error al registrar errores mediante servicios de Adaptadores de platea. Error que se intenta Registrar : ", new Throwable(mensajeExcepcion));
                    }
                } finally {
                    log.info(this.getClass().getName() + ".execute -  Fin " + formatFechaLog.format(new Date()));
                }
            } // synchronized (jec)
            } // if (servidor.equals(System.getProperty("weblogic.Name")))
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(this.getClass().getName() + " Error: " + ex);
        }
    }

    private InteropJobsLog mapearDatosPersonaServicioFSEToInteropJobsLog(InteropJobsFSEPersonaServicio interopJobsFSEPersonaServicio) {
        InteropJobsLog respuesta = null;
        if (interopJobsFSEPersonaServicio != null) {
            respuesta = new InteropJobsLog(this.getClass().getSimpleName(),
                    interopJobsUtils.getCodProcedimientoFromNumExpediente(interopJobsFSEPersonaServicio.getNumeroExpediente()),
                    interopJobsFSEPersonaServicio.getNumeroExpediente(),
                    interopJobsFSEPersonaServicio.getTipoDocumentoRegexlan(),
                    interopJobsFSEPersonaServicio.getNumeroDocumento(),
                    interopJobsFSEPersonaServicio.toString());
        }
        return respuesta;
    }

    private void crearXML(Document doc, String urlSGAGeneracion, int contadorFicheros, String procedimiento, AdaptadorSQLBD adaptador) throws TransformerConfigurationException, TransformerException, IOException, Exception {
        // Imprimir el fichero XML por cada procedimiento
        try {
            // + "\\" + procedimiento Sin estructura de subcarpeta
            String nombreFichero = urlSGAGeneracion + "/" + contadorFicheros
                    + "-" + formatFechaSGAYYYYMMDD.format(new Date())
                    + "_" + procedimiento + ".xml";
            // Añadido para borrar ficheros antiguos xml
            final List<String> files = listarFicheros(urlSGAGeneracion, InteropJobsAKSSGAConstantes.DIAS_TRANSCURRIDOS_BORRAR_FICHEROS_SERVIDOR);
            log.info(borrarFicherosAntiguos(files));
            // Fin de la eliminación de ficheros antiguos xml
            File fileXML = new File(nombreFichero);
            fileXML.getParentFile().mkdirs();
            fileXML.createNewFile();
            FileOutputStream output = new FileOutputStream(fileXML);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            //Configurar para imrimir con espacios y en java 6 modo arbol con espacios ente nodos padres/hijos
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            //Ocultar texto standalone en el definicion del fichero
            doc.setXmlStandalone(true);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(output);

            transformer.transform(source, result);

            try {
                // Leer el fihero Formateado que acabamos de crear
                File fileXMLFormateado = new File(nombreFichero);
                if (fileXMLFormateado.exists() && fileXMLFormateado.isFile()) {
                    // Creado en el server local, dejamos en el PIF
                    String procedimientoPlatea = interopJobsArchivadoDocsAndExptsAKSSGAService.getMelanbideDokusiPlateaProByCodigoProcedimientoFlexia(procedimiento, adaptador);
                    log.info("procedimientoPlatea : " + procedimientoPlatea);
                    String app = interopJobsUtils.getParameter(InteropJobsAKSSGAConstantes.SGA_APP_PIF, interopJobsUtils.FICHERO_PROPIEDADES);
                    log.info("app : " + app);
                    /*if (procedimientoPlatea!= null && procedimientoPlatea.indexOf('_') == -1) {
                    app = procedimientoPlatea;
                }else{
                    app = procedimientoPlatea.substring(0, procedimientoPlatea.indexOf('_'));
                }*/
                    //Lan6Config.getProperty("AdaptadoresPlateaLan6.properties", "APP_NAME_" + procedimientoPlatea);

                    String urlToken = (Lan6Config.getProperty("AdaptadoresPlateaLan6.properties", "URL_TOKEN") + "?app=" + app);
                    log.info("urlToken : " + urlToken);
                    String sessionToken = Lan6Ficheros.obtenerFicheroURL(urlToken);
                    log.info("sessionToken : " + sessionToken);
                    String rutaURLPIF = Lan6Config.getProperty("AdaptadoresPlateaLan6.properties", "URL_PIF");
                    log.info("rutaURLPIF : " + rutaURLPIF);

                    //Lan6Config.getProperty("AdaptadoresPlateaLan6.properties", "CARPETA_PIF_" + procedimientoPlatea);
                    String rutaLanPIFWrite = interopJobsUtils.getParameter(InteropJobsAKSSGAConstantes.URL_PATH_GENERACION_SGA_PIF, interopJobsUtils.FICHERO_PROPIEDADES);
                    log.info("rutaLanPIFWrite : " + rutaLanPIFWrite);
                    // Lan6Config.getProperty("AdaptadoresPlateaLan6.properties", "USU_" + procedimientoPlatea);
                    String usuarioPIF = interopJobsUtils.getParameter(InteropJobsAKSSGAConstantes.SGA_USUARIO_PIF, interopJobsUtils.FICHERO_PROPIEDADES);
                    log.info("usuarioPIF : " + usuarioPIF);
                    //Lan6Config.getProperty("AdaptadoresPlateaLan6.properties", "PASS_" + procedimientoPlatea);
                    String passwordPIF = interopJobsUtils.getParameter(InteropJobsAKSSGAConstantes.SGA_PASS_PIF, interopJobsUtils.FICHERO_PROPIEDADES);
                    log.info("passwordPIF : " + passwordPIF);

                    Lan6GestionFicherosPif gestionFicherosPif = new Lan6GestionFicherosPif(sessionToken, rutaURLPIF, usuarioPIF, passwordPIF, null);
                    Lan6Documento lan6Documento = new Lan6Documento();
                    lan6Documento.setNombre(fileXMLFormateado.getName());
                    lan6Documento.setContenido(FileUtils.readFileToByteArray(fileXML));
                    lan6Documento.setFormat("xml");
                    log.info("lan6Documento.setNombre : " + lan6Documento.getNombre()
                            + " lan6Documento.setContenido (length) : " + (lan6Documento.getContenido() != null ? lan6Documento.getContenido().length : 0)
                            + " lan6Documento.setFormat : " + lan6Documento.getFormat()
                    );

                    // Añadido para recorrer los ficheros en remoto.
                    try {
                        byte []lista = gestionFicherosPif.recuperarListaFicheros(rutaLanPIFWrite);
                    } catch (Lan6UtilExcepcion ex) {
                        log.error("No consigo sacar el listado de ficheros en remoto " + ex.getMensajeExcepcion());
                    }
                    listarFicheros(rutaLanPIFWrite, InteropJobsAKSSGAConstantes.DIAS_TRANSCURRIDOS_BORRAR_FICHEROS_SERVIDOR);
                    // Fin de lo añadido para recorrer los ficheros en remoto.
                    String rutaNuevoFichero = gestionFicherosPif.dejarFichero(lan6Documento, rutaLanPIFWrite + "/" + fileXML.getName());
                    log.info("rutaNuevoFichero : " + rutaNuevoFichero);
                } else {
                    log.error("No se ha podido leer el fichero XML dejado e el server local para enviar a PIF");
                }

            } catch (Lan6Excepcion ex) {
                ex.printStackTrace();
                log.error("Error al dejar el fichero en PIF ..." + ex.getMessage(), ex);
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error al imprimir el fichero XML ...", e);
            throw e;
        }
    }

    // Estos tres metodos deberan ir en otra clase, no tiene sentido duplicarlos, ya están probados en local.
    private List<String> listarFicheros(final String directorio, final String parametro) {
        final File carpeta = new File(directorio);

        final File[] files = carpeta.listFiles(new FileFilter() {
            @Override
            public boolean accept(File arch) {
                return arch.isFile();
            }
        });

        final List<String> result = new ArrayList<String>();
        final DateTime fechaActual = new DateTime();

        final int dias = leerParametroNumerico(parametro);
        final DateTime fechaPasada = fechaActual.minusDays(dias);

        log.info("listarFicheros " + fechaActual + ", " + fechaPasada);
        for (int i = 0; files != null && i < files.length; i++) {
            if (files[i].lastModified() < fechaPasada.getMillis()) {
                result.add(files[i].getAbsolutePath());
                log.info("listarFicheros borrese: " + files[i].getName());
            }
            log.info("listarFicheros " + files[i].getName() + ", " + new DateTime(files[i].lastModified()));
        }
        return result;
    }

    private boolean borrarFicherosAntiguos(final List<String> ficheros) {
        boolean exito = true;

        for (final String fichero : ficheros) {
            File ficheroLogs = new File(fichero);
            if (!ficheroLogs.delete()) {
                exito = false;
            }
        }
        return exito;
    }

    private int leerParametroNumerico(final String parametro) {
        try {
            final String valueString = interopJobsUtils.getParameter(parametro,
                    interopJobsUtils.FICHERO_PROPIEDADES);
            if (valueString == null) {
                throw new NumberFormatException();
            }
            return Integer.parseInt(valueString);

        } catch (NumberFormatException ex) {
            return 25;
        }
    }

}
