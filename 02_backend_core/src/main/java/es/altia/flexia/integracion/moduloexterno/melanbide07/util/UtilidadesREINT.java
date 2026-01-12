/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide07.util;

import com.google.gson.Gson;
import es.altia.flexia.integracion.moduloexterno.melanbide07.dao.MeLanbide07DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide07.manager.MeLanbide07Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide07.vo.ParamsEnviarEjecutiva;
import es.altia.flexia.integracion.moduloexterno.melanbide07.vo.ParamsSeguridadZorku;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author laura
 */
public class UtilidadesREINT {

    //Logger
    private static Logger log = LogManager.getLogger(UtilidadesREINT.class);
    //Instancia del cliente del servicio web
    //   private static GerServiceDeuda instance = null;
    private static final String BARRA = "/";
    private static final String MODULO_INTEGRACION = "MODULO_INTEGRACION";
    private static final String ERROR_RECUPERANDO_ID_DEUDA = "15";
    private static final String ERROR_RECUPERANDO_ID_DEUDA_VACIO = "35";
    private static final String TIPO_NIF_FLEXIA = "1";
    private static final String TIPO_NIE_FLEXIA = "3";
    private static final String TIPO_CIF_FLEXIA = "4";
    private static final String TIPO_CIF_ENT_FLEXIA = "5";
    private static final String TIPO_PASAPORTE_FLEXIA = "2";
    private static final int TAMANHO_EJERCICIO = 4;

    public static ParamsSeguridadZorku getSeguridad(int codOrganizacion) {
        ParamsSeguridadZorku seguridadVO = new ParamsSeguridadZorku();
        String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String usuario = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.USUARIO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String contrasena = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CONTRASENA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String area = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.AREA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        log.info("Usuario: " + usuario);
        log.info("Contrasena: " + contrasena);
        log.info("Area: " + area);
        seguridadVO.setArea(area);
        seguridadVO.setUsuario(usuario);
        seguridadVO.setContrasena(contrasena);
        return seguridadVO;
    }

    public static String llamaAlServicioWebEnviarEjecutivaNoRGI(String codOrganizacion, String numExpediente, Integer idSecuenciaProcesoProgramado, AdaptadorSQLBD adaptador) throws Exception {
        log.info("enviarEjecutivaSubvenciones ( numExpediente = " + numExpediente + " ) : BEGIN - CodOrg: " + codOrganizacion);
        String codOperacion = "0";
        String detalleError = "";
        String ejercicio = "";
        String codProcedimiento = "";
        String idDeuda = "";
        Connection con = null;
        try {
            con = adaptador.getConnection();
            String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoIdDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoIdDeudaVia = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA_VIA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFecAcuseNotifResolucion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FECHA_ACUSE_NOTIF_RESOL, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoTipoNotifResolucion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_TIPO_NOTIF_RESOL, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            //String codTramiteViaEjec = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_VIA_EJECUTIVA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codTramiteAcuseNotificacion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_ACUSE_RESOLUCION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            String fechaAcuseZorku = "";
            String tipoNotificacion = "";

            IModuloIntegracionExternoCamposFlexia gestorCampoSup = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            if (numExpediente != null && !"".equals(numExpediente)) {
                String[] datos = numExpediente.split(BARRA);
                ejercicio = datos[0];
                codProcedimiento = datos[1];
            }
            int ocurrenciaTramiteAcuse = MeLanbide07DAO.getInstance().getMaxOcurrenciaTramitexCodigo(Integer.parseInt(codOrganizacion), codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramiteAcuseNotificacion), con);

            SalidaIntegracionVO campoFecAcuseNotifResolucion = gestorCampoSup.getCampoSuplementarioTramite(codOrganizacion, ejercicio, numExpediente, codProcedimiento,
                    Integer.parseInt(codTramiteAcuseNotificacion), ocurrenciaTramiteAcuse, codCampoFecAcuseNotifResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
            SalidaIntegracionVO campoTipoNotifResolucion = gestorCampoSup.getCampoSuplementarioTramite(codOrganizacion, ejercicio, numExpediente, codProcedimiento,
                    Integer.parseInt(codTramiteAcuseNotificacion), ocurrenciaTramiteAcuse, codCampoTipoNotifResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);

            Calendar fecAcuseNotifResolucion = null;

            //Informo los parametros
            if (campoFecAcuseNotifResolucion.getStatus() == 0) {
                fecAcuseNotifResolucion = campoFecAcuseNotifResolucion.getCampoSuplementario().getValorFecha();
                if (fecAcuseNotifResolucion != null) {
                    fechaAcuseZorku = convierteFechaZorku(fecAcuseNotifResolucion);
                }
            }
            log.info("Fecha fechaAcuseNotifResolucion: " + fechaAcuseZorku);
            if (campoTipoNotifResolucion.getStatus() == 0) {
                tipoNotificacion = campoTipoNotifResolucion.getCampoSuplementario().getValorDesplegable();
            }
            log.info("tipoNotificacion: " + tipoNotificacion);
            try {
                SalidaIntegracionVO campoIdDeudaRes = gestorCampoSup.getCampoSuplementarioExpediente(codOrganizacion, ejercicio, numExpediente, codProcedimiento,
                        codCampoIdDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                if (campoIdDeudaRes.getStatus() == 0) {
                    idDeuda = campoIdDeudaRes.getCampoSuplementario().getValorTexto();
                } else {
                    log.error("  --  enviarEjecutivaSubvenciones  -- Devolvemos: " + ERROR_RECUPERANDO_ID_DEUDA);
                    return ERROR_RECUPERANDO_ID_DEUDA;
                }
                log.info("idDeuda: " + idDeuda);

            } catch (Exception e) {
                log.error("  --  enviarEjecutivaSubvenciones  -- Devolvemos: " + ERROR_RECUPERANDO_ID_DEUDA, e);
                return ERROR_RECUPERANDO_ID_DEUDA;

            }

            //Ejecucion del SW de establecimiento de fechas de pago 
            //comprobar si la deuda no esta pagada. Si esta pagada, se mostrara el mensaje "La deuda ya ha sido pagada. No es posible enviarla a vía ejecutiva"
            String estadoDeuda = MeLanbide07Manager.getInstance().getEstadoDeudaZORKU(idDeuda, adaptador);
            if (!"2".equals(estadoDeuda)) {
                log.info("LLAMAR A enviarEjecutivaSubvenciones");
                String urlEjecutiva = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_EJECUTIVA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                //Creo el VO de seguridad con un usuario SIPCA
                ParamsSeguridadZorku seguridadVO = getSeguridad(Integer.parseInt(codOrganizacion));
                ParamsEnviarEjecutiva parametrosEjecutiva = new ParamsEnviarEjecutiva();
                parametrosEjecutiva.setZorUsUsuario(seguridadVO.getUsuario());
                parametrosEjecutiva.setZorUsPassword(seguridadVO.getContrasena());
                parametrosEjecutiva.setArea(seguridadVO.getArea());
                parametrosEjecutiva.setZorLiNumLiquidacion(new BigDecimal(idDeuda));
                parametrosEjecutiva.setZorTnotCodigo(tipoNotificacion);
                parametrosEjecutiva.setZorLiFechaNotificacion(fechaAcuseZorku);

                try {
                    HttpClient client = HttpClients.createDefault();
                    HttpPost llamadaEjecutiva = new HttpPost(urlEjecutiva);
                    String jsonBody = new Gson().toJson(parametrosEjecutiva);
                    StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);

                    llamadaEjecutiva.setHeader("Accept", "application/json");
                    llamadaEjecutiva.setEntity(entidadLlamada);
                    log.info("  --  enviarEjecutivaSubvenciones  -- Llamando al servicio " + urlEjecutiva + " ... " + jsonBody);
                    HttpResponse respuesta = client.execute(llamadaEjecutiva);
                    // recojo respuesta
                    log.info("  --  enviarEjecutivaSubvenciones  --  getStatusLine() : " + respuesta.getStatusLine());
                    int statusCode = respuesta.getStatusLine().getStatusCode();
                    log.info("  --  enviarEjecutivaSubvenciones  --  getStatusLine().getStatusCode() : " + statusCode);
                    log.info("  --  enviarEjecutivaSubvenciones  --  Hay respuesta de ZORKU");
                    HttpEntity entidadRespuesta = respuesta.getEntity();
                    // convierto la respuesta a STRING
                    String respuestaString = EntityUtils.toString(entidadRespuesta);
                    log.debug("  --  enviarEjecutivaSubvenciones  --  Respuesta String: " + respuestaString);
                        // convierto a JSON
                        JSONObject respuestaJson;
                        switch (statusCode) {
                            case HttpStatus.SC_OK:
                            log.info("  --  enviarEjecutivaSubvenciones  -- Hay respuesta SIN ERROR del SW");
                                respuestaJson = new JSONObject(respuestaString);
                            String idDeudaEjec = respuestaJson.getString("numLiquidacion");
                            log.info("  --  enviarEjecutivaSubvenciones  -- Liquidación generada en Ejecutiva - " + idDeudaEjec);
                            detalleError = "Liquidación generada en Ejecutiva - " + idDeudaEjec;
                            // SE GRABA EL ID DE LA LIQUIDACION EN EJECUTIVA
                            try {
                                CampoSuplementarioModuloIntegracionVO campoSuplementarioIdDeuda = new CampoSuplementarioModuloIntegracionVO();
                                campoSuplementarioIdDeuda.setCodOrganizacion(String.valueOf(codOrganizacion));
                                campoSuplementarioIdDeuda.setCodProcedimiento(codProcedimiento);
                                campoSuplementarioIdDeuda.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                campoSuplementarioIdDeuda.setTramite(false);
                                campoSuplementarioIdDeuda.setNumExpediente(numExpediente);
                                campoSuplementarioIdDeuda.setEjercicio(ejercicio);
                                campoSuplementarioIdDeuda.setCodigoCampo(codCampoIdDeudaVia);
                                campoSuplementarioIdDeuda.setValorTexto(idDeudaEjec);
                                gestorCampoSup.grabarCampoSuplementario(campoSuplementarioIdDeuda);
                                log.info(codCampoIdDeudaVia + ": " + idDeudaEjec);
                            } catch (Exception e) {
                                log.error("  --  enviarEjecutivaSubvenciones  -- Error al frabar el ID de la Liquidación en Ejecutiva. " + idDeudaEjec + "-" + e.getLocalizedMessage());
                            }
                                codOperacion = "0";
                                break;
                            case HttpStatus.SC_BAD_REQUEST:
                                respuestaJson = new JSONObject(respuestaString);
                                String codError = respuestaJson.getString("codigoError");
                                String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                log.error("  --  enviarEjecutivaSubvenciones  -- Error  al llamar a la API  - " + codError);
                                log.error("  --  enviarEjecutivaSubvenciones  -- " + msgErrorCas);
                                log.error("  --  enviarEjecutivaSubvenciones  -- " + msgErrorEus);
                                detalleError = "Error de WebService: " + msgErrorCas;
                                codOperacion = "1";
                                break;
                        case HttpStatus.SC_FORBIDDEN:
                            log.error("  --  enviarEjecutivaSubvenciones  -- Error " + statusCode + " al llamar a la API . " + respuesta.getStatusLine().getReasonPhrase());
                            detalleError = "Error de WebService: " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase();
                            codOperacion = "1";
                            break;
                            case HttpStatus.SC_NOT_FOUND:
                                log.error("  --  enviarEjecutivaSubvenciones  -- Error 404 al llamar a la API " + respuesta.getStatusLine().getReasonPhrase());
                                detalleError = "Error de WebService: 404 - " + respuesta.getStatusLine().getReasonPhrase();
                                codOperacion = "1";
                                break;
                            default:
                                //Resultado con error
                            log.error("error en enviarEjecutivaSubvenciones " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase());
                                detalleError = "Error de WebService: " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase();
                                codOperacion = "1";
                                break;
                }

                } catch (IOException e) {
                    log.error("IOException  en enviarEjecutivaSubvenciones ", e);
                    detalleError = "IOException  en enviarEjecutivaSubvenciones ";
                    codOperacion = "1";
                } catch (UnsupportedCharsetException e) {
                    log.error("UnsupportedCharsetException  en enviarEjecutivaSubvenciones ", e);
                    detalleError = "UnsupportedCharsetException  en enviarEjecutivaSubvenciones ";
                    codOperacion = "1";
                } catch (ParseException e) {
                    log.error("ParseException  en enviarEjecutivaSubvenciones ", e);
                    detalleError = "ParseException  en enviarEjecutivaSubvenciones ";
                    codOperacion = "1";
                } catch (JSONException e) {
                    log.error("JSONException  en enviarEjecutivaSubvenciones ", e);
                    detalleError = "JSONException  en enviarEjecutivaSubvenciones ";
                    codOperacion = "1";
                }

            } else {
                codOperacion = "40";//La deuda ya ha sido pagada. No es posible enviarla a vía ejecutiva
                log.error("error estadoDeuda: La deuda ya ha sido pagada. No es posible enviarla a vía ejecutiva");
                detalleError = "Error de validación: La deuda ya ha sido pagada. No es posible enviarla a vía ejecutiva";
            }
            log.info("enviarEjecutivaSubvenciones - FIN - Devolvemos: " + codOperacion);

        } catch (Exception e) {
            //Resultado con error
            log.error("error general en enviarEjecutivaSubvenciones ", e);
            detalleError = "excepción general en enviarEjecutivaSubvenciones " + e.getLocalizedMessage();
            codOperacion = "1";
        } finally {
            if (idSecuenciaProcesoProgramado != null) {
                int error = 0;
                if (!"0".equals(codOperacion)) {
                    error = 1;
                }
                MeLanbide07DAO.getInstance().insertarRegistroProcesosProgramadosViaEjeREINT(idSecuenciaProcesoProgramado, new Date(), numExpediente, idDeuda, detalleError, error, con);
            }
            ///////////////// PROC_PROG_VIAEJE_REINT
            //ID: IDENTIFICADOR CORRELATIVO DE CADA REGISTRO ACTUALIZADO. PK.
            //ID_PROCESO: ID DE LA EJECUCION DEL PROCESO
            //FECHA_OPERACION: Fecha tratamiento ese expediente
            //NUMERO_EXPEDIENTE
            //DOCUMENTO_TERCERO
            //NOMBRE_TERCERO
            //ID_DEUDA
            //DETALLE_ERROR: incluir error q devuelve webservice en la llamada
            //ERROR: 0-correcto / 1-error

        }
        return codOperacion;
    }

    /* Los tiposDocumento que maneja el Servicio Web de Alta de Deuda no se corresponden con los identificadores de tipo de Documento que maneja Flexia, obtenemos con este método la equivalencia */
    public static String traducirTipoDocZorku(String tipoFlexia) {
        log.debug("traducirTipoDocZorku.BEGIN. Documento de Flexia: " + tipoFlexia);

        String tipoCIF = ConfigurationParameter.getParameter(ConstantesMeLanbide07.TIPO_CIF_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String tipoNIE = ConfigurationParameter.getParameter(ConstantesMeLanbide07.TIPO_NIE_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String tipoPasaporte = ConfigurationParameter.getParameter(ConstantesMeLanbide07.TIPO_PASAPORTE_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String tipoOtro = ConfigurationParameter.getParameter(ConstantesMeLanbide07.TIPO_OTRO_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String tipoDNI = ConfigurationParameter.getParameter(ConstantesMeLanbide07.TIPO_DNI_ZK, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

        // DNI
        if (TIPO_NIF_FLEXIA.equals(tipoFlexia)) {
            return tipoDNI;
        }
        // CIF or CIF ENT. PUBLICA
        if (TIPO_CIF_FLEXIA.equals(tipoFlexia) || TIPO_CIF_ENT_FLEXIA.equals(tipoFlexia)) {
            return tipoCIF;
        }
        // NIE
        if (TIPO_NIE_FLEXIA.equals(tipoFlexia)) {
            return tipoNIE;
        }
        //Pasaporte
        if (TIPO_PASAPORTE_FLEXIA.equals(tipoFlexia)) {
            return tipoPasaporte;
        }
        return tipoOtro;
    }

    public static String traducirIdProvincia(String idFlexia) {
        log.debug("traducirIdProvincia. BEGIN: IdFlexia:" + idFlexia);
        String idWS = "";
        int tamanhoIdWS = 2;
        int tamanhoIdFlexia = idFlexia.length();
        try {
            if (tamanhoIdFlexia == tamanhoIdWS) {
                log.debug("traducirIdProvincia. END. IdWS: " + idFlexia);
                return idFlexia;
            }
            if (idFlexia != null) {
                if (!"".equals(idFlexia.trim())) {

                    idWS = "0" + idFlexia;
                    tamanhoIdFlexia = idWS.length();
                    while (tamanhoIdFlexia < tamanhoIdWS) {
                        idWS = "0" + idFlexia;
                        tamanhoIdFlexia = idWS.length();
                    }//while (tamanhoIdFlexia < tamanhoCodigoIdWS)
                }
            }
            log.debug("traducirIdProvincia. END: IdWS:" + idWS);
        } catch (Exception e) {
            log.error("Salta excepcion en el metodo traducirIdProvincia con idFlexia recibido:" + idFlexia);
        }
        return idWS;
    }

    public static String traducirIdMunicipio(String idFlexia) {
        log.debug("traducirIdPMunicipio. BEGIN: IdFlexia:" + idFlexia);
        String idWS = "";
        int tamanhoIdWS = 3;
        int tamanhoIdFlexia = idFlexia.length();
        try {
            if (tamanhoIdFlexia == tamanhoIdWS) {
                log.debug("traducirIdMunicipio. END. IdWS: " + idFlexia);
                return idFlexia;
            }
            if (!"".equals(idFlexia.trim())) {
                idWS = "0" + idFlexia;
                tamanhoIdFlexia = idWS.length();
                while (tamanhoIdFlexia < tamanhoIdWS) {
                    idWS = "0" + idWS;
                    tamanhoIdFlexia = idWS.length();
                }//while (tamanhoIdFlexia < tamanhoCodigoIdWS)
            }
            log.debug("traducirIdMunicipio. END: IdWS:" + idWS);
        } catch (Exception e) {
            log.error("Salta excepcion en el metodo traducirIdMunicipio con idFlexia recibido: " + idFlexia);
        }
        return idWS;
    }

    /**
     * Método que siempre nos devuelve los 4 primeros caracteres del String
     * recibido, en este caso el ejercicio
     *
     * @param codProcedimientoDeuda
     * @return ejercicio
     */
    public static String dameEjercicioDeuda(String codProcedimientoDeuda) {
        log.debug("dameEjercicio. BEGIN: " + codProcedimientoDeuda);
        String ejercicio = "";
        int indiceFinal = 0;
        int indiceInicial = 0;
        try {
            indiceFinal = codProcedimientoDeuda.length();
            log.debug("dameEjercicio. IndiceFinal: " + indiceFinal);
            indiceInicial = indiceFinal - TAMANHO_EJERCICIO;
            log.debug("dameEjercicio. IndiceInicial: " + indiceInicial);
            ejercicio = codProcedimientoDeuda.substring(indiceInicial, indiceFinal);
        } catch (Exception e) {
            log.error("Salto excepcion en dameEjercicioDeuda. CLASE: MELANBIDE07.java");
        }
        log.debug("dameEjercicio.END: " + ejercicio);
        return ejercicio;
    }

    public static String dameCodigoProcedimientoWebService(String codProcedimientoDeuda) {
        log.debug("dameCodigoProcedimientoWebService. BEGIN: " + codProcedimientoDeuda);
        String codigoPropiamenteDicho = "";
        int endIndex = 0;
        try {
            endIndex = codProcedimientoDeuda.length() - TAMANHO_EJERCICIO;
            log.debug("dameCodigoProcedimientoWebService. endIndex: " + endIndex);
            codigoPropiamenteDicho = codProcedimientoDeuda.substring(0, endIndex);
        } catch (Exception e) {
            log.error("Salto excepcion en dameCodigoProcedimientoWebService. CLASE: MELANBIDE07.java");
        }
        log.debug("dameCodigoProcedimientoWebService.END: " + codigoPropiamenteDicho);
        return codigoPropiamenteDicho;
    }

    // #311136
    public static String recuperaCifrasTelefono(String telefonoTer) {
        log.info("recuperaTelefono del tercero:" + telefonoTer);
        String telRetorno = "";
        for (int i = 0; i < telefonoTer.length(); i++) {
            if (Character.isDigit(telefonoTer.charAt(i))) {
                telRetorno = telRetorno + telefonoTer.charAt(i);
            }
        }
        if (telRetorno.length() > 9) {
            try {
                telRetorno = telRetorno.substring(0, 9);
            } catch (Exception e) {
                log.error("Error al recortar el telefono " + e);
            }
        }
        log.debug("Telefono Retorno: " + telRetorno);
        return telRetorno;
    }

    //Ajustes 20/02/2019
    /**
     * operacion que valida el IBAN de una cuenta bancaria devuelve si es o no
     * valido
     *
     * @param cuenta
     * @return boolean (IBAN valido)
     */
    public static boolean validaIBAN(String cuenta) {
        int resto = 0;
        int dc = 0;
        String cadenaDc = "";
        BigInteger cuentaNumero = new BigInteger("0");
        BigInteger modo = new BigInteger("97");
        cuentaNumero = new BigInteger(cuenta.substring(4, 24) + "142800");
        resto = cuentaNumero.mod(modo).intValue();
        dc = 98 - resto;
        cadenaDc = String.valueOf(dc);
        if (dc < 10) {
            cadenaDc = "0" + cadenaDc;
        }
        // comparo los caracteres 2 y 3 de la cuenta (digito de control) con cadenaDc
        return (cuenta.substring(2, 4).equals(cadenaDc));
    }

    /**
     * operacion que calcula la diferencia en dias entre 2 fechas
     *
     * @param fechaIn
     * @param fechaFinal
     * @return int (numero de dias de diferencia)
     */
    public static int restarFechas(Date fechaIn, Date fechaFinal) {
        GregorianCalendar fechaInicio = new GregorianCalendar();
        fechaInicio.setTime(fechaIn);
        GregorianCalendar fechaFin = new GregorianCalendar();
        fechaFin.setTime(fechaFinal);
        int dias = 0;
        if (fechaFin.get(Calendar.YEAR) == fechaInicio.get(Calendar.YEAR)) {
            dias = (fechaFin.get(Calendar.DAY_OF_YEAR) - fechaInicio.get(Calendar.DAY_OF_YEAR)) + 1;
        } else {
            int rangoAnyos = fechaFin.get(Calendar.YEAR) - fechaInicio.get(Calendar.YEAR);
            for (int i = 0; i <= rangoAnyos; i++) {
                int diasAnio = fechaInicio.isLeapYear(fechaInicio.get(Calendar.YEAR)) ? 366 : 365;
                if (i == 0) {
                    dias = 1 + dias + (diasAnio - fechaInicio.get(Calendar.DAY_OF_YEAR));
                } else if (i == rangoAnyos) {
                    dias = dias + fechaFin.get(Calendar.DAY_OF_YEAR);
                } else {
                    dias = dias + diasAnio;
                }
            }
        }
        return dias;
    }

    public static int dameAcCodZorku(int codArea) {
        int acCod = 0;
        switch (codArea) {
            case 2:
                acCod = 4;
                break;
            case 3:
                acCod = 6;
                break;
            case 4:
                acCod = 3;
                break;
            case 5:
                acCod = 7;
                break;
            case 8:
                acCod = 9;
                break;
            case 9:
                acCod = 10;
                break;
            case 10:
                acCod = 11;
                break;
            case 12:
                acCod = 8;
        }
        return acCod;
    }

    public static String convierteFechaZorku(Calendar fecha) {
        String fechaZORKU = null;
        try {
            XMLGregorianCalendar fechaConvertida = null;
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTime(fecha.getTime());
            fechaConvertida = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            String valorXmlGcal = String.valueOf(fechaConvertida);
            fechaZORKU = valorXmlGcal.split("\\u002b")[0];
        } catch (DatatypeConfigurationException ex) {
            log.error("Salto excepcion en convierteFechaZorku. CLASE: MELANBIDE07.java");
        }
        return fechaZORKU;
    }

    /**
     * Metodo llamado para devolver un String en formato JSON al cliente que ha
     * realiza la peticion a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a traves del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    public static void retornarJSON(String json, HttpServletResponse response) {
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
    }
}
