package es.altia.flexia.integracion.moduloexterno.melanbide80;

import es.altia.agora.business.escritorio.UsuarioValueObject;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide80.i18n.MeLanbide80I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide80.manager.MeLanbide80Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConstantesMeLanbide80;
import es.altia.flexia.integracion.moduloexterno.melanbide80.vo.PersonaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide80.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.ConstantesGestionErroresDokusi;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
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
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.util.ConstantesMeLanbide_Dokusi;
import es.altia.util.commons.MimeTypes;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;

public class MELANBIDE80 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE80.class);
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

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
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide80/melanbide80.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<PersonaVO> listaPersonas = MeLanbide80Manager.getInstance().getDatosPersona(numExpediente, codOrganizacion, adapt);
                if (listaPersonas.size() > 0) {
                    request.setAttribute("listaPersonas", listaPersonas);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos De Personas - MELANBIDE80 - cargarPantallaPrincipal", ex);
            }
        }

        return url;
    }

    private List<DesplegableAdmonLocalVO> traducirDesplegable(HttpServletRequest request, List<DesplegableAdmonLocalVO> desplegable) {

        for (DesplegableAdmonLocalVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().equals("")) {
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }

        return desplegable;
    }

    public String cargarNuevaPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String urlnuevaPersona = "/jsp/extension/melanbide80/nuevaPersona.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp").toString();
                request.setAttribute("numExp", numExp);
            }
            //Cargamos el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaTipContA = MeLanbide80Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide80.COD_DES_CTRA, ConstantesMeLanbide80.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide80Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide80.COD_DES_JORN, ConstantesMeLanbide80.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaSituacion = MeLanbide80Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide80.COD_DES_ERTE, ConstantesMeLanbide80.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            if (listaTipContA.size() > 0) {
                listaTipContA = traducirDesplegable(request, listaTipContA);
                request.setAttribute("listaTipcontA", listaTipContA);
            }
            if (listaJornada.size() > 0) {
                listaJornada = traducirDesplegable(request, listaJornada);
                request.setAttribute("listaJornada", listaJornada);
            }
            if (listaSituacion.size() > 0) {
                listaSituacion = traducirDesplegable(request, listaSituacion);
                request.setAttribute("listaSituacion", listaSituacion);
            }

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva persona : " + ex.getMessage());
        }
        return urlnuevaPersona;
    }

    public String cargarModificarPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevaPersona = "/jsp/extension/melanbide80/nuevaPersona.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            // Recuperramos datos de Persona a modificar y cargamos en el request
            if (id != null && !id.equals("")) {
                PersonaVO datModif = MeLanbide80Manager.getInstance().getPersonaPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            //Cargamos el el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaTipContA = MeLanbide80Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide80.COD_DES_CTRA, ConstantesMeLanbide80.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide80Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide80.COD_DES_JORN, ConstantesMeLanbide80.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            List<DesplegableAdmonLocalVO> listaSituacion = MeLanbide80Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide80.COD_DES_ERTE, ConstantesMeLanbide80.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            if (listaTipContA.size() > 0) {
                listaTipContA = traducirDesplegable(request, listaTipContA);
                request.setAttribute("listaTipcontA", listaTipContA);
            }
            if (listaJornada.size() > 0) {
                listaJornada = traducirDesplegable(request, listaJornada);
                request.setAttribute("listaJornada", listaJornada);
            }
            if (listaSituacion.size() > 0) {
                listaSituacion = traducirDesplegable(request, listaSituacion);
                request.setAttribute("listaSituacion", listaSituacion);
            }

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return urlnuevaPersona;

    }

    public void eliminarPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        String numExp = "";
        try {
            String id = (String) request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Persona a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp").toString();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide80Manager.getInstance().eliminarPersona(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide80Manager.getInstance().getDatosPersona(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.debug("Error al recuperar la lista de personas después de eliminar una Persona");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una Persona: " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaPersona(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void crearNuevaPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        PersonaVO nuevaPersona = new PersonaVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = (String) request.getParameter("expediente");

            String dni = (String) request.getParameter("dni");
            String nombre = (String) request.getParameter("nombre");
            String apel1 = (String) request.getParameter("apel1");
            String apel2 = (String) request.getParameter("apel2");
            String tipcontA = (String) request.getParameter("tipcontA");
            String tipcontB = (String) request.getParameter("tipcontB").replace(",", ".");
            String jornada = (String) request.getParameter("jornada");
            String porjorpar = (String) request.getParameter("porjorpar").replace(",", ".");
            String situacion = (String) request.getParameter("situacion");
            String reducjorn = (String) request.getParameter("reducjorn").replace(",", ".");
            String fecinisit = (String) request.getParameter("fecinisit");
            String fecfinsit = (String) request.getParameter("fecfinsit");
            String numdiasit = (String) request.getParameter("numdiasit");
            String baseregul = (String) request.getParameter("baseregul").replace(",", ".");
            String impprest = (String) request.getParameter("impprest").replace(",", ".");
            String complsal = (String) request.getParameter("complsal").replace(",", ".");
            String impsubvsol = (String) request.getParameter("impsubvsol").replace(",", ".");

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            nuevaPersona.setNumExp(numExp);

            nuevaPersona.setDni(dni);
            nuevaPersona.setNombre(nombre);
            nuevaPersona.setApel1(apel1);
            nuevaPersona.setApel2(apel2);
            nuevaPersona.setTipcontA(tipcontA);
            if (tipcontB != null && !"".equals(tipcontB)) {
                nuevaPersona.setTipcontB(Double.parseDouble(tipcontB));
            }
            nuevaPersona.setJornada(jornada);
            if (porjorpar != null && !"".equals(porjorpar)) {
                nuevaPersona.setPorjorpar(Double.parseDouble(porjorpar));
            }
            nuevaPersona.setSituacion(situacion);
            if (reducjorn != null && !"".equals(reducjorn)) {
                nuevaPersona.setReducjorn(Double.parseDouble(reducjorn));
            }
            if (fecinisit != null && !"".equals(fecinisit)) {
                nuevaPersona.setFecinisit(new java.sql.Date(formatoFecha.parse(fecinisit).getTime()));
            }
            if (fecfinsit != null && !"".equals(fecfinsit)) {
                nuevaPersona.setFecfinsit(new java.sql.Date(formatoFecha.parse(fecfinsit).getTime()));
            }
            if (numdiasit != null && !"".equals(numdiasit)) {
                nuevaPersona.setNumdiasit(Integer.parseInt(numdiasit));
            }
            if (baseregul != null && !"".equals(baseregul)) {
                nuevaPersona.setBaseregul(Double.parseDouble(baseregul));
            }
            if (impprest != null && !"".equals(impprest)) {
                nuevaPersona.setImpprest(Double.parseDouble(impprest));
            }
            if (complsal != null && !"".equals(complsal)) {
                nuevaPersona.setComplsal(Double.parseDouble(complsal));
            }
            if (impsubvsol != null && !"".equals(impsubvsol)) {
                nuevaPersona.setImpsubvsol(Double.parseDouble(impsubvsol));
            }

            MeLanbide80Manager meLanbide80Manager = MeLanbide80Manager.getInstance();
            boolean insertOK = meLanbide80Manager.crearNuevaPersona(nuevaPersona, adapt);
            if (insertOK) {
                log.debug("Persona Insertada Correctamente");
                lista = meLanbide80Manager.getDatosPersona(numExp, codOrganizacion, adapt);

            } else {
                log.debug("NO se ha insertado correctamente la nueva persona");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto PersonaVO" + ex.getMessage());
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaPersona(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<PersonaVO> lista = new ArrayList<PersonaVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("expediente");

            String dni = (String) request.getParameter("dni");
            String nombre = (String) request.getParameter("nombre");
            String apel1 = (String) request.getParameter("apel1");
            String apel2 = (String) request.getParameter("apel2");
            String tipcontA = (String) request.getParameter("tipcontA");
            String tipcontB = (String) request.getParameter("tipcontB").replace(",", ".");
            String jornada = (String) request.getParameter("jornada");
            String porjorpar = (String) request.getParameter("porjorpar").replace(",", ".");
            String situacion = (String) request.getParameter("situacion");
            String reducjorn = (String) request.getParameter("reducjorn").replace(",", ".");
            String fecinisit = (String) request.getParameter("fecinisit");
            String fecfinsit = (String) request.getParameter("fecfinsit");
            String numdiasit = (String) request.getParameter("numdiasit");
            String baseregul = (String) request.getParameter("baseregul").replace(",", ".");
            String impprest = (String) request.getParameter("impprest").replace(",", ".");
            String complsal = (String) request.getParameter("complsal").replace(",", ".");
            String impsubvsol = (String) request.getParameter("impsubvsol").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Persona a Modificar ");
                codigoOperacion = "3";
            } else {
                PersonaVO datModif = MeLanbide80Manager.getInstance().getPersonaPorID(id, adapt);
                numExp = datModif.getNumExp();
                datModif.setId(Integer.parseInt(id));

                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

                datModif.setNumExp(numExp);

                datModif.setDni(dni);
                datModif.setNombre(nombre);
                datModif.setApel1(apel1);
                datModif.setApel2(apel2);
                datModif.setTipcontA(tipcontA);
                if (tipcontB != null && !"".equals(tipcontB)) {
                    datModif.setTipcontB(Double.parseDouble(tipcontB));
                }
                datModif.setJornada(jornada);
                if (porjorpar != null && !"".equals(porjorpar)) {
                    datModif.setPorjorpar(Double.parseDouble(porjorpar));
                }
                datModif.setSituacion(situacion);
                if (reducjorn != null && !"".equals(reducjorn)) {
                    datModif.setReducjorn(Double.parseDouble(reducjorn));
                }
                if (fecinisit != null && !"".equals(fecinisit)) {
                    datModif.setFecinisit(new java.sql.Date(formatoFecha.parse(fecinisit).getTime()));
                }
                if (fecfinsit != null && !"".equals(fecfinsit)) {
                    datModif.setFecfinsit(new java.sql.Date(formatoFecha.parse(fecfinsit).getTime()));
                }
                if (numdiasit != null && !"".equals(numdiasit)) {
                    datModif.setNumdiasit(Integer.parseInt(numdiasit));
                }
                if (baseregul != null && !"".equals(baseregul)) {
                    datModif.setBaseregul(Double.parseDouble(baseregul));
                }
                if (impprest != null && !"".equals(impprest)) {
                    datModif.setImpprest(Double.parseDouble(impprest));
                }
                if (complsal != null && !"".equals(complsal)) {
                    datModif.setComplsal(Double.parseDouble(complsal));
                }
                if (impsubvsol != null && !"".equals(impsubvsol)) {
                    datModif.setImpsubvsol(Double.parseDouble(impsubvsol));
                }

                MeLanbide80Manager meLanbide80Manager = MeLanbide80Manager.getInstance();
                boolean modOK = meLanbide80Manager.modificarPersona(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide80Manager.getDatosPersona(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Personas después de Modificar una persona : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de Personas después de Modificar una persona : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }

        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaPersona(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public String cargarXMLsolicitud(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en cargarXMLsolicitud" + this.getClass().getSimpleName());
        int codIdioma = 1;
        String codigoOperacion = "0";
        // buscar oid
        String oidDocumento = "";
//        String idProcedimiento = "";
        byte[] fichero = null;
        String mimeTypeContent = "";
        String nombreFichero = "";
        String extensionFichero = "";
        String numExp = "";
        try {
            numExp = request.getParameter("numero").toString();
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
            MeLanbide80Manager meLanbide80Manager = MeLanbide80Manager.getInstance();
            oidDocumento = meLanbide80Manager.getOidDocumento(numExp, adapt);
            if (oidDocumento != null && !oidDocumento.equalsIgnoreCase("")) {
//                idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide_Dokusi.PREFIJO_ID_PROC + "AEXCE", ConstantesMeLanbide_Dokusi.FICHERO_PROPIEDADES);
//                if (idProcedimiento == null || idProcedimiento.equals("")) {
//                    log.error("No se ha recueprado correctamente el codigo del procedimiento para platea del MELANBIDE_DOKUSI.properties : CEESC");
//                    throw new Exception("Codigo Procedimiento de Platea no recuperado del Properties. CEESC");
//                }
                log.info("Instanciamos el Servicio de Dokusi : Lan6DokusiServicios servicios = new Lan6DokusiServicios(AEXCE);");
                Lan6DokusiServicios servicios = new Lan6DokusiServicios("AEXCE");
                Lan6Documento lan6Documento = new Lan6Documento();
                try {
                    lan6Documento = servicios.consultarDocumento(oidDocumento);
                    fichero = lan6Documento.getContenido();
                    mimeTypeContent = getMimeTypeFromExtension(lan6Documento.getFormat());
                    nombreFichero = lan6Documento.getNombre();
                    extensionFichero = ConstantesDatos.DOT + lan6Documento.getFormat();
                    log.debug("Fichero: " + nombreFichero + "-" + extensionFichero + " - " + fichero.toString());

                } catch (Lan6Excepcion ex) {
                    log.error("Error Lan6Excepcion  al consultar documento externo en Dokusi", ex);
                    throw new Exception(ConstantesGestionErroresDokusi.COD_FLEX_ERROR_GETDOC_EXT);
                }
                if (fichero != null) {
                    String xmlSt = new String(fichero);
                    cargarExpedienteExtension(codOrganizacion, numExpediente, xmlSt);
                    request.getSession().setAttribute("mensajeImportar", MeLanbide80I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
                } else {
                    log.error("FICHERO NULO EN descargarDocumentoDokusiGError - No se ha recuperado ningun documento de Dokusi");
                    request.getSession().setAttribute("mensajeImportar", MeLanbide80I18n.getInstance().getMensaje(codIdioma, "error.errorImportarOid"));
                }

            }
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE80.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE80.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE80.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BDException bde) {
            codigoOperacion = "1";
            log.error("Error de tipo BD al recuperar la lista de Personas después de cargar el XML : " + bde.getMensaje());
            request.getSession().setAttribute("mensajeImportar", MeLanbide80I18n.getInstance().getMensaje(codIdioma, "error.errorBD"));
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide80I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarListaPersonas.jsp");
        return "/jsp/extension/melanbide80/recargarListaPersonas.jsp";
    }

    public String procesarXML(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en procesarXML" + this.getClass().getSimpleName());
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile file = table.get("fichero_xml");
            //Use commons-fileupload to obtain the byte[] of the file (in a servlet of yours)
            byte[] archivoComprimido = file.getFileData();
            String fileName = file.getFileName();
            log.debug("fileName: " + fileName);
            try {
                String xml = new String(archivoComprimido);
                log.debug("XML " + xml);
                if (!("").equals(xml)) {
                    cargarExpedienteExtension(codOrganizacion, numExpediente, xml);
                }
            } catch (DataFormatException ex) {
                java.util.logging.Logger.getLogger(MELANBIDE80.class.getName()).log(Level.SEVERE, null, ex);
            }
            request.getSession().setAttribute("mensajeImportar", MeLanbide80I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE80.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE80.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE80.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide80I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarListaPersonas.jsp");
        return "/jsp/extension/melanbide80/recargarListaPersonas.jsp";
    }

    public void getListaRegistros(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en getListaRegistros de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        String numExp = "";
        try {
            numExp = request.getParameter("numExp").toString();
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide80Manager.getInstance().getDatosPersona(numExp, codOrganizacion, adapt);
            if (lista.isEmpty()) {
                log.error("No se ha cargado ningún registro para el expediente " + numExp + " después de cargar el XML");
                codigoOperacion = "3";
            }
        } catch (BDException bde) {
            codigoOperacion = "1";
            log.error("Error de tipo BD al recuperar la lista de Personas después de cargar el XML : " + bde.getMensaje());
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error al recuperar la lista de Personas después de cargar el XML : " + ex.getMessage());
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaPersona(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void generarExcelAnexo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en generarExcelAnexo de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        List<PersonaVO> lista = null;
        try {
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

            }

            MeLanbide80I18n meLanbide80I18n = MeLanbide80I18n.getInstance();
            MeLanbide80Manager manager = MeLanbide80Manager.getInstance();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            DecimalFormat formatoNumero = new DecimalFormat("##,##0.00");
            String numExp = "";
            double rendimientoD = 1.00;
            double jornadaD = 1.00;
            double reduccionD = 1.00;
            double baseD = 0.00;
            double complementoD = 0.00;
            double calculoImp = 0.00;
            double subvMax = 0.00;
            double aConceder = 0.00;
            double totalConceder = 0.0D;
            Date fechaIni = null;
            Date fechaFin = null;
            try {
                numExp = request.getParameter("numExp").toString();
                try {
                    lista = manager.getDatosPersona(numExp, codOrganizacion, adapt);
                    if (lista.isEmpty()) {
                        meLanbide80I18n.getMensaje(idioma, "error.errorGen");
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos del Anexo ");
                }
            } catch (Exception ex) {
                log.error("Error recuperando datos de Anexo : " + ex);
                codigoOperacion = "1";
            }
            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();
                Map<String, CellStyle> estilos = crearEstilos(libro);
                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                    log.error("Error findColor : " + e);
                    codigoOperacion = "1";
                }

                int numFila = 0;
                //int numFila2 = 0;
                int contCelda = 0;
                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFSheet hoja = libro.createSheet("Personas con discapacidad");

                //Se establece el ancho de cada columnas
                hoja.setColumnWidth(0, 1500);//blanco               A
                hoja.setColumnWidth(1, 14 * 256);//dni                  B
                hoja.autoSizeColumn(2);//nombre               C
                hoja.setColumnWidth(3, 6000);//apellidos            D
                hoja.setColumnWidth(4, 3000);//tipo contrato        E
                hoja.setColumnWidth(5, 3700);// % rendimiento       F
                hoja.setColumnWidth(6, 3000);//jornada              G
                hoja.setColumnWidth(7, 4000);// % jornada           H
                hoja.setColumnWidth(8, 3000);// situacion           I
                hoja.setColumnWidth(9, 4000);// % reduccion Jornada J
                hoja.setColumnWidth(10, 3500);// fecha inicio       K
                hoja.setColumnWidth(11, 3500);// fecha fin          L
                hoja.setColumnWidth(12, 3000);// dias               M
                hoja.setColumnWidth(13, 4000);// base diaria        N
                hoja.setColumnWidth(14, 4000);// importe prestacion O
                hoja.setColumnWidth(15, 4000);// complemento        P
                hoja.setColumnWidth(16, 4000);// impo subv          Q
                hoja.setColumnWidth(17, 3000);// control dias       R
                hoja.setColumnWidth(18, 4000);// calculo O          S
                hoja.setColumnWidth(19, 4000);// subv maxima        T
                hoja.setColumnWidth(20, 4000);// imp conceder       U

                //Se establece el alto de las columnas   
                hoja.setDefaultRowHeight((short) 800);
                fila = hoja.createRow(numFila++);
                numFila++;
                fila = hoja.createRow(numFila);
                fila.setHeightInPoints(25);
                //celda en blanco
                hoja.setColumnWidth(contCelda, (short) 1500);
                celda = fila.createCell(contCelda++);

                hoja.setColumnWidth(contCelda, (short) 4000);
                celda = fila.createCell(contCelda++);
                celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.expediente"));
                celda.setCellStyle(estilos.get("estiloTitulo"));

                hoja.setColumnWidth(contCelda, (short) 4250);
                celda = fila.createCell(contCelda++);
                celda.setCellValue(numExp);
                celda.setCellStyle(estilos.get("estiloTitulo"));
                hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 2, 3)); // hoja.addMergedRegion(new CellRangeAddress(numFila,numFila,18,19));

                numFila++;
                numFila++;
                fila = hoja.createRow(numFila);

                crearCabecera(libro, fila, celda, idioma);
                log.debug("Insertamos los datos, fila a fila");
                int filaPrimera = numFila + 2;
                log.debug("Primera fila a sumar= " + filaPrimera);
                //Insertamos los datos, fila a fila
                int registros = lista.size();
                for (PersonaVO persona : lista) {
                    fechaFin = null;
                    numFila++;
                    fila = hoja.createRow(numFila);
                    log.debug("Fila: " + (numFila + 1));
                    //COLUMNA: dni  B
                    celda = fila.createCell(1);
                    celda.setCellValue(persona.getDni() != null ? String.valueOf(persona.getDni()) : "");
                    celda.setCellStyle(estilos.get("estiloCentrado"));

                    //COLUMNA: nombre   C
                    celda = fila.createCell(2);
                    celda.setCellValue(persona.getNombre() != null ? persona.getNombre().toUpperCase() : "");
                    celda.setCellStyle(estilos.get("estiloCelda"));

                    //COLUMNA: apellidos    D
                    celda = fila.createCell(3);
                    celda.setCellValue((persona.getApel1() != null ? persona.getApel1().toUpperCase() : "") + " " + (persona.getApel2() != null ? persona.getApel2().toUpperCase() : ""));
                    celda.setCellStyle(estilos.get("estiloCelda"));

                    //COLUMNA: tipo contrato    E
                    celda = fila.createCell(4);
                    celda.setCellValue(persona.getDesTipcontA() != null ? getDescripcionDesplegable(request, persona.getDesTipcontA()) : "");
                    celda.setCellStyle(estilos.get("estiloCentrado"));

                    //COLUMNA:  % rendimiento   F
                    celda = fila.createCell(5);
                    rendimientoD = (persona.getTipcontB() != null ? persona.getTipcontB() / 100 : 1.00);
                    celda.setCellValue(persona.getTipcontB() != null ? String.valueOf(formatoNumero.format(persona.getTipcontB() / 100)) : "1");
                    celda.setCellStyle(estilos.get("estiloCentrado"));

                    //COLUMNA: jornada  G
                    celda = fila.createCell(6);
                    celda.setCellValue(persona.getDesJornada() != null ? getDescripcionDesplegable(request, persona.getDesJornada()) : "");
                    celda.setCellStyle(estilos.get("estiloCentrado"));

                    //COLUMNA: % jornada    H
                    celda = fila.createCell(7);
                    jornadaD = (persona.getPorjorpar() != null ? persona.getPorjorpar() / 100 : 1.00);
                    celda.setCellValue(persona.getPorjorpar() != null ? String.valueOf(formatoNumero.format(persona.getPorjorpar() / 100)) : "1");
                    celda.setCellStyle(estilos.get("estiloCentrado"));

                    //COLUMNA: situacion    I
                    celda = fila.createCell(8);
                    celda.setCellValue(persona.getDesSituacion() != null ? getDescripcionDesplegable(request, persona.getDesSituacion()) : "");
                    celda.setCellStyle(estilos.get("estiloCentrado"));

                    //COLUMNA: % reduccion Jornada  J
                    celda = fila.createCell(9);
                    reduccionD = (persona.getReducjorn() != null ? persona.getReducjorn() / 100 : 1.00);
                    celda.setCellValue(persona.getReducjorn() != null ? String.valueOf(formatoNumero.format(persona.getReducjorn() / 100)) : "1");
                    celda.setCellStyle(estilos.get("estiloCentrado"));

                    //COLUMNA: fecha inicio K 
                    celda = fila.createCell(10);
                    if (persona.getFecinisit() != null) {
                        fechaIni = persona.getFecinisit();
                    }
                    celda.setCellValue(fechaIni);
                    celda.setCellStyle(estilos.get("estiloFecha"));

                    //COLUMNA: fecha fin    L
                    // 31/12/2020
                    celda = fila.createCell(11);
                    if (persona.getFecfinsit() != null) {
                        fechaFin = persona.getFecfinsit();
                    }
                    if (fechaFin != null) {
                        celda.setCellValue(fechaFin);
                    } else {
                        celda.setCellValue("31/12/2020");
                    }
                    celda.setCellStyle(estilos.get("estiloFecha"));

                    //COLUMNA:dias  M
                    celda = fila.createCell(12);
                    celda.setCellValue(persona.getNumdiasit() != null ? String.valueOf(persona.getNumdiasit()) : "");
                    celda.setCellStyle(estilos.get("estiloCentrado"));

                    //COLUMNA: base N
                    celda = fila.createCell(13);
                    baseD = (persona.getBaseregul() != null ? persona.getBaseregul() : 0.00);
                    celda.setCellValue(baseD);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    celda.setCellStyle(estilos.get("estiloImportes"));

                    //COLUMNA: prestacion   O
                    celda = fila.createCell(14);
                    celda.setCellValue(persona.getImpprest() != null ? (persona.getImpprest()) : 0.00);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    celda.setCellStyle(estilos.get("estiloImportes"));

                    //COLUMNA: complemento  P
                    celda = fila.createCell(15);
                    complementoD = (persona.getComplsal() != null ? persona.getComplsal() : 0.00);
                    celda.setCellValue(complementoD);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    celda.setCellStyle(estilos.get("estiloImportes"));

                    //COLUMNA: subvencion solicitada  Q
                    celda = fila.createCell(16);
                    celda.setCellValue(persona.getImpsubvsol() != null ? (persona.getImpsubvsol()) : 0.00);
                    celda.setCellStyle(estilos.get("estiloImportes"));

                    //COLUMNA: control dias  R                  
                    celda = fila.createCell(17);
                    // =DATEDIF(A2, B2, "d")
                    celda.setCellFormula("SUM(DATEDIF(K" + (numFila + 1) + ", L" + (numFila + 1) + ", \"d\")+1)");
                    celda.setCellStyle(estilos.get("estiloCentrado"));

                    //COLUMNA: calculo importe prestacion  S
                    //=SI(I5="IT VINCULADA A COVID-19";N5*0,75*R5;(SI(N5>52,29;36,6*J5*R5-(N5*J5*R5*0,7);0)+N5*J5*R5*0,7+SI(23,9>N5;23,9*0,7*J5*R5-(N5*J5*R5*0,7);0)))
                    String form = "IF(I" + (numFila + 1) + "=\"IT VINCULADA A COVID-19\",N" + (numFila + 1) + "*R" + (numFila + 1) + "*0.75,(IF(N" + (numFila + 1) + ">52.29,(36.6*J" + (numFila + 1) + "*R" + (numFila + 1) + ")-(N" + (numFila + 1) + "*J" + (numFila + 1) + "*R" + (numFila + 1) + "*0.7),0)+(N" + (numFila + 1) + "*J" + (numFila + 1) + "*R" + (numFila + 1) + "*0.7)+IF(23.9>N" + (numFila + 1) + ",(23.9*0.7*J" + (numFila + 1) + "*R" + (numFila + 1) + ")-(N" + (numFila + 1) + "*J" + (numFila + 1) + "*R" + (numFila + 1) + "*0.7),0)))";
                    celda = fila.createCell(18);
                    celda.setCellFormula(form);
                    celda.setCellStyle(estilos.get("estiloImportes"));

                    //COLUMNA: subvencion maxima  T
                    //=SI(P5>((475/30)*F5*H5*J5*R5);(475/30)*F5*H5*J5*R5;P5)
                    String form2 = "IF(P" + (numFila + 1) + ">((475/30)*F" + (numFila + 1) + "*H" + (numFila + 1) + "*J" + (numFila + 1) + "*R" + (numFila + 1) + "),(475/30)*F" + (numFila + 1) + "*H" + (numFila + 1) + "*J" + (numFila + 1) + "*R" + (numFila + 1) + ",P" + (numFila + 1) + ")";
                    celda = fila.createCell(19);
                    celda.setCellFormula(form2);
                    celda.setCellStyle(estilos.get("estiloImportes"));

                    //COLUMNA: importe a conceder U
                    //=SI(((S5+P5)/R5)>31,66*F5*H5*J5;T5;0)                    
                    celda = fila.createCell(20);
                    celda.setCellFormula("IF(((S" + (numFila + 1) + "+P" + (numFila + 1) + ")/R" + (numFila + 1) + ")>(31.66*F" + (numFila + 1) + "*H" + (numFila + 1) + "*J" + (numFila + 1) + "),T" + (numFila + 1) + ",0)");
                    celda.setCellStyle(estilos.get("estiloUltimaColumna"));
                    totalConceder = totalConceder + aConceder;

                }
                int filaUltima = numFila + 1;
                log.debug("Fin tabla - Ultima fila a sumar= " + filaUltima);

                numFila++;
                //Pinto la linea inferior de la tabla
                fila = hoja.createRow(numFila);
                for (int i = 1; i < 21; i++) {
                    celda = fila.createCell(i);
                    celda.setCellStyle(estilos.get("estiloUltimaFila"));
                }
                // total
                numFila++;
                fila = hoja.createRow(numFila);
                celda = fila.createCell(18);
                celda.setCellValue("TOTAL A CONCEDER:");
                celda.setCellStyle(estilos.get("estiloTotal"));
                hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 18, 19));

                celda = fila.createCell(20);
                String rango = "U" + filaPrimera + ":U" + (filaUltima);
                celda.setCellFormula("SUM(" + rango + ")");
                celda.setCellStyle(estilos.get("estiloTotal"));
                log.debug("Rango SUMA= " + rango);
                
                log.debug("Definir directorio y fichero");
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("AEXCE_DatosAnexo_", ".xls", directorioTemp);
                informe.renameTo(informe);
                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream(istr); // promote

                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();

                log.debug("Abre el fichero EXCEL");
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException ioe) {
                log.error("EXCEPCION informe generarExcelAnexo:" + ioe);
            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe generarExcelAnexo : " + ex);
        } catch (SQLException ex) {
            log.error("EXCEPCION informe generarExcelAnexo : " + ex);
        }
        log.info("FIN  generarExcelAnexo  >>>>>>>>>>>>>>");
    }

    // Funciones Privadas
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;

        if (log.isDebugEnabled()) {
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

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
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
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
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;

        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide80.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide80.FICHERO_PROPIEDADES);

        try {
            if (!descripcion.isEmpty()) {

                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide80.CODIGO_IDIOMA_EUSKERA) {
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

    private int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide80.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {

            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide80.CODIGO_IDIOMA_CASTELLANO;
        }

        return idioma;
    }

    /**
     * Devuelve la extension de fichero correspondiente al tipo MIME pasado.
     * Utilizamos la clase de flexia para no reescribir funciones
     */
    private String getMimeTypeFromExtension(String extension) {
        return MimeTypes.guessMimeTypeFromExtension(extension);
    }

    private int restarFechas(Date fechaIn, Date fechaFinal) {
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

    private void crearCabecera(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, int idioma) {
        log.info(">>>> ENTRA en crearCabecera de " + this.getClass().getSimpleName());
        try {
            MeLanbide80I18n meLanbide80I18n = MeLanbide80I18n.getInstance();

            Map<String, CellStyle> estilos = crearEstilos(libro);

            //cabeceras
            // dni
            celda = fila.createCell(1);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "persona.dni").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //  nombre 
            celda = fila.createCell(2);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "persona.nombre").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //apellidos               
            celda = fila.createCell(3);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.apelllidos").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Tipo contrato
            celda = fila.createCell(4);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "persona.tipcontA").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Porc.rendimiento
            celda = fila.createCell(5);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.rendimiento").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // jornada
            celda = fila.createCell(6);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "persona.jornada").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Porc.jornada
            celda = fila.createCell(7);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.porjornada").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Situación
            celda = fila.createCell(8);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "persona.situacion").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Porc.reducción jornada
            celda = fila.createCell(9);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.reduccion").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Fecha inicio situación
            celda = fila.createCell(10);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.fecinicio").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Fecha fin situación
            celda = fila.createCell(11);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.fecfin").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Días situación
            celda = fila.createCell(12);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.dias").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Base reguladora diaria
            celda = fila.createCell(13);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "persona.baseregul").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Importe prestación
            celda = fila.createCell(14);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "persona.impprest").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Complemento salarial empresa
            celda = fila.createCell(15);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "persona.complsal").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Importe subvención solicitada
            celda = fila.createCell(16);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "persona.impsubvsol").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Control dias  R
            celda = fila.createCell(17);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.controlDias").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Calculo importe pretacion  S
            celda = fila.createCell(18);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.calculoImporte").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Subvencion maxima  T
            celda = fila.createCell(19);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.subvMaxima").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Importe a conceder U
            celda = fila.createCell(20);
            celda.setCellValue(meLanbide80I18n.getMensaje(idioma, "excel.importeConceder").toUpperCase());
            celda.setCellStyle(estilos.get("cabeceraUltima"));

        } catch (Exception ex) {
            log.error("Error al definir estilos de cabeceras: " + ex);
        }
        log.debug("crearCabecera  >>>>> END");
    }

    /**
     * Create a library of cell styles
     */
    private static Map<String, CellStyle> crearEstilos(HSSFWorkbook libro) {
        Map<String, CellStyle> estilos = new HashMap();
        HSSFCellStyle estilo;
        HSSFPalette palette = libro.getCustomPalette();
        HSSFColor hssfColorCabecera = null;
        HSSFCreationHelper createHelper = libro.getCreationHelper();
        try {
            hssfColorCabecera = palette.findColor((byte) 75, (byte) 149, (byte) 211);
            if (hssfColorCabecera == null) {
                hssfColorCabecera = palette.getColor(HSSFColor.ROYAL_BLUE.index);
            }
        } catch (Exception e) {
            log.error("Error findColor : " + e);
            //  codigoOperacion = "1";
        }
        HSSFFont titulo = libro.createFont();
        titulo.setColor(hssfColorCabecera.getIndex());
        titulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titulo.setFontHeight((short) 250);

        HSSFFont total = libro.createFont();
        total.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        total.setFontHeight((short) 250);

        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        negrita.setFontHeight((short) 200);

        HSSFFont normal = libro.createFont();
        normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        normal.setFontHeight((short) 150);

        HSSFFont negritaTitulo = libro.createFont();
        negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        negritaTitulo.setFontHeight((short) 200);
        negritaTitulo.setColor(HSSFColor.WHITE.index);

        estilo = libro.createCellStyle();
        estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estilo.setFillForegroundColor(hssfColorCabecera.getIndex());
        estilo.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estilo.setWrapText(true);
        estilo.setShrinkToFit(true);
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estilo.setFont(negritaTitulo);
        estilo.setFont(negritaTitulo);
        estilos.put("cabecera", estilo);

        estilo = libro.createCellStyle();
        estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estilo.setFillForegroundColor(hssfColorCabecera.getIndex());
        estilo.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estilo.setWrapText(true);
        estilo.setShrinkToFit(true);
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estilo.setFont(negritaTitulo);
        estilos.put("cabeceraUltima", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setFont(titulo);
        estilos.put("estiloTitulo", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estilo.setDataFormat(libro.createDataFormat().getFormat("#,##0.00"));
        estilo.setWrapText(true);
        estilo.setFont(total);
        estilos.put("estiloTotal", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setDataFormat(libro.createDataFormat().getFormat("#,##0.00"));
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilos.put("estiloImportes", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilos.put("estiloCentrado", estilo);

        estilo = libro.createCellStyle();
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        estilos.put("estiloCelda", estilo);

        estilo = libro.createCellStyle();
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilos.put("estiloUltimaFila", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setDataFormat(libro.createDataFormat().getFormat("#,##0.00"));
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilos.put("estiloUltimaColumna", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilo.setDataFormat(createHelper.createDataFormat().getFormat("d/m/yyyy"));
        // estilo.setDataFormat(HSSFDataFormat.getBuiltinFormat("d/m/yyyy"));
        estilos.put("estiloFecha", estilo);
        return estilos;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String obtenerXmlSalidaPersona(HttpServletRequest request, String codigoOperacion, List<PersonaVO> lista) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (PersonaVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<DNI>");
            xmlSalida.append(fila.getDni());
            xmlSalida.append("</DNI>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<APEL1>");
            xmlSalida.append(fila.getApel1());
            xmlSalida.append("</APEL1>");
            xmlSalida.append("<APEL2>");
            xmlSalida.append(fila.getApel2());
            xmlSalida.append("</APEL2>");
            xmlSalida.append("<DESTIPCONTA>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesTipcontA()));
            xmlSalida.append("</DESTIPCONTA>");
            xmlSalida.append("<TIPCONTB>");
            if (fila.getTipcontB() != null && !"".equals(fila.getTipcontB())) {
                xmlSalida.append(fila.getTipcontB());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</TIPCONTB>");
            xmlSalida.append("<DESJORNADA>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesJornada()));
            xmlSalida.append("</DESJORNADA>");
            xmlSalida.append("<PORJORPAR>");
            if (fila.getPorjorpar() != null && !"".equals(fila.getPorjorpar())) {
                xmlSalida.append(fila.getPorjorpar());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</PORJORPAR>");
            xmlSalida.append("<DESSITUACION>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesSituacion()));
            xmlSalida.append("</DESSITUACION>");
            xmlSalida.append("<REDUCJORN>");
            if (fila.getReducjorn() != null && !"".equals(fila.getReducjorn())) {
                xmlSalida.append(fila.getReducjorn());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</REDUCJORN>");
            xmlSalida.append("<FECINISIT>");
            if (fila.getFecinisit() != null) {
                xmlSalida.append(dateFormat.format(fila.getFecinisit()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECINISIT>");
            xmlSalida.append("<FECFINSIT>");
            if (fila.getFecfinsit() != null) {
                xmlSalida.append(dateFormat.format(fila.getFecfinsit()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECFINSIT>");
            xmlSalida.append("<NUMDIASIT>");
            if (fila.getNumdiasit() != null && !"".equals(fila.getNumdiasit())) {
                xmlSalida.append(fila.getNumdiasit());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</NUMDIASIT>");
            xmlSalida.append("<BASEREGUL>");
            if (fila.getBaseregul() != null && !"".equals(fila.getBaseregul())) {
                xmlSalida.append(fila.getBaseregul());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</BASEREGUL>");
            xmlSalida.append("<IMPPREST>");
            if (fila.getImpprest() != null && !"".equals(fila.getImpprest())) {
                xmlSalida.append(fila.getImpprest());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</IMPPREST>");
            xmlSalida.append("<COMPLSAL>");
            if (fila.getComplsal() != null && !"".equals(fila.getComplsal())) {
                xmlSalida.append(fila.getComplsal());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</COMPLSAL>");
            xmlSalida.append("<IMPSUBVSOL>");
            if (fila.getImpsubvsol() != null && !"".equals(fila.getImpsubvsol())) {
                xmlSalida.append(fila.getImpsubvsol());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</IMPSUBVSOL>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }

}
