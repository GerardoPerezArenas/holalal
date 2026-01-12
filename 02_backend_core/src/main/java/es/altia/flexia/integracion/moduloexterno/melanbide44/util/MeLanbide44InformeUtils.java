/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44.util;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaInformeDesglose;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaResumenInformeProyectos;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author santiagoc
 */
public class MeLanbide44InformeUtils {
    private static MeLanbide44InformeUtils instance = null;
    
    private MeLanbide44InformeUtils()
    {
        
    }
    
    public static MeLanbide44InformeUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide44InformeUtils.class)
            {
                instance = new MeLanbide44InformeUtils();
            }
        }
        return instance;
    }
    
    public List<FilaInformeDesglose> extraerDatosInformeDesglose(ResultSet rs) throws Exception
    {
        List<FilaInformeDesglose> retList = new ArrayList<FilaInformeDesglose>();
        HashMap<Integer, FilaInformeDesglose> mapaDatos = new HashMap<Integer, FilaInformeDesglose>();
        int codSolicitud = 0;
        String empresa = null;
        int tipoDis;
        int gravedadDis;
        int tipoContrato;
        int sexo;
        FilaInformeDesglose filaAct = null;
        while(rs.next())
        {
            filaAct = new FilaInformeDesglose();
            filaAct.setNumExp(rs.getString("ECA_NUMEXP"));
            filaAct.setEmpresa(rs.getString("ECA_SEG_EMPRESA"));
            filaAct.sethInd_psi_65(rs.getInt("H_IND_PSI_65"));
            if(rs.wasNull())
            {
                filaAct.sethInd_psi_65(null);
            }
            filaAct.setmInd_psi_65(rs.getInt("M_IND_PSI_65"));
            if(rs.wasNull())
            {
                filaAct.setmInd_psi_65(null);
            }
            filaAct.sethTemp_psi_65(rs.getInt("H_TEMP_PSI_65"));
            if(rs.wasNull())
            {
                filaAct.sethTemp_psi_65(null);
            }
            filaAct.setmTemp_psi_65(rs.getInt("M_TEMP_PSI_65"));
            if(rs.wasNull())
            {
                filaAct.setmTemp_psi_65(null);
            }
            filaAct.sethInd_psi_33_65(rs.getInt("H_IND_PSI_33_65"));
            if(rs.wasNull())
            {
                filaAct.sethInd_psi_33_65(null);
            }
            filaAct.setmInd_psi_33_65(rs.getInt("M_IND_PSI_33_65"));
            if(rs.wasNull())
            {
                filaAct.setmInd_psi_33_65(null);
            }
            filaAct.sethTemp_psi_33_65(rs.getInt("H_TEMP_PSI_33_65"));
            if(rs.wasNull())
            {
                filaAct.sethTemp_psi_33_65(null);
            }
            filaAct.setmTemp_psi_33_65(rs.getInt("M_TEMP_PSI_33_65"));
            if(rs.wasNull())
            {
                filaAct.setmTemp_psi_33_65(null);
            }
            filaAct.sethInd_fis_65(rs.getInt("H_IND_FIS_65"));
            if(rs.wasNull())
            {
                filaAct.sethInd_fis_65(null);
            }
            filaAct.setmInd_fis_65(rs.getInt("M_IND_FIS_65"));
            if(rs.wasNull())
            {
                filaAct.setmInd_fis_65(null);
            }
            filaAct.sethTemp_fis_65(rs.getInt("H_TEMP_FIS_65"));
            if(rs.wasNull())
            {
                filaAct.sethTemp_fis_65(null);
            }
            filaAct.setmTemp_fis_65(rs.getInt("M_TEMP_FIS_65"));
            if(rs.wasNull())
            {
                filaAct.setmTemp_fis_65(null);
            }
            filaAct.sethInd_sens_mas_33(rs.getInt("H_IND_SENS_MAS_33"));
            if(rs.wasNull())
            {
                filaAct.sethInd_sens_mas_33(null);
            }
            filaAct.setmInd_sens_mas_33(rs.getInt("M_IND_SENS_MAS_33"));
            if(rs.wasNull())
            {
                filaAct.setmInd_sens_mas_33(null);
            }
            filaAct.sethTemp_sens_mas_33(rs.getInt("H_TEMP_SENS_MAS_33"));
            if(rs.wasNull())
            {
                filaAct.sethTemp_sens_mas_33(null);
            }
            filaAct.setmTemp_sens_mas_33(rs.getInt("M_TEMP_SENS_MAS_33"));
            if(rs.wasNull())
            {
                filaAct.setmTemp_sens_mas_33(null);
            }
            
            retList.add(filaAct);
        }
        Iterator<Integer> it = mapaDatos.keySet().iterator();
        while(it.hasNext())
        {
            retList.add(mapaDatos.get(it.next()));
        }
        return retList;
    }
    
    /*public List<FilaInformeProyectos> extraerDatosInformeProyectos(ResultSet rs) throws Exception
    {
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        HashMap<Integer, FilaInformeProyectos> mapaDatos = new HashMap<Integer, FilaInformeProyectos>();
        int codSolicitud = 0;
        int sexo;
        Date fecNacimiento;
        FilaInformeProyectos filaAct = null;
        while(rs.next())
        {
            codSolicitud = rs.getInt("ECA_SOLICITUD_COD");
            if(!rs.wasNull())
            {
                filaAct = mapaDatos.get(codSolicitud);
                if(filaAct == null)
                {
                    filaAct = new FilaInformeProyectos();
                    mapaDatos.put(codSolicitud, filaAct);
                }

                sexo = rs.getInt("ECA_SEG_SEXO");
                if(rs.wasNull())
                {
                    sexo = -1;
                }
                
                fecNacimiento = rs.getDate("ECA_SEG_FECNACIMIENTO");

                filaAct.anadir(sexo, fecNacimiento);
            }
        }
        Iterator<Integer> it = mapaDatos.keySet().iterator();
        while(it.hasNext())
        {
            retList.add(mapaDatos.get(it.next()));
        }
        return retList;
    }*/
    
    public List<FilaResumenInformeProyectos> extraerDatosResumenInformeProyectos(int parte, ResultSet rs) throws Exception
    {
        List<FilaResumenInformeProyectos> retList = new ArrayList<FilaResumenInformeProyectos>();
        switch(parte)
        {
            case 0:
                retList = extraerDatosResumenInformeProyectos0(rs);
                break;
            case 4:
                retList = extraerDatosResumenInformeProyectos4(rs);
                break;
        }
        return retList;
    }
    
    private List<FilaResumenInformeProyectos> extraerDatosResumenInformeProyectos0(ResultSet rs) throws Exception
    {
        List<FilaResumenInformeProyectos> retList = new ArrayList<FilaResumenInformeProyectos>();
        FilaResumenInformeProyectos fila = null;
        while(rs.next())
        {
            fila = new FilaResumenInformeProyectos();
            fila.setNumExp(rs.getString("ECA_NUMEXP") != null ? rs.getString("ECA_NUMEXP") : "");
            fila.setImporteSubvencion(rs.getDouble("IMPORTE_SUBVENCION"));
            if(rs.wasNull())
            {
                fila.setImporteSubvencion(0.0);
            }
            retList.add(fila);
        }
        return retList;
    }
    
    private List<FilaResumenInformeProyectos> extraerDatosResumenInformeProyectos4(ResultSet rs) throws Exception
    {
        List<FilaResumenInformeProyectos> retList = new ArrayList<FilaResumenInformeProyectos>();
        FilaResumenInformeProyectos fila = null;
        while(rs.next())
        {
            fila = new FilaResumenInformeProyectos();
            fila.setTotalPrep(rs.getInt("TOT_PREP"));
            if(rs.wasNull())
            {
                fila.setTotalPrep(0);
            }
            fila.setPrepIndef(rs.getInt("INDEF"));
            if(rs.wasNull())
            {
                fila.setPrepIndef(0);
            }
            fila.setPrepTempo(rs.getInt("TEMPO"));
            if(rs.wasNull())
            {
                fila.setPrepTempo(0);
            }
            retList.add(fila);
        }
        return retList;
    }
    
    public List<FilaInformeProyectos> extraerDatosInformeProyectos(int parte, ResultSet rs) throws Exception
    {
        List<FilaInformeProyectos> retList = null;
        switch(parte)
        {
            case 2:
                retList = extraerDatosInformeProyectosParte2(rs);
                break;
            case 3:
                retList = extraerDatosInformeProyectosParte3(rs);
        }
        return retList;
    }
    
    private List<FilaInformeProyectos> extraerDatosInformeProyectosParte2(ResultSet rs) throws Exception
    {
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        FilaInformeProyectos fila = null;
        while(rs.next())
        {
            fila = new FilaInformeProyectos();
            fila.setEmpresa(rs.getString("ECA_SEG_EMPRESA") != null ? rs.getString("ECA_SEG_EMPRESA").toUpperCase() : "");
            fila.setInsSeg(rs.getString("INS_SEG") != null ? rs.getString("INS_SEG").toUpperCase() : "");
            fila.setTotalTrabajadores(rs.getInt("TOTAL"));
            if(rs.wasNull())
            {
                fila.setTotalTrabajadores(0);
            }
            fila.setH25(rs.getInt("H_MENOR_25"));
            if(rs.wasNull())
            {
                fila.setH25(0);
            }
            fila.setH25_54(rs.getInt("H_ENTRE_25_55"));
            if(rs.wasNull())
            {
                fila.setH25_54(0);
            }
            fila.setH55(rs.getInt("H_MAYOR_55"));
            if(rs.wasNull())
            {
                fila.setH55(0);
            }
            fila.setM25(rs.getInt("M_MENOR_25"));
            if(rs.wasNull())
            {
                fila.setM25(0);
            }
            fila.setM25_54(rs.getInt("M_ENTRE_25_55"));
            if(rs.wasNull())
            {
                fila.setM25_54(0);
            }
            fila.setM55(rs.getInt("M_MAYOR_55"));
            if(rs.wasNull())
            {
                fila.setM55(0);
            }
            retList.add(fila);
        }
        return retList;
    }
    
    private List<FilaInformeProyectos> extraerDatosInformeProyectosParte3(ResultSet rs) throws Exception
    {
        List<FilaInformeProyectos> retList = new ArrayList<FilaInformeProyectos>();
        FilaInformeProyectos fila = null;
        while(rs.next())
        {
            fila = new FilaInformeProyectos();
            fila.setEmpresa(rs.getString("ECA_SEG_EMPRESA"));
            fila.setTotalPreparadores(rs.getInt("TOT_PREP"));
            if(rs.wasNull())
            {
                fila.setTotalPreparadores(0);
            }
            fila.setPrepIndefinido(rs.getInt("INDEF"));
            if(rs.wasNull())
            {
                fila.setPrepIndefinido(0);
            }
            fila.setPrepTemporal(rs.getInt("TEMPO"));
            if(rs.wasNull())
            {
                fila.setPrepTemporal(0);
            }
            retList.add(fila);
        }
        return retList;
    }
    
    public void generarInformeDesglosePDF(List<FilaInformeDesglose> filas, String ano, String formato, int idioma, HttpServletResponse response)
    {
        try
        {
            MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
            
            
            Map<String, Object> reportParams = new HashMap<String, Object>();
            String urlLogoLanbide="/es/altia/flexia/integracion/moduloexterno/melanbide44/reports/imagenes/Lanbide_logo.jpg";
            reportParams.put("logo", getClass().getClassLoader().getResourceAsStream(urlLogoLanbide));
            String urlLogoGV="/es/altia/flexia/integracion/moduloexterno/melanbide44/reports/imagenes/GV_logo.jpg";
            reportParams.put("logo2", getClass().getClassLoader().getResourceAsStream(urlLogoGV));
            reportParams.put("ano", ano);
            
            //Traducciones
            reportParams.put("titulo", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.titulo").toUpperCase());
            reportParams.put("subtitulo", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.subTitulo").toUpperCase());
            reportParams.put("cAutonoma", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.cAutonoma").toUpperCase());
            reportParams.put("empresa", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.empresa").toUpperCase());
            reportParams.put("psi65", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.psiquicos_65").toUpperCase());
            reportParams.put("psi33", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.psiquicos_33_65").toUpperCase());
            reportParams.put("fisicos65", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.fisicos_sensoriales_65").toUpperCase());
            reportParams.put("sordas", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.sordas_33").toUpperCase());
            reportParams.put("fisicos", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.fisicos").toUpperCase());
            reportParams.put("sensoriales", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.sensoriales").toUpperCase());
            reportParams.put("indefinido", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.indefinido").toUpperCase());
            reportParams.put("temporal", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.temporal").toUpperCase());
            reportParams.put("hombre", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.hombre").toUpperCase());
            reportParams.put("mujer", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.mujer").toUpperCase());
            reportParams.put("pagina", meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.pagina"));
            
            String urlMasterReport = "/es/altia/flexia/integracion/moduloexterno/melanbide44/reports/InformeDesglose.jasper";
            MeLanbide44InformeUtils.getInstance().exportarPDFoWord(urlMasterReport, reportParams, filas, formato, "informeDesgloseTrabDiscApoyo", response);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void generarInformeProyectosPDF(List<FilaResumenInformeProyectos> filasResumen, HashMap<String, List<FilaInformeProyectos>> filasPorExpediente, String ano, String formato, int idioma, HttpServletResponse response)
    {
        try
        {
            
            MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
            
            
            Map<String, Object> reportParams = new HashMap<String, Object>();
            String urlLogoLanbide="/es/altia/flexia/integracion/moduloexterno/melanbide44/reports/imagenes/Lanbide_logo.jpg";
            reportParams.put("logo", getClass().getClassLoader().getResourceAsStream(urlLogoLanbide));
            String urlLogoGV="/es/altia/flexia/integracion/moduloexterno/melanbide44/reports/imagenes/GV_logo.jpg";
            reportParams.put("logo2", getClass().getClassLoader().getResourceAsStream(urlLogoGV));
            reportParams.put("ano", ano);
            
            //Traducciones
            reportParams.put("titulo", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.titulo").toUpperCase());
            reportParams.put("subtitulo", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.subTitulo").toUpperCase());
            reportParams.put("cAutonoma", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.cAutonoma").toUpperCase());
            reportParams.put("entidad", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.entidad").toUpperCase());
            reportParams.put("empresa", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.empresa").toUpperCase());
            reportParams.put("actividad", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.actividad").toUpperCase());
            reportParams.put("fecAprob", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.fecAprobacion").toUpperCase());
            reportParams.put("importe", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.impSubvencion").toUpperCase());
            reportParams.put("insercion", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.insSeg").toUpperCase());
            reportParams.put("trabApoyados", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.trabApoyados").toUpperCase());
            reportParams.put("preparadores", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.prepLaborales").toUpperCase());
            reportParams.put("total", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.total").toUpperCase());
            reportParams.put("tipoCont", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.tipoContrato").toUpperCase());
            reportParams.put("25Anos", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.anos25").toUpperCase());
            reportParams.put("25_54Anos", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.anos25_54").toUpperCase());
            reportParams.put("55Anos", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.anos55").toUpperCase());
            reportParams.put("hombre", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.hombre").toUpperCase());
            reportParams.put("mujer", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.mujer").toUpperCase());
            reportParams.put("pagina", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.pagina"));
            reportParams.put("indefinido", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.indefinido"));
            reportParams.put("temporal", meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.temporal"));
            
            String urlMasterReport = "/es/altia/flexia/integracion/moduloexterno/melanbide44/reports/InformeProyectos.jasper";
            MeLanbide44InformeUtils.getInstance().exportarPDFoWord(urlMasterReport, reportParams, filasResumen, formato, "informeProyectosEmpleoConApoyo", response);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void exportarPDFoWord(String reportUrl, Map<String, Object> reportParams, List filas, String formato, String nombreInforme, HttpServletResponse response) throws Exception
    {
        if(formato != null)
        {
            ResourceBundle m_Conf = ResourceBundle.getBundle("common");
            JasperReport masterReport = (JasperReport) JRLoader.loadObject(this.getClass().getResourceAsStream(reportUrl));
            JasperPrint print = null;
            if(filas != null && !filas.isEmpty())
            {
                print = JasperFillManager.fillReport(masterReport, reportParams, new JRBeanCollectionDataSource(filas));
            }
            else
            {
                print = JasperFillManager.fillReport(masterReport, reportParams, new JREmptyDataSource(1));
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            String extension = "";
            if(formato.equalsIgnoreCase("pdf"))
            {
                JRPdfExporter pdfExporter = new JRPdfExporter();
                //JRDocxExporter pdfExporter = new JRDocxExporter();
                pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                pdfExporter.exportReport();
                extension = ".pdf";
            }
            else if(formato.equalsIgnoreCase("word"))
            {
                //JRPdfExporter pdfExporter = new JRPdfExporter();
                JRDocxExporter pdfExporter = new JRDocxExporter();
                pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, os);
                pdfExporter.exportReport();
                extension = ".doc";
            }

            File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
            if(!directorioTemp.exists())
            {
                directorioTemp.mkdirs();
            }
            File informe = File.createTempFile(nombreInforme, extension, directorioTemp);


            FileOutputStream fileOutput = new FileOutputStream(informe);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            bufferedOutput.write(os.toByteArray(), 0, os.toByteArray().length);
            bufferedOutput.flush();
            bufferedOutput.close();
            fileOutput.flush();
            fileOutput.close();


            String rutaArchivoSalida = informe.getAbsolutePath();
            FileInputStream istr = new FileInputStream(rutaArchivoSalida);
            BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

            int size = (int) informe.length(); 
            byte[] data = new byte[size]; 
            bstr.read( data, 0, size ); 
            bstr.close();

        //            response.setContentType("application/pdf");
        //            response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
        //            response.setHeader("Content-Transfer-Encoding", "binary");  

            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma","public");
            response.setHeader ("Content-Type", "application/octet-stream");
            response.setHeader ("Content-Disposition", "attachment; filename="+informe.getName());

            response.setContentLength(data.length);
            response.getOutputStream().write(data, 0, data.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }
    
    public void generarInformeDesgloseExcel(List<FilaInformeDesglose> filas, String ano, int idioma, HttpServletResponse response)
    {
        try
        {
            MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
            
            HSSFWorkbook libro = new HSSFWorkbook();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    palette.setColorAtIndex(HSSFColor.ROYAL_BLUE.index, (byte)75, (byte)149, (byte)211);
                }
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            
            
            HSSFFont negritaTitulo = libro.createFont();
            negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            negritaTitulo.setFontHeight((short)200);
            HSSFFont negritaCabecera = libro.createFont();
            negritaCabecera.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            negritaCabecera.setFontHeight((short)180);
            negritaCabecera.setColor(HSSFColor.WHITE.index);
            HSSFFont normal = libro.createFont();
            normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            normal.setFontHeight((short)180);
            HSSFSheet hoja = null;
            int numFila = 0;
            
            HSSFRow fila = null;
            HSSFCell celda = null;
            HSSFCellStyle estiloCelda = null;
            
            hoja = libro.createSheet();
            
            hoja.setColumnWidth(0, 8000);
            hoja.setColumnWidth(1, 10000);
            hoja.setColumnWidth(2, 10000);
            hoja.setColumnWidth(3, 1700);
            hoja.setColumnWidth(4, 1700);
            hoja.setColumnWidth(5, 1700);
            hoja.setColumnWidth(6, 1700);
            hoja.setColumnWidth(7, 1700);
            hoja.setColumnWidth(8, 1700);
            hoja.setColumnWidth(9, 1700);
            hoja.setColumnWidth(10, 1700);
            hoja.setColumnWidth(11, 1700);
            hoja.setColumnWidth(12, 1700);
            hoja.setColumnWidth(13, 1700);
            hoja.setColumnWidth(14, 1700);
            hoja.setColumnWidth(15, 1700);
            hoja.setColumnWidth(16, 1700);
            hoja.setColumnWidth(17, 1700);
            hoja.setColumnWidth(18, 1700);
            
            //------------------------------------------- FILA 1 -------------------------------------------------
            
            fila = hoja.createRow(numFila);
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 20));

            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.titulo").toUpperCase()+" "+ano);
            celda.setCellStyle(estiloCelda);
            
            //------------------------------------------- FILA 3 -------------------------------------------------
            
            numFila += 2;
            fila = hoja.createRow(numFila);
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 20));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.subTitulo").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            //------------------------------------------- FILA 5 -------------------------------------------------
            
            numFila += 2;
            fila = hoja.createRow(numFila);
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 20));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.cAutonoma").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            //------------------------------------------- FILA 7 -------------------------------------------------
            
            numFila += 2;
            fila = hoja.createRow(numFila);
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 0, 0));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.provincia").toUpperCase());
            celda.setCellStyle(estiloCelda);     

            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 1, 1));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(1);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.entidad").toUpperCase());
            celda.setCellStyle(estiloCelda);      

            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 2, 2));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(2);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.empresa").toUpperCase());
            celda.setCellStyle(estiloCelda);         
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+1, 3, 6));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(3);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.psiquicos_65").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(4);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(5);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(6);
            celda.setCellStyle(estiloCelda);  
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+1, 7, 10));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(7);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.psiquicos_33_65").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(8);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(9);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(10);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 11, 14));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(11);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.fisicos_65").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(12);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(13);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(14);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+1, 15, 18));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(15);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.sensoriales_33").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(16);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(17);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(18);
            celda.setCellStyle(estiloCelda);
            
            //------------------------------------------- FILA 8 -------------------------------------------------
            
            numFila++;
            fila = hoja.createRow(numFila);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(2);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 11, 14));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(11);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.fisicos").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(12);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(13);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(14);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 15, 18));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(15);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.sensoriales").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(16);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(17);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(18);
            celda.setCellStyle(estiloCelda);
            
            //------------------------------------------- FILA 9 -------------------------------------------------
            
            numFila++;
            fila = hoja.createRow(numFila);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(2);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 3, 4));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(3);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.indefinido").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(4);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 5, 6));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(5);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.temporal").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(6);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 7, 8));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(7);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.indefinido").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(8);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 9, 10));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(9);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.temporal").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(10);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 11, 12));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(11);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.indefinido").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(12);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 13, 14));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(13);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.temporal").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(14);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 15, 16));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(15);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.indefinido").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(16);
            celda.setCellStyle(estiloCelda);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 17, 18));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(17);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.temporal").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(18);
            celda.setCellStyle(estiloCelda);
            
            //------------------------------------------- FILA 11 -------------------------------------------------
            
            numFila++;
            fila = hoja.createRow(numFila);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            celda = fila.createCell(2);
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(3);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.hombre").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(4);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.mujer").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(5);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.hombre").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(6);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.mujer").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(7);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.hombre").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(8);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.mujer").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(9);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.hombre").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(10);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.mujer").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(11);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.hombre").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(12);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.mujer").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(13);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.hombre").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(14);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.mujer").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(15);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.hombre").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(16);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.mujer").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(17);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.hombre").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(18);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeDesglose.mujer").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            //------------------------------------------- FILAS -------------------------------------------------
            
            String provinciaAct = null;
            String entidadAct = null;
            int p = 0;
            int e = 0;
            boolean cambioProvincia = false;
            boolean cambioEntidad = false;
            
            FilaInformeDesglose filaAct = null;
            
            for(int i = 0; i < filas.size(); i++)
            {
                
                filaAct = filas.get(i);
                
                if(provinciaAct == null)
                {
                    cambioProvincia = true;
                    provinciaAct = filaAct.getCodProvincia();
                    int filasProvincia = contarFilasProvincia(provinciaAct, filas);
                    hoja.addMergedRegion(new CellRangeAddress(numFila+1, numFila+filasProvincia, 0, 0));
                }
                else
                {
                    if(filaAct.getCodProvincia() != null && !filaAct.getCodProvincia().equalsIgnoreCase(provinciaAct))
                    {
                        //Si hay un cambio de provincia, añado un merged region en la columna PROVINCIA
                        cambioProvincia = true;
                        provinciaAct = filaAct.getCodProvincia();
                        int filasProvincia = contarFilasProvincia(provinciaAct, filas);
                        hoja.addMergedRegion(new CellRangeAddress(numFila+1, numFila+filasProvincia, 0, 0));
                        p = 0;
                    }
                    else
                    {
                        cambioProvincia = false;
                    }
                }
                
                if(entidadAct == null)
                {
                    cambioEntidad = true;
                    entidadAct = filaAct.getEntidadPromotora();
                        int filasEntidad = this.contarFilasEntidad(entidadAct, filas);
                        hoja.addMergedRegion(new CellRangeAddress(numFila+1, numFila+filasEntidad, 1, 1));
                }
                else
                {
                    if(filaAct.getEntidadPromotora() != null && !filaAct.getEntidadPromotora().equalsIgnoreCase(entidadAct))
                    {
                        //Si hay un cambio de entidad dentro de la misma provincia, hay que añadir un merged region
                        cambioEntidad = true;
                        entidadAct = filaAct.getEntidadPromotora();
                        int filasEntidad = this.contarFilasEntidad(entidadAct, filas);
                        hoja.addMergedRegion(new CellRangeAddress(numFila+1, numFila+filasEntidad, 1, 1));
                        e = 0;
                    }
                    else
                    {
                        cambioEntidad = false;
                    }
                }
                numFila++;
                fila = hoja.createRow(numFila);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                celda = fila.createCell(0);
                /*if(cambioProvincia)
                {
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    celda.setCellValue(filaAct.getDescProvincia());
                }
                if(i == filas.size()-1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }*/
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    celda.setCellValue(filaAct.getDescProvincia());
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    celda.setCellValue(filaAct.getDescProvincia());
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                celda.setCellStyle(estiloCelda);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(1);
                /*if(cambioEntidad)
                {
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    celda.setCellValue(filaAct.getEntidadPromotora());
                }
                if(i == filas.size()-1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }*/
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    celda.setCellValue(filaAct.getEntidadPromotora());
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    celda.setCellValue(filaAct.getEntidadPromotora());
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                celda.setCellStyle(estiloCelda);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(2);    
                celda.setCellValue(filaAct.getEmpresa());
                celda.setCellStyle(estiloCelda);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(3);
                celda.setCellValue(filaAct.gethInd_psi_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(4);
                celda.setCellValue(filaAct.getmInd_psi_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(5);
                celda.setCellValue(filaAct.gethTemp_psi_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(6);
                celda.setCellValue(filaAct.getmTemp_psi_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(7);
                celda.setCellValue(filaAct.gethInd_psi_33_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(8);
                celda.setCellValue(filaAct.getmInd_psi_33_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(9);
                celda.setCellValue(filaAct.gethTemp_psi_33_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(10);
                celda.setCellValue(filaAct.getmTemp_psi_33_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(11);
                celda.setCellValue(filaAct.gethInd_fis_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(12);
                celda.setCellValue(filaAct.getmInd_fis_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(13);
                celda.setCellValue(filaAct.gethTemp_fis_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(14);
                celda.setCellValue(filaAct.getmTemp_fis_65());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(15);
                celda.setCellValue(filaAct.gethInd_sens_mas_33());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(16);
                celda.setCellValue(filaAct.getmInd_sens_mas_33());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(17);
                celda.setCellValue(filaAct.gethTemp_sens_mas_33());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                //estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                if(cambioProvincia)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
                else if(cambioEntidad)
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                }
                else
                {
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                }
                if(i == filas.size() - 1)
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                }
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(18);
                celda.setCellValue(filaAct.getmTemp_sens_mas_33());
                celda.setCellStyle(estiloCelda);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                
                p++;
                e++;
            }
            
            exportarExcel("informeDesgloseTrabDiscApoyo", libro, response);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void generarInformeProyectosExcel(List<FilaResumenInformeProyectos> filasResumen, HashMap<String, List<FilaInformeProyectos>> filasPorExpediente, String ano, int idioma, HttpServletResponse response)
    {
        try
        {
            MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
            
            HSSFWorkbook libro = new HSSFWorkbook();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    palette.setColorAtIndex(HSSFColor.ROYAL_BLUE.index, (byte)75, (byte)149, (byte)211);
                }
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            
            
            HSSFFont negritaTitulo = libro.createFont();
            negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            negritaTitulo.setFontHeight((short)200);
            HSSFFont negritaCabecera = libro.createFont();
            negritaCabecera.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            negritaCabecera.setFontHeight((short)180);
            negritaCabecera.setColor(HSSFColor.WHITE.index);
            HSSFFont normal = libro.createFont();
            normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
            normal.setFontHeight((short)180);
            HSSFSheet hoja = null;
            int numFila = 0;
            
            HSSFRow fila = null;
            HSSFCell celda = null;
            HSSFCellStyle estiloCelda = null;
            
            hoja = libro.createSheet();
            
            hoja.setColumnWidth(0, 8000);
            hoja.setColumnWidth(1, 10000);
            hoja.setColumnWidth(2, 10000);
            hoja.setColumnWidth(3, 10000);
            hoja.setColumnWidth(4, 3000);
            hoja.setColumnWidth(5, 3000);
            hoja.setColumnWidth(6, 5000);
            hoja.setColumnWidth(7, 1700);
            hoja.setColumnWidth(8, 1700);
            hoja.setColumnWidth(9, 1700);
            hoja.setColumnWidth(10, 1700);
            hoja.setColumnWidth(11, 1700);
            hoja.setColumnWidth(12, 1700);
            hoja.setColumnWidth(13, 1700);
            hoja.setColumnWidth(14, 1700);
            hoja.setColumnWidth(15, 3000);
            hoja.setColumnWidth(16, 3000);
            
            //------------------------------------------- FILA 1 -------------------------------------------------
            
            fila = hoja.createRow(numFila);
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 16));

            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.titulo").toUpperCase()+" "+ano);
            celda.setCellStyle(estiloCelda);
            
            //------------------------------------------- FILA 3 -------------------------------------------------
            
            numFila += 2;
            fila = hoja.createRow(numFila);
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 16));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);
            estiloCelda.setFont(negritaTitulo);
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.cAutonoma").toUpperCase());
            celda.setCellStyle(estiloCelda);
            
            //------------------------------------------- FILA 5 -------------------------------------------------
            
            numFila += 2;
            fila = hoja.createRow(numFila);
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 0, 0));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(0);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.provincia").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 1, 1));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(1);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.entidad").toUpperCase());
            celda.setCellStyle(estiloCelda);      
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 2, 2));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(2);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.empresa").toUpperCase());
            celda.setCellStyle(estiloCelda);     
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 3, 3));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(3);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.actividad").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 4, 4));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(4);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.fecAprobacion").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 5, 5));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(5);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.impSubvencion").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+3, 6, 6));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(6);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.insSeg").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+1, 7, 13));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(7);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.trabApoyados").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(8);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(9);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(10);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(11);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(12);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(13);
            celda.setCellStyle(estiloCelda);  
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+1, 14, 16));
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(14);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.prepLaborales").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(15);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(16);
            celda.setCellStyle(estiloCelda);  
            
            //------------------------------------------- FILA 6 -------------------------------------------------
            
            numFila++;
            fila = hoja.createRow(numFila);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(2);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(3);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(4);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(5);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(6);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(7);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(8);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(9);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(10);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(11);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(12);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(13);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(14);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(15);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(16);
            celda.setCellStyle(estiloCelda); 
            
            //------------------------------------------- FILA 7 -------------------------------------------------
            
            numFila++;
            fila = hoja.createRow(numFila);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(2);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(3);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(4);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(5);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(6);
            celda.setCellStyle(estiloCelda); 
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+1, 7, 7));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(7);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.total").toUpperCase());
            celda.setCellStyle(estiloCelda); 
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 8, 10));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(8);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.hombre").toUpperCase());
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(9);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(10);
            celda.setCellStyle(estiloCelda); 
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 11, 13));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(11);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.mujer").toUpperCase());
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(12);
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(13);
            celda.setCellStyle(estiloCelda); 
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+1, 14, 14));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(14);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.total").toUpperCase());
            celda.setCellStyle(estiloCelda); 
            
            hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 15, 16));
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(15);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.tipoContrato").toUpperCase());
            celda.setCellStyle(estiloCelda); 
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(16);
            celda.setCellStyle(estiloCelda); 
            
            //------------------------------------------- FILA 8 -------------------------------------------------
            
            numFila++;
            fila = hoja.createRow(numFila);
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(0);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(1);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(2);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(3);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(4);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(5);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(6);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(7);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(8);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.anos25").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(9);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.anos25_54").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(10);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.anos55").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(11);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.anos25").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(12);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.anos25_54").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(13);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.anos55").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(14);
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(15);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.indefinido").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFillForegroundColor(HSSFColor.ROYAL_BLUE.index);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(negritaCabecera);
            celda = fila.createCell(16);
            celda.setCellValue(meLanbide44I18n.getMensaje(idioma, "doc.informeProyectos.temporal").toUpperCase());
            celda.setCellStyle(estiloCelda);  
            
            //------------------------------------------- FILAS -------------------------------------------------
            
            List<FilaInformeProyectos> filasDatos = null;
            
            FilaResumenInformeProyectos filaResumen = null;
            
            for(int i = 0; i < filasResumen.size(); i++)
            {
                filaResumen = filasResumen.get(i);
                filasDatos = filasPorExpediente.get(filaResumen.getNumExp());
                
                //------------------------------------------- FILAS RESUMEN -------------------------------------------------
                numFila++;
                fila = hoja.createRow(numFila);
                
                hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 1, 4));
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(0);
                celda.setCellValue(filaResumen.getDescProvincia());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(1);
                celda.setCellValue(filaResumen.getEntidad());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(2);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(3);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(4);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(5);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                celda.setCellValue(filaResumen.getImporteSubvencion());
                celda.setCellStyle(estiloCelda);
                
                hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 6, 13));
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(6);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(7);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(8);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(9);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(10);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(11);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(12);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(13);
                celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(14);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                celda.setCellValue(filaResumen.getTotalPrep());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(15);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                celda.setCellValue(filaResumen.getPrepIndef());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                if(filasDatos == null || filasDatos.isEmpty())
                {
                    if(i == (filasResumen.size() - 1))
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                    }
                }
                else
                {
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                }
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setFont(normal);
                celda = fila.createCell(16);
                celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                celda.setCellValue(filaResumen.getPrepTempo());
                celda.setCellStyle(estiloCelda);
                
                
                
                //------------------------------------------- FILAS DATOS -------------------------------------------------
                
                
                FilaInformeProyectos filaAct = null;
                
                for(int j = 0; j < filasDatos.size(); j++)
                {
                
                    filaAct = filasDatos.get(j);
                    
                    numFila++;
                    fila = hoja.createRow(numFila);
                    
                    if(j == 0)
                    {
                        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+(filasDatos.size() - 1), 0, 0));
                    }
                    else if(j == 1)
                    {
                        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila+(filasDatos.size() - 2), 1, 1));
                    }

                    if(j == (filasDatos.size() - 1))
                    {
                        //Si es la ultima fila, hay que poner borde grueso abajo
                        estiloCelda = libro.createCellStyle();
                        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                        estiloCelda.setWrapText(true);
                        estiloCelda.setFont(normal);
                        celda = fila.createCell(0);
                        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                        celda.setCellStyle(estiloCelda);
                        
                        estiloCelda = libro.createCellStyle();
                        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                        estiloCelda.setWrapText(true);
                        estiloCelda.setFont(normal);
                        celda = fila.createCell(1);
                        if(j == 0)
                        {
                            celda.setCellValue(filaResumen.getTotalPrep()+" PREPARADORES LABORALES");
                        }
                        else
                        {
                            celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                        }
                        celda.setCellStyle(estiloCelda);
                    }
                    else
                    {
                        //Si no es la ultima fila, hay que poner borde fino abajo
                        estiloCelda = libro.createCellStyle();
                        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                        estiloCelda.setWrapText(true);
                        estiloCelda.setFont(normal);
                        celda = fila.createCell(0);
                        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                        celda.setCellStyle(estiloCelda);
                        
                        estiloCelda = libro.createCellStyle();
                        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                        estiloCelda.setWrapText(true);
                        estiloCelda.setFont(normal);
                        celda = fila.createCell(1);
                        if(j == 0)
                        {
                            celda.setCellValue(filaResumen.getTotalPrep()+" PREPARADORES LABORALES");
                        }
                        else
                        {
                            celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
                        }
                        celda.setCellStyle(estiloCelda);
                    }

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(2);
                    celda.setCellValue(filaAct.getEmpresa());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(3);
                    celda.setCellValue(filaAct.getActividad());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(4);
                    celda.setCellValue(filaAct.getFecAprobacion());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(5);
                    celda.setCellValue("-");
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(6);
                    celda.setCellValue(filaAct.getInsSeg());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(7);
                    celda.setCellValue(filaAct.getTotalTrabajadores());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(8);
                    celda.setCellValue(filaAct.getH25());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(9);
                    celda.setCellValue(filaAct.getH25_54());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(10);
                    celda.setCellValue(filaAct.getH55());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(11);
                    celda.setCellValue(filaAct.getM25());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(12);
                    celda.setCellValue(filaAct.getM25_54());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(13);
                    celda.setCellValue(filaAct.getM55());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(14);
                    celda.setCellValue(filaAct.getTotalPreparadores());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(15);
                    celda.setCellValue(filaAct.getPrepIndefinido());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    if(j == (filasDatos.size() - 1))
                    {
                        if(i == (filasResumen.size() - 1))
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    }
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell(16);
                    celda.setCellValue(filaAct.getPrepTemporal());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }
            }
            
            exportarExcel("informeProyectosEmpleoConApoyo", libro, response);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void exportarExcel(String nombreFichero, HSSFWorkbook libro, HttpServletResponse response)
    {
        try 
        {
            ResourceBundle m_Conf = ResourceBundle.getBundle("common");
            File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
            File informe = File.createTempFile(nombreFichero, ".xls", directorioTemp);

            FileOutputStream archivoSalida = new FileOutputStream(informe);
            libro.write(archivoSalida);
            archivoSalida.close();

            String rutaArchivoSalida = informe.getAbsolutePath();

            FileInputStream istr = new FileInputStream(rutaArchivoSalida);

            BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

            int size = (int) informe.length(); 
            byte[] data = new byte[size]; 
            bstr.read( data, 0, size ); 
            bstr.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
            response.setHeader("Content-Transfer-Encoding", "binary");  
            response.setContentLength(data.length);
            response.getOutputStream().write(data, 0, data.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } 
        catch (Exception ioe) 
        {
        }
    }
    
    private int contarFilasProvincia(String codProvincia, List<FilaInformeDesglose> filas)
    {
        int cont = 0;
        if(codProvincia != null && !codProvincia.equals(""))
        {
            if(filas != null)
            {
                for(FilaInformeDesglose fila : filas)
                {
                    if(fila.getCodProvincia() != null && fila.getCodProvincia().equalsIgnoreCase(codProvincia))
                    {
                        cont++;
                    }
                }
            }
        }
        return cont;
    }
    
    private int contarFilasEntidad(String entidad, List<FilaInformeDesglose> filas)
    {
        int cont = 0;
        if(entidad != null && !entidad.equals(""))
        {
            if(filas != null)
            {
                /*for(FilaInformeDesglose fila : filas)
                {
                    if(fila.getEntidadPromotora() != null && fila.getEntidadPromotora().equalsIgnoreCase(entidad))
                    {
                        cont++;
                    }
                }*/
                
                for(int i = 0; i < filas.size(); i++)
                {
                    FilaInformeDesglose fila = filas.get(i);
                    
                    if(fila.getEntidadPromotora() != null && fila.getEntidadPromotora().equalsIgnoreCase(entidad))
                    {
                        cont++;
                    }
                }
            }
        }
        return cont;
    }
}
