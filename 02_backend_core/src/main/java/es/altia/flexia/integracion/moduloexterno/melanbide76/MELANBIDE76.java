
package es.altia.flexia.integracion.moduloexterno.melanbide76;

import es.altia.agora.business.escritorio.UsuarioValueObject;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide76.i18n.MeLanbide76I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide76.manager.MeLanbide76Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide76.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide76.util.ConstantesMeLanbide76;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.MinimisVO;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.DatosTablaDesplegableExtVO;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide76.vo.DesplegableExternoVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MELANBIDE76 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE76.class);
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
        log.debug("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide76/melanbide76.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<ContratacionVO> listaAccesos = MeLanbide76Manager.getInstance().getDatosContratacion(numExpediente, codOrganizacion, adapt);
                if (listaAccesos.size() > 0) {
                    request.setAttribute("listaAccesos", listaAccesos);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de contrataciones - MELANBIDE76 - cargarPantallaPrincipal", ex);
            }
        }

        return url;
    }
    
    public String cargarPantallaMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaMinimis de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide76/minimis.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<MinimisVO> listaMinimis = MeLanbide76Manager.getInstance().getDatosMinimis(numExpediente, codOrganizacion, adapt);
                if (listaMinimis.size() > 0) {
                    request.setAttribute("listaMinimis", listaMinimis);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de minimis - MELANBIDE76 - cargarPantallaMinimis", ex);
            }
        }

        return url;
    }
    
    private List<DesplegableAdmonLocalVO> traducirDesplegable(HttpServletRequest request, List<DesplegableAdmonLocalVO> desplegable){
        
        for (DesplegableAdmonLocalVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().equals("") ){
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }
        
        return desplegable;
    }
    
    public String cargarNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String urlnuevaContratacion = "/jsp/extension/melanbide76/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;
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
            //Cargamos en el request los valores de los desplegables
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_SEXO, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaSexo.size() > 0) {
                listaSexo = traducirDesplegable(request, listaSexo);
                request.setAttribute("listaSexo", listaSexo);
            }
            List<DesplegableAdmonLocalVO> listaMayor45 = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_BOOL, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaMayor45.size() > 0) {
                listaMayor45 = traducirDesplegable(request, listaMayor45);
                request.setAttribute("listaMayor45", listaMayor45);
            }
            List<DesplegableAdmonLocalVO> listaFinFormativa = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_BOOL, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaFinFormativa.size() > 0) {
                listaFinFormativa = traducirDesplegable(request, listaFinFormativa);
                request.setAttribute("listaFinFormativa", listaFinFormativa);
            }
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_JORN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaJornada.size() > 0) {
                listaJornada = traducirDesplegable(request, listaJornada);
                request.setAttribute("listaJornada", listaJornada);
            }
            List<DesplegableAdmonLocalVO> listaGrupoCotizacion = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_GCOT, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaGrupoCotizacion.size() > 0) {
                listaGrupoCotizacion = traducirDesplegable(request, listaGrupoCotizacion);
                request.setAttribute("listaGrupoCotizacion", listaGrupoCotizacion);
            }
            List<DesplegableAdmonLocalVO> listaTipRetribucion = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_DTRT, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaTipRetribucion.size() > 0) {
                listaTipRetribucion = traducirDesplegable(request, listaTipRetribucion);
                request.setAttribute("listaTipRetribucion", listaTipRetribucion);
            }
            
            //Desplegables externos
            DatosTablaDesplegableExtVO datosTablaDesplegableOcupaciones = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_OCIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            String tablaOcupaciones = datosTablaDesplegableOcupaciones.getTabla();
            String campoCodigoOcupaciones = datosTablaDesplegableOcupaciones.getCampoCodigo();
            String campoValorOcupaciones = datosTablaDesplegableOcupaciones.getCampoValor();
            List<DesplegableExternoVO> listaOcupacion = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaOcupaciones, campoCodigoOcupaciones, campoValorOcupaciones, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaOcupacion.size() > 0) {
                request.setAttribute("listaOcupacion", listaOcupacion);
            }
            DatosTablaDesplegableExtVO datosTablaDesplegableTitulaciones = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_TIIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            String tablaTitulaciones = datosTablaDesplegableTitulaciones.getTabla();
            String campoCodigoTitulaciones = datosTablaDesplegableTitulaciones.getCampoCodigo();
            String campoValorTitulaciones = datosTablaDesplegableTitulaciones.getCampoValor();
            List<DesplegableExternoVO> listaTitulacion = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaTitulaciones, campoCodigoTitulaciones, campoValorTitulaciones, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaTitulacion.size() > 0) {
                request.setAttribute("listaTitulacion", listaTitulacion);
            }
            DatosTablaDesplegableExtVO datosTablaDesplegableCProfesionales = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_CPIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            String tablaCProfesionales = datosTablaDesplegableCProfesionales.getTabla();
            String campoCodigoCProfesionales = datosTablaDesplegableCProfesionales.getCampoCodigo();
            String campoValorCProfesionales = datosTablaDesplegableCProfesionales.getCampoValor();
            List<DesplegableExternoVO> listaCProfesionalidad = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaCProfesionales, campoCodigoCProfesionales, campoValorCProfesionales, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaCProfesionalidad.size() > 0) {
                request.setAttribute("listaCProfesionalidad", listaCProfesionalidad);
            }
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva contrataciï¿½n : " + ex.getMessage());
        }
        return urlnuevaContratacion;
    }

    public String cargarModificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevaContratacion = "/jsp/extension/melanbide76/nuevaContratacion.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            // Recuperramos datos e Acceso a modificar y cargamos en el request
            if (id != null && !id.equals("")) {
                ContratacionVO datModif = MeLanbide76Manager.getInstance().getContratacionPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            //Cargamos el el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaSexo = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_SEXO, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaSexo.size() > 0) {
                listaSexo = traducirDesplegable(request, listaSexo);
                request.setAttribute("listaSexo", listaSexo);
            }
            List<DesplegableAdmonLocalVO> listaMayor45 = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_BOOL, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaMayor45.size() > 0) {
                listaMayor45 = traducirDesplegable(request, listaMayor45);
                request.setAttribute("listaMayor45", listaMayor45);
            }
            List<DesplegableAdmonLocalVO> listaFinFormativa = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_BOOL, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaFinFormativa.size() > 0) {
                listaFinFormativa = traducirDesplegable(request, listaFinFormativa);
                request.setAttribute("listaFinFormativa", listaFinFormativa);
            }
            List<DesplegableAdmonLocalVO> listaJornada = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_JORN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaJornada.size() > 0) {
                listaJornada = traducirDesplegable(request, listaJornada);
                request.setAttribute("listaJornada", listaJornada);
            }
            List<DesplegableAdmonLocalVO> listaGrupoCotizacion = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_GCOT, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaGrupoCotizacion.size() > 0) {
                listaGrupoCotizacion = traducirDesplegable(request, listaGrupoCotizacion);
                request.setAttribute("listaGrupoCotizacion", listaGrupoCotizacion);
            }
            List<DesplegableAdmonLocalVO> listaTipRetribucion = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_DTRT, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaTipRetribucion.size() > 0) {
                listaTipRetribucion = traducirDesplegable(request, listaTipRetribucion);
                request.setAttribute("listaTipRetribucion", listaTipRetribucion);
            }
            
            //Desplegables externos
            DatosTablaDesplegableExtVO datosTablaDesplegableOcupaciones = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_OCIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            String tablaOcupaciones = datosTablaDesplegableOcupaciones.getTabla();
            String campoCodigoOcupaciones = datosTablaDesplegableOcupaciones.getCampoCodigo();
            String campoValorOcupaciones = datosTablaDesplegableOcupaciones.getCampoValor();
            List<DesplegableExternoVO> listaOcupacion = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaOcupaciones, campoCodigoOcupaciones, campoValorOcupaciones, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaOcupacion.size() > 0) {
                request.setAttribute("listaOcupacion", listaOcupacion);
            }
            DatosTablaDesplegableExtVO datosTablaDesplegableTitulaciones = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_TIIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            String tablaTitulaciones = datosTablaDesplegableTitulaciones.getTabla();
            String campoCodigoTitulaciones = datosTablaDesplegableTitulaciones.getCampoCodigo();
            String campoValorTitulaciones = datosTablaDesplegableTitulaciones.getCampoValor();
            List<DesplegableExternoVO> listaTitulacion = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaTitulaciones, campoCodigoTitulaciones, campoValorTitulaciones, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaTitulacion.size() > 0) {
                request.setAttribute("listaTitulacion", listaTitulacion);
            }
            DatosTablaDesplegableExtVO datosTablaDesplegableCProfesionales = MeLanbide76Manager.getInstance().getDatosMapeoDesplegableExterno(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_EXT_CPIN, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            String tablaCProfesionales = datosTablaDesplegableCProfesionales.getTabla();
            String campoCodigoCProfesionales = datosTablaDesplegableCProfesionales.getCampoCodigo();
            String campoValorCProfesionales = datosTablaDesplegableCProfesionales.getCampoValor();
            List<DesplegableExternoVO> listaCProfesionalidad = MeLanbide76Manager.getInstance().getValoresDesplegablesExternos(tablaCProfesionales, campoCodigoCProfesionales, campoValorCProfesionales, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaCProfesionalidad.size() > 0) {
                request.setAttribute("listaCProfesionalidad", listaCProfesionalidad);
            }
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificaciï¿½n : " + ex.getMessage());
        }
        return urlnuevaContratacion;

    }

    public void eliminarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        String numExp = "";
        try {
            String id = (String) request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la contrataciï¿½n a elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp").toString();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide76Manager.getInstance().eliminarContratacion(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide76Manager.getInstance().getDatosContratacion(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.debug("Error al recuperar la lista de contrataciï¿½n despuï¿½s de eliminar una contrataciï¿½n");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una contrataciï¿½n: " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaContratacion(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void crearNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        ContratacionVO nuevaContratacion = new ContratacionVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            String numExp = (String) request.getParameter("expediente");
            
            String oferta = (String) request.getParameter("oferta");
            String idContrato1 = (String) request.getParameter("idContrato1");
            String idContrato2 = (String) request.getParameter("idContrato2");
            
            String dni = (String) request.getParameter("dni");
            String nombre = (String) request.getParameter("nombre");
            String apellido1 = (String) request.getParameter("apellido1");
            String apellido2 = (String) request.getParameter("apellido2");
            String fechaNacimiento = (String) request.getParameter("fechaNacimiento");
            String edad = (String) request.getParameter("edad");
            String sexo = (String) request.getParameter("sexo");
            String mayor45 = (String) request.getParameter("mayor45");
            String finFormativa = (String) request.getParameter("finFormativa");
            String codFormativa = (String) request.getParameter("codFormativa");
            String denFormativa = (String) request.getParameter("denFormativa");
            
            String puesto = (String) request.getParameter("puesto");
            String ocupacion = (String) request.getParameter("ocupacion");
            String desOcupacion = (String) request.getParameter("desOcupacion");
            String desOcupacionLibre = (String) request.getParameter("desOcupacionLibre");
            String desTitulacionLibre = (String) request.getParameter("desTitulacionLibre");
            String titulacion = (String) request.getParameter("titulacion");
            String cProfesionalidad = (String) request.getParameter("cProfesionalidad");
            String modalidadContrato = (String) request.getParameter("modalidadContrato");
            String jornada = (String) request.getParameter("jornada");
            String porcJornada = (String) request.getParameter("porcJornada").replace(",", ".");
            String horasConv = (String) request.getParameter("horasConv");
            String fechaInicio = (String) request.getParameter("fechaInicio");
            String fechaFin = (String) request.getParameter("fechaFin");
            String mesesContrato = (String) request.getParameter("mesesContrato");
            String grupoCotizacion = (String) request.getParameter("grupoCotizacion");
            String direccionCT = (String) request.getParameter("direccionCT");
            String numSS = (String) request.getParameter("numSS");
            String costeContrato = (String) request.getParameter("costeContrato").replace(",", ".");
            String tipRetribucion = (String) request.getParameter("tipRetribucion");
            
            String importeSub = (String) request.getParameter("importeSub").replace(",", ".");
            
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            
            nuevaContratacion.setNumExp(numExp);
            
            nuevaContratacion.setOferta(oferta);
            nuevaContratacion.setIdContrato1(idContrato1);
            nuevaContratacion.setIdContrato2(idContrato2);
            
            nuevaContratacion.setDni(dni);
            nuevaContratacion.setNombre(nombre);
            nuevaContratacion.setApellido1(apellido1);
            nuevaContratacion.setApellido2(apellido2);
            if (fechaNacimiento != null && !"".equals(fechaNacimiento)) {
                nuevaContratacion.setFechaNacimiento(new java.sql.Date(formatoFecha.parse(fechaNacimiento).getTime()));
            }
            if (edad != null && !"".equals(edad)) {
                nuevaContratacion.setEdad(Integer.parseInt(edad));
            }
            nuevaContratacion.setSexo(sexo);
            nuevaContratacion.setMayor45(mayor45);
            nuevaContratacion.setFinFormativa(finFormativa);
            nuevaContratacion.setCodFormativa(codFormativa);
            nuevaContratacion.setDenFormativa(denFormativa);
            
            nuevaContratacion.setPuesto(puesto);
            nuevaContratacion.setOcupacion(ocupacion);
            nuevaContratacion.setDesOcupacion(desOcupacion);
            nuevaContratacion.setDesOcupacionLibre(desOcupacionLibre);
            nuevaContratacion.setDesTitulacionLibre(desTitulacionLibre);
            nuevaContratacion.setTitulacion(titulacion);
            nuevaContratacion.setcProfesionalidad(cProfesionalidad);
            nuevaContratacion.setModalidadContrato(modalidadContrato);
            nuevaContratacion.setJornada(jornada);
            if (porcJornada != null && !"".equals(porcJornada)) {
                nuevaContratacion.setPorcJornada(Double.parseDouble(porcJornada));
            }
            if (horasConv != null && !"".equals(horasConv)) {
                nuevaContratacion.setHorasConv(Integer.parseInt(horasConv));
            }
            if (fechaInicio != null && !"".equals(fechaInicio)) {
                nuevaContratacion.setFechaInicio(new java.sql.Date(formatoFecha.parse(fechaInicio).getTime()));
            }
            if (fechaFin != null && !"".equals(fechaFin)) {
                nuevaContratacion.setFechaFin(new java.sql.Date(formatoFecha.parse(fechaFin).getTime()));
            }
            nuevaContratacion.setMesesContrato(mesesContrato);
            nuevaContratacion.setGrupoCotizacion(grupoCotizacion);
            nuevaContratacion.setDireccionCT(direccionCT);
            nuevaContratacion.setNumSS(numSS);
            if (costeContrato != null && !"".equals(costeContrato)) {
                nuevaContratacion.setCosteContrato(Double.parseDouble(costeContrato));
            }
            nuevaContratacion.setTipRetribucion(tipRetribucion);
            
            if (importeSub != null && !"".equals(importeSub)) {
                nuevaContratacion.setImporteSub(Double.parseDouble(importeSub));
            }
            
            
            MeLanbide76Manager meLanbide76Manager = MeLanbide76Manager.getInstance();
            boolean insertOK = meLanbide76Manager.crearNuevaContratacion(nuevaContratacion, adapt);
            if (insertOK) {
                log.debug("Contrataciï¿½n insertada correctamente");
                lista = meLanbide76Manager.getDatosContratacion(numExp, codOrganizacion, adapt);

            } else {
                log.debug("No se ha insertado correctamente la nueva contrataciï¿½n");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parametros recibidos del jsp al objeto ContratacionVO" + ex.getMessage());
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaContratacion(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            
            String numExp = (String) request.getParameter("expediente");
            
            String oferta = (String) request.getParameter("oferta");
            String idContrato1 = (String) request.getParameter("idContrato1");
            String idContrato2 = (String) request.getParameter("idContrato2");
            
            String dni = (String) request.getParameter("dni");
            String nombre = (String) request.getParameter("nombre");
            String apellido1 = (String) request.getParameter("apellido1");
            String apellido2 = (String) request.getParameter("apellido2");
            String fechaNacimiento = (String) request.getParameter("fechaNacimiento");
            String edad = (String) request.getParameter("edad");
            String sexo = (String) request.getParameter("sexo");
            String mayor45 = (String) request.getParameter("mayor45");
            String finFormativa = (String) request.getParameter("finFormativa");
            String codFormativa = (String) request.getParameter("codFormativa");
            String denFormativa = (String) request.getParameter("denFormativa");
            
            String puesto = (String) request.getParameter("puesto");
            String ocupacion = (String) request.getParameter("ocupacion");
            String desOcupacion = (String) request.getParameter("desOcupacion");
            String desOcupacionLibre = (String) request.getParameter("desOcupacionLibre");
            String desTitulacionLibre = (String) request.getParameter("desTitulacionLibre");
            String titulacion = (String) request.getParameter("titulacion");
            String cProfesionalidad = (String) request.getParameter("cProfesionalidad");
            String modalidadContrato = (String) request.getParameter("modalidadContrato");
            String jornada = (String) request.getParameter("jornada");
            String porcJornada = (String) request.getParameter("porcJornada").replace(",", ".");
            String horasConv = (String) request.getParameter("horasConv");
            String fechaInicio = (String) request.getParameter("fechaInicio");
            //log.debug("++++++++fechaInicio: " + fechaInicio);
            String fechaFin = (String) request.getParameter("fechaFin");
            //log.debug("++++++++fechaFin: " + fechaFin);
            String mesesContrato = (String) request.getParameter("mesesContrato");
            String grupoCotizacion = (String) request.getParameter("grupoCotizacion");
            String direccionCT = (String) request.getParameter("direccionCT");
            String numSS = (String) request.getParameter("numSS");
            String costeContrato = (String) request.getParameter("costeContrato").replace(",", ".");
            String tipRetribucion = (String) request.getParameter("tipRetribucion");
            
            String importeSub = (String) request.getParameter("importeSub").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la contrataciï¿½n a modificar ");
                codigoOperacion = "3";
            } else {
                ContratacionVO datModif = MeLanbide76Manager.getInstance().getContratacionPorID(id, adapt);
                numExp = datModif.getNumExp();
                datModif.setId(Integer.parseInt(id));

                SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                
                
                datModif.setNumExp(numExp);

                datModif.setOferta(oferta);
                datModif.setIdContrato1(idContrato1);
                datModif.setIdContrato2(idContrato2);

                datModif.setDni(dni);
                datModif.setNombre(nombre);
                datModif.setApellido1(apellido1);
                datModif.setApellido2(apellido2);
                datModif.setFechaNacimiento(null);
                if (fechaNacimiento != null && !"".equals(fechaNacimiento)) {
                    datModif.setFechaNacimiento(new java.sql.Date(formatoFecha.parse(fechaNacimiento).getTime()));
                }
                if (edad != null && !"".equals(edad)) {
                    datModif.setEdad(Integer.parseInt(edad));
                }
                datModif.setSexo(sexo);
                datModif.setMayor45(mayor45);
                datModif.setFinFormativa(finFormativa);
                datModif.setCodFormativa(codFormativa);
                datModif.setDenFormativa(denFormativa);

                datModif.setPuesto(puesto);
                datModif.setOcupacion(ocupacion);
                datModif.setDesOcupacion(desOcupacion);
                datModif.setDesOcupacionLibre(desOcupacionLibre);
                datModif.setDesTitulacionLibre(desTitulacionLibre);
                datModif.setTitulacion(titulacion);
                datModif.setcProfesionalidad(cProfesionalidad);
                datModif.setModalidadContrato(modalidadContrato);
                datModif.setJornada(jornada);
                datModif.setPorcJornada(null);
                if (porcJornada != null && !"".equals(porcJornada)) {
                    datModif.setPorcJornada(Double.parseDouble(porcJornada));
                }
                if (horasConv != null && !"".equals(horasConv)) {
                    datModif.setHorasConv(Integer.parseInt(horasConv));
                }
                datModif.setFechaInicio(null);
                if (fechaInicio != null && !"".equals(fechaInicio)) {
                    datModif.setFechaInicio(new java.sql.Date(formatoFecha.parse(fechaInicio).getTime()));
                }
                 datModif.setFechaFin(null);
                if (fechaFin != null && !"".equals(fechaFin)) {
                    datModif.setFechaFin(new java.sql.Date(formatoFecha.parse(fechaFin).getTime()));
                }
                datModif.setMesesContrato(mesesContrato);
                datModif.setGrupoCotizacion(grupoCotizacion);
                datModif.setDireccionCT(direccionCT);
                datModif.setNumSS(numSS);
                datModif.setCosteContrato(null);
                if (costeContrato != null && !"".equals(costeContrato)) {
                    datModif.setCosteContrato(Double.parseDouble(costeContrato));
                }
                datModif.setTipRetribucion(tipRetribucion);
                
                datModif.setImporteSub(null);
                if (importeSub != null && !"".equals(importeSub)) {
                    datModif.setImporteSub(Double.parseDouble(importeSub));
                }
            

                MeLanbide76Manager meLanbide76Manager = MeLanbide76Manager.getInstance();
                boolean modOK = meLanbide76Manager.modificarContratacion(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide76Manager.getDatosContratacion(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de contrataciones despuï¿½s de modificar una contrataciï¿½n : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de contrataciones despuï¿½s de modificar una contrataciï¿½n : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }

        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaContratacion(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }
    
    public String cargarNuevaMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String urlnuevaMinimis = "/jsp/extension/melanbide76/nuevoMinimis.jsp?codOrganizacion=" + codOrganizacion;
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
            //Cargamos en el request los valores de los desplegables
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_DTSV, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaEstado.size() > 0) {
                listaEstado = traducirDesplegable(request, listaEstado);
                request.setAttribute("listaEstado", listaEstado);
            }
       
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva minimis : " + ex.getMessage());
        }
        return urlnuevaMinimis;
    }

    public String cargarModificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevaMinimis = "/jsp/extension/melanbide76/nuevoMinimis.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            // Recuperramos datos e Acceso a modificar y cargamos en el request
            if (id != null && !id.equals("")) {
                MinimisVO datModif = MeLanbide76Manager.getInstance().getMinimisPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            //Cargamos el el request los valores  de los desplegables
            List<DesplegableAdmonLocalVO> listaEstado = MeLanbide76Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide76.COD_DES_DTSV, ConstantesMeLanbide76.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaEstado.size() > 0) {
                listaEstado = traducirDesplegable(request, listaEstado);
                request.setAttribute("listaEstado", listaEstado);
            }
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificaciï¿½n : " + ex.getMessage());
        }
        return urlnuevaMinimis;

    }

    public void eliminarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MinimisVO> lista = new ArrayList<MinimisVO>();
        String numExp = "";
        try {
            String id = (String) request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp").toString();
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide76Manager.getInstance().eliminarMinimis(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide76Manager.getInstance().getDatosMinimis(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.debug("Error al recuperar la lista de minimis después de eliminar una minimis");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una minimis: " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaMinimis(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void crearNuevaMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MinimisVO> lista = new ArrayList<MinimisVO>();
        MinimisVO nuevaMinimis = new MinimisVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            String numExp = (String) request.getParameter("expediente");
            
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
                nuevaMinimis.setImporte(Double.parseDouble(importe));
            }
            if (fecha != null && !"".equals(fecha)) {
                nuevaMinimis.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
            }
            
            MeLanbide76Manager meLanbide76Manager = MeLanbide76Manager.getInstance();
            boolean insertOK = meLanbide76Manager.crearNuevaMinimis(nuevaMinimis, adapt);
            if (insertOK) {
                log.debug("minimis insertada correctamente");
                lista = meLanbide76Manager.getDatosMinimis(numExp, codOrganizacion, adapt);

            } else {
                log.debug("No se ha insertado correctamente la nueva minimis");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los parametros recibidos del jsp al objeto MinimisVO" + ex.getMessage());
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaMinimis(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<MinimisVO> lista = new ArrayList<MinimisVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
            
            String numExp = (String) request.getParameter("expediente");
            
            String estado = (String) request.getParameter("estado");
            String organismo = (String) request.getParameter("organismo");
            String objeto = (String) request.getParameter("objeto");
            String importe = (String) request.getParameter("importe").replace(",", ".");
            String fecha = (String) request.getParameter("fecha");
       
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a modificar ");
                codigoOperacion = "3";
            } else {
                MinimisVO datModif = MeLanbide76Manager.getInstance().getMinimisPorID(id, adapt);
                numExp = datModif.getNumExp();
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
           
                MeLanbide76Manager meLanbide76Manager = MeLanbide76Manager.getInstance();
                boolean modOK = meLanbide76Manager.modificarMinimis(datModif, adapt);
                if (modOK) {
                    try {
                        lista = meLanbide76Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de minimis después de modificar una minimis : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de minimis después de modificar una minimis : " + ex.getMessage());
                    }
                } else {
                    codigoOperacion = "2";
                }
            }

        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "2";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaMinimis(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);

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
                // Conexiï¿½n al esquema genï¿½rico
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
    
    
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide76.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide76.FICHERO_PROPIEDADES);
        
        try {
            if (!descripcion.isEmpty()) {

                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide76.CODIGO_IDIOMA_EUSKERA) {
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

    private int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide76.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {

            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide76.CODIGO_IDIOMA_CASTELLANO;
        }

        return idioma;
    }
    
    private String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    // ----------------------------------------------------------------------------------------------------------
    // ---------------    XML    --------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
    private void retornarXML(String salida, HttpServletResponse response) {
        try {
            if (salida != null) {
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(salida);
                out.flush();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String obtenerXmlSalidaContratacion(HttpServletRequest request, String codigoOperacion, List<ContratacionVO> lista) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (ContratacionVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            
            xmlSalida.append("<NOFECONT>");
            xmlSalida.append(fila.getOferta());
            xmlSalida.append("</NOFECONT>");
            xmlSalida.append("<IDCONT1>");
            xmlSalida.append(fila.getIdContrato1());
            xmlSalida.append("</IDCONT1>");
            xmlSalida.append("<IDCONT2>");
            xmlSalida.append(fila.getIdContrato2());
            xmlSalida.append("</IDCONT2>");
            
            xmlSalida.append("<DNICONT>");
            xmlSalida.append(fila.getDni());
            xmlSalida.append("</DNICONT>");
            xmlSalida.append("<NOMCONT>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMCONT>");
            xmlSalida.append("<APE1CONT>");
            xmlSalida.append(fila.getApellido1());
            xmlSalida.append("</APE1CONT>");
            xmlSalida.append("<APE2CONT>");
            xmlSalida.append(fila.getApellido2());
            xmlSalida.append("</APE2CONT>");
            xmlSalida.append("<FECHNACCONT>");
            if (fila.getFechaNacimiento()!= null) {
                xmlSalida.append(dateFormat.format(fila.getFechaNacimiento()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHNACCONT>");
            xmlSalida.append("<EDADCONT>");
            if (fila.getEdad() != null && !"".equals(fila.getEdad())) {
                xmlSalida.append(fila.getEdad());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</EDADCONT>");
            xmlSalida.append("<SEXOCONT>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesSexo()));
            xmlSalida.append("</SEXOCONT>");
            xmlSalida.append("<MAY45CONT>");
            xmlSalida.append(fila.getMayor45());
            xmlSalida.append("</MAY45CONT>");
            xmlSalida.append("<ACCFORCONT>");
            xmlSalida.append(fila.getFinFormativa());
            xmlSalida.append("</ACCFORCONT>");
            xmlSalida.append("<CODFORCONT>");
            xmlSalida.append(fila.getCodFormativa());
            xmlSalida.append("</CODFORCONT>");
            xmlSalida.append("<DENFORCONT>");
            xmlSalida.append(fila.getDenFormativa());
            xmlSalida.append("</DENFORCONT>");
            
            xmlSalida.append("<PUESTOCONT>");
            xmlSalida.append(fila.getPuesto());
            xmlSalida.append("</PUESTOCONT>");
            xmlSalida.append("<CODOCUCONT>");
            xmlSalida.append(fila.getOcupacion());
            xmlSalida.append("</CODOCUCONT>");
            xmlSalida.append("<OCUCONT>");
            if (fila.getDesOcupacionLibre()!= null && !"".equals(fila.getDesOcupacionLibre())) {
                xmlSalida.append(fila.getDesOcupacionLibre());
            } else {
                xmlSalida.append(fila.getDesOcupacion());
            }
            xmlSalida.append("</OCUCONT>");
            xmlSalida.append("<DESTITULACION>");
            xmlSalida.append(fila.getDesTitulacionLibre());
            xmlSalida.append("</DESTITULACION>");
            xmlSalida.append("<TITULACION>");
            xmlSalida.append(fila.getDesTitulacion());
            xmlSalida.append("</TITULACION>");
            xmlSalida.append("<CPROFESIONALIDAD>");
            xmlSalida.append(fila.getDesCProfesionalidad());
            xmlSalida.append("</CPROFESIONALIDAD>");
            xmlSalida.append("<MODCONT>");
            xmlSalida.append(fila.getModalidadContrato());
            xmlSalida.append("</MODCONT>");
            xmlSalida.append("<JORCONT>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesJornada()));
            xmlSalida.append("</JORCONT>");
            xmlSalida.append("<PORCJOR>");
            if (fila.getPorcJornada()!= null && !"".equals(fila.getPorcJornada())) {
                xmlSalida.append(fila.getPorcJornada());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</PORCJOR>");
            xmlSalida.append("<HORASCONV>");
            if (fila.getHorasConv() != null && !"".equals(fila.getHorasConv())) {
                xmlSalida.append(fila.getHorasConv());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</HORASCONV>");
            xmlSalida.append("<FECHINICONT>");
            if (fila.getFechaInicio()!= null) {
                xmlSalida.append(dateFormat.format(fila.getFechaInicio()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHINICONT>");
            xmlSalida.append("<FECHFINCONT>");
            if (fila.getFechaFin()!= null) {
                xmlSalida.append(dateFormat.format(fila.getFechaFin()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHFINCONT>");
            xmlSalida.append("<DURCONT>");
            if (fila.getMesesContrato()!= null && !"".equals(fila.getMesesContrato())) {
                xmlSalida.append(fila.getMesesContrato());
            } else {
                xmlSalida.append("-");
            }
            xmlSalida.append("</DURCONT>");
            xmlSalida.append("<GRSS>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesGrupoCotizacion()));
            xmlSalida.append("</GRSS>");
            xmlSalida.append("<DIRCENTRCONT>");
            xmlSalida.append(fila.getDireccionCT());
            xmlSalida.append("</DIRCENTRCONT>");
            xmlSalida.append("<NSSCONT>");
            xmlSalida.append(fila.getNumSS());
            xmlSalida.append("</NSSCONT>");
            xmlSalida.append("<CSTCONT>");
            if (fila.getCosteContrato()!= null && !"".equals(fila.getCosteContrato())) {
                xmlSalida.append(fila.getCosteContrato());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</CSTCONT>");
            xmlSalida.append("<TIPRSB>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesTipRetribucion()));
            xmlSalida.append("</TIPRSB>");
            
            xmlSalida.append("<IMPSUBVCONT>");
            if (fila.getImporteSub()!= null && !"".equals(fila.getImporteSub())) {
                xmlSalida.append(fila.getImporteSub());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</IMPSUBVCONT>");
            
            
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }
    
    private String obtenerXmlSalidaMinimis(HttpServletRequest request, String codigoOperacion, List<MinimisVO> lista) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (MinimisVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            
            xmlSalida.append("<ESTADO>");
            xmlSalida.append(fila.getEstado());
            xmlSalida.append("</ESTADO>");
            xmlSalida.append("<ORGANISMO>");
            xmlSalida.append(fila.getOrganismo());
            xmlSalida.append("</ORGANISMO>");
            xmlSalida.append("<OBJETO>");
            xmlSalida.append(fila.getObjeto());
            xmlSalida.append("</OBJETO>");
            xmlSalida.append("<IMPORTE>");
            if (fila.getImporte()!= null && !"".equals(fila.getImporte())) {
                xmlSalida.append(fila.getImporte());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</IMPORTE>");
           
            xmlSalida.append("<FECHA>");
            if (fila.getFecha()!= null) {
                xmlSalida.append(dateFormat.format(fila.getFecha()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHA>");
          
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }
    
    
}
