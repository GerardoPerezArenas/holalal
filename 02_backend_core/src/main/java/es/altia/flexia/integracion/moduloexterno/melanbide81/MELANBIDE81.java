package es.altia.flexia.integracion.moduloexterno.melanbide81;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide81.manager.MeLanbide81Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConstantesMeLanbide81;
import es.altia.flexia.integracion.moduloexterno.melanbide81.util.MeLanbide81Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ProyectoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.PuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo1VO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo2VO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import static es.altia.util.commons.WebOperations.retornarJSON;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.zip.DataFormatException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author kepa
 */
public class MELANBIDE81 extends ModuloIntegracionExterno {

    private static final Logger log = LogManager.getLogger(MELANBIDE81.class);
    private static final MeLanbide81Utils m81Utils = new MeLanbide81Utils();
    private final MeLanbide81Manager m81Manager = new MeLanbide81Manager();
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide81.FORMATO_FECHA);

    /**
     * Alta Expedientes via registro platea --> MELANBIDE 42
     *
     * @param codigoOrganizacion
     * @param numeroExpediente
     * @param xml
     * @throws Exception
     */
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);
        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    /**
     * @param codOrganizacion
     * @param codTramite
     * @param request
     * @param numExpediente
     * @param ocurrenciaTramite
     * @param response
     * @return url de la Pestaña principal de LPEEL
     */
    public String cargarPantallaLPEEL(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaLPEEL de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getSimpleName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        request.setAttribute("numExp", numExpediente);
        try {
            List<ProyectoVO> listaProyectos = m81Manager.getListaProyectos(numExpediente, adapt);
            if (!listaProyectos.isEmpty()) {
                for (ProyectoVO proy : listaProyectos) {
                    proy.setDescTipoProyecto(m81Utils.getDescripcionDesplegable(request, proy.getDescTipoProyecto()));
                }
                request.setAttribute("listaProyectos", listaProyectos);
            }
        } catch (Exception ex) {
            log.error("Error al recuperar los datos de Proyectos - MELANBIDE81 - cargarPantallaLPEEL", ex);
        }
        return "/jsp/extension/melanbide81/proyectos/m81Proyectos.jsp";
    }

    /**
     * @param codOrganizacion
     * @param codTramite
     * @param request
     * @param numExpediente
     * @param ocurrenciaTramite
     * @param response
     *
     */
    public String cargarPantallaAportacionContrataciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaAportacionContrataciones de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;

        try {
            adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getSimpleName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            Connection con = null;
            try {
                con = adapt.getConnection();
                //SubPestana TIPO1
                List<Tipo1VO> divListTipo1 = m81Manager.getListaTipo1(numExpediente, adapt);
                {
                    for (Tipo1VO cont : divListTipo1) {
                        if (!divListTipo1.isEmpty()) {
                        cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                        cont.setDescGrupocot(m81Utils.getDescripcionDesplegable(request, cont.getDescGrupocot()));
                        cont.setDescInscrita(m81Utils.getDescripcionDesplegable(request, cont.getDescInscrita()));
                    }
                    request.setAttribute("divListaTipo1", divListTipo1);
                }
                    
                request.setAttribute("urlPestanaTipo1", "/jsp/extension/melanbide81/tipo_contrataciones/tipo1.jsp");
                //SubPestana TIPO2
                List<Tipo2VO> divListTipo2 = m81Manager.getListaTipo2(numExpediente, adapt);
                {
                    for (Tipo2VO cont : divListTipo2) {
                        cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                        cont.setDescGrupocot(m81Utils.getDescripcionDesplegable(request, cont.getDescGrupocot()));
                        cont.setDescInscrita(m81Utils.getDescripcionDesplegable(request, cont.getDescInscrita()));
                        cont.setDescTipocontrato(m81Utils.getDescripcionDesplegable(request, cont.getDescTipocontrato()));
                        cont.setDescColectivo(m81Utils.getDescripcionDesplegable(request, cont.getDescColectivo()));
                    }
                    request.setAttribute("divListaTipo2", divListTipo2);
                }
                }
                request.setAttribute("urlPestanaTipo2", "/jsp/extension/melanbide81/tipo_contrataciones/tipo2.jsp");
            } catch (Exception ex) {
                log.error("Error al recuperar los datos de listaTipo1 - MELANBIDE81 - cargarSubpestanaTipo2", ex);
            } finally {
                try {
                    adapt.devolverConexion(con);
                } catch (BDException e) {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        log.info("antes de retornar la url : /jsp/extension/melanbide81/tipo_contrataciones/aportacionContrataciones.jsp");
        return "/jsp/extension/melanbide81/tipo_contrataciones/aportacionContrataciones.jsp";
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
    public String cargarListaxProyecto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarListaxProyecto de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        String numExp = null;
        if (request.getParameter("numExp") != null) {
            numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
        }
        int idProyecto = Integer.valueOf(request.getParameter("idProyecto"));
        request.setAttribute("idProyecto", request.getParameter("idProyecto"));
        request.setAttribute("prioridad", request.getParameter("prioridad"));
        request.setAttribute("denomProyecto", request.getParameter("denomProyecto"));
        request.setAttribute("tipoProyecto", request.getParameter("tipoProyecto"));
        log.debug("idProy: " + idProyecto + " - Prioridad: " + request.getParameter("prioridad") + " - Denominación: " + request.getParameter("denomProyecto") + " - Tipo: " + request.getParameter("tipoProyecto"));
        try {
            adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getSimpleName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        if (adapt != null) {
            try {

                String urlPuestos = cargarPestanaPuestos(numExp, idProyecto, adapt, request);
                if (urlPuestos != null) {
                    request.setAttribute("urlPestanaPuestos", urlPuestos);
                }
                String urlContrataciones = cargarPestanaContrataciones(numExp, idProyecto, adapt, request);
                if (urlContrataciones != null) {
                    request.setAttribute("urlPestanaContrataciones", urlContrataciones);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos de las subPestañas - MELANBIDE81 - cargarPantallaLPEEL ", ex);
            }
        }
        return "/jsp/extension/melanbide81/proyectos/m81ListasxProyectos.jsp";
    }

    /**
     * @param numExp
     * @param idProyecto
     * @param adapt
     * @param request
     * @return url Puestos
     */
    private String cargarPestanaPuestos(String numExp, int idProyecto, AdaptadorSQLBD adapt, HttpServletRequest request) {
        try {
            List<PuestoVO> listaPuestos = m81Manager.getListaPuestosxProyecto(numExp, idProyecto, adapt);
            if (!listaPuestos.isEmpty()) {
                request.setAttribute("listaPuestos", listaPuestos);
            }
        } catch (Exception ex) {
            log.error("Error al recuperar los datos de Puestos - MELANBIDE81 - cargarSubpestanaPuestos", ex);
        }
        return "/jsp/extension/melanbide81/puestos/m81Puestos.jsp";
    }

    /**
     * @param numExp
     * @param idProyecto
     * @param adapt
     * @param request
     * @return url Contrataciones
     */
    private String cargarPestanaContrataciones(String numExp, int idProyecto, AdaptadorSQLBD adapt, HttpServletRequest request) {
        try {
            List<ContratacionVO> listaContrataciones = this.m81Manager.getListaContratacionesxProyecto(numExp, idProyecto, adapt);
            if (!listaContrataciones.isEmpty()) {
                for (ContratacionVO cont : listaContrataciones) {
                    cont.setDescTipoDesempleado(m81Utils.getDescripcionDesplegable(request, cont.getDescTipoDesempleado()));
                    cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                }
                request.setAttribute("listaContrataciones", listaContrataciones);
            }
        } catch (Exception ex) {
            log.error("Error al recuperar los datos de Contrataciones - MELANBIDE81 - cargarSubpestanaContrataciones", ex);
        }
        return "/jsp/extension/melanbide81/contrataciones/m81Contrataciones.jsp";
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
    public void generarExcelAyudasContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en generar excel Ayudas Contratacion de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        String numExp = null;
        if (request.getParameter("numExp") != null) {
            numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
        }
        int idProyecto = Integer.valueOf(request.getParameter("idProyecto"));

        log.debug("idProy: " + idProyecto + " - Prioridad: " + request.getParameter("prioridad") + " - Denominación: " + request.getParameter("denomProyecto") + " - Tipo: " + request.getParameter("tipoProyecto"));
        try {
            adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getSimpleName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        List<ContratacionVO> listaContrataciones = new ArrayList<ContratacionVO>();

        if (adapt != null) {
            try {

                //Obtenemos los datos de ayudas a la contratacion que iran en el excel 
                listaContrataciones = m81Manager.getListaContratacionesxProyecto(numExp, idProyecto, adapt);
                if (!listaContrataciones.isEmpty()) {
                    for (ContratacionVO cont : listaContrataciones) {
                        cont.setDescTipoDesempleado(m81Utils.getDescripcionDesplegable(request, cont.getDescTipoDesempleado()));
                        cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                    }

                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos del excel de ayudas a la contratacion ", ex);
            }
        }

        //Procedemos a generar el Excel
        FileInputStream istr = null;
        try {
            HSSFWorkbook libro = new HSSFWorkbook();
            Map<String, CellStyle> estilos = crearEstilos(libro);
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
            } catch (Exception e) {
                log.error("Error findColor : " + e);

            }

            int numFila = 0;
            //int numFila2 = 0;
            int contCelda = 0;
            HSSFRow fila = null;
            HSSFCell celda = null;
            HSSFSheet hoja = libro.createSheet("Ayudas a la contratacion");
            fila = hoja.createRow(numFila);
            //Creamos la cabeçera 
            crearCabeceraAyuda(libro, fila, celda, 1);

            log.debug("Insertamos los datos, fila a fila");
            int filaPrimera = numFila + 2;
            log.debug("Primera fila a sumar= " + filaPrimera);
            //Insertamos los datos, fila a fila
            int registros = listaContrataciones.size();
            for (ContratacionVO contratacion : listaContrataciones) {
                numFila++;
                fila = hoja.createRow(numFila);
                log.debug("Fila: " + (numFila + 1));
                //COLUMNA: Tipo persona desempleada
                celda = fila.createCell(0);
                celda.setCellValue(contratacion.getDescTipoDesempleado() != null ? contratacion.getDescTipoDesempleado().toUpperCase() : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Sexo
                celda = fila.createCell(1);
                celda.setCellValue(contratacion.getDescSexo() != null ? contratacion.getDescSexo().toUpperCase() : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Duración estimada
                celda = fila.createCell(2);
                celda.setCellValue(contratacion.getDuracion() != null ? String.valueOf((Object) contratacion.getDuracion()) : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: % jornada
                celda = fila.createCell(3);
                celda.setCellValue(contratacion.getPorcJorn() != null ? String.valueOf((Object) contratacion.getPorcJorn()).replace(".", ",") : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA:  Nº contratos previstos
                celda = fila.createCell(4);
                celda.setCellValue(contratacion.getNumContratos() != null ? String.valueOf((Object) contratacion.getNumContratos()) : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Subv. solicitada
                celda = fila.createCell(5);
                celda.setCellValue(contratacion.getSubvencion() != null ? String.valueOf((Object) contratacion.getSubvencion()).replace(".", ",") : "");
                celda.setCellStyle(estilos.get("estiloUltimaColumna"));

            }

            int filaUltima = numFila + 1;
            log.debug("Fin tabla - Ultima fila a sumar= " + filaUltima);

            numFila++;
            //Pinto la linea inferior de la tabla
            fila = hoja.createRow(numFila);
            for (int i = 0; i < 6; i++) {
                celda = fila.createCell(i);
                celda.setCellStyle(estilos.get("estiloUltimaFila"));
            }

            hoja.setColumnWidth(0, 12000);//Tipo persona Desempleada     
            hoja.setColumnWidth(1, 3000);//Sexo    
            hoja.autoSizeColumn(2);//Duración estimada    
            hoja.autoSizeColumn(3);//% jornada    
            hoja.autoSizeColumn(4);//Nº contratos previstos    
            hoja.autoSizeColumn(5);//Subv. solicitada    

            log.debug("Definir directorio y fichero");
            File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
            File informe = File.createTempFile(numExp.replace("/", "") + "_Ayudas_Contratacion", ".xls", directorioTemp);
            informe.renameTo(informe);
            FileOutputStream archivoSalida = new FileOutputStream(informe);
            libro.write(archivoSalida);
            archivoSalida.close();
            String rutaArchivoSalida = null;
            rutaArchivoSalida = informe.getAbsolutePath();

            istr = new FileInputStream(rutaArchivoSalida);

            BufferedInputStream bstr = new BufferedInputStream(istr); // promote

            int size = (int) informe.length();
            byte[] data = new byte[size];
            bstr.read(data, 0, size);
            bstr.close();

            log.debug("Abre el fichero EXCEL");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setContentLength(data.length);
            response.getOutputStream().write(data, 0, data.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception ex) {
            log.error("Error al generar el excel de ayudas a la contratacion ", ex);
        } finally {
            if (istr != null) {
                try {
                    istr.close();
                } catch (IOException ex) {
                    log.error("Error al intentar cerrar el InputStreamReader", ex);
                }
            }

        }
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
    public void generarExcelFomentoEmpleo(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en generar excel Ayudas Contratacion de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        String numExp = null;
        if (request.getParameter("numExp") != null) {
            numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
        }
        int idProyecto = Integer.valueOf(request.getParameter("idProyecto"));

        log.debug("idProy: " + idProyecto + " - Prioridad: " + request.getParameter("prioridad") + " - Denominación: " + request.getParameter("denomProyecto") + " - Tipo: " + request.getParameter("tipoProyecto"));
        try {
            adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getSimpleName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        List<PuestoVO> listaPuestos = new ArrayList<PuestoVO>();
        ProyectoVO proyecto = new ProyectoVO();

        if (adapt != null) {
            try {
                String id = request.getParameter("idProyecto");
                //Obtenemos los datos de ayudas a la contratacion que iran en el excel 
                listaPuestos = m81Manager.getListaPuestosxProyecto(numExp, idProyecto, adapt);
                proyecto = m81Manager.getProyectoPorID(numExp, id, adapt);

            } catch (Exception ex) {
                log.error("Error al recuperar los datos del excel de ayudas a la contratacion ", ex);
            }
        }

        //Procedemos a generar el Excel
        FileInputStream istr = null;
        try {
            HSSFWorkbook libro = new HSSFWorkbook();
            Map<String, CellStyle> estilos = crearEstilos(libro);
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try {
                hssfColor = palette.findColor((byte) 75, (byte) 149, (byte) 211);
                if (hssfColor == null) {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
            } catch (Exception e) {
                log.error("Error findColor : " + e);

            }

            int numFila = 0;
            //int numFila2 = 0;
            int contCelda = 0;
            HSSFRow fila = null;
            HSSFCell celda = null;
            HSSFSheet hoja = libro.createSheet("Fomento del Empleo");
            fila = hoja.createRow(numFila);
            //Creamos la cabeçera 
            crearCabeceraFomento(libro, fila, celda, 1);

            log.debug("Insertamos los datos, fila a fila");
            int filaPrimera = numFila + 2;
            log.debug("Primera fila a sumar= " + filaPrimera);
            //Insertamos los datos, fila a fila
            int registros = listaPuestos.size();
            for (PuestoVO puesto : listaPuestos) {
                numFila++;
                fila = hoja.createRow(numFila);
                log.debug("Fila: " + (numFila + 1));
                //COLUMNA: Prioridad
                celda = fila.createCell(0);
                celda.setCellValue(proyecto.getPrioridad() != null ? String.valueOf((Object) proyecto.getPrioridad()).toUpperCase() : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Denominacion
                celda = fila.createCell(1);
                celda.setCellValue(proyecto.getDenominacion() != null ? proyecto.getDenominacion().toUpperCase() : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Entidad Actuante
                celda = fila.createCell(2);
                celda.setCellValue(proyecto.getEntidad() != null ? String.valueOf((Object) proyecto.getEntidad()).toUpperCase() : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Denominación del puesto
                celda = fila.createCell(3);
                celda.setCellValue(puesto.getDenominacion() != null ? String.valueOf((Object) puesto.getDenominacion()).toUpperCase() : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Duración estimada
                celda = fila.createCell(4);
                celda.setCellValue(puesto.getDuracion() != null ? String.valueOf((Object) puesto.getDuracion()).replace(".", ",") : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: % jornada
                celda = fila.createCell(5);
                celda.setCellValue(puesto.getPorcJorn() != null ? String.valueOf((Object) puesto.getPorcJorn()).replace(".", ",") : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Nº contratos
                celda = fila.createCell(6);
                celda.setCellValue(puesto.getNumContratos() != null ? String.valueOf((Object) puesto.getNumContratos()) : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Coste estimado
                celda = fila.createCell(7);
                celda.setCellValue(puesto.getCoste() != null ? String.valueOf((Object) puesto.getCoste()).replace(".", ",") : "");
                celda.setCellStyle(estilos.get("estiloCentrado"));

                //COLUMNA: Subv. solicitada
                celda = fila.createCell(8);
                celda.setCellValue(puesto.getSubvencion() != null ? String.valueOf((Object) puesto.getSubvencion()).replace(".", ",") : "");
                celda.setCellStyle(estilos.get("estiloUltimaColumna"));

            }

            int filaUltima = numFila + 1;
            log.debug("Fin tabla - Ultima fila a sumar= " + filaUltima);

            numFila++;
            //Pinto la linea inferior de la tabla
            fila = hoja.createRow(numFila);
            for (int i = 0; i < 9; i++) {
                celda = fila.createCell(i);
                celda.setCellStyle(estilos.get("estiloUltimaFila"));
            }

            hoja.autoSizeColumn(0);//Prioridad    
            hoja.setColumnWidth(1, 11000);//Denominación   
            hoja.setColumnWidth(2, 11000);//Entidad Actuante
            hoja.autoSizeColumn(3);//Denominación del puesto
            hoja.autoSizeColumn(4);//Duración estimada   
            hoja.autoSizeColumn(5);//% jornada   
            hoja.autoSizeColumn(6);//Nº contratos    
            hoja.autoSizeColumn(7);//Coste estimado   
            hoja.autoSizeColumn(8);//Subv. solicitada   

            log.debug("Definir directorio y fichero");
            File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
            File informe = File.createTempFile(numExp.replace("/", "") + "_Fomento_Empleo", ".xls", directorioTemp);
            informe.renameTo(informe);
            FileOutputStream archivoSalida = new FileOutputStream(informe);
            libro.write(archivoSalida);
            archivoSalida.close();
            String rutaArchivoSalida = null;
            rutaArchivoSalida = informe.getAbsolutePath();

            istr = new FileInputStream(rutaArchivoSalida);

            BufferedInputStream bstr = new BufferedInputStream(istr); // promote

            int size = (int) informe.length();
            byte[] data = new byte[size];
            bstr.read(data, 0, size);
            bstr.close();

            log.debug("Abre el fichero EXCEL");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setContentLength(data.length);
            response.getOutputStream().write(data, 0, data.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception ex) {
            log.error("Error al generar el excel de ayudas a la contratacion ", ex);
        } finally {
            if (istr != null) {
                try {
                    istr.close();
                } catch (IOException ex) {
                    log.error("Error al intentar cerrar el InputStreamReader", ex);
                }
            }

        }
    }

    private void crearCabeceraFomento(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, int idioma) {
        log.info(">>>> ENTRA en crearCabecera de " + this.getClass().getSimpleName());
        try {
            MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();

            Map<String, CellStyle> estilos = crearEstilos(libro);

            //cabeceras
            // Prioridad
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.prioridad").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //Denominacion
            celda = fila.createCell(1);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.denominacion").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //Entidad Actuante     
            celda = fila.createCell(2);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.entidadActuante").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //Denominación del puesto
            celda = fila.createCell(3);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.denominacionPuesto").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // duracionEstimada
            celda = fila.createCell(4);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.duracionEstimada").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // % jornada
            celda = fila.createCell(5);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.porcentajeJornada").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Nº contratos
            celda = fila.createCell(6);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.numeroContratos").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //Coste estimado
            celda = fila.createCell(7);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.costeEstimado").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //Subv. solicitada
            celda = fila.createCell(8);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.subSolicitada").toUpperCase());
            celda.setCellStyle(estilos.get("cabeceraUltima"));

        } catch (Exception ex) {
            log.error("Error al definir estilos de cabeceras: " + ex);
        }
        log.debug("crearCabecera  >>>>> END");
    }

    private void crearCabeceraAyuda(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, int idioma) {
        log.info(">>>> ENTRA en crearCabecera de " + this.getClass().getSimpleName());
        try {
            MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();

            Map<String, CellStyle> estilos = crearEstilos(libro);

            //cabeceras
            // Tipo persona desempleada
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.tipoPersonaDesempleada").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //Sexo
            celda = fila.createCell(1);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.sexo").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //Duración estimada     
            celda = fila.createCell(2);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.duracionEstimada").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            //% jornada
            celda = fila.createCell(3);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.porcentajeJornada").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Nº contratos previstos
            celda = fila.createCell(4);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.nContratosPrevistos").toUpperCase());
            celda.setCellStyle(estilos.get("cabecera"));
            // Subv. solicitada
            celda = fila.createCell(5);
            celda.setCellValue(meLanbide81I18n.getMensaje(idioma, "excel.subSolicitada").toUpperCase());
            celda.setCellStyle(estilos.get("cabeceraUltima"));

        } catch (Exception ex) {
            log.error("Error al definir estilos de cabeceras: " + ex);
        }
        log.debug("crearCabecera  >>>>> END");
    }

    /**
     * Create a library of cell styles
     */
    private static Map<String, CellStyle> crearEstilos(HSSFWorkbook libro) {
        Map<String, CellStyle> estilos = new HashMap<String, CellStyle>();
        HSSFCellStyle estilo;
        HSSFPalette palette = libro.getCustomPalette();
        HSSFColor hssfColorCabecera = null;
        HSSFCreationHelper createHelper = libro.getCreationHelper();
        try {
            hssfColorCabecera = palette.findColor((byte) 75, (byte) 149, (byte) 211);
            if (hssfColorCabecera == null) {
                hssfColorCabecera = palette.getColor(HSSFColor.ROYAL_BLUE.index);
            }
        } catch (Exception e) {
            log.error("Error findColor : " + e);
            //  codigoOperacion = "1";
        }
        HSSFFont titulo = libro.createFont();
        titulo.setColor(hssfColorCabecera.getIndex());
        titulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titulo.setFontHeight((short) 250);

        HSSFFont total = libro.createFont();
        total.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        total.setFontHeight((short) 250);

        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        negrita.setFontHeight((short) 200);

        HSSFFont normal = libro.createFont();
        normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        normal.setFontHeight((short) 150);

        HSSFFont negritaTitulo = libro.createFont();
        negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        negritaTitulo.setFontHeight((short) 200);
        negritaTitulo.setColor(HSSFColor.WHITE.index);

        estilo = libro.createCellStyle();
        estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estilo.setFillForegroundColor(hssfColorCabecera.getIndex());
        estilo.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estilo.setWrapText(true);
        estilo.setShrinkToFit(true);
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estilo.setFont(negritaTitulo);
        estilo.setFont(negritaTitulo);
        estilos.put("cabecera", estilo);

        estilo = libro.createCellStyle();
        estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estilo.setFillForegroundColor(hssfColorCabecera.getIndex());
        estilo.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estilo.setWrapText(true);
        estilo.setShrinkToFit(true);
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estilo.setFont(negritaTitulo);
        estilos.put("cabeceraUltima", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setFont(titulo);
        estilos.put("estiloTitulo", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estilo.setDataFormat(libro.createDataFormat().getFormat("#,##0.00"));
        estilo.setWrapText(true);
        estilo.setFont(total);
        estilos.put("estiloTotal", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setDataFormat(libro.createDataFormat().getFormat("#,##0.00"));
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilos.put("estiloImportes", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilos.put("estiloCentrado", estilo);

        estilo = libro.createCellStyle();
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        estilos.put("estiloCelda", estilo);

        estilo = libro.createCellStyle();
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilos.put("estiloUltimaFila", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setDataFormat(libro.createDataFormat().getFormat("#,##0.00"));
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilos.put("estiloUltimaColumna", estilo);

        estilo = libro.createCellStyle();
        estilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estilo.setWrapText(true);
        estilo.setFont(normal);
        estilo.setDataFormat(createHelper.createDataFormat().getFormat("d/m/yyyy"));
        // estilo.setDataFormat(HSSFDataFormat.getBuiltinFormat("d/m/yyyy"));
        estilos.put("estiloFecha", estilo);
        return estilos;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return urlNuevoProyecto
     */
    public String cargarNuevoProyecto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoProyecto - ");
        String nuevo = "1";
        String urlNuevoProyecto = "/jsp/extension/melanbide81/proyectos/m81ProyectoMantenimiento.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }

            cargarDesplegablesProyecto(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva contratación : " + ex.getMessage());
        }
        return urlNuevoProyecto;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return urlNuevoProyecto
     */
    public String cargarModificarProyecto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarProyecto - ");
        String nuevo = "0";
        String urlNuevoProyecto = "/jsp/extension/melanbide81/proyectos/m81ProyectoMantenimiento.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);

            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                ProyectoVO datModif = m81Manager.getProyectoPorID(numExp, id, m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
            cargarDesplegablesProyecto(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlNuevoProyecto;
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
    public void crearNuevoProyecto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoProyecto - ");
        String codigoOperacion = "-1";
        List<ProyectoVO> lista = new ArrayList<ProyectoVO>();
        ProyectoVO nuevoProyecto = new ProyectoVO();
        boolean insertOK = false;
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String numExp = request.getParameter("numExp");
            String prioridad = request.getParameter("prioridad");
            String denominacion = request.getParameter("denominacion");
            String entidad = request.getParameter("entidad");
            String tipoProy = request.getParameter("tipoProy");
            String fases = request.getParameter("fases");
            log.debug("Exp " + numExp);
            log.debug("prioridad " + prioridad);
            log.debug("denominacion " + denominacion);
            log.debug("entidad " + entidad);
            log.debug("tipoProy " + tipoProy);
            log.debug("fases " + fases);

            nuevoProyecto.setNumExp(numExp);
            nuevoProyecto.setPrioridad(Integer.valueOf(prioridad));
            nuevoProyecto.setDenominacion(denominacion);
            nuevoProyecto.setEntidad(entidad);
            nuevoProyecto.setTipoProyecto(tipoProy);
            nuevoProyecto.setFases(Integer.valueOf(fases));
            try {
                insertOK = m81Manager.crearProyecto(nuevoProyecto, adapt);
            } catch (Exception ex) {
                log.error("Se ha producido un error grabando un nuevo Proyecto : ", ex);
                codigoOperacion = "7";
            }

            if (insertOK) {
                log.debug("Proyecto insertado Correctamente");
                codigoOperacion = "0";
                try {
                    lista = m81Manager.getListaProyectos(numExp, adapt);
                    if (!lista.isEmpty()) {
                        for (ProyectoVO proy : lista) {
                            proy.setDescTipoProyecto(m81Utils.getDescripcionDesplegable(request, proy.getDescTipoProyecto()));
                        }
                    }
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    log.error("Error de tipo BD al recuperar la lista de Proyectos después de Insertar un Proyecto : " + bde.getMensaje());
                } catch (Exception ex) {
                    codigoOperacion = "5";
                    log.error("Error al recuperar la lista de Proyectos después de Insertar un Proyecto : " + ex.getMessage());
                }

            } else {
                log.error("NO se ha insertado correctamente el nuevo Proyecto");
                codigoOperacion = "1";
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando un nuevo Proyecto : ", ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
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
    public void modificarProyecto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarProyecto - ");
        String codigoOperacion = "-1";
        List<ProyectoVO> lista = new ArrayList<ProyectoVO>();
        ProyectoVO datModif = new ProyectoVO();
        boolean modOK = false;
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String prioridad = request.getParameter("prioridad");
            String denominacion = request.getParameter("denominacion");
            String entidad = request.getParameter("entidad");
            String tipoProy = request.getParameter("tipoProy");
            String fases = request.getParameter("fases");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la Contratacion a Modificar ");
                codigoOperacion = "3";
            } else {
                datModif.setId(Integer.parseInt(id));
                datModif.setNumExp(numExp);
                datModif.setPrioridad(Integer.valueOf(prioridad));
                datModif.setDenominacion(denominacion);
                datModif.setEntidad(entidad);
                datModif.setTipoProyecto(tipoProy);
                datModif.setFases(Integer.valueOf(fases));
                try {
                    modOK = m81Manager.modificarProyecto(datModif, adapt);
                } catch (Exception ex) {
                    log.error("Se ha producido un error modificando un Proyecto : ", ex);
                    codigoOperacion = "8";
                }
                if (modOK) {
                    log.debug("Proyecto modificado Correctamente");
                    codigoOperacion = "0";
                    try {
                        lista = m81Manager.getListaProyectos(numExp, adapt);
                        if (!lista.isEmpty()) {
                            for (ProyectoVO proy : lista) {
                                proy.setDescTipoProyecto(m81Utils.getDescripcionDesplegable(request, proy.getDescTipoProyecto()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de Proyectos después de Insertar un Proyecto : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de Proyectos después de Insertar un Proyecto : " + ex.getMessage());
                    }
                } else {
                    log.error("NO se ha modificado correctamente el Proyecto");
                    codigoOperacion = "1";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Se ha producido un error modificando un Proyecto : ", ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
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
    public void eliminarProyecto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarProyecto - ");
        String codigoOperacion = "-1";
        List<ProyectoVO> lista = new ArrayList<ProyectoVO>();
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Proyecto a Modificar ");
                codigoOperacion = "3";
            } else {
                String numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
                try {
                    if (m81Manager.eliminarProyecto(numExp, Integer.valueOf(id), adapt)) {
                        codigoOperacion = "0";
                        try {
                            lista = m81Manager.getListaProyectos(numExp, adapt);
                            if (!lista.isEmpty()) {
                                for (ProyectoVO proy : lista) {
                                    proy.setDescTipoProyecto(m81Utils.getDescripcionDesplegable(request, proy.getDescTipoProyecto()));
                                }
                            }
                        } catch (BDException bde) {
                            codigoOperacion = "1";
                            log.error("Error de tipo BD al recuperar la lista de Proyectos después de Insertar un Proyecto : " + bde.getMensaje());
                        } catch (Exception ex) {
                            codigoOperacion = "5";
                            log.error("Error al recuperar la lista de Proyectos después de Insertar un Proyecto : " + ex.getMessage());
                        }
                    } else {
                        codigoOperacion = "6";
                    }
                } catch (Exception ex) {
                    log.error("Error eliminando un Proyecto: " + ex);
                    codigoOperacion = "6";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error eliminando un Proyecto: " + ex);
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Error eliminando un Proyecto: " + ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
    }

    private void cargarDesplegablesProyecto(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaTipoProy = m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_TIPO_PROY, ConstantesMeLanbide81.FICHERO_PROPIEDADES), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaTipoProy.isEmpty()) {
            listaTipoProy = m81Utils.traducirDesplegable(request, listaTipoProy);
            request.setAttribute("listaTipoProy", listaTipoProy);
        }
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
    public String cargarNuevoPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoPuesto - ");
        String nuevo = "1";
        String urlnuevaContratacion = "/jsp/extension/melanbide81/puestos/m81PuestoMantenimiento.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }
            request.setAttribute("idProyecto", request.getParameter("idProyecto"));
            request.setAttribute("prioridad", request.getParameter("prioridad"));

        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva contratación : " + ex.getMessage());
        }
        return urlnuevaContratacion;
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
    public String cargarModificarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarPuesto - ");
        String nuevo = "0";
        String urlnuevaContratacion = "/jsp/extension/melanbide81/puestos/m81PuestoMantenimiento.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                PuestoVO datModif = m81Manager.getPuestoPorID(numExp, id, m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                    request.setAttribute("idProyecto", String.valueOf(datModif.getIdProyecto()));
                }
            }
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación : " + ex.getMessage());
        }
        return urlnuevaContratacion;

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
    public void crearNuevoPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoPuesto - ");
        String codigoOperacion = "-1";
        List<PuestoVO> lista = new ArrayList<PuestoVO>();
        PuestoVO nuevoPuesto = new PuestoVO();
        boolean insertOK = false;
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String numExp = request.getParameter("numExp");
            String prioridad = request.getParameter("prioridad");
            String denominacion = request.getParameter("denom");
            String duracion = request.getParameter("duracion").replace(",", ".");
            String porcJorn = request.getParameter("porcJorn").replace(",", ".");
            String numContratos = request.getParameter("numContratos");
            String coste = request.getParameter("coste").replace(",", ".");
            String subvencion = request.getParameter("subvencion").replace(",", ".");
            int idProyecto = Integer.valueOf(request.getParameter("idProyecto"));

            nuevoPuesto.setNumExp(numExp);
            nuevoPuesto.setIdProyecto(idProyecto);
            nuevoPuesto.setIdPrioridadProyecto(Integer.valueOf(prioridad));
            nuevoPuesto.setDenominacion(denominacion);
            nuevoPuesto.setPorcJorn(Double.valueOf(porcJorn));
            nuevoPuesto.setDuracion(Double.valueOf(duracion));
            nuevoPuesto.setNumContratos(Integer.valueOf(numContratos));
            nuevoPuesto.setCoste(Double.valueOf(coste));
            nuevoPuesto.setSubvencion(Double.valueOf(subvencion));

            try {
                insertOK = m81Manager.crearPuesto(nuevoPuesto, adapt);
            } catch (Exception ex) {
                log.error("Se ha producido un error grabando un nuevo Puesto : ", ex);
                codigoOperacion = "7";
            }
            if (insertOK) {
                log.debug("Puesto insertado Correctamente");
                codigoOperacion = "0";
                try {
                    lista = m81Manager.getListaPuestosxProyecto(numExp, idProyecto, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    log.error("Error de tipo BD al recuperar la lista de Puestos después de Insertar un Puesto : " + bde.getMensaje());
                } catch (Exception ex) {
                    codigoOperacion = "5";
                    log.error("Error al recuperar la lista de Puestos después de Insertar un Puesto : " + ex.getMessage());
                }
            } else {
                log.error("NO se ha insertado correctamente el nuevo Puesto");
                codigoOperacion = "1";
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("NO se ha insertado correctamente el nuevo Puesto");
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
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
    public void modificarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarPuesto - ");
        String codigoOperacion = "-1";
        List<PuestoVO> lista = new ArrayList<PuestoVO>();
        PuestoVO datModif = new PuestoVO();
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String id = request.getParameter("id");
            int idProyecto = Integer.valueOf(request.getParameter("idProyecto"));
            String numExp = request.getParameter("numExp");
            String prioridad = request.getParameter("prioridad");
            String denominacion = request.getParameter("denom");
            String duracion = request.getParameter("duracion").replace(",", ".");
            String porcJorn = request.getParameter("porcJorn").replace(",", ".");
            String numContratos = request.getParameter("numContratos");
            String coste = request.getParameter("coste").replace(",", ".");
            String subvencion = request.getParameter("subvencion").replace(",", ".");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Puesto a Modificar ");
                codigoOperacion = "3";
            } else {
                datModif.setId(Integer.parseInt(id));
                datModif.setIdProyecto(idProyecto);
                datModif.setNumExp(numExp);
                datModif.setIdPrioridadProyecto(Integer.valueOf(prioridad));
                datModif.setDenominacion(denominacion);
                datModif.setPorcJorn(Double.valueOf(porcJorn));
                datModif.setDuracion(Double.valueOf(duracion));
                datModif.setNumContratos(Integer.valueOf(numContratos));
                datModif.setCoste(Double.valueOf(coste));
                datModif.setSubvencion(Double.valueOf(subvencion));

                boolean modOK = false;
                try {
                    modOK = m81Manager.modificarPuesto(datModif, adapt);
                } catch (Exception ex) {
                    log.error("Se ha producido un error modificando un Puesto : ", ex);
                    codigoOperacion = "8";
                }
                if (modOK) {
                    log.debug("Puesto modificado Correctamente");
                    codigoOperacion = "0";
                    try {
                        lista = m81Manager.getListaPuestosxProyecto(numExp, idProyecto, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de Puestos después de modificar un Puesto : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de Puestos después de modificar un Puesto : " + ex.getMessage());
                    }
                } else {
                    log.error("NO se ha modificado correctamente el Puesto");
                    codigoOperacion = "1";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Se ha producido un error modificando un Puesto : ", ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
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
    public void eliminarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarPuesto - ");
        String codigoOperacion = "-1";
        List<PuestoVO> lista = new ArrayList<PuestoVO>();
        try {
            String id = request.getParameter("id");
            int idProyecto = Integer.valueOf(request.getParameter("idProyecto"));

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Puesto a eliminar ");
                codigoOperacion = "3";
            } else {
                String numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
                try {
                    if (m81Manager.eliminarPuesto(numExp, Integer.valueOf(id), adapt)) {
                        codigoOperacion = "0";
                        try {
                            lista = m81Manager.getListaPuestosxProyecto(numExp, idProyecto, adapt);
                        } catch (BDException bde) {
                            codigoOperacion = "1";
                            log.error("Error de tipo BD al recuperar la lista de Puestos después de modificar un Puesto : " + bde.getMensaje());
                        } catch (Exception ex) {
                            codigoOperacion = "5";
                            log.error("Error al recuperar la lista de Puestos después de modificar un Puesto : " + ex.getMessage());
                        }
                    } else {
                        codigoOperacion = "6";
                    }
                } catch (Exception ex) {
                    log.error("Error eliminando un Puesto: " + ex);
                    codigoOperacion = "6";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error eliminando un Puesto: " + ex);
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Error eliminando un Puesto: " + ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
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
    public String cargarNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevaContratacion - ");
        String nuevo = "1";
        String urlnuevaContratacion = "/jsp/extension/melanbide81/contrataciones/m81ContratacionMantenimiento.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }
            request.setAttribute("idProyecto", request.getParameter("idProyecto"));
            request.setAttribute("prioridad", request.getParameter("prioridad"));
            cargarDesplegablesContratacion(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de una nueva contratación : " + ex.getMessage());
        }
        return urlnuevaContratacion;
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
    public String cargarModificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarContratacion - ");
        String nuevo = "0";
        String urlnuevaContratacion = "/jsp/extension/melanbide81/contrataciones/m81ContratacionMantenimiento.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                ContratacionVO datModif = this.m81Manager.getContratacionPorID(numExp, id, m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                    request.setAttribute("idProyecto", String.valueOf(datModif.getIdProyecto()));
                }
            }
            cargarDesplegablesContratacion(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificaci: " + ex.getMessage());
        }
        return urlnuevaContratacion;
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
    public void crearNuevaContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevaContratacion - ");
        String codigoOperacion = "-1";
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        ContratacionVO nuevaContratacion = new ContratacionVO();
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String numExp = request.getParameter("numExp");
            String prioridad = request.getParameter("prioridad");
            String tipoDesempleado = request.getParameter("tipoDesempleado");
            String sexo = request.getParameter("sexo");
            String duracion = request.getParameter("duracionContrato").replace(",", ".");
            String porcJorn = request.getParameter("porcJorn").replace(",", ".");
            String numContratos = request.getParameter("numContratos");
            String subvencion = request.getParameter("subvencion").replace(",", ".");
            int idProyecto = Integer.valueOf(request.getParameter("idProyecto")).intValue();
            nuevaContratacion.setNumExp(numExp);
            nuevaContratacion.setIdProyecto(Integer.valueOf(idProyecto));
            nuevaContratacion.setIdPrioridadProyecto(Integer.valueOf(prioridad));
            nuevaContratacion.setTipoDesempleado(tipoDesempleado);
            nuevaContratacion.setSexo(sexo);
            nuevaContratacion.setDuracion(Double.valueOf(duracion));
            nuevaContratacion.setPorcJorn(Double.valueOf(porcJorn));
            nuevaContratacion.setNumContratos(Integer.valueOf(numContratos));
            nuevaContratacion.setSubvencion(Double.valueOf(subvencion));
            boolean insertOK = false;
            try {
                insertOK = this.m81Manager.crearContratacion(nuevaContratacion, adapt);
            } catch (Exception ex) {
                log.error("NO se ha insertado correctamente la nueva Contratacion");
                codigoOperacion = "7";
            }
            if (insertOK) {
                log.debug("Contratacion insertado Correctamente");
                codigoOperacion = "0";
                try {
                    lista = this.m81Manager.getListaContratacionesxProyecto(numExp, idProyecto, adapt);
                    if (!lista.isEmpty()) {
                        for (ContratacionVO cont : lista) {
                            cont.setDescTipoDesempleado(m81Utils.getDescripcionDesplegable(request, cont.getDescTipoDesempleado()));
                            cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                        }
                    }
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    log.error("Error de tipo BD al recuperar la lista de Contrataciones despude Insertar un Contratacion : " + bde.getMensaje());
                } catch (Exception ex) {
                    codigoOperacion = "5";
                    log.error("Error al recuperar la lista de Contrataciones despude Insertar una Contratacion : " + ex.getMessage());
                }
            } else {
                log.error("NO se ha insertado correctamente la nueva Contratacion");
                codigoOperacion = "7";
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parrecibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("NO se ha insertado correctamente la nueva Contratacion");
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON((new Gson()).toJson(resultado), response);
    }

    public void modificarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarContratacion - ");
        String codigoOperacion = "-1";
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        ContratacionVO datModif = new ContratacionVO();
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String id = request.getParameter("id");
            int idProyecto = Integer.valueOf(request.getParameter("idProyecto")).intValue();
            String numExp = request.getParameter("numExp");
            String prioridad = request.getParameter("prioridad");
            String tipoDesempleado = request.getParameter("tipoDesempleado");
            String sexo = request.getParameter("sexo");
            String duracion = request.getParameter("duracionContrato").replace(",", ".");
            String porcJorn = request.getParameter("porcJorn").replace(",", ".");
            String numContratos = request.getParameter("numContratos").replace(",", ".");
            String subvencion = request.getParameter("subvencion").replace(",", ".");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la Contratacia Modificar ");
                codigoOperacion = "3";
            } else {
                datModif.setId(Integer.valueOf(id));
                datModif.setNumExp(numExp);
                datModif.setIdProyecto(Integer.valueOf(idProyecto));
                datModif.setIdPrioridadProyecto(Integer.valueOf(prioridad));
                datModif.setTipoDesempleado(tipoDesempleado);
                datModif.setSexo(sexo);
                datModif.setDuracion(Double.valueOf(duracion));
                datModif.setPorcJorn(Double.valueOf(porcJorn));
                datModif.setNumContratos(Integer.valueOf(numContratos));
                datModif.setSubvencion(Double.valueOf(subvencion));
                boolean modOK = false;
                try {
                    modOK = this.m81Manager.modificarContratacion(datModif, adapt);
                } catch (Exception ex) {
                    log.error("NO se ha actualizado correctamente la Contratacion");
                    codigoOperacion = "8";
                }
                if (modOK) {
                    log.debug("Contratacion actualizada Correctamente");
                    codigoOperacion = "0";
                    try {
                        lista = this.m81Manager.getListaContratacionesxProyecto(numExp, idProyecto, adapt);
                        if (!lista.isEmpty()) {
                            for (ContratacionVO cont : lista) {
                                cont.setDescTipoDesempleado(m81Utils.getDescripcionDesplegable(request, cont.getDescTipoDesempleado()));
                                cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de Contrataciones despude Modificar una Contratacion : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de Contrataciones despude Modificar una Contratacion : " + ex.getMessage());
                    }
                } else {
                    log.error("NO se ha modificado correctamente la  Contratacion");
                    codigoOperacion = "8";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parrecibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("NO se ha actualizado correctamente la Contratacion");
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON((new Gson()).toJson(resultado), response);
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
    public void eliminarContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarContratacion - ");
        String codigoOperacion = "-1";
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        try {
            String id = request.getParameter("id");
            int idProyecto = Integer.valueOf(request.getParameter("idProyecto")).intValue();
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id de la Contratacia eliminar ");
                codigoOperacion = "3";
            } else {
                String numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
                try {
                    if (this.m81Manager.eliminarContratacion(numExp, Integer.valueOf(id).intValue(), adapt)) {
                        codigoOperacion = "0";
                        try {
                            lista = this.m81Manager.getListaContratacionesxProyecto(numExp, idProyecto, adapt);
                        } catch (BDException bde) {
                            codigoOperacion = "1";
                            log.error("Error de tipo BD al recuperar la lista de Contrataciones despude modificar una Contrataci: " + bde.getMensaje());
                        } catch (Exception ex) {
                            codigoOperacion = "5";
                            log.error("Error al recuperar la lista de Contrataciones despude modificar una Contrataci: " + ex.getMessage());
                        }
                    } else {
                        codigoOperacion = "6";
                    }
                } catch (Exception ex) {
                    log.error("NO se ha eliminado Contratacion");
                    codigoOperacion = "6";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error eliminando una Contrataci" + ex);
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Error eliminando una Contrataci" + ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON((new Gson()).toJson(resultado), response);
    }

    private void cargarDesplegablesContratacion(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaTipo = this.m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter("COD_TIPO_DESE", "MELANBIDE81"), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaTipo.isEmpty()) {
            listaTipo = m81Utils.traducirDesplegable(request, listaTipo);
            request.setAttribute("listaTipo", listaTipo);
        }
        List<DesplegableVO> listaSexo = this.m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter("COD_SEXO", "MELANBIDE81"), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaSexo.isEmpty()) {
            listaSexo = m81Utils.traducirDesplegable(request, listaSexo);
            request.setAttribute("listaSexo", listaSexo);
        }
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return urlNuevoTipo1
     */
    public String cargarNuevoTipo1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoTipo1 - ");
        String nuevo = "1";
        String urlNuevoTipo1 = "/jsp/extension/melanbide81/tipo_contrataciones/nuevoTipo1.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }
            //Cargamos los valores  de los desplegables
            cargarDesplegablesTipo1(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de un nuevo Tipo1 : " + ex.getMessage());
        }
        return urlNuevoTipo1;
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
    public String cargarModificarTipo1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarTipo1 - ");
        String nuevo = "0";
        String urlNuevoTipo1 = "/jsp/extension/melanbide81/tipo_contrataciones/nuevoTipo1.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                Tipo1VO datModif = m81Manager.getTipo1PorID(numExp, id, m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);

                }
            }
            //Cargamos los valores  de los desplegables
            cargarDesplegablesTipo1(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación  Tipo1: " + ex.getMessage());
        }
        return urlNuevoTipo1;

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
    public void crearNuevoTipo1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        log.info("Entramos en crearNuevoTipo1 - ");
        String codigoOperacion = "-1";
        List<Tipo1VO> lista = new ArrayList<Tipo1VO>();
        Tipo1VO nuevoTipo1 = new Tipo1VO();
        boolean insertOK = false;
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String numExp = request.getParameter("numExp");
            String entbene = request.getParameter("entbene");
            String entcontra = request.getParameter("entcontra");
            String cif = request.getParameter("cif");
            String ccc = request.getParameter("ccc");
            String denomproy = request.getParameter("denomproy");
            String denompuesto = request.getParameter("denompuesto");
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dni = request.getParameter("dni");
            String naf = request.getParameter("naf");
            String fecnacimiento = request.getParameter("fecnacimiento");
            String sexo = request.getParameter("sexo");
            String grupocot = request.getParameter("grupocot");
            String fecinicio = request.getParameter("fecinicio");
            String fecfin = request.getParameter("fecfin");
            String porcJorn = request.getParameter("porcJorn").replace(",", ".");
            String durcontrato = request.getParameter("durcontrato");
            String edad = request.getParameter("edad");
            String municipio = request.getParameter("municipio");
            String costesal = request.getParameter("costesal").replace(",", ".");
            String costess = request.getParameter("costess").replace(",", ".");
            String costetotal = request.getParameter("costetotal").replace(",", ".");
            String inscrita = request.getParameter("inscrita");
            String certinter = request.getParameter("certinter");
            String subconcedida = request.getParameter("subconcedida").replace(",", ".");
            String pago1 = request.getParameter("pago1").replace(",", ".");
            String subliquidada = request.getParameter("subliquidada").replace(",", ".");
            String pago2 = request.getParameter("pago2").replace(",", ".");
            String observaciones = request.getParameter("observaciones");

            nuevoTipo1.setNumExp(numExp);
            nuevoTipo1.setEntbene(entbene);
            nuevoTipo1.setEntcontra(entcontra);
            nuevoTipo1.setCif(cif);
            nuevoTipo1.setCcc(ccc);
            nuevoTipo1.setDenomproy(denomproy);
            nuevoTipo1.setDenompuesto(denompuesto);
            nuevoTipo1.setNombre(nombre);
            nuevoTipo1.setApellido1(apellido1);
            nuevoTipo1.setApellido2(apellido2);
            nuevoTipo1.setDni(dni);
            nuevoTipo1.setNaf(naf);

            if (fecnacimiento != null && !"".equals(fecnacimiento)) {
                nuevoTipo1.setFecnacimiento(new java.sql.Date(formatoFecha.parse(fecnacimiento).getTime()));
            }
            nuevoTipo1.setSexo(sexo);
            nuevoTipo1.setGrupocot(grupocot);

            if (fecinicio != null && !"".equals(fecinicio)) {
                nuevoTipo1.setFecinicio(new java.sql.Date(formatoFecha.parse(fecinicio).getTime()));
            }
            if (fecfin != null && !"".equals(fecfin)) {
                nuevoTipo1.setFecfin(new java.sql.Date(formatoFecha.parse(fecfin).getTime()));
            }

            nuevoTipo1.setPorcJorn(Double.valueOf(porcJorn));
            nuevoTipo1.setDurcontrato(durcontrato);
            nuevoTipo1.setEdad(Integer.valueOf(edad));
            nuevoTipo1.setMunicipio(municipio);

            nuevoTipo1.setCostesal(Double.valueOf(costesal));

            nuevoTipo1.setCostess(Double.valueOf(costess));

            nuevoTipo1.setCostetotal(Double.valueOf(costetotal));

            nuevoTipo1.setInscrita(inscrita);
            nuevoTipo1.setCertinter(certinter);
            nuevoTipo1.setSubconcedida((subconcedida != null && !subconcedida.isEmpty() ? Double.valueOf(subconcedida) : Double.valueOf("0")));
            nuevoTipo1.setPago1((pago1 != null && !pago1.isEmpty() ? Double.valueOf(pago1) : Double.valueOf("0")));
            nuevoTipo1.setSubliquidada((subliquidada != null && !subliquidada.isEmpty() ? Double.valueOf(subliquidada) : Double.valueOf("0")));
            nuevoTipo1.setPago2((pago2 != null && !pago2.isEmpty() ? Double.valueOf(pago2) : Double.valueOf("0")));
            nuevoTipo1.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);

            try {
                insertOK = m81Manager.crearNuevoTipo1(nuevoTipo1, adapt);
            } catch (Exception ex) {
                log.error("Se ha producido un error grabando un nuevo Tipo1 : ", ex);
                codigoOperacion = "7";
            }
            if (insertOK) {
                log.debug("Tipo1 insertado Correctamente");
                codigoOperacion = "0";
                try {
                    lista = m81Manager.getListaTipo1(numExp, adapt);
                    if (!lista.isEmpty()) {
                        for (Tipo1VO cont : lista) {
                            cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                            cont.setDescGrupocot(m81Utils.getDescripcionDesplegable(request, cont.getDescGrupocot()));
                            cont.setDescInscrita(m81Utils.getDescripcionDesplegable(request, cont.getDescInscrita()));
                        }
                    }
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    log.error("Error de tipo BD al recuperar la lista de Tipo1 después de Insertar un Tipo1 : " + bde.getMensaje());
                } catch (Exception ex) {
                    codigoOperacion = "5";
                    log.error("Error al recuperar la lista de Tipo1 después de Insertar un Tipo1 : " + ex.getMessage());
                }
            } else {
                log.error("NO se ha insertado correctamente el nuevo Tipo1");
                codigoOperacion = "1";
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("NO se ha insertado correctamente el nuevo Tipo1");
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
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
    public void modificarTipo1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        log.info("Entramos en modificarTipo1 - ");
        String codigoOperacion = "-1";
        List<Tipo1VO> lista = new ArrayList<Tipo1VO>();
        Tipo1VO datModif = new Tipo1VO();
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String id = request.getParameter("id");

            String numExp = request.getParameter("numExp");
            String entbene = request.getParameter("entbene");
            String entcontra = request.getParameter("entcontra");
            String cif = request.getParameter("cif");
            String ccc = request.getParameter("ccc");
            String denomproy = request.getParameter("denomproy");
            String denompuesto = request.getParameter("denompuesto");
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dni = request.getParameter("dni");
            String naf = request.getParameter("naf");
            String fecnacimiento = request.getParameter("fecnacimiento");
            String sexo = request.getParameter("sexo");
            String grupocot = request.getParameter("grupocot");
            String fecinicio = request.getParameter("fecinicio");
            String fecfin = request.getParameter("fecfin");
            String porcJorn = request.getParameter("porcJorn").replace(",", ".");
            String durcontrato = request.getParameter("durcontrato");
            String edad = request.getParameter("edad");
            String municipio = request.getParameter("municipio");
            String costesal = request.getParameter("costesal").replace(",", ".");
            String costess = request.getParameter("costess").replace(",", ".");
            String costetotal = request.getParameter("costetotal").replace(",", ".");
            String inscrita = request.getParameter("inscrita");
            String certinter = request.getParameter("certinter");
            String subconcedida = request.getParameter("subconcedida").replace(",", ".");
            String pago1 = request.getParameter("pago1").replace(",", ".");
            String subliquidada = request.getParameter("subliquidada").replace(",", ".");
            String pago2 = request.getParameter("pago2").replace(",", ".");
            String observaciones = request.getParameter("observaciones");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Tipo1 a Modificar ");
                codigoOperacion = "3";
            } else {

                datModif.setId(Integer.parseInt(id));
                datModif.setNumExp(numExp);
                datModif.setEntbene(entbene);
                datModif.setEntcontra(entcontra);
                datModif.setCif(cif);
                datModif.setCcc(ccc);
                datModif.setDenomproy(denomproy);
                datModif.setDenompuesto(denompuesto);
                datModif.setNombre(nombre);
                datModif.setApellido1(apellido1);
                datModif.setApellido2(apellido2);
                datModif.setDni(dni);
                datModif.setNaf(naf);

                if (fecnacimiento != null && !"".equals(fecnacimiento)) {
                    datModif.setFecnacimiento(new java.sql.Date(formatoFecha.parse(fecnacimiento).getTime()));
                }
                datModif.setSexo(sexo);
                datModif.setGrupocot(grupocot);

                if (fecinicio != null && !"".equals(fecinicio)) {
                    datModif.setFecinicio(new java.sql.Date(formatoFecha.parse(fecinicio).getTime()));
                }
                if (fecfin != null && !"".equals(fecfin)) {
                    datModif.setFecfin(new java.sql.Date(formatoFecha.parse(fecfin).getTime()));
                }

                datModif.setPorcJorn(Double.valueOf(porcJorn));
                datModif.setDurcontrato(durcontrato);
                datModif.setEdad(Integer.valueOf(edad));
                datModif.setMunicipio(municipio);
                datModif.setCostesal(Double.valueOf(costesal));

                datModif.setCostess(Double.valueOf(costess));

                datModif.setCostetotal(Double.valueOf(costetotal));

                datModif.setInscrita(inscrita);
                datModif.setCertinter(certinter);
                datModif.setSubconcedida((subconcedida != null && !subconcedida.isEmpty() ? Double.valueOf(subconcedida) : Double.valueOf("0")));
                datModif.setPago1((pago1 != null && !pago1.isEmpty() ? Double.valueOf(pago1) : Double.valueOf("0")));
                datModif.setSubliquidada((subliquidada != null && !subliquidada.isEmpty() ? Double.valueOf(subliquidada) : Double.valueOf("0")));
                datModif.setPago2((pago2 != null && !pago2.isEmpty() ? Double.valueOf(pago2) : Double.valueOf("0")));
                datModif.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);

                boolean modOK = false;
                try {
                    modOK = m81Manager.modificarTipo1(datModif, adapt);
                } catch (Exception ex) {
                    log.error("Se ha producido un error modificando un Tipo1 : ", ex);
                    codigoOperacion = "8";
                }
                if (modOK) {
                    log.debug("Tipo1 modificado Correctamente");
                    codigoOperacion = "0";
                    try {
                        lista = m81Manager.getListaTipo1(numExp, adapt);
                        if (!lista.isEmpty()) {
                            for (Tipo1VO cont : lista) {
                                cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                                cont.setDescGrupocot(m81Utils.getDescripcionDesplegable(request, cont.getDescGrupocot()));
                                cont.setDescInscrita(m81Utils.getDescripcionDesplegable(request, cont.getDescInscrita()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de Tipo1 después de modificar un Tipo1 : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de Tipo1 después de modificar un Tipo1 : " + ex.getMessage());
                    }
                } else {
                    log.error("NO se ha modificado correctamente el Tipo1");
                    codigoOperacion = "1";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Se ha producido un error modificando un Tipo1 : ", ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
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
    public void eliminarTipo1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarPuesto - ");
        String codigoOperacion = "-1";
        List<Tipo1VO> lista = new ArrayList<Tipo1VO>();
        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Puesto a eliminar ");
                codigoOperacion = "3";
            } else {
                String numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
                try {
                    if (m81Manager.eliminarTipo1(numExp, Integer.valueOf(id), adapt)) {
                        codigoOperacion = "0";
                        try {
                            lista = m81Manager.getListaTipo1(numExp, adapt);
                        } catch (BDException bde) {
                            codigoOperacion = "1";
                            log.error("Error de tipo BD al recuperar la lista de Puestos después de modificar un Tipo1 : " + bde.getMensaje());
                        } catch (Exception ex) {
                            codigoOperacion = "5";
                            log.error("Error al recuperar la lista de Puestos después de modificar un Tipo1 : " + ex.getMessage());
                        }
                    } else {
                        codigoOperacion = "6";
                    }
                } catch (Exception ex) {
                    log.error("Error eliminando un Tipo1: " + ex);
                    codigoOperacion = "6";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error eliminando un Tipo1: " + ex);
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Error eliminando un Tipo1: " + ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
    }

    private void cargarDesplegablesTipo1(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaSexo = m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_SEXO, ConstantesMeLanbide81.FICHERO_PROPIEDADES), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaSexo.isEmpty()) {
            listaSexo = m81Utils.traducirDesplegable(request, listaSexo);
            request.setAttribute("listaSexo", listaSexo);
        }
        List<DesplegableVO> listaGrupoCotizacion = m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_GCON, ConstantesMeLanbide81.FICHERO_PROPIEDADES), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaGrupoCotizacion.isEmpty()) {
            listaGrupoCotizacion = m81Utils.traducirDesplegable(request, listaGrupoCotizacion);
            request.setAttribute("listaGrupoCotizacion", listaGrupoCotizacion);
        }
        List<DesplegableVO> listaInscrita = m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_DES_BOOL, ConstantesMeLanbide81.FICHERO_PROPIEDADES), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (listaInscrita.size() > 0) {
            listaInscrita = m81Utils.traducirDesplegable(request, listaInscrita);
            request.setAttribute("listaInscrita", listaInscrita);
        }

    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return urlNuevoTipo2
     */
    public String cargarNuevoTipo2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarNuevoTipo2 - ");
        String nuevo = "1";
        String urlNuevoTipo2 = "/jsp/extension/melanbide81/tipo_contrataciones/nuevoTipo2.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                request.setAttribute("numExp", request.getParameter("numExp"));
            }
            //Cargamos los valores  de los desplegables
            cargarDesplegablesTipo2(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Se ha presentado un error al intentar preparar la jsp de un nuevo Tipo2 : " + ex.getMessage());
        }
        return urlNuevoTipo2;
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
    public String cargarModificarTipo2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarModificarTipo2 - ");
        String nuevo = "0";
        String urlNuevoTipo2 = "/jsp/extension/melanbide81/tipo_contrataciones/nuevoTipo2.jsp";
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                Tipo2VO datModif = m81Manager.getTipo2PorID(numExp, id, m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);

                }
            }
            //Cargamos los valores  de los desplegables
            cargarDesplegablesTipo2(codOrganizacion, request);
        } catch (Exception ex) {
            log.error("Error al tratar de preparar los datos para modificar y llamar la jsp de modificación  Tipo2: " + ex.getMessage());
        }
        return urlNuevoTipo2;

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
    public void crearNuevoTipo2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        log.info("Entramos en crearNuevoTipo2 - ");
        String codigoOperacion = "-1";
        List<Tipo2VO> lista = new ArrayList<Tipo2VO>();
        Tipo2VO nuevoTipo2 = new Tipo2VO();
        boolean insertOK = false;
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String numExp = request.getParameter("numExp");
            String entbene = request.getParameter("entbene");
            String empcontra = request.getParameter("empcontra");
            String cif = request.getParameter("cif");
            String ccc = request.getParameter("ccc");
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dni = request.getParameter("dni");
            String naf = request.getParameter("naf");
            String fecnacimiento = request.getParameter("fecnacimiento");
            String sexo = request.getParameter("sexo");
            String grupocot = request.getParameter("grupocot");
            String fecinicio = request.getParameter("fecinicio");
            String fecfin = request.getParameter("fecfin");
            String porcJorn = request.getParameter("porcJorn").replace(",", ".");
            String durcontrato = request.getParameter("durcontrato");
            String edad = request.getParameter("edad");
            String municipio = request.getParameter("municipio");
            String costesal = request.getParameter("costesal").replace(",", ".");
            String costess = request.getParameter("costess").replace(",", ".");
            String costetotal = request.getParameter("costetotal").replace(",", ".");
            String inscrita = request.getParameter("inscrita");
            String certinter = request.getParameter("certinter");
            String subconcedida = request.getParameter("subconcedida").replace(",", ".");
            String pago1 = request.getParameter("pago1").replace(",", ".");
            String subliquidada = request.getParameter("subliquidada").replace(",", ".");
            String pago2 = request.getParameter("pago2").replace(",", ".");
            String observaciones = request.getParameter("observaciones");
            String Tipocontrato = request.getParameter("Tipocontrato");
            String colectivo = request.getParameter("colectivo");

            nuevoTipo2.setNumExp(numExp);
            nuevoTipo2.setEntbene(entbene);
            nuevoTipo2.setEmpcontra(empcontra);
            nuevoTipo2.setCif(cif);
            nuevoTipo2.setCcc(ccc);
            nuevoTipo2.setNombre(nombre);
            nuevoTipo2.setApellido1(apellido1);
            nuevoTipo2.setApellido2(apellido2);
            nuevoTipo2.setDni(dni);
            nuevoTipo2.setNaf(naf);

            if (fecnacimiento != null && !"".equals(fecnacimiento)) {
                nuevoTipo2.setFecnacimiento(new java.sql.Date(formatoFecha.parse(fecnacimiento).getTime()));
            }
            nuevoTipo2.setSexo(sexo);
            nuevoTipo2.setGrupocot(grupocot);

            if (fecinicio != null && !"".equals(fecinicio)) {
                nuevoTipo2.setFecinicio(new java.sql.Date(formatoFecha.parse(fecinicio).getTime()));
            }
            if (fecfin != null && !"".equals(fecfin)) {
                nuevoTipo2.setFecfin(new java.sql.Date(formatoFecha.parse(fecfin).getTime()));
            }

            nuevoTipo2.setPorcJorn(Double.valueOf(porcJorn));
            nuevoTipo2.setDurcontrato(durcontrato);
            nuevoTipo2.setEdad(Integer.valueOf(edad));
            nuevoTipo2.setMunicipio(municipio);
            nuevoTipo2.setCostesal(Double.valueOf(costesal));
            nuevoTipo2.setCostess(Double.valueOf(costess));
            nuevoTipo2.setCostetotal(Double.valueOf(costetotal));
            nuevoTipo2.setInscrita(inscrita);
            nuevoTipo2.setCertinter(certinter);
            nuevoTipo2.setSubconcedida((subconcedida != null && !subconcedida.isEmpty() ? Double.valueOf(subconcedida) : Double.valueOf("0")));
            nuevoTipo2.setPago1((pago1 != null && !pago1.isEmpty() ? Double.valueOf(pago1) : Double.valueOf("0")));
            nuevoTipo2.setSubliquidada((subliquidada != null && !subliquidada.isEmpty() ? Double.valueOf(subliquidada) : Double.valueOf("0")));
            nuevoTipo2.setPago2((pago2 != null && !pago2.isEmpty() ? Double.valueOf(pago2) : Double.valueOf("0")));
            nuevoTipo2.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);           
            nuevoTipo2.setTipocontrato(Tipocontrato);
            nuevoTipo2.setColectivo(colectivo);

            try {
                insertOK = m81Manager.crearNuevoTipo2(nuevoTipo2, adapt);
            } catch (Exception ex) {
                log.error("Se ha producido un error grabando un nuevo Tipo2 : ", ex);
                codigoOperacion = "7";
            }
            if (insertOK) {
                log.debug("Tipo2 insertado Correctamente");
                codigoOperacion = "0";
                try {
                    lista = m81Manager.getListaTipo2(numExp, adapt);
                    if (!lista.isEmpty()) {
                        for (Tipo2VO cont : lista) {
                            cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                            cont.setDescGrupocot(m81Utils.getDescripcionDesplegable(request, cont.getDescGrupocot()));
                            cont.setDescInscrita(m81Utils.getDescripcionDesplegable(request, cont.getDescInscrita()));
                            cont.setDescTipocontrato(m81Utils.getDescripcionDesplegable(request, cont.getDescTipocontrato()));
                            cont.setDescColectivo(m81Utils.getDescripcionDesplegable(request, cont.getDescColectivo()));
                        }
                    }
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    log.error("Error de tipo BD al recuperar la lista de Tipo2 después de Insertar un Tipo2 : " + bde.getMensaje());
                } catch (Exception ex) {
                    codigoOperacion = "5";
                    log.error("Error al recuperar la lista de Tipo2 después de Insertar un Tipo2 : " + ex.getMessage());
                }
            } else {
                log.error("NO se ha insertado correctamente el nuevo Tipo2");
                codigoOperacion = "1";
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("NO se ha insertado correctamente el nuevo Tipo2");
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
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
    public void modificarTipo2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws ParseException {
        log.info("Entramos en modificarTipo2 - ");
        String codigoOperacion = "-1";
        List<Tipo2VO> lista = new ArrayList<Tipo2VO>();
        Tipo2VO datModif = new Tipo2VO();
        try {
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String id = request.getParameter("id");

            String numExp = request.getParameter("numExp");
            String entbene = request.getParameter("entbene");
            String empcontra = request.getParameter("empcontra");
            String cif = request.getParameter("cif");
            String ccc = request.getParameter("ccc");
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            String dni = request.getParameter("dni");
            String naf = request.getParameter("naf");
            String fecnacimiento = request.getParameter("fecnacimiento");
            String sexo = request.getParameter("sexo");
            String grupocot = request.getParameter("grupocot");
            String fecinicio = request.getParameter("fecinicio");
            String fecfin = request.getParameter("fecfin");
            String porcJorn = request.getParameter("porcJorn").replace(",", ".");
            String durcontrato = request.getParameter("durcontrato");
            String edad = request.getParameter("edad");
            String municipio = request.getParameter("municipio");
            String costesal = request.getParameter("costesal").replace(",", ".");
            String costess = request.getParameter("costess").replace(",", ".");
            String costetotal = request.getParameter("costetotal").replace(",", ".");
            String inscrita = request.getParameter("inscrita");
            String certinter = request.getParameter("certinter");
            String subconcedida = request.getParameter("subconcedida").replace(",", ".");
            String pago1 = request.getParameter("pago1").replace(",", ".");
            String subliquidada = request.getParameter("subliquidada").replace(",", ".");
            String pago2 = request.getParameter("pago2").replace(",", ".");
            String observaciones = request.getParameter("observaciones");
            String tipocontrato = request.getParameter("tipocontrato");
            String colectivo = request.getParameter("colectivo");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Tipo2 a Modificar ");
                codigoOperacion = "3";
            } else {

                datModif.setNumExp(numExp);
                datModif.setId(Integer.parseInt(id));
                datModif.setEntbene(entbene);
                datModif.setEmpcontra(empcontra);
                datModif.setCif(cif);
                datModif.setCcc(ccc);
                datModif.setNombre(nombre);
                datModif.setApellido1(apellido1);
                datModif.setApellido2(apellido2);
                datModif.setDni(dni);
                datModif.setNaf(naf);

                if (fecnacimiento != null && !"".equals(fecnacimiento)) {
                    datModif.setFecnacimiento(new java.sql.Date(formatoFecha.parse(fecnacimiento).getTime()));
                }
                datModif.setSexo(sexo);
                datModif.setGrupocot(grupocot);

                if (fecinicio != null && !"".equals(fecinicio)) {
                    datModif.setFecinicio(new java.sql.Date(formatoFecha.parse(fecinicio).getTime()));
                }
                if (fecfin != null && !"".equals(fecfin)) {
                    datModif.setFecfin(new java.sql.Date(formatoFecha.parse(fecfin).getTime()));
                }

                datModif.setPorcJorn(Double.valueOf(porcJorn));
                datModif.setDurcontrato(durcontrato);
                datModif.setEdad(Integer.valueOf(edad));
                datModif.setMunicipio(municipio);
                datModif.setCostesal(Double.valueOf(costesal));
                datModif.setCostess(Double.valueOf(costess));
                datModif.setCostetotal(Double.valueOf(costetotal));
                datModif.setInscrita(inscrita);
                datModif.setCertinter(certinter);
                datModif.setSubconcedida((subconcedida != null && !subconcedida.isEmpty() ? Double.valueOf(subconcedida) : Double.valueOf("0")));
                datModif.setPago1((pago1 != null && !pago1.isEmpty() ? Double.valueOf(pago1) : Double.valueOf("0")));
                datModif.setSubliquidada((subliquidada != null && !subliquidada.isEmpty() ? Double.valueOf(subliquidada) : Double.valueOf("0")));
                datModif.setPago2((pago2 != null && !pago2.isEmpty() ? Double.valueOf(pago2) : Double.valueOf("0")));                             
                datModif.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);
                datModif.setTipocontrato(tipocontrato);
                datModif.setColectivo(colectivo);

                boolean modOK = false;
                try {
                    modOK = m81Manager.modificarTipo2(datModif, adapt);
                } catch (Exception ex) {
                    log.error("Se ha producido un error modificando un Tipo2 : ", ex);
                    codigoOperacion = "8";
                }
                if (modOK) {
                    log.debug("Puesto modificado Correctamente");
                    codigoOperacion = "0";
                    try {
                        lista = m81Manager.getListaTipo2(numExp, adapt);
                        if (!lista.isEmpty()) {
                            for (Tipo2VO cont : lista) {
                                cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                                cont.setDescGrupocot(m81Utils.getDescripcionDesplegable(request, cont.getDescGrupocot()));
                                cont.setDescInscrita(m81Utils.getDescripcionDesplegable(request, cont.getDescInscrita()));
                                cont.setDescTipocontrato(m81Utils.getDescripcionDesplegable(request, cont.getDescTipocontrato()));
                                cont.setDescColectivo(m81Utils.getDescripcionDesplegable(request, cont.getDescColectivo()));
                            }
                        }
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.error("Error de tipo BD al recuperar la lista de Tipo2 después de modificar un Tipo2 : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.error("Error al recuperar la lista de Tipo2 después de modificar un Tipo2 : " + ex.getMessage());
                    }
                } else {
                    log.error("NO se ha modificado correctamente el PueTipo2sto");
                    codigoOperacion = "1";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Se ha producido un error modificando un Tipo2 : ", ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
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
    public void eliminarTipo2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarTipo2 - ");
        String codigoOperacion = "-1";
        List<Tipo2VO> lista = new ArrayList<Tipo2VO>();
        try {
            String id = request.getParameter("id");

            if (id == null || id.isEmpty()) {
                log.error("No se ha recibido desde la JSP el id del Tipo2 a eliminar ");
                codigoOperacion = "3";
            } else {
                String numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
                try {
                    if (m81Manager.eliminarTipo2(numExp, Integer.valueOf(id), adapt)) {
                        codigoOperacion = "0";
                        try {
                            lista = m81Manager.getListaTipo2(numExp, adapt);
                        } catch (BDException bde) {
                            codigoOperacion = "1";
                            log.error("Error de tipo BD al recuperar la lista de Puestos después de modificar un Tipo2 : " + bde.getMensaje());
                        } catch (Exception ex) {
                            codigoOperacion = "5";
                            log.error("Error al recuperar la lista de Puestos después de modificar un Tipo2 : " + ex.getMessage());
                        }
                    } else {
                        codigoOperacion = "6";
                    }
                } catch (Exception ex) {
                    log.error("Error eliminando un Tipo2: " + ex);
                    codigoOperacion = "6";
                }
            }
        } catch (NumberFormatException ex) {
            log.error("Error eliminando un Tipo2: " + ex);
            codigoOperacion = "3";
        } catch (SQLException ex) {
            log.error("Error eliminando un Tipo2: " + ex);
            codigoOperacion = "1";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
    }

    private void cargarDesplegablesTipo2(int codOrganizacion, HttpServletRequest request) throws SQLException, Exception {
        List<DesplegableVO> listaSexo = m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_SEXO, ConstantesMeLanbide81.FICHERO_PROPIEDADES), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaSexo.isEmpty()) {
            listaSexo = m81Utils.traducirDesplegable(request, listaSexo);
            request.setAttribute("listaSexo", listaSexo);
        }

        List<DesplegableVO> listaGrupoCotizacion = m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_GCON, ConstantesMeLanbide81.FICHERO_PROPIEDADES), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaGrupoCotizacion.isEmpty()) {
            listaGrupoCotizacion = m81Utils.traducirDesplegable(request, listaGrupoCotizacion);
            request.setAttribute("listaGrupoCotizacion", listaGrupoCotizacion);
        }
        List<DesplegableVO> listaInscrita = m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_DES_BOOL, ConstantesMeLanbide81.FICHERO_PROPIEDADES), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (listaInscrita.size() > 0) {
            listaInscrita = m81Utils.traducirDesplegable(request, listaInscrita);
            request.setAttribute("listaInscrita", listaInscrita);
        }
        List<DesplegableVO> listaTipoContrato = m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_DES_TCLE, ConstantesMeLanbide81.FICHERO_PROPIEDADES), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaTipoContrato.isEmpty()) {
            listaTipoContrato = m81Utils.traducirDesplegable(request, listaTipoContrato);
            request.setAttribute("listaTipoContrato", listaTipoContrato);
        }
        List<DesplegableVO> listaColectivo = m81Manager.getValoresDesplegables(ConfigurationParameter.getParameter(ConstantesMeLanbide81.COD_DES_CLEP, ConstantesMeLanbide81.FICHERO_PROPIEDADES), m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        if (!listaColectivo.isEmpty()) {
            listaColectivo = m81Utils.traducirDesplegable(request, listaColectivo);
            request.setAttribute("listaColectivo", listaColectivo);
        }

    }
    public String procesarXMLTipo1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en procesarXMLTipo1" + this.getClass().getSimpleName());
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
            if (!"".equals(xml)) {
                cargarExpedienteExtension(codOrganizacion, numExpediente, xml);
            }

            request.getSession().setAttribute("mensajeImportar", MeLanbide81I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
        } catch (DataFormatException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE81.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE81.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE81.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE81.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide81I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarLista.jsp");
        return "/jsp/extension/melanbide81/tipo_contrataciones/recargarLista.jsp";
    }
     public String procesarXMLTipo2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en procesarXMLTipo2" + this.getClass().getSimpleName());
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
            FormFile fichero = table.get("fichero_xml2");
            //Use commons-fileupload to obtain the byte[] of the file (in a servlet of yours)
            byte[] contenido = fichero.getFileData();
            String fileName = fichero.getFileName();
            log.debug("fileName: " + fileName);
            String xml = new String(contenido);
            if (!"".equals(xml)) {
                cargarExpedienteExtension(codOrganizacion, numExpediente, xml);
            }

            request.getSession().setAttribute("mensajeImportar", MeLanbide81I18n.getInstance().getMensaje(codIdioma, "msg.registrosCargadosOK"));
        } catch (DataFormatException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE81.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE81.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE81.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE81.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide81I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        log.debug("Llama a recargarListaTipo2.jsp");
        return "/jsp/extension/melanbide81/tipo_contrataciones/recargarListaTipo2.jsp";
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
    public void actualizarPestanaTipo1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en actualizarPestana de " + this.getClass().getSimpleName());
        String codigoOperacion = "-1";
        List<Tipo1VO> lista = new ArrayList<Tipo1VO>();
        String numExp = "";
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide81Manager.getInstance().getListaTipo1(numExp,  adapt);
            if (lista.isEmpty()) {
                log.error("No se ha cargado ningún registro para el expediente " + numExp + " después de cargar el XML");
                codigoOperacion = "4";
            } else {
                codigoOperacion = "0";
                 for (Tipo1VO cont : lista) {
                                cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                                cont.setDescGrupocot(m81Utils.getDescripcionDesplegable(request, cont.getDescGrupocot()));
                                cont.setDescInscrita(m81Utils.getDescripcionDesplegable(request, cont.getDescInscrita()));                
                }
            }
        } catch (BDException bde) {
            codigoOperacion = "1";
            log.error("Error de tipo BD al recuperar la lista de Personas después de cargar el XML : " + bde.getMensaje());
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error al recuperar la lista de Personas después de cargar el XML : " + ex.getMessage());
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
    }
    public void actualizarPestanaTipo2(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info(">>>>>>> ENTRA en actualizarPestana de " + this.getClass().getSimpleName());
        String codigoOperacion = "-1";
        List<Tipo2VO> lista = new ArrayList<Tipo2VO>();
        String numExp = "";
        try {
            numExp = request.getParameter("numExp");
            AdaptadorSQLBD adapt = m81Utils.getAdaptSQLBD(String.valueOf(codOrganizacion));
            lista = MeLanbide81Manager.getInstance().getListaTipo2(numExp,  adapt);
            if (lista.isEmpty()) {
                log.error("No se ha cargado ningún registro para el expediente " + numExp + " después de cargar el XML");
                codigoOperacion = "4";
            } else {
                codigoOperacion = "0";
                 for (Tipo2VO cont : lista) {
                                cont.setDescSexo(m81Utils.getDescripcionDesplegable(request, cont.getDescSexo()));
                                cont.setDescGrupocot(m81Utils.getDescripcionDesplegable(request, cont.getDescGrupocot()));
                                cont.setDescTipocontrato(m81Utils.getDescripcionDesplegable(request, cont.getDescTipocontrato()));
                                cont.setDescColectivo(m81Utils.getDescripcionDesplegable(request, cont.getDescColectivo()));
                                cont.setDescInscrita(m81Utils.getDescripcionDesplegable(request, cont.getDescInscrita()));
                
                }
            }
        } catch (BDException bde) {
            codigoOperacion = "1";
            log.error("Error de tipo BD al recuperar la lista de Personas después de cargar el XML : " + bde.getMensaje());
        } catch (Exception ex) {
            codigoOperacion = "2";
            log.error("Error al recuperar la lista de Personas después de cargar el XML : " + ex.getMessage());
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        m81Utils.retornarJSON(new Gson().toJson(resultado), response);
    }
 
}
