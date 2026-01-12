package es.altia.flexia.integracion.moduloexterno.melanbide89;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide89.i18n.MeLanbide89I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide89.manager.MeLanbide89Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConstantesMeLanbide89;
import es.altia.flexia.integracion.moduloexterno.melanbide89.vo.AccesoVO;
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

public class MELANBIDE89 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE89.class);
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
        log.info("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide89/melanbide89.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<AccesoVO> listaAccesos = MeLanbide89Manager.getInstance().getListaAccesos(numExpediente, codOrganizacion, adapt);
                if (listaAccesos.size() > 0) {
                    request.setAttribute("listaAccesos", listaAccesos);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de accesos - MELANBIDE89 - cargarPantallaPrincipal", ex);
            }
        }

        return url;
    }
   
    public String cargarNuevoAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoAcceso - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String url = "/jsp/extension/melanbide89/nuevoAcceso.jsp?codOrganizacion=" + codOrganizacion;
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
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nuevo acceso : " + ex.getMessage());
        }
        return url;
    }

    public String cargarModificarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarAcceso - " + numExpediente);
        String nuevo = "0";
        String url = "/jsp/extension/melanbide89/nuevoAcceso.jsp?codOrganizacion=" + codOrganizacion;
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
                AccesoVO datModif = MeLanbide89Manager.getInstance().getAccesoPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return url;

    }
    
    public void crearNuevoAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoAcceso - " + numExpediente);
        String codigoOperacion = "-1";
        List<AccesoVO> lista = new ArrayList<AccesoVO>();
        AccesoVO nuevoAcceso = new AccesoVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            log.debug("Recojo");
            String numExp = request.getParameter("numExp");
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dninie = request.getParameter("dninie");
      
            log.debug("Cargo");
            nuevoAcceso.setNumExp(numExp);
            nuevoAcceso.setNombre(nombre);
            nuevoAcceso.setApellido1(apellido1);
            nuevoAcceso.setApellido2(apellido2);
            nuevoAcceso.setDninie(dninie);

            MeLanbide89Manager meLanbide89Manager = MeLanbide89Manager.getInstance();
            boolean insertOK = meLanbide89Manager.crearAcceso(nuevoAcceso, adapt);
            if (insertOK) {
                log.debug("Acceso Insertado Correctamente");
                codigoOperacion = "0";
                lista = meLanbide89Manager.getListaAccesos(numExp, codOrganizacion, adapt);
            } else {
                log.debug("NO se ha insertado correctamente el nuevo acceso");
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

    public void modificarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarAcceso - " + numExpediente);
        String codigoOperacion = "-1";
        List<AccesoVO> listaAccesos = new ArrayList<AccesoVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dninie = request.getParameter("dninie");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del acceso a Modificar ");
                codigoOperacion = "3";
            } else {
                AccesoVO accesoModif = new AccesoVO();
            
                accesoModif.setId(Integer.parseInt(id));
                accesoModif.setNumExp(numExp);
                accesoModif.setNombre(nombre);
                accesoModif.setApellido1(apellido1);
                accesoModif.setApellido2(apellido2);
                accesoModif.setDninie(dninie);
  
                MeLanbide89Manager meLanbide89Manager = MeLanbide89Manager.getInstance();
                boolean modOK = meLanbide89Manager.modificarAcceso(accesoModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        listaAccesos = meLanbide89Manager.getListaAccesos(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de accesos después de Modificar un acceso : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de accesos después de Modificar un acceso : " + ex.getMessage());
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
            resultado.setAtributo("lista", listaAccesos);
        }
        retornarJSON(new Gson().toJson(resultado), response);

    }
    
    public void eliminarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarAcceso - " + numExpediente);
        String codigoOperacion = "-1";
        List<AccesoVO> listaAccesos = new ArrayList<AccesoVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del acceso a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide89Manager.getInstance().eliminarAcceso(id, numExp, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        listaAccesos = MeLanbide89Manager.getInstance().getListaAccesos(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de accesos después de eliminar un acceso");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un acceso: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaAccesos);
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

            request.getSession().setAttribute("mensajeImportar", MeLanbide89I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
        } catch (DataFormatException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE89.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE89.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE89.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE89.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide89I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarListaAccesos.jsp");
        return "/jsp/extension/melanbide89/recargarListaAccesos.jsp";
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
        List<AccesoVO> lista = new ArrayList<AccesoVO>();
        String numExp = "";
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide89Manager.getInstance().getListaAccesos(numExp, codOrganizacion, adapt);
            if (lista.isEmpty()) {
                log.error("No se ha cargado ningún dato para el expediente " + numExp + " después de cargar el XML");
                codigoOperacion = "4";
            } else {
                codigoOperacion = "0";
            }
        } catch (BDException bde) {
            codigoOperacion = "1";
            log.error("Error de tipo BD al recuperar la lista de accesos después de cargar el XML : " + bde.getMensaje());
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error al recuperar la lista de accesos después de cargar el XML : " + ex.getMessage());
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

    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha realiza la petición
     * a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a través del cual se devuelve la salida
     * al cliente que ha realizado la solicitud
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
