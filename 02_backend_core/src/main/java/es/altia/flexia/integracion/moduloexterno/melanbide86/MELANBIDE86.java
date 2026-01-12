package es.altia.flexia.integracion.moduloexterno.melanbide86;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide86.i18n.MeLanbide86I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide86.manager.MeLanbide86Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConstantesMeLanbide86;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.dto.SearchCVResponseDTO;
import es.altia.flexia.integracion.moduloexterno.melanbide67.MELANBIDE67;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.zip.DataFormatException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
import es.altia.agora.technical.CamposFormulario;
import es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.PersonaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.MELANBIDE_INTEROP;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.cvl.Persona;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.RegistroVidaLaboralVO;

public class MELANBIDE86 extends ModuloIntegracionExterno {

    private static final Logger log = LogManager.getLogger(MELANBIDE86.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static MELANBIDE67 melanbide67 = new MELANBIDE67();
    private static MELANBIDE_INTEROP melanbide_interop = new MELANBIDE_INTEROP();
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
        log.info("Entramos en cargarPantallaPrincipal de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide86/melanbide86.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaContratacionVO> listaContrataciones = MeLanbide86Manager.getInstance().getListaContrataciones(numExpediente, codOrganizacion, adapt);
                if (!listaContrataciones.isEmpty()) {
                    for (FilaContratacionVO contr : listaContrataciones) {
                        contr.setDescActDes1(getDescripcionDesplegable(request, contr.getDescActDes1()));
                        contr.setDescGrupoCotiz1(getDescripcionDesplegable(request, contr.getDescGrupoCotiz1()));
                        contr.setDescSexo2(getDescripcionDesplegable(request, contr.getDescSexo2()));
                        contr.setDescActDes2(getDescripcionDesplegable(request, contr.getDescActDes2()));
                        contr.setDescGrupoCotiz2(getDescripcionDesplegable(request, contr.getDescGrupoCotiz2()));
                    }
                    final CamposFormulario camposFormularioFechas = MeLanbide86Manager.getInstance().getValoresFechas(numExpediente, adapt);
                    final CamposFormulario camposFormularioFicheros = MeLanbide86Manager.getInstance().getFicheros(numExpediente, adapt);
                    iterListaContrataciones(listaContrataciones, camposFormularioFechas,
                            camposFormularioFicheros);
                    request.setAttribute("listaContrataciones", listaContrataciones);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos De Contrataciones - MELANBIDE86 - cargarPantallaPrincipal", ex);
            }
        }
        return url;
    }

    public String cargarNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaContratacion - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide86/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;
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
            cargarDesplegablesIKER(codOrganizacion, request);

        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva Contratacion : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public String cargarModificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarContratacion - " + numExpediente);
        String nuevo = "0";
        String urlnuevaContratacion = "/jsp/extension/melanbide86/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;
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
            // Recuperramos datos de Contratacion a modificar y cargamos en el request
            if (id != null && !id.isEmpty()) {
                FilaContratacionVO datModif = MeLanbide86Manager.getInstance().getContratacionPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            //Cargamos los valores  de los desplegables
            cargarDesplegablesIKER(codOrganizacion, request);
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public void crearNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevaContratacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaContratacionVO> lista = new ArrayList<FilaContratacionVO>();
        FilaContratacionVO nuevaContratacion = new FilaContratacionVO();
        int tablas = 1;
        boolean tabla2 = false;
        boolean tabla3 = false;

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            log.debug("Recojo 1");

            String numExp = request.getParameter("numExpediente");
            String numPuesto = request.getParameter("numPuesto");
            String denomPuesto = request.getParameter("denomPuesto");
            String actDes1 = request.getParameter("actDes1");
            String titulacion1 = request.getParameter("titulacion1");
            String tipoCont1 = request.getParameter("tipoCont1");
            String durContrato1 = request.getParameter("durContrato1");
            String grupoCotiz1 = request.getParameter("grupoCotiz1");
            String costeSalarial1 = request.getParameter("costeSalarial1").replace(",", ".");
            String subvSolicitada1 = request.getParameter("subvSolicitada1").replace(",", ".");
            String cainVinn1 = request.getParameter("cainVinn1");
            log.debug("Recojo 2");
            String nOferta2 = request.getParameter("nOferta2");
            String nombre2 = request.getParameter("nombre2");
            String apellido12 = request.getParameter("apellido12");
            String apellido22 = request.getParameter("apellido22");
            String dniNie2 = request.getParameter("dniNie2");
            String sexo2 = request.getParameter("sexo2");
            String actDes2 = request.getParameter("actDes2");
            String titulacion2 = request.getParameter("titulacion2");
            String tipoCont2 = request.getParameter("tipoCont2");
            String durContrato2 = request.getParameter("durContrato2");
            String grupoCotiz2 = request.getParameter("grupoCotiz2");
            String fechaNacimiento2 = request.getParameter("fechaNacimiento2");
            String fechaInicio2 = request.getParameter("fechaInicio2");
            String edad2 = request.getParameter("edad2");
            String retribucionBruta2 = request.getParameter("retribucionBruta2").replace(",", ".");
            String cainVinn2 = request.getParameter("cainVinn2");
            log.debug("Recojo 3");
            String nombre3 = request.getParameter("nombre3");
            String apellido13 = request.getParameter("apellido13");
            String apellido23 = request.getParameter("apellido23");
            String dniNie3 = request.getParameter("dniNie3");
            String durContrato3 = request.getParameter("durContrato3");
            String fechaInicio3 = request.getParameter("fechaInicio3");
            String fechaFin3 = request.getParameter("fechaFin3");
            String costeSalarial3 = request.getParameter("costeSalarial3").replace(",", ".");
            String costesSS3 = request.getParameter("costesSS3").replace(",", ".");
            String costeTotalReal = request.getParameter("costeTotalReal").replace(",", ".");
            String subvConcedidaLan3 = request.getParameter("subvConcedidaLan3").replace(",", ".");
            log.debug("Cargo 1");

            nuevaContratacion.setNumExp(numExp);
            nuevaContratacion.setNumPuesto(Integer.parseInt(numPuesto));
            nuevaContratacion.setDenomPuesto(denomPuesto);
            nuevaContratacion.setActDes1(actDes1);
            nuevaContratacion.setTitulacion1(titulacion1);
            nuevaContratacion.setTipoCont1(tipoCont1);
            nuevaContratacion.setDurContrato1(Integer.parseInt(durContrato1));
            nuevaContratacion.setGrupoCotiz1(grupoCotiz1);
            nuevaContratacion.setCosteSalarial1(Double.parseDouble(costeSalarial1));
            nuevaContratacion.setSubvSolicitada1(Double.parseDouble(subvSolicitada1));
            nuevaContratacion.setCainVinn1(cainVinn1);
            // CONTRATACION_INI
            log.debug("Cargo 2");
            nuevaContratacion.setNumExp2(numExp);
            nuevaContratacion.setNumPuesto2(Integer.parseInt(numPuesto));
            if (nOferta2 != null && !"".equalsIgnoreCase(nOferta2)) {
                nuevaContratacion.setnOferta2(nOferta2);
                tabla2 = true;
            }
            if (nombre2 != null && !"".equalsIgnoreCase(nombre2)) {
                tabla2 = true;
                nuevaContratacion.setNombre2(nombre2);
            }
            if (apellido12 != null && !"".equalsIgnoreCase(apellido12)) {
                tabla2 = true;
                nuevaContratacion.setApellido12(apellido12);
            }
            if (tabla2) {
                if (apellido22 != null && !"".equalsIgnoreCase(apellido22)) {
                    nuevaContratacion.setApellido22(apellido22);
                }
                if (dniNie2 != null && !"".equalsIgnoreCase(dniNie2)) {
                    nuevaContratacion.setDniNie2(dniNie2);
                }
                if (sexo2 != null && !"".equalsIgnoreCase(sexo2)) {
                    nuevaContratacion.setSexo2(sexo2);
                }
                if (actDes2 != null && !"".equalsIgnoreCase(actDes2)) {
                    nuevaContratacion.setActDes2(actDes2);
                }
                if (titulacion2 != null && !"".equalsIgnoreCase(titulacion2)) {
                    nuevaContratacion.setTitulacion2(titulacion2);
                }
                if (tipoCont2 != null && !"".equalsIgnoreCase(tipoCont2)) {
                    nuevaContratacion.setTipoCont2(tipoCont2);
                }
                if (durContrato2 != null && !"".equalsIgnoreCase(durContrato2)) {
                    nuevaContratacion.setDurContrato2(Integer.parseInt(durContrato2));
                }
                if (grupoCotiz2 != null && !"".equalsIgnoreCase(grupoCotiz2)) {
                    nuevaContratacion.setGrupoCotiz2(grupoCotiz2);
                }
                if (fechaNacimiento2 != null && !"".equalsIgnoreCase(fechaNacimiento2)) {
                    nuevaContratacion.setFechaNacimiento2(new java.sql.Date(dateFormat.parse(fechaNacimiento2).getTime()));
                }
                if (fechaInicio2 != null && !"".equalsIgnoreCase(fechaInicio2)) {
                    nuevaContratacion.setFechaInicio2(new java.sql.Date(dateFormat.parse(fechaInicio2).getTime()));
                }
                if (edad2 != null && !"".equalsIgnoreCase(edad2)) {
                    nuevaContratacion.setEdad2(Integer.parseInt(edad2));
                }
                if (retribucionBruta2 != null && !"".equalsIgnoreCase(retribucionBruta2)) {
                    nuevaContratacion.setRetribucionBruta2(Double.parseDouble(retribucionBruta2));
                }
                if (cainVinn2 != null && !"".equalsIgnoreCase(cainVinn2)) {
                    nuevaContratacion.setCainVinn1(cainVinn1);
                }
            }

            // CONTRATACION_FIN
            log.debug("Cargo 3");
            nuevaContratacion.setNumExp3(numExp);
            nuevaContratacion.setNumPuesto3(Integer.parseInt(numPuesto));
            if (nombre3 != null && !"".equalsIgnoreCase(nombre3)) {
                tabla3 = true;
                nuevaContratacion.setNombre3(nombre3);
            }
            if (apellido13 != null && !"".equalsIgnoreCase(apellido13)) {
                tabla3 = true;
                nuevaContratacion.setApellido13(apellido13);
            }
            if (tabla3) {
                if (apellido23 != null && !"".equalsIgnoreCase(apellido23)) {
                    nuevaContratacion.setApellido23(apellido23);
                }
                if (dniNie3 != null && !"".equalsIgnoreCase(dniNie3)) {
                    nuevaContratacion.setDniNie3(dniNie3);
                }
                if (durContrato3 != null && !"".equalsIgnoreCase(durContrato3)) {
                    nuevaContratacion.setDurContrato3(Integer.parseInt(durContrato3));
                }
                if (fechaInicio3 != null && !"".equalsIgnoreCase(fechaInicio3)) {
                    nuevaContratacion.setFechaInicio3(new java.sql.Date(dateFormat.parse(fechaInicio3).getTime()));
                }
                if (fechaFin3 != null && !"".equalsIgnoreCase(fechaFin3)) {
                    nuevaContratacion.setFechaFin3(new java.sql.Date(dateFormat.parse(fechaFin3).getTime()));
                }
                if (costeSalarial3 != null && !"".equalsIgnoreCase(costeSalarial3)) {
                    nuevaContratacion.setCosteSalarial3(Double.parseDouble(costeSalarial3));
                }
                if (costesSS3 != null && !"".equalsIgnoreCase(costesSS3)) {
                    nuevaContratacion.setCostesSS3(Double.parseDouble(costesSS3));
                }
                if (costeTotalReal != null && !"".equalsIgnoreCase(costeTotalReal)) {
                    nuevaContratacion.setCosteTotalReal(Double.parseDouble(costeTotalReal));
                }
                if (subvConcedidaLan3 != null && !"".equalsIgnoreCase(subvConcedidaLan3)) {
                    nuevaContratacion.setSubvConcedidaLan3(Double.parseDouble(subvConcedidaLan3));
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

            MeLanbide86Manager meLanbide86Manager = MeLanbide86Manager.getInstance();
            boolean insertOK = meLanbide86Manager.crearContratacion(nuevaContratacion, tablas, adapt);
            if (insertOK) {
                log.debug("Contratacion Insertada Correctamente");
                codigoOperacion = "0";
                lista = meLanbide86Manager.getListaContrataciones(numExp, codOrganizacion, adapt);
                if (!lista.isEmpty()) {
                    for (FilaContratacionVO contr : lista) {
                        contr.setDescActDes1(getDescripcionDesplegable(request, contr.getDescActDes1()));
                        contr.setDescGrupoCotiz1(getDescripcionDesplegable(request, contr.getDescGrupoCotiz1()));
                        contr.setDescSexo2(getDescripcionDesplegable(request, contr.getDescSexo2()));
                        contr.setDescActDes2(getDescripcionDesplegable(request, contr.getDescActDes2()));
                        contr.setDescGrupoCotiz2(getDescripcionDesplegable(request, contr.getDescGrupoCotiz2()));
                    }
                    final CamposFormulario camposFormularioFechas = MeLanbide86Manager.getInstance().getValoresFechas(numExp, adapt);
                    final CamposFormulario camposFormularioFicheros = MeLanbide86Manager.getInstance().getFicheros(numExp, adapt);
                    iterListaContrataciones(lista, camposFormularioFechas,
                            camposFormularioFicheros);
                }
            } else {
                log.debug("NO se ha insertado correctamente la nueva Contratacion");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
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
            String numExp = request.getParameter("numExpediente");
            String numPuesto = request.getParameter("numPuesto");
            String denomPuesto = request.getParameter("denomPuesto");
            String actDes1 = request.getParameter("actDes1");
            String titulacion1 = request.getParameter("titulacion1");
            String tipoCont1 = request.getParameter("tipoCont1");
            String durContrato1 = request.getParameter("durContrato1");
            String grupoCotiz1 = request.getParameter("grupoCotiz1");
            String costeSalarial1 = request.getParameter("costeSalarial1").replace(",", ".");
            String subvSolicitada1 = request.getParameter("subvSolicitada1").replace(",", ".");
            String cainVinn1 = request.getParameter("cainVinn1");
            String nOferta2 = request.getParameter("nOferta2");
            String nombre2 = request.getParameter("nombre2");
            String apellido12 = request.getParameter("apellido12");
            String apellido22 = request.getParameter("apellido22");
            String dniNie2 = request.getParameter("dniNie2");
            String sexo2 = request.getParameter("sexo2");
            String actDes2 = request.getParameter("actDes2");
            String titulacion2 = request.getParameter("titulacion2");
            String tipoCont2 = request.getParameter("tipoCont2");
            String durContrato2 = request.getParameter("durContrato2");
            String grupoCotiz2 = request.getParameter("grupoCotiz2");
            String fechaNacimiento2 = request.getParameter("fechaNacimiento2");
            String fechaInicio2 = request.getParameter("fechaInicio2");
            String edad2 = request.getParameter("edad2");
            String retribucionBruta2 = request.getParameter("retribucionBruta2").replace(",", ".");
            String cainVinn2 = request.getParameter("cainVinn2");
            String nombre3 = request.getParameter("nombre3");
            String apellido13 = request.getParameter("apellido13");
            String apellido23 = request.getParameter("apellido23");
            String dniNie3 = request.getParameter("dniNie3");
            String durContrato3 = request.getParameter("durContrato3");
            String fechaInicio3 = request.getParameter("fechaInicio3");
            String fechaFin3 = request.getParameter("fechaFin3");
            String costeSalarial3 = request.getParameter("costeSalarial3").replace(",", ".");
            String costesSS3 = request.getParameter("costesSS3").replace(",", ".");
            String costeTotalReal = request.getParameter("costeTotalReal").replace(",", ".");
            String subvConcedidaLan3 = request.getParameter("subvConcedidaLan3").replace(",", ".");

            if (id == null || id.isEmpty()) {
                log.debug("No se ha recibido desde la JSP el id de la Contratacion a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaContratacionVO contratacionModif = new FilaContratacionVO();
                contratacionModif.setId(Integer.parseInt(id));
                contratacionModif.setNumExp(numExp);
                contratacionModif.setNumPuesto(Integer.parseInt(numPuesto));
                contratacionModif.setDenomPuesto(denomPuesto);
                contratacionModif.setActDes1(actDes1);
                contratacionModif.setTitulacion1(titulacion1);
                contratacionModif.setTipoCont1(tipoCont1);
                contratacionModif.setDurContrato1(Integer.parseInt(durContrato1));
                contratacionModif.setGrupoCotiz1(grupoCotiz1);
                contratacionModif.setCosteSalarial1(Double.parseDouble(costeSalarial1));
                contratacionModif.setSubvSolicitada1(Double.parseDouble(subvSolicitada1));
                contratacionModif.setCainVinn1(cainVinn1);
                // CONTRATACION_INI
                contratacionModif.setNumExp2(numExp);
                contratacionModif.setNumPuesto2(Integer.parseInt(numPuesto));
                contratacionModif.setDenomPuesto2(denomPuesto);
                if (nOferta2 != null && !"".equalsIgnoreCase(nOferta2)) {
                    contratacionModif.setnOferta2(nOferta2);
                    tabla2 = true;
                }
                if (nombre2 != null && !"".equalsIgnoreCase(nombre2)) {
                    tabla2 = true;
                    contratacionModif.setNombre2(nombre2);
                }
                if (apellido12 != null && !"".equalsIgnoreCase(apellido12)) {
                    tabla2 = true;
                    contratacionModif.setApellido12(apellido12);
                }
                if (tabla2) {
                    if (apellido22 != null && !"".equalsIgnoreCase(apellido22)) {
                        contratacionModif.setApellido22(apellido22);
                    }
                    if (dniNie2 != null && !"".equalsIgnoreCase(dniNie2)) {
                        contratacionModif.setDniNie2(dniNie2);
                    }
                    if (sexo2 != null && !"".equalsIgnoreCase(sexo2)) {
                        contratacionModif.setSexo2(sexo2);
                    }
                    if (actDes2 != null && !"".equalsIgnoreCase(actDes2)) {
                        contratacionModif.setActDes2(actDes2);
                    }
                    if (titulacion2 != null && !"".equalsIgnoreCase(titulacion2)) {
                        contratacionModif.setTitulacion2(titulacion2);
                    }
                    if (tipoCont2 != null && !"".equalsIgnoreCase(tipoCont2)) {
                        contratacionModif.setTipoCont2(tipoCont2);
                    }
                    if (durContrato2 != null && !"".equalsIgnoreCase(durContrato2)) {
                        contratacionModif.setDurContrato2(Integer.parseInt(durContrato2));
                    }
                    if (grupoCotiz2 != null && !"".equalsIgnoreCase(grupoCotiz2)) {
                        contratacionModif.setGrupoCotiz2(grupoCotiz2);
                    }
                    if (fechaNacimiento2 != null && !"".equalsIgnoreCase(fechaNacimiento2)) {
                        contratacionModif.setFechaNacimiento2(new java.sql.Date(dateFormat.parse(fechaNacimiento2).getTime()));
                    }
                    if (fechaInicio2 != null && !"".equalsIgnoreCase(fechaInicio2)) {
                        contratacionModif.setFechaInicio2(new java.sql.Date(dateFormat.parse(fechaInicio2).getTime()));
                    }
                    if (edad2 != null && !"".equalsIgnoreCase(edad2)) {
                        contratacionModif.setEdad2(Integer.parseInt(edad2));
                    }
                    if (retribucionBruta2 != null && !"".equalsIgnoreCase(retribucionBruta2)) {
                        contratacionModif.setRetribucionBruta2(Double.parseDouble(retribucionBruta2));
                    }
                    if (cainVinn2 != null && !"".equalsIgnoreCase(cainVinn2)) {
                        contratacionModif.setCainVinn2(cainVinn2);
                    }
                }
                // CONTRATACION_FIN
                contratacionModif.setNumExp3(numExp);
                contratacionModif.setNumPuesto3(Integer.parseInt(numPuesto));
                if (nombre3 != null && !"".equalsIgnoreCase(nombre3)) {
                    tabla3 = true;
                    contratacionModif.setNombre3(nombre3);
                }
                if (apellido13 != null && !"".equalsIgnoreCase(apellido13)) {
                    tabla3 = true;
                    contratacionModif.setApellido13(apellido13);
                }
                if (tabla3) {
                    if (apellido23 != null && !"".equalsIgnoreCase(apellido23)) {
                        contratacionModif.setApellido23(apellido23);
                    }
                    if (dniNie3 != null && !"".equalsIgnoreCase(dniNie3)) {
                        contratacionModif.setDniNie3(dniNie3);
                    }
                    if (durContrato3 != null && !"".equalsIgnoreCase(durContrato3)) {
                        contratacionModif.setDurContrato3(Integer.parseInt(durContrato3));
                    }
                    if (fechaInicio3 != null && !"".equalsIgnoreCase(fechaInicio3)) {
                        contratacionModif.setFechaInicio3(new java.sql.Date(dateFormat.parse(fechaInicio3).getTime()));
                    }
                    if (fechaFin3 != null && !"".equalsIgnoreCase(fechaFin3)) {
                        contratacionModif.setFechaFin3(new java.sql.Date(dateFormat.parse(fechaFin3).getTime()));
                    }
                    if (costeSalarial3 != null && !"".equalsIgnoreCase(costeSalarial3)) {
                        contratacionModif.setCosteSalarial3(Double.parseDouble(costeSalarial3));
                    }
                    if (costesSS3 != null && !"".equalsIgnoreCase(costesSS3)) {
                        contratacionModif.setCostesSS3(Double.parseDouble(costesSS3));
                    }
                    if (costeTotalReal != null && !"".equalsIgnoreCase(costeTotalReal)) {
                        contratacionModif.setCosteTotalReal(Double.parseDouble(costeTotalReal));
                    }
                    if (subvConcedidaLan3 != null && !"".equalsIgnoreCase(subvConcedidaLan3)) {
                        contratacionModif.setSubvConcedidaLan3(Double.parseDouble(subvConcedidaLan3));
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

                MeLanbide86Manager meLanbide86Manager = MeLanbide86Manager.getInstance();
                boolean modOK = meLanbide86Manager.modificarContratacion(contratacionModif, tablas, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        listaContrataciones = meLanbide86Manager.getListaContrataciones(numExp, codOrganizacion, adapt);
                        if (!listaContrataciones.isEmpty()) {
                            for (FilaContratacionVO contr : listaContrataciones) {
                                contr.setDescActDes1(getDescripcionDesplegable(request, contr.getDescActDes1()));
                                contr.setDescGrupoCotiz1(getDescripcionDesplegable(request, contr.getDescGrupoCotiz1()));
                                contr.setDescSexo2(getDescripcionDesplegable(request, contr.getDescSexo2()));
                                contr.setDescActDes2(getDescripcionDesplegable(request, contr.getDescActDes2()));
                                contr.setDescGrupoCotiz2(getDescripcionDesplegable(request, contr.getDescGrupoCotiz2()));
                            }
                        }
                        final CamposFormulario camposFormularioFechas = MeLanbide86Manager.getInstance().getValoresFechas(numExp, adapt);
                        final CamposFormulario camposFormularioFicheros = MeLanbide86Manager.getInstance().getFicheros(numExp, adapt);
                        iterListaContrataciones(listaContrataciones, camposFormularioFechas,
                                camposFormularioFicheros);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Contrataciones después de Modificar una Contratacion : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de Contrataciones después de Modificar una Contratacion : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "3";
                    log.debug("Error modificar --- ");
                }
            }
        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaContrataciones);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void eliminarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarContratacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaContratacionVO> listaContrataciones = new ArrayList<FilaContratacionVO>();
        String numExp = "", docCV = "", docDemanda = "";
        try {
            String id = request.getParameter("id");
            docCV = request.getParameter("docCV");
            docDemanda = request.getParameter("docDemanda");
            if (id == null || id.isEmpty()) {
                log.debug("No se ha recibido desde la JSP el id de la Contratacion a Eliminar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide86Manager.getInstance().eliminarContratacion(id, numExp, docCV, docDemanda, codOrganizacion,
                        adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        listaContrataciones = MeLanbide86Manager.getInstance().getListaContrataciones(numExp, codOrganizacion, adapt);
                        if (!listaContrataciones.isEmpty()) {
                            for (FilaContratacionVO contr : listaContrataciones) {
                                contr.setDescActDes1(getDescripcionDesplegable(request, contr.getDescActDes1()));
                                contr.setDescGrupoCotiz1(getDescripcionDesplegable(request, contr.getDescGrupoCotiz1()));
                                contr.setDescSexo2(getDescripcionDesplegable(request, contr.getDescSexo2()));
                                contr.setDescActDes2(getDescripcionDesplegable(request, contr.getDescActDes2()));
                                contr.setDescGrupoCotiz2(getDescripcionDesplegable(request, contr.getDescGrupoCotiz2()));
                            }
                            final CamposFormulario camposFormularioFechas = MeLanbide86Manager.getInstance().getValoresFechas(numExp, adapt);
                            final CamposFormulario camposFormularioFicheros = MeLanbide86Manager.getInstance().getFicheros(numExp, adapt);
                            iterListaContrataciones(listaContrataciones, camposFormularioFechas,
                                    camposFormularioFicheros);
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de Contrataciones después de eliminar una Contratacion", ex);
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una Contratacion: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaContrataciones);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

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
            }

            request.getSession().setAttribute("mensajeImportar", MeLanbide86I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
        } catch (DataFormatException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE86.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE86.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE86.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE86.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide86I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarListaPersonas.jsp");
        return "/jsp/extension/melanbide86/recargarListaContrataciones.jsp";
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
        List<FilaContratacionVO> lista = new ArrayList<FilaContratacionVO>();
        String numExp = "";
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide86Manager.getInstance().getListaContrataciones(numExp, codOrganizacion, adapt);
            if (lista.isEmpty()) {
                log.error("No se ha cargado ningún registro para el expediente " + numExp + " después de cargar el XML");
                codigoOperacion = "4";
            } else {
                codigoOperacion = "0";
                for (FilaContratacionVO contr : lista) {
                    contr.setDescActDes1(getDescripcionDesplegable(request, contr.getDescActDes1()));
                    contr.setDescGrupoCotiz1(getDescripcionDesplegable(request, contr.getDescGrupoCotiz1()));
                    contr.setDescSexo2(getDescripcionDesplegable(request, contr.getDescSexo2()));
                    contr.setDescActDes2(getDescripcionDesplegable(request, contr.getDescActDes2()));
                    contr.setDescGrupoCotiz2(getDescripcionDesplegable(request, contr.getDescGrupoCotiz2()));
                }
                final CamposFormulario camposFormularioFechas = MeLanbide86Manager.getInstance().getValoresFechas(numExp, adapt);
                final CamposFormulario camposFormularioFicheros = MeLanbide86Manager.getInstance().getFicheros(numExp, adapt);
                iterListaContrataciones(lista, camposFormularioFechas,
                        camposFormularioFicheros);
            }
        } catch (BDException bde) {
            codigoOperacion = "1";
            log.error("Error de tipo BD al recuperar la lista de Personas después de cargar el XML : " + bde.getMensaje());
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error al recuperar la lista de Personas después de cargar el XML : " + ex.getMessage());
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
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
            listaContrataciones = MeLanbide86Manager.getInstance().getListaContrataciones(numExpediente, codOrganizacion, adapt);
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al invocar getListaContrataciones ", ex);
        }

        final Calendar calendar = Calendar.getInstance();
        final java.util.Date currentDate = calendar.getTime();
        final Date fechaDemandaInter = new java.sql.Date(currentDate.getTime());

        int i = 0;
        for (final FilaContratacionVO contr : listaContrataciones) {
            log.debug("recuperarDatosCVoDemandaIntermediacion DNI = " + contr);
            if (contr.getDniNie2() != null && !contr.getDniNie2().equals("")) {
                final SearchCVResponseDTO respuestaDni2 = melanbide67.recuperarDatosCVoDemandaIntermediacion(codOrganizacion,
                        contr.getDniNie2(), request.getParameter("documento"), numExpediente, request, response);
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
                final CamposFormulario camposFormularioFicheros = MeLanbide86Manager.getInstance().getFicheros(numExpediente, adapt);

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
                log.error("Error al recuperar los datos de contrataciones - MELANBIDE86 - obtainDocumentFile", ex);
            }
        }
        log.info("Fin de obtainDocumentFile de " + this.getClass().getName());
    }

    private void iterListaContrataciones(List<FilaContratacionVO> listaContrataciones,
                                         final CamposFormulario camposFormularioFechas, final CamposFormulario camposFormularioFicheros)
            throws ParseException, FileNotFoundException, IOException, SQLException {
        int i = 0;
        Object obj = null;
        String dateString = null;
        java.sql.Date dateSql = null;

        for (final FilaContratacionVO fila : listaContrataciones) {
            if (camposFormularioFechas.contieneCampo("FECHA" + "CV" + "2" + i + "INTER")) {
                obj = camposFormularioFechas.get("FECHA" + "CV" + "2" + i + "INTER");
                dateString = (String) obj;
                dateSql = new java.sql.Date(dateFormat.parse(dateString).getTime());
                fila.setFechaCv2(dateSql);
                fila.setFechaCv2Str(dateFormat.format(dateSql));
            }
            if (camposFormularioFechas.contieneCampo("FECHA" + "DEM" + "2" + i + "INTER")) {
                obj = camposFormularioFechas.get("FECHA" + "DEM" + "2" + i + "INTER");
                dateString = (String) obj;
                dateSql = new java.sql.Date(dateFormat.parse(dateString).getTime());
                fila.setFechaDemanda2(dateSql);
                fila.setFechaDemanda2Str(dateFormat.format(dateSql));
            }
            if (camposFormularioFicheros.contieneCampo("DOC" + "CV" + "2" + i + "INTER")) {
                fila.setCv2("DOC" + "CV" + "2" + i + "INTER");
            }
            if (camposFormularioFicheros.contieneCampo("DOC" + "DEM" + "2" + i + "INTER")) {
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
                MeLanbide86Manager.getInstance().setDatosCVoDemandaIntermediacion(numExpediente, fechaDocInter, nameFieldFecha, docFile, nameFieldDoc, adapt);

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
        Connection con = null;

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

    /**
     * Método que recuperalos valores de los desplegables del modulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegablesIKER(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaActDes = MeLanbide86Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide86.COD_DES_PEIA, ConstantesMeLanbide86.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaSexo = MeLanbide86Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide86.COD_DES_SEXO, ConstantesMeLanbide86.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaGrupoCot = MeLanbide86Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide86.COD_DES_GCON, ConstantesMeLanbide86.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

        if (!listaActDes.isEmpty()) {
            listaActDes = traducirDesplegable(request, listaActDes);
            request.setAttribute("listaActDes", listaActDes);
        }
        if (!listaSexo.isEmpty()) {
            listaSexo = traducirDesplegable(request, listaSexo);
            request.setAttribute("listaSexo", listaSexo);
        }
        if (!listaGrupoCot.isEmpty()) {
            listaGrupoCot = traducirDesplegable(request, listaGrupoCot);
            request.setAttribute("listaGrupoCot", listaGrupoCot);
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
     * Método que retorna el valor de un desplegable en el idioma del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide86.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide86.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (!descripcion.isEmpty() ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide86.CODIGO_IDIOMA_EUSKERA) {
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
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide86.CODIGO_IDIOMA_CASTELLANO;
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide86.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }
    public static String getCodigoProcedimientoFromNumExpediente(String numExpediente) {
        String retorno = "";
        try {
            if (numExpediente != null) {
                String[] arrayDatos = numExpediente.split("/");
                if (arrayDatos != null && arrayDatos.length == 3) {
                    retorno = arrayDatos[1];
                }
            }
        } catch (Exception e) {
            retorno = "";
            log.error("Error al recuperar el cod procedimiento desde el numero de expediente - retornamos vacio. ", e);
        }
        return retorno;
    }

    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petición a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se
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
    public String cargarPantallaSubSolic(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaSubvenciones de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide86/subSolic.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaMinimisVO> listaMinimis = MeLanbide86Manager.getInstance().getDatosMinimis(numExpediente, codOrganizacion, adapt);
                if (listaMinimis.size() > 0) {
                    for (FilaMinimisVO lm : listaMinimis) {
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                    request.setAttribute("listaMinimis", listaMinimis);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de minimis - MELANBIDE86 - cargarPantallaSubvenciones", ex);
            }
        }

        return url;
    }


    public String cargarNuevoMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoMinimis - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevoMinimis = "/jsp/extension/melanbide86/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
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
            cargarDesplegablesSubvencion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva minimis : " + ex.getMessage());
        }
        return urlnuevoMinimis;
    }

    public String cargarModificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarMinimis - " + numExpediente);
        String nuevo = "0";
        String urlnuevoMinimis = "/jsp/extension/melanbide86/nuevoSubSolic.jsp?codOrganizacion=" + codOrganizacion;
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
            if (id != null && !id.isEmpty()){
                FilaMinimisVO datModif = MeLanbide86Manager.getInstance().getMinimisPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            cargarDesplegablesSubvencion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificaci?n : " + ex.getMessage());
        }
        return urlnuevoMinimis;

    }

    public void crearNuevoMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO nuevaMinimis = new FilaMinimisVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");
            String estado = (String) request.getParameter("estado");
            String organismo = (String) request.getParameter("organismo");
            String objeto = (String) request.getParameter("objeto");
            String importe = (String) request.getParameter("importe").replace(",", ".");
            String fecha = (String) request.getParameter("fecha");

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

            nuevaMinimis.setNumExp(numExp);

            nuevaMinimis.setEstado(estado);
            nuevaMinimis.setOrganismo(organismo);
            nuevaMinimis.setObjeto(objeto);
            if (importe != null && !"".equals(importe)) {
                nuevaMinimis.setImporte(Double.valueOf(importe));
            }
            if (fecha != null && !"".equals(fecha)) {
                nuevaMinimis.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
            }

            MeLanbide86Manager meLanbide86Manager = MeLanbide86Manager.getInstance();
            boolean insertOK = meLanbide86Manager.crearNuevoMinimis(nuevaMinimis, adapt);
            if (insertOK) {
                log.debug("minimis insertada correctamente");
                codigoOperacion = "0";
                lista = meLanbide86Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                if (lista.size() > 0) {
                    for (FilaMinimisVO lm : lista) {
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                }
            } else {
                log.debug("NO se ha insertado correctamente la nueva minimis");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los par?metros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("numExp");

            String estado = (String) request.getParameter("estado");
            String organismo = (String) request.getParameter("organismo");
            String objeto = (String) request.getParameter("objeto");
            String importe = (String) request.getParameter("importe").replace(",", ".");
            String fecha = (String) request.getParameter("fecha");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaMinimisVO datModif = new FilaMinimisVO();

                datModif.setId(Integer.parseInt(id));

                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

                datModif.setNumExp(numExp);

                datModif.setEstado(estado);
                datModif.setOrganismo(organismo);
                datModif.setObjeto(objeto);
                if (importe != null && !"".equals(importe)) {
                    datModif.setImporte(Double.parseDouble(importe));
                }
                if (fecha != null && !"".equals(fecha)) {
                    datModif.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
                }

                MeLanbide86Manager meLanbide86Manager = MeLanbide86Manager.getInstance();
                boolean modOK = meLanbide86Manager.modificarMinimis(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = meLanbide86Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de minimis despu?s de Modificar una minimis : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de minimis despu?s de Modificar una minimis : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "3";
                }
            }

        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);

    }

    public void eliminarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide86Manager.getInstance().eliminarMinimis(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide86Manager.getInstance().getDatosMinimis(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de minimis despu?s de eliminar una minimis");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una minimis: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     * M?todo que recupera los valores de los desplegables del m?dulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegablesSubvencion(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaEstado = MeLanbide86Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide86.COD_DES_DTSV, ConstantesMeLanbide86.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaEstado.isEmpty()) {
            listaEstado = traducirDesplegable(request, listaEstado);
            request.setAttribute("listaEstado", listaEstado);
        }
    }

    // INICIO Tarea  IKER - [40984] - Interoperabilidad - Consulta vida laboral desde la ficha de expediente
    private List<Persona> obtainPersonasAnexos (int codOrganizacion, final AdaptadorSQLBD adapt, final String numExpediente) throws Exception {
        List<Persona> personas = new ArrayList<Persona>();

        log.info("=========== ENTRO en obtainPersonasAnexos");
        final Connection con = adapt.getConnection();
        final List<FilaContratacionVO> listaContrataciones = MeLanbide86Manager.getInstance().getListaContrataciones(numExpediente, codOrganizacion, adapt);

        for (final FilaContratacionVO f : listaContrataciones) {
            if (f.getDniNie2() != null && !f.getDniNie2().equals("")) {
                if (!estaEnLista(f.getDniNie2(), personas)) {
                    personas.add(new Persona(f.getNombre2(), f.getApellido12(), f.getApellido22(),
                            getTipoDocumento(f.getDniNie2()), f.getDniNie2()));
                }
            }
            if (f.getDniNie3() != null && !f.getDniNie3().equals("")) {
                if (!estaEnLista(f.getDniNie3(), personas)) {
                    personas.add(new Persona(f.getNombre3(), f.getApellido13(), f.getApellido23(),
                            getTipoDocumento(f.getDniNie3()), f.getDniNie3()));
                }
            }
        }
        log.info("=========== Nunero de personas = " + personas.size());
        log.info("=========== FIN en obtainPersonasAnexos");
        return personas;
    }

    private boolean estaEnLista (final String dni, final List<Persona> personas) {
        for (final Persona p : personas) {
            if (p.getNumDocumento().equals(dni)) {
                return true;
            }
        }
        return false;
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
    // Fin tarea IKER - [40984] - Interoperabilidad - Consulta vida laboral desde la ficha de expediente

    // Inicio tarea #901087 NISAE- [] - Interoperabilidad - Consulta vida laboral - Horizontal - Ficha de expediente desde cualquier procedimiento con Modulo Extension.
    public String cargarVidaLaboral(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
                                    HttpServletRequest request, HttpServletResponse response) {

        log.info("=========== ENTRO en cargarVidaLaboral");
        melanbide_interop.cargarVidaLaboral(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente,
                request, response);
        return "/jsp/extension/melanbide86/pestanaVidaLaboral.jsp";
    }

    public void consultaVidaLaboralCVLBatchExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite,
                                                      String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            melanbide_interop.consultaVidaLaboralCVLBatchExpediente(codOrganizacion, codTramite, ocurrenciaTramite,
                    obtainPersonasAnexos (codOrganizacion, adapt, numExpediente),
                    numExpediente, request, response);
        } catch (final Exception ex) {
            log.error ("Error en consultaVidaLaboralCVLBatchExpediente " + ex.getMessage());
        }
    }
    // Fin tarea #901087 NISAE- [] - Interoperabilidad - Consulta vida laboral - Horizontal - Ficha de expediente desde cualquier procedimiento con Modulo Extension.
}
