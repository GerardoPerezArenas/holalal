package es.altia.flexia.integracion.moduloexterno.melanbide85;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.CamposFormulario;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide85.i18n.MeLanbide85I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide85.manager.MeLanbide85Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide85.util.ConstantesMeLanbide85;
import es.altia.flexia.integracion.moduloexterno.melanbide85.vo.FilaContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide85.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.FileNotFoundException;
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
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
import es.altia.flexia.integracion.moduloexterno.melanbide67.MELANBIDE67;
import es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.dto.SearchCVResponseDTO;
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
import java.util.Arrays;
import java.util.Date;

public class MELANBIDE85 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE85.class);
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    private static final int BUFFER_SIZE = 4096;
    private static MELANBIDE67 melanbide67 = new MELANBIDE67();

    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();

    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);

        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide85/melanbide85.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaContratacionVO> listaContrataciones = MeLanbide85Manager.getInstance().getListaContrataciones(numExpediente, codOrganizacion, adapt);
                final CamposFormulario camposFormularioFechas = MeLanbide85Manager.getInstance().getValoresFechas(numExpediente, adapt);
                final CamposFormulario camposFormularioFicheros = MeLanbide85Manager.getInstance().getFicheros(numExpediente, adapt);
                if (listaContrataciones.size() > 0) {

                    iterListaContrataciones(listaContrataciones, camposFormularioFechas,
                            camposFormularioFicheros);
                    request.setAttribute("listaContrataciones", listaContrataciones);
                    for (FilaContratacionVO contr : listaContrataciones) {
                        // Aquí me falta recuperar el documento y las fechas de la parte de intermediación.
                        contr.setDesdurcontrato1(getDescripcionDesplegable(request, contr.getDesdurcontrato1()));
                        contr.setDesdurcontrato2(getDescripcionDesplegable(request, contr.getDesdurcontrato2()));
                        contr.setDesdurcontrato3(getDescripcionDesplegable(request, contr.getDesdurcontrato3()));
                        contr.setDesgrupocotiz1(getDescripcionDesplegable(request, contr.getDesgrupocotiz1()));
                        contr.setDesgrupocotiz2(getDescripcionDesplegable(request, contr.getDesgrupocotiz2()));
                        contr.setDessexo2(getDescripcionDesplegable(request, contr.getDessexo2()));
                        contr.setDesempverde1(getDescripcionDesplegable(request, contr.getDesempverde1()));
                        contr.setDesempverde2(getDescripcionDesplegable(request, contr.getDesempverde2()));
                        contr.setDesempdigit1(getDescripcionDesplegable(request, contr.getDesempdigit1()));
                        contr.setDesempdigit2(getDescripcionDesplegable(request, contr.getDesempdigit2()));
                        contr.setDesempgen1(getDescripcionDesplegable(request, contr.getDesempgen1()));
                        contr.setDesempgen2(getDescripcionDesplegable(request, contr.getDesempgen2()));
                    }
                    request.setAttribute("listaContrataciones", listaContrataciones);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de contrataciones - MELANBIDE85 - cargarPantallaPrincipal", ex);
            }
        }

        return url;
    }

    public String cargarNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaContratacion - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide85/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;
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

            cargarDesplegables(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva contratación : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public String cargarModificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarContratacion - " + numExpediente);
        String nuevo = "0";
        String urlnuevaContratacion = "/jsp/extension/melanbide85/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;
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
            if (id != null && !id.equals("")) {
                FilaContratacionVO datModif = MeLanbide85Manager.getInstance().getContratacionPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            cargarDesplegables(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
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
            String numExp = request.getParameter("numExp");
            String numpuesto = request.getParameter("numpuesto");
            String denompuesto1 = request.getParameter("denompuesto1");
            String titulacion1 = request.getParameter("titulacion1");
            String municipioct1 = request.getParameter("municipioct1");
            String durcontrato1 = request.getParameter("durcontrato1");
            String grupocotiz1 = request.getParameter("grupocotiz1");
            String costesalarial1 = request.getParameter("costesalarial1").replace(",", ".");
            String costeepis1 = request.getParameter("costeepis1").replace(",", ".");
            String subvsolicitada1 = request.getParameter("subvsolicitada1").replace(",", ".");
            String empverde1 = request.getParameter("empverde1");
            String empdigit1 = request.getParameter("empdigit1");
            String empgen1 = request.getParameter("empgen1");

            log.debug("Recojo 2");
            String noferta2 = request.getParameter("noferta2");
            String nombre2 = request.getParameter("nombre2");
            String apellido12 = request.getParameter("apellido12");
            String apellido22 = request.getParameter("apellido22");
            String dninie2 = request.getParameter("dninie2");
            String sexo2 = request.getParameter("sexo2");
            String titulacion2 = request.getParameter("titulacion2");
            String denompuesto2 = request.getParameter("denompuesto2");
            String municipioct2 = request.getParameter("municipioct2");
            String grupocotiz2 = request.getParameter("grupocotiz2");
            String durcontrato2 = request.getParameter("durcontrato2");
            String fechanacimiento2 = request.getParameter("fechanacimiento2");
            String fechainicio2 = request.getParameter("fechainicio2");
            String edad = request.getParameter("edad");
            String retribucionbruta2 = request.getParameter("retribucionbruta2").replace(",", ".");
            String empverde2 = request.getParameter("empverde2");
            String empdigit2 = request.getParameter("empdigit2");
            String empgen2 = request.getParameter("empgen2");

            log.debug("Recojo 3");
            String nombre3 = request.getParameter("nombre3");
            String apellido13 = request.getParameter("apellido13");
            String apellido23 = request.getParameter("apellido23");
            String dninie3 = request.getParameter("dninie3");
            String durcontrato3 = request.getParameter("durcontrato3");
            String fechainicio3 = request.getParameter("fechainicio3");
            String fechafin3 = request.getParameter("fechafin3");
            String costesalarial3 = request.getParameter("costesalarial3").replace(",", ".");
            String costesss3 = request.getParameter("costesss3").replace(",", ".");
            String costeepis3 = request.getParameter("costeepis3").replace(",", ".");
            String costetotalreal3 = request.getParameter("costetotalreal3").replace(",", ".");
            String subvconcedidalan3 = request.getParameter("subvconcedidalan3").replace(",", ".");

            log.debug("Cargo 1");
            nuevaContratacion.setNumExp(numExp);
            nuevaContratacion.setNumpuesto(Integer.parseInt(numpuesto));
            nuevaContratacion.setDenompuesto1(denompuesto1);
            nuevaContratacion.setTitulacion1(titulacion1);
            nuevaContratacion.setMunicipioct1(municipioct1);
            nuevaContratacion.setDurcontrato1(durcontrato1);
            nuevaContratacion.setGrupocotiz1(grupocotiz1);
            nuevaContratacion.setCostesalarial1(Double.parseDouble(costesalarial1));
            nuevaContratacion.setCosteepis1(Double.parseDouble(costeepis1));
            nuevaContratacion.setSubvsolicitada1(Double.parseDouble(subvsolicitada1));
            nuevaContratacion.setEmpverde1(empverde1);
            nuevaContratacion.setEmpdigit1(empdigit1);
            nuevaContratacion.setEmpgen1(empgen1);

            // CONTRATACION_INI
            log.debug("Cargo 2");
            nuevaContratacion.setNumExp2(numExp);
            nuevaContratacion.setNumPuesto2(Integer.parseInt(numpuesto));
            if (noferta2 != null && !"".equalsIgnoreCase(noferta2)) {
                nuevaContratacion.setNoferta2(noferta2);
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
                if (dninie2 != null && !"".equalsIgnoreCase(dninie2)) {
                    nuevaContratacion.setDninie2(dninie2);
                }
                if (sexo2 != null && !"".equalsIgnoreCase(sexo2)) {
                    nuevaContratacion.setSexo2(sexo2);
                }
                if (titulacion2 != null && !"".equalsIgnoreCase(titulacion2)) {
                    nuevaContratacion.setTitulacion2(titulacion2);
                }
                if (denompuesto2 != null && !"".equalsIgnoreCase(denompuesto2)) {
                    nuevaContratacion.setDenompuesto2(denompuesto2);
                }
                if (municipioct2 != null && !"".equalsIgnoreCase(municipioct2)) {
                    nuevaContratacion.setMunicipioct2(municipioct2);
                }
                if (grupocotiz2 != null && !"".equalsIgnoreCase(grupocotiz2)) {
                    nuevaContratacion.setGrupocotiz2(grupocotiz2);
                }
                if (durcontrato2 != null && !"".equalsIgnoreCase(durcontrato2)) {
                    nuevaContratacion.setDurcontrato2(durcontrato2);
                }

                if (fechanacimiento2 != null && !"".equalsIgnoreCase(fechanacimiento2)) {
                    nuevaContratacion.setFechanacimiento2(new java.sql.Date(dateFormat.parse(fechanacimiento2).getTime()));
                }
                if (fechainicio2 != null && !"".equalsIgnoreCase(fechainicio2)) {
                    nuevaContratacion.setFechainicio2(new java.sql.Date(dateFormat.parse(fechainicio2).getTime()));
                }
                if (edad != null && !"".equalsIgnoreCase(edad)) {
                    nuevaContratacion.setEdad(Integer.parseInt(edad));
                }
                if (retribucionbruta2 != null && !"".equalsIgnoreCase(retribucionbruta2)) {
                    nuevaContratacion.setRetribucionbruta2(Double.parseDouble(retribucionbruta2));
                }
                if (empverde2 != null && !"".equalsIgnoreCase(empverde2)) {
                    nuevaContratacion.setEmpverde2(empverde2);
                }
                if (empdigit2 != null && !"".equalsIgnoreCase(empdigit2)) {
                    nuevaContratacion.setEmpdigit2(empdigit2);
                }
                if (empgen2 != null && !"".equalsIgnoreCase(empgen2)) {
                    nuevaContratacion.setEmpgen2(empgen2);
                }
            }

            // CONTRATACION_FIN
            log.debug("Cargo 3");
            nuevaContratacion.setNumExp3(numExp);
            nuevaContratacion.setNumPuesto3(Integer.parseInt(numpuesto));
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
                if (dninie3 != null && !"".equalsIgnoreCase(dninie3)) {
                    nuevaContratacion.setDninie3(dninie3);
                }
                if (durcontrato3 != null && !"".equalsIgnoreCase(durcontrato3)) {
                    nuevaContratacion.setDurcontrato3(durcontrato3);
                }
                if (fechainicio3 != null && !"".equalsIgnoreCase(fechainicio3)) {
                    nuevaContratacion.setFechainicio3(new java.sql.Date(dateFormat.parse(fechainicio3).getTime()));
                }
                if (fechafin3 != null && !"".equalsIgnoreCase(fechafin3)) {
                    nuevaContratacion.setFechafin3(new java.sql.Date(dateFormat.parse(fechafin3).getTime()));
                }
                if (costesalarial3 != null && !"".equalsIgnoreCase(costesalarial3)) {
                    nuevaContratacion.setCostesalarial3(Double.parseDouble(costesalarial3));
                }
                if (costesss3 != null && !"".equalsIgnoreCase(costesss3)) {
                    nuevaContratacion.setCostesss3(Double.parseDouble(costesss3));
                }
                if (costeepis3 != null && !"".equalsIgnoreCase(costeepis3)) {
                    nuevaContratacion.setCosteepis3(Double.parseDouble(costeepis3));
                }
                if (costetotalreal3 != null && !"".equalsIgnoreCase(costetotalreal3)) {
                    nuevaContratacion.setCostetotalreal3(Double.parseDouble(costetotalreal3));
                }
                if (subvconcedidalan3 != null && !"".equalsIgnoreCase(subvconcedidalan3)) {
                    nuevaContratacion.setSubvconcedidalan3(Double.parseDouble(subvconcedidalan3));
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

            MeLanbide85Manager meLanbide85Manager = MeLanbide85Manager.getInstance();
            boolean insertOK = meLanbide85Manager.crearContratacion(nuevaContratacion, tablas, adapt);
            if (insertOK) {
                log.debug("Contratacion Insertada Correctamente");
                codigoOperacion = "0";
                lista = meLanbide85Manager.getListaContrataciones(numExp, codOrganizacion, adapt);
                if (lista.size() > 0) {
                    final CamposFormulario camposFormularioFechas = MeLanbide85Manager.getInstance().getValoresFechas(numExp, adapt);
                    final CamposFormulario camposFormularioFicheros = MeLanbide85Manager.getInstance().getFicheros(numExp, adapt);
                    iterListaContrataciones(lista, camposFormularioFechas,
                            camposFormularioFicheros);
                    for (FilaContratacionVO contr : lista) {
                        contr.setDesdurcontrato1(getDescripcionDesplegable(request, contr.getDesdurcontrato1()));
                        contr.setDesdurcontrato2(getDescripcionDesplegable(request, contr.getDesdurcontrato2()));
                        contr.setDesdurcontrato3(getDescripcionDesplegable(request, contr.getDesdurcontrato3()));
                        contr.setDesgrupocotiz1(getDescripcionDesplegable(request, contr.getDesgrupocotiz1()));
                        contr.setDesgrupocotiz2(getDescripcionDesplegable(request, contr.getDesgrupocotiz2()));
                        contr.setDessexo2(getDescripcionDesplegable(request, contr.getDessexo2()));
                        contr.setDesempverde1(getDescripcionDesplegable(request, contr.getDesempverde1()));
                        contr.setDesempverde2(getDescripcionDesplegable(request, contr.getDesempverde2()));
                        contr.setDesempdigit1(getDescripcionDesplegable(request, contr.getDesempdigit1()));
                        contr.setDesempdigit2(getDescripcionDesplegable(request, contr.getDesempdigit2()));
                        contr.setDesempgen1(getDescripcionDesplegable(request, contr.getDesempgen1()));
                        contr.setDesempgen2(getDescripcionDesplegable(request, contr.getDesempgen2()));
                    }
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
        log.info("Entramos en modificarContratacion - " + request.getParameter("numExp"));
        String codigoOperacion = "-1";
        List<FilaContratacionVO> listaContrataciones = new ArrayList<FilaContratacionVO>();
        int tablas = 1;
        boolean tabla2 = false;
        boolean tabla3 = false;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String numpuesto = request.getParameter("numpuesto");
            String denompuesto1 = request.getParameter("denompuesto1");
            String titulacion1 = request.getParameter("titulacion1");
            String municipioct1 = request.getParameter("municipioct1");
            String durcontrato1 = request.getParameter("durcontrato1");
            String grupocotiz1 = request.getParameter("grupocotiz1");
            String costesalarial1 = request.getParameter("costesalarial1").replace(",", ".");
            String costeepis1 = request.getParameter("costeepis1").replace(",", ".");
            String subvsolicitada1 = request.getParameter("subvsolicitada1").replace(",", ".");
            String empverde1 = request.getParameter("empverde1");
            String empdigit1 = request.getParameter("empdigit1");
            String empgen1 = request.getParameter("empgen1");

            String noferta2 = request.getParameter("noferta2");
            String nombre2 = request.getParameter("nombre2");
            String apellido12 = request.getParameter("apellido12");
            String apellido22 = request.getParameter("apellido22");
            String dninie2 = request.getParameter("dninie2");
            String sexo2 = request.getParameter("sexo2");
            String titulacion2 = request.getParameter("titulacion2");
            String denompuesto2 = request.getParameter("denompuesto2");
            String municipioct2 = request.getParameter("municipioct2");
            String grupocotiz2 = request.getParameter("grupocotiz2");
            String durcontrato2 = request.getParameter("durcontrato2");
            String fechanacimiento2 = request.getParameter("fechanacimiento2");
            String fechainicio2 = request.getParameter("fechainicio2");
            String edad = request.getParameter("edad");
            String retribucionbruta2 = request.getParameter("retribucionbruta2").replace(",", ".");
            String empverde2 = request.getParameter("empverde2");
            String empdigit2 = request.getParameter("empdigit2");
            String empgen2 = request.getParameter("empgen2");

            String nombre3 = request.getParameter("nombre3");
            String apellido13 = request.getParameter("apellido13");
            String apellido23 = request.getParameter("apellido23");
            String dninie3 = request.getParameter("dninie3");
            String durcontrato3 = request.getParameter("durcontrato3");
            String fechainicio3 = request.getParameter("fechainicio3");
            String fechafin3 = request.getParameter("fechafin3");
            String costesalarial3 = request.getParameter("costesalarial3").replace(",", ".");
            String costesss3 = request.getParameter("costesss3").replace(",", ".");
            String costeepis3 = request.getParameter("costeepis3").replace(",", ".");
            String costetotalreal3 = request.getParameter("costetotalreal3").replace(",", ".");
            String subvconcedidalan3 = request.getParameter("subvconcedidalan3").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Contratacion a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaContratacionVO contratacionModif = new FilaContratacionVO();

                contratacionModif.setId(Integer.parseInt(id));
                contratacionModif.setNumExp(numExp);
                contratacionModif.setNumpuesto(Integer.parseInt(numpuesto));
                contratacionModif.setDenompuesto1(denompuesto1);
                contratacionModif.setTitulacion1(titulacion1);
                contratacionModif.setMunicipioct1(municipioct1);
                contratacionModif.setDurcontrato1(durcontrato1);
                contratacionModif.setGrupocotiz1(grupocotiz1);
                contratacionModif.setCostesalarial1(Double.parseDouble(costesalarial1));
                contratacionModif.setCosteepis1(Double.parseDouble(costeepis1));
                contratacionModif.setSubvsolicitada1(Double.parseDouble(subvsolicitada1));
                contratacionModif.setEmpverde1(empverde1);
                contratacionModif.setEmpdigit1(empdigit1);
                contratacionModif.setEmpgen1(empgen1);

                // CONTRATACION_INI
                contratacionModif.setNumExp2(numExp);
                contratacionModif.setNumPuesto2(Integer.parseInt(numpuesto));
                if (noferta2 != null && !"".equalsIgnoreCase(noferta2)) {
                    contratacionModif.setNoferta2(noferta2);
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
                contratacionModif.setDenompuesto2(denompuesto2);
                if (tabla2) {
                    if (apellido22 != null && !"".equalsIgnoreCase(apellido22)) {
                        contratacionModif.setApellido22(apellido22);
                    }
                    if (dninie2 != null && !"".equalsIgnoreCase(dninie2)) {
                        contratacionModif.setDninie2(dninie2);
                    }
                    if (sexo2 != null && !"".equalsIgnoreCase(sexo2)) {
                        contratacionModif.setSexo2(sexo2);
                    }
                    if (titulacion2 != null && !"".equalsIgnoreCase(titulacion2)) {
                        contratacionModif.setTitulacion2(titulacion2);
                    }
                    if (municipioct2 != null && !"".equalsIgnoreCase(municipioct2)) {
                        contratacionModif.setMunicipioct2(municipioct2);
                    }
                    if (grupocotiz2 != null && !"".equalsIgnoreCase(grupocotiz2)) {
                        contratacionModif.setGrupocotiz2(grupocotiz2);
                    }
                    if (durcontrato2 != null && !"".equalsIgnoreCase(durcontrato2)) {
                        contratacionModif.setDurcontrato2(durcontrato2);
                    }
                    if (fechanacimiento2 != null && !"".equalsIgnoreCase(fechanacimiento2)) {
                        contratacionModif.setFechanacimiento2(new java.sql.Date(dateFormat.parse(fechanacimiento2).getTime()));
                    }
                    if (fechainicio2 != null && !"".equalsIgnoreCase(fechainicio2)) {
                        contratacionModif.setFechainicio2(new java.sql.Date(dateFormat.parse(fechainicio2).getTime()));
                    }
                    if (edad != null && !"".equalsIgnoreCase(edad)) {
                        contratacionModif.setEdad(Integer.parseInt(edad));
                    }
                    if (retribucionbruta2 != null && !"".equalsIgnoreCase(retribucionbruta2)) {
                        contratacionModif.setRetribucionbruta2(Double.parseDouble(retribucionbruta2));
                    }
                    if (empverde2 != null && !"".equalsIgnoreCase(empverde2)) {
                        contratacionModif.setEmpverde2(empverde2);
                    }
                    if (empdigit2 != null && !"".equalsIgnoreCase(empdigit2)) {
                        contratacionModif.setEmpdigit2(empdigit2);
                    }
                    if (empgen2 != null && !"".equalsIgnoreCase(empgen2)) {
                        contratacionModif.setEmpgen2(empgen2);
                    }
                }

                // CONTRATACION_FIN
                contratacionModif.setNumExp3(numExp);
                contratacionModif.setNumPuesto3(Integer.parseInt(numpuesto));
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
                    if (dninie3 != null && !"".equalsIgnoreCase(dninie3)) {
                        contratacionModif.setDninie3(dninie3);
                    }
                    if (durcontrato3 != null && !"".equalsIgnoreCase(durcontrato3)) {
                        contratacionModif.setDurcontrato3(durcontrato3);
                    }
                    if (fechainicio3 != null && !"".equalsIgnoreCase(fechainicio3)) {
                        contratacionModif.setFechainicio3(new java.sql.Date(dateFormat.parse(fechainicio3).getTime()));
                    }
                    if (fechafin3 != null && !"".equalsIgnoreCase(fechafin3)) {
                        contratacionModif.setFechafin3(new java.sql.Date(dateFormat.parse(fechafin3).getTime()));
                    }
                    if (costesalarial3 != null && !"".equalsIgnoreCase(costesalarial3)) {
                        contratacionModif.setCostesalarial3(Double.parseDouble(costesalarial3));
                    }
                    if (costesss3 != null && !"".equalsIgnoreCase(costesss3)) {
                        contratacionModif.setCostesss3(Double.parseDouble(costesss3));
                    }
                    if (costeepis3 != null && !"".equalsIgnoreCase(costeepis3)) {
                        contratacionModif.setCosteepis3(Double.parseDouble(costeepis3));
                    }
                    if (costetotalreal3 != null && !"".equalsIgnoreCase(costetotalreal3)) {
                        contratacionModif.setCostetotalreal3(Double.parseDouble(costetotalreal3));
                    }
                    if (subvconcedidalan3 != null && !"".equalsIgnoreCase(subvconcedidalan3)) {
                        contratacionModif.setSubvconcedidalan3(Double.parseDouble(subvconcedidalan3));
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

                MeLanbide85Manager meLanbide85Manager = MeLanbide85Manager.getInstance();
                boolean modOK = meLanbide85Manager.modificarContratacion(contratacionModif, tablas, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        listaContrataciones = meLanbide85Manager.getListaContrataciones(numExp, codOrganizacion, adapt);
                        if (listaContrataciones.size() > 0) {
                            final CamposFormulario camposFormularioFechas = MeLanbide85Manager.getInstance().getValoresFechas(numExp, adapt);
                            final CamposFormulario camposFormularioFicheros = MeLanbide85Manager.getInstance().getFicheros(numExp, adapt);
                            iterListaContrataciones(listaContrataciones, camposFormularioFechas,
                                    camposFormularioFicheros);
                            for (FilaContratacionVO contr : listaContrataciones) {
                                contr.setDesdurcontrato1(getDescripcionDesplegable(request, contr.getDesdurcontrato1()));
                                contr.setDesdurcontrato2(getDescripcionDesplegable(request, contr.getDesdurcontrato2()));
                                contr.setDesdurcontrato3(getDescripcionDesplegable(request, contr.getDesdurcontrato3()));
                                contr.setDesgrupocotiz1(getDescripcionDesplegable(request, contr.getDesgrupocotiz1()));
                                contr.setDesgrupocotiz2(getDescripcionDesplegable(request, contr.getDesgrupocotiz2()));
                                contr.setDessexo2(getDescripcionDesplegable(request, contr.getDessexo2()));
                                contr.setDesempverde1(getDescripcionDesplegable(request, contr.getDesempverde1()));
                                contr.setDesempverde2(getDescripcionDesplegable(request, contr.getDesempverde2()));
                                contr.setDesempdigit1(getDescripcionDesplegable(request, contr.getDesempdigit1()));
                                contr.setDesempdigit2(getDescripcionDesplegable(request, contr.getDesempdigit2()));
                                contr.setDesempgen1(getDescripcionDesplegable(request, contr.getDesempgen1()));
                                contr.setDesempgen2(getDescripcionDesplegable(request, contr.getDesempgen2()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Contrataciones después de Modificar una Contratacion : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de Contrataciones después de Modificar una Contratacion : " + ex.getMessage());
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
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Contratacion a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide85Manager.getInstance().eliminarContratacion(id, numExp, docCV, docDemanda,
                        codOrganizacion, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                       
                        listaContrataciones = MeLanbide85Manager.getInstance().getListaContrataciones(numExp, codOrganizacion, adapt);
                        if (listaContrataciones.size() > 0) {
                            final CamposFormulario camposFormularioFechas = MeLanbide85Manager.getInstance().getValoresFechas(numExp, adapt);
                            final CamposFormulario camposFormularioFicheros = MeLanbide85Manager.getInstance().getFicheros(numExp, adapt);
                            iterListaContrataciones(listaContrataciones, camposFormularioFechas,
                                    camposFormularioFicheros);
                            for (FilaContratacionVO contr : listaContrataciones) {
                                contr.setDesdurcontrato1(getDescripcionDesplegable(request, contr.getDesdurcontrato1()));
                                contr.setDesdurcontrato2(getDescripcionDesplegable(request, contr.getDesdurcontrato2()));
                                contr.setDesdurcontrato3(getDescripcionDesplegable(request, contr.getDesdurcontrato3()));
                                contr.setDesgrupocotiz1(getDescripcionDesplegable(request, contr.getDesgrupocotiz1()));
                                contr.setDesgrupocotiz2(getDescripcionDesplegable(request, contr.getDesgrupocotiz2()));
                                contr.setDessexo2(getDescripcionDesplegable(request, contr.getDessexo2()));
                                contr.setDesempverde1(getDescripcionDesplegable(request, contr.getDesempverde1()));
                                contr.setDesempverde2(getDescripcionDesplegable(request, contr.getDesempverde2()));
                                contr.setDesempdigit1(getDescripcionDesplegable(request, contr.getDesempdigit1()));
                                contr.setDesempdigit2(getDescripcionDesplegable(request, contr.getDesempdigit2()));
                                contr.setDesempgen1(getDescripcionDesplegable(request, contr.getDesempgen1()));
                                contr.setDesempgen2(getDescripcionDesplegable(request, contr.getDesempgen2()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de Contrataciones después de eliminar una Contratacion");
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

            request.getSession().setAttribute("mensajeImportar", MeLanbide85I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
        } catch (DataFormatException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE85.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE85.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE85.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE85.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide85I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarListaContrataciones.jsp");
        return "/jsp/extension/melanbide85/recargarListaContrataciones.jsp";
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
            lista = MeLanbide85Manager.getInstance().getListaContrataciones(numExp, codOrganizacion, adapt);
            if (lista.isEmpty()) {
                log.error("No se ha cargado ningún registro para el expediente " + numExp + " después de cargar el XML");
                codigoOperacion = "4";
            } else {
                codigoOperacion = "0";
                final CamposFormulario camposFormularioFechas = MeLanbide85Manager.getInstance().getValoresFechas(numExp, adapt);
                final CamposFormulario camposFormularioFicheros = MeLanbide85Manager.getInstance().getFicheros(numExp, adapt);
                iterListaContrataciones(lista, camposFormularioFechas,
                        camposFormularioFicheros);
                for (FilaContratacionVO contr : lista) {
                    contr.setDesdurcontrato1(getDescripcionDesplegable(request, contr.getDesdurcontrato1()));
                    contr.setDesdurcontrato2(getDescripcionDesplegable(request, contr.getDesdurcontrato2()));
                    contr.setDesdurcontrato3(getDescripcionDesplegable(request, contr.getDesdurcontrato3()));
                    contr.setDesgrupocotiz1(getDescripcionDesplegable(request, contr.getDesgrupocotiz1()));
                    contr.setDesgrupocotiz2(getDescripcionDesplegable(request, contr.getDesgrupocotiz2()));
                    contr.setDessexo2(getDescripcionDesplegable(request, contr.getDessexo2()));
                    contr.setDesempverde1(getDescripcionDesplegable(request, contr.getDesempverde1()));
                    contr.setDesempverde2(getDescripcionDesplegable(request, contr.getDesempverde2()));
                    contr.setDesempdigit1(getDescripcionDesplegable(request, contr.getDesempdigit1()));
                    contr.setDesempdigit2(getDescripcionDesplegable(request, contr.getDesempdigit2()));
                    contr.setDesempgen1(getDescripcionDesplegable(request, contr.getDesempgen1()));
                    contr.setDesempgen2(getDescripcionDesplegable(request, contr.getDesempgen2()));
                }
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
            listaContrataciones = MeLanbide85Manager.getInstance().getListaContrataciones(numExpediente, codOrganizacion, adapt);
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al invocar getListaContrataciones ", ex);
        }
        final Date fechaDemandaInter = new Date();
        int i = 0;
        for (final FilaContratacionVO contr : listaContrataciones) {
            log.debug("recuperarDatosCVoDemandaIntermediacion DNI = " + contr);
            if (contr.getDninie2() != null && !contr.getDninie2().equals("")) {
                final SearchCVResponseDTO respuestaDni2 = melanbide67.recuperarDatosCVoDemandaIntermediacion(codOrganizacion,
                        contr.getDninie2(), request.getParameter("documento"), numExpediente, request, response);
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
        log.info("Inicio de obtainDocumentFile numExp = " + request.getParameter("numExp") + " de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        final String numExp = request.getParameter("numExp");
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        if (adapt != null) {
            try {
                final CamposFormulario camposFormularioFicheros = MeLanbide85Manager.getInstance().getFicheros(numExp, adapt);

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
                fila.setFechaCv2Str(dateFormat.format(fila.getFechaCv2()));
            }
            if (camposFormularioFechas.contieneCampo("FECHA" + "DEM" + "2" + i + "INTER")) {
                obj = camposFormularioFechas.get("FECHA" + "DEM" + "2" + i + "INTER");
                dateString = (String) obj;
                dateSql = new java.sql.Date(dateFormat.parse(dateString).getTime());
                fila.setFechaDemanda2(dateSql);
                fila.setFechaDemanda2Str(dateFormat.format(fila.getFechaDemanda2()));
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
                MeLanbide85Manager.getInstance().setDatosCVoDemandaIntermediacion(numExpediente,
                        fechaDocInter, nameFieldFecha, docFile, nameFieldDoc, adapt);
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
//            log.info("getDocFile - bytesRead = " + bytesRead);
            fileOutputStream.write(dataBuffer, 0, bytesRead);
        }

        fileOutputStream.close();
        in.close();
        log.info("getDocFile - End()");
        return new File(nameFile);
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
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class
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

    /**
     * Método que recupera los valores de los desplegables del módulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegables(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaMesContrato = MeLanbide85Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide85.COD_DES_MEPE, ConstantesMeLanbide85.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaGrupoCot = MeLanbide85Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide85.COD_DES_GCON, ConstantesMeLanbide85.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaSexo = MeLanbide85Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide85.COD_DES_SEXO, ConstantesMeLanbide85.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaBool = MeLanbide85Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide85.COD_DES_BOOL, ConstantesMeLanbide85.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

        if (listaMesContrato.size() > 0) {
            listaMesContrato = traducirDesplegable(request, listaMesContrato);
            request.setAttribute("listaMesContrato", listaMesContrato);
        }
        if (listaGrupoCot.size() > 0) {
            listaGrupoCot = traducirDesplegable(request, listaGrupoCot);
            request.setAttribute("listaGrupoCot", listaGrupoCot);
        }
        if (listaSexo.size() > 0) {
            listaSexo = traducirDesplegable(request, listaSexo);
            request.setAttribute("listaSexo", listaSexo);
        }
        if (listaBool.size() > 0) {
            listaBool = traducirDesplegable(request, listaBool);
            request.setAttribute("listaBool", listaBool);
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
    private List<DesplegableAdmonLocalVO> traducirDesplegable(HttpServletRequest request, List<DesplegableAdmonLocalVO> desplegable) {

        for (DesplegableAdmonLocalVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().equals("")) {
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

        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide85.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide85.FICHERO_PROPIEDADES);

        try {
            if (!descripcion.isEmpty()) {

                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide85.CODIGO_IDIOMA_EUSKERA) {
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
        int idioma = ConstantesMeLanbide85.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {

            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide85.CODIGO_IDIOMA_CASTELLANO;
        }

        return idioma;
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

}
