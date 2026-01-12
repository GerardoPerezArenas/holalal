package es.altia.flexia.integracion.moduloexterno.melanbide92;

import com.google.gson.Gson;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.flexia.integracion.moduloexterno.melanbide92.manager.MeLanbide92Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConstantesMeLanbide92;
import es.altia.flexia.integracion.moduloexterno.melanbide92.util.MeLanbide92Utilidades;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsAltaDeuda;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsAltaFraccionamiento;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsAnularDeuda;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsCambioCuenta;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsFinalizarSuspensionPeriodo;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsSeguridadZorku;
import es.altia.flexia.integracion.moduloexterno.melanbide92.vo.ParamsSuspenderPeriodoPago;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class MELANBIDE92 extends ModuloIntegracionExterno {

    //Logger
    private static final Logger log = LogManager.getLogger(MELANBIDE92.class);
    private static final MeLanbide92Utilidades m92Utils = new MeLanbide92Utilidades();
    private final IModuloIntegracionExternoCamposFlexia gestorCampoSup = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

    //Codigos de error
    private static final String OPERACION_CORRECTA = "0";
    private static final String ERROR_LLAMADA_ALTA_CARTA = "1";
    private static final String ERROR_LLAMADA_ACTUALIZAR = "2";
    private static final String ERROR_LLAMADA_ANULAR = "3";
    private static final String ERROR_ANIO_CONVOCATORIA = "4";
    private static final String ERROR_COD_SUBVENCION = "5";
    private static final String ERROR_DATOS_SIN_GUARDAR = "6";
    private static final String ERROR_RECUPERANDO_SUPLEMENTARIOS = "7";
    private static final String ERROR_RECUPERANDO_IMPORTE_INI = "8";
    private static final String ERROR_RECUPERANDO_IMPORTE_RES = "9";
    private static final String ERROR_RECUPERANDO_IMPORTE_SUS = "10";
    private static final String ERROR_RECUPERANDO_IMP_PENDIENTE = "11";
    private static final String ERROR_CONVIRTIENDO_IMPORTE_DEUDA = "12";
    private static final String ERROR_RECUPERANDO_FECHA_RESOLUCION = "13";
    private static final String ERROR_RECUPERANDO_FECHA_SUSPENSION = "14";
    private static final String ERROR_RECUPERANDO_FECHA_ACTIVACION = "15";
    private static final String ERROR_RECUPERANDO_FECHA_PAGO = "16";
    private static final String ERROR_RECUPERANDO_FECHA_REQ = "17";
    private static final String ERROR_CALCULANDO_FECHA_LIMITE_PAGO = "18";
    private static final String ERROR_RECUPERANDO_ID_DEUDA = "19";
    private static final String ERROR_FASE = "20";
    private static final String ERROR_CODIGO_AREA_WS = "21";
    private static final String ERROR_CODIGO_ACCESO = "22";
    private static final String ERROR_CP = "23";
    private static final String ERROR_DIRECCION = "24";
    private static final String ERROR_MUNICIPIO = "25";
    private static final String ERROR_PROVINCIA = "26";
    private static final String ERROR_TIPO_DOCUMENTO = "27";
    private static final String ERROR_DOCUMENTO = "28";
    private static final String ERROR_BORRANDO_CARTA = "29";
    private static final String ERROR_GRABANDO_CARTA = "30";
    private static final String ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS = "31";
    private static final String ERROR_LLAMADA_FRACCIONAR = "32";
    private static final String ERROR_LLAMADA_SUSPENSION = "33";
    private static final String ERROR_LLAMADA_FIN_SUSP = "34";
    private static final String ERROR_RECUPERANDO_FECHA_SOLI_FRAC = "35";
    private static final String ERROR_RECUPERANDO_FEC_LIMITE_P_RES = "36";
    private static final String ERROR_RECUPERANDO_ID_DEUDA_RES = "37";
    private static final String ERROR_RECUPERANDO_MOT_ANULACION = "38";
    private static final String ERROR_RECUPERANDO_OBS_ANULACION = "39";
    private static final String ERROR_RECUPERANDO_IBAN = "40";
    private static final String ERROR_RECUPERANDO_ENTIDAD = "41";
    private static final String ERROR_RECUPERANDO_SUCURSAL = "42";
    private static final String ERROR_RECUPERANDO_DC = "43";
    private static final String ERROR_RECUPERANDO_NUM_CUENTA = "44";
    private static final String ERROR_RECUPERANDO_ESTADO = "45";
    private static final String ERROR_BORRANDO_FEC_VENC = "46";
    private static final String ERROR_BORRANDO_DEUDA = "47";
    private static final String ERROR_GRABAR_FECHA_SUSPENSION = "48";
    private static final String ERROR_CUENTA_TAMANIO = "49";
    private static final String ERROR_CUENTA_INCORRECTA = "50";
    private static final String ERROR_FECHA_SOL_MAYOR_LIMITE = "51";
    private static final String ERROR_FECHA_SOL_MAYOR_HOY = "52";
    private static final String ERROR_ESTADO_PENDIENTE = "53";
    private static final String ERROR_ESTADO_NO_SUSPENSION = "54";
    private static final String ERROR_RECUPERANDO_FECHA_ACUSE_RES = "55";
    private static final String ERROR_FECHA_ACUSE = "56";
    private static final String ERROR_FECHA_LIMITE = "57";
    private static final String ERROR_LLAMADA_CAMBIO_CUENTA = "58";
    //
    private static final String ERROR_OPERACION = "99";
    private static final String ERROR_403 = "403";
    private static final String ERROR_404 = "404";
    private static final String ERROR_500 = "500";
    private static final String ERROR_503 = "503";

    /**
     * carga de la pestana para generar las Cartas de Pago
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String cargarPestanaAltaDeuda(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("===========>  ENTRA EN cargarPestanaAltaDeuda " + numExpediente);
        String ejercicio = "";
        String codProcedimiento = "";
        int tramite = Integer.parseInt(request.getParameter("codTramite"));
        int ocurrencia = Integer.parseInt(request.getParameter("ocuTramite"));
        boolean liquidacionAbonada = false;
        int codigoExterno = 0;
        String url = "/jsp/extension/melanbide92/pestanaCartaPago.jsp";
        AdaptadorSQLBD adapt = null;
        String codCampoIdDeudaIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoIdDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoIdDeudaSus = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_SUS, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecPagoIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_PAGO_DEUDA_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoImporteDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_IMPORTE_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        SalidaIntegracionVO campoNumLiquidacion = null;

        try {
            MeLanbide92Manager m92Manager = MeLanbide92Manager.getInstance();
            try {
                adapt = m92Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (numExpediente != null && !"".equals(numExpediente)) {
                String[] datos = numExpediente.split(ConstantesDatos.BARRA);
                ejercicio = datos[0];
                codProcedimiento = datos[1];
            }
            if (adapt != null) {
                try {
                    codigoExterno = m92Manager.getCodigoExternoTramite(codOrganizacion, codProcedimiento, String.valueOf(tramite), adapt);
                } catch (Exception e) {
                    log.error("Excepción al obtener el código externo del trámite " + tramite + " en " + codProcedimiento);
                }
            }

            switch (codigoExterno) {
                case 5001:
                    log.debug("5001 - REQUERIMIENTO");
                    request.setAttribute("fase", 1);
                    campoNumLiquidacion = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            codCampoIdDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    if (campoNumLiquidacion != null && campoNumLiquidacion.getStatus() == 0) {
                        request.setAttribute("idDeudaIni", campoNumLiquidacion.getCampoSuplementario().getValorTexto());
                    }
                    break;
                case 5004:
                    log.debug("5004 - RESOLUCIÓN");
                    request.setAttribute("fase", 2);
                    campoNumLiquidacion = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            codCampoIdDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    if (campoNumLiquidacion != null && campoNumLiquidacion.getStatus() == 0) {
                        request.setAttribute("idDeudaIni", campoNumLiquidacion.getCampoSuplementario().getValorTexto());
                    }
                    campoNumLiquidacion = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    if (campoNumLiquidacion != null && campoNumLiquidacion.getStatus() == 0) {
                        request.setAttribute("idDeudaRes", campoNumLiquidacion.getCampoSuplementario().getValorTexto());
                    }

                    // FECHA DE PAGO PERIODO ALEGACIONES
                    SalidaIntegracionVO campoFecPagoAlegaciones = null;
                    campoFecPagoAlegaciones = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            codCampoFecPagoIni, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                    if (campoFecPagoAlegaciones != null && campoFecPagoAlegaciones.getStatus() == 0) {
                        log.info("Tiene Fecha pago Deuda Alegaciones: ");
                        liquidacionAbonada = true;
                        int grabaCero = MeLanbide92Manager.getInstance().guardarValorCampoNumericoTramite(codOrganizacion, numExpediente, tramite, ocurrencia, codCampoImporteDeudaRes, BigDecimal.ZERO, adapt);
                        if (grabaCero > 0) {
                            log.info("Grabado el valor 0 en " + codCampoImporteDeudaRes);
                        } else {
                            log.error("Error al grabar el campo suplementario " + codCampoImporteDeudaRes);

                        }
                        /*
                        CampoSuplementarioModuloIntegracionVO campoSuplementarioImporte = new CampoSuplementarioModuloIntegracionVO();
                        campoSuplementarioImporte.setCodOrganizacion(String.valueOf(codOrganizacion));
                        campoSuplementarioImporte.setCodProcedimiento(codProcedimiento);
                        campoSuplementarioImporte.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                        campoSuplementarioImporte.setTramite(true);
                        campoSuplementarioImporte.setCodTramite(String.valueOf(tramite));
                        campoSuplementarioImporte.setOcurrenciaTramite(String.valueOf(ocurrencia));
                        campoSuplementarioImporte.setNumExpediente(numExpediente);
                        campoSuplementarioImporte.setEjercicio(ejercicio);
                        campoSuplementarioImporte.setCodigoCampo(codCampoImporteDeudaRes);
                        campoSuplementarioImporte.setValorNumero("0");
                        SalidaIntegracionVO grabaCampo = gestorCampoSup.grabarCampoSuplementario(campoSuplementarioImporte);

                        if (grabaCampo.getStatus() != 0) {
                            log.error("Excepción al grabar el campo suplementario " + codCampoImporteDeudaRes);
                        } else {
                            log.info("Grabado el valor 0 en " + codCampoImporteDeudaRes);
                        }*/
                    }
                    request.setAttribute("liquidacionAbonada", liquidacionAbonada);
                    break;

                case 5012:
                    log.debug("5012 - SUSPENSION");
                    request.setAttribute("fase", 3);
                    campoNumLiquidacion = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            codCampoIdDeudaSus, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    if (campoNumLiquidacion != null && campoNumLiquidacion.getStatus() == 0) {
                        request.setAttribute("idDeudaSus", campoNumLiquidacion.getCampoSuplementario().getValorTexto());
                    }
                    break;
                default:
                    log.error("El código de trámite " + tramite + " no corresponde a un trámite con pestaña");
                    break;
            }

            request.setAttribute("numExp", numExpediente);
            request.setAttribute("codigoProcedimiento", codProcedimiento);
            request.setAttribute("ejercicio", ejercicio);
            request.setAttribute("codigoTramite", tramite);
            request.setAttribute("codigoExterno", codigoExterno);
        } catch (Exception ex) {
            log.error("Error general : " + ex.getMessage());
            throw new Exception(ex);
        }
        return url;
    }

    /**
     * metodo invocado desde jsp que comprueba que se han grabado en BBDD los
     * campos del tramite necesarios para generar las deudas
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @throws Exception
     */
    public void validarCampos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codOperacion = ERROR_OPERACION;
        String numExp = request.getParameter("numExp");
        String tramite = request.getParameter("codTramite");
        String ocurrencia = request.getParameter("ocuTramite");
        int fase = Integer.parseInt(request.getParameter("fase"));
        log.info("===========>  ENTRA EN validarCampos - Fase: " + fase);
        String ejercicio = "";
        String codProcedimiento = "";
        SalidaIntegracionVO campoImporteDeuda = null;
        if (numExp != null && !"".equals(numExp)) {
            String[] datos = numExp.split(ConstantesDatos.BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }//if(numExp!=null && !"".equals(numExp))
        try {
            switch (fase) {
                case 1:
                    String cImporteDeudaIni = request.getParameter("importeDeudaIni");
                    String importeDeudaIni = null;
                    String codCampoImporteDeudaIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_IMPORTE_DEUDA_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                    try {
                        cImporteDeudaIni = m92Utils.quitarComasImporte(cImporteDeudaIni);
                        campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoImporteDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                        if (campoImporteDeuda != null && campoImporteDeuda.getStatus() == 0) {
                            try {
                                importeDeudaIni = campoImporteDeuda.getCampoSuplementario().getValorNumero();
                            } catch (Exception e) {
                                log.error("  -- validarCampos -- Excepcion al convertir a numero el importe de la deuda");
                                codOperacion = ERROR_RECUPERANDO_IMPORTE_INI;
                            }
                        }
                        if (importeDeudaIni.equals(cImporteDeudaIni)) {
                            codOperacion = OPERACION_CORRECTA;
                        } else {
                            codOperacion = ERROR_DATOS_SIN_GUARDAR;
                        }
                    } catch (NumberFormatException e) {
                        log.error("  -- validarCampos -- Error general : " + e.getMessage());
                        codOperacion = ERROR_RECUPERANDO_IMPORTE_INI;
                    }
                    break;
                case 2:
                    String cImporteDeudaRes = null;
                    String cFecResolucion = null;
                    String codCampoImporteDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_IMPORTE_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                    String codCampoFecResolucion = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_RESOLUCION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                    String importeDeudaRes = null;
                    String fechaRes = null;
                    SalidaIntegracionVO campoFecResolucion = null;
                    try {
                        cImporteDeudaRes = request.getParameter("importeDeudaRes");
                        cFecResolucion = request.getParameter("fechaResolucion");

                        cImporteDeudaRes = m92Utils.quitarComasImporte(cImporteDeudaRes);
                        campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoImporteDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                        if (campoImporteDeuda != null && campoImporteDeuda.getStatus() == 0) {
                            importeDeudaRes = campoImporteDeuda.getCampoSuplementario().getValorNumero();
                        } else {
                            log.error("  -- validarCampos -- Error al recuperar el Importe de la Deuda en Resolución");
                            codOperacion = ERROR_RECUPERANDO_IMPORTE_RES;
                        }
                        campoFecResolucion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoFecResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        if (campoFecResolucion.getStatus() == 0) {
                            fechaRes = campoFecResolucion.getCampoSuplementario().getValorFechaAsString();
                        } else {
                            log.error("  -- validarCampos -- Error recuperando Fecha Resolución");
                            codOperacion = ERROR_RECUPERANDO_FECHA_RESOLUCION;
                        }

                        if (importeDeudaRes.equals(cImporteDeudaRes) && fechaRes.equals(cFecResolucion)) {
                            codOperacion = OPERACION_CORRECTA;
                        } else {
                            codOperacion = ERROR_DATOS_SIN_GUARDAR;
                        }
                    } catch (NumberFormatException ex) {
                        log.error("  -- validarCampos --  Error general : " + ex.getMessage());
                        codOperacion = ERROR_RECUPERANDO_SUPLEMENTARIOS;
                    }
                    break;
                case 3:
                    String cImporteDeudaSus = request.getParameter("importeDeudaSus");
                    String cFecActivacion = request.getParameter("fechaActivacion");
                    String importeDeudaSus = null;
                    String fechaActivacion = null;
                    String codCampoImporteDeudaSus = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_IMPORTE_DEUDA_SUS, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                    String codCampoFecActivacion = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_ACTIVACION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                    SalidaIntegracionVO campoFecActivacion = null;
                    try {
                        cImporteDeudaSus = m92Utils.quitarComasImporte(cImporteDeudaSus);
                        campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoImporteDeudaSus, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                        if (campoImporteDeuda != null && campoImporteDeuda.getStatus() == 0) {
                            try {
                                importeDeudaSus = campoImporteDeuda.getCampoSuplementario().getValorNumero();
                            } catch (Exception e) {
                                log.error("  -- validarCampos -- Excepcion r al recuperar el Importe de la Deuda generada tras la Suspensión del Periodo de Pago Voluntario");
                                codOperacion = ERROR_RECUPERANDO_IMPORTE_SUS;
                            }
                        } else {
                            codOperacion = ERROR_RECUPERANDO_IMPORTE_SUS;
                        }
                        campoFecActivacion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoFecActivacion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        if (campoFecActivacion.getStatus() == 0) {
                            fechaActivacion = campoFecActivacion.getCampoSuplementario().getValorFechaAsString();
                        } else {
                            log.error("  -- validarCampos -- Error recuperando Fecha de Activación del Periodo de Pago");
                            codOperacion = ERROR_RECUPERANDO_FECHA_ACTIVACION;
                        }
                        if (importeDeudaSus.equals(cImporteDeudaSus) && fechaActivacion.equals(cFecActivacion)) {
                            codOperacion = OPERACION_CORRECTA;
                        } else {
                            codOperacion = ERROR_DATOS_SIN_GUARDAR;
                        }
                    } catch (NumberFormatException ex) {
                        codOperacion = ERROR_RECUPERANDO_SUPLEMENTARIOS;
                        log.error("  -- validarCampos -- Error general : " + ex.getMessage());
                    }
                    break;
                default:
                    codOperacion = ERROR_FASE;
                    break;
            }
        } catch (Exception ex) {
            log.error("  -- validarCampos --  Error general : " + ex.getMessage());
            //  codOperacion = ERROR_BBDD;
        }

            GeneralValueObject resultado = new GeneralValueObject();
            resultado.setAtributo("codigoOperacion", codOperacion);
            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
        }

    /**
     * metodo invocado desde jsp que llama a altaDeudaSubvenciones() de ZORKU
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public String altaDeudaSubvenciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("===========>  altaDeudaSubvenciones ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_OPERACION;
        String numExp = request.getParameter("numExp");
        String tramite = request.getParameter("codTramite");
        String ocurrencia = request.getParameter("ocuTramite");
        int fase = Integer.parseInt(request.getParameter("fase"));
        log.info("Exp: " + numExp + " - Tramite/Ocurrencia: " + tramite + "/" + ocurrencia + " - Fase: " + fase);
        MeLanbide92Manager m92Manager = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;

// Creo los VO necesarios
        GeneralValueObject resultado = new GeneralValueObject();
        ParamsSeguridadZorku seguridadVO = new ParamsSeguridadZorku();

        String zorLiNumLiquidacion = "";
        String numLiquidacionGenerada = "";
        String nombreCarta = null;
        String ejercicio = "";
        String codProcedimiento = "";
        String zorDeCodSubvencion = null;
        String zorLaEjercicio = "";
        Calendar zorLiFechaRequerimiento = null;
        Calendar zorLiFechaResolucion = null;
        Calendar fechaSuspension = null;
        Calendar zorLiFechaLimitePago = null;
        Calendar zorLiFechaPagoExpSubv = null;
        Calendar zorFechaPagoDeudaIni = null;
        BigDecimal zorLiImporteDeuda = null;
        BigDecimal intereses = BigDecimal.ZERO;

        String codigoAreaFlexia = null; //  se utiliza para obtener codigoAreaAcceso
        int zorArCodigo = 0;
        int codTramReq = 0;
        int ocuTramReq = 1;
        int codTramRes = 0;
        int ocuTramRes = 1;

        String urlAltaDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_ALTA_DEUDA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String urlActualizarDom = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_ACTUALIZAR_DOMICILIO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);

        String zorTipoPago = ConfigurationParameter.getParameter(ConstantesMeLanbide92.TIPO_PAGO_DEUDA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String zorEstadoDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide92.ESTADO_DEUDA_PENDIENTE, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoCodigoAreaSW = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CODIGO_AREA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codigoCodAnioConvocatoria = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ANIO_CONVO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecPagoSubv = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_PAGO_EXP_SUBV, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecRequerimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_REQUERIMIENTO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);

        String codCampoIdDeudaIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoCartaPagoIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CARTAPAGO_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoImporteDeudaIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_IMPORTE_DEUDA_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecLimiteIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);

        String codCampoIdDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoCartaPagoRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CARTAPAGO_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoImporteDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_IMPORTE_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecLimiteRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecResolucion = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_RESOLUCION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoIntereses = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_INTERES_DEMORA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecPagoIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_PAGO_DEUDA_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);

        String codCampoIdDeudaSus = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_SUS, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoCartaPagoSus = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CARTAPAGO_SUS, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoImporteDeudaSus = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_IMPORTE_DEUDA_SUS, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecLimiteSus = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_SUS, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecActivacion = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_ACTIVACION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecSuspension = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_SUSPENSION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);

        SalidaIntegracionVO campoFecPagExpSubv = null;
        SalidaIntegracionVO campoFecRequerimiento = null;
        SalidaIntegracionVO campoFecPagDeudaIni = null;
        SalidaIntegracionVO campoFecResolucion = null;
        SalidaIntegracionVO campoFecSuspension = null;
        SalidaIntegracionVO campoFecActivacion = null;
        SalidaIntegracionVO campoCodigoAreaFlexia = null;
        SalidaIntegracionVO campoImporteDeuda = null;
        SalidaIntegracionVO campoNumLiquidacion = null;
        SalidaIntegracionVO campoAnioConvocatoria = null;

        if (numExp != null && !"".equals(numExp)) {
            String[] datos = numExp.split(ConstantesDatos.BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }//if(numExp!=null && !"".equals(numExp))
        try {
            m92Manager = MeLanbide92Manager.getInstance();
            adapt = m92Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adapt.getConnection();
            log.info("  -- altaDeudaSubvenciones -- Llenado objetos del cliente ");
            seguridadVO = MeLanbide92Utilidades.getSeguridad(codOrganizacion);
            // SUPLEMENTARIOS
            try {
                codTramReq = m92Manager.getCodigoInternoTramite(codOrganizacion, codProcedimiento, ConfigurationParameter.getParameter(ConstantesMeLanbide92.TRAMITE_REQUERIMIENTO, ConstantesMeLanbide92.FICHERO_PROPIEDADES), adapt);
                ocuTramReq = m92Manager.getMaxOcurrenciaTramitexCodigo(codOrganizacion, numExp, codTramReq, adapt);
                codTramRes = m92Manager.getCodigoInternoTramite(codOrganizacion, codProcedimiento, ConfigurationParameter.getParameter(ConstantesMeLanbide92.TRAMITE_RESOLUCION, ConstantesMeLanbide92.FICHERO_PROPIEDADES), adapt);
                ocuTramRes = m92Manager.getMaxOcurrenciaTramitexCodigo(codOrganizacion, numExp, codTramRes, adapt);
                switch (fase) {
                    // 5001 - REQUERIMIENTO
                    case 1:
                        log.info("  -- altaDeudaSubvenciones -- Tramite 5001 - REQUERIMIENTO");
                        zorLiFechaRequerimiento = Calendar.getInstance();
                        zorLiFechaLimitePago = zorLiFechaRequerimiento;
                        zorLiFechaLimitePago.add(Calendar.MONTH, 1);

                        campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoImporteDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                        break;
                    // 5004 - RESOLUCION
                    case 2:
                        log.info("  -- altaDeudaSubvenciones -- Tramite 5004 - RESOLUCION");
                        zorLiFechaLimitePago = Calendar.getInstance();
                        zorLiFechaLimitePago.add(Calendar.MONTH, 3);

                        campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoImporteDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                        campoNumLiquidacion = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                codCampoIdDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                        campoFecRequerimiento = gestorCampoSup.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                codTramReq, ocuTramReq, codCampoFecRequerimiento, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        campoFecResolucion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoFecResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        campoFecPagDeudaIni = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                codCampoFecPagoIni, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                        if (campoFecRequerimiento != null && campoFecRequerimiento.getStatus() == 0) {
                            zorLiFechaRequerimiento = campoFecRequerimiento.getCampoSuplementario().getValorFecha();
                        }
                        if (campoFecResolucion.getStatus() == 0) {
                            zorLiFechaResolucion = campoFecResolucion.getCampoSuplementario().getValorFecha();
                        } else {
                            log.error("  -- altaDeudaSubvenciones -- Error recuperando Fecha Resolución");
                            resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_RESOLUCION);
                            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                            return ERROR_RECUPERANDO_FECHA_RESOLUCION;
                        }
                        if (campoFecPagDeudaIni != null && campoFecPagDeudaIni.getStatus() == 0) {
                            zorFechaPagoDeudaIni = campoFecPagDeudaIni.getCampoSuplementario().getValorFecha();
                        }
                        break;
                    // 5014 - SUSPENSION
                    case 3:
                        log.info("  -- altaDeudaSubvenciones -- Tramite 5012 - SUSPENSION");

                        campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoImporteDeudaSus, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                        campoNumLiquidacion = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                        campoFecRequerimiento = gestorCampoSup.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                codTramReq, ocuTramReq, codCampoFecRequerimiento, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        campoFecResolucion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                codTramRes, ocuTramRes, codCampoFecResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        campoFecSuspension = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                codCampoFecSuspension, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        campoFecActivacion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                                Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoFecActivacion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                        if (campoFecRequerimiento.getStatus() == 0) {
                            zorLiFechaRequerimiento = campoFecRequerimiento.getCampoSuplementario().getValorFecha();
                        }
                        if (campoFecResolucion.getStatus() == 0) {
                            zorLiFechaResolucion = campoFecResolucion.getCampoSuplementario().getValorFecha();
                        } else {
                            log.error("  -- altaDeudaSubvenciones -- Error recuperando Fecha Resolución");
                            resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_RESOLUCION);
                            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                            return ERROR_RECUPERANDO_FECHA_RESOLUCION;
                        }
                        if (campoFecSuspension.getStatus() == 0) {
                            fechaSuspension = campoFecSuspension.getCampoSuplementario().getValorFecha();
                        } else {
                            log.error("  -- altaDeudaSubvenciones -- Error recuperando Fecha Suspensión Periodo Pago");
                            resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_SUSPENSION);
                            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                            return ERROR_RECUPERANDO_FECHA_SUSPENSION;
                        }
                        if (campoFecActivacion.getStatus() == 0) {
                            zorLiFechaLimitePago = campoFecActivacion.getCampoSuplementario().getValorFecha();
                        } else {
                            log.error("  -- altaDeudaSubvenciones -- Error recuperando Fecha Activación Periodo Pago");
                            resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_ACTIVACION);
                            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                            return ERROR_RECUPERANDO_FECHA_ACTIVACION;
                        }
                        // calcular FechaLimitePago = fecha activacion + (90 dias - ( fecha suspension - fecha resolucion que genero la carta de pago) + 10 dias)
                        int diasDiferencia = m92Utils.restarFechas(fechaSuspension.getTime(), zorLiFechaRequerimiento.getTime());
                        int diferenciaFinal = 100 + diasDiferencia;
                        zorLiFechaLimitePago.add(Calendar.DAY_OF_YEAR, diferenciaFinal);
                        break;
                    default:
                        log.error("  -- altaDeudaSubvenciones -- El código de trámite no corresponde a un trámite con pestaña");
                        resultado.setAtributo("codigoOperacion", ERROR_FASE);
                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_FASE;
                }
                if (campoNumLiquidacion != null && campoNumLiquidacion.getStatus() == 0) {
                    zorLiNumLiquidacion = campoNumLiquidacion.getCampoSuplementario().getValorTexto();
                } else if (fase != 1) {
                    log.error("  -- altaDeudaSubvenciones -- Error recuperando IdDeuda");
                    resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_ID_DEUDA);
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_RECUPERANDO_ID_DEUDA;
                }
                // DATOS COMUNES
                if (campoImporteDeuda != null && campoImporteDeuda.getStatus() == 0) {
                    try {
                        zorLiImporteDeuda = new BigDecimal(campoImporteDeuda.getCampoSuplementario().getValorNumero());
                    } catch (Exception e) {
                        log.error("  -- altaDeudaSubvenciones --  Excepcion al convertir a numero el importe de la deuda");
                        resultado.setAtributo("codigoOperacion", ERROR_CONVIRTIENDO_IMPORTE_DEUDA);
                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_CONVIRTIENDO_IMPORTE_DEUDA;
                    }
                } else {
                    log.error("  -- altaDeudaSubvenciones --  ERROR IMPORTE DEUDA");
                    resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_IMP_PENDIENTE);
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_RECUPERANDO_IMP_PENDIENTE;
                }

                campoFecPagExpSubv = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                        codCampoFecPagoSubv, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                if (campoFecPagExpSubv.getStatus() == 0) {
                    zorLiFechaPagoExpSubv = campoFecPagExpSubv.getCampoSuplementario().getValorFecha();
                } else {
                    log.error("  -- altaDeudaSubvenciones --  ERROR  FECHA PAGO subv");//26
                    resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_PAGO);
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_RECUPERANDO_FECHA_PAGO;
                }

                campoCodigoAreaFlexia = gestorCampoSup.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExp, codProcedimiento,
                        codCampoCodigoAreaSW, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);

                if (campoCodigoAreaFlexia.getStatus() == 0) {
                    codigoAreaFlexia = campoCodigoAreaFlexia.getCampoSuplementario().getValorDesplegable();
                    try {
                        int codArea = Integer.parseInt(codigoAreaFlexia);
                        zorArCodigo = m92Utils.dameAcCodZorku(codArea);
                    } catch (NumberFormatException e) {
                        log.error("  -- altaDeudaSubvenciones --  ERROR recuperando el código de Acceso del Área ZORKU: " + e);
                        resultado.setAtributo("codigoOperacion", ERROR_CODIGO_ACCESO);
                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_CODIGO_ACCESO;
                    }
                } else {
                    log.error("  -- altaDeudaSubvenciones --  ERROR recuperando el código de Área en Regexlan");//26
                        resultado.setAtributo("codigoOperacion", ERROR_CODIGO_AREA_WS);
                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_CODIGO_AREA_WS;
                    }

                // nuevo parametro AÑO CONVOCATORIA
                campoAnioConvocatoria = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExp, codProcedimiento,
                        codigoCodAnioConvocatoria, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
                // recojo el valor del anio de la subvencion
                if (campoAnioConvocatoria.getStatus() == 0) {
                    zorLaEjercicio = campoAnioConvocatoria.getCampoSuplementario().getValorDesplegable();
                    log.debug("Año Convocatoria ayuda: " + zorLaEjercicio);
                } else {
                    log.error("---- altaDeudaSubvenciones - No existe el año de subvención");
                    resultado.setAtributo("codigoOperacion", ERROR_ANIO_CONVOCATORIA);
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_ANIO_CONVOCATORIA;
                }
                // con el ANIO y el procedimiento del expediente inicial obtengo el codigo de la subvencion
                zorDeCodSubvencion = MeLanbide92Manager.getInstance().getCodigoSubvencionEika(zorLaEjercicio, numExp.split(ConstantesDatos.BARRA)[1], adapt);
                if (zorDeCodSubvencion == null || zorDeCodSubvencion.isEmpty()) {
                    log.error("---- altaDeudaSubvenciones - No existe el código de subvención");
                    resultado.setAtributo("codigoOperacion", ERROR_COD_SUBVENCION);
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_COD_SUBVENCION;
                }

            } catch (NumberFormatException e) {
                log.error("error en altaDeudaSubvenciones ", e);
                resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_SUPLEMENTARIOS);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_RECUPERANDO_SUPLEMENTARIOS;
            }

            // DATOS INTERESADO
            log.info("  -- altaDeudaSubvenciones --  Recojo suplementarios - INTERESADO");

            ParamsAltaDeuda parametrosAlta = new ParamsAltaDeuda();
            parametrosAlta = m92Utils.getDatosInteresado(codOrganizacion, parametrosAlta, numExp, this);

            // CONTROL DATOS OBLIGATORIOS       
            if (zorLiFechaRequerimiento == null) {
                log.error("  -- altaDeudaSubvenciones --  ERROR  FECHA REQUERIMIENTO");//27
                resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_REQ);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_RECUPERANDO_FECHA_REQ;
            }
            if (zorLiFechaLimitePago == null) {
                log.error("---- altaDeudaSubvenciones -- ERROR  FECHA LIMITE PAGO");// 28
                resultado.setAtributo("codigoOperacion", ERROR_CALCULANDO_FECHA_LIMITE_PAGO);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_CALCULANDO_FECHA_LIMITE_PAGO;
            }
            if (zorLiImporteDeuda == null) {
                log.error("-  -- altaDeudaSubvenciones --   ERROR IMPORTE DEUDA");
                resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_IMP_PENDIENTE);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_RECUPERANDO_IMP_PENDIENTE;
            }
            if ((zorArCodigo == 0)) {
                log.error("  -- altaDeudaSubvenciones --   ERROR  CODIGO AREA ACCESO");
                resultado.setAtributo("codigoOperacion", ERROR_CODIGO_ACCESO);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_CODIGO_ACCESO;
            }
            if ((("".equals(parametrosAlta.getZorDirCp())) || (parametrosAlta.getZorDirCp() == null))) {
                log.error("  -- altaDeudaSubvenciones --   ERROR  CP");
                resultado.setAtributo("codigoOperacion", ERROR_CP);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_CP;
            }
            if ((("".equals(parametrosAlta.getZorDirDireccionCompleta())) || (parametrosAlta.getZorDirDireccionCompleta() == null))) {
                log.error("--- ERROR NOMBRE CALLE ");
                resultado.setAtributo("codigoOperacion", ERROR_DIRECCION);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_DIRECCION;
            }
            if ((("".equals(parametrosAlta.getZorDirMunicipio())) || (parametrosAlta.getZorDirMunicipio() == null))) {
                log.error("  -- altaDeudaSubvenciones --   ERROR  ID MUNICIPIO");
                resultado.setAtributo("codigoOperacion", ERROR_MUNICIPIO);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_MUNICIPIO;
            }
            if ((("".equals(parametrosAlta.getZorDirProv())) || (parametrosAlta.getZorDirProv() == null))) {
                log.error("  -- altaDeudaSubvenciones --   ERROR ID PROVINCIA ");
                resultado.setAtributo("codigoOperacion", ERROR_PROVINCIA);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_PROVINCIA;
            }
            if ((("".equals(parametrosAlta.getZorTerNumDocumento())) || (parametrosAlta.getZorTerNumDocumento() == null))) {
                log.error("  -- altaDeudaSubvenciones --   ERROR  NUMERO DOCUMENTO");
                resultado.setAtributo("codigoOperacion", ERROR_DOCUMENTO);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_DOCUMENTO;
            }
            if ((("".equals(parametrosAlta.getZorTerTipoDocumento())) || (parametrosAlta.getZorTerTipoDocumento() == null) || (!"1".equals(parametrosAlta.getZorTerTipoDocumento())) && (!"2".equals(parametrosAlta.getZorTerTipoDocumento())) && (!"3".equals(parametrosAlta.getZorTerTipoDocumento())) && (!"4".equals(parametrosAlta.getZorTerTipoDocumento())) && (!"5".equals(parametrosAlta.getZorTerTipoDocumento())))) {
                log.error("  -- altaDeudaSubvenciones --   ERROR  TIPO DOCUMENTO");
                resultado.setAtributo("codigoOperacion", ERROR_TIPO_DOCUMENTO);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_TIPO_DOCUMENTO;
            }
//Rellenamos los objetos del cliente            
            log.debug("  -- altaDeudaSubvenciones --  Llenado objetos del cliente ");
            // Seguridad
            parametrosAlta.setZorUsUsuario(seguridadVO.getUsuario());
            parametrosAlta.setZorUsPassword(seguridadVO.getContrasena());
            parametrosAlta.setArea(seguridadVO.getArea());
            //Area acceso
            parametrosAlta.setZorArCodigo(new BigDecimal(zorArCodigo));
            parametrosAlta.setZorLaEjercicio(new BigDecimal(zorLaEjercicio));
// datos deuda
            if (!zorLiNumLiquidacion.isEmpty()) {
                parametrosAlta.setZorLiNumLiquidacion(new BigDecimal(zorLiNumLiquidacion));
            }
            parametrosAlta.setZorLiImporteDeuda(zorLiImporteDeuda);
            parametrosAlta.setZorEdCodigo(zorEstadoDeuda);
            parametrosAlta.setZorTpCodigo(zorTipoPago);
            parametrosAlta.setZorDeExpediente(numExp);
            parametrosAlta.setZorDeExpRei(numExp);
            parametrosAlta.setZorDeExpInicial(numExp);
            parametrosAlta.setZorDeCodSubvencion(zorDeCodSubvencion); // nuevo parámetro 
//Fechas
            log.info("  -- altaDeudaSubvenciones --  Llenado objetos del cliente  - FECHAS");
            parametrosAlta.setZorLiFechaPagoExpSubv(MeLanbide92Utilidades.convierteFechaZorku(zorLiFechaPagoExpSubv));
            parametrosAlta.setZorLiFechaRequerimiento(MeLanbide92Utilidades.convierteFechaZorku(zorLiFechaRequerimiento));
            parametrosAlta.setZorLiFechaLimitePago(MeLanbide92Utilidades.convierteFechaZorku(zorLiFechaLimitePago));
                if (fase != 1) {
                parametrosAlta.setZorLiFechaResolucion(MeLanbide92Utilidades.convierteFechaZorku(zorLiFechaResolucion));
                }
            if (fase == 2 && zorFechaPagoDeudaIni != null) {
                parametrosAlta.setZorLiFechaPago(MeLanbide92Utilidades.convierteFechaZorku(zorFechaPagoDeudaIni));
                }

            log.info("  -- altaDeudaSubvenciones --  Tramite: " + tramite + " - Ocu: " + ocurrencia);
            log.info("======== PARAMETROS altaDeudaSubvenciones");
            log.info(parametrosAlta.toString());
            try {
                log.info("  --  altaDeudaSubvenciones  --   Cliente ");
                HttpClient client = HttpClients.createDefault();
                String jsonBody = new Gson().toJson(parametrosAlta);
                StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                // #377721 llamada al SW actualizarDomicilioSubvenciones para los trámites resolución y suspensión
            if (fase != 1) {
                    HttpPost llamadaActualizarDomicilio = new HttpPost(urlActualizarDom);
                    llamadaActualizarDomicilio.setHeader("Accept", "application/json");
                    llamadaActualizarDomicilio.setEntity(entidadLlamada);
                    try {
                        log.info("Llamando al SW actualizarDomicilioSubvenciones " + urlActualizarDom + "... " + jsonBody);
                        HttpResponse respuestaActualizarDom = client.execute(llamadaActualizarDomicilio);
// recojo respuesta
                        log.info("  --  actualizarDomicilioSubvenciones  --  getStatusLine() : " + respuestaActualizarDom.getStatusLine());
                        int statusCodeActualizar = respuestaActualizarDom.getStatusLine().getStatusCode();
                        log.info("  --  actualizarDomicilioSubvenciones  --  getStatusLine().getStatusCode() : " + statusCodeActualizar);
                        log.info("  --  actualizarDomicilioSubvenciones  --  Hay respuesta de ZORKU");
                        HttpEntity entidadRespuestaActualizar = respuestaActualizarDom.getEntity();
                        // convierto la respuesta a STRING
                        String respuestaActualizarString = EntityUtils.toString(entidadRespuestaActualizar);
                        log.debug("  --  actualizarDomicilioSubvenciones  --  Respuesta String: " + respuestaActualizarString);
                        if (!respuestaActualizarString.isEmpty()) {
                            // convierto a JSON
                            JSONObject respuestaActualizarJson;
                            switch (statusCodeActualizar) {
                                case HttpStatus.SC_OK:
                                    respuestaActualizarJson = new JSONObject(respuestaActualizarString);
//                                    boolean valido = respuestaActualizarJson.getBoolean("valido");
//                                    if (valido) {
//                                        log.debug("  --  actualizarDomicilioSubvenciones  -- VALIDO");
//                                    }
                                    log.info("  --  actualizarDomicilioSubvenciones  --  OK");
                                    log.info("resolucionVOActualizarDomicilio. Identificador de la liquidación gestionada: " + respuestaActualizarJson.getString("numLiquidacion"));
                                    break;
                                case HttpStatus.SC_BAD_REQUEST:
                                    respuestaActualizarJson = new JSONObject(respuestaActualizarString);
                                    String codError = respuestaActualizarJson.getString("codigoError");
                                    String msgErrorCas = respuestaActualizarJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                    String msgErrorEus = respuestaActualizarJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                    log.error("  --  actualizarDomicilioSubvenciones  -- Error  al llamar a la ZORKU  - " + codError);
                                    log.error("  --  actualizarDomicilioSubvenciones  -- " + msgErrorCas);
                                    log.error("  --  actualizarDomicilioSubvenciones  -- " + msgErrorEus);
                                    resultado.setAtributo("codigoOperacion", msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus);
                                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                    return ERROR_LLAMADA_ACTUALIZAR;
                                case HttpStatus.SC_FORBIDDEN:
                                    log.error("  --  actualizarDomicilioSubvenciones  -- Error  " + statusCodeActualizar + " - " + respuestaActualizarDom.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                    resultado.setAtributo("codigoOperacion", ERROR_403);
                                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                    return ERROR_403;
                                case HttpStatus.SC_NOT_FOUND:
                                    log.error("  --  actualizarDomicilioSubvenciones  -- Error 404 al llamar a la ZORKU " + respuestaActualizarDom.getStatusLine().getReasonPhrase());
                                    resultado.setAtributo("codigoOperacion", ERROR_404);
                                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                    return ERROR_404;
                                case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                    log.error("  --  actualizarDomicilioNoRGI  -- Error  " + statusCodeActualizar + " - " + respuestaActualizarDom.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                    resultado.setAtributo("codigoOperacion", ERROR_500);
                                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                    return ERROR_500;
                                case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                    log.error("  --  actualizarDomicilioNoRGI  -- Error  " + statusCodeActualizar + " - " + respuestaActualizarDom.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                    resultado.setAtributo("codigoOperacion", ERROR_503);
                                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                    return ERROR_503;
                                default:
                                    log.error("  --  actualizarDomicilioSubvenciones  -- Error " + statusCodeActualizar + " al llamar a la ZORKU " + respuestaActualizarDom.getStatusLine().getReasonPhrase());
                                    resultado.setAtributo("codigoOperacion", statusCodeActualizar + " - " + respuestaActualizarDom.getStatusLine().getReasonPhrase());
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_LLAMADA_ACTUALIZAR;
                }
                        } else {
                            log.error("  --  actualizarDomicilioSubvenciones  -- Respuesta  " + statusCodeActualizar + " - " + respuestaActualizarDom.getStatusLine().getReasonPhrase());
                            resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ACTUALIZAR + " - " + statusCodeActualizar + " - " + respuestaActualizarDom.getStatusLine().getReasonPhrase());
                            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                            return ERROR_LLAMADA_ACTUALIZAR;
                }

                    } catch (IOException e) {
                        log.error("  --  actualizarDomicilioSubvenciones  -- IOException  al llamar a la ZORKU " + e.getMessage());
                        resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ACTUALIZAR);
                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_LLAMADA_ACTUALIZAR;
                    } catch (ParseException e) {
                        log.error("  --  actualizarDomicilioSubvenciones  -- ParseException  al llamar a la ZORKU " + e.getMessage());
                        resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ACTUALIZAR);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_LLAMADA_ACTUALIZAR;
                    } catch (JSONException e) {
                        log.error("  --  actualizarDomicilioSubvenciones  -- JSONException  al llamar a la ZORKU " + e.getMessage());
                        resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ACTUALIZAR);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_LLAMADA_ACTUALIZAR;
                    }
            }

                // llamada al SW altaCartaDeudaNoRGI
                log.info("Llamando al servicio " + urlAltaDeuda + " ... " + jsonBody);
                HttpPost llamadaAltaDeuda = new HttpPost(urlAltaDeuda);
                llamadaAltaDeuda.setHeader("Accept", "application/json");
                llamadaAltaDeuda.setEntity(entidadLlamada);
            try {
                    HttpResponse respuestaAltaDeuda = client.execute(llamadaAltaDeuda);
                    // recojo respuesta
                    log.info("  --  altaDeudaSubvenciones  --  getStatusLine() : " + respuestaAltaDeuda.getStatusLine());
                    int statusCodeAlta = respuestaAltaDeuda.getStatusLine().getStatusCode();
                    log.info("  --  altaDeudaSubvenciones  --  getStatusLine().getStatusCode() : " + statusCodeAlta);
                    log.info("  --  altaDeudaSubvenciones  --  Hay respuesta de ZORKU");
                    HttpEntity entidadRespuestaAlta = respuestaAltaDeuda.getEntity();
                    // convierto la respuesta a STRING
                    String respuestaAltaString = EntityUtils.toString(entidadRespuestaAlta);
                    log.debug("  --  altaDeudaSubvenciones  --  Respuesta String: " + respuestaAltaString);
                    if (!respuestaAltaString.isEmpty()) {
                        // convierto a JSON
                        JSONObject respuestaAltaJson;

                        switch (statusCodeAlta) {
                            case HttpStatus.SC_OK:
                                respuestaAltaJson = new JSONObject(respuestaAltaString);
                                boolean valido = respuestaAltaJson.getBoolean("valido");
                                log.info("  --  altaDeudaSubvenciones  -- VALIDO: " + valido);
                                numLiquidacionGenerada = respuestaAltaJson.getString("numLiquidacion");
                                nombreCarta = "LIQ_" + numLiquidacionGenerada + ".pdf";
                                String oidCarta = respuestaAltaJson.getString("oidCartaPago");
                                log.info("Deuda: " + numLiquidacionGenerada + " generada correctamente. OID: " + oidCarta);
                boolean grabar = true;
                                String codNumLiquidacion = "";
                String codCartaPago = "";
                String codFechaLimite = "";
                switch (fase) {
                    case 1:
                                        codNumLiquidacion = codCampoIdDeudaIni;
                        codCartaPago = codCampoCartaPagoIni;
                        codFechaLimite = codCampoFecLimiteIni;
                        break;
                    case 2:
                                        codNumLiquidacion = codCampoIdDeudaRes;
                        codCartaPago = codCampoCartaPagoRes;
                        codFechaLimite = codCampoFecLimiteRes;
                                        // si no se ha pagado la deuda de alegaciones hay que anularla
                                        if (zorFechaPagoDeudaIni == null) {
                                            String motivo = "05";
                                            String observaciones = "Anulada desde Regexlan tras generar la Liquidación en Resolución";
                                            String urlAnularDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_ANULACION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                                            ParamsAnularDeuda parametrosAnulacion = new ParamsAnularDeuda();
                                            parametrosAnulacion.setZorUsUsuario(seguridadVO.getUsuario());
                                            parametrosAnulacion.setZorUsPassword(seguridadVO.getContrasena());
                                            parametrosAnulacion.setArea(seguridadVO.getArea());
                                            parametrosAnulacion.setZorLiNumLiquidacion(new BigDecimal(zorLiNumLiquidacion));
                                            parametrosAnulacion.setZorMaCod(motivo);
                                            parametrosAnulacion.setZorLiMotivoAnulacionTexto(observaciones);
                                            try {
                                                client = HttpClients.createDefault();
                                                jsonBody = new Gson().toJson(parametrosAnulacion);
                                                entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                                                HttpPost llamadaAnulacion = new HttpPost(urlAnularDeuda);
                                                llamadaAnulacion.setHeader("Accept", "application/json");
                                                llamadaAnulacion.setEntity(entidadLlamada);
                                                log.info("Llamando al servicio " + urlAnularDeuda + " ... " + jsonBody);
                                                HttpResponse respuesta = client.execute(llamadaAnulacion);
                                                // recojo respuesta
                                                log.info(" altaDeudaSubvenciones --  anularDeudaSubvenciones  --  getStatusLine() : " + respuesta.getStatusLine());
                                                int statusCode = respuesta.getStatusLine().getStatusCode();
                                                log.info(" altaDeudaSubvenciones --  anularDeudaSubvenciones  --  getStatusLine().getStatusCode() : " + statusCode);
                                                log.info(" altaDeudaSubvenciones --  anularDeudaSubvenciones  --  Hay respuesta de ZORKU");
                                                HttpEntity entidadRespuesta = respuesta.getEntity();
                                                // convierto la respuesta a STRING
                                                String respuestaString = EntityUtils.toString(entidadRespuesta);
                                                log.debug(" altaDeudaSubvenciones --  anularDeudaSubvenciones  --  Respuesta String: " + respuestaString);
                                                    // convierto a JSON
                                                    JSONObject respuestaJson;
                                                    switch (statusCode) {
                                                        case HttpStatus.SC_OK:
                                                            respuestaJson = new JSONObject(respuestaString);
                                                            log.info("anularDeudaSubvenciones - Hay respuesta SIN ERROR del SW");
                                                            codOperacion = OPERACION_CORRECTA;
                                                            try {
                                                                String fechaOper = null;
                                                                int usuarioExpediente = m92Manager.getUsuarioTramite(codOrganizacion, numExp, codProcedimiento, Integer.parseInt(tramite), Integer.parseInt(ocurrencia), adapt);
                                                                String nombreUsuario = m92Manager.getNombreUsuario(usuarioExpediente, adapt);
                                                                OperacionExpedienteVO operacion = new OperacionExpedienteVO();
                                                                //lleno el objeto
                                                                log.debug("Lleno el Objeto operacionVO");
                                                                operacion.setCodMunicipio(codOrganizacion);
                                                                operacion.setEjercicio(Integer.parseInt(ejercicio));
                                                                operacion.setNumExpediente(numExp);
                                                            operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ANULAR_DEUDA);
                                                                operacion.setFechaOperacion(new GregorianCalendar());
                                                                operacion.setCodUsuario(usuarioExpediente);
                                                                fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()))
                                                                        + " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));

                                                                // descripcion
                                                                log.debug("Creo el XML de la operacion");
                                                                String descripcion = null;
                                                                StringBuilder textoXml = new StringBuilder("");
                                                                textoXml.append("<div class=\"movExpC1\">{eMovExpAnularDeuda}</div>\n");
                                                                textoXml.append("<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">{eMovExpNumExp}:</div>\n"
                                                                        + "<div class=\"movExpVal\">").append(numExp).append("</div>\n"
                                                                        + "</div>");
                                                                textoXml.append("<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">{eMovExpCodProc}:</div>\n"
                                                                        + "<div class=\"movExpVal\">").append(codProcedimiento).append("</div>\n"
                                                                        + "</div>");

                                                            textoXml.append("<div class=\"movExpC2\">LIQUIDACI\u00d3N ANULADA:</div>\n");
                                                                textoXml.append("<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">Tipo de Liquidaci\u00f3n:</div>\n"
                                                                        + "<div class=\"movExpVal\">Fase Alegaciones</div>\n"
                                                                        + "</div>");
                                                                textoXml.append("<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">N\u00famero de Liquidaci\u00f3n:</div>\n"
                                                                        + "<div class=\"movExpVal\">").append(zorLiNumLiquidacion).append("</div>\n"
                                                                        + "</div>");
                                                                textoXml.append("<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">Carta de Pago anulada:</div>\n"
                                                                        + "<div class=\"movExpVal\">LIQ_").append(zorLiNumLiquidacion).append(".pdf</div>\n"
                                                                        + "</div>");
                                                                textoXml.append("<div class=\"movExpC2\">MOTIVO ANULACI\u00d3N</div>\n");
                                                                textoXml.append("<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">C\u00f3digo:</div>\n"
                                                                        + "<div class=\"movExpVal\">").append(motivo).append("</div>\n"
                                                                        + "</div>");
                                                                textoXml.append("<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">Descripci\u00f3n:</div>\n"
                                                                        + "<div class=\"movExpVal\">Otros motivos</div>\n"
                                                                        + "</div>");
                                                                textoXml.append("<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">{eMovExpObservac}:</div>\n"
                                                                        + "<div class=\"movExpVal\">").append(observaciones).append("</div>\n"
                                                                        + "</div>");
                                                                textoXml.append("<div class=\"movExpC2\">{eMovExpUsuFec}</div>\n"
                                                                        + "<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">{eMovExpUsuario}:</div>\n"
                                                                        + "<div class=\"movExpVal\">").append(usuarioExpediente).append("</div>\n"
                                                                        + "</div>"
                                                                        + "<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">{eMovExpNomUsuario}:</div>\n"
                                                                        + "<div class=\"movExpVal\">").append(nombreUsuario).append("</div>\n"
                                                                        + "</div>");
                                                                textoXml.append("<div class=\"movExpLin\">\n"
                                                                        + "<div class=\"movExpEtiq\">{gEtiqFecOpe}:</div>\n"
                                                                        + "<div class=\"movExpVal\">").append(fechaOper).append("</div>\n"
                                                                        + "</div>");
                                                                descripcion = textoXml.toString();

                                                                operacion.setDescripcionOperacion(descripcion);

                                                                OperacionesExpedienteDAO.getInstance().insertarOperacionExpediente(operacion, con);
                                                            } catch (Exception e) {
                                                                log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- ha ocurrido un error al grabar la operacion anularDeuda del expediente " + numExp + " - ", e);
                                                            }
                                                            break;
                                                        case HttpStatus.SC_BAD_REQUEST:
                                                            respuestaJson = new JSONObject(respuestaString);
                                                            String codError = respuestaJson.getString("codigoError");
                                                            String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                                            String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                                            log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- Error  al llamar a la ZORKU  - " + codError);
                                                            log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- " + msgErrorCas);
                                                            log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- " + msgErrorEus);
                                                        codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                                                            break;
                                                    case HttpStatus.SC_FORBIDDEN:
                                                        log.error("  --  altaDeudaSubvenciones  -- Error  " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " en anularDeudaSubvenciones al llamar a la ZORKU ");
                                                        resultado.setAtributo("codigoOperacion", ERROR_403);
                                                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                                        return ERROR_403;
                                                        case HttpStatus.SC_NOT_FOUND:
                                                            log.error("  --  altaDeudaSubvenciones  -- Error 404 en anularDeudaSubvenciones al llamar a ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                                            resultado.setAtributo("codigoOperacion", ERROR_404);
                                                            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                                            return ERROR_404;
                                                    case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                                        log.error("  --  altaDeudaSubvenciones  -- Error  " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " en anularDeudaSubvenciones al llamar a la ZORKU ");
                                                        resultado.setAtributo("codigoOperacion", ERROR_500);
                                                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                                        return ERROR_500;
                                                    case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                                        log.error("  --  altaDeudaSubvenciones  -- Error  " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " en anularDeudaSubvenciones al llamar a la ZORKU ");
                                                        resultado.setAtributo("codigoOperacion", ERROR_503);
                                                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                                        return ERROR_503;
                                                        default:
                                                            log.error("  --  altaDeudaSubvenciones  -- Error en anularDeudaSubvenciones " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                                                            resultado.setAtributo("codigoOperacion", statusCode + " - " + respuesta.getStatusLine().getReasonPhrase());
                                                            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                                            return ERROR_LLAMADA_ANULAR;
                                                    }
                                            } catch (IOException e) {
                                                log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- IOException  " + e.getMessage());
                                                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ANULAR);
                                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                                return ERROR_LLAMADA_ANULAR;
                                            } catch (UnsupportedCharsetException e) {
                                                log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- UnsupportedCharsetException  " + e.getMessage());
                                                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ANULAR);
                                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                                return ERROR_LLAMADA_ANULAR;
                                            } catch (ParseException e) {
                                                log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- ParseException  " + e.getMessage());
                                                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ANULAR);
                                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                                return ERROR_LLAMADA_ANULAR;
                                            } catch (JSONException e) {
                                                log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- JSONException  " + e.getMessage());
                                                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ANULAR);
                                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                                return ERROR_LLAMADA_ANULAR;
                                            }
                                        }
                        break;
                    case 3:
                                        codNumLiquidacion = codCampoIdDeudaSus;
                        codCartaPago = codCampoCartaPagoSus;
                        codFechaLimite = codCampoFecLimiteSus;
                        break;
                    default:
                        grabar = false;
                        break;
                }

                if (grabar) {
                                    try {
                                        log.info("  -- altaDeudaSubvenciones --  Grabar  - " + codNumLiquidacion + " - " + codCartaPago + " - " + codCampoFecLimiteIni);
                                        if (m92Manager.existeFicheroExpediente(codOrganizacion, numExp, codCartaPago, adapt)) {
                        log.debug(" existe");
                        int cartaBorrada = m92Manager.borrarFicheroExpediente(codOrganizacion, numExp, codCartaPago, adapt);
                                            int relBorrada = m92Manager.borrarRelacionDokusiCSE(codOrganizacion, numExp, codCartaPago, adapt);
                                            if (cartaBorrada <= 0 || relBorrada <= 0) {
                                                log.error("  -- altaDeudaSubvenciones --  Ha ocurrido un error al borrar la carta de pago");
                            resultado.setAtributo("codigoOperacion", ERROR_BORRANDO_CARTA);
                            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                            return ERROR_BORRANDO_CARTA;
                        }
                    }

                                        if (m92Manager.grabarCartaDokusi(codOrganizacion, numExp, codCartaPago, nombreCarta, oidCarta, adapt)) {
                                            log.info("  -- altaDeudaSubvenciones --  CARTA Grabada");
                                        } else {
                                            log.error("  -- altaDeudaSubvenciones --  Ha ocurrido un error al grabar la carta de pago");
                        resultado.setAtributo("codigoOperacion", ERROR_GRABANDO_CARTA);
                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_GRABANDO_CARTA;
                    }

                    CampoSuplementarioModuloIntegracionVO campoSuplementarioIdDeuda = new CampoSuplementarioModuloIntegracionVO();
                    campoSuplementarioIdDeuda.setCodOrganizacion(String.valueOf(codOrganizacion));
                    campoSuplementarioIdDeuda.setCodProcedimiento(codProcedimiento);
                    campoSuplementarioIdDeuda.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    campoSuplementarioIdDeuda.setTramite(false);
                    campoSuplementarioIdDeuda.setNumExpediente(numExp);
                                        campoSuplementarioIdDeuda.setEjercicio(ejercicio);
                                        campoSuplementarioIdDeuda.setCodigoCampo(codNumLiquidacion);
                                        campoSuplementarioIdDeuda.setValorTexto(numLiquidacionGenerada);
                                        gestorCampoSup.grabarCampoSuplementario(campoSuplementarioIdDeuda);
                                        log.info("  -- altaDeudaSubvenciones --  Se graba el campo " + codNumLiquidacion + ": " + numLiquidacionGenerada);

                    CampoSuplementarioModuloIntegracionVO campoSuplementarioFechLim = new CampoSuplementarioModuloIntegracionVO();
                    campoSuplementarioFechLim.setCodOrganizacion(String.valueOf(codOrganizacion));
                    campoSuplementarioFechLim.setCodProcedimiento(codProcedimiento);
                    campoSuplementarioFechLim.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                    campoSuplementarioFechLim.setTramite(false);
                    campoSuplementarioFechLim.setNumExpediente(numExp);
                                        campoSuplementarioFechLim.setEjercicio(ejercicio);
                    campoSuplementarioFechLim.setCodigoCampo(codFechaLimite);
                                        campoSuplementarioFechLim.setValorFecha(zorLiFechaLimitePago);
                                        gestorCampoSup.grabarCampoSuplementario(campoSuplementarioFechLim);
                                        log.info("  -- altaDeudaSubvenciones --  Se graba el campo " + codFechaLimite + " = " + zorLiFechaLimitePago.toString());
                    if (fase == 1) {
                                            log.info("  -- altaDeudaSubvenciones --  Grabar  - " + codCampoFecRequerimiento);
                        CampoSuplementarioModuloIntegracionVO campoSuplementarioFechReq = new CampoSuplementarioModuloIntegracionVO();
                        campoSuplementarioFechReq.setCodOrganizacion(String.valueOf(codOrganizacion));
                        campoSuplementarioFechReq.setCodProcedimiento(codProcedimiento);
                        campoSuplementarioFechReq.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        campoSuplementarioFechReq.setTramite(true);
                        campoSuplementarioFechReq.setCodTramite(tramite);
                        campoSuplementarioFechReq.setOcurrenciaTramite(ocurrencia);
                        campoSuplementarioFechReq.setNumExpediente(numExp);
                                            campoSuplementarioFechReq.setEjercicio(ejercicio);
                        campoSuplementarioFechReq.setCodigoCampo(codCampoFecRequerimiento);
                                            campoSuplementarioFechReq.setValorFecha(zorLiFechaRequerimiento);
                                            gestorCampoSup.grabarCampoSuplementario(campoSuplementarioFechReq);
                                            log.info("  -- altaDeudaSubvenciones --  Se graba el campo " + codCampoFecRequerimiento + " = " + zorLiFechaRequerimiento.toString());
                    }
                    if (fase == 2) {
                                            log.info("  -- altaDeudaSubvenciones --  Grabar  - " + codCampoIntereses);
                                            intereses = new BigDecimal(respuestaAltaJson.getDouble("importeIntereses"));
                                            intereses = intereses.setScale(2, RoundingMode.HALF_UP);
                                            //  FALLA el metodo de FLEXIA lo grabo a mano
//                        CampoSuplementarioModuloIntegracionVO campoSuplementarioInter = new CampoSuplementarioModuloIntegracionVO();
//                        campoSuplementarioInter.setCodOrganizacion(String.valueOf(codOrganizacion));
//                        campoSuplementarioInter.setCodProcedimiento(codProcedimiento);
//                        campoSuplementarioInter.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
//                        campoSuplementarioInter.setTramite(true);
//                        campoSuplementarioInter.setCodTramite(tramite);
//                        campoSuplementarioInter.setOcurrenciaTramite(ocurrencia);
//                        campoSuplementarioInter.setNumExpediente(numExp);
//                        campoSuplementarioInter.setEjercicio(ejercicio);
//                        campoSuplementarioInter.setCodigoCampo(codCampoIntereses);
//                        campoSuplementarioInter.setValorNumero(intereses.toString());
//                        el.grabarCampoSuplementario(campoSuplementarioInter);
                                            /* int grabado = */
                                            int grabaOK = m92Manager.guardarValorCampoNumericoTramite(codOrganizacion, numExp, Integer.parseInt(tramite), Integer.parseInt(ocurrencia), codCampoIntereses, intereses, adapt);
                                            if (grabaOK <= 0) {
                                                log.error("Excepción al grabar el campo suplementario " + codCampoIntereses);
                                            } else {
                                                log.info("Grabado el valor " + intereses + " en " + codCampoIntereses);
                                            }
                                        }

                                    } catch (Exception e) {
                                        log.error("  -- altaDeudaSubvenciones --  Devolvemos: " + ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS);
                                        resultado.setAtributo("codigoOperacion", ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS);
                                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                        return ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS;
                    }
                } else {
                                    log.error("  -- altaDeudaSubvenciones --  El código de trámite no corresponde a un trámite con pestaña");
                    resultado.setAtributo("codigoOperacion", ERROR_FASE);
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_FASE;
                }

                                break;
                            case HttpStatus.SC_BAD_REQUEST:
                                respuestaAltaJson = new JSONObject(respuestaAltaString);
                                String codError = respuestaAltaJson.getString("codigoError");
                                String msgErrorCas = respuestaAltaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                String msgErrorEus = respuestaAltaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                log.error("  --  altaDeudaSubvenciones  -- Error  al llamar a la ZORKU  - " + codError);
                                log.error("  --  altaDeudaSubvenciones  -- " + msgErrorCas);
                                log.error("  --  altaDeudaSubvenciones  -- " + msgErrorEus);
                                codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                                log.info("altaDeudaSubvenciones.Devolvemos: " + codOperacion);
                                resultado.setAtributo("codigoOperacion", codOperacion);
                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                return codOperacion;
                            case HttpStatus.SC_FORBIDDEN:
                                log.error("  --  altaDeudaSubvenciones  -- Error  " + statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                resultado.setAtributo("codigoOperacion", ERROR_403);
                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                return ERROR_403;
                            case HttpStatus.SC_NOT_FOUND:
                                log.error("  --  altaDeudaSubvenciones  -- Error 404 al llamar a la ZORKU " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                                resultado.setAtributo("codigoOperacion", ERROR_404);
                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                return ERROR_404;
                            case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                log.error("  --  altaDeudaSubvenciones  -- Error  " + statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                resultado.setAtributo("codigoOperacion", ERROR_500);
                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                return ERROR_500;
                            case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                log.error("  --  altaDeudaSubvenciones  -- Error  " + statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                resultado.setAtributo("codigoOperacion", ERROR_503);
                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                return ERROR_503;
                            default:
                                log.error("  --  altaDeudaSubvenciones  -- Error " + statusCodeAlta + " al llamar a la ZORKU ");
                                resultado.setAtributo("codigoOperacion", statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                                return ERROR_LLAMADA_ALTA_CARTA;
                        }
                    } else {
                        log.error("  --  altaDeudaSubvenciones  -- Respuesta  de la ZORKU " + statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                        resultado.setAtributo("codigoOperacion", statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_LLAMADA_ALTA_CARTA;
                    }
                } catch (IOException e) {
                    log.error("  --  altaDeudaSubvenciones  -- IOException  al llamar a la ZORKU " + e.getMessage());
                    resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ALTA_CARTA);
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_LLAMADA_ALTA_CARTA;
                } catch (ParseException e) {
                    log.error("  --  altaDeudaSubvenciones  -- ParseException  al llamar a la ZORKU " + e.getMessage());
                    resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ALTA_CARTA);
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_LLAMADA_ALTA_CARTA;
                } catch (JSONException e) {
                    log.error("  --  altaDeudaSubvenciones  -- JSONException  al llamar a la ZORKU " + e.getMessage());
                    resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ALTA_CARTA);
                    m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_LLAMADA_ALTA_CARTA;
                }
            } catch (UnsupportedCharsetException e) {
                log.error("  --  altaDeudaSubvenciones  -- UnsupportedCharsetException  al llamar a la ZORKU " + e.getMessage());
                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ALTA_CARTA);
                m92Utils.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_LLAMADA_ALTA_CARTA;
            }

        } catch (Exception e) {
            log.error("error en altaDeudaSubvenciones ", e);
            resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_ALTA_CARTA);
            m92Utils.retornarJSON(new Gson().toJson(resultado), response);
            return ERROR_LLAMADA_ALTA_CARTA;
        } finally {
            if (adapt != null) {
                adapt.devolverConexion(con);
            }
        }

        //Si todo va bien, todo correcto, devolvemos 0
        log.info("  -- altaDeudaSubvenciones --  Devolvemos: " + OPERACION_CORRECTA);
        codOperacion = OPERACION_CORRECTA;
        resultado.setAtributo("codigoOperacion", codOperacion);
        resultado.setAtributo("idDeuda", numLiquidacionGenerada);
        m92Utils.retornarJSON(new Gson().toJson(resultado), response);
        return codOperacion;
    }

    /**
     * operacion de extension que llama al metodo anularDeudaSubvenciones() de
     * ZORKU
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws BDException
     */
    public String anularDeudaSubvenciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("===========>  anularDeudaSubvenciones ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_OPERACION;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide92Manager m92Manager = null;

        String numLiquidacion = "";
        String motAnulacion = "";
        String observaciones = null;
        String ejercicio = "";
        String codProcedimiento = "";
        String codCampoCartaPago = null;
        String codDeudaABorrar = null;
        String codFechaABorrar = null;
        String codCheckABorrar = null;
        String tipoDeuda = null;

        //Creo el VO de seguridad con un usuario SIPCA
        ParamsSeguridadZorku seguridadVO = MeLanbide92Utilidades.getSeguridad(codOrganizacion);
        SalidaIntegracionVO campoIdDeuda = null;
        SalidaIntegracionVO campoObsDeuda = null;

        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(ConstantesDatos.BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];

        }

        String codCampoIdDeudaIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoIdDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoIdDeudaSus = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_SUS, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoCartaPagoIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CARTAPAGO_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoCartaPagoRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CARTAPAGO_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoCartaPagoSus = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CARTAPAGO_SUS, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecLimiteIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_INI, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecLimiteRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecLimiteSus = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_SUS, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoAnuladaIni = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_DEUDA_ANULADA_REQ, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoAnuladaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_DEUDA_ANULADA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoIntereses = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_INTERES_DEMORA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoMotAnulDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_MOT_ANUL_DEUDA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoObsAnulDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_OBS_ANUL_DEUDA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String urlAnularDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_ANULACION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        log.debug("URL: " + urlAnularDeuda);
        try {
            m92Manager = MeLanbide92Manager.getInstance();
            adapt = m92Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            log.info("llamada al sw");
            ParamsAnularDeuda parametrosAnulacion = new ParamsAnularDeuda();
            parametrosAnulacion.setZorUsUsuario(seguridadVO.getUsuario());
            parametrosAnulacion.setZorUsPassword(seguridadVO.getContrasena());
            parametrosAnulacion.setArea(seguridadVO.getArea());

            // recojo los suplementarios
            try {
                motAnulacion = m92Manager.getCodigoDespExternoTramite(codOrganizacion, numExpediente, codTramite, ocurrenciaTramite, codCampoMotAnulDeuda, adapt);
            } catch (Exception e) {
                log.error("  -- anularDeudaSubvenciones --  Devolvemos: " + ERROR_RECUPERANDO_MOT_ANULACION, e);
                return ERROR_RECUPERANDO_MOT_ANULACION;
            }

            campoObsDeuda = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramite, ocurrenciaTramite, codCampoObsAnulDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            if (campoObsDeuda.getStatus() == 0) {
                observaciones = campoObsDeuda.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  -- anularDeudaSubvenciones --  Faltan las observaciones en la anulación");
                return ERROR_RECUPERANDO_OBS_ANULACION;
            }

            campoIdDeuda = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoIdDeudaSus, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

            if ((campoIdDeuda.getStatus() == 0)) {
                log.info("  -- anularDeudaSubvenciones --  Se anula la deuda en SUSPENSIÓN");
                numLiquidacion = campoIdDeuda.getCampoSuplementario().getValorTexto();
                codCampoCartaPago = codCampoCartaPagoSus;
                codDeudaABorrar = codCampoIdDeudaSus;
                codFechaABorrar = codCampoFecLimiteSus;
                tipoDeuda = "Fase Suspensión";
            } else {
                campoIdDeuda = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                if ((campoIdDeuda.getStatus() == 0)) {
                    log.info("  -- anularDeudaSubvenciones --  Se anula la deuda en RESOLUCIÓN");
                    numLiquidacion = campoIdDeuda.getCampoSuplementario().getValorTexto();
                    codCampoCartaPago = codCampoCartaPagoRes;
                    codDeudaABorrar = codCampoIdDeudaRes;
                    codFechaABorrar = codCampoFecLimiteRes;
                    codCheckABorrar = codCampoAnuladaRes;
                    tipoDeuda = "Fase Resolución";
                } else {
                    campoIdDeuda = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            codCampoIdDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                    if ((campoIdDeuda.getStatus() == 0)) {
                        log.info("  -- anularDeudaSubvenciones --  Se anula la deuda en REQUERIMIENTO");
                        numLiquidacion = campoIdDeuda.getCampoSuplementario().getValorTexto();
                        codCampoCartaPago = codCampoCartaPagoIni;
                        codDeudaABorrar = codCampoIdDeudaIni;
                        codFechaABorrar = codCampoFecLimiteIni;
                        codCheckABorrar = codCampoAnuladaIni;
                        tipoDeuda = "Fase Alegaciones";
                        //  esPrimera = true;
                    } else {
                        log.error("  -- anularDeudaSubvenciones --  NO existe la deuda");
                        return ERROR_RECUPERANDO_ID_DEUDA;
                    }
                }
            }

            log.info("  -- anularDeudaSubvenciones --  >>>>Llamamos al ws <<<<<<<<");
            log.info(tipoDeuda);
            parametrosAnulacion.setZorLiNumLiquidacion(new BigDecimal(numLiquidacion));
            parametrosAnulacion.setZorMaCod(motAnulacion);
            parametrosAnulacion.setZorLiMotivoAnulacionTexto(observaciones);

            try {
                HttpClient client = HttpClients.createDefault();
                String jsonBody = new Gson().toJson(parametrosAnulacion);
                StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                HttpPost llamadaAnulacion = new HttpPost(urlAnularDeuda);
                llamadaAnulacion.setHeader("Accept", "application/json");
                llamadaAnulacion.setEntity(entidadLlamada);
                log.info("Llamando al servicio " + urlAnularDeuda + " ... " + jsonBody);
                HttpResponse respuesta = client.execute(llamadaAnulacion);
                // recojo respuesta
                log.info("  --  anularDeudaSubvenciones  --  getStatusLine() : " + respuesta.getStatusLine());
                int statusCode = respuesta.getStatusLine().getStatusCode();
                log.info("  --  anularDeudaSubvenciones  --  getStatusLine().getStatusCode() : " + statusCode);
                HttpEntity entidadRespuesta = respuesta.getEntity();
                // convierto la respuesta a STRING
                String respuestaString = EntityUtils.toString(entidadRespuesta);
                log.debug("  --  anularDeudaSubvenciones  --  Respuesta String: " + respuestaString);
                    // convierto a JSON
                    JSONObject respuestaJson;
                    switch (statusCode) {
                        case HttpStatus.SC_OK:
                            respuestaJson = new JSONObject(respuestaString);
                            log.info("Hay respuesta SIN ERROR del SW");
                            if (codCampoCartaPago != null) {
                                String nombreCarta = null;
                                String fechaOper = null;

                                if (m92Manager.existeFicheroExpediente(codOrganizacion, numExpediente, codCampoCartaPago, adapt)) {
                                    nombreCarta = m92Manager.getNombreFicheroExpediente(codOrganizacion, numExpediente, codCampoCartaPago, adapt);
                                    //  elimino de MELANBIDE_DOKUSI_RELDOC_DOCCSE y de E_TFI                            
                                    if (m92Manager.borrarRelacionDokusiCSE(codOrganizacion, numExpediente, codCampoCartaPago, adapt) <= 0 || m92Manager.borrarFicheroExpediente(codOrganizacion, numExpediente, codCampoCartaPago, adapt) <= 0) {
                                        log.error("  -- anularDeudaSubvenciones --  Ha ocurrido un error al borrar la carta de pago");
                                        return ERROR_BORRANDO_CARTA;
                                    } else {
                                        log.info("  -- anularDeuda --  Carta Borrada");
                                    }
                                }
                                //   deudaBorrada;

                                if (m92Manager.borrarTextoExpediente(codOrganizacion, numExpediente, codDeudaABorrar, adapt) > 0) {
                                    log.info("  -- anularDeudaSubvenciones --  IdDeuda Borrada");
                                    CampoSuplementarioModuloIntegracionVO campoEliminar = new CampoSuplementarioModuloIntegracionVO();

                                    campoEliminar.setCodOrganizacion(String.valueOf(codOrganizacion));
                                    campoEliminar.setCodProcedimiento(codProcedimiento);
                                    campoEliminar.setEjercicio(ejercicio);
                                    campoEliminar.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                                    campoEliminar.setTramite(false);
                                    campoEliminar.setNumExpediente(numExpediente);
                                    campoEliminar.setCodigoCampo(codFechaABorrar);
                                    SalidaIntegracionVO borradoCampo = gestorCampoSup.eliminarValorCampoSuplementario(campoEliminar);
                                    if (borradoCampo.getStatus() == 0) {
                                        log.info("  -- anularDeudaSubvenciones --  Fecha Vencimiento eliminada");
                                    } else {
                                        log.error("  -- anularDeudaSubvenciones --  Ha ocurrido un error al borrar la fecha de vencimiento de la carta de pago");
                                        return ERROR_BORRANDO_FEC_VENC;
                                    }
                                    if (tipoDeuda.equalsIgnoreCase("Fase Resolución")) {
                                        // eliminar intereses del trámite de resolucion
                                        int codTramRes = m92Manager.getCodigoInternoTramite(codOrganizacion, codProcedimiento, ConfigurationParameter.getParameter(ConstantesMeLanbide92.TRAMITE_RESOLUCION, ConstantesMeLanbide92.FICHERO_PROPIEDADES), adapt);
                                        int ocuTramRes = m92Manager.getMaxOcurrenciaTramitexCodigo(codOrganizacion, numExpediente, codTramRes, adapt);
                                        campoEliminar = new CampoSuplementarioModuloIntegracionVO();
                                        campoEliminar.setCodOrganizacion(String.valueOf(codOrganizacion));
                                        campoEliminar.setCodProcedimiento(codProcedimiento);
                                        campoEliminar.setEjercicio(ejercicio);
                                        campoEliminar.setNumExpediente(numExpediente);
                                        campoEliminar.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                                        campoEliminar.setTramite(true);
                                        campoEliminar.setCodTramite(String.valueOf(codTramRes));
                                        campoEliminar.setOcurrenciaTramite(String.valueOf(ocuTramRes));
                                        campoEliminar.setCodigoCampo(codCampoIntereses);
                                        borradoCampo = gestorCampoSup.eliminarValorCampoSuplementario(campoEliminar);
                                        int borraInteres = m92Manager.borrarNumericoTramite(codOrganizacion, numExpediente, codTramRes, ocuTramRes, codCampoIntereses, adapt);
                                    if (borradoCampo.getStatus() == 0) {
                                        log.info("  -- anularDeudaSubvenciones --  Intereses eliminados");
                                    }
                                }
                                     
                                if (tipoDeuda.equalsIgnoreCase("Fase Resolución") || tipoDeuda.equalsIgnoreCase("Fase Alegaciones")) {
                                    // comprobar si existe el check de Anulada y eliminarlo
                                    if (m92Manager.tieneCheckMarcado(codOrganizacion, numExpediente, codCheckABorrar, adapt)) {
                                        campoEliminar = new CampoSuplementarioModuloIntegracionVO();
                                        campoEliminar.setCodOrganizacion(String.valueOf(codOrganizacion));
                                        campoEliminar.setCodProcedimiento(codProcedimiento);
                                        campoEliminar.setEjercicio(ejercicio);
                                        campoEliminar.setNumExpediente(numExpediente);
                                        campoEliminar.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
                                        campoEliminar.setTramite(false);
                                        campoEliminar.setNumExpediente(numExpediente);
                                        campoEliminar.setCodigoCampo(codCheckABorrar);
                                        borradoCampo = gestorCampoSup.eliminarValorCampoSuplementario(campoEliminar);
                                        if (borradoCampo.getStatus() == 0) {
                                            log.info("  -- anularDeudaSubvenciones --  Check Vencimiento eliminado");
                                        }
                                    }
                                    }
                                    try {
                                        // INSERTAR OPERACIONES EXPEDIENTE
                                        int usuarioExpediente = m92Manager.getUsuarioTramite(codOrganizacion, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, adapt);
                                        String nombreUsuario = m92Manager.getNombreUsuario(usuarioExpediente, adapt);
                                        String descMotivo = m92Manager.getDescripcionDespExternoTramite(codOrganizacion, numExpediente, codTramite, ocurrenciaTramite, codCampoMotAnulDeuda, adapt);
                                        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
                                        //lleno el objeto
                                        log.debug("  -- anularDeudaSubvenciones --  Lleno el Objeto operacionVO");
                                        operacion.setCodMunicipio(codOrganizacion);
                                        operacion.setEjercicio(Integer.parseInt(ejercicio));
                                        operacion.setNumExpediente(numExpediente);
                                    operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ANULAR_DEUDA);
                                        operacion.setFechaOperacion(new GregorianCalendar());
                                        operacion.setCodUsuario(usuarioExpediente);
                                        try {
                                            fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()))
                                                    + " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
                                        } catch (Exception e) {
                                            log.error("  -- anularDeudaSubvenciones --  Ha ocurrido un error al convertir la fecha de la operacion a String. ", e);
                                        }

                                        // descripcion
                                        log.debug("  -- anularDeudaSubvenciones --  Creo el XML de la operacion");
                                        String descripcion = null;
                                        StringBuilder textoXml = new StringBuilder("");
                                        textoXml.append("<div class=\"movExpC1\">{eMovExpAnularDeuda}</div>\n"
                                                + "<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpNumExp}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(numExpediente).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpCodProc}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(codProcedimiento).append("</div>\n"
                                                + "</div>");
                                    textoXml.append("<div class=\"movExpC2\">LIQUIDACI\u00d3N ANULADA</div>\n");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">Tipo de liquidaci\u00f3n:</div>\n"
                                                + "<div class=\"movExpVal\">").append(tipoDeuda).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">N\u00famero de Liquidaci\u00f3n:</div>\n"
                                            + "<div class=\"movExpVal\">").append(numLiquidacion).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">Carta de Pago eliminada:</div>\n"
                                                + "<div class=\"movExpVal\">").append(nombreCarta).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpC2\">Motivo anulaci\u00f3n</div>\n"
                                                + "<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{gEtiq_codigo}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(motAnulacion).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{etiqDescripcion}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(descMotivo).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpObservac}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(observaciones).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpC2\">{eMovExpUsuFec}</div>\n"
                                                + "<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpUsuario}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(usuarioExpediente).append("</div>\n"
                                                + "</div>"
                                                + "<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpNomUsuario}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(nombreUsuario).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{gEtiqFecOpe}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(fechaOper).append("</div>\n"
                                                + "</div>");
                                        descripcion = textoXml.toString();

                                        operacion.setDescripcionOperacion(descripcion);
                                        con = adapt.getConnection();
                                        OperacionesExpedienteDAO.getInstance().insertarOperacionExpediente(operacion, con);
                                    } catch (Exception e) {
                                        log.error("  -- anularDeudaSubvenciones --  Ha ocurrido un error al grabar la operacion anularDeudaSubvenciones del expediente " + numExpediente + " - ", e);
                                    }

                                } else {
                                    log.error("  -- anularDeudaSubvenciones --  Ha ocurrido un error al borrar la deuda");
                                    return ERROR_BORRANDO_DEUDA;
                                }
                            }
                            log.info("anularDeudaSubvenciones - OK");
                            codOperacion = OPERACION_CORRECTA;
                            break;
                        case HttpStatus.SC_BAD_REQUEST:
                            respuestaJson = new JSONObject(respuestaString);
                            String codError = respuestaJson.getString("codigoError");
                            String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                            String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                            log.error("  --  anularDeudaSubvenciones  -- Error  al llamar a la ZORKU  - " + codError);
                            log.error("  --  anularDeudaSubvenciones  -- " + msgErrorCas);
                            log.error("  --  anularDeudaSubvenciones  -- " + msgErrorEus);
                        try {
                            // comprobamos si existe descripción para el código recibido de lo contrario se devuelve el mensaje de error en la respuesta
                            String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide92.BARRA_SEPARADORA + "MODULO_INTEGRACION" + ConstantesMeLanbide92.BARRA_SEPARADORA + "anularDeudaSubvenciones" + ConstantesMeLanbide92.BARRA_SEPARADORA + "MENSAJE_ERROR" + ConstantesMeLanbide92.BARRA_SEPARADORA + codError, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                            if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                log.debug("Nombre mensaje= " + nombreMensaje);
                                codOperacion = codError;
                            } else {
                                codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                            }
                        } catch (Exception e) {
                            codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                        }
                        break;
                    case HttpStatus.SC_FORBIDDEN:
                        log.error("  --  anularDeudaSubvenciones  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_403;
                            break;
                        case HttpStatus.SC_NOT_FOUND:
                            log.error("  --  anularDeudaSubvenciones  -- Error 404 al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_404;
                        break;
                    case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                        log.error("  --  anularDeudaSubvenciones  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_500;
                        break;
                    case HttpStatus.SC_SERVICE_UNAVAILABLE:
                        log.error("  --  anularDeudaSubvenciones  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_503;
                        break;
                        default:
                            log.error("  --  anularDeudaSubvenciones  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_LLAMADA_ANULAR;
                        break;
                }
            } catch (Exception e) {
                log.error("error en anularDanularDeudaSubvencioneseuda ", e);
                codOperacion = ERROR_LLAMADA_ANULAR;
            }
        } catch (SQLException e) {
            log.error("error en anularDanularDeudaSubvencioneseuda ", e);
            codOperacion = ERROR_LLAMADA_ANULAR;
        } finally {
            if (adapt != null) {
                adapt.devolverConexion(con);
            }
        }
        //Si todo va bien, todo correcto, devolvemos 0
        log.info("  -- anularDeudaSubvenciones --  Devolvemos: " + codOperacion);
        return codOperacion;
    }

    /**
     * operacion que llama al SW suspenderPeriodoPagoVoluntario de GEP para
     * suspender el periodo de pago voluntario de una deuda
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws BDException
     */
    public String suspenderPeriodoPagoVoluntario(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("===========>  suspenderPeriodoPagoVoluntarioSubvenciones ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_OPERACION;
        String ejercicio = "";
        String codProcedimiento = "";
        String numLiquidacion = "";
        String estadoDeuda = "";
        String fechaSuspension = "";
        SalidaIntegracionVO campoNumLiquidacion = null;
        String codCampoIdDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecSuspension = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_SUSPENSION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);

        MeLanbide92Manager m92Manager = null;
        AdaptadorSQLBD adaptador = null;
        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(ConstantesDatos.BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }
        try {
            m92Manager = MeLanbide92Manager.getInstance();
            adaptador = m92Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            campoNumLiquidacion = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            if ((campoNumLiquidacion.getStatus() == 0)) {
                numLiquidacion = campoNumLiquidacion.getCampoSuplementario().getValorTexto();
                try {
                    estadoDeuda = m92Manager.getEstadoDeudaZORKU(numLiquidacion, adaptador);
                } catch (Exception e) {
                    log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  Error al recuperar el estado de la deuda - " + e);
                    return ERROR_RECUPERANDO_ESTADO;
                }
                if (estadoDeuda.equalsIgnoreCase("1")) {
                    String urlSuspension = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_SUSPENSION_PERIODO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                    log.debug("URL: " + urlSuspension);
                    ParamsSeguridadZorku seguridadVO = MeLanbide92Utilidades.getSeguridad(codOrganizacion);
                    ParamsSuspenderPeriodoPago parametrosSuspension = new ParamsSuspenderPeriodoPago();
                    parametrosSuspension.setZorUsUsuario(seguridadVO.getUsuario());
                    parametrosSuspension.setZorUsPassword(seguridadVO.getContrasena());
                    parametrosSuspension.setArea(seguridadVO.getArea());

                    SalidaIntegracionVO campoFechaSuspension = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            codCampoFecSuspension, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                    // si tiene grabada la fecha de suspensión en el expediente la coje, si no se usa la fecha actual
                    Calendar fecSuspCal = null;
                    if (campoFechaSuspension.getStatus() == 0) {
                        fecSuspCal = campoFechaSuspension.getCampoSuplementario().getValorFecha();
                    } else {
                        fecSuspCal = Calendar.getInstance();
                    }
                    fechaSuspension = MeLanbide92Utilidades.convierteFechaZorku(fecSuspCal);
                    //formatoFecha.format(fechaSuspDate);
                    log.info(">>>>Llamamos al ws suspenderPeriodoPagoVoluntarioSubvenciones <<<<<<<<");
                    log.info("IdDeuda: " + numLiquidacion);
                    log.info("Fecha Suspensión: " + fechaSuspension);
                    parametrosSuspension.setZorLiNumLiquidacion(new BigDecimal(numLiquidacion));
                    parametrosSuspension.setZorLiFechaSuspension(fechaSuspension);
                    try {
                        HttpClient client = HttpClients.createDefault();
                        String jsonBody = new Gson().toJson(parametrosSuspension);
                        StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                        HttpPost llamadaSuspension = new HttpPost(urlSuspension);
                        llamadaSuspension.setHeader("Accept", "application/json");
                        llamadaSuspension.setEntity(entidadLlamada);
                        log.info("Llamando al servicio " + urlSuspension + " ... " + jsonBody);
                        HttpResponse respuesta = client.execute(llamadaSuspension);
                        // recojo respuesta
                        log.info("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  getStatusLine() : " + respuesta.getStatusLine());
                        int statusCode = respuesta.getStatusLine().getStatusCode();
                        log.info("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  getStatusLine().getStatusCode() : " + statusCode);
                        log.info("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  Hay respuesta de ZORKU");
                        HttpEntity entidadRespuesta = respuesta.getEntity();
                        // convierto la respuesta a STRING
                        String respuestaString = EntityUtils.toString(entidadRespuesta);
                        log.debug("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  Respuesta String: " + respuestaString);
                            // convierto a JSON
                            JSONObject respuestaJson;
                            switch (statusCode) {
                                case HttpStatus.SC_OK:
                                    log.info("Hay respuesta SIN ERROR del SW");
                                    respuestaJson = new JSONObject(respuestaString);
                                    codOperacion = OPERACION_CORRECTA;
                                    try {
                                        CampoSuplementarioModuloIntegracionVO campoFechaSusp = new CampoSuplementarioModuloIntegracionVO();
                                        campoFechaSusp.setCodOrganizacion(String.valueOf(codOrganizacion));
                                        campoFechaSusp.setCodProcedimiento(codProcedimiento);
                                        campoFechaSusp.setEjercicio(ejercicio);
                                        campoFechaSusp.setNumExpediente(numExpediente);
                                        campoFechaSusp.setTramite(false);
                                        campoFechaSusp.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                                        campoFechaSusp.setCodigoCampo(codCampoFecSuspension);
                                        campoFechaSusp.setValorFecha(fecSuspCal);
                                        SalidaIntegracionVO salidaGrabacion = gestorCampoSup.grabarCampoSuplementario(campoFechaSusp);
                                        if (salidaGrabacion != null && salidaGrabacion.getStatus() == 0) {
                                            log.info("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  Se graba el campo " + codCampoFecSuspension + " = " + Calendar.getInstance().toString());
                                            codOperacion = OPERACION_CORRECTA;
                                        } else {
                                            log.error("  -- suspenderPeriodoPagoVoluntarioSubvenciones --  Se ha producio un error grabando el valor del campo sumplementario Fecha Suspension.");
                                            codOperacion = ERROR_GRABAR_FECHA_SUSPENSION;
                                        }
                                    } catch (Exception e) {
                                        log.error("  -- suspenderPeriodoPagoVoluntarioSubvenciones --  Se ha producio un error grabando el valor del campo sumplementario Fecha Suspension. " + e);
                                        return ERROR_GRABAR_FECHA_SUSPENSION;
                                    }
                                    break;
                                case HttpStatus.SC_BAD_REQUEST:
                                    respuestaJson = new JSONObject(respuestaString);
                                    String codError = respuestaJson.getString("codigoError");
                                    String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                    String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                    log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  -- Error  al llamar a la ZORKU  - " + codError);
                                    log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  -- " + msgErrorCas);
                                    log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  -- " + msgErrorEus);
                                try {
                                    // comprobamos si existe descripción para el código recibido de lo contrario se devuelve el mensaje de error en la respuesta
                                    String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide92.BARRA_SEPARADORA + "MODULO_INTEGRACION" + ConstantesMeLanbide92.BARRA_SEPARADORA + "suspenderPeriodoPagoVoluntarioSubvenciones" + ConstantesMeLanbide92.BARRA_SEPARADORA + "MENSAJE_ERROR" + ConstantesMeLanbide92.BARRA_SEPARADORA + codError, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                                    if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                        log.debug("Nombre mensaje= " + nombreMensaje);
                                        codOperacion = codError;
                                    } else {
                                        codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                                    }
                                } catch (Exception e) {
                                    codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                                }
                                    break;
                            case HttpStatus.SC_FORBIDDEN:
                                log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                                return ERROR_403;
                                case HttpStatus.SC_NOT_FOUND:
                                    log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  -- Error 404 al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                    return ERROR_404;
                            case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                                return ERROR_500;
                            case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                                return ERROR_503;
                                default:
                                    log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                    return ERROR_LLAMADA_SUSPENSION;
                            }
                    } catch (IOException e) {
                        log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  -- IOException en la llamada al servicio Web. " + e);
                        return ERROR_LLAMADA_SUSPENSION;
                    } catch (UnsupportedCharsetException e) {
                        log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  UnsupportedCharsetException en la llamada al servicio Web. " + e);
                        return ERROR_LLAMADA_SUSPENSION;
                    } catch (ParseException e) {
                        log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  ParseException en la llamada al servicio Web. " + e);
                        return ERROR_LLAMADA_SUSPENSION;
                    } catch (JSONException e) {
                        log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  JSONException en la llamada al servicio Web. " + e);
                        return ERROR_LLAMADA_SUSPENSION;
                    }

                } else {
                    log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  La deuda no está Pendiente de Pago (estado 1)");
                    codOperacion = ERROR_ESTADO_PENDIENTE;
                }
            } else {
                log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  Se ha producio un error recuperando el valor del campo sumplementario IdDeuda.");
                codOperacion = ERROR_RECUPERANDO_ID_DEUDA_RES;
            }

        } catch (SQLException e) {
            log.error("  --  suspenderPeriodoPagoVoluntarioSubvenciones  --  Se ha producido un error en la llamada al servicio Web. " + e);
            return ERROR_LLAMADA_SUSPENSION;
        }
        log.info("<==== suspenderPeriodoPagoVoluntarioSubvenciones: devolvemos " + codOperacion);
        return codOperacion;
    }

    /**
     * operacion que llama al SW anularSuspensionPeriodoPagoVoluntario de GEP
     * para finalizar la suspesion del periodo de pago voluntario de una deuda
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws Exception
     */
    public String anularSuspensionPeriodoPagoVoluntario(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("===========>  anularSuspensionPeriodoPagoVoluntario ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_OPERACION;
        String ejercicio = "";
        String codProcedimiento = "";
        String numLiquidacion = "";
        String estadoDeuda = "";
        String codCampoIdDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        SalidaIntegracionVO campoNumLiquidacion = null;

        MeLanbide92Manager m92Manager = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;

        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(ConstantesDatos.BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }
        try {
            m92Manager = MeLanbide92Manager.getInstance();
            adapt = m92Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            campoNumLiquidacion = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            if ((campoNumLiquidacion.getStatus() == 0)) {
                numLiquidacion = campoNumLiquidacion.getCampoSuplementario().getValorTexto();
                try {
                    estadoDeuda = m92Manager.getEstadoDeudaZORKU(numLiquidacion, adapt);
                } catch (Exception e) {
                    log.error("  --  anularSuspensionPeriodoPagoVoluntario  --  Error al recuperar el estado de la deuda - " + e);
                    return ERROR_RECUPERANDO_ESTADO;
                }
                if (estadoDeuda.equalsIgnoreCase("30")) {
                    String urlFinalizarSusp = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_FINALIZAR_SUSP, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                    log.debug("URL: " + urlFinalizarSusp);
                    //Creo el VO de seguridad con un usuario SIPCA
                    ParamsSeguridadZorku seguridadVO = MeLanbide92Utilidades.getSeguridad(codOrganizacion);
                    log.info(">>>>Llamamos al ws anularSuspensionPeriodoPagoVoluntario <<<<<<<<");
                    log.info("IdDeuda: " + numLiquidacion);
                    ParamsFinalizarSuspensionPeriodo parametrosFinalizar = new ParamsFinalizarSuspensionPeriodo();
                    parametrosFinalizar.setZorUsUsuario(seguridadVO.getUsuario());
                    parametrosFinalizar.setZorUsPassword(seguridadVO.getContrasena());
                    parametrosFinalizar.setArea(seguridadVO.getArea());
                    parametrosFinalizar.setZorLiNumLiquidacion(new BigDecimal(numLiquidacion));
                    try {
                        HttpClient client = HttpClients.createDefault();
                        String jsonBody = new Gson().toJson(parametrosFinalizar);
                        StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                        HttpPost llamadaSuspension = new HttpPost(urlFinalizarSusp);
                        llamadaSuspension.setHeader("Accept", "application/json");
                        llamadaSuspension.setEntity(entidadLlamada);
                        log.info("Llamando al servicio " + urlFinalizarSusp + " ... " + jsonBody);
                        HttpResponse respuesta = client.execute(llamadaSuspension);
                        // recojo respuesta
                        log.info("  --  finalizarSuspensionPeriodo  --  getStatusLine() : " + respuesta.getStatusLine());
                        int statusCode = respuesta.getStatusLine().getStatusCode();
                        log.info("  --  finalizarSuspensionPeriodo  --  getStatusLine().getStatusCode() : " + statusCode);
                        log.info("  --  finalizarSuspensionPeriodo  --  Hay respuesta de ZORKU");
                        HttpEntity entidadRespuesta = respuesta.getEntity();
                        // convierto la respuesta a STRING
                        String respuestaString = EntityUtils.toString(entidadRespuesta);
                        log.debug("  --  finalizarSuspensionPeriodo  --  Respuesta String: " + respuestaString);
                        // convierto a JSON
                            JSONObject respuestaJson;
                            switch (statusCode) {
                                case HttpStatus.SC_OK:
                                    log.info("Hay respuesta SIN ERROR del SW");
                                    respuestaJson = new JSONObject(respuestaString);
                                    codOperacion = OPERACION_CORRECTA;
                                    break;
                                case HttpStatus.SC_BAD_REQUEST:
                                    respuestaJson = new JSONObject(respuestaString);
                                    String codError = respuestaJson.getString("codigoError");
                                    String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                    String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                    log.error("  --  finalizarSuspensionPeriodo  -- Error  al llamar a la ZORKU  - " + codError);
                                    log.error("  --  finalizarSuspensionPeriodo  -- " + msgErrorCas);
                                    log.error("  --  finalizarSuspensionPeriodo  -- " + msgErrorEus);
                                try {
                                    // comprobamos si existe descripción para el código recibido de lo contrario se devuelve el mensaje de error en la respuesta
                                    String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide92.BARRA_SEPARADORA + "MODULO_INTEGRACION" + ConstantesMeLanbide92.BARRA_SEPARADORA + "finalizarSuspensionPeriodo" + ConstantesMeLanbide92.BARRA_SEPARADORA + "MENSAJE_ERROR" + ConstantesMeLanbide92.BARRA_SEPARADORA + codError, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                                    if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                        log.debug("Nombre mensaje= " + nombreMensaje);
                                        codOperacion = codError;
                                    } else {
                                        codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                                    }
                                } catch (Exception e) {
                                    codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                                }
                                break;
                            case HttpStatus.SC_FORBIDDEN:
                                log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                                return ERROR_403;
                                case HttpStatus.SC_NOT_FOUND:
                                    log.error("  --  finalizarSuspensionPeriodo  -- Error 404 al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                    return ERROR_404;
                            case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                                return ERROR_500;
                            case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                                return ERROR_503;
                                default:
                                    log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                    return ERROR_LLAMADA_FIN_SUSP;
                            }
                    } catch (IOException ex) {
                        log.error("  --  anularSuspensionPeriodoPagoVoluntario  --  IOException en la llamada al servicio Web. " + ex.getMessage());
                        return ERROR_LLAMADA_FIN_SUSP;
                    } catch (UnsupportedCharsetException ex) {
                        log.error("  --  anularSuspensionPeriodoPagoVoluntario  --  UnsupportedCharsetException en la llamada al servicio Web. " + ex.getMessage());
                        return ERROR_LLAMADA_FIN_SUSP;
                    } catch (ParseException ex) {
                        log.error("  --  anularSuspensionPeriodoPagoVoluntario  --  ParseException en la llamada al servicio Web. " + ex.getMessage());
                        return ERROR_LLAMADA_FIN_SUSP;
                    } catch (JSONException ex) {
                        log.error("  --  anularSuspensionPeriodoPagoVoluntario  --  JSONException en la llamada al servicio Web. " + ex.getMessage());
                        return ERROR_LLAMADA_FIN_SUSP;
                    }

                    // llamada a ZORKU para anular la deuda
                    String motivo = "05";
                    String observaciones = "Anulada desde Regexlan  tras suspensión periodo pago";
                    String urlAnularDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_ANULACION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                    log.debug("URL: " + urlAnularDeuda);
                    ParamsAnularDeuda parametrosAnulacion = new ParamsAnularDeuda();
                    parametrosAnulacion.setZorUsUsuario(seguridadVO.getUsuario());
                    parametrosAnulacion.setZorUsPassword(seguridadVO.getContrasena());
                    parametrosAnulacion.setArea(seguridadVO.getArea());
                    parametrosAnulacion.setZorLiNumLiquidacion(new BigDecimal(numLiquidacion));
                    parametrosAnulacion.setZorMaCod(motivo);
                    parametrosAnulacion.setZorLiMotivoAnulacionTexto(observaciones);
                    log.info(">>>>Llamamos al ws para anular<<<<<<<<");
                    try {
                        HttpClient client = HttpClients.createDefault();
                        String jsonBody = new Gson().toJson(parametrosAnulacion);
                        StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                        HttpPost llamadaAnulacion = new HttpPost(urlAnularDeuda);
                        llamadaAnulacion.setHeader("Accept", "application/json");
                        llamadaAnulacion.setEntity(entidadLlamada);
                        log.info("Llamando al servicio " + urlAnularDeuda + " ... " + jsonBody);
                        HttpResponse respuesta = client.execute(llamadaAnulacion);
                        // recojo respuesta
                        log.info(" anularSuspensionPeriodoPagoVoluntario --  anularDeudaSubvenciones  --  getStatusLine() : " + respuesta.getStatusLine());
                        int statusCode = respuesta.getStatusLine().getStatusCode();
                        log.info(" anularSuspensionPeriodoPagoVoluntario --  anularDeudaSubvenciones  --  getStatusLine().getStatusCode() : " + statusCode);
                        log.info(" anularSuspensionPeriodoPagoVoluntario --  anularDeudaSubvenciones  --  Hay respuesta de ZORKU");
                        HttpEntity entidadRespuesta = respuesta.getEntity();
                        // convierto la respuesta a STRING
                        String respuestaString = EntityUtils.toString(entidadRespuesta);
                        log.debug(" anularSuspensionPeriodoPagoVoluntario --  anularDeudaSubvenciones  --  Respuesta String: " + respuestaString);
                            JSONObject respuestaJson;
                            switch (statusCode) {
                                case HttpStatus.SC_OK:
                                    respuestaJson = new JSONObject(respuestaString);
                                    log.info("anularDeudaSubvenciones - Hay respuesta SIN ERROR del SW");
                                    codOperacion = OPERACION_CORRECTA;
                                    try {
                                        String fechaOper = null;
                                        int usuarioExpediente = m92Manager.getUsuarioTramite(codOrganizacion, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, adapt);
                                        String nombreUsuario = m92Manager.getNombreUsuario(usuarioExpediente, adapt);
                                        OperacionExpedienteVO operacion = new OperacionExpedienteVO();
                                        //lleno el objeto
                                        log.debug("Lleno el Objeto operacionVO");
                                        operacion.setCodMunicipio(codOrganizacion);
                                        operacion.setEjercicio(Integer.parseInt(ejercicio));
                                        operacion.setNumExpediente(numExpediente);
                                    operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ANULAR_DEUDA);
                                        operacion.setFechaOperacion(new GregorianCalendar());
                                        operacion.setCodUsuario(usuarioExpediente);
                                        try {
                                            fechaOper = DateOperations.extraerFechaTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()))
                                                    + " " + DateOperations.extraerHoraTimeStamp(DateOperations.toTimestamp(operacion.getFechaOperacion()));
                                        } catch (Exception e) {
                                            log.error("Ha ocurrido un error al convertir la fecha de la operacion a String. ", e);
                                        }
                                        // descripcion
                                        log.debug("anularDeudaSubvenciones - Creo el XML de la operacion");
                                        String descripcion = null;
                                        StringBuilder textoXml = new StringBuilder("");
                                        textoXml.append("<div class=\"movExpC1\">{eMovExpAnularDeuda}</div>\n"
                                                + "<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpNumExp}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(numExpediente).append("</div>\n"
                                                + "</div>");

                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpCodProc}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(codProcedimiento).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpCodTramite}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(codTramite).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpOcurrTramite}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(ocurrenciaTramite).append("</div>\n"
                                                + "</div>");
                                    textoXml.append("<div class=\"movExpC2\">LIQUIDACI\u00d3N ANULADA:</div>\n");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">Tipo de liquidaci\u00f3n:</div>\n"
                                                + "<div class=\"movExpVal\">Fase Resoluci\u00f3n</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">N\u00famero de Liquidaci\u00f3n:</div>\n"
                                            + "<div class=\"movExpVal\">").append(numLiquidacion).append("</div>\n"
                                                + "</div>");

                                        textoXml.append("<div class=\"movExpC2\">MOTIVO ANULACI\u00d3N</div>\n"
                                                + "<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">C\u00f3digo:</div>\n"
                                                + "<div class=\"movExpVal\">05</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">Descripci\u00f3n:</div>\n"
                                                + "<div class=\"movExpVal\">Otros motivos</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpObservac}</div>\n"
                                                + "<div class=\"movExpVal\">").append(observaciones).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpC2\">{eMovExpUsuFec}</div>\n"
                                                + "<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpUsuario}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(usuarioExpediente).append("</div>\n"
                                                + "</div>"
                                                + "<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{eMovExpNomUsuario}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(nombreUsuario).append("</div>\n"
                                                + "</div>");
                                        textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">{gEtiqFecOpe}:</div>\n"
                                                + "<div class=\"movExpVal\">").append(fechaOper).append("</div>\n"
                                                + "</div>");
                                        descripcion = textoXml.toString();

                                        operacion.setDescripcionOperacion(descripcion);

                                        OperacionesExpedienteDAO.getInstance().insertarOperacionExpediente(operacion, con);
                                    } catch (Exception e) {
                                        log.error(" anularSuspensionPeriodoPagoVoluntario --  anularDeudaSubvenciones  -- ha ocurrido un error al grabar la operacion anularDeuda del expediente " + numExpediente + " - ", e);
                                    }
                                    log.info("  --  anularSuspensionPeriodoPagoVoluntario  -- OK");
                                    break;

                                case HttpStatus.SC_BAD_REQUEST:
                                    respuestaJson = new JSONObject(respuestaString);
                                    String codError = respuestaJson.getString("codigoError");
                                    String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                    String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                    log.error(" anularSuspensionPeriodoPagoVoluntario --  anularDeudaSubvenciones  -- Error  al llamar a la ZORKU  - " + codError);
                                    log.error(" anularSuspensionPeriodoPagoVoluntario --  anularDeudaSubvenciones  -- " + msgErrorCas);
                                    log.error(" anularSuspensionPeriodoPagoVoluntario --  anularDeudaSubvenciones  -- " + msgErrorEus);
                                    codOperacion = msgErrorCas;
                                    break;
                            case HttpStatus.SC_FORBIDDEN:
                                log.error("  --  anularSuspensionPeriodoPagoVoluntario  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                                return ERROR_403;
                                case HttpStatus.SC_NOT_FOUND:
                                    log.error("  --  anularSuspensionPeriodoPagoVoluntario  -- Error 404 en anularDeudaSubvenciones al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                    return ERROR_404;
                            case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                log.error("  --  anularSuspensionPeriodoPagoVoluntario  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + "  en anularDeudaSubvenciones al llamar a ZORKU ");
                                return ERROR_500;
                            case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                log.error("  --  anularSuspensionPeriodoPagoVoluntario  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + "  en anularDeudaSubvenciones al llamar a ZORKU ");
                                return ERROR_503;
                                default:
                                    log.error("  --  anularSuspensionPeriodoPagoVoluntario  -- Error en anularDeudaSubvenciones " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                    return ERROR_LLAMADA_FIN_SUSP;
                            }
                    } catch (IOException e) {
                        log.error("anularSuspensionPeriodoPagoVoluntario --  anularDeuda  --  Se ha producido IOException en la llamada al servicio Web. " + e.getMessage());
                        codOperacion = ERROR_LLAMADA_FIN_SUSP;
                    } catch (UnsupportedCharsetException e) {
                        log.error("anularSuspensionPeriodoPagoVoluntario --  anularDeuda  --  Se ha producido UnsupportedCharsetException en la llamada al servicio Web. " + e.getMessage());
                        codOperacion = ERROR_LLAMADA_FIN_SUSP;
                    } catch (ParseException e) {
                        log.error("anularSuspensionPeriodoPagoVoluntario --  anularDeuda  --  Se ha producido ParseException en la llamada al servicio Web. " + e.getMessage());
                        codOperacion = ERROR_LLAMADA_FIN_SUSP;
                    } catch (JSONException e) {
                        log.error("anularSuspensionPeriodoPagoVoluntario --  anularDeuda  --  Se ha producido JSONException en la llamada al servicio Web. " + e.getMessage());
                        codOperacion = ERROR_LLAMADA_FIN_SUSP;
                    }
                } else {
                    log.error("  --  anularSuspensionPeriodoPagoVoluntario  --   La deuda no está en Suspensión PV (estado 30)");
                    return ERROR_ESTADO_NO_SUSPENSION;
                }
            } else {
                log.error("  --  anularSuspensionPeriodoPagoVoluntario  --  Se ha producio un error recuperando el valor del campo sumplementario IdDeuda.");
                codOperacion = ERROR_RECUPERANDO_ID_DEUDA_RES;
            }
        } catch (SQLException ex) {
            log.error("  --  anularSuspensionPeriodoPagoVoluntario  --  Se ha producido un error en la llamada al servicio Web. " + ex.getMessage());
            return ERROR_LLAMADA_FIN_SUSP;
        } finally {
            if (adapt != null) {
                adapt.devolverConexion(con);
            }
        }
        log.info("<==== anularSuspensionPeriodoPagoVoluntario:devolvemos " + codOperacion);
        return codOperacion;
    }

    /**
     *
     * operacion de extension que llama al metodo
     * altaFraccionamientoSubvenciones() de ZORKU
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws Exception
     */
    public String altaFraccionamientoSubvenciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("===========>  altaFraccionamientoSubvenciones ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = OPERACION_CORRECTA;
        String ejercicio = "";
        String codProcedimiento = "";
        String numLiquidacionRes = "";
        String CTAIBAN = null;
        String CTAENTIDAD = null;
        String CTASUCURSAL = null;
        String CTADC = null;
        String CTANUMERO = null;
        String codCampoIdDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoIban = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTAIBAN, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTAENTIDAD, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoSucursal = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTASUCURSAL, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoDC = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTADC, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoNumeroCuenta = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTANUMERO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecSolicitudFracc = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FECHA_SOLICITUD_FRAC, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecLimitePago = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoImportePlazo = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_IMP_PLAZO_FRA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoPlazoFrac = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_MESES_FRA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);

        //Recuperamos el valor de la URL
        String urlFraccionamientoDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_FRACCIONAMIENTO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        BigDecimal plazo = new BigDecimal("0");
        BigDecimal cuotaSolicitada = new BigDecimal("0");
        Calendar fecSoliFrac = null;
        Calendar fecLimitePago = null;
        int codTramSolFracc = 0;
        int ocuTramSolFracc = 1;

        MeLanbide92Manager m92Manager = null;
        AdaptadorSQLBD adapt = null;

        try {
            if (numExpediente != null && !"".equals(numExpediente)) {
                String[] datos = numExpediente.split(ConstantesDatos.BARRA);
                ejercicio = datos[0];
                codProcedimiento = datos[1];
            }
            m92Manager = MeLanbide92Manager.getInstance();
            adapt = m92Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            codTramSolFracc = m92Manager.getCodigoInternoTramite(codOrganizacion, codProcedimiento, ConfigurationParameter.getParameter(ConstantesMeLanbide92.TRAMITE_FRACCIONAMIENTO, ConstantesMeLanbide92.FICHERO_PROPIEDADES), adapt);
            ocuTramSolFracc = m92Manager.getMaxOcurrenciaTramitexCodigo(codOrganizacion, numExpediente, codTramSolFracc, adapt);

            SalidaIntegracionVO campoFecSoliFraccionamiento = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramSolFracc, ocuTramSolFracc, codCampoFecSolicitudFracc, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
            SalidaIntegracionVO campoFecLimitePago = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoFecLimitePago, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
            SalidaIntegracionVO campoIBAN = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramSolFracc, ocuTramSolFracc, codCampoIban, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoEntidad = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramSolFracc, ocuTramSolFracc, codCampoEntidad, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoSucursal = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramSolFracc, ocuTramSolFracc, codCampoSucursal, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoDC = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramSolFracc, ocuTramSolFracc, codCampoDC, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoCuenta = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramSolFracc, ocuTramSolFracc, codCampoNumeroCuenta, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoPlazo = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramSolFracc, ocuTramSolFracc, codCampoPlazoFrac, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
            SalidaIntegracionVO campoImportePlazo = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramSolFracc, ocuTramSolFracc, codCampoImportePlazo, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
            SalidaIntegracionVO campoNumLiquidacionRes = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

            //Creo el VO de seguridad
            ParamsSeguridadZorku seguridadVO = MeLanbide92Utilidades.getSeguridad(codOrganizacion);

            if (campoFecSoliFraccionamiento.getStatus() == 0) {
                fecSoliFrac = campoFecSoliFraccionamiento.getCampoSuplementario().getValorFecha();
                log.info("  --  altaFraccionamientoSubvenciones  --   Fecha Solicitud Fraccionamiento: " + fecSoliFrac.getTime());
            } else {
                log.error("  --  altaFraccionamientoSubvenciones  --   Falta Fecha Solicitud Fraccionamiento: ");
                return ERROR_RECUPERANDO_FECHA_SOLI_FRAC;
            }
            if (campoFecLimitePago.getStatus() == 0) {
                fecLimitePago = campoFecLimitePago.getCampoSuplementario().getValorFecha();
                log.info("  --  altaFraccionamientoSubvenciones  --   Fecha Límite" + fecLimitePago);
            } else {
                log.error("  --  altaFraccionamientoSubvenciones  --   Falta Fecha Límite Pago ");
            }
            // comprobar si la fecha introducida es anterior o igual al vencimiento de la carta de pago o a HOY
            if (fecSoliFrac.after(Calendar.getInstance())) {
                log.error("  --  altaFraccionamientoSubvenciones  --   Fecha Solicitud MAYOR que hoy - " + (Calendar.getInstance().getTime()));
                return ERROR_FECHA_SOL_MAYOR_HOY;
            }
            if (fecSoliFrac.after(fecLimitePago)) {
                log.error("  --  altaFraccionamientoSubvenciones  --   Fecha Solicitud MAYOR que Limite Pago: ");
                return ERROR_FECHA_SOL_MAYOR_LIMITE;
            }
            //convertir el Calendar a formato ZORKU
            String fechaSolicZorku = null;
            if (fecSoliFrac != null) {
                fechaSolicZorku = MeLanbide92Utilidades.convierteFechaZorku(fecSoliFrac);
                log.debug("  --  altaFraccionamientoSubvenciones  --   fecha Formateada ZK: " + fechaSolicZorku);
            }

            // control datos
            if (campoNumLiquidacionRes.getStatus() == 0) {
                numLiquidacionRes = campoNumLiquidacionRes.getCampoSuplementario().getValorTexto();
                log.info("  --  altaFraccionamientoSubvenciones  --   idDeuda: " + numLiquidacionRes);
            } else {
                log.error("  --  altaFraccionamientoSubvenciones  --   Devolvemos: " + ERROR_RECUPERANDO_ID_DEUDA_RES);
                return ERROR_RECUPERANDO_ID_DEUDA_RES;
            }
            if (campoIBAN.getStatus() == 0) {
                CTAIBAN = campoIBAN.getCampoSuplementario().getValorTexto().toUpperCase();
            } else {
                log.error("  --  altaFraccionamientoSubvenciones  --   Falta IBAN ");
                return ERROR_RECUPERANDO_IBAN;
            }
            if (campoEntidad.getStatus() == 0) {
                CTAENTIDAD = campoEntidad.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  altaFraccionamientoSubvenciones  --   Falta ENTIDAD ");
                return ERROR_RECUPERANDO_ENTIDAD;
            }
            if (campoSucursal.getStatus() == 0) {
                CTASUCURSAL = campoSucursal.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  altaFraccionamientoSubvenciones  --   Falta SUCURSAL ");
                return ERROR_RECUPERANDO_SUCURSAL;
            }
            if (campoDC.getStatus() == 0) {
                CTADC = campoDC.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  altaFraccionamientoSubvenciones  --   Falta DC ");
                return ERROR_RECUPERANDO_DC;
            }
            if (campoCuenta.getStatus() == 0) {
                CTANUMERO = campoCuenta.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  altaFraccionamientoSubvenciones  --   Falta Nº CUENTA ");
                return ERROR_RECUPERANDO_NUM_CUENTA;
            }
            if (CTAIBAN.length() != 4 || CTAENTIDAD.length() != 4 || CTASUCURSAL.length() != 4 || CTADC.length() != 2 || CTANUMERO.length() != 10) {
                log.error("  --  altaFraccionamientoSubvenciones  --   El numero de cuenta no es valido - El tamaño de algún dato es incorrecto");
                return ERROR_CUENTA_TAMANIO;
            }
            String ibanCompleto = CTAIBAN + CTAENTIDAD + CTASUCURSAL + CTADC + CTANUMERO;
            if (!m92Utils.validaIBAN(ibanCompleto)) {
                log.error("  --  altaFraccionamientoSubvenciones  --   El numero de cuenta no es valido");
                return ERROR_CUENTA_INCORRECTA;
            }
            log.debug("  --  altaFraccionamientoSubvenciones  --   IBAN: " + CTAIBAN + " - Entidad: " + CTAENTIDAD + " - Suc: " + CTASUCURSAL + " - DC: " + CTADC + " - ");

            // cargo el objeto parametrosFracc
            log.info("  --  altaFraccionamientoSubvenciones  --   Paso los datos a parametrosFracc");
            ParamsAltaFraccionamiento parametrosFracc = new ParamsAltaFraccionamiento();
            parametrosFracc.setZorUsUsuario(seguridadVO.getUsuario());
            parametrosFracc.setZorUsPassword(seguridadVO.getContrasena());
            parametrosFracc.setArea(seguridadVO.getArea());
            parametrosFracc.setExpFraccionamiento(numExpediente);
            parametrosFracc.setZorLiFechaSolicitudFracc(fechaSolicZorku);
            parametrosFracc.setZorLiNumLiquidacion(new BigDecimal(numLiquidacionRes));
            parametrosFracc.setZorCbtIban(CTAIBAN);
            parametrosFracc.setZorCbtEntidad(CTAENTIDAD);
            parametrosFracc.setZorCbtSucursal(CTASUCURSAL);
            parametrosFracc.setZorCbtDc(CTADC);
            parametrosFracc.setZorCbtNumCuenta(CTANUMERO);

            if (campoImportePlazo.getStatus() == 0) {
                cuotaSolicitada = new BigDecimal(campoImportePlazo.getCampoSuplementario().getValorNumero());
                parametrosFracc.setCuotaSolicitada(cuotaSolicitada);
            }
            if (campoPlazo.getStatus() == 0) {
                plazo = new BigDecimal(campoPlazo.getCampoSuplementario().getValorNumero());
                parametrosFracc.setMesesAplazamiento(plazo);
            }

            //Ejecucion del SW de establecimiento de fechas de pago
            try {
                HttpClient client = HttpClients.createDefault();
                HttpPost llamadaFraccionamiento = new HttpPost(urlFraccionamientoDeuda);
                String jsonBody = new Gson().toJson(parametrosFracc);
                StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                llamadaFraccionamiento.setHeader("Accept", "application/json");
                llamadaFraccionamiento.setEntity(entidadLlamada);
                log.info("  --  altaFraccionamientoSubvenciones  --   Llamando al servicio " + urlFraccionamientoDeuda + " ... " + jsonBody);
                HttpResponse respuesta = client.execute(llamadaFraccionamiento);
                // recojo respuesta
                log.info("  --  altaFraccionamientoSubvenciones  --  getStatusLine() : " + respuesta.getStatusLine());
                int statusCode = respuesta.getStatusLine().getStatusCode();
                log.info("  --  altaFraccionamientoSubvenciones  --  getStatusLine().getStatusCode() : " + statusCode);
                log.info("  --  altaFraccionamientoSubvenciones  --  Hay respuesta de ZORKU");
                HttpEntity entidadRespuesta = respuesta.getEntity();
                // convierto la respuesta a STRING
                String respuestaString = EntityUtils.toString(entidadRespuesta);
                log.debug("  --  altaFraccionamientoSubvenciones  --  Respuesta String: " + respuestaString);
                // convierto a JSON
                JSONObject respuestaJson;
                switch (statusCode) {
                    case HttpStatus.SC_OK:
                        respuestaJson = new JSONObject(respuestaString);
                        log.info("  --  altaFraccionamientoSubvenciones  --  Fraccionamiento OK");
                        break;
                    case HttpStatus.SC_BAD_REQUEST:
                        respuestaJson = new JSONObject(respuestaString);
                        String codError = respuestaJson.getString("codigoError");
                        String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                        String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                        log.error("  --  altaFraccionamientoSubvenciones  -- Error  al llamar a ZORKU  - " + codError);
                        log.error("  --  altaFraccionamientoSubvenciones  -- " + msgErrorCas);
                        log.error("  --  altaFraccionamientoSubvenciones  -- " + msgErrorEus);
                        try {
                            // comprobamos si existe descripción para el código recibido de lo contrario se devuelve el mensaje de error en la respuesta
                            String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide92.BARRA_SEPARADORA + "MODULO_INTEGRACION" + ConstantesMeLanbide92.BARRA_SEPARADORA + "altaFraccionamientoSubvenciones" + ConstantesMeLanbide92.BARRA_SEPARADORA + "MENSAJE_ERROR" + ConstantesMeLanbide92.BARRA_SEPARADORA + codError, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                            if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                log.debug("Nombre mensaje= " + nombreMensaje);
                                codOperacion = codError;
                            } else {
                                codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                            }
                        } catch (Exception e) {
                            codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                        }
                        break;
                    case HttpStatus.SC_FORBIDDEN:
                        log.error("  --  altaFraccionamientoSubvenciones  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                        codOperacion = ERROR_403;
                        break;
                    case HttpStatus.SC_NOT_FOUND:
                        log.error("  --  altaFraccionamientoSubvenciones  -- Error 404 al llamar a ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_404;
                        break;
                    case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                        log.error("  --  altaFraccionamientoSubvenciones  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                        codOperacion = ERROR_500;
                        break;
                    case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                        log.error("  --  altaFraccionamientoSubvenciones  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                        codOperacion = ERROR_503;
                        break;
                    default:
                        log.error("  --  altaFraccionamientoSubvenciones  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                        codOperacion = ERROR_LLAMADA_FRACCIONAR;
                        break;
                }
                log.info("  --  altaFraccionamientoSubvenciones  --  FIN");
            } catch (IOException e) {
                log.error("  --  altaFraccionamientoSubvenciones  --  IOException  ", e);
                codOperacion = ERROR_LLAMADA_FRACCIONAR;
            } catch (UnsupportedCharsetException e) {
                log.error("  --  altaFraccionamientoSubvenciones  --  UnsupportedCharsetException ", e);
                codOperacion = ERROR_LLAMADA_FRACCIONAR;
            } catch (ParseException e) {
                log.error("  --  altaFraccionamientoSubvenciones  --  ParseException ", e);
                codOperacion = ERROR_LLAMADA_FRACCIONAR;
            } catch (JSONException e) {
                log.error("  --  altaFraccionamientoSubvenciones  --  JSONException ", e);
                codOperacion = ERROR_LLAMADA_FRACCIONAR;
            }
        } catch (Exception e) {
            log.error("error en altaFraccionamientoSubvenciones ", e);
            codOperacion = ERROR_LLAMADA_FRACCIONAR;
        }
        log.info("<==== altaFraccionamientoSubvenciones: devolvemos " + codOperacion);
        return codOperacion;
    }

    /**
     *
     * operacion de extension que llama al metodo cambioCuentaDomiciliacion() de ZORKU
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws Exception
     */
    public String cambioCuentaDomiciliacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("cambioCuentaDomiciliacion ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_OPERACION;
        String ejercicio = "";
        String codProcedimiento = "";
        String numLiquidacion = "";
        String numDocumento = null;
        String CTAIBAN = null;
        String CTAENTIDAD = null;
        String CTASUCURSAL = null;
        String CTADC = null;
        String CTANUMERO = null;
        String codCampoIdDeudaRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_ID_DEUDA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoIban = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTAIBAN, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoEntidad = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTAENTIDAD, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoSucursal = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTASUCURSAL, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoDC = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTADC, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoNumeroCuenta = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_CTANUMERO, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        //Recuperamos el valor de la URL
        String urlCambioCuenta = ConfigurationParameter.getParameter(ConstantesMeLanbide92.URL_CAMBIO_CUENTA, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        int codTramCambioCuenta = 0;
        int ocuTramCambioCuenta = 1;
        MeLanbide92Manager m92Manager = null;
        AdaptadorSQLBD adapt = null;

        try {
            if (numExpediente != null && !"".equals(numExpediente)) {
                String[] datos = numExpediente.split(ConstantesDatos.BARRA);
                ejercicio = datos[0];
                codProcedimiento = datos[1];
            }
            m92Manager = MeLanbide92Manager.getInstance();
            adapt = m92Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            codTramCambioCuenta = m92Manager.getCodigoInternoTramite(codOrganizacion, codProcedimiento, ConfigurationParameter.getParameter(ConstantesMeLanbide92.TRAMITE_CAMBIO_CUENTA, ConstantesMeLanbide92.FICHERO_PROPIEDADES), adapt);
            ocuTramCambioCuenta = m92Manager.getMaxOcurrenciaTramitexCodigo(codOrganizacion, numExpediente, codTramCambioCuenta, adapt);
            numDocumento = m92Manager.getDocumentoInteresado(codOrganizacion, numExpediente, adapt);
            SalidaIntegracionVO campoIBAN = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente,
                    codProcedimiento, codTramCambioCuenta, ocuTramCambioCuenta, codCampoIban, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoEntidad = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente,
                    codProcedimiento, codTramCambioCuenta, ocuTramCambioCuenta, codCampoEntidad, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoSucursal = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente,
                    codProcedimiento, codTramCambioCuenta, ocuTramCambioCuenta, codCampoSucursal, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoDC = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente,
                    codProcedimiento, codTramCambioCuenta, ocuTramCambioCuenta, codCampoDC, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoCuenta = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente,
                    codProcedimiento, codTramCambioCuenta, ocuTramCambioCuenta, codCampoNumeroCuenta, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoNumLiquidacionRes = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente,
                    codProcedimiento, codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            // control datos
            if (campoNumLiquidacionRes.getStatus() == 0) {
                numLiquidacion = campoNumLiquidacionRes.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaDomiciliacion  --   Devolvemos: " + ERROR_RECUPERANDO_ID_DEUDA_RES);
                return ERROR_RECUPERANDO_ID_DEUDA_RES;
            }
            if (campoIBAN.getStatus() == 0) {
                CTAIBAN = campoIBAN.getCampoSuplementario().getValorTexto().toUpperCase();
            } else {
                log.error("  --  cambioCuentaDomiciliacion  --   Falta IBAN ");
                return ERROR_RECUPERANDO_IBAN;
            }
            if (campoEntidad.getStatus() == 0) {
                CTAENTIDAD = campoEntidad.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaDomiciliacion  --   Falta ENTIDAD ");
                return ERROR_RECUPERANDO_ENTIDAD;
            }
            if (campoSucursal.getStatus() == 0) {
                CTASUCURSAL = campoSucursal.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaDomiciliacion  --   Falta SUCURSAL ");
                return ERROR_RECUPERANDO_SUCURSAL;
            }
            if (campoDC.getStatus() == 0) {
                CTADC = campoDC.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaDomiciliacion  --   Falta DC ");
                return ERROR_RECUPERANDO_DC;
            }
            if (campoCuenta.getStatus() == 0) {
                CTANUMERO = campoCuenta.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaDomiciliacion  --   Falta Nº CUENTA ");
                return ERROR_RECUPERANDO_NUM_CUENTA;
            }
            if (CTAIBAN.length() != 4 || CTAENTIDAD.length() != 4 || CTASUCURSAL.length() != 4 || CTADC.length() != 2 || CTANUMERO.length() != 10) {
                log.error("  --  cambioCuentaDomiciliacion  --   El numero de cuenta no es valido - El tamaño de algún dato es incorrecto");
                return ERROR_CUENTA_TAMANIO;
            }
            String ibanCompleto = CTAIBAN + CTAENTIDAD + CTASUCURSAL + CTADC + CTANUMERO;
            if (!m92Utils.validaIBAN(ibanCompleto)) {
                log.error("  --  cambioCuentaDomiciliacion  --   El numero de cuenta no es valido");
                return ERROR_CUENTA_INCORRECTA;
            }
            //Creo el VO de seguridad
            ParamsSeguridadZorku seguridadVO = MeLanbide92Utilidades.getSeguridad(codOrganizacion);
            ParamsCambioCuenta parametrosCambioCuenta = new ParamsCambioCuenta();
            // lleno el objeto
            // seguridad
            parametrosCambioCuenta.setZorUsUsuario(seguridadVO.getUsuario());
            parametrosCambioCuenta.setZorUsPassword(seguridadVO.getContrasena());
            parametrosCambioCuenta.setArea(seguridadVO.getArea());
            // interesado
            parametrosCambioCuenta.setNumDocumento(numDocumento);
            // liquidacion
            parametrosCambioCuenta.setZorLiNumLiquidacion(new BigDecimal(numLiquidacion));
            // datos bancarios
            parametrosCambioCuenta.setZorCbtIban(CTAIBAN);
            parametrosCambioCuenta.setZorCbtEntidad(CTAENTIDAD);
            parametrosCambioCuenta.setZorCbtSucursal(CTASUCURSAL);
            parametrosCambioCuenta.setZorCbtDc(CTADC);
            parametrosCambioCuenta.setZorCbtNumCuenta(CTANUMERO);
            // llamada
            try {
                HttpClient client = HttpClients.createDefault();
                HttpPost llamadaCambioCuenta = new HttpPost(urlCambioCuenta);
                String jsonBody = new Gson().toJson(parametrosCambioCuenta);
                StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                llamadaCambioCuenta.setHeader("Accept", "application/json");
                llamadaCambioCuenta.setEntity(entidadLlamada);
                // llamada a la API
                log.info("  --  cambioCuentaDomiciliacion  --   Llamando al servicio " + urlCambioCuenta + " ... " + jsonBody);
                HttpResponse respuesta = client.execute(llamadaCambioCuenta);
                // recojo respuesta
                log.info("  --  cambioCuentaDomiciliacion  --  getStatusLine() : " + respuesta.getStatusLine());
                int statusCode = respuesta.getStatusLine().getStatusCode();
                log.info("  --  cambioCuentaDomiciliacion  --  getStatusLine().getStatusCode() : " + statusCode);
                log.info("  --  cambioCuentaDomiciliacion  --  Hay respuesta de ZORKU");
                HttpEntity entidadRespuesta = respuesta.getEntity();
                // convierto la respuesta a STRING
                String respuestaString = EntityUtils.toString(entidadRespuesta);
                log.debug("  --  cambioCuentaDomiciliacion  --  Respuesta String: " + respuestaString);
                // convierto a JSON
                JSONObject respuestaJson;
                switch (statusCode) {
                    case HttpStatus.SC_OK:
                        respuestaJson = new JSONObject(respuestaString);
                        log.info("  --  cambioCuentaDomiciliacion  --   OK");
                        codOperacion = OPERACION_CORRECTA;
                        break;
                    case HttpStatus.SC_BAD_REQUEST:
                        respuestaJson = new JSONObject(respuestaString);
                        String codError = respuestaJson.getString("codigoError");
                        String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                        String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                        log.error("  --  cambioCuentaDomiciliacion  -- Error  al llamar a ZORKU  - " + codError);
                        log.error("  --  cambioCuentaDomiciliacion  -- " + msgErrorCas);
                        log.error("  --  cambioCuentaDomiciliacion  -- " + msgErrorEus);
                        try {
                            // comprobamos si existe descripción para el código recibido de lo contrario se devuelve el mensaje de error en la respuesta
                            String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide92.BARRA_SEPARADORA + "MODULO_INTEGRACION" + ConstantesMeLanbide92.BARRA_SEPARADORA + "altaFraccionamientoSubvenciones" + ConstantesMeLanbide92.BARRA_SEPARADORA + "MENSAJE_ERROR" + ConstantesMeLanbide92.BARRA_SEPARADORA + codError, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
                            if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                log.debug("Nombre mensaje= " + nombreMensaje);
                                codOperacion = codError;
                            } else {
                                codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                            }
                        } catch (Exception e) {
                            codOperacion = msgErrorCas + " " + ConstantesMeLanbide92.BARRA_SEPARADORA + " " + msgErrorEus;
                        }
                        break;
                    case HttpStatus.SC_FORBIDDEN:
                        log.error("  --  cambioCuentaDomiciliacion  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                        codOperacion = ERROR_403;
                        break;
                    case HttpStatus.SC_NOT_FOUND:
                        log.error("  --  cambioCuentaDomiciliacion  -- Error 404 al llamar a ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_404;
                        break;
                    case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                        log.error("  --  cambioCuentaDomiciliacion  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                        codOperacion = ERROR_500;
                        break;
                    case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                        log.error("  --  cambioCuentaDomiciliacion  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                        codOperacion = ERROR_503;
                        break;
                    default:
                        log.error("  --  cambioCuentaDomiciliacion  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                        codOperacion = ERROR_LLAMADA_CAMBIO_CUENTA;
                        break;
                }
            } catch (IOException e) {
                log.error("  --  cambioCuentaDomiciliacion  -- IOException ", e);
                codOperacion = ERROR_LLAMADA_CAMBIO_CUENTA;
            } catch (UnsupportedCharsetException e) {
                log.error("  --  cambioCuentaDomiciliacion  -- UnsupportedCharsetException ", e);
                codOperacion = ERROR_LLAMADA_CAMBIO_CUENTA;
            } catch (ParseException e) {
                log.error("  --  cambioCuentaDomiciliacion  -- ParseException ", e);
                codOperacion = ERROR_LLAMADA_CAMBIO_CUENTA;
            } catch (JSONException e) {
                log.error("  --  cambioCuentaDomiciliacion  -- JSONException ", e);
                codOperacion = ERROR_LLAMADA_CAMBIO_CUENTA;
            }
        } catch (Exception e) {
            log.error("error en cambioCuentaDomiciliacion ", e);
            codOperacion = ERROR_LLAMADA_CAMBIO_CUENTA;
        }
        log.info("<==== cambioCuentaDomiciliacion: devolvemos " + codOperacion);
        return codOperacion;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws Exception
     */
    public String compruebaFecAcuseFecResol(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("===========>  compruebaFecAcuseFecResol() : BEGIN");
        String codOperacion = ERROR_OPERACION;
        String codCampoFecResolucion = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_RESOLUCION, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecAcuseRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_ACUSE_NOTIF_RESOL, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String ejercicio = null;
        String codProcedimiento = null;
        int codTramRes = 0;
        int ocuTramRes = 1;
        int codTramAcuse = 0;
        int ocuTramAcuse = 1;
        Calendar fechaResolucion = null;
        Calendar fechaAcuseRes = null;
        SalidaIntegracionVO campoFecResolucion = null;
        SalidaIntegracionVO campoFecAcuseRes = null;

        MeLanbide92Manager m92Manager = null;
        AdaptadorSQLBD adapt = null;
        try {
            String[] datos = numExpediente.split(ConstantesDatos.BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
            m92Manager = MeLanbide92Manager.getInstance();
            adapt = m92Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            codTramRes = m92Manager.getCodigoInternoTramite(codOrganizacion, codProcedimiento, ConfigurationParameter.getParameter(ConstantesMeLanbide92.TRAMITE_RESOLUCION, ConstantesMeLanbide92.FICHERO_PROPIEDADES), adapt);
            ocuTramRes = m92Manager.getMaxOcurrenciaTramitexCodigo(codOrganizacion, numExpediente, codTramRes, adapt);
            codTramAcuse = m92Manager.getCodigoInternoTramite(codOrganizacion, codProcedimiento, ConfigurationParameter.getParameter(ConstantesMeLanbide92.TRAMITE_ACUSE_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES), adapt);
            ocuTramAcuse = m92Manager.getMaxOcurrenciaTramitexCodigo(codOrganizacion, numExpediente, codTramAcuse, adapt);
            campoFecResolucion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramRes, ocuTramRes, codCampoFecResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
            campoFecAcuseRes = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramAcuse, ocuTramAcuse, codCampoFecAcuseRes, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

            if (campoFecResolucion.getStatus() == 0) {
                fechaResolucion = campoFecResolucion.getCampoSuplementario().getValorFecha();

                if (campoFecAcuseRes.getStatus() == 0) {
                    fechaAcuseRes = campoFecAcuseRes.getCampoSuplementario().getValorFecha();

                    if (fechaResolucion.after(fechaAcuseRes) || fechaResolucion.equals(fechaAcuseRes)) {
                        codOperacion = OPERACION_CORRECTA;
                    } else {
                        log.info("compruebaFecAcuseFecResol: La fecha de Acuse notificación de la resolución es menor que la fecha de la resolución ");
                        codOperacion = ERROR_FECHA_ACUSE;
                    }
                } else {
                    log.error("  -- compruebaFecAcuseFecResol -- Error recuperando Fecha Acuse Resolución");
                    codOperacion = ERROR_RECUPERANDO_FECHA_ACUSE_RES;
                }
            } else {
                log.error("  -- compruebaFecAcuseFecResol -- Error recuperando Fecha Resolución");
                codOperacion = ERROR_RECUPERANDO_FECHA_RESOLUCION;
            }

        } catch (Exception ex) {
            log.error("Error en la funcion compruebaFecAcuseFecResol: ", ex);
        }
        return codOperacion;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws Exception
     */
    public String compruebaFecAcusefFecLimPago(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("===========>  compruebaFechaNotifFechaLimPago() : BEGIN");
        String codOperacion = ERROR_OPERACION;
        AdaptadorSQLBD adapt = null;
        String codCampoFecAcuseRes = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_ACUSE_NOTIF_RESOL, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String codCampoFecLimite = ConfigurationParameter.getParameter(ConstantesMeLanbide92.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES);
        String ejercicio = null;
        String codProcedimiento = null;
        int codTramAcuse = 0;
        int ocuTramAcuse = 1;
        Calendar fechaAcuseRes = null;
        Calendar fechaLimite = null;

        SalidaIntegracionVO campoFecAcuseRes = null;
        SalidaIntegracionVO campoFecLimite = null;

        MeLanbide92Manager m92Manager = null;
        try {
            String[] datos = numExpediente.split(ConstantesDatos.BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
            m92Manager = MeLanbide92Manager.getInstance();
            adapt = m92Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            codTramAcuse = m92Manager.getCodigoInternoTramite(codOrganizacion, codProcedimiento, ConfigurationParameter.getParameter(ConstantesMeLanbide92.TRAMITE_ACUSE_RES, ConstantesMeLanbide92.FICHERO_PROPIEDADES), adapt);
            ocuTramAcuse = m92Manager.getMaxOcurrenciaTramitexCodigo(codOrganizacion, numExpediente, codTramAcuse, adapt);
            campoFecAcuseRes = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codTramAcuse, ocuTramAcuse, codCampoFecAcuseRes, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
            campoFecLimite = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoFecLimite, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

            if (campoFecAcuseRes.getStatus() == 0) {
                fechaAcuseRes = campoFecAcuseRes.getCampoSuplementario().getValorFecha();
                if (campoFecLimite.getStatus() == 0) {
                    fechaLimite = campoFecLimite.getCampoSuplementario().getValorFecha();
                    if (fechaAcuseRes.before(fechaLimite)) {
                        codOperacion = OPERACION_CORRECTA;
                    } else {
                        log.info("compruebaFechaNotifFechaLimPagoREINT: La fecha de Acuse notificación de la resolución es posterior a la fecha de límite de pago. ");
                        codOperacion = ERROR_FECHA_LIMITE;
                    }
                } else {
                    log.error("  -- compruebaFechaNotifFechaLimPago -- Error recuperando Fecha Límite Pago");
                    codOperacion = ERROR_RECUPERANDO_FEC_LIMITE_P_RES;
                }
            } else {
                log.error("  -- compruebaFechaNotifFechaLimPago -- Error recuperando Fecha Acuse Resolución");
                codOperacion = ERROR_RECUPERANDO_FECHA_ACUSE_RES;
            }
        } catch (Exception e) {
        }
        return codOperacion;
    }

}
