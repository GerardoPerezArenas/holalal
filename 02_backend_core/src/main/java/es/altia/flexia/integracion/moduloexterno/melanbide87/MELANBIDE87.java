package es.altia.flexia.integracion.moduloexterno.melanbide87;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide87.manager.MeLanbide87Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConstantesMeLanbide87;
import es.altia.flexia.integracion.moduloexterno.melanbide87.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide87.vo.InstalacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
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
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MELANBIDE87 extends ModuloIntegracionExterno {

    private static final Logger log = LogManager.getLogger(MELANBIDE87.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
        log.info("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide87/melanbide87.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<InstalacionVO> listaInstalaciones = MeLanbide87Manager.getInstance().getListaInstalaciones(numExpediente, codOrganizacion, adapt);
                if (listaInstalaciones.size() > 0) {
                    for (InstalacionVO pers : listaInstalaciones) {
                        pers.setDescTipoInst(getDescripcionDesplegable(request, pers.getDescTipoInst()));
                    }
                    request.setAttribute("listaInstalaciones", listaInstalaciones);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos De Instalacions - MELANBIDE87 - cargarPantallaPrincipal", ex);
            }
        }
        return url;
    }

    public String cargarNuevaInstalacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaInstalacion - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevaInstalacion = "/jsp/extension/melanbide87/nuevaInstalacion.jsp?codOrganizacion=" + codOrganizacion;
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
            cargarDesplegablesCOLVU(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva Instalacion : " + ex.getMessage());
        }
        return urlnuevaInstalacion;
    }

    public String cargarModificarInstalacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarInstalacion - " + numExpediente);
        String nuevo = "0";
        String urlnuevaInstalacion = "/jsp/extension/melanbide87/nuevaInstalacion.jsp?codOrganizacion=" + codOrganizacion;
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
            // Recuperramos datos de Instalacion a modificar y cargamos en el request
            if (id != null && !id.equals("")) {
                InstalacionVO datModif = MeLanbide87Manager.getInstance().getInstalacionPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            //Cargamos los valores  de los desplegables
            cargarDesplegablesCOLVU(codOrganizacion, request);
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return urlnuevaInstalacion;
    }

    public void crearNuevaInstalacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarInstalacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<InstalacionVO> lista = new ArrayList<InstalacionVO>();
        InstalacionVO nuevaInstalacion = new InstalacionVO();
        String numExp = null;
        String tipoInst = null;
        String municipio = null;
        String localidad = null;
        String direccion = null;
        String codPost = null;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            numExp = request.getParameter("expediente");
            tipoInst = request.getParameter("tipoInst");
            municipio = request.getParameter("municipio");
            localidad = request.getParameter("localidad");
            direccion = request.getParameter("direccion");
            codPost = request.getParameter("codPost");

            nuevaInstalacion.setNumExp(numExp);
            nuevaInstalacion.setTipoInst(tipoInst);
            nuevaInstalacion.setMunicipio(municipio);
            nuevaInstalacion.setLocalidad(localidad);
            nuevaInstalacion.setDireccion(direccion);
            nuevaInstalacion.setCodPost(Integer.parseInt(codPost));

            MeLanbide87Manager meLanbide87Manager = MeLanbide87Manager.getInstance();
            boolean insertOK = meLanbide87Manager.crearNuevaInstalacion(nuevaInstalacion, adapt);
            if (insertOK) {
                log.debug("Instalacion Insertada Correctamente");
                codigoOperacion = "0";
                lista = meLanbide87Manager.getListaInstalaciones(numExp, codOrganizacion, adapt);
                if (lista.size() > 0) {
                    for (InstalacionVO inst : lista) {
                        inst.setDescTipoInst(getDescripcionDesplegable(request, inst.getDescTipoInst()));
                    }
                }
            } else {
                log.debug("NO se ha insertado correctamente la nueva Instalacion");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto Instalacion2VO" + ex.getMessage());
            codigoOperacion = "2";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarInstalacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarInstalacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<InstalacionVO> lista = new ArrayList<InstalacionVO>();
        String numExp = null;
        String tipoInst = null;
        String municipio = null;
        String localidad = null;
        String direccion = null;
        String codPost = null;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            numExp = request.getParameter("expediente");
            tipoInst = request.getParameter("tipoInst");
            municipio = request.getParameter("municipio");
            localidad = request.getParameter("localidad");
            direccion = request.getParameter("direccion");
            codPost = request.getParameter("codPost");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Instalacion a Modificar ");
                codigoOperacion = "3";
            } else {
                InstalacionVO instalacionModif = new InstalacionVO();
                instalacionModif.setId(Integer.parseInt(id));
                instalacionModif.setNumExp(numExp);
                instalacionModif.setTipoInst(tipoInst);
                instalacionModif.setMunicipio(municipio);
                instalacionModif.setLocalidad(localidad);
                instalacionModif.setDireccion(direccion);
                instalacionModif.setCodPost(Integer.parseInt(codPost));

                MeLanbide87Manager meLanbide87Manager = MeLanbide87Manager.getInstance();
                boolean modOK = meLanbide87Manager.modificarInstalacion(instalacionModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = meLanbide87Manager.getListaInstalaciones(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (InstalacionVO inst : lista) {
                                inst.setDescTipoInst(getDescripcionDesplegable(request, inst.getDescTipoInst()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Instalacions después de Modificar una Instalacion : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de Instalacions después de Modificar una Instalacion : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
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

    public void eliminarInstalacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarInstalacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<InstalacionVO> lista = new ArrayList<InstalacionVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la Instalacion a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide87Manager.getInstance().eliminarInstalacion(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide87Manager.getInstance().getListaInstalaciones(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (InstalacionVO inst : lista) {
                                inst.setDescTipoInst(getDescripcionDesplegable(request, inst.getDescTipoInst()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de Instalacions después de eliminar una Instalacion");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una Instalacion: " + ex);
            codigoOperacion = "2";
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
     * Método que erecuperalos valores de los desplegables del modulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegablesCOLVU(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaTipoInst = MeLanbide87Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide87.COD_DES_TINS, ConstantesMeLanbide87.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        // nivel academico
        if (listaTipoInst.size() > 0) {
            listaTipoInst = traducirDesplegable(request, listaTipoInst);
            request.setAttribute("listaTipoInst", listaTipoInst);
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
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide87.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide87.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (!descripcion.isEmpty() ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide87.CODIGO_IDIOMA_EUSKERA) {
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
        int idioma = ConstantesMeLanbide87.CODIGO_IDIOMA_CASTELLANO;
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide87.CODIGO_IDIOMA_CASTELLANO;
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
