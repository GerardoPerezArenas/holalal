package es.altia.flexia.integracion.moduloexterno.melanbide15;

import com.google.gson.Gson;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide15.manager.MeLanbide15Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConstantesMeLanbide15;
import es.altia.flexia.integracion.moduloexterno.melanbide15.util.MeLanbide15Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.FormacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.IdentidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.OrientacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MELANBIDE15 extends ModuloIntegracionExterno {

    private static final MeLanbide15Utils m15Utils = new MeLanbide15Utils();
    private final MeLanbide15Manager m15Manager = new MeLanbide15Manager();
    private static Logger log = LogManager.getLogger(MELANBIDE15.class);
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);

        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaCATP(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws BDException {
        log.info("Entramos en cargarPantallaCATP de " + this.getClass().getSimpleName());

        request.setAttribute("numExp", numExpediente);

        AdaptadorSQLBD adapt = null;
        Connection con = null;

        try {
            adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adapt.getConnection();

            // Procesar cada pestaña con métodos genéricos
            procesarPestana(adapt, request, codOrganizacion, numExpediente, "Identidad", "/jsp/extension/melanbide15/identidad.jsp");
            procesarPestana(adapt, request, codOrganizacion, numExpediente, "Formacion", "/jsp/extension/melanbide15/formacion.jsp");
            procesarPestana(adapt, request, codOrganizacion, numExpediente, "Orientacion", "/jsp/extension/melanbide15/orientacion.jsp");
            procesarPestana(adapt, request, codOrganizacion, numExpediente, "Contratacion", "/jsp/extension/melanbide15/contratacion.jsp");

        } catch (SQLException ex) {
            log.error("Error al procesar las subpestañas: ", ex);
        } finally {
            if (con != null) {
                adapt.devolverConexion(con); // Aquí no necesitamos el try-catch porque el método ya lanza BDException
            }
        }

        log.info("Antes de retornar la URL: /jsp/extension/melanbide15/catpPrincipal.jsp");
        return "/jsp/extension/melanbide15/catpPrincipal.jsp";
    }

// Método genérico para procesar subpestañas
    private void procesarPestana(AdaptadorSQLBD adapt, HttpServletRequest request, int codOrganizacion, String numExpediente, String tipoPestana, String url) {
        try {
            // Obtiene la lista correspondiente al tipo de pestaña
            List<?> lista = obtenerListaPorTipo(tipoPestana, numExpediente, codOrganizacion, adapt, request);

            if (lista != null && !lista.isEmpty()) {
                // Procesa cada elemento en la lista
                for (Object item : lista) {
                    procesarDescripcion(item, request, tipoPestana);
                }

                // Establece la lista procesada en el request
                request.setAttribute("lista" + tipoPestana, lista);
            }

            // Establece la URL de la pestaña en el request
            request.setAttribute("urlPestana" + tipoPestana, url);

        } catch (Exception ex) {
            log.error("Error procesando pestaña " + tipoPestana + ": ", ex);
        }
    }

// Método para obtener listas según el tipo de pestaña
    private List<?> obtenerListaPorTipo(String tipoPestana, String numExpediente, int codOrganizacion, AdaptadorSQLBD adapt, HttpServletRequest request) throws Exception {
        if ("Identidad".equals(tipoPestana)) {
            request.setAttribute("urlPestana", "/jsp/extension/melanbide15/identidad.jsp");
            return m15Manager.getListaIdentidad(numExpediente, codOrganizacion, adapt);

        } else if ("Formacion".equals(tipoPestana)) {
            request.setAttribute("urlPestana", "/jsp/extension/melanbide15/formacion.jsp");
            return m15Manager.getListaFormacion(numExpediente, codOrganizacion, adapt);

        } else if ("Orientacion".equals(tipoPestana)) {
            request.setAttribute("urlPestana", "/jsp/extension/melanbide15/orientacion.jsp");
            return m15Manager.getListaOrientacion(numExpediente, codOrganizacion, adapt);

        } else if ("Contratacion".equals(tipoPestana)) {
            request.setAttribute("urlPestana", "/jsp/extension/melanbide15/contratacion.jsp");
            return m15Manager.getListaContratacion(numExpediente, codOrganizacion, adapt);

        } else {
            log.error("Tipo de pestaña desconocido: " + tipoPestana);
            return new ArrayList(); // Lista vacía si no se reconoce el tipo
        }
    }

// Método para procesar la descripción de elementos según tipo de pestaña
    private void procesarDescripcion(Object item, HttpServletRequest request, String tipoPestana) {
        try {
            if ("Identidad".equals(tipoPestana)) {
                IdentidadVO identidad = (IdentidadVO) item;
                identidad.setDescDniNie(m15Utils.getDescripcionDesplegable(request, identidad.getDescDniNie()));
                identidad.setDescSexo(m15Utils.getDescripcionDesplegable(request, identidad.getDescSexo()));
                identidad.setDescSustituto(m15Utils.getDescripcionDesplegable(request, identidad.getDescSustituto()));

            } else if ("Formacion".equals(tipoPestana)) {
                FormacionVO formacion = (FormacionVO) item;
                formacion.setDescDniFor(m15Utils.getDescripcionDesplegable(request, formacion.getDescDniFor()));

            } else if ("Orientacion".equals(tipoPestana)) {
                OrientacionVO orientacion = (OrientacionVO) item;
                orientacion.setDescDniOri(m15Utils.getDescripcionDesplegable(request, orientacion.getDescDniOri()));

            } else if ("Contratacion".equals(tipoPestana)) {
                ContratacionVO contratacion = (ContratacionVO) item;
                contratacion.setDescDniCon(m15Utils.getDescripcionDesplegable(request, contratacion.getDescDniCon()));
            }
        } catch (Exception ex) {
            log.error("Error procesando descripciones para " + tipoPestana + ": ", ex);
        }
    }

    public String cargarNuevaIdentidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaIdentidad - " + numExpediente);
        String nuevo = "1";
        String urlnuevaIdentidad = "/jsp/extension/melanbide15/nuevaIdentidad.jsp?codOrganizacion=" + codOrganizacion;
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
            cargarDesplegablesIdentidad(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva identidad : " + ex.getMessage());
        }
        return urlnuevaIdentidad;
    }

    public String cargarModificarIdentidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarMinimis - " + numExpediente);
        String nuevo = "0";
        String urlnuevaIdentidad = "/jsp/extension/melanbide15/nuevaIdentidad.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                IdentidadVO datModif = m15Manager.getIdentidadPorID(id, m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            cargarDesplegablesIdentidad(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlnuevaIdentidad;

    }

    public void crearNuevaIdentidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevaIdentidad - " + numExpediente);
        String codigoOperacion = "-1";
        List<IdentidadVO> lista = new ArrayList<IdentidadVO>();
        IdentidadVO nuevaIdentidad = new IdentidadVO();

        try {
            AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");

            String dniNie = (String) request.getParameter("dniNie");
            String numIden = (String) request.getParameter("numIden");
            String nombre = (String) request.getParameter("nombre");
            String apellido1 = (String) request.getParameter("apellido1");
            String apellido2 = (String) request.getParameter("apellido2");
            String sexo = (String) request.getParameter("sexo");
            String fechaNacimiento = (String) request.getParameter("fechaNacimiento");

            String sustituto = (String) request.getParameter("sustituto");

            nuevaIdentidad.setNumExp(numExp);
            nuevaIdentidad.setDniNie(dniNie);
            nuevaIdentidad.setNumIden(numIden);
            nuevaIdentidad.setNombre(nombre);
            nuevaIdentidad.setApellido1(apellido1);
            nuevaIdentidad.setApellido2(apellido2);
            nuevaIdentidad.setSexo(sexo);
            if (fechaNacimiento != null && !"".equals(fechaNacimiento)) {
                nuevaIdentidad.setFechaNacimiento(new java.sql.Date(dateFormat.parse(fechaNacimiento).getTime()));
            }
            nuevaIdentidad.setSustituto(sustituto);
            MeLanbide15Manager meLanbide15Manager = m15Manager.getInstance();
            boolean insertOK = m15Manager.crearNuevaIdentidad(nuevaIdentidad, adapt);
            if (insertOK) {
                log.debug("identidad insertada correctamente");
                codigoOperacion = "0";
                lista = m15Manager.getListaIdentidad(numExp, codOrganizacion, adapt);
                if (lista.size() > 0) {
                    for (IdentidadVO idem : lista) {
                        idem.setDescDniNie(m15Utils.getDescripcionDesplegable(request, idem.getDescDniNie()));
                        idem.setDescSexo(m15Utils.getDescripcionDesplegable(request, idem.getDescSexo()));
                        idem.setDescSustituto(m15Utils.getDescripcionDesplegable(request, idem.getDescSustituto()));
                    }
                }
            } else {
                log.debug("NO se ha insertado correctamente la nueva identidad");
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

    public void modificarIdentidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarIdentidad - " + numExpediente);
        String codigoOperacion = "-1";
        List<IdentidadVO> lista = new ArrayList<IdentidadVO>();

        try {
            AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("numExp");

            String dniNie = (String) request.getParameter("dniNie");
            String numIden = (String) request.getParameter("numIden");
            String nombre = (String) request.getParameter("nombre");
            String apellido1 = (String) request.getParameter("apellido1");
            String apellido2 = (String) request.getParameter("apellido2");
            String sexo = (String) request.getParameter("sexo");
            String fechaNacimiento = (String) request.getParameter("fechaNacimiento");
            String sustituto = (String) request.getParameter("sustituto");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la identidad a Modificar ");
                codigoOperacion = "3";
            } else {
                IdentidadVO datModif = new IdentidadVO();

                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setDniNie(dniNie);
                datModif.setNumIden(numIden);
                datModif.setNombre(nombre);
                datModif.setApellido1(apellido1);
                datModif.setApellido2(apellido2);
                datModif.setSexo(sexo);

                if (fechaNacimiento != null && !"".equals(fechaNacimiento)) {
                    datModif.setFechaNacimiento(new java.sql.Date(dateFormat.parse(fechaNacimiento).getTime()));
                }
                datModif.setSustituto(sustituto);

                MeLanbide15Manager meLanbide15Manager = m15Manager.getInstance();
                boolean modOK = m15Manager.modificarIdentidad(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = m15Manager.getListaIdentidad(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (IdentidadVO idem : lista) {
                                idem.setDescDniNie(m15Utils.getDescripcionDesplegable(request, idem.getDescDniNie()));
                                idem.setDescSexo(m15Utils.getDescripcionDesplegable(request, idem.getDescSexo()));
                                idem.setDescSustituto(m15Utils.getDescripcionDesplegable(request, idem.getDescSustituto()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de identidad después de Modificar una identidad : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de identidad después de Modificar una identidad : " + ex.getMessage());
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

    public void eliminarIdentidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarIdentidad - " + numExpediente);

        String codigoOperacion = "-1"; // Código de operación por defecto
        List<IdentidadVO> lista = new ArrayList<IdentidadVO>();

        try {
            // Obtener el ID de la identidad desde la solicitud
            String idParam = request.getParameter("id");
            String numExp = request.getParameter("numExp");

            if (idParam == null || idParam.isEmpty() || numExp == null || numExp.isEmpty()) {
                log.debug("No se ha recibido desde la JSP el id o el número de expediente de la identidad a eliminar.");
                codigoOperacion = "3"; // Código para error de entrada
            } else {
                // Convertir el parámetro ID a entero
                int id = Integer.parseInt(idParam);

                AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

                // Ejecutar la eliminación
                int result = m15Manager.eliminarIdentidad(id, numExp, adapt);
                if (result <= 0) {
                    codigoOperacion = "6"; // Código para fallo al eliminar
                } else {
                    codigoOperacion = "0"; // Código para operación exitosa

                    try {
                        // Recuperar la lista actualizada de identidades
                        lista = m15Manager.getListaIdentidad(numExp, codOrganizacion, adapt);

                        for (IdentidadVO idem : lista) {
                            // Actualizar las descripciones de los desplegables
                            idem.setDescDniNie(m15Utils.getDescripcionDesplegable(request, idem.getDescDniNie()));
                            idem.setDescSexo(m15Utils.getDescripcionDesplegable(request, idem.getDescSexo()));
                            idem.setDescSustituto(m15Utils.getDescripcionDesplegable(request, idem.getDescSustituto()));
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5"; // Código para error al recuperar lista
                        log.error("Error al recuperar la lista de identidad después de eliminar una identidad.", ex);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            codigoOperacion = "3"; // Código para error en la entrada (ID no válido)
            log.error("El parámetro 'id' no es un número válido: " + request.getParameter("id"), ex);
        } catch (Exception ex) {
            codigoOperacion = "2"; // Código para error general
            log.error("Error eliminando una identidad.", ex);
        }

        // Crear el objeto de respuesta
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);

        if ("0".equalsIgnoreCase(codigoOperacion)) {
            resultado.setAtributo("lista", lista);
        }

        // Enviar la respuesta como JSON
        retornarJSON(new Gson().toJson(resultado), response);
    }

    private void cargarDesplegablesIdentidad(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaDniNie = m15Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_DNINIE, ConstantesMeLanbide15.FICHERO_PROPIEDADES), m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaDniNie.isEmpty()) {
            listaDniNie = m15Utils.traducirDesplegable(request, listaDniNie);
            request.setAttribute("listaDniNie", listaDniNie);
        }
        List<DesplegableVO> listaSexo = m15Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_SEXO, ConstantesMeLanbide15.FICHERO_PROPIEDADES), m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaSexo.isEmpty()) {
            listaSexo = m15Utils.traducirDesplegable(request, listaSexo);
            request.setAttribute("listaSexo", listaSexo);
        }
        List<DesplegableVO> listaSustituto = m15Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_BOOL, ConstantesMeLanbide15.FICHERO_PROPIEDADES), m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (listaSustituto.size() > 0) {
            listaSustituto = m15Utils.traducirDesplegable(request, listaSustituto);
            request.setAttribute("listaSustituto", listaSustituto);
        }

    }

    public String cargarNuevaFormacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaFormacion - " + numExpediente);

        String nuevo = "1";
        String numExp = "";
        String urlNuevaFormacion = "/jsp/extension/melanbide15/nuevaFormacion.jsp?codOrganizacion=" + codOrganizacion;

        try {
            // Verificar y establecer el atributo "nuevo"
            if (request.getAttribute("nuevo") != null) {
                if (!"0".equals(request.getAttribute("nuevo").toString())) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }

            // Verificar y establecer el número de expediente
            if (request.getParameter("numExp") != null && !request.getParameter("numExp").trim().isEmpty()) {
                numExp = request.getParameter("numExp").trim();
                request.setAttribute("numExp", numExp);
            } else {
                log.warn("El número de expediente no se recibió correctamente.");
            }

            // Cargar los desplegables necesarios para la JSP
            cargarDesplegablesFormacion(codOrganizacion, request);

        } catch (Exception ex) {
            log.error("Error al intentar preparar la JSP de una nueva formación: ", ex);
        }

        return urlNuevaFormacion;
    }

    public String cargarModificarFormacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarFormacion - " + numExpediente);

        String nuevo = "0";
        String urlNuevaFormacion = "/jsp/extension/melanbide15/nuevaFormacion.jsp?codOrganizacion=" + codOrganizacion;

        try {
            // Configurar el atributo "nuevo"
            if (request.getAttribute("nuevo") != null) {
                if (!"0".equals(request.getAttribute("nuevo").toString())) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }

            // Obtener y validar el número de expediente
            String numExp = request.getParameter("numExp");
            if (numExp != null && !numExp.trim().isEmpty()) {
                request.setAttribute("numExp", numExp.trim());
            } else {
                log.warn("El número de expediente no se recibió correctamente.");
            }

            // Obtener y validar el ID
            String id = request.getParameter("id");
            if (id != null && !id.trim().isEmpty()) {
                FormacionVO datModif = m15Manager.getFormacionPorID(id.trim(), m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                } else {
                    log.warn("No se encontró información de FormacionVO para el ID proporcionado: " + id);
                }
            } else {
                log.warn("El parámetro 'id' no se recibió correctamente.");
            }

            // Cargar los desplegables necesarios
            cargarDesplegablesFormacion(codOrganizacion, request);

        } catch (Exception ex) {
            log.error("Error al preparar los datos para modificar y llamar la JSP de modificación: ", ex);
        }

        return urlNuevaFormacion;
    }

    public void crearNuevaFormacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevaFormacion - " + numExpediente);

        String codigoOperacion = "-1";
        List<FormacionVO> lista = new ArrayList<FormacionVO>();
        FormacionVO nuevaFormacion = new FormacionVO();

        try {
            // Obtener adaptador de base de datos
            AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            // Recuperar parámetros del request
            String numExp = request.getParameter("numExp");
            String dniFor = request.getParameter("dniFor");
            String numIdenFor = request.getParameter("numIdenFor");
            String horasFor = request.getParameter("horasFor");
            String porcentajeFor = request.getParameter("porcentajeFor");
            String subvencionFor = request.getParameter("subvencionFor");

            // Validar y preparar horasFor
            if (horasFor == null || horasFor.trim().isEmpty()) {
                log.debug("El parámetro horasFor es nulo o vacío. Se asigna 0.0.");
                horasFor = "0.0";
            } else {
                horasFor = horasFor.replace(",", ".");
            }

            // Validar y preparar porcentajeFor
            if (porcentajeFor == null || porcentajeFor.trim().isEmpty()) {
                log.debug("El parámetro porcentajeFor es nulo o vacío. Se asigna 0.0.");
                porcentajeFor = "0.0";
            } else {
                porcentajeFor = porcentajeFor.replace(",", ".");
            }

            // Validar y preparar subvencionFor
            if (subvencionFor == null || subvencionFor.trim().isEmpty()) {
                log.debug("El parámetro subvencionFor es nulo o vacío. Se asigna 0.0.");
                subvencionFor = "0.0";
            } else {
                subvencionFor = subvencionFor.replace(",", ".");
            }

            // Asignar valores al objeto nuevaFormacion
            nuevaFormacion.setNumExp(numExp);
            nuevaFormacion.setDniFor(dniFor);
            nuevaFormacion.setNumIdenFor(numIdenFor);

            try {
                nuevaFormacion.setHorasFor(Double.valueOf(horasFor));
                nuevaFormacion.setPorcentajeFor(Double.valueOf(porcentajeFor));
                nuevaFormacion.setSubvencionFor(Double.valueOf(subvencionFor));
            } catch (NumberFormatException e) {
                log.error("Error al convertir parámetros a Double: " + e.getMessage(), e);
                throw new IllegalArgumentException("Formato incorrecto en parámetros numéricos.", e);
            }

            // Crear nueva formación en la base de datos
            boolean insertOK = m15Manager.crearNuevaFormacion(nuevaFormacion, adapt);
            if (insertOK) {
                log.debug("Formación insertada correctamente.");
                codigoOperacion = "0";

                // Recuperar lista actualizada de formaciones
                lista = m15Manager.getListaFormacion(numExp, codOrganizacion, adapt);
                if (!lista.isEmpty()) {
                    for (FormacionVO form : lista) {
                        form.setDescDniFor(m15Utils.getDescripcionDesplegable(request, form.getDescDniFor()));
                    }
                }
            } else {
                log.debug("No se insertó correctamente la nueva formación.");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error al procesar la nueva formación: ", ex);
            codigoOperacion = "3";
        }

        // Preparar y retornar el resultado como JSON
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if ("0".equalsIgnoreCase(codigoOperacion)) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarFormacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarFormacion - " + numExpediente);

        String codigoOperacion = "-1";
        List<FormacionVO> lista = new ArrayList<FormacionVO>();

        try {
            // Obtener el adaptador de base de datos
            AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            // Recuperar parámetros del request
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String dniFor = request.getParameter("dniFor");
            String numIdenFor = request.getParameter("numIdenFor");
            String horasFor = request.getParameter("horasFor");
            String porcentajeFor = request.getParameter("porcentajeFor");
            String subvencionFor = request.getParameter("subvencionFor");

            // Validar ID
            if (id == null || id.trim().isEmpty()) {
                log.debug("No se ha recibido el ID de la formación a modificar");
                codigoOperacion = "3"; // Código de error para falta de ID
            } else {
                // Crear y asignar valores al objeto FormacionVO
                FormacionVO datModif = new FormacionVO();
                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setDniFor(dniFor);
                datModif.setNumIdenFor(numIdenFor);

                try {
                    datModif.setHorasFor(Double.valueOf(horasFor != null ? horasFor.replace(",", ".") : "0.0"));
                    datModif.setPorcentajeFor(Double.valueOf(porcentajeFor != null ? porcentajeFor.replace(",", ".") : "0.0"));
                    datModif.setSubvencionFor(Double.valueOf(subvencionFor != null ? subvencionFor.replace(",", ".") : "0.0"));
                } catch (NumberFormatException e) {
                    log.error("Error al convertir parámetros numéricos: " + e.getMessage(), e);
                    codigoOperacion = "3"; // Código para error en el formato de parámetros numéricos
                    throw e;
                }

                boolean modOK = false;
                try {
                    // Modificar formación en la base de datos
                    modOK = m15Manager.modificarFormacion(datModif, adapt);
                } catch (Exception ex) {
                    log.error("Se ha producido un error modificando una Formacion: ", ex);
                    codigoOperacion = "8"; // Código para error genérico de modificación
                }

                if (modOK) {
                    log.debug("Formación modificada correctamente");
                    codigoOperacion = "0"; // Código para éxito
                    try {
                        // Recuperar lista actualizada de formaciones
                        lista = m15Manager.getListaFormacion(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FormacionVO form : lista) {
                                form.setDescDniFor(m15Utils.getDescripcionDesplegable(request, form.getDescDniFor()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1"; // Código para error de base de datos
                        log.error("Error de tipo BD al recuperar la lista de formaciones después de modificar una formación: " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "5"; // Código para error genérico al recuperar lista
                        log.error("Error al recuperar la lista de formaciones después de modificar una formación: " + ex.getMessage());
                    }
                } else {
                    log.error("No se ha modificado correctamente la formación");
                    codigoOperacion = "1"; // Código para fallo en la modificación
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parámetros recibidos del JSP al objeto: " + ex.getMessage(), ex);
            codigoOperacion = "3"; // Código para error en formato numérico
        } catch (SQLException ex) {
            log.error("Se ha producido un error modificando una Formacion: ", ex);
            codigoOperacion = "1"; // Código para error de base de datos
        }

        // Preparar y retornar el resultado como JSON
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if ("0".equalsIgnoreCase(codigoOperacion)) {
            resultado.setAtributo("lista", lista);
        }
        m15Utils.retornarJSON(new Gson().toJson(resultado), response);
    }

    public void eliminarFormacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarFormacion - " + numExpediente);

        String codigoOperacion = "-1"; // Código de operación inicial
        List<FormacionVO> lista = new ArrayList<FormacionVO>();

        try {
            // Obtener parámetros del request
            String idParam = request.getParameter("id");
            String numExp = request.getParameter("numExp");

            if (idParam == null || idParam.trim().isEmpty() || numExp == null || numExp.trim().isEmpty()) {
                log.debug("No se han recibido los parámetros requeridos (id o numExp) desde la JSP.");
                codigoOperacion = "3"; // Código para error en la entrada
            } else {
                // Convertir ID a entero
                int id;
                try {
                    id = Integer.parseInt(idParam);
                } catch (NumberFormatException e) {
                    log.error("El parámetro 'id' no es un número válido: " + idParam, e);
                    codigoOperacion = "3"; // Código para error en el formato del ID
                    throw e;
                }

                // Obtener el adaptador de base de datos
                AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

                // Ejecutar la eliminación
                int result = m15Manager.eliminarFormacion(id, numExp, adapt);
                if (result <= 0) {
                    log.debug("No se pudo eliminar la formación con ID: " + id);
                    codigoOperacion = "6"; // Código para fallo en la eliminación
                } else {
                    codigoOperacion = "0"; // Código para éxito en la eliminación
                    log.debug("Formación con ID: " + id + " eliminada correctamente.");

                    try {
                        // Recuperar lista actualizada de formaciones
                        lista = m15Manager.getListaFormacion(numExp, codOrganizacion, adapt);
                        for (FormacionVO form : lista) {
                            // Actualizar descripciones si es necesario
                            form.setDescDniFor(m15Utils.getDescripcionDesplegable(request, form.getDescDniFor()));
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5"; // Código para error al recuperar la lista
                        log.error("Error al recuperar la lista de formación después de la eliminación: ", ex);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error en el formato de parámetros (ID no válido): " + request.getParameter("id"), ex);
            codigoOperacion = "3"; // Código para error de formato numérico
        } catch (Exception ex) {
            log.error("Error eliminando una formación: ", ex);
            codigoOperacion = "2"; // Código para error general
        }

        // Crear el objeto de respuesta
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);

        if ("0".equalsIgnoreCase(codigoOperacion)) {
            resultado.setAtributo("lista", lista);
        }

        // Enviar la respuesta como JSON
        retornarJSON(new Gson().toJson(resultado), response);
    }

    private void cargarDesplegablesFormacion(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaDniNie = m15Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_DNINIE, ConstantesMeLanbide15.FICHERO_PROPIEDADES), m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaDniNie.isEmpty()) {
            listaDniNie = m15Utils.traducirDesplegable(request, listaDniNie);
            request.setAttribute("listaDniNie", listaDniNie);
        }

    }

    public String cargarNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaContratacion - " + numExpediente);

        String nuevo = "1";
        String numExp = "";
        String urlNuevaContratacion = "/jsp/extension/melanbide15/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;

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

            cargarDesplegablesContratacion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al preparar la JSP para una nueva contratación: " + ex.getMessage());
        }

        return urlNuevaContratacion;
    }

    public String cargarModificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarContratacion - " + numExpediente);

        String nuevo = "0";
        String urlNuevaContratacion = "/jsp/extension/melanbide15/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;

        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                ContratacionVO datModif = m15Manager.getContratacionPorID(id, m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            cargarDesplegablesContratacion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlNuevaContratacion;

    }

    public void crearNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        log.info("Entramos en crearNuevaContratacion - " + numExpediente);

        String codigoOperacion = "-1";
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        ContratacionVO nuevaContratacion = new ContratacionVO();

        try {
            AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");
            String dniCon = request.getParameter("dniCon");
            String numIdenCon = request.getParameter("numIdenCon");
            String fecIniCon = request.getParameter("fecIniCon");
            String fecFinCon = request.getParameter("fecFinCon");
            String subvencionCon = request.getParameter("subvencionCon");

            // Validar y preparar subvencionFor
            if (subvencionCon == null || subvencionCon.trim().isEmpty()) {
                log.debug("El parámetro subvencionCon es nulo o vacío. Se asigna 0.0.");
                subvencionCon = "0.0";
            } else {
                subvencionCon = subvencionCon.replace(",", ".");
            }

            nuevaContratacion.setNumExp(numExp);
            nuevaContratacion.setDniCon(dniCon);
            nuevaContratacion.setNumIdenCon(numIdenCon);

            if (fecIniCon != null && !"".equals(fecIniCon)) {
                nuevaContratacion.setFecIniCon(new java.sql.Date(dateFormat.parse(fecIniCon).getTime()));
            }

            if (fecFinCon != null && !"".equals(fecFinCon)) {
                nuevaContratacion.setFecFinCon(new java.sql.Date(dateFormat.parse(fecFinCon).getTime()));
            }

            try {
                nuevaContratacion.setSubvencionCon(Double.valueOf(subvencionCon));
            } catch (NumberFormatException e) {
                log.error("Error al convertir parámetros a Double: " + e.getMessage(), e);
                throw new IllegalArgumentException("Formato incorrecto en parámetros numéricos.", e);
            }
            boolean insertOK = m15Manager.crearNuevaContratacion(nuevaContratacion, adapt);
            if (insertOK) {
                log.debug("Contratación insertada correctamente");
                codigoOperacion = "0";
                lista = m15Manager.getListaContratacion(request.getParameter("numExp"), codOrganizacion, adapt);
            } else {
                log.debug("No se insertó correctamente la nueva contratación");
                codigoOperacion = "1";
            }

        } catch (Exception ex) {
            log.debug("Error al procesar la nueva contratación: ", ex);
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if ("0".equals(codigoOperacion)) {
            resultado.setAtributo("lista", lista);
        }

        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarContratacion - " + numExpediente);

        String codigoOperacion = "-1";
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();

        try {
            AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String dniCon = request.getParameter("dniCon");
            String numIdenCon = request.getParameter("numIdenCon");
            String fecIniCon = (String) request.getParameter("fecIniCon");
            String fecFinCon = (String) request.getParameter("fecFinCon");
            String subvencionCon = request.getParameter("subvencionCon").replace(",", ".");

            // Validar ID
            if (id == null || id.isEmpty()) {
                log.debug("No se ha recibido el ID de la contratación a modificar.");
                codigoOperacion = "3";
            } else {
                ContratacionVO datModif = new ContratacionVO();
                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setDniCon(dniCon);
                // Asignar DNI/NIE solo si no está vacío
                if (dniCon != null && !dniCon.isEmpty()) {
                    datModif.setDniCon(dniCon);
                }
                datModif.setNumIdenCon(numIdenCon);

                if (fecIniCon != null && !"".equals(fecIniCon)) {
                    datModif.setFecIniCon(new java.sql.Date(dateFormat.parse(fecIniCon).getTime()));
                }
                if (fecFinCon != null && !"".equals(fecFinCon)) {
                    datModif.setFecFinCon(new java.sql.Date(dateFormat.parse(fecFinCon).getTime()));
                }
                if (subvencionCon != null && !"".equals(subvencionCon)) {
                    datModif.setSubvencionCon(Double.valueOf(subvencionCon));
                }

                boolean modOK = m15Manager.modificarContratacion(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    lista = m15Manager.getListaContratacion(datModif.getNumExp(), codOrganizacion, adapt);
                    if (lista.size() > 0) {
                        for (ContratacionVO form : lista) {
                            form.setDescDniCon(m15Utils.getDescripcionDesplegable(request, form.getDescDniCon()));
                        }
                    }

                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            log.debug("Error al modificar contratación: ", ex);
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if ("0".equals(codigoOperacion)) {
            resultado.setAtributo("lista", lista);
        }

        retornarJSON(new Gson().toJson(resultado), response);
    }

    private void cargarDesplegablesContratacion(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaDniNie = m15Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_DNINIE, ConstantesMeLanbide15.FICHERO_PROPIEDADES), m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaDniNie.isEmpty()) {
            listaDniNie = m15Utils.traducirDesplegable(request, listaDniNie);
            request.setAttribute("listaDniNie", listaDniNie);
        }

    }

    public void eliminarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarContratacion - " + numExpediente);

        String codigoOperacion = "-1"; // Código de operación por defecto
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();

        try {
            // Obtener el ID de la contratación desde la solicitud
            String idParam = request.getParameter("id");
            String numExp = request.getParameter("numExp");

            if (idParam == null || idParam.isEmpty() || numExp == null || numExp.isEmpty()) {
                log.debug("No se ha recibido desde la JSP el id o el número de expediente de la contratación a eliminar.");
                codigoOperacion = "3"; // Código para error de entrada
            } else {
                // Convertir el parámetro ID a entero
                int id = Integer.parseInt(idParam);

                AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

                // Ejecutar la eliminación
                int result = m15Manager.eliminarContratacion(id, numExp, adapt);
                if (result <= 0) {
                    codigoOperacion = "6"; // Código para fallo al eliminar
                } else {
                    codigoOperacion = "0"; // Código para operación exitosa

                    try {
                        // Recuperar la lista actualizada de contrataciones
                        lista = m15Manager.getListaContratacion(numExp, codOrganizacion, adapt);

                        for (ContratacionVO contratacion : lista) {
                            // Actualizar las descripciones de los desplegables si es necesario
                            contratacion.setDescDniCon(m15Utils.getDescripcionDesplegable(request, contratacion.getDescDniCon()));
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5"; // Código para error al recuperar lista
                        log.error("Error al recuperar la lista de contratación después de eliminar una contratación.", ex);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            codigoOperacion = "3"; // Código para error en la entrada (ID no válido)
            log.error("El parámetro 'id' no es un número válido: " + request.getParameter("id"), ex);
        } catch (Exception ex) {
            codigoOperacion = "2"; // Código para error general
            log.error("Error eliminando una contratación.", ex);
        }

        // Crear el objeto de respuesta
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);

        if ("0".equalsIgnoreCase(codigoOperacion)) {
            resultado.setAtributo("lista", lista);
        }

        // Enviar la respuesta como JSON
        retornarJSON(new Gson().toJson(resultado), response);
    }

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

    public String cargarNuevaOrientacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaOrientacion - " + numExpediente);

        String nuevo = "1";
        String numExp = "";
        String urlNuevaOrientacion = "/jsp/extension/melanbide15/nuevaOrientacion.jsp?codOrganizacion=" + codOrganizacion;

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

            cargarDesplegablesOrientacion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al preparar la JSP para una nueva orientación: " + ex.getMessage());
        }

        return urlNuevaOrientacion;
    }

    public String cargarModificarOrientacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarOrientacion - " + numExpediente);

        String nuevo = "0";
        String urlNuevaOrientacion = "/jsp/extension/melanbide15/nuevaOrientacion.jsp?codOrganizacion=" + codOrganizacion;

        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }

            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                OrientacionVO datModif = m15Manager.getOrientacionPorID(id, m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            cargarDesplegablesOrientacion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al preparar los datos para modificar una orientación: " + ex.getMessage());
        }

        return urlNuevaOrientacion;
    }

    public void crearNuevaOrientacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevaOrientacion - " + numExpediente);

        String codigoOperacion = "-1";
        List<OrientacionVO> lista = new ArrayList<OrientacionVO>();
        OrientacionVO nuevaOrientacion = new OrientacionVO();

        try {
            // Inicialización de Adaptador SQL
            AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            // Recuperar parámetros desde el Request
            String numExp = request.getParameter("numExp");
            String dniOri = request.getParameter("dniOri");
            String numIdenOri = request.getParameter("numIdenOri");

            String horasOri = request.getParameter("horasOri");
            String subvencionOri = request.getParameter("subvencionOri");

            // Validar y preparar parámetros horasOri y subvencionOri
            if (horasOri == null || horasOri.trim().isEmpty()) {
                log.debug("El parámetro horasOri es nulo o vacío. Se asigna 0.0.");
                horasOri = "0.0";
            } else {
                horasOri = horasOri.replace(",", ".");
            }

            if (subvencionOri == null || subvencionOri.trim().isEmpty()) {
                log.debug("El parámetro subvencionOri es nulo o vacío. Se asigna 0.0.");
                subvencionOri = "0.0";
            } else {
                subvencionOri = subvencionOri.replace(",", ".");
            }

            // Asignar valores al objeto OrientacionVO
            nuevaOrientacion.setNumExp(numExp);
            nuevaOrientacion.setDniOri(dniOri);
            nuevaOrientacion.setNumIdenOri(numIdenOri);

            try {
                nuevaOrientacion.setHorasOri(Double.valueOf(horasOri));
                nuevaOrientacion.setSubvencionOri(Double.valueOf(subvencionOri));
            } catch (NumberFormatException e) {
                log.error("Error al convertir horasOri o subvencionOri a Double: " + e.getMessage());
                throw new IllegalArgumentException("Formato incorrecto en horasOri o subvencionOri", e);
            }

            // Invocar el Manager para insertar la orientación
            MeLanbide15Manager meLanbide15Manager = m15Manager.getInstance();
            boolean insertOK = meLanbide15Manager.crearNuevaOrientacion(nuevaOrientacion, adapt);

            if (insertOK) {
                log.debug("Orientación insertada correctamente");
                codigoOperacion = "0";
                lista = meLanbide15Manager.getListaOrientacion(numExp, codOrganizacion, adapt);

                if (lista.size() > 0) {
                    for (OrientacionVO ori : lista) {
                        ori.setDescDniOri(m15Utils.getDescripcionDesplegable(request, ori.getDescDniOri()));
                    }
                }
            } else {
                log.debug("No se insertó correctamente la nueva orientación");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error al procesar la creación de orientación: " + ex.getMessage(), ex);
            codigoOperacion = "3";
        }

        // Preparar respuesta JSON
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);

        if ("0".equals(codigoOperacion)) {
            resultado.setAtributo("lista", lista);
        }

        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarOrientacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificar orientación - " + numExpediente);

        String codigoOperacion = "-1";
        List<OrientacionVO> lista = new ArrayList<OrientacionVO>();

        try {
            AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String id = request.getParameter("id");

            String numExp = request.getParameter("numExp");

            String dniOri = request.getParameter("dniOri");
            String numIdenOri = request.getParameter("numIdenOri");
            String horasOri = request.getParameter("horasOri").replace(",", ".");
            String subvencionOri = request.getParameter("subvencionOri").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido el ID de la orientación a modificar.");
                codigoOperacion = "3";
            } else {

                OrientacionVO datModif = new OrientacionVO();

                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setDniOri(dniOri);
                datModif.setNumIdenOri(numIdenOri);
                datModif.setHorasOri(Double.valueOf(horasOri));
                datModif.setSubvencionOri(Double.valueOf(subvencionOri));

                MeLanbide15Manager meLanbide15Manager = m15Manager.getInstance();
                boolean modOK = m15Manager.modificarOrientacion(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = m15Manager.getListaOrientacion(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (OrientacionVO ori : lista) {
                                ori.setDescDniOri(m15Utils.getDescripcionDesplegable(request, ori.getDescDniOri()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de orientación después de Modificar una orientación : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de orientación después de Modificar una orientación : " + ex.getMessage());
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

    public void eliminarOrientacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminar orientación - " + numExpediente);

        String codigoOperacion = "-1";
        List<OrientacionVO> lista = new ArrayList<OrientacionVO>();

        try {
            String idParam = request.getParameter("id");
            String numExp = request.getParameter("numExp");

            if (idParam == null || idParam.isEmpty() || numExp == null || numExp.isEmpty()) {
                log.debug("No se ha recibido el id o el número de expediente de la orientación a eliminar.");
                codigoOperacion = "3";
            } else {
                int id = Integer.parseInt(idParam);
                AdaptadorSQLBD adapt = m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));

                int result = m15Manager.eliminarOrientacion(id, numExp, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        // Recuperar la lista actualizada de identidades
                        lista = m15Manager.getListaOrientacion(numExp, codOrganizacion, adapt);

                        for (OrientacionVO ori : lista) {
                            // Actualizar las descripciones de los desplegables
                            ori.setDescDniOri(m15Utils.getDescripcionDesplegable(request, ori.getDescDniOri()));
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5"; // Código para error al recuperar lista
                        log.error("Error al recuperar la lista de orientación después de eliminar una orientación.", ex);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            codigoOperacion = "3"; // Código para error en la entrada (ID no válido)
            log.error("El parámetro 'id' no es un número válido: " + request.getParameter("id"), ex);
        } catch (Exception ex) {
            codigoOperacion = "2"; // Código para error general
            log.error("Error eliminando una orientación.", ex);
        }

        // Crear el objeto de respuesta
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);

        if ("0".equalsIgnoreCase(codigoOperacion)) {
            resultado.setAtributo("lista", lista);
        }

        // Enviar la respuesta como JSON
        retornarJSON(new Gson().toJson(resultado), response);
    }

    private void cargarDesplegablesOrientacion(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaDniNie = m15Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide15.COD_DES_DNINIE, ConstantesMeLanbide15.FICHERO_PROPIEDADES), m15Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaDniNie.isEmpty()) {
            listaDniNie = m15Utils.traducirDesplegable(request, listaDniNie);
            request.setAttribute("listaDniNie", listaDniNie);
        }
    }

}
