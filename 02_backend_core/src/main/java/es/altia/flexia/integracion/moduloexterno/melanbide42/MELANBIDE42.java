package es.altia.flexia.integracion.moduloexterno.melanbide42;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.flexia.integracion.moduloexterno.melanbide42.i18n.MeLanbide42I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide42.manager.MELanbide42Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide42.util.ConstantesMeLanbide42;
import es.altia.flexia.integracion.moduloexterno.melanbide42.util.MELanbide42Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42ModuloVO;
import es.altia.flexia.integracion.moduloexterno.melanbide42.vo.MELanbide42XMLAltaExpediente;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author mikel
 */
public class MELANBIDE42 extends ModuloIntegracionExterno{
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE42.class);
    
    private static MELanbide42Manager manager = new MELanbide42Manager();
    
    public void cargarExpedienteExtension(int codOrg, String numExp, String xml) throws Exception {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteExtension() : BEGIN");
        try {  
            log.debug("funcion cargarExpedienteExtension()");
            MELanbide42XMLAltaExpediente xmlExpediente = manager.parsearExpedienteElectronico(xml);
            
            MELanbide42ModuloVO modulo = xmlExpediente.getListaModulos().get(0);
            String codigo = modulo.getCodigo();
            
            AdaptadorSQLBD adaptador = manager.getAdaptSQLBD(String.valueOf(codOrg));
            
            log.debug("codigo: " + codigo);
            if (codigo.equals("MELANBIDE41")) {
                // Caso especial. MELANBIDE41 y MELANBIDE50 funcionan igual
                // Duplicamos código, tener en cuenta si se toca algo
                // Debemos comprobar si en realidad es un MELANBIDE41 o un MELANBIDE50
                // RGCF RGCFM RGCFB
                String tipoModif = xmlExpediente.getCamposVariables().get("TIPOMODIF");
                if (tipoModif == null) {
                    throw new MELanbide42Exception("El campo variable TIPOMODIF no puede estar vacío");
                }
                if ("ALTA".equals(tipoModif)) {
                    cargarExpedienteMELANBIDE41(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
                } else {
                    // Cualquier otro caso es módulo 50
                    log.debug("ANTES =================================>" + xml);
                    xml = xml.replaceAll("MELANBIDE41", "MELANBIDE50");
                    log.debug("=================================>    DESPUES" + xml);
                    xmlExpediente = manager.parsearExpedienteElectronico(xml);
                    cargarExpedienteMELANBIDE50(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
                }

            } else if (codigo.equals("MELANBIDE01")) { // CONCM
                cargarExpedienteMELANBIDE01(xmlExpediente, adaptador, numExp, codOrg);
            } else if (codigo.equals("MELANBIDE10")) { // DISCP
                cargarExpedienteMELANBIDE10(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            }  else if (codigo.equals("MELANBIDE11")) { // DEM50
                cargarExpedienteMELANBIDE11(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE14")) { // PFSE
                cargarExpedienteMELANBIDE14(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            }else if (codigo.equals("MELANBIDE33")) { // SEI
                cargarExpedienteMELANBIDE33(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE50")) { // RGCFM RGCFB
                throw new MELanbide42Exception("El código MELANBIDE50 no está soportado directamente en el XML");
            } else if (codigo.equals("MELANBIDE45")) { // RGEF
                cargarExpedienteMELANBIDE45(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE54")) { // AACC
                cargarExpedienteMELANBIDE54(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE58")) { // CEESC
                cargarExpedienteMELANBIDE58(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE61") || codigo.equals("REPLE")) {//melanbide61
                cargarExpedienteMELANBIDE61(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE65")) { // UAAP
                cargarExpedienteMELANBIDE65(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE66")) { // APEC
                cargarExpedienteMELANBIDE66(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE32")) { //  CEMP
                cargarExpedienteMELANBIDE32(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE47")) { // ORI14
                cargarExpedienteMELANBIDE47(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE48")) { // COLEC
                cargarExpedienteMELANBIDE48(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE72")) { // DECEX
                cargarExpedienteMELANBIDE72(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE75")) { // COCUR
                cargarExpedienteMELANBIDE75(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE76")) { // DLDUR
                cargarExpedienteMELANBIDE76(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE69")) { // APES
                cargarExpedienteMELANBIDE69(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE35")) { // ECA
                cargarExpedienteMELANBIDE35(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE80")) { // AEXCE
                cargarExpedienteMELANBIDE80(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE81")) { // LPEEL
                cargarExpedienteMELANBIDE81(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE82")) { // GEL
                cargarExpedienteMELANBIDE82(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE84")) { // ENTAP
                cargarExpedienteMELANBIDE84(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE85")) { // PEX
                cargarExpedienteMELANBIDE85(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE86")) { // IKER
                cargarExpedienteMELANBIDE86(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE88")) { // TRECO
                cargarExpedienteMELANBIDE88(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE89")) { // GO
                cargarExpedienteMELANBIDE89(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE90")) { // ININ
                cargarExpedienteMELANBIDE90(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE91")) { // IGCEE
                cargarExpedienteMELANBIDE91(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE67")) { // LAK
                cargarExpedienteMELANBIDE67(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE12")) { // PRACT
                cargarExpedienteMELANBIDE12(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE15")) { // CATP
                cargarExpedienteMELANBIDE15(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE16")) { // LAKOF
                cargarExpedienteMELANBIDE16(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE17")) { // LAKCC
                cargarExpedienteMELANBIDE17(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            } else if (codigo.equals("MELANBIDE18")) { // FRACC
                cargarExpedienteMELANBIDE18(xmlExpediente, adaptador, numExp, String.valueOf(codOrg));
            }
            
            else {
                throw new MELanbide42Exception("El módulo " + modulo.getCodigo() + " no está definido");
            }

        } catch (MELanbide42Exception mle){
            // TODO GESTION ERRORES
            log.error("Error al parsear XML", mle);
            String error = "Error en la funcion cargarExpedienteExtension: " + mle.getMessage();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_01");
            errorB.setMensajeError("Error al parsear XML");
            errorB.setSituacion("cargarExpedienteExtension");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
            throw new MELanbide42Exception(mle.getMessage());
//           throw mle;
        } catch (Exception e){
            throw e;
        }
        if(log.isDebugEnabled()) log.debug("cargarExpedienteExtension() : END");
    }
    
    public void cargarExpedienteMELANBIDE10(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE10() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE10() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE10", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE10: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE10: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE10");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE41(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE41() : BEGIN");
        try {
            manager.realizarCalculosMELANBIDE41(xmlExpediente, adapt);
            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);
        } catch (MELanbide42Exception mle){
            // TODO GESTION ERRORES
            log.error("Error al cargar expediente MELANBIDE41", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE41: " + mle.getMessage();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE41");
            errorB.setSituacion("cargarExpedienteMELANBIDE41");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE41", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE41: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE41");
            errorB.setSituacion("cargarExpedienteMELANBIDE41");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE41() : END");
    }

    public void cargarExpedienteMELANBIDE45(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE45() : BEGIN");
        try
        {
            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error al cargar expediente MELANBIDE45", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE45: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE45");
            errorB.setSituacion("cargarExpedienteMELANBIDE45");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE45() : END");
        
    }

    public void cargarExpedienteMELANBIDE50(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE50() : BEGIN");
        try {
            manager.realizarCalculosMELANBIDE50(xmlExpediente, adapt);
            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);
        } catch (MELanbide42Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE50", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE50: " + mle.getMessage();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE50");
            errorB.setSituacion("cargarExpedienteMELANBIDE50");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE50", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE50: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE50");
            errorB.setSituacion("cargarExpedienteMELANBIDE50");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE50() : END");
    }

    public void cargarExpedienteMELANBIDE54(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE54() : BEGIN");
        try
        {
        manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);

        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE54() : END");
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE54", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE54: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE54");
            errorB.setSituacion("cargarExpedienteMELANBIDE54");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE58(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) throws Exception {
        log.debug("cargarExpedienteMELANBIDE58() : BEGIN");

        MeLanbide42I18n meLanbide42I18n = MeLanbide42I18n.getInstance();
        try {

            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);
            int idiomaUsuario = 1;
// recalculo subvencion
            String mensajeRecalculo = manager.recalcularSMI(xmlExpediente, numExp, codOrg);

            if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide42.ERROR_RECALCULO)) {
                alertaPopup("A", meLanbide42I18n.getMensaje(idiomaUsuario, "error.recalculoSMI"));
            } else if (mensajeRecalculo.equalsIgnoreCase(ConstantesMeLanbide42.NOT_FOUND_SMI)) {
                alertaPopup("A", meLanbide42I18n.getMensaje(idiomaUsuario, "error.notFoundSMI"));
            }

            log.debug("cargarExpedienteMELANBIDE58() : END");
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE58", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE58: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE58");
            errorB.setSituacion("cargarExpedienteMELANBIDE58");
            MELanbide42Manager.grabarError(errorB, error, error, numExp);
            throw mle;
        }
    }
    
    public void alertaPopup(String tipo, String mensaje) throws Exception {
        log.debug("alertaPopup() : BEGIN");
        try {
            ScriptEngineManager scrManager = new ScriptEngineManager();
            ScriptEngine engine = scrManager.getEngineByName("JavaScript");
            Invocable inv = (Invocable) engine;

            engine.eval(new FileReader("/scripts/general.js"));

            inv.invokeFunction("jsp_alerta", tipo, mensaje);

            log.debug("alertaPopup() : END");
        } catch (FileNotFoundException ex) {
            log.error("Error en la funcion alertaPopup", ex);
        } catch (NoSuchMethodException ex) {
            log.error("Error en la funcion alertaPopup", ex);
        } catch (ScriptException ex) {
            log.error("Error en la funcion alertaPopup", ex);
        }
    }
    
    public void cargarExpedienteMELANBIDE61(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE61() : BEGIN");
        try
        {
            //manager.realizarCalculosMELANBIDE61(xmlExpediente, adapt);
            manager.anadirCamposMelanbide61Pexco(xmlExpediente, numExp, codOrg); 
            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);

        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE61() : END");
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE61", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE61: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE61");
            errorB.setSituacion("cargarExpedienteMELANBIDE61");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    public void cargarExpedienteMELANBIDE65(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if (log.isDebugEnabled()) {
            log.debug("cargarExpedienteMELANBIDE65() : BEGIN");
        }
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);

            //manager.grabarAnexosUAAP(numExp, codOrg, adapt);
            log.debug("cargarExpedienteMELANBIDE65() : END");
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE65", mle);
            String error = "Error en la funcion cargarExpedienteMELANBIDE65: " + mle.toString();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE65");
            errorB.setSituacion("cargarExpedienteMELANBIDE65");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    public void cargarExpedienteMELANBIDE66(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) throws Exception {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE66() : BEGIN");
        try
        {            
            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg); 
            if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE66() : END");
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            /*log.error("Error en la funcion cargarExpedienteMELANBIDE66", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE66: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE66");
            errorB.setSituacion("cargarExpedienteMELANBIDE66");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);*/
            throw mle;
        }
    }
    
    
    
    public void cargarExpedienteMELANBIDE33(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE33() : BEGIN");
        try{
        manager.anadirCamposMelanbide33(xmlExpediente, numExp, codOrg); 

            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE33", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE33: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE33");
            errorB.setSituacion("cargarExpedienteMELANBIDE33");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }

        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE33() : END");
    }
    
    public void cargarExpedienteMELANBIDE01(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, int codOrganizacion) throws Exception{
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE01() : BEGIN");
        try {
            manager.realizarCalculosMELANBIDE01(xmlExpediente, adapt, numExp, codOrganizacion);
            log.debug("Primera tabla: " + xmlExpediente.getListaModulos().get(0).getTablas().get(0).getNombre());
            //xmlExpediente.getListaModulos().get(0).getTablas().get(0).getNombre();
            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrganizacion+"");
        } catch (MELanbide42Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE01", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE01: " + mle.getMessage();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE01");
            errorB.setSituacion("cargarExpedienteMELANBIDE01");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
            throw mle;
        }
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE01() : END");
    }
    
    public void cargarExpedienteMELANBIDE32(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE32() : BEGIN");
        try
        {
            manager.prepararDatosCEMPMod32(xmlExpediente, adapt, numExp, codOrg);
            /* Preparados los datos con sus PK y FK que hay que incluir. Hay que asegurarse que el orden de los nodos sea:
            ORI_ENTIDAD
            ORI_CE_TRAYECTORIA
            ORI14_CE_UBIC --> LLaves foraneas de ENTIDAD y AMBITOS.
            ORI_AMBITOS_CE_SOLICITADOS
            ori_ce_criterios_centro  --> lLAVES FORANEAS DESDE UBICACIONES
            */
            MELanbide42XMLAltaExpediente xMLAltaExpedienteNodosOrdenadosCEMP = manager.ordenarNodosXML_CEMP(xmlExpediente);
            manager.insertarExpedienteElectronico(xMLAltaExpedienteNodosOrdenadosCEMP, adapt, numExp, codOrg);
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE32() : END");
        }
        catch (MELanbide42Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE32 " + mle.getMessage(), mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE32 => " +
                    mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE32");
            errorB.setSituacion("cargarExpedienteMELANBIDE32");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE32 " + mle.getMessage(), mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE32: " + mle.getMessage() + " - " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE32");
            errorB.setSituacion("cargarExpedienteMELANBIDE32");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE47(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE47() : BEGIN");
        try
        {
            manager.prepararDatosORI14Mod47(xmlExpediente, adapt, numExp, codOrg);
            /* Preparados los datos con sus PK y FK que hay que incluir. Hay que asegurarse que el orden de los nodos sea:
            ORI14_ENTIDAD
            ORI14_ASOCIACION_ENT (Que debe guardar en ORI14_ASOCIACION,  son los datos de la entidad principal para la tabla Asociacion)
            ORI14_ASOCIACION (Esta es la lista de entidades que conforma la asociacion en caso que lo sea.
            ORI14_ORI_TRAYECTORIA
            ORI14_ORI_UBIC
            ORI14_AMBITOS_SOLICITADOS
            ORI_OTROS_PROGRAMAS
            ORI_ACTIVIDADES
            ORI_TRAYECTORIA_ENTIDAD (UNIFICA DESDE CONVOCARORIA 2021 TRAYECTORIA, OTROS PROGRMAS Y ACTIVIDADES
            */
            MELanbide42XMLAltaExpediente xMLAltaExpedienteNodosOrdenadosORI14 = manager.ordenarNodosXML_ORI14(xmlExpediente);
            //manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);
            manager.insertarExpedienteElectronico(xMLAltaExpedienteNodosOrdenadosORI14, adapt, numExp, codOrg);
            if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE47() : END");
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE47", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE47: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE47");
            errorB.setSituacion("cargarExpedienteMELANBIDE47");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE48(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE48() : BEGIN");
        try
        {
            manager.prepararDatosCOLECMod48(xmlExpediente, adapt, numExp, codOrg);
            manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);
            if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE48() : END");
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE48", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE48: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE48");
            errorB.setSituacion("cargarExpedienteMELANBIDE47");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
   
   
    public static void main(String[] args) throws IOException{
        FileReader fReader = null;
        try
        {
            //File file = new File ("C:\\Users\\leires.VITORIA\\Downloads\\FLX_DATOS_INTEGRACION_SOLICITUD.xml");
            //File file = new File("D:\\Proyectos\\Flexia\\S75\\LAN68_SEI\\FLX_DATOS_INTEGRACION_SOLICITUD_SEI.xml");
           // File file = new File("C:\\FLX_DATOS_INTEGRACION_SOLICITUD_REGC_DES.xml");
            // File file = new File("C:\\FLEXIA_XML\\FLX_DATOS_INTEGRACION_SOLICITUD_CEECS.xml");
            //File file = new File("C:\\FLEXIA_XML\\FLX_DATOS_INTEGRACION_SOLICITUD_859.xml");
            //File file = new File("C:\\FLEXIA_XML\\FLX_DATOS_INTEGRACION_SOLICITUD_UAAP.xml");
            //File file = new File("C:\\FLEXIA_XML\\EJEMPLO_XML_DATOS_INTEGRACION_SOLICITUD_REPLE.xml");           
            File file = new File("C:\\FLEXIA_XML\\FLX_DATOS_INTEGRACION_SOLICITUD_REPLE.xml");           
            fReader = new FileReader (file);
            BufferedReader reader = new BufferedReader(fReader);
            String         line = null;
            StringBuilder  stringBuilder = new StringBuilder();
            String         ls = System.getProperty("line.separator");

            while( ( line = reader.readLine() ) != null ) {
                stringBuilder.append( line );
                stringBuilder.append( ls );
            }

            String xml = stringBuilder.toString();

            //String[] sqlargs = {"ORACLE", "", "", "", ""};
            //AdaptadorSQLBD sql = new AdaptadorSQLBD(args);
            //new MELANBIDE42().cargarExpedienteExtension(0, "2016/CEECS/000001", xml);
           // new MELANBIDE42().cargarExpedienteExtension(0, "2016/UAAP/000001", xml);
             new MELANBIDE42().cargarExpedienteExtension(0, "2016/REPLE/000001", xml);
        }
        catch(Exception ex)
        {
            log.debug("Error en funciï¿?n main: " + ex);
        }
        finally
        {
            if (fReader != null) fReader.close();
        }
    }

    public void cargarExpedienteMELANBIDE72(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE72() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);

            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE72() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE72", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE72: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE72: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE72");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE75(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE75() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);

            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE75() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE75", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE75: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE75: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE75");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE76(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE76() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);

            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE76() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE76", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE76: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE76: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE76");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE69(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adapt, String numExp, String codOrg) {
        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE69() : BEGIN");
        try
        {
        manager.insertarExpedienteElectronico(xmlExpediente, adapt, numExp, codOrg);

        if(log.isDebugEnabled()) log.debug("cargarExpedienteMELANBIDE69() : END");
        }
        catch (Exception mle){
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE69", mle);
            
            String error = "Error en la funcion cargarExpedienteMELANBIDE69: " + mle.toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE69");
            errorB.setSituacion("cargarExpedienteMELANBIDE69");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE35(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE35() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);

            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE35() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE35", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE35: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE35: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE35");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE80(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE80() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);

            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE80() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE80", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE80: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE80: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE80");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE81(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) throws Exception{
        log.info("cargarExpedienteMELANBIDE81() : BEGIN");
        try {
            manager.prepararDatosLPELLMod81(xmlExpediente, adaptador, numExp, codOrg);
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE81() : END");
            }
        } catch (Exception mle) {
            log.error("Error en la funcion cargarExpedienteMELANBIDE81", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE81: " + mle.toString();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE81: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE81");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
            throw new MELanbide42Exception("Error al grabar en el expediente: " + mle.getMessage());

        }
    }

    public void cargarExpedienteMELANBIDE82(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) throws Exception{
        log.info("cargarExpedienteMELANBIDE82() : BEGIN");
        try {
            manager.anadirCampoPuestoGEL(xmlExpediente, numExp, codOrg);
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);

            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE82() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE82", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE82: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE82: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE82");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
            throw new MELanbide42Exception("Error al grabar en el expediente: " + mle.getMessage());

        }
    }
    
    public void cargarExpedienteMELANBIDE84(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) throws Exception{
        log.info("cargarExpedienteMELANBIDE84() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);

            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE84() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE84", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE84: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE84: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE84");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
                   throw new MELanbide42Exception("Error al grabar en el expediente: " + mle.getMessage());
        }
    }

    public void cargarExpedienteMELANBIDE85(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) throws Exception{
        log.info("cargarExpedienteMELANBIDE85() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);

            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE85() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE85", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE85: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE85: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE85");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
            throw new MELanbide42Exception("Error al grabar en el expediente: " + mle.getMessage());

        }
    }
    
    public void cargarExpedienteMELANBIDE86(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) throws Exception{
        log.info("cargarExpedienteMELANBIDE86() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);

            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE86() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE86", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE86: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE86: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE86");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
            throw new MELanbide42Exception("Error al grabar en el expediente: " + mle.getMessage());

        }
    }
    
    
    public void cargarExpedienteMELANBIDE88(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE88() : BEGIN");
        try {
            //manager.prepararDatosTRECOMod88(xmlExpediente, adaptador, numExp, codOrg);
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE88() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE88", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE88: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE88: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE88");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE89(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE89() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE89() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE89", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE89: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE89: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE89");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE90(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE90() : BEGIN");
        try {
            manager.prepararDatosININMod90(xmlExpediente);
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE90() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE90", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE90: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE90: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE90");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    public void cargarExpedienteMELANBIDE91(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE91() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE91() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE91", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE91: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE91: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE91");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    public  void cargarExpedienteMELANBIDE11(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE11() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE11() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE11", mle);
            String error = "Error en la funcion cargarExpedienteMELANBIDE11: " + mle.toString();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE11: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE11");
            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE14(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE14() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE14() : END");
            }
        } catch (Exception mle) {
            log.error("Error en la funcion cargarExpedienteMELANBIDE14", mle);
            String error = "Error en la funcion cargarExpedienteMELANBIDE14: " + mle.toString();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE14: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE14");
            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    private void cargarExpedienteMELANBIDE67(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE67() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE67() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE67", mle);
            String error = "Error en la funcion cargarExpedienteMELANBIDE67: " + mle.toString();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE67: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE67");
            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
    
    public void cargarExpedienteMELANBIDE12(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE12() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE12() : END");
            }
        } catch (Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE12", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE12: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE12: " + mle.getMessage() );
            errorB.setSituacion("cargarExpedienteMELANBIDE12");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    public void cargarExpedienteMELANBIDE15(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE15() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE15() : END");
            }
        } catch (MELanbide42Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE15 - CATP ", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE15: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE15 - CATP: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE15");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        } catch (SQLException mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE15 - CATP ", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE15: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE15 - CATP: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE15");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    private void cargarExpedienteMELANBIDE16(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE16() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE16() : END");
            }
        } catch (MELanbide42Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE16 - LAKOF ", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE16: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE16 - LAKOF: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE16");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        } catch (SQLException mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE16 - LAKOF ", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE16: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE16 - LAKOF: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE16");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    private void cargarExpedienteMELANBIDE17(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE17() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE17() : END");
            }
        } catch (MELanbide42Exception mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE17 - LAKCC ", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE17: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE17 - LAKCC: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE17");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        } catch (SQLException mle) {
            // TODO GESTION ERRORES
            log.error("Error en la funcion cargarExpedienteMELANBIDE17 - LAKCC ", mle);

            String error = "Error en la funcion cargarExpedienteMELANBIDE17: " + mle.toString();

            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE17 - LAKCC: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE17");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }

    private void cargarExpedienteMELANBIDE18(MELanbide42XMLAltaExpediente xmlExpediente, AdaptadorSQLBD adaptador, String numExp, String codOrg) {
        log.info("cargarExpedienteMELANBIDE18() : BEGIN");
        try {
            manager.insertarExpedienteElectronico(xmlExpediente, adaptador, numExp, codOrg);
            if (log.isDebugEnabled()) {
                log.info("cargarExpedienteMELANBIDE18() : END");
            }
        } catch (MELanbide42Exception mle) {
            log.error("Error en la funcion cargarExpedienteMELANBIDE18 - FRACC ", mle);
            String error = "Error en la funcion cargarExpedienteMELANBIDE18: " + mle.toString();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE17 - FRACC: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE18");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        } catch (SQLException mle) {
            log.error("Error en la funcion cargarExpedienteMELANBIDE18 - FRACC ", mle);
            String error = "Error en la funcion cargarExpedienteMELANBIDE18: " + mle.toString();
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("TELEMATICO_02");
            errorB.setMensajeError("Error al cargar expediente MELANBIDE17 - FRACC: " + mle.getMessage());
            errorB.setSituacion("cargarExpedienteMELANBIDE18");

            MELanbide42Manager.grabarError(errorB, error, error, numExp);
        }
    }
}
