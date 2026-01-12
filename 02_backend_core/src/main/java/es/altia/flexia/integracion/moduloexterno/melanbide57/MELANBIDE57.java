package es.altia.flexia.integracion.moduloexterno.melanbide57;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide57.manager.MeLanbide57Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide57.manager.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide57.manager.util.ConstantesMelanbide57;
import es.altia.flexia.integracion.moduloexterno.melanbide57.util.MeLanbide57Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeGeneralVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeInternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeRGIVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.TramiteVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 13-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class MELANBIDE57 extends ModuloIntegracionExterno{
    
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE57.class);
    
    //Paleta de colores
    private static final XSSFColor colorAzulGris = new XSSFColor(new byte[] { (byte) 198, (byte) 217, (byte) 241});//red, green, blue
    private static final XSSFColor colorLima = new XSSFColor(new byte[] { (byte) 195, (byte) 214, (byte) 155});//red, green, blue
    private static final XSSFColor colorLavanda = new XSSFColor(new byte[] { (byte) 204, (byte) 193, (byte) 218});//red, green, blue
    private static final XSSFColor colorTurquesa = new XSSFColor(new byte[] { (byte) 147, (byte) 205, (byte) 221});//red, green, blue
    private static final XSSFColor colorMarron = new XSSFColor(new byte[] { (byte) 127, (byte) 127, (byte) 127});//red, green, blue
    private static XSSFFont estiloFontCabecera;
    private static XSSFFont estiloFontDatos;
    private static XSSFCellStyle estiloCellMeses;
    private static XSSFCellStyle estiloCellTotal;
    private static XSSFCellStyle estiloCellTotalBloq;
    private static XSSFCellStyle estiloCellDerivadas;
    private static XSSFCellStyle estiloCellCabecera;
    private static XSSFCellStyle estiloCellResueltas;
    private static XSSFCellStyle estiloCellDatos;
    

    /**
     * Prepara la pantalla que visualizar áreas asignadas a la queja en la pestaña Datos supl
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return String con el forward a la JSP
     */
    public String datosSuplAreas (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response){
        if(log.isDebugEnabled()) log.debug("datosSuplAreas ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        //acceder a BD para obtener los nombres de las Areas que hay que sustituir en el nombre de los trámites
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        if (adapt != null) {
            Connection con = null;
            try
            {
                con = adapt.getConnection();
                List<TramiteVO> listaTramitesAreas  = MeLanbide57Manager.getInstance().getListaTramitesAreas(codOrganizacion, ocurrenciaTramite, numExpediente, con);
                request.setAttribute("listaTramitesAreas", listaTramitesAreas);
            }
            catch(Exception e)
            {
                log.error("Error al recuperar tramitesAreas: " + e.getMessage());
            }
            finally
            {
                try
                {
                    adapt.devolverConexion(con);       
                }
                catch(Exception e)
                {
                    log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                }
            }
        }
        
        
    
    //sustituir     TML_VALOR por UOR_NOM
        /*SELECT A_UOR.UOR_NOM,  E_TML.TML_VALOR
        FROM E_CRO INNER JOIN A_UOR ON E_CRO.CRO_UTR = A_UOR.UOR_COD INNER JOIN E_TRA ON (E_CRO.CRO_TRA=E_TRA.TRA_COD AND E_CRO.CRO_PRO=E_TRA.TRA_PRO AND  E_CRO.CRO_MUN=E_TRA.TRA_MUN) INNER JOIN E_TML ON (E_TRA.TRA_COD=E_TML.TML_TRA AND E_TRA.TRA_PRO=E_TML.TML_PRO AND  E_TRA.TRA_MUN=E_TML.TML_MUN)
        WHERE E_CRO.CRO_NUM    = numExpediente
        AND E_CRO.CRO_OCU = ocurrenciaTramite
        AND E_CRO.CRO_MUN = codOrganizacion
        AND (E_TRA.TRA_COU =codTraGestionAreaAsignada1
  OR E_TRA.TRA_COU   =codTraGestionAreaAsignada2
  OR E_TRA.TRA_COU   =codTraGestionAreaAsignada3
  OR E_TRA.TRA_COU   =codTraGestionAreaAsignada4)
AND E_TML.TML_LENG=1*/
        if(log.isDebugEnabled()) log.debug("datosSuplAreas() : END");
        return "/jsp/extension/melanbide57/melanbide57.jsp";
    }

    public String cargarPantallaPrincipal(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaPrincipal de " + this.getClass().getName());
        String url = "/jsp/extension/melanbide57/generarEstadisticas.jsp";

        return url;
    }

    public String cargarPantallaInformeRGI(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaInformeRGI de " + this.getClass().getName());
        String url = "/jsp/extension/melanbide57/informeRGI.jsp";

        return url;
    }

   private void creaEstiloCellComun(XSSFCellStyle estilo) throws Exception{
        estilo.setWrapText(true);
        estilo.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        estilo.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        estilo.setBottomBorderColor((short) 8);
        estilo.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        estilo.setLeftBorderColor((short) 8);
        estilo.setBorderRight(XSSFCellStyle.BORDER_THIN);
        estilo.setRightBorderColor((short) 8);
        estilo.setBorderTop(XSSFCellStyle.BORDER_THIN);
        estilo.setTopBorderColor((short) 8);
        estilo.setAlignment(XSSFCellStyle.ALIGN_CENTER);
    }

    private void crearEstilos(XSSFWorkbook libro)  throws Exception{
        estiloFontCabecera = libro.createFont();
        estiloFontCabecera.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        estiloFontCabecera.setFontName("Arial");
        estiloFontCabecera.setFontHeightInPoints((short) 8);

        estiloFontDatos = libro.createFont();
        estiloFontDatos.setFontName("Arial");
        estiloFontDatos.setFontHeightInPoints((short) 8);

        estiloCellMeses = libro.createCellStyle();
        estiloCellMeses.setFillForegroundColor(colorAzulGris);
        estiloCellMeses.setFont(estiloFontCabecera);
        creaEstiloCellComun(estiloCellMeses);

        estiloCellTotal = libro.createCellStyle();
        estiloCellTotal.setFillForegroundColor(colorTurquesa);
        estiloCellTotal.setFont(estiloFontCabecera);
        creaEstiloCellComun(estiloCellTotal);

        estiloCellTotalBloq = libro.createCellStyle();
        estiloCellTotalBloq.setFillForegroundColor(colorMarron);
        estiloCellTotalBloq.setFont(estiloFontCabecera);
        creaEstiloCellComun(estiloCellTotalBloq);

        estiloCellDerivadas = libro.createCellStyle();
        estiloCellDerivadas.setFillForegroundColor(colorLima);
        estiloCellDerivadas.setFont(estiloFontCabecera);
        creaEstiloCellComun(estiloCellDerivadas);

        estiloCellCabecera = estiloCellDerivadas;

        estiloCellResueltas = libro.createCellStyle();
        estiloCellResueltas.setFillForegroundColor(colorLavanda);
        estiloCellResueltas.setFont(estiloFontCabecera);
        creaEstiloCellComun(estiloCellResueltas);

        estiloCellDatos = libro.createCellStyle();
        estiloCellDatos.setFillForegroundColor(IndexedColors.WHITE.index);
        estiloCellDatos.setFont(estiloFontDatos);
        creaEstiloCellComun(estiloCellDatos);
    }

    /**
     * muestra el nº de expedientes derivados a cada unidad tramitadora y el nº expedientes resueltos a su vez (por cada mes entre las fechas seleccionadas en el filtro)
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void descargarInformeInterno(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        if(log.isDebugEnabled()) log.debug("descargarInformeInterno() : BEGIN");
        Date mesDesde;
        Date mesHasta;
        SimpleDateFormat sdfFechaMesAnyo = new SimpleDateFormat("MM/yyyy");
        String strFechaDesde;
        String strFechaHasta;
        int numFila = 0;
        final int numColumnasMerge = 3;
        try
        {
            strFechaDesde = (String)request.getParameter("meLanbide57FechaDesde");
            strFechaHasta = (String)request.getParameter("meLanbide57FechaHasta");
            //se supone que las fechas de inicio y fin son obligatorias
            mesDesde = sdfFechaMesAnyo.parse(strFechaDesde.substring(3));
            mesHasta = sdfFechaMesAnyo.parse(strFechaHasta.substring(3));
            
            XSSFWorkbook libro = new XSSFWorkbook();
            
            /*************************
            *ESTILOS COMUNES DE CELDAS
            *************************/
            crearEstilos(libro);
            
            XSSFSheet hoja = hoja = libro.createSheet("INFORME INTERNO");
            /*********
            *CABECERAS
            *********/
            //MESES (fila 1)
                int contCelda = 0;
                XSSFRow fila = hoja.createRow(numFila++);
                //celda en blanco
                XSSFCell celda = fila.createCell(contCelda++);
                celda.setCellStyle(estiloCellDatos);
                //celda en blanco
                celda = fila.createCell(contCelda++);
                celda.setCellStyle(estiloCellDatos);
                //celdas de meses
                GregorianCalendar gcal = new GregorianCalendar();
                gcal.setTime(mesDesde);
                HashMap<String, Integer> mapColumnMesAnyo = new HashMap<String, Integer>();
                HashMap<String, Integer> mapRowUnidad = new HashMap<String, Integer>();
                StringBuilder mesAnyo = new StringBuilder();
                
                contCelda = creaCeldasMeses(hoja, fila, celda, contCelda, mapColumnMesAnyo, mesHasta, gcal, numColumnasMerge, numFila);

                //celda total
                celda = fila.createCell(contCelda++);
                celda.setCellValue("TOTAL");
                celda.setCellStyle(estiloCellTotal);
                //creo dos columnas adicionales para combinarlas después
                celda = fila.createCell(contCelda++);
                celda.setCellStyle(estiloCellTotal);
                celda = fila.createCell(contCelda++);
                celda.setCellStyle(estiloCellTotal);
                //unimos las tres últimas celdas creadas en la fila actual
                hoja.addMergedRegion(new CellRangeAddress(numFila-1, numFila-1, contCelda-numColumnasMerge, contCelda-1));

            //DERIVADAS / RESUELTAS (fila 2)
                contCelda = 0;
                fila = hoja.createRow(numFila++);
                //derivadas
                celda = fila.createCell(contCelda++);
                celda.setCellValue("Q/S DERIVADAS A");
                celda.setCellStyle(estiloCellDerivadas);
                //resueltas
                celda = fila.createCell(contCelda++);
                celda.setCellValue("Q/S RESUELTAS POR");
                celda.setCellStyle(estiloCellResueltas);
                //TODO: de momento recorro los meses como en el primer while: depende de cómo saque los datos más tarde mirar si cambiar la forma
                gcal = new GregorianCalendar();
                gcal.setTime(mesDesde);
                while (!gcal.getTime().after(mesHasta)) {
                    gcal.add(Calendar.MONTH, 1);
                    celda = fila.createCell(contCelda++);
                    celda.setCellValue("Nº");
                    celda.setCellStyle(estiloCellDerivadas);
                    celda = fila.createCell(contCelda++);
                    celda.setCellValue("Nº");
                    celda.setCellStyle(estiloCellResueltas);
                    celda = fila.createCell(contCelda++);
                    celda.setCellValue("%");
                    celda.setCellStyle(estiloCellResueltas);
                }
                celda = fila.createCell(contCelda++);
                celda.setCellValue("D");
                celda.setCellStyle(estiloCellTotal);
                celda = fila.createCell(contCelda++);
                celda.setCellValue("R");
                celda.setCellStyle(estiloCellTotal);
                celda = fila.createCell(contCelda++);
                celda.setCellValue("%");
                celda.setCellStyle(estiloCellTotal);
                
                
            /******
            *DATOS
            ******/
            //recupero los datos a mostrar
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (adapt != null) {
                Connection con = null;
                try
                {
                    con = adapt.getConnection();
                    String codigoTramiteRevision = ConfigurationParameter.getParameter("CODIGO_TRAMITE_REVISION_GRABACION_DATOS",this.getNombreModulo());
                    String codigoDesplegableRespuesta1 = ConfigurationParameter.getParameter("CODIGO_CAMPO_RESPUESTA1",this.getNombreModulo());
                    String codigoDesplegableRespuesta2 = ConfigurationParameter.getParameter("CODIGO_CAMPO_RESPUESTA2",this.getNombreModulo());
                    String codigoDesplegableRespuesta3 = ConfigurationParameter.getParameter("CODIGO_CAMPO_RESPUESTA3",this.getNombreModulo());
                    String codigoDesplegableRespuesta4 = ConfigurationParameter.getParameter("CODIGO_CAMPO_RESPUESTA4",this.getNombreModulo());
                    /* 
                    * nº expedientes derivados: por cada unidad tramitadora (del conjunto de trámites 301, 302, 303, 304 del procedimiento QUEJA) saco el nº expedientes (por cada mes de entre las fechas del filtro) que tengan asociados uno o varios de dichos trámites (301,302,303,304) y que además cumplan que el campo FECHAQUEJA, del trámite inicial del procedimiento, tenga un valor comprendido entre las fechas desde - hasta indicadas en el filtro
                    * nº expedientes resueltos: lo mismo que los derivados pero además deben cumplir que el campo RESUELTA de los trámites 301,302,303,304 (RESUELTA1, RESUELTA2, RESUELTA3, RESUELTA4) tenga valor "S", es decir, que esté resuelta...
                    */
                    List<InformeInternoVO> listaDatosInformeInternoDerivadas  = MeLanbide57Manager.getInstance().getListaDatosInformeInternoDerivadas(codOrganizacion, ocurrenciaTramite, strFechaDesde, strFechaHasta, codigoTramiteRevision, con);
                    List<InformeInternoVO> listaDatosInformeInternoResueltas  = MeLanbide57Manager.getInstance().getListaDatosInformeInternoResueltas(codOrganizacion, ocurrenciaTramite, strFechaDesde, strFechaHasta, codigoTramiteRevision, codigoDesplegableRespuesta1, codigoDesplegableRespuesta2, codigoDesplegableRespuesta3, codigoDesplegableRespuesta4, con);
                    if((listaDatosInformeInternoDerivadas!=null && !listaDatosInformeInternoDerivadas.isEmpty())){//sólo miro las derivadas porque si no hay derivadas no hay resueltas
                        //la lista de datosInformeGeneral está ordenada por unidades pero no por meses y años
                        String uoCodigoAux = "";
                        StringBuilder strFormula = new StringBuilder();
                        StringBuilder strFormulaTotalD = new StringBuilder();
                        StringBuilder strFormulaTotalR = new StringBuilder();
                        StringBuilder strColumnaD = new StringBuilder();
                        StringBuilder strColumnaR = new StringBuilder();
                        for (InformeInternoVO datosInformeInterno : listaDatosInformeInternoDerivadas) {
                            if(!uoCodigoAux.equals(datosInformeInterno.getUoCodigo())){//por cada unidad se crea una fila
                                fila = hoja.createRow(numFila++);
                                //guardo el nº fila de la unidad
                                mapRowUnidad.put(datosInformeInterno.getUoCodigo(), numFila-1);
                                uoCodigoAux = datosInformeInterno.getUoCodigo();
                                //muestro la unidad en la primera celda
                                celda = fila.createCell(0);
                                celda.setCellValue(datosInformeInterno.getUoNombre());
                                celda.setCellStyle(estiloCellDatos);
                                //creo otra columna adicional para combinarlas después
                                celda = fila.createCell(1);
                                celda.setCellStyle(estiloCellDatos);
                                //unimos las dos últimas celdas creadas en la fila actual
                                hoja.addMergedRegion(new CellRangeAddress(numFila-1, numFila-1, 0, 1));
                                strFormulaTotalD.delete(0, strFormulaTotalD.length());
                                strFormulaTotalR.delete(0, strFormulaTotalR.length());
                                for (Iterator<Map.Entry<String, Integer>> it = mapColumnMesAnyo.entrySet().iterator(); it.hasNext();) {
                                    Map.Entry<String, Integer> entry = it.next();
                                    //inicializo a 0 las celdas de datos (derivadas y resueltas)
                                    celda = fila.createCell(entry.getValue());
                                    celda.setCellValue(0);
                                    celda.setCellStyle(estiloCellDatos);
                                    celda = fila.createCell(entry.getValue()+1);
                                    celda.setCellValue(0);
                                    celda.setCellStyle(estiloCellDatos);
                                    celda = fila.createCell((entry.getValue()+ 2));
                                    strFormula.delete(0, strFormula.length());
                                    strColumnaD.delete(0, strColumnaD.length());
                                    strColumnaR.delete(0, strColumnaR.length());
                                    strColumnaR.append(CellReference.convertNumToColString(entry.getValue()+1));
                                    strColumnaR.append(numFila);
                                    strFormula.append(strColumnaR);
                                    strFormula.append("*100/");
                                    strColumnaD.append(CellReference.convertNumToColString(entry.getValue()));
                                    strColumnaD.append(numFila);
                                    strFormula.append(strColumnaD);
                                    strFormulaTotalD.append(strColumnaD);
                                    strFormulaTotalD.append("+");
                                    strFormulaTotalR.append(strColumnaR);
                                    strFormulaTotalR.append("+");
                                    celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                                    celda.setCellFormula(strFormula.toString());
                                    celda.setCellStyle(estiloCellResueltas);
                                }
                                //totales
                                celda = fila.createCell(contCelda-3);
//                                celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                                celda.setCellFormula(strFormulaTotalD.substring(0, strFormulaTotalD.length()-1));//quito el último "+"
                                celda.setCellStyle(estiloCellTotal);
                                celda = fila.createCell(contCelda-2);
//                                celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                                celda.setCellFormula(strFormulaTotalR.substring(0, strFormulaTotalR.length()-1));//quito el último "+"
                                celda.setCellStyle(estiloCellTotal);
                                celda = fila.createCell(contCelda-1);
//                                celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                                celda.setCellFormula(getFormulaColumnaPorcentaje(contCelda-2, numFila));
                                celda.setCellStyle(estiloCellTotal);
                            }
                            //se sobrescriben los meses que tienen expedientes (si no tienen ya hemos puesto por defecto 0)
                            if(datosInformeInterno.getMes()!=null && datosInformeInterno.getAnyo()!=null){
                                //recupero el nºcolumna según mesAnyo
                                mesAnyo.delete(0, mesAnyo.length());
                                mesAnyo.append(MeLanbide57Utils.getNombreMesGregCalendar(Integer.parseInt(datosInformeInterno.getMes())-1));
                                mesAnyo.append(datosInformeInterno.getAnyo());
                                //celda Nºderivadas (sobrescribir valores que no son 0)
                                celda = fila.getCell(mapColumnMesAnyo.get(mesAnyo.toString()));
                                celda.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                                celda.setCellValue(Double.parseDouble(datosInformeInterno.getTotalExpedientes()));
                                celda.setCellStyle(estiloCellDatos);
                            }
                        }
                        //FILA TOTALES
                        fila = hoja.createRow(numFila++);
                        celda = fila.createCell(0);
                        celda.setCellValue("TOTAL");
                        celda.setCellStyle(estiloCellTotal);
                        //creo otra columna adicional para combinarlas después
                        celda = fila.createCell(1);
                        celda.setCellStyle(estiloCellTotal);
                        //unimos las dos últimas celdas creadas en la fila actual
                        hoja.addMergedRegion(new CellRangeAddress(numFila-1, numFila-1, 0, 1));
                        for (Iterator<Map.Entry<String, Integer>> it = mapColumnMesAnyo.entrySet().iterator(); it.hasNext();) {
                            Map.Entry<String, Integer> entry = it.next();
                            celda = fila.createCell(entry.getValue());
                            celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                            celda.setCellFormula(getFormulaColumnaTotalII(entry.getValue(), numFila-1));
                            celda.setCellStyle(estiloCellTotal);
                            
                            celda = fila.createCell(entry.getValue()+1);
                            celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                            celda.setCellFormula(getFormulaColumnaTotalII(entry.getValue()+1, numFila-1));
                            celda.setCellStyle(estiloCellTotal);

                            celda = fila.createCell(entry.getValue()+2);
                            celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                            celda.setCellFormula(getFormulaColumnaPorcentaje(entry.getValue()+1, numFila));
                            celda.setCellStyle(estiloCellTotal);
                        }
                        //celda total de totales
                        celda = fila.createCell(contCelda-3);
                        celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        celda.setCellFormula(getFormulaColumnaTotalII(contCelda-3, numFila-1));
                        celda.setCellStyle(estiloCellTotal);

                        celda = fila.createCell(contCelda-2);
                        celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        celda.setCellFormula(getFormulaColumnaTotalII(contCelda-2, numFila-1));
                        celda.setCellStyle(estiloCellTotal);

                        celda = fila.createCell(contCelda-1);
                        celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                        celda.setCellFormula(getFormulaColumnaPorcentaje(contCelda-2, numFila));
                        celda.setCellStyle(estiloCellTotal);
                        
                        
                        if(listaDatosInformeInternoResueltas!=null && !listaDatosInformeInternoResueltas.isEmpty()){
                            for (InformeInternoVO datosInformeInterno : listaDatosInformeInternoResueltas) {
                                //se sobrescriben los meses que tienen expedientes (si no tienen ya hemos puesto por defecto 0)
                                if(datosInformeInterno.getMes()!=null && datosInformeInterno.getAnyo()!=null){
                                    //recupero el nºcolumna según mesAnyo
                                    mesAnyo.delete(0, mesAnyo.length());
                                    mesAnyo.append(MeLanbide57Utils.getNombreMesGregCalendar(Integer.parseInt(datosInformeInterno.getMes())-1));
                                    mesAnyo.append(datosInformeInterno.getAnyo());
                                    //celda Nºderivadas (sobrescribir valores que no son 0)
                                    fila = hoja.getRow(mapRowUnidad.get(datosInformeInterno.getUoCodigo()));
                                    celda = fila.getCell(mapColumnMesAnyo.get(mesAnyo.toString())+1);
                                    celda.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                                    celda.setCellValue(Double.parseDouble(datosInformeInterno.getTotalExpedientes()));
                                    celda.setCellStyle(estiloCellDatos);
                                    
                                }
                            }
                        }
                    }
                }
                catch(Exception e)
                {
                    log.error("Error al recuperar tramitesAreas: " + e.getMessage());
                }
                finally
                {
                    try
                    {
                        adapt.devolverConexion(con);       
                    }
                    catch(Exception e)
                    {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }
            
            //AUTOAJUSTAR CONTENIDO
            //TODO: mejorar autosize porque deja textos en dos filas..
            for(int j = 0; j < contCelda; j++) {
                hoja.autoSizeColumn(j);
            }
            
            /*************
            *CREAR FICHERO
            *************/
            crearFichero(libro, response, "informeInterno");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el informe interno ", ex);
        }
        if(log.isDebugEnabled()) log.debug("descargarInformeInterno() : END");
    }

    private int creaCeldasMeses(XSSFSheet hoja, XSSFRow fila, XSSFCell celda, int contCelda, HashMap<String, Integer> mapColumnMesAnyo, Date mesHasta, GregorianCalendar gcal, int numColumnasMerge, int numFila) throws Exception{
        int numMes;
        int numAnyo;
        StringBuilder mesAnyo = new StringBuilder();
        while (!gcal.getTime().after(mesHasta)) {
            numMes = gcal.get(Calendar.MONTH);//cojo el mes y el año antes de añadir un mes al calendario
            numAnyo = gcal.get(Calendar.YEAR);//cojo el mes y el año antes de añadir un mes al calendario
            gcal.add(Calendar.MONTH, 1);
            celda = fila.createCell(contCelda++);
            mesAnyo.delete(0, mesAnyo.length());
            mesAnyo.append(MeLanbide57Utils.getNombreMesGregCalendar(numMes));
            mesAnyo.append(numAnyo);
            celda.setCellValue(mesAnyo.toString());
            celda.setCellStyle(estiloCellMeses);
            //creo columnas adicionales para combinarlas después
            for (int i=1;i<numColumnasMerge;i++){
                celda = fila.createCell(contCelda++);
                celda.setCellStyle(estiloCellMeses);
               
            }
            //unimos las dos últimas celdas creadas en la fila actual
            hoja.addMergedRegion(new CellRangeAddress(numFila-1, numFila-1, contCelda-numColumnasMerge, contCelda-1));
            //por cada mesAnyo que mostramos, guardamos el nºcolumna donde comienza dicho mesAnyo para poder asociar más tarde los datos del informe obtenidos a esa columna
            mapColumnMesAnyo.put(mesAnyo.toString(), contCelda-numColumnasMerge);//clave:mesAnyo, valor:nºcolumna donde comienza la celda de ese mesAnyo
        }

        return contCelda;
    }

    public void descargarInformeGeneral(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        if(log.isDebugEnabled()) log.debug("descargarInformeGeneral() : BEGIN");
        Date mesDesde;
        Date mesHasta;
        SimpleDateFormat sdfFechaMesAnyo = new SimpleDateFormat("MM/yyyy");
        String strFechaDesde;
        String strFechaHasta;
        int numFila = 0;
        final int numColumnasMerge = 2;
        try
        {
            strFechaDesde = (String)request.getParameter("meLanbide57FechaDesde");
            strFechaHasta = (String)request.getParameter("meLanbide57FechaHasta");
            //se supone que las fechas de inicio y fin son obligatorias
            mesDesde = sdfFechaMesAnyo.parse(strFechaDesde.substring(3));
            mesHasta = sdfFechaMesAnyo.parse(strFechaHasta.substring(3));
            
            XSSFWorkbook libro = new XSSFWorkbook();
            
            /*************************
            *ESTILOS COMUNES DE CELDAS
            *************************/
            crearEstilos(libro);
            
            XSSFSheet hoja = hoja = libro.createSheet("INFORME GENERAL");
            /*********
            *CABECERAS
            *********/
            //MESES (fila 1)
                int contCelda = 0;
                XSSFRow fila = hoja.createRow(numFila++);
                //celda en blanco
                XSSFCell celda = fila.createCell(contCelda++);
                celda.setCellStyle(estiloCellDatos);
                //celdas de meses
                GregorianCalendar gcal = new GregorianCalendar();
                gcal.setTime(mesDesde);
                HashMap<String, Integer> mapColumnMesAnyo = new HashMap<String, Integer>();
                
                contCelda = creaCeldasMeses(hoja, fila, celda, contCelda, mapColumnMesAnyo, mesHasta, gcal, numColumnasMerge, numFila);
                
                //celda total
                celda = fila.createCell(contCelda++);
                celda.setCellValue("TOTAL");
                celda.setCellStyle(estiloCellCabecera);
                //creo columna adicional para combinarlas después
                celda = fila.createCell(contCelda++);
                celda.setCellStyle(estiloCellCabecera);
                //unimos las dos últimas celdas creadas en la fila actual
                hoja.addMergedRegion(new CellRangeAddress(numFila-1, numFila-1, contCelda-numColumnasMerge, contCelda-1));
                
                
            /******
            *DATOS
            ******/
            //recupero los datos a mostrar
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (adapt != null) {
                Connection con = null;
                try
                {
                    //recupero literales de los campos del trámite que aparecerán en el excel
                    String codigoTramiteArchivo = ConfigurationParameter.getParameter("CODIGO_TRAMITE_ARCHIVO",this.getNombreModulo());
                    String codigoTramiteRevision = ConfigurationParameter.getParameter("CODIGO_TRAMITE_REVISION_GRABACION_DATOS",this.getNombreModulo());
                    String codigoCampoTipologia = ConfigurationParameter.getParameter("CODIGO_CAMPO_TIPOLOGIA",this.getNombreModulo());
                    String codigoCampoCanalRecep = ConfigurationParameter.getParameter("CODIGO_CAMPO_CANAL_RECEPCION",this.getNombreModulo());
                    String codigoCampoIdioma = ConfigurationParameter.getParameter("CODIGO_CAMPO_IDIOMA",this.getNombreModulo());
                    String codigoCampoGenero = ConfigurationParameter.getParameter("CODIGO_CAMPO_GENERO",this.getNombreModulo());
                    String codigoCampoTipoInterloc = ConfigurationParameter.getParameter("CODIGO_CAMPO_TIPO_INTERLOC",this.getNombreModulo());
                    String codigoCampoEstadoCierre = ConfigurationParameter.getParameter("CODIGO_CAMPO_ESTADO_CIERRE",this.getNombreModulo());
                    String codigoCampoUnidadCausante = ConfigurationParameter.getParameter("CODIGO_CAMPO_UNIDAD_CAUSANTE",this.getNombreModulo());

                    //TODO: el nombre de cada bloque se puede sacar de E_TML... mirar si merece la pena. De momento lo dejo con los literales que nos han pasado en el excel porque no coinciden exactamente con lo del E_TML
                //BLOQUE TIPOLOGÍA
                    con = adapt.getConnection();
                    List<InformeGeneralVO> listaDatosIGTipologia  = MeLanbide57Manager.getInstance().getListaDatosInformeGeneralDesplegable(codigoCampoTipologia, codOrganizacion/*, ocurrenciaTramite*/, strFechaDesde, strFechaHasta, codigoTramiteRevision, con, false);
                    
                    
                    //*************PRUEBAS DATOS PRO **************
                    /*listaDatosIGTipologia = new ArrayList<InformeGeneralVO>();
                    listaDatosIGTipologia.add(new InformeGeneralVO("AGRADECIMIENTO","3","2014","1"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("AGRADECIMIENTO","7","2014","2"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("AGRADECIMIENTO","6","2014","2"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("AGRADECIMIENTO","4","2014","1"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","9","2014","3"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","12","2014","2"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","6","2014","3"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","10","2014","9"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","5","2014","6"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","7","2014","8"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","4","2014","1"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","3","2014","3"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","8","2014","1"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("DESESTIMADA","11","2014","13"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","5","2014","27"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","12","2014","27"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","8","2014","20"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","7","2014","47"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","9","2014","106"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","11","2014","53"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","3","2014","47"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","10","2014","77"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","4","2014","33"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","2","2014","15"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("QUEJA","6","2014","31"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("SUGERENCIA","11","2014","1"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("SUGERENCIA","4","2014","1"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("SUGERENCIA","6","2014","6"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("SUGERENCIA","3","2014","1"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("SUGERENCIA","10","2014","4"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("SUGERENCIA","7","2014","4"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("SUGERENCIA","9","2014","2"));
                    listaDatosIGTipologia.add(new InformeGeneralVO("SUGERENCIA","5","2014","2")); */
                    //*************PRUEBAS PRO **************
                    
                    
                    numFila = crearBloqueIGCampo("TIPOLOGÍA", listaDatosIGTipologia, hoja, numFila, contCelda, mapColumnMesAnyo, fila);
                //BLOQUE CANAL RECEPCION
                    List<InformeGeneralVO> listaDatosIGCanalRecepcion  = MeLanbide57Manager.getInstance().getListaDatosInformeGeneralDesplegable(codigoCampoCanalRecep, codOrganizacion/*, ocurrenciaTramite*/, strFechaDesde, strFechaHasta, codigoTramiteRevision, con, false);
                    numFila = crearBloqueIGCampo("Q/S/A POR CANAL DE ENTRADA", listaDatosIGCanalRecepcion, hoja, numFila, contCelda, mapColumnMesAnyo, fila);
                //BLOQUE IDIOMA
                    List<InformeGeneralVO> listaDatosIGIdioma  = MeLanbide57Manager.getInstance().getListaDatosInformeGeneralDesplegable(codigoCampoIdioma, codOrganizacion/*, ocurrenciaTramite*/, strFechaDesde, strFechaHasta, codigoTramiteRevision, con, false);
                    numFila = crearBloqueIGCampo("Q/S/A POR IDIOMA", listaDatosIGIdioma, hoja, numFila, contCelda, mapColumnMesAnyo, fila);
                //BLOQUE GENERO
                    List<InformeGeneralVO> listaDatosIGGenero  = MeLanbide57Manager.getInstance().getListaDatosInformeGeneralDesplegable(codigoCampoGenero, codOrganizacion/*, ocurrenciaTramite*/, strFechaDesde, strFechaHasta, codigoTramiteRevision, con, false);
                    numFila = crearBloqueIGCampo("Q/S/A POR GÉNERO", listaDatosIGGenero, hoja, numFila, contCelda, mapColumnMesAnyo, fila);
                //BLOQUE INTERLOCUTOR
                    List<InformeGeneralVO> listaDatosIGInterloc  = MeLanbide57Manager.getInstance().getListaDatosInformeGeneralDesplegable(codigoCampoTipoInterloc, codOrganizacion/*, ocurrenciaTramite*/, strFechaDesde, strFechaHasta, codigoTramiteRevision, con, false);
                    numFila = crearBloqueIGCampo("Q/S/A POR INTERLOCUTOR", listaDatosIGInterloc, hoja, numFila, contCelda, mapColumnMesAnyo, fila);
                //BLOQUE RESPONDIDAS
                    List<InformeGeneralVO> listaDatosIGRespond  = MeLanbide57Manager.getInstance().getListaDatosInformeGeneralDesplegable(codigoCampoEstadoCierre, codOrganizacion/*, ocurrenciaTramite*/, strFechaDesde, strFechaHasta, codigoTramiteArchivo, con, true);
                    //*************SIMULACIÓN DATOS PRO **************
                    /*listaDatosIGRespond = new ArrayList<InformeGeneralVO>();
                    listaDatosIGRespond.add(new InformeGeneralVO("No","6","2016","1"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","7","2016","2"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","5","2016","1"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","2","2016","2"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","9","2016","1"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","1","2016","10"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","11","2016","3"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","12","2016","7"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","2","2016","5"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","10","2016","3"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","8","2016","1"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","3","2016","1"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","4","2016","1"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","3","2016","4"));
                    listaDatosIGRespond.add(new InformeGeneralVO("No","9","2016","6")); 
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","8","2016","21"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","10","2016","54"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","5","2016","43"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","11","2016","59"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","6","2016","31"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","7","2016","37"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","4","2016","41"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","12","2016","44"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","9","2016","107"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","3","2016","44"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","2","2016","49"));
                    listaDatosIGRespond.add(new InformeGeneralVO("Sí","1","2016","64"));*/
                    numFila = crearBloqueIGCampo("RESPONDIDAS", listaDatosIGRespond, hoja, numFila, contCelda, mapColumnMesAnyo, fila);
                //BLOQUE NIVEL DE RESPUESTA (relacionado con el anterior bloque, pero con el desglose)
                    List<InformeGeneralVO> listaDatosIGNivelResp  = MeLanbide57Manager.getInstance().getListaDatosInformeGeneralDesplegable(codigoCampoEstadoCierre, codOrganizacion/*, ocurrenciaTramite*/, strFechaDesde, strFechaHasta, codigoTramiteArchivo, con, false);
                    numFila = crearBloqueIGCampo("NIVEL DE RESPUESTA", listaDatosIGNivelResp, hoja, numFila, contCelda, mapColumnMesAnyo, fila);
                //BLOQUE UNIDAD CAUSANTE
                    List<InformeGeneralVO> listaDatosIGUnidadCausante  = MeLanbide57Manager.getInstance().getListaDatosInformeGeneralDesplegable(codigoCampoUnidadCausante, codOrganizacion/*, ocurrenciaTramite*/, strFechaDesde, strFechaHasta, codigoTramiteRevision, con, false);
                    numFila = crearBloqueIGCampo("UNIDAD CAUSANTE", listaDatosIGUnidadCausante, hoja, numFila, contCelda, mapColumnMesAnyo, fila);
                    
                }
                catch(Exception e)
                {
                    log.error("Error al recuperar: " + e.getMessage());
                }
                finally
                {
                    try
                    {
                        adapt.devolverConexion(con);       
                    }
                    catch(Exception e)
                    {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }
            
            //AUTOAJUSTAR CONTENIDO
            //TODO: mejorar autosize porque deja textos en dos filas..
            for(int j = 0; j < contCelda; j++) {
                hoja.autoSizeColumn(j);
            }
           

            /*************
            *CREAR FICHERO
            *************/
            crearFichero(libro, response, "informeGeneral");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el informe interno ", ex);
        }
        if(log.isDebugEnabled()) log.debug("descargarInformeInterno() : END");
    }
  /*private int row;
  private int col;
  private String sheetName;
  private boolean rowAbs;
  private boolean colAbs;
    
    private static String convertNumToColString(int col)
  {
    String retval = null;
    int mod = col % 26;
    int div = col / 26;
    char small = (char)(mod + 65);
    char big = (char)(div + 64);
    if (div == 0) {
      retval = "" + small;
    } else {
      retval = "" + big + "" + small;
    }
    return retval;
  }
  
  public String toString()
  {
    StringBuffer retval = new StringBuffer();
    retval.append(this.colAbs ? "$" : "");
    retval.append(convertNumToColString(this.col));
    retval.append(this.rowAbs ? "$" : "");
    retval.append(this.row + 1);
    
    return retval.toString();
  }*/
    
    
    
        private int crearBloqueIGCampo(String nombreBloque, List<InformeGeneralVO> listaDatosIG, XSSFSheet hoja, int numFila, int contCelda, HashMap<String, Integer> mapColumnMesAnyo, XSSFRow fila) throws Exception{
        if((listaDatosIG!=null && !listaDatosIG.isEmpty())){
            int contValoresCampos=0;
            //la lista de datosInformeGeneral está ordenada por descripción del valor del campo pero no por meses y años
            String descCampoAux = "";
            XSSFCell celda;
            StringBuilder strFormula = new StringBuilder();
            StringBuilder strFormulaTotal = new StringBuilder();
            StringBuilder strColumna = new StringBuilder();
            StringBuilder mesAnyo = new StringBuilder();
            XSSFRow filaAux;
            for (InformeGeneralVO datosInformeGeneral : listaDatosIG) {
                //la primera línea con las cabeceras del campo
                if("".equals(descCampoAux)){
                    fila = hoja.createRow(numFila++);
                    celda = fila.createCell(0);
                    celda.setCellValue(nombreBloque);
                    celda.setCellStyle(estiloCellCabecera);
                    for (Iterator<Map.Entry<String, Integer>> it = mapColumnMesAnyo.entrySet().iterator(); it.hasNext();) {
                        Map.Entry<String, Integer> entry = it.next();
                        celda = fila.createCell(entry.getValue());
                        celda.setCellValue("Nº");
                        celda.setCellStyle(estiloCellCabecera);
                        celda = fila.createCell(entry.getValue()+1);
                        celda.setCellValue("%");
                        celda.setCellStyle(estiloCellCabecera);
                    }
                    celda = fila.createCell(contCelda-2);
                    celda.setCellValue("Nº");
                    celda.setCellStyle(estiloCellCabecera);
                    celda = fila.createCell(contCelda-1);
                    celda.setCellValue("%");
                    celda.setCellStyle(estiloCellCabecera);
                }
                if(!descCampoAux.equals(datosInformeGeneral.getDescripcionCampo())){//por cada valor (distinto) del campo se crea una fila
                    fila = hoja.createRow(numFila++);
                    descCampoAux = datosInformeGeneral.getDescripcionCampo();
                    contValoresCampos++;
                    //muestro la descripción del valor del campo
                    celda = fila.createCell(0);
                    celda.setCellValue(datosInformeGeneral.getDescripcionCampo());
                    celda.setCellStyle(estiloCellDatos);
                    strFormulaTotal.delete(0, strFormulaTotal.length());
                    for (Iterator<Map.Entry<String, Integer>> it = mapColumnMesAnyo.entrySet().iterator(); it.hasNext();) {
                        Map.Entry<String, Integer> entry = it.next();
                        //inicializo a 0 las celdas de datos (por si luego no hay datos para que tengan valor)
                        celda = fila.createCell(entry.getValue());
                        celda.setCellValue(0);
                        celda.setCellStyle(estiloCellDatos);

                        //creo las celdas de las fórmulas vacías para añadirlas más tarde (aún no sé el nº total de distintos valores del campo)
                        celda = fila.createCell(entry.getValue()+1);
                        strColumna.delete(0, strColumna.length());
                        strColumna.append(CellReference.convertNumToColString(entry.getValue()));
                        strColumna.append(numFila);
                        strFormulaTotal.append(strColumna);
                        strFormulaTotal.append("+");
                    }
                    //totales
                    celda = fila.createCell(contCelda-2);
//                                celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                    celda.setCellFormula(strFormulaTotal.substring(0, strFormulaTotal.length()-1));//quito el último "+"
                    celda.setCellStyle(estiloCellDatos);
                    celda = fila.createCell(contCelda-1);
                    celda.setCellStyle(estiloCellDatos);
                }
                //se sobrescriben los meses que tienen expedientes (si no tienen ya hemos puesto por defecto 0)
                if(datosInformeGeneral.getMes()!=null && datosInformeGeneral.getAnyo()!=null){
                    //recupero el nºcolumna según mesAnyo
                    mesAnyo.delete(0, mesAnyo.length());
                    mesAnyo.append(MeLanbide57Utils.getNombreMesGregCalendar(Integer.parseInt(datosInformeGeneral.getMes())-1));
                    mesAnyo.append(datosInformeGeneral.getAnyo());
                    //celda (sobrescribir valores que no son 0)
                    celda = fila.getCell(mapColumnMesAnyo.get(mesAnyo.toString()));
                    celda.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                    celda.setCellValue(Double.parseDouble(datosInformeGeneral.getTotalExpedientes()));
                    celda.setCellStyle(estiloCellDatos);
                }
            }
            //FILA TOTALES
            fila = hoja.createRow(numFila++);
            celda = fila.createCell(0);
            celda.setCellValue("TOTAL");
            celda.setCellStyle(estiloCellTotal);
            for (Iterator<Map.Entry<String, Integer>> it = mapColumnMesAnyo.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, Integer> entry = it.next();
                celda = fila.createCell(entry.getValue());
                celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                celda.setCellFormula(getFormulaColumnaTotal(entry.getValue(), numFila-1, numFila-contValoresCampos));
                celda.setCellStyle(estiloCellDatos);

                celda = fila.createCell(entry.getValue()+1);
                celda.setCellStyle(estiloCellTotalBloq);

                //sobrescribo las celdas de % de los meses que antes he creado vacías
                for(int i=numFila-contValoresCampos; i<numFila;i++){
                    filaAux = hoja.getRow(i-1);
                    celda = filaAux.getCell(entry.getValue()+1);
                    strFormula.delete(0, strFormula.length());
                    strColumna.delete(0, strColumna.length());
                    strColumna.append(CellReference.convertNumToColString(entry.getValue()));
                    strColumna.append(i);
                    strFormula.append(strColumna);
                    strFormula.append("*100/$");
                    strFormula.append(CellReference.convertNumToColString(entry.getValue()));
                    strFormula.append("$");
                    strFormula.append(numFila);
                    celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                    celda.setCellFormula(strFormula.toString());
                    celda.setCellStyle(estiloCellDatos);
                }
            }
            //celda % de la columna TOTAL
            for(int i=numFila-contValoresCampos; i<numFila;i++){
                filaAux = hoja.getRow(i-1);
                celda = filaAux.getCell(contCelda-1);
                strFormula.delete(0, strFormula.length());
                strColumna.delete(0, strColumna.length());
                strColumna.append(CellReference.convertNumToColString(contCelda-2));
                strColumna.append(i);
                strFormula.append(strColumna);
                strFormula.append("*100/$");
                strFormula.append(CellReference.convertNumToColString(contCelda-2));
                strFormula.append("$");
                strFormula.append(numFila);
                celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
                celda.setCellFormula(strFormula.toString());
                celda.setCellStyle(estiloCellDatos);
            }
            //celda total de totales
            celda = fila.createCell(contCelda-2);
            celda.setCellType(XSSFCell.CELL_TYPE_FORMULA);
            celda.setCellFormula(getFormulaColumnaTotal(contCelda-2, numFila-1, numFila-contValoresCampos));
            celda.setCellStyle(estiloCellTotal);

            celda = fila.createCell(contCelda-1);
            celda.setCellStyle(estiloCellTotalBloq);
            
        }

        return numFila;
    }

    private void crearFichero(XSSFWorkbook libro, HttpServletResponse response, String nombreFichero) throws Exception{
        ByteArrayOutputStream excelOutput = new ByteArrayOutputStream();
        libro.write(excelOutput);
        response.setContentType("application/vnd.ms-excel");
        StringBuilder ficheroAttachment = new StringBuilder("attachment; filename=");
        ficheroAttachment.append(nombreFichero);
        ficheroAttachment.append(".xls");
        response.setHeader("Content-Disposition", ficheroAttachment.toString());
        response.setHeader("Content-Transfer-Encoding", "binary");  
        response.setContentLength(excelOutput.size());
        response.getOutputStream().write(excelOutput.toByteArray(), 0, excelOutput.size());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    private String getFormulaColumnaPorcentaje(int numColumn, int numFila) throws Exception{
        StringBuilder formula = new StringBuilder();
        formula.append(CellReference.convertNumToColString(numColumn));
        formula.append(numFila);//primera fila de datos
        formula.append("*100/");
        formula.append(CellReference.convertNumToColString(numColumn-1));
        formula.append(numFila);
        return formula.toString();
    }

    private String getFormulaColumnaTotalII(int numColumn, int numFila) throws Exception{
        return getFormulaColumnaTotal(numColumn, numFila, 3);//primera fila de datos del informe interno
    }

    private String getFormulaColumnaTotal(int numColumn, int ultimaFila, int primeraFila) throws Exception{
        StringBuilder formula = new StringBuilder();
        formula.append("SUM(");
        formula.append(CellReference.convertNumToColString(numColumn));
        formula.append(primeraFila);
        formula.append(":");
        formula.append(CellReference.convertNumToColString(numColumn));
        formula.append(ultimaFila);
        formula.append(")");
        return formula.toString();
    }

    /**
     * muestra los expedientes iniciados o cualquier trámite en alguna de las unidades tramitadoras del usuario conectado
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void descargarInformeRGI(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
//        //codOrganizacion=1;
        if(log.isDebugEnabled()) log.debug("descargarInformeRGI() : BEGIN");
        String strFechaDesde;
        String strFechaHasta;
        int numFila = 0;
//        final int numColumnasMerge = 3;
        try
        {
            strFechaDesde = (String)request.getParameter("meLanbide57FechaDesde");
            strFechaHasta = (String)request.getParameter("meLanbide57FechaHasta");
            
            XSSFWorkbook libro = new XSSFWorkbook();
            /*************************
            *ESTILOS COMUNES DE CELDAS
            *************************/
            crearEstilos(libro);
            XSSFSheet hoja = hoja = libro.createSheet("INFORME RGI");
            
            /*********
            *CABECERAS
            *********/
                int contCelda = 0;
                XSSFRow fila = hoja.createRow(numFila++);
                XSSFCell celda = fila.createCell(contCelda++);
                celda.setCellValue("NºEXPEDIENTE");
                celda.setCellStyle(estiloCellCabecera);
               
                celda = fila.createCell(contCelda++);
                celda.setCellValue("PREOCEDIMIENTO");
                celda.setCellStyle(estiloCellCabecera);
               
                celda = fila.createCell(contCelda++);
                celda.setCellValue("INTERESADO NIF");
                celda.setCellStyle(estiloCellCabecera);
               
                celda = fila.createCell(contCelda++);
                celda.setCellValue("INTERESADO AP1");
                celda.setCellStyle(estiloCellCabecera);
               
                celda = fila.createCell(contCelda++);
                celda.setCellValue("INTERESADO AP2");
                celda.setCellStyle(estiloCellCabecera);
               
                celda = fila.createCell(contCelda++);
                celda.setCellValue("INTERESADO NOMBRE");
                celda.setCellStyle(estiloCellCabecera);
               
                celda = fila.createCell(contCelda++);
                celda.setCellValue("ASUNTO");
                celda.setCellStyle(estiloCellCabecera);
               
                celda = fila.createCell(contCelda++);
                celda.setCellValue("FECHA INICIO");
                celda.setCellStyle(estiloCellCabecera);
               
                celda = fila.createCell(contCelda++);
                celda.setCellValue("FECHA FIN");
                celda.setCellStyle(estiloCellCabecera);
               
                celda = fila.createCell(contCelda++);
                celda.setCellValue("USUARIO DE INICIO");
                celda.setCellStyle(estiloCellCabecera);
                
                
            /******
            *DATOS
            ******/
            //recupero los datos a mostrar
            AdaptadorSQLBD adapt = null;
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (adapt != null) {
                Connection con = null;
                try
                {
                    con = adapt.getConnection();
                    SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMelanbide57.FORMATO_FECHA);
                    UsuarioValueObject usuario = (UsuarioValueObject)request.getSession().getAttribute("usuario");
                    List<InformeRGIVO> listaDatosInformeRGI  = MeLanbide57Manager.getInstance().getListaDatosInformeRGI(codOrganizacion, strFechaDesde, strFechaHasta, usuario.getIdUsuario(), con);

                    if((listaDatosInformeRGI!=null && !listaDatosInformeRGI.isEmpty())){
                        for (InformeRGIVO datosInforme : listaDatosInformeRGI) {
                            contCelda = 0;
                            fila = hoja.createRow(numFila++);
                            celda = fila.createCell(0);
                            celda.setCellValue(datosInforme.getNumExpediente());
                            celda = fila.createCell(1);
                            celda.setCellValue(datosInforme.getProcedimiento());
                            celda = fila.createCell(2);
                            celda.setCellValue(datosInforme.getInteresadoNif());
                            celda = fila.createCell(3);
                            celda.setCellValue(datosInforme.getInteresadoAp1());
                            celda = fila.createCell(4);
                            celda.setCellValue(datosInforme.getInteresadoAp2());
                            celda = fila.createCell(5);
                            celda.setCellValue(datosInforme.getInteresadoNombre());
                            celda = fila.createCell(6);
                            celda.setCellValue(datosInforme.getAsunto());
                            celda = fila.createCell(7);
                            if(datosInforme.getFechaInicio()!=null){
                                celda.setCellValue(formatoFecha.format(datosInforme.getFechaInicio()));
                            }
                            celda = fila.createCell(8);
                            if(datosInforme.getFechaFin()!=null){
                                celda.setCellValue(formatoFecha.format(datosInforme.getFechaFin()));
                            }
                            celda = fila.createCell(9);
                            celda.setCellValue(datosInforme.getUsuInicio());
                        }
                    }
                }
                catch(Exception e)
                {
                    log.error("Error al recuperar informeRGI: " + e.getMessage());
                }
                finally
                {
                    try
                    {
                        adapt.devolverConexion(con);       
                    }
                    catch(Exception e)
                    {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }
            
//            //AUTOAJUSTAR CONTENIDO
//            //TODO: mejorar autosize porque deja textos en dos filas..
            
            /*************
            *CREAR FICHERO
            *************/
            crearFichero(libro, response, "informeRGI");
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el informe RGI ", ex);
        }
        if(log.isDebugEnabled()) log.debug("descargarInformeRGI() : END");
    }

    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
        if(log.isDebugEnabled()) log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor)){
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
            }catch(TechnicalException te){
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(conGenerico!=null && !conGenerico.isClosed())
                conGenerico.close();
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
}//class