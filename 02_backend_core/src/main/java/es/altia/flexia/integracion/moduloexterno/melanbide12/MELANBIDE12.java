package es.altia.flexia.integracion.moduloexterno.melanbide12;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide12.manager.MeLanbide12Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConstantesMeLanbide12;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1ParticipanteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaExternaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaPropiaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL2ParticipanteVO;
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

public class MELANBIDE12 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE12.class);
    private static DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final MeLanbide12Manager m12Manager = new MeLanbide12Manager();
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);

        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    //Minimis___________________________________________________________________________________________________________________________________________________________________________
    public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide12/melanbide12.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaMinimisVO> listaMinimis = MeLanbide12Manager.getInstance().getDatosMinimis(numExpediente, codOrganizacion, adapt);
                if (listaMinimis.size() > 0) {
                    for (FilaMinimisVO lm : listaMinimis) {
                        log.debug("Estado antes: "+ lm.getDesEstado());
                        
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                                            log.debug("Estado despues: "+ lm.getDesEstado());
}
                    request.setAttribute("listaMinimis", listaMinimis);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de minimis - melanbide12 - cargarPantallaPrincipal", ex);
            }
        }

        return url;
    }

    public String cargarNuevoMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoMinimis - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevoMinimis = "/jsp/extension/melanbide12/nuevoMinimis.jsp?codOrganizacion=" + codOrganizacion;
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

            cargarDesplegablesSubvencion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva minimis : " + ex.getMessage());
        }
        return urlnuevoMinimis;
    }

    public String cargarModificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarMinimis - " + numExpediente);
        String nuevo = "0";
        String urlnuevoMinimis = "/jsp/extension/melanbide12/nuevoMinimis.jsp?codOrganizacion=" + codOrganizacion;
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
                FilaMinimisVO datModif = MeLanbide12Manager.getInstance().getMinimisPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            cargarDesplegablesSubvencion(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlnuevoMinimis;

    }

    public void crearNuevoMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO nuevaMinimis = new FilaMinimisVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");

            String estado = (String) request.getParameter("estado");
            String organismo = (String) request.getParameter("organismo");
            String objeto = (String) request.getParameter("objeto");
            String importe = (String) request.getParameter("importe").replace(",", ".");
            String fecha = (String) request.getParameter("fecha");

            nuevaMinimis.setNumExp(numExp);
            nuevaMinimis.setEstado(estado);
            nuevaMinimis.setOrganismo(organismo);
            nuevaMinimis.setObjeto(objeto);
            if (importe != null && !"".equals(importe)) {
                nuevaMinimis.setImporte(Double.valueOf(importe));
            }
            if (fecha != null && !"".equals(fecha)) {
                nuevaMinimis.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
            }

            MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
            boolean insertOK = melanbide12Manager.crearNuevoMinimis(nuevaMinimis, adapt);
            if (insertOK) {
                log.debug("minimis insertada correctamente");
                codigoOperacion = "0";
                lista = melanbide12Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                if (lista.size() > 0) {
                    for (FilaMinimisVO lm : lista) {
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                }
            } else {
                log.debug("NO se ha insertado correctamente la nueva minimis");
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

    public void modificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("numExp");

            String estado = (String) request.getParameter("estado");
            String organismo = (String) request.getParameter("organismo");
            String objeto = (String) request.getParameter("objeto");
            String importe = (String) request.getParameter("importe").replace(",", ".");
            String fecha = (String) request.getParameter("fecha");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaMinimisVO datModif = new FilaMinimisVO();

                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setEstado(estado);
                datModif.setOrganismo(organismo);
                datModif.setObjeto(objeto);
                if (importe != null && !"".equals(importe)) {
                    datModif.setImporte(Double.valueOf(importe));
                }
                if (fecha != null && !"".equals(fecha)) {
                    datModif.setFecha(new java.sql.Date(formatoFecha.parse(fecha).getTime()));
                }

                MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
                boolean modOK = melanbide12Manager.modificarMinimis(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = melanbide12Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de minimis después de Modificar una minimis : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de minimis después de Modificar una minimis : " + ex.getMessage());
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

    public void eliminarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a Eliminar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide12Manager.getInstance().eliminarMinimis(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide12Manager.getInstance().getDatosMinimis(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de minimis después de eliminar una minimis");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una minimis: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }
    //End Minimis_______________________________________________________________________________________________________________________________________________________________________

    //L1Participante___________________________________________________________________________________________________________________________________________________________________________
    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return url jsp L1Participante
     */
    public String cargarPantallaL1Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaL1Participante de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide12/l1Participante.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaL1ParticipanteVO> listaL1Participante = MeLanbide12Manager.getInstance().getDatosL1Participante(numExpediente, codOrganizacion, adapt);
                if (listaL1Participante.size() > 0) {
                    for (FilaL1ParticipanteVO lm : listaL1Participante) {
                        log.debug("TipoDoc antes: "+ lm.getDesTipoDoc());

                        lm.setDesTipoDoc(getDescripcionDesplegable(request, lm.getDesTipoDoc()));
                        log.debug("TipoDoc despues: "+ lm.getDesTipoDoc());
                    }
                    request.setAttribute("listaL1Participante", listaL1Participante);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de melanbide12 - cargarPantallaL1Participante", ex);
            }
        }

        return url;
    }

    public String cargarNuevoL1Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoL1Participante - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevoL1Participante = "/jsp/extension/melanbide12/nuevoL1Participante.jsp?codOrganizacion=" + codOrganizacion;
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

            cargarDesplegableL1(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva L1Participante : " + ex.getMessage());
        }
        return urlnuevoL1Participante;
    }

    public String cargarModificarL1Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarL1Participante - " + numExpediente);
        String nuevo = "0";
        String urlnuevoL1Participante = "/jsp/extension/melanbide12/nuevoL1Participante.jsp?codOrganizacion=" + codOrganizacion;
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
                FilaL1ParticipanteVO datModif = MeLanbide12Manager.getInstance().getL1ParticipantePorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            cargarDesplegableL1(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlnuevoL1Participante;

    }

    public void crearNuevoL1Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoL1Participante - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL1ParticipanteVO> lista = new ArrayList<FilaL1ParticipanteVO>();
        FilaL1ParticipanteVO nuevoL1Participante = new FilaL1ParticipanteVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");

            String tipoDoc = (String) request.getParameter("tipoDoc");
            String doc = (String) request.getParameter("doc");
            String nombre = (String) request.getParameter("nombre");
            String ape1 = (String) request.getParameter("ape1");
            String ape2 = (String) request.getParameter("ape2");
            String nss = (String) request.getParameter("nss");
            String cod_act_form = (String) request.getParameter("cod_act_form");
            String fec_ini_pract = (String) request.getParameter("fec_ini_pract");
            String fec_fin_pract = (String) request.getParameter("fec_fin_pract");
            String cc_cot = (String) request.getParameter("cc_cot");
            String dias_cot = (String) request.getParameter("dias_cot").replace(",", ".");
            String imp_solic = (String) request.getParameter("imp_solic").replace(",", ".");

            nuevoL1Participante.setNumExp(numExp);
            nuevoL1Participante.setTipoDoc(tipoDoc);
            nuevoL1Participante.setDoc(doc);
            nuevoL1Participante.setNombre(nombre);
            nuevoL1Participante.setApe1(ape1);
            nuevoL1Participante.setApe2(ape2);
            nuevoL1Participante.setNss(nss);
            nuevoL1Participante.setCodActForm(cod_act_form);
            if (fec_ini_pract != null && !"".equals(fec_ini_pract)) {
                nuevoL1Participante.setFecIniPract(new java.sql.Date(formatoFecha.parse(fec_ini_pract).getTime()));
            }
            if (fec_fin_pract != null && !"".equals(fec_fin_pract)) {
                nuevoL1Participante.setFecFinPract(new java.sql.Date(formatoFecha.parse(fec_fin_pract).getTime()));
            }
            nuevoL1Participante.setCcCot(cc_cot);
            if (dias_cot != null && !"".equals(dias_cot)) {
                nuevoL1Participante.setDiasCot(Double.valueOf(dias_cot));
            }
            if (imp_solic != null && !"".equals(imp_solic)) {
                nuevoL1Participante.setImpSolic(Double.valueOf(imp_solic));
            }

            MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
            boolean insertOK = melanbide12Manager.crearNuevoL1Participante(nuevoL1Participante, adapt);
            if (insertOK) {
                log.debug("L1Participante insertado correctamente");
                codigoOperacion = "0";
                lista = melanbide12Manager.getDatosL1Participante(numExp, codOrganizacion, adapt);
                if (lista.size() > 0) {
                    for (FilaL1ParticipanteVO lm : lista) {
                        lm.setDesTipoDoc(getDescripcionDesplegable(request, lm.getDesTipoDoc()));
                    }
                }
            } else {
                log.debug("NO se ha insertado correctamente el nuevo L1Participante");
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

    public void modificarL1Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarL1Participante - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL1ParticipanteVO> lista = new ArrayList<FilaL1ParticipanteVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("numExp");

            String tipoDoc = (String) request.getParameter("tipoDoc");
            String doc = (String) request.getParameter("doc");
            String nombre = (String) request.getParameter("nombre");
            String ape1 = (String) request.getParameter("ape1");
            String ape2 = (String) request.getParameter("ape2");
            String nss = (String) request.getParameter("nss");
            String cod_act_form = (String) request.getParameter("cod_act_form");
            String fec_ini_pract = (String) request.getParameter("fec_ini_pract");
            String fec_fin_pract = (String) request.getParameter("fec_fin_pract");
            String cc_cot = (String) request.getParameter("cc_cot");
            String dias_cot = (String) request.getParameter("dias_cot").replace(",", ".");
            String imp_solic = (String) request.getParameter("imp_solic").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del L1Participante a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaL1ParticipanteVO datModif = new FilaL1ParticipanteVO();

                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setTipoDoc(tipoDoc);
                datModif.setDoc(doc);
                datModif.setNombre(nombre);
                datModif.setApe1(ape1);
                datModif.setApe2(ape2);
                datModif.setNss(nss);
                datModif.setCodActForm(cod_act_form);
                if (fec_ini_pract != null && !"".equals(fec_ini_pract)) {
                    datModif.setFecIniPract(new java.sql.Date(formatoFecha.parse(fec_ini_pract).getTime()));
                }
                if (fec_fin_pract != null && !"".equals(fec_fin_pract)) {
                    datModif.setFecFinPract(new java.sql.Date(formatoFecha.parse(fec_fin_pract).getTime()));
                }
                datModif.setCcCot(cc_cot);
                if (dias_cot != null && !"".equals(dias_cot)) {
                    datModif.setDiasCot(Double.valueOf(dias_cot));
                }
                if (imp_solic != null && !"".equals(imp_solic)) {
                    datModif.setImpSolic(Double.valueOf(imp_solic));
                }

                MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
                boolean modOK = melanbide12Manager.modificarL1Participante(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = melanbide12Manager.getDatosL1Participante(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaL1ParticipanteVO lm : lista) {
                                lm.setDesTipoDoc(getDescripcionDesplegable(request, lm.getDesTipoDoc()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de L1Participante después de Modificar un L1Participante : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de L1Participante después de Modificar un L1Participante : " + ex.getMessage());
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

    public void eliminarL1Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarL1Participante - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL1ParticipanteVO> lista = new ArrayList<FilaL1ParticipanteVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del L1Participante a Eliminar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide12Manager.getInstance().eliminarL1Participante(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide12Manager.getInstance().getDatosL1Participante(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaL1ParticipanteVO lm : lista) {
                                lm.setDesTipoDoc(getDescripcionDesplegable(request, lm.getDesTipoDoc()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de L1Participante después de eliminar un L1Participante");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un L1Participante: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    //End L1Participante_______________________________________________________________________________________________________________________________________________________________________

    //L1EmpresaExterna___________________________________________________________________________________________________________________________________________________________________________
    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return url jsp EmpresaExterna
     */
    public String cargarPantallaL1EmpresaExterna(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaL1EmpresaExterna de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide12/l1EmpresaExterna.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaL1EmpresaExternaVO> listaL1EmpresaExterna = MeLanbide12Manager.getInstance().getDatosL1EmpresaExterna(numExpediente, codOrganizacion, adapt);
                if (listaL1EmpresaExterna.size() > 0) {
                    request.setAttribute("listaL1EmpresaExterna", listaL1EmpresaExterna);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de melanbide12 - cargarPantallaL1EmpresaExterna", ex);
            }
        }

        return url;
    }

    public String cargarNuevoL1EmpresaExterna(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoL1EmpresaExterna - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevoL1EmpresaExterna = "/jsp/extension/melanbide12/nuevoL1EmpresaExterna.jsp?codOrganizacion=" + codOrganizacion;
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
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva L1EmpresaExterna : " + ex.getMessage());
        }
        return urlnuevoL1EmpresaExterna;
    }

    public String cargarModificarL1EmpresaExterna(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarL1EmpresaExterna - " + numExpediente);
        String nuevo = "0";
        String urlnuevoL1EmpresaExterna = "/jsp/extension/melanbide12/nuevoL1EmpresaExterna.jsp?codOrganizacion=" + codOrganizacion;
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
                FilaL1EmpresaExternaVO datModif = MeLanbide12Manager.getInstance().getL1EmpresaExternaPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlnuevoL1EmpresaExterna;

    }

    public void crearNuevoL1EmpresaExterna(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoL1EmpresaExterna - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL1EmpresaExternaVO> lista = new ArrayList<FilaL1EmpresaExternaVO>();
        FilaL1EmpresaExternaVO nuevoL1EmpresaExterna = new FilaL1EmpresaExternaVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");

            String cif = (String) request.getParameter("cif");
            String denom_empr = (String) request.getParameter("denom_empr");
            String n_factura = (String) request.getParameter("n_factura");
            String fec_emis = (String) request.getParameter("fec_emis");
            String fec_pago = (String) request.getParameter("fec_pago");
            String imp_base = (String) request.getParameter("imp_base").replace(",", ".");
            String imp_iva = (String) request.getParameter("imp_iva").replace(",", ".");
            String imp_total = (String) request.getParameter("imp_total").replace(",", ".");
            String personas = (String) request.getParameter("personas").replace(",", ".");
            String imp_persona_fact = (String) request.getParameter("imp_persona_fact").replace(",", ".");
            String imp_solic = (String) request.getParameter("imp_solic").replace(",", ".");

            nuevoL1EmpresaExterna.setNumExp(numExp);
            nuevoL1EmpresaExterna.setCif(cif);
            nuevoL1EmpresaExterna.setDenomEmpr(denom_empr);
            nuevoL1EmpresaExterna.setnFactura(n_factura);
            if (fec_emis != null && !"".equals(fec_emis)) {
                nuevoL1EmpresaExterna.setFecEmis(new java.sql.Date(formatoFecha.parse(fec_emis).getTime()));
            }
            if (fec_pago != null && !"".equals(fec_pago)) {
                nuevoL1EmpresaExterna.setFecPago(new java.sql.Date(formatoFecha.parse(fec_pago).getTime()));
            }
            if (imp_base != null && !"".equals(imp_base)) {
                nuevoL1EmpresaExterna.setImpBase(Double.valueOf(imp_base));
            }
            if (imp_iva != null && !"".equals(imp_iva)) {
                nuevoL1EmpresaExterna.setImpIva(Double.valueOf(imp_iva));
            }
            if (imp_total != null && !"".equals(imp_total)) {
                nuevoL1EmpresaExterna.setImpTotal(Double.valueOf(imp_total));
            }
            if (personas != null && !"".equals(personas)) {
                nuevoL1EmpresaExterna.setPersonas(Double.valueOf(personas));
            }
            if (imp_persona_fact != null && !"".equals(imp_persona_fact)) {
                nuevoL1EmpresaExterna.setImpPersonaFact(Double.valueOf(imp_persona_fact));
            }
            if (imp_solic != null && !"".equals(imp_solic)) {
                nuevoL1EmpresaExterna.setImpSolic(Double.valueOf(imp_solic));
            }

            MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
            boolean insertOK = melanbide12Manager.crearNuevoL1EmpresaExterna(nuevoL1EmpresaExterna, adapt);
            if (insertOK) {
                log.debug("L1EmpresaExterna insertado correctamente");
                codigoOperacion = "0";
                lista = melanbide12Manager.getDatosL1EmpresaExterna(numExp, codOrganizacion, adapt);
            } else {
                log.debug("NO se ha insertado correctamente el nuevo L1EmpresaExterna");
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

    public void modificarL1EmpresaExterna(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarL1EmpresaExterna - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL1EmpresaExternaVO> lista = new ArrayList<FilaL1EmpresaExternaVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("numExp");

            String cif = (String) request.getParameter("cif");
            String denom_empr = (String) request.getParameter("denom_empr");
            String n_factura = (String) request.getParameter("n_factura");
            String fec_emis = (String) request.getParameter("fec_emis");
            String fec_pago = (String) request.getParameter("fec_pago");
            String imp_base = (String) request.getParameter("imp_base").replace(",", ".");
            String imp_iva = (String) request.getParameter("imp_iva").replace(",", ".");
            String imp_total = (String) request.getParameter("imp_total").replace(",", ".");
            String personas = (String) request.getParameter("personas").replace(",", ".");
            String imp_persona_fact = (String) request.getParameter("imp_persona_fact").replace(",", ".");
            String imp_solic = (String) request.getParameter("imp_solic").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del L1EmpresaExterna a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaL1EmpresaExternaVO datModif = new FilaL1EmpresaExternaVO();

                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setCif(cif);
                datModif.setDenomEmpr(denom_empr);
                datModif.setnFactura(n_factura);
                if (fec_emis != null && !"".equals(fec_emis)) {
                    datModif.setFecEmis(new java.sql.Date(formatoFecha.parse(fec_emis).getTime()));
                }
                if (fec_pago != null && !"".equals(fec_pago)) {
                    datModif.setFecPago(new java.sql.Date(formatoFecha.parse(fec_pago).getTime()));
                }
                if (imp_base != null && !"".equals(imp_base)) {
                    datModif.setImpBase(Double.valueOf(imp_base));
                }
                if (imp_iva != null && !"".equals(imp_iva)) {
                    datModif.setImpIva(Double.valueOf(imp_iva));
                }
                if (imp_total != null && !"".equals(imp_total)) {
                    datModif.setImpTotal(Double.valueOf(imp_total));
                }
                if (personas != null && !"".equals(personas)) {
                    datModif.setPersonas(Double.valueOf(personas));
                }
                if (imp_persona_fact != null && !"".equals(imp_persona_fact)) {
                    datModif.setImpPersonaFact(Double.valueOf(imp_persona_fact));
                }
                if (imp_solic != null && !"".equals(imp_solic)) {
                    datModif.setImpSolic(Double.valueOf(imp_solic));
                }

                MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
                boolean modOK = melanbide12Manager.modificarL1EmpresaExterna(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = melanbide12Manager.getDatosL1EmpresaExterna(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de L1EmpresaExterna después de Modificar un L1EmpresaExterna : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de L1EmpresaExterna después de Modificar un L1EmpresaExterna : " + ex.getMessage());
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

    public void eliminarL1EmpresaExterna(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarL1EmpresaExterna - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL1EmpresaExternaVO> lista = new ArrayList<FilaL1EmpresaExternaVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del L1EmpresaExterna a Eliminar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide12Manager.getInstance().eliminarL1EmpresaExterna(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide12Manager.getInstance().getDatosL1EmpresaExterna(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de L1EmpresaExterna después de eliminar un L1EmpresaExterna");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un L1EmpresaExterna: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    //End L1EmpresaExterna_______________________________________________________________________________________________________________________________________________________________________

    //L1EmpresaPropia___________________________________________________________________________________________________________________________________________________________________________
    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return url jsp L1EmpresaPropia
     */
    public String cargarPantallaL1EmpresaPropia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaL1EmpresaPropia de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide12/l1EmpresaPropia.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaL1EmpresaPropiaVO> listaL1EmpresaPropia = MeLanbide12Manager.getInstance().getDatosL1EmpresaPropia(numExpediente, codOrganizacion, adapt);
                if (listaL1EmpresaPropia.size() > 0) {
                    request.setAttribute("listaL1EmpresaPropia", listaL1EmpresaPropia);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de melanbide12 - cargarPantallaL1EmpresaPropia", ex);
            }
        }

        return url;
    }

    public String cargarNuevoL1EmpresaPropia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoL1EmpresaPropia - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevoL1EmpresaPropia = "/jsp/extension/melanbide12/nuevoL1EmpresaPropia.jsp?codOrganizacion=" + codOrganizacion;
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
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva L1EmpresaPropia : " + ex.getMessage());
        }
        return urlnuevoL1EmpresaPropia;
    }

    public String cargarModificarL1EmpresaPropia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarL1EmpresaPropia - " + numExpediente);
        String nuevo = "0";
        String urlnuevoL1EmpresaPropia = "/jsp/extension/melanbide12/nuevoL1EmpresaPropia.jsp?codOrganizacion=" + codOrganizacion;
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
                FilaL1EmpresaPropiaVO datModif = MeLanbide12Manager.getInstance().getL1EmpresaPropiaPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlnuevoL1EmpresaPropia;

    }

    public void crearNuevoL1EmpresaPropia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoL1EmpresaPropia - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL1EmpresaPropiaVO> lista = new ArrayList<FilaL1EmpresaPropiaVO>();
        FilaL1EmpresaPropiaVO nuevoL1EmpresaPropia = new FilaL1EmpresaPropiaVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");

            String dni = (String) request.getParameter("dni");
            String nombre = (String) request.getParameter("nombre");
            String ape1 = (String) request.getParameter("ape1");
            String ape2 = (String) request.getParameter("ape2");
            String retr_anual_bruta = (String) request.getParameter("retr_anual_bruta").replace(",", ".");
            String cc_cot_ss = (String) request.getParameter("cc_cot_ss").replace(",", ".");
            String horas_lab_anual = (String) request.getParameter("horas_lab_anual").replace(",", ".");
            String horas_imput = (String) request.getParameter("horas_imput").replace(",", ".");
            String imp_gest = (String) request.getParameter("imp_gest").replace(",", ".");
            String person_pract = (String) request.getParameter("person_pract").replace(",", ".");
            String imp_solic = (String) request.getParameter("imp_solic").replace(",", ".");

            nuevoL1EmpresaPropia.setNumExp(numExp);
            nuevoL1EmpresaPropia.setDni(dni);
            nuevoL1EmpresaPropia.setNombre(nombre);
            nuevoL1EmpresaPropia.setApe1(ape1);
            nuevoL1EmpresaPropia.setApe2(ape2);
            if (retr_anual_bruta != null && !"".equals(retr_anual_bruta)) {
                nuevoL1EmpresaPropia.setRetrAnualBruta(Double.valueOf(retr_anual_bruta));
            }
            if (cc_cot_ss != null && !"".equals(cc_cot_ss)) {
                nuevoL1EmpresaPropia.setCcCotSs(Double.valueOf(cc_cot_ss));
            }
            if (horas_lab_anual != null && !"".equals(horas_lab_anual)) {
                nuevoL1EmpresaPropia.setHorasLabAnual(Double.valueOf(horas_lab_anual));
            }
            if (horas_imput != null && !"".equals(horas_imput)) {
                nuevoL1EmpresaPropia.setHorasImput(Double.valueOf(horas_imput));
            }
            if (imp_gest != null && !"".equals(imp_gest)) {
                nuevoL1EmpresaPropia.setImpGest(Double.valueOf(imp_gest));
            }
            if (person_pract != null && !"".equals(person_pract)) {
                nuevoL1EmpresaPropia.setPersonPract(Double.valueOf(person_pract));
            }
            if (imp_solic != null && !"".equals(imp_solic)) {
                nuevoL1EmpresaPropia.setImpSolic(Double.valueOf(imp_solic));
            }

            MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
            boolean insertOK = melanbide12Manager.crearNuevoL1EmpresaPropia(nuevoL1EmpresaPropia, adapt);
            if (insertOK) {
                log.debug("L1EmpresaPropia insertado correctamente");
                codigoOperacion = "0";
                lista = melanbide12Manager.getDatosL1EmpresaPropia(numExp, codOrganizacion, adapt);
            } else {
                log.debug("NO se ha insertado correctamente el nuevo L1EmpresaPropia");
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

    public void modificarL1EmpresaPropia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarL1EmpresaPropia - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL1EmpresaPropiaVO> lista = new ArrayList<FilaL1EmpresaPropiaVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("numExp");

            String dni = (String) request.getParameter("dni");
            String nombre = (String) request.getParameter("nombre");
            String ape1 = (String) request.getParameter("ape1");
            String ape2 = (String) request.getParameter("ape2");
            String retr_anual_bruta = (String) request.getParameter("retr_anual_bruta").replace(",", ".");
            String cc_cot_ss = (String) request.getParameter("cc_cot_ss").replace(",", ".");
            String horas_lab_anual = (String) request.getParameter("horas_lab_anual").replace(",", ".");
            String horas_imput = (String) request.getParameter("horas_imput").replace(",", ".");
            String imp_gest = (String) request.getParameter("imp_gest").replace(",", ".");
            String person_pract = (String) request.getParameter("person_pract").replace(",", ".");
            String imp_solic = (String) request.getParameter("imp_solic").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del L1EmpresaPropia a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaL1EmpresaPropiaVO datModif = new FilaL1EmpresaPropiaVO();

                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setDni(dni);
                datModif.setNombre(nombre);
                datModif.setApe1(ape1);
                datModif.setApe2(ape2);
                if (retr_anual_bruta != null && !"".equals(retr_anual_bruta)) {
                    datModif.setRetrAnualBruta(Double.valueOf(retr_anual_bruta));
                }
                if (cc_cot_ss != null && !"".equals(cc_cot_ss)) {
                    datModif.setCcCotSs(Double.valueOf(cc_cot_ss));
                }
                if (horas_lab_anual != null && !"".equals(horas_lab_anual)) {
                    datModif.setHorasLabAnual(Double.valueOf(horas_lab_anual));
                }
                if (horas_imput != null && !"".equals(horas_imput)) {
                    datModif.setHorasImput(Double.valueOf(horas_imput));
                }
                if (imp_gest != null && !"".equals(imp_gest)) {
                    datModif.setImpGest(Double.valueOf(imp_gest));
                }
                if (person_pract != null && !"".equals(person_pract)) {
                    datModif.setPersonPract(Double.valueOf(person_pract));
                }
                if (imp_solic != null && !"".equals(imp_solic)) {
                    datModif.setImpSolic(Double.valueOf(imp_solic));
                }

                MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
                boolean modOK = melanbide12Manager.modificarL1EmpresaPropia(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = melanbide12Manager.getDatosL1EmpresaPropia(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de L1EmpresaPropia después de Modificar un L1EmpresaPropia : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de L1EmpresaPropia después de Modificar un L1EmpresaPropia : " + ex.getMessage());
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

    public void eliminarL1EmpresaPropia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarL1EmpresaPropia - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL1EmpresaPropiaVO> lista = new ArrayList<FilaL1EmpresaPropiaVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del L1EmpresaPropia a Eliminar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide12Manager.getInstance().eliminarL1EmpresaPropia(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide12Manager.getInstance().getDatosL1EmpresaPropia(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de L1EmpresaPropia después de eliminar un L1EmpresaPropia");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un L1EmpresaPropia: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    //End L1EmpresaPropia_______________________________________________________________________________________________________________________________________________________________________

    //L2Participante___________________________________________________________________________________________________________________________________________________________________________
    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return url jsp L2Participante
     */
    public String cargarPantallaL2Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaL2Participante de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide12/l2Participante.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaL2ParticipanteVO> listaL2Participante = MeLanbide12Manager.getInstance().getDatosL2Participante(numExpediente, codOrganizacion, adapt);
                if (listaL2Participante.size() > 0) {
                    for (FilaL2ParticipanteVO lm : listaL2Participante) {
                        log.debug("TipoDoc antes: "+ lm.getDesTipoDoc());

                        lm.setDesTipoDoc(getDescripcionDesplegable(request, lm.getDesTipoDoc()));
                        log.debug("TipoDoc despues: "+ lm.getDesTipoDoc());
                    }
                    request.setAttribute("listaL2Participante", listaL2Participante);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de melanbide12 - cargarPantallaL2Participante", ex);
            }
        }

        return url;
    }

    public String cargarNuevoL2Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoL2Participante - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevoL2Participante = "/jsp/extension/melanbide12/nuevoL2Participante.jsp?codOrganizacion=" + codOrganizacion;
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

            cargarDesplegableL2(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva L2Participante : " + ex.getMessage());
        }
        return urlnuevoL2Participante;
    }

    public String cargarModificarL2Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarL2Participante - " + numExpediente);
        String nuevo = "0";
        String urlnuevoL2Participante = "/jsp/extension/melanbide12/nuevoL2Participante.jsp?codOrganizacion=" + codOrganizacion;
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
                FilaL2ParticipanteVO datModif = MeLanbide12Manager.getInstance().getL2ParticipantePorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }

            cargarDesplegableL2(codOrganizacion, request);

        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlnuevoL2Participante;

    }

    public void crearNuevoL2Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoL2Participante - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL2ParticipanteVO> lista = new ArrayList<FilaL2ParticipanteVO>();
        FilaL2ParticipanteVO nuevoL2Participante = new FilaL2ParticipanteVO();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");

            String tipoDoc = (String) request.getParameter("tipoDoc");
            String doc = (String) request.getParameter("doc");
            String nombre = (String) request.getParameter("nombre");
            String ape1 = (String) request.getParameter("ape1");
            String ape2 = (String) request.getParameter("ape2");
            String cod_act_form = (String) request.getParameter("cod_act_form");
            String horas_pract = (String) request.getParameter("horas_pract").replace(",", ".");
            String imp_solic = (String) request.getParameter("imp_solic").replace(",", ".");

            nuevoL2Participante.setNumExp(numExp);
            nuevoL2Participante.setTipoDoc(tipoDoc);
            nuevoL2Participante.setDoc(doc);
            nuevoL2Participante.setNombre(nombre);
            nuevoL2Participante.setApe1(ape1);
            nuevoL2Participante.setApe2(ape2);
            nuevoL2Participante.setCodActForm(cod_act_form);
            if (horas_pract != null && !"".equals(horas_pract)) {
                nuevoL2Participante.setHorasPract(Double.valueOf(horas_pract));
            }
            if (imp_solic != null && !"".equals(imp_solic)) {
                nuevoL2Participante.setImpSolic(Double.valueOf(imp_solic));
            }

            MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
            boolean insertOK = melanbide12Manager.crearNuevoL2Participante(nuevoL2Participante, adapt);
            if (insertOK) {
                log.debug("L2Participante insertado correctamente");
                codigoOperacion = "0";
                lista = melanbide12Manager.getDatosL2Participante(numExp, codOrganizacion, adapt);
                if (lista.size() > 0) {
                    for (FilaL2ParticipanteVO lm : lista) {
                        lm.setDesTipoDoc(getDescripcionDesplegable(request, lm.getDesTipoDoc()));
                    }
                }
            } else {
                log.debug("NO se ha insertado correctamente el nuevo L2Participante");
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

    public void modificarL2Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarL2Participante - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL2ParticipanteVO> lista = new ArrayList<FilaL2ParticipanteVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");

            String numExp = (String) request.getParameter("numExp");

            String tipoDoc = (String) request.getParameter("tipoDoc");
            String doc = (String) request.getParameter("doc");
            String nombre = (String) request.getParameter("nombre");
            String ape1 = (String) request.getParameter("ape1");
            String ape2 = (String) request.getParameter("ape2");
            String cod_act_form = (String) request.getParameter("cod_act_form");
            String horas_pract = (String) request.getParameter("horas_pract").replace(",", ".");
            String imp_solic = (String) request.getParameter("imp_solic").replace(",", ".");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del L2Participante a Modificar ");
                codigoOperacion = "3";
            } else {
                FilaL2ParticipanteVO datModif = new FilaL2ParticipanteVO();

                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setTipoDoc(tipoDoc);
                datModif.setDoc(doc);
                datModif.setNombre(nombre);
                datModif.setApe1(ape1);
                datModif.setApe2(ape2);
                datModif.setCodActForm(cod_act_form);
                if (horas_pract != null && !"".equals(horas_pract)) {
                    datModif.setHorasPract(Double.valueOf(horas_pract));
                }
                if (imp_solic != null && !"".equals(imp_solic)) {
                    datModif.setImpSolic(Double.valueOf(imp_solic));
                }

                MeLanbide12Manager melanbide12Manager = MeLanbide12Manager.getInstance();
                boolean modOK = melanbide12Manager.modificarL2Participante(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = melanbide12Manager.getDatosL2Participante(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaL2ParticipanteVO lm : lista) {
                                lm.setDesTipoDoc(getDescripcionDesplegable(request, lm.getDesTipoDoc()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de L2Participante después de Modificar un L2Participante : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de L2Participante después de Modificar un L2Participante : " + ex.getMessage());
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

    public void eliminarL2Participante(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarL2Participante - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaL2ParticipanteVO> lista = new ArrayList<FilaL2ParticipanteVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del L2Participante a Eliminar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide12Manager.getInstance().eliminarL2Participante(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide12Manager.getInstance().getDatosL2Participante(numExp, codOrganizacion, adapt);
                        if (lista.size() > 0) {
                            for (FilaL2ParticipanteVO lm : lista) {
                                lm.setDesTipoDoc(getDescripcionDesplegable(request, lm.getDesTipoDoc()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de L2Participante después de eliminar un L2Participante");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando un L2Participante: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    //End L2Participante_______________________________________________________________________________________________________________________________________________________________________


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
        }// synchronized
        return adapt;
    }//getConnection

    /**
     * Métodos que recuperan los valores de los desplegables del módulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegablesSubvencion(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaEstado = MeLanbide12Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide12.COD_DES_ESTADO_AYUDA, ConstantesMeLanbide12.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaEstado.isEmpty()) {
            listaEstado = traducirDesplegable(request, listaEstado);
            request.setAttribute("listaEstado", listaEstado);
        }
    }

    private void cargarDesplegableL1(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaTipoDoc = MeLanbide12Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide12.COD_DES_TIP_DOC, ConstantesMeLanbide12.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaTipoDoc.isEmpty()) {
            listaTipoDoc = traducirDesplegable(request, listaTipoDoc);
            request.setAttribute("listaTipoDoc", listaTipoDoc);
        }
    }

    private void cargarDesplegableL2(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaTipoDoc = MeLanbide12Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide12.COD_DES_TIP_DOC_L2, ConstantesMeLanbide12.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaTipoDoc.isEmpty()) {
            listaTipoDoc = traducirDesplegable(request, listaTipoDoc);
            request.setAttribute("listaTipoDoc", listaTipoDoc);
        }
    }

    /**
     * Método que extrae la descripción de los desplegables en el idioma del
     * usuario, en BBDD están en un campo separadas por Pipeline |
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
     * Método que retorna el valor de un desplegable en el idioma del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraSeparadoraIdioma = ConfigurationParameter.getParameter(ConstantesMeLanbide12.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide12.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraIdioma) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide12.CODIGO_IDIOMA_EUSKERA) {
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
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = ConstantesMeLanbide12.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide12.CODIGO_IDIOMA_CASTELLANO;
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
