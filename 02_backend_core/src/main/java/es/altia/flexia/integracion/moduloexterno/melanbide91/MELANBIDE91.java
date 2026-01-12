package es.altia.flexia.integracion.moduloexterno.melanbide91;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide91.dao.MeLanbide91DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.i18n.MeLanbide91I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide91.manager.MeLanbide91Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide91.util.ConstantesMeLanbide91;
import es.altia.flexia.integracion.moduloexterno.melanbide91.util.MeLanbide91MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide91.util.MeLanbide91Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.ContrGenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.SubvenSolicVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
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

public class MELANBIDE91 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE91.class);
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final MeLanbide91Utils m91Utils = new MeLanbide91Utils();
   
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
        // get idioma
        int idioma = 1;
        String url = "/jsp/extension/melanbide91/melanbide91.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<ContrGenVO> listaAccesos = MeLanbide91Manager.getInstance().getListaAccesos(numExpediente,idioma, codOrganizacion, adapt);
                if (listaAccesos != null && listaAccesos.size() > 0) {
                    request.setAttribute("listaAccesos", listaAccesos);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de accesos - MELANBIDE91 - cargarPantallaPrincipal", ex);
            }
        }

        return url;
    }

    public String cargarPantallaPrincipalSubvenciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaPrincipalSubvenciones de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide91/melanbide91Subvencion.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<SubvenSolicVO> listaAccesos = MeLanbide91Manager.getInstance().getListaSubvenciones(numExpediente,codTramite, codOrganizacion, adapt);
                if (listaAccesos.size() > 0) {
                    request.setAttribute("listaSubvenciones", listaAccesos);
                }
                
                
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de accesos - MELANBIDE91 - cargarPantallaPrincipal", ex);
            }
        }

        return url;
    }

    public String cargarNuevoAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoAcceso - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String url = "/jsp/extension/melanbide91/nuevoAcceso.jsp?codOrganizacion=" + codOrganizacion;
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
            
            int idioma= m91Utils.getIdioma(request);
            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaSINO = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            
            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaJORNADA = this.getListaValoresListaJORNADA(codOrganizacion,idioma,numExpediente);
            request.setAttribute("listaJORNADA", listaJORNADA);
            

            String codProcedimiento = MeLanbide91Utils.getCodigoProcedimientoFromNumExpediente(numExp);
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            String codCampoDesplegable = ConfigurationParameter.getParameter(codOrganizacion  + ConstantesMeLanbide91.BARRA_SEPARADORA + ConstantesMeLanbide91.MODULO_INTEGRACION +  ConstantesMeLanbide91.BARRA_SEPARADORA + this.getNombreModulo() + ConstantesMeLanbide91.BARRA_SEPARADORA + codProcedimiento + ConstantesMeLanbide91.BARRA_SEPARADORA + "COD_DES_SEXO", this.getNombreModulo());
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), codCampoDesplegable);
            listaSINO = salidaIntegracion.getCampoDesplegable().getValores();

            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaSINOReq = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(listaSINO!=null && !listaSINO.isEmpty() ){
                for (ValorCampoDesplegableModuloIntegracionVO valorCampoDesplegableModuloIntegracionVO : listaSINO) {
                    valorCampoDesplegableModuloIntegracionVO.setDescripcion(MeLanbide91Manager.getInstance().getDescripcionDesplegableByIdioma(idioma, valorCampoDesplegableModuloIntegracionVO.getDescripcion()));
                    listaSINOReq.add(valorCampoDesplegableModuloIntegracionVO);
                }
                //MeLanbide91Manager.getInstance().
            }
            request.setAttribute("listaSINO", listaSINOReq);

            request.setAttribute("listaSINO", listaSINO);

            //codCampoDesplegable = ConfigurationParameter.getParameter(codOrganizacion  + ConstantesMeLanbide91.BARRA_SEPARADORA + ConstantesMeLanbide91.MODULO_INTEGRACION +  ConstantesMeLanbide91.BARRA_SEPARADORA + this.getNombreModulo() + ConstantesMeLanbide91.BARRA_SEPARADORA + codProcedimiento + ConstantesMeLanbide91.BARRA_SEPARADORA + "JORN", this.getNombreModulo());
           // salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), codCampoDesplegable);
           // listaJORNADA = salidaIntegracion.getCampoDesplegable().getValores();
            //request.setAttribute("listaJORNADA", listaJORNADA);
            

            request.setAttribute("listaSINO", listaSINO);
                    

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nuevo acceso : " + ex.getMessage());
        }
        return url;
    }

    public String cargarModificarAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarAcceso - " + numExpediente);
        String nuevo = "0";
        String url = "/jsp/extension/melanbide91/nuevoAcceso.jsp?codOrganizacion=" + codOrganizacion;
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
                ContrGenVO datModif = MeLanbide91Manager.getInstance().getAccesoPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            int idioma= m91Utils.getIdioma(request);
            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaSINO = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            
            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaJORNADA = this.getListaValoresListaJORNADA(codOrganizacion,idioma,numExpediente);
            request.setAttribute("listaJORNADA", listaJORNADA);
            
            String codProcedimiento= MeLanbide91Utils.getCodigoProcedimientoFromNumExpediente(numExpediente);
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            String codCampoDesplegable = ConfigurationParameter.getParameter(codOrganizacion  + ConstantesMeLanbide91.BARRA_SEPARADORA + ConstantesMeLanbide91.MODULO_INTEGRACION +  ConstantesMeLanbide91.BARRA_SEPARADORA + this.getNombreModulo() + ConstantesMeLanbide91.BARRA_SEPARADORA + codProcedimiento + ConstantesMeLanbide91.BARRA_SEPARADORA + "COD_DES_SEXO", this.getNombreModulo());

            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), codCampoDesplegable);
            listaSINO=salidaIntegracion.getCampoDesplegable().getValores();

            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaSINOReq = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(listaSINO!=null && !listaSINO.isEmpty() ){
                for (ValorCampoDesplegableModuloIntegracionVO valorCampoDesplegableModuloIntegracionVO : listaSINO) {
                    valorCampoDesplegableModuloIntegracionVO.setDescripcion(MeLanbide91Manager.getInstance().getDescripcionDesplegableByIdioma(idioma, valorCampoDesplegableModuloIntegracionVO.getDescripcion()));
                    listaSINOReq.add(valorCampoDesplegableModuloIntegracionVO);
                }
                //MeLanbide91Manager.getInstance().
            }
            request.setAttribute("listaSINO", listaSINO);  

            request.setAttribute("listaSINO", listaSINO);
            
            //codCampoDesplegable = ConfigurationParameter.getParameter(codOrganizacion  + ConstantesMeLanbide91.BARRA_SEPARADORA + ConstantesMeLanbide91.MODULO_INTEGRACION +  ConstantesMeLanbide91.BARRA_SEPARADORA + this.getNombreModulo() + ConstantesMeLanbide91.BARRA_SEPARADORA + codProcedimiento + ConstantesMeLanbide91.BARRA_SEPARADORA + "JORN", this.getNombreModulo());
            //salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), codCampoDesplegable);
            //listaJORNADA=salidaIntegracion.getCampoDesplegable().getValores();
            //request.setAttribute("listaJORNADA", listaJORNADA);
            

            request.setAttribute("listaSINO", listaSINO);        
       

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return url;

    }

    public void crearNuevoAcceso(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoAcceso - " + numExpediente);
        String codigoOperacion = "-1";
        List<ContrGenVO> lista = new ArrayList<ContrGenVO>();
        ContrGenVO nuevoContrGen = new ContrGenVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            log.debug("Recojo");
            String numExp = request.getParameter("numExp");
            String nombre = request.getParameter("nombre");
            String apellido1= request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String sexo = request.getParameter("sexo");
            String dni = request.getParameter("dni");           
            String psiquica = (String) request.getParameter("psiquica").replace(",", ".");
            String fisica = (String) request.getParameter("fisica").replace(",", ".");
            String sensorial = (String) request.getParameter("sensorial").replace(",", ".");
            String fecIniStr = request.getParameter("fecIni");
            String jornada = request.getParameter("jornada");
            String porcParcial = (String) request.getParameter("porcParcial").replace(",", ".");
            
            log.debug("Cargo");
            nuevoContrGen.setNumExp(numExp);
            nuevoContrGen.setNombre(nombre);
            nuevoContrGen.setApellido1(apellido1);
            nuevoContrGen.setApellido2(apellido2);
            nuevoContrGen.setSexo(sexo);
            nuevoContrGen.setDni(dni);
                        
            if(psiquica!=null && !psiquica.isEmpty() && !psiquica.equalsIgnoreCase("null"))
            nuevoContrGen.setPsiquica(Double.parseDouble(psiquica));
            
            if(fisica!=null && !fisica.isEmpty() && !fisica.equalsIgnoreCase("null"))
            nuevoContrGen.setFisica(Double.parseDouble(fisica));
            
            if(sensorial!=null && !sensorial.isEmpty() && !sensorial.equalsIgnoreCase("null"))
            nuevoContrGen.setSensorial(Double.parseDouble(sensorial));
            
            java.util.Date fecIni =null;
            if(fecIniStr!=null && !fecIniStr.isEmpty() && !fecIniStr.equalsIgnoreCase("null")){
                fecIni=dateFormat.parse(fecIniStr);
                nuevoContrGen.setFecIni(fecIni);
            }
                        
            nuevoContrGen.setJornada(jornada);
            if(porcParcial!=null && !porcParcial.isEmpty() && !porcParcial.equalsIgnoreCase("null"))
            nuevoContrGen.setPorcParcial(Double.parseDouble(porcParcial));

            MeLanbide91Manager meLanbide91Manager = MeLanbide91Manager.getInstance();
            boolean insertOK = meLanbide91Manager.crearAcceso(nuevoContrGen, adapt);
            if (insertOK) {
                log.debug("Acceso Insertado Correctamente");
                codigoOperacion = "0";
                lista = meLanbide91Manager.getListaAccesos(numExp, codTramite, codOrganizacion, adapt);
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
        List<ContrGenVO> listaAccesos = new ArrayList<ContrGenVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String sexo = request.getParameter("sexo");
            String dni = request.getParameter("dni");
            String psiquica = (String) request.getParameter("psiquica").replace(",", ".");
            String fisica = (String) request.getParameter("fisica").replace(",", ".");
            String sensorial = (String) request.getParameter("sensorial").replace(",", ".");
            String fecIniStr = request.getParameter("fecIni");
            
            java.util.Date fecIni =null;
            if(fecIniStr!=null && !fecIniStr.isEmpty() && !fecIniStr.equalsIgnoreCase("null")){
                fecIni=dateFormat.parse(fecIniStr);
            }
            String jornada = request.getParameter("jornada");
            
            String porcParcial = (String) request.getParameter("porcParcial").replace(",", ".");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del acceso a Modificar ");
                codigoOperacion = "3";
            } else {
                ContrGenVO contrGenVOModif = new ContrGenVO();

                contrGenVOModif.setId(Integer.parseInt(id));
                contrGenVOModif.setNumExp(numExp);
                contrGenVOModif.setNombre(nombre);
                contrGenVOModif.setApellido1(apellido1);
                contrGenVOModif.setApellido2(apellido2);
                contrGenVOModif.setSexo(sexo);
                contrGenVOModif.setDni(dni);
                if(psiquica!=null && !psiquica.isEmpty() && !psiquica.equalsIgnoreCase("null"))
                contrGenVOModif.setPsiquica(Double.parseDouble(psiquica));
                if(fisica!=null && !fisica.isEmpty() && !fisica.equalsIgnoreCase("null"))
                contrGenVOModif.setFisica(Double.parseDouble(fisica));
                if(sensorial!=null && !sensorial.isEmpty() && !sensorial.equalsIgnoreCase("null"))
                contrGenVOModif.setSensorial(Double.parseDouble(sensorial));
                contrGenVOModif.setFecIni(fecIni);
                contrGenVOModif.setJornada(jornada);
                if(porcParcial!=null && !porcParcial.isEmpty() && !porcParcial.equalsIgnoreCase("null"))
                contrGenVOModif.setPorcParcial(Double.parseDouble(porcParcial));
                
                MeLanbide91Manager meLanbide91Manager = MeLanbide91Manager.getInstance();
                boolean modOK = meLanbide91Manager.modificarAcceso(contrGenVOModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        listaAccesos = meLanbide91Manager.getListaAccesos(numExp,codTramite, codOrganizacion, adapt);
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
        List<ContrGenVO> listaAccesos = new ArrayList<ContrGenVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del acceso a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide91Manager.getInstance().eliminarAcceso(id, numExp, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        listaAccesos = MeLanbide91Manager.getInstance().getListaAccesos(numExp,codTramite, codOrganizacion, adapt);
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
    
    private ArrayList<ValorCampoDesplegableModuloIntegracionVO> getListaValoresListaJORNADA(int codOrganizacion,int idioma,String numExpediente){
       ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaJORNADA = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
       ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaJORNADAReq = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
       try {
           String codProcedimiento = MeLanbide91Utils.getCodigoProcedimientoFromNumExpediente(numExpediente);
           IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
           String codCampoDesplegable = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide91.BARRA_SEPARADORA + ConstantesMeLanbide91.MODULO_INTEGRACION + ConstantesMeLanbide91.BARRA_SEPARADORA + this.getNombreModulo() + ConstantesMeLanbide91.BARRA_SEPARADORA + codProcedimiento + ConstantesMeLanbide91.BARRA_SEPARADORA + "COD_DES_JORN", this.getNombreModulo());
           SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), codCampoDesplegable);
           listaJORNADAReq = salidaIntegracion.getCampoDesplegable().getValores();
           if (listaJORNADAReq != null && !listaJORNADAReq.isEmpty()) {
                for (ValorCampoDesplegableModuloIntegracionVO valorCampoDesplegableModuloIntegracionVO : listaJORNADAReq) {
                    valorCampoDesplegableModuloIntegracionVO.setDescripcion(MeLanbide91Manager.getInstance().getDescripcionDesplegableByIdioma(idioma, valorCampoDesplegableModuloIntegracionVO.getDescripcion()));
                    listaJORNADA.add(valorCampoDesplegableModuloIntegracionVO);
                }
           
           }  
           
       } catch (Exception e) {
            log.error("error al recupear la lista de tipo datos subvencion" + e.getMessage(), e);
       }
       return listaJORNADA;
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

            request.getSession().setAttribute("mensajeImportar", MeLanbide91I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
        } catch (DataFormatException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE91.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE91.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE91.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE91.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide91I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarListaAccesos.jsp");
        return "/jsp/extension/melanbide91/recargarListaAccesos.jsp";
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
        List<ContrGenVO> lista = new ArrayList<ContrGenVO>();
        String numExp = "";
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide91Manager.getInstance().getListaAccesos(numExp,codTramite, codOrganizacion, adapt);
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
    public void actualizarPestanaSubvenciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en actualizarPestana de " + this.getClass().getSimpleName());
        String codigoOperacion = "-1";
        List<SubvenSolicVO> lista = new ArrayList<SubvenSolicVO>();
        String numExp = "";
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide91Manager.getInstance().getListaSubvenciones(numExp,codTramite, codOrganizacion, adapt);
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

    private Date stringToDate(String strDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyy");

        try {
            java.util.Date date = sdf.parse(strDate);
            java.sql.Date sqlDate = new Date(date.getTime());
            log.debug("sql date: " + strDate);
            return sqlDate;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String cargarNuevoAccesoSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoAccesoSubvencion - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String url = "/jsp/extension/melanbide91/nuevoAccesoSubvencion.jsp?codOrganizacion=" + codOrganizacion;
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
            int idioma= m91Utils.getIdioma(request);
            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaTIPODATOS = this.getListaValoresTipoDatoSubvencion(codOrganizacion,idioma,numExpediente);
            request.setAttribute("listaTIPODATOS", listaTIPODATOS);
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nuevo acceso : " + ex.getMessage());
        }
        return url;
    }

    public String cargarModificarAccesoSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarAccesoSubvencion - " + numExpediente);
        String nuevo = "0";
        String url = "/jsp/extension/melanbide91/nuevoAccesoSubvencion.jsp?codOrganizacion=" + codOrganizacion;
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
                SubvenSolicVO datModif = MeLanbide91Manager.getInstance().getSubvencionPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            int idioma= m91Utils.getIdioma(request);
            ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaTIPODATOS = this.getListaValoresTipoDatoSubvencion(codOrganizacion,idioma,numExpediente);
            request.setAttribute("listaTIPODATOS", listaTIPODATOS);
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return url;

    }

    public void crearNuevoAccesoSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
//        log.info("Entramos en crearNuevoAccesoSubvencion - " + numExpediente);
        String codigoOperacion = "-1";
        List<SubvenSolicVO> lista = new ArrayList<SubvenSolicVO>();
        SubvenSolicVO nuevoSubvenSolicVO = new SubvenSolicVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            log.debug("Recojo");
            String numExp = request.getParameter("numExp");
            String tipodatos = request.getParameter("tipodatos");
            String tipo = request.getParameter("tipoHtml");
            String fechaStr = request.getParameter("fecha");
            String destino = request.getParameter("destino");            
            String coste = (String) request.getParameter("coste").replace(",", ".");          
           
            nuevoSubvenSolicVO.setNumExp(numExp);
            nuevoSubvenSolicVO.setTipoDatos(tipodatos);
            nuevoSubvenSolicVO.setTipo(tipo);
            java.util.Date fecha =null;
            if(fechaStr!=null && !fechaStr.isEmpty() && !fechaStr.equalsIgnoreCase("null")){
                fecha=dateFormat.parse(fechaStr);
                nuevoSubvenSolicVO.setFecha(fecha);
            }
            nuevoSubvenSolicVO.setDestino(destino);
            
             if(coste!=null && !coste.isEmpty() && !coste.equalsIgnoreCase("null"))
            nuevoSubvenSolicVO.setCoste(Double.parseDouble(coste));                    

            MeLanbide91Manager meLanbide91Manager = MeLanbide91Manager.getInstance();
            boolean insertOK = meLanbide91Manager.crearSubvencion(nuevoSubvenSolicVO, adapt);
            if (insertOK) {
                log.debug("Subvencion Insertado Correctamente");
                codigoOperacion = "0";
                lista = meLanbide91Manager.getListaSubvenciones(numExp,codTramite, codOrganizacion, adapt);
            } else {
                log.debug("NO se ha insertado correctamente la Subvencion");
                codigoOperacion = "1";
            }
           
            
        } catch (Exception ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        
        resultado.setAtributo("lista", lista);
        
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarAccesoSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarAccesoSubvencion - " + numExpediente);
        String codigoOperacion = "-1";
        List<SubvenSolicVO> listaAccesos = new ArrayList<SubvenSolicVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            Integer id = Integer.parseInt(request.getParameter("id"));
            String numExp = request.getParameter("numExp");
            String tipodatos = request.getParameter("tipodatos");
            String tipo = request.getParameter("tipoHtml");
            String fechaStr = request.getParameter("fecha");
           // Date fecha = stringToDate(request.getParameter("fecha"));
            java.util.Date fecha =null;
            if(fechaStr!=null && !fechaStr.isEmpty() && !fechaStr.equalsIgnoreCase("null")){
                fecha=dateFormat.parse(fechaStr);
            }
            
            String destino = request.getParameter("destino");
            String coste = (String) request.getParameter("coste").replace(",", ".");
            
            

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del acceso a Modificar ");
                codigoOperacion = "3";
            } else {
                SubvenSolicVO subvenSolicVOModif = new SubvenSolicVO();

                subvenSolicVOModif.setId(id);
                subvenSolicVOModif.setNumExp(numExp);
                subvenSolicVOModif.setTipoDatos(tipodatos);
                subvenSolicVOModif.setTipo(tipo);
                subvenSolicVOModif.setFecha(fecha);
                subvenSolicVOModif.setDestino(destino);
                subvenSolicVOModif.setCoste(Double.parseDouble(coste));
                
                

                MeLanbide91Manager meLanbide91Manager = MeLanbide91Manager.getInstance();
                boolean modOK = meLanbide91Manager.modificarSubvencion(subvenSolicVOModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        listaAccesos = meLanbide91Manager.getListaSubvenciones(numExp,codTramite, codOrganizacion, adapt);
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

    public void eliminarAccesoSubvencion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarAccesoSubvencion - " + numExpediente);
        String codigoOperacion = "-1";
        List<SubvenSolicVO> listaAccesos = new ArrayList<SubvenSolicVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del acceso a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide91Manager.getInstance().eliminarSubvencion(id, numExp, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        listaAccesos = MeLanbide91Manager.getInstance().getListaSubvenciones(numExp, codTramite,codOrganizacion, adapt);
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
    
    private ArrayList<ValorCampoDesplegableModuloIntegracionVO> getListaValoresTipoDatoSubvencion(int codOrganizacion,int idioma, String numExpediente){
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaTIPODATOS = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaTIPODATOREq = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        try {
            String codProcedimiento = MeLanbide91Utils.getCodigoProcedimientoFromNumExpediente(numExpediente);
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            String codCampoDesplegable = ConfigurationParameter.getParameter(codOrganizacion + ConstantesMeLanbide91.BARRA_SEPARADORA + ConstantesMeLanbide91.MODULO_INTEGRACION + ConstantesMeLanbide91.BARRA_SEPARADORA + this.getNombreModulo() + ConstantesMeLanbide91.BARRA_SEPARADORA + codProcedimiento + ConstantesMeLanbide91.BARRA_SEPARADORA + "COD_DES_DATOS", this.getNombreModulo());
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), codCampoDesplegable);
            listaTIPODATOREq = salidaIntegracion.getCampoDesplegable().getValores();
            if (listaTIPODATOREq != null && !listaTIPODATOREq.isEmpty()) {
                for (ValorCampoDesplegableModuloIntegracionVO valorCampoDesplegableModuloIntegracionVO : listaTIPODATOREq) {
                    valorCampoDesplegableModuloIntegracionVO.setDescripcion(MeLanbide91Manager.getInstance().getDescripcionDesplegableByIdioma(idioma, valorCampoDesplegableModuloIntegracionVO.getDescripcion()));
                    listaTIPODATOS.add(valorCampoDesplegableModuloIntegracionVO);
                }
                //MeLanbide91Manager.getInstance().
            }
        } catch (Exception e) {
             log.error("error al recuperar la lista de tipo datos subvencion" + e.getMessage(), e);
        }
        return listaTIPODATOS;
    }
 
    public List<DesplegableVO> getValoresDesplegables(String des_cod,int idioma, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide91DAO meLanbide91DAO = MeLanbide91DAO.getInstance();
            return meLanbide91DAO.getValoresDesplegables(des_cod, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable : " + des_cod, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando valores de desplegable :  " + des_cod, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }    


  
      

}
