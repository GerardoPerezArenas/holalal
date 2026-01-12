package es.altia.flexia.integracion.moduloexterno.meikus;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.meikus.exception.MeIkus01Exception;
import es.altia.flexia.integracion.moduloexterno.meikus.manager.MeIkus01Manager;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.IPasarelaPago;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.util.PasarelaPagosManager;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.ResultadoReservaPasarelaPagosVO;
import es.altia.flexia.integracion.moduloexterno.meikus.pasarelapago.vo.ResultadoResolucionPasarelaPagosVO;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01Constantes;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.ConvocatoriaVO;
import es.altia.flexia.integracion.moduloexterno.meikus.vo.DatosPasikusVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.altia.util.commons.StringOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.OperacionPasikus;
import es.altia.flexia.integracion.moduloexterno.meikus.plugin.bilbomatica.W75BResultadoReservaVO;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.DatosExpedienteVO;
import es.altia.agora.business.escritorio.UsuarioValueObject;

/**
 *
 * @author paz.rodriguez
 */
public class Meikus01 extends ModuloIntegracionExterno {

    //Logger
    protected static Logger log = LogManager.getLogger(Meikus01.class);

    //Codigos de error devueltos por la aplicacion
    private static final String TODO_CORRECTO = "0";
    private static final String ERROR_RECUPERANDO_CONVOCATORIAS = "1";
    private static final String ERROR_COD_CONVOCATORIA_EXISTENTE = "2";
    private static final String ERROR_INSERTANDO_CONVOCATORIA = "3";
    private static final String ERROR_ELIMINANDO_CONVOCATORIA = "4";

    //Codigos de error devueltos por las operaciones a nivel de tramite
    private static final String OPERACION_CORRECTA = "0";
    private static final String ERROR_OPERANDO_CON_PASARELA_PAGOS = "1";
    private static final String ERROR_OPERACION_INICIAR_EXPEDIENTE = "2";

    //Error para mostrar un mensaje de que no se puede retroceder el tramite
    private static final String ERROR_IMPEDIR_RETROCEDER_TRAMITE = "3";

    private static final GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();

    private static final SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Recupera la pantalla donde se mostraran la lista de convocatorias
     *
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento:
     * @param request:
     * @param response:
     * @return devuelve un string con la url de la jsp
     */
    public String prepararPantallaConvocatoria(int codOrganizacion, String codProcedimiento,
            HttpServletRequest request, HttpServletResponse response) throws TechnicalException {
        log.debug("prepararPantallaConvocatoria() : BEGIN ");
        String url = "";
        String codError = TODO_CORRECTO;
        Vector salida = new Vector<ConvocatoriaVO>();

        try {
            log.debug("Instanciamos el manager de acceso a la BBDD");
            MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();

            log.debug("El codigo del procedimiento es: " + codProcedimiento);
            log.debug("Comprobamos si existen convocatorias");
            salida = meIkus01Manager.getConvocatorias(codProcedimiento, codOrganizacion);
            request.setAttribute("RelacionConvocatorias", salida);
        } catch (MeIkus01Exception e) {
            log.error("Se ha producido un error buscando las instancias de convocatorias ya existentes en la BBDD");
            codError = ERROR_RECUPERANDO_CONVOCATORIAS;
        }//try-catch

        try {
            log.debug("Recuperamos la url de la JSP del modulo que muestra las convocatorias");
            url = MeIkus01ConfigurationParameter.getParameter(codOrganizacion + ConstantesDatos.MODULO_INTEGRACION
                    + this.getNombreModulo() + ConstantesDatos.BARRA + codProcedimiento + MeIkus01Constantes.LISTADO_CONVOCATORIAS, this.getNombreModulo());
            log.debug("Valor url:" + url);
        } catch (Exception e) {
            log.error("Se ha producido un erro recuperando la URL de la pantalla a nivel del procedimiento " + e.getMessage());
        }//try-catch

        log.debug("Guardamos el posible codigo de error en la request");
        request.setAttribute("codError", codError);
        log.debug("prepararPantallaConvocatoria() : END ");
        return url;
    }//prepararPantallaConvocatoria

    /**
     * Se procede a dar de alta una convocatoria
     *
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento:
     * @param request:
     * @param response:
     * @return devuelve un string igual a null
     */
    public String procesarAltaConvocatoria(int codOrganizacion, String codProcedimiento, HttpServletRequest request, HttpServletResponse response) throws TechnicalException {
        log.debug("procesarAltaConvocatoria() : BEGIN ");
        //1º Obtencion de conexion a la BBDD
        boolean salida = false;
        ConvocatoriaVO convocatoria = new ConvocatoriaVO();
        int numPlurianualidades = 0;
        int numJustificacion = 0;
        StringBuilder xmlSalida = new StringBuilder();
        String codError = TODO_CORRECTO;

        try {
            MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
            log.debug("El codigo del procedimiento es: " + codProcedimiento);
            log.debug(this.getClass().getName() + ".procesarAltaConvocatoria() :  Se llama al DAO de convocatorias");
            if (log.isDebugEnabled()) {
                log.debug("Los parametros recuperados del formulario de alta son = ");
                log.debug("convocatoria = " + request.getParameter("convocatoria"));
                log.debug("descPlurianualidades = " + request.getParameter("plurianualidades"));
                log.debug("numAnos = " + request.getParameter("numAnos"));
                log.debug("descJustificacion = " + request.getParameter("justificacion"));
                log.debug("numPagos = " + request.getParameter("numPagos"));
            }//if (log.isDebugEnabled())

            log.debug("Se recupera parametro convocatoria del request");
            String codConvocatoria = request.getParameter("convocatoria");
            int numConvocatoria = Integer.parseInt(codConvocatoria);

            log.debug("Se comprueba si existe dicha convocatoria");
            salida = meIkus01Manager.existeConvocatoria(numConvocatoria, codProcedimiento, codOrganizacion);

            log.debug("El valor del métod existeConvatoria es = " + salida);
            if (!salida) {
                log.debug("Los parametros ConvocatoriaVO son = ");
                String cadPlurianualidades = request.getParameter("plurianualidades");
                log.debug("Plurinualidades:" + cadPlurianualidades);
                if (cadPlurianualidades.equals("SI")) {
                    numPlurianualidades = 1;
                } else if (cadPlurianualidades.equals("NO")) {
                    numPlurianualidades = 0;
                }//if (cadPlurianualidades.equals("SI"))

                String cadJustificacion = request.getParameter("justificacion");
                log.debug("Justificacion = " + cadJustificacion);
                if (cadJustificacion.equals("SI")) {
                    numJustificacion = 1;
                } else if (cadJustificacion.equals("NO")) {
                    numJustificacion = 0;
                }//if (cadJustificacion.equals("SI"))

                String cadNumPagos = request.getParameter("numPagos");
                log.debug("NumPagos = " + cadNumPagos);
                int numPagos = Integer.parseInt(cadNumPagos);

                String cadNumAnos = request.getParameter("numAnos");
                log.debug("NumAnos = " + cadNumAnos);
                int numAnos = Integer.parseInt(cadNumAnos);

                convocatoria.setConvocatoria(numConvocatoria);
                convocatoria.setPlurianualidades(numPlurianualidades);
                convocatoria.setJustificacion(numJustificacion);
                convocatoria.setNumPagos(numPagos);
                convocatoria.setNumAnos(numAnos);
                convocatoria.setCodProcedimiento(codProcedimiento);
                convocatoria.setCodOrganizacion(codOrganizacion);

                try {
                    salida = meIkus01Manager.insertarConvocatoria(convocatoria);
                } catch (MeIkus01Exception ex) {
                    log.error("Se ha producido un error insertando el alta de la convocatoria " + ex.getMessage());
                    codError = ERROR_INSERTANDO_CONVOCATORIA;
                }//try-catch
                if (!salida) {//tratamiento mensaje de error
                    codError = ERROR_INSERTANDO_CONVOCATORIA;
                }//if (!salida)
            } else {
                codError = ERROR_COD_CONVOCATORIA_EXISTENTE;
            }//if (!salida)    
        } catch (MeIkus01Exception e) {
            codError = ERROR_INSERTANDO_CONVOCATORIA;
            log.error("Se ha producido un error grabando una nueva convocatoria " + e.getMessage());
        } catch (NumberFormatException e) {
            codError = ERROR_INSERTANDO_CONVOCATORIA;
            log.error("Se ha producido un error grabando una nueva convocatoria " + e.getMessage());
        } //try-catch
        //try-catch

        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_ERROR>");
        xmlSalida.append(codError);
        xmlSalida.append("</CODIGO_ERROR>");
        xmlSalida.append("</RESPUESTA>");
        log.debug("Se devuelve = " + xmlSalida.toString());
        try {
            log.debug("Respuesta");
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("Se ha producido un error enviando la response " + e.getMessage());
        }//try-catch
        log.debug("procesarAltaConvocatoria() : BEGIN ");
        return null;
    }//procesarAltaConvocatoria

    /**
     * Se procede a eliminar una convocatoria
     *
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento:
     * @param request:
     * @param response:
     * @return devuelve un string igual a null
     */
    public String procesarEliminarConvocatoria(int codOrganizacion, String codProcedimiento, HttpServletRequest request, HttpServletResponse response) throws TechnicalException {
        log.debug("procesarEliminarConvocatoria() : BEGIN ");

        ConvocatoriaVO convocatoria = new ConvocatoriaVO();
        int numPlurianualidades = 0;
        int numJustificacion = 0;
        StringBuilder xmlSalida = new StringBuilder();
        String codError = TODO_CORRECTO;

        try {
            MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
            log.debug("El codigo del procedimiento es: " + codProcedimiento);
            log.debug("Se recupera parametro convocatoria del request");
            String cadConvocatoria = request.getParameter("convocatoria");
            int numConvocatoria = Integer.parseInt(cadConvocatoria);
            log.debug("Convocatoria = " + numConvocatoria);

            //construccion del objeto ConvocatoriaVO
            String cadPlurianualidades = request.getParameter("plurianualidades");
            log.debug("Plurinualidades:" + cadPlurianualidades);
            if (cadPlurianualidades.equals("SI")) {
                numPlurianualidades = 1;
            } else if (cadPlurianualidades.equals("NO")) {
                numPlurianualidades = 0;
            }//if (cadPlurianualidades.equals("SI"))

            String cadJustificacion = request.getParameter("justificacion");
            log.debug("Justificacion:" + cadJustificacion);
            if (cadJustificacion.equals("SI")) {
                numJustificacion = 1;
            } else if (cadJustificacion.equals("NO")) {
                numJustificacion = 0;
            }//if (cadJustificacion.equals("SI"))

            String cadNumPagos = request.getParameter("numPagos");
            int numPagos = Integer.parseInt(cadNumPagos);
            log.debug("NumPagos:" + cadNumPagos);

            String cadNumAnos = request.getParameter("numAnos");
            int numAnos = Integer.parseInt(cadNumAnos);
            log.debug("NumAnos:" + cadNumAnos);

            convocatoria.setConvocatoria(numConvocatoria);
            convocatoria.setPlurianualidades(numPlurianualidades);
            convocatoria.setJustificacion(numJustificacion);
            convocatoria.setNumPagos(numPagos);
            convocatoria.setNumAnos(numAnos);
            convocatoria.setCodProcedimiento(codProcedimiento);
            convocatoria.setCodOrganizacion(codOrganizacion);

            try {
                meIkus01Manager.eliminarConvocatoria(convocatoria, codOrganizacion);
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido una excepcion eliminando la convocatoria " + ex.getMessage());
                codError = ERROR_ELIMINANDO_CONVOCATORIA;
            }//try-catch
        } catch (NumberFormatException e) {
            log.error("Se ha producido un error eliminando una convocatoria " + e.getMessage());
            codError = ERROR_ELIMINANDO_CONVOCATORIA;
        }//try-catch

        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_ERROR>");
        xmlSalida.append(codError);
        xmlSalida.append("</CODIGO_ERROR>");
        xmlSalida.append("</RESPUESTA>");
        log.debug("Se devuelve = " + xmlSalida.toString());
        try {
            log.debug("Respuesta");
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("Se ha producido un error enviando la response " + e.getMessage());
        }//try-catch
        log.debug("procesarEliminarConvocatoria() : END ");
        return null;
    }//procesarEliminarConvocatoria

    /**
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @param request
     * @param response
     * @return
     * @throws TechnicalException
     */
    public String cargarVentanaConvocatoria(int codOrganizacion, String codProcedimiento, HttpServletRequest request, HttpServletResponse response) throws TechnicalException {
        String id = null;
        String url = null;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    //edicion
                    id = request.getParameter("id");
                    if (id != null && !id.isEmpty()) {
                        MeIkus01Manager meIkus01Manager = MeIkus01Manager.getInstance();
                        ConvocatoriaVO salida = meIkus01Manager.getConvocatoriaId(codProcedimiento, id, codOrganizacion);
                        if (salida != null) {
                            request.setAttribute("convocatoria", salida);
                        }
                    }
                } else {
                    // alta
                }
            }
        } catch (Exception ex) {
        }
        return url;

    }

    /*
     |--------------------------------------------------------------------------------------------------------------------|
     |                                Operaciones de tramite contra la pasarela de pagos                                  |
     |--------------------------------------------------------------------------------------------------------------------|
     */
    /**
     * Operacion que se implementa en los retrocesos de los tramites que no
     * permitan retroceder y lanza un error personalizado informando sobre ello.
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     */
    public String impedirRetrocederTramite(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        log.debug("impedirRetrocederTramite() : BEGIN ");
        log.debug("Operacion lanzada para impedir que se retrocedan los tramites que no interesa que se puedan retroceder");
        log.debug("impedirRetrocederTramite() : END ");
        return ERROR_IMPEDIR_RETROCEDER_TRAMITE;
    }//impedirRetrocederTramite

    /**
     * Operacion que ejecuta una funcionalidad de la pasarela con las funciones
     * necesarias para inicializar las propiedades que la pasarela de pago
     * necesite para su funcionamiento.
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     */
    public String iniciarExpedientePasarela(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        log.debug("iniciarExpedientePasarela() : BEGIN ");
        String codOperacion = OPERACION_CORRECTA;
        try {
            log.debug("Recuperamos el ejercicio y el codigo del procedimiento del numero del expediente");
            String[] propsExpediente = numExpediente.split("/");
            String ejercicio = propsExpediente[0];
            String codProcedimiento = propsExpediente[1];

            log.debug("Instanciamos la clase de la pasarela");
            IPasarelaPago pasarelaPago = PasarelaPagosManager.getPasarelaPagos(codOrganizacion, codProcedimiento);
            log.debug("Llamamos al metodo iniciarExpedientePasarela");
            codOperacion = pasarelaPago.iniciarExpedientePasarela(numExpediente, codProcedimiento, ejercicio, codOrganizacion);

            if (codOperacion.equalsIgnoreCase(OPERACION_CORRECTA)) {
                log.debug("Operacion iniciar expediente ejecutada contra la pasarela correctamente");
            } else {
                log.error("Se ha producido un error en la llamada a la pasarela para iniciar las propiedades necesarias para el expediente");
                log.error("CodError = " + codOperacion);
            }//if(codOperacion.equalsIgnoreCase(OPERACION_CORRECTA))
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error reservando el credito en la pasarela de pagos");
            codOperacion = ERROR_OPERACION_INICIAR_EXPEDIENTE;
        }//try-catch
        log.debug("iniciarExpedientePasarela() : END ");
        return codOperacion;
    }//iniciarExpedientePasarela

    /**
     * Operacion que ejecuta una funcionalidad de la pasarela de pagos que
     * reserve el credito para el expediente
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     */
    public String reservarCredito(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        log.debug("reservarCredito() : BEGIN ");
        String codOperacion = OPERACION_CORRECTA;
        ResultadoReservaPasarelaPagosVO resultado = new ResultadoReservaPasarelaPagosVO();
        try {
            log.debug("Recuperamos el ejercicio y el codigo del procedimiento del numero del expediente");
            String[] propsExpediente = numExpediente.split("/");
            String ejercicio = propsExpediente[0];
            String codProcedimiento = propsExpediente[1];

            log.debug("Instanciamos la clase de la pasarela");
            IPasarelaPago pasarelaPago = PasarelaPagosManager.getPasarelaPagos(codOrganizacion, codProcedimiento);
            log.debug("Llamamos al metodo de reserva");
            resultado = pasarelaPago.reserva(numExpediente, codProcedimiento, ejercicio, codOrganizacion);

            if (resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA)) {
                log.debug("Operacion reservar credito contra la pasarela ejecutada correctamente");
            } else {
                log.error("Se ha producido un error en la llamada a la pasarela para reservar credito");
                log.error("CodError = " + resultado.getCodigoError());
                log.error(("Mensaje error = " + resultado.getCodMensajeError()));
                codOperacion = resultado.getCodigoError();
            }//if(resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA))
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error reservando el credito en la pasarela de pagos");
            if (ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError())) {
                codOperacion = ex.getCodError();
            } else {
                codOperacion = ERROR_OPERANDO_CON_PASARELA_PAGOS;
            }//if(ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError()))
        }//try-catch
        log.debug("reservarCredito() : END ");
        return codOperacion;
    }//reservarCredito

    /**
     * Operacion que ejecuta una funcionalidad de la pasarela de pagos que grabe
     * la resolucion para el expediente
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     */
    public String grabarResolucion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        log.debug("grabarResolucion() : BEGIN ");
        String codOperacion = OPERACION_CORRECTA;
        ResultadoResolucionPasarelaPagosVO resultado = new ResultadoResolucionPasarelaPagosVO();
        try {
            log.debug("Recuperamos el ejercicio y el codigo del procedimiento del numero del expediente");
            String[] propsExpediente = numExpediente.split("/");
            String ejercicio = propsExpediente[0];
            String codProcedimiento = propsExpediente[1];

            log.debug("Instanciamos la clase de la pasarela");
            IPasarelaPago pasarelaPago = PasarelaPagosManager.getPasarelaPagos(codOrganizacion, codProcedimiento);
            log.debug("Llamamos al metodo grabar resolucion");
            resultado = pasarelaPago.grabarResolucion(numExpediente, codProcedimiento, ejercicio, codOrganizacion);

            if (resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA)) {
                log.debug("Operacion grabar resolucion contra la pasarela ejecutada correctamente");
            } else {
                log.error("Se ha producido un error en la llamada a la pasarela para grabar la resolucion");
                log.error("CodError = " + resultado.getCodigoError());
                log.error("Mensaje error = " + resultado.getCodMensajeError());
                codOperacion = resultado.getCodigoError();
            }//if(resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA))
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error grabando la resolucion en la pasarela de pagos " + ex.getMessage());
            if (ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError())) {
                codOperacion = ex.getCodError();
            } else {
                codOperacion = ERROR_OPERANDO_CON_PASARELA_PAGOS;
            }//if(ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError()))
        }//try-catch
        log.debug("grabarResolucion() : END ");
        return codOperacion;
    }//grabarResolucion

    /**
     * Operacion que ejecuta una funcionalidad de la pasarela de pagos que grabe
     * el pago para el expediente
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return
     */
    public String grabarPago(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        log.debug("grabarPago() : BEGIN ");
        String codOperacion = OPERACION_CORRECTA;
        ResultadoResolucionPasarelaPagosVO resultado = new ResultadoResolucionPasarelaPagosVO();
        try {
            log.debug("Recuperamos el ejercicio y el codigo del procedimiento del numero del expediente");
            String[] propsExpediente = numExpediente.split("/");
            String ejercicio = propsExpediente[0];
            String codProcedimiento = propsExpediente[1];

            log.debug("Instanciamos la clase de la pasarela");
            IPasarelaPago pasarelaPago = PasarelaPagosManager.getPasarelaPagos(codOrganizacion, codProcedimiento);
            log.debug("Llamamos al metodo grabar pago");
            resultado = pasarelaPago.grabarPago(numExpediente, codProcedimiento, ejercicio, codOrganizacion);

            if (resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA)) {
                log.debug("Operacion grabar pago contra la pasarela ejecutada correctamente");
            } else {
                log.error("Se ha producido un error en la llamada a la pasarela para grabar el pago");
                log.error("CodError = " + resultado.getCodigoError());
                log.error("Mensaje error = " + resultado.getCodMensajeError());
                codOperacion = resultado.getCodigoError();
            }//if(resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA))
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error grabando la resolucion en la pasarela de pagos " + ex.getMessage());
            if (ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError())) {
                codOperacion = ex.getCodError();
            } else {
                codOperacion = ERROR_OPERANDO_CON_PASARELA_PAGOS;
            }//if(ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError()))
        }//try-catch
        log.debug("grabarPago() : END ");
        return codOperacion;
    }//grabarPago

    public void reservarCreditoSinExt(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) {
        log.debug("reservarCreditoSinExt() : BEGIN ");
        String codOperacion = iniciarExpedientePasarela(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente);
        ResultadoReservaPasarelaPagosVO resultado = new ResultadoReservaPasarelaPagosVO();
        final OperacionPasikus operacion = new OperacionPasikus();

        if (codOperacion.equalsIgnoreCase(OPERACION_CORRECTA)) {
            try {
                log.debug("Recuperamos el ejercicio y el codigo del procedimiento del numero del expediente");
                String[] propsExpediente = numExpediente.split("/");
                String ejercicio = propsExpediente[0];
                String codProcedimiento = propsExpediente[1];

                // Se comprueba que los valores tengan campos.
                final List<String> campos = Arrays.asList("IMPANIO1", "IMPANIO2");
                if (MeIkus01Manager.getInstance().ningunCampoNumericoEsNulo(campos, codOrganizacion, numExpediente)) {
                    log.debug("Instanciamos la clase de la pasarela");
                    IPasarelaPago pasarelaPago = PasarelaPagosManager.getPasarelaPagos(codOrganizacion, codProcedimiento);
                    log.debug("Llamamos al metodo de reserva");
                    resultado = pasarelaPago.reservaSinExt(numExpediente, codProcedimiento, ejercicio, codOrganizacion);

                    if (resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA)) {
                        operacion.setCodError(OPERACION_CORRECTA);
                        log.debug("Operacion reservar credito contra la pasarela ejecutada correctamente");
                    } else {
                        operacion.setCodError(resultado.getCodigoError());

                        if (request.getParameter("idiomaUsuario") != null) {
                            if (request.getParameter("idiomaUsuario").equals("1")) {
                                operacion.setMsgCodError(resultado.getCodMensajeError()); //getMensaje().getDescripcionCastellano());
                            } // Castellano
                            else {
                                operacion.setMsgCodError(resultado.getCodMensajeError()); //getMensaje().getDescripcionEuskera());
                            } // Euskera
                        } else {
                            operacion.setMsgCodError(resultado.getCodMensajeError()); //getMensaje().getDescripcionCastellano());
                        }
                        log.error("Se ha producido un error en la llamada a la pasarela para reservar credito");
                        log.error("CodError = " + resultado.getCodigoError());
                        log.error(("Mensaje error = " + resultado.getCodMensajeError()));
                        codOperacion = resultado.getCodigoError();
                    }//if(resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA))
                } // if(MeIkus01Manager.getInstance().ningunCampoEsNulo(campos, codProcedimiento, codOrganizacion))
                else {
                    // Sólo falta internacionalizar los mensajes.
                    operacion.setCodError("-1");
                    if (request.getParameter("idiomaUsuario") != null) {
                        if (request.getParameter("idiomaUsuario").equals("1")) {
                            operacion.setMsgCodError("Es necesario poner el valor en los cuadros de texto RESERVA PAGO 1 y RESERVA PAGO 2 y posteriormente guardar los datos");
                        } // Castellano
                        else {
                            operacion.setMsgCodError("Balioa jarri behar da ERRESERBA 1 ORDAINKETA eta ERRESERBA 2 testu-tauletan, eta, ondoren, datuak gorde.");
                        } // Euskera
                    } else {
                        operacion.setMsgCodError("Es necesario poner el valor en los cuadros de texto RESERVA PAGO 1 y RESERVA PAGO 2 y posteriormente guardar los datos");
                    }
                    // Fin de lo que falta de internacionalizar
                }
            } catch (MeIkus01Exception ex) {
                log.error("Se ha producido un error reservando el credito en la pasarela de pagos");
                if (ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError())) {
                    codOperacion = ex.getCodError();
                } else {
                    codOperacion = ERROR_OPERANDO_CON_PASARELA_PAGOS;
                }//if(ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError()))
                operacion.setCodError(codOperacion);
            }//try-catch     
        }
        final Gson gson = gsonBuilder.serializeNulls().create();
        log.debug("reservarCreditoSinExt() : END ");
        RespuestaAjaxUtils.retornarJSON(gson.toJson(operacion), response);
    }

    public void grabarResolucionSinExt(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) {

        log.debug("grabarResolucionSinExt() : BEGIN ");
        final OperacionPasikus operacion = new OperacionPasikus();
        String codOperacion = OPERACION_CORRECTA;
        ResultadoResolucionPasarelaPagosVO resultado = new ResultadoResolucionPasarelaPagosVO();
        try {
            log.debug("Recuperamos el ejercicio y el codigo del procedimiento del numero del expediente");
            String[] propsExpediente = numExpediente.split("/");
            String ejercicio = propsExpediente[0];
            String codProcedimiento = propsExpediente[1];

            final List<String> campos = Arrays.asList("IMPANIO1", "IMPANIO2", "IMPRESERVA");
            if (MeIkus01Manager.getInstance().ningunCampoNumericoEsNulo(campos, codOrganizacion, numExpediente)) {
                log.debug("Instanciamos la clase de la pasarela");
                IPasarelaPago pasarelaPago = PasarelaPagosManager.getPasarelaPagos(codOrganizacion, codProcedimiento);
                log.debug("Llamamos al metodo grabar resolucion");
                resultado = pasarelaPago.grabarResolucionSinExt(numExpediente, codProcedimiento, ejercicio, codOrganizacion);

                if (resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA)) {
                    log.debug("Operacion grabar resolucion contra la pasarela ejecutada correctamente");
                    operacion.setCodError(resultado.getCodigoError());
                } else {
                    log.error("Se ha producido un error en la llamada a la pasarela para grabar la resolucion");
                    log.error("CodError = " + resultado.getCodigoError());
                    log.error("Mensaje error = " + resultado.getCodMensajeError());
                    operacion.setCodError(resultado.getCodigoError());
                    // Falta por internacionalizar
                    if (request.getParameter("idiomaUsuario") != null) {
                        if (request.getParameter("idiomaUsuario").equals("1")) {
                            operacion.setMsgCodError(resultado.getCodMensajeError());
                        } // Castellano
                        else {
                            operacion.setMsgCodError(resultado.getCodMensajeError());
                        } // Euskera
                    } else {
                        operacion.setMsgCodError(resultado.getCodMensajeError());
                    }
                    // Fin de lo que falta de internacionalizar                
                }//if(resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA))
            } //if (MeIkus01Manager.getInstance().ningunCampoEsNulo(campos, codOrganizacion, numExpediente))
            else {
                // Sólo falta internacionalizar los mensajes.
                operacion.setCodError("-1");
                if (request.getParameter("idiomaUsuario") != null) {
                    if (request.getParameter("idiomaUsuario").equals("1")) {
                        operacion.setMsgCodError("Es necesario poner el valor en los cuadros de texto IMPORTE RESERVA, RESERVA PAGO 1 y RESERVA PAGO 2 y posteriormente guardar los datos");
                    } // Castellano
                    else {
                        operacion.setMsgCodError("ERRESERBAREN ZENBATEKOA, 1. ORDAINKETAREN ERRESERBA eta 2. ORDAINKETAREN ERRESERBA testu-tauletan jarri behar da balioa, eta, ondoren, datuak gorde.");
                    } // Euskera
                } else {
                    operacion.setMsgCodError("Es necesario poner el valor en los cuadros de texto IMPORTE RESERVA, RESERVA PAGO 1 y RESERVA PAGO 2 y posteriormente guardar los datos");
                }
                // Fin de lo que falta de internacionalizar
            }
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error grabando la resolucion en la pasarela de pagos " + ex.getMessage());
            if (ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError())) {
                codOperacion = ex.getCodError();
            } else {
                codOperacion = ERROR_OPERANDO_CON_PASARELA_PAGOS;
            }//if(ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError()))
            operacion.setCodError(codOperacion);
            if (codOperacion.equals("23")) {
                operacion.setMsgCodError("Debe avanzar el expediente hasta el trámite de resolución y grabar la fecha de resolución (FECRESOL01)");
            } else if (codOperacion.equals("25")) {
                operacion.setMsgCodError("Es necesario poner SI o NO en ¿ENVIAR LOS DATOS A EIKA? y posteriormente guardar los datos");
            } else {
                operacion.setMsgCodError(ex.getMessage());
            }
        }//try-catch    
        final Gson gson = gsonBuilder.serializeNulls().create();
        RespuestaAjaxUtils.retornarJSON(gson.toJson(operacion), response);
        log.debug("grabarResolucionSinExt() : END ");
    }

    public void grabarPagoSinExt(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) {
        log.debug("grabarPagoSinExt() : BEGIN ");

        String codOperacion = OPERACION_CORRECTA;
        final OperacionPasikus operacion = new OperacionPasikus();
        ResultadoResolucionPasarelaPagosVO resultado = new ResultadoResolucionPasarelaPagosVO();
        try {
            log.debug("Recuperamos el ejercicio y el codigo del procedimiento del numero del expediente");
            String[] propsExpediente = numExpediente.split("/");
            String ejercicio = propsExpediente[0];
            String codProcedimiento = propsExpediente[1];
            final List<String> campos = Arrays.asList("PAGO1", "PAGO2");
            if (MeIkus01Manager.getInstance().ningunCampoNumericoEsNulo(campos, codOrganizacion, numExpediente)) {
                log.debug("Instanciamos la clase de la pasarela");
                IPasarelaPago pasarelaPago = PasarelaPagosManager.getPasarelaPagos(codOrganizacion, codProcedimiento);
                log.debug("Llamamos al metodo grabar pago");
                resultado = pasarelaPago.grabarPagoSinExt(numExpediente, codProcedimiento, ejercicio, codOrganizacion);

                if (resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA)) {
                    log.debug("Operacion grabar pago contra la pasarela ejecutada correctamente");
                    operacion.setCodError(resultado.getCodigoError());
                } else {
                    log.error("Se ha producido un error en la llamada a la pasarela para grabar el pago");
                    log.error("CodError = " + resultado.getCodigoError());
                    log.error("Mensaje error = " + resultado.getCodMensajeError());
                    codOperacion = resultado.getCodigoError();
                    operacion.setCodError(resultado.getCodigoError());
                    operacion.setMsgCodError(resultado.getCodMensajeError());
                }//if(resultado.getCodigoError().equalsIgnoreCase(OPERACION_CORRECTA))
            } else {
                // Sólo falta internacionalizar los mensajes.
                operacion.setCodError("-1");
                if (request.getParameter("idiomaUsuario") != null) {
                    if (request.getParameter("idiomaUsuario").equals("1")) {
                        operacion.setMsgCodError("Es necesario poner el valor en los cuadros de texto CONCEDIDO PAGO 1 y CONCEDIDO PAGO 2 y posteriormente guardar los datos");
                    } // Castellano
                    else {
                        operacion.setMsgCodError("Balioa jarri behar da EMANDAKO 1. ORDAINKETA eta EMANDAKO 2. ORDAINKETA testu-tauletan, eta, ondoren, datuak gorde");
                    } // Euskera
                } else {
                    operacion.setMsgCodError("Es necesario poner el valor en los cuadros de texto CONCEDIDO PAGO 1 y CONCEDIDO PAGO 2 y posteriormente guardar los datos");
                }
                // Fin de lo que falta de internacionalizar
            }
        } catch (MeIkus01Exception ex) {
            log.error("Se ha producido un error grabando la resolucion en la pasarela de pagos " + ex.getMessage());
            if (ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError())) {
                codOperacion = ex.getCodError();
            } else {
                codOperacion = ERROR_OPERANDO_CON_PASARELA_PAGOS;
            }//if(ex.getCodError() != null && !"".equalsIgnoreCase(ex.getCodError()))
            if (codOperacion.equals("25")) {
                operacion.setMsgCodError("Es necesario responder SI o NO en ¿ENVIAR LOS DATOS A EIKA? y posteriormente guardar los datos");
            } else {
                operacion.setMsgCodError(ex.getMessage());
            }
            operacion.setCodError(codOperacion);
        }//try-catch  
        final Gson gson = gsonBuilder.serializeNulls().create();
        RespuestaAjaxUtils.retornarJSON(gson.toJson(operacion), response);
        log.debug("grabarPagoSinExt() : END ");
    }

    public boolean mostrarBtnReserva(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) {
        log.debug("mostrarBtnReserva: numExpediente = " + numExpediente);

        return MeIkus01Manager.getInstance().ningunCampoNumericoEsNulo(Arrays.asList("IMPANIO1", "IMPANIO2"), codOrganizacion, numExpediente)
                && !MeIkus01Manager.getInstance().ningunCampoTextoEsNuloOVacio(Arrays.asList("IDRESERVA"), codOrganizacion, numExpediente);
    }

    public boolean mostrarBtnGrabarRes(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) {
        log.debug("mostrarBtnGrabarRes: numExpediente = " + numExpediente);

        final boolean result = MeIkus01Manager.getInstance().ningunCampoTextoEsNuloOVacio(Arrays.asList("IDRESERVA"), codOrganizacion, numExpediente)
                && MeIkus01Manager.getInstance().ningunCampoNumericoEsNulo(Arrays.asList("IMPANIO1", "IMPANIO2"), codOrganizacion, numExpediente)
                && !MeIkus01Manager.getInstance().ningunCampoTextoEsNuloOVacio(Arrays.asList("IDIKUS"), codOrganizacion, numExpediente);
        return result;
    }

    public boolean mostrarBtnGrabarPago(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) {

        log.debug("mostrarBtnGrabarPago: numExpediente = " + numExpediente);

        boolean parcialResult = MeIkus01Manager.getInstance().ningunCampoTextoEsNuloOVacio(Arrays.asList("IDIKUS"), codOrganizacion, numExpediente)
                && MeIkus01Manager.getInstance().ningunCampoNumericoEsNulo(Arrays.asList("PAGO1", "PAGO2"), codOrganizacion, numExpediente);

        String[] propsExpediente = numExpediente.split("/");
        String codProcedimiento = propsExpediente[1];
        Integer numPagos = null;
        try {
            Vector<ConvocatoriaVO> convocatorias = MeIkus01Manager.getInstance().getConvocatorias(codProcedimiento, codOrganizacion);
            if (convocatorias.size() >= 1) {
                ConvocatoriaVO convocatoria = convocatorias.get(0);
                numPagos = convocatoria.getNumPagos();
            }
        } catch (MeIkus01Exception ex) {
        }
        boolean result = false;

        if (parcialResult && numPagos != null) {
            if (numPagos == 1) {
                result = !MeIkus01Manager.getInstance().ningunCampoTextoEsNuloOVacio(Arrays.asList("IDPAGO1"), codOrganizacion, numExpediente);
            } else if (numPagos == 2) {
                result = !MeIkus01Manager.getInstance().ningunCampoTextoEsNuloOVacio(Arrays.asList("IDPAGO1"), codOrganizacion, numExpediente)
                        || !MeIkus01Manager.getInstance().ningunCampoTextoEsNuloOVacio(Arrays.asList("IDPAGO2"), codOrganizacion, numExpediente);
            }
        }
        return result;

    }
    public String cargarDatosPasikus(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("=========== Inicio en cargarDatosPasikus");

        try {
            final String envioEika = MeIkus01Manager.getInstance().getSuplementarioDesplegable(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "ENVIOEIKA");
            final String impReserva = MeIkus01Manager.getInstance().getSuplementarioNumerico(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "IMPRESERVA");
            final String idReserva = MeIkus01Manager.getInstance().getSuplementarioTexto(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "IDRESERVA");
            final String impanio1 = MeIkus01Manager.getInstance().getSuplementarioNumerico(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "IMPANIO1");
            final String impanio2 = MeIkus01Manager.getInstance().getSuplementarioNumerico(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "IMPANIO2");
            final String idIkus = MeIkus01Manager.getInstance().getSuplementarioTexto(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "IDIKUS");
            final String expeIkad = MeIkus01Manager.getInstance().getSuplementarioTexto(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "EXPEIKAD");
            final String importeConcedido = MeIkus01Manager.getInstance().getSuplementarioNumerico(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "IMPORTECONCEDIDO");
            final String pago1 = MeIkus01Manager.getInstance().getSuplementarioNumerico(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "PAGO1");
            final String pago2 = MeIkus01Manager.getInstance().getSuplementarioNumerico(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "PAGO2");
            final String idPago1 = MeIkus01Manager.getInstance().getSuplementarioTexto(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "IDPAGO1");
            final String idPago2 = MeIkus01Manager.getInstance().getSuplementarioTexto(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "IDPAGO2");
            final String expeIkao1 = MeIkus01Manager.getInstance().getSuplementarioTexto(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "EXPEIKAO1");
            final String expeIkao2 = MeIkus01Manager.getInstance().getSuplementarioTexto(codOrganizacion, numExpediente,
                    numExpediente.split("/")[0], "EXPEIKAO2");
            final String btnReservarCredito = mostrarBtnReserva(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                    request, response) ? "1" : "0";
            final String btnGrabarResol = mostrarBtnGrabarRes(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                    request, response) ? "1" : "0";
            final String btnGrabarPago = mostrarBtnGrabarPago(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                    request, response) ? "1" : "0";

            final DatosPasikusVO datosPasikusVO = new DatosPasikusVO(envioEika, impReserva,
                    idReserva, impanio1, impanio2, idIkus, expeIkad,
                    importeConcedido, pago1, pago2, idPago1,
                    idPago2, expeIkao1, expeIkao2, btnReservarCredito, btnGrabarResol, btnGrabarPago);
            request.setAttribute("datosPasikus", datosPasikusVO);
        } catch (final Exception ex) {
        }
        log.info("cargarDatosPasikus - End() " + new Date().toString());
        return "/jsp/extension/meikus01/pestanaDatosPasikus.jsp";
    }

    public void grabarDatosPasikus(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("=========== End en grabarDatosPasikus");

        String respuesta = null;

        try {
            String codMunicipio = request.getParameter("codMunicipio");
            String ejercicio = request.getParameter("ejercicio");
            String codProcedimiento = request.getParameter("codProcedimiento");
            String expHistorico = request.getParameter("expHistorico");
            final DatosExpedienteVO datosExpediente = new DatosExpedienteVO();

            if (StringOperations.stringNoNuloNoVacio(numExpediente) && StringOperations.stringNoNuloNoVacio(codProcedimiento) && StringOperations.stringNoNuloNoVacio(ejercicio)) {
                // Se recupera de la sesión el parámetro usuario con la información del usuario logueado
                HttpSession session = request.getSession();
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");

                if (usuarioVO != null) {
                    datosExpediente.setCodOrganizacion(usuarioVO.getOrgCod());
                    datosExpediente.setEjercicio(Integer.parseInt(ejercicio));
                    datosExpediente.setNumExpediente(numExpediente);
                    datosExpediente.setCodProcedimiento(codProcedimiento);
                    datosExpediente.setCodTramite(null);
                    datosExpediente.setDesdeJsp(ConstantesDatos.DESDE_JSP_FICHA_EXPEDIENTE);
                    datosExpediente.setConsultaCampos(null);
                    datosExpediente.setExpHistorico("true".equals(expHistorico) ? true : false);
                }
            }
            final String envioEika = request.getParameter("envioEika");
            final String impReserva = request.getParameter("impReserva");
            final String idReserva = request.getParameter("idReserva");
            final String impAnio1 = request.getParameter("impAnio1");
            final String impAnio2 = request.getParameter("impAnio2");
            final String idIkus = request.getParameter("idIkus");
            final String expeIkad = request.getParameter("expeIkad");
            final String importeConcedido = request.getParameter("importeConcedido");
            final String pago1 = request.getParameter("pago1");
            final String pago2 = request.getParameter("pago2");
            final String idPago1 = request.getParameter("idPago1");
            final String idPago2 = request.getParameter("idPago2");
            final String expeIkao1 = request.getParameter("expeIkao1");
            final String expeIkao2 = request.getParameter("expeIkao2");

            final DatosPasikusVO datosPasikusVO = new DatosPasikusVO(envioEika, impReserva, idReserva,
                    impAnio1, impAnio2, idIkus, expeIkad, importeConcedido, pago1, pago2,
                    idPago1, idPago2, expeIkao1, expeIkao2, null, null, null);

            int res = MeIkus01Manager.getInstance().guardarDatosPasikus(codOrganizacion, datosExpediente, codMunicipio, datosPasikusVO);

            respuesta = new Integer(res).toString();
        } catch (final Exception ex) {
            respuesta = "0";
        }
        log.info("grabarDatosPasikus - End() " + new Date().toString());
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
    }

    public void comprobarAutorizado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("===========Inicio en comprobarAutorizado");
        String respuesta = null;

        try {
            String codMunicipio = request.getParameter("codMunicipio");
            String ejercicio = request.getParameter("ejercicio");
            String codProcedimiento = request.getParameter("codProcedimiento");
            String expHistorico = request.getParameter("expHistorico");
            final DatosExpedienteVO datosExpediente = new DatosExpedienteVO();

            if (StringOperations.stringNoNuloNoVacio(numExpediente) && StringOperations.stringNoNuloNoVacio(codProcedimiento) && StringOperations.stringNoNuloNoVacio(ejercicio)) {
                // Se recupera de la sesión el parámetro usuario con la información del usuario logueado
                HttpSession session = request.getSession();
                UsuarioValueObject usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");

                if (usuarioVO != null) {
                    datosExpediente.setCodOrganizacion(usuarioVO.getOrgCod());
                    datosExpediente.setEjercicio(Integer.parseInt(ejercicio));
                    datosExpediente.setNumExpediente(numExpediente);
                    datosExpediente.setCodProcedimiento(codProcedimiento);
                    datosExpediente.setCodTramite(null);
                    datosExpediente.setDesdeJsp(ConstantesDatos.DESDE_JSP_FICHA_EXPEDIENTE);
                    datosExpediente.setConsultaCampos(null);
                    datosExpediente.setExpHistorico("true".equals(expHistorico) ? true : false);
                }
            }
            final String impReserva = request.getParameter("impReserva");
            final String impAnio1 = request.getParameter("impAnio1");
            final String impAnio2 = request.getParameter("impAnio2");

            int res = MeIkus01Manager.getInstance().comprobarAutorizado(codOrganizacion, datosExpediente, codMunicipio,
                    impReserva, impAnio1, impAnio2);

            respuesta = Integer.toString(res);
        } catch (final Exception ex) {
            respuesta = "0";
        }
        log.info("comprobarAutorizado - End() " + new Date().toString());
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
    }


}//class

