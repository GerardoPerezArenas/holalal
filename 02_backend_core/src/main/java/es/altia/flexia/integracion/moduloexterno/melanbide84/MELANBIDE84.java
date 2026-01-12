package es.altia.flexia.integracion.moduloexterno.melanbide84;

import com.google.gson.Gson;
import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.melanbide84.i18n.MeLanbide84I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide84.manager.MeLanbide84Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide84.util.ConstantesMeLanbide84;
import es.altia.flexia.integracion.moduloexterno.melanbide84.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide84.vo.PersonaVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionBindingStub;
import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionServiceLocator;
import es.altia.webservice.client.tramitacion.ws.wto.DireccionVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InfoConexionVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.RespuestasTramitacionVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.zip.DataFormatException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.rpc.ServiceException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;

public class MELANBIDE84 extends ModuloIntegracionExterno {

    private static final Logger log = LogManager.getLogger(MELANBIDE84.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    @Override
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
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide84/melanbide84.jsp";
        boolean enTramite = false;
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<PersonaVO> listaEntap = MeLanbide84Manager.getInstance().getListaPersonas(numExpediente, codOrganizacion, adapt);
                if (!listaEntap.isEmpty()) {
                    for (PersonaVO pers : listaEntap) {
                        pers.setDescNivel(getDescripcionDesplegable(request, pers.getDescNivel()));
                        pers.setDescSexo(getDescripcionDesplegable(request, pers.getDescSexo()));
                    }
                    request.setAttribute("listaEntap", listaEntap);
                }
                //Buscar si el expediente tiene el tramite 110 abierto
                enTramite = MeLanbide84Manager.getInstance().estaEnTramite110(codOrganizacion, numExpediente, adapt);

                request.setAttribute("enTramite", enTramite);
            } catch (Exception ex) {
                log.error("Error al recuperar los datos De Personas - MELANBIDE84 - cargarPantallaPrincipal", ex);
            }
        }
        return url;
    }

    public String cargarNuevaPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaPersona - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevaPersona = "/jsp/extension/melanbide84/nuevaPersona.jsp?codOrganizacion=" + codOrganizacion;
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
            cargarDesplegablesENTAP(codOrganizacion, request);

        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva persona : " + ex.getMessage());
        }
        return urlnuevaPersona;
    }

    public String cargarModificarPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarPersona - " + numExpediente);
        String nuevo = "0";
        String urlnuevaPersona = "/jsp/extension/melanbide84/nuevaPersona.jsp?codOrganizacion=" + codOrganizacion;
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
            // Recuperramos datos de Persona a modificar y cargamos en el request
            if (id != null && !id.isEmpty()) {
                PersonaVO datModif = MeLanbide84Manager.getInstance().getPersonaPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            //Cargamos los valores  de los desplegables
            cargarDesplegablesENTAP(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return urlnuevaPersona;
    }

    public void crearNuevaPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarPersona - " + numExpediente);
        String codigoOperacion = "-1";
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        PersonaVO nuevaPersona = new PersonaVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("expediente");
            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            String ape1 = request.getParameter("ape1");
            String ape2 = request.getParameter("ape2");
            String fecNac = request.getParameter("fecNac");
            String sexo = request.getParameter("sexo");
            String nivel = request.getParameter("nivel");
            String calle = request.getParameter("calle");
            String numero = request.getParameter("numero");
            String letra = request.getParameter("letra");
            String planta = request.getParameter("planta");
            String puerta = request.getParameter("puerta");
            String codPost = request.getParameter("codPost");
            String localidad = request.getParameter("localidad");
            String programa = request.getParameter("programa");
            String fecInicio = request.getParameter("fecInicio");
            String fecSolicitud = request.getParameter("fecSolicitud");

            nuevaPersona.setExpEntap(numExp);
            nuevaPersona.setDni(dni);
            nuevaPersona.setNombre(nombre);
            nuevaPersona.setApel1(ape1);
            if (ape2 != null && !"".equalsIgnoreCase(ape2)) {
                nuevaPersona.setApel2(ape2);
            }
            nuevaPersona.setSexo(Integer.valueOf(sexo));
            // DIRECCION
            // Oblig: calle - CP - localidad / NO obl: numero - letra - planta - puerta 
            nuevaPersona.setCalle(calle);
            if (numero != null && !"".equalsIgnoreCase(numero)) {
                nuevaPersona.setNumero(Integer.valueOf(numero));
            }
            nuevaPersona.setLetra(letra != null && !"".equals(letra) ? letra : "");
            if (planta != null && !"".equalsIgnoreCase(planta)) {
                nuevaPersona.setPlanta(planta);
            }
            if (puerta != null && !"".equalsIgnoreCase(puerta)) {
                nuevaPersona.setPuerta(puerta);
            }
            nuevaPersona.setCodPost(Integer.valueOf(codPost));
            nuevaPersona.setLocalidad(localidad);
            nuevaPersona.setPrograma(programa);
            nuevaPersona.setNivelAcademico(nivel);
            if (fecNac != null && !"".equals(fecNac)) {
                nuevaPersona.setFechaNacimiento(new java.sql.Date(dateFormat.parse(fecNac).getTime()));
            }
            if (fecInicio != null && !"".equals(fecInicio)) {
                nuevaPersona.setFechaInicio(new java.sql.Date(dateFormat.parse(fecInicio).getTime()));
            }
            if (fecSolicitud != null && !"".equals(fecSolicitud)) {
                nuevaPersona.setFechaSolicitud(new java.sql.Date(dateFormat.parse(fecSolicitud).getTime()));
            }
            MeLanbide84Manager meLanbide84Manager = MeLanbide84Manager.getInstance();
            boolean insertOK = meLanbide84Manager.crearNuevaPersona(nuevaPersona, adapt);
            if (insertOK) {
                log.debug("Persona Insertada Correctamente");
                codigoOperacion = "0";
                lista = meLanbide84Manager.getListaPersonas(numExp, codOrganizacion, adapt);
                if (!lista.isEmpty()) {
                    for (PersonaVO pers : lista) {
                        pers.setDescSexo(getDescripcionDesplegable(request, pers.getDescSexo()));
                        pers.setDescNivel(getDescripcionDesplegable(request, pers.getDescNivel()));
                    }
                }
            } else {
                log.error("NO se ha insertado correctamente la nueva persona");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto Persona2VO" + ex.getMessage());
            codigoOperacion = "2";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarPersona - " + numExpediente);
        String codigoOperacion = "-1";
        List<PersonaVO> lista = new ArrayList<PersonaVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String numExp = request.getParameter("expediente");
            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            String ape1 = request.getParameter("ape1");
            String ape2 = request.getParameter("ape2");
            String fecNac = request.getParameter("fecNac");
            String sexo = request.getParameter("sexo");
            String nivel = request.getParameter("nivel");
            String calle = request.getParameter("calle");
            String numero = request.getParameter("numero");
            String letra = request.getParameter("letra");
            String planta = request.getParameter("planta");
            String puerta = request.getParameter("puerta");
            String codPost = request.getParameter("codPost");
            String localidad = request.getParameter("localidad");
            String programa = request.getParameter("programa");
            String fecInicio = request.getParameter("fecInicio");
            String fecSolicitud = request.getParameter("fecSolicitud");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la Persona a Modificar ");
                codigoOperacion = "3";
            } else {
                PersonaVO datModif = new PersonaVO();
                datModif.setId(Integer.valueOf(id));
                datModif.setExpEntap(numExp);
                datModif.setDni(dni);
                datModif.setNombre(nombre);
                datModif.setPrograma(programa);
                datModif.setApel1(ape1);
                if (ape2 != null && !"".equalsIgnoreCase(ape2)) {
                    datModif.setApel2(ape2);
                }
                datModif.setSexo(Integer.valueOf(sexo));
                // DIRECCION
                // Oblig: calle - CP - localidad / NO obl: numero - planta - puerta 
                datModif.setCalle(calle);
                if (numero != null && !"".equalsIgnoreCase(numero)) {
                    datModif.setNumero(Integer.valueOf(numero));
                }
                datModif.setLetra(letra != null && !"".equals(letra) ? letra : "");
                if (planta != null && !"".equals(planta)) {
                    datModif.setPlanta(planta);
                }
                if (puerta != null && !"".equalsIgnoreCase(puerta)) {
                    datModif.setPuerta(puerta);
                }
                datModif.setCodPost(Integer.valueOf(codPost));
                datModif.setLocalidad(localidad);
                datModif.setNivelAcademico(nivel);
                if (fecInicio != null && !"".equals(fecInicio)) {
                    datModif.setFechaInicio(new java.sql.Date(dateFormat.parse(fecInicio).getTime()));
                }
                if (fecNac != null && !"".equals(fecNac)) {
                    datModif.setFechaNacimiento(new java.sql.Date(dateFormat.parse(fecNac).getTime()));
                }
                if (fecSolicitud != null && !"".equals(fecSolicitud)) {
                    datModif.setFechaSolicitud(new java.sql.Date(dateFormat.parse(fecSolicitud).getTime()));
                }

                MeLanbide84Manager meLanbide84Manager = MeLanbide84Manager.getInstance();
                boolean modOK = meLanbide84Manager.modificarPersona(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = meLanbide84Manager.getListaPersonas(numExp, codOrganizacion, adapt);
                        if (!lista.isEmpty()) {
                            for (PersonaVO pers : lista) {
                                pers.setDescNivel(getDescripcionDesplegable(request, pers.getDescNivel()));
                                pers.setDescSexo(getDescripcionDesplegable(request, pers.getDescSexo()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de Personas después de Modificar una persona : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de Personas después de Modificar una persona : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }

        } catch (Exception ex) {
            log.error("Error modificar --- ", ex);
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void eliminarPersona(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarPersona - " + numExpediente);
        String codigoOperacion = "-1";
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la Persona a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide84Manager.getInstance().eliminarPersona(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide84Manager.getInstance().getListaPersonas(numExp, codOrganizacion, adapt);
                        if (!lista.isEmpty()) {
                            for (PersonaVO pers : lista) {
                                pers.setDescNivel(getDescripcionDesplegable(request, pers.getDescNivel()));
                                pers.setDescSexo(getDescripcionDesplegable(request, pers.getDescSexo()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de personas después de eliminar una Persona");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error eliminando una Persona: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
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
            if (!"".equals(xml)) {
                cargarExpedienteExtension(codOrganizacion, numExpediente, xml);
            }

            request.getSession().setAttribute("mensajeImportar", MeLanbide84I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
        } catch (DataFormatException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE84.class.getName()).log(Level.SEVERE, null, ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide84I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE84.class.getName()).log(Level.SEVERE, null, ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide84I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE84.class.getName()).log(Level.SEVERE, null, ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide84I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE84.class.getName()).log(Level.SEVERE, null, ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide84I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide84I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarListaPersonas.jsp");
        return "/jsp/extension/melanbide84/recargarListaPersonas.jsp";
    }

    /**
     * Método que inicia un expediente APEA o APEI en REGEXLAN y MI CARPETA a
     * todas las personas recogidas en la tabla MELANBIDE84_PERSONAS, añade como
     * representante al interesado del expediente ENTAP, relaciona cada nuevo
     * expediente con el ENTAP. Devuelve el codigo de la operacion y la lista de
     * expedientes iniciados en formato JSON
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void iniciarExpedientes(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("iniciarExpedientes APEA/APEI - " + numExpediente);
        int cTramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide84.CODIGO_TRAMITE110, ConstantesMeLanbide84.FICHERO_PROPIEDADES));
        String codigoOperacion = "-1";
        String numExpENTAP = null;
        String numExpAPE = null;
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        RespuestasTramitacionVO respuestaInicio;
        RespuestasTramitacionVO respuestaRelacion;
//        RespuestasTramitacionVO respuestaAltaTer;
        String[] expedientesIniciados = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        try {
            numExpENTAP = request.getParameter("numero");
            String[] numeroExpediente = numExpENTAP.split(ConstantesMeLanbide84.BARRA_SEPARADORA);
            MeLanbide84Manager meLanbide84Manager = MeLanbide84Manager.getInstance();
            ExpedienteVO expENTAP = new ExpedienteVO();
            expENTAP = meLanbide84Manager.getDatosExpediente(codOrganizacion, numExpENTAP, adapt);
            IdExpedienteVO idexpediente1 = meLanbide84Manager.getDatosIdExpediente(codOrganizacion, numExpENTAP, adapt);
            String ejeENTAP = numeroExpediente[0];
            String res = numeroExpediente[2];
            idexpediente1.setNumero(Integer.parseInt(res));
            expENTAP.setIdExpedienteVO(idexpediente1);
            int codEntidadENTAP = 0;
            int codRepreENTAP = 0;
            lista = meLanbide84Manager.getListaPersonas(numExpENTAP, codOrganizacion, adapt);
            if (lista.isEmpty()) {
                log.error("No hay registros de personas para el expediente " + numExpENTAP);
                codigoOperacion = "4";
            } else {
                int j = lista.size();
                // Se declara el objeto binding para las llamadas a los SW de WSTramitacion
                String wsUrl = ConfigurationParameter.getParameter(ConstantesMeLanbide84.URL_WS_TRAMITACION, ConstantesMeLanbide84.FICHERO_PROPIEDADES);
                URL url = new URL(wsUrl);
                WSTramitacionBindingStub binding = null;
                try {
                    binding = (WSTramitacionBindingStub) new WSTramitacionServiceLocator().getWSTramitacionPort(url);
                    binding.setTimeout(60000);
                } catch (ServiceException ex) {
                    log.error("eee", ex);
                }
                //Informacion de la conexion
                InfoConexionVO inf = new InfoConexionVO();
                inf.setOrganizacion(String.valueOf(codOrganizacion));
                inf.setAplicacion("RGI");
                log.debug("------------Info conexion----------");
                log.debug("Organizacion info: " + inf.getOrganizacion());
                log.debug("Aplicacion inf: " + inf.getAplicacion());
                log.debug("------------Info conexion----------");

                int codigoUOR = MeLanbide84Manager.getInstance().getcodigoUOR(codOrganizacion, numExpENTAP, numeroExpediente[1], cTramite, adapt);
                int usuarioExpediente = MeLanbide84Manager.getInstance().getUsuarioExpediente(codOrganizacion, numExpENTAP, numeroExpediente[1], cTramite, adapt);
                codEntidadENTAP = meLanbide84Manager.getCodTerceroExp(codOrganizacion, numExpENTAP, 1, adapt);
                codRepreENTAP = meLanbide84Manager.getCodTerceroExp(codOrganizacion, numExpENTAP, 2, adapt);
                expedientesIniciados = new String[j];
                int i = 0;
                // por cada persona del anexo se inicia un expediente
                for (PersonaVO persona : lista) {
                    log.info(">>>>>>>>>>>>>>>>>>    Interesado nº " + (i + 1) + " de " + j);

                    ExpedienteVO expAPE = new ExpedienteVO();
                    TerceroVO terAPE = new TerceroVO();
                    DireccionVO domAPE = null;
                    IdExpedienteVO idExpedienteAPE = new IdExpedienteVO();
                    InteresadoExpedienteVO interesadoAPE = new InteresadoExpedienteVO();

                    String ejeAPE = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                    idExpedienteAPE.setEjercicio(Calendar.getInstance().get(Calendar.YEAR));//año actual
                    idExpedienteAPE.setProcedimiento(persona.getPrograma());

                    expAPE.setIdExpedienteVO(idExpedienteAPE);
                    expAPE.setUor(codigoUOR);
                    expAPE.setUorTramiteInicio(codigoUOR);
                    expAPE.setUsuario(usuarioExpediente);
                    expAPE.setAsunto("Expediente " + persona.getPrograma() + " asociado al expediente " + numExpENTAP);
                    log.debug("Asunto : " + expAPE.getAsunto());

                    // cargo tercero 
                    terAPE.setAp1(persona.getApel1());
                    if (persona.getApel2() != null && !persona.getApel2().isEmpty()) {
                        terAPE.setAp2(persona.getApel2());
                    }
                    terAPE.setNombre(persona.getNombre());
                    terAPE.setDoc(persona.getDni());
                    if (persona.getDni().startsWith("X") || persona.getDni().startsWith("Y") || persona.getDni().startsWith("Z")) {
                        terAPE.setTipoDoc("3");
                    } else {
                        terAPE.setTipoDoc("1");
                    }

                    int codigoTerceroAPE = meLanbide84Manager.existeTercero(terAPE, adapt);
                    int codigoDomicilioAPE = 0;
                    boolean iniciadosExpedientes = false;
                    if (codigoTerceroAPE != 0) {
                        log.info("Existe en T_TER: " + codigoTerceroAPE);
                        // busco si existen expedientes con el ejercicio del ENTAP y con el año actual si no son el mismo
                        List<String> expedientesPersona = meLanbide84Manager.getExpedientesPersona(codOrganizacion, codigoTerceroAPE, ejeENTAP, persona.getPrograma(), adapt);
                        if (!ejeENTAP.equalsIgnoreCase(ejeAPE)) {
                            List<String> expedientesPersona2 = meLanbide84Manager.getExpedientesPersona(codOrganizacion, codigoTerceroAPE, ejeAPE, persona.getPrograma(), adapt);
                            expedientesPersona.addAll(expedientesPersona2);
                        }
                        if (!expedientesPersona.isEmpty()) {
                            log.info("La persona es interesada en " + expedientesPersona.size() + " expediente(s) " + persona.getPrograma());
                            int codRepreAPE;
                            for (String expediente : expedientesPersona) {
                                codRepreAPE = meLanbide84Manager.getCodTerceroExp(codOrganizacion, expediente, 2, adapt);
                                if (codRepreAPE == codEntidadENTAP) {
                                    log.info("La entidad es representante en el " + expediente);
                                    iniciadosExpedientes = true;
                                    codigoOperacion = "0";
                                    break;
                                } else if (codRepreAPE == codRepreENTAP) {
                                    log.info("El representante de la entidad es representante en el " + expediente);
                                    iniciadosExpedientes = true;
                                    codigoOperacion = "0";
                                    break;
                                }
                            }
                        }
                    } else {
                        log.info("NO existe en T_TER: ");
                    }
                    if (!iniciadosExpedientes) {
                        if (codigoTerceroAPE != 0) {
                            codigoDomicilioAPE = meLanbide84Manager.existeDomicilioTer(codigoTerceroAPE, adapt);
                        }

                        if (codigoDomicilioAPE == 0) {
                            log.info("NO tiene domicilio en T_TER: ");
                            // buscar en t_MUN de flbgen los codigos de pais, provincia y municipio
                            int[] codigosLocalidad = null;
                            codigosLocalidad = meLanbide84Manager.getCodigosMunicipio(persona.getLocalidad(), adapt);
                            if (codigosLocalidad != null) {
                                log.debug("Pais: " + codigosLocalidad[0] + " - Provincia: " + codigosLocalidad[1] + " - Municipio: " + codigosLocalidad[2]);
                                domAPE = new DireccionVO();
                                domAPE.setCodPais(codigosLocalidad[0]);
                                domAPE.setCodProvincia(codigosLocalidad[1]);
                                domAPE.setCodMunicipio(codigosLocalidad[2]);
                                domAPE.setCodPostal(String.valueOf(persona.getCodPost()));
                                domAPE.setEsPrincipal(true);
                                domAPE.setNombreVia(persona.getCalle());
                                int tipoVia = meLanbide84Manager.getTipoVia(domAPE.getCodPais(), domAPE.getCodProvincia(), domAPE.getCodMunicipio(), domAPE.getNombreVia(), adapt);
                                domAPE.setTipoVia(tipoVia);
                                if (persona.getNumero() != null && !persona.getNumero().toString().isEmpty()) {
                                    domAPE.setPrimerNumero(persona.getNumero());
                                }
                                domAPE.setPrimeraLetra(persona.getLetra() != null && !persona.getLetra().isEmpty() ? persona.getLetra() : "");
                                if (persona.getPlanta() != null && !persona.getPlanta().isEmpty()) {
                                    domAPE.setPlanta(persona.getPlanta());
                                }
                                if (persona.getPuerta() != null && !persona.getPuerta().isEmpty()) {
                                    domAPE.setPuerta(persona.getPuerta());
                                }
                            } else {
                                log.error("No se han recuperado los códigos de Pais, Provincia y Municipio");
                                domAPE = new DireccionVO();
                                domAPE.setCodPais(108);
                                domAPE.setCodProvincia(99);
                                domAPE.setCodMunicipio(999);
                                domAPE.setTipoVia(0);
                                domAPE.setNombreVia("SIN DOMICILIO");
                                domAPE.setEmplazamiento("DESCONOCIDO");
                            }
                        } else {
                            log.info("Tiene domicilio en T_TER: " + codigoDomicilioAPE);
                            domAPE = meLanbide84Manager.getDomicilioTercero(terAPE, codigoTerceroAPE, codigoDomicilioAPE, adapt);
                        }
                        if (domAPE != null) {
                            terAPE.setDomicilio(domAPE);
                        }
                        // cargo interesado 
                        interesadoAPE.setRol(1);
                        interesadoAPE.setTercero(terAPE);

                        // recojo el interesado del ENTAP como representante
                        List<InteresadoExpedienteVO> interesados = MeLanbide84Manager.getInstance().getDatosInteresado(codOrganizacion, Integer.valueOf(numeroExpediente[0]), numExpENTAP, adapt);
                        String docEntidad = "";
                        // agrego el tercero
                        interesados.add(interesadoAPE);

                        for (InteresadoExpedienteVO interesado : interesados) {
                            log.info("------------Datos Interesado----------");
                            log.info("Rol: " + interesado.getRol());
                            log.info("-----------------Datos de remitente/Datos del Tercero---------------");
                            log.info("Apellido 1: " + interesado.getTercero().getAp1());
                            log.info("Apellido2: " + interesado.getTercero().getAp2());
                            log.info("Documento: " + interesado.getTercero().getDoc());
                            log.info("Email: " + interesado.getTercero().getEmail());
                            log.info("Nombre: " + interesado.getTercero().getNombre());
                            log.info("Telefono: " + interesado.getTercero().getTelefono());
                            log.info("TipoDoc: " + interesado.getTercero().getTipoDoc());
                            log.info("----------------------fin Datos de remitente/Datos del Tercero-----------");
                            if (interesado.getRol() == 2) {
                                docEntidad = interesado.getTercero().getDoc();
                            }
                            log.info("------------Datos Domicilio----------");
                            if (interesado.getTercero().getDomicilio() != null) {
                                log.info("Bloque: " + interesado.getTercero().getDomicilio().getBloque());
                                log.info("Cod Municipio: " + interesado.getTercero().getDomicilio().getCodMunicipio());
                                log.info("Cod Pais: " + interesado.getTercero().getDomicilio().getCodPais());
                                log.info("C Postal: " + interesado.getTercero().getDomicilio().getCodPostal());
                                log.info("CodProvincia: " + interesado.getTercero().getDomicilio().getCodProvincia());
                                log.info("Emplazamiento: " + interesado.getTercero().getDomicilio().getEmplazamiento());
                                log.info("Es principal: " + interesado.getTercero().getDomicilio().isEsPrincipal());
                                log.info("Escalera: " + interesado.getTercero().getDomicilio().getEscalera());
                                log.info("Nombre: " + interesado.getTercero().getDomicilio().getNombreVia());
                                log.info("Numero: " + interesado.getTercero().getDomicilio().getPrimerNumero());
                                log.info("Letra: " + interesado.getTercero().getDomicilio().getPrimeraLetra());
                                log.info("Planta: " + interesado.getTercero().getDomicilio().getPlanta());
                                log.info("Portal: " + interesado.getTercero().getDomicilio().getPortal());
                                log.info("Puerta: " + interesado.getTercero().getDomicilio().getPuerta());
                                log.info("TipoVia: " + interesado.getTercero().getDomicilio().getTipoVia());
                            } else {
                                log.info("Este interesado no tiene domicilio registrado en T_TER");
                            }
                            log.info("------------FIN Datos Domicilio----------");

                        }
                        expAPE.setInteresados(interesados.toArray(new InteresadoExpedienteVO[]{}));

                        log.info("------------datos que enviamos al expediente ----------");

                        log.info("------------UOR----------");
                        log.info("Uor: " + expAPE.getUor());
                        log.info("uorTramiteInicio: " + expAPE.getUorTramiteInicio());
                        log.info("Usuario: " + expAPE.getUsuario());
                        log.info("Asunto: " + expAPE.getAsunto());
                        log.info("------------FIN DATOS UOR----------");
                        ///Expediente
                        log.info("------------Datos Expediente----------");
                        //IdExpedientesVO
                        log.info("------------ID EXPEDIENTE1----------");
                        log.info("Ejercicio " + idExpedienteAPE.getEjercicio());
                        log.info("PROCEDIMIENTO " + idExpedienteAPE.getProcedimiento());
                        log.info("------------FIN ID EXPEDIENTE1----------");

                        log.info("------------Llamamos a ws tramitacion iniciarExpediente() ----------");
                        try {
                            respuestaInicio = binding.iniciarExpediente(expAPE, inf);

                            if (respuestaInicio.getStatus() == 1) {
                                codigoOperacion = "7";
                                log.error("Ha ocurrido un error al iniciar expediente " + idExpedienteAPE.getProcedimiento());
                                ErrorBean errorB = new ErrorBean();
                                errorB.setIdError("TRAMITACION_ENTAP_004");
                                errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Ha ocurrido un error en WSTramitacion al iniciar expediente " + idExpedienteAPE.getProcedimiento());
                                errorB.setSituacion("iniciarExpediente");
                                grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Ha ocurrido un error en WSTramitacion al iniciar expediente " + idExpedienteAPE.getProcedimiento(), numExpENTAP);
                            } else {
                                log.info("------------Respuesta de iniciarExpediente() ----------");
                                log.info("Ejercicio: " + respuestaInicio.getIdExpediente().getEjercicio());
                                log.info("Número: " + respuestaInicio.getIdExpediente().getNumero());
                                log.info("Numero_Expediente: " + respuestaInicio.getIdExpediente().getNumeroExpediente());
                                log.info("Procedimiento: " + respuestaInicio.getIdExpediente().getProcedimiento());
                                //casca porque getExpediente() == null
                                log.info("------------Fin Respuesta de iniciarExpediente----------");
                                log.info("idTramite: " + respuestaInicio.getIdtramite());
                                log.info("Tramite: " + respuestaInicio.getTramite());
                                log.info("Status: " + respuestaInicio.getStatus());
                                log.info("Resultado: " + respuestaInicio.getError());
                                if (respuestaInicio.getIdExpediente().getNumeroExpediente() != null) {
                                    numExpAPE = respuestaInicio.getIdExpediente().getNumeroExpediente();
                                    // anyado el num Exp para la respuesta
                                    expedientesIniciados[i] = persona.getDni() + " - " + numExpAPE;
                                    i++;
                                    // recupero el codigo del tercero del expediente iniciado para grabar los suplementarios
                                    int codInteresadoAPE = meLanbide84Manager.getCodTerceroExp(codOrganizacion, numExpAPE, 1, adapt);

                                    //asociar expedientes
                                    ExpedienteVO exp2 = new ExpedienteVO();
                                    IdExpedienteVO idexpediente2 = new IdExpedienteVO(respuestaInicio.getIdExpediente().getEjercicio(), respuestaInicio.getIdExpediente().getNumero(), numExpAPE, respuestaInicio.getIdExpediente().getProcedimiento());
                                    exp2.setIdExpedienteVO(idexpediente2);
                                    int codUsuario = expAPE.getUsuario();
                                    log.info("------------llama a relacionarExpedientes ----------");

                                    respuestaRelacion = binding.relacionarExpedientes(expENTAP, exp2, codUsuario, inf);

                                    if (respuestaRelacion.getStatus() == 1) {
                                        codigoOperacion = "8";
                                        log.error("Ha ocurrido un error al asociar expedientes");
                                        ErrorBean errorB = new ErrorBean();
                                        errorB.setIdError("TRAMITACION_ENTAP_003");
                                        errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Ha ocurrido un error en WSTramitacion  al asociar expedientes");
                                        errorB.setSituacion("relacionarExpedientes");
                                        grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Error en WSTramitacion  al asociar expedientes ", numExpAPE);

                                    } else {
                                        log.info("------------Respuesta de relacionarExpedientes()----------");
                                        log.info("RESPUESTA: " + respuestaRelacion.getError());
                                        log.info("Status: " + respuestaRelacion.getStatus());
                                        log.info("------------Fin Respuesta de relacionarExpedientes()----------");

                                        log.debug("------------Datos Expediente2----------");
                                        log.debug("Ejercicio: " + exp2.getIdExpedienteVO().getEjercicio());
                                        log.debug("Numero: " + exp2.getIdExpedienteVO().getNumero());
                                        log.debug("Expediente2: " + exp2.getIdExpedienteVO().getNumeroExpediente());
                                        log.debug("Procedimiento: " + exp2.getIdExpedienteVO().getProcedimiento());
                                        log.debug("Usuario: " + exp2.getUsuario());
                                        log.debug("------------Fin Datos Expediente2----------");

                                        // grabar sexo - f nacimiento - nivel - f solicitud - f inicio -ENTIDAD
                                        int camposGrabados = 0;
                                        boolean grabado = false;
                                        String tabla = "";
                                        String campo = "";
                                        // SEXO
                                        try {
                                            tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_DESPLE_TERCEROS, ConstantesMeLanbide84.FICHERO_PROPIEDADES);
                                            campo = ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_SEXO_TER, ConstantesMeLanbide84.FICHERO_PROPIEDADES);
                                            if (meLanbide84Manager.existeSuplementarioTercero(codOrganizacion, tabla, campo, codInteresadoAPE, adapt)) {
//                                        grabado = meLanbide84Manager.actualizarSuplementarioTercero(codOrganizacion, tabla, campo, codInteresadoAPE, String.valueOf(persona.getSexo()), adapt);
                                                grabado = true;
                                            } else {
                                                grabado = meLanbide84Manager.grabarSuplementarioTercero(codOrganizacion, tabla, campo, codInteresadoAPE, String.valueOf(persona.getSexo()), adapt);
                                                camposGrabados++;
                                            }
                                            if (!grabado) {
                                                log.error("Ha ocurrido un error al grabar " + campo);
                                                ErrorBean errorB = new ErrorBean();
                                                errorB.setIdError("TRAMITACION_ENTAP_001");
                                                errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                                errorB.setSituacion("iniciarExpedientes");
                                                grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Error al grabar " + campo, numExpAPE);
                                            }
                                        } catch (Exception ex) {
                                            log.error("Ha ocurrido un error al grabar " + campo, ex);
                                            ErrorBean errorB = new ErrorBean();
                                            errorB.setIdError("TRAMITACION_ENTAP_001");
                                            errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                            errorB.setSituacion("iniciarExpedientes");
                                            grabarError(errorB, ex.getMessage() != null ? ex.getMessage() : "null", ex.toString(), numExpAPE);
                                        }
                                        // F NAC TERCERO
                                        try {
                                            tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide84.TABLA_FECHAS_TERCEROS, ConstantesMeLanbide84.FICHERO_PROPIEDADES);
                                            campo = ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_FNAC_TER, ConstantesMeLanbide84.FICHERO_PROPIEDADES);
                                            if (meLanbide84Manager.existeSuplementarioTercero(codOrganizacion, tabla, campo, codInteresadoAPE, adapt)) {
//                                        grabado = meLanbide84Manager.actualizarSuplementarioTercero(codOrganizacion, tabla, campo, codInteresadoAPE, dateFormat.format(persona.getFechaNacimiento()), adapt);
                                                grabado = true;
                                            } else {
                                                grabado = meLanbide84Manager.grabarSuplementarioTercero(codOrganizacion, tabla, campo, codInteresadoAPE, dateFormat.format(persona.getFechaNacimiento()), adapt);
                                                camposGrabados++;
                                            }
                                            if (!grabado) {
                                                log.error("Ha ocurrido un error al grabar " + campo);
                                                ErrorBean errorB = new ErrorBean();
                                                errorB.setIdError("TRAMITACION_ENTAP_001");
                                                errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                                errorB.setSituacion("iniciarExpedientes");
                                                grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Error al grabar " + campo, numExpAPE);
                                            }
                                        } catch (Exception ex) {
                                            log.error("Ha ocurrido un error al grabar " + campo, ex);
                                            ErrorBean errorB = new ErrorBean();
                                            errorB.setIdError("TRAMITACION_ENTAP_001");
                                            errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                            errorB.setSituacion("iniciarExpedientes");
                                            grabarError(errorB, ex.getMessage() != null ? ex.getMessage() : "null", ex.toString(), numExpAPE);
                                        }
                                        // NIVEL ACADEMICO
                                        try {
                                            campo = ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_NIVEL, ConstantesMeLanbide84.FICHERO_PROPIEDADES);
                                            if (meLanbide84Manager.existeSuplementarioDesplegable(codOrganizacion, campo, numExpAPE, adapt)) {
                                                grabado = true;
                                            } else {
                                                grabado = meLanbide84Manager.grabarSuplementarioDesplegable(codOrganizacion, campo, numExpAPE, persona.getNivelAcademico(), adapt);
                                                camposGrabados++;
                                            }
                                            if (!grabado) {
                                                log.error("Ha ocurrido un error al grabar " + campo);
                                                ErrorBean errorB = new ErrorBean();
                                                errorB.setIdError("TRAMITACION_ENTAP_001");
                                                errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                                errorB.setSituacion("iniciarExpedientes");
                                                grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Error al grabar " + campo, numExpAPE);
                                            }
                                        } catch (Exception ex) {
                                            log.error("Ha ocurrido un error al grabar " + campo, ex);
                                            ErrorBean errorB = new ErrorBean();
                                            errorB.setIdError("TRAMITACION_ENTAP_001");
                                            errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                            errorB.setSituacion("iniciarExpedientes");
                                            grabarError(errorB, ex.getMessage() != null ? ex.getMessage() : "null", ex.toString(), numExpAPE);
                                        }
                                        // FECHA SOLICITUD
                                        try {
                                            campo = ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_FEC_SOLICITUD, ConstantesMeLanbide84.FICHERO_PROPIEDADES);
                                            if (meLanbide84Manager.existeSuplementarioFecha(codOrganizacion, campo, numExpAPE, adapt)) {
                                                grabado = true;
                                            } else {
                                                grabado = meLanbide84Manager.grabarSuplementarioFecha(codOrganizacion, campo, numExpAPE, persona.getFechaSolicitud(), adapt);
                                                camposGrabados++;
                                            }
                                            if (!grabado) {
                                                log.error("Ha ocurrido un error al grabar " + campo);
                                                ErrorBean errorB = new ErrorBean();
                                                errorB.setIdError("TRAMITACION_ENTAP_001");
                                                errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                                errorB.setSituacion("iniciarExpedientes");
                                                grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Error al grabar " + campo, numExpAPE);
                                            }
                                        } catch (Exception ex) {
                                            log.error("Ha ocurrido un error al grabar " + campo, ex);
                                            ErrorBean errorB = new ErrorBean();
                                            errorB.setIdError("TRAMITACION_ENTAP_001");
                                            errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                            errorB.setSituacion("iniciarExpedientes");
                                            grabarError(errorB, ex.getMessage() != null ? ex.getMessage() : "null", ex.toString(), numExpAPE);
                                        }
                                        // FECHA INICIO
                                        try {
                                            campo = ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_FEC_INICIO, ConstantesMeLanbide84.FICHERO_PROPIEDADES);
                                            if (meLanbide84Manager.existeSuplementarioFecha(codOrganizacion, campo, numExpAPE, adapt)) {
                                                grabado = true;
                                            } else {
                                                grabado = meLanbide84Manager.grabarSuplementarioFecha(codOrganizacion, campo, numExpAPE, persona.getFechaInicio(), adapt);
                                                camposGrabados++;
                                            }
                                            if (!grabado) {
                                                log.error("Ha ocurrido un error al grabar " + campo);
                                                ErrorBean errorB = new ErrorBean();
                                                errorB.setIdError("TRAMITACION_ENTAP_001");
                                                errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                                errorB.setSituacion("iniciarExpedientes");
                                                grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Error algrabar " + campo, numExpAPE);
                                            }
                                        } catch (Exception ex) {
                                            log.error("Ha ocurrido un error al grabar " + campo, ex);
                                            ErrorBean errorB = new ErrorBean();
                                            errorB.setIdError("TRAMITACION_ENTAP_001");
                                            errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                            errorB.setSituacion("iniciarExpedientes");
                                            grabarError(errorB, ex.getMessage() != null ? ex.getMessage() : "null", ex.toString(), numExpAPE);
                                        }
                                        // ENTIDAD
                                        try {
                                            campo = ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_ENTI_COLAB, ConstantesMeLanbide84.FICHERO_PROPIEDADES);//ENTCOLAB
                                            if (meLanbide84Manager.existeSuplementarioDesplegable(codOrganizacion, campo, numExpAPE, adapt)) {
                                                grabado = true;
                                            } else {
                                                String codDespEntidad = null;
                                                codDespEntidad = meLanbide84Manager.getCodEntidad(codOrganizacion, docEntidad, adapt);
                                                if (codDespEntidad != null && !codDespEntidad.isEmpty()) {
                                                    grabado = meLanbide84Manager.grabarSuplementarioDesplegable(codOrganizacion, campo, numExpAPE, codDespEntidad, adapt);
                                                    camposGrabados++;
                                                } else {
                                                    grabado = false;
                                                }

                                            }
                                            if (!grabado) {
                                                log.error("Ha ocurrido un error al grabar " + campo);
                                                ErrorBean errorB = new ErrorBean();
                                                errorB.setIdError("TRAMITACION_ENTAP_001");
                                                errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                                errorB.setSituacion("iniciarExpedientes");
                                                grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Error al grabar " + campo, numExpAPE);
                                            }
                                        } catch (Exception ex) {
                                            log.error("Ha ocurrido un error al grabar " + campo, ex);
                                            ErrorBean errorB = new ErrorBean();
                                            errorB.setIdError("TRAMITACION_ENTAP_001");
                                            errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al grabar " + campo);
                                            errorB.setSituacion("iniciarExpedientes");
                                            grabarError(errorB, ex.getMessage() != null ? ex.getMessage() : "null", ex.toString(), numExpAPE);
                                        }
                                        log.info("Se han grabado " + camposGrabados + " campos en " + numExpAPE);

                                        try {
                                            log.info("------------ generarMisGestionesInicio()----------");
                                            codigoOperacion = misGestInicioOficio(numExpAPE, codOrganizacion);
                                            if (!codigoOperacion.equalsIgnoreCase("0")) {
                                                log.error("Error en la función generarMisGestionesInicio.");
                                                ErrorBean errorB = new ErrorBean();
                                                errorB.setIdError("TRAMITACION_ENTAP_002");
                                                errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error en la función generarMisGestionesInicio().");
                                                errorB.setSituacion("iniciarExpedientes");
                                                grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Error en la función generarMisGestionesInicio().", numExpAPE);

                                            } else {
                                                // llamar a actualizar interesados
                                                //  public String actualizaInteresados (int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) 
                                                MELANBIDE43 meLanbide43 = new MELANBIDE43();
                                                codigoOperacion = meLanbide43.actualizaInteresados(codOrganizacion, 0, 0, numExpAPE);
                                                if (!codigoOperacion.equalsIgnoreCase("0")) {
                                                    log.error("Error en la función actualizaInteresados.");
                                                    ErrorBean errorB = new ErrorBean();
                                                    errorB.setIdError("TRAMITACION_ENTAP_002");
                                                    errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error en la función actualizaInteresados().");
                                                    errorB.setSituacion("actualizaInteresados");
                                                    grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Error en la función actualizaInteresados().", numExpAPE);

                                                }
                                            }
                                        } catch (Exception ex) {
                                            codigoOperacion = "9";
                                            //insertar error en registro de errores           
                                            log.error("Error en la función generarMisGestionesInicio: ", ex);
                                            ErrorBean errorB = new ErrorBean();
                                            errorB.setIdError("TRAMITACION_ENTAP_002");
                                            errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al iniciar un expediente en la función generarMisGestionesInicio().");
                                            errorB.setSituacion("generarMisGestionesInicio");
                                            grabarError(errorB, ex.getMessage() != null ? ex.getMessage() : "null", ex.toString(), numExpAPE);
                                        }

                                    }
                                } else {
                                    log.error("------------Error en iniciarExpediente() ----------");
                                    codigoOperacion = "7";
                                    ErrorBean errorB = new ErrorBean();
                                    errorB.setIdError("TRAMITACION_ENTAP_004");
                                    errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Ha ocurrido un error en WSTramitacion al iniciar expediente " + idExpedienteAPE.getProcedimiento());
                                    errorB.setSituacion("iniciarExpediente");
                                    grabarError(errorB, "Error en el  inicio automático de expedientes APEA/APEI desde ENTAP", "Error en el  inicio automático de expedientes APEA/APEI desde " + numExpENTAP + ". Ha ocurrido un error en WSTramitacion al iniciar expediente " + idExpedienteAPE.getProcedimiento(), numExpediente);
                                }
                            }
                        } catch (RemoteException ex) {
                            log.error("------------EXCEPCION en iniciarExpediente() ---------- " + ex);
                            codigoOperacion = "7";
                            ErrorBean errorB = new ErrorBean();
                            errorB.setIdError("TRAMITACION_ENTAP_004");
                            errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Ha ocurrido un error en WSTramitacion al iniciar expediente " + idExpedienteAPE.getProcedimiento());
                            errorB.setSituacion("iniciarExpediente");
                            grabarError(errorB, ex.getMessage() != null ? ex.getMessage() : "null", ex.toString(), numExpediente);
                        }
                    } else {
                        log.info("Ya se ha iniciado un expediente con esta entidad y persona");
                        // anyado el num Exp para la respuesta
                        expedientesIniciados[i] = persona.getDni() + " ya tiene un expediente " + persona.getPrograma() + " iniciado.";
                        i++;
                        codigoOperacion = "0";
                    }
                } // for persona
            }
        } catch (Exception e) {
            log.error("------------EXCEPCION en iniciarExpediente() ---------- " + e);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0") || codigoOperacion.equalsIgnoreCase("8") || codigoOperacion.equalsIgnoreCase("9")) {
            resultado.setAtributo("expedientesIniciados", expedientesIniciados);
        }
        retornarJSON(new Gson().toJson(resultado), response);
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
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        String numExp = "";
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide84Manager.getInstance().getListaPersonas(numExp, codOrganizacion, adapt);
            if (lista.isEmpty()) {
                log.error("No se ha cargado ningún registro para el expediente " + numExp + " después de cargar el XML");
                codigoOperacion = "4";
            } else {
                codigoOperacion = "0";
                for (PersonaVO pers : lista) {
                    pers.setDescNivel(getDescripcionDesplegable(request, pers.getDescNivel()));
                    pers.setDescSexo(getDescripcionDesplegable(request, pers.getDescSexo()));
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
     * Método que erecuperalos valores de los desplegables del modulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegablesENTAP(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaNivel = MeLanbide84Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_DES_NAPE, ConstantesMeLanbide84.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaPrograma = MeLanbide84Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_DES_APEX, ConstantesMeLanbide84.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaSexo = MeLanbide84Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_DES_SEXO, ConstantesMeLanbide84.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        // nivel academico
        if (!listaNivel.isEmpty()) {
            listaNivel = traducirDesplegable(request, listaNivel);
            request.setAttribute("listaNivel", listaNivel);
        }
        // programa APEA / APEI
        if (!listaPrograma.isEmpty()) {
            listaPrograma = traducirDesplegable(request, listaPrograma);
            request.setAttribute("listaPrograma", listaPrograma);
        }
        // sexo
        if (!listaSexo.isEmpty()) {
            listaSexo = traducirDesplegable(request, listaSexo);
            request.setAttribute("listaSexo", listaSexo);
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
     * Método que retorna el valor de un desplegable en el idiona del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide84.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide84.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (!descripcion.isEmpty() ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide84.CODIGO_IDIOMA_EUSKERA) {
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
        int idioma = ConstantesMeLanbide84.CODIGO_IDIOMA_CASTELLANO;
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide84.CODIGO_IDIOMA_CASTELLANO;
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

    /**
     * Método que graba un error en la tabla de gestion de errores y envia el
     * correspondiente mail
     *
     * @param error
     * @param excepError
     * @param causa
     * @param numExp
     */
    private void grabarError(ErrorBean error, String excepError, String causa, String numExp) {
        try {
            log.info("grabando el error");
            error.setMensajeExcepError(excepError); //churro e.getException
            error.setTraza(excepError);
            error.setCausa(causa);//causa    .getMensaje.getcausa
            log.info("causa: " + causa);
            log.info("numExp: " + numExp);
            String idProcedimiento = "ENTAP";
            String[] exp = null;
            if ("".equals(numExp)) {
                numExp = "0000/ERR/000000";
            } else {
                exp = numExp.split(ConstantesMeLanbide84.BARRA_SEPARADORA);
                idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(exp[1]); //convierteProcedimiento(codProcedimiento);

            }

            log.info("idProcedimiento: " + idProcedimiento);
            error.setIdProcedimiento(idProcedimiento);
            error.setSistemaOrigen("REGEXLAN");
            error.setIdClave("");
            error.setErrorLog("flexia_debug");
            error.setIdFlexia(numExp);

            String respGrabarError = RegistroErrores.registroError(error);
            log.info("Respuesta RegistroErrores.registroError(): " + respGrabarError);
        } catch (Exception ex) {
            log.error("Error al grabarError" + ex);
        }
    }

    /**
     * Método que inicia el expediente en MI CARPETA, ejecuta las mismas
     * operaciones que generarMisGestionesInicio()
     *
     * @param numExpediente
     * @param codOrganizacion
     * @return el codigo de la operacion avanzarGestiones()
     * @throws Exception
     */
    private String misGestInicioOficio(String numExpediente, int codOrganizacion) throws Exception {
        log.info("------------ Inicio generarMisGestionesInicio ----------  " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String codigoOperacion = "-1";
        String idGest = null;
        MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
        try {
            //LAMAR A MIS GESTIONES INICIO                            
            int codTramInicio = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide84.COD_TRAM_INICIO, ConstantesMeLanbide84.FICHERO_PROPIEDADES));
            Date date = new Date();
            String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExpediente, adapt);
            log.debug("fechaIni " + fechaIni);
            date = dateFormat.parse(fechaIni);
            log.debug("date " + date);
            String fecha = meLanbide43Manager.verificarFecha(numExpediente.split(ConstantesMeLanbide84.BARRA_SEPARADORA)[1], adapt);
            Participantes par = meLanbide43Manager.obtenerDatosParticipantes(numExpediente, adapt);
            log.debug("PARTICIPANTES: " + par);
            log.info("tipo id: " + par.getTipoID());
            if (par.getTipoID() != 0) {
                Date fec = null;
                if (!fecha.isEmpty()) {
                    log.debug("fecha " + fecha);
                    fec = dateFormat.parse(fecha);
                    if (date.after(fec)) {
                        log.info("Llama a guardarGestiones()");
                        idGest = MeLanbide43Manager.getInstance().guardarGestiones(numExpediente, codOrganizacion, adapt, codTramInicio, "I");
                        log.info("Id generado: " + idGest);
                        log.info("Llama a avanzarGestiones()");
                        codigoOperacion = MeLanbide43Manager.getInstance().avanzarGestiones(idGest, String.valueOf(codOrganizacion), adapt);
                        log.info("Resultado de avanzarGestiones() " + codigoOperacion);
                    }
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "9";
            meLanbide43Manager.borrarProcesado(idGest, adapt);
            //insertar error en registro de errores           
            log.error("Error en la función generarMisGestionesInicio: ", ex);
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TRAMITACION_ENTAP_002");
            errorB.setMensajeError("Error en el  inicio automático de expedientes APEA/APEI desde ENTAP. Error al iniciar un expediente en la función generarMisGestionesInicio().");
            errorB.setSituacion("iniciarExpedientes");
            grabarError(errorB, ex.getMessage() != null ? ex.getMessage() : "null", ex.toString(), numExpediente);
        }
        log.info("------------ Fin generarMisGestionesInicio ----------");
        return codigoOperacion;
    }

}
