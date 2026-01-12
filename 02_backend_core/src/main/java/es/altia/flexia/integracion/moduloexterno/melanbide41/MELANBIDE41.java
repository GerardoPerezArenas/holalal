/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide41;

/**
 *
 * @author davidg
 */
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.mail.MailHelper;
import es.altia.common.service.mail.exception.MailException;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import es.altia.flexia.integracion.moduloexterno.melanbide41.dao.MeLanbide41DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.manager.MeLanbide41Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConstantesMeLanbide41;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.MeLanbide41Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide41.util.MeLanbide41Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.capacidad.CapacidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.disponibilidad.DisponibilidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.dotacion.DotacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.identificacionesp.IdentificacionEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.interesadoexpediente.InteresadoExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.material.MaterialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.servicios.ServiciosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.ubicacion.UbicacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import net.lanbide.formacion.ws.regexlan.AltaCentroConEspecialidades;
import net.lanbide.formacion.ws.regexlan.AltaCentroConEspecialidadesRequest;
import net.lanbide.formacion.ws.regexlan.AltaExpedienteRequest;
import net.lanbide.formacion.ws.regexlan.CentroVO;
import net.lanbide.formacion.ws.regexlan.EspecialidadVO;
import net.lanbide.formacion.ws.regexlan.ExpedienteVO;
import net.lanbide.formacion.ws.regexlan.WSExpedientesFormacionPortBindingStub;
import net.lanbide.formacion.ws.regexlan.WsExpedientesFormacionResultado;
import net.lanbide.formacion.ws.regexlan.WSExpedientesFormacionServiceLocator;
import net.lanbide.formacion.ws.regexlan.WSRegistroCentrosFormacionPortBindingStub;
import net.lanbide.formacion.ws.regexlan.WSRegistroCentrosFormacionServiceLocator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import java.io.IOException;
import net.lanbide.formacion.ws.regexlan.WsRegistroCentrosFormacionResultado;

public class MELANBIDE41 extends ModuloIntegracionExterno {
    //Logger

    private static Logger log = LogManager.getLogger(MELANBIDE41.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en la funcion **** cargarPantallaPrincipal **  traza 1");
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = null;
        int ano = 0;
        try {
            ano = MeLanbide41Utils.getEjercicioDeExpediente(numExpediente);
            //EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(ano, adapt);
            //if(config != null)
            //{
            //    request.getSession().setAttribute("ecaConfiguracion", config);
            //}
        } catch (Exception ex) {
            log.error("Error al recuperar el ano getEjercicioDeExpediente " + ano + " --", ex);
        }

        if (adapt != null) {
            try {
                // SubPestana Especialidades
                List<EspecialidadesVO> listEspecialidades = MeLanbide41Manager.getInstance().getDatosEspecialidades(numExpediente, adapt);
                log.info("DESPUES DE RECOGER ESPECIALIDADES");
                if (listEspecialidades.size() > 0) {
                    request.setAttribute("ListEspecialidades", listEspecialidades);
                }
                try {
                    url = cargarSubpestanaEspecialidades(listEspecialidades, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaEspecialidades", url);
                    }
                } catch (Exception ex) {
                    log.error("eror cargarSubpestanaEspecialidades", ex);
                }
                //Subpestana Servicios
                List<ServiciosVO> listServicios = MeLanbide41Manager.getInstance().getDatosServicios(numExpediente, adapt);
                if (listServicios.size() > 0) {
                    request.setAttribute("listServicios", listServicios);
                }
                try {
                    url = cargarSubpestanaServicios(listServicios, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaServicios", url);
                    }
                } catch (Exception ex) {
                    log.error("eror cargarSubpestanaServicios", ex);
                }
                //Subpestana Disponibilidad
                List<DisponibilidadVO> listDisponibilidad = MeLanbide41Manager.getInstance().getDatosDisponibilidad(numExpediente, adapt);
                if (listDisponibilidad.size() > 0) {
                    request.setAttribute("listDisponibilidad", listDisponibilidad);
                }
                try {
                    url = cargarSubpestanaDisponibilidad(listDisponibilidad, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaDisponibilidad", url);
                    }
                } catch (Exception ex) {
                    log.error("eror cargarSubpestanaDisponibilidad", ex);
                }
            } catch (Exception ex) {
                log.error("eror intentado cargar datos pestañas", ex);
            }
        }
        log.info("antes de retornar la url : /jsp/extension/melanbide41/melanbide41.jsp  -- amano");
        return "/jsp/extension/melanbide41/melanbide41.jsp";
    }

    public String cargarListasxEspecialidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {

        }
        String url = null;
        if (request.getParameter("id") != null && numExpediente != null) {
            try {
                String id = request.getParameter("id");
                EspecialidadesVO especialidad = new EspecialidadesVO();
                especialidad = MeLanbide41Manager.getInstance().getEspecialidadPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (especialidad != null) {
                    request.setAttribute("datoEspecialidad", especialidad);
                }
                //Lista Idetificacion Observaciones
                List<IdentificacionEspVO> listaIdentificacionEsp = MeLanbide41Manager.getInstance().getDatosIdentificacionEsp(numExpediente, especialidad.getId(), adapt);
                if (listaIdentificacionEsp.size() > 0) {
                    request.setAttribute("listaIdentificacionEsp", listaIdentificacionEsp);
                }
                try {
                    url = cargarSubpestanaIdentificacionEsp(listaIdentificacionEsp, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaIdentificacionEsp", url);
                    }
                    url = cargarNuevaIdentificacionEsp(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response, especialidad);
                    if (url != null) {
                        request.setAttribute("urlNuevaIdentificacionEsp", url);
                    }
                } catch (Exception ex) {
                }
                //Lista Capacidad
                List<CapacidadVO> listCapacidad = MeLanbide41Manager.getInstance().getDatosCapacidad(numExpediente, especialidad.getId(), adapt);
                if (listCapacidad.size() > 0) {
                    request.setAttribute("listCapacidad", listCapacidad);
                }
                try {
                    url = cargarSubpestanaCapacidad(listCapacidad, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaCapacidad", url);
                    }
                } catch (Exception ex) {
                }
                //Lista Dotacion 
                            log.debug("Lista Dotacion= " );

                List<DotacionVO> listaDotacion = MeLanbide41Manager.getInstance().getDatosDotacion(numExpediente, especialidad.getId(), adapt);
                if (listaDotacion.size() > 0) {
                    request.setAttribute("listaDotacion", listaDotacion);
                }
                             log.debug("url Dotacion= " );
               try {
                    url = cargarSubpestanaDotacion(listaDotacion, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaDotacion", url);
                    }
                } catch (Exception ex) {
                }
                //Lista Materal de Consumo
                             log.debug("Lista Material= " );
               List<MaterialVO> listaMaterial = MeLanbide41Manager.getInstance().getDatosMaterial(numExpediente, especialidad.getId(), adapt);
                if (listaMaterial.size() > 0) {
                    request.setAttribute("listaMaterial", listaMaterial);
                }
                              log.debug("url Material= " );
               try {
                    url = cargarSubpestanaMaterial(listaMaterial, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaMaterial", url);
                    }
                } catch (Exception ex) {
                }
            } catch (Exception ex) {

            }
        }
        return "/jsp/extension/melanbide41/listasxEspecialidad.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

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
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    private String cargarSubpestanaEspecialidades(List<EspecialidadesVO> listEspecialidades, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listEspecialidades.size() > 0) {
            try {
                //int ano = MeLanbide41Utils.getEjercicioDeExpediente(sol.getNumExp());
                //EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(ano, adapt);
                //request.setAttribute("ecaConfiguracion", config);
            } catch (Exception ex) {
            }

            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide41/especialidades.jsp";
    }

    private String cargarSubpestanaServicios(List<ServiciosVO> listServicios, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listServicios.size() > 0) {
            try {
                //int ano = MeLanbide41Utils.getEjercicioDeExpediente(sol.getNumExp());
                //EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(ano, adapt);
                //request.setAttribute("ecaConfiguracion", config);
            } catch (Exception ex) {
            }

            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide41/servicios.jsp";
    }

    private String cargarSubpestanaDisponibilidad(List<DisponibilidadVO> listDisponibilidad, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listDisponibilidad.size() > 0) {
            try {
                //int ano = MeLanbide41Utils.getEjercicioDeExpediente(sol.getNumExp());
                //EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(ano, adapt);
                //request.setAttribute("ecaConfiguracion", config);
            } catch (Exception ex) {
            }

            try {
                int codIdioma = 1;
                try {
                    String nombreModulo = MeLanbide41Utils.getNombreModulo();
                    request.setAttribute("nombreModulo", nombreModulo);
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide41/disponibilidad.jsp";
    }

    private String cargarSubpestanaCapacidad(List<CapacidadVO> listCapacidad, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listCapacidad.size() > 0) {
            try {
                //int ano = MeLanbide41Utils.getEjercicioDeExpediente(sol.getNumExp());
                //EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(ano, adapt);
                //request.setAttribute("ecaConfiguracion", config);
            } catch (Exception ex) {
            }

            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide41/capacidad.jsp";
    }

    private String cargarSubpestanaDotacion(List<DotacionVO> listaDotacion, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listaDotacion.size() > 0) {
            try {
                //int ano = MeLanbide41Utils.getEjercicioDeExpediente(sol.getNumExp());
                //EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(ano, adapt);
                //request.setAttribute("ecaConfiguracion", config);
            } catch (Exception ex) {
            }

            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        log.debug( "/jsp/extension/melanbide41/dotacion.jsp");
        return "/jsp/extension/melanbide41/dotacion.jsp";
    }

    private String cargarSubpestanaMaterial(List<MaterialVO> listaMaterial, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listaMaterial.size() > 0) {
            try {
                //int ano = MeLanbide41Utils.getEjercicioDeExpediente(sol.getNumExp());
                //EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(ano, adapt);
                //request.setAttribute("ecaConfiguracion", config);
            } catch (Exception ex) {
            }

            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide41/material.jsp";
    }

    private String cargarSubpestanaIdentificacionEsp(List<IdentificacionEspVO> listaIdentificacionEsp, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (listaIdentificacionEsp.size() > 0) {
            try {
                int codIdioma = 1;
                try {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if (usuario != null) {
                        codIdioma = usuario.getIdioma();
                    }
                } catch (Exception ex) {
                }
            } catch (Exception ex) {
            }
        }
        return "/jsp/extension/melanbide41/identificacionEsp.jsp";
    }

    public String cargarNuevaEspecialidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adapt.getConnection();
            request.setAttribute("nuevo", "1");
            //Cargamos los certificados para rellenar el combo.
            ArrayList<CerCertificadoVO> listaCertificados = new ArrayList<CerCertificadoVO>();
            try {
                MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
                listaCertificados = meLanbide41Manager.getCertificados(con);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Guardamos los certificados recuperados en la request
            request.setAttribute("listaCertificados", listaCertificados);

            List<EspecialidadesVO> listaEspSol = MeLanbide41Manager.getInstance().getDatosEspecialidades(numExpediente, con);
            if (listaEspSol != null) {
                request.setAttribute("listaEspSol", listaEspSol);
            }

            //lista motivos denegación completa
            List<ValorCampoDesplegableModuloIntegracionVO> listaMotDeng = MeLanbide41Manager.getInstance().getDatosMotivosDeneg(numExpediente, con);

            if (listaMotDeng != null) {
                request.setAttribute("listaMotivos", listaMotDeng);
            }
        } catch (Exception ex) {
        } finally {
            if (adapt != null && con != null) {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        return "/jsp/extension/melanbide41/operaciones/nuevaEspecialidad.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearEspecialidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspecialidadesVO> especialidades = new ArrayList<EspecialidadesVO>();
        EspecialidadesVO nuevaEsp = new EspecialidadesVO();
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adapt.getConnection();
            //Recojo los parametros
            String codigocp = (String) request.getParameter("codigocp");
            String denominacion = (String) request.getParameter("denominacion");
            String presencial = (String) request.getParameter("presencial");
            String teleformacion = (String) request.getParameter("teleformacion");
            String acreditacion = (String) request.getParameter("acreditacion");
            String motDeneg = (String) request.getParameter("motDeneg");

            nuevaEsp.setNumExp(numExpediente);
            nuevaEsp.setCodCP(codigocp);
            nuevaEsp.setDenominacion(denominacion);
            nuevaEsp.setMotDeneg(motDeneg.split(","));
            if (!presencial.isEmpty() && !presencial.equals("")) {
                nuevaEsp.setInscripcionPresencial(Integer.parseInt(presencial));
            }
            //else
            //    nuevaEsp.setInscripcionPresencial(Integer.parseInt(ConstantesDatos.ZERO));
            if (!teleformacion.isEmpty() && !teleformacion.equals("")) {
                nuevaEsp.setInscripcionTeleformacion(Integer.parseInt(teleformacion));
            }
            //else
            //    nuevaEsp.setInscripcionTeleformacion(Integer.parseInt(ConstantesDatos.ZERO));
            if (!acreditacion.isEmpty() && !acreditacion.equals("")) {
                nuevaEsp.setAcreditacion(Integer.parseInt(acreditacion));
            }
            //else
            //    nuevaEsp.setAcreditacion(Integer.parseInt(ConstantesDatos.ZERO)); 

            try {
                adapt.inicioTransaccion(con);
                //creo especialidad
                MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
                int idInsertado = meLanbide41Manager.crearNuevaEspecialidad(nuevaEsp, con);
                //actualizo líneas de disponibilidad e identificación de la especialidad dada de alta
                meLanbide41Manager.crearNuevaDisponibilidad(idInsertado, nuevaEsp.getNumExp(), nuevaEsp.getCodCP(), con);
                //creo motivos de denegación de la especialidad
                if (nuevaEsp.getMotDeneg() != null && nuevaEsp.getMotDeneg().length > 0) {
                    for (String idMotDeneg : nuevaEsp.getMotDeneg()) {
                        if (idMotDeneg != null && !"".equals(idMotDeneg)) {
                            meLanbide41Manager.crearNuevaEspecialidadMotDeneg(idInsertado, idMotDeneg, con);
                        }
                    }
                }
                adapt.finTransaccion(con);
            } catch (Exception ex) {
                codigoOperacion = "2";
                if (adapt != null && con != null) {
                    try {
                        adapt.rollBack(con);
                    } catch (BDException e) {
                        log.error("Error al realizar rollback en la operacion MELANBIDE41.crearEspecialidad: " + e.getMessage());
                    }
                }
            }

            //recupero de nuevo las especialidades actualizadas para mostrarlas
            try {
                especialidades = MeLanbide41Manager.getInstance().getDatosEspecialidades(numExpediente, con);
            } catch (BDException bde) {
                codigoOperacion = "1";
                java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
            } catch (Exception ex) {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (BDException ex) {

        } catch (NumberFormatException ex) {
        } catch (SQLException ex) {
        } finally {
            if (adapt != null && con != null) {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (EspecialidadesVO fila : especialidades) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ESP_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</ESP_NUM>");
            xmlSalida.append("<ESP_CODCP>");
            xmlSalida.append(fila.getCodCP());
            xmlSalida.append("</ESP_CODCP>");
            xmlSalida.append("<ESP_DENOM>");
            xmlSalida.append(fila.getDenominacion());
            xmlSalida.append("</ESP_DENOM>");
            xmlSalida.append("<ESP_PRESE>");
            xmlSalida.append(fila.getInscripcionPresencial() != null ? fila.getInscripcionPresencial().toString() : "");
            xmlSalida.append("</ESP_PRESE>");
            xmlSalida.append("<ESP_TELEF>");
            xmlSalida.append(fila.getInscripcionTeleformacion() != null ? fila.getInscripcionTeleformacion().toString() : "");
            xmlSalida.append("</ESP_TELEF>");
            xmlSalida.append("<ESP_ACRED>");
            xmlSalida.append(fila.getAcreditacion() != null ? fila.getAcreditacion().toString() : "");
            xmlSalida.append("</ESP_ACRED>");
            xmlSalida.append("<ESP_MOT_DENEG>");
            xmlSalida.append(fila.getMotDeneg());
            xmlSalida.append("</ESP_MOT_DENEG>");

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

    public String cargarModifEspecialidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adapt.getConnection();
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            //Cargamos los certificados para rellenar el combo.
            ArrayList<CerCertificadoVO> listaCertificados = new ArrayList<CerCertificadoVO>();
            try {
                MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
                listaCertificados = meLanbide41Manager.getCertificados(con);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Guardamos los certificados recuperados en la request
            request.setAttribute("listaCertificados", listaCertificados);

            if (id != null && !id.equals("")) {
                EspecialidadesVO datModif = MeLanbide41Manager.getInstance().getEspecialidadPorCodigo(numExpediente, id, con);
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            List<EspecialidadesVO> listaEspSol = MeLanbide41Manager.getInstance().getDatosEspecialidades(numExpediente, con);
            if (listaEspSol != null) {
                request.setAttribute("listaEspSol", listaEspSol);
            }

            //lista motivos denegación seleccionados para la especialidad a modificar
            List<String> listaMotDenegSelec = MeLanbide41Manager.getInstance().getMotDenegSelec(id, con);
            if (listaEspSol != null) {
//                request.setAttribute("codListaMotivos", new String[] {"1","2"});
                request.setAttribute("codListaMotivos", listaMotDenegSelec);
            }

            List<ValorCampoDesplegableModuloIntegracionVO> listaMotDeng = MeLanbide41Manager.getInstance().getDatosMotivosDeneg(numExpediente, con);
            if (listaMotDeng != null) {
                request.setAttribute("listaMotivos", listaMotDeng);
            }
        } catch (Exception ex) {
        } finally {
            if (adapt != null && con != null) {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        return "/jsp/extension/melanbide41/operaciones/nuevaEspecialidad.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarEspecialidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspecialidadesVO> especialidades = new ArrayList<EspecialidadesVO>();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            con = adapt.getConnection();
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String codigocp = (String) request.getParameter("codigocp");
            String denominacion = (String) request.getParameter("denominacion");
            String presencial = (String) request.getParameter("presencial");
            String teleformacion = (String) request.getParameter("teleformacion");
            String acreditacion = (String) request.getParameter("acreditacion");
            String motDeneg = (String) request.getParameter("motDeneg");

            EspecialidadesVO datModif = MeLanbide41Manager.getInstance().getEspecialidadPorCodigo(numExpediente, id, con);
            datModif.setId(Integer.parseInt(id));
            datModif.setCodCP(codigocp);
            datModif.setNumExp(numExpediente);
            datModif.setDenominacion(denominacion);
            datModif.setInscripcionPresencial((!presencial.equals("") ? Integer.parseInt(presencial) : null));
            datModif.setInscripcionTeleformacion((!teleformacion.equals("") ? Integer.parseInt(teleformacion) : null));
            datModif.setAcreditacion((!acreditacion.equals("") ? Integer.parseInt(acreditacion) : null));
            datModif.setMotDeneg(motDeneg.split(","));

            try {
                adapt.inicioTransaccion(con);
                MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
                meLanbide41Manager.modificarEspecialidad(datModif, con);
                meLanbide41Manager.eliminarEspecialidadMotDeneg(datModif.getId(), con);
                if (datModif.getMotDeneg() != null && datModif.getMotDeneg().length > 0) {
                    for (String idMotDeneg : datModif.getMotDeneg()) {
                        if (idMotDeneg != null && !"".equals(idMotDeneg)) {
                            meLanbide41Manager.crearNuevaEspecialidadMotDeneg(datModif.getId(), idMotDeneg, con);
                        }
                    }
                }

                adapt.finTransaccion(con);
            } catch (Exception ex) {
                codigoOperacion = "2";
                if (adapt != null && con != null) {
                    try {
                        adapt.rollBack(con);
                    } catch (BDException e) {
                        log.error("Error al realizar rollback en la operacion MELANBIDE41.crearEspecialidad: " + e.getMessage());
                    }
                }
            }
            try {
                especialidades = MeLanbide41Manager.getInstance().getDatosEspecialidades(numExpediente, con);
            } catch (BDException bde) {
                codigoOperacion = "1";
                java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
            } catch (Exception ex) {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
        } finally {
            if (adapt != null && con != null) {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (EspecialidadesVO fila : especialidades) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ESP_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</ESP_NUM>");
            xmlSalida.append("<ESP_CODCP>");
            xmlSalida.append(fila.getCodCP());
            xmlSalida.append("</ESP_CODCP>");
            xmlSalida.append("<ESP_DENOM>");
            xmlSalida.append(fila.getDenominacion());
            xmlSalida.append("</ESP_DENOM>");
            xmlSalida.append("<ESP_PRESE>");
            xmlSalida.append(fila.getInscripcionPresencial() != null ? fila.getInscripcionPresencial().toString() : "");
            xmlSalida.append("</ESP_PRESE>");
            xmlSalida.append("<ESP_TELEF>");
            xmlSalida.append(fila.getInscripcionTeleformacion() != null ? fila.getInscripcionTeleformacion().toString() : "");
            xmlSalida.append("</ESP_TELEF>");
            xmlSalida.append("<ESP_ACRED>");
            xmlSalida.append(fila.getAcreditacion() != null ? fila.getAcreditacion().toString() : "");
            xmlSalida.append("</ESP_ACRED>");
            xmlSalida.append("<ESP_MOT_DENEG>");
            xmlSalida.append(fila.getMotDeneg());
            xmlSalida.append("</ESP_MOT_DENEG>");

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

    public void eliminarEspecialidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspecialidadesVO> especialidades = new ArrayList<EspecialidadesVO>();
        try {
            String id = (String) request.getParameter("id");
            Integer idE = null;
            if (id == null || id.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idE = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idE != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
                    Connection con = null;
                    try {
                        con = adapt.getConnection();
                        adapt.inicioTransaccion(con);
                        meLanbide41Manager.eliminarEspecialidadMotDeneg(idE, con);
                        meLanbide41Manager.eliminarEspecialidad(numExpediente, idE, con);

                        codigoOperacion = "0";
                        especialidades = MeLanbide41Manager.getInstance().getDatosEspecialidades(numExpediente, con);

                        //Recogemos disponiblidad e identificacion de ese mismo id de especialidad para borrarlos
                        DisponibilidadVO disponibilidad = new DisponibilidadVO();
                        IdentificacionEspVO identificacion = new IdentificacionEspVO();
                        try {
                            disponibilidad = meLanbide41Manager.getDisponibilidadPorCodigoEspSol_NumExp(numExpediente, Integer.valueOf(idE), con);
                            identificacion = meLanbide41Manager.getIdentificacionEspPorCodigoEspSol_NumExp(numExpediente, Integer.valueOf(idE), con);
                            int eliminaDisponibilidad = meLanbide41Manager.eliminarDisponibilidadDesdeListEspSol(numExpediente, disponibilidad.getId(), idE, con);
                            if (eliminaDisponibilidad > 0) {
                                log.debug("Eliminada Correctamente Disponibilidad: " + disponibilidad.getId() + "  para IdEspecialidad " + idE + "Expediente : " + numExpediente);
                            }
                            int eliminaIdentificacion = meLanbide41Manager.eliminarIdentificacionEspDesdeListEspSol(numExpediente, identificacion.getId(), idE, con);
                            if (eliminaIdentificacion > 0) {
                                log.debug("Eliminada Correctamente Identificacion: " + identificacion.getId() + "  para IdEspecialidad " + idE + "Expediente : " + numExpediente);
                            }
                        } catch (Exception ex1) {
                            log.error("Error al Eliminar Disponibilidades o Identifaciones de Especialidades Solicitadas al BOrrar desde la lista de Especialidades: IdEspecialidad " + idE + "Expediente : " + numExpediente);
                            java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                        adapt.finTransaccion(con);
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
                        log.error("Error al eliminar especialidad: " + ex.getMessage());
                        codigoOperacion = "2";
                        if (adapt != null && con != null) {
                            try {
                                adapt.rollBack(con);
                            } catch (BDException e) {
                                log.error("Error al realizar rollback en la operacion MELANBIDE41.crearEspecialidad: " + e.getMessage());
                            }
                        }
                    } finally {
                        if (adapt != null && con != null) {
                            try {
                                adapt.devolverConexion(con);
                            } catch (BDException e) {
                                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                            }
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (SQLException ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (EspecialidadesVO fila : especialidades) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ESP_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</ESP_NUM>");
            xmlSalida.append("<ESP_CODCP>");
            xmlSalida.append(fila.getCodCP());
            xmlSalida.append("</ESP_CODCP>");
            xmlSalida.append("<ESP_DENOM>");
            xmlSalida.append(fila.getDenominacion());
            xmlSalida.append("</ESP_DENOM>");
            xmlSalida.append("<ESP_PRESE>");
            xmlSalida.append(fila.getInscripcionPresencial() != null ? fila.getInscripcionPresencial().toString() : "");
            xmlSalida.append("</ESP_PRESE>");
            xmlSalida.append("<ESP_TELEF>");
            xmlSalida.append(fila.getInscripcionTeleformacion() != null ? fila.getInscripcionTeleformacion().toString() : "");
            xmlSalida.append("</ESP_TELEF>");
            xmlSalida.append("<ESP_ACRED>");
            xmlSalida.append(fila.getAcreditacion() != null ? fila.getAcreditacion().toString() : "");
            xmlSalida.append("</ESP_ACRED>");
            xmlSalida.append("<ESP_MOT_DENEG>");
            xmlSalida.append(fila.getMotDeneg());
            xmlSalida.append("</ESP_MOT_DENEG>");

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

    public String cargarNuevoServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevoServicio.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ServiciosVO> lista = new ArrayList<ServiciosVO>();
        ServiciosVO nuevoDato = new ServiciosVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String descripcion = (String) request.getParameter("descripcion");
            String ubicacion = (String) request.getParameter("ubicacion");
            String superficie = (String) request.getParameter("superficie");
            superficie = superficie.replace(",", ".");

            nuevoDato.setNumExp(numExpediente);
            nuevoDato.setDescripcion(descripcion);
            nuevoDato.setUbicacion(ubicacion);
            if (!superficie.isEmpty() && !superficie.equals("")) {
                try {
                    BigDecimal tempSup = new BigDecimal(superficie);
                    nuevoDato.setSuperficie(tempSup);
                } catch (Exception ex) {
                    log.debug("Ha habido un erro al hacer el parseo a BigDecimal del campo Superficie " + superficie);
                    throw ex;
                }
            }

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
            boolean insertOK = meLanbide41Manager.crearNuevoServicio(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosServicios(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.debug("No se ha podido Insertar El Nuevo servicio para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (ServiciosVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<SER_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</SER_NUM>");
            xmlSalida.append("<SER_DESC>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</SER_DESC>");
            xmlSalida.append("<SER_UBIC>");
            xmlSalida.append(fila.getUbicacion());
            xmlSalida.append("</SER_UBIC>");
            xmlSalida.append("<SER_SUPE>");
            xmlSalida.append(fila.getSuperficie() != null ? fila.getSuperficie().toString() : "");
            xmlSalida.append("</SER_SUPE>");
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

    public String cargarModifServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            if (id != null && !id.equals("")) {
                ServiciosVO datModif = MeLanbide41Manager.getInstance().getServicioPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevoServicio.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ServiciosVO> lista = new ArrayList<ServiciosVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String descripcion = (String) request.getParameter("descripcion");
            String ubicacion = (String) request.getParameter("ubicacion");
            String superficie = (String) request.getParameter("superficie");
            superficie = superficie.replace(",", ".");

            ServiciosVO datModif = MeLanbide41Manager.getInstance().getServicioPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.parseInt(id));
            datModif.setNumExp(numExpediente);
            datModif.setDescripcion(descripcion);
            datModif.setUbicacion(ubicacion);
            datModif.setSuperficie((!superficie.equals("") ? new BigDecimal(superficie) : null));

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
            boolean modOK = meLanbide41Manager.modificarServicio(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosServicios(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (ServiciosVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<SER_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</SER_NUM>");
            xmlSalida.append("<SER_DESC>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</SER_DESC>");
            xmlSalida.append("<SER_UBIC>");
            xmlSalida.append(fila.getUbicacion());
            xmlSalida.append("</SER_UBIC>");
            xmlSalida.append("<SER_SUPE>");
            xmlSalida.append(fila.getSuperficie() != null ? fila.getSuperficie().toString().replace(".", ",") : "");
            xmlSalida.append("</SER_SUPE>");
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

    public void eliminarServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ServiciosVO> lista = new ArrayList<ServiciosVO>();
        try {
            String id = (String) request.getParameter("id");
            Integer idS = null;
            if (id == null || id.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idS = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idS != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide41Manager.getInstance().eliminarServicio(numExpediente, idS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide41Manager.getInstance().getDatosServicios(numExpediente, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (ServiciosVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<SER_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</SER_NUM>");
            xmlSalida.append("<SER_DESC>");
            xmlSalida.append(fila.getDescripcion());
            xmlSalida.append("</SER_DESC>");
            xmlSalida.append("<SER_UBIC>");
            xmlSalida.append(fila.getUbicacion());
            xmlSalida.append("</SER_UBIC>");
            xmlSalida.append("<SER_SUPE>");
            xmlSalida.append(fila.getSuperficie() != null ? fila.getSuperficie().toString() : "");
            xmlSalida.append("</SER_SUPE>");
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

    public String cargarModifDisponibilidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idespsol = request.getParameter("idespsol");
            if ((id != null && !id.equals("")) && (idespsol != null && !idespsol.equals(""))) {
                DisponibilidadVO datModif = MeLanbide41Manager.getInstance().getDisponibilidadPorCodigo(numExpediente, id, idespsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevaDisponibilidad.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarDisponibilidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DisponibilidadVO> lista = new ArrayList<DisponibilidadVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String idEspSol = (String) request.getParameter("idEspSol");
            String codcp = (String) request.getParameter("codcp");
            String propiedadce = (String) request.getParameter("propiedadce");
            String situados = (String) request.getParameter("situados");
            String supaulas = (String) request.getParameter("supaulas");
            supaulas = supaulas.replace(",", ".");
            String suptaller = (String) request.getParameter("suptaller");
            suptaller = suptaller.replace(",", ".");
            String supaulastaller = (String) request.getParameter("supaulastaller");
            supaulastaller = supaulastaller.replace(",", ".");
            String supcampoprac = (String) request.getParameter("supcampoprac");
            supcampoprac = supcampoprac.replace(",", ".");

            DisponibilidadVO datModif = MeLanbide41Manager.getInstance().getDisponibilidadPorCodigo(numExpediente, id, idEspSol, adapt);
            datModif.setId(Integer.valueOf(id));
            datModif.setIdEspSol(Integer.valueOf(idEspSol));
            datModif.setNumExp(numExpediente);
            datModif.setCodCp(codcp);
            datModif.setPropiedadCedidos(propiedadce);
            datModif.setSituados(situados);
            /*datModif.setSupAulas((!supaulas.equals("") ? new BigDecimal(supaulas) : null));
            datModif.setSupTaller((!suptaller.equals("") ? new BigDecimal(suptaller) : null));
            datModif.setSupAulaTaller((!supaulastaller.equals("") ? new BigDecimal(supaulastaller) : null));
            datModif.setSupCampoPract((!supcampoprac.equals("") ? new BigDecimal(supcampoprac) : null));*/

            datModif.setSupAulas(supaulas);
            datModif.setSupTaller(suptaller);
            datModif.setSupAulaTaller(supaulastaller);
            datModif.setSupCampoPract(supcampoprac);

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
            boolean modOK = meLanbide41Manager.modificarDisponibilidad(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosDisponibilidad(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (DisponibilidadVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol());
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<DRE_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</DRE_NUM>");
            xmlSalida.append("<DRE_CODCP>");
            xmlSalida.append(fila.getCodCp());
            xmlSalida.append("</DRE_CODCP>");
            xmlSalida.append("<DRE_PRCE>");
            xmlSalida.append(fila.getPropiedadCedidos());
            xmlSalida.append("</DRE_PRCE>");
            xmlSalida.append("<DRE_SIT>");
            xmlSalida.append(fila.getSituados());
            xmlSalida.append("</DRE_SIT>");
            xmlSalida.append("<DRE_AUL>");
            xmlSalida.append(fila.getSupAulas() != null ? fila.getSupAulas().replace(".", ",") : "");
            xmlSalida.append("</DRE_AUL>");
            xmlSalida.append("<DRE_TAL>");
            xmlSalida.append(fila.getSupTaller() != null ? fila.getSupTaller().replace(".", ",") : "");
            xmlSalida.append("</DRE_TAL>");
            xmlSalida.append("<DRE_AUTA>");
            xmlSalida.append(fila.getSupAulaTaller() != null ? fila.getSupAulaTaller().replace(".", ",") : "");
            xmlSalida.append("</DRE_AUTA>");
            xmlSalida.append("<DRE_CAPR>");
            xmlSalida.append(fila.getSupCampoPract() != null ? fila.getSupCampoPract().replace(".", ",") : "");
            xmlSalida.append("</DRE_CAPR>");
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

    public String cargarModifIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idespsol = request.getParameter("idEspSol");
            if ((id != null && !id.equals("")) && (idespsol != null && !idespsol.equals(""))) {
                IdentificacionEspVO datModif = MeLanbide41Manager.getInstance().getIdentificacionEspPorCodigo(numExpediente, id, idespsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevaIdentificacionEsp.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public String cargarNuevaIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response, EspecialidadesVO datoEsp) {
        try {
            String nuevo = "1";
            request.setAttribute("datoEspecialidad", datoEsp);
            IdentificacionEspVO identEspecialidad = new IdentificacionEspVO();
            List<IdentificacionEspVO> listIdenEsp = new ArrayList<IdentificacionEspVO>();
            listIdenEsp = MeLanbide41Manager.getInstance().getDatosIdentificacionEsp(numExpediente, datoEsp.getId(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listIdenEsp.size() == 1) {
                for (IdentificacionEspVO identificacionesp : listIdenEsp) {
                    identEspecialidad = identificacionesp;
                }
                nuevo = "0";
            } else if (listIdenEsp.size() > 1) {
                log.debug("SE han recuperado mas de un registro sobre detalles de la especialidad" + datoEsp.getNumExp() + " -- " + datoEsp.getId());
            } else {
                log.error("Se ha presentado un error al  recueperado datos sobre detalles de la especialidad" + datoEsp.getNumExp() + " -- " + datoEsp.getId());
            }

            request.setAttribute("nuevo", nuevo);
            request.setAttribute("datModif", identEspecialidad);

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevaIdentificacionEsp.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<IdentificacionEspVO> lista = new ArrayList<IdentificacionEspVO>();

        IdentificacionEspVO datModif = new IdentificacionEspVO();
        boolean modOK = false;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String nuevo = (String) request.getParameter("nuevo");
            String id = (String) request.getParameter("id");
            String idEspSol = (String) request.getParameter("idEspSol");
            String codcp = (String) request.getParameter("codcp");
            String denomesp = (String) request.getParameter("denomesp");
            String horas = (String) request.getParameter("horas");
            horas = horas.replace(",", ".");
            String alumnos = (String) request.getParameter("alumnos");
            String alumnosAut = (String) request.getParameter("alumnosAut");
            String certpro = (String) request.getParameter("certpro");
            String realdecregu = (String) request.getParameter("realdecregu");
            String boefecpub = (String) request.getParameter("boefecpub");
            String descripadapt = (String) request.getParameter("descripadapt");
            String observadapt = (String) request.getParameter("observadapt");

            if (nuevo.equals("1")) {

                datModif.setIdEspSol(Integer.parseInt(idEspSol));
                datModif.setNumExp(numExpediente);
                datModif.setCodCp(codcp);
                datModif.setDenomEsp(denomesp);
                datModif.setHoras((horas != null && !horas.equals("") ? new BigDecimal(horas) : null));
                datModif.setAlumnos((alumnos != null && !alumnos.equals("") ? new BigDecimal(alumnos) : null));
                datModif.setAlumnosAut((alumnosAut != null && !alumnosAut.equals("") ? new BigDecimal(alumnosAut) : null));
                datModif.setCertPro((certpro != null && !certpro.equals("") && !certpro.equals("null") ? new Integer(certpro) : null));
                datModif.setRealDecRegu(realdecregu);
                datModif.setBoeFecPub(boefecpub);
                datModif.setDescripAdapt(descripadapt);
                datModif.setObservAdapt(observadapt);

                MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
                modOK = meLanbide41Manager.crearNuevaIdentificacionEsp(datModif, adapt);
            } else if (nuevo.equals("0")) {
                datModif = MeLanbide41Manager.getInstance().getIdentificacionEspPorCodigo(numExpediente, id, idEspSol, adapt);
                datModif.setIdEspSol(Integer.parseInt(idEspSol));
                datModif.setNumExp(numExpediente);
                datModif.setCodCp(codcp);
                datModif.setDenomEsp(denomesp);
                datModif.setHoras((horas != null && !horas.equals("") ? new BigDecimal(horas) : null));
                datModif.setAlumnos((alumnos != null && !alumnos.equals("") ? new BigDecimal(alumnos) : null));
                datModif.setAlumnosAut((alumnosAut != null && !alumnosAut.equals("") ? new BigDecimal(alumnosAut) : null));
                datModif.setCertPro((certpro != null && !certpro.equals("") && !certpro.equals("null") ? new Integer(certpro) : null));
                datModif.setRealDecRegu(realdecregu);
                datModif.setBoeFecPub(boefecpub);
                datModif.setDescripAdapt(descripadapt);
                datModif.setObservAdapt(observadapt);

                MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
                modOK = meLanbide41Manager.modificarIdentificacionEsp(datModif, adapt);
            } else {
                codigoOperacion = "2";
                log.error("No se ha podido recuperar el codigo de NUEVO para gestionar alta o modificaicon de datos de identifiacion de especialidad");
            }

            if (modOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosIdentificacionEsp(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (IdentificacionEspVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol());
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<IDE_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</IDE_NUM>");
            xmlSalida.append("<IDE_CODESP>");
            xmlSalida.append(fila.getCodCp());
            xmlSalida.append("</IDE_CODESP>");
            xmlSalida.append("<IDE_DENESP>");
            xmlSalida.append(fila.getDenomEsp());
            xmlSalida.append("</IDE_DENESP>");
            xmlSalida.append("<IDE_HORAS>");
            xmlSalida.append(fila.getHoras() != null ? fila.getHoras().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_HORAS>");
            xmlSalida.append("<IDE_ALUM>");
            xmlSalida.append(fila.getAlumnos() != null ? fila.getAlumnos().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_ALUM>");
            xmlSalida.append("<IDE_ALUM_AUT>");
            xmlSalida.append(fila.getAlumnosAut() != null ? fila.getAlumnosAut().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_ALUM_AUT>");
            xmlSalida.append("<IDE_CERTP>");
            xmlSalida.append(fila.getCertPro() != null ? fila.getCertPro().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_CERTP>");
            xmlSalida.append("<IDE_RDER>");
            xmlSalida.append(fila.getRealDecRegu());
            xmlSalida.append("</IDE_RDER>");
            xmlSalida.append("<IDE_BOEFP>");
            xmlSalida.append(fila.getBoeFecPub());
            xmlSalida.append("</IDE_BOEFP>");
            xmlSalida.append("<IDE_DESADAP>");
            xmlSalida.append(fila.getDescripAdapt());
            xmlSalida.append("</IDE_DESADAP>");
            xmlSalida.append("<IDE_OBSADAP>");
            xmlSalida.append(fila.getObservAdapt());
            xmlSalida.append("</IDE_OBSADAP>");
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

    public void modificarIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<IdentificacionEspVO> lista = new ArrayList<IdentificacionEspVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String idEspSol = (String) request.getParameter("idEspSol");
            String codcp = (String) request.getParameter("codcp");
            String denomesp = (String) request.getParameter("denomesp");
            String horas = (String) request.getParameter("horas");
            horas = horas.replace(",", ".");
            String alumnos = (String) request.getParameter("alumnos");
            String certpro = (String) request.getParameter("certpro");
            String realdecregu = (String) request.getParameter("realdecregu");
            String boefecpub = (String) request.getParameter("boefecpub");

            IdentificacionEspVO datModif = MeLanbide41Manager.getInstance().getIdentificacionEspPorCodigo(numExpediente, id, idEspSol, adapt);
            datModif.setId(Integer.parseInt(id));
            datModif.setIdEspSol(Integer.parseInt(idEspSol));
            datModif.setNumExp(numExpediente);
            datModif.setCodCp(codcp);
            datModif.setDenomEsp(denomesp);
            datModif.setHoras((horas != null && !horas.equals("") ? new BigDecimal(horas) : null));
            datModif.setAlumnos((alumnos != null && !alumnos.equals("") ? new BigDecimal(alumnos) : null));
            datModif.setCertPro((certpro != null && !certpro.equals("") && !certpro.equals("null") ? new Integer(certpro) : null));
            datModif.setRealDecRegu(realdecregu);
            datModif.setBoeFecPub(boefecpub);

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
            boolean modOK = meLanbide41Manager.modificarIdentificacionEsp(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosIdentificacionEsp(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (IdentificacionEspVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol());
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<IDE_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</IDE_NUM>");
            xmlSalida.append("<IDE_CODESP>");
            xmlSalida.append(fila.getCodCp());
            xmlSalida.append("</IDE_CODESP>");
            xmlSalida.append("<IDE_DENESP>");
            xmlSalida.append(fila.getDenomEsp());
            xmlSalida.append("</IDE_DENESP>");
            xmlSalida.append("<IDE_HORAS>");
            xmlSalida.append(fila.getHoras() != null ? fila.getHoras().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_HORAS>");
            xmlSalida.append("<IDE_ALUM>");
            xmlSalida.append(fila.getAlumnos() != null ? fila.getAlumnos().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_ALUM>");
            xmlSalida.append("<IDE_CERTP>");
            xmlSalida.append(fila.getCertPro() != null ? fila.getCertPro().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_CERTP>");
            xmlSalida.append("<IDE_RDER>");
            xmlSalida.append(fila.getRealDecRegu());
            xmlSalida.append("</IDE_RDER>");
            xmlSalida.append("<IDE_BOEFP>");
            xmlSalida.append(fila.getBoeFecPub());
            xmlSalida.append("</IDE_BOEFP>");
            xmlSalida.append("<IDE_DESADAP>");
            xmlSalida.append(fila.getDescripAdapt());
            xmlSalida.append("</IDE_DESADAP>");
            xmlSalida.append("<IDE_OBSADAP>");
            xmlSalida.append(fila.getObservAdapt());
            xmlSalida.append("</IDE_OBSADAP>");
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

    public void actualizarDatosPantallaIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<IdentificacionEspVO> lista = new ArrayList<IdentificacionEspVO>();

        String idEpsol = (String) request.getParameter("idEpsol");
        Integer idEpsolint = (idEpsol != null && !idEpsol.isEmpty() ? Integer.valueOf(idEpsol) : 0);
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide41Manager.getInstance().getDatosIdentificacionEsp(numExpediente, idEpsolint, adapt);

        } catch (Exception ex) {
            log.error("Error al recuperar los datos de las Identifiacion  al Actualizar los datos de la pantalla despues de MOdificar - eliminar - Alta en lista especialidades solicitadas");
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (IdentificacionEspVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol());
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<IDE_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</IDE_NUM>");
            xmlSalida.append("<IDE_CODESP>");
            xmlSalida.append(fila.getCodCp());
            xmlSalida.append("</IDE_CODESP>");
            xmlSalida.append("<IDE_DENESP>");
            xmlSalida.append(fila.getDenomEsp());
            xmlSalida.append("</IDE_DENESP>");
            xmlSalida.append("<IDE_HORAS>");
            xmlSalida.append(fila.getHoras() != null ? fila.getHoras().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_HORAS>");
            xmlSalida.append("<IDE_ALUM>");
            xmlSalida.append(fila.getAlumnos() != null ? fila.getAlumnos().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_ALUM>");
            xmlSalida.append("<IDE_CERTP>");
            xmlSalida.append(fila.getCertPro() != null ? fila.getCertPro().toString().replace(".", ",") : "");
            xmlSalida.append("</IDE_CERTP>");
            xmlSalida.append("<IDE_RDER>");
            xmlSalida.append(fila.getRealDecRegu());
            xmlSalida.append("</IDE_RDER>");
            xmlSalida.append("<IDE_BOEFP>");
            xmlSalida.append(fila.getBoeFecPub());
            xmlSalida.append("</IDE_BOEFP>");
            xmlSalida.append("<IDE_DESADAP>");
            xmlSalida.append(fila.getDescripAdapt());
            xmlSalida.append("</IDE_DESADAP>");
            xmlSalida.append("<IDE_OBSADAP>");
            xmlSalida.append(fila.getObservAdapt());
            xmlSalida.append("</IDE_OBSADAP>");
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

    public void actualizarDatosPantallaDisponibilidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DisponibilidadVO> lista = new ArrayList<DisponibilidadVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide41Manager.getInstance().getDatosDisponibilidad(numExpediente, adapt);

        } catch (Exception ex) {
            log.debug("Error al recuperar los datos de las disponibildades al Actualizar los datos de l pantalla despues de MOdificar - eliminar - Alta en lista especialidades solicitadas");
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (DisponibilidadVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol());
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<DRE_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</DRE_NUM>");
            xmlSalida.append("<DRE_CODCP>");
            xmlSalida.append(fila.getCodCp());
            xmlSalida.append("</DRE_CODCP>");
            xmlSalida.append("<DRE_PRCE>");
            xmlSalida.append(fila.getPropiedadCedidos());
            xmlSalida.append("</DRE_PRCE>");
            xmlSalida.append("<DRE_SIT>");
            xmlSalida.append(fila.getSituados());
            xmlSalida.append("</DRE_SIT>");
            xmlSalida.append("<DRE_AUL>");
            xmlSalida.append(fila.getSupAulas() != null ? fila.getSupAulas().replace(".", ",") : "");
            xmlSalida.append("</DRE_AUL>");
            xmlSalida.append("<DRE_TAL>");
            xmlSalida.append(fila.getSupTaller() != null ? fila.getSupTaller().replace(".", ",") : "");
            xmlSalida.append("</DRE_TAL>");
            xmlSalida.append("<DRE_AUTA>");
            xmlSalida.append(fila.getSupAulaTaller() != null ? fila.getSupAulaTaller().replace(".", ",") : "");
            xmlSalida.append("</DRE_AUTA>");
            xmlSalida.append("<DRE_CAPR>");
            xmlSalida.append(fila.getSupCampoPract() != null ? fila.getSupCampoPract().replace(".", ",") : "");
            xmlSalida.append("</DRE_CAPR>");
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

    public String cargarNuevaCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");
            String idEpsol = (String) request.getParameter("idEpsol");
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide41Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevaCapacidad.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearCapacidad de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<CapacidadVO> lista = new ArrayList<CapacidadVO>();
        CapacidadVO nuevoDato = new CapacidadVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String idEpsol = (String) request.getParameter("idEpsol");
            String identificacionespfor = (String) request.getParameter("identificacionespfor");
            String ubicacion = (String) request.getParameter("ubicacion");
            String superficie = (String) request.getParameter("superficie");
            superficie = superficie.replace(",", ".");


            if (!idEpsol.isEmpty() && !idEpsol.equals("")) {
                nuevoDato.setIdEspSol(Integer.valueOf(idEpsol));
            } else {
                nuevoDato.setIdEspSol(null);
            }
            nuevoDato.setNumExp(numExpediente);
            nuevoDato.setIdetificacionEspFor(identificacionespfor);
            nuevoDato.setUbicacion(ubicacion);
            if (!superficie.isEmpty() && !superficie.equals("")) {
                try {
                    //BigDecimal tempSup = new BigDecimal(superficie);
                    //nuevoDato.setSuperficie(tempSup);
                    nuevoDato.setSuperficie(superficie);
                } catch (Exception ex) {
                    log.error("Ha habido un erro al hacer el parseo a BigDecimal del campo Superficie  de Capacidad de Instalaciones" + superficie);
                    throw ex;
                }
            }

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
          log.debug("antes de crearNuevaCapacidad manager " + this.getClass().getSimpleName());
           boolean insertOK = meLanbide41Manager.crearNuevaCapacidad(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosCapacidad(numExpediente, nuevoDato.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.error("No se ha podido Insertar Nueva capcidad de instalaciones para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }
          log.debug("antes de xml " + this.getClass().getSimpleName());

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (CapacidadVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<CAIN_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</CAIN_NUM>");
            xmlSalida.append("<CAIN_IDEF>");
            xmlSalida.append(fila.getIdetificacionEspFor());
            xmlSalida.append("</CAIN_IDEF>");
            xmlSalida.append("<CAIN_UBI>");
            xmlSalida.append(fila.getUbicacion());
            xmlSalida.append("</CAIN_UBI>");
            xmlSalida.append("<CAIN_SUP>");
            xmlSalida.append(fila.getSuperficie() != null ? fila.getSuperficie() : "");
            xmlSalida.append("</CAIN_SUP>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug(xmlSalida);
        try {
         log.info("envio xml " + this.getClass().getSimpleName());
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
        }//try-catch

    }

    public String cargarModifCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");

            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide41Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

            if (id != null && !id.equals("")) {
                CapacidadVO datModif = MeLanbide41Manager.getInstance().getCapacidadPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevaCapacidad.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("modificarCapacidad BEGIN");
        String codigoOperacion = "0";
        List<CapacidadVO> lista = new ArrayList<CapacidadVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String idEpsol = (String) request.getParameter("idEpsol");
            String identificacionespfor = (String) request.getParameter("identificacionespfor");
            String ubicacion = (String) request.getParameter("ubicacion");
            String superficie = (String) request.getParameter("superficie");
            //superficie = superficie.replace(",", ".");
            log.debug("superficie:" + superficie);

            CapacidadVO datModif = MeLanbide41Manager.getInstance().getCapacidadPorCodigo(numExpediente, id, adapt);
            log.debug("datModif:" + datModif.getId());
            datModif.setId(Integer.parseInt(id));
            datModif.setNumExp(numExpediente);
            datModif.setIdetificacionEspFor(identificacionespfor);
            datModif.setUbicacion(ubicacion);
            //datModif.setSuperficie((!superficie.equals("") ? new BigDecimal(superficie) : null));
            //datModif.setSuperficie((!superficie.equals("") ? superficie : null));
            datModif.setSuperficie(superficie);

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
            boolean modOK = meLanbide41Manager.modificarCapacidad(datModif, adapt);
            log.debug("modOK:" + modOK);
            if (modOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosCapacidad(numExpediente, datModif.getIdEspSol(), adapt);

                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (CapacidadVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<CAIN_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</CAIN_NUM>");
            xmlSalida.append("<CAIN_IDEF>");
            xmlSalida.append(fila.getIdetificacionEspFor());
            xmlSalida.append("</CAIN_IDEF>");
            xmlSalida.append("<CAIN_UBI>");
            xmlSalida.append(fila.getUbicacion());
            xmlSalida.append("</CAIN_UBI>");
            xmlSalida.append("<CAIN_SUP>");
            xmlSalida.append(fila.getSuperficie());
            xmlSalida.append("</CAIN_SUP>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("xmlSalida:" + xmlSalida);
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
        }//try-catch
        log.info("modificarCapacidad END");
    }

    public void eliminarCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<CapacidadVO> lista = new ArrayList<CapacidadVO>();
        try {
            String id = (String) request.getParameter("id");
            String idEpsol = (String) request.getParameter("idEpsol");
            Integer idEpsolint = (idEpsol != null && !idEpsol.isEmpty() ? Integer.valueOf(idEpsol) : 0);
            Integer idS = null;
            if (id == null || id.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idS = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idS != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide41Manager.getInstance().eliminarCapacidad(numExpediente, idS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide41Manager.getInstance().getDatosCapacidad(numExpediente, idEpsolint, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (CapacidadVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<CAIN_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</CAIN_NUM>");
            xmlSalida.append("<CAIN_IDEF>");
            xmlSalida.append(fila.getIdetificacionEspFor());
            xmlSalida.append("</CAIN_IDEF>");
            xmlSalida.append("<CAIN_UBI>");
            xmlSalida.append(fila.getUbicacion());
            xmlSalida.append("</CAIN_UBI>");
            xmlSalida.append("<CAIN_SUP>");
            xmlSalida.append(fila.getSuperficie() != null ? fila.getSuperficie() : "");
            xmlSalida.append("</CAIN_SUP>");
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

    public String cargarNuevaDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");
            String idEpsol = (String) request.getParameter("idEpsol");
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide41Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevaDotacion.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DotacionVO> lista = new ArrayList<DotacionVO>();
        DotacionVO nuevoDato = new DotacionVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String idEpsol = (String) request.getParameter("idEpsol");
            String cantidad = (String) request.getParameter("cantidad");
            String denominacionET = (String) request.getParameter("denominacionET");
            String meLanbide41FechaAdqui = (String) request.getParameter("meLanbide41FechaAdqui");

            if (!idEpsol.isEmpty() && !idEpsol.equals("")) {
                nuevoDato.setIdEspSol(Integer.valueOf(idEpsol));
            } else {
                nuevoDato.setIdEspSol(null);
            }
            nuevoDato.setNumExp(numExpediente);
            //nuevoDato.setCantidad(Integer.valueOf(cantidad));
            nuevoDato.setCantidad(cantidad);
            nuevoDato.setDenominacionET(denominacionET);

            /*SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            nuevoDato.setFechaAdq(new java.sql.Date(formatoFecha.parse(meLanbide41FechaAdqui).getTime()));*/
            nuevoDato.setFechaAdq(meLanbide41FechaAdqui);

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
            boolean insertOK = meLanbide41Manager.crearNuevaDotacion(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosDotacion(numExpediente, nuevoDato.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.error("No se ha podido Insertar Nueva Dotacion  para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (DotacionVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<DOT_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</DOT_NUM>");
            xmlSalida.append("<DOT_CANT>");
            xmlSalida.append(fila.getCantidad() != null ? fila.getCantidad() : "");
            xmlSalida.append("</DOT_CANT>");
            xmlSalida.append("<DOT_DET>");
            xmlSalida.append(fila.getDenominacionET());
            xmlSalida.append("</DOT_DET>");
            xmlSalida.append("<DOT_FAD>");
            xmlSalida.append(fila.getFechaAdq());
            xmlSalida.append("</DOT_FAD>");
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

    public String cargarModifDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");

            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide41Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

            if (id != null && !id.equals("")) {
                DotacionVO datModif = MeLanbide41Manager.getInstance().getDotacionPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevaDotacion.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DotacionVO> lista = new ArrayList<DotacionVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String idEpsol = (String) request.getParameter("idEpsol");
            String cantidad = (String) request.getParameter("cantidad");
            String denominacionET = (String) request.getParameter("denominacionET");
            String meLanbide41FechaAdqui = (String) request.getParameter("meLanbide41FechaAdqui");

            DotacionVO datModif = MeLanbide41Manager.getInstance().getDotacionPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.valueOf(id));
            datModif.setNumExp(numExpediente);
            //datModif.setCantidad(Integer.valueOf(cantidad));
            datModif.setCantidad(cantidad);
            datModif.setDenominacionET(denominacionET);
            /*SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            datModif.setFechaAdq(new java.sql.Date(formatoFecha.parse(meLanbide41FechaAdqui).getTime()));*/
            datModif.setFechaAdq(meLanbide41FechaAdqui);

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
            boolean modOK = meLanbide41Manager.modificarDotacion(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosDotacion(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (DotacionVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<DOT_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</DOT_NUM>");
            xmlSalida.append("<DOT_CANT>");
            xmlSalida.append(fila.getCantidad() != null ? fila.getCantidad() : "");
            xmlSalida.append("</DOT_CANT>");
            xmlSalida.append("<DOT_DET>");
            xmlSalida.append(fila.getDenominacionET());
            xmlSalida.append("</DOT_DET>");
            xmlSalida.append("<DOT_FAD>");
            xmlSalida.append(fila.getFechaAdq());
            xmlSalida.append("</DOT_FAD>");
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

    public void eliminarDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DotacionVO> lista = new ArrayList<DotacionVO>();
        try {
            String id = (String) request.getParameter("id");
            String idEpsol = (String) request.getParameter("idEpsol");
            Integer idEpsolint = (idEpsol != null && !idEpsol.isEmpty() ? Integer.valueOf(idEpsol) : 0);
            Integer idS = null;
            if (id == null || id.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idS = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idS != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide41Manager.getInstance().eliminarDotacion(numExpediente, idS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide41Manager.getInstance().getDatosDotacion(numExpediente, idEpsolint, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (DotacionVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<DOT_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</DOT_NUM>");
            xmlSalida.append("<DOT_CANT>");
            xmlSalida.append(fila.getCantidad() != null ? fila.getCantidad() : "");
            xmlSalida.append("</DOT_CANT>");
            xmlSalida.append("<DOT_DET>");
            xmlSalida.append(fila.getDenominacionET());
            xmlSalida.append("</DOT_DET>");
            xmlSalida.append("<DOT_FAD>");
            xmlSalida.append(fila.getFechaAdq());
            xmlSalida.append("</DOT_FAD>");
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

    public String cargarNuevoMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");
            String idEpsol = (String) request.getParameter("idEpsol");
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide41Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevoMaterial.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MaterialVO> lista = new ArrayList<MaterialVO>();
        MaterialVO nuevoDato = new MaterialVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String idEpsol = (String) request.getParameter("idEpsol");
            String cantidad = (String) request.getParameter("cantidad");
            String denominacionET = (String) request.getParameter("denominacionET");

            if (!idEpsol.isEmpty() && !idEpsol.equals("")) {
                nuevoDato.setIdEspSol(Integer.valueOf(idEpsol));
            } else {
                nuevoDato.setIdEspSol(null);
            }
            nuevoDato.setNumExp(numExpediente);
            nuevoDato.setCantidad(Integer.valueOf(cantidad));
            nuevoDato.setDenominacionET(denominacionET);

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
            boolean insertOK = meLanbide41Manager.crearNuevoMaterial(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosMaterial(numExpediente, nuevoDato.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.debug("No se ha podido Insertar Nuevo Material   para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (MaterialVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<MAC_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</MAC_NUM>");
            xmlSalida.append("<MAC_CANT>");
            xmlSalida.append(fila.getCantidad() != null ? fila.getCantidad().toString() : "");
            xmlSalida.append("</MAC_CANT>");
            xmlSalida.append("<MAC_DET>");
            xmlSalida.append(fila.getDenominacionET());
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

    public String cargarModifMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");

            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide41Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

            if (id != null && !id.equals("")) {
                MaterialVO datModif = MeLanbide41Manager.getInstance().getMaterialPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide41/operaciones/nuevoMaterial.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MaterialVO> lista = new ArrayList<MaterialVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            String idEpsol = (String) request.getParameter("idEpsol");
            String cantidad = (String) request.getParameter("cantidad");
            String denominacionET = (String) request.getParameter("denominacionET");

            MaterialVO datModif = MeLanbide41Manager.getInstance().getMaterialPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.valueOf(id));
            datModif.setNumExp(numExpediente);
            datModif.setCantidad(Integer.valueOf(cantidad));
            datModif.setDenominacionET(denominacionET);

            MeLanbide41Manager meLanbide41Manager = MeLanbide41Manager.getInstance();
            boolean modOK = meLanbide41Manager.modificarMaterial(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide41Manager.getInstance().getDatosMaterial(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (MaterialVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<MAC_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</MAC_NUM>");
            xmlSalida.append("<MAC_CANT>");
            xmlSalida.append(fila.getCantidad() != null ? fila.getCantidad().toString() : "");
            xmlSalida.append("</MAC_CANT>");
            xmlSalida.append("<MAC_DET>");
            xmlSalida.append(fila.getDenominacionET());
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

    public void eliminarMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MaterialVO> lista = new ArrayList<MaterialVO>();
        try {
            String id = (String) request.getParameter("id");
            String idEpsol = (String) request.getParameter("idEpsol");
            Integer idEpsolint = (idEpsol != null && !idEpsol.isEmpty() ? Integer.valueOf(idEpsol) : 0);
            Integer idS = null;
            if (id == null || id.equals("")) {
                codigoOperacion = "3";
            } else {
                try {
                    idS = Integer.parseInt(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idS != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide41Manager.getInstance().eliminarMaterial(numExpediente, idS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide41Manager.getInstance().getDatosMaterial(numExpediente, idEpsolint, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE41.class.getName()).log(Level.SEVERE, null, ex);
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
        for (MaterialVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<MAC_NUM>");
            xmlSalida.append(fila.getNumExp());
            xmlSalida.append("</MAC_NUM>");
            xmlSalida.append("<MAC_CANT>");
            xmlSalida.append(fila.getCantidad() != null ? fila.getCantidad().toString() : "");
            xmlSalida.append("</MAC_CANT>");
            xmlSalida.append("<MAC_DET>");
            xmlSalida.append(fila.getDenominacionET());
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


    /**
     * Operación que da de alta un centro con especialidades a través del servicio web de centros de formación
     * de Lanbide
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @return String que puede tomar los siguientes valores:
     * 
     *         "0" --> Si la operación se ha ejecutado correctamente
     *         "1" --> Error al obtener las propiedades de configuración 
     *         "2" --> Revisar las propiedades de configuración con los valores de los campos suplementarios y código de trámite
     *         "3" --> No se ha podido establecer conexión con el servicio web de centros de formación 
     *         "4" --> El campo suplementario de expediente con el código de centro no tiene valor
     *         "5" --> El campo suplementario de expediente con el código de ubicación no tiene valor
     *         "6" --> No se ha podido obtener el valor del campo "Fecha de resolución" en ninguno de los trámites en los que se encuentra
     *         "7" --> No se ha podido obtener una conexión a la BBDD
     *         "8" --> El campo suplementario tipo centro no tiene valor
     *         "9" --> No se puede obtener el código correspondiente al tipo de centro de base de datos
     *        "10" --> No se ha podido dar de alta el centro con la especialidades en el servicio web de centros de formación 
     *        "11" --> Se ha producido un error técnico durante la ejecución de la operación
     *        "12" --> El servicio web no ha devuelto el número de censo del registro vasco
     *        "13" --> No se ha podido grabar el número de registro en el campo suplementario correspondiente
     *        "14" --> No se ha obtenido ninguna especialidad
     *        "15" --> Especialidades sin alumnos          
     *        "16" --> Error al obtener corrServicio o corrSubservicio
     *        "17" --> Error al obtener corrServicio
     *        "18" --> Error al obtener corrSubservicio
     */
    public String altaCentroConEspecialidades(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        String salida = "-1";
        log.info(this.getClass().getName() + ".altaCentroConEspecialidades  =================>");
        boolean conexionWS = false;
        boolean propiedadesRecuperadas = false;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        WSRegistroCentrosFormacionPortBindingStub binding = null;
        String codigoCampoCodCentro = null;
        String codigoCampoCodUbicacion = null;
        String codigoCampoTipoCentro = null;
        String codigoCampoFechaResolucion = null;
        String error = "";
        //String codigoTramiteResolucionPositivaMixta = null;
        //String codigoTramiteResolucionNegativa = null;
        String urlServicioWeb = null;
        String codigoTramiteConsultaSeleccionCentro = null;
        String codigoTramiteInscripcionCentroEnRegistro = null;
        String codigoCampoNumeroRegistroVasco = null;

        try {

            codigoCampoCodCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO", this.getNombreModulo());
            codigoCampoCodUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION", this.getNombreModulo());
            codigoCampoFechaResolucion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TRAMITE_FECHA_RESOLUCION", this.getNombreModulo());
            //codigoTramiteResolucionPositivaMixta = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_POSITIVA_MIXTA",this.getNombreModulo());
            //codigoTramiteResolucionNegativa      = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_DESISTIMIENTO_NEGATIVA",this.getNombreModulo());
            codigoCampoTipoCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_TIPOCENTRO", this.getNombreModulo());
            urlServicioWeb = ConfigurationParameter.getParameter("URL_WS_REGISTROCENTROS", this.getNombreModulo());
            //urlServicioWeb = "http://10.168.212.161:11003/ikaslanWS/WSRegistroCentrosFormacion?wsdl";

            // Se recupera el código del trámite de consulta y selección de centro del que se lee el código de centro y código de ubicación
            codigoTramiteConsultaSeleccionCentro = ConfigurationParameter.getParameter("CODIGO_TRAMITE_CONSULTA_SELECCION_CENTRO", this.getNombreModulo());
            // Se recupera el código del trámite "INSCRIPCION O ACREDITACION DEL CENTRO O ENTIDAD EN EL REGISTRO" del que se lee la fecha de resolución           
            codigoTramiteInscripcionCentroEnRegistro = ConfigurationParameter.getParameter("CODIGO_TRAMITE_ACREDITACION_CENTRO_ENTIDAD_REGISTRO", this.getNombreModulo());

            // Se recupera el código del campo en el que se grabará el número de registro devuelto por la operación altaCentroConEspecialidades
            codigoCampoNumeroRegistroVasco = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMREGISTRO_VASCO", this.getNombreModulo());

            log.info("codigoCampoNumeroRegistroVasco: " + codigoCampoNumeroRegistroVasco);
            log.info("codigoCampoCodCentro: " + codigoCampoCodCentro);
            log.info("codigoCampoCodUbicacion: " + codigoCampoCodUbicacion);
            log.info("codigoCampoFechaResolucion: " + codigoCampoFechaResolucion);
            log.info("codigoCampoTipoCentro: " + codigoCampoTipoCentro);
            log.info("codigoTramiteConsultaSeleccionCentro: " + codigoTramiteConsultaSeleccionCentro);
            log.info("codigoTramiteInscripcionCentroEnRegistro: " + codigoTramiteInscripcionCentroEnRegistro);
            log.info("urlServicioWeb: " + urlServicioWeb);

            propiedadesRecuperadas = true;
        } catch (Exception e) {
            log.error(this.getClass().getName() + ".altaCentroConEspecialidades():  Error al recuperar valores de propiedades del fichero de configuración: " + e.getMessage());
            salida = "1";
        }

        if (propiedadesRecuperadas) {
            if (!MeLanbide41Utils.isInteger(codigoTramiteConsultaSeleccionCentro)
                    || !MeLanbide41Utils.isInteger(codigoTramiteInscripcionCentroEnRegistro)
                    || codigoCampoCodCentro == null || codigoCampoCodUbicacion == null || codigoCampoFechaResolucion == null
                    || codigoCampoTipoCentro == null || codigoTramiteConsultaSeleccionCentro == null || codigoTramiteInscripcionCentroEnRegistro == null || codigoCampoNumeroRegistroVasco == null) {
                salida = "2";
                error = "Ha habido un error al recuperar las propiedades";
            } else {
                log.debug(" =============> Antes de obtener referencia al servicio web");
                try {
                    binding = (WSRegistroCentrosFormacionPortBindingStub) new WSRegistroCentrosFormacionServiceLocator().getWSRegistroCentrosFormacionPort(new java.net.URL(urlServicioWeb));
                    conexionWS = true;
                } catch (Exception e) {
                    log.error(this.getClass().getName() + ".altaCentroConEspecialidades(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                }

                if (!conexionWS) {
                    salida = "3";
                    error = "Ha habido un error en la conexión con el servicio web";
                } else {
                    /**
                     * ********* LLAMADA AL SERVICIO WEB **********
                     */
                    try {
                        net.lanbide.formacion.ws.regexlan.AltaCentroConEspecialidades centroConEspecialidades = new AltaCentroConEspecialidades();
                        net.lanbide.formacion.ws.regexlan.AltaCentroConEspecialidadesRequest centroRequest = new AltaCentroConEspecialidadesRequest();

                        try {
                            adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                            con   = adapt.getConnection();                        
                        }catch(BDException e){
                            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                        } catch (SQLException e) {
                            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                        }

                        if (con == null) {
                            // No se ha podido obtener una conexión a la BBDD
                            salida = "7";
                            error = "Ha habido un error en la conexión";
                        } else {
                            String[] datosExp = numExpediente.split("/");
                            String ejercicio = datosExp[0];
                            String codProcedimiento = datosExp[1];

                            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
                            // Se recupera la ocurrencia más reciente del trámite de "CONSULTA Y SELECCIÓN DE CENTRO"
                            int ocurrenciaRecienteConsulta = MeLanbide41DAO.getInstance().getOcurrenciaMasRecienteTramite(Integer.parseInt(codigoTramiteConsultaSeleccionCentro), numExpediente, Integer.toString(codOrganizacion), con);
                            // SE RECUPERA EL VALOR DEL CAMPO "CODCENTRO" DEFINIDO A NIVEL DE TRÁMITE                       
                            SalidaIntegracionVO salidaCampoCodCentro = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteConsultaSeleccionCentro), ocurrenciaRecienteConsulta, codigoCampoCodCentro, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                            // SE RECUPERA EL VALOR DEL CAMPO "CODUBICACION" DEFINIDO A NIVEL DE TRÁMITE                        
                            SalidaIntegracionVO salidaCampoCodUbicacion = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteConsultaSeleccionCentro), ocurrenciaRecienteConsulta, codigoCampoCodUbicacion, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                            // Se recupera la ocurrencia más reciente del trámite de "INSCRIPCION O ACREDITACIÓN DEL CENTRO O ENTIDAD EN EL REGISTRO"
                            int ocurrencia2 = MeLanbide41DAO.getInstance().getOcurrenciaMasRecienteTramite(Integer.parseInt(codigoTramiteInscripcionCentroEnRegistro), numExpediente, Integer.toString(codOrganizacion), con);
                            // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION DESISTIMIENTO NEGATIVA"
                            SalidaIntegracionVO salidaFecResolucionTramite = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteInscripcionCentroEnRegistro), ocurrencia2, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                            log.info("salidaCampoCodCentro.getStatus(): " + salidaCampoCodCentro.getStatus());
                            log.info("salidaCampoCodUbicacion.getStatus(): " + salidaCampoCodUbicacion.getStatus());
                            log.info("salidaFecResolucionTramiteResolucionNegativa.getStatus(): " + salidaFecResolucionTramite.getStatus());

                            if (salidaCampoCodCentro.getStatus() != 0) {
                                salida = "4";
                            } else if (salidaCampoCodUbicacion.getStatus() != 0) {
                                salida = "5";
                            } else if (salidaFecResolucionTramite.getStatus() != 0) {
                                salida = "6";
                            } else {

                                String valorTipoCentroTramite = MeLanbide41DAO.getInstance().getValorCampoDesplegableExternoTramite(codOrganizacion, numExpediente, Integer.parseInt(codigoTramiteConsultaSeleccionCentro), ocurrenciaRecienteConsulta, codigoCampoTipoCentro, con);
                                if (valorTipoCentroTramite == null || valorTipoCentroTramite.length() == 0) {
                                    salida = "8";
                                } else {
                                    String codTipoCentro = MeLanbide41DAO.getInstance().getCodigoValorTipoCentro(valorTipoCentroTramite, con);
                                    if (codTipoCentro == null || codTipoCentro.length() == 0) {
                                        salida = "9";
                                    } else {
                                        log.debug("Cod. Centro: " + salidaCampoCodCentro.getCampoSuplementario().getValorTexto());
                                        log.debug("Cod. Ubicación: " + salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto());
                                        String[] datosCorr = null;
                                        datosCorr = MeLanbide41Manager.getInstance().getCodigosCorr(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), adapt);
//                                        datosCorr = MeLanbide41DAO.getInstance().getCodigosCorr(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), con);
                                        if (datosCorr == null || datosCorr.length ==0) {
                                            salida = "16";
                                        } else {
                                            /**
                                             * ****** ExpedienteVO *******
                                             */
                                            ExpedienteVO expVO = new ExpedienteVO();
                                            expVO.setNumExpediente(numExpediente);

                                            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
                                            if (salidaFecResolucionTramite.getStatus() == 0 && salidaFecResolucionTramite.getCampoSuplementario() != null) {
                                                expVO.setFechaResolucion(sf.format(salidaFecResolucionTramite.getCampoSuplementario().getValorFecha().getTime()));
                                            }

                                            log.debug("Fecha expediente: " + expVO.getFechaResolucion());
                                            /**
                                             * ****** CentroVO *******
                                             */
                                            CentroVO centro = new CentroVO();
                                            // Código del centro
                                            centro.setGenCenCodCentro(salidaCampoCodCentro.getCampoSuplementario().getValorTexto());
                                            // Código de la ubicación
                                            centro.setGenCenCodUbic(salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto());
                                            // Código de tipo de centro
                                            centro.setTipoCentro(codTipoCentro);

                                            log.debug("centro.getGenCenCodCentro(): " + centro.getGenCenCodCentro());
                                            log.debug("centro.getGenCenCodUbic(): " + centro.getGenCenCodUbic());
                                            log.debug("centro.getTipoCentro(): " + centro.getTipoCentro());

                                            if (datosCorr[0] == null || datosCorr[0].isEmpty() || datosCorr[0].equals("")) {
                                                log.error("NO hay corrServicio");
                                                // corrServicio
                                                salida = "17";
                                            } else if (datosCorr[1] == null || datosCorr[1].isEmpty()) {
                                                // corrSubservicio 
                                                log.error("NO hay corrSUBServicio");
                                                salida = "18";
                                            } else {
//                                                log.debug("hay corrServicio");
//                                                centro.setCorrServicio(datosCorr[0]); 
                                                log.debug("hay corrSUBServicio: " + datosCorr[1]);
                                                centro.setCorrSubservicio(datosCorr[1]);

//                                                log.debug("centro.getCorrServicio(): " + centro.getCorrServicio());
                                                log.debug("centro.getCorrSubservicio(): " + centro.getCorrSubservicio());

                                                // Lista de especialidades
                                                ArrayList<EspecialidadesVO> especialidadesExpediente = (ArrayList<EspecialidadesVO>) MeLanbide41DAO.getInstance().getDatosEspecialidades(numExpediente, con);
                                                net.lanbide.formacion.ws.regexlan.EspecialidadVO[] especialidadesVO = null;
                                                net.lanbide.formacion.ws.regexlan.ListaEspecialidades listaEspecialidades = null;

                                                if (especialidadesExpediente != null && especialidadesExpediente.size() > 0) {
                                                    especialidadesVO = new net.lanbide.formacion.ws.regexlan.EspecialidadVO[especialidadesExpediente.size()];
                                                    listaEspecialidades = new net.lanbide.formacion.ws.regexlan.ListaEspecialidades();
                                                }

                                                boolean errorNumeroAlumnosEspecialidad = false;
                                                for (int i = 0; especialidadesExpediente != null && i < especialidadesExpediente.size(); i++) {
                                                    EspecialidadesVO especialidad = especialidadesExpediente.get(i);
                                                    log.debug("Iniciando recogida de especialidades");
                                                    net.lanbide.formacion.ws.regexlan.EspecialidadVO aux = new EspecialidadVO();
                                                    aux.setCodigo(especialidad.getCodCP());
                                                    aux.setTipo("1");

                                                    log.debug("Id especialidad: " + especialidad.getId());
                                                    log.debug("Código: " + aux.getCodigo());
                                                    log.debug("Tipo: " + aux.getTipo());
                                                    IdentificacionEspVO idenEsp = MeLanbide41DAO.getInstance().getIdentificacionEspPorCodigoEspSol_NumExp(numExpediente, especialidad.getId(), con);
                                                    if (idenEsp.getAlumnosAut() != null) {
                                                        log.debug("Alumnos: " + idenEsp.getAlumnosAut());
                                                        aux.setNumPersonas(idenEsp.getAlumnosAut().intValue());
                                                        especialidadesVO[i] = aux;

                                                    } else {
                                                        log.error("No se ha podido recuperar de BBDD el número de alumnos de la especialidad " + especialidad.getId());
                                                        errorNumeroAlumnosEspecialidad = true;
                                                        break;
                                                    }
                                                }

                                                if (errorNumeroAlumnosEspecialidad) {
                                                    salida = "15";
                                                    error = "Hay especialidades que no tienen asignado un número de alumnos";
                                                } else if (especialidadesVO == null || especialidadesVO.length == 0) {
                                                    salida = "14";
                                                    error = "Ha habido un error al recuperar la especialidad";
                                                } else {
                                                    listaEspecialidades.setEspecialidad(especialidadesVO);

                                                    centroRequest.setExpediente(expVO);
                                                    centroRequest.setCentro(centro);
//                                              centroRequest.setListaEspecialidades(especialidadesVO);
                                                    centroRequest.setListaEspecialidades(listaEspecialidades);

                                                    centroConEspecialidades.setAltaCentroConEspecialidadesRequest(centroRequest);
//                                              AltaCentroConEspecialidadesResponse respuesta = binding.altaCentroConEspecialidades(centroConEspecialidades);

//AltaCentroConEspecialidadesResponse respuesta = binding.altaCentroConEspecialidades(centroRequest);
                                                    WsRegistroCentrosFormacionResultado respuesta = binding.altaCentroConEspecialidades(centroRequest);

                                                    if (respuesta != null) {

//                                                  log.info("respuesta.get_return().getCodRdo(): " + respuesta.get_return().getCodRdo());
//                                                  log.info("respuesta.get_return().getDescRdo(): " + respuesta.get_return().getDescRdo());
//                                                  log.info("respuesta.get_return().getNumCensoLB(): " + respuesta.get_return().getNumCensoLB());
                                                        log.info("respuesta.getCodRdo(): " + respuesta.getCodRdo());
                                                        log.info("respuesta.getDescRdo(): " + respuesta.getDescRdo());
                                                        log.info("respuesta.getNumCensoLB(): " + respuesta.getNumCensoLB());
                                                        if (respuesta.getCodRdo().equals(0)) {
                                                            String numCenso = respuesta.getNumCensoLB();
                                                            if (numCenso != null && !"".equals(numCenso)) {

                                                                // TODO: Grabar el número de censo en el trámite y ocurrencia actual, ya informados
                                                                // a través  de los parámetros de entrada
                                                                CampoSuplementarioModuloIntegracionVO campo = new CampoSuplementarioModuloIntegracionVO();
                                                                campo.setCodOrganizacion(Integer.toString(codOrganizacion));
                                                                campo.setCodProcedimiento(codProcedimiento);
                                                                campo.setEjercicio(ejercicio);
                                                                campo.setNumExpediente(numExpediente);
                                                                campo.setCodTramite(Integer.toString(codTramite));
                                                                campo.setOcurrenciaTramite(Integer.toString(ocurrenciaTramite));
                                                                campo.setCodigoCampo(codigoCampoNumeroRegistroVasco);
                                                                campo.setValorTexto(numCenso);
                                                                campo.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                                                campo.setTramite(true);

                                                                SalidaIntegracionVO salidaGrabacion = el.grabarCampoSuplementario(campo);
                                                                if (salidaGrabacion != null && salidaGrabacion.getStatus() == 0) {
                                                                    salida = "0";
                                                                } else {
                                                                    salida = "13";
                                                                }
                                                            } else {
                                                                salida = "12";
                                                                error = "Ha habido un error al recuperar el número de censo";
                                                            }
                                                        } else {
                                                            salida = "10";
                                                            error = respuesta.getDescRdo();
                                                        }
                                                    }
                                                }
                                                
                                                
                                                
                                            }
                                            
                                        }
                                    }
                                }
                            }
                        }// else

                    } catch (BDException e) {
                        log.error("Error al obtener una conexión a la BBDD: " + e.getMessage(), e);
                        salida = "11";
                    } catch (Exception e) {
                        log.error("Error : " + e.getMessage(), e);
                        salida = "11";
                    } finally {
                        try {
                            if (!"".equals(error)) {
                                salida = error;
                            }
                            if (adapt != null) {
                                adapt.devolverConexion(con);
                            }
                        } catch (BDException e) {
                            log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
                        }
                    }
                }
            }// else    
        }// if(propiedadesRecuperadas)

        log.info("altaCentroConEspecialidades devolviendo " + salida);
        return salida;
    }

    
    /**
     * Carga la pantalla "Consulta de ubicaciones" para el trámite de "Consulta y selección de centro"
     * @param codOrganizacion: Código organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia trámite
     * @param numExpediente: Número del expediente
     * @param request
     * @param response
     * @return String con la URL de la JSP a la que hay que redirigir el control
     */
    public String prepararCargaConsultaUbicaciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String salida = null;
        String[] datosExpediente = null;
        String codProcedimiento = null;
        String ejercicio = null;

        datosExpediente = numExpediente.split("/");
        ejercicio = datosExpediente[0];
        codProcedimiento = datosExpediente[1];

        String property = codOrganizacion + "/MODULO_INTEGRACION/" + this.getNombreModulo() + "/" + codProcedimiento + "/" + codTramite + "/SALIDA_CONSULTA_UBICACIONES";
        salida = ConfigurationParameter.getParameter(property, this.getNombreModulo());

        return salida;
    }
   
    /**
     * 
     * Recupera las ubicaciones de un determinado centro y las devuelve en un XML para que pueda ser visualizado
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
             *      1 --> Error al recuperar el documento de interesado con rol por defecto del expediente
             *      2 --> El expediente no tiene un interesado con rol por defecto y con un CIF como documento
             *      3 --> Se ha producido un error técnico al intentar recuperar el código de centro
             *      4 --> No se ha podido recuperar el código de centro asociado al documento del interesado con rol 
             *            por defecto en el expediente
             *      5 --> Error técnico al recuperar las ubicaciones del centro
             *      6 --> El centro no tienen asignada ninguna ubicación
             *      7 --> Ha ocurrido un error técnico al ejecutar la operación 
             *      9 --> Existe más de un centro para ese CIF
             */            
            String documento = null;
            boolean continuar = false;
            try {
                // Se busca el interesado con rol principal en el expediente
                documento = MeLanbide41DAO.getInstance().getDocumentoInteresadoRolDefecto(codOrganizacion, numExpediente, con);
                continuar = true;
            } catch (MeLanbide41Exception e) {
                documento = null;
            }

            if (!continuar) {
                codError = "1"; // Error al recuperar el documento de interesado con rol por defecto del expediente
            } else {

                //if(documento==null || "".equals(documento)){
                //codError ="2"; // El expediente no tiene un interesado con rol por defecto y con un CIF como documento                
                //}else{            
                ArrayList<String> codigosCentros = null;
                continuar = false;
                try {

                    codigosCentros = MeLanbide41DAO.getInstance().getCodCentro(documento, con);

                    continuar = true;
                } catch (MeLanbide41Exception e) {
                    continuar = false;
                }

                if (!continuar) {
                    codError = "3"; // Se ha producido un error técnico al recuperar el código de centro
                } else {

                    if (codigosCentros != null && codigosCentros.size() > 1) {
                        codError = "9";
                    } else if (codigosCentros == null || codigosCentros.isEmpty()) {
                        codError = "4";
                    } else if (codigosCentros.size() == 1) {

                        codCentro = codigosCentros.get(0);
                        continuar = false;
                        try {
                            // Se procede a recuperar las ubicaciones del centro
                            ubicaciones = MeLanbide41DAO.getInstance().getUbicacionesCentro(codCentro, con);
                            continuar = true;
                        } catch (MeLanbide41Exception e) {
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
        } catch (SQLException e) {
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
     * Para un trámite de un  expediente, se graba el código de centro y el código de ubicación seleccionados, en 
     * determinados campos suplementarios de tipo texto
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     */
    public void grabarUbicacionSeleccionada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        log.info(this.getClass().getName() + ".grabarUbicacionSeleccionada =====>");
        StringBuffer xml = new StringBuffer();
        String codError = null;
        String codigoUbicacion = null;
        String codigoCentro = null;

        String codigoCampoCodCentro = null;
        String codigoCampoCodUbicacion = null;
        String codigoCampoCodCensoSilicoi = null;
        String codigoCampoCodCensoLanbide = null;
        String numCensoSilicoi = null;
        String numCensoLanbide = null;

        try {

            codigoUbicacion = request.getParameter("codigoUbicacion");
            codigoCentro = request.getParameter("codigoCentro");
            log.info("codUbicacion: " + codigoUbicacion);
            log.info("codCentro: " + codigoCentro);
      
            /**
             * Valores posibles de la variable codError
             *      0 --> OK
             *      1 --> Error al recuperar codigoUbicacion o el código de centro
             *      2 --> No se ha podido recuperar el código del campo "Código de centro" para grabar su valor
             *      3 --> No se ha podido recuperar el código del campo "Código de ubicación" para grabar su valor
             *      4 --> No se ha podido grabar el valor del código del centro
             *      5 --> No se ha podido grabar el valor del código de la ubicación
             *      6 --> Se ha producido un error técnico
             *      7 --> No se ha podido recuperar el número de censo Silicoi
             *      8 --> No se han podido recuperar los códigos corr
             *      9 --> No se han podido recuperar corrServicio
             *      10 --> No se han podido recuperar corrSubservicio
             *      11 --> No se han podido recuperar el número de censo Lanbide
             */
            if (codigoUbicacion == null || codigoCentro == null || codigoCentro.length() == 0 || codigoUbicacion.length() == 0) {
                codError = "1";
            } else {

                codigoCampoCodCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO", this.getNombreModulo());
                codigoCampoCodUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION", this.getNombreModulo());
                codigoCampoCodCensoSilicoi = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMCENSOSILCOI", this.getNombreModulo());
                //codigoCampoCodCensoSilicoi = "NUMCENSOSILCOI2";
                codigoCampoCodCensoLanbide = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMCENSOLANBIDE", this.getNombreModulo());

                log.debug("codigoCampoCodCensoSilicoi: " + codigoCampoCodCensoSilicoi);

                if (codigoCampoCodCentro == null || codigoCampoCodCentro.length() == 0) {
                    codError = "2";
                } else if (codigoCampoCodUbicacion == null || codigoCampoCodUbicacion.length() == 0) {
                    codError = "3";
                } else {

                    String[] datosExpediente = numExpediente.split("/");
                    String ejercicio = datosExpediente[0];
                    String codProcedimiento = datosExpediente[1];

                    String[] datosCorr = null;
                    datosCorr = MeLanbide41Manager.getInstance().getCodigosCorr(codigoCentro, codigoUbicacion, getAdaptSQLBD(Integer.toString(codOrganizacion)));
                    if (datosCorr == null || datosCorr.length == 0) {
                        codError = "8";
                    } else {
                        if (datosCorr[0] == null || datosCorr[0].isEmpty() || datosCorr[0].equals("")) {
                            log.error("NO hay corrServicio");
                            codError = "9";
                        } else if (datosCorr[1] == null || datosCorr[1].isEmpty() || datosCorr[1].equals("")) {
                            log.error("NO hay corrSUBServicio");
                            codError = "10";
                        } else {
                            numCensoSilicoi = MeLanbide41Manager.getInstance().getNumCensoSILICOI(codigoCentro, codigoUbicacion, datosCorr[1], getAdaptSQLBD(Integer.toString(codOrganizacion)));
                            if (numCensoSilicoi == null || numCensoSilicoi.isEmpty()) {
                                codError = "7";
                            } else {
                                log.info("numCenso SILICOI: " + numCensoSilicoi);
                                IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

                                // Se graba el valor del numero de censo SILICOI del centro en el campo suplementario NUMCENSOSILCOI2, que es 
                                // de tipo texto
                                CampoSuplementarioModuloIntegracionVO campoSilicoi = new CampoSuplementarioModuloIntegracionVO();
                                campoSilicoi.setCodOrganizacion(Integer.toString(codOrganizacion));
                                campoSilicoi.setCodProcedimiento(codProcedimiento);
                                campoSilicoi.setEjercicio(ejercicio);
                                campoSilicoi.setCodigoCampo(codigoCampoCodCensoSilicoi);
                                campoSilicoi.setNumExpediente(numExpediente);
                                campoSilicoi.setValorTexto(numCensoSilicoi);
                                campoSilicoi.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                campoSilicoi.setCodTramite(Integer.toString(codTramite));
                                campoSilicoi.setOcurrenciaTramite(Integer.toString(ocurrenciaTramite));
                                campoSilicoi.setTramite(true);

                                SalidaIntegracionVO salidaCampoNumCenso = el.grabarCampoSuplementario(campoSilicoi);
                                log.info("censo SILICOI grabado: " + salidaCampoNumCenso.getStatus());

                                //if(salidaCampoNumCenso.getStatus()!=0){
                                //codError = "4";
                                //}else{
                                numCensoLanbide = MeLanbide41Manager.getInstance().getNumeroCensoCenFor(codigoCentro, codigoUbicacion, datosCorr[1], getAdaptSQLBD(Integer.toString(codOrganizacion)));
                                if (numCensoLanbide == null || numCensoLanbide.isEmpty() || numCensoLanbide.equalsIgnoreCase("")) {
                                    codError = "11";
                                } else {
                                    log.info("numCensoLanbide: " + numCensoLanbide);

                                    //grabamos el número de censo Lanbide como dato suplementario
                                    CampoSuplementarioModuloIntegracionVO campoCensoLanbide = new CampoSuplementarioModuloIntegracionVO();
                                    campoCensoLanbide.setCodOrganizacion(Integer.toString(codOrganizacion));
                                    campoCensoLanbide.setCodProcedimiento(codProcedimiento);
                                    campoCensoLanbide.setEjercicio(ejercicio);
                                    campoCensoLanbide.setCodigoCampo(codigoCampoCodCensoLanbide);
                                    campoCensoLanbide.setNumExpediente(numExpediente);
                                    campoCensoLanbide.setValorTexto(numCensoLanbide);
                                    campoCensoLanbide.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                    campoCensoLanbide.setCodTramite(Integer.toString(codTramite));
                                    campoCensoLanbide.setOcurrenciaTramite(Integer.toString(ocurrenciaTramite));
                                    campoCensoLanbide.setTramite(true);

                                    SalidaIntegracionVO salidaCampoNumCensoLanbide = el.grabarCampoSuplementario(campoCensoLanbide);
                                    log.info("censo lanbide grabado: " + salidaCampoNumCensoLanbide.getStatus());
                                    /*if(salidaCampoNumCensoLanbide.getStatus()!=0){
                            codError = "4";
                        }else{*/

                                    // Se graba el valor del código  del centro en el campo suplementario , que es 
                                    // de tipo texto
                                    CampoSuplementarioModuloIntegracionVO campoCentro = new CampoSuplementarioModuloIntegracionVO();
                                    campoCentro.setCodOrganizacion(Integer.toString(codOrganizacion));
                                    campoCentro.setCodProcedimiento(codProcedimiento);
                                    campoCentro.setEjercicio(ejercicio);
                                    campoCentro.setCodigoCampo(codigoCampoCodCentro);
                                    campoCentro.setNumExpediente(numExpediente);
                                    campoCentro.setValorTexto(codigoCentro);
                                    campoCentro.setCodTramite(Integer.toString(codTramite));
                                    campoCentro.setOcurrenciaTramite(Integer.toString(ocurrenciaTramite));
                                    campoCentro.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                    campoCentro.setTramite(true);

                                    SalidaIntegracionVO salidaCampoCodCentro = el.grabarCampoSuplementario(campoCentro);
                                    log.info("codCentro grabado: " + salidaCampoCodCentro.getStatus());

                                    if (salidaCampoCodCentro.getStatus() != 0) {
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
                                        campoUbicacion.setCodTramite(Integer.toString(codTramite));
                                        campoUbicacion.setOcurrenciaTramite(Integer.toString(ocurrenciaTramite));
                                        campoUbicacion.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                        campoUbicacion.setTramite(true);

                                        SalidaIntegracionVO salidaCampoCodUbicacion = el.grabarCampoSuplementario(campoUbicacion);

                                        log.info("codUbicacion grabado: " + salidaCampoCodUbicacion.getStatus());
                                        if (salidaCampoCodUbicacion.getStatus() != 0) {
                                            codError = "5";
                                        } else {
                                            codError = "0";
                                        }
                                    }
                                    //}
                                    //}
                                }                                                                
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
            log.debug("XML: " + xml.toString());
        } catch (Exception e) {
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
     * 
     * Envia correo con información de los datos (rol, identificador, nombre, dirección, e-mail, teléfono, ..) de todos los interesados del expediente
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @return
     */
    public String envioCorreoAltaCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {

        Connection con = null;
        AdaptadorSQLBD adapt = null;
        InteresadoExpedienteVO interesadoExpedienteVO = null;
        StringBuilder textoMensaje = new StringBuilder();
        String salida = null;

        String destinatario = ConfigurationParameter.getParameter("mail.destinatario", this.getNombreModulo());
        String remitente = ConfigurationParameter.getParameter("mail.remitente", this.getNombreModulo());
        String asunto = ConfigurationParameter.getParameter("mail.asunto", this.getNombreModulo());

        try {
            adapt = this.getAdaptSQLBD(Integer.toString(codOrganizacion));
            con = adapt.getConnection();

            MailHelper mailHelper = new MailHelper();

            ArrayList<InteresadoExpedienteVO> interesadosExp = MeLanbide41DAO.getInstance().getListaInteresadosExpediente(codOrganizacion, numExpediente, con);

            if (interesadosExp == null) {
                log.error("Error al recuperar los interesados del expediente " + numExpediente);
                salida = "1";
            } else {
                if (interesadosExp.isEmpty()) {
                    log.error("El expediente " + numExpediente + " no tiene interesados asociados");
                    salida = "2";
                } else {

                    asunto = asunto.replaceAll("@NUMERO@", numExpediente);

                    //Recorrer la lista de interesados del expediente
                    int j = 1;
                    for (int i = 0; i < interesadosExp.size(); i++) {
                        interesadoExpedienteVO = interesadosExp.get(i);

                        String contenido = ConfigurationParameter.getParameter("mail.contenido", this.getNombreModulo());

                        contenido = contenido.replaceAll("@interesado@", String.valueOf(j));
                        if (interesadoExpedienteVO.getDescRol() != null && !interesadoExpedienteVO.getDescRol().equals("")) {
                            contenido = contenido.replaceAll("@rol@", interesadoExpedienteVO.getDescRol());
                        } else {
                            contenido = contenido.replaceAll("@rol@", "");
                        }
                        if (interesadoExpedienteVO.getTxtDoc() != null && !interesadoExpedienteVO.getTxtDoc().equals("")) {
                            contenido = contenido.replaceAll("@documento@", interesadoExpedienteVO.getTxtDoc());
                        } else {
                            contenido = contenido.replaceAll("@documento@", "");
                        }
                        if (interesadoExpedienteVO.getNombreCompleto() != null && !interesadoExpedienteVO.getNombreCompleto().equals("")) {
                            contenido = contenido.replaceAll("@nombre@", interesadoExpedienteVO.getNombreCompleto());
                        } else {
                            contenido = contenido.replaceAll("@nombre@", "");
                        }
                        if (interesadoExpedienteVO.getDomicilio() != null && !interesadoExpedienteVO.getDomicilio().equals("")) {
                            contenido = contenido.replaceAll("@domicilio@", interesadoExpedienteVO.getDomicilio());
                        } else {
                            contenido = contenido.replaceAll("@domicilio@", "");
                        }

                        if (interesadoExpedienteVO.getMunicipio() != null && !interesadoExpedienteVO.getMunicipio().equals("")) {
                            contenido = contenido.replaceAll("@municipio@", interesadoExpedienteVO.getMunicipio());
                        } else {
                            contenido = contenido.replaceAll("@municipio@", "");
                        }

                        if (interesadoExpedienteVO.getProvincia() != null && !interesadoExpedienteVO.getProvincia().equals("")) {
                            contenido = contenido.replaceAll("@provincia@", interesadoExpedienteVO.getProvincia());
                        } else {
                            contenido = contenido.replaceAll("@provincia@", "");
                        }

                        if (interesadoExpedienteVO.getCp() != null && !interesadoExpedienteVO.getCp().equals("")) {
                            contenido = contenido.replaceAll("@codigopostal@", interesadoExpedienteVO.getCp());
                        } else {
                            contenido = contenido.replaceAll("@codigopostal@", "");
                        }

                        if (interesadoExpedienteVO.getEmail() != null && !interesadoExpedienteVO.getEmail().equals("")) {
                            contenido = contenido.replaceAll("@email@", interesadoExpedienteVO.getEmail());
                        } else {
                            contenido = contenido.replaceAll("@email@", "");
                        }
                        if (interesadoExpedienteVO.getTelf() != null && !interesadoExpedienteVO.getTelf().equals("")) {
                            contenido = contenido.replaceAll("@telefono@", interesadoExpedienteVO.getTelf());
                        } else {
                            contenido = contenido.replaceAll("@telefono@", "");
                        }

                        textoMensaje.append(contenido);
                        j++;
                    }

                    //mailHelper.sendMail(remitente,asunto,textoMensaje.toString());
                    mailHelper.sendMail(destinatario, asunto, textoMensaje.toString(), remitente);
                    salida = "0";
                }
            }
        } catch (BDException e) {
            log.error("Error al obtener una conexión a la BBDD. " + e.getMessage());
            salida = "3";
        } catch (MailServiceNotActivedException ms) {
            log.error(ms.getMessage());
            salida = "4";
        } catch (MailException me) {
            log.error(me.getMessage());
            salida = "5";
        } catch (SQLException ex) {
            log.error("Error al obtener una conexión a la BBDD (SQL). " + ex.getMessage());
            salida = "3";
        } finally {
            try {
                if (adapt != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al devolver la conexion. " + e.getMessage());
            }
        }
        log.info("envioCorreoAltaCentro() ====>");
        log.info("asunto: " + asunto + ", remitente: " + remitente);
        return salida;
    }
 
     /**
     * Operación que se ejecuta al dar de alta un expediente
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @return String que puede tomar los siguientes valores:
     *
     * "0" --> Si la operación se ha ejecutado correctamente 
     * "1" --> Error al obtener las propiedades de configuración
     * "2" --> Revisar las propiedades de configuración con los valores de los campos suplementarios y código de trámite
     * "3" --> No se ha podido establecer conexión con el servicio web de centros de formación
     * "4" --> No se ha podido obtener una conexión a la BBDD
     * "5" --> No se ha podido dar de alta el expendiente en el servicio web
     * 
     */
    public String altaExpedienteIkasLan(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        String salida = "-1";
        String error = "";
        boolean conexionWS = false;
        boolean propiedadesRecuperadas = false;
        WSExpedientesFormacionPortBindingStub binding = null;
        Connection con = null;
        AdaptadorSQLBD adapt = null;

        String urlServicioWeb = ConfigurationParameter.getParameter(ConstantesMeLanbide41.URL_WS_IKASLANF_ALTAEXPFORMACION, ConstantesMeLanbide41.FICHERO_PROPIEDADES);

        String codigoTramiteInscripcionCentroEnRegistro = null;
        String codigoCampoFechaResolucion = null;
        try {
            try {
                codigoCampoFechaResolucion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TRAMITE_FECHA_RESOLUCION", this.getNombreModulo());
                // Se recupera el código del trámite "INSCRIPCION O ACREDITACION DEL CENTRO O ENTIDAD EN EL REGISTRO" del que se lee la fecha de resolución           
                codigoTramiteInscripcionCentroEnRegistro = ConfigurationParameter.getParameter("CODIGO_TRAMITE_ACREDITACION_CENTRO_ENTIDAD_REGISTRO", this.getNombreModulo());

                log.info("NoErro - codigoCampoFechaResolucion: " + codigoCampoFechaResolucion);
                log.info("NoErro - codigoTramiteInscripcionCentroEnRegistro: " + codigoTramiteInscripcionCentroEnRegistro);
                log.info("NoErro - urlServicioWeb: " + urlServicioWeb);

                propiedadesRecuperadas = true;
            } catch (Exception e) {
                log.error(this.getClass().getName() + ".altaExpedienteIkasLan():  Error al recuperar valores de propiedades del fichero de configuración: " + e.getMessage());
                salida = "1";
            }

            if (propiedadesRecuperadas) {
                if (!MeLanbide41Utils.isInteger(codigoTramiteInscripcionCentroEnRegistro) || codigoTramiteInscripcionCentroEnRegistro == null || codigoCampoFechaResolucion == null) {
                    salida = "2";
                    error = "Ha habido un error al recuperar las propiedades";
                } else {
                    try {

                        binding = (WSExpedientesFormacionPortBindingStub) new WSExpedientesFormacionServiceLocator().getWSExpedientesFormacionPort(new java.net.URL(urlServicioWeb));
                        conexionWS = true;

                    } catch (Exception e) {
                        log.error(this.getClass().getName() + ".altaCentroConEspecialidades(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                    }

                    if (!conexionWS) {
                        salida = "3";
                        error = "Ha habido un error en la conexión con el servicio web";
                    } else {

                        AltaExpedienteRequest request = null;

                        try {
                            adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                            con = adapt.getConnection();
                        } catch (BDException e) {
                            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                        } catch (SQLException e) {
                            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                        }

                        if (con == null) {
                            // No se ha podido obtener una conexión a la BBDD
                            salida = "4";
                            error = "Ha habido un error en la conexión";
                        } else {
                            SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
                            String[] datosAlta = null;
                            String docInteresado = null;

                            try {
                                datosAlta = MeLanbide41DAO.getInstance().getDatosAltaExpedienteIkasLan(codOrganizacion, numExpediente, con);
                            } catch (MeLanbide41Exception ex) {
                                log.error(" Exception al getNumeroRegistroExpediente -  Ponemos datosAlta = null ", ex);
                                datosAlta = null;
                            } catch (SQLException ex) {
                                log.error(" Exception al getNumeroRegistroExpediente -  Ponemos datosAlta = null ", ex);
                                datosAlta = null;
                            }

                            if (datosAlta != null) {
                                log.info("Datos de alta del expediente recuperados OK - vamos a instaciar clases de WS - ClienteWSExpedientesFormacion ");
                                ExpedienteVO expediente = new ExpedienteVO();

                                //numExpediente
                                expediente.setNumExpediente(numExpediente);

                                //fechaCreacion
                                if (datosAlta[0] != null && !datosAlta[0].equals("")) {
                                    expediente.setFechaCreacion(datosAlta[0]);

                                    //numRegistroEntrada
                                    if (datosAlta[1] != null && !datosAlta[1].equals("")) {
                                        expediente.setNumRegistroEntrada(datosAlta[1]);

                                        //infoAdicional
                                        if (datosAlta[2] != null && !datosAlta[2].equals("")) {
                                            expediente.setInfoAdicional(datosAlta[2]);
                                        }
                                        //fechaResolucion
                                        // pasamos vacia no es obligatoria segun nueva documentacion
                                        //tipoExpediente
                                        Integer tipoExpedienteIkesLanF = null;
                                        try {
                                            tipoExpedienteIkesLanF = getTipoExpedienteIkasLanF(numExpediente, String.valueOf(codOrganizacion));
                                            log.info("Tipo de Expdiente recuperado a partir del Numero de Expediente Flexia : " + tipoExpedienteIkesLanF);
                                        } catch (Exception ex) {
                                            log.error("Error al preparar el ripo de expediente para enviar a Ikeslan", ex);
                                            salida = "11";
                                            error = " Error al recuperar el tipo de expediente para enviar a IkasLanF - " + ex.getMessage();
                                        }
                                        expediente.setTipoExpediente(tipoExpedienteIkesLanF);
                                        //identificadorInteresado
                                        try {
                                            //MeLanbide41DAO.getInstance().getDocumentoInteresadoRolDefecto(codOrganizacion, numExpediente, con);
                                            docInteresado = MeLanbide41Manager.getInstance().getDocInteresadoParaIkasLanF(codOrganizacion, numExpediente, con);
                                        } catch (MeLanbide41Exception ex) {
                                            docInteresado = "";
                                            log.error("Error al obtener el Numero del documento del interesado " + numExpediente);
                                        }

                                        if (docInteresado != null && !docInteresado.equals("")) {
                                            expediente.setIdentificadorInteresado(docInteresado);

                                            request = new AltaExpedienteRequest();
                                            request.setExpediente(expediente);

                                            WsExpedientesFormacionResultado response = binding.altaExpediente(request);

                                            if (response != null) {
                                                if (response.getCodRdo().equals(0)) {
                                                    salida = "0";
                                                } else {
                                                    salida = "5";
                                                    error = response.getDescRdo();
                                                }
                                            }
                                        } else {
                                            salida = "7";
                                            error = "No se ha podido recuperar el identificador del interesado.";
                                        }
                                        // Comentamos salida de error sin inf adicional no es campo obligatorio para WS
                                        //}
                                        //else
                                        //{
                                        //salida = "10";
                                        //}
                                    } else {
                                        salida = "9";
                                    }
                                } else {
                                    salida = "6";
                                    error = "No se ha podido recuperar el numero de registro del expediente.";
                                }
                            } else {
                                salida = "8";
                            }
                        }
                    }
                }
            } else {
                salida = "1";
                error = "Ha habido un error al recuperar las propiedades";
            }
        } catch (RemoteException ex) {
            log.error(ex.getMessage());
            salida = "3";
            error = "Ha habido un error en la conexión con el servicio web";
        } catch (Exception ex) {
            log.error("Exception General al dar de alta expeciente lanF", ex);
            salida = "4";
            error = "Ha habido un Exception general al dar de alta el expediente lanF - " + ex.getMessage();
        } finally {
            try {
                if (!error.equals("")) //&& !salida.equals("5")
                {
                    salida = error;
                }
                if (adapt != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
        log.info("altaExpedienteIkaslanF - Salida : " + salida);
        return salida;
    }

    private Integer getTipoExpedienteIkasLanF(String numExpediente, String codOrganizacion) {
        log.info(" getTipoExpedienteIkasLanF - BEGIN - " + numExpediente);
        Integer tipoExpedienteIkesLanF = null;
        IModuloIntegracionExternoCamposFlexia interfazCamposSuple = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salidaCS = new SalidaIntegracionVO();
        String[] datosNumExpediente = new String[3];
        String ejercicioExpediente = "";
        String procedimientoExpedi = "";

        if (numExpediente != null) {
            datosNumExpediente = numExpediente.split(ConstantesMeLanbide41.BARRA_SEPARADORA);
            ejercicioExpediente = datosNumExpediente[0];
            procedimientoExpedi = datosNumExpediente[1];
        }
        if (procedimientoExpedi.equalsIgnoreCase(ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_PROCEDIMIENTO_RGCF, ConstantesMeLanbide41.FICHERO_PROPIEDADES))) {
            tipoExpedienteIkesLanF = Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide41.TIPOIKASLANF_PROCEDIMIENTO_RGCF, ConstantesMeLanbide41.FICHERO_PROPIEDADES));
        } else if (procedimientoExpedi.equalsIgnoreCase(ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_PROCEDIMIENTO_RGCFM, ConstantesMeLanbide41.FICHERO_PROPIEDADES))) {
            String codCampoSupleTipoModiRGCFM = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_CAMPOSUP_TIPOMODIF_RGCFM, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
            String tipoCampoSupleTipoModiRGCFM = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.TIPODATO_CAMPOSUP_TIPOMODIF_RGCFM, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
            salidaCS = interfazCamposSuple.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicioExpediente, numExpediente, procedimientoExpedi, codCampoSupleTipoModiRGCFM, Integer.valueOf(tipoCampoSupleTipoModiRGCFM));
            log.info(" codCampoSupleTipoModiRGCFM : " + codCampoSupleTipoModiRGCFM);
            log.info(" tipoCampoSupleTipoModiRGCFM : " + codCampoSupleTipoModiRGCFM);
            log.info("salidaCS : " + salidaCS.getStatus() + "-" + salidaCS.getDescStatus());
            String valorCS = "";
            if (salidaCS.getStatus() == 0) {
                CampoSuplementarioModuloIntegracionVO desplegable = salidaCS.getCampoSuplementario();
                valorCS = desplegable.getValorDesplegable();
                log.info(" valorCS : " + valorCS);
                String[] valoresDesplegableTipoModif = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.COD_DESP_TIPOMODIF_LISTA_CODVALORES, ConstantesMeLanbide41.FICHERO_PROPIEDADES).split(ConstantesMeLanbide41.SEPARADOR_DOT_COMMA);
                if (valoresDesplegableTipoModif != null && valoresDesplegableTipoModif.length > 0) {
                    for (String codValorDes : valoresDesplegableTipoModif) {
                        if (valorCS.equalsIgnoreCase(codValorDes)) {
                            tipoExpedienteIkesLanF = Integer.valueOf(ConfigurationParameter.getParameter(codValorDes, ConstantesMeLanbide41.FICHERO_PROPIEDADES));
                            break;
                        }
                    }
                }
            }
        } else if (procedimientoExpedi.equalsIgnoreCase(ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_PROCEDIMIENTO_RGEF, ConstantesMeLanbide41.FICHERO_PROPIEDADES))) {
            tipoExpedienteIkesLanF = Integer.valueOf(ConfigurationParameter.getParameter(ConstantesMeLanbide41.TIPOIKASLANF_PROCEDIMIENTO_RGEF, ConstantesMeLanbide41.FICHERO_PROPIEDADES));
        } else if (procedimientoExpedi.equalsIgnoreCase(ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_PROCEDIMIENTO_CEPAP, ConstantesMeLanbide41.FICHERO_PROPIEDADES))) {
            String codCampoSupleTipoAcreditacionCEPAP = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.CODIGO_CAMPOSUP_TIPOACREDITACION_CEPAP, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
            String tipoCampoSupleTipoAcreditacionCEPAP = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.TIPODATO_CAMPOSUP_TIPOACREDITACION_CEPAP, ConstantesMeLanbide41.FICHERO_PROPIEDADES);
            salidaCS = interfazCamposSuple.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicioExpediente, numExpediente, procedimientoExpedi, codCampoSupleTipoAcreditacionCEPAP, Integer.valueOf(tipoCampoSupleTipoAcreditacionCEPAP));
            String valorCS = "";
            log.info(" codCampoSupleTipoAcreditacionCEPAP : " + codCampoSupleTipoAcreditacionCEPAP);
            log.info(" tipoCampoSupleTipoAcreditacionCEPAP : " + tipoCampoSupleTipoAcreditacionCEPAP);
            log.info("salidaCS : " + salidaCS.getStatus() + "-" + salidaCS.getDescStatus());
            if (salidaCS.getStatus() == 0) {
                CampoSuplementarioModuloIntegracionVO desplegable = salidaCS.getCampoSuplementario();
                valorCS = desplegable.getValorDesplegable();
                log.info(" valorCS : " + valorCS);
                String[] valoresDesplegableTipoModif = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide41.BARRA_SEPARADORA + ConstantesMeLanbide41.COD_DESP_TIPOACREDITACION_CEPAP_LISTA_CODVALORES, ConstantesMeLanbide41.FICHERO_PROPIEDADES).split(ConstantesMeLanbide41.SEPARADOR_DOT_COMMA);
                if (valoresDesplegableTipoModif != null && valoresDesplegableTipoModif.length > 0) {
                    for (String codValorDes : valoresDesplegableTipoModif) {
                        if (valorCS.equalsIgnoreCase(codValorDes)) {
                            tipoExpedienteIkesLanF = Integer.valueOf(ConfigurationParameter.getParameter(codValorDes, ConstantesMeLanbide41.FICHERO_PROPIEDADES));
                            break;
                        }
                    }
                }
            }
        }
        log.info(" getTipoExpedienteIkasLanF - END - " + numExpediente + " Return : " + tipoExpedienteIkesLanF);
        return tipoExpedienteIkesLanF;
    }
}
