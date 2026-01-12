package es.altia.flexia.integracion.moduloexterno.melanbide92.util;

import com.google.gson.Gson;
import es.altia.agora.technical.ConstantesDatos;
import static es.altia.agora.technical.ConstantesDatos.BARRA;
import static es.altia.agora.technical.ConstantesDatos.MODULO_INTEGRACION;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide92.MELANBIDE92;
import es.altia.flexia.integracion.moduloexterno.melanbide92.manager.MeLanbide92Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsAltaDeuda;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsEnviarEjecutiva;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsSeguridadZorku;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DomicilioInteresadoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.InteresadoExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
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
 * @author kepa
 */
public class MeLanbide92Utilidades {
    //Logger

    private static final Logger log = LogManager.getLogger(MeLanbide92Utilidades.class);
    private static final IModuloIntegracionExternoCamposFlexia gestorCampoSup = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
    private static final String ERROR_RECUPERANDO_ID_DEUDA = "31";
    private static final String TIPO_NIF_FLEXIA = "1";
    private static final String TIPO_NIE_FLEXIA = "3";
    private static final String TIPO_CIF_FLEXIA = "4";
    private static final String TIPO_CIF_ENT_FLEXIA = "5";
    private static final String TIPO_PASAPORTE_FLEXIA = "2";

    public static String enviarEjecutivaSubvenciones(String codOrganizacion, String numExpediente, int codTramAcuseRes, int ocurrencia, Integer idSecuenciaProcesoProgramado, AdaptadorSQLBD adapt) throws Exception {
        log.info("enviarEjecutivaSubvenciones ( numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = "0";
        String detalleError = "";
        String ejercicio = "";
        String codProcedimiento = "";
        String numLiquidacion = "";
        String codCampoIdDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoIdDeudaVia = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_VIA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecAcuseNotifResolucion = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_ACUSE_NOTIF_RESOL, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoTipoNotifResolucion = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_TIPO_NOTIF_RESOL, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String fechaAcuseZorku = "";
        String tipoNotificacion = "";
        Connection con = null;
        try {
            con = adapt.getConnection();
            String[] datos = numExpediente.split(ConstantesMeLanbide92.BARRA_SEPARADORA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
            SalidaIntegracionVO campoFecAcuseNotifResolucion = gestorCampoSup.getCampoSuplementarioTramite(codOrganizacion, ejercicio, numExpediente, codProcedimiento,
                    codTramAcuseRes, ocurrencia, codCampoFecAcuseNotifResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
            SalidaIntegracionVO campoTipoNotifResolucion = gestorCampoSup.getCampoSuplementarioTramite(codOrganizacion, ejercicio, numExpediente, codProcedimiento,
                    codTramAcuseRes, ocurrencia, codCampoTipoNotifResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);

            Calendar fecAcuseNotifResolucion = null;

            //Informo los parametros
            if (campoFecAcuseNotifResolucion.getStatus() == 0) {
                fecAcuseNotifResolucion = campoFecAcuseNotifResolucion.getCampoSuplementario().getValorFecha();
                if (fecAcuseNotifResolucion != null) {
                    fechaAcuseZorku = convierteFechaZorku(fecAcuseNotifResolucion);
                }
            }
            log.info("fecAcuseNotifResolucion " + fechaAcuseZorku);
            if (campoTipoNotifResolucion.getStatus() == 0) {
                tipoNotificacion = campoTipoNotifResolucion.getCampoSuplementario().getValorDesplegable();
            }
            log.info("tipoNotificacion: " + tipoNotificacion);
            try {
                SalidaIntegracionVO campoIdDeudaRes = gestorCampoSup.getCampoSuplementarioExpediente(codOrganizacion, ejercicio, numExpediente, codProcedimiento,
                        codCampoIdDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                if (campoIdDeudaRes.getStatus() == 0) {
                    numLiquidacion = campoIdDeudaRes.getCampoSuplementario().getValorTexto();
                } else {
                    log.error("enviarEjecutivaSubvenciones.Devolvemos: " + ERROR_RECUPERANDO_ID_DEUDA);
                    return ERROR_RECUPERANDO_ID_DEUDA;
                }
                log.info("idDeuda: " + numLiquidacion);

            } catch (Exception e) {
                log.error("enviarEjecutivaSubvenciones.Devolvemos: " + ERROR_RECUPERANDO_ID_DEUDA, e);
                return ERROR_RECUPERANDO_ID_DEUDA;
            }
            //comprobar si la deuda no esta pagada. Si esta pagada, se mostrara el mensaje "La deuda ya ha sido pagada. No es posible enviarla a vía ejecutiva"
            String estadoDeuda = MeLanbide92Manager.getInstance().getEstadoDeudaZORKU(numLiquidacion, adapt);
            if (!"2".equals(estadoDeuda)) {
                log.info("LLAMAR A enviarEjecutivaSubvenciones");
                String urlEjecutiva = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_EJECUTIVA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                log.debug("URL: " + urlEjecutiva);
                ParamsSeguridadZorku seguridadVO = getSeguridad(Integer.parseInt(codOrganizacion));
                ParamsEnviarEjecutiva parametrosEjecutiva = new ParamsEnviarEjecutiva();
                parametrosEjecutiva.setZorUsUsuario(seguridadVO.getUsuario());
                parametrosEjecutiva.setZorUsPassword(seguridadVO.getContrasena());
                parametrosEjecutiva.setArea(seguridadVO.getArea());
                parametrosEjecutiva.setZorLiNumLiquidacion(new BigDecimal(numLiquidacion));
                parametrosEjecutiva.setZorTnotCodigo(tipoNotificacion);
                parametrosEjecutiva.setZorLiFechaNotificacion(fechaAcuseZorku);

                try {
                    HttpClient client = HttpClients.createDefault();
                    String jsonBody = new Gson().toJson(parametrosEjecutiva);
                    StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                    HttpPost llamadaEjecutiva = new HttpPost(urlEjecutiva);
                    llamadaEjecutiva.setHeader("Accept", "application/json");
                    llamadaEjecutiva.setEntity(entidadLlamada);
                    log.info("Llamando al servicio " + urlEjecutiva + " ... " + jsonBody);
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
                                log.info("Hay respuesta SIN ERROR del SW");
                                respuestaJson = new JSONObject(respuestaString);
                            String numLiquidacionEjec = respuestaJson.getString("numLiquidacion");
                            log.info("  --  enviarEjecutivaSubvenciones  -- Liquidación generada en Ejecutiva - " + numLiquidacionEjec);
                            detalleError = "Liquidación generada en Ejecutiva - " + numLiquidacionEjec;
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
                                campoSuplementarioIdDeuda.setValorTexto(numLiquidacionEjec);
                                gestorCampoSup.grabarCampoSuplementario(campoSuplementarioIdDeuda);
                                log.info(codCampoIdDeudaVia + ": " + numLiquidacionEjec);
                            } catch (Exception e) {
                                log.error("  --  enviarEjecutivaSubvenciones  -- Error al grabar el número de la Liquidación en Ejecutiva. " + numLiquidacionEjec + " - " + e.getLocalizedMessage());
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
                            detalleError = "Error de ZORKU: " + msgErrorCas;
                                codOperacion = "1";
                                break;
                            default:
                                //Resultado con error
                            log.error("  --  enviarEjecutivaSubvenciones  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                            detalleError = "Error de ZORKU: " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase();
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
                log.error("  --  enviarEjecutivaSubvenciones  --error estadoDeuda: La deuda ya ha sido pagada. No es posible enviarla a vía ejecutiva");
                detalleError = "Error de validación: La deuda ya ha sido pagada. No es posible enviarla a vía ejecutiva";
            }
        } catch (Exception e) {
            //Resultado con error
            log.error("  --  enviarEjecutivaSubvenciones  --error general ", e);
            detalleError = "excepción general en enviarEjecutivaSubvenciones ";
            codOperacion = "1";
        } finally {
            if (idSecuenciaProcesoProgramado != null) {
                int error = 0;
                if (!"0".equals(codOperacion)) {
                    error = 1;
                }
                MeLanbide92Manager.getInstance().insertarRegistroProcProgViaEjeLIREI(idSecuenciaProcesoProgramado, new Date(), numExpediente, numLiquidacion, detalleError, error, adapt);
            }
        }
        return codOperacion;
    }

    /**
     * Operacion que recupera los datos de conexion a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     * @throws java.sql.SQLException
     */
    public AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
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
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
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

    public void cerrarResultSet(ResultSet resultSet) throws TechnicalException {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new TechnicalException(e.getMessage(), e);
            }
        }
    }

    /**
     * It closes a <code>Statement</code> if not <code>null</code>.
     */
    public void cerrarStatement(Statement statement) throws TechnicalException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new TechnicalException(e.getMessage(), e);
            }
        }
    }
    
    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    public  void retornarJSON(String json, HttpServletResponse response) {
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

    /**
     *
     * @param codOrganizacion
     * @return
     */
    public static ParamsSeguridadZorku getSeguridad(int codOrganizacion) {
        ParamsSeguridadZorku seguridadVO = new ParamsSeguridadZorku();
        String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide92.NOMBRE_MODULO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String usuario = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + nombreModulo + BARRA + ConstantesMeLanbide92.USUARIO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String contrasena = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + nombreModulo + BARRA + ConstantesMeLanbide92.CONTRASENA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String area = ConfigurationParameter.getParameter(codOrganizacion + MODULO_INTEGRACION + nombreModulo + BARRA + ConstantesMeLanbide92.AREA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        log.debug("Usuario: " + usuario);
        log.debug("Contrasena: " + contrasena);
        log.debug("Area: " + area);
        seguridadVO.setArea(area);
        seguridadVO.setUsuario(usuario);
        seguridadVO.setContrasena(contrasena);
        return seguridadVO;
    }

    /* Los tiposDocumento que maneja el Servicio Web de Alta de Deuda no se corresponden con los identificadores de tipo de Documento que maneja Flexia, obtenemos con este método la equivalencia */
    public String traducirTipoDocZorku(String tipoFlexia) {
        log.debug("traducirTipoDocZorku.BEGIN. Documento de Flexia: " + tipoFlexia);

        String tipoCIF = ConfigurationParameter.getParameter(ConstantesMeLanbide92.TIPO_CIF_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String tipoNIE = ConfigurationParameter.getParameter(ConstantesMeLanbide92.TIPO_NIE_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String tipoPasaporte = ConfigurationParameter.getParameter(ConstantesMeLanbide92.TIPO_PASAPORTE_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String tipoOtro = ConfigurationParameter.getParameter(ConstantesMeLanbide92.TIPO_OTRO_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String tipoDNI = ConfigurationParameter.getParameter(ConstantesMeLanbide92.TIPO_DNI_ZK, ConstantesMeLanbide92.FICHERO_PROPIEDADES);

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

    public String traducirIdProvincia(String idFlexia) {
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

    public String traducirIdMunicipio(String idFlexia) {
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
     *
     * @param telefonoTer
     * @return
     */
    public String recuperaCifrasTelefono(String telefonoTer) {
        log.debug("recuperaTelefono del tercero:" + telefonoTer);
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

    /**
     * operacion que valida el IBAN de una cuenta bancaria devuelve si es o no
     * valido
     *
     * @param cuenta
     * @return boolean (IBAN valido)
     */
    public boolean validaIBAN(String cuenta) {
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
    public int restarFechas(Date fechaIn, Date fechaFinal) {
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

    public int dameAcCodZorku(int codArea) {
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
            log.error("Salto excepcion en convierteFechaZorku.");
        }
        return fechaZORKU;
    }

    public String quitarComasImporte(String importe) {
        if (importe.contains(".")) {
            importe = importe.replace(".", "");
        }
        if (importe.contains(",")) {
            importe = importe.replace(",", ".");
        }
        return importe;
    }

    /**
     *
     * @param codOrganizacion
     * @param parametrosAlta
     * @param numExp
     * @return
     */
    public ParamsAltaDeuda getDatosInteresado(int codOrganizacion, ParamsAltaDeuda parametrosAlta, String numExp, MELANBIDE92 melanbidE92) {
        String ejercicio;
        String procedimiento;
        String[] datos = numExp.split(ConstantesDatos.BARRA);
        ejercicio = datos[0];
        procedimiento = datos[1];
        try {
            SalidaIntegracionVO salida = gestorCampoSup.getExpediente(String.valueOf(codOrganizacion), numExp, procedimiento, ejercicio);
            InteresadoExpedienteModuloIntegracionVO interesado = new InteresadoExpedienteModuloIntegracionVO();
            if (salida != null) {
                if (salida.getExpediente() != null) {
                    if ((salida.getExpediente().getInteresados() != null) && (!salida.getExpediente().getInteresados().isEmpty())) {
                        for (int i = 0; i < salida.getExpediente().getInteresados().size(); i++) {
                            interesado = salida.getExpediente().getInteresados().get(i);
                            log.debug("ROL: " + interesado.getCodigoRol());
                            if (interesado.getCodigoRol() == 1) {
                                if (interesado.getNombre() != null) {
                                    parametrosAlta.setZorTerNombre(interesado.getNombre());
                                }
                                if (interesado.getApellido1() != null) {
                                    parametrosAlta.setZorTerApellido1(interesado.getApellido1());
                                }
                                if (interesado.getApellido2() != null) {
                                    parametrosAlta.setZorTerApellido2(interesado.getApellido2());
                                }
                                if (interesado.getEmail() != null) {
                                    parametrosAlta.setZorTerEmail(interesado.getEmail());
                                }
                                if (interesado.getDocumento() != null) {
                                    parametrosAlta.setZorTerNumDocumento(interesado.getDocumento());
                                }
                                if (interesado.getNumeroTelefonoFax() != null) {
                                    //  gerDeTelefono = interesado.getNumeroTelefonoFax();
                                    parametrosAlta.setZorTerTelefono(new BigDecimal(recuperaCifrasTelefono(interesado.getNumeroTelefonoFax())));
                                }
                                parametrosAlta.setZorTerTipoDocumento(traducirTipoDocZorku(String.valueOf(interesado.getTipoDocumento())));
                                break;
                            }
                        }
                    }
                } //if interesado
                // domicilio
                if ((interesado.getDomicilios() != null) && (!interesado.getDomicilios().isEmpty())) {
                    int codDomicilioExpediente = interesado.getCodDomicilioExpediente();
                    log.info("CodDomicilio Expediente " + codDomicilioExpediente);
                    DomicilioInteresadoModuloIntegracionVO domicilio = new DomicilioInteresadoModuloIntegracionVO();
                    for (int i = 0; i < interesado.getDomicilios().size(); i++) {
                        domicilio = interesado.getDomicilios().get(i);
                        String codDom = domicilio.getIdDomicilio();
                        log.debug("codDomicilio " + i + " Interesado " + codDom);
                        if ((Integer.toString(codDomicilioExpediente)).equals(codDom)) {
                            break;
                        } else {
                            domicilio = interesado.getDomicilios().get(0);
                        }
                    } //for domicilios
                    log.info("He recogido el codDomicilio  " + domicilio.getIdDomicilio());
                    parametrosAlta.setZorDirMunicipio(traducirIdMunicipio(domicilio.getIdMunicipio()));
                    parametrosAlta.setZorDirProv(traducirIdProvincia(domicilio.getIdProvincia()));
                    if (parametrosAlta.getZorDirProv() != null) {
                        // OJO DEFINICION SW
                        log.debug("------------ Convierto Provincia a Territorio");
                        if ("48".equals(parametrosAlta.getZorDirProv())) {
                            parametrosAlta.setZorDeTerritorio("1");
                            log.debug("1");
                        } else if ("20".equals(parametrosAlta.getZorDirProv())) {
                            parametrosAlta.setZorDeTerritorio("2");
                            log.debug("2");
                        } else if ("1".equals(parametrosAlta.getZorDirProv()) || "01".equals(parametrosAlta.getZorDirProv())) {
                            parametrosAlta.setZorDeTerritorio("3");
                            log.debug("3");
                        } else {
                            log.error("El c\u00f3digo de provincia " + parametrosAlta.getZorDirProv() + " no es de un territorio de la CAV");
                            parametrosAlta.setZorDeTerritorio("4");
                        }
                    }
                    //        parametrosAlta.setGerDeNombreCalle(domicilio.getDescripcionVia());
                    //  deudaVO.setGerDeNoraViaPublicaId(String.valueOf(domicilio.getTipoVia()));
                    String tipoViaNora = ConfigurationParameter.getParameter("CodigoVia/Flexia/" + domicilio.getTipoVia(), ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                    String zorDirDireccionCompleta = (tipoViaNora != null && !"".equals(tipoViaNora) ? tipoViaNora : ConstantesMeLanbide92.TIPO_VIA_DEFECTO) + " " + domicilio.getDescripcionVia() + " " + (domicilio.getNumDesde() != null ? domicilio.getNumDesde() + " " : "") + (domicilio.getPortal() != null && !domicilio.getPortal().isEmpty() ? domicilio.getPortal() + " " : "") + (domicilio.getBloque() != null && !domicilio.getBloque().isEmpty() ? domicilio.getBloque() + " " : "") + (domicilio.getEscalera() != null && !domicilio.getEscalera().isEmpty() ? domicilio.getEscalera() + " " : "") + (domicilio.getPlanta() != null && !domicilio.getPlanta().isEmpty() ? domicilio.getPlanta() + " " : "") + (domicilio.getPuerta() != null && !domicilio.getPuerta().isEmpty() ? domicilio.getPuerta() : "");
                    parametrosAlta.setZorDirDireccionCompleta(zorDirDireccionCompleta);
                    parametrosAlta.setZorDirCp(domicilio.getCodigoPostal());
                } //salida.getExpediente() != null
            } //salida != null
        } catch (Exception e) {
            log.error("Se ha producido un error al recuperar los datos del tercero. " + e.getMessage());
        }
        return parametrosAlta;
    }
}
