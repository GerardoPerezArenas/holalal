/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide45;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide45.dao.MeLanbide45DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.manager.MeLanbide45Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide45.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide45.util.MeLanbide45Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide45.util.MeLanbide45Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.espacioform.EspacioFormVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.materialconsu.MaterialConsuVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.moduloform.ModuloFormVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.ubicacion.UbicacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.lanbide.formacion.ws.regexlan.AltaEspecialidad;
import net.lanbide.formacion.ws.regexlan.AltaEspecialidadRequest;
import net.lanbide.formacion.ws.regexlan.CentroVO;
import net.lanbide.formacion.ws.regexlan.EspecialidadFicheroVO;
import net.lanbide.formacion.ws.regexlan.ExpedienteVO;
import net.lanbide.formacion.ws.regexlan.ModuloFormativosVO;
import net.lanbide.formacion.ws.regexlan.OcupacionVO;
import net.lanbide.formacion.ws.regexlan.UnidadCompetenciaVO;
import net.lanbide.formacion.ws.regexlan.WSFicheroEspecialidadesFormacionPortBindingStub;
import net.lanbide.formacion.ws.regexlan.WSFicheroEspecialidadesFormacionServiceLocator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.lanbide.formacion.ws.regexlan.ListaModulosFormativos;
import net.lanbide.formacion.ws.regexlan.ListaOcupaciones;
import net.lanbide.formacion.ws.regexlan.ListaUnidadesCompetencia;
import net.lanbide.formacion.ws.regexlan.WsFicheroEspecialidadesFormacionResultado;

/**
 *
 * @author davidg
 */
public class MELANBIDE45 extends ModuloIntegracionExterno {

    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE45.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

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
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
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
                }//try-catch
            }// finally
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    public String cargarPantallaPrincipalModulo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = null;
        int ano = 0;
        try {
            ano = MeLanbide45Utils.getEjercicioDeExpediente(numExpediente);

        } catch (Exception ex) {
            log.error("Error al recuperar el ano getEjercicioDeExpediente " + ano + " -- ", ex);
        }

        if (adapt != null) {
            try {
                // SubPestana Modulos Formativos
                List<ModuloFormVO> listModulosForm = MeLanbide45Manager.getInstance().getDatosModulosForm(numExpediente, adapt);
                if (listModulosForm.size() > 0) {
                    request.setAttribute("listModulosForm", listModulosForm);
                }
                try {
                    url = cargarSubpestanaModulosFormativos(listModulosForm, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaModulosForm", url);
                    }
                } catch (Exception ex) {
                    log.error("eror cargarSubpestanaModulosFormativos -- En carga Pantalla Principal", ex);
                }
                // SubPestana Espacios Formativos
                List<EspacioFormVO> listEspaciosForm = MeLanbide45Manager.getInstance().getDatosEspaciosForm(numExpediente, adapt);
                if (listEspaciosForm.size() > 0) {
                    request.setAttribute("listEspaciosForm", listEspaciosForm);
                }
                try {
                    url = cargarSubpestanaEspaciosFormativos(listEspaciosForm, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaEspaciosFor", url);
                    }
                } catch (Exception ex) {
                    log.error("Eror cargarSubpestanaEspaciosFormativos -- En carga Pantalla Principal", ex);
                }
                // SubPestana Materiales consumidos
                List<MaterialConsuVO> listMaterialConsu = MeLanbide45Manager.getInstance().getDatosMaterialForm(numExpediente, adapt);
                if (listMaterialConsu.size() > 0) {
                    request.setAttribute("listMaterialConsu", listMaterialConsu);
                }
                try {
                    url = cargarSubpestanaMaterialConsu(listMaterialConsu, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaMaterialConsu", url);
                    }
                } catch (Exception ex) {
                    log.error("Eror cargarSubpestanaEspaciosFormativos -- En carga Pantalla Principal", ex);
                }
            } catch (Exception ex) {
                log.error("Eror intentado cargar datos de Subpestanas en En pantalla principal del modulo", ex);
            }
        }
        return "/jsp/extension/melanbide45/melanbide45.jsp";
    }

    private String cargarSubpestanaModulosFormativos(List<ModuloFormVO> listModulosForm, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listModulosForm.size() > 0) {
            try {
                int codIdioma = 1;
                try {
                    String nombreModulo = MeLanbide45Utils.getNombreModulo();
                    request.setAttribute("nombreModulo", nombreModulo);
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                        request.setAttribute("codIdioma", codIdioma);
                    }

                    request.setAttribute("listModulosForm", listModulosForm);
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide45/modulosForm.jsp";
    }

    private String cargarSubpestanaEspaciosFormativos(List<EspacioFormVO> listEspaciosForm, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listEspaciosForm.size() > 0) {
            try {
                int codIdioma = 1;
                try {
                    String nombreModulo = MeLanbide45Utils.getNombreModulo();
                    request.setAttribute("nombreModulo", nombreModulo);
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                    request.setAttribute("listEspaciosForm", listEspaciosForm);
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide45/espaciosForm.jsp";
    }

    private String cargarSubpestanaMaterialConsu(List<MaterialConsuVO> listMatetrialConsu, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listMatetrialConsu.size() > 0) {
            try {
                int codIdioma = 1;
                try {
                    String nombreModulo = MeLanbide45Utils.getNombreModulo();
                    request.setAttribute("nombreModulo", nombreModulo);
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                    request.setAttribute("listMatetrialConsu", listMatetrialConsu);
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide45/materialConsu.jsp";
    }

    public String cargarNuevoModuloForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide45/operaciones/nuevoModuloForm.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarNuevoMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide45/operaciones/nuevoMaterial.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearModuloForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ModuloFormVO> lista = new ArrayList<ModuloFormVO>();
        ModuloFormVO nuevoDato = new ModuloFormVO();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            String denominacion = (String) request.getParameter("denominacion");
            String duracion = (String) request.getParameter("duracion");
            duracion = duracion.replace(",", ".");
            String objetivo = (String) request.getParameter("objetivo");
            String contenidoTP = (String) request.getParameter("contenidoTP");
            String codMod = (String) request.getParameter("codMod");
            String codUC = (String) request.getParameter("codUC");
            String desUC = (String) request.getParameter("desUC");
            String duracionMax = (String) request.getParameter("duracionMax");
            duracionMax = duracionMax.replace(",", ".");
            String ucNivel = (String) request.getParameter("ucNivel");
            String ucExiste = (String) request.getParameter("ucExiste");
            String nivel = (String) request.getParameter("nivel");
            String existe = (String) request.getParameter("existe");

            nuevoDato.setNumExp(numExpediente);
            nuevoDato.setDenominacion(denominacion);
            if (!duracion.isEmpty() && !duracion.equals("")) {
                try {
                    BigDecimal tempSup = new BigDecimal(duracion);
                    nuevoDato.setDuracion(tempSup);
                } catch (Exception ex) {
                    log.debug("Ha habido un erro al hacer el parseo a BigDecimal del campo Duracion " + duracion);
                    throw ex;
                }
            }
            nuevoDato.setObjetivo(objetivo);
            nuevoDato.setContenidoTP(contenidoTP);

            nuevoDato.setCodMod(codMod);
            nuevoDato.setCodUC(codUC);
            nuevoDato.setDesUC(desUC);
            if (!duracionMax.isEmpty() && !duracionMax.equals("")) {
                try {
                    BigDecimal tempSup = new BigDecimal(duracionMax);
                    nuevoDato.setDuracMax(tempSup);
                } catch (Exception ex) {
                    log.debug("Ha habido un erro al hacer el parseo a BigDecimal del campo Duracion " + duracion);
                    throw ex;
                }
            }
            nuevoDato.setUc_nivel(ucNivel);
            nuevoDato.setUc_existe(ucExiste);
            nuevoDato.setNivel(nivel);
            nuevoDato.setExiste(existe);

            MeLanbide45Manager meLanbide45Manager = MeLanbide45Manager.getInstance();
            boolean insertOK = meLanbide45Manager.crearNuevoModuloForm(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide45Manager.getInstance().getDatosModulosForm(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.debug("No se ha podido Insertar El Nuevo Modulo formativo para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (ModuloFormVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<MDF_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</MDF_NUM>");
            xmlSalida.append("<MDF_DEN>");
            xmlSalida.append(fila.getDenominacion());
            xmlSalida.append("</MDF_DEN>");
            xmlSalida.append("<MDF_DUR>");
            xmlSalida.append(fila.getDuracion() != null ? fila.getDuracion().toString() : "");
            xmlSalida.append("</MDF_DUR>");
            xmlSalida.append("<MDF_OBJ>");
            xmlSalida.append(fila.getObjetivo());
            xmlSalida.append("</MDF_OBJ>");
            xmlSalida.append("<MDF_CTP>");
            xmlSalida.append(fila.getContenidoTP());
            xmlSalida.append("</MDF_CTP>");

            xmlSalida.append("<MDF_COD>");
            xmlSalida.append(fila.getCodMod());
            xmlSalida.append("</MDF_COD>");
            xmlSalida.append("<MDF_UC_COD>");
            xmlSalida.append(fila.getCodUC());
            xmlSalida.append("</MDF_UC_COD>");
            xmlSalida.append("<MDF_UC_DEN>");
            xmlSalida.append(fila.getDesUC());
            xmlSalida.append("</MDF_UC_DEN>");
            xmlSalida.append("<MDF_DUR_MAX_TEL>");
            xmlSalida.append(fila.getDuracMax());
            xmlSalida.append("</MDF_DUR_MAX_TEL>");
            xmlSalida.append("<MDF_UC_NIVEL>");
            xmlSalida.append(fila.getUc_nivel());
            xmlSalida.append("</MDF_UC_NIVEL>");
            xmlSalida.append("<MDF_UC_EXISTENTE>");
            xmlSalida.append(fila.getUc_existe());
            xmlSalida.append("</MDF_UC_EXISTENTE>");
            xmlSalida.append("<MDF_NIVEL>");
            xmlSalida.append(fila.getNivel());
            xmlSalida.append("</MDF_NIVEL>");
            xmlSalida.append("<MDF_EXISTENTE>");
            xmlSalida.append(fila.getExiste());
            xmlSalida.append("</MDF_EXISTENTE>");
            xmlSalida.append("</FILA>");
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
        }//try-catch

    }

    public void crearMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MaterialConsuVO> lista = new ArrayList<MaterialConsuVO>();
        MaterialConsuVO nuevoDato = new MaterialConsuVO();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            String descripcion = (String) request.getParameter("descripcion");
            String cantidad = (String) request.getParameter("cantidad");

            nuevoDato.setNumExp(numExpediente);
            nuevoDato.setDescripcion(descripcion);
            if (!cantidad.isEmpty() && !cantidad.equals("")) {
                try {
                    Integer tempSup = new Integer(cantidad);
                    nuevoDato.setCantidad(tempSup);
                } catch (NumberFormatException ex) {
                    log.debug("Ha habido un erro al hacer el parseo a BigDecimal del campo cantidad " + cantidad);
                    throw ex;
                }
            }

            MeLanbide45Manager meLanbide45Manager = MeLanbide45Manager.getInstance();
            boolean insertOK = meLanbide45Manager.crearMaterialConsu(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide45Manager.getInstance().getDatosMaterialConsu(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.debug("No se ha podido Insertar El Nuevo Modulo formativo para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (MaterialConsuVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<MAC_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</MAC_NUM>");
            xmlSalida.append("<MAC_DET>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</MAC_DET>");
            xmlSalida.append("<MAC_CANT>");
            xmlSalida.append(fila.getCantidad() != null ? fila.getCantidad().toString() : "");
            xmlSalida.append("</MAC_CANT>");
            xmlSalida.append("</FILA>");
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
        }//try-catch

    }

    public String cargarModifModuloForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            if (id != null && !id.equals("")) {
                ModuloFormVO datModif = MeLanbide45Manager.getInstance().getModuloFormPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide45/operaciones/nuevoModuloForm.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public String cargarModifMaterialConsu(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            if (id != null && !id.equals("")) {
                MaterialConsuVO datModif = MeLanbide45Manager.getInstance().getMaterialesPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide45/operaciones/nuevoMaterial.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarModuloForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ModuloFormVO> lista = new ArrayList<ModuloFormVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String denominacion = (String) request.getParameter("denominacion");
            String duracion = (String) request.getParameter("duracion");
            duracion = duracion.replace(",", ".");
            String objetivo = (String) request.getParameter("objetivo");
            String contenidoTP = (String) request.getParameter("contenidoTP");
            String codMod = (String) request.getParameter("codMod");
            String codUC = (String) request.getParameter("codUC");
            String desUC = (String) request.getParameter("desUC");
            String duracionMax = (String) request.getParameter("duracionMax");
            duracionMax = duracionMax.replace(",", ".");
            String ucNivel = (String) request.getParameter("ucNivel");
            String ucExiste = (String) request.getParameter("ucExiste");
            String nivel = (String) request.getParameter("nivel");
            String existe = (String) request.getParameter("existe");

            ModuloFormVO datModif = MeLanbide45Manager.getInstance().getModuloFormPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.parseInt(id));
            datModif.setNumExp(numExpediente);
            datModif.setDenominacion(denominacion);
            datModif.setDuracion((!duracion.equals("") ? new BigDecimal(duracion) : null));
            datModif.setObjetivo(objetivo);
            datModif.setContenidoTP(contenidoTP);
            datModif.setCodMod(codMod);
            datModif.setCodUC(codUC);
            datModif.setDesUC(desUC);
            datModif.setDuracMax((!duracionMax.equals("") ? new BigDecimal(duracionMax) : null));
            datModif.setUc_nivel(ucNivel);
            datModif.setUc_existe(ucExiste);
            datModif.setNivel(nivel);
            datModif.setExiste(existe);

            MeLanbide45Manager meLanbide45Manager = MeLanbide45Manager.getInstance();
            boolean modOK = meLanbide45Manager.modificarModuloForm(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide45Manager.getInstance().getDatosModulosForm(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (ModuloFormVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<MDF_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</MDF_NUM>");
            xmlSalida.append("<MDF_DEN>");
            xmlSalida.append(fila.getDenominacion());
            xmlSalida.append("</MDF_DEN>");
            xmlSalida.append("<MDF_DUR>");
            xmlSalida.append(fila.getDuracion() != null ? fila.getDuracion().toString() : "");
            xmlSalida.append("</MDF_DUR>");
            xmlSalida.append("<MDF_OBJ>");
            xmlSalida.append(fila.getObjetivo());
            xmlSalida.append("</MDF_OBJ>");
            xmlSalida.append("<MDF_CTP>");
            xmlSalida.append(fila.getContenidoTP());
            xmlSalida.append("</MDF_CTP>");

            xmlSalida.append("<MDF_COD>");
            xmlSalida.append(fila.getCodMod());
            xmlSalida.append("</MDF_COD>");
            xmlSalida.append("<MDF_UC_COD>");
            xmlSalida.append(fila.getCodUC());
            xmlSalida.append("</MDF_UC_COD>");
            xmlSalida.append("<MDF_UC_DEN>");
            xmlSalida.append(fila.getDesUC());
            xmlSalida.append("</MDF_UC_DEN>");
            xmlSalida.append("<MDF_DUR_MAX_TEL>");
            xmlSalida.append(fila.getDuracMax());
            xmlSalida.append("</MDF_DUR_MAX_TEL>");
            xmlSalida.append("<MDF_UC_NIVEL>");
            xmlSalida.append(fila.getUc_nivel());
            xmlSalida.append("</MDF_UC_NIVEL>");
            xmlSalida.append("<MDF_UC_EXISTENTE>");
            xmlSalida.append(fila.getUc_existe());
            xmlSalida.append("</MDF_UC_EXISTENTE>");
            xmlSalida.append("<MDF_NIVEL>");
            xmlSalida.append(fila.getNivel());
            xmlSalida.append("</MDF_NIVEL>");
            xmlSalida.append("<MDF_EXISTENTE>");
            xmlSalida.append(fila.getExiste());
            xmlSalida.append("</MDF_EXISTENTE>");
            xmlSalida.append("</FILA>");
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
        }//try-catch

    }

    public void modificarMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MaterialConsuVO> lista = new ArrayList<MaterialConsuVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String descripcion = (String) request.getParameter("descripcion");
            String cantidad = (String) request.getParameter("cantidad");

            MaterialConsuVO datModif = MeLanbide45Manager.getInstance().getMaterialesPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.parseInt(id));
            datModif.setNumExp(numExpediente);
            datModif.setDescripcion(descripcion);
            datModif.setCantidad((!cantidad.equals("") ? new Integer(cantidad) : null));

            MeLanbide45Manager meLanbide45Manager = MeLanbide45Manager.getInstance();
            boolean modOK = meLanbide45Manager.modificarMateriales(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide45Manager.getInstance().getDatosMaterialForm(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (MaterialConsuVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<MAC_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</MAC_NUM>");
            xmlSalida.append("<MAC_DET>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</MAC_DET>");
            xmlSalida.append("<MAC_CANT>");
            xmlSalida.append(fila.getCantidad() != null ? fila.getCantidad().toString() : "");
            xmlSalida.append("</MAC_CANT>");
            xmlSalida.append("</FILA>");
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
        }//try-catch

    }

    public void eliminarModuloForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ModuloFormVO> lista = new ArrayList<ModuloFormVO>();
        try {
            String id = (String) request.getParameter("id");
            Integer idtemp = null;
            if (id == null || id.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idtemp = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idtemp != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide45Manager.getInstance().eliminarModuloForm(numExpediente, idtemp, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide45Manager.getInstance().getDatosModulosForm(numExpediente, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (ModuloFormVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<MDF_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</MDF_NUM>");
            xmlSalida.append("<MDF_DEN>");
            xmlSalida.append(fila.getDenominacion());
            xmlSalida.append("</MDF_DEN>");
            xmlSalida.append("<MDF_DUR>");
            xmlSalida.append(fila.getDuracion() != null ? fila.getDuracion().toString() : "");
            xmlSalida.append("</MDF_DUR>");
            xmlSalida.append("<MDF_OBJ>");
            xmlSalida.append(fila.getObjetivo());
            xmlSalida.append("</MDF_OBJ>");
            xmlSalida.append("<MDF_CTP>");
            xmlSalida.append(fila.getContenidoTP());
            xmlSalida.append("</MDF_CTP>");

            xmlSalida.append("<MDF_COD>");
            xmlSalida.append(fila.getCodMod());
            xmlSalida.append("</MDF_COD>");
            xmlSalida.append("<MDF_UC_COD>");
            xmlSalida.append(fila.getCodUC());
            xmlSalida.append("</MDF_UC_COD>");
            xmlSalida.append("<MDF_UC_DEN>");
            xmlSalida.append(fila.getDesUC());
            xmlSalida.append("</MDF_UC_DEN>");
            xmlSalida.append("<MDF_DUR_MAX_TEL>");
            xmlSalida.append(fila.getDuracMax());
            xmlSalida.append("</MDF_DUR_MAX_TEL>");
            xmlSalida.append("</FILA>");
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
        }//try-catch
    }

    public void eliminarMateriales(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MaterialConsuVO> lista = new ArrayList<MaterialConsuVO>();
        try {
            String id = (String) request.getParameter("id");
            Integer idtemp = null;
            if (id == null || id.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idtemp = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idtemp != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide45Manager.getInstance().eliminarMaterialConsu(numExpediente, idtemp, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide45Manager.getInstance().getDatosMaterialConsu(numExpediente, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (MaterialConsuVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<MAC_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</MAC_NUM>");
            xmlSalida.append("<MAC_CANT>");
            xmlSalida.append(fila.getCantidad() != null ? fila.getCantidad().toString() : "");
            xmlSalida.append("</MAC_CANT>");
            xmlSalida.append("<MAC_DET>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</MAC_DET>");
            xmlSalida.append("</FILA>");
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
        }//try-catch
    }

    public String cargarNuevoEspacioForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide45/operaciones/nuevoEspacioForm.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarModifEspacioForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            if (id != null && !id.equals("")) {
                EspacioFormVO datModif = MeLanbide45Manager.getInstance().getEspacioFormPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide45/operaciones/nuevoEspacioForm.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void crearEspacioForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspacioFormVO> lista = new ArrayList<EspacioFormVO>();
        EspacioFormVO nuevoDato = new EspacioFormVO();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            String descripcion = (String) request.getParameter("descripcion");
            String superficie = (String) request.getParameter("superficie");
            superficie = superficie.replace(",", ".");

            nuevoDato.setNumExp(numExpediente);
            nuevoDato.setDescripcion(descripcion);
            if (!superficie.isEmpty() && !superficie.equals("")) {
                try {
                    BigDecimal tempSup = new BigDecimal(superficie);
                    nuevoDato.setSuperficie(tempSup);
                } catch (Exception ex) {
                    log.debug("Ha habido un erro al hacer el parseo a BigDecimal del campo Superficie " + superficie);
                    throw ex;
                }
            }

            MeLanbide45Manager meLanbide45Manager = MeLanbide45Manager.getInstance();
            boolean insertOK = meLanbide45Manager.crearNuevoEspacioForm(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide45Manager.getInstance().getDatosEspaciosForm(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.debug("No se ha podido Insertar El Nuevo Espacio formativo para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (EspacioFormVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<EPF_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</EPF_NUM>");
            xmlSalida.append("<EPF_DES>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</EPF_DES>");
            xmlSalida.append("<EPF_SUP>");
            xmlSalida.append(fila.getSuperficie() != null ? fila.getSuperficie().toString() : "");
            xmlSalida.append("</EPF_SUP>");
            xmlSalida.append("</FILA>");
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
        }//try-catch

    }

    public void modificarEspacioForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspacioFormVO> lista = new ArrayList<EspacioFormVO>();
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try {
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String descripcion = (String) request.getParameter("descripcion");
            String superficie = (String) request.getParameter("superficie");
            superficie = superficie.replace(",", ".");

            EspacioFormVO datModif = MeLanbide45Manager.getInstance().getEspacioFormPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.parseInt(id));
            datModif.setNumExp(numExpediente);
            datModif.setDescripcion(descripcion);
            datModif.setSuperficie((!superficie.equals("") ? new BigDecimal(superficie) : null));

            MeLanbide45Manager meLanbide45Manager = MeLanbide45Manager.getInstance();
            boolean modOK = meLanbide45Manager.modificarEspacioForm(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide45Manager.getInstance().getDatosEspaciosForm(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (EspacioFormVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<EPF_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</EPF_NUM>");
            xmlSalida.append("<EPF_DES>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</EPF_DES>");
            xmlSalida.append("<EPF_SUP>");
            xmlSalida.append(fila.getSuperficie() != null ? fila.getSuperficie().toString() : "");
            xmlSalida.append("</EPF_SUP>");
            xmlSalida.append("</FILA>");
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
        }//try-catch

    }

    public void eliminarEspacioForm(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspacioFormVO> lista = new ArrayList<EspacioFormVO>();
        try {
            String id = (String) request.getParameter("id");
            Integer idtemp = null;
            if (id == null || id.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idtemp = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idtemp != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide45Manager.getInstance().eliminarEspacioForm(numExpediente, idtemp, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide45Manager.getInstance().getDatosEspaciosForm(numExpediente, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE45.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (EspacioFormVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<EPF_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</EPF_NUM>");
            xmlSalida.append("<EPF_DES>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</EPF_DES>");
            xmlSalida.append("<EPF_SUP>");
            xmlSalida.append(fila.getSuperficie() != null ? fila.getSuperficie().toString() : "");
            xmlSalida.append("</EPF_SUP>");
            xmlSalida.append("</FILA>");
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
        }//try-catch
    }

    /**
     *
     * Recupera las ubicaciones de un determinado centro y las devuelve en un
     * XML para que pueda ser visualizado
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     */
    public void consultarUbicaciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        ArrayList<UbicacionVO> ubicaciones = null;
        StringBuffer xml = new StringBuffer();
        String codError = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String codCentro = null;
        log.info(this.getClass().getName() + ".consultarUbicaciones =====>");

        try {
            adapt = this.getAdaptSQLBD(Integer.toString(codOrganizacion));
            con = adapt.getConnection();

            /**
             * Mensaje puede tomar los siguientes valores:
             * 1 --> recuperar el documento de interesado con rol por defecto del expediente
             * 2 --> El expediente no tiene un interesado con rol por defecto y con un CIF como documento 
             * 3 --> Se ha producido un error técnico al intentar recuperar el código de centro
             * 4 --> No se ha podido recuperar el código de centro asociado al documento del interesado con rol por defecto en el expediente 
             * 5 --> Error técnico al recuperar las ubicaciones del centro
             * 6 --> El centro no tienen asignada ninguna ubicación
             * 7 --> Ha ocurrido un error técnico al ejecutar la operación
             * 9 --> Existe más de un centro para ese CIF
             */
            String documento = null;
            boolean continuar = false;
            try {
                // Se busca el interesado con rol principal en el expediente
                documento = MeLanbide45DAO.getInstance().getDocumentoInteresadoRolDefecto(codOrganizacion, numExpediente, con);
                continuar = true;
            } catch (MeLanbide45Exception e) {
                documento = null;
            }

            if (!continuar) {
                codError = "1"; // Error al recuperar el documento de interesado con rol por defecto del expediente
            } else {

                //if (documento == null || "".equals(documento)) {
                //codError = "2"; // El expediente no tiene un interesado con rol por defecto y con un CIF como documento                
                //} else {
                ArrayList<String> codigosCentros = null;
                continuar = false;
                try {
                    codigosCentros = MeLanbide45DAO.getInstance().getCodCentro(documento, con);
                    log.info("codigosCentros " + codigosCentros);
                    continuar = true;
                } catch (MeLanbide45Exception e) {
                    continuar = false;
                }

                if (!continuar) {
                    codError = "3"; // Se ha producido un error técnico al recuperar el código de centro
                } else {
                    log.info("continuar" + continuar);

                    if (codigosCentros != null && codigosCentros.size() > 1) {
                        codError = "9";
                    } else if (codigosCentros == null || codigosCentros.size() == 0) {
                        codError = "4";
                    } else if (codigosCentros != null && codigosCentros.size() == 1) {

                        codCentro = codigosCentros.get(0);
                        continuar = false;
                        try {
                            // Se procede a recuperar las ubicaciones del centro
                            ubicaciones = MeLanbide45DAO.getInstance().getUbicacionesCentro(codCentro, con);
                            continuar = true;
                        } catch (MeLanbide45Exception e) {
                            continuar = false;
                        }

                        if (!continuar) {
                            codError = "5"; // Error técnico al recuperar las ubicaciones del centro
                        } else {
                            if (ubicaciones == null || ubicaciones.isEmpty()) {
                                codError = "6"; // El centro no tienen asignada ninguna ubicación
                            } else {
                                codError = "0"; // Se ha ejecutado la operación correctamente
                            }
                        }
                    }
                }
                //}
            }

            xml.append("<RESPUESTA>");
            xml.append("<STATUS>");
            xml.append(codError);
            xml.append("</STATUS>");
            if (codError.equals("0")) {
                xml.append("<CODCENTRO>");
                xml.append(codCentro);
                xml.append("</CODCENTRO>");
            }

            if (codError.equals("0") && ubicaciones != null) {
                xml.append("<UBICACIONES>");
                for (int i = 0; i < ubicaciones.size(); i++) {
                    UbicacionVO ubicacion = ubicaciones.get(i);
                    xml.append("<UBICACION>");
                    xml.append("<CODUBICACION>");
                    xml.append(ubicacion.getCodUbicacion());
                    xml.append("</CODUBICACION>");

                    xml.append("<NOMBREUBICACION>");
                    xml.append(ubicacion.getNombreUbicacion());
                    xml.append("</NOMBREUBICACION>");

                    xml.append("<TIPOCALLE>");
                    xml.append(ubicacion.getTipoCalle());
                    xml.append("</TIPOCALLE>");

                    xml.append("<NOMBRECALLE>");
                    xml.append(ubicacion.getNombreCalle());
                    xml.append("</NOMBRECALLE>");

                    xml.append("<NUMEROCALLE>");
                    xml.append(ubicacion.getNumeroCalle());
                    xml.append("</NUMEROCALLE>");

                    xml.append("<BIS>");
                    if (ubicacion.getBis() != null) {
                        xml.append(ubicacion.getBis());
                    } else {
                        xml.append("-");
                    }
                    xml.append("</BIS>");

                    xml.append("<ESCALERA>");
                    if (ubicacion.getEscalera() != null) {
                        xml.append(ubicacion.getEscalera());
                    } else {
                        xml.append("-");
                    }
                    xml.append("</ESCALERA>");

                    xml.append("<PISO>");
                    if (ubicacion.getPiso() != null) {
                        xml.append(ubicacion.getPiso());
                    } else {
                        xml.append("-");
                    }
                    xml.append("</PISO>");

                    xml.append("<PUERTA>");
                    if (ubicacion.getPuerta() != null) {
                        xml.append(ubicacion.getPuerta());
                    } else {
                        xml.append("-");
                    }
                    xml.append("</PUERTA>");

                    xml.append("<CODPROVINCIA>");
                    xml.append(ubicacion.getCodProvincia());
                    xml.append("</CODPROVINCIA>");

                    xml.append("<DESCPROVINCIA>");
                    xml.append(ubicacion.getDescProvincia());
                    xml.append("</DESCPROVINCIA>");

                    xml.append("<CODMUNICIPIO>");
                    xml.append(ubicacion.getCodMunicipio());
                    xml.append("</CODMUNICIPIO>");

                    xml.append("<DESCMUNICIPIO>");
                    xml.append(ubicacion.getDescMunicipio());
                    xml.append("</DESCMUNICIPIO>");

                    xml.append("<CODPOSTAL>");
                    xml.append(ubicacion.getCodPostal());
                    xml.append("</CODPOSTAL>");
                    xml.append("</UBICACION>");
                }

                xml.append("</UBICACIONES>");
            }
            xml.append("</RESPUESTA>");
            log.info("XML: " + xml.toString());

        } catch (BDException e) {

            xml.append("<RESPUESTA>");
            xml.append("<STATUS>");
            xml.append("7");
            xml.append("</STATUS>");
            xml.append("</RESPUESTA>");

        } finally {
            try {
                if (adapt != null) {
                    adapt.devolverConexion(con);
                }
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xml.toString());
                out.flush();
                out.close();

            } catch (BDException e) {
            } catch (IOException e) {
            }
        }
        log.info(this.getClass().getName() + ".consultarUbicaciones <=====");
    }

     /**
     * 
     * Para un expediente, se graba el código de centro y el código de ubicación seleccionados, en 
     * determinados campos suplementarios de tipo fichero
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     */
    public void grabarUbicacionSeleccionada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        log.info(this.getClass().getSimpleName()+ ".grabarUbicacionSeleccionada =====> " + numExpediente);
        StringBuffer xml = new StringBuffer();
        String codError = null;
        String codigoUbicacion = null;
        String codigoCentro = null;
        
        String codigoCampoCodCentro = null;
        String codigoCampoCodUbicacion = null;
        String codigoCampoCodCenso = null;
        String codigoCampoCodCensoLanbide = null;
        String numCensoSilicoi = null;
        String numCensoLanbide=null;
        
        try {

            codigoUbicacion = request.getParameter("codigoUbicacion");
            codigoCentro = request.getParameter("codigoCentro");
            log.info("codUbicacion: " + codigoUbicacion);
            log.info("codCentro: " + codigoCentro);

            /**
             * Valores posibles de la variable codError
             * 0 --> OK
             * 1 --> Error al recuperar codigoUbicacion o el código de centro
             * 2 --> No se ha podido recuperar el código del campo "Código de centro" para grabar su valor
             * 3 --> No se ha podido recuperar el código del campo "Código de ubicación" para grabar su valor
             * 4 --> No se ha podido grabar el valor del código del centro
             * 5 --> No se ha podido grabar el valor del código de la ubicación 
             * 6 --> Se ha producido un error técnico
             * 7 --> No se ha podido recuperar el número de censo Silicoi
             * 8 --> No se han podido recuperar los códigos corr
             * 9 --> No se han podido recuperar corrServicio
             * 10 --> No se han podido recuperar corrSubservicio
             * 11 --> No se han podido recuperar el número de censo Lanbide
             */
            if (codigoUbicacion == null || codigoCentro == null || codigoCentro.length() == 0 || codigoUbicacion.length() == 0) {
                log.error("Error al recuperar codigoUbicacion o el código de centro");
                codError = "1";
            } else {

                codigoCampoCodCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO", this.getNombreModulo());
                codigoCampoCodUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION", this.getNombreModulo());
                codigoCampoCodCenso = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMCENSOSILCOI", this.getNombreModulo());
                codigoCampoCodCensoLanbide = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMCENSOLANBIDE", this.getNombreModulo());
                //codigoCampoCodCenso = "NUMCENSOSILCOI2";

                log.info("codigoCampoCodCenso: " + codigoCampoCodCenso);

                if (codigoCampoCodCentro == null || codigoCampoCodCentro.length() == 0) {
                    log.error("No se ha podido recuperar el código del campo Código de centro para grabar su valor");
                    codError = "2";
                } else if (codigoCampoCodUbicacion == null || codigoCampoCodUbicacion.length() == 0) {
                    log.error("No se ha podido recuperar el código del campo Código de ubicación para grabar su valor");
                    codError = "3";
                } else {
                    String[] datosExpediente = numExpediente.split("/");
                    String ejercicio = datosExpediente[0];
                    String codProcedimiento = datosExpediente[1];

                    String[] datosCorr = null;
                    datosCorr = MeLanbide45Manager.getInstance().getCodigosCorr(codigoCentro, codigoUbicacion, getAdaptSQLBD(Integer.toString(codOrganizacion)));
                    if (datosCorr == null || datosCorr.length == 0) {
                        log.error("No se han podido recuperar los códigos corr");
                        codError = "8";
                    } else {
                        if (datosCorr[0] == null || datosCorr[0].isEmpty() || datosCorr[0].equals("")) {
                            log.error("NO hay corrServicio");
                            codError = "9";
                        } else if (datosCorr[1] == null || datosCorr[1].isEmpty() || datosCorr[1].equals("")) {
                            log.error("NO hay corrSUBServicio");
                            codError = "10";
                        } else {
                            numCensoSilicoi = MeLanbide45Manager.getInstance().getNumCensoSILICOI(codigoCentro, codigoUbicacion, datosCorr[1], getAdaptSQLBD(Integer.toString(codOrganizacion)));
                            if (numCensoSilicoi == null) {
                                log.error("Error al recuperar el número de Censo SILICOI");
                                codError = "7";
                            } else {
                                log.info("numCenso SILICOI: " + numCensoSilicoi);
                                //grabamos el número de censo SILICOI como dato suplementario
                                IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
                                CampoSuplementarioModuloIntegracionVO campoSilicoi = new CampoSuplementarioModuloIntegracionVO();
                                campoSilicoi.setCodOrganizacion(Integer.toString(codOrganizacion));
                                campoSilicoi.setCodProcedimiento(codProcedimiento);
                                campoSilicoi.setEjercicio(ejercicio);
                                campoSilicoi.setCodigoCampo(codigoCampoCodCenso);
                                campoSilicoi.setValorTexto(numCensoSilicoi);
                                campoSilicoi.setNumExpediente(numExpediente);
                                campoSilicoi.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                                SalidaIntegracionVO salidaCampoNumCenso = el.grabarCampoSuplementario(campoSilicoi);
                                log.info("censo SILICOI grabado: " + salidaCampoNumCenso.getStatus());
                                //if(salidaCampoNumCenso.getStatus()!=0){
                                //codError = "4";
                                //}else{

                                // numCensoLanbide = MeLanbide45Manager.getInstance().getNumCensoLanbide(codigoCentro, codigoUbicacion, getAdaptSQLBD(Integer.toString(codOrganizacion)));
                                numCensoLanbide = MeLanbide45Manager.getInstance().getNumeroCensoCenFor(codigoCentro, codigoUbicacion, datosCorr[1], getAdaptSQLBD(Integer.toString(codOrganizacion)));
                                if (numCensoLanbide == null || numCensoLanbide.isEmpty() || numCensoLanbide.equalsIgnoreCase("")) {
                                    log.error("No se ha podido recuperar el Número de Censo Lanbide");
                                    codError = "11";
                                } else {
                                    log.info("numCensoLanbide: " + numCensoLanbide);

                                    //grabamos el número de censo como dato suplementario
                                    CampoSuplementarioModuloIntegracionVO campoCensoLanbide = new CampoSuplementarioModuloIntegracionVO();
                                    campoCensoLanbide.setCodOrganizacion(Integer.toString(codOrganizacion));
                                    campoCensoLanbide.setCodProcedimiento(codProcedimiento);
                                    campoCensoLanbide.setEjercicio(ejercicio);
                                    campoCensoLanbide.setCodigoCampo(codigoCampoCodCensoLanbide);
                                    campoCensoLanbide.setNumExpediente(numExpediente);
                                    campoCensoLanbide.setValorTexto(numCensoLanbide);
                                    campoCensoLanbide.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                                    SalidaIntegracionVO salidaCampoNumCensoLanbide = el.grabarCampoSuplementario(campoCensoLanbide);
                                    log.info("censo lanbide grabado: " + salidaCampoNumCensoLanbide.getStatus());
                                    if (salidaCampoNumCensoLanbide.getStatus() != 0) {
                                        log.error("No se ha podido grabar el Número de Censo Lanbide");
                                        codError = "4";
                                    } else {
                                        // Se graba el valor del código de centro en el campo suplementario de "Código de centro", que es 
                                        // de tipo texto
                                        CampoSuplementarioModuloIntegracionVO campoCentro = new CampoSuplementarioModuloIntegracionVO();
                                        campoCentro.setCodOrganizacion(Integer.toString(codOrganizacion));
                                        campoCentro.setCodProcedimiento(codProcedimiento);
                                        campoCentro.setEjercicio(ejercicio);
                                        campoCentro.setCodigoCampo(codigoCampoCodCentro);
                                        campoCentro.setNumExpediente(numExpediente);
                                        campoCentro.setValorTexto(codigoCentro);
                                        campoCentro.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                                        SalidaIntegracionVO salidaCampoCodCentro = el.grabarCampoSuplementario(campoCentro);

                                        log.info("codCentro grabado: " + salidaCampoCodCentro.getStatus());

                                        if (salidaCampoCodCentro.getStatus() != 0) {
                                            log.error("No se ha podido grabar el código del centro");
                                            codError = "4";
                                        } else {
                                            // Se graba el valor del código de ubicación del centro en el campo suplementario de "Código de ubicación", que es 
                                            // de tipo texto
                                            CampoSuplementarioModuloIntegracionVO campoUbicacion = new CampoSuplementarioModuloIntegracionVO();
                                            campoUbicacion.setCodOrganizacion(Integer.toString(codOrganizacion));
                                            campoUbicacion.setCodProcedimiento(codProcedimiento);
                                            campoUbicacion.setEjercicio(ejercicio);
                                            campoUbicacion.setCodigoCampo(codigoCampoCodUbicacion);
                                            campoUbicacion.setNumExpediente(numExpediente);
                                            campoUbicacion.setValorTexto(codigoUbicacion);
                                            campoUbicacion.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                                            SalidaIntegracionVO salidaCampoCodUbicacion = el.grabarCampoSuplementario(campoUbicacion);

                                            log.info("codUbicacion grabado: " + salidaCampoCodUbicacion.getStatus());
                                            if (salidaCampoCodUbicacion.getStatus() != 0) {
                                                log.error("No se ha podido grabar el código de la ubicación");
                                                codError = "5";
                                            } else {
                                                codError = "0";
                                            }
                                        }
                                    }
                                }
                                //}      
                            }
                        }
                    }
                }
            }
            xml.append("<RESPUESTA>");
            xml.append("<STATUS>");
            xml.append(codError);
            xml.append("</STATUS>");
            if (codError.equals("0")) {
                xml.append("<CODCENSOLANBIDE>");
                xml.append(numCensoLanbide);
                xml.append("</CODCENSOLANBIDE>");
                xml.append("<CODUBIC>");
                xml.append(numCensoSilicoi);
                xml.append("</CODUBIC>");
            }
            xml.append("</RESPUESTA>");
            log.info("XML: " + xml.toString());

        } catch (Exception e) {
            log.error("error en grabarUbicacionSeleccionada", e);

            xml.append("<RESPUESTA>");
            xml.append("<STATUS>");
            xml.append("6");
            xml.append("</STATUS>");
            xml.append("</RESPUESTA>");

        } finally {
            try {
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xml.toString());
                out.flush();
                out.close();

            } catch (IOException e) {
            }
        }
        log.info(this.getClass().getName() + ".grabarUbicacionSeleccionada <=====");
    }

    /**
     * Método que da de alta una especialidad en el fichero de especialiades de formacion.
     * Para ello hay que realizar una llamada al servicio web correspondiente
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return String que puede tomar los siguientes valores:
     * "0": OK
     * "1": Error al recuperar las propiedades del fichero de configuracion
     * "2": Error al recuperar los campos desplegables externos
     * "3": Error al recuperar los datos para la llamada al servicio web 
     * "4": Algún valor obligario de los Modulos Formativos o Unidades de Competencia no esta informado
     * "5": Error al establecer la conexión con el servicio web 
     * "10": El campo suplementartio Subtipo Especialidad es obligatorio y no viene informado
     * "11": El campo suplementario Familia Profesional es obligatorio y no viene informado 
     * "12": El campo suplementario Área Profesional es obligatorio y no viene informado 
     * "13": El campo suplementario Fecha Resolución es obligatorio y no viene informado
     * "14": El campo suplementario Código del Centro es obligatorio y no viene informado 
     * "15": El campo suplementario Código de Ubicación del Centro es obligatorio y no viene informado
     * "16": El campo suplementario Código de Especialidad es obligatorio y no viene informado 
     * "17": El campo suplementario Denominación es obligatorio y no viene informado 
     * "18": El campo suplementario Objetivo General de Formación es obligatorio y no viene informado 
     * "19": El campo suplementario Duración es obligatorio y no viene informado
     * "20": El campo suplementario Duración es obligatorio y no viene informado
     * "21": El campo suplementario Duración Máxima Teleformación es obligatorio y no viene informado
     * "22": El campo suplementario Nivel de Cualificación es obligatorio y no viene informado
     * "23": El campo suplementario Ocupaciones de Referencia es obligatorio y no viene informado
     *
     *
     */
    public String altaEspecialidadFicheroEspecialidadesFormacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {

        String salida = null;
        String urlWS = null;
        String campoCodigoCentro = null;
        String campoCodigoUbicacion = null;
        String campoFechaResolucion = null;
        String campoCodigoEspecialidad = null;
        String campoSubtipo = null;
        String campoDenominacion = null;
        String campoFamiliaProfesional = null;
        String campoAreaProfesional = null;
        String campoDuracion = null;
        String campoObjetivoFormacion = null;
        String campoDuracionMaximoTeleformacion = null;
        String campoDuracionMaximoPresencial = null;
        String campoNivelCualificacion = null;
        String campoCodOcupacion = null;
        String tipoEspecialidad = null;
        String subTipo = null;
        String familia = null;
        String area = null;
        OcupacionVO ocupacionVO = new OcupacionVO();
        OcupacionVO ocupacionVO2 = new OcupacionVO();
        OcupacionVO ocupacionVO3 = new OcupacionVO();
        OcupacionVO ocupacionVO4 = new OcupacionVO();
        EspecialidadFicheroVO especialidadFicheroVO = new EspecialidadFicheroVO();
        ExpedienteVO expedienteVO = new ExpedienteVO();
        CentroVO centroVO = new CentroVO();

        ListaUnidadesCompetencia listaUnidades = new ListaUnidadesCompetencia();
        ListaModulosFormativos listaModulos = new ListaModulosFormativos();
        ListaOcupaciones listaOcupaciones = new ListaOcupaciones();

        OcupacionVO[] ocupaciones = null;
        ModuloFormativosVO[] modulosForm = null;
        WSFicheroEspecialidadesFormacionPortBindingStub binding = null;
        boolean conexionWS = false;
        boolean error = false;
        boolean propiedadesConfiguracion = false;
        boolean camposDesplegablesExternos = false;
        boolean cargaDatosServicio = false;
        SalidaIntegracionVO salidaFecResolucion = null;
        SalidaIntegracionVO salidaCampoCodigoCentro = null;
        SalidaIntegracionVO salidaCampoCodigoUbicacion = null;
        SalidaIntegracionVO salidaCampoCodigoEspecialidad = null;
        SalidaIntegracionVO salidaCampoDenominacion = null;
        SalidaIntegracionVO salidaCampoObjetivoFormacion = null;
        SalidaIntegracionVO salidaCampoDuracion = null;
        SalidaIntegracionVO salidaCampoDuracionMaxPresencial = null;
        SalidaIntegracionVO salidaCampoDuracionMaxTeleformacion = null;
        SalidaIntegracionVO salidaCampoNivelCualificacion = null;
        SalidaIntegracionVO salidaCampoCodigoOcupacion = null;
        UnidadCompetenciaVO unidadCompetenciaVO = null;
        UnidadCompetenciaVO[] unidadesCompetencia = null;
        WsFicheroEspecialidadesFormacionResultado respuesta = null;
//        AltaEspecialidadResponse respuesta = null;
        boolean modifEspecialidad = false;
        boolean modifMFoUC = false;

        //Utilizaremos esta HashMap para guardar el ID de cada Modulo y asi poder hacer el update cuando el binding ha ido bien
        HashMap<Integer, ModuloFormVO> mapModuloID = new HashMap<Integer, ModuloFormVO>();

        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        try {
            urlWS = ConfigurationParameter.getParameter("URL_WS_FICHERO_ESPECIALIDADES_FORMACION", this.getNombreModulo());
            campoFechaResolucion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TRAMITE_FECHA_RESOLUCION", this.getNombreModulo());
            campoCodigoCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO", this.getNombreModulo());
            campoCodigoUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION", this.getNombreModulo());
            campoCodigoEspecialidad = ConfigurationParameter.getParameter("CODIGO_CAMPO_TEXTO_CODIGO_ESPECIALIDAD", this.getNombreModulo());
            campoSubtipo = ConfigurationParameter.getParameter("CODIGO_CAMPO_DESPLEGABLE_EXTERNO_SUBTIPO", this.getNombreModulo());
            campoDenominacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TEXTO_DENOMINACION", this.getNombreModulo());
            campoFamiliaProfesional = ConfigurationParameter.getParameter("CODIGO_CAMPO_DESPLEGABLE_EXTERNO_FAMILIA", this.getNombreModulo());
            campoAreaProfesional = ConfigurationParameter.getParameter("CODIGO_CAMPO_DESPLEGABLE_EXTERNO_AREA", this.getNombreModulo());
            campoObjetivoFormacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TEXTO_LARGO_OBJETIVO_FORMACION", this.getNombreModulo());
            campoDuracion = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMERICO_DURACION", this.getNombreModulo());
            campoDuracionMaximoPresencial = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMERICO_DURACION_MAXIMO_PRESENCIAL", this.getNombreModulo());
            campoDuracionMaximoTeleformacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMERICO_DURACION_MAXIMO_TELEFORMACION", this.getNombreModulo());
            campoNivelCualificacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TEXTO_NIVEL_CUALIFICACION", this.getNombreModulo());
            campoCodOcupacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODOCUPACION", this.getNombreModulo());
            tipoEspecialidad = ConfigurationParameter.getParameter("TIPO_ESPECIALIDAD", this.getNombreModulo());

            propiedadesConfiguracion = true;
        } catch (Exception e) {
            salida = "1";
            log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Error al recuperar las propiedades del fichero de configuración " + this.getNombreModulo() + ".properties. " + e.getMessage());
        }

        log.debug("CODIGO ORGANIZACION: " + codOrganizacion);

        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

        if (propiedadesConfiguracion) {
            String[] datosExp = numExpediente.split("/");
            String ejercicio = datosExp[0];
            String codProcedimiento = datosExp[1];

            try {
                //Recupera los campos desplegables externos                
                subTipo = MeLanbide45Manager.getInstance().getValorCodDesplegableExternoExpediente(codOrganizacion, numExpediente, campoSubtipo, adapt);
                familia = MeLanbide45Manager.getInstance().getValorCodDesplegableExternoExpediente(codOrganizacion, numExpediente, campoFamiliaProfesional, adapt);
                area = MeLanbide45Manager.getInstance().getValorCodDesplegableExternoExpediente(codOrganizacion, numExpediente, campoAreaProfesional, adapt);
                ocupacionVO.setCodOcupacion(MeLanbide45Manager.getInstance().getValorCodDesplegableExternoExpediente(codOrganizacion, numExpediente, "OCUPACION1", adapt));
                ocupacionVO2.setCodOcupacion(MeLanbide45Manager.getInstance().getValorCodDesplegableExternoExpediente(codOrganizacion, numExpediente, "OCUPACION2", adapt));
                ocupacionVO3.setCodOcupacion(MeLanbide45Manager.getInstance().getValorCodDesplegableExternoExpediente(codOrganizacion, numExpediente, "OCUPACION3", adapt));
                ocupacionVO4.setCodOcupacion(MeLanbide45Manager.getInstance().getValorCodDesplegableExternoExpediente(codOrganizacion, numExpediente, "OCUPACION4", adapt));

                camposDesplegablesExternos = true;
            } catch (Exception e) {
                salida = "2";
                log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Error al recuperar los campos desplegables externos." + e.getMessage());
            }

            if (camposDesplegablesExternos) {
                //Recupera los valores de los campos suplementarios a nivel de expediente o tramite y los valida
                if (subTipo == null) {
                    salida = "10";
                    log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Subtipo Especialidad es obligatorio y no viene informado.");
                } else {
                    if (familia == null) {
                        salida = "11";
                        log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Familia Profesional es obligatorio y no viene informado.");
                    } else {
                        if (area == null) {
                            salida = "12";
                            log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Área Profesional es obligatorio y no viene informado.");
                        } else {
                            salidaFecResolucion = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, campoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                            if (salidaFecResolucion.getStatus() != 0) {
                                salida = "13";
                                log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Fecha Resolución es obligatorio y no viene informado.");
                            } else {
                                salidaCampoCodigoCentro = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoCodigoCentro, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                if (salidaCampoCodigoCentro.getStatus() != 0) {
                                    salida = "14";
                                    log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Código del Centro es obligatorio y no viene informado.");
                                } else {
                                    salidaCampoCodigoUbicacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoCodigoUbicacion, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                    if (salidaCampoCodigoUbicacion.getStatus() != 0) {
                                        salida = "15";
                                        log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Código de Ubicación es obligatorio y no viene informado.");
                                    } else {
                                        salidaCampoCodigoEspecialidad = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoCodigoEspecialidad, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                        // Si no hay valor lo calculamos desde base de datos
                                        if (salidaCampoCodigoEspecialidad.getStatus() != 0) {
                                            try {
                                                log.debug("especialidadFicheroVO");
                                                especialidadFicheroVO.setCodigo(MeLanbide45Manager.getInstance().getSiguienteCodEsp(codOrganizacion, numExpediente, area, adapt));
                                                modifEspecialidad = true;
                                                if (especialidadFicheroVO.getCodigo() == null) {
                                                    salida = "25";
                                                    log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Código de Especialidad no es correcto.");
                                                } else {
                                                    salidaCampoCodigoEspecialidad.setStatus(0);
                                                }
                                            } catch (Exception ex) {
                                                salida = "26";
                                                log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Código de Especialidad no ha podido ser generado.");
                                            }
                                        } else {
                                            // IMPORTANTE, vuelvo a calcular un codigo ya que si viene de un retroceso el codigo que existiese seria incorrecto                                            
                                            // lo dejo en el else por si acaso hubiera que cambiarlo para distingir bien donde estamos
                                            try {
                                                especialidadFicheroVO.setCodigo(MeLanbide45Manager.getInstance().getSiguienteCodEsp(codOrganizacion, numExpediente, area, adapt));
                                            } catch (Exception ex) {
                                                salida = "26";
                                                log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Código de Especialidad no ha podido ser generado.");
                                            }
                                        }
                                        if (salidaCampoCodigoEspecialidad.getStatus() == 0) {
                                            salidaCampoDenominacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoDenominacion, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                            if (salidaCampoDenominacion.getStatus() != 0) {
                                                salida = "17";
                                                log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Denominación es obligatorio y no viene informado.");
                                            } else {
                                                salidaCampoObjetivoFormacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoObjetivoFormacion, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO);
                                                /*if (salidaCampoObjetivoFormacion.getStatus()!=0){
                                                 salida = "18";
                                                 log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Objetivo Formación es obligatorio y no viene informado." );                                                    
                                                 }else{*/
                                                salidaCampoDuracion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoDuracion, IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                                                if (salidaCampoDuracion.getStatus() != 0) {
                                                    salida = "19";
                                                    log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Duración es obligatorio y no viene informado.");
                                                } else {
                                                    salidaCampoDuracionMaxPresencial = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoDuracionMaximoPresencial, IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                                                    if (salidaCampoDuracionMaxPresencial.getStatus() != 0) {
                                                        salida = "20";
                                                        log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Duración es obligatorio y no viene informado.");
                                                    } else {
                                                        salidaCampoDuracionMaxTeleformacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoDuracionMaximoTeleformacion, IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                                                        /*if (salidaCampoDuracionMaxTeleformacion.getStatus()!=0){
                                                         salida = "21";
                                                         log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Duración Máxima Teleformación es obligatorio y no viene informado." );                                                                
                                                         }else{*/
                                                        salidaCampoNivelCualificacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, campoNivelCualificacion, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                                        if (salidaCampoNivelCualificacion.getStatus() != 0) {
                                                            salida = "22";
                                                            log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Nivel de Cualificación es obligatorio y no viene informado.");
                                                        } else {
                                                            //salidaCampoCodigoOcupacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion),ejercicio,numExpediente,codProcedimiento,campoCodOcupacion,IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO_LARGO);                                                                                    
                                                            if (ocupacionVO.getCodOcupacion() == null) {
                                                                salida = "23";
                                                                log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): El campo suplementartio Ocupaciones es obligatorio y no viene informado.");
                                                            }
                                                        }
                                                        //}
                                                    }
                                                }
                                                //}
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (salida == null) {

                    try {

                        //Rellena los objetos para la llamada al servicio web
                        Date date = salidaFecResolucion.getCampoSuplementario().getValorFecha().getTime();
                        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                        String fechaResolucion = format1.format(date);
                        expedienteVO.setNumExpediente(numExpediente);
                        expedienteVO.setFechaResolucion(fechaResolucion);
                        log.debug("ExpedienteVO");
                        log.debug("numExp: " + numExpediente);
                        log.debug("fechaResolucion: " + fechaResolucion);

                        centroVO.setGenCenCodCentro(salidaCampoCodigoCentro.getCampoSuplementario().getValorTexto().trim());
                        centroVO.setGenCenCodUbic(salidaCampoCodigoUbicacion.getCampoSuplementario().getValorTexto().trim());
                        log.debug("centroVO");
                        log.debug("setGenCenCodCentro: " + salidaCampoCodigoCentro.getCampoSuplementario().getValorTexto().trim());
                        log.debug("setGenCenCodUbic: " + salidaCampoCodigoUbicacion.getCampoSuplementario().getValorTexto().trim());
                        String[] datosCorr = null;
                        datosCorr = MeLanbide45Manager.getInstance().getCodigosCorr(salidaCampoCodigoCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodigoUbicacion.getCampoSuplementario().getValorTexto(), adapt);
                        if (datosCorr == null || datosCorr.length == 0) {
                            salida = "27";
                        } else {
                            if (datosCorr[0] == null || datosCorr[0].isEmpty() || datosCorr[0].equals("")) {
                                log.error("NO hay corrServicio");
                                // corrServicio
                                salida = "28";
                            } else if (datosCorr[1] == null || datosCorr[1].isEmpty()) {
                                // corrSubservicio 
                                log.error("NO hay corrSUBServicio");
                                salida = "29";
                            } else {
                                log.debug("hay corrSUBServicio: " + datosCorr[1]);
                                centroVO.setCorrSubservicio(datosCorr[1]);
                                log.debug("centro.getCorrSubservicio(): " + centroVO.getCorrSubservicio());
                                /*
                         log.debug("especialidadFicheroVO");
                         if (salidaCampoCodigoEspecialidad.getCampoSuplementario().getValorTexto() == null || "".equals(salidaCampoCodigoEspecialidad.getCampoSuplementario().getValorTexto())) {
                         especialidadFicheroVO.setCodigo(MeLanbide45Manager.getInstance().getSiguienteCodEsp(codOrganizacion, numExpediente, area, adapt));
                         } else {
                         especialidadFicheroVO.setCodigo(salidaCampoCodigoEspecialidad.getCampoSuplementario().getValorTexto().trim());
                         }                         
                         log.debug("Codigo: " + salidaCampoCodigoEspecialidad.getCampoSuplementario().getValorTexto().trim());
                                 */
                                especialidadFicheroVO.setTipo(tipoEspecialidad);
                                log.debug("tipo: " + tipoEspecialidad);
                                especialidadFicheroVO.setSubtipo(subTipo);
                                log.debug("subtipo: " + subTipo);
                                especialidadFicheroVO.setDescripcionC(salidaCampoDenominacion.getCampoSuplementario().getValorTexto().trim());
                                especialidadFicheroVO.setDescripcionE(salidaCampoDenominacion.getCampoSuplementario().getValorTexto().trim());
                                log.debug("Descripcion: " + salidaCampoDenominacion.getCampoSuplementario().getValorTexto().trim());
                                especialidadFicheroVO.setFamilia(familia);
                                log.debug("Familia: " + familia);
                                especialidadFicheroVO.setArea(area);
                                log.debug("area: " + area);
                                if (salidaCampoObjetivoFormacion.getStatus() == 0) {
                                    especialidadFicheroVO.setObjetivo(salidaCampoObjetivoFormacion.getCampoSuplementario().getValorTexto().trim());
                                    log.debug("objetivo: " + salidaCampoObjetivoFormacion);
                                } else {
                                    especialidadFicheroVO.setObjetivo("");
                                }
                                especialidadFicheroVO.setDuracion(salidaCampoDuracion.getCampoSuplementario().getValorNumero());
                                log.debug("Duracion: " + salidaCampoDuracion.getCampoSuplementario().getValorNumero());
                                especialidadFicheroVO.setMaxPresencial(salidaCampoDuracionMaxPresencial.getCampoSuplementario().getValorNumero());
                                log.debug("maxPresencial: " + salidaCampoDuracionMaxPresencial.getCampoSuplementario().getValorNumero());
                                if (salidaCampoDuracionMaxTeleformacion.getStatus() == 0) {
                                    especialidadFicheroVO.setMaxTeleformacion(salidaCampoDuracionMaxTeleformacion.getCampoSuplementario().getValorNumero());
                                    log.debug("maxTeleformacion: " + salidaCampoDuracionMaxTeleformacion.getCampoSuplementario().getValorNumero());
                                } else {
                                    especialidadFicheroVO.setMaxTeleformacion("");
                                }
                                especialidadFicheroVO.setNivel(salidaCampoNivelCualificacion.getCampoSuplementario().getValorTexto().trim());
                                log.debug("nivel: " + salidaCampoNivelCualificacion.getCampoSuplementario().getValorTexto().trim());

                                //Leire
                                //ocupacionVO.setCodOcupacion(salidaCampoCodigoOcupacion.getCampoSuplementario().getValorTexto().trim());
                                log.debug("ocupacionVO");
                                log.debug("codOcupacion: " + ocupacionVO.getCodOcupacion());
                                log.debug("codOcupacion2: " + ocupacionVO2.getCodOcupacion());
                                log.debug("codOcupacion3: " + ocupacionVO3.getCodOcupacion());
                                log.debug("codOcupacion4: " + ocupacionVO4.getCodOcupacion());
                                int i = 1;
                                boolean oc2 = false;
                                boolean oc3 = false;
                                boolean oc4 = false;
                                if (null != ocupacionVO2.getCodOcupacion() && !ocupacionVO2.getCodOcupacion().equals("")) {
                                    i++;
                                    oc2 = true;
                                }
                                if (null != ocupacionVO3.getCodOcupacion() && !ocupacionVO3.getCodOcupacion().equals("")) {
                                    i++;
                                    oc3 = true;
                                }
                                if (null != ocupacionVO4.getCodOcupacion() && !ocupacionVO4.getCodOcupacion().equals("")) {
                                    i++;
                                    oc4 = true;
                                }
                                log.debug("ocupaciones introducidas: " + i);
                                int x = 0;
                                ocupaciones = new OcupacionVO[i];
                                ocupaciones[0] = ocupacionVO;
                                if (oc2) {
                                    x++;
                                    ocupaciones[x] = ocupacionVO2;
                                    log.debug("ocupaciones[x]: " + x + " - " + ocupacionVO2.getCodOcupacion());
                                }
                                if (oc3) {
                                    x++;
                                    ocupaciones[x] = ocupacionVO3;
                                    log.debug("ocupaciones[x]: " + x + " - " + ocupacionVO3.getCodOcupacion());
                                }
                                if (oc4) {
                                    x++;
                                    ocupaciones[x] = ocupacionVO4;
                                    log.debug("ocupaciones[x]: " + x + " - " + ocupacionVO4.getCodOcupacion());
                                }
                                listaOcupaciones.setOcupacion(ocupaciones);
                                //Recupera la lista de los modulos de formacion para construir la lista de unidades de compeencia
                                List<ModuloFormVO> listModulos = MeLanbide45Manager.getInstance().getDatosModulosForm(numExpediente, adapt);

                                int j = 0;
                                unidadesCompetencia = new UnidadCompetenciaVO[listModulos.size()];
                                while (j < listModulos.size() && !error) {
                                    unidadCompetenciaVO = new UnidadCompetenciaVO();
                                    ModuloFormativosVO moduloFormativosVOAux = new ModuloFormativosVO();
                                    ModuloFormVO moduloFormVO = listModulos.get(j);
                                    modifMFoUC = false;

                                    if (moduloFormVO.getUc_existe() == null || moduloFormVO.getUc_existe().length() == 0
                                            || // moduloFormVO.getCodUC()==null || moduloFormVO.getCodUC().length()==0 ||
                                            ("S".equals(moduloFormVO.getUc_existe()) && (moduloFormVO.getCodUC() == null || moduloFormVO.getCodUC().length() == 0))
                                            || moduloFormVO.getExiste() == null || moduloFormVO.getExiste().length() == 0
                                            || // moduloFormVO.getCodMod()==null || moduloFormVO.getCodMod().length()==0 ||
                                            ("S".equals(moduloFormVO.getExiste()) && (moduloFormVO.getCodMod() == null || moduloFormVO.getCodMod().length() == 0))) {
                                        error = true;
                                    } else {

                                        log.debug("indicadorExiste: " + moduloFormVO.getUc_existe());
                                        if ("N".equals(moduloFormVO.getUc_existe())) {
                                            unidadCompetenciaVO.setDescripcionC(moduloFormVO.getDesUC());
                                            unidadCompetenciaVO.setDescripcionE(moduloFormVO.getDesUC());
                                            unidadCompetenciaVO.setNivel(moduloFormVO.getUc_nivel());
                                            log.debug("Descripcion: " + moduloFormVO.getDesUC());
                                            log.debug("Nivel: " + moduloFormVO.getUc_nivel());
                                            // Calculamos siempre el UC para evitar error de cuando hace retroceso
                                            if (moduloFormVO.getCodUC() == null || "".equals(moduloFormVO.getCodUC())) {
                                                unidadCompetenciaVO.setCodigo(MeLanbide45Manager.getInstance().getSiguienteUC(codOrganizacion, numExpediente, area, moduloFormVO.getUc_nivel(), adapt));
                                                modifMFoUC = true;
                                            } else {
                                                unidadCompetenciaVO.setCodigo(MeLanbide45Manager.getInstance().getSiguienteUC(codOrganizacion, numExpediente, area, moduloFormVO.getUc_nivel(), adapt));
                                                modifMFoUC = true;
                                            }
                                        } else {
                                            unidadCompetenciaVO.setCodigo(MeLanbide45Manager.getInstance().getSiguienteUC(codOrganizacion, numExpediente, area, moduloFormVO.getUc_nivel(), adapt));
                                            //unidadCompetenciaVO.setCodigo(moduloFormVO.getCodUC());
                                        }
                                        log.debug("codigo: " + moduloFormVO.getCodUC());

                                        log.debug("unidadCompetenciaVO");
                                        unidadCompetenciaVO.setIndicadorExistente(moduloFormVO.getUc_existe());

                                        //moduloFormativosVOAux.setIndicadorExistente(moduloFormVO.getExiste());
                                        //moduloFormativosVOAux.setCodigo(moduloFormVO.getCodMod());
                                        log.debug("moduloFormativosVO");
                                        if ("N".equals(moduloFormVO.getExiste())) {
                                            moduloFormativosVOAux.setDescripcionC(moduloFormVO.getDenominacion());
                                            moduloFormativosVOAux.setDescripcionE(moduloFormVO.getDenominacion());
                                            moduloFormativosVOAux.setNivel(moduloFormVO.getNivel());
                                            moduloFormativosVOAux.setDuracion(String.valueOf(moduloFormVO.getDuracion()));
                                            moduloFormativosVOAux.setMaxPresencial(String.valueOf(moduloFormVO.getDuracion()));
                                            moduloFormativosVOAux.setMaxTeleformacion(String.valueOf(moduloFormVO.getDuracMax()));
                                            moduloFormativosVOAux.setObjetivos(moduloFormVO.getObjetivo());
                                            moduloFormativosVOAux.setContPracticos(moduloFormVO.getContenidoTP());
                                            // Calculamos siempre el UC para evitar error de cuando hace retroceso
                                            if (moduloFormVO.getCodMod() == null || "".equals(moduloFormVO.getCodMod())) {
                                                moduloFormativosVOAux.setCodigo(MeLanbide45Manager.getInstance().getSiguienteMF(codOrganizacion, numExpediente, area, moduloFormVO.getUc_nivel(), adapt));
                                                modifMFoUC = true;
                                            } else {// Lo dejamos asi para que siempre se recalcule, habra que ver como se quiere hacer cuand retrocede expediente
                                                moduloFormativosVOAux.setCodigo(MeLanbide45Manager.getInstance().getSiguienteMF(codOrganizacion, numExpediente, area, moduloFormVO.getUc_nivel(), adapt));
                                                modifMFoUC = true;
                                            }
                                            log.debug("Descripcion: " + moduloFormVO.getDenominacion());
                                            log.debug("Nivel: " + moduloFormVO.getNivel());
                                            log.debug("Duracion: " + String.valueOf(moduloFormVO.getDuracion()));
                                            log.debug("maxPresencial: " + String.valueOf(moduloFormVO.getDuracMax()));
                                            log.debug("objetivos: " + moduloFormVO.getObjetivo());
                                            log.debug("contPracticos: " + moduloFormVO.getContenidoTP());
                                        } else {
                                            //moduloFormativosVOAux.setCodigo(moduloFormVO.getCodMod());                                
                                            moduloFormativosVOAux.setCodigo(MeLanbide45Manager.getInstance().getSiguienteMF(codOrganizacion, numExpediente, area, moduloFormVO.getUc_nivel(), adapt));
                                            modifMFoUC = true;
                                        }

                                        moduloFormativosVOAux.setIndicadorExistente(moduloFormVO.getExiste());
                                        log.debug("indicadorExiste: " + moduloFormVO.getExiste());
                                        log.debug("codigo: " + moduloFormVO.getCodMod());

                                        modulosForm = new ModuloFormativosVO[1];
                                        modulosForm[0] = moduloFormativosVOAux;
                                        listaModulos.setModuloFormativo(modulosForm);
                                        unidadCompetenciaVO.setListaModulosFormativos(listaModulos);
                                        //    unidadCompetenciaVO.setListaModulosFormativos(modulosForm);
                                        unidadesCompetencia[j] = unidadCompetenciaVO;
                                        listaUnidades.setUnidadCompetencia(unidadesCompetencia);
                                        // Solo en el caso de que se halla modificado lo añadirmos para hacer el update mas adelante
                                        if (modifMFoUC) {
                                            ModuloFormVO mod = new ModuloFormVO();
                                            mod.setCodMod(moduloFormativosVOAux.getCodigo());
                                            mod.setCodUC(unidadCompetenciaVO.getCodigo());
                                            mod.setId(moduloFormVO.getId());
                                            mod.setNumExp(moduloFormVO.getNumExp());
                                            mapModuloID.put(moduloFormVO.getId(), mod);
                                        }
                                        j++;
                                    }
                                }
                                cargaDatosServicio = true;
                                log.debug("unidadesCompetencia: " + Arrays.toString(unidadesCompetencia));
                            }                        
                        } // if datosCorr == null
                    } catch (Exception ex) {
                        salida = "3";
                        log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Error al recuperar los datos para la llamada al servicio web. " + ex.getMessage());
                    }
                    log.error("cargaDatosServicio: " + cargaDatosServicio);
                    if (cargaDatosServicio) {
                        try {
                            if (error) {
                                salida = "4";
                                log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Algún valor obligatorio de los Modulos Formativos o Unidades de Competencia no esta informado. Los valores obligatorios son Código y Existente.");
                            } else {

                                try {
                                    log.error("Antes de establecer el servicio URL: " + urlWS);
                                    binding = (WSFicheroEspecialidadesFormacionPortBindingStub) new WSFicheroEspecialidadesFormacionServiceLocator().getWSFicheroEspecialidadesFormacionPort(new java.net.URL(urlWS));
                                    log.error("Despues de establecer el servicio");
                                    conexionWS = true;
                                } catch (MalformedURLException e) {
                                    log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Error al establecer conexion con WSFicheroEspecialidadesFormacion. " + e.getMessage());
                                } catch (Exception e) {
                                    log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Error al establecer conexion con WSFicheroEspecialidadesFormacion. " + e.getMessage());
                                }

                                if (!conexionWS) {
                                    salida = "5";
                                } else {
                                    AltaEspecialidad altaEspecialidad = new AltaEspecialidad();
                                    log.error("procedemos a dar el alta de la especialidad");
                                    AltaEspecialidadRequest altaEspecialidadRequest = new AltaEspecialidadRequest();

                                    altaEspecialidadRequest.setExpediente(expedienteVO);
                                    altaEspecialidadRequest.setCentro(centroVO);
                                    altaEspecialidadRequest.setEspecialidad(especialidadFicheroVO);
                                    altaEspecialidadRequest.setListaUnidadesCompetencia(listaUnidades);
                                    altaEspecialidadRequest.setListaOcupaciones(listaOcupaciones);

                                    altaEspecialidad.setAltaEspecialidadRequest(altaEspecialidadRequest);

                                    log.error("Llamada al servicio web de altaEspecialidad");
                                    //Llamada al servicio web para el alta de especialidades
//                                    respuesta = binding.altaEspecialidad(altaEspecialidad);
                                    respuesta = binding.altaEspecialidad(altaEspecialidadRequest);
                                    log.error("despues del alta respuesta: " + respuesta);
//                                    if (respuesta != null && respuesta.get_return().getCodRdo().equals(0)) {
                                    if (respuesta != null && respuesta.getCodRdo().equals(0)) {
                                        salida = "0";
                                        // En este caso el binding ha ido bien asi que procedemos a actualizar los datos.                                        

                                        boolean codEsp = false;
                                        try {
                                            codEsp = MeLanbide45Manager.getInstance().crearCodigoModifEspecialidad(expedienteVO.getNumExpediente(), codOrganizacion, especialidadFicheroVO.getCodigo(), adapt);
                                        } catch (Exception ex) {
                                            if (!codEsp) {
                                                log.error("Error al guardar el codigo de Especialidad que ha sido creado automaticamente");
                                            }
                                        }
                                        // Actualizamos el codigo UC y MF

                                        for (Map.Entry e : mapModuloID.entrySet()) {
                                            ModuloFormVO modForm = new ModuloFormVO();
                                            modForm = (ModuloFormVO) e.getValue();
                                            // En este punto tengo recuperado los Modulos y Espacios Formativos
                                            // asi que los guardamos en la tabla correspondiente
                                            boolean bien = MeLanbide45Manager.getInstance().modificarMFyUCModuloForm(modForm, adapt);
                                            if (!bien) {
                                                log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Error al guardar los codigos MF y UC tras llamar al WS ");
                                                salida = "24";
                                            }
                                        }

                                    } else {
                                        log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Error del servicio web. " + respuesta.getDescRdo());
                                        salida = respuesta.getDescRdo();
                                    }
                                }// if !conexion
                            }//if error
                        } catch (Exception ex) {
                            log.error(this.getClass().getName() + ".altaEspecialidadFicheroEspecialidadesFormacion(): Error en la llamada al servicio web. " + ex.getMessage());
                            salida = ex.getMessage();
                            /*guardar el error en BD con envío mail... SÓLO PARA TELETRAMITACIÓN*/
 /*try {
                             String causaExcepcion = (ex.getCause() != null ? (ex.getCause().getMessage() != null ? ex.getCause().getMessage() + " - " + ex.getCause().toString() : ex.getCause().toString()) : "");
                             String mensajeExcepcion = (ex.getMessage()!=null ? ex.getMessage().toString() : ConstantesGestionErroresDokusi.LAN6EXCEPCION_SINMENSAJEERROR_MSG);
                             String trazaError = Lan6UtilExcepcionGE.escribirStackTrace(ex);

                             Lan6ErrorBeanGE errorBean = new Lan6ErrorBeanGE(causaExcepcion, mensajeExcepcion, trazaError, ConstantesGestionErroresDokusi.SISTEMA_ORIGEN_FLEXIA, ConstantesGestionErroresDokusi.COD_FLEX_ERROR_GETDOCUMENTO_TRAMI_REL, ConstantesGestionErroresDokusi.MEN_FLEX_ERROR_GETDOCUMENTO_TRAMI_REL, ex.gettipologia());
                             GestionarErrores.grabarError(errorBean, docGestor, oidDoc, "", this.getClass().getName() + ".getDocumento()");
                             log.error("Error Registrado en BD correctamente.");
                             } catch (Exception ex1) {
                             log.error(" Dokusi. Flexia Error al registrar errores mediante servicios de Adaptadores de platea. Error que se intenta Registrar : " + ex.getMessage(), ex1);
                             throw ex1;
                             }*/
                        }
                    } // if cargaDatosServicio
                } // if salida = null
            }// if campos desplegables
        } // if propiedades configuracion

        return salida;
    }    
   
}
