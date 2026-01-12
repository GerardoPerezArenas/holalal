/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.lanbide01.validator;

import es.altia.flexia.integracion.moduloexterno.lanbide01.exception.MeLanbide01Exception;
import es.altia.flexia.integracion.moduloexterno.lanbide01.manager.MeLanbide01Manager;
import es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao.MeLanbide01DatosCalculoDao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConnectionUtils;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Melanbide01DecretoExpedienteEnum;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.MeLanbide01Constantes;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.MeLanbide01RolesCONCMEnum;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Melanbide01SexoPersonaEnum;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.Utilities;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.AlarmaVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.DatosCalculoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.ExpedienteMeLanbide01VO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01Decreto;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.InteresadoExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author INGDGC
 */
public class MeLanbide01ValidatorUtils {
    
    private final Logger log = LogManager.getLogger(MeLanbide01Validator.class);
    // Formateador de Fecha RegistroLog
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFecha_dd_MM_yyyy = new SimpleDateFormat("dd/MM/yyyy");
    private final MeLanbide01CamposSupleExpediente meLanbide01CamposSupleExpediente = new MeLanbide01CamposSupleExpediente();
    
    public Calendar getCampoSuplementarioFecNacPersonaDependiente(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioFecNacPersonaDependiente() : BEGIN");
        Calendar valor = null;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/FECNAPDEPEN", MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + "/PANTALLA_EXPEDIENTE/TIPO/FECNAPDEPEN", MeLanbide01Constantes.FICHERO_CONFIGURACION);
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorFecha();
        }//if(salida.getStatus() == 0)
        log.info("getCampoSuplementarioFecNacPersonaDependiente() : END");
        return valor;
    }
    
    public Calendar getCampoSuplementarioFecInicioAccionSubvConcedida(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioFecInicioAccionSubvConcedida() : BEGIN");
        Calendar valor = null;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.CAMPO_INICIO_CONTRATO, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_CAMPO_INICIO_CONTRATO, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            valor = Calendar.getInstance();
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorFecha();
        }//if(salida.getStatus() == 0)
        log.info("getCampoSuplementarioFecInicioAccionSubvConcedida() : END");
        return valor;
    }
    
    public Calendar getCampoSuplementarioFecFinAccionSubvConcedida(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioFecInicioAccionSubvConcedida() : BEGIN");
        Calendar valor = null;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.CAMPO_FIN_CONTRATO, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_CAMPO_FIN_CONTRATO, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            valor = Calendar.getInstance();
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorFecha();
        }//if(salida.getStatus() == 0)
        log.info("getCampoSuplementarioFecInicioAccionSubvConcedida() : END");
        return valor;
    }
    
    public String getCampoSuplementarioActividadSubvencionada(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioActividadSubvencionada() : BEGIN");
        }
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();        
        String campo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + MeLanbide01Constantes.FICHERO_CONFIGURACION + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/ACTIVIDAD_SUBVENCIONADA", MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + MeLanbide01Constantes.FICHERO_CONFIGURACION + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/TIPO/ACTIVIDAD_SUBVENCIONADA", MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String valor = "";
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorDesplegable();
        }//if(salida.getStatus() == 0)
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioActividadSubvencionada() : END");
        }
        return valor;
    }//getCampoSuplementarioActividadSubvencionada
    
    public String getCampoSuplementarioRelancionPersonaDependiente(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioRelancionPersonaDependiente() : BEGIN");
        }
        String valor = new String();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + MeLanbide01Constantes.FICHERO_CONFIGURACION + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/NOMBRE_CAMPO/RELACPERSDEPEN", MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + MeLanbide01Constantes.FICHERO_CONFIGURACION + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/TIPO/RELACPERSDEPEN", MeLanbide01Constantes.FICHERO_CONFIGURACION);
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorDesplegable();
        }//if(salida.getStatus() == 0)
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioRelancionPersonaDependiente() : END");
        }
        return valor;
    }//getCampoSuplementarioRelancionPersonaDependiente
    
    public String getCampoSuplementarioReducPersSust(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioReducPersSust() : BEGIN");
        }
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.REDUCPERSSUST, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_REDUCPERSSUST, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String valor = "";
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorNumero();
        } else {
            //throw new MeLanbide01Exception("Se ha producido un error recuperando el campo suplementario de reduccion de la persona sustituida : " + salida.getStatus() + " - " +salida.getDescStatus());
        }//if(salida.getStatus() == 0)
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioReducPersSust() : END");
        }
        return valor;
    }//getCampoSuplementarioReducPersSust

    public String getCampoSuplementarioJornPersSust(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioJornPersSust() : BEGIN");
        }
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.JORNPERSSUST, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_JORNPERSSUST, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String valor = "";
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorNumero();
        } else {
            //throw new MeLanbide01Exception("Se ha producido un error recuperando el campo suplementario de jornada de la persona sustituida : " + salida.getStatus() + " - " +salida.getDescStatus());
        }//if(salida.getStatus() == 0)
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioJornPersSust() : END");
        }
        return valor;
    }//getCampoSuplementarioReducPersSust

    
    public String getCampoSuplementarioJornPersCont(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioJornPersCont() : BEGIN");
        }
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.JORNPERSCONT, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_JORNPERSCONT, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String valor = "";
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorNumero();
        } else {
            //throw new MeLanbide01Exception("Se ha producido un error recuperando el campo suplementario de jornada de la persona contratada : " + salida.getStatus() + " - " +salida.getDescStatus());
        }//if(salida.getStatus() == 0)
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioJornPersCont() : END");
        }
        return valor;
    }//getCampoSuplementarioReducPersSust
    
    public String getValorPersonaDependiente(Integer codOrganizacion, String codProcedimiento) {
        if (log.isDebugEnabled()) {
            log.debug("getValorPersonaDependiente() : BEGIN");
        }
        String valorPersonaDependiente = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + MeLanbide01Constantes.FICHERO_CONFIGURACION + "/" + codProcedimiento + "/VALOR_TIENE_PERSONA_DEPENDIENTE", MeLanbide01Constantes.FICHERO_CONFIGURACION);
        if (log.isDebugEnabled()) {
            log.debug("getValorPersonaDependiente() : END");
        }
        return valorPersonaDependiente;
    }//getValorPersonaDependiente
    
    public String getCampoSuplementarioGradoMinusvalia(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioGradoMinusvalia() : BEGIN");
        }
        String valor = new String();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + MeLanbide01Constantes.FICHERO_CONFIGURACION + "/" + codProcedimiento + MeLanbide01Constantes.GRADMINUSDEPEN, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + "/MODULO_INTEGRACION/" + MeLanbide01Constantes.FICHERO_CONFIGURACION + "/" + codProcedimiento + "/PANTALLA_EXPEDIENTE/TIPO/GRADMINUSDEPEN", MeLanbide01Constantes.FICHERO_CONFIGURACION);
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorNumero();
        }//if(salida.getStatus() == 0)
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioGradoMinusvalia() : END");
        }
        return valor;
    }//getCampoSuplementarioGradoMinusvalia
    
    public int calcularDiferenciaAnyosEntreFecha(Date fechaInicio, Date fechaFin) {
        Calendar fechaAct = Calendar.getInstance();
        fechaAct.setTime(fechaFin);

        Calendar fechaNac = Calendar.getInstance();
        fechaNac.setTime(fechaInicio);

        int dif_anios = fechaAct.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
        int dif_meses = fechaAct.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
        int dif_dias = fechaAct.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);

        //Si está en ese año pero todavía no los ha cumplido
        if (dif_meses < 0 || (dif_meses == 0 && dif_dias < 0)) {
            dif_anios--;
        }
        return dif_anios;
    }
    
    public HashMap<String, String> getMaxAnhosDependenciaActividadSubvencionada(Integer codOrganizacion, String codProcedimiento) {
        if (log.isDebugEnabled()) {
            log.debug("getMaxAnhosDependenciaActividadSubvencionada() : BEGIN");
        }
        HashMap<String, String> maxAnhosDependenciaActividadSubvencionada = new HashMap<String, String>();
        String tiposActividad = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPOS_ACTIVIDAD_SUBVENCIONADA, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String maxAnhos = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.MAX_ANHOS_DEPENDENCIA_ACTIVIDAD_SUBVENCIONADA, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String[] splitTiposActividad = tiposActividad.split(";");
        String[] splitMaxAnhos = maxAnhos.split(";");
        for (int i = 0; i < splitTiposActividad.length; i++) {
            maxAnhosDependenciaActividadSubvencionada.put(splitTiposActividad[i], splitMaxAnhos[i]);
        }//for(int i=0; i<splitTiposActividad.length; i++)
        if (log.isDebugEnabled()) {
            log.debug("getMaxAnhosDependenciaActividadSubvencionada() : END");
        }
        return maxAnhosDependenciaActividadSubvencionada;
    }//getMaxAñosDependenciaActividadSubvencionada

    
    /**
     * Comprueba que la edad de un posible hijo dependiente no supere las
     * marcadas como propiedades en el fichero de configuracion.
     *
     * @param codigoOrganizacion: Código de organización/municipio
     * @param numExpediente Número del expediente
     * @param codProcedimiento: Código del procedimiento
     * @param ejercicio: Ejercicio
     * @return Valores posibles: "0" --> Si no hay alarma "1" --> El valor del
     * campo "fecha de actuación concedida" no puede ser inferior al valor de
     * "Fecha de nacimiento de persona dependiente" "2" --> Se ha superado el
     * límite máximo de días para la persona dependiente      *
     * @throws MeLanbide01Exception
     */
    public String alarmaMaxAnosDependencia(String codigoOrganizacion, String numExpediente, String codProcedimiento,
            String ejercicio) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("alarmaMaxAnosDependencia() : BEGIN");
        }
        String alarma = "0";
        try {
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

            //Recuperamos la actividad de los campos suplementarios del expediente
            String actividadSubvencionada = getCampoSuplementarioActividadSubvencionada(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            //Recuperamos la relacion con la persona dependiente
            String relacionPersonaDependiente = getCampoSuplementarioRelancionPersonaDependiente(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            //Recuperamos el valor que indica si tiene un hijo dependiente
            String codRelacionPersonaDependiente = getValorPersonaDependiente(Integer.valueOf(codigoOrganizacion), codProcedimiento);

            String CODIGO_CAMPO_FECHAFIN_ACTUACION = ConfigurationParameter.getParameter(codigoOrganizacion + "/MODULO_INTEGRACION/CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", MeLanbide01Constantes.FICHERO_CONFIGURACION);
            String TIPO_CODIGO_CAMPO_FECHAFIN_ACTUACION = ConfigurationParameter.getParameter(codigoOrganizacion + "/MODULO_INTEGRACION/TIPO_CAMPO_FECHA_FIN_ACTUACION_CONCEDIDO", MeLanbide01Constantes.FICHERO_CONFIGURACION);

            SalidaIntegracionVO salidaFechaFinActuacion = el.getCampoSuplementarioExpediente(codigoOrganizacion, ejercicio, numExpediente,
                    codProcedimiento, CODIGO_CAMPO_FECHAFIN_ACTUACION, Integer.parseInt(TIPO_CODIGO_CAMPO_FECHAFIN_ACTUACION));

            if (salidaFechaFinActuacion != null && salidaFechaFinActuacion.getStatus() == 0) {

                Calendar FECHA_FIN_ACTUACION = salidaFechaFinActuacion.getCampoSuplementario().getValorFecha();

                if (FECHA_FIN_ACTUACION != null) {
                    log.debug("alarmaMaxAnosDependencia - FECHA_FIN_ACTUACION no nula ");
                    //Si el valor del campo "Al cuidado de" indica que es un hijo (valor de código 0 en el desplegable)
                    if (codRelacionPersonaDependiente.equalsIgnoreCase(relacionPersonaDependiente)) {
                        log.debug("alarmaMaxAnosDependencia - codRelacionPersonaDependiente ");
                        //Recuperamos la fecha de nacimiento de la persona dependiente
                        Calendar FECHA_NACIMIENTO_PERSO_DEPENDIENTE = getCampoSuplementarioFecNacPersonaDependiente(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

                        if (FECHA_NACIMIENTO_PERSO_DEPENDIENTE.after(FECHA_FIN_ACTUACION)) {
                            // La fecha de nacimiento de la persona dependiente, no puede ser superior al a fecha de fin de actuación.
                            // En este caso se muestra un error en la pestaña "Datos de cálculo"                           
                            alarma = "1";
                            return alarma;
                        } else {
                            log.debug("alarmaMaxAnosDependencia - FECHA_NACIMIENTO_PERSO_DEPENDIENTE antes que fin de actuacion ");
                            //Recuperamos el grado de minusvalia de la persona dependiente
                            String gradoMinusvaliaPersonaDependiente = getCampoSuplementarioGradoMinusvalia(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

                            Integer valor = calcularDiferenciaAnyosEntreFecha(FECHA_NACIMIENTO_PERSO_DEPENDIENTE.getTime(), FECHA_FIN_ACTUACION.getTime());

                            //Integer edad = calcularEdad(fecNacimientoPersonaDependiente);
                            HashMap<String, String> maximosAñosPersonaDependiente
                                    = getMaxAnhosDependenciaActividadSubvencionada(Integer.valueOf(codigoOrganizacion), codProcedimiento);

                            Iterator it = maximosAñosPersonaDependiente.entrySet().iterator();

                            while (it.hasNext()) {
                                Map.Entry e = (Map.Entry) it.next();
                                if (e.getKey().equals(actividadSubvencionada)) {
                                    if (!actividadSubvencionada.equals("REDUCC")) {
                                        Integer numAnhos = Integer.valueOf(String.valueOf(e.getValue()));
                                        if (valor >= numAnhos) {
                                            //alarma = (String) e.getKey();                                            
                                            alarma = "2";
                                        }
                                    } else {//es reduccion (menor 12 si minusvalia<=33%, menor 18 si minusvalia>33% )
                                        log.debug("alarmaMaxAnosDependencia - actividad subvencionada es REDUCCION ");
                                        String[] splitMaxAnhos = String.valueOf(e.getValue()).split("/");
                                        String gradosMinusvalia = ConfigurationParameter.getParameter(codigoOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                                                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.GRADO_MINUSVALIA_ACTIVIDAD_SUBVENCIONADA, MeLanbide01Constantes.FICHERO_CONFIGURACION);
                                        log.debug("alarmaMaxAnosDependencia - GRADO MINUSVALIA: " + gradosMinusvalia);
                                        log.debug("alarmaMaxAnosDependencia - DIFERENCIA AÑOS: " + valor);
                                        String[] splitGradosMinus = gradosMinusvalia.split(";");
                                        for (int i = 0; i < splitMaxAnhos.length; ++i) {
                                            String[] minmaxGrado = splitGradosMinus[i].split("-");
                                            log.debug("alarmaMaxAnosDependencia - COMPROBAR minmaxGrado: " + minmaxGrado[0] + " - " + minmaxGrado[1]);
                                            if (Integer.valueOf(gradoMinusvaliaPersonaDependiente) <= Integer.valueOf(minmaxGrado[1]) && Integer.valueOf(gradoMinusvaliaPersonaDependiente) > Integer.valueOf(minmaxGrado[0])) {
                                                log.debug("alarmaMaxAnosDependencia - VALOR >=" + splitMaxAnhos[i] + "?: " + (valor >= Integer.valueOf(splitMaxAnhos[i])));
                                                if (valor >= Integer.valueOf(splitMaxAnhos[i])) {
                                                    alarma = "2";
                                                    log.debug("alarmaMaxAnosDependencia - ALARMA ");
                                                }
                                            }
                                        }
                                    }
                                    break;
                                }
                            }// while
                        }
                    }
                }// if
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error al verificar la alarma del máximo de años de la persona dependiente con respecto a la actividad subvencionable: " + e.getMessage());
        }

        if (log.isDebugEnabled()) {
            log.debug("alarmaMaxAnosDependencia() : END");
        }
        return alarma;
    }//alarmaMaxAnosDependencia
    
    /*
    DGC 26/01/2015  Metodo que comoprueba si el expediente esta fuera de plazo 
    comparando las fechas de accion subvencionable concedida + 3 mese < Fecha de solicitud
     */
    public boolean compruebaSiExpedienteFueraPlazo(int codOrganizacion, String ejercicio, String codProcedimiento, String numExpediente,
            String campoSuplementarioFechaSolicitud, String tipoCampoSuplementarioFechaSolicitud,
            String campoSuplementarioFechaFinActuacionConcedido, String tipoCampoSuplementarioFechaFinActuacionConcedido) {
        boolean result = false;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        CampoSuplementarioModuloIntegracionVO fechaSolicitud = new CampoSuplementarioModuloIntegracionVO();
        CampoSuplementarioModuloIntegracionVO fechaFinAccionSubvConcedida = new CampoSuplementarioModuloIntegracionVO();
        SalidaIntegracionVO salidaFechaSolicitud = new SalidaIntegracionVO();
        salidaFechaSolicitud = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campoSuplementarioFechaSolicitud, Integer.parseInt(tipoCampoSuplementarioFechaSolicitud));
        SalidaIntegracionVO salidaFechaFinAccionSubvConcedida = new SalidaIntegracionVO();
        salidaFechaFinAccionSubvConcedida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campoSuplementarioFechaFinActuacionConcedido, Integer.parseInt(tipoCampoSuplementarioFechaFinActuacionConcedido));
        if (salidaFechaSolicitud.getStatus() == 0) {
            fechaSolicitud = salidaFechaSolicitud.getCampoSuplementario();
            Calendar _fechaSolicitud = fechaSolicitud.getValorFecha();
            if (salidaFechaFinAccionSubvConcedida.getStatus() == 0) {
                fechaFinAccionSubvConcedida = salidaFechaFinAccionSubvConcedida.getCampoSuplementario();
                Calendar _fechaFinAccSubvConc = fechaFinAccionSubvConcedida.getValorFecha();
                if (_fechaSolicitud != null && _fechaFinAccSubvConc != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("Fecha de Solictud = " + formatFecha_dd_MM_yyyy.format(_fechaSolicitud.getTime()));
                        log.debug("Fecha de Fin Acc. Subv. Conc. = " + formatFecha_dd_MM_yyyy.format(_fechaFinAccSubvConc.getTime()));
                    }//if(log.isDebugEnabled())
                    String nroMeseAdd = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + "NUM_MESES_ADD_EXP_FUERAPLAZO", MeLanbide01Constantes.FICHERO_CONFIGURACION);
                    log.debug("Numero meses a Add a fecha fin acci. subv. concedida " + nroMeseAdd);
                    nroMeseAdd = (nroMeseAdd != null && nroMeseAdd.length() > 0 ? nroMeseAdd : "0");
                    // Añadimos los tres meses a la Fecha de fin de accion subv. concedida
                    Calendar _fechaLimiteExpediente = Calendar.getInstance();
                    _fechaLimiteExpediente.set(_fechaFinAccSubvConc.get(Calendar.YEAR), _fechaFinAccSubvConc.get(Calendar.MONTH), _fechaFinAccSubvConc.get(Calendar.DAY_OF_MONTH));
                    _fechaLimiteExpediente.add(Calendar.MONTH, Integer.valueOf(nroMeseAdd));
                    log.debug("Fecha Limite de Plazo Expediente : " + _fechaLimiteExpediente.getTime());
                    if (_fechaSolicitud.after(_fechaLimiteExpediente)) {
                        result = true;
                        // Fecha Limite o Fecha Fin Accion Concedida dentro de Periodo suspension plazos? 
                        if ((fechaDentroPeriodoSuspensionPlazos(MeLanbide01Constantes.COD_SUPENSION_PLAZOS_COVID19, _fechaLimiteExpediente)
                                || fechaDentroPeriodoSuspensionPlazos(MeLanbide01Constantes.COD_SUPENSION_PLAZOS_COVID19, _fechaFinAccSubvConc))
                                && validarFechaLimiteContraPeriodoSuspencionPlazos(MeLanbide01Constantes.COD_SUPENSION_PLAZOS_COVID19, numExpediente, _fechaSolicitud, _fechaFinAccSubvConc, _fechaLimiteExpediente, nroMeseAdd)) {
                            // Si esta dentro del periodo calculamos la nueva fecha para validar
                            result = false;
                        }
                    }
                } else {
                    //Si la fecha es nula lo marcamos como error.
                    log.debug("Una de las fechas a comparar esta a null - NO podemos evauar si el expediente esta fuera de plazo o no : Fecha solicitud y (fecha acc. subv. concedida +3M)");
                }//if(fechaInicio != null)
            } else {
                log.debug("No evaluamos si expediente esta fuera de plazo - NO hemos recuperado el valor del campo suple. Fecha fin de accion subv. concedida");
            }
        } else {
            log.debug("No evaluamos si expediente esta fuera de plazo - NO hemos recuperado el valor del campo suple. Fecha de solicitud");
        }//if(salida.getStatus() == 0)
        return result;
    }

    public boolean validarFechaLimiteContraPeriodoSuspencionPlazos(String codPeridoSuspension, String numeroExpediente, Calendar _fechaSolicitud, Calendar _fechaFinAccSubvConc, Calendar _fechaLimiteExpediente, String nroMeseAdd) {
        log.info("Inicio validarFechaLimiteContraPeriodoSuspencionPlazos - " + formatFechaLog.format(new Date()));
        boolean retornoValidado = true;
        try {
            log.info("Revisamos caso : " + codPeridoSuspension + " -- " + numeroExpediente);
            log.info("fechaSolicitud,_fechaFinAccSubvConc,_fechaLimiteExpediente,nroMeseAdd : \n  "
                    + (_fechaSolicitud != null ? _fechaSolicitud.getTime() : "") + ","
                    + (_fechaFinAccSubvConc != null ? _fechaFinAccSubvConc.getTime() : "") + ","
                    + (_fechaLimiteExpediente != null ? _fechaLimiteExpediente.getTime() : "") + "," + nroMeseAdd
            );
            if (codPeridoSuspension != null && !codPeridoSuspension.isEmpty()) {
                String inicioPeriodoSuspencionPlazo = ConfigurationParameter.getParameter(MeLanbide01Constantes.FECHA_INICIO_PERIODO_SUSPENCION_PLAZOS + MeLanbide01Constantes.BARRA + codPeridoSuspension, MeLanbide01Constantes.FICHERO_CONFIGURACION);
                String finPeriodoSuspensionPlazo = ConfigurationParameter.getParameter(MeLanbide01Constantes.FECHA_FIN_PERIODO_SUSPENCION_PLAZOS + MeLanbide01Constantes.BARRA + codPeridoSuspension, MeLanbide01Constantes.FICHERO_CONFIGURACION);
                if (inicioPeriodoSuspencionPlazo != null && !inicioPeriodoSuspencionPlazo.isEmpty()
                        && finPeriodoSuspensionPlazo != null && !finPeriodoSuspensionPlazo.isEmpty()) {
                    Calendar fechaInicioPeriodoSuspensionPlazo = Calendar.getInstance();
                    Calendar fechaFinPeriodoSuspensionPlazo = Calendar.getInstance();
                    fechaInicioPeriodoSuspensionPlazo.setTime(formatFecha_dd_MM_yyyy.parse(inicioPeriodoSuspencionPlazo));
                    fechaFinPeriodoSuspensionPlazo.setTime(formatFecha_dd_MM_yyyy.parse(finPeriodoSuspensionPlazo));
                    Calendar nuevaFechaLimiteExpediente = null; // Calendar.getInstance();
                    if (_fechaLimiteExpediente != null && _fechaSolicitud != null && _fechaFinAccSubvConc != null) {
                        if (_fechaLimiteExpediente.after(fechaInicioPeriodoSuspensionPlazo) && _fechaLimiteExpediente.before(fechaFinPeriodoSuspensionPlazo)) {
                            /**
                             * *
                             * Caso 1 : Para aquellos cuyo período semestral o
                             * contrato finaliza + 3 meses es una fecha que cae
                             * dentro del periodo de suspensión de plazos, habrá
                             * que contar el tiempo transcurrido de esos 3 meses
                             * antes del 14 de Marzo y reaundar el tiempo que le
                             * falte a partir del 1 de junio
                             */
                            int numeroDiaTranscurridosPlazo = fechaInicioPeriodoSuspensionPlazo.get(Calendar.DAY_OF_YEAR) - _fechaFinAccSubvConc.get(Calendar.DAY_OF_YEAR);
                            log.info("numeroDiaTranscurridosPlazo : " + numeroDiaTranscurridosPlazo);
                            nuevaFechaLimiteExpediente = fechaFinPeriodoSuspensionPlazo;
                            nuevaFechaLimiteExpediente.add(Calendar.MONTH, Integer.valueOf(nroMeseAdd));
                            nuevaFechaLimiteExpediente.add(Calendar.DAY_OF_YEAR, (numeroDiaTranscurridosPlazo * (-1)));
                        } else if (_fechaFinAccSubvConc.after(fechaInicioPeriodoSuspensionPlazo) && _fechaFinAccSubvConc.before(fechaFinPeriodoSuspensionPlazo)) {
                            /**
                             * *
                             * Caso 2: aquellos cuyo período semestral o
                             * contrato finaliza dentro del periodo de
                             * suspensión de plazos administrativos, el plazo de
                             * solicitud no surte efectos en el período de
                             * interrupción y se iniciaría a partir del 1 de
                             * Junio, es decir a partir de dicha fecha se
                             * empiezan a contar los 3 meses.
                             */
                            nuevaFechaLimiteExpediente = fechaFinPeriodoSuspensionPlazo;
                            nuevaFechaLimiteExpediente.add(Calendar.MONTH, Integer.valueOf(nroMeseAdd));
                        }
                        if (nuevaFechaLimiteExpediente != null) {
                            log.info("nuevaFechaLimiteExpediente : " + nuevaFechaLimiteExpediente.getTime());
                            if (_fechaSolicitud.after(nuevaFechaLimiteExpediente)) {
                                retornoValidado = false;
                            }
                        }
                    } else {
                        log.error("FehaSolicitud-FechaFinctSubv-FechaLimiteExpediente recibidas a null " + _fechaSolicitud + " - " + _fechaFinAccSubvConc + " - " + _fechaLimiteExpediente);
                    }
                } else {
                    log.error("-- Fechas inicio Fin periodo suspension plazos no configurada.");
                }

            } else {
                log.error("Codigo de periodo suspension no recivido");
            }
        } catch (Exception e) {
            log.error("Error al validar expediente fuera ade plazo, excepcion por periodo de suspension de plazos", e);
        }
        log.info("Fin validarFechaLimiteContraPeriodoSuspencionPlazos - " + retornoValidado + " - " + formatFechaLog.format(new Date()));
        return retornoValidado;
    }
    
    public boolean fechaDentroPeriodoSuspensionPlazos(String codPeridoSuspension, Calendar _fechaEvaluar) {
        log.info("Inicio fechaDentroPeriodoSuspensionPlazos - " + formatFechaLog.format(new Date()));
        boolean retorno = false;
        try {
            log.info("Revisamos caso : " + codPeridoSuspension + " -- "
                    + (_fechaEvaluar != null ? _fechaEvaluar.getTime() : "")
            );
            if (codPeridoSuspension != null && !codPeridoSuspension.isEmpty()) {
                String inicioPeriodoSuspencionPlazo = ConfigurationParameter.getParameter(MeLanbide01Constantes.FECHA_INICIO_PERIODO_SUSPENCION_PLAZOS + MeLanbide01Constantes.BARRA + codPeridoSuspension, MeLanbide01Constantes.FICHERO_CONFIGURACION);
                String finPeriodoSuspensionPlazo = ConfigurationParameter.getParameter(MeLanbide01Constantes.FECHA_FIN_PERIODO_SUSPENCION_PLAZOS + MeLanbide01Constantes.BARRA + codPeridoSuspension, MeLanbide01Constantes.FICHERO_CONFIGURACION);
                log.info("inicioPeriodoSuspencionPlazo--finPeriodoSuspensionPlazo: " + inicioPeriodoSuspencionPlazo + "--" + finPeriodoSuspensionPlazo);
                if (inicioPeriodoSuspencionPlazo != null && !inicioPeriodoSuspencionPlazo.isEmpty()
                        && finPeriodoSuspensionPlazo != null && !finPeriodoSuspensionPlazo.isEmpty()
                        && _fechaEvaluar != null) {
                    Calendar fechaInicioPeriodoSuspensionPlazo = Calendar.getInstance();
                    Calendar fechaFinPeriodoSuspensionPlazo = Calendar.getInstance();
                    fechaInicioPeriodoSuspensionPlazo.setTime(formatFecha_dd_MM_yyyy.parse(inicioPeriodoSuspencionPlazo));
                    fechaFinPeriodoSuspensionPlazo.setTime(formatFecha_dd_MM_yyyy.parse(finPeriodoSuspensionPlazo));
                    if (_fechaEvaluar.after(fechaInicioPeriodoSuspensionPlazo) && _fechaEvaluar.before(fechaFinPeriodoSuspensionPlazo)) {
                        retorno = true;
                    } else {
                        log.info("_fechaEvaluar No esta dentro del periodo de suspension de plazos  " + fechaInicioPeriodoSuspensionPlazo + " - " + fechaFinPeriodoSuspensionPlazo + " - " + _fechaEvaluar);
                    }
                } else {
                    log.error("-- Fechas inicio Fin periodo suspension plazos no configuradas o fecha a comparr racibida a null ");
                }

            } else {
                log.error("Codigo de periodo suspension no recivido");
            }
        } catch (Exception e) {
            log.error("Error al comprobar si una fecha esta dentro un periodo de suspension de plazos ", e);
        }
        log.info("Fin fechaDentroPeriodoSuspensionPlazos - " + retorno + " - " + formatFechaLog.format(new Date()));
        return retorno;
    }
    
    /**
     * Recupera la propiedad del fichero de configuracion que indica el numero
     * maximo de intervalos permitidos
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     */
    public String getNumMaximoIntervalosPermitidos(Integer codOrganizacion, String codProcedimiento) {
        if (log.isDebugEnabled()) {
            log.debug("getNumMaximoIntervalosPermitidos() : BEGIN");
        }
        String numeroMaximoIntervalos = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.NUMERO_MAXIMO_INTERVALOS, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        if (log.isDebugEnabled()) {
            log.debug("getNumMaximoIntervalosPermitidos() : END");
        }
        return numeroMaximoIntervalos;
    }//getNumMaximoIntervalosPermitidos

    /**
     * Recupera la propiedad del fichero de configuracion que indica el numero
     * minimo de dias para calcular la subvencion
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     */
    public String getNumMinimoDias(Integer codOrganizacion, String codProcedimiento) {
        if (log.isDebugEnabled()) {
            log.debug("getNumMinimoDias() : BEGIN");
        }
        String numeroMaximoIntervalos = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.NUMERO_MINIMO_DIAS, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        if (log.isDebugEnabled()) {
            log.debug("getNumMinimoDias() : END");
        }
        return numeroMaximoIntervalos;
    }//getNumMinimoDias

    public String getMinReducPersSust(Integer codOrganizacion, String codProcedimiento) {
        if (log.isDebugEnabled()) {
            log.debug("getMinReducPersSust() : BEGIN");
        }
        String minReducPersSust = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.MINIMO_REDUCPERSSUST, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        if (log.isDebugEnabled()) {
            log.debug("getMinReducPersSust() : END");
        }
        return minReducPersSust;
    }//getMinReducPersSust

    /**
     * Recupera una propiedad del fichero de propiedades con un valor que indica
     * cual es el codigo de rol de persona contratada
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     */
    public String getRolPersonaContratada(Integer codOrganizacion, String codProcedimiento) {
        if (log.isDebugEnabled()) {
            log.debug("getRolPersonaContratada() : BEGIN");
        }
        String rolPersonaContratada = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.ROL_PERSONA_CONTRATADA, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        if (log.isDebugEnabled()) {
            log.debug("getRolPersonaContratada() : END");
        }
        return rolPersonaContratada;
    }//getRolPersonaContratada
    
    /**
     * Calcula si hay que mostrar la alarma de maximo de dias pero este metodo
     * se usa solo al cargar la pagina y al modificar los datos adicionales del
     * expediente por que esta busqueda tiene en cuenta los expedientes en los
     * que el tercero tiene el rol de persona contratada y ejerce la misma
     * actividad y el expediente actual pero recupera la suma de los dias del
     * expediente actual de la BBDD
     *
     * @param codigoOrganizacion
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codDecretoAplica
     * @return
     * @throws MeLanbide01Exception
     */
    public String alarmaMaximoDias(String codigoOrganizacion, String numExpediente, String codProcedimiento,
            String ejercicio,String codDecretoAplica) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("alarmaMaximoDias() : BEGIN");
        }
        String alarma = new String("");
        Integer numeroTotalDias = new Integer(0);
        try {
            String actividadSubvencionada = getCampoSuplementarioActividadSubvencionada(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);
            //Recuperamos el numero total de dias
            numeroTotalDias = diasRestantesSubvencionInteresado(codigoOrganizacion, numExpediente, codProcedimiento, ejercicio);
            //Comprobamos si el numero total de dias es mayor que el definido en los parametros de configuracion
            if (numeroTotalDias > 0) {
                HashMap<String, String> maximosDiasActividadSubvencionada
                        = getMaximosDiasActividadSubvencionada(Integer.valueOf(codigoOrganizacion), codProcedimiento,codDecretoAplica,numExpediente);

                Iterator it = maximosDiasActividadSubvencionada.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry) it.next();
                    if (e.getKey().equals(actividadSubvencionada)) {
                        Integer numDias = Integer.valueOf(String.valueOf(e.getValue()));
                        if (numDias < numeroTotalDias) {
                            alarma = (String) e.getKey();
                        }//if(numDias > numeroTotalDias)
                    }//if(e.getKey().equals(actividadSubvencionada))
                }//while (it.hasNext())
            }//if(numeroTotalDias > 0)
        } catch (Exception ex) {
            throw new MeLanbide01Exception("No se ha podido calcular la alarma de dias para el expediente = " + numExpediente);
        }//try
        if (log.isDebugEnabled()) {
            log.debug("alarmaMaximoDias() : END");
        }
        return alarma;
    }//alarmaMaximoDias
    
    /**
     * Calcula si hay que mostrar la alarma de maximo de dias pero este metodo
     * se usa al añadir o modificar los adicionales del expediente por que esta
     * busqueda tiene en cuenta los expedientes en los que el tercero tiene el
     * rol de persona contratada y ejerce la misma actividad y para el
     * expediente actual tiene en cuenta los datos de la tabla y los que
     * añadimos y modificamos
     *
     * @param codigoOrganizacion
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param numTotalDiasExpediente
     * @param codDecretoAplica
     * @return
     * @throws MeLanbide01Exception
     */
    public AlarmaVO alarmaMaximoDias2(String codigoOrganizacion, String numExpediente, String codProcedimiento,
            String ejercicio, Integer numTotalDiasExpediente,String codDecretoAplica) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("alarmaMaximoDias2() : BEGIN");
        }
        //String alarma = new String("");
        AlarmaVO alarma = new AlarmaVO();

        try {
            Integer numeroTotalDias = new Integer(0);

            if (log.isDebugEnabled()) {
                log.debug("Recuperamos la actividad subvencionada");
            }
            //Recuperamos la actividad de los campos suplementarios del expediente
            String actividadSubvencionada = getCampoSuplementarioActividadSubvencionada(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            if (log.isDebugEnabled()) {
                log.debug("Recuperamos la suma de los dias subvencionados");
            }
            numeroTotalDias = diasRestantesSubvencionInteresado2(codigoOrganizacion, numExpediente, codProcedimiento,
                    ejercicio, numTotalDiasExpediente);

            //Comprobamos si el numero total de dias es mayor que el definido en los parametros de configuracion
            if (numeroTotalDias > 0) {
                HashMap<String, String> maximosDiasActividadSubvencionada
                        = getMaximosDiasActividadSubvencionada(Integer.valueOf(codigoOrganizacion), codProcedimiento,codDecretoAplica,numExpediente);

                Iterator it = maximosDiasActividadSubvencionada.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry) it.next();
                    if (e.getKey().equals(actividadSubvencionada)) {
                        Integer numDias = Integer.valueOf(String.valueOf(e.getValue()));
                        if (numDias < numeroTotalDias) {
                            //alarma = (String) e.getKey();
                            alarma.setCodigoAlarma("25");
                            alarma.setDiasExcedidos(numDias);
                        }//if(numDias > numeroTotalDias)
                    }//if(e.getKey().equals(actividadSubvencionada))
                }//while (it.hasNext())
            }//if(numeroTotalDias > 0)
        } catch (Exception e) {
            log.debug("===================> alarmaMaximoDias2() ERROR: " + e.getMessage());
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("alarmaMaximoDias2() : END");
        }
        return alarma;
    }//alarmaMaximoDias2

    
    public int diasRestantesSubvencionInteresado(String codigoOrganizacion, String numExpediente, String codProcedimiento,
            String ejercicio) throws MeLanbide01Exception, SQLException {
        if (log.isDebugEnabled()) {
            log.debug("diasRestantesSubvencionInteresado() : BEGIN");
        }
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        SalidaIntegracionVO salidaExpedientesInteresadoTercero = new SalidaIntegracionVO();
        ExpedienteModuloIntegracionVO expediente = new ExpedienteModuloIntegracionVO();
        ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados = new ArrayList<InteresadoExpedienteModuloIntegracionVO>();
        ArrayList<ExpedienteModuloIntegracionVO> listaExpedientesInteresadoTercero = new ArrayList<ExpedienteModuloIntegracionVO>();
        MeLanbide01Manager meLanbide01Manager = MeLanbide01Manager.getInstance();
        Integer numeroTotalDias = new Integer(0);

        //Recuperamos la actividad de los campos suplementarios del expediente
        String actividadSubvencionada = getCampoSuplementarioActividadSubvencionada(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

        //Recuperamos la relacion con la persona dependiente = MotivoSubvencion
        String motivoSubRelacionPersDepe = getCampoSuplementarioRelancionPersonaDependiente(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

        //Recuperamos la fecha de nacimiendo de la persona dependiente
        Calendar fechaNacimientoPersonaDependiente
                = getCampoSuplementarioFecNacPersonaDependiente(new Integer(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

        log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() actividadSubvencionada : " + actividadSubvencionada);
        log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() fechaNacimientoPersonaDependiente : " + fechaNacimientoPersonaDependiente);

        if (!"".equalsIgnoreCase(actividadSubvencionada)) {
            String rolPersonaContratada = getRolPersonaContratada(Integer.valueOf(codigoOrganizacion), codProcedimiento);
            log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() rolPersonaContratada: " + rolPersonaContratada);
            salida = el.getExpediente(codigoOrganizacion, numExpediente, codProcedimiento, ejercicio);
            expediente = salida.getExpediente();
            interesados = expediente.getInteresados();

            log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() número de interesados del expediente: " + interesados.size());
            if (interesados.size() > 0) {
                for (InteresadoExpedienteModuloIntegracionVO interesado : interesados) {
                    //Buscamos al interesado con el rol de persona contratada
                    log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() rol interesado expediente: " + interesado.getCodigoRol() + ", codTercero: " + interesado.getCodigoTercero());
                    log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() rolPersonaContratada: " + rolPersonaContratada);
                    if (interesado.getCodigoRol() == Integer.valueOf(rolPersonaContratada)) {
                        String documentoInteresado = interesado.getDocumento();
                        String tipoDocumentoInteresado = String.valueOf(interesado.getTipoDocumento());

                        /**
                         * salidaExpedientesInteresadoTercero =
                         * el.getExpedientesByInteresadoAndProc
                         * (codigoOrganizacion, codProcedimiento,
                         * tipoDocumentoInteresado, documentoInteresado);
                         * listaExpedientesInteresadoTercero =
                         * salidaExpedientesInteresadoTercero.getExpedientes();
                         */
                        log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() se buscan expedientes en los que el interesado está, documentoInteresado: " + documentoInteresado + ",tipoDocumentoInteresado: " + tipoDocumentoInteresado);
                        ArrayList<ExpedienteMeLanbide01VO> expTerceros = MeLanbide01DatosCalculoDao.getInstance().getExpedientesTercero(codigoOrganizacion, codProcedimiento, tipoDocumentoInteresado, documentoInteresado, ConnectionUtils.getAdaptSQLBD(codigoOrganizacion));

                        log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado(): " + expTerceros.size());

                        //if(listaExpedientesInteresadoTercero!=null && listaExpedientesInteresadoTercero.size()>0){
                        if (expTerceros != null && expTerceros.size() > 0) {
                            //for(ExpedienteModuloIntegracionVO expedienteInteresado : listaExpedientesInteresadoTercero){
                            for (ExpedienteMeLanbide01VO expedienteInteresado : expTerceros) {
                                //Comprobamos que para el expediente del que somos interesados, la actividad subvencionada es la misma que par el 
                                //expediente en el que nos encontramos.
                                String actividadSubvencionadaInteresado = getCampoSuplementarioActividadSubvencionada(Integer.valueOf(codigoOrganizacion), String.valueOf(expedienteInteresado.getEjercicio()),
                                        expedienteInteresado.getNumExpediente(), expedienteInteresado.getCodProcedimiento());

                                String motivoSubRelacionPersDepeExpRel = getCampoSuplementarioRelancionPersonaDependiente(Integer.valueOf(codigoOrganizacion), String.valueOf(expedienteInteresado.getEjercicio()), expedienteInteresado.getNumExpediente(), codProcedimiento);

                                //Recuperamos el valor del estudio tecnico del expediente
                                String resultadoEstudioTecnico = getCampoSuplementarioResultadoEstudioTecnico(Integer.valueOf(codigoOrganizacion), String.valueOf(expedienteInteresado.getEjercicio()),
                                        expedienteInteresado.getNumExpediente(), expedienteInteresado.getCodProcedimiento());

                                //Recuperamos el valor del fichero de propiedades que nos indica cuando un estudio tecnico esta rechazado
                                String valorNegativoEstudioTecnico = getResultadoEstudioTecnicoNegativo(Integer.valueOf(codigoOrganizacion), codProcedimiento);

                                //Recuperamos la fecha de nacimiento de la persona dependiente
                                Calendar fechaNacimientoPersonaDependienteExpRel = getCampoSuplementarioFecNacPersonaDependiente(Integer.valueOf(codigoOrganizacion), String.valueOf(expedienteInteresado.getEjercicio()),
                                        expedienteInteresado.getNumExpediente(), expedienteInteresado.getCodProcedimiento());

                                log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() actividadSubvencionadaInteresado : " + actividadSubvencionadaInteresado);
                                log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() resultadoEstudioTecnico : " + resultadoEstudioTecnico);
                                log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() valorNegativoEstudioTecnico : " + valorNegativoEstudioTecnico);
                                log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() fechaNacimientoPersonaDependienteExpRel : " + fechaNacimientoPersonaDependienteExpRel);

                                if (!"".equalsIgnoreCase(actividadSubvencionadaInteresado)) {
                                    if (actividadSubvencionada.equalsIgnoreCase(actividadSubvencionadaInteresado)) {
                                        // Comparamos que sea e mismo motivo
                                        if (motivoSubRelacionPersDepe!=null && motivoSubRelacionPersDepe.equalsIgnoreCase(motivoSubRelacionPersDepeExpRel)) {
                                            //Si el estudio tecnico esta denegado no sumamos los dias de ese expediente
                                            if (!valorNegativoEstudioTecnico.equalsIgnoreCase(resultadoEstudioTecnico)) {
                                                //Si el dia de nacimiento de la persona dependiente coincide con el del expediente relacionado
                                                //ya que la subvencion es para cada persona dependiente.
                                                if ((fechaNacimientoPersonaDependienteExpRel != null && fechaNacimientoPersonaDependiente != null)
                                                        && (fechaNacimientoPersonaDependienteExpRel.equals(fechaNacimientoPersonaDependiente))) {
                                                    //Buscamos para cada expediente el numero de dias

                                                    log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() a calcular el número de dias");
                                                    numeroTotalDias += meLanbide01Manager.numDiasTotalExpediente(expedienteInteresado.getNumExpediente(),
                                                            codigoOrganizacion, ConnectionUtils.getAdaptSQLBD(codigoOrganizacion));
                                                }/*if((fechaNacimientoPersonaDependienteExpRel != null && fechaNacimientoPersonaDependiente != null)
                                                && (fechaNacimientoPersonaDependienteExpRel.equals(fechaNacimientoPersonaDependiente)))*/
                                            }//if(!valorNegativoEstudioTecnico.equalsIgnoreCase(resultadoEstudioTecnico))
                                        }else {
                                            log.debug("El expediente relacionado con el interesado "
                                                        + expedienteInteresado.getNumExpediente() + " no tiene o no coincide el valor del campo Motivo Subvencion / Relacion con la persona dependiente : Hijo, familiar, etc ");
                                        }
                                    }//if(actividadSubvencionada.equalsIgnoreCase(actividadSubvencionadaInteresado))
                                } else {
                                    log.debug("El expediente relacionado con el interesado "
                                                + expedienteInteresado.getNumExpediente() + " no tiene el campo actividad subvencionada");
                                }
                            }//for
                        }
                    }
                }
            }
        } else {
            throw new MeLanbide01Exception("No se ha podido recuperar el campo de actividad subvencionada para el expediente = " + numExpediente);
        }
        if (log.isDebugEnabled()) {
            log.debug("diasRestantesSubvencionInteresado() : END");
        }

        log.debug(" ******************************> ModuloSubvencionLanbide.diasRestantesSubvencionInteresado() numeroTotalDias : " + numeroTotalDias);
        return numeroTotalDias;
    }//diasRestantesSubvencionInteresado
    
    /**
     * Calcula para el interesado contratado los dias subvencionables que le
     * quedan, se usa al añadir o eliminar manualmente un periodo Tiene en
     * cuenta los expedientes en los que el tercero con rol de persona
     * contratada aparece como interesado.
     *
     * @param codigoOrganizacion
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param numTotalDiasExpediente
     * @param codDecretoAplica
     * @return
     * @throws MeLanbide01Exception
     */
    public Integer diasRestantesSubvencionables2(String codigoOrganizacion, String numExpediente, String codProcedimiento,
            String ejercicio, Integer numTotalDiasExpediente,String codDecretoAplica) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("diasRestantesSubvencionables2() : BEGIN");
        }
        Integer diasRestantes = 0;
        try {
            if (log.isDebugEnabled()) {
                log.debug("Recuperamos la suma de los dias subvencionados");
            }
            Integer numeroTotalDias = diasRestantesSubvencionInteresado2(codigoOrganizacion, numExpediente, codProcedimiento,
                    ejercicio, numTotalDiasExpediente);

            if (log.isDebugEnabled()) {
                log.debug("Recuperamos la actividad subvencionada");
            }
            //Recuperamos la actividad de los campos suplementarios del expediente
            String actividadSubvencionada = getCampoSuplementarioActividadSubvencionada(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            if (log.isDebugEnabled()) {
                log.debug("Recuperamos los maximos dias para las actividades subvencionadas");
            }
            HashMap<String, String> maximosDiasActividadSubvencionada
                    = getMaximosDiasActividadSubvencionada(Integer.valueOf(codigoOrganizacion), codProcedimiento,codDecretoAplica,numExpediente);

            Iterator it = maximosDiasActividadSubvencionada.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                if (e.getKey().equals(actividadSubvencionada)) {
                    Integer numDias = Integer.valueOf(String.valueOf(e.getValue()));
                    diasRestantes = numDias - numeroTotalDias;
                }//if(e.getKey().equals(actividadSubvencionada))
            }//while (it.hasNext())

        } catch (Exception ex) {
            throw new MeLanbide01Exception("No se ha podido calcular el numero de dias restantes subvencionables para el interesado = " + numExpediente);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("diasRestantesSubvencionables2() : END");
        }
        return diasRestantes;
    }//diasRestantesSubvencionables2
    
    /**
     *
     * @param codigoOrganizacion
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param numTotalDiasExpediente
     * @return
     * @throws MeLanbide01Exception
     */
    public Integer diasRestantesSubvencionInteresado2(String codigoOrganizacion, String numExpediente, String codProcedimiento,
            String ejercicio, Integer numTotalDiasExpediente) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("diasRestantesSubvencionInteresado2() : BEGIN");
        }
        Integer numeroTotalDias = new Integer(0);
        try {
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            SalidaIntegracionVO salida = new SalidaIntegracionVO();
            SalidaIntegracionVO salidaExpedientesInteresadoTercero = new SalidaIntegracionVO();
            ExpedienteModuloIntegracionVO expediente = new ExpedienteModuloIntegracionVO();
            ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados = new ArrayList<InteresadoExpedienteModuloIntegracionVO>();
            ArrayList<ExpedienteModuloIntegracionVO> listaExpedientesInteresadoTercero = new ArrayList<ExpedienteModuloIntegracionVO>();
            MeLanbide01Manager meLanbide01Manager = MeLanbide01Manager.getInstance();

            //Recuperamos la actividad de los campos suplementarios del expediente
            String actividadSubvencionada = getCampoSuplementarioActividadSubvencionada(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            //Recuperamos la relacion con la persona dependiente = MotivoSubvencion
            String motivoSubRelacionPersDepe = getCampoSuplementarioRelancionPersonaDependiente(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            //Recuperamos la fecha de nacimiendo de la persona dependiente
            Calendar fechaNacimientoPersonaDependiente
                    = getCampoSuplementarioFecNacPersonaDependiente(numeroTotalDias, ejercicio, numExpediente, codProcedimiento);

            // Se obtiene el valor del desplegable "Resultado Estudio Técnico" para el expediente actual
            String resultadoEstudioTecnicoExpedienteActual = getCampoSuplementarioResultadoEstudioTecnico(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            log.debug("===================> diasRestantesSubvencionInteresado2() valor campo actividadSubvencionada: " + actividadSubvencionada);
            String rolPersonaContratada = getRolPersonaContratada(Integer.valueOf(codigoOrganizacion), codProcedimiento);
            log.debug("===================> diasRestantesSubvencionInteresado2() codigo de rolPersonaContratada: " + rolPersonaContratada);
            salida = el.getExpediente(codigoOrganizacion, numExpediente, codProcedimiento, ejercicio);
            expediente = salida.getExpediente();
            interesados = expediente.getInteresados();
            log.debug("===================> diasRestantesSubvencionInteresado2() número interesados expediente: " + interesados.size());
            log.debug("===================> diasRestantesSubvencionInteresado2() parametros numTotalDiasExpediente: " + numTotalDiasExpediente);

            if (interesados.size() > 0) {
                for (InteresadoExpedienteModuloIntegracionVO interesado : interesados) {
                    //Buscamos al interesado con el rol de persona contratada
                    log.debug("===================> diasRestantesSubvencionInteresado2() rol interesado expediente: " + interesado.getCodigoRol());
                    if (interesado.getCodigoRol() == Integer.valueOf(rolPersonaContratada)) {
                        String documentoInteresado = interesado.getDocumento();
                        String tipoDocumentoInteresado = String.valueOf(interesado.getTipoDocumento());
                        ArrayList<ExpedienteMeLanbide01VO> expInteresados = MeLanbide01DatosCalculoDao.getInstance().getExpedientesTercero(codigoOrganizacion, codProcedimiento, tipoDocumentoInteresado, documentoInteresado, ConnectionUtils.getAdaptSQLBD(codigoOrganizacion));

                        //if(listaExpedientesInteresadoTercero!=null && listaExpedientesInteresadoTercero.size()>0){
                        if (expInteresados != null && expInteresados.size() > 0) {
                            //for(ExpedienteModuloIntegracionVO expedienteInteresado : listaExpedientesInteresadoTercero){
                            for (int i = 0; i < expInteresados.size(); i++) {
                                //Comprobamos que para el expediente del que somos interesados, la actividad subvencionada es la misma que par el 
                                //expediente en el que nos encontramos.
                                String actividadSubvencionadaInteresado = getCampoSuplementarioActividadSubvencionada(Integer.valueOf(codigoOrganizacion), String.valueOf(expInteresados.get(i).getEjercicio()),
                                        expInteresados.get(i).getNumExpediente(), expInteresados.get(i).getCodProcedimiento());

                                log.debug("===================> diasRestantesSubvencionInteresado2() en el expediente " + expInteresados.get(i).getNumExpediente() + " el interesado: " + documentoInteresado + " tiene como actividad subvencionable: " + actividadSubvencionadaInteresado);

                                if ((actividadSubvencionada.equalsIgnoreCase(actividadSubvencionadaInteresado))
                                        && (!expInteresados.get(i).getNumExpediente().equalsIgnoreCase(numExpediente))) {
                                    String motivoSubRelacionPersDepeExpRel = getCampoSuplementarioRelancionPersonaDependiente(Integer.valueOf(codigoOrganizacion), String.valueOf(expInteresados.get(i).getEjercicio()), expInteresados.get(i).getNumExpediente(), codProcedimiento);
                                    if (motivoSubRelacionPersDepe!=null && motivoSubRelacionPersDepe.equalsIgnoreCase(motivoSubRelacionPersDepeExpRel)) {

                                        //Recuperamos del fichero de properties el valor que indica que el estudio tecnico fue rechazado
                                        String valorNegativoEstudioTecnico = getResultadoEstudioTecnicoNegativo(Integer.valueOf(codigoOrganizacion), codProcedimiento);

                                        //Recuperamos el valor del campo del estudio tecnico
                                        String resultadoEstudioTecnico = getCampoSuplementarioResultadoEstudioTecnico(Integer.valueOf(codigoOrganizacion), String.valueOf(expInteresados.get(i).getEjercicio()),
                                                expInteresados.get(i).getNumExpediente(), expInteresados.get(i).getCodProcedimiento());

                                        //Si el estudio tecnico esta denegado no sumamos los dias de ese expediente
                                        if (!valorNegativoEstudioTecnico.equalsIgnoreCase(resultadoEstudioTecnico)) {

                                            //Recuperamos la fecha de nacimiento de la persona dependiente
                                            Calendar fechaNacimientoPersonaDependienteExpRel = getCampoSuplementarioFecNacPersonaDependiente(Integer.valueOf(codigoOrganizacion), String.valueOf(expInteresados.get(i).getEjercicio()),
                                                    expInteresados.get(i).getNumExpediente(), expInteresados.get(i).getCodProcedimiento());

                                            //Si el dia de nacimiento de la persona dependiente coincide con el del expediente relacionado
                                            //ya que la subvencion es para cada persona dependiente.
                                            if ((fechaNacimientoPersonaDependienteExpRel != null && fechaNacimientoPersonaDependiente != null)
                                                    && (fechaNacimientoPersonaDependienteExpRel.equals(fechaNacimientoPersonaDependiente))) {
                                                //Buscamos para cada expediente el numero de dias
                                                numeroTotalDias += meLanbide01Manager.numDiasTotalExpediente(expInteresados.get(i).getNumExpediente(),
                                                        codigoOrganizacion, ConnectionUtils.getAdaptSQLBD(codigoOrganizacion));
                                            }
                                        }
                                }else {
                                    log.debug("El expediente relacionado con el interesado "
                                            + expInteresados.get(i).getNumExpediente() + " no tiene o no coincide el valor del campo Motivo Subvencion / Relacion con la persona dependiente : Hijo, familiar, etc ");
                                }
                                    log.debug("===================> diasRestantesSubvencionInteresado2() iteraccion numeroTotalDias " + numeroTotalDias);
                                }
                            }
                        }
                    }
                }
            }

            log.debug("===================> diasRestantesSubvencionInteresado2() número de dias del expediente: " + numTotalDiasExpediente);

            // Si el expediente actual no toma el valor N en el desplegable "Resultado Estudio Técnico", entonces, se suma al resultado, en caso contrario no
            if (resultadoEstudioTecnicoExpedienteActual != null && !resultadoEstudioTecnicoExpedienteActual.equalsIgnoreCase("N")) {
                numeroTotalDias += numTotalDiasExpediente;
            }

            log.debug("===================> diasRestantesSubvencionInteresado2() numeroTotalDias finales: " + numeroTotalDias);

        } catch (Exception ex) {
            throw new MeLanbide01Exception("No se ha podido calcular el numero de dias restantes subvencionados para el interesado = " + numExpediente);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("diasRestantesSubvencionInteresado2() : END");
        }
        return numeroTotalDias;
    }//diasRestantesSubvencionInteresado2
    
    /**
     * Recupera el valor del campo suplementario del resultado del estudio
     * tecnico
     *
     * @param codOrganizacion
     * @param ejercicio
     * @param numExpediente
     * @param codProcedimiento
     * @return String
     * @throws MeLanbide01Exception
     */
    public String getCampoSuplementarioResultadoEstudioTecnico(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioResultadoEstudioTecnico() : BEGIN");
        }
        String valor = new String();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION
                + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.RESULTESTTEC, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION
                + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_RESULTESTTEC, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorDesplegable();
        }//if(salida.getStatus() == 0)
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioResultadoEstudioTecnico() : END");
        }
        return valor;
    }//getCampoSuplementarioResultadoEstudioTecnico

    /**
     * Recupera del fichero de propiedades del modulo el valor definido como
     * resultado del estudio tecnico negativo
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @return
     */
    public String getResultadoEstudioTecnicoNegativo(Integer codOrganizacion, String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getResultadoEstudioTecnicoNegativo() : BEGIN");
        }
        String valor = new String();
        try {
            valor = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.FICHERO_CONFIGURACION
                    + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.ESTUDIO_TECNICO_NEGATIVO, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el valor negativo del resultado del estudio tecnico " + ex.getMessage());
            throw new MeLanbide01Exception("Se ha producido un error recuperando el valor negativo del resultado del estudio tecnico", ex);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("getResultadoEstudioTecnicoNegativo() : END");
        }
        return valor;
    }//getResultadoEstudioTecnicoNegativo
    
        /**
     * Funcion que devuelve un hashmap con la clave del tipo de actividad y el
     * valor que es el maximo de dias subvencionables para esa actividad.
     *
     * @param codOrganizacion
     * @param codProcedimiento
     * @param codDecretoAplica
     * @param numeroExpediente
     * @return HashMap<String, String>
     */
    public  HashMap<String, String> getMaximosDiasActividadSubvencionada(Integer codOrganizacion, String codProcedimiento,String codDecretoAplica, String numeroExpediente) {
        if (log.isDebugEnabled()) {
            log.debug("getMaximosDiasActividadSubvencionada() : BEGIN");
        }
        HashMap<String, String> maximosDiasActividadSubvencionada = new HashMap<String, String>();
        String tiposActividad = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPOS_ACTIVIDAD_SUBVENCIONADA, MeLanbide01Constantes.FICHERO_CONFIGURACION);
        // Por defecto aplicamos losvalores iniciales del primer decreto
        String maximoDias = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.MAX_DIAS_TIPOS_ACTIVIDAD_SUBVENCIONADA, MeLanbide01Constantes.FICHERO_CONFIGURACION);;
        log.info("Datos Decreto: " + codDecretoAplica);
        // Desde el decreto de 2019 se dan mas dias si la pers. contratada es Mujer. 
        // y Mas por cada hijos desde el 2 En caso de partos Multiples
        //Si no hay datos no aplicamos el plus
        if(Melanbide01DecretoExpedienteEnum.D2019_164.getCodigoDecreto().equalsIgnoreCase(codDecretoAplica)){
            // Sexo de la persona Contratada y Sustiruida
            Map<String, Integer> datosCalculoDias = null;
            try {
                datosCalculoDias = MeLanbide01Manager.getInstance().getDatosCalculoNumMaxDias_Dec164_2019(codOrganizacion, codProcedimiento, numeroExpediente, MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.getCodigo(), MeLanbide01RolesCONCMEnum.PERSONA_CONTRATADA.getCodigo());                
            } catch (Exception e) {
                log.error("Error al recuperar los datos para calcular el maximo de dias aplicables en la subvencion .. " + codDecretoAplica + " " +e.getMessage(),e);
            }
            if(datosCalculoDias!=null && datosCalculoDias.get("motivoSubvencion")!=null){
                Integer motivoSubvencion = datosCalculoDias.get("motivoSubvencion");
                Integer codSexoPContratada = datosCalculoDias.get(MeLanbide01RolesCONCMEnum.PERSONA_CONTRATADA.toString());
                Integer codSexoPSustituida = datosCalculoDias.get(MeLanbide01RolesCONCMEnum.PERSONA_SUSTITUIDA.toString());
                Integer totalHijosCausantesSubv = datosCalculoDias.get("totalHijosCausantesSubv");
                Integer factorBaseMaximoDias = datosCalculoDias.get("factorBaseMaximoDias");
                Integer factorPlus = datosCalculoDias.get("factorPlus");
                log.info("Datos codSexoPContratada/codSexoPSustituida/motivoSubvencion/totalHijosCausantesSubv: " + codSexoPContratada + "/" +codSexoPSustituida+ "/" + motivoSubvencion + "/" + totalHijosCausantesSubv);
                maximoDias = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                        + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento
                        + MeLanbide01Constantes.MAX_DIAS_TIPOS_ACTIVIDAD_SUBVENCIONADA + MeLanbide01Constantes.BARRA + codDecretoAplica + MeLanbide01Constantes.BARRA + motivoSubvencion,
                         MeLanbide01Constantes.FICHERO_CONFIGURACION);
                if(codSexoPContratada!=null && codSexoPSustituida!=null){
                    if(Melanbide01SexoPersonaEnum.COD_SEXO_MUJER.getCodigo().equalsIgnoreCase(codSexoPContratada.toString())
                            && Melanbide01SexoPersonaEnum.COD_SEXO_HOMBRE.getCodigo().equalsIgnoreCase(codSexoPSustituida.toString())) {
                        maximoDias = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento 
                                + MeLanbide01Constantes.MAX_DIAS_TIPOS_ACTIVIDAD_SUBVENCIONADA +MeLanbide01Constantes.BARRA+codDecretoAplica+MeLanbide01Constantes.BARRA+motivoSubvencion+MeLanbide01Constantes.PERSONA_CONTRATADA_MUJER
                                , MeLanbide01Constantes.FICHERO_CONFIGURACION);
                    }
                }else{
                    log.info("Sexo persona Contratada/Sustituida no recuperado... No aplicamos extension Dias");
                }
                // Comprobamos plus dias por numero de hijos
                if (0 == motivoSubvencion && totalHijosCausantesSubv != null && totalHijosCausantesSubv>1 ){
                    log.info("Expediente con PLUS por numero de Hijos: " + numeroExpediente + " : " + totalHijosCausantesSubv );
                    String plusPorHijo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                            + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento
                            + MeLanbide01Constantes.MAX_DIAS_TIPOS_ACTIVIDAD_SUBVENCIONADA + MeLanbide01Constantes.BARRA + codDecretoAplica + MeLanbide01Constantes.BARRA + motivoSubvencion + MeLanbide01Constantes.PLUS_POR_HIJO,
                             MeLanbide01Constantes.FICHERO_CONFIGURACION);
                    if(plusPorHijo!=null){
                        String[] splitPlusPorHijo= plusPorHijo.split(";");
                        String[] splitMaximoDiasPreview = maximoDias.split(";");
                        if(splitPlusPorHijo!= null && splitMaximoDiasPreview!=null
                                && splitPlusPorHijo.length==splitMaximoDiasPreview.length){
                            String newMaximoDias = "";
                            for(int i = 0; i < splitMaximoDiasPreview.length; i++) {
                                Integer maxDias = Integer.valueOf(splitMaximoDiasPreview[i]);
                                Integer plusHij = Integer.valueOf(splitPlusPorHijo[i]);
                                factorBaseMaximoDias = (factorBaseMaximoDias!=null ? factorBaseMaximoDias : 1);
                                factorPlus = (factorPlus!=null ? factorPlus : 0);
                                maxDias= ((maxDias * factorBaseMaximoDias) // Numero Hijos, Caso Multiples Se toma uno que es la base para el primer maximo 1278,  los demsa son conplus
                                        +(plusHij*(factorPlus)) // Numero de hijos en parto multiple despues del primero
                                        ); 
                                newMaximoDias = (!newMaximoDias.isEmpty()?newMaximoDias+";"+String.valueOf(maxDias):String.valueOf(maxDias));
                            }
                            log.info("Nuevo numeroDiasConPlusHijo : Previo " + maximoDias+ " Calculado : " + newMaximoDias);
                            if(!newMaximoDias.isEmpty())
                                maximoDias=newMaximoDias;
                        }else{
                            log.info("Opciones calculo PLUS por hijo no estan correctamente configuradas. "
                                    + Arrays.toString(splitPlusPorHijo)
                                    + " ::: "
                                    + Arrays.toString(splitMaximoDiasPreview)
                            );
                        }
                    }else{
                        log.info("Datos PLus Dias por Hijo no recuperados");
                    }
                }
            }else{
                log.error("Datos para calcular maximo de dia en "+ codDecretoAplica + " no recuperados. "
                        + " Se devuelven valores por defecto : " +tiposActividad + "=>" + maximoDias);
            }           
        }else if(Melanbide01DecretoExpedienteEnum.D2010_177.getCodigoDecreto().equalsIgnoreCase(codDecretoAplica)){
            /**
             * #549259
             * 18/02/2022 Sumar dias para casos de gemelos no se tenia en cuenta por misma fecha de nacimiento 
             **/
            int numHijosPartoMultiple = 1;
            try {
                String valorDesplegable=meLanbide01CamposSupleExpediente.getCampoSuplementarioDesplegableExpediente(codOrganizacion, "NUMHIJCS", numeroExpediente);
                numHijosPartoMultiple = (valorDesplegable!=null && !valorDesplegable.isEmpty() ?Integer.valueOf(valorDesplegable):1);
            } catch (MeLanbide01Exception e) {
                log.error("Al leer dato de numero de hijos que causa la subvencion ... " + codDecretoAplica + " " + numeroExpediente, e);
            } catch (NumberFormatException e) {
                log.error("Error al covertir un valor a numerico ... " + e.getMessage(), e);
            }
            String[] splitMaximoDiasPreview = maximoDias.split(";");
            if (splitMaximoDiasPreview != null) {
                String newMaximoDias = "";
                for (int i = 0; i < splitMaximoDiasPreview.length; i++) {
                    Integer maxDias = Integer.valueOf(splitMaximoDiasPreview[i]);
                    maxDias = maxDias * numHijosPartoMultiple; 
                    newMaximoDias = (!newMaximoDias.isEmpty() ? newMaximoDias + ";" + String.valueOf(maxDias) : String.valueOf(maxDias));
                }
                log.info("Nuevo numeroDiasConPlusHijo - Decreto "+codDecretoAplica +": Previo " + maximoDias+ " Calculado : " + newMaximoDias);
                            if(!newMaximoDias.isEmpty())
                                maximoDias=newMaximoDias;
            }
            
        }
        String[] splitTiposActividad = tiposActividad.split(";");
        String[] splitMaximoDias = maximoDias.split(";");        

        log.debug(" ======================> ModuloSubvencionLanbide.getMaximosDiasActividadSubvencionada() tiposActividad: " + tiposActividad);
        log.debug(" ======================> ModuloSubvencionLanbide.getMaximosDiasActividadSubvencionada() maximoDias: " + maximoDias);

        for (int i = 0; i < splitTiposActividad.length; i++) {
            maximosDiasActividadSubvencionada.put(splitTiposActividad[i], splitMaximoDias[i]);
        }//for(int i=0; i<splitTiposActividad.length; i++)
        if (log.isDebugEnabled()) {
            log.debug("getMaximosDiasActividadSubvencionada() : END");
        }
        return maximosDiasActividadSubvencionada;
    }//getMaximosDiasActividadSubvencionada
    
    public Calendar getCampoSuplementarioFecInicioContratoInterinidad(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioFecInicioContratoInterinidad() : BEGIN");
        Calendar valor = null;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.CODIGO_CAMPO_SUPLEMENTARIO_FECINICONTRATO_INTERINIDAD,MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.TIPO_CAMPO_SUPLEMENTARIO_FECINICONTRATO_INTERINIDAD,MeLanbide01Constantes.FICHERO_CONFIGURACION);
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            valor = Calendar.getInstance();
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorFecha();
        }//if(salida.getStatus() == 0)
        log.info("getCampoSuplementarioFecInicioContratoInterinidad() : END");
        return valor;
    }
    
    public Calendar getCampoSuplementarioFecFinContratoInterinidad(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        log.info("getCampoSuplementarioFecFinContratoInterinidad() : BEGIN");
        Calendar valor = null;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.CODIGO_CAMPO_SUPLEMENTARIO_FECFINCONTRATO_INTERINIDAD,MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.TIPO_CAMPO_SUPLEMENTARIO_FECFINCONTRATO_INTERINIDAD,MeLanbide01Constantes.FICHERO_CONFIGURACION);
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            valor = Calendar.getInstance();
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorFecha();
        }//if(salida.getStatus() == 0)
        log.info("getCampoSuplementarioFecFinContratoInterinidad() : END");
        return valor;
    }
    
    public String getCampoSuplementarioPorcJornLabContratadaContratoInterinidad(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioPorcJornLabContratadaContratoInterinidad() : BEGIN");
        }
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.CODIGO_CAMPO_SUPLEMENTARIO_PERCONTJLCONTPORC,MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(String.valueOf(codOrganizacion) + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.TIPO_CAMPO_SUPLEMENTARIO_PERCONTJLCONTPORC,MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String valor = "";
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorNumero();
        }
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioPorcJornLabContratadaContratoInterinidad() : END");
        }
        return valor;
    }
    
    public String getCampoSuplementarioPorcJornadaSustitucionContratoInterinidad(Integer codOrganizacion, String ejercicio, String numExpediente,
            String codProcedimiento) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioPorcJornadaSustitucionContratoInterinidad() : BEGIN");
        }
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        String campo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.JORNPERSCONT,MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String tipoCampo = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.MODULO_INTEGRACION
                + MeLanbide01Constantes.FICHERO_CONFIGURACION + MeLanbide01Constantes.BARRA + codProcedimiento + MeLanbide01Constantes.TIPO_JORNPERSCONT,MeLanbide01Constantes.FICHERO_CONFIGURACION);
        String valor = "";
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), ejercicio, numExpediente,
                codProcedimiento, campo, Integer.parseInt(tipoCampo));
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            valor = campoSuplementario.getValorNumero();
        }
        if (log.isDebugEnabled()) {
            log.debug("getCampoSuplementarioPorcJornadaSustitucionContratoInterinidad() : END");
        }
        return valor;
    }
    
    public Melanbide01Decreto getDecretoAplicableExptePorFecInicioPeriodosOrFecIniAcciSubvConcedida(Integer codigoOrganizacion, String numeroExpediente){
        Melanbide01Decreto respuesta=null;
        try {
            String fechaInicialPeriodoSubvencio = "";
            AdaptadorSQLBD adapt  = ConnectionUtils.getAdaptSQLBD(String.valueOf(codigoOrganizacion));
            DatosCalculoVO datosCalculo = MeLanbide01Manager.getInstance().getDatosCalculo(numeroExpediente, String.valueOf(codigoOrganizacion),
                    adapt);
            if (datosCalculo != null && datosCalculo.getPeriodos() != null && datosCalculo.getPeriodos().size() > 0) {
                // Los periodos se ordenan en la SQL   FECHA_INICIO ASC,FECHA_FIN ASC Cgemos la fecha inicio del primer periodo
                fechaInicialPeriodoSubvencio = datosCalculo.getPeriodos().get(0).getFechaInicioAsString();
            } else {
                // Leemos fecha Inicio actuacion subvencion concedida
                Integer ejercio = Utilities.getEjercicioFromNumExpediente(numeroExpediente);
                String codigoProcedimiento = Utilities.getCodigoProcedimientoFromNumExpediente(numeroExpediente);
                Calendar fechaInicio = getCampoSuplementarioFecInicioAccionSubvConcedida(codigoOrganizacion, String.valueOf(ejercio), numeroExpediente, codigoProcedimiento);
                if (fechaInicio != null) {
                    fechaInicialPeriodoSubvencio = formatFecha_dd_MM_yyyy.format(fechaInicio.getTime());
                }
            }
            if (fechaInicialPeriodoSubvencio != null && !fechaInicialPeriodoSubvencio.isEmpty()) {
                respuesta = MeLanbide01Manager.getInstance().getDecretoAplicableExpediente(formatFecha_dd_MM_yyyy.parse(fechaInicialPeriodoSubvencio), adapt);
            }                
        }catch (Exception e) {
            log.error("Error getDecretoAplicableExptePorFecInicioPeriodoOFecIniAcciSubvConcedida " + e.getMessage(), e);
            respuesta=null;
        }
        return respuesta;
    }
    
    /**
     * Calcula para el interesado con rol de persona contratada, los dias
     * subvencionables que le quedan, se usa al cargar la pagina o modificar los
     * datos suplementarios del expediente Tiene en cuenta los expedientes en
     * los que el tercero con rol de persona contratada aparece como interesado.
     *
     * @param codigoOrganizacion
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param codDecretoAplica
     * @throws MeLanbide01Exception
     */
    public Integer diasRestantesSubvencionables(String codigoOrganizacion, String numExpediente, String codProcedimiento,
            String ejercicio,String codDecretoAplica) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("diasRestantesSubvencionables2() : BEGIN");
        }
        Integer diasRestantes = new Integer(0);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Recuperamos la suma de los dias subvencionados");
            }
            Integer numeroTotalDias = diasRestantesSubvencionInteresado(codigoOrganizacion, numExpediente, codProcedimiento,
                    ejercicio);

            log.debug(" **********************> ModuloSubvencionLanbide.diasRestantesSubvencionables() diasRestantesSubvencionInteresado: " + numeroTotalDias);

            if (log.isDebugEnabled()) {
                log.debug("Recuperamos la actividad subvencionada");
            }
            //Recuperamos la actividad de los campos suplementarios del expediente
            String actividadSubvencionada = getCampoSuplementarioActividadSubvencionada(Integer.valueOf(codigoOrganizacion), ejercicio, numExpediente, codProcedimiento);

            log.debug(" **********************> ModuloSubvencionLanbide.diasRestantesSubvencionables() actividadSubvencionada: " + actividadSubvencionada);

            if (log.isDebugEnabled()) {
                log.debug("Recuperamos los maximos dias para las actividades subvencionadas");
            }
            HashMap<String, String> maximosDiasActividadSubvencionada
                    = getMaximosDiasActividadSubvencionada(Integer.valueOf(codigoOrganizacion), codProcedimiento,codDecretoAplica,numExpediente);

            Iterator it = maximosDiasActividadSubvencionada.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                if (e.getKey().equals(actividadSubvencionada)) {
                    Integer numDias = Integer.valueOf(String.valueOf(e.getValue()));
                    diasRestantes = numDias - numeroTotalDias;
                }//if(e.getKey().equals(actividadSubvencionada))
            }//while (it.hasNext())

        } catch (Exception ex) {
            log.error("Error al calcular el numero de dias restantes: " + ex.getMessage(),ex);
            throw new MeLanbide01Exception("No se ha podido calcular el numero de dias restantes subvencionables para el interesado = " + numExpediente);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("diasRestantesSubvencionables2() : END");
        }

        log.debug(" **********************> ModuloSubvencionLanbide.diasRestantesSubvencionables() diasRestantes: " + diasRestantes);
        return diasRestantes;
    }//diasRestantesSubvencionables2
    
    /**
     * Calcula el numero de dias entre dos fechas, Si estas son diferentes de Null y endDate es posterior.
     * Tiene en cuenta los intervalos. 01/01/2001 - 01/01/2001 = 1 Dia
     * @param startDate
     * @param endDate
     * @return Numero de dias entre dos fechas incluyendo los intervalos.
     */
    public int getNumeroDiasEntreDosFecha(Calendar startDate,Calendar endDate){
        Integer respuesta=0;
        if(startDate!=null && endDate!=null
                && endDate.after(startDate)){
            respuesta=1; // Para que tenga en cuenta los intervalos : 01/01/2001 - 01/01/201 = 1 Dia PEj.
            while (startDate.before(endDate)) {
                respuesta++;
                startDate.add(Calendar.DATE, 1);
            }
        }
        return respuesta;
    }
    
        /**
     * Calcula el porcentaje subvencionado
     *
     * @param reducPersSust
     * @param jornPersSust
     * @param jornPersCont
     * @return
     * @throws MeLanbide01Exception
     */
    public String calcularPorcSubvenc(String reducPersSust, String jornPersSust, String jornPersCont) throws MeLanbide01Exception {
        if (log.isDebugEnabled()) {
            log.debug("calcularPorcSubvenc() : BEGIN");
        }
        Double resultado = null;
        String res;
        try {
            Double dobReducPerSust = new Double(reducPersSust);
            Double doubleJornPersSust = new Double(jornPersSust);
            Double doubleJornPersCont = new Double(jornPersCont);
            //formula =  ((%MeLanbide01Constantes.REDUCPERSSUST) X (%JORNPERSSUST))/(%JORNPERSCONT)
            resultado = ((dobReducPerSust) * (doubleJornPersSust) / (doubleJornPersCont));
            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
            DecimalFormat formateador = new DecimalFormat("####.##", simbolo);
            res = formateador.format(resultado);
        } catch (Exception ex) {
            log.error("Se ha producido un error calculando el porcentaje subvencionado", ex);
            throw new MeLanbide01Exception("Se ha producido un error calculando el porcentaje subvencionado", ex);
        }//try-catch
        if (log.isDebugEnabled()) {
            log.debug("calcularPorcSubvenc() : END");
        }
        return res;
    }//calcularPorcSubvenc
    
}//class
