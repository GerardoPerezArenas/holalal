package es.altia.flexia.integracion.moduloexterno.melanbide07;

import com.google.gson.Gson;
import es.altia.agora.business.sge.OperacionExpedienteVO;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide07.dao.MeLanbide07DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide07.manager.MeLanbide07Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConstantesMeLanbide07;
import es.altia.flexia.integracion.moduloexterno.melanbide07.util.UtilidadesREINT;
import es.altia.flexia.integracion.moduloexterno.melanbide07.vo.ParamsAltaDeuda;
import es.altia.flexia.integracion.moduloexterno.melanbide07.vo.ParamsAltaFraccionamiento;
import es.altia.flexia.integracion.moduloexterno.melanbide07.vo.ParamsAnularDeuda;
import es.altia.flexia.integracion.moduloexterno.melanbide07.vo.ParamsCambioCuenta;
import es.altia.flexia.integracion.moduloexterno.melanbide07.vo.ParamsFinalizarSuspensionPeriodo;
import es.altia.flexia.integracion.moduloexterno.melanbide07.vo.ParamsSeguridadZorku;
import es.altia.flexia.integracion.moduloexterno.melanbide07.vo.ParamsSuspenderPeriodoPago;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DomicilioInteresadoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.InteresadoExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

/**
 *
 * <ol> <li></li> </ol>
 */
public class MELANBIDE07 extends ModuloIntegracionExterno {

    //Logger
    private static final Logger log = LogManager.getLogger(MELANBIDE07.class);
    //Constantes de la clase
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final String BARRA = "/";
    private final String MODULO_INTEGRACION = "MODULO_INTEGRACION";
    private final IModuloIntegracionExternoCamposFlexia gestorCampoSup = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

    //Codigos de error
    private static final String OPERACION_CORRECTA = "0";
    private static final String ERROR_LLAMADA_SW = "1";
    private static final String ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS = "2";
    private static final String ERROR_IMPORTE_NEGATIVO = "3";
    private static final String ERROR_RECUPERANDO_FECHA_LIMITE_PAGO = "4";
    private static final String ERROR_RECUPERANDO_FEC_LIMITE_P_INI = "4";
    private static final String ERROR_RECUPERANDO_FEC_LIMITE_P_SOL = "4";
    private static final String ERROR_DATOS_OBLIGATORIOS = "5";
    private static final String ERROR_TIPO_DOCUMENTO = "6";
    private static final String ERROR_MUNICIPIO = "7";
    private static final String ERROR_DOCUMENTO = "8";
    private static final String ERROR_CONVIRTIENDO_IMPORTE_DEUDA = "9";
    private static final String ERROR_PROVINCIA = "10";
    private static final String ERROR_NUMERO_EXPEDIENTE = "11";
    private static final String ERROR_CODIGO_AREA_WS = "12";
    private static final String ERROR_ESTADO_PENDIENTE = "13";
    private static final String ERROR_VIA = "14";
    private static final String ERROR_RECUPERANDO_ID_DEUDA = "15";
    private static final String ERROR_CP = "16";
    private static final String ERROR_BORRANDO_CARTA = "17";
    private static final String ERROR_ANIO_CONVOCATORIA = "18";
    private static final String ERROR_COD_SUBVENCION = "19";
    private static final String ERROR_BORRANDO_DEUDA = "20";
    private static final String ERROR_BORRANDO_FEC_VENC = "21";
    private static final String ERROR_RECUPERANDO_MOT_DEUDA = "22";
    private static final String ERROR_BORRANDO_CHECK = "23";
    private static final String ERROR_RECUPERANDO_ID_DEUDA_INI = "24";
    private static final String ERROR_PARSEANDO_FECHAS = "25";
    private static final String ERROR_RECUPERANDO_IBAN = "26";
    private static final String ERROR_CUENTA_INCORRECTA = "27";
    private static final String ERROR_RECUPERANDO_FECHA_SOLI_FRAC = "28";
    private static final String ERROR_FECHA_SOL_MAYOR_LIMITE = "29";
    private static final String ERROR_FECHA_SOL_MAYOR_HOY = "30";
    private static final String ERROR_RECUPERANDO_ESTADO = "31";
    private static final String ERROR_ANULAR_SUSPENSION = "32";
    private static final String ERROR_ESTADO_NO_SUSPENSION = "33";
    private static final String ERROR_RECUPERANDO_ENTIDAD = "34";
    private static final String ERROR_RECUPERANDO_SUCURSAL = "35";
    private static final String ERROR_RECUPERANDO_DC = "36";
    private static final String ERROR_RECUPERANDO_NUM_CUENTA = "37";
    private static final String ERROR_CUENTA_TAMANIO = "38";
    private static final String ERROR_DATOS_SIN_GUARDAR = "39";
    private static final String ERROR_RECUPERANDO_IMPORTE_INI = "41";
    private static final String ERROR_RECUPERANDO_IMPORTE_RES = "42";
    private static final String ERROR_RECUPERANDO_IMPORTE_SUS = "43";
    private static final String ERROR_RECUPERANDO_FECHA_REQUER = "44";
    private static final String ERROR_RECUPERANDO_FECHA_RESOLUCION = "45";
    private static final String ERROR_RECUPERANDO_FECHA_ACTIVACION = "46";
    private static final String ERROR_RECUPERANDO_FECHA_SUSPENSION = "47";

    private static final String ERROR_GENERICO = "-1";
    private static final String ERROR_403 = "403";
    private static final String ERROR_404 = "404";
    private static final String ERROR_500 = "500";
    private static final String ERROR_503 = "503";

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     * @throws BDException
     * @throws SQLException
     */
    public String cargarPantalla(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws BDException, SQLException {
        log.debug("CARGAR PANTALLA");
        String ejercicio = "";
        String codProcedimiento = "";
        String cTramite = request.getParameter("codTramite");
        String ocuTramite = request.getParameter("ocurrenciaTramite");
        log.info("Tramite: " + cTramite + " - Ocurrencia: " + ocuTramite);

        String idDeudaIni = "";
        String idDeuda = "";
        String idDeudaSusp = "";
        String deudaPagada = "N";
        boolean ayudaNoAbonada = false;
        String alega = "N";

        try {
            if (numExpediente != null && !"".equals(numExpediente)) {
                String[] datos = numExpediente.split(BARRA);
                ejercicio = datos[0];
                codProcedimiento = datos[1];
            }
            String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codTramiteReqReintegro = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_REQ_REINTEGRO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codTramiteResolucion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_RESOLUCION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codTramiteSuspension = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_SUSPENSION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codTramiteAlegaciones = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_ALEGACIONES, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            String codigoCampoIdDeudaIni = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA_INI, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codigoCampoIdDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codigoCampoImporte = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_IMPORTE_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codigoCampoFecPagoAleg = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_PAGO_DEUDA_INI, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codigoCampoAyudaAbonada = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_AYUDA_NO_ABONADA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codigoCampoResultadoAlegaciones = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_RESULTADO_ALEGACIONES, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            SalidaIntegracionVO campoAyudaNoAbo = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codigoCampoAyudaAbonada, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            if (campoAyudaNoAbo.getStatus() == 0) {
                if (campoAyudaNoAbo.getCampoSuplementario().getValorTexto().equalsIgnoreCase("X")) {
                    log.info("La Ayuda no está abonada: ");
                    ayudaNoAbonada = true;
                    deudaPagada = "S";
                }
            }

// O1 REQUERIMIENTO REINTEGRO
            if (cTramite.equals(codTramiteReqReintegro)) {
                log.debug("REQUERIMIENTO");
                idDeudaIni = MeLanbide07Manager.getInstance().getValorCampoTextoExpediente(codOrganizacion, numExpediente, ejercicio, codigoCampoIdDeudaIni, codProcedimiento, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (idDeudaIni == null) {
                    idDeudaIni = "";
                }
            } else if (cTramite.equals(codTramiteResolucion)) {
// O4 RESOLUCION 
                log.debug("RESOLUCIÓN");
                idDeuda = MeLanbide07Manager.getInstance().getValorCampoTextoExpediente(codOrganizacion, numExpediente, ejercicio, codigoCampoIdDeuda, codProcedimiento, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (idDeuda == null) {
                    idDeuda = "";
                }
                idDeudaIni = MeLanbide07Manager.getInstance().getValorCampoTextoExpediente(codOrganizacion, numExpediente, ejercicio, codigoCampoIdDeudaIni, codProcedimiento, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (idDeudaIni == null) {
                    idDeudaIni = "";
                }
                SalidaIntegracionVO campoResultadoAlegaciones = gestorCampoSup.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteAlegaciones), 1, codigoCampoResultadoAlegaciones, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
                if (campoResultadoAlegaciones != null && campoResultadoAlegaciones.getStatus() == 0) {
                    if (campoResultadoAlegaciones.getCampoDesplegable().getCodigo().equalsIgnoreCase("T") || campoResultadoAlegaciones.getCampoDesplegable().getCodigo().equalsIgnoreCase("P")) {
                        alega = "S";
                    }
                }
                // FECHA DE PAGO PERIODO ALEGACIONES
                SalidaIntegracionVO campoFecPagoAlegaciones = null;
                campoFecPagoAlegaciones = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion),
                        ejercicio, numExpediente,
                        codProcedimiento, codigoCampoFecPagoAleg, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                if (campoFecPagoAlegaciones != null && campoFecPagoAlegaciones.getStatus() == 0) {
                    log.info("Tiene Fecha pago Deuda Alegaciones: ");
                    deudaPagada = "S";
                    // grabo un 0 en el importe deuda resolucion
                    int grabaOK = MeLanbide07Manager.getInstance().guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(cTramite), 1, codigoCampoImporte, BigDecimal.ZERO, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (grabaOK <= 0) {
                        log.error("Excepción al grabar el campo suplementario " + codigoCampoImporte);
                    } else {
                        log.info("Grabado el valor 0 en " + codigoCampoImporte);
                    }

                }
            } else if (cTramite.equals(codTramiteSuspension)) {
// 98 SUSPENSION      
                log.debug("SUSPENSION");
                String codigoCampoIdDeudaSusp = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA_SUSP, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                idDeudaSusp = MeLanbide07Manager.getInstance().getValorCampoTextoExpediente(codOrganizacion, numExpediente, ejercicio, codigoCampoIdDeudaSusp, codProcedimiento, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (idDeudaSusp == null) {
                    idDeudaSusp = "";
                }
            }

            log.debug("tr Llamada " + cTramite);
            log.debug("tr Reintegro " + codTramiteReqReintegro);
            log.debug("tr resolucion " + codTramiteResolucion);
            log.debug("tr Suspension" + codTramiteSuspension);

            request.setAttribute("codigoTramiteReqReintegro", codTramiteReqReintegro);
            request.setAttribute("codigoTramiteResolucion", codTramiteResolucion);
            request.setAttribute("codigoTramiteSuspension", codTramiteSuspension);
            request.setAttribute("valorIdDeuda", idDeuda);
            request.setAttribute("valorIdDeudaIni", idDeudaIni);
            request.setAttribute("valorIdDeudaSusp", idDeudaSusp);
            request.setAttribute("deudaPagada", deudaPagada);
            request.setAttribute("alega", alega);

        } catch (SQLException ex) {
            log.error("Error SQL : " + ex.getMessage());

        } catch (Exception ex) {
            log.error("Error general : " + ex.getMessage());
        }
        return "/jsp/extension/melanbide07/melanbide07_ET.jsp";
    }

    public String cargarValidaNumExp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        return "/jsp/extension/melanbide07/validaNumExp.jsp";
    }

    public String validarNumeroExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        log.info("validarNumeroExpediente: BEGIN");
        String numeroExpediente = request.getParameter("numeroExpediente");
        Connection con = null;
        String resultado = OPERACION_CORRECTA;
        try {
            con = this.getAdaptSQLBD(String.valueOf(codOrganizacion)).getConnection();
            resultado = MeLanbide07DAO.getInstance().validarNumeroExpediente(numeroExpediente, con);

            String xmlSalida = obtenerXmlSalidaExp(resultado);
            retornarXML(xmlSalida, response);
        } catch (Exception e) {
            //Resultado con error
            log.error("error general en validar número expediente ", e);
            resultado = "2";
        } finally {
            if (con != null) {
                con.close();
            }
        }
        return resultado;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @throws BDException
     * @throws SQLException
     */
    public void validarCamposDeuda(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws BDException, SQLException {
        // tram 01 _ REQUERIMIENTO
        String cImporteDeudaIni = request.getParameter("importeDeudaIni");
        if (cImporteDeudaIni.contains(".")) {
            cImporteDeudaIni = cImporteDeudaIni.replace(".", "");
            log.debug(cImporteDeudaIni);
        }
        if (cImporteDeudaIni.contains(",")) {
            cImporteDeudaIni = cImporteDeudaIni.replace(",", ".");
            log.debug(cImporteDeudaIni);
        }
        String cFechaReqDeuda = request.getParameter("fechaReqDeuda");

        // tram 04 - RESOLUCION
        String cImporteDeuda = request.getParameter("importeDeuda");
        if (cImporteDeuda.contains(".")) {
            cImporteDeuda = cImporteDeuda.replace(".", "");
        }
        if (cImporteDeuda.contains(",")) {
            cImporteDeuda = cImporteDeuda.replace(",", ".");
        }
        String cFecResolucion = request.getParameter("fecResolucion");

        // tram 98 - SUSPENSION
        String cImporteDeudaSusp = request.getParameter("importeDeudaSusp");
        if (cImporteDeudaSusp.contains(".")) {
            cImporteDeudaSusp = cImporteDeudaSusp.replace(".", "");
        }
        if (cImporteDeudaSusp.contains(",")) {
            cImporteDeudaSusp = cImporteDeudaSusp.replace(",", ".");
        }
        String cFecActivacion = request.getParameter("fecActivacion");

        // comunes
        String resultado = "1";
        String ejercicio = "";
        String codProcedimiento = "";
        String cTramite = request.getParameter("codTramite");
        log.info(">>>>>>>>>> Parametros request");
        log.info("TRAMITE " + cTramite);

        MeLanbide07Manager manager = MeLanbide07Manager.getInstance();
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
//        IModuloIntegracionExternoCamposFlexia gestorCampoSup = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

        BigDecimal importeDeudaIni = BigDecimal.ZERO;
        String fechaReqDeuda = "";

        BigDecimal importeDeuda = BigDecimal.ZERO;
        String fechaResolucion = "";

        BigDecimal importeDeudaSusp = BigDecimal.ZERO;
        String fechaActivacion = "";
        try {
            if (numExpediente != null && !"".equals(numExpediente)) {
                String[] datos = numExpediente.split(BARRA);
                ejercicio = datos[0];
                codProcedimiento = datos[1];
            }
            String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codTramiteRequerimiento = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_REQ_REINTEGRO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            int ocurrenciaTramiteRequerimiento = MeLanbide07Manager.getInstance().getMaxOcurrenciaTramitexCodigo(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramiteRequerimiento), adaptador);
            String codTramiteResolucion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_RESOLUCION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            int ocurrenciaTramiteResolucion = MeLanbide07Manager.getInstance().getMaxOcurrenciaTramitexCodigo(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramiteResolucion), adaptador);
            String codTramiteSuspension = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_SUSPENSION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            int ocurrenciaTramiteSuspension = MeLanbide07Manager.getInstance().getMaxOcurrenciaTramitexCodigo(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramiteSuspension), adaptador);

            if (cTramite.equals(codTramiteRequerimiento)) {  // tram 01 _ REQUERIMIENTO
                log.info("Importe Deuda Inicial: " + cImporteDeudaIni);
                log.info("Fecha Requerimiento: " + cFechaReqDeuda);
                log.info(" 01 REQUERIMIENTO");
                String codigoCampoImporteDeudaIni = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_IMPORTE_DEUDA_INI, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                String codigoCampoFecReqDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_REQ_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

                importeDeudaIni = manager.getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, Integer.parseInt(codTramiteRequerimiento), ocurrenciaTramiteRequerimiento, codigoCampoImporteDeudaIni, adaptador);

                SalidaIntegracionVO campoFecReqDeuda = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteRequerimiento), ocurrenciaTramiteRequerimiento, codigoCampoFecReqDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                if (campoFecReqDeuda.getStatus() == 0) {
                    Calendar fecReqDeuda = campoFecReqDeuda.getCampoSuplementario().getValorFecha();
                    fechaReqDeuda = formatoFecha.format(fecReqDeuda.getTime());
                }

                log.info(">>>>>>>>>>>>>   De BBDD");
                log.info("Importe inicial : " + importeDeudaIni);
                log.info("Fecha Requerimiento: " + fechaReqDeuda);

                if (importeDeudaIni.toPlainString().equals(cImporteDeudaIni) && fechaReqDeuda.equals(cFechaReqDeuda)) {
                    resultado = OPERACION_CORRECTA;
                } else {
                    resultado = ERROR_DATOS_SIN_GUARDAR;
                }

            } else if (cTramite.equals(codTramiteResolucion)) { // tram 04 - RESOLUCION
                log.info("Importe Deuda: " + cImporteDeuda);
                log.info("Fecha Resolucion: " + cFecResolucion);
                log.info(" 04 RESOLUCIÓN");
                String codigoCampoImporteDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_IMPORTE_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                String codigoCampoFecResolucion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_RESOLUCION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

                importeDeuda = manager.getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, Integer.parseInt(codTramiteResolucion), ocurrenciaTramiteResolucion, codigoCampoImporteDeuda, adaptador);

                SalidaIntegracionVO campoFecResolucion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteResolucion), ocurrenciaTramiteResolucion, codigoCampoFecResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                if (campoFecResolucion.getStatus() == 0) {
                    Calendar fecResolucion = campoFecResolucion.getCampoSuplementario().getValorFecha();
                    fechaResolucion = formatoFecha.format(fecResolucion.getTime());
                } else {
                    log.error("  -- validarCampos -- Error recuperando Fecha Resolución");
                    resultado = ERROR_RECUPERANDO_FECHA_RESOLUCION;
                }

                log.info(">>>>>>>>>>>>>   De BBDD");
                log.info("Importe : " + importeDeuda);
                log.info("Fecha Resolucion: " + fechaResolucion);

                if (importeDeuda.toPlainString().equals(cImporteDeuda) && fechaResolucion.equals(cFecResolucion)) {
                    resultado = OPERACION_CORRECTA;
                } else {
                    resultado = ERROR_DATOS_SIN_GUARDAR;
                }

            } else if (cTramite.equals(codTramiteSuspension)) { // tram 98 - SUSPENSIÓN
                log.info("Importe Deuda Susp: " + cImporteDeudaSusp);
                log.info("Fecha Activacion: " + cFecActivacion);
                log.info(" 98 SUSPENSIÓN");
                String codigoCampoImporteDeudaSusp = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_IMPORTE_DEUDA_SUSP, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                String codigoCampoFecResolucion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_RESOLUCION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                String codigoCampoFecActivacion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_ACTIVACION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

                importeDeudaSusp = manager.getValorCampoNumericoTramite(codOrganizacion, numExpediente, ejercicio, Integer.parseInt(codTramiteSuspension), ocurrenciaTramiteSuspension, codigoCampoImporteDeudaSusp, adaptador);

                SalidaIntegracionVO campoFecResolucion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteResolucion), ocurrenciaTramiteResolucion, codigoCampoFecResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                SalidaIntegracionVO campoFecActivacion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteSuspension), ocurrenciaTramiteSuspension, codigoCampoFecActivacion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                if (campoFecResolucion.getStatus() == 0) {
                    Calendar fecResolucion = campoFecResolucion.getCampoSuplementario().getValorFecha();
                    fechaResolucion = formatoFecha.format(fecResolucion.getTime());
                } else {
                    log.error("  -- validarCampos -- Error recuperando Fecha Resolución");
                    resultado = ERROR_RECUPERANDO_FECHA_RESOLUCION;
                }

                if (campoFecActivacion.getStatus() == 0) {
                    Calendar fecActivacion = campoFecActivacion.getCampoSuplementario().getValorFecha();
                    fechaActivacion = formatoFecha.format(fecActivacion.getTime());
                } else {
                    log.error("  -- validarCampos -- Error recuperando Fecha de Activación del Periodo de Pago");
                    resultado = ERROR_RECUPERANDO_FECHA_ACTIVACION;
                }
                log.info(">>>>>>>>>>>>>   De BBDD");
                log.info("Importe : " + importeDeudaSusp);
                log.info("Fecha Resolucion: " + fechaResolucion);
                log.info("Fecha Activacion: " + fechaActivacion);
                if (importeDeudaSusp.toPlainString().equals(cImporteDeudaSusp) && fechaActivacion.equals(cFecActivacion)) {
                    resultado = OPERACION_CORRECTA;
                } else {
                    resultado = ERROR_DATOS_SIN_GUARDAR;
                }
            }
        } catch (Exception ex) {
            log.error("Error general : " + ex.getMessage());
            resultado = ERROR_GENERICO;
            }
        String xmlSalida = obtenerXmlSalida(resultado, "");
        retornarXML(xmlSalida, response);
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
    public String compruebaFechaNotifFechaResolREINT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        String codOperacion = OPERACION_CORRECTA;
        try {
            MeLanbide07Manager meLanbide07Manager = MeLanbide07Manager.getInstance();
            if (!meLanbide07Manager.compruebaFechaNotifFechaResolREINT(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)))) {
                codOperacion = "1";
                log.info("compruebaFechaNotifFechaResolREINT: La fecha de notificación de la resolución es menor que la fecha de la resolución ");
            }
        } catch (Exception ex) {
            codOperacion = "2";
            log.error("Error en la funcion compruebaFechaNotifREINT: ", ex);

        }
        log.info("compruebaFechaNotifFechaResolREINT() : END => " + codOperacion);
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
    public String compruebaFechaNotifFechaLimPagoREINT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        String codOperacion = OPERACION_CORRECTA;
        AdaptadorSQLBD adaptador = null;
        String[] datos = numExpediente.split(ConstantesMeLanbide07.BARRA_SEPARADORA);
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide07Manager meLanbide07Manager = MeLanbide07Manager.getInstance();
            // si la deuda no esta abonada devuelvo 0 
            if (meLanbide07Manager.deudaAbonada(codOrganizacion, Integer.parseInt(datos[0]), numExpediente, adaptador)) {
                if (!meLanbide07Manager.compruebaFechaNotifFechaLimPagoREINT(numExpediente, adaptador)) {
                    codOperacion = "1";
                    log.info("compruebaFechaNotifFechaLimPagoREINT: La fecha de notificación de la resolución es posterior a la fecha de límite de pago. ");
                }
            }
        } catch (Exception ex) {
            codOperacion = "2";
            log.error("Error en la función compruebaFechaNotifREINT: ", ex);
        }
        log.info("compruebaFechaNotifFechaLimPagoREINT() : END => " + codOperacion);
        return codOperacion;
    }

    // TAREA #336868
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
     * @throws SQLException
     * @throws BDException
     */
    public String llamaAlServicioWebaltaCartaPagoNoRGI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException, BDException {
        log.info("llamaAlServicioWebaltaCartaPagoNoRGI / altaDeudaSubvenciones ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite
                + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");

        String cTramite = request.getParameter("codTramite");
        String ocuTramite = request.getParameter("ocuTramite");
        MeLanbide07Manager manager = MeLanbide07Manager.getInstance();

        String xmlSalida = null;
        String codOperacion = OPERACION_CORRECTA;
        AdaptadorSQLBD adaptador = null;
        Connection con = null;

        String idDeuda = "";
        String nombreCarta = null;

        String ejercicio = "";
        String codProcedimiento = "";

        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }//if(numExpediente!=null && !"".equals(numExpediente))

        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adaptador.getConnection();
            String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            Calendar fechaActual = Calendar.getInstance();
// parametros de entrada
// obligatorios
            String zorDeCodSubvencion = null;
            String zorLaEjercicio = "";
            String zorDirCp = "";
            String zorEstado = "";
            BigDecimal zorLiImporteDeuda = null;
            BigDecimal intereses = BigDecimal.ZERO;
            String zorTerNombre = "";
            String gerDeNombreCalle = "";
            String municipioId = "";
            String provinciaId = "";
            String zorTerNumDocumento = "";
            String zorTerTipoDocumento = "";
            int tipoDocumento = -1;
            String zorTipoPago = "";
            String codigoAreaSW = null;
            String zorDirDireccionCompleta = "";
// obligatorios condicionados
            String zorTerApellido1 = ""; //Obligatorio si Tipo del Documento gerDeTipoDocumento != 1 (cif)
            String zorDeExpInicial = ""; //Obligatorio si la deuda pertenece a procedimientos de SIPCA

            Calendar gerDeFechaPagoExpedienteSubvencion = null; // Obligatorio si gerDeIdDeuda = nulo
            Calendar gerDeFechaRequerimiento = null; // Obligatorio si gerDeIdDeuda = nulo
            Calendar gerDeFechaResolucion = null; // Obligatorio si gerDeIdDeuda NO nulo
            Calendar valorFechaLimitePago = null;
            Calendar fSusp = null;
            String zorLiFechaPagoExpSubv = null;
            String zorLiFechaRequerimiento = null;
            String zorLiFechaResolucion = null;
            String zorLiFechaLimitePago = null;
            String zorFechaPagoDeudaIni = null;
// no obligatorios
            String zorTerApellido2 = "";
            String zorTerEmail = "";
            String codigoVia = "";
            String zorTerTelefono = "";
            String zorLiNumLiquidacion = "";
            String sinInteres = "";
            // FECHA DE PAGO PERIODO ALEGACIONES
            Calendar fecPagoDeudaIniPeriodoAlegaciones = null;

            // suplementarios a recuperar
            String codCampoIdDeudaRes = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoIdDeudaIni = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA_INI, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoIdDeudaSusp = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA_SUSP, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoImporteDeudaRes = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_IMPORTE_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoImporteDeudaIni = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_IMPORTE_DEUDA_INI, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoImporteDeudaSusp = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_IMPORTE_DEUDA_SUSP, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFecPagExpSubv = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_PAGO_EXP_SUBV, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFecReqDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_REQ_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFecResolucion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_RESOLUCION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFecActivacion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_ACTIVACION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFecSuspension = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_SUSPENSION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoSinInteres = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_SIN_INTERES, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoIntereses = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_INTERES_DEMORA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoCodAreaSW = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CODIGO_AREA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoAnioConvocatoria = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ANIO_CONVO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFecPagoDeudaIni = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_PAGO_DEUDA_INI, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            //No confundir con nuestro numero de expediente de Flexia
            String codCampoExpedienteSubv = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_NUMERO_EXPEDIENTE, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            String codTramiteRequerimiento = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_REQ_REINTEGRO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codTramiteResolucion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_RESOLUCION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codTramiteSuspension = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_SUSPENSION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            int ocurrenciaTramiteRequerimiento = MeLanbide07Manager.getInstance().getMaxOcurrenciaTramitexCodigo(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramiteRequerimiento), adaptador);
            int ocurrenciaTramiteResolucion = MeLanbide07Manager.getInstance().getMaxOcurrenciaTramitexCodigo(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramiteResolucion), adaptador);
            int ocurrenciaTramiteSuspension = MeLanbide07Manager.getInstance().getMaxOcurrenciaTramitexCodigo(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramiteSuspension), adaptador);

            SalidaIntegracionVO campoIdDeuda = null;
            SalidaIntegracionVO campoImporteDeuda = null;
            SalidaIntegracionVO campoFecPagExpSubv = null;
            SalidaIntegracionVO campoFecReqDeuda = null;
            SalidaIntegracionVO campoFecResolucion = null;
            SalidaIntegracionVO campoFecActivacion = null;
            SalidaIntegracionVO campoFecSuspension = null;
            SalidaIntegracionVO campoNumExpInicial = null;
            SalidaIntegracionVO campoCodArea = null;
            SalidaIntegracionVO campoFecPagoDeudaIni = null;
            SalidaIntegracionVO campoSinInteres = null;
            SalidaIntegracionVO campoAnioConvocatoria = null;

            //Recuperamos el valor de la URL y del QName
            String urlAltaDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_ALTA_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String urlActualizarDom = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_ACTUALIZAR_DOMICILIO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            //Creo el VO de seguridad con un usuario SIPCA
            ParamsSeguridadZorku seguridadVO = UtilidadesREINT.getSeguridad(codOrganizacion);
            log.debug("área: " + seguridadVO.getArea());

// RECOJO LOS CAMPOS SUPLEMENTARIOS DE EXPEDIENTE Y TRAMITE QUE TENGO QUE PASAR A LA LLAMADA
            try {
                log.debug("---- altaDeudaSubvenciones - Recojo suplementarios - FECHAS");

                campoFecResolucion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteResolucion), ocurrenciaTramiteResolucion, codCampoFecResolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                campoFecPagExpSubv = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoFecPagExpSubv, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                campoFecReqDeuda = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteRequerimiento), ocurrenciaTramiteRequerimiento, codCampoFecReqDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                campoNumExpInicial = gestorCampoSup.getCampoSuplementarioExpediente((Integer.toString(codOrganizacion)), ejercicio, numExpediente, codProcedimiento,
                        codCampoExpedienteSubv, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                campoCodArea = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoCodAreaSW, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);

                campoAnioConvocatoria = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoAnioConvocatoria, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
            } catch (NumberFormatException e) {

            }
            if ((campoFecResolucion.getStatus() == 0)) {
                gerDeFechaResolucion = campoFecResolucion.getCampoSuplementario().getValorFecha();
            }
// Tramite 04 - RESOLUCION
            if (codTramiteResolucion.equals(cTramite)) {
                log.info("Tramite 04 - RESOLUCION");
                // IDDEUDAINI - IMPORTEDEUDA - FECRESOLUCION - FECLIMITEPAGO
                campoIdDeuda = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoIdDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                if (campoIdDeuda.getStatus() == 0) {
                    zorLiNumLiquidacion = campoIdDeuda.getCampoSuplementario().getValorTexto();
                } else {
                    xmlSalida = obtenerXmlSalida(ERROR_RECUPERANDO_ID_DEUDA, "");
                    retornarXML(xmlSalida, response);
                }
                // #414909              
                try {
                    valorFechaLimitePago = fechaActual;
                    valorFechaLimitePago.add(Calendar.MONTH, 3);

                } catch (Exception e) {
                    log.error("altaDeudaSubvenciones.Devolvemos: " + ERROR_RECUPERANDO_FECHA_LIMITE_PAGO);
                    xmlSalida = obtenerXmlSalida(ERROR_RECUPERANDO_FECHA_LIMITE_PAGO, "");
                    retornarXML(xmlSalida, response);
                    return ERROR_RECUPERANDO_FECHA_LIMITE_PAGO;
                }

                campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteResolucion), ocurrenciaTramiteResolucion, codCampoImporteDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);

                //#376840
                campoFecPagoDeudaIni = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoFecPagoDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                if (campoFecPagoDeudaIni != null && campoFecPagoDeudaIni.getStatus() == 0) {
                    fecPagoDeudaIniPeriodoAlegaciones = campoFecPagoDeudaIni.getCampoSuplementario().getValorFecha();
                    log.info("Fecha pago Deuda Alegaciones: " + fecPagoDeudaIniPeriodoAlegaciones.toString());
                }

                campoSinInteres = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteResolucion), ocurrenciaTramiteResolucion, codCampoSinInteres, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);

                if (campoSinInteres.getStatus() == 0) {
                    String valor = campoSinInteres.getCampoSuplementario().getValorDesplegable();
                    if (valor.equalsIgnoreCase("X")) {
                        sinInteres = "1";
                    }
                }
// ---------------------------------------------------------------------------------------------------------------                
            } else if (codTramiteRequerimiento.equals(cTramite)) {
// Tramite 01 - REQUERIMIENTO
                log.info("Tramite 01 - REQUERIMIENTO");
                // IMPORTEDEUDAINI - FECHAREQDEUDA - FECLIMITEPAGOINI - 

                try {// #414909 
                    valorFechaLimitePago = fechaActual;
                    valorFechaLimitePago.add(Calendar.MONTH, 1);
                } catch (Exception e) {
                    log.error("altaDeudaSubvenciones.Devolvemos: " + ERROR_RECUPERANDO_FEC_LIMITE_P_INI);
                    xmlSalida = obtenerXmlSalida(ERROR_RECUPERANDO_FEC_LIMITE_P_INI, "");
                    retornarXML(xmlSalida, response);
                    return ERROR_RECUPERANDO_FEC_LIMITE_P_INI;
                }

                campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteRequerimiento), ocurrenciaTramiteRequerimiento, codCampoImporteDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
// ---------------------------------------------------------------------------------------------------------------                
            } else if (codTramiteSuspension.equals(cTramite)) {
// Tramite 98 - SUSPENSION
                log.info("Tramite 98 - SUSPENSION");
                // IMPORTEDEUDASUSP - IDDEUDA - 
                campoIdDeuda = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteSuspension), ocurrenciaTramiteSuspension, codCampoImporteDeudaSusp, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                if ((campoIdDeuda.getStatus() == 0)) {
                    zorLiNumLiquidacion = campoIdDeuda.getCampoSuplementario().getValorTexto();
                } else {
                    xmlSalida = obtenerXmlSalida(ERROR_RECUPERANDO_ID_DEUDA, "");
                    retornarXML(xmlSalida, response);
                }
                campoFecSuspension = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoFecSuspension, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                campoFecActivacion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramiteSuspension), ocurrenciaTramiteSuspension, codCampoFecActivacion, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                if (campoFecSuspension.getStatus() == 0) {
                    fSusp = campoFecSuspension.getCampoSuplementario().getValorFecha();
                } else {
                    xmlSalida = obtenerXmlSalida(ERROR_DATOS_OBLIGATORIOS, "");// fecha suspension
                    retornarXML(xmlSalida, response);
                }
                if (campoFecActivacion.getStatus() == 0) {
                    valorFechaLimitePago = campoFecActivacion.getCampoSuplementario().getValorFecha();
                } else {
                    xmlSalida = obtenerXmlSalida(ERROR_RECUPERANDO_FEC_LIMITE_P_SOL, "");
                    retornarXML(xmlSalida, response);
                }
                // calcular gerDeFechaLimitePago = fecha activacion + (90 dias - ( fecha suspension - fecha resolucion que genero la carta de pago) + 10 dias)
                int diasDif = 0;
                diasDif = UtilidadesREINT.restarFechas(fSusp.getTime(), gerDeFechaResolucion.getTime());
                log.info("Dias Diferencia " + fSusp.getTime().toString() + " ==> " + gerDeFechaResolucion.getTime().toString() + "  = " + diasDif);
                int diferenciaFinal = 100 + diasDif;
                log.info("Diferencia final: " + diferenciaFinal);
                //   gerDeFechaLimitePago = fAct;
                log.info("Fecha Activacion: " + valorFechaLimitePago.getTime().toString());
                valorFechaLimitePago.add(Calendar.DAY_OF_YEAR, diferenciaFinal);
                log.info("Fecha limite pago: " + valorFechaLimitePago.getTime().toString());
            }
// ---------------------------------------------------------------------------------------------------------------                

            log.debug("---- altaDeudaSubvenciones - Paso suplementarios a variables");

            if ((campoImporteDeuda.getStatus() == 0)) {
                try {
                    zorLiImporteDeuda = new BigDecimal(campoImporteDeuda.getCampoSuplementario().getValorNumero());
                    if (zorLiImporteDeuda.compareTo(BigDecimal.ZERO) < 0) {
                        log.error("Importe negativo: " + zorLiImporteDeuda);
                        xmlSalida = obtenerXmlSalida(ERROR_IMPORTE_NEGATIVO, "");
                        retornarXML(xmlSalida, response);
                    }
                } catch (Exception e) {
                    log.error("Salta excepcion al convertir a numero el importe de la deuda");

                    log.error("altaDeudaSubvenciones.Devolvemos: " + ERROR_CONVIRTIENDO_IMPORTE_DEUDA);
                    xmlSalida = obtenerXmlSalida(ERROR_CONVIRTIENDO_IMPORTE_DEUDA, "");
                    retornarXML(xmlSalida, response);
                }
            }

            // codigo area acceso
            if (campoCodArea.getStatus() == 0) {
                codigoAreaSW = campoCodArea.getCampoSuplementario().getValorDesplegable();
            }
            int codArea = Integer.parseInt(codigoAreaSW);
            int zorArCodigo = UtilidadesREINT.dameAcCodZorku(codArea);

            if ((campoFecPagExpSubv.getStatus() == 0)) {
                gerDeFechaPagoExpedienteSubvencion = campoFecPagExpSubv.getCampoSuplementario().getValorFecha();
            } else {
                xmlSalida = obtenerXmlSalida(ERROR_DATOS_OBLIGATORIOS, "");
                retornarXML(xmlSalida, response);
                return ERROR_DATOS_OBLIGATORIOS;
            }
            if (codTramiteSuspension.equals(cTramite)) {
                gerDeFechaRequerimiento = Calendar.getInstance();
            } else {
                if ((campoFecReqDeuda.getStatus() == 0)) {
                    gerDeFechaRequerimiento = campoFecReqDeuda.getCampoSuplementario().getValorFecha();
                }
            }

            if (campoNumExpInicial.getStatus() == 0) {
                zorDeExpInicial = campoNumExpInicial.getCampoSuplementario().getValorTexto();
                log.debug("Exp Inicial: " + zorDeExpInicial);
            } else {
                xmlSalida = obtenerXmlSalida(ERROR_NUMERO_EXPEDIENTE, "");
                retornarXML(xmlSalida, response);
                return ERROR_NUMERO_EXPEDIENTE;
            }
            // recojo el valor del anio de la subvencion
            if (campoAnioConvocatoria.getStatus() == 0) {
                zorLaEjercicio = campoAnioConvocatoria.getCampoSuplementario().getValorDesplegable();
                log.debug("Año Convocatoria ayuda: " + zorLaEjercicio);
            } else {
                log.error("---- altaDeudaSubvenciones - No existe el año de subvención");
                xmlSalida = obtenerXmlSalida(ERROR_ANIO_CONVOCATORIA, "");
                retornarXML(xmlSalida, response);
                return ERROR_ANIO_CONVOCATORIA;
            }
            // con el ANIO y el procedimiento del expediente inicial obtengo el codigo de la subvencion
            zorDeCodSubvencion = MeLanbide07Manager.getInstance().getCodigoSubvencionEika(zorLaEjercicio, zorDeExpInicial.split(BARRA)[1], adaptador);
            if (zorDeCodSubvencion == null || zorDeCodSubvencion.isEmpty()) {
                log.error("---- altaDeudaSubvenciones - No existe el código de subvención");
                xmlSalida = obtenerXmlSalida(ERROR_COD_SUBVENCION, "");
                retornarXML(xmlSalida, response);
                return ERROR_COD_SUBVENCION;
            }
            log.debug("---- altaDeudaSubvenciones - Recojo suplementarios - INTERESADO");
// RECOJO LOS DATOS DEL INTERESADO
            // cp - nombre - ape1 - ape2 - nombre calle - id muni - id prov - numdoc - territorio - tipo doc
            SalidaIntegracionVO salida = gestorCampoSup.getExpediente(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio);
            InteresadoExpedienteModuloIntegracionVO interesado = new InteresadoExpedienteModuloIntegracionVO();
            if (salida != null) {
                if (salida.getExpediente() != null) {
                    if ((salida.getExpediente().getInteresados() != null) && (!salida.getExpediente().getInteresados().isEmpty())) {
                        for (int i = 0; i < salida.getExpediente().getInteresados().size(); i++) {
                            interesado = salida.getExpediente().getInteresados().get(i);
                            log.debug("ROL: " + interesado.getCodigoRol());
                            if (interesado.getCodigoRol() == 1) {
                                if (interesado.getNombre() != null) {
                                    zorTerNombre = interesado.getNombre();
                                }
                                if (interesado.getApellido1() != null) {
                                    zorTerApellido1 = interesado.getApellido1();
                                }
                                if (interesado.getApellido2() != null) {
                                    zorTerApellido2 = interesado.getApellido2();
                                }
                                if (interesado.getEmail() != null) {
                                    zorTerEmail = interesado.getEmail();
                                }
                                if (interesado.getDocumento() != null) {
                                    zorTerNumDocumento = interesado.getDocumento();
                                }
                                if (interesado.getNumeroTelefonoFax() != null) {
                                    //  gerDeTelefono = interesado.getNumeroTelefonoFax();
                                    zorTerTelefono = UtilidadesREINT.recuperaCifrasTelefono(interesado.getNumeroTelefonoFax());
                                }
                                tipoDocumento = interesado.getTipoDocumento();

                                zorTerTipoDocumento = UtilidadesREINT.traducirTipoDocZorku(String.valueOf(tipoDocumento));
                                break;
                            }
                        }
                    }
                }//if interesado
                // domicilio
                if ((interesado.getDomicilios() != null) && (!interesado.getDomicilios().isEmpty())) {
                    int codDomicilioExpediente = interesado.getCodDomicilioExpediente();
                    if (log.isDebugEnabled()) {
                        log.info("CodDomicilio Expediente " + codDomicilioExpediente);
                    }
                    DomicilioInteresadoModuloIntegracionVO domicilio = new DomicilioInteresadoModuloIntegracionVO();
                    for (int i = 0; i < interesado.getDomicilios().size(); i++) {
                        domicilio = interesado.getDomicilios().get(i);
                        String codDom = domicilio.getIdDomicilio();
                        if (log.isDebugEnabled()) {
                            log.debug("codDomicilio " + i + " Interesado " + codDom);
                        }
                        if ((Integer.toString(codDomicilioExpediente)).equals(codDom)) {
                            break;
                        } else {
                            domicilio = interesado.getDomicilios().get(0);
                        }

                    }//for domicilios
                    if (log.isDebugEnabled()) {
                        log.info("He recogido el codDomicilio  " + domicilio.getIdDomicilio());
                    }
                    municipioId = domicilio.getIdMunicipio();
                    provinciaId = domicilio.getIdProvincia();
                    gerDeNombreCalle = domicilio.getDescripcionVia();
                    codigoVia = manager.dameCodigoTipoVia(codDomicilioExpediente, adaptador);
                    if (codigoVia != null && !"".equals(codigoVia)) {
                        if (codigoVia.length() > 2) {
                            codigoVia = codigoVia.substring(0, 2);
                        }
                        if (!manager.coincideCodigo(codigoVia, adaptador)) {
                            log.info("No coincide codigo, paso 'CL'");
                            codigoVia = "CL";
                        }
                    } else {
                        codigoVia = "CL";
                    }
                    zorDirDireccionCompleta = codigoVia + " " + gerDeNombreCalle + " " + (domicilio.getNumDesde() != null ? domicilio.getNumDesde() + " " : "") + (domicilio.getPortal() != null && !domicilio.getPortal().isEmpty() ? domicilio.getPortal() + " " : "") + (domicilio.getBloque() != null && !domicilio.getBloque().isEmpty() ? domicilio.getBloque() + " " : "") + (domicilio.getEscalera() != null && !domicilio.getEscalera().isEmpty() ? domicilio.getEscalera() + " " : "") + (domicilio.getPlanta() != null && !domicilio.getPlanta().isEmpty() ? domicilio.getPlanta() + " " : "") + (domicilio.getPuerta() != null && !domicilio.getPuerta().isEmpty() ? domicilio.getPuerta() : "");

                    zorDirCp = domicilio.getCodigoPostal();
                }//salida.getExpediente() != null
            } //salida != null

            // estado deuda
            zorEstado = ConfigurationParameter.getParameter(ConstantesMeLanbide07.ESTADO_DEUDA_PENDIENTE, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            // tipo de pago
            zorTipoPago = ConfigurationParameter.getParameter(ConstantesMeLanbide07.TIPO_PAGO_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            // control de datos obligatorios
            log.debug("---- altaDeudaSubvenciones - Control datos ");

            if ((("".equals(zorDirCp)) || (zorDirCp == null))) {
                log.error("--- ERROR  CP");
                xmlSalida = obtenerXmlSalida(ERROR_CP, "");
                retornarXML(xmlSalida, response);
                return ERROR_CP;
            }
            if (valorFechaLimitePago == null) {
                log.error("--- ERROR  FECHA LIMITE");
                xmlSalida = obtenerXmlSalida(ERROR_RECUPERANDO_FECHA_LIMITE_PAGO, "");
                retornarXML(xmlSalida, response);
                return ERROR_RECUPERANDO_FECHA_LIMITE_PAGO;
            }
            if ((("".equals(gerDeNombreCalle)) || (gerDeNombreCalle == null))) {
                log.error("--- ERROR NOMBRE CALLE ");
                xmlSalida = obtenerXmlSalida(ERROR_VIA, "");
                retornarXML(xmlSalida, response);
                return ERROR_VIA;
            }
            if ((("".equals(municipioId)) || (municipioId == null))) {
                log.error("--- ERROR  ID MUNICIPIO");
                xmlSalida = obtenerXmlSalida(ERROR_MUNICIPIO, "");
                retornarXML(xmlSalida, response);
                return ERROR_MUNICIPIO;
            }
            if ((("".equals(provinciaId)) || (provinciaId == null)) || (!"1".equals(provinciaId)) && (!"48".equals(provinciaId)) && (!"20".equals(provinciaId))) {
                log.error("--- ERROR ID PROVINCIA ");
                xmlSalida = obtenerXmlSalida(ERROR_PROVINCIA, "");
                retornarXML(xmlSalida, response);
                return ERROR_PROVINCIA;
            }
            if ((("".equals(zorTerNumDocumento)) || (zorTerNumDocumento == null))) {
                log.error("--- ERROR  NUMERO DOCUMENTO");
                xmlSalida = obtenerXmlSalida(ERROR_DOCUMENTO, "");
                retornarXML(xmlSalida, response);
                return ERROR_DOCUMENTO;
            }
            if ((("".equals(zorTerTipoDocumento)) || (zorTerTipoDocumento == null) || (!"1".equals(zorTerTipoDocumento)) && (!"2".equals(zorTerTipoDocumento)) && (!"3".equals(zorTerTipoDocumento)) && (!"4".equals(zorTerTipoDocumento)) && (!"5".equals(zorTerTipoDocumento)))) {
                log.error("--- ERROR  TIPO DOCUMENTO");
                xmlSalida = obtenerXmlSalida(ERROR_TIPO_DOCUMENTO, "");
                retornarXML(xmlSalida, response);
                return ERROR_TIPO_DOCUMENTO;
            }

            if ((zorArCodigo == 0)) {
                log.error("--- ERROR  CODIGO AREA ACCESO");
                xmlSalida = obtenerXmlSalida(ERROR_CODIGO_AREA_WS, "");
                retornarXML(xmlSalida, response);
                return ERROR_CODIGO_AREA_WS;
            }

            // ZORKU
            ParamsAltaDeuda parametrosAlta = new ParamsAltaDeuda();

            parametrosAlta.setZorUsUsuario(seguridadVO.getUsuario());
            parametrosAlta.setZorUsPassword(seguridadVO.getContrasena());
            parametrosAlta.setArea(seguridadVO.getArea());
            // Area Acceso          
            parametrosAlta.setZorArCodigo(new BigDecimal(zorArCodigo));
            parametrosAlta.setZorLaEjercicio(new BigDecimal(zorLaEjercicio));
            // Expediente
            parametrosAlta.setZorDeExpediente(zorDeExpInicial);
            parametrosAlta.setZorDeExpRei(numExpediente);
            parametrosAlta.setZorDeExpInicial(zorDeExpInicial);
            if (provinciaId.equals("48")) {
                parametrosAlta.setZorDeTerritorio("1");
            } else if (provinciaId.equals("20")) {
                parametrosAlta.setZorDeTerritorio("2");
            } else if (provinciaId.equals("1")) {
                parametrosAlta.setZorDeTerritorio("3");
                } else {
                parametrosAlta.setZorDeTerritorio("4");
                }
            parametrosAlta.setZorDeCodSubvencion(zorDeCodSubvencion);
            // datos interesado
            parametrosAlta.setZorTerTipoDocumento(zorTerTipoDocumento);
            parametrosAlta.setZorTerNumDocumento(zorTerNumDocumento);
            parametrosAlta.setZorTerNombre(zorTerNombre);
            parametrosAlta.setZorTerApellido1(zorTerApellido1);
            parametrosAlta.setZorTerApellido2(zorTerApellido2);
            parametrosAlta.setZorTerEmail(zorTerEmail);
            if (!zorTerTelefono.isEmpty()) {
                parametrosAlta.setZorTerTelefono(new BigDecimal(zorTerTelefono));
            }
            // domicilio
            parametrosAlta.setZorDirProv(UtilidadesREINT.traducirIdProvincia(provinciaId));
            parametrosAlta.setZorDirMunicipio(UtilidadesREINT.traducirIdMunicipio(municipioId));
            parametrosAlta.setZorDirDireccionCompleta(zorDirDireccionCompleta);
            parametrosAlta.setZorDirCp(zorDirCp);
            // deuda
            if (!zorLiNumLiquidacion.isEmpty()) {
                parametrosAlta.setZorLiNumLiquidacion(new BigDecimal(zorLiNumLiquidacion));
            }
            parametrosAlta.setZorLiImporteDeuda(zorLiImporteDeuda);

            //Fechas
            //convertir el Calendar a formato ZORKU
            log.info("---- llamaAlServicioWebaltaCartaPagoNoRGI - Llenado objetos del cliente  - FECHAS");

                // 1 - Pago Expediente Subvencion
                if (gerDeFechaPagoExpedienteSubvencion != null) {
                zorLiFechaPagoExpSubv = UtilidadesREINT.convierteFechaZorku(gerDeFechaPagoExpedienteSubvencion);
                log.debug("fecha Formateada ZK: " + zorLiFechaPagoExpSubv);
                }

                // 2 - fecha Limite De Pago
            if (valorFechaLimitePago != null) {
                zorLiFechaLimitePago = UtilidadesREINT.convierteFechaZorku(valorFechaLimitePago);
                log.debug("Fecha Limite Pago: " + zorLiFechaLimitePago);
                }

                // 3 - fecha Requerimiento
                if (gerDeFechaRequerimiento != null) {
                zorLiFechaRequerimiento = UtilidadesREINT.convierteFechaZorku(gerDeFechaRequerimiento);
                log.debug("Fecha Requerimiento: " + zorLiFechaRequerimiento);
                }

                // 4 - fecha Resolucion
                if (gerDeFechaResolucion != null) {
                zorLiFechaResolucion = UtilidadesREINT.convierteFechaZorku(gerDeFechaResolucion);
                log.debug("Fecha Resolucion: " + zorLiFechaResolucion);
                }

                // 5. gerDeFechaPago para incluir la fecha de pago en periodo de alegaciones si se invoca desde resolucion
                if (codTramiteResolucion.equalsIgnoreCase(cTramite)) {
                log.debug("Vamos a convertir la fecha de pago periodo alegaciones a formato ZORKU");
                    // 5 - fecha pago deuda en periodo de alegaciones
                    if (fecPagoDeudaIniPeriodoAlegaciones != null) {
                    zorFechaPagoDeudaIni = UtilidadesREINT.convierteFechaZorku(fecPagoDeudaIniPeriodoAlegaciones);
                    log.debug("Fecha pago Deuda periodo Alegaciones : " + zorFechaPagoDeudaIni);
                    parametrosAlta.setZorLiFechaPago(zorFechaPagoDeudaIni);
                    }

                if (sinInteres.equals("1")) {
                    parametrosAlta.setSinIntereses(new BigDecimal(1));
                }
            }
            parametrosAlta.setZorLiFechaResolucion(zorLiFechaResolucion);
            parametrosAlta.setZorLiFechaRequerimiento(zorLiFechaRequerimiento);
            parametrosAlta.setZorLiFechaPagoExpSubv(zorLiFechaPagoExpSubv);
            parametrosAlta.setZorLiFechaLimitePago(zorLiFechaLimitePago);

            parametrosAlta.setZorEdCodigo(zorEstado);
            parametrosAlta.setZorTpCodigo(zorTipoPago);

            log.info("Tramite: " + cTramite + " - Ocu: " + ocuTramite);

            try {
                log.info("  --  altaDeudaSubvenciones  --   Cliente ");
                HttpClient client = HttpClients.createDefault();
                String jsonBody = new Gson().toJson(parametrosAlta);
                StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                log.info("======== PARAMETROS altaDeudaREINT  -- Parámetros llamada a ZORKU: " + jsonBody);
// #377721 llamada al SW actualizarDomicilioNoRGI para los trámites resolución y suspensión
            if (codTramiteResolucion.equals(cTramite) || codTramiteSuspension.equals(cTramite)) {
                    log.info("  --  altaDeudaREINT  --  Llamando al SW actualizarDomicilioNoRGI ... ");
                    HttpPost llamadaActualizarDomicilio = new HttpPost(urlActualizarDom);
                    llamadaActualizarDomicilio.setHeader("Accept", "application/json");
                    llamadaActualizarDomicilio.setEntity(entidadLlamada);
                    try {
                        HttpResponse respuestaActualizarDom = client.execute(llamadaActualizarDomicilio);
// recojo respuesta
                        log.info("  --  altaDeudaREINT  --  actualizarDomicilioNoRGI  --  getStatusLine() : " + respuestaActualizarDom.getStatusLine());
                        int statusCodeActualizar = respuestaActualizarDom.getStatusLine().getStatusCode();
                        log.info("  --  altaDeudaREINT  --  actualizarDomicilioNoRGI  --  getStatusLine().getStatusCode() : " + statusCodeActualizar);
                        log.info("  --  altaDeudaREINT  --  actualizarDomicilioNoRGI  --  Hay respuesta de ZORKU");
                        HttpEntity entidadRespuestaActualizar = respuestaActualizarDom.getEntity();
                        // convierto la respuesta a STRING
                        String respuestaActualizarString = EntityUtils.toString(entidadRespuestaActualizar);
                        log.debug("  --  altaDeudaREINT  --  actualizarDomicilioNoRGI  --  Respuesta String: " + respuestaActualizarString);
                            // convierto a JSON
                            JSONObject respuestaActualizarJson;
                            switch (statusCodeActualizar) {
                                case HttpStatus.SC_OK:
                                    respuestaActualizarJson = new JSONObject(respuestaActualizarString);
                                    boolean valido = respuestaActualizarJson.getBoolean("valido");
                                    if (valido) {
                                    log.debug("   --  altaDeudaREINT --  actualizarDomicilioNoRGI  -- VALIDO");
                        }
                                log.info("  --  altaDeudaREINT  --  resolucionVOActualizarDomicilio. Identificador de la deuda actualizada : " + respuestaActualizarJson.getString("numLiquidacion"));
                                    break;
                                case HttpStatus.SC_BAD_REQUEST:
                                    respuestaActualizarJson = new JSONObject(respuestaActualizarString);
                                    String codError = respuestaActualizarJson.getString("codigoError");
                                    String msgErrorCas = respuestaActualizarJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                    String msgErrorEus = respuestaActualizarJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                    log.error("  --  actualizarDomicilioNoRGI  -- Error  al llamar a la ZORKU  - " + codError);
                                    log.error("  --  actualizarDomicilioNoRGI  -- " + msgErrorCas);
                                    log.error("  --  actualizarDomicilioNoRGI  -- " + msgErrorEus);
                                    xmlSalida = obtenerXmlSalida(msgErrorCas, "");
                        retornarXML(xmlSalida, response);
                                    return ERROR_LLAMADA_SW;
                            case HttpStatus.SC_FORBIDDEN:
                                log.error("  --  actualizarDomicilioNoRGI  -- Error  " + statusCodeActualizar + " - " + respuestaActualizarDom.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                xmlSalida = obtenerXmlSalida(ERROR_403, "");
                                retornarXML(xmlSalida, response);
                                return ERROR_403;
                                case HttpStatus.SC_NOT_FOUND:
                                    log.error("  --  actualizarDomicilioNoRGI  -- Error 404 al llamar a la ZORKU " + respuestaActualizarDom.getStatusLine().getReasonPhrase());
                                    xmlSalida = obtenerXmlSalida(ERROR_404, "");
                                    retornarXML(xmlSalida, response);
                                    return ERROR_404;
                            case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                log.error("  --  actualizarDomicilioNoRGI  -- Error  " + statusCodeActualizar + " - " + respuestaActualizarDom.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                xmlSalida = obtenerXmlSalida(ERROR_500, "");
                                retornarXML(xmlSalida, response);
                                return ERROR_500;
                            case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                log.error("  --  actualizarDomicilioNoRGI  -- Error  " + statusCodeActualizar + " - " + respuestaActualizarDom.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                xmlSalida = obtenerXmlSalida(ERROR_503, "");
                                retornarXML(xmlSalida, response);
                                return ERROR_503;
                                default:
                                    log.error("  --  actualizarDomicilioNoRGI  -- Error " + statusCodeActualizar + " al llamar a la ZORKU " + respuestaActualizarDom.getStatusLine().getReasonPhrase());
                                    xmlSalida = obtenerXmlSalida(respuestaActualizarDom.getStatusLine().getReasonPhrase(), "");
                    retornarXML(xmlSalida, response);
                    return ERROR_LLAMADA_SW;
            }
                    } catch (IOException e) {
                        log.error("  --  actualizarDomicilioNoRGI  -- IOException  al llamar a la ZORKU " + e.getMessage());
                xmlSalida = obtenerXmlSalida(ERROR_LLAMADA_SW, "");
                retornarXML(xmlSalida, response);
                return ERROR_LLAMADA_SW;
            }
                }

                HttpPost llamadaAltaDeuda = new HttpPost(urlAltaDeuda);
                llamadaAltaDeuda.setHeader("Accept", "application/json");
                llamadaAltaDeuda.setEntity(entidadLlamada);
                log.info("Llamando al servicio " + urlAltaDeuda + " ... " + jsonBody);
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
                    log.debug("  --  altaDeudaREINT  --  Respuesta String: " + respuestaAltaString);
                        // convierto a JSON
                        JSONObject respuestaAltaJson;

                        switch (statusCodeAlta) {
                            case HttpStatus.SC_OK:
                                respuestaAltaJson = new JSONObject(respuestaAltaString);
                                boolean valido = respuestaAltaJson.getBoolean("valido");
                                log.info("  --  altaDeudaSubvenciones  -- VALIDO: " + valido);
                                idDeuda = respuestaAltaJson.getString("numLiquidacion");
                                nombreCarta = "LIQ_" + idDeuda + ".pdf";
                                String oidCarta = respuestaAltaJson.getString("oidCartaPago");
                                log.info("Deuda: " + idDeuda + " generada correctamente. OID: " + oidCarta);

            String codIdDeuda = "";
            String codCampoCartaPago = "";
            String codCampoFechaLimite = "";
                                log.info("altaDeudaSubvenciones.Grabar suplementarios - Trámite " + cTramite);
            if (codTramiteRequerimiento.equals(cTramite)) {// grabar en suplementarios los campos devueltos por el SW en IDDEUDAINI, CARTAPAGO, FECHA VENCIMIENTO                                
                codIdDeuda = codCampoIdDeudaIni;
                codCampoCartaPago = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CARTAPAGO_REQ, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                codCampoFechaLimite = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_VENCIMIENTO_CARTA_REQ, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                log.info("codCampoVenCarta REQUERIMIENTO: " + codCampoFechaLimite);
            } else if (codTramiteResolucion.equals(cTramite)) {// grabar en suplementarios los campos devueltos por el SW en IDDEUDA, CARTAPAGORES, FECHA VENCIMIENTO  
                                codIdDeuda = codCampoIdDeudaRes;
                codCampoCartaPago = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CARTAPAGO_RES, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                codCampoFechaLimite = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                log.info("codCampoVenCarta RESOLUCION: " + codCampoFechaLimite);

                                    // si no se ha pagado la deuda de alegaciones hay que anularla
                                    if (zorFechaPagoDeudaIni == null) {
                                        String motivo = "05";
                                        String observaciones = "Anulada desde Regexlan tras generar la Liquidación en Resolución";
                                        String urlAnularDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_ANULACION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
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
                                        log.debug(" altaDeudaREINT --  anularDeudaSubvenciones  --  Respuesta String: " + respuestaString);
                                                // convierto a JSON
                                                JSONObject respuestaJson;
                                                switch (statusCode) {
                                                    case HttpStatus.SC_OK:
                                                        respuestaJson = new JSONObject(respuestaString);
                                                        log.info("anularDeudaSubvenciones - Hay respuesta SIN ERROR del SW");
                                                        codOperacion = OPERACION_CORRECTA;
                                                        try {
                                                            String fechaOper = null;
                                                            int usuarioExpediente = MeLanbide07Manager.getInstance().getUsuarioExpediente(codOrganizacion, numExpediente, codProcedimiento, Integer.parseInt(codTramiteResolucion), ocurrenciaTramiteResolucion, adaptador);
                                                            String nombreUsuario = MeLanbide07Manager.getInstance().getNombreUsuario(usuarioExpediente, adaptador);
                                                            OperacionExpedienteVO operacion = new OperacionExpedienteVO();
                                                            //lleno el objeto
                                                            log.debug("Lleno el Objeto operacionVO");
                                                            operacion.setCodMunicipio(codOrganizacion);
                                                            operacion.setEjercicio(Integer.parseInt(ejercicio));
                                                            operacion.setNumExpediente(numExpediente);
                                                            operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ANULAR_DEUDA);
//                                                            operacion.setTipoOperacion(32);// codificar
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
                                                                    + "<div class=\"movExpVal\">").append(numExpediente).append("</div>\n"
                                                                    + "</div>");
                                                            textoXml.append("<div class=\"movExpLin\">\n"
                                                                    + "<div class=\"movExpEtiq\">{eMovExpCodProc}:</div>\n"
                                                                    + "<div class=\"movExpVal\">").append(codProcedimiento).append("</div>\n"
                                                                    + "</div>");
                                                            textoXml.append("<div class=\"movExpLin\">\n"
                                                                    + "<div class=\"movExpEtiq\">{eMovExpExpIni}:</div>\n"
                                                                    + "<div class=\"movExpVal\">").append(zorDeExpInicial).append("</div>\n"
                                                                    + "</div>");

                                                            textoXml.append("<div class=\"movExpC2\">DEUDA ANULADA:</div>\n");
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
                                                            log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- ha ocurrido un error al grabar la operacion anularDeuda del expediente " + numExpediente + " - ", e);
                                                        }
                                                        break;

                                                    case HttpStatus.SC_BAD_REQUEST:
                                                        respuestaJson = new JSONObject(respuestaString);
                                                        String codError = respuestaJson.getString("codigoError");
                                                        String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                                        String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                                log.error(" altaDeudaREINT --  anularDeudaSubvenciones  -- Error  al llamar a la ZORKU  - " + codError);
                                                log.error(" altaDeudaREINT --  anularDeudaSubvenciones  -- " + msgErrorCas);
                                                log.error(" altaDeudaREINT --  anularDeudaSubvenciones  -- " + msgErrorEus);
                                                codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                                        break;
                                            case HttpStatus.SC_FORBIDDEN:
                                                log.error("  --  altaDeudaSubvenciones  -- Error en anularDeudaSubvenciones " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                                xmlSalida = obtenerXmlSalida(ERROR_403, "");
                                                retornarXML(xmlSalida, response);
                                                return ERROR_403;
                                                    case HttpStatus.SC_NOT_FOUND:
                                                        log.error("  --  altaDeudaSubvenciones  -- Error 404 en anularDeudaSubvenciones al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                                        xmlSalida = obtenerXmlSalida(ERROR_404, "");
                                                        retornarXML(xmlSalida, response);
                                                        return ERROR_404;
                                            case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                                log.error("  --  altaDeudaSubvenciones  -- Error en anularDeudaSubvenciones " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                                xmlSalida = obtenerXmlSalida(ERROR_500, "");
                                                retornarXML(xmlSalida, response);
                                                return ERROR_500;
                                            case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                                log.error("  --  altaDeudaSubvenciones  -- Error en anularDeudaSubvenciones " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                                xmlSalida = obtenerXmlSalida(ERROR_503, "");
                                                retornarXML(xmlSalida, response);
                                                return ERROR_503;
                                                    default:
                                                        log.error("  --  altaDeudaSubvenciones  -- Error en anularDeudaSubvenciones " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                                        xmlSalida = obtenerXmlSalida(statusCode + " - " + respuesta.getStatusLine().getReasonPhrase(), "");
                                                        retornarXML(xmlSalida, response);
                                                        return ERROR_LLAMADA_SW;
                                                }
                                        } catch (IOException e) {
                                            log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- IOException  " + e.getMessage());
                                            xmlSalida = obtenerXmlSalida(ERROR_LLAMADA_SW, "");
                                            retornarXML(xmlSalida, response);
                                            return ERROR_LLAMADA_SW;
                                        } catch (UnsupportedCharsetException e) {
                                            log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- UnsupportedCharsetException  " + e.getMessage());
                                            xmlSalida = obtenerXmlSalida(ERROR_LLAMADA_SW, "");
                                            retornarXML(xmlSalida, response);
                                            return ERROR_LLAMADA_SW;
                                        } catch (ParseException e) {
                                            log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- ParseException  " + e.getMessage());
                                            xmlSalida = obtenerXmlSalida(ERROR_LLAMADA_SW, "");
                                            retornarXML(xmlSalida, response);
                                            return ERROR_LLAMADA_SW;
                                        } catch (JSONException e) {
                                            log.error(" altaDeudaSubvenciones --  anularDeudaSubvenciones  -- JSONException  " + e.getMessage());
                                            xmlSalida = obtenerXmlSalida(ERROR_LLAMADA_SW, "");
                                            retornarXML(xmlSalida, response);
                                            return ERROR_LLAMADA_SW;
                                        }
                                    }

            } else if (codTramiteSuspension.equals(cTramite)) {// grabar en suplementarios los campos devueltos por el SW en IDDEUDASUS, CARTAPAGOSUS, FECHA VENCIMIENTO 
                codIdDeuda = codCampoIdDeudaSusp;
                codCampoCartaPago = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CARTAPAGO_SUSP, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                codCampoFechaLimite = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_VENCIMIENTO_CARTA_SUS, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                log.info("codCampoVenCarta SUSPENSION: " + codCampoFechaLimite);
            }

            try {
                                    log.info("altaDeudaSubvenciones.Grabar  - " + codIdDeuda + " - " + codCampoCartaPago + " - " + codCampoFechaLimite);

                CampoSuplementarioModuloIntegracionVO campoSuplementarioFechLim = new CampoSuplementarioModuloIntegracionVO();
                campoSuplementarioFechLim.setCodOrganizacion(String.valueOf(codOrganizacion));
                campoSuplementarioFechLim.setCodProcedimiento(codProcedimiento);
                campoSuplementarioFechLim.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                campoSuplementarioFechLim.setTramite(false);
                campoSuplementarioFechLim.setNumExpediente(numExpediente);
                campoSuplementarioFechLim.setEjercicio(ejercicio);
                campoSuplementarioFechLim.setCodigoCampo(codCampoFechaLimite);
                                    campoSuplementarioFechLim.setValorFecha(valorFechaLimitePago);
                                    gestorCampoSup.grabarCampoSuplementario(campoSuplementarioFechLim);
                                    log.info("Se graba el campo " + codCampoFechaLimite + " = " + valorFechaLimitePago.toString());

                CampoSuplementarioModuloIntegracionVO campoSuplementarioIdDeuda = new CampoSuplementarioModuloIntegracionVO();
                campoSuplementarioIdDeuda.setCodOrganizacion(String.valueOf(codOrganizacion));
                campoSuplementarioIdDeuda.setCodProcedimiento(codProcedimiento);
                campoSuplementarioIdDeuda.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                campoSuplementarioIdDeuda.setTramite(false);
                campoSuplementarioIdDeuda.setNumExpediente(numExpediente);
                campoSuplementarioIdDeuda.setEjercicio(ejercicio);
                campoSuplementarioIdDeuda.setCodigoCampo(codIdDeuda);
                campoSuplementarioIdDeuda.setValorTexto(idDeuda);
                                    gestorCampoSup.grabarCampoSuplementario(campoSuplementarioIdDeuda);
                log.info(codIdDeuda + ": " + idDeuda);

                // SI EXISTE LA CARTA SE BORRA
                                    if (MeLanbide07Manager.getInstance().existeDocumentoTFI(numExpediente, codCampoCartaPago, adaptador)) {
                                        int cartaBorrada = MeLanbide07Manager.getInstance().borrarFicheroExpediente(numExpediente, codCampoCartaPago, adaptador);
                                        int relBorrada = MeLanbide07Manager.getInstance().borrarRelacionDokusiCSE(codOrganizacion, numExpediente, codCampoCartaPago, adaptador);

                    if (cartaBorrada <= 0) {
                        log.error("Ha ocurrido un error al borrar la carta de pago");
                        xmlSalida = obtenerXmlSalida(ERROR_BORRANDO_CARTA, "");
                        retornarXML(xmlSalida, response);
                                            return ERROR_BORRANDO_CARTA;
                    }
                }

                                    if (MeLanbide07Manager.getInstance().grabarCartaDokusi(codOrganizacion, numExpediente, codCampoCartaPago, nombreCarta, oidCarta, adaptador)) {
                                        log.info("Carta Grabada");
                                    } else {
                                        log.error("altaDeudaSubvenciones.Devolvemos: " + ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS);
                                        codOperacion = ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS;
                                        xmlSalida = obtenerXmlSalida(ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS, "");
                                        retornarXML(xmlSalida, response);
                                        return ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS;
                                    }

                if (codTramiteResolucion.equals(cTramite)) {
                                        log.info("altaDeudaSubvenciones --  Grabar  - " + codCampoIntereses);
                                        intereses = new BigDecimal(respuestaAltaJson.getDouble("importeIntereses"));
                                        intereses = intereses.setScale(2, RoundingMode.HALF_UP);
                                        int grabaOK = MeLanbide07Manager.getInstance().guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramiteResolucion), ocurrenciaTramiteResolucion, codCampoIntereses, intereses, adaptador);
                    if (grabaOK <= 0) {
                        log.error("Excepción al grabar el campo suplementario " + codCampoIntereses);
                    } else {
                        log.info("Grabado el valor " + intereses + " en " + codCampoIntereses);
                    }
                }
            } catch (Exception e) {
                                    log.error("altaDeudaSubvenciones.Devolvemos: " + ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS);
                codOperacion = ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS;
                xmlSalida = obtenerXmlSalida(ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS, "");
                retornarXML(xmlSalida, response);
                return ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS;
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
                                codOperacion = msgErrorCas;
                                log.info("altaDeudaSubvenciones.Devolvemos: " + codOperacion);
                                xmlSalida = obtenerXmlSalida(codOperacion, "");
                                retornarXML(xmlSalida, response);
                                return codOperacion;
                        case HttpStatus.SC_FORBIDDEN:
                            log.error("  --  altaDeudaSubvenciones  -- Error " + statusCodeAlta + " al llamar a la ZORKU " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                            xmlSalida = obtenerXmlSalida(ERROR_403, "");
                            retornarXML(xmlSalida, response);
                            return ERROR_403;
                            case HttpStatus.SC_NOT_FOUND:
                                log.error("  --  altaDeudaSubvenciones  -- Error 404 al llamar a la ZORKU " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                                xmlSalida = obtenerXmlSalida(ERROR_404, "");
                                retornarXML(xmlSalida, response);
                                return ERROR_404;
                        case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                            log.error("  --  altaDeudaSubvenciones  -- Error " + statusCodeAlta + " al llamar a la ZORKU " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                            xmlSalida = obtenerXmlSalida(ERROR_500, "");
                            retornarXML(xmlSalida, response);
                            return ERROR_500;
                        case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                            log.error("  --  altaDeudaSubvenciones  -- Error " + statusCodeAlta + " al llamar a la ZORKU " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                            xmlSalida = obtenerXmlSalida(ERROR_503, "");
                            retornarXML(xmlSalida, response);
                            return ERROR_503;
                            default:
                                log.error("  --  altaDeudaSubvenciones  -- Error " + statusCodeAlta + " al llamar a la ZORKU ");
                                xmlSalida = obtenerXmlSalida(statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase(), "");
                                retornarXML(xmlSalida, response);
                                return ERROR_LLAMADA_SW;
                        }
                } catch (IOException e) {
                    log.error("  --  altaDeudaSubvenciones  -- IOException  al llamar a la ZORKU " + e.getMessage());
                    xmlSalida = obtenerXmlSalida(ERROR_LLAMADA_SW, "");
                    retornarXML(xmlSalida, response);
                    return ERROR_LLAMADA_SW;
                }

            } catch (UnsupportedCharsetException e) {
                log.error("  --  altaDeudaSubvenciones  -- UnsupportedCharsetException  al llamar a la ZORKU " + e.getMessage());
                xmlSalida = obtenerXmlSalida(ERROR_LLAMADA_SW, "");
                retornarXML(xmlSalida, response);
                return ERROR_LLAMADA_SW;
            }

            //____________________________________________________________________________BLOQUE altaCartaDeudaNoRGI__________________________________________________________________________________________________________
        } catch (Exception e) {  //Resultado con error
            log.error("error en altaDeudaSubvenciones ", e);
            codOperacion = ERROR_GENERICO;
            xmlSalida = obtenerXmlSalida(ERROR_GENERICO, "");
            retornarXML(xmlSalida, response);
            return ERROR_GENERICO;
        } finally {
            if (adaptador != null) {
                adaptador.devolverConexion(con);
            }
        }
        //Si todo va bien, todo correcto, devolvemos 0
        log.info("altaDeudaSubvenciones.Devolvemos: 0");
        xmlSalida = obtenerXmlSalida("0", idDeuda);
        retornarXML(xmlSalida, response);
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
    public String anularDeudaREINT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info("dENTRO DE anularDeudaREINT(): ");
        log.info("parametros  de : anularDeudaSubvenciones: codOrg " + codOrganizacion + " - Tramite: " + codTramite + " - Ocurrencia: " + ocurrenciaTramite + " " + numExpediente);
        String codOperacion = OPERACION_CORRECTA;
        AdaptadorSQLBD adaptador = null;
        Connection con = null;
        String iddeuda = "";
        String motdeuda = "";
        String observaciones = null;
        String ejercicio = "";
        String codProcedimiento = "";
        String codCampoCartaPago = null;
        String codDeudaABorrar = null;
        String codFechaABorrar = null;
        String codCheckABorrar = null;
        String tipoDeuda = null;
        boolean esPrimera = false;
        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }//if(numExpediente!=null && !"".equals(numExpediente))

        String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoIdDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoIdDeudaIni = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA_INI, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoObsAnulDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_OBS_ANUL_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoFecVenRes = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoFecVenReq = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_VENCIMIENTO_CARTA_REQ, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoDeudaAnuladaIni = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_DEUDA_ANULADA_REQ, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoDeudaAnuladaRes = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_DEUDA_ANULADA_RES, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoIntereses = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_INTERES_DEMORA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        SalidaIntegracionVO campoIdDeuda = null;
        SalidaIntegracionVO campoObsDeuda = null;

        try {
            String urlAnularDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_ANULACION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            log.debug("URL: " + urlAnularDeuda);

            //Creo el VO de seguridad con un usuario SIPCA
            ParamsSeguridadZorku seguridadVO = UtilidadesREINT.getSeguridad(codOrganizacion);

            log.info("llamada al sw");
            ParamsAnularDeuda parametrosAnulacion = new ParamsAnularDeuda();
            parametrosAnulacion.setZorUsUsuario(seguridadVO.getUsuario());
            parametrosAnulacion.setZorUsPassword(seguridadVO.getContrasena());
            parametrosAnulacion.setArea(seguridadVO.getArea());
            //Hacemos la llamada al ws de anularDeudaREINT

            //Recuperamos los datos del campo desplegable que nos interesa
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adaptador.getConnection();
            MeLanbide07Manager m07Manager = MeLanbide07Manager.getInstance();
            try {
                motdeuda = m07Manager.getCampoMotivoAnulacionDesplegable(codOrganizacion, numExpediente, ocurrenciaTramite, adaptador);
            } catch (Exception e) {
                log.error("anularDeudaSubvenciones. Devolvemos: " + ERROR_RECUPERANDO_MOT_DEUDA, e);
                return ERROR_RECUPERANDO_MOT_DEUDA;
            }

            campoIdDeuda = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoIdDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            if ((campoIdDeuda.getStatus() == 0)) {
                log.info("Existe la SEGUNDA deuda");
                iddeuda = campoIdDeuda.getCampoSuplementario().getValorTexto();
                codCampoCartaPago = "CARTAPAGORES";
                codDeudaABorrar = codCampoIdDeuda;
                codFechaABorrar = codCampoFecVenRes;
                codCheckABorrar = codCampoDeudaAnuladaRes;
                tipoDeuda = "Fase Resolución";
            } else {
                campoIdDeuda = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoIdDeudaIni, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                if ((campoIdDeuda.getStatus() == 0)) {
                    log.info("Existe la PRIMERA deuda");
                    iddeuda = campoIdDeuda.getCampoSuplementario().getValorTexto();
                    codCampoCartaPago = "CARTAPAGO";
                    codDeudaABorrar = codCampoIdDeudaIni;
                    codFechaABorrar = codCampoFecVenReq;
                    codCheckABorrar = codCampoDeudaAnuladaIni;
                    tipoDeuda = "Fase Alegaciones";
                    esPrimera = true;
                } else {
                    log.error("anularDeudaSubvenciones - NO existe la deuda");
                    return ERROR_RECUPERANDO_ID_DEUDA_INI;
                }
            }
            String codTramiteAnulacion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_ANULACION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            campoObsDeuda = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    Integer.parseInt(codTramiteAnulacion), ocurrenciaTramite, codCampoObsAnulDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            if (campoObsDeuda.getStatus() == 0) {
                observaciones = campoObsDeuda.getCampoSuplementario().getValorTexto();
            }

            log.info(">>>>Llamamos al ws <<<<<<<<");
            log.info("IdDeuda: " + iddeuda);
            log.info("Codigo Motivo Anulacion: " + motdeuda);
            log.info("Observaciones Anulación Deuda: " + observaciones);
            parametrosAnulacion.setZorLiNumLiquidacion(new BigDecimal(iddeuda));
            parametrosAnulacion.setZorMaCod(motdeuda);
            parametrosAnulacion.setZorLiMotivoAnulacionTexto(observaciones);
// control retorno     
            int cartaBorrada = -1;
            int deudaBorrada = -1;
            int fechaBorrada = -1;
            int checkBorrado = -1;
            String nombreCarta = null;
            String fechaOper = null;
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
                            nombreCarta = m07Manager.getNombreFicheroExpediente(codOrganizacion, codProcedimiento, ejercicio, numExpediente, codCampoCartaPago, adaptador);
                            if (m07Manager.existeDocumentoTFI(numExpediente, codCampoCartaPago, adaptador)) {// si  existe lo borro
                                int relBorrada = m07Manager.borrarRelacionDokusiCSE(codOrganizacion, numExpediente, codCampoCartaPago, adaptador);
                                cartaBorrada = m07Manager.borrarFicheroExpediente(numExpediente, codCampoCartaPago, adaptador);
                                if (cartaBorrada <= 0 || relBorrada <= 0) {
                                    log.error("anularDeudaSubvenciones - Ha ocurrido un error al borrar la carta de pago");
                                    return ERROR_BORRANDO_CARTA;
                                } else {
                                    log.info("Carta Borrada");
                                }
                            }
                            deudaBorrada = m07Manager.borrarTextoExpediente(numExpediente, codDeudaABorrar, iddeuda, adaptador);
                            if (deudaBorrada <= 0) {
                                log.error("anularDeudaSubvenciones - Ha ocurrido un error al borrar la deuda");
                                return ERROR_BORRANDO_DEUDA;
                            } else {
                                log.info("IdDeuda Borrada");

                                try {
                                    int usuarioExpediente = m07Manager.getUsuarioExpediente(codOrganizacion, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, adaptador);
                                    String nombreUsuario = MeLanbide07Manager.getInstance().getNombreUsuario(usuarioExpediente, adaptador);
                                    String descMotivo = m07Manager.getValorDesplegableExternoTramite(codOrganizacion, numExpediente, ejercicio, codTramite, ocurrenciaTramite, "MOTANULDEUDA", adaptador);
                                    OperacionExpedienteVO operacion = new OperacionExpedienteVO();
                                    //lleno el objeto
                                    log.debug("Lleno el Objeto operacionVO");
                                    operacion.setCodMunicipio(codOrganizacion);
                                    operacion.setEjercicio(Integer.parseInt(ejercicio));
                                    operacion.setNumExpediente(numExpediente);
                                    operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ANULAR_DEUDA);// codificar
//                                        operacion.setTipoOperacion(32);// codificar
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
                                            + "<div class=\"movExpEtiq\">{eMovExpEjercicio}:</div>\n"
                                            + "<div class=\"movExpVal\">").append(ejercicio).append("</div>\n"
                                            + "</div>");
                                    textoXml.append("<div class=\"movExpC2\">Liquidaci\u00f3n anulada</div>\n");
                                    textoXml.append("<div class=\"movExpLin\">\n"
                                            + "<div class=\"movExpEtiq\">Tipo de liquidaci\u00f3n:</div>\n"
                                            + "<div class=\"movExpVal\">").append(tipoDeuda).append("</div>\n"
                                            + "</div>");
                                    textoXml.append("<div class=\"movExpLin\">\n"
                                            + "<div class=\"movExpEtiq\">N\u00famero de Liquidaci\u00f3n:</div>\n"
                                            + "<div class=\"movExpVal\">").append(iddeuda).append("</div>\n"
                                            + "</div>");
                                    textoXml.append("<div class=\"movExpLin\">\n"
                                            + "<div class=\"movExpEtiq\">Carta de Pago eliminada:</div>\n"
                                            + "<div class=\"movExpVal\">").append(nombreCarta).append("</div>\n"
                                            + "</div>");
                                    textoXml.append("<div class=\"movExpC2\">Motivo anulaci\u00f3n</div>\n"
                                            + "<div class=\"movExpLin\">\n"
                                            + "<div class=\"movExpEtiq\">C\u00f3digo:</div>\n"
                                            + "<div class=\"movExpVal\">").append(motdeuda).append("</div>\n"
                                            + "</div>");
                                    textoXml.append("<div class=\"movExpLin\">\n"
                                            + "<div class=\"movExpEtiq\">Descripci\u00f3n:</div>\n"
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

                                    OperacionesExpedienteDAO.getInstance().insertarOperacionExpediente(operacion, con);
                                } catch (Exception e) {
                                    log.error("anularDeudaSubvenciones - Ha ocurrido un error al grabar la operacion anularDeuda del expediente " + numExpediente + " - ", e);
                                }
                                int borraInteres = m07Manager.borrarNumericoTramite(codOrganizacion, numExpediente, codTramite, ocurrenciaTramite, codCampoIntereses, adaptador);
                                if (m07Manager.existeFechaExpediente(numExpediente, "E_TFE", codFechaABorrar, adaptador)) {
                                    log.debug("Existe la fecha, BORRO");
                                    fechaBorrada = m07Manager.borrarFechaExpediente(numExpediente, codFechaABorrar, adaptador);
                                } else {
                                    log.debug("No existe la fecha, no la borro");
                                    fechaBorrada = 1;
                                }
                                if (fechaBorrada <= 0) {
                                    log.error("anularDeudaSubvenciones - Ha ocurrido un error al borrar la fecha de vencimiento de la carta de pago");
                                    return ERROR_BORRANDO_FEC_VENC;
                                } else {
                                    if (m07Manager.desplegableMarcadoConX(codOrganizacion, numExpediente, codCheckABorrar, adaptador)) {
                                        log.debug("Existe el check, BORRO");
                                        checkBorrado = m07Manager.borrarDesplegableExpediente(codOrganizacion, numExpediente, codCheckABorrar, adaptador);
                                    } else {
                                        log.debug("No existe el check, no borro");
                                        checkBorrado = 1;
                                    }
                                    if (checkBorrado <= 0) {
                                        log.error("Ha ocurrido un error al borrar el check de Deuda Anulada en GEP");
                                        return ERROR_BORRANDO_CHECK;
                                    }
                                }
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
                            String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + BARRA + "MODULO_INTEGRACION" + BARRA + "anularDeudaREINT" + BARRA + "MENSAJE_ERROR" + BARRA + codError, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                            if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                log.debug("Nombre mensaje= " + nombreMensaje);
                                codOperacion = codError;
                            } else {
                                codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                            }
                        } catch (Exception e) {
                            codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                        }
                        break;
                    case HttpStatus.SC_FORBIDDEN:
                        log.error("  --  anularDeudaSubvenciones  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        return ERROR_403;
                    case HttpStatus.SC_NOT_FOUND:
                        log.error("  --  anularDeudaSubvenciones  -- Error 404 al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        return ERROR_404;
                    case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                        log.error("  --  anularDeudaSubvenciones  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        return ERROR_500;
                    case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                        log.error("  --  anularDeudaSubvenciones  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        return ERROR_503;
                    default:
                        log.error("  --  anularDeudaSubvenciones  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        return ERROR_LLAMADA_SW;
                }
            } catch (Exception e) {
                log.error("error en anularDeudaSubvenciones ", e);
                return ERROR_GENERICO;
            }

        } catch (BDException e) {
            log.error("BDException en anularDeudaSubvenciones ", e);
            return ERROR_GENERICO;
        } catch (NumberFormatException e) {
            log.error("NumberFormatException en anularDeudaSubvenciones ", e);
            return ERROR_GENERICO;
        } catch (SQLException e) {
            log.error("SQLException en anularDeudaSubvenciones ", e);
            return ERROR_GENERICO;
        } finally {
            if (adaptador != null) {
                adaptador.devolverConexion(con);
            }
        }
        log.info("-----------Retorno del ws anularDeudaSubvenciones fin: " + codOperacion);
        return codOperacion;
    }

    /**
     * operacion de extension que llama al SW de ZORKU para suspender el periodo
     * de pago voluntario de una deuda
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws BDException
     */
    public String suspenderPeriodoPagoVoluntarioREINT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info("suspenderPeriodoPagoVoluntarioREINT ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite
                + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String mensaje = "-1";
        String ejercicio = "";
        String codProcedimiento = "";
        String idDeuda = "";
        String estadoDeuda = "";
        String fechaSuspension = "";
        AdaptadorSQLBD adaptador = null;
        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];

        }
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoIdDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFechaSuspension = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_SUSPENSION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codTramiteSuspension = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_SUSPENSION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            SalidaIntegracionVO campoIdDeuda = null;
            campoIdDeuda = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoIdDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            if ((campoIdDeuda.getStatus() == 0)) {
                idDeuda = campoIdDeuda.getCampoSuplementario().getValorTexto();

                try {
                    estadoDeuda = MeLanbide07Manager.getInstance().getEstadoDeudaZORKU(idDeuda, adaptador);
                } catch (Exception e) {
                    log.error("Error al recuperar el estado de la deuda");
                    return ERROR_RECUPERANDO_ESTADO;
                }
                if (estadoDeuda.equalsIgnoreCase("1")) {
                    String urlSuspension = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_SUSPENSION_PERIODO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                    log.debug("URL: " + urlSuspension);
                    ParamsSeguridadZorku seguridadVO = UtilidadesREINT.getSeguridad(codOrganizacion);
                    ParamsSuspenderPeriodoPago parametrosSuspension = new ParamsSuspenderPeriodoPago();
                    parametrosSuspension.setZorUsUsuario(seguridadVO.getUsuario());
                    parametrosSuspension.setZorUsPassword(seguridadVO.getContrasena());
                    parametrosSuspension.setArea(seguridadVO.getArea());

                    SalidaIntegracionVO campoFechaSuspension = null;
                    campoFechaSuspension = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            codCampoFechaSuspension, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                    // si tiene grabada la fecha de suspensión en el expediente la coje, si no se usa la fecha actual
                    Calendar fecSuspCal = null;
                    if (campoFechaSuspension.getStatus() == 0) {
                        fecSuspCal = campoFechaSuspension.getCampoSuplementario().getValorFecha();
                    } else {
                        fecSuspCal = Calendar.getInstance();
                    }
                    if (fecSuspCal != null) {
                        fechaSuspension = UtilidadesREINT.convierteFechaZorku(fecSuspCal);
                        log.debug("Fecha suspensión periodo  : " + fechaSuspension);
                    } else {
                        log.error("No se ha obtenido la fecha de suspensión");
                        return ERROR_PARSEANDO_FECHAS;
                    }

                    log.info(">>>>Llamamos al ws SuspenderPeriodoPagoVoluntario <<<<<<<<");
                    log.info("IdDeuda: " + idDeuda);
                    log.info("Fecha Suspensión: " + fechaSuspension);
                    parametrosSuspension.setZorLiNumLiquidacion(new BigDecimal(idDeuda));
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
                        log.info("  --  SuspenderPeriodoPagoVoluntario  --  getStatusLine() : " + respuesta.getStatusLine());
                        int statusCode = respuesta.getStatusLine().getStatusCode();
                        log.info("  --  SuspenderPeriodoPagoVoluntario  --  getStatusLine().getStatusCode() : " + statusCode);
                        log.info("  --  SuspenderPeriodoPagoVoluntario  --  Hay respuesta de ZORKU");
                        HttpEntity entidadRespuesta = respuesta.getEntity();
                        // convierto la respuesta a STRING
                        String respuestaString = EntityUtils.toString(entidadRespuesta);
                        log.debug("  --  SuspenderPeriodoPagoVoluntario  --  Respuesta String: " + respuestaString);
                            // convierto a JSON
                            JSONObject respuestaJson;

                            switch (statusCode) {
                                case HttpStatus.SC_OK:
                                    log.info("Hay respuesta SIN ERROR del SW");
                                    respuestaJson = new JSONObject(respuestaString);
                                    mensaje = OPERACION_CORRECTA;
                            try {
                                CampoSuplementarioModuloIntegracionVO campoFechaSusp = new CampoSuplementarioModuloIntegracionVO();
                                campoFechaSusp.setCodOrganizacion(String.valueOf(codOrganizacion));
                                campoFechaSusp.setCodProcedimiento(codProcedimiento);
                                campoFechaSusp.setEjercicio(ejercicio);
                                campoFechaSusp.setNumExpediente(numExpediente);
                                campoFechaSusp.setTramite(false);
                                campoFechaSusp.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                                campoFechaSusp.setCodigoCampo(codCampoFechaSuspension);
                                campoFechaSusp.setValorFecha(fecSuspCal);
                                        SalidaIntegracionVO salidaGrabacion = gestorCampoSup.grabarCampoSuplementario(campoFechaSusp);
                                if (salidaGrabacion != null && salidaGrabacion.getStatus() == 0) {
                                            log.info("Se graba el campo " + codCampoFechaSuspension + " = " + fecSuspCal.toString());
                                } else {
                                    log.error("Se ha producio un error grabando el valor del campo sumplementario Fecha Suspension.");
                                    mensaje = ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS;
                                }
                            } catch (Exception e) {
                                log.error("Se ha producio un error grabando el valor del campo sumplementario Fecha Suspension.");
                                return ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS;
                            }
                                    break;
                                case HttpStatus.SC_BAD_REQUEST:
                                    respuestaJson = new JSONObject(respuestaString);
                                    String codError = respuestaJson.getString("codigoError");
                                    String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                    String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                    log.error("  --  SuspenderPeriodoPagoVoluntario  -- Error  al llamar a la ZORKU  - " + codError);
                                    log.error("  --  SuspenderPeriodoPagoVoluntario  -- " + msgErrorCas);
                                    log.error("  --  SuspenderPeriodoPagoVoluntario  -- " + msgErrorEus);
                                try {
                                    // comprobamos si existe descripción para el código recibido de lo contrario se devuelve el mensaje de error en la respuesta
                                    String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + BARRA + "MODULO_INTEGRACION" + BARRA + "suspenderPeriodoPagoVoluntarioREINT" + BARRA + "MENSAJE_ERROR" + BARRA + codError, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                                    if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                        log.debug("Nombre mensaje= " + nombreMensaje);
                                        mensaje = codError;
                                    } else {
                                        mensaje = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                    }
                                } catch (Exception e) {
                                    mensaje = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                }
                                    break;
                            case HttpStatus.SC_FORBIDDEN:
                                log.error("  --  SuspenderPeriodoPagoVoluntario  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                return ERROR_403;
                                case HttpStatus.SC_NOT_FOUND:
                                    log.error("  --  SuspenderPeriodoPagoVoluntario  -- Error 404 al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                    return ERROR_404;
                            case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                log.error("  --  SuspenderPeriodoPagoVoluntario  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                return ERROR_500;
                            case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                log.error("  --  SuspenderPeriodoPagoVoluntario  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                return ERROR_503;
                                default:
                                    log.error("  --  SuspenderPeriodoPagoVoluntario  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                    return ERROR_ANULAR_SUSPENSION;
                    }

                    } catch (IOException e) {
                        //Resultado con error
                        log.error("IOException en SuspenderPeriodoPagoVoluntario ", e);
                        mensaje = ERROR_GENERICO;
                    } catch (UnsupportedCharsetException e) {
                        //Resultado con error
                        log.error("UnsupportedCharsetException en SuspenderPeriodoPagoVoluntario ", e);
                        mensaje = ERROR_GENERICO;
                    } catch (ParseException e) {
                        //Resultado con error
                        log.error("ParseException en SuspenderPeriodoPagoVoluntario ", e);
                        mensaje = ERROR_GENERICO;
                    } catch (JSONException e) {
                        //Resultado con error
                        log.error("JSONException en SuspenderPeriodoPagoVoluntario ", e);
                        mensaje = ERROR_GENERICO;
                    }

                } else {
                    log.error("La deuda no está Pendiente de Pago (estado 1)");
                    mensaje = ERROR_ESTADO_PENDIENTE;
                }
            } else {
                log.error("Se ha producio un error recuperando el valor del campo sumplementario IdDeuda.");
                mensaje = ERROR_RECUPERANDO_ID_DEUDA;
            }
        } catch (SQLException e) {
            log.error("Se ha producido SQLException en la llamada al servicio Web.");
            mensaje = ERROR_GENERICO;
        }
        log.info("<==== suspenderPeriodoPagoVoluntario: devolvemos " + mensaje);
        return mensaje;
    }

    /**
     * operacion de extension que llama al SW de ZORKU para finalizar la
     * suspension del periodo de pago voluntario de una deuda
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws Exception
     */
    public String finalizarSuspensionPeriodo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("finalizarSuspensionPeriodo ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_GENERICO;
        String ejercicio = "";
        String codProcedimiento = "";
        String idDeuda = "";
        String estadoDeuda = "";
        AdaptadorSQLBD adaptador = null;
        Connection con = null;
        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adaptador.getConnection();
            String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoIdDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            SalidaIntegracionVO campoIdDeuda = null;
            campoIdDeuda = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoIdDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            if ((campoIdDeuda.getStatus() == 0)) {
                idDeuda = campoIdDeuda.getCampoSuplementario().getValorTexto();
                try {
                    estadoDeuda = MeLanbide07Manager.getInstance().getEstadoDeudaZORKU(idDeuda, adaptador);
                } catch (Exception e) {
                    log.error("finalizarSuspensionPeriodo - Error al recuperar el estado de la deuda");
                    return ERROR_RECUPERANDO_ESTADO;
                }

                // llamada a GEP para cambiar el estado de 30 a 1
                if (estadoDeuda.equalsIgnoreCase("30")) {
                String urlFinalizarSusp = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_FINALIZAR_SUSP, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                log.debug("URL: " + urlFinalizarSusp);
                //Creo el VO de seguridad con un usuario SIPCA
                ParamsSeguridadZorku seguridadVO = UtilidadesREINT.getSeguridad(codOrganizacion);
                    log.info(">>>>Llamamos al ws anularSuspensionPeriodoPagoVoluntario <<<<<<<<");
                    log.info("IdDeuda: " + idDeuda);
                    ParamsFinalizarSuspensionPeriodo parametrosFinalizar = new ParamsFinalizarSuspensionPeriodo();
                    parametrosFinalizar.setZorUsUsuario(seguridadVO.getUsuario());
                    parametrosFinalizar.setZorUsPassword(seguridadVO.getContrasena());
                    parametrosFinalizar.setArea(seguridadVO.getArea());
                    parametrosFinalizar.setZorLiNumLiquidacion(new BigDecimal(idDeuda));
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
                                    String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + BARRA + "MODULO_INTEGRACION" + BARRA + "finalizarSuspensionPeriodo" + BARRA + "MENSAJE_ERROR" + BARRA + codError, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                                    if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                        log.debug("Nombre mensaje= " + nombreMensaje);
                                        codOperacion = codError;
                                    } else {
                                        codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                    }
                                } catch (Exception e) {
                                    codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                }
                                break;
                            case HttpStatus.SC_FORBIDDEN:
                                log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                return ERROR_403;
                                case HttpStatus.SC_NOT_FOUND:
                                    log.error("  --  finalizarSuspensionPeriodo  -- Error 404 al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                    return ERROR_404;
                            case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                return ERROR_500;
                            case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                return ERROR_503;
                                default:
                                    log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                    return ERROR_LLAMADA_SW;
                        }
                    } catch (IOException e) {
                        log.error("IOException en finalizarSuspensionPeriodo ", e);
                        return ERROR_GENERICO;
                    } catch (UnsupportedCharsetException e) {
                        log.error("UnsupportedCharsetException en finalizarSuspensionPeriodo ", e);
                        return ERROR_GENERICO;
                    } catch (ParseException e) {
                        log.error("ParseException en finalizarSuspensionPeriodo ", e);
                        return ERROR_GENERICO;
                    } catch (JSONException e) {
                        log.error("JSONException en finalizarSuspensionPeriodo ", e);
                        return ERROR_GENERICO;
                }

                    // llamada a ZORKU para anular la deuda
                String motivo = "05";
                String observaciones = "Anulada desde Regexlan  tras suspensión periodo pago";
                    String urlAnularDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_ANULACION, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                    log.debug("URL: " + urlAnularDeuda);
                    ParamsAnularDeuda parametrosAnulacion = new ParamsAnularDeuda();
                    parametrosAnulacion.setZorUsUsuario(seguridadVO.getUsuario());
                    parametrosAnulacion.setZorUsPassword(seguridadVO.getContrasena());
                    parametrosAnulacion.setArea(seguridadVO.getArea());
                    parametrosAnulacion.setZorLiNumLiquidacion(new BigDecimal(idDeuda));
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
                        log.info(" finalizarSuspensionPeriodo --  anularDeudaSubvenciones  --  getStatusLine() : " + respuesta.getStatusLine());
                        int statusCode = respuesta.getStatusLine().getStatusCode();
                        log.info(" finalizarSuspensionPeriodo --  anularDeudaSubvenciones  --  getStatusLine().getStatusCode() : " + statusCode);
                        log.info(" finalizarSuspensionPeriodo --  anularDeudaSubvenciones  --  Hay respuesta de ZORKU");
                        HttpEntity entidadRespuesta = respuesta.getEntity();
                        // convierto la respuesta a STRING
                        String respuestaString = EntityUtils.toString(entidadRespuesta);
                        log.debug(" finalizarSuspensionPeriodo --  anularDeudaSubvenciones  --  Respuesta String: " + respuestaString);
                            // convierto a JSON
                            JSONObject respuestaJson;
                            switch (statusCode) {
                                case HttpStatus.SC_OK:
                                    respuestaJson = new JSONObject(respuestaString);
                                    log.info("anularDeudaSubvenciones - Hay respuesta SIN ERROR del SW");
                                    codOperacion = OPERACION_CORRECTA;
                        try {
                            String fechaOper = null;
                            int usuarioExpediente = MeLanbide07Manager.getInstance().getUsuarioExpediente(codOrganizacion, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, adaptador);
                                        String nombreUsuario = MeLanbide07Manager.getInstance().getNombreUsuario(usuarioExpediente, adaptador);
                            OperacionExpedienteVO operacion = new OperacionExpedienteVO();
                            //lleno el objeto
                            log.debug("Lleno el Objeto operacionVO");
                            operacion.setCodMunicipio(codOrganizacion);
                            operacion.setEjercicio(Integer.parseInt(ejercicio));
                            operacion.setNumExpediente(numExpediente);
                                        operacion.setTipoOperacion(ConstantesDatos.TIPO_MOV_ANULAR_DEUDA);
//                                        operacion.setTipoOperacion(32);// codificar
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
                                    + "<div class=\"movExpEtiq\">{eMovExpEjercicio}:</div>\n"
                                    + "<div class=\"movExpVal\">").append(ejercicio).append("</div>\n"
                                    + "</div>");
                            textoXml.append("<div class=\"movExpLin\">\n"
                                    + "<div class=\"movExpEtiq\">{eMovExpCodTramite}:</div>\n"
                                    + "<div class=\"movExpVal\">").append(codTramite).append("</div>\n"
                                    + "</div>");
                            textoXml.append("<div class=\"movExpLin\">\n"
                                    + "<div class=\"movExpEtiq\">{eMovExpOcurrTramite}:</div>\n"
                                    + "<div class=\"movExpVal\">").append(ocurrenciaTramite).append("</div>\n"
                                    + "</div>");
                                        textoXml.append("<div class=\"movExpC2\">DEUDA ANULADA:</div>\n");
                            textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">Tipo de liquidaci\u00f3n:</div>\n"
                                    + "<div class=\"movExpVal\">Fase Resoluci\u00f3n</div>\n"
                                    + "</div>");
                            textoXml.append("<div class=\"movExpLin\">\n"
                                                + "<div class=\"movExpEtiq\">N\u00famero de Liquidaci\u00f3n:</div>\n"
                                    + "<div class=\"movExpVal\">").append(idDeuda).append("</div>\n"
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
                                    log.error(" finalizarSuspensionPeriodo --  anularDeudaREINT  --ha ocurrido un error al grabar la operacion anularDeuda del expediente " + numExpediente + " - ", e);
                        }
                                    break;

                                case HttpStatus.SC_BAD_REQUEST:
                                    respuestaJson = new JSONObject(respuestaString);
                                    String codError = respuestaJson.getString("codigoError");
                                    String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                    String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                log.error(" finalizarSuspensionPeriodo --  anularDeudaREINT  -- Error  al llamar a la ZORKU  - " + codError);
                                log.error(" finalizarSuspensionPeriodo --  anularDeudaREINT  -- " + msgErrorCas);
                                log.error(" finalizarSuspensionPeriodo --  anularDeudaREINT  -- " + msgErrorEus);
                                try {
                                    // comprobamos si existe descripción para el código recibido de lo contrario se devuelve el mensaje de error en la respuesta
                                    String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + BARRA + "MODULO_INTEGRACION" + BARRA + "finalizarSuspensionPeriodo" + BARRA + "MENSAJE_ERROR" + BARRA + codError, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                                    if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                        log.debug("Nombre mensaje= " + nombreMensaje);
                                        codOperacion = codError;
                                    } else {
                                        codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                    }
                                } catch (Exception e) {
                                    codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                }
                                break;
                            case HttpStatus.SC_FORBIDDEN:
                                log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " en anularDeudaSubvenciones al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                codOperacion = ERROR_403;
                                    break;
                                case HttpStatus.SC_NOT_FOUND:
                                log.error("  --  finalizarSuspensionPeriodo  -- Error 404 en anularDeudaSubvenciones al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                codOperacion = ERROR_404;
                                break;
                            case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                                log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " en anularDeudaSubvenciones al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                codOperacion = ERROR_500;
                                break;
                            case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                                log.error("  --  finalizarSuspensionPeriodo  -- Error " + statusCode + " en anularDeudaSubvenciones al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                codOperacion = ERROR_503;
                                break;
                                default:
                                log.error("  --  finalizarSuspensionPeriodo  -- Error en anularDeudaSubvenciones " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                                codOperacion = ERROR_LLAMADA_SW;
                                break;
                    }
                    } catch (IOException e) {
                        log.error("finalizarSuspensionPeriodo - Se ha producido IOException en la llamada al servicio Web. " + e.getMessage());
                        codOperacion = ERROR_GENERICO;
                    } catch (UnsupportedCharsetException e) {
                        log.error("finalizarSuspensionPeriodo - Se ha producido UnsupportedCharsetExceptionr en la llamada al servicio Web. " + e.getMessage());
                        codOperacion = ERROR_GENERICO;
                    } catch (ParseException e) {
                        log.error("finalizarSuspensionPeriodo - Se ha producido ParseException en la llamada al servicio Web. " + e.getMessage());
                        codOperacion = ERROR_GENERICO;
                    } catch (JSONException e) {
                        log.error("finalizarSuspensionPeriodo - Se ha producido JSONException en la llamada al servicio Web. " + e.getMessage());
                        codOperacion = ERROR_GENERICO;
                    }
                } else {
                    log.error("finalizarSuspensionPeriodo - La deuda no está en Suspensión PV (estado 30)");
                    codOperacion = ERROR_ESTADO_NO_SUSPENSION;
                }
            } else {
                log.error("finalizarSuspensionPeriodo - Se ha producio un error recuperando el valor del campo sumplementario IdDeuda.");
                codOperacion = ERROR_RECUPERANDO_ID_DEUDA;
            }
        } catch (BDException ex) {
            log.error("finalizarSuspensionPeriodo - Se ha producido un error en la llamada al servicio Web. " + ex.getMessage());
            codOperacion = ERROR_GENERICO;
        } catch (SQLException ex) {
            log.error("finalizarSuspensionPeriodo - Se ha producido un error en la llamada al servicio Web. " + ex.getMessage());
            codOperacion = ERROR_GENERICO;
        } finally {
            if (adaptador != null) {
                adaptador.devolverConexion(con);
            }
        }
        log.info("<==== finalizarSuspensionPeriodo:devolvemos " + codOperacion);
        return codOperacion;
    }

    /**
     * operacion de extension que llama al metodo
     * altaFraccionamientoSubvenciones() de ZORKU
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws BDException
     */
    public String altaFraccionamientoREINT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info("altaFraccionamientoREINT ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_GENERICO;
        String ejercicio = "";
        String codProcedimiento = "";
        String idDeuda = "";
        AdaptadorSQLBD adaptador = null;

        try {
            if (numExpediente != null && !"".equals(numExpediente)) {
                String[] datos = numExpediente.split(BARRA);
                ejercicio = datos[0];
                codProcedimiento = datos[1];
    }
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String CTAIBAN = null;
            String CTAENTIDAD = null;
            String CTASUCURSAL = null;
            String CTADC = null;
            String CTANUMERO = null;
            String codCampoIdDeudaRes = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoIban = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTAIBAN, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoEntidad = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTAENTIDAD, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoSucursal = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTASUCURSAL, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoDC = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTADC, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoNumeroCuenta = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTANUMERO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFecSolicitudFracc = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FECHA_SOLICITUD_FRACC, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoFecLimitePago = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoImportePlazo = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_IMP_PLAZO_F, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoPlazoFrac = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_MESES_FRAC, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            String codCampoNoFraccionar = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_LLAMAR_FRACCIONAR, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            String codTramSolFracc = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_SOL_FRACC, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
            int ocuTramSolFracc = MeLanbide07Manager.getInstance().getMaxOcurrenciaTramitexCodigo(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramSolFracc), adaptador);

            BigDecimal plazo = new BigDecimal("0");
            BigDecimal cuotaSolicitada = new BigDecimal("0");
            // fecha solicitud fraccionamiento
            Calendar fecSoliFrac = null;
            Calendar fecLimitePago = null;

            String sinFraccionar = "";

            SalidaIntegracionVO campoFecSoliFraccionamiento = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoFecSolicitudFracc, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
            SalidaIntegracionVO campoSinFraccionar = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoNoFraccionar, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
            SalidaIntegracionVO campoFecLimitePago = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoFecLimitePago, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

            //Recuperamos el valor de la URL
            String urlFraccionamientoDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_FRACCIONAMIENTO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

            log.debug("service despues");
            //Creo el VO de seguridad con un usuario SIPCA
            ParamsSeguridadZorku seguridadVO = UtilidadesREINT.getSeguridad(codOrganizacion);

            if (campoSinFraccionar.getStatus() == 0) {
                sinFraccionar = campoSinFraccionar.getCampoSuplementario().getValorDesplegable();
            }
            log.info("sin Fraccionar = " + sinFraccionar);
            if (sinFraccionar.equalsIgnoreCase("S")) {
                log.info("Avanza SIN llamar al SW - OK");
                return OPERACION_CORRECTA;
            } else {
                if (campoFecSoliFraccionamiento.getStatus() == 0) {
                    fecSoliFrac = campoFecSoliFraccionamiento.getCampoSuplementario().getValorFecha();
                    log.info("Fecha Solicitud Fraccionamiento: " + fecSoliFrac.getTime());
                } else {
                    log.error("Falta Fecha Solicitud Fraccionamiento: ");
                    return ERROR_RECUPERANDO_FECHA_SOLI_FRAC;
                }
                if (campoFecLimitePago.getStatus() == 0) {
                    fecLimitePago = campoFecLimitePago.getCampoSuplementario().getValorFecha();
                    log.info("Fecha Límite" + fecLimitePago);
                } else {
                    log.error("Falta Fecha Límite Pago ");
                    return ERROR_RECUPERANDO_FECHA_LIMITE_PAGO;
                }

                // comprobar si la fecha introducida es anterior o igual al vencimiento de la carta de pago o a HOY
                if (fecSoliFrac.after(Calendar.getInstance())) {
                    log.error("Fecha Solicitud MAYOR que hoy - " + (Calendar.getInstance().getTime()));
                    return ERROR_FECHA_SOL_MAYOR_HOY;
                }
                if (fecSoliFrac.after(fecLimitePago)) {
                    log.error("Fecha Solicitud MAYOR que Limite Pago: ");
                    return ERROR_FECHA_SOL_MAYOR_LIMITE;
                }
                //convertir el Calendar a formato ZORKU
                String fechaSolicZorku = null;
                if (fecSoliFrac != null) {
                    fechaSolicZorku = UtilidadesREINT.convierteFechaZorku(fecSoliFrac);
                    log.debug("fecha Formateada ZK: " + fechaSolicZorku);
                }

                // cargo el objeto
                ParamsAltaFraccionamiento parametrosFracc = new ParamsAltaFraccionamiento();
                parametrosFracc.setZorUsUsuario(seguridadVO.getUsuario());
                parametrosFracc.setZorUsPassword(seguridadVO.getContrasena());
                parametrosFracc.setArea(seguridadVO.getArea());
                parametrosFracc.setZorLiFechaSolicitudFracc(fechaSolicZorku);

                try {
                    SalidaIntegracionVO campoIBAN = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoIban, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    SalidaIntegracionVO campoEntidad = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoEntidad, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    SalidaIntegracionVO campoSucursal = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoSucursal, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    SalidaIntegracionVO campoDC = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoDC, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    SalidaIntegracionVO campoCuenta = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoNumeroCuenta, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    SalidaIntegracionVO campoPlazo = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoPlazoFrac, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                    SalidaIntegracionVO campoImportePlazo = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoImportePlazo, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);

                    if (campoIBAN.getStatus() == 0) {
                        CTAIBAN = campoIBAN.getCampoSuplementario().getValorTexto().toUpperCase();
                        log.info("CTAIBAN: " + CTAIBAN);
                    } else {
                        return ERROR_RECUPERANDO_IBAN;
                    }
                    if (campoEntidad.getStatus() == 0) {
                        CTAENTIDAD = campoEntidad.getCampoSuplementario().getValorTexto();
                        log.info("CTAENTIDAD: " + CTAENTIDAD);
                    } else {
                        return ERROR_RECUPERANDO_ENTIDAD;
                    }
                    if (campoSucursal.getStatus() == 0) {
                        CTASUCURSAL = campoSucursal.getCampoSuplementario().getValorTexto();
                        log.info("CTASUCURSAL: " + CTASUCURSAL);
                    } else {
                        return ERROR_RECUPERANDO_SUCURSAL;
                    }
                    if (campoDC.getStatus() == 0) {
                        CTADC = campoDC.getCampoSuplementario().getValorTexto();
                        log.info("CTADC: " + CTADC);
                    } else {
                        return ERROR_RECUPERANDO_DC;
                    }
                    if (campoCuenta.getStatus() == 0) {
                        CTANUMERO = campoCuenta.getCampoSuplementario().getValorTexto();
                        log.info("CTANUMERO: " + CTANUMERO);
                    } else {
                        return ERROR_RECUPERANDO_NUM_CUENTA;
                    }
                    if (campoImportePlazo.getStatus() == 0) {
                        cuotaSolicitada = new BigDecimal(campoImportePlazo.getCampoSuplementario().getValorNumero());
                        parametrosFracc.setCuotaSolicitada(cuotaSolicitada);
                    }
                    if (campoPlazo.getStatus() == 0) {
                        plazo = new BigDecimal(campoPlazo.getCampoSuplementario().getValorNumero());
                        parametrosFracc.setMesesAplazamiento(plazo);
                    }
                } catch (NumberFormatException e) {
                    log.error("altaFraccionamientoREINT.Devolvemos: Errores al recuperar los datos bancarios", e);
                    return ERROR_RECUPERANDO_IBAN;
                }
                // AJUSTES  
                if (CTAIBAN.length() != 4 || CTAENTIDAD.length() != 4 || CTASUCURSAL.length() != 4 || CTADC.length() != 2 || CTANUMERO.length() != 10) {
                    log.error("El numero de cuenta no es valido - El tamaño de algún dato es incorrecto");
                    return ERROR_CUENTA_TAMANIO;
                }
                String ibanCompleto = CTAIBAN + CTAENTIDAD + CTASUCURSAL + CTADC + CTANUMERO;
                if (!UtilidadesREINT.validaIBAN(ibanCompleto)) {
                    log.error("El numero de cuenta no es valido");
                    return ERROR_CUENTA_INCORRECTA;
                }
                log.info("Paso los datos bancarios a parametrosFracc");
                log.debug("IBAN: " + CTAIBAN + " - Entidad: " + CTAENTIDAD + " - Suc: " + CTASUCURSAL + " - DC: " + CTADC + " - ");
                parametrosFracc.setZorCbtIban(CTAIBAN);
                parametrosFracc.setZorCbtEntidad(CTAENTIDAD);
                parametrosFracc.setZorCbtSucursal(CTASUCURSAL);
                parametrosFracc.setZorCbtDc(CTADC);
                parametrosFracc.setZorCbtNumCuenta(CTANUMERO);
                try {
                    SalidaIntegracionVO campoIdDeudaRes = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                            codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    if (campoIdDeudaRes.getStatus() == 0) {
                        idDeuda = campoIdDeudaRes.getCampoSuplementario().getValorTexto();
                    } else {
                        log.error("altaFraccionamientoREINT.Devolvemos: " + ERROR_RECUPERANDO_ID_DEUDA);
                        return ERROR_RECUPERANDO_ID_DEUDA;
                    }
                    log.info("idDeuda: " + idDeuda);
                    parametrosFracc.setZorLiNumLiquidacion(new BigDecimal(idDeuda));
                } catch (Exception e) {
                    log.error("altaFraccionamientoREINT.Devolvemos: " + ERROR_RECUPERANDO_ID_DEUDA, e);
                    return ERROR_RECUPERANDO_ID_DEUDA;
                }

                //Ejecucion del SW de establecimiento de fechas de pago
        try {
                    HttpClient client = HttpClients.createDefault();
                    String jsonBody = new Gson().toJson(parametrosFracc);
                    StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                    HttpPost llamadaFraccionamiento = new HttpPost(urlFraccionamientoDeuda);
                    llamadaFraccionamiento.setHeader("Accept", "application/json");
                    llamadaFraccionamiento.setEntity(entidadLlamada);
                    log.info("Llamando al servicio " + urlFraccionamientoDeuda + " ... " + jsonBody);
                    HttpResponse respuesta = client.execute(llamadaFraccionamiento);
                    // recojo respuesta
                    log.info("  --  altaFraccionamientoREINT  --  getStatusLine() : " + respuesta.getStatusLine());
                    int statusCode = respuesta.getStatusLine().getStatusCode();
                    log.info("  --  altaFraccionamientoREINT  --  getStatusLine().getStatusCode() : " + statusCode);
                    log.info("  --  altaFraccionamientoREINT  --  Hay respuesta de ZORKU");
                    HttpEntity entidadRespuesta = respuesta.getEntity();
                    // convierto la respuesta a STRING
                    String respuestaString = EntityUtils.toString(entidadRespuesta);
                    log.debug("  --  altaFraccionamientoREINT  --  Respuesta String: " + respuestaString);
                        // convierto a JSON
                        JSONObject respuestaJson;
                        switch (statusCode) {
                            case HttpStatus.SC_OK:
                                respuestaJson = new JSONObject(respuestaString);
                            boolean valido = respuestaJson.getBoolean("valido");
                            if (valido) {
                                log.debug("  --  altaFraccionamientoREINT  -- VALIDO");
                                codOperacion = OPERACION_CORRECTA;
                            }
                            log.info("  --  altaFraccionamientoREINT  -- Fraccionamiento OK");
                                break;
                            case HttpStatus.SC_BAD_REQUEST:
                                respuestaJson = new JSONObject(respuestaString);
                                String codError = respuestaJson.getString("codigoError");
                                String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                            log.error("  --  altaFraccionamientoREINT  -- Error  al llamar a la ZORKU  - " + codError);
                            log.error("  --  altaFraccionamientoREINT  -- " + msgErrorCas);
                            log.error("  --  altaFraccionamientoREINT  -- " + msgErrorEus);
                            try {
                                // comprobamos si existe descripción para el código recibido de lo contrario se devuelve el mensaje de error en la respuesta
                                String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + BARRA + "MODULO_INTEGRACION" + BARRA + "altaFraccionamientoREINT" + BARRA + "MENSAJE_ERROR" + BARRA + codError, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                                if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                    log.debug("Nombre mensaje= " + nombreMensaje);
                                    codOperacion = codError;
                                } else {
                                    codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                }
                            } catch (Exception e) {
                                codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                }
                            break;
                        case HttpStatus.SC_FORBIDDEN:
                            log.error("  --  altaFraccionamientoREINT  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                            codOperacion = ERROR_403;
                            break;
                            case HttpStatus.SC_NOT_FOUND:
                            log.error("  --  altaFraccionamientoREINT  -- Error 404 al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                            codOperacion = ERROR_404;
                            break;
                        case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                            log.error("  --  altaFraccionamientoREINT  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                            codOperacion = ERROR_500;
                            break;
                        case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                            log.error("  --  altaFraccionamientoREINT  -- Error " + statusCode + " al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                            codOperacion = ERROR_503;
                            break;
                            default:
                            log.error("  --  altaFraccionamientoREINT  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a la ZORKU ");
                            codOperacion = ERROR_LLAMADA_SW;
                            break;
                    }

                } catch (IOException e) {
                    log.error("  --  altaFraccionamientoREINT  -- IOException  al llamar a la ZORKU " + e.getMessage());
                    codOperacion = ERROR_LLAMADA_SW;
                } catch (UnsupportedCharsetException e) {
                    log.error("  --  altaFraccionamientoREINT  -- UnsupportedCharsetException  al llamar a la ZORKU " + e.getMessage());
                    codOperacion = ERROR_LLAMADA_SW;
                } catch (ParseException e) {
                    log.error("  --  altaFraccionamientoREINT  -- ParseException  al llamar a la ZORKU " + e.getMessage());
                    codOperacion = ERROR_LLAMADA_SW;
                } catch (JSONException e) {
                    log.error("  --  altaFraccionamientoREINT  -- JSONException  al llamar a la ZORKU " + e.getMessage());
                    codOperacion = ERROR_LLAMADA_SW;
                }
                log.info("altaFraccionamientoREINT - FIN");
            }
        } catch (Exception e) {
            log.error("error en altaFraccionamientoREINT ", e);
            codOperacion = ERROR_GENERICO;
        }
        log.info("<==== altaFraccionamientoREINT:devolvemos " + codOperacion);
        return codOperacion;
    }

    /**
     * operacion de extension que llama al metodo cambioCuentaDomiciliacion() de
     * ZORKU
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     * @throws Exception
     */
    public String cambioCuentaREINT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("cambioCuentaREINT ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = ERROR_GENERICO;
        String ejercicio = "";
        String codProcedimiento = "";
        String idDeuda = "";
        AdaptadorSQLBD adapt = null;
        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }

        String numDocumento = null;
        String CTAIBAN = null;
        String CTAENTIDAD = null;
        String CTASUCURSAL = null;
        String CTADC = null;
        String CTANUMERO = null;
        String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoIdDeudaRes = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoIban = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTAIBAN, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoEntidad = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTAENTIDAD, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoSucursal = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTASUCURSAL, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoDC = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTADC, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoNumeroCuenta = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTANUMERO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codTramCambioCuenta = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_CAMBIO_CC, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String urlCambioCuenta = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_CAMBIO_CUENTA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            int ocuTramCambioCuenta = MeLanbide07Manager.getInstance().getMaxOcurrenciaTramitexCodigo(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramCambioCuenta), adapt);
            //Creo el VO de seguridad con un usuario SIPCA
            ParamsSeguridadZorku seguridadVO = UtilidadesREINT.getSeguridad(codOrganizacion);
            ParamsCambioCuenta parametrosCambioCuenta = new ParamsCambioCuenta();
            numDocumento = MeLanbide07Manager.getInstance().getDocumentoInteresado(codOrganizacion, numExpediente, adapt);
            SalidaIntegracionVO campoIBAN = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codTramCambioCuenta), ocuTramCambioCuenta, codCampoIban, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoEntidad = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codTramCambioCuenta), ocuTramCambioCuenta, codCampoEntidad, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoSucursal = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codTramCambioCuenta), ocuTramCambioCuenta, codCampoSucursal, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoDC = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codTramCambioCuenta), ocuTramCambioCuenta, codCampoDC, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoCuenta = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codTramCambioCuenta), ocuTramCambioCuenta, codCampoNumeroCuenta, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            SalidaIntegracionVO campoIdDeudaRes = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            // control datos
            if (numDocumento == null || numDocumento.isEmpty()) {
                log.error("  --  cambioCuentaREINT  --  No se ha recuperado el Documento de identidad del interesado");
                return ERROR_DOCUMENTO;
            }
            if (campoIBAN.getStatus() == 0) {
                CTAIBAN = campoIBAN.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaREINT  --   Falta IBAN ");
                return ERROR_RECUPERANDO_IBAN;
            }
            if (campoEntidad.getStatus() == 0) {
                CTAENTIDAD = campoEntidad.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaREINT  --  Falta ENTIDAD ");
                return ERROR_RECUPERANDO_ENTIDAD;
            }
            if (campoSucursal.getStatus() == 0) {
                CTASUCURSAL = campoSucursal.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaREINT  --  Falta SUCURSAL ");
                return ERROR_RECUPERANDO_SUCURSAL;
            }
            if (campoDC.getStatus() == 0) {
                CTADC = campoDC.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaREINT  --  Falta DC ");
                return ERROR_RECUPERANDO_DC;
            }
            if (campoCuenta.getStatus() == 0) {
                CTANUMERO = campoCuenta.getCampoSuplementario().getValorTexto();
            } else {
                log.error("  --  cambioCuentaREINT  --  Falta Nº CUENTA ");
                return ERROR_RECUPERANDO_NUM_CUENTA;
            }
            // comprobar el tamaño de los campos de la cuenta
            if (CTAIBAN.length() != 4 || CTAENTIDAD.length() != 4 || CTASUCURSAL.length() != 4 || CTADC.length() != 2 || CTANUMERO.length() != 10) {
                log.error("  --  cambioCuentaREINT  --  El numero de cuenta no es valido - El tamaño de algún dato es incorrecto");
                return ERROR_CUENTA_TAMANIO;
            }
            // validar IBAN completo
            String ibanCompleto = CTAIBAN + CTAENTIDAD + CTASUCURSAL + CTADC + CTANUMERO;
            if (!UtilidadesREINT.validaIBAN(ibanCompleto)) {
                log.error("  --  cambioCuentaREINT  --  El numero de cuenta no es valido");
                return ERROR_CUENTA_INCORRECTA;
            }

            if (campoIdDeudaRes.getStatus() == 0) {
                idDeuda = campoIdDeudaRes.getCampoSuplementario().getValorTexto();
                parametrosCambioCuenta.setZorLiNumLiquidacion(new BigDecimal(idDeuda));
            }
            // lleno el objeto
            // seguridad
            parametrosCambioCuenta.setZorUsUsuario(seguridadVO.getUsuario());
            parametrosCambioCuenta.setZorUsPassword(seguridadVO.getContrasena());
            parametrosCambioCuenta.setArea(seguridadVO.getArea());
            // interesado
            parametrosCambioCuenta.setNumDocumento(numDocumento);
            // datos bancarios
            parametrosCambioCuenta.setZorCbtIban(CTAIBAN);
            parametrosCambioCuenta.setZorCbtEntidad(CTAENTIDAD);
            parametrosCambioCuenta.setZorCbtSucursal(CTASUCURSAL);
            parametrosCambioCuenta.setZorCbtDc(CTADC);
            parametrosCambioCuenta.setZorCbtNumCuenta(CTANUMERO);
            // llamada
            try {
                log.debug("  --  cambioCuentaREINT  --   Cliente ");
                HttpClient client = HttpClients.createDefault();
                log.info("  --  cambioCuentaREINT  --   request " + urlCambioCuenta);
                HttpPost llamadaCambioCuenta = new HttpPost(urlCambioCuenta);
                log.debug("  --  cambioCuentaREINT  --   entidad ");
                // Serializar el objeto a JSON
                String jsonBody = new Gson().toJson(parametrosCambioCuenta);
                // Lo pasas a la entidad para la request
                StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                llamadaCambioCuenta.setHeader("Accept", "application/json");
                llamadaCambioCuenta.setEntity(entidadLlamada);
                // llamada a la API
                log.debug("  --  cambioCuentaREINT  --  llamada a la API ... " + jsonBody);
                HttpResponse respuesta = client.execute(llamadaCambioCuenta);
                // recojo respuesta
                log.info("  --  cambioCuentaREINT  --  getStatusLine() : " + respuesta.getStatusLine());
                int statusCode = respuesta.getStatusLine().getStatusCode();
                log.info("  --  cambioCuentaREINT  --  getStatusLine().getStatusCode() : " + statusCode);
                log.info("  --  cambioCuentaREINT  --  Hay respuesta de ZORKU");
                HttpEntity entidadRespuesta = respuesta.getEntity();
                // convierto la respuesta a STRING
                String respuestaString = EntityUtils.toString(entidadRespuesta);
                log.debug("  --  cambioCuentaREINT  --  Respuesta String: " + respuestaString);
                // convierto a JSON
                JSONObject respuestaJson;
                switch (statusCode) {
                    case HttpStatus.SC_OK:
                        respuestaJson = new JSONObject(respuestaString);
                        boolean valido = respuestaJson.getBoolean("valido");
                        if (valido) {
                            log.debug("  --  cambioCuentaREINT  -- VALIDO");
                        }
                        log.info("  --  cambioCuentaREINT  --  OK");
                        codOperacion = OPERACION_CORRECTA;
                        break;
                    case HttpStatus.SC_BAD_REQUEST:
                        respuestaJson = new JSONObject(respuestaString);
                        String codError = respuestaJson.getString("codigoError");
                        String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                        String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                        log.error("  --  cambioCuentaREINT  -- Error  al llamar a la API de cambio de cuenta - " + codError);
                        log.error("  --  cambioCuentaREINT  -- " + msgErrorCas);
                        log.error("  --  cambioCuentaREINT  -- " + msgErrorEus);
                        // comprobamos si existe descripción para el código recibido de lo contrario se devuelve el mensaje de la respuesta
                        try {
                            String nombreMensaje = ConfigurationParameter.getParameter(codOrganizacion + BARRA + "MODULO_INTEGRACION" + BARRA + "cambioCuentaREINT" + BARRA + "MENSAJE_ERROR" + BARRA + codError, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
                            if ((nombreMensaje != null) && (!"".equals(nombreMensaje))) {
                                log.debug("Nombre mensaje= " + nombreMensaje);
                                codOperacion = codError;
                            } else {
                                codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                            }
                        } catch (Exception e) {
                            codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                        }
                        break;
                    case HttpStatus.SC_FORBIDDEN:
                        log.error("  --  cambioCuentaREINT  -- Error " + statusCode + " al llamar a la API de fraccionamiento. " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_403;
                        break;
                    case HttpStatus.SC_NOT_FOUND:
                        log.error("  --  cambioCuentaREINT  -- Error 404 al llamar a la ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_404;
                        break;
                    case HttpStatus.SC_INTERNAL_SERVER_ERROR: //500
                        log.error("  --  cambioCuentaREINT  -- Error " + statusCode + " al llamar a la API de fraccionamiento. " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_500;
                        break;
                    case HttpStatus.SC_SERVICE_UNAVAILABLE: // 503
                        log.error("  --  cambioCuentaREINT  -- Error " + statusCode + " al llamar a la API de fraccionamiento. " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_503;
                        break;
                    default:
                        log.error("  --  cambioCuentaREINT  -- Error " + statusCode + " al llamar a la API de fraccionamiento. " + respuesta.getStatusLine().getReasonPhrase());
                        codOperacion = ERROR_LLAMADA_SW;
                        break;
                }
            } catch (IOException ex) {
                log.error("  --  cambioCuentaREINT  -- IOException  al llamar a la API de fraccionamiento. " + ex.getMessage());
                codOperacion = ERROR_LLAMADA_SW;
            } catch (UnsupportedCharsetException ex) {
                log.error("  --  cambioCuentaREINT  -- UnsupportedCharsetException  al llamar a la API de fraccionamiento. " + ex.getMessage());
                codOperacion = ERROR_LLAMADA_SW;
            } catch (ParseException ex) {
                log.error("  --  cambioCuentaREINT  -- ParseException  al llamar a la API de fraccionamiento. " + ex.getMessage());
                codOperacion = ERROR_LLAMADA_SW;
            } catch (JSONException ex) {
                log.error("  --  cambioCuentaREINT  -- JSONException  al llamar a la API de fraccionamiento. " + ex.getMessage());
                codOperacion = ERROR_LLAMADA_SW;
            }
        } catch (Exception e) {
            log.error("  --  cambioCuentaREINT  -- Excepción genérica operación. " + e.getMessage());
            codOperacion = ERROR_GENERICO;
            }

        log.info("<==== cambioCuentaREINT:devolvemos " + codOperacion);
        return codOperacion;
        }

    /**
     * metodo invocado desde jsp que llama a altaFraccionamientoSubvenciones()
     * de ZORKU
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
    public String altaFraccionamientoREINT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("===========>  altaFraccionamientoREINT ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String codOperacion = OPERACION_CORRECTA;
        String ejercicio = "";
        String codProcedimiento = "";
        String idDeuda = "";
        String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide07.NOMBRE_MODULO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String CTAIBAN = null;
        String CTAENTIDAD = null;
        String CTASUCURSAL = null;
        String CTADC = null;
        String CTANUMERO = null;
        String codCampoIban = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTAIBAN, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoEntidad = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTAENTIDAD, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoSucursal = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTASUCURSAL, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoDC = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTADC, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoNumeroCuenta = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_CTANUMERO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoIdDeudaRes = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_ID_DEUDA, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoFecSolicitudFracc = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FECHA_SOLICITUD_FRACC, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoFecLimitePago = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_FEC_VENCIMIENTO_CARTA_RES, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoImportePlazo = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_IMP_PLAZO_F, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoPlazoFrac = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_LLAMAR_FRACCIONAR, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codCampoNoFraccionar = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.CAMPO_LLAMAR_FRACCIONAR, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        String codTramSolFracc = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide07.TRAMITE_SOL_FRACC, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        //Recuperamos el valor de la URL
        String urlFraccionamientoDeuda = ConfigurationParameter.getParameter(ConstantesMeLanbide07.URL_FRACCIONAMIENTO, ConstantesMeLanbide07.FICHERO_PROPIEDADES);
        BigDecimal plazo = new BigDecimal("0");
        BigDecimal cuotaSolicitada = new BigDecimal("0");
        Calendar fecSoliFrac = null;
        Calendar fecLimitePago = null;
        String sinFraccionar = "";
        AdaptadorSQLBD adapt = null;
        GeneralValueObject resultadoOperacion = new GeneralValueObject();

        try {
            if (numExpediente != null && !"".equals(numExpediente)) {
                String[] datos = numExpediente.split(BARRA);
                ejercicio = datos[0];
                codProcedimiento = datos[1];
            }
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            int ocuTramSolFracc = MeLanbide07Manager.getInstance().getMaxOcurrenciaTramitexCodigo(codOrganizacion, codProcedimiento, ejercicio, numExpediente, Integer.parseInt(codTramSolFracc), adapt);

            SalidaIntegracionVO campoFecSoliFraccionamiento = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoFecSolicitudFracc, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
            SalidaIntegracionVO campoSinFraccionar = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoNoFraccionar, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
            SalidaIntegracionVO campoFecLimitePago = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codCampoFecLimitePago, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
//Creo el VO de seguridad
            ParamsSeguridadZorku seguridadVO = UtilidadesREINT.getSeguridad(codOrganizacion);

            if (campoSinFraccionar.getStatus() == 0) {
                sinFraccionar = campoSinFraccionar.getCampoSuplementario().getValorDesplegable();
            }
            log.info("  --  altaFraccionamientoREINT  --   sin Fraccionar = " + sinFraccionar);
            if (sinFraccionar.equalsIgnoreCase("S")) {
                log.info("  --  altaFraccionamientoREINT  --   Avanza SIN llamar al SW - OK");
                resultadoOperacion.setAtributo("codigoOperacion", OPERACION_CORRECTA);
                UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                return OPERACION_CORRECTA;
            } else {
                if (campoFecSoliFraccionamiento.getStatus() == 0) {
                    fecSoliFrac = campoFecSoliFraccionamiento.getCampoSuplementario().getValorFecha();
                    log.info("  --  altaFraccionamientoREINT  --   Fecha Solicitud Fraccionamiento: " + fecSoliFrac.getTime());
                } else {
                    log.error("  --  altaFraccionamientoREINT  --   Falta Fecha Solicitud Fraccionamiento: ");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_SOLI_FRAC);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_RECUPERANDO_FECHA_SOLI_FRAC;
                }
                if (campoFecLimitePago.getStatus() == 0) {
                    fecLimitePago = campoFecLimitePago.getCampoSuplementario().getValorFecha();
                    log.info("  --  altaFraccionamientoREINT  --   Fecha Límite" + fecLimitePago);
                } else {
                    log.error("  --  altaFraccionamientoREINT  --   Falta Fecha Límite Pago ");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_LIMITE_PAGO);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_RECUPERANDO_FECHA_LIMITE_PAGO;
                }
                // comprobar si la fecha introducida es anterior o igual al vencimiento de la carta de pago o a HOY
                if (fecSoliFrac.after(Calendar.getInstance())) {
                    log.error("  --  altaFraccionamientoREINT  --   Fecha Solicitud MAYOR que hoy - " + (Calendar.getInstance().getTime()));
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_FECHA_SOL_MAYOR_HOY);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_FECHA_SOL_MAYOR_HOY;
                }
                if (fecSoliFrac.after(fecLimitePago)) {
                    log.error("  --  altaFraccionamientoREINT  --   Fecha Solicitud MAYOR que Limite Pago: ");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_FECHA_SOL_MAYOR_LIMITE);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_FECHA_SOL_MAYOR_LIMITE;
                }
                //convertir el Calendar a formato ZORKU
                String fechaSolicZorku = null;
                if (fecSoliFrac != null) {
                    fechaSolicZorku = UtilidadesREINT.convierteFechaZorku(fecSoliFrac);
                    log.debug("  --  altaFraccionamientoREINT  --   fecha Formateada ZK: " + fechaSolicZorku);
                }

                SalidaIntegracionVO campoIBAN = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoIban, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                SalidaIntegracionVO campoEntidad = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoEntidad, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                SalidaIntegracionVO campoSucursal = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoSucursal, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                SalidaIntegracionVO campoDC = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoDC, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                SalidaIntegracionVO campoCuenta = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoNumeroCuenta, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                SalidaIntegracionVO campoPlazo = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoPlazoFrac, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                SalidaIntegracionVO campoImportePlazo = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        Integer.parseInt(codTramSolFracc), ocuTramSolFracc, codCampoImportePlazo, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                SalidaIntegracionVO campoIdDeudaRes = gestorCampoSup.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                        codCampoIdDeudaRes, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                if (campoIdDeudaRes.getStatus() == 0) {
                    idDeuda = campoIdDeudaRes.getCampoSuplementario().getValorTexto();
                    log.info("  --  altaFraccionamientoREINT  --   idDeuda: " + idDeuda);
                } else {
                    log.error("  --  altaFraccionamientoREINT  --   Devolvemos: " + ERROR_RECUPERANDO_ID_DEUDA);
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_RECUPERANDO_ID_DEUDA);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_RECUPERANDO_ID_DEUDA;
                }
                if (campoIBAN.getStatus() == 0) {
                    CTAIBAN = campoIBAN.getCampoSuplementario().getValorTexto().toUpperCase();
                } else {
                    log.error("  --  altaFraccionamientoREINT  --   Falta IBAN ");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_RECUPERANDO_IBAN);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_RECUPERANDO_IBAN;
                }
                if (campoEntidad.getStatus() == 0) {
                    CTAENTIDAD = campoEntidad.getCampoSuplementario().getValorTexto();
                } else {
                    log.error("  --  altaFraccionamientoREINT  --   Falta ENTIDAD ");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_RECUPERANDO_ENTIDAD);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_RECUPERANDO_ENTIDAD;
                }
                if (campoSucursal.getStatus() == 0) {
                    CTASUCURSAL = campoSucursal.getCampoSuplementario().getValorTexto();
                } else {
                    log.error("  --  altaFraccionamientoREINT  --   Falta SUCURSAL ");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_RECUPERANDO_SUCURSAL);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_RECUPERANDO_SUCURSAL;
                }
                if (campoDC.getStatus() == 0) {
                    CTADC = campoDC.getCampoSuplementario().getValorTexto();
                } else {
                    log.error("  --  altaFraccionamientoREINT  --   Falta DC ");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_RECUPERANDO_DC);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_RECUPERANDO_DC;
                }
                if (campoCuenta.getStatus() == 0) {
                    CTANUMERO = campoCuenta.getCampoSuplementario().getValorTexto();
                } else {
                    log.error("  --  altaFraccionamientoREINT  --   Falta Nº CUENTA ");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_RECUPERANDO_NUM_CUENTA);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_RECUPERANDO_NUM_CUENTA;
                }
                if (CTAIBAN.length() != 4 || CTAENTIDAD.length() != 4 || CTASUCURSAL.length() != 4 || CTADC.length() != 2 || CTANUMERO.length() != 10) {
                    log.error("  --  altaFraccionamientoREINT  --   El numero de cuenta no es valido - El tamaño de algún dato es incorrecto");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_CUENTA_TAMANIO);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_CUENTA_TAMANIO;
                }
                String ibanCompleto = CTAIBAN + CTAENTIDAD + CTASUCURSAL + CTADC + CTANUMERO;
                if (!UtilidadesREINT.validaIBAN(ibanCompleto)) {
                    log.error("  --  altaFraccionamientoREINT  --   El numero de cuenta no es valido");
                    resultadoOperacion.setAtributo("codigoOperacion", ERROR_CUENTA_INCORRECTA);
                    UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                    return ERROR_CUENTA_INCORRECTA;
                }
                log.debug("  --  altaFraccionamientoREINT  --   IBAN: " + CTAIBAN + " - Entidad: " + CTAENTIDAD + " - Suc: " + CTASUCURSAL + " - DC: " + CTADC + " - ");

                // cargo el objeto parametrosFracc
                log.info("  --  altaFraccionamientoREINT  --   Paso los datos a parametrosFracc");
                ParamsAltaFraccionamiento parametrosFracc = new ParamsAltaFraccionamiento();
                parametrosFracc.setZorUsUsuario(seguridadVO.getUsuario());
                parametrosFracc.setZorUsPassword(seguridadVO.getContrasena());
                parametrosFracc.setArea(seguridadVO.getArea());
                parametrosFracc.setZorLiFechaSolicitudFracc(fechaSolicZorku);
                parametrosFracc.setZorLiNumLiquidacion(new BigDecimal(idDeuda));
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
                    String jsonBody = new Gson().toJson(parametrosFracc);
                    StringEntity entidadLlamada = new StringEntity(jsonBody, ContentType.APPLICATION_JSON);
                    HttpPost llamadaFraccionamiento = new HttpPost(urlFraccionamientoDeuda);
                    llamadaFraccionamiento.setHeader("Accept", "application/json");
                    llamadaFraccionamiento.setEntity(entidadLlamada);
                    log.info("  --  altaFraccionamientoREINT  --   Llamando al servicio " + urlFraccionamientoDeuda + " ... " + jsonBody);
                    HttpResponse respuesta = client.execute(llamadaFraccionamiento);
                    // recojo respuesta
                    log.info("  --  altaFraccionamientoREINT  --  getStatusLine() : " + respuesta.getStatusLine());
                    int statusCode = respuesta.getStatusLine().getStatusCode();
                    log.info("  --  altaFraccionamientoREINT  --  getStatusLine().getStatusCode() : " + statusCode);
                    log.info("  --  altaFraccionamientoREINT  --  Hay respuesta de ZORKU");
                    HttpEntity entidadRespuesta = respuesta.getEntity();
                    // convierto la respuesta a STRING
                    String respuestaString = EntityUtils.toString(entidadRespuesta);
                    log.debug("  --  altaFraccionamientoREINT  --  Respuesta String: " + respuestaString);
                    if (!respuestaString.isEmpty()) {
                        // convierto a JSON
                        JSONObject respuestaJson;
                        switch (statusCode) {
                            case HttpStatus.SC_OK:
                                respuestaJson = new JSONObject(respuestaString);
                                log.info("  --  altaFraccionamientoREINT  --  Fraccionamiento OK");
                                break;
                            case HttpStatus.SC_BAD_REQUEST:
                                respuestaJson = new JSONObject(respuestaString);
                                String codError = respuestaJson.getString("codigoError");
                                String msgErrorCas = respuestaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                                String msgErrorEus = respuestaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                                log.error("  --  altaFraccionamientoREINT  -- Error  al llamar a ZORKU  - " + codError);
                                log.error("  --  altaFraccionamientoREINT  -- " + msgErrorCas);
                                log.error("  --  altaFraccionamientoREINT  -- " + msgErrorEus);
                                codOperacion = msgErrorCas + " " + BARRA + " " + msgErrorEus;
                                resultadoOperacion.setAtributo("codigoOperacion", codOperacion);
                                UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                                return msgErrorCas;
                            //           break;
                            case HttpStatus.SC_NOT_FOUND:
                                log.error("  --  altaFraccionamientoREINT  -- Error 404 al llamar a ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                resultadoOperacion.setAtributo("codigoOperacion", "Error 404 al llamar a ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                                return ERROR_404;
                            default:
                                log.error("  --  altaFraccionamientoREINT  -- Error " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase() + " al llamar a ZORKU ");
                                resultadoOperacion.setAtributo("codigoOperacion", "Error " + statusCode + " al llamar a ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                                UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                                return ERROR_LLAMADA_SW;
                        }

                    } else {
                        log.error("  --  altaFraccionamientoREINT  -- Respuesta  de la ZORKU " + statusCode + " - " + respuesta.getStatusLine().getReasonPhrase());
                        resultadoOperacion.setAtributo("codigoOperacion", "Error " + statusCode + " al llamar a ZORKU " + respuesta.getStatusLine().getReasonPhrase());
                        UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
                        return ERROR_LLAMADA_SW;
                    }
                    log.info("  --  altaFraccionamientoREINT  --  FIN");
                } catch (IOException e) {
                    log.error("  --  altaFraccionamientoREINT  --  IOException  ", e);
                    codOperacion = ERROR_GENERICO;
                } catch (UnsupportedCharsetException e) {
                    log.error("  --  altaFraccionamientoREINT  --  UnsupportedCharsetException ", e);
                    codOperacion = ERROR_GENERICO;
                } catch (ParseException e) {
                    log.error("  --  altaFraccionamientoREINT  --  ParseException ", e);
                    codOperacion = ERROR_GENERICO;
                } catch (JSONException e) {
                    log.error("  --  altaFraccionamientoREINT  --  JSONException ", e);
                    codOperacion = ERROR_GENERICO;
                }
            }

        } catch (Exception e) {
            log.error("error en altaFraccionamientoREINT ", e);
            codOperacion = ERROR_GENERICO;
        }
        log.info("<==== altaFraccionamientoREINT  --  devolvemos " + codOperacion);
        resultadoOperacion.setAtributo("codigoOperacion", codOperacion);
        UtilidadesREINT.retornarJSON(new Gson().toJson(resultadoOperacion), response);
        return codOperacion;
    }

    /**
     * Operacion que recupera los datos de conexion a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     * @throws java.sql.SQLException
     */
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
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexi?n al esquema gen?rico
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

    private String obtenerXmlSalida(String resultadoOperacion, String idDeuda) {
        StringBuffer xmlSalida = null;

        xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(resultadoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<ID_DEUDA>");
        xmlSalida.append(idDeuda);
        xmlSalida.append("</ID_DEUDA>");
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaExp(String resultadoOperacion) {
        StringBuffer xmlSalida = null;

        xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(resultadoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }

    private void retornarXML(String salida, HttpServletResponse response) {
        try {
            if (salida != null) {
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(salida);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            log.error("retornarXML : " + e.getMessage(), e);
        }
    }

}//class
