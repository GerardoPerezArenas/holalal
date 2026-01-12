/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide18;

import com.google.gson.Gson;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide18.manager.MeLanbide18Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide18.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide18.util.ConstantesMeLanbide18;
import es.altia.flexia.integracion.moduloexterno.melanbide18.vo.FilaDeudaFraccVO;
import es.altia.flexia.integracion.moduloexterno.melanbide18.vo.DeudaZorkuVO;
import es.altia.flexia.integracion.moduloexterno.melanbide18.vo.ParamsAltaFraccionamiento;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.InteresadoExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
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

public class MELANBIDE18 extends ModuloIntegracionExterno {

    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE18.class);
    // ERRORES
    private static final String OPERACION_CORRECTA = "0";
    private static final String ERROR_OPERACION = "1";
    private static final String ERROR_LLAMADA_FRACCIONAR = "10";
    private static final String ERROR_RECUPERANDO_IBAN = "11";
    private static final String ERROR_RECUPERANDO_ENTIDAD = "12";
    private static final String ERROR_RECUPERANDO_SUCURSAL = "13";
    private static final String ERROR_RECUPERANDO_DC = "14";
    private static final String ERROR_RECUPERANDO_NUM_CUENTA = "15";
    private static final String ERROR_CUENTA_TAMANIO = "16";
    private static final String ERROR_CUENTA_INCORRECTA = "17";
    private static final String ERROR_LISTA_EXPEDIENTES = "18";
    private static final String ERROR_RECUPERANDO_TELEFONO = "19";
    private static final String ERROR_RECUPERANDO_DOCUMENTO = "20";
    private static final String ERROR_LLAMADA_CAMBIO_CC = "21";
    private static final String ERROR_RESPUESTA_CAMBIO_CC = "22";
    private static final String ERROR_RESPUESTA_FRACCIONAR = "23";
    private static final String ERROR_FRACCIONAR_TODAS = "24";
    private static final String ERROR_404 = "404";

    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        log.info("cargarExpedienteExtension begin");
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaDeudasFracc(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaDeudasFracc - " + numExpediente);
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide18/deudasFracc.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaDeudaFraccVO> listaDeudasFracc = MeLanbide18Manager.getInstance().getDeudasFracc(numExpediente, codOrganizacion, adapt);
                if (!listaDeudasFracc.isEmpty()) {
                    request.setAttribute("listaDeudasFracc", listaDeudasFracc);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos de deudas - MELANBIDE18 - cargarPantallaDeudasFracc", ex);
            }
        }
        return url;
    }

    public String consultarDeudasZorku(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en consultarDeudas  - " + numExpediente);
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
                }
        String url = "/jsp/extension/melanbide18/deudas.jsp";
        String numExp = request.getParameter("numExp");
//        request.setAttribute("numExp", numExpediente);
                request.setAttribute("numExp", numExp);
        String codCampoTodas = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_TODAS, ConstantesMeLanbide18.FICHERO_PROPIEDADES);

        if (adapt != null) {
        try {
                List<DeudaZorkuVO> listaDeudas = MeLanbide18Manager.getInstance().getDeudasZorku(numExp, codOrganizacion, adapt);
                if (listaDeudas != null) {
                    request.setAttribute("listaDeudas", listaDeudas);
                }
                IModuloIntegracionExternoCamposFlexia gestorCampoSup = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
                SalidaIntegracionVO campoTodas = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), numExp.split(ConstantesMeLanbide18.BARRA)[0], numExp, numExp.split(ConstantesMeLanbide18.BARRA)[1], codCampoTodas, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
                if (campoTodas.getStatus() == 0) {
                    request.setAttribute("fraccionarTodas", campoTodas.getCampoSuplementario().getValorDesplegable());
            }

        } catch (Exception ex) {
                log.error("Error al recuperar los datos de deudas - MELANBIDE18 - consultarDeudas", ex);
            }
        }
        return url;
    }

    public void crearNuevaDeudaFracc(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevaDeudaFracc - " + numExpediente);
        String codigoOperacion = "-1";
        boolean insertOK = false;
        
        List<FilaDeudaFraccVO> lista = new ArrayList<FilaDeudaFraccVO>();
        MeLanbide18Manager meLanbide18Manager = MeLanbide18Manager.getInstance();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");
            String idsLiquidaciones = request.getParameter("idsLiquidaciones");
            final String[] listNumLiquidaciones = idsLiquidaciones != null && !idsLiquidaciones.isEmpty() ? idsLiquidaciones.split(",") : null;

            if (listNumLiquidaciones != null) {
                final List<Long> liquidaciones = new ArrayList<Long>();
                
                for (final String idLiq : listNumLiquidaciones) {
                    liquidaciones.add(Long.valueOf(idLiq));
                }
                
                insertOK = meLanbide18Manager.crearDeudaFracc(numExp, liquidaciones, codOrganizacion, adapt);                
            }

            if (insertOK) {
                log.debug("deuda fraccionada insertada correctamente");
                codigoOperacion = "0";
                lista = meLanbide18Manager.getDeudasFracc(numExp, codOrganizacion, adapt);
            } else {
                log.debug("NO se ha insertado correctamente la nueva deuda fraccionada");
                codigoOperacion = "9";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("listaDeudasFracc", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void eliminarDeudaFracc(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarDeudaFracc - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaDeudaFraccVO> lista = new ArrayList<FilaDeudaFraccVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");

            if (id == null || id.isEmpty()) {
                log.debug("No se ha recibido desde la JSP el id de la deuda fraccionada a eliminar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide18Manager.getInstance().eliminarDeudaFracc(Integer.valueOf(id), adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide18Manager.getInstance().getDeudasFracc(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de deudas despu?s de eliminar una deuda");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una deuda fraccionado: " + ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public String fraccionarDeudas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente/*, HttpServletRequest request, HttpServletResponse response*/) throws Exception {
        log.info("===========>  fraccionarDeudas ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_OPERACION;
        String ejercicio = "";
        String codProcedimiento = "";
        String fraccionarTodas = null;
        boolean notificacionPostal = false;

        String urlFraccionamiento = ConfigurationParameter.getParameter(ConstantesMeLanbide18.URL_FRACCIONAMIENTO, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String usuario = ConfigurationParameter.getParameter(ConstantesMeLanbide18.USUARIO_ZORKU, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String pass = ConfigurationParameter.getParameter(ConstantesMeLanbide18.PASS_ZORKU, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String numDocumento = null;
        List<String> expedientesFraccionar = null;
        BigDecimal telefono = null;
        String email = null;
        BigDecimal cuota = null;
        String CTAIBAN = null;
        String CTAENTIDAD = null;
        String CTASUCURSAL = null;
        String CTADC = null;
        String CTANUMERO = null;

        AdaptadorSQLBD adapt = null;

        String codCampoIban = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTAIBAN, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTAENTIDAD, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoSucursal = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTASUCURSAL, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoDC = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTADC, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoNumeroCuenta = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTANUMERO, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoCuota = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CUOTA, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoTodas = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_TODAS, ConstantesMeLanbide18.FICHERO_PROPIEDADES);

        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(ConstantesMeLanbide18.BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            log.debug("  --  fraccionarDeudas  --   parametrosLlamada ");
            ParamsAltaFraccionamiento parametrosLlamada = new ParamsAltaFraccionamiento();

            // Datos del INTERESADO -
            IModuloIntegracionExternoCamposFlexia gestorCampoSup = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            SalidaIntegracionVO salida = gestorCampoSup.getExpediente(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio);
            log.debug("  --  fraccionarDeudas  --   INTERESADO ");

            InteresadoExpedienteModuloIntegracionVO interesado = new InteresadoExpedienteModuloIntegracionVO();
            if (salida != null) {
                if (salida.getExpediente() != null) {
                    if ((salida.getExpediente().getInteresados() != null) && (!salida.getExpediente().getInteresados().isEmpty())) {
                        for (int i = 0; i < salida.getExpediente().getInteresados().size(); i++) {
                            interesado = salida.getExpediente().getInteresados().get(i);
                            if (interesado.getCodigoRol() == 1) {
                                log.debug("ROL: " + interesado.getCodigoRol());
                                if (interesado.getDocumento() != null) {
                                    numDocumento = interesado.getDocumento();
                                } else {
                                    log.error("No se ha recuperado el Documento de identidad del interesado");
                                    return ERROR_RECUPERANDO_DOCUMENTO;
                                }
                                if (interesado.getEmail() != null) {
                                    email = interesado.getEmail();
                                } else {
                                    log.debug("Sin MAIL");
                                }
                                if (interesado.getNumeroTelefonoFax() != null) {
                                    String tlfStr = recuperaCifrasTelefono(interesado.getNumeroTelefonoFax());
                                    telefono = new BigDecimal(tlfStr);
                                } else {
                                    log.debug("Sin telefono");
                                }
                            }
                        }
                    }
                }
            }

            SalidaIntegracionVO campoIban = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoIban, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoEntidad = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoEntidad, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoSucursal = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoSucursal, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoDC = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoDC, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoNumero = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoNumeroCuenta, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoCuota = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoCuota, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
            SalidaIntegracionVO campoTodas = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoTodas, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);

            if (campoIban.getStatus() == 0) {
                CTAIBAN = campoIban.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  fraccionarDeudas  --   Falta IBAN ");
                return ERROR_RECUPERANDO_IBAN;
            }
            if (campoEntidad.getStatus() == 0) {
                CTAENTIDAD = campoEntidad.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  fraccionarDeudas  --  Falta ENTIDAD ");
                return ERROR_RECUPERANDO_ENTIDAD;
            }
            if (campoSucursal.getStatus() == 0) {
                CTASUCURSAL = campoSucursal.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  fraccionarDeudas  --  Falta SUCURSAL ");
                return ERROR_RECUPERANDO_SUCURSAL;
            }
            if (campoDC.getStatus() == 0) {
                CTADC = campoDC.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  fraccionarDeudas  --  Falta DC ");
                return ERROR_RECUPERANDO_DC;
            }
            if (campoNumero.getStatus() == 0) {
                CTANUMERO = campoNumero.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  fraccionarDeudas  --  Falta Nº CUENTA ");
                return ERROR_RECUPERANDO_NUM_CUENTA;
            }
            // comprobar el tamaño de los campos de la cuenta
            if (CTAIBAN.length() != 4 || CTAENTIDAD.length() != 4 || CTASUCURSAL.length() != 4 || CTADC.length() != 2 || CTANUMERO.length() != 10) {
                log.error("  --  fraccionarDeudas  --  El numero de cuenta no es valido - El tamaño de algún dato es incorrecto");
                return ERROR_CUENTA_TAMANIO;
            }
            // validar IBAN completo
            String ibanCompleto = CTAIBAN + CTAENTIDAD + CTASUCURSAL + CTADC + CTANUMERO;
            if (!validaIBAN(ibanCompleto)) {
                log.error("  --  fraccionarDeudas  --  El numero de cuenta no es valido");
                return ERROR_CUENTA_INCORRECTA;
            }

            if (campoTodas.getStatus() == 0) {
                fraccionarTodas = campoTodas.getCampoSuplementario().getValorDesplegable();
            } else {
                log.error("  --  fraccionarDeudas  -- No se han recuperado el campo Fraccionar Toas");
                return ERROR_FRACCIONAR_TODAS;
            }

            // compruebo tipo notificacion
            if (!MeLanbide18Manager.getInstance().tieneMarcaTelematica(codOrganizacion, numExpediente, adapt)) {
                log.debug("Notificación POSTAL");
                notificacionPostal = true;
            }
            log.debug("  --  fraccionarDeudas  --   lleno el objeto ");

            // lleno el objeto
            // seguridad
            parametrosLlamada.setZorUsUsuario(usuario);
            parametrosLlamada.setZorUsPassword(pass);
            // interesado
            parametrosLlamada.setNumDocumento(numDocumento);
            if (telefono != null) {
                parametrosLlamada.setTelefono(telefono);
            } else {
                log.error("  --  fraccionarDeudas  -- No se ha recuperado el teléfono");
                return ERROR_RECUPERANDO_TELEFONO;
            }
            if (email != null) {
                parametrosLlamada.setEmail(email);
            }
            // deudas
            expedientesFraccionar = MeLanbide18Manager.getInstance().getListaExpedientesFraccionar(numExpediente, adapt);
            if (fraccionarTodas != null) {
                if (fraccionarTodas.equalsIgnoreCase("N")) {

                }

                if (!expedientesFraccionar.isEmpty()) {
                    parametrosLlamada.setListExpedientes(expedientesFraccionar);
                } else {
                    log.error("  --  fraccionarDeudas  -- No se han recuperado los expedientes  a fraccionar");
                    return ERROR_LISTA_EXPEDIENTES;
                }
            } else {
            }

            if (campoCuota.getStatus() == 0) {
                cuota = new BigDecimal(campoCuota.getCampoSuplementario().getValorNumero());
                parametrosLlamada.setCuotaSolicitada(cuota);
            }
            // datos bancarios
            parametrosLlamada.setZorCbtIban(CTAIBAN);
            parametrosLlamada.setZorCbtEntidad(CTAENTIDAD);
            parametrosLlamada.setZorCbtSucursal(CTASUCURSAL);
            parametrosLlamada.setZorCbtDc(CTADC);
            parametrosLlamada.setZorCbtNumCuenta(CTANUMERO);

            /**
             * Modificacion Agosto 2025 En el alta de fraccionamiento de
             * Subvenciones hemos añadido dos parámetros nuevos:
             * expFraccionamiento : el expediente de fraccionamiento de la
             * subvención. Lo necesitamos para pintarlo en la resolución.
             * notificacionPostal: boolean - pasar a true si tenemos que
             * notificar la resolución por NOTIFICA o si no a false.
             */
            parametrosLlamada.setExpFraccionamiento(numExpediente);
            parametrosLlamada.setNotificacionPostal(notificacionPostal);

            // llamada
            log.info("  --  fraccionarDeudas  -- Parámetros llamada a ZORKU: " + parametrosLlamada.toString());
            try {
                log.debug("  --  fraccionarDeudas  --   Cliente ");
                HttpClient client = HttpClients.createDefault();
                log.debug("  --  fraccionarDeudas  --   request " + urlFraccionamiento);
                HttpPost postRequest = new HttpPost(urlFraccionamiento);
                log.debug("  --  fraccionarDeudas  --   entidad ");
                // Serializar el objeto a JSON
                String jsonBody = new Gson().toJson(parametrosLlamada);
                // Lo pasas a la entidad para la request
                StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                postRequest.setHeader("Accept", "application/json");

                log.info("  --  fraccionarDeudas  --   request.set ENTIDAD " + jsonBody);
                postRequest.setEntity(entidadLlamada);
                
                // llamada a la API
                log.info("  --  fraccionarDeudas  --  llamada a la API");
                HttpResponse respuesta = client.execute(postRequest);
                
                // recojo respuesta
                log.info("  --  fraccionarDeudas  --  getStatusLine() : " + respuesta.getStatusLine());
                int statusCode = respuesta.getStatusLine().getStatusCode();
                log.debug("  --  fraccionarDeudas  --  getStatusLine().getStatusCode() : " + statusCode);
                log.info("  --  fraccionarDeudas  --  Hay respuesta de ZORKU");
                HttpEntity entidadRespuesta = respuesta.getEntity();
                // convierto la respuesta a STRING
                String respuestaString = EntityUtils.toString(entidadRespuesta);
                log.debug("  --  fraccionarDeudas  --  Respuesta String: " + respuestaString);
                //   if (!respuestaString.isEmpty()) {
                // convierto a JSON
                JSONObject respuestaJson;

                switch (statusCode) {
                    case HttpStatus.SC_OK:
                        respuestaJson = new JSONObject(respuestaString);
                        boolean valido = respuestaJson.getBoolean("valido");
                        if (valido) {
                            log.info("  --  fraccionarDeudas  -- VALIDO");
                        }
                        log.info("  --  fraccionarDeudas  -- FRACCionamiento OK");
                        codOperacion = OPERACION_CORRECTA;
                        break;
                    case HttpStatus.SC_BAD_REQUEST:
                        respuestaJson = new JSONObject(respuestaString);
                        String codError = respuestaJson.getString("codigoError");
                        String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                        String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                        log.error("  --  fraccionarDeudas  -- Error  al llamar a la API de fraccionamiento - " + codError);
                        log.error("  --  fraccionarDeudas  -- " + msgErrorCas);
                        log.error("  --  fraccionarDeudas  -- " + msgErrorEus);
//                            codOperacion = msgErrorCas;
                        codOperacion = ERROR_RESPUESTA_FRACCIONAR;
                        break;
                    case HttpStatus.SC_NOT_FOUND:
                        log.error("  --  fraccionarDeudas  -- Error 404 en fraccionamiento al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        return ERROR_404;
                    default:
                        log.error("  --  fraccionarDeudas  -- Error " + statusCode + " al llamar a la API de fraccionamiento. " + respuesta.getStatusLine().getReasonPhrase());
                    return ERROR_LLAMADA_FRACCIONAR;
                }
//                } else {
//                    log.error("  --  fraccionarDeuda  -- Respuesta vacía de la API ");
//                    return ERROR_LLAMADA_FRACCIONAR;
//                }

            } catch (IOException e) {
                log.error("  --  fraccionarDeudas  -- IOException  al llamar a la API de fraccionamiento. " + e.getMessage());
                return ERROR_LLAMADA_FRACCIONAR;
            } catch (UnsupportedCharsetException e) {
                log.error("  --  fraccionarDeudas  -- UnsupportedCharsetException  al llamar a la API de fraccionamiento. " + e.getMessage());
                return ERROR_LLAMADA_FRACCIONAR;
            } catch (ParseException e) {
                log.error("  --  fraccionarDeudas  -- ParseException  al llamar a la API de fraccionamiento. " + e.getMessage());
                return ERROR_LLAMADA_FRACCIONAR;
            } catch (JSONException e) {
                log.error("  --  fraccionarDeudas  -- JSONException  al llamar a la API de fraccionamiento. " + e.getMessage());
                return ERROR_LLAMADA_FRACCIONAR;
            }

        } catch (Exception e) {
            log.error("  --  fraccionarDeudas  -- Excepción genérica operación. " + e.getMessage());
            return codOperacion;
        }

        //Si todo va bien, todo correcto, devolvemos 0
        log.info("  --  fraccionarDeudas  -- .Devolvemos: " + codOperacion);
        return codOperacion;
    }

    public String cambioCuentaFraccionamiento(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("===========>  cambio Cuenta Fraccionamiento ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_OPERACION;
        String ejercicio = "";
        String codProcedimiento = "";

        String urlCambioCuenta = ConfigurationParameter.getParameter(ConstantesMeLanbide18.URL_CAMBIO_CUENTA, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String usuario = ConfigurationParameter.getParameter(ConstantesMeLanbide18.USUARIO_ZORKU, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String pass = ConfigurationParameter.getParameter(ConstantesMeLanbide18.PASS_ZORKU, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String numDocumento = null;
        String CTAIBAN = null;
        String CTAENTIDAD = null;
        String CTASUCURSAL = null;
        String CTADC = null;
        String CTANUMERO = null;
        String codCampoIban = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTAIBAN, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTAENTIDAD, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoSucursal = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTASUCURSAL, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoDC = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTADC, ConstantesMeLanbide18.FICHERO_PROPIEDADES);
        String codCampoNumeroCuenta = ConfigurationParameter.getParameter(ConstantesMeLanbide18.CAMPO_CTANUMERO, ConstantesMeLanbide18.FICHERO_PROPIEDADES);

        AdaptadorSQLBD adapt = null;

        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(ConstantesMeLanbide18.BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            log.debug("  --  cambio Cuenta Fraccionamiento  --   parametrosLlamada ");
            ParamsAltaFraccionamiento parametrosLlamada = new ParamsAltaFraccionamiento();
            // Datos del INTERESADO -
            IModuloIntegracionExternoCamposFlexia gestorCampoSup = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            numDocumento = MeLanbide18Manager.getInstance().getDocumentoInteresado(numExpediente, codOrganizacion, adapt);
            SalidaIntegracionVO campoIban = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoIban, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoEntidad = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoEntidad, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoSucursal = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoSucursal, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoDC = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoDC, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoNumero = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoNumeroCuenta, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

            // control datos
            if (numDocumento == null || numDocumento.isEmpty()) {
                log.error("  --  cambio Cuenta Fraccionamiento  --  No se ha recuperado el Documento de identidad del interesado");
                return ERROR_RECUPERANDO_DOCUMENTO;
            }
            if (campoIban.getStatus() == 0) {
                CTAIBAN = campoIban.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambio Cuenta Fraccionamiento  --   Falta IBAN ");
                return ERROR_RECUPERANDO_IBAN;
            }
            if (campoEntidad.getStatus() == 0) {
                CTAENTIDAD = campoEntidad.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambio Cuenta Fraccionamiento  --  Falta ENTIDAD ");
                return ERROR_RECUPERANDO_ENTIDAD;
            }
            if (campoSucursal.getStatus() == 0) {
                CTASUCURSAL = campoSucursal.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambio Cuenta Fraccionamiento  --  Falta SUCURSAL ");
                return ERROR_RECUPERANDO_SUCURSAL;
            }
            if (campoDC.getStatus() == 0) {
                CTADC = campoDC.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambio Cuenta Fraccionamiento  --  Falta DC ");
                return ERROR_RECUPERANDO_DC;
            }
            if (campoNumero.getStatus() == 0) {
                CTANUMERO = campoNumero.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambio Cuenta Fraccionamiento  --  Falta Nº CUENTA ");
                return ERROR_RECUPERANDO_NUM_CUENTA;
            }
            // comprobar el tamaño de los campos de la cuenta
            if (CTAIBAN.length() != 4 || CTAENTIDAD.length() != 4 || CTASUCURSAL.length() != 4 || CTADC.length() != 2 || CTANUMERO.length() != 10) {
                log.error("  --  cambio Cuenta Fraccionamiento  --  El numero de cuenta no es valido - El tamaño de algún dato es incorrecto");
                return ERROR_CUENTA_TAMANIO;
            }
            // validar IBAN completo
            String ibanCompleto = CTAIBAN + CTAENTIDAD + CTASUCURSAL + CTADC + CTANUMERO;
            if (!validaIBAN(ibanCompleto)) {
                log.error("  --  cambio Cuenta Fraccionamiento  --  El numero de cuenta no es valido");
                return ERROR_CUENTA_INCORRECTA;
            }

            // lleno el objeto
            // seguridad
            parametrosLlamada.setZorUsUsuario(usuario);
            parametrosLlamada.setZorUsPassword(pass);
            // interesado
            parametrosLlamada.setNumDocumento(numDocumento);
            // datos bancarios
            parametrosLlamada.setZorCbtIban(CTAIBAN);
            parametrosLlamada.setZorCbtEntidad(CTAENTIDAD);
            parametrosLlamada.setZorCbtSucursal(CTASUCURSAL);
            parametrosLlamada.setZorCbtDc(CTADC);
            parametrosLlamada.setZorCbtNumCuenta(CTANUMERO);

            // llamada
            log.info("  --  cambio Cuenta Fraccionamiento  -- Parámetros llamada a ZORKU: " + parametrosLlamada.toString());
            try {
                log.debug("  --  cambio Cuenta Fraccionamiento  --   Cliente ");
                HttpClient client = HttpClients.createDefault();
                log.debug("  --  cambio Cuenta Fraccionamiento  --   request " + urlCambioCuenta);
                HttpPost postRequest = new HttpPost(urlCambioCuenta);
                log.debug("  --  cambio Cuenta Fraccionamiento  --   entidad ");
                // Serializar el objeto a JSON
                String jsonBody = new Gson().toJson(parametrosLlamada);
                // Lo pasas a la entidad para la request
                StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                postRequest.setHeader("Accept", "application/json");

                log.info("  --  cambio Cuenta Fraccionamiento  --   request.set ENTIDAD " + jsonBody);
                postRequest.setEntity(entidadLlamada);

                // llamada a la API
                log.info("  --  cambio Cuenta Fraccionamiento  --  llamada a la API");
                HttpResponse respuesta = client.execute(postRequest);

                // recojo respuesta
                log.info("  --  cambio Cuenta Fraccionamiento  --  getStatusLine() : " + respuesta.getStatusLine());
                int statusCode = respuesta.getStatusLine().getStatusCode();
                log.debug("  --  cambio Cuenta Fraccionamiento  --  getStatusLine().getStatusCode() : " + statusCode);
                log.info("  --  cambio Cuenta Fraccionamiento  --  Hay respuesta de ZORKU");
                HttpEntity entidadRespuesta = respuesta.getEntity();
                // convierto la respuesta a STRING
                String respuestaString = EntityUtils.toString(entidadRespuesta);
                log.debug("  --  cambio Cuenta Fraccionamiento  --  Respuesta String: " + respuestaString);
//                if (!respuestaString.isEmpty()) {
                // convierto a JSON
                JSONObject respuestaJson;

                switch (statusCode) {
                    case HttpStatus.SC_OK:
                        respuestaJson = new JSONObject(respuestaString);
                        boolean valido = respuestaJson.getBoolean("valido");
                        if (valido) {
                            log.info("  --  cambio Cuenta Fraccionamiento  -- VALIDO");
                        }
                        log.info("  --  cambio Cuenta Fraccionamiento  --  OK");
                        codOperacion = OPERACION_CORRECTA;
                        break;
                    case HttpStatus.SC_BAD_REQUEST:
                        respuestaJson = new JSONObject(respuestaString);
                        String codError = respuestaJson.getString("codigoError");
                        String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                        String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                        log.error("  --  cambio Cuenta Fraccionamiento  -- Error  al llamar a la API de fraccionamiento - " + codError);
                        log.error("  --  cambio Cuenta Fraccionamiento  -- " + msgErrorCas);
                        log.error("  --  cambio Cuenta Fraccionamiento  -- " + msgErrorEus);
                        codOperacion = ERROR_RESPUESTA_CAMBIO_CC;
                        break;
                    case HttpStatus.SC_NOT_FOUND:
                        log.error("  --  cambio Cuenta Fraccionamiento  -- Error 404 al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        return ERROR_404;
                    default:
                        log.error("  --  cambio Cuenta Fraccionamiento  -- Error al llamar a la API de fraccionamiento. " + statusCode);
                        return ERROR_LLAMADA_CAMBIO_CC;
                }
//                } else {
//                    log.error("  --  fraccionarDeuda  -- Respuesta vacía de la API ");
//                    return ERROR_LLAMADA_CAMBIO_CC;
//                }
            } catch (IOException e) {
                log.error("  --  cambio Cuenta Fraccionamiento  -- IOException  al llamar a la API de fraccionamiento. " + e.getMessage());
                return ERROR_LLAMADA_CAMBIO_CC;
            } catch (UnsupportedCharsetException e) {
                log.error("  --  cambio Cuenta Fraccionamiento  -- UnsupportedCharsetException  al llamar a la API de fraccionamiento. " + e.getMessage());
                return ERROR_LLAMADA_CAMBIO_CC;
            } catch (ParseException e) {
                log.error("  --  cambio Cuenta Fraccionamiento  -- ParseException  al llamar a la API de fraccionamiento. " + e.getMessage());
                return ERROR_LLAMADA_CAMBIO_CC;
            } catch (JSONException e) {
                log.error("  --  cambio Cuenta Fraccionamiento  -- JSONException  al llamar a la API de fraccionamiento. " + e.getMessage());
                return ERROR_LLAMADA_CAMBIO_CC;
            }

        } catch (Exception e) {
            log.error("  --  cambio Cuenta Fraccionamiento  -- Excepción genérica operación. " + e.getMessage());
            return ERROR_LLAMADA_CAMBIO_CC;
        }
        //Si todo va bien, todo correcto, devolvemos 0
        log.info("  --  cambio Cuenta Fraccionamiento  -- .Devolvemos: " + codOperacion);
        return codOperacion;
    }

    /**
     * Metodo llamado para devolver un String en formato JSON al cliente que ha
     * realiza la peticion a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a traves del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json, HttpServletResponse response) {
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
     * Operacion que recupera los datos de conexion a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
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
                log.debug("He cogido el jndi: " + jndiGenerico);
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

    /**
     * operacion que valida el IBAN de una cuenta bancaria devuelve si es o no
     * valido
     *
     * @param cuenta
     * @return boolean (IBAN valido)
     */
    private boolean validaIBAN(String cuenta) {
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

    private static String recuperaCifrasTelefono(String telefonoTer) {
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
}
