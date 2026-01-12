package es.altia.flexia.integracion.moduloexterno.melanbide82;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide82.i18n.MeLanbide82I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide82.manager.MeLanbide82Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConstantesMeLanbide82;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.FilaContratacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.dto.SearchCVResponseDTO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.MELANBIDE67;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.zip.DataFormatException;
import javax.imageio.stream.FileCacheImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.util.FileBufferedOutputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
import es.altia.agora.technical.CamposFormulario;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.NuevosAtributosFilaContrVO;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.PersonaContratadaVO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.text.ParseException;
import java.sql.Date;
import java.util.Calendar;

public class MELANBIDE82 extends ModuloIntegracionExterno {

    private static final Logger log = LogManager.getLogger(MELANBIDE82.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static MELANBIDE67 melanbide67 = new MELANBIDE67();
    private static final int BUFFER_SIZE = 4096;
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

    @Override
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);

        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide82/melanbide82.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaContratacionVO> listaContrataciones = MeLanbide82Manager.getInstance().getListaContrataciones(numExpediente, codOrganizacion, adapt);
                if (!listaContrataciones.isEmpty()) {
                    listaContrataciones = obtenerDescripciones(listaContrataciones, request);
                    final NuevosAtributosFilaContrVO nuevosAtributosFilaContrVO = new NuevosAtributosFilaContrVO();
                    nuevosAtributosFilaContrVO.setCamposFormularioFechas(MeLanbide82Manager.getInstance().getValoresFechas(numExpediente, adapt));
                    nuevosAtributosFilaContrVO.setCamposFormularioFicheros(MeLanbide82Manager.getInstance().getFicheros(numExpediente, adapt));
                    iterListaContrataciones(listaContrataciones, nuevosAtributosFilaContrVO);
                    request.setAttribute("listaContrataciones", listaContrataciones);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos de contrataciones - MELANBIDE82 - cargarPantallaPrincipal", ex);
            }
        }
        return url;
    }

    public String cargarNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaContratacion - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide82/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;
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
            cargarDesplegablesGEL(codOrganizacion, request);

        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva contratación : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public String cargarModificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarContratacion - " + numExpediente);
        String nuevo = "0";
        String urlnuevaContratacion = "/jsp/extension/melanbide82/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
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
            // Recuperramos datos e Acceso a modificar y cargamos en el request
            if (id != null && !id.isEmpty()) {
                FilaContratacionVO datModif = MeLanbide82Manager.getInstance().getContratacionPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            //Cargamos los valores  de los desplegables
            cargarDesplegablesGEL(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public void crearNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevaContratacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaContratacionVO> listaContrataciones = new ArrayList<FilaContratacionVO>();
        FilaContratacionVO nuevaContratacion = new FilaContratacionVO();
        int tablas = 1;
        boolean tabla2 = false;
        boolean tabla3 = false;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String numExp = request.getParameter("expediente");
            String prioridad = request.getParameter("prioridad");
            String denomPuesto = request.getParameter("denomPuesto");
            String nivelCualificacion = request.getParameter("nivelCualificacion");
            String modContrato = request.getParameter("modContrato");
            String durContrato = request.getParameter("durContrato");
            String grupoCotiz = request.getParameter("grupoCotiz");
            String costeSalarial = request.getParameter("costeSalarial").replace(",", ".");
            String subvSolicitada = request.getParameter("subvSolicitada").replace(",", ".");

            //INICIO
            String municipioInicio = (request.getParameter("municipioInicio") != null ? request.getParameter("municipioInicio") : "");
            String dniNieInicio = (request.getParameter("dniNieInicio") != null ? request.getParameter("dniNieInicio") : "");
            String nombreInicio = (request.getParameter("nombreInicio") != null ? request.getParameter("nombreInicio") : "");
            String apellido1Inicio = (request.getParameter("apellido1Inicio") != null ? request.getParameter("apellido1Inicio") : "");
            String apellido2Inicio = (request.getParameter("apellido2Inicio") != null ? request.getParameter("apellido2Inicio") : "");
            String edadInicio = (request.getParameter("edadInicio") != null ? request.getParameter("edadInicio") : "");
            String sexoInicio = (request.getParameter("sexoInicio") != null ? request.getParameter("sexoInicio") : "");
            String grupoCotizInicio = (request.getParameter("grupoCotizInicio") != null ? request.getParameter("grupoCotizInicio") : "");
            String fechaNacimientoInicio = (request.getParameter("fechaNacimientoInicio") != null ? request.getParameter("fechaNacimientoInicio") : "");
            String nivelCualificacionInicio = (request.getParameter("nivelCualificacionInicio") != null ? request.getParameter("nivelCualificacionInicio") : "");
            String puestoTrabajoInicio = (request.getParameter("puestoTrabajoInicio") != null ? request.getParameter("puestoTrabajoInicio") : "");
            String nOfertaInicio = (request.getParameter("nOfertaInicio") != null ? request.getParameter("nOfertaInicio") : "");
            String retribucionBrutaInicio = (request.getParameter("retribucionBrutaInicio") != null ? request.getParameter("retribucionBrutaInicio").replace(",", ".") : "");
            String fechaInicioInicio = (request.getParameter("fechaInicioInicio") != null ? request.getParameter("fechaInicioInicio") : "");
            String durContratoInicio = (request.getParameter("durContratoInicio") != null ? request.getParameter("durContratoInicio") : "");
            //FIN
            String municipioFin = (request.getParameter("municipioFin") != null ? request.getParameter("municipioFin") : "");
            String dniNieFin = (request.getParameter("dniNieFin") != null ? request.getParameter("dniNieFin") : "");
            String nombreFin = (request.getParameter("nombreFin") != null ? request.getParameter("nombreFin") : "");
            String apellido1Fin = (request.getParameter("apellido1Fin") != null ? request.getParameter("apellido1Fin") : "");
            String apellido2Fin = (request.getParameter("apellido2Fin") != null ? request.getParameter("apellido2Fin") : "");
            String sexoFin = (request.getParameter("sexoFin") != null ? request.getParameter("sexoFin") : "");
            String grupoCotizFin = (request.getParameter("grupoCotizFin") != null ? request.getParameter("grupoCotizFin") : "");
            String fechaInicioFin = (request.getParameter("fechaInicioFin") != null ? request.getParameter("fechaInicioFin") : "");
            String fechaFinFin = (request.getParameter("fechaFinFin") != null ? request.getParameter("fechaFinFin") : "");
            String durContratoFin = (request.getParameter("durContratoFin") != null ? request.getParameter("durContratoFin") : "");
            String retribucionBrutaFin = (request.getParameter("retribucionBrutaFin") != null ? request.getParameter("retribucionBrutaFin").replace(",", ".") : "");
            String costeSalarialFin = (request.getParameter("costeSalarialFin") != null ? request.getParameter("costeSalarialFin").replace(",", ".") : "");
            String indemFinContratoFin = (request.getParameter("indemFinContratoFin") != null ? request.getParameter("indemFinContratoFin").replace(",", ".") : "");
            String costesSsFin = (request.getParameter("costesSsFin") != null ? request.getParameter("costesSsFin").replace(",", ".") : "");
            String costeTotalRealFin = (request.getParameter("costeTotalRealFin") != null ? request.getParameter("costeTotalRealFin").replace(",", ".") : "");
            String subvConcedidaFin = (request.getParameter("subvConcedidaFin") != null ? request.getParameter("subvConcedidaFin").replace(",", ".") : "");

            //FilaContratacionVO datModif = new FilaContratacionVO();
            nuevaContratacion.setNumExp(numExp);

            if (prioridad != null && !"".equals(prioridad)) {
                nuevaContratacion.setPrioridad(Integer.parseInt(prioridad));
            }
            nuevaContratacion.setDenomPuesto(denomPuesto);
            nuevaContratacion.setNivelCualificacion(nivelCualificacion);
            nuevaContratacion.setModContrato(modContrato);
            if (durContrato != null && !"".equals(durContrato)) {
                nuevaContratacion.setDurContrato(Integer.parseInt(durContrato));
            }
            nuevaContratacion.setGrupoCotiz(grupoCotiz);
            if (costeSalarial != null && !"".equals(costeSalarial)) {
                nuevaContratacion.setCostesalarial(Double.parseDouble(costeSalarial));
            }
            if (subvSolicitada != null && !"".equals(subvSolicitada)) {
                nuevaContratacion.setSubvsolicitada(Double.parseDouble(subvSolicitada));
            }

            //INICIO
            if (nombreInicio != null && !"".equalsIgnoreCase(nombreInicio)) {
                tabla2 = true;
                nuevaContratacion.setNombreInicio(nombreInicio);
            }
            if (apellido1Inicio != null && !"".equalsIgnoreCase(apellido1Inicio)) {
                tabla2 = true;
                nuevaContratacion.setApellido1Inicio(apellido1Inicio);
            }
            if (tabla2) {
                nuevaContratacion.setMunicipioInicio(municipioInicio);
                nuevaContratacion.setApellido2Inicio(apellido2Inicio);
                nuevaContratacion.setDninieInicio(dniNieInicio);
                if (fechaNacimientoInicio != null && !"".equalsIgnoreCase(fechaNacimientoInicio)) {
                    nuevaContratacion.setFechanacimientoInicio(new java.sql.Date(dateFormat.parse(fechaNacimientoInicio).getTime()));
                }
                nuevaContratacion.setSexoInicio(sexoInicio);
                nuevaContratacion.setNivelcualificacionInicio(nivelCualificacionInicio);
                nuevaContratacion.setPuestotrabajoInicio(puestoTrabajoInicio);
                nuevaContratacion.setNofertaInicio(nOfertaInicio);
                nuevaContratacion.setGrupocotizInicio(grupoCotizInicio);
                if (durContratoInicio != null && !"".equals(durContratoInicio)) {
                    nuevaContratacion.setDurcontratoInicio(Integer.parseInt(durContratoInicio));
                }
                if (fechaInicioInicio != null && !"".equalsIgnoreCase(fechaInicioInicio)) {
                    nuevaContratacion.setFechainicioInicio(new java.sql.Date(dateFormat.parse(fechaInicioInicio).getTime()));
                }
                if (edadInicio != null && !"".equals(edadInicio)) {
                    nuevaContratacion.setEdadInicio(Integer.parseInt(edadInicio));
                }
                if (retribucionBrutaInicio != null && !"".equals(retribucionBrutaInicio)) {
                    nuevaContratacion.setRetribucionbrutaInicio(Double.parseDouble(retribucionBrutaInicio));
                }
            }

            //FIN
            if (nombreFin != null && !"".equalsIgnoreCase(nombreFin)) {
                tabla3 = true;
                nuevaContratacion.setNombreFin(nombreFin);
            }
            if (apellido1Fin != null && !"".equalsIgnoreCase(apellido1Fin)) {
                tabla3 = true;
                nuevaContratacion.setApellido1Fin(apellido1Fin);
            }
            if (tabla3) {
                nuevaContratacion.setMunicipioFin(municipioFin);
                nuevaContratacion.setDninieFin(dniNieFin);
                nuevaContratacion.setApellido2Fin(apellido2Fin);
                nuevaContratacion.setSexoFin(sexoFin);
                nuevaContratacion.setGrupocotizFin(grupoCotizFin);
                if (fechaInicioFin != null && !"".equalsIgnoreCase(fechaInicioFin)) {
                    nuevaContratacion.setFechainicioFin(new java.sql.Date(dateFormat.parse(fechaInicioFin).getTime()));
                }
                if (fechaFinFin != null && !"".equalsIgnoreCase(fechaFinFin)) {
                    nuevaContratacion.setFechafinFin(new java.sql.Date(dateFormat.parse(fechaFinFin).getTime()));
                }
                if (durContratoFin != null && !"".equals(durContratoFin)) {
                    nuevaContratacion.setDurcontratoFin(Integer.parseInt(durContratoFin));
                }
                if (retribucionBrutaFin != null && !"".equals(retribucionBrutaFin)) {
                    nuevaContratacion.setRetribucionbrutaFin(Double.parseDouble(retribucionBrutaFin));
                }
                if (costeSalarialFin != null && !"".equals(costeSalarialFin)) {
                    nuevaContratacion.setCostesalarialFin(Double.parseDouble(costeSalarialFin));
                }
                if (indemFinContratoFin != null && !"".equals(indemFinContratoFin)) {
                    nuevaContratacion.setIndemfincontratoFin(Double.parseDouble(indemFinContratoFin));
                }
                if (costesSsFin != null && !"".equals(costesSsFin)) {
                    nuevaContratacion.setCostesssFin(Double.parseDouble(costesSsFin));
                }
                if (costeTotalRealFin != null && !"".equals(costeTotalRealFin)) {
                    nuevaContratacion.setCostetotalrealFin(Double.parseDouble(costeTotalRealFin));
                }
                if (subvConcedidaFin != null && !"".equals(subvConcedidaFin)) {
                    nuevaContratacion.setSubvconcedidalanFin(Double.parseDouble(subvConcedidaFin));
                }
            }

            if (!tabla2 && !tabla3) {
                tablas = 1;
            } else if (tabla2) {
                if (!tabla3) {
                    tablas = 2;
                } else {
                    tablas = 3;
                }
            }
            log.debug("Tablas: " + tablas);

            MeLanbide82Manager meLanbide82Manager = MeLanbide82Manager.getInstance();
            boolean insertOK = meLanbide82Manager.crearContratacion(nuevaContratacion, tablas, adapt);
            if (insertOK) {
                log.debug("Contratación insertada correctamente");
                codigoOperacion = "0";
                listaContrataciones = meLanbide82Manager.getListaContrataciones(numExp, codOrganizacion, adapt);
                if (!listaContrataciones.isEmpty()) {
                    listaContrataciones = obtenerDescripciones(listaContrataciones, request);
                    final NuevosAtributosFilaContrVO nuevosAtributosFilaContrVO = new NuevosAtributosFilaContrVO();
                    nuevosAtributosFilaContrVO.setCamposFormularioFechas(MeLanbide82Manager.getInstance().getValoresFechas(numExp, adapt));
                    nuevosAtributosFilaContrVO.setCamposFormularioFicheros(MeLanbide82Manager.getInstance().getFicheros(numExp, adapt));
                    iterListaContrataciones(listaContrataciones, nuevosAtributosFilaContrVO);
                }
            } else {
                log.error("No se ha insertado correctamente la nueva contratación");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error al parsear los parametros recibidos del jsp al objeto ContratacionVO" + ex.getMessage());
            codigoOperacion = "3";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaContratacion(request, codigoOperacion, listaContrataciones);
        retornarXML(xmlSalida, response);
    }

    public void modificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarContratacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaContratacionVO> listaContrataciones = new ArrayList<FilaContratacionVO>();
        int tablas = 1;
        boolean tabla2 = false;
        boolean tabla3 = false;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String numExp = request.getParameter("expediente");

            String prioridad = request.getParameter("prioridad");
            String denomPuesto = request.getParameter("denomPuesto");
            String nivelCualificacion = request.getParameter("nivelCualificacion");
            String modContrato = request.getParameter("modContrato");
            String durContrato = request.getParameter("durContrato");
            String grupoCotiz = request.getParameter("grupoCotiz");
            String costeSalarial = request.getParameter("costeSalarial").replace(",", ".");
            String subvSolicitada = request.getParameter("subvSolicitada").replace(",", ".");
            //INICIO
            String municipioInicio = (request.getParameter("municipioInicio") != null ? request.getParameter("municipioInicio") : "");
            String dniNieInicio = (request.getParameter("dniNieInicio") != null ? request.getParameter("dniNieInicio") : "");
            String nombreInicio = (request.getParameter("nombreInicio") != null ? request.getParameter("nombreInicio") : "");
            String apellido1Inicio = (request.getParameter("apellido1Inicio") != null ? request.getParameter("apellido1Inicio") : "");
            String apellido2Inicio = (request.getParameter("apellido2Inicio") != null ? request.getParameter("apellido2Inicio") : "");
            String edadInicio = (request.getParameter("edadInicio") != null ? request.getParameter("edadInicio") : "");
            String sexoInicio = (request.getParameter("sexoInicio") != null ? request.getParameter("sexoInicio") : "");
            String grupoCotizInicio = (request.getParameter("grupoCotizInicio") != null ? request.getParameter("grupoCotizInicio") : "");
            String fechaNacimientoInicio = (request.getParameter("fechaNacimientoInicio") != null ? request.getParameter("fechaNacimientoInicio") : "");
            String nivelCualificacionInicio = (request.getParameter("nivelCualificacionInicio") != null ? request.getParameter("nivelCualificacionInicio") : "");
            String puestoTrabajoInicio = (request.getParameter("puestoTrabajoInicio") != null ? request.getParameter("puestoTrabajoInicio") : "");
            String nOfertaInicio = (request.getParameter("nOfertaInicio") != null ? request.getParameter("nOfertaInicio") : "");
            String retribucionBrutaInicio = (request.getParameter("retribucionBrutaInicio") != null ? request.getParameter("retribucionBrutaInicio").replace(",", ".") : "");
            String fechaInicioInicio = (request.getParameter("fechaInicioInicio") != null ? request.getParameter("fechaInicioInicio") : "");
            String durContratoInicio = (request.getParameter("durContratoInicio") != null ? request.getParameter("durContratoInicio") : "");
            //FIN
            String municipioFin = (request.getParameter("municipioFin") != null ? request.getParameter("municipioFin") : "");
            String dniNieFin = (request.getParameter("dniNieFin") != null ? request.getParameter("dniNieFin") : "");
            String nombreFin = (request.getParameter("nombreFin") != null ? request.getParameter("nombreFin") : "");
            String apellido1Fin = (request.getParameter("apellido1Fin") != null ? request.getParameter("apellido1Fin") : "");
            String apellido2Fin = (request.getParameter("apellido2Fin") != null ? request.getParameter("apellido2Fin") : "");
            String sexoFin = (request.getParameter("sexoFin") != null ? request.getParameter("sexoFin") : "");
            String grupoCotizFin = (request.getParameter("grupoCotizFin") != null ? request.getParameter("grupoCotizFin") : "");
            String fechaInicioFin = (request.getParameter("fechaInicioFin") != null ? request.getParameter("fechaInicioFin") : "");
            String fechaFinFin = (request.getParameter("fechaFinFin") != null ? request.getParameter("fechaFinFin") : "");
            String durContratoFin = (request.getParameter("durContratoFin") != null ? request.getParameter("durContratoFin") : "");
            String retribucionBrutaFin = (request.getParameter("retribucionBrutaFin") != null ? request.getParameter("retribucionBrutaFin").replace(",", ".") : "");
            String costeSalarialFin = (request.getParameter("costeSalarialFin") != null ? request.getParameter("costeSalarialFin").replace(",", ".") : "");
            String indemFinContratoFin = (request.getParameter("indemFinContratoFin") != null ? request.getParameter("indemFinContratoFin").replace(",", ".") : "");
            String costesSsFin = (request.getParameter("costesSsFin") != null ? request.getParameter("costesSsFin").replace(",", ".") : "");
            String costeTotalRealFin = (request.getParameter("costeTotalRealFin") != null ? request.getParameter("costeTotalRealFin").replace(",", ".") : "");
            String subvConcedidaFin = (request.getParameter("subvConcedidaFin") != null ? request.getParameter("subvConcedidaFin").replace(",", ".") : "");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la contratación a modificar ");
                codigoOperacion = "3";
            } else {
                FilaContratacionVO datModif = MeLanbide82Manager.getInstance().getContratacionPorID(id, adapt);
//                FilaContratacionVO datModif = new FilaContratacionVO();
                numExp = datModif.getNumExp();
                datModif.setId(Integer.parseInt(id));
                datModif.setNumExp(numExp);

                if (prioridad != null && !"".equals(prioridad)) {
                    datModif.setPrioridad(Integer.parseInt(prioridad));
                }
                datModif.setDenomPuesto(denomPuesto);
                datModif.setNivelCualificacion(nivelCualificacion);
                datModif.setModContrato(modContrato);
                if (durContrato != null && !"".equals(durContrato)) {
                    datModif.setDurContrato(Integer.parseInt(durContrato));
                }
                datModif.setGrupoCotiz(grupoCotiz);
                if (costeSalarial != null && !"".equals(costeSalarial)) {
                    datModif.setCostesalarial(Double.parseDouble(costeSalarial));
                }
                if (subvSolicitada != null && !"".equals(subvSolicitada)) {
                    datModif.setSubvsolicitada(Double.parseDouble(subvSolicitada));
                }
                //INICIO
                if (dniNieInicio != null && !"".equalsIgnoreCase(dniNieInicio)) {
                    datModif.setDninieInicio(dniNieInicio);
                    tabla2 = true;
                    datModif.setMunicipioInicio(municipioInicio);
                    datModif.setNombreInicio(nombreInicio);
                    datModif.setApellido1Inicio(apellido1Inicio);
                    datModif.setApellido2Inicio(apellido2Inicio);
                    if (edadInicio != null && !"".equals(edadInicio)) {
                        datModif.setEdadInicio(Integer.parseInt(edadInicio));
                    }
                    datModif.setSexoInicio(sexoInicio);
                    datModif.setGrupocotizInicio(grupoCotizInicio);
                    if (fechaNacimientoInicio != null && !"".equalsIgnoreCase(fechaNacimientoInicio)) {
                        datModif.setFechanacimientoInicio(new java.sql.Date(dateFormat.parse(fechaNacimientoInicio).getTime()));
                    }
                    datModif.setNivelcualificacionInicio(nivelCualificacionInicio);
                    datModif.setPuestotrabajoInicio(puestoTrabajoInicio);
                    datModif.setNofertaInicio(nOfertaInicio);
                    if (retribucionBrutaInicio != null && !"".equals(retribucionBrutaInicio)) {
                        datModif.setRetribucionbrutaInicio(Double.parseDouble(retribucionBrutaInicio));
                    }
                    if (fechaInicioInicio != null && !"".equalsIgnoreCase(fechaInicioInicio)) {
                        datModif.setFechainicioInicio(new java.sql.Date(dateFormat.parse(fechaInicioInicio).getTime()));
                    }
                    if (durContratoInicio != null && !"".equals(durContratoInicio)) {
                        datModif.setDurcontratoInicio(Integer.parseInt(durContratoInicio));
                    }
                }
                //FIN
                if (dniNieFin != null && !"".equalsIgnoreCase(dniNieFin)) {
                    datModif.setDninieFin(dniNieFin);
                    tabla3 = true;
                    datModif.setMunicipioFin(municipioFin);
                    datModif.setNombreFin(nombreFin);
                    datModif.setApellido1Fin(apellido1Fin);
                    datModif.setApellido2Fin(apellido2Fin);
                    datModif.setSexoFin(sexoFin);
                    datModif.setGrupocotizFin(grupoCotizFin);
                    if (fechaInicioFin != null && !"".equalsIgnoreCase(fechaInicioFin)) {
                        datModif.setFechainicioFin(new java.sql.Date(dateFormat.parse(fechaInicioFin).getTime()));
                    }
                    if (fechaFinFin != null && !"".equalsIgnoreCase(fechaFinFin)) {
                        datModif.setFechafinFin(new java.sql.Date(dateFormat.parse(fechaFinFin).getTime()));
                    }
                    if (durContratoFin != null && !"".equals(durContratoFin)) {
                        datModif.setDurcontratoFin(Integer.parseInt(durContratoFin));
                    }
                    if (retribucionBrutaFin != null && !"".equals(retribucionBrutaFin)) {
                        datModif.setRetribucionbrutaFin(Double.parseDouble(retribucionBrutaFin));
                    }
                    if (costeSalarialFin != null && !"".equals(costeSalarialFin)) {
                        datModif.setCostesalarialFin(Double.parseDouble(costeSalarialFin));
                    }
                    if (indemFinContratoFin != null && !"".equals(indemFinContratoFin)) {
                        datModif.setIndemfincontratoFin(Double.parseDouble(indemFinContratoFin));
                    }
                    if (costesSsFin != null && !"".equals(costesSsFin)) {
                        datModif.setCostesssFin(Double.parseDouble(costesSsFin));
                    }
                    if (costeTotalRealFin != null && !"".equals(costeTotalRealFin)) {
                        datModif.setCostetotalrealFin(Double.parseDouble(costeTotalRealFin));
                    }
                    if (subvConcedidaFin != null && !"".equals(subvConcedidaFin)) {
                        datModif.setSubvconcedidalanFin(Double.parseDouble(subvConcedidaFin));
                    }
                }

                if (!tabla2 && !tabla3) {
                    tablas = 1;
                } else if (tabla2) {
                    if (!tabla3) {
                        tablas = 2;
                    } else {
                        tablas = 3;
                    }
                }
                log.debug("Tablas: " + tablas);

                MeLanbide82Manager meLanbide82Manager = MeLanbide82Manager.getInstance();
                boolean modOK = meLanbide82Manager.modificarContratacion(datModif, tablas, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        listaContrataciones = meLanbide82Manager.getListaContrataciones(numExp, codOrganizacion, adapt);
                        if (!listaContrataciones.isEmpty()) {
                            listaContrataciones = obtenerDescripciones(listaContrataciones, request);
                            final NuevosAtributosFilaContrVO nuevosAtributosFilaContrVO = new NuevosAtributosFilaContrVO();
                            nuevosAtributosFilaContrVO.setCamposFormularioFechas(MeLanbide82Manager.getInstance().getValoresFechas(numExp, adapt));
                            nuevosAtributosFilaContrVO.setCamposFormularioFicheros(MeLanbide82Manager.getInstance().getFicheros(numExp, adapt));
                            iterListaContrataciones(listaContrataciones, nuevosAtributosFilaContrVO);
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de contrataciones después de modificar una contratación : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de contrataciones después de modificar una contratación : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "1";
                    log.error("Error modificar una Contratación.");
                }
            }
        } catch (Exception ex) {
            log.error("Error modificar una Contratación ", ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaContratacion(request, codigoOperacion, listaContrataciones);
        retornarXML(xmlSalida, response);
    }

    public void eliminarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarContratacion - ");
        String codigoOperacion = "-1";
        List<FilaContratacionVO> listaContrataciones = new ArrayList<FilaContratacionVO>();
        String numExp = "", docCV = "", docDemanda = "";
        try {
            String id = request.getParameter("id");
            docCV = request.getParameter("docCV");
            docDemanda = request.getParameter("docDemanda");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la contratación a elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide82Manager.getInstance().eliminarContratacion(id, numExp, docCV, docDemanda,
                        codOrganizacion, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        listaContrataciones = MeLanbide82Manager.getInstance().getListaContrataciones(numExp, codOrganizacion, adapt);
                        if (!listaContrataciones.isEmpty()) {
                            listaContrataciones = obtenerDescripciones(listaContrataciones, request);
                            final NuevosAtributosFilaContrVO nuevosAtributosFilaContrVO = new NuevosAtributosFilaContrVO();
                            nuevosAtributosFilaContrVO.setCamposFormularioFechas(MeLanbide82Manager.getInstance().getValoresFechas(numExp, adapt));
                            nuevosAtributosFilaContrVO.setCamposFormularioFicheros(MeLanbide82Manager.getInstance().getFicheros(numExp, adapt));
                            iterListaContrataciones(listaContrataciones, nuevosAtributosFilaContrVO);
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "1";
                        log.error("Error al recuperar la lista de contratación después de eliminar una contratación", ex);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error eliminando una contratación: " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaContratacion(request, codigoOperacion, listaContrataciones);
        retornarXML(xmlSalida, response);
    }

    /**
     * Metodo que carga en el expediente los datos del XML recibido llamando a
     * cargarExpedienteExtension()
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String procesarXML(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en procesarXML" + this.getClass().getSimpleName());
        int codIdioma = 1;
        request.getSession().setAttribute("mensajeImportar", "");
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile fichero = table.get("fichero_xml");
            //Use commons-fileupload to obtain the byte[] of the file (in a servlet of yours)
            byte[] contenido = fichero.getFileData();
            String fileName = fichero.getFileName();
            log.debug("fileName: " + fileName);
            String xml = new String(contenido);
            if (!("").equals(xml)) {
                cargarExpedienteExtension(codOrganizacion, numExpediente, xml);
            } else {
                request.getSession().setAttribute("mensajeImportar", MeLanbide82I18n.getInstance().getMensaje(codIdioma, "error.xmlVacio"));
            }

            request.getSession().setAttribute("mensajeImportar", MeLanbide82I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
        } catch (DataFormatException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE82.class.getName()).log(Level.SEVERE, null, ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide82I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE82.class.getName()).log(Level.SEVERE, null, ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide82I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE82.class.getName()).log(Level.SEVERE, null, ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide82I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE82.class.getName()).log(Level.SEVERE, null, ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide82I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide82I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarListaContrataciones.jsp");
        return "/jsp/extension/melanbide82/recargarListaContrataciones.jsp";
    }

    /**
     * Método que recarga los datos de la pestaña después de procesar el XML
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void actualizarPestana(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en actualizarPestana de " + this.getClass().getSimpleName());
        String codigoOperacion = "-1";
        List<FilaContratacionVO> listaContrataciones = new ArrayList<FilaContratacionVO>();
        String numExp = "";
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            listaContrataciones = MeLanbide82Manager.getInstance().getListaContrataciones(numExp, codOrganizacion, adapt);
            if (listaContrataciones.isEmpty()) {
                log.error("No se ha cargado ningún registro para el expediente " + numExp + " después de procesar el XML");
                codigoOperacion = "4";
            } else {
                codigoOperacion = "0";
                listaContrataciones = obtenerDescripciones(listaContrataciones, request);
                final NuevosAtributosFilaContrVO nuevosAtributosFilaContrVO = new NuevosAtributosFilaContrVO();
                nuevosAtributosFilaContrVO.setCamposFormularioFechas(MeLanbide82Manager.getInstance().getValoresFechas(numExp, adapt));
                nuevosAtributosFilaContrVO.setCamposFormularioFicheros(MeLanbide82Manager.getInstance().getFicheros(numExp, adapt));
                iterListaContrataciones(listaContrataciones, nuevosAtributosFilaContrVO);
            }
        } catch (BDException bde) {
            codigoOperacion = "1";
            log.error("Error de tipo BD al recuperar la lista de Personas después de cargar el XML : " + bde.getMensaje());
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error al recuperar la lista de Personas después de cargar el XML : " + ex.getMessage());
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaContratacion(request, codigoOperacion, listaContrataciones);
        retornarXML(xmlSalida, response);
    }

    public void comprobarPrioridad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en comprobarPrioridad - ");
        String codigoOperacion = "0";
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String numExp = request.getParameter("expediente");
            String prioridad = request.getParameter("prioridad");
            if (MeLanbide82Manager.getInstance().existePrioridad(numExp, Integer.valueOf(prioridad), adapt)) {
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error al comprobar si existe Prioridad : " + ex.getMessage());
        }
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        retornarXML(xmlSalida.toString(), response);
    }

    public void recuperarDatosCVoDemandaIntermediacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        final List<SearchCVResponseDTO> responseDTOList = new ArrayList<SearchCVResponseDTO>();
        List<FilaContratacionVO> listaContrataciones = null;
        AdaptadorSQLBD adapt = null;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        try {
            listaContrataciones = MeLanbide82Manager.getInstance().getListaContrataciones(numExpediente, codOrganizacion, adapt);
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al invocar getListaContrataciones ", ex);
        }

        final Calendar calendar = Calendar.getInstance();
        final java.util.Date currentDate = calendar.getTime();
        final Date fechaDemandaInter = new java.sql.Date(currentDate.getTime());

        int i = 0;
        for (final FilaContratacionVO contr : listaContrataciones) {
            log.debug("recuperarDatosCVoDemandaIntermediacion DNI = " + contr);
            if (contr.getDninieInicio() != null && !contr.getDninieInicio().equals("")) {
                final SearchCVResponseDTO respuestaDni2 = melanbide67.recuperarDatosCVoDemandaIntermediacion(codOrganizacion,
                        contr.getDninieInicio(), request.getParameter("documento"), numExpediente, request, response);
                if (respuestaDni2 != null && !respuestaDni2.hasError()) {
                    responseDTOList.add(respuestaDni2);
                    gestionarFichero(respuestaDni2.getUrl(), numExpediente, fechaDemandaInter,
                            "FECHA" + getPartNameField(request.getParameter("documento")) + "2" + i + "INTER",
                            "DOC" + getPartNameField(request.getParameter("documento")) + "2" + i + "INTER", adapt);
                }

            }
            i++;
        }
        String respuesta = "1";
        for (final SearchCVResponseDTO responseDTO : responseDTOList) {
            if (!responseDTO.hasError()) {
                respuesta = "0";
            }
        }
        setResponse("recuperarDatosCVoDemandaIntermediacion preparando respuesta ", response, respuesta);
        log.debug("recuperarDatosCVoDemandaIntermediacion respondeDTO = " + responseDTOList
                + ", " + responseDTOList.size());
    }

    public void recuperarDatosIntermediacionExterno(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) {
        List<FilaContratacionVO> listaContrataciones = null;
        AdaptadorSQLBD adapt = null;
        String[] result;
        String respuesta = "0";

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        try {
            listaContrataciones = MeLanbide82Manager.getInstance().getListaContrataciones(numExpediente, codOrganizacion, adapt);
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al invocar getListaContrataciones ", ex);
        }

        int i = 0;
        result = listaContrataciones != null && !listaContrataciones.isEmpty() ? new String[listaContrataciones.size()] : null;

        for (final FilaContratacionVO contr : listaContrataciones) {
            log.debug("recuperarDatosIntermediacionExterno DNI inicio= " + contr.getDninieInicio());
            result[i] = "0";
            if (contr.getDninieInicio() != null && !contr.getDninieInicio().equals("")) {
                result[i] = recuperarDatosIntermediacionExterno(codOrganizacion,
                        ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_CONTRATATACION_INICIO, ConstantesMeLanbide82.FICHERO_PROPIEDADES), 
                        contr.getIdInicio(),
                        ConfigurationParameter.getParameter(ConstantesMeLanbide82.COLUMNA_CONTRATACION_SISTGRANTIAJUVE_INICIO, ConstantesMeLanbide82.FICHERO_PROPIEDADES), 
                        contr.getDninieInicio(), numExpediente, request, response);
            }
            log.debug("recuperarDatosIntermediacionExterno DNI fin= " + contr.getDninieFin());
            if (contr.getDninieFin() != null && !contr.getDninieFin().equals("")) {
                result[i] = recuperarDatosIntermediacionExterno(codOrganizacion,
                         ConfigurationParameter.getParameter(ConstantesMeLanbide82.TABLA_CONTRATATACION_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES), 
                        contr.getIdFin(),
                        ConfigurationParameter.getParameter(ConstantesMeLanbide82.COLUMNA_CONTRATACION_SISTGRANTIAJUVE_FIN, ConstantesMeLanbide82.FICHERO_PROPIEDADES), 
                        contr.getDninieFin(), numExpediente, request, response);
            }
            i++;
        }
        if (result != null) {
            int size = result.length;
            for (i = 0; i < size; i++) {
                if (!result[i].equals("0")) {
                    respuesta = result[i];
                    break;
                }
            }
        }
        setResponse("recuperarDatosIntermediacionExterno preparando respuesta ", response, respuesta);
    }
    
    private String recuperarDatosIntermediacionExterno(int codOrganizacion, final String tabla, final Integer id, final String nombreCampo,
            final String numDoc, final String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("=========== ENTRO en recuperarDatosIntermediacionExterno");

        AdaptadorSQLBD adaptador = null;
        PersonaContratadaVO personaContratada = new PersonaContratadaVO();
        String codigoError = "0";

        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide82Manager manager = MeLanbide82Manager.getInstance();

            String nifPersonaContratada = numDoc;

            //Se informa del numero de oferta
            if (null != nifPersonaContratada && !nifPersonaContratada.isEmpty()) {
                personaContratada = manager.getDatosPersonaContratadaExterno(nifPersonaContratada, adaptador);
                personaContratada.setDni(numDoc);
                if (null != personaContratada.getDni() && !personaContratada.getDni().equals("")) {
                    manager.setDatosPersonaContratadaExterno(personaContratada, tabla, id, nombreCampo,
                            adaptador);
                } else {
                    codigoError = "4";
                }
            } else {
                codigoError = "1";
            }

        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
        } catch (Exception ex) {
            log.error("Ha ocurrido un error al recuperar valores de datos suplementarios para el expediente " + numExpediente, ex);
        } finally {
            return codigoError;
        }
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
                final CamposFormulario camposFormularioFicheros = MeLanbide82Manager.getInstance().getFicheros(numExpediente, adapt);

                final String nameFile = request.getParameter("nameFile");
                if (camposFormularioFicheros.contieneCampo(nameFile.split("\\.")[0])) {
                    final Object obj = camposFormularioFicheros.get(nameFile.split("\\.")[0]);
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

    private void iterListaContrataciones(List<FilaContratacionVO> listaContrataciones,
            final NuevosAtributosFilaContrVO nuevosAtributosFilaContrVO)
            throws ParseException, FileNotFoundException, IOException, SQLException {
        int i = 0;
        Object obj = null;
        String dateString = null;
        java.sql.Date dateSql = null;

        for (final FilaContratacionVO fila : listaContrataciones) {
            if (nuevosAtributosFilaContrVO.getCamposFormularioFechas().contieneCampo("FECHA" + "CV" + "2" + i + "INTER")) {
                obj = nuevosAtributosFilaContrVO.getCamposFormularioFechas().get("FECHA" + "CV" + "2" + i + "INTER");
                dateString = (String) obj;
                dateSql = new java.sql.Date(dateFormat.parse(dateString).getTime());
                fila.setFechaCv2(dateSql);
            }
            if (nuevosAtributosFilaContrVO.getCamposFormularioFechas().contieneCampo("FECHA" + "DEM" + "2" + i + "INTER")) {
                obj = nuevosAtributosFilaContrVO.getCamposFormularioFechas().get("FECHA" + "DEM" + "2" + i + "INTER");
                dateString = (String) obj;
                dateSql = new java.sql.Date(dateFormat.parse(dateString).getTime());
                fila.setFechaDemanda2(dateSql);
            }
            if (nuevosAtributosFilaContrVO.getCamposFormularioFicheros().contieneCampo("DOC" + "CV" + "2" + i + "INTER")) {
                fila.setCv2("DOC" + "CV" + "2" + i + "INTER");
            }
            if (nuevosAtributosFilaContrVO.getCamposFormularioFicheros().contieneCampo("DOC" + "DEM" + "2" + i + "INTER")) {
                fila.setDemanda2("DOC" + "DEM" + "2" + i + "INTER");
            }
            i++;
        }
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

    private String getPartNameField(final String documento) {
        if (documento != null) {
            if (documento.equals("1")) {
                return "CV";
            } else {
                return "DEM";
            }
        } else {
            return "CV";
        }
    }

    private boolean gestionarFichero(final String url, final String numExpediente, final Date fechaDocInter,
            final String nameFieldFecha, final String nameFieldDoc, final AdaptadorSQLBD adapt) {
        File docFile = null;

        try {
            docFile = getDocFile(url);
        } catch (final MalformedURLException ex) {
            log.error("gestionarFichero: No hemos podido abrir el fichero del documento");
            return false;
        } catch (final IOException ex) {
            log.error("gestionarFichero: No hemos podido abrir el fichero del documento");
            return false;
        }

        if (docFile != null) {
            log.info("gestionarFichero docFile " + docFile.getAbsolutePath() + ", " + docFile.getName() + ", "
                    + docFile.length());
            try {
                MeLanbide82Manager.getInstance().setDatosCVoDemandaIntermediacion(numExpediente, fechaDocInter, nameFieldFecha, docFile, nameFieldDoc, adapt);

//                        .setDatosCVoDemandaIntermediacion(numExpediente,
//                        fechaDocInter, nameFieldFecha, docFile, nameFieldDoc, adapt);
            } catch (Exception ex) {
                docFile.delete();
                return false;
            }
            docFile.delete();
            return true;
        } else {
            log.error("gestionarFichero: No hemos guardado el fichero de documento");
            return false;
        }
    }

    private File getDocFile(final String fileUrl) throws MalformedURLException, IOException {
        log.info("getDocFile - Begin()");
        String nameFile;
        final String[] partsUrl = fileUrl.split("/");
        if (partsUrl != null && partsUrl.length > 0) {
            nameFile = partsUrl[partsUrl.length - 1];
        } else {
            return null;
        }
        log.info("getDocFile - nameFile = " + nameFile);
        final BufferedInputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
        log.info("getDocFile - in = " + in);
        final FileOutputStream fileOutputStream = new FileOutputStream(nameFile);
        byte dataBuffer[] = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
//            log.info("getFileCV - bytesRead = " + bytesRead);
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }

        fileOutputStream.close();
        in.close();
        log.info("getDocFile - End()");
        return new File(nameFile);
    }

    // Funciones Privadas
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
                ds
                        = (DataSource) pc.lookup(jndiGenerico, DataSource.class
                        );
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
            if (log.isDebugEnabled()) {
//                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    /**
     * Método que recuperalos valores de los desplegables del modulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegablesGEL(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaSexo = MeLanbide82Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide82.COD_DES_SEXO, ConstantesMeLanbide82.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaNivel = MeLanbide82Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide82.COD_DES_NIVEL, ConstantesMeLanbide82.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaGrupoCotizacion = MeLanbide82Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide82.COD_DES_GCON, ConstantesMeLanbide82.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaDuracion = MeLanbide82Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide82.COD_DES_DURA, ConstantesMeLanbide82.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

        if (!listaSexo.isEmpty()) {
            listaSexo = traducirDesplegable(request, listaSexo);
            request.setAttribute("listaSexo", listaSexo);
        }
        if (!listaNivel.isEmpty()) {
            listaNivel = traducirDesplegable(request, listaNivel);
            request.setAttribute("listaNivel", listaNivel);
        }
        if (!listaGrupoCotizacion.isEmpty()) {
            listaGrupoCotizacion = traducirDesplegable(request, listaGrupoCotizacion);
            request.setAttribute("listaGrupoCotizacion", listaGrupoCotizacion);
        }
        if (!listaDuracion.isEmpty()) {
            listaDuracion = traducirDesplegable(request, listaDuracion);
            request.setAttribute("listaDuracion", listaDuracion);
        }
    }

    /**
     * Método que extrae la descripcion de los desplegables en el idioma del
     * usuario, en BBDD estan en un campo separadas por Pipeline |
     *
     * @param request
     * @param desplegable
     * @return la lista en el idioma de usu
     */
    private List<DesplegableAdmonLocalVO> traducirDesplegable(HttpServletRequest request, List<DesplegableAdmonLocalVO> desplegable) {
        for (DesplegableAdmonLocalVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().isEmpty()) {
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }
        return desplegable;
    }

    /**
     * @param listaContrataciones
     * @param request
     * @return la lista en el idioma correspondiente
     */
    private List<FilaContratacionVO> obtenerDescripciones(List<FilaContratacionVO> listaContrataciones, HttpServletRequest request) {
        for (FilaContratacionVO contr : listaContrataciones) {
            contr.setDescNivelcualificacion(getDescripcionDesplegable(request, contr.getDescNivelcualificacion()));
            contr.setDescDurContrato(getDescripcionDesplegable(request, contr.getDescDurContrato()));
            contr.setDescGrupoCotiz(getDescripcionDesplegable(request, contr.getDescGrupoCotiz()));
            contr.setDescSexoInicio(getDescripcionDesplegable(request, contr.getDescSexoInicio()));
            contr.setDescNivelcualificacionInicio(getDescripcionDesplegable(request, contr.getDescNivelcualificacionInicio()));
            if (contr.getDurcontratoInicio() != null) {
                contr.setDescDurContratoInicio(getDescripcionDesplegable(request, contr.getDescDurContratoInicio()));
            }
            contr.setDescGrupocotizInicio(getDescripcionDesplegable(request, contr.getDescGrupocotizInicio()));
            contr.setDescSexoFin(getDescripcionDesplegable(request, contr.getDescSexoFin()));
            if (contr.getDurcontratoFin() != null) {
                contr.setDescDurContratoFin(getDescripcionDesplegable(request, contr.getDescDurContratoFin()));
            }
            contr.setDescGrupocotizFin(getDescripcionDesplegable(request, contr.getDescGrupocotizFin()));
        }
        return listaContrataciones;
    }

    /**
     * Método que retorna el valor de un desplegable en el idiona del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor en el idioma correspondiente
     */
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide82.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide82.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide82.CODIGO_IDIOMA_EUSKERA) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                }
            } else {
                descripcion = "-";
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
        int idioma = ConstantesMeLanbide82.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide82.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }

    // ----------------------------------------------------------------------------------------------------------
    // ---------------    XML    --------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
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
        }
    }

    private String obtenerXmlSalidaContratacion(HttpServletRequest request, String codigoOperacion, List<FilaContratacionVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();

        xmlSalida.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for (FilaContratacionVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<PRIORIDAD>");
            xmlSalida.append(fila.getPrioridad() != null && !"".equals(fila.getPrioridad().toString()) ? fila.getPrioridad() : "");
            xmlSalida.append("</PRIORIDAD>");
            xmlSalida.append("<DENOMPUESTO>");
            xmlSalida.append(fila.getDenomPuesto());
            xmlSalida.append("</DENOMPUESTO>");
            xmlSalida.append("<NIVELCUALIFICACION>");
            xmlSalida.append(fila.getNivelCualificacion());
            xmlSalida.append("</NIVELCUALIFICACION>");
            xmlSalida.append("<DESC_NIVEL>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDescNivelcualificacion()));
            xmlSalida.append("</DESC_NIVEL>");
            xmlSalida.append("<MODCONTRATO>");
            xmlSalida.append(fila.getModContrato());
            xmlSalida.append("</MODCONTRATO>");
            xmlSalida.append("<DURCONTRATO>");
            xmlSalida.append(fila.getDurContrato() != null && !"".equals(fila.getDurContrato().toString()) ? fila.getDurContrato() : "");
            xmlSalida.append("</DURCONTRATO>");
            xmlSalida.append("<DESC_DURCONTRATO>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDescDurContrato()));
            xmlSalida.append("</DESC_DURCONTRATO>");
            xmlSalida.append("<GRUPOCOTIZ>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDescGrupoCotiz()));
            xmlSalida.append("</GRUPOCOTIZ>");
            xmlSalida.append("<COSTESALARIAL>");
            xmlSalida.append(fila.getCostesalarial() != null && !"".equals(fila.getCostesalarial().toString()) ? fila.getCostesalarial().toString().replace(".", ",") : "");
            xmlSalida.append("</COSTESALARIAL>");
            xmlSalida.append("<SUBVSOLICITADA>");
            xmlSalida.append(fila.getSubvsolicitada() != null && !"".equals(fila.getSubvsolicitada().toString()) ? fila.getSubvsolicitada().toString().replace(".", ",") : "");
            xmlSalida.append("</SUBVSOLICITADA>");

            // CONTRATACION_INI
            xmlSalida.append("<MUNICIPIOINICIO>");
            xmlSalida.append(fila.getMunicipioInicio() != null ? fila.getMunicipioInicio() : "");
            xmlSalida.append("</MUNICIPIOINICIO>");
            xmlSalida.append("<NOMBREINICIO>");
            xmlSalida.append(fila.getNombreInicio() != null ? fila.getNombreInicio() : "");
            xmlSalida.append("</NOMBREINICIO>");
            xmlSalida.append("<APELLIDO1INICIO>");
            xmlSalida.append(fila.getApellido1Inicio() != null ? fila.getApellido1Inicio() : "");
            xmlSalida.append("</APELLIDO1INICIO>");
            xmlSalida.append("<APELLIDO2INICIO>");
            xmlSalida.append(fila.getApellido2Inicio() != null ? fila.getApellido2Inicio() : "");
            xmlSalida.append("</APELLIDO2INICIO>");
            xmlSalida.append("<DNINIEINICIO>");
            xmlSalida.append(fila.getDninieInicio() != null ? fila.getDninieInicio() : "");
            xmlSalida.append("</DNINIEINICIO>");

            xmlSalida.append("<CV2>");
            xmlSalida.append(fila.getCv2() != null ? fila.getCv2() : "");
            xmlSalida.append("</CV2>");
            xmlSalida.append("<FECHACV2>");
            xmlSalida.append(fila.getFechaCv2() != null ? dateFormat.format(fila.getFechaCv2()) : "");
            xmlSalida.append("</FECHACV2>");
            xmlSalida.append("<DEMANDA2>");
            xmlSalida.append(fila.getDemanda2() != null ? fila.getDemanda2() : "");
            xmlSalida.append("</DEMANDA2>");
            xmlSalida.append("<FECHADEMANDA2>");
            xmlSalida.append(fila.getFechaDemanda2() != null ? dateFormat.format(fila.getFechaDemanda2()) : "");
            xmlSalida.append("</FECHADEMANDA2>");

            xmlSalida.append("<FECHANACIMIENTOINICIO>");
            xmlSalida.append(fila.getFechanacimientoInicio() != null ? dateFormat.format(fila.getFechanacimientoInicio()) : "");
            xmlSalida.append("</FECHANACIMIENTOINICIO>");
            xmlSalida.append("<SEXOINICIO>");
            xmlSalida.append(fila.getSexoInicio() != null ? fila.getSexoInicio() : "");
            xmlSalida.append("</SEXOINICIO>");
            xmlSalida.append("<DESC_SEXOINICIO>");
            xmlSalida.append(fila.getDescSexoInicio() != null ? getDescripcionDesplegable(request, fila.getDescSexoInicio()) : "");
            xmlSalida.append("</DESC_SEXOINICIO>");
            xmlSalida.append("<NIVELCUALIFICACIONINICIO>");
            xmlSalida.append(fila.getNivelcualificacionInicio() != null ? fila.getNivelcualificacionInicio() : "");
            xmlSalida.append("</NIVELCUALIFICACIONINICIO>");
            xmlSalida.append("<DESC_NIVELINICIO>");
            xmlSalida.append(fila.getDescNivelcualificacionInicio() != null ? getDescripcionDesplegable(request, fila.getDescNivelcualificacionInicio()) : "");
            xmlSalida.append("</DESC_NIVELINICIO>");
            xmlSalida.append("<PUESTOTRABAJOINICIO>");
            xmlSalida.append(fila.getPuestotrabajoInicio() != null ? fila.getPuestotrabajoInicio() : "");
            xmlSalida.append("</PUESTOTRABAJOINICIO>");
            xmlSalida.append("<NOFERTAINICIO>");
            xmlSalida.append(fila.getNofertaInicio() != null ? fila.getNofertaInicio() : "");
            xmlSalida.append("</NOFERTAINICIO>");
            xmlSalida.append("<GRUPOCOTIZINICIO>");
            xmlSalida.append(fila.getGrupocotizInicio() != null ? fila.getGrupocotizInicio() : "");
            xmlSalida.append("</GRUPOCOTIZINICIO>");
            xmlSalida.append("<DESC_GRUPOCOTIZINICIO>");
            xmlSalida.append(fila.getDescGrupocotizInicio() != null ? getDescripcionDesplegable(request, fila.getDescGrupocotizInicio()) : "");
            xmlSalida.append("</DESC_GRUPOCOTIZINICIO>");
            xmlSalida.append("<DURACIONINICIO>");
            xmlSalida.append(fila.getDurcontratoInicio() != null ? fila.getDurcontratoInicio().toString() : "");
            xmlSalida.append("</DURACIONINICIO>");
            xmlSalida.append("<DESC_DURACIONINICIO>");
            xmlSalida.append(fila.getDescDurContratoInicio() != null ? getDescripcionDesplegable(request, fila.getDescDurContratoInicio()) : "");
            xmlSalida.append("</DESC_DURACIONINICIO>");
            xmlSalida.append("<FECHAINICIOINICIO>");
            xmlSalida.append(fila.getFechainicioInicio() != null ? dateFormat.format(fila.getFechainicioInicio()) : "");
            xmlSalida.append("</FECHAINICIOINICIO>");
            xmlSalida.append("<EDADINICIO>");
            xmlSalida.append(fila.getEdadInicio() != null ? fila.getEdadInicio().toString() : "");
            xmlSalida.append("</EDADINICIO>");
            xmlSalida.append("<RETRIBUCIONBRUTAINICIO>");
            xmlSalida.append(fila.getRetribucionbrutaInicio() != null && !"".equals(fila.getRetribucionbrutaInicio().toString()) ? fila.getRetribucionbrutaInicio().toString().replace(".", ",") : "");
            xmlSalida.append("</RETRIBUCIONBRUTAINICIO>");
            xmlSalida.append("<SISTGRANTIAJUVE_INI>");
            xmlSalida.append(fila.getSistGrantiaJuveIni() != null && !"".equals(fila.getSistGrantiaJuveIni().toString()) ? fila.getSistGrantiaJuveIni().toString().replace(".", ",") : "");
            xmlSalida.append("</SISTGRANTIAJUVE_INI>");
            xmlSalida.append("<DESC_SISTGRANTIAJUVE_INI>");
            xmlSalida.append(fila.getDescSistGrantiaJuveIni() != null ? getDescripcionDesplegable(request, fila.getDescSistGrantiaJuveIni()) : "");
            xmlSalida.append("</DESC_SISTGRANTIAJUVE_INI>");            
            // CONTRATACION_FIN
            xmlSalida.append("<MUNICIPIOFIN>");
            xmlSalida.append(fila.getMunicipioFin() != null ? fila.getMunicipioFin() : "");
            xmlSalida.append("</MUNICIPIOFIN>");
            xmlSalida.append("<NOMBREFIN>");
            xmlSalida.append(fila.getNombreFin() != null ? fila.getNombreFin() : "");
            xmlSalida.append("</NOMBREFIN>");
            xmlSalida.append("<APELLIDO1FIN>");
            xmlSalida.append(fila.getApellido1Fin() != null ? fila.getApellido1Fin() : "");
            xmlSalida.append("</APELLIDO1FIN>");
            xmlSalida.append("<APELLIDO2FIN>");
            xmlSalida.append(fila.getApellido2Fin() != null ? fila.getApellido2Fin() : "");
            xmlSalida.append("</APELLIDO2FIN>");
            xmlSalida.append("<DNINIEFIN>");
            xmlSalida.append(fila.getDninieFin() != null ? fila.getDninieFin() : "");
            xmlSalida.append("</DNINIEFIN>");
            xmlSalida.append("<SEXOFIN>");
            xmlSalida.append(fila.getSexoFin() != null ? fila.getSexoFin() : "");
            xmlSalida.append("</SEXOFIN>");
            xmlSalida.append("<DESC_SEXOFIN>");
            xmlSalida.append(fila.getDescSexoFin() != null ? getDescripcionDesplegable(request, fila.getDescSexoFin()) : "");
            xmlSalida.append("</DESC_SEXOFIN>");
            xmlSalida.append("<GRUPOCOTIZFIN>");
            xmlSalida.append(fila.getGrupocotizFin() != null ? fila.getGrupocotizFin() : "");
            xmlSalida.append("</GRUPOCOTIZFIN>");
            xmlSalida.append("<DESC_GRUPOCOTIZFIN>");
            xmlSalida.append(fila.getDescGrupocotizFin() != null ? getDescripcionDesplegable(request, fila.getDescGrupocotizFin()) : "");
            xmlSalida.append("</DESC_GRUPOCOTIZFIN>");
            xmlSalida.append("<DURACIONFIN>");
            xmlSalida.append(fila.getDurcontratoFin() != null ? fila.getDurcontratoFin().toString() : "");
            xmlSalida.append("</DURACIONFIN>");
            xmlSalida.append("<DESC_DURACIONFIN>");
            xmlSalida.append(fila.getDescDurContratoFin() != null ? getDescripcionDesplegable(request, fila.getDescDurContratoFin()) : "");
            xmlSalida.append("</DESC_DURACIONFIN>");
            xmlSalida.append("<FECHAINICIOFIN>");
            xmlSalida.append(fila.getFechainicioFin() != null ? dateFormat.format(fila.getFechainicioFin()) : "");
            xmlSalida.append("</FECHAINICIOFIN>");
            xmlSalida.append("<FECHAFINFIN>");
            xmlSalida.append(fila.getFechafinFin() != null ? dateFormat.format(fila.getFechafinFin()) : "");
            xmlSalida.append("</FECHAFINFIN>");
            xmlSalida.append("<RETRIBUCIONBRUTAFIN>");
            xmlSalida.append(fila.getRetribucionbrutaFin() != null && !"".equals(fila.getRetribucionbrutaFin().toString()) ? fila.getRetribucionbrutaFin().toString().replace(".", ",") : "");
            xmlSalida.append("</RETRIBUCIONBRUTAFIN>");
            xmlSalida.append("<COSTEFIN>");
            xmlSalida.append(fila.getCostesalarialFin() != null && !"".equals(fila.getCostesalarialFin().toString()) ? fila.getCostesalarialFin().toString().replace(".", ",") : "");
            xmlSalida.append("</COSTEFIN>");
            xmlSalida.append("<COSTESSFIN>");
            xmlSalida.append(fila.getCostesssFin() != null && !"".equals(fila.getCostesssFin().toString()) ? fila.getCostesssFin().toString().replace(".", ",") : "");
            xmlSalida.append("</COSTESSFIN>");
            xmlSalida.append("<INDEMFINCONTRATOFIN>");
            xmlSalida.append(fila.getIndemfincontratoFin() != null && !"".equals(fila.getIndemfincontratoFin().toString()) ? fila.getIndemfincontratoFin().toString().replace(".", ",") : "");
            xmlSalida.append("</INDEMFINCONTRATOFIN>");
            xmlSalida.append("<COSTEREALFIN>");
            xmlSalida.append(fila.getCostetotalrealFin() != null && !"".equals(fila.getCostetotalrealFin().toString()) ? fila.getCostetotalrealFin().toString().replace(".", ",") : "");
            xmlSalida.append("</COSTEREALFIN>");
            xmlSalida.append("<SUBCONCEDIDAFIN>");
            xmlSalida.append(fila.getSubvconcedidalanFin() != null && !"".equals(fila.getSubvconcedidalanFin().toString()) ? fila.getSubvconcedidalanFin().toString().replace(".", ",") : "");
            xmlSalida.append("</SUBCONCEDIDAFIN>");
            xmlSalida.append("<SISTGRANTIAJUVE_FIN>");
            xmlSalida.append(fila.getSistGrantiaJuveFin() != null && !"".equals(fila.getSistGrantiaJuveFin().toString()) ? fila.getSistGrantiaJuveFin().toString().replace(".", ",") : "");
            xmlSalida.append("</SISTGRANTIAJUVE_FIN>");
            xmlSalida.append("<DESC_SISTGRANTIAJUVE_FIN>");
            xmlSalida.append(fila.getDescSistGrantiaJuveFin() != null ? getDescripcionDesplegable(request, fila.getDescSistGrantiaJuveFin()) : "");
            xmlSalida.append("</DESC_SISTGRANTIAJUVE_FIN>");                
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("obtenerXmlSalida xmlSalida = " + xmlSalida);
        return xmlSalida.toString();
    }

    public void generarExcelGEL(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>> ENTRA en generarExcelGEL de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;
        List<FilaContratacionVO> listaContrataciones = null;
        Connection con = null;
        try {
            listaContrataciones = (List<FilaContratacionVO>) request.getAttribute("listaContrataciones");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) {
                        idioma = usuario.getIdioma();
                    }
                }
            } catch (Exception ex) {
                log.error("Error recuperando información de sesión: " + ex);
            }

            MeLanbide82I18n meLanbide82I18n = MeLanbide82I18n.getInstance();
            MeLanbide82Manager manager = MeLanbide82Manager.getInstance();
            numExpediente = request.getParameter("numExp");
            try {
                listaContrataciones = manager.getListaContrataciones(numExpediente, codOrganizacion, adapt);
                if (!listaContrataciones.isEmpty()) {
                    // Obtener valores adicionales
                    listaContrataciones = obtenerDescripciones(listaContrataciones, request);
                    final NuevosAtributosFilaContrVO nuevosAtributosFilaContrVO = new NuevosAtributosFilaContrVO();
                    nuevosAtributosFilaContrVO.setCamposFormularioFechas(MeLanbide82Manager.getInstance().getValoresFechas(numExpediente, adapt));
                    nuevosAtributosFilaContrVO.setCamposFormularioFicheros(MeLanbide82Manager.getInstance().getFicheros(numExpediente, adapt));
                    // Actualiza la lista de contrataciones con las fechas y ficheros
                    iterListaContrataciones(listaContrataciones, nuevosAtributosFilaContrVO);
                } else {
                    meLanbide82I18n.getMensaje(idioma, "error.errorGen");
                }
            } catch (Exception ex) {
                log.error("Error al recuperar datos de MeLanbide82");
            }

            FileInputStream istr = null;

            try {
                HSSFWorkbook libro = new HSSFWorkbook();

                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short) 200);

                int numFila = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                HSSFSheet hoja = libro.createSheet("Datos MeLanbide82");

                // Configurar el ancho de las columnas
                hoja.setColumnWidth(0, 3000); // Prioridad
                hoja.setColumnWidth(1, 15000); // Denominación del puesto
                hoja.setColumnWidth(2, 10000); // Nivel de Cualificación
                hoja.setColumnWidth(3, 10000); // Modalidad de Contrato
                hoja.setColumnWidth(4, 4000); // Duración Contrato en Meses
                hoja.setColumnWidth(5, 18000); // Grupo Cotización
                hoja.setColumnWidth(6, 5000); // Coste Salarial
                hoja.setColumnWidth(7, 5000); // Subvención Solicitada
                hoja.setColumnWidth(8, 9000); // Municipio
                hoja.setColumnWidth(9, 10000); // Nombre
                hoja.setColumnWidth(10, 10000); // Primer Apellido
                hoja.setColumnWidth(11, 10000); // Segundo Apellido
                hoja.setColumnWidth(12, 3000); // DNI/NIE
                hoja.setColumnWidth(13, 9000); // Documento CV Intermediacion
                hoja.setColumnWidth(14, 6000); // Fecha CV Intermediacion
                hoja.setColumnWidth(15, 9000); // Documento Demanda Intermediacion  
                hoja.setColumnWidth(16, 6000); // Fecha Demanda Intermediacion
                hoja.setColumnWidth(17, 4000); //Fecha de Nacimiento
                hoja.setColumnWidth(18, 3000); // Sexo
                hoja.setColumnWidth(19, 10000); // Nivel de Cualificación
                hoja.setColumnWidth(20, 15000); // Puesto de Trabajo
                hoja.setColumnWidth(21, 4000); // Número de Oferta
                hoja.setColumnWidth(22, 18000); // Grupo Cotización
                hoja.setColumnWidth(23, 4000); // Duración Contrato en Meses
                hoja.setColumnWidth(24, 4000); // Fecha de Inicio Contrato
                hoja.setColumnWidth(25, 3000); // Edad
                hoja.setColumnWidth(26, 4000); // Retribución Bruta
                hoja.setColumnWidth(27, 9000); // Municipio (Fin)                
                hoja.setColumnWidth(28, 10000); // Nombre (Fin)
                hoja.setColumnWidth(29, 10000); // Primer Apellido (Fin)
                hoja.setColumnWidth(30, 10000); // Segundo Apellido (Fin)
                hoja.setColumnWidth(31, 3000); // DNI/NIE (Fin)
                hoja.setColumnWidth(32, 3000); // Sexo (Fin)
                hoja.setColumnWidth(33, 5000); // Grupo Cotización (Fin)
                hoja.setColumnWidth(34, 5000); // Duración Contrato en Meses (Fin)
                hoja.setColumnWidth(35, 4000); // Fecha de Inicio (Fin)
                hoja.setColumnWidth(36, 4000); // Fecha de Fin (Fin)
                hoja.setColumnWidth(37, 5000); // Retribución Bruta (Fin)
                hoja.setColumnWidth(38, 5000); // Coste Salarial (Fin)
                hoja.setColumnWidth(39, 5000); // Coste Seguridad Social (Fin)
                hoja.setColumnWidth(40, 5000); // Indemnización Fin Contrato
                hoja.setColumnWidth(41, 5000); // Coste Total Real
                hoja.setColumnWidth(42, 5000); // Subvención Concedida

                fila = hoja.createRow(numFila);
                fila.setHeight((short) 750);

                crearEstiloInformeDatosGEL(libro, fila, celda, estiloCelda, idioma);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

                // Insertar los datos en las filas
                for (FilaContratacionVO filaContratacion : listaContrataciones) {
                    numFila++;
                    fila = hoja.createRow(numFila);

                    // COLUMNA: Prioridad
                    celda = fila.createCell(0);
                    celda.setCellValue(filaContratacion.getPrioridad() != null ? filaContratacion.getPrioridad() : 0);
                    celda.setCellStyle(estiloCelda);
                    // COLUMNA: Denominación del puesto
                    celda = fila.createCell(1);
                    celda.setCellValue(filaContratacion.getDenomPuesto() != null ? filaContratacion.getDenomPuesto() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Nivel de Cualificación
                    celda = fila.createCell(2);
                    celda.setCellValue(filaContratacion.getDescNivelcualificacion() != null ? getDescripcionDesplegable(request, filaContratacion.getDescNivelcualificacion()) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Modalidad de Contrato
                    celda = fila.createCell(3);
                    celda.setCellValue(filaContratacion.getModContrato() != null ? filaContratacion.getModContrato() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Duración Contrato en Meses
                    celda = fila.createCell(4);
                    celda.setCellValue(filaContratacion.getDescDurContrato() != null ? getDescripcionDesplegable(request, filaContratacion.getDescDurContrato()) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Grupo Cotización
                    celda = fila.createCell(5);
                    celda.setCellValue(filaContratacion.getDescGrupoCotiz() != null ? getDescripcionDesplegable(request, filaContratacion.getDescGrupoCotiz()) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Coste Salarial
                    celda = fila.createCell(6);
                    celda.setCellValue(filaContratacion.getCostesalarial() != null ? filaContratacion.getCostesalarial() : 0.0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Subvención Solicitada
                    celda = fila.createCell(7);
                    celda.setCellValue(filaContratacion.getSubvsolicitada() != null ? filaContratacion.getSubvsolicitada() : 0.0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Municipio
                    celda = fila.createCell(8);
                    celda.setCellValue(filaContratacion.getMunicipioInicio() != null ? filaContratacion.getMunicipioInicio() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Nombre
                    celda = fila.createCell(9);
                    celda.setCellValue(filaContratacion.getNombreInicio() != null ? filaContratacion.getNombreInicio() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Primer Apellido
                    celda = fila.createCell(10);
                    celda.setCellValue(filaContratacion.getApellido1Inicio() != null ? filaContratacion.getApellido1Inicio() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Segundo Apellido
                    celda = fila.createCell(11);
                    celda.setCellValue(filaContratacion.getApellido2Inicio() != null ? filaContratacion.getApellido2Inicio() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: DNI/NIE
                    celda = fila.createCell(12);
                    celda.setCellValue(filaContratacion.getDninieInicio() != null ? filaContratacion.getDninieInicio() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Documento CV
                    celda = fila.createCell(13);
                    celda.setCellValue(filaContratacion.getCv2() != null ? filaContratacion.getCv2() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Fecha Documento CV
                    celda = fila.createCell(14);
                    if (filaContratacion.getFechaCv2() != null) {
                        celda.setCellValue(dateFormat.format(filaContratacion.getFechaCv2()));
                    } else {
                        celda.setCellValue("");
                    }
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Demanda 
                    celda = fila.createCell(15);
                    celda.setCellValue(filaContratacion.getDemanda2() != null ? filaContratacion.getDemanda2() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Fecha Demanda
                    celda = fila.createCell(16);
                    if (filaContratacion.getFechaDemanda2() != null) {
                        celda.setCellValue(dateFormat.format(filaContratacion.getFechaDemanda2()));
                    } else {
                        celda.setCellValue("");
                    }
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Fecha de Nacimiento
                    celda = fila.createCell(17);
                    if (filaContratacion.getFechanacimientoInicio() != null) {
                        celda.setCellValue(dateFormat.format(filaContratacion.getFechanacimientoInicio()));
                    } else {
                        celda.setCellValue("");
                    }
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Sexo
                    celda = fila.createCell(18);
                    celda.setCellValue(filaContratacion.getDescSexoInicio() != null ? getDescripcionDesplegable(request, getDescripcionDesplegable(request, filaContratacion.getDescSexoInicio())) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Nivel de Cualificación
                    celda = fila.createCell(19);
                    celda.setCellValue(filaContratacion.getDescNivelcualificacionInicio() != null ? getDescripcionDesplegable(request, filaContratacion.getDescNivelcualificacionInicio()) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Puesto de Trabajo
                    celda = fila.createCell(20);
                    celda.setCellValue(filaContratacion.getPuestotrabajoInicio() != null ? filaContratacion.getPuestotrabajoInicio() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Número de Oferta
                    celda = fila.createCell(21);
                    celda.setCellValue(filaContratacion.getNofertaInicio() != null ? filaContratacion.getNofertaInicio() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Grupo Cotización
                    celda = fila.createCell(22);
                    celda.setCellValue(filaContratacion.getDescGrupocotizInicio() != null ? getDescripcionDesplegable(request, filaContratacion.getDescGrupocotizInicio()) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Duración Contrato en Meses (Fin)
                    celda = fila.createCell(23);
                    celda.setCellValue(filaContratacion.getDurcontratoInicio() != null ? filaContratacion.getDurcontratoInicio() : 0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Fecha de Inicio Contrato
                    celda = fila.createCell(24);
                    if (filaContratacion.getFechainicioInicio() != null) {
                        celda.setCellValue(dateFormat.format(filaContratacion.getFechainicioInicio()));
                    } else {
                        celda.setCellValue("");
                    }
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Edad
                    celda = fila.createCell(25);
                    celda.setCellValue(filaContratacion.getEdadInicio() != null ? filaContratacion.getEdadInicio() : 0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Retribución Bruta
                    celda = fila.createCell(26);
                    celda.setCellValue(filaContratacion.getRetribucionbrutaInicio() != null ? filaContratacion.getRetribucionbrutaInicio() : 0.0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Municipio (Fin)
                    celda = fila.createCell(27);
                    celda.setCellValue(filaContratacion.getMunicipioFin() != null ? filaContratacion.getMunicipioFin() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Nombre (Fin)
                    celda = fila.createCell(28);
                    celda.setCellValue(filaContratacion.getNombreFin() != null ? filaContratacion.getNombreFin() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Primer Apellido (Fin)
                    celda = fila.createCell(29);
                    celda.setCellValue(filaContratacion.getApellido1Fin() != null ? filaContratacion.getApellido1Fin() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Segundo Apellido (Fin)
                    celda = fila.createCell(30);
                    celda.setCellValue(filaContratacion.getApellido2Fin() != null ? filaContratacion.getApellido2Fin() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: DNI/NIE (Fin)
                    celda = fila.createCell(31);
                    celda.setCellValue(filaContratacion.getDninieFin() != null ? filaContratacion.getDninieFin() : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Sexo (Fin)
                    celda = fila.createCell(32);
                    celda.setCellValue(filaContratacion.getDescSexoFin() != null ? getDescripcionDesplegable(request, filaContratacion.getDescSexoFin()) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Grupo Cotización (Fin)
                    celda = fila.createCell(33);
                    celda.setCellValue(filaContratacion.getDescGrupocotizFin() != null ? getDescripcionDesplegable(request, filaContratacion.getDescGrupocotizFin()) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Duración Contrato en Meses (Fin)
                    celda = fila.createCell(34);
                    celda.setCellValue(filaContratacion.getDurcontratoFin() != null ? filaContratacion.getDurcontratoFin() : 0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Fecha de Incio (Fin)
                    celda = fila.createCell(35);
                    if (filaContratacion.getFechainicioFin() != null) {
                        celda.setCellValue(dateFormat.format(filaContratacion.getFechainicioFin()));
                    } else {
                        celda.setCellValue("");
                    }
                    celda.setCellStyle(estiloCelda);
                    // COLUMNA: Fecha de Fin (Fin)
                    celda = fila.createCell(36);
                    if (filaContratacion.getFechafinFin() != null) {
                        celda.setCellValue(dateFormat.format(filaContratacion.getFechafinFin()));
                    } else {
                        celda.setCellValue("");
                    }
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Retribución Bruta (Fin)
                    celda = fila.createCell(37);
                    celda.setCellValue(filaContratacion.getRetribucionbrutaFin() != null ? filaContratacion.getRetribucionbrutaFin() : 0.0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Coste Salarial (Fin)
                    celda = fila.createCell(38);
                    celda.setCellValue(filaContratacion.getCostesalarialFin() != null ? filaContratacion.getCostesalarialFin() : 0.0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Coste Seguridad Social (Fin)
                    celda = fila.createCell(39);
                    celda.setCellValue(filaContratacion.getCostesssFin() != null ? filaContratacion.getCostesssFin() : 0.0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Indemnización Fin Contrato
                    celda = fila.createCell(40);
                    celda.setCellValue(filaContratacion.getIndemfincontratoFin() != null ? filaContratacion.getIndemfincontratoFin() : 0.0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Coste Total Real
                    celda = fila.createCell(41);
                    celda.setCellValue(filaContratacion.getCostetotalrealFin() != null ? filaContratacion.getCostetotalrealFin() : 0.0);
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: Subvención Concedida
                    celda = fila.createCell(42);
                    celda.setCellValue(filaContratacion.getSubvconcedidalanFin() != null ? filaContratacion.getSubvconcedidalanFin() : 0.0);
                    celda.setCellStyle(estiloCelda);

                }
                // Guardar el archivo en un directorio temporal
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenDatosMeLanbide82", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream(istr);

                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException ioe) {
                log.error("EXCEPCION informe resumenDatosMeLanbide82");

            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe resumenDatosMeLanbide82");
        } catch (SQLException ex) {
            log.error("EXCEPCION informe resumenDatosMeLanbide82");
        }
    }

    private void crearEstiloInformeDatosGEL(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info(">>>> ENTRA en crearEstiloInformeDatosGEL de " + this.getClass().getSimpleName());
        try {
            MeLanbide82I18n meLanbide82I18n = MeLanbide82I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                palette.getColor(HSSFColor.GREY_50_PERCENT.index);

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);

                // Crear estilos de celda y cabeceras
                //PUESTO
                HSSFCellStyle estiloTexto = libro.createCellStyle();
                estiloTexto.setDataFormat(libro.createDataFormat().getFormat("@")); // '@' para que Excel lo trate como texto

                // COLUMNA: Prioridad
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "label.prioridad").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Denominación del puesto
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "label.denomPuesto").toUpperCase());
                celda.setCellStyle(estiloCelda);

                //DETTALLE DEL PUESTO
                // COLUMNA: Nivel de Cualificación
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excel.nivelCualificacion").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Modalidad de Contrato
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excel.modContrato").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Duración del Contrato (Meses)
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excel.durContrato").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Grupo de Cotización
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excel.grupoCotiz").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Coste Salarial
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excel.costeSalarial").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Subvención Solicitada
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excel.subvSolicitada").toUpperCase());
                celda.setCellStyle(estiloCelda);

                //Persona Contratada
                // COLUMNA: Municipio
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.municipio").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Nombre
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.nombre").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Primer Apellido
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.apellido1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Segundo Apellido
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.apellido2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: DNI/NIE
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.dniNie").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Documento CV Intermediacion
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.cv2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Fecha CV Intermediacion
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(14);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.fechaCv2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Documento Demanda Intermediacion
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(15);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.demanda2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Fecha Demanda Intermediacion
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(16);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.fechaDemanda2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Fecha de nacimiento
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(17);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.fechaNacimiento").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Sexo
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.sexo").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Nivel de Cualificación
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(19);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.nivelCualificacion").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Puesto de Trabajo
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(20);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.puestoTrabajo").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Oferta
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(21);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.nOferta").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Grupo de Cotizacion
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(22);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.grupoCotiz").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Duración del Contrato (Meses)
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(23);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.durContrato").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Fecha de Inicio Contrato
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.fechaInicio").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Edad
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(25);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.edad").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Retribución Bruta
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(26);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelINI.retribucionBruta").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Municipio
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(27);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.municipio").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Nombre
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(28);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.nombre").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Primer Apellido
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(29);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.apellido1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Segundo Apellido
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(30);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.apellido2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: DNI/NIE
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(31);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.dniNie").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Sexo
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(32);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.sexo").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Grupo de Cotizacion
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(33);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.grupoCotiz").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Duración del Contrato (Meses)
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(34);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.durContrato").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Fecha de Inicio
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(35);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.fechaInicio").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Fecha de Fin Contrato
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(36);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.fechaFin").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Retribución Bruta
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(37);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.retribucionBruta").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Coste Salarial
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(38);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.costeSalarial").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Coste Seguridad Social
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(39);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.costesSs").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Indemnización Fin Contrato
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(40);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.indemFinContrato").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Coste Total Real
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(41);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.costeTotalReal").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // COLUMNA: Subvención Concedida
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(42);
                celda.setCellValue(meLanbide82I18n.getMensaje(idioma, "excelFIN.subvConcedida").toUpperCase());
                celda.setCellStyle(estiloCelda);

            } catch (Exception e) {
                log.error("Error en crearEstiloInformeDatosMeLanbide82: " + e);
            }
        } catch (Exception ex) {
            log.error("Error general en crearEstiloInformeDatosMeLanbide82: " + ex);
        }
    }

}
