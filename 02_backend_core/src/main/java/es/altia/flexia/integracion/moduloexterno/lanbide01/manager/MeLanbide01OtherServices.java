/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.manager;

import es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao.Melanbide01Dao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Melanbide01TipoCampoSuplementarioEnum;
import es.altia.flexia.integracion.moduloexterno.lanbide01.validator.MeLanbide01CamposSupleExpediente;
import es.altia.util.conexion.AdaptadorSQL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MeLanbide01OtherServices {
    
    private final Logger log = LogManager.getLogger(MeLanbide01OtherServices.class);
    // Formateador de Fecha RegistroLog
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFecha_dd_MM_yyyy = new SimpleDateFormat("dd/MM/yyyy");
    
    private final Melanbide01Dao melanbide01Dao = new Melanbide01Dao();
    private final MeLanbide01CamposSupleExpediente meLanbide01CamposSupleExpediente = new MeLanbide01CamposSupleExpediente();
    
    public void validarActualizarFechaNacimientoPersSustYContr(String numeroExpediente,AdaptadorSQL adaptador) throws Exception{
        Connection con = null;
        try {
            con = adaptador.getConnection();
            this.validarActualizarFechaNacimientoPersSustYContr(numeroExpediente, con);
        } catch (NumberFormatException e) {
            log.error("SQLException  validarActualizarFechaNacimientoPersSustYContr " + e.getMessage(), e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al validarActualizarFechaNacimientoPersSustYContr " + ex.getMessage(), ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("validarActualizarFechaNacimientoPersSustYContr() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: validarActualizarFechaNacimientoPersSustYContr - " + e.getMessage());
            }
        }        
    }
    
    public void validarActualizarFechaNacimientoPersSustYContr(String numeroExpediente,Connection con){
        try {
            List<Map<String, String>> datosTratar = melanbide01Dao.getDatosCargarValidarEdadPersonasContraYSusti(numeroExpediente, con);
            for (Map<String, String> map : datosTratar) {
                log.info("Procesamos: " + " ==> " +  Arrays.toString(map.values().toArray()) );
                if("INSERT".equalsIgnoreCase(map.get("TIPOOPERACION"))
                        || "UPDATE".equalsIgnoreCase(map.get("TIPOOPERACION"))){
                    log.info("Dato CS Actualizado : ? "  
                            + meLanbide01CamposSupleExpediente.grabarCampoSuplementarioExpediente(Integer.valueOf(map.get("CODIGOORGANIZACION"))
                            ,map.get("CAMPOSUPLEMENTARIO")
                            ,map.get("EDADCALCULADA")
                            ,Integer.valueOf(Melanbide01TipoCampoSuplementarioEnum.NUMERICO.getCodigo())
                            ,numeroExpediente)                          
                    );
                }else{
                    log.info("No se realiza niguna operacion: las fechas son iguales o la fecha del tercero es null en la tabla CS Terceros (T_TIPOFECHA) de TFECHANACIMIENTO");
                }
            }
        } catch (Exception e) {
            log.error("Error al cargar/validar fechas de nacimiento de personas sustituida/contrada contra campos suplementarios " + e.getMessage(), e);
        }
    }
    
}
