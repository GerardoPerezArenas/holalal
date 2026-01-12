package es.altia.flexia.integracion.moduloexterno.melanbide14;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide14.manager.MeLanbide14Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConstantesMeLanbide14;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionSolicitadaVO;
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

public class MELANBIDE14 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE14.class);
    private static DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final MeLanbide14Manager m14Manager = new MeLanbide14Manager();
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
        String url = "/jsp/extension/melanbide14/melanbide14.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<OperacionVO> listaOperaciones = MeLanbide14Manager.getInstance().getListaOperaciones(numExpediente, codOrganizacion, adapt);
                if (listaOperaciones != null) {
                    for (final OperacionVO op : listaOperaciones) {
                        op.setDesPrio(getDescripcionDesplegable(request, op.getDesPrio()));
                        op.setDesLin1(getDescripcionDesplegable(request, op.getDesLin1()));
                        op.setDesLin2(getDescripcionDesplegable(request, op.getDesLin2()));
                        op.setDesLin3(getDescripcionDesplegable(request, op.getDesLin3()));
                    }
                    request.setAttribute("listaOperaciones", listaOperaciones);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de accesos - MELANBIDE14 - cargarPantallaPrincipal", ex);
            }
        }

        return url;
    }

    public String cargarNuevaOperacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaOperacion - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String url = "/jsp/extension/melanbide14/nuevaOperacion.jsp?codOrganizacion=" + codOrganizacion;

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
            cargarDesplegablesOperaciones(codOrganizacion, request);
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nueva operacion : " + ex.getMessage());
        }
        return url;
    }

    public String cargarModificarOperacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarOperacion - " + numExpediente);
        String nuevo = "0";
        String url = "/jsp/extension/melanbide14/nuevaOperacion.jsp?codOrganizacion=" + codOrganizacion;

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
                OperacionVO datModif = MeLanbide14Manager.getInstance().getOperacionPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            cargarDesplegablesOperaciones(codOrganizacion, request);
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificaci?n : " + ex.getMessage());
        }
        return url;

    }

    public void crearNuevaOperacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevaOperacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<OperacionVO> lista = new ArrayList<OperacionVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");
            String numOper = request.getParameter("numOper");
            String nombreOper = request.getParameter("nombreOper");
            String prio = request.getParameter("prio");
            String lin1 = request.getParameter("lin1");
            String lin2 = request.getParameter("lin2");
            String lin3 = request.getParameter("lin3");
            String impOper = request.getParameter("impOper");

            final OperacionVO nuevaOperacion = new OperacionVO(null, numExp, Integer.valueOf(numOper), nombreOper, prio,
                    lin1, lin2, lin3, Double.valueOf(impOper));
            MeLanbide14Manager meLanbide14Manager = MeLanbide14Manager.getInstance();
            boolean insertOK = meLanbide14Manager.crearOperacion(nuevaOperacion, adapt);
            if (insertOK) {
                log.debug("Operacion Insertada Correctamente");
                codigoOperacion = "0";
                lista = meLanbide14Manager.getListaOperaciones(numExp, codOrganizacion, adapt);
                if (lista != null) {
                    for (final OperacionVO op : lista) {
                        op.setDesPrio(getDescripcionDesplegable(request, op.getDesPrio()));
                        op.setDesLin1(getDescripcionDesplegable(request, op.getDesLin1()));
                        op.setDesLin2(getDescripcionDesplegable(request, op.getDesLin2()));
                        op.setDesLin3(getDescripcionDesplegable(request, op.getDesLin3()));
                    }
                }
            } else {
                log.debug("NO se ha insertado correctamente la nueva operacion");
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.debug("Error al parsear los par?metros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarOperacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarOperacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<OperacionVO> listaOperaciones = new ArrayList<OperacionVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String numOper = request.getParameter("numOper");
            String nombreOper = request.getParameter("nombreOper");
            String prio = request.getParameter("prio");
            String lin1 = request.getParameter("lin1");
            String lin2 = request.getParameter("lin2");
            String lin3 = request.getParameter("lin3");
            String impOper = request.getParameter("impOper");

            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la operacion a Modificar ");
                codigoOperacion = "3";
            } else {
                OperacionVO operacionModif = new OperacionVO();

                operacionModif.setId(Integer.parseInt(id));
                operacionModif.setNumExp(numExp);
                operacionModif.setNumOper(Integer.parseInt(numOper));
                operacionModif.setNombreOper(nombreOper);
                operacionModif.setPrio(prio);
                operacionModif.setLin1(lin1);
                operacionModif.setLin2(lin2);
                operacionModif.setLin3(lin3);
                operacionModif.setImpOper(Double.valueOf(impOper));

                MeLanbide14Manager meLanbide14Manager = MeLanbide14Manager.getInstance();
                boolean modOK = meLanbide14Manager.modificarOperacion(operacionModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        listaOperaciones = meLanbide14Manager.getListaOperaciones(numExp, codOrganizacion, adapt);
                        if (listaOperaciones != null) {
                            for (final OperacionVO op : listaOperaciones) {
                                op.setDesPrio(getDescripcionDesplegable(request, op.getDesPrio()));
                                op.setDesLin1(getDescripcionDesplegable(request, op.getDesLin1()));
                                op.setDesLin2(getDescripcionDesplegable(request, op.getDesLin2()));
                                op.setDesLin3(getDescripcionDesplegable(request, op.getDesLin3()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de accesos despu?s de Modificar una operacion : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.debug("Error al recuperar la lista de accesos despu?s de Modificar una operacion : " + ex.getMessage());
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
            resultado.setAtributo("lista", listaOperaciones);
        }
        retornarJSON(new Gson().toJson(resultado), response);

    }

    public void eliminarOperacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarOperacion - " + numExpediente);
        String codigoOperacion = "-1";
        List<OperacionVO> listaOperaciones = new ArrayList<OperacionVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del acceso a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide14Manager.getInstance().eliminarOperacion(id, numExp, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        listaOperaciones = MeLanbide14Manager.getInstance().getListaOperaciones(numExp, codOrganizacion, adapt);
                        if (listaOperaciones != null) {
                            for (final OperacionVO op : listaOperaciones) {
                                op.setDesPrio(getDescripcionDesplegable(request, op.getDesPrio()));
                                op.setDesLin1(getDescripcionDesplegable(request, op.getDesLin1()));
                                op.setDesLin2(getDescripcionDesplegable(request, op.getDesLin2()));
                                op.setDesLin3(getDescripcionDesplegable(request, op.getDesLin3()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de accesos despu?s de eliminar una operacion");
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("Error eliminando una operacion: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaOperaciones);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String cargarPantallaFinanciacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaFinanciacion de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide14/financiacion/financiacion.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<String> listaEjercicios = m14Manager.getEjerciciosSolicitados(numExpediente, adapt);
                if (listaEjercicios != null && !listaEjercicios.isEmpty()) {
                    request.setAttribute("listaEjercicios", listaEjercicios);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de ejercicios solicitados ", ex);
            }
        }
        return url;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void cargarOperacionesSolicitadas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("==> Entramos en cargarOperacionesSolicitadas");
        String codigoOperacion = "-1";
        List<OperacionSolicitadaVO> listaOperaciones = new ArrayList<OperacionSolicitadaVO>();
        String numExp = "";
        String ejercicioSol = "";
        try {
            numExp = request.getParameter("numExp");
            ejercicioSol = request.getParameter("ejercicio");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            listaOperaciones = m14Manager.getOperacionesSolicitadasEjercicio(numExp, ejercicioSol, adapt);
            if (listaOperaciones.isEmpty()) {
                log.error("No se han recuperado operaciones para el ejercicio " + ejercicioSol);
                codigoOperacion = "4";
            } else {
                for (OperacionSolicitadaVO operacion : listaOperaciones) {
                    operacion.setDescPrioridad(getDescripcionDesplegable(request, operacion.getDescPrioridad()));
                    operacion.setDescObjetivo(getDescripcionDesplegable(request, operacion.getDescObjetivo()));
                    operacion.setDescTipologia(getDescripcionDesplegable(request, operacion.getDescTipologia()));
                    operacion.setDescEntidad(getDescripcionDesplegable(request, operacion.getDescEntidad()));
                }
                codigoOperacion = "0";
            }
        } catch (Exception ex) {
            codigoOperacion = "5";
            log.error("Error al recuperar la lista de Operaciones Solicitadas - " + ex.getMessage());
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaOperaciones);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String cargarMantenimientoOperacionSolicitada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarMantenimientoOperacionSolicitada");
        String url = "/jsp/extension/melanbide14/financiacion/mantenimientoSolicitadas.jsp";
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) { // edicion
                OperacionSolicitadaVO datModif = m14Manager.getOperacionSolicitadaPorID(id, adapt);
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
                request.setAttribute("nuevo", "0");
            } else {// alta
                int ultimaOperacion = Integer.parseInt(request.getParameter("ultimaOperacion"));
                ultimaOperacion++;
                request.setAttribute("numOrden", String.valueOf(ultimaOperacion));
                request.setAttribute("ejerSolicitado", request.getParameter("ejerSolicitado"));
                request.setAttribute("nuevo", "1");
            }
            request.setAttribute("numExp", request.getParameter("numExp"));
            request.setAttribute("ejerSolicitado", request.getParameter("ejerSolicitado"));
            cargarDesplegablesFinanciacion(request, adapt);
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de mantenimiento de OPERACIONES SOLICITADAS : " + ex.getMessage());
        }
        return url;
    }

    public void altaOpeSolicitada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en altaOpeSolicitada - ");
        String codigoOperacion = "-1";
        List<OperacionSolicitadaVO> listaOperaciones = new ArrayList<OperacionSolicitadaVO>();
        OperacionSolicitadaVO operacion = new OperacionSolicitadaVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String numExp = request.getParameter("numExp");
            String ejeOperacion = request.getParameter("ejeOperacion");
            String numOpePre = request.getParameter("numOpePre");
            String prioridad = request.getParameter("prioridad");
            String objetivo = request.getParameter("objetivo");
            String tipologia = request.getParameter("tipologia");
            String fecInicio = request.getParameter("fecInicio");
            String fecFin = request.getParameter("fecFin");
            String entidad = request.getParameter("entidad");
            String organismo = request.getParameter("organismo");

            operacion.setNumExp(numExp);
            operacion.setEjeOperacion(Integer.valueOf(ejeOperacion));
            operacion.setNumOpePre(Integer.valueOf(numOpePre));
            operacion.setPrioridad(prioridad);
            operacion.setObjetivo(objetivo);
            operacion.setTipologia(tipologia);
            operacion.setFecInicio(new java.sql.Date(formatoFecha.parse(fecInicio).getTime()));
            operacion.setFecFin(new java.sql.Date(formatoFecha.parse(fecFin).getTime()));
            operacion.setEntidad(entidad);
            operacion.setOrganismo(organismo);

            if (m14Manager.nuevaOpeSolicitada(operacion, adapt)) {
                log.debug("Operación insertada correctamente");
                codigoOperacion = "0";
                try {
                    listaOperaciones = m14Manager.getOperacionesSolicitadasEjercicio(numExp, ejeOperacion, adapt);
                    if (!listaOperaciones.isEmpty()) {
                        for (OperacionSolicitadaVO ope : listaOperaciones) {
                            ope.setDescPrioridad(getDescripcionDesplegable(request, ope.getDescPrioridad()));
                            ope.setDescObjetivo(getDescripcionDesplegable(request, ope.getDescObjetivo()));
                            ope.setDescTipologia(getDescripcionDesplegable(request, ope.getDescTipologia()));
                            ope.setDescEntidad(getDescripcionDesplegable(request, ope.getDescEntidad()));
                        }
                    } else {
                        codigoOperacion = "4";
                        log.error("No se ha recuperado las Operaciónes después de dar una de alta");
                    }
                } catch (Exception ex) {
                    codigoOperacion = "5";
                    log.error("Error al recuperar la lista de Operaciónes después de dar una de alta" + ex);
                }
            } else {
                log.error("NO se ha insertado correctamente la nueva Operación");
                codigoOperacion = "7";
            }
        } catch (Exception ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaOperaciones);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarOpeSolicitada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarOpeSolicitada - ");
        String codigoOperacion = "-1";
        List<OperacionSolicitadaVO> listaOperaciones = new ArrayList<OperacionSolicitadaVO>();
        OperacionSolicitadaVO operacion = new OperacionSolicitadaVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String ejeOperacion = request.getParameter("ejeOperacion");
            String numOpePre = request.getParameter("numOpePre");
            String prioridad = request.getParameter("prioridad");
            String objetivo = request.getParameter("objetivo");
            String tipologia = request.getParameter("tipologia");
            String fecInicio = request.getParameter("fecInicio");
            String fecFin = request.getParameter("fecFin");
            String entidad = request.getParameter("entidad");
            String organismo = request.getParameter("organismo");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la Factura a Modificar ");
                codigoOperacion = "3";
            } else {
                operacion.setId(Integer.valueOf(id));
                operacion.setNumExp(numExp);
                operacion.setEjeOperacion(Integer.valueOf(ejeOperacion));
                operacion.setNumOpePre(Integer.valueOf(numOpePre));
                operacion.setPrioridad(prioridad);
                operacion.setObjetivo(objetivo);
                operacion.setTipologia(tipologia);
                operacion.setFecInicio(new java.sql.Date(formatoFecha.parse(fecInicio).getTime()));
                operacion.setFecFin(new java.sql.Date(formatoFecha.parse(fecFin).getTime()));
                operacion.setEntidad(entidad);
                operacion.setOrganismo(organismo);

                if (m14Manager.modificarOpeSolicitada(operacion, adapt)) {
                    log.debug("Operación modificada correctamente");
                    codigoOperacion = "0";
                    try {
                        listaOperaciones = m14Manager.getOperacionesSolicitadasEjercicio(numExp, ejeOperacion, adapt);
                        if (!listaOperaciones.isEmpty()) {
                            for (OperacionSolicitadaVO ope : listaOperaciones) {
                                ope.setDescPrioridad(getDescripcionDesplegable(request, ope.getDescPrioridad()));
                                ope.setDescObjetivo(getDescripcionDesplegable(request, ope.getDescObjetivo()));
                                ope.setDescTipologia(getDescripcionDesplegable(request, ope.getDescTipologia()));
                                ope.setDescEntidad(getDescripcionDesplegable(request, ope.getDescEntidad()));
                            }
                        } else {
                            codigoOperacion = "4";
                            log.error("No se ha recuperado las Operaciónes después de modificar");
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de Operaciónes después de modificar" + ex);
                    }
                } else {
                    log.error("NO se ha modificado la Operación");
                    codigoOperacion = "8";
                }
            }
        } catch (Exception ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaOperaciones);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void eliminarOpeSolicitada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarOpeSolicitada - ");
        String codigoOperacion = "-1";
        List<OperacionSolicitadaVO> listaOperaciones = new ArrayList<OperacionSolicitadaVO>();
        String numExp = "";
        String ejerSolicitado = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la SubSolic a Elimnar ");
                codigoOperacion = "3";
            } else {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                numExp = request.getParameter("numExp");
                ejerSolicitado = request.getParameter("ejerSolicitado");
                if (m14Manager.eliminarOpeSolicitada(id, adapt)) {
                    codigoOperacion = "0";
                    try {
                        listaOperaciones = m14Manager.getOperacionesSolicitadasEjercicio(numExp, ejerSolicitado, adapt);
                        if (!listaOperaciones.isEmpty()) {
                            for (OperacionSolicitadaVO ope : listaOperaciones) {
                                ope.setDescPrioridad(getDescripcionDesplegable(request, ope.getDescPrioridad()));
                                ope.setDescObjetivo(getDescripcionDesplegable(request, ope.getDescObjetivo()));
                                ope.setDescTipologia(getDescripcionDesplegable(request, ope.getDescTipologia()));
                                ope.setDescEntidad(getDescripcionDesplegable(request, ope.getDescEntidad()));
                            }
                        } else {
                            codigoOperacion = "4";
                            log.error("No se ha recuperado las Operaciónes después de eliminar");
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de Operaciónes después de eliminar" + ex);
                    }
                } else {
                    log.error("NO se ha eliminado la Operación");
                    codigoOperacion = "6";
                }
            }
        } catch (Exception ex) {
            log.error("Error eliminando una Operación: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaOperaciones);
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
                // Conexi?n al esquema gen?rico
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
     * Método que recupera los valores de los desplegables del módulo
     *
     * @param codOrganizacion
     * @param request
     * @throws SQLException
     * @throws Exception
     */
    private void cargarDesplegablesOperaciones(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaPrio = MeLanbide14Manager.getInstance().getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_PRIO, ConstantesMeLanbide14.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaLin1 = MeLanbide14Manager.getInstance().getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_LINACT1, ConstantesMeLanbide14.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaLin2 = MeLanbide14Manager.getInstance().getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_LINACT2, ConstantesMeLanbide14.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        List<DesplegableAdmonLocalVO> listaLin3 = MeLanbide14Manager.getInstance().getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_LINACT3, ConstantesMeLanbide14.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

        if (!listaPrio.isEmpty()) {
            listaPrio = traducirDesplegable(request, listaPrio);
            request.setAttribute("listaPrio", listaPrio);
        }
        if (!listaLin1.isEmpty()) {
            listaLin1 = traducirDesplegable(request, listaLin1);
            request.setAttribute("listaLin1", listaLin1);
        }
        if (!listaLin2.isEmpty()) {
            listaLin2 = traducirDesplegable(request, listaLin2);
            request.setAttribute("listaLin2", listaLin2);
        }
        if (!listaLin3.isEmpty()) {
            listaLin3 = traducirDesplegable(request, listaLin3);
            request.setAttribute("listaLin3", listaLin3);
        }
    }

    private void cargarDesplegablesFinanciacion(HttpServletRequest request, AdaptadorSQLBD adapt) throws Exception {
        List<DesplegableAdmonLocalVO> listaPrioridad = m14Manager.getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_PRIORIDAD, ConstantesMeLanbide14.FICHERO_PROPIEDADES), adapt);
        List<DesplegableAdmonLocalVO> listaObjetivo = m14Manager.getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_OBJETIVO, ConstantesMeLanbide14.FICHERO_PROPIEDADES), adapt);
        List<DesplegableAdmonLocalVO> listaTipologia = m14Manager.getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_TIPOLOGIA, ConstantesMeLanbide14.FICHERO_PROPIEDADES), adapt);
        List<DesplegableAdmonLocalVO> listaEntidad = m14Manager.getValoresE_DES_VAL(ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_ENTIDAD, ConstantesMeLanbide14.FICHERO_PROPIEDADES), adapt);

        if (!listaPrioridad.isEmpty()) {
            listaPrioridad = traducirDesplegable(request, listaPrioridad);
            request.setAttribute("listaPrioridad", listaPrioridad);
        }
        if (!listaObjetivo.isEmpty()) {
            listaObjetivo = traducirDesplegable(request, listaObjetivo);
            request.setAttribute("listaObjetivo", listaObjetivo);
        }
        if (!listaTipologia.isEmpty()) {
            listaTipologia = traducirDesplegable(request, listaTipologia);
            request.setAttribute("listaTipologia", listaTipologia);
        }
        if (!listaEntidad.isEmpty()) {
            listaEntidad = traducirDesplegable(request, listaEntidad);
            request.setAttribute("listaEntidad", listaEntidad);
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
     * Método que retorna el valor de un desplegable en el idiona del usuario
     *
     * @param request
     * @param descripcionCompleta
     * @return String con el valor
     */
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide14.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide14.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide14.CODIGO_IDIOMA_EUSKERA) {
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
        int idioma = ConstantesMeLanbide14.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide14.CODIGO_IDIOMA_CASTELLANO;
        }
        return idioma;
    }

    /**
     * Método llamado para devolver un String en formato JSON al cliente que ha
     * realiza la petici?n a alguna de las operaciones de este action realiza la
     * petición a alguna de las operaciones de este action
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
