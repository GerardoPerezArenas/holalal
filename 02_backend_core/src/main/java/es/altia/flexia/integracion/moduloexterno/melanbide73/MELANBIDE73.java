/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide73;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide73.manager.MeLanbide73Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide73.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide73.util.ConstantesMeLanbide73;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.UnidadTramitadoraVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.GrupoUnidadesOrganicas;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.ResultadoOperacion;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.UnidadOrganica;
import es.altia.flexia.integracion.moduloexterno.melanbide73.vo.Usuario;
import es.altia.util.ajax.respuesta.RespuestaAjaxUtils;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 *
 * @author Kepa
 */
public class MELANBIDE73 extends ModuloIntegracionExterno {

    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE73.class);
    public String unidadVieja;

    private GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    private Gson gson = gsonBuilder.serializeNulls().create();

    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide73.MELANBIDE73");
        final Object me73Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);

        method.invoke(me73Class, codigoOrganizacion, numeroExpediente, xml);
    }

    /**
     * Prepara la pantalla para el alta de datos y la rellena con los datos en
     * caso de que existan ya.
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return String con el forward a la JSP
     */
    public String unidadRGI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("===========  ENTRA EN unidadRGI");

        List<UnidadTramitadoraVO> listaUnidades = new ArrayList<UnidadTramitadoraVO>();
        List<UnidadTramitadoraVO> listaUnidadesRgi = new ArrayList<UnidadTramitadoraVO>();
        String permisoCambio = "0";
        String codUsuario = null;
        AdaptadorSQLBD adapt = null;

        HttpSession session = request.getSession();
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma = 1;
        int codUsu = 0;
        String css = "";
        String uniVieja = null;
        // recojo el usuario
        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            codUsu = usuarioVO.getIdUsuario();
            idioma = usuarioVO.getIdioma();
            css = usuarioVO.getCss();
            log.debug("Idioma: " + idioma);
            codUsuario = String.valueOf(codUsu);
            log.debug("USU: " + codUsu);
        }
        unidadVieja = uniVieja;

        log.debug("Antigua unidad " + uniVieja + " - " + unidadVieja);
        try {
            if (request.getParameter("codOrganizacion") != null) {
                codOrganizacion = Integer.valueOf(request.getParameter("codOrganizacion"));
            }
            log.debug("Antes de generar el Adaptador de BD");
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            request.setAttribute("numExp", numExpediente);
            listaUnidades = MeLanbide73Manager.getInstance().cargarUnidadesExpediente(numExpediente, adapt);// unidades del expediente
            request.setAttribute("listaUnidades", listaUnidades);

            uniVieja = listaUnidades.get(0).getNomUnidad();

            listaUnidadesRgi = MeLanbide73Manager.getInstance().cargarListaUnidades(adapt);
            request.setAttribute("listaUnidadesRgi", listaUnidadesRgi);

            // aqui comparar las unidades de usuario y RGI para dar permiso
            if (tienePermiso(codUsuario, codOrganizacion)) {
                permisoCambio = "1";
                log.debug("Tiene permiso de cambio");
            } else {
                log.debug("NO tiene permiso de cambio.");
            }

            request.setAttribute("permiso", permisoCambio);
        } catch (Exception ex) {
            ex.printStackTrace();
        }// try-catch

        return "/jsp/extension/melanbide73/melanbide73.jsp";
    }

    /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) {
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
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
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

    public void cambiarUnidadTramitadora(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("==================== Entro en cambioUnidadTramita");
        List<UnidadTramitadoraVO> listaUnidades = new ArrayList<UnidadTramitadoraVO>();
        String codigoOperacion = "0";
        UnidadTramitadoraVO nueva = new UnidadTramitadoraVO();
        boolean modOK = false;
        String uniVieja = null;

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            // parametros
            String numExp = (String) request.getParameter("numExp");
            String id = (String) request.getParameter("id");

            log.debug("Exped: " + numExp);
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Unidad a Modificar ");
                codigoOperacion = "3";
            } //              else if(numExp== null || numExp.equals("")){
            //                                log.debug("No se ha recibido desde la JSP el numero de expediente ");
            //                codigoOperacion = "3";
            //            }
            else {
                MeLanbide73Manager meLanbideManager = MeLanbide73Manager.getInstance();
                // primero recojo la unidad para mostrarla en la ventana de operaciones
                listaUnidades = MeLanbide73Manager.getInstance().cargarUnidadesExpediente(numExp, adapt);// unidades del expediente
                uniVieja = listaUnidades.get(0).getNomUnidad();
                log.debug("Id= " + id);
                log.debug("Expediente: " + numExp);
                nueva.setNomUnidad(id);
                log.debug(nueva.getNomUnidad());

                modOK = meLanbideManager.cambiarUnidad(nueva, numExp, adapt);
                if (modOK) {
                    log.error("================ CAMBIO OK");
                } else {
                    codigoOperacion = "2";
                }
            }
        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "2";
        }// try-catch

        if (modOK) {
            log.debug("Se prepara para grabar en Operaciones Expediente");
            try {

                // DATOS OPERACION  
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                String numExp = (String) request.getParameter("numExp");
                String[] partes = numExp.split("/");
                String ejercicio = partes[0];
                String codProcedimiento = partes[1];
                String[] partesT = null;

                // fecha
                String fechaCortada = "";
                String fechaRecortada = "";
                SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Calendar FECHA_ACTUAL = Calendar.getInstance();
                fechaCortada = formateador.format(FECHA_ACTUAL.getTime());
                log.debug("Fecha cortada " + fechaCortada);
                fechaRecortada = fechaCortada.substring(0, 10);
                log.debug("recortada " + fechaRecortada);
                // usuario
                HttpSession session = request.getSession();
                UsuarioValueObject usuarioVO = new UsuarioValueObject();
                String codUsuario = null;
                String nomUsu = null;
                int codUsu = 0;
                if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    codUsu = usuarioVO.getIdUsuario();

                    codUsuario = String.valueOf(codUsu);
                    log.debug("USU: " + codUsu);
                    nomUsu = usuarioVO.getNombreUsu();
                }
                // descripcion
                String descripcion = null;
                StringBuilder textoXml = new StringBuilder("");
                textoXml.append("<div class=\"movExpC1\">Cambio de Unidad Tramitadora</div>\n"
                        + "<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">{eMovExpNumExp}:</div>\n"
                        + "<div class=\"movExpVal\">").append(numExp).append("</div>\n"
                        + "</div>");
                textoXml.append("<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">{eMovExpCodMun}:</div>\n"
                        + "<div class=\"movExpVal\">").append(codOrganizacion).append("</div>\n"
                        + "</div>");
                textoXml.append("<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">{eMovExpCodProc}:</div>\n"
                        + "<div class=\"movExpVal\">").append(codProcedimiento).append("</div>\n"
                        + "</div>");
                textoXml.append("<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">{eMovExpEjercicio}:</div>\n"
                        + "<div class=\"movExpVal\">").append(ejercicio).append("</div>\n"
                        + "</div>");
                textoXml.append("<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">Operacion: </div>\n"
                        + "<div class=\"movExpVal\">CAMBIO DE UNIDAD TRAMITADORA</div>\n"
                        + "</div>");

                textoXml.append("<div class=\"movExpC2\">Oficinas</div>\n"
                        + "<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">Oficina anterior:</div>\n"
                        + "<div class=\"movExpVal\">").append(uniVieja).append("</div>\n"
                        + "</div>");
                textoXml.append("<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">Nueva oficina:</div>\n"
                        + "<div class=\"movExpVal\">").append(nueva.getNomUnidad()).append("</div>\n"
                        + "</div>");

                textoXml.append("<div class=\"movExpC2\">{eMovExpUsuFec}</div>\n"
                        + "<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">{eMovExpUsuario}:</div>\n"
                        + "<div class=\"movExpVal\">").append(codUsu).append("</div>\n"
                        + "</div>");
                textoXml.append("<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">{eMovExpNomUsuario}:</div>\n"
                        + "<div class=\"movExpVal\">").append(nomUsu).append(" </div>\n"
                        + "</div>\n"
                        + "<div class=\"movExpLin\">\n"
                        + "<div class=\"movExpEtiq\">{gEtiqFecOpe}:</div>\n"
                        + "<div class=\"movExpVal\">").append(fechaCortada).append("</div>\n"
                        + "</div>");
                descripcion = textoXml.toString();

                MeLanbide73Manager meLanbideManager = MeLanbide73Manager.getInstance();
                boolean grabaOK = meLanbideManager.insertarOperacionExpediente(codOrganizacion, ejercicio, numExp, fechaCortada, codUsuario, descripcion, FECHA_ACTUAL, adapt);
                if (grabaOK) {
                    log.debug("REGISTRO OPERACIÓN EXPEDIENTE grabado OK");
                } else {
                    codigoOperacion = "2";
                }

            } catch (Exception e) {
                log.error("ERROR " + e.toString());
            }

        }// if monOK

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
            log.error("Error al modificar Unidad Tramitadora", e);
            e.printStackTrace();
        }//try-catch
    }// cambiarUnidad

    private boolean tienePermiso(String codUsuario, int codOrganizacion) {
        List<UnidadTramitadoraVO> listaUnidadesUsuario = new ArrayList<UnidadTramitadoraVO>();
        boolean permisoCambio = false;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            listaUnidadesUsuario = MeLanbide73Manager.getInstance().cargarUnidadesUsuario(codUsuario, adapt);
            log.debug("---------------   Empieza a comparar --------------------");
            log.debug("Hay " + listaUnidadesUsuario.size() + " unidades-");

            for (int i = 0; i < listaUnidadesUsuario.size(); i++) {
                log.debug("Unidad " + listaUnidadesUsuario.get(i).getCodVisible() + " - " + listaUnidadesUsuario.get(i).getNomUnidad());
                if (listaUnidadesUsuario.get(i).getCodVisible().equalsIgnoreCase(ConfigurationParameter.getParameter(ConstantesMeLanbide73.OFICINA_CENTRAL, ConstantesMeLanbide73.FICHERO_PROPIEDADES))) {
                    permisoCambio = true;
                    log.debug("TRUE");
                } else if (listaUnidadesUsuario.get(i).getCodVisible().equalsIgnoreCase(ConfigurationParameter.getParameter(ConstantesMeLanbide73.OFICINA_ARABA, ConstantesMeLanbide73.FICHERO_PROPIEDADES))) {
                    permisoCambio = true;
                    log.debug("TRUE");
                } else if (listaUnidadesUsuario.get(i).getCodVisible().equalsIgnoreCase(ConfigurationParameter.getParameter(ConstantesMeLanbide73.OFICINA_BIZKAIA, ConstantesMeLanbide73.FICHERO_PROPIEDADES))) {
                    permisoCambio = true;
                    log.debug("TRUE");
                } else if (listaUnidadesUsuario.get(i).getCodVisible().equalsIgnoreCase(ConfigurationParameter.getParameter(ConstantesMeLanbide73.OFICINA_GIPUZKOA, ConstantesMeLanbide73.FICHERO_PROPIEDADES))) {
                    permisoCambio = true;
                    log.debug("TRUE");
                }
            }
        } catch (Exception ex) {
            log.error("Error al comprobar Unidades de Usuario --- ", ex);
        }
        log.debug("Permiso cambio = " + permisoCambio);
        return permisoCambio;
    }// tienePermiso

    public void getGruposUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adaptGenerico = this.getAdaptSQLBDflbgen();

        final List<GeneralComboVO> resultGrupos = MeLanbide73Manager.getInstance().cargarGruposUORS(adaptGenerico,
                codOrganizacion);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(resultGrupos), response);
    }

    public String getGestionGruposUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final AdaptadorSQLBD adaptGenerico = this.getAdaptSQLBDflbgen();

        final List<GeneralComboVO> resultUsuarios = MeLanbide73Manager.getInstance().cargarListaUsuarios(adaptGenerico);
        request.setAttribute("listaUsuarios", resultUsuarios);

        final List<GeneralComboVO> resultUOR = MeLanbide73Manager.getInstance().cargarListaUORs(adapt);
        request.setAttribute("listaUOR", resultUOR);

        final List<GeneralComboVO> resultGrupos = MeLanbide73Manager.getInstance().cargarGruposUORS(adaptGenerico,
                codOrganizacion);
        request.setAttribute("listaGrupos", resultGrupos);

        return "/jsp/extension/melanbide73/gruposuor/GestionGruposUOR.jsp";
    }

    public void insertarGrupoUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adaptGenerico = this.getAdaptSQLBDflbgen();

        final int result = MeLanbide73Manager.getInstance().insertarGrupoUOR(adaptGenerico,
                request.getParameter(("nombreGrupo")), codOrganizacion);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void borrarGrupoUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adaptGenerico = this.getAdaptSQLBDflbgen();
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final boolean result = MeLanbide73Manager.getInstance().borrarGrupoUOR(adaptGenerico, adapt,
                new Integer(request.getParameter("idGrupo")).intValue(),
                codOrganizacion);

        request.setAttribute("resultBorrarGrupo", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void modificarGrupoUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adaptGenerico = this.getAdaptSQLBDflbgen();

        int result;
        try {
            result = MeLanbide73Manager.getInstance().modificarGrupoUOR(adaptGenerico,
                    new Integer(request.getParameter("idGrupo")).intValue(), request.getParameter("nombreGrupo"),
                    codOrganizacion);
        } catch (final NumberFormatException ex) {
            result = -1;
        }
        request.setAttribute("resultModificarGrupo", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void insertarUOREnGrupoUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adaptGenerico = this.getAdaptSQLBDflbgen();
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final boolean result = MeLanbide73Manager.getInstance().insertarUOREnGrupoUOR(adaptGenerico, adapt,
                new Integer(request.getParameter("idGrupo")).intValue(),
                new Integer(request.getParameter("idUOR")).intValue(),
                codOrganizacion);

        request.setAttribute("resultInsertarUORGrupo", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void eliminarUORDeGrupoUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adaptGenerico = this.getAdaptSQLBDflbgen();
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final boolean result = MeLanbide73Manager.getInstance().eliminarUORDeGrupoUOR(adaptGenerico, adapt,
                new Integer(request.getParameter("idGrupo")).intValue(),
                new Integer(request.getParameter("idUOR")).intValue(),
                codOrganizacion);

        request.setAttribute("resultEliminarUORGrupo", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void insertarUsuarioEnGrupoUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adaptGenerico = this.getAdaptSQLBDflbgen();
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final ResultadoOperacion result = MeLanbide73Manager.getInstance().insertarUsuarioEnGrupoUOR(adapt, adaptGenerico,
                new Integer(request.getParameter("idGrupo")).intValue(),
                new Integer(request.getParameter("idUsuario")).intValue(), codOrganizacion);
        request.setAttribute("resultInsertarUsuarioGrupo", result.getCodigo());

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result.getCodigo()), response);
    }

    public void eliminarUsuarioDeGrupoUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adaptGenerico = this.getAdaptSQLBDflbgen();
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final ResultadoOperacion result = MeLanbide73Manager.getInstance().eliminarUsuarioDeGrupoUOR(adapt, adaptGenerico,
                new Integer(request.getParameter("idGrupo")).intValue(),
                new Integer(request.getParameter("idUsuario")).intValue(),
                codOrganizacion);
        request.setAttribute("resultEliminarUsuarioGrupo", result.getCodigo());

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result.getCodigo()), response);
    }

    public void getUnidadesOrganicas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final List<UnidadOrganica> uors = MeLanbide73Manager.getInstance().getUnidadesOrganicas(adapt,
                new Integer(request.getParameter("idGrupo")).intValue(), codOrganizacion);

        final List<GeneralComboVO> result = new ArrayList<GeneralComboVO>();
        for (final UnidadOrganica uor : uors) {
            final GeneralComboVO generalComboVO = new GeneralComboVO();
            generalComboVO.setCodigo("" + uor.getUorCod());
            generalComboVO.setDescripcion(uor.getUorCodVis() + " - " + uor.getUorNom());
            result.add(generalComboVO);
        }
        request.setAttribute("resultgetUnidadesOrganicas", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void getUsuarios(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final List<Usuario> usuarios = MeLanbide73Manager.getInstance().getUsuarios(adapt,
                new Integer(request.getParameter("idGrupo")).intValue(),
                codOrganizacion);

        final List<GeneralComboVO> result = new ArrayList<GeneralComboVO>();
        for (final Usuario usuario : usuarios) {
            final GeneralComboVO generalComboVO = new GeneralComboVO();
            generalComboVO.setCodigo("" + usuario.getUsuCod());
            generalComboVO.setDescripcion(usuario.getUsuLog() + " - " + usuario.getUsuNom());
            result.add(generalComboVO);
        }
        request.setAttribute("resultgetUsuarios", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void getUORsAfectadas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final List<UnidadOrganica> uors = MeLanbide73Manager.getInstance().getUORsAfectadas(adapt,
                new Integer(request.getParameter("idGrupo")).intValue(),
                codOrganizacion);

        final List<GeneralComboVO> result = new ArrayList<GeneralComboVO>();
        for (final UnidadOrganica uo : uors) {
            final GeneralComboVO generalComboVO = new GeneralComboVO();
            generalComboVO.setCodigo("" + uo.getUorCod());
            generalComboVO.setDescripcion(uo.getUorNom());
            result.add(generalComboVO);
        }
        request.setAttribute("resultgetUORsAfectadas", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void getGruposUORsAfectados(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final List<GrupoUnidadesOrganicas> grupos = MeLanbide73Manager.getInstance().getGruposUORsAfectados(adapt,
                new Integer(request.getParameter("idGrupo")).intValue(),
                codOrganizacion);

        final List<GeneralComboVO> result = new ArrayList<GeneralComboVO>();
        for (final GrupoUnidadesOrganicas grupo : grupos) {
            final GeneralComboVO generalComboVO = new GeneralComboVO();
            generalComboVO.setCodigo("" + grupo.getGrpCod());
            generalComboVO.setDescripcion(grupo.getGrpNom());
            result.add(generalComboVO);
        }
        request.setAttribute("resultgetGruposUORsAfectados", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void getGruposUORsAfectadosAlAnadirEliminarUOR(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final List<GrupoUnidadesOrganicas> grupos = MeLanbide73Manager.getInstance().getGruposUORsAfectadosAlAnadirEliminarUOR(
                adapt,
                new Integer(request.getParameter("idGrupo")).intValue(),
                new Integer(request.getParameter("idUOR")).intValue(),
                codOrganizacion);

        final List<GeneralComboVO> result = new ArrayList<GeneralComboVO>();
        for (final GrupoUnidadesOrganicas grupo : grupos) {
            final GeneralComboVO generalComboVO = new GeneralComboVO();
            generalComboVO.setCodigo("" + grupo.getGrpCod());
            generalComboVO.setDescripcion(grupo.getGrpNom());
            result.add(generalComboVO);
        }
        request.setAttribute("resultgetGruposUORsAfectados", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }

    public void existeUOREnOtroGrupo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        final boolean result = MeLanbide73Manager.getInstance().existeUOREnOtroGrupo(adapt,
                new Integer(request.getParameter("idGrupo")).intValue(),
                new Integer(request.getParameter("idUOR")).intValue(),
                codOrganizacion);

        request.setAttribute("resultexisteUOREnOtroGrupo", result);

        RespuestaAjaxUtils.retornarJSON(gson.toJson(result), response);
    }
}// class
