/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//prueba
package es.altia.flexia.integracion.moduloexterno.melanbide67;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.merkalan.exception.RemoteServiceException;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.dto.SearchCVResponseDTO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.enums.GeneratedDocuments;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.facade.LangaiVision360WSFacade;
import es.altia.flexia.integracion.moduloexterno.melanbide67.manager.MeLanbide67Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.MeLanbide67MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.MeLanbide67Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.MeLanbide67Validator;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.CentroColVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DatosPestanaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DetallesCampoSuplementarioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.EntidadColaboradoraVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.FilaCentroColVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.FilaPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.LakModRecalculoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.LeaukPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.LlamadaServicioCV;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.PersonaContratadaPuestoTrabajoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.PersonaContratadaVO;

import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItemPuestos;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SubSolicVO;


import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.flexia.interfaces.user.web.carga.parcial.fichaexpediente.vo.DatosExpedienteVO;
import es.altia.technical.PortableContext;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import es.altia.util.commons.StringOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.MELANBIDE_INTEROP;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.cvl.Persona;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MELANBIDE67 extends ModuloIntegracionExterno {

    //Logger
    private static final Logger log = LogManager.getLogger(MELANBIDE67.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    private String msgValidacion = "";
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
    private static final MeLanbide67Utils m67Utils = new MeLanbide67Utils();
    private static MELANBIDE_INTEROP melanbide_interop = new MELANBIDE_INTEROP();

    private static final int BUFFER_SIZE = 4096;

    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();

//    private GestionServiciosNISAE gestionServiciosNISAE = new GestionServiciosNISAE();
    /**
     * Alta Expedientes via registro platea --> MELANBIDE 42
     *
     * @param codigoOrganizacion
     * @param numeroExpediente
     * @param xml
     * @throws Exception
     */
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaDatosCpe(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {

            //Cargo los datos de las pestanas
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error  " + ex.getMessage(), ex);
            }

            try {
                cargarSubpestanaSolicitud_DatosCpe(numExpediente, adapt, request);
            } catch (Exception ex) {
                log.error("Error  " + ex.getMessage(), ex);
            }

        } catch (Exception ex) {
            log.error("Error en cargarPantallaDatosCpe " + ex.getMessage(), ex);
        }
        return "/jsp/extension/melanbide67/datosLeauk.jsp";
    }

    public String cargarPantallaCentrosColaboradores(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {

            //Cargo los datos de las pestanas
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {

            }

            int ejercicio = 0;
            try {
                ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
            } catch (Exception ex) {
                log.error("Error  " + ex.getMessage(), ex);
            }

            try {
                cargarSubpestanaSolicitudCentroCol(numExpediente, ejercicio, adapt, request);
            } catch (Exception ex) {
                log.error("Error  " + ex.getMessage(), ex);
            }

        } catch (Exception ex) {
            log.error("Error en cargarCentrosColaboradores " + ex.getMessage(), ex);
        }
        return "/jsp/extension/melanbide67/centrosColaboradores.jsp";
    }

    public String cargarNuevoPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        cargarCombosNuevoPuesto(codOrganizacion, request);
        return "/jsp/extension/melanbide67/nuevoPuesto.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarModificarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String opcion = (String) request.getParameter("opcion");
            if (opcion != null) {
                String idString = (String) request.getParameter("idPuesto");
                cargarCombosNuevoPuesto(codOrganizacion, request);

                if (idString != null && !idString.equals("")) {
                    int id = Integer.parseInt(request.getParameter("idPuesto"));
                    LeaukPuestoVO puesto = new LeaukPuestoVO();
                    puesto.setIdPuesto(id);
                    puesto.setNumExp(numExpediente);
                    puesto = MeLanbide67Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (puesto != null) {
                        request.setAttribute("puestoModif", puesto);

                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        String desc = null;
                        /*if(puesto.getCodTit1() != null && !puesto.getCodTit1().equals(""))
                        {
                            desc = MeLanbide67Manager.getInstance().getDescripcionTitulacion(puesto.getCodTit1(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit1", desc);
                            }
                        }
                        if(puesto.getCodTit2() != null && !puesto.getCodTit2().equals(""))
                        {
                            desc = MeLanbide67Manager.getInstance().getDescripcionTitulacion(puesto.getCodTit2(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit2", desc);
                            }
                        }
                        if(puesto.getCodTit3() != null && !puesto.getCodTit3().equals(""))
                        {
                            desc = MeLanbide67Manager.getInstance().getDescripcionTitulacion(puesto.getCodTit3(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit3", desc);
                            }
                        }*/
                    }
                    if (opcion.equalsIgnoreCase("consultar")) {
                        request.setAttribute("consulta", true);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
        return "/jsp/extension/melanbide67/nuevoPuesto.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarModificarCentroCol(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String opcion = (String) request.getParameter("opcion");
            if (opcion != null) {
                String idString = (String) request.getParameter("idCentroCol");

                if (idString != null && !idString.equals("")) {
                    int id = Integer.parseInt(request.getParameter("idCentroCol"));
                    CentroColVO cc = new CentroColVO();
                    cc.setIdCentroCol(id);
                    cc.setNumExp(numExpediente);
                    cc = MeLanbide67Manager.getInstance().getCentroColPorCodigoYExpediente(cc, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (cc != null) {
                        request.setAttribute("centroColModif", cc);

                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        String desc = null;

                    }
                    if (opcion.equalsIgnoreCase("consultar")) {
                        request.setAttribute("consulta", true);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
        return "/jsp/extension/melanbide67/nuevoCentCol.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void guardarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();

        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {

            Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
            if (ejercicio != null) {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
                String idPuesto = (String) request.getParameter("idPuesto");
                int idPuestoInt = 0;
                if (!idPuesto.equals("null")) {
                    idPuestoInt = Integer.parseInt(idPuesto);
                }

                String codPuesto = (String) request.getParameter("codPuesto");
                String descPuesto = (String) request.getParameter("descPuesto");
                String apellido1 = (String) request.getParameter("apellido1");
                String apellido2 = (String) request.getParameter("apellido2");
                String nombre = (String) request.getParameter("nombre");
                String impSubvSol = (String) request.getParameter("impSubvSol");
                String impSubvEst = (String) request.getParameter("impSubvEst");
                String impSubvOfe = (String) request.getParameter("impSubvOfe");
                String reintegros = (String) request.getParameter("reintegros");
                String impTotal = (String) request.getParameter("impTotal");
                String salarioSol = (String) request.getParameter("salarioSol");
                String salarioOfe = (String) request.getParameter("salarioOfe");
                String sexoSol = (String) request.getParameter("sexoSol");
                int sexoSolInt = 0;
                if (!sexoSol.equals("null")) {
                    sexoSolInt = Integer.parseInt(sexoSol);
                }
                String sexoOfe = (String) request.getParameter("sexoOfe");
                int sexoOfeInt = 0;
                if (!sexoOfe.equals("null")) {
                    sexoOfeInt = Integer.parseInt(sexoOfe);
                }
                String dptoSol = (String) request.getParameter("dptoSol");
                String dptoOfe = (String) request.getParameter("dptoOfe");
                String codTitSol = (String) request.getParameter("codTitSol");
                String codTitOfe = (String) request.getParameter("codTitOfe");
                String codModSol = (String) request.getParameter("codModSol");
                String codModOfe = (String) request.getParameter("codModOfe");
                String jornadaLabSol = (String) request.getParameter("jornadaLabSol");
                String jornadaLabOfe = (String) request.getParameter("jornadaLabOfe");
                String centroTrabSol = (String) request.getParameter("centroTrabSol");
                String centroTrabOfe = (String) request.getParameter("centroTrabOfe");
                String ctaCotizSol = (String) request.getParameter("ctaCotizSol");
                String ctaCotizOfe = (String) request.getParameter("ctaCotizOfe");
                String fechaIniSol = (String) request.getParameter("fechaIniSol");
                String fechaFinSol = (String) request.getParameter("fechaFinSol");
                String fechaIniOfe = (String) request.getParameter("fechaIniOfe");
                String fechaFinOfe = (String) request.getParameter("fechaFinOfe");
                String grupoCotizSol = (String) request.getParameter("grupoCotizSol");
                String grupoCotizOfe = (String) request.getParameter("grupoCotizOfe");
                String convenioColSol = (String) request.getParameter("convenioColSol");
                String convenioColOfe = (String) request.getParameter("convenioColOfe");
                String observaciones = (String) request.getParameter("observaciones");
                String codTipoNif = (String) request.getParameter("codTipoNif");
                String dniTrab = (String) request.getParameter("nif");
                String fecNacimiento = (String) request.getParameter("fecNacimiento");
                String numOferta = (String) request.getParameter("numOferta");
                String centroColGestion = (String) request.getParameter("centroColGestion");
                String centroColCaptacion = (String) request.getParameter("centroColCaptacion");
                String impPago1 = (String) request.getParameter("impPago1");
                String impPago2 = (String) request.getParameter("impPago2");

                LeaukPuestoVO puesto = null;
                if (idPuestoInt != 0) {
                    puesto = new LeaukPuestoVO();
                    puesto.setIdPuesto(idPuestoInt);
                    puesto.setNumExp(numExpediente);
                    //puesto.setEjercicio(ejercicio);
                    puesto = MeLanbide67Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                } else {
                    puesto = new LeaukPuestoVO();
                    puesto.setNumExp(numExpediente);
                    //puesto.setEjercicio(ejercicio);
                }
                if (puesto == null) {
                    codigoOperacion = "3";
                } else {
                    puesto.setCodPuesto(codPuesto != null && !codPuesto.equals("") ? codPuesto.toUpperCase() : null);
                    puesto.setDescPuesto(descPuesto != null && !descPuesto.equals("") ? descPuesto.toUpperCase() : null);
                    puesto.setApellido1(apellido1 != null && !apellido1.equals("") ? apellido1.toUpperCase() : null);
                    puesto.setApellido2(apellido2 != null && !apellido2.equals("") ? apellido2.toUpperCase() : null);
                    puesto.setNombre(nombre != null && !nombre.equals("") ? nombre.toUpperCase() : null);
                    puesto.setImpSubvSol(impSubvSol != null && !impSubvSol.equals("") ? new BigDecimal(impSubvSol.replaceAll(",", "\\.")) : null);
                    puesto.setImpSubvEst(impSubvEst != null && !impSubvEst.equals("") ? new BigDecimal(impSubvEst.replaceAll(",", "\\.")) : null);
                    puesto.setImpSubvOfe(impSubvOfe != null && !impSubvOfe.equals("") ? new BigDecimal(impSubvOfe.replaceAll(",", "\\.")) : null);
                    puesto.setReintegros(reintegros != null && !reintegros.equals("") ? new BigDecimal(reintegros.replaceAll(",", "\\.")) : null);
                    puesto.setImpTotal(impTotal != null && !impTotal.equals("") ? new BigDecimal(impTotal.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioSol(salarioSol != null && !salarioSol.equals("") ? new BigDecimal(salarioSol.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioOfe(salarioOfe != null && !salarioOfe.equals("") ? new BigDecimal(salarioOfe.replaceAll(",", "\\.")) : null);
                    puesto.setSexoSol(sexoSolInt);
                    puesto.setSexoOfe(sexoOfeInt);
                    puesto.setDptoSol(dptoSol != null && !dptoSol.equals("") ? dptoSol.toUpperCase() : null);
                    puesto.setDptoOfe(dptoOfe != null && !dptoOfe.equals("") ? dptoOfe.toUpperCase() : null);
                    puesto.setCodTitulacionSol(codTitSol != null && !codTitSol.equals("") ? codTitSol.toUpperCase() : null);
                    puesto.setCodTitulacionOfe(codTitOfe != null && !codTitOfe.equals("") ? codTitOfe.toUpperCase() : null);
                    puesto.setCodModSol(codModSol != null && !codModSol.equals("") ? codModSol.toUpperCase() : null);
                    puesto.setCodModOfe(codModOfe != null && !codModOfe.equals("") ? codModOfe.toUpperCase() : null);
                    puesto.setJornadaLabSol(jornadaLabSol != null && !jornadaLabSol.equals("") ? new BigDecimal(jornadaLabSol.replaceAll(",", "\\.")) : null);
                    puesto.setJornadaLabOfe(jornadaLabOfe != null && !jornadaLabOfe.equals("") ? new BigDecimal(jornadaLabOfe.replaceAll(",", "\\.")) : null);
                    puesto.setCentroTrabSol(centroTrabSol != null && !centroTrabSol.equals("") ? centroTrabSol.toUpperCase() : null);
                    puesto.setCentroTrabOfe(centroTrabOfe != null && !centroTrabOfe.equals("") ? centroTrabOfe.toUpperCase() : null);
                    puesto.setCtaCotizSol(ctaCotizSol != null && !ctaCotizSol.equals("") ? ctaCotizSol.toUpperCase() : null);
                    puesto.setCtaCotizOfe(ctaCotizOfe != null && !ctaCotizOfe.equals("") ? ctaCotizOfe.toUpperCase() : null);
                    puesto.setFecFinSol(fechaFinSol != null && !fechaFinSol.equals("") ? format.parse(fechaFinSol) : null);
                    puesto.setFecIniSol(fechaIniSol != null && !fechaIniSol.equals("") ? format.parse(fechaIniSol) : null);
                    puesto.setFecFinOfe(fechaFinOfe != null && !fechaFinOfe.equals("") ? format.parse(fechaFinOfe) : null);
                    puesto.setFecIniOfe(fechaIniOfe != null && !fechaIniOfe.equals("") ? format.parse(fechaIniOfe) : null);
                    puesto.setGrupoCotizSol(grupoCotizSol != null && !grupoCotizSol.equals("") ? grupoCotizSol.toUpperCase() : null);
                    puesto.setGrupoCotizOfe(grupoCotizOfe != null && !grupoCotizOfe.equals("") ? grupoCotizOfe.toUpperCase() : null);
                    puesto.setConvenioColSol(convenioColSol != null && !convenioColSol.equals("") ? convenioColSol.toUpperCase() : null);
                    puesto.setConvenioColOfe(convenioColOfe != null && !convenioColOfe.equals("") ? convenioColOfe.toUpperCase() : null);
                    puesto.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);
                    puesto.setCodTipoNif(codTipoNif != null && !codTipoNif.equals("") ? codTipoNif.toUpperCase() : null);
                    puesto.setNif(dniTrab != null && !dniTrab.equals("") ? dniTrab.toUpperCase() : null);
                    puesto.setFecNacimiento(fecNacimiento != null && !fecNacimiento.equals("") ? format.parse(fecNacimiento) : null);
                    puesto.setNum_oferta(numOferta != null && !numOferta.equals("") ? numOferta.toUpperCase() : null);
                    puesto.setCentroColGestion(centroColGestion != null && !centroColGestion.equals("") ? centroColGestion.toUpperCase() : null);
                    puesto.setCentroColCaptacion(centroColCaptacion != null && !centroColCaptacion.equals("") ? centroColCaptacion.toUpperCase() : null);
                    puesto.setImpPago1(impPago1 != null && !impPago1.equals("") ? new BigDecimal(impPago1.replaceAll(",", "\\.")) : null);
                    puesto.setImpPago2(impPago2 != null && !impPago2.equals("") ? new BigDecimal(impPago2.replaceAll(",", "\\.")) : null);

                    //TODO: se podria validar los datos del puesto
                    MeLanbide67Manager.getInstance().guardarLakPuestoVO(codOrganizacion, puesto, adaptador);
                    recalculoSubvLeaukEmpMod(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
                }
                //calculos = MeLanbide67Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
                puestos = MeLanbide67Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adaptador);
            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error  " + ex.getMessage(), ex);
        }
        escribirListaPuestosRequest(codigoOperacion, puestos, calculos, response);
    }

    public void guardarCentroCol(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaCentroColVO> centrosCol = new ArrayList<FilaCentroColVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();

        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {

            Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
            if (ejercicio != null) {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
                String idCentroCol = (String) request.getParameter("idCentroCol");
                int idCentroInt = 0;
                if (!idCentroCol.equals("null")) {
                    idCentroInt = Integer.parseInt(idCentroCol);
                }

                String numExp = (String) request.getParameter("numero");
                String ofertaEmpleo = (String) request.getParameter("ofertaEmpleo");
                String numOfertaSol = (String) request.getParameter("numOfertaSol");
                String numOfertaOfe = (String) request.getParameter("numOfertaOfe");
                String numConSubvSol = (String) request.getParameter("numConSubvSol");
                String numConSubvOfe = (String) request.getParameter("numConSubvOfe");
                String subvencion = (String) request.getParameter("subvencion");
                String impSubvSol = (String) request.getParameter("impSubvSol");
                String impSubvOfe = (String) request.getParameter("impSubvOfe");
                String observaciones = (String) request.getParameter("observaciones");

                CentroColVO cc = null;
                if (idCentroInt != 0) {
                    cc = new CentroColVO();
                    cc.setIdCentroCol(idCentroInt);
                    cc.setNumExp(numExpediente);
                    //puesto.setEjercicio(ejercicio);
                    cc = MeLanbide67Manager.getInstance().getCentroColPorCodigoYExpediente(cc, adaptador);
                } else {
                    cc = new CentroColVO();
                    cc.setNumExp(numExpediente);
                    //puesto.setEjercicio(ejercicio);
                }
                if (cc == null) {
                    codigoOperacion = "3";
                } else {
                    cc.setOfertaEmpleo(ofertaEmpleo != null && !ofertaEmpleo.equals("") ? ofertaEmpleo.toUpperCase() : null);
                    cc.setNumOfertaSol(Integer.parseInt(numOfertaSol));
                    cc.setNumOfertaOfe(Integer.parseInt(numOfertaOfe));
                    cc.setNumConSubvSol(Integer.parseInt(numConSubvSol));
                    cc.setNumConSubvOfe(Integer.parseInt(numConSubvOfe));
                    cc.setSubvencion(Integer.parseInt(subvencion));
                    cc.setImpSubvSol(impSubvSol != null && !impSubvSol.equals("") ? new BigDecimal(impSubvSol.replaceAll(",", "\\.")) : null);
                    cc.setImpSubvOfe(impSubvOfe != null && !impSubvOfe.equals("") ? new BigDecimal(impSubvOfe.replaceAll(",", "\\.")) : null);
                    cc.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);
                    //TODO: se podria validar los datos del puesto

                    //MeLanbide67Manager.getInstance().guardarCpePuestoVO(codOrganizacion, puesto, impAnterior, estadoAnterior, adaptador);
                    MeLanbide67Manager.getInstance().guardarCentroColVO(codOrganizacion, cc, adaptador);
                    log.debug("ANTES RECALCULO");
                    recalculoSubvLeaukCenMod(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
                    log.debug("DESPUES RECALCULO");
                }
                //calculos = MeLanbide67Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
                centrosCol = MeLanbide67Manager.getInstance().getListaCentrosColPorExpediente(numExpediente, adaptador);
                log.debug("centrosCol: " + centrosCol.size());
            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error  " + ex.getMessage(), ex);
        }
        escribirListaCCRequest(codigoOperacion, centrosCol, calculos, response);
    }

    public void eliminarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();

        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {

            Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
            if (ejercicio != null) {

                String idPuesto = (String) request.getParameter("idPuesto");
                int idPuestoInt = 0;
                if (idPuesto != null) {
                    idPuestoInt = Integer.parseInt(idPuesto);
                }

                LeaukPuestoVO puesto = null;
                if (idPuestoInt != 0) {
                    puesto = new LeaukPuestoVO();
                    puesto.setIdPuesto(idPuestoInt);
                    puesto.setNumExp(numExpediente);
                    //puesto.setEjercicio(ejercicio);
                    puesto = MeLanbide67Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                }
                if (puesto == null) {
                    codigoOperacion = "3";
                } else {
                    MeLanbide67Manager.getInstance().eliminarLakPuestoVO(codOrganizacion, puesto, adaptador);
                }
                //calculos = MeLanbide67Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
                puestos = MeLanbide67Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adaptador);
            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error  " + ex.getMessage(), ex);
        }
        escribirListaPuestosRequest(codigoOperacion, puestos, calculos, response);
    }

    private void cargarSubpestanaSolicitud_DatosCpe(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request) {
        try {
            List<FilaPuestoVO> puestos = MeLanbide67Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adapt);
            request.setAttribute("puestos", puestos);
        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
    }

    private void cargarSubpestanaSolicitudCentroCol(String numExpediente, int ejercicio, AdaptadorSQLBD adapt, HttpServletRequest request) {
        try {
            int impSubv = 0;
            List<FilaCentroColVO> centrosCol = MeLanbide67Manager.getInstance().getListaCentrosColPorExpediente(numExpediente, adapt);
            for (int i = 0; i < centrosCol.size(); i++) {
                String ofertaEmpleo = centrosCol.get(i).getOfertaEmpleo();
                impSubv = MeLanbide67Manager.getInstance().getImpSubv(ejercicio, ofertaEmpleo, adapt);
                centrosCol.get(i).setSubvencion(impSubv);
            }

            request.setAttribute("centrosCol", centrosCol);

        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
    }

    private boolean validarDatosCpe(HttpServletRequest request) {
        try {
            String empresa = request.getParameter("empresa");
            String impSubvAprobadaExp = request.getParameter("impSubvAprobadaExp");
            String impSubvAprobadaConv = request.getParameter("impSubvAprobadaConv");
            String otrasAyudas = request.getParameter("otrasAyudas");
            String impSubvFinal = request.getParameter("impSubvFinal");

            if (!MeLanbide67Validator.validarTexto(empresa, 500)) {
                return false;
            }
            if (!MeLanbide67Validator.validarNumericoDecimal(impSubvAprobadaExp, 10, 2)) {
                return false;
            }
            if (!MeLanbide67Validator.validarNumericoDecimal(impSubvAprobadaConv, 10, 2)) {
                return false;
            }
            if (!MeLanbide67Validator.validarNumericoDecimal(otrasAyudas, 10, 2)) {
                return false;
            }
            if (!MeLanbide67Validator.validarNumericoDecimal(impSubvFinal, 10, 2)) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
            return false;
        }
    }

    private void cargarCombosNuevoPuesto(int codOrganizacion, HttpServletRequest request) {
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

        List<ValorCampoDesplegableModuloIntegracionVO> list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();

        List<SelectItem> listaModalidadSol = new ArrayList<SelectItem>();
        List<SelectItem> listaModalidadOfe = new ArrayList<SelectItem>();

        //Combo MODALIDAD
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_MODALIDAD, ConstantesMeLanbide67.FICHERO_PROPIEDADES);

            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaModalidadSol = MeLanbide67MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list, SelectItem.ORDENAR_POR_CODIGO);
            listaModalidadOfe = MeLanbide67MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list, SelectItem.ORDENAR_POR_CODIGO);

        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
        request.setAttribute("listaModalidadSol", listaModalidadSol);
        request.setAttribute("listaModalidadOfe", listaModalidadOfe);

        List<ValorCampoDesplegableModuloIntegracionVO> listTit = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();

        List<SelectItem> listaTitulacionSol = new ArrayList<SelectItem>();
        List<SelectItem> listaTitulacionOfe = new ArrayList<SelectItem>();

        //Combo TITULACION
        try {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_TITULACION, ConstantesMeLanbide67.FICHERO_PROPIEDADES);

            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            listTit = salidaIntegracion.getCampoDesplegable().getValores();
            listaTitulacionSol = MeLanbide67MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(listTit, SelectItem.ORDENAR_POR_CODIGO);
            listaTitulacionOfe = MeLanbide67MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(listTit, SelectItem.ORDENAR_POR_CODIGO);

        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
        request.setAttribute("listaTitulacionSol", listaTitulacionSol);
        request.setAttribute("listaTitulacionOfe", listaTitulacionOfe);

        request.setAttribute("listaTitulacionSol", listaTitulacionSol);
        request.setAttribute("listaTitulacionOfe", listaTitulacionOfe);

        try {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            List<SelectItemPuestos> listaPuesto = MeLanbide67Manager.getInstance().getListaPuestos(adaptador);
            request.setAttribute("lstPuesto", listaPuesto);
        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }

        //Combo TIPO DOCUMENTO
        List<es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem> listaTipoDoc = new ArrayList<es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem>();
        try {
            listaTipoDoc = MeLanbide67Manager.getInstance().getTiposDocumento(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
        request.setAttribute("listaNif", listaTipoDoc);

    }

    private void escribirListaPuestosRequest(String codigoOperacion, List<FilaPuestoVO> puestos, Map<String, BigDecimal> calculos, HttpServletResponse response) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for (FilaPuestoVO puesto : puestos) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(puesto.getIdPuesto());
            xmlSalida.append("</ID>");
            xmlSalida.append("<COD_PUESTO>");
            xmlSalida.append(puesto.getCodPuesto());
            xmlSalida.append("</COD_PUESTO>");
            xmlSalida.append("<DESC_PUESTO>");
            xmlSalida.append(puesto.getDescPuesto());
            xmlSalida.append("</DESC_PUESTO>");
            xmlSalida.append("<APELLIDO1>");
            xmlSalida.append(puesto.getApellido1());
            xmlSalida.append("</APELLIDO1>");
            xmlSalida.append("<APELLIDO2>");
            xmlSalida.append(puesto.getApellido2());
            xmlSalida.append("</APELLIDO2>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(puesto.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<IMPSUBVSOL>");
            xmlSalida.append(puesto.getImpSubvSol());
            xmlSalida.append("</IMPSUBVSOL>");
            xmlSalida.append("<IMPSUBVEST>");
            xmlSalida.append(puesto.getImpSubvEst());
            xmlSalida.append("</IMPSUBVEST>");
            xmlSalida.append("<IMPSUBVOFE>");
            xmlSalida.append(puesto.getImpSubvOfe());
            xmlSalida.append("</IMPSUBVOFE>");
            xmlSalida.append("<REINTEGROS>");
            xmlSalida.append(puesto.getReintegros());
            xmlSalida.append("</REINTEGROS>");
            xmlSalida.append("<IMPTOTAL>");
            xmlSalida.append(puesto.getImpTotal());
            xmlSalida.append("</IMPTOTAL>");
            xmlSalida.append("<SALARIOSOL>");
            xmlSalida.append(puesto.getSalarioSol());
            xmlSalida.append("</SALARIOSOL>");
            xmlSalida.append("<SALARIOOFE>");
            xmlSalida.append(puesto.getSalarioOfe());
            xmlSalida.append("</SALARIOOFE>");
            xmlSalida.append("<SEXOSOL>");
            xmlSalida.append(puesto.getSexoSol());
            xmlSalida.append("</SEXOSOL>");
            xmlSalida.append("<SEXOOFE>");
            xmlSalida.append(puesto.getSexoOfe());
            xmlSalida.append("</SEXOOFE>");
            xmlSalida.append("<DPTOSOL>");
            xmlSalida.append(puesto.getDptoSol());
            xmlSalida.append("</DPTOSOL>");
            xmlSalida.append("<DPTOOFE>");
            xmlSalida.append(puesto.getDptoOfe());
            xmlSalida.append("</DPTOOFE>");
            xmlSalida.append("<TITULACION_SOL>");
            xmlSalida.append(puesto.getCodTitulacionSol());
            xmlSalida.append("</TITULACION_SOL>");
            xmlSalida.append("<TITULACION_OFE>");
            xmlSalida.append(puesto.getCodTitulacionOfe());
            xmlSalida.append("</TITULACION_OFE>");
            xmlSalida.append("<MODALIDAD_SOL>");
            xmlSalida.append(puesto.getCodModSol());
            xmlSalida.append("</MODALIDAD_SOL>");
            xmlSalida.append("<MODALIDAD_OFE>");
            xmlSalida.append(puesto.getCodModOfe());
            xmlSalida.append("</MODALIDAD_OFE>");
            xmlSalida.append("<JORNADA_LAB_SOL>");
            xmlSalida.append(puesto.getJornadaLabSol());
            xmlSalida.append("</JORNADA_LAB_SOL>");
            xmlSalida.append("<JORNADA_LAB_OFE>");
            xmlSalida.append(puesto.getJornadaLabOfe());
            xmlSalida.append("</JORNADA_LAB_OFE>");
            xmlSalida.append("<CENTRO_TRAB_SOL>");
            xmlSalida.append(puesto.getCentroTrabSol());
            xmlSalida.append("</CENTRO_TRAB_SOL>");
            xmlSalida.append("<CENTRO_TRAB_OFE>");
            xmlSalida.append(puesto.getCentroTrabOfe());
            xmlSalida.append("</CENTRO_TRAB_OFE>");
            xmlSalida.append("<CTA_COTIZ_SOL>");
            xmlSalida.append(puesto.getCtaCotizSol());
            xmlSalida.append("</CTA_COTIZ_SOL>");
            xmlSalida.append("<CTA_COTIZ_OFE>");
            xmlSalida.append(puesto.getCtaCotizOfe());
            xmlSalida.append("</CTA_COTIZ_OFE>");
            xmlSalida.append("<FEC_INI_CONTR_SOL>");
            xmlSalida.append(puesto.getFecIniSol());
            xmlSalida.append("</FEC_INI_CONTR_SOL>");
            xmlSalida.append("<FEC_FIN_CONTR_SOL>");
            xmlSalida.append(puesto.getFecFinSol());
            xmlSalida.append("</FEC_FIN_CONTR_SOL>");
            xmlSalida.append("<FEC_INI_CONTR_OFE>");
            xmlSalida.append(puesto.getFecIniOfe());
            xmlSalida.append("</FEC_INI_CONTR_OFE>");
            xmlSalida.append("<FEC_FIN_CONTR_OFE>");
            xmlSalida.append(puesto.getFecFinOfe());
            xmlSalida.append("</FEC_FIN_CONTR_OFE>");
            xmlSalida.append("<GRUPO_COTIZ_SOL>");
            xmlSalida.append(puesto.getGrupoCotizSol());
            xmlSalida.append("</GRUPO_COTIZ_SOL>");
            xmlSalida.append("<GRUPO_COTIZ_OFE>");
            xmlSalida.append(puesto.getGrupoCotizOfe());
            xmlSalida.append("</GRUPO_COTIZ_OFE>");
            xmlSalida.append("<CONVENIO_COL_SOL>");
            xmlSalida.append(puesto.getConvenioColSol());
            xmlSalida.append("</CONVENIO_COL_SOL>");
            xmlSalida.append("<CONVENIO_COL_OFE>");
            xmlSalida.append(puesto.getConvenioColOfe());
            xmlSalida.append("</CONVENIO_COL_OFE>");
            xmlSalida.append("<OBSERVACIONES>");
            xmlSalida.append(puesto.getObservaciones());
            xmlSalida.append("</OBSERVACIONES>");
            xmlSalida.append("<DNI_TRAB>");
            xmlSalida.append(puesto.getDni());
            xmlSalida.append("</DNI_TRAB>");
            xmlSalida.append("<FEC_NACIMIENTO>");
            xmlSalida.append(puesto.getFecNacimiento());
            xmlSalida.append("</FEC_NACIMIENTO>");
            xmlSalida.append("<NUM_OFERTA>");
            xmlSalida.append(puesto.getNum_oferta());
            xmlSalida.append("</NUM_OFERTA>");
            xmlSalida.append("<CENTRO_COL_GESTION>");
            xmlSalida.append(puesto.getCentroColGestion());
            xmlSalida.append("</CENTRO_COL_GESTION>");
            xmlSalida.append("<CENTRO_COL_CAPTACION>");
            xmlSalida.append(puesto.getCentroColCaptacion());
            xmlSalida.append("</CENTRO_COL_CAPTACION>");
            xmlSalida.append("<IMP_PAGO_1>");
            xmlSalida.append(puesto.getImpPago1());
            xmlSalida.append("</IMP_PAGO_1>");
            xmlSalida.append("<IMP_PAGO_2>");
            xmlSalida.append(puesto.getImpPago2());
            xmlSalida.append("</IMP_PAGO_2>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error  " + e.getMessage(), e);
        }//try-catch
    }

    private void escribirListaCCRequest(String codigoOperacion, List<FilaCentroColVO> puestos, Map<String, BigDecimal> calculos, HttpServletResponse response) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for (FilaCentroColVO puesto : puestos) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(puesto.getIdCentroCol());
            xmlSalida.append("</ID>");
            xmlSalida.append("<NUM_EXP>");
            xmlSalida.append(puesto.getNumExp());
            xmlSalida.append("</NUM_EXP>");
            xmlSalida.append("<OFERTA_EMPLEO>");
            xmlSalida.append(puesto.getOfertaEmpleo());
            xmlSalida.append("</OFERTA_EMPLEO>");
            xmlSalida.append("<NUM_OFERTAS_SOL>");
            xmlSalida.append(puesto.getNumOfertaSol());
            xmlSalida.append("</NUM_OFERTAS_SOL>");
            xmlSalida.append("<NUM_OFERTAS_CON>");
            xmlSalida.append(puesto.getNumOfertaOfe());
            xmlSalida.append("</NUM_OFERTAS_CON>");
            xmlSalida.append("<NUM_CONTR_SOL>");
            xmlSalida.append(puesto.getNumConSubvSol());
            xmlSalida.append("</NUM_CONTR_SOL>");
            xmlSalida.append("<NUM_CONTR_CON>");
            xmlSalida.append(puesto.getNumConSubvOfe());
            xmlSalida.append("</NUM_CONTR_CON>");
            xmlSalida.append("<IMPORTE_SUBV>");
            xmlSalida.append(puesto.getSubvencion());
            xmlSalida.append("</IMPORTE_SUBV>");
            xmlSalida.append("<IMPORTE_SUBV_SOL>");
            xmlSalida.append(puesto.getImpSubvSol());
            xmlSalida.append("</IMPORTE_SUBV_SOL>");
            xmlSalida.append("<IMPORTE_SUBV_CON>");
            xmlSalida.append(puesto.getImpSubvOfe());
            xmlSalida.append("</IMPORTE_SUBV_CON>");
            xmlSalida.append("<OBSERVACIONES>");
            xmlSalida.append(puesto.getObservaciones());
            xmlSalida.append("</OBSERVACIONES>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error  " + e.getMessage(), e);
        }//try-catch
    }

    /**
     * Operación que recupera los datos de conexión a la BBDD
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
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
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
                log.error("*** AdaptadorSQLBD: " + te.getMessage(), te);
            } catch (SQLException e) {
                log.error("*** Error: " + e.getMessage(), e);
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
                    log.error("*** Error: " + e.getMessage(), e);
                }//try-catch
            }// finally
        }// synchronized
        return adapt;
    }//getConnection

    public void recalculoSubvLeaukCen(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaCentroColVO> centrosCol = new ArrayList<FilaCentroColVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();

        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {

            Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
            if (ejercicio != null) {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
                String idCentroCol = (String) request.getParameter("idCentroCol");
                int idCentroInt = 0;
                if (!idCentroCol.equals("null")) {
                    idCentroInt = Integer.parseInt(idCentroCol);
                }
                Integer idLinea = null;
                if (!idCentroCol.equals("null")) {
                    idLinea = Integer.parseInt(idCentroCol);
                }
                String anio = String.valueOf(ejercicio);
                String rdo = MeLanbide67Manager.getInstance().recalculoSubvLeaukCen(numExpediente, idLinea, anio, adaptador);
                log.debug("RESULTADO RECALCULO" + rdo);

                if (rdo.equalsIgnoreCase("OK")) {
                    centrosCol = MeLanbide67Manager.getInstance().getListaCentrosColPorExpediente(numExpediente, adaptador);
                } else if (rdo.equalsIgnoreCase("NOT_FOUND_IMP_LEAUK")) {
                    codigoOperacion = "3";
                } else if (rdo.equalsIgnoreCase("UPDATE_RECAL_LEAUK")) {
                    codigoOperacion = "3";
                } else {
                    codigoOperacion = "3";
                }

            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("*** Error: " + ex.getMessage(), ex);
        }
        escribirListaCCRequest(codigoOperacion, centrosCol, calculos, response);
    }

    public void recalculoSubvLeaukCenMod(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaCentroColVO> centrosCol = new ArrayList<FilaCentroColVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();

        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {

            Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
            if (ejercicio != null) {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
                String idCentroCol = (String) request.getParameter("idCentroCol");
                int idCentroInt = 0;
                if (!idCentroCol.equals("null")) {
                    idCentroInt = Integer.parseInt(idCentroCol);
                }
                Integer idLinea = null;
                if (!idCentroCol.equals("null")) {
                    idLinea = Integer.parseInt(idCentroCol);
                }
                String anio = String.valueOf(ejercicio);
                String rdo = MeLanbide67Manager.getInstance().recalculoSubvLeaukCen(numExpediente, idLinea, anio, adaptador);
                log.debug("RESULTADO RECALCULO" + rdo);

                if (rdo.equalsIgnoreCase("OK")) {
                    centrosCol = MeLanbide67Manager.getInstance().getListaCentrosColPorExpediente(numExpediente, adaptador);
                } else if (rdo.equalsIgnoreCase("NOT_FOUND_IMP_LEAUK")) {
                    codigoOperacion = "3";
                } else if (rdo.equalsIgnoreCase("UPDATE_RECAL_LEAUK")) {
                    codigoOperacion = "3";
                }

            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("*** Error: " + ex.getMessage(), ex);
        }
        //escribirListaCCRequest(codigoOperacion, centrosCol, calculos, response);
    }

    public void recalculoSubvLeaukEmp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();

        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {

            Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
            if (ejercicio != null) {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
                String idPuesto = (String) request.getParameter("idPuesto");
                int idPuestoInt = 0;
                if (!idPuesto.equals("null")) {
                    idPuestoInt = Integer.parseInt(idPuesto);
                }
                Integer idLinea = null;
                if (!idPuesto.equals("null")) {
                    idLinea = Integer.parseInt(idPuesto);
                }

                String anio = String.valueOf(ejercicio);
                String rdo = MeLanbide67Manager.getInstance().recalculoSubvLeaukEmp(numExpediente, idLinea, anio, adaptador);
                if (rdo.equalsIgnoreCase("OK")) {
                    puestos = MeLanbide67Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adaptador);
                } else if (rdo.equalsIgnoreCase("NOT_FOUND_IMP_LEAUK")) {
                    codigoOperacion = "3";
                } else if (rdo.equalsIgnoreCase("UPDATE_RECAL_LEAUK")) {
                    codigoOperacion = "3";
                }

            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("*** Error: " + ex.getMessage(), ex);
        }
        escribirListaPuestosRequest(codigoOperacion, puestos, calculos, response);
    }

    public void recalculoSubvLeaukEmpMod(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();

        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {

            Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
            if (ejercicio != null) {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
                String idPuesto = (String) request.getParameter("idPuesto");
                int idPuestoInt = 0;
                if (!idPuesto.equals("null")) {
                    idPuestoInt = Integer.parseInt(idPuesto);
                }
                Integer idLinea = null;
                if (!idPuesto.equals("null")) {
                    idLinea = Integer.parseInt(idPuesto);
                }

                String anio = String.valueOf(ejercicio);
                String rdo = MeLanbide67Manager.getInstance().recalculoSubvLeaukEmp(numExpediente, idLinea, anio, adaptador);
                if (rdo.equalsIgnoreCase("OK")) {
                    puestos = MeLanbide67Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adaptador);
                } else if (rdo.equalsIgnoreCase("NOT_FOUND_IMP_LEAUK")) {
                    codigoOperacion = "3";
                } else if (rdo.equalsIgnoreCase("UPDATE_RECAL_LEAUK")) {
                    codigoOperacion = "3";
                }

            } else {
                codigoOperacion = "3";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("*** Error: " + ex.getMessage(), ex);
        }
        //escribirListaPuestosRequest(codigoOperacion, puestos, calculos, response);
    }

    public void crearRegisCentrosCol(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
        MeLanbide67Manager.getInstance().crearRegisCentrosCol(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
    }

    //
    public String calculoSubvenLAK(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        log.info("------- INICIO calculoSubvLak - Expediente " + numExpediente + " - Tram: " + codTramite + " - " + ocurrenciaTramite);
        // CODIGOS OPERACION
        // 0 = O.K.
        // 1 = ERROR GENERICO
        // 2 = Error al obtener el adaptador
        // 3 = Ha fallado la carga de datos.
        // 4 = Ha ocurrido un error al calcular la edad
        // 5 = Ha ocurrido un error al grabar SUPLEMENTARIO
        // 6 = Ha ocurrido un error extrayendo los datos del calculo
        // 7 = No se han encontrado datos sobre los importes para la convocatoria en curso.
        // 8 = No se ha encontrado el puesto de trabajo
        // 9 = La funcion ha devuelto un error no controlado
        // 10 = La respuesta de la funcion esta vacia
        // 11 = error al recuperar la Fecha Inicio de contrato
        // 12 = error al recuperar la Fecha Nacimiento

        String codigoOperacion = "0";
        String importeTotal = "";
        String impPago1 = "";
        String impPago2 = "";
        String ejercicio = "";
        String codPuesto = "";
        String titulacion = "";
        String sexo = "";
        String jornada = "";
        double jornadaDbl = 0;
        String modalidadContrato = null;
        int edad = 0;

        Date fecIni = null;
        Date fecNac = null;
        BigDecimal costeContrato = BigDecimal.ZERO;

        String codProcedimiento = null;

        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(ConstantesMeLanbide67.BARRA_SEPARADORA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }//if(numExpediente!=null && !"".equals(numExpediente))
        AdaptadorSQLBD adaptador = null;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        try {
            log.info("calculoSubvLak  - antes de adaptador");
            try {
                adaptador = getAdaptSQLBD(Integer.toString(codOrganizacion));
            } catch (Exception e) {
                log.error("Error al obtener el adaptador " + e.getMessage());
                return "2";
            }
            log.info("calculoSubvLak  - antes de Manager.getInstance()");

            MeLanbide67Manager meLanbide67Manager = MeLanbide67Manager.getInstance();

            try {
                log.info("calculoSubvLak  - recojo suplementarios ");
                codPuesto = meLanbide67Manager.getSuplementarioDesplegable(codOrganizacion, numExpediente, ejercicio, "NOMCODOCU", adaptador);
                modalidadContrato = meLanbide67Manager.getSuplementarioDesplegable(codOrganizacion, numExpediente, ejercicio, "DURACONTRA", adaptador);
                titulacion = meLanbide67Manager.getSuplementarioDesplegable(codOrganizacion, numExpediente, ejercicio, "TITPUESTRA", adaptador);
                sexo = meLanbide67Manager.getSuplementarioDesplegable(codOrganizacion, numExpediente, ejercicio, "SEXOPERCON", adaptador);
                jornada = meLanbide67Manager.getSuplementarioNumerico(codOrganizacion, numExpediente, ejercicio, "PORCJORN", adaptador);
                //
                log.info("calculoSubvLak  - recojo suplementarios - fechas");
                fecIni = meLanbide67Manager.getValorFechaDate(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_FEC_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES), adaptador);
                if (fecIni == null || fecIni.toString().equals("")) {
                    log.error("Error al recuperar la Fecha De Inicio del Contrato");
                    return "11";
                }
                fecNac = meLanbide67Manager.getValorFechaDate(codOrganizacion, numExpediente, ejercicio, ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_FEC_NAC, ConstantesMeLanbide67.FICHERO_PROPIEDADES), adaptador);
                if (fecNac == null || fecNac.toString().equals("")) {
                    log.error("Error al recuperar la Fecha De Nacimiento");
                    return "12";
                }
                log.debug("Porcentaje " + jornada);
                if (jornada == null || jornada.length() == 0) {
                    jornadaDbl = 100.0;
                } else {
                    try {
                        if (jornada.contains(",")) {
                            String[] partes = jornada.split(",");
                            String entero = (partes[0]);
                            String decimal = partes[1];
                            String convertido = entero + "." + decimal;
                            jornadaDbl = Double.parseDouble(convertido);
                        } else {
                            jornadaDbl = Double.parseDouble(jornada);
                        }
                    } catch (Exception e) {
                        log.error("Error al parsear" + e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                log.error("Ha fallado la carga de datos. " + e.getMessage(), e);
                return "3";
            }

            try {
                log.info("Llamo a calcular edad: Fecha Nacimiento " + fecNac.toString() + " - Fecha Inicio Contrato " + fecIni.toString());
                edad = MeLanbide67Utils.calcularEdad2(fecNac, fecIni);
                log.info("Edad: " + edad);
            } catch (Exception e) {
                log.error("Ha ocurrido un error al calcular la edad " + e.getMessage(), e);
                return "4";
            }
            try {
                log.info("Se va a grabar la edad ");
                String codCampoEdad = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_EDA_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
                log.debug("Cod Campo Edad: " + codCampoEdad);
                CampoSuplementarioModuloIntegracionVO campoSuplementarioEdad = new CampoSuplementarioModuloIntegracionVO();
                campoSuplementarioEdad.setCodOrganizacion(String.valueOf(codOrganizacion));
                campoSuplementarioEdad.setCodProcedimiento(codProcedimiento);
                campoSuplementarioEdad.setTipoCampo(ConstantesMeLanbide67.TIPO_DATO_NUMERICO);
                campoSuplementarioEdad.setTramite(false);
                campoSuplementarioEdad.setNumExpediente(numExpediente);
                campoSuplementarioEdad.setEjercicio(ejercicio);
                campoSuplementarioEdad.setCodigoCampo(codCampoEdad);
                campoSuplementarioEdad.setValorNumero(String.valueOf(edad));
                el.grabarCampoSuplementario(campoSuplementarioEdad);
                log.info("calculoSubvLak  - edad grabada: " + edad + " años");

            } catch (Exception e) {
                log.error("Ha ocurrido un error al grabar la edad " + e.getMessage(), e);
                return "5";
            }

            /*
            try {
                costeContrato = meLanbide67Manager.getCosteContratoLAK(ejercicio, titulacion, adaptador);
            } catch (Exception e) {
                 log.error("Ha fallado la carga de Coste Contrato. " + e.getMessage(), e);
                return "3";
            }

            try {
                log.debug("Se va a grabar el COSTE CONTRATO ");
                String codCampoCoste = ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_SUPL_COS_CON, ConstantesMeLanbide67.FICHERO_PROPIEDADES);
                CampoSuplementarioModuloIntegracionVO campoSuplementarioCoste = new CampoSuplementarioModuloIntegracionVO();
                campoSuplementarioCoste.setCodOrganizacion(String.valueOf(codOrganizacion));
                campoSuplementarioCoste.setCodProcedimiento(codProcedimiento);
                campoSuplementarioCoste.setTipoCampo(ConstantesMeLanbide67.TIPO_DATO_NUMERICO);
                campoSuplementarioCoste.setTramite(false);
                campoSuplementarioCoste.setNumExpediente(numExpediente);
                campoSuplementarioCoste.setEjercicio(ejercicio);
                campoSuplementarioCoste.setCodigoCampo(codCampoCoste);
                campoSuplementarioCoste.setValorNumero(costeContrato.toString());
                el.grabarCampoSuplementario(campoSuplementarioCoste);
            } catch (Exception e) {
                log.error("Ha ocurrido un error al grabar el COSTE CONTRATO " + e.getMessage(), e);
                return  "5";
            }
             */
// -------------------------------------
            log.info("-- PARAMETROS de ENTRADA");
            log.info("Año: " + ejercicio);
            log.info("Id. Puesto: " + codPuesto);
            log.info("Titulacion: " + titulacion);
            log.info("Modalidad Contrato: " + modalidadContrato);
            log.info("Sexo Trabajador(a): " + sexo);
            log.info("Porcentaje de jornada: " + jornadaDbl);
            String salida = MeLanbide67Manager.getInstance().calculoSubvenLAK(ejercicio, codPuesto, titulacion, modalidadContrato, sexo, jornadaDbl, adaptador);

            if (!salida.isEmpty()) {
                log.info("-- SALIDA: " + salida);
                if (salida.contains(ConstantesMeLanbide67.PIPE)) {
                    // comprobar
                    try {
                        String[] valores = salida.split("\\|");
                        importeTotal = valores[0]; //IMPTOT
                        impPago1 = valores[1]; // IMPPAGO1
                        impPago2 = valores[2]; // IMPPAGO2
                    } catch (Exception e) {
                        log.error("fallo extrayendo el resultado del calculo" + e.getMessage(), e);
                        codigoOperacion = "6";
                    }

                    log.debug("TOTAL " + importeTotal);
                    log.debug("1 " + impPago1);
                    log.debug("2 " + impPago2);

                    boolean grabaOK = false;
                    if (importeTotal != null) {
                        grabaOK = MeLanbide67Manager.getInstance().grabarDatosCalculoLAK(importeTotal, impPago1, impPago2, codOrganizacion, numExpediente, adaptador);
                    } else {
                        codigoOperacion = "5";
                    }
                    if (grabaOK) {
                        log.info("Los importes de la subvención del expediente " + numExpediente + " se han grabado correctamente");
                    } else {
                        log.error("Los importes de la subvención del expediente " + numExpediente + " no se han grabado");
                        codigoOperacion = "5";
                    }

                } else if (salida.equalsIgnoreCase("NOT_FOUND_IMP_LAK")) {
                    codigoOperacion = "7";
                    log.error("No se han encontrado datos sobre los importes para la convocatoria en curso.");

                } else if (salida.equalsIgnoreCase("NOT_FOUND_PUESTO_TRAB")) {
                    codigoOperacion = "8";
                    log.error("No se ha encontrado el puesto de trabajo con el código: " + codPuesto);
                } else {
                    log.error("Error no controlado en la función: " + salida);
                    codigoOperacion = "9";
                }

            } else {
                log.error("Respuesta vacia");
                codigoOperacion = "10";
            }

        } catch (Exception e) {
            codigoOperacion = "1";
            log.error("Error  " + e.getMessage(), e);
        }//try-catch

        log.info("------- FIN calculoSubvLak - Expediente " + numExpediente);
        return codigoOperacion;
    }// calculoSubvenLAK

    public String cargarPestanaCalculo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("=========== ENTRO en cargarPestanaCalculo");
        DatosPestanaVO datosPestana = new DatosPestanaVO();
        String mensaje = "";
        int codIdioma = 1;
        boolean deshabilitado = true;
        String estadoInicial = "0";

        try {
            if (request.getParameter("idioma") != null) {
                codIdioma = Integer.parseInt((String) request.getParameter("idioma"));
            }
        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
        try {
            AdaptadorSQLBD adapt = null;
            try {
                if (request.getParameter("codOrganizacion") != null) {
                    codOrganizacion = Integer.valueOf(request.getParameter("codOrganizacion"));
                }
                log.info("Antes de generar el Adaptador de BD");
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            } catch (Exception ex) {
                log.error("Error  " + ex.getMessage(), ex);
            }

            log.info("numExp: " + numExpediente);

            log.info("Tramite: " + codTramite);
            log.info("Ocurrencia: " + ocurrenciaTramite);
            MeLanbide67Manager meLanbide67Manager = MeLanbide67Manager.getInstance();
            log.info("Llamo a cargarDatosLak");
            try {
                datosPestana = meLanbide67Manager.cargarDatosLak(codOrganizacion, numExpediente, adapt);

            } catch (Exception e) {
                log.error("Se ha producido un error en la carga de datos de la pestaña " + e.getMessage(), e);
            }
            if (datosPestana.getCodCausa() != null && datosPestana.getCodMotivo() != null) {
                if (datosPestana.getCodCausa().equals("3") && datosPestana.getCodMotivo().equals("5")) {
                    estadoInicial = "1";
                } else if (datosPestana.getCodCausa().equals("3") && !datosPestana.getCodMotivo().equals("5")) {
                    estadoInicial = "2";
                } else if (datosPestana.getCodCausa().equals("5")) {
                    estadoInicial = "3";
                } else if (datosPestana.getCodCausa().equals("1") || datosPestana.getCodCausa().equals("2") || datosPestana.getCodCausa().equals("4")) {
                    estadoInicial = "4";
                }
            }
            /*  AJUSTES
            if (estadoInicial.equals("1") || estadoInicial.equals("4")) {
                datosPestana.setPagoNuevo1("0.00");
                datosPestana.setPagoNuevo2("0.00");
            }
            if (estadoInicial.equals("3")) {
                datosPestana.setPagoNuevo1(datosPestana.getPago1());
                datosPestana.setPagoNuevo2(datosPestana.getPago2());
            }*/
            List<SelectItem> listaCausas = meLanbide67Manager.getListaDesplegable(adapt, "CAUS");
            request.setAttribute("listaCausas", listaCausas);
            List<SelectItem> listaMotivos = meLanbide67Manager.getListaDesplegable(adapt, "MOLA");
            request.setAttribute("listaMotivos", listaMotivos);

            log.debug("-------------     Estado inicial= " + estadoInicial);
            request.setAttribute("datosPestana", datosPestana);
            request.setAttribute("estadoInicial", estadoInicial);
            request.setAttribute("numExp", numExpediente);

        } catch (Exception e) {
            log.error("Error en cargarPestanaCalculo " + e.getMessage(), e);
        }

        log.info("llamo a la pestaña");
        return "/jsp/extension/melanbide67/pestana260.jsp";

    }// cargarPestañaCalculo

    public void recalculoModSubvLakEmp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("=========== ENTRO en RecalculoModsubvlakemp");
        // CODIGOS OPERACION
        // 0 = O.K.
        // 1=ERROR_GENERICO
        // 2=ERROR_ADAPTADOR
        // 3 = CARGA DE DATOS
        // 4 = NOT_FOUND_IMP_LAK
        // 5 = ERROR DE LA FUNCION
        // 6 = ERROR_RESPUESTA
        LakModRecalculoVO datosCalculo = new LakModRecalculoVO();
        String codigoOperacion = "0";
        String salida = "";
        String importeTotal = "";
        String impPago1 = "";
        String impPago2 = "";
        String anio = "";
        String codPuesto = "";
        String titulacion = "";
        // falta
        String sexo = "";
        String jornada = "";
        String jornadaConver = "";
        String fecIni = "";
        String finEspe = "";
        String finReal = "";
        String causa = "";
        String motivo = "";
        String concedidoStr = "";
        String pagado1 = "";
        String pagado2 = "";
        try {
            AdaptadorSQLBD adapt = null;
            try {
                if (request.getParameter("codOrganizacion") != null) {
                    codOrganizacion = Integer.valueOf(request.getParameter("codOrganizacion"));
                }
                log.info("Antes de generar el Adaptador de BD");
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            } catch (Exception e) {
                log.error("Error  " + e.getMessage(), e);
                codigoOperacion = "2";
            }

            String expediente = (String) request.getParameter("numExp");
            Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(expediente);
            String ejerStr = String.valueOf(ejercicio);
            log.info("RecalculoModsubvlakemp -  antes de MeLanbide67Manager.getInstance();");
            MeLanbide67Manager meLanbide67Manager = MeLanbide67Manager.getInstance();
            try {
                titulacion = meLanbide67Manager.getSuplementarioDesplegable(codOrganizacion, expediente, ejerStr, "TITPUESTRA", adapt);
                sexo = meLanbide67Manager.getSuplementarioDesplegable(codOrganizacion, expediente, ejerStr, "SEXOPERCON", adapt);
                //     jornada = meLanbide67Manager.getSuplementarioDesplegable(codOrganizacion, expediente, ejerStr, "JORPUESTRA", adapt);
                jornada = meLanbide67Manager.getSuplementarioNumerico(codOrganizacion, expediente, ejerStr, "PORCJORN", adapt);
                log.info("RecalculoModsubvlakemp - recupero suplementarios");
                log.info("Titulación: " + titulacion);
                log.info("Sexo " + sexo);
                log.info("Porcentaje " + jornada);
                if (jornada == null || jornada.length() == 0) {

                } else {
                    try {
                        log.info("RecalculoModsubvlakemp - sustituyo ");
                        if (jornada.contains(",")) {
                            String[] partes = jornada.split(",");
                            String entero = (partes[0]);
                            String decimal = partes[1];
                            String convertido = entero + "." + decimal;
                            // jornadaDbl = Double.parseDouble(convertido);
                            jornada = convertido;
                        } else {
                            //  jornadaDbl = Double.parseDouble(jornada);
                        }
                    } catch (Exception e) {
                        log.error("Error al parsear " + e.getMessage(), e);
                    }
                }

                codPuesto = meLanbide67Manager.getSuplementarioDesplegable(codOrganizacion, expediente, ejerStr, "NOMCODOCU", adapt);
            } catch (Exception e) {
                log.error("Error  " + e.getMessage(), e);
                codigoOperacion = "3";
            }
            if (ejercicio != null) {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide67.FORMATO_FECHA);
                anio = String.valueOf(ejercicio);

                // valores reales
                causa = (String) request.getParameter("causa");
                motivo = (String) request.getParameter("motivo");

                fecIni = (String) request.getParameter("fecIni");
                finEspe = (String) request.getParameter("fecFinEspe");
                finReal = (String) request.getParameter("fecFinReal");

                concedidoStr = (String) request.getParameter("impConcedido");
                log.info("concedidoStr  " + concedidoStr);
                if (concedidoStr != null && concedidoStr.contains(",")) {
                    concedidoStr = concedidoStr.replace(".", "");
                    //if (concedidoStr.contains(",")) {
                    concedidoStr = concedidoStr.replace(",", ".");
                    log.debug("Sustituyo conce");
                }
                pagado1 = (String) request.getParameter("impPagado1");
                log.info("pagado1  " + pagado1);
                if (pagado1 != null && pagado1.contains(",")) {
                    pagado1 = pagado1.replace(".", "");
                    //if (pagado1.contains(",")) {
                    pagado1 = pagado1.replace(",", ".");
                    log.debug("Sustituyo P1");
                }
                if (pagado1.isEmpty() || pagado1.equals("")) {
                    pagado1 = "0";
                }
                pagado2 = (String) request.getParameter("impPagado2");
                log.info("pagado2  " + pagado2);
                if (pagado2 != null && pagado2.contains(",")) {
                    pagado2 = pagado2.replace(".", "");
                    //if (pagado2.contains(",")) {
                    pagado2 = pagado2.replace(",", ".");
                    log.debug("Sustituyo P2");
                }
                if (pagado2.isEmpty() || pagado2.equals("")) {
                    pagado2 = "0";
                }
                // datos forzados para probar
//                fecIni = "10/10/2018";
//                finEspe = "10/04/2019";
//                finReal = "10/04/2019";
//                causa = "3";
//                motivo = "2";
//                concedidoStr = "10000";
//                pagado1 = "6000.25";
//                pagado2 = "0";

                datosCalculo.setAnioSol(anio);
                datosCalculo.setCodPuesto(codPuesto);
                datosCalculo.setTitulacion(titulacion);
                datosCalculo.setSexo(sexo);
                datosCalculo.setJornada(jornada);
                datosCalculo.setFecIni(fecIni);
                datosCalculo.setFecFinEspe(finEspe);
                datosCalculo.setFecFinReal(finReal);
                datosCalculo.setCausa(causa);
                datosCalculo.setMotivo(motivo);
                datosCalculo.setImpConcedido(concedidoStr);
                datosCalculo.setImpPagadoPag1(pagado1);
                datosCalculo.setImpPagadoPag2(pagado2);

                log.info("OBJETO datosCalculo en el .java");
                log.info("causa - " + causa);
                log.info("motivo - " + motivo);
                log.info("f Inicio  - " + fecIni);
                log.info("f Esperada - " + finEspe);
                log.info("f Real - " + finReal);
                log.info("Concedido - " + concedidoStr);
                log.info("Pagado 1 - " + pagado1);
                log.info("pagado 2 - " + pagado2);

                log.debug("=========  JAVA");
                log.info("AÑO " + datosCalculo.getAnioSol());
                log.info("Llamo al MANAGER");
                salida = MeLanbide67Manager.getInstance().recalculoModsubvlakemp(datosCalculo, adapt);
                if (!salida.isEmpty()) {
                    log.info("-- SALIDA: " + salida);
                    if (salida.contains(ConstantesMeLanbide67.PIPE)) {
                        // comprobar
                        String[] valores = salida.split("\\|");
                        importeTotal = valores[0]; //IMPTOT
                        impPago1 = valores[1]; // IMPPAGO1
                        impPago2 = valores[2]; // IMPPAGO2
                        log.info("TOTAL " + importeTotal);
                        if (importeTotal.contains(",")) {
                            importeTotal = importeTotal.replace(",", ".");
                            log.debug("Sustituyo T");
                        }
                        log.info("Pago 1 - " + impPago1);
                        if (impPago1.contains(",")) {
                            impPago1 = impPago1.replace(",", ".");
                            log.debug("Sustituyo P1");
                        }
                        log.info("Pago 2 - " + impPago2);
                        if (impPago2.contains(",")) {
                            impPago2 = impPago2.replace(",", ".");
                            log.debug("Sustituyo P2");
                        }
                        boolean grabaOK = MeLanbide67Manager.getInstance().grabarDatosCalculoLAK(importeTotal, impPago1, impPago2, codOrganizacion, expediente, adapt);
                        if (grabaOK) {
                            log.info("Los importes de la subvención del expediente " + numExpediente + " se han grabado correctamente");
                        } else {
                            log.error("Los importes de la subvención del expediente " + numExpediente + " no se han grabado");
                        }
                    } else if (salida.equalsIgnoreCase("NOT_FOUND_IMP_LAK")) {
                        codigoOperacion = "4";
                        log.error("No se han encontrado datos sobre los importes para la convocatoria en curso.");
                    } else {
                        log.error("Error: " + salida);
                        codigoOperacion = "5";
                    }
                } else {
                    log.error("Error: " + salida);
                    codigoOperacion = "6";
                }
            } else {
                log.error("Ejercicio = null");
                codigoOperacion = "3";
            } // if ejercicio != null

        } catch (Exception e) {
            codigoOperacion = "1";
            log.error("Error  " + e.getMessage(), e);
        }// try catch
        // RESPUESTA
        log.info("Preparo el XML de respuesta");
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<SALIDA>");
        xmlSalida.append(salida);
        xmlSalida.append("</SALIDA>");
        if (codigoOperacion.equals("0")) {
            xmlSalida.append("<IMPTOT>");
            xmlSalida.append(importeTotal);
            xmlSalida.append("</IMPTOT>");
            xmlSalida.append("<PAGO1>");
            xmlSalida.append(impPago1);
            xmlSalida.append("</PAGO1>");
            xmlSalida.append("<PAGO2>");
            xmlSalida.append(impPago2);
            xmlSalida.append("</PAGO2>");
        }
        xmlSalida.append("</RESPUESTA>");

        try {
            log.info("Envio la respuesta");
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("Error  " + e.getMessage(), e);
        }//try-catch

    }// recalculoModSubvLakEmp

    public void grabarSuplementariosTramite(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("============  ENTRO EN GRABAR SUPLEMENTARIOS");
        DatosPestanaVO datosPestana = new DatosPestanaVO();
        boolean grabaOK = false;
        int codIdioma = 1;
        String codigoOperacion = "0";
        String causa = ""; // CAUSA
        String motivo = ""; // MOTIMODRESOL
        String fechaFin = ""; // FECFINMODRESOL
        String importeResIni = ""; // IMPRESOLINI
        String importeModif = ""; // IMPREMODSOL
        String pagado1 = ""; //IMPPAGPAGO1
        String pendiente2 = "";// IMPPTEPAGO2
        String aDevolver = ""; // IMPDEVOL
        try {
            if (request.getParameter("idioma") != null) {
                codIdioma = Integer.parseInt((String) request.getParameter("idioma"));
            }
        } catch (Exception ex) {
            log.error("Error  " + ex.getMessage(), ex);
        }
        AdaptadorSQLBD adapt = null;
        try {
            if (request.getParameter("codOrganizacion") != null) {
                codOrganizacion = Integer.valueOf(request.getParameter("codOrganizacion"));
            }
            log.debug("Antes de generar el Adaptador de BD");
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        } catch (Exception e) {
            log.error("Error  " + e.getMessage(), e);
        }
        MeLanbide67Manager meLanbide67Manager = MeLanbide67Manager.getInstance();
        try {
            String expediente = (String) request.getParameter("numExp");
            causa = (String) request.getParameter("causa");
            motivo = (String) request.getParameter("motivo");
            fechaFin = (String) request.getParameter("fecFinReal");
            // importes
            String impResStr = (String) request.getParameter("impResol");
            if (impResStr.contains(".") && impResStr.contains(",")) {
                impResStr = impResStr.replace(".", "");
                log.debug("Sustituyo");
            } else if (impResStr.contains(".")) {
                impResStr = impResStr.replace(".", ",");
                log.debug("Sustituyo");
            }

            importeResIni = impResStr;

            String impModStr = (String) request.getParameter("impModif");
            if (impModStr.contains(".") && impModStr.contains(",")) {
                impModStr = impModStr.replace(".", "");
                log.debug("Sustituyo T");
            } else if (impModStr.contains(".")) {
                impModStr = impModStr.replace(".", ",");
                log.debug("Sustituyo T");
            }
            importeModif = impModStr;

            String pagaStr = (String) request.getParameter("impPagado1");
            if (pagaStr.contains(".") && pagaStr.contains(",")) {
                pagaStr = pagaStr.replace(".", "");
                log.debug("Sustituyo P1");
            } else if (pagaStr.contains(".")) {
                pagaStr = pagaStr.replace(".", ",");
                log.debug("Sustituyo P1");
            }
            pagado1 = pagaStr;

            String pendStr = (String) request.getParameter("impPagado2");
            if (pendStr.contains(".") && pendStr.contains(",")) {
                pendStr = pendStr.replace(".", "");
                log.debug("Sustituyo P2");
            } else if (pendStr.contains(".")) {
                pendStr = pendStr.replace(".", ",");
                log.debug("Sustituyo P2");
            }
            pendiente2 = pendStr;

            String devoStr = (String) request.getParameter("aDevolver");
            if (devoStr.contains(".") && devoStr.contains(",")) {
                devoStr = devoStr.replace(".", "");
                log.debug("Sustituyo D");
            } else if (devoStr.contains(".")) {
                devoStr = devoStr.replace(".", ",");
                log.debug("Sustituyo D");
            }
            String importeTotalActualizado = (String) request.getParameter("importeTotalActualizado");
            if (importeTotalActualizado.contains(".") && importeTotalActualizado.contains(",")) {
                importeTotalActualizado = importeTotalActualizado.replace(".", "");
                //importeTotalActualizado = importeTotalActualizado.replace(",", ".");
                log.debug("Sustituyo . dejo solo la coma importeTotalActualizado formateado");
            } else if (importeTotalActualizado.contains(".")) {
                importeTotalActualizado = importeTotalActualizado.replace(".", ",");
                log.debug("Sustituyo importeTotalActualizado no formateado ");
            }
            String importePrimerPagoActualizado = (String) request.getParameter("importePrimerPagoActualizado");
            if (importePrimerPagoActualizado.contains(".") && importePrimerPagoActualizado.contains(",")) {
                importePrimerPagoActualizado = importePrimerPagoActualizado.replace(".", "");
                //importePrimerPagoActualizado = importePrimerPagoActualizado.replace(",", ".");
                log.debug("Sustituyo . dejo solo coma importePrimerPagoActualizado formateado");
            } else if (importePrimerPagoActualizado.contains(".")) {
                importePrimerPagoActualizado = importePrimerPagoActualizado.replace(".", ",");
                log.debug("Sustituyo importePrimerPagoActualizado no formateado");
            }

            String importeSegundoPagoActualizado = (String) request.getParameter("importeSegundoPagoActualizado");
            if (importeSegundoPagoActualizado.contains(".") && importeSegundoPagoActualizado.contains(",")) {
                importeSegundoPagoActualizado = importeSegundoPagoActualizado.replace(".", "");
                //importeSegundoPagoActualizado = importeSegundoPagoActualizado.replace(",", ".");
                log.debug("Sustituyo . y dejo solo coma importeSegundoPagoActualizado formateado");
            } else if (importeSegundoPagoActualizado.contains(".")) {
                importeSegundoPagoActualizado = importeSegundoPagoActualizado.replace(".", ",");
                log.debug("Sustituyo importeSegundoPagoActualizado no formateado");
            }

            aDevolver = devoStr;
            // valores reales
            datosPestana.setNumExp(expediente);
            datosPestana.setCodCausa(causa);
            datosPestana.setCodMotivo(motivo);
            datosPestana.setFechaFinReal(fechaFin);
            datosPestana.setImporteTotalIni(importeResIni);
            datosPestana.setTotalNuevo(importeModif);
            datosPestana.setPago1(pagado1);
            datosPestana.setPago2(pendiente2);
            datosPestana.setDevolverTotal(aDevolver);
            datosPestana.setImporteTotalActualizado(importeTotalActualizado);
            datosPestana.setImportePrimerPagoActualizado(importePrimerPagoActualizado);
            datosPestana.setImporteSegundoPagoActualizado(importeSegundoPagoActualizado);
            log.info("A grabar datos Tramite 260");
            grabaOK = meLanbide67Manager.guardarDatosTramite260(datosPestana, codOrganizacion, adapt);
            if (grabaOK) {
            } else {
                codigoOperacion = "1";
            }
        } catch (Exception e) {
            log.error("No se ha cargado el objeto. " + e.getMessage(), e);
            codigoOperacion = "2";
        }
        // RESPUESTA
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error al grabar suplementarios " + e.getMessage(), e);
        }//try-catch
    }// grabarSuplementarios

    public String validarImportesPago(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        log.info("Validar importes pago Expediente: " + numExpediente + " - Tramite: " + codTramite + " - " + ocurrenciaTramite);
        String codOperacion = "0";
        AdaptadorSQLBD adaptador = null;
        Connection con = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adaptador.getConnection();
            MeLanbide67Manager meLanbide67Manager = MeLanbide67Manager.getInstance();
            if (!meLanbide67Manager.validaImportesPago(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, con)) {
                codOperacion = "1";
                log.error("Validar importes pago Expediente: " + numExpediente + " - La suma de los importes del primer y segundo pago no coinciden con el importe total.");
            } else {
                log.debug("Validar importes pago Expediente: " + numExpediente + " - La suma de los importes del primer y segundo pago  coinciden con el importe total");
            }
        } catch (Exception e) {
            codOperacion = "2";
            log.error("Error en la operacion validarImportesPago(): " + e);
        } finally {
            if (adaptador != null) {
                adaptador.devolverConexion(con);
            }
        }
        return codOperacion;
    }// validarImportes

    public String cargarDatosExtension(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("=========== ENTRO en cargarDatosExtension");

        return "/jsp/extension/melanbide67/pestanaExtension.jsp";
    }

    public String cargarPersonaContratadaPuestoTrabajo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("=========== ENTRO en cargarPesonaContratadaPuestoTrabajo");
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide67Manager instance = MeLanbide67Manager.getInstance();

            final PersonaContratadaPuestoTrabajoVO personaContratadaPuestoTrabajoVO = instance.getDatosPersonaContratada(codOrganizacion,
                    codTramite, ocurrenciaTramite, numExpediente, adapt);
            request.setAttribute("personaContratadaPuestoTrabajo", personaContratadaPuestoTrabajoVO);

        } catch (final Exception ex) {
            log.info("Excepción en cargarPesonaContratadaPuestoTrabajo: " + ex.getMessage());
        }
        log.info("cargarPesonaContratadaPuestoTrabajo - End() " + new Date().toString());

        return "/jsp/extension/melanbide67/pestanaPersonaContratada.jsp";
    }

    public void guardarPersonaContratadaPuestoTrabajo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request,
                                                      HttpServletResponse response) {
        log.info("=========== Inicio en guardarPersonaContratadaPuestoTrabajo");

        String respuesta = null;
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
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
            final String dniPerCon = request.getParameter("dniPerCon");
            final String nomPerCon = request.getParameter("nomPerCon");
            final String apel1PerCon = request.getParameter("apel1PerCon");
            final String apel2PerCon = request.getParameter("apel2PerCon");
            final String fecNacPerCon = request.getParameter("fecNacPerCon");
            final String edadPerCon = request.getParameter("edadPerCon");
            final String sexoPerCont = request.getParameter("sexoPerCont");
            final String muniPerCon = request.getParameter("muniPerCon");
            final String experiencia6M = request.getParameter("experiencia6M");
            final String desempleadoAnterior = request.getParameter("desempleadoAnterior");
            final String sistGarantiaJuve = request.getParameter("sistGarantiaJuve");
            final String docCvInter = request.getParameter("docCvInter");
            final String docDemInter = request.getParameter("docDemInter");
            final String fechaDemInter = request.getParameter("fechaDemInter");
            final String fechaCvInter = request.getParameter("fechaCvInter");
            final String desempleado = request.getParameter("desempleado");
            final String fecIniContr = request.getParameter("fecIniContr");
            final String mujSubRepre = request.getParameter("mujSubRepre");
            final String numCtaCotSS = request.getParameter("numCtaCotSS");
            final String dirCenTrab = request.getParameter("dirCenTrab");
            final String grupCotSS = request.getParameter("grupCotSS");
            final String jorPuestra = request.getParameter("jorPuestra");
            final String modContra = request.getParameter("modContra");
            final String titPuestra = request.getParameter("titPuestra");
            final String nomCodOcu = request.getParameter("nomCodOcu");
            final String nomPuesTra = request.getParameter("nomPuesTra");
            final String fecReFinContr = request.getParameter("fecReFinContr");
            final String fecFinContr = request.getParameter("fecFinContr");
            final String salarioMin = request.getParameter("salarioMin");
            final String titulacionEspecifica = request.getParameter("titulacionEspecifica");
            final String cosConReal = request.getParameter("cosConReal");
            final String porcJorn = request.getParameter("porcJorn");
            final String duraContra = request.getParameter("duraContra");

            final List<DetallesCampoSuplementarioVO> detallesCampoSuplementarioVOs = new ArrayList<DetallesCampoSuplementarioVO>();

            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(dniPerCon, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(nomPerCon, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(apel1PerCon, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(apel2PerCon, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(fecNacPerCon, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(edadPerCon, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(sexoPerCont, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(muniPerCon, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(experiencia6M, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(desempleadoAnterior, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(sistGarantiaJuve, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(docCvInter, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(docDemInter, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(fechaDemInter, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(fechaCvInter, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(desempleado, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(fecIniContr, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(mujSubRepre, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(numCtaCotSS, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(dirCenTrab, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(grupCotSS, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(jorPuestra, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(modContra, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(titPuestra, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(nomCodOcu, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(nomPuesTra, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(fecReFinContr, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(fecFinContr, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(salarioMin, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(titulacionEspecifica, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(cosConReal, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(porcJorn, null, null, null, null, null));
            detallesCampoSuplementarioVOs.add(new DetallesCampoSuplementarioVO(duraContra, null, null, null, null, null));

            int res = MeLanbide67Manager.getInstance().guardarDatosPersonaContradata(codOrganizacion, datosExpediente, codMunicipio,
                    detallesCampoSuplementarioVOs, adaptador);

            respuesta = Integer.valueOf(res).toString();
        } catch (final Exception ex) {
            respuesta = "1";
        }
        log.info("guardarPersonaContratadaPuestoTrabajo - End() " + new Date().toString());
        RespuestaAjaxUtils.retornarJSON(gson.toJson(respuesta), response);
    }

    public void obtainDocumentFile(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Inicio de obtainDocumentFile de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        if (adapt != null) {
            try {
                final String ejercicio = request.getParameter("ejercicio");
                final String codCampo = request.getParameter("codCampo");
                final CamposFormulario camposFormularioFicheros = MeLanbide67Manager.getInstance().getFicheros(codOrganizacion, numExpediente, ejercicio, codCampo, adapt);

                final String nameFile = request.getParameter("nameFile");
                if (camposFormularioFicheros.contieneCampo(codCampo + "_NOMBRE")) { //nameFile.split("\\.")[0])) {
                    final Object obj = camposFormularioFicheros.get(codCampo); //nameFile.split("\\.")[0]);
                    if (obj != null) {
                        final Blob file = (Blob) obj;
                        InputStream inputStream = file.getBinaryStream();
                        OutputStream outputStream = new FileOutputStream(nameFile);

                        int bytesRead = -1;
                        byte[] buffer = new byte[BUFFER_SIZE];
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                        outputStream.close();
                        final File docFich = new File(nameFile);
                        prepareResponseWithFile(docFich, response);
                    }
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos de contrataciones - MELANBIDE85 - obtainDocumentFile", ex);
            }
        }
        log.info("Fin de obtainDocumentFile de " + this.getClass().getName());
    }

    private void prepareResponseWithFile(final File documento,
                                         final HttpServletResponse response) {
        FileInputStream istr = null;
        try {
            istr = new FileInputStream(documento.getAbsolutePath());
            BufferedInputStream bstr = new BufferedInputStream(istr);
            int size = (int) documento.length();
            byte[] data = new byte[size];
            try {
                bstr.read(data, 0, size);
                bstr.close();
            } catch (IOException ex) {
                log.error("prepareResponseWithFile: Excepción al leer bstr " + ex.getMessage());
            }
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + documento.getName());
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setContentLength(data.length);
            try {
                response.getOutputStream().write(data, 0, data.length);
            } catch (IllegalStateException ex) {
                log.error("prepareResponseWithFile: IllegalStateException " + ex.getMessage());
            } catch (IOException ex) {
                log.error("prepareResponseWithFile: IOException " + ex.getMessage());
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (FileNotFoundException ex) {
            log.error("prepareResponseWithFile: Fichero no encontrado " + ex.getMessage());
        } catch (IOException ex) {
            log.error("IllegalStateException: Exception in flush or close " + ex.getMessage());
        } finally {
            documento.delete();
            try {
                istr.close();
            } catch (IOException ex) {
                log.error("prepareResponseWithFile: Error al cerrar istr " + ex.getMessage());
            }
        }
    }

    public void recuperarDatosIntermediacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("=========== ENTRO en recuperarDatosIntermediacion");

        AdaptadorSQLBD adaptador = null;
        EntidadColaboradoraVO entidadColaboradora = new EntidadColaboradoraVO();
        PersonaContratadaVO personaContratada = new PersonaContratadaVO();

        String numExp = "";
        String codigoError = "0";
        String xmlSalida = null;

        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide67Manager manager = MeLanbide67Manager.getInstance();

            // A partir del numero de expediente obtenemos los datos de Número de Oferta y NIF de la persona contratada de Datos Suplementarios, y el NIF de la persona responsable de la Entidad Colaboradora
            String numeroOferta = manager.getDatosSuplementariosNumeroOferta(numExpediente, adaptador);
            String nifPersonaContratada = manager.getDatosSuplementariosNIFPersonaContratada(numExpediente, adaptador);
            String nifPersonaResponsableEntidadColaboradora = manager.getDatosSuplementariosNIFEntidadColaboradora(numExpediente, adaptador);

            if ((null != numeroOferta) && (!numeroOferta.isEmpty())) {
                //Se informa del numero de oferta
                if ((null != nifPersonaContratada) && (!nifPersonaContratada.isEmpty())) {
                    entidadColaboradora = manager.getDatosEntidadColaboradora(numeroOferta, adaptador);
                    personaContratada = manager.getDatosPersonaContratada(nifPersonaContratada, numeroOferta, adaptador);

                    if (null != personaContratada.getDni() && null != entidadColaboradora.getNif()) {
                        manager.setDatosEntidadColaboradora(numExpediente, entidadColaboradora, adaptador);
                        manager.setDatosPersonaContratada(numExpediente, personaContratada, adaptador);
                    } else {
                        codigoError = "4";
                    }
                } else {
                    codigoError = "1";
                }
            } else {
                if ((null != nifPersonaContratada) && (!nifPersonaContratada.isEmpty())) {
                    codigoError = "2";
                } else {
                    codigoError = "3";
                }
            }

        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
        } catch (Exception ex) {
            log.error("Ha ocurrido un error al recuperar valores de datos suplementarios para el expediente " + numExpediente, ex);
        } finally {
            retornarSalida(codigoError, response);
        }
    }

    private void retornarSalida(String codigoError, HttpServletResponse response) {
        log.info("retornarSalida - INICIO");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = gsonBuilder.create();
        String respuestaJsonString = gson.toJson(codigoError);

        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(respuestaJsonString);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("Error preparando respuesta " + e.getMessage(), e);
            e.printStackTrace();
        }
        log.info("retornarSalida - FIN");
    }

    public void recuperarDatosCVIntermediacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("recuperarDatosCVIntermediacion - Begin() " + new Date().toString());
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        int idioma;
        final HttpSession session = request.getSession();
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        SearchCVResponseDTO responseDTO = null;
        String respuestaServicio = null;

        try {
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
            } else {
                idioma = -1;
            }

            String nifPersonaContratada = MeLanbide67Manager.getInstance().getDatosSuplementariosNIFPersonaContratada(numExpediente, adapt);
            LlamadaServicioCV llamadaServicioCV = new LlamadaServicioCV();
            if (nifPersonaContratada != null && nifPersonaContratada.charAt(0) >= 'A' && nifPersonaContratada.charAt(0) <= 'Z') {
                llamadaServicioCV.setTipoDoc(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_NIE_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            } else {
                llamadaServicioCV.setTipoDoc(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_DNI_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            }
            llamadaServicioCV.setNumDoc(nifPersonaContratada);
            llamadaServicioCV.setDocumento(GeneratedDocuments.CV.getCode()); //ConfigurationParameter.getParameter(ConstantesMeLanbide67.DOCUMENTO_CV, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            llamadaServicioCV.setIdioma(Integer.toString(idioma));

            log.info("recuperarDatosCVIntermediacion parametrosRecibidos: tipoDoc=" + llamadaServicioCV.getTipoDoc()
                    + ", numDoc=" + llamadaServicioCV.getNumDoc() + ", documento=" + llamadaServicioCV.getDocumento()
                    + ", idioma=" + llamadaServicioCV.getIdioma() + ", servicioWS="
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide67.URL_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            LangaiVision360WSFacade langaiVision360WSFacade = new LangaiVision360WSFacade(ConfigurationParameter.getParameter(ConstantesMeLanbide67.URL_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));

            final Date fechaCVInter = new Date();
            responseDTO = langaiVision360WSFacade.generateApplicantCV(llamadaServicioCV.getTipoDoc(),
                    llamadaServicioCV.getNumDoc(), llamadaServicioCV.getDocumento(), llamadaServicioCV.getIdioma());

            log.info("recuperarDatosCVIntermediacion: responseDTO = " + responseDTO.getUrl() + ", " + responseDTO.hasError());
            if (responseDTO != null && !responseDTO.hasError()) {
                respuestaServicio = responseDTO.getUrl();
                // POR PROBLEMAS EN INTERMEDIACION SE COMENTA PROVISIONALMENTE
                /*
                final File cv = getFileCV(responseDTO.getUrl());
                if (cv != null) {
                    log.info("recuperarDatosCVIntermediacion cv " + cv.getAbsolutePath() + ", " + cv.getName() + ", "
                            + cv.length());
                    MeLanbide67Manager.getInstance().setDatosCVIntermediacion(numExpediente, fechaCVInter,
                            cv, adapt);
                    cv.delete();
                } else {
                    log.info("recuperarDatosCVIntermediacion: No hemos guardado el cv");
                }
                 */
                // Y SE HACE ESTO
                MeLanbide67Manager.getInstance().setDatosCVIntermediacionProvisional(numExpediente, fechaCVInter,respuestaServicio, adapt);
            }
        } catch (final RemoteServiceException e) {
            log.error("recuperarDatosCVIntermediacion WS_langaiVision360WSFacade - Exception general al llamar al WS langaiVision360WSFacade. "
                    + numExpediente + ", " + e.getMessage() + ", " + e.getLinkedCause());
        } catch (Exception ex) {
            log.error("recuperarDatosCVIntermediacion WS_langaiVision360WSFacade - Exception general al llamar al WS langaiVision360WSFacade. "
                    + numExpediente + ", " + ex.getMessage()
            );
            if (responseDTO != null) {
                respuestaServicio = Integer.toString(responseDTO.getErrorCode());
                log.error("recuperarDatosCVIntermediacion respuestaServicio: " + respuestaServicio);
            } else {
                respuestaServicio = "-2";
            }
        } finally {
            setResponse("recuperarDatosCVIntermediacion error preparando respuesta", response, respuestaServicio);
        }
        log.info("recuperarDatosCVIntermediacion - End() " + new Date().toString());
    }

    public void recuperarDatosDemandaIntermediacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("recuperarDatosDemandaIntermediacion - Begin() " + new Date().toString());
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        int idioma;
        final HttpSession session = request.getSession();
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        SearchCVResponseDTO responseDTO = null;
        String respuestaServicio = null;

        try {
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
            } else {
                idioma = 1;
            }

            String nifPersonaContratada = MeLanbide67Manager.getInstance().getDatosSuplementariosNIFPersonaContratada(numExpediente, adapt);
            LlamadaServicioCV llamadaServicioCV = new LlamadaServicioCV();
            if (nifPersonaContratada != null && nifPersonaContratada.charAt(0) >= 'A' && nifPersonaContratada.charAt(0) <= 'Z') {
                llamadaServicioCV.setTipoDoc(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_NIE_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            } else {
                llamadaServicioCV.setTipoDoc(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_DNI_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            }
            llamadaServicioCV.setNumDoc(nifPersonaContratada);
            llamadaServicioCV.setDocumento(GeneratedDocuments.DEMANDA.getCode()); //ConfigurationParameter.getParameter(ConstantesMeLanbide67.DOCUMENTO_DEMANDA, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            llamadaServicioCV.setIdioma(Integer.toString(idioma));

            log.info("recuperarDatosDemandaIntermediacion parametrosRecibidos: tipoDoc=" + llamadaServicioCV.getTipoDoc()
                    + ", numDoc=" + llamadaServicioCV.getNumDoc() + ", documento=" + llamadaServicioCV.getDocumento()
                    + ", idioma=" + llamadaServicioCV.getIdioma() + ", servicioWS="
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide67.URL_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            LangaiVision360WSFacade langaiVision360WSFacade = new LangaiVision360WSFacade(ConfigurationParameter.getParameter(ConstantesMeLanbide67.URL_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));

            final Date fechaDemandaInter = new Date();
            responseDTO = langaiVision360WSFacade.generateApplicantCV(llamadaServicioCV.getTipoDoc(),
                    llamadaServicioCV.getNumDoc(), llamadaServicioCV.getDocumento(), llamadaServicioCV.getIdioma());

            log.info("recuperarDatosDemandaIntermediacion: responseDTO = " + responseDTO.getUrl() + ", " + responseDTO.hasError());
            if (responseDTO != null && !responseDTO.hasError()) {
                respuestaServicio = responseDTO.getUrl();
                // POR PROBLEMAS EN INTERMEDIACION SE COMENTA PROVISIONALMENTE
          /*      final File demanda = getFileCV(responseDTO.getUrl());
                if (demanda != null) {
                    log.info("recuperarDatosDemandaIntermediacion cv " + demanda.getAbsolutePath() + ", " + demanda.getName() + ", "
                            + demanda.length());
                    MeLanbide67Manager.getInstance().setDatosDemandaIntermediacion(numExpediente, fechaDemandaInter,
                            demanda, adapt);
                    demanda.delete();
                } else {
                    log.info("recuperarDatosDemandaIntermediacion: No hemos guardado la demanda");
                }*/
                // Y SE HACE ESTO
                MeLanbide67Manager.getInstance().setDatosDemandaIntermediacionProvisional(numExpediente, fechaDemandaInter,respuestaServicio, adapt);
            }
        } catch (final RemoteServiceException e) {
            log.error("recuperarDatosDemandaIntermediacion WS_langaiVision360WSFacade - Exception general al llamar al WS langaiVision360WSFacade. "
                    + numExpediente + ", " + e.getMessage() + ", " + e.getLinkedCause());
        } catch (Exception ex) {
            log.error("recuperarDatosDemandaIntermediacion WS_langaiVision360WSFacade - Exception general al llamar al WS langaiVision360WSFacade. "
                    + numExpediente + ", " + ex.getMessage());
            if (responseDTO != null) {
                respuestaServicio = Integer.toString(responseDTO.getErrorCode());
                log.error("recuperarDatosDemandaIntermediacion respuestaServicio: " + respuestaServicio);
            } else {
                respuestaServicio = "-2";
            }
        } finally {
            setResponse("recuperarDatosDemandaIntermediacion error preparando respuesta ", response, respuestaServicio);
        }
        log.info("recuperarDatosDemandaIntermediacion - End() " + new Date().toString());
    }

    public SearchCVResponseDTO recuperarDatosCVoDemandaIntermediacion(int codOrganizacion,
                                                                      final String numDoc, final String doc, final String numExpediente,
                                                                      final HttpServletRequest request, final HttpServletResponse response) {
        log.info("recuperarDatosDemandaIntermediacion - Begin() " + new Date().toString());
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        int idioma;
        final HttpSession session = request.getSession();
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        SearchCVResponseDTO responseDTO = null;
        String respuestaServicio = null;

        try {
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                idioma = usuarioVO.getIdioma();
            } else {
                idioma = 1;
            }

            LlamadaServicioCV llamadaServicioCV = new LlamadaServicioCV();
            if (numDoc != null && numDoc.charAt(0) >= 'A'
                    && numDoc.charAt(0) <= 'Z') {
                llamadaServicioCV.setTipoDoc(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_NIE_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            } else {
                llamadaServicioCV.setTipoDoc(ConfigurationParameter.getParameter(ConstantesMeLanbide67.TIPO_DNI_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            }
            llamadaServicioCV.setNumDoc(numDoc);
            llamadaServicioCV.setDocumento(doc);
            llamadaServicioCV.setIdioma(Integer.toString(idioma));

            log.info("recuperarDatosDemandaIntermediacion parametrosRecibidos: tipoDoc=" + llamadaServicioCV.getTipoDoc()
                    + ", numDoc=" + llamadaServicioCV.getNumDoc() + ", documento=" + llamadaServicioCV.getDocumento()
                    + ", idioma=" + llamadaServicioCV.getIdioma() + ", servicioWS="
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide67.URL_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));
            LangaiVision360WSFacade langaiVision360WSFacade = new LangaiVision360WSFacade(ConfigurationParameter.getParameter(ConstantesMeLanbide67.URL_CV_WS, ConstantesMeLanbide67.FICHERO_PROPIEDADES));

            responseDTO = langaiVision360WSFacade.generateApplicantCV(llamadaServicioCV.getTipoDoc(), llamadaServicioCV.getNumDoc(), doc, llamadaServicioCV.getIdioma());

            log.info("recuperarDatosDemandaIntermediacion: responseDTO = " + responseDTO.getUrl() + ", " + responseDTO.hasError());
            if (responseDTO != null && !responseDTO.hasError()) {
                return responseDTO;
            }
        } catch (final RemoteServiceException e) {
            log.error("recuperarDatosDemandaIntermediacion WS_langaiVision360WSFacade - Exception general al llamar al WS langaiVision360WSFacade. "
                    + numExpediente + ", " + e.getMessage() + ", " + e.getLinkedCause());
            return new SearchCVResponseDTO(null, -2);
        } catch (Exception ex) {
            log.error("recuperarDatosDemandaIntermediacion WS_langaiVision360WSFacade - Exception general al llamar al WS langaiVision360WSFacade. "
                    + numExpediente + ", " + ex.getMessage());
            if (responseDTO != null) {
                respuestaServicio = Integer.toString(responseDTO.getErrorCode());
                log.error("recuperarDatosDemandaIntermediacion respuestaServicio: " + respuestaServicio);
                return responseDTO;
            } else {
                return new SearchCVResponseDTO(null, -2);
            }
        }
        return new SearchCVResponseDTO(null, -2);
    }

    private void setResponse(final String msg, final HttpServletResponse response, final String respuestaServicio) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        final Gson gson = gsonBuilder.create();
        String resultadoJsonString = gson.toJson(respuestaServicio);
        try {
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(resultadoJsonString);
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error(msg + e.getMessage(), e);
        }
    }

    private File getFileCV(final String fileUrl) throws MalformedURLException, IOException {
        log.info("getFileCV - Begin()");
        String nameFile;
        final String[] partsUrl = fileUrl.split("/");
        if (partsUrl != null && partsUrl.length > 0) {
            nameFile = partsUrl[partsUrl.length - 1];
        } else {
            return null;
        }
        log.info("getFileCV - nameFile = " + nameFile);
        final BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
        log.info("getFileCV - in = " + in);
        final FileOutputStream fileOutputStream = new FileOutputStream(nameFile);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
//            log.info("getFileCV - bytesRead = " + bytesRead);
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }

        fileOutputStream.close();
        in.close();
        log.info("getFileCV - End()");
        return new File(nameFile);
    }

    public String cargarPantallaSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaSubSolic de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide67/subSolic.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<SubSolicVO> listaSubSolic = MeLanbide67Manager.getInstance().getDatosSubSolic(numExpediente, codOrganizacion, adapt);
                if (!listaSubSolic.isEmpty()) {
                    for (SubSolicVO lm : listaSubSolic) {
                        lm.setDesEstado(m67Utils.getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                    request.setAttribute("listaSubSolic", listaSubSolic);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos de SubSolic - MELANBIDE67 - cargarPantallaPrincipal", ex);
            }
        }
        return url;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String cargarNuevoSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoSubSolic - ");
        String nuevo = "1";
        String urlnuevoSubSolic = "/jsp/extension/melanbide67/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }
            cargarDesplegables(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de un nuevo SubSolic : " + ex.getMessage());
        }
        return urlnuevoSubSolic;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String cargarModificarSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarSubSolic - " + numExpediente);
        String nuevo = "0";
        String urlnuevoSubSolic = "/jsp/extension/melanbide67/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                log.info("Entramos en HAY NUEVO - " + numExpediente);

                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                SubSolicVO datModif = MeLanbide67Manager.getInstance().getSubSolicPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            cargarDesplegables(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificaci? " + ex.getMessage());
        }
        return urlnuevoSubSolic;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void crearNuevoSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoSubSolic - " + numExpediente);
        String codigoOperacion = "-1";
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();
        SubSolicVO nuevaSubSolic = new SubSolicVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");
            String estado = request.getParameter("estado");
            String organismo = request.getParameter("organismo");
            String objeto = request.getParameter("objeto");
            String importe = request.getParameter("importe").replace(",", ".");
            String fecha = request.getParameter("fecha");

            nuevaSubSolic.setNumExp(numExp);
            nuevaSubSolic.setEstado(estado);
            nuevaSubSolic.setOrganismo(organismo);
            nuevaSubSolic.setObjeto(objeto);
            if (importe != null && !"".equals(importe)) {
                nuevaSubSolic.setImporte(Double.valueOf(importe));
            }
            if (fecha != null && !"".equals(fecha)) {
                nuevaSubSolic.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
            }

            MeLanbide67Manager meLanbide67Manager = MeLanbide67Manager.getInstance();
            boolean insertOK = meLanbide67Manager.crearNuevoSubSolic(nuevaSubSolic, adapt);
            if (insertOK) {
                log.debug("SubSolic insertada correctamente");
                codigoOperacion = "0";
                lista = meLanbide67Manager.getDatosSubSolic(numExp, codOrganizacion, adapt);
                if (!lista.isEmpty()) {
                    for (SubSolicVO lm : lista) {
                        lm.setDesEstado(m67Utils.getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                }
            } else {
                log.error("NO se ha insertado correctamente la nueva SubSolic");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m67Utils.retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @throws Exception
     */
    public void modificarSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("Entramos en modificarSubSolic - " + numExpediente);
        String codigoOperacion = "-1";
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String estado = request.getParameter("estado");
            String organismo = request.getParameter("organismo");
            String objeto = request.getParameter("objeto");
            String importe = request.getParameter("importe").replace(",", ".");
            String fecha = request.getParameter("fecha");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la SubSolic a Modificar ");

            } else {
                SubSolicVO datModif = new SubSolicVO();
                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setEstado(estado);
                datModif.setOrganismo(organismo);
                datModif.setObjeto(objeto);
                if (importe != null && !"".equals(importe)) {
                    datModif.setImporte(Double.valueOf(importe));
                }
                if (fecha != null && !"".equals(fecha)) {
                    datModif.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
                }

                MeLanbide67Manager meLanbide67Manager = MeLanbide67Manager.getInstance();
                boolean modOK = meLanbide67Manager.modificarSubSolic(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = meLanbide67Manager.getDatosSubSolic(numExp, codOrganizacion, adapt);
                        if (!lista.isEmpty()) {
                            for (SubSolicVO lm : lista) {
                                lm.setDesEstado(m67Utils.getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de SubSolic después de Modificar un SubSolic : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de SubSolic después de Modificar un SubSolic : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "3";
                    log.error("Error al Modificar un SubSolic  ");
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error modificar --- ", ex);
            codigoOperacion = "3";
        } catch (ParseException ex) {
            log.error("Error modificar --- ", ex);
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m67Utils.retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void eliminarSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarSubSolic - " + numExpediente);
        String codigoOperacion = "-1";
        List<SubSolicVO> lista = new ArrayList<SubSolicVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la SubSolic a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide67Manager.getInstance().eliminarSubSolic(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide67Manager.getInstance().getDatosSubSolic(numExp, codOrganizacion, adapt);
                        if (!lista.isEmpty()) {
                            for (SubSolicVO lm : lista) {
                                lm.setDesEstado(m67Utils.getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de SubSolic después de eliminar un SubSolic");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error eliminando un SubSolic: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m67Utils.retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     * Método que recupera los valores de los desplegables del módulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegables(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaEstado = MeLanbide67Manager.getInstance().getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide67.CAMPO_ESTADO, ConstantesMeLanbide67.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaEstado.isEmpty()) {
            listaEstado = m67Utils.traducirDesplegable(request, listaEstado);
            request.setAttribute("listaEstado", listaEstado);
        }
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     *
     * @return
     */
    public String cargaSeccionIAE(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        //String valorCampoIAEE = MeLanbide67Manager.getInstance().getValorCampoSuplementarioIAEELAK(numExpediente, adaptador);
        String valorCampoIAEELAK = null;
        String seccion = null;
        try {
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error  " + ex.getMessage(), ex);
            }
            try {
                valorCampoIAEELAK = MeLanbide67Manager.getInstance().getValorCAmpoIAEELAK(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, valorCampoIAEELAK, adapt);
                //request.setAttribute("valorCampoIAEELAK", valorCampoIAEELAK);
                seccion = MeLanbide67Manager.getInstance().getSeccionIAEELAK(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, valorCampoIAEELAK, adapt);
                MeLanbide67Manager.getInstance().guardarDatosDeSeccionEnElExpediente(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, seccion, adapt);
            } catch (Exception ex) {
                log.error("Error  " + ex.getMessage(), ex);
            }

        } catch (Exception ex) {
            log.error("Error en cargaSeccionIAE " + ex.getMessage(), ex);
        }
        return null;
    }

    public void guardarDatosDeSeccionEnElExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, String valor, HttpServletRequest request, HttpServletResponse response) {
        Integer ejercicio = MeLanbide67Utils.getEjercicioDeExpediente(numExpediente);
        AdaptadorSQLBD adapt = null;
        try {
            MeLanbide67Manager.getInstance().guardarDatosDeSeccionEnElExpediente(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, valor, adapt);
        } catch (Exception ex) {
            log.error("Error al guardar datos  " + ex.getMessage(), ex);
        }
    }

    // INICIO Tarea  #858265 abierta Anterior | 14/15 | Siguiente » UAAP - [39448] - Interoperabilidad - Consulta vida laboral desde la ficha de expediente
    public String cargarVidaLaboral(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                    HttpServletRequest request, HttpServletResponse response) {

        log.info("=========== ENTRO en cargarVidaLaboral");
        melanbide_interop.cargarVidaLaboral(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                request, response);
        return "/jsp/extension/melanbide67/pestanaVidaLaboral.jsp";
    }

    public void consultaVidaLaboralCVLBatchExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite,
                                                      String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String nifPersonaContratada = MeLanbide67Manager.getInstance().getDatosSuplementariosNIFPersonaContratada(numExpediente, adapt);
            final Persona persona = new Persona("", "", "", getTipoDocumento(nifPersonaContratada),
                    nifPersonaContratada);


            melanbide_interop.consultaVidaLaboralCVLBatchExpediente(codOrganizacion, codTramite, ocurrenciaTramite,
                    Arrays.asList(persona),
                    numExpediente, request, response);
        } catch (final Exception ex) {
            log.error ("Error en consultaVidaLaboralCVLBatchExpediente " + ex.getMessage());
        }
    }

    private String getTipoDocumento(final String numDocumento) {
        String tipoDocumento = null;
        if (numDocumento != null && numDocumento.length() >= 1
                && numDocumento.charAt(0) >= 'A'
                && numDocumento.charAt(0) <= 'Z') {
            tipoDocumento = "NIE";
        } else {
            tipoDocumento = "DNI";
        }
        return tipoDocumento;
    }
}// class
