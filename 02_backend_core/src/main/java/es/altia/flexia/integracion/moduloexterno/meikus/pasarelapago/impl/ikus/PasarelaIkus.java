package es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.impl.ikus;

import es.altia.flexia.integracion.moduloexterno.meikus.exception.MeIkus01Exception;
import es.altia.flexia.integracion.moduloexterno.meikus.manager.MeIkus01Manager;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.IPasarelaPago;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.PagoPasarelaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.ResultadoReservaPasarelaPagosVO;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.ResultadoResolucionPasarelaPagosVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.Exception_Exception;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.ServiceOpc2;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BFormaJuridicaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BImporteAniosVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BLineaAyudaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BPagoVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BReservaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BResolucionVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BResultadoReservaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BResultadoResolucionVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BResultadoTerceroIkusVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BSeguridadVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BTerceroIkusVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BTerceroVO;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BTerritorioHistoricoVO;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01Constantes;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01Utils;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.ConvocatoriaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.OperacionesPasarelaEnum;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.RespuestaOperacionVO;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.TerceroPasarela;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.InteresadoExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 03/12/2012 1.0 Historial de cambios:
 * <ol>
 * <li>david.caamano * 03/12/2012 * Edición inicial</li>
 * </ol>
 */
public class PasarelaIkus implements IPasarelaPago {

    //Logger
    private final static Logger log = LogManager.getLogger(PasarelaIkus.class);

    //Codigos de error
    private static final ArrayList<String> codigosError = new ArrayList<String>();

    //URL
    private static final String URL_WEBSERVICE = "PASARELA_PAGOS/URL_WEBSERVICE";

    //Service name
    private static final String SERVICE_NAME_URL = "PASARELA_PAGOS/SERVICE_NAME_URL";
    private static final String SERVICE_NAME_NAME = "PASARELA_PAGOS/SERVICE_NAME_NAME";

    //Datos de seguridad del servicio web
    private static final String WEBSERVICE_USER = "PASARELA_PAGOS/WEBSERVICE_USER";
    private static final String WEBSERVICE_PASS = "PASARELA_PAGOS/WEBSERVICE_PASS";

    //Operacion correcta
    private static final String OPERACION_CORRECTA = "0";

    //Instancia del cliente del servicio web
    private ServiceOpc2 instance = null;

    //Campos suplementarios tercero IKUS
    private static final String CAMPO_SUPLEMENTARIO_IKUS_TER_COD = "IKUS_TER_COD";
    private static final String CAMPO_SUPLEMENTARIO_IKUS_TER_VER = "IKUS_TER_VER";
    private static final String CAMPO_SUPLEMENTARIO_IKUS_TER_TIP = "IKUS_TER_TIP";
    private static final String CAMPO_SUPLEMENTARIO_IKUS_TER_F_JUR = "IKUS_TER_F_JUR";

    //Constante que indica que no se produjeron errores
    private static final String TODO_CORRECTO = "0";

    //Errores funcionales del expediente
    private static final String CAMPO_COD_LINEA_AYUDA_NO_VALIDO = "10";
    private static final String CAMPOS_IMPORTE_ANIO_NO_VALIDO = "11";
    private static final String CAMPO_ANIO_CONVOCATORIA_NO_VALIDO = "13";
    private static final String CAMPO_IMPORTE_CONCEDIDO_NO_VALIDO = "14";
    private static final String ERROR_CAMPOS_ACCESO_IKUS = "15";
    private static final String ERROR_RECUPERANDO_NUM_MAXIMO_PAGOS = "17";
    private static final String ERROR_RECUPERANDO_PAGOS_REALIZADOS = "18";

    //Error del territorio historico
    private static final String ERROR_TERRITORIO_HISTORICO = "16";

    //Error recuperando los datos suplementarios del tercero
    private static final String ERROR_DATOS_SUPLEMENTARIO_TERCERO = "20";

    //Error recuperando el campo del pago
    private static final String ERROR_RECUPERANDO_CAMPO_PAGO = "21";

    //Error recuperando el id de expediente generado por la pasarela de pago
    private static final String ERROR_RECUPERANDO_EXP_PASARELA_PAGO = "22";

    //Error llamando al IKUS
    private static final String ERROR_LLAMANDO_PASARELA = "23";

    //Error recuperando el dato suplementario de trámite fecha resolucion
    private static final String ERROR_SUPLEMENTARIO_TRAM_RESOLUCION = "24";

    private static final String ERROR_SUPLEMENTARIO_EIKA = "25";
    private static final String ERROR_EJERCICIO_CONTABLE = "26";

    private final SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Inicia el registro de pagos del expediente
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    @Override
    public String iniciarExpedientePasarela(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("iniciarExpedientePasarela() : BEGIN");
        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
        String codError = TODO_CORRECTO;
        try {
            PagoPasarelaVO pago = new PagoPasarelaVO();
            pago.setCodOrganizacion(codOrganizacion);
            pago.setCodProcedimiento(codProcedimiento);
            pago.setNumExpediente(numExpediente);
            pago.setEjercicio(ejercicio);
            pago.setNumPago("0");
            meIkus01Manager.altaExpedientePago(pago);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error iniciando el expediente para la plataforma " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error iniciando el expediente para la plataforma ", ex);
        }//try-catch
        log.debug("iniciarExpedientePasarela() : END");
        return codError;
    }//iniciarExpedientePasarela

    /**
     * Llama a la operacion de reservar credito de la pasarela de pagos IKUS y
     * guarda un registro del resultado de la operacion
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    @Override
    public ResultadoReservaPasarelaPagosVO reserva(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("reserva Pago() : BEGIN");
        ResultadoReservaPasarelaPagosVO resultadoReservaVO = new ResultadoReservaPasarelaPagosVO();
        try {
            W75BResultadoReservaVO resultado = new W75BResultadoReservaVO();
            log.debug("Creamos un objeto del tipo SeguridadVO para la llamada a la pasarela IKUS");
            W75BSeguridadVO seguridadVO = new W75BSeguridadVO();
            try {
                seguridadVO = getSeguridadVO(codOrganizacion, codProcedimiento);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS ", ex);
                meIkusException.setCodError(ERROR_CAMPOS_ACCESO_IKUS);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el numero maximo de pagos del expediente");
            Integer numPagos = null;
            try {
                numPagos = getNumPagos(codProcedimiento, codOrganizacion);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el numero maximo de pagos del expediente " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el numero maximo de pagos del expediente ", ex);
                meIkusException.setCodError(ERROR_RECUPERANDO_NUM_MAXIMO_PAGOS);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo ImporteAniosVO para la llamada a la pasarela IKUS");
            W75BImporteAniosVO importeAniosVO = new W75BImporteAniosVO();
            try {
                importeAniosVO = getImportesReservaAnios(numExpediente, codProcedimiento, ejercicio, codOrganizacion, numPagos);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando los importes del expediente " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando los importes del expediente ", ex);
                meIkusException.setCodError(CAMPOS_IMPORTE_ANIO_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo LineaAyudaVO para la llamada a la pasarela IKUS");
            W75BLineaAyudaVO lineaAyudaVO = new W75BLineaAyudaVO();
            try {
                log.debug("Recuperamos el id de la linea de los campos suplementarios del expediente");
                //String idLinea = getCampoSuplementarioLineaAyuda(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                // Se parametriza e properties el 17/05/2016 Tarea 271063 Nota: 5
                String idLinea = getIdLIneaAyudaFromFicheroProperties(codOrganizacion, codProcedimiento);
                log.debug("idLinea = " + idLinea);
                lineaAyudaVO.setIdLinea(idLinea);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el id de linea del properties " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el id de linea del fichero properties ", ex);
                meIkusException.setCodError(CAMPO_COD_LINEA_AYUDA_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el anio de la convocatoria");
            String anioConvocatoria = new String();
            try {
                //anioConvocatoria = getAnioConvocatoria(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                // 17/05/2016 El año de la convocatoria será el año del expediente Tarea 271063 Nota: 5. 
                anioConvocatoria = getAnioFromNumExpediente(numExpediente);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el anio de la convocatoria " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria ", ex);
                meIkusException.setCodError(CAMPO_ANIO_CONVOCATORIA_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo ReservaVO para la llamada a la pasarela IKUS");
            W75BReservaVO reservaVO = new W75BReservaVO();
            reservaVO.setImporteAnios(importeAniosVO);
            reservaVO.setLineaAyuda(lineaAyudaVO);
            reservaVO.setEjercicio(anioConvocatoria);
            reservaVO.setNumeroExpediente(numExpediente);

            log.debug("Llamamos una instancia del cliente del webservice");
            ServiceOpc2 service = getWebServiceClient(codOrganizacion, codProcedimiento);

            log.debug("Llamamos a la operacion de grabar reserva");
            resultado = service.getServiceOpc2Port().grabarReserva(seguridadVO, reservaVO);

            log.debug("resultado = " + resultado.getCodigoError());

            //Comprobamos que el objeto retorno no sea nulo.
            if (resultado.getRetorno() != null) {
                log.debug("Existe objeto retorno");
                W75BReservaVO resultadoReserva = resultado.getRetorno();
                W75BImporteAniosVO resultadoImporteAnios = resultadoReserva.getImporteAnios();
                W75BLineaAyudaVO resultadoLineaAyuda = resultadoReserva.getLineaAyuda();
                Date fechaActual = new Date();

                log.debug("Construimos el XML de la respuesta");
                resultadoReservaVO.setCodigoError(OPERACION_CORRECTA);
                //Guardamos el resultado de la operacion
                StringBuilder xmlResultado = new StringBuilder();
                xmlResultado.append("<operacion>");
                xmlResultado.append("<cod_error>");
                xmlResultado.append(OPERACION_CORRECTA);
                xmlResultado.append("</cod_error>");
                xmlResultado.append("<id_reserva>");
                xmlResultado.append(resultadoReserva.getIdReserva());
                xmlResultado.append("</id_reserva>");
                xmlResultado.append("<importe_anios>");
                xmlResultado.append("<importe_anio_1>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio1());
                xmlResultado.append("</importe_anio_1>");
                xmlResultado.append("<importe_anio_2>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio2());
                xmlResultado.append("</importe_anio_2>");
                xmlResultado.append("<importe_anio_3>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio3());
                xmlResultado.append("</importe_anio_3>");
                xmlResultado.append("<importe_anio_4>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio4());
                xmlResultado.append("</importe_anio_4>");
                xmlResultado.append("<importe_anio_5>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio5());
                xmlResultado.append("</importe_anio_5>");
                xmlResultado.append("<importe_resto_anios>");
                xmlResultado.append(resultadoImporteAnios.getImporteRestoAnios());
                xmlResultado.append("</importe_resto_anios>");
                xmlResultado.append("</importe_anios>");
                xmlResultado.append("<id_linea>");
                xmlResultado.append(resultadoLineaAyuda.getIdLinea());
                xmlResultado.append("</id_linea>");
                xmlResultado.append("<fecha_reserva>");
                xmlResultado.append(formateador.format(fechaActual));
                xmlResultado.append("</fecha_reserva>");
                xmlResultado.append("</operacion>");

                log.debug("Construimos el objeto de la respuesta a grabar en la BBDD");
                RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                respuestaOperacionVO.setEjercicio(ejercicio);
                respuestaOperacionVO.setIdExpediente(numExpediente);
                respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.RSV);

                try {
                    log.debug("Grabamos los datos de la respuesta en la BBDD");
                    grabarResultadoOperacion(respuestaOperacionVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
                try {
                    log.debug("Grabamos el Id de la Reserva en la BBDD");
                    grabarIdReserva(numExpediente, codProcedimiento, ejercicio, resultadoReserva.getIdReserva(), codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando el Id de la Reserva en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
            } else {
                log.debug("NO existe objeto retorno");
                resultadoReservaVO.setCodigoError(resultado.getCodigoError());
                resultadoReservaVO.setCodMensajeError(resultado.getMensaje().getDescripcionCastellano());

                log.debug("Construimos el XML de la respuesta");
                //Guardamos el resultado de la operacion
                StringBuilder xmlResultado = new StringBuilder();
                xmlResultado.append("<operacion>");
                xmlResultado.append("<cod_error>");
                xmlResultado.append(resultado.getCodigoError());
                xmlResultado.append("</cod_error>");
                xmlResultado.append("<mensaje_error>");
                xmlResultado.append(resultado.getMensaje().getDescripcionCastellano());
                xmlResultado.append("</mensaje_error>");
                xmlResultado.append("</operacion>");

                log.debug("Construimos el objeto de la respuesta a grabar en la BBDD");
                RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                respuestaOperacionVO.setEjercicio(ejercicio);
                respuestaOperacionVO.setIdExpediente(numExpediente);
                respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.RSV);

                try {
                    log.debug("Grabamos los datos de la respuesta en la BBDD");
                    grabarResultadoOperacion(respuestaOperacionVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
            }//if(resultado.getRetorno() != null)
        } catch (Exception_Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos", ex);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        }//try-catch
        log.debug("reserva() : END");
        return resultadoReservaVO;
    }//reserva

    /**
     * Llama a la operacion de reservar credito de la pasarela de pagos IKUS y
     * guarda un registro del resultado de la operacion
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    @Override
    public ResultadoReservaPasarelaPagosVO reservaSinExt(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("reservaSinExt Pago() : BEGIN");
        ResultadoReservaPasarelaPagosVO resultadoReservaVO = new ResultadoReservaPasarelaPagosVO();
        try {
            W75BResultadoReservaVO resultado = new W75BResultadoReservaVO();
            log.debug("Creamos un objeto del tipo SeguridadVO para la llamada a la pasarela IKUS");
            W75BSeguridadVO seguridadVO = new W75BSeguridadVO();
            try {
                seguridadVO = getSeguridadVO(codOrganizacion, codProcedimiento);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS ", ex);
                meIkusException.setCodError(ERROR_CAMPOS_ACCESO_IKUS);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el numero maximo de pagos del expediente");
            Integer numPagos = null;
            try {
                numPagos = getNumPagos(codProcedimiento, codOrganizacion);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el numero maximo de pagos del expediente " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el numero maximo de pagos del expediente ", ex);
                meIkusException.setCodError(ERROR_RECUPERANDO_NUM_MAXIMO_PAGOS);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo ImporteAniosVO para la llamada a la pasarela IKUS");
            W75BImporteAniosVO importeAniosVO = new W75BImporteAniosVO();
            try {
                importeAniosVO = getImportesReservaAnios(numExpediente, codProcedimiento, ejercicio, codOrganizacion, numPagos);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando los importes del expediente " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando los importes del expediente ", ex);
                meIkusException.setCodError(CAMPOS_IMPORTE_ANIO_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo LineaAyudaVO para la llamada a la pasarela IKUS");
            W75BLineaAyudaVO lineaAyudaVO = new W75BLineaAyudaVO();
            try {
                log.debug("Recuperamos el id de la linea de los campos suplementarios del expediente");
                //String idLinea = getCampoSuplementarioLineaAyuda(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                // Se parametriza e properties el 17/05/2016 Tarea 271063 Nota: 5
                String idLinea = getIdLIneaAyudaFromFicheroProperties(codOrganizacion, codProcedimiento);
                log.debug("idLinea = " + idLinea);
                lineaAyudaVO.setIdLinea(idLinea);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el id de linea del properties " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el id de linea del fichero properties ", ex);
                meIkusException.setCodError(CAMPO_COD_LINEA_AYUDA_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el anio de la convocatoria");
            String anioConvocatoria = new String();
            try {
                //anioConvocatoria = getAnioConvocatoria(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                // 17/05/2016 El año de la convocatoria será el año del expediente Tarea 271063 Nota: 5. 
                anioConvocatoria = getAnioFromNumExpediente(numExpediente);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el anio de la convocatoria " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria ", ex);
                meIkusException.setCodError(CAMPO_ANIO_CONVOCATORIA_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo ReservaVO para la llamada a la pasarela IKUS");
            W75BReservaVO reservaVO = new W75BReservaVO();
            reservaVO.setImporteAnios(importeAniosVO);
            reservaVO.setLineaAyuda(lineaAyudaVO);
            reservaVO.setEjercicio(anioConvocatoria);
            reservaVO.setNumeroExpediente(numExpediente);

            log.debug("Llamamos una instancia del cliente del webservice");
            ServiceOpc2 service = getWebServiceClient(codOrganizacion, codProcedimiento);

            log.debug("Llamamos a la operacion de reservar con los parámetros:\nseguridadVO = ");
            log.debug("seguridadVO: usuario = " + seguridadVO.getUsuario());
            log.debug("seguridadVO: contraseña = " + seguridadVO.getContrasena());
            log.debug("seguridadVO: perfil = " + seguridadVO.getPerfil());
            log.debug("reserva: anio convocatoria = " + reservaVO.getEjercicio());
            log.debug("reserva: Id reserva = " + reservaVO.getIdReserva());
            log.debug("reserva: num expediente = " + reservaVO.getNumeroExpediente());
            log.debug("reserva: fecha reserva = " + reservaVO.getFechaReserva());
            log.debug("reserva: importe ano 1 = " + reservaVO.getImporteAnios().getImporteAnio1());
            log.debug("reserva: importe ano 2 = " + reservaVO.getImporteAnios().getImporteAnio2());
            log.debug("reserva: Id linea ayuda  = " + reservaVO.getLineaAyuda().getIdLinea());
            log.debug("reserva: ultimo ejercicio ayuda = " + reservaVO.getLineaAyuda().getUltimoEjercicio());
            log.debug("reserva: descripcion ayuda = " + reservaVO.getLineaAyuda().getDescripcion());
            log.debug("reserva: es tipo concursal ayuda = " + reservaVO.getLineaAyuda().isTipoConcursal());
            
            log.debug("Llamamos a la operacion de grabar reserva");
            resultado = service.getServiceOpc2Port().grabarReserva(seguridadVO, reservaVO);

            log.debug("resultado = " + resultado.getCodigoError());

            //Comprobamos que el objeto retorno no sea nulo.
            if (resultado.getRetorno() != null) {
                log.debug("Existe objeto retorno");
                W75BReservaVO resultadoReserva = resultado.getRetorno();
                W75BImporteAniosVO resultadoImporteAnios = resultadoReserva.getImporteAnios();
                W75BLineaAyudaVO resultadoLineaAyuda = resultadoReserva.getLineaAyuda();
                Date fechaActual = new Date();

                log.debug("Construimos el XML de la respuesta");
                resultadoReservaVO.setCodigoError(OPERACION_CORRECTA);
                //Guardamos el resultado de la operacion
                StringBuilder xmlResultado = new StringBuilder();
                xmlResultado.append("<operacion>");
                xmlResultado.append("<cod_error>");
                xmlResultado.append(OPERACION_CORRECTA);
                xmlResultado.append("</cod_error>");
                xmlResultado.append("<id_reserva>");
                xmlResultado.append(resultadoReserva.getIdReserva());
                xmlResultado.append("</id_reserva>");
                xmlResultado.append("<importe_anios>");
                xmlResultado.append("<importe_anio_1>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio1());
                xmlResultado.append("</importe_anio_1>");
                xmlResultado.append("<importe_anio_2>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio2());
                xmlResultado.append("</importe_anio_2>");
                xmlResultado.append("<importe_anio_3>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio3());
                xmlResultado.append("</importe_anio_3>");
                xmlResultado.append("<importe_anio_4>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio4());
                xmlResultado.append("</importe_anio_4>");
                xmlResultado.append("<importe_anio_5>");
                xmlResultado.append(resultadoImporteAnios.getImporteAnio5());
                xmlResultado.append("</importe_anio_5>");
                xmlResultado.append("<importe_resto_anios>");
                xmlResultado.append(resultadoImporteAnios.getImporteRestoAnios());
                xmlResultado.append("</importe_resto_anios>");
                xmlResultado.append("</importe_anios>");
                xmlResultado.append("<id_linea>");
                xmlResultado.append(resultadoLineaAyuda.getIdLinea());
                xmlResultado.append("</id_linea>");
                xmlResultado.append("<fecha_reserva>");
                xmlResultado.append(formateador.format(fechaActual));
                xmlResultado.append("</fecha_reserva>");
                xmlResultado.append("</operacion>");

                log.debug("Construimos el objeto de la respuesta a grabar en la BBDD");
                RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                respuestaOperacionVO.setEjercicio(ejercicio);
                respuestaOperacionVO.setIdExpediente(numExpediente);
                respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.RSV);

                try {
                    log.debug("Grabamos los datos de la respuesta en la BBDD");
                    grabarResultadoOperacion(respuestaOperacionVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
                try {
                    log.debug("Grabamos el Id de la Reserva en la BBDD");
                    grabarIdReserva(numExpediente, codProcedimiento, ejercicio, resultadoReserva.getIdReserva(), codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando el Id de la Reserva en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
            } else {
                log.debug("NO existe objeto retorno");
                resultadoReservaVO.setCodigoError(resultado.getCodigoError());
                resultadoReservaVO.setCodMensajeError(resultado.getMensaje().getDescripcionCastellano());

                log.debug("Construimos el XML de la respuesta");
                //Guardamos el resultado de la operacion
                StringBuilder xmlResultado = new StringBuilder();
                xmlResultado.append("<operacion>");
                xmlResultado.append("<cod_error>");
                xmlResultado.append(resultado.getCodigoError());
                xmlResultado.append("</cod_error>");
                xmlResultado.append("<mensaje_error>");
                xmlResultado.append(resultado.getMensaje().getDescripcionCastellano());
                xmlResultado.append("</mensaje_error>");
                xmlResultado.append("</operacion>");

                log.debug("Construimos el objeto de la respuesta a grabar en la BBDD");
                RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                respuestaOperacionVO.setEjercicio(ejercicio);
                respuestaOperacionVO.setIdExpediente(numExpediente);
                respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.RSV);

                try {
                    log.debug("Grabamos los datos de la respuesta en la BBDD");
                    grabarResultadoOperacion(respuestaOperacionVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
            }//if(resultado.getRetorno() != null)
        } catch (Exception_Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos", ex);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        }//try-catch
        log.debug("reservaSinExt() : END");
        return resultadoReservaVO;
    }//reservaSinExt

    /**
     * Llama a la operacion de grabar la resolucion de la pasarela de pagos IKUS
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    @Override
    public ResultadoResolucionPasarelaPagosVO grabarResolucion(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("grabarResolucion() : BEGIN");
        ResultadoResolucionPasarelaPagosVO resultadoResolucionPasarelaPagosVO = new ResultadoResolucionPasarelaPagosVO();
        try {
            W75BResultadoResolucionVO resultado = new W75BResultadoResolucionVO();
            log.debug("Creamos un objeto del tipo SeguridadVO para la llamada a la pasarela IKUS");
            W75BSeguridadVO seguridadVO = new W75BSeguridadVO();
            try {
                seguridadVO = getSeguridadVO(codOrganizacion, codProcedimiento);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS ", ex);
                meIkusException.setCodError(ERROR_CAMPOS_ACCESO_IKUS);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el numero total de pagos");
            Integer numPagos = null;
            try {
                numPagos = getNumPagos(codProcedimiento, codOrganizacion);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el numero maximo de pagos del expediente " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el numero maximo de pagos del expediente ", ex);
                meIkusException.setCodError(ERROR_RECUPERANDO_NUM_MAXIMO_PAGOS);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo ImporteAniosVO para la llamada a la pasarela IKUS");
            W75BImporteAniosVO importeAniosVO = new W75BImporteAniosVO();
            try {
                importeAniosVO = getImportesReservaAnios(numExpediente, codProcedimiento, ejercicio, codOrganizacion, numPagos);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando los importes del expediente " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando los importes del expediente ", ex);
                meIkusException.setCodError(CAMPOS_IMPORTE_ANIO_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo LineaAyudaVO para la llamada a la pasarela IKUS");
            W75BLineaAyudaVO lineaAyudaVO = new W75BLineaAyudaVO();
            try {
                log.debug("Recuperamos el id de la linea de los campos suplementarios del expediente");
                //String idLinea = getCampoSuplementarioLineaAyuda(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                // Se parametriza e properties el 17/05/2016 Tarea 271063 Nota: 5
                String idLinea = getIdLIneaAyudaFromFicheroProperties(codOrganizacion, codProcedimiento);
                log.info("idLinea = " + idLinea);
                lineaAyudaVO.setIdLinea(idLinea);
            } catch (MeIkus01Exception ex) {    //MeIkus01Exception
                log.error("Se ha producido un error recuperando el id de linea de los campos suplementarios " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el id de linea de los campos suplementarios ", ex);
                meIkusException.setCodError(CAMPO_COD_LINEA_AYUDA_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el importe concedido del expediente");
            Double importeConcedido = null;
            try {
                String importe = getCampoSuplementarioImporteReserva(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                importeConcedido = Double.valueOf(importe);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el importe concedido de los campos suplementarios " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el importe concedido de los campos suplementarios ", ex);
                meIkusException.setCodError(CAMPO_IMPORTE_CONCEDIDO_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el anio de la convocatoria");
            String anioConvocatoria = new String();
            try {
                //anioConvocatoria = getAnioConvocatoria(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                // 17/05/2016 El año de la convocatoria será el año del expediente Tarea 271063 Nota: 5. 
                anioConvocatoria = getAnioFromNumExpediente(numExpediente);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria ", ex);
                meIkusException.setCodError(CAMPO_ANIO_CONVOCATORIA_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos los datos del tercero");
            TerceroPasarela datosTercero = new TerceroPasarela();
            try {
                datosTercero = getDatosSuplementariosTercero(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando los datos suplementarios del tercero " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando los datos suplementarios del tercero " + ex.getMessage());
                meIkusException.setCodError(ERROR_DATOS_SUPLEMENTARIO_TERCERO);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo W75BFormaJuridicaVO para la forma juridica del tercero IKUS");
            W75BFormaJuridicaVO formaJuridicaVO = new W75BFormaJuridicaVO();
            formaJuridicaVO.setCodigo(datosTercero.getFormaJuridica());

            log.debug("Creamos un objeto del tipo W75BTerceroVO para los datos de tercero del IKUS");
            W75BTerceroVO terceroIkus = new W75BTerceroVO();
            terceroIkus.setNombre(datosTercero.getNombre());
            terceroIkus.setCodigo(datosTercero.getDocumento());
            terceroIkus.setTipo(datosTercero.getTipoTercero());
            terceroIkus.setVersionTercero(datosTercero.getVersion());
            terceroIkus.setFormaJuridica(formaJuridicaVO);

            log.debug("Creamos un objeto W75BTerritorioHistoricoVO para el territorio historico");
            W75BTerritorioHistoricoVO territorioHistoricoVO = new W75BTerritorioHistoricoVO();
            territorioHistoricoVO.setIdTerritorio(datosTercero.getCodTerritorioHistorico());

            /////    
            log.debug("Recuperamos los datos de la fechaResolucion");

            String fechaResolucion = null;
            Date fechaDate = null;
            GregorianCalendar calendario = new GregorianCalendar();
            XMLGregorianCalendar xmlCalendario = null;

            try {
                fechaResolucion = getCampoSuplementarioTramiteFechaResolucion(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                fechaDate = formateador.parse(fechaResolucion);
                calendario.setTime(fechaDate);
                xmlCalendario = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendario);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando la fecha resolución de los campos suplementarios de trámite " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando la fecha resolución de los campos suplementarios de trámite ", ex);
                meIkusException.setCodError(ERROR_SUPLEMENTARIO_TRAM_RESOLUCION);
                throw meIkusException;
            }//try-catch
            //////    

//                Si este campo se manda vacío o en S hace el envío, si se manda a N solo guarda los datos en PasIkus, para las pruebas enviarlo a N
            String envioEika = "";
//             envioEika = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA
//                    + MeIkus01Constantes.NOMBRE_MODULO + MeIkus01Constantes.BARRA + MeIkus01Constantes.ENVIO_EIKA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            try {
                envioEika = getValorEnvioEika(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el valor del envío a EIKA  " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el valor del envío a EIKA ", ex);
                meIkusException.setCodError(ERROR_SUPLEMENTARIO_EIKA);
                throw meIkusException;
            }

            String concepto = numExpediente + " CONVOCATORIA " + codProcedimiento + " " + ejercicio;

            log.debug("Creamos un objeto del tipo ResolucionVO para la llamada a la pasarela IKUS");
            log.info("Importe: " + importeConcedido);
            log.info("FECHA RESOLUCION: " + fechaResolucion);
            log.info("Envío a EIKA: " + envioEika);
            log.info("Concepto: " + concepto);
            W75BResolucionVO resolucion = new W75BResolucionVO();
            resolucion.setNumeroExpediente(numExpediente);
            resolucion.setLineaAyuda(lineaAyudaVO);
            resolucion.setAnioConvocatoria(anioConvocatoria);
            resolucion.setImporteAnios(importeAniosVO);
            resolucion.setTercero(terceroIkus);
            resolucion.setTerritorioHistorico(territorioHistoricoVO);
            resolucion.setImporteConcedido(importeConcedido);
            resolucion.setConceptoResumido(concepto);
            resolucion.setFechaResolucion(xmlCalendario);
            resolucion.setEnvioEIKA(envioEika);
//            resolucion.setEjercicioContable(getAnioFromNumExpediente(numExpediente));

            log.debug("Llamamos una instancia del cliente del webservice");
            ServiceOpc2 service = getWebServiceClient(codOrganizacion, codProcedimiento);

            log.debug("Llamamos a la operacion de grabar resolucion");
            resultado = service.getServiceOpc2Port().grabarResolucion(seguridadVO, resolucion);

            log.debug("resultado = " + resultado.getCodigoError());
            if (resultado.getRetorno() != null) {
                log.debug("Existe objeto retorno");
                resultadoResolucionPasarelaPagosVO.setCodigoError(OPERACION_CORRECTA);
                log.debug("Creamos el XML con los datos de la respuesta");
                //Guardamos el resultado de la operacion
                StringBuilder xmlResultado = new StringBuilder();
                xmlResultado.append("<operacion>");
                xmlResultado.append("<cod_error>");
                xmlResultado.append(OPERACION_CORRECTA);
                xmlResultado.append("</cod_error>");
                xmlResultado.append("<importe_concedido>");
                xmlResultado.append(resultado.getRetorno().getImporteConcedido());
                xmlResultado.append("</importe_concedido>");
                xmlResultado.append("<id_expediente>");
                xmlResultado.append(resultado.getRetorno().getIdExpediente());
                xmlResultado.append("</id_expediente>");
                if (envioEika.equalsIgnoreCase("S")) {
                    xmlResultado.append("<exp_eika>");
                    xmlResultado.append(resultado.getRetorno().getExpEika());
                    xmlResultado.append("</exp_eika>");
                }

                xmlResultado.append("<importe_anios>");
                xmlResultado.append("<importe_anio_1>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio1());
                xmlResultado.append("</importe_anio_1>");
                xmlResultado.append("<importe_anio_2>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio2());
                xmlResultado.append("</importe_anio_2>");
                xmlResultado.append("<importe_anio_3>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio3());
                xmlResultado.append("</importe_anio_3>");
                xmlResultado.append("<importe_anio_4>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio4());
                xmlResultado.append("</importe_anio_4>");
                xmlResultado.append("<importe_anio_5>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio5());
                xmlResultado.append("</importe_anio_5>");
                xmlResultado.append("<importe_resto_anios>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteRestoAnios());
                xmlResultado.append("</importe_resto_anios>");
                xmlResultado.append("</importe_anios>");
                xmlResultado.append("<tercero>");
                xmlResultado.append("<id_tercero>");
                xmlResultado.append(resultado.getRetorno().getTercero().getIdTercero());
                xmlResultado.append("</id_tercero>");
                xmlResultado.append("<tipo>");
                xmlResultado.append(resultado.getRetorno().getTercero().getTipo());
                xmlResultado.append("</tipo>");
                xmlResultado.append("<version>");
                xmlResultado.append(resultado.getRetorno().getTercero().getVersionTercero());
                xmlResultado.append("</version>");
                xmlResultado.append("</tercero>");
                xmlResultado.append("<id_territorio>");
                xmlResultado.append(resultado.getRetorno().getTerritorioHistorico().getIdTerritorio());
                xmlResultado.append("</id_territorio>");
                xmlResultado.append("<fecha_resolucion>");
                xmlResultado.append(fechaDate);
                xmlResultado.append("</fecha_resolucion>");
                xmlResultado.append("</operacion>");

                log.debug("Creamos el el objeto con la respuesta para grabarlo");
                RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                respuestaOperacionVO.setEjercicio(ejercicio);
                respuestaOperacionVO.setIdExpediente(numExpediente);
                respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.GRES);
                MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();

                try {
                    log.debug("Grabamos los datos de la respuesta en la BBDD");
                    grabarResultadoOperacion(respuestaOperacionVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-

                try {
                    log.debug("Grabamos el ID del Expediente PASIKUS en la BBDD");
                    grabarIdExpedientePasarela(numExpediente, codProcedimiento, ejercicio, resultado.getRetorno().getIdExpediente(), codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando el ID del Expediente PASIKUS en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
                if (envioEika.equalsIgnoreCase("S")) {
                    try {
                        log.debug("Grabamos el ID del Expediente EIKA en la BBDD");
                        grabarIdExpedienteEikaD(numExpediente, codProcedimiento, ejercicio, resultado.getRetorno().getExpEika(), codOrganizacion);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando el ID del Expediente EIKA en la BBDD, se continua con la operacion " + ex.getMessage());
                    }//try-catch
                }
                try {
                    log.debug("Grabamos el ID PASIKUS del Tercero en la BBDD");
                    datosTercero.setIdTerceroIkus(resultado.getRetorno().getTercero().getIdTercero());
                    meIkus01Manager.grabarIdTercero(datosTercero, codProcedimiento, codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando el ID PASIKUS del Tercero en la BBDD, se continua con la operacion " + ex.getMessage());
                }

                log.debug("Creamos el objeto para actualizar el registro de pagos");
                PagoPasarelaVO pagoPasarelaVO = new PagoPasarelaVO();
                pagoPasarelaVO.setCodOrganizacion(codOrganizacion);
                pagoPasarelaVO.setCodProcedimiento(codProcedimiento);
                pagoPasarelaVO.setEjercicio(ejercicio);
                pagoPasarelaVO.setNumExpediente(numExpediente);
                pagoPasarelaVO.setIdExpedientePasarela(resultado.getRetorno().getIdExpediente());
                try {
                    log.debug("Actualizamos el registro de pagos para anhadir el id del expediente develto por la pasarela");
                    meIkus01Manager.anhadirIdExpedientePasarela(pagoPasarelaVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos del pago en la BBDD, se continua con la operacion  " + ex.getMessage());
                }//try-catch
            } else {
                log.debug("NO existe objeto retorno");
                String tubo = MeIkus01ConfigurationParameter.getParameter(MeIkus01Constantes.TUBO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
                if (resultado.getCodigoError().contains("|")) {
                    String[] error = resultado.getCodigoError().split(tubo);
                    resultadoResolucionPasarelaPagosVO.setCodigoError(error[0]);
                    resultadoResolucionPasarelaPagosVO.setCodMensajeError(error[1]);
                } else {
                    resultadoResolucionPasarelaPagosVO.setCodigoError(resultado.getCodigoError());
                    resultadoResolucionPasarelaPagosVO.setCodMensajeError(resultado.getMensaje().getDescripcionCastellano());
                }

                //Guardamos el resultado de la operacion
                log.debug("Creamos el XML con los datos de la respuesta");
                StringBuilder xmlResultado = new StringBuilder();
                xmlResultado.append("<operacion>");
                xmlResultado.append("<cod_error>");
                xmlResultado.append(resultado.getCodigoError());
                xmlResultado.append("</cod_error>");
                xmlResultado.append("<mensaje_error>");
                xmlResultado.append(resultado.getMensaje().getDescripcionCastellano());
                xmlResultado.append("</mensaje_error>");
                xmlResultado.append("</operacion>");

                log.debug("Creamos el el objeto con la respuesta para grabarlo");
                RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                respuestaOperacionVO.setEjercicio(ejercicio);
                respuestaOperacionVO.setIdExpediente(numExpediente);
                respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.GRES);

                try {
                    log.debug("Grabamos los datos de la respuesta en la BBDD");
                    grabarResultadoOperacion(respuestaOperacionVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
            }//if(resultado.getRetorno() != null)
        } catch (Exception_Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos", ex);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        } //try-catch
        catch (ParseException ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        } catch (DatatypeConfigurationException ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        }//try-catch
        log.debug("grabarResolucion() : END");
        return resultadoResolucionPasarelaPagosVO;
    }//grabarResolucion

    /**
     * Llama a la operacion de grabar la resolucion de la pasarela de pagos IKUS
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    @Override
    public ResultadoResolucionPasarelaPagosVO grabarResolucionSinExt(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("grabarResolucionSinExt() : BEGIN");
        ResultadoResolucionPasarelaPagosVO resultadoResolucionPasarelaPagosVO = new ResultadoResolucionPasarelaPagosVO();
        try {
            W75BResultadoResolucionVO resultado = new W75BResultadoResolucionVO();
            log.debug("Creamos un objeto del tipo SeguridadVO para la llamada a la pasarela IKUS");
            W75BSeguridadVO seguridadVO = new W75BSeguridadVO();
            try {
                seguridadVO = getSeguridadVO(codOrganizacion, codProcedimiento);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS ", ex);
                meIkusException.setCodError(ERROR_CAMPOS_ACCESO_IKUS);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el numero total de pagos");
            Integer numPagos = null;
            try {
                numPagos = getNumPagos(codProcedimiento, codOrganizacion);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el numero maximo de pagos del expediente " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el numero maximo de pagos del expediente ", ex);
                meIkusException.setCodError(ERROR_RECUPERANDO_NUM_MAXIMO_PAGOS);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo ImporteAniosVO para la llamada a la pasarela IKUS");
            W75BImporteAniosVO importeAniosVO = new W75BImporteAniosVO();
            try {
                importeAniosVO = getImportesReservaAnios(numExpediente, codProcedimiento, ejercicio, codOrganizacion, numPagos);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando los importes del expediente " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando los importes del expediente ", ex);
                meIkusException.setCodError(CAMPOS_IMPORTE_ANIO_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Creamos un objeto del tipo LineaAyudaVO para la llamada a la pasarela IKUS");
            W75BLineaAyudaVO lineaAyudaVO = new W75BLineaAyudaVO();
            try {
                log.debug("Recuperamos el id de la linea de los campos suplementarios del expediente");
                //String idLinea = getCampoSuplementarioLineaAyuda(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                // Se parametriza e properties el 17/05/2016 Tarea 271063 Nota: 5
                String idLinea = getIdLIneaAyudaFromFicheroProperties(codOrganizacion, codProcedimiento);
                log.info("idLinea = " + idLinea);
                lineaAyudaVO.setIdLinea(idLinea);
            } catch (MeIkus01Exception ex) {    //MeIkus01Exception
                log.error("Se ha producido un error recuperando el id de linea de los campos suplementarios " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el id de linea de los campos suplementarios ", ex);
                meIkusException.setCodError(CAMPO_COD_LINEA_AYUDA_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el importe concedido del expediente");
            Double importeConcedido = null;
            try {
                String importe = getCampoSuplementarioImporteReserva(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                importeConcedido = Double.valueOf(importe);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el importe concedido de los campos suplementarios " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el importe concedido de los campos suplementarios ", ex);
                meIkusException.setCodError(CAMPO_IMPORTE_CONCEDIDO_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos el anio de la convocatoria");
            String anioConvocatoria = new String();
            try {
                //anioConvocatoria = getAnioConvocatoria(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                // 17/05/2016 El año de la convocatoria será el año del expediente Tarea 271063 Nota: 5. 
                anioConvocatoria = getAnioFromNumExpediente(numExpediente);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria ", ex);
                meIkusException.setCodError(CAMPO_ANIO_CONVOCATORIA_NO_VALIDO);
                throw meIkusException;
            }//try-catch

            log.debug("Recuperamos los datos del tercero");
            TerceroPasarela datosTercero = new TerceroPasarela();
            try {
                datosTercero = getDatosSuplementariosTercero(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando los datos suplementarios del tercero " + ex.getMessage());
                throw ex;
            }//try-catch

            log.debug("Datos del tercero: "
                    + "\n\tTerritorio historico = " + datosTercero.getCodTerritorioHistorico()
                    + "\n\tDocumento = " + datosTercero.getDocumento()
                    + "\n\tForma juridica = " + datosTercero.getFormaJuridica()
                    + "\n\tId tercero Ikus = " + datosTercero.getIdTerceroIkus()
                    + "\n\tNombre = " + datosTercero.getNombre()
                    + "\n\tTipo tercero = " + datosTercero.getTipoTercero()
                    + "\n\tVersion = " + datosTercero.getVersion());

            log.debug("Creamos un objeto del tipo W75BFormaJuridicaVO para la forma juridica del tercero IKUS");
            W75BFormaJuridicaVO formaJuridicaVO = new W75BFormaJuridicaVO();
            formaJuridicaVO.setCodigo(datosTercero.getFormaJuridica());

            log.debug("Creamos un objeto del tipo W75BTerceroVO para los datos de tercero del IKUS");
            W75BTerceroVO terceroIkus = new W75BTerceroVO();
            terceroIkus.setNombre(datosTercero.getNombre());
            terceroIkus.setCodigo(datosTercero.getDocumento());
            terceroIkus.setTipo(datosTercero.getTipoTercero());
            terceroIkus.setVersionTercero(datosTercero.getVersion());
            terceroIkus.setFormaJuridica(formaJuridicaVO);

            log.debug("Creamos un objeto W75BTerritorioHistoricoVO para el territorio historico");
            W75BTerritorioHistoricoVO territorioHistoricoVO = new W75BTerritorioHistoricoVO();
            territorioHistoricoVO.setIdTerritorio(datosTercero.getCodTerritorioHistorico());

            /////    
            log.debug("Recuperamos los datos de la fechaResolucion");

            String fechaResolucion = null;
            Date fechaDate = null;
            GregorianCalendar calendario = new GregorianCalendar();
            XMLGregorianCalendar xmlCalendario = null;

            try {
                fechaResolucion = getCampoSuplementarioTramiteFechaResolucion(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                fechaDate = formateador.parse(fechaResolucion);
                calendario.setTime(fechaDate);
                xmlCalendario = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendario);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Es necesario avanzar el trámite GENERAR DOCUMENTO RESOLUCION y rellenar el valor del "
                        + "campo suplementario Fecha De Resolucion (FECRESOL01)", ex);
                meIkusException.setCodError(ERROR_SUPLEMENTARIO_TRAM_RESOLUCION);
                throw meIkusException;
            }//try-catch
            //////    

//                Si este campo se manda vacío o en S hace el envío, si se manda a N solo guarda los datos en PasIkus, para las pruebas enviarlo a N
            String envioEika = "";
//             envioEika = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA
//                    + MeIkus01Constantes.NOMBRE_MODULO + MeIkus01Constantes.BARRA + MeIkus01Constantes.ENVIO_EIKA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            try {
                envioEika = getValorEnvioEika(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el valor del envío a EIKA  " + ex.getMessage());
                MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el valor del envío a EIKA ", ex);
                meIkusException.setCodError(ERROR_SUPLEMENTARIO_EIKA);
                throw meIkusException;
            }

            String concepto = numExpediente + " CONVOCATORIA " + codProcedimiento + " " + ejercicio;

            log.debug("Creamos un objeto del tipo ResolucionVO para la llamada a la pasarela IKUS");
            log.info("Importe: " + importeConcedido);
            log.info("FECHA RESOLUCION: " + fechaResolucion);
            log.info("Envío a EIKA: " + envioEika);
            log.info("Concepto: " + concepto);
            W75BResolucionVO resolucion = new W75BResolucionVO();
            resolucion.setNumeroExpediente(numExpediente);
            resolucion.setLineaAyuda(lineaAyudaVO);
            resolucion.setAnioConvocatoria(anioConvocatoria);
            resolucion.setImporteAnios(importeAniosVO);
            resolucion.setTercero(terceroIkus);
            resolucion.setTerritorioHistorico(territorioHistoricoVO);
            resolucion.setImporteConcedido(importeConcedido);
            resolucion.setConceptoResumido(concepto);
            resolucion.setFechaResolucion(xmlCalendario);
            resolucion.setEnvioEIKA(envioEika);
//            resolucion.setEjercicioContable(getAnioFromNumExpediente(numExpediente));

            log.debug("Llamamos una instancia del cliente del webservice");
            ServiceOpc2 service = getWebServiceClient(codOrganizacion, codProcedimiento);

            log.debug("Llamamos a la operacion de grabarResolucion con los parámetros:\nseguridadVO = ");
            log.debug("seguridadVO: usuario = " + seguridadVO.getUsuario());
            log.debug("seguridadVO: contraseña = " + seguridadVO.getContrasena());
            log.debug("seguridadVO: perfil = " + seguridadVO.getPerfil());
            log.debug("resolucion: anio convocatoria = " + resolucion.getAnioConvocatoria());
            log.debug("resolucion: concepto resumido = " + resolucion.getConceptoResumido());
            log.debug("resolucion: ejercicio contable = " + resolucion.getEjercicioContable());
            log.debug("resolucion: envio EIKA = " + resolucion.getEnvioEIKA());
            log.debug("resolucion: Exp EIKA = " + resolucion.getExpEika());
            log.debug("resolucion: Id Aplicacion = " + resolucion.getIdAplicacion());
            log.debug("resolucion: Id Aplicacion definitiva = " + resolucion.getIdAplicacionDefinitiva());
            log.debug("resolucion Id convocatoria = " + resolucion.getIdConvocatoria());
            log.debug("resolucion: Id disposicion = " + resolucion.getIdDisposicion());
            log.debug("resolucion: Id expediente = " + resolucion.getIdExpediente());
            log.debug("resolucion: numero expediente = " + resolucion.getNumeroExpediente());
            log.debug("resolucion: fecha resolucion = " + resolucion.getFechaResolucion());
            if (resolucion.getTercero() != null) {
                log.debug("resolucion.getTercero(): forma juridica = " + resolucion.getTercero().getFormaJuridica().getCodigo());
                log.debug("resolucion.getTercero(): id tercero = " + resolucion.getTercero().getIdTercero());
                log.debug("resolucion.getTercero(): codigo tercero = " + resolucion.getTercero().getCodigo());
                log.debug("resolucion.getTercero(): tipo tercero = " + resolucion.getTercero().getTipo());
                log.debug("resolucion.getTercero(): version tercero = " + resolucion.getTercero().getVersionTercero());
            }
            if (resolucion.getTerritorioHistorico() != null) {
                log.debug("resolucion.getTerritorioHistorico(): territorio historico = " + resolucion.getTerritorioHistorico().getIdTerritorio());
            }
            if (resolucion.getImporteAnios() != null) {
                log.debug("resolucion: importe anio1 = " + resolucion.getImporteAnios().getImporteAnio1());
                log.debug("resolucion: importe anio2 = " + resolucion.getImporteAnios().getImporteAnio2());
            }
            log.debug("resolucion: importe concedido = " + resolucion.getImporteConcedido());
            if (resolucion.getLineaAyuda() != null) {
                log.debug("resolucion: id linea ayuda = " + resolucion.getLineaAyuda().getIdLinea());
            }
            
            resultado = service.getServiceOpc2Port().grabarResolucion(seguridadVO, resolucion);

            log.debug("resultado = " + resultado.getCodigoError());
            if (resultado.getRetorno() != null) {
                log.debug("Existe objeto retorno");
                resultadoResolucionPasarelaPagosVO.setCodigoError(OPERACION_CORRECTA);
                log.debug("Creamos el XML con los datos de la respuesta");
                //Guardamos el resultado de la operacion
                StringBuilder xmlResultado = new StringBuilder();
                xmlResultado.append("<operacion>");
                xmlResultado.append("<cod_error>");
                xmlResultado.append(OPERACION_CORRECTA);
                xmlResultado.append("</cod_error>");
                xmlResultado.append("<importe_concedido>");
                xmlResultado.append(resultado.getRetorno().getImporteConcedido());
                xmlResultado.append("</importe_concedido>");
                xmlResultado.append("<id_expediente>");
                xmlResultado.append(resultado.getRetorno().getIdExpediente());
                xmlResultado.append("</id_expediente>");
                if (envioEika.equalsIgnoreCase("S")) {
                    xmlResultado.append("<exp_eika>");
                    xmlResultado.append(resultado.getRetorno().getExpEika());
                    xmlResultado.append("</exp_eika>");
                }

                xmlResultado.append("<importe_anios>");
                xmlResultado.append("<importe_anio_1>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio1());
                xmlResultado.append("</importe_anio_1>");
                xmlResultado.append("<importe_anio_2>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio2());
                xmlResultado.append("</importe_anio_2>");
                xmlResultado.append("<importe_anio_3>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio3());
                xmlResultado.append("</importe_anio_3>");
                xmlResultado.append("<importe_anio_4>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio4());
                xmlResultado.append("</importe_anio_4>");
                xmlResultado.append("<importe_anio_5>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteAnio5());
                xmlResultado.append("</importe_anio_5>");
                xmlResultado.append("<importe_resto_anios>");
                xmlResultado.append(resultado.getRetorno().getImporteAnios().getImporteRestoAnios());
                xmlResultado.append("</importe_resto_anios>");
                xmlResultado.append("</importe_anios>");
                xmlResultado.append("<tercero>");
                xmlResultado.append("<id_tercero>");
                xmlResultado.append(resultado.getRetorno().getTercero().getIdTercero());
                xmlResultado.append("</id_tercero>");
                xmlResultado.append("<tipo>");
                xmlResultado.append(resultado.getRetorno().getTercero().getTipo());
                xmlResultado.append("</tipo>");
                xmlResultado.append("<version>");
                xmlResultado.append(resultado.getRetorno().getTercero().getVersionTercero());
                xmlResultado.append("</version>");
                xmlResultado.append("</tercero>");
                xmlResultado.append("<id_territorio>");
                xmlResultado.append(resultado.getRetorno().getTerritorioHistorico().getIdTerritorio());
                xmlResultado.append("</id_territorio>");
                xmlResultado.append("<fecha_resolucion>");
                xmlResultado.append(fechaDate);
                xmlResultado.append("</fecha_resolucion>");
                xmlResultado.append("</operacion>");

                log.debug("Creamos el el objeto con la respuesta para grabarlo");
                RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                respuestaOperacionVO.setEjercicio(ejercicio);
                respuestaOperacionVO.setIdExpediente(numExpediente);
                respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.GRES);
                MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();

                try {
                    log.debug("Grabamos los datos de la respuesta en la BBDD");
                    grabarResultadoOperacion(respuestaOperacionVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-

                try {
                    log.debug("Grabamos el ID del Expediente PASIKUS en la BBDD");
                    grabarIdExpedientePasarela(numExpediente, codProcedimiento, ejercicio, resultado.getRetorno().getIdExpediente(), codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando el ID del Expediente PASIKUS en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
                if (envioEika.equalsIgnoreCase("S")) {
                    try {
                        log.debug("Grabamos el ID del Expediente EIKA en la BBDD");
                        grabarIdExpedienteEikaD(numExpediente, codProcedimiento, ejercicio, resultado.getRetorno().getExpEika(), codOrganizacion);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando el ID del Expediente EIKA en la BBDD, se continua con la operacion " + ex.getMessage());
                    }//try-catch
                }
                try {
                    log.debug("Grabamos el ID PASIKUS del Tercero en la BBDD");
                    datosTercero.setIdTerceroIkus(resultado.getRetorno().getTercero().getIdTercero());
                    meIkus01Manager.grabarIdTercero(datosTercero, codProcedimiento, codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando el ID PASIKUS del Tercero en la BBDD, se continua con la operacion " + ex.getMessage());
                }

                log.debug("Creamos el objeto para actualizar el registro de pagos");
                PagoPasarelaVO pagoPasarelaVO = new PagoPasarelaVO();
                pagoPasarelaVO.setCodOrganizacion(codOrganizacion);
                pagoPasarelaVO.setCodProcedimiento(codProcedimiento);
                pagoPasarelaVO.setEjercicio(ejercicio);
                pagoPasarelaVO.setNumExpediente(numExpediente);
                pagoPasarelaVO.setIdExpedientePasarela(resultado.getRetorno().getIdExpediente());
                try {
                    log.debug("Actualizamos el registro de pagos para anhadir el id del expediente develto por la pasarela");
                    meIkus01Manager.anhadirIdExpedientePasarela(pagoPasarelaVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos del pago en la BBDD, se continua con la operacion  " + ex.getMessage());
                }//try-catch
            } else {
                log.debug("NO existe objeto retorno");
                String tubo = MeIkus01ConfigurationParameter.getParameter(MeIkus01Constantes.TUBO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
                if (resultado.getCodigoError().contains("|")) {
                    String[] error = resultado.getCodigoError().split(tubo);
                    resultadoResolucionPasarelaPagosVO.setCodigoError(error[0]);
                    resultadoResolucionPasarelaPagosVO.setCodMensajeError(error[1]);
                } else {
                    resultadoResolucionPasarelaPagosVO.setCodigoError(resultado.getCodigoError());
                    resultadoResolucionPasarelaPagosVO.setCodMensajeError(resultado.getMensaje().getDescripcionCastellano());
                }

                //Guardamos el resultado de la operacion
                log.debug("Creamos el XML con los datos de la respuesta");
                StringBuilder xmlResultado = new StringBuilder();
                xmlResultado.append("<operacion>");
                xmlResultado.append("<cod_error>");
                xmlResultado.append(resultado.getCodigoError());
                xmlResultado.append("</cod_error>");
                xmlResultado.append("<mensaje_error>");
                xmlResultado.append(resultado.getMensaje().getDescripcionCastellano());
                xmlResultado.append("</mensaje_error>");
                xmlResultado.append("</operacion>");

                log.debug("Creamos el el objeto con la respuesta para grabarlo");
                RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                respuestaOperacionVO.setEjercicio(ejercicio);
                respuestaOperacionVO.setIdExpediente(numExpediente);
                respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.GRES);

                try {
                    log.debug("Grabamos los datos de la respuesta en la BBDD");
                    grabarResultadoOperacion(respuestaOperacionVO);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                }//try-catch
            }//if(resultado.getRetorno() != null)
        } catch (Exception_Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos", ex);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            throw ex;
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        } //try-catch
        catch (ParseException ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        } catch (DatatypeConfigurationException ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        }//try-catch
        log.debug("grabarResolucionSinExt() : END");
        return resultadoResolucionPasarelaPagosVO;
    }//grabarResolucionSinExt

    /**
     * Llama a la operacion que graba el pago en la pasarela IKUS
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    @Override
    public ResultadoResolucionPasarelaPagosVO grabarPago(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("grabarPago() : BEGIN");
        ResultadoResolucionPasarelaPagosVO resultadoResolucion = new ResultadoResolucionPasarelaPagosVO();
        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
        log.debug("Recuperamos el numero total de pagos");
        Integer numPagos = null;
        try {
            numPagos = getNumPagos(codProcedimiento, codOrganizacion);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error recuperando el numero maximo de pagos del expediente " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el numero maximo de pagos del expediente ", ex);
            meIkusException.setCodError(ERROR_RECUPERANDO_NUM_MAXIMO_PAGOS);
            throw meIkusException;
        }//try-catch

        log.debug("Recuperamos el ultimo anio de pago realizado");
        Integer pagosRealizados = null;
        try {
            pagosRealizados = Integer.valueOf(getPagosRealizados(numExpediente, codOrganizacion));
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error recuperando el ultimo anio de pago del expediente " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el ultimo anio de pago del expediente ", ex);
            meIkusException.setCodError(ERROR_RECUPERANDO_PAGOS_REALIZADOS);
            throw meIkusException;
        }//try-catch

        log.debug("Comprobamos que el indice del nuevo pago no supere el numero maximo de pagos permitidos");
        Integer nuevoPago = pagosRealizados + 1;
        log.info("Total pagos:      " + numPagos);
        log.info("Pagos realizados: " + pagosRealizados);
        log.info("Nuevo pago:       " + nuevoPago);

        if (nuevoPago <= numPagos) {
            log.debug("No se ha sobrepasado el numero maximo de pagos");
            try {
                W75BResultadoResolucionVO resultado = new W75BResultadoResolucionVO();
                log.debug("Creamos un objeto del tipo SeguridadVO para la llamada a la pasarela IKUS");
                W75BSeguridadVO seguridadVO = new W75BSeguridadVO();
                try {
                    seguridadVO = getSeguridadVO(codOrganizacion, codProcedimiento);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS ", ex);
                    meIkusException.setCodError(ERROR_CAMPOS_ACCESO_IKUS);
                    throw meIkusException;
                }//try-catch

                log.debug("Recuperamos la cantidad del pago");
                String importePago = new String();
                try {
                    importePago = getImporteConcedidoAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, nuevoPago);
                    log.debug("importePago = " + importePago);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe del pago correspondiente " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el importe del pago correspondiente ", ex);
                    meIkusException.setCodError(ERROR_RECUPERANDO_CAMPO_PAGO);
                    throw meIkusException;
                }//try-catch

                log.debug("Recuperamos el numero de expediente de la pasarela de pago");
                String numExpedientePasarela = new String();
                try {
                    numExpedientePasarela = getIdExpedientePasarela(numExpediente, codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el id de expediente generado por la pasarela " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el id de expediente generado por la pasarela ", ex);
                    meIkusException.setCodError(ERROR_RECUPERANDO_EXP_PASARELA_PAGO);
                    throw meIkusException;
                }//try-catch

                log.debug("Recuperamos el anio de la convocatoria");
                String anioConvocatoria = new String();
                try {
                    //anioConvocatoria = getAnioConvocatoria(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                    // 17/05/2016 El año de la convocatoria será el año del expediente Tarea 271063 Nota: 5. 
                    anioConvocatoria = getAnioFromNumExpediente(numExpediente);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria ", ex);
                    meIkusException.setCodError(CAMPO_ANIO_CONVOCATORIA_NO_VALIDO);
                    throw meIkusException;
                }//try-catch

                String ejercicioContable = "";
                try {
                    int ejeExp = Integer.parseInt(getAnioFromNumExpediente(numExpediente));
                    ejercicioContable = String.valueOf(ejeExp + pagosRealizados);
                    log.info("Ejercicio contable: " + ejercicioContable);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error calculando el ejercicio contable " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error calculando el ejercicio contable ", ex);
                    meIkusException.setCodError(ERROR_EJERCICIO_CONTABLE);
                    throw meIkusException;
                } catch (NumberFormatException ex) {
                    log.error("Se ha producido un error calculando el ejercicio contable " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error calculando el ejercicio contable ", ex);
                    meIkusException.setCodError(ERROR_EJERCICIO_CONTABLE);
                    throw meIkusException;
                }
                log.debug("Creamos un objeto del tipo PagoVO para la llamada a la pasarela IKUS");
                W75BPagoVO pago = new W75BPagoVO();
                pago.setNumeroPago(String.valueOf(nuevoPago));
                pago.setImporte(Double.parseDouble(importePago));

//                Si este campo se manda vacío o en S hace el envío, si se manda a N solo guarda los datos en PasIkus, para las pruebas enviarlo a N
                String envioEika = "";
//                envioEika = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA
//                        + MeIkus01Constantes.NOMBRE_MODULO + MeIkus01Constantes.BARRA + MeIkus01Constantes.ENVIO_EIKA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
                try {
                    envioEika = getValorEnvioEika(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando la fecha resolución de los campos suplementarios de trámite " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando la fecha resolución de los campos suplementarios de trámite ", ex);
                    meIkusException.setCodError(ERROR_SUPLEMENTARIO_EIKA);
                    throw meIkusException;
                }
                log.info("Ejercicio contable: " + ejercicioContable);
                log.info("Envío a EIKA: " + envioEika);

                log.debug("Creamos un objeto del tipo ResolucionVO para la llamada a la pasarela IKUS");
                W75BResolucionVO resolucion = new W75BResolucionVO();
                resolucion.setIdExpediente(numExpedientePasarela);
                resolucion.setPago(pago);
                resolucion.setAnioConvocatoria(anioConvocatoria);
                //18/05/2017 nuevo parametro obligatorio
                //anio de convocatoria se coge del año del expediente. 
                resolucion.setEjercicioContable(ejercicioContable);
                resolucion.setEnvioEIKA(envioEika);

                log.debug("Llamamos una instancia del cliente del webservice");
                ServiceOpc2 service = getWebServiceClient(codOrganizacion, codProcedimiento);

                log.info("Llamamos a la operacion de grabar pago");
                resultado = service.getServiceOpc2Port().grabarPago(seguridadVO, resolucion);
                log.info("resultado = " + resultado.getCodigoError());

                if (resultado.getRetorno() != null) {
                    log.debug("Existe objeto retorno");
                    Date fechaActual = new Date();
                    resultadoResolucion.setCodigoError(OPERACION_CORRECTA);
                    //Guardamos el resultado de la operacion
                    StringBuilder xmlResultado = new StringBuilder();
                    xmlResultado.append("<operacion>");
                    xmlResultado.append("<cod_error>");
                    xmlResultado.append(OPERACION_CORRECTA);
                    xmlResultado.append("</cod_error>");
                    xmlResultado.append("<id_pago>");
                    xmlResultado.append(resultado.getRetorno().getPago().getIdPago());
                    xmlResultado.append("</id_pago>");
                    if (envioEika.equalsIgnoreCase("S")) {
                        xmlResultado.append("<exp_eika>");
                        xmlResultado.append(resultado.getRetorno().getExpEika());
                        xmlResultado.append("</exp_eika>");
                    }
                    xmlResultado.append("<pago_importe>");
                    xmlResultado.append(resultado.getRetorno().getPago().getImporte());
                    xmlResultado.append("</pago_importe>");
                    xmlResultado.append("<numero_pago>");
                    xmlResultado.append(resultado.getRetorno().getPago().getNumeroPago());
                    xmlResultado.append("</numero_pago>");
                    xmlResultado.append("<id_expediente>");
                    xmlResultado.append(resultado.getRetorno().getNumeroExpediente());
                    xmlResultado.append("</id_expediente>");
                    xmlResultado.append("<fecha_pago>");
                    xmlResultado.append(formateador.format(fechaActual));
                    xmlResultado.append("</fecha_pago>");
                    xmlResultado.append("</operacion>");

                    RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                    respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                    respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                    respuestaOperacionVO.setEjercicio(ejercicio);
                    respuestaOperacionVO.setIdExpediente(numExpediente);
                    respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                    respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.GPAG);

                    try {
                        log.debug("Grabamos los datos de la respuesta en la BBDD");
                        grabarResultadoOperacion(respuestaOperacionVO);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                    }//try-catch
                    try {
                        log.debug("Grabamos el ID del PAGO en la BBDD");
                        grabarIdPago(numExpediente, codProcedimiento, ejercicio, resultado.getRetorno().getPago().getIdPago(), resultado.getRetorno().getPago().getNumeroPago(), codOrganizacion);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando el ID del PAGO en la BBDD, se continua con la operacion " + ex.getMessage());
                    }//try-catch
                    if (envioEika.equalsIgnoreCase("S")) {
                        try {
                            log.debug("Grabamos el ID del Expediente EIKA D en la BBDD");
                            grabarIdExpedienteEikaO(numExpediente, codProcedimiento, ejercicio, resultado.getRetorno().getExpEika(), resultado.getRetorno().getPago().getNumeroPago(), codOrganizacion);
                        } catch (MeIkus01Exception ex) {
                            log.error("Se ha producido un error grabando el ID del Expediente EIKA en la BBDD, se continua con la operacion " + ex.getMessage());
                        }//try-catch
                    }
                    log.debug("Actualizamos el registro de pagos para anhadir el id del expediente devuelto por la pasarela");
                    PagoPasarelaVO pagoPasarelaVO = new PagoPasarelaVO();
                    pagoPasarelaVO.setCodOrganizacion(codOrganizacion);
                    pagoPasarelaVO.setCodProcedimiento(codProcedimiento);
                    pagoPasarelaVO.setEjercicio(ejercicio);
                    pagoPasarelaVO.setNumExpediente(numExpediente);
                    pagoPasarelaVO.setNumPago(String.valueOf(nuevoPago));
                    try {
                        meIkus01Manager.altaExpedientePago(pagoPasarelaVO);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando los datos del pago en la BBDD " + ex.getMessage());
                    }//try-catch
                } else {
                    log.debug("NO existe objeto retorno");
                    if (resultado.getCodigoError().contains("|")) {
                        String tubo = MeIkus01ConfigurationParameter.getParameter(MeIkus01Constantes.TUBO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
                        String[] error = resultado.getCodigoError().split(tubo);
                        resultadoResolucion.setCodigoError(error[0]);
                        resultadoResolucion.setCodMensajeError(error[1]);
                    } else {
                        resultadoResolucion.setCodigoError(resultado.getCodigoError());
                        resultadoResolucion.setCodMensajeError(resultado.getMensaje().getDescripcionCastellano());
                    }

                    //Guardamos el resultado de la operacion
                    StringBuilder xmlResultado = new StringBuilder();
                    xmlResultado.append("<operacion>");
                    xmlResultado.append("<cod_error>");
                    xmlResultado.append(resultado.getCodigoError());
                    xmlResultado.append("</cod_error>");
                    xmlResultado.append("<mensaje_error>");
                    xmlResultado.append(resultado.getMensaje().getDescripcionCastellano());
                    xmlResultado.append("</mensaje_error>");
                    xmlResultado.append("</operacion>");

                    RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                    respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                    respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                    respuestaOperacionVO.setEjercicio(ejercicio);
                    respuestaOperacionVO.setIdExpediente(numExpediente);
                    respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                    respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.GPAG);

                    try {
                        log.debug("Grabamos los datos de la respuesta en la BBDD");
                        grabarResultadoOperacion(respuestaOperacionVO);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                    }//try-catch
                }//if(resultado.getRetorno() != null)
            } catch (Exception_Exception ex) {
                log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
                throw new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos", ex);
            } catch (Exception ex) {
                log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
                MeIkus01Exception meIkusException = (MeIkus01Exception) ex;
                if ((meIkusException.getCodError() == null) || ("".equalsIgnoreCase(meIkusException.getCodError()))) {
                    meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
                    meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
                }
                throw meIkusException;
            }//try-catch
        } else {
            log.debug("Se ha sobrepasado el numero maximo de pagos");
        }//if(nuevoPago > numPagos)
        log.debug("grabarPago() : END");
        return resultadoResolucion;
    }//grabarPago    

    /**
     * Llama a la operacion que graba el pago en la pasarela IKUS
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    @Override
    public ResultadoResolucionPasarelaPagosVO grabarPagoSinExt(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("grabarPago() : BEGIN");
        ResultadoResolucionPasarelaPagosVO resultadoResolucion = new ResultadoResolucionPasarelaPagosVO();
        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
        log.debug("Recuperamos el numero total de pagos");
        Integer numPagos = null;
        try {
            numPagos = getNumPagos(codProcedimiento, codOrganizacion);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error recuperando el numero maximo de pagos del expediente " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el numero maximo de pagos del expediente ", ex);
            meIkusException.setCodError(ERROR_RECUPERANDO_NUM_MAXIMO_PAGOS);
            throw meIkusException;
        }//try-catch

        log.debug("Recuperamos el ultimo anio de pago realizado");
        Integer pagosRealizados = null;
        try {
            pagosRealizados = Integer.valueOf(getPagosRealizados(numExpediente, codOrganizacion));
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error recuperando el ultimo anio de pago del expediente " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el ultimo anio de pago del expediente ", ex);
            meIkusException.setCodError(ERROR_RECUPERANDO_PAGOS_REALIZADOS);
            throw meIkusException;
        }//try-catch

        log.debug("Comprobamos que el indice del nuevo pago no supere el numero maximo de pagos permitidos");
        Integer nuevoPago = pagosRealizados + 1;
        log.info("Total pagos:      " + numPagos);
        log.info("Pagos realizados: " + pagosRealizados);
        log.info("Nuevo pago:       " + nuevoPago);

        if (nuevoPago <= numPagos) {
            log.debug("No se ha sobrepasado el numero maximo de pagos");
            try {
                W75BResultadoResolucionVO resultado = new W75BResultadoResolucionVO();
                log.debug("Creamos un objeto del tipo SeguridadVO para la llamada a la pasarela IKUS");
                W75BSeguridadVO seguridadVO = new W75BSeguridadVO();
                try {
                    seguridadVO = getSeguridadVO(codOrganizacion, codProcedimiento);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando las propiedades de autenticacion del servicio IKUS ", ex);
                    meIkusException.setCodError(ERROR_CAMPOS_ACCESO_IKUS);
                    throw meIkusException;
                }//try-catch

                log.debug("Recuperamos la cantidad del pago");
                String importePago = new String();
                try {
                    importePago = getImporteConcedidoAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, nuevoPago);
                    log.debug("importePago = " + importePago);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe del pago correspondiente " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el importe del pago correspondiente ", ex);
                    meIkusException.setCodError(ERROR_RECUPERANDO_CAMPO_PAGO);
                    throw meIkusException;
                }//try-catch

                log.debug("Recuperamos el numero de expediente de la pasarela de pago");
                String numExpedientePasarela = new String();
                try {
                    numExpedientePasarela = getIdExpedientePasarela(numExpediente, codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el id de expediente generado por la pasarela " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el id de expediente generado por la pasarela ", ex);
                    meIkusException.setCodError(ERROR_RECUPERANDO_EXP_PASARELA_PAGO);
                    throw meIkusException;
                }//try-catch

                log.debug("Recuperamos el anio de la convocatoria");
                String anioConvocatoria = new String();
                try {
                    //anioConvocatoria = getAnioConvocatoria(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                    // 17/05/2016 El año de la convocatoria será el año del expediente Tarea 271063 Nota: 5. 
                    anioConvocatoria = getAnioFromNumExpediente(numExpediente);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el campo suplementario del anio de la convocatoria ", ex);
                    meIkusException.setCodError(CAMPO_ANIO_CONVOCATORIA_NO_VALIDO);
                    throw meIkusException;
                }//try-catch

                String ejercicioContable = "";
                try {
                    int ejeExp = Integer.parseInt(getAnioFromNumExpediente(numExpediente));
                    ejercicioContable = String.valueOf(ejeExp + pagosRealizados);
                    log.info("Ejercicio contable: " + ejercicioContable);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error calculando el ejercicio contable " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error calculando el ejercicio contable ", ex);
                    meIkusException.setCodError(ERROR_EJERCICIO_CONTABLE);
                    throw meIkusException;
                } catch (NumberFormatException ex) {
                    log.error("Se ha producido un error calculando el ejercicio contable " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error calculando el ejercicio contable ", ex);
                    meIkusException.setCodError(ERROR_EJERCICIO_CONTABLE);
                    throw meIkusException;
                }
                log.debug("Creamos un objeto del tipo PagoVO para la llamada a la pasarela IKUS");
                W75BPagoVO pago = new W75BPagoVO();
                pago.setNumeroPago(String.valueOf(nuevoPago));
                pago.setImporte(Double.parseDouble(importePago));

//                Si este campo se manda vacío o en S hace el envío, si se manda a N solo guarda los datos en PasIkus, para las pruebas enviarlo a N
                String envioEika = "";
//                envioEika = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA
//                        + MeIkus01Constantes.NOMBRE_MODULO + MeIkus01Constantes.BARRA + MeIkus01Constantes.ENVIO_EIKA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
                try {
                    envioEika = getValorEnvioEika(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando la fecha resolución de los campos suplementarios de trámite " + ex.getMessage());
                    MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error recuperando el valor del envío a EIKA, es necesario indicar el valor SI o NO "
                            + "en \"¿Enviar los datos a EIKA\"", ex);
                    meIkusException.setCodError(ERROR_SUPLEMENTARIO_EIKA);
                    throw meIkusException;
                }
                log.info("Ejercicio contable: " + ejercicioContable);
                log.info("Envío a EIKA: " + envioEika);

                log.debug("Creamos un objeto del tipo ResolucionVO para la llamada a la pasarela IKUS");
                W75BResolucionVO resolucion = new W75BResolucionVO();
                resolucion.setIdExpediente(numExpedientePasarela);
                resolucion.setPago(pago);
                resolucion.setAnioConvocatoria(anioConvocatoria);
                //18/05/2017 nuevo parametro obligatorio
                //anio de convocatoria se coge del año del expediente. 
                resolucion.setEjercicioContable(ejercicioContable);
                resolucion.setEnvioEIKA(envioEika);

                log.debug("Llamamos una instancia del cliente del webservice con los parámetros:"
                        + "\ncodOrganizacion = " + codOrganizacion
                        + "\ncodProcedimiento = " + codProcedimiento);
                ServiceOpc2 service = getWebServiceClient(codOrganizacion, codProcedimiento);

                log.info("Llamamos a la operacion de grabar pago con los parámetros:\nseguridadVO = "
                        + "\n\tusuario = " + seguridadVO.getUsuario()
                        + "\n\tcontraseña = " + seguridadVO.getContrasena()
                        + "\n\tperfil = " + seguridadVO.getPerfil()
                        + "\nresolucion = "
                        + "\n\tanio = " + resolucion.getAnioConvocatoria()
                        + "\n\tconcepto resumido = " + resolucion.getConceptoResumido()
                        + "\n\tejercicio contable = " + resolucion.getEjercicioContable()
                        + "\n\tenvio EIKA = " + resolucion.getEnvioEIKA()
                        + "\n\tExp EIKA = " + resolucion.getExpEika()
                        + "\n\tId Aplicacion = " + resolucion.getIdAplicacion()
                        + "\n\tId Aplicacion definitiva = " + resolucion.getIdAplicacionDefinitiva()
                        + "\n\tId convocatoria = " + resolucion.getIdConvocatoria()
                        + "\n\tId disposicion = " + resolucion.getIdDisposicion()
                        + "\n\tId expediente = " + resolucion.getIdExpediente()
                        + "\n\tnumero expediente = " + resolucion.getNumeroExpediente()
                        + "\n\tfecha resolucion = " + resolucion.getFechaResolucion()
                        + "\n\tforma juridica = " + resolucion.getFormaJuridica()
                        + "\n\tImporte anios = " + resolucion.getImporteAnios()
                        + "\n\tImporte concedido = " + resolucion.getImporteConcedido()
                        + "\n\tlinea ayuda = " + resolucion.getLineaAyuda()
                        + "\n\tpago = "
                        + "\n\t\tid pago = " + resolucion.getPago().getIdPago()
                        + "\n\t\tnumero pago = " + resolucion.getPago().getNumeroPago()
                        + "\n\t\ttipo documento = " + resolucion.getPago().getTipoDocumento()
                        + "\n\t\timporte = " + resolucion.getPago().getImporte()
                        + "\n\t\tjustificacion obligatoria = " + resolucion.getPago().isJustificacionObligatoria()
                        + "\n\ttercero = " + resolucion.getTercero()
                        + "\n\tterritorio historico = " + resolucion.getTerritorioHistorico());

                resultado = service.getServiceOpc2Port().grabarPago(seguridadVO, resolucion);
                log.info("resultado = " + resultado.getCodigoError());

                if (resultado.getRetorno() != null) {
                    log.debug("Existe objeto retorno");
                    Date fechaActual = new Date();
                    resultadoResolucion.setCodigoError(OPERACION_CORRECTA);
                    //Guardamos el resultado de la operacion
                    StringBuilder xmlResultado = new StringBuilder();
                    xmlResultado.append("<operacion>");
                    xmlResultado.append("<cod_error>");
                    xmlResultado.append(OPERACION_CORRECTA);
                    xmlResultado.append("</cod_error>");
                    xmlResultado.append("<id_pago>");
                    xmlResultado.append(resultado.getRetorno().getPago().getIdPago());
                    xmlResultado.append("</id_pago>");
                    if (envioEika.equalsIgnoreCase("S")) {
                        xmlResultado.append("<exp_eika>");
                        xmlResultado.append(resultado.getRetorno().getExpEika());
                        xmlResultado.append("</exp_eika>");
                    }
                    xmlResultado.append("<pago_importe>");
                    xmlResultado.append(resultado.getRetorno().getPago().getImporte());
                    xmlResultado.append("</pago_importe>");
                    xmlResultado.append("<numero_pago>");
                    xmlResultado.append(resultado.getRetorno().getPago().getNumeroPago());
                    xmlResultado.append("</numero_pago>");
                    xmlResultado.append("<id_expediente>");
                    xmlResultado.append(resultado.getRetorno().getNumeroExpediente());
                    xmlResultado.append("</id_expediente>");
                    xmlResultado.append("<fecha_pago>");
                    xmlResultado.append(formateador.format(fechaActual));
                    xmlResultado.append("</fecha_pago>");
                    xmlResultado.append("</operacion>");

                    RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                    respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                    respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                    respuestaOperacionVO.setEjercicio(ejercicio);
                    respuestaOperacionVO.setIdExpediente(numExpediente);
                    respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                    respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.GPAG);

                    try {
                        log.debug("Grabamos los datos de la respuesta en la BBDD");
                        grabarResultadoOperacion(respuestaOperacionVO);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                    }//try-catch
                    try {
                        log.debug("Grabamos el ID del PAGO en la BBDD");
                        grabarIdPago(numExpediente, codProcedimiento, ejercicio, resultado.getRetorno().getPago().getIdPago(), resultado.getRetorno().getPago().getNumeroPago(), codOrganizacion);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando el ID del PAGO en la BBDD, se continua con la operacion " + ex.getMessage());
                    }//try-catch
                    if (envioEika.equalsIgnoreCase("S")) {
                        try {
                            log.debug("Grabamos el ID del Expediente EIKA D en la BBDD");
                            grabarIdExpedienteEikaO(numExpediente, codProcedimiento, ejercicio, resultado.getRetorno().getExpEika(), resultado.getRetorno().getPago().getNumeroPago(), codOrganizacion);
                        } catch (MeIkus01Exception ex) {
                            log.error("Se ha producido un error grabando el ID del Expediente EIKA en la BBDD, se continua con la operacion " + ex.getMessage());
                        }//try-catch
                    }
                    log.debug("Actualizamos el registro de pagos para anhadir el id del expediente devuelto por la pasarela");
                    PagoPasarelaVO pagoPasarelaVO = new PagoPasarelaVO();
                    pagoPasarelaVO.setCodOrganizacion(codOrganizacion);
                    pagoPasarelaVO.setCodProcedimiento(codProcedimiento);
                    pagoPasarelaVO.setEjercicio(ejercicio);
                    pagoPasarelaVO.setNumExpediente(numExpediente);
                    pagoPasarelaVO.setNumPago(String.valueOf(nuevoPago));
                    try {
                        meIkus01Manager.altaExpedientePago(pagoPasarelaVO);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando los datos del pago en la BBDD " + ex.getMessage());
                    }//try-catch
                } else {
                    log.debug("NO existe objeto retorno");
                    if (resultado.getCodigoError().contains("|")) {
                        String tubo = MeIkus01ConfigurationParameter.getParameter(MeIkus01Constantes.TUBO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
                        String[] error = resultado.getCodigoError().split(tubo);
                        resultadoResolucion.setCodigoError(error[0]);
                        resultadoResolucion.setCodMensajeError(error[1]);
                    } else {
                        resultadoResolucion.setCodigoError(resultado.getCodigoError());
                        resultadoResolucion.setCodMensajeError(resultado.getMensaje().getDescripcionCastellano());
                    }

                    //Guardamos el resultado de la operacion
                    StringBuilder xmlResultado = new StringBuilder();
                    xmlResultado.append("<operacion>");
                    xmlResultado.append("<cod_error>");
                    xmlResultado.append(resultado.getCodigoError());
                    xmlResultado.append("</cod_error>");
                    xmlResultado.append("<mensaje_error>");
                    xmlResultado.append(resultado.getMensaje().getDescripcionCastellano());
                    xmlResultado.append("</mensaje_error>");
                    xmlResultado.append("</operacion>");

                    RespuestaOperacionVO respuestaOperacionVO = new RespuestaOperacionVO();
                    respuestaOperacionVO.setCodOrganizacion(codOrganizacion);
                    respuestaOperacionVO.setCodProcedimiento(codProcedimiento);
                    respuestaOperacionVO.setEjercicio(ejercicio);
                    respuestaOperacionVO.setIdExpediente(numExpediente);
                    respuestaOperacionVO.setInfoAdicional(xmlResultado.toString());
                    respuestaOperacionVO.setOperacion(OperacionesPasarelaEnum.GPAG);

                    try {
                        log.debug("Grabamos los datos de la respuesta en la BBDD");
                        grabarResultadoOperacion(respuestaOperacionVO);
                    } catch (MeIkus01Exception ex) {
                        log.error("Se ha producido un error grabando los datos de la respuesta del servicio en la BBDD, se continua con la operacion " + ex.getMessage());
                    }//try-catch
                }//if(resultado.getRetorno() != null)
            } catch (Exception_Exception ex) {
                log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
                throw new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos", ex);
            } catch (Exception ex) {
                log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
                MeIkus01Exception meIkusException = null;
                if (ex instanceof MeIkus01Exception) {
                    meIkusException = (MeIkus01Exception) ex;
                } else {
                    meIkusException = new MeIkus01Exception(ex.getMessage());
                }
                if ((meIkusException.getCodError() == null) || ("".equalsIgnoreCase(meIkusException.getCodError()))) {
                    meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
                }
                meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
                throw meIkusException;
            }//try-catch
        } else {
            log.debug("Se ha sobrepasado el numero maximo de pagos");
        }//if(nuevoPago > numPagos)
        log.debug("grabarPagoSinExt() : END");
        return resultadoResolucion;
    }//grabarPagoSinExt

    /**
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private W75BResultadoTerceroIkusVO buscarTercero(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        log.debug("buscarTercero() : BEGIN");
        W75BResultadoTerceroIkusVO resultado = new W75BResultadoTerceroIkusVO();
        try {
            log.debug("Recuperamos el tercero del expediente");
            TerceroPasarela interesado = getDatosSuplementariosTercero(numExpediente, codProcedimiento, ejercicio, codOrganizacion);
            if (interesado != null) {
                log.debug("Creamos un objeto del tipo SeguridadVO para la llamada a la pasarela IKUS");
                W75BSeguridadVO seguridadVO = getSeguridadVO(codOrganizacion, codProcedimiento);

                log.debug("Creamos un objeto del tipo TerceroIkusVO para la llamada a la pasarela IKUS");
                W75BTerceroIkusVO terceroIkus = new W75BTerceroIkusVO();
                terceroIkus.setCodigo(interesado.getDocumento());
                terceroIkus.setNombre(interesado.getNombre());
                terceroIkus.setTipo(interesado.getTipoTercero());
                terceroIkus.setVersionTercero(interesado.getVersion());

                log.debug("Llamamos una instancia del cliente del webservice");
                ServiceOpc2 service = getWebServiceClient(codOrganizacion, codProcedimiento);

                log.debug("Llamamos a la operacion de grabar resolucion");
                resultado = service.getServiceOpc2Port().recuperarTerceroIkus(seguridadVO, terceroIkus);
            }//if(interesado != null)
        } catch (Exception_Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos", ex);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error en la llamada al servicio web de la pasarela de pagos " + ex.getMessage());
            MeIkus01Exception meIkusException = new MeIkus01Exception("Se ha producido un error en la llamada al servicio web de la pasarela de pagos ");
            meIkusException.setCodError(ERROR_LLAMANDO_PASARELA);
            throw meIkusException;
        }//try-catch
        log.debug("buscarTercero() : END");
        return resultado;
    }//buscarTercero

    /**
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    public ResultadoReservaPasarelaPagosVO eliminarReserva(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }//eliminarReserva

    /**
     * Recupera los datos suplementarios del interesado del expediente
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private TerceroPasarela getDatosSuplementariosTercero(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        log.debug("getDatosSuplementariosTercero() : BEGIN");
        TerceroPasarela datosTercero = new TerceroPasarela();
        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();

        try {
            log.debug("Recuperamos el tercero del expediente");
            InteresadoExpedienteModuloIntegracionVO interesado = getInteresado(codOrganizacion, numExpediente, codProcedimiento, ejercicio);
            if (interesado != null) {
                datosTercero.setCodTercero(interesado.getCodigoTercero());
                String nombreCompleto = "";
                if (interesado.getApellido1() != null) {
                    nombreCompleto += interesado.getApellido1();
                }
                if (interesado.getApellido2() != null) {
                    if (!nombreCompleto.isEmpty()) {
                        nombreCompleto += " " + interesado.getApellido2();
                    } else {
                        nombreCompleto += interesado.getApellido2();
                    }
                }
                if (nombreCompleto.isEmpty() && interesado.getNombre() != null) {
                    nombreCompleto += interesado.getNombre();
                }
                else {
                    nombreCompleto += ", " + interesado.getNombre();
                }
                log.info("nombreCompleto = " + nombreCompleto);
                datosTercero.setNombre(nombreCompleto);
                datosTercero.setDocumento(getCodigoIkusFromDocumento(interesado.getDocumento()));
//                datosTercero.setTipoTercero(getCampoSuplementarioTipoTerceroIkus(codOrganizacion, interesado));
                switch (interesado.getTipoDocumento()) {
                    case 4:
                        datosTercero.setTipoTercero("1");
                        break;
                    case 1:
                    case 3:
                        datosTercero.setTipoTercero("2");
                        break;
                    default:
                        throw new MeIkus01Exception("El tipo de documento del tercero no es válido");
                }
                datosTercero.setVersion(getCampoSuplementarioVersionTerceroIkus(codOrganizacion, interesado));
//                datosTercero.setCodTerritorioHistorico(getTerritorioHistorico(interesado, codOrganizacion));
                datosTercero.setFormaJuridica(getCampoSuplementarioFormaJuridicaIkus(codOrganizacion, interesado));

//                String codProv = meIkus01Manager.getCodigoProvincia(String.valueOf(interesado.getCodigoTercero()), numExpediente, codOrganizacion);
                String codPostal = meIkus01Manager.getCodigoPostal(String.valueOf(interesado.getCodigoTercero()), numExpediente, codOrganizacion);
                datosTercero.setCodTerritorioHistorico(getConversionTerritorioHistorico(codOrganizacion, codPostal.substring(0, 2)));

                log.info("Cod 3º     : " + datosTercero.getCodTercero());
                log.info("Nombre     : " + datosTercero.getNombre());
                log.info("Documento  : " + datosTercero.getDocumento());
                log.info("Tipo 3º    : " + datosTercero.getTipoTercero());
                log.info("Versión    : " + datosTercero.getVersion());
                log.info("F. Jurídica: " + datosTercero.getFormaJuridica());
                log.info("TT.HH.     : " + datosTercero.getCodTerritorioHistorico());
            }//if(interesado != null)
            else {
                final MeIkus01Exception meIkus01Exception = new MeIkus01Exception("No hay datos de tercero. Son necesarios para poder"
                        + " avanzar el expediente");
                throw meIkus01Exception;
            }
        } catch (MeIkus01Exception ex) {
            throw ex;
        }//try-catch
        log.debug("getDatosSuplementariosTercero() : END");

        return datosTercero;
    }//getDatosSuplementariosTercero

    private String getCodigoIkusFromDocumento(String documento) throws MeIkus01Exception {
        log.debug("getCodigoIkusFromDocumento() : BEGIN");
        Integer tamanhoCodigoIkus = 10;
        Integer tamanhoCodigo = documento.length();
        while (tamanhoCodigo < tamanhoCodigoIkus) {
            documento = "0" + documento;
            tamanhoCodigo = documento.length();
        }
        log.debug("getCodigoIkusFromDocumento() : END");
        return documento;
    }

    /**
     * Recupera el territorio historico de un tercero por la provincia de su
     * direccion
     *
     * @param interesado
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getTerritorioHistorico(InteresadoExpedienteModuloIntegracionVO interesado, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("getTerritorioHistorico() : BEGIN");
        String territorioHistorico = new String();
        try {
//            String codProvincia = interesado.getDomicilios().get(0).getIdProvincia();
            String codPostal = interesado.getDomicilios().get(0).getCodigoPostal();
//            if (!codProvincia.equalsIgnoreCase(null) && !"".equalsIgnoreCase(codProvincia)) {
            if (!codPostal.equalsIgnoreCase(null) && !"".equalsIgnoreCase(codPostal)) {
                territorioHistorico = getConversionTerritorioHistorico(codOrganizacion, codPostal.substring(0, 2));
                if (territorioHistorico == null || "".equalsIgnoreCase(territorioHistorico)) {
                    log.error("Se ha producido un error recuperando la informacion del tercero para recuperar su territorio ");
                    throw new MeIkus01Exception("Se ha producido un error recuperando la informacion del tercero para recuperar su territorio ");
                }//if(territorioHistorico == null || "".equalsIgnoreCase(territorioHistorico))
            }//if(!codProvincia.equalsIgnoreCase(null) && !"".equalsIgnoreCase(codProvincia))   
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error recuperando la informacion del tercero para recuperar su territorio " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error recuperando la informacion del tercero para recuperar su territorio ", ex);
        }//try-catch
        log.info("getTerritorioHistorico() : END ==> " + territorioHistorico);
        return territorioHistorico;
    }//getTerritorioHistorico

    /**
     * Introduce los posibles codigos de error de la pasarela en el array
     * disponible
     *
     * @param codigosError
     * @throws MeIkus01Exception
     */
    public void setCodigosError(ArrayList<String> codigosError) throws MeIkus01Exception {
        log.debug("setCodigosError() : BEGIN");
        PasarelaIkus.codigosError.addAll(codigosError);
        log.debug("setCodigosError() : END");
    }//setCodigosError

    /**
     * Recupera una instancia del cliente de la pasarela IKUS
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     * @throws
     * es.altia.flexia.integracion.moduloexterno.meikus.exception.MeIkus01Exception
     */
    private ServiceOpc2 getWebServiceClient(Integer codOrganizacion, String codProcedimiento) throws MeIkus01Exception {
        log.debug("getWebServiceClient() : BEGIN");
        if (instance == null) {
            log.debug("No existe instancia, creamos una nueva");
            URL url = null;
            QName qName = null;
            log.debug("Recuperamos la URL del servicio web");
            try {
                url = new URL(getURL(codOrganizacion, codProcedimiento));
            } catch (MalformedURLException ex) {
                log.error("Se ha producido un error parseando la URL del servicio web " + ex.getMessage());
                throw new MeIkus01Exception("Se ha producido un error parseando la URL del servicio web", ex);
            }
            //try-catch
            //try-catch

            log.debug("Recuperamos el service name del servicio web");
            try {
                qName = new QName(getServiceNameURL(codOrganizacion, codProcedimiento), getServiceNameName(codOrganizacion, codProcedimiento));
            } catch (Exception ex) {
                log.error("Se ha producido un error recuperando el qName del servicio web " + ex.getMessage());
                throw new MeIkus01Exception("Se ha producido un error recuperando el qName del servicio web ", ex);
            }//try-catch
            log.debug("Creamos la instancia del cliente");
            instance = new ServiceOpc2(url, qName);
        } else {
            log.debug("Existe una instancia del cliente del servicio");
        }//if(instance == null)
        log.debug("getWebServiceClient() : END");
        return instance;
    }//getWebServiceClient

    /**
     * Recupera del fichero de propiedades del modulo la URL del servicio de
     * pagos electronicos
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     */
    private String getURL(Integer codOrganizacion, String codProcedimiento) {
        log.debug("getURL() : BEGIN");
        String url = "";
        log.debug("Recuperamos la URL para el servicio web");
        url = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                + codProcedimiento + MeIkus01Constantes.BARRA + URL_WEBSERVICE, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
        log.debug("URL webservice = " + url);
        log.debug("getURL() : END");
        return url;
    }//getURL

    /**
     * Recupera del fichero de propiedades del modulo la url del service name
     * del servicio de pagos electronicos
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     */
    private String getServiceNameURL(Integer codOrganizacion, String codProcedimiento) {
        log.debug("getServiceNameURL() : BEGIN");
        String serviceNameURL = "";
        log.debug("Recuperamos la url del service name para el servicio web");
        serviceNameURL = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                + codProcedimiento + MeIkus01Constantes.BARRA + SERVICE_NAME_URL, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
        log.debug("URL service name = " + serviceNameURL);
        log.debug("getServiceNameURL() : END");
        return serviceNameURL;
    }//getServiceNameURL

    /**
     * Recupera del fichero de propiedades del modulo el nombre del service name
     * del servicio de pagos electronicos
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     */
    private String getServiceNameName(Integer codOrganizacion, String codProcedimiento) {
        log.debug("getServiceNameURL() : BEGIN");
        String serviceNameName = "";
        log.debug("Recuperamos el nombre del service name para el servicio web");
        serviceNameName = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                + codProcedimiento + MeIkus01Constantes.BARRA + SERVICE_NAME_NAME, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
        log.debug("service name name = " + serviceNameName);
        log.debug("getServiceNameURL() : END");
        return serviceNameName;
    }//getServiceNameURL

    /**
     * Creamos un objeto con del tipo W75BSeguridadVO con los parametros de
     * seguridad del servicio web
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     * @throws MeIkus01Exception
     */
    private W75BSeguridadVO getSeguridadVO(Integer codOrganizacion, String codProcedimiento) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getSeguridadVO() : BEGIN");
        }
        W75BSeguridadVO seguridadVO = new W75BSeguridadVO();
        String usuario = "";
        String password = "";
        try {
            usuario = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + codProcedimiento + MeIkus01Constantes.BARRA + WEBSERVICE_USER, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            password = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + codProcedimiento + MeIkus01Constantes.BARRA + WEBSERVICE_PASS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los datos de conexion al webservice " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error recuperando los datos de conexion al webservice", ex);
        }//try-catch

        if ((usuario != null && !"".equalsIgnoreCase(usuario))
                && (password != null && !"".equalsIgnoreCase(password))) {
            seguridadVO.setUsuario(usuario);
            seguridadVO.setContrasena(password);
        }/*if((usuario != null && !"".equalsIgnoreCase(usuario))
                &&(password != null && !"".equalsIgnoreCase(password)))*/
        if (log.isDebugEnabled()) {
            log.debug("getSeguridadVO() : END");
        }
        return seguridadVO;
    }//getSeguridadVO

    /**
     * Recupera los importes para los anios y crea un objeto W75BImporteAniosVO
     * para la llamada a la pasarela de pagos
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @param numPagos
     * @return
     * @throws MeIkus01Exception
     */
    private W75BImporteAniosVO getImportesConcedidosAnios(String numExpediente, String codProcedimiento,
            String ejercicio, Integer codOrganizacion, Integer numPagos) throws MeIkus01Exception {
        W75BImporteAniosVO importeAniosVO = new W75BImporteAniosVO();
        if (log.isDebugEnabled()) {
            log.debug("getImportesConcedidosAnios() : BEGIN");
        }
        if (log.isDebugEnabled()) {
            log.debug("Recorremos el numero de pagos recogiendo los datos de los campos de los importes");
        }
        for (int x = 1; x <= numPagos; x++) {
            switch (x) {
                case 1:
                    try {
                    importeAniosVO.setImporteAnio1(Double.parseDouble(getImporteConcedidoAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el primer campo de importe anio");
                }//try-catch
                break;
                case 2:
                    try {
                    importeAniosVO.setImporteAnio2(Double.parseDouble(getImporteConcedidoAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el segundo campo de importe anio");
                }//try-catch
                break;
                case 3:
                    try {
                    importeAniosVO.setImporteAnio3(Double.parseDouble(getImporteConcedidoAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el tercer campo de importe anio");
                }//try-catch
                break;
                case 4:
                    try {
                    importeAniosVO.setImporteAnio4(Double.parseDouble(getImporteConcedidoAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el cuarto campo de importe anio");
                }//try-catch
                break;
                case 5:
                    try {
                    importeAniosVO.setImporteAnio5(Double.parseDouble(getImporteConcedidoAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el cuerto campo de importe anio");
                }//try-catch
                break;
                default:
                    break;
            }
        }//for(int x=0; x<=numPagos; x++)
        if (numPagos > 5) {
            try {
                importeAniosVO.setImporteRestoAnios(Double.parseDouble(getCampoImporteRestoAniosConcedido(numExpediente, codProcedimiento, ejercicio, codOrganizacion)));
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el importe para el importe del resto de anios");
            }//try-catch
        }//if(numPagos > 5)
        if (log.isDebugEnabled()) {
            log.debug("getImportesConcedidosAnios() : END");
        }
        return importeAniosVO;
    }//getImportesAnios

    /**
     * Recupera la cantidad del campo suplementario del importe del resto de
     * anios
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getCampoImporteRestoAniosConcedido(String numExpediente, String codProcedimiento,
            String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoImporteRestoAniosConcedido() : BEGIN");
        }
        String importeAnios = "";
        String tipoCampo = "";
        String valor = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            importeAnios = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_IMPORTE_RESTO_ANIOS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_IMPORTE_RESTO_ANIOS, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    importeAnios, Integer.parseInt(tipoCampo));

            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe del resto de anios " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe del resto de anios " + ex.getMessage());
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("getCampoImporteRestoAniosConcedido() : END");
        }
        return valor;
    }//getCampoImporteRestoAniosConcedido

    /**
     * Recupera el campo suplementario de la linea de ayuda
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getCampoSuplementarioLineaAyuda(String numExpediente, String codProcedimiento,
            String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioLineaAyuda() : BEGIN");
        }
        String lineaAyuda = "";
        String tipoCampo = "";
        String valor = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            lineaAyuda = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_COD_LINEA_AYUDA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_COD_LINEA_AYUDA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    lineaAyuda, Integer.parseInt(tipoCampo));

            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario de la linea de ayuda " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario de la linea de ayuda " + ex.getMessage());
        } catch (NumberFormatException ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario de la linea de ayuda " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario de la linea de ayuda " + ex.getMessage());
        } //try-catch
        //try-catch
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioLineaAyuda() : END");
        }
        return valor;
    }//getCampoSuplementarioLineaAyuda

    /**
     * Recupera el campo suplementario del importe concedido
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getCampoSuplementarioImporteConcedido(String numExpediente, String codProcedimiento,
            String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioImporteConcedido() : BEGIN");
        }
        String importeConcedido = "";
        String tipoCampo = "";
        String valor = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            importeConcedido = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_IMPORTE_CONCEDIDO_TOTAL, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_IMPORTE_CONCEDIDO_TOTAL, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    importeConcedido, Integer.parseInt(tipoCampo));

            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe concedido " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe concedido " + ex.getMessage());
        } catch (NumberFormatException ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe concedido " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe concedido " + ex.getMessage());
        } //try-catch
        //try-catch
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioImporteConcedido() : END");
        }
        return valor;
    }//getCampoSuplementarioImporteConcedido

    /**
     * Recupera el interesado del expediente con el rol definido en el fichero
     * de configuracion del modulo
     *
     * @param codOrganizacion
     * @param expediente
     * @param codProcedimiento
     * @param ejercicio
     * @return
     * @throws MeIkus01Exception
     */
    private InteresadoExpedienteModuloIntegracionVO getInteresado(Integer codOrganizacion, String expediente, String codProcedimiento,
            String ejercicio) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getInteresado() : BEGIN");
        }
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        InteresadoExpedienteModuloIntegracionVO interesado = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Recuperamos el codigo del rol del beneficiario");
            }
            Integer rolBeneficiario = Integer.valueOf(getRolBeneficiario(codProcedimiento, codOrganizacion));

            SalidaIntegracionVO salida = el.getExpediente(String.valueOf(codOrganizacion), expediente, codProcedimiento, ejercicio);
            ExpedienteModuloIntegracionVO expedienteVO = salida.getExpediente();
            ArrayList<InteresadoExpedienteModuloIntegracionVO> interesadosExpediente = expedienteVO.getInteresados();
            if (log.isDebugEnabled()) {
                log.debug("Comprobamos que nos devuelva interesados");
            }
            if (!interesadosExpediente.isEmpty()) {
                for (InteresadoExpedienteModuloIntegracionVO interesadoExpediente : interesadosExpediente) {
                    if (rolBeneficiario.equals(interesadoExpediente.getCodigoRol())) {
                        if (log.isDebugEnabled()) {
                            log.info("El rol del interesado coincide con el del beneficiario");
                        }
                        interesado = new InteresadoExpedienteModuloIntegracionVO();
                        interesado = interesadoExpediente;
                    }//if(rolBeneficiario.equals(interesadoExpediente.getCodigoRol()))
                }//for(InteresadoExpedienteModuloIntegracionVO interesadoExpediente : interesadosExpediente)
                if (interesado == null) {
                    log.error("No se ha encontrado ningun interesado con el rol de beneficiario");
                    throw new MeIkus01Exception("No se ha encontrado ningun interesado con el rol de beneficiario");
                }//if(interesado == null)
            } else {
                log.error("El expediente no tiene interesados");
                throw new MeIkus01Exception("El expediente no tiene interesados");
            }//if(interesadosExpediente.size() > 0)
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error recuperando los terceros del expediente " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error recuperando los terceros del expediente ", ex);
        } catch (NumberFormatException ex) {
            log.error("Se ha producido un error recuperando los terceros del expediente " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error recuperando los terceros del expediente ", ex);
        } //try-catch
        //try-catch
        if (log.isDebugEnabled()) {
            log.debug("getInteresado() : END");
        }
        return interesado;
    }//getInteresado

    /**
     * Recupera el campo suplementario del tercero que contiene la version del
     * tercero en el IKUS
     *
     * @param codOrganizacion
     * @param interesado
     * @return
     * @throws MeIkus01Exception
     */
    private String getCampoSuplementarioVersionTerceroIkus(Integer codOrganizacion, InteresadoExpedienteModuloIntegracionVO interesado)
            throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioVersionTerceroIkus() : BEGIN");
        }
        String campoTerceroIkus = "";
        String tipoCampo = "";
        String valor = "";
        if (interesado == null) {
            log.error("No existen datos de tercero, son necesarios para las operaciones Pasikus");
            throw new MeIkus01Exception("No existen datos de tercero para dicho expediente, es necesario cumplimentarlos para "
                    + "cualquier operación relacionada con la pasarela de pagos Pasikus");
        }
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            campoTerceroIkus = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + CAMPO_SUPLEMENTARIO_IKUS_TER_VER, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
            log.info("campoTerceroIkus = " + campoTerceroIkus);
            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + CAMPO_SUPLEMENTARIO_IKUS_TER_VER, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
            log.info("tipoCampo = " + tipoCampo);
            SalidaIntegracionVO salidaCampoSuplementarioTercero
                    = el.getCampoSuplementarioTercero(String.valueOf(codOrganizacion), interesado.getCodigoTercero(),
                            campoTerceroIkus, Integer.parseInt(tipoCampo));
            log.info("salidaCampoSuplementarioTercero = " + salidaCampoSuplementarioTercero);
            log.info("salidaCampoSuplementarioTercero.getCampoSuplementarioTercero() = "
                    + salidaCampoSuplementarioTercero.getCampoSuplementarioTercero());

            log.info("Version Tercero Ikus : " + salidaCampoSuplementarioTercero.getCampoSuplementarioTercero().getValorDesplegable());

            valor = MeIkus01Utils.getValorCampoSuplementarioTercero(salidaCampoSuplementarioTercero.getCampoSuplementarioTercero());
        } catch (NumberFormatException ex) {
            final MeIkus01Exception meIkus01Exception = new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario de la version del tercero IKUS");
            meIkus01Exception.setCodError(ERROR_DATOS_SUPLEMENTARIO_TERCERO);
            throw meIkus01Exception;
        } catch (final NullPointerException ex) {
            log.error("Se ha producido una excepción al recuperar los datos de tercero: " + ex.getMessage());
            final MeIkus01Exception meIkus01Exception = new MeIkus01Exception("Es necesario indicar la versión en datos de tercero");
            throw meIkus01Exception;
        }
        //try-catch
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioVersionTerceroIkus() : END");
        }
        return valor;
    }//getCampoSuplementarioVersionTerceroIkus

    /**
     * Recupera el campo suplementario que contiene el tipo de tercero del IKUS
     *
     * @param codOrganizacion
     * @param interesado
     * @return
     * @throws MeIkus01Exception
     */
    private String getCampoSuplementarioTipoTerceroIkus(Integer codOrganizacion, InteresadoExpedienteModuloIntegracionVO interesado)
            throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioTipoTerceroIkus() : BEGIN");
        }
        String campoTerceroIkus = "";
        String tipoCampo = "";
        String valor = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            campoTerceroIkus = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + CAMPO_SUPLEMENTARIO_IKUS_TER_TIP, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + CAMPO_SUPLEMENTARIO_IKUS_TER_TIP, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salidaCampoSuplementarioTercero
                    = el.getCampoSuplementarioTercero(String.valueOf(codOrganizacion), interesado.getCodigoTercero(), campoTerceroIkus, Integer.parseInt(tipoCampo));

            log.debug("String.valueOf(codOrganizacion) : " + String.valueOf(codOrganizacion));
            log.debug("interesado.getCodigoTercero() : " + interesado.getCodigoTercero());
            log.debug("campoTerceroIkus : " + campoTerceroIkus);
            log.debug("Integer.valueOf(tipoCampo) : " + Integer.valueOf(tipoCampo));

            log.info("Tipo Tercero Ikus : " + salidaCampoSuplementarioTercero.getCampoSuplementarioTercero().getValorTexto());

            valor = MeIkus01Utils.getValorCampoSuplementarioTercero(salidaCampoSuplementarioTercero.getCampoSuplementarioTercero());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del tipo del tercero IKUS " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del tipo del tercero IKUS " + ex.getMessage());
        } catch (NumberFormatException ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del tipo del tercero IKUS " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del tipo del tercero IKUS " + ex.getMessage());
        } //try-catch
        //try-catch
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioTipoTerceroIkus() : END");
        }
        return valor;
    }//getCampoSuplementarioCodTerceroIkus

    /**
     * Recupera el campo suplementario que contiene la forma juridica del
     * tercero del IKUS
     *
     * @param codOrganizacion
     * @param interesado
     * @return
     * @throws MeIkus01Exception
     */
    public String getCampoSuplementarioFormaJuridicaIkus(Integer codOrganizacion, InteresadoExpedienteModuloIntegracionVO interesado)
            throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioFormaJuridicaIkus() : BEGIN");
        }
        String campoFormaJuridicaIkus = "";
        String tipoCampo = "";
        String valor = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            campoFormaJuridicaIkus = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + CAMPO_SUPLEMENTARIO_IKUS_TER_F_JUR, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
            log.info("campoFormaJuridicaIkus = " + campoFormaJuridicaIkus);
            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + CAMPO_SUPLEMENTARIO_IKUS_TER_F_JUR, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
            log.info("tipoCampo = " + tipoCampo);
            SalidaIntegracionVO salidaCampoSuplementarioTercero
                    = el.getCampoSuplementarioTercero(String.valueOf(codOrganizacion), interesado.getCodigoTercero(), campoFormaJuridicaIkus, Integer.parseInt(tipoCampo));
            log.info("salidaCampoSuplementarioTercero = " + salidaCampoSuplementarioTercero);
            log.info("salidaCampoSuplementarioTercero.getCampoSuplementarioTercero() = " + salidaCampoSuplementarioTercero.getCampoSuplementarioTercero());
            log.info("Forma Juridica Ikus : " + salidaCampoSuplementarioTercero.getCampoSuplementarioTercero().getValorDesplegable());
            valor = MeIkus01Utils.getValorCampoSuplementarioTercero(salidaCampoSuplementarioTercero.getCampoSuplementarioTercero());
            log.info("valor = " + valor);
        } catch (MeIkus01Exception ex) {
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del la forma juridica IKUS");
        } catch (NumberFormatException ex) {
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del la forma juridica IKUS");
        } catch (final NullPointerException ex) {
            throw new MeIkus01Exception("Es necesario indicar la forma juridica en datos de tercero");//
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioFormaJuridicaIkus() : END");
        }
        return valor;
    }//getCampoSuplementarioTipoTerceroIkus

    /**
     * Recupera el campo suplementario que contiene el anio de la convocatoria
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getAnioConvocatoria(String numExpediente, String codProcedimiento,
            String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getAnioConvocatoria() : BEGIN");
        }
        String anioConvocatoria = new String();
        String tipoCampo = new String();
        String valor = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            anioConvocatoria = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_ANIO_CONVOCATORIA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_ANIO_CONVOCATORIA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    anioConvocatoria, Integer.parseInt(tipoCampo));

            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un problema recuperando el anio de la convocatoria " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un problema recuperando el anio de la convocatoria ", ex);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("getAnioConvocatoria() : END");
        }
        return valor;
    }//getAnioConvocatoria

    private String getCodProv(Integer codOrganizcion, String codTercero, String numExpediente) throws MeIkus01Exception {
        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();

        String codProv = new String();
        try {
            codProv = meIkus01Manager.getCodigoProvincia(codTercero, numExpediente, codOrganizcion);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error recuperando la propiedad de mapeo al territorio historico para la provincia = " + codTercero + " " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error recuperando la propiedad de mapeo al territorio historico para la provincia = " + codTercero + " ", ex);
        }
        return codProv;
    }

    /**
     * Para un id de provincia realiza una conversion al territorio historico
     * correspondiente de la pasarela de pagos
     *
     * @param codOrganizacion
     * @param codProvincia
     * @return
     * @throws MeIkus01Exception
     */
    private String getConversionTerritorioHistorico(Integer codOrganizacion, String codProvincia) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getConversionTerritorioHistorico() : BEGIN");
        }
        String codTerritorioHistorico = new String();
        try {
            codTerritorioHistorico = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TERRITORIO_HISTORICO + MeIkus01Constantes.BARRA + codProvincia,
                    MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la propiedad de mapeo al territorio historico para la provincia = " + codProvincia + " " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error recuperando la propiedad de mapeo al territorio historico para la provincia = " + codProvincia + " ", ex);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("getConversionTerritorioHistorico() : END");
        }
        return codTerritorioHistorico;
    }//getConversionTerritorioHistorico

    /**
     * Graba en la base de datos el resultado de una operacion contra la
     * pasarela de pagos
     *
     * @param respuesta
     * @throws MeIkus01Exception
     */
    private void grabarResultadoOperacion(RespuestaOperacionVO respuesta) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("grabarResultadoOperacion() : BEGIN");
        }
        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
        try {
            meIkus01Manager.grabarRespuestaOperacion(respuesta);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error grabando la respuesta de la pasarela de pagos en la BBDD " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error grabando la respuesta de la pasarela de pagos en la BBDD", ex);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("grabarResultadoOperacion() : END");
        }
    }//grabarResultadoOperacion

    /**
     * Devuelve el numero de pagos que tiene la convocatoria de un procedimiento
     *
     * @param codProcedimiento
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private Integer getNumPagos(String codProcedimiento, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getNumPagos() : BEGIN");
        }
        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
        Integer numPagos = null;
        try {
            Vector<ConvocatoriaVO> convocatorias = meIkus01Manager.getConvocatorias(codProcedimiento, codOrganizacion);
            if (convocatorias.size() >= 1) {
                ConvocatoriaVO convocatoria = convocatorias.get(0);
                numPagos = convocatoria.getNumPagos();
            } else {
                log.error("No se ha definido una convocatoria para el expediente");
                throw new MeIkus01Exception("No se ha definido una convocatoria para el expediente");
            }//if(convocatorias.size() >= 1)
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un recuperando los datos de la convocatoria " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un recuperando los datos de la convocatoria ", ex);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("getNumPagos() : END");
        }
        return numPagos;
    }//getNumPagos

    /**
     * Recupera el ultimo anio de pago de un expediente
     *
     * @param numExpediente
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getPagosRealizados(String numExpediente, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getPagosRealizados() : BEGIN");
        }
        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
        String pagosRealizados = null;
        try {
            PagoPasarelaVO pago = meIkus01Manager.getPagoExpediente(numExpediente, codOrganizacion);
            if (pago != null && pago.getNumExpediente() != null) {
                pagosRealizados = pago.getNumPago();
            } else {
                log.error("No existe registro de pago para este expediente");
                throw new MeIkus01Exception("No existe registro de pago para este expediente");
            }//if(pago != null && pago.getNumExpediente() != null)
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un recuperando el ultimo anio de pago de la convocatoria " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un recuperando el ultimo anio de pago de la convocatoria ", ex);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("getPagosRealizados() : END");
        }
        return pagosRealizados;
    }//getUltimoAnioPago

    /**
     * Recupera el indentificador del expediente asociado a la pasarela de pagos
     *
     * @param numExpediente
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getIdExpedientePasarela(String numExpediente, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getIdExpedientePasarela() : BEGIN");
        }
        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
        String idExpedientePasarela = null;
        try {
            PagoPasarelaVO pago = meIkus01Manager.getPagoExpediente(numExpediente, codOrganizacion);
            if (pago != null && pago.getNumExpediente() != null) {
                idExpedientePasarela = pago.getIdExpedientePasarela();
            }//if(pago != null && pago.getNumExpediente() != null)
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un recuperando el numero de expediente de la pasarela de pagos " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un recuperando el numero de expediente de la pasarela de pagos ", ex);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("getIdExpedientePasarela() : END");
        }
        return idExpedientePasarela;
    }//getIdExpedientePasarela

    /**
     * Devuelve el importe del anio enviado como parametro
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @param numAnio
     * @return
     * @throws MeIkus01Exception
     */
    private String getImporteConcedidoAnio(String numExpediente, String codProcedimiento,
            String ejercicio, Integer codOrganizacion, Integer numAnio) throws MeIkus01Exception {
        log.debug("getImporteConcedidoAnio() : BEGIN");
        String tipoCampo = "";
        String valor = "";
        String prefijoCampo = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            prefijoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.PREF_IMPORTE_CONCEDIDO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            String anioRecuperar = prefijoCampo + numAnio;

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.PREF_IMPORTE_CONCEDIDO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    anioRecuperar, Integer.parseInt(tipoCampo));

            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe del anio " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe del anio " + ex.getMessage());
        } catch (NumberFormatException ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe del anio " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe del anio " + ex.getMessage());
        } //try-catch
        //try-catch
        log.debug("getImporteConcedidoAnio() : END");
        return valor;
    }//getImporteAnio

    /**
     * Recupera del fichero de propiedades del modulo el rol del procedimiento
     * que indica cual es el beneficiario
     *
     * @param codProcedimiento
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getRolBeneficiario(String codProcedimiento, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getRolBeneficiario() : BEGIN");
        }
        String rolBeneficiario = new String();
        try {
            rolBeneficiario = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.ROL_BENEFICIARIO,
                    MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
        } catch (Exception ex) {
            log.error("Se ha producido un error recupenrando el rol del beneficiario " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido un error recupenrando el rol del beneficiario ", ex);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("getRolBeneficiario() : END");
        }
        return rolBeneficiario;
    }//getRolBeneficiario

    private String getAnioFromNumExpediente(String numExpediente) throws MeIkus01Exception {
        log.debug("getAnioFromNumExpediente - Begin()");
        String anioExpediente = "";
        if (numExpediente != null && !"".equals(numExpediente)) {
            try {
                anioExpediente = numExpediente.substring(0, 4);
            } catch (Exception e) {
                log.error("Error al recoger año del num expediente : " + e.getMessage());
                throw new MeIkus01Exception("No se ha podido recueprar el año desde el numero del expediente : " + numExpediente);
            }
            if (anioExpediente != null) {
                try {
                    Integer anio = Integer.valueOf(anioExpediente);
                    log.info("Anio Obtenido " + anio + " / " + anioExpediente);
                } catch (NumberFormatException ex) {
                    log.error("Anio de la convocatoria erroneo. No se puede convertir a numero entero " + anioExpediente + ex.getMessage());
                    throw new MeIkus01Exception("Se ha producido un error recuperando el año desde numero de expediente. No es un valor numerico");
                }
            } else {
                throw new MeIkus01Exception("Se ha producido un error recuperando el año desde numero de expediente. Se ha recuperado valor a null");
            }
        } else {
            throw new MeIkus01Exception("Numero de Expediente recibido a null, no se puede obtener el año del expediente desde el numero de Expediente" + numExpediente);
        }
        log.debug("getAnioFromNumExpediente - End() " + numExpediente + "-->" + anioExpediente);
        return anioExpediente;
    }

    private String getIdLIneaAyudaFromFicheroProperties(Integer codOrganizacion, String codProcedimiento) throws MeIkus01Exception {
        log.debug("getIdLIneaAyudaFromFicheroProperties - Begin()");
        String IdLineaAyuda = "";
        try {
            IdLineaAyuda = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.CODIGO_LINEA_AYUDA,
                    MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);
            if (IdLineaAyuda == null || "".equals(IdLineaAyuda)) {
                throw new MeIkus01Exception("Id linea de ayuda no recuperado desde el Properties");
            }
        } catch (MeIkus01Exception e) {
            log.error("Error al recuperar el Id de la linea de ayuda desde el properties :  " + e.getMessage(), e);
            throw new MeIkus01Exception("Error recuperando el ID de la linea de ayuda por procedimiento desde el fichero properties" + e.getMessage());
        }
        log.debug("getIdLIneaAyudaFromFicheroProperties - End() " + codOrganizacion + "/" + codProcedimiento + "-->" + IdLineaAyuda);
        return IdLineaAyuda;
    }

    /**
     * Recupera el campo suplementario de la fecha resolucion
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getCampoSuplementarioTramiteFechaResolucion(String numExpediente, String codProcedimiento,
            String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioTramiteFechaResolucion() : BEGIN");
        }
        String valor = new String();
        String tipoCampo = new String();
        String codCampo = new String();
        String codTramite = new String();
        Integer ocuTramite;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            codCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_FECHA_RESOLUCION, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_FECHA_RESOLUCION, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            codTramite = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TRAMITE + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_FECHA_RESOLUCION, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            ocuTramite = MeIkus01Manager.getInstance().getOcurrencia(numExpediente, new Integer(codTramite), codOrganizacion);

            log.debug("Campo: " + codCampo + " - Tipo: " + tipoCampo + " - tramite: " + codTramite + " - ocu: " + ocuTramite);

            SalidaIntegracionVO salida = el.getCampoSuplementarioTramite(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    Integer.parseInt(codTramite), ocuTramite, codCampo, Integer.parseInt(tipoCampo));
            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario de tramite fecha resolución " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario de tramite fecha resolución " + ex.getMessage());
        } catch (NumberFormatException ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario de tramite fecha resolución " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario de tramite fecha resolución " + ex.getMessage());
        } catch (NullPointerException ex) {
            log.error("No existe el campo suplementario de tramite fecha resolución " + ex.getMessage());
            throw new MeIkus01Exception("No existe el campo suplementario de tramite fecha resolución " + ex.getMessage());
        }    //try-catch
        //try-catch
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioTramiteFechaResolucion() : END");
        }
        return valor;
    }//getCampoSuplementarioTramiteFechaResolucion    

    /**
     * Recupera los importes para los anios y crea un objeto W75BImporteAniosVO
     * para la llamada a la pasarela de pagos
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @param numPagos
     * @return
     * @throws MeIkus01Exception
     */
    private W75BImporteAniosVO getImportesReservaAnios(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion, Integer numPagos) throws MeIkus01Exception {
        W75BImporteAniosVO importeAniosVO = new W75BImporteAniosVO();
        if (log.isDebugEnabled()) {
            log.debug("getImportesConcedidosAnios() : BEGIN");
        }
        if (log.isDebugEnabled()) {
            log.debug("Recorremos el numero de pagos recogiendo los datos de los campos de los importes");
        }
        for (int x = 1; x <= numPagos; x++) {
            switch (x) {
                case 1:
                    try {
                    importeAniosVO.setImporteAnio1(Double.parseDouble(getImporteReservaAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el primer campo de importe anio");
                }//try-catch
                break;
                case 2:
                    try {
                    importeAniosVO.setImporteAnio2(Double.parseDouble(getImporteReservaAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el segundo campo de importe anio");
                }//try-catch
                break;
                case 3:
                    try {
                    importeAniosVO.setImporteAnio3(Double.parseDouble(getImporteReservaAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el tercer campo de importe anio");
                }//try-catch
                break;
                case 4:
                    try {
                    importeAniosVO.setImporteAnio4(Double.parseDouble(getImporteReservaAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el cuarto campo de importe anio");
                }//try-catch
                break;
                case 5:
                    try {
                    importeAniosVO.setImporteAnio5(Double.parseDouble(getImporteReservaAnio(numExpediente, codProcedimiento, ejercicio, codOrganizacion, x)));
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error recuperando el importe para el cuerto campo de importe anio");
                }//try-catch
                break;
                default:
                    break;
            }
        }//for(int x=0; x<=numPagos; x++)
        if (numPagos > 5) {
            try {
                importeAniosVO.setImporteRestoAnios(Double.parseDouble(getCampoImporteReservaRestoAnios(numExpediente, codProcedimiento, ejercicio, codOrganizacion)));
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error recuperando el importe para el importe del resto de anios");
            }//try-catch
        }//if(numPagos > 5)
        if (log.isDebugEnabled()) {
            log.debug("getImportesConcedidosAnios() : END");
        }
        return importeAniosVO;
    }//getImportesAnios

    /**
     * Devuelve el importe del anio enviado como parametro
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @param numAnio
     * @return
     * @throws MeIkus01Exception
     */
    private String getImporteReservaAnio(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion, Integer numAnio) throws MeIkus01Exception {
        log.info("getImporteReservaAnio() : BEGIN");
        String tipoCampo = "";
        String valor = "";
        String prefijoCampo = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            prefijoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.PREF_IMPORTE_RESERVA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            String anioRecuperar = prefijoCampo + numAnio;

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.PREF_IMPORTE_RESERVA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    anioRecuperar, Integer.parseInt(tipoCampo));

            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe del anio " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe del anio " + ex.getMessage());
        } catch (NumberFormatException ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe del anio " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe del anio " + ex.getMessage());
        } //try-catch
        //try-catch
        log.debug("getImporteReservaAnio() : END");
        return valor;
    }//getImporteReservaAnio

    /**
     * Recupera la cantidad del campo suplementario del importe del resto de
     * anios
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getCampoImporteReservaRestoAnios(String numExpediente, String codProcedimiento,
            String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoImporteReservaRestoAnios() : BEGIN");
        }
        String importeReservaAnios = "";
        String tipoCampo = "";
        String valor = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            importeReservaAnios = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_IMPORTE_RESERVA_RESTO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_IMPORTE_RESERVA_RESTO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    importeReservaAnios, Integer.parseInt(tipoCampo));

            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe del resto de anios " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe del resto de anios " + ex.getMessage());
        } catch (NumberFormatException ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe del resto de anios " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe del resto de anios " + ex.getMessage());
        } //try-catch
        //try-catch
        if (log.isDebugEnabled()) {
            log.debug("getCampoImporteReservaRestoAnios() : END");
        }
        return valor;
    }//getCampoImporteReservaRestoAnios

    /**
     * Recupera el campo suplementario del importe reserva
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getCampoSuplementarioImporteReserva(String numExpediente, String codProcedimiento,
            String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioImporteConcedido() : BEGIN");
        }
        String importeReserva = "";
        String tipoCampo = "";
        String valor = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            importeReserva = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_IMPORTE_RESERVA_TOTAL, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_IMPORTE_RESERVA_TOTAL, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    importeReserva, Integer.parseInt(tipoCampo));

            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe concedido " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe concedido " + ex.getMessage());
        } catch (NumberFormatException ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del importe concedido " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del importe concedido " + ex.getMessage());
        } //try-catch
        //try-catch
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioImporteConcedido() : END");
        }
        return valor;
    }//getCampoSuplementarioImporteConcedido

    /**
     * Graba el ID de la Reserva que devuelve PASIKUS
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param valorIdReserva
     * @param ejercicio
     * @param codOrganizacion
     * @throws MeIkus01Exception
     */
    private void grabarIdReserva(String numExpediente, String codProcedimiento, String ejercicio, String valorIdReserva, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("grabarIdReserva() : BEGIN");
        String codigoCampo = "";
        String tipoCampo = "";
        try {
            codigoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_ID_RESERVA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_ID_RESERVA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            grabarCampoTextoExpediente(numExpediente, codProcedimiento, ejercicio, codigoCampo, tipoCampo, valorIdReserva, codOrganizacion);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion grabando el campo suplementario IdReserva " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion grabando el campo suplementario IdReserva " + ex.getMessage());
        }
    }

    /**
     * Graba el ID del expediente que devuelve PASIKUS
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param valorIdExpediente
     * @param ejercicio
     * @param codOrganizacion
     * @throws MeIkus01Exception
     */
    private void grabarIdExpedientePasarela(String numExpediente, String codProcedimiento, String ejercicio, String valorIdExpediente, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("grabarIdExpedientePasarela() : BEGIN");
        String codigoCampo = "";
        String tipoCampo = "";
        try {
            codigoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_ID_EXPEDIENTE, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_ID_EXPEDIENTE, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            grabarCampoTextoExpediente(numExpediente, codProcedimiento, ejercicio, codigoCampo, tipoCampo, valorIdExpediente, codOrganizacion);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion grabando el campo suplementario Id Expediente Pasarela " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion grabando el campo suplementario Id Expediente Pasarela " + ex.getMessage());
        }
    }

    /**
     * Graba el ID del pago que devuelve PASIKUS
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param valorIdPago
     * @param numeroPago
     * @param codOrganizacion
     * @throws MeIkus01Exception
     */
    private void grabarIdPago(String numExpediente, String codProcedimiento, String ejercicio, String valorIdPago, String numeroPago, Integer codOrganizacion) throws MeIkus01Exception {
        String prefijoCampo = "";
        String idPagoaGrabar = "";
        String tipoCampo = "";
        try {
            prefijoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.PREF_ID_PAGO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.PREF_ID_PAGO, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            idPagoaGrabar = prefijoCampo + numeroPago;
            grabarCampoTextoExpediente(numExpediente, codProcedimiento, ejercicio, idPagoaGrabar, tipoCampo, valorIdPago, codOrganizacion);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion grabando el campo suplementario IdPago " + numeroPago + " " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion grabando el campo suplementario IdPago" + numeroPago + " " + ex.getMessage());
        }
    }

    /**
     * Graba el ID del expediente EIKA D que devuelve PASIKUS en
     * grabarResolucion
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param expEikaD
     * @param codOrganizacion
     * @throws MeIkus01Exception
     */
    private void grabarIdExpedienteEikaD(String numExpediente, String codProcedimiento, String ejercicio, String expEikaD, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("grabarIdExpedienteEika D : BEGIN");
        String codigoCampo = "";
        String tipoCampo = "";
        try {
            codigoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_EXP_EIKA_D, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_EXP_EIKA_D, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            grabarCampoTextoExpediente(numExpediente, codProcedimiento, ejercicio, codigoCampo, tipoCampo, expEikaD, codOrganizacion);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion grabando el campo suplementario Id Exp EIKA D " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion grabando el campo suplementario Id Exp EIKA D " + ex.getMessage());
        }
    }

    /**
     * Graba el ID del expediente EIKA O que devuelve PASIKUS en grabarPago
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param expEikaO
     * @param numeroPago
     * @param codOrganizacion
     * @throws MeIkus01Exception
     */
    private void grabarIdExpedienteEikaO(String numExpediente, String codProcedimiento, String ejercicio, String expEikaO, String numeroPago, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("grabarIdExpedienteEika O : BEGIN");
        String prefijoCampo = "";
        String idExpGrabar = "";
        String tipoCampo = "";
        try {
            prefijoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.PREF_EXP_EIKA_O, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.PREF_EXP_EIKA_O, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            idExpGrabar = prefijoCampo + numeroPago;
            grabarCampoTextoExpediente(numExpediente, codProcedimiento, ejercicio, idExpGrabar, tipoCampo, expEikaO, codOrganizacion);
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion grabando el campo suplementario Id Exp EIKA O " + numeroPago + " " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion grabando el campo suplementario  Id Exp EIKA O  " + numeroPago + " " + ex.getMessage());
        }
    }

    /**
     * Graba un campo texto de expediente
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param valor
     * @param codigoCampo
     * @param tipoCampo
     * @param ejercicio
     * @param codOrganizacion
     * @throws MeIkus01Exception
     */
    private void grabarCampoTextoExpediente(String numExpediente, String codProcedimiento, String ejercicio, String codigoCampo, String tipoCampo, String valor, Integer codOrganizacion) throws MeIkus01Exception {
        log.info("grabarCampoTextoExpediente() : BEGIN - Exp " + numExpediente + " - Campo " + codigoCampo);
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            CampoSuplementarioModuloIntegracionVO campoSuplementarioIdReserva = new CampoSuplementarioModuloIntegracionVO();
            campoSuplementarioIdReserva.setCodOrganizacion(String.valueOf(codOrganizacion));
            campoSuplementarioIdReserva.setCodProcedimiento(codProcedimiento);
            campoSuplementarioIdReserva.setTipoCampo(Integer.parseInt(tipoCampo));
            campoSuplementarioIdReserva.setTramite(false);
            campoSuplementarioIdReserva.setNumExpediente(numExpediente);
            campoSuplementarioIdReserva.setEjercicio(ejercicio);
            campoSuplementarioIdReserva.setCodigoCampo(codigoCampo);
            campoSuplementarioIdReserva.setValorTexto(valor);
            el.grabarCampoSuplementario(campoSuplementarioIdReserva);

        } catch (NumberFormatException ex) {
            log.error("Se ha producido una excepcion grabando el campo suplementario IdReserva " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion grabando el campo suplementario IdReserva " + ex.getMessage());
        }
    }

    /**
     * Obtiene el valor de ENVIOEIKA
     *
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codOrganizacion
     * @return
     * @throws MeIkus01Exception
     */
    private String getValorEnvioEika(String numExpediente, String codProcedimiento, String ejercicio, Integer codOrganizacion) throws MeIkus01Exception {
        String codigoCampo = "";
        String tipoCampo = "";
        String valor = "";
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            codigoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_ENVIO_EIKA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            tipoCampo = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.MODULO_INTEGRACION + MeIkus01Constantes.BARRA + MeIkus01Constantes.NOMBRE_MODULO
                    + MeIkus01Constantes.BARRA + codProcedimiento + MeIkus01Constantes.BARRA + MeIkus01Constantes.PANTALLA_EXPEDIENTE
                    + MeIkus01Constantes.BARRA + MeIkus01Constantes.TIPO_CAMPO + MeIkus01Constantes.BARRA
                    + MeIkus01Constantes.CAMPO_ENVIO_EIKA, MeIkus01Constantes.FICHERO_CONFIGURACION_MEIKUS);

            SalidaIntegracionVO salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente, codProcedimiento,
                    codigoCampo, Integer.parseInt(tipoCampo));

            valor = MeIkus01Utils.getValorCampoSuplementario(salida.getCampoSuplementario());
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido una excepcion recuperando el campo suplementario del envio ma EIKA " + ex.getMessage());
            throw new MeIkus01Exception("Se ha producido una excepcion recuperando el campo suplementario del envio ma EIKA " + ex.getMessage());
        } catch (NullPointerException ex) {
            log.error("No existe el campo suplementario de tramite envío eika " + ex.getMessage());
            throw new MeIkus01Exception("No existe el campo suplementario de tramite envío eika " + ex.getMessage());
        }
        return valor;
    }

}//class
