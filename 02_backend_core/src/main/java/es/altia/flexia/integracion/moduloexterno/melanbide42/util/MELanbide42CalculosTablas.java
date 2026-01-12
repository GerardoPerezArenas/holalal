/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide42.util;

import es.altia.flexia.integracion.moduloexterno.melanbide42.dao.MELanbide42DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.manager.MELanbide42Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42FilaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42ModuloVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42TablaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42XMLAltaExpediente;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author mikel
 */
public class MELanbide42CalculosTablas {
    
        //Logger
    private static final Logger log = LogManager.getLogger(MELanbide42CalculosTablas.class);

    private MELanbide42CalculosTablas (){
        // No instanciable
    }
    
    /**
     * Realiza los m�lculos para la tabla MELANBIDE01_PERIODO
     * @param expediente
     * @param tablaCal
     * @param tablaPer
     * @param adaptSQL 
     * @param numExp 
     * @param codOrganizacion 
     * @throws java.text.ParseException 
     */
    public static void calculo_MELANBIDE01_PERIODO(MELanbide42XMLAltaExpediente expediente, 
            MELanbide42TablaVO tablaCal, MELanbide42TablaVO tablaPer, AdaptadorSQLBD adaptSQL, String numExp, int codOrganizacion) throws Exception{
        float porcSubvenc = 0f;
        float reducPersSust = 0f;
        float jornPerSust = 0f;
        float jornPersCont = 0f;
        Integer numDias = 0;
        float gasto = 0f;
        float baseCotizacion = 0f;
        float bonificacion = 0f;
        float factorCorreccion = 0f;
        Date fechaInicio = null;
        Date fechaFin = null;
        String nextValCal = "";
        String nextValPer = "";
        float totalDes = 0f;
        String seqName = "";
        Connection con = adaptSQL.getConnection();
        try
        {
            MELanbide42DAO dao = MELanbide42DAO.getInstance();
            String ejercicio = "";
            String proc = "";
            String [] num = numExp.split("/");
            ejercicio = num[0];
            proc = num[1];
            
            log.info("En funci�n calculo_MELANBIDE01_PERIODO");
            log.info("Secuencia calculo, valor: " + nextValCal);
           
            String descuento = MELanbide42Properties.getProperty("MELANBIDE01/PARAMETROS/PORCENTAJE_DESCUENTO_SOBRE_TOTAL_GASTOS").toString();
            float improteSub = 0f;
            try{
                 seqName = MELanbide42Properties.getProperty("MELANBIDE01/MELANBIDE01_DATOS_CALCULO/seqname");
                 nextValCal = dao.getSeqNextValue(seqName, con);
            }catch(Exception ex){
                log.error("Se ha producido un error en la funcion calculo_MELANBIDE01_DATOS_CALCULO", ex);
            }
                 improteSub = calculo_MELANBIDE01_DATOS_CALCULO(expediente,tablaPer);
                log.info("Importe subvencion: "+improteSub);
            if(expediente.getCamposVariables().get("REDUCPERSSUST") != null && !expediente.getCamposVariables().get("REDUCPERSSUST").equals(""))
                reducPersSust = Float.parseFloat(expediente.getCamposVariables().get("REDUCPERSSUST").replace(",", "."));
            if(expediente.getCamposVariables().get("JORNPERSSUST") != null && !expediente.getCamposVariables().get("JORNPERSSUST").equals(""))
                jornPerSust = Float.parseFloat(expediente.getCamposVariables().get("JORNPERSSUST").replace(",", "."));
            if(expediente.getCamposVariables().get("JORNPERSCONT") != null && !expediente.getCamposVariables().get("JORNPERSCONT").equals(""))
                jornPersCont = Float.parseFloat(expediente.getCamposVariables().get("JORNPERSCONT").replace(",", "."));
            if(jornPersCont > 0 && reducPersSust > 0 && jornPerSust > 0)
                porcSubvenc = (reducPersSust * jornPerSust) / jornPersCont;
            factorCorreccion = Float.parseFloat(MELanbide42Properties.getProperty("MELANBIDE01/PARAMETROS/FACTOR_CORRECCION_GASTO"));  
            //MELANBIDE01_DATOS_CALCULO
            //for (MELanbide42FilaVO f1 : tablaCal.getFilas()){
            MELanbide42TablaVO tabla = new MELanbide42TablaVO("MELANBIDE01_DATOS_CALCULO", new MELanbide42ModuloVO("MELANBIDE01"));
            improteSub = (improteSub * (factorCorreccion/100) * (porcSubvenc/100));
        
            MELanbide42FilaVO f1 = new MELanbide42FilaVO(tabla);
            f1.addCampo("ID", nextValCal);
            f1.addCampo("COD_MUNICIPIO", String.valueOf(codOrganizacion));
            f1.addCampo("NUM_EXPEDIENTE", numExp);
            f1.addCampo("EJERCICIO", ejercicio);
            f1.addCampo("COD_PROCEDIMIENTO", proc);
            f1.addCampo("MODULO", "/MODULO_INTEGRACION/");
            f1.addCampo("DESCUENTO", descuento);
            f1.addCampo("IMPORTE_SUBVENC", String.valueOf(improteSub).replace(".", ","));
            if(descuento != null)
                totalDes = improteSub * (1 - (Float.parseFloat(descuento)/100));
            f1.addCampo("TOTAL_CON_DESCUENTO", String.valueOf(totalDes).replace(".", ","));
            //}
            tabla.addFila(0, f1);
            expediente.getListaModulos().get(0).addTabla(0, tabla);
            
            log.info("Anadida tabla MELANBIDE01_DATOS_CALCULO");
            
            //MELANBIDE01_PERIODO
            if(tablaPer != null){
                for (MELanbide42FilaVO f2 : tablaPer.getFilas()){
                    try
                    {
                    seqName = MELanbide42Properties.getProperty("MELANBIDE01/MELANBIDE01_PERIODO/seqname");
                    nextValPer = dao.getSeqNextValue(seqName, con);
                    }
                    catch(Exception ex){
                        log.error("Se ha producido un error al recoger la secuencia", ex);
                    }
                    log.info("Secuencia periodo, valor: " + nextValPer);
                    f2.addCampo("ID", nextValPer);
                    f2.addCampo("ID_DATOS_CALCULO", nextValCal);
                    // PORC_SUBVENC = ((REDUC_PERS_SUST) X (JORN_PERS_SUST)) / (JORN_PERS_CONT)
                    
                    f2.addCampo("PORC_SUBVENC", String.valueOf(porcSubvenc).replace(".", ","));

                    // NUM_DIAS = Diferencia en d�as entre Fecha desde y fecha hasta
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    fechaInicio = sdf.parse(f2.getCampos().get("FECHA_INICIO"));
                    fechaFin = sdf.parse(f2.getCampos().get("FECHA_FIN"));
                    GregorianCalendar gc1 = new GregorianCalendar();
                    GregorianCalendar gc2 = new GregorianCalendar();
                    gc1.setTime(fechaInicio);
                    gc2.setTime(fechaFin);
                    //long duration = (gc2.getTimeInMillis() - gc1.getTimeInMillis()) / (1000*3600*24);
                    // 2018/02/15 Cogeremos lo que venga en el XML Pintamos en trazas si el valro es diferente.
                    Integer duration = (gc2.get(GregorianCalendar.DAY_OF_YEAR) - gc1.get(GregorianCalendar.DAY_OF_YEAR));
                    numDias = duration + 1;
                    //f2.addCampo("NUM_DIAS", String.valueOf(numDias));
                    String numDiasXML = f2.getCampos().get("NUM_DIAS");
                    if (numDiasXML != null) {
                        if (!numDiasXML.equalsIgnoreCase(String.valueOf(numDias))) {
                            log.info("Numero de Días incorrecto. Intervalo: " + f2.getCampos().get("FECHA_INICIO") + " - " + f2.getCampos().get("FECHA_FIN") + " - XML : " + numDiasXML + " - Calculo : " + numDias);
                        }
                    } else {
                        log.info("Campo  NUM_DIAS viene a null en el XML");
                    }

                    // GASTO = (BASE_COTIZACION - BONIFICACIÓN) X 23,60 X PORC_SUBVENC
                    if(f2.getCampos().get("BASE_COTIZACION") != null)
                        baseCotizacion = Float.parseFloat(f2.getCampos().get("BASE_COTIZACION").replace(",", "."));
                    bonificacion = Float.parseFloat(MELanbide42Properties.getProperty("MELANBIDE01/PARAMETROS/BONIFICACION_GASTO"));
                    
                    gasto = (baseCotizacion * (factorCorreccion/100) * (porcSubvenc/100));
                    f2.addCampo("GASTO", String.valueOf(gasto).replace(".", ","));

                    //FECHA_INICIO Y FECHA_FIN
                    f2.addCampo("FECHA_INICIO", sdf.format(fechaInicio));
                    f2.addCampo("FECHA_FIN", sdf.format(fechaFin));

                    //BASE_COTIZACION
                    f2.addCampo("BASE_COTIZACION", String.valueOf(baseCotizacion).replace(".", ","));
                    f2.addCampo("REDUC_PERS_SUST", String.valueOf(reducPersSust).replace(".", ","));
                    f2.addCampo("JORN_PERS_SUST", String.valueOf(jornPerSust).replace(".", ","));
                    f2.addCampo("JORN_PERS_CONT", String.valueOf(jornPersCont).replace(".", ","));
                    f2.addCampo("BONIFICACION", String.valueOf(bonificacion).replace(".", ","));
                    
                    log.info("A�adida tabla MELANBIDE01_PERIODO");
                }
            }
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error en calculo_MELANBIDE01_PERIODO", ex);
            
//            String error = "Error en la funcion calculo_MELANBIDE01_PERIODO: " + ex.toString();
//            
//            ErrorBean errorB = new ErrorBean();
//            errorB.setIdError("TELEMATICO_05");
//            errorB.setMensajeError("Error al realizar los m�lculos de MELANBIDE01");
//            errorB.setSituacion("calculo_MELANBIDE01_PERIODO");
//
//            MELanbide42Manager.grabarError(errorB, error, error, numExp);
            throw ex;
        }finally{
            adaptSQL.devolverConexion(con);
        }
    }
    
    
    
    
    /**
     * Realiza los m�lculos para la tabla MELANBIDE01_PERIODO
     * @param expediente
     * @param tabla 
     * @return  
     */
    /*public static void calculo_MELANBIDE01_DATOS_CALCULO(MELanbide42XMLAltaExpediente expediente, MELanbide42TablaVO tabla) throws ParseException{
        float importeSubvenc = 0f;
        
        for (MELanbide42FilaVO f : tabla.getFilas()){
            // PORC_SUBVENC = Suma de todos los importes calculados en la tabla MELANBIDE01_PERIODO 
            importeSubvenc = Integer.parseInt(f.getCampos().get("GASTO"));
            f.addCampo("IMPORTE_SUBVENC", String.valueOf(importeSubvenc));
        }
    }*/
    public static float calculo_MELANBIDE01_DATOS_CALCULO(MELanbide42XMLAltaExpediente expediente, MELanbide42TablaVO tabla) throws ParseException{
        float importeSubvenc = 0f;
        if(tabla!=null){
           for (MELanbide42FilaVO f : tabla.getFilas()){
            // PORC_SUBVENC = Suma de todos los importes calculados en la tabla MELANBIDE01_PERIODO 
            if(f.getCampos().get("BASE_COTIZACION")!= null)
                importeSubvenc += Float.parseFloat(f.getCampos().get("BASE_COTIZACION").replace(",", "."));
           // f.addCampo("IMPORTE_SUBVENC", String.valueOf(importeSubvenc));
        } 
        }
        return importeSubvenc;
    }
    
    
    /**
     * Realiza los m�lculos para la tabla MELANBIDE41_ESPECIALIDADES Y DISPONRECURSOS
     * @param expediente
     * @param tablaEsp
     * @param tablaDis
     * @param tablaCap
     * @param tablaDot
     * @param tablaMat
     * @param tablaIdent
     * @param adaptSQL
     * @throws ParseException
     * @throws MELanbide42Exception
     * @throws BDException
     * @throws java.sql.SQLException
     */
    public static void calculo_MELANBIDE41_ESPECIALIDADES(MELanbide42XMLAltaExpediente expediente, 
            MELanbide42TablaVO tablaEsp, MELanbide42TablaVO tablaDis,
            MELanbide42TablaVO tablaCap, MELanbide42TablaVO tablaDot, 
            MELanbide42TablaVO tablaMat, MELanbide42TablaVO tablaIdent,
            AdaptadorSQLBD adaptSQL) throws ParseException, MELanbide42Exception, BDException, SQLException{
        if(log.isDebugEnabled()) log.debug("calculo_MELANBIDE41_ESPECIALIDADES() : BEGIN");

        MELanbide42DAO dao = MELanbide42DAO.getInstance();
        String seqName = MELanbide42Properties.getProperty("MELANBIDE41/MELANBIDE41_ESPECIALIDADES/seqname");
        Connection con = adaptSQL.getConnection();
        // Recorro las filas de MELANBIDE41_ESPECIALIDADES para insertar el ID autogenerado via secuencia
        try {
            for (MELanbide42FilaVO f : tablaEsp.getFilas()) {
                try {
                    String nextval;
//            if (5 == 5) {
//                log.info("SEQ NEXT VAL TRUCADOOOO");
//                nextval = String.valueOf(System.currentTimeMillis());
//            } else {
                    nextval = dao.getSeqNextValue(seqName, con);
//            }
                    f.addCampo("ID", nextval);

                    // Una vez que se tiene el ID, hay que marcar la fila de DISPONRECURSOS que cumple:
                    // MELANBIDE41_ESPECIALIDADES.ESP_CODCP = MELANBIDE41_DISPONRECURSOS.DRE_CODCP
                    // En esa fila, el valor de ID_ESPCOL es el valor del ID de ESPECIALIDADES
                    String espCodcp = f.getCampos().get("ESP_CODCP");
                    if (tablaDis != null) {
                        ArrayList<MELanbide42FilaVO> filasRecursos = tablaDis.getFilasByFieldValue("DRE_CODCP", espCodcp);
                        // Tengo las filas de DISPONRECURSOS para actualizarle el ID_ESPSOL
                        for (MELanbide42FilaVO f1 : filasRecursos) {
                            f1.addCampo("ID_ESPSOL", nextval);
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de DISPONRECURSOS con DRE_CODCP=" + espCodcp);
                        }
                    } else {
                        log.debug("No hay tabla DISPONRECURSOS");
                    }

                    // Hay que hacer lo mismo para
                    // MELANBIDE41_CAPACIDADINST.CAIN_CODCP = MELANBIDE41_ESPECIALIDADES.ESP_CODCP
                    // MELANBIDE41_DOTACION.DOT_CODCP = MELANBIDE41_ESPECIALIDADES.ESP_CODCP
                    // MELANBIDE41_MATERIALCONSU.MAC_CODCP = MELANBIDE41_ESPECIALIDADES.ESP_CODCP
                    // La diferencia con estos es que ADEMAS, en el XML el campo indicado es s�lo informativo
                    // por tanto, después de a�adir el ID_ESPSOL, hay que borrar el anterior
                    // Algunas tablas, adem�s, pueden tener N filas relacionadas.
                    if (tablaCap != null) {
                        ArrayList<MELanbide42FilaVO> filasCapacidad = tablaCap.getFilasByFieldValue("CAIN_CODCP", espCodcp);
                        for (MELanbide42FilaVO f2 : filasCapacidad) {
                            f2.addCampo("ID_ESPSOL", nextval);
                            f2.removeCampo("CAIN_CODCP");
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de CAPACIDADINST con CAIN_CODCP=" + espCodcp + " y eliminado el CAIN_CODCP");
                        }
                    } else {
                        log.debug("No hay tabla CAPACIDADINST");
                    }

                    if (tablaDot != null) {
                        ArrayList<MELanbide42FilaVO> filasDotacion = tablaDot.getFilasByFieldValue("DOT_CODCP", espCodcp);
                        for (MELanbide42FilaVO f3 : filasDotacion) {
                            f3.addCampo("ID_ESPSOL", nextval);
                            f3.removeCampo("DOT_CODCP");
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de DOTACION con DOT_CODCP=" + espCodcp + " y eliminado el DOT_CODCP");
                        }
                    } else {
                        log.debug("No hay tabla DOTACION");
                    }

                    if (tablaMat != null) {
                        ArrayList<MELanbide42FilaVO> filasMaterial = tablaMat.getFilasByFieldValue("MAC_CODCP", espCodcp);
                        for (MELanbide42FilaVO f4 : filasMaterial) {
                            f4.addCampo("ID_ESPSOL", nextval);
                            f4.removeCampo("MAC_CODCP");
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de MATERIALCONSU con MAC_CODCP=" + espCodcp + " y eliminado el MAC_CODCP");
                        }
                    } else {
                        log.debug("No hay tabla MATERIALCONSU");
                    }

                    if (tablaIdent != null) {
                        ArrayList<MELanbide42FilaVO> filasIdentificacion = tablaIdent.getFilasByFieldValue("IDE_CODESP", espCodcp);
                        for (MELanbide42FilaVO f4 : filasIdentificacion) {
                            f4.addCampo("ID_ESPSOL", nextval);
                            //f4.removeCampo("IDE_CODESP"); <<-- En esta tabla NO se elimina el campo
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de IDENT con IDE_CODESP=" + espCodcp);
                        }
                    } else {
                        log.debug("No hay tabla IDENT");
                    }
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(MELanbide42CalculosTablas.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            // Ahora quito los campos que no sirven de 

        } catch (Exception ex) {
            log.error("Error cargando datos M41 " + ex.getMessage(),ex);
        }finally{
            adaptSQL.devolverConexion(con);
        }
        
        

        if(log.isDebugEnabled()) log.debug("calculo_MELANBIDE41_ESPECIALIDADES() : END");
    }

    /**
     * Realiza los m�lculos para la tabla MELANBIDE50_ESPECIALIDADES Y DISPONRECURSOS
     * @param expediente
     * @param tablaEsp
     * @param tablaDis
     * @param tablaCap
     * @param tablaDot
     * @param tablaMat
     * @param tablaIdent
     * @param tablaEspac
     * @param adaptSQL
     * @throws ParseException
     * @throws MELanbide42Exception
     * @throws BDException
     */
    public static void calculo_MELANBIDE50_ESPECIALIDADES(MELanbide42XMLAltaExpediente expediente,
            MELanbide42TablaVO tablaEsp, MELanbide42TablaVO tablaDis,
            MELanbide42TablaVO tablaCap, MELanbide42TablaVO tablaDot,
            MELanbide42TablaVO tablaMat, MELanbide42TablaVO tablaIdent,
            MELanbide42TablaVO tablaEspac, AdaptadorSQLBD adaptSQL) throws ParseException, MELanbide42Exception, BDException {
        log.info("calculo_MELANBIDE50_ESPECIALIDADES() : BEGIN");
        Connection con = adaptSQL.getConnection();
        try {
            
            MELanbide42DAO dao = MELanbide42DAO.getInstance();
            String seqName = MELanbide42Properties.getProperty("MELANBIDE50/MELANBIDE50_ESPECIALIDADES/seqname");
            // Recorro las filas de MELANBIDE50_ESPECIALIDADES para insertar el ID autogenerado via secuencia
            for (MELanbide42FilaVO f : tablaEsp.getFilas()) {
                try {
                    String nextval;
                    //if (5 == 5) {
                    //    log.info("SEQ NEXT VAL TRUCADOOOO");
                    //    nextval = String.valueOf(System.currentTimeMillis());
                    //} else {
                    nextval = dao.getSeqNextValue(seqName, con);
                    //}
                    f.addCampo("ID", nextval);

                    // Una vez que se tiene el ID, hay que marcar la fila de DISPONRECURSOS que cumple:
                    // MELANBIDE50_ESPECIALIDADES.ESP_CODCP = MELANBIDE50_DISPONRECURSOS.DRE_CODCP
                    // En esa fila, el valor de ID_ESPCOL es el valor del ID de ESPECIALIDADES
                    String espCodcp = f.getCampos().get("ESP_CODCP");
                    if (tablaDis != null) {
                        ArrayList<MELanbide42FilaVO> filasRecursos = tablaDis.getFilasByFieldValue("DRE_CODCP", espCodcp);
                        // Tengo las filas de DISPONRECURSOS para actualizarle el ID_ESPSOL
                        for (MELanbide42FilaVO f1 : filasRecursos) {
                            f1.addCampo("ID_ESPSOL", nextval);
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de DIS con DRE_CODCP=" + espCodcp);
                        }

                    } else {
                        log.debug("No hay tabla DIS");
                    }

                    // Hay que hacer lo mismo para
                    // MELANBIDE50_CAPACIDADINST.CAIN_CODCP = MELANBIDE50_ESPECIALIDADES.ESP_CODCP
                    // MELANBIDE50_DOTACION.DOT_CODCP = MELANBIDE50_ESPECIALIDADES.ESP_CODCP
                    // MELANBIDE50_MATERIALCONSU.MAC_CODCP = MELANBIDE50_ESPECIALIDADES.ESP_CODCP
// MELANBIDE50_ESPACIOS.NESP_CODESP = MELANBIDE50_ESPECIALIDADES.ESP_CODCP
                    // La diferencia con estos es que ADEMAS, en el XML el campo indicado es s�lo informativo
                    // por tanto, despues de a�adir el ID_ESPSOL, hay que borrar el anterior
                    // Algunas tablas, adem�s, pueden tener N filas relacionadas.
                    if (tablaCap != null) {
                        ArrayList<MELanbide42FilaVO> filasCapacidad = tablaCap.getFilasByFieldValue("CAIN_CODCP", espCodcp);
                        for (MELanbide42FilaVO f2 : filasCapacidad) {
                            f2.addCampo("ID_ESPSOL", nextval);
                            f2.removeCampo("CAIN_CODCP");
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de CAPACIDADINST con CAIN_CODCP=" + espCodcp + " y eliminado el campo CAIN_CODCP");
                        }
                    } else {
                        log.debug("No existe tabla CAPACIDADINST");
                    }

                    if (tablaDot != null) {
                        ArrayList<MELanbide42FilaVO> filasDotacion = tablaDot.getFilasByFieldValue("DOT_CODCP", espCodcp);
                        for (MELanbide42FilaVO f3 : filasDotacion) {
                            f3.addCampo("ID_ESPSOL", nextval);
                            f3.removeCampo("DOT_CODCP");
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de DOTACION con DOT_CODCP=" + espCodcp + " y eliminado el campo DOT_CODCP");
                        }
                    } else {
                        log.debug("No existe tabla DOTACION");
                    }

                    if (tablaMat != null) {
                        ArrayList<MELanbide42FilaVO> filasMaterial = tablaMat.getFilasByFieldValue("MAC_CODCP", espCodcp);
                        for (MELanbide42FilaVO f4 : filasMaterial) {
                            f4.addCampo("ID_ESPSOL", nextval);
                            f4.removeCampo("MAC_CODCP");
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de MATERIALCONSU con MAC_CODCP=" + espCodcp + " y eliminado el campo MAC_CODCP");
                        }
                    } else {
                        log.debug("No existe tabla MATERIALCONSU");
                    }

                    if (tablaIdent != null) {
                        ArrayList<MELanbide42FilaVO> filasIdentificacion = tablaIdent.getFilasByFieldValue("IDE_CODESP", espCodcp);
                        for (MELanbide42FilaVO f4 : filasIdentificacion) {
                            f4.addCampo("ID_ESPSOL", nextval);
                            //f4.removeCampo("IDE_CODESP"); <<-- En esta tabla NO se elimina el campo
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de IDENT con IDE_CODESP=" + espCodcp);
                        }
                    } else {
                        log.debug("No existe tabla IDENT");
                    }

                    if (tablaEspac != null) {
                        ArrayList<MELanbide42FilaVO> filasEspacio = tablaEspac.getFilasByFieldValue("NESP_CODESP", espCodcp);
                        for (MELanbide42FilaVO f4 : filasEspacio) {
                            f4.addCampo("ID_ESPSOL", nextval);
                            f4.removeCampo("NESP_CODESP");
                        }
                        if (log.isDebugEnabled()) {
                            log.debug("A�adido ID_ESPSOL=" + nextval + " a todas las filas de ESPACIOS con NESP_CODESP=" + espCodcp);
                        }
                    } else {
                        log.debug("No existe tabla ESPACIOS");
                    }
                } catch (SQLException ex) {
                    log.error(MELanbide42CalculosTablas.class.getName() + " >> Error cargar M50 : " + ex.getMessage(), ex);
                }

            }
        } catch (Exception ex) {
            log.error("Error al cargar M50 " +ex.getMessage() , ex);
        }finally{
            adaptSQL.devolverConexion(con);
        }
        log.debug("calculo_MELANBIDE50_ESPECIALIDADES() : END");
    }
    
    
    public static void prepararDatosORI14Mod47(MELanbide42XMLAltaExpediente xmlExpediente
            , MELanbide42TablaVO tablaEntidad   //, MELanbide42TablaVO tablaAsociacionDatosEntidad
            , MELanbide42TablaVO tablaAsociacionListaEnts
            , MELanbide42TablaVO tablaTrayectoria, MELanbide42TablaVO tablaUbicaciones
            , MELanbide42TablaVO tablaAmbitosSoli
            , MELanbide42TablaVO tablaOtrosProgramas , MELanbide42TablaVO tablaActividades, MELanbide42TablaVO tablaCertificadosCalidad
            ,MELanbide42ModuloVO modulo
            ,AdaptadorSQLBD adaptSQL,String numExp, String codOrg) throws MELanbide42Exception, Exception {
        
        log.info("prepararDatosORI14Mod47() : BEGIN");

        MELanbide42DAO dao = MELanbide42DAO.getInstance();
        MELanbide42Manager manager = new MELanbide42Manager();
        String seqName = MELanbide42Properties.getProperty("MELANBIDE47/ORI14_ENTIDAD/seqname");
        String ejercicio = MELanbide42GeneralUtils.getAnioFromNumExpediente(numExp); //numExp!=null && numExp!="" ? numExp.substring(0,4) :"";
        Connection con = adaptSQL.getConnection();
        try {
            Map<String, String> datosTerceroInnerExtTable = manager.getTerceroxNumExpxRol(numExp, 1, con);
            // Hay que preparar los datos de la tabla Entidad Generando su Clave para luego preparar los datos
            // de  la tabla asociacion con los datos de la misma entidad. 
            for (MELanbide42FilaVO f : tablaEntidad.getFilas()) {
                try {
                    String nextval;
                    nextval = dao.getSeqNextValue(seqName, con);
                    f.addCampo("ORI_ENT_COD", nextval);
                    f.addCampo("EXT_MUN", codOrg);
                    f.addCampo("EXT_EJE", ejercicio);
                    f.addCampo("EXT_NUM", numExp);
                    f.addCampo("EXT_TER", datosTerceroInnerExtTable.get("EXT_TER"));
                    f.addCampo("EXT_NVR", datosTerceroInnerExtTable.get("EXT_NVR"));
                    f.addCampo("ORI_ENT_NOM", datosTerceroInnerExtTable.get("HTE_NOC"));
                    f.addCampo("ORI_ENT_CIF", datosTerceroInnerExtTable.get("HTE_DOC"));
                    // Actualizamos el valor de los campos SI/NO
                    f.getCampos().put("ORI_ENT_ACEPTA_MAS", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_ACEPTA_MAS")));
                    f.getCampos().put("ORI_ENT_SEGULOCALES_AMB", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_SEGULOCALES_AMB")));
                    //// Actualizamos el valor de los campos SI/NO Copiamos los datos que nos han indicado en el nodo ORI14_ENTIDAD del XML
                    f.getCampos().put("ORI_ENT_SUPRAMUN", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_SUPRAMUN")));
                    f.getCampos().put("ORI_ENT_ADMLOCAL", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_ADMLOCAL")));
                    f.getCampos().put("ORI_EXP_CENTROFP_PUB", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_EXP_CENTROFP_PUB")));
                    f.getCampos().put("ORI_EXP_CENTROFP_PRIV", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_EXP_CENTROFP_PRIV")));
                    f.getCampos().put("ORI_ENT_AGENCOLOC", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_AGENCOLOC")));
                    f.getCampos().put("ORI_ENT_SINANIMOLUCRO", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_SINANIMOLUCRO")));
                    f.getCampos().put("ORI_ENT_PLAN_IGUALDAD_REG", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_PLAN_IGUALDAD_REG")));
                    f.getCampos().put("ORI_ENT_CERT_CALIDAD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_CERT_CALIDAD")));
                    f.getCampos().put("ENT_SUJETA_DER_PUBL", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ENT_SUJETA_DER_PUBL")));
                    // Necesitamos indicar si la entidad va a ser una Asociacion o No, 
                    // comprobamos si vienen datos para la tabla Aociacion. 
                    if (tablaAsociacionListaEnts != null && tablaAsociacionListaEnts.getFilas() != null && tablaAsociacionListaEnts.getFilas().size() > 0) {
                        f.addCampo("ORI_ENT_ASOCIACION", "1");
                        log.info("f.addCampo(ORI_ENT_ASOCIACION,1); --> Entidad Registrada como Asociacion");
                    } else {
                        f.addCampo("ORI_ENT_ASOCIACION", "0");
                        log.info("f.addCampo(ORI_ENT_ASOCIACION,0); --> Entidad Registrada como No-Asociacion");
                    }
                    String esAsociacion = f.getCampos().get("ORI_ENT_ASOCIACION");
                    log.info("A�adido ORI14_ENTIDAD - ORI_ENT_ASOCIACION " + " - " + datosTerceroInnerExtTable.get("HTE_NOC") + " : " + esAsociacion);
                    log.info("A�adido DATOS TABLA ORI14_ENTIDAD. " + nextval + " - " + datosTerceroInnerExtTable.get("HTE_NOC"));
                    // Rellenados los datos de la tabla entidad, hay que completar los datos de la tabla Asociacion para La entidad principal
                    // NEcesitamos obtener el codigo PK de la asociacion por si hay que guardar trayectoria, con el ID de la tabla Asociacion aunque no lo sea.
                    String nuevoCodigoEntidad = f.getCampos().get("ORI_ENT_COD");
                    String nuevoCodigoAsocDatEntidadPrinc = "";//  // Ojo solo debe usarse para crear la trayectoria si no es un asociacion
                    // La inserci�n de estos datos solo debe hacerse si, no es una asociacion
                    // Hay que crear la linea en la tabla entidad principal aunque no cumplimenten los datos de supramunicipal, etcc  
                    if ("0".equalsIgnoreCase(esAsociacion)) {
                        MELanbide42TablaVO tablaAsociacionDatosEntidad = new MELanbide42TablaVO("ORI14_ASOCIACION_ENT", modulo);
                        MELanbide42FilaVO filaAsoDatEnt_0 = new MELanbide42FilaVO(tablaAsociacionDatosEntidad);
                        seqName = MELanbide42Properties.getProperty("MELANBIDE47/ORI14_ASOCIACION/seqname");
                        nextval = dao.getSeqNextValue(seqName, con);
                        filaAsoDatEnt_0.addCampo("ORI_ENT_ASO_NUM_EXP", numExp);
                        filaAsoDatEnt_0.addCampo("ORI_ASOC_COD", nextval);
                        filaAsoDatEnt_0.addCampo("ORI_ENT_COD", nuevoCodigoEntidad);
                        filaAsoDatEnt_0.addCampo("ORI_ASOC_NOMBRE", datosTerceroInnerExtTable.get("HTE_NOC"));
                        filaAsoDatEnt_0.addCampo("ORI_ASOC_CIF", datosTerceroInnerExtTable.get("HTE_DOC"));
                        //// Actualizamos el valor de los campos SI/NO Copiamos los datos que nos han indicado en el nodo ORI14_ENTIDAD del XML
                        filaAsoDatEnt_0.addCampo("ORI_ENT_SUPRAMUN", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_SUPRAMUN")));
                        filaAsoDatEnt_0.addCampo("ORI_ENT_ADMLOCAL", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_ADMLOCAL")));
                        filaAsoDatEnt_0.addCampo("ORI_EXP_CENTROFP_PUB", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_EXP_CENTROFP_PUB")));
                        filaAsoDatEnt_0.addCampo("ORI_EXP_CENTROFP_PRIV", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_EXP_CENTROFP_PRIV")));
                        filaAsoDatEnt_0.addCampo("ORI_ENT_AGENCOLOC", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_AGENCOLOC")));
                        filaAsoDatEnt_0.addCampo("ORI_ENT_SINANIMOLUCRO", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_SINANIMOLUCRO")));
                        filaAsoDatEnt_0.addCampo("ORI_ENT_AGENCOLOC_NUMERO", f.getCampos().get("ORI_ENT_AGENCOLOC_NUMERO"));
                        filaAsoDatEnt_0.addCampo("ORI_ENT_PLAN_IGUALDAD_REG", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_PLAN_IGUALDAD_REG")));
                        filaAsoDatEnt_0.addCampo("ORI_ENT_CERT_CALIDAD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ORI_ENT_CERT_CALIDAD")));
                        filaAsoDatEnt_0.addCampo("COMP_IGUALDAD_OPCION", f.getCampos().get("COMP_IGUALDAD_OPCION"));
                        filaAsoDatEnt_0.addCampo("ENT_SUJETA_DER_PUBL", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ENT_SUJETA_DER_PUBL")));

                        nuevoCodigoAsocDatEntidadPrinc = filaAsoDatEnt_0.getCampos().get("ORI_ASOC_COD");
                        tablaAsociacionDatosEntidad.addFila(filaAsoDatEnt_0);
                        xmlExpediente.getListaModulos().get(0).addTabla(tablaAsociacionDatosEntidad);
                        log.info("No es una Agrupacion. Generado Datos para tabla ORI14_ASOCIACION (En XML temporalmete se llamara ORI14_ASOCIACION_ENT) de la entidad principal. CodEntidad " + nuevoCodigoEntidad + " Cod_Asociacion" + nuevoCodigoAsocDatEntidadPrinc + " - " + datosTerceroInnerExtTable.get("HTE_NOC") + "-" + datosTerceroInnerExtTable.get("HTE_DOC"));
                    }
                    // Ahora a�adimos los numeros de secuencia PK para las entidades que forman la asociacion 
                    // y a su vez sus PK en la tabla de las trayectorias de cada entidad.
                    // Comentado 25/05/2021 NO hay asociaciones convocatoria 2021
//                if(tablaAsociacionListaEnts!=null){
//                    seqName=MELanbide42Properties.getProperty("MELANBIDE47/ORI14_ASOCIACION/seqname");
//                    for(MELanbide42FilaVO filaAsociacion : tablaAsociacionListaEnts.getFilas()){
//                        nextval = dao.getSeqNextValue(seqName, adaptSQL.getConnection());
//                        filaAsociacion.addCampo("ORI_ASOC_COD", nextval);
//                        filaAsociacion.addCampo("ORI_ENT_COD", nuevoCodigoEntidad);
//                        String nuevoCodAsocEnt_i = filaAsociacion.getCampos().get("ORI_ASOC_COD");
//                        //// Actualizamos el valor de los campos SI/NO
//                        filaAsociacion.getCampos().put("ORI_ENT_SUPRAMUN", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaAsociacion.getCampos().get("ORI_ENT_SUPRAMUN")));
//                        filaAsociacion.getCampos().put("ORI_ENT_ADMLOCAL", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaAsociacion.getCampos().get("ORI_ENT_ADMLOCAL")));
//                        filaAsociacion.getCampos().put("ORI_EXP_CENTROFP_PUB", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaAsociacion.getCampos().get("ORI_EXP_CENTROFP_PUB")));
//                        filaAsociacion.getCampos().put("ORI_EXP_CENTROFP_PRIV", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaAsociacion.getCampos().get("ORI_EXP_CENTROFP_PRIV")));
//                        filaAsociacion.getCampos().put("ORI_ENT_AGENCOLOC", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaAsociacion.getCampos().get("ORI_ENT_AGENCOLOC")));
//                        filaAsociacion.getCampos().put("ORI_ENT_SINANIMOLUCRO", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaAsociacion.getCampos().get("ORI_ENT_SINANIMOLUCRO")));
//                        filaAsociacion.getCampos().put("ORI_ENT_PLAN_IGUALDAD_REG", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaAsociacion.getCampos().get("ORI_ENT_PLAN_IGUALDAD_REG")));
//                        filaAsociacion.getCampos().put("ORI_ENT_CERT_CALIDAD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaAsociacion.getCampos().get("ORI_ENT_CERT_CALIDAD")));
//
//                        log.info("A�adido Datos PK a Entidad de una Asociacion ORI_ASOC_COD=" + nuevoCodAsocEnt_i + " COdgio Entidad=" + nuevoCodigoEntidad);
//                        // Buscamos la trayectoria por el codigo asignado en el formulario WEB NUM_ENTIDAD_ANEXO_I
//                        // ORI_ASOC_NOMBRE = ORI_ORITRAY_NOMBASO
//                        String codigoPestanaAsociacion = filaAsociacion.getCampos().get("NUM_ENTIDAD_ANEXO_I");
//                        if(tablaTrayectoria!=null){
//                            for(MELanbide42FilaVO filaTrayectoria : tablaTrayectoria.getFilas()){
//                                String codigoPestanaAsoTrayectoria = filaTrayectoria.getCampos().get("NUM_ENTIDAD_ANEXO_I");
//                                if(codigoPestanaAsoTrayectoria.equalsIgnoreCase(codigoPestanaAsociacion)){
//                                    filaTrayectoria.addCampo("TRAYCODIGOENTIDAD", nuevoCodAsocEnt_i);
//                                    filaTrayectoria.getCampos().put("TRAYTIENEEXPERIENCIA", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaTrayectoria.getCampos().get("TRAYTIENEEXPERIENCIA")));
//                                    // Calcular el numero de meses  -- TRAYNUMEROMESES : TRAYFECHAINICIO, TRAYFECHAFIN
//                                    String TRAYFECHAINICIO = filaTrayectoria.getCampos().get("TRAYFECHAINICIO");
//                                    String TRAYFECHAFIN = filaTrayectoria.getCampos().get("TRAYFECHAFIN");
//                                    Integer nroMeses = MELanbide42GeneralUtils.calularMesesEntreFechas(TRAYFECHAINICIO, TRAYFECHAFIN);
//                                    filaTrayectoria.getCampos().put("TRAYNUMEROMESES", String.valueOf(nroMeses != null ? nroMeses : 0));
//                                }
//                                log.info("A�adido TRAYCODIGOENTIDAD=" + nuevoCodAsocEnt_i + " a la fila de ORI_TRAYECTORIA_ENTIDAD con NUM_ENTIDAD_ANEXO_I=" + codigoPestanaAsociacion);
//                            }
//                        }
//                    }
//                }
                    // En caso de que no sea una Asociacion, vamos a preparar los datos de la trayectoria 
                    // para la entidad Pricipal
                    // && tablaTrayectoria.getFilas().size()==1 Debe venir solo una linea con los datos de trayectoria (Convocatorias antes de 2021 Comentamos 25/05/2021)
                    if ("0".equalsIgnoreCase(esAsociacion) && tablaTrayectoria != null && tablaTrayectoria.getFilas() != null) {
                        for (MELanbide42FilaVO filaTrayectoria1 : tablaTrayectoria.getFilas()) {
                            //String nombreAsiEnt_Traye1 = filaTrayectoria1.getCampos().get("NUM_ENTIDAD_ANEXO_I");
                            filaTrayectoria1.addCampo("TRAYCODIGOENTIDAD", nuevoCodigoEntidad);
                            filaTrayectoria1.getCampos().put("TRAYTIENEEXPERIENCIA", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaTrayectoria1.getCampos().get("TRAYTIENEEXPERIENCIA")));
                            // Calcular el numero de meses  -- TRAYNUMEROMESES : TRAYFECHAINICIO, TRAYFECHAFIN
                            String TRAYFECHAINICIO = filaTrayectoria1.getCampos().get("TRAYFECHAINICIO");
                            String TRAYFECHAFIN = filaTrayectoria1.getCampos().get("TRAYFECHAFIN");
                            Integer nroMeses = MELanbide42GeneralUtils.calularMesesEntreFechas(TRAYFECHAINICIO, TRAYFECHAFIN);
                            filaTrayectoria1.getCampos().put("TRAYNUMEROMESES", String.valueOf(nroMeses != null ? nroMeses : 0));
                            log.info("Añadido TRAYCODIGOENTIDAD=" + nuevoCodigoEntidad + " a la fila de ORI_TRAYECTORIA_ENTIDAD"); // con NUM_ENTIDAD_ANEXO_I=" + nombreAsiEnt_Traye1);
                        }
                    }
                    // Ahora necesitamos completar la tabla de Ubicaciones.
                    // Para ello necesitamos dos FK --> CODIGO DE ENTIDAD (PK de la tabla ORI14_ENTIDAD) y CODIGO DE AMBITO (PK de la tabla precargada, ORI14_AMBITOS_HORAS)  )
                    if (tablaUbicaciones != null) {
                        for (MELanbide42FilaVO filaUbicciones : tablaUbicaciones.getFilas()) {
                            filaUbicciones.addCampo("ORI_ORIENT_ANO", ejercicio);
                            filaUbicciones.addCampo("ORI_ENT_COD", nuevoCodigoEntidad);
                            /*
                        // Esto se ha modificado en el formulario web, ahora se lee de las tablas precargadas
                        // En el XML la descripcion del ambito vienen en ORI_ORIENT_AMBITO_UBICACION
                        //Esto hay que revisarlo, porque depender de lo ingresado por el usuario puede dar muchos errores al recuperar el codigo del ambito.
                        // Deberia limitarse la lista a la precarga de BBDD en el formulario u otro metodo de limitar las opcines del usuario.
                        String nombreAmbito = filaUbicciones.getCampos().get("ORI_ORIENT_AMBITO_UBICACION");
                        log.info("Vamos a buscar el codigo de ambito en BBDD para " + nombreAmbito);
                        String codAmbito = manager.getCodAmbitoxNombrexAnioConvocxCodPro(nombreAmbito,ejercicio,procedimiento,adaptSQL.getConnection());
                        log.info("Codigo de ambito recuperado en BBDD para " + nombreAmbito + " : " + codAmbito);
                        filaUbicciones.addCampo("ORI_AMB_COD",codAmbito);
                             */
                            //// Actualizamos el valor de los campos SI/NO
                            filaUbicciones.getCampos().put("ORI_ORIENT_ESPACIOADICIONA", MELanbide42GeneralUtils.parsearNumero1_0aTexto_S_N(filaUbicciones.getCampos().get("ORI_ORIENT_ESPACIOADICIONA")));
                            filaUbicciones.getCampos().put("ORI_ORIENT_ESPHERRABUSQEMP", MELanbide42GeneralUtils.parsearNumero1_0aTexto_S_N(filaUbicciones.getCampos().get("ORI_ORIENT_ESPHERRABUSQEMP")));
                            filaUbicciones.getCampos().put("ORI_ORIENT_DESPACHOS", MELanbide42GeneralUtils.parsearNumero1_0aTexto_S_N(filaUbicciones.getCampos().get("ORI_ORIENT_DESPACHOS")));
                            filaUbicciones.getCampos().put("ORI_ORIENT_AULAGRUPAL", MELanbide42GeneralUtils.parsearNumero1_0aTexto_S_N(filaUbicciones.getCampos().get("ORI_ORIENT_AULAGRUPAL")));
                            filaUbicciones.getCampos().put("ORI_LOCALPREVAPRO", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaUbicciones.getCampos().get("ORI_LOCALPREVAPRO")));
                            filaUbicciones.getCampos().put("ORI_MATENREQ_LPA", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaUbicciones.getCampos().get("ORI_MATENREQ_LPA")));

                        }
                        log.info("Hemos puestos los cod de Ambitos en las ubicaciones de la entidad " + nuevoCodigoEntidad);
                    }
                    // Rellenamos los nuevos datos de Certificados de calidad para la entidad
                    if (tablaCertificadosCalidad != null) {
                        for (MELanbide42FilaVO filaCertCalidad : tablaCertificadosCalidad.getFilas()) {
                            filaCertCalidad.addCampo("ID_ENTIDAD", nuevoCodigoEntidad);
                            filaCertCalidad.getCampos().put("VALOR_SN_SOLICITUD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaCertCalidad.getCampos().get("VALOR_SN_SOLICITUD")));
                            filaCertCalidad.removeCampo("NUM_ENTIDAD_ANEXO_I");
                        }
                        log.info("Id Entidad added to tabla Certificados Calidad: " + nuevoCodigoEntidad);
                    }

                    //Desde Conv, 2021 Se uifican en Trayectoria
//                if(tablaOtrosProgramas!=null){
//                    for(MELanbide42FilaVO filaUbicciones : tablaOtrosProgramas.getFilas()){
//                        filaUbicciones.addCampo("ORI_OTRPRO_EXP_EJE", ejercicio);
//                        filaUbicciones.addCampo("ORI_OTRPRO_COD_ENTIDAD", nuevoCodigoEntidad);
//                    }
//                    log.info("Hemos puestos cod de entidad a la tabla otros programas " + nuevoCodigoEntidad);
//                }
//                if(tablaActividades!=null){
//                    for(MELanbide42FilaVO filaUbicciones : tablaActividades.getFilas()){
//                        filaUbicciones.addCampo("ORI_ACTIV_EXP_EJE", ejercicio);
//                        filaUbicciones.addCampo("ORI_ACTIV_COD_ENTIDAD", nuevoCodigoEntidad);
//                    }
//                    log.info("Hemos puestos cod de entidad a la tabla Actividades programas " + nuevoCodigoEntidad);
//                }
                } catch (SQLException ex) {
                    log.error("Error al prepararDatosORI14Mod47 en cargas telematicas : " + ex.getMessage(), ex);
                }
            }    
        } catch (Exception ex) {
            log.error("Error cargando M47 " + ex.getMessage(),ex);
        }finally{
            adaptSQL.devolverConexion(con);
        }
        log.info("prepararDatosORI14Mod47() : END");
    }
    
    public static void prepararDatosCEMPMod32(MELanbide42XMLAltaExpediente xmlExpediente, MELanbide42TablaVO tablaEntidad, MELanbide42TablaVO tablaTrayectoria, MELanbide42TablaVO tablaUbicaciones, MELanbide42TablaVO tablaAmbitosSoli, MELanbide42TablaVO tablaCriteriosCentro, AdaptadorSQLBD adaptSQL, String numExp, String codOrg) throws MELanbide42Exception {
        log.info("prepararDatosCEMPMod32() : BEGIN");
        MELanbide42DAO dao = MELanbide42DAO.getInstance();
        MELanbide42Manager manager = new MELanbide42Manager();
        Connection con  = null;
        try {
            con  = adaptSQL.getConnection();
            String seqName = MELanbide42Properties.getProperty("MELANBIDE32/ORI_ENTIDAD/seqname");
            String ejercicio = MELanbide42GeneralUtils.getAnioFromNumExpediente(numExp);
            Map<String, String> datosTerceroInnerExtTable = manager.getTerceroxNumExpxRol(numExp, 1, con);
            // Hay que preparar los datos de la tabla Entidad Generando su Clave 

            for (MELanbide42FilaVO f : tablaEntidad.getFilas()) {
                try {
                    String nextval;
                    nextval = dao.getSeqNextValue(seqName, con);
                    f.addCampo("ORI_ENT_COD", nextval);
                    f.addCampo("EXT_MUN", codOrg);
                    f.addCampo("EXT_EJE", ejercicio);
                    f.addCampo("EXT_NUM", numExp);
                    f.addCampo("EXT_TER", datosTerceroInnerExtTable.get("EXT_TER"));
                    f.addCampo("EXT_NVR", datosTerceroInnerExtTable.get("EXT_NVR"));
                    f.addCampo("ORI_ENT_NOM", datosTerceroInnerExtTable.get("HTE_NOC"));

                    log.info("Anadido DATOS TABLA ORI_ENTIDAD. " + nextval + " - " + datosTerceroInnerExtTable.get("HTE_NOC"));

                    boolean marcarAdminLocalSegubCrite7_1 = false;

                    // Rellenados los datos de la tabla entidad,  vamos a preparar los datos de la trayectoria 
                    String nuevoCodigoEntidad = f.getCampos().get("ORI_ENT_COD");
                    if (tablaTrayectoria != null) {
                        for (MELanbide42FilaVO filaTrayectoria1 : tablaTrayectoria.getFilas()) {
                            filaTrayectoria1.addCampo("ORI_ENT_COD", nuevoCodigoEntidad);
                            filaTrayectoria1.addCampo("ORI_CE_TRAY_NUM_EXP", numExp);
                            log.info("Anadido ORI_CE_COD=" + nuevoCodigoEntidad + " a la fila de ORI_CE_TRAYECTORIA.");
                        }
                    }
                    // Ahora necesitamos completar la tabla de Ubicaciones.
                    // Para ello necesitamos dos FK --> CODIGO DE ENTIDAD (PK de la tabla ORI_ENTIDAD) y CODIGO DE AMBITO (PK de la tabla precargada, ORI14_AMBITOS_HORAS)  )
                    // En este emetodo creamos un mapa de los criteriso para centros en ambitos distribuidos, porque hay que asignarlos a todas las ubicaciones
                    // Tambien los borramos de la tabla criterios para que no se intenten insertar dos veces
                    Map<String,List<MELanbide42FilaVO>> mapaCriteriosCentrosAmbitosDistribuidos = new HashMap<String, List<MELanbide42FilaVO>>();
                    if (tablaCriteriosCentro != null) {
                        mapaCriteriosCentrosAmbitosDistribuidos = getCriteriosCentrosAmbitosDistribuidos(tablaCriteriosCentro);
                    }
                    if (tablaUbicaciones != null) {
                        for (MELanbide42FilaVO filaUbicciones : tablaUbicaciones.getFilas()) {
                            String seqNameUbicaciones = MELanbide42Properties.getProperty("MELANBIDE32/ORI_CE_UBIC/seqname");
                            String nextvalUbicaciones = dao.getSeqNextValue(seqNameUbicaciones, con);
                            filaUbicciones.addCampo("ORI_CE_UBIC_COD", nextvalUbicaciones);
                            filaUbicciones.addCampo("ORI_ENT_COD", nuevoCodigoEntidad);
                            filaUbicciones.addCampo("MUN_PAI", ConstantesMeLanbide42.CODIGO_PAIS_ESPANA);
                            filaUbicciones.addCampo("PRV_PAI", ConstantesMeLanbide42.CODIGO_PAIS_ESPANA);
                            filaUbicciones.getCampos().put("ORI_CE_APROBADO", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaUbicciones.getCampos().get("ORI_CE_APROBADO")));
                            filaUbicciones.getCampos().put("ORI_CE_MANTENIDO", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaUbicciones.getCampos().get("ORI_CE_MANTENIDO")));
                            // Rellenar los criterios de evaluacion por centro
                            String idAmbitoNroCentro = filaUbicciones.getCampos().get("numeroAmbitoCentro");
                            //Verificar si la ubicacion es para un ambito especial y marcar el campo ORI_CE_ESPECIAL
                            if(idAmbitoNroCentro != null && idAmbitoNroCentro.startsWith("1_")){
                                filaUbicciones.addCampo("ORI_CE_ESPECIAL","S");
                            }

                            log.info("Tratando centro: " + idAmbitoNroCentro);
                            if(idAmbitoNroCentro != null && idAmbitoNroCentro.startsWith("2_")){
                                log.info("Criterios-Distribuidos- Centro : " + idAmbitoNroCentro);
                                // Para los distribuidos creamos la fila dede el mapa
                                List<MELanbide42FilaVO> filasCriteriosCentro = mapaCriteriosCentrosAmbitosDistribuidos.get(idAmbitoNroCentro);
                                if(filasCriteriosCentro != null ){
                                    for( MELanbide42FilaVO filaCriteriosCentro :filasCriteriosCentro){
                                        MELanbide42FilaVO copiaModificable = new MELanbide42FilaVO(filaCriteriosCentro.getTabla());
                                        copiaModificable.getCampos().putAll(filaCriteriosCentro.getCampos());

                                        marcarAdminLocalSegubCrite7_1 = marcarAdminLocalSegubCrite7_1 || esCriterioAdminLocal7_1(copiaModificable);
                                        prepararDatosCEMPMod32_filaCriteriosCentros(copiaModificable,nextvalUbicaciones,con);

                                        if (tablaCriteriosCentro != null)
                                            tablaCriteriosCentro.addFila(copiaModificable);
                                        else{
                                            tablaCriteriosCentro = new MELanbide42TablaVO("ORI_CE_CRITERIOS_CENTRO",tablaUbicaciones.getModulo());
                                            tablaCriteriosCentro.addFila(copiaModificable);
                                        }
                                    }
                                }
                            }else {
                            if (tablaCriteriosCentro != null) {
                                    log.info("Criterios-NoDistribuidos- Centro : " + idAmbitoNroCentro);
                                // Leemos solo las lineas que corresponden al centro 
                                for (MELanbide42FilaVO filaCriteriosCentro : tablaCriteriosCentro.getFilasByFieldValue("numeroAmbitoCentro", idAmbitoNroCentro) ) {
                                        //tablaCriteriosCentro.getFilas()
                                    if (idAmbitoNroCentro != null && idAmbitoNroCentro.equalsIgnoreCase(filaCriteriosCentro.getCampos().get("numeroAmbitoCentro"))) {
                                            marcarAdminLocalSegubCrite7_1 = marcarAdminLocalSegubCrite7_1 || esCriterioAdminLocal7_1(filaCriteriosCentro);
                                        prepararDatosCEMPMod32_filaCriteriosCentros(filaCriteriosCentro,nextvalUbicaciones,con);
                                    } else {
                                        log.info("Continua el loop no se corresponden los IDs: "
                                                + idAmbitoNroCentro + " <==> " + filaCriteriosCentro.getCampos().get("numeroAmbitoCentro")
                                        );
                                    }
                                }

                            } else {
                                log.info("No se han recibido datos en la tabla Cirterio por centro.");
                            }
                            }
                            // Quitar codigo temporal de Ubicacion numero centro Ambito
                            filaUbicciones.removeCampo("numeroAmbitoCentro");
                        }
                    }
                    
                    log.info("Hemos puestos los cod de Ambitos en las ubicaciones de la entidad " + nuevoCodigoEntidad);
                    // Despues de tratar la tabla de centros y criterios quedan criterios sin relacionar, montamos la SQl para que crear el centro y evitar que falle
                    prepararDatosCEMPMod32_criteriosSinCentro(xmlExpediente, tablaCriteriosCentro, tablaUbicaciones, nuevoCodigoEntidad, con);
                    
                    if(marcarAdminLocalSegubCrite7_1){
                        f.addCampo("ORI_ENT_ADMLOCAL","S");
                    }
                    
                } catch (SQLException ex) {
                    log.error("SQLException : Error al procesar los datos de carga para M32 : " + ex.getMessage() + " - " + ex.getSQLState(), ex);
                    throw  new MELanbide42Exception("SQLException : Error al procesar los datos de carga para M32 : " + ex.getMessage() + " - " + ex.getSQLState(), ex);
                } catch (MELanbide42Exception ex) {
                    log.error("MELanbide42Exception : Error al procesar los datos de carga para M32 : " + ex.getMessage(), ex);
                    throw  ex;
                }
            }
        } catch (Exception ex) {
            log.error("Exception : Error al procesar los datos de carga para M32 : " + ex.getMessage() + " - " + ex.getLocalizedMessage(), ex);
            throw  new MELanbide42Exception("Exception : Error al procesar los datos de carga para M32 : " + ex.getMessage() + " - " + ex.getLocalizedMessage(), ex);
        }finally{
            try {
                adaptSQL.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar la conexion de BBDD " + e.getMessage() + " - " + e.getLocalizedMessage(), e);
            }
        }
        log.info("prepararDatosCEMPMod32() : END");
    }
    
    private static boolean esCriterioAdminLocal7_1(MELanbide42FilaVO fila) {
        String ejercicioConvocatoria = fila.getCampos().get("ejercicioConvocatoria");
        String codigoCriterioOrden = fila.getCampos().get("codigoCriterioOrden");
        String codigoCriterioOrdenOpcion = fila.getCampos().get("codigoCriterioOrdenOpcion");

        return ejercicioConvocatoria != null && ejercicioConvocatoria.equalsIgnoreCase("2025")
                && codigoCriterioOrden != null && codigoCriterioOrden.equalsIgnoreCase("7")
                && codigoCriterioOrdenOpcion != null && codigoCriterioOrdenOpcion.equalsIgnoreCase("1");
    }

    private static Map<String, List<MELanbide42FilaVO>> getCriteriosCentrosAmbitosDistribuidos(MELanbide42TablaVO tablaCriteriosCentro) {
        Map<String,List<MELanbide42FilaVO>> respuesta = new HashMap<String, List<MELanbide42FilaVO>>();
        MELanbide42TablaVO tablaCriteriosCentroSinDistribuidos = new MELanbide42TablaVO(tablaCriteriosCentro.getNombre(),tablaCriteriosCentro.getModulo());
        try {
            if(tablaCriteriosCentro != null && tablaCriteriosCentro.getFilas() != null){
                for(MELanbide42FilaVO criterios : tablaCriteriosCentro.getFilas()){
                    String numeroAmbitoCentro = criterios.getCampos().get("numeroAmbitoCentro");
                    if(numeroAmbitoCentro != null && numeroAmbitoCentro.startsWith("2_")){
                        if(respuesta.containsKey(numeroAmbitoCentro)){
                            respuesta.get(numeroAmbitoCentro).add(criterios);
                        }else{
                            List<MELanbide42FilaVO> listaCriterios = new ArrayList<MELanbide42FilaVO>();
                            listaCriterios.add(criterios);
                            respuesta.put(numeroAmbitoCentro, listaCriterios);
                        }
                    }else {
                        tablaCriteriosCentroSinDistribuidos.addFila(criterios);
                    }
                }
                tablaCriteriosCentro.getFilas().clear();
                tablaCriteriosCentro.getFilas().addAll(tablaCriteriosCentroSinDistribuidos.getFilas());
            }
        } catch (Exception e) {
            log.error("Error al rellenar el map de criterios para centros en ambitos distribuidos: " + e.getMessage(), e);
        }
        return respuesta;
    }

     public static void prepararDatosCOLECMod48(MELanbide42XMLAltaExpediente xmlExpediente, MELanbide42TablaVO tablaSolicitud, MELanbide42TablaVO tablaEntidad, MELanbide42TablaVO tablaEntidadListAgrup, MELanbide42TablaVO tablaTrayectoriaEsp, MELanbide42TablaVO tablaTrayectoria, MELanbide42TablaVO tablaOtrosProgramas, MELanbide42TablaVO tablaActividades, MELanbide42TablaVO tablaUbicaciones
             ,MELanbide42TablaVO tablaPorcxColecxTHH,MELanbide42TablaVO tablaCertificadosCalidad
             ,AdaptadorSQLBD adapt, String numExp, String codOrg) throws MELanbide42Exception, BDException, Exception {
         if (log.isDebugEnabled()) {
             log.info("prepararDatosCOLECMod48() : BEGIN");
         }

         MELanbide42DAO dao = MELanbide42DAO.getInstance();
         MELanbide42Manager manager = new MELanbide42Manager();
         String seqName = MELanbide42Properties.getProperty("MELANBIDE48/COLEC_ENTIDAD/seqname");
         String ejercicio = MELanbide42GeneralUtils.getAnioFromNumExpediente(numExp);
         //String procedimiento = MELanbide42GeneralUtils.getCodigoProcFromNumExpediente(numExp);
        Connection con = null;
        try{
        con = adapt.getConnection();
         Map<String, String> datosTerceroInnerExtTable = manager.getTerceroxNumExpxRol(numExp, 1, con);
         Map<String, String> mapaEntidadesAgrupaCodPestaAnexos = new HashMap<String, String>();
         // Hay que preparar los datos de la tabla Entidad Generando su Clave 
         for (MELanbide42FilaVO f : tablaEntidad.getFilas()) {
             try {
                 String nextval;
                 nextval = dao.getSeqNextValue(seqName, con);
                 f.addCampo("COLEC_ENT_COD", nextval);
                 f.addCampo("COLEC_EXP_EJE", ejercicio);
                 f.addCampo("COLEC_ANIO_CONVOCATORIA", ejercicio);
                 f.addCampo("COLEC_NUMEXP", numExp);
                 // Vienen en el XML Dejamos los que nos pasan 
                 //f.addCampo("COLEC_ENT_TIPO_CIF", datosTerceroInnerExtTable.get("HTE_TID"));
                 //f.addCampo("COLEC_ENT_CIF", datosTerceroInnerExtTable.get("HTE_DOC"));
                 //f.addCampo("COLEC_ENT_NOMBRE", datosTerceroInnerExtTable.get("HTE_NOC"));

                 log.info("ANadido DATOS TABLA COLEC_ENTIDAD. " + nextval + " - " + datosTerceroInnerExtTable.get("HTE_NOC"));
                 // Ahora comprobamos si es una Agrupacion para cargar la entidad padre en la tabla de los componentes de la agrupacion
                 // y Guardar los demas datos de acuerdo a cada una de las entidades.
                 String nuevoCodigoEntidad = f.getCampos().get("COLEC_ENT_COD");
                 String esAgrupacion = f.getCampos().get("COLEC_ENT_ESAGRUPACION");
                 // Sino viene campo comprobaamos con el nodo de la tabla de agrupaciones
                 if(esAgrupacion!=null && !esAgrupacion.equalsIgnoreCase("")){
                     esAgrupacion = MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(esAgrupacion);
                 }else{
                     esAgrupacion=String.valueOf(tablaEntidadListAgrup!=null && tablaEntidadListAgrup.getFilas() != null 
                             && tablaEntidadListAgrup.getFilas().size()>0 ? 1 : 0);
                 }
                 // Actualizamos el valor del la tabla como Int en BBDD
                 f.addCampo("COLEC_ENT_ESAGRUPACION", esAgrupacion);
                 // Mapeamos los valores S/N de desplegables desde Formulario
                 f.getCampos().put("ACEPTNUMEROSUPEHORAS", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ACEPTNUMEROSUPEHORAS")));
                 f.getCampos().put("SEGUNDOLOCALMISMOAMB", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("SEGUNDOLOCALMISMOAMB")));
                 f.getCampos().put("PLANIGUALDAD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("PLANIGUALDAD")));
                 f.getCampos().put("CERTIFICADOCALIDAD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("CERTIFICADOCALIDAD")));
                 f.getCampos().put("ENT_SUJETA_DER_PUBL", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ENT_SUJETA_DER_PUBL")));
                 f.getCampos().put("ENT_SIN_ANIMO_LUCRO", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(f.getCampos().get("ENT_SIN_ANIMO_LUCRO")));
                 //Cargamos el codigo de la entidad padre en los componentes de la agrupacion
                 if("1".equalsIgnoreCase(esAgrupacion)){
                     for (MELanbide42FilaVO integrAgup : tablaEntidadListAgrup.getFilas()) {
                        String nextval1;
                        String seqName1 = MELanbide42Properties.getProperty("MELANBIDE48/COLEC_ENTIDAD_AGRUP_LIST/seqname");
                        nextval1 = dao.getSeqNextValue(seqName1, con);
                        String codigoEntidadInteAgrupa = nextval1;
                        integrAgup.addCampo("COLEC_ENT_AGRUP_COD", nuevoCodigoEntidad); //Entidad Padre
                        integrAgup.addCampo("COLEC_ENT_COD", codigoEntidadInteAgrupa);
                        integrAgup.addCampo("COLEC_NUMEXP", numExp); 
                        // A�adimos en el HashMap la clave generada y el orden que le corresponde para los anexos
                        String nroPestanaEntidad = integrAgup.getCampos().get("NUM_ENTIDAD_ANEXO_I");
                        mapaEntidadesAgrupaCodPestaAnexos.put(nroPestanaEntidad, codigoEntidadInteAgrupa);
                        // Mapeamos datos de certificados de cada entidad list de agrupacion
                         if (tablaCertificadosCalidad != null){
                             for(MELanbide42FilaVO filaCertCalidad : tablaCertificadosCalidad.getFilasByFieldValue("NUM_ENTIDAD_ANEXO_I",nroPestanaEntidad)){
                                 filaCertCalidad.addCampo("ID_ENTIDAD",codigoEntidadInteAgrupa);
                                 filaCertCalidad.getCampos().put("VALOR_SN_SOLICITUD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaCertCalidad.getCampos().get("VALOR_SN_SOLICITUD")));
                                 filaCertCalidad.removeCampo("NUM_ENTIDAD_ANEXO_I");
                             }
                         }

                        // Quitamos campo temporal para evitar fallo de inserci�n.
                        integrAgup.removeCampo("NUM_ENTIDAD_ANEXO_I");
                         // Mapeamos los valores S/N de desplegables desde Formulario
                         integrAgup.getCampos().put("ACEPTNUMEROSUPEHORAS", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(integrAgup.getCampos().get("ACEPTNUMEROSUPEHORAS")));
                         integrAgup.getCampos().put("SEGUNDOLOCALMISMOAMB", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(integrAgup.getCampos().get("SEGUNDOLOCALMISMOAMB")));
                         integrAgup.getCampos().put("PLANIGUALDAD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(integrAgup.getCampos().get("PLANIGUALDAD")));
                         integrAgup.getCampos().put("CERTIFICADOCALIDAD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(integrAgup.getCampos().get("CERTIFICADOCALIDAD")));
                         integrAgup.getCampos().put("ENT_SUJETA_DER_PUBL", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(integrAgup.getCampos().get("ENT_SUJETA_DER_PUBL")));
                         integrAgup.getCampos().put("ENT_SIN_ANIMO_LUCRO", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(integrAgup.getCampos().get("ENT_SIN_ANIMO_LUCRO")));
                     }
                 }else{
                     // A�adimos con el codigo 0 al mapa el codigo generado de la entidad padre
                     mapaEntidadesAgrupaCodPestaAnexos.put("0", nuevoCodigoEntidad);
                 }
                 
                 
                 // Rellenados los datos de las tablas entidades,  vamos a preparar los datos de las
                 // trayectorias Normal y Especial 
                 if (tablaTrayectoriaEsp != null) {
                     for (MELanbide42FilaVO filaTrayectoria1 : tablaTrayectoriaEsp.getFilas()) {
                         String Codpestana = filaTrayectoria1.getCampos().get("NUM_ENTIDAD_ANEXO_I");
                         String codEntidadItAgrup = MELanbide42GeneralUtils.getCodEntidadDsdMapNroPestanaFormCOLEC(mapaEntidadesAgrupaCodPestaAnexos,esAgrupacion,Codpestana);
                         log.info("Mapeada Entidad " + codEntidadItAgrup + " CodigoPesta�a : " + Codpestana + " - Entidad Principal : "  + nuevoCodigoEntidad);
                         filaTrayectoria1.addCampo("COLEC_COD_ENTIDAD", codEntidadItAgrup);
                         filaTrayectoria1.addCampo("COLEC_NUMEXP", numExp);
                         filaTrayectoria1.addCampo("COLEC_EXP_EJE", ejercicio);
                         // Mapeamos los valores S/N de desplegables desde Formulario
                         filaTrayectoria1.getCampos().put("TRAYTIENEEXPERIENCIA", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaTrayectoria1.getCampos().get("TRAYTIENEEXPERIENCIA")));
                         log.info("Trayectoria Especial : A�adido COLEC_COD_ENTIDAD=" + codEntidadItAgrup + " a la fila de COLEC_TRAY_ESP.");
                         // Calcular el numero de meses  -- TRAYNUMEROMESES : TRAYFECHAINICIO, TRAYFECHAFIN
                         String TRAYFECHAINICIO = filaTrayectoria1.getCampos().get("TRAYFECHAINICIO");
                         String TRAYFECHAFIN = filaTrayectoria1.getCampos().get("TRAYFECHAFIN");
                         Integer nroMeses = MELanbide42GeneralUtils.calularMesesEntreFechas(TRAYFECHAINICIO, TRAYFECHAFIN);
                         filaTrayectoria1.getCampos().put("TRAYNUMEROMESES", String.valueOf(nroMeses != null ? nroMeses : 0));
                         // Quitamos campo temporal para evitar fallo de inserci�n.
                         filaTrayectoria1.removeCampo("NUM_ENTIDAD_ANEXO_I");
                     }
                 }
                 if (tablaTrayectoria != null) {
                     for (MELanbide42FilaVO filaTrayectoria1 : tablaTrayectoria.getFilas()) {
                         String Codpestana = filaTrayectoria1.getCampos().get("NUM_ENTIDAD_ANEXO_I");
                         String codEntidadItAgrup = MELanbide42GeneralUtils.getCodEntidadDsdMapNroPestanaFormCOLEC(mapaEntidadesAgrupaCodPestaAnexos,esAgrupacion,Codpestana);
                         log.info("Mapeada Entidad " + codEntidadItAgrup + " CodigoPesta�a : " + Codpestana + " - Entidad Principal : " + nuevoCodigoEntidad);
                         filaTrayectoria1.addCampo("TRAYCODIGOENTIDAD", codEntidadItAgrup);
                         filaTrayectoria1.addCampo("TRAYNUMEXPEDIENTE", numExp);
                         //filaTrayectoria1.addCampo("COLEC_EXP_EJE", ejercicio);
                         // Quitamos campo temporal para evitar fallo de inserci�n.
                         // Mapeamos los valores S/N de desplegables desde Formulario
                         filaTrayectoria1.getCampos().put("TRAYTIENEEXPERIENCIA", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaTrayectoria1.getCampos().get("TRAYTIENEEXPERIENCIA")));
                         // Calcular el numero de meses  -- TRAYNUMEROMESES : TRAYFECHAINICIO, TRAYFECHAFIN
                         String TRAYFECHAINICIO = filaTrayectoria1.getCampos().get("TRAYFECHAINICIO");
                         String TRAYFECHAFIN = filaTrayectoria1.getCampos().get("TRAYFECHAFIN");
                         Integer nroMeses = MELanbide42GeneralUtils.calularMesesEntreFechas(TRAYFECHAINICIO,TRAYFECHAFIN);
                         filaTrayectoria1.getCampos().put("TRAYNUMEROMESES",String.valueOf(nroMeses!=null?nroMeses:0));
                         filaTrayectoria1.removeCampo("NUM_ENTIDAD_ANEXO_I");
                         log.info("Trayectoria General : A�adido COLEC_COD_ENTIDAD=" + codEntidadItAgrup + " a la fila de COLEC_TRAY.");
                     }
                 }
                 // Actividades y Otros Programas
                 if (tablaActividades != null) {
                     for (MELanbide42FilaVO fila : tablaActividades.getFilas()) {
                         String Codpestana = fila.getCampos().get("NUM_ENTIDAD_ANEXO_I");
                         String codEntidadItAgrup = MELanbide42GeneralUtils.getCodEntidadDsdMapNroPestanaFormCOLEC(mapaEntidadesAgrupaCodPestaAnexos,esAgrupacion,Codpestana);
                         log.info("Mapeada Entidad " + codEntidadItAgrup + " CodigoPesta�a : " + Codpestana + " - Entidad Principal : " + nuevoCodigoEntidad);
                         fila.addCampo("COLEC_ACTIV_COD_ENTIDAD", codEntidadItAgrup);
                         fila.addCampo("COLEC_ACTIV_NUMEXP", numExp);
                         fila.addCampo("COLEC_ACTIV_EXP_EJE", ejercicio);
                         // Calcular el numero de meses  -- NUMEROMESES: COLEC_ACTIV_INICIO, COLEC_ACTIV_FIN
                         String TRAYFECHAINICIO = fila.getCampos().get("COLEC_ACTIV_INICIO");
                         String TRAYFECHAFIN = fila.getCampos().get("COLEC_ACTIV_FIN");
                         Integer nroMeses = MELanbide42GeneralUtils.calularMesesEntreFechas(TRAYFECHAINICIO, TRAYFECHAFIN);
                         fila.getCampos().put("NUMEROMESES", String.valueOf(nroMeses != null ? nroMeses : 0));
                         // Quitamos campo temporal para evitar fallo de inserci�n.
                         fila.removeCampo("NUM_ENTIDAD_ANEXO_I");
                         log.info("Trayectoria General : A�adido COLEC_ACTIV_COD_ENTIDAD=" + codEntidadItAgrup + " a la fila de COLEC_ACTIVIDADES.");
                     }
                 }
                 if (tablaOtrosProgramas != null) {
                     for (MELanbide42FilaVO fila : tablaOtrosProgramas.getFilas()) {
                         String Codpestana = fila.getCampos().get("NUM_ENTIDAD_ANEXO_I");
                         String codEntidadItAgrup = MELanbide42GeneralUtils.getCodEntidadDsdMapNroPestanaFormCOLEC(mapaEntidadesAgrupaCodPestaAnexos,esAgrupacion,Codpestana);
                         log.info("Mapeada Entidad " + codEntidadItAgrup + " CodigoPesta�a : " + Codpestana + " - Entidad Principal : " + nuevoCodigoEntidad);
                         fila.addCampo("COLEC_OTRPRO_COD_ENTIDAD", codEntidadItAgrup);
                         fila.addCampo("COLEC_OTRPRO_NUMEXP", numExp);
                         fila.addCampo("COLEC_OTRPRO_EXP_EJE", ejercicio);
                         // Calcular el numero de meses  -- NUMEROMESES : COLEC_OTRPRO_PROG_INICIO, COLEC_OTRPRO_PROG_FIN
                         String TRAYFECHAINICIO = fila.getCampos().get("COLEC_OTRPRO_PROG_INICIO");
                         String TRAYFECHAFIN = fila.getCampos().get("COLEC_OTRPRO_PROG_FIN");
                         Integer nroMeses = MELanbide42GeneralUtils.calularMesesEntreFechas(TRAYFECHAINICIO, TRAYFECHAFIN);
                         fila.getCampos().put("NUMEROMESES", String.valueOf(nroMeses != null ? nroMeses : 0));
                         // Quitamos campo temporal para evitar fallo de inserci�n.
                         fila.removeCampo("NUM_ENTIDAD_ANEXO_I");
                         log.info("Trayectoria General : A�adido COLEC_OTRPRO_COD_ENTIDAD=" + codEntidadItAgrup + " a la fila de COLEC_OTROS_PROGRAMAS.");
                     }
                 }
                 // Nueva tabla de compromiso se realizacion por colectivo y TH
                 if (tablaPorcxColecxTHH != null) {
                     for (MELanbide42FilaVO fila : tablaPorcxColecxTHH.getFilas()) {
                         String Codpestana = fila.getCampos().get("NUM_ENTIDAD_ANEXO_I");
                         String codEntidadItAgrup = MELanbide42GeneralUtils.getCodEntidadDsdMapNroPestanaFormCOLEC(mapaEntidadesAgrupaCodPestaAnexos, esAgrupacion, Codpestana);
                         log.info("Mapeada Entidad -tablaPorcxColecxTHH " + codEntidadItAgrup + " CodigoPesta�a : " + Codpestana + " - Entidad Principal : " + nuevoCodigoEntidad);
                         fila.addCampo("COLEC_COMPREAL_COD_ENTIDAD", codEntidadItAgrup);
                         fila.addCampo("COLEC_COMPREAL_NUM_EXP", numExp);
                         fila.addCampo("COLEC_COMPREAL_EJERCICIO", ejercicio);
                         // Quitamos campo temporal para evitar fallo de inserci�n.
                         fila.removeCampo("NUM_ENTIDAD_ANEXO_I");
                         log.info("Porcentaje Realizacion : A�adido COLEC_COMPREAL_COD_ENTIDAD=" + codEntidadItAgrup + " a la fila de COLEC_COMPREAL_XCOLTH.");
                     }
                 }
                // Tabla de Ubicaciones.
                 if (tablaUbicaciones != null) {
                     for (MELanbide42FilaVO filaUbicciones : tablaUbicaciones.getFilas()) {
                         String Codpestana = filaUbicciones.getCampos().get("COLEC_UBIC_CT_NUM_ANEXO");
                         //String codColectivoUbicacion = MELanbide42GeneralUtils.mapearNroAnexoFormToCodColecBBDDUbicacion(nroAnexo);
                         String codEntidadItAgrup = MELanbide42GeneralUtils.getCodEntidadDsdMapNroPestanaFormCOLEC(mapaEntidadesAgrupaCodPestaAnexos,esAgrupacion,Codpestana);
                         log.info("Mapeada Entidad " + codEntidadItAgrup + " CodigoPesta�a : " + Codpestana + " - Entidad Principal : " + nuevoCodigoEntidad);
                         filaUbicciones.addCampo("COLEC_UBIC_CT_CODENTIDAD", codEntidadItAgrup);
                         //filaUbicciones.addCampo("COLEC_UBIC_CT_TIPO", codColectivoUbicacion);
                         filaUbicciones.addCampo("COLEC_UBIC_CT_NUMEXP", numExp);
                         // Mapeamos los valores S/N de desplegables desde Formulario
                         filaUbicciones.getCampos().put("COLEC_UBIC_CT_ESP_COMP_WIFI", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaUbicciones.getCampos().get("COLEC_UBIC_CT_ESP_COMP_WIFI")));
                         // Borramos el campo temporal para evitar error inserci�n
                         filaUbicciones.removeCampo("COLEC_UBIC_CT_NUM_ANEXO");
                         log.info("Ubicacion Mapeada. Etidad Padre " + nuevoCodigoEntidad + " Entidad : " + codEntidadItAgrup);
                     }
                 }
                 
                 if(tablaSolicitud!=null){
                     for (MELanbide42FilaVO filaSolicitud : tablaSolicitud.getFilas()) {
                         filaSolicitud.addCampo("COLEC_EXP_EJE", ejercicio);
                     }
                 }
                 // Mapeamos datos de certificados - Entidad principal, deben venir con codigo 0, ya se han mapeado los de la lista de agrupacion
                 if (tablaCertificadosCalidad != null){
                     for(MELanbide42FilaVO filaCertCalidad : tablaCertificadosCalidad.getFilasByFieldValue("NUM_ENTIDAD_ANEXO_I","0")){
                         filaCertCalidad.addCampo("ID_ENTIDAD",nuevoCodigoEntidad);
                         filaCertCalidad.getCampos().put("VALOR_SN_SOLICITUD", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaCertCalidad.getCampos().get("VALOR_SN_SOLICITUD")));
                         filaCertCalidad.removeCampo("NUM_ENTIDAD_ANEXO_I");
                     }
                 }
             } catch (SQLException ex) {
                 log.error("Error al preprarar los datos para la carga de M48 : " + ex.getMessage(), ex);
             }
         }
         if (log.isDebugEnabled()) {
             log.info("prepararDatosCOLECMod48() : END");
         }
        } catch (Exception ex){
            log.error("Error al preprarar los datos para la carga de M48 : " + ex.getMessage(), ex);
        } finally {
            
            adapt.devolverConexion(con);
        
        }
    }

    public static void prepararDatosTRECOMod88(MELanbide42XMLAltaExpediente xmlExpediente, MELanbide42TablaVO tablaInversiones, MELanbide42TablaVO tablaSubvenciones, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        
        if (log.isDebugEnabled()) {
             log.info("prepararDatosTRECOMod88() : BEGIN");
         }
        if (tablaInversiones != null) {
            for (MELanbide42FilaVO fila : tablaInversiones.getFilas()) {
                //Es campo obligatorio debe llevar 0 / 1
                String valor = fila.getCampos().get("pagada");
                valor = (valor!= null && !valor.isEmpty()?valor:"0");
                fila.getCampos().put("pagada", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(valor));
                log.info("Campo pagada - Mapeado " + fila.getCampos().get("pagada"));
            }
        } 
        
        if (tablaSubvenciones != null) {
            for (MELanbide42FilaVO fila : tablaSubvenciones.getFilas()) {
                //Es campo obligatorio debe llevar 0 / 1
                String valor = fila.getCampos().get("estado");
                valor = (valor != null && !valor.isEmpty() ? valor : "0");
                fila.getCampos().put("estado", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(valor));
                log.info("Campo pagada - Mapeado " + fila.getCampos().get("estado"));
            }
        } 
        if (log.isDebugEnabled()) {
            log.info("prepararDatosTRECOMod88() : END");
        }
    }

    public static void prepararDatosLPEEL(MELanbide42XMLAltaExpediente xmlExpediente, MELanbide42TablaVO tablaProyectos, MELanbide42TablaVO tablaPuestos, MELanbide42TablaVO tablaContrataciones, AdaptadorSQLBD adapt, String numExp, String codOrg) throws MELanbide42Exception, Exception {
        if (log.isDebugEnabled()) {
            log.info("prepararDatosLPEEL() : BEGIN");
        }
        MELanbide42DAO dao = MELanbide42DAO.getInstance();
        String seqName = MELanbide42Properties.getProperty("MELANBIDE81/MELANBIDE81_PROYECTOS/seqname");
        String prioridad = null;
        Connection con = null;
        
        try{
          con=  adapt.getConnection();
        for (MELanbide42FilaVO f : tablaProyectos.getFilas()) {
            try {
                // Recorro las filas de MELANBIDE81_PROYECTOS para insertar el ID generado via secuencia
                String nextval = dao.getSeqNextValue(seqName,con );
                f.addCampo("ID", nextval);
                // Recojo el valor de PRIORIDAD para insertar el ID en ID_PROYECTO en las filas que tengan ese valor en ID_PRIORIDAD_PROYECTO de las tablas del modulo
                prioridad = f.getCampos().get("PRIORIDAD");

                if (tablaPuestos != null) {
                    ArrayList<MELanbide42FilaVO> filasPuestos = tablaPuestos.getFilasByFieldValue("ID_PRIORIDAD_PROYECTO", prioridad);
                    for (MELanbide42FilaVO filaP : filasPuestos) {
                        filaP.addCampo("ID_PROYECTO", nextval);
                    }
                    if (log.isDebugEnabled()) {
                        log.info("A�adido ID_PROYECTO=" + nextval + " a todas las filas de MELANBIDE81_PUESTOS con PRIORIDAD=" + prioridad );
                    }
                } else {
                    log.debug("No existe tabla MELANBIDE81_PUESTOS");
                }

                if (tablaContrataciones != null) {
                    ArrayList<MELanbide42FilaVO> filasContrataciones = tablaContrataciones.getFilasByFieldValue("ID_PRIORIDAD_PROYECTO", prioridad);
                    for (MELanbide42FilaVO filaC : filasContrataciones) {
                        filaC.addCampo("ID_PROYECTO", nextval);
                    }
                    if (log.isDebugEnabled()) {
                        log.info("A�adido ID_PROYECTO=" + nextval + " a todas las filas de MELANBIDE81_CONTRATACIONES con PRIORIDAD=" + prioridad);
                    }
                } else {
                    log.debug("No existe tabla MELANBIDE81_CONTRATACIONES");
                }
            } catch (SQLException sqe) {
                log.error("Error al preprarar los datos para la carga de M81 : " + sqe.getMessage(), sqe);
            }
        }
        } catch (Exception ex){
            log.error("Error al preprarar los datos para la carga de M48 : " + ex.getMessage(), ex);
        } finally {
            
            adapt.devolverConexion(con);
        
        }
    }
    
    public static void prepararDatosCEMPMod32_filaCriteriosCentros(MELanbide42FilaVO filaCriteriosCentro, String nextvalUbicaciones, Connection con ) throws MELanbide42Exception, SQLException {
        MELanbide42DAO dao = MELanbide42DAO.getInstance();
        String queryGetCriterio = "select id  from ori_ce_criterios_eva "
                + " where codigo='" + filaCriteriosCentro.getCampos().get("codigoCriterio") + "' "
                + " and codigoorden='" + filaCriteriosCentro.getCampos().get("codigoCriterioOrden") + "'"
                + " and EJERCICIOCONVOCATORIA='" + filaCriteriosCentro.getCampos().get("ejercicioConvocatoria") + "'"
                ;
        String idCriterio = dao.prepararDatosGetStringByQuery(queryGetCriterio, "id", con);
        String queryGetCriterioOption = "select id from ori_ce_criterios_eva_opcion "
                + " where idcriteriofk='" + idCriterio + "' "
                + " and codigoorden='" + filaCriteriosCentro.getCampos().get("codigoCriterioOrdenOpcion") + "'";

        String idCriterioOpcion = dao.prepararDatosGetStringByQuery(queryGetCriterioOption, "id", con);

        filaCriteriosCentro.removeCampo("numeroAmbitoCentro");
        filaCriteriosCentro.removeCampo("codigoCriterio");
        filaCriteriosCentro.removeCampo("codigoCriterioOrden");
        filaCriteriosCentro.removeCampo("codigoCriterioOrdenOpcion");

        filaCriteriosCentro.addCampo("idCentro", nextvalUbicaciones);
        filaCriteriosCentro.addCampo("idcriterio", (idCriterio != null && !idCriterio.isEmpty() && !idCriterio.equalsIgnoreCase("null") ? idCriterio : "0"));
        filaCriteriosCentro.addCampo("idcriterioopcion", (idCriterioOpcion != null && !idCriterioOpcion.isEmpty() && !idCriterioOpcion.equalsIgnoreCase("null") ? idCriterioOpcion : "0"));
        //filaCriteriosCentro.getCampos().put("centroSeleccionOpcion", MELanbide42GeneralUtils.parsearTextoSI_S_NO_N_aNumero1_0(filaCriteriosCentro.getCampos().get("centroSeleccionOpcion")));
    }
    
    private static void prepararDatosCEMPMod32_criteriosSinCentro(MELanbide42XMLAltaExpediente xmlExpediente, MELanbide42TablaVO tablaCriteriosCentro, MELanbide42TablaVO tablaUbicaciones, String nuevoCodigoEntidad, Connection con) throws MELanbide42Exception,SQLException{
            MELanbide42DAO dao = MELanbide42DAO.getInstance();
            List<String> listaCriteriosSinCentro = new ArrayList<String>();
            if (tablaCriteriosCentro != null) {
                // Obtengo lo codigos de centroAmbito criterio
                for (MELanbide42FilaVO filaCritCentroAnonimo : tablaCriteriosCentro.getFilas()) {
                    String codigoAmbitoCentroAnoni = filaCritCentroAnonimo.getCampos().get("numeroAmbitoCentro");
                    if (codigoAmbitoCentroAnoni != null && !codigoAmbitoCentroAnoni.isEmpty()) {
                        if (!listaCriteriosSinCentro.contains(codigoAmbitoCentroAnoni)) {
                            listaCriteriosSinCentro.add(codigoAmbitoCentroAnoni);
                        }
                    }
                }
                // Leemos los criterios anonimos y creamos el centro Generico en ese ambito
                for (String criterioSinCentro : listaCriteriosSinCentro) {
                    log.info("Tratando Criterio sin centro asociado: " + criterioSinCentro);
                    //Crear el centro 
                    boolean establaNueva = false;
                    if (tablaUbicaciones == null) {
                        tablaUbicaciones = new MELanbide42TablaVO("ORI_CE_UBIC", xmlExpediente.getListaModulos().get(0));//new MELanbide42ModuloVO("MELANBIDE32")
                        establaNueva = true;

                    }
                    MELanbide42FilaVO filaUbicaciones = new MELanbide42FilaVO(tablaUbicaciones);
                    String seqNameUbicaciones = MELanbide42Properties.getProperty("MELANBIDE32/ORI_CE_UBIC/seqname");
                    String nextvalUbicaciones = dao.getSeqNextValue(seqNameUbicaciones, con);
                    filaUbicaciones.addCampo("ORI_CE_UBIC_COD", nextvalUbicaciones);
                    filaUbicaciones.addCampo("ORI_ENT_COD", nuevoCodigoEntidad);
                    filaUbicaciones.addCampo("MUN_PAI", ConstantesMeLanbide42.CODIGO_PAIS_ESPANA);
                    filaUbicaciones.addCampo("PRV_PAI", ConstantesMeLanbide42.CODIGO_PAIS_ESPANA);
                    filaUbicaciones.addCampo("ORI_CE_DIRECCION", "Desconocida/Ezezagutu");
                    String[] datosCodigo = criterioSinCentro.split("_");
                    if (datosCodigo != null && datosCodigo.length == 3) {
                        filaUbicaciones.addCampo("ORI_AMB_COD", datosCodigo[1]);
                    }
                    if (criterioSinCentro.startsWith("1")) {
                        filaUbicaciones.addCampo("ORI_CE_ESPECIAL", "S");

                    } else if (criterioSinCentro.startsWith("3")) {
                        filaUbicaciones.addCampo("ORI_CE_DISTRIBUIDO", "S");
                    }
                    tablaUbicaciones.addFila(filaUbicaciones);
                    if (establaNueva) {
                        xmlExpediente.getListaModulos().get(0).addTabla(tablaUbicaciones);
                    }

                    for (MELanbide42FilaVO filaCriteriosCentroAnonimos : tablaCriteriosCentro.getFilasByFieldValue("numeroAmbitoCentro", criterioSinCentro)) {
                        prepararDatosCEMPMod32_filaCriteriosCentros(filaCriteriosCentroAnonimos, nextvalUbicaciones, con);
                    }
                }
            }
    }

    public static void prepararDatosININMod90(MELanbide42TablaVO tablaFacturas) {
        if(tablaFacturas != null){
            // Copiamos los datos de la solicitud a los nuevos campos validados:
            for (MELanbide42FilaVO filaFactura :tablaFacturas.getFilas()){
                if(filaFactura.getCampos().get("IMPORTEBASE") != null)
                    filaFactura.addCampo("IMPORTEBASE_VALIDADO", filaFactura.getCampos().get("IMPORTEBASE"));
                if(filaFactura.getCampos().get("PORCIVA") != null)
                    filaFactura.addCampo("PORCIVA_VALIDADO", filaFactura.getCampos().get("PORCIVA"));
                if(filaFactura.getCampos().get("IMPORTEIVA") != null)
                    filaFactura.addCampo("IMPORTEIVA_VALIDADO", filaFactura.getCampos().get("IMPORTEIVA"));
                if(filaFactura.getCampos().get("PRORRATA") != null)
                    filaFactura.addCampo("PRORRATA_VALIDADO", filaFactura.getCampos().get("PRORRATA"));
            }

        }
    }
}
