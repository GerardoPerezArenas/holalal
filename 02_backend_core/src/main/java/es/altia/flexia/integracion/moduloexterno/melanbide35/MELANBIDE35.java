/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide35;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide35.exception.ExcelRowMappingException;
import es.altia.flexia.integracion.moduloexterno.melanbide35.exception.FilaProspectorSolicitudNoValidaException;
import es.altia.flexia.integracion.moduloexterno.melanbide35.exception.FilaSeguimientoPrepNoValidaException;
import es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide35.manager.MeLanbide35Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35;
import es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35InformeUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.Eca23ConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.EcaConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.informes.FilaInformeDesglose;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.informes.FilaInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.informes.FilaResumenInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaInsPreparadores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaSegPreparadores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaPreparadorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaProspectorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusInsercionesECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusPreparadoresECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusSeguimientosECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JustificacionECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.procesos.FilaAuditoriaProcesosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.EcaResDetalleInsercionesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.EcaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.FilaEcaResPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.resumen.FilaEcaResProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.DatosAnexosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolValoracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitud23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaInsercionesECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaProspectorECA23VO;
import es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaProspectorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.MELANBIDE44;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide73.manager.MeLanbide73Manager;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import es.altia.technical.PortableContext;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.opensaml.xml.signature.J;


/**
 *
 * @author santiagoc
 */
public class MELANBIDE35 extends ModuloIntegracionExterno {

    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE35.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();

    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);

        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaSolicitudPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            MELANBIDE44 meLanbide44 = new MELANBIDE44();
            int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
            String[] exp = numExpediente.split("/");
            int num = Integer.parseInt(exp[2]);
            if ((anio == 2023 && num>10) || anio>2023  ) {
                log.info("Expediente posterior al 2023/ECA/000010");
                AdaptadorSQLBD adapt = null;
                try {
                    adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
                } catch (Exception ex) {
                    log.error("Error:", ex);
                }
                String url = null;

                if (adapt != null) {
                    try {
                        // Pestana Solicitud
                        EcaSolicitud23VO sol = MeLanbide35Manager.getInstance().getDatosSolicitudECA23(numExpediente, adapt);
                        if (sol != null) {
                            request.setAttribute("SolicitudECA23", sol);
                        }
                        Eca23ConfiguracionVO importes = MeLanbide35Manager.getInstance().getImportesECA23(String.valueOf(anio), adapt);
                        if (importes != null) {
                            request.setAttribute("configuracion", importes);
                        }
                        try {
                            url = cargarSubpestanaInsercion_SolicitudECA23(sol, adapt, request);
                            if (url != null) {
                                log.debug("urlPestanaInsercion_solicitudECA23 " + url);
                                request.setAttribute("urlPestanaInsercion_solicitudECA23", url);
                            }
                        } catch (Exception ex) {
                            log.error("Error:", ex);
                        }
                        //Subpestana PREPARADORES
                        try {
                            url = cargarSubpestanaPreparador_SolicitudECA23(sol, adapt, request);
                            if (url != null) {
                                log.debug("urlPestanaPreparador_solicitudECA23 " + url);
                                request.setAttribute("urlPestanaPreparador_solicitudECA23", url);
                            }
                        } catch (Exception ex) {
                            log.error("Error al cargarSubpestanaPreparador_SolicitudECA23: ", ex);
                        }
                        //Subpestana PROSPECTORES
                        try {
                            url = cargarSubpestanaProspector_SolicitudECA23(sol, adapt, request);
                            if (url != null) {
                                log.debug("urlPestanaProspector_solicitudECA23 " + url);
                                request.setAttribute("urlPestanaProspector_solicitudECA23", url);
                            }
                        } catch (Exception ex) {
                            log.error("Error al cargarSubpestanaProspector_SolicitudECA23: ", ex);
                        }

                    } catch (Exception ex) {
                        log.error("Error:", ex);
                    }
                    return "/jsp/extension/melanbide35/solicitud23/solicitud23.jsp";
                }
            } else if (anio > 2014) {
                log.debug("anio > 2014");
                return meLanbide44.cargarPantallaSolicitudPrincipal(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
            } else {
                log.debug("anio < 2014");
                AdaptadorSQLBD adapt = null;
                try {
                    adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                } catch (Exception ex) {
                }
                String url = null;

                try {

                    EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(anio, adapt);
                    request.getSession().removeAttribute("ecaConfiguracion");
                    if (config != null) {
                        request.getSession().setAttribute("ecaConfiguracion", config);
                    }
                } catch (Exception ex) {
                }

                // Pestana SOLICITUD
                if (adapt != null) {
                    try {
                        EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                        if (sol != null) {
                            request.setAttribute("ecaSolicitud", sol);
                        }

                        try {
                            url = cargarSubpestanaSolicitud_Solicitud(sol, adapt, request);
                            if (url != null) {
                                request.setAttribute("urlPestanaSolicitud_solicitud", url);
                            }
                        } catch (Exception ex) {
                        }

                        //Subpestana PREPARADORES
                        try {
                            url = cargarSubpestanaPreparadores_Solicitud(sol, adapt, request);
                            if (url != null) {
                                request.setAttribute("urlPestanaPreparadores_solicitud", url);
                            }
                        } catch (Exception ex) {
                        }

                        //Subpestana PROSPECTORES
                        try {
                            url = cargarSubpestanaProspectores_Solicitud(sol, adapt, request);
                            if (url != null) {
                                request.setAttribute("urlPestanaProspectores_solicitud", url);
                            }
                        } catch (Exception ex) {
                        }

                        //Subpestana VALORACION
                        try {
                            url = cargarSubpestanaValoracion_Solicitud(sol, adapt, request);
                            if (url != null) {
                                request.setAttribute("urlPestanaValoracion_solicitud", url);
                            }
                        } catch (Exception ex) {

                        }
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception ex) {
        }
        return "/jsp/extension/melanbide35/solicitud/solicitud_principal.jsp";
    }

    public String cargarPantallaJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
        }
        String url = null;
        //Subpestana JUSTIFICACION
        if (adapt != null) {
            try {
                int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
                if (anio > 2023) {
                    Eca23ConfiguracionVO configuracion = obtenerImportePorDiscapacidadECA23(String.valueOf(anio), adapt);
                    request.setAttribute("configuracion", configuracion);
                    // pestana JUSTIFICACION 23
                    JustificacionECA23VO justificacion = new JustificacionECA23VO();
                    double importeEca = 0;
                    double importeValidado = 0;
                    double totalInserciones = 0;
                    double totalSeguimientos = 0;
                    importeValidado = MeLanbide35Manager.getInstance().getSuplementarioImporte(numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide35.CAMPO_IMPORTE_VALIDADO_23, ConstantesMeLanbide35.FICHERO_PROPIEDADES), adapt);
                    totalInserciones = MeLanbide35Manager.getInstance().getTotalJusInsercionesECA23(numExpediente, adapt);
                    totalSeguimientos = MeLanbide35Manager.getInstance().getTotalJusSegumientosECA23(numExpediente, adapt);
                    importeEca = totalInserciones + totalSeguimientos;
                    justificacion.setNumExpediente(numExpediente);
                    justificacion.setImporteEca(importeEca);
                    justificacion.setImporteValidado(importeValidado);
                    justificacion.setTotalInserciones(totalInserciones);
                    justificacion.setTotalSeguimientos(totalSeguimientos);
                    request.setAttribute("justificacion", justificacion);
                    try {
                        // JUSTIFICACION INSERCIONES
                        url = cargarSubPestanaInserciones_JustificacionECA23(numExpediente, request, adapt);
                        if (url != null) {
                            request.setAttribute("urlPestanaInserciones_JustificacionECA23", url);
                        }
                    } catch (Exception e) {
                    }
                    try {
                        // JUSTIFICACION PREPARADORES
                        url = cargarSubPestanaPreparadores_JustificacionECA23(numExpediente, request, adapt);
                        if (url != null) {
                            request.setAttribute("urlPestanaPreparadores_JustificacionECA23", url);
                        }
                    } catch (Exception e) {
                    }
                    return "/jsp/extension/melanbide35/justificacion23/justificacion23.jsp";

                } else if (anio > 2014) {
                    MELANBIDE44 meLanbide44 = new MELANBIDE44();
                    return meLanbide44.cargarPantallaJustificacion(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
                } else {
                    //Subpestana PREPARADORES
                    try {
                        EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                        if (sol == null) {
                            sol = new EcaSolicitudVO();
                        }
                        url = cargarSubpestanaPreparadores_Justificacion(sol, adapt, request);
                        if (url != null) {
                            request.setAttribute("urlPestanaPreparadores_justificacion", url);
                        }
                    } catch (Exception ex) {
                    }

                    //Subpestana PROSPECTORES
                    try {
                        EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                        if (sol == null) {
                            sol = new EcaSolicitudVO();
                        }
                        url = cargarSubpestanaProspectores_Justificacion(sol, adapt, request);
                        if (url != null) {
                            request.setAttribute("urlPestanaProspectores_justificacion", url);
                        }
                    } catch (Exception ex) {
                    }
                }
            } catch (Exception ex) {
            }
        }

        return "/jsp/extension/melanbide35/justificacion/justificacion.jsp";
    }

    private String cargarSubpestanaPreparadores_Justificacion(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request) {
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        try {

            List<FilaPreparadorJustificacionVO> listaPreparadoresJustificacion = MeLanbide35Manager.getInstance().getListaPreparadoresJustificacion(sol, adapt, codIdioma);
            if (listaPreparadoresJustificacion != null) {
                request.setAttribute("listaPreparadoresJustificacion", listaPreparadoresJustificacion);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return "/jsp/extension/melanbide35/justificacion/preparadores.jsp";
    }

    private String cargarSubpestanaProspectores_Justificacion(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (sol != null) {
            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }
            try {
                List<FilaProspectorJustificacionVO> listaProspectoresJus = MeLanbide35Manager.getInstance().getListaProspectoresJustificacion(sol, adapt, codIdioma);
                if (listaProspectoresJus != null) {
                    request.setAttribute("listaProspectoresJus", listaProspectoresJus);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        return "/jsp/extension/melanbide35/justificacion/prospectores.jsp";
    }
    public String cargarPantallaResumenCampos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        double importeEca = 0;
        double totalInserciones = 0;
        double totalSeguimientos = 0;
        double subvConc = 0;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {}

        if (adapt != null) {
            try {
                int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
                EcaSolicitud23VO sol = MeLanbide35Manager.getInstance().getDatosSolicitudECA23(numExpediente, adapt);
                if (anio > 2023) {
                    totalInserciones = MeLanbide35Manager.getInstance().getTotalValInsercionesECA23(numExpediente, adapt);
                    totalSeguimientos = MeLanbide35Manager.getInstance().getTotalValSegumientosECA23(numExpediente, adapt);
                    importeEca = totalInserciones + totalSeguimientos;
                }
                subvConc = MeLanbide35Manager.getInstance().getSuplementarioImporte(numExpediente,
                        ConfigurationParameter.getParameter(ConstantesMeLanbide35.CAMPO_IMPORTE_SUBV_CONC, ConstantesMeLanbide35.FICHERO_PROPIEDADES), adapt);
                request.setAttribute("subvMod", String.valueOf(importeEca));
                request.setAttribute("subvConc", String.valueOf(subvConc)); // De momento está pendiente de establecer.
                request.setAttribute("cuantiaSolicitada", sol != null ? String.valueOf(sol.getCuantiaSolicitada()) : String.valueOf(0));
            } catch (Exception ex) {
                log.error("cargarPantallaResumenCampos: Excepcion general: ", ex);
            }

        }
        return "/jsp/extension/melanbide35/pestanaresumen/resumen.jsp";
    }

    public String cargarPantallaValidacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
        }
        String url = null;
        //Subpestana VALIDACION
        if (adapt != null) {
            try {
                int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
                if (anio > 2023) {
                    Eca23ConfiguracionVO configuracion = obtenerImportePorDiscapacidadECA23(String.valueOf(anio), adapt);
                    request.setAttribute("configuracion", configuracion);
                    // pestana JUSTIFICACION 23
                    JustificacionECA23VO validacion = new JustificacionECA23VO();
                    double importeEca = 0;
                    double importeValidado = 0;
                    double totalInserciones = 0;
                    double totalSeguimientos = 0;
                    importeValidado = MeLanbide35Manager.getInstance().getSuplementarioImporte(numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide35.CAMPO_IMPORTE_VALIDADO_23, ConstantesMeLanbide35.FICHERO_PROPIEDADES), adapt);
                    totalInserciones = MeLanbide35Manager.getInstance().getTotalValInsercionesECA23(numExpediente, adapt);
                    totalSeguimientos = MeLanbide35Manager.getInstance().getTotalValSegumientosECA23(numExpediente, adapt);
                    importeEca = totalInserciones + totalSeguimientos;
                    validacion.setNumExpediente(numExpediente);
                    validacion.setImporteEca(importeEca);
                    validacion.setImporteValidado(importeValidado);
                    validacion.setTotalInserciones(totalInserciones);
                    validacion.setTotalSeguimientos(totalSeguimientos);
                    log.info("cargarPantallaValidacion: importeValidado = " + importeValidado + ", totalInserciones = " + totalInserciones + ", totalSeguimientos = " + totalSeguimientos);
                    request.setAttribute("validacion", validacion);
                    try {
                        // JUSTIFICACION INSERCIONES
                        url = cargarSubPestanaInserciones_ValidacionECA23(numExpediente, request, adapt);
                        if (url != null) {
                            request.setAttribute("urlPestanaInserciones_ValidacionECA23", url);
                        }
                        log.info("cargarPantallaValidacion: url inserciones = " + url);
                    } catch (Exception e) {
                        log.error("cargarPantallaValidacion: validacion inserciones: ", e);
                    }
                    try {
                        // JUSTIFICACION PREPARADORES
                        url = cargarSubPestanaPreparadores_ValidacionECA23(numExpediente, request, adapt);
                        if (url != null) {
                            request.setAttribute("urlPestanaPreparadores_ValidacionECA23", url);
                        }
                        log.info("cargarPantallaValidacion: url preparadores = " + url);
                    } catch (Exception e) {
                        log.error("cargarPantallaValidacion: validacion preparadores: ", e);
                    }
                    return "/jsp/extension/melanbide35/validacion23/validacion23.jsp";

                }
                else {
                    return "/jsp/extension/melanbide35/validacion23/invalid_exp.jsp";
                }
            } catch (Exception ex) {
                log.error("cargarPantallaValidacion: Excepcion general: ", ex);                       
            }

        }

        return "/jsp/extension/melanbide35/validacion23/validacion23.jsp";
    }

    private String cargarSubPestanaInserciones_ValidacionECA23(String numExpediente, HttpServletRequest request, AdaptadorSQLBD adapt) {
        log.debug("cargarSubPestanaInserciones_ValidacionECA23");

        try {
            double total = 0.0;
            List<JusInsercionesECA23VO> listaInsercionesValidacion = MeLanbide35Manager.getInstance().getValInsercionesECA23(numExpediente, adapt);
            if (listaInsercionesValidacion != null) {
                for (JusInsercionesECA23VO jusInsercion : listaInsercionesValidacion) {
                    jusInsercion.setDescSexo(getDescripcionDesplegable(request, jusInsercion.getDescSexo()));
                    jusInsercion.setDescTipoDisc(getDescripcionDesplegable(request, jusInsercion.getDescTipoDisc()));
                    jusInsercion.setDescColectivo(getDescripcionDesplegable(request, jusInsercion.getDescColectivo()));
                    jusInsercion.setDescTipoContrato(getDescripcionDesplegable(request, jusInsercion.getDescTipoContrato()));
                    jusInsercion.setDescTipoEdadSexo(getDescripcionDesplegable(request, jusInsercion.getDescTipoEdadSexo()));
                    jusInsercion.setDescCnaeE(jusInsercion.getDescCnaeE());
                    jusInsercion.setDescCnaeC(jusInsercion.getDescCnaeC());
                    total += jusInsercion.getImporteInser();
                }
                request.setAttribute("listaInsercionesValidacion", listaInsercionesValidacion);
                request.setAttribute("totalInsercionesVal", String.valueOf(total));
                request.setAttribute("numInsercionesVal", listaInsercionesValidacion.size());
            }
        } catch (Exception ex) {
            log.error("cargarSubPestanaInserciones_ValidacionECA23 error: ", ex);
        }
        return "/jsp/extension/melanbide35/validacion23/validacionInsercion23.jsp";
    }    

    private String cargarSubPestanaPreparadores_ValidacionECA23(String numExpediente, HttpServletRequest request, AdaptadorSQLBD adapt) {
        log.debug("cargarSubPestanaPreparadores_ValidacionECA23");
        try {
            double total = 0.0;
            int numSeguimientosVal = 0;
            List<JusPreparadoresECA23VO> listaPreparadoresValidacion = MeLanbide35Manager.getInstance().getValPreparadoresECA23(numExpediente, adapt);
            if (listaPreparadoresValidacion != null) {
                for (JusPreparadoresECA23VO jusPreparador : listaPreparadoresValidacion) {
                    double impoPrep = MeLanbide35Manager.getInstance().getTotalValSeguimientosPreparadorECA23(numExpediente, jusPreparador.getNifPreparador(), adapt);
                    if (jusPreparador.getImportePrep() == null || jusPreparador.getImportePrep().compareTo(0.0) == 0) {
                        jusPreparador.setImportePrep(impoPrep);
                    }
                    total += jusPreparador.getImportePrep();
                    int seguimientosPrep = MeLanbide35Manager.getInstance().getNumValSeguimientosPreparadorECA23(numExpediente, jusPreparador.getNifPreparador(), adapt);
                    jusPreparador.setSegumientos(seguimientosPrep);
                    numSeguimientosVal += seguimientosPrep;
                }
                log.debug("imp - " + total);
                log.debug("seg - " + numSeguimientosVal);
                log.debug("prep - " + listaPreparadoresValidacion.size());

                request.setAttribute("listaPreparadoresValidacion", listaPreparadoresValidacion);
                request.setAttribute("totalPreparadoresVal", String.valueOf(total));
                request.setAttribute("numPreparadoresVal", listaPreparadoresValidacion.size());
                request.setAttribute("numSeguimientosVal", numSeguimientosVal);

            }
        } catch (Exception ex) {
            log.error("cargarSubPestanaPreparadores_ValidacionECA23 error: ", ex);
        }
        return "/jsp/extension/melanbide35/validacion23/validacionPreparadores23.jsp";
    }

    public void copiarDesdeJustificacionAValidacion (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = null;

        try {
            final String numExp = request.getParameter("numero");
            MeLanbide35Manager manager = MeLanbide35Manager.getInstance();
            int result = manager.copiarDesdeJustificiacionAValidacion(numExp, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (result == -1) {
                codigoOperacion = "1";
            } else {
                codigoOperacion = "0";
            }


        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(codigoOperacion), response);
    }

    public String cargarSeguimientosPreparador_ValidacionECA23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("cargarSeguimientosPreparador_ValidacionECA23");
        AdaptadorSQLBD adapt = null;
        String numExp = null;
        String dniPreparador = null;
        double total = 0.0;
        if (request.getParameter("numExp") != null) {
            numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
        }

        try {
            dniPreparador = request.getParameter("dniPreparador");
            int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExp);
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            Eca23ConfiguracionVO configuracion = obtenerImportePorDiscapacidadECA23(String.valueOf(anio), adapt);
            request.setAttribute("configuracion", configuracion);
            request.setAttribute("dniPreparador", dniPreparador);
            log.info("cargarSeguimientosPreparador_ValidacionECA23: numExp=" + numExp + ", dniPreparador=" + dniPreparador);
            List<JusSeguimientosECA23VO> listaSegPreparador = MeLanbide35Manager.getInstance().getValSeguimientosPreparadorECA23(numExp, dniPreparador, adapt);
            if (listaSegPreparador != null) {
                for (JusSeguimientosECA23VO valSegPrepECA23VO : listaSegPreparador) {
                    valSegPrepECA23VO.setDescSexo(getDescripcionDesplegable(request, valSegPrepECA23VO.getDescSexo()));
                    valSegPrepECA23VO.setDescTipoDisc(getDescripcionDesplegable(request, valSegPrepECA23VO.getDescTipoDisc()));
                    valSegPrepECA23VO.setDescColectivo(getDescripcionDesplegable(request, valSegPrepECA23VO.getDescColectivo()));
                    valSegPrepECA23VO.setDescTipoContrato(getDescripcionDesplegable(request, valSegPrepECA23VO.getDescTipoContrato()));
                    valSegPrepECA23VO.setDescTipoEdadSexo(getDescripcionDesplegable(request, valSegPrepECA23VO.getDescTipoEdadSexo()));
                    total += valSegPrepECA23VO.getImporteSegui();
                }
                request.setAttribute("listaSegPreparador", listaSegPreparador);
                request.setAttribute("numSeguimientosPreparador", listaSegPreparador.size());
                request.setAttribute("totalSegPreparadorVal", String.valueOf(total));

            }

        } catch (Exception ex) {
            log.error("Ha ocurrido un error en cargarSeguimientosPreparador_ValidacionECA23 " + ex.getMessage());
        }
        return "/jsp/extension/melanbide35/validacion23/validaSeguimientosPreparador23.jsp";
    }
    public String cargarMantenimientoInsertValInsercion23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarMantenimientoInsertValInsercion23 - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide35/validacion23/mantenimientoValInsercion23.jsp?codOrganizacion=" + codOrganizacion;

        AdaptadorSQLBD adapt = null;
        AdaptadorSQLBD adaptGen = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            adaptGen = this.getAdaptSQLBDflbgen();
            final List<GeneralComboVO> listSexo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_SEXO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaSexo", listSexo);
            final List<GeneralComboVO> listTipoDisc = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_TIPO_DIS, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoDisc", listTipoDisc);
            final List<GeneralComboVO> listColectivo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_COLECTIVO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaColectivo", listColectivo);
            final List<GeneralComboVO> listTipoContrato = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_CONTRATO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoContrato", listTipoContrato);
            final List<GeneralComboVO> listTipoEdadSexo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_EDAD_SEXO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoEdadSexo", listTipoEdadSexo);
            final List<GeneralComboVO> listCnae = MeLanbide35Manager.getInstance().getComboExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide35.VISTA_DES_CNAE, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adaptGen);
            request.setAttribute("listaCnae", listCnae);
        } catch (Exception ex) {}


        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }
            //Cargamos los valores  de los desplegables
            //cargarDesplegablesIKER(codOrganizacion, request);

        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva Contratacion : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public String cargarMantenimientoUpdateValInsercion23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarMantenimientoUpdateValInsercion23 - " + numExpediente);
        String nuevo = "0";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide35/validacion23/mantenimientoValInsercion23.jsp?codOrganizacion=" + codOrganizacion;

        AdaptadorSQLBD adapt = null;
        AdaptadorSQLBD adaptGen = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            adaptGen = this.getAdaptSQLBDflbgen();
            final List<GeneralComboVO> listSexo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_SEXO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaSexo", listSexo);
            final List<GeneralComboVO> listTipoDisc = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_TIPO_DIS, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoDisc", listTipoDisc);
            final List<GeneralComboVO> listColectivo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_COLECTIVO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaColectivo", listColectivo);
            final List<GeneralComboVO> listTipoContrato = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_CONTRATO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoContrato", listTipoContrato);
            final List<GeneralComboVO> listTipoEdadSexo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_EDAD_SEXO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoEdadSexo", listTipoEdadSexo);
            final List<GeneralComboVO> listCnae = MeLanbide35Manager.getInstance().getComboExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide35.VISTA_DES_CNAE, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adaptGen);
            request.setAttribute("listaCnae", listCnae);
        } catch (Exception ex) {}


        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }
            final String id =  request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                final List<JusInsercionesECA23VO> jusInsercionesECA23VOList = MeLanbide35Manager.getInstance().getValInsercionesECA23(numExp, adapt);
                for (final JusInsercionesECA23VO ins : jusInsercionesECA23VOList) {
                    if (Objects.equals(ins.getId(), Integer.valueOf(id))) {
                        request.setAttribute("datModif", ins);
                        break;
                    }
                }
            }

        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva Contratacion : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }
    public void guardarValInsercion23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                      HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        String codigoOperacion = "1";
        JusInsercionesECA23VO jusInsercionesECA23VO = new JusInsercionesECA23VO();
        log.info("Entramos en guardarValInsercion23 - " + numExpediente);
        int idiomaUsuario = 1;
        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        try {
            if (usuario != null) {
                idiomaUsuario = usuario.getIdioma();
            }
        } catch (Exception ex) {}
        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
            jusInsercionesECA23VO.setNumExpediente(request.getParameter("numExpediente"));
            jusInsercionesECA23VO.setDni(request.getParameter("dni"));
            jusInsercionesECA23VO.setNombre(request.getParameter("nombre"));
            jusInsercionesECA23VO.setApellido1(request.getParameter("apellido1"));
            jusInsercionesECA23VO.setApellido2(request.getParameter("apellido2"));
            jusInsercionesECA23VO.setSexo(request.getParameter("sexo"));
            final String fecNac = request.getParameter("fecNac");
            if (fecNac != null && !fecNac.isEmpty()) {
                jusInsercionesECA23VO.setfNacimiento(new java.sql.Date(format.parse(fecNac).getTime()));
            } else {
                jusInsercionesECA23VO.setfNacimiento(null);
            }
            jusInsercionesECA23VO.setfNacimientoStr(fecNac);
            jusInsercionesECA23VO.setTipoDisc(request.getParameter("tipoDisc"));
            jusInsercionesECA23VO.setGrado(request.getParameter("grado") != null && !request.getParameter("grado").isEmpty() ? Double.valueOf(request.getParameter("grado")) : null);
            jusInsercionesECA23VO.setColectivo(request.getParameter("colectivo"));
            jusInsercionesECA23VO.setTipoContrato(request.getParameter("tipoContrato"));
            jusInsercionesECA23VO.setJornada(request.getParameter("jornada") != null && !request.getParameter("jornada").isEmpty() ? Double.valueOf(request.getParameter("jornada")) : null);
            final String fecInicio = request.getParameter("fecInicio");
            if (fecInicio != null && !fecInicio.isEmpty()) {
                jusInsercionesECA23VO.setfInicio(new java.sql.Date(format.parse(fecInicio).getTime()));
            } else {
                jusInsercionesECA23VO.setfInicio(null);
            }
            jusInsercionesECA23VO.setfIniciostr(fecInicio);
            jusInsercionesECA23VO.setTipoEdadSexo(request.getParameter("tipoEdadSexo"));
            String fecFin = request.getParameter("fecFin");
            if (fecFin != null && !fecFin.isEmpty()) {
                jusInsercionesECA23VO.setfFin(new java.sql.Date(format.parse(fecFin).getTime()));
            } else {
                jusInsercionesECA23VO.setfFin(null);
            }
            jusInsercionesECA23VO.setfFinStr(fecFin);
            jusInsercionesECA23VO.setDias(request.getParameter("dias") != null && !request.getParameter("dias").isEmpty() ? Integer.valueOf(request.getParameter("dias")) : null);
            jusInsercionesECA23VO.setEdad(request.getParameter("edad") != null && !request.getParameter("edad").isEmpty() ? Integer.valueOf(request.getParameter("edad")) : null);
            jusInsercionesECA23VO.setEmpresa(request.getParameter("empresa"));
            jusInsercionesECA23VO.setNifEmpresa(request.getParameter("nifEmpresa"));
            jusInsercionesECA23VO.setCnae(request.getParameter("cnae"));
            jusInsercionesECA23VO.setNifPreparador(request.getParameter("nifPreparador"));
            jusInsercionesECA23VO.setImporteInser(request.getParameter("importeInser") != null && !request.getParameter("importeInser").isEmpty() ? Double.valueOf(request.getParameter("importeInser")) : null);
        } catch (final Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }
        try {
            MeLanbide35Manager.getInstance().insertValInsercionesECA23(jusInsercionesECA23VO, adapt);
            codigoOperacion = "0";
        } catch (final Exception ex) {
            log.error("NO se ha insertado correctamente la nueva Insercion", ex.getMessage());
            codigoOperacion = "7";
        }
        List<JusInsercionesECA23VO> insercionesActualizadas = new ArrayList<JusInsercionesECA23VO>();
        try {
            insercionesActualizadas = MeLanbide35Manager.getInstance().getValInsercionesECA23(request.getParameter("numExpediente"),
                    adapt);
            actualizarCombosInserciones(insercionesActualizadas ,idiomaUsuario);
        } catch (final Exception ex) {
            log.error("No se han recuperado las inserciones", ex.getMessage());
            codigoOperacion = "5";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", insercionesActualizadas);
        }

        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultado), response);
    }

    public void modificarValInsercion23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                      HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        String codigoOperacion = "1";
        log.info("Entramos en modificarValInsercion23 - " + numExpediente);
        int idiomaUsuario = 1;
        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        try {
            if (usuario != null) {
                idiomaUsuario = usuario.getIdioma();
            }
        } catch (Exception ex) {}
        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
            JusInsercionesECA23VO jusInsercionesECA23VO = new JusInsercionesECA23VO();
            final String idStr = request.getParameter("id");
            jusInsercionesECA23VO.setId(Integer.valueOf(idStr));
            jusInsercionesECA23VO.setNumExpediente(request.getParameter("numExpediente"));
            jusInsercionesECA23VO.setDni(request.getParameter("dni"));
            jusInsercionesECA23VO.setNombre(request.getParameter("nombre"));
            jusInsercionesECA23VO.setApellido1(request.getParameter("apellido1"));
            jusInsercionesECA23VO.setApellido2(request.getParameter("apellido2"));
            jusInsercionesECA23VO.setSexo(request.getParameter("sexo"));
            final String fecNac = request.getParameter("fecNac");
            if (fecNac != null && !fecNac.isEmpty()) {
                jusInsercionesECA23VO.setfNacimiento(new java.sql.Date(format.parse(fecNac).getTime()));
            }
            else {
                jusInsercionesECA23VO.setfNacimiento(null);
            }
            jusInsercionesECA23VO.setfNacimientoStr(fecNac);
            jusInsercionesECA23VO.setTipoDisc(request.getParameter("tipoDisc"));
            jusInsercionesECA23VO.setGrado(request.getParameter("grado") != null && !request.getParameter("grado").isEmpty() ? Double.valueOf(request.getParameter("grado")) : null );
            jusInsercionesECA23VO.setColectivo(request.getParameter("colectivo"));
            jusInsercionesECA23VO.setTipoContrato(request.getParameter("tipoContrato"));
            jusInsercionesECA23VO.setJornada(request.getParameter("jornada") != null && !request.getParameter("jornada").isEmpty() ? Double.valueOf(request.getParameter("jornada")) : null );
            final String fecInicio = request.getParameter("fecInicio");
            if (fecInicio != null && !fecInicio.isEmpty()) {
                jusInsercionesECA23VO.setfInicio(new java.sql.Date(format.parse(fecInicio).getTime()));
            }
            else {
                jusInsercionesECA23VO.setfInicio(null);
            }
            jusInsercionesECA23VO.setfIniciostr(fecInicio);
            jusInsercionesECA23VO.setTipoEdadSexo(request.getParameter("tipoEdadSexo"));
            String fecFin = request.getParameter("fecFin");
            if (fecFin != null && !fecFin.isEmpty()) {
                jusInsercionesECA23VO.setfFin(new java.sql.Date(format.parse(fecFin).getTime()));
            }
            else {
                jusInsercionesECA23VO.setfFin(null);
            }
            jusInsercionesECA23VO.setfFinStr(fecFin);
            jusInsercionesECA23VO.setDias(request.getParameter("dias") != null && !request.getParameter("dias").isEmpty() ? Integer.valueOf(request.getParameter("dias")) : null);
            jusInsercionesECA23VO.setEdad(request.getParameter("edad") != null && !request.getParameter("edad").isEmpty() ? Integer.valueOf(request.getParameter("edad")) : null);
            jusInsercionesECA23VO.setEmpresa(request.getParameter("empresa"));
            jusInsercionesECA23VO.setNifEmpresa(request.getParameter("nifEmpresa"));
            jusInsercionesECA23VO.setCnae(request.getParameter("cnae"));
            jusInsercionesECA23VO.setNifPreparador(request.getParameter("nifPreparador"));
            jusInsercionesECA23VO.setImporteInser(request.getParameter("importeInser") != null && !request.getParameter("importeInser").isEmpty() ? Double.valueOf(request.getParameter("importeInser")) : null);
            MeLanbide35Manager.getInstance().updateInsercionValECA23(jusInsercionesECA23VO, adapt);
            codigoOperacion = "0";
        } catch (final Exception ex) {
            codigoOperacion = "8";
            log.error("Error en modificarValInsercion23 ", ex.getMessage() );
        }

        List<JusInsercionesECA23VO> insercionesActualizadas = new ArrayList<JusInsercionesECA23VO>();
        try {
            insercionesActualizadas = MeLanbide35Manager.getInstance().getValInsercionesECA23(request.getParameter("numExpediente"),
                    adapt);
            actualizarCombosInserciones(insercionesActualizadas, idiomaUsuario);
        } catch (final Exception ex) {
            log.error("No se han recuperado las inserciones", ex.getMessage());
            codigoOperacion = "5";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", insercionesActualizadas);
        }
        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultado), response);
    }
    public void eliminarValInsercion23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                        HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        String codigoOperacion = "1";
        log.info("Entramos en eliminarValInsercion23 - " + numExpediente);
        int idiomaUsuario = 1;
        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        try {
            if (usuario != null) {
                idiomaUsuario = usuario.getIdioma();
            }
        } catch (Exception ex) {}
        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));

            final String idStr = request.getParameter("id");
            MeLanbide35Manager.getInstance().deleteValInsercionECA23(Integer.valueOf(idStr), adapt);
            codigoOperacion = "0";
        } catch (final Exception ex) {
            codigoOperacion = "6";
            log.error("Error en eliminarValInsercion23 ", ex.getMessage() );
        }
        List<JusInsercionesECA23VO> insercionesActualizadas = new ArrayList<JusInsercionesECA23VO>();
        try {
            insercionesActualizadas = MeLanbide35Manager.getInstance().getValInsercionesECA23(request.getParameter("numExp"),
                    adapt);
            actualizarCombosInserciones(insercionesActualizadas, idiomaUsuario);
        } catch (final Exception ex) {
            log.error("No se han recuperado las inserciones", ex.getMessage());
            codigoOperacion = "5";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", insercionesActualizadas);
        }

        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultado), response);
    }
    private void actualizarCombosInserciones(final List<JusInsercionesECA23VO> insercionesActualizadas, int idiomaUsuario) {
        for (final JusInsercionesECA23VO ins : insercionesActualizadas) {
            String descSexo, descTipoDisc, descColectivo, descTipoContrato, descTipoEdadSexo;
            if (idiomaUsuario == 4) {
                descSexo = ins.getDescSexo().split("\\|")[1];
                descTipoDisc = ins.getDescTipoDisc().split("\\|")[1];
                descColectivo = ins.getDescColectivo().split("\\|")[1];
                descTipoContrato = ins.getDescTipoContrato().split("\\|")[1];
                descTipoEdadSexo = ins.getDescTipoEdadSexo().split("\\|")[1];
            } else {
                descSexo = ins.getDescSexo().split("\\|")[0];
                descTipoDisc = ins.getDescTipoDisc().split("\\|")[0];
                descColectivo = ins.getDescColectivo().split("\\|")[0];
                descTipoContrato = ins.getDescTipoContrato().split("\\|")[0];
                descTipoEdadSexo = ins.getDescTipoEdadSexo().split("\\|")[0];
            }
            ins.setDescSexo(descSexo);
            ins.setDescTipoDisc(descTipoDisc);
            ins.setDescColectivo(descColectivo);
            ins.setDescTipoContrato(descTipoContrato);
            ins.setTipoEdadSexo(descTipoEdadSexo);
        }
    }
    public String cargarMantenimientoInsertValPreparador23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarMantenimientoInsertValPreparador23 - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide35/validacion23/mantenimientoValPreparador23.jsp?codOrganizacion=" + codOrganizacion;

        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {}
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }


        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de un nuevo preparador : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public String cargarMantenimientoUpdateValPreparador23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarMantenimientoUpdateValPreparador23 - " + numExpediente);
        String nuevo = "0";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide35/validacion23/mantenimientoValPreparador23.jsp?codOrganizacion=" + codOrganizacion;

        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {}


        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }
            final String id =  request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                final List<JusPreparadoresECA23VO> valPreparadoresECA23 = MeLanbide35Manager.getInstance(). getValPreparadoresECA23(numExp, adapt);
                for (final JusPreparadoresECA23VO ins : valPreparadoresECA23) {
                    if (Objects.equals(ins.getId(), Integer.valueOf(id))) {
                        request.setAttribute("datModif", ins);
                        break;
                    }
                }
            }

        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de un nuevo preparador : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }
    public void guardarValPreparador23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                      HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        String codigoOperacion = "1";
        log.info("Entramos en guardarValPreparador23 - " + numExpediente);
        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));

            JusPreparadoresECA23VO preparador = new JusPreparadoresECA23VO();

            preparador.setNumExpediente(request.getParameter("numExp"));
            preparador.setNifPreparador(request.getParameter("nifPreparador"));
            preparador.setJornada(request.getParameter("jornada") != null && !request.getParameter("jornada").isEmpty() ? Double.valueOf(request.getParameter("jornada")) : null );
            preparador.setPermitidos(request.getParameter("permitidos") != null && !request.getParameter("permitidos").isEmpty() ? Integer.valueOf(request.getParameter("permitidos")) : null);
            preparador.setImportePrep(request.getParameter("importePrep") != null && !request.getParameter("importePrep").isEmpty() ? Double.valueOf(request.getParameter("importePrep")) : null);

            MeLanbide35Manager.getInstance().insertValPreparadoresECA23(preparador, adapt);
            codigoOperacion = "0";
        } catch (final Exception ex) {
            codigoOperacion = "1";
            log.error("Error en guardarValPreparador23 ", ex.getMessage() );
        }
        List<JusPreparadoresECA23VO> preparadoresActualizados = new ArrayList<JusPreparadoresECA23VO>();
        try {
            preparadoresActualizados = MeLanbide35Manager.getInstance().getValPreparadoresECA23(request.getParameter("numExp"),
                    adapt);
        } catch (final Exception ex) {
            log.error("No se han recuperado los preparadores", ex.getMessage());
            codigoOperacion = "5";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", preparadoresActualizados);
        }

        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultado), response);
    }

    public void modificarValPreparador23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                        HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        String codigoOperacion = "1";
        log.info("Entramos en modificarValPreparador23 - " + numExpediente);
        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            JusPreparadoresECA23VO preparador = new JusPreparadoresECA23VO();

            final String idStr = request.getParameter("id");
            preparador.setId(Integer.valueOf(idStr));
            preparador.setNumExpediente(request.getParameter("numExp"));
            preparador.setNifPreparador(request.getParameter("nifPreparador"));
            preparador.setJornada(request.getParameter("jornada") != null && !request.getParameter("jornada").isEmpty() ? Double.valueOf(request.getParameter("jornada")) : null );
            preparador.setPermitidos(request.getParameter("permitidos") != null && !request.getParameter("permitidos").isEmpty() ? Integer.valueOf(request.getParameter("permitidos")) : null);
            preparador.setImportePrep(request.getParameter("importePrep") != null && !request.getParameter("importePrep").isEmpty() ? Double.valueOf(request.getParameter("importePrep")) : null);

            MeLanbide35Manager.getInstance().updatePreparadorValECA23(preparador, adapt);
            codigoOperacion = "0";
        } catch (final Exception ex) {
            codigoOperacion = "1";
            log.error("Error en modificarValPreparador23 ", ex.getMessage() );
        }

        List<JusPreparadoresECA23VO> preparadoresActualizados = new ArrayList<JusPreparadoresECA23VO>();
        try {
            preparadoresActualizados = MeLanbide35Manager.getInstance().getValPreparadoresECA23(request.getParameter("numExp"),
                    adapt);
        } catch (final Exception ex) {
            log.error("No se han recuperado los preparadores", ex.getMessage());
            codigoOperacion = "5";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", preparadoresActualizados);
        }

        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultado), response);
    }

    public void eliminarValPreparador23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                       HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        String codigoOperacion = "1";
        log.info("Entramos en eliminarValPreparador23 - " + numExpediente);

        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));

            final String idStr = request.getParameter("id");
            MeLanbide35Manager.getInstance().deleteValPreparadorECA23(Integer.valueOf(idStr), adapt);
            codigoOperacion = "0";
        } catch (final Exception ex) {
            codigoOperacion = "6";
            log.error("Error en eliminarValPreparador23 ", ex.getMessage() );
        }
        List<JusPreparadoresECA23VO> preparadoresActualizados = new ArrayList<JusPreparadoresECA23VO>();
        try {
            preparadoresActualizados = MeLanbide35Manager.getInstance().getValPreparadoresECA23(request.getParameter("numExp"),
                    adapt);
        } catch (final Exception ex) {
            log.error("No se han recuperado los preparadores", ex.getMessage());
            codigoOperacion = "5";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", preparadoresActualizados);
        }

        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultado), response);
    }

    public String cargarMantenimientoInsertValSeguimiento23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarMantenimientoInsertValSeguimiento23 - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide35/validacion23/mantenimientoValSeguimientos23.jsp?codOrganizacion=" + codOrganizacion;

        AdaptadorSQLBD adapt = null;
        AdaptadorSQLBD adaptGen = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            adaptGen = this.getAdaptSQLBDflbgen();
            final List<GeneralComboVO> listSexo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_SEXO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaSexo", listSexo);
            final List<GeneralComboVO> listTipoDisc = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_TIPO_DIS, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoDisc", listTipoDisc);
            final List<GeneralComboVO> listColectivo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_COLECTIVO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaColectivo", listColectivo);
            final List<GeneralComboVO> listTipoContrato = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_CONTRATO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoContrato", listTipoContrato);
            final List<GeneralComboVO> listTipoEdadSexo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_EDAD_SEXO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoEdadSexo", listTipoEdadSexo);
            final List<GeneralComboVO> listCnae = MeLanbide35Manager.getInstance().getComboExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide35.VISTA_DES_CNAE, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adaptGen);
            request.setAttribute("listaCnae", listCnae);
        } catch (Exception ex) {}


        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }
            //Cargamos los valores  de los desplegables
            //cargarDesplegablesIKER(codOrganizacion, request);

        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva Contratacion : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public String cargarMantenimientoUpdateValSeguimiento23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarMantenimientoUpdateValSeguimiento23 - " + numExpediente);
        String nuevo = "0";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide35/validacion23/mantenimientoValSeguimientos23.jsp?codOrganizacion=" + codOrganizacion;

        AdaptadorSQLBD adapt = null;
        AdaptadorSQLBD adaptGen = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            adaptGen = this.getAdaptSQLBDflbgen();
            final List<GeneralComboVO> listSexo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_SEXO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaSexo", listSexo);
            final List<GeneralComboVO> listTipoDisc = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_TIPO_DIS, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoDisc", listTipoDisc);
            final List<GeneralComboVO> listColectivo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_COLECTIVO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaColectivo", listColectivo);
            final List<GeneralComboVO> listTipoContrato = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_CONTRATO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoContrato", listTipoContrato);
            final List<GeneralComboVO> listTipoEdadSexo = MeLanbide35Manager.getInstance().getCombo(ConfigurationParameter.getParameter(ConstantesMeLanbide35.COD_DES_EDAD_SEXO, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adapt);
            request.setAttribute("listaTipoEdadSexo", listTipoEdadSexo);
            final List<GeneralComboVO> listCnae = MeLanbide35Manager.getInstance().getComboExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide35.VISTA_DES_CNAE, ConstantesMeLanbide35.FICHERO_PROPIEDADES),adaptGen);
            request.setAttribute("listaCnae", listCnae);
        } catch (Exception ex) {}


        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }
            final String id =  request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                final List<JusSeguimientosECA23VO> jusSeguimientosECA23VOList = MeLanbide35Manager.getInstance().getValSeguimientosPreparadorECA23(request.getParameter("numExp"),
                        request.getParameter("nifPreparador"), adapt);
                for (final JusSeguimientosECA23VO ins : jusSeguimientosECA23VOList) {
                    if (Objects.equals(ins.getId(), Integer.valueOf(id))) {
                        request.setAttribute("datModif", ins);
                        break;
                    }
                }
            }

        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva Contratacion : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public void guardarValSeguimientos23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                      HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        String codigoOperacion = "1";
        int idiomaUsuario = 1;

        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        try {
            if (usuario != null) {
                idiomaUsuario = usuario.getIdioma();
            }
        } catch (Exception ex) {}
        log.info("Entramos en guardarValSeguimientos23 - " + numExpediente);
        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);

            JusSeguimientosECA23VO jusSeguimientosECA23VO = new JusSeguimientosECA23VO();

            jusSeguimientosECA23VO.setNumExpediente(request.getParameter("numExpediente"));
            jusSeguimientosECA23VO.setDni(request.getParameter("dni"));
            jusSeguimientosECA23VO.setNombre(request.getParameter("nombre"));
            jusSeguimientosECA23VO.setApellido1(request.getParameter("apellido1"));
            jusSeguimientosECA23VO.setApellido2(request.getParameter("apellido2"));
            jusSeguimientosECA23VO.setSexo(request.getParameter("sexo"));
            final String fecNac = request.getParameter("fecNac");
            if (fecNac != null && !fecNac.isEmpty()) {
                jusSeguimientosECA23VO.setfNacimiento(new java.sql.Date(format.parse(fecNac).getTime()));
            }
            else {
                jusSeguimientosECA23VO.setfNacimiento(null);
            }
            jusSeguimientosECA23VO.setfNacimientoStr(fecNac);
            jusSeguimientosECA23VO.setTipoDisc(request.getParameter("tipoDisc"));
            jusSeguimientosECA23VO.setGrado(request.getParameter("grado") != null && !request.getParameter("grado").isEmpty() ? Double.valueOf(request.getParameter("grado")) : null );
            jusSeguimientosECA23VO.setColectivo(request.getParameter("colectivo"));
            jusSeguimientosECA23VO.setTipoContrato(request.getParameter("tipoContrato"));
            jusSeguimientosECA23VO.setJornada(request.getParameter("jornada") != null && !request.getParameter("jornada").isEmpty() ? Double.valueOf(request.getParameter("jornada")) : null );
            final String fecInicio = request.getParameter("fecInicio");
            if (fecInicio != null && !fecInicio.isEmpty()) {
                jusSeguimientosECA23VO.setfInicio(new java.sql.Date(format.parse(fecInicio).getTime()));
            }
            else {
                jusSeguimientosECA23VO.setfInicio(null);
            }
            jusSeguimientosECA23VO.setfIniciostr(fecInicio);
            jusSeguimientosECA23VO.setTipoEdadSexo(request.getParameter("tipoEdadSexo"));
            String fecFin = request.getParameter("fecFin");
            if (fecFin != null && !fecFin.isEmpty()) {
                jusSeguimientosECA23VO.setfFin(new java.sql.Date(format.parse(fecFin).getTime()));
            }
            else {
                jusSeguimientosECA23VO.setfFin(null);
            }
            jusSeguimientosECA23VO.setfFinStr(fecFin);
            jusSeguimientosECA23VO.setEmpresa(request.getParameter("empresa"));
            jusSeguimientosECA23VO.setNifEmpresa(request.getParameter("nifEmpresa"));
            jusSeguimientosECA23VO.setCnae(request.getParameter("cnae"));
            jusSeguimientosECA23VO.setNifPreparador(request.getParameter("nifPreparador"));
            jusSeguimientosECA23VO.setImporteSegui(request.getParameter("importeSegui") != null && !request.getParameter("importeSegui").isEmpty() ? Double.valueOf(request.getParameter("importeSegui")) : null);
            MeLanbide35Manager.getInstance().insertValSeguimientosECA23(jusSeguimientosECA23VO, adapt);
            codigoOperacion = "0";
        } catch (final Exception ex) {
            codigoOperacion = "1";
            log.error("Error en guardarValSeguimientos23 ", ex.getMessage() );
        }
        List<JusSeguimientosECA23VO> seguimientosActualizados = new ArrayList<JusSeguimientosECA23VO>();
        try {
            seguimientosActualizados = MeLanbide35Manager.getInstance().getValSeguimientosPreparadorECA23(request.getParameter("numExpediente"),
                    request.getParameter("nifPreparador"), adapt);
            actualizarCombosSeguimientos(seguimientosActualizados, idiomaUsuario);
        } catch (final Exception ex) {
            log.error("No se han recuperado los seguimientos", ex.getMessage());
            codigoOperacion = "5";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", seguimientosActualizados);
        }

        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultado), response);
    }

    public void modificarValSeguimiento23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                        HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        String codigoOperacion = "1";
        int idiomaUsuario = 1;

        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        try {
            if (usuario != null) {
                idiomaUsuario = usuario.getIdioma();
            }
        } catch (Exception ex) {}
        log.info("Entramos en modificarValSeguimiento23 - " + numExpediente);
        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);

            JusSeguimientosECA23VO jusSeguimientosECA23VO = new JusSeguimientosECA23VO();

            final String idStr = request.getParameter("id");
            jusSeguimientosECA23VO.setId(Integer.valueOf(idStr));
            jusSeguimientosECA23VO.setNumExpediente(request.getParameter("numExpediente"));
            jusSeguimientosECA23VO.setDni(request.getParameter("dni"));
            jusSeguimientosECA23VO.setNombre(request.getParameter("nombre"));
            jusSeguimientosECA23VO.setApellido1(request.getParameter("apellido1"));
            jusSeguimientosECA23VO.setApellido2(request.getParameter("apellido2"));
            jusSeguimientosECA23VO.setSexo(request.getParameter("sexo"));
            final String fecNac = request.getParameter("fecNac");
            if (fecNac != null && !fecNac.isEmpty()) {
                jusSeguimientosECA23VO.setfNacimiento(new java.sql.Date(format.parse(fecNac).getTime()));
            }
            else {
                jusSeguimientosECA23VO.setfNacimiento(null);
            }
            jusSeguimientosECA23VO.setfNacimientoStr(fecNac);
            jusSeguimientosECA23VO.setTipoDisc(request.getParameter("tipoDisc"));
            jusSeguimientosECA23VO.setGrado(request.getParameter("grado") != null && !request.getParameter("grado").isEmpty() ? Double.valueOf(request.getParameter("grado")) : null );
            jusSeguimientosECA23VO.setColectivo(request.getParameter("colectivo"));
            jusSeguimientosECA23VO.setTipoContrato(request.getParameter("tipoContrato"));
            jusSeguimientosECA23VO.setJornada(request.getParameter("jornada") != null && !request.getParameter("jornada").isEmpty() ? Double.valueOf(request.getParameter("jornada")) : null );
            final String fecInicio = request.getParameter("fecInicio");
            if (fecInicio != null && !fecInicio.isEmpty()) {
                jusSeguimientosECA23VO.setfInicio(new java.sql.Date(format.parse(fecInicio).getTime()));
            }
            else {
                jusSeguimientosECA23VO.setfInicio(null);
            }
            jusSeguimientosECA23VO.setfIniciostr(fecInicio);
            jusSeguimientosECA23VO.setTipoEdadSexo(request.getParameter("tipoEdadSexo"));
            String fecFin = request.getParameter("fecFin");
            if (fecFin != null && !fecFin.isEmpty()) {
                jusSeguimientosECA23VO.setfFin(new java.sql.Date(format.parse(fecFin).getTime()));
            }
            else {
                jusSeguimientosECA23VO.setfFin(null);
            }
            jusSeguimientosECA23VO.setfFinStr(fecFin);

            jusSeguimientosECA23VO.setEmpresa(request.getParameter("empresa"));
            jusSeguimientosECA23VO.setNifEmpresa(request.getParameter("nifEmpresa"));
            jusSeguimientosECA23VO.setCnae(request.getParameter("cnae"));
            jusSeguimientosECA23VO.setNifPreparador(request.getParameter("nifPreparador"));
            jusSeguimientosECA23VO.setImporteSegui(request.getParameter("importeSegui") != null && !request.getParameter("importeSegui").isEmpty() ? Double.valueOf(request.getParameter("importeSegui")) : null);
            MeLanbide35Manager.getInstance().updateSeguimientoValECA23(jusSeguimientosECA23VO, adapt);
            codigoOperacion = "0";
        } catch (final Exception ex) {
            codigoOperacion = "1";
            log.error("Error en modificarValSeguimiento23 ", ex.getMessage() );
        }
        List<JusSeguimientosECA23VO> seguimientosActualizados = new ArrayList<JusSeguimientosECA23VO>();
        try {
            seguimientosActualizados = MeLanbide35Manager.getInstance().getValSeguimientosPreparadorECA23(request.getParameter("numExpediente"),
                    request.getParameter("nifPreparador"), adapt);
            actualizarCombosSeguimientos(seguimientosActualizados, idiomaUsuario);
        } catch (final Exception ex) {
            log.error("No se han recuperado los seguimientos", ex.getMessage());
            codigoOperacion = "5";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", seguimientosActualizados);
        }

        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultado), response);
    }

    public void eliminarValSeguimiento23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                        HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        String resultadoOperacion = "1";
        int idiomaUsuario = 1;

        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        try {
            if (usuario != null) {
                idiomaUsuario = usuario.getIdioma();
            }
        } catch (Exception ex) {}
        log.info("Entramos en eliminarValSeguimiento23 - " + numExpediente);
        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));

            final String idStr = request.getParameter("id");
            MeLanbide35Manager.getInstance().deleteValSeguimientoECA23(Integer.valueOf(idStr), adapt);
            resultadoOperacion = "0";
        } catch (final Exception ex) {
            resultadoOperacion = "1";
            log.error("Error en eliminarValSeguimiento23 ", ex.getMessage() );
        }
        List<JusSeguimientosECA23VO> seguimientosActualizados = new ArrayList<JusSeguimientosECA23VO>();
        try {
            seguimientosActualizados = MeLanbide35Manager.getInstance().getValSeguimientosPreparadorECA23(request.getParameter("numExp"),
                    request.getParameter("nifPreparador"), adapt);
            actualizarCombosSeguimientos(seguimientosActualizados, idiomaUsuario);
        } catch (final Exception ex) {
            log.error("No se han recuperado los seguimientos", ex.getMessage());
            resultadoOperacion = "5";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", resultadoOperacion);
        if (resultadoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", seguimientosActualizados);
        }

        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultado), response);
    }

    private void actualizarCombosSeguimientos(final List<JusSeguimientosECA23VO> seguimientosActulizados, int idiomaUsuario) {
        for (final JusSeguimientosECA23VO segui : seguimientosActulizados) {
            String descSexo, descTipoDisc, descColectivo, descTipoContrato, descTipoEdadSexo;
            if (idiomaUsuario == 4) {
                descSexo = segui.getDescSexo().split("\\|")[1];
                descTipoDisc = segui.getDescTipoDisc().split("\\|")[1];
                descColectivo = segui.getDescColectivo().split("\\|")[1];
                descTipoContrato = segui.getDescTipoContrato().split("\\|")[1];
                descTipoEdadSexo = segui.getDescTipoEdadSexo().split("\\|")[1];
            } else {
                descSexo = segui.getDescSexo().split("\\|")[0];
                descTipoDisc = segui.getDescTipoDisc().split("\\|")[0];
                descColectivo = segui.getDescColectivo().split("\\|")[0];
                descTipoContrato = segui.getDescTipoContrato().split("\\|")[0];
                descTipoEdadSexo = segui.getDescTipoEdadSexo().split("\\|")[0];
            }
            segui.setDescSexo(descSexo);
            segui.setDescTipoDisc(descTipoDisc);
            segui.setDescColectivo(descColectivo);
            segui.setDescTipoContrato(descTipoContrato);
            segui.setDescTipoEdadSexo(descTipoEdadSexo);
        }
    }
    public String cargarPantallaResumen(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            MELANBIDE44 meLanbide44 = new MELANBIDE44();
            int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
            if (anio > 2023) {
                return null;
            } else if (anio > 2014) {
                return meLanbide44.cargarPantallaResumen(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
            } else {
                EcaResumenVO res = this.calcularDatosResumen(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
                if (res != null) {
                    request.setAttribute("datosResumen", res);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide35/resumen/resumen.jsp";
    }

    public void guardarDatosSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try {
                String c1H = request.getParameter("col1SolicH");
                String c1M = request.getParameter("col1SolicM");
                String c2H = request.getParameter("col2SolicH");
                String c2M = request.getParameter("col2SolicM");
                String c3H = request.getParameter("col3SolicH");
                String c3M = request.getParameter("col3SolicM");
                String c4H = request.getParameter("col4SolicH");
                String c4M = request.getParameter("col4SolicM");
//                String c5H = (String)request.getParameter("col5SolicH");
//                String c5M = (String)request.getParameter("col5SolicM");
//                 String c6H = (String)request.getParameter("col6SolicH");
//                String c6M = (String)request.getParameter("col6SolicM");
//                
                String segH = request.getParameter("seg1SolicH");
                String segM = request.getParameter("seg1SolicM");
                String segActuaciones = request.getParameter("nActuacionesSolic");
                String prospectoresNum = request.getParameter("prospectoresSolicNum");
                String prospectoresImp = request.getParameter("prospectoresSolicSol");
                String preparadoresNum = request.getParameter("preparadoresSolicNum");
                String preparadoresImp = request.getParameter("preparadoresSolicSol");
                String gastos = request.getParameter("gastosGeneralesSolic");
                String otrasSub = request.getParameter("otrasAyudas");
                String subPub = request.getParameter("impOrgPublicos");
                String subPriv = request.getParameter("impOrgPrivados");
                String totalSub = request.getParameter("totSubSolicitada");
                String totalAprobado = request.getParameter("totSubAprobada");

                //CONCEDIDO
                String c1HConc = request.getParameter("col1HConc");
                String c1MConc = request.getParameter("col1MConc");
                String c2HConc = request.getParameter("col2HConc");
                String c2MConc = request.getParameter("col2MConc");
                String c3HConc = request.getParameter("col3HConc");
                String c3MConc = request.getParameter("col3MConc");
                String c4HConc = request.getParameter("col4HConc");
                String c4MConc = request.getParameter("col4MConc");
                String c5HConc = request.getParameter("col5HConc");
                String c5MConc = request.getParameter("col5MConc");
                String c6HConc = request.getParameter("col6HConc");
                String c6MConc = request.getParameter("col6MConc");
                String segHConc = request.getParameter("segHConc");
                String segMConc = request.getParameter("segMConc");
                String segActuacionesConc = request.getParameter("nActuacionesConc");
                String prospectoresNumConc = request.getParameter("prospectoresNumConc");
                String prospectoresImpConc = request.getParameter("prospectoresSolConc");
                String preparadoresNumConc = request.getParameter("preparadoresNumConc");
                String preparadoresImpConc = request.getParameter("preparadoresSolConc");
                String gastosConc = request.getParameter("gastosGeneralesConc");

                EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                if (sol == null) {
                    sol = new EcaSolicitudVO();
                }

                sol.setGastos(gastos != null && !gastos.isEmpty() ? new BigDecimal(gastos.replaceAll(",", "\\.")) : null);
                sol.setInserC1H(c1H != null && !c1H.isEmpty() ? Float.valueOf(c1H) : null);
                sol.setInserC1M(c1M != null && !c1M.isEmpty() ? Float.valueOf(c1M) : null);
                sol.setInserC2H(c2H != null && !c2H.isEmpty() ? Float.valueOf(c2H) : null);
                sol.setInserC2M(c2M != null && !c2M.isEmpty() ? Float.valueOf(c2M) : null);
                sol.setInserC3H(c3H != null && !c3H.isEmpty() ? Float.valueOf(c3H) : null);
                sol.setInserC3M(c3M != null && !c3M.isEmpty() ? Float.valueOf(c3M) : null);
                sol.setInserC4H(c4H != null && !c4H.isEmpty() ? Float.valueOf(c4H) : null);
                sol.setInserC4M(c4M != null && !c4M.isEmpty() ? Float.valueOf(c4M) : null);
//                sol.setInserC5H(c5H != null && !c5H.equals("") ? Float.parseFloat(c5H) : null);
//                sol.setInserC5M(c5M != null && !c5M.equals("") ? Float.parseFloat(c5M) : null);
//                sol.setInserC6H(c6H != null && !c6H.equals("") ? Float.parseFloat(c6H) : null);
//                sol.setInserC6M(c6M != null && !c6M.equals("") ? Float.parseFloat(c6M) : null);
                sol.setNumExp(numExpediente);
                sol.setExpEje(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente));

                if (otrasSub != null) {
                    if (otrasSub.equalsIgnoreCase(ConstantesMeLanbide35.CIERTO)) {
                        sol.setOtrasSub(true);
                        sol.setSubPriv(subPriv != null && !subPriv.isEmpty() ? new BigDecimal(subPriv.replaceAll(",", "\\.")) : null);
                        sol.setSubPub(subPub != null && !subPub.isEmpty() ? new BigDecimal(subPub.replaceAll(",", "\\.")) : null);
                    } else if (otrasSub.equalsIgnoreCase(ConstantesMeLanbide35.FALSO)) {
                        sol.setOtrasSub(false);
                        sol.setSubPriv(null);
                        sol.setSubPub(null);
                    } else {
                        sol.setOtrasSub(null);
                        sol.setSubPriv(null);
                        sol.setSubPub(null);
                    }
                }

                sol.setPreparadoresImp(preparadoresImp != null && !preparadoresImp.isEmpty() ? new BigDecimal(preparadoresImp.replaceAll(",", "\\.")) : null);
                sol.setPreparadoresNum(preparadoresNum != null && !preparadoresNum.isEmpty() ? Integer.valueOf(preparadoresNum) : null);
                sol.setProspectoresImp(prospectoresImp != null && !prospectoresImp.isEmpty() ? new BigDecimal(prospectoresImp.replaceAll(",", "\\.")) : null);
                sol.setProspectoresNum(prospectoresNum != null && !prospectoresNum.isEmpty() ? Integer.valueOf(prospectoresNum) : null);
                sol.setSegActuaciones(segActuaciones != null && !segActuaciones.isEmpty() ? new BigDecimal(segActuaciones.replaceAll(",", "\\.")) : null);
                sol.setSegH(segH != null && !segH.isEmpty() ? Integer.valueOf(segH) : null);
                sol.setSegM(segM != null && !segM.isEmpty() ? Integer.valueOf(segM) : null);
                sol.setTotalAprobado(totalAprobado != null && !totalAprobado.isEmpty() ? new BigDecimal(totalAprobado.replaceAll(",", "\\.")) : null);
                sol.setTotalSubvencion(totalSub != null && !totalSub.isEmpty() ? new BigDecimal(totalSub.replaceAll(",", "\\.")) : null);

                //CONCEDIDO
                sol.setInserC1HConc(c1HConc != null && !c1HConc.isEmpty() ? Float.valueOf(c1HConc) : null);
                sol.setInserC1MConc(c1MConc != null && !c1MConc.isEmpty() ? Float.valueOf(c1MConc) : null);
                sol.setInserC2HConc(c2HConc != null && !c2HConc.isEmpty() ? Float.valueOf(c2HConc) : null);
                sol.setInserC2MConc(c2MConc != null && !c2MConc.isEmpty() ? Float.valueOf(c2MConc) : null);
                sol.setInserC3HConc(c3HConc != null && !c3HConc.isEmpty() ? Float.valueOf(c3HConc) : null);
                sol.setInserC3MConc(c3MConc != null && !c3MConc.isEmpty() ? Float.valueOf(c3MConc) : null);
                sol.setInserC4HConc(c4HConc != null && !c4HConc.isEmpty() ? Float.valueOf(c4HConc) : null);
                sol.setInserC4MConc(c4MConc != null && !c4MConc.isEmpty() ? Float.valueOf(c4MConc) : null);
                sol.setInserC5HConc(c5HConc != null && !c5HConc.isEmpty() ? Float.valueOf(c5HConc) : null);
                sol.setInserC5MConc(c5MConc != null && !c5MConc.isEmpty() ? Float.valueOf(c5MConc) : null);
                sol.setInserC6HConc(c6HConc != null && !c6HConc.isEmpty() ? Float.valueOf(c6HConc) : null);
                sol.setInserC6MConc(c6MConc != null && !c6MConc.isEmpty() ? Float.valueOf(c6MConc) : null);

                sol.setSegHConc(segHConc != null && !segHConc.isEmpty() ? Integer.valueOf(segHConc) : null);
                sol.setSegMConc(segMConc != null && !segMConc.isEmpty() ? Integer.valueOf(segMConc) : null);
                sol.setPreparadoresImpConc(preparadoresImpConc != null && !preparadoresImpConc.isEmpty() ? new BigDecimal(preparadoresImpConc.replaceAll(",", "\\.")) : null);
                sol.setPreparadoresNumConc(preparadoresNumConc != null && !preparadoresNumConc.isEmpty() ? Integer.valueOf(preparadoresNumConc) : null);
                sol.setProspectoresImpConc(prospectoresImpConc != null && !prospectoresImpConc.isEmpty() ? new BigDecimal(prospectoresImpConc.replaceAll(",", "\\.")) : null);
                sol.setProspectoresNumConc(prospectoresNumConc != null && !prospectoresNumConc.isEmpty() ? Integer.valueOf(prospectoresNumConc) : null);
                sol.setSegActuacionesConc(segActuacionesConc != null && !segActuacionesConc.isEmpty() ? new BigDecimal(segActuacionesConc.replaceAll(",", "\\.")) : null);
                sol.setGastosConc(gastosConc != null && !gastosConc.isEmpty() ? new BigDecimal(gastosConc.replaceAll(",", "\\.")) : null);

                sol = MeLanbide35Manager.getInstance().guardarDatosSolicitud(sol, adapt);
                if (sol != null) {
                    codigoOperacion = "0";
                } else {
                    codigoOperacion = "1";
                }
            } catch (Exception ex) {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE35.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuilder xmlSalida = new StringBuilder();
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
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    public String procesarExcelPreparadores(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codIdioma = 1;
        int i = 0;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        try {

            MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

            wrapper.getParameterMap();
            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile file = table.get("fichero_preparadores_sol");

            byte[] data = file.getFileData();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            HSSFWorkbook workbook = new HSSFWorkbook(bais);

            HSSFSheet sheet = workbook.getSheetAt(0);

            List<String> nifsRepetidos = this.hayNifsRepetidos(sheet, 0);

            if (nifsRepetidos == null || nifsRepetidos.isEmpty()) {
                List<String> sustitutosDeSustitutos = this.haySustitutosDeSustitutos(sheet, 0, 24);
                if (sustitutosDeSustitutos == null || sustitutosDeSustitutos.isEmpty()) {
                    int filaIni = sheet.getFirstRowNum();
                    int filaFin = sheet.getLastRowNum();

                    HSSFRow row = null;
                    List<EcaSolPreparadoresVO> filasImportar = new ArrayList<EcaSolPreparadoresVO>();
                    EcaSolPreparadoresVO prep = null;
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    try {
                        for (i = filaIni + 1; i <= filaFin; i++) {
                            row = sheet.getRow(i);
                            prep = (EcaSolPreparadoresVO) MeLanbide35MappingUtils.getInstance().map(row, EcaSolPreparadoresVO.class);
                            if (prep != null) {
                                //los datos se guardan en campos carga y modificables
                                prep.setNif_Carga(prep.getNif());
                                prep.setNombre_Carga(prep.getNombre());
                                prep.setFecIni_Carga(prep.getFecIni());
                                prep.setFecFin_Carga(prep.getFecFin());
                                prep.setHorasCont_Carga(prep.getHorasCont());
                                prep.setHorasJC_Carga(prep.getHorasJC());
                                prep.setHorasEca_Carga(prep.getHorasEca());
                                prep.setImpSSJC_Carga(prep.getImpSSJC());
                                prep.setImpSSJR_Carga(prep.getImpSSJR());
                                prep.setImpSSECA_Carga(prep.getImpSSECA());
                                prep.setSegAnt_Carga(prep.getSegAnt());
                                prep.setImpSegAnt_Carga(prep.getImpSegAnt());
                                prep.setInsC1H_Carga(prep.getInsC1H());
                                prep.setInsC1M_Carga(prep.getInsC1M());
                                prep.setInsC1_Carga(prep.getInsC1());
                                prep.setInsC2H_Carga(prep.getInsC2H());
                                prep.setInsC2M_Carga(prep.getInsC2M());
                                prep.setInsC2_Carga(prep.getInsC2());
                                prep.setInsC3H_Carga(prep.getInsC3H());
                                prep.setInsC3M_Carga(prep.getInsC3M());
                                prep.setInsC3_Carga(prep.getInsC3());
                                prep.setInsC4H_Carga(prep.getInsC4H());
                                prep.setInsC4M_Carga(prep.getInsC4M());
                                prep.setInsC4_Carga(prep.getInsC4());

                                //prep.setInsImporte_Carga(prep.getInsImporte());
                                //prep.setInsSegImporte_Carga(prep.getInsSegImporte());
                                //prep.setCoste_Carga(prep.getCoste());
                                //datos calculados
                                if (prep.getInsC1H() != null || prep.getInsC1M() != null) {
                                    prep.setInsC1((prep.getInsC1H() != null ? prep.getInsC1H() : BigDecimal.ZERO).add(prep.getInsC1M() != null ? prep.getInsC1M() : BigDecimal.ZERO));
                                    if (!MeLanbide35Utils.validarDatoExcel(prep.getInsC1().toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.totalColectivoDemasiadoGrande"), 1, i + 1));
                                        throw erme;
                                    }
                                } else {
                                    prep.setInsC1(null);
                                }
                                if (prep.getInsC2H() != null || prep.getInsC2M() != null) {
                                    prep.setInsC2((prep.getInsC2H() != null ? prep.getInsC2H() : BigDecimal.ZERO).add(prep.getInsC2M() != null ? prep.getInsC2M() : BigDecimal.ZERO));
                                    if (!MeLanbide35Utils.validarDatoExcel(prep.getInsC2().toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.totalColectivoDemasiadoGrande"), 2, i + 1));
                                        throw erme;
                                    }
                                } else {
                                    prep.setInsC2(null);
                                }
                                if (prep.getInsC3H() != null || prep.getInsC3M() != null) {
                                    prep.setInsC3((prep.getInsC3H() != null ? prep.getInsC3H() : BigDecimal.ZERO).add(prep.getInsC3M() != null ? prep.getInsC3M() : BigDecimal.ZERO));
                                    if (!MeLanbide35Utils.validarDatoExcel(prep.getInsC3().toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.totalColectivoDemasiadoGrande"), 3, i + 1));
                                        throw erme;
                                    }
                                } else {
                                    prep.setInsC3(null);
                                }
                                if (prep.getInsC4H() != null || prep.getInsC4M() != null) {
                                    prep.setInsC4((prep.getInsC4H() != null ? prep.getInsC4H() : BigDecimal.ZERO).add(prep.getInsC4M() != null ? prep.getInsC4M() : BigDecimal.ZERO));
                                    if (!MeLanbide35Utils.validarDatoExcel(prep.getInsC4().toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.totalColectivoDemasiadoGrande"), 4, i + 1));
                                        throw erme;
                                    }
                                } else {
                                    prep.setInsC4(null);
                                }

                                int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
                                EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(anio, adapt);
                                BigDecimal importeCalculado = null;
                                if (prep.getInsC1H() != null) {
                                    if (importeCalculado == null) {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC1H().multiply((config != null && config.getImC1h() != null ? config.getImC1h() : new BigDecimal("1"))));
                                }
                                if (prep.getInsC1M() != null) {
                                    if (importeCalculado == null) {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC1M().multiply((config != null && config.getImC1m() != null ? config.getImC1m() : new BigDecimal("1"))));
                                }
                                if (prep.getInsC2H() != null) {
                                    if (importeCalculado == null) {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC2H().multiply((config != null && config.getImC2h() != null ? config.getImC2h() : new BigDecimal("1"))));
                                }
                                if (prep.getInsC2M() != null) {
                                    if (importeCalculado == null) {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC2M().multiply((config != null && config.getImC2m() != null ? config.getImC2m() : new BigDecimal("1"))));
                                }
                                if (prep.getInsC3H() != null) {
                                    if (importeCalculado == null) {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC3H().multiply((config != null && config.getImC3h() != null ? config.getImC3h() : new BigDecimal("1"))));
                                }
                                if (prep.getInsC3M() != null) {
                                    if (importeCalculado == null) {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC3M().multiply((config != null && config.getImC3m() != null ? config.getImC3m() : new BigDecimal("1"))));
                                }
                                if (prep.getInsC4H() != null) {
                                    if (importeCalculado == null) {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC4H().multiply((config != null && config.getImC4h() != null ? config.getImC4h() : new BigDecimal("1"))));
                                }
                                if (prep.getInsC4M() != null) {
                                    if (importeCalculado == null) {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC4M().multiply((config != null && config.getImC4m() != null ? config.getImC4m() : new BigDecimal("1"))));
                                }
                                if (importeCalculado != null && !MeLanbide35Utils.validarDatoExcel(importeCalculado.toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.impInsercionesDemasiadoGrande"), i + 1));
                                    throw erme;
                                } else {
                                    prep.setInsImporte(importeCalculado);
                                }

                                BigDecimal costesSalSSEca = null;
                                if (prep.getImpSSJC() != null && prep.getHorasEca() != null && prep.getHorasJC() != null) {
                                    costesSalSSEca = new BigDecimal("0.00");
                                    costesSalSSEca = prep.getImpSSJC().multiply(prep.getHorasEca());
                                    costesSalSSEca = !prep.getHorasJC().equals(BigDecimal.ZERO) ? costesSalSSEca.divide(prep.getHorasJC(), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                                }
                                prep.setImpSSECA(costesSalSSEca);

                                if (prep.getSegAnt() != null) {
                                    BigDecimal impSeg = config != null && config.getImSeguimiento() != null ? config.getImSeguimiento() : BigDecimal.ZERO;
                                    BigDecimal importeCalcSeg = new BigDecimal(prep.getSegAnt().toString()).multiply(impSeg);

                                    if (costesSalSSEca != null) {
                                        BigDecimal poMax = config != null && config.getPoMaxSeguimientos() != null ? config.getPoMaxSeguimientos() : new BigDecimal("1");
                                        BigDecimal por = costesSalSSEca.multiply(poMax);
                                        if (importeCalcSeg != null && importeCalcSeg.compareTo(por) > 0)//mayor
                                        {
                                            importeCalcSeg = new BigDecimal(por.toString());
                                        }
                                    }

                                    importeCalcSeg = importeCalcSeg.setScale(2, RoundingMode.HALF_UP);

                                    if (importeCalcSeg != null && !MeLanbide35Utils.validarDatoExcel(importeCalcSeg.toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.importeSegAntDemasiadoGrande"), String.valueOf(anio), i + 1));
                                        throw erme;
                                    } else {
                                        prep.setImpSegAnt(importeCalcSeg);
                                    }
                                }

                                BigDecimal ImpSSJR = null;
                                if (prep.getImpSSJC() != null && prep.getHorasCont() != null && prep.getHorasJC() != null) {
                                    ImpSSJR = prep.getImpSSJC().multiply(prep.getHorasCont());
                                    ImpSSJR = !prep.getHorasJC().equals(BigDecimal.ZERO) ? ImpSSJR.divide(prep.getHorasJC(), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                                }
                                if (ImpSSJR != null && !MeLanbide35Utils.validarDatoExcel(ImpSSJR.toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.impSSJRDemasiadoGrande"), i + 1));
                                    throw erme;
                                } else {
                                    prep.setImpSSJR(ImpSSJR);
                                }

                                if (prep.getImpSegAnt() != null || prep.getInsImporte() != null) {
                                    BigDecimal importeCalcInsSeg = BigDecimal.ZERO;
                                    if (prep.getImpSegAnt() != null) {
                                        importeCalcInsSeg = importeCalcInsSeg.add(prep.getImpSegAnt());
                                    }
                                    if (prep.getInsImporte() != null) {
                                        importeCalcInsSeg = importeCalcInsSeg.add(prep.getInsImporte());
                                    }

                                    if (importeCalcInsSeg != null && !MeLanbide35Utils.validarDatoExcel(importeCalcInsSeg.toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.impInsSegDemasiadoGrande"), i + 1));
                                        throw erme;
                                    } else {
                                        prep.setInsSegImporte(importeCalcInsSeg);
                                    }
                                }

                                if (prep.getInsSegImporte() != null && costesSalSSEca != null) {
                                    prep.setCoste(costesSalSSEca.compareTo(prep.getInsSegImporte()) > 0 ? prep.getInsSegImporte() : costesSalSSEca);
                                } else if (prep.getInsSegImporte() != null) {
                                    prep.setCoste(prep.getInsSegImporte());
                                } else if (costesSalSSEca != null) {
                                    prep.setCoste(costesSalSSEca);
                                } else {
                                    prep.setCoste(null);
                                }

                                if (prep.getCoste() != null && !MeLanbide35Utils.validarDatoExcel(prep.getCoste().toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.costeDemasiadoGrande"), i + 1));
                                    throw erme;
                                }

                                if (prep.getInsC1_Carga() == null) {
                                    prep.setInsC1_Carga(prep.getInsC1());
                                }
                                if (prep.getInsC2_Carga() == null) {
                                    prep.setInsC2_Carga(prep.getInsC2());
                                }
                                if (prep.getInsC3_Carga() == null) {
                                    prep.setInsC3_Carga(prep.getInsC3());
                                }
                                if (prep.getInsC4_Carga() == null) {
                                    prep.setInsC4_Carga(prep.getInsC4());
                                }

                                if (prep.getImpSegAnt_Carga() == null) {
                                    prep.setImpSegAnt_Carga(prep.getImpSegAnt());
                                }

                                if (prep.getImpSSECA_Carga() == null) {
                                    prep.setImpSSECA_Carga(prep.getImpSSECA());
                                }

                                if (prep.getImpSSJR_Carga() == null) {
                                    prep.setImpSSJR_Carga(prep.getImpSSJR());
                                }

                                //No vienen en excel
                                prep.setInsImporte_Carga(prep.getInsImporte());
                                prep.setInsSegImporte_Carga(prep.getInsSegImporte());
                                prep.setCoste_Carga(prep.getCoste());

                                filasImportar.add(prep);
                            }
                        }
                        if (filasImportar != null && !filasImportar.isEmpty()) {
                            Collections.sort(filasImportar);
                            int filasImportadas = MeLanbide35Manager.getInstance().importarPreparadores(numExpediente, filasImportar, adapt);

                            if (filasImportadas < filasImportar.size()) {
                                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                            } else {
                                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1") + " " + filasImportadas + " " + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                            }
                        } else {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.noHayRegistrosImportar"));
                        }
                    } catch (ExcelRowMappingException erme) {
                        String mensajeImportar = null;
                        if (erme.getMensaje() != null && !erme.getMensaje().isEmpty()) {
                            mensajeImportar = erme.getMensaje();
                        } else {
                            mensajeImportar = MeLanbide35Utils.crearMensajeDatoExcelNoValido(erme, i + 1, codIdioma);
                        }
                        request.getSession().setAttribute("mensajeImportar", mensajeImportar);
                    }
                } else {
                    String mensajeImportar = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.sustitutosDeSustitutos") + "<br/>";
                    for (String nif : sustitutosDeSustitutos) {
                        mensajeImportar += "<br/>" + nif.toUpperCase();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);
                }
            } else {
                String mensajeImportar = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.nifsRepetidosExcel_1") + "<br/>";
                for (String nif : nifsRepetidos) {
                    mensajeImportar += "<br/>" + nif.toUpperCase();
                }
                mensajeImportar += "<br/><br/>" + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.nifsRepetidosExcel_2");
                request.getSession().setAttribute("mensajeImportar", mensajeImportar);
            }
        } catch (FilaSeguimientoPrepNoValidaException ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
        } catch (IOException ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
        } catch (Exception ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        return "/jsp/extension/melanbide35/solicitud/recargarListaPreparadoresSolic.jsp";
    }

    public void getListaPreparadoresSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            try {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            } catch (Exception ex) {

            }

            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide35Manager meLanbide35Manager = MeLanbide35Manager.getInstance();
            EcaSolicitudVO sol = meLanbide35Manager.getDatosSolicitud(numExpediente, adapt);
            List<FilaPreparadorSolicitudVO> preparadores = null;

            if (sol != null) {
                preparadores = MeLanbide35Manager.getInstance().getListaPreparadoresSolicitud(sol, adapt, codIdioma);
            } else {
                preparadores = new ArrayList<FilaPreparadorSolicitudVO>();
            }

            String codigoOperacion = "0";

            this.escribirListaPreparadoresRequest(codigoOperacion, preparadores, null, codIdioma, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void eliminarPreparadorSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        try {
            String idPrep = request.getParameter("idPrep");
            Integer idP = null;
            if (idPrep == null || idPrep.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idP = Integer.valueOf(idPrep);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null) {
                    EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                    prep.setSolPreparadoresCod(idP);
                    int result = MeLanbide35Manager.getInstance().eliminarPreparadorSolicitud(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        if (codigoOperacion.equalsIgnoreCase("0")) {
            this.getListaPreparadoresSolicitud(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
        } else {
            StringBuilder xmlSalida = new StringBuilder();
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
            } catch (IOException e) {
                e.printStackTrace();
            }//try-catch
        }
    }

    public String cargarNuevoPreparadorSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String opcion = request.getParameter("opcion");
            if (opcion != null) {
                if (opcion.equalsIgnoreCase("nuevo")) {
                    //Cargar nuevo preparador
                } else if (opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar")) {
                    //Cargar modificar preparador
                    String idPrep = request.getParameter("idPrepModif");
                    if (idPrep != null) {
                        EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                        prep.setSolPreparadoresCod(Integer.valueOf(idPrep));
                        prep = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if (prep != null) {
                            request.setAttribute("preparadorModif", prep);
                            EcaSolPreparadoresVO sustituto = MeLanbide35Manager.getInstance().getPreparadorSolicitudSustituto(prep.getSolPreparadoresCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if (sustituto != null) {
                                request.setAttribute("sustituto", sustituto);
                            }

                            if (prep.getSolPreparadorOrigen() != null) {
                                EcaSolPreparadoresVO origen = new EcaSolPreparadoresVO();
                                origen.setSolPreparadoresCod(prep.getSolPreparadorOrigen());
                                origen = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorId(origen, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if (origen != null) {
                                    request.setAttribute("preparadorOrigen", origen);
                                }
                            }
                        }
                    }
                    if (opcion.equalsIgnoreCase("consultar")) {
                        request.setAttribute("consulta", true);
                    }
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide35/solicitud/nuevoPreparadorSolicitud.jsp";
    }

    public void guardarPreparadorSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<String> validaciones = new ArrayList<String>();
        List<FilaPreparadorSolicitudVO> preparadores = new ArrayList<FilaPreparadorSolicitudVO>();

        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if (sol != null && sol.getSolicitudCod() != null) {

                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                //Recojo los parámetros
                String idPrep = request.getParameter("idPrep");
                String nif = request.getParameter("nif");
                String nomApel = request.getParameter("nomApel");
                String feIni = request.getParameter("feIni");
                String feFin = request.getParameter("feFin");
                String horasAnualesJC = request.getParameter("horasAnualesJC");
                String horasContrato = request.getParameter("horasContrato");
                String horasECA = request.getParameter("horasECA");
                String costesSSJor = request.getParameter("costesSSJor");
                String costesSSPorJor = request.getParameter("costesSSPorJor");
                String costesSSECA = request.getParameter("costesSSECA");
                String segAnt = request.getParameter("segAnt");
                String importe = request.getParameter("importe");
                String c1h = request.getParameter("c1h");
                String c1m = request.getParameter("c1m");
                String c1total = request.getParameter("c1total");
                String c2h = request.getParameter("c2h");
                String c2m = request.getParameter("c2m");
                String c2total = request.getParameter("c2total");
                String c3h = request.getParameter("c3h");
                String c3m = request.getParameter("c3m");
                String c3total = request.getParameter("c3total");
                String c4h = request.getParameter("c4h");
                String c4m = request.getParameter("c4m");
                String c4total = request.getParameter("c4total");
                String inserciones = request.getParameter("inserciones");
                String segIns = request.getParameter("segIns");
                String costesSS = request.getParameter("costesSS");
                String idPrepOrigen = request.getParameter("idPrepOrigen");
                String impteConcedido = request.getParameter("impteConcedido");

                EcaSolPreparadoresVO prepValida = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorNIF(sol, nif, adapt);
                boolean prepRepetido = false;

                if (prepValida != null && idPrep != null) {
                    if (!idPrep.equalsIgnoreCase(prepValida.getSolPreparadoresCod().toString())) {
                        prepRepetido = true;
                    }
                }
                if (!prepRepetido) {
                    EcaSolPreparadoresVO prep = null;
                    if (idPrep != null && !idPrep.isEmpty()) {
                        prep = new EcaSolPreparadoresVO();
                        prep.setSolPreparadoresCod(Integer.valueOf(idPrep));
                        prep = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorId(prep, adapt);
                    } else {
                        prep = new EcaSolPreparadoresVO();
                    }
                    if (prep == null) {
                        codigoOperacion = "3";
                    } else {
                        prep.setCoste(costesSS != null && !costesSS.isEmpty() ? new BigDecimal(costesSS) : null);
                        prep.setImpteConcedido(impteConcedido != null && !impteConcedido.isEmpty() ? new BigDecimal(impteConcedido) : null);
                        prep.setFecFin(feFin != null && !feFin.isEmpty() ? format.parse(feFin) : null);
                        prep.setFecIni(feIni != null && !feIni.isEmpty() ? format.parse(feIni) : null);
                        prep.setHorasCont(horasContrato != null && !horasContrato.isEmpty() ? new BigDecimal(horasContrato) : null);
                        prep.setHorasEca(horasECA != null && !horasECA.isEmpty() ? new BigDecimal(horasECA) : null);
                        prep.setHorasJC(horasAnualesJC != null && !horasAnualesJC.isEmpty() ? new BigDecimal(horasAnualesJC) : null);
                        prep.setImpSSECA(costesSSECA != null && !costesSSECA.isEmpty() ? new BigDecimal(costesSSECA) : null);
                        prep.setImpSSJC(costesSSJor != null && !costesSSJor.isEmpty() ? new BigDecimal(costesSSJor) : null);
                        prep.setImpSSJR(costesSSPorJor != null && !costesSSPorJor.isEmpty() ? new BigDecimal(costesSSPorJor) : null);
                        prep.setImpSegAnt(importe != null && !importe.isEmpty() ? new BigDecimal(importe) : null);
                        prep.setInsC1(c1total != null && !c1total.isEmpty() ? new BigDecimal(c1total) : null);
                        prep.setInsC1H(c1h != null && !c1h.isEmpty() ? new BigDecimal(c1h) : null);
                        prep.setInsC1M(c1m != null && !c1m.isEmpty() ? new BigDecimal(c1m) : null);
                        prep.setInsC2(c2total != null && !c2total.isEmpty() ? new BigDecimal(c2total) : null);
                        prep.setInsC2H(c2h != null && !c2h.isEmpty() ? new BigDecimal(c2h) : null);
                        prep.setInsC2M(c2m != null && !c2m.isEmpty() ? new BigDecimal(c2m) : null);
                        prep.setInsC3(c3total != null && !c3total.isEmpty() ? new BigDecimal(c3total) : null);
                        prep.setInsC3H(c3h != null && !c3h.isEmpty() ? new BigDecimal(c3h) : null);
                        prep.setInsC3M(c3m != null && !c3m.isEmpty() ? new BigDecimal(c3m) : null);
                        prep.setInsC4(c4total != null && !c4total.isEmpty() ? new BigDecimal(c4total) : null);
                        prep.setInsC4H(c4h != null && !c4h.isEmpty() ? new BigDecimal(c4h) : null);
                        prep.setInsC4M(c4m != null && !c4m.isEmpty() ? new BigDecimal(c4m) : null);
                        prep.setInsImporte(inserciones != null && !inserciones.isEmpty() ? new BigDecimal(inserciones) : null);
                        prep.setInsSegImporte(segIns != null && !segIns.isEmpty() ? new BigDecimal(segIns) : null);
                        prep.setNif(nif != null ? nif.toUpperCase() : null);
                        prep.setNombre(nomApel != null ? nomApel.toUpperCase() : null);
                        prep.setSegAnt(segAnt != null && !segAnt.isEmpty() ? Integer.valueOf(segAnt) : null);
                        prep.setSolicitud(sol.getSolicitudCod());
                        if (idPrepOrigen != null && !idPrepOrigen.isEmpty()) {
                            prep.setSolPreparadorOrigen(Integer.valueOf(idPrepOrigen));
                            if (prep.getTipoSust() == null) {
                                prep.setTipoSust(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD);
                            }
                        }

                        EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente), adapt);

                        boolean soloFormato = prep.getSolPreparadoresCod() != null;

                        EcaSolPreparadoresVO origen = null;

                        if (prep.getSolPreparadorOrigen() != null) {
                            origen = new EcaSolPreparadoresVO();
                            origen.setSolPreparadoresCod(prep.getSolPreparadorOrigen());
                            origen = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorId(origen, adapt);
                        }

                        List<EcaSolPreparadoresVO> sustitutos = MeLanbide35Manager.getInstance().getSustitutosPreparadorSolicitud(prep.getSolPreparadoresCod(), adapt);

                        validaciones = MeLanbide35Utils.validarEcaSolPreparadoresVO(prep, origen, sustitutos, config, sol.getExpEje(), codIdioma, soloFormato, false);
                        if (validaciones == null || validaciones.isEmpty()) {
                            MeLanbide35Manager.getInstance().guardarEcaSolPreparadoresVO(prep, adapt);
                            preparadores = MeLanbide35Manager.getInstance().getListaPreparadoresSolicitud(sol, adapt, codIdioma);
                        } else {
                            codigoOperacion = "4";
                        }
                    }
                } else {
                    codigoOperacion = "5";
                }
            } else {
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }

        this.escribirListaPreparadoresRequest(codigoOperacion, preparadores, validaciones, codIdioma, response);
    }

    public String cargarNuevoPreparadorJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        try {
            String opcion = request.getParameter("opcion");
            if (opcion != null) {
                List<SelectItem> listaTipoContrato = MeLanbide35Manager.getInstance().getListaTipoContrato(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                request.setAttribute("lstTipoContrato", listaTipoContrato);
                if (opcion.equalsIgnoreCase("nuevo")) {
                    //Cargar nuevo preparador
                    String idPrepS = request.getParameter("idPrepSustituir");
                    if (idPrepS != null) {
                        EcaJusPreparadoresVO prepS = new EcaJusPreparadoresVO();
                        prepS.setJusPreparadoresCod(Integer.valueOf(idPrepS));
                        prepS = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prepS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if (prepS != null) {
                            request.setAttribute("preparadorSustituir", prepS);
                        }
                    }
                } else if (opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar")) {
                    //Cargar modificar preparador
                    String idPrep = request.getParameter("idPrepModificar");
                    if (idPrep != null) {
                        EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                        prep.setJusPreparadoresCod(Integer.valueOf(idPrep));
                        prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if (prep != null) {
                            log.debug(prep.getHorasCont() != null ? prep.getHorasCont().toString() : "");
                            request.setAttribute("preparadorModif", prep);
                            EcaJusPreparadoresVO sustituto = new EcaJusPreparadoresVO();
                            sustituto = MeLanbide35Manager.getInstance().getPreparadorJustificacionSustituto(prep.getJusPreparadoresCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if (sustituto != null) {
                                request.setAttribute("sustituto", sustituto);
                            }
                        }

                        if (!prep.getJusPreparadorOrigen().equals("")) {
                            EcaJusPreparadoresVO prepS = new EcaJusPreparadoresVO();
                            prepS.setJusPreparadoresCod(prep.getJusPreparadorOrigen());
                            prepS = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prepS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if (prepS != null) {
                                request.setAttribute("preparadorSustituir", prepS);
                            }
                        }

                    }
                    if (opcion.equalsIgnoreCase("consultar")) {
                        request.setAttribute("consulta", true);
                    }
                }

            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide35/justificacion/nuevoPreparadorJustificacion.jsp";
    }

    public String procesarExcelProspectores(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        try {

            MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

            wrapper.getParameterMap();
            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile file = table.get("fichero_prospectores_sol");

            byte[] data = file.getFileData();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            HSSFWorkbook workbook = new HSSFWorkbook(bais);

            HSSFSheet sheet = workbook.getSheetAt(0);

            List<String> nifsRepetidos = this.hayNifsRepetidos(sheet, 0);

            if (nifsRepetidos == null || nifsRepetidos.isEmpty()) {
                List<String> sustitutosDeSustitutos = this.haySustitutosDeSustitutos(sheet, 0, 13);
                if (sustitutosDeSustitutos == null || sustitutosDeSustitutos.isEmpty()) {
                    int filaIni = sheet.getFirstRowNum();
                    int filaFin = sheet.getLastRowNum();

                    int i = 0;

                    HSSFRow row = null;
                    List<EcaSolProspectoresVO> filasImportar = new ArrayList<EcaSolProspectoresVO>();
                    EcaSolProspectoresVO prep = null;
                    try {
                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente), adapt);
                        for (i = filaIni + 1; i <= filaFin; i++) {
                            row = sheet.getRow(i);
                            prep = (EcaSolProspectoresVO) MeLanbide35MappingUtils.getInstance().map(row, EcaSolProspectoresVO.class);
                            if (prep != null) {
                                //los datos se guardan en campos carga y modificables
                                prep.setNif_Carga(prep.getNif());
                                prep.setNombre_Carga(prep.getNombre());
                                prep.setFecIni_Carga(prep.getFecIni());
                                prep.setFecFin_Carga(prep.getFecFin());
                                prep.setHorasCont_Carga(prep.getHorasCont());
                                prep.setHorasJC_Carga(prep.getHorasJC());
                                prep.setHorasEca_Carga(prep.getHorasEca());
                                prep.setImpSSJC_Carga(prep.getImpSSJC());
                                prep.setImpSSJR_Carga(prep.getImpSSJR());
                                prep.setImpSSECA_Carga(prep.getImpSSECA());
                                prep.setVisitas_Carga(prep.getVisitas());
                                prep.setVisitasImp_Carga(prep.getVisitasImp());
                                prep.setCoste_Carga(prep.getCoste());

                                //datos calculados   
                                BigDecimal importeCalcVis = null;
                                if (prep.getVisitas() != null) {
                                    importeCalcVis = new BigDecimal("0.00");
                                    importeCalcVis = new BigDecimal(prep.getVisitas().toString()).multiply(
                                            config != null && config.getImpVisita() != null ? config.getImpVisita() : new BigDecimal("0"));

                                    if (importeCalcVis != null && !MeLanbide35Utils.validarDatoExcel(importeCalcVis.toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.prospectores.impVisitasDemasiadoGrande"), i + 1));
                                        throw erme;
                                    } else {
                                        prep.setVisitasImp(importeCalcVis);
                                    }
                                }

                                BigDecimal costesSalSSEca = null;
                                if (prep.getImpSSJC() != null && prep.getHorasEca() != null && prep.getHorasJC() != null) {
                                    costesSalSSEca = new BigDecimal("0.00");
                                    costesSalSSEca = prep.getImpSSJC().multiply(prep.getHorasEca());
                                    costesSalSSEca = !prep.getHorasJC().equals(BigDecimal.ZERO) ? costesSalSSEca.divide(prep.getHorasJC(), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                                }
                                if (costesSalSSEca != null && !MeLanbide35Utils.validarDatoExcel(costesSalSSEca.toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.prospectores.impSSEcaDemasiadoGrande"), i + 1));
                                    throw erme;
                                } else {
                                    prep.setImpSSECA(costesSalSSEca);
                                }

                                BigDecimal ImpSSJR = null;
                                if (prep.getImpSSJC() != null && prep.getHorasCont() != null && prep.getHorasJC() != null) {
                                    ImpSSJR = new BigDecimal("0.00");
                                    ImpSSJR = prep.getImpSSJC().multiply(prep.getHorasCont());
                                    ImpSSJR = !prep.getHorasJC().equals(BigDecimal.ZERO) ? ImpSSJR.divide(prep.getHorasJC(), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                                }
                                if (ImpSSJR != null && !MeLanbide35Utils.validarDatoExcel(ImpSSJR.toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.prospectores.impSSJRDemasiadoGrande"), i + 1));
                                    throw erme;
                                } else {
                                    prep.setImpSSJR(ImpSSJR);
                                }

                                if (importeCalcVis != null && costesSalSSEca != null) {
                                    prep.setCoste(costesSalSSEca.compareTo(importeCalcVis) < 0 ? costesSalSSEca : importeCalcVis);
                                } else if (importeCalcVis != null) {
                                    prep.setCoste(importeCalcVis);
                                } else if (costesSalSSEca != null) {
                                    prep.setCoste(costesSalSSEca);
                                } else {
                                    prep.setCoste(null);
                                }

                                if (prep.getCoste() != null && !MeLanbide35Utils.validarDatoExcel(prep.getCoste().toString(), ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true)) {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide35.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.prospectores.costeDemasiadoGrande"), i + 1));
                                    throw erme;
                                }

                                if (prep.getVisitasImp_Carga() == null) {
                                    prep.setVisitasImp_Carga(prep.getVisitasImp());
                                }

                                if (prep.getImpSSECA_Carga() == null) {
                                    prep.setImpSSECA_Carga(prep.getImpSSECA());
                                }

                                if (prep.getImpSSJC_Carga() == null) {
                                    prep.setImpSSJC_Carga(prep.getImpSSJR());
                                }

                                if (prep.getCoste_Carga() == null) {
                                    prep.setCoste_Carga(prep.getCoste());
                                }

                                filasImportar.add(prep);
                            }
                        }
                        if (filasImportar != null && !filasImportar.isEmpty()) {
                            Collections.sort(filasImportar);
                            int filasImportadas = MeLanbide35Manager.getInstance().importarProspectores(numExpediente, filasImportar, adapt);

                            if (filasImportadas < filasImportar.size()) {
                                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                            } else {
                                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1") + " " + filasImportadas + " " + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                            }
                        } else {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.noHayRegistrosImportar"));
                        }
                    } catch (ExcelRowMappingException erme) {
                        String mensajeImportar = null;
                        if (erme.getMensaje() != null && !erme.getMensaje().isEmpty()) {
                            mensajeImportar = erme.getMensaje();
                        } else {
                            mensajeImportar = MeLanbide35Utils.crearMensajeDatoExcelNoValido(erme, i + 1, codIdioma);
                        }
                        request.getSession().setAttribute("mensajeImportar", mensajeImportar);
                    }
                } else {
                    String mensajeImportar = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.sustitutosDeSustitutos") + "<br/>";
                    for (String nif : sustitutosDeSustitutos) {
                        mensajeImportar += "<br/>" + nif.toUpperCase();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);
                }
            } else {
                String mensajeImportar = MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.nifsRepetidosExcel_1") + "<br/>";
                for (String nif : nifsRepetidos) {
                    mensajeImportar += "<br/>" + nif.toUpperCase();
                }
                mensajeImportar += "<br/><br/>" + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.nifsRepetidosExcel_2");
                request.getSession().setAttribute("mensajeImportar", mensajeImportar);
            }
        } catch (FilaProspectorSolicitudNoValidaException ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
        } catch (IOException ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
        } catch (Exception ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        return "/jsp/extension/melanbide35/solicitud/recargarListaProspectoresSolic.jsp";
    }

    public void getListaProspectoresSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            try {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            } catch (Exception ex) {

            }

            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide35Manager meLanbide35Manager = MeLanbide35Manager.getInstance();
            EcaSolicitudVO sol = meLanbide35Manager.getDatosSolicitud(numExpediente, adapt);
            List<FilaProspectorSolicitudVO> prospectores = null;

            if (sol != null) {
                prospectores = MeLanbide35Manager.getInstance().getListaProspectoresSolicitud(sol, adapt, codIdioma);
            } else {
                prospectores = new ArrayList<FilaProspectorSolicitudVO>();
            }

            String codigoOperacion = "0";

            this.escribirListaProspectoresRequest(codigoOperacion, prospectores, null, codIdioma, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String cargarNuevoProspectorSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String opcion = request.getParameter("opcion");
            if (opcion != null) {
                if (opcion.equalsIgnoreCase("nuevo")) {
                    //Cargar nuevo preparador
                } else if (opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar")) {
                    //Cargar modificar preparador
                    String idPros = request.getParameter("idProsModif");
                    if (idPros != null) {
                        EcaSolProspectoresVO pros = new EcaSolProspectoresVO();
                        pros.setSolProspectoresCod(Integer.valueOf(idPros));
                        pros = MeLanbide35Manager.getInstance().getProspectorSolicitudPorId(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if (pros != null) {
                            request.setAttribute("prospectorModif", pros);
                            EcaSolProspectoresVO sustituto = new EcaSolProspectoresVO();
                            sustituto = MeLanbide35Manager.getInstance().getProspectorSolicitudSustituto(pros.getSolProspectoresCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if (sustituto != null) {
                                request.setAttribute("sustitutopros", sustituto);
                            }

                            if (pros.getSolProspectorOrigen() != null && !pros.getSolProspectorOrigen().equals("")) {
                                EcaSolProspectoresVO prosS = new EcaSolProspectoresVO();
                                prosS.setSolProspectoresCod(pros.getSolProspectorOrigen());
                                prosS = MeLanbide35Manager.getInstance().getProspectorSolicitudPorId(prosS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if (prosS != null) {
                                    request.setAttribute("prospectorOrigen", prosS);
                                }
                            }

                        }
                    }
                    if (opcion.equalsIgnoreCase("consultar")) {
                        request.setAttribute("consulta", true);
                    }
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide35/solicitud/nuevoProspectorSolicitud.jsp";
    }

    public void guardarProspectorSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<String> validaciones = new ArrayList<String>();
        List<FilaProspectorSolicitudVO> prospectores = new ArrayList<FilaProspectorSolicitudVO>();

        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if (sol != null && sol.getSolicitudCod() != null) {

                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                //Recojo los parámetros
                String idPros = request.getParameter("idPros");
                String nif = request.getParameter("nif");
                String nomApel = request.getParameter("nomApel");
                String feIni = request.getParameter("feIni");
                String feFin = request.getParameter("feFin");
                String horasAnualesJC = request.getParameter("horasAnualesJC");
                String horasContrato = request.getParameter("horasContrato");
                String horasECA = request.getParameter("horasECA");
                String costesSSJor = request.getParameter("costesSSJor");
                String costesSSPorJor = request.getParameter("costesSSPorJor");
                String costesSSECA = request.getParameter("costesSSECA");
                String visitas = request.getParameter("visitas");
                String visitasImp = request.getParameter("visitasImp");
                String coste = request.getParameter("coste");
                String impteConcedido = request.getParameter("impteConcedido");
                String idProsOrigen = request.getParameter("idProsOrigen");

                EcaSolProspectoresVO prosValida = MeLanbide35Manager.getInstance().getProspectorSolicitudPorNIF(sol, nif, adapt);
                boolean prosRepetido = false;

                if (prosValida != null && idPros != null) {
                    if (!idPros.equalsIgnoreCase(prosValida.getSolProspectoresCod().toString())) {
                        prosRepetido = true;
                    }
                }
                if (!prosRepetido) {
                    EcaSolProspectoresVO pros = null;
                    if (idPros != null && !idPros.isEmpty()) {
                        pros = new EcaSolProspectoresVO();
                        pros.setSolProspectoresCod(Integer.valueOf(idPros));
                        pros = MeLanbide35Manager.getInstance().getProspectorSolicitudPorId(pros, adapt);
                    } else {
                        pros = new EcaSolProspectoresVO();
                    }
                    if (pros == null) {
                        codigoOperacion = "3";
                    } else {
                        pros.setCoste(coste != null && !coste.isEmpty() ? new BigDecimal(coste.replaceAll(",", "\\.")) : null);
                        pros.setImpteConcedido(impteConcedido != null && !impteConcedido.isEmpty() ? new BigDecimal(impteConcedido.replaceAll(",", "\\.")) : null);
                        pros.setFecFin(feFin != null && !feFin.isEmpty() ? format.parse(feFin) : null);
                        pros.setFecIni(feIni != null && !feIni.isEmpty() ? format.parse(feIni) : null);
                        pros.setHorasCont(horasContrato != null && !horasContrato.isEmpty() ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                        pros.setHorasEca(horasECA != null && !horasECA.isEmpty() ? new BigDecimal(horasECA.replaceAll(",", "\\.")) : null);
                        pros.setHorasJC(horasAnualesJC != null && !horasAnualesJC.isEmpty() ? new BigDecimal(horasAnualesJC.replaceAll(",", "\\.")) : null);
                        pros.setImpSSECA(costesSSECA != null && !costesSSECA.isEmpty() ? new BigDecimal(costesSSECA.replaceAll(",", "\\.")) : null);
                        pros.setImpSSJC(costesSSJor != null && !costesSSJor.isEmpty() ? new BigDecimal(costesSSJor.replaceAll(",", "\\.")) : null);
                        pros.setImpSSJR(costesSSPorJor != null && !costesSSPorJor.isEmpty() ? new BigDecimal(costesSSPorJor.replaceAll(",", "\\.")) : null);
                        pros.setNif(nif != null ? nif.toUpperCase() : null);
                        pros.setNombre(nomApel != null ? nomApel.toUpperCase() : null);
                        pros.setSolicitud(sol.getSolicitudCod());
                        pros.setVisitas(visitas != null && !visitas.isEmpty() ? Integer.valueOf(visitas) : null);
                        pros.setVisitasImp(visitasImp != null && !visitasImp.isEmpty() ? new BigDecimal(visitasImp.replaceAll(",", "\\.")) : null);
                        if (idProsOrigen != null && !idProsOrigen.isEmpty()) {
                            pros.setSolProspectorOrigen(Integer.valueOf(idProsOrigen));
                            pros.setTipoSust(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD);
                        }
                        EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente), adapt);

                        boolean soloFormato = pros.getSolProspectoresCod() != null;

                        EcaSolProspectoresVO origen = null;

                        if (pros.getSolProspectorOrigen() != null) {
                            origen = new EcaSolProspectoresVO();
                            origen.setSolProspectoresCod(pros.getSolProspectorOrigen());
                            origen = MeLanbide35Manager.getInstance().getProspectorSolicitudPorId(origen, adapt);
                        }

                        List<EcaSolProspectoresVO> sustitutos = MeLanbide35Manager.getInstance().getSustitutosProspectorSolicitud(pros.getSolProspectoresCod(), adapt);

                        validaciones = MeLanbide35Utils.validarEcaSolProspectoresVO(pros, origen, sustitutos, config, sol.getExpEje(), codIdioma, soloFormato, false);
                        if (validaciones == null || validaciones.isEmpty()) {
                            MeLanbide35Manager.getInstance().guardarEcaSolProspectoresVO(pros, adapt);
                            prospectores = MeLanbide35Manager.getInstance().getListaProspectoresSolicitud(sol, adapt, codIdioma);
                        } else {
                            codigoOperacion = "4";
                        }
                    }
                } else {
                    codigoOperacion = "5";
                }
            } else {
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }

        this.escribirListaProspectoresRequest(codigoOperacion, prospectores, validaciones, codIdioma, response);
    }

    public void eliminarProspectorSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        try {
            String idPros = request.getParameter("idPros");
            Integer idP = null;
            if (idPros == null || idPros.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idP = Integer.valueOf(idPros);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null) {
                    EcaSolProspectoresVO pros = new EcaSolProspectoresVO();
                    pros.setSolProspectoresCod(idP);
                    int result = MeLanbide35Manager.getInstance().eliminarProspectorSolicitud(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        if (codigoOperacion.equalsIgnoreCase("0")) {
            this.getListaProspectoresSolicitud(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
        } else {
            StringBuilder xmlSalida = new StringBuilder();
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
            } catch (IOException e) {
                e.printStackTrace();
            }//try-catch
        }
    }

    public void guardarDatosValoracionSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try {
                String numProyectosExp = request.getParameter("numProyectosExp");
                String puntuacionExp = request.getParameter("puntuacionExp");
                String porInsMujeres = request.getParameter("porInsMujeres");
                String puntuacionInsMujeres = request.getParameter("puntuacionInsMujeres");
                String porSensEmpresarial = request.getParameter("numProyectosSensEmpresarial");
                String puntuacionSensEmpresarial = request.getParameter("puntuacionSensEmpresarial");
                String totValoracionSol = request.getParameter("totValoracionSol");

                EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                if (sol != null) {
                    EcaSolValoracionVO valoracion = MeLanbide35Manager.getInstance().getValoracionSolicitud(sol, adapt);
                    if (valoracion == null) {
                        valoracion = new EcaSolValoracionVO();
                    }
                    valoracion.setSolicitud(sol.getSolicitudCod());
                    valoracion.setExperienciaNum(numProyectosExp != null && !numProyectosExp.isEmpty() ? Integer.valueOf(numProyectosExp) : null);
                    valoracion.setExperienciaVal(puntuacionExp != null && !puntuacionExp.isEmpty() ? new BigDecimal(puntuacionExp.replaceAll(",", "\\.")) : null);
                    valoracion.setInsMujeresNum(porInsMujeres != null && !porInsMujeres.isEmpty() ? new BigDecimal(porInsMujeres.replaceAll(",", "\\.")) : null);
                    valoracion.setInsMujeresVal(puntuacionInsMujeres != null && !puntuacionInsMujeres.isEmpty() ? new BigDecimal(puntuacionInsMujeres.replaceAll(",", "\\.")) : null);
                    valoracion.setSensibilidadNum(porSensEmpresarial != null && !porSensEmpresarial.isEmpty() ? Integer.valueOf(porSensEmpresarial) : null);
                    valoracion.setSensibilidadVal(puntuacionSensEmpresarial != null && !puntuacionSensEmpresarial.isEmpty() ? new BigDecimal(puntuacionSensEmpresarial.replaceAll(",", "\\.")) : null);
                    valoracion.setTotal(totValoracionSol != null && !totValoracionSol.isEmpty() ? new BigDecimal(totValoracionSol.replaceAll(",", "\\.")) : null);

                    valoracion = MeLanbide35Manager.getInstance().guardarDatosValoracionSolicitud(valoracion, adapt);
                    if (valoracion != null) {
                        codigoOperacion = "0";
                    } else {
                        codigoOperacion = "1";
                    }
                }
            } catch (Exception ex) {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE35.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuilder xmlSalida = new StringBuilder();
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
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    private String cargarSubpestanaSolicitud_Solicitud(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request) {

        if (sol != null) {
            try {
                int anio = MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumExp());
                EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(anio, adapt);
                request.setAttribute("ecaConfiguracion", config);
            } catch (Exception ex) {
            }

            try {
                //Columna de medio en subpesta?a solicitud (datos detalle anexos)
                DatosAnexosVO datosAnexos = MeLanbide35Manager.getInstance().getDatosSolicitudAnexos(sol, adapt);
                if (datosAnexos != null) {
                    request.setAttribute("datosAnexos", datosAnexos);
                }

                /*
                //ANTES COLUMNA DE EN MEDIO - CARGA AUTOMATICA
                DatosAnexosVO datosCarga = MeLanbide35Manager.getInstance().getDatosSolicitudCarga(sol, adapt);
                if(datosCarga != null)
                {
                    request.setAttribute("datosCarga", datosCarga);
                }*/
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
                List<FilaPreparadorSolicitudVO> listaPreparadoresSolicitud = MeLanbide35Manager.getInstance().getListaPreparadoresSolicitud(sol, adapt, codIdioma);
                if (listaPreparadoresSolicitud != null) {
                    request.setAttribute("listaPreparadoresSolicitud", listaPreparadoresSolicitud);

                    restarDatosPreparadoresConErrores(listaPreparadoresSolicitud, datosAnexos, adapt);
                }

                List<FilaProspectorSolicitudVO> listaProspectoresSolicitud = MeLanbide35Manager.getInstance().getListaProspectoresSolicitud(sol, adapt, codIdioma);
                if (listaProspectoresSolicitud != null) {
                    request.setAttribute("listaProspectoresSolicitud", listaProspectoresSolicitud);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "/jsp/extension/melanbide35/solicitud/solicitud.jsp";
    }

    private String cargarSubpestanaPreparadores_Solicitud(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (sol != null) {
            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }

            try {
                List<FilaPreparadorSolicitudVO> listaPreparadoresSolicitud = MeLanbide35Manager.getInstance().getListaPreparadoresSolicitud(sol, adapt, codIdioma);
                if (listaPreparadoresSolicitud != null) {
                    request.setAttribute("listaPreparadoresSolicitud", listaPreparadoresSolicitud);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        return "/jsp/extension/melanbide35/solicitud/preparadores.jsp";
    }

    private String cargarSubpestanaProspectores_Solicitud(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (sol != null) {

            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }

            try {
                List<FilaProspectorSolicitudVO> listaProspectoresSolicitud = MeLanbide35Manager.getInstance().getListaProspectoresSolicitud(sol, adapt, codIdioma);
                if (listaProspectoresSolicitud != null) {
                    request.setAttribute("listaProspectoresSolicitud", listaProspectoresSolicitud);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        return "/jsp/extension/melanbide35/solicitud/prospectores.jsp";
    }

    private String cargarSubpestanaValoracion_Solicitud(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (sol != null) {
            try {
                EcaSolValoracionVO valoracion = MeLanbide35Manager.getInstance().getValoracionSolicitud(sol, adapt);
                if (valoracion != null) {
                    request.setAttribute("valoracionSolicitud", valoracion);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
        return "/jsp/extension/melanbide35/solicitud/valoracion.jsp";
    }

    private void escribirListaPreparadoresRequest(String codigoOperacion, List<FilaPreparadorSolicitudVO> preparadores, List<String> validaciones, int codigoIdioma, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        for (FilaPreparadorSolicitudVO prep : preparadores) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getId());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ConstantesMeLanbide35.FALSO);
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NIF>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getNif());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_NIF));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NIF>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getNombreApel());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<FECHA_INICIO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getFechaInicio());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_INICIO>");
            xmlSalida.append("<FECHA_FIN>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getFechaFin());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_FIN>");
            xmlSalida.append("<HORAS_ANUALES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getHorasAnuales());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_ANUALES>");
            xmlSalida.append("<HORAS_CONTRATO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getHorasContrato());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_CONTRATO>");
            xmlSalida.append("<HORAS_DEDICACION_ECA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getHorasDedicacionECA());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_DEDICACION_ECA>");
            xmlSalida.append("<COSTES_SALARIALES_SS_JOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getCostesSalarialesSSJor());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_JOR>");
            xmlSalida.append("<COSTES_SALARIALES_SS_POR_JOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getCostesSalarialesSSPorJor());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_POR_JOR>");
            xmlSalida.append("<COSTES_SALARIALES_SS_ECA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getCostesSalarialesSSEca());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_ECA>");
            xmlSalida.append("<NUM_SEG_ANTERIORES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getNumSegAnteriores());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_NUM_SEG_ANTERIORES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NUM_SEG_ANTERIORES>");
            xmlSalida.append("<IMPORTE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getImporte());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</IMPORTE>");
            xmlSalida.append("<C1H>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC1h());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C1H));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C1H>");
            xmlSalida.append("<C1M>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC1m());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C1M));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C1M>");
            xmlSalida.append("<C1TOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC1Total());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C1TOTAL>");
            xmlSalida.append("<C2H>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC2h());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C2H));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C2H>");
            xmlSalida.append("<C2M>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC2m());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C2M));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C2M>");
            xmlSalida.append("<C2TOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC2Total());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C2TOTAL>");
            xmlSalida.append("<C3H>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC3h());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C3H));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C3H>");
            xmlSalida.append("<C3M>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC3m());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C3M));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C3M>");
            xmlSalida.append("<C3TOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC3Total());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C3TOTAL>");
            xmlSalida.append("<C4H>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC4h());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C4H));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C4H>");
            xmlSalida.append("<C4M>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC4m());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C4M));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C4M>");
            xmlSalida.append("<C4TOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC4m());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C4TOTAL>");
            xmlSalida.append("<INSERCIONES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getInserciones());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</INSERCIONES>");
            xmlSalida.append("<SEGUIMIENTOS_INSERCIONES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getSeguimientosInserciones());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</SEGUIMIENTOS_INSERCIONES>");
            xmlSalida.append("<COSTES_SALARIALES_SS>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getCostesSalarialesSS());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS>");
            xmlSalida.append("<IMPORTE_CONCEDIDO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getImporteConcedido());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</IMPORTE_CONCEDIDO>");
            if (prep.getTipoSust() != null && !prep.getTipoSust().isEmpty()) {
                xmlSalida.append("<TIPO_SUST>");
                xmlSalida.append(prep.getTipoSust());
                xmlSalida.append("</TIPO_SUST>");
            }
            xmlSalida.append("<ERRORES>");
            if (prep.getErrores() != null) {
                for (int contE = 0; contE < prep.getErrores().size(); contE++) {
                    xmlSalida.append("<ERROR>");
                    xmlSalida.append(prep.getErrores().get(contE));
                    xmlSalida.append("</ERROR>");
                }
            }
            xmlSalida.append("</ERRORES>");
            xmlSalida.append("</FILA>");
            i++;
        }
        if (validaciones != null && !validaciones.isEmpty()) {
            xmlSalida.append("<VALIDACION>");
            String validacion = validaciones.get(0);
            String mensajeCompleto = null;
            if (validacion != null && !validacion.isEmpty()) {
                if (validacion.contains("/")) {
                    String[] array = validacion.split("/");
                    String parte = null;
                    for (int j = 1; j < array.length; j++) {
                        parte = array[j];
                        if (mensajeCompleto == null) {
                            mensajeCompleto = new String(parte);
                        } else {
                            mensajeCompleto += ConstantesMeLanbide35.BARRA_SEPARADORA + parte;
                        }
                    }
                    //validacion = array[array.length-1];
                }
            } else {
                mensajeCompleto = MeLanbide35I18n.getInstance().getMensaje(codigoIdioma, "error.datosNoValidos");
            }
            xmlSalida.append(mensajeCompleto);
            xmlSalida.append("</VALIDACION>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    private void escribirListaProspectoresRequest(String codigoOperacion, List<FilaProspectorSolicitudVO> prospectores, List<String> validaciones, int codigoIdioma, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        for (FilaProspectorSolicitudVO pros : prospectores) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getId());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ConstantesMeLanbide35.FALSO);
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NIF>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getNif());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_NIF));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NIF>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getNombreApel());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<FECHA_INICIO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getFechaInicio());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_FECHA_INICIO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_INICIO>");
            xmlSalida.append("<FECHA_FIN>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getFechaFin());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_FIN>");
            xmlSalida.append("<HORAS_ANUALES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getHorasAnuales());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_HORAS_ANUALES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_ANUALES>");
            xmlSalida.append("<HORAS_CONTRATO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getHorasContrato());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_HORAS_CONTRATO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_CONTRATO>");
            xmlSalida.append("<HORAS_DEDICACION_ECA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getHorasDedicacionECA());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_DEDICACION_ECA>");
            xmlSalida.append("<COSTES_SALARIALES_SS_JOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getCostesSalarialesSSJor());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_JOR>");
            xmlSalida.append("<COSTES_SALARIALES_SS_POR_JOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getCostesSalarialesSSPorJor());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_POR_JOR>");
            xmlSalida.append("<COSTES_SALARIALES_SS_ECA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getCostesSalarialesSSEca());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_ECA>");
            xmlSalida.append("<VISITAS>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getVisitas());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_VISITAS));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</VISITAS>");
            xmlSalida.append("<VISITAS_IMP>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getVisitasImp());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_VISITAS_IMP));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</VISITAS_IMP>");
            xmlSalida.append("<COSTE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getCoste());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_COSTE));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTE>");
            xmlSalida.append("<IMPORTE_CONCEDIDO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getImporteConcedido());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</IMPORTE_CONCEDIDO>");
            if (pros.getTipoSust() != null) {
                xmlSalida.append("<TIPO_SUST>");
                xmlSalida.append(pros.getTipoSust());
                xmlSalida.append("</TIPO_SUST>");
            }
            xmlSalida.append("<ERRORES>");
            if (pros.getErrores() != null) {
                for (int contE = 0; contE < pros.getErrores().size(); contE++) {
                    xmlSalida.append("<ERROR>");
                    xmlSalida.append(pros.getErrores().get(contE));
                    xmlSalida.append("</ERROR>");
                }
            }
            xmlSalida.append("</ERRORES>");
            xmlSalida.append("</FILA>");
            i++;
        }
        if (validaciones != null && !validaciones.isEmpty()) {
            xmlSalida.append("<VALIDACION>");
            String validacion = validaciones.get(0);
            String mensajeCompleto = null;
            if (validacion != null && !validacion.isEmpty()) {
                if (validacion.contains("/")) {
                    String[] array = validacion.split("/");
                    String parte = null;
                    for (int j = 1; j < array.length; j++) {
                        parte = array[j];
                        if (mensajeCompleto == null) {
                            mensajeCompleto = new String(parte);
                        } else {
                            mensajeCompleto += ConstantesMeLanbide35.BARRA_SEPARADORA + parte;
                        }
                    }
                    //validacion = array[array.length-1];
                }
            } else {
                mensajeCompleto = MeLanbide35I18n.getInstance().getMensaje(codigoIdioma, "error.datosNoValidos");
            }
            xmlSalida.append(mensajeCompleto);
            xmlSalida.append("</VALIDACION>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    /**
     * ****************
     * JUSTIFICACION
     *
     **************
     */
    public void eliminarPreparadorJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {
        }

        List<FilaPreparadorJustificacionVO> preparadores = null;
        String codigoOperacion = "0";
        try {
            String idPrep = request.getParameter("idPrep");
            Integer idP = null;
            if (idPrep == null || idPrep.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idP = Integer.valueOf(idPrep);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null) {
                    EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                    prep.setJusPreparadoresCod(idP);
                    int result = MeLanbide35Manager.getInstance().eliminarPreparadorJustificacion(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        /*   if(codigoOperacion.equalsIgnoreCase("0"))
        {*/
        //this.getListaPreparadoresSoliictud(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
        try {
            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            preparadores = MeLanbide35Manager.getInstance().getListaPreparadoresJustificacion(sol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
            this.escribirListaPreparadoresJusRequest(codigoOperacion, preparadores, null, codIdioma, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }*/
    }

    public void guardarPreparadorJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codIdioma = 1;
        boolean soloFormato = false;
        List<String> validaciones = new ArrayList<String>();
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }

        String codigoOperacion = "0";
        List<FilaPreparadorJustificacionVO> preparadores = new ArrayList<FilaPreparadorJustificacionVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if (sol != null && sol.getSolicitudCod() != null) {

                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                //Recojo los parámetros
                String idPrep = request.getParameter("idPrep");
                String nif = request.getParameter("nif");
                String nomApel = request.getParameter("nomApel");
                String feIni = request.getParameter("feIni");
                String feFin = request.getParameter("feFin");
                String horasAnualesJC = request.getParameter("horasAnualesJC");
                String horasContrato = request.getParameter("horasContrato");
                String horasECA = request.getParameter("horasECA");
                String costesSSJor = request.getParameter("costesSSJor");
                String costesSSPorJor = request.getParameter("costesSSPorJor");
                String costesSSECA = request.getParameter("costesSSECA");
                String segAnt = request.getParameter("segAnt");
                String importe = request.getParameter("importe");
                String inserciones = request.getParameter("inserciones");
                String segIns = request.getParameter("segIns");
                String costesSS = request.getParameter("costesSS");
                String tipoContrato = request.getParameter("tipoContrato");
                String idPrepOrigen = request.getParameter("idPrepOrigen");

                //SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                EcaJusPreparadoresVO prepValida = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorNIF(sol, nif, feIni != null && !feIni.isEmpty() ? format.parse(feIni) : null, /*feFin,*/ adapt);
                boolean prepRepetido = false;
                if (prepValida != null && idPrep != null) {
                    if (!idPrep.equalsIgnoreCase(prepValida.getJusPreparadoresCod().toString())) {
                        prepRepetido = true;
                    }
                }
                if (!prepRepetido) {
                    EcaJusPreparadoresVO prep = null;
                    if (idPrep != null && !idPrep.isEmpty()) {
                        prep = new EcaJusPreparadoresVO();
                        prep.setJusPreparadoresCod(Integer.valueOf(idPrep));
                        prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prep, adapt);
                        soloFormato = true;
                    } else {
                        prep = new EcaJusPreparadoresVO();
                    }
                    if (prep == null) {
                        codigoOperacion = "3";
                    } else {
                        EcaSolPreparadoresVO prepRelacion = null;
                        try {
                            prepRelacion = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorNIF(sol, nif, adapt);
                        } catch (Exception ex) {

                        }
                        if (prepRelacion != null) {
                            prep.setSolPreparadoresCod(prepRelacion.getSolPreparadoresCod());
                        } else {
                            prep.setSolPreparadoresCod(null);
                        }

                        prep.setCoste(costesSS != null && !costesSS.isEmpty() ? new BigDecimal(costesSS.replaceAll(",", "\\.")) : null);
                        prep.setFecFin(feFin != null && !feFin.isEmpty() ? format.parse(feFin) : null);
                        prep.setFecIni(feIni != null && !feIni.isEmpty() ? format.parse(feIni) : null);
                        prep.setHorasCont(horasContrato != null && !horasContrato.isEmpty() ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                        prep.setHorasEca(horasECA != null && !horasECA.isEmpty() ? new BigDecimal(horasECA.replaceAll(",", "\\.")) : null);
                        prep.setHorasJC(horasAnualesJC != null && !horasAnualesJC.isEmpty() ? new BigDecimal(horasAnualesJC.replaceAll(",", "\\.")) : null);
                        prep.setImpSSECA(costesSSECA != null && !costesSSECA.isEmpty() ? new BigDecimal(costesSSECA.replaceAll(",", "\\.")) : null);
                        prep.setImpSSJC(costesSSJor != null && !costesSSJor.isEmpty() ? new BigDecimal(costesSSJor.replaceAll(",", "\\.")) : null);
                        prep.setImpSSJR(costesSSPorJor != null && !costesSSPorJor.isEmpty() ? new BigDecimal(costesSSPorJor.replaceAll(",", "\\.")) : null);
                        prep.setImpSegAnt(importe != null && !importe.isEmpty() ? new BigDecimal(importe.replaceAll(",", "\\.")) : null);
                        prep.setInsImporte(inserciones != null && !inserciones.isEmpty() ? new BigDecimal(inserciones.replaceAll(",", "\\.")) : null);
                        prep.setInsSegImporte(segIns != null && !segIns.isEmpty() ? new BigDecimal(segIns.replaceAll(",", "\\.")) : null);
                        prep.setNif(nif != null ? nif.toUpperCase() : null);
                        prep.setNombre(nomApel != null ? nomApel.toUpperCase() : null);
                        prep.setSegAnt(segAnt != null && !segAnt.isEmpty() ? new BigDecimal(segAnt.replaceAll(",", "\\.")) : null);
                        prep.setSolicitud(sol.getSolicitudCod());
                        prep.setTipoContrato(tipoContrato != null && !tipoContrato.isEmpty() ? Integer.valueOf(tipoContrato) : null);
                        if (idPrepOrigen != null && !idPrepOrigen.isEmpty()) {
                            prep.setJusPreparadorOrigen(Integer.valueOf(idPrepOrigen));
                            if (prep.getTipoSust() == null) {
                                prep.setTipoSust(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_JUSTIFICACION);
                            }
                        }

                        List<Integer> listaTipoContrato = MeLanbide35Manager.getInstance().getListaCodigosTipoContrato(adapt);
                        //String validacion = MeLanbide35Utils.validarEcaJusPreparadoresVO(prep,true);

                        EcaJusPreparadoresVO origen = null;

                        if (prep.getJusPreparadorOrigen() != null) {
                            origen = new EcaJusPreparadoresVO();
                            origen.setJusPreparadoresCod(prep.getJusPreparadorOrigen());
                            origen = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(origen, adapt);
                        }

                        List<EcaJusPreparadoresVO> sustitutos = MeLanbide35Manager.getInstance().getSustitutosPreparadorJustificacion(prep.getJusPreparadoresCod(), adapt);

                        validaciones = MeLanbide35Utils.validarEcaJusPreparadoresVO(prep, origen, sustitutos, listaTipoContrato, null, sol.getExpEje(), codIdioma, soloFormato, false);

                        if (validaciones == null || validaciones.isEmpty()) {
                            MeLanbide35Manager.getInstance().guardarEcaJusPreparadoresVO(prep, adapt);
                            preparadores = MeLanbide35Manager.getInstance().getListaPreparadoresJustificacion(sol, adapt, codIdioma);
                        } else {
                            codigoOperacion = "4";
                        }
                    }
                } else {
                    codigoOperacion = "5";
                }
            } else {
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }

        this.escribirListaPreparadoresJusRequest(codigoOperacion, preparadores, validaciones, codIdioma, response);
    }

    private void escribirListaPreparadoresJusRequest(String codigoOperacion, List<FilaPreparadorJustificacionVO> preparadores, List<String> validaciones, int codigoIdioma, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        for (FilaPreparadorJustificacionVO prep : preparadores) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getId());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ConstantesMeLanbide35.FALSO);
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NIF>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getNif());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_NIF));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NIF>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getNombreApel());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<FECHA_INICIO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getFechaInicio());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_INICIO>");
            xmlSalida.append("<FECHA_FIN>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getFechaFin());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_FIN>");
            xmlSalida.append("<TIPO_CONTRATO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getTipoContrato());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</TIPO_CONTRATO>");
            xmlSalida.append("<HORAS_ANUALES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getHorasAnuales());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_ANUALES>");
            xmlSalida.append("<HORAS_CONTRATO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getHorasContrato());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_CONTRATO>");
            xmlSalida.append("<HORAS_DEDICACION_ECA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getHorasDedicacionECA());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_DEDICACION_ECA>");
            xmlSalida.append("<COSTES_SALARIALES_SS_JOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getCostesSalarialesSSJor());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_JOR>");
            xmlSalida.append("<COSTES_SALARIALES_SS_POR_JOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getCostesSalarialesSSPorJor());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_POR_JOR>");
            xmlSalida.append("<COSTES_SALARIALES_SS_ECA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getCostesSalarialesSSEca());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_ECA>");
            xmlSalida.append("<NUM_SEG_ANTERIORES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getNumSegAnteriores());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NUM_SEG_ANTERIORES>");
            xmlSalida.append("<IMPORTE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getImporte());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_IMPORTE));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</IMPORTE>");
            xmlSalida.append("<C1H>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC1h());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C1H));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C1H>");
            xmlSalida.append("<C1M>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC1m());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C1M));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C1M>");
            xmlSalida.append("<C1TOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC1Total());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C1TOTAL>");
            xmlSalida.append("<C2H>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC2h());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C2H));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C2H>");
            xmlSalida.append("<C2M>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC2m());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C2M));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C2M>");
            xmlSalida.append("<C2TOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC2Total());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C2TOTAL>");
            xmlSalida.append("<C3H>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC3h());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C3H));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C3H>");
            xmlSalida.append("<C3M>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC3m());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C3M));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C3M>");
            xmlSalida.append("<C3TOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC3Total());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C3TOTAL>");
            xmlSalida.append("<C4H>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC4h());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C4H));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C4H>");
            xmlSalida.append("<C4M>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC4m());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C4M));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C4M>");
            xmlSalida.append("<C4TOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getC4Total());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</C4TOTAL>");
            xmlSalida.append("<INSERCIONES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getInserciones());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_INSERCIONES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</INSERCIONES>");
            xmlSalida.append("<SEGUIMIENTOS_INSERCIONES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getSeguimientosInserciones());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</SEGUIMIENTOS_INSERCIONES>");
            xmlSalida.append("<COSTES_SALARIALES_SS>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getCostesSalarialesSS());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS>");
            xmlSalida.append("<ERRORES>");
            if (prep.getErrores() != null) {
                for (int contE = 0; contE < prep.getErrores().size(); contE++) {
                    xmlSalida.append("<ERROR>");
                    xmlSalida.append(prep.getErrores().get(contE));
                    xmlSalida.append("</ERROR>");
                }
            }
            xmlSalida.append("</ERRORES>");
            if (prep.esSustituto() != null) {
                xmlSalida.append("<ES_SUSTITUTO>");
                xmlSalida.append(prep.esSustituto() ? "1" : "0");
                xmlSalida.append("</ES_SUSTITUTO>");
            }
            xmlSalida.append("</FILA>");
            i++;
        }
        if (validaciones != null && !validaciones.isEmpty()) {
            xmlSalida.append("<VALIDACION>");
            String validacion = validaciones.get(0);
            String mensajeCompleto = null;
            if (validacion != null && !validacion.isEmpty()) {
                if (validacion.contains("/")) {
                    String[] array = validacion.split("/");
                    String parte = null;
                    for (int j = 1; j < array.length; j++) {
                        parte = array[j];
                        if (mensajeCompleto == null) {
                            mensajeCompleto = new String(parte);
                        } else {
                            mensajeCompleto += ConstantesMeLanbide35.BARRA_SEPARADORA + parte;
                        }
                    }
                }
            } else {
                mensajeCompleto = MeLanbide35I18n.getInstance().getMensaje(codigoIdioma, "error.datosNoValidos");
            }
            xmlSalida.append(mensajeCompleto);
            xmlSalida.append("</VALIDACION>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch

    }

    public String procesarExcelSeguimientosPrep(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codIdioma = 1;

        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {
        }

        int anio = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
        if (anio < 2016) {//<2016
            try {
                String tipo = request.getParameter("tiposeg");
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

                wrapper.getParameterMap();
                CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
                handler.handleRequest(request);
                Hashtable<String, FormFile> table = handler.getFileElements();
                FormFile file = null;
                if (tipo.equals(ConstantesMeLanbide35.TIPO_SEGUIMIENTO_MELANBIDE35)) {
                    file = table.get("fichero_seguimientos_jus");
                } else {
                    file = table.get("fichero_inserciones_jus");
                }
                byte[] data = file.getFileData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                HSSFWorkbook workbook = new HSSFWorkbook(bais);

                HSSFSheet sheet = workbook.getSheetAt(0);

                int filaIni = sheet.getFirstRowNum();
                int filaFin = sheet.getLastRowNum();

                HSSFRow row = null;
                List<EcaSegPreparadoresVO> filasImportar = new ArrayList<EcaSegPreparadoresVO>();
                EcaSegPreparadoresVO seg = null;
                int i = 0;
                EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                try {
                    for (i = filaIni + 1; i <= filaFin; i++) {
                        row = sheet.getRow(i);
                        if (tipo.equals("0")) {
                            seg = (EcaSegPreparadoresVO) MeLanbide35MappingUtils.getInstance().map(row, EcaSegPreparadoresVO.class);
                        } else {
                            seg = (EcaSegPreparadoresVO) MeLanbide35MappingUtils.getInstance().map(row, EcaInsPreparadoresVO.class);
                        }
                        if (seg != null) {

                            //validacion = MeLanbide35Utils.validarEcaSegPreparadoresVO(seg,true);
                            /*validacion = MeLanbide35Utils.validarEcaSegPreparadoresVO
                            if(validacion.equals(ConstantesMeLanbide35.OK))
                            {   
                             */
                            EcaJusPreparadoresVO prep = null;
                            if (tipo.equals("1") && seg.getFecIni() != null) {
                                prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorNIF(sol, seg.getNifPreparador(), seg.getFecIni(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            } else if (tipo.equals("0") && seg.getFecIni() != null) {
                                prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorNIF(sol, seg.getNifPreparador(), seg.getFecIni(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            }
                            if (prep != null) {
                                seg.setJusPreparadoresCod(prep.getJusPreparadoresCod());
                                filasImportar.add(seg);
                            } else {
                                //throw new ExcelRowMappingException("13");  
                                ExcelRowMappingException erme = new ExcelRowMappingException("13");
                                erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.NifPreparadorNoEncontrado"), MeLanbide35Utils.fromColNumberToExcelColName(13), i + 1));
                                throw erme;
                            }
                            /* }
                            else
                            {
                               System.out.println("ERROR excepcion");
                                throw new FilaSeguimientoPrepNoValidaException();
                            }*/
                        }
                    }
                    if (filasImportar != null && !filasImportar.isEmpty()) {
                        int filasImportadas = 0;
                        if (tipo.equals(ConstantesMeLanbide35.TIPO_SEGUIMIENTO_MELANBIDE35)) {
                            filasImportadas = MeLanbide35Manager.getInstance().importarSeguimientos(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        } else {
                            filasImportadas = MeLanbide35Manager.getInstance().importarInserciones(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        }

                        if (filasImportadas < filasImportar.size()) {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                        } else {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1") + " " + filasImportadas + " " + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                        }
                    } else {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.noHayRegistrosImportar"));
                    }
                } catch (ExcelRowMappingException erme) {
                    String mensajeImportar = null;
                    if (erme.getMensaje() == null || erme.getMensaje().isEmpty()) {
                        mensajeImportar = MeLanbide35Utils.crearMensajeDatoExcelNoValido(erme, i + 1, codIdioma);
                        if (erme.getCampo().equals("13")) {
                            mensajeImportar = mensajeImportar + " " + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.NifPreparadorNoEncontrado");
                        }
                    } else {
                        mensajeImportar = erme.getMensaje();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);
                }
            } catch (FilaSeguimientoPrepNoValidaException ex) {
                ex.printStackTrace();
                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
            } catch (IOException ex) {
                ex.printStackTrace();
                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
            } catch (Exception ex) {
                ex.printStackTrace();
                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
            }
        } else {//a partir de 2016
            try {
                String tipo = request.getParameter("tiposeg");
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

                wrapper.getParameterMap();
                CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
                handler.handleRequest(request);
                Hashtable<String, FormFile> table = handler.getFileElements();
                FormFile file = null;
                if (tipo.equals(ConstantesMeLanbide35.TIPO_SEGUIMIENTO_MELANBIDE35)) {
                    file = table.get("fichero_seguimientos_jus");
                } else {
                    file = table.get("fichero_inserciones_jus");
                }
                byte[] data = file.getFileData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                HSSFWorkbook workbook = new HSSFWorkbook(bais);

                HSSFSheet sheet = workbook.getSheetAt(0);

                int filaIni = sheet.getFirstRowNum();
                int filaFin = sheet.getLastRowNum();

                HSSFRow row = null;
                List<EcaSegPreparadores2016VO> filasImportar = new ArrayList<EcaSegPreparadores2016VO>();
                EcaSegPreparadores2016VO seg = null;
                int i = 0;
                EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                try {
                    for (i = filaIni + 1; i <= filaFin; i++) {
                        row = sheet.getRow(i);
                        if (tipo.equals("0")) {
                            seg = (EcaSegPreparadores2016VO) MeLanbide35MappingUtils.getInstance().map(row, EcaSegPreparadores2016VO.class);
                        } else {
                            seg = (EcaSegPreparadores2016VO) MeLanbide35MappingUtils.getInstance().map(row, EcaInsPreparadores2016VO.class);
                        }
                        if (seg != null) {

                            //validacion = MeLanbide35Utils.validarEcaSegPreparadoresVO(seg,true);
                            /*validacion = MeLanbide35Utils.validarEcaSegPreparadoresVO
                            if(validacion.equals(ConstantesMeLanbide35.OK))
                            {   
                             */
                            EcaJusPreparadoresVO prep = null;
                            if (tipo.equals("1") && seg.getFecIni() != null) {
                                prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorNIF(sol, seg.getNifPreparador(), seg.getFecIni(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            } else if (tipo.equals("0") && seg.getFecIni() != null) {
                                prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorNIF(sol, seg.getNifPreparador(), seg.getFecIni(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            }
                            if (prep != null) {
                                seg.setJusPreparadoresCod(prep.getJusPreparadoresCod());
                                filasImportar.add(seg);
                            } else {
                                //throw new ExcelRowMappingException("13");  
                                ExcelRowMappingException erme = new ExcelRowMappingException("13");
                                erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.NifPreparadorNoEncontrado"), MeLanbide35Utils.fromColNumberToExcelColName(13), i + 1));
                                throw erme;
                            }
                            /* }
                            else
                            {
                               System.out.println("ERROR excepcion");
                                throw new FilaSeguimientoPrepNoValidaException();
                            }*/
                        }
                    }
                    if (filasImportar != null && !filasImportar.isEmpty()) {
                        int filasImportadas = 0;
                        if (tipo.equals(ConstantesMeLanbide35.TIPO_SEGUIMIENTO_MELANBIDE35)) {
                            filasImportadas = MeLanbide35Manager.getInstance().importarSeguimientos2016(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        } else {
                            filasImportadas = MeLanbide35Manager.getInstance().importarInserciones2016(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        }

                        if (filasImportadas < filasImportar.size()) {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                        } else {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1") + " " + filasImportadas + " " + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                        }
                    } else {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.noHayRegistrosImportar"));
                    }
                } catch (ExcelRowMappingException erme) {
                    String mensajeImportar = null;
                    if (erme.getMensaje() == null || erme.getMensaje().isEmpty()) {
                        mensajeImportar = MeLanbide35Utils.crearMensajeDatoExcelNoValido(erme, i + 1, codIdioma);
                        if (erme.getCampo().equals("13")) {
                            mensajeImportar = mensajeImportar + " " + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.NifPreparadorNoEncontrado");
                        }
                    } else {
                        mensajeImportar = erme.getMensaje();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);
                }
            } catch (FilaSeguimientoPrepNoValidaException ex) {
                ex.printStackTrace();
                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
            } catch (IOException ex) {
                ex.printStackTrace();
                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
            } catch (Exception ex) {
                ex.printStackTrace();
                request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
            }
        }
        return "/jsp/extension/melanbide35/justificacion/recargarListaSeguimientosPrep.jsp";
    }

    public String cargarSeguimientos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "";
        try {
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            try {
                if (usuario != null) {
                    idiomaUsuario = usuario.getIdioma();
                }
            } catch (Exception ex) {
            }
            String tipo = request.getParameter("tiposeg");
            String colectivo = request.getParameter("colectivo") != null ? request.getParameter("colectivo") : "";
            String sexo = request.getParameter("sexo") != null ? request.getParameter("sexo") : "";
            if (tipo.equals("0")) {
                url = "/jsp/extension/melanbide35/justificacion/seguimientos.jsp";
            } else {
                url = "/jsp/extension/melanbide35/justificacion/inserciones.jsp";
            }
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            String idprep = request.getParameter("idPrep");
            if (idprep != null) {

                EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                prep.setJusPreparadoresCod(Integer.valueOf(idprep));

                prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prep, adapt);
                if (prep != null) {
                    request.setAttribute("preparador", prep);
                    if (tipo.equals("0")) {
                        List<FilaSegPreparadoresVO> listaSeguimientos = MeLanbide35Manager.getInstance().getListaSeguimientos(sol, prep, tipo, colectivo, sexo, idiomaUsuario, adapt);
                        if (listaSeguimientos != null) {
                            request.setAttribute("listaSeguimientos", listaSeguimientos);
                            request.setAttribute("colectivo", colectivo);
                            request.setAttribute("sexo", sexo);
                        }
                    } else {
                        List<FilaInsPreparadoresVO> listaInserciones = MeLanbide35Manager.getInstance().getListaInserciones(sol, prep, tipo, colectivo, sexo, idiomaUsuario, adapt);
                        if (listaInserciones != null) {
                            request.setAttribute("listaSeguimientos", listaInserciones);
                            request.setAttribute("colectivo", colectivo);
                            request.setAttribute("sexo", sexo);
                        }
                    }
                }
            } else {
                if (tipo.equals("0")) {
                    List<FilaSegPreparadoresVO> listaSeguimientos = MeLanbide35Manager.getInstance().getListaSeguimientos(sol, null, tipo, colectivo, sexo, idiomaUsuario, adapt);
                    if (listaSeguimientos != null) {
                        request.setAttribute("listaSeguimientos", listaSeguimientos);
                    }
                } else {
                    List<FilaInsPreparadoresVO> listaInserciones = MeLanbide35Manager.getInstance().getListaInserciones(sol, null, tipo, colectivo, sexo, idiomaUsuario, adapt);
                    if (listaInserciones != null) {
                        request.setAttribute("listaSeguimientos", listaInserciones);
                    }
                }
            }

            String opcion = request.getParameter("opcion");
            if (opcion.equalsIgnoreCase("consultar")) {
                request.setAttribute("consulta", true);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return url;
    }

    public void getListaPreparadoresJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            try {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            } catch (Exception ex) {

            }

            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide35Manager meLanbide35Manager = MeLanbide35Manager.getInstance();
            EcaSolicitudVO sol = meLanbide35Manager.getDatosSolicitud(numExpediente, adapt);
            List<FilaPreparadorJustificacionVO> preparadores = null;

            if (sol != null) {
                preparadores = MeLanbide35Manager.getInstance().getListaPreparadoresJustificacion(sol, adapt, codIdioma);
            } else {
                preparadores = new ArrayList<FilaPreparadorJustificacionVO>();
            }

            String codigoOperacion = "0";

            this.escribirListaPreparadoresJusRequest(codigoOperacion, preparadores, null, codIdioma, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cargarComboProcesos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        int codIdioma = 1;

        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        if (usuario != null) {
            codIdioma = usuario.getIdioma();
        }

        List<SelectItem> listaProcesos = new ArrayList<SelectItem>();
        try {
            SelectItem si = null;

            si = new SelectItem();

            si.setId(ConstantesMeLanbide35.COD_PROC_RESOLUCION_PROV);
            si.setLabel(MeLanbide35Utils.obtenerNombreProceso(ConstantesMeLanbide35.COD_PROC_RESOLUCION_PROV, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide35.COD_PROC_DOC_RESOLUCION);
            si.setLabel(MeLanbide35Utils.obtenerNombreProceso(ConstantesMeLanbide35.COD_PROC_DOC_RESOLUCION, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide35.COD_PROC_CONSOLIDAR);
            si.setLabel(MeLanbide35Utils.obtenerNombreProceso(ConstantesMeLanbide35.COD_PROC_CONSOLIDAR, codIdioma));
            listaProcesos.add(si);

            si = new SelectItem();
            si.setId(ConstantesMeLanbide35.COD_PROC_DESHACER_CONSOLIDACION);
            si.setLabel(MeLanbide35Utils.obtenerNombreProceso(ConstantesMeLanbide35.COD_PROC_DESHACER_CONSOLIDACION, codIdioma));
            listaProcesos.add(si);
        } catch (Exception ex) {

        }
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append("0");
        xmlSalida.append("</CODIGO_OPERACION>");
        for (SelectItem si : listaProcesos) {
            xmlSalida.append("<SELECT_ITEM>");
            xmlSalida.append("<ID>");
            xmlSalida.append(si.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<LABEL>");
            xmlSalida.append(si.getLabel());
            xmlSalida.append("</LABEL>");
            xmlSalida.append("</SELECT_ITEM>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    public void filtrarAuditoriaProcesos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<FilaAuditoriaProcesosVO> audList = new ArrayList<FilaAuditoriaProcesosVO>();
        List<FilaAuditoriaProcesosVO> tempList = new ArrayList<FilaAuditoriaProcesosVO>();
        try {
            String p1 = request.getParameter("pagAct");
            String p2 = request.getParameter("maxFilas");

            String nombre = request.getParameter("nomApellidos");
            String p3 = request.getParameter("feDesde");
            String p4 = request.getParameter("feHasta");
            String p5 = request.getParameter("codProc");

            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

            Date feDesde = null;
            if (p3 != null && !p3.isEmpty()) {
                try {
                    feDesde = format.parse(p3);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(feDesde);
                    cal.set(Calendar.HOUR, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    feDesde = cal.getTime();
                } catch (ParseException ex) {

                }
            }
            Date feHasta = null;
            if (p4 != null && !p4.isEmpty()) {
                try {
                    feHasta = format.parse(p4);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(feHasta);
                    cal.set(Calendar.HOUR, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    feHasta = cal.getTime();
                } catch (ParseException ex) {

                }
            }

            Integer codProc = null;
            if (p5 != null && !p5.isEmpty()) {
                codProc = Integer.valueOf(p5);
            }

            Integer act = null;
            Integer max = null;
            try {
                act = Integer.valueOf(p1);
                max = Integer.valueOf(p2);
            } catch (NumberFormatException ex) {
                act = 0;
                max = 0;
            }

            int codIdioma = 1;

            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }

            tempList = MeLanbide35Manager.getInstance().filtrarAuditoriaProcesos(nombre, feDesde, feHasta, codProc, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            int desde = act - 1;
            desde = desde * max;
            if (desde < 0) {
                desde = 0;
            }
            int hasta = desde + max;
            if (hasta > tempList.size()) {
                hasta = tempList.size();
            }

            for (int i = desde; i < hasta; i++) {
                audList.add(tempList.get(i));
            }
        } catch (Exception ex) {

        }
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append("0");
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<TOTAL_REGISTROS>");
        xmlSalida.append(tempList.size());
        xmlSalida.append("</TOTAL_REGISTROS>");
        for (FilaAuditoriaProcesosVO fila : audList) {
            xmlSalida.append("<REGISTRO>");
            xmlSalida.append("<NOMAPELLIDOS>");
            xmlSalida.append(fila.getNomApellidos());
            xmlSalida.append("</NOMAPELLIDOS>");
            xmlSalida.append("<PROCESO>");
            xmlSalida.append(fila.getProceso());
            xmlSalida.append("</PROCESO>");
            xmlSalida.append("<FECHA>");
            xmlSalida.append(fila.getFecha());
            xmlSalida.append("</FECHA>");
            xmlSalida.append("<RESULTADO>");
            xmlSalida.append(fila.getResultado());
            xmlSalida.append("</RESULTADO>");
            xmlSalida.append("</REGISTRO>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    public void generarInformeDesglose(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String anio = request.getParameter("anio");
            String formato = request.getParameter("formato");

            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }

            List<FilaInformeDesglose> filas = MeLanbide35Manager.getInstance().getDatosInformeDesglose(anio, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (formato != null) {
                if (formato.equalsIgnoreCase("pdf") || formato.equalsIgnoreCase("word")) {
                    MeLanbide35InformeUtils.getInstance().generarInformeDesglosePDF(filas, anio, formato, codIdioma, response);
                } else if (formato.equalsIgnoreCase("excel")) {
                    MeLanbide35InformeUtils.getInstance().generarInformeDesgloseExcel(filas, anio, codIdioma, response);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void generarInformeProyectos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String anio = request.getParameter("anio");
            String formato = request.getParameter("formato");

            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            MeLanbide35Manager manager = MeLanbide35Manager.getInstance();

            List<FilaResumenInformeProyectos> filasResumen = manager.getDatosResumenInformeProyectos(anio, adapt);

            HashMap<String, List<FilaInformeProyectos>> filasPorExpediente = new HashMap<String, List<FilaInformeProyectos>>();

            for (FilaResumenInformeProyectos fila : filasResumen) {
                filasPorExpediente.put(fila.getNumExp(), manager.getDatosInformeProyectos(fila.getNumExp(), adapt));
            }

            MeLanbide35Manager.getInstance().getDatosInformeProyectos(anio, adapt);

            if (formato != null) {
                if (formato.equalsIgnoreCase("pdf") || formato.equalsIgnoreCase("word")) {
                    MeLanbide35InformeUtils.getInstance().generarInformeProyectosPDF(filasResumen, filasPorExpediente, anio, formato, codIdioma, response);
                } else if (formato.equalsIgnoreCase("excel")) {
                    MeLanbide35InformeUtils.getInstance().generarInformeProyectosExcel(filasResumen, filasPorExpediente, anio, codIdioma, response);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String cargarNuevoSeguimientoPreparador(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        int codIdioma = 1;

        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        if (usuario != null) {
            codIdioma = usuario.getIdioma();
        }
        try {

            String sexo = request.getParameter("sexo");
            String colectivo = request.getParameter("colectivo");
            String preparador = request.getParameter("preparador");
            String tiposeg = request.getParameter("tiposeg");
            if (tiposeg != null) {
                request.setAttribute("tiposeg", tiposeg);
            }
            String opcion = request.getParameter("opcion");
            if (opcion != null) {
                if (opcion.equalsIgnoreCase("nuevo")) {
                    //Cargar nuevo preparador
                } else if (opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar")) {
                    //Cargar modificar preparador
                    String idSeg = request.getParameter("idSegModificar");
                    if (idSeg != null) {

                        String[] datos = idSeg.split("_");

                        EcaSegPreparadoresVO seg = new EcaSegPreparadoresVO();
                        seg.setJusPreparadoresCod(Integer.valueOf(datos[0]));
                        seg.setSegPreparadoresCod(Integer.valueOf(datos[1]));
                        seg = MeLanbide35Manager.getInstance().getSeguimientoPreparadorPorId(seg, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if (seg != null) {
                            request.setAttribute("seguimientoModif", seg);
                        }
                    }
                    if (opcion.equalsIgnoreCase("consultar")) {
                        request.setAttribute("consulta", true);
                    }

                }
                List<SelectItem> listaTipoContrato = MeLanbide35Manager.getInstance().getListaTipoContrato(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<SelectItem> listaTipoDiscapacidad = MeLanbide35Manager.getInstance().getListaTipoDiscapacidad(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<SelectItem> listaGravedad = MeLanbide35Manager.getInstance().getListaGravedad(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                request.setAttribute("lstTipoContrato", listaTipoContrato);
                request.setAttribute("lstTipoDiscapacidad", listaTipoDiscapacidad);
                request.setAttribute("lstGravedad", listaGravedad);
                request.setAttribute("colectivo", colectivo);
                request.setAttribute("sexo", sexo);
                request.setAttribute("preparador", preparador);
                if (preparador != null) {
                    EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                    prep.setJusPreparadoresCod(Integer.valueOf(preparador));
                    prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    request.setAttribute("nifprep", prep.getNif());
                    log.debug("nifpreparador:" + prep.getNif());
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide35/justificacion/nuevoSeguimientoPreparador.jsp";
    }

    public void guardarSeguimientoPreparador(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }

        String codigoOperacion = "0";
        List<FilaSegPreparadoresVO> seguimientos = null;
        List<FilaInsPreparadoresVO> inserciones = null;
        List<String> validaciones = new ArrayList<String>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String tiposeg = "0";
            String sexosegs = "";
            String colectivosegs = "";
            EcaJusPreparadoresVO prepseg = null;
            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if (sol != null && sol.getSolicitudCod() != null) {

                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                //Recojo los parámetros
                String idPrep = request.getParameter("idPrep");
                String idSeg = request.getParameter("idSeg");
                String nif = request.getParameter("nif");
                String nomApel = request.getParameter("nomApel");
                String feIni = request.getParameter("feIni");
                String feFin = request.getParameter("feFin");
                String horasContrato = request.getParameter("horasCont");
                String fecNacimiento = request.getParameter("fecNacimiento");
                String sexo = request.getParameter("sexo");
                String tipoDiscap = request.getParameter("tipoDiscap");
                String gravedad = request.getParameter("gravedad");
                String tipoCont = request.getParameter("tipoCont");
                String porcJorn = request.getParameter("porcJorn");
                String finContratoDespido = request.getParameter("finContratoDespido");
                String empresa = request.getParameter("empresa");
                //String perscont = (String)request.getParameter("perscont");
                String fecSeguimiento = request.getParameter("fecSeguimiento");
                String observaciones = request.getParameter("observaciones");
                tiposeg = request.getParameter("tiposeg");
                String nifprep = request.getParameter("nifPreparador");

                sexosegs = request.getParameter("sexosegs");
                colectivosegs = request.getParameter("colectivosegs");
                String idprepsegs = request.getParameter("prepsegs");
                if (idprepsegs != null && !idprepsegs.isEmpty()) {
                    prepseg = new EcaJusPreparadoresVO();
                    prepseg.setJusPreparadoresCod(Integer.valueOf(idprepsegs));
                    prepseg = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prepseg, adapt);
                }

                EcaSegPreparadoresVO seg = null;
                boolean soloFormato = false;
                if (idPrep != null && !idPrep.isEmpty() && idSeg != null && !idSeg.isEmpty()) {
                    seg = new EcaSegPreparadoresVO();
                    seg.setJusPreparadoresCod(Integer.valueOf(idPrep));
                    seg.setSegPreparadoresCod(Integer.valueOf(idSeg));
                    seg = MeLanbide35Manager.getInstance().getSeguimientoPreparadorPorId(seg, adapt);
                    soloFormato = true;
                } else {
                    seg = new EcaSegPreparadoresVO();
                    if (prepseg == null) {
                        //EcaJusPreparadoresVO prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorNIF(sol, nifprep.toUpperCase(),tiposeg.equals("0")?format.parse( fecSeguimiento):format.parse(feIni), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        EcaJusPreparadoresVO prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorNIF(sol, nifprep.toUpperCase(), tiposeg.equals("0") ? format.parse(feIni) : format.parse(feIni), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                        if (prep != null) {
                            seg.setJusPreparadoresCod(prep.getJusPreparadoresCod());
                        }
                    } else {
                        seg.setJusPreparadoresCod(prepseg.getJusPreparadoresCod());
                    }

                }
                if (seg == null) {
                    codigoOperacion = "3";
                } else {
                    /*seg.setNif(nif != null ? nif.toUpperCase() : null);
                    seg.setNombre(nomApel != null ? nomApel.toUpperCase() : null);                    
                    seg.setFecFin(feFin != null && !feFin.equals("") ? format.parse(feFin) : null);
                    seg.setFecIni(feIni != null && !feIni.equals("") ? format.parse(feIni) : null);
                    seg.setHorasCont(horasContrato != null && !horasContrato.equals("") ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                    seg.setFecNacimiento(fecNacimiento != null && !fecNacimiento.equals("") ? format.parse(fecNacimiento) : null);
                    seg.setSexo(sexo != null && !sexo.equals("") ? Integer.parseInt(sexo) : null);
                    seg.setTipoDiscapacidad(tipoDiscap != null && !tipoDiscap.equals("") ? Integer.parseInt(tipoDiscap) : null);
                    seg.setGravedad(gravedad != null && !gravedad.equals("") ? Integer.parseInt(gravedad) : null);
                    seg.setTipoContrato(tipoCont != null && !tipoCont.equals("") ? Integer.parseInt(tipoCont) : null);
                    seg.setPorcJornada(porcJorn != null && !porcJorn.equals("") ? new BigDecimal(porcJorn.replaceAll(",", "\\.")) : null);
                    seg.setEmpresa(empresa != null ? empresa.toUpperCase() : null);   
                    seg.setNomPersContacto(perscont != null ? perscont.toUpperCase() : null);   
                    seg.setFecSeguimiento(fecSeguimiento != null && !fecSeguimiento.equals("") ? format.parse(fecSeguimiento) : null);
                    seg.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);
                    seg.setTipo(tiposeg != null && !tiposeg.equals("") ? Integer.parseInt(tiposeg.toUpperCase()) : null);
                                        
                    
                    List<Integer> listaTipoContrato =  MeLanbide35Manager.getInstance().getListaCodigosTipoContrato(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    List<Integer> listaTipoDiscapacidad =  MeLanbide35Manager.getInstance().getListaCodigosTipoDiscapacidad(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    List<Integer> listaGravedad =  MeLanbide35Manager.getInstance().getListaCodigosGravedad(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    
                    EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente), adapt);
                    List<String> validacion = MeLanbide35Utils.validarEcaSegPreparadoresVO(sol.getNumExp(), seg, config, codIdioma, listaTipoDiscapacidad, listaGravedad,listaTipoContrato, soloFormato);
                    if(validacion == null || validacion.size() == 0)
                    {
                        MeLanbide35Manager.getInstance().guardarEcaSegPreparadoresVO(seg, adapt);
                    }
                    else
                    {
                        if (seg.getJusPreparadoresCod()!=null)
                        codigoOperacion = "4";
                        else codigoOperacion = "5";
                    }*/

                    if (seg.getJusPreparadoresCod() != null) {
                        seg.setNif(nif != null ? nif.toUpperCase() : null);
                        seg.setNombre(nomApel != null ? nomApel.toUpperCase() : null);
                        seg.setFecFin(feFin != null && !feFin.isEmpty() ? format.parse(feFin) : null);
                        seg.setFecIni(feIni != null && !feIni.isEmpty() ? format.parse(feIni) : null);
                        seg.setHorasCont(horasContrato != null && !horasContrato.isEmpty() ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                        seg.setFecNacimiento(fecNacimiento != null && !fecNacimiento.isEmpty() ? format.parse(fecNacimiento) : null);
                        seg.setSexo(sexo != null && !sexo.isEmpty() ? Integer.valueOf(sexo) : null);
                        seg.setTipoDiscapacidad(tipoDiscap != null && !tipoDiscap.isEmpty() ? Integer.valueOf(tipoDiscap) : null);
                        seg.setGravedad(gravedad != null && !gravedad.isEmpty() ? Integer.valueOf(gravedad) : null);
                        seg.setTipoContrato(tipoCont != null && !tipoCont.isEmpty() ? Integer.valueOf(tipoCont) : null);
                        seg.setPorcJornada(porcJorn != null && !porcJorn.isEmpty() ? new BigDecimal(porcJorn.replaceAll(",", "\\.")) : null);
                        seg.setFinContratoDespido(finContratoDespido != null && !finContratoDespido.isEmpty() ? Integer.valueOf(finContratoDespido) : null);
                        seg.setEmpresa(empresa != null ? empresa.toUpperCase() : null);
                        //seg.setNomPersContacto(perscont != null ? perscont.toUpperCase() : null);   
                        if (tiposeg.equals("0")) {
                            seg.setFecSeguimiento(fecSeguimiento != null && !fecSeguimiento.isEmpty() ? format.parse(fecSeguimiento) : null);
                        }
                        seg.setObservaciones(observaciones != null && !observaciones.isEmpty() ? observaciones.toUpperCase() : null);
                        seg.setTipo(tiposeg != null && !tiposeg.isEmpty() ? Integer.valueOf(tiposeg.toUpperCase()) : null);

                        List<Integer> listaTipoContrato = MeLanbide35Manager.getInstance().getListaCodigosTipoContrato(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        List<Integer> listaTipoDiscapacidad = MeLanbide35Manager.getInstance().getListaCodigosTipoDiscapacidad(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        List<Integer> listaGravedad = MeLanbide35Manager.getInstance().getListaCodigosGravedad(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                        EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente), adapt);
                        validaciones = MeLanbide35Utils.validarEcaSegPreparadoresVO(sol.getNumExp(), seg, config, codIdioma, listaTipoDiscapacidad, listaGravedad, listaTipoContrato, soloFormato, false);
                        if (validaciones == null || validaciones.isEmpty()) {
                            MeLanbide35Manager.getInstance().guardarEcaSegPreparadoresVO(seg, adapt);
                        } else {
                            codigoOperacion = "4";
                        }
                    } else {
                        codigoOperacion = "5";
                    }
                }
            } else {
                codigoOperacion = "1";
            }

            if (tiposeg.equals("0")) {
                seguimientos = MeLanbide35Manager.getInstance().getListaSeguimientos(sol, prepseg, tiposeg, colectivosegs, sexosegs, codIdioma, adapt);
            } else {
                inserciones = MeLanbide35Manager.getInstance().getListaInserciones(sol, prepseg, tiposeg, colectivosegs, sexosegs, codIdioma, adapt);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }

        if (seguimientos != null) {
            this.escribirListaSegPreparadoresRequest(codigoOperacion, seguimientos, validaciones, codIdioma, response);
        } else {
            this.escribirListaInsPreparadoresRequest(codigoOperacion, inserciones, validaciones, codIdioma, response);
        }
    }

    public void descargarPlantilla(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String nombrePlantilla = request.getParameter("nombrePlantilla");
            String urlArchivo = "/es/altia/flexia/integracion/moduloexterno/melanbide35/plantillas/" + nombrePlantilla;

            /*  //MODIFICAR CABECERA            
            FileInputStream file;
            file = new FileInputStream(new File(urlArchivo));
            //Get the workbook instance for XLS file 
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            
             //Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Pongo el nombre de la ubicacion del centro logeado
            HSSFRow row = sheet.getRow(0);
            HSSFCell cell = row.getCell(10);                       
            cell.setCellValue("anteriores a "+MeLanbide44Utils.getEjercicioDeExpediente(numExpediente));
            
            file.close();
                BufferedInputStream bstr = new BufferedInputStream( file ); // promote
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("plantilla", ".xls", directorioTemp);
                int size = (int) informe.length(); 
                byte[] data = new byte[size]; 
                bstr.read( data, 0, size ); 
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");  
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            
            
             */
            InputStream is = getClass().getClassLoader().getResourceAsStream(urlArchivo);

            byte[] data = IOUtils.toByteArray(is);

            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setHeader("Content-Type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + nombrePlantilla);

            response.setContentLength(data.length);
            response.getOutputStream().write(data, 0, data.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void escribirListaSegPreparadoresRequest(String codigoOperacion, List<FilaSegPreparadoresVO> seguimientos, List<String> validaciones, int codigoIdioma, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        for (FilaSegPreparadoresVO seg : seguimientos) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getCodPreparador()).append("_").append(seg.getIdseg());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ConstantesMeLanbide35.FALSO);
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NIF>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getNif());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_NIF));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NIF));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NIF>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getNombreApe());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_NOMBREAPEL));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NOMBRE>");

            xmlSalida.append("<FECHA_NACIMIENTO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getFecNacimiento());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_NACIMIENTO>");
            xmlSalida.append("<SEXO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getSexo());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_SEXO));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_SEXO));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</SEXO>");
            xmlSalida.append("<TIPO_DISCAPACIDAD>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getTipoDiscapacidad());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</TIPO_DISCAPACIDAD>");
            xmlSalida.append("<GRAVEDAD>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getGravedad());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_GRAVEDAD));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</GRAVEDAD>");
            xmlSalida.append("<TIPO_CONTRATO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getTipoContrato());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_TIPOCONTRATO));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</TIPO_CONTRATO>");
            xmlSalida.append("<PORC_JORNADA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getPorcJornada());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_PORCJORN));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_PORCJORN));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</PORC_JORNADA>");
            xmlSalida.append("<FECHA_INICIO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getFecIni());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_FECHA_INICIO));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_INICIO>");
            xmlSalida.append("<FECHA_FIN>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getFecFin());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_FECHA_FIN));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_FIN>");
            xmlSalida.append("<NIF_PREPARADOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getNifPreparador());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_NIF_PREPARADOR));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NIF_PREPARADOR>");
            xmlSalida.append("<EMPRESA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getEmpresa());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_EMPRESA));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_EMPRESA));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</EMPRESA>");

            xmlSalida.append("<HORAS_CONTRATO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getHorasCont());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_HORAS_ANUALES));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_CONTRATO>");
            xmlSalida.append("<OBSERVACIONES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(seg.getObservaciones());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            if (seg.getTipo().equals("0")) {
                xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_OBSERVACIONES));
            } else {
                xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES));
            }
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</OBSERVACIONES>");
            xmlSalida.append("<ERRORES>");
            if (seg.getErrores() != null) {
                for (int contE = 0; contE < seg.getErrores().size(); contE++) {
                    xmlSalida.append("<ERROR>");
                    xmlSalida.append(seg.getErrores().get(contE));
                    xmlSalida.append("</ERROR>");
                }
            }
            xmlSalida.append("</ERRORES>");
            xmlSalida.append("</FILA>");
            i++;
        }
        if (validaciones != null && !validaciones.isEmpty()) {
            xmlSalida.append("<VALIDACION>");
            String validacion = validaciones.get(0);
            if (validacion != null && !validacion.isEmpty()) {
                if (validacion.contains("/")) {
                    String[] array = validacion.split("/");
                    validacion = array[array.length - 1];
                }
            } else {
                validacion = MeLanbide35I18n.getInstance().getMensaje(codigoIdioma, "error.datosNoValidos");
            }
            xmlSalida.append(validacion);
            xmlSalida.append("</VALIDACION>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    private void escribirListaInsPreparadoresRequest(String codigoOperacion, List<FilaInsPreparadoresVO> inserciones, List<String> validaciones, int codigoIdioma, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        for (FilaInsPreparadoresVO ins : inserciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getCodPreparador()).append("_").append(ins.getIdseg());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ConstantesMeLanbide35.FALSO);
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NIF>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getNif());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NIF));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NIF>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getNombreApe());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NOMBRE>");

            xmlSalida.append("<FECHA_NACIMIENTO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getFecNacimiento());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_NACIMIENTO>");
            xmlSalida.append("<SEXO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getSexo());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_SEXO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</SEXO>");
            xmlSalida.append("<COLECTIVO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getColectivo());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COLECTIVO>");
            xmlSalida.append("<TIPO_DISCAPACIDAD>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getTipoDiscapacidad());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</TIPO_DISCAPACIDAD>");
            xmlSalida.append("<GRAVEDAD>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getGravedad());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</GRAVEDAD>");
            xmlSalida.append("<TIPO_CONTRATO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getTipoContrato());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</TIPO_CONTRATO>");
            xmlSalida.append("<PORC_JORNADA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getPorcJornada());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_PORCJORN));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</PORC_JORNADA>");
            xmlSalida.append("<FECHA_INICIO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getFecIni());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_INICIO>");
            xmlSalida.append("<FECHA_FIN>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getFecFin());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_FIN>");
            xmlSalida.append("<NIF_PREPARADOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getNifPreparador());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NIF_PREPARADOR>");
            xmlSalida.append("<EMPRESA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getEmpresa());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_EMPRESA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</EMPRESA>");
            /*xmlSalida.append("<PERS_CONTACTO>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(ins.getNombrePersContacto());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_CONTACTO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</PERS_CONTACTO>");  */
            xmlSalida.append("<HORAS_CONTRATO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getHorasCont());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_CONTRATO>");
            xmlSalida.append("<OBSERVACIONES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(ins.getObservaciones());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</OBSERVACIONES>");
            xmlSalida.append("<ERRORES>");
            if (ins.getErrores() != null) {
                for (int contE = 0; contE < ins.getErrores().size(); contE++) {
                    xmlSalida.append("<ERROR>");
                    xmlSalida.append(ins.getErrores().get(contE));
                    xmlSalida.append("</ERROR>");
                }
            }
            xmlSalida.append("</ERRORES>");
            xmlSalida.append("</FILA>");
            i++;
        }
        if (validaciones != null && !validaciones.isEmpty()) {
            xmlSalida.append("<VALIDACION>");
            String validacion = validaciones.get(0);
            if (validacion != null && !validacion.isEmpty()) {
                if (validacion.contains("/")) {
                    String[] array = validacion.split("/");
                    validacion = array[array.length - 1];
                }
            } else {
                validacion = MeLanbide35I18n.getInstance().getMensaje(codigoIdioma, "error.datosNoValidos");
            }
            xmlSalida.append(validacion);
            xmlSalida.append("</VALIDACION>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    public void eliminarSegPreparador(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<FilaSegPreparadoresVO> seguimientos = null;
        List<FilaInsPreparadoresVO> inserciones = null;
        String codigoOperacion = "0";
        String tipoSeg = "";
        int codIdioma = 1;
        String colectivo = "";
        String sexo = "";
        String filtrar = null;
        Integer idP = null;
        Integer idS = null;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {
        }
        try {
            tipoSeg = request.getParameter("tipoSeg");
            String idSegPrep = request.getParameter("idSegPrep");
            colectivo = request.getParameter("colectivo");
            sexo = request.getParameter("sexo");
            filtrar = request.getParameter("filtrar");
            if (idSegPrep == null || idSegPrep.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    String[] datos = idSegPrep.split("_");
                    idP = Integer.valueOf(datos[0]);
                    idS = Integer.valueOf(datos[1]);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null && idS != null) {
                    EcaSegPreparadoresVO seg = new EcaSegPreparadoresVO();
                    seg.setJusPreparadoresCod(idP);
                    seg.setSegPreparadoresCod(idS);
                    int result = MeLanbide35Manager.getInstance().eliminarSegPreparador(seg, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        if (codigoOperacion.equalsIgnoreCase("0")) {
            //cargarSeguimientos(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);

            try {
                EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                EcaJusPreparadoresVO prep = null;
                if (filtrar != null && filtrar.equalsIgnoreCase("1")) {
                    prep = new EcaJusPreparadoresVO();
                    prep.setJusPreparadoresCod(idP);
                    prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                }
                if (tipoSeg != null) {
                    if (tipoSeg.equalsIgnoreCase(ConstantesMeLanbide35.TIPO_SEGUIMIENTO_MELANBIDE35)) {
                        seguimientos = MeLanbide35Manager.getInstance().getListaSeguimientos(sol, prep, tipoSeg, colectivo, sexo, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        this.escribirListaSegPreparadoresRequest(codigoOperacion, seguimientos, null, codIdioma, response);
                    } else if (tipoSeg.equalsIgnoreCase(ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35)) {
                        inserciones = MeLanbide35Manager.getInstance().getListaInserciones(sol, prep, tipoSeg, colectivo, sexo, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        this.escribirListaInsPreparadoresRequest(codigoOperacion, inserciones, null, codIdioma, response);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder xmlSalida = new StringBuilder();
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
            } catch (IOException e) {
                e.printStackTrace();
            }//try-catch
        }
    }

    public String cargarNuevoProspectorJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String opcion = request.getParameter("opcion");
            if (opcion != null) {
                if (opcion.equalsIgnoreCase("nuevo")) {
                    //Cargar nuevo preparador
                    //Cargar nuevo preparador
                    String idProsS = request.getParameter("idProsOrigen");
                    if (idProsS != null) {
                        EcaJusProspectoresVO prosS = new EcaJusProspectoresVO();
                        prosS.setJusProspectoresCod(Integer.valueOf(idProsS));
                        prosS = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(prosS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if (prosS != null) {
                            request.setAttribute("prospectorOrigen", prosS);
                        }
                    }
                } else if (opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar")) {
                    //Cargar modificar preparador
                    String idPrep = request.getParameter("idProsModif");
                    if (idPrep != null) {
                        EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                        pros.setJusProspectoresCod(Integer.valueOf(idPrep));
                        pros = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if (pros != null) {
                            request.setAttribute("prospectorModif", pros);
                            EcaJusProspectoresVO sustituto = new EcaJusProspectoresVO();
                            sustituto = MeLanbide35Manager.getInstance().getProspectorJustificacionSustituto(pros.getJusProspectoresCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if (sustituto != null) {
                                request.setAttribute("sustitutopros", sustituto);
                            }
                        }

                        if (pros.getJusProspectorOrigen() != null && !pros.getJusProspectorOrigen().equals("")) {
                            EcaJusProspectoresVO prosS = new EcaJusProspectoresVO();
                            prosS.setJusProspectoresCod(pros.getJusProspectorOrigen());
                            prosS = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(prosS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if (prosS != null) {
                                request.setAttribute("prospectorOrigen", prosS);
                            }
                        }
                    }
                    if (opcion.equalsIgnoreCase("consultar")) {
                        request.setAttribute("consulta", true);
                    }
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide35/justificacion/nuevoProspectorJustificacion.jsp";
    }

    public void guardarProspectorJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaProspectorJustificacionVO> prospectores = new ArrayList<FilaProspectorJustificacionVO>();;

        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if (sol != null && sol.getSolicitudCod() != null) {

                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                //Recojo los parámetros
                String idPros = request.getParameter("idPros");
                String nif = request.getParameter("nif");
                String nomApel = request.getParameter("nomApel");
                String feIni = request.getParameter("feIni");
                String feFin = request.getParameter("feFin");
                String horasAnualesJC = request.getParameter("horasAnualesJC");
                String horasContrato = request.getParameter("horasContrato");
                String horasECA = request.getParameter("horasECA");
                String costesSSJor = request.getParameter("costesSSJor");
                String costesSSPorJor = request.getParameter("costesSSPorJor");
                String costesSSECA = request.getParameter("costesSSECA");
                String idProsOrigen = request.getParameter("idProsOrigen");

                EcaJusProspectoresVO prosValida = MeLanbide35Manager.getInstance().getProspectorJustificacionPorNIF(sol, nif, feIni != null && !feIni.isEmpty() ? format.parse(feIni) : null, adapt);
                boolean prepRepetido = false;
                if (prosValida != null && idPros != null) {
                    if (!idPros.equalsIgnoreCase(prosValida.getJusProspectoresCod().toString())) {
                        prepRepetido = true;
                    }
                }
                if (!prepRepetido) {

                    EcaJusProspectoresVO pros = null;
                    if (idPros != null && !idPros.isEmpty()) {
                        pros = new EcaJusProspectoresVO();
                        pros.setJusProspectoresCod(Integer.valueOf(idPros));
                        pros = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(pros, adapt);
                    } else {
                        pros = new EcaJusProspectoresVO();
                    }
                    if (pros == null) {
                        codigoOperacion = "3";
                    } else {
                        EcaSolProspectoresVO prosRelacion = null;
                        try {
                            prosRelacion = MeLanbide35Manager.getInstance().getProspectorSolicitudPorNIF(sol, nif, adapt);
                        } catch (Exception ex) {

                        }
                        if (prosRelacion != null) {
                            pros.setSolProspectoresCod(prosRelacion.getSolProspectoresCod());
                        } else {
                            pros.setSolProspectoresCod(null);
                        }

                        pros.setFecFin(feFin != null && !feFin.isEmpty() ? format.parse(feFin) : null);
                        pros.setFecIni(feIni != null && !feIni.isEmpty() ? format.parse(feIni) : null);
                        pros.setHorasCont(horasContrato != null && !horasContrato.isEmpty() ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                        pros.setHorasEca(horasECA != null && !horasECA.isEmpty() ? new BigDecimal(horasECA.replaceAll(",", "\\.")) : null);
                        pros.setHorasJC(horasAnualesJC != null && !horasAnualesJC.isEmpty() ? new BigDecimal(horasAnualesJC.replaceAll(",", "\\.")) : null);
                        pros.setImpSSECA(costesSSECA != null && !costesSSECA.isEmpty() ? new BigDecimal(costesSSECA.replaceAll(",", "\\.")) : null);
                        pros.setImpSSJC(costesSSJor != null && !costesSSJor.isEmpty() ? new BigDecimal(costesSSJor.replaceAll(",", "\\.")) : null);
                        pros.setImpSSJR(costesSSPorJor != null && !costesSSPorJor.isEmpty() ? new BigDecimal(costesSSPorJor.replaceAll(",", "\\.")) : null);
                        pros.setNif(nif != null ? nif.toUpperCase() : null);
                        pros.setNombre(nomApel != null ? nomApel.toUpperCase() : null);
                        pros.setSolicitud(sol.getSolicitudCod());

                        if (idProsOrigen != null && !idProsOrigen.isEmpty()) {
                            pros.setJusProspectorOrigen(Integer.valueOf(idProsOrigen));
                            //Si el codOrigen no es nulo, significa que el prospector que vamos a dar de alta es un sustituto por lo que hay
                            //que indicar el tipo de sustituto correspondiente
                            pros.setTipoSust(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_JUSTIFICACION);
                        }

                        EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente), adapt);

                        boolean soloFormato = pros.getJusProspectoresCod() != null;

                        EcaJusProspectoresVO origen = null;

                        if (pros.getJusProspectorOrigen() != null) {
                            origen = new EcaJusProspectoresVO();
                            origen.setJusProspectoresCod(pros.getJusProspectorOrigen());
                            origen = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(origen, adapt);
                        }

                        List<EcaJusProspectoresVO> sustitutos = MeLanbide35Manager.getInstance().getSustitutosProspectorJustificacion(pros.getJusProspectoresCod(), adapt);

                        List<String> validacion = MeLanbide35Utils.validarEcaJusProspectoresVO(pros, origen, sustitutos, config, sol.getExpEje(), codIdioma, soloFormato, false);
                        if (validacion == null || validacion.isEmpty()) {
                            MeLanbide35Manager.getInstance().guardarEcaJusProspectoresVO(pros, adapt);
                            prospectores = MeLanbide35Manager.getInstance().getListaProspectoresJustificacion(sol, adapt, codIdioma);
                        } else {
                            codigoOperacion = "4";
                        }
                    }
                } else {
                    codigoOperacion = "5";
                }
            } else {
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }

        this.escribirListaProspectoresJusRequest(codigoOperacion, prospectores, response);
    }

    private void escribirListaProspectoresJusRequest(String codigoOperacion, List<FilaProspectorJustificacionVO> prospectores, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        for (FilaProspectorJustificacionVO pros : prospectores) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getId());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ConstantesMeLanbide35.FALSO);
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NIF>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getNif());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_NIF));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NIF>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getNombreApel());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<FECHA_INICIO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getFechaInicio());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_INICIO>");
            xmlSalida.append("<FECHA_FIN>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getFechaFin());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_FIN>");
            xmlSalida.append("<HORAS_ANUALES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getHorasAnuales());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_HORAS_ANUALES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_ANUALES>");
            xmlSalida.append("<HORAS_CONTRATO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getHorasContrato());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_HORAS_CONTRATO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_CONTRATO>");
            xmlSalida.append("<HORAS_DEDICACION_ECA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getHorasDedicacionECA());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</HORAS_DEDICACION_ECA>");
            xmlSalida.append("<COSTES_SALARIALES_SS_JOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getCostesSalarialesSSJor());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_JOR>");
            xmlSalida.append("<COSTES_SALARIALES_SS_POR_JOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getCostesSalarialesSSPorJor());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_POR_JOR>");
            xmlSalida.append("<COSTES_SALARIALES_SS_ECA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getCostesSalarialesSSEca());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</COSTES_SALARIALES_SS_ECA>");
            xmlSalida.append("<VISITAS>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getVisitas());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_VISITAS));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</VISITAS>");
            xmlSalida.append("<VISITAS_IMP>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(pros.getVisitasImp());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_VISITAS_IMP));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</VISITAS_IMP>");
            xmlSalida.append("<ERRORES>");
            if (pros.getErrores() != null) {
                for (int contE = 0; contE < pros.getErrores().size(); contE++) {
                    xmlSalida.append("<ERROR>");
                    xmlSalida.append(pros.getErrores().get(contE));
                    xmlSalida.append("</ERROR>");
                }
            }
            xmlSalida.append("</ERRORES>");
            if (pros.esSustituto() != null) {
                xmlSalida.append("<ES_SUSTITUTO>");
                xmlSalida.append(pros.esSustituto() ? "1" : "0");
                xmlSalida.append("</ES_SUSTITUTO>");
            }
            xmlSalida.append("</FILA>");
            i++;
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    public void eliminarProspectorJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        try {
            String idPros = request.getParameter("idPros");
            Integer idP = null;
            if (idPros == null || idPros.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idP = Integer.valueOf(idPros);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null) {
                    EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                    pros.setJusProspectoresCod(idP);
                    int result = MeLanbide35Manager.getInstance().eliminarProspectorJustificacion(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        /*if(codigoOperacion.equalsIgnoreCase("0"))
        {
            this.getListaProspectoresJustificacion(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }//try-catch
        }*/
        int codIdioma = 1;
        List<FilaProspectorJustificacionVO> prospectores = null;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }

        try {
            prospectores = this.getListaProspectoresJustificacion(codOrganizacion, numExpediente, codIdioma);
        } catch (Exception ex) {

        }

        this.escribirListaProspectoresJusRequest(codigoOperacion, prospectores, response);
    }

    public void getListaProspectoresJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            try {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            } catch (Exception ex) {

            }

            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }

            List<FilaProspectorJustificacionVO> prospectores = null;

            try {
                prospectores = this.getListaProspectoresJustificacion(codOrganizacion, numExpediente, codIdioma);
            } catch (Exception ex) {

            }

            String codigoOperacion = "0";

            this.escribirListaProspectoresJusRequest(codigoOperacion, prospectores, response);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String cargarVisitas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "";
        try {
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            try {
                if (usuario != null) {
                    idiomaUsuario = usuario.getIdioma();
                }
            } catch (Exception ex) {
            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            String idprep = request.getParameter("idProspector");
            String opcion = request.getParameter("opcion");
            if (idprep != null) {
                url = "/jsp/extension/melanbide35/justificacion/visitas.jsp";
                EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                pros.setJusProspectoresCod(Integer.valueOf(idprep));

                pros = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(pros, adapt);
                if (pros != null) {
                    request.setAttribute("prospector", pros);
                    List<FilaVisProspectoresVO> listaVisitas = MeLanbide35Manager.getInstance().getListaVisitas(sol, pros, idiomaUsuario, adapt);
                    if (listaVisitas != null) {
                        request.setAttribute("listaVisitas", listaVisitas);

                    }
                }
            } else {
                url = "/jsp/extension/melanbide35/justificacion/visitas.jsp";
                List<FilaVisProspectoresVO> listaVisitas = MeLanbide35Manager.getInstance().getListaVisitas(sol, null, idiomaUsuario, adapt);
                if (listaVisitas != null) {
                    request.setAttribute("listaVisitas", listaVisitas);
                }
            }
            if (opcion.equalsIgnoreCase("consultar")) {
                request.setAttribute("consulta", true);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return url;
    }

    public String procesarExcelVisitasPros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codIdioma = 1;

        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }

        } catch (Exception ex) {

        }
        try {
            MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

            wrapper.getParameterMap();
            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile file = null;
            file = table.get("fichero_visitas_jus");
            byte[] data = file.getFileData();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            HSSFWorkbook workbook = new HSSFWorkbook(bais);

            HSSFSheet sheet = workbook.getSheetAt(0);

            int filaIni = sheet.getFirstRowNum();
            int filaFin = sheet.getLastRowNum();

            HSSFRow row = null;
            List<EcaVisProspectoresVO> filasImportar = new ArrayList<EcaVisProspectoresVO>();
            EcaVisProspectoresVO vis = null;
            String validacion = null;
            int i = 0;
            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            try {
                for (i = filaIni + 1; i <= filaFin; i++) {
                    row = sheet.getRow(i);
                    vis = (EcaVisProspectoresVO) MeLanbide35MappingUtils.getInstance().map(row, EcaVisProspectoresVO.class);
                    if (vis != null) {
                        EcaJusProspectoresVO pros = null;
                        if (vis.getFecVisita() != null) {
                            pros = MeLanbide35Manager.getInstance().getProspectorJustificacionPorNIF(sol, vis.getNifProspector(), vis.getFecVisita(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        }
                        if (pros != null) {
                            vis.setJusProspectoresCod(pros.getJusProspectoresCod());

                            filasImportar.add(vis);
                        } else {
                            //throw new ExcelRowMappingException("2");                            
                            ExcelRowMappingException erme = new ExcelRowMappingException("2");
                            erme.setMensaje(String.format(MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.NifProspectorNoEncontrado"), MeLanbide35Utils.fromColNumberToExcelColName(2), i + 1));
                            throw erme;
                        }
                    }
                }
                int filasImportadas = 0;
                filasImportadas = MeLanbide35Manager.getInstance().importarVisitas(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                if (filasImportadas < filasImportar.size()) {
                    request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                } else {
                    request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1") + " " + filasImportadas + " " + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                }
            } catch (ExcelRowMappingException erme) {
                String mensajeImportar = null;
                if (erme.getMensaje() == null || erme.getMensaje().isEmpty()) {
                    mensajeImportar = MeLanbide35Utils.crearMensajeDatoExcelNoValido(erme, i + 1, codIdioma);
                    if (erme.getCampo().equals("2")) {
                        mensajeImportar = mensajeImportar + " " + MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.NifProspectorNoEncontrado");
                    }
                } else {
                    mensajeImportar = erme.getMensaje();
                }
                request.getSession().setAttribute("mensajeImportar", mensajeImportar);
            }
        } catch (FilaSeguimientoPrepNoValidaException ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
        } catch (IOException ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
        } catch (Exception ex) {
            ex.printStackTrace();
            request.getSession().setAttribute("mensajeImportar", MeLanbide35I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        return "/jsp/extension/melanbide35/justificacion/recargarListaVisitas.jsp";
    }

    public String cargarNuevaVisitaProspector(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        int codIdioma = 1;

        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        if (usuario != null) {
            codIdioma = usuario.getIdioma();
        }
        try {
            String prospector = request.getParameter("prospector");

            String opcion = request.getParameter("opcion");
            if (opcion != null) {
                if (opcion.equalsIgnoreCase("nuevo")) {
                    //Cargar nuevo preparador
                } else if (opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar")) {
                    //Cargar modificar visita
                    String idVis = request.getParameter("idVisModificar");
                    if (idVis != null) {

                        String[] datos = idVis.split("_");

                        EcaVisProspectoresVO vis = new EcaVisProspectoresVO();
                        vis.setJusProspectoresCod(Integer.valueOf(datos[0]));
                        vis.setVisProspectoresCod(Integer.valueOf(datos[1]));
                        vis = MeLanbide35Manager.getInstance().getVisitaProspectorPorId(vis, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if (vis != null) {
                            request.setAttribute("visitaModif", vis);
                        }
                    }
                    if (opcion.equalsIgnoreCase("consultar")) {
                        request.setAttribute("consulta", true);
                    }

                }

                List<SelectItem> listaSector = MeLanbide35Manager.getInstance().getListaSectorActividad(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<SelectItem> listaProvincia = MeLanbide35Manager.getInstance().getListaProvincias(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<SelectItem> listaCumple = MeLanbide35Manager.getInstance().getListaCumpleLismi(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<SelectItem> listaResultado = MeLanbide35Manager.getInstance().getListaResultadoFinal(codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                request.setAttribute("lstSector", listaSector);
                request.setAttribute("lstProvincia", listaProvincia);
                request.setAttribute("lstCumpleLismi", listaCumple);
                request.setAttribute("lstResultado", listaResultado);

                request.setAttribute("prospector", prospector);
                if (prospector != null) {
                    EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                    pros.setJusProspectoresCod(Integer.valueOf(prospector));
                    pros = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    request.setAttribute("nifpros", pros.getNif());
                    log.debug("nifprospector:" + pros.getNif());
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide35/justificacion/nuevaVisitaProspector.jsp";
    }

    public void guardarVisitaProspector(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }

        String codigoOperacion = "0";
        List<FilaVisProspectoresVO> visitas = null;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String tiposeg = "0";
            String sexosegs = "";
            String colectivosegs = "";

            List<Integer> listaSector = MeLanbide35Manager.getInstance().getListaCodigosSectorActividad(adapt);
            List<Integer> listaProvincias = MeLanbide35Manager.getInstance().getListaCodigosProvincia(adapt);
            List<Integer> listaCumple = MeLanbide35Manager.getInstance().getListaCodigosCumple(adapt);
            List<Integer> listaResultado = MeLanbide35Manager.getInstance().getListaCodigosResultado(adapt);

            EcaJusProspectoresVO prosvis = null;
            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if (sol != null && sol.getSolicitudCod() != null) {

                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
                //Recojo los parámetros
                String idPros = request.getParameter("idPros");
                String idVis = request.getParameter("idVis");
                String cif = request.getParameter("cif");
                String empresa = request.getParameter("empresa");

                String sector = request.getParameter("sector");
                String direccion = request.getParameter("direccion");
                String cpostal = request.getParameter("cpostal");
                String localidad = request.getParameter("localidad");
                String provincia = request.getParameter("provincia");
                String personaContacto = request.getParameter("personaContacto");
                String puesto = request.getParameter("puesto");
                String email = request.getParameter("email");
                String telefono = request.getParameter("telefono");
                String numtrab = request.getParameter("numtrab");
                String numtrabdisc = request.getParameter("numtrabdisc");
                String fecVisita = request.getParameter("fecVisita");
                String cumple = request.getParameter("cumple");
                String resultado = request.getParameter("resultado");
                String observaciones = request.getParameter("observaciones");
                tiposeg = request.getParameter("tiposeg");
                String nifpros = request.getParameter("nifProspector");

                String idprosvis = request.getParameter("prosvis");
                if (idprosvis != null && !idprosvis.isEmpty()) {
                    prosvis = new EcaJusProspectoresVO();
                    prosvis.setJusProspectoresCod(Integer.valueOf(idprosvis));
                    prosvis = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(prosvis, adapt);
                }

                EcaVisProspectoresVO vis = null;
                boolean soloFormato = false;
                if (idPros != null && !idPros.isEmpty() && idVis != null && !idVis.isEmpty()) {
                    vis = new EcaVisProspectoresVO();
                    vis.setJusProspectoresCod(Integer.valueOf(idPros));
                    vis.setVisProspectoresCod(Integer.valueOf(idVis));
                    vis = MeLanbide35Manager.getInstance().getVisitaProspectorPorId(vis, adapt);
                    soloFormato = true;
                } else {
                    vis = new EcaVisProspectoresVO();
                    EcaJusProspectoresVO pros = MeLanbide35Manager.getInstance().getProspectorJustificacionPorNIF(sol, nifpros.toUpperCase(), format.parse(fecVisita), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                    if (pros != null) {
                        vis.setJusProspectoresCod(pros.getJusProspectoresCod());
                        //nuevo marzo 2014 laura
                        vis.setNifProspector(nifpros.toUpperCase());
                    }
                }
                if (vis == null) {
                    codigoOperacion = "3";
                } else {
                    if (vis.getJusProspectoresCod() != null) {
                        vis.setCif(cif != null ? cif.toUpperCase() : null);
                        vis.setEmpresa(empresa != null ? empresa.toUpperCase() : null);
                        vis.setSector(sector != null && !sector.isEmpty() ? Integer.valueOf(sector) : null);
                        vis.setDireccion(direccion != null ? direccion.toUpperCase() : null);
                        vis.setCpostal(cpostal != null ? cpostal : null);
                        vis.setLocalidad(localidad != null ? localidad.toUpperCase() : null);
                        vis.setProvincia(provincia != null && !provincia.isEmpty() ? Integer.valueOf(provincia) : null);
                        vis.setPersContacto(personaContacto != null ? personaContacto.toUpperCase() : null);
                        vis.setPuesto(puesto != null ? puesto.toUpperCase() : null);
                        vis.setMail(email != null ? email.toUpperCase() : null);
                        vis.setTelefono(telefono != null && !telefono.isEmpty() ? Integer.valueOf(telefono) : null);
                        vis.setNumTrab(numtrab != null && !numtrab.isEmpty() ? Integer.valueOf(numtrab) : null);
                        vis.setNumTrabDisc(numtrabdisc != null && !numtrabdisc.isEmpty() ? Integer.valueOf(numtrabdisc) : null);
                        vis.setCumpleLismi(cumple != null && !cumple.isEmpty() ? Integer.valueOf(cumple) : null);
                        vis.setResultadoFinal(resultado != null && !resultado.isEmpty() ? Integer.valueOf(resultado) : null);
                        vis.setFecVisita(fecVisita != null && !fecVisita.isEmpty() ? format.parse(fecVisita) : null);
                        vis.setObservaciones(observaciones != null && !observaciones.isEmpty() ? observaciones.toUpperCase() : null);

                        EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente), adapt);
                        List<String> validacion = MeLanbide35Utils.validarEcaVisProspectoresVO(sol.getNumExp(), vis, listaSector, listaProvincias, listaCumple, listaResultado, config, codIdioma, true);
                        if (validacion == null || validacion.isEmpty()) {
                            MeLanbide35Manager.getInstance().guardarEcaVisProspectoresVO(vis, adapt);
                        } else {
                            //if (vis.getJusProspectoresCod()!=null)
                            codigoOperacion = "4";

                        }
                    } else {
                        codigoOperacion = "5";
                    }
                }
            } else {
                codigoOperacion = "1";
            }

            visitas = MeLanbide35Manager.getInstance().getListaVisitas(sol, prosvis, codIdioma, adapt);

        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }

        this.escribirListaVisProspectoresRequest(codigoOperacion, visitas, response);
    }

    public void eliminarVisitaProspectorJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        List<FilaVisProspectoresVO> listaVisitas = new ArrayList<FilaVisProspectoresVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String idVis = request.getParameter("idVis");
            String filtrar = request.getParameter("filtrar");
            if (idVis == null || idVis.isEmpty()) {
                codigoOperacion = "3";
            } else {
                String[] datos = idVis.split("_");
                String idPros = datos[0];
                idVis = datos[1];
                Integer idP = null;
                Integer idV = null;
                try {
                    idP = Integer.valueOf(idPros);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                try {
                    idV = Integer.valueOf(idVis);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null && idV != null) {
                    EcaVisProspectoresVO vis = new EcaVisProspectoresVO();
                    vis.setJusProspectoresCod(idP);
                    vis.setVisProspectoresCod(idV);
                    int result = MeLanbide35Manager.getInstance().eliminarVisitaProspectorJustificacion(vis, adapt);
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                        if (sol != null) {
                            EcaJusProspectoresVO pros = null;
                            if (filtrar != null && filtrar.equalsIgnoreCase("1")) {
                                pros = new EcaJusProspectoresVO();
                                pros.setJusProspectoresCod(idP);
                                pros = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(pros, adapt);
                            }
                            listaVisitas = MeLanbide35Manager.getInstance().getListaVisitas(sol, pros, idV, adapt);
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        if (codigoOperacion.equalsIgnoreCase("0")) {
            this.escribirListaVisProspectoresRequest(codigoOperacion, listaVisitas, response);
        } else {
            StringBuilder xmlSalida = new StringBuilder();
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
            } catch (IOException e) {
                e.printStackTrace();
            }//try-catch
        }
    }

    public void getNumeroSeguimientosInsercionesPreparador(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        Map<String, Integer> valores = new HashMap<String, Integer>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String idPrep = request.getParameter("idPrep");
            if (idPrep == null || idPrep.isEmpty()) {
                codigoOperacion = "3";
            } else {
                Integer idP = null;
                try {
                    idP = Integer.valueOf(idPrep);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null) {
                    EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                    if (sol != null) {
                        EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                        prep.setJusPreparadoresCod(idP);
                        prep.setSolicitud(sol.getSolicitudCod());
                        prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prep, adapt);
                        if (prep != null) {
                            valores = MeLanbide35Manager.getInstance().getNumeroSeguimientosInsercionesPreparador(prep, adapt);
                        }
                    } else {
                        codigoOperacion = "3";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        Integer valor = null;
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<SEGUIMIENTOS>");
        valor = valores.get(ConstantesMeLanbide35.TIPO_SEGUIMIENTO_MELANBIDE35);
        xmlSalida.append(valor != null ? valor.toString() : "0");
        xmlSalida.append("</SEGUIMIENTOS>");
        xmlSalida.append("<INSERCIONES>");
        valor = valores.get(ConstantesMeLanbide35.TIPO_INSERCION_MELANBIDE35);
        xmlSalida.append(valor != null ? valor.toString() : "0");
        xmlSalida.append("</INSERCIONES>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    public void getNumeroVisitasProspector(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        Integer valor = 0;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String idPrep = request.getParameter("idPros");
            if (idPrep == null || idPrep.isEmpty()) {
                codigoOperacion = "3";
            } else {
                Integer idP = null;
                try {
                    idP = Integer.valueOf(idPrep);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null) {
                    EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                    if (sol != null) {
                        EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                        pros.setJusProspectoresCod(idP);
                        pros.setSolicitud(sol.getSolicitudCod());
                        pros = MeLanbide35Manager.getInstance().getProspectorJustificacionPorId(pros, adapt);
                        if (pros != null) {
                            valor = MeLanbide35Manager.getInstance().getNumeroVisitasProspector(pros, adapt);
                        }
                    } else {
                        codigoOperacion = "3";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<VISITAS>");
        xmlSalida.append(valor != null ? valor.toString() : "0");
        xmlSalida.append("</VISITAS>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    public void getDatosAnexosSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        DatosAnexosVO datosAnexos = null;
        //DatosAnexosVO datosCarga = null;
        EcaSolicitudVO solicitud = null;
        String codigoOperacion = "0";
        try {
            int codIdioma = 1;

            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {
            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            solicitud = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            datosAnexos = MeLanbide35Manager.getInstance().getDatosSolicitudAnexos(solicitud, adapt);

            try {
                List<FilaPreparadorSolicitudVO> preparadores = MeLanbide35Manager.getInstance().getListaPreparadoresSolicitud(solicitud, adapt, codIdioma);
                if (preparadores != null && !preparadores.isEmpty()) {
                    int numPreparadores = 0;
                    BigDecimal imp = new BigDecimal("0.00");
                    for (FilaPreparadorSolicitudVO prep : preparadores) {
                        if (prep.getErrores() == null || prep.getErrores().isEmpty()) {
                            numPreparadores++;
                            imp = imp.add(new BigDecimal(prep.getCostesSalarialesSS().replace(".", "").replace(",", ".")));
                        }
                    }
                    datosAnexos.setNumPreparadores(new BigDecimal(numPreparadores));
                    datosAnexos.setImportePreparadores(imp);
                }

                restarDatosPreparadoresConErrores(preparadores, datosAnexos, adapt);
            } catch (Exception ex) {

            }

            try {
                List<FilaProspectorSolicitudVO> prospectores = MeLanbide35Manager.getInstance().getListaProspectoresSolicitud(solicitud, adapt, codIdioma);
                if (prospectores != null && !prospectores.isEmpty()) {
                    int numProspectores = 0;
                    BigDecimal imp = new BigDecimal("0.00");
                    for (FilaProspectorSolicitudVO pros : prospectores) {
                        if (pros.getErrores() == null || pros.getErrores().isEmpty() || (pros.getErrores().size() == 1 && pros.getErrorCampo(11).equals("S"))) {
                            numProspectores++;
                            imp = imp.add(new BigDecimal(pros.getCoste().replace(".", "").replace(",", ".")));
                        }
                    }
                    datosAnexos.setNumProspectores(new BigDecimal(numProspectores));
                    datosAnexos.setImporteProspectores(imp);
                }
            } catch (Exception ex) {

            }

            //datosCarga = MeLanbide35Manager.getInstance().getDatosSolicitudCarga(solicitud, adapt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.escribirDatosAnexosRequest(codigoOperacion, datosAnexos, solicitud, response);
    }

    private void escribirDatosAnexosRequest(String codigoOperacion, DatosAnexosVO datosAnexos, EcaSolicitudVO solicitud, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<VALORES_ANEXOS>");
        xmlSalida.append("<C1H>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC1H() != null ? datosAnexos.getC1H().toString() : "");
        xmlSalida.append("</C1H>");
        xmlSalida.append("<C1M>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC1M() != null ? datosAnexos.getC1M().toString() : "");
        xmlSalida.append("</C1M>");
        xmlSalida.append("<C1T>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC1T() != null ? datosAnexos.getC1T().toString() : "");
        xmlSalida.append("</C1T>");
        xmlSalida.append("<C2H>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC2H() != null ? datosAnexos.getC2H().toString() : "");
        xmlSalida.append("</C2H>");
        xmlSalida.append("<C2M>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC2M() != null ? datosAnexos.getC2M().toString() : "");
        xmlSalida.append("</C2M>");
        xmlSalida.append("<C2T>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC2T() != null ? datosAnexos.getC2T().toString() : "");
        xmlSalida.append("</C2T>");
        xmlSalida.append("<C3H>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC3H() != null ? datosAnexos.getC3H().toString() : "");
        xmlSalida.append("</C3H>");
        xmlSalida.append("<C3M>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC3M() != null ? datosAnexos.getC3M().toString() : "");
        xmlSalida.append("</C3M>");
        xmlSalida.append("<C3T>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC3T() != null ? datosAnexos.getC3T().toString() : "");
        xmlSalida.append("</C3T>");
        xmlSalida.append("<C4H>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC4H() != null ? datosAnexos.getC4H().toString() : "");
        xmlSalida.append("</C4H>");
        xmlSalida.append("<C4M>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC4M() != null ? datosAnexos.getC4M().toString() : "");
        xmlSalida.append("</C4M>");
        xmlSalida.append("<C4T>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getC4T() != null ? datosAnexos.getC4T().toString() : "");
        xmlSalida.append("</C4T>");
        xmlSalida.append("<TOT_ACTUACIONES>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getSeguimientosAnt() != null ? datosAnexos.getSeguimientosAnt().toString() : "");
        xmlSalida.append("</TOT_ACTUACIONES>");
        xmlSalida.append("<PROSPECTORES_NUM>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getNumProspectores() != null ? datosAnexos.getNumProspectores().toString() : "");
        xmlSalida.append("</PROSPECTORES_NUM>");
        xmlSalida.append("<PROSPECTORES_SOL>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getImporteProspectores() != null ? datosAnexos.getImporteProspectores().toString() : "");
        xmlSalida.append("</PROSPECTORES_SOL>");
        xmlSalida.append("<PREPARADORES_NUM>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getNumPreparadores() != null ? datosAnexos.getNumPreparadores().toString() : "");
        xmlSalida.append("</PREPARADORES_NUM>");
        xmlSalida.append("<PREPARADORES_SOL>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getImportePreparadores() != null ? datosAnexos.getImportePreparadores().toString() : "");
        xmlSalida.append("</PREPARADORES_SOL>");
        xmlSalida.append("<GASTOS>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getGastos() != null ? datosAnexos.getGastos().toString() : "");
        xmlSalida.append("</GASTOS>");
        xmlSalida.append("<MAX_SUBV>");
        xmlSalida.append(datosAnexos != null && datosAnexos.getMaxSubvencionable() != null ? datosAnexos.getMaxSubvencionable().toString() : "");
        xmlSalida.append("</MAX_SUBV>");
        xmlSalida.append("</VALORES_ANEXOS>");

        xmlSalida.append("<VALORES_CONCEDIDO>");
        xmlSalida.append("<C1H>");
        xmlSalida.append(solicitud != null && solicitud.getInserC1HConc() != null ? solicitud.getInserC1HConc().toString() : "");
        xmlSalida.append("</C1H>");
        xmlSalida.append("<C1M>");
        xmlSalida.append(solicitud != null && solicitud.getInserC1MConc() != null ? solicitud.getInserC1MConc().toString() : "");
        xmlSalida.append("</C1M>");
        xmlSalida.append("<C1T>");
        xmlSalida.append(solicitud != null && solicitud.getInserC1HConc() != null && solicitud.getInserC1MConc() != null ? (solicitud.getInserC1HConc() + solicitud.getInserC1MConc()) : "");
        xmlSalida.append("</C1T>");
        xmlSalida.append("<C2H>");
        xmlSalida.append(solicitud != null && solicitud.getInserC2HConc() != null ? solicitud.getInserC2HConc().toString() : "");
        xmlSalida.append("</C2H>");
        xmlSalida.append("<C2M>");
        xmlSalida.append(solicitud != null && solicitud.getInserC2MConc() != null ? solicitud.getInserC2MConc().toString() : "");
        xmlSalida.append("</C2M>");
        xmlSalida.append("<C2T>");
        xmlSalida.append(solicitud != null && solicitud.getInserC2HConc() != null && solicitud.getInserC2MConc() != null ? (solicitud.getInserC2HConc() + solicitud.getInserC2MConc()) : "");
        xmlSalida.append("</C2T>");
        xmlSalida.append("<C3H>");
        xmlSalida.append(solicitud != null && solicitud.getInserC3HConc() != null ? solicitud.getInserC3HConc().toString() : "");
        xmlSalida.append("</C3H>");
        xmlSalida.append("<C3M>");
        xmlSalida.append(solicitud != null && solicitud.getInserC3MConc() != null ? solicitud.getInserC3MConc().toString() : "");
        xmlSalida.append("</C3M>");
        xmlSalida.append("<C3T>");
        xmlSalida.append(solicitud != null && solicitud.getInserC3HConc() != null && solicitud.getInserC3MConc() != null ? (solicitud.getInserC3HConc() + solicitud.getInserC3MConc()) : "");
        xmlSalida.append("</C3T>");
        xmlSalida.append("<C4H>");
        xmlSalida.append(solicitud != null && solicitud.getInserC4HConc() != null ? solicitud.getInserC4HConc().toString() : "");
        xmlSalida.append("</C4H>");
        xmlSalida.append("<C4M>");
        xmlSalida.append(solicitud != null && solicitud.getInserC4MConc() != null ? solicitud.getInserC4MConc().toString() : "");
        xmlSalida.append("</C4M>");
        xmlSalida.append("<C4T>");
        xmlSalida.append(solicitud != null && solicitud.getInserC4HConc() != null && solicitud.getInserC4MConc() != null ? (solicitud.getInserC4HConc() + solicitud.getInserC4MConc()) : "");
        xmlSalida.append("</C4T>");
        xmlSalida.append("<TOT_ACTUACIONES>");
        xmlSalida.append(solicitud != null && solicitud.getSegActuacionesConc() != null ? solicitud.getSegActuacionesConc().toPlainString() : "");
        xmlSalida.append("</TOT_ACTUACIONES>");
        xmlSalida.append("<PROSPECTORES_NUM>");
        xmlSalida.append(solicitud != null && solicitud.getProspectoresNumConc() != null ? solicitud.getProspectoresNumConc().toString() : "");
        xmlSalida.append("</PROSPECTORES_NUM>");
        xmlSalida.append("<PROSPECTORES_SOL>");
        xmlSalida.append(solicitud != null && solicitud.getProspectoresImpConc() != null ? solicitud.getProspectoresImpConc().toPlainString() : "");
        xmlSalida.append("</PROSPECTORES_SOL>");
        xmlSalida.append("<PREPARADORES_NUM>");
        xmlSalida.append(solicitud != null && solicitud.getPreparadoresNumConc() != null ? solicitud.getPreparadoresNumConc().toString() : "");
        xmlSalida.append("</PREPARADORES_NUM>");
        xmlSalida.append("<PREPARADORES_SOL>");
        xmlSalida.append(solicitud != null && solicitud.getPreparadoresImpConc() != null ? solicitud.getPreparadoresImpConc().toPlainString() : "");
        xmlSalida.append("</PREPARADORES_SOL>");
        xmlSalida.append("<GASTOS>");
        xmlSalida.append(solicitud != null && solicitud.getGastosConc() != null ? solicitud.getGastosConc().toPlainString() : "");
        xmlSalida.append("</GASTOS>");
        xmlSalida.append("</VALORES_CONCEDIDO>");

        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    private void escribirListaVisProspectoresRequest(String codigoOperacion, List<FilaVisProspectoresVO> visitas, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        for (FilaVisProspectoresVO vis : visitas) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getCodProspector()).append("_").append(vis.getIdvisita());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(ConstantesMeLanbide35.FALSO);
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<CIF>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getCif());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_CIF));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</CIF>");
            xmlSalida.append("<EMPRESA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getEmpresa());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_EMPRESA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</EMPRESA>");
            xmlSalida.append("<FECHA_VISITA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getFecVisita());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_FECVISITA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</FECHA_VISITA>");
            xmlSalida.append("<SECTOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getDescSector());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_SECTORACT));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</SECTOR>");
            xmlSalida.append("<DIRECCION>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getDireccion());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_DIRECCION));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</DIRECCION>");
            xmlSalida.append("<CPOSTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getCpostal());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_CPOSTAL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</CPOSTAL>");
            xmlSalida.append("<LOCALIDAD>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getLocalidad());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_LOCALIDAD));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</LOCALIDAD>");
            xmlSalida.append("<PROVINCIA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getDescProvincia());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_PROVINCIA));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</PROVINCIA>");
            xmlSalida.append("<PERS_CONTACTO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getPersContacto());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_PCONTACTO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</PERS_CONTACTO>");
            xmlSalida.append("<PUESTO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getPuesto());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_CARGO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</PUESTO>");
            xmlSalida.append("<EMAIL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getMail());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_EMAIL));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</EMAIL>");
            xmlSalida.append("<TELEFONO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getTelefono());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_TELEFONO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</TELEFONO>");
            xmlSalida.append("<NIF_PROSPECTOR>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getNifProspector());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NIF_PROSPECTOR>");
            xmlSalida.append("<NUMTRAB>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getNumTrab());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_NUMTRAB));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NUMTRAB>");
            xmlSalida.append("<NUMTRABDISC>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getNumTrabDisc());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</NUMTRABDISC>");
            xmlSalida.append("<CUMPLELISMI>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getDescCumpleLismi());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</CUMPLELISMI>");
            xmlSalida.append("<RESULTADO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getDescResultadoFinal());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_RESULTADO));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</RESULTADO>");
            xmlSalida.append("<OBSERVACIONES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(vis.getObservaciones());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("<ERROR>");
            xmlSalida.append(vis.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_OBSERVACIONES));
            xmlSalida.append("</ERROR>");
            xmlSalida.append("</OBSERVACIONES>");
            xmlSalida.append("<ERRORES>");
            if (vis.getErrores() != null) {
                for (int contE = 0; contE < vis.getErrores().size(); contE++) {
                    xmlSalida.append("<ERROR>");
                    xmlSalida.append(vis.getErrores().get(contE));
                    xmlSalida.append("</ERROR>");
                }
            }
            xmlSalida.append("</ERRORES>");
            xmlSalida.append("</FILA>");
            i++;
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

    private List<String> hayNifsRepetidos(HSSFSheet sheet, int colNumber) {
        //TODO: implementar --> Busca nifs repetidos en la columna indicada. En caso de que los haya, devuelve true y se mostrará un mensaje de aviso.

        int filaIni = sheet.getFirstRowNum();
        int filaFin = sheet.getLastRowNum();

        HSSFRow row = null;
        List<String> nifs = new ArrayList<String>();
        List<String> nifsRepetidos = new ArrayList<String>();
        HSSFCell cell = null;
        String value = null;
        try {
            for (int i = filaIni + 1; i < filaFin; i++) {
                row = sheet.getRow(i);
                cell = row.getCell(colNumber);
                if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    try {
                        value = cell.getStringCellValue();
                        if (nifs.contains(value)) {
                            nifsRepetidos.add(value);
                        } else {
                            nifs.add(value);
                        }
                    } catch (Exception ex) {

                    }
                }
            }
        } catch (Exception ex) {

        }
        return nifsRepetidos;
    }

    private List<String> haySustitutosDeSustitutos(HSSFSheet sheet, int colNumber1, int colNumber2) {
        //TODO: implementar --> Busca nifs repetidos en la columna indicada. En caso de que los haya, devuelve true y se mostrará un mensaje de aviso.

        int filaIni = sheet.getFirstRowNum();
        int filaFin = sheet.getLastRowNum();

        HSSFRow row = null;
        List<String> retList = new ArrayList<String>();
        List<String> sustitutos = new ArrayList<String>();
        List<String> sustituidos = new ArrayList<String>();
        HSSFCell cell = null;
        HSSFCell cellSustituido = null;
        String nif = null;
        String nifSustituido = null;
        try {
            for (int i = filaIni + 1; i < filaFin; i++) {
                row = sheet.getRow(i);
                cell = row.getCell(colNumber1);
                cellSustituido = row.getCell(colNumber2);
                //if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING && cellSustituido.getCellType() == HSSFCell.CELL_TYPE_STRING)
                //{
                try {
                    nif = cell.getStringCellValue();
                    nifSustituido = cellSustituido.getStringCellValue();

                    if (nifSustituido != null && !nifSustituido.isEmpty()) {
                        if (sustitutos.contains(nifSustituido)) {
                            retList.add(nifSustituido);
                        } else {
                            sustitutos.add(nif);
                            sustituidos.add(nifSustituido);
                        }
                    }
                } catch (Exception ex) {

                }
                //}
            }
        } catch (Exception ex) {

        }
        return retList;
    }

    public void copiarPreparadores(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<FilaPreparadorJustificacionVO> preparadores = null;
        String codigoOperacion = "0";
        int codIdioma = 1;

        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {
        }
        try {
            //EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            MeLanbide35Manager manager = MeLanbide35Manager.getInstance();
            if (manager.hayPreparadoresParaCopiar(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)))) {
                int result = manager.copiarPreparadores(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                }
            } else {
                codigoOperacion = "-1";
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        if (codigoOperacion.equalsIgnoreCase("0")) {
            try {
                EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (sol != null) {
                    preparadores = MeLanbide35Manager.getInstance().getListaPreparadoresJustificacion(sol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                } else {
                    preparadores = new ArrayList<FilaPreparadorJustificacionVO>();
                }
                this.escribirListaPreparadoresJusRequest(codigoOperacion, preparadores, null, codIdioma, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder xmlSalida = new StringBuilder();
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
            } catch (IOException e) {
                e.printStackTrace();
            }//try-catch
        }
    }

    public void copiarProspectores(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<FilaProspectorJustificacionVO> prospectores = null;
        String codigoOperacion = "0";
        int codIdioma = 1;

        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {
        }
        try {
            //EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            MeLanbide35Manager manager = MeLanbide35Manager.getInstance();
            if (manager.hayProspectoresParaCopiar(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)))) {
                int result = MeLanbide35Manager.getInstance().copiarProspectores(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                }
            } else {
                codigoOperacion = "-1";
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        if (codigoOperacion.equalsIgnoreCase("0")) {
            try {
                EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (sol != null) {
                    prospectores = MeLanbide35Manager.getInstance().getListaProspectoresJustificacion(sol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                } else {
                    prospectores = new ArrayList<FilaProspectorJustificacionVO>();
                }
                this.escribirListaProspectoresJusRequest(codigoOperacion, prospectores, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder xmlSalida = new StringBuilder();
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
            } catch (IOException e) {
                e.printStackTrace();
            }//try-catch
        }
    }

    public String compararDatosPrepSol(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "";
        try {
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            try {
                if (usuario != null) {
                    idiomaUsuario = usuario.getIdioma();
                }
            } catch (Exception ex) {
            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            String idprep = request.getParameter("idPrep");
            url = "/jsp/extension/melanbide35/solicitud/comparacionPreparadorSolicitud.jsp";
            if (idprep != null) {
                EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                prep.setSolPreparadoresCod(Integer.valueOf(idprep));

                prep = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorId(prep, adapt);
                if (prep != null) {
                    request.setAttribute("preparador", prep);
                }
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return url;
    }

    public String compararDatosProsSol(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String url = "";
        try {
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            try {
                if (usuario != null) {
                    idiomaUsuario = usuario.getIdioma();
                }
            } catch (Exception ex) {
            }

            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            String idpros = request.getParameter("idPros");
            if (idpros != null) {
                url = "/jsp/extension/melanbide35/solicitud/comparacionProspectorSolicitud.jsp";
                EcaSolProspectoresVO pros = new EcaSolProspectoresVO();
                pros.setSolProspectoresCod(Integer.valueOf(idpros));

                pros = MeLanbide35Manager.getInstance().getProspectorSolicitudPorId(pros, adapt);
                if (pros != null) {
                    request.setAttribute("prospector", pros);
                }
            } else {
                url = "/jsp/extension/melanbide35/solicitud/comparacionProspectorSolicitud.jsp";

            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return url;
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

    private AdaptadorSQLBD getAdaptSQLBDflbgen() {
        if (log.isDebugEnabled()) {
            log.debug("getConnection  : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");

        String[] salida = null;

        if (log.isDebugEnabled()) {
            log.debug("getJndi =========> ");
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                if (jndiGenerico != null && gestor != null && !"".equals(jndiGenerico) && !"".equals(gestor)) {
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndiGenerico;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            } catch (Exception te) {
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    private void restarDatosPreparadoresConErrores(List<FilaPreparadorSolicitudVO> listaPreparadoresSolicitud, DatosAnexosVO datosAnexos, AdaptadorSQLBD adapt) {
        try {
            //Este for es para ir recorriendo todos los preparadores y mirar cuales tienen errores
            //En la pestaña de solicitud, columna de anexos, no deben contarse las inserciones, visitas, etc de los preparadores con errores
            //La parte de actuación subvencionable se hace en la propia jsp, pero esto se hace aqui porque es más rápido
            BigDecimal bd1 = null;
            BigDecimal bd2 = null;
            BigDecimal total = null;
            if (listaPreparadoresSolicitud != null) {
                for (FilaPreparadorSolicitudVO f : listaPreparadoresSolicitud) {
                    if (f.getErrores() != null && !f.getErrores().isEmpty()) {
                        EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                        prep.setSolPreparadoresCod(Integer.valueOf(f.getId()));
                        prep = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorId(prep, adapt);
                        if (prep != null) {
                            //Inserciones col. 1
                            if (prep.getInsC1H() != null || datosAnexos.getC1H() != null) {
                                bd1 = datosAnexos.getC1H() != null ? datosAnexos.getC1H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC1H() != null ? prep.getInsC1H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC1H(total);
                            }
                            if (prep.getInsC1M() != null || datosAnexos.getC1M() != null) {
                                bd1 = datosAnexos.getC1M() != null ? datosAnexos.getC1M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC1M() != null ? prep.getInsC1M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC1M(total);
                            }
                            if (prep.getInsC1() != null || datosAnexos.getC1T() != null) {
                                bd1 = datosAnexos.getC1T() != null ? datosAnexos.getC1T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC1() != null ? prep.getInsC1() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC1T(total);
                            }

                            //Inserciones col. 2
                            if (prep.getInsC2H() != null || datosAnexos.getC2H() != null) {
                                bd1 = datosAnexos.getC2H() != null ? datosAnexos.getC2H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC2H() != null ? prep.getInsC2H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC2H(total);
                            }
                            if (prep.getInsC2M() != null || datosAnexos.getC2M() != null) {
                                bd1 = datosAnexos.getC2M() != null ? datosAnexos.getC2M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC2M() != null ? prep.getInsC2M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC2M(total);
                            }
                            if (prep.getInsC2() != null || datosAnexos.getC2T() != null) {
                                bd1 = datosAnexos.getC2T() != null ? datosAnexos.getC2T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC2() != null ? prep.getInsC2() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC2T(total);
                            }

                            //Inserciones col. 3
                            if (prep.getInsC3H() != null || datosAnexos.getC3H() != null) {
                                bd1 = datosAnexos.getC3H() != null ? datosAnexos.getC3H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC3H() != null ? prep.getInsC3H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC3H(total);
                            }
                            if (prep.getInsC3M() != null || datosAnexos.getC3M() != null) {
                                bd1 = datosAnexos.getC3M() != null ? datosAnexos.getC3M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC3M() != null ? prep.getInsC3M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC3M(total);
                            }
                            if (prep.getInsC3() != null || datosAnexos.getC3T() != null) {
                                bd1 = datosAnexos.getC3T() != null ? datosAnexos.getC3T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC3() != null ? prep.getInsC3() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC3T(total);
                            }

                            //Inserciones col. 4
                            if (prep.getInsC4H() != null || datosAnexos.getC4H() != null) {
                                bd1 = datosAnexos.getC4H() != null ? datosAnexos.getC4H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC4H() != null ? prep.getInsC4H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC4H(total);
                            }
                            if (prep.getInsC4M() != null || datosAnexos.getC4M() != null) {
                                bd1 = datosAnexos.getC4M() != null ? datosAnexos.getC4M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC4M() != null ? prep.getInsC4M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC4M(total);
                            }
                            if (prep.getInsC4() != null || datosAnexos.getC4T() != null) {
                                bd1 = datosAnexos.getC4T() != null ? datosAnexos.getC4T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC4() != null ? prep.getInsC4() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC4T(total);
                            }

                            //Seguimientos
                            if (prep.getSegAnt() != null || datosAnexos.getSeguimientosAnt() != null) {
                                bd1 = datosAnexos.getSeguimientosAnt() != null ? datosAnexos.getSeguimientosAnt() : new BigDecimal("0.00");
                                bd2 = new BigDecimal(prep.getSegAnt() != null ? prep.getSegAnt().toString() : "0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setSeguimientosAnt(total);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {

        }
    }

    public String cargarDetalleProspectoresResumen(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        try {
            List<FilaEcaResProspectoresVO> listaDetalleProspectoresResumen = MeLanbide35Manager.getInstance().getListaDetalleProspectoresResumen(numExpediente, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaDetalleProspectoresResumen != null) {
                request.setAttribute("listaDetalleProspectoresResumen", listaDetalleProspectoresResumen);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "/jsp/extension/melanbide35/resumen/detalleProspectoresResumen.jsp";
    }

    public String cargarDetallePreparadoresResumen(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<FilaEcaResPreparadoresVO> listaDetallePreparadoresResumen = MeLanbide35Manager.getInstance().getListaDetallePreparadoresResumen(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaDetallePreparadoresResumen != null) {
                request.setAttribute("listaDetallePreparadoresResumen", listaDetallePreparadoresResumen);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "/jsp/extension/melanbide35/resumen/detallePreparadoresResumen.jsp";
    }

    public String cargarDetalleInsercionesPrep(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            String idPrep = request.getParameter("prepId");
            String ejercicio = String.valueOf(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente));
            EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
            prep.setJusPreparadoresCod(Integer.valueOf(idPrep));
            prep = MeLanbide35Manager.getInstance().getPreparadorJustificacionPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            EcaResDetalleInsercionesVO det = null;
            if (prep != null) {
                request.setAttribute("preparador", prep);
                det = MeLanbide35Manager.getInstance().cargarDetalleInsercionesPrep(ejercicio, prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (det != null) {
                    request.setAttribute("detalleInserciones", det);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "/jsp/extension/melanbide35/resumen/detalleInsercionesPrepResumen.jsp";
    }

    public void actualizarDatosResumen(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String mensaje = "";
        try {
            numExpediente = request.getParameter("numero");
            mensaje = MeLanbide35Manager.getInstance().actualizarDatosResumen(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (mensaje != null && !mensaje.isEmpty()) {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }

                    //mensaje = MeLanbide35I18n.getInstance().getMensaje(codIdioma, mensaje);
                } catch (Exception ex) {

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<MENSAJE>");
        //xmlSalida.append(mensaje != null ? mensaje : "");
        xmlSalida.append(mensaje != null && !mensaje.isEmpty() ? mensaje : ConstantesMeLanbide35.OK);
        xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch  
    }

    public void guardarDatosResumen(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try {
                String subvPriv = request.getParameter("subvPriv");
                String subvPub = request.getParameter("subvPub");
                String totSubv = request.getParameter("totSubv");

                EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                if (sol != null) {
                    EcaResumenVO res = MeLanbide35Manager.getInstance().getResumenSolicitud(sol, adapt);
                    if (res == null) {
                        res = new EcaResumenVO();
                        res.setSolicitud(sol.getSolicitudCod());
                    }
                    if (subvPriv != null && !subvPriv.isEmpty()) {
                        res.setEcaResSubPriv(new BigDecimal(subvPriv.replaceAll(",", "\\.")));
                    } else {
                        res.setEcaResSubPriv(null);
                    }
                    if (subvPub != null && !subvPub.isEmpty()) {
                        res.setEcaResSubPub(new BigDecimal(subvPub.replaceAll(",", "\\.")));
                    } else {
                        res.setEcaResSubPub(null);
                    }
                    if (totSubv != null && !totSubv.isEmpty()) {
                        res.setEcaResTotSubv(new BigDecimal(totSubv.replaceAll(",", "\\.")));
                    } else {
                        res.setEcaResTotSubv(null);
                    }
                    res = MeLanbide35Manager.getInstance().guardarResumenSolicitud(res, adapt);

                    if (res != null) {
                        codigoOperacion = "0";
                    } else {
                        codigoOperacion = "1";
                    }
                } else {
                    codigoOperacion = "3";
                }
            } catch (Exception ex) {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE35.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        respuestaCodOperacion(codigoOperacion, response);
    }

    public String cargarSustituirPreparadorSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        try {
            //Cargar modificar preparador
            String idPrep = request.getParameter("idPrepSustituir");
            if (idPrep != null) {
                EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                prep.setSolPreparadoresCod(Integer.valueOf(idPrep));
                prep = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (prep != null) {
                    log.debug(prep.getHorasCont() != null ? prep.getHorasCont().toString() : "");
                    request.setAttribute("preparadorOrigen", prep);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide35/solicitud/nuevoPreparadorSolicitud.jsp";
    }

    public String cargarSustituirProspectorSolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        try {
            //Cargar modificar preparador
            String idPros = request.getParameter("idProsSustituir");
            if (idPros != null) {
                EcaSolProspectoresVO pros = new EcaSolProspectoresVO();
                pros.setSolProspectoresCod(Integer.valueOf(idPros));
                pros = MeLanbide35Manager.getInstance().getProspectorSolicitudPorId(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (pros != null) {
                    log.debug(pros.getHorasCont() != null ? pros.getHorasCont().toString() : "");
                    request.setAttribute("prospectorOrigen", pros);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide35/solicitud/nuevoProspectorSolicitud.jsp";
    }

    private List<FilaProspectorJustificacionVO> getListaProspectoresJustificacion(int codOrganizacion, String numExpediente, int codIdioma) throws Exception {
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        MeLanbide35Manager meLanbide35Manager = MeLanbide35Manager.getInstance();
        EcaSolicitudVO sol = meLanbide35Manager.getDatosSolicitud(numExpediente, adapt);
        List<FilaProspectorJustificacionVO> prospectores = null;

        if (sol != null) {
            prospectores = MeLanbide35Manager.getInstance().getListaProspectoresJustificacion(sol, adapt, codIdioma);
        } else {
            prospectores = new ArrayList<FilaProspectorJustificacionVO>();
        }

        return prospectores;
    }

    private EcaResumenVO calcularDatosResumen(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        EcaResumenVO res = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EcaSolicitudVO sol = MeLanbide35Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(MeLanbide35Utils.getEjercicioDeExpediente(numExpediente), adapt);
            if (sol != null) {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {

                }
                Integer numPreparadoresSinErrores = null;
                Integer numProspectoresSinErrores = null;

                BigDecimal importePreparadores = null;
                BigDecimal importeProspectores = null;

                BigDecimal gastosGenerales = null;

                BigDecimal subvTot = null;

                try {
                    res = MeLanbide35Manager.getInstance().getResumenSolicitud(sol, adapt);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (res == null) {
                    res = new EcaResumenVO();
                }

                //SOLICITADO
                subvTot = null;

                res.setNumeroProspectores_solic(sol.getProspectoresNum());
                res.setImporteProspectores_solic(sol.getProspectoresImp());
                res.setNumeroPreparadores_solic(sol.getPreparadoresNum());
                res.setImportePreparadores_solic(sol.getPreparadoresImp());
                res.setGastosGenerales_solic(sol.getGastos());
                res.setSubvOrgPublicos_solic(sol.getSubPub());
                res.setSubvOrgPrivados_solic(sol.getSubPriv());
                res.setOtrasSubv(sol.getOtrasSub());

                if (sol.getProspectoresImp() != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(sol.getProspectoresImp());
                }
                if (sol.getPreparadoresImp() != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(sol.getPreparadoresImp());
                }
                if (sol.getGastos() != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(sol.getGastos());
                }
                if (sol.getSubPub() != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(sol.getSubPub());
                }
                if (sol.getSubPriv() != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(sol.getSubPriv());
                }

                if (subvTot != null && subvTot.compareTo(BigDecimal.ZERO) < 0) {
                    subvTot = new BigDecimal("0.00");
                }

                res.setTotSubv_solic(subvTot);
                res.setTotAprobado_solic(sol.getTotalAprobado());

                //CONCEDIDO
                numPreparadoresSinErrores = null;
                numProspectoresSinErrores = null;

                importePreparadores = null;
                importeProspectores = null;

                gastosGenerales = null;

                subvTot = null;

                res.setNumeroPreparadores_conc(sol.getPreparadoresNumConc());
                res.setImportePreparadores_conc(sol.getPreparadoresImpConc());
                res.setNumeroProspectores_conc(sol.getProspectoresNumConc());
                res.setImporteProspectores_conc(sol.getProspectoresImpConc());
                res.setGastosGenerales_conc(sol.getGastosConc());

                //AÑADIDOS CAMPOS GASTOS (MISMOS QUE SOLICITUD EN AÑO ANTERIOR A 2014)
                res.setSubvOrgPublicos_conc(sol.getSubPub());
                res.setSubvOrgPrivados_conc(sol.getSubPriv());
                if (subvTot == null) {
                    subvTot = new BigDecimal("0.00");
                }
                subvTot = subvTot.add(sol.getPreparadoresImpConc() != null ? sol.getPreparadoresImpConc() : new BigDecimal("0.00"));
                subvTot = subvTot.add(sol.getProspectoresImpConc() != null ? sol.getProspectoresImpConc() : new BigDecimal("0.00"));
                subvTot = subvTot.add(sol.getGastosConc() != null ? sol.getGastosConc() : new BigDecimal("0.00"));
                //SE AÑADE, ANTES SALÍA SIN RESTAR GASTOS DE SOLICITUD , PERO NO COINCIDIA CON PESTAÑA SOLICITUD
                subvTot = subvTot.subtract(sol.getSubPub() != null ? sol.getSubPub() : new BigDecimal("0.00"));
                subvTot = subvTot.subtract(sol.getSubPriv() != null ? sol.getSubPriv() : new BigDecimal("0.00"));
                res.setTotSubv_conc(subvTot);

                //JUSTIFICADO
                numPreparadoresSinErrores = null;
                numProspectoresSinErrores = null;

                importePreparadores = null;
                importeProspectores = null;

                gastosGenerales = null;

                subvTot = null;

                FilaProspectorJustificacionVO prosJ = null;
                List<FilaProspectorJustificacionVO> solProsListJ = MeLanbide35Manager.getInstance().getListaProspectoresJustificacion(sol, adapt, codIdioma);
                if (solProsListJ != null && !solProsListJ.isEmpty()) {
                    for (int i = 0; i < solProsListJ.size(); i++) {
                        prosJ = solProsListJ.get(i);
                        if ((prosJ.getErrores().isEmpty()) || (prosJ.getErrores().size() == 1 && prosJ.getErrorCampo(11).equals("S"))) {
                            if (importeProspectores == null) {
                                importeProspectores = new BigDecimal("0.00");
                            }
                            if (numProspectoresSinErrores == null) {
                                numProspectoresSinErrores = 0;
                            }

                            //Si es sustituto en la solicitud, no se cuenta para el numero pero si debe contabilizarse para importe
                            if (prosJ.getTipoSust() == null || !prosJ.getTipoSust().equals(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD)) {
                                numProspectoresSinErrores++;
                            }
                            if (prosJ.getVisitasImp() != null && !prosJ.getVisitasImp().equals("-")) {
                                importeProspectores = importeProspectores.add(new BigDecimal(prosJ.getVisitasImp().replace(".", "").replace(",", ".")));
                            }
                        }
                    }
                }

                res.setNumeroProspectores_justif(numProspectoresSinErrores);
                res.setImporteProspectores_justif(importeProspectores);

                FilaPreparadorJustificacionVO prepJ = null;
                List<FilaPreparadorJustificacionVO> solPrepListJ = MeLanbide35Manager.getInstance().getListaPreparadoresJustificacion(sol, adapt, codIdioma);
                if (solPrepListJ != null && !solPrepListJ.isEmpty()) {
                    for (int i = 0; i < solPrepListJ.size(); i++) {
                        prepJ = solPrepListJ.get(i);
                        if (prepJ.getErrores().isEmpty()) {
                            if (importePreparadores == null) {
                                importePreparadores = new BigDecimal("0.00");
                            }
                            if (numPreparadoresSinErrores == null) {
                                numPreparadoresSinErrores = 0;
                            }

                            //Si es sustituto en la solicitud, no se cuenta para el numero pero si debe contabilizarse para importe
                            if (prepJ.getTipoSust() == null || !prepJ.getTipoSust().equals(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD)) {
                                numPreparadoresSinErrores++;
                            }
                            if (prepJ.getCostesSalarialesSS() != null && !prepJ.getCostesSalarialesSS().equals("-")) {
                                // numero= new BigDecimal(numformateado.replace(".", "").replace(",", "."));
                                importePreparadores = importePreparadores.add(new BigDecimal(prepJ.getCostesSalarialesSS().replace(".", "").replace(",", ".")));
                            }
                        }
                    }
                }

                res.setNumeroPreparadores_justif(numPreparadoresSinErrores);
                res.setImportePreparadores_justif(importePreparadores);

                if (gastosGenerales == null) {
                    gastosGenerales = new BigDecimal("0.00");
                }

                if (importeProspectores != null) {
                    gastosGenerales = gastosGenerales.add(importeProspectores);
                }

                if (importePreparadores != null) {
                    gastosGenerales = gastosGenerales.add(importePreparadores);
                }

                if (gastosGenerales != null) {
                    gastosGenerales = gastosGenerales.multiply(config != null && config.getPoGastos() != null ? new BigDecimal(config.getPoGastos().toString()) : new BigDecimal("1"));
                    res.setGastosGenerales_justif(gastosGenerales);
                }

                res.setSubvOrgPrivados_justif(res.getEcaResSubPriv() != null ? res.getEcaResSubPriv() : res.getSubvOrgPrivados_solic());
                res.setSubvOrgPublicos_justif(res.getEcaResSubPub() != null ? res.getEcaResSubPub() : res.getSubvOrgPublicos_solic());

                if (res.getEcaResTotSubv() == null) {

                    if (importePreparadores != null) {
                        if (subvTot == null) {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.add(importePreparadores);
                    }
                    if (importeProspectores != null) {
                        if (subvTot == null) {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.add(importeProspectores);
                    }
                    if (gastosGenerales != null) {
                        if (subvTot == null) {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.add(gastosGenerales);
                    }
                    if (res.getSubvOrgPublicos_justif() != null) {
                        if (subvTot == null) {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.subtract(res.getSubvOrgPublicos_justif());
                    }
                    if (res.getSubvOrgPrivados_justif() != null) {
                        if (subvTot == null) {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.subtract(res.getSubvOrgPrivados_justif());
                    }

                    res.setTotSubv_justif(subvTot);
                } else {
                    res.setTotSubv_justif(res.getEcaResTotSubv());
                }

                if (res.getTotSubv_justif().compareTo(BigDecimal.ZERO) < 0) {
                    res.setTotSubv_justif(new BigDecimal("0.00"));
                }

                importePreparadores = null;
                importeProspectores = null;

                gastosGenerales = null;

                BigDecimal impPagPros = null;
                BigDecimal total = null;

                subvTot = null;

                List<FilaEcaResProspectoresVO> listaProspectoresResumen = MeLanbide35Manager.getInstance().getListaDetalleProspectoresResumen(numExpediente, codIdioma, adapt);
                List<FilaEcaResPreparadoresVO> listaPreparadoresResumen = MeLanbide35Manager.getInstance().getListaDetallePreparadoresResumen(numExpediente, adapt);

                FilaEcaResProspectoresVO filaProsJ = null;
                FilaEcaResPreparadoresVO filaPrepJ = null;

                int numPrep = 0;
                int numPros = 0;

                //El importe de prospectores es el SUMATORIO (MIN(COSTES SALARIALES + SS ECA, TOTAL))
                if (listaProspectoresResumen != null) {

                    for (int i = 0; i < listaProspectoresResumen.size(); i++) {
                        filaProsJ = listaProspectoresResumen.get(i);

                        if (filaProsJ.getTipoSust() == null || !filaProsJ.getTipoSust().equals(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD)) {
                            numPros++;
                        }

                        if (importeProspectores == null) {
                            importeProspectores = new BigDecimal("0.00");
                        }

                        if (filaProsJ.getImpPagar() != null && !filaProsJ.getImpPagar().equals("-")) {
                            impPagPros = new BigDecimal(filaProsJ.getImpPagar().replace(".", "").replace(",", "."));
                        } else {
                            impPagPros = new BigDecimal("0.00");
                        }
                        importeProspectores = importeProspectores.add(impPagPros);
                    }
                }

                res.setNumeroProspectores_pag(numPros);
                res.setImporteProspectores_pag(importeProspectores);

                if (listaPreparadoresResumen != null) {
                    for (int i = 0; i < listaPreparadoresResumen.size(); i++) {
                        filaPrepJ = listaPreparadoresResumen.get(i);

                        if (filaPrepJ.getTipoSust() == null || !filaPrepJ.getTipoSust().equals(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD)) {
                            numPrep++;
                        }

                        if (importePreparadores == null) {
                            importePreparadores = new BigDecimal("0.00");
                        }

                        if (filaPrepJ.getImpPagar() != null && !filaPrepJ.getImpPagar().equals("-")) {
                            importePreparadores = importePreparadores.add(new BigDecimal(filaPrepJ.getImpPagar().replace(".", "").replace(",", ".")));
                        }
                    }
                }

                res.setNumeroPreparadores_pag(numPrep);
                res.setImportePreparadores_pag(importePreparadores);

                if (gastosGenerales == null) {
                    gastosGenerales = new BigDecimal("0.00");
                }

                if (importeProspectores != null) {
                    gastosGenerales = gastosGenerales.add(importeProspectores);
                }

                if (importePreparadores != null) {
                    gastosGenerales = gastosGenerales.add(importePreparadores);
                }

                if (gastosGenerales != null) {
                    gastosGenerales = gastosGenerales.multiply(config != null && config.getPoGastos() != null ? new BigDecimal(config.getPoGastos().toString()) : new BigDecimal("1"));
                    res.setGastosGenerales_pag(gastosGenerales);
                }

                res.setSubvOrgPublicos_pag(res.getSubvOrgPublicos_justif());
                res.setSubvOrgPrivados_pag(res.getSubvOrgPrivados_justif());
                //res.setTotSubv_pag(res.getTotSubv_justif());
                if (importePreparadores != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(importePreparadores.setScale(2, RoundingMode.HALF_UP));
                }
                if (importeProspectores != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(importeProspectores.setScale(2, RoundingMode.HALF_UP));
                }
                if (gastosGenerales != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(gastosGenerales.setScale(2, RoundingMode.HALF_UP));
                }
                if (res.getSubvOrgPublicos_pag() != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(res.getSubvOrgPublicos_pag().setScale(2, RoundingMode.HALF_UP));
                }
                if (res.getSubvOrgPrivados_pag() != null) {
                    if (subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(res.getSubvOrgPrivados_pag().setScale(2, RoundingMode.HALF_UP));
                }

                if (subvTot != null && subvTot.compareTo(BigDecimal.ZERO) < 0) {
                    subvTot = new BigDecimal("0.00");
                }

                res.setTotSubv_pag(subvTot);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return res;
    }

    public void getDatosResumenEca(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        String codigoOperacion = "0";
        int codIdioma = 1;
        EcaResumenVO resumen = null;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {

        }
        try {
            numExpediente = request.getParameter("numero");
            resumen = this.calcularDatosResumen(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);

        } catch (Exception ex) {
            codigoOperacion = "1";
        }

        escribirDatosResumenRequest(codigoOperacion, resumen, codIdioma, response);
    }

    private void escribirDatosResumenRequest(String codigoOperacion, EcaResumenVO resumen, int codigoIdioma, HttpServletResponse response) {
        if (resumen == null) {
            resumen = new EcaResumenVO();
        }
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<VALORES_SOLICITADO>");
        xmlSalida.append("<NUM_PROS>");
        xmlSalida.append(resumen.getNumeroProspectores_solic() != null ? resumen.getNumeroProspectores_solic().toString() : "");
        xmlSalida.append("</NUM_PROS>");
        xmlSalida.append("<IMP_PROS>");
        xmlSalida.append(resumen.getImporteProspectores_solic() != null ? resumen.getImporteProspectores_solic().toString() : "");
        xmlSalida.append("</IMP_PROS>");
        xmlSalida.append("<NUM_PREP>");
        xmlSalida.append(resumen.getNumeroPreparadores_solic() != null ? resumen.getNumeroPreparadores_solic().toString() : "");
        xmlSalida.append("</NUM_PREP>");
        xmlSalida.append("<IMP_PREP>");
        xmlSalida.append(resumen.getImportePreparadores_solic() != null ? resumen.getImportePreparadores_solic().toString() : "");
        xmlSalida.append("</IMP_PREP>");
        xmlSalida.append("<GASTOS_GENERALES>");
        xmlSalida.append(resumen.getGastosGenerales_solic() != null ? resumen.getGastosGenerales_solic().toString() : "");
        xmlSalida.append("</GASTOS_GENERALES>");
        xmlSalida.append("<FL_OTRAS_SUBV>");
        xmlSalida.append(resumen.getOtrasSubv() != null && resumen.getOtrasSubv() == true ? resumen.getOtrasSubv().toString() : "");
        xmlSalida.append("</FL_OTRAS_SUBV>");
        xmlSalida.append("<OTRAS_SUBV_PUB>");
        xmlSalida.append(resumen.getSubvOrgPublicos_solic() != null ? resumen.getSubvOrgPublicos_solic().toString() : "");
        xmlSalida.append("</OTRAS_SUBV_PUB>");
        xmlSalida.append("<OTRAS_SUBV_PRIV>");
        xmlSalida.append(resumen.getSubvOrgPrivados_solic() != null ? resumen.getSubvOrgPrivados_solic().toString() : "");
        xmlSalida.append("</OTRAS_SUBV_PRIV>");
        xmlSalida.append("<TOT_SUBV>");
        xmlSalida.append(resumen.getTotSubv_solic() != null ? resumen.getTotSubv_solic().toString() : "");
        xmlSalida.append("</TOT_SUBV>");
        xmlSalida.append("<TOT_SUBV_APROBADA>");
        xmlSalida.append(resumen.getTotAprobado_solic() != null ? resumen.getTotAprobado_solic().toString() : "");
        xmlSalida.append("</TOT_SUBV_APROBADA>");
        xmlSalida.append("</VALORES_SOLICITADO>");

        xmlSalida.append("<VALORES_CONCEDIDO>");
        xmlSalida.append("<NUM_PROS>");
        xmlSalida.append(resumen.getNumeroProspectores_conc() != null ? resumen.getNumeroProspectores_conc().toString() : "");
        xmlSalida.append("</NUM_PROS>");
        xmlSalida.append("<IMP_PROS>");
        xmlSalida.append(resumen.getImporteProspectores_conc() != null ? resumen.getImporteProspectores_conc().toString() : "");
        xmlSalida.append("</IMP_PROS>");
        xmlSalida.append("<NUM_PREP>");
        xmlSalida.append(resumen.getNumeroPreparadores_conc() != null ? resumen.getNumeroPreparadores_conc().toString() : "");
        xmlSalida.append("</NUM_PREP>");
        xmlSalida.append("<IMP_PREP>");
        xmlSalida.append(resumen.getImportePreparadores_conc() != null ? resumen.getImportePreparadores_conc().toString() : "");
        xmlSalida.append("</IMP_PREP>");
        xmlSalida.append("<GASTOS_GENERALES>");
        xmlSalida.append(resumen.getGastosGenerales_conc() != null ? resumen.getGastosGenerales_conc().toString() : "");
        xmlSalida.append("</GASTOS_GENERALES>");
        xmlSalida.append("<TOT_SUBV>");
        xmlSalida.append(resumen.getTotSubv_conc() != null ? resumen.getTotSubv_conc().toString() : "");
        xmlSalida.append("</TOT_SUBV>");
        xmlSalida.append("</VALORES_CONCEDIDO>");

        xmlSalida.append("<VALORES_JUSTIFICADO>");
        xmlSalida.append("<NUM_PROS>");
        xmlSalida.append(resumen.getNumeroProspectores_justif() != null ? resumen.getNumeroProspectores_justif().toString() : "");
        xmlSalida.append("</NUM_PROS>");
        xmlSalida.append("<IMP_PROS>");
        xmlSalida.append(resumen.getImporteProspectores_justif() != null ? resumen.getImporteProspectores_justif().toString() : "");
        xmlSalida.append("</IMP_PROS>");
        xmlSalida.append("<NUM_PREP>");
        xmlSalida.append(resumen.getNumeroPreparadores_justif() != null ? resumen.getNumeroPreparadores_justif().toString() : "");
        xmlSalida.append("</NUM_PREP>");
        xmlSalida.append("<IMP_PREP>");
        xmlSalida.append(resumen.getImportePreparadores_justif() != null ? resumen.getImportePreparadores_justif().toString() : "");
        xmlSalida.append("</IMP_PREP>");
        xmlSalida.append("<GASTOS_GENERALES>");
        xmlSalida.append(resumen.getGastosGenerales_justif() != null ? resumen.getGastosGenerales_justif().toString() : "");
        xmlSalida.append("</GASTOS_GENERALES>");
        xmlSalida.append("<OTRAS_SUBV_PUB>");
        xmlSalida.append(resumen.getSubvOrgPublicos_justif() != null ? resumen.getSubvOrgPublicos_justif().toString() : "");
        xmlSalida.append("</OTRAS_SUBV_PUB>");
        xmlSalida.append("<OTRAS_SUBV_PRIV>");
        xmlSalida.append(resumen.getSubvOrgPrivados_justif() != null ? resumen.getSubvOrgPrivados_justif().toString() : "");
        xmlSalida.append("</OTRAS_SUBV_PRIV>");
        xmlSalida.append("<TOT_SUBV>");
        xmlSalida.append(resumen.getTotSubv_justif() != null ? resumen.getTotSubv_justif().toString() : "");
        xmlSalida.append("</TOT_SUBV>");
        xmlSalida.append("</VALORES_JUSTIFICADO>");

        xmlSalida.append("<VALORES_PAGAR>");
        xmlSalida.append("<NUM_PROS>");
        xmlSalida.append(resumen.getNumeroProspectores_pag() != null ? resumen.getNumeroProspectores_pag().toString() : "");
        xmlSalida.append("</NUM_PROS>");
        xmlSalida.append("<IMP_PROS>");
        xmlSalida.append(resumen.getImporteProspectores_pag() != null ? resumen.getImporteProspectores_pag().toString() : "");
        xmlSalida.append("</IMP_PROS>");
        xmlSalida.append("<NUM_PREP>");
        xmlSalida.append(resumen.getNumeroPreparadores_pag() != null ? resumen.getNumeroPreparadores_pag().toString() : "");
        xmlSalida.append("</NUM_PREP>");
        xmlSalida.append("<IMP_PREP>");
        xmlSalida.append(resumen.getImportePreparadores_pag() != null ? resumen.getImportePreparadores_pag().toString() : "");
        xmlSalida.append("</IMP_PREP>");
        xmlSalida.append("<GASTOS_GENERALES>");
        xmlSalida.append(resumen.getGastosGenerales_pag() != null ? resumen.getGastosGenerales_pag().toString() : "");
        xmlSalida.append("</GASTOS_GENERALES>");
        xmlSalida.append("<OTRAS_SUBV_PUB>");
        xmlSalida.append(resumen.getSubvOrgPublicos_pag() != null ? resumen.getSubvOrgPublicos_pag().toString() : "");
        xmlSalida.append("</OTRAS_SUBV_PUB>");
        xmlSalida.append("<OTRAS_SUBV_PRIV>");
        xmlSalida.append(resumen.getSubvOrgPrivados_pag() != null ? resumen.getSubvOrgPrivados_pag().toString() : "");
        xmlSalida.append("</OTRAS_SUBV_PRIV>");
        xmlSalida.append("<TOT_SUBV>");
        xmlSalida.append(resumen.getTotSubv_pag() != null ? resumen.getTotSubv_pag().toString() : "");
        xmlSalida.append("</TOT_SUBV>");
        xmlSalida.append("</VALORES_PAGAR>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }//try-catch
    }

////////////////////////////////////////////////
//////////////////    2023    //////////////////
////////////////////////////////////////////////
//////////////////    SOLICITUD     //////////////////
    //////////////////    INSERCION    //////////////////
    private String cargarSubpestanaInsercion_SolicitudECA23(EcaSolicitud23VO sol, AdaptadorSQLBD adapt, HttpServletRequest request) {
        log.debug("cargarSubpestanaInsercion_SolicitudECA23");
        if (sol != null) {
            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
                List<FilaInsercionesECA23VO> listaInsercionesSolicitud = MeLanbide35Manager.getInstance().getListaInsercionesSolicitudECA23(sol, adapt, codIdioma);
                if (listaInsercionesSolicitud != null) {
                    request.setAttribute("listaInsercionesSolicitud", listaInsercionesSolicitud);
                }
                EcaSolicitud23VO solicitudECA23 = MeLanbide35Manager.getInstance().getSolicitudECA23(sol.getNumeroExpediente(), adapt, codIdioma);

                solicitudECA23.setCuantiaSolicitada(solicitudECA23.getImporteSolicitadoSeguimiento().add(solicitudECA23.getTotalImporteInserciones()));

                MeLanbide35Manager.getInstance().actualizaCuantiaSolic(sol, adapt);
                if (solicitudECA23 != null) {
                    request.setAttribute("SolicitudECA23", solicitudECA23);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "/jsp/extension/melanbide35/solicitud23/inserciones/insercion23.jsp";
    }

    public void recargarInsercionSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("recargarInsercionSolicitud23");
        EcaSolicitud23VO sol = new EcaSolicitud23VO();
        sol.setNumeroExpediente(numExpediente);
        if (sol != null) {
            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
                sol = MeLanbide35Manager.getInstance().getSolicitudECA23(sol.getNumeroExpediente(), getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                List<FilaInsercionesECA23VO> listaInsercionesSolicitud = MeLanbide35Manager.getInstance().getListaInsercionesSolicitudECA23(sol, getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                sol = MeLanbide35Manager.getInstance().getSolicitudECA23(sol.getNumeroExpediente(), getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                if (listaInsercionesSolicitud != null) {
                    escribirListaInsercionesECA23Request("0", listaInsercionesSolicitud, sol, codIdioma, response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String cargarNuevoInsercionSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("cargarNuevoInsercionSolicitud23");
        try {
            int codIdioma = 1;
            try {
                if (request.getParameter("idioma") != null) {
                    codIdioma = Integer.parseInt(request.getParameter("idioma"));
                }
            } catch (NumberFormatException ex) {
            }
            List<SelectItem> listaTipoEdadSexo = MeLanbide35Manager.getInstance().getListaDesplegable(getAdaptSQLBD(String.valueOf(codOrganizacion)), "TPSE", codIdioma);

            request.setAttribute("lstTipoSexo", listaTipoEdadSexo);

            List<SelectItem> listaTipoDiscapacidad = MeLanbide35Manager.getInstance().getListaDesplegable(getAdaptSQLBD(String.valueOf(codOrganizacion)), "TPDE", codIdioma);
            request.setAttribute("lstTipoDiscapacidad", listaTipoDiscapacidad);
            int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);

            Eca23ConfiguracionVO configuracion = obtenerImportePorDiscapacidadECA23(String.valueOf(anio), getAdaptSQLBD(String.valueOf(codOrganizacion)));
            request.setAttribute("configuracion", configuracion);

            String opcion = request.getParameter("opcion");
            if (opcion != null) {
                if (!opcion.equalsIgnoreCase("nuevo")) {
                    if ((opcion.equalsIgnoreCase("modificar")) || (opcion.equalsIgnoreCase("consultar"))) {
                        String idPrep = request.getParameter("idPrepModif");
                        if (idPrep != null) {
                            EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                            prep.setSolPreparadoresCod(Integer.valueOf(idPrep));
                            prep = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorId(prep, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if (prep != null) {
                                request.setAttribute("preparadorModif", prep);
                                EcaSolPreparadoresVO sustituto = MeLanbide35Manager.getInstance().getPreparadorSolicitudSustituto(prep.getSolPreparadoresCod(), getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if (sustituto != null) {
                                    request.setAttribute("sustituto", sustituto);
                                }
                                if (prep.getSolPreparadorOrigen() != null) {
                                    EcaSolPreparadoresVO origen = new EcaSolPreparadoresVO();
                                    origen.setSolPreparadoresCod(prep.getSolPreparadorOrigen());
                                    origen = MeLanbide35Manager.getInstance().getPreparadorSolicitudPorId(origen, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                    if (origen != null) {
                                        request.setAttribute("preparadorOrigen", origen);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return "/jsp/extension/melanbide35/solicitud23/inserciones/nuevaInsercionSolicitud23.jsp";
    }

    public void eliminarInsercionSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("eliminarInsercionSolicitud23");
        String codigoOperacion = "0";
        try {
            String idInser = request.getParameter("idInserc");
            Integer idP = null;
            if ((idInser == null) || (idInser.isEmpty())) {
                codigoOperacion = "3";
            } else {
                try {
                    idP = Integer.valueOf(idInser);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null) {
                    FilaInsercionesECA23VO inser = new FilaInsercionesECA23VO();
                    inser.setId(idP);
                    int result = MeLanbide35Manager.getInstance().eliminarInsercionSolicitud23(inser, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        respuestaCodOperacion(codigoOperacion, response);
    }

    public void guardarInsercionSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("guardarInsercionSolicitud23");
        String codigoOperacion = "0";

        try {
            AdaptadorSQLBD adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));

            EcaSolicitud23VO sol = MeLanbide35Manager.getInstance().getDatosSolicitudECA23(numExpediente, adapt);
            if ((sol != null) && (sol.getNumeroExpediente() != null)) {

                String numeroExpediente = sol.getNumeroExpediente();
                String tipoDiscapacidad = request.getParameter("tipoDiscapacidad");
                String tipoSexoEdad = request.getParameter("tipoSexoEdad");
                String numeroPersonas = request.getParameter("numeroPersonas");
                String porcentajeTrabajo = request.getParameter("porcentajeTrabajo");
                String importeSolicitud = request.getParameter("importeSolicitud").replace(".", "").replace(",", ".");
                String importeCalUnAno = request.getParameter("importeSolicitudUnAno").replace(".", "").replace(",", ".");

                FilaInsercionesECA23VO insercion = new FilaInsercionesECA23VO();
                insercion.setNumeroExpediente(numeroExpediente);
                insercion.setTipoDiscapacidad(tipoDiscapacidad);
                insercion.setPorcentajeTrabajo(BigDecimal.valueOf(Double.parseDouble(porcentajeTrabajo)));
                insercion.setImporteCalculadoUnAnio(BigDecimal.valueOf(Double.parseDouble(importeCalUnAno)));
                insercion.setNumeroPersonas(Integer.valueOf(numeroPersonas));
                insercion.setTipoSexoEdad(tipoSexoEdad);
                insercion.setImporteSolicitado(BigDecimal.valueOf(Double.parseDouble(importeSolicitud)));

                int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);

                BigDecimal importe = calcularImporteInsercionECA23(insercion, String.valueOf(anio), adapt);

                MeLanbide35Manager.getInstance().guardarEca23SolInsercionVO(insercion, adapt);
            }
        } catch (Exception ex) {
            log.error("Error en guardarInsercionSolicitud23: " + ex.getStackTrace());
            codigoOperacion = "2";
        }
        respuestaCodOperacion(codigoOperacion, response);
    }

    public BigDecimal calcularImporteInsercionECA23(FilaInsercionesECA23VO insercion, String anio, AdaptadorSQLBD adapt) throws Exception {
        log.debug("calcularImporteInsercionECA23");
        Eca23ConfiguracionVO importesConfiguracion = obtenerImportePorDiscapacidadECA23(anio, adapt);
        BigDecimal importeColectivo = null;
        if (insercion.getTipoDiscapacidad().equalsIgnoreCase("A")) {
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("H")) {
                importeColectivo = importesConfiguracion.getcAh();
            }
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("H45")) {
                importeColectivo = importesConfiguracion.getcAh45();
            }
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("M")) {
                importeColectivo = importesConfiguracion.getcAm();
            }
        }
        if (insercion.getTipoDiscapacidad().equalsIgnoreCase("B")) {
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("H")) {
                importeColectivo = importesConfiguracion.getcBh();
            }
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("H45")) {
                importeColectivo = importesConfiguracion.getcBh45();
            }
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("M")) {
                importeColectivo = importesConfiguracion.getcBm();
            }
        }
        if (insercion.getTipoDiscapacidad().equalsIgnoreCase("C")) {
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("H")) {
                importeColectivo = importesConfiguracion.getcCh();
            }
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("H45")) {
                importeColectivo = importesConfiguracion.getcCh45();
            }
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("M")) {
                importeColectivo = importesConfiguracion.getcCm();
            }
        }
        if (insercion.getTipoDiscapacidad().equalsIgnoreCase("D")) {
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("H")) {
                importeColectivo = importesConfiguracion.getcDh();
            }
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("H45")) {
                importeColectivo = importesConfiguracion.getcDh45();
            }
            if (insercion.getTipoSexoEdad().equalsIgnoreCase("M")) {
                importeColectivo = importesConfiguracion.getcDm();
            }
        }
        if (importeColectivo == null) {
            log.error("Error no se ha detectado configuracion para el colectivo indicado " + insercion.getNumeroExpediente());
        }
        importeColectivo = importeColectivo.multiply(insercion.getPorcentajeTrabajo()).divide(BigDecimal.valueOf(100L));

        importeColectivo = importeColectivo.multiply(new BigDecimal(insercion.getNumeroPersonas()));
        if (importeColectivo.compareTo(insercion.getImporteCalculadoUnAnio()) != 0) {
            log.error("Error no es el mismo valor del javascript el total de la insercion. Javascript: " + insercion.getImporteCalculadoUnAnio() + " Java: " + importeColectivo);
        }
        return insercion.getImporteCalculadoUnAnio();
    }

    //////////////////    PROSPECTOR    //////////////////
    private String cargarSubpestanaProspector_SolicitudECA23(EcaSolicitud23VO sol, AdaptadorSQLBD adapt, HttpServletRequest request) {
        log.debug("cargarSubpestanaProspector_SolicitudECA23");
        if (sol != null) {
            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
                List<FilaProspectorECA23VO> listaProspectoresSolicitud = MeLanbide35Manager.getInstance().getListaProspectoresSolicitudECA23(sol, adapt, codIdioma);
                if (listaProspectoresSolicitud != null) {
                    request.setAttribute("listaProspectoresSolicitud", listaProspectoresSolicitud);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "/jsp/extension/melanbide35/solicitud23/prospectores/prospector23.jsp";
    }

    public void recargarProspectoresSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("recargarProspectoresSolicitud23");
        EcaSolicitud23VO sol = new EcaSolicitud23VO();
        sol.setNumeroExpediente(numExpediente);
        if (sol != null) {
            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
                List<FilaProspectorECA23VO> listaProspectoresSolicitud = MeLanbide35Manager.getInstance().getListaProspectoresSolicitudECA23(sol, getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                if (listaProspectoresSolicitud != null) {
                    escribirListaProspectoresECA23Request("0", listaProspectoresSolicitud, codIdioma, response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String cargarNuevoProspectorSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("cargarNuevoProspectorSolicitud23");
        try {
            String opcion = request.getParameter("opcion");
            if (opcion != null) {
                if (!opcion.equalsIgnoreCase("nuevo")) {
                    if ((opcion.equalsIgnoreCase("modificar")) || (!opcion.equalsIgnoreCase("consultar"))) {
                    }
                }
            }
        } catch (Exception ex) {
        }
        return "/jsp/extension/melanbide35/solicitud23/prospectores/nuevaProspectorSolicitud23.jsp";

    }

    public void eliminarProspectorSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("eliminarProspectorSolicitud23");
        String codigoOperacion = "0";
        try {
            String idInser = request.getParameter("id");
            Integer idP = null;
            if ((idInser == null) || (idInser.isEmpty())) {
                codigoOperacion = "3";
            } else {
                try {
                    idP = Integer.valueOf(idInser);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null) {
                    FilaProspectorECA23VO inser = new FilaProspectorECA23VO();
                    inser.setId(idP);
                    int result = MeLanbide35Manager.getInstance().eliminarProspectorSolicitud23(inser, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        respuestaCodOperacion(codigoOperacion, response);
    }

    public void guardarProspectorSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("guardarProspectorSolicitud23");
        String codigoOperacion = "0";

        try {
            AdaptadorSQLBD adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));

            EcaSolicitud23VO sol = MeLanbide35Manager.getInstance().getDatosSolicitudECA23(numExpediente, adapt);
            if ((sol != null) && (sol.getNumeroExpediente() != null)) {

                String numeroExpediente = sol.getNumeroExpediente();
                String nombre = request.getParameter("nombre");
                String dni = request.getParameter("dni");

                FilaProspectorECA23VO prospector = new FilaProspectorECA23VO();
                prospector.setNumeroExpediente(numeroExpediente);
                prospector.setNombre(nombre);
                prospector.setDni(dni);

                MeLanbide35Manager.getInstance().guardarEca23SolProspectorVO(prospector, adapt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }
        respuestaCodOperacion(codigoOperacion, response);

    }

    //////////////////    PREPARADOR    //////////////////
    private String cargarSubpestanaPreparador_SolicitudECA23(EcaSolicitud23VO sol, AdaptadorSQLBD adapt, HttpServletRequest request) {
        log.debug("cargarSubpestanaPreparador_SolicitudECA23");
        if (sol != null) {
            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
                List<FilaPreparadorECA23VO> listaPreparadorSolicitud = MeLanbide35Manager.getInstance().getListaPreparadoresSolicitudECA23(sol, adapt, codIdioma);
                if (listaPreparadorSolicitud != null) {
                    request.setAttribute("listaPreparadoresSolicitud", listaPreparadorSolicitud);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return "/jsp/extension/melanbide35/solicitud23/preparadores/preparador23.jsp";
    }

    public void recargarPreparadoresSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("recargarPreparadoresSolicitud23");
        EcaSolicitud23VO sol = new EcaSolicitud23VO();
        sol.setNumeroExpediente(numExpediente);
        if (sol != null) {
            try {
                int anio = MeLanbide35Utils.getEjercicioDeExpediente(sol.getNumeroExpediente());
                EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(anio, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                request.setAttribute("ecaConfiguracion", config);
            } catch (Exception ex) {
            }
            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
                List<FilaPreparadorECA23VO> listaPreparadoresSolicitud = MeLanbide35Manager.getInstance().getListaPreparadoresSolicitudECA23(sol, getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                sol = MeLanbide35Manager.getInstance().getSolicitudECA23(sol.getNumeroExpediente(), getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                if (listaPreparadoresSolicitud != null) {
                    escribirListaPreparadoresECA23Request("0", listaPreparadoresSolicitud, sol, codIdioma, response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String cargarNuevoPreparadorSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("cargarNuevoPreparadorSolicitud23");
        try {
            String opcion = request.getParameter("opcion");
            if (opcion != null) {
                if (!opcion.equalsIgnoreCase("nuevo")) {
                    if ((opcion.equalsIgnoreCase("modificar")) || (!opcion.equalsIgnoreCase("consultar"))) {
                    }
                }
            }
        } catch (Exception ex) {
        }
        return "/jsp/extension/melanbide35/solicitud23/preparadores//nuevaPreparadorSolicitud23.jsp";

    }

    public void eliminarPreparadorSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("eliminarPreparadorSolicitud23");
        String codigoOperacion = "0";
        try {
            String idInser = request.getParameter("id");
            Integer idP = null;
            if ((idInser == null) || (idInser.isEmpty())) {
                codigoOperacion = "3";
            } else {
                try {
                    idP = Integer.valueOf(idInser);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idP != null) {
                    FilaPreparadorECA23VO inser = new FilaPreparadorECA23VO();
                    inser.setId(idP);
                    int result = MeLanbide35Manager.getInstance().eliminarPreparadorSolicitud23(inser, getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        respuestaCodOperacion(codigoOperacion, response);
    }

    public void guardarPreparadorSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("guardarPreparadorSolicitud23");
        String codigoOperacion = "0";

        try {
            AdaptadorSQLBD adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));

            EcaSolicitud23VO sol = MeLanbide35Manager.getInstance().getDatosSolicitudECA23(numExpediente, adapt);
            if ((sol != null) && (sol.getNumeroExpediente() != null)) {

                String numeroExpediente = sol.getNumeroExpediente();
                String nombre = request.getParameter("nombre");
                String dni = request.getParameter("dni");
                String horasECA = request.getParameter("horaseca");

                FilaPreparadorECA23VO preparador = new FilaPreparadorECA23VO();
                preparador.setNumeroExpediente(numeroExpediente);
                preparador.setNombre(nombre);
                preparador.setDni(dni);
                preparador.setHorasECA(BigDecimal.valueOf(Double.parseDouble(horasECA)));

                MeLanbide35Manager.getInstance().guardarEca23SolPreparadorVO(preparador, adapt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }
        respuestaCodOperacion(codigoOperacion, response);
    }

    public void guardarHorasPreparadorSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("guardarHorasPreparadorSolicitud23");
        String codigoOperacion = "0";
        try {
            AdaptadorSQLBD adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));

            EcaSolicitud23VO sol = MeLanbide35Manager.getInstance().getDatosSolicitudECA23(numExpediente, adapt);
            if ((sol != null) && (sol.getNumeroExpediente() != null)) {

                String numeroExpediente = sol.getNumeroExpediente();
                String horas = request.getParameter("horas");
                String sumatoria = request.getParameter("sumatoria");

                EcaSolicitud23VO solicitud = new EcaSolicitud23VO();
                solicitud.setNumeroExpediente(numeroExpediente);
                solicitud.setHorasConvenio(BigDecimal.valueOf(Double.parseDouble(horas)));
                solicitud.setSumatoriaHorasTotales(BigDecimal.valueOf(Double.parseDouble(sumatoria)));

                MeLanbide35Manager.getInstance().guardarHorasPreparadorSolicitud23(solicitud, adapt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }
        respuestaCodOperacion(codigoOperacion, response);
    }

    public void recargarSolicitud23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("recargarSolicitud23");
        EcaSolicitud23VO sol = new EcaSolicitud23VO();
        sol.setNumeroExpediente(numExpediente);
        if (sol != null) {
            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
                EcaSolicitud23VO seguimientos = MeLanbide35Manager.getInstance().getSolicitudECA23(numExpediente, getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                if (seguimientos != null) {
                    escribirSolicitudECA23Request("0", seguimientos, codIdioma, response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //////////////////    JUSTIFICACION  23  //////////////////
    private String cargarSubPestanaInserciones_JustificacionECA23(String numExpediente, HttpServletRequest request, AdaptadorSQLBD adapt) {
        log.debug("cargarSubPestanaInserciones_JustificacionECA23");
        try {
            double total = 0.0;
            List<JusInsercionesECA23VO> listaInsercionesJustificacion = MeLanbide35Manager.getInstance().getJustInsercionesECA23(numExpediente, adapt);
            if (listaInsercionesJustificacion != null) {
                for (JusInsercionesECA23VO jusInsercion : listaInsercionesJustificacion) {
                    jusInsercion.setDescSexo(getDescripcionDesplegable(request, jusInsercion.getDescSexo()));
                    jusInsercion.setDescTipoDisc(getDescripcionDesplegable(request, jusInsercion.getDescTipoDisc()));
                    jusInsercion.setDescColectivo(getDescripcionDesplegable(request, jusInsercion.getDescColectivo()));
                    jusInsercion.setDescTipoContrato(getDescripcionDesplegable(request, jusInsercion.getDescTipoContrato()));
                    jusInsercion.setDescTipoEdadSexo(getDescripcionDesplegable(request, jusInsercion.getDescTipoEdadSexo()));
                    total += jusInsercion.getImporteInser();
                }
                request.setAttribute("listaInsercionesJustificacion", listaInsercionesJustificacion);
                request.setAttribute("totalInsercionesJus", String.valueOf(total));
                request.setAttribute("numInsercionesJus", listaInsercionesJustificacion.size());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide35/justificacion23/justificaInsercion23.jsp";
        }
        
    private String cargarSubPestanaPreparadores_JustificacionECA23(String numExpediente, HttpServletRequest request, AdaptadorSQLBD adapt) {
        log.debug("cargarSubPestanaPreparadores_JustificacionECA23");
        try {
            double total = 0.0;
            int numSeguimientosJus = 0;
            List<JusPreparadoresECA23VO> listaPreparadoresJustificacion = MeLanbide35Manager.getInstance().getJusPreparadoresECA23(numExpediente, adapt);
            if (listaPreparadoresJustificacion != null) {
                for (JusPreparadoresECA23VO jusPreparador : listaPreparadoresJustificacion) {
                    double impoPrep = MeLanbide35Manager.getInstance().getTotalJusSeguimientosPreparadorECA23(numExpediente, jusPreparador.getNifPreparador(), adapt);
                    if (jusPreparador.getImportePrep() == null || jusPreparador.getImportePrep().compareTo(0.0) == 0) {
                        jusPreparador.setImportePrep(impoPrep);
                    }
                    total += jusPreparador.getImportePrep();
                    int seguimientosPrep = MeLanbide35Manager.getInstance().getNumJusSeguimientosPreparadorECA23(numExpediente, jusPreparador.getNifPreparador(), adapt);
                    jusPreparador.setSegumientos(seguimientosPrep);
                    numSeguimientosJus += seguimientosPrep;
                }
                log.debug("imp - " + total);
                log.debug("seg - " + numSeguimientosJus);
                log.debug("prep - " + listaPreparadoresJustificacion.size());

                request.setAttribute("listaPreparadoresJustificacion", listaPreparadoresJustificacion);
                request.setAttribute("totalPreparadoresJus", String.valueOf(total));
                request.setAttribute("numPreparadoresJus", listaPreparadoresJustificacion.size());
                request.setAttribute("numSeguimientosJus", numSeguimientosJus);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide35/justificacion23/justificaPreparadores23.jsp";
    }

    public String cargarSeguimientosPreparador_JustificacionECA23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("cargarSeguimientosPreparador_JustificacionECA23");
        AdaptadorSQLBD adapt = null;
        String numExp = null;
        String dniPreparador = null;
        double total = 0.0;
        if (request.getParameter("numExp") != null) {
            numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
    }

            try {
            dniPreparador = request.getParameter("dniPreparador");
            int anio = MeLanbide35Utils.getEjercicioDeExpediente(numExp);
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            Eca23ConfiguracionVO configuracion = obtenerImportePorDiscapacidadECA23(String.valueOf(anio), adapt);
            request.setAttribute("configuracion", configuracion);
            request.setAttribute("dniPreparador", dniPreparador);
            List<JusSeguimientosECA23VO> listaSegPreparador = MeLanbide35Manager.getInstance().getJustSeguimientosPreparadorECA23(numExp, dniPreparador, adapt);
            if (listaSegPreparador != null) {
                for (JusSeguimientosECA23VO jusSegPrepECA23VO : listaSegPreparador) {
                    jusSegPrepECA23VO.setDescSexo(getDescripcionDesplegable(request, jusSegPrepECA23VO.getDescSexo()));
                    jusSegPrepECA23VO.setDescTipoDisc(getDescripcionDesplegable(request, jusSegPrepECA23VO.getDescTipoDisc()));
                    jusSegPrepECA23VO.setDescColectivo(getDescripcionDesplegable(request, jusSegPrepECA23VO.getDescColectivo()));
                    jusSegPrepECA23VO.setDescTipoContrato(getDescripcionDesplegable(request, jusSegPrepECA23VO.getDescTipoContrato()));
                    jusSegPrepECA23VO.setDescTipoEdadSexo(getDescripcionDesplegable(request, jusSegPrepECA23VO.getDescTipoEdadSexo()));
                    total += jusSegPrepECA23VO.getImporteSegui();
                    }
                request.setAttribute("listaSegPreparador", listaSegPreparador);
                request.setAttribute("numSeguimientosPreparador", listaSegPreparador.size());
                request.setAttribute("totalSegPreparadorJus", String.valueOf(total));

            }

                } catch (Exception ex) {
            ex.printStackTrace();
                }
        return "/jsp/extension/melanbide35/justificacion23/justificaSeguimientosPreparador23.jsp";

    }

    public String cargarMantenimientoJusInsercion23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("cargarMantenimientoJusInsercion23");
        String url = "";
        try {
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                request.setAttribute("nuevo", "0");
            } else {
                request.setAttribute("nuevo", "1");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        return url;

    }

    public Eca23ConfiguracionVO obtenerImportePorDiscapacidadECA23(String anio, AdaptadorSQLBD adapt) throws Exception {
        log.debug("obtenerImportePorDiscapacidadECA23");
        try {
            return MeLanbide35Manager.getInstance().getImportesECA23(anio, adapt);
        } catch (Exception e) {
            log.error("Se ha producido un error recuperando la configuracion de ECA 23", e);
            throw new Exception(e);
        }
    }

    public void actualizarImportesEca23(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("actualizarImportesEca23");
        String codigoOperacion = "0";
        BigDecimal total = new BigDecimal(0);
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
        } catch (Exception ex) {
        }
        try {
            AdaptadorSQLBD adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            EcaSolicitud23VO sol = MeLanbide35Manager.getInstance().getSolicitudECA23(numExpediente, adapt, codIdioma);
            if ((sol != null) && (sol.getNumeroExpediente() != null)) {
                sol.setCuantiaSolicitada(sol.getImporteSolicitadoSeguimiento().add(sol.getTotalImporteInserciones()));

                MeLanbide35Manager.getInstance().actualizaCuantiaSolic(sol, adapt);
                total = sol.getCuantiaSolicitada();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            codigoOperacion = "2";
        }
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<TOTAL>");
        xmlSalida.append(total);
        xmlSalida.append("</TOTAL>");
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void respuestaCodOperacion(String codigoOperacion, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escribirListaInsercionesECA23Request(String codigoOperacion, List<FilaInsercionesECA23VO> inserciones, EcaSolicitud23VO sol, int codigoIdioma, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        for (FilaInsercionesECA23VO inser : inserciones) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(inser.getId());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NUMEROEXPEDIENTE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(inser.getNumeroExpediente());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</NUMEROEXPEDIENTE>");
            xmlSalida.append("<TIPODISCAPACIDAD>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(inser.getTipoDiscapacidad());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</TIPODISCAPACIDAD>");
            xmlSalida.append("<TIPOSEXOEDAD>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(inser.getTipoSexoEdad());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</TIPOSEXOEDAD>");
            xmlSalida.append("<NUMEROPERSONAS>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(inser.getNumeroPersonas());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</NUMEROPERSONAS>");
            xmlSalida.append("<PORCENTAJETRABAJO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(inser.getPorcentajeTrabajo());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</PORCENTAJETRABAJO>");
            xmlSalida.append("<IMPORTECAL1ANO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(inser.getImporteCalculadoUnAnio());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</IMPORTECAL1ANO>");
            xmlSalida.append("<IMPORTESOLICITUD>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(inser.getImporteSolicitado());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</IMPORTESOLICITUD>");
            xmlSalida.append("<IMPORTETOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(sol.getTotalImporteInserciones());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</IMPORTETOTAL>");
            xmlSalida.append("<NUMEROPERSONASTOTAL>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(sol.getTotalNumeroPersonasInsercion());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</NUMEROPERSONASTOTAL>");
            xmlSalida.append("<TOTALIMPORTECAL1ANO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(sol.getTotalImporteCalculadoUnAnio());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</TOTALIMPORTECAL1ANO>");
            xmlSalida.append("</FILA>");
            i++;
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escribirListaProspectoresECA23Request(String codigoOperacion, List<FilaProspectorECA23VO> prospectores, int codigoIdioma, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        for (FilaProspectorECA23VO prospec : prospectores) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prospec.getId());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NUMEROEXPEDIENTE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prospec.getNumeroExpediente());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</NUMEROEXPEDIENTE>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prospec.getNombre());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<DNI>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prospec.getDni());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</DNI>");
            xmlSalida.append("</FILA>");
            i++;
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escribirSolicitudECA23Request(String codigoOperacion, EcaSolicitud23VO solicitud, int codigoIdioma, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        xmlSalida.append("<NUMEROEXPEDIENTE>");
        xmlSalida.append("<VALOR>");
        xmlSalida.append(solicitud.getNumeroExpediente());
        xmlSalida.append("</VALOR>");
        xmlSalida.append("</NUMEROEXPEDIENTE>");
        xmlSalida.append("<MUJERESSEG>");
        xmlSalida.append("<VALOR>");
        xmlSalida.append(solicitud.getMujeresSeguimiento());
        xmlSalida.append("</VALOR>");
        xmlSalida.append("</MUJERESSEG>");
        xmlSalida.append("<HOMBRESSEG>");
        xmlSalida.append("<VALOR>");
        xmlSalida.append(solicitud.getHombresSeguimiento());
        xmlSalida.append("</VALOR>");
        xmlSalida.append("</HOMBRESSEG>");
        xmlSalida.append("<TOTALNUMEROPERSONAS>");
        xmlSalida.append("<VALOR>");
        xmlSalida.append(solicitud.getTotalNumeroPersonas());
        xmlSalida.append("</VALOR>");
        xmlSalida.append("</TOTALNUMEROPERSONAS>");
        xmlSalida.append("<TOTALIMPORTECALCULADOUNANIO>");
        xmlSalida.append("<VALOR>");
        xmlSalida.append(solicitud.getTotalImporteCalculadoUnAnio());
        xmlSalida.append("</VALOR>");
        xmlSalida.append("</TOTALIMPORTECALCULADOUNANIO>");
        xmlSalida.append("<TOTALIMPORTESOLIC>");
        xmlSalida.append("<VALOR>");
        xmlSalida.append(solicitud.getTotalImporteInserciones());
        xmlSalida.append("</VALOR>");
        xmlSalida.append("</TOTALIMPORTESOLIC>");
        xmlSalida.append("<CUANTIASOLIC>");
        xmlSalida.append("<VALOR>");
        xmlSalida.append(solicitud.getCuantiaSolicitada());
        xmlSalida.append("</VALOR>");
        xmlSalida.append("</CUANTIASOLIC>");
        xmlSalida.append("<IMPORTESOLICSEG>");
        xmlSalida.append("<VALOR>");
        xmlSalida.append(solicitud.getImporteSolicitadoSeguimiento());
        xmlSalida.append("</VALOR>");
        xmlSalida.append("</IMPORTESOLICSEG>");
        i++;

        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escribirListaPreparadoresECA23Request(String codigoOperacion, List<FilaPreparadorECA23VO> preparadores, EcaSolicitud23VO sol, int codigoIdioma, HttpServletResponse response) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        int i = 0;
        BigDecimal horasTotales = new BigDecimal(0);
        for (FilaPreparadorECA23VO prep : preparadores) {
            horasTotales = horasTotales.add(prep.getHorasECA());
        }
        for (FilaPreparadorECA23VO prep : preparadores) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getId());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NUMEROEXPEDIENTE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getNumeroExpediente());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</NUMEROEXPEDIENTE>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getNombre());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<DNI>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getDni());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</DNI>");
            xmlSalida.append("<HORASECA>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(prep.getHorasECA());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</HORASECA>");
            xmlSalida.append("<HORASTOTALES>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(horasTotales);
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</HORASTOTALES>");
            xmlSalida.append("<HORASCONVENIO>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(sol.getHorasConvenio());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</HORASCONVENIO>");
            xmlSalida.append("<SUMATORIAHORAS>");
            xmlSalida.append("<VALOR>");
            xmlSalida.append(sol.getSumatoriaHorasTotales());
            xmlSalida.append("</VALOR>");
            xmlSalida.append("</SUMATORIAHORAS>");
            xmlSalida.append("</FILA>");
            i++;
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que extrae la descripción de los desplegables en el idioma del
     * usuario, en BBDD están en un campo separadas por Pipeline |
     *
     * @param request
     * @param desplegable
     * @return la lista en el idioma de usu
     */
    private List<DesplegableVO> traducirListaDesplegables(HttpServletRequest request, List<DesplegableVO> desplegable) {
        for (DesplegableVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().isEmpty()) {
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }
        return desplegable;
    }

    /**
     * Método que retorna el valor de un desplegable en el idiona del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraSeparadoraIdioma = ConfigurationParameter.getParameter(ConstantesMeLanbide35.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide35.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraIdioma) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide35.CODIGO_IDIOMA_EUSKERA) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                }
            } else {
                descripcion = " ";
            }
            return descripcion;
        } catch (Exception e) {
            return descripcion;
        }
    }

    /**
     * Método que recupera el Idioma de la request para la gestion de
     * Desplegables
     *
     * @param request
     * @return int idioma 1-castellano 4-euskera
     */
    private int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide35.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide35.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }
}
