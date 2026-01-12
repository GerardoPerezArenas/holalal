/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32;

import es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide32.manager.MeLanbide32Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide32.manager.MeLanbide32ManagerCriteriosSeleccionCE;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.AmbitoCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.OriCECriteriosEvaDTO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EspecialidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.ProvinciaVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

/**
 *
 * @author INGDGC
 */
public class DocumentacionResolucion {
    
    private static Logger log = LogManager.getLogger(DocumentacionResolucion.class);
    private static MeLanbide32ManagerCriteriosSeleccionCE meLanbide32ManagerCriteriosSeleccionCE =  new MeLanbide32ManagerCriteriosSeleccionCE();

    public static HSSFWorkbook crearDocumentacionResolucionCentrosCEMP2014(Integer ano,String codProcedimiento,List<Integer> provincias,Integer idioma,MeLanbide32I18n meLanbide32I18n,AdaptadorSQLBD adapt) {
        log.debug("crearDocumentacionResolucionCentrosCEMP2014 - Begin()");
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        ProvinciaVO prov = null;
        HSSFSheet hoja = null;
        List<AmbitoCentroEmpleoVO> ambitosProv = null;
        int numFila = 0;
        List<CeUbicacionVO> ubicacionesAmbito = null;
        EntidadVO entidad = null;
        int totalesAdjudicadas = 0;
        boolean adjudicada = false;
        try {
            for (Integer codProv : provincias) {
                prov = MeLanbide32Manager.getInstance().getProvinciaPorCodigo(codProv, adapt);
                if (prov != null && prov.getPrvNom() != null) {
                    String provincia = "";
                    if (prov.getPrvNom().toUpperCase().contains("ARABA")) {
                        provincia = "ARABA";
                    } else {
                        provincia = prov.getPrvNom().toUpperCase();
                    }
                    hoja = libro.createSheet(provincia);

                    hoja.setColumnWidth(0, 7525);
                    hoja.setColumnWidth(1, 7000);
                    hoja.setColumnWidth(2, 2500);
                    hoja.setColumnWidth(3, 1500);
                    hoja.setColumnWidth(4, 4000);
                    hoja.setColumnWidth(5, 7000);
                    hoja.setColumnWidth(6, 3000);
                    hoja.setColumnWidth(7, 3000);
                    hoja.setColumnWidth(8, 3500);
                    hoja.setColumnWidth(9, 4000);
                    hoja.setColumnWidth(10, 3000);
                    hoja.setColumnWidth(11, 4500);
                    hoja.setColumnWidth(12, 15000);

                    ambitosProv = MeLanbide32Manager.getInstance().getAmbitosPorProvincia(codProv, ano, adapt);
                    for (AmbitoCentroEmpleoVO amb : ambitosProv) {
                        //Ambito
                        anadirAmbitoResolucionCentros(libro, hoja, amb, numFila, meLanbide32I18n, idioma);

                        //Cabecera 1 Tabla
                        numFila = numFila + 4;
                        anadirCabecera1ResolucionCentrosCEMP2014(libro, hoja, numFila, meLanbide32I18n, idioma);

                        //Cabecera 2 tabla
                        numFila = numFila + 1;
                        anadirCabecera2ResolucionCentrosCEMP2014(libro, hoja, numFila, meLanbide32I18n, idioma);

                        //Entidades
                        try {
                            ubicacionesAmbito = MeLanbide32Manager.getInstance().getUbicacionesDeAmbitoCEMP2014(amb, adapt);
                            for (CeUbicacionVO ubic : ubicacionesAmbito) {
                                numFila = numFila + 1;
                                entidad = MeLanbide32Manager.getInstance().getEntidadPorCodigo(ubic.getOriEntCod(), adapt);
                                if (entidad != null) {
                                    adjudicada = anadirEntidadResolucionCentrosCEMP2014(libro, hoja, ubic, entidad, numFila, codProcedimiento, adapt);
                                    if (adjudicada) {
                                        totalesAdjudicadas++;
                                    }
                                }
                            }
                        } catch (Exception ex) {

                        }

                        //Totales
                        numFila = numFila + 1;
                        anadirFilaTotalesResolucionCentrosCEMP2014(libro, hoja, numFila, ubicacionesAmbito != null ? ubicacionesAmbito.size() : 0, totalesAdjudicadas, meLanbide32I18n, idioma);
                        numFila = numFila + 4;
                        totalesAdjudicadas = 0;
                    }
                }
                numFila = 0;
            }
        } catch (Exception e) {
        }
        log.debug("crearDocumentacionResolucionCentrosCEMP2014 - End()");
        return libro;
    }

    public static HSSFWorkbook crearDocumentacionResolucionCentrosCEMPdsd2017(Integer ano,String codProcedimiento,List<Integer> provincias,Integer idioma,MeLanbide32I18n meLanbide32I18n,AdaptadorSQLBD adapt) {
        log.debug("crearDocumentacionResolucionCentrosCEMPdsd2017 - Begin()");
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        ProvinciaVO prov = null;
        HSSFSheet hoja = null;
        List<AmbitoCentroEmpleoVO> ambitosProv = null;
        int numFila = 0;
        List<CeUbicacionVO> ubicacionesAmbito = null;
        EntidadVO entidad = null;
        int totalesAdjudicadas = 0;
        boolean adjudicada = false;
        try {
            for (Integer codProv : provincias) {
                prov = MeLanbide32Manager.getInstance().getProvinciaPorCodigo(codProv, adapt);
                if (prov != null && prov.getPrvNom() != null) {
                    String provincia = "";
                    if (prov.getPrvNom().toUpperCase().contains("/")) {
                        provincia = prov.getPrvNom().toUpperCase().replaceAll("/", "_");
                    } else {
                        provincia = prov.getPrvNom().toUpperCase();
                    }
                    hoja = libro.createSheet(provincia);

                    hoja.setColumnWidth(0, 7525); // Num Exp
                    hoja.setColumnWidth(1, 7000); // Nombre Empresa
                    hoja.setColumnWidth(2, 2500); // Centro Especial
                    hoja.setColumnWidth(3, 2000); // Admin Local
                    hoja.setColumnWidth(4, 2000); // Supramunicipal
                    // Incluye nuevos campos tipo entidad como check radio 
                    hoja.setColumnWidth(5, 2000); // Ate. Pers. Disca.
                    hoja.setColumnWidth(6, 2000); // Colec. Riesgo Exclu.
                    hoja.setColumnWidth(7, 2000); // Centro Formacion profe
                    hoja.setColumnWidth(8, 2000); // Org. Sind. Eempres
                    hoja.setColumnWidth(9, 2000); // Sin animo lucro
                    // fin nuevos campos
                    hoja.setColumnWidth(10, 3000); // Agencia colocacion
                    hoja.setColumnWidth(11, 3000); // Num Trabajadores
                    hoja.setColumnWidth(12, 3500); // Num Traba, Discapacidad
                    hoja.setColumnWidth(13, 4000); // Porce. Trabaj. discapacidad
                    hoja.setColumnWidth(14, 3000); // años atrayectoria
                    hoja.setColumnWidth(15, 4500); // Denegado
                    hoja.setColumnWidth(16, 15000); // Motivo Deegacion
                    // añadimos informacion tramite y fecha registro
                    hoja.setColumnWidth(17, 3000);//Tramite resolucion provisional?
                    hoja.setColumnWidth(18, 6000);//Fecha Registrp
                    
                    ambitosProv = MeLanbide32Manager.getInstance().getAmbitosPorProvincia(codProv, ano, adapt);
                    for (AmbitoCentroEmpleoVO amb : ambitosProv) {
                        //Ambito
                        anadirAmbitoResolucionCentros(libro, hoja, amb, numFila, meLanbide32I18n, idioma);

                        //Cabecera 1 Tabla
                        numFila = numFila + 4;
                        anadirCabecera1ResolucionCentrosCEMPdsd2017(libro, hoja, numFila, meLanbide32I18n, idioma);

                        //Cabecera 2 tabla
                        numFila = numFila + 1;
                        anadirCabecera2ResolucionCentrosCEMPdsd2017(libro, hoja, numFila, meLanbide32I18n, idioma);

                        //Entidades
                        try {
                            // La condicion para convocatorias >=2017 esta en el dao.
                            ubicacionesAmbito = MeLanbide32Manager.getInstance().getUbicacionesDeAmbitoCEMP2014(amb, adapt);
                            for (CeUbicacionVO ubic : ubicacionesAmbito) {
                                numFila = numFila + 1;
                                entidad = MeLanbide32Manager.getInstance().getEntidadPorCodigo(ubic.getOriEntCod(), adapt);
                                if (entidad != null) {
                                    adjudicada = anadirEntidadResolucionCentrosCEMPdsd2017(libro, hoja, ubic, entidad, numFila, codProcedimiento, adapt);
                                    if (adjudicada) {
                                        totalesAdjudicadas++;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            log.error("crearDocumentacionResolucionCentrosCEMPdsd2017 -  Error al procesar los datos de las ubicaciones/entidades", ex);
                        }

                        //Totales
                        numFila = numFila + 1;
                        anadirFilaTotalesResolucionCentrosCEMP2014dsd2017(libro, hoja, numFila, ubicacionesAmbito != null ? ubicacionesAmbito.size() : 0, totalesAdjudicadas, meLanbide32I18n, idioma,ano);
                        numFila = numFila + 4;
                        totalesAdjudicadas = 0;
                    }
                }
                numFila = 0;
            }
        } catch (Exception e) {
        }
        log.debug("crearDocumentacionResolucionCentrosCEMPdsd2017 - End()");
        return libro;
        
    }
    
    public static void anadirAmbitoResolucionCentros(HSSFWorkbook libro, HSSFSheet hoja, AmbitoCentroEmpleoVO amb, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma)
    {
        HSSFRow fila = hoja.createRow(numFila);
        
        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        estiloCelda.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);       
        estiloCelda.setFont(negrita);
        
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.ambito"));
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell(1);
        celda.setCellValue(amb.getOriAmbAmbito() != null ? amb.getOriAmbAmbito() : "");
        celda.setCellStyle(estiloCelda);
        
        numFila = numFila + 1;
        fila = hoja.createRow(numFila);
        celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.centrosDisponibles"));
        celda.setCellStyle(estiloCelda);

        celda = fila.createCell(1);
        celda.setCellValue(amb.getOriAmbCe() != null ? amb.getOriAmbCe().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        numFila = numFila + 1;
        fila = hoja.createRow(numFila);
        HSSFCellStyle estiloCeldaCEEspeDispo = libro.createCellStyle();
        estiloCeldaCEEspeDispo.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        estiloCeldaCEEspeDispo.setFillForegroundColor(HSSFColor.AQUA.index);
        estiloCeldaCEEspeDispo.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCeldaCEEspeDispo.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCeldaCEEspeDispo.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCeldaCEEspeDispo.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCeldaCEEspeDispo.setWrapText(true);
        estiloCeldaCEEspeDispo.setFont(negrita);
        celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.centrosEspecialesDisponibles"));
        celda.setCellStyle(estiloCeldaCEEspeDispo);

        celda = fila.createCell(1);
        celda.setCellValue(amb.getOriAmbCeEspecial() != null ? amb.getOriAmbCeEspecial().doubleValue() : 0);
        celda.setCellStyle(estiloCeldaCEEspeDispo);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    }
    
    private static void anadirCabecera1ResolucionCentrosCEMP2014(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma)
    {
        HSSFRow fila = hoja.createRow(numFila);

        hoja.addMergedRegion(new CellRangeAddress (numFila,numFila,3,10));
        
        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(3);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.valoracion"));
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(9);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        celda.setCellStyle(estiloCelda);
        /*
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_NONE);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(11);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(12);
        celda.setCellStyle(estiloCelda);
        */
    }
    
    private static void anadirCabecera1ResolucionCentrosCEMPdsd2017(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma)
    {
        HSSFRow fila = hoja.createRow(numFila);

        hoja.addMergedRegion(new CellRangeAddress (numFila,numFila,3,14));
        
        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(3);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.valoracion"));
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(9);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(11);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(12);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(13);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(14);
        celda.setCellStyle(estiloCelda);
        
    }
    
    
    private static void anadirCabecera2ResolucionCentrosCEMP2014(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma)
    {
        HSSFRow fila = hoja.createRow(numFila);
        fila.setHeight((short)800);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0,1));
        
        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.entColaboradoras"));
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);
        

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.centroEspecial"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(3);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.admLocal"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.supramun"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.especialidad"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.acolocacion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.numTrab"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.numTrabDisc"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(9);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.porTrabDisc"));
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.anosTray"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIME.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(11);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.adjudicado"));
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(12);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.motivodenegacion"));
        celda.setCellStyle(estiloCelda);
    }
    
    private static void anadirCabecera2ResolucionCentrosCEMPdsd2017(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma) {
        HSSFRow fila = hoja.createRow(numFila);
        fila.setHeight((short) 800);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 1));

        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.entColaboradoras"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.centroEspecial"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(3);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.admLocal"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.supramun"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.especialidad.personas.discapacidad"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.especialidad.colectivos.riegos.exclusion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.especialidad.centro.formacion.profesional"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.organizacion.sindical"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(9);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.organizacion.sin.animo.lucro"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.acolocacion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(11);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.numTrab"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(12);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.numTrabDisc"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(13);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.porTrabDisc"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(14);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.anosTray"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.LIME.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(15);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.adjudicado"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(16);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.motivodenegacion"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(17);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.estaTramite.resolucionProvisional"));
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(18);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.fecha.registro"));
        celda.setCellStyle(estiloCelda);
    }
    
    private static boolean anadirEntidadResolucionCentrosCEMP2014(HSSFWorkbook libro, HSSFSheet hoja, CeUbicacionVO ubic, EntidadVO entidad, int numFila, String codProcedimiento, AdaptadorSQLBD adapt)
    {
        HSSFRow fila = hoja.createRow(numFila);
        
        //hoja.addMergedRegion(new Region(numFila, 0, numFila, 1));
        
        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);             
        celda.setCellValue(entidad.getExtNum());
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        String nomEnt = entidad != null && entidad.getOriEntNom() != null ? entidad.getOriEntNom() : "";
        nomEnt += nomEnt != null && !nomEnt.equals("") ? (ubic.getOriCeDireccion() != null && !ubic.getOriCeDireccion().equals("") ? " - " : "") : "";
        nomEnt += ubic != null && ubic.getOriCeDireccion() != null && !ubic.getOriCeDireccion().equals("") ? ubic.getOriCeDireccion() : "";
                
        celda.setCellValue(nomEnt);
        celda.setCellStyle(estiloCelda);
        
//        estiloCelda = libro.createCellStyle();
//        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
//        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
//        estiloCelda.setWrapText(true);
//        celda = fila.createCell(2);
//        celda.setCellStyle(estiloCelda);
        
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(ubic.getOriCeEspecial() != null ? ubic.getOriCeEspecial() : "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(3);
        celda.setCellValue(entidad.getOriEntAdmLocalVal() != null ? entidad.getOriEntAdmLocalVal() : "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellValue(entidad.getOriEntSupramunVal() != null ? entidad.getOriEntSupramunVal() : "");
        celda.setCellStyle(estiloCelda);
        
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        if(entidad.getOriExpCodVal() != null)
        {
            String codEspOtros = ConfigurationParameter.getParameter("codEspecialidadOtros", ConstantesMeLanbide32.FICHERO_CONF_PANTALLAS);
            if(codEspOtros != null && codEspOtros.equals(entidad.getOriExpCodVal().toString()))
            {
                celda.setCellValue(entidad.getOriEntOtrosVal() != null ? entidad.getOriEntOtrosVal() : "");
            }
            else
            {
                EspecialidadVO especialidad = null;
                try
                {
                    especialidad = MeLanbide32Manager.getInstance().getEspecialidadPorCodigo(entidad.getOriExpCodVal(), adapt);

                }
                catch(Exception ex)
                {

                }
                celda.setCellValue(especialidad != null && especialidad.getOriEspDesc() != null ? especialidad.getOriEspDesc() : "");
            }
        }
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellValue(entidad.getOriEntAcolocacionVal() != null ? entidad.getOriEntAcolocacionVal() : "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.DARK_YELLOW.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellValue(entidad.getOriEntNumtrabVal() != null ? entidad.getOriEntNumtrabVal().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellValue(entidad.getOriEntNumtrabDiscVal() != null ? entidad.getOriEntNumtrabDiscVal().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(9);
        celda.setCellValue(entidad.getOriEntPortrabDiscVal()!= null ? entidad.getOriEntPortrabDiscVal().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        int anosTray = 0;
        if(entidad != null)
        {
            try
            {
                anosTray = MeLanbide32Manager.getInstance().getAnosTrayectoriaCE(entidad, adapt);
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            }
        }
        celda.setCellValue(anosTray);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(11);
        String adjudicada = null;
        boolean estaAdj = false;
        boolean estaDen = false;
        
        try
        {
            adjudicada = MeLanbide32Manager.getInstance().getUbicacionAdjudicadaCEMP2014(ubic.getOriCeUbicCod(),ubic.getOrdenAdjudicado(), adapt);
            if(adjudicada != null && adjudicada.equals(ConstantesMeLanbide32.ADJUDICADO))
            {
                estaAdj = true;
            }
            else
            {
                // || !adjudicada.contains(ConstantesMeLanbide32.AMBITODIST)
                // || !adjudicada.contains(ConstantesMeLanbide32.SUPLENTE)
                if(adjudicada == null || adjudicada.isEmpty())
                {
                    estaDen=true;
                    adjudicada = ConstantesMeLanbide32.DENEGADO;
                }
            }
        }
        catch(Exception ex)
        {
            
        }
        //celda.setCellValue(adjudicada != null && !adjudicada.equals("") ? adjudicada : ConstantesMeLanbide32.NO);
        celda.setCellValue(adjudicada);
        celda.setCellStyle(estiloCelda);
        
         estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(12);
        String motivoDenegacion = null;

        try
        {
            if(estaDen){
                motivoDenegacion = MeLanbide32Manager.getInstance().getMotivoDenegacionCEMP2014_ExcelResol(entidad, adapt);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al recueprar el motivo de denegacion cuando generamos el excel de resolucion en CEMP Procesos" + ex.getMessage());
        }
        celda.setCellValue(motivoDenegacion);
        celda.setCellStyle(estiloCelda);
        
        return estaAdj;
    }
    
    private static boolean anadirEntidadResolucionCentrosCEMPdsd2017(HSSFWorkbook libro, HSSFSheet hoja, CeUbicacionVO ubic, EntidadVO entidad, int numFila, String codProcedimiento, AdaptadorSQLBD adapt)
    {
        HSSFRow fila = hoja.createRow(numFila);
        
        //hoja.addMergedRegion(new Region(numFila, 0, numFila, 1));
        
        HSSFCellStyle estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        HSSFCell celda = fila.createCell(0);             
        celda.setCellValue(entidad.getExtNum());
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(1);
        String nomEnt = entidad != null && entidad.getOriEntNom() != null ? entidad.getOriEntNom() : "";
        nomEnt += nomEnt != null && !nomEnt.equals("") ? (ubic.getOriCeDireccion() != null && !ubic.getOriCeDireccion().equals("") ? " - " : "") : "";
        nomEnt += ubic != null && ubic.getOriCeDireccion() != null && !ubic.getOriCeDireccion().equals("") ? ubic.getOriCeDireccion() : "";
                
        celda.setCellValue(nomEnt);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(2);
        celda.setCellValue(ubic.getOriCeEspecial() != null ? ubic.getOriCeEspecial() : "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(3);
        celda.setCellValue(entidad.getOriEntAdmLocalVal() != null ? entidad.getOriEntAdmLocalVal() : "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(4);
        celda.setCellValue(entidad.getOriEntSupramunVal() != null ? entidad.getOriEntSupramunVal() : "");
        celda.setCellStyle(estiloCelda);
        
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(5);
        celda.setCellValue(entidad.getTipAtenPerDiscapaVAL()!= null ? entidad.getTipAtenPerDiscapaVAL(): "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(6);
        celda.setCellValue(entidad.getTipAtenColRiesExcVAL()!= null ? entidad.getTipAtenColRiesExcVAL(): "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(7);
        celda.setCellValue(entidad.getTipCentroForProfeVAL()!= null ? entidad.getTipCentroForProfeVAL(): "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(8);
        celda.setCellValue(entidad.getTipOrgSindiEmpresVAL()!= null ? entidad.getTipOrgSindiEmpresVAL(): "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(9);
        celda.setCellValue(entidad.getTipSinAnimoLucroVAL()!= null ? entidad.getTipSinAnimoLucroVAL(): "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(10);
        celda.setCellValue(entidad.getOriEntAcolocacionVal() != null ? entidad.getOriEntAcolocacionVal() : "");
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.DARK_YELLOW.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(11);
        celda.setCellValue(entidad.getOriEntNumtrabVal() != null ? entidad.getOriEntNumtrabVal().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(12);
        celda.setCellValue(entidad.getOriEntNumtrabDiscVal() != null ? entidad.getOriEntNumtrabDiscVal().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(13);
        celda.setCellValue(entidad.getOriEntPortrabDiscVal()!= null ? entidad.getOriEntPortrabDiscVal().doubleValue() : 0);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(14);
        int anosTray = 0;
        if(entidad != null)
        {
            try
            {
                anosTray = MeLanbide32Manager.getInstance().getAnosTrayectoriaValidadaCE(entidad, adapt);
            }
            catch(Exception ex)
            {
                log.error("Error al recuperar los años de trayectoria validada",ex);
            }
        }
        celda.setCellValue(anosTray);
        celda.setCellStyle(estiloCelda);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.TAN.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(15);
        String adjudicada = null;
        boolean estaAdj = false;
        boolean estaDen = false;
        
        try
        {
            adjudicada = MeLanbide32Manager.getInstance().getUbicacionAdjudicadaCEMP2014dsd2017(ubic.getOriCeUbicCod(),ubic.getOrdenAdjudicado(), adapt);
            if(adjudicada != null && adjudicada.equals(ConstantesMeLanbide32.ADJUDICADO))
            {
                estaAdj = true;
            }
            else
            {
                // || !adjudicada.contains(ConstantesMeLanbide32.AMBITODIST)
                // || !adjudicada.contains(ConstantesMeLanbide32.SUPLENTE)
                if(adjudicada == null || adjudicada.isEmpty())
                {
                    estaDen=true;
                    adjudicada = ConstantesMeLanbide32.DENEGADO;
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Error al validar si el centro es o no adjudicado",ex);
        }
        //celda.setCellValue(adjudicada != null && !adjudicada.equals("") ? adjudicada : ConstantesMeLanbide32.NO);
        celda.setCellValue(adjudicada);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(16);
        String motivoDenegacion = null;

        try
        {
            if(estaDen){
                motivoDenegacion = MeLanbide32Manager.getInstance().getMotivoDenegacionCEMP2014_ExcelResol(entidad, adapt);
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error al recueprar el motivo de denegacion cuando generamos el excel de resolucion en CEMP Procesos" + ex.getMessage());
        }
        celda.setCellValue(motivoDenegacion);
        celda.setCellStyle(estiloCelda);
        
        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(17);
        Boolean estaResolProvisionalTramite = null;
        try {
            estaResolProvisionalTramite = MeLanbide32Manager.getInstance().expteEstaTramiteResolProvisional(entidad.getExtNum(), adapt);
        } catch (Exception e) {
            log.error("Error al comprobar si el expediente esta en resolucion provisional " + entidad.getExtNum(), e);
        }
        String valorCompruebaTramite = "";
        if (estaResolProvisionalTramite != null) {
            if (estaResolProvisionalTramite) {
                valorCompruebaTramite = "S";
            } else {
                valorCompruebaTramite = "N";
            }
        }
        celda.setCellValue(valorCompruebaTramite);
        celda.setCellStyle(estiloCelda);

        estiloCelda = libro.createCellStyle();
        estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCelda.setFillForegroundColor(HSSFColor.WHITE.index);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setWrapText(true);
        celda = fila.createCell(18);
        String fechaRegistro = "";
        try {
            fechaRegistro = MeLanbide32Manager.getInstance().getFechaRegistroExpediente(entidad.getExtNum(), codProcedimiento, adapt);
        } catch (Exception e) {
            log.error("Error al comprobar si el expediente esta en resolucion provisional " + entidad.getExtNum(), e);
        }
        celda.setCellValue(fechaRegistro);
        celda.setCellStyle(estiloCelda);
        
        return estaAdj;
    }
    
    // Añadimos año en la llamada para mostrar total en una columna u otra segun convocatoria.
    private static void anadirFilaTotalesResolucionCentrosCEMP2014(HSSFWorkbook libro, HSSFSheet hoja, int numFila, int solicitadosTotales, int adjudicadosTotales, MeLanbide32I18n meLanbide32I18n, int idioma)
    {
        anadirFilaTotalesResolucionCentrosCEMP2014dsd2017(libro, hoja, numFila, solicitadosTotales, adjudicadosTotales, meLanbide32I18n, idioma,null);
    }
    private static void anadirFilaTotalesResolucionCentrosCEMP2014dsd2017(HSSFWorkbook libro, HSSFSheet hoja, int numFila, int solicitadosTotales, int adjudicadosTotales, MeLanbide32I18n meLanbide32I18n, int idioma, Integer ano)
    {
        HSSFRow fila = hoja.createRow(numFila);
        
        hoja.addMergedRegion(new CellRangeAddress(numFila,numFila,0,1));
        
        HSSFCellStyle estiloCeldaTotal = libro.createCellStyle();
        estiloCeldaTotal.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCeldaTotal.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCeldaTotal.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCeldaTotal.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCeldaTotal.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCeldaTotal.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCeldaTotal.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloCeldaTotal.setWrapText(true);
        
        HSSFCell celda = fila.createCell(0);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.centrosTotSolicitadas"));
        celda.setCellStyle(estiloCeldaTotal);
        
        celda = fila.createCell(1);
        celda.setCellStyle(estiloCeldaTotal);
        
        celda = fila.createCell(2);
        celda.setCellValue(Double.valueOf(solicitadosTotales));
        celda.setCellStyle(estiloCeldaTotal);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                
        // Si es convocatoria anterior a 2017 Se muestra en la columna 11 sino en la 15
        if(ano!=null && ano>=2017){
            if(ano>=2023)
                celda = fila.createCell(8);
            else 
                celda = fila.createCell(15);
        }else{
            celda = fila.createCell(11);
        }
        celda.setCellValue(Double.valueOf(adjudicadosTotales));
        celda.setCellStyle(estiloCeldaTotal);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
    }

    static HSSFWorkbook crearDocumentacionResolucionCentrosCEMPdsd2023(Integer ano, String codProcedimiento, List<Integer> provincias, int idioma, MeLanbide32I18n meLanbide32I18n, AdaptadorSQLBD adapt) {
        log.info("crearDocumentacionResolucionCentrosCEMPdsd2023 - Begin()");
        HSSFWorkbook libro = new HSSFWorkbook();
        HSSFFont negrita = libro.createFont();
        negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        ProvinciaVO prov = null;
        HSSFSheet hoja = null;
        List<AmbitoCentroEmpleoVO> ambitosProv = null;
        int numFila = 0;
        List<CeUbicacionVO> ubicacionesAmbito = null;
        EntidadVO entidad = null;
        int totalesAdjudicadas = 0;
        boolean adjudicada = false;
        try {
            List<OriCECriteriosEvaDTO> criteriosSeleccion = meLanbide32ManagerCriteriosSeleccionCE.getOriCECriteriosEvaDTOByCodigo("2",ano, adapt);
            for (Integer codProv : provincias) {
                prov = MeLanbide32Manager.getInstance().getProvinciaPorCodigo(codProv, adapt);
                if (prov != null && prov.getPrvNom() != null) {
                    String provincia = "";
                    if (prov.getPrvNom().toUpperCase().contains("/")) {
                        provincia = prov.getPrvNom().toUpperCase().replaceAll("/", "_");
                    } else {
                        provincia = prov.getPrvNom().toUpperCase();
                    }
                    hoja = libro.createSheet(provincia);

                    int indiceColumna  = 0;
                    hoja.setColumnWidth(indiceColumna++, 7525); // Num Exp
                    hoja.setColumnWidth(indiceColumna++, 7000); // Nombre Empresa
                    hoja.setColumnWidth(indiceColumna++, 2500); // Centro Especial
                    hoja.setColumnWidth(indiceColumna++, 2000); // Admin Local
                    hoja.setColumnWidth(indiceColumna++, 2000); // Supramunicipal
                    // Incluye nuevos campos tipo entidad como check radio 
                    hoja.setColumnWidth(indiceColumna++, 2000); // Ate. Pers. Disca.
                    hoja.setColumnWidth(indiceColumna++, 2000); // Colec. Riesgo Exclu.
                    // Para convocatorias a prtir de 2025 Agregamos los criterios de valoracion al excel
                    if(ano >=2025){
                        // Criterios de selecccion de la convocatoria
                        if(criteriosSeleccion != null && !criteriosSeleccion.isEmpty()){
                            for (int i = 0; i < criteriosSeleccion.size(); i++) {
                                hoja.setColumnWidth(indiceColumna++, 3500);
                            }
                        }

                    }
                    int ultimaColumnaMergeValoracion = indiceColumna;
                    hoja.setColumnWidth(indiceColumna++, 3000); // Sumatoria puntuacion
                    hoja.setColumnWidth(indiceColumna++, 4500); // Denegado
                    hoja.setColumnWidth(indiceColumna++, 15000); // Motivo Deegacion
                    // añadimos informacion tramite y fecha registro
                    hoja.setColumnWidth(indiceColumna++, 3000);//Tramite resolucion provisional?
                    hoja.setColumnWidth(indiceColumna++, 6000);//Fecha Registrp

                    ambitosProv = MeLanbide32Manager.getInstance().getAmbitosPorProvincia(codProv, ano, adapt);
                    for (AmbitoCentroEmpleoVO amb : ambitosProv) {
                        //Ambito
                        anadirAmbitoResolucionCentros(libro, hoja, amb, numFila, meLanbide32I18n, idioma);

                        //Cabecera 1 Tabla
                        numFila = numFila + 4;
                        anadirCabecera1ResolucionCentrosCEMPdsd2023(libro, hoja, numFila, ultimaColumnaMergeValoracion, meLanbide32I18n, idioma, criteriosSeleccion);


                        //Cabecera 2 tabla
                        numFila = numFila + 1;
                        anadirCabecera2ResolucionCentrosCEMPdsd2023(libro, hoja, numFila, meLanbide32I18n,idioma, criteriosSeleccion);

                        //Entidades
                        try {
                            ubicacionesAmbito = MeLanbide32Manager.getInstance().getUbicacionesDeAmbitoCEMP2014(amb, adapt);
                            for (CeUbicacionVO ubic : ubicacionesAmbito) {
                                numFila = numFila + 1;
                                entidad = MeLanbide32Manager.getInstance().getEntidadPorCodigo(ubic.getOriEntCod(), adapt);
                                if (entidad != null) {
                                    adjudicada = anadirEntidadResolucionCentrosCEMPdsd2023(libro, hoja, ubic, entidad, numFila, codProcedimiento, adapt);
                                    if (adjudicada) {
                                        totalesAdjudicadas++;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            log.error("crearDocumentacionResolucionCentrosCEMPdsd2023 -  Error al procesar los datos de las ubicaciones/entidades", ex);
                        }

                        //Totales
                        numFila = numFila + 1;
                        anadirFilaTotalesResolucionCentrosCEMP2014dsd2017(libro, hoja, numFila, ubicacionesAmbito != null ? ubicacionesAmbito.size() : 0, totalesAdjudicadas, meLanbide32I18n, idioma, ano);
                        numFila = numFila + 4;
                        totalesAdjudicadas = 0;
                    }
                }
                numFila = 0;
            }
        } catch (Exception e) {
            log.error("Error procesando documento de ressolucion: " + e.getMessage(), e);
        }
        log.debug("crearDocumentacionResolucionCentrosCEMPdsd2017 - End()");
        return libro;

    }
    
    private static int anadirCabeceraResolucionCentrosCEMPdsd2025CriteriosSeleccion(HSSFWorkbook libro, HSSFSheet hoja, HSSFRow fila, int indiceColumna, List<OriCECriteriosEvaDTO> criteriosSeleccion) {
        try {
            if(criteriosSeleccion != null && !criteriosSeleccion.isEmpty()){
                // Crear el estilo base
                HSSFCellStyle estiloCeldaGREY_50_PERCENT = libro.createCellStyle();
                estiloCeldaGREY_50_PERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCeldaGREY_50_PERCENT.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                estiloCeldaGREY_50_PERCENT.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaGREY_50_PERCENT.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaGREY_50_PERCENT.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaGREY_50_PERCENT.setBorderLeft(HSSFCellStyle.BORDER_THICK);

                // Crear y configurar la fuente
                HSSFFont font = libro.createFont();
                font.setFontHeightInPoints((short) 8);
                estiloCeldaGREY_50_PERCENT.setFont(font);
                // Configurar orientación vertical (90 grados hacia arriba)
                estiloCeldaGREY_50_PERCENT.setRotation((short) 90);
                // Configurar alineación horizontal a la izquierda
                estiloCeldaGREY_50_PERCENT.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                // Configurar alineación vertical en la parte inferior
                estiloCeldaGREY_50_PERCENT.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
                estiloCeldaGREY_50_PERCENT.setWrapText(true);
                fila.setHeight((short) 3000);
                for(OriCECriteriosEvaDTO criteriosEvaDTO: criteriosSeleccion){
                    HSSFCell celda = fila.createCell(indiceColumna++);
                    celda.setCellValue(criteriosEvaDTO.getCodigoOrden() + ". "+ criteriosEvaDTO.getDescripcion_ES() + " \n " + criteriosEvaDTO.getDescripcion_EU());
                    celda.setCellStyle(estiloCeldaGREY_50_PERCENT);
                }
            }
        } catch (Exception e) {
            log.error("Error al recuperar la lista de criterios en la generacion del excel de resolucion de centros: " + e.getMessage(), e);
        }
        return indiceColumna;
    }

    private static void anadirCabecera1ResolucionCentrosCEMPdsd2023(HSSFWorkbook libro, HSSFSheet hoja, int numFila, int ultimaColumnaMergeValoracion, MeLanbide32I18n meLanbide32I18n, int idioma, List<OriCECriteriosEvaDTO> criteriosSeleccion)
    {
        HSSFRow fila = hoja.createRow(numFila);

        int indiceColumna = 3;
        hoja.addMergedRegion(new CellRangeAddress (numFila,numFila,indiceColumna,ultimaColumnaMergeValoracion));
        
        HSSFCellStyle estiloCeldaGREY_50_PERCENT = libro.createCellStyle();
        estiloCeldaGREY_50_PERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCeldaGREY_50_PERCENT.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
        estiloCeldaGREY_50_PERCENT.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCeldaGREY_50_PERCENT.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCeldaGREY_50_PERCENT.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCeldaGREY_50_PERCENT.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCeldaGREY_50_PERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaGREY_50_PERCENT.setWrapText(true);
        
        
        HSSFCell celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.valoracion"));
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);
  
        celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);
        
        celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);
        
        celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);

        if(criteriosSeleccion != null && !criteriosSeleccion.isEmpty()){
            for (int i = 0; i < criteriosSeleccion.size(); i++) {
                celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);
            }
        }

        /*celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);
        
        celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);

        celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);

        celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);

        celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaGREY_50_PERCENT);        
         */
    }

    private static void anadirCabecera2ResolucionCentrosCEMPdsd2023(HSSFWorkbook libro, HSSFSheet hoja, int numFila, MeLanbide32I18n meLanbide32I18n, int idioma, List<OriCECriteriosEvaDTO> criteriosSeleccion) {
        HSSFRow fila = hoja.createRow(numFila);
        fila.setHeight((short) 800);

        hoja.addMergedRegion(new CellRangeAddress(numFila, numFila, 0, 1));

        HSSFCellStyle estiloCeldaPALE_BLUE = libro.createCellStyle();
        estiloCeldaPALE_BLUE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCeldaPALE_BLUE.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        estiloCeldaPALE_BLUE.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCeldaPALE_BLUE.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCeldaPALE_BLUE.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCeldaPALE_BLUE.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCeldaPALE_BLUE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaPALE_BLUE.setWrapText(true);
        
        HSSFCellStyle estiloCeldaGREY_25_PERCENT = libro.createCellStyle();
        estiloCeldaGREY_25_PERCENT.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCeldaGREY_25_PERCENT.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloCeldaGREY_25_PERCENT.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCeldaGREY_25_PERCENT.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCeldaGREY_25_PERCENT.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCeldaGREY_25_PERCENT.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCeldaGREY_25_PERCENT.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaGREY_25_PERCENT.setWrapText(true);
        
        HSSFCellStyle estiloCeldaLIME = libro.createCellStyle();
        estiloCeldaLIME.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCeldaLIME.setFillForegroundColor(HSSFColor.LIME.index);
        estiloCeldaLIME.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCeldaLIME.setBorderTop(HSSFCellStyle.BORDER_THICK);
        estiloCeldaLIME.setBorderRight(HSSFCellStyle.BORDER_THICK);
        estiloCeldaLIME.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCeldaLIME.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaLIME.setWrapText(true);
        
        int indiceColumna = 0;
        HSSFCell celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.entColaboradoras"));
        celda.setCellStyle(estiloCeldaPALE_BLUE);

        celda = fila.createCell(indiceColumna++);
        celda.setCellStyle(estiloCeldaPALE_BLUE);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.centroEspecial"));
        celda.setCellStyle(estiloCeldaPALE_BLUE);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.admLocal"));
        celda.setCellStyle(estiloCeldaGREY_25_PERCENT);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.supramun"));
        celda.setCellStyle(estiloCeldaGREY_25_PERCENT);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.especialidad.personas.discapacidad"));
        celda.setCellStyle(estiloCeldaGREY_25_PERCENT);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.especialidad.colectivos.riegos.exclusion"));
        celda.setCellStyle(estiloCeldaGREY_25_PERCENT);

        indiceColumna=anadirCabeceraResolucionCentrosCEMPdsd2025CriteriosSeleccion(libro, hoja, fila, indiceColumna,criteriosSeleccion);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionHoras.puntUbicacion"));
        celda.setCellStyle(estiloCeldaGREY_25_PERCENT);

        
        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.adjudicado"));
        celda.setCellStyle(estiloCeldaLIME);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.motivodenegacion"));
        celda.setCellStyle(estiloCeldaGREY_25_PERCENT);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.estaTramite.resolucionProvisional"));
        celda.setCellStyle(estiloCeldaGREY_25_PERCENT);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(meLanbide32I18n.getMensaje(idioma, "doc.resolucionCentros.fecha.registro"));
        celda.setCellStyle(estiloCeldaGREY_25_PERCENT);
    }

    private static boolean anadirEntidadResolucionCentrosCEMPdsd2023(HSSFWorkbook libro, HSSFSheet hoja, CeUbicacionVO ubic, EntidadVO entidad, int numFila, String codProcedimiento, AdaptadorSQLBD adapt) {
        
        HSSFRow fila = hoja.createRow(numFila);
        
        HSSFCellStyle estiloCeldaResolucionCNE = libro.createCellStyle();
        estiloCeldaResolucionCNE.setFillForegroundColor(HSSFColor.WHITE.index);
        
        HSSFCellStyle estiloCeldaResolucionCEAdj = libro.createCellStyle();
        estiloCeldaResolucionCEAdj.setFillForegroundColor(HSSFColor.AQUA.index);
        
        String adjudicada = null;
        boolean estaAdj = false;
        boolean estaDen = false;

        try {
            if(ubic!=null)
                adjudicada = MeLanbide32Manager.getInstance().getUbicacionAdjudicadaCEMP2014dsd2017(ubic.getOriCeUbicCod(), ubic.getOrdenAdjudicado(), adapt);
            if (adjudicada != null && adjudicada.startsWith(ConstantesMeLanbide32.ADJUDICADO)) {
                estaAdj = true;
            } else {
                if (adjudicada == null || adjudicada.isEmpty()) {
                    estaDen = true;
                    adjudicada = ConstantesMeLanbide32.DENEGADO;
                }
            }
        } catch (Exception ex) {
            log.error("Error al validar si el centro es o no adjudicado", ex);
        }
        
        HSSFCellStyle estiloCeldaResolucion = (ubic!=null && ubic.getOriCeEspecial() != null
                && ubic.getOriCeEspecial().equalsIgnoreCase("S") && estaAdj  ? estiloCeldaResolucionCEAdj
                : estiloCeldaResolucionCNE);
        estiloCeldaResolucion.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        estiloCeldaResolucion.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaResolucion.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloCeldaResolucion.setWrapText(true);
        
        int indiceColumna = 0;
        HSSFCell celda = fila.createCell(indiceColumna++);
        celda.setCellValue(entidad != null ? entidad.getExtNum() : "");
        celda.setCellStyle(estiloCeldaResolucion);

        celda = fila.createCell(indiceColumna++);
        String nomEnt = entidad != null && entidad.getOriEntNom() != null ? entidad.getOriEntNom() : "";
        nomEnt += nomEnt != null && !nomEnt.equals("") ? (ubic.getOriCeDireccion() != null && !ubic.getOriCeDireccion().equals("") ? " - " : "") : "";
        nomEnt += ubic != null && ubic.getOriCeDireccion() != null && !ubic.getOriCeDireccion().equals("") ? ubic.getOriCeDireccion() : "";
        celda.setCellValue(nomEnt);
        celda.setCellStyle(estiloCeldaResolucion);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue( ubic != null && ubic.getOriCeEspecial() != null ? ubic.getOriCeEspecial() : "");
        celda.setCellStyle(estiloCeldaResolucion);
        
        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(entidad != null && entidad.getOriEntAdmLocalVal() != null ? entidad.getOriEntAdmLocalVal() : "");
        celda.setCellStyle(estiloCeldaResolucion);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(entidad != null && entidad.getOriEntSupramunVal() != null ? entidad.getOriEntSupramunVal() : "");
        celda.setCellStyle(estiloCeldaResolucion);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(entidad != null && entidad.getTipAtenPerDiscapaVAL() != null ? entidad.getTipAtenPerDiscapaVAL() : "");
        celda.setCellStyle(estiloCeldaResolucion);

        celda = fila.createCell(indiceColumna++);
        celda.setCellValue(entidad != null && entidad.getTipAtenColRiesExcVAL() != null ? entidad.getTipAtenColRiesExcVAL() : "");
        celda.setCellStyle(estiloCeldaResolucion);

        try {
            List<OriCECriteriosEvaDTO> criteriosSeleccion = meLanbide32ManagerCriteriosSeleccionCE.obtenerCriteriosPuntuacionPorUbicacion(ubic,adapt);
            if(criteriosSeleccion!=null && !criteriosSeleccion.isEmpty()){
                for(OriCECriteriosEvaDTO criteriosEvaDTO: criteriosSeleccion){
                    celda = fila.createCell(indiceColumna++);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    celda.setCellValue(criteriosEvaDTO.getPuntuacionCentro() > 0 ? String.valueOf(criteriosEvaDTO.getPuntuacionCentro()) : "");
                    celda.setCellStyle(estiloCeldaResolucion);
                }
            }
        } catch (Exception e) {
            log.error("Error al recuperar la lista de criterios en la generacion del excel de resolucion de centros: " + e.getMessage(), e);
        }

        celda = fila.createCell(indiceColumna++);
        int puntuacionUbicacion = 0;
        if (entidad != null) {
            try {
                puntuacionUbicacion = MeLanbide32Manager.getInstance().getPuntuacionUbicacionCE(ubic, adapt);
            } catch (Exception ex) {
                log.error("Error al recuperar puntuacion Ubicacion", ex);
            }
        }
        celda.setCellValue(puntuacionUbicacion);
        celda.setCellStyle(estiloCeldaResolucion);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

        celda = fila.createCell(indiceColumna++);
        //celda.setCellValue(adjudicada != null && !adjudicada.equals("") ? adjudicada : ConstantesMeLanbide32.NO);
        celda.setCellValue(adjudicada);
        celda.setCellStyle(estiloCeldaResolucion);

        celda = fila.createCell(indiceColumna++);
        String motivoDenegacion = null;

        try {
            if (estaDen) {
                motivoDenegacion = MeLanbide32Manager.getInstance().getMotivoDenegacionCEMP2014_ExcelResol(entidad, adapt);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error al recueprar el motivo de denegacion cuando generamos el excel de resolucion en CEMP Procesos" + ex.getMessage());
        }
        celda.setCellValue(motivoDenegacion);
        celda.setCellStyle(estiloCeldaResolucion);

        celda = fila.createCell(indiceColumna++);
        Boolean estaResolProvisionalTramite = null;
        try {
            estaResolProvisionalTramite = MeLanbide32Manager.getInstance().expteEstaTramiteResolProvisional((entidad != null ? entidad.getExtNum():""), adapt);
        } catch (Exception e) {
            log.error("Error al comprobar si el expediente esta en resolucion provisional " + (entidad != null ? entidad.getExtNum():""), e);
        }
        String valorCompruebaTramite = "";
        if (estaResolProvisionalTramite != null) {
            if (estaResolProvisionalTramite) {
                valorCompruebaTramite = "S";
            } else {
                valorCompruebaTramite = "N";
            }
        }
        celda.setCellValue(valorCompruebaTramite);
        celda.setCellStyle(estiloCeldaResolucion);

        celda = fila.createCell(indiceColumna++);
        String fechaRegistro = "";
        try {
            fechaRegistro = MeLanbide32Manager.getInstance().getFechaRegistroExpediente(entidad.getExtNum(), codProcedimiento, adapt);
        } catch (Exception e) {
            log.error("Error al comprobar si el expediente esta en resolucion provisional " + entidad.getExtNum(), e);
        }
        celda.setCellValue(fechaRegistro);
        celda.setCellStyle(estiloCeldaResolucion);

        return estaAdj;
    }
    
    
    
}
