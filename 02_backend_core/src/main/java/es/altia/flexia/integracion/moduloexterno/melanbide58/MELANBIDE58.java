/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide58.manager.MeLanbide58Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.AltaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.BajaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.CausaAltaBajaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DiscapacitadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.PlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.SMIVO;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.ConstantesGestionErroresDokusi;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.ErrorLan6ExcepcionBean;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.exception.GestionarErroresDokusi;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.manager.MeLanbideDokusiManager;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.util.ConstantesMeLanbide_Dokusi;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.util.MeLanbideDokusiUtil;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.technical.PortableContext;
import es.altia.util.commons.MimeTypes;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6ErrorBean;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author davidg
 */
public class MELANBIDE58 extends ModuloIntegracionExterno {

    private static final Logger log = LogManager.getLogger(MELANBIDE58.class);
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);

        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);

    }

    // ----------------------------------------------------------------------------------------------------------
    // --------------------------  ANEXO A     ------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
    public String cargarPantallaAnexoA(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaAnexoA de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                // SubPestana ALTAS
                List<AltaVO> listAltas = MeLanbide58Manager.getInstance().getDatosAltas(numExpediente, codOrganizacion, con);
                if (!listAltas.isEmpty()) {
                    request.setAttribute("listaAltas", listAltas);
                }
                request.setAttribute("urlPestanaAltas", "/jsp/extension/melanbide58/altas.jsp");
                // SubPestana BAJAS
                List<BajaVO> listBajas = MeLanbide58Manager.getInstance().getDatosBajas(numExpediente, codOrganizacion, con);
                if (!listBajas.isEmpty()) {
                    request.setAttribute("listaBajas", listBajas);
                }
                request.setAttribute("urlPestanaBajas", "/jsp/extension/melanbide58/bajas.jsp");
            } catch (Exception ex) {
                log.error("Error al recueperar los datos De anexoA - MELANBIDE58 - cargarPantallaAnexoA", ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        log.info("antes de retornar la url : /jsp/extension/melanbide58/anexoA.jsp");
        return "/jsp/extension/melanbide58/anexoA.jsp";
    }

    // ------------------  ALTAS  -------------------------------------------------------------------------------
    public String cargarNuevoAlta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoAlta de " + this.getClass().getSimpleName());
        String nuevo = "1";
        String numExp = "";
        int numLinea = 0;
        AdaptadorSQLBD adapt = null;
        MeLanbide58Manager meLanbide58Manager = new MeLanbide58Manager();
        String url = "/jsp/extension/melanbide58/nuevoAlta.jsp?codOrganizacion=" + codOrganizacion;
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

            List<CausaAltaBajaVO> listaCausasAlta = MeLanbide58Manager.getInstance().getListAltas(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaCausasAlta != null) {
                for (CausaAltaBajaVO causaAl : listaCausasAlta) {
                    causaAl.setDes_nombre(getDescripcionDesplegable(request, causaAl.getDes_nombre()));
                }
                request.setAttribute("listaCausas", listaCausasAlta);
            }
            // recupero el último nº de linea en el anexo A-Altas
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            numLinea = meLanbide58Manager.getUltimoNumLinea(numExp, ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_ALTAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            log.debug("Último número de línea en el anexo A-Altas: " + numLinea);
            request.setAttribute("numLineaA", String.valueOf(numLinea + 1));
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de un Nuevo alta : " + ex.getMessage());
        }
        return url;
    }

    public String cargarModificarAlta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarAlta de " + this.getClass().getSimpleName());
        String nuevo = "0";
        String url = "/jsp/extension/melanbide58/nuevoAlta.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            // Recuperramos datos de alta a modificar y cargamos en el request
            if (id != null && !id.isEmpty()) {
                AltaVO datModif = MeLanbide58Manager.getInstance().getAltaPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            List<CausaAltaBajaVO> listaCausasAlta = MeLanbide58Manager.getInstance().getListAltas(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if (listaCausasAlta != null) {
                for (CausaAltaBajaVO causaAl : listaCausasAlta) {
                    causaAl.setDes_nombre(getDescripcionDesplegable(request, causaAl.getDes_nombre()));
                }
                request.setAttribute("listaCausas", listaCausasAlta);
            }
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return url;
    }

    public void eliminarAlta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarAlta de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<AltaVO> lista = new ArrayList<AltaVO>();
        String numExp = "";
        Connection con = null;
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del alta a Eliminar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                if (adapt != null) {
                    try {
                        con = adapt.getConnection();
                        int result = MeLanbide58Manager.getInstance().eliminarAlta(id, adapt);
                        if (result <= 0) {
                            codigoOperacion = "1";
                        } else {
                            codigoOperacion = "0";
                            try {
                                lista = MeLanbide58Manager.getInstance().getDatosAltas(numExp, codOrganizacion, con);
                                for (AltaVO alta : lista) {
                                    if (!alta.getCausaDesc().isEmpty() && alta.getCausaDesc() != null) {
                                        alta.setCausaDesc(getDescripcionDesplegable(request, alta.getCausaDesc()));
                                    }
                                }
                            } catch (Exception ex) {
                                log.error("Error al recuperar la lista de altas despues de eliminar un registro");
                            }
                        }

                    } catch (Exception ex) {
                        log.error("Error eliminando un registro de alta :", ex);
                    } finally {
                        try {
                            adapt.devolverConexion(con);
                        } catch (BDException e) {
                            log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error eliminando un registro de alta : " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAltas(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void buscarAlta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en buscarAlta de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<AltaVO> lista = new ArrayList<AltaVO>();
        String numExp = "";
        Connection con = null;
        try {
            String nif = request.getParameter("nif");
            String numLinea = request.getParameter("numLinea");
            String apellidos = request.getParameter("apellidos");
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            if (adapt != null) {
                try {
                    con = adapt.getConnection();
                    lista = MeLanbide58Manager.getInstance().getRegistrosAlta(numExp, nif, numLinea, apellidos, codOrganizacion, con);
                    if (lista.isEmpty()) {
                        codigoOperacion = "4";
                    }
                } catch (Exception ex) {
                    codigoOperacion = "1";
                    log.error("Error al recuperar datos de alta :", ex);
                } finally {
                    try {
                        adapt.devolverConexion(con);
                    } catch (BDException e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }

        } catch (SQLException ex) {
            log.error("Error buscando registros de alta : " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAltas(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void crearNuevoAlta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoAlta de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<AltaVO> lista = new ArrayList<AltaVO>();
        AltaVO nuevoVO = new AltaVO();
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE58.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (adapt != null) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                //Recojo los parametros
                String nombre = request.getParameter("nombre");
                String apellidos = request.getParameter("apellidos");
                String nif = request.getParameter("nif");
                String fechaAlta = request.getParameter("fechaAlta");
                String numSS = request.getParameter("numSS");
                String numLinea = request.getParameter("numLinea");
                String numExp = request.getParameter("expediente");
                String causa = request.getParameter("causa");

                nuevoVO.setNumExp(numExp);
                nuevoVO.setNombre(nombre);
                nuevoVO.setApellidos(apellidos);
                nuevoVO.setNif(nif);

                if (fechaAlta != null && !"".equals(fechaAlta)) {
                    nuevoVO.setFechaAlta(new java.sql.Date(dateFormat.parse(fechaAlta).getTime()));
                }
                nuevoVO.setNumSS(numSS);
                if (numLinea != null && !"".equals(numLinea)) {
                    nuevoVO.setNumLinea(Integer.valueOf(numLinea));
                }
                nuevoVO.setCausa(causa);

                MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
                boolean insertOK = meLanbide58Manager.crearNuevoAlta(nuevoVO, con);
                if (insertOK) {
                    log.debug("Registro de alta Insertado Correctamente");
                    lista = meLanbide58Manager.getDatosAltas(numExp, codOrganizacion, con);
                    for (AltaVO alta : lista) {
                        if (!alta.getCausaDesc().isEmpty() && alta.getCausaDesc() != null) {
                            alta.setCausaDesc(getDescripcionDesplegable(request, alta.getCausaDesc()));
                        }
                    }
                } else {
                    log.error("NO se ha insertado correctamente el nuevo registro de alta");
                    codigoOperacion = "1";
                }
            } catch (Exception ex) {
                log.error("Error al parsear los parametros recibidos del jsp al objeto AltaVO" + ex.getMessage());
                codigoOperacion = "2";
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAltas(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarAlta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarAlta de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<AltaVO> lista = new ArrayList<AltaVO>();
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE58.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (adapt != null) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                //Recojo los parametros
                String id = request.getParameter("id");
                String nombre = request.getParameter("nombre");
                String apellidos = request.getParameter("apellidos");
                String nif = request.getParameter("nif");
                String fechaAlta = request.getParameter("fechaAlta");
                String numSS = request.getParameter("numSS");
                String numLinea = request.getParameter("numLinea");
                String numExp = request.getParameter("expediente");
                String causa = request.getParameter("causa");

                if (id == null || id.isEmpty()) {
                    log.debug("No se ha recibido desde la JSP el id del registro Alta a Modificar ");
                    codigoOperacion = "3";
                } else {
                    AltaVO datModif = new AltaVO();
                    datModif.setId(Integer.valueOf(id));
                    datModif.setNombre(nombre);
                    datModif.setApellidos(apellidos);
                    datModif.setNif(nif);
                    datModif.setNumSS(numSS);
                    if (fechaAlta != null && !"".equals(fechaAlta)) {
                        datModif.setFechaAlta(new java.sql.Date(dateFormat.parse(fechaAlta).getTime()));
                    } else {
                        datModif.setFechaAlta(null);
                    }
                    if (numLinea != null && !"".equals(numLinea)) {
                        datModif.setNumLinea(Integer.valueOf(numLinea));
                    } else {
                        datModif.setNumLinea(null);
                    }
                    datModif.setCausa(causa);
                    MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
                    boolean modOK = meLanbide58Manager.modificarAlta(datModif, con);
                    if (modOK) {
                        try {
                            lista = meLanbide58Manager.getDatosAltas(numExp, codOrganizacion, con);
                            for (AltaVO alta : lista) {
                                if (!alta.getCausaDesc().isEmpty() && alta.getCausaDesc() != null) {
                                    alta.setCausaDesc(getDescripcionDesplegable(request, alta.getCausaDesc()));
                                }
                            }
                        } catch (BDException bde) {
                            codigoOperacion = "1";
                            log.error("Error de tipo BD al recuperar la lista de Altas despues de Modificar un registro : " + bde.getMensaje());
                        } catch (Exception ex) {
                            codigoOperacion = "2";
                            log.error("Error al recuperar la lista de Altas despues de Modificar un registro : " + ex.getMessage());
                        }
                    } else {
                        codigoOperacion = "2";
                    }
                }
            } catch (Exception ex) {
                log.error("Error modificar --- ", ex);
                codigoOperacion = "2";
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAltas(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void generarExcelAnexoAltas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en generarExcelAnexoAltas de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;
        List<AltaVO> listaAltas = null;
        Connection con = null;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) {
                        idioma = usuario.getIdioma();
                    }
                }
            } catch (Exception ex) {

            }

            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            MeLanbide58Manager manager = MeLanbide58Manager.getInstance();
            String numExpe = "";
            String nif = request.getParameter("nif");
            String numLinea = request.getParameter("numLinea");
            String apellidos = request.getParameter("apellidos");
            try {
                numExpe = request.getParameter("numExp");
                try {
                    con = adapt.getConnection();
                    listaAltas = manager.getRegistrosAlta(numExpe, nif, numLinea, apellidos, codOrganizacion, con);
                    if (listaAltas.isEmpty()) {
                        meLanbide58I18n.getMensaje(idioma, "error.errorGen");
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos del Anexo A-altas");
                }

            } catch (Exception ex) {
                log.error("Error recuperando datos de Anexo A-altas : " + ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }

            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();

                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short) 200);
                int numFila = 0;
                //int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                HSSFSheet hoja = libro.createSheet("Datos Anexo A - Altas");

                //Se establece el ancho de cada columnas
                //hoja.setColumnWidth((short)0, (short)1500);//ID
                hoja.setColumnWidth(0, 3000);//Número de línea
                hoja.setColumnWidth(1, 8000);//Apellidos y nombre (1)
                hoja.setColumnWidth(2, 3000);//Fecha alta
                hoja.setColumnWidth(3, 3000);//NIF
                hoja.setColumnWidth(4, 3000);//Nº Afiliación a la seg. soc.
                hoja.setColumnWidth(5, 3000);

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);

                crearEstiloInformeDatosAnexoAltas(libro, fila, celda, estiloCelda, idioma);

                int p = 0;
                int n = 0;

                String numExp = null;

                estiloCelda = libro.createCellStyle();
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

                //Insertamos los datos, fila a fila
                for (AltaVO filaI : listaAltas) {
                    if (filaI.getNumExp() != null && !filaI.getNumExp().equals(numExp)) {
                        numExp = filaI.getNumExp();
                        p = 0;
                        n++;
                        numFila++;
                    } else {
                        numFila++;
                        p++;
                    }

                    fila = hoja.createRow(numFila);

                    //COLUMNA: NUMERO DE LINEA
                    celda = fila.createCell(0);
                    celda.setCellValue(filaI.getNumLinea() != null ? String.valueOf(filaI.getNumLinea()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: APELLIDOS Y NOMBRE
                    celda = fila.createCell(1);
                    celda.setCellValue((filaI.getApellidos() != null ? filaI.getApellidos().toUpperCase() : "") + ", " + (filaI.getNombre() != null ? filaI.getNombre().toUpperCase() : ""));
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA ALTA
                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getFechaAlta() != null ? dateFormat.format(filaI.getFechaAlta()) : "");
                    //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: NIF
                    celda = fila.createCell(3);
                    celda.setCellValue(filaI.getNif() != null ? filaI.getNif() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: Nº AFILIACION A LA SEG. SOC.
                    celda = fila.createCell(4);
                    celda.setCellValue(filaI.getNumSS() != null ? String.valueOf(filaI.getNumSS()) : "");
                    celda.setCellStyle(estiloCelda);

                    // CAUSA
                    celda = fila.createCell(5);
                    celda.setCellValue(filaI.getCausaDesc() != null ? String.valueOf(getDescripcionDesplegable(request, filaI.getCausaDesc())) : "");
                    celda.setCellStyle(estiloCelda);

                }

                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenDatosAnexoA_Altas", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream(istr); // promote

                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException ioe) {
                log.error("EXCEPCION informe resumenDatosAnexoA_Altas");
            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoA_Altas");
        } catch (SQLException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoA_Altas");
        }
    }

    private void crearEstiloInformeDatosAnexoAltas(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info("Entramos en crearEstiloInformeDatosAnexoAltas de " + this.getClass().getSimpleName());
        try {
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                palette.getColor(HSSFColor.GREY_50_PERCENT.index);

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras               
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);

                celda = fila.createCell(0);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "altas.tablaAltas.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(1);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "altas.tablaAltas.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(2);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "altas.tablaAltas.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(3);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "altas.tablaAltas.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(4);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "altas.tablaAltas.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(5);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "altas.tablaAltas.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);
            } catch (Exception e) {
            }

        } catch (Exception ex) {

        }

    }

    // ------------------  BAJAS  -------------------------------------------------------------------------------
    public String cargarNuevoBaja(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoBaja de " + this.getClass().getSimpleName());
        String nuevo = "1";
        String numExp = "";
        int numLinea = 0;
        AdaptadorSQLBD adapt = null;
        MeLanbide58Manager meLanbide58Manager = new MeLanbide58Manager();
        String url = "/jsp/extension/melanbide58/nuevoBaja.jsp?codOrganizacion=" + codOrganizacion;
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

            List<CausaAltaBajaVO> listaCausasBaja = MeLanbide58Manager.getInstance().getListBajas(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            if (listaCausasBaja != null) {
                for (CausaAltaBajaVO causaAl : listaCausasBaja) {
                    causaAl.setDes_nombre(getDescripcionDesplegable(request, causaAl.getDes_nombre()));
                }
                request.setAttribute("listaCausas", listaCausasBaja);
            }
            // recupero el último nº de linea en el anexo A-Altas
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            numLinea = meLanbide58Manager.getUltimoNumLinea(numExp, ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_BAJAS, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            log.debug("Último número de línea en el anexo A-Bajas: " + numLinea);
            request.setAttribute("numLineaA", String.valueOf(numLinea + 1));
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de un Nuevo baja : " + ex.getMessage());
        }
        return url;
    }

    public String cargarModificarBaja(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarBaja de " + this.getClass().getSimpleName());
        String nuevo = "0";
        String url = "/jsp/extension/melanbide58/nuevoBaja.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            // Recuperramos datos de baja a modificar y cargamos en el request
            if (id != null && !id.isEmpty()) {
                BajaVO datModif = MeLanbide58Manager.getInstance().getBajaPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            List<CausaAltaBajaVO> listaCausasBaja = MeLanbide58Manager.getInstance().getListBajas(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            if (listaCausasBaja != null) {
                for (CausaAltaBajaVO causaAl : listaCausasBaja) {
                    causaAl.setDes_nombre(getDescripcionDesplegable(request, causaAl.getDes_nombre()));
                }
                request.setAttribute("listaCausas", listaCausasBaja);
            }
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return url;
    }

    public void eliminarBaja(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarBaja de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<BajaVO> lista = new ArrayList<BajaVO>();
        String numExp = "";
        Connection con = null;
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.debug("No se ha recibido desde la JSP el id del baja a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                if (adapt != null) {
                    try {
                        con = adapt.getConnection();
                        int result = MeLanbide58Manager.getInstance().eliminarBaja(id, adapt);
                        if (result <= 0) {
                            codigoOperacion = "1";
                        } else {
                            codigoOperacion = "0";
                            try {
                                lista = MeLanbide58Manager.getInstance().getDatosBajas(numExp, codOrganizacion, con);
                                for (BajaVO baja : lista) {
                                    if (!baja.getCausaDesc().isEmpty() && baja.getCausaDesc() != null) {
                                        baja.setCausaDesc(getDescripcionDesplegable(request, baja.getCausaDesc()));
                                    }
                                }
                            } catch (Exception ex) {
                                log.error("Error al recuperar la lista de bajas despues de eliminar un registro");
                            }
                        }
                    } catch (Exception ex) {
                        log.error("Error eliminando un registro de baja", ex);
                    } finally {
                        try {
                            adapt.devolverConexion(con);
                        } catch (BDException e) {
                            log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            log.error("Error eliminando un registro de baja : " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBajas(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void buscarBaja(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en buscarBaja de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<BajaVO> lista = new ArrayList<BajaVO>();
        String numExp = "";
        Connection con = null;
        try {
            String nif = request.getParameter("nifB");
            String numLinea = request.getParameter("numLineaB");
            String apellidos = request.getParameter("apellidosB");
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            if (adapt != null) {
                try {
                    con = adapt.getConnection();
                    lista = MeLanbide58Manager.getInstance().getRegistrosBaja(numExp, nif, numLinea, apellidos, codOrganizacion, con);
                    if (lista.isEmpty()) {
                        codigoOperacion = "4";
                    }
                } catch (Exception ex) {
                    codigoOperacion = "1";
                    log.error("Error al recuperar datos de baja :", ex);
                } finally {
                    try {
                        adapt.devolverConexion(con);
                    } catch (BDException e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }

        } catch (SQLException ex) {
            log.debug("Error buscando registros de baja : " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBajas(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void crearNuevoBaja(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoBaja de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<BajaVO> lista = new ArrayList<BajaVO>();
        BajaVO nuevoVO = new BajaVO();
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE58.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (adapt != null) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                //Recojo los parametros
                String nombre = request.getParameter("nombre");
                String apellidos = request.getParameter("apellidos");
                String nif = request.getParameter("nif");
                String fechaBaja = request.getParameter("fechaBaja");
                String numSS = request.getParameter("numSS");
                String numLinea = request.getParameter("numLinea");
                String numExp = request.getParameter("expediente");
                String causa = request.getParameter("causa");

                nuevoVO.setNumExp(numExp);
                nuevoVO.setNombre(nombre);
                nuevoVO.setApellidos(apellidos);
                nuevoVO.setNif(nif);
                if (fechaBaja != null && !"".equals(fechaBaja)) {
                    nuevoVO.setFechaBaja(new java.sql.Date(dateFormat.parse(fechaBaja).getTime()));
                }
                nuevoVO.setNumSS(numSS);
                if (numLinea != null && !"".equals(numLinea)) {
                    nuevoVO.setNumLinea(Integer.valueOf(numLinea));
                }

                nuevoVO.setCausa(causa);

                MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
                boolean insertOK = meLanbide58Manager.crearNuevoBaja(nuevoVO, adapt);
                if (insertOK) {
                    log.debug("Registro de baja Insertado Correctamente");
                    lista = meLanbide58Manager.getDatosBajas(numExp, codOrganizacion, con);
                    for (BajaVO baja : lista) {
                        if (!baja.getCausaDesc().isEmpty() && baja.getCausaDesc() != null) {
                            baja.setCausaDesc(getDescripcionDesplegable(request, baja.getCausaDesc()));
                        }
                    }
                } else {
                    log.error("NO se ha insertado correctamente el nuevo registro de baja");
                    codigoOperacion = "1";
                }
            } catch (Exception ex) {
                log.error("Error al parsear los parametros recibidos del jsp al objeto BajaVO" + ex.getMessage());
                codigoOperacion = "2";
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBajas(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarBaja(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarBaja de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<BajaVO> lista = new ArrayList<BajaVO>();
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE58.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (adapt != null) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                //Recojo los parametros
                String id = request.getParameter("id");
                String nombre = request.getParameter("nombre");
                String apellidos = request.getParameter("apellidos");
                String nif = request.getParameter("nif");
                String fechaBaja = request.getParameter("fechaBaja");
                String numSS = request.getParameter("numSS");
                String numLinea = request.getParameter("numLinea");
                String numExp = request.getParameter("expediente");
                String causa = request.getParameter("causa");

                if (id == null || id.isEmpty()) {
                    log.debug("No se ha recibido desde la JSP el id del registro Baja a Modificar ");
                    codigoOperacion = "3";
                } else {
                    BajaVO datModif = new BajaVO();
                    datModif.setId(Integer.valueOf(id));
                    datModif.setNombre(nombre);
                    datModif.setApellidos(apellidos);
                    datModif.setNif(nif);
                    datModif.setNumSS(numSS);
                    if (fechaBaja != null && !"".equals(fechaBaja)) {
                        datModif.setFechaBaja(new java.sql.Date(dateFormat.parse(fechaBaja).getTime()));
                    } else {
                        datModif.setFechaBaja(null);
                    }
                    if (numLinea != null && !"".equals(numLinea)) {
                        datModif.setNumLinea(Integer.valueOf(numLinea));
                    } else {
                        datModif.setNumLinea(null);
                    }
                    datModif.setCausa(causa);

                    MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
                    boolean modOK = meLanbide58Manager.modificarBaja(datModif, adapt);
                    if (modOK) {
                        try {
                            lista = meLanbide58Manager.getDatosBajas(numExp, codOrganizacion, con);
                            for (BajaVO baja : lista) {
                                if (!baja.getCausaDesc().isEmpty() && baja.getCausaDesc() != null) {
                                    baja.setCausaDesc(getDescripcionDesplegable(request, baja.getCausaDesc()));
                                }
                            }
                        } catch (BDException bde) {
                            codigoOperacion = "1";
                            log.error("Error de tipo BD al recuperar la lista de Bajas despues de Modificar un registro : " + bde.getMensaje());
                        } catch (Exception ex) {
                            codigoOperacion = "2";
                            log.error("Error al recuperar la lista de Bajas despues de Modificar un registro : " + ex.getMessage());
                        }
                    } else {
                        codigoOperacion = "2";
                    }
                }
            } catch (Exception ex) {
                log.error("Error modificar --- ", ex);
                codigoOperacion = "2";
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaBajas(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void generarExcelAnexoAbajas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en generarExcelAnexoAbajas de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;
        List<BajaVO> listaBajas = null;
        Connection con = null;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) {
                        idioma = usuario.getIdioma();
                    }
                }
            } catch (Exception ex) {

            }

            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            MeLanbide58Manager manager = MeLanbide58Manager.getInstance();
            String numExpe = "";
            String nif = request.getParameter("nifB");
            String numLinea = request.getParameter("numLineaB");
            String apellidos = request.getParameter("apellidosB");
            try {
                numExpe = request.getParameter("numExp");
                try {
                    con = adapt.getConnection();
                    listaBajas = manager.getRegistrosBaja(numExpe, nif, numLinea, apellidos, codOrganizacion, con);
                    if (listaBajas.isEmpty()) {
                        meLanbide58I18n.getMensaje(idioma, "error.errorGen");
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos del Anexo A-bajas");
                }

            } catch (Exception ex) {
                log.error("Error recuperando datos de Anexo A-bajas : " + ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }

            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();

                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short) 200);
                int numFila = 0;
                //int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                HSSFSheet hoja = libro.createSheet("Datos Anexo A - Bajas");

                //Se establece el ancho de cada columnas
                //hoja.setColumnWidth((short)0, (short)1500);//ID
                hoja.setColumnWidth(0, 3000);//Número de línea
                hoja.setColumnWidth(1, 8000);//Apellidos y nombre (1)
                hoja.setColumnWidth(2, 3000);//Fecha baja
                hoja.setColumnWidth(3, 3000);//NIF
                hoja.setColumnWidth(4, 3000);//Nº Afiliación a la seg. soc.
                hoja.setColumnWidth(5, 3000);

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);

                crearEstiloInformeDatosAnexoAbajas(libro, fila, celda, estiloCelda, idioma);

                int p = 0;
                int n = 0;

                String numExp = null;

                estiloCelda = libro.createCellStyle();
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

                //Insertamos los datos, fila a fila
                for (BajaVO filaI : listaBajas) {
                    if (filaI.getNumExp() != null && !filaI.getNumExp().equals(numExp)) {
                        numExp = filaI.getNumExp();
                        p = 0;
                        n++;
                        numFila++;
                    } else {
                        numFila++;
                        p++;
                    }

                    fila = hoja.createRow(numFila);

                    //COLUMNA: NUMERO DE LINEA
                    celda = fila.createCell(0);
                    celda.setCellValue(filaI.getNumLinea() != null ? String.valueOf(filaI.getNumLinea()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: APELLIDOS Y NOMBRE
                    celda = fila.createCell(1);
                    celda.setCellValue((filaI.getApellidos() != null ? filaI.getApellidos().toUpperCase() : "") + ", " + (filaI.getNombre() != null ? filaI.getNombre().toUpperCase() : ""));
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA BAJA
                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getFechaBaja() != null ? dateFormat.format(filaI.getFechaBaja()) : "");
                    //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: NIF
                    celda = fila.createCell(3);
                    celda.setCellValue(filaI.getNif() != null ? filaI.getNif() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: Nº AFILIACION A LA SEG. SOC.
                    celda = fila.createCell(4);
                    celda.setCellValue(filaI.getNumSS() != null ? String.valueOf(filaI.getNumSS()) : "");
                    celda.setCellStyle(estiloCelda);

                    // CAUSA
                    celda = fila.createCell(5);
                    celda.setCellValue(filaI.getCausaDesc() != null ? String.valueOf(getDescripcionDesplegable(request, filaI.getCausaDesc())) : "");
                    celda.setCellStyle(estiloCelda);
                }

                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenDatosAnexoA_Bajas", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream(istr); // promote

                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException ioe) {
                log.error("EXCEPCION informe resumenDatosAnexoA_Bajas");
            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("IOEXCEPCION informe resumenDatosAnexoA_Bajas");
        } catch (SQLException ex) {
            log.error("SQLEXCEPCION informe resumenDatosAnexoA_Bajas");
        }
    }

    private void crearEstiloInformeDatosAnexoAbajas(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info("Entramos en crearEstiloInformeDatosAnexoAbajas de " + this.getClass().getSimpleName());
        try {
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                palette.getColor(HSSFColor.GREY_50_PERCENT.index);

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras             
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);

                celda = fila.createCell(0);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "bajas.tablaBajas.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(1);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "bajas.tablaBajas.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(2);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "bajas.tablaBajas.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(3);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "bajas.tablaBajas.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(4);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "bajas.tablaBajas.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                celda = fila.createCell(5);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "altas.tablaBajas.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);
            } catch (Exception e) {
            }

        } catch (Exception ex) {
        }

    }

    // ----------------------------------------------------------------------------------------------------------
    // --------------------------  ANEXO B -  SMI    ------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
    public String cargarPantallaAnexoB(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaAnexoB de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        MeLanbide58Manager meLanbide58Manager = new MeLanbide58Manager();
        ArrayList totalS = new ArrayList();
        ArrayList smiMesDia = new ArrayList();
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide58/anexoB.jsp";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                smiMesDia = meLanbide58Manager.getSmiMesDia(numExpediente, codOrganizacion, adapt);
                if (!smiMesDia.isEmpty()) {
                    log.debug("Recuperando el Salario Mínimo Interprofesional(SMI) OK");
                    request.setAttribute("smiMes", smiMesDia.get(0).toString());
                    request.setAttribute("smiDia", smiMesDia.get(1).toString());
                } else {
                    request.setAttribute("smiMes", "0");
                    request.setAttribute("smiDia", "0");
                    log.error(this.getClass().getName() + " No se puede recalcular la subvención porque el Salario Mínimo Interprofesional para el año solicitado no está dado de alta en el sistema.");
                }

                totalS = meLanbide58Manager.getTotalSubvencion(numExpediente, adapt);
                if (!totalS.isEmpty()) {
                    log.debug("Recuperando el Total de la Subvención OK");
                    request.setAttribute("importe", totalS.get(0).toString());
                    request.setAttribute("importeConcedido", totalS.get(1).toString());
               //     request.setAttribute("importeCopiaLanbide", totalS.get(2).toString());
                } else {
                    request.setAttribute("importe", "0");
                    request.setAttribute("importeConcedido", "0");
               //     request.setAttribute("importeCopiaLanbide", "0");
                    log.error(this.getClass().getName() + " Error al recuperar el Total de la Subvención");
                }

                List<SMIVO> listaSMI = MeLanbide58Manager.getInstance().getDatosSMI(numExpediente, codOrganizacion, adapt);
                if (!listaSMI.isEmpty()) {
                    request.setAttribute("listaSMI", listaSMI);
                }

            } catch (Exception ex) {
                log.error("Error al recueperar los datos De SMI - MELANBIDE58 - cargarPantallaAnexoB", ex);
            }
        }
        return url;
    }

    private void cargarDesplegablesAnexoB(int codOrganizacion, HttpServletRequest request) throws Exception {
        List<DesplegableAdmonLocalVO> listaCausaIncidencia = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_CAUSA_INCIDENCIA, ConstantesMeLanbide58.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaCausaIncidencia.isEmpty()) {
            listaCausaIncidencia = traducirDesplegable(request, listaCausaIncidencia);
            request.setAttribute("listaCausaIncidencia", listaCausaIncidencia);
        }

    }

    public String cargarNuevoSMI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String smiDia = "";
        int numLinea = 0;
        String urlnuevoSM = "/jsp/extension/melanbide58/nuevoSMI.jsp?codOrganizacion=" + codOrganizacion;
        AdaptadorSQLBD adapt = null;
        MeLanbide58Manager meLanbide58Manager = new MeLanbide58Manager();
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
            if (request.getParameter("smiDia") != null) {
                smiDia = request.getParameter("smiDia");
                request.setAttribute("smiDia", smiDia);
            }
            // recupero el último nº de linea en el anexo B
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            numLinea = meLanbide58Manager.getUltimoNumLinea(numExp, ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_SMI, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            log.debug("Último número de línea en el anexo B: " + numLinea);
            request.setAttribute("numLineaS", String.valueOf(numLinea + 1));
            //Cargamos el el request los valores  de los desplegables
            cargarDesplegablesAnexoB(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de un Nuevo smi : " + ex.getMessage());
        }
        return urlnuevoSM;
    }

    public String cargarModificarSMI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarSMI de " + this.getClass().getSimpleName());
        String nuevo = "0";
        String url = "/jsp/extension/melanbide58/nuevoSMI.jsp?codOrganizacion=" + codOrganizacion;
        String smiDia = "";
        String numLinea = request.getParameter("numLineaSMI");
        String apellidos = request.getParameter("apellidosSMI");

        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            // Recuperramos datos de smi a modificar y cargamos en el request
            if (id != null && !id.isEmpty()) {
                SMIVO datModif = MeLanbide58Manager.getInstance().getSMIPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            if (request.getParameter("smiDia") != null) {
                smiDia = request.getParameter("smiDia");
                request.setAttribute("smiDia", smiDia);
            }

            request.setAttribute("apellidosS", apellidos);
            request.setAttribute("numLineaS", numLinea);

            //Cargamos el el request los valores  de los desplegables
            cargarDesplegablesAnexoB(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return url;
    }

    public void eliminarSMI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarSMI de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<SMIVO> lista = new ArrayList<SMIVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del smi a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide58Manager.getInstance().eliminarSMI(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide58Manager.getInstance().getDatosSMI(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.error("Error al recuperar la lista de smi despues de eliminar un registro");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error eliminando un registro de SMI : " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaSMI(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);

    }

    public void buscarSMI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en buscarSMI de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<SMIVO> lista = new ArrayList<SMIVO>();
        String numExp = "";
        String numLinea = request.getParameter("numLineaSMI");
        String apellidos = request.getParameter("apellidosSMI");
        String nif = request.getParameter("nif");

        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try {
                lista = MeLanbide58Manager.getInstance().getRegistrosAnexoB(numExp, numLinea, apellidos, nif, adapt);
                if (lista.isEmpty()) {
                    codigoOperacion = "1";
                }
            } catch (Exception ex) {
                log.error("Error al recuperar datos de SMI");
            }

        } catch (SQLException ex) {
            log.error("Error recuperando datos de SMI : " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaSMI(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void buscarSubImpInc(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en buscarSubImpInc de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<SMIVO> lista = new ArrayList<SMIVO>();
        String numExp = "";
        String numLinea = request.getParameter("numLineaSMI");
        String apellidos = request.getParameter("apellidosSMI");
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try {
                lista = MeLanbide58Manager.getInstance().getRegistrosSubImpInc(numExp, numLinea, apellidos, adapt);
                if (lista.isEmpty()) {
                    codigoOperacion = "1";
                }
            } catch (Exception ex) {
                log.error("Error al recuperar datos de SMI");
            }

        } catch (SQLException ex) {
            log.error("Error recuperando datos de SMI : " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaSMI(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    /**
     * Operación que inserta un nuevo registro en el Anexo B
     *
     * "0" --> Si la operación se ha ejecutado correctamente
     *
     * "1" --> NO se ha insertado correctamente el nuevo registro de smi
     *
     * "2" --> Error al parsear los parametros recibidos del jsp al objeto SMIVO
     *
     * "3" -->
     *
     * "4" -->
     *
     * "5" --> error.recalculoSMI
     *
     * "6" --> error.notFoundSMI
     *
     * "7" --> error.actualizarRecalculoSMI
     *
     * "8" --> la persona no existe en anexo C
     *
     * "9" --> no coinciden los porcentajes de jornada
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void crearNuevoSMI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entra en crearNuevoSMI de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<SMIVO> lista = new ArrayList<SMIVO>();
        SMIVO nuevoSMI = new SMIVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String numLinea = request.getParameter("numLinea");
            String nombre = request.getParameter("nombre");
            String dni = request.getParameter("dni");
            String apellidos = request.getParameter("apellidos");
            String codCausaIncidencia = request.getParameter("codCausaIncidencia");
            String numDiasSinIncidencias = request.getParameter("numDiasSinIncidencias").replace(",", ".");
            String numDiasIncidencia = request.getParameter("numDiasIncidencia").replace(",", ".");
            String importeSubvencion = request.getParameter("importeSubvencion").replace(",", ".");
            String fecha = request.getParameter("fecha");
            String porcJornada = request.getParameter("porcJornada").replace(",", ".");
            String porcReduccion = request.getParameter("porcReduccion").replace(",", ".");
            String observaciones = request.getParameter("observaciones");
            String numExp = request.getParameter("expediente");

            log.debug("linea: " + numLinea);
            log.debug("DNI: " + dni);
            log.debug("Nombre + Apellidos: " + nombre + " - " + apellidos);
            log.debug("Causa: " + codCausaIncidencia);
            log.debug("Dias SIN: " + numDiasSinIncidencias);
            log.debug("Dias Incidencia: " + numDiasIncidencia);
            log.debug("Importe: " + importeSubvencion);
            log.debug("Fecha: " + fecha);
            log.debug("% JORNADA: " + porcJornada);
            log.debug("% reduccion: " + porcReduccion);
            log.debug("Exp: " + numExp);

            nuevoSMI.setNumExp(numExp);
            if (numLinea != null && !"".equals(numLinea)) {
                nuevoSMI.setNumLinea(Integer.valueOf(numLinea));
            }
            nuevoSMI.setNif(dni);
            nuevoSMI.setNombre(nombre);
            nuevoSMI.setApellidos(apellidos);
            nuevoSMI.setCausaIncidencia(codCausaIncidencia);
            if (numDiasSinIncidencias != null && !"".equals(numDiasSinIncidencias)) {
                nuevoSMI.setNumDiasSinIncidencias(Double.valueOf(numDiasSinIncidencias));
            }
            if (numDiasIncidencia != null && !"".equals(numDiasIncidencia)) {
                nuevoSMI.setNumDiasIncidencia(Double.valueOf(numDiasIncidencia));
            }
            if (importeSubvencion != null && !"".equals(importeSubvencion)) {
                nuevoSMI.setImporteSolicitado(Double.valueOf(importeSubvencion));
            }
            if (fecha != null && !"".equals(fecha)) {
                nuevoSMI.setFecha(new java.sql.Date(dateFormat.parse(fecha).getTime()));
            }
            if (porcJornada != null && !"".equals(porcJornada)) {
                nuevoSMI.setPorcJornada(Double.valueOf(porcJornada));
            }
            if (porcReduccion != null && !"".equals(porcReduccion)) {
                nuevoSMI.setPorcReduccion(Double.valueOf(porcReduccion));
            }

            nuevoSMI.setObservaciones(observaciones);

            MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
            // antes de grabar comprobar dni y si tiene % jornada en B que sea como en C
            /*   boolean existe = false;
            boolean coincide = false;
            if (dni.equalsIgnoreCase("88888888") || dni.equalsIgnoreCase("99999999")) {
                existe = true;
                coincide = true;
            } else {
                existe = meLanbide58Manager.existeAnexoC(numExp, dni, adapt);
                if (existe) {
                    coincide = meLanbide58Manager.coincideJornada(numExp, dni, nuevoSMI.getPorcJornada(), adapt);
                    if (!coincide){
                        log.error("No coinciden los porcentajes de jornada");
                        codigoOperacion = "9";
                    }
                } else {
                    log.error("La persona con documento " + dni + " no existe en el Anexo C");
                    codigoOperacion = "8";
                }
            }
            if (existe && coincide) {*/
            Integer idNuevoSMI = meLanbide58Manager.crearNuevoSMI(nuevoSMI, adapt);
            if (idNuevoSMI != null) {

                // si no tiene número de linea busco el padre y se recalcula el padre
                log.debug("NUMLINEA: " + numLinea);
                if (numLinea == null || "".equals(numLinea)) {
                    log.debug("NUMLINEA: NULL");
                    Integer numLineaPadre = MeLanbide58Manager.getInstance().getLineaPadreBporDNI(numExp, dni, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    numLinea = numLineaPadre.toString();
                    log.debug("NUMLINEA PADRE: " + numLinea);
                }

                ////////////////////
                String mensajeRecalculo = meLanbide58Manager.recalcularSMI(numExp, numLinea, adapt);
                /*if(mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.ANIO_VACIO)){
                codigoOperacion = "4";
            } else */
                if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.ERROR_RECALCULO)) {
                    codigoOperacion = "5";
                } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.NOT_FOUND_SMI)) {
                    codigoOperacion = "6";
                } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.UPDATE_RECALCULO)) {
                    codigoOperacion = "7";
                }

                //  grabar importe total 
                grabarImporteResolCEESC(codOrganizacion, codTramite, ocurrenciaTramite, numExp);

                log.debug("Registro de smi Insertado Correctamente");
                lista = meLanbide58Manager.getDatosSMI(numExp, codOrganizacion, adapt);

            } else {
                log.error("NO se ha insertado correctamente el nuevo registro de smi");
                codigoOperacion = "1";
            }
//            }

        } catch (Exception ex) {
            log.error("Error al parsear los parametros recibidos del jsp al objeto SMIVO" + ex.getMessage());
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaSMI(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarSMI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarSMI de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<SMIVO> lista = new ArrayList<SMIVO>();

        SMIVO datModif = new SMIVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String numLinea = request.getParameter("numLinea");
            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String codCausaIncidencia = request.getParameter("codCausaIncidencia");
            String numDiasSinIncidencias = request.getParameter("numDiasSinIncidencias").replace(",", ".");
            String numDiasIncidencia = request.getParameter("numDiasIncidencia").replace(",", ".");
            String importeSubvencion = request.getParameter("importeSubvencion").replace(",", ".");
            String fecha = request.getParameter("fecha");
            String porcJornada = request.getParameter("porcJornada").replace(",", ".");
            String porcReduccion = request.getParameter("porcReduccion").replace(",", ".");
            String observaciones = request.getParameter("observaciones");
            String numExp = request.getParameter("expediente");
            String porcOriginal = request.getParameter("porcOriginal");

            log.debug("linea: " + numLinea);
            log.debug("DNI: " + dni);
            log.debug("Nombre + Apellidos: " + nombre + " - " + apellidos);
            log.debug("Causa: " + codCausaIncidencia);
            log.debug("Dias SIN: " + numDiasSinIncidencias);
            log.debug("Dias Incidencia: " + numDiasIncidencia);
            log.debug("Importe: " + importeSubvencion);
            log.debug("Fecha: " + fecha);
            log.debug("% JORNADA: " + porcJornada);
            log.debug("% reduccion: " + porcReduccion);
            log.debug("Exp: " + numExp);
            log.debug("% original: " + porcOriginal);
            if (id == null && "".equals(id)) {
                log.error("No se ha recibido desde la JSP el id del registro smi a Modificar ");
                codigoOperacion = "3";
            } else {
                datModif.setId(Integer.valueOf(id));
                if (numLinea != null && !"".equals(numLinea)) {
                    datModif.setNumLinea(Integer.valueOf(numLinea));
                } else {
                    datModif.setNumLinea(null);
                }
                datModif.setNif(dni);
                datModif.setNombre(nombre);
                datModif.setApellidos(apellidos);
                datModif.setCausaIncidencia(codCausaIncidencia);
                if (numDiasSinIncidencias != null && !"".equals(numDiasSinIncidencias)) {
                    datModif.setNumDiasSinIncidencias(Double.valueOf(numDiasSinIncidencias));
                } else {
                    datModif.setNumDiasSinIncidencias(null);
                }
                if (numDiasIncidencia != null && !"".equals(numDiasIncidencia)) {
                    datModif.setNumDiasIncidencia(Double.valueOf(numDiasIncidencia));
                } else {
                    datModif.setNumDiasIncidencia(null);
                }
                if (importeSubvencion != null && !"".equals(importeSubvencion)) {
                    datModif.setImporteSolicitado(Double.valueOf(importeSubvencion));
                } else {
                    datModif.setImporteSolicitado(null);
                }
                if (fecha != null && !"".equals(fecha)) {
                    datModif.setFecha(new java.sql.Date(dateFormat.parse(fecha).getTime()));
                } else {
                    datModif.setFecha(null);
                }
                if (porcJornada != null && !"".equals(porcJornada)) {
                    datModif.setPorcJornada(Double.valueOf(porcJornada));
                } else {
                    datModif.setPorcJornada(null);
                }
                if (porcReduccion != null && !"".equals(porcReduccion)) {
                    datModif.setPorcReduccion(Double.valueOf(porcReduccion));
                } else {
                    datModif.setPorcReduccion(null);
                }
                datModif.setObservaciones(observaciones);

                MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
                boolean modOK = meLanbide58Manager.modificarSMI(datModif, adapt);

                if (modOK) {
                    boolean esNueva = false;
                    boolean faltanDatos = false;
                    String nif = null;
                    nif = meLanbide58Manager.getNifSMIPorId(numExp, id, adapt);
                    if (!nif.equalsIgnoreCase("88888888") && !nif.equalsIgnoreCase("99999999")) {
                        if (porcJornada != null && !"".equals(porcJornada)) {
                            boolean modCOK = meLanbide58Manager.actualizaJornadaC(numExp, nif, datModif.getPorcJornada(), porcOriginal, adapt);
                        }
                        // COMPROBAR SI ES NUEVA ALTA Y TIENE DATOS COMPLETOS
                        esNueva = meLanbide58Manager.esNuevaAlta(numExp, nif, adapt);
                        faltanDatos = meLanbide58Manager.faltanDatos(numExp, nif, adapt);
                    }
                    try {

                        if (esNueva && faltanDatos) {
                            log.info("La persona con DNI: " + nif + " es nuevo ALTA y Faltan datos. No se realiza el recalculo");
                        } else {
                            // si el numero de linea esta en blanco se busca la linea padre
                            if (numLinea.trim().isEmpty()) {
                                numLinea = String.valueOf(meLanbide58Manager.getLineaPadrePorID(numExp, id, adapt));
                            }
                            log.debug("Nº linea: " + numLinea);
                            if (numLinea != null && !numLinea.equalsIgnoreCase("0") && !numLinea.isEmpty()) {
                                String mensajeRecalculo = meLanbide58Manager.recalcularSMI(numExp, numLinea, adapt);
                                if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.ANIO_VACIO)) {
                                    codigoOperacion = "4";
                                } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.ERROR_RECALCULO)) {
                                    codigoOperacion = "5";
                                } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.NOT_FOUND_SMI)) {
                                    codigoOperacion = "6";
                                } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.UPDATE_RECALCULO)) {
                                    codigoOperacion = "7";
                                }
                            }
                            //  grabar importe total 
                            grabarImporteResolCEESC(codOrganizacion, codTramite, ocurrenciaTramite, numExp);
                        }

                        //getDatosSMI => obtiene todos los registros para un expediente
                        lista = meLanbide58Manager.getDatosSMI(numExp, codOrganizacion, adapt);

                        /*getRegistrosAnexoB => obtiene todos los registros teniendo en cuenta el filtro
                        Añadido en el modificado de SMI para que cuando vuelva mantenga listados los registros
                        que se filtraron*/
                        //lista = meLanbide58Manager.getRegistrosAnexoB(numExp, numLineaS, apellidosS, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de smi despues de Modificar un registro : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de smi despues de Modificar un registro : " + ex.getMessage());
                    }

                } else {
                    codigoOperacion = "2";
                }
            }
        } catch (Exception ex) {
            log.error("Error modificar --- ", ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaSMI(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void copiaImportesSMI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en copiaImportesSMI de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<SMIVO> lista = new ArrayList<SMIVO>();

        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros            
            String numExp = request.getParameter("numExp");
            log.info("Expediente: " + numExp);
            MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
            boolean copiaOK = meLanbide58Manager.copiaImportesSMI(numExp, adapt);
            if (copiaOK) {
                log.debug("copiado de importes correcta");
                //  grabar importe total 
                grabarImporteResolCEESC(codOrganizacion, codTramite, ocurrenciaTramite, numExp);
                lista = meLanbide58Manager.getDatosSMI(numExp, codOrganizacion, adapt);

            } else {
                log.error("NO se han copiado correctamente los importes");
                codigoOperacion = "1";
            }

        } catch (Exception ex) {
            log.error("Error al parsear los parametros recibidos del jsp" + ex.getMessage());
            codigoOperacion = "1";
        }

        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaSMI(request, codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void grabarImporteResolCEESC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        log.info(">>>> ENTRA en grabarImporteResolCEESC ");
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide58Manager meLanbide58Manager = new MeLanbide58Manager();
            ArrayList totalS = new ArrayList();
            String importeFinal;
            totalS = meLanbide58Manager.getTotalSubvencion(numExpediente, adapt);
            if (!totalS.isEmpty()) {
                log.debug("Recuperando el Total de la Subvención OK");
                importeFinal = totalS.get(1).toString();
            } else {
                importeFinal = "0";
                log.error(this.getClass().getSimpleName() + " Error al recuperar el Total de la Subvención");
            }

            //Una vez recuperado el dato lo grabamso en el campo del expediente
            log.debug("importeFinal: " + importeFinal);
            meLanbide58Manager.grabarImporteResolCEESC(codOrganizacion, numExpediente, adapt, codTramite, importeFinal);
        } catch (Exception ex) {
            log.error("*** AdaptadorSQLBD: " + ex.toString());
        }
        log.debug("grabarImporteResolCEESC END");
    }

    public void generarExcelAnexoB(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en generarExcellSMI de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;
        List<SMIVO> listaSMI = null;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) {
                        idioma = usuario.getIdioma();
                    }
                }
            } catch (Exception ex) {
            }
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            MeLanbide58Manager manager = MeLanbide58Manager.getInstance();
            String numExpe = "";
            String numLinea = request.getParameter("numLineaSMI");
            String apellidos = request.getParameter("apellidosSMI");
            String nif = request.getParameter("nif");
            Double totalSolicitado = 0.00;
            Double totalConcedido = 0.00;
            try {
                numExpe = request.getParameter("numExp");
                try {
                    listaSMI = manager.getRegistrosAnexoB(numExpe, numLinea, apellidos, nif, adapt);
                    if (listaSMI.isEmpty()) {
                        meLanbide58I18n.getMensaje(idioma, "error.errorGen");
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos de SMI");
                }

            } catch (Exception ex) {
                log.error("Error recuperando datos de SMI : " + ex);
            }

            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();

                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont rojo = libro.createFont();
                rojo.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                rojo.setFontHeight((short) 150);
                rojo.setColor(HSSFColor.RED.index);

                HSSFFont azul = libro.createFont();
                azul.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                azul.setFontHeight((short) 150);
                azul.setColor(HSSFColor.BLUE.index);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short) 200);
                int numFila = 0;
                //int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;
                HSSFCellStyle estiloFilaRoja = null;
                HSSFCellStyle estiloFilaAzul = null;
                HSSFCellStyle estiloFilaNormal = null;
                HSSFSheet hoja = libro.createSheet("Datos Anexo B - SMI");

                //Se establece el ancho de cada columnas
                //hoja.setColumnWidth((short)0, (short)1500);//ID
                hoja.setColumnWidth(0, 3000);//Número de línea
                hoja.setColumnWidth(1, 3000); // dni
                hoja.setColumnWidth(2, 8000);//Apellidos y nombre (1)
                hoja.setColumnWidth(3, 4750);//Número de días sin incidencias
                hoja.setColumnWidth(4, 4000);//Incidencia (número de días)
                hoja.setColumnWidth(5, 3000);//Incidencia (causa)
                hoja.setColumnWidth(6, 3000);//Fecha
                hoja.setColumnWidth(7, 3000);//Porcentaje jornada laboral
                hoja.setColumnWidth(8, 3000);//Porcentaje reducción jornada laboral
                hoja.setColumnWidth(9, 3500);//Importe de la subvención
                hoja.setColumnWidth(10, 5000);//Importe de la subvención calculado

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);

                crearEstiloInformeDatosSMI(libro, fila, celda, estiloCelda, idioma);

                int p = 0;
                int n = 0;

                String numExp = null;

                // definir 3 estilos azul rojo negro
                estiloFilaRoja = libro.createCellStyle();
                estiloFilaRoja.setWrapText(true);
                estiloFilaRoja.setFont(rojo);
                estiloFilaRoja.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloFilaRoja.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloFilaRoja.setBorderTop(HSSFCellStyle.BORDER_THIN);

                estiloFilaAzul = libro.createCellStyle();
                estiloFilaAzul.setWrapText(true);
                estiloFilaAzul.setFont(azul);
                estiloFilaAzul.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                estiloFilaAzul.setBorderTop(HSSFCellStyle.BORDER_THICK);

                estiloFilaNormal = libro.createCellStyle();
                estiloFilaNormal.setWrapText(true);
                estiloFilaNormal.setFont(normal);
                estiloFilaNormal.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloFilaNormal.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloFilaNormal.setBorderTop(HSSFCellStyle.BORDER_THIN);

                //Insertamos los datos, fila a fila
                for (int i = 0; i < listaSMI.size(); i++) {
                    if (listaSMI.get(i).getNumExp() != null && !listaSMI.get(i).getNumExp().equals(numExp)) {
                        numExp = listaSMI.get(i).getNumExp();
                        p = 0;
                        n++;
                        numFila++;
                    } else {
                        numFila++;
                        p++;
                    }

                    fila = hoja.createRow(numFila);

                    boolean padreRojo = false;
                    int linea = i + 1;
                    //Miramos si la linea es padre 
                    if (linea < listaSMI.size() && listaSMI.get(i).getNumLinea() != null && listaSMI.get(linea).getNumLinea() == null) {
                        //Miramos si algún hijo está em rojo
                        while (linea < listaSMI.size() && listaSMI.get(linea).getNumLinea() == null) {
                            if ((listaSMI.get(linea).getImporteSolicitado() != null && !listaSMI.get(linea).getImporteSolicitado().equals(listaSMI.get(linea).getImporteRecalculo()))
                                    || (listaSMI.get(linea).getImporteSolicitado() == null && listaSMI.get(linea).getImporteRecalculo() != null)) {
                                padreRojo = true;
                            }
                            linea++;
                        }
                    }
                    if ((listaSMI.get(i).getImporteSolicitado() != null && !listaSMI.get(i).getImporteSolicitado().equals(listaSMI.get(i).getImporteRecalculo()))
                            || (listaSMI.get(i).getImporteSolicitado() == null && listaSMI.get(i).getImporteRecalculo() != null) || padreRojo) {
                        estiloCelda = estiloFilaRoja;
                    } else {
                        estiloCelda = estiloFilaNormal;
                    }
                    //COLUMNA: NUMERO DE LINEA
                    celda = fila.createCell(0);
                    celda.setCellValue(listaSMI.get(i).getNumLinea() != null ? String.valueOf(listaSMI.get(i).getNumLinea()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: DNI
                    celda = fila.createCell(1);
                    celda.setCellValue(listaSMI.get(i).getNif());
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: APELLIDOS Y NOMBRE
                    celda = fila.createCell(2);
                    celda.setCellValue((listaSMI.get(i).getApellidos() != null ? listaSMI.get(i).getApellidos().toUpperCase() : "") + ", " + (listaSMI.get(i).getNombre() != null ? listaSMI.get(i).getNombre().toUpperCase() : ""));
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: NUMERO DE DIAS SIN INCIDENCIAS
                    celda = fila.createCell(3);
                    celda.setCellValue(listaSMI.get(i).getNumDiasSinIncidencias() != null ? String.valueOf(listaSMI.get(i).getNumDiasSinIncidencias()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: INCIDENCIA (NUMERO DE DIAS)
                    celda = fila.createCell(4);
                    celda.setCellValue(listaSMI.get(i).getNumDiasIncidencia() != null ? String.valueOf(listaSMI.get(i).getNumDiasIncidencia()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: INCIDENCIA (CAUSA)
                    celda = fila.createCell(5);
                    celda.setCellValue(listaSMI.get(i).getDesCausaIncidencia() != null ? String.valueOf(getDescripcionDesplegable(request, listaSMI.get(i).getDesCausaIncidencia())) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA
                    celda = fila.createCell(6);
                    celda.setCellValue(listaSMI.get(i).getFecha() != null ? dateFormat.format(listaSMI.get(i).getFecha()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: PORCENTAJE JORNADA LABORAL
                    celda = fila.createCell(7);
                    celda.setCellValue(listaSMI.get(i).getPorcJornada() != null ? String.valueOf(listaSMI.get(i).getPorcJornada()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: PORCENTAJE REDUCCION JORNADA LABORAL
                    celda = fila.createCell(8);
                    celda.setCellValue(listaSMI.get(i).getPorcReduccion() != null ? String.valueOf(listaSMI.get(i).getPorcReduccion()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: IMPORTE DE LA SUBVENCION
                    celda = fila.createCell(9);
                    celda.setCellValue(listaSMI.get(i).getImporteSolicitado() != null ? String.valueOf(listaSMI.get(i).getImporteSolicitado()) : "");
                    celda.setCellStyle(estiloCelda);
                    totalSolicitado += listaSMI.get(i).getImporteSolicitado() != null ? listaSMI.get(i).getImporteSolicitado() : Double.valueOf("0.0");

                    //COLUMNA: IMPORTE DE LA SUBVENCION CALCULADO
                    celda = fila.createCell(10);
                    celda.setCellValue(listaSMI.get(i).getImporteRecalculo() != null ? String.valueOf(listaSMI.get(i).getImporteRecalculo()) : "");
                    celda.setCellStyle(estiloCelda);
                    totalConcedido += listaSMI.get(i).getImporteRecalculo() != null ? listaSMI.get(i).getImporteRecalculo() : Double.valueOf("0.0");

                }
                numFila++;
                fila = hoja.createRow(numFila);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "label.totalSubvencion").toUpperCase());
                celda.setCellStyle(estiloFilaAzul);
                celda = fila.createCell(9);
                celda.setCellValue(totalSolicitado);
                celda.setCellStyle(estiloFilaAzul);

                celda = fila.createCell(10);
                celda.setCellValue(totalConcedido);
                celda.setCellStyle(estiloFilaAzul);

                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenDatosAnexoB", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream(istr); // promote

                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException ioe) {
                log.error("EXCEPCION informe resumenDatosAnexoB");

            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoB");

        } catch (SQLException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoB");
        }
    }

    private void crearEstiloInformeDatosSMI(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info("Entramos en crearEstiloInformeDatosSMI de " + this.getClass().getSimpleName());
        try {
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            HSSFColor hssfColor2 = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                hssfColor2 = palette.getColor(HSSFColor.GREY_50_PERCENT.index);

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras
                // num linea
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // dni
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // ape + nombre               
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // dias SIN              
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // num dias incidencia
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // causa
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // fecha
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // % jornada laboral
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                //% bajo rendimiento
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // Importe de la subvencion
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // importe calculado
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "smi.tablaSMI.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);

            } catch (Exception e) {
            }

        } catch (Exception ex) {

        }

    }

    // ----------------------------------------------------------------------------------------------------------
    // --------------------------  ANEXO C -  PLANTILLA    ------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------
    public String cargarPantallaPrincipal(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaPrincipal de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide58/melanbide58.jsp";
        String mensaje = "";
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<PlantillaVO> listaPlantilla = MeLanbide58Manager.getInstance().getDatosControlAcceso(numExpediente, codOrganizacion, adapt);
                if (!listaPlantilla.isEmpty()) {
                    request.setAttribute("listaPlantilla", listaPlantilla);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos De Acceso - MELANBIDE58 - cargarPantallaPrincipal", ex);
                mensaje = "Error al recuperar los datos.";
            }
        }
        request.setAttribute("mensaje", mensaje);
        return url;
    }

    public String cargarNuevoAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoAcceso de " + this.getClass().getSimpleName());
        String nuevo = "1";
        String numExp = "";
        int numLinea = 0;
        String urlnuevoAcceso = "/jsp/extension/melanbide58/nuevoAcceso.jsp?codOrganizacion=" + codOrganizacion;
        AdaptadorSQLBD adapt = null;
        MeLanbide58Manager meLanbide58Manager = new MeLanbide58Manager();
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
            //Cargamos el el request los valores  de los desplegables
            cargarDesplegablesAnexoC(codOrganizacion, request);

            // recupero el último nº de linea en el anexo C
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            numLinea = meLanbide58Manager.getUltimoNumLinea(numExp, ConfigurationParameter.getParameter(ConstantesMeLanbide58.MELANBIDE58_PLANTILLA, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
            log.debug("Último número de línea en el anexo C: " + numLinea);
            request.setAttribute("numLineaC", String.valueOf(numLinea + 1));
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de un Nuevo Acceso : " + ex.getMessage());
        }
        return urlnuevoAcceso;
    }

    public String cargarModificarAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarAcceso de " + this.getClass().getSimpleName());
        String nuevo = "0";
        String urlnuevoAcceso = "/jsp/extension/melanbide58/nuevoAcceso.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            log.debug("id: " + id);
            // Recuperramos datos e Acceso a modificar y cargamos en el request
            if (id != null && !id.isEmpty()) {
                PlantillaVO datModif = MeLanbide58Manager.getInstance().getControlAccesoPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            //Cargamos el el request los valores  de los desplegables 
            cargarDesplegablesAnexoC(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return urlnuevoAcceso;

    }

    public void eliminarAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarAcceso de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        String numExp = "";
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Control de Acceso a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = MeLanbide58Manager.getInstance().eliminarAcceso(id, adapt);
                if (result <= 0) {
                    codigoOperacion = "1";
                } else {
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide58Manager.getInstance().getDatosControlAcceso(numExp, codOrganizacion, adapt);
                    } catch (Exception ex) {
                        log.error("Error al recuperar la lista de acceso despues de eliminar un registro de Control");
                    }
                }
            }
        } catch (Exception ex) {
            log.error("Error eliminando un registro de Control : " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAcceso(request, codigoOperacion, lista, "0");
        retornarXML(xmlSalida, response);
    }

    public void buscarAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en buscarAcceso de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        String nif = request.getParameter("nifC");
        String numLinea = request.getParameter("numLineaC");
        String apellidos = request.getParameter("apellidosC");
        String numExp = "";
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try {
                lista = MeLanbide58Manager.getInstance().getRegistrosAcceso(numExp, nif, numLinea, apellidos, adapt);
                if (lista.isEmpty()) {
                    codigoOperacion = "1";
                }
            } catch (Exception ex) {
                log.error("Error al recuperar datos de Acceso");
            }

        } catch (SQLException ex) {
            log.error("Error recuperando datos de Acceso: " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAcceso(request, codigoOperacion, lista, "0");
        retornarXML(xmlSalida, response);
    }

    public void crearNuevoAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoAcceso de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        PlantillaVO nuevaPersona = new PlantillaVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros

            String dni_nif = request.getParameter("dni_nif");
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String codSexo = request.getParameter("codSexo");
            String fechaNac = request.getParameter("fechaNac");
            String numSS = request.getParameter("numSS");
            String fechaCert = request.getParameter("fecCert");
            String tipoDis = request.getParameter("tipoDis");
            String grado = request.getParameter("grado").replace(",", ".");
            String codCodContrato = request.getParameter("codCodContrato");
            String numLinea = request.getParameter("numLinea");
            String numExp = request.getParameter("expediente");
//            String porcJornada = request.getParameter("porcJornada").replace(",", ".");
            String validado = request.getParameter("validado");
            String severo = request.getParameter("severo");

            nuevaPersona.setNumExp(numExp);
            if (fechaNac != null && !"".equals(fechaNac)) {
                nuevaPersona.setFecNaci(new java.sql.Date(dateFormat.parse(fechaNac).getTime()));
            }
            if (fechaCert != null && !"".equals(fechaCert)) {
                nuevaPersona.setFecCertificado(new java.sql.Date(dateFormat.parse(fechaCert).getTime()));
            }
            nuevaPersona.setNif_Dni(dni_nif);
            nuevaPersona.setNombre(nombre);
            nuevaPersona.setApellidos(apellidos);
            nuevaPersona.setSexo(codSexo);
            nuevaPersona.setNumSS(numSS);
            nuevaPersona.setTipoDis(tipoDis);
            if (grado != null && !"".equals(grado)) {
                nuevaPersona.setGrado(Double.valueOf(grado));
                if (esSevera(tipoDis, (Double.valueOf(grado)))) {
                    severo = "S";
                    log.debug("Es SEVERO");
                } else {
                    severo = "N";
                    log.debug("NO es SEVERO");
                }
            } else {
                nuevaPersona.setGrado(null);
                severo = "N";
                log.debug("NO es SEVERO");
            }

            nuevaPersona.setDiscSevera(severo);
            nuevaPersona.setDiscValidada(validado);
            nuevaPersona.setCodContrato(codCodContrato);
            if (numLinea != null && !"".equals(numLinea)) {
                nuevaPersona.setNumLinea(Integer.valueOf(numLinea));
            }
//            if (porcJornada != null && !"".equals(porcJornada)) {
//                nuevaPersona.setPorcJornada(Double.valueOf(porcJornada));
//            }
            MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
            if (meLanbide58Manager.crearNuevoAcceso(nuevaPersona, adapt)) {
                log.info("Nuevo registro insertado en Anexo C");
                try {
                    lista = meLanbide58Manager.getDatosControlAcceso(numExp, codOrganizacion, adapt);
                    if (lista.isEmpty()) {
                        codigoOperacion = "1";
                        log.error("Error al recuperar datos de Acceso");
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos de Acceso");
                }
            } else {
                log.error("NO se ha insertado correctamente el nuevo registro de acceso");
                codigoOperacion = "1";
            }

        } catch (Exception ex) {
            log.error("Error al parsear los parametros recibidos del jsp al objeto ControlAccesoVO " + ex.getMessage());
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAcceso(request, codigoOperacion, lista, "0");
        retornarXML(xmlSalida, response);
    }

    public void modificarAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>> ENTRA en modificarAcceso de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        String codigoIncompleto = "0";
        List<PlantillaVO> lista = new ArrayList<PlantillaVO>();
        DiscapacitadoVO persona = new DiscapacitadoVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Recojo los parametros
            String id = request.getParameter("id");
            String dni_nif = request.getParameter("dni_nif");
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String codSexo = request.getParameter("codSexo");
            String fechaNac = request.getParameter("fechaNac");
            String numSS = request.getParameter("numSS");
            String fechaCert = request.getParameter("fecCert");
            String tipoDis = request.getParameter("tipoDis");
            String grado = request.getParameter("grado").replace(",", ".");
            String codCodContrato = request.getParameter("codCodContrato");
            String numLinea = request.getParameter("numLinea");
            String numExp = request.getParameter("expediente");
//            String porcJornada = request.getParameter("porcJornada").replace(",", ".");
            String validado = request.getParameter("validado");
            String severo = request.getParameter("severo");
//            String porcOriginal = request.getParameter("porcOriginal");
//            log.debug("% original: " + porcOriginal);
            // en las tablas del modulo los dni no estan validados, en PERS_DISCP si - Elimino caracteres no admitidos 
            String dniLimpio = dni_nif.replaceAll("[^\\dA-Za-z]", "").toUpperCase();
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Control de Acceso a Modificar ");
                codigoOperacion = "3";
            } else {
                log.debug("getControlAccesoPorID");
                PlantillaVO datModif = MeLanbide58Manager.getInstance().getControlAccesoPorID(id, adapt);
                numExp = datModif.getNumExp();
                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                if (fechaNac != null && !"".equals(fechaNac)) {
                    datModif.setFecNaci(new java.sql.Date(dateFormat.parse(fechaNac).getTime()));
                } else {
                    datModif.setFecNaci(null);
                }
                if (fechaCert != null && !"".equals(fechaCert)) {
                    datModif.setFecCertificado(new java.sql.Date(dateFormat.parse(fechaCert).getTime()));
                } else {
                    datModif.setFecCertificado(null);
                }
                datModif.setNif_Dni(dni_nif);
                datModif.setNombre(nombre);
                datModif.setApellidos(apellidos);
                datModif.setSexo(codSexo);
                datModif.setNumSS(numSS);
                datModif.setTipoDis(tipoDis);
                if (grado != null && !"".equals(grado)) {
                    datModif.setGrado(Double.valueOf(grado));
                    if (esSevera(tipoDis, (Double.valueOf(grado)))) {
                        severo = "S";
                        log.debug("Es SEVERO");
                    } else {
                        severo = "N";
                        log.debug("NO es SEVERO");
                    }
                } else {
                    severo = "N";
                    log.debug("NO es SEVERO");
                    datModif.setGrado(null);
                }

                datModif.setDiscSevera(severo);
                datModif.setDiscValidada(validado);
                datModif.setCodContrato(codCodContrato);
                if (numLinea != null && !"".equals(numLinea)) {
                    datModif.setNumLinea(Integer.valueOf(numLinea));
                } else {
                    datModif.setNumLinea(null);
                }
//                if (porcJornada != null && !"".equals(porcJornada)) {
//                    datModif.setPorcJornada(Double.valueOf(porcJornada));
//                } else {
//                    datModif.setPorcJornada(null);
//                }
                MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
                boolean modOK = meLanbide58Manager.modificarAccesoSeleccionado(datModif, adapt);
                int mismoDni = meLanbide58Manager.modificarAccesosXpersona(datModif, numExp, adapt);
                log.debug("Actualizados " + mismoDni + " registros en Anexo C para el DNI" + dni_nif);
                if (modOK) {

                    // actualizar x DNI y porcOriginal en Anexo B
//                    if (!porcJornada.equalsIgnoreCase(porcOriginal)) {
//                        if (!meLanbide58Manager.actualizaJornadaB(numExp, apellidos, dni_nif, datModif.getPorcJornada(), porcOriginal, adapt)) {
//                            log.error("Error en actualizaJornadaB");
//                            codigoOperacion = "11";
//                        }
//                    }

                    // comprobar Si validado
                    if (validado.equalsIgnoreCase("S")) {
                        // comprobar registrado en tabla Pers_Disc    
                        int codigo = 0;
                        codigo = meLanbide58Manager.existePersonaDisc(dniLimpio, adapt);

                        log.debug("Cargo los datos en DiscapacitadoVO");
                        persona.setId(codigo);
                        persona.setDni(dniLimpio);
                        persona.setApellidos(apellidos);
                        persona.setNombre(nombre);
                        persona.setTipoDisc(tipoDis);
                        if (grado != null && !"".equals(grado)) {
                            persona.setPorcDisc(Double.valueOf(grado));
                        }

                        if (fechaCert != null && !"".equals(fechaCert)) {
                            persona.setFechResolucion(new java.sql.Date(dateFormat.parse(fechaCert).getTime()));
                            persona.setFechEmision(new java.sql.Date(dateFormat.parse(fechaCert).getTime()));
                        } else {
                            persona.setFechResolucion(null);
                            persona.setFechEmision(null);
                        }
                        persona.setDiscSevera(severo);

                        if (codigo == 0) {
                            // no existe en Pers_Disc
                            if (meLanbide58Manager.nuevaPersonaDisc(persona, adapt)) {
                                log.debug("Nueva persona registrada en PERS_DISCP - " + dniLimpio);
                            } else {
                                log.error("Error al insertar nueva Persona Discapacitada  " + dniLimpio);
                                codigoOperacion = "8";
                            }
                        } else {
                            if (meLanbide58Manager.tieneFechaBajaDisc(codigo, dniLimpio, adapt)) {
                                // borrar f baja
                                boolean grabaFecha = meLanbide58Manager.borrarFechaBajaDisc(codigo, dniLimpio, adapt);
                                if (grabaFecha) {
                                    log.info("Fecha de baja borrada en PERS_DISCP en el registro " + codigo + " de la persona con DNI " + dniLimpio);
                                    boolean validaNo = meLanbide58Manager.actualizaDiscValidada(numExp, dni_nif, "N", adapt);
                                    if (validaNo) {
                                        log.info("Se ha grabado validez = N  de la persona con DNI " + dniLimpio);
                                    } else {
                                        log.error("NO se ha grabado validez = N  de la persona con DNI " + dniLimpio);
                                    }
                                } else {
                                    log.error("NO se ha borrado la fecha de baja en PERS_DISCP en el registro " + codigo + " de la persona con DNI " + dniLimpio);
                                }

                                // comprobar fecha caducidad
                                java.util.Date hoy = new Date();
                                if (persona.getFechCaducidad() != null && persona.getFechCaducidad().before(hoy)) {
                                    log.info("Certificado CADUCADO");
                                    boolean grabaBajaCaduca = meLanbide58Manager.grabaFechaBaja(codigo, dniLimpio, persona.getFechCaducidad(), adapt);
                                    if (grabaBajaCaduca) {
                                        log.info("F Baja grabada para la persona con DNI " + dniLimpio);
                                    } else {
                                        log.error("Ha ocurrido un error grabando la F Baja" + dniLimpio);
                                        codigoOperacion = "9";
                                    }
                                }
                            }
                            if (meLanbide58Manager.actualizarPersonaDisc(persona, adapt)) {
                                log.debug("Persona actualizada en PERS_DISCP - " + dniLimpio);
                                DiscapacitadoVO persActualizada = MeLanbide58Manager.getInstance().getPersonaDiscp(codigo, dniLimpio, adapt);

                                persona.setValidez(persActualizada.getValidez());
                                persona.setOidCertificado(persActualizada.getOidCertificado());
                                if (persActualizada.getValidez() != null && persActualizada.getValidez().equalsIgnoreCase("T")) {
                                    persona.setFechCaducidad(persActualizada.getFechCaducidad());
                                }
                            } else {
                                log.error("Error al actualizar una Persona Discapacitada  " + dniLimpio);
                                codigoOperacion = "9";
                            }
                        }

                        log.debug("persona registrada en PERS_DISCP - " + dniLimpio);
                        log.debug("Validez - " + persona.getValidez());
                        log.debug("F Caducidad - " + persona.getFechCaducidad());
                        log.debug("OID - " + persona.getOidCertificado());

                        if ((persona.getValidez() == null || persona.getValidez().isEmpty()) || (persona.getOidCertificado() == null || persona.getOidCertificado().isEmpty())) {
                            // quitar marca de validado
                            try {
                                meLanbide58Manager.actualizaDiscValidada(numExp, dni_nif, "N", adapt);
                            } catch (BDException bd) {
                                log.error("Error de tipo BD al eliminar la la marca de discapacidad validadas despues de Modificar un registro : " + bd.getMensaje());
                            } catch (Exception ex) {
                                log.error("Error al recuperar la lista de Accesos despues de Modificar un registro : " + ex.getMessage());
                            }
                            // avisar que faltan datos
                            codigoIncompleto = persona.getDni();

                        }
                    } // if VALIDADO

                    try {
                        // COMPROBAR SI ES NUEVA ALTA Y TIENE DATOS PENDIENTES
                        boolean esNueva = meLanbide58Manager.esNuevaAlta(numExp, dni_nif, adapt);
                        if (esNueva && !codigoIncompleto.equalsIgnoreCase("0")) {
                            log.info("La persona con DNI: " + dni_nif + " es nuevo ALTA y Faltan datos. No se realiza el recalculo");
                        } else {
                            // recalcular SMI
                            String numLineaB = "";
                            numLineaB = String.valueOf(meLanbide58Manager.getLineaPadreBValidadaDNI(numExp, dni_nif, adapt));
                            log.debug("Nº linea: " + numLineaB);
                            // si existe en Anexo B se recalcula
                            if (numLineaB != null && !numLineaB.equalsIgnoreCase("0") && !numLineaB.isEmpty()) {
                                String mensajeRecalculo = meLanbide58Manager.recalcularSMI(numExp, numLineaB, adapt);
                                if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.ANIO_VACIO)) {
                                    codigoOperacion = "4";
                                } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.ERROR_RECALCULO)) {
                                    codigoOperacion = "5";
                                } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.NOT_FOUND_SMI)) {
                                    codigoOperacion = "6";
                                } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.UPDATE_RECALCULO)) {
                                    codigoOperacion = "7";
                                }
                                //  grabar importe total 
                                grabarImporteResolCEESC(codOrganizacion, codTramite, ocurrenciaTramite, numExp);
                            }
                        }

                        lista = meLanbide58Manager.getDatosControlAcceso(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de Accesos despues de Modificar un registro : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "2";
                        log.error("Error al recuperar la lista de Accesos despues de Modificar un registro : " + ex.getMessage());
                    }
                } else {
                    log.error("Error en modificarAccesoSeleccionado");
                    codigoOperacion = "10";
                } // modOK
            } // id == null || id.equals("")
        } catch (Exception ex) {
            log.error("Error modificar --- ", ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaAcceso(request, codigoOperacion, lista, codigoIncompleto);
        retornarXML(xmlSalida, response);
    }

    private void cargarDesplegablesAnexoC(int codOrganizacion, HttpServletRequest request) throws Exception {
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        List<DesplegableAdmonLocalVO> listaSexo = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_SEXO, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
        if (!listaSexo.isEmpty()) {
            listaSexo = traducirDesplegable(request, listaSexo);
            request.setAttribute("listaSexo", listaSexo);
        }
        List<DesplegableAdmonLocalVO> listaTipoDis = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TIPODIS, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
        if (!listaTipoDis.isEmpty()) {
            listaTipoDis = traducirDesplegable(request, listaTipoDis);
            request.setAttribute("listaTipoDis", listaTipoDis);
        }
        List<DesplegableAdmonLocalVO> listaCodContrato = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_COD_CONTRATO, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
        if (!listaCodContrato.isEmpty()) {
            listaCodContrato = traducirDesplegable(request, listaCodContrato);
            request.setAttribute("listaCodContrato", listaCodContrato);
        }
        List<DesplegableAdmonLocalVO> listaSiNo = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_BOOL, ConstantesMeLanbide58.FICHERO_PROPIEDADES), adapt);
        if (!listaSiNo.isEmpty()) {
            listaSiNo = traducirDesplegable(request, listaSiNo);
            request.setAttribute("listaSiNo", listaSiNo);
        }
    }

    public void generarExcelAnexoC(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>> ENTRA en generarExcelAnexoC  de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;
        List<PlantillaVO> listaPlantilla = null;
        try {
            //  listaPlantilla = (List<PantillaVO>) request.getAttribute("listaPlantilla");
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) {
                        idioma = usuario.getIdioma();
                    }
                }
            } catch (Exception ex) {
            }

            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            MeLanbide58Manager manager = MeLanbide58Manager.getInstance();
            String numExpe = "";
            String nif = request.getParameter("nifC");
            String numLinea = request.getParameter("numLineaC");
            String apellidos = request.getParameter("apellidosC");
            try {
                numExpe = request.getParameter("numExp");
                try {
                    listaPlantilla = manager.getRegistrosAcceso(numExpe, nif, numLinea, apellidos, adapt);
                    if (listaPlantilla.isEmpty()) {
                        meLanbide58I18n.getMensaje(idioma, "error.errorGen");
                    }
                } catch (Exception ex) {
                    log.error("Error al recuperar datos del Anexo C");
                }
            } catch (Exception ex) {
                log.error("Error recuperando datos de Anexo C : " + ex);
            }

            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();

                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short) 200);
                int numFila = 0;
                //int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                HSSFSheet hoja = libro.createSheet("Datos Anexo C - Plantilla");

                //Se establece el ancho de cada columnas
                //hoja.setColumnWidth((short)0, (short)1500);//ID
                hoja.setColumnWidth(0, 3000);//Número de línea
                hoja.setColumnWidth(1, 8000);//Apellidos y nombre (1)
                hoja.setColumnWidth(2, 4750);//Sexo(2)
                hoja.setColumnWidth(3, 000);//Fecha de nacimiento
                hoja.setColumnWidth(4, 3000);//DNI
                hoja.setColumnWidth(5, 3000);//Nº Afiliación a la seg. soc.
                hoja.setColumnWidth(6, 4000);//Fecha certificado discapacidad
                hoja.setColumnWidth(7, 3000);//Tipo discapac. (3) 
                hoja.setColumnWidth(8, 3500);//Grado de discapac.
                hoja.setColumnWidth(9, 5000);//Tipo de contrato (4A)
                //      hoja.setColumnWidth(10, 5000);//Duración (4B)
//                hoja.setColumnWidth(10, 3000);//Poecentaje Jornada
                hoja.setColumnWidth(10, 4000);//Discapacitado Severo
                hoja.setColumnWidth(11, 4000);//Discapacitado Validado

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);

                crearEstiloInformeDatosAnexoC(libro, fila, celda, estiloCelda, idioma);

                int p = 0;
                int n = 0;

                String numExp = null;

                estiloCelda = libro.createCellStyle();
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                //Insertamos los datos, fila a fila
                for (PlantillaVO filaI : listaPlantilla) {
                    if (filaI.getNumExp() != null && !filaI.getNumExp().equals(numExp)) {
                        numExp = filaI.getNumExp();
                        p = 0;
                        n++;
                        numFila++;
                    } else {
                        numFila++;
                        p++;
                    }
                    fila = hoja.createRow(numFila);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    //COLUMNA: NUMERO DE LINEA
                    celda = fila.createCell(0);
                    celda.setCellValue(filaI.getNumLinea() != null ? String.valueOf(filaI.getNumLinea()) : "");
                    celda.setCellStyle(estiloCelda);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

                    //COLUMNA: APELLIDOS Y NOMBRE
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                    celda = fila.createCell(1);
                    celda.setCellValue((filaI.getApellidos() != null ? filaI.getApellidos().toUpperCase() : "") + ", " + (filaI.getNombre() != null ? filaI.getNombre().toUpperCase() : ""));
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: SEXO (2)
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getDesSexo() != null ? String.valueOf(getDescripcionDesplegable(request, filaI.getDesSexo())) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA DE NACIMIENTO
                    celda = fila.createCell(3);
                    celda.setCellValue(filaI.getFecNaci() != null ? dateFormat.format(filaI.getFecNaci()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: DNI
                    celda = fila.createCell(4);
                    celda.setCellValue(filaI.getNif_Dni() != null ? String.valueOf(filaI.getNif_Dni()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: Nº AFILIACION A LA SEG. SOC.
                    celda = fila.createCell(5);
                    celda.setCellValue(filaI.getNumSS() != null ? String.valueOf(filaI.getNumSS()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA CERTIFICADO DISCAPACIDAD
                    celda = fila.createCell(6);
                    celda.setCellValue(filaI.getFecCertificado() != null ? dateFormat.format(filaI.getFecCertificado()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIPO DISCAPAC. (3)
                    celda = fila.createCell(7);
                    celda.setCellValue(filaI.getDesTipoDis() != null ? String.valueOf(getDescripcionDesplegable(request, filaI.getDesTipoDis())) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: GRADO DE DISCAPAC.
                    celda = fila.createCell(8);
                    celda.setCellValue(filaI.getGrado() != null ? String.valueOf(filaI.getGrado()) : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: CODIGO DE CONTRATO (4A)
                    celda = fila.createCell(9);
                    celda.setCellValue(filaI.getDesCodContrato() != null ? String.valueOf(getDescripcionDesplegable(request, filaI.getDesCodContrato())) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: PORCENTAJE JORNADA
//                    celda = fila.createCell(10);
//                    celda.setCellValue(filaI.getPorcJornada() != null ? String.valueOf(filaI.getPorcJornada() + "  %") : "");
//                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: DISCAPACIDAD SEVERA
                    celda = fila.createCell(10);
                    celda.setCellValue(filaI.getDesDiscSevera() != null ? String.valueOf(getDescripcionDesplegable(request, filaI.getDesDiscSevera())) : "");
                    celda.setCellStyle(estiloCelda);

                    // COLUMNA: DISCAPACIDAD VALIDADA
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    celda = fila.createCell(11);
                    celda.setCellValue(filaI.getDesDiscValidada() != null ? String.valueOf(getDescripcionDesplegable(request, filaI.getDesDiscValidada())) : "");
                    celda.setCellStyle(estiloCelda);
                }

                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenDatosAnexoC", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream(istr); // promote

                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException ioe) {
                log.error("EXCEPCION informe resumenDatosAnexoC");

            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoC");
        } catch (SQLException ex) {
            log.error("EXCEPCION informe resumenDatosAnexoC");
        }
    }

    private void crearEstiloInformeDatosAnexoC(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info(">>>> ENTRA en crearEstiloInformeDatosAnexoC de " + this.getClass().getSimpleName());
        try {
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();

            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                palette.getColor(HSSFColor.GREY_50_PERCENT.index);

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);

                // num linea                
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // Apellidos y nombre 
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                //Sexo
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // Fecha de nacimiento
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // DNI
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // num SS
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // fecha certificado discapacidad
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // Tipo de discapac
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // porcen discap
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // Tipo de contrato
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col19").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // Discapacidad severa  
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col15").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // Discapacidad validada                
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "anexoC.tablaPlantilla.col18").toUpperCase());
                celda.setCellStyle(estiloCelda);

            } catch (Exception e) {
            }

        } catch (Exception ex) {

        }

    }

    // -------------------------------------------------------------
    // ---------------------   PERSONAS DISCP   --------------------
    // -------------------------------------------------------------    
    public String cargarConsultaDisc(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarConsultaDisc de " + this.getClass().getSimpleName());
        String urlConsultaDiscp = "/jsp/extension/melanbide58/consultaPersDiscp.jsp?codOrganizacion=" + codOrganizacion;
        String id = null;
        String dni = null;
        String dniLimpio = null;
        String numExp = null;
        List<DiscapacitadoVO> datosPersona = null;
        try {
            id = request.getParameter("id");
            cargarDesplegablesPersDiscp(codOrganizacion, request);
            numExp = request.getParameter("numExp");
            if (id != null && !id.isEmpty()) {
                log.debug("Hay ID");
                // buscar dni x id

                dni = MeLanbide58Manager.getInstance().getNifAnexoCPorId(numExp, id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                log.debug("Con DNI: " + dni);
                request.setAttribute("dniCeesc", dni);
                // OJO DNIs no validados - Elimino caracteres no admitidos                         
                dniLimpio = dni.replaceAll("[^\\dA-Za-z]", "").toUpperCase();
                log.debug("DNI limpio: " + dniLimpio);
                request.setAttribute("dniLimpio", dniLimpio);
                // cargar datos presona seleccionada
                datosPersona = MeLanbide58Manager.getInstance().getDatosPersona(dni, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (!datosPersona.isEmpty()) {

                    log.debug("Existe en PERS_DISCP");
                } else {
                    //  datosPersona = MeLanbide58Manager.getInstance().getPersonasBusqueda("", "", "", "", "", "", "", this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    log.debug("NO Existe en PERS_DISCP");
                }
            } else {
                //datosPersona = MeLanbide58Manager.getInstance().getPersonasBusqueda("", "", "", "", "", "", "", this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                log.debug("NO hay ID");
            }
        } catch (Exception ex) {
            log.error("Error en cargarConsultaDisc", ex);
        }
        request.setAttribute("expediente", numExp);
        request.setAttribute("datosPersona", datosPersona);
        log.debug(" Se llama a url" + urlConsultaDiscp);
        return urlConsultaDiscp;
    }

    private void cargarDesplegablesPersDiscp(int codOrganizacion, HttpServletRequest request) throws Exception {
        log.debug("Cargo los deplegables de PERS_DISCP");

        // Tipo discapacidad
        List<DesplegableAdmonLocalVO> listaTipoDiscapacidad = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TIPODIS, ConstantesMeLanbide58.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaTipoDiscapacidad.isEmpty()) {
            listaTipoDiscapacidad = traducirDesplegable(request, listaTipoDiscapacidad);
            request.setAttribute("listaTipoDiscapacidad", listaTipoDiscapacidad);
        }
        // Validez certificado
        List<DesplegableAdmonLocalVO> listaValidezCertificado = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_VALIDISC, ConstantesMeLanbide58.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaValidezCertificado.isEmpty()) {
            listaValidezCertificado = traducirDesplegable(request, listaValidezCertificado);
            request.setAttribute("listaValidezCertificado", listaValidezCertificado);
        }
        // Marca de discapacidad severa
        List<DesplegableAdmonLocalVO> listaEsSevera = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_BOOL, ConstantesMeLanbide58.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaEsSevera.isEmpty()) {
            listaEsSevera = traducirDesplegable(request, listaEsSevera);
            request.setAttribute("listaEsSevera", listaEsSevera);
        }
        // Territorio Historico
        List<DesplegableAdmonLocalVO> listaTerritorio = MeLanbide58Manager.getInstance().getValoresDesplegablesAdmonLocalxdes_cod(ConfigurationParameter.getParameter(ConstantesMeLanbide58.COD_DESP_TERRITORIO, ConstantesMeLanbide58.FICHERO_PROPIEDADES), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaTerritorio.isEmpty()) {
            listaTerritorio = traducirDesplegable(request, listaTerritorio);
            request.setAttribute("listaTerritorio", listaTerritorio);
        }
    }

    public void buscarPersonaDiscp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en buscarPersonaDiscp de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        List<DiscapacitadoVO> lista = null;
        String dni = request.getParameter("dni");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String tipoD = request.getParameter("tipoD");
        String grado = request.getParameter("grado");
        String severa = request.getParameter("severa");
        String validez = request.getParameter("validez");
        String centro = request.getParameter("centro");
        String territorio = request.getParameter("territorio");
        log.debug("dni " + dni);
        log.debug("nombre " + nombre);
        log.debug("apellidos" + apellidos);
        log.debug("tipo " + tipoD);
        log.debug("grado " + grado);
        log.debug("severa " + severa);
        log.debug("validez " + validez);
        log.debug("centro  " + centro);
        log.debug("territorio " + territorio);
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide58Manager.getInstance().getPersonasBusqueda(dni, nombre, apellidos, tipoD, grado, severa, validez, centro, territorio, adapt);
            if (lista.isEmpty()) {
                codigoOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error recuperando datos de Personas Discapacitadas: " + ex);
            codigoOperacion = "2";
        }
        String xmlSalida = null;
        xmlSalida = obtenerXmlSalidaDiscapacitados(codigoOperacion, lista);
        retornarXML(xmlSalida, response);
    }

    public void modificarDiscapacitado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExp, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos modificarDiscapacitado  de " + this.getClass().getSimpleName());
        String codigoOperacion = "0";
        DiscapacitadoVO persona = new DiscapacitadoVO();
        String severo = null;
        String id = request.getParameter("id");
        String dni = request.getParameter("dni");
        String nombre = request.getParameter("nombre");
        String apellidos = request.getParameter("apellidos");
        String tipoD = request.getParameter("tipoD");
        String grado = request.getParameter("grado").replace(",", ".");
        String fecEmision = request.getParameter("fecEmision");
        String fecResolucion = request.getParameter("fecResolucion");
        String validez = request.getParameter("validez");
        String fecCaducidad = request.getParameter("fecCaducidad");
        String severa = request.getParameter("severa");
        String fecValidacion = request.getParameter("fecValidacion");
        String fecBaja = request.getParameter("fecBaja");
        String oidCertificado = request.getParameter("oid");
        String nombreCertificado = request.getParameter("nombreCertificado");
        String expediente = request.getParameter("expediente");

        log.debug("id " + id);
        log.debug("dni " + dni);
        log.debug("nombre " + nombre);
        log.debug("apellidos " + apellidos);
        log.debug("tipo " + tipoD);
        log.debug("grado " + grado);
        log.debug("fecEmision " + fecEmision);
        log.debug("fecResolucion " + fecResolucion);
        log.debug("validez " + validez);
        log.debug("fecCaducidad " + fecCaducidad);
        log.debug("severa " + severa);
        log.debug("fecValidacion " + fecValidacion);
        log.debug("fecBaja " + fecBaja);
        log.debug("Documento " + oidCertificado + " - " + nombreCertificado);
        log.debug("expediente " + expediente);

        String hoy = dateFormat.format(System.currentTimeMillis());
        boolean actualizaFalta = false;
        MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE58.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (id == null || id.isEmpty()) {
            log.error("No se ha recibido desde la JSP el id de la Persona Discapacitada a Modificar ");
            codigoOperacion = "3";
        } else {

            try {
                persona.setId(Integer.valueOf(id));
                persona.setDni(dni);
                persona.setApellidos(apellidos);
                persona.setNombre(nombre);
                persona.setTipoDisc(tipoD);
                if (grado != null && !"".equals(grado)) {
                    persona.setPorcDisc(Double.valueOf(grado));
                }
                if (fecEmision != null && !"".equals(fecEmision)) {
                    persona.setFechEmision(new java.sql.Date(dateFormat.parse(fecEmision).getTime()));
                }
                if (fecResolucion != null && !"".equals(fecResolucion)) {
                    persona.setFechResolucion(new java.sql.Date(dateFormat.parse(fecResolucion).getTime()));
                }
                persona.setValidez(validez);
                if (fecCaducidad != null && !"".equals(fecCaducidad)) {
                    persona.setFechCaducidad(new java.sql.Date(dateFormat.parse(fecCaducidad).getTime()));
                }

                if (esSevera(tipoD, (Double.valueOf(grado)))) {
                    severo = "S";
                    log.debug("Es SEVERO");
                } else {
                    severo = "N";
                    log.debug("NO es SEVERO");
                }
                persona.setDiscSevera(severo);
                /* if (fecValidacion != null && !"".equals(fecValidacion)) {
                    persona.setFechValidacion(new java.sql.Date(formatoFecha.parse(fecValidacion).getTime()));
                }*/
                persona.setFechValidacion(new java.sql.Date(dateFormat.parse(hoy).getTime()));
                if (fecBaja != null && !"".equals(fecBaja)) {
                    persona.setFechBaja(new java.sql.Date(dateFormat.parse(fecBaja).getTime()));
                }
                persona.setOidCertificado(oidCertificado);
                persona.setNombreCertificado(nombreCertificado);

                int modOK = meLanbide58Manager.modificarPersDiscp(persona, adapt);
                if (modOK <= 0) {
                    log.error("Error al modificar Persona Discapacitada - ID: " + id);
                    codigoOperacion = "1";
                } else {
                    // actualizar datos en C
                    // f certificado - tipo -  grado - severa
                    boolean actualizaC = meLanbide58Manager.actualizaPersonaAnexoC(expediente, dni, fecEmision, tipoD, (Double.valueOf(grado)), severo, adapt);
                    if (!actualizaC) {
                        log.error("Error al actualizar en el Anexo C los datos de PERS_DICP");
                        codigoOperacion = "3";
                    }
                }
                if (persona.getValidez() != null && !persona.getValidez().isEmpty() && persona.getOidCertificado() != null && !persona.getOidCertificado().isEmpty()) {
                    actualizaFalta = meLanbide58Manager.actualizaDatosIncompletos(expediente, dni, "N", adapt);
                    if (actualizaFalta) {
                        log.info("Datos Incompletos = N grabado en el expediente " + expediente + " - DNI - " + dni);
                        boolean valida = meLanbide58Manager.actualizaDiscValidada(expediente, dni, "S", adapt);
                        if (valida) {
                            log.info("Validado= S  grabado en el expediente " + expediente + " - DNI - " + dni);
                        } else {
                            log.error("Ha ocurrido un error grabando Validado= S " + expediente + " - DNI - " + dni);
                            codigoOperacion = "5";
                        }

                    } else {
                        log.error("Ha ocurrido un error grabando Datos incompletos " + expediente + " - DNI - " + dni);
                        codigoOperacion = "4";
                    }
                }

                // comprobar fecha caducidad
                java.util.Date dateHoy = new Date();
                if (persona.getFechCaducidad() != null) {
                    if (persona.getFechCaducidad().before(dateHoy)) {
                        log.info("Certificado CADUCADO");
                        boolean grabaBajaCaduca = meLanbide58Manager.grabaFechaBaja(Integer.parseInt(id), dni, persona.getFechCaducidad(), adapt);
                        if (grabaBajaCaduca) {
                            log.info("F Baja grabada para la persona con DNI " + dni);
                        } else {
                            log.error("Ha ocurrido un error grabando la F Baja" + dni);
                            codigoOperacion = "6";
                        }
                    }
                }

            } catch (Exception ex) {
                log.debug("Error modificar --- ", ex);
                codigoOperacion = "2";
            }
        }
        if (codigoOperacion.equalsIgnoreCase("0") && actualizaFalta) {
            try {
                // recalcular
                String numLineaB = "";
                numLineaB = String.valueOf(meLanbide58Manager.getLineaPadreBporDNI(expediente, dni, adapt));
                log.debug("Nº linea: " + numLineaB);
                if (numLineaB != null && !numLineaB.equalsIgnoreCase("0") && !numLineaB.isEmpty()) {
                    String mensajeRecalculo = meLanbide58Manager.recalcularSMI(expediente, numLineaB, adapt);
                    if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.ANIO_VACIO)) {
                        codigoOperacion = "7";
                    } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.ERROR_RECALCULO)) {
                        codigoOperacion = "8";
                    } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.NOT_FOUND_SMI)) {
                        codigoOperacion = "9";
                    } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide58.UPDATE_RECALCULO)) {
                        codigoOperacion = "10";
                    }
                    //  grabar importe total 
                    grabarImporteResolCEESC(codOrganizacion, codTramite, ocurrenciaTramite, expediente);
                }
            } catch (Exception ex) {
                log.debug("Error al recalcular --- ", ex);
            }

        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        log.debug("xml: " + xmlSalida);
        retornarXML(xmlSalida.toString(), response);
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
     * @throws Exception
     */
    public String grabarCertificado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info(this.getClass().getSimpleName() + ">>>>>>> ENTRA en grabarCertificado");
        int codIdioma = 1;
        String idPersona = "";
        String idDocumento = "";
        String idProcedimiento = "";
        String idRegistro = "";
        String expediente = "";
        DocumentoGestor docGestor = null;
        AdaptadorSQLBD adapt = null;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }
            idPersona = request.getParameter("idPersona");
            idRegistro = request.getParameter("idRegistro");
            expediente = request.getParameter("expediente");
            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile fichero = table.get("certificado_pdf");
            // creo un doc gestor para pasar datos si se graba error
            docGestor = new DocumentoGestor();
            //Use commons-fileupload to obtain the byte[] of the file (in a servlet of yours)
            byte[] contenidoFichero = fichero.getFileData();
            // ContentType del fichero
            String nombreFichero = fichero.getFileName();
            log.info("fileName: " + nombreFichero);
            // Extensión del fichero
            String extensionFichero = "";

            String[] partes = null;
            try {
                partes = nombreFichero.split("\\.");
            } catch (Exception e) {
                log.error("split " + e);
            }

            extensionFichero = partes[1];
            log.info("Extensión recibida: " + extensionFichero);
            if (!extensionFichero.equalsIgnoreCase("pdf")) {
                log.info("Se modifica la extensión a .pdf");
                extensionFichero = "pdf";
            }
            log.debug("antes de idProcedimiento: ");
//            idProcedimiento = "LAN62_CEECS";
//            idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide_Dokusi.PREFIJO_ID_PROC + "CEESC", ConstantesMeLanbide_Dokusi.FICHERO_PROPIEDADES);
            idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea("CEESC"); //convierteProcedimiento(codProcedimiento);

            if (idProcedimiento == null || idProcedimiento.isEmpty()) {

                log.error("No se ha recueprado correctamente el codigo del procedimiento para platea del MELANBIDE_DOKUSI.properties : CEESC");
                throw new Exception("Codigo Procedimiento de Platea no recuperado del Properties. CEESC");
            }

            log.info("Instanciamos el Servicio de Dokusi : Lan6DokusiServicios servicios = new Lan6DokusiServicios(" + idProcedimiento + ");");
            Lan6DokusiServicios servicios = new Lan6DokusiServicios("CEESC");
            Lan6Documento lan6Documento = new Lan6Documento();
            lan6Documento.setTipoDocumental(Lan6Constantes.TIPO_DOCUMENTAL_ARCHIVO);
            lan6Documento.setOrigen(Lan6Constantes.MD_ORIGEN_CIUDADANIA);

            // Comprobar caracteres No permitidos
            String[] listaCaracteresNoPermitidos = MeLanbideDokusiManager.getInstance().getListaCaracteresNoPermitidos(codOrganizacion);
            String nombreDoc = "";
            String tituloDoc = "";
            log.info("Antes de conprobar caracteres No permitidos : Nombre del Documento recibido de Flexia " + partes[0]);
            try {
                if (listaCaracteresNoPermitidos != null && listaCaracteresNoPermitidos.length > 0) {
                    nombreDoc = MeLanbideDokusiManager.getInstance().sustituirCaracteresEspeciales(partes[0], listaCaracteresNoPermitidos, codOrganizacion);
                    tituloDoc = MeLanbideDokusiManager.getInstance().sustituirCaracteresEspeciales(partes[0], listaCaracteresNoPermitidos, codOrganizacion);
                }
            } catch (Exception ex) {
                log.error("Error en al Comprobar Caracteres No permitidos en el Nombre del Fichero a Subir a DOKUSI", ex);
                throw ex;
            }
            log.info("Nombre Documento retornado por susituirCaracteresEspeciales :  " + nombreDoc);
            if (docGestor.getExtension() == null || "".equals(docGestor.getExtension())) {
                docGestor.setExtension(MeLanbideDokusiUtil.guessExtensionFromName(nombreDoc));
            }
            lan6Documento.setTitulo(tituloDoc);//("TituloPruebaGuardar");
            lan6Documento.setNombre(nombreDoc);//("NombrePruebaGuardar");
            lan6Documento.setFormat(extensionFichero);
            lan6Documento.setNumTitular(idPersona);
            lan6Documento.setIdExpediente(expediente);
            lan6Documento.setIdProcPlatea(idProcedimiento);

            String metadatoNaturaleza = MeLanbideDokusiManager.getInstance().getMetadatoNaturalezaxTipoDocumentoFromProperties(ConstantesMeLanbide_Dokusi.CODIGO_DOCUMENTO_EXTERNO);
            lan6Documento.setNaturaleza(metadatoNaturaleza);
            log.info("lan6Documento.setTitulo: " + tituloDoc
                    + " lan6Documento.setNombre: " + nombreDoc
                    + " lan6Documento.setFormat: " + extensionFichero
                    + " lan6Documento.setNaturaleza: " + metadatoNaturaleza
                    + " lan6Documento.setIdExpediente: " + expediente
                    + " lan6Documento.setIdProcPlatea: " + idProcedimiento
            );
            try {
                lan6Documento.setContenido(contenidoFichero);//(array);
            } catch (Exception ex) {
                log.error("Error en la llamada al metodo lan6Documento.setContenido(contenidoFichero) del adaptador de Platea para dokusi", ex);
                throw ex;
            }
            try {
                // se graba en DOKUSI
                idDocumento = servicios.guardarDocumento(lan6Documento);
            } catch (Lan6Excepcion ex) {
                log.error("Error Lan6Excepcion  al guardarDocumento externo en Dokusi", ex);
                try {
                    docGestor.setCodProcedimiento(idProcedimiento);

                    log.info("Vamos a registrar el error en BD : " + (!ex.getMessages().isEmpty() ? ex.getMessages().get(0) : ""));
                    String causaExcepcion = (ex.getCause() != null ? (ex.getCause().getMessage() != null ? ex.getCause().getMessage() + " - " + ex.getCause().toString() : ex.getCause().toString()) : "");
                    String mensajeExcepcion = (!ex.getMessages().isEmpty() ? ex.getMessages().get(0) : ConstantesGestionErroresDokusi.LAN6EXCEPCION_SINMENSAJEERROR_MSG);
                    String trazaError = ex.getTrazaExcepcion();  // ExceptionUtils.getStackTrace(ex);

                    Lan6ErrorBean errorBean = new Lan6ErrorBean(causaExcepcion, mensajeExcepcion,
                            trazaError, ConstantesGestionErroresDokusi.SISTEMA_ORIGEN_FLEXIA,
                            ConstantesGestionErroresDokusi.COD_FLEX_ERROR_SETDOC_EXT, ConstantesGestionErroresDokusi.MEN_FLEX_ERROR_SETDOC_EXT, ex.gettipologia());
                    ErrorLan6ExcepcionBean errorLan6Bean = new ErrorLan6ExcepcionBean(errorBean, ex);
                    GestionarErroresDokusi.grabarError(errorLan6Bean, docGestor, idDocumento, "", this.getClass().getName() + ".setDocumentoExterno()");
                    log.info("Error Registrado en BD correctamente. ");
                } catch (Exception ex1) {
                    log.error(" Dokusi. Flexia Error al registrar errores mediante servicios de Adaptadores de platea. Error que se intenta Registrar : " + ex.getMessage(), ex1);
                    throw ex1;
                }
                throw new Exception(ConstantesGestionErroresDokusi.COD_FLEX_ERROR_SETDOC_EXT);
            }

            // grabar idDocumento y nombre certificado en PERS_DISCP
            if (!("".equalsIgnoreCase(idDocumento))) {
                try {
                    adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(MELANBIDE58.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
                    // si el registro de la  persona se ha insertado en esta sesion  idRegistro tendra valor 0 y tenemos que recuperar ese valor
                    if (idRegistro == null || idRegistro.equalsIgnoreCase("0")) {
                        idRegistro = meLanbide58Manager.ultimaIdPorDni(idPersona, adapt);
                    }
                    boolean insertOk = meLanbide58Manager.grabarOid(idRegistro, idPersona, idDocumento, nombreDoc, adapt);
                    if (insertOk) {
                        log.info("Se graba el OID del certificado: " + idDocumento + "-" + nombreDoc + " - DNI: " + idPersona);
                        request.getSession().setAttribute("idDocumento", idDocumento);
                        request.getSession().setAttribute("nombreFichero", nombreDoc);
                        request.getSession().setAttribute("resultado", "0");
                    }
                } catch (Exception e) {
                    log.error("Error al guardar en OID " + e);
                    throw e;
                }
            }

            request.getSession().setAttribute("mensajeImportar", MeLanbide58I18n.getInstance().getMensaje(codIdioma, "msg.certificadoGrabadoOK"));

        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE58.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE58.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE58.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide58I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        return "/jsp/extension/melanbide58/recargarConsultaPersDiscp.jsp";
    }

    public void descargarCertificado(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info(this.getClass().getSimpleName() + ">>>>>>> ENTRA en descargarCertificado");
        String idProcedimiento = "";
        byte[] fichero = null;
        String mimeTypeContent = "";
        String nombreFichero = "";
        String extensionFichero = "";
        try {
            String oidCertificado = request.getParameter("oidCertificado");
            log.info("Recojo el OID: " + oidCertificado);
            if (oidCertificado == null || oidCertificado.isEmpty()) {
                log.error("No se ha recibido desde la JSP el oid del certificado.");
            } else {
                idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide_Dokusi.PREFIJO_ID_PROC + "CEESC", ConstantesMeLanbide_Dokusi.FICHERO_PROPIEDADES);
                if (idProcedimiento == null || idProcedimiento.isEmpty()) {
                    log.error("No se ha recueprado correctamente el codigo del procedimiento para platea del MELANBIDE_DOKUSI.properties : CEESC");
                    throw new Exception("Codigo Procedimiento de Platea no recuperado del Properties. CEESC");
                }
                log.info("Instanciamos el Servicio de Dokusi : Lan6DokusiServicios servicios = new Lan6DokusiServicios(" + idProcedimiento + ");");
                Lan6DokusiServicios servicios = new Lan6DokusiServicios(idProcedimiento);
                Lan6Documento lan6Documento = new Lan6Documento();
                try {
                    lan6Documento = servicios.consultarDocumento(oidCertificado);
                    fichero = lan6Documento.getContenido();
                    mimeTypeContent = getMimeTypeFromExtension(lan6Documento.getFormat());
                    nombreFichero = lan6Documento.getNombre();
                    extensionFichero = ConstantesDatos.DOT + lan6Documento.getFormat();
                } catch (Lan6Excepcion ex) {
                    log.error("Error Lan6Excepcion  al consultar documento externo en Dokusi", ex);
                    throw new Exception(ConstantesGestionErroresDokusi.COD_FLEX_ERROR_GETDOC_EXT);
                }
                if (fichero != null) {
                    BufferedOutputStream bos = null;
                    try {
                        log.info("Parametros para crearcrearNuevaGestionError el fichero en Outputstring: " + nombreFichero
                                + " Extension : " + extensionFichero
                                + " MimeType : " + mimeTypeContent);
                        response.setContentType(mimeTypeContent);
                        response.setHeader("Content-Disposition", "attachment; filename=" + nombreFichero + extensionFichero);
                        response.setHeader("Content-Transfer-Encoding", "binary");
                        log.info("fichero.length : " + fichero.length);
                        ServletOutputStream out = response.getOutputStream();
                        response.setContentLength(fichero.length);
                        bos = new BufferedOutputStream(out);
                        bos.write(fichero, 0, fichero.length);
                        bos.flush();
                        bos.close();
                        log.info(" *** Termina de leer y escribir el Fichero ***  : " + nombreFichero);
                    } catch (IOException e) {
                        if (log.isDebugEnabled()) {
                            log.error("Excepcion en catch de descargarCertificado", e);
                        }
                        throw e;
                    } finally {
                        if (bos != null) {
                            bos.close();
                        }
                    }
                } else {
                    log.error("FICHERO NULO EN descargarDocumentoDokusiGError - No se ha recuperado ningun documento de Dokusi");
                }
            }
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " -  Error al descargar un documento  ", ex);
            throw ex;
        }
    }

    public void generarExcelPersDiscp(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en generarExcelPersDiscp de " + this.getClass().getSimpleName());
        String rutaArchivoSalida = null;
        Connection con = null;
        List<DiscapacitadoVO> lista = null;
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) {
                        idioma = usuario.getIdioma();
                    }
                }
            } catch (Exception ex) {

            }
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            MeLanbide58Manager manager = MeLanbide58Manager.getInstance();

            String dni = request.getParameter("dni");
            String nombre = request.getParameter("nombre");
            String apellidos = request.getParameter("apellidos");
            String tipoD = request.getParameter("tipoD");
            String grado = request.getParameter("grado");
            String severa = request.getParameter("severa");
            String validez = request.getParameter("validez");
            String centro = request.getParameter("centro");
            String territorio = request.getParameter("territorio");
            log.debug("dni " + dni);
            log.debug("nombre " + nombre);
            log.debug("apellidos" + apellidos);
            log.debug("tipo " + tipoD);
            log.debug("grado " + grado);
            log.debug("severa " + severa);
            log.debug("validez " + validez);
            log.debug("centro  " + centro);
            log.debug("territorio " + territorio);
            try {
                con = adapt.getConnection();
                lista = manager.getPersonasBusqueda(dni, nombre, apellidos, tipoD, grado, severa, validez, centro, territorio, adapt);
                if (lista.isEmpty()) {
                    meLanbide58I18n.getMensaje(idioma, "error.errorGen");
                }
            } catch (Exception ex) {
                log.debug("Error recuperando datos de Pers_Discp : " + ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }

            FileInputStream istr = null;
            try {
                HSSFWorkbook libro = new HSSFWorkbook();
                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try {
                    hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                    if (hssfColor == null) {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                } catch (Exception e) {
                }

                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negrita.setFontHeight((short) 200);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short) 150);

                int numFila = 0;
                int contCelda = 0;
                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCentrado = null;
                HSSFCellStyle estiloIzqierda = libro.createCellStyle();
                HSSFCellStyle estiloCabecera = libro.createCellStyle();
                HSSFSheet hoja = libro.createSheet("Registro Personas con Discapacidad");

                //Se establece el ancho de cada columnas
                //hoja.setColumnWidth((short)0, (short)1500);//ID
                hoja.setColumnWidth(0, 3000);//DNI
                hoja.setColumnWidth(1, 4000);//nombre
                hoja.setColumnWidth(2, 6000);//apellidos
                hoja.setColumnWidth(3, 3000);//tipoD
                hoja.setColumnWidth(4, 3000);//grado
                hoja.setColumnWidth(5, 4000);//Fecha emisión certificado discapacidad
                hoja.setColumnWidth(6, 3000);//Fecha Resolución 
                hoja.setColumnWidth(7, 3000);//Validez 
                hoja.setColumnWidth(8, 3500);//Fecha caducidad
                hoja.setColumnWidth(9, 5000);//severa
                hoja.setColumnWidth(10, 5000);//Fecha validación)
                hoja.setColumnWidth(11, 3000);//Fecha de baja
                hoja.setColumnWidth(12, 4000);//OID
                hoja.setColumnWidth(13, 4000);//nombre Doc
                hoja.setColumnWidth(14, 4000);//centro
                hoja.setColumnWidth(15, 4000);//territorio

                fila = hoja.createRow(numFila);
                //Se establece el alto de las columnas
                fila.setHeight((short) 750);

                estiloCentrado = libro.createCellStyle();
                estiloCentrado.setWrapText(true);
                estiloCentrado.setFont(normal);
                estiloCentrado.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCentrado.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCentrado.setBorderTop(HSSFCellStyle.BORDER_THICK);

                estiloIzqierda.setWrapText(true);
                estiloIzqierda.setFont(normal);
                estiloIzqierda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloIzqierda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                estiloIzqierda.setBorderTop(HSSFCellStyle.BORDER_THICK);

                estiloCabecera.setWrapText(true);
                estiloCabecera.setFont(negrita);
                estiloCabecera.setAlignment(HSSFCellStyle.ALIGN_CENTER);

                fila = hoja.createRow(numFila++);
                //celda en blanco
                hoja.setColumnWidth(contCelda, (short) 3000);
                celda = fila.createCell(contCelda++);

                hoja.setColumnWidth(contCelda, (short) 4000);
                celda = fila.createCell(contCelda++);
                //   celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "excel.expediente"));
                celda.setCellStyle(estiloCabecera);

                hoja.setColumnWidth(contCelda, (short) 6000);
                celda = fila.createCell(contCelda++);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "excel.encabezado"));
                celda.setCellStyle(estiloCabecera);
                contCelda += 2;
                celda = fila.createCell(contCelda);
                numFila++;
                fila = hoja.createRow(numFila);

                crearEstiloInformePersDiscp(libro, fila, celda, estiloCentrado, idioma);

                //Insertamos los datos, fila a fila
                for (DiscapacitadoVO filaI : lista) {
                    numFila++;
                    fila = hoja.createRow(numFila);
                    //DNI
                    celda = fila.createCell(0);
                    celda.setCellValue(filaI.getDni() != null ? String.valueOf(filaI.getDni()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //nombre
                    celda = fila.createCell(1);
                    celda.setCellValue(filaI.getNombre() != null ? filaI.getNombre().toUpperCase() : "");
                    celda.setCellStyle(estiloIzqierda);

                    //apellidos
                    celda = fila.createCell(2);
                    celda.setCellValue(filaI.getApellidos() != null ? filaI.getApellidos().toUpperCase() : "");
                    celda.setCellStyle(estiloIzqierda);

                    //tipoD
                    celda = fila.createCell(3);
                    celda.setCellValue(filaI.getDescTipoDisc() != null ? getDescripcionDesplegable(request, filaI.getDescTipoDisc()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //grado
                    celda = fila.createCell(4);
                    celda.setCellValue(filaI.getPorcDisc() != null ? String.valueOf(filaI.getPorcDisc()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //Fecha emisión certificado discapacidad
                    celda = fila.createCell(5);
                    celda.setCellValue(filaI.getFechEmision() != null ? dateFormat.format(filaI.getFechEmision()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //Fecha Resolución
                    celda = fila.createCell(6);
                    celda.setCellValue(filaI.getFechResolucion() != null ? dateFormat.format(filaI.getFechResolucion()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //Validez
                    celda = fila.createCell(7);
                    celda.setCellValue(filaI.getDescValidez() != null ? getDescripcionDesplegable(request, filaI.getDescValidez()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //Fecha caducidad
                    celda = fila.createCell(8);
                    celda.setCellValue(filaI.getFechCaducidad() != null ? dateFormat.format(filaI.getFechCaducidad()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //severa
                    celda = fila.createCell(9);
                    celda.setCellValue(filaI.getDescDiscSevera() != null ? getDescripcionDesplegable(request, filaI.getDescDiscSevera()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //Fecha validación)
                    celda = fila.createCell(10);
                    celda.setCellValue(filaI.getFechValidacion() != null ? dateFormat.format(filaI.getFechValidacion()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //Fecha de baja
                    celda = fila.createCell(11);
                    celda.setCellValue(filaI.getFechBaja() != null ? dateFormat.format(filaI.getFechBaja()) : "-");
                    celda.setCellStyle(estiloCentrado);

                    //OID
                    celda = fila.createCell(12);
                    celda.setCellValue(filaI.getOidCertificado() != null ? String.valueOf(filaI.getOidCertificado()) : "");
                    celda.setCellStyle(estiloIzqierda);

                    //nombre Doc
                    celda = fila.createCell(13);
                    celda.setCellValue(filaI.getNombreCertificado() != null ? String.valueOf(filaI.getNombreCertificado()) : "");
                    celda.setCellStyle(estiloIzqierda);

                    //centro
                    celda = fila.createCell(14);
                    celda.setCellValue(filaI.getCentro() != null ? String.valueOf(filaI.getCentro()) : "");
                    celda.setCellStyle(estiloCentrado);

                    //territorio
                    celda = fila.createCell(15);
                    celda.setCellValue(filaI.getDescTerritorio() != null ? String.valueOf(filaI.getDescTerritorio()) : "");
                    celda.setCellStyle(estiloCentrado);
                }
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("Registro_Pers_Discp", ".xlsx", directorioTemp);
                FileOutputStream archivoSalida = new FileOutputStream(informe);
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();

                istr = new FileInputStream(rutaArchivoSalida);
                BufferedInputStream bstr = new BufferedInputStream(istr); // promote

                int size = (int) informe.length();
                byte[] data = new byte[size];
                bstr.read(data, 0, size);
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();
            } catch (IOException ioe) {
                log.error("EXCEPCION informe generarExcelPersDiscp");
            } finally {
                if (istr != null) {
                    istr.close();
                }
            }
        } catch (IOException ex) {
            log.error("EXCEPCION informe generarExcelPersDiscp");
        } catch (SQLException ex) {
            log.error("EXCEPCION informe generarExcelPersDiscp");
        }

    }

    private void crearEstiloInformePersDiscp(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma) {
        log.info(">>>> ENTRA en crearEstiloInformePersDiscp de " + this.getClass().getSimpleName());
        try {
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short) 170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.
                //cabeceras
                //DNI
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // nombre                
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // apellidos
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                //Tipo
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                //grado
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // emision
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // resolucion
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                //validez
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // caducidad
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // severa
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // validacion
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // baja
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // oid
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // certi
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col14").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // nif centro
                celda = fila.createCell(14);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col16").toUpperCase());
                celda.setCellStyle(estiloCelda);

                // tthh
                celda = fila.createCell(15);
                celda.setCellValue(meLanbide58I18n.getMensaje(idioma, "consPersDiscp.tablaResultados.col17").toUpperCase());
                celda.setCellStyle(estiloCelda);

            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
    }

    /**
     * Operacion encargada de procesar los anexos A y C del expediente para
     * validar los datos de discapacidad Devuelve el CODIGO_OPERACION Posibles
     * códigos:
     *
     * 0 -> sin fallos
     *
     * 1 -> Excepción al procesar Anexos
     *
     * 2 -> error al crear la conexión a la BBDD
     *
     * 3 -> ERROR al grabar el resultado de la Operación Ejecutada
     *
     * 4 -> ERROR al marcar Operación Ejecutada
     *
     * 5 -> fallo en la operación - procesosKO
     *
     * 6 -> fallo en la operación - genericoKO
     *
     * @param codOrganizacion Organizacion
     * @param codTramite codigo tramite
     * @param ocurrenciaTramite ocurrencia tramite
     * @param numExpediente expediente
     * @param request
     * @param response
     * @return String codigoOperacion
     */
    public String procesarAnexos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>>  Entra en procesarAnexos");
        int procesosKO = 0;
        int genericoKO = 0;
        String codigoOperacion = "0";

        String sustituyeDNIs = "0";
        String actualizaSevera = "0";
        String procesaAltas = "0";
        String procesaBajas = "0";
        String procesaC = "0";

        ArrayList respuestaDNI = new ArrayList();
        ArrayList respuestaSevera = new ArrayList();
        ArrayList respuestaAltas = new ArrayList();
        ArrayList respuestaBajas = new ArrayList();
        ArrayList respuestaPlantilla = new ArrayList();

        StringBuilder mensajeOK = new StringBuilder();
        StringBuilder mensajeKO = new StringBuilder();
        StringBuilder mensajeFinal = new StringBuilder();
        String numExp = null;

        AdaptadorSQLBD adapt = null;
        try {
            adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
        } catch (SQLException e) {
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            codigoOperacion = "2";
        }
        numExp = request.getParameter("numExp");
        try {
            mensajeFinal.append("RESULTADO PROCESO&#10;");

            // sustituir DNIs
            respuestaDNI = limpiarDNIs(numExp, adapt);
            if (!respuestaDNI.isEmpty()) {
                sustituyeDNIs = respuestaDNI.get(0).toString();
                if (sustituyeDNIs.equalsIgnoreCase("0")) {
                    log.info("=================>>> sustituyeDNIs finalizado OK.");
                    mensajeOK.append(respuestaDNI.get(1)).append(" &#10;");
                } else if (sustituyeDNIs.equalsIgnoreCase("1")) {
                    log.info("=================>>> El expediente no tiene DNIs incorrectos.");
                    // mensaje.append("&#10; El expediente no tiene DNIs incorrectos. ");               
                } else if (sustituyeDNIs.equalsIgnoreCase("2")) {
                    log.error("Ha ocurrido un error genérico al corregir DNIs.");
                    genericoKO++;
                    mensajeKO.append(respuestaDNI.get(1)).append(" &#10;");
                    mensajeKO.append("Ha ocurrido un error genérico al corregir DNIs.&#10;");
                } else {
                    procesosKO++;
                    mensajeKO.append(" ===>>>  Resultado al corregir DNIs: &#10;");
                    mensajeKO.append(respuestaDNI.get(1)).append(" &#10;");
                    // mensajeKO.append(sustituyeDNIs).append(" &#10; ");
                    sustituyeDNIs = "3";
                }
            } else {
                //respuesta vacia
                genericoKO++;
                mensajeKO.append("No hay respuesta al corregir DNIs. &#10;");
                sustituyeDNIs = "4";
            }

            // actualizar ES SEVERA
            respuestaSevera = procesarSevera(codOrganizacion, numExp, adapt);
            if (!respuestaSevera.isEmpty()) {
                actualizaSevera = respuestaSevera.get(0).toString();
                if (actualizaSevera.equalsIgnoreCase("0")) {
                    log.info("=================>>>  Actualizada Discapacidad severa - OK.  &#10;");
                    mensajeOK.append(respuestaSevera.get(1)).append(" &#10;");
                } else if (actualizaSevera.equalsIgnoreCase("1")) {
                    genericoKO++;
                    mensajeKO.append("Ha ocurrido un error genérico al actualizar Discapacidad severa. &#10; ");
                } else if (actualizaSevera.equalsIgnoreCase("2")) {
                    genericoKO++;
                    mensajeKO.append("Ha ocurrido un error en BBDD al actualizar Discapacidad severa. &#10;");
                } else {
                    procesosKO++;
                    mensajeKO.append(" &#10;ERRORES al actualizar la Discapacidad Severa: &#10;");
                    mensajeKO.append(respuestaSevera.get(1)).append(" &#10;");
                    actualizaSevera = "3";
                }
            } else {
                genericoKO++;
                mensajeKO.append("No hay respuesta al actualizar ES SEVERA.&#10; ");
                actualizaSevera = "4";
            }

            // Anexo A-ALTAS
            respuestaAltas = procesarAnexoAltas(numExp, adapt);
            if (!respuestaSevera.isEmpty()) {
                procesaAltas = respuestaAltas.get(0).toString();
                if (procesaAltas.equalsIgnoreCase("0")) {
                    log.info("=================>>>  Anexo A ALTAS procesado");
                    mensajeOK.append(respuestaAltas.get(1)).append(" &#10;");
                } else if (procesaAltas.startsWith("1")) {
                    log.info("El expediente " + numExp + " no tiene datos en el Anexo A ALTAS");
                    mensajeOK.append("El expediente no tiene datos en el Anexo A-ALTAS.&#10;");
                } else if (procesaAltas.equalsIgnoreCase("2")) {
                    genericoKO++;
                    mensajeKO.append(respuestaAltas.get(1)).append(" &#10;");
                    mensajeKO.append("Ha ocurrido un error genérico al procesar el Anexo A-ALTAS.&#10;");
                } else {
                    log.error("Error al procesar Anexo A ALTAS --- ");
                    procesosKO++;
                    mensajeKO.append(" &#10;===>>> ANEXO A-ALTAS:&#10;");
                    mensajeKO.append(respuestaAltas.get(1)).append(" &#10;");
                    procesaAltas = "3";
                }
            } else {
                genericoKO++;
                mensajeKO.append("No hay respuesta al procesar Anexo A-Altas.&#10; ");
                procesaAltas = "4";
            }

            // Anexo A-BAJAS
            respuestaBajas = procesarAnexoBajas(numExp, adapt);
            if (!respuestaBajas.isEmpty()) {
                procesaBajas = respuestaBajas.get(0).toString();
                if (procesaBajas.equalsIgnoreCase("0")) {
                    log.info("=================>>>  Anexo A BAJAS procesado");
                    mensajeOK.append(respuestaBajas.get(1)).append(" &#10;");
                } else if (procesaBajas.startsWith("1")) {
                    log.info("El expediente " + numExp + " no tiene datos en el Anexo A Bajas");
                    mensajeOK.append("El expediente no tiene datos en el Anexo A-BAJAS. &#10; ");
                } else if (procesaBajas.equalsIgnoreCase("2")) {
                    genericoKO++;
                    mensajeKO.append(respuestaBajas.get(1)).append(" &#10;");
                    mensajeKO.append("Ha ocurrido un error genérico al procesar el Anexo A-BAJAS.&#10;");
                } else {
                    log.error("Incidencias al procesar Anexo A BAJAS --- ");
                    procesosKO++;
                    mensajeKO.append(" &#10;===>>>  ANEXO A-BAJAS:&#10;");
                    mensajeKO.append(respuestaBajas.get(1)).append(" &#10;");
                    procesaBajas = "3";
                }
            } else {
                procesosKO++;
                mensajeKO.append("No hay respuesta al procesar Anexo A-Bajas.&#10; ");
                procesaBajas = "4";
            }
// #492267 - No procesar Anexo C 
            // VALIDADA = S (anexo C menos A-Alta y A_Bajas
            respuestaPlantilla = validarAnexoCPlantilla(numExp, adapt);

            //  respuestaPlantilla = procesarAnexoCPlantilla(codOrganizacion, numExp, adapt);
            if (!respuestaPlantilla.isEmpty()) {
                procesaC = respuestaPlantilla.get(0).toString();
                if (procesaC.equalsIgnoreCase("0")) {
                    log.info("=================>>>  Anexo C - PLANTILLA procesado");
                    mensajeOK.append(respuestaPlantilla.get(1)).append(" &#10;");
                } else if (procesaC.equalsIgnoreCase("1")) {
                    genericoKO++;
                    mensajeKO.append("Ha ocurrido un error genérico al procesar el Anexo C - PLANTILLA.&#10;");
                } else {
                    log.error("Incidencias al procesar Anexo C - PLANTILLA --- ");
                    procesosKO++;
                    mensajeKO.append(" &#10;===>>> ANEXO C - PLANTILLA: &#10;");
                    mensajeKO.append(respuestaPlantilla.get(1)).append(" &#10; ");
                    procesaC = "3";
                }
            } else {
                procesosKO++;
                mensajeKO.append("No hay respuesta al procesar Anexo C-Plantilla.&#10; ");
                procesaBajas = "4";
            }

            // control resultado
            if (procesosKO == 0) {
                if (genericoKO == 0) {
                    codigoOperacion = "0";
                    mensajeOK.append("Se han ejecutado correctamente las operaciones.&#10;");
                } else {
                    codigoOperacion = "6";
                }
            } else {
                codigoOperacion = "5";
            }

            if (mensajeOK.length() > 0) {
                mensajeFinal.append(" &#10;=================>>>  CORRECTO: &#10;").append(mensajeOK);
            }
            if (mensajeKO.length() > 0) {
                mensajeFinal.append(" &#10;=================>>>  A REVISAR: &#10;").append(mensajeKO);
            }
            try {// grabar resultado
                if (!MeLanbide58Manager.getInstance().grabarResultadoProcesar(codOrganizacion, numExp, mensajeFinal.toString(), adapt)) {
                    log.error(" ERROR al grabar el resultado de la Operación Ejecutada");
                    codigoOperacion = "3";
                }
            } catch (Exception e) {
                log.error(" Excepción al grabar el resultado de la Operación Ejecutada");
                codigoOperacion = "3";
            }

            try {// marcar procesado
                if (!MeLanbide58Manager.getInstance().grabarAnexosProcesados(codOrganizacion, numExp, adapt)) {
                    log.error(" ERROR al marcar Operación Ejecutada&");
                    codigoOperacion = "4";
                }
            } catch (Exception e) {
                log.error("Excepción al marcar Operación Ejecutada&");
                codigoOperacion = "4";
            }

        } catch (Exception e) {
            log.error("Excepción al procesar Anexos --- " + e);
            codigoOperacion = "1";
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            resultado.setAtributo("error", codigoOperacion);
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
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

    /**
     * Operacion que se encarga de eliminar signos y espacios y convierte a
     * Mayusculas las minusculas procesa las tablas de los anexos del modulo 58
     * 1 - A Altas 2 - A Bajas 3 - B SMI - ojo IDSMI 4 - C Plantilla
     *
     * @param numExp expediente
     * @param adapt adaptador
     * @return codigo de la operacion y resultado 0 - Correcto 1 - No hay DNIs
     * incorrectos 2 - Error generico 3 - Error BBDD
     */
    private ArrayList /*String*/ limpiarDNIs(String numExp, AdaptadorSQLBD adapt) {
        log.info("Entra en limpiarDNIs " + numExp);
        ArrayList respuesta = new ArrayList();
        String codOperacion = "0";
        StringBuilder resultado = new StringBuilder();
        Map<String, String> regIncorrectos = new HashMap<String, String>();
        MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
        // 
        try {
            for (int tabla = 1; tabla < 4; tabla++) {
                log.debug("Tabla " + tabla);
                regIncorrectos = meLanbide58Manager.getDocIncorrectosAnexos(numExp, tabla, adapt);
                if (!regIncorrectos.isEmpty()) {
                    if (regIncorrectos.containsKey("X")) {
//                   falta tabla - no deberia pasar
                        codOperacion = "2";
                        resultado.append("El código  ").append(tabla).append(" no es correcto.");
                    } else if (regIncorrectos.containsKey("0")) {
                        // no hay
                        log.debug("No hay DNIs incorrectos");
                        resultado.append("No hay DNIs incorrectos");
                        codOperacion = "1";
                    } else {
                        String id = null;
                        String dniInc = null;
                        String dniOK = null;
                        for (Map.Entry<String, String> incorrecto : regIncorrectos.entrySet()) {
                            id = incorrecto.getKey();
                            dniInc = incorrecto.getValue();
                            try {
                                dniOK = dniInc.replaceAll("[^\\dA-Za-z]", "").toUpperCase();
                                if (meLanbide58Manager.grabarDniCorrecto(numExp, id, dniOK, tabla, adapt)) {
                                    log.info("Se ha sustituido en la tabla " + tabla + " el DNI " + dniInc + " por " + dniOK + " en el expediente " + numExp);
                                } else {
                                    // error al grabar
                                    resultado.append("Error al sustituir en ").append(tabla).append(" el DNI ").append(dniInc).append(".&#10;");
                                    log.error("Ha ocurrido un error al sustituir en la tabla " + tabla + " el DNI " + dniInc + " por " + dniOK + " en el expediente " + numExp);
                                    codOperacion = "3";
                                }
                            } catch (Exception e) {
                                log.error("EXCEPCIÓN al sustituir en la tabla " + tabla + " el DNI " + dniInc + " en el expediente " + numExp, e);
                                resultado.append("EXCEPCIÓN al sustituir en la tabla ").append(tabla).append(" el DNI ").append(dniInc).append(".&#10;");
                                codOperacion = "2";
                            }
                        } // for
                    }
                } else {
                    log.debug("No hay DNIs incorrectos ");
                    resultado.append("No hay DNIs incorrectos");
                    codOperacion = "1";
                }
            }
        } catch (Exception ex) {
            log.error("Excepcion en limpiarDNIs ", ex);
            resultado.append("Excepcion en limpiarDNIs ").append(ex).append(".&#10;");
            codOperacion = "2";
        }

        if (codOperacion.equalsIgnoreCase("0")) {
            resultado.append("Limpiar DNIs incorrectos - OK.&#10;");
        }
        respuesta.add(codOperacion);
        respuesta.add(resultado);
        return respuesta;
    }

    /**
     * Operacion que calcula la severidad de la discapacidad para todas las
     * personas del Anexo-C
     *
     * @param codOrganizacion
     * @param numExp numero expediente
     * @param adapt
     * @return String con el resultado 0 - Correcto 1 - Error generico 2 - Error
     * BBDD Mensaje con descripcion de errores
     */
    private ArrayList procesarSevera(int codOrganizacion, String numExp, AdaptadorSQLBD adapt) {
        log.info(">>>>>>>  Entra en procesaSevera");
        ArrayList respuesta = new ArrayList();
        String codOperacion = "0";
        StringBuilder resultado = new StringBuilder();
        boolean correcto = true;
        String dni = null;
        int id = 0;
        String severa = "";
        List<PlantillaVO> listaC = new ArrayList<PlantillaVO>();
        Connection con = null;
        MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();

        try {
            listaC = meLanbide58Manager.getPlantilla(numExp, codOrganizacion, adapt);
            if (!listaC.isEmpty()) {
                try {
                    con = adapt.getConnection();
                } catch (BDException e) {
                    log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                    resultado.append("Error al obtener una conexión a la BBDD.&#10;");
                    codOperacion = "2";
                }
                for (PlantillaVO fila : listaC) {
                    severa = "-";
                    dni = fila.getNif_Dni();
                    id = fila.getId();
                    if (fila.getTipoDis() != null) {
                        if (fila.getGrado() != null) {
                            if (esSevera(fila.getTipoDis(), fila.getGrado())) {
                                severa = "S";
                                log.debug("Es SEVERO");
                            } else {
                                severa = "N";
                                log.debug("NO es SEVERO");
                            }
                        } else if (fila.getTipoDis().equalsIgnoreCase("P") || fila.getTipoDis().equalsIgnoreCase("PA") || fila.getTipoDis().equalsIgnoreCase("PG") || fila.getTipoDis().equalsIgnoreCase("PT")) {
                            severa = "N";
                            log.debug("NO es SEVERO");
                        } else {
                            correcto = false;
                            log.error("Falta el grado de discapacidad en la persona con DNI " + dni);
                            resultado.append(" Falta el grado de discapacidad en Anexo C - ").append(dni).append(".&#10;");
                        }

                        boolean actualizaOK = meLanbide58Manager.actualizaDatoDiscSevera(numExp, id, dni, severa, con);
                        if (!actualizaOK) {
                            correcto = false;
                            log.error("Error al actualizar la discapacidad SEVERA del registro con DNI " + dni);
                            resultado.append("Error al actualizar la discapacidad SEVERA del registro con DNI ").append(dni).append(". &#10;");
                        }
                    } else {
                        correcto = false;
                        log.error("Falta el tipo de discapacidad en la persona con DNI " + dni);
                        resultado.append("Falta el tipo de discapacidad en Anexo C - ").append(dni).append(". &#10;");
                    }
                } //FOR
            } else {
                correcto = false;
                log.error("Error al recuperar la Plantilla del expediente " + numExp);
                resultado.append("No se han recuperado los datos de la Plantilla del expediente  ").append(numExp).append(". &#10;");
            }
            if (!correcto) {
                codOperacion = "3";
            } else {
                resultado.append("Actualizar Discapacidad Severa - OK. &#10;");
            }
            log.info("=================>>>  ES SEVERA actualizado");
        } catch (Exception ex) {
            log.error("Error al Actualizar ES SEVERA --- ", ex);
            resultado.append("Error al Actualizar ES SEVERA --- ").append(". &#10;");
            codOperacion = "1";
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                resultado.append("Error al cerrar conexion a la BBDD");
                codOperacion = "2";
            }
        }
        respuesta.add(codOperacion);
        respuesta.add(resultado);
        return respuesta;
    }

    /**
     * Operacion que procesa los datos de las personas presentes en el Anexo-A
     * ALTAS para validar los datos de discapacidad
     *
     * @param numExp
     * @param adapt
     * @return String con el resultado 0 - Correcto 1 - no tiene datos en el
     * Anexo A Altas 2 - Error generico Mensaje con descripcion de errores
     */
    private ArrayList procesarAnexoAltas(String numExp, AdaptadorSQLBD adapt) {
        log.info("Entramos en procesarAnexoAltas de " + this.getClass().getSimpleName() + " - " + numExp);

        ArrayList respuesta = new ArrayList();
        String codOperacion = "0";
        StringBuilder resultado = new StringBuilder();
        String validada = "S";
        String faltanDatos = "N";
        boolean esBaja58 = false;
        boolean esBajaPD = false;
        boolean correcto = true;
        boolean insertado = false;
        DiscapacitadoVO personaDisc = new DiscapacitadoVO();
        PlantillaVO personaAnexo = new PlantillaVO();
        Map<String, Date> fechasAltas = new HashMap<String, Date>();
        MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
        try {
            fechasAltas = meLanbide58Manager.getFechasAltas(numExp, adapt);
            if (!fechasAltas.isEmpty()) {
                log.info("El expediente " + numExp + "  tiene datos en el Anexo A Altas");
                for (Map.Entry<String, Date> registro : fechasAltas.entrySet()) {
                    String dniAlta = registro.getKey();
                    if (meLanbide58Manager.existeAnexoC(numExp, dniAlta, adapt)) {
                        faltanDatos = "N";
                        validada = "S";
                        esBaja58 = false;
                        esBajaPD = false;
                        insertado = false;
                        // correcto = true;
                        // OJO DNIs no validados - Elimino caracteres no admitidos
                        String dniLimpio = dniAlta.replaceAll("[^\\dA-Za-z]", "").toUpperCase();

                        personaAnexo = meLanbide58Manager.getPersonaAnexoCPorDNI(dniAlta, numExp, adapt);
                        Date fechaAlta = registro.getValue();
                        Date fechaBaja = meLanbide58Manager.getUltimaFechaABajas(numExp, dniAlta, adapt);
                        Date anteriorValidez = null;
                        if (personaAnexo.getFecCertificado() != null) {
                            anteriorValidez = sumarRestarDiasFecha(personaAnexo.getFecCertificado(), -1);
                            if ((fechaBaja != null) && (fechaBaja.after(fechaAlta))) {
                                // baja en 58
                                esBaja58 = true;
                                log.info("Es baja 58: " + fechaBaja);
                            }
                            int codigo = meLanbide58Manager.existePersonaDisc(dniLimpio, adapt);
                            if (codigo != 0) {
                                // existe en PD
                                log.info("Cargo los datos en PersonaVO con ID: " + codigo);
                                personaDisc = meLanbide58Manager.getPersonaDiscp(codigo, dniLimpio, adapt);
                                if (personaDisc.getTipoDisc() != null && personaDisc.getPorcDisc() != null) {
                                    if (personaDisc.getFechBaja() != null) {
                                        esBajaPD = true;
                                        log.info("F Baja en PD: " + personaDisc.getFechBaja());
                                    } else {
                                        log.info("Es ALTA PD");
                                    }

                                    if (!personaAnexo.getTipoDis().equalsIgnoreCase(personaDisc.getTipoDisc()) || personaAnexo.getGrado().compareTo(personaDisc.getPorcDisc()) != 0) {
                                        log.info("Cambian datos");
                                        correcto = false;
                                        validada = "N";
                                        if (personaAnexo.getFecCertificado().compareTo(personaDisc.getFechEmision()) == 0) {
                                            resultado.append("--> No coinciden el tipo o el grado - DNI: ").append(dniLimpio).append(". &#10; ");
                                        } else {
                                            resultado.append("--> No coinciden el tipo o el grado ni la fecha del certificado - DNI: ").append(dniLimpio).append(". &#10; ");
                                        }
                                        if (!esBaja58) {
                                            if (!esBajaPD) {
                                                // 1 ALTA expediente ALTA registro
                                            } else {
                                                // 3 ALTA expediente BAJA registro
                                            }
                                        } else {
                                            if (!esBajaPD) {
                                                // 2 BAJA expediente ALTA registro
                                            } else {
                                                // 4 BAJA expediente BAJA registro
                                            }
                                        }
                                    } else {
                                        // NO CAMBIAN DATOS
                                        log.info("NO cambian datos");
                                        if (!esBaja58) {
                                            validada = "S";
                                            if (!esBajaPD) {
                                                // 1 ALTA expediente ALTA registro
                                            } else {
                                                // 3 ALTA expediente BAJA registro
                                                // borra
                                                try {
                                                    boolean borrarFeBajaPD = meLanbide58Manager.borrarFechaBajaDisc(codigo, dniLimpio, adapt);
                                                    if (borrarFeBajaPD) {
                                                        log.info("F Baja borrada para la persona con DNI " + dniLimpio);
                                                    } else {
                                                        log.error("Ha ocurrido un error borrando la F Baja" + dniLimpio);
                                                        correcto = false;
                                                        resultado.append(" Ha ocurrido un error borrando la F Baja en el REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                                    }
                                                } catch (Exception e) {
                                                    log.error("Excepción borrando la F Baja" + dniLimpio + " - " + e);
                                                    correcto = false;
                                                    resultado.append(" Ha ocurrido un error borrando la F Baja en el REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                                }
                                            }
                                        } else {
                                            if (!esBajaPD) {
                                                // 2 BAJA expediente ALTA registro
                                                // grabo fecha
                                                try {
                                                    boolean grabaFecha = meLanbide58Manager.grabaFechaBaja(codigo, dniLimpio, fechaBaja, adapt);
                                                    if (grabaFecha) {
                                                        log.info("F Baja grabada para la persona con DNI " + dniLimpio);
                                                    } else {
                                                        log.error("Ha ocurrido un error grabando la F Baja " + dniLimpio);
                                                        correcto = false;
                                                        resultado.append(" Ha ocurrido un error grabando la F Baja ").append(fechaBaja).append(" en el registro. DNI: ").append(dniLimpio).append(" &#10;");
                                                    }
                                                } catch (Exception e) {
                                                    log.error("Excepción grabando la F Baja " + dniLimpio + " - " + e);
                                                    correcto = false;
                                                    resultado.append("Ha ocurrido un error grabando la F Baja ").append(fechaBaja).append(" en el registro. DNI: ").append(dniLimpio).append(" &#10;");
                                                }
                                            } else {
                                                // 4 BAJA expediente BAJA registro
                                            }
                                        }
                                    }
                                } else {
                                    faltanDatos = "S";
                                    validada = "N";
                                    correcto = false;
                                    resultado.append(" Falta Tipo o Grado en el REGISTRO - ").append(dniLimpio).append("&#10;");
                                }
                            } else {
                                // no existe
                                log.info("La persona con DNI " + dniLimpio + " no figura en la tabla PERS_DISCP");
                                // nuevo
                                personaDisc.setDni(dniLimpio);
                                personaDisc.setApellidos(personaAnexo.getApellidos());
                                personaDisc.setNombre(personaAnexo.getNombre());
                                personaDisc.setTipoDisc(personaAnexo.getTipoDis());
                                personaDisc.setPorcDisc(personaAnexo.getGrado());
                                if (personaAnexo.getDiscSevera() != null) {
                                    personaDisc.setDiscSevera(personaAnexo.getDiscSevera());
                                }
                                personaDisc.setFechEmision(personaAnexo.getFecCertificado());
                                personaDisc.setFechResolucion(personaAnexo.getFecCertificado());
                                try {
                                    insertado = meLanbide58Manager.nuevaPersonaDisc(personaDisc, adapt);
                                    if (insertado) {
                                        log.info("Insertada nueva persona con DNI " + dniLimpio + ". FALTAN DATOS");
                                        resultado.append(" Insertada nueva persona en REGISTRO &#10;").append(dniLimpio);
                                    } else {
                                        log.error("Ha ocurrido un error insertado una nueva persona DNI " + dniLimpio);
                                        resultado.append(" Ha ocurrido un error insertado una nueva persona en el REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                    }

                                } catch (Exception e) {
                                    log.error("Excepción insertado una nueva persona DNI " + dniLimpio + " - " + e);
                                    resultado.append(" Ha ocurrido un error insertado una nueva persona en el REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                }
                                correcto = false;
                                faltanDatos = "S";
                                validada = "N";
                            }

                            if (!esBaja58) {
                                if ((personaDisc.getValidez() == null || personaDisc.getValidez().isEmpty()) || (personaDisc.getOidCertificado() == null || personaDisc.getOidCertificado().isEmpty())) {
                                    faltanDatos = "S";
                                    log.debug("Faltan datos");
                                    correcto = false;
                                    resultado.append("Falta Validez y/o Certificado en el REGISTRO - ").append(dniLimpio).append(" &#10; ");
                                } else {
                                    faltanDatos = "N";
                                }
                                // marcar como nueva alta
                                try {
                                    boolean marcado = meLanbide58Manager.marcarNuevaAlta(numExp, dniLimpio, adapt);
                                    if (marcado) {
                                        log.info("Marcada como nueva alta en EXPEDIENTE la persona con DNI " + dniLimpio + ". FALTAN DATOS");
                                    } else {
                                        log.error("Ha ocurrido un error marcando una nueva alta, DNI " + dniLimpio);
                                        validada = "N";
                                        correcto = false;
                                        resultado.append(" Ha ocurrido un error marcando una nueva alta , DNI ").append(dniLimpio).append(" &#10;");
                                    }
                                } catch (Exception e) {
                                    log.error("Excepción  marcando una nueva alta, DNI " + dniLimpio + " - " + e);
                                    validada = "N";
                                    correcto = false;
                                    resultado.append(" Ha ocurrido un error marcando una nueva alta , DNI ").append(dniLimpio).append(" &#10;");
                                }
                            }

                            // comprobar fecha caducidad
                            // Si la persona tiene registrada Fecha de CADUCIDAD en el registro se comprueba si se ha superado la fecha, en caso afirmativo se graba Fecha de Baja en el Registro
                            if (personaDisc.getFechCaducidad() != null) {
                                java.util.Date hoy = new Date();
                                if (!personaDisc.getFechCaducidad().before(hoy)) {
                                    correcto = false;
                                    log.info("Certificado CADUCADO");
                                    try {
                                        boolean grabaBajaCaduca = meLanbide58Manager.grabaFechaBaja(codigo, dniLimpio, personaDisc.getFechCaducidad(), adapt);
                                        if (grabaBajaCaduca) {
                                            log.info("F Baja grabada para la persona con DNI " + dniLimpio);
                                            resultado.append("F Baja grabada en REGISTRO a: ").append(dniLimpio).append("  &#10;");
                                        } else {
                                            log.error("Ha ocurrido un error grabando la F Baja en REGISTRO " + dniLimpio);
                                            resultado.append("Ha ocurrido un error grabando la F Baja en REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                        }
                                    } catch (Exception e) {
                                        log.error("Excepción grabando la F Baja en REGISTRO " + dniLimpio + " - " + e);
                                        resultado.append("Ha ocurrido un error grabando la F Baja en REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                    }
                                }
                            }
                        } else {
                            log.error("La persona con DNI " + dniLimpio + " - Expediente: " + numExp + " no tiene Fecha CERTIFICADO en Anexo C");
                            resultado.append("Falta Fecha  CERTIFICADO en Anexo C - ").append(dniLimpio).append("  &#10;");
                            correcto = false;
                            validada = "N";
                            faltanDatos = "S";
                        }

                        // grabar FALTAN DATOS
                        try {
                            boolean actualizaFalta = meLanbide58Manager.actualizaDatosIncompletos(numExp, dniLimpio, faltanDatos, adapt);
                            if (actualizaFalta) {
                                log.info("Datos Incompletos = " + faltanDatos + " grabado en el expediente " + numExp + " - DNI - " + dniLimpio);
                            } else {
                                log.error("Ha ocurrido un error grabando Datos incompletos " + numExp + " - DNI - " + dniLimpio);
                                resultado.append("Ha ocurrido un error grabando Datos incompletos. DNI: ").append(dniLimpio).append(" &#10;");
                                correcto = false;
                            }
                        } catch (Exception e) {
                            log.error("Excepcion grabando Datos incompletos " + numExp + " - DNI - " + dniLimpio + " - " + e);
                            resultado.append("Ha ocurrido un error grabando Datos incompletos. DNI: ").append(dniLimpio).append(" &#10;");
                            correcto = false;
                        }

                        // grabar VALIDADA
                        try {
                            boolean validado = meLanbide58Manager.actualizaDiscValidada(numExp, dniLimpio, validada, adapt);
                            if (!validado) {
                                log.error("Ha ocurrido un error validando la discapacidad. DNI " + dniLimpio);
                                resultado.append("Ha ocurrido un error grabando Discapacidad Validada. DNI - ").append(dniLimpio).append(" &#10;");
                                correcto = false;
                            }
                        } catch (Exception e) {
                            log.error("Excepción validando la discapacidad. DNI " + dniLimpio + " - " + e);
                            resultado.append("Ha ocurrido un error grabando Discapacidad Validada. DNI - ").append(dniLimpio).append(" &#10;");
                            correcto = false;
                        }
                        // si faltan datos Borrar Importes
                        if (faltanDatos.equalsIgnoreCase("S")) {
                            try {
                                boolean importeBorrado = meLanbide58Manager.borrarImportesSMI(numExp, dniLimpio, adapt);
                                if (importeBorrado) {
                                    log.info("Se ha borrado el importe calculado de " + dniLimpio);
                                } else {
                                    log.error("NO se ha borrado el IMPORTE CALCULADO de " + dniLimpio);
                                    correcto = false;
                                    resultado.append("NO se ha borrado el IMPORTE CALCULADO de  ").append(dniLimpio).append("&#10;");
                                }
                            } catch (Exception e) {
                                log.error("NO se ha borrado el IMPORTE CALCULADO de " + dniLimpio + " - " + e);
                                correcto = false;
                                resultado.append("NO se ha borrado el IMPORTE CALCULADO de  ").append(dniLimpio).append("&#10;");
                            }
                        }
                    } else {
                        log.error("No existe en Anexo C");
                        correcto = false;
                        resultado.append("NO EXISTE en Anexo C  ").append(dniAlta).append("&#10;");
                    }
                } // for
            } else {
                log.info("El expediente " + numExp + " no tiene datos en el Anexo A Altas ");
                resultado.append("- No tiene datos en el Anexo A Altas.&#10;");
                codOperacion = "1";
            }

        } catch (Exception ex) {
            log.error("Error genérico en procesaA-Altas", ex);
            correcto = false;
            resultado.append("Error genérico en procesaA-Altas.&#10;");
            codOperacion = "2";
        }
        if (!correcto) {
            codOperacion = "3";
        } else {
            resultado.append("Anexo A-Altas procesado - OK. &#10;");
        }

        respuesta.add(codOperacion);
        respuesta.add(resultado);
        return respuesta;
    }

    /**
     * Operacion que procesa los datos de las personas presentes en el Anexo-A
     * BAJAS y no estan en el Anexo-A ALTAS para validar los datos de
     * discapacidad
     *
     * @param numExp
     * @param adapt
     * @return String con el resultado 0 - Correcto 1 - no tiene datos en el
     * Anexo A Bajas 2 - Error generico Mensaje con descripcion de errores
     */
    private ArrayList procesarAnexoBajas(String numExp, AdaptadorSQLBD adapt) {
        log.info("=================>>>  Entramos en procesaA-Bajas de " + this.getClass().getSimpleName() + " - " + numExp);

        ArrayList respuesta = new ArrayList();
        String codOperacion = "0";
        StringBuilder resultado = new StringBuilder();
        String validada = "S";
        String faltanDatos = "N";
        boolean esBaja58 = false;
        boolean cambianDatos = false;
        boolean esBajaPD = false;
        boolean correcto = true;
        boolean insertado = false;
        DiscapacitadoVO personaDisc = new DiscapacitadoVO();
        PlantillaVO personaAnexo = new PlantillaVO();
        Map<String, Date> fechasBajas = new HashMap<String, Date>();
        MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
        try {
            fechasBajas = meLanbide58Manager.getFechasBajas(numExp, adapt);
            if (!fechasBajas.isEmpty()) {
                log.info("El expediente " + numExp + "  tiene datos en el Anexo A Bajas");
                for (Map.Entry<String, Date> registro : fechasBajas.entrySet()) {
                    String dniBaja = registro.getKey();
                    if (meLanbide58Manager.existeAnexoC(numExp, dniBaja, adapt)) {
                        faltanDatos = "N";
                        validada = "S";
                        esBaja58 = false;
                        esBajaPD = false;
                        insertado = false;

                        // OJO DNIs no validados - Elimino caracteres no admitidos
                        String dniLimpio = dniBaja.replaceAll("[^\\dA-Za-z]", "").toUpperCase();
                        Date fechaBaja = registro.getValue();
                        Date fechaAlta = null;
                        personaAnexo = meLanbide58Manager.getPersonaAnexoCPorDNI(dniBaja, numExp, adapt);
                        if (personaAnexo.getFecCertificado() != null) {
                            Date anteriorValidez = sumarRestarDiasFecha(personaAnexo.getFecCertificado(), -1);
                            fechaAlta = meLanbide58Manager.getUltimaFechaAAltas(numExp, dniBaja, adapt);
                            if (fechaAlta == null) {
                                esBaja58 = true;
                                log.info("Es baja 58: " + fechaBaja);
                            } else if (fechaAlta.before(fechaBaja)) {
                                esBaja58 = true;
                                log.info("Es baja 58: " + fechaBaja);
                            } else {
                                log.info("Es alta 58: " + fechaAlta);
                            }
                            int codigo = meLanbide58Manager.existePersonaDisc(dniLimpio, adapt);
                            if (codigo != 0) {
                                // existe en PD
                                log.info("Cargo los datos en PersonaVO con ID: " + codigo);
                                personaDisc = meLanbide58Manager.getPersonaDiscp(codigo, dniLimpio, adapt);
                                if (personaDisc.getTipoDisc() != null && personaDisc.getPorcDisc() != null) {
                                    if (personaDisc.getFechBaja() != null) {
                                        esBajaPD = true;
                                        log.info("Es baja PD");
                                    } else {
                                        log.info("Es ALTA PD");
                                    }

                                    if (!personaAnexo.getTipoDis().equalsIgnoreCase(personaDisc.getTipoDisc()) || personaAnexo.getGrado().compareTo(personaDisc.getPorcDisc()) != 0) {
                                        // CAMBIAN DATOS	
                                        cambianDatos = true;
                                        log.info("Cambian datos");
                                        correcto = false;
                                        validada = "N";
                                        if (personaAnexo.getFecCertificado().compareTo(personaDisc.getFechEmision()) == 0) {
                                            resultado.append("--> No coinciden el tipo o el grado - DNI: ").append(dniLimpio).append(". &#10; ");
                                        } else {
                                            resultado.append("--> No coinciden el tipo o el grado ni la fecha del certificado - DNI: ").append(dniLimpio).append(". &#10; ");
                                        }
                                        if (!esBaja58) {
                                            if (!esBajaPD) {
                                                // 1 ALTA expediente ALTA registro
                                            } else {
                                                // 3 ALTA expediente BAJA registro
                                            }
                                        } else {
                                            if (!esBajaPD) {
                                                // 2 BAJA expediente ALTA registro
                                            } else {
                                                // 4 BAJA expediente BAJA registro
                                            }
                                        }
                                    } else {
                                        // NO CAMBIAN DATOS	
                                        log.info("NO cambian datos");
                                        if (!esBaja58) {
                                            validada = "S";
                                            if (esBajaPD) {
                                                // alta en EXPEDIENTE - baja en REGISTRO
                                                // borrar F baja Reg
                                                try {
                                                    boolean borrarFeBajaPD = meLanbide58Manager.borrarFechaBajaDisc(codigo, dniLimpio, adapt);
                                                    if (borrarFeBajaPD) {
                                                        log.info("F Baja borrada para la persona con DNI " + dniLimpio);
                                                    } else {
                                                        log.error("Ha ocurrido un error borrando la F Baja " + dniLimpio);
                                                        correcto = false;
                                                        resultado.append("Ha ocurrido un error borrando la F Baja en el REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                                    }
                                                } catch (Exception e) {
                                                    log.error("Excepción borrando la F Baja " + dniLimpio + " - " + e);
                                                    correcto = false;
                                                    resultado.append("Ha ocurrido un error borrando la F Baja en el REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                                }

                                            } else {
                                                // 1 ALTA expediente ALTA registro
                                            }
                                        } else {
                                            if (!esBajaPD) {
                                                // Baja en EXPEDIENTE - Alta en REGISTRO
                                                // grabar f baja 
                                                try {
                                                    boolean grabaFecha = meLanbide58Manager.grabaFechaBaja(codigo, dniLimpio, fechaBaja, adapt);
                                                    if (grabaFecha) {
                                                        log.info("F Baja grabada para la persona con DNI " + dniLimpio);
                                                    } else {
                                                        log.error("Ha ocurrido un error grabando la F Baja" + dniLimpio);
                                                        correcto = false;
                                                        resultado.append(" Ha ocurrido un error grabando la F Baja en el REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                                    }
                                                } catch (Exception e) {
                                                    log.error("Excepcion grabando la F Baja" + dniLimpio + " - " + e);
                                                    correcto = false;
                                                    resultado.append(" Ha ocurrido un error grabando la F Baja en el REGISTRO. DNI: ").append(dniLimpio).append(" &#10;");
                                                }
                                            } else {
                                                // 4 BAJA expediente BAJA registro
                                            }
                                        }
                                    }
                                } else {
                                    faltanDatos = "S";
                                    validada = "N";
                                    correcto = false;
                                    resultado.append("Falta Tipo o Grado en el REGISTRO - ").append(dniLimpio).append(" &#10;");
                                }

                            } else {
                                // 12 - no existe
                                log.info("La persona con DNI " + dniLimpio + " NO EXISTE en PD");
                                // nuevo
                                personaDisc.setDni(dniLimpio);
                                personaDisc.setApellidos(personaAnexo.getApellidos());
                                personaDisc.setNombre(personaAnexo.getNombre());
                                personaDisc.setTipoDisc(personaAnexo.getTipoDis());
//                                if (personaAnexo.getSubtipo() != null) {
//                                    personaDisc.setSubtipo(personaAnexo.getSubtipo());
//                                }
//                                personaDisc.setPorcDisc(personaAnexo.getGrado());
                                if (personaAnexo.getDiscSevera() != null) {
                                    personaDisc.setDiscSevera(personaAnexo.getDiscSevera());
                                }
                                personaDisc.setFechEmision(personaAnexo.getFecCertificado());
                                personaDisc.setFechResolucion(personaAnexo.getFecCertificado());
                                try {
                                    insertado = meLanbide58Manager.nuevaPersonaDisc(personaDisc, adapt);
                                    if (insertado) {
                                        log.info("Insertada nueva persona con DNI " + dniLimpio + ". FALTAN DATOS");
                                        resultado.append("Insertada nueva persona  en REGISTRO  ").append(dniLimpio).append(". FALTAN DATOS &#10;");
                                    } else {
                                        log.error("Ha ocurrido un error insertado una nueva persona DNI " + dniLimpio);
                                        resultado.append("Ha ocurrido un error insertado en el REGISTRO una nueva persona. DNI: ").append(dniLimpio).append(" &#10;");
                                    }
                                    correcto = false;
                                    faltanDatos = "S";
                                } catch (Exception e) {
                                    log.error("Excepción insertado una nueva persona DNI " + dniLimpio + " - " + e);
                                    resultado.append("Ha ocurrido un error insertado en el REGISTRO una nueva persona. DNI: ").append(dniLimpio).append(" &#10;");
                                }
                                correcto = false;
                                faltanDatos = "S";
                                validada = "N";
                            }

                            if ((personaDisc.getValidez() == null || personaDisc.getValidez().isEmpty()) || (personaDisc.getOidCertificado() == null || personaDisc.getOidCertificado().isEmpty())) {
                                faltanDatos = "S";
                                log.debug("Faltan datos");
                                correcto = false;
                                resultado.append("Falta Validez y/o Certificado en el REGISTRO - ").append(dniLimpio).append(" &#10;");
                            } else {
                                faltanDatos = "N";
                            }
                            // marcar como nueva alta
                            if (!esBaja58) {
                                try {
                                    boolean marcado = meLanbide58Manager.marcarNuevaAlta(numExp, dniLimpio, adapt);
                                    if (marcado) {
                                        log.info("Marcada como nueva alta en EXPEDIENTE la persona con DNI " + dniLimpio + ". FALTAN DATOS");
                                    } else {
                                        log.error("Ha ocurrido un error marcando una nueva alta, DNI " + dniLimpio);
                                        validada = "N";
                                        correcto = false;
                                        resultado.append(" Ha ocurrido un error marcando una nueva alta , DNI ").append(dniLimpio).append(" &#10;");
                                    }
                                } catch (Exception e) {
                                    log.error("Excepción  marcando una nueva alta, DNI " + dniLimpio + " - " + e);
                                    validada = "N";
                                    correcto = false;
                                    resultado.append(" Ha ocurrido un error marcando una nueva alta , DNI ").append(dniLimpio).append(" &#10;");
                                }
                            }
                            // comprobar fecha caducidad
                            // Si la persona tiene registrada Fecha de CADUCIDAD en el registro se comprueba si se ha superado la fecha, en caso afirmativo se graba Fecha de Baja en el Registro
                            if (personaDisc.getFechCaducidad() != null) {
                                java.util.Date hoy = new Date();
                                if (personaDisc.getFechCaducidad().before(hoy)) {
                                    log.info("Certificado CADUCADO");
                                    correcto = false;
                                    try {
                                        boolean grabaBajaCaduca = meLanbide58Manager.grabaFechaBaja(codigo, dniLimpio, personaDisc.getFechCaducidad(), adapt);
                                        if (grabaBajaCaduca) {
                                            log.info("F Baja grabada para la persona con DNI " + dniLimpio);
                                            resultado.append("F Baja grabada en REGISTRO a: ").append(dniLimpio).append("  &#10;");
                                        } else {
                                            log.error("Ha ocurrido un error grabando la F Baja" + dniLimpio);
                                            resultado.append("Ha ocurrido un error grabando la F Baja en el REGISTRO. DNI: ").append(dniLimpio).append(" - Certificado CADUCADO &#10;");
                                        }
                                    } catch (Exception e) {
                                        log.error("Excepcion grabando la F Baja" + dniLimpio + " - " + e);
                                        resultado.append("Ha ocurrido un error grabando la F Baja en el REGISTRO. DNI: ").append(dniLimpio).append(" - Certificado CADUCADO &#10;");
                                    }
                                }
                            }

                        } else {
                            log.error("La persona con DNI " + dniLimpio + " - Expediente: " + numExp + " no tiene Fecha CERTIFICADO en Anexo C");
                            resultado.append("Falta Fecha CERTIFICADO en Anexo C - ").append(dniLimpio).append("  &#10;");
                            correcto = false;
                            validada = "N";
                            faltanDatos = "S";
                        }

                        // grabar FALTAN DATOS
                        try {
                            boolean actualizaFalta = meLanbide58Manager.actualizaDatosIncompletos(numExp, dniLimpio, faltanDatos, adapt);
                            if (actualizaFalta) {
                                log.info("Datos Incompletos = " + faltanDatos + " grabado en el expediente - DNI - " + dniLimpio);
                            } else {
                                log.error("Ha ocurrido un error grabando Datos incompletos  - DNI - " + dniLimpio);
                                resultado.append("Ha ocurrido un error grabando Datos incompletos en el Expediente. DNI: ").append(dniLimpio).append(" &#10;");
                            }
                        } catch (Exception e) {
                            log.error("Excepción grabando Datos incompletos  - DNI - " + dniLimpio + " - " + e);
                            resultado.append("Ha ocurrido un error grabando Datos incompletos en el Expediente. DNI: ").append(dniLimpio).append(" &#10;");
                        }

                        // grabar VALIDADA
                        try {
                            boolean validado = meLanbide58Manager.actualizaDiscValidada(numExp, dniLimpio, validada, adapt);
                            if (!validado) {
                                log.error("Ha ocurrido un error validando la discapacidad. DNI " + dniLimpio);
                                resultado.append("Ha ocurrido un error validando la discapacidad. DNI  ").append(dniLimpio).append(" &#10;");
                            }
                        } catch (Exception e) {
                            log.error("Excepción validando la discapacidad. DNI " + dniLimpio + " - " + e);
                            resultado.append("Ha ocurrido un error validando la discapacidad. DNI  ").append(dniLimpio).append(" &#10;");
                        }

                        // si faltan datos Borrar Importes
                        if (faltanDatos.equalsIgnoreCase("S")) {
                            correcto = false;
                            try {
                                boolean importeBorrado = meLanbide58Manager.borrarImportesSMI(numExp, dniLimpio, adapt);
                                if (importeBorrado) {
                                    log.info("Se ha borrado el importe calculado de " + dniLimpio);
                                } else {
                                    log.error("NO se ha borrado el IMPORTE CALCULADO de " + dniLimpio);
                                    resultado.append("NO se ha borrado el IMPORTE CALCULADO de  ").append(dniLimpio).append(" &#10;");
                                }
                            } catch (Exception e) {
                                log.error("NO se ha borrado el IMPORTE CALCULADO de " + dniLimpio + " - " + e);
                                resultado.append("NO se ha borrado el IMPORTE CALCULADO de  ").append(dniLimpio).append(" &#10;");
                            }
                        }
                    } else {
                        log.error("No existe en Anexo C");
                        correcto = false;
                        resultado.append("NO EXISTE en Anexo C  ").append(dniBaja).append("&#10;");
                    }
                }// for
            } else {
                log.info("El expediente " + numExp + " no tiene datos en el Anexo A Bajas ");
                resultado.append("- No tiene datos en el Anexo A Bajas.&#10;");
                codOperacion = "1";
            }
        } catch (Exception ex) {
            log.error("Error en procesaABajas", ex);
            correcto = false;
            resultado.append("Error en procesaA-Bajas. &#10;");
            codOperacion = "2";
        }
        if (!correcto) {
            codOperacion = "3";
        } else {
            resultado.append("Anexo A-Bajas procesado - OK. &#10;");
        }
        respuesta.add(codOperacion);
        respuesta.add(resultado);
        return respuesta;
    }

    /**
     * Operacion que procesa los datos de las personas presentes en el Anexo-C y
     * no se han procesado el los anexos A-ALTAS y A_BAJAS para validar los
     * datos de discapacidad
     *
     * @param codOrg
     * @param numExp
     * @param adapt
     * @return String con el resultado
     */
    private ArrayList procesarAnexoCPlantilla(int codOrg, String numExp, AdaptadorSQLBD adapt) {
        log.info("Entra en procesaPlantilla " + numExp);
        boolean correcto = true;
        ArrayList respuesta = new ArrayList();
        String codOperacion = "0";
        StringBuilder resultado = new StringBuilder();
        String dni = null;
        int id = 0;
        String tipoDisc = null;
        Double gradoDisc = 0.0;
        List<PlantillaVO> listaC = new ArrayList<PlantillaVO>();
        MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();

        try {
            listaC = meLanbide58Manager.getPlantillaSinProcesar(numExp, adapt);

            if (!listaC.isEmpty()) {
                int mesAyuda = meLanbide58Manager.getMesAyuda(codOrg, numExp, adapt);
                int anioAyuda = meLanbide58Manager.getAnioAyuda(numExp, adapt);
                log.debug("MES ayuda: " + mesAyuda);
                log.debug("AÑO ayuda: " + anioAyuda);

                for (PlantillaVO filaAnexoC : listaC) {
                    //    String codOperacion = "0";
                    DiscapacitadoVO persona = new DiscapacitadoVO();
                    String validada = "N";
                    String faltanDatos = "N";
                    String nuevaAlta = "N";
                    Date anteriorValidez = null;

                    if (filaAnexoC.getFecCertificado() != null) {
                        if (filaAnexoC.getTipoDis() != null) {
                            if (filaAnexoC.getGrado() != null || filaAnexoC.getTipoDis().equalsIgnoreCase("P") || filaAnexoC.getTipoDis().equalsIgnoreCase("PA") || filaAnexoC.getTipoDis().equalsIgnoreCase("PG") || filaAnexoC.getTipoDis().equalsIgnoreCase("PT")) {
                                // OJO DNIs no validados - Elimino caracteres no admitidos
                                String dniLimpio = filaAnexoC.getNif_Dni().replaceAll("[^\\dA-Za-z]", "").toUpperCase();
                                log.debug("DNI limpio: " + dniLimpio);

                                int codigo = meLanbide58Manager.existePersonaDisc(dniLimpio, adapt);

                                if (codigo != 0) {
                                    persona = meLanbide58Manager.getPersonaDiscp(codigo, dniLimpio, adapt);
                                    String valCert = persona.getValidez();

                                    if (valCert != null && !"".equalsIgnoreCase(valCert)) {
                                        if (persona.getFechBaja() == null) {
                                            // no dado de baja
                                            if (persona.getPorcDisc() != null || persona.getTipoDisc().equalsIgnoreCase("P")) {
                                                if (persona.getTipoDisc().equalsIgnoreCase("P") || (persona.getTipoDisc().equalsIgnoreCase(filaAnexoC.getTipoDis()) && persona.getPorcDisc().compareTo(filaAnexoC.getGrado()) == 0)) {
                                                    Calendar calendar = Calendar.getInstance();
                                                    calendar.setTime(persona.getFechResolucion());
                                                    int mesResolucion = (calendar.get(Calendar.MONTH)) + 1;
                                                    int anioResolucion = calendar.get(Calendar.YEAR);
                                                    log.debug("Ayuda: " + mesAyuda + "-" + anioAyuda);
                                                    log.debug("Resolucion: " + mesResolucion + "-" + anioResolucion);

                                                    if (anioResolucion < anioAyuda) {
                                                        // S
                                                        validada = "S";
                                                        log.info("VALIDADO - Año resolucion < ayuda");
                                                    } else if (anioResolucion == anioAyuda) {
                                                        if (mesResolucion < mesAyuda) {
                                                            // S
                                                            log.debug("MES resolucion < ayuda");
                                                            if (valCert.equalsIgnoreCase("I")) {
                                                                log.info("VALIDADO - Indefinido");
                                                                validada = "S";
                                                            } else if (valCert.equalsIgnoreCase("T")) {
                                                                if (persona.getFechCaducidad() != null) {
                                                                    calendar.setTime(persona.getFechCaducidad());
                                                                    int mesCaducidad = calendar.get(Calendar.MONTH);
                                                                    int anioCaducidad = calendar.get(Calendar.YEAR);
                                                                    log.debug("Caducidad: " + mesCaducidad + "-" + anioCaducidad);

                                                                    if (anioCaducidad > anioAyuda) {
                                                                        // S
                                                                        log.info("VALIDADO - Temporal  año caducidad > ayuda");
                                                                        validada = "S";
                                                                    } else if (anioCaducidad == anioAyuda) {
                                                                        if (mesCaducidad >= mesAyuda) {
                                                                            // S
                                                                            log.info("VALIDADO - Temporal  mes caducidad >= ayuda");
                                                                            validada = "S";
                                                                        } else {
                                                                            log.error("NO validado -  MES Caducidad Fuera de fechas");
                                                                        }
                                                                    } else {
                                                                        log.error("NO validado -   AÑO Caducidad Fuera de fechas");
                                                                    }

                                                                    // comprobar fecha caducidad
                                                                    Date hoy = new Date();
                                                                    if (persona.getFechCaducidad().before(hoy)) {
                                                                        log.info("Certificado CADUCADO");
                                                                        try {
                                                                            boolean grabaBajaCaduca = meLanbide58Manager.grabaFechaBaja(codigo, dniLimpio, persona.getFechCaducidad(), adapt);
                                                                            if (grabaBajaCaduca) {
                                                                                log.info("F Baja grabada para la persona con DNI " + dniLimpio);
                                                                            } else {
                                                                                log.error("Ha ocurrido un error grabando la F Baja " + dniLimpio);
                                                                                resultado.append("Ha ocurrido un error grabando la F Baja de la persona con DNI ").append(dniLimpio).append(" &#10;");
                                                                                correcto = false;
                                                                                validada = "";
                                                                            }
                                                                        } catch (Exception e) {
                                                                            log.error("Excepcion grabando la F Baja " + dniLimpio + " - " + e);
                                                                            resultado.append("Ha ocurrido un error grabando la F Baja de la persona con DNI ").append(dniLimpio).append(" &#10;");
                                                                            correcto = false;
                                                                            validada = "";
                                                                        }
                                                                    }
                                                                } else {
                                                                    log.error("NO validado -  Discapacidad temporal sin fecha de caducidad");
                                                                    //   faltanDatos = "S";
                                                                    resultado.append("NO validado -  Discapacidad temporal sin fecha de caducidad DNI ").append(dniLimpio).append(" &#10;");
                                                                    correcto = false;
                                                                }
                                                            } else {
                                                                log.error("NO validado -  Codigo VALIDEZ incorrecto");
                                                                //  faltanDatos = "S";
                                                                resultado.append("NO validado -  Codigo VALIDEZ incorrecto. DNI ").append(dniLimpio).append(" &#10;");
                                                                correcto = false;
                                                            }
                                                        } else {
                                                            log.error("NO validado -  MES Resolucion Fuera de fechas");
                                                        }
                                                    } else {
                                                        log.error("NO validado -   AÑO Resolucion Fuera de fechas");
                                                    }
                                                } else {
                                                    log.info("No coincide el tipo o el grado de discapacidad ====> ");
                                                    // comparo las fechas de os certificados
                                                    if (persona.getFechResolucion().compareTo(filaAnexoC.getFecCertificado()) == 0) {
                                                        resultado.append("No coinciden el tipo o el grado de la persona con DNI: ").append(dniLimpio).append(" &#10;");
                                                        correcto = false;
                                                    } else {
                                                        try {
                                                            anteriorValidez = sumarRestarDiasFecha(persona.getFechResolucion(), -1);
                                                        } catch (Exception e) {
                                                            log.error("Excepción al calcular Anterior Validez - " + e);
                                                            correcto = false;
                                                        }
                                                        try {
                                                            boolean grabaFecha = meLanbide58Manager.grabaFechaBaja(codigo, dniLimpio, anteriorValidez, adapt);
                                                            if (grabaFecha) {
                                                                log.info("F Baja grabada para la persona con DNI " + dniLimpio);
                                                            } else {
                                                                log.error("Ha ocurrido un error grabando la F Baja" + dniLimpio);
                                                                validada = "";
                                                                resultado.append("Ha ocurrido un error grabando la F Baja de la persona con DNI ").append(dniLimpio).append(" &#10;");
                                                                correcto = false;
                                                            }
                                                        } catch (Exception e) {
                                                            log.error("Excepcion grabando la F Baja" + dniLimpio + " -" + e);
                                                            validada = "";
                                                            resultado.append("Ha ocurrido un error grabando la F Baja de la persona con DNI ").append(dniLimpio).append(" &#10;");
                                                            correcto = false;
                                                        }

                                                        persona.setTipoDisc(filaAnexoC.getTipoDis());
//                                                        if (filaAnexoC.getSubtipo() != null) {
//                                                            persona.setSubtipo(filaAnexoC.getSubtipo());
//                                                        }
                                                        persona.setPorcDisc(filaAnexoC.getGrado());
                                                        persona.setDiscSevera(filaAnexoC.getDiscSevera());
                                                        persona.setFechEmision(filaAnexoC.getFecCertificado());
                                                        persona.setFechResolucion(filaAnexoC.getFecCertificado());
                                                        persona.setValidez("");
                                                        persona.setFechCaducidad(null);
                                                        persona.setFechBaja(null);
                                                        persona.setOidCertificado("");
                                                        persona.setNombreCertificado("");
                                                        // nuevo
                                                        correcto = false;
                                                        try {
                                                            boolean insertado = meLanbide58Manager.nuevaPersonaDisc(persona, adapt);
                                                            if (insertado) {
                                                                validada = "N";
                                                                log.info("Nuevo registro para la persona con DNI " + dniLimpio + ". FALTAN DATOS");
                                                                resultado.append("Nuevo registro para la persona con DNI  ").append(dniLimpio).append(". FALTAN DATOS &#10;");
                                                            } else {
                                                                validada = "";
                                                                log.error("Ha ocurrido un error grabando un nuevo registro DNI " + dniLimpio);
                                                                resultado.append("Ha ocurrido un error grabando un nuevo registro DNI  ").append(dniLimpio).append(" &#10;");
                                                            }
                                                        } catch (Exception e) {
                                                            validada = "";
                                                            log.error("Excepción grabando un nuevo registro DNI " + dniLimpio + " - " + e);
                                                            resultado.append("Ha ocurrido un error grabando un nuevo registro DNI  ").append(dniLimpio).append(" &#10;");
                                                        }
                                                    }
                                                }
                                            } else {
                                                log.info("No tiene grado de discapacidad en PERS_DISCP");
                                                resultado.append("No tiene GRADO Discapacdad en el REGISTRO - ").append(dniLimpio).append(" &#10;");
                                                correcto = false;
                                            }
                                        } else {
                                            log.info("Esta dado de baja " + persona.getFechBaja().toString() + " ====>");
                                            validada = "N";
                                        }
                                    } else {
                                        log.info("Codigo VALIDEZ vacio");
                                        //    faltanDatos = "S";
                                        resultado.append("Codigo VALIDEZ vacio en el REGISTRO - ").append(dniLimpio).append(" &#10;");
                                        correcto = false;
                                    }
                                } else {
                                    log.info("La persona con DNI " + dni + " no figura en la tabla PERS_DISCP");
                                    persona.setDni(dniLimpio);
                                    persona.setApellidos(filaAnexoC.getApellidos());
                                    persona.setNombre(filaAnexoC.getNombre());
                                    persona.setTipoDisc(tipoDisc);
                                    persona.setPorcDisc(gradoDisc);
                                    persona.setDiscSevera(filaAnexoC.getDiscSevera());
                                    persona.setFechEmision(filaAnexoC.getFecCertificado());
                                    persona.setFechResolucion(filaAnexoC.getFecCertificado());
                                    persona.setValidez("");
                                    persona.setFechCaducidad(null);
                                    persona.setOidCertificado("");
                                    persona.setNombreCertificado("");
                                    correcto = false;
                                    try {
                                        boolean insertado = meLanbide58Manager.nuevaPersonaDisc(persona, adapt);
                                        if (insertado) {
                                            log.info("Insertada nueva persona con DNI " + dniLimpio + ". FALTAN DATOS");
                                            validada = "N";
                                            resultado.append("Nuevo registro para la persona con DNI  ").append(dniLimpio).append(". FALTAN DATOS &#10;");
                                        } else {
                                            log.error("Ha ocurrido un error insertado una nueva persona DNI " + dniLimpio);
                                            validada = "";
                                            resultado.append("Ha ocurrido un error grabando un nuevo registro DNI  ").append(dniLimpio).append(" &#10;");
                                        }
                                        //  faltanDatos = "S";
                                    } catch (Exception e) {
                                        resultado.append("Ha ocurrido un error grabando un nuevo registro DNI  ").append(dniLimpio).append(" &#10;");
                                        log.error("Excepción grabando un nuevo registro DNI " + dniLimpio + " - " + e);
                                    }
                                }

                                try {
                                    boolean actualizaFalta = meLanbide58Manager.actualizaDatosIncompletos(numExp, dniLimpio, faltanDatos, adapt);
                                    if (actualizaFalta) {
                                        log.info("Datos Incompletos =" + faltanDatos + " grabado en el expediente " + numExp + " - DNI - " + dniLimpio);
                                    } else {
                                        log.error("Ha ocurrido un error grabando Datos incompletos " + numExp + " - DNI - " + dniLimpio);
                                        validada = "";
                                        resultado.append("Ha ocurrido un error grabando Datos incompletos  DNI  ").append(dniLimpio).append(" &#10;");
                                        correcto = false;
                                    }
                                } catch (Exception e) {
                                    resultado.append("Ha ocurrido un error grabando Datos incompletos  DNI  ").append(dniLimpio).append(" &#10;");
                                    correcto = false;
                                }

                                try {
                                    boolean validado = meLanbide58Manager.actualizaDiscValidada(numExp, dniLimpio, validada, adapt);
                                    if (!validado) {
                                        log.error("Ha ocurrido un error validando la discapacidad. DNI " + dniLimpio + " - Expediente: " + numExp);
                                        resultado.append("Ha ocurrido un error validando la discapacidad. DNI  ").append(dniLimpio).append(" &#10;");
                                        correcto = false;
                                    }
                                } catch (Exception e) {
                                    resultado.append("Ha ocurrido un error validando la discapacidad. DNI  ").append(dniLimpio).append(" &#10;");
                                    correcto = false;
                                    log.error("Excepción validando la discapacidad. DNI " + dniLimpio + " - " + e);
                                }

                                if (faltanDatos.equalsIgnoreCase("S")) {
                                    try {
                                        boolean importeBorrado = meLanbide58Manager.borrarImportesSMI(numExp, dniLimpio, adapt);
                                        if (importeBorrado) {
                                            log.info("Se ha borrado el importe calculado de " + dni);
                                        } else {
                                            log.error("NO se ha borrado el IMPORTE CALCULADO de " + dni);
                                            resultado.append("Error borrando el IMPORTE CALCULADO de  ").append(dniLimpio).append(" &#10;");
                                            correcto = false;
                                        }
                                    } catch (Exception e) {
                                        resultado.append("Error borrando el IMPORTE CALCULADO de  ").append(dniLimpio).append(" &#10;");
                                        correcto = false;
                                        log.error("Excepción borrando el IMPORTE CALCULADO de DNI " + dniLimpio + " - " + e);
                                    }
                                }
                                // persona procesada

                            } else {
                                correcto = false;
                                log.error("Falta el grado de discapacidad en el Anexo C - " + filaAnexoC.getNif_Dni());
                                resultado.append("Falta el grado de discapacidad en el Anexo C - ").append(filaAnexoC.getNif_Dni()).append(" &#10;");
                            }
                        } else {
                            correcto = false;
                            log.error("Falta el tipo de discapacidad en la persona con DNI " + filaAnexoC.getNif_Dni());
                            resultado.append("Falta el tipo de discapacidad en el Anexo C - ").append(filaAnexoC.getNif_Dni()).append(" &#10;");
                        }
                    } else {
                        correcto = false;
                        log.error("Falta la fecha del certificado en la persona con DNI " + filaAnexoC.getNif_Dni());
                        resultado.append("Falta la fecha del certificado en el Anexo C -  ").append(filaAnexoC.getNif_Dni()).append(" &#10;");
                    }
                } // for

            } else {
                // no hay datos en anexo C
                log.error("El expediente " + numExp + " no tiene datos en el Anexo C");
                correcto = false;
                resultado.append("El expediente NO tiene datos en el Anexo C. &#10;");
            }
        } catch (Exception ex) {
            resultado.append("ERROR a Actualizar Discapacidad Severa. &#10;");
            log.error("Error procesarAnexoC  : " + ex);
            codOperacion = "1";
        } finally {
            log.info("=================>>>  procesarAnexoC finalizado");

            //        log.debug(resultado);
            if (!correcto) {
                codOperacion = "3";
            } else {
                resultado.append("Anexo C procesado - OK. &#10;");
            }
            respuesta.add(codOperacion);
            respuesta.add(resultado);
            return respuesta;
        }

    }

    /**
     * Operacion que valida la discapacidad de las personas presentes en el
     * Anexo-C y no se han procesado el los anexos A-ALTAS y A_BAJAS
     *
     * @param numExp
     * @param adapt
     * @return String con el resultado
     */
    private ArrayList validarAnexoCPlantilla(String numExp, AdaptadorSQLBD adapt) {
        log.info("Entra en validarAnexoCPlantilla " + numExp);
        boolean correcto = true;
        ArrayList respuesta = new ArrayList();
        String codOperacion = "0";
        StringBuilder resultado = new StringBuilder();
        List<PlantillaVO> listaC = new ArrayList<PlantillaVO>();
        MeLanbide58Manager meLanbide58Manager = MeLanbide58Manager.getInstance();
        try {
            listaC = meLanbide58Manager.getPlantillaSinProcesar(numExp, adapt);
            if (!listaC.isEmpty()) {
                for (PlantillaVO filaAnexoC : listaC) {
                    String dniLimpio = filaAnexoC.getNif_Dni().replaceAll("[^\\dA-Za-z]", "").toUpperCase();
                    try {
                        boolean validado = meLanbide58Manager.actualizaDiscValidada(numExp, dniLimpio, "S", adapt);
                        if (!validado) {
                            log.error("Ha ocurrido un error validando la discapacidad. DNI " + dniLimpio + " - Expediente: " + numExp);
                            resultado.append("Ha ocurrido un error validando la discapacidad. DNI  ").append(dniLimpio).append(" &#10;");
                            correcto = false;
                        }
                    } catch (Exception e) {
                        resultado.append("Ha ocurrido un error validando la discapacidad. DNI  ").append(dniLimpio).append(" &#10;");
                        correcto = false;
                        log.error("Excepción validando la discapacidad. DNI " + dniLimpio + " - " + e);
                    }
                }
            } else {
                // no hay datos en anexo C
                log.error("El expediente " + numExp + " no tiene datos en el Anexo C");
                correcto = false;
                resultado.append("El expediente NO tiene datos en el Anexo C. &#10;");
            }
        } catch (Exception ex) {
            resultado.append("ERROR a validar Anexo C. &#10;");
            log.error("Error procesarAnexoC  : " + ex);
            codOperacion = "1";
        } finally {
            log.info("=================>>>  procesarAnexoC finalizado");

            //        log.debug(resultado);
            if (!correcto) {
                codOperacion = "3";
            } else {
                resultado.append("Anexo C validado - OK. &#10;");
            }
            respuesta.add(codOperacion);
            respuesta.add(resultado);
            return respuesta;
        }
    }

    /**
     * Operacion que calcula la severidad de la discapacidad en función del Tipo
     * y Grado
     *
     * Tipo discapacidad = PS, PSF, PSS, PSFS y grado discapacidad >= 33
     *
     * Tipo discapacidad = F, S o FS y grado discapacidad >= 65)
     *
     * @param tipoDisc Tipo discapacidad
     * @param gradoDisc grado discapacidad
     * @return boolean Es Severa S/N
     */
    private boolean esSevera(String tipoDisc, Double gradoDisc) {
        boolean severo = false;
        if (((tipoDisc.equalsIgnoreCase("PS") || tipoDisc.equalsIgnoreCase("PSF") || tipoDisc.equalsIgnoreCase("PSS") || tipoDisc.equalsIgnoreCase("PSFS")) && gradoDisc.compareTo(33.00) >= 0)
                || ((tipoDisc.equalsIgnoreCase("F") || tipoDisc.equalsIgnoreCase("FS") || tipoDisc.equalsIgnoreCase("S")) && gradoDisc.compareTo(65.00) >= 0)) {
            severo = true;
        }
        return severo;
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
        } catch (IOException e) {
        }
    }

    private String obtenerXmlSalidaAltas(String codigoOperacion, List<AltaVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (AltaVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NUMLINEA>");
            if (fila.getNumLinea() != null && !"".equals(fila.getNumLinea().toString())) {
                xmlSalida.append(fila.getNumLinea());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</NUMLINEA>");
            xmlSalida.append("<APELLIDOS>");
            xmlSalida.append(fila.getApellidos());
            xmlSalida.append("</APELLIDOS>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<FECHAALTA>");
            if (fila.getFechaAlta() != null) {
                xmlSalida.append(dateFormat.format(fila.getFechaAlta()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHAALTA>");
            xmlSalida.append("<NIF>");
            xmlSalida.append(fila.getNif());
            xmlSalida.append("</NIF>");
            xmlSalida.append("<NUMSS>");
            xmlSalida.append(fila.getNumSS());
            xmlSalida.append("</NUMSS>");
            xmlSalida.append("<DES_NOM>");
            if (fila.getCausaDesc() != null && !"".equals(fila.getCausaDesc())) {
                xmlSalida.append(fila.getCausaDesc());
            } else {
                xmlSalida.append("-");
            }
            xmlSalida.append("</DES_NOM>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
//        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaBajas(String codigoOperacion, List<BajaVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (BajaVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NUMLINEA>");
            if (fila.getNumLinea() != null && !"".equals(fila.getNumLinea().toString())) {
                xmlSalida.append(fila.getNumLinea());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</NUMLINEA>");
            xmlSalida.append("<APELLIDOS>");
            xmlSalida.append(fila.getApellidos());
            xmlSalida.append("</APELLIDOS>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<FECHABAJA>");
            if (fila.getFechaBaja() != null) {
                xmlSalida.append(dateFormat.format(fila.getFechaBaja()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHABAJA>");
            xmlSalida.append("<NIF>");
            xmlSalida.append(fila.getNif());
            xmlSalida.append("</NIF>");
            xmlSalida.append("<NUMSS>");
            xmlSalida.append(fila.getNumSS());
            xmlSalida.append("</NUMSS>");
            xmlSalida.append("<DES_NOM>");
            if (fila.getCausaDesc() != null && !"".equals(fila.getCausaDesc())) {
                xmlSalida.append(fila.getCausaDesc());
            } else {
                xmlSalida.append("-");
            }
            xmlSalida.append("</DES_NOM>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
//        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaSMI(HttpServletRequest request, String codigoOperacion, List<SMIVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (SMIVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NUMLINEA>");
            if (fila.getNumLinea() != null && !"".equals(fila.getNumLinea().toString())) {
                xmlSalida.append(fila.getNumLinea());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</NUMLINEA>");
            xmlSalida.append("<APELLIDOS>");
            xmlSalida.append(fila.getApellidos());
            xmlSalida.append("</APELLIDOS>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<NUMDIASSININCIDENCIAS>");
            if (fila.getNumDiasSinIncidencias() != null && !"".equals(fila.getNumDiasSinIncidencias().toString())) {
                xmlSalida.append(fila.getNumDiasSinIncidencias() != null ? fila.getNumDiasSinIncidencias().toString().replace(".", ",") : "");
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</NUMDIASSININCIDENCIAS>");
            xmlSalida.append("<NUMDIASINCIDENCIA>");
            if (fila.getNumDiasIncidencia() != null && !"".equals(fila.getNumDiasIncidencia().toString())) {
                xmlSalida.append(fila.getNumDiasIncidencia() != null ? fila.getNumDiasIncidencia().toString().replace(".", ",") : "");
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</NUMDIASINCIDENCIA>");
            xmlSalida.append("<CAUSAINCIDENCIA>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesCausaIncidencia()));
            xmlSalida.append("</CAUSAINCIDENCIA>");
            xmlSalida.append("<PORCJORNADA>");
            if (fila.getPorcJornada() != null && !"".equals(fila.getPorcJornada().toString())) {
                xmlSalida.append(fila.getPorcJornada() != null ? fila.getPorcJornada().toString().replace(".", ",") : "");
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</PORCJORNADA>");
            xmlSalida.append("<PORCREDUCCION>");
            if (fila.getPorcReduccion() != null && !"".equals(fila.getPorcReduccion().toString())) {
                xmlSalida.append(fila.getPorcReduccion() != null ? fila.getPorcReduccion().toString().replace(".", ",") : "");
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</PORCREDUCCION>");
            xmlSalida.append("<FECHA>");
            if (fila.getFecha() != null) {
                xmlSalida.append(dateFormat.format(fila.getFecha()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHA>");
            xmlSalida.append("<IMPORTESUBVENCION>");
            if (fila.getImporteSolicitado() != null && !"".equals(fila.getImporteSolicitado().toString())) {
                xmlSalida.append(fila.getImporteSolicitado() != null ? fila.getImporteSolicitado().toString().replace(".", ",") : "");
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</IMPORTESUBVENCION>");
            xmlSalida.append("<IMPORTERECALCULO>");
            if (fila.getImporteRecalculo() != null && !"".equals(fila.getImporteRecalculo().toString())) {
                xmlSalida.append(fila.getImporteRecalculo() != null ? fila.getImporteRecalculo().toString().replace(".", ",") : "");
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</IMPORTERECALCULO>");
           
            xmlSalida.append("<OBSERVACIONES>");
            xmlSalida.append(fila.getObservaciones());
            xmlSalida.append("</OBSERVACIONES>");
            xmlSalida.append("<NIF>");
            xmlSalida.append(fila.getNif());
            xmlSalida.append("</NIF>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        //     log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaAcceso(HttpServletRequest request, String codigoOperacion, List<PlantillaVO> lista, String codigoIncompleto) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("<CODIGO_INCOMPLETO>");
        xmlSalida.append(codigoIncompleto);
        xmlSalida.append("</CODIGO_INCOMPLETO>");
        for (PlantillaVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<NUMLINEA>");
            if (fila.getNumLinea() != null && !"".equals(fila.getNumLinea().toString())) {
                xmlSalida.append(fila.getNumLinea());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</NUMLINEA>");
            xmlSalida.append("<APELLIDOS>");
            xmlSalida.append(fila.getApellidos());
            xmlSalida.append("</APELLIDOS>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<SEXO>");
            xmlSalida.append(fila.getDesSexo());
            xmlSalida.append("</SEXO>");
            xmlSalida.append("<FECNACI>");
            if (fila.getFecNaci() != null) {
                xmlSalida.append(dateFormat.format(fila.getFecNaci()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECNACI>");
            xmlSalida.append("<NIFCIF>");
            xmlSalida.append(fila.getNif_Dni());
            xmlSalida.append("</NIFCIF>");
            xmlSalida.append("<NUMSS>");
            xmlSalida.append(fila.getNumSS());
            xmlSalida.append("</NUMSS>");
            xmlSalida.append("<FECCERTIFICADO>");
            if (fila.getFecCertificado() != null) {
                xmlSalida.append(dateFormat.format(fila.getFecCertificado()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECCERTIFICADO>");
            xmlSalida.append("<TIPODIS>");
            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesTipoDis()));
            xmlSalida.append("</TIPODIS>");
            xmlSalida.append("<GRADO>");
            if (fila.getGrado() != null && !"".equals(fila.getGrado().toString())) {
                xmlSalida.append(fila.getGrado());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</GRADO>");
            xmlSalida.append("<DISC_SEVERA_EMP>");
            if (fila.getDiscSevera() != null && !"".equals(fila.getDiscSevera())) {
                xmlSalida.append(fila.getDiscSevera());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</DISC_SEVERA_EMP>");
            xmlSalida.append("<DES_DISC_SEVERA_EMP>");
            if (fila.getDesDiscSevera() != null && !"".equals(fila.getDesDiscSevera())) {
                xmlSalida.append(getDescripcionDesplegable(request, fila.getDesDiscSevera()));
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</DES_DISC_SEVERA_EMP>");
            xmlSalida.append("<DISC_SEVERA_LAN>");
            if (fila.getDiscValidada() != null && !"".equals(fila.getDiscValidada())) {
                xmlSalida.append(fila.getDiscValidada());
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</DISC_SEVERA_LAN>");
            xmlSalida.append("<DES_DISC_VALIDADA>");
            if (fila.getDesDiscValidada() != null && !"".equals(fila.getDesDiscValidada())) {
                xmlSalida.append(getDescripcionDesplegable(request, fila.getDesDiscValidada()));
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</DES_DISC_VALIDADA>");
            xmlSalida.append("<CODCONTRATO>");
            xmlSalida.append(fila.getCodContrato());
//            xmlSalida.append(getDescripcionDesplegable(request, fila.getDesCodContrato()));
            xmlSalida.append("</CODCONTRATO>");
//            xmlSalida.append("<PORCJORNADA>");
//            if (fila.getPorcJornada() != null && !"".equalsIgnoreCase(fila.getPorcJornada().toString())) {
//                xmlSalida.append(fila.getPorcJornada() != null ? fila.getPorcJornada().toString().replace(".", ",") : "");
//            } else {
//                xmlSalida.append("null");
//            }
//            xmlSalida.append("</PORCJORNADA>");
            xmlSalida.append("<DATOS_PENDIENTES>");
            xmlSalida.append(fila.getDatosPendientes());
            xmlSalida.append("</DATOS_PENDIENTES>");
            xmlSalida.append("<NUEVA_ALTA>");
            xmlSalida.append(fila.getNuevaAlta());
            xmlSalida.append("</NUEVA_ALTA>");

            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        //      log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }

    private String obtenerXmlSalidaDiscapacitados(String codigoOperacion, List<DiscapacitadoVO> lista) {
        StringBuilder xmlSalida = new StringBuilder();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");
        for (DiscapacitadoVO fila : lista) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(fila.getId() != null ? fila.getId().toString() : "");
            xmlSalida.append("</ID>");
            xmlSalida.append("<DNI>");
            xmlSalida.append(fila.getDni());
            xmlSalida.append("</DNI>");
            xmlSalida.append("<APELLIDOS>");
            xmlSalida.append(fila.getApellidos());
            xmlSalida.append("</APELLIDOS>");
            xmlSalida.append("<NOMBRE>");
            xmlSalida.append(fila.getNombre());
            xmlSalida.append("</NOMBRE>");
            xmlSalida.append("<TIPODISC>");
            xmlSalida.append(fila.getTipoDisc());
            xmlSalida.append("</TIPODISC>");
            xmlSalida.append("<DESCTIPODISC>");
            xmlSalida.append(fila.getDescTipoDisc());
            xmlSalida.append("</DESCTIPODISC>");
            xmlSalida.append("<PORCDISC>");
            if (fila.getPorcDisc() != null && !"".equalsIgnoreCase(fila.getPorcDisc().toString())) {
                xmlSalida.append(fila.getPorcDisc() != null ? fila.getPorcDisc().toString().replace(".", ",") : "");
            } else {
                xmlSalida.append("null");
            }
            xmlSalida.append("</PORCDISC>");
            xmlSalida.append("<FECHEMISION>");
            if (fila.getFechEmision() != null) {
                xmlSalida.append(dateFormat.format(fila.getFechEmision()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHEMISION>");
            xmlSalida.append("<FECHRESOLUCION>");
            if (fila.getFechResolucion() != null) {
                xmlSalida.append(dateFormat.format(fila.getFechResolucion()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHRESOLUCION>");
            xmlSalida.append("<VALIDEZ>");
            xmlSalida.append(fila.getValidez());
            xmlSalida.append("</VALIDEZ>");
            xmlSalida.append("<DESCVALIDEZ>");
            xmlSalida.append(fila.getDescValidez());
            xmlSalida.append("</DESCVALIDEZ>");
            xmlSalida.append("<FECHCADUCIDAD>");
            if (fila.getFechCaducidad() != null) {
                xmlSalida.append(dateFormat.format(fila.getFechCaducidad()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHCADUCIDAD>");
            xmlSalida.append("<SEVERA>");
            xmlSalida.append(fila.getDiscSevera());
            xmlSalida.append("</SEVERA>");
            xmlSalida.append("<DESCSEVERA>");
            xmlSalida.append(fila.getDescDiscSevera());
            xmlSalida.append("</DESCSEVERA>");
            xmlSalida.append("<FECHVALIDACION>");
            if (fila.getFechValidacion() != null) {
                xmlSalida.append(dateFormat.format(fila.getFechValidacion()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHVALIDACION>");
            xmlSalida.append("<FECHBAJA>");
            if (fila.getFechBaja() != null) {
                xmlSalida.append(dateFormat.format(fila.getFechBaja()));
            } else {
                xmlSalida.append("");
            }
            xmlSalida.append("</FECHBAJA>");
            xmlSalida.append("<OID_CERTIFICADO>");
            xmlSalida.append(fila.getOidCertificado());
            xmlSalida.append("</OID_CERTIFICADO>");
            xmlSalida.append("<NOMBRE_CERTIFICADO>");
            xmlSalida.append(fila.getNombreCertificado());
            xmlSalida.append("</NOMBRE_CERTIFICADO>");
            xmlSalida.append("<CENTRO>");
            xmlSalida.append(fila.getCentro());
            xmlSalida.append("</CENTRO>");
            xmlSalida.append("<TERRITORIO>");
            xmlSalida.append(fila.getTerritorio());
            xmlSalida.append("</TERRITORIO>");
            xmlSalida.append("<DESC_TERRITORIO>");
            xmlSalida.append(fila.getDescTerritorio());
            xmlSalida.append("</DESC_TERRITORIO>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
//        log.debug("xml: " + xmlSalida);
        return xmlSalida.toString();
    }

    // Funciones Comunes
    private List<DesplegableAdmonLocalVO> traducirDesplegable(HttpServletRequest request, List<DesplegableAdmonLocalVO> desplegable) {
        for (DesplegableAdmonLocalVO d : desplegable) {
            if (d.getDes_nom() != null && !d.getDes_nom().isEmpty()) {
                d.setDes_nom(getDescripcionDesplegable(request, d.getDes_nom()));
            }
        }
        return desplegable;
    }

    /**
     * Operacion que Recupera la descripcion del desplegable en el Idioma de la
     * request
     *
     * @param request
     * @param descripcionCompleta
     * @return String con la descripcion en un idioma
     */
    private String getDescripcionDesplegable(HttpServletRequest request, String descripcionCompleta) {
        String descripcion = descripcionCompleta;
        String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide58.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                    if (getIdioma(request) == 4) {
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
            if (log.isDebugEnabled()) {
//                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    /**
     * Operacion que Recupera el Idioma de la request para la gestion de
     * Desplegables
     *
     * @param request
     * @return int codigo idioma 1 o 4
     */
    private int getIdioma(HttpServletRequest request) {
        // Recuperamos el Idioma de la request para la gestion de Desplegables
        UsuarioValueObject usuario = new UsuarioValueObject();
        int idioma = 1;  // Por Defecto 1 Castellano
        try {
            if (request != null && request.getSession() != null) {
                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    idioma = usuario.getIdioma();
                }
            }
        } catch (Exception ex) {
            log.error("Error al recuperar el idioma del usuario de la request, asignamos por defecto 1 Castellano", ex);
            idioma = 1;
        }
        return idioma;
    }

    /**
     * Devuelve la extension de fichero correspondiente al tipo MIME pasado.
     * Utilizamos la clase de flexia para no reescribir funciones
     *
     * @param extension
     * @return
     */
    public String getMimeTypeFromExtension(String extension) {
        return MimeTypes.guessMimeTypeFromExtension(extension);
    }

    /**
     * Suma los días recibidos a la fecha Devuelve el objeto Date con los nuevos
     * días añadidos
     *
     * @param fecha
     * @param dias numero de días a añadir, o restar en caso de días<0
     * @return Date fecha con los nuevos días añadidos
     */
    private Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

}
