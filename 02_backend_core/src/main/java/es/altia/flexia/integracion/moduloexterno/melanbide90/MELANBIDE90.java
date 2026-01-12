package es.altia.flexia.integracion.moduloexterno.melanbide90;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide90.i18n.MeLanbide90I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide90.manager.MeLanbide90Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide90.manager.MeLanbide90MinimisManager;
import es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide90.util.ConstantesMeLanbide90;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FamiliaSolicitadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide90.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

public class MELANBIDE90 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE90.class);
    private static DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    private final MeLanbide90Manager m90Manager = new MeLanbide90Manager();
    private final IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
    private final MeLanbide90MinimisManager meLanbide90MinimisManager = new MeLanbide90MinimisManager();
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
        String url = "/jsp/extension/melanbide90/melanbide90.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<FilaMinimisVO> listaMinimis = MeLanbide90Manager.getInstance().getDatosMinimis(numExpediente, codOrganizacion, adapt);
                double valorMinimis = 0.0;
                if (listaMinimis.size() > 0) {
                    for (FilaMinimisVO lm : listaMinimis) {
//                        log.debug("Estado antes: " + lm.getDesEstado());
                        if (meLanbide90MinimisManager.isSubvencionAplicarMinimisExpediente(lm)){
                            valorMinimis += lm.getImporte() != null ? lm.getImporte() : 0.0;
                        }
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
//                        log.debug("Estado despues: " + lm.getDesEstado());
                    }
                    request.setAttribute("listaMinimis", listaMinimis);
                }
                //Validar que el dato existe y esta actualizado
                log.info("Validar minimis Minimis aplicar expediente: "
                        + meLanbide90MinimisManager.gestionValidarValorMinimisAplicarExpediente(codOrganizacion,numExpediente,valorMinimis).toString()
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recuperar los datos de minimis - MELANBIDE90 - cargarPantallaPrincipal: " + ex.getMessage(), ex);
            }
        }

        return url;
    }

    public String cargarNuevoMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoMinimis - " + numExpediente);
        String nuevo = "1";
        String numExp = "";
        String urlnuevoMinimis = "/jsp/extension/melanbide90/nuevoMinimis.jsp?codOrganizacion=" + codOrganizacion;
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
        String urlnuevoMinimis = "/jsp/extension/melanbide90/nuevoMinimis.jsp?codOrganizacion=" + codOrganizacion;
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
                FilaMinimisVO datModif = MeLanbide90Manager.getInstance().getMinimisPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
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
        String numExp = request.getParameter("numExp");

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
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

            MeLanbide90Manager meLanbide90Manager = MeLanbide90Manager.getInstance();
            boolean insertOK = meLanbide90Manager.crearNuevoMinimis(nuevaMinimis, adapt);
            if (insertOK) {
                log.debug("minimis insertada correctamente");
                codigoOperacion = "0";
                lista = meLanbide90Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                double valorMinimis = 0.0;
                if (lista.size() > 0) {
                    for (FilaMinimisVO lm : lista) {
                        if (meLanbide90MinimisManager.isSubvencionAplicarMinimisExpediente(lm)){
                            valorMinimis += lm.getImporte() != null ? lm.getImporte() : 0.0;
                        }
                        lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                    }
                }
                //Validar que el dato existe y esta actualizado
                log.info("Validar minimis Minimis aplicar expediente: "
                        + meLanbide90MinimisManager.gestionValidarValorMinimisAplicarExpediente(codOrganizacion,numExp,valorMinimis).toString()
                );
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
        Double valorMinimisAplicarActualizado = meLanbide90MinimisManager.getValorCSEMinimisAplicarExpediente(codOrganizacion,numExp);
        resultado.setAtributo("valorMinimisAplicar",(valorMinimisAplicarActualizado != null ? String.valueOf(valorMinimisAplicarActualizado) : ""));
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void modificarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        String numExp = (String) request.getParameter("numExp");
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = (String) request.getParameter("id");
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

                MeLanbide90Manager meLanbide90Manager = MeLanbide90Manager.getInstance();
                boolean modOK = meLanbide90Manager.modificarMinimis(datModif, adapt);
                if (modOK) {
                    codigoOperacion = "0";
                    try {
                        lista = meLanbide90Manager.getDatosMinimis(numExp, codOrganizacion, adapt);
                        double valorMinimis = 0.0;
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                if (meLanbide90MinimisManager.isSubvencionAplicarMinimisExpediente(lm)){
                                    valorMinimis += lm.getImporte() != null ? lm.getImporte() : 0.0;
                                }
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                        //Validar que el dato existe y esta actualizado
                        log.info("Validar minimis Minimis aplicar expediente: "
                                + meLanbide90MinimisManager.gestionValidarValorMinimisAplicarExpediente(codOrganizacion,numExp,valorMinimis).toString()
                        );
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
        Double valorMinimisAplicarActualizado = meLanbide90MinimisManager.getValorCSEMinimisAplicarExpediente(codOrganizacion,numExp);
        resultado.setAtributo("valorMinimisAplicar",(valorMinimisAplicarActualizado != null ? String.valueOf(valorMinimisAplicarActualizado) : ""));
        retornarJSON(new Gson().toJson(resultado), response);

    }

    public void eliminarMinimis(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarMinimis - " + numExpediente);
        String codigoOperacion = "-1";
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        String numExp = request.getParameter("numExp");
        try {
            String id = request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id de la minimis a Elimnar ");
                codigoOperacion = "3";
            } else {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide90Manager.getInstance().eliminarMinimis(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide90Manager.getInstance().getDatosMinimis(numExp, codOrganizacion, adapt);
                        double valorMinimis = 0.0;
                        if (lista.size() > 0) {
                            for (FilaMinimisVO lm : lista) {
                                if (meLanbide90MinimisManager.isSubvencionAplicarMinimisExpediente(lm)){
                                    valorMinimis += lm.getImporte() != null ? lm.getImporte() : 0.0;
                                }
                                lm.setDesEstado(getDescripcionDesplegable(request, lm.getDesEstado()));
                            }
                        }
                        //Validar que el dato existe y esta actualizado
                        log.info("Validar minimis Minimis aplicar expediente: "
                                + meLanbide90MinimisManager.gestionValidarValorMinimisAplicarExpediente(codOrganizacion,numExp,valorMinimis).toString()
                        );
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
        Double valorMinimisAplicarActualizado = meLanbide90MinimisManager.getValorCSEMinimisAplicarExpediente(codOrganizacion,numExp);
        resultado.setAtributo("valorMinimisAplicar",(valorMinimisAplicarActualizado != null ? String.valueOf(valorMinimisAplicarActualizado) : ""));
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
     * @return URL de la pestana de Facturas
     */
    public String cargarPantallaFacturas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaFacturas de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        String url = null;
        String codModalidad = null;
        String descModalidad = "";
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        try {
            url = "/jsp/extension/melanbide90/detalleFacturas.jsp";
            codModalidad = m90Manager.getValorDesplegableExpediente(codOrganizacion, numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_CAMPO_MODALIDAD, ConstantesMeLanbide90.FICHERO_PROPIEDADES), adapt);
            List<DesplegableAdmonLocalVO> modalidades = m90Manager.getValoresDesplegablesXdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_MODALIDAD, ConstantesMeLanbide90.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (!modalidades.isEmpty()) {
                for (DesplegableAdmonLocalVO valordesp : modalidades) {
                    if (valordesp.getDes_val_cod().equalsIgnoreCase(codModalidad)) {
                        descModalidad = getDescripcionDesplegable(request, valordesp.getDes_nom());
                    }
                }
            }
            List<FamiliaSolicitadaVO> listaFamilias = m90Manager.getFamiliasSolicitadas(numExpediente, adapt);
            List<FacturaVO> listaFacturas0l = null;
            List<FacturaVO> listaFacturas02 = null;
            List<FacturaVO> listaFacturas03 = null;
            log.debug("Hay " + listaFamilias.size() + " familia(s) solicitada(s).");
            if (listaFamilias.size() >= 1) {
                listaFacturas0l = m90Manager.getDatosFacturasFamilia(codOrganizacion, numExpediente, listaFamilias.get(0).getCodigo(), adapt);
                if (!listaFacturas0l.isEmpty()) {
                    for (FacturaVO factura : listaFacturas0l) {
                        factura.setDescTipoGasto(getDescripcionDesplegable(request, factura.getDescTipoGasto()));
                        factura.setDescValidada(getDescripcionDesplegable(request, factura.getDescValidada()));
                        factura.setDescIvaSub(getDescripcionDesplegable(request, factura.getDescIvaSub()));
                        factura.setDescMotNoVal(getDescripcionDesplegable(request, factura.getDescMotNoVal()));
                        factura.setDescPorcentajeIva(getDescripcionDesplegable(request, factura.getDescPorcentajeIva()));
                    }
                }
                request.setAttribute("listaFamilias", listaFamilias);
                request.setAttribute("listaFacturas01", listaFacturas0l);
            }

            if (listaFamilias.size() >= 2) {
                listaFacturas02 = m90Manager.getDatosFacturasFamilia(codOrganizacion, numExpediente, listaFamilias.get(1).getCodigo(), adapt);
                if (!listaFacturas02.isEmpty()) {
                    for (FacturaVO factura : listaFacturas02) {
                        factura.setDescTipoGasto(getDescripcionDesplegable(request, factura.getDescTipoGasto()));
                        factura.setDescValidada(getDescripcionDesplegable(request, factura.getDescValidada()));
                        factura.setDescIvaSub(getDescripcionDesplegable(request, factura.getDescIvaSub()));
                        factura.setDescMotNoVal(getDescripcionDesplegable(request, factura.getDescMotNoVal()));
                        factura.setDescPorcentajeIva(getDescripcionDesplegable(request, factura.getDescPorcentajeIva()));
                    }
                }
                request.setAttribute("listaFacturas02", listaFacturas02);
            }

            if (listaFamilias.size() == 3) {
                listaFacturas03 = m90Manager.getDatosFacturasFamilia(codOrganizacion, numExpediente, listaFamilias.get(2).getCodigo(), adapt);
                if (!listaFacturas03.isEmpty()) {
                    for (FacturaVO factura : listaFacturas03) {
                        factura.setDescTipoGasto(getDescripcionDesplegable(request, factura.getDescTipoGasto()));
                        factura.setDescValidada(getDescripcionDesplegable(request, factura.getDescValidada()));
                        factura.setDescIvaSub(getDescripcionDesplegable(request, factura.getDescIvaSub()));
                        factura.setDescMotNoVal(getDescripcionDesplegable(request, factura.getDescMotNoVal()));
                        factura.setDescPorcentajeIva(getDescripcionDesplegable(request, factura.getDescPorcentajeIva()));
                    }
                }
                request.setAttribute("listaFacturas03", listaFacturas03);
            }

            request.setAttribute("numExp", numExpediente);
            request.setAttribute("modalidad", descModalidad);
            request.setAttribute("familias", listaFamilias.size());

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Error al recuperar los datos de las facturas - ", ex);
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
     * @return url de la ventana de mantenimiento de Facturas
     */
    public String cargarMantenimientoFactura(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarMantenimientoFactura - ");
        String url = "/jsp/extension/melanbide90/mantenimientoFactura.jsp";
        try {
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                // edicion
                FacturaVO datosModif = m90Manager.getFacturaPorId(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datosModif != null) {
                    datosModif.setDescPorcentajeIva(getDescripcionDesplegable(request, datosModif.getDescPorcentajeIva()));
                    datosModif.setDescPorcentajeIvaValidado(getDescripcionDesplegable(request, datosModif.getDescPorcentajeIvaValidado()));
                    request.setAttribute("datosModif", datosModif);
                }
                request.setAttribute("nuevo", "0");
            } else {
                // alta
                int ultimaFactura = Integer.parseInt(request.getParameter("ultimaFactura"));
                ultimaFactura++;
                request.setAttribute("numOrden", String.valueOf(ultimaFactura));
                request.setAttribute("nuevo", "1");
            }

            request.setAttribute("numExp", request.getParameter("numExp"));
            request.setAttribute("codFamilia", request.getParameter("codFamilia"));
            FamiliaSolicitadaVO familia = MeLanbide90Manager.getInstance().getFamiliaSolicitadaExpedienteCodigo(request.getParameter("numExp"), request.getParameter("codFamilia"), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            String descripcion = "";
            if (getIdioma(request) == 4) {
                descripcion = familia.getDescFamiliaEus();
            } else {
                descripcion = familia.getDescFamiliaCas();
            }
            request.setAttribute("desFamilia", descripcion);

            cargarDesplegablesFacturas(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de mantenimiento de FACTURAS : " + ex.getMessage());
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
    public void altaFactura(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en altaFactura - ");
        String codigoOperacion = "-1";
        List<FacturaVO> listaFacturas = new ArrayList<FacturaVO>();
        FacturaVO factura = new FacturaVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String numExp = request.getParameter("numExp");
            String codFamilia = request.getParameter("codFamilia");
            String numOrden = request.getParameter("numOrden");
            String tipoGasto = request.getParameter("tipoGasto");
            String proveedor = request.getParameter("proveedor");
            String numFactura = request.getParameter("numFactura");
            String fecEmision = request.getParameter("fecEmision");
            String fecPago = request.getParameter("fecPago");
            String base = request.getParameter("base").replace(",", ".");
            String iva = request.getParameter("iva").replace(",", ".");
            String validada = request.getParameter("validada");
            String subvencionable = request.getParameter("subvencionable");
            String importeTotal = request.getParameter("importeTotal");
            if (importeTotal != null && !importeTotal.isEmpty())
                factura.setImporteTotal(Double.valueOf(importeTotal.replace(",", ".")));
            String importeVali = request.getParameter("importeVali").replace(",", ".");
            String motivo = request.getParameter("motivo");
            String baseValidado = request.getParameter("baseValidado");
            if (baseValidado != null && !baseValidado.isEmpty())
                factura.setImporteBaseValidado(Double.valueOf(baseValidado.replace(",", ".")));
            String porIvaValidado = request.getParameter("porIvaValidado");
            if (porIvaValidado != null && !porIvaValidado.isEmpty())
                factura.setPorcentajeIvaValidado(porIvaValidado.replace(",", "."));
            String prorrataValidado = request.getParameter("prorrataValidado");
            if (prorrataValidado != null && !prorrataValidado.isEmpty())
                factura.setProrrataValidado(Integer.parseInt(prorrataValidado.replace(",", ".")));
            String importeIvaValidado = request.getParameter("importeIvaValidado");
            if (importeIvaValidado != null && !importeIvaValidado.isEmpty())
                factura.setImporteIvaValidado(Double.valueOf(importeIvaValidado.replace(",", ".")));


            factura.setNumExp(numExp);
            factura.setFamilia(codFamilia);
            factura.setNumOrden(Integer.valueOf(numOrden));
            factura.setTipoGasto(tipoGasto);
            factura.setProveedor(proveedor);
            factura.setNumFactura(numFactura);
            factura.setFecEmision(new java.sql.Date(formatoFecha.parse(fecEmision).getTime()));
            factura.setFecPago(new java.sql.Date(formatoFecha.parse(fecPago).getTime()));
            factura.setImporteBase(Double.valueOf(base));
            factura.setImporteIva(Double.valueOf(iva));
            factura.setValidada(validada);
            if (subvencionable != null && !subvencionable.isEmpty()) {
                factura.setIvaSub(subvencionable);
            }
            factura.setImporteVali(Double.valueOf(importeVali));
            if (motivo != null && !motivo.isEmpty()) {
                factura.setMotNoVal(motivo);
            }

            if (m90Manager.nuevaFactura(factura, adapt)) {
                log.debug("Factura insertada correctamente");
                codigoOperacion = "0";
                try {
                    listaFacturas = m90Manager.getDatosFacturasFamilia(codOrganizacion, numExp, codFamilia, adapt);
                    if (!listaFacturas.isEmpty()) {
                        for (FacturaVO fct : listaFacturas) {
                            fct.setDescTipoGasto(getDescripcionDesplegable(request, fct.getDescTipoGasto()));
                            fct.setDescValidada(getDescripcionDesplegable(request, fct.getDescValidada()));
                            fct.setDescIvaSub(getDescripcionDesplegable(request, fct.getDescIvaSub()));
                            fct.setDescMotNoVal(getDescripcionDesplegable(request, fct.getDescMotNoVal()));
                            fct.setDescPorcentajeIva(getDescripcionDesplegable(request, fct.getDescPorcentajeIva()));
                        }
                    }
                } catch (Exception e) {
                    codigoOperacion = "5";
                    log.error("Error al recuperar la lista de Facturas después de dar una de alta");
                }
            } else {
                log.error("NO se ha insertado correctamente la nueva Factura");
                codigoOperacion = "7";
            }
        } catch (Exception ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaFacturas);
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
     */
    public void modificarFactura(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarFactura - ");
        String codigoOperacion = "-1";
        List<FacturaVO> listaFacturas = new ArrayList<FacturaVO>();
        FacturaVO factura = new FacturaVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String codFamilia = request.getParameter("codFamilia");
            String numOrden = request.getParameter("numOrden");
            String tipoGasto = request.getParameter("tipoGasto");
            String proveedor = request.getParameter("proveedor");
            String numFactura = request.getParameter("numFactura");
            String fecEmision = request.getParameter("fecEmision");
            String fecPago = request.getParameter("fecPago");
            String base = request.getParameter("base").replace(",", ".");
            String iva = request.getParameter("iva").replace(",", ".");
            String validada = request.getParameter("validada");
            String subvencionable = request.getParameter("subvencionable");
            String importeVali = request.getParameter("importeVali").replace(",", ".");
            String importeTotal = request.getParameter("importeTotal");
            if (importeTotal != null && !importeTotal.isEmpty())
                factura.setImporteTotal(Double.valueOf(importeTotal.replace(",", ".")));
            String motivo = request.getParameter("motivo");
            String baseValidado = request.getParameter("baseValidado");
            if (baseValidado != null && !baseValidado.isEmpty())
                factura.setImporteBaseValidado(Double.valueOf(baseValidado.replace(",", ".")));
            String porIvaValidado = request.getParameter("porIvaValidado");
            if (porIvaValidado != null && !porIvaValidado.isEmpty())
                factura.setPorcentajeIvaValidado(porIvaValidado.replace(",", "."));
            String prorrataValidado = request.getParameter("prorrataValidado");
            if (prorrataValidado != null && !prorrataValidado.isEmpty())
                factura.setProrrataValidado(Integer.parseInt(prorrataValidado.replace(",", ".")));
            String importeIvaValidado = request.getParameter("importeIvaValidado");
            if (importeIvaValidado != null && !importeIvaValidado.isEmpty())
                factura.setImporteIvaValidado(Double.valueOf(importeIvaValidado.replace(",", ".")));

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la Factura a Modificar ");
                codigoOperacion = "3";
            } else {
                factura.setId(Integer.valueOf(id));
                factura.setNumExp(numExp);
                factura.setFamilia(codFamilia);
                factura.setNumOrden(Integer.valueOf(numOrden));
                factura.setTipoGasto(tipoGasto);
                factura.setProveedor(proveedor);
                factura.setNumFactura(numFactura);
                factura.setFecEmision(new java.sql.Date(formatoFecha.parse(fecEmision).getTime()));
                factura.setFecPago(new java.sql.Date(formatoFecha.parse(fecPago).getTime()));
                factura.setImporteBase(Double.valueOf(base));
                factura.setImporteIva(Double.valueOf(iva));
                factura.setValidada(validada);
                if (subvencionable != null && !subvencionable.isEmpty()) {
                    factura.setIvaSub(subvencionable);
                }
                factura.setImporteVali(Double.valueOf(importeVali));
                if (motivo != null && !motivo.isEmpty()) {
                    factura.setMotNoVal(motivo);
                }
                if (m90Manager.modificarFactura(factura, adapt)) {
                    log.debug("Factura modificada correctamente");
                    codigoOperacion = "0";
                    try {
                        listaFacturas = m90Manager.getDatosFacturasFamilia(codOrganizacion, numExp, codFamilia, adapt);
                        if (!listaFacturas.isEmpty()) {
                            for (FacturaVO fct : listaFacturas) {
                                fct.setDescTipoGasto(getDescripcionDesplegable(request, fct.getDescTipoGasto()));
                                fct.setDescValidada(getDescripcionDesplegable(request, fct.getDescValidada()));
                                fct.setDescIvaSub(getDescripcionDesplegable(request, fct.getDescIvaSub()));
                                fct.setDescMotNoVal(getDescripcionDesplegable(request, fct.getDescMotNoVal()));
                                fct.setDescPorcentajeIva(getDescripcionDesplegable(request, fct.getDescPorcentajeIva()));
                                fct.setDescPorcentajeIvaValidado(getDescripcionDesplegable(request, fct.getDescPorcentajeIvaValidado()));
                            }
                        }
                    } catch (Exception e) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de Facturas después de modificar una");
                    }
                } else {
                    log.error("NO se ha modificado la Factura");
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
            resultado.setAtributo("lista", listaFacturas);
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
     */
    public void eliminarFactura(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarFactura - ");
        String codigoOperacion = "-1";
        List<FacturaVO> listaFacturas = new ArrayList<FacturaVO>();
        String numExp = "";
        String codFamilia = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la SubSolic a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                codFamilia = request.getParameter("codFamilia");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                if (m90Manager.eliminarFactura(id, adapt)) {
                    codigoOperacion = "0";
                    try {
                        listaFacturas = m90Manager.getDatosFacturasFamilia(codOrganizacion, numExp, codFamilia, adapt);
                        if (!listaFacturas.isEmpty()) {
                            for (FacturaVO fct : listaFacturas) {
                                fct.setDescTipoGasto(getDescripcionDesplegable(request, fct.getDescTipoGasto()));
                                fct.setDescValidada(getDescripcionDesplegable(request, fct.getDescValidada()));
                                fct.setDescIvaSub(getDescripcionDesplegable(request, fct.getDescIvaSub()));
                                fct.setDescMotNoVal(getDescripcionDesplegable(request, fct.getDescMotNoVal()));
                                fct.setDescPorcentajeIva(getDescripcionDesplegable(request, fct.getDescPorcentajeIva()));
                            }
                        }
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de Facturas después de eliminar una");
                    }
                } else {
                    codigoOperacion = "6";
                }
            }
        } catch (Exception ex) {
            log.error("Error eliminando una Factura: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", listaFacturas);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    public void volcarDatos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en volcarDatos - ");
        String codigoOperacion = "-1";
        String numExp = "";
        String numFamilias = "";
        String docInteresado = null;
        int ejercicioExp = 0;
        FamiliaSolicitadaVO familiaSol = new FamiliaSolicitadaVO();
        Map<Integer, Integer> afsPeriodo = new HashMap<Integer, Integer>();
        Double[] importesMax;
        String codCampoMaxMinimis = ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_CAMPO_MAX_MINIMIS, ConstantesMeLanbide90.FICHERO_PROPIEDADES);
        String codCampoMinimis = ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_CAMPO_MINIMIS, ConstantesMeLanbide90.FICHERO_PROPIEDADES);
        String codCampoFinal = ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_CAMPO_SUBVFINAL, ConstantesMeLanbide90.FICHERO_PROPIEDADES);
        double maxFamilia = 0.0;
        double maxMinimis = 0.0;
        double minimis = 0.0;
        double subTotal = 0.0;
        double subFinal = 0.0;
        SalidaIntegracionVO campoMinimis = null;

        try {
            numExp = request.getParameter("numExp");
            numFamilias = request.getParameter("numFamilias");
            String[] datosExp = numExp.split(ConstantesMeLanbide90.BARRA_SEPARADORA);
            ejercicioExp = Integer.parseInt(datosExp[0]);
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            log.info("Hay " + numFamilias + " en el expediente");
            docInteresado = m90Manager.getDocumentoInteresado(codOrganizacion, numExp, adapt);
            importesMax = m90Manager.getImportesMaximosEjer(ejercicioExp, adapt);

            // defino los valores comunes de los campos a grabar
            CampoSuplementarioModuloIntegracionVO campoNumerico = new CampoSuplementarioModuloIntegracionVO();
            campoNumerico.setCodOrganizacion(String.valueOf(codOrganizacion));
            campoNumerico.setCodProcedimiento(datosExp[1]);
            campoNumerico.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
            campoNumerico.setTramite(false);
            campoNumerico.setNumExpediente(numExp);
            campoNumerico.setEjercicio(datosExp[0]);

            if (importesMax.length > 0) {
                maxFamilia = importesMax[0];
                maxMinimis = importesMax[1];
                log.info("Importe máximo por familia: " + maxFamilia);
                maxMinimis = Math.round(maxMinimis * 100) / 100.0;
                log.info("Importe máximo MINIMIS: " + maxMinimis);

                // grabar MAXIMO MINIMIS
                campoNumerico.setCodigoCampo(codCampoMaxMinimis);
                campoNumerico.setValorNumero(String.valueOf(maxMinimis));
                SalidaIntegracionVO respuesta =  el.grabarCampoSuplementario(campoNumerico);
                log.info("Importe máximo MINIMIS-grabar: " + respuesta.getStatus() + " - " + respuesta.getDescStatus());

                for (int i = 1; i <= Integer.parseInt(numFamilias); i++) {
                    String codFamilia = null;
                    String codCampoFamilia = ConfigurationParameter.getParameter(ConstantesMeLanbide90.PREF_CAMPO_FAMILIA, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + i;
                    String codCampoValidado = ConfigurationParameter.getParameter(ConstantesMeLanbide90.PREF_CAMPO_VALIDADO, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + i;
                    String codCampoEspecialidades = ConfigurationParameter.getParameter(ConstantesMeLanbide90.PREF_CAMPO_ESPECIALIDADES, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + i;
                    String codCampoMedia = ConfigurationParameter.getParameter(ConstantesMeLanbide90.PREF_CAMPO_MEDIA, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + i;
                    String codCampoPorcentaje = ConfigurationParameter.getParameter(ConstantesMeLanbide90.PREF_CAMPO_PORCENTAJE, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + i;
                    String codCampoSubvPor = ConfigurationParameter.getParameter(ConstantesMeLanbide90.PREF_CAMPO_SUBVCALCU, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + i;
                    String codCampoSubvCon = ConfigurationParameter.getParameter(ConstantesMeLanbide90.PREF_CAMPO_SUBVCONCE, ConstantesMeLanbide90.FICHERO_PROPIEDADES) + i;

                    int especialidades = 0;
                    int accionesAnio = 0;
                    int accionesPeriodo = 0;
                    int aniosAcreditados = 0;
                    boolean afConvocatoria = false;
                    double media = 0.0;
                    int porcentaje = 0;
                    double validado = 0.0;
                    double calculada = 0.0;
                    double concedida = 0.0;

                    // obtengo FAMILIAi del expediente
                    log.debug(i + "ª familia - " + codCampoFamilia);
                    codFamilia = m90Manager.getCodSelDesplegableExterno(codOrganizacion, numExp, codCampoFamilia, adapt);
                    if (codFamilia != null) {
                        log.info(codFamilia);
                        // obtengo los datos de la Familia
                        familiaSol = m90Manager.getFamiliaSolicitadaExpedienteCodigo(numExp, codFamilia, adapt);
                        if(familiaSol != null && familiaSol.getValidadoTotal()!= null )
                        validado = familiaSol.getValidadoTotal();

                        // grabar TOTAL VALIDADO
                        validado = Math.round(validado * 100) / 100.0;
                        log.info("validado: " + validado);
                        campoNumerico.setCodigoCampo(codCampoValidado);
                        campoNumerico.setValorNumero(String.valueOf(validado));
                        respuesta = el.grabarCampoSuplementario(campoNumerico);
                        log.info("validado-grabar: " + respuesta.getStatus() + " - " + respuesta.getDescStatus());


                        // obtengo ESPECIALIDADES
                        especialidades = m90Manager.getEspecialidadesFamiliaEjer(codFamilia, docInteresado, ejercicioExp, adapt);

                        // grabar ESPECIALIDADES
                        log.info("especialidades: " + especialidades);
                        campoNumerico.setCodigoCampo(codCampoEspecialidades);
                        campoNumerico.setValorNumero(String.valueOf(especialidades));
                        respuesta = el.grabarCampoSuplementario(campoNumerico);
                        log.info("validado-grabar: " + respuesta.getStatus() + " - " + respuesta.getDescStatus());

                        // obtengo los datos de las AAFF en el periodo
                        afsPeriodo = m90Manager.getAFsFamiliaPeriodo(codFamilia, docInteresado, ejercicioExp, adapt);
                        if (!afsPeriodo.isEmpty()) {
                            for (Map.Entry<Integer, Integer> entry : afsPeriodo.entrySet()) {
                                Integer key = entry.getKey();
                                accionesAnio = entry.getValue();
                                log.info("Ejercicio " + key + " => " + accionesAnio);
                                if (accionesAnio > 0) {
                                    accionesPeriodo += accionesAnio;
                                    aniosAcreditados++;
                                    if (key == ejercicioExp) {
                                        afConvocatoria = true;
                                    }
                                }
                            }
                            /*
                        Si se han impartido AFs en el año de la convocatoria: Se suman las AFs del período de los 5 años y se divide por el número de años en que hay AFs impartidas.
                        Si no se han impartido AFs en el año de la convocatoria, pero sí se han impartido AFs al menos durante 3 años del periodo,
                            se suman las AFs del período de los 5 años y se divide por el número de años en que hay AFs.
                        Para el resto de los casos la media es 0
                             */
                            log.info("En convocatoria: " + afConvocatoria);
                            log.info("Especialidades: " + especialidades);
                            log.info("Total acciones: " + accionesPeriodo);
                            log.info("Años acreditados: " + aniosAcreditados);
//descomentar en PRE
                            if (afConvocatoria || aniosAcreditados >= 3) {
                                media = Double.valueOf(accionesPeriodo / aniosAcreditados);
                            } else {
                                log.info("No cumple condiciones AAFF");
                            }
                            media = Math.round(media * 100) / 100.0;
                            log.info("Media: " + media);
                            // grabar MEDIA AAFF                     
                            campoNumerico.setCodigoCampo(codCampoMedia);
                            campoNumerico.setValorNumero(String.valueOf(media));
                            respuesta = el.grabarCampoSuplementario(campoNumerico);
                            log.info("media-grabar: " + respuesta.getStatus() + " - " + respuesta.getDescStatus());

                            // calcular porcentaje
                            if (especialidades >= 2 && media >= 2) {
                                if (media >= 2 && media <= 5.5) {
                                    log.info("Cod media = A");
                                    porcentaje = 55;
                                } else if (media > 5.5 && media <= 10) {
                                    log.info("Cod media = B");
                                    porcentaje = 60;
                                } else if (media > 10) {
                                    log.info("Cod media = C");
                                    porcentaje = 65;
                                }
                                if (especialidades >= 5 && especialidades <= 10) {
                                    log.info("Cod especialidades = B");
                                    porcentaje += 5;
                                } else if (especialidades > 10) {
                                    log.info("Cod especialidades = C");
                                    porcentaje += 10;
                                } else {
                                    log.info("Cod especialidades = A");
                                }
                            } else {
                                log.info("Las especialidades acreditadas (" + especialidades + ") y/o la media (" + media + ") son inferiores a 2");
                            }

                            log.info("Porcentaje: " + porcentaje + "%");
                            log.info("Validado: " + validado);
                            calculada = Math.round((validado * porcentaje)) / 100.0;
                            log.info("Subvención CALCULADA: " + calculada);
                            if (calculada > maxFamilia) {
                                concedida = maxFamilia;
                            } else {
                                concedida = calculada;
                            }
                            log.info("Subvención Concedida: " + concedida);

                        } else {
                            log.error("No se han recuperado Acciones Formativas de " + codFamilia + " en el expediente " + numExp);
                        }
                        // grabar PORCENTAJE                                             
                        campoNumerico.setCodigoCampo(codCampoPorcentaje);
                        campoNumerico.setValorNumero(String.valueOf(porcentaje));
                        respuesta = el.grabarCampoSuplementario(campoNumerico);
                        log.info("porcentaje-grabar: " + respuesta.getStatus() + " - " + respuesta.getDescStatus());

                        // grabar SUBV CALCULADA
                        calculada = Math.round( calculada * 100) / 100.0;
                        campoNumerico.setCodigoCampo(codCampoSubvPor);
                        campoNumerico.setValorNumero(String.valueOf(calculada));
                        respuesta = el.grabarCampoSuplementario(campoNumerico);
                        log.info("subvCalculada-grabar: " + respuesta.getStatus() + " - " + respuesta.getDescStatus());


                        // grabar SUBV CONCEDIDA
                        concedida = Math.round(concedida * 100) / 100.0;
                        campoNumerico.setCodigoCampo(codCampoSubvCon);
                        campoNumerico.setValorNumero(String.valueOf(concedida));
                        respuesta = el.grabarCampoSuplementario(campoNumerico);
                        log.info("subvConcedida-grabar: " + respuesta.getStatus() + " - " + respuesta.getDescStatus());

                    } else {
                        log.info("No hay datos en la " + codCampoFamilia);
                    }
                    subTotal += concedida;
                }// for
                log.info("Subv TOTAL: " + subTotal);
                subTotal = Math.round(subTotal * 100) / 100.0;
                log.info("Subv TOTAL: " + subTotal);
                campoMinimis = el.getCampoSuplementarioExpediente(Integer.toString(codOrganizacion), datosExp[0], numExp, datosExp[1], codCampoMinimis, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);
                if (campoMinimis != null && campoMinimis.getStatus() == 0) {
                    minimis = Double.parseDouble(campoMinimis.getCampoSuplementario().getValorNumero());
                    log.info("Minimis: " + minimis);
                    log.info("      -----------");
                    log.info("Suma: " + (subTotal + minimis));
                    // el mismo que el del campo SUBVENCIÓN TOTAL si la suma de los MINIMIS A APLICAR y la SUBVENCIÓN TOTAL no supera los 300.000 euros. Si los superara sería la diferencia entre 300.000 ¤ y los MINIMIS a aplicar
                    if ((subTotal + minimis) > maxMinimis) {
                        subFinal = maxMinimis - minimis;
                    } else {
                        subFinal = subTotal;
                    }
                    log.info("Subv FINAL: " + subFinal);
                    subFinal = Math.round(subFinal * 100) / 100.0;
                    if(subFinal < 0)
                        subFinal = 0;
                    log.info("Subv FINAL: " + subFinal);
                    // grabar SUBV FINAL
                    campoNumerico.setCodigoCampo(codCampoFinal);
                    campoNumerico.setValorNumero(String.valueOf(subFinal));
                    respuesta =  el.grabarCampoSuplementario(campoNumerico);
                    log.info("Subv FINAL-grabar: " + respuesta.getStatus() + " - " + respuesta.getDescStatus());
                    // todo OK 
                    codigoOperacion = "0";
                } else {
                    log.error(" -- Error recuperando MINIMIS");
                    codigoOperacion = "01";
                }

            } else {
                log.error("No se han recuperado los importes máximos de " + ejercicioExp);
                codigoOperacion = "";
            }

        } catch (Exception ex) {
            log.error("Excepción en el proceso " + ex.getMessage());
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
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
        List<DesplegableAdmonLocalVO> listaEstado = MeLanbide90Manager.getInstance().getValoresDesplegablesXdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_ESTADO_AYUDA, ConstantesMeLanbide90.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaEstado.isEmpty()) {
            listaEstado = traducirListaDesplegables(request, listaEstado);
            request.setAttribute("listaEstado", listaEstado);
        }
    }

    private void cargarDesplegablesFacturas(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableAdmonLocalVO> listaTipoGasto = MeLanbide90Manager.getInstance().getValoresDesplegablesXdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_GASTO, ConstantesMeLanbide90.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaTipoGasto.isEmpty()) {
            listaTipoGasto = traducirListaDesplegables(request, listaTipoGasto);
            request.setAttribute("listaTipoGasto", listaTipoGasto);
        }

        List<DesplegableAdmonLocalVO> listaBool = MeLanbide90Manager.getInstance().getValoresDesplegablesXdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_BOOL, ConstantesMeLanbide90.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaBool.isEmpty()) {
            listaBool = traducirListaDesplegables(request, listaBool);
            request.setAttribute("listaBool", listaBool);
        }

        List<DesplegableAdmonLocalVO> listaMotivos = MeLanbide90Manager.getInstance().getValoresDesplegablesXdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_MOTIVO, ConstantesMeLanbide90.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaMotivos.isEmpty()) {
            listaMotivos = traducirListaDesplegables(request, listaMotivos);
            request.setAttribute("listaMotivo", listaMotivos);
        }
        List<DesplegableAdmonLocalVO> listaPorcentajes = MeLanbide90Manager.getInstance().getValoresDesplegablesXdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_PORCIVA, ConstantesMeLanbide90.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaPorcentajes.isEmpty()) {
            listaPorcentajes = traducirListaDesplegables(request, listaPorcentajes);
            request.setAttribute("listaPorcentajes", listaPorcentajes);
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
    private List<DesplegableAdmonLocalVO> traducirListaDesplegables(HttpServletRequest request, List<DesplegableAdmonLocalVO> desplegable) {
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
        String barraSeparadoraIdioma = ConfigurationParameter.getParameter(ConstantesMeLanbide90.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide90.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraIdioma) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == ConstantesMeLanbide90.CODIGO_IDIOMA_EUSKERA) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                }
            } else {
                descripcion = " ";
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
        int idioma = ConstantesMeLanbide90.CODIGO_IDIOMA_CASTELLANO;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = ConstantesMeLanbide90.CODIGO_IDIOMA_CASTELLANO;
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

    public void generarExcelListafacturas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en generarExcelListaExpedientes de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        String numExp = null;
        MeLanbide90I18n m90i18n = MeLanbide90I18n.getInstance();
        // Configurar formato de moneda
        NumberFormat formatNumber = NumberFormat.getCurrencyInstance(Locale.ITALY);

        if (request.getParameter("numExp") != null) {
            numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
        }
        int idioma = 1;
        try {
            adapt = getAdaptSQLBD(String.valueOf(codOrganizacion));
            idioma = getIdioma(request);
        } catch (SQLException ex) {
            log.error(this.getClass().getSimpleName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        try {
            // Crear libro de Excel y estilos
            HSSFWorkbook libro = new HSSFWorkbook();
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor darkRedColor = palette.findSimilarColor(153, 0, 0);  // DARK_RED
            HSSFColor whiteColor = palette.findSimilarColor(255, 255, 255);  // Blanco

            // Crear fuentes
            HSSFFont fontTitulo = libro.createFont();
            fontTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            fontTitulo.setFontHeightInPoints((short) 16);
            fontTitulo.setColor(darkRedColor.getIndex());

            HSSFFont fontCabecera = libro.createFont();
            fontCabecera.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            fontCabecera.setColor(whiteColor.getIndex());

            HSSFFont fontTotal = libro.createFont();
            fontTotal.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

            // ESTILOS
            HSSFCellStyle estiloTitulo = libro.createCellStyle();
            estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloTitulo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloTitulo.setFont(fontTitulo);

            HSSFCellStyle estiloCabecera = libro.createCellStyle();
            estiloCabecera.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCabecera.setFillForegroundColor(darkRedColor.getIndex());
            estiloCabecera.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCabecera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCabecera.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCabecera.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCabecera.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCabecera.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCabecera.setWrapText(true);
            estiloCabecera.setFont(fontCabecera);

            HSSFCellStyle estiloCentrado = libro.createCellStyle();
            estiloCentrado.setBorderTop(HSSFCellStyle.BORDER_THIN);
            estiloCentrado.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estiloCentrado.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCentrado.setBorderRight(HSSFCellStyle.BORDER_THIN);
            estiloCentrado.setAlignment(HSSFCellStyle.ALIGN_CENTER);

            HSSFCellStyle estiloIzquierda = libro.createCellStyle();
            estiloIzquierda.setBorderTop(HSSFCellStyle.BORDER_THIN);
            estiloIzquierda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estiloIzquierda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloIzquierda.setBorderRight(HSSFCellStyle.BORDER_THIN);
            estiloIzquierda.setAlignment(HSSFCellStyle.ALIGN_LEFT);

            HSSFCellStyle estiloImportes = libro.createCellStyle();
            estiloImportes.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            estiloImportes.setBorderTop(HSSFCellStyle.BORDER_THIN);
            estiloImportes.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estiloImportes.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloImportes.setBorderRight(HSSFCellStyle.BORDER_THIN);

            HSSFCellStyle estiloTotal = libro.createCellStyle();
            estiloTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            estiloTotal.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloTotal.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloTotal.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloTotal.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloTotal.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloTotal.setFont(fontTotal);

            // Cabeceras de la tabla
            String[] cabeceras = {
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col1"), // N orden
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col2"), // Tipo Gasto
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col3"), // Proveedor
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col4"), // n factura
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col5"), // emision
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col6"), // pago
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col7"), // base
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col8"), // iva
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col9"), // total
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col10"), // validada
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col11"), // iva sub
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col12"), // imp validado
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col13") // motivo
            };

            String[] cabecerasResumen = {
                m90i18n.getMensaje(idioma, "label.codigo"),// codigo
                m90i18n.getMensaje(idioma, "label.familia"), // descripcion
                m90i18n.getMensaje(idioma, "label.base"), // base
                m90i18n.getMensaje(idioma, "label.iva"), // iva
                m90i18n.getMensaje(idioma, "label.facturas.tabla.col9"), // total
                m90i18n.getMensaje(idioma, "label.totalValidado") // validado
            };

            double baseTotal = 0;
            double ivaTotal = 0;
            double totalTotal = 0;
            double validadoTotal = 0;
            List<FamiliaSolicitadaVO> listaFamilias = m90Manager.getFamiliasSolicitadas(numExp, adapt);

            HSSFSheet hoja = null;
            HSSFRow fila = null;
            HSSFCell celda = null;
            int numFila = 0;
            int col = 0;
            double baseFamilia = 0;
            double ivaFamilia = 0;
            double totalFamilia = 0;
            double validadoFamilia = 0;
            String descFam = null;
            for (FamiliaSolicitadaVO familiaSolicitada : listaFamilias) {
                String codFam = familiaSolicitada.getCodigo();
                if (idioma == ConstantesMeLanbide90.CODIGO_IDIOMA_CASTELLANO) {
                    descFam = familiaSolicitada.getDescFamiliaCas();
                } else {
                    descFam = familiaSolicitada.getDescFamiliaEus();
                }
                
                // Calcular los totales
                baseFamilia = familiaSolicitada.getBaseTotal();
                ivaFamilia = familiaSolicitada.getIvaTotal();
                totalFamilia = familiaSolicitada.getImporteTotal();
                validadoFamilia = familiaSolicitada.getValidadoTotal();

                List<FacturaVO> listaFacturasFamilia = new ArrayList<FacturaVO>();
                try {
                    listaFacturasFamilia = MeLanbide90Manager.getInstance().getDatosFacturasFamilia(codTramite, numExp, codFam, adapt);
                } catch (Exception ex) {
                    log.error("Error al recuperar los datos del excel de Lista de Facturas para la familia " + codFam, ex);
                }
                
                // Solo crear una hoja si la familia tiene facturas asociadas
                if (listaFacturasFamilia != null && !listaFacturasFamilia.isEmpty()) {
                    // Crear una hoja para la familia con el nombre adecuado
                    hoja = libro.createSheet(codFam);
                    numFila = 0;
                    col = 0;

                    hoja.setColumnWidth(0, 1000); // Orden
                    hoja.setColumnWidth(1, 6000); // Tipo Gasto
                    hoja.setColumnWidth(2, 12000); // Proveedor
                    hoja.setColumnWidth(3, 5000); // Num Factura
                    hoja.setColumnWidth(4, 2600); // Fecha Emision
                    hoja.setColumnWidth(5, 2600); // Fecha Pago
                    hoja.setColumnWidth(6, 3000); // Base
                    hoja.setColumnWidth(7, 3000); // IVA
                    hoja.setColumnWidth(8, 3000); // Total
                    hoja.setColumnWidth(9, 2500); // Validada
                    hoja.setColumnWidth(10, 2500); // IVA subvencionable  
                    hoja.setColumnWidth(11, 3000); // Imp validado
                    hoja.setColumnWidth(12, 2700); // motivo NO validada

                    // Añadir título "DETALLE FACTURAS"
                    HSSFRow filaTitulo = hoja.createRow(numFila++);
                    HSSFCell celdaTitulo = filaTitulo.createCell(col);
                    celdaTitulo.setCellValue(m90i18n.getMensaje(idioma, "label.tituloFacturas"));
                    celdaTitulo.setCellStyle(estiloTitulo);
                    filaTitulo.setHeightInPoints(35);
                    hoja.addMergedRegion(new CellRangeAddress(0, 0, 0, cabeceras.length - 1));

                    HSSFRow filaSubtitulo = hoja.createRow(numFila++);
                    HSSFCell celdaSubtitulo = filaSubtitulo.createCell(col);
                    celdaSubtitulo.setCellValue(descFam);
                    celdaSubtitulo.setCellStyle(estiloTitulo);
                    filaSubtitulo.setHeightInPoints(25);
                    hoja.addMergedRegion(new CellRangeAddress(1, 1, 0, cabeceras.length - 1));

                    // Crear fila de cabecera
                    fila = hoja.createRow(numFila++);

                    // Añadir las cabeceras
                    for (int j = 0; j < cabeceras.length; j++) {
                        celda = fila.createCell(j);
                        celda.setCellValue(cabeceras[j]);
                        celda.setCellStyle(estiloCabecera);
                    }

                    // Insertar los datos de las facturas
                    for (FacturaVO factura : listaFacturasFamilia) {
                        col = 0;
                        fila = hoja.createRow(numFila++);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getNumOrden());
                        celda.setCellStyle(estiloCentrado);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getTipoGasto() != null ? getDescripcionDesplegable(request, factura.getDescTipoGasto()) : "");
                        celda.setCellStyle(estiloIzquierda);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getProveedor() != null ? factura.getProveedor() : "");
                        celda.setCellStyle(estiloIzquierda);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getNumFactura() != null ? factura.getNumFactura() : "");
                        celda.setCellStyle(estiloIzquierda);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getFecEmision() != null ? formatoFecha.format(factura.getFecEmision()) : "");
                        celda.setCellStyle(estiloCentrado);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getFecPago() != null ? formatoFecha.format(factura.getFecPago()) : "");
                        celda.setCellStyle(estiloCentrado);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getImporteBase() != null ? formatNumber.format(factura.getImporteBase()) : "");
                        celda.setCellStyle(estiloImportes);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getImporteIva() != null ? formatNumber.format(factura.getImporteIva()) : "");
                        celda.setCellStyle(estiloImportes);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getImporteTotal() != null ? formatNumber.format(factura.getImporteTotal()) : "");
                        celda.setCellStyle(estiloImportes);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getValidada() != null ? getDescripcionDesplegable(request, factura.getDescValidada()) : "");
                        celda.setCellStyle(estiloCentrado);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getIvaSub() != null ? getDescripcionDesplegable(request, factura.getDescIvaSub()) : "");
                        celda.setCellStyle(estiloCentrado);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getImporteVali() != null ? formatNumber.format(factura.getImporteVali()) : "");
                        celda.setCellStyle(estiloImportes);

                        celda = fila.createCell(col++);
                        celda.setCellValue(factura.getMotNoVal() != null ? factura.getMotNoVal() : "");
                        celda.setCellStyle(estiloCentrado);
                    } // factura
                    
                    numFila++; // Aumentar el número de fila para que haya una fila vacía antes del total
                    col = 5;
                    fila = hoja.createRow(numFila++);

                    celda = fila.createCell(col++);
                    celda.setCellValue(m90i18n.getMensaje(idioma, "label.totalFamilia"));
                    celda.setCellStyle(estiloCentrado);

                    celda = fila.createCell(col++);
                    celda.setCellValue(formatNumber.format(baseFamilia));
                    celda.setCellStyle(estiloTotal);

                    celda = fila.createCell(col++);
                    celda.setCellValue(formatNumber.format(ivaFamilia));
                    celda.setCellStyle(estiloTotal);

                    celda = fila.createCell(col++);
                    celda.setCellValue(formatNumber.format(totalFamilia));
                    celda.setCellStyle(estiloTotal);

                    celda = fila.createCell(col++);
                    celda.setCellValue(m90i18n.getMensaje(idioma, "label.totalValidado"));
                    celda.setCellStyle(estiloCentrado);
                    hoja.addMergedRegion(new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), 9, col++));

                    celda = fila.createCell(col++);
                    celda.setCellValue(formatNumber.format(validadoFamilia));
                    celda.setCellStyle(estiloTotal);
                } else {
                    log.info("No se encontraron facturas para la familia: " + codFam);
                }
            } // familia

            // resumen
            hoja = libro.createSheet(m90i18n.getMensaje(idioma, "label.resumen"));
            numFila = 0;
            col = 1;

            hoja.setColumnWidth(col++, 2000); // codigo
            hoja.setColumnWidth(col++, 12000); // descripcion
            hoja.setColumnWidth(col++, 4000); // base
            hoja.setColumnWidth(col++, 4000); // iva
            hoja.setColumnWidth(col++, 4000); // total
            hoja.setColumnWidth(col++, 4000); // validado

            fila = hoja.createRow(numFila++);
            fila = hoja.createRow(numFila++);
            col = 1;

            // Añadir las cabeceras
            for (String cabecera : cabecerasResumen) {
                celda = fila.createCell(col++);
                celda.setCellValue(cabecera);
                celda.setCellStyle(estiloCabecera);
            }
            for (FamiliaSolicitadaVO familiaSolicitada : listaFamilias) {
                // Calcular los totales
                baseTotal += familiaSolicitada.getBaseTotal();
                ivaTotal += familiaSolicitada.getIvaTotal();
                totalTotal += familiaSolicitada.getImporteTotal();
                validadoTotal += familiaSolicitada.getValidadoTotal();

                fila = hoja.createRow(numFila++);
                col = 1;

                // insertamos los datos de la familia
                celda = fila.createCell(col++);
                celda.setCellValue(familiaSolicitada.getCodigo());
                celda.setCellStyle(estiloCentrado);

                celda = fila.createCell(col++);
                if (idioma == ConstantesMeLanbide90.CODIGO_IDIOMA_CASTELLANO) {
                    celda.setCellValue(familiaSolicitada.getDescFamiliaCas());
                } else {
                    celda.setCellValue(familiaSolicitada.getDescFamiliaEus());
                }
                celda.setCellStyle(estiloIzquierda);

                celda = fila.createCell(col++);
                celda.setCellValue(formatNumber.format(familiaSolicitada.getBaseTotal()));
                celda.setCellStyle(estiloImportes);

                celda = fila.createCell(col++);
                celda.setCellValue(formatNumber.format(familiaSolicitada.getIvaTotal()));
                celda.setCellStyle(estiloImportes);

                celda = fila.createCell(col++);
                celda.setCellValue(formatNumber.format(familiaSolicitada.getImporteTotal()));
                celda.setCellStyle(estiloImportes);

                celda = fila.createCell(col++);
                celda.setCellValue(formatNumber.format(familiaSolicitada.getValidadoTotal()));
                celda.setCellStyle(estiloImportes);
            }
            
            fila = hoja.createRow(numFila++);
            col = 2;

            celda = fila.createCell(col++);
            celda.setCellValue(m90i18n.getMensaje(idioma, "label.totalFacturas"));
            celda.setCellStyle(estiloCentrado);

            celda = fila.createCell(col++);
            celda.setCellValue(formatNumber.format(baseTotal));
            celda.setCellStyle(estiloTotal);

            celda = fila.createCell(col++);
            celda.setCellValue(formatNumber.format(ivaTotal));
            celda.setCellStyle(estiloTotal);

            celda = fila.createCell(col++);
            celda.setCellValue(formatNumber.format(totalTotal));
            celda.setCellStyle(estiloTotal);

            celda = fila.createCell(col++);
            celda.setCellValue(formatNumber.format(validadoTotal));
            celda.setCellStyle(estiloTotal);

            numFila+=4;            

            fila = hoja.createRow(numFila++);
            col = 1;
            celda = fila.createCell(col);
            celda.setCellValue(m90i18n.getMensaje(idioma, "label.facturas.tabla.col13"));
            celda.setCellStyle(estiloTitulo);
            hoja.addMergedRegion(new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), col, col + 1));

            List<DesplegableAdmonLocalVO> listaMotivos = new ArrayList<DesplegableAdmonLocalVO>();
            try {
                listaMotivos = m90Manager.getValoresDesplegablesXdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide90.COD_DES_MOTIVO, ConstantesMeLanbide90.FICHERO_PROPIEDADES), adapt);
            } catch (Exception ex) {
                log.error("Error al recuperar los motivos de no validación ", ex);
            }

            for (DesplegableAdmonLocalVO motivo : listaMotivos) {
                col = 1;
                fila = hoja.createRow(numFila++);
                celda = fila.createCell(col++);
                celda.setCellValue(motivo.getDes_val_cod());
                celda.setCellStyle(estiloCentrado);

                celda = fila.createCell(col++);
                celda.setCellValue(getDescripcionDesplegable(request, motivo.getDes_nom()));
                celda.setCellStyle(estiloIzquierda);
            }

// escribir el fichero
            ByteArrayOutputStream excelOutput = new ByteArrayOutputStream();
            libro.write(excelOutput);
            excelOutput.close();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=DETALLE_FACTURAS_" + numExp.replace(ConstantesMeLanbide90.BARRA_SEPARADORA, "_") + ".xls");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setContentLength(excelOutput.size());
            response.getOutputStream().write(excelOutput.toByteArray(), 0, excelOutput.size());
            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (IOException ioe) {
            log.error("EXCEPCION informe listaFacturas", ioe);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MELANBIDE90.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
