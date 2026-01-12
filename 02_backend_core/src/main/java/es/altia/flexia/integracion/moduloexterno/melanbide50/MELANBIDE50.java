/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide50;

/**
 *
 * @author davidg
 */
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide50.dao.MeLanbide50DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.manager.MeLanbide50Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConstantesMeLanbide50;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.MeLanbide50Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide50.util.MeLanbide50Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.CampoSuplementario;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Domicilio;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Tercero;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.capacidad.CapacidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.direccion.DireccionInteresadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.disponibilidad.DisponibilidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.documentos.DocumentosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.dotacion.DotacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.espacios.EspacioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.identificacionesp.IdentificacionEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.material.MaterialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.servicios.ServiciosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.ubicacion.UbicacionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.flexia.webservice.registro.cliente.wto.DomicilioVO;
import es.altia.flexia.webservice.registro.cliente.wto.InfoConexionVO;
import es.altia.flexia.webservice.registro.cliente.wto.RegistroVO;
import es.altia.flexia.webservice.registro.cliente.wto.RemitenteVO;
import es.altia.flexia.webservice.registro.cliente.wto.SalidaRegistroESBean;
import es.altia.flexia.webservice.registro.cliente.wsimpl.WSRegistroESBindingStub;
import es.altia.flexia.webservice.registro.cliente.wsimpl.WSRegistroESServiceLocator;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6DocumentoDossier;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DossierServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6UtilExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.config.Lan6Config;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
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
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.rpc.ServiceException;
import net.lanbide.formacion.ws.regexlan.AmpliacionAlumnadoEspecialidadesCentro;
import net.lanbide.formacion.ws.regexlan.AmpliacionAlumnadoEspecialidadesCentroRequest;
import net.lanbide.formacion.ws.regexlan.AmpliacionEspecialidadesCentro;
import net.lanbide.formacion.ws.regexlan.AmpliacionEspecialidadesCentroRequest;
import net.lanbide.formacion.ws.regexlan.CambioDomicilioCentro;
import net.lanbide.formacion.ws.regexlan.CambioDomicilioCentroRequest;
import net.lanbide.formacion.ws.regexlan.CambioTitularidadCentro;
import net.lanbide.formacion.ws.regexlan.CambioTitularidadCentroRequest;
import net.lanbide.formacion.ws.regexlan.DireccionVO;
import net.lanbide.formacion.ws.regexlan.EspecialidadVO;
import net.lanbide.formacion.ws.regexlan.ExpedienteVO;
import net.lanbide.formacion.ws.regexlan.WSRegistroCentrosFormacionPortBindingStub;
import net.lanbide.formacion.ws.regexlan.WSRegistroCentrosFormacionServiceLocator;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MELANBIDE50 extends ModuloIntegracionExterno {

    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE50.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

    // Alta Expedientes via registro platea --> MELANBIDE 42
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        log.info("cargarExpedienteExtension() BEGIN ");
        xml = xml.replaceAll("MELANBIDE41", "MELANBIDE50");

        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);

        //Controlamos que no tengamos ningun error registrado en el proceso anterior de modulomelanbide50
        //Visitaremos la ramade errordigitpara comprobar este mensaje.
        /*  AdaptadorSQLBD adapt = null;
         HttpServletRequest request = null;
         HttpServletResponse response = null;
         String errorMensajeUsuario= MeLanbide50Manager.getInstance().getErrorEspecialCargarExpedientes50(numeroExpediente, adapt);
         request.setAttribute("errorMensajeUsuario", errorMensajeUsuario); 
         */
 /* //xml
         StringBuffer xmlSalida = new StringBuffer();
         xmlSalida.append("<RESPUESTA>");
         xmlSalida.append("<ERROR_OPERACION>");
         xmlSalida.append(errorMensajeUsuario);
         log.error("errorMensajeUsuario: "+ errorMensajeUsuario);
         xmlSalida.append("</ERROR_OPERACION>");            
         xmlSalida.append("</RESPUESTA>");
         try{
         response.setContentType("text/xml");
         response.setCharacterEncoding("UTF-8");
         PrintWriter out = response.getWriter();
         out.print(xmlSalida.toString());
         out.flush();
         out.close();
         }catch(Exception e){
         e.printStackTrace();
         }//try-catch
         */
        log.info("cargarExpedienteExtension() END");
    }

    public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
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
            ano = MeLanbide50Utils.getEjercicioDeExpediente(numExpediente);
        } catch (Exception ex) {
            log.error("Error al recuperar el ano getEjercicioDeExpediente " + ano + " --", ex);
        }

        if (adapt != null) {
            try {

                // SubPestana Especialidades
                List<EspecialidadesVO> listEspecialidades = MeLanbide50Manager.getInstance().getDatosEspecialidades(numExpediente, adapt);
                log.info("DESPUES DE RECOGER ESPECIALIDADES");
                if (!listEspecialidades.isEmpty()) {
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
                List<ServiciosVO> listServicios = MeLanbide50Manager.getInstance().getDatosServicios(numExpediente, adapt);
                if (!listServicios.isEmpty()) {
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
                List<DisponibilidadVO> listDisponibilidad = MeLanbide50Manager.getInstance().getDatosDisponibilidad(numExpediente, adapt);
                if (!listDisponibilidad.isEmpty()) {
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

            // Documentos aportados anteriormente
            /*  List<DocumentosVO> listDocumentos = MeLanbide50Manager.getInstance().getDatosDocumentos(numExpediente, adapt);
             log.error("DESPUES DE RECOGER Documentos");
             if (listDocumentos.size() > 0) {
             request.setAttribute("listDocumentos", listDocumentos);
             }*/
        }
        log.info("antes de retornar la url : /jsp/extension/melanbide50/melanbide50.jsp  -- amano");
        return "/jsp/extension/melanbide50/melanbide50.jsp";
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
                especialidad = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (especialidad != null) {
                    request.setAttribute("datoEspecialidad", especialidad);
                }
                //Lista Idetificacion Observaciones
                List<IdentificacionEspVO> listaIdentificacionEsp = MeLanbide50Manager.getInstance().getDatosIdentificacionEsp(numExpediente, especialidad.getId(), adapt);
                if (!listaIdentificacionEsp.isEmpty()) {
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
                List<CapacidadVO> listCapacidad = MeLanbide50Manager.getInstance().getDatosCapacidad(numExpediente, especialidad.getId(), adapt);
                if (!listCapacidad.isEmpty()) {
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
                List<DotacionVO> listaDotacion = MeLanbide50Manager.getInstance().getDatosDotacion(numExpediente, especialidad.getId(), adapt);
                if (!listaDotacion.isEmpty()) {
                    request.setAttribute("listaDotacion", listaDotacion);
                }
                try {
                    url = cargarSubpestanaDotacion(listaDotacion, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaDotacion", url);
                    }
                } catch (Exception ex) {
                }
                //Lista Materal de Consumo
                List<MaterialVO> listaMaterial = MeLanbide50Manager.getInstance().getDatosMaterial(numExpediente, especialidad.getId(), adapt);
                if (!listaMaterial.isEmpty()) {
                    request.setAttribute("listaMaterial", listaMaterial);
                }
                try {
                    url = cargarSubpestanaMaterial(listaMaterial, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaMaterial", url);
                    }
                } catch (Exception ex) {
                }
                // subpestana ESPACIOS
                try {
                    List<EspacioVO> listaEspacios = MeLanbide50Manager.getInstance().getDatosEspacio(numExpediente, especialidad.getId(), adapt);
                    if (!listaEspacios.isEmpty()) {
                        request.setAttribute("listaEspacios", listaEspacios);
                    }
                    url = cargarSubpestanaEspacios(listaEspacios, adapt, request);
                    if (url != null) {
                        request.setAttribute("urlPestanaEspacios", url);
                    }
                } catch (Exception ex) {
                    log.error("eror cargarSubpestana Espacios", ex);
                }
            } catch (Exception ex) {

            }
        }
        return "/jsp/extension/melanbide50/listasxEspecialidad.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

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

    private String cargarSubpestanaEspecialidades(List<EspecialidadesVO> listEspecialidades, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (!listEspecialidades.isEmpty()) {
            try {
                //int ano = MeLanbide50Utils.getEjercicioDeExpediente(sol.getNumExp());
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
        return "/jsp/extension/melanbide50/especialidades.jsp";
    }

    private String cargarSubpestanaServicios(List<ServiciosVO> listServicios, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (!listServicios.isEmpty()) {
            try {
                //int ano = MeLanbide50Utils.getEjercicioDeExpediente(sol.getNumExp());
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
        return "/jsp/extension/melanbide50/servicios.jsp";
    }

    private String cargarSubpestanaDisponibilidad(List<DisponibilidadVO> listDisponibilidad, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (!listDisponibilidad.isEmpty()) {
            try {
                //int ano = MeLanbide50Utils.getEjercicioDeExpediente(sol.getNumExp());
                //EcaConfiguracionVO config = MeLanbide35Manager.getInstance().getConfiguracionEca(ano, adapt);
                //request.setAttribute("ecaConfiguracion", config);
            } catch (Exception ex) {
            }

            try {
                int codIdioma = 1;
                try {
                    String nombreModulo = MeLanbide50Utils.getNombreModulo();
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
        return "/jsp/extension/melanbide50/disponibilidad.jsp";
    }

    private String cargarSubpestanaCapacidad(List<CapacidadVO> listCapacidad, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (!listCapacidad.isEmpty()) {
            try {
                //int ano = MeLanbide50Utils.getEjercicioDeExpediente(sol.getNumExp());
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
        return "/jsp/extension/melanbide50/capacidad.jsp";
    }

    private String cargarSubpestanaDotacion(List<DotacionVO> listaDotacion, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (!listaDotacion.isEmpty()) {
            try {
                //int ano = MeLanbide50Utils.getEjercicioDeExpediente(sol.getNumExp());
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
        return "/jsp/extension/melanbide50/dotacion.jsp";
    }

    private String cargarSubpestanaMaterial(List<MaterialVO> listaMaterial, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (!listaMaterial.isEmpty()) {
            try {
                //int ano = MeLanbide50Utils.getEjercicioDeExpediente(sol.getNumExp());
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
        return "/jsp/extension/melanbide50/material.jsp";
    }

    private String cargarSubpestanaEspacios(List<EspacioVO> listaEspacios, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (!listaEspacios.isEmpty()) {
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
        return "/jsp/extension/melanbide50/espacios.jsp";
    }

    private String cargarSubpestanaIdentificacionEsp(List<IdentificacionEspVO> listaIdentificacionEsp, AdaptadorSQLBD adapt, HttpServletRequest request) {
        if (!listaIdentificacionEsp.isEmpty()) {
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
        return "/jsp/extension/melanbide50/identificacionEsp.jsp";
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
                MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
                listaCertificados = meLanbide50Manager.getCertificados(con);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Guardamos los certificados recuperados en la request
            request.setAttribute("listaCertificados", listaCertificados);

            List<EspecialidadesVO> listaEspSol = MeLanbide50Manager.getInstance().getDatosEspecialidades(numExpediente, con);

            if (listaEspSol != null) {
                request.setAttribute("listaEspSol", listaEspSol);
            }

            //lista motivos denegación completa
            List<ValorCampoDesplegableModuloIntegracionVO> listaMotDeng = MeLanbide50Manager.getInstance().getDatosMotivosDeneg(numExpediente, con);

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
        return "/jsp/extension/melanbide50/operaciones/nuevaEspecialidad.jsp?codOrganizacionModulo=" + codOrganizacion;
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
            String codigocp = request.getParameter("codigocp");
            String denominacion = request.getParameter("denominacion");
            String presencial = request.getParameter("presencial");
            String teleformacion = request.getParameter("teleformacion");
            String acreditacion = request.getParameter("acreditacion");
            String motDeneg = request.getParameter("motDeneg");

            nuevaEsp.setNumExp(numExpediente);
            nuevaEsp.setCodCP(codigocp);
            nuevaEsp.setDenominacion(denominacion);
            nuevaEsp.setMotDeneg(motDeneg.split(","));

            if (!presencial.isEmpty() && !presencial.equals("")) {
                nuevaEsp.setInscripcionPresencial(Integer.valueOf(presencial));
            }
            //else
            //    nuevaEsp.setInscripcionPresencial(Integer.parseInt(ConstantesDatos.ZERO));
            if (!teleformacion.isEmpty() && !teleformacion.equals("")) {
                nuevaEsp.setInscripcionTeleformacion(Integer.valueOf(teleformacion));
            }
            //else
            //    nuevaEsp.setInscripcionTeleformacion(Integer.parseInt(ConstantesDatos.ZERO));
            if (!acreditacion.isEmpty() && !acreditacion.equals("")) {
                nuevaEsp.setAcreditacion(Integer.valueOf(acreditacion));
            }
            //else
            //    nuevaEsp.setAcreditacion(Integer.parseInt(ConstantesDatos.ZERO)); 
            try {
                adapt.inicioTransaccion(con);
                //creo especialidad
                MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
                int idInsertado = meLanbide50Manager.crearNuevaEspecialidad(nuevaEsp, con);
                //creo motivos de denegación de la especialidad
                if (nuevaEsp.getMotDeneg() != null && nuevaEsp.getMotDeneg().length > 0) {
                    for (String idMotDeneg : nuevaEsp.getMotDeneg()) {
                        if (idMotDeneg != null && !"".equals(idMotDeneg)) {
                            meLanbide50Manager.crearNuevaEspecialidadMotDeneg(idInsertado, idMotDeneg, con);
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
                        log.error("Error al realizar rollback en la operacion MELANBIDE50.crearEspecialidad: " + e.getMessage());
                    }
                }
            }

            //recupero de nuevo las especialidades actualizadas para mostrarlas
            try {
                especialidades = MeLanbide50Manager.getInstance().getDatosEspecialidades(numExpediente, con);
            } catch (BDException bde) {
                codigoOperacion = "1";
                java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
            } catch (Exception ex) {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (BDException ex) {
            codigoOperacion = "2";
        } catch (NumberFormatException ex) {
            codigoOperacion = "2";
        } catch (SQLException ex) {
            codigoOperacion = "2";
        } finally {
            if (adapt != null && con != null) {
                try {
                    adapt.devolverConexion(con);
                    log.debug(" crearEspecialidad() - Conexion a la BBDD - Cerrada: ");
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaEspecialidades(codigoOperacion, especialidades);
        retornarXML(xmlSalida, response);

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
                MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
                listaCertificados = meLanbide50Manager.getCertificados(con);
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Guardamos los certificados recuperados en la request
            request.setAttribute("listaCertificados", listaCertificados);

            if (id != null && !id.isEmpty()) {
                EspecialidadesVO datModif = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExpediente, id, con);
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            List<EspecialidadesVO> listaEspSol = MeLanbide50Manager.getInstance().getDatosEspecialidades(numExpediente, con);
            if (listaEspSol != null) {
                request.setAttribute("listaEspSol", listaEspSol);
            }

            //lista motivos denegación seleccionados para la especialidad a modificar
            List<String> listaMotDenegSelec = MeLanbide50Manager.getInstance().getMotDenegSelec(id, con);
            if (listaEspSol != null) {
//                request.setAttribute("codListaMotivos", new String[] {"1","2"});
                request.setAttribute("codListaMotivos", listaMotDenegSelec);
            }

            List<ValorCampoDesplegableModuloIntegracionVO> listaMotDeng = MeLanbide50Manager.getInstance().getDatosMotivosDeneg(numExpediente, con);
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
        return "/jsp/extension/melanbide50/operaciones/nuevaEspecialidad.jsp?codOrganizacionModulo=" + codOrganizacion;

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
            String id = request.getParameter("id");
            String codigocp = request.getParameter("codigocp");
            String denominacion = request.getParameter("denominacion");
            String presencial = request.getParameter("presencial");
            String teleformacion = request.getParameter("teleformacion");
            String acreditacion = request.getParameter("acreditacion");
            String motDeneg = request.getParameter("motDeneg");

            EspecialidadesVO datModif = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExpediente, id, con);
            datModif.setId(Integer.valueOf(id));
            datModif.setCodCP(codigocp);
            datModif.setNumExp(numExpediente);
            datModif.setDenominacion(denominacion);
            datModif.setInscripcionPresencial((!presencial.isEmpty() ? Integer.valueOf(presencial) : null));
            datModif.setInscripcionTeleformacion((!teleformacion.isEmpty() ? Integer.valueOf(teleformacion) : null));
            datModif.setAcreditacion((!acreditacion.isEmpty() ? Integer.valueOf(acreditacion) : null));
            datModif.setMotDeneg(motDeneg.split(","));

            try {
                adapt.inicioTransaccion(con);
                MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
                meLanbide50Manager.modificarEspecialidad(datModif, con);
                meLanbide50Manager.eliminarEspecialidadMotDeneg(datModif.getId(), con);
                if (datModif.getMotDeneg() != null && datModif.getMotDeneg().length > 0) {
                    for (String idMotDeneg : datModif.getMotDeneg()) {
                        if (idMotDeneg != null && !"".equals(idMotDeneg)) {
                            meLanbide50Manager.crearNuevaEspecialidadMotDeneg(datModif.getId(), idMotDeneg, con);
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
                        log.error("Error al realizar rollback en la operacion MELANBIDE50.crearEspecialidad: " + e.getMessage());
                    }
                }
            }
            try {
                especialidades = MeLanbide50Manager.getInstance().getDatosEspecialidades(numExpediente, con);
            } catch (BDException bde) {
                codigoOperacion = "1";
                java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
            } catch (Exception ex) {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
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

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaEspecialidades(codigoOperacion, especialidades);
        retornarXML(xmlSalida, response);
    }

    public void eliminarEspecialidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspecialidadesVO> especialidades = new ArrayList<EspecialidadesVO>();
        try {
            String id = request.getParameter("id");
            Integer idE = null;
            if (id == null || id.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idE = Integer.valueOf(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idE != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
                    Connection con = null;
                    try {
                        con = adapt.getConnection();
                        adapt.inicioTransaccion(con);
                        meLanbide50Manager.eliminarEspecialidadMotDeneg(idE, con);
                        meLanbide50Manager.eliminarEspecialidad(numExpediente, idE, con);

                        codigoOperacion = "0";
                        especialidades = MeLanbide50Manager.getInstance().getDatosEspecialidades(numExpediente, con);

                        //Recogemos disponiblidad e identificacion de ese mismo id de especialidad para borrarlos
                        DisponibilidadVO disponibilidad = new DisponibilidadVO();
                        IdentificacionEspVO identificacion = new IdentificacionEspVO();
                        try {
                            disponibilidad = meLanbide50Manager.getDisponibilidadPorCodigoEspSol_NumExp(numExpediente, idE, con);
                            identificacion = meLanbide50Manager.getIdentificacionEspPorCodigoEspSol_NumExp(numExpediente, idE, con);
                            int eliminaDisponibilidad = meLanbide50Manager.eliminarDisponibilidadDesdeListEspSol(numExpediente, disponibilidad.getId(), idE, con);
                            if (eliminaDisponibilidad > 0) {
                                log.info("Eliminada Correctamente Disponibilidad: " + disponibilidad.getId() + "  para IdEspecialidad " + idE + "Expediente : " + numExpediente);
                            }
                            int eliminaIdentificacion = meLanbide50Manager.eliminarIdentificacionEspDesdeListEspSol(numExpediente, identificacion.getId(), idE, con);
                            if (eliminaIdentificacion > 0) {
                                log.info("Eliminada Correctamente Identificacion: " + identificacion.getId() + "  para IdEspecialidad " + idE + "Expediente : " + numExpediente);
                            }
                        } catch (Exception ex1) {
                            log.error("Error al Eliminar Disponibilidades o Identifaciones de Especialidades Solicitadas al BOrrar desde la lista de Especialidades: IdEspecialidad " + idE + "Expediente : " + numExpediente);
                            java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                        adapt.finTransaccion(con);
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                        log.error("Error al eliminar especialidad: " + ex.getMessage());
                        codigoOperacion = "2";
                        if (adapt != null && con != null) {
                            try {
                                adapt.rollBack(con);
                            } catch (BDException e) {
                                log.error("Error al realizar rollback en la operacion MELANBIDE50.crearEspecialidad: " + e.getMessage());
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

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaEspecialidades(codigoOperacion, especialidades);
        retornarXML(xmlSalida, response);
    }

    public String cargarNuevoServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");
        } catch (Exception ex) {
        }
        return "/jsp/extension/melanbide50/operaciones/nuevoServicio.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ServiciosVO> lista = new ArrayList<ServiciosVO>();
        ServiciosVO nuevoDato = new ServiciosVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String descripcion = request.getParameter("descripcion");
            String ubicacion = request.getParameter("ubicacion");
            String superficie = request.getParameter("superficie");
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

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean insertOK = meLanbide50Manager.crearNuevoServicio(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosServicios(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.debug("No se ha podido Insertar El Nuevo servicio para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaServicio(codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public String cargarModifServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                ServiciosVO datModif = MeLanbide50Manager.getInstance().getServicioPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {
        }
        return "/jsp/extension/melanbide50/operaciones/nuevoServicio.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void modificarServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ServiciosVO> lista = new ArrayList<ServiciosVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String descripcion = request.getParameter("descripcion");
            String ubicacion = request.getParameter("ubicacion");
            String superficie = request.getParameter("superficie");
            superficie = superficie.replace(",", ".");

            ServiciosVO datModif = MeLanbide50Manager.getInstance().getServicioPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.valueOf(id));
            datModif.setNumExp(numExpediente);
            datModif.setDescripcion(descripcion);
            datModif.setUbicacion(ubicacion);
            datModif.setSuperficie((!superficie.isEmpty() ? new BigDecimal(superficie) : null));

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean modOK = meLanbide50Manager.modificarServicio(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosServicios(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaServicio(codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public void eliminarServicio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ServiciosVO> lista = new ArrayList<ServiciosVO>();
        try {
            String id = request.getParameter("id");
            Integer idS = null;
            if (id == null || id.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idS = Integer.valueOf(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idS != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide50Manager.getInstance().eliminarServicio(numExpediente, idS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide50Manager.getInstance().getDatosServicios(numExpediente, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaServicio(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public String cargarModifDisponibilidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idespsol = request.getParameter("idespsol");
            if ((id != null && !id.isEmpty()) && (idespsol != null && !idespsol.isEmpty())) {
                DisponibilidadVO datModif = MeLanbide50Manager.getInstance().getDisponibilidadPorCodigo(numExpediente, id, idespsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide50/operaciones/nuevaDisponibilidad.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarDisponibilidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DisponibilidadVO> lista = new ArrayList<DisponibilidadVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String idEspSol = request.getParameter("idEspSol");
            String codcp = request.getParameter("codcp");
            String propiedadce = request.getParameter("propiedadce");
            String situados = request.getParameter("situados");
            String supaulas = request.getParameter("supaulas");
            supaulas = supaulas.replace(",", ".");
            String suptaller = request.getParameter("suptaller");
            suptaller = suptaller.replace(",", ".");
            String supaulastaller = request.getParameter("supaulastaller");
            supaulastaller = supaulastaller.replace(",", ".");
            String supcampoprac = request.getParameter("supcampoprac");
            supcampoprac = supcampoprac.replace(",", ".");

            DisponibilidadVO datModif = MeLanbide50Manager.getInstance().getDisponibilidadPorCodigo(numExpediente, id, idEspSol, adapt);
            datModif.setId(Integer.valueOf(id));
            datModif.setIdEspSol(Integer.valueOf(idEspSol));
            datModif.setNumExp(numExpediente);
            datModif.setCodCp(codcp);
            datModif.setPropiedadCedidos(propiedadce);
            datModif.setSituados(situados);
            datModif.setSupAulas(supaulas);
            datModif.setSupTaller(suptaller);
            datModif.setSupAulaTaller(supaulastaller);
            datModif.setSupCampoPract(supcampoprac);

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean modOK = meLanbide50Manager.modificarDisponibilidad(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosDisponibilidad(numExpediente, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaDisponibilidad(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public String cargarModifIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idespsol = request.getParameter("idEspSol");
            if ((id != null && !id.isEmpty()) && (idespsol != null && !idespsol.isEmpty())) {
                IdentificacionEspVO datModif = MeLanbide50Manager.getInstance().getIdentificacionEspPorCodigo(numExpediente, id, idespsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide50/operaciones/nuevaIdentificacionEsp.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public String cargarNuevaIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response, EspecialidadesVO datoEsp) {
        try {
            String nuevo = "1";
            request.setAttribute("datoEspecialidad", datoEsp);
            IdentificacionEspVO identEspecialidad = new IdentificacionEspVO();
            List<IdentificacionEspVO> listIdenEsp = new ArrayList<IdentificacionEspVO>();
            listIdenEsp = MeLanbide50Manager.getInstance().getDatosIdentificacionEsp(numExpediente, datoEsp.getId(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listIdenEsp.size() == 1) {
                for (IdentificacionEspVO identificacionesp : listIdenEsp) {
                    identEspecialidad = identificacionesp;
                }
                nuevo = "0";
            } else if (listIdenEsp.size() > 1) {
                log.error("SE han recuperado mas de un registro sobre detalles de la especialidad" + datoEsp.getNumExp() + " -- " + datoEsp.getId());
            } else {
                log.error("Se ha presentado un error al  recueperado datos sobre detalles de la especialidad" + datoEsp.getNumExp() + " -- " + datoEsp.getId());
            }

            request.setAttribute("nuevo", nuevo);
            request.setAttribute("datModif", identEspecialidad);

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide50/operaciones/nuevaIdentificacionEsp.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<IdentificacionEspVO> lista = new ArrayList<IdentificacionEspVO>();

        IdentificacionEspVO datModif = new IdentificacionEspVO();
        boolean modOK = false;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String nuevo = request.getParameter("nuevo");
            String id = request.getParameter("id");
            String idEspSol = request.getParameter("idEspSol");
            String codcp = request.getParameter("codcp");
            String denomesp = request.getParameter("denomesp");
            String horas = request.getParameter("horas");
            horas = horas.replace(",", ".");
            String alumnos = request.getParameter("alumnos");
            String certpro = request.getParameter("certpro");
            String realdecregu = request.getParameter("realdecregu");
            String boefecpub = request.getParameter("boefecpub");
            String descripadapt = request.getParameter("descripadapt");
            String observadapt = request.getParameter("observadapt");

            if (nuevo.equals("1")) {

                datModif.setIdEspSol(Integer.valueOf(idEspSol));
                datModif.setNumExp(numExpediente);
                datModif.setCodCp(codcp);
                datModif.setDenomEsp(denomesp);
                datModif.setHoras((horas != null && !horas.isEmpty() ? new BigDecimal(horas) : null));
                datModif.setAlumnos((alumnos != null && !alumnos.isEmpty() ? new BigDecimal(alumnos) : null));
                datModif.setCertPro((certpro != null && !certpro.isEmpty() && !certpro.equals("null") ? Integer.valueOf(certpro) : null));
                datModif.setRealDecRegu(realdecregu);
                datModif.setBoeFecPub(boefecpub);
                datModif.setDescripAdapt(descripadapt);
                datModif.setObservAdapt(observadapt);

                MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
                modOK = meLanbide50Manager.crearNuevaIdentificacionEsp(datModif, adapt);
            } else if (nuevo.equals("0")) {
                datModif = MeLanbide50Manager.getInstance().getIdentificacionEspPorCodigo(numExpediente, id, idEspSol, adapt);
                datModif.setIdEspSol(Integer.valueOf(idEspSol));
                datModif.setNumExp(numExpediente);
                datModif.setCodCp(codcp);
                datModif.setDenomEsp(denomesp);
                datModif.setHoras((horas != null && !horas.isEmpty() ? new BigDecimal(horas) : null));
                datModif.setAlumnos((alumnos != null && !alumnos.isEmpty() ? new BigDecimal(alumnos) : null));
                datModif.setCertPro((certpro != null && !certpro.isEmpty() && !certpro.equals("null") ? Integer.valueOf(certpro) : null));
                datModif.setRealDecRegu(realdecregu);
                datModif.setBoeFecPub(boefecpub);
                datModif.setDescripAdapt(descripadapt);
                datModif.setObservAdapt(observadapt);

                MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
                modOK = meLanbide50Manager.modificarIdentificacionEsp(datModif, adapt);
            } else {
                codigoOperacion = "2";
                log.error("No se ha podido recuperar el codigo de NUEVO para gestionar alta o modificaicon de datos de identifiacion de especialidad");
            }

            if (modOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosIdentificacionEsp(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaIdentificacionEsp(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<IdentificacionEspVO> lista = new ArrayList<IdentificacionEspVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String idEspSol = request.getParameter("idEspSol");
            String codcp = request.getParameter("codcp");
            String denomesp = request.getParameter("denomesp");
            String horas = request.getParameter("horas");
            horas = horas.replace(",", ".");
            String alumnos = request.getParameter("alumnos");
            String certpro = request.getParameter("certpro");
            String realdecregu = request.getParameter("realdecregu");
            String boefecpub = request.getParameter("boefecpub");

            IdentificacionEspVO datModif = MeLanbide50Manager.getInstance().getIdentificacionEspPorCodigo(numExpediente, id, idEspSol, adapt);
            datModif.setId(Integer.valueOf(id));
            datModif.setIdEspSol(Integer.valueOf(idEspSol));
            datModif.setNumExp(numExpediente);
            datModif.setCodCp(codcp);
            datModif.setDenomEsp(denomesp);
            datModif.setHoras((horas != null && !horas.isEmpty() ? new BigDecimal(horas) : null));
            datModif.setAlumnos((alumnos != null && !alumnos.isEmpty() ? new BigDecimal(alumnos) : null));
            datModif.setCertPro((certpro != null && !certpro.isEmpty() && !certpro.equals("null") ? Integer.valueOf(certpro) : null));
            datModif.setRealDecRegu(realdecregu);
            datModif.setBoeFecPub(boefecpub);

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean modOK = meLanbide50Manager.modificarIdentificacionEsp(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosIdentificacionEsp(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaIdentificacionEsp(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void actualizarDatosPantallaIdentificacionEsp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<IdentificacionEspVO> lista = new ArrayList<IdentificacionEspVO>();

        String idEpsol = request.getParameter("idEpsol");
        Integer idEpsolint = (idEpsol != null && !idEpsol.isEmpty() ? Integer.valueOf(idEpsol) : 0);
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide50Manager.getInstance().getDatosIdentificacionEsp(numExpediente, idEpsolint, adapt);

        } catch (Exception ex) {
            log.debug("Error al recuperar los datos de las Identifiacion  al Actualizar los datos de la pantalla despues de MOdificar - eliminar - Alta en lista especialidades solicitadas");
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaIdentificacionEsp(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void actualizarDatosPantallaDisponibilidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DisponibilidadVO> lista = new ArrayList<DisponibilidadVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide50Manager.getInstance().getDatosDisponibilidad(numExpediente, adapt);

        } catch (Exception ex) {
            log.error("Error al recuperar los datos de las disponibildades al Actualizar los datos de l pantalla despues de MOdificar - eliminar - Alta en lista especialidades solicitadas");
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaDisponibilidad(codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public String cargarNuevaCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");
            String idEpsol = request.getParameter("idEpsol");
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide50/operaciones/nuevaCapacidad.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<CapacidadVO> lista = new ArrayList<CapacidadVO>();
        CapacidadVO nuevoDato = new CapacidadVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String idEpsol = request.getParameter("idEpsol");
            String identificacionespfor = request.getParameter("identificacionespfor");
            String ubicacion = request.getParameter("ubicacion");
            String superficie = request.getParameter("superficie");
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

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean insertOK = meLanbide50Manager.crearNuevaCapacidad(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosCapacidad(numExpediente, nuevoDato.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.error("No se ha podido Insertar Nueva capcidad de instalaciones para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaCapacidad(codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public String cargarModifCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");

            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

            if (id != null && !id.isEmpty()) {
                CapacidadVO datModif = MeLanbide50Manager.getInstance().getCapacidadPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide50/operaciones/nuevaCapacidad.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<CapacidadVO> lista = new ArrayList<CapacidadVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");
            String identificacionespfor = request.getParameter("identificacionespfor");
            String ubicacion = request.getParameter("ubicacion");
            String superficie = request.getParameter("superficie");
            superficie = superficie.replace(",", ".");

            CapacidadVO datModif = MeLanbide50Manager.getInstance().getCapacidadPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.valueOf(id));
            datModif.setNumExp(numExpediente);
            datModif.setIdetificacionEspFor(identificacionespfor);
            datModif.setUbicacion(ubicacion);
            //datModif.setSuperficie((!superficie.equals("") ? new BigDecimal(superficie) : null));
            datModif.setSuperficie((!superficie.isEmpty() ? superficie : null));

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean modOK = meLanbide50Manager.modificarCapacidad(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosCapacidad(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaCapacidad(codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public void eliminarCapacidad(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<CapacidadVO> lista = new ArrayList<CapacidadVO>();
        try {
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");
            Integer idEpsolint = (idEpsol != null && !idEpsol.isEmpty() ? Integer.valueOf(idEpsol) : 0);
            Integer idS = null;
            if (id == null || id.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idS = Integer.valueOf(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idS != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide50Manager.getInstance().eliminarCapacidad(numExpediente, idS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide50Manager.getInstance().getDatosCapacidad(numExpediente, idEpsolint, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaCapacidad(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public String cargarNuevaDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");
            String idEpsol = request.getParameter("idEpsol");
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide50/operaciones/nuevaDotacion.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DotacionVO> lista = new ArrayList<DotacionVO>();
        DotacionVO nuevoDato = new DotacionVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String idEpsol = request.getParameter("idEpsol");
            String cantidad = request.getParameter("cantidad");
            String denominacionET = request.getParameter("denominacionET");
            String meLanbide50FechaAdqui = request.getParameter("meLanbide50FechaAdqui");

            if (!idEpsol.isEmpty() && !idEpsol.equals("")) {
                nuevoDato.setIdEspSol(Integer.valueOf(idEpsol));
            } else {
                nuevoDato.setIdEspSol(null);
            }
            nuevoDato.setNumExp(numExpediente);
            //nuevoDato.setCantidad(Integer.valueOf(cantidad));
            nuevoDato.setCantidad(cantidad);
            nuevoDato.setDenominacionET(denominacionET);

            //SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");
            //nuevoDato.setFechaAdq(new java.sql.Date(formatoFecha.parse(meLanbide50FechaAdqui).getTime()));
            /*String[] fechaArray = meLanbide50FechaAdqui.split("/");
             String anyo=fechaArray[2].substring(2, 4);
             String dateAdq=fechaArray[0]+"/"+fechaArray[1]+"/"+anyo;*/
            nuevoDato.setFechaAdq(meLanbide50FechaAdqui);

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean insertOK = meLanbide50Manager.crearNuevaDotacion(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosDotacion(numExpediente, nuevoDato.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.debug("No se ha podido Insertar Nueva Dotacion  para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaDotacion(codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public String cargarModifDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");

            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

            if (id != null && !id.isEmpty()) {
                DotacionVO datModif = MeLanbide50Manager.getInstance().getDotacionPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide50/operaciones/nuevaDotacion.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DotacionVO> lista = new ArrayList<DotacionVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");
            String cantidad = request.getParameter("cantidad");
            String denominacionET = request.getParameter("denominacionET");
            String meLanbide50FechaAdqui = request.getParameter("meLanbide50FechaAdqui");

            DotacionVO datModif = MeLanbide50Manager.getInstance().getDotacionPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.valueOf(id));
            datModif.setNumExp(numExpediente);
            //datModif.setCantidad(Integer.valueOf(cantidad));
            datModif.setCantidad(cantidad);
            datModif.setDenominacionET(denominacionET);
            //SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            //datModif.setFechaAdq(new java.sql.Date(formatoFecha.parse(meLanbide50FechaAdqui).getTime()));
            /*String[] fechaArray = meLanbide50FechaAdqui.split("/");
             String anyo=fechaArray[2].substring(2, 4);
             String dateAdq=fechaArray[0]+"/"+fechaArray[1]+"/"+anyo;*/
            datModif.setFechaAdq(meLanbide50FechaAdqui);

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean modOK = meLanbide50Manager.modificarDotacion(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosDotacion(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
log.debug("Modificado");
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaDotacion(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void eliminarDotacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<DotacionVO> lista = new ArrayList<DotacionVO>();
        try {
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");
            Integer idEpsolint = (idEpsol != null && !idEpsol.isEmpty() ? Integer.valueOf(idEpsol) : 0);
            Integer idS = null;
            if (id == null || id.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idS = Integer.valueOf(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idS != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide50Manager.getInstance().eliminarDotacion(numExpediente, idS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide50Manager.getInstance().getDatosDotacion(numExpediente, idEpsolint, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaDotacion(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public String cargarNuevoMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "1");
            String idEpsol = request.getParameter("idEpsol");
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide50/operaciones/nuevoMaterial.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MaterialVO> lista = new ArrayList<MaterialVO>();
        MaterialVO nuevoDato = new MaterialVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String idEpsol = request.getParameter("idEpsol");
            String cantidad = request.getParameter("cantidad");
            String denominacionET = request.getParameter("denominacionET");

            if (!idEpsol.isEmpty() && !idEpsol.equals("")) {
                nuevoDato.setIdEspSol(Integer.valueOf(idEpsol));
            } else {
                nuevoDato.setIdEspSol(null);
            }
            nuevoDato.setNumExp(numExpediente);
            nuevoDato.setCantidad(Integer.valueOf(cantidad));
            nuevoDato.setDenominacionET(denominacionET);

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean insertOK = meLanbide50Manager.crearNuevoMaterial(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosMaterial(numExpediente, nuevoDato.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.debug("No se ha podido Insertar Nuevo Material   para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaMaterial(codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public String cargarModifMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");

            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExpediente, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

            if (id != null && !id.isEmpty()) {
                MaterialVO datModif = MeLanbide50Manager.getInstance().getMaterialPorCodigo(numExpediente, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {

        }
        return "/jsp/extension/melanbide50/operaciones/nuevoMaterial.jsp?codOrganizacionModulo=" + codOrganizacion;

    }

    public void modificarMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MaterialVO> lista = new ArrayList<MaterialVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");
            String cantidad = request.getParameter("cantidad");
            String denominacionET = request.getParameter("denominacionET");

            MaterialVO datModif = MeLanbide50Manager.getInstance().getMaterialPorCodigo(numExpediente, id, adapt);
            datModif.setId(Integer.valueOf(id));
            datModif.setNumExp(numExpediente);
            datModif.setCantidad(Integer.valueOf(cantidad));
            datModif.setDenominacionET(denominacionET);

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean modOK = meLanbide50Manager.modificarMaterial(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosMaterial(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaMaterial(codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public void eliminarMaterial(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MaterialVO> lista = new ArrayList<MaterialVO>();
        try {
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");
            Integer idEpsolint = (idEpsol != null && !idEpsol.isEmpty() ? Integer.valueOf(idEpsol) : 0);
            Integer idS = null;
            if (id == null || id.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idS = Integer.valueOf(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idS != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide50Manager.getInstance().eliminarMaterial(numExpediente, idS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide50Manager.getInstance().getDatosMaterial(numExpediente, idEpsolint, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaMaterial(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public String cargarNuevoEspacio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            log.debug("Exp " + numExpediente);
            request.setAttribute("nuevo", "1");
            String idEpsol = request.getParameter("idEpsol");
            String numExp = request.getParameter("numero");
            log.debug("Exp " + numExp);
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExp, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);
            request.setAttribute("numero", numExp);
        } catch (Exception ex) {
            log.error("error cargarNuevoEspacio ", ex);

        }
        return "/jsp/extension/melanbide50/operaciones/nuevoEspacio.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarModificarEspacio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setAttribute("nuevo", "0");
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");
            String numExp = request.getParameter("numero");
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if (idEpsol != null && !idEpsol.isEmpty()) {
                datoEspecialidad = MeLanbide50Manager.getInstance().getEspecialidadPorCodigo(numExp, idEpsol, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            request.setAttribute("datoEspecialidad", datoEspecialidad);

            if (id != null && !id.isEmpty()) {
                EspacioVO datModif = MeLanbide50Manager.getInstance().getEspacioPorCodigo(numExp, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            request.setAttribute("numero", numExp);
        } catch (Exception ex) {
            log.error("error cargarModificarEspacio ", ex);
        }
        return "/jsp/extension/melanbide50/operaciones/nuevoEspacio.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void crearEspacio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspacioVO> lista = new ArrayList<EspacioVO>();
        EspacioVO nuevoDato = new EspacioVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String idEpsol = request.getParameter("idEpsol");
            String denominacion = request.getParameter("denominacion");
            String espaciosAcred = request.getParameter("espaciosAcred");
            String espaciosSolic = request.getParameter("espaciosSolic");
            String alumnosAcred = request.getParameter("alumnosAcred");
            String alumnosSolic = request.getParameter("alumnosSolic");

            if (!idEpsol.isEmpty()) {
                nuevoDato.setIdEspSol(Integer.valueOf(idEpsol));
            } else {
                nuevoDato.setIdEspSol(null);
            }
            nuevoDato.setNumExp(numExpediente);
            nuevoDato.setDenominacion(denominacion);
            nuevoDato.setEspAcred(espaciosAcred);
            nuevoDato.setEspAutor(espaciosSolic);
            if (!alumnosAcred.isEmpty()) {
                nuevoDato.setNumAlumnos(Integer.valueOf(alumnosAcred));
            } else {
                nuevoDato.setNumAlumnos(null);
            }
            if (!alumnosSolic.isEmpty()) {
                nuevoDato.setAlumNuevos(Integer.valueOf(alumnosSolic));
            } else {
                nuevoDato.setAlumNuevos(null);
            }

            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean insertOK = meLanbide50Manager.crearNuevoEspacio(nuevoDato, adapt);
            if (insertOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosEspacio(numExpediente, nuevoDato.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log.debug("No se ha podido Insertar Nuevo Espacio para el expediente : " + numExpediente);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaEspacio(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarEspacio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspacioVO> lista = new ArrayList<EspacioVO>();
        EspacioVO datModif = new EspacioVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");
            String denominacion = request.getParameter("denominacion");
            String espaciosAcred = request.getParameter("espaciosAcred");
            String espaciosSolic = request.getParameter("espaciosSolic");
            String alumnosAcred = request.getParameter("alumnosAcred");
            String alumnosSolic = request.getParameter("alumnosSolic");

            datModif.setId(Integer.valueOf(id));
            datModif.setIdEspSol(Integer.valueOf(idEpsol));
            datModif.setNumExp(numExpediente);
            datModif.setDenominacion(denominacion);
            datModif.setEspAcred(espaciosAcred);
            datModif.setEspAutor(espaciosSolic);
            if (!alumnosAcred.isEmpty()) {
                datModif.setNumAlumnos(Integer.valueOf(alumnosAcred));
            } else {
                datModif.setNumAlumnos(null);
            }
            if (!alumnosSolic.isEmpty()) {
                datModif.setAlumNuevos(Integer.valueOf(alumnosSolic));
            } else {
                datModif.setAlumNuevos(null);
            }
            MeLanbide50Manager meLanbide50Manager = MeLanbide50Manager.getInstance();
            boolean modOK = meLanbide50Manager.modificarEspacio(datModif, adapt);
            if (modOK) {
                try {
                    lista = MeLanbide50Manager.getInstance().getDatosEspacio(numExpediente, datModif.getIdEspSol(), adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, bde);
                } catch (Exception ex) {
                    codigoOperacion = "2";
                    java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                codigoOperacion = "2";
            }
        } catch (NumberFormatException e) {
            codigoOperacion = "2";
        } catch (SQLException e) {
            codigoOperacion = "1";
        } catch (Exception ex) {
            codigoOperacion = "2";
        }
        log.debug(codigoOperacion);
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaEspacio(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void eliminarEspacio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<EspacioVO> lista = new ArrayList<EspacioVO>();
        try {
            String id = request.getParameter("id");
            String idEpsol = request.getParameter("idEpsol");
            Integer idEpsolInt = (idEpsol != null && !idEpsol.isEmpty() ? Integer.valueOf(idEpsol) : 0);
            Integer idS = null;
            if (id == null || id.isEmpty()) {
                codigoOperacion = "3";
            } else {
                try {
                    idS = Integer.valueOf(id);
                } catch (NumberFormatException ex) {
                    codigoOperacion = "3";
                }
                if (idS != null) {
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    int result = MeLanbide50Manager.getInstance().eliminarEspacio(numExpediente, idS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if (result <= 0) {
                        codigoOperacion = "1";
                    } else {
                        codigoOperacion = "0";
                        try {
                            lista = MeLanbide50Manager.getInstance().getDatosEspacio(numExpediente, idEpsolInt, adapt);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(MELANBIDE50.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    codigoOperacion = "3";
                }
            }
        } catch (Exception e) {
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaEspacio(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    /*   XMLs Respuesta   */
    private String obtenerXmlSalidaEspecialidades(String codigoOperacion, List<EspecialidadesVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (EspecialidadesVO fila : lista) {
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
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaServicio(String codigoOperacion, List<ServiciosVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
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
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaDisponibilidad(String codigoOperacion, List<DisponibilidadVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
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
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaCapacidad(String codigoOperacion, List<CapacidadVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
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
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaIdentificacionEsp(String codigoOperacion, List<IdentificacionEspVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
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
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaDotacion(String codigoOperacion, List<DotacionVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
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
        log.debug(xmlSalida);
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaMaterial(String codigoOperacion, List<MaterialVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
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
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaEspacio(String codigoOperacion, List<EspacioVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (EspacioVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<ID_ESPSOL>");
            xmlSalida.append(fila.getIdEspSol() != null ? fila.getIdEspSol().toString() : "");
            xmlSalida.append("</ID_ESPSOL>");
            xmlSalida.append("<NESP_DENESP>");
            xmlSalida.append(fila.getDenominacion() != null ? fila.getDenominacion() : "");
            xmlSalida.append("</NESP_DENESP>");
            xmlSalida.append("<NESP_ESPACRED>");
            xmlSalida.append(fila.getEspAcred() != null ? fila.getEspAcred() : "");
            xmlSalida.append("</NESP_ESPACRED>");
            xmlSalida.append("<NESP_ESPAUT>");
            xmlSalida.append(fila.getEspAutor() != null ? fila.getEspAutor() : "");
            xmlSalida.append("</NESP_ESPAUT>");
            xmlSalida.append("<NESP_ALUM>");
            xmlSalida.append(fila.getNumAlumnos() != null ? fila.getNumAlumnos().toString() : "");
            xmlSalida.append("</NESP_ALUM>");
            xmlSalida.append("<NESP_ALUMNUEV>");
            xmlSalida.append(fila.getAlumNuevos() != null ? fila.getAlumNuevos().toString() : "");
            xmlSalida.append("</NESP_ALUMNUEV>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug(xmlSalida);
        return xmlSalida.toString();
    }

    /**
     * Operacion que retorna el XML
     *
     * @param salida
     * @param response
     */
    private void retornarXML(String salida, HttpServletResponse response) {
        try {
            if (salida != null) {
                log.debug("retornarXML");
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(salida);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
        }
    }

    /**
     * Operación que permite ampliar las especialidades de un centro a través
     * del servicio web de registro de centros y especialidades
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
     * "4" --> El campo suplementario de expediente con el código de centro no tiene valor 
     * "5" --> No se ha podido obtener el valor del campo "Fecha de resolución" en ninguno de los trámites en los que se encuentra 
     * "6" --> No se ha podido obtener una conexión a la BBDD
     * "7" --> No se ha podido dar de alta el centro con la especialidades en el servicio web de centros de formación 
     * "8" --> Se ha producido un error técnico durante la ejecución de la operación
     * "9" --> Es obligatorio seleccionar las especialidades del centro
     * "10" --> El campo suplementario de expediente con el código de ubicación no tiene valor
     * "11" --> No se ha podido recuperar el número de censo
     * "13" --> Error al obtener corrServicio o corrSubservicio
     * "14" --> Error al obtener corrServicio
     * "15" --> Error al obtener corrSubservicio
     * @throws es.altia.util.conexion.BDException
     */
    public String ampliacionEspecialidadesCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info(this.getClass().getSimpleName() + ".ampliacionEspecialidadesCentro  =================> " + numExpediente);

        String salida = null;
        boolean conexionWS = false;
        boolean propiedadesRecuperadas = false;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        WSRegistroCentrosFormacionPortBindingStub binding = null;
        String codigoCampoCodCentro = null;
        String codigoCampoCodUbicacion = null;
        //String codigoCampoCodNumeroCenso = null;               
        String codigoCampoFechaResolucion = null;
        String codigoTramiteResolucionPositivaMixta = null;
        String codigoTramiteResolucionNegativa = null;
        String codigoTramiteResolucionPosNegInspeccion = null;
        String urlServicioWeb = null;

        try {
            codigoCampoCodCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO", this.getNombreModulo());
            codigoCampoCodUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION", this.getNombreModulo());
            codigoCampoFechaResolucion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TRAMITE_FECHA_RESOLUCION", this.getNombreModulo());
            codigoTramiteResolucionPositivaMixta = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_POSITIVA_MIXTA", this.getNombreModulo());
            codigoTramiteResolucionNegativa = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_DESISTIMIENTO_NEGATIVA", this.getNombreModulo());
            codigoTramiteResolucionPosNegInspeccion = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_POSNEG_INSPECCION", this.getNombreModulo());
            urlServicioWeb = ConfigurationParameter.getParameter("URL_WS_REGISTROCENTROS", this.getNombreModulo());

            log.info("codigoCampoCodCentro: " + codigoCampoCodCentro);
            log.info("codigoCampoCodUbicacion: " + codigoCampoCodUbicacion);
            log.info("codigoCampoFechaResolucion: " + codigoCampoFechaResolucion);
            log.info("codigoTramiteResolucionPositivaMixta: " + codigoTramiteResolucionPositivaMixta);
            log.info("codigoTramiteResolucionNegativa: " + codigoTramiteResolucionNegativa);
            log.info("codigoTramiteResolucionPosNegInspeccion: " + codigoTramiteResolucionPosNegInspeccion);
            log.info("urlServicioWeb: " + urlServicioWeb);

            propiedadesRecuperadas = true;
        } catch (Exception e) {
            log.error(this.getClass().getName() + ".ampliacionEspecialidadesCentro():  Error al recuperar valores de propiedades del fichero de configuración: " + e.getMessage());
            salida = "1";
        }

        if (propiedadesRecuperadas) {
            if (!MeLanbide50Utils.isInteger(codigoTramiteResolucionPositivaMixta)
                    || !MeLanbide50Utils.isInteger(codigoTramiteResolucionNegativa)
                    || !MeLanbide50Utils.isInteger(codigoTramiteResolucionPosNegInspeccion)
                    || codigoCampoCodCentro == null || codigoCampoCodUbicacion == null || codigoCampoFechaResolucion == null
                    || codigoTramiteResolucionPositivaMixta == null || codigoTramiteResolucionNegativa == null || codigoTramiteResolucionPosNegInspeccion == null) {
                log.error("ampliacionEspecialidadesCentro salida 2");
                salida = "2";
            } else {
                log.info("ampliacionEspecialidadesCentro antes binding");
                try {
                    binding = (WSRegistroCentrosFormacionPortBindingStub) new WSRegistroCentrosFormacionServiceLocator().getWSRegistroCentrosFormacionPort(new java.net.URL(urlServicioWeb));
                    conexionWS = true;
                } catch (MalformedURLException e) {
                    log.error(this.getClass().getName() + ".ampliacionEspecialidadesCentro(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                } catch (ServiceException e) {
                    log.error(this.getClass().getName() + ".ampliacionEspecialidadesCentro(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                }
                log.info("ampliacionEspecialidadesCentro después binding");

                if (!conexionWS) {
                    salida = "3";
                } else {
                    // LLAMADA AL SERVICIO WEB 
                    try {
                        log.info("ampliacionEspecialidadesCentro ini llamada WS");

                        String[] datosExp = numExpediente.split("/");
                        String ejercicio = datosExp[0];
                        String codProcedimiento = datosExp[1];

                        log.info("ampliacionEspecialidadesCentro ejercicio " + ejercicio);
                        log.info("ampliacionEspecialidadesCentro codProcedimiento " + codProcedimiento);
                        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

                        log.info("ampliacionEspecialidadesCentro recuperar valor CODCENTRO ");
                        // SE RECUPERA EL VALOR DEL CAMPO "CODCENTRO" DEFINIDO A NIVEL DE EXPEDIENTE
                        SalidaIntegracionVO salidaCampoCodCentro = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codigoCampoCodCentro, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                        log.info("ampliacionEspecialidadesCentro recuperar valor CODUBICACION ");
                        // SE RECUPERA EL VALOR DEL CAMPO "CODUBICACION" DEFINIDO A NIVEL DE EXPEDIENTE
                        SalidaIntegracionVO salidaCampoCodUbicacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codigoCampoCodUbicacion, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                        log.info("ampliacionEspecialidadesCentro recuperar valor FECRESOLUCIONP ");
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION DESISTIMIENTO NEGATIVA"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionNegativa = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionNegativa), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                        log.info("ampliacionEspecialidadesCentro recuperar valor FECRESOLUCIONN ");
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION POSITIVA MIXTA"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionPositiva = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionPositivaMixta), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                        log.info("ampliacionEspecialidadesCentro recuperar valor FECRESOLUCIONN ");
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION POSITIVA MIXTA"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionPosNegInspeccion = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionPosNegInspeccion), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                        //TODO: añadir aquí la consulta de fecResolucionINspeccion (trámite con código 12!!!!!!!!!!!!)
                        log.info("salidaCampoCodCentro.getStatus(): " + salidaCampoCodCentro.getStatus());
                        log.info("salidaCampoCodUbicacion.getStatus(): " + salidaCampoCodUbicacion.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionNegativa.getStatus(): " + salidaFecResolucionTramiteResolucionNegativa.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionPositiva.getStatus(): " + salidaFecResolucionTramiteResolucionPositiva.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus(): " + salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus());

                        if (salidaCampoCodCentro.getStatus() != 0) {
                            salida = "4";
                        } else if (salidaCampoCodUbicacion.getStatus() != 0) {
                            salida = "10";
                        } else if (salidaFecResolucionTramiteResolucionNegativa.getStatus() != 0 && salidaFecResolucionTramiteResolucionPositiva.getStatus() != 0 && salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus() != 0) {
                            log.error("ampliacionEspecialidadesCentro salida 5");
                            salida = "5";
                        } else {

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
                                log.error("ampliacionEspecialidadesCentro salida 6");
                                salida = "6";
                            } else {

                                ExpedienteVO expVO = new ExpedienteVO();
                                expVO.setNumExpediente(numExpediente);

                                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
                                if (salidaFecResolucionTramiteResolucionNegativa.getStatus() == 0 && salidaFecResolucionTramiteResolucionNegativa.getCampoSuplementario() != null) {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionNegativa.getCampoSuplementario().getValorFecha().getTime()));
                                } else if (salidaFecResolucionTramiteResolucionPositiva.getStatus() == 0 && salidaFecResolucionTramiteResolucionPositiva.getCampoSuplementario() != null) {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionPositiva.getCampoSuplementario().getValorFecha().getTime()));
                                } else {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionPosNegInspeccion.getCampoSuplementario().getValorFecha().getTime()));
                                }
                                log.info("Fecha expediente: " + expVO.getFechaResolucion());

                                // Lista de especialidades
                                ArrayList<EspecialidadesVO> especialidadesExpediente = (ArrayList<EspecialidadesVO>) MeLanbide50DAO.getInstance().getDatosEspecialidadesAcreditadas(numExpediente, con);
                                net.lanbide.formacion.ws.regexlan.EspecialidadVO[] especialidadesVO = null;
                                net.lanbide.formacion.ws.regexlan.ListaEspecialidades listaEspecialidades = null;

                                if (especialidadesExpediente != null && !especialidadesExpediente.isEmpty()) {
                                    especialidadesVO = new net.lanbide.formacion.ws.regexlan.EspecialidadVO[especialidadesExpediente.size()];
                                    listaEspecialidades = new net.lanbide.formacion.ws.regexlan.ListaEspecialidades();
                                }

                                boolean errorNumeroAlumnosEspecialidad = false;
                                if (especialidadesExpediente != null && !especialidadesExpediente.isEmpty()) {

                                    for (int i = 0; i < especialidadesExpediente.size(); i++) {
                                        EspecialidadesVO especialidad = especialidadesExpediente.get(i);

                                        net.lanbide.formacion.ws.regexlan.EspecialidadVO aux = new EspecialidadVO();
                                        aux.setCodigo(especialidad.getCodCP());
                                        aux.setTipo("1");

                                        IdentificacionEspVO idenEsp = MeLanbide50DAO.getInstance().getIdentificacionEspPorCodigoEspSol_NumExp(numExpediente, especialidad.getId(), con);
                                        if (idenEsp.getAlumnos() != null) {
                                            aux.setNumPersonas(idenEsp.getAlumnos().intValue());
                                            especialidadesVO[i] = aux;
                                        } else {
                                            log.error("No se ha podido recuperar de BBDD el número de alumnos de la especialidad " + especialidad.getId());
                                            errorNumeroAlumnosEspecialidad = true;
                                            break;
                                        }
                                        especialidadesVO[i] = aux;
                                    }
                                    listaEspecialidades.setEspecialidad(especialidadesVO);
                                    log.info(" =======================> codCentro: " + salidaCampoCodCentro.getCampoSuplementario().getValorTexto());
                                    log.info(" =======================> codCodUbicacion: " + salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto());
                                    String[] datosCorr = null;
                                    datosCorr = MeLanbide50Manager.getInstance().getCodigosCorr(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), adapt);
                                    if (datosCorr == null || datosCorr.length == 0) {
                                        salida = "13";
                                    } else {
                                        if (datosCorr[0] == null || datosCorr[0].isEmpty()) {
                                            log.error("NO hay corrServicio");
                                            // corrServicio
                                            salida = "14";
                                        } else if (datosCorr[1] == null || datosCorr[1].isEmpty()) {
                                            // corrSubservicio 
                                            log.error("NO hay corrSUBServicio");
                                            salida = "15";
                                        } else {
                                            log.info("hay corrSUBServicio: " + datosCorr[1]);
                                            //centro.setCorrSubservicio(datosCorr[1]);

                                            // Se recupera el número de censo de la tabla LAN_CENTROS_SERVICIOS a partir del código de centro y del código de ubicación
//                                            String numeroCenso = MeLanbide50DAO.getInstance().getNumeroCenso(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), con);
                                            String numeroCenso = MeLanbide50Manager.getInstance().getNumeroCensoCenFor(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), datosCorr[1], adapt);
                                            if (errorNumeroAlumnosEspecialidad) {
                                                log.error("ampliacionEspecialidadesCentro salida 12");
                                                salida = "12";
                                            } else if (numeroCenso == null || numeroCenso.length() == 0) {
                                                log.error("ampliacionEspecialidadesCentro salida 11");
                                                salida = "11";
                                            } else {

                                                /**
                                                 * ***
                                                 */
                                                net.lanbide.formacion.ws.regexlan.AmpliacionEspecialidadesCentroRequest ampliacionEspecialidadesCentroRequest = new AmpliacionEspecialidadesCentroRequest();

                                                ampliacionEspecialidadesCentroRequest.setExpediente(expVO);
                                                ampliacionEspecialidadesCentroRequest.setListaEspecialidades(listaEspecialidades);
                                                ampliacionEspecialidadesCentroRequest.setNumCensoLB(numeroCenso);

                                                net.lanbide.formacion.ws.regexlan.AmpliacionEspecialidadesCentro ampliacionEspecialidadesCentro = new AmpliacionEspecialidadesCentro();
                                                ampliacionEspecialidadesCentro.setAmpliacionEspecialidadesCentroRequest(ampliacionEspecialidadesCentroRequest);
                                                log.info("ampliacionEspecialidadesCentro fechaResolucion " + ampliacionEspecialidadesCentroRequest.getExpediente().getFechaResolucion());
                                                log.info("ampliacionEspecialidadesCentro numExpediente " + ampliacionEspecialidadesCentroRequest.getExpediente().getNumExpediente());
                                                log.info("ampliacionEspecialidadesCentro numeroCenso CENFOR " + ampliacionEspecialidadesCentroRequest.getNumCensoLB());
                                                if (ampliacionEspecialidadesCentroRequest.getListaEspecialidades() != null) {
                                                    for (EspecialidadVO especialidad : ampliacionEspecialidadesCentroRequest.getListaEspecialidades().getEspecialidad()) {
                                                        log.info("ampliacionEspecialidadesCentro especialidad codigo " + especialidad.getCodigo());
                                                        log.info("ampliacionEspecialidadesCentro especialidad tipo " + especialidad.getTipo());
                                                        log.info("ampliacionEspecialidadesCentro especialidad numPersonas " + especialidad.getNumPersonas());
                                                    }
                                                }
                                                net.lanbide.formacion.ws.regexlan.WsRegistroCentrosFormacionResultado respuesta = binding.ampliacionEspecialidadesCentro(ampliacionEspecialidadesCentroRequest);
//                                                net.lanbide.formacion.ws.regexlan.AmpliacionEspecialidadesCentroResponse respuesta = binding.ampliacionEspecialidadesCentro(ampliacionEspecialidadesCentro);
                                                if (respuesta != null) {
                                                    log.info("respuesta.get_return().getCodRdo(): " + respuesta.getCodRdo());
                                                    log.info("respuesta.get_return().getDescRdo(): " + respuesta.getDescRdo());
                                                    if (respuesta.getCodRdo().equals(0)) {
                                                        log.info("ampliacionEspecialidadesCentro salida 0");
                                                        salida = "0";
                                                    } else {
                                                        log.error("ampliacionEspecialidadesCentro salida 7");
                                                        salida = "7";
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    log.error("ampliacionEspecialidadesCentro salida 9");
                                    salida = "9";
                                }
                            }

                        }// else
                        log.info("ampliacionEspecialidadesCentro fin llamada WS");

                    } catch (BDException e) {
                        log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                        salida = "6";
                    } catch (org.apache.axis.AxisFault e) {
                        log.error("Error al conectarse al servicio web: " + e.getMessage());
                        salida = "3";
                    } catch (Exception e) {
                        log.error("Error técnico durante la ejecución de la operación: " + e.getMessage());
                        salida = "8";
                    } finally {
                        if (adapt != null) {
                            adapt.devolverConexion(con);
                        }
                    }
                }
            }// else    
        }// if(propiedadesRecuperadas)

        log.info(this.getClass().getName() + ".ampliacionEspecialidadesCentro() salida: " + salida + " <======================");
        return salida;
    }

    /**
     * Operación que da actualiza el registro de centros el dato de alumnos
     * autorizados en una o varias especialidades asociadas a un centro de
     * especialidades
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
     * "4" --> El campo suplementario de expediente con el código de centro no tiene valor
     * "5" --> No se ha podido obtener el valor del campo "Fecha de resolución" en ninguno de los trámites en los que se encuentra
     * "6" --> No se ha podido obtener una conexión a la BBDD
     * "7" --> No se ha podido dar de alta el centro con la especialidades en el servicio web de centros de formación
     * "8" --> Se ha producido un error técnico durante la ejecución de la operación
     * "9" --> Es obligatorio seleccionar las especialidades del centro
     * "10" --> Es obligatorio que las especialidades tengan un número de alumnos
     * "11" --> Es obligatorio que el campo código de la ubicación tenga un valor
     * "12" --> Error, no se ha podido recuperar el número de censo
     * "13" --> Error al obtener corrServicio o corrSubservicio
     * "14" --> Error al obtener corrServicio
     * "15" --> Error al obtener corrSubservicio
     * @throws es.altia.util.conexion.BDException
     */
    public String ampliacionAlumnadoEspecialidadesCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info(this.getClass().getName() + ".ampliacionAlumnadoEspecialidadesCentro  =================>");

        String salida = null;
        boolean conexionWS = false;
        boolean propiedadesRecuperadas = false;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        WSRegistroCentrosFormacionPortBindingStub binding = null;
        //String codigoCampoCodNumeroCenso = null;               
        String codigoCampoCodCentro = null;
        String codigoCampoCodUbicacion = null;
        String codigoCampoFechaResolucion = null;
        String codigoTramiteResolucionPositivaMixta = null;
        String codigoTramiteResolucionNegativa = null;
        String codigoTramiteResolucionPosNegInspeccion = null;
        String urlServicioWeb = null;

        try {
            //codigoCampoCodNumeroCenso    = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMEROCENSO",this.getNombreModulo());           

            codigoCampoCodCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO", this.getNombreModulo());
            codigoCampoCodUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION", this.getNombreModulo());
            codigoCampoFechaResolucion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TRAMITE_FECHA_RESOLUCION", this.getNombreModulo());
            codigoTramiteResolucionPositivaMixta = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_POSITIVA_MIXTA", this.getNombreModulo());
            codigoTramiteResolucionNegativa = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_DESISTIMIENTO_NEGATIVA", this.getNombreModulo());
            codigoTramiteResolucionPosNegInspeccion = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_POSNEG_INSPECCION", this.getNombreModulo());
            urlServicioWeb = ConfigurationParameter.getParameter("URL_WS_REGISTROCENTROS", this.getNombreModulo());

            log.info("codigoCampoCodCentro: " + codigoCampoCodCentro);
            log.info("codigoCampoCodUbicacion: " + codigoCampoCodUbicacion);
            log.info("codigoCampoFechaResolucion: " + codigoCampoFechaResolucion);
            log.info("codigoTramiteResolucionPositivaMixta: " + codigoTramiteResolucionPositivaMixta);
            log.info("codigoTramiteResolucionNegativa: " + codigoTramiteResolucionNegativa);
            log.info("codigoTramiteResolucionPosNegInspeccion: " + codigoTramiteResolucionPosNegInspeccion);
            log.info("urlServicioWeb: " + urlServicioWeb);

            propiedadesRecuperadas = true;
        } catch (Exception e) {
            log.error(this.getClass().getName() + ".ampliacionAlumnadoEspecialidadesCentro():  Error al recuperar valores de propiedades del fichero de configuración: " + e.getMessage());
            salida = "1";
        }

        if (propiedadesRecuperadas) {
            if (!MeLanbide50Utils.isInteger(codigoTramiteResolucionPositivaMixta)
                    || !MeLanbide50Utils.isInteger(codigoTramiteResolucionNegativa)
                    || !MeLanbide50Utils.isInteger(codigoTramiteResolucionPosNegInspeccion)
                    || codigoCampoCodCentro == null || codigoCampoCodUbicacion == null || codigoCampoFechaResolucion == null
                    || codigoTramiteResolucionPositivaMixta == null || codigoTramiteResolucionNegativa == null || codigoTramiteResolucionPosNegInspeccion == null) {
                salida = "2";
            } else {

                try {
                    binding = (WSRegistroCentrosFormacionPortBindingStub) new WSRegistroCentrosFormacionServiceLocator().getWSRegistroCentrosFormacionPort(new java.net.URL(urlServicioWeb));
                    conexionWS = true;
                } catch (MalformedURLException e) {
                    log.error(this.getClass().getName() + ".ampliacionAlumnadoEspecialidadesCentro(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                } catch (ServiceException e) {
                    log.error(this.getClass().getName() + ".ampliacionAlumnadoEspecialidadesCentro(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                }

                if (!conexionWS) {
                    salida = "3";
                } else {
                    // LLAMADA AL SERVICIO WEB 
                    try {

                        String[] datosExp = numExpediente.split("/");
                        String ejercicio = datosExp[0];
                        String codProcedimiento = datosExp[1];

                        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

                        // SE RECUPERA EL VALOR DEL CAMPO "CODCENTRO" DEFINIDO A NIVEL DE EXPEDIENTE
                        SalidaIntegracionVO salidaCampoCodCentro = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codigoCampoCodCentro, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                        // SE RECUPERA EL VALOR DEL CAMPO "CODUBICACION" DEFINIDO A NIVEL DE EXPEDIENTE
                        SalidaIntegracionVO salidaCampoCodUbicacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codigoCampoCodUbicacion, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION DESISTIMIENTO NEGATIVA"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionNegativa = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionNegativa), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION POSITIVA MIXTA"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionPositiva = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionPositivaMixta), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION POSITIVA NEGATIVA CON INSPECCION"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionPosNegInspeccion = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionPosNegInspeccion), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                        log.info("salidaCampoCodCentro.getStatus(): " + salidaCampoCodCentro.getStatus());
                        log.info("salidaCampoCodUbicacion.getStatus(): " + salidaCampoCodUbicacion.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionNegativa.getStatus(): " + salidaFecResolucionTramiteResolucionNegativa.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionPositiva.getStatus(): " + salidaFecResolucionTramiteResolucionPositiva.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus(): " + salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus());

                        if (salidaCampoCodCentro.getStatus() != 0) {
                            salida = "4";
                        } else if (salidaCampoCodUbicacion.getStatus() != 0) {
                            salida = "11";
                        } else if (salidaFecResolucionTramiteResolucionNegativa.getStatus() != 0 && salidaFecResolucionTramiteResolucionPositiva.getStatus() != 0 && salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus() != 0) {
                            salida = "5";
                        } else {
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
                                salida = "6";
                            } else {

                                ExpedienteVO expVO = new ExpedienteVO();
                                expVO.setNumExpediente(numExpediente);

                                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
                                if (salidaFecResolucionTramiteResolucionNegativa.getStatus() == 0 && salidaFecResolucionTramiteResolucionNegativa.getCampoSuplementario() != null) {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionNegativa.getCampoSuplementario().getValorFecha().getTime()));
                                } else if (salidaFecResolucionTramiteResolucionPositiva.getStatus() == 0 && salidaFecResolucionTramiteResolucionPositiva.getCampoSuplementario() != null) {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionPositiva.getCampoSuplementario().getValorFecha().getTime()));
                                } else {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionPosNegInspeccion.getCampoSuplementario().getValorFecha().getTime()));
                                }
                                log.debug("Fecha expediente: " + expVO.getFechaResolucion());

                                // Lista de especialidades
                                ArrayList<EspecialidadesVO> especialidadesExpediente = (ArrayList<EspecialidadesVO>) MeLanbide50DAO.getInstance().getDatosEspecialidades(numExpediente, con);
                                net.lanbide.formacion.ws.regexlan.EspecialidadVO[] especialidadesVO = null;
                                net.lanbide.formacion.ws.regexlan.ListaEspecialidades listaEspecialidades = null;

                                if (especialidadesExpediente != null && !especialidadesExpediente.isEmpty()) {
                                    especialidadesVO = new net.lanbide.formacion.ws.regexlan.EspecialidadVO[especialidadesExpediente.size()];
                                    listaEspecialidades = new net.lanbide.formacion.ws.regexlan.ListaEspecialidades();
                                }

                                if (especialidadesExpediente != null && !especialidadesExpediente.isEmpty()) {

                                    int contadorEspecialidadesConAlumnos = 0;
                                    for (int i = 0; i < especialidadesExpediente.size(); i++) {
                                        EspecialidadesVO especialidad = especialidadesExpediente.get(i);

                                        net.lanbide.formacion.ws.regexlan.EspecialidadVO aux = new EspecialidadVO();
                                        aux.setCodigo(especialidad.getCodCP());

                                        if (especialidad.getAcreditacion() != null && especialidad.getAcreditacion().equals(1)) {
                                            // Acreditada
                                            aux.setTipo("1");
                                        } else if (especialidad.getInscripcionPresencial() != null && especialidad.getInscripcionPresencial().equals(0) && especialidad.getInscripcionTeleformacion() != null && especialidad.getInscripcionTeleformacion().equals(0)) {
                                            // Inscrito en presencial y teleformación
                                            aux.setTipo("2");
                                        } else if (especialidad.getInscripcionPresencial() != null && especialidad.getInscripcionPresencial().equals(0) && especialidad.getInscripcionTeleformacion() == null) {
                                            // Sólo presencial
                                            aux.setTipo("3");
                                        } else if (especialidad.getInscripcionPresencial() == null && especialidad.getInscripcionTeleformacion() != null && especialidad.getInscripcionTeleformacion().equals(0)) {
                                            // Sólo teleformación
                                            aux.setTipo("4");
                                        } else {
                                            aux.setTipo("5");
                                        }

                                        // Se recupera el número de alumnos del código de la especialidad
                                        String numero = MeLanbide50DAO.getInstance().getNumeroAlumnosEspecialidad(numExpediente, aux.getCodigo(), con);
                                        if (numero != null) {
                                            contadorEspecialidadesConAlumnos++;
                                            //aux.setNumPersonas(numero.intValue());
                                            aux.setNumPersonas(Integer.parseInt(numero));
                                            especialidadesVO[i] = aux;
                                        }
                                    }
                                    listaEspecialidades.setEspecialidad(especialidadesVO);

                                    // Se comprueba si el número de especialidades a pasar al servicio web, 
                                    // se corresponde con las recuperadas. Sino hay coincidencia, entonces, es 
                                    // que alguna de las especialidades no tiene definido un número de alumnos.
                                    if (especialidadesExpediente.size() != contadorEspecialidadesConAlumnos) {
                                        salida = "10";
                                    } else {
                                        String[] datosCorr = null;
                                        datosCorr = MeLanbide50Manager.getInstance().getCodigosCorr(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), adapt);
                                        if (datosCorr == null || datosCorr.length == 0) {
                                            salida = "13";
                                        } else {
                                            if (datosCorr[0] == null || datosCorr[0].isEmpty() || datosCorr[0].isEmpty()) {
                                                log.error("NO hay corrServicio");
                                                // corrServicio
                                                salida = "14";
                                            } else if (datosCorr[1] == null || datosCorr[1].isEmpty()) {
                                                // corrSubservicio 
                                                log.error("NO hay corrSUBServicio");
                                                salida = "15";
                                            } else {
                                                log.debug("hay corrSUBServicio: " + datosCorr[1]);
                                                // Se recupera el número de censo de la tabla LAN_CENTROS_SERVICIOS a partir del código de centro y del código de ubicación
                                                String numeroCenso = MeLanbide50Manager.getInstance().getNumeroCensoCenFor(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), datosCorr[1], adapt);
//                                                String numeroCenso = MeLanbide50DAO.getInstance().getNumeroCenso(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), con);
                                                if (numeroCenso == null || numeroCenso.length() == 0) {
                                                    salida = "12";
                                                } else {

                                                    net.lanbide.formacion.ws.regexlan.AmpliacionAlumnadoEspecialidadesCentroRequest ampliacionAlumnadoEspecialidadesRequest = new AmpliacionAlumnadoEspecialidadesCentroRequest();
                                                    ampliacionAlumnadoEspecialidadesRequest.setExpediente(expVO);
                                                    ampliacionAlumnadoEspecialidadesRequest.setListaEspecialidades(listaEspecialidades);
                                                    //ampliacionAlumnadoEspecialidadesRequest.setNumCensoLB(salidaCampoCodCentro.getCampoSuplementario().getValorTexto());
                                                    ampliacionAlumnadoEspecialidadesRequest.setNumCensoLB(numeroCenso);

                                                    net.lanbide.formacion.ws.regexlan.AmpliacionAlumnadoEspecialidadesCentro ampliacionAlumnadoEspecialidadesCentro = new AmpliacionAlumnadoEspecialidadesCentro();
                                                    ampliacionAlumnadoEspecialidadesCentro.setAmpliacionAlumnadoEspecialidadesCentroRequest(ampliacionAlumnadoEspecialidadesRequest);

                                                    //  net.lanbide.formacion.ws.regexlan.AmpliacionAlumnadoEspecialidadesCentroResponse respuesta = binding.ampliacionAlumnadoEspecialidadesCentro(ampliacionAlumnadoEspecialidadesCentro);
                                                    net.lanbide.formacion.ws.regexlan.WsRegistroCentrosFormacionResultado respuesta = binding.ampliacionAlumnadoEspecialidadesCentro(ampliacionAlumnadoEspecialidadesRequest);

                                                    if (respuesta != null) {

                                                        log.info("respuesta.get_return().getCodRdo(): " + respuesta.getCodRdo());
                                                        log.info("respuesta.get_return().getDescRdo(): " + respuesta.getDescRdo());
                                                        if (respuesta.getCodRdo().equals(0)) {
                                                            salida = "0";
                                                        } else {
                                                            salida = "7";
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    salida = "9";
                                }
                            }
                        }// else
                    } catch (BDException e) {
                        log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                        salida = "6";
                    } catch (org.apache.axis.AxisFault e) {
                        log.error("Error al conectarse al servicio web: " + e.getMessage());
                        salida = "3";
                    } catch (Exception e) {
                        log.error("Error técnico durante la ejecución de la operación: " + e.getMessage());
                        salida = "8";
                    } finally {
                        if (adapt != null) {
                            adapt.devolverConexion(con);
                        }
                    }
                }
            }// else    
        }// if(propiedadesRecuperadas)

        log.info(this.getClass().getName() + ".ampliacionAlumnadoEspecialidadesCentro() salida: " + salida + " <======================");
        return salida;
    }

    /**
     * Operación que modifica los datos de titularidad en un centro ya escrito
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
     * "4" --> El campo suplementario de expediente con el código de centro no tiene valor
     * "5" --> No se ha podido obtener el valor del campo "Fecha de resolución" en ninguno de los trámites en los que se encuentra
     * "6" --> No se ha podido obtener una conexión a la BBDD
     * "7" --> No se ha podido dar de alta el centro con la especialidades en el servicio web de centros de formación
     * "8" --> Se ha producido un error técnico durante la ejecución de la operación
     * "9" --> Es obligatorio que el campo código de la ubicación tenga un valor
     * "10" --> Error, no se ha podido recuperar el número de censo
     * @throws es.altia.util.conexion.BDException
     */
    public String cambioTitularidadCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info(this.getClass().getName() + ".cambioTitularidadCentro  =================>");

        String salida = null;
        boolean conexionWS = false;
        boolean propiedadesRecuperadas = false;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        WSRegistroCentrosFormacionPortBindingStub binding = null;
        //String codigoCampoCodNumeroCenso = null;               
        String codigoCampoCodCentro = null;
        String codigoCampoCodUbicacion = null;
        String codigoCampoFechaResolucion = null;
        String codigoTramiteResolucionPositivaMixta = null;
        String codigoTramiteResolucionNegativa = null;
        String codigoTramiteResolucionPosNegInspeccion = null;
        String urlServicioWeb = null;
        String nuevoCifTitular = null;
        String codigoRolCambioTitularidadCentro = null;

        try {
            //codigoCampoCodNumeroCenso    = ConfigurationParameter.getParameter("CODIGO_CAMPO_NUMEROCENSO",this.getNombreModulo());           

            codigoCampoCodCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO", this.getNombreModulo());
            codigoCampoCodUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION", this.getNombreModulo());
            codigoCampoFechaResolucion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TRAMITE_FECHA_RESOLUCION", this.getNombreModulo());
            codigoTramiteResolucionPositivaMixta = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_POSITIVA_MIXTA", this.getNombreModulo());
            codigoTramiteResolucionNegativa = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_DESISTIMIENTO_NEGATIVA", this.getNombreModulo());
            codigoTramiteResolucionPosNegInspeccion = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_POSNEG_INSPECCION", this.getNombreModulo());
            urlServicioWeb = ConfigurationParameter.getParameter("URL_WS_REGISTROCENTROS", this.getNombreModulo());

            codigoRolCambioTitularidadCentro = ConfigurationParameter.getParameter("CODIGO_ROL_INTERESADO_ENTIDAD_JURIDICA_NUEVA", this.getNombreModulo());

            log.info("codigoCampoCodCentro: " + codigoCampoCodCentro);
            log.info("codigoCampoCodUbicacion: " + codigoCampoCodUbicacion);
            log.info("codigoCampoFechaResolucion: " + codigoCampoFechaResolucion);
            log.info("codigoTramiteResolucionPositivaMixta: " + codigoTramiteResolucionPositivaMixta);
            log.info("codigoTramiteResolucionNegativa: " + codigoTramiteResolucionNegativa);
            log.info("codigoTramiteResolucionPosNegInspeccion: " + codigoTramiteResolucionPosNegInspeccion);
            log.info("urlServicioWeb: " + urlServicioWeb);
            log.info("codigoRolCambioTitularidadCentro " + codigoRolCambioTitularidadCentro);
            propiedadesRecuperadas = true;
        } catch (Exception e) {
            log.error(this.getClass().getName() + ".cambioTitularidadCentro():  Error al recuperar valores de propiedades del fichero de configuración: " + e.getMessage());
            salida = "1";
        }

        if (propiedadesRecuperadas) {
            if (!MeLanbide50Utils.isInteger(codigoTramiteResolucionPositivaMixta)
                    || !MeLanbide50Utils.isInteger(codigoTramiteResolucionNegativa)
                    || !MeLanbide50Utils.isInteger(codigoTramiteResolucionPosNegInspeccion)
                    || codigoCampoCodCentro == null || codigoCampoCodUbicacion == null || codigoCampoFechaResolucion == null
                    || codigoTramiteResolucionPositivaMixta == null || codigoTramiteResolucionNegativa == null || codigoTramiteResolucionPosNegInspeccion == null || codigoRolCambioTitularidadCentro == null) {
                salida = "2";
            } else {

                try {
                    binding = (WSRegistroCentrosFormacionPortBindingStub) new WSRegistroCentrosFormacionServiceLocator().getWSRegistroCentrosFormacionPort(new java.net.URL(urlServicioWeb));
                    conexionWS = true;
                } catch (MalformedURLException e) {
                    log.error(this.getClass().getName() + ".cambioTitularidadCentro(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                } catch (ServiceException e) {
                    log.error(this.getClass().getName() + ".cambioTitularidadCentro(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                }

                if (!conexionWS) {
                    salida = "3";
                } else {
                    // LLAMADA AL SERVICIO WEB 
                    try {

                        String[] datosExp = numExpediente.split("/");
                        String ejercicio = datosExp[0];
                        String codProcedimiento = datosExp[1];

                        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

                        // SE RECUPERA EL VALOR DEL CAMPO "CODCENTRO" DEFINIDO A NIVEL DE EXPEDIENTE
                        SalidaIntegracionVO salidaCampoCodCentro = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codigoCampoCodCentro, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                        // SE RECUPERA EL VALOR DEL CAMPO "CODUBICACION" DEFINIDO A NIVEL DE EXPEDIENTE
                        SalidaIntegracionVO salidaCampoCodUbicacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codigoCampoCodUbicacion, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION DESISTIMIENTO NEGATIVA"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionNegativa = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionNegativa), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION POSITIVA MIXTA"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionPositiva = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionPositivaMixta), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION POSITIVA NEGATIVA CON INSPECCION"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionPosNegInspeccion = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionPosNegInspeccion), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                        log.info("salidaCampoCodCentro.getStatus(): " + salidaCampoCodCentro.getStatus());
                        log.info("salidaCampoCodUbicacion.getStatus(): " + salidaCampoCodUbicacion.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionNegativa.getStatus(): " + salidaFecResolucionTramiteResolucionNegativa.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionPositiva.getStatus(): " + salidaFecResolucionTramiteResolucionPositiva.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus(): " + salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus());

                        if (salidaCampoCodCentro.getStatus() != 0) {
                            salida = "4";
                        } else if (salidaCampoCodUbicacion.getStatus() != 0) {
                            salida = "9";
                        } else if (salidaFecResolucionTramiteResolucionNegativa.getStatus() != 0 && salidaFecResolucionTramiteResolucionPositiva.getStatus() != 0 && salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus() != 0) {
                            salida = "5";
                        } else {
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
                                salida = "6";
                            } else {

                                ExpedienteVO expVO = new ExpedienteVO();
                                expVO.setNumExpediente(numExpediente);

                                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
                                if (salidaFecResolucionTramiteResolucionNegativa.getStatus() == 0 && salidaFecResolucionTramiteResolucionNegativa.getCampoSuplementario() != null) {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionNegativa.getCampoSuplementario().getValorFecha().getTime()));
                                } else if (salidaFecResolucionTramiteResolucionPositiva.getStatus() == 0 && salidaFecResolucionTramiteResolucionPositiva.getCampoSuplementario() != null) {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionPositiva.getCampoSuplementario().getValorFecha().getTime()));
                                } else {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionPosNegInspeccion.getCampoSuplementario().getValorFecha().getTime()));
                                }
                                log.debug("Fecha expediente: " + expVO.getFechaResolucion());
                                String[] datosCorr = null;
                                datosCorr = MeLanbide50Manager.getInstance().getCodigosCorr(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), adapt);
                                if (datosCorr == null || datosCorr.length == 0) {
                                    salida = "27";
                                } else {
                                    if (datosCorr[0] == null || datosCorr[0].isEmpty() || datosCorr[0].isEmpty()) {
                                        log.error("NO hay corrServicio");
                                        // corrServicio
                                        salida = "28";
                                    } else if (datosCorr[1] == null || datosCorr[1].isEmpty()) {
                                        // corrSubservicio 
                                        log.error("NO hay corrSUBServicio");
                                        salida = "29";
                                    } else {
                                        log.debug("hay corrSUBServicio: " + datosCorr[1]);
                                        String numeroCenso = MeLanbide50Manager.getInstance().getNumeroCensoCenFor(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), datosCorr[1], adapt);
                                        // Se recupera el número de censo de la tabla LAN_CENTROS_SERVICIOS a partir del código de centro y del código de ubicación
                                        // String numeroCenso = MeLanbide50DAO.getInstance().getNumeroCenso(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), con);
                                        if (numeroCenso == null || numeroCenso.length() == 0) {
                                            salida = "10";
                                        } else {

                                            nuevoCifTitular = MeLanbide50DAO.getInstance().getCIFInteresadoConRol(codOrganizacion, Integer.parseInt(codigoRolCambioTitularidadCentro), numExpediente, con);
                                            if (nuevoCifTitular == null || "".equals(nuevoCifTitular)) {
                                                salida = "11";
                                            } else {

                                                log.info(" ============> cambioTitularidadCentro numeroCenso CENFOR: " + numeroCenso);
                                                log.info(" ============> cambioTitularidadCentro nuevoCifTitular: " + nuevoCifTitular);
                                                log.info(" ============> cambioTitularidadCentro fechaResolución: " + expVO.getFechaResolucion());

                                                net.lanbide.formacion.ws.regexlan.CambioTitularidadCentroRequest cambioTitularidadRequest = new CambioTitularidadCentroRequest();
                                                cambioTitularidadRequest.setExpediente(expVO);
                                                cambioTitularidadRequest.setNumCensoLB(numeroCenso);
                                                cambioTitularidadRequest.setNuevoCifTitular(nuevoCifTitular);

                                                net.lanbide.formacion.ws.regexlan.CambioTitularidadCentro cambioTitularidad = new CambioTitularidadCentro();

                                                cambioTitularidad.setCambioTitularidadCentroRequest(cambioTitularidadRequest);
                                                net.lanbide.formacion.ws.regexlan.WsRegistroCentrosFormacionResultado respuesta = binding.cambioTitularidadCentro(cambioTitularidadRequest);
                                                //                                  net.lanbide.formacion.ws.regexlan.CambioTitularidadCentroResponse respuesta = binding.cambioTitularidadCentro(cambioTitularidad);

                                                if (respuesta != null) {

                                                    log.info("respuesta.get_return().getCodRdo(): " + respuesta.getCodRdo());
                                                    log.info("respuesta.get_return().getDescRdo(): " + respuesta.getDescRdo());
                                                    if (respuesta.getCodRdo().equals(0)) {
                                                        salida = "0";
                                                    } else {
                                                        salida = "7";
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }// else

                    } catch (BDException e) {
                        log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                        salida = "6";
                    } catch (org.apache.axis.AxisFault e) {
                        log.error("Error al conectarse al servicio web: " + e.getMessage());
                        salida = "3";
                    } catch (Exception e) {
                        log.error("Error técnico durante la ejecución de la operación: " + e.getMessage());
                        salida = "8";
                    } finally {
                        if (adapt != null) {
                            adapt.devolverConexion(con);
                        }
                    }
                }
            }// else    
        }// if(propiedadesRecuperadas)

        log.info(this.getClass().getName() + ".cambioTitularidadCentro() salida: " + salida + " <======================");
        return salida;
    }

    /**
     * Operación que modifica en el registro de centros los datos de titularidad
     * de un centro ya inscrito
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @return String que puede tomar los siguientes valores:
     *
     * "0" --> Si la operación se ha ejecutado correctamente
     *
     * "1" --> Error al obtener las propiedades de configuración
     * "2" --> Revisar las propiedades de configuración con los valores de los campos suplementarios y código de trámite 
     * "3" --> No se ha podido establecer conexión con el servicio web de centros de formación 
     * "4" --> El campo suplementario de expediente con el código de centro no tiene valor 
     * "5" --> No se ha podido obtener el valor del campo "Fecha de resolución" en ninguno de los trámites en los que se encuentra
     * "6" --> No se ha podido obtener una conexión a la BBDD
     * "7" --> No se ha podido dar de alta el centro con la especialidades en el servicio web de centros de formación
     * "8" --> Se ha producido un error técnico durante la ejecución de la operación 
     * "9" --> Es obligatorio que el campo código de la ubicación tenga un valor 
     * "10" --> Error, no se ha podido recuperar el número de censo
     * "11" --> El expediente tiene más de un interesado con el rol de "CENTRO O ENTIDAD" 
     * "12" --> El expediente no tiene ningún interesado con el rol de "CENTRO O ENTIDAD"
     * @throws es.altia.util.conexion.BDException
     */
    public String cambioDomicilioCentro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info(this.getClass().getName() + ".cambioDomicilioCentro  =================>");

        String salida = null;
        boolean conexionWS = false;
        boolean propiedadesRecuperadas = false;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        WSRegistroCentrosFormacionPortBindingStub binding = null;
        //String codigoCampoCodNumeroCenso = null;               
        String codigoCampoCodCentro = null;
        String codigoCampoCodUbicacion = null;
        String codigoCampoFechaResolucion = null;
        String codigoTramiteResolucionPositivaMixta = null;
        String codigoTramiteResolucionNegativa = null;
        String codigoTramiteResolucionPosNegInspeccion = null;
        String urlServicioWeb = null;
        String codigoRolTitularidadCentro = null;

        try {

            codigoCampoCodCentro = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODCENTRO", this.getNombreModulo());
            codigoCampoCodUbicacion = ConfigurationParameter.getParameter("CODIGO_CAMPO_CODUBICACION", this.getNombreModulo());
            codigoCampoFechaResolucion = ConfigurationParameter.getParameter("CODIGO_CAMPO_TRAMITE_FECHA_RESOLUCION", this.getNombreModulo());
            codigoTramiteResolucionPositivaMixta = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_POSITIVA_MIXTA", this.getNombreModulo());
            codigoTramiteResolucionNegativa = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_DESISTIMIENTO_NEGATIVA", this.getNombreModulo());
            codigoTramiteResolucionPosNegInspeccion = ConfigurationParameter.getParameter("CODIGO_TRAMITE_NOTIFICACION_RESOLUCION_POSNEG_INSPECCION", this.getNombreModulo());
            urlServicioWeb = ConfigurationParameter.getParameter("URL_WS_REGISTROCENTROS", this.getNombreModulo());
            codigoRolTitularidadCentro = ConfigurationParameter.getParameter("CODIGO_ROL_INTERESADO_CENTRO_O_ENTIDAD", this.getNombreModulo());

            log.info("codigoCampoCodCentro: " + codigoCampoCodCentro);
            log.info("codigoCampoCodUbicacion: " + codigoCampoCodUbicacion);
            log.info("codigoCampoFechaResolucion: " + codigoCampoFechaResolucion);
            log.info("codigoTramiteResolucionPositivaMixta: " + codigoTramiteResolucionPositivaMixta);
            log.info("codigoTramiteResolucionNegativa: " + codigoTramiteResolucionNegativa);
            log.info("codigoTramiteResolucionPosNegInspeccion: " + codigoTramiteResolucionPosNegInspeccion);
            log.info("urlServicioWeb: " + urlServicioWeb);
            log.info("codigoRolTitularidadCentro: " + codigoRolTitularidadCentro);

            propiedadesRecuperadas = true;
        } catch (Exception e) {
            log.error(this.getClass().getName() + ".cambioDomicilioCentro():  Error al recuperar valores de propiedades del fichero de configuración: " + e.getMessage());
            salida = "1";
        }

        if (propiedadesRecuperadas) {
            if (!MeLanbide50Utils.isInteger(codigoTramiteResolucionPositivaMixta)
                    || !MeLanbide50Utils.isInteger(codigoTramiteResolucionNegativa)
                    || !MeLanbide50Utils.isInteger(codigoTramiteResolucionPosNegInspeccion)
                    || codigoCampoCodCentro == null || codigoCampoCodUbicacion == null || codigoCampoFechaResolucion == null
                    || codigoTramiteResolucionPositivaMixta == null || codigoTramiteResolucionNegativa == null || codigoTramiteResolucionPosNegInspeccion == null || codigoRolTitularidadCentro == null) {
                salida = "2";
            } else {

                try {
                    binding = (WSRegistroCentrosFormacionPortBindingStub) new WSRegistroCentrosFormacionServiceLocator().getWSRegistroCentrosFormacionPort(new java.net.URL(urlServicioWeb));
                    conexionWS = true;
                } catch (MalformedURLException e) {
                    log.error(this.getClass().getName() + ".cambioDomicilioCentro(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                } catch (ServiceException e) {
                    log.error(this.getClass().getName() + ".cambioDomicilioCentro(): Error al establecer conexion con WSRegistroCentrosFormacion: " + e.getMessage());
                }

                if (!conexionWS) {
                    salida = "3";
                } else {
                    // LLAMADA AL SERVICIO WEB 
                    try {

                        String[] datosExp = numExpediente.split("/");
                        String ejercicio = datosExp[0];
                        String codProcedimiento = datosExp[1];

                        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

                        // SE RECUPERA EL VALOR DEL CAMPO "CODCENTRO" DEFINIDO A NIVEL DE EXPEDIENTE
                        SalidaIntegracionVO salidaCampoCodCentro = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codigoCampoCodCentro, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                        // SE RECUPERA EL VALOR DEL CAMPO "CODUBICACION" DEFINIDO A NIVEL DE EXPEDIENTE
                        SalidaIntegracionVO salidaCampoCodUbicacion = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, codigoCampoCodUbicacion, IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION DESISTIMIENTO NEGATIVA"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionNegativa = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionNegativa), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION POSITIVA MIXTA"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionPositiva = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionPositivaMixta), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                        // SE RECUPERA EL VALOR DEL CAMPO "FECRESOLUCION" DEL TRÁMITE "NOTIFICACION RESOLUCION POSITIVA NEGATIVA CON INSPECCION"
                        SalidaIntegracionVO salidaFecResolucionTramiteResolucionPosNegInspeccion = el.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicio, numExpediente, codProcedimiento, Integer.parseInt(codigoTramiteResolucionPosNegInspeccion), ocurrenciaTramite, codigoCampoFechaResolucion, IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

                        log.info("salidaCampoCodCentro.getStatus(): " + salidaCampoCodCentro.getStatus());
                        log.info("salidaCampoCodUbicacion.getStatus(): " + salidaCampoCodUbicacion.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionNegativa.getStatus(): " + salidaFecResolucionTramiteResolucionNegativa.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionPositiva.getStatus(): " + salidaFecResolucionTramiteResolucionPositiva.getStatus());
                        log.info("salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus(): " + salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus());

                        if (salidaCampoCodCentro.getStatus() != 0) {
                            salida = "4";
                        } else if (salidaCampoCodUbicacion.getStatus() != 0) {
                            salida = "9";
                        } else if (salidaFecResolucionTramiteResolucionNegativa.getStatus() != 0 && salidaFecResolucionTramiteResolucionPositiva.getStatus() != 0 && salidaFecResolucionTramiteResolucionPosNegInspeccion.getStatus() != 0) {
                            salida = "5";
                        } else {
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
                                salida = "6";
                            } else {
                                ExpedienteVO expVO = new ExpedienteVO();
                                expVO.setNumExpediente(numExpediente);

                                SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
                                if (salidaFecResolucionTramiteResolucionNegativa.getStatus() == 0 && salidaFecResolucionTramiteResolucionNegativa.getCampoSuplementario() != null) {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionNegativa.getCampoSuplementario().getValorFecha().getTime()));
                                } else if (salidaFecResolucionTramiteResolucionPositiva.getStatus() == 0 && salidaFecResolucionTramiteResolucionPositiva.getCampoSuplementario() != null) {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionPositiva.getCampoSuplementario().getValorFecha().getTime()));
                                } else {
                                    expVO.setFechaResolucion(sf.format(salidaFecResolucionTramiteResolucionPosNegInspeccion.getCampoSuplementario().getValorFecha().getTime()));
                                }
                                log.debug("Fecha expediente: " + expVO.getFechaResolucion());
                                String[] datosCorr = null;
                                datosCorr = MeLanbide50Manager.getInstance().getCodigosCorr(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), adapt);
                                if (datosCorr == null || datosCorr.length == 0) {
                                    salida = "27";
                                } else {
                                    if (datosCorr[0] == null || datosCorr[0].isEmpty() || datosCorr[0].isEmpty()) {
                                        log.error("NO hay corrServicio");
                                        // corrServicio
                                        salida = "28";
                                    } else if (datosCorr[1] == null || datosCorr[1].isEmpty()) {
                                        // corrSubservicio 
                                        log.error("NO hay corrSUBServicio");
                                        salida = "29";
                                    } else {
                                        log.debug("hay corrSUBServicio: " + datosCorr[1]);
                                        String numeroCenso = MeLanbide50Manager.getInstance().getNumeroCensoCenFor(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), datosCorr[1], adapt);

                                        // Se recupera el número de censo de la tabla LAN_CENTROS_SERVICIOS a partir del código de centro y del código de ubicación
//                                String numeroCenso = MeLanbide50DAO.getInstance().getNumeroCenso(salidaCampoCodCentro.getCampoSuplementario().getValorTexto(), salidaCampoCodUbicacion.getCampoSuplementario().getValorTexto(), con);
                                        if (numeroCenso == null || numeroCenso.length() == 0) {
                                            salida = "10";
                                        } else {

                                            Hashtable<String, Object> domicilio = MeLanbide50DAO.getInstance().getDomicilioInteresadoConRol(codOrganizacion, Integer.parseInt(codigoRolTitularidadCentro), numExpediente, con);
                                            if (domicilio != null) {

                                                String status = (String) domicilio.get("status");
                                                if (status != null && "1".equals(status)) {
                                                    // Hay más de un interesado con el rol "CENTRO O ENTIDAD"
                                                    salida = "11";
                                                } else if (status != null && "2".equals(status)) {
                                                    // El expediente no tiene ningún interesado con el rol "CENTRO O ENTIDAD"
                                                    salida = "12";
                                                } else if (status != null && "0".equals(status)) {

                                                    DireccionInteresadoVO dom = (DireccionInteresadoVO) domicilio.get("direccion");

                                                    DireccionVO direccion = new DireccionVO();
                                                    direccion.setCodMunicipio(dom.getCodMunicipio());
                                                    direccion.setCodPostal(dom.getCodPostal());
                                                    direccion.setCodProvincia(dom.getCodProvincia());
                                                    direccion.setEscalera(dom.getEscalera());
                                                    direccion.setNomVia(dom.getNombreVia());
                                                    direccion.setNumVia(dom.getNumeroDesde());
                                                    direccion.setPlanta(dom.getPlanta());
                                                    direccion.setTipoVia(dom.getTipoViaPersonaFisica());
                                                    direccion.setPuerta(dom.getPuerta());

                                                    net.lanbide.formacion.ws.regexlan.CambioDomicilioCentroRequest cambioDomicilioCentroRequest = new CambioDomicilioCentroRequest();
                                                    cambioDomicilioCentroRequest.setExpediente(expVO);
                                                    cambioDomicilioCentroRequest.setNumCensoLB(numeroCenso);
                                                    cambioDomicilioCentroRequest.setDireccion(direccion);

                                                    net.lanbide.formacion.ws.regexlan.CambioDomicilioCentro cambioDomicilioCentro = new CambioDomicilioCentro();
                                                    cambioDomicilioCentro.setCambioDomicilioCentroRequest(cambioDomicilioCentroRequest);

                                                    net.lanbide.formacion.ws.regexlan.WsRegistroCentrosFormacionResultado respuesta = binding.cambioDomicilioCentro(cambioDomicilioCentroRequest);
//                                            net.lanbide.formacion.ws.regexlan.CambioDomicilioCentroResponse respuesta = binding.cambioDomicilioCentro(cambioDomicilioCentro);

                                                    if (respuesta != null) {

                                                        log.info("respuesta.get_return().getCodRdo(): " + respuesta.getCodRdo());
                                                        log.info("respuesta.get_return().getDescRdo(): " + respuesta.getDescRdo());
                                                        if (respuesta.getCodRdo().equals(0)) {
                                                            salida = "0";
                                                        } else {
                                                            salida = "7";
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
                        log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                        salida = "6";
                    } catch (org.apache.axis.AxisFault e) {
                        log.error("Error al conectarse al servicio web: " + e.getMessage());
                        salida = "3";
                    } catch (Exception e) {
                        log.error("Error técnico durante la ejecución de la operación: " + e.getMessage());
                        salida = "8";
                    } finally {
                        if (adapt != null) {
                            adapt.devolverConexion(con);
                        }
                    }
                }
            }// else    
        }// if(propiedadesRecuperadas)

        log.info(this.getClass().getName() + ".cambioDomicilioCentro() salida: " + salida + " <======================");
        return salida;
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
        log.info(this.getClass().getSimpleName() + ".consultarUbicaciones =====> " + numExpediente);
        ArrayList<UbicacionVO> ubicaciones = null;
        StringBuilder xml = new StringBuilder();
        String codError = null;
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String codCentro = null;

        try {
            adapt = this.getAdaptSQLBD(Integer.toString(codOrganizacion));
            con = adapt.getConnection();

            /**
             * Mensaje puede tomar los siguientes valores:
             * 1 --> Error al recuperar el documento de interesado con rol por defecto del expediente
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
                documento = MeLanbide50DAO.getInstance().getDocumentoInteresadoRolDefecto(codOrganizacion, numExpediente, con);
                continuar = true;
            } catch (MeLanbide50Exception e) {
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
                    //codCentro = MeLanbide50DAO.getInstance().getCodCentro(documento, con);
                    codigosCentros = MeLanbide50DAO.getInstance().getCodCentro(documento, con);

                    continuar = true;
                } catch (MeLanbide50Exception e) {
                    continuar = false;
                }

                if (!continuar) {
                    codError = "3"; // Se ha producido un error técnico al recuperar el código de centro
                } else {
                    if (codigosCentros != null && codigosCentros.size() > 1) {
                        codError = "9";
                    } else if (codigosCentros == null || codigosCentros.isEmpty()) {
                        codError = "4";
                    } else if (codigosCentros != null && codigosCentros.size() == 1) {

                        codCentro = codigosCentros.get(0);
                        continuar = false;
                        try {
                            // Se procede a recuperar las ubicaciones del centro
                            ubicaciones = MeLanbide50DAO.getInstance().getUbicacionesCentro(codCentro, con);
                            continuar = true;
                        } catch (MeLanbide50Exception e) {
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
                adapt.devolverConexion(con);

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
     * Para un expediente, se graba el código de centro y el código de ubicación
     * seleccionados, en determinados campos suplementarios de tipo fichero
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @param request: Objeto de tipo HttpServletRequest
     * @param response: Objeto de tipo HttpServletResponse
     */
    public void grabarUbicacionSeleccionada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {

        log.info(this.getClass().getSimpleName() + ".grabarUbicacionSeleccionada =====> " + numExpediente);
        StringBuilder xml = new StringBuilder();
        String codError = null;
        String codigoUbicacion = null;
        String codigoCentro = null;

        String codigoCampoCodCentro = null;
        String codigoCampoCodUbicacion = null;
        String codigoCampoCodCenso = null;
        String codigoCampoCodCensoLanbide = null;
        String numCensoSilicoi = null;
        String numCensoCenFor = null;

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
             * 9 --> No se ha podido recuperar corrServicio
             * 10 --> No se ha podido recuperar corrSubservicio
             * 11 --> No se ha podido recuperar el número de censo Lanbide
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
                    datosCorr = MeLanbide50Manager.getInstance().getCodigosCorr(codigoCentro, codigoUbicacion, getAdaptSQLBD(Integer.toString(codOrganizacion)));
                    if (datosCorr == null || datosCorr.length == 0) {
                        log.error("No se han podido recuperar los códigos corr");
                        codError = "8";
                    } else {
                        if (datosCorr[0] == null || datosCorr[0].isEmpty() || datosCorr[0].isEmpty()) {
                            log.error("NO hay corrServicio");
                            codError = "9";
                        } else if (datosCorr[1] == null || datosCorr[1].isEmpty() || datosCorr[1].isEmpty()) {
                            log.error("NO hay corrSUBServicio");
                            codError = "10";
                        } else {
                            numCensoSilicoi = MeLanbide50Manager.getInstance().getNumCensoSILICOI(codigoCentro, codigoUbicacion, datosCorr[1], getAdaptSQLBD(Integer.toString(codOrganizacion)));
                            if (numCensoSilicoi == null) {
                                log.error("Error al recuperar el número de Censo SILICOI");
                                codError = "7";
                            } else {
                                log.info("numCenso SILICOI: " + numCensoSilicoi);
                                IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
                                //grabamos el número de censo como dato suplementario
                                CampoSuplementarioModuloIntegracionVO campoSilicoi = new CampoSuplementarioModuloIntegracionVO();
                                campoSilicoi.setCodOrganizacion(Integer.toString(codOrganizacion));
                                campoSilicoi.setCodProcedimiento(codProcedimiento);
                                campoSilicoi.setEjercicio(ejercicio);
                                campoSilicoi.setCodigoCampo(codigoCampoCodCenso);
                                campoSilicoi.setNumExpediente(numExpediente);
                                campoSilicoi.setValorTexto(numCensoSilicoi);
                                campoSilicoi.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                SalidaIntegracionVO salidaCampoNumCenso = el.grabarCampoSuplementario(campoSilicoi);
                                log.info("censo SILICOI grabado: " + salidaCampoNumCenso.getStatus());
                                //if(salidaCampoNumCenso.getStatus()!=0){
                                //codError = "4";
                                //}else{
                                numCensoCenFor = MeLanbide50Manager.getInstance().getNumeroCensoCenFor(codigoCentro, codigoUbicacion, datosCorr[1], getAdaptSQLBD(Integer.toString(codOrganizacion)));

                                //     numCensoLanbide = MeLanbide50Manager.getInstance().getNumCensoLanbide(codigoCentro, codigoUbicacion, getAdaptSQLBD(Integer.toString(codOrganizacion)));
                                if (numCensoCenFor == null || numCensoCenFor.isEmpty()) {
                                    log.error("No se ha podido recuperar el Número de Censo Lanbide CENFOR");
                                    codError = "11";
                                } else {
                                    log.info("numCensoLanbide CENFOR: " + numCensoCenFor);

                                    //grabamos el número de censo como dato suplementario
                                    CampoSuplementarioModuloIntegracionVO campoCensoLanbide = new CampoSuplementarioModuloIntegracionVO();
                                    campoCensoLanbide.setCodOrganizacion(Integer.toString(codOrganizacion));
                                    campoCensoLanbide.setCodProcedimiento(codProcedimiento);
                                    campoCensoLanbide.setEjercicio(ejercicio);
                                    campoCensoLanbide.setCodigoCampo(codigoCampoCodCensoLanbide);
                                    campoCensoLanbide.setNumExpediente(numExpediente);
                                    campoCensoLanbide.setValorTexto(numCensoCenFor);
                                    campoCensoLanbide.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                                    SalidaIntegracionVO salidaCampoNumCensoLanbide = el.grabarCampoSuplementario(campoCensoLanbide);
                                    log.info("censo lanbide grabado CENFOR: " + salidaCampoNumCensoLanbide.getStatus());
                                    if (salidaCampoNumCensoLanbide.getStatus() != 0) {
                                        log.error("No se ha podido grabar el Número de Censo Lanbide CENFOR");
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
                                            // Se graba el valor del código de centro en el campo suplementario de "Código de ubicación", que es 
                                            // de tipo texto
                                            CampoSuplementarioModuloIntegracionVO campo2 = new CampoSuplementarioModuloIntegracionVO();
                                            campo2.setCodOrganizacion(Integer.toString(codOrganizacion));
                                            campo2.setCodProcedimiento(codProcedimiento);
                                            campo2.setEjercicio(ejercicio);
                                            campo2.setCodigoCampo(codigoCampoCodUbicacion);
                                            campo2.setNumExpediente(numExpediente);
                                            campo2.setValorTexto(codigoUbicacion);
                                            campo2.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);

                                            SalidaIntegracionVO salidaCampoCodUbicacion = el.grabarCampoSuplementario(campo2);

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
                xml.append(numCensoCenFor);
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

//Nuevo Metodo
    public String alta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("begin alta");
        //COMPROBAR SI DOCUMENTO Y PASAR POR REQUEST
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String numDoc = MeLanbide50Manager.getInstance().getNumDocumento(codOrganizacion, numExpediente, adaptador, codTramite);

        request.setAttribute("numDoc", numDoc);

        return "/jsp/extension/melanbide50/alta.jsp";
    }

    public void altaRegistro(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        int codigoResultado = 0;
        String numRegSalida = "";
        String fecRegSalida = "";
        String documento = "";
        log.info("begin altaregistro");
        try {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String wsUrl = ConfigurationParameter.getParameter(ConstantesMeLanbide50.URL_WS_REGISTRO, ConstantesMeLanbide50.FICHERO_PROPIEDADES);
            log.info("wsUrl: " + wsUrl);
            //java.net.URL url = new java.net.URL("http://localhost:8081/WSRegistroES/services/WSRegistroESPort?wsdl");
            //java.net.URL url = new java.net.URL("http://10.168.212.21:7081/WSRegistroES/services/WSRegistroESPort?wsdl");
            java.net.URL url = new java.net.URL(wsUrl);
            WSRegistroESBindingStub binding = null;
            try {
                binding = (WSRegistroESBindingStub) new WSRegistroESServiceLocator().getWSRegistroESPort(url);
                log.info("resultado binding : " + binding);
                // Time out after a minute
                binding.setTimeout(60000);
            } catch (ServiceException ex) {
                log.error("eee", ex);
            }

            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide50.FORMATO_FECHA);

            String[] datosExpediente = numExpediente.split(ConstantesMeLanbide50.BARRA_SEPARADORA);

            //Informacion de la conexion
            InfoConexionVO inf = new InfoConexionVO();
            inf.setOrganizacion(String.valueOf(codOrganizacion));
            inf.setAplicacion("RGI");

            log.info("------------Info conexion----------");
            log.info("Organizacion info " + inf.getOrganizacion());
            log.info("Aplicacion inf " + inf.getAplicacion());
            log.info("------------Info conexion----------");

            Expediente exp = MeLanbide50Manager.getInstance().getDatosExpediente(codOrganizacion, numExpediente, adaptador);

            ///Expediente
            log.info("Datos expediente  respuesta: " + exp.getUorRegistro());
            log.info("Datos expediente  respuesta num: " + exp.getExpNum());
            //Registro
            RegistroVO reg = new RegistroVO();
//            if(exp.getExrDep() != null)
//                reg.setDepartamento(exp.getExrDep()); //--> E_EXR.EXR_DEP
            reg.setDepartamento(1);
            //reg.setCodTipoEntradaSalida("1");
            reg.setEjercicio(datosExpediente[0]); //--> Del expediente
            reg.setCodUorRegistro(0); //1
            reg.setCodUorOrigenDestino(exp.getUorRegistro().toString()); //9999
            log.info("setCodUorOrigenDestino: " + exp.getUorRegistro().toString());
            reg.setFechaPresentacion(format.format(new java.util.Date())); //9999
            log.info("FechaPresentacion: " + format.format(new java.util.Date()));
            reg.setCodTipoDocumento(ConfigurationParameter.getParameter(ConstantesMeLanbide50.TIPO_DOC, ConstantesMeLanbide50.FICHERO_PROPIEDADES)); //--> R_RES.RES_TDO
            log.info("tipoDocumento: " + reg.getCodTipoDocumento());
            reg.setTipo("S");
            log.info("tipoRegistro: " + reg.getTipo());
            log.info("Descripcion tipo documento" + reg.getDescTipoDocumento());
            reg.setAsunto(ConfigurationParameter.getParameter(ConstantesMeLanbide50.ASUNTO, ConstantesMeLanbide50.FICHERO_PROPIEDADES) + " " + numExpediente); //--> Del expediente             
            log.info("Asunto: " + reg.getAsunto());
            reg.setObservaciones(ConfigurationParameter.getParameter(ConstantesMeLanbide50.OBSERVACIONES, ConstantesMeLanbide50.FICHERO_PROPIEDADES));//--> Del expediente    
            log.info("Observaciones: " + reg.getObservaciones());
            log.info("Departamento:" + reg.getDepartamento());
            log.info("Ejercicio:" + reg.getEjercicio());
            log.info("Codigo Registro:" + reg.getCodUorRegistro());

//            reg.setNumTransporte(exp.getResNtr()); //-->R_RES.RES_NTR     
//            log.info("numTransporte: "+reg.getNumTransporte());
//            reg.setAutoridad(exp.getResAut()); //--> R_RES.RES_AUT    
//            log.info("Autoridad: "+reg.getAutoridad());
            /*???*/  //reg.setCodAsuntoCodificado("QWW");
            reg.setProcedimiento(datosExpediente[1]); //--> Del expediente
            log.info("Procedimiento:" + reg.getProcedimiento());
            log.info("Objeto registro completado");

            //Interesados
            List<Tercero> interesados = MeLanbide50Manager.getInstance().getTercerosExpediente(codOrganizacion, Integer.valueOf(datosExpediente[0]), numExpediente, adaptador);

            RemitenteVO rem = null;
            DomicilioVO dom = null;
            Domicilio domTer = null;
            List<RemitenteVO> remitentes = new ArrayList<RemitenteVO>();

            for (Tercero ter : interesados) {
                //Interesado 1
                rem = new RemitenteVO();
                rem.setApe1(ter.getTerAp1());
                rem.setApe2(ter.getTerAp2());
                rem.setNombre(ter.getTerNom());
                rem.setTipoDoc(ter.getTerTid() != null ? ter.getTerTid().toString() : null);
                rem.setDocumento(ter.getTerDoc());
                rem.setRol(ter.getExtRol());
                log.info("-----------------Datos de remitente---------------");
                log.info("Apellido 1 remitente" + rem.getApe1());
                log.info("Apellido2 remitente" + rem.getApe2());
                log.info("Nombre remitente" + rem.getNombre());
                log.info("Tipo documento remitente:" + rem.getTipoDoc());
                log.info("Documento:" + rem.getDocumento());
                log.info("Rol:" + rem.getRol());
                log.info("----------------------fin datos remitente-----------");
                domTer = ter.getDomicilio();
                if (domTer != null) {
                    dom = new DomicilioVO();
                    log.info("------------Datos Domicilio----------");
                    dom.setCodMunicipio(domTer.getMun());
                    log.info("Domicilio COdMunicipio: " + dom.getCodMunicipio());
                    dom.setCodProvincia(domTer.getPrv());
                    log.info("Domicilio CodProvincia: " + dom.getCodProvincia());
                    dom.setCodPais(domTer.getPai());
                    log.info("Domicilio CodPais: " + dom.getCodPais());
                    dom.setTipoVia(domTer.getTvi());
                    log.info("Domicilio TipoVia: " + dom.getTipoVia());
                    dom.setVia(domTer.getNomVia());
                    log.info("Domicilio Via: " + dom.getVia());
                    dom.setEmplazamiento(domTer.getDmc());
                    log.info("Domicilio Emplazamiento: " + dom.getEmplazamiento());
                    rem.setDomicilio(dom);
                    log.info("Domicilio Domicilio: " + rem.getDomicilio());
                    log.info("------------FIN Datos Domicilio----------");

                    /*???*/
 /*CampoSuplementarioVO cs1 = new CampoSuplementarioVO();
                     CampoSuplementarioVO cs2 = new CampoSuplementarioVO();           
                     cs1.setCodCampo("TNUMSEGSOCIAL"); 
                     cs1.setValorCampo("2222222");
                     cs2.setCodCampo("TIKUSTERVER"); 
                     cs2.setValorCampo("14");
                     rem.setCamposSuplementarios(new CampoSuplementarioVO[]{cs1,cs2});*/
                }
                remitentes.add(rem);
            }
            if (!remitentes.isEmpty()) {
                reg.setInteresados(remitentes.toArray(new RemitenteVO[]{}));
            }

            /*
             //Interesado 1
             rem = new RemitenteVO();
             rem.setApe1(ter.getTerAp1());
             rem.setApe2(ter.getTerAp2());
             rem.setNombre(ter.getTerNom());
             rem.setTipoDoc(ter.getTerTid() != null ? ter.getTerTid().toString() : null);
             rem.setDocumento(ter.getTerDoc());
             rem.setRol(ter.getExtRol());
             */
            log.info("Nº Interesados:" + reg.getInteresados().length);

            reg.setExpRelacionado(numExpediente);
            log.info("Exp Relacionado:" + reg.getExpRelacionado());

            // SalidaRegistroESBean value = null;
            SalidaRegistroESBean value = binding.setRegistroES(reg, inf);
            log.info("Enviamos value: " + value.getStatus());

            log.info("Estado value: " + value.getStatus());
            log.info("Descripcion value: " + value.getDescStatus());
            log.info("Numero value:" + value.getNumero());
            log.info("FechaPresentacion value: " + value.getFecha());
            log.info("Ejercicio value: " + value.getEjercicio());
            log.info("getStatus value: " + value.getEjercicio());

            // value.
            if (value.getStatus() == 0) {

                //log.debug("Registro:" + value.getRegistros().length);
                if (value.getNumero() != null) {
                    //RegistroVO registro = value.getRegistros()[0]; 
                    log.info("Numero:" + value.getNumero());
                    log.info("FechaPresentacion: " + value.getFecha());
                    log.info("Ejercicio: " + value.getEjercicio());

                    documento = rem.getDocumento();
                    String codCampoNumero = ConfigurationParameter.getParameter(ConstantesMeLanbide50.CAMPO_SUPL_NUMREGSALIDA, ConstantesMeLanbide50.FICHERO_PROPIEDADES);
                    String codCampoFecha = ConfigurationParameter.getParameter(ConstantesMeLanbide50.CAMPO_SUPL_FECREGSALIDA, ConstantesMeLanbide50.FICHERO_PROPIEDADES);

                    MeLanbide50Manager manager = MeLanbide50Manager.getInstance();

                    try {

                        boolean result = false;
                        Date fecha = format.parse(value.getFecha());
                        log.info("Fecha result:" + value.getNumero());
                        //String campoNumRegSalida = value.getNumero();
                        List<CampoSuplementario> camposSuplementarios = new ArrayList<CampoSuplementario>();

                        CampoSuplementario campoFecRegSalida = new CampoSuplementario();
                        campoFecRegSalida.setCodCampo(codCampoFecha);
                        campoFecRegSalida.setTipoDato(ConstantesMeLanbide50.TIPOS_DATOS_SUPLEMENTARIOS.FECHA);
                        campoFecRegSalida.setValor(format.format(new java.util.Date()));
                        log.info("Campo fecha Registro salida fin:" + campoFecRegSalida);
                        fecRegSalida = (String) campoFecRegSalida.getValor();

                        log.info("Campo fecha Registro salida fin cod campo:" + campoFecRegSalida.getCodCampo());
                        log.info("Campo fecha Registro salida fin valor:" + campoFecRegSalida.getValor());
                        camposSuplementarios.add(campoFecRegSalida);

                        CampoSuplementario campoNumRegSalida = new CampoSuplementario();
                        campoNumRegSalida.setCodCampo(codCampoNumero);
                        campoNumRegSalida.setTipoDato(ConstantesMeLanbide50.TIPOS_DATOS_SUPLEMENTARIOS.NUMERICO);
                        campoNumRegSalida.setValor(Integer.valueOf(value.getNumero()));
                        log.info("Campo suplementario:" + campoNumRegSalida);
                        numRegSalida = campoNumRegSalida.getValor().toString();

                        log.info("Campo suplementario:" + campoNumRegSalida);
                        log.info("Campo suplementario:" + campoNumRegSalida.getCodCampo());
                        log.info("Campo suplementario:" + campoNumRegSalida.getTipoDato());
                        log.info("Campo suplementario:" + campoNumRegSalida.getTipoDato());
                        camposSuplementarios.add(campoNumRegSalida);

//                        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
//                    
//                        //grabamos el número de censo como dato suplementario
//                        CampoSuplementarioModuloIntegracionVO campo3 = new CampoSuplementarioModuloIntegracionVO();
//                        campo3.setCodOrganizacion(Integer.toString(codOrganizacion));
//                        campo3.setCodProcedimiento(datosExpediente[1]);
//                        campo3.setEjercicio(datosExpediente[0]);
//                        campo3.setCodigoCampo(codCampoFecha);
//                        campo3.setNumExpediente(numExpediente);
//                        campo3.setValorTexto(fecha.toString());
//                        campo3.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
//                    
//                        SalidaIntegracionVO salidaCampoFecha =el.grabarCampoSuplementario(campo3);
//                        
//                        log.info("fecha grabado: " + salidaCampoFecha.getStatus());
//                        if(salidaCampoFecha.getStatus()!=0){
//                            codigoResultado = 1;
//                        }
//                        CampoSuplementarioModuloIntegracionVO campo = new CampoSuplementarioModuloIntegracionVO();
//                        campo.setCodOrganizacion(Integer.toString(codOrganizacion));
//                        campo.setCodProcedimiento(datosExpediente[1]);
//                        campo.setEjercicio(datosExpediente[0]);
//                        campo.setCodigoCampo(codCampoNumero);
//                        campo.setNumExpediente(numExpediente);
//                        campo.setValorTexto(campoNumRegSalida);
//                        campo.setTipoCampo(IModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
//                    
//                        SalidaIntegracionVO salidaCampoNum =el.grabarCampoSuplementario(campo);
//                        log.info("num registro grabado: " + salidaCampoNum.getStatus());
//                        if(salidaCampoNum.getStatus()!=0){
//                            codigoResultado = 1;
//                        }
                        result = manager.grabarCamposSuplementariosTramite(codOrganizacion, datosExpediente[1], Integer.valueOf(datosExpediente[0]), numExpediente, codTramite, ocurrenciaTramite, camposSuplementarios, adaptador);
                        log.info("resultado campo suplementario:" + result);
                        if (result) {
                            codigoResultado = 0;
                        } else {
                            codigoResultado = 1;
                        }
                    } catch (Exception ex) {
                        log.error(ex);
                        codigoResultado = 2;
                    }
                }
            } else {
                log.error("value.getStatus(): " + value.getStatus());
                codigoResultado = 3;
            }
        } catch (javax.xml.rpc.ServiceException jre) {
            log.error("Error en altaRegistro1: ", jre);
            codigoResultado = 3;
        } catch (Exception ex) {
            log.error("Error en altaRegistro2: ", ex);
            codigoResultado = 4;
        }
        log.info("numRegSalida: " + numRegSalida);
        log.info("fecRegSalida: " + fecRegSalida);

        //xml
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoResultado);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<NUM_SALIDA>");
        xmlSalida.append(numRegSalida);
        xmlSalida.append("</NUM_SALIDA>");
        xmlSalida.append("<FEC_SALIDA>");
        xmlSalida.append(fecRegSalida);
        xmlSalida.append("</FEC_SALIDA>");
        xmlSalida.append("<COD_TRAMITE>");
        xmlSalida.append(codTramite);
        xmlSalida.append("</COD_TRAMITE>");
        xmlSalida.append("<COD_DOCUMENTO>");
        xmlSalida.append(documento);
        xmlSalida.append("</COD_DOCUMENTO>");

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

        //return String.valueOf(codigoResultado);
    }

    //Nuevo metodo para buscar documentos dockusi
    public String documentos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("begin documentosAportadosAnteriormente");
        //COMPROBAR SI DOCUMENTO Y PASAR POR REQUEST
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String numDoc = MeLanbide50Manager.getInstance().getNumDocumento(codOrganizacion, numExpediente, adaptador, codTramite);

        request.setAttribute("numDoc", numDoc);

        // Documentos aportados anteriormente
        List<DocumentosVO> listDocumentos = MeLanbide50Manager.getInstance().getDatosDocumentos(numExpediente, adaptador);
        log.info("DESPUES DE RECOGER Documentos");
        if (!listDocumentos.isEmpty()) {
            request.setAttribute("listDocumentos", listDocumentos);
        }

        return "/jsp/extension/melanbide50/documentos.jsp";

    }

    //Nuevo Metodo Búsqueda en DOKUSI de los documentos aportados
    public void BusquedaDocumentosAportadosDokusi(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Lan6UtilExcepcion, Lan6Excepcion, SQLException, Exception {

        log.info("begin BusquedaDocumentosAportadosDokusi => " + numExpediente);

//  begin BusquedaDocumentosAportadosDokusi => ejgv_d_contralab,    doc industria,  04-08-2020, 2020/CONCM/000040
        //Recuperamos los datos seleccionados del formulario
        String camposArray[] = numExpediente.split(",");

        String tipoDocumental = camposArray[0];
        String asuntoDocumental = camposArray[1];
        String nombreDoc = camposArray[2];
        String fecha = camposArray[3];
        String expediente = camposArray[4];
        String codigoOperacion = "";
//        log.info("DATOS DEL JSP " + nombreDoc + " - " + fecha + " - " + expediente + " - " + tipoDocumental+ " - " + asuntoDocumental);

        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String dniTercero = MeLanbide50Manager.getInstance().getDniTercero(codOrganizacion, expediente, adaptador, codTramite);

        log.info("datos recuperados del tercero segun el expediente => "   + dniTercero);

        log.info("begin BusquedaDocumentosAportadosDokusi");
        List<Lan6Documento> listaDocumentosLan6 = null;
        // Documentos aportados anteriormente
        // Invocar solo si la solicitud tiene tipo documental
        if (tipoDocumental != null && !tipoDocumental.isEmpty()) {
            try {
//                String idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_" + "LAN62_CONC");
//                log.debug("Id Proc Platea => " + idProcedimiento);
//                Lan6DossierServicios servicios = new Lan6DossierServicios(idProcedimiento);

                Lan6DossierServicios servicios = new Lan6DossierServicios(expediente.split(ConstantesDatos.BARRA)[1]); // 2.2 

                Lan6DocumentoDossier lan6DocumentoDossier = new Lan6DocumentoDossier();

                log.info("DATOS RECIBIDOS DEL JSP" + numExpediente);
                lan6DocumentoDossier.setTipoDocumental(tipoDocumental);
//                lan6DocumentoDossier.setAsuntoDocumental(asuntoDocumental);
                lan6DocumentoDossier.setNumTitular(dniTercero);
          //      lan6DocumentoDossier.setFechaEntrega(fecha);
         //       lan6DocumentoDossier.setNombre(Nombre_del_documento);

                log.debug("DATOS RECIBIDOS DEL JSP fijos");
                log.info("tipo_documental => " + lan6DocumentoDossier.getTipoDocumental());
                log.info("asunto_documental => " + lan6DocumentoDossier.getAsuntoDocumental());
                log.info("dni tercero => " + lan6DocumentoDossier.getNumTitular());
//                log.info("fecha" + lan6DocumentoDossier.getFechaEntrega());
//                log.info("nombre_del_documento " + lan6DocumentoDossier.getTitulo());
                log.info("DATOS RECIBIDOS DEL JSP fijos");

                listaDocumentosLan6 = servicios.buscarDocumentosDossier(lan6DocumentoDossier);
                codigoOperacion = "0";

                log.info("longitud LISTA DOCUMENTOS LAN6 - " + listaDocumentosLan6.size());
            } catch (Lan6Excepcion e) {
                log.error("problemas en BusquedaDocumentosAportadosDokusi" + e);
                codigoOperacion = "2 - " + e.getMensajeExcepcion();
            }
        } else {
            codigoOperacion = "1";
        }

        //xml
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");

        if (listaDocumentosLan6 != null) {
            int i = 1;
            xmlSalida.append("<CODIGO_OPERACION>");
            xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            for (Lan6Documento fila : listaDocumentosLan6) {

                xmlSalida.append("<FILA>");
                xmlSalida.append("<TITULO>");
                xmlSalida.append(fila.getTitulo());
                log.info("TITULO " + i + ": " + fila.getTitulo());
                xmlSalida.append("</TITULO>");
                xmlSalida.append("<NOMBRE>");
                xmlSalida.append(fila.getNombre());
                log.info("NOMBRE " + i + ": " + fila.getNombre());
                xmlSalida.append("</NOMBRE>");
                xmlSalida.append("<FECHA>");

                String strdate = null;

                if (fila.getFechaCreacion() != null) {
                    strdate = sdf.format(fila.getFechaCreacion().getTime());
                }
                xmlSalida.append(strdate);
                xmlSalida.append("</FECHA>");
                xmlSalida.append("<ID_DOCUMENTO>");
                xmlSalida.append(fila.getIdDocumento());
                xmlSalida.append("</ID_DOCUMENTO>");
                xmlSalida.append("</FILA>");
                i++;
            }
        } else {
            xmlSalida.append("<CODIGO_OPERACION>");
            xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
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
        }//try-catch*/

    }

    //Nuevo Metodo Consulta en DOKUSI de los documentos aportados
    public void ConsultaDocumentosAportadosDokusi(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Lan6UtilExcepcion, Lan6Excepcion, SQLException, Exception {
        log.info("begin ConsultaDocumentosAportadosDokusi" + numExpediente);

        //Recuperamos los datos seleccionados del formulario
        String camposArray[] = numExpediente.split(",");

        String id_documento = camposArray[1];
        String expediente = camposArray[0];
        log.info("DATOS DEL JSP ConsultaDocumentosAportadosDokusi" + id_documento + " " + expediente);
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        String dniTercero = MeLanbide50Manager.getInstance().getDniTercero(codOrganizacion, expediente, adaptador, codTramite);

        log.info("datos recuperados dle tercero segun el expediente" + dniTercero);
        log.info("begin ConsultaDocumentosAportadosDokusi");
        List<Lan6Documento> ListaDocumentosLan6 = null;
        // Documentos aportados anteriormente
        try {
//                String idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_" + "LAN62_CONC");
//                log.debug("Id Proc Platea => " + idProcedimiento);
//                Lan6DossierServicios servicios = new Lan6DossierServicios(idProcedimiento);
                Lan6DossierServicios servicios = new Lan6DossierServicios(expediente.split(ConstantesDatos.BARRA)[1]); // 2.2 
               
            List<Lan6DocumentoDossier> listaDocDossier = new ArrayList<Lan6DocumentoDossier>();
            Lan6DocumentoDossier docDossier = new Lan6DocumentoDossier();

            docDossier.setIdDocumento(id_documento);//ID del Documento a consultar
            listaDocDossier.add(docDossier);
            listaDocDossier = servicios.consultarDocumentosDossier(listaDocDossier, dniTercero);// Identificador del solicitante del expediente

            /*docDossier.setIdDocumento("09f4240180753e25");//ID del Documento a consultar
            listaDocDossier.add(docDossier);
            listaDocDossier = servicios.consultarDocumentosDossier(listaDocDossier,"44100103H");// Identificador del solicitante del expediente
            
             */
            log.info("LISTA DOCUMENTOS listaDocDossier " + id_documento + " " + dniTercero);
            log.info("tamaño del documento" + listaDocDossier.size());

            for (Lan6DocumentoDossier fila : listaDocDossier) {
                log.info("recogemos el documento");
                String nombrePDF = fila.getIdRutaPif();
                log.info("nombre del documento: " + nombrePDF);
                String camposArray2[] = nombrePDF.split("/");

                String Nombre_del_documento = camposArray2[2];
                byte[] pdf = fila.getContenido();
                log.info("nombre del documento: pdf[0] " + Nombre_del_documento);

                try {
                    log.info("dento del pdf");
                    log.info("pdf.length: " + pdf.length);
                    response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                    response.setHeader("Content-Disposition", "attachment;filename=" + Nombre_del_documento);
                    response.setContentLength(pdf.length);
                    response.getOutputStream().write(pdf);
                    response.getOutputStream().flush();
                    response.getOutputStream().close();

                    log.info("documento " + Nombre_del_documento + " descargado.");
                } catch (IOException e) {
                    log.error("Error en ConsultaDocumentosAportadosDokusi", e);
                }
            }
        } catch (Lan6Excepcion e) {
        }//try-catch*/ 
    }//class

}
