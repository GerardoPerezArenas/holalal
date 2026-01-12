package es.altia.flexia.integracion.moduloexterno.melanbide43.util;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.business.registro.InteresadoAnotacionVO;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.persistence.manual.OperacionesExpedienteDAO;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.sge.plugin.documentos.exception.AlmacenDocumentoTramitacionException;
import es.altia.agora.business.sge.plugin.documentos.vo.Documento;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.util.Utilities;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.exception.GestionAutomaticaKONTUException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexiaWS.tramitacion.WSTramitacionFlexiaImplSoapBindingImpl;
import es.altia.flexiaWS.tramitacion.bd.acceso.ExpedienteDAO;
import es.altia.flexiaWS.tramitacion.bd.datos.*;
import es.altia.flexiaWS.tramitacion.bd.util.Configuracion;
import es.altia.flexiaWS.tramitacion.bd.util.XMLTraductor;
import es.altia.util.StringUtils;
import es.altia.util.commons.DateOperations;
import es.altia.util.documentos.AlmacenDocumentoHelper;
import es.altia.util.xml.ExtractorDatosXMLIntegracion;
import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionBindingStub;
import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionServiceLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class KONTU_JobUtils {

    private final Logger log = LogManager.getLogger(KONTU_JobUtils.class);

    //Instancia
    private static KONTU_JobUtils instance = null;

    public static KONTU_JobUtils getInstance() {
        if (instance == null) {
            instance = new KONTU_JobUtils();
        }
        return instance;
    }

    public KONTU_JobUtils() {
    }

    /**
     * Inicia un expediente en Regexlan, a través del WS de tramitación interno de flexia, y en miCarpeta, ejecutando
     * las operaciones integradas en el trámite de inicio
     * @param nomUsuario
     * @param expedienteVO
     * @param infoConexionVO
     * @param documentosAnotacion
     * @param con
     * @return
     */
    public RespuestasTramitacionVO iniciarExpediente(String nomUsuario, ExpedienteVO expedienteVO, InfoConexionVO infoConexionVO,
                                                     Vector<RegistroValueObject> documentosAnotacion, Connection con) throws AlmacenDocumentoTramitacionException, GestionAutomaticaKONTUException {

        WSTramitacionFlexiaImplSoapBindingImpl wsTramitacionFlexiaImpl = null;
        RespuestasTramitacionVO respuestaLlamada = null;

        try {
            //Comprobamos si entre los documentos de la anotación hay un fichero FLX_DATOS_INTEGRACION_SOLICITUD para añadir otros datos al expediente
            RegistroValueObject documentoXML = recuperarXMLDatosIntegracion(expedienteVO.getOrganizacionUsuario(), documentosAnotacion);
            if (documentoXML != null && documentoXML.getDoc() != null && documentoXML.getDoc().length > 0) {
                log.debug("Existe el fichero  " + ConstantesDatos.NOMBRE_FICHERO_DATOS_INTEGRACION_SOLICITUD);
                //Existe el fichero. Extraemos de el los datos de campos suplementarios e interesados para añadirlos al expediente
                anadirDatosExtraDeXMLaExpediente(documentoXML, expedienteVO);
            } else {
                log.debug("No existe el fichero  " + ConstantesDatos.NOMBRE_FICHERO_DATOS_INTEGRACION_SOLICITUD);
                throw new GestionAutomaticaKONTUException(3, "No existe el fichero  " + ConstantesDatos.NOMBRE_FICHERO_DATOS_INTEGRACION_SOLICITUD + " en la anotación");
            }

            //Al iniciar expediente se inicia el tramite de inicio
            anadirDatosTramiteInicio(expedienteVO, con);

            wsTramitacionFlexiaImpl = new WSTramitacionFlexiaImplSoapBindingImpl();
            log.info("Vamos a llamar a iniciarExpedienteOperacion de WSTramitacionFlexia");
            respuestaLlamada = wsTramitacionFlexiaImpl.iniciarExpedienteOperacion(expedienteVO, infoConexionVO);

            if (respuestaLlamada.getSalida().getCodigoError().equals(0)) {
                String numExpediente = respuestaLlamada.getTramite().getNumeroExpediente();
                expedienteVO.setNumero(numExpediente);
                log.info("Expediente " + numExpediente + " iniciado en Regexlan");

                // Se registran las operaciones de expediente en bbdd
                registrarOperacionesInicio(infoConexionVO.getOrganizacion(), respuestaLlamada.getTramite().getCodTramite(), nomUsuario, expedienteVO, con);

                // Se comprueba si se ha ejecutado la operación de inicio del trámite: generarMisGestInicio(); es decir, si se ha iniciado el expediente en MiCarpeta
                try {
                    boolean iniciadoEnMiCarpeta = MeLanbide43DAO.getInstance().iniciadoEnMiCarpeta(numExpediente, con);
                    if(!iniciadoEnMiCarpeta) {
                        log.info("Expediente " + numExpediente + " NO iniciado en Mi Carpeta");
                        //Queremos registrar el error sin detener el proceso
                        ErrorBean errorB = new ErrorBean();
                        errorB.setIdError("JOB_GESTAUTOKONTU_ERR_005");
                        errorB.setMensajeError("Error en GestionAutomaticaKONTU: el expediente no ha podido ser iniciado en Mi Carpeta");
                        errorB.setSituacion("es.altia.flexia.integracion.moduloexterno.melanbide43.util.KONTU_JobUtils.iniciarExpediente()");
                        MeLanbide43Manager.grabarError(errorB, errorB.getMensajeError(), null, numExpediente);
                    } else {
                        log.info("Expediente " + numExpediente + " iniciado en Mi Carpeta");
                    }
                } catch (Exception e){
                    log.error("Se ha producido un error al comprobar si el expediente existe en enMiCarpeta", e);
                }

            } else {
                log.info("Expediente NO iniciado en Regexlan");
                throw new GestionAutomaticaKONTUException(4, "Ha ocurrido un error al llamar a iniciarExpedienteOperacion: expediente NO inicado en Regexlan");
            }
        } catch (AlmacenDocumentoTramitacionException e) {
            String mensaje = "Ha ocurrido un error al recuperar el contenido del fichero " + ConstantesDatos.NOMBRE_FICHERO_DATOS_INTEGRACION_SOLICITUD + " adjunto a la anotación: " + e.getMessage();
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(8, mensaje);
        } catch (RemoteException e) {
            String mensaje = "Ha ocurrido un error al llamar a iniciarExpedienteOperacion: " + e.getMessage();
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(4,mensaje);
        } catch (SQLException e) {
            String mensaje = "Ha ocurrido un error en anadirDatosTramiteInicio(): " + e.getMessage();
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(4,mensaje);
        } catch (GestionAutomaticaKONTUException e) {
            throw e;
        } catch (Exception e) {
            String mensaje;
            if(respuestaLlamada!=null && !respuestaLlamada.getSalida().getCodigoError().equals(0)) {
                mensaje = "Ha ocurrido un error en la llamada a iniciarExpedienteOperacion: " + respuestaLlamada.getSalida().getIncidencias();
            } else {
                mensaje = "Ha ocurrido un error iniciarExpediente(): " + e.getMessage();
            }
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(4,mensaje);
        }

        return respuestaLlamada;
    }

    public boolean asociarExpedienteRegistro(ExpedienteVO expedienteVO, RegistroValueObject registroVO, InfoConexionVO infoConexionVO) throws GestionAutomaticaKONTUException {
        log.info(String.format("asociarExpedienteRegistro() - expediente %s; anotación %d/%d", expedienteVO.getNumero(), registroVO.getAnoReg(), registroVO.getNumReg()));
        URL urlEndpoint = null;
        WSTramitacionBindingStub wsTramitacionCliente = null;

        try {
            urlEndpoint = new URL(ConfigurationParameter.getParameter("WSTramitacion_EndPoint", "common"));
            wsTramitacionCliente = (WSTramitacionBindingStub) new WSTramitacionServiceLocator().getWSTramitacionPort(urlEndpoint);

            log.info("Vamos a llamar a asociarExpRegistro() de WSTramitacion");
            es.altia.webservice.client.tramitacion.ws.wto.RespuestasTramitacionVO respuestaLlamada =
                    wsTramitacionCliente.asociarExpRegistro(expedienteFlexiaWS2ExpedienteTramClient(expedienteVO),
                            registroVO2RegistroAsociadoVO(registroVO), infoConFlexiaWS2InfoConTramClient(infoConexionVO));
            if (respuestaLlamada.getStatus() == 0) {
                log.info(String.format("Expediente %s y anotación %d/%d asociados correctamente", expedienteVO.getNumero(), registroVO.getAnoReg(), registroVO.getNumReg()));
                return true;
            } else {
                String mensaje = String.format("Expediente %s y anotación %d/%d NO asociados", expedienteVO.getNumero(), registroVO.getAnoReg(), registroVO.getNumReg());
                log.info(mensaje);
                throw new GestionAutomaticaKONTUException(6, mensaje, expedienteVO.getNumero());
            }

        } catch (MalformedURLException e) {
            String mensaje = "Ha ocurrido un error al llamar a asociarExpRegistro(): " + e.getMessage();
            log.error(mensaje);
            throw new GestionAutomaticaKONTUException(6, mensaje, expedienteVO.getNumero());
        } catch (ServiceException e) {
            String mensaje = "Ha ocurrido un error al llamar a asociarExpRegistro(): " + e.getMessage();
            log.error(mensaje);
            throw new GestionAutomaticaKONTUException(6, mensaje, expedienteVO.getNumero());
        } catch (RemoteException e) {
            String mensaje = "Ha ocurrido un error al llamar a asociarExpRegistro(): " + e.getMessage();
            log.error(mensaje);
            throw new GestionAutomaticaKONTUException(6, mensaje, expedienteVO.getNumero());
        } catch (Exception e) {
            String mensaje = "Ha ocurrido un error en asociarExpedienteRegistro(): " + e.getMessage();
            log.error(mensaje);
            throw new GestionAutomaticaKONTUException(6, mensaje, expedienteVO.getNumero());
        }
    }

    public boolean tramitarExpediente(ExpedienteVO expedienteVO, InfoConexionVO infoConexionVO, Connection con) throws GestionAutomaticaKONTUException {
        boolean exito = false;
        String codigoCampo = "";
        String valorCampo = null;
        TramiteVO tramiteVO = null;
        IModuloIntegracionExternoCamposFlexia iIntegracionExterna = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO campo = null;

        try {
            // Se recupera el valor del campo suplementario VALSEDE
            codigoCampo = ConfigurationParameter.getParameter("codCampo_VALIDAIBAN", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            campo = iIntegracionExterna.getCampoSuplementarioExpediente(infoConexionVO.getOrganizacion(), expedienteVO.getEjercicio(), expedienteVO.getNumero(),
                    expedienteVO.getProcedimiento(), codigoCampo, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);
            if (campo.getStatus() == 0) {
                valorCampo = campo.getCampoSuplementario().getValorDesplegable();
                log.debug(String.format("Existe valor para el campo %s en el expediente %s -> %s", codigoCampo, expedienteVO.getNumero(), valorCampo));

                //Se crea el objeto TramiteVO con los datos comunes a todos los avances en la tramitacion que se tienen que realizar
                tramiteVO = crearTramiteVO(expedienteVO);

                String codTramOrigen;
                String codTramDestino;
                if ("N".equals(valorCampo)) { // Si el valor del campo es N: Avanzar del trámite 10 al 20
                    codTramOrigen = ConfigurationParameter.getParameter("codTramKONTU_codVis10", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    codTramDestino = ConfigurationParameter.getParameter("codTramKONTU_codVis20", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    exito = avanzarTramite(tramiteVO, codTramOrigen, codTramDestino, "no", expedienteVO, infoConexionVO);
                    if(!exito) {
                        String mensaje = String.format("No se ha podido avanzar el expediente del trámite %s al %s", codTramOrigen, codTramDestino);
                        log.debug(mensaje);
                        throw new GestionAutomaticaKONTUException(7, mensaje, expedienteVO.getNumero());
                    }
                } else { // Si el valor del campo es S:
                    // 1. Avanzar del trámite 10 al 30
                    codTramOrigen = ConfigurationParameter.getParameter("codTramKONTU_codVis10", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    codTramDestino = ConfigurationParameter.getParameter("codTramKONTU_codVis30", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    exito = avanzarTramite(tramiteVO, codTramOrigen, codTramDestino, "si" , expedienteVO, infoConexionVO);

                    // El siguiente paso depende del exito de este, si no se avanza entonces se notifica el error
                    if(exito) {
                        // 2. Grabar el IBAN (campo IBAN del expediente) en el campo suplementario del tercero
                        int codTerceroPrincipal = ExpedienteDAO.getInstance().getCodInteresadoPrincipal(expedienteVO, con);
                        SalidaIntegracionVO salida = grabarIBANCampoTercero(iIntegracionExterna, expedienteVO, codTerceroPrincipal);

                        // El siguiente paso depende del exito de este, si no se graba entonces se notifica el error
                        if (salida != null && salida.getStatus() == 0) {
                            // 3. Finalizar el trámite 30 y con ello el expediente.
                            codTramOrigen = ConfigurationParameter.getParameter("codTramKONTU_codVis30", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                            exito = avanzarTramite(tramiteVO, codTramOrigen, null, "", expedienteVO, infoConexionVO);
                            if(!exito){
                                String mensaje = String.format("No se ha podido avanzar el trámite %s que finaliza el expediente.", codTramOrigen);
                                log.debug(mensaje);
                                throw new GestionAutomaticaKONTUException(7, mensaje, expedienteVO.getNumero());
                            }
                        } else {
                            String mensaje = "No se ha podido grabar el valor de IBAN en el campo suplementario del tercero.";
                            if (salida != null && salida.getStatus() != 0) {
                                mensaje += " " + salida.getDescStatus();
                            }
                            log.debug(mensaje);
                            throw new GestionAutomaticaKONTUException(7, mensaje, expedienteVO.getNumero());
                        }
                    } else {
                        String mensaje = String.format("No se ha podido avanzar el expediente del trámite %s al %s", codTramOrigen, codTramDestino);
                        log.debug(mensaje);
                        throw new GestionAutomaticaKONTUException(7, mensaje, expedienteVO.getNumero());
                    }
                }
            } else {
                String mensaje = String.format("Ha ocurrido un error al recuperar el valor del campo %s en el expediente %s: %s", codigoCampo, expedienteVO.getNumero(), campo.getDescStatus());
                log.error(mensaje);
                throw new GestionAutomaticaKONTUException(7, mensaje, expedienteVO.getNumero());
            }
        } catch (GestionAutomaticaKONTUException e) {
            log.error(String.format("Ha ocurrido un error en tramitarExpediente(): %s", e.getDescripcionError()), e);
            throw e;
        } catch (Exception e) {
            String mensaje = String.format("Ha ocurrido un error en tramitarExpediente(): %s", e.getMessage());
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(7, mensaje, expedienteVO.getNumero());
        }

        //Si se llega a este punto es que se ha podido avanzar sin errores
        return true;
    }

    /**
     * Crea un objeto ExpedienteVO de WSTramitacionFlexia y lo rellena con datos del objeto de registro: RegistroValueObject
     * @param codUsuario
     * @param codOrganizacion
     * @param registroVO
     * @param con
     * @return
     * @throws NumberFormatException
     * @throws TechnicalException
     */
    public ExpedienteVO crearExpedienteVO(int codUsuario, String codOrganizacion, RegistroValueObject registroVO, Connection con) throws GestionAutomaticaKONTUException {
        log.info(String.format("crearExpedienteVO() init - ejercicio: %d; procedimiento: %s", registroVO.getAnoReg(), registroVO.getCodProcedimiento()));
        ExpedienteVO expedienteVO = new ExpedienteVO();

        try {
            expedienteVO.setUor(registroVO.getIdUndTramitad());
            expedienteVO.setUorTramiteInicio(registroVO.getIdUndTramitad());
            expedienteVO.setUsuario(String.valueOf(codUsuario));
            expedienteVO.setAsunto(registroVO.getAsunto());
            expedienteVO.setObservaciones(registroVO.getObservaciones());
            expedienteVO.setEjercicio(String.valueOf(registroVO.getAnoReg()));
            expedienteVO.setProcedimiento(registroVO.getCodProcedimiento());
            expedienteVO.setOrganizacionUsuario(codOrganizacion);
            expedienteVO.setTercero(crearListaInteresadosExpediente(registroVO.getListaInteresados()));
            //Buscamos el rol por defecto de los expedientes de este procedimiento y lo guardamos en la propiedad 'rol' del expediente
            int COD_ROL_DEFECTO_PROCEDIMIENTO = TramitacionExpedientesDAO.getInstance().getCodRolPorDefecto(expedienteVO.getProcedimiento(), con);
            expedienteVO.setRol(String.valueOf(COD_ROL_DEFECTO_PROCEDIMIENTO));
        } catch(NumberFormatException ex){
            throw new GestionAutomaticaKONTUException(2, ex.getMessage());
        } catch (TechnicalException ex){
            throw new GestionAutomaticaKONTUException(2, ex.getMessage());
        }catch (Exception ex){
            throw new GestionAutomaticaKONTUException(2, ex.getMessage());
        }
        return expedienteVO;

    }

    /**
     * Crea un objeto InfoConexionVO de WSTramitacionFlexia con la aplicación permitida por el servicio y el código de organización que permite obtener una conexión de bd
     * @param codOrganizacion
     * @return
     * @throws NumberFormatException
     */
    public InfoConexionVO crearInfoConexionVO(int codOrganizacion) throws GestionAutomaticaKONTUException {
        InfoConexionVO infoConexionVO = new InfoConexionVO();
        String aplicacionPermitida;

        try {
            aplicacionPermitida = getAplicacionPermitida();
            log.debug(String.format("crearInfoConexionVO() - aplicacion: %s; organizacion: %d", aplicacionPermitida, codOrganizacion));
            infoConexionVO.setAplicacion(getAplicacionPermitida());
            infoConexionVO.setOrganizacion(String.valueOf(codOrganizacion));
        } catch (NumberFormatException ex){
            throw new GestionAutomaticaKONTUException(2, ex.getMessage());
        } catch (Exception ex){
            throw new GestionAutomaticaKONTUException(2, ex.getMessage());
        }
        return infoConexionVO;

    }

    /**
     * Crea una lista de objetos InteresadoVOde WSTramitacionFlexia y la rellena con los datos de la lista de objetos InteresadoAnotacionVO del registro
     * @param listaInteresadosReg
     * @return
     */
    private InteresadoVO[] crearListaInteresadosExpediente(List<InteresadoAnotacionVO> listaInteresadosReg) throws Exception {
        log.debug(String.format("crearListaInteresadosExpediente() - Se añaden al expediente los %d interesados de la anotación", listaInteresadosReg.size()));
        InteresadoVO[] listaInteresadosExp = new InteresadoVO[listaInteresadosReg.size()];
        for (int i = 0; i < listaInteresadosReg.size(); i++) {
            InteresadoAnotacionVO interesadoRegistro = listaInteresadosReg.get(i);
            InteresadoVO interesadoExpediente = new InteresadoVO();

            interesadoExpediente.setCodigo(String.valueOf(interesadoRegistro.getCodTercero()));
            interesadoExpediente.setVersion(String.valueOf(interesadoRegistro.getNumVersion()));
            interesadoExpediente.setRol(interesadoRegistro.getCodigoRol());
            interesadoExpediente.setCoddomicilio(String.valueOf(interesadoRegistro.getCodDomicilio()));

            listaInteresadosExp[i] = interesadoExpediente;
        }

        return listaInteresadosExp;
    }

    /**
     * Obtiene una de las aplicaciones permitidas indicadas en el properties del servicio
     * @return
     */
    private String getAplicacionPermitida() throws Exception {
        String todasApl = Configuracion.getAplicacion();
        String apl = null;

        if (todasApl.contains(";")) {
            apl = todasApl.substring(0, todasApl.indexOf(';'));
        } else {
            apl = todasApl;
        }
        return apl;
    }

    /**
     * Convierte un objeto InfoConexionVO de WSTramitacionFlexia en el objeto InfoConexionVO del cliente de WSTramitacion
     * @param infoConexionVO
     * @return
     */
    private es.altia.webservice.client.tramitacion.ws.wto.InfoConexionVO infoConFlexiaWS2InfoConTramClient(InfoConexionVO infoConexionVO) throws Exception {
        es.altia.webservice.client.tramitacion.ws.wto.InfoConexionVO infoConexionWTO =
                new es.altia.webservice.client.tramitacion.ws.wto.InfoConexionVO();
        try {
            infoConexionWTO.setAplicacion(infoConexionVO.getAplicacion());
            infoConexionWTO.setOrganizacion(infoConexionVO.getOrganizacion());
        } catch (Exception e){
            log.error("Ha ocurrido un error al convertir el objeto InfoConexionVO en el esperado por WSTramitacion: " + e.getMessage(), e);
            throw e;
        }

        return infoConexionWTO;

    }

    /**
     * Convierte un objeto ExpedienteVO de WSTramitacionFlexia en el objeto ExpedienteVO del cliente de WSTramitacion
     * @param expedienteVO
     * @return
     */
    private es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO expedienteFlexiaWS2ExpedienteTramClient(ExpedienteVO expedienteVO) throws Exception {
        es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO expedienteWTO =
                new es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO();
        es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO idExpedienteWTO =
                new es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO();
        try {
            idExpedienteWTO.setNumeroExpediente(expedienteVO.getNumero());
            idExpedienteWTO.setEjercicio(Integer.parseInt(expedienteVO.getEjercicio()));
            idExpedienteWTO.setProcedimiento(expedienteVO.getProcedimiento());

            expedienteWTO.setIdExpedienteVO(idExpedienteWTO);
            expedienteWTO.setUor((Integer.parseInt(expedienteVO.getUor())));
            expedienteWTO.setCampos(expedienteVO.getCampos());
            expedienteWTO.setUorTramiteInicio(Integer.parseInt(expedienteVO.getUorTramiteInicio()));
            expedienteWTO.setUsuario(Integer.parseInt(expedienteVO.getUsuario()));
            expedienteWTO.setAsunto(expedienteVO.getAsunto());
            expedienteWTO.setObservaciones(expedienteVO.getObservaciones());
        } catch (NumberFormatException e){
            log.error("Ha ocurrido un error al convertir el objeto ExpedienteVO en el esperado por WSTramitacion: " + e.getMessage(), e);
            throw e;
        } catch (Exception e){
            log.error("Ha ocurrido un error al convertir el objeto ExpedienteVO en el esperado por WSTramitacion: " + e.getMessage(), e);
            throw e;
        }

        return expedienteWTO;

    }

    /**
     * Convierte un objeto RegistroValueObject, con los datos de registro en flexia, en un objeto RegistroAsociadoVO del cliente de WSTramitacion
     * @param registroVO
     * @return
     */
    private es.altia.webservice.client.tramitacion.ws.wto.RegistroAsociadoVO registroVO2RegistroAsociadoVO(RegistroValueObject registroVO) throws Exception {
        es.altia.webservice.client.tramitacion.ws.wto.RegistroAsociadoVO registroAsociadoVO =
                new es.altia.webservice.client.tramitacion.ws.wto.RegistroAsociadoVO();
        try {
            registroAsociadoVO.setCodDepartamento(registroVO.getIdentDepart());
            registroAsociadoVO.setUorRegistro(registroVO.getUnidadOrgan());
            registroAsociadoVO.setTipoEntrada(registroVO.getTipoReg());
            registroAsociadoVO.setEjercicioAnotacion(registroVO.getAnoReg());
            long valorLong = registroVO.getNumReg();
            if (valorLong >= Integer.MIN_VALUE && valorLong <= Integer.MAX_VALUE) {
                registroAsociadoVO.setNumAsiento((int) valorLong);
            } else {
                log.warn("El valor de la propiedad 'numReg' de RegistroValueObject (objeto origen) es de tipo long " +
                        "y está fuera del rango del tipo int, tipo de la propiedad 'numAsiento' de RegistroAsociadoVO (objeto destino)");
            }
        }catch(Exception e){
            log.error("Ha ocurrido un error al convertir el objeto RegistroValueObject en el esperado por WSTramitacion: " + e.getMessage(), e);
            throw e;
        }

        return registroAsociadoVO;
    }

    private void registrarOperacionesInicio(String codOrganizacion, String codTramiteInicio, String nomUsuario, ExpedienteVO expedienteVO, Connection con) {
        GeneralValueObject datos = null;

        try {
            datos = new GeneralValueObject();
            datos.setAtributo("codMunicipio", codOrganizacion);
            datos.setAtributo("codProcedimiento", expedienteVO.getProcedimiento());
            datos.setAtributo("ejercicio", expedienteVO.getEjercicio());
            datos.setAtributo("numero", expedienteVO.getNumero());
            datos.setAtributo("codUOR", expedienteVO.getUor());
            datos.setAtributo("asunto", expedienteVO.getAsunto());
            datos.setAtributo("observaciones", expedienteVO.getObservaciones());
            datos.setAtributo("codTramite", codTramiteInicio);
            datos.setAtributo("ocurrTramite", "1");
            datos.setAtributo("usuario", expedienteVO.getUsuario());
            datos.setAtributo("fechaInicioTramite", DateOperations.toString(new GregorianCalendar(), DateOperations.LATIN_DATE_24HOUR_NOSECONDS));
            datos.setAtributo("nomUsuario", nomUsuario);
            datos.setAtributo("nombreUsuario", nomUsuario);
            datos.setAtributo("descUOR", "");
            datos.setAtributo("localizacion", "");
            datos.setAtributo("codLocalizacion", "");
            datos.setAtributo("tipoAlta", "");
            datos.setAtributo("nomTramite", "");
            datos.setAtributo("origenLlamada", ConstantesDatos.ORIGEN_LLAMADA_BATCH_KONTU);

            OperacionesExpedienteDAO.getInstance().registrarAltaExpediente(datos, con);
            OperacionesExpedienteDAO.getInstance().registrarIniciarTramite(datos, false, con);
        } catch (Exception e) {
            log.error("No se han podido registrar las operaciones de inicio. Error: " + e.getMessage());
        }
    }

    /**
     * Realiza el avance en la tramitacion, cerrando un trámite inicado y abriendo otro si así se indica.
     * @param tramiteVO
     * @param codTramOrigen
     * @param codTramDestino
     * @param expedienteVO
     * @param infoConexionVO
     * @return
     */
    private boolean avanzarTramite(TramiteVO tramiteVO, String codTramOrigen, String codTramDestino, String respuesta, ExpedienteVO expedienteVO, InfoConexionVO infoConexionVO) throws GestionAutomaticaKONTUException {
        log.info(String.format("avanzarTramite() - Avance del expediente %s del trámite de cod interno %s al de cod interno %s", expedienteVO.getNumero(), codTramOrigen, codTramDestino));
        final String ORIGEN_LLAMADA = ConstantesDatos.ORIGEN_LLAMADA_BATCH_KONTU;
        WSTramitacionFlexiaImplSoapBindingImpl wsTramitacionFlexiaImpl = null;
        SalidaBoolean respuestaLlamada = null;
        CondicionFinalizacionVO condicionFinalizacionVO = null;

        try {
            wsTramitacionFlexiaImpl = new WSTramitacionFlexiaImplSoapBindingImpl();

            // Construimos el objeto CondicionFinalizacionVO con los datos para el avance
            String tipoFinalizacion = (codTramDestino != null) ? "P" : "F";
            condicionFinalizacionVO = crearCondFinalizacionVO(tipoFinalizacion, respuesta, codTramDestino);

            //Guardamos en el objeto TramiteVO el codigo del tramite que se finaliza
            // La ocurrencia es siempre 1: el expediente a tramitar se ha iniciado en un paso anterior de este mismo proceso batch
            tramiteVO.setCodTramite(codTramOrigen);
            tramiteVO.setOcurrenciaTramite("1");

            // Realizamos la llamada al web service
            log.info("Vamos a llamar a finalizarTramiteOperacion() de WSTramitacionFlexia");
            respuestaLlamada = wsTramitacionFlexiaImpl.finalizarTramiteOperacion(expedienteVO, tramiteVO, condicionFinalizacionVO, ORIGEN_LLAMADA, infoConexionVO);
            if(respuestaLlamada.getCodigoError().equals(0)){
                log.info(String.format("El expediente %s ha sido tramitado correctamente", expedienteVO.getNumero()));
            } else {
                log.info(String.format("El expediente %s NO se ha podido ser tramitado: %s", expedienteVO.getNumero(), respuestaLlamada.getIncidencias()));

            }
        } catch (RemoteException e) {
            String mensaje = "Ha ocurrido un error en la llamada a finalizarTramiteOperacion: " + e.getMessage();
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(7, mensaje, expedienteVO.getNumero());
        } catch (GestionAutomaticaKONTUException e) {
            throw new GestionAutomaticaKONTUException(7, e.getDescripcionError(), expedienteVO.getNumero());
        } catch (Exception e) {
            String mensaje;
            if(respuestaLlamada!=null && !respuestaLlamada.getCodigoError().equals(0)) {
                mensaje = "Ha ocurrido un error en la llamada a finalizarTramiteOperacion: " + respuestaLlamada.getIncidencias();
            } else {
                mensaje = "Ha ocurrido un error avanzarTramite(): " + e.getMessage();
            }
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(7, mensaje, expedienteVO.getNumero());
        }

        return respuestaLlamada!=null && respuestaLlamada.getCodigoError().equals(0);
    }

    /**
     * Crea un objeto TramiteVO de WSTramitacionFlexia y lo rellena con datos del objeto ExpedienteVO, también de WSTramitacionFlexia
     * @param expedienteVO
     * @return
     */
    private TramiteVO crearTramiteVO(ExpedienteVO expedienteVO) throws GestionAutomaticaKONTUException {
        TramiteVO tramiteVO = new TramiteVO();

        try {
            tramiteVO.setUsuario(expedienteVO.getUsuario());
            tramiteVO.setUnidadTramitadoraTram(expedienteVO.getUorTramiteInicio());
            tramiteVO.setEjercicio(expedienteVO.getEjercicio());
            tramiteVO.setNumeroExpediente(expedienteVO.getNumero());
        } catch (Exception e){
            String mensaje = "Ha ocurrido un error en crearTramiteVO(): " + e.getMessage();
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(7, mensaje, expedienteVO.getNumero());
        }

        return tramiteVO;

    }

    /**
     * Crea un objeto CondicionFinalizacionVO de WSTramitacionFlexia con los datos necesarios para realizar el avance en la tramitación requerido
     * @param tipoFinalizacion
     * @param codTramite
     * @return
     */
    private CondicionFinalizacionVO crearCondFinalizacionVO(String tipoFinalizacion, String respuesta, String codTramite) throws GestionAutomaticaKONTUException{
        CondicionFinalizacionVO condicionFinalizacionVO = new CondicionFinalizacionVO();
        FlujoFinalizacionVO flujoFinalizacionVO = null;
        TramiteVO[] listaRespuesta = null;

        try {
            if (tipoFinalizacion.equals("P")) {
                listaRespuesta = new TramiteVO[1];
                flujoFinalizacionVO = new FlujoFinalizacionVO();

                TramiteVO tramite = new TramiteVO();
                tramite.setCodTramite(codTramite);
                tramite.setOcurrenciaTramite("1");
                listaRespuesta[0] = tramite;
                flujoFinalizacionVO.setListaRespuesta(listaRespuesta);
                flujoFinalizacionVO.setTipoApertura(1);// Obligatoriedad de la salida (1 = obligatorio)

            }

            condicionFinalizacionVO.setTipoFinalizacion(tipoFinalizacion);
            if ("si".equals(respuesta)) {
                condicionFinalizacionVO.setFlujoSI(flujoFinalizacionVO);
            } else if ("no".equals(respuesta)) {
                condicionFinalizacionVO.setFlujoNO(flujoFinalizacionVO);
            }
            condicionFinalizacionVO.setRespuesta(respuesta);
        } catch (Exception e){
            String mensaje = "Ha ocurrido un error en crearCondFinalizacionVO: " + e.getMessage();
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(7, mensaje);
        }

        return condicionFinalizacionVO;

    }

    private RegistroValueObject recuperarXMLDatosIntegracion(String codOrganizacion, Vector<RegistroValueObject> documentosAnotacion) throws AlmacenDocumentoTramitacionException {
        RegistroValueObject documento = null;

        for (RegistroValueObject doc : documentosAnotacion) {
            if (doc.getNombreDoc().equalsIgnoreCase(ConstantesDatos.NOMBRE_FICHERO_DATOS_INTEGRACION_SOLICITUD)) {
                //Recupera el contenido del documento mediante el plugin
                AlmacenDocumentoHelper almacenDocumentoHelper = new AlmacenDocumentoHelper(Integer.parseInt(codOrganizacion));
                Documento iDocumento = null;
                try {
                    iDocumento = almacenDocumentoHelper.getDocumentoRegistro(doc, new MeLanbide43BDUtil().getParamsConexBD(codOrganizacion));
                } catch (AlmacenDocumentoTramitacionException e) {
                    log.error("Ha ocurrido un error al recuperar el contenido del fichero " + ConstantesDatos.NOMBRE_FICHERO_DATOS_INTEGRACION_SOLICITUD, e);
                    throw e;
                }

                if (iDocumento != null && iDocumento.getFichero() != null && iDocumento.getFichero().length > 0) {
                    doc.setDoc(iDocumento.getFichero());
                }

                documento = doc;
            }
        }

        return documento;
    }

    private void anadirDatosExtraDeXMLaExpediente(RegistroValueObject xml, ExpedienteVO expedienteVO) throws GestionAutomaticaKONTUException {
        ExtractorDatosXMLIntegracion lector = new ExtractorDatosXMLIntegracion(xml.getNombreDoc(), xml.getDoc());
        List<GeneralValueObject> listaCSExpediente = null;
        List<GeneralValueObject> listaCSTramite = null;
        List<GeneralValueObject> listaInteresados = null;

        try {
            //Recuperamos los datos extra (de campos suplementarios, de interesados, y de extension) del documento xml
            Map<String, List<GeneralValueObject>> datosFichero = lector.leerDatosFichero();
            listaCSExpediente = datosFichero.get("CAMPOS_EXPEDIENTE");
            listaCSTramite = datosFichero.get("CAMPOS_TRAMITE");
            listaInteresados = datosFichero.get("DATOS_INTERESADOS");
            //TODO obtener datos de ME

            //Añadimos al expediente los datos de campos suplementarios seteando la propiedad 'campos'
            if ((listaCSExpediente != null && !listaCSExpediente.isEmpty()) || (listaCSTramite != null && !listaCSTramite.isEmpty())) {
                log.debug("El fichero XML tiene etiquetas de campos suplementarios de expediente y/o trámite.");
                anadirDatosCamposSuplementarios(listaCSExpediente, listaCSTramite, expedienteVO);
            } else {
                log.debug("El fichero XML no tiene etiquetas de campos suplementarios.");
            }

            //Añadimos al expediente los datos de interesados, excepto los campos suplementarios de estos
            if (listaInteresados != null && !listaInteresados.isEmpty()) {
                log.debug("El fichero XML tiene etiquetas de interesados.");
                anadirDatosInteresados(listaInteresados, expedienteVO);
            } else {
                log.debug("El fichero XML no tiene etiquetas de interesados.");
            }
        } catch (Exception e){
            String mensaje = "Ha ocurrido un error en anadirDatosExtraDeXMLaExpediente(): " + e.getMessage();
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(4,mensaje);
        }
    }

    private void anadirDatosCamposSuplementarios(List<GeneralValueObject> listaCamposExpediente, List<GeneralValueObject> listaCamposTramite, ExpedienteVO expedienteVO) {
        String xmlCampos = XMLTraductor.generarXMLdeCSfromFicheroDatosIntegracion(listaCamposExpediente, listaCamposTramite);
        log.debug(String.format("anadirDatosCamposSuplementarios() - xml: %s", xmlCampos));
        expedienteVO.setCampos(xmlCampos);
    }

    private void anadirDatosInteresados(List<GeneralValueObject> listaDatosInteresados, ExpedienteVO expedienteVO) throws Exception {
        //Quitamos de la lista de interesados del xml los que tienen datos no validos
        log.debug("Tamano lista interesados antes de filtrar validos: " + listaDatosInteresados.size());
        listaDatosInteresados = filtrarInteresadosValidos(Integer.parseInt(expedienteVO.getRol()), listaDatosInteresados);
        log.debug("Tamano lista interesados despues de filtrar validos: " + listaDatosInteresados.size());
        if(!listaDatosInteresados.isEmpty()){
            //Tratamos los datos de los interesados generando el array de InteresadoVO que espera el expediente
            InteresadoVO[] intExpFromReg = expedienteVO.getTercero();
            InteresadoVO[] intExpFromXML = tratarDatosInteresados(listaDatosInteresados, expedienteVO.getOrganizacionUsuario());
            int tamano = intExpFromReg.length;
            if(intExpFromXML != null && intExpFromXML.length != 0) {
                tamano += intExpFromXML.length;
            }
            InteresadoVO[] interesadosExpediente = new InteresadoVO[tamano];

            //Rellenamos el array de interesados uniendo los dos que tenemos
            for (int i = 0; i < intExpFromReg.length; i++) {
                interesadosExpediente[i] = intExpFromReg[i];
            }

            for (int i = 0; i < intExpFromXML.length; i++) {
                interesadosExpediente[intExpFromReg.length + i] = intExpFromXML[i];
            }

            expedienteVO.setTercero(interesadosExpediente);

        }
    }

    private InteresadoVO[] tratarDatosInteresados(List<GeneralValueObject> listaInteresados, String codOrganizacion) throws Exception{

        GeneralValueObject gVOInteresado;
        GeneralValueObject gVODatosInteresado;
        List<GeneralValueObject> listaCamposInteresado;
        DomicilioVO domicilio = null;
        InteresadoVO interesado = null;
        InteresadoVO[] listaInteresadosVO = null;
        int resultado = -1;

        try {
            if(!listaInteresados.isEmpty()) {
                listaInteresadosVO = new InteresadoVO[listaInteresados.size()];
                for (int i = 0; i < listaInteresados.size(); i++) {
                    gVOInteresado = listaInteresados.get(i);
                    gVODatosInteresado = (GeneralValueObject) gVOInteresado.getAtributo("datosInteresado");
                    listaCamposInteresado = (List<GeneralValueObject>) gVOInteresado.getAtributo("datosSuplementarios");

                    //Extraemos del xml los datos de interesado y rellenamos un objeto InteresadoVO
                    interesado = tratarDatosSoloInteresado(gVODatosInteresado);
                    //Extraemos del xml los datos de domicilio del interesado y rellenamos un objeto DomicilioVO. Asignamos el domicilio al interesado.
                    domicilio = tratarDatosSoloDomicilio(codOrganizacion, gVODatosInteresado);
                    interesado.setDomicilio(domicilio);

                    //Generamos la cadena de campos suplementarios del interesado a partir de lo recibido en el xml
                    if(listaCamposInteresado!=null && !listaCamposInteresado.isEmpty()){
                        String xmlCampos = XMLTraductor.generarXMLdeCSfromFicheroDatosIntegracion(listaCamposInteresado);
                        log.debug(String.format("tratarDatosInteresados() - xml campos interesado %d: %s", i, xmlCampos));
                        interesado.setCampos(xmlCampos);
                    }

                    listaInteresadosVO[i] = interesado;
                }
            }


        } catch (Exception e) {
            log.debug("Ha ocurrido un error en KONTU_JobUtils.tratarDatosInteresados: " + e.getMessage());
            e.printStackTrace();
        }

        log.debug("Salimos de KONTU_JobUtils.tratarDatosInteresados devolviendo: " + resultado);
        return listaInteresadosVO;
    }

    private InteresadoVO tratarDatosSoloInteresado(GeneralValueObject gVODatosInteresado) throws Exception {
        InteresadoVO interesadoVO = new InteresadoVO();

        String documento = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NIF)).trim();
        String nombre = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOMBRE)).trim();
        String apellido1 = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_APELLIDO1);
        StringUtils.trimNotNull(apellido1);

        String apellido2 = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_APELLIDO2);
        StringUtils.trimNotNull(apellido2);

        String telefono = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_TELEFONO);
        StringUtils.trimNotNull(telefono);

        String email = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_EMAIL);
        StringUtils.trimNotNull(email);

        String rol = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ROL)).trim();
        String notifElectronica = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOTIFICACIONELECTRONICA);
        StringUtils.trimNotNull(notifElectronica);
        //El campo codigo de via no se recupera del XML porque no se va utilizar al ser diferentes los codigos de vía que
        //manejan ellos con los que tenemos nosotros en Flexia

        //Recupera el tipo de documento
        int tipoDoc = 0;
        if (Utilities.validaNIF(documento) && nombre != null && apellido1 != null && apellido1.length() > 0) {
            // Se trata de un tercero cuyo documento es un NIF, ya que al menos tiene el primer apellido
            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_NIF;
        } else if (Utilities.validaNIF(documento) && nombre != null && (apellido1 == null || apellido1.length() == 0) && (apellido2 == null || apellido2.length() == 0)) {
            // Si el documento es un NIF, y sólo se ha enviado un nombre sin apellidos.
            // Se entiende que se trata de un CIF, ya que en Flexia por configuración se permite el alta
            // de cif que sean un NIF.
            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_CIF;
        } else if (Utilities.validaCIF(documento)) {
            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_CIF;
        } else if (Utilities.validarNie(documento)) {
            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_NIE;
        } else if (documento == null || "0".equals(documento) || "".equals(documento)) {
            documento = "";
            tipoDoc = ConstantesDatos.TIPO_DOCUMENTO_SIN_DOCUMENTO;
        }

        //Rellenamos el objeto InteresadoVO con los datos del xml
        interesadoVO.setCodigo("-1"); //Indicamos que se está ejecutando el job de KONTU y el interesado viene del XML
        interesadoVO.setDoc(documento);
        interesadoVO.setTipoDoc(String.valueOf(tipoDoc));
        interesadoVO.setNombre(nombre);
        interesadoVO.setAp1(apellido1);
        if (apellido2 != null && apellido2.length() > 0) {
            interesadoVO.setAp2(apellido2);
        }
        if (email != null && email.length() > 0) {
            interesadoVO.setEmail(email);
        }
        if (telefono != null && telefono.length() > 0) {
            interesadoVO.setTelefono(telefono);
        }
        interesadoVO.setRol(Integer.parseInt(rol));

        return interesadoVO;
    }

    private DomicilioVO tratarDatosSoloDomicilio(String codOrganizacion, GeneralValueObject gVODatosInteresado) throws Exception {
        Config m_ConfigCommon = ConfigServiceHelper.getConfig("common");
        DomicilioVO domicilio = new DomicilioVO();

        String pais = ConstantesDatos.CODIGO_PAIS_ESPAÑA;
        String provincia = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PROVINCIA));
        if (provincia != null) {
            provincia = provincia.trim();
        } else {
            provincia = m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.CODIGO_PROVINCIA_DESCONOCIDO);
        }

        String municipio = ((String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_MUNICIPIO));
        if (municipio != null) {
            municipio = municipio.trim();
        } else {
            municipio = m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.CODIGO_MUNICIPIO_DESCONOCIDO);
        }
        String nombreVia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOMBREVIA);
        if (nombreVia != null) {
            nombreVia = nombreVia.trim();
        } else {
            nombreVia = m_ConfigCommon.getString(codOrganizacion + ConstantesDatos.DESCRIPCION_VIA_DESCONOCIDA);
        }

        String emplazamiento = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_EMPLAZAMIENTO);
        StringUtils.trimNotNull(emplazamiento);

        String tipoVia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_TIPOVIA);
        StringUtils.trimNotNull(tipoVia);

        String letraDesde = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_LETRADESDE);
        StringUtils.trimNotNull(letraDesde);

        String letraHasta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_LETRAHASTA);
        StringUtils.trimNotNull(letraHasta);

        String bloque = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_BLOQUE);
        StringUtils.trimNotNull(bloque);

        String portal = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PORTAL);
        StringUtils.trimNotNull(portal);

        String escalera = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ESCALERA);
        StringUtils.trimNotNull(escalera);

        String planta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PLANTA);
        StringUtils.trimNotNull(planta);

        String puerta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PUERTA);
        StringUtils.trimNotNull(puerta);

        String codigoPostal = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_CODIGOPOSTAL);
        StringUtils.trimNotNull(codigoPostal);

        String numeroDesde = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NUMERODESDE);
        StringUtils.trimNotNull(numeroDesde);

        String numeroHasta = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NUMEROHASTA);
        StringUtils.trimNotNull(numeroHasta);

        //Rellenamos el objeto InteresadoVO con los datos del xml
        domicilio.setCodPais(Integer.parseInt(pais));
        domicilio.setCodProvincia(Integer.parseInt(provincia));
        domicilio.setCodMunicipio(Integer.parseInt(municipio));
        domicilio.setNombreVia(nombreVia);
        if(tipoVia != null) {
            domicilio.setTipoVia(Integer.parseInt(tipoVia));
        }
        if(numeroDesde != null) {
            domicilio.setPrimerNumero(Integer.parseInt(numeroDesde));
        }
        if(numeroHasta != null) {
            domicilio.setUltimoNumero(Integer.parseInt(numeroHasta));
        }
        if(letraDesde != null) {
            domicilio.setPrimeraLetra(letraDesde);
        }
        if(letraHasta != null) {
            domicilio.setUltimaLetra(letraHasta);
        }
        if(bloque != null) {
            domicilio.setBloque(bloque);
        }
        if(escalera != null) {
            domicilio.setEscalera(escalera);
        }
        if(planta != null) {
            domicilio.setPlanta(planta);
        }
        if(portal != null) {
            domicilio.setPortal(portal);
        }
        if(puerta != null) {
            domicilio.setPuerta(puerta);
        }
        if(codigoPostal != null) {
            domicilio.setCodPostal(codigoPostal);
        }

        return domicilio;
    }

    private List<GeneralValueObject> filtrarInteresadosValidos(int codRolDefecto, List<GeneralValueObject> listaInteresados) throws Exception {
        List<GeneralValueObject> interesadosValidos = new ArrayList<GeneralValueObject>();
        GeneralValueObject gVOInteresado = null;
        GeneralValueObject gVODatosInteresado = null;

        for (int i = 0; i < listaInteresados.size(); i++) {

            gVOInteresado = listaInteresados.get(i);
            gVODatosInteresado = (GeneralValueObject) gVOInteresado.getAtributo("datosInteresado");
            boolean valido = true;

            String documento = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NIF);
            if (documento != null) {
                documento = documento.trim();
            }
            String nombre = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOMBRE);
            if (nombre != null) {
                nombre = nombre.trim();
            }
            String apellido1 = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_APELLIDO1);
            if (apellido1 != null) {
                apellido1 = apellido1.trim();
            }

            String provincia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_PROVINCIA);
            if (provincia != null) {
                provincia = provincia.trim();
            }
            String municipio = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_MUNICIPIO);
            if (municipio != null) {
                municipio = municipio.trim();
            }
            String nombreVia = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_NOMBREVIA);
            if (nombreVia != null) {
                nombreVia = nombreVia.trim();
            }
            String emplazamiento = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_EMPLAZAMIENTO);
            if (emplazamiento != null) {
                emplazamiento = emplazamiento.trim();
            }
            String rol = (String) gVODatosInteresado.getAtributo(ConstantesDatos.TAG_XML_ROL);
            if (rol != null) {
                rol = rol.trim();
            }

            if (rol != null && !"".equals(rol)) {
                int iCodRol = Integer.parseInt(rol);

                if (iCodRol == codRolDefecto) {
                    valido = false;
                }
            }

            if (valido) {
                //Valida los campos obligatorios
                if (documento == null || documento.length() == 0 || rol == null || rol.length() == 0 ||
                        nombre == null || nombre.length() == 0) {
                    valido = false;

                }
                if(valido) {
                    if ((documento != null && !"0".equals(documento)) || nombre == null || nombre.equals("") || apellido1 != null) {

                        // Se comprueba si se ha introducido el documento, y CIF, NIF o NIE valido
                        boolean hayDocumento = documento != null && !"".equals(documento);
                        boolean hayNombre = nombre != null && !"".equals(nombre);
                        boolean hayAp1 = apellido1 != null && !"".equals(apellido1);

                        boolean validaCIF = (hayDocumento && hayNombre && Utilities.validaCIF(documento));
                        boolean validaNIF = (hayDocumento && hayNombre && hayAp1 && Utilities.validaNIF(documento));
                        boolean validaNIE = (hayDocumento && hayNombre && hayAp1 && Utilities.validarNie(documento));
                        if (!validaCIF && !validaNIF && !validaNIE)
                            valido = false;
                    }
                }
                /*
                Por ahora dejamos de hacer esta validacion
                //Valida datos en el domicilio
                if (provincia == null && municipio == null) {
                    if (nombreVia != null || emplazamiento != null) {
                        valido = false;
                    }

                } else if (provincia != null && municipio != null) {
                    if (nombreVia == null && emplazamiento == null) {
                        valido = false;
                    }
                } else {
                    valido = false;
                }
                */

                if (valido) {
                    interesadosValidos.add(gVOInteresado);
                }
            }
        }

        return interesadosValidos;
    }

    private void anadirDatosTramiteInicio(ExpedienteVO expedienteVO, Connection con) throws SQLException, GestionAutomaticaKONTUException {
        TramiteVO tramiteVO = new TramiteVO();
        int codTramite;

        try {
            codTramite = DefinicionProcedimientosDAO.getInstance().obtenerCodigoTramiteInicio(
                    Integer.parseInt(expedienteVO.getOrganizacionUsuario()), expedienteVO.getProcedimiento(), con);
            tramiteVO.setCodTramite(String.valueOf(codTramite));
            tramiteVO.setOcurrenciaTramite("1");

            expedienteVO.setTramite(tramiteVO);
        } catch (SQLException e){
            throw e;
        } catch (NumberFormatException e){
            String mensaje = "Ha ocurrido un error en anadirDatosTramiteInicio(): " + e.getMessage();
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(4,mensaje);
        }catch (Exception e){
            String mensaje = "Ha ocurrido un error en anadirDatosTramiteInicio(): " + e.getMessage();
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(4,mensaje);
        }
    }

    /**
     * Graba el valor del campo suplementario IBAN del expediente en el campo suplementario del tercero destinado a tal fin del interesado principal (con rol 1)
     * @param iIntegracionExterna
     * @param expedienteVO
     * @param codTercero
     * @return
     */
    private SalidaIntegracionVO grabarIBANCampoTercero(IModuloIntegracionExternoCamposFlexia iIntegracionExterna, ExpedienteVO expedienteVO, int codTercero) throws GestionAutomaticaKONTUException {
        String codigoCampoExp = "";
        String codigoCampoTer = "";
        String valorCampo;
        String organizacion = expedienteVO.getOrganizacionUsuario();
        String codProc = expedienteVO.getProcedimiento();
        String ejercicio = expedienteVO.getEjercicio();
        String numExpediente = expedienteVO.getNumero();
        SalidaIntegracionVO salidaSetCampo = null;
        SalidaIntegracionVO salidaGetCampo = null;

        try {
            codigoCampoExp = ConfigurationParameter.getParameter("codCampo_IBAN", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            codigoCampoTer = ConfigurationParameter.getParameter("codCampo_CUENTA", ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            log.info(String.format("grabarIBANCampoTercero() - Vamos a grabar el valor del campo suplementario %s del expediente %s " +
                    "en el campo suplementario %s del interesado de rol 1 del mismo, el de código %d", codigoCampoExp, numExpediente, codigoCampoTer, codTercero));

            // Se recupera el valor del campo suplementario IBAN del expediente
            salidaGetCampo = iIntegracionExterna.getCampoSuplementarioExpediente(organizacion, ejercicio, numExpediente,
                    codProc, codigoCampoExp, ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
            if (salidaGetCampo.getStatus() == 0) {
                valorCampo = salidaGetCampo.getCampoSuplementario().getValorTexto();
                log.debug(String.format("Existe valor para el campo %s en el expediente %s -> %s", codigoCampoExp, numExpediente, valorCampo));

                if(valorCampo!=null && !valorCampo.isEmpty()){
                    // Se graba el valor de IBAN en TNUMCUEBANC, campo suplementario de tercero
                    CampoSuplementarioModuloIntegracionVO campoSuplementarioIBAN = new CampoSuplementarioModuloIntegracionVO();
                    campoSuplementarioIBAN.setCodOrganizacion(organizacion);
                    campoSuplementarioIBAN.setCodProcedimiento(codProc);
                    campoSuplementarioIBAN.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                    campoSuplementarioIBAN.setTramite(false);
                    campoSuplementarioIBAN.setTercero(true);
                    campoSuplementarioIBAN.setCodTercero(String.valueOf(codTercero));
                    campoSuplementarioIBAN.setCodigoCampo(codigoCampoTer);
                    campoSuplementarioIBAN.setValorTexto(valorCampo);
                    salidaSetCampo = iIntegracionExterna.grabarCampoSuplementario(campoSuplementarioIBAN);
                    if(salidaSetCampo.getStatus()==0) {
                        log.debug(String.format("Se ha grabado correctamente el valor de IBAN en el campo %s del tercero: %s", codigoCampoTer, salidaSetCampo.getDescStatus()));
                    } else {
                        String mensaje = String.format("Ha ocurrido un error al grabar el valor de IBAN en el campo %s del tercero: %s", codigoCampoTer, salidaSetCampo.getDescStatus());
                        log.error(mensaje);
                        throw new GestionAutomaticaKONTUException(8, mensaje, expedienteVO.getNumero());
                    }
                }
            } else {
                String mensaje = String.format("Ha ocurrido un error al recuperar el valor del campo %s en el expediente %s: %s", codigoCampoExp, expedienteVO.getNumero(), salidaGetCampo.getDescStatus());
                log.error(mensaje);
                throw new GestionAutomaticaKONTUException(8, mensaje, expedienteVO.getNumero());
            }
        } catch (Exception e) {
            String mensaje = String.format("Ha ocurrido un error al grabar el valor del campo %s del expediente %s en el campo %s del interesado: %s",
                    codigoCampoExp, numExpediente, codigoCampoTer, e.getMessage());
            log.error(mensaje, e);
            throw new GestionAutomaticaKONTUException(8, mensaje, expedienteVO.getNumero());
        }

        return salidaSetCampo;
    }

}
