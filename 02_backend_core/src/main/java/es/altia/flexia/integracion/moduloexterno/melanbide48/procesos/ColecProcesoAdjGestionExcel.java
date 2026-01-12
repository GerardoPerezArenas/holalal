/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.procesos;

import es.altia.flexia.integracion.moduloexterno.melanbide48.MELANBIDE48;
import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecProcesoAdjGestionExcelDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecAmbitosBloquesHoras;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecProcesoAdjudicacionExcelDatosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecUbicCTValoracion;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 *
 * @author INGDGC
 */
public class ColecProcesoAdjGestionExcel {
    private static final Logger log = LogManager.getLogger(MELANBIDE48.class);
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static final ColecProcesoAdjGestionExcelDAO colecProcesoAdjGestionExcelDAO = new ColecProcesoAdjGestionExcelDAO();

    public void definirAnchoColumnas(HSSFSheet hojaColectivo) {
        if(hojaColectivo!=null){
            try {
                hojaColectivo.setColumnWidth(0, 7000);//num exp
                hojaColectivo.setColumnWidth(1, 10000);//nombre ent
                hojaColectivo.setColumnWidth(2, 10000);//direccion
                hojaColectivo.setColumnWidth(3, 10000);//municipio
                hojaColectivo.setColumnWidth(4, 4000);//cod postal
                hojaColectivo.setColumnWidth(5, 8000);//horas solic o numero de ubicaciones
                hojaColectivo.setColumnWidth(6, 8000);//trayectoria
                hojaColectivo.setColumnWidth(7, 8000);//punt ubic
                hojaColectivo.setColumnWidth(8, 8000);//Segundos Locales
                hojaColectivo.setColumnWidth(9, 6000);//Plan igualda
                hojaColectivo.setColumnWidth(10, 6000);//Certificado Calidad
                hojaColectivo.setColumnWidth(11, 6000);// Espacio Complementario
                hojaColectivo.setColumnWidth(12, 6000);//Centro Especial Empleo
                hojaColectivo.setColumnWidth(13, 6000);//Empresa de insercion o Promotora de empresas de Insercion
                hojaColectivo.setColumnWidth(14, 6000);//Total Puntuacion Ubicacion
                hojaColectivo.setColumnWidth(15, 6000);// Total Maxima Puntuacion Entidad
                hojaColectivo.setColumnWidth(16, 6000);//Bloques concedidos / Ambito otorgado
                hojaColectivo.setColumnWidth(17, 6000);//Tramite resolucion provisional?
                hojaColectivo.setColumnWidth(18, 6000);//Fecha Registro
            } catch (Exception e) {
                log.error("Error definirAnchoColumnas.. ", e);
            }
        }
    }
    
    public Map<String, HSSFCellStyle> crearEstilosCeldas(HSSFWorkbook libro) {
        Map<String, HSSFCellStyle> resultado = new HashMap<String, HSSFCellStyle>();
        try {
            HSSFCellStyle estilo = libro.createCellStyle();
            HSSFFont negrita = libro.createFont();
            negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFFont notasCursivaSize8 = libro.createFont();
            notasCursivaSize8.setItalic(true);
            notasCursivaSize8.setFontHeightInPoints((short)8);
            //Fondo Verde Negrita
            estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estilo.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
            estilo.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            estilo.setBorderTop(HSSFCellStyle.BORDER_THICK);
            estilo.setBorderLeft(HSSFCellStyle.BORDER_THICK);
            estilo.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estilo.setFont(negrita);
            estilo.setWrapText(true);
            resultado.put(ColecProcesoAdjExcelEstilosEnum.LIGHT_GREEN_BOLD_BORDER.getCodigoEstilo(), estilo);
            //Fondo Naranja Negrita
            estilo = libro.createCellStyle();
            estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estilo.setFillForegroundColor(HSSFColor.TAN.index);
            estilo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estilo.setBorderTop(HSSFCellStyle.BORDER_THIN);
            estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estilo.setBorderRight(HSSFCellStyle.BORDER_THIN);
            estilo.setFont(negrita);
            estilo.setWrapText(true);
            resultado.put(ColecProcesoAdjExcelEstilosEnum.TAN_BOLD_BORDER.getCodigoEstilo(), estilo);
            //Fondo Azul claro Negrita
            estilo = libro.createCellStyle();
            estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estilo.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
            estilo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estilo.setBorderTop(HSSFCellStyle.BORDER_THIN);
            estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estilo.setBorderRight(HSSFCellStyle.BORDER_THIN);
            estilo.setFont(negrita);
            estilo.setWrapText(true);
            resultado.put(ColecProcesoAdjExcelEstilosEnum.LIGHT_BLUE_BOLD_BORDER.getCodigoEstilo(), estilo);
            //Fondo Gris claro Negrita
            estilo = libro.createCellStyle();
            estilo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estilo.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
            estilo.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            estilo.setBorderTop(HSSFCellStyle.BORDER_THIN);
            estilo.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estilo.setBorderRight(HSSFCellStyle.BORDER_THIN);
            estilo.setFont(negrita);
            estilo.setWrapText(true);
            resultado.put(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo(), estilo);
            //Texto Cursiva y pequeno par notas
            estilo = libro.createCellStyle();
            estilo.setFont(notasCursivaSize8);
            resultado.put(ColecProcesoAdjExcelEstilosEnum.NOTAS_CURSIVA_SIZE_8.getCodigoEstilo(), estilo);
            
        } catch (Exception ex) {
            log.error("Error al preparar los estilos de las celdas ", ex);
        }
        return resultado;
    }

    public void agregarDatosAmbitoNumeroBloques(HSSFWorkbook libro, HSSFSheet hoja, ColecAmbitosBloquesHoras ambitoBloquesHoras, AtomicInteger numFila, MeLanbide48I18n meLanbide48I18n, int codIdioma, Map<String, HSSFCellStyle> estilosCelda) {
        int numeroCelda = 0;
        HSSFRow fila = hoja.createRow(numFila.getAndIncrement());

        HSSFCell celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "label.solicitud.colectivo.nuevoCentro.tabla.centros.ambito"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.LIGHT_GREEN_BOLD_BORDER.getCodigoEstilo()));
        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(codIdioma == 4 ? ambitoBloquesHoras.getAmbitoDescripcionEu() : ambitoBloquesHoras.getAmbitoDescripcion());
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.LIGHT_GREEN_BOLD_BORDER.getCodigoEstilo()));

        //reiniciamos la celda
        numeroCelda = 0;
        fila = hoja.createRow(numFila.getAndIncrement());
        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.nro.bloques.ambito"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.TAN_BOLD_BORDER.getCodigoEstilo()));
        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(ambitoBloquesHoras.getColectivo() < 3 ? ambitoBloquesHoras.getNumeroCentrosminimos() : ambitoBloquesHoras.getNumeroBloqueshoras());
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.TAN_BOLD_BORDER.getCodigoEstilo()));

    }

    public void agregarDatosCabeceraTablaBloques(HSSFWorkbook libro, HSSFSheet hojaColectivo, ColecAmbitosBloquesHoras ambitoBloquesHoras, AtomicInteger numFila, MeLanbide48I18n meLanbide48I18n, int codIdioma, Map<String, HSSFCellStyle> estilosCelda) {
        int numeroCelda = 0;
        HSSFRow fila = hojaColectivo.createRow(numFila.getAndIncrement());

        HSSFCell celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.numero.expediente"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.nombre.entidad"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.direccion"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.municipio"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.codigo.postal"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.horas.solicitadas.ubicaciones"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.trayectoria"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.ubicacion"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.segundos.locales"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.plan.igualdad"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.certificado.calidad"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.espacio.complementario"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.centro.especial.empleo"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.epresa.promotora.insercion"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.total.ubicacion"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.puntuacion.maxima.total.entidad"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));
        
        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.concedido"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.tramite.resolucion.provisional"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

        celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.fecha.registro"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

    }

    public void agregarDatosUbicacionesTablaBloques(HSSFWorkbook libro, HSSFSheet hojaColectivo, ColecAmbitosBloquesHoras ambitoBloquesHoras, AtomicInteger numFila, MeLanbide48I18n meLanbide48I18n, int codIdioma, Map<String, HSSFCellStyle> estilosCelda, Map<String,Double> mapaTotales,AdaptadorSQLBD asqlbd) throws Exception {
        List<ColecProcesoAdjudicacionExcelDatosVO> excelDatosAmbito = this.getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito(ambitoBloquesHoras.getIdFkConvocatoriaActiva(), ambitoBloquesHoras.getColectivo(), ambitoBloquesHoras.getId(), asqlbd);
        /** mapaTotales: 
         * put("sumaTotalUbicaciones",0.0); // Linea total para COlectivos 1 y 2
         * put("sumaTotalAmbitoBloquesSolitados",0.0); // Linea total para COlectivos 3 y 4 
         * put("sumaTotalAmbitoBloquesConcedidos",0.0); // Linea total para COlectivos 3 y 4
         * put("sumaTotalAmbitoValoracionesMaxEntidad",0.0); // Linea total para * COlectivos 1 a 4
         */
        if(excelDatosAmbito.isEmpty()){
            HSSFRow fila = hojaColectivo.createRow(numFila.getAndIncrement());
            int numeroCelda = 0;
            HSSFCell celda = fila.createCell(numeroCelda++);
            celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.sin.ubicaciones.registradas"));
        }else{
            Double sumaTotalUbicaciones=0.0;
            Double sumaTotalAmbitoBloquesSolitados=0.0;
            Double sumaTotalAmbitoBloquesConcedidos=0.0;
            Double sumaTotalAmbitoValoracionesMaxEntidad=0.0;
            for (ColecProcesoAdjudicacionExcelDatosVO excelDato : excelDatosAmbito) {
                HSSFRow filaAnt = hojaColectivo.getRow(numFila.get()-1);
                HSSFRow fila = hojaColectivo.createRow(numFila.getAndIncrement());
                int numeroCelda = 0;
                HSSFCell celda = fila.createCell(numeroCelda++);
                celda.setCellValue(excelDato.getNumeroExpediente());
                //celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(excelDato.getIdEntidadPadre() != excelDato.getIdEntidad() ? excelDato.getEntidadPadreNombre() : excelDato.getEntidadNombre());

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(excelDato.getDireccion());

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(excelDato.getMunicipioDes());

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(excelDato.getCodigoPostal());

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(excelDato.getIdColectivo() < 3 ? String.format(Locale.GERMAN, "%,.2f", excelDato.getNumeroUbicaciones()) : String.format(Locale.GERMAN, "%,.2f", excelDato.getBloquesHorasSolicitados()));
                
                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getPuntuacionTrayectoriaEentidad()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getPuntuacionUbicacionCT()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getPuntuacionSegundosLocales()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getPuntuacionPlanIgualdad()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getPuntuacionCertificadoCalidad()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getPuntuacionEspacioComplem()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getPuntuacionCentroEspEmpleo()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getPuntuacionEmpoPromEmpInsercion()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getValoracionTotalUbicacion()));

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(String.format(Locale.GERMAN, "%,.2f", excelDato.getValoracionTotalMaxEntidad()));

                celda = fila.createCell(numeroCelda++);
                String valorCelda = (excelDato.getIdColectivo()<3 ? (excelDato.getUbicacionAdjudicada()==1?"Si/Bai":"No/Ez")
                        :String.format(Locale.GERMAN, "%,.2f", excelDato.getHorasAsignadas())
                        );
                celda.setCellValue(valorCelda);
                
                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(excelDato.getExpEnTramResolProvisional());

                celda = fila.createCell(numeroCelda++);
                celda.setCellValue(excelDato.getFechaRegistroStr());
                
                // Consolidar Totales
                if(!excelDato.getNumeroExpediente().equalsIgnoreCase(filaAnt.getCell(0).getStringCellValue()))
                    sumaTotalAmbitoValoracionesMaxEntidad+=(excelDato.getValoracionTotalMaxEntidad()!=null?excelDato.getValoracionTotalMaxEntidad():0.0);
                if (excelDato.getIdColectivo() < 3) {
                    if(!excelDato.getNumeroExpediente().equalsIgnoreCase(filaAnt.getCell(0).getStringCellValue()))
                        sumaTotalUbicaciones += (excelDato.getNumeroUbicaciones()!=null ? excelDato.getNumeroUbicaciones(): 0.0);
                } else {
                    if(!excelDato.getNumeroExpediente().equalsIgnoreCase(filaAnt.getCell(0).getStringCellValue()))
                        sumaTotalAmbitoBloquesSolitados += (excelDato.getBloquesHorasSolicitados()!=null ? excelDato.getBloquesHorasSolicitados(): 0.0);
                    sumaTotalAmbitoBloquesConcedidos += (excelDato.getHorasAsignadas()!=null ? excelDato.getHorasAsignadas(): 0.0);
                }
            }
            mapaTotales.put("sumaTotalUbicaciones", sumaTotalUbicaciones);
            mapaTotales.put("sumaTotalAmbitoBloquesSolitados", sumaTotalAmbitoBloquesSolitados);
            mapaTotales.put("sumaTotalAmbitoBloquesConcedidos", sumaTotalAmbitoBloquesConcedidos);
            mapaTotales.put("sumaTotalAmbitoValoracionesMaxEntidad", sumaTotalAmbitoValoracionesMaxEntidad);
        }
    }

    public void agregarDatosTotalesTablaBloques(HSSFWorkbook libro, HSSFSheet hojaColectivo, ColecAmbitosBloquesHoras ambitoBloquesHoras, AtomicInteger numFila, MeLanbide48I18n meLanbide48I18n, int codIdioma, Map<String, HSSFCellStyle> estilosCelda,Map<String,Double> mapaTotales) {
        int numeroCelda = 0;
        HSSFRow filaAnt = hojaColectivo.getRow(numFila.get()-1);
        int indexLastCeldaFilaAnterior = filaAnt.getLastCellNum();
        HSSFRow fila = hojaColectivo.createRow(numFila.getAndIncrement());

        /**
         * mapaTotales: 
         * put("sumaTotalUbicaciones",0.0); // Linea total para COlectivos 1 y 2
         * put("sumaTotalAmbitoBloquesSolitados",0.0); // Linea total para COlectivos 3 y 4 
         * put("sumaTotalAmbitoBloquesConcedidos",0.0); // Linea total para COlectivos 3 y 4
         * put("sumaTotalAmbitoValoracionesMaxEntidad",0.0); // Linea total para * COlectivos 1 a 2
         */
        HSSFCell celda = fila.createCell(numeroCelda++);
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.totales"));
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));
        for (int i = numeroCelda; i < indexLastCeldaFilaAnterior; i++) {
            celda = fila.createCell(i);
            String valorCelda="";
            switch (i) {
                case 5:
                    if(ambitoBloquesHoras.getColectivo()<3)
                        valorCelda=String.format(Locale.GERMAN, "%,.2f", mapaTotales.get("sumaTotalUbicaciones"));
                    else
                        valorCelda=String.format(Locale.GERMAN, "%,.2f", mapaTotales.get("sumaTotalAmbitoBloquesSolitados"));
                    break;
                case 15:
                    valorCelda=String.format(Locale.GERMAN, "%,.2f", mapaTotales.get("sumaTotalAmbitoValoracionesMaxEntidad"));
                    break;
                case 16:
                    if(ambitoBloquesHoras.getColectivo()>2)
                        valorCelda = String.format(Locale.GERMAN, "%,.2f", mapaTotales.get("sumaTotalAmbitoBloquesConcedidos"));
                    break;
                default:
                    break;
            }
            celda.setCellValue(valorCelda);
            celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.GREY_25_PERCENT_BOLD_BORDER.getCodigoEstilo()));
        }
        // Agregar la nota para los totales
        numeroCelda = 0;
        fila = hojaColectivo.createRow(numFila.getAndIncrement());
        celda = fila.createCell(numeroCelda++);
        celda.setCellStyle(estilosCelda.get(ColecProcesoAdjExcelEstilosEnum.NOTAS_CURSIVA_SIZE_8.getCodigoEstilo()));
        celda.setCellValue(meLanbide48I18n.getMensaje(codIdioma, "procesos.adjudicacion.excel.totales.nota"));
    }
    
    public ColecProcesoAdjudicacionExcelDatosVO getColecProcesoAdjudicacionExcelDatosVOByIdUbicacion(int idConvocatoria,int idFkUbicacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecProcesoAdjGestionExcelDAO.getColecProcesoAdjudicacionExcelDatosVOByIdUbicacion(idConvocatoria,idFkUbicacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecProcesoAdjudicacionExcelDatosVOByIdUbicacion ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecProcesoAdjudicacionExcelDatosVOByIdUbicacion ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    public List<ColecProcesoAdjudicacionExcelDatosVO> getColecProcesoAdjudicacionExcelDatosVOByIdColectivo(int idConvocatoria,int idColectivo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecProcesoAdjGestionExcelDAO.getColecProcesoAdjudicacionExcelDatosVOByIdColectivo(idConvocatoria,idColectivo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecProcesoAdjudicacionExcelDatosVOByIdColectivo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecProcesoAdjudicacionExcelDatosVOByIdColectivo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    public List<ColecProcesoAdjudicacionExcelDatosVO> getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito(int idConvocatoria,int idColectivo,int idAmbito, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecProcesoAdjGestionExcelDAO.getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito(idConvocatoria,idColectivo,idAmbito, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecProcesoAdjudicacionExcelDatosVOByIdColectivoIdAmbito ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }    
}
