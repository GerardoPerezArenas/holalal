/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide54.job;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.agora.business.registro.RegistroValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide54.*;
import es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altaagencia.IWACALAS;
import es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altaagencia.IWACALASResponse;
import es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altaagencia.IWACALAS_Service;
import es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altacentro.IWACACTS;
import es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altacentro.IWACACTSResponse;
import es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altacentro.IWACACTS_Service;
import es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.consultaagencia.*;
import es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.consultaagencia.IWACCIFSResponse.AREACOMUNICACION.ADSDATOSSALIDA.DATOSs.DATOS;
import es.altia.flexia.integracion.moduloexterno.melanbide54.dao.MeLanbide54DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.manager.MeLanbide54Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide54.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide54.util.ConstantesMeLanbide54;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.CentroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.RegistroAACCVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.RegistroBatchVO;
import es.altia.flexia.notificacion.vo.NotificacionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.BDException;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import java.net.URL;
import javax.xml.namespace.QName;

import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionBindingStub;
import es.altia.webservice.client.tramitacion.ws.wsimpl.WSTramitacionServiceLocator;
import es.altia.webservice.client.tramitacion.ws.wto.CondicionFinalizacionVO;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.FlujoFinalizacionVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.IdTramiteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InfoConexionVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.RegistroAsociadoVO;
import es.altia.webservice.client.tramitacion.ws.wto.RespuestasTramitacionVO;
import es.altia.webservice.client.tramitacion.ws.wto.TerceroVO;
import es.altia.webservice.client.tramitacion.ws.wto.TramiteVO;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TramitacionAutomaticaAgenciasAACCJob implements Job {

    private static final Integer OP_INICIO_EXPEDIENTE = 1;
    private static final Integer OP_MISGESTIONES_INICIO_EXPEDIENTE = 2;
    private static final Integer OP_MISGESTIONES_INICIO_OPERACION = 3;
    private static final Integer OP_ASOCIAR_REGISTRO = 4;
    private static final Integer OP_BUSCAR_XML = 5;
    private static final Integer OP_GRABAR_DATOS_SUPLEMENTARIOS = 6;
    private static final Integer OP_GRABAR_EXPEDIENTE_EXTENSIÓN = 7;
    private static final Integer OP_AVANZAR_TRAMITE = 8;
    private static final Integer OP_INSERTAR_NOTIFICACION = 9;
    private static final Integer OP_SW_CONSULTA_AGENCIA = 11;
    private static final Integer OP_SW_ALTA_AGENCIA = 12;
    private static final Integer OP_SW_ALTA_CENTRO = 13;

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        Connection con = null;
        try {
            int pepe = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.error("jec.getRefireCount(): " + pepe);
            Trigger pepito = jec.getTrigger();

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide54.CAMPO_SERVIDOR, ConstantesMeLanbide54.FICHERO_PROPIEDADES);

            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.error("servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    boolean dev = false;
                    int numIntentos = 0;
                    //String numExpediente = "";
                    int anoRegistro = 0;
                    int numRegistro = 0;
                    int id = 0;
                    String[] params = new String[]{"ORACLE"};
                    try {
                        log.error("Execute lanzado " + System.getProperty("weblogic.Name"));
                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COD_ORG, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                        //int codOrg = 0;      //pruebas
                        //int codOrg = 1;      //real
                        String codProc = ConfigurationParameter.getParameter(ConstantesMeLanbide54.COD_PROCEDIMIENTO, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
                        //Recogemos el codigoUor del properties:
                        int codigoUOR = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COD_UOR, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                        log.error("codigo UOR " + codigoUOR);
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter(ConstantesMeLanbide54.DOS_ENTORNOS, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                        // Se declara el objeto binding para las llamadas a los SW de WSTramitacion
                        String wsUrl = ConfigurationParameter.getParameter(ConstantesMeLanbide54.URL_WS_REGISTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
                        URL url = new URL(wsUrl);
                        WSTramitacionBindingStub binding = null;
                        try {
                            binding = (WSTramitacionBindingStub) new WSTramitacionServiceLocator().getWSTramitacionPort(url);
                            log.error("resultado binding : " + binding);
                            binding.setTimeout(60000);
                        } catch (Exception ex) {
                            log.error("eee", ex);
                        }
                        //Informacion de la conexion
                        InfoConexionVO inf = new InfoConexionVO();
                        inf.setOrganizacion(String.valueOf(codOrg));
                        inf.setAplicacion("RGI");
                        log.error("------------Info conexion----------");
                        log.error("Organizacion info " + inf.getOrganizacion());
                        log.error("Aplicacion inf " + inf.getAplicacion());
                        log.error("------------Info conexion----------");

                        RegistroBatchVO registroBatch = new RegistroBatchVO();
                        boolean continuar = false;
                        while (codOrg < 2) {

                            con = this.getAdaptSQLBD(String.valueOf(codOrg)).getConnection();
                            log.error("en el while de tokens codOrg: " + codOrg);

                            ExpedienteVO expediente = new ExpedienteVO();
                            String numeroExpediente = null;
                            String numExpedientePend = "";
                            String cif = "";
                            String email = "";
                            String codAgencia = "";
                            String codCentro = "";
                            CentroVO centro = new CentroVO();
                            TerceroVO tercero = new TerceroVO();
                            int codtramite = 0;
                            int anoExp = 0;
                            int numExp = 0;
                            int codIntTramAvanzar = 0;
                            byte[] archivoXML = null;
                            boolean existeAgencia = false;
                            TramiteVO idTramite = new TramiteVO();
                            IdTramiteVO idTram = new IdTramiteVO();
                            IdTramiteVO idTramAvanzar = new IdTramiteVO();
                            CondicionFinalizacionVO condFinalizacion = new CondicionFinalizacionVO();
                            /*                        
                         * Buscar los registros telemáticos pendientes                       
                             */
                            MeLanbide54DAO meLanbide54DAO = MeLanbide54DAO.getInstance();
                            List<RegistroAACCVO> registros = meLanbide54DAO.getRegistrosTelematicosAACC(con);
                            log.debug("RegistrosTelematicosAACC: " + registros.size());

                            int idRegBatch = 0;
                            List<RegistroBatchVO> expedientesRelanzar = meLanbide54DAO.getExpedientesTelematicosAACC(con);
                            log.debug("getExpedientesTelematicosAACC: " + expedientesRelanzar.size());

                            for (Iterator<RegistroAACCVO> i = registros.iterator(); i.hasNext();) {
                                RegistroAACCVO registro = i.next();
                                anoRegistro = registro.getResEje();
                                numRegistro = registro.getResNum();
                                log.debug("numRegistro: " + numRegistro);
                                //Por cada registro hay que abrir un expediente, pero de momento pongo que sólo abra uno para probar                                                   

                                // if(numRegistro == 134){    //OJO quitar  
                                if ((numRegistro == 578) || (numRegistro == 57)) {   //OJO quitar       
                                    //   if(numRegistro==483){   //OJO quitar                               
                                    //   if(numRegistro==807){   //OJO quitar                         
                                    //   if(numRegistro==99999){   //OJO quitar                                      
                                    expediente = inicioExpedienteAACC(codigoUOR, codOrg, registro, codProc, inf, binding);
                                    log.debug("sale de inicioExpedienteAACC");
                                    numeroExpediente = expediente.getIdExpedienteVO().getNumeroExpediente();
                                    log.debug("numeroExpediente: " + numeroExpediente);
                                    if (numeroExpediente != null) {
                                        //String numExpediente = expediente.getIdExpedienteVO().getNumeroExpediente();                                       
                                        ////tercero=expediente.getInteresados()[0].getTercero();                                
                                        tercero = MeLanbide54DAO.getInstance().getTercero(codOrg, registro.getResEje(), registro, con);
                                        //Una vez generado el nuevo expediente, buscamos el XML en el registro
                                        // Devuelve el XML en formato byte[]. Hay que guardar los datos suplementarios de Flexia y cargarExpedienteExtension . 
                                        log.debug("antes de buscar xml ");
                                        archivoXML = meLanbide54DAO.BuscarXML(con, registro);
                                        log.debug("despues de buscar xml ");
                                        registroBatch.setEjerRegistro(registro.getResEje());
                                        registroBatch.setNumRegistro(registro.getResNum());
                                        registroBatch.setNumExpediente(numeroExpediente);
                                        registroBatch.setCodTramite(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES)));
                                        registroBatch.setOperacion("buscar XML");
                                        registroBatch.setRelanzar(0);
                                        registroBatch.setCodOperacion(OP_BUSCAR_XML);
                                        registroBatch.setObservaciones("");
                                        if (archivoXML == null) {
                                            registroBatch.setResultado("KO");
                                            registroBatch.setObservaciones("No existe xml (FLX_DATOS_INTEGRACION_SOLICITUD) en el registro (tabla R_RED)");
                                            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                                            ErrorBean err = new ErrorBean();
                                            err.setIdError("TRAMITACION_AACC_005");
                                            err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. No existe xml (FLX_DATOS_INTEGRACION_SOLICITUD) en el registro: " + anoRegistro + "/" + numRegistro);
                                            err.setSituacion("JobExecute");
                                            String causa = "";
                                            String error = "";
                                            grabarError(err, error, causa, numeroExpediente);
                                        } else {
                                            registroBatch.setResultado("OK");
                                            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                            // Grabado de datos suplementarios a partir del XML.
                                            boolean grabado = false;
                                            grabado = grabadoDatosSuplementariosXML(archivoXML, codOrg, codProc, con, numeroExpediente, registro);
                                            // posible fallo TRAMITACION_AACC_006
                                            // llamada a cargarExpedienteExtensión
                                            MELANBIDE54 meLanbide54 = MELANBIDE54.getInstance();
                                            try {
                                                String xmlString = new String(archivoXML, "ISO-8859-1");
                                                meLanbide54.cargarExpedienteExtension(codOrg, numeroExpediente, xmlString);
                                                log.debug("Después de cargarExpedienteExtension ");
                                                //Comprobar que ha cargado los datos del centro y grabar en la tabla 
                                                if (MeLanbide54DAO.getInstance().existenDatosExtension(con, numeroExpediente)) {
                                                    registroBatch.setResultado("OK");
                                                    registroBatch.setObservaciones("");
                                                    continuar = true;
                                                } else {
                                                    registroBatch.setResultado("KO");
                                                    registroBatch.setObservaciones("No existe en la tabla MELANBIDE54_CENTROS");
                                                }
                                                registroBatch.setEjerRegistro(registro.getResEje());
                                                registroBatch.setNumRegistro(registro.getResNum());
                                                registroBatch.setNumExpediente(numeroExpediente);
                                                registroBatch.setCodTramite(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES)));
                                                registroBatch.setOperacion("cargarExpedienteExtension");
                                                registroBatch.setRelanzar(0);
                                                registroBatch.setCodOperacion(OP_GRABAR_EXPEDIENTE_EXTENSIÓN);

                                                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                                            } catch (Exception e) {
                                                registroBatch.setResultado("KO");
                                                registroBatch.setEjerRegistro(registro.getResEje());
                                                registroBatch.setNumRegistro(registro.getResNum());
                                                registroBatch.setNumExpediente(numeroExpediente);
                                                registroBatch.setCodTramite(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES)));
                                                registroBatch.setOperacion("cargarExpedienteExtension");
                                                registroBatch.setRelanzar(0);
                                                registroBatch.setCodOperacion(OP_GRABAR_EXPEDIENTE_EXTENSIÓN);
                                                registroBatch.setObservaciones("Excepción al cargarExpedienteExtension");
                                                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                                                ErrorBean err = new ErrorBean();
                                                err.setIdError("TRAMITACION_AACC_007");
                                                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al ejecutar la operacion cargarExpedienteExtension en el expediente " + numeroExpediente);
                                                err.setSituacion("JobExecute");
                                                String causa = e.getCause().getMessage();
                                                String error = e.getMessage();

                                                grabarError(err, error, causa, numeroExpediente);
                                            }
                                            /*                                                             
                                    TramiteVO idTramite = new TramiteVO();
                                    //Recogemos el Código interno del trámite del properties:
                                    int codIntTram=Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA,ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                    log.error("codigo interno Trámite 1001: "+codIntTram);                                     
                                    IdTramiteVO idTram = new IdTramiteVO(codIntTram,1);  // La ocurrencia es 1 
                                    idTramite.setId(idTram);                                     
                                    CondicionFinalizacionVO condFinalizacion = new CondicionFinalizacionVO();  
//                                    int codigoUOR=Integer.parseInt(ConfigurationParameter.getParameter("COD_UOR",ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                    idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                    idTramite.setUsuarioFin(registro.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   
                                             */

//--------------------------------------------------
                                            idTramite = new TramiteVO();
                                            idTram = new IdTramiteVO();
                                            idTramAvanzar = new IdTramiteVO();
                                            condFinalizacion = new CondicionFinalizacionVO();
                                            FlujoFinalizacionVO flujo = new FlujoFinalizacionVO();
                                            IdTramiteVO[] listaRespuesta = new IdTramiteVO[1];
                                            String tipoFin = "";

                                            if (continuar) {
                                                //Llamada al SW Consulta Agencia  --> Si me devuelve IAG0018, es que la agencia no existe
                                                existeAgencia = false;
                                                //-----------------------------------              
                                                IWACCIFSResponse.AREACOMUNICACION areacomunicacionResponseC = null;
                                                cif = tercero.getDoc();
                                                areacomunicacionResponseC = llamadaSWConsultaAgencia(con, cif, numeroExpediente, registro);
                                                if (areacomunicacionResponseC != null) {
                                                    //Si no existe la Agencia -->   <ADC-COD-ERROR>IAG0018</ADC-COD-ERROR>
                                                    //Si existe la Agencia -->      <ADC-COD-ERROR>IAG0017</ADC-COD-ERROR>       
                                                    String codErrorIWACCIFS = areacomunicacionResponseC.getADCAREACONTROL().getADCCODERROR();
                                                    String existe_agenciaSW = ConfigurationParameter.getParameter(ConstantesMeLanbide54.EXISTE_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
                                                    String no_existe_agenciaSW = ConfigurationParameter.getParameter(ConstantesMeLanbide54.NO_EXISTE_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES);

                                                    if (existe_agenciaSW.equals(codErrorIWACCIFS)) {
                                                        existeAgencia = true;
                                                        log.debug("Existe Agencia");
                                                        log.debug("ADS-NUM-REG: " + areacomunicacionResponseC.getADSDATOSSALIDA().getADSNUMREG());
                                                        List<DATOS> datosSalidaIWACCIFS = areacomunicacionResponseC.getADSDATOSSALIDA().getDATOSs().getDATOS();

                                                        for (Iterator<DATOS> i2 = datosSalidaIWACCIFS.iterator(); i2.hasNext();) {
                                                            DATOS datoSalidaIWACCIFS = i2.next();
                                                            log.debug("ADS-NIFE: " + datoSalidaIWACCIFS.getADSNIFE());
                                                            log.debug("ADS-CO-AGENC: " + datoSalidaIWACCIFS.getADSCOAGENC());
                                                            log.debug("ADS-FX-RESOL: " + datoSalidaIWACCIFS.getADSFXRESOL());
                                                            log.debug("ADS-NOMAGE: " + datoSalidaIWACCIFS.getADSNOMAGE());
                                                            log.debug("ADS-FX-BAJA: " + datoSalidaIWACCIFS.getADSFXBAJA());
                                                            log.debug("ADS-MOTBAJA: " + datoSalidaIWACCIFS.getADSMOTBAJA());
                                                            if ((!"".equals(datoSalidaIWACCIFS.getADSCOAGENC())) && ("".equals(datoSalidaIWACCIFS.getADSFXBAJA()))) {
                                                                log.debug("Agencia a guardar en datos suplementarios: " + datoSalidaIWACCIFS.getADSCOAGENC());
                                                                codAgencia = datoSalidaIWACCIFS.getADSCOAGENC();
                                                            }
                                                        }
                                                    } else {
                                                        if (no_existe_agenciaSW.equals(codErrorIWACCIFS)) {
                                                            existeAgencia = false;
                                                            log.debug("NO existe Agencia con el CIF: " + areacomunicacionResponseC.getADEDATOSENTRADA().getADENIFE());
                                                        } else {
                                                            continuar = false;
                                                            log.debug("Error en el SW de Consulta Agencia");
                                                        }
                                                    }
                                                } else {
                                                    continuar = false;
                                                }
                                                //-----------------------------------                                
                                                if (!existeAgencia && continuar) {
                                                    //Buscar en qué trámite está
                                                    codtramite = meLanbide54DAO.buscarTramite(con, numeroExpediente);
                                                    if (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) {
                                                        idTram.setCodTramite(codtramite);
                                                        idTram.setOcurrencia(1);
                                                        idTramite.setId(idTram);
                                                        idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                                        idTramite.setUsuarioFin(registro.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   						
                                                        tipoFin = meLanbide54DAO.BuscarTipoFinalizacion(con, codOrg, codProc, codtramite);
                                                        condFinalizacion.setTipoFinalizacion(tipoFin);
                                                        flujo.setTipoApertura(1);   // 1--> Obligatoria
                                                        //Recogemos el Código interno del trámite a iniciar del properties:
                                                        codIntTramAvanzar = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                                        idTramAvanzar.setCodTramite(codIntTramAvanzar);
                                                        idTramAvanzar.setOcurrencia(1);
                                                        listaRespuesta[0] = idTramAvanzar;
                                                        flujo.setListaRespuesta(listaRespuesta);
                                                        condFinalizacion.setFlujoNO(flujo);
                                                        condFinalizacion.setFlujoSI(flujo);
                                                        condFinalizacion.setRespuesta("no");
                                                        idTramite.setCondFinalizacion(condFinalizacion);
                                                        // finalizar trámite 1001 y avanzar al 1002
                                                        if (!finalizarTramite_relanzar(codOrg, registro, expediente, idTramite, condFinalizacion, inf, binding)) {
                                                            continuar = false;
                                                        }
                                                    }
                                                    if (continuar) {
                                                        //Llamada a SW Alta Agencia                                          
                                                        IWACALASResponse.AREACOMUNICACION areacomunicacionResponseA = null;
                                                        areacomunicacionResponseA = llamadaSWAltaAgencia(con, codOrg, tercero, numeroExpediente, registro);
                                                        if (areacomunicacionResponseA != null) {
                                                            String codErrorIWACALAS = areacomunicacionResponseA.getADCAREACONTROL().getADCCODERROR();
                                                            String codRetornoIWACALAS = areacomunicacionResponseA.getADCAREACONTROL().getADCCODRETORNO();
                                                            if (("".equals(codErrorIWACALAS)) && ("0".equals(codRetornoIWACALAS))) {
                                                                codAgencia = areacomunicacionResponseA.getADSDATOSSALIDA().getADSCODCOLOCACION();
                                                            } else {
                                                                continuar = false;
                                                            }
                                                        } else {
                                                            continuar = false;
                                                        }
                                                    }
                                                }
                                                if (continuar) {
                                                    //Grabo el NUMERO DE AGENCIA EN el campo suplementario
                                                    String codCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide54.CAMPO_COD_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
                                                    String valorCampo = codAgencia;
                                                    String nombreTabla = ConfigurationParameter.getParameter(ConstantesMeLanbide54.TABLA_CAMPOS_TEXTO, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
                                                    if (!MeLanbide54DAO.getInstance().existeCampoSuplementarioAACC(con, codOrg, numeroExpediente, codCampo, valorCampo, nombreTabla)) {
                                                        MeLanbide54DAO.getInstance().grabarCampoSuplementarioAACC(con, codOrg, numeroExpediente, codCampo, valorCampo, nombreTabla);
                                                    }
                                                }
                                            }
                                            if (continuar) {
                                                //Buscar en qué trámite está
                                                codtramite = meLanbide54DAO.buscarTramite(con, numeroExpediente);
                                                if ((codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) || (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB, ConstantesMeLanbide54.FICHERO_PROPIEDADES)))) {
                                                    idTram.setCodTramite(codtramite);
                                                    idTram.setOcurrencia(1);
                                                    idTramite.setId(idTram);
                                                    idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                                    idTramite.setUsuarioFin(registro.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   						
                                                    tipoFin = meLanbide54DAO.BuscarTipoFinalizacion(con, codOrg, codProc, codtramite);
                                                    condFinalizacion.setTipoFinalizacion(tipoFin);
                                                    flujo.setTipoApertura(1);   // 1--> Obligatoria
                                                    //Recogemos el Código interno del trámite a iniciar del properties:
                                                    codIntTramAvanzar = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                                    idTramAvanzar.setCodTramite(codIntTramAvanzar);
                                                    idTramAvanzar.setOcurrencia(1);
                                                    listaRespuesta[0] = idTramAvanzar;
                                                    flujo.setListaRespuesta(listaRespuesta);
                                                    condFinalizacion.setFlujoSI(flujo);
                                                    condFinalizacion.setRespuesta("si");
                                                    idTramite.setCondFinalizacion(condFinalizacion);
                                                    //Avanzar al trámite 1004-comunicación número de agencia/alta centro
                                                    if (!finalizarTramite_relanzar(codOrg, registro, expediente, idTramite, condFinalizacion, inf, binding)) {
                                                        log.debug("no finaliza tramite. Reg :" + registro + " Exp.: " + expediente + " Tramite" + idTramite + " Tipo Fin: " + tipoFin + " Cod.FinS: " + condFinalizacion + " - " + inf);
                                                        continuar = false;
                                                    }
                                                }
                                            }
                                            if (continuar) {
                                                //Buscar en qué trámite está
                                                codtramite = meLanbide54DAO.buscarTramite(con, numeroExpediente);
                                                if (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) {
                                                    registroBatch.setEjerRegistro(registro.getResEje());
                                                    registroBatch.setNumRegistro(registro.getResNum());
                                                    registroBatch.setNumExpediente(numeroExpediente);
                                                    registroBatch.setCodTramite(codtramite);
                                                    registroBatch.setRelanzar(0);
                                                    registroBatch.setCodOperacion(OP_INSERTAR_NOTIFICACION);
                                                    registroBatch.setObservaciones("");

                                                    //Llamada a SW Alta centro/s .. Controlar el retorno
//                                                 
                                                    log.error("Llamada a recogeCentros");
                                                    log.error("Nº Expd.: " + numeroExpediente);
                                                    List<CentroVO> centros = meLanbide54DAO.recogeCentros(numeroExpediente, con);
                                                    log.debug("Número de Centros en el Expediente nº " + numeroExpediente + " = " + centros.size());
                                                    int cont = 0;
                                                    for (CentroVO next : centros) {
                                                        centro = centros.get(cont);
                                                        log.error("LLamada a SW altaCentro");
                                                        log.debug("Centro " + centro.getId());
                                                        log.debug("Cod.TH: " + centro.getCodTH());
                                                        log.debug("Cod Municipio: " + centro.getCodMun());

                                                        IWACACTSResponse.AREACOMUNICACION areacomunicacionResponseCt = null;
                                                        areacomunicacionResponseCt = llamadaSWAltaCentro(con, codOrg, centro, numeroExpediente, registro);
                                                        if (areacomunicacionResponseCt != null) {
                                                            String codErrorIWACACTS = areacomunicacionResponseCt.getADCAREACONTROL().getADCCODERROR();
                                                            String codRetornoIWACACTS = areacomunicacionResponseCt.getADCAREACONTROL().getADCCODRETORNO();
                                                            if (("".equals(codErrorIWACACTS)) && ("0".equals(codRetornoIWACACTS))) {
                                                                codCentro = areacomunicacionResponseCt.getADSDATOSSALIDA().getADSNUCENTRO();
                                                                log.debug("Código de centro generado por el SW del SEPE: " + codCentro);
                                                            } else {
                                                                continuar = false;
                                                            }

                                                            // Grabo el numero de centro en la tabla MELANBIDE54_CENTROS
                                                            if (continuar) {
                                                                centro.setCodsepe(codCentro);
                                                                log.debug("Llamo a actualizaCentro()");
                                                                meLanbide54DAO.actualizaCentro(centro, numeroExpediente, con);
                                                            }
                                                        }
                                                        cont++;
                                                    }
                                                    log.debug("Finalizado SW altaCentro");
                                                    //Comunicaciones    
                                                    //Para poder enviar notificacion, es necesario activar la notificación electrónica al tercero asociado al expediente
                                                    try {
                                                        registroBatch.setOperacion("insertar/modificar notificación");
                                                        meLanbide54DAO.permitirNotificacionElectronica(numeroExpediente, con);
                                                        boolean enviada = false;
                                                        NotificacionVO notificacion = new NotificacionVO();
                                                        notificacion.setNumExpediente(numeroExpediente);
                                                        notificacion.setCodigoProcedimiento(codProc);
                                                        notificacion.setEjercicio(Calendar.getInstance().get(Calendar.YEAR));
                                                        notificacion.setCodigoMunicipio(codOrg);
                                                        //Recogemos el Código interno del trámite del properties                                                                                                                                       
                                                        notificacion.setCodigoTramite(codtramite);
                                                        notificacion.setOcurrenciaTramite(1);
                                                        notificacion.setActoNotificado("Notificación Número de Agencia ");      //David
                                                        notificacion.setTextoNotificacion("Texto prueba");  //David      
                                                        //Aquí se debe buscar si existe notificación pendiente (para ese expediente) antes de insertar una nueva.
                                                        //Si no existe se inserta y si existe no se inserta.
                                                        int codigoNotif = meLanbide54DAO.buscarCodigoNotificacion(numeroExpediente, con);
                                                        if (codigoNotif == 1) {  //significa que no hay notificación 
                                                            log.error("no hay notificacion");
                                                            enviada = meLanbide54DAO.insertarNotificacion(notificacion, con);
                                                        } else {
                                                            log.error("sí hay notificacion");
                                                            enviada = meLanbide54DAO.modificarNotificacion(codigoNotif, con);
                                                        }
                                                        int codigoNotificacion = meLanbide54DAO.buscarCodigoNotificacion(numeroExpediente, con);
                                                        notificacion.setCodigoNotificacion(codigoNotificacion);
                                                        int versionTercero = meLanbide54DAO.buscarVersionTercero(registro.getResTer(), con);
                                                        int codigoTercero = registro.getResTer();
                                                        //busca si tiene registro en la tabla AUTORIZADO_NOTIFICACION, para ese tercero, expediente. Si no tiene lo inserta
                                                        boolean autorizado = meLanbide54DAO.buscaAutorizadoNotificacion(notificacion, codigoTercero, versionTercero, con);
                                                        if (!autorizado) {
                                                            meLanbide54DAO.insertaAutorizadoNotificacion(notificacion, codigoTercero, versionTercero, con);
                                                        }
                                                        //grabamos registro en MELANBIDE54_REG_BATCH             
                                                        registroBatch.setResultado("OK");
                                                        MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                                        log.debug("Grabado OK en REG_BATCH");
                                                    } catch (Exception e) {
                                                        registroBatch.setResultado("KO");
                                                        registroBatch.setObservaciones("Excepción al insertar la notificación");
                                                        MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                                        continuar = false;

                                                        ErrorBean err = new ErrorBean();
                                                        err.setIdError("TRAMITACION_AACC_009");
                                                        err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al insertar las notificaciones del expediente " + numeroExpediente);
                                                        err.setSituacion("JobExecute");
                                                        String causa = e.getCause().getMessage();
                                                        String error = e.getMessage();
                                                        grabarError(err, error, causa, numeroExpediente);
                                                    }
                                                }
                                            }

                                            if (continuar) {
                                                //Buscar en qué trámite está
                                                codtramite = meLanbide54DAO.buscarTramite(con, numeroExpediente);
                                                if (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) {
                                                    //Si han ido las comunicaciones avanzar trámite 1004                                      
                                                    //Avanzar al trámite 1005-Comunicación nueva agencia/centro y al 999-Cierre de expediente
                                                    idTram.setCodTramite(codtramite);
                                                    idTram.setOcurrencia(1);
                                                    idTramite.setId(idTram);
                                                    idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                                    idTramite.setUsuarioFin(registro.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   						                                       
                                                    idTramite.setId(idTram);
                                                    IdTramiteVO[] listaRespuesta2 = new IdTramiteVO[2];
                                                    //Recogemos el Código interno del trámite a iniciar 1005 del properties:                                                
                                                    codIntTramAvanzar = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUEVA_AGENCIA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                                    log.error("codigo interno Trámite a avanzar 1005 " + codIntTramAvanzar);
                                                    listaRespuesta2[0] = new IdTramiteVO();
                                                    listaRespuesta2[0].setCodTramite(codIntTramAvanzar);
                                                    listaRespuesta2[0].setOcurrencia(1);
                                                    //Recogemos el Código interno del trámite a iniciar 999 del properties:                                                                                                
                                                    codIntTramAvanzar = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.CIERRE_DE_EXPEDIENTE, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                                    log.error("codigo interno Trámite a avanzar 999 " + codIntTramAvanzar);
                                                    listaRespuesta2[1] = new IdTramiteVO();
                                                    listaRespuesta2[1].setCodTramite(codIntTramAvanzar);
                                                    listaRespuesta2[1].setOcurrencia(1);
                                                    flujo.setListaRespuesta(listaRespuesta2);
                                                    flujo.setTipoApertura(1);   // 1--> Obligatoria                                     
                                                    //condFinalizacion.setTipoFinalizacion("T");   
                                                    tipoFin = meLanbide54DAO.BuscarTipoFinalizacion(con, codOrg, codProc, codtramite);
                                                    condFinalizacion.setTipoFinalizacion(tipoFin);
                                                    condFinalizacion.setFlujoSI(flujo);
                                                    idTramite.setCondFinalizacion(condFinalizacion);

                                                    if (!finalizarTramite_relanzar(codOrg, registro, expediente, idTramite, condFinalizacion, inf, binding)) {
                                                        continuar = false;
                                                        log.debug("no finaliza tramite. Reg :" + registro + " Exp.: " + expediente + " Tramite" + idTramite + " Tipo Fin: " + tipoFin + " Cod.FinS: " + condFinalizacion + " - " + inf);
                                                    }
                                                }
                                            }

                                        }
                                    } else {
                                        log.debug("Error al iniciar el expediente");
                                    }
                                }

                            }
                            //---------BUCLE PARA RELANZAR EL PROCESO------------------------------------------------------------------------------------                                                         

                            for (Iterator<RegistroBatchVO> i = expedientesRelanzar.iterator(); i.hasNext();) {
                                RegistroBatchVO expedientePendiente = i.next();
                                log.debug("idRegBatch: " + expedientePendiente.getId());
                                log.debug("anoRegistro: " + expedientePendiente.getEjerRegistro());
                                log.debug("numRegistro: " + expedientePendiente.getNumRegistro());
                                log.debug("numExpedientePend: " + expedientePendiente.getNumExpediente());
                                log.debug("codtramite: " + expedientePendiente.getCodTramite());
                                idRegBatch = expedientePendiente.getId();
                                anoRegistro = expedientePendiente.getEjerRegistro();
                                numRegistro = expedientePendiente.getNumRegistro();
                                numExpedientePend = expedientePendiente.getNumExpediente();
                                log.debug("anoExp: " + Integer.parseInt(numExpedientePend.substring(0, 4)));
                                log.debug("numExp: " + Integer.parseInt(numExpedientePend.substring(10, 16)));
                                anoExp = Integer.parseInt(numExpedientePend.substring(0, 4));
                                numExp = Integer.parseInt(numExpedientePend.substring(10, 16));
                                codtramite = expedientePendiente.getCodTramite();
                                log.debug("expedientePendiente: " + numExpedientePend);
                                continuar = true;
                                RegistroAACCVO registro = meLanbide54DAO.getRegistroTelematicoAACC(con, anoRegistro, numRegistro);
                                //Se pone el campo 'Relanzar' de la tabla MELANBIDE54_REG_BATCH a 2.
                                if (!meLanbide54DAO.ponerEstadoRelanzado(idRegBatch, con)) {
                                    continuar = false;
                                }
                                //Pregunta si no hay expediente es que no se ha hecho el iniciarexpedienteConOperacion
                                if (continuar) {
                                    if ("".equals(numExpedientePend)) {
                                        expediente = inicioExpedienteAACC_relanzar(codigoUOR, codOrg, registro, codProc, inf, binding);
                                        if (expediente == null) {
                                            continuar = false;
                                        } else {
                                            numExpedientePend = expediente.getIdExpedienteVO().getNumeroExpediente();
                                            tercero = expediente.getInteresados()[0].getTercero();
                                        }
                                    } else {
                                        IdExpedienteVO idexpediente2 = new IdExpedienteVO(anoExp, numExp, numExpedientePend, codProc);
                                        expediente.setIdExpedienteVO(idexpediente2);
                                        tercero = MeLanbide54DAO.getInstance().getTercero(codOrg, anoRegistro, registro, con);
                                    }
                                }
                                log.debug("Cod.provincia: " + tercero.getDomicilio().getCodProvincia());
                                //Pregunta si hay operación pendiente (Ver si hay TAREAS_PENDIENTES_INICIO)
                                if (continuar) {
                                    if (meLanbide54DAO.buscarOperacionesPendientes(con, numExpedientePend, codtramite)) {
                                        // Llama a operación 'generarMisGestInicioOficio'    
                                        registroBatch.setEjerRegistro(anoRegistro);
                                        registroBatch.setNumRegistro(numRegistro);
                                        registroBatch.setNumExpediente(numExpedientePend);
                                        registroBatch.setCodTramite(codtramite);
                                        registroBatch.setOperacion("generarMisGestInicioOficio");
                                        registroBatch.setRelanzar(0);
                                        registroBatch.setCodOperacion(OP_MISGESTIONES_INICIO_OPERACION);
                                        registroBatch.setObservaciones("");

                                        String resultado = "0";
                                        generarMisGestInicioOficio(codOrg, codtramite, 1, numExpedientePend);
                                        //if("0".equals(resultado)){   
                                        if (continuar) {    //quitar esta línea y poner la de arriba
                                            //Borrar el registro de TAREAS_PENDIENTES_INICIO 
                                            //espera de respuesta de Mila
                                            registroBatch.setResultado("OK");
                                            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                        } else {
                                            //grabar error y registro en la tabla nueva y sale de todo--> continuar=NO
                                            registroBatch.setResultado("KO");
                                            registroBatch.setObservaciones("Error al generarMisGestInicioOficio");
                                            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                                            ErrorBean err = new ErrorBean();
                                            err.setIdError("TRAMITACION_AACC_003");
                                            err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al ejecutar la operacion generarMisGestInicioOficio en el expediente " + numeroExpediente);
                                            err.setSituacion("JobExecute");
                                            String causa = "";
                                            String error = "";
                                            grabarError(err, error, causa, numExpedientePend);

                                            continuar = false;
                                        }
                                    }
                                }

                                if (continuar) {
                                    //pregunta si el expediente está asociado al registro (buscar si existe en la tabla E_EXR con expediente y registro)
                                    if (!meLanbide54DAO.buscarRegistroAsociado(con, numExpedientePend, anoRegistro, numRegistro)) {
                                        if (!asociarExpedienteRegistro(codOrg, registro, expediente, inf, binding)) {
                                            continuar = false;
                                        }
                                    }
                                }
                                if (continuar) {
                                    //Buscar en qué trámite está
                                    codtramite = meLanbide54DAO.buscarTramite(con, numExpedientePend);
                                    if ((codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) || (codtramite == Integer.parseInt(ConfigurationParameter.getParameter("GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB", ConstantesMeLanbide54.FICHERO_PROPIEDADES)))) {
                                        //Se busca si existe el XML en el registro
                                        // Devuelve el XML en formato byte[]. Hay que guardar los datos suplementarios de Flexia y cargarExpedienteExtension . 
                                        archivoXML = meLanbide54DAO.BuscarXML(con, registro);

                                        registroBatch.setEjerRegistro(registro.getResEje());
                                        registroBatch.setNumRegistro(registro.getResNum());
                                        registroBatch.setNumExpediente(numExpedientePend);
                                        registroBatch.setCodTramite(codtramite);
                                        registroBatch.setOperacion("buscar XML");
                                        registroBatch.setRelanzar(0);
                                        registroBatch.setCodOperacion(OP_BUSCAR_XML);
                                        registroBatch.setObservaciones("");
                                        if (archivoXML != null) {
                                            registroBatch.setResultado("OK");
                                            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                        } else {
                                            registroBatch.setResultado("KO");
                                            registroBatch.setObservaciones("No existe xml (FLX_DATOS_INTEGRACION_SOLICITUD) en el registro (tabla R_RED)");
                                            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                                            ErrorBean err = new ErrorBean();
                                            err.setIdError("TRAMITACION_AACC_005");
                                            err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. No existe xml (FLX_DATOS_INTEGRACION_SOLICITUD) en el registro: " + anoRegistro + "/" + numRegistro);
                                            err.setSituacion("JobExecute");
                                            String causa = "";
                                            String error = "";
                                            grabarError(err, error, causa, numeroExpediente);

                                            continuar = false;
                                        }
                                    }
                                }

                                if (continuar) {
                                    //Buscar en qué trámite está
                                    codtramite = meLanbide54DAO.buscarTramite(con, numExpedientePend);
                                    if ((codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) || (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB, ConstantesMeLanbide54.FICHERO_PROPIEDADES)))) {
                                        //pregunta si ha grabado los datos suplementarios de flexia                                 
                                        if (!grabadoDatosSuplementariosXML(archivoXML, codOrg, codProc, con, numExpedientePend, registro)) {
                                            continuar = false;
                                        }
                                    }
                                }

                                if (continuar) {
                                    //Buscar en qué trámite está
                                    codtramite = meLanbide54DAO.buscarTramite(con, numExpedientePend);
                                    if ((codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) || (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB, ConstantesMeLanbide54.FICHERO_PROPIEDADES)))) {
                                        //Pregunta si se han cargado los centros de trabajo en el expediente(existe registro en MELANBIDE54_CENTROS para el expediente)
                                        if (!meLanbide54DAO.existenDatosExtension(con, numExpedientePend)) {
                                            registroBatch.setEjerRegistro(registro.getResEje());
                                            registroBatch.setNumRegistro(registro.getResNum());
                                            registroBatch.setNumExpediente(numExpedientePend);
                                            registroBatch.setCodTramite(codtramite);
                                            registroBatch.setOperacion("cargarExpedienteExtension");
                                            registroBatch.setRelanzar(0);
                                            registroBatch.setCodOperacion(OP_GRABAR_EXPEDIENTE_EXTENSIÓN);
                                            try {
                                                String xmlString = new String(archivoXML, "ISO-8859-1");
                                                MELANBIDE54.getInstance().cargarExpedienteExtension(codOrg, numeroExpediente, xmlString);
                                                log.debug("Después de cargarExpedienteExtension ");
                                                //Comprobar que ha cargado los datos del centro y grabar en la tabla 
                                                if (meLanbide54DAO.existenDatosExtension(con, numExpedientePend)) {
                                                    registroBatch.setResultado("OK");
                                                    registroBatch.setObservaciones("");
                                                } else {
                                                    registroBatch.setResultado("KO");
                                                    registroBatch.setObservaciones("No existe en la tabla MELANBIDE54_CENTROS");
                                                    continuar = false;
                                                }
                                                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                                            } catch (Exception e) {
                                                registroBatch.setResultado("KO");
                                                registroBatch.setObservaciones("Excepción al cargarExpedienteExtension");
                                                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                                continuar = false;
                                                //  Lo comento porque en caso de excepción ya se grabaría en el MELANBIDE42 en cargarExpedienteExtension
                                                //                                        ErrorBean err = new ErrorBean();
                                                //                                        err.setIdError("TRAMITACION_AACC_007");
                                                //                                        err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al ejecutar la operacion cargarExpedienteExtension en el expediente "+numeroExpediente);
                                                //                                        err.setSituacion("JobExecute");              
                                                //                                        String causa = e.getCause().getMessage();
                                                //                                        String error = e.getMessage();
                                                //                                        grabarError(err, error, causa, numExpedientePend);                                                                                                                         
                                            }
                                        }
                                    }
                                }

                                idTramite = new TramiteVO();
                                idTram = new IdTramiteVO();
                                idTramAvanzar = new IdTramiteVO();
                                condFinalizacion = new CondicionFinalizacionVO();
                                FlujoFinalizacionVO flujo = new FlujoFinalizacionVO();
                                IdTramiteVO[] listaRespuesta = new IdTramiteVO[1];
                                String tipoFin = "";

                                if (continuar) {
                                    //Llamada al SW Consulta Agencia  --> Si me devuelve IAG0018, es que la agencia no existe
                                    existeAgencia = false;
//-----------------------------------              
                                    IWACCIFSResponse.AREACOMUNICACION areacomunicacionResponseC = null;
                                    cif = tercero.getDoc();
                                    areacomunicacionResponseC = llamadaSWConsultaAgencia(con, cif, numExpedientePend, registro);
                                    if (areacomunicacionResponseC != null) {
                                        //Si no existe la Agencia -->   <ADC-COD-ERROR>IAG0018</ADC-COD-ERROR>
                                        //Si existe la Agencia -->      <ADC-COD-ERROR>IAG0017</ADC-COD-ERROR>       
                                        String codErrorIWACCIFS = areacomunicacionResponseC.getADCAREACONTROL().getADCCODERROR();
                                        String existe_agenciaSW = ConfigurationParameter.getParameter(ConstantesMeLanbide54.EXISTE_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
                                        String no_existe_agenciaSW = ConfigurationParameter.getParameter(ConstantesMeLanbide54.NO_EXISTE_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES);

                                        if (existe_agenciaSW.equals(codErrorIWACCIFS)) {
                                            existeAgencia = true;
                                            log.debug("Existe Agencia");
                                            log.debug("ADS-NUM-REG: " + areacomunicacionResponseC.getADSDATOSSALIDA().getADSNUMREG());
                                            List<DATOS> datosSalidaIWACCIFS = areacomunicacionResponseC.getADSDATOSSALIDA().getDATOSs().getDATOS();

                                            for (Iterator<DATOS> i2 = datosSalidaIWACCIFS.iterator(); i2.hasNext();) {
                                                DATOS datoSalidaIWACCIFS = i2.next();
                                                log.debug("ADS-NIFE: " + datoSalidaIWACCIFS.getADSNIFE());
                                                log.debug("ADS-CO-AGENC: " + datoSalidaIWACCIFS.getADSCOAGENC());
                                                log.debug("ADS-FX-RESOL: " + datoSalidaIWACCIFS.getADSFXRESOL());
                                                log.debug("ADS-NOMAGE: " + datoSalidaIWACCIFS.getADSNOMAGE());
                                                log.debug("ADS-FX-BAJA: " + datoSalidaIWACCIFS.getADSFXBAJA());
                                                log.debug("ADS-MOTBAJA: " + datoSalidaIWACCIFS.getADSMOTBAJA());
                                                if ((!"".equals(datoSalidaIWACCIFS.getADSCOAGENC())) && ("".equals(datoSalidaIWACCIFS.getADSFXBAJA()))) {
                                                    log.debug("Agencia a guardar en datos suplementarios: " + datoSalidaIWACCIFS.getADSCOAGENC());
                                                    codAgencia = datoSalidaIWACCIFS.getADSCOAGENC();
                                                }
                                            }
                                        } else {
                                            if (no_existe_agenciaSW.equals(codErrorIWACCIFS)) {
                                                existeAgencia = false;
                                                log.debug("NO existe Agencia con el CIF: " + areacomunicacionResponseC.getADEDATOSENTRADA().getADENIFE());
                                            } else {
                                                continuar = false;
                                                log.debug("Error en el SW de Consulta Agencia");
                                            }
                                        }
                                    } else {
                                        continuar = false;
                                    }
//-----------------------------------                                
                                    if (!existeAgencia && continuar) {
                                        //Buscar en qué trámite está
                                        codtramite = meLanbide54DAO.buscarTramite(con, numExpedientePend);
                                        if (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) {
                                            idTram.setCodTramite(codtramite);
                                            idTram.setOcurrencia(1);
                                            idTramite.setId(idTram);
                                            idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                            idTramite.setUsuarioFin(registro.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   						
                                            tipoFin = meLanbide54DAO.BuscarTipoFinalizacion(con, codOrg, codProc, codtramite);
                                            condFinalizacion.setTipoFinalizacion(tipoFin);
                                            flujo.setTipoApertura(1);   // 1--> Obligatoria
                                            //Recogemos el Código interno del trámite a iniciar del properties:
                                            codIntTramAvanzar = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                            idTramAvanzar.setCodTramite(codIntTramAvanzar);
                                            idTramAvanzar.setOcurrencia(1);
                                            listaRespuesta[0] = idTramAvanzar;
                                            flujo.setListaRespuesta(listaRespuesta);
                                            condFinalizacion.setFlujoNO(flujo);
                                            condFinalizacion.setFlujoSI(flujo);
                                            condFinalizacion.setRespuesta("no");
                                            idTramite.setCondFinalizacion(condFinalizacion);
                                            // finalizar trámite 1001 y avanzar al 1002
                                            if (!finalizarTramite_relanzar(codOrg, registro, expediente, idTramite, condFinalizacion, inf, binding)) {
                                                continuar = false;
                                            }
                                        }

                                    }
                                }
                                if (!existeAgencia && continuar) {
                                    //Llamada a SW Alta Agencia                                          
                                    IWACALASResponse.AREACOMUNICACION areacomunicacionResponseA = null;
                                    areacomunicacionResponseA = llamadaSWAltaAgencia(con, codOrg, tercero, numExpedientePend, registro);
                                    if (areacomunicacionResponseA != null) {
                                        String codErrorIWACALAS = areacomunicacionResponseA.getADCAREACONTROL().getADCCODERROR();
                                        String codRetornoIWACALAS = areacomunicacionResponseA.getADCAREACONTROL().getADCCODRETORNO();
                                        if (("".equals(codErrorIWACALAS)) && ("0".equals(codRetornoIWACALAS))) {
                                            codAgencia = areacomunicacionResponseA.getADSDATOSSALIDA().getADSCODCOLOCACION();
                                        } else {
                                            continuar = false;
                                        }
                                    } else {
                                        continuar = false;
                                    }

                                    if (continuar) {
                                        //Grabo el NUMERO DE AGENCIA EN el campo suplementario
                                        String codCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide54.CAMPO_COD_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
                                        String valorCampo = codAgencia;
                                        String nombreTabla = ConfigurationParameter.getParameter(ConstantesMeLanbide54.TABLA_CAMPOS_TEXTO, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
                                        if (!MeLanbide54DAO.getInstance().existeCampoSuplementarioAACC(con, codOrg, numExpedientePend, codCampo, valorCampo, nombreTabla)) {
                                            MeLanbide54DAO.getInstance().grabarCampoSuplementarioAACC(con, codOrg, numExpedientePend, codCampo, valorCampo, nombreTabla);
                                        }
                                    }
                                }
                                if (continuar) {
                                    //Buscar en qué trámite está
                                    codtramite = meLanbide54DAO.buscarTramite(con, numExpedientePend);
                                    if ((codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) || (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB, ConstantesMeLanbide54.FICHERO_PROPIEDADES)))) {
                                        idTram.setCodTramite(codtramite);
                                        idTram.setOcurrencia(1);
                                        idTramite.setId(idTram);
                                        idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                        idTramite.setUsuarioFin(registro.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   						
                                        tipoFin = meLanbide54DAO.BuscarTipoFinalizacion(con, codOrg, codProc, codtramite);
                                        condFinalizacion.setTipoFinalizacion(tipoFin);
                                        flujo.setTipoApertura(1);   // 1--> Obligatoria
                                        //Recogemos el Código interno del trámite a iniciar del properties:
                                        codIntTramAvanzar = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                        idTramAvanzar.setCodTramite(codIntTramAvanzar);
                                        idTramAvanzar.setOcurrencia(1);
                                        listaRespuesta[0] = idTramAvanzar;
                                        flujo.setListaRespuesta(listaRespuesta);
                                        condFinalizacion.setFlujoSI(flujo);
                                        condFinalizacion.setRespuesta("si");
                                        idTramite.setCondFinalizacion(condFinalizacion);
                                        //Avanzar al trámite 1004-comunicación número de agencia/alta centro
                                        if (!finalizarTramite_relanzar(codOrg, registro, expediente, idTramite, condFinalizacion, inf, binding)) {
                                            continuar = false;
                                            log.debug("no finaliza tramite. Reg :" + registro + " Exp.: " + expediente + " Tramite" + idTramite + " Tipo Fin: " + tipoFin + " Cod.FinS: " + condFinalizacion + " - " + inf);
                                        }
                                    }
                                }
                                if (continuar) {
                                    //Buscar en qué trámite está
                                    codtramite = meLanbide54DAO.buscarTramite(con, numExpedientePend);
                                    if (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) {
                                        registroBatch.setEjerRegistro(registro.getResEje());
                                        registroBatch.setNumRegistro(registro.getResNum());
                                        registroBatch.setNumExpediente(numExpedientePend);
                                        registroBatch.setCodTramite(codtramite);
                                        registroBatch.setRelanzar(0);
                                        registroBatch.setCodOperacion(OP_INSERTAR_NOTIFICACION);
                                        registroBatch.setObservaciones("");

                                        //Llamada a SW Alta centro/s .. Controlar el retorno
                                        log.error("Llamada a recogeCentros");
                                        log.error("Nº Expd.: " + numeroExpediente);
                                        List<CentroVO> centros = meLanbide54DAO.recogeCentros(numeroExpediente, con);
                                        log.debug("Número de Centros en el Expediente nº " + numeroExpediente + " = " + centros.size());
                                        int cont = 0;
                                        for (CentroVO next : centros) {
                                            centro = centros.get(cont);
                                            log.error("LLamada a SW altaCentro");
                                            log.debug("Centro " + centro.getId());
                                            log.debug("Cod.TH: " + centro.getCodTH());
                                            log.debug("Cod Municipio: " + centro.getCodMun());

                                            IWACACTSResponse.AREACOMUNICACION areacomunicacionResponseCt = null;
                                            areacomunicacionResponseCt = llamadaSWAltaCentro(con, codOrg, centro, numeroExpediente, registro);
                                            if (areacomunicacionResponseCt != null) {
                                                String codErrorIWACACTS = areacomunicacionResponseCt.getADCAREACONTROL().getADCCODERROR();
                                                String codRetornoIWACACTS = areacomunicacionResponseCt.getADCAREACONTROL().getADCCODRETORNO();
                                                if (("".equals(codErrorIWACACTS)) && ("0".equals(codRetornoIWACACTS))) {
                                                    codCentro = areacomunicacionResponseCt.getADSDATOSSALIDA().getADSNUCENTRO();
                                                    log.debug("Código de centro generado por el SW del SEPE: " + codCentro);
                                                } else {
                                                    continuar = false;
                                                }

                                                // Grabo el numero de centro en la tabla MELANBIDE54_CENTROS
                                                if (continuar) {
                                                    centro.setCodsepe(codCentro);
                                                    log.debug("Llamo a actualizaCentro()");
                                                    meLanbide54DAO.actualizaCentro(centro, numeroExpediente, con);
                                                }
                                            }
                                            cont++;
                                        }
                                        log.debug("Finalizado SW altaCentro (2)");
                                        //...........
                                        //Comunicaciones    
                                        //Para poder enviar notificacion, es necesario activar la notificación electrónica al tercero asociado al expediente
                                        try {
                                            registroBatch.setOperacion("insertar/modificar notificación");
                                            meLanbide54DAO.permitirNotificacionElectronica(numExpedientePend, con);
                                            boolean enviada = false;
                                            NotificacionVO notificacion = new NotificacionVO();
                                            notificacion.setNumExpediente(numExpedientePend);
                                            notificacion.setCodigoProcedimiento(codProc);
                                            notificacion.setEjercicio(Calendar.getInstance().get(Calendar.YEAR));
                                            notificacion.setCodigoMunicipio(codOrg);
                                            //Recogemos el Código interno del trámite del properties                                                                                                                                       
                                            notificacion.setCodigoTramite(codtramite);
                                            notificacion.setOcurrenciaTramite(1);
                                            notificacion.setActoNotificado("Número de agencia");      //David
                                            notificacion.setTextoNotificacion("Texto prueba");  //David      
                                            //Aquí se debe buscar si existe notificación pendiente (para ese expediente) antes de insertar una nueva.
                                            //Si no existe se inserta y si existe no se inserta.
                                            int codigoNotif = meLanbide54DAO.buscarCodigoNotificacion(numExpedientePend, con);
                                            log.debug("Llega: " + codigoNotif);
                                            if (codigoNotif == 1) {  //significa que no hay notificación 
                                                log.error("no hay notificacion");
                                                enviada = meLanbide54DAO.insertarNotificacion(notificacion, con);
                                            } else {
                                                log.error("sí hay notificacion");
                                                enviada = meLanbide54DAO.modificarNotificacion(codigoNotif, con);
                                            }
                                            int codigoNotificacion = meLanbide54DAO.buscarCodigoNotificacion(numExpedientePend, con);
                                            notificacion.setCodigoNotificacion(codigoNotificacion);
                                            int versionTercero = meLanbide54DAO.buscarVersionTercero(registro.getResTer(), con);
                                            int codigoTercero = registro.getResTer();
                                            //busca si tiene registro en la tabla AUTORIZADO_NOTIFICACION, para ese tercero, expediente. Si no tiene lo inserta
                                            boolean autorizado = meLanbide54DAO.buscaAutorizadoNotificacion(notificacion, codigoTercero, versionTercero, con);
                                            if (!autorizado) {
                                                meLanbide54DAO.insertaAutorizadoNotificacion(notificacion, codigoTercero, versionTercero, con);
                                            }
                                            //grabamos registro en MELANBIDE54_REG_BATCH             
                                            registroBatch.setResultado("OK");
                                            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                            log.debug("Grabado OK en REG_BATCH");
                                        } catch (Exception e) {
                                            registroBatch.setResultado("KO");
                                            registroBatch.setObservaciones("Excepción al insertar la notificación");
                                            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                            continuar = false;

                                            ErrorBean err = new ErrorBean();
                                            err.setIdError("TRAMITACION_AACC_009");
                                            err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al insertar las notificaciones del expediente " + numeroExpediente);
                                            err.setSituacion("JobExecute");
                                            String causa = e.getCause().getMessage();
                                            String error = e.getMessage();
                                            grabarError(err, error, causa, numExpedientePend);
                                        }
                                    }
                                }
                                if (continuar) {
                                    //Buscar en qué trámite está
                                    codtramite = meLanbide54DAO.buscarTramite(con, numExpedientePend);
                                    if (codtramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) {
                                        //Si han ido las comunicaciones avanzar trámite 1004                                      
                                        //Avanzar al trámite 1005-Comunicación nueva agencia/centro y al 999-Cierre de expediente
                                        idTram.setCodTramite(codtramite);
                                        idTram.setOcurrencia(1);
                                        idTramite.setId(idTram);
                                        idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                        idTramite.setUsuarioFin(registro.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   						                                       
                                        idTramite.setId(idTram);
                                        IdTramiteVO[] listaRespuesta2 = new IdTramiteVO[2];
                                        //Recogemos el Código interno del trámite a iniciar 1005 del properties:                                                
                                        codIntTramAvanzar = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUEVA_AGENCIA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                        listaRespuesta2[0] = new IdTramiteVO();
                                        listaRespuesta2[0].setCodTramite(codIntTramAvanzar);
                                        listaRespuesta2[0].setOcurrencia(1);
                                        //Recogemos el Código interno del trámite a iniciar 999 del properties:                                                                                                
                                        codIntTramAvanzar = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.CIERRE_DE_EXPEDIENTE, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                        listaRespuesta2[1] = new IdTramiteVO();
                                        listaRespuesta2[1].setCodTramite(codIntTramAvanzar);
                                        listaRespuesta2[1].setOcurrencia(1);
                                        flujo.setListaRespuesta(listaRespuesta2);
                                        flujo.setTipoApertura(1);   // 1--> Obligatoria                                     
                                        //condFinalizacion.setTipoFinalizacion("T");   
                                        tipoFin = meLanbide54DAO.BuscarTipoFinalizacion(con, codOrg, codProc, codtramite);
                                        condFinalizacion.setTipoFinalizacion(tipoFin);
                                        condFinalizacion.setFlujoSI(flujo);
                                        idTramite.setCondFinalizacion(condFinalizacion);
                                        if (!finalizarTramite_relanzar(codOrg, registro, expediente, idTramite, condFinalizacion, inf, binding)) {
                                            continuar = false;
                                            log.debug("no finaliza tramite. Reg :" + registro + " Exp.: " + expediente + " Tramite" + idTramite + " Tipo Fin: " + tipoFin + " Cod.FinS: " + condFinalizacion + " - " + inf);
                                        }
                                    }
                                }

                                if (continuar) {
                                    if (meLanbide54DAO.existeCodigoPlatea(con, numExpedientePend)) {
                                        //Llamada al Sistema de Gestión de Agencias de Colocación (SGAC)
                                        //.....
                                        //buscar en qué trámite está  (1005 o 999). Puede que tenga Cod.Platea, haya avanzado el 1005 correctamente
                                        // pero haya fallado en el avanzar el 999. En ese caso no hay que finalizar el 1005
                                        codtramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUEVA_AGENCIA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                        if (meLanbide54DAO.existeTramitePendiente(con, numExpedientePend, codtramite)) {
                                            idTram.setCodTramite(codtramite);
                                            idTram.setOcurrencia(1);
                                            idTramite.setId(idTram);
                                            idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                            idTramite.setUsuarioFin(registro.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   						
                                            CondicionFinalizacionVO condFinalizacionS = new CondicionFinalizacionVO();
                                            //condFinalizacionS.setTipoFinalizacion("S");                                        
                                            tipoFin = meLanbide54DAO.BuscarTipoFinalizacion(con, codOrg, codProc, codtramite);
                                            condFinalizacionS.setTipoFinalizacion(tipoFin);
                                            idTramite.setCondFinalizacion(condFinalizacionS);
                                            //Finalizar trámite 1005-Comunicación nueva agencia/centro
                                            if (!finalizarTramite_relanzar(codOrg, registro, expediente, idTramite, condFinalizacionS, inf, binding)) {
                                                continuar = false;
                                                log.debug("no finaliza tramite. Reg :" + registro + " Exp.: " + expediente + " Tramite" + idTramite + " Tipo Fin: " + tipoFin + " Cod.FinS: " + condFinalizacionS + " - " + inf);
                                            }
                                        }
                                        if (continuar) {
                                            codtramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.CIERRE_DE_EXPEDIENTE, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
                                            if (meLanbide54DAO.existeTramitePendiente(con, numExpedientePend, codtramite)) {
                                                idTram.setCodTramite(codtramite);
                                                idTram.setOcurrencia(1);
                                                idTramite.setId(idTram);
                                                idTramite.setUtr(codigoUOR); //Código interno de la unidad orgánica de Flexia que procederá a finalizar el trámite deseado.                                  
                                                idTramite.setUsuarioFin(registro.getResUsu()); //Código interno del usuario que figurará como encargadao de la finalización del trámite.                                   						
                                                CondicionFinalizacionVO condFinalizacionF = new CondicionFinalizacionVO();
                                                //condFinalizacionS.setTipoFinalizacion("T");                                        
                                                tipoFin = meLanbide54DAO.BuscarTipoFinalizacion(con, codOrg, codProc, codtramite);
                                                condFinalizacionF.setTipoFinalizacion(tipoFin);
                                                idTramite.setCondFinalizacion(condFinalizacionF);
                                                //Finalizar trámite 999-Cierre de expediente 
                                                if (!finalizarTramite_relanzar(codOrg, registro, expediente, idTramite, condFinalizacionF, inf, binding)) {
                                                    continuar = false;
                                                    log.debug("no finaliza tramite. Reg :" + registro + " Exp.: " + expediente + " Tramite" + idTramite + " Tipo Fin: " + tipoFin + " Cod.FinS: " + condFinalizacionF + " - " + inf);
                                                }
                                            }
                                        }

                                    } else {
                                        log.debug("No existe Cod.Platea");
                                        registroBatch.setEjerRegistro(registro.getResEje());
                                        registroBatch.setNumRegistro(registro.getResNum());
                                        registroBatch.setNumExpediente(numExpedientePend);
                                        registroBatch.setCodTramite(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUEVA_AGENCIA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES)));
                                        registroBatch.setOperacion("finalizaTramite");
                                        registroBatch.setCodOperacion(OP_AVANZAR_TRAMITE);
                                        registroBatch.setObservaciones("");

                                        int intentos = meLanbide54DAO.buscarNumeroIntentos(con, numExpedientePend);
                                        log.debug("intentos: " + intentos);
                                        if (intentos < 3) {
                                            //Graba con relanzar = 1 y OK
                                            registroBatch.setRelanzar(1);
                                            registroBatch.setResultado("OK");
                                            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                        } else {
                                            if (intentos == 3) {
                                                log.debug("Se han hecho 3 reintentos de envío comunicación ");
                                                //Graba con relanzar = 0 y KO
                                                registroBatch.setRelanzar(0);
                                                registroBatch.setResultado("KO");
                                                registroBatch.setObservaciones("Se han hecho 3 reintentos de envío comunicación y no se ha generado código Platea");
                                                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                                                //Graba error
                                                ErrorBean err = new ErrorBean();
                                                err.setIdError("TRAMITACION_AACC_010");
                                                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Se han hecho 3 reintentos de envío comunicación y no se ha generado código Platea");
                                                err.setSituacion("JobExecute");
                                                String causa = "";
                                                String error = "";
                                                grabarError(err, error, causa, numExpedientePend);
                                            }
                                        }
                                    }
                                }
                            }
//---------------------------------------------------------------------------------------------

                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }

                            try {
                                if (con != null && !con.isClosed()) {
                                    con.close();
                                }
                                log.debug("Conexión cerrada en el OAD");
                            } catch (SQLException sqle) {
                                log.error("*** ConexionBD: " + sqle.toString());
                                throw new BDException(999, "Error, no se pudo cerrar la conexion", sqle.toString());
                            }
                        }
                    } catch (Exception e) {
                        log.error("Error en el job de alta automática de AACC: ", e);
                        e.printStackTrace();

                        try {
                        } catch (Exception i) {
                            log.error("Error en la función actualizarError: " + i.getMessage());
                        }

                        try {
                            throw e;
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(TramitacionAutomaticaAgenciasAACCJob.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                java.util.logging.Logger.getLogger(TramitacionAutomaticaAgenciasAACCJob.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }//para local quitar
            }
        } catch (Exception ex) {
            log.error("Error: " + ex);
        }
    }
    protected static Config conf = ConfigServiceHelper.getConfig("notificaciones");
    private Logger log = LogManager.getLogger(TramitacionAutomaticaAgenciasAACCJob.class);
    private String codOrganizacion;

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        if (log.isDebugEnabled()) {
            log.error("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;

        if (log.isDebugEnabled()) {
            log.error("getJndi =========> ");
            log.error("parametro codOrganizacion: " + codOrganizacion);
            log.error("gestor: " + gestor);
            log.error("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.error("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexiï¿½n al esquema genï¿½rico
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
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
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
                log.error("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection

    public ExpedienteVO inicioExpedienteAACC(int codigoUOR, int codOrganizacion, RegistroAACCVO registro, String codProc, InfoConexionVO inf, WSTramitacionBindingStub binding) {

        RespuestasTramitacionVO value = null;
        boolean continuar = false;
        log.debug("begin inicioExpedienteAACC()");
        log.error("begin inicioExpedienteAACC()");
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();

            int tramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
            ExpedienteVO exp = new ExpedienteVO();
            IdExpedienteVO idexpediente = new IdExpedienteVO();
            idexpediente.setEjercicio(Calendar.getInstance().get(Calendar.YEAR));//año actual
            int anioActual = idexpediente.getEjercicio();
            idexpediente.setProcedimiento(codProc);
            int usuarioRegistro = registro.getResUsu();
            exp.setIdExpedienteVO(idexpediente);
            exp.setUor(codigoUOR);
            exp.setUorTramiteInicio(codigoUOR);
            exp.setUsuario(usuarioRegistro);
            exp.setAsunto(registro.getResAsu());
            log.error("Asunto registro: " + exp.getAsunto());

            InteresadoExpedienteVO interesado = new InteresadoExpedienteVO();
            interesado = MeLanbide54Manager.getInstance().getDatosInteresado(codOrganizacion, anioActual, registro, con);
            List<InteresadoExpedienteVO> interesados = new ArrayList<InteresadoExpedienteVO>();
            interesados.add(interesado);
            exp.setInteresados(interesados.toArray(new InteresadoExpedienteVO[]{}));

            log.error("------------Datos Interesado----------");
            log.error("Rol: " + interesado.getRol());
            log.error("-----------------Datos de remitente/Datos del Tercero---------------");
            log.error("Apellido 1 remitente" + interesado.getTercero().getAp1());
            log.error("Apellido 2 remitente" + interesado.getTercero().getAp2());
            log.error("Documento:" + interesado.getTercero().getDoc());
            log.error("Email:" + interesado.getTercero().getEmail());
            log.error("Nombre:" + interesado.getTercero().getNombre());
            log.error("Telefono:" + interesado.getTercero().getTelefono());
            log.error("TipoDoc:" + interesado.getTercero().getTipoDoc());
            log.error("----------------------fin Datos de remitente/Datos del Tercero-----------");

            log.error("------------Datos Domicilio----------");
            log.error("Domicilio Bloque: " + interesado.getTercero().getDomicilio().getBloque());
            log.error("Domicilio COdMunicipio: " + interesado.getTercero().getDomicilio().getCodMunicipio());
            log.error("Domicilio CodPais: " + interesado.getTercero().getDomicilio().getCodPais());
            log.error("Domicilio Cpostal: " + interesado.getTercero().getDomicilio().getCodPostal());
            log.error("Domicilio CodProvincia: " + interesado.getTercero().getDomicilio().getCodProvincia());
            log.error("Domicilio Emplazamiento: " + interesado.getTercero().getDomicilio().getEmplazamiento());
            log.error("Domicilio Esprincipal : " + interesado.getTercero().getDomicilio().isEsPrincipal());
            log.error("Domicilio Escalera: " + interesado.getTercero().getDomicilio().getEscalera());
            log.error("Domicilio Nombre: " + interesado.getTercero().getDomicilio().getNombreVia());
            log.error("Domicilio Planta: " + interesado.getTercero().getDomicilio().getPlanta());
            log.error("Domicilio Portal: " + interesado.getTercero().getDomicilio().getPortal());
            log.error("Domicilio PrimerNumero: " + interesado.getTercero().getDomicilio().getPrimerNumero());
            log.error("Domicilio PrimeraLetra: " + interesado.getTercero().getDomicilio().getPrimeraLetra());
            log.error("Domicilio Puerta: " + interesado.getTercero().getDomicilio().getPuerta());
            log.error("Domicilio TipoVia: " + interesado.getTercero().getDomicilio().getTipoVia());
            log.error("Domicilio UltimaLetra: " + interesado.getTercero().getDomicilio().getUltimaLetra());
            log.error("Domicilio UltimoNumero: " + interesado.getTercero().getDomicilio().getUltimoNumero());
            log.error("------------FIN Datos Domicilio----------");

            log.error("------------datos que enviamos al expediente ----------");

            log.error("------------UOR----------");
            log.error("Uor: " + exp.getUor());
            log.error("uorTramiteInicio: " + exp.getUorTramiteInicio());
            log.error("usuario: " + exp.getUsuario());
            log.error("Asunto entrada: " + exp.getAsunto());
            log.error("------------FIN DATOS UOR----------");
            ///Expediente 
            log.error("------------Datos Expediente----------");
            //IdExpedientesVO            
            log.error("------------ID EXPEDIENTE1----------");
            log.error("Ejercicio " + idexpediente.getEjercicio());
            log.error("PROCEDIMIENTO " + idexpediente.getProcedimiento());
            log.error("------------FIN ID EXPEDIENTE1----------");

            log.error("------------Llamamos a ws tramitacion iniciarExpedienteConOperacion() ----------");
            RegistroBatchVO registroBatch = new RegistroBatchVO();
            String numExpediente = "";
            try {
                value = binding.iniciarExpedienteConOperacion(exp, inf);
                log.error("------------Respuesta de iniciarExpedienteConOperacion----------");
                log.error("Documento: " + value.getDocumento());
                log.error("Expediente: " + value.getExpediente());
                log.error("Ejercicio: " + value.getIdExpediente().getEjercicio());
                log.error("Número: " + value.getIdExpediente().getNumero());
                log.error("Numero_Expediente: " + value.getIdExpediente().getNumeroExpediente());
                log.error("Procedimiento: " + value.getIdExpediente().getProcedimiento());
                //casca porque getExpediente() == null
                if (value.getExpediente() != null) {
                    numExpediente = value.getIdExpediente().getNumeroExpediente();
                    log.error("Asunto salida: " + value.getExpediente().getAsunto());
                    registroBatch.setResultado("OK");
                    registroBatch.setObservaciones("");
                    registroBatch.setEjerRegistro(registro.getResEje());
                    registroBatch.setNumRegistro(registro.getResNum());
                    registroBatch.setNumExpediente(numExpediente);
                    registroBatch.setCodTramite(tramite);
                    registroBatch.setOperacion("iniciarExpedienteConOperacion");
                    registroBatch.setRelanzar(0);
                    registroBatch.setCodOperacion(OP_INICIO_EXPEDIENTE);
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                }
                //log.error("Domicilio Nombre: " + value.getExpediente().getInteresados()[0].getTercero().getDomicilio().getNombreVia());
                // ojo me devuelve NULL
                log.error("------------Fin Respuesta de iniciarExpedienteConOperacion----------");
                log.error("idTramite: " + value.getIdtramite());
                log.error("Tramite: " + value.getTramite());
                log.error("Status: " + value.getStatus());
                log.error("Error: " + value.getError());
            } catch (Exception e) {
                registroBatch.setResultado("KO");
                registroBatch.setObservaciones("Excepción en iniciarExpedienteConOperacion ");
                registroBatch.setEjerRegistro(registro.getResEje());
                registroBatch.setNumRegistro(registro.getResNum());
                registroBatch.setNumExpediente("");
                registroBatch.setCodTramite(tramite);
                registroBatch.setOperacion("iniciarExpedienteConOperacion");
                registroBatch.setRelanzar(0);
                registroBatch.setCodOperacion(OP_INICIO_EXPEDIENTE);
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AACC_001");
                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW iniciarExpedienteConOperacion ");
                err.setSituacion("JobExecute");
                String causa = e.getCause().getMessage();
                String error = e.getMessage();

                numExpediente = "";
                grabarError(err, error, causa, numExpediente);
                return null;
            }
            log.error("------------Fin Llamada a ws tramitacion iniciarExpedienteConOperacion() ----------");

            if (value.getStatus() == 0) {
                //Comprobar la tabla TAREAS_PENDIENTES_INICIO. Si tiene registros para el expediente y trámite...
                //...es que ha fallado la operación 'generarMisGestInicioOficio'   
                if (MeLanbide54DAO.getInstance().buscarOperacionesPendientes(con, numExpediente, tramite)) {
                    registroBatch.setResultado("KO");
                    registroBatch.setObservaciones("Existe en TAREAS_PENDIENTES_INICIO");
                } else {
                    registroBatch.setResultado("OK");
                    registroBatch.setObservaciones("");
                }
                registroBatch.setEjerRegistro(registro.getResEje());
                registroBatch.setNumRegistro(registro.getResNum());
                registroBatch.setNumExpediente(numExpediente);
                registroBatch.setCodTramite(tramite);
                registroBatch.setOperacion("generarMisGestInicioOficio");
                registroBatch.setRelanzar(0);
                registroBatch.setCodOperacion(OP_MISGESTIONES_INICIO_EXPEDIENTE);
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                //.............TRAMITACION_AACC_002.............
                //------------------------------------------     
                ExpedienteVO expediente = new ExpedienteVO();
                IdExpedienteVO idexpediente2 = new IdExpedienteVO(value.getIdExpediente().getEjercicio(), value.getIdExpediente().getNumero(), value.getIdExpediente().getNumeroExpediente(), value.getIdExpediente().getProcedimiento());
                expediente.setIdExpedienteVO(idexpediente2);

                int ano = value.getIdExpediente().getEjercicio();
                String[] numeroExpediente = numExpediente.split(ConstantesMeLanbide54.BARRA_SEPARADORA);
                //Una vez generado el nuevo expediente necesitamos asociar el expediente al registro, 'asociarExpRegistro':
                RespuestasTramitacionVO value2 = null;

                RegistroAsociadoVO registroAsociado = new RegistroAsociadoVO();
                //obligatorios
                registroAsociado.setCodDepartamento(registro.getResDep());
                registroAsociado.setEjercicioAnotacion(registro.getResEje());
                registroAsociado.setNumAsiento(registro.getResNum());
                registroAsociado.setTipoEntrada(registro.getResTip());
                registroAsociado.setUorRegistro(registro.getResUor());

                ///Datos Registro
                log.error("------------Datos Registro----------");
                log.error("codDepartamento " + registroAsociado.getCodDepartamento());
                log.error("ejercicioAnotacion " + registroAsociado.getEjercicioAnotacion());
                log.error("numAsiento " + registroAsociado.getNumAsiento());
                log.error("tipoEntrada " + registroAsociado.getTipoEntrada());
                log.error("uorRegistro " + registroAsociado.getUorRegistro());
                log.error("------------Llamamos a ws tramitacion asociarExpRegistro() ----------");
                try {
                    value2 = binding.asociarExpRegistro(expediente, registroAsociado, inf);
                    log.error("------------Respuesta de asociarExpRegistro----------");
                    log.error("Status: " + value2.getStatus());
                    registroBatch.setObservaciones("");
                } catch (Exception e) {
                    registroBatch.setObservaciones("Excepción en asociarExpRegistro");
                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AACC_003");
                    err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW asociarExpRegistro con el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum());
                    err.setSituacion("JobExecute");
                    String causa = e.getCause().getMessage();
                    String error = e.getMessage();

                    grabarError(err, error, causa, numExpediente);
                    return null;
                }
                log.error("------------Fin Respuesta de asociarExpRegistro----------");

                registroBatch.setEjerRegistro(registro.getResEje());
                registroBatch.setNumRegistro(registro.getResNum());
                registroBatch.setNumExpediente(numExpediente);
                registroBatch.setCodTramite(tramite);
                registroBatch.setOperacion("asociarExpRegistro");
                registroBatch.setRelanzar(0);
                registroBatch.setCodOperacion(OP_ASOCIAR_REGISTRO);

                if (value2.getStatus() == 0) {
                    registroBatch.setResultado("OK");
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                    return expediente;
                } else {
                    registroBatch.setResultado("KO");
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AACC_003");
                    err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SWasociarExpRegistro con el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum() + ". Status: " + value2.getStatus() + " Error: " + value2.getError());
                    err.setSituacion("JobExecute");
                    String causa = "";
                    String error = "";
                    grabarError(err, error, causa, numExpediente);
                    return null;
                }

            } else {
                registroBatch.setEjerRegistro(registro.getResEje());
                registroBatch.setNumRegistro(registro.getResNum());
                registroBatch.setNumExpediente("");
                registroBatch.setCodTramite(0);
                registroBatch.setOperacion("iniciarExpedienteConOperacion");
                registroBatch.setResultado("--");
                registroBatch.setCodOperacion(OP_INICIO_EXPEDIENTE);
                registroBatch.setRelanzar(0);
                registroBatch.setObservaciones("Fallo al iniciarExpedienteConOperacion. Volverá a intentarlo en la siguiente ejecución");
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AACC_001");
                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC, al iniciar el expediente. Status: " + value.getStatus() + ". Error: " + value.getError());
                err.setSituacion("JobExecute");
                // String causa = ex.getCausaExcepcion();   //PARA CUANDO HAYA EXCEPCIONES
                // error = ice.getTrazaExcepcion();                
                String causa = "";
                String error = "";

                numExpediente = value.getIdExpediente().getNumeroExpediente();
                grabarError(err, error, causa, numExpediente);
                return null;
            }

        } catch (javax.xml.rpc.ServiceException jre) {
            log.error("Error en INICIO EXPEDIENTE AACC: ", jre);
            return null;
        } catch (Exception ex) {
            log.error("Error en INICIO EXPEDIENTE AACC: ", ex);
            return null;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /* 
    public boolean finalizarTramite (int codOrganizacion,RegistroAACCVO registro,ExpedienteVO expediente,TramiteVO idTramite,CondicionFinalizacionVO condFinalizacion,InfoConexionVO inf,WSTramitacionBindingStub binding){

        RespuestasTramitacionVO value;
        boolean result = false;
        RegistroBatchVO registroBatch = new RegistroBatchVO();          
       
        log.debug("begin finalizarTramite()");
        log.error("begin finalizarTramite()");
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();
            
            log.error("------------datos que enviamos a finalizar trámite ----------");         
        ///Expediente 
            log.error("------------Datos Expediente----------");
        //IdExpedientesVO            
            log.error("------------idExpedienteVO:----------");
            log.error("PROCEDIMIENTO "+ expediente.getIdExpedienteVO().getProcedimiento());            
            log.error("Ejercicio "+expediente.getIdExpedienteVO().getEjercicio());
            log.error("EXPEDIENTE "+ expediente.getIdExpedienteVO().getNumeroExpediente());
            log.error("------------FIN ID EXPEDIENTE1----------");
        //TramiteVO:            
            log.error("------------TramiteVO:----------");   
            log.error("Código interno "+ idTramite.getId().getCodTramite());
            log.error("Ocurrencia "+ idTramite.getId().getOcurrencia());
            log.error("Utr "+ idTramite.getUtr());
            log.error("Usuario fin "+ idTramite.getUsuarioFin());
            log.error("condicionFinalizacionVO:  ");  
            log.error("tipoFinalizacion "+ idTramite.getCondFinalizacion().getTipoFinalizacion());   
            if(idTramite.getCondFinalizacion().getFlujoSI()!=null){
                log.error("flujoSI:  ");
                log.error("tipoApertura "+ idTramite.getCondFinalizacion().getFlujoSI().getTipoApertura());
                for(int i=0; i<idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta().length; i++){
                    log.error("Cod.Trámite a iniciar "+i+": "+ idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta()[i].getCodTramite());  
                }
                log.error("Ocurrencia Trámite a iniciar "+ idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta()[0].getOcurrencia());                
            }
            if(idTramite.getCondFinalizacion().getFlujoNO()!=null){            
                log.error("flujoNO:  ");               
                log.error("tipoApertura "+ idTramite.getCondFinalizacion().getFlujoNO().getTipoApertura());
                log.error("Cod.Trámite a iniciar "+ idTramite.getCondFinalizacion().getFlujoNO().getListaRespuesta()[0].getCodTramite());              
                log.error("Ocurrencia Trámite a iniciar "+ idTramite.getCondFinalizacion().getFlujoNO().getListaRespuesta()[0].getOcurrencia());                
            }
            //condicionFinalizacionVO:            
            log.error("------------condicionFinalizacionVO:----------");      
            log.error("tipoFinalizacion "+ condFinalizacion.getTipoFinalizacion()); 
            if( condFinalizacion.getFlujoSI()!=null){
                log.error("flujoSI:  ");   
                log.error("tipoApertura "+ condFinalizacion.getFlujoSI().getTipoApertura());
                for(int i=0; i<condFinalizacion.getFlujoSI().getListaRespuesta().length; i++){
                    log.error("Cod.Trámite a iniciar "+i+": "+ condFinalizacion.getFlujoSI().getListaRespuesta()[i].getCodTramite());  
                }                
                log.error("Ocurrencia Trámite a iniciar "+ condFinalizacion.getFlujoSI().getListaRespuesta()[0].getOcurrencia());                
            }
            if( condFinalizacion.getFlujoNO()!=null){     
                log.error("flujoNO:  ");   
                log.error("tipoApertura "+ condFinalizacion.getFlujoNO().getTipoApertura());
                log.error("Cod.Trámite a iniciar "+ condFinalizacion.getFlujoNO().getListaRespuesta()[0].getCodTramite());                
                log.error("Ocurrencia Trámite a iniciar "+ condFinalizacion.getFlujoNO().getListaRespuesta()[0].getOcurrencia());                                  
            }       
            //InfoConexionVO:            
            log.error("------------InfoConexionVO:----------");  
            log.error("organizacion "+ inf.getOrganizacion());
            log.error("aplicacion "+ inf.getAplicacion());
                      
            if("S".equals(idTramite.getCondFinalizacion().getTipoFinalizacion())){               
                log.error("------------Llamamos a ws tramitacion finalizaTramite() ----------");                  
                value= binding.finalizaTramite(expediente,idTramite,condFinalizacion,inf);      
                log.error("------------Respuesta de finalizaTramite----------");                  
                registroBatch.setOperacion("finalizaTramite");                
            }else{ 
                log.error("------------Llamamos a ws tramitacion finalizaTramiteConOperacion() ----------");                  
                value= binding.finalizaTramiteConOperacion(expediente,idTramite,condFinalizacion,inf); 
                log.error("------------Respuesta de finalizaTramiteConOperacion----------");    
                registroBatch.setOperacion("finalizaTramiteConOperacion");                
            }                          
            log.error("Status: "+value.getStatus());
            log.error("------------Fin Respuesta de finalizaTramiteConOperacion----------");
             
            log.error("------------Fin Llamada a ws tramitacion finalizaTramiteConOperacion() ----------");    

            if(value.getStatus()==0){                       
                result = true;
                registroBatch.setResultado("OK");                
            } else {
                registroBatch.setResultado("KO");                  
                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AACC_007");
                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC, al avanzar el trámite "+idTramite.getId().getCodTramite()+": "+value.getError());
                err.setSituacion("JobExecute");
             // String causa = ex.getCausaExcepcion();   //PARA CUANDO HAYA EXCEPCIONES
             // error = ice.getTrazaExcepcion();                
                String causa = "";
                String error = "";

                String numExpediente = expediente.getIdExpedienteVO().getNumeroExpediente();  
                grabarError(err, error, causa, numExpediente);                           
                result = false;
            }
            registroBatch.setEjerRegistro(registro.getResEje());
            registroBatch.setNumRegistro(registro.getResNum());                
            registroBatch.setNumExpediente(expediente.getIdExpedienteVO().getNumeroExpediente());
            registroBatch.setCodTramite(idTramite.getId().getCodTramite());                                  
            registroBatch.setCodOperacion(OP_AVANZAR_TRAMITE);                                
            if(idTramite.getId().getCodTramite()==Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO,ConstantesMeLanbide54.FICHERO_PROPIEDADES))){
                registroBatch.setRelanzar(1);    //en caso de trámite 1004-Comunicación número de agencia/centro
            }else{
                registroBatch.setRelanzar(0);                
            }            
            registroBatch.setObservaciones("");
            MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);            
            
        } catch (Exception ex) {
            log.error("Error en avanzar trámite AACC: ", ex);
            result = false;
            
            registroBatch.setEjerRegistro(registro.getResEje());
            registroBatch.setNumRegistro(registro.getResNum());                
            registroBatch.setNumExpediente(expediente.getIdExpedienteVO().getNumeroExpediente());
            registroBatch.setCodTramite(idTramite.getId().getCodTramite());                
            registroBatch.setOperacion("finalizaTramite");    
            registroBatch.setResultado("KO"); 
            registroBatch.setCodOperacion(OP_AVANZAR_TRAMITE);                    
            registroBatch.setRelanzar(0);
            registroBatch.setObservaciones("Excepción en el método finalizaTramite ");
            try {              
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
            } catch (Exception ex1) {
                log.error("Error al InsertarRegistroBatch" + ex1);
            }
        }finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return result;
    }
     */
    public void grabarError(ErrorBean error, String excepError, String causa, String numExp) {
        AdaptadorSQLBD adapt = null;
        try {
            log.error("grabando el error");
            error.setMensajeExcepError(excepError); //churro e.getException
            error.setTraza(excepError);
            error.setCausa(causa);//causa    .getMensaje.getcausa
            log.error("causa: " + causa);
            log.error("numExp: " + numExp);
            if ("".equals(numExp)) {
                numExp = "0000/ERR/000000";
            }

            String idProcedimiento = "";
            idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide54.ID_PROC_AACC, ConstantesMeLanbide54.FICHERO_ADAPTADORES);
            log.error("idProcedimiento: " + idProcedimiento);
            error.setIdProcedimiento(idProcedimiento);
            error.setSistemaOrigen("REGEXLAN");
            error.setIdClave("");
            error.setErrorLog("flexia_debug");
            error.setIdFlexia(numExp);

            String respGrabarError = RegistroErrores.registroError(error);
            log.error("Respuesta RegistroErrores.registroError(): " + respGrabarError);
        } catch (Exception ex) {
            log.error("Error al grabarError" + ex);
        }
    }

    public boolean grabadoDatosSuplementariosXML(byte[] archivoXML, int codOrg, String codProc, Connection con, String numExpediente, RegistroAACCVO registro) {
        log.error("Entra en grabadoDatosSuplementariosXML");
        RegistroBatchVO registroBatch = new RegistroBatchVO();
        boolean ret = false;
        RegistroValueObject documento = new RegistroValueObject();
        int resSup = 1;
        documento.setDoc(archivoXML);
        //Recupera los datos suplementarios del documento xml
        String codCampo = "";
        String valorCampo = "";
        String tipoCampo = "";
        String nombreTabla = "";
        List<GeneralValueObject> listaDatosSuplementarios = leerDatosSuplementariosXML(documento, ConstantesMeLanbide54.TAG_XML_CAMPOSVARIABLES);

        if (listaDatosSuplementarios == null) {
            log.error("grabadoDatosSuplementariosXML: Error el leer el fichero XML");
        } else {
            //Guarda los datos suplementarios 
            if (listaDatosSuplementarios.size() > 0) {

                registroBatch.setEjerRegistro(registro.getResEje());
                registroBatch.setNumRegistro(registro.getResNum());
                registroBatch.setNumExpediente(numExpediente);
                registroBatch.setCodTramite(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES)));
                registroBatch.setOperacion("grabadoDatosSuplementariosXML");
                registroBatch.setCodOperacion(OP_GRABAR_DATOS_SUPLEMENTARIOS);
                registroBatch.setRelanzar(0);
                registroBatch.setObservaciones("");

                try {
                    for (GeneralValueObject gv : listaDatosSuplementarios) {
                        // log.error (gv.getAtributo(ConstantesMeLanbide54.TAG_XML_CODIGO) + " - " + gv.getAtributo(ConstantesMeLanbide54.TAG_XML_VALOR));
                        codCampo = (String) gv.getAtributo(ConstantesMeLanbide54.TAG_XML_CODIGO);
                        log.error("Codigo campo: " + codCampo);
                        valorCampo = (String) gv.getAtributo(ConstantesMeLanbide54.TAG_XML_VALOR);
                        tipoCampo = MeLanbide54DAO.getInstance().getTipoCampo(con, codOrg, codProc, codCampo);
                        log.error("Tipo campo: " + tipoCampo);
                        nombreTabla = MeLanbide54DAO.getInstance().getNombreTabla(con, Integer.parseInt(tipoCampo));
                        log.error("Nombre tabla: " + nombreTabla);
                        if (!MeLanbide54DAO.getInstance().existeCampoSuplementarioAACC(con, codOrg, numExpediente, codCampo, valorCampo, nombreTabla)) {
                            if (resSup == 1) {
                                resSup = MeLanbide54DAO.getInstance().grabarCampoSuplementarioAACC(con, codOrg, numExpediente, codCampo, valorCampo, nombreTabla);
                            }
                        }
                        ret = true;
                    }
                } catch (Exception ex) {
                    log.error("Error en grabadoDatosSuplementariosXML en expediente: " + numExpediente + " ,código: " + codCampo + " valor: " + valorCampo + " nombre tabla: " + nombreTabla, ex);
                    ret = false;
                    //GRABAR EN LA NUEVA TABLA   
                    registroBatch.setResultado("KO");
                    registroBatch.setObservaciones("Excepción al grabadoDatosSuplementariosXML");
                    try {
                        MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                    } catch (Exception ex1) {
                        log.error("Error al InsertarRegistroBatch" + ex1);
                    }

                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AACC_006");
                    err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al grabadoDatosSuplementariosXML con el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum());
                    err.setSituacion("JobExecute");
                    String causa = ex.getCause().getMessage();
                    String error = ex.getMessage();

                    grabarError(err, error, causa, numExpediente);
                }
            }

            if (resSup == 0) {
                log.error("grabadoDatosSuplementariosXML: No ha guardado los datos suplementarios del fichero. Puede que no hayan o que ya existían");
                registroBatch.setResultado("--");
                registroBatch.setObservaciones("No ha grabado grabadoDatosSuplementariosXML. Puede ser que no haya datos o que ya existían.");
                try {
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                } catch (Exception ex1) {
                    log.error("Error al InsertarRegistroBatch" + ex1);
                }
            } else {
                registroBatch.setResultado("OK");
                try {
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                } catch (Exception ex1) {
                    log.error("Error al InsertarRegistroBatch" + ex1);
                }
            }
        }
        return ret;

    }

    private List<GeneralValueObject> leerDatosSuplementariosXML(RegistroValueObject documento, String padre) {

        log.error("grabadoDatosSuplementariosXML: Inicio lectura tags fichero" + documento.getNombreDoc());

        List<GeneralValueObject> listaCamposExpediente = new ArrayList<GeneralValueObject>();

        try {
            byte[] ficheroBytes = documento.getDoc();
            String fichero = new String(ficheroBytes, ConstantesMeLanbide54.CODIFICACION_ISO_8859_1);
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new StringReader(fichero));
            Element nodoRaiz = doc.getRootElement();

            if (nodoRaiz != null) {
                Element nodoPadre = nodoRaiz.getChild(padre);
                if (nodoPadre != null) {
                    List listaNodosHijos = nodoPadre.getChildren();
                    for (int i = 0; i < listaNodosHijos.size(); i++) {
                        Element nodoHijo = (Element) listaNodosHijos.get(i);
                        String codCampo = nodoHijo.getChildTextTrim(ConstantesMeLanbide54.TAG_XML_CODCAMPO);
                        String valorCampo = nodoHijo.getChildTextTrim(ConstantesMeLanbide54.TAG_XML_VALORCAMPO);
                        GeneralValueObject generalVO = new GeneralValueObject();
                        generalVO.setAtributo(ConstantesMeLanbide54.TAG_XML_CODIGO, codCampo);
                        generalVO.setAtributo(ConstantesMeLanbide54.TAG_XML_VALOR, valorCampo);

                        listaCamposExpediente.add(generalVO);
                    }
                }
            }
        } catch (IOException io) {
            //lista = null;
            listaCamposExpediente = null;
            log.error("grabadoDatosSuplementariosXML: leerDatosSuplementariosXML(): Error al procesar el fichero XML. IOExecption: " + io.getMessage());
        } catch (JDOMException jdo) {
            //lista = null;
            listaCamposExpediente = null;
            log.error("grabadoDatosSuplementariosXML: leerDatosSuplementariosXML(): Error al procesar el fichero XML. JDOM Exception: " + jdo.getMessage());
        } catch (Exception e) {
            //lista = null;
            listaCamposExpediente = null;
            log.error("grabadoDatosSuplementariosXML: leerDatosSuplementariosXML(): Error al procesar el fichero XML. Exception: " + e.getMessage());
        }
        log.error("grabadoDatosSuplementariosXML: leerDatosSuplementariosXML(): Fin lectura tags fichero " + documento.getNombreDoc());
        return listaCamposExpediente;
    }

    public ExpedienteVO inicioExpedienteAACC_relanzar(int codigoUOR, int codOrganizacion, RegistroAACCVO registro, String codProc, InfoConexionVO inf, WSTramitacionBindingStub binding) {

        RespuestasTramitacionVO value = null;
        boolean continuar = false;
        log.debug("begin inicioExpedienteAACC()");
        log.error("begin inicioExpedienteAACC()");
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();

            int tramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
            ExpedienteVO exp = new ExpedienteVO();
            IdExpedienteVO idexpediente = new IdExpedienteVO();
            idexpediente.setEjercicio(Calendar.getInstance().get(Calendar.YEAR));//año actual
            int anioActual = idexpediente.getEjercicio();
            idexpediente.setProcedimiento(codProc);
            int usuarioRegistro = registro.getResUsu();
            log.error("usuario TRAMITE " + usuarioRegistro);

            exp.setIdExpedienteVO(idexpediente);
            exp.setUor(codigoUOR);
            exp.setUorTramiteInicio(codigoUOR);
            exp.setUsuario(usuarioRegistro);
            exp.setAsunto(registro.getResAsu());
            log.error("Asunto registro: " + exp.getAsunto());

            InteresadoExpedienteVO interesado = new InteresadoExpedienteVO();
            interesado = MeLanbide54Manager.getInstance().getDatosInteresado(codOrganizacion, anioActual, registro, con);
            List<InteresadoExpedienteVO> interesados = new ArrayList<InteresadoExpedienteVO>();
            interesados.add(interesado);
            exp.setInteresados(interesados.toArray(new InteresadoExpedienteVO[]{}));

            log.error("------------Datos Interesado----------");
            log.error("Rol: " + interesado.getRol());
            log.error("-----------------Datos de remitente/Datos del Tercero---------------");
            log.error("Apellido 1 remitente" + interesado.getTercero().getAp1());
            log.error("Apellido 2 remitente" + interesado.getTercero().getAp2());
            log.error("Documento:" + interesado.getTercero().getDoc());
            log.error("Email:" + interesado.getTercero().getEmail());
            log.error("Nombre:" + interesado.getTercero().getNombre());
            log.error("Telefono:" + interesado.getTercero().getTelefono());
            log.error("TipoDoc:" + interesado.getTercero().getTipoDoc());
            log.error("----------------------fin Datos de remitente/Datos del Tercero-----------");

            log.error("------------Datos Domicilio----------");
            log.error("Domicilio Bloque: " + interesado.getTercero().getDomicilio().getBloque());
            log.error("Domicilio COdMunicipio: " + interesado.getTercero().getDomicilio().getCodMunicipio());
            log.error("Domicilio CodPais: " + interesado.getTercero().getDomicilio().getCodPais());
            log.error("Domicilio Cpostal: " + interesado.getTercero().getDomicilio().getCodPostal());
            log.error("Domicilio CodProvincia: " + interesado.getTercero().getDomicilio().getCodProvincia());
            log.error("Domicilio Emplazamiento: " + interesado.getTercero().getDomicilio().getEmplazamiento());
            log.error("Domicilio Esprincipal : " + interesado.getTercero().getDomicilio().isEsPrincipal());
            log.error("Domicilio Escalera: " + interesado.getTercero().getDomicilio().getEscalera());
            log.error("Domicilio Nombre: " + interesado.getTercero().getDomicilio().getNombreVia());
            log.error("Domicilio Planta: " + interesado.getTercero().getDomicilio().getPlanta());
            log.error("Domicilio Portal: " + interesado.getTercero().getDomicilio().getPortal());
            log.error("Domicilio PrimerNumero: " + interesado.getTercero().getDomicilio().getPrimerNumero());
            log.error("Domicilio PrimeraLetra: " + interesado.getTercero().getDomicilio().getPrimeraLetra());
            log.error("Domicilio Puerta: " + interesado.getTercero().getDomicilio().getPuerta());
            log.error("Domicilio TipoVia: " + interesado.getTercero().getDomicilio().getTipoVia());
            log.error("Domicilio UltimaLetra: " + interesado.getTercero().getDomicilio().getUltimaLetra());
            log.error("Domicilio UltimoNumero: " + interesado.getTercero().getDomicilio().getUltimoNumero());
            log.error("------------FIN Datos Domicilio----------");

            log.error("------------datos que enviamos al expediente ----------");

            log.error("------------UOR----------");
            log.error("Uor: " + exp.getUor());
            log.error("uorTramiteInicio: " + exp.getUorTramiteInicio());
            log.error("usuario: " + exp.getUsuario());
            log.error("Asunto entrada: " + exp.getAsunto());
            log.error("------------FIN DATOS UOR----------");
            ///Expediente 
            log.error("------------Datos Expediente----------");
            //IdExpedientesVO            
            log.error("------------ID EXPEDIENTE1----------");
            log.error("Ejercicio " + idexpediente.getEjercicio());
            log.error("PROCEDIMIENTO " + idexpediente.getProcedimiento());
            log.error("------------FIN ID EXPEDIENTE1----------");

            log.error("------------Llamamos a ws tramitacion iniciarExpedienteConOperacion() ----------");
            RegistroBatchVO registroBatch = new RegistroBatchVO();
            String numExpediente = "";

            registroBatch.setObservaciones("");
            registroBatch.setEjerRegistro(registro.getResEje());
            registroBatch.setNumRegistro(registro.getResNum());
            registroBatch.setNumExpediente("");
            registroBatch.setCodTramite(tramite);
            registroBatch.setOperacion("iniciarExpedienteConOperacion");
            registroBatch.setRelanzar(0);
            registroBatch.setCodOperacion(OP_INICIO_EXPEDIENTE);

            try {
                value = binding.iniciarExpedienteConOperacion(exp, inf);
                log.error("------------Respuesta de iniciarExpedienteConOperacion----------");
                log.error("Documento: " + value.getDocumento());
                log.error("Expediente: " + value.getExpediente());
                log.error("Ejercicio: " + value.getIdExpediente().getEjercicio());
                log.error("Número: " + value.getIdExpediente().getNumero());
                log.error("Numero_Expediente: " + value.getIdExpediente().getNumeroExpediente());
                log.error("Procedimiento: " + value.getIdExpediente().getProcedimiento());
                log.error("------------Fin Respuesta de iniciarExpedienteConOperacion----------");
                log.error("idTramite: " + value.getIdtramite());
                log.error("Tramite: " + value.getTramite());
                log.error("Status: " + value.getStatus());
                log.error("Error: " + value.getError());

                if (value.getStatus() == 0) {
                    numExpediente = value.getIdExpediente().getNumeroExpediente();
                    registroBatch.setResultado("OK");
                    registroBatch.setNumExpediente(numExpediente);
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                    //Comprobar la tabla TAREAS_PENDIENTES_INICIO. Si tiene registros para el expediente y trámite...
                    //...es que ha fallado la operación 'generarMisGestInicioOficio'   
                    if (MeLanbide54DAO.getInstance().buscarOperacionesPendientes(con, numExpediente, tramite)) {
                        registroBatch.setResultado("KO");
                        registroBatch.setObservaciones("Existe en TAREAS_PENDIENTES_INICIO");
                    } else {
                        registroBatch.setResultado("OK");
                        registroBatch.setObservaciones("");
                    }
                    registroBatch.setOperacion("generarMisGestInicioOficio");
                    registroBatch.setRelanzar(0);
                    registroBatch.setCodOperacion(OP_MISGESTIONES_INICIO_EXPEDIENTE);
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                    ExpedienteVO expediente = new ExpedienteVO();
                    IdExpedienteVO idexpediente2 = new IdExpedienteVO(value.getIdExpediente().getEjercicio(), value.getIdExpediente().getNumero(), value.getIdExpediente().getNumeroExpediente(), value.getIdExpediente().getProcedimiento());
                    expediente.setIdExpedienteVO(idexpediente2);
                    return expediente;

                } else {
                    registroBatch.setResultado("KO");
                    registroBatch.setNumExpediente(numExpediente);
                    registroBatch.setObservaciones("Fallo al iniciarExpedienteConOperacion. Volverá a intentarlo en la siguiente ejecución");
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AACC_001");
                    err.setMensajeError("Error en el job de Tramitación automática de agencias AACC, al iniciar el expediente. Status: " + value.getStatus() + ". Error: " + value.getError());
                    err.setSituacion("JobExecute");
                    // String causa = ex.getCausaExcepcion();   //PARA CUANDO HAYA EXCEPCIONES
                    // error = ice.getTrazaExcepcion();                
                    String causa = "";
                    String error = "";

                    numExpediente = value.getIdExpediente().getNumeroExpediente();
                    grabarError(err, error, causa, numExpediente);
                    return null;
                }
            } catch (Exception e) {
                registroBatch.setResultado("KO");
                registroBatch.setObservaciones("Excepción en iniciarExpedienteConOperacion ");
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AACC_001");
                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW iniciarExpedienteConOperacion ");
                err.setSituacion("JobExecute");
                String causa = e.getCause().getMessage();
                String error = e.getMessage();

                numExpediente = "";
                grabarError(err, error, causa, numExpediente);
                return null;
            }

        } catch (javax.xml.rpc.ServiceException jre) {
            log.error("Error en INICIO EXPEDIENTE AACC: ", jre);
            return null;
        } catch (Exception ex) {
            log.error("Error en INICIO EXPEDIENTE AACC: ", ex);
            return null;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean asociarExpedienteRegistro(int codOrganizacion, RegistroAACCVO registro, ExpedienteVO expediente, InfoConexionVO inf, WSTramitacionBindingStub binding) {

        RespuestasTramitacionVO value = null;
        RegistroBatchVO registroBatch = new RegistroBatchVO();
        int tramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
        String numExpediente = expediente.getIdExpedienteVO().getNumeroExpediente();
        log.debug("begin asociarExpedienteRegistro()");
        log.error("begin asociarExpedienteRegistro()");
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();

            RegistroAsociadoVO registroAsociado = new RegistroAsociadoVO();
            //obligatorios
            registroAsociado.setCodDepartamento(registro.getResDep());
            registroAsociado.setEjercicioAnotacion(registro.getResEje());
            registroAsociado.setNumAsiento(registro.getResNum());
            registroAsociado.setTipoEntrada(registro.getResTip());
            registroAsociado.setUorRegistro(registro.getResUor());

            ///Datos Registro
            log.error("------------Datos Registro----------");
            log.error("codDepartamento " + registroAsociado.getCodDepartamento());
            log.error("ejercicioAnotacion " + registroAsociado.getEjercicioAnotacion());
            log.error("numAsiento " + registroAsociado.getNumAsiento());
            log.error("tipoEntrada " + registroAsociado.getTipoEntrada());
            log.error("uorRegistro " + registroAsociado.getUorRegistro());
            log.error("------------Llamamos a ws tramitacion asociarExpRegistro() ----------");

            registroBatch.setEjerRegistro(registro.getResEje());
            registroBatch.setNumRegistro(registro.getResNum());
            registroBatch.setNumExpediente(numExpediente);
            registroBatch.setCodTramite(tramite);
            registroBatch.setOperacion("asociarExpRegistro");
            registroBatch.setRelanzar(0);
            registroBatch.setCodOperacion(OP_ASOCIAR_REGISTRO);
            registroBatch.setObservaciones("");

            try {
                value = binding.asociarExpRegistro(expediente, registroAsociado, inf);
                log.error("------------Respuesta de asociarExpRegistro----------");
                log.error("Status: " + value.getStatus());
                if (value.getStatus() == 0) {
                    registroBatch.setResultado("OK");
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                    return true;
                } else {
                    registroBatch.setResultado("KO");
                    registroBatch.setObservaciones("Error al asociarExpRegistro con el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum() + ". Status: " + value.getStatus() + " Error: " + value.getError());
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AACC_004");
                    err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SWasociarExpRegistro con el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum() + ". Status: " + value.getStatus() + " Error: " + value.getError());
                    err.setSituacion("JobExecute");
                    String causa = "";
                    String error = "";
                    grabarError(err, error, causa, numExpediente);
                    return false;
                }

            } catch (Exception e) {
                registroBatch.setResultado("KO");
                registroBatch.setObservaciones("Excepción en asociarExpRegistro");
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AACC_003");
                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW asociarExpRegistro con el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum());
                err.setSituacion("JobExecute");
                String causa = e.getCause().getMessage();
                String error = e.getMessage();

                grabarError(err, error, causa, numExpediente);
                return false;
            }
        } catch (Exception ex) {
            log.error("Error en ASOCIAR EXPEDIENTE AL REGISTRO AACC: ", ex);
            return false;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }

    }

    public boolean finalizarTramite_relanzar(int codOrganizacion, RegistroAACCVO registro, ExpedienteVO expediente, TramiteVO idTramite, CondicionFinalizacionVO condFinalizacion, InfoConexionVO inf, WSTramitacionBindingStub binding) {

        RespuestasTramitacionVO value;
        boolean result = false;
        RegistroBatchVO registroBatch = new RegistroBatchVO();
        String numExpediente = expediente.getIdExpedienteVO().getNumeroExpediente();
        log.debug("begin finalizarTramite()");
        log.error("begin finalizarTramite()");
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        Connection con = null;
        try {
            con = adaptador.getConnection();

            log.error("------------datos que enviamos a finalizar trámite ----------");
            ///Expediente 
            log.error("------------Datos Expediente----------");
            //IdExpedientesVO            
            log.error("------------idExpedienteVO:----------");
            log.error("PROCEDIMIENTO " + expediente.getIdExpedienteVO().getProcedimiento());
            log.error("Ejercicio " + expediente.getIdExpedienteVO().getEjercicio());
            log.error("EXPEDIENTE " + expediente.getIdExpedienteVO().getNumeroExpediente());
            log.error("------------FIN ID EXPEDIENTE1----------");
            //TramiteVO:            
            log.error("------------TramiteVO:----------");
            log.error("Código interno " + idTramite.getId().getCodTramite());
            log.error("Ocurrencia " + idTramite.getId().getOcurrencia());
            log.error("Utr " + idTramite.getUtr());
            log.error("Usuario fin " + idTramite.getUsuarioFin());
            log.error("condicionFinalizacionVO:  ");
            log.error("tipoFinalizacion " + idTramite.getCondFinalizacion().getTipoFinalizacion());
            if (idTramite.getCondFinalizacion().getFlujoSI() != null) {
                log.error("flujoSI:  ");
                log.error("tipoApertura " + idTramite.getCondFinalizacion().getFlujoSI().getTipoApertura());
                for (int i = 0; i < idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta().length; i++) {
                    log.error("Cod.Trámite a iniciar " + i + ": " + idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta()[i].getCodTramite());
                }
                log.error("Ocurrencia Trámite a iniciar " + idTramite.getCondFinalizacion().getFlujoSI().getListaRespuesta()[0].getOcurrencia());
            }
            if (idTramite.getCondFinalizacion().getFlujoNO() != null) {
                log.error("flujoNO:  ");
                log.error("tipoApertura " + idTramite.getCondFinalizacion().getFlujoNO().getTipoApertura());
                log.error("Cod.Trámite a iniciar " + idTramite.getCondFinalizacion().getFlujoNO().getListaRespuesta()[0].getCodTramite());
                log.error("Ocurrencia Trámite a iniciar " + idTramite.getCondFinalizacion().getFlujoNO().getListaRespuesta()[0].getOcurrencia());
            }
            //condicionFinalizacionVO:            
            log.error("------------condicionFinalizacionVO:----------");
            log.error("tipoFinalizacion " + condFinalizacion.getTipoFinalizacion());
            if (condFinalizacion.getFlujoSI() != null) {
                log.error("flujoSI:  ");
                log.error("tipoApertura " + condFinalizacion.getFlujoSI().getTipoApertura());
                for (int i = 0; i < condFinalizacion.getFlujoSI().getListaRespuesta().length; i++) {
                    log.error("Cod.Trámite a iniciar " + i + ": " + condFinalizacion.getFlujoSI().getListaRespuesta()[i].getCodTramite());
                }
                log.error("Ocurrencia Trámite a iniciar " + condFinalizacion.getFlujoSI().getListaRespuesta()[0].getOcurrencia());
            }
            if (condFinalizacion.getFlujoNO() != null) {
                log.error("flujoNO:  ");
                log.error("tipoApertura " + condFinalizacion.getFlujoNO().getTipoApertura());
                log.error("Cod.Trámite a iniciar " + condFinalizacion.getFlujoNO().getListaRespuesta()[0].getCodTramite());
                log.error("Ocurrencia Trámite a iniciar " + condFinalizacion.getFlujoNO().getListaRespuesta()[0].getOcurrencia());
            }
            //InfoConexionVO:            
            log.error("------------InfoConexionVO:----------");
            log.error("organizacion " + inf.getOrganizacion());
            log.error("aplicacion " + inf.getAplicacion());

            registroBatch.setEjerRegistro(registro.getResEje());
            registroBatch.setNumRegistro(registro.getResNum());
            registroBatch.setNumExpediente(expediente.getIdExpedienteVO().getNumeroExpediente());
            registroBatch.setCodTramite(idTramite.getId().getCodTramite());
            registroBatch.setCodOperacion(OP_AVANZAR_TRAMITE);
            if (idTramite.getId().getCodTramite() == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES))) {
                registroBatch.setRelanzar(1);    //en caso de trámite 1004-Comunicación número de agencia/centro
            } else {
                registroBatch.setRelanzar(0);
            }
            registroBatch.setObservaciones("");

            try {
                if ("S".equals(idTramite.getCondFinalizacion().getTipoFinalizacion())) {
                    log.error("------------Llamamos a ws tramitacion finalizaTramite() ----------");
                    value = binding.finalizaTramite(expediente, idTramite, condFinalizacion, inf);
                    log.error("------------Respuesta de finalizaTramite----------");
                    registroBatch.setOperacion("finalizaTramite");
                } else {
                    log.error("------------Llamamos a ws tramitacion finalizaTramiteConOperacion() ----------");
                    value = binding.finalizaTramiteConOperacion(expediente, idTramite, condFinalizacion, inf);
                    log.error("------------Respuesta de finalizaTramiteConOperacion----------");
                    registroBatch.setOperacion("finalizaTramiteConOperacion");
                }
                log.error("Status: " + value.getStatus());
                log.error("------------Fin Respuesta de finalizaTramiteConOperacion----------");

                if (value.getStatus() == 0) {
                    registroBatch.setResultado("OK");
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                    return true;
                } else {
                    registroBatch.setResultado("KO");
                    registroBatch.setObservaciones("Error al finalizarTramite " + idTramite.getId().getCodTramite() + ": " + value.getError());;
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AACC_007");
                    err.setMensajeError("Error en el job de Tramitación automática de agencias AACC, al avanzar el trámite " + idTramite.getId().getCodTramite() + ": " + value.getError());
                    err.setSituacion("JobExecute");
                    String causa = "";
                    String error = "";
                    grabarError(err, error, causa, numExpediente);
                    return false;
                }

            } catch (Exception e) {
                registroBatch.setResultado("KO");
                registroBatch.setObservaciones("Excepción en finalizarTramite");
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);

                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AACC_007");
                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC, al avanzar el trámite " + idTramite.getId().getCodTramite());
                err.setSituacion("JobExecute");
                String causa = e.getCause().getMessage();
                String error = e.getMessage();
                grabarError(err, error, causa, numExpediente);
                return false;
            }

        } catch (Exception ex) {
            log.error("Error al AVANZAR EL TRÁMITE ", ex);
            return false;

        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    // Generar mis gestiones inicio oficio --> MELANBIDE 43
    public void generarMisGestInicioOficio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide43.MELANBIDE43");
        final Object me43Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("generarMisGestInicioOficio", types);
        method.invoke(me43Class, codOrganizacion, codTramite, ocurrenciaTramite, numExpediente);

        //¿no debería devolver un String?
    }

    public IWACCIFSResponse.AREACOMUNICACION llamadaSWConsultaAgencia(Connection con, String cif, String numExpediente, RegistroAACCVO registro) {

        log.debug("begin llamadaSWConsultaAgencia()");
        RegistroBatchVO registroBatch = new RegistroBatchVO();

        registroBatch.setEjerRegistro(registro.getResEje());
        registroBatch.setNumRegistro(registro.getResNum());
        registroBatch.setNumExpediente(numExpediente);
        registroBatch.setCodTramite(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMPROBACION_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES)));
        registroBatch.setOperacion("llamadaSWConsultaAgencia");
        registroBatch.setRelanzar(0);
        registroBatch.setCodOperacion(OP_SW_CONSULTA_AGENCIA);
        registroBatch.setObservaciones("");

        try {
            IWACCIFSResponse.AREACOMUNICACION areacomunicacionResponseC = null;

            String urlWS_IWACCIFS = ConfigurationParameter.getParameter(ConstantesMeLanbide54.URL_SW_IWACCIFS, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            String NameSpaceWS_IWACCIFS = ConfigurationParameter.getParameter(ConstantesMeLanbide54.NAMESPACE_SW_IWACCIFS, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            String nameWS_IWACCIFS = ConfigurationParameter.getParameter(ConstantesMeLanbide54.NAME_SW_IWACCIFS, ConstantesMeLanbide54.FICHERO_PROPIEDADES);

            URL url_IWACCIFS = null;
            try {
                url_IWACCIFS = new URL(urlWS_IWACCIFS);
            } catch (MalformedURLException ex) {
                java.util.logging.Logger.getLogger(TramitacionAutomaticaAgenciasAACCJob.class.getName()).log(Level.SEVERE, null, ex);
            }
            QName qName_IWACCIFS = new QName(NameSpaceWS_IWACCIFS, nameWS_IWACCIFS);

            IWACCIFS_Service iwaccifs = new IWACCIFS_Service(url_IWACCIFS, qName_IWACCIFS);
            es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.consultaagencia.IESISPEPort ePort = iwaccifs.getIESISPESOAP12Port();
            IWACCIFS.AREACOMUNICACION areacomunicacionRequest = new IWACCIFS.AREACOMUNICACION();
            IWACCIFS.AREACOMUNICACION.ADCAREACONTROL adcareacontrol = new IWACCIFS.AREACOMUNICACION.ADCAREACONTROL();
            IWACCIFS.AREACOMUNICACION.ADEDATOSENTRADA adedatosentrada = new IWACCIFS.AREACOMUNICACION.ADEDATOSENTRADA();
            IWACCIFS.AREACOMUNICACION.ADSDATOSSALIDA adsdatossalida = new IWACCIFS.AREACOMUNICACION.ADSDATOSSALIDA();

            adcareacontrol.setADCPROGPRINC(nameWS_IWACCIFS);
            adcareacontrol.setADCCANALORIGEN("S");
            // ...
            String usuarioSW_SEPE = ConfigurationParameter.getParameter(ConstantesMeLanbide54.USUARIO_SWSEPE, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            adedatosentrada.setADEUSUARIO(usuarioSW_SEPE);
            //adedatosentrada.setADENIFE("B85236545");      //DAVID
            adedatosentrada.setADENIFE(cif);
            // La parte de salida no hace falta, sera lo que devuelve el WS
            areacomunicacionRequest.setADCODCONTROLENTIREX(BigDecimal.ZERO);
            areacomunicacionRequest.setADCAREACONTROL(adcareacontrol);
            areacomunicacionRequest.setADEDATOSENTRADA(adedatosentrada);
            areacomunicacionRequest.setADSDATOSSALIDA(adsdatossalida);

            areacomunicacionResponseC = ePort.iwaccifs(areacomunicacionRequest);
            //Si no existe la Agencia -->   <ADC-COD-ERROR>IAG0018</ADC-COD-ERROR>
            //Si existe la Agencia -->      <ADC-COD-ERROR>IAG0017</ADC-COD-ERROR>       
            String codErrorIWACCIFS = areacomunicacionResponseC.getADCAREACONTROL().getADCCODERROR();
            String existe_agenciaSW = ConfigurationParameter.getParameter(ConstantesMeLanbide54.EXISTE_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            String no_existe_agenciaSW = ConfigurationParameter.getParameter(ConstantesMeLanbide54.NO_EXISTE_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES);

            if (existe_agenciaSW.equals(codErrorIWACCIFS)) {
                //Grabar en la tabla --grabado OK a SW Consula Agencias
                registroBatch.setResultado("OK");
                registroBatch.setObservaciones("Existe Agencia de colocación");
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
            } else {
                if (no_existe_agenciaSW.equals(codErrorIWACCIFS)) {
                    registroBatch.setResultado("OK");
                    registroBatch.setObservaciones("No existe Agencia de colocación con el CIF " + areacomunicacionResponseC.getADEDATOSENTRADA().getADENIFE());
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                } else {
                    //Grabar en la tabla --Error en la consulta de Agencias.
                    registroBatch.setResultado("KO");
                    registroBatch.setObservaciones("Error en el SW Consulta Agencia de colocación con el CIF " + areacomunicacionResponseC.getADEDATOSENTRADA().getADENIFE() + " .Error: " + areacomunicacionResponseC.getADCAREACONTROL().getADCLITERROR());
                    MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                    //Grabar error 
                    ErrorBean err = new ErrorBean();
                    err.setIdError("TRAMITACION_AACC_011");
                    err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW Consulta Agencia, en el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum() + " .Error: " + areacomunicacionResponseC.getADCAREACONTROL().getADCLITERROR());
                    err.setSituacion("JobExecute");
                    String causa = "";
                    String error = "";
                    grabarError(err, error, causa, numExpediente);
                }
            }
            return areacomunicacionResponseC;

        } catch (Exception ex) {
            registroBatch.setResultado("KO");
            registroBatch.setObservaciones("Excepción en SW Consulta Agencia");
            try {
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
            } catch (Exception ex1) {
                log.error("Error en el sW Alta Agencia ", ex1);
                return null;
            }
            ErrorBean err = new ErrorBean();
            err.setIdError("TRAMITACION_AACC_011");
            err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW Consulta Agencia, en el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum());
            err.setSituacion("JobExecute");
            String causa = ex.getCause().getMessage();
            String error = ex.getMessage();
            grabarError(err, error, causa, numExpediente);
            return null;
        }
    }

    public IWACALASResponse.AREACOMUNICACION llamadaSWAltaAgencia(Connection con, int codOrg, TerceroVO tercero, String numExpediente, RegistroAACCVO registro) {

        log.debug("begin llamadaSWAltaAgencia()");
        String codCampo = "";
        String valorCampo = "";
        String nombreTabla = "";

        RegistroBatchVO registroBatch = new RegistroBatchVO();

        registroBatch.setEjerRegistro(registro.getResEje());
        registroBatch.setNumRegistro(registro.getResNum());
        registroBatch.setNumExpediente(numExpediente);
        registroBatch.setCodTramite(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.GENERACION_NMERO_DE_AGENCIA_EN_SILCOI_WEB, ConstantesMeLanbide54.FICHERO_PROPIEDADES)));
        registroBatch.setOperacion("llamadaSWAltaAgencia");
        registroBatch.setRelanzar(0);
        registroBatch.setCodOperacion(OP_SW_ALTA_AGENCIA);
        registroBatch.setObservaciones("");

        try {
            IWACALASResponse.AREACOMUNICACION areacomunicacionResponseA = null;

            String urlWS_IWACALAS = ConfigurationParameter.getParameter(ConstantesMeLanbide54.URL_SW_IWACALAS, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            String NameSpaceWS_IWACALAS = ConfigurationParameter.getParameter(ConstantesMeLanbide54.NAMESPACE_SW_IWACALAS, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            String nameWS_IWACALAS = ConfigurationParameter.getParameter(ConstantesMeLanbide54.NAME_SW_IWACALAS, ConstantesMeLanbide54.FICHERO_PROPIEDADES);

            URL url_IWACALAS = null;
            try {
                url_IWACALAS = new URL(urlWS_IWACALAS);
            } catch (MalformedURLException ex) {
                java.util.logging.Logger.getLogger(TramitacionAutomaticaAgenciasAACCJob.class.getName()).log(Level.SEVERE, null, ex);
            }
            QName qName_IWACALAS = new QName(NameSpaceWS_IWACALAS, nameWS_IWACALAS);

            IWACALAS_Service iwacalas = new IWACALAS_Service(url_IWACALAS, qName_IWACALAS);
            es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altaagencia.IESISPEPort ePort = iwacalas.getIESISPESOAP12Port();
            IWACALAS.AREACOMUNICACION areacomunicacionRequest = new IWACALAS.AREACOMUNICACION();
            IWACALAS.AREACOMUNICACION.ADCAREACONTROL adcareacontrol = new IWACALAS.AREACOMUNICACION.ADCAREACONTROL();
            IWACALAS.AREACOMUNICACION.ADEDATOSENTRADA adedatosentrada = new IWACALAS.AREACOMUNICACION.ADEDATOSENTRADA();
            IWACALAS.AREACOMUNICACION.ADSDATOSSALIDA adsdatossalida = new IWACALAS.AREACOMUNICACION.ADSDATOSSALIDA();

            adcareacontrol.setADCPROGPRINC(nameWS_IWACALAS);
            adcareacontrol.setADCCANALORIGEN("S");

            String usuarioSW_SEPE = ConfigurationParameter.getParameter(ConstantesMeLanbide54.USUARIO_SWSEPE, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            adedatosentrada.setADEUSUARIO(usuarioSW_SEPE);
            adedatosentrada.setADENIFA(tercero.getDoc());
            adedatosentrada.setADEDENOMINACION(tercero.getNombre());
            //Acceder a la tabla GEN_PROVINCIAS, por el código de provincia y se recoge el código de comunidad autónoma.
            String codProvincia = String.format("%02d", tercero.getDomicilio().getCodProvincia());
            String comunidadAut = MeLanbide54DAO.getInstance().BuscarComunidadAut(con, codProvincia);
            if (comunidadAut != null) {
                adedatosentrada.setADEIDCOAUTA(comunidadAut);
            }

            //Obtener fecha actual            
            Date ahora = new Date();
            SimpleDateFormat formateador = new SimpleDateFormat("yyyyMMdd");
            adedatosentrada.setADEFXAUTOZA(formateador.format(ahora));
            String tipoVia = convertirTipoVia(tercero.getDomicilio().getTipoVia());
            if (tipoVia != null) {
                adedatosentrada.setADETIPVIA(tipoVia);
            }
            if (tercero.getDomicilio().getNombreVia() != null) {
                adedatosentrada.setADENOMVIA(tercero.getDomicilio().getNombreVia());
            }
            adedatosentrada.setADENUMERO(String.valueOf(tercero.getDomicilio().getPrimerNumero()));
            //adedatosentrada.setADEBISDUP("");            
            adedatosentrada.setADEESCALERA(tercero.getDomicilio().getEscalera());
            adedatosentrada.setADEPISO(tercero.getDomicilio().getPlanta());
            adedatosentrada.setADELETRAPUERTA(tercero.getDomicilio().getPuerta());
            BigDecimal muni = new BigDecimal(String.valueOf(tercero.getDomicilio().getCodProvincia()) + String.format("%03d", tercero.getDomicilio().getCodMunicipio()));
            adedatosentrada.setADEMUNICIPIO(muni);
            BigDecimal codpostal = new BigDecimal(tercero.getDomicilio().getCodPostal());
            adedatosentrada.setADECODPOSTAL(codpostal);
            String telefono = "";
            String fax = "";
            if (tercero.getTelefono() != null) {
                if (tercero.getTelefono().contains("/")) {
                    String[] partes = tercero.getTelefono().split("/");
                    telefono = ((partes[0].trim()).replace(".", "")).replace("-", "");
                    fax = ((partes[1].trim()).replace(".", "")).replace("-", "");
                } else {
                    telefono = ((tercero.getTelefono().replace(".", "")).replace("-", "")).substring(0, 9);
                }
            } 
            adedatosentrada.setADETELEFONO(telefono);
            adedatosentrada.setADEFAX(fax);
            if (tercero.getEmail() != null) {
                adedatosentrada.setADEMAIL(tercero.getEmail());     //email del tercero            
            } 

            codCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide54.CAMPO_PAGWEB, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            nombreTabla = ConfigurationParameter.getParameter(ConstantesMeLanbide54.TABLA_CAMPOS_TEXTO, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            valorCampo = MeLanbide54DAO.getInstance().buscarValorCampoSuplementario(con, codOrg, numExpediente, codCampo, nombreTabla);
            adedatosentrada.setADEPAGINAWEB(valorCampo);
            adedatosentrada.setADESWMAIL(BigDecimal.ONE);
            adedatosentrada.setADESWSMS(BigDecimal.ZERO);
            adedatosentrada.setADECODEXPED(numExpediente);

            adedatosentrada.setADEFXRESOLUCION(formateador.format(ahora));

            codCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide54.CAMPO_ACTREALIZ, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            nombreTabla = ConfigurationParameter.getParameter(ConstantesMeLanbide54.TABLA_CAMPOS_DESPLEGABLES, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            valorCampo = MeLanbide54DAO.getInstance().buscarValorCampoSuplementario(con, codOrg, numExpediente, codCampo, nombreTabla);
            adedatosentrada.setADETIPAGENCIA(valorCampo);

            codCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide54.CAMPO_CORREOWEB, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            nombreTabla = ConfigurationParameter.getParameter(ConstantesMeLanbide54.TABLA_CAMPOS_TEXTO, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            valorCampo = MeLanbide54DAO.getInstance().buscarValorCampoSuplementario(con, codOrg, numExpediente, codCampo, nombreTabla);
            log.error("como correo2 devuelve: "+valorCampo);
            if (valorCampo != null) {
                adedatosentrada.setADEMAIL2(valorCampo);
            } 
           
            codCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide54.CAMPO_ETT, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            nombreTabla = ConfigurationParameter.getParameter(ConstantesMeLanbide54.TABLA_CAMPOS_DESPLEGABLES, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            valorCampo = MeLanbide54DAO.getInstance().buscarValorCampoSuplementario(con, codOrg, numExpediente, codCampo, nombreTabla);
            adedatosentrada.setADEINETT(valorCampo);
            // La parte de salida no hace falta, sera lo que devuelve el WS
            areacomunicacionRequest.setADCODCONTROLENTIREX(BigDecimal.ZERO);
            areacomunicacionRequest.setADCAREACONTROL(adcareacontrol);
            areacomunicacionRequest.setADEDATOSENTRADA(adedatosentrada);
            areacomunicacionRequest.setADSDATOSSALIDA(adsdatossalida);
//---------------------
            log.error("ADC-PROG-PRINC: " + adcareacontrol.getADCPROGPRINC());
            log.error("ADC-CANAL-ORIGEN: " + adcareacontrol.getADCCANALORIGEN());
            log.error("ADE-USUARIO: " + adedatosentrada.getADEUSUARIO());
            log.error("ADE-NIF-A: " + adedatosentrada.getADENIFA());
            log.error("ADE-DENOMINACION: " + adedatosentrada.getADEDENOMINACION());
            log.error("ADE-ID-COAUT-A: " + adedatosentrada.getADEIDCOAUTA());
            log.error("ADE-FX-AUTOZ-A: " + adedatosentrada.getADEFXAUTOZA());
            log.error("ADE-TIP-VIA: " + adedatosentrada.getADETIPVIA());
            log.error("ADE-NOM-VIA: " + adedatosentrada.getADENOMVIA());
            log.error("ADE-NUMERO: " + adedatosentrada.getADENUMERO());
            log.error("ADE-BIS-DUP: " + adedatosentrada.getADEBISDUP());
            log.error("ADE-ESCALERA: " + adedatosentrada.getADEESCALERA());
            log.error("ADE-PISO: " + adedatosentrada.getADEPISO());
            log.error("ADE-LETRA-PUERTA: " + adedatosentrada.getADELETRAPUERTA());
            log.error("ADE-MUNICIPIO: " + adedatosentrada.getADEMUNICIPIO());
            log.error("ADE-COD-POSTAL: " + adedatosentrada.getADECODPOSTAL());
            log.error("ADE-TELEFONO: " + adedatosentrada.getADETELEFONO());
            log.error("ADE-FAX: " + adedatosentrada.getADEFAX());
            log.error("ADE-MAIL: " + adedatosentrada.getADEMAIL());
            log.error("ADE-PAGINA-WEB: " + adedatosentrada.getADEPAGINAWEB());
            log.error("ADE-SW-MAIL: " + adedatosentrada.getADESWMAIL());
            log.error("ADE-SW-SMS: " + adedatosentrada.getADESWSMS());
            log.error("ADE-COD-EXPED: " + adedatosentrada.getADECODEXPED());
            log.error("ADE-FX-RESOLUCION: " + adedatosentrada.getADEFXRESOLUCION());
            log.error("ADE-TIP-AGENCIA: " + adedatosentrada.getADETIPAGENCIA());
            log.error("ADE-IN-ETT: " + adedatosentrada.getADEINETT());
            log.error("ADE-MAIL2: " + adedatosentrada.getADEMAIL2());
//---------------------

            areacomunicacionResponseA = ePort.iwacalas(areacomunicacionRequest);
            //Si ha dado de alta la agencia correctamente--> ADC-COD-RETORNO = 0;      
            String codErrorIWACALAS = areacomunicacionResponseA.getADCAREACONTROL().getADCCODERROR();
            String codRetornoIWACALAS = areacomunicacionResponseA.getADCAREACONTROL().getADCCODRETORNO();

            if (("".equals(codErrorIWACALAS)) && ("0".equals(codRetornoIWACALAS))) {
                //Grabar en la tabla --grabado OK a SW Alta Agencias
                registroBatch.setResultado("OK");
                registroBatch.setObservaciones("Agencia de colocación dada de alta correctamente. Nº Agencia: " + areacomunicacionResponseA.getADSDATOSSALIDA().getADSCODCOLOCACION());
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
            } else {
                registroBatch.setResultado("KO");
                registroBatch.setObservaciones("Error en el SW Alta Agencia de colocación con el CIF " + areacomunicacionResponseA.getADEDATOSENTRADA().getADENIFA() + " .Error: " + areacomunicacionResponseA.getADCAREACONTROL().getADCLITERROR());
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                //Grabar error 
                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AACC_012");
                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW Alta Agencia, en el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum() + " .Error: " + areacomunicacionResponseA.getADCAREACONTROL().getADCLITERROR());
                err.setSituacion("JobExecute");
                String causa = "";
                String error = "";
                grabarError(err, error, causa, numExpediente);
            }
            return areacomunicacionResponseA;

        } catch (Exception ex) {
            registroBatch.setResultado("KO");
            registroBatch.setObservaciones("Excepción en SW Alta Agencia");
            try {
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
            } catch (Exception ex1) {
                log.error("Error en el sW Alta Agencia ", ex1);
                return null;
            }
            ErrorBean err = new ErrorBean();
            err.setIdError("TRAMITACION_AACC_012");
            err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW Alta Agencia, en el expediente " + numExpediente + " y el registro" + registro.getResEje() + "/" + registro.getResNum());
            err.setSituacion("JobExecute");
            String causa = ex.getCause().getMessage();
            String error = ex.getMessage();
            grabarError(err, error, causa, numExpediente);
            return null;
        }
    }

    public IWACACTSResponse.AREACOMUNICACION llamadaSWAltaCentro(Connection con, int codOrg, CentroVO centro, String numExpediente, RegistroAACCVO registro) {

        log.debug("begin llamadaSWAltaCentro()");
        String codCampo = "";
        String valorCampo = "";
        String nombreTabla = "";

        RegistroBatchVO registroBatch = new RegistroBatchVO();

        registroBatch.setEjerRegistro(registro.getResEje());
        registroBatch.setNumRegistro(registro.getResNum());
        registroBatch.setNumExpediente(numExpediente);
        registroBatch.setCodTramite(Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide54.COMUNICACION_NUMERO_DE_AGENCIA_ALTA_CENTRO, ConstantesMeLanbide54.FICHERO_PROPIEDADES)));
        registroBatch.setOperacion("llamadaSWAltaCentro");
        registroBatch.setRelanzar(0);
        registroBatch.setCodOperacion(OP_SW_ALTA_CENTRO);
        registroBatch.setObservaciones("");

        try {
            IWACACTSResponse.AREACOMUNICACION areacomunicacionResponseCT = null;

            String urlWS_IWACACTS = ConfigurationParameter.getParameter(ConstantesMeLanbide54.URL_SW_IWACACTS, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            String NameSpaceWS_IWACACTS = ConfigurationParameter.getParameter(ConstantesMeLanbide54.NAMESPACE_SW_IWACACTS, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            String nameWS_IWACACTS = ConfigurationParameter.getParameter(ConstantesMeLanbide54.NAME_SW_IWACACTS, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            log.debug("Define la URL");
            URL url_IWACACTS = null;
            try {
                url_IWACACTS = new URL(urlWS_IWACACTS);
            } catch (MalformedURLException ex) {
                java.util.logging.Logger.getLogger(TramitacionAutomaticaAgenciasAACCJob.class.getName()).log(Level.SEVERE, null, ex);
            }
            QName qNameIWACACTS = new QName(NameSpaceWS_IWACACTS, nameWS_IWACACTS);
            IWACACTS_Service iwacacts = new IWACACTS_Service(url_IWACACTS, qNameIWACACTS);
            es.altia.flexia.integracion.moduloexterno.melanbide54.cliente.altacentro.IESISPEPort ePort = iwacacts.getIESISPESOAP12Port();

            log.debug("Llama Metodos IWACACTS");
            IWACACTS.AREACOMUNICACION areacomunicacionRequest = new IWACACTS.AREACOMUNICACION();
            IWACACTS.AREACOMUNICACION.ADCAREACONTROL adcareacontrol = new IWACACTS.AREACOMUNICACION.ADCAREACONTROL();
            IWACACTS.AREACOMUNICACION.ADEDATOSENTRADA adedatosentrada = new IWACACTS.AREACOMUNICACION.ADEDATOSENTRADA();
            IWACACTS.AREACOMUNICACION.ADEDATOSENTRADA.ADEDATOSCENTRO adedatoscentro = new IWACACTS.AREACOMUNICACION.ADEDATOSENTRADA.ADEDATOSCENTRO();
            IWACACTS.AREACOMUNICACION.ADSDATOSSALIDA adedatossalida = new IWACACTS.AREACOMUNICACION.ADSDATOSSALIDA();

            //Datos Control
            log.debug("--- Datos control");
            adcareacontrol.setADCPROGPRINC(nameWS_IWACACTS);
            adcareacontrol.setADCCANALORIGEN("S");

            // Datos Entrada
            log.debug("--- Datos Entrada");
            adedatosentrada.setADEUSUARIO(ConfigurationParameter.getParameter(ConstantesMeLanbide54.USUARIO_SWSEPE, ConstantesMeLanbide54.FICHERO_PROPIEDADES));
            log.debug("Usuario: " + adedatosentrada.getADEUSUARIO());
            adedatosentrada.setADENUCENTRO(centro.getId());
            log.debug("Id Centro: " + adedatosentrada.getADENUCENTRO());
            // Acceder a la tabla GEN_PROVINCIAS, por el código de provincia y se recoge el código de comunidad autónoma.
            String coaut = MeLanbide54DAO.getInstance().BuscarComunidadAut(con, centro.getCodTH());
            if (coaut != null) {
                adedatosentrada.setADEIDCOAUT(coaut);
            }

            codCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide54.CAMPO_COD_AGENCIA, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            nombreTabla = ConfigurationParameter.getParameter(ConstantesMeLanbide54.TABLA_CAMPOS_TEXTO, ConstantesMeLanbide54.FICHERO_PROPIEDADES);
            String codigoAgencia = MeLanbide54DAO.getInstance().buscarValorCampoSuplementario(con, codOrg, numExpediente, codCampo, nombreTabla);
            log.debug("Codigo Agencia recuperado: " + codigoAgencia);
            adedatosentrada.setADECOAGENCA(codigoAgencia);

            adedatosentrada.setADETIPOPE("A");// tipo de operacion A= Alta
            // 
            String th = centro.getCodTH();
            if (th.length() < 2) {
                th = "0" + th;
            }
            String muni = centro.getCodMun();
            if (muni.length() < 2) {
                muni = "00" + muni;
            } else if (muni.length() < 3) {
                muni = "0" + muni;
            }
            String municipio = th + muni;
            adedatoscentro.setADEMUNICENTRO(municipio);

            adedatoscentro.setADETELCENTRO(centro.getTlef());
            String domicilio = (centro.getCalle() + " nº " + centro.getPortal() + " " + centro.getPiso() + " - " + centro.getLetra());
            adedatoscentro.setADEDOMICCENTRO(domicilio);
            adedatoscentro.setADECPOSTCENTRO(centro.getCp());
            adedatoscentro.setADEEMAILCENTRO(centro.getEmail());
            adedatoscentro.setADEOBSCENTRO(valorCampo);
            // Insertamos los datos de entrada en el centro
            adedatosentrada.setADEDATOSCENTRO(adedatoscentro);
            // La parte de salida no hace falta, sera lo que devuelve el WS
            areacomunicacionRequest.setADCODCONTROLENTIREX(BigDecimal.ZERO);
            areacomunicacionRequest.setADCAREACONTROL(adcareacontrol);
            areacomunicacionRequest.setADEDATOSENTRADA(adedatosentrada);
            areacomunicacionRequest.setADSDATOSSALIDA(adedatossalida);
// -----------------------------------            
            log.error(" --  DATOS ALTA  --");
            log.error("  --  Control  --");
            log.error("ADC-PROG-PRINC - " + adcareacontrol.getADCPROGPRINC());
            log.error("ADC-CANAL-ORIGEN - " + adcareacontrol.getADCCANALORIGEN());
            log.error("  --  Entrada  --");
            log.error("ADE-USUARIO - " + adedatosentrada.getADEUSUARIO());
            log.error("ADE-ID-COAUT - " + adedatosentrada.getADEIDCOAUT());
            log.error("ADE-CO-AGENC-A - " + adedatosentrada.getADECOAGENCA());
            log.error("ADE-TIP-OPE - " + adedatosentrada.getADETIPOPE());
            log.error("ADE-MUNI-CENTRO - " + adedatosentrada.getADEDATOSCENTRO().getADEMUNICENTRO());
            log.error("ADE-TEL-CENTRO - " + adedatosentrada.getADEDATOSCENTRO().getADETELCENTRO());
            log.error("ADE-DOMIC-CENTRO - " + adedatosentrada.getADEDATOSCENTRO().getADEDOMICCENTRO());
            log.error("ADE-CPOST-CENTRO - " + adedatosentrada.getADEDATOSCENTRO().getADECPOSTCENTRO());
            log.error("ADE-EMAIL-CENTRO - " + adedatosentrada.getADEDATOSCENTRO().getADEEMAILCENTRO());
            log.error("ADE-OBS-CENTRO - " + adedatosentrada.getADEDATOSCENTRO().getADEOBSCENTRO());
//  -------------------------------

            areacomunicacionResponseCT = ePort.iwacacts(areacomunicacionRequest);
            // Se se ha dado de alta el centro correctamente ADC-COD_RETORNO = 0
            String codRetIWACACTS = areacomunicacionResponseCT.getADCAREACONTROL().getADCCODRETORNO();
            log.debug("Codigo retorno: " + codRetIWACACTS);
            String codErrIWACACTS = areacomunicacionResponseCT.getADCAREACONTROL().getADCCODERROR();

            if (("".equals(codErrIWACACTS)) && ("0".equals(codRetIWACACTS))) {
                // Grabar en la tabla grabado OK a SW Alta Centros
                registroBatch.setResultado("OK");
                registroBatch.setObservaciones("Centro de Trabajo nº. " + areacomunicacionResponseCT.getADSDATOSSALIDA().getADSNUCENTRO() + " dado de alta correctamente.");
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                log.debug("Centro de Trabajo nº. " + areacomunicacionResponseCT.getADSDATOSSALIDA().getADSNUCENTRO() + " dado de alta correctamente. ");
            } else {
                registroBatch.setResultado("KO");
                registroBatch.setObservaciones("Error en el SW Alta Centro de Trabajo de la agencia nº " + adedatosentrada.getADECOAGENCA() + ". Error: " + areacomunicacionResponseCT.getADCAREACONTROL().getADCLITERROR());
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
                log.error("Error en el SW Alta Centro de Trabajo de la agencia nº " + adedatosentrada.getADECOAGENCA() + ". Error: " + areacomunicacionResponseCT.getADCAREACONTROL().getADCLITERROR());

                // Grabar error
                ErrorBean err = new ErrorBean();
                err.setIdError("TRAMITACION_AACC_013");
                err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW Alta Centro de Trabajo, en el expediente " + numExpediente + " y el registro " + registro.getResEje() + "/" + registro.getResNum() + " .Error: " + areacomunicacionResponseCT.getADCAREACONTROL().getADCLITERROR());
                err.setSituacion("JobExecute");
                String causa = "";
                String error = "";
                grabarError(err, error, causa, numExpediente);
            }
            return areacomunicacionResponseCT;

        } catch (Exception ex) {
            registroBatch.setResultado("KO");
            registroBatch.setObservaciones("Error en el SW Alta Centro de Trabajo");
            try {
                MeLanbide54DAO.getInstance().InsertarRegistroBatch(con, registroBatch);
            } catch (Exception exAlCt) {
                log.error("Error en el SW Alta de Centro de Trbajo");
                return null;
            }
            ErrorBean err = new ErrorBean();
            err.setIdError("TRAMITACION_AACC_013");
            err.setMensajeError("Error en el job de Tramitación automática de agencias AACC. Error al llamar al SW Alta Centro de Trabajo, en el expediente " + numExpediente + " y el registro " + registro.getResEje() + "/" + registro.getResNum());
            err.setSituacion("JobExecute");
            String causa = ex.getCause().getMessage();
            String error = ex.getMessage();
            grabarError(err, error, causa, numExpediente);
            return null;
        }
    }

    public String convertirTipoVia(int tipoVia) {
        String tipVia = "";

        switch (tipoVia) {
            case 81:
                tipVia = "AQ";
                break;
            case 82:
                tipVia = "AC";
                break;
            case 83:
                tipVia = "AL";
                break;
            case 1:
            case 3:
            case 85:
                tipVia = "AD";
                break;
            case 89:
                tipVia = "AN";
                break;
            case 4:
            case 99:
                tipVia = "AP";
                break;
            case 95:
                tipVia = "AT";
                break;
            case 7:
            case 104:
                tipVia = "AV";
                break;
            case 9:
            case 113:
                tipVia = "BA";
                break;
            case 12:
            case 115:
                tipVia = "BC";
                break;
            case 106:
                tipVia = "BD";
                break;
            case 11:
            case 108:
                tipVia = "BO";
                break;
            case 10:
            case 114:
                tipVia = "BL";
                break;
            case 18:
            case 124:
                tipVia = "CL";
                break;
            case 17:
            case 144:
                tipVia = "CA";
                break;
            case 145:
                tipVia = "CJ";
                break;
            case 141:
                tipVia = "CE";
                break;
            case 174:
            case 175:
                tipVia = "CZ";
                break;
            case 73:
            case 148:
            case 149:
                tipVia = "CM";
                break;
            case 154:
                tipVia = "CR";
                break;
            case 39:
            case 169:
                tipVia = "CT";
                break;
            case 40:
            case 167:
                tipVia = "CS";
                break;
            case 15:
                tipVia = "CH";
                break;
            case 74:
            case 150:
                tipVia = "CO";
                break;
            case 163:
                tipVia = "KO";
                break;
            case 168:
                tipVia = "CN";
                break;
            case 41:
            case 171:
                tipVia = "CU";
                break;
            case 22:
                tipVia = "ED";
                break;
            case 186:
                tipVia = "ES";
                break;
            case 26:
            case 187:
                tipVia = "EL";
                break;
            case 189:
                tipVia = "ET";
                break;
            case 30:
            case 203:
                tipVia = "GL";
                break;
            case 31:
            case 202:
                tipVia = "GR";
                break;
            case 34:
            case 213:
                tipVia = "JA";
                break;
            case 36:
            case 230:
                tipVia = "LG";
                break;
            case 59:
                tipVia = "MZ";
                break;
            case 76:
                tipVia = "MC";
                break;
            case 58:
            case 236:
                tipVia = "MO";
                break;
            case 55:
            case 237:
                tipVia = "MU";
                break;
            case 56:
                tipVia = "MN";
                break;
            case 240:
            case 241:
            case 268:
                tipVia = "PQ";
                break;
            case 242:
                tipVia = "PC";
                break;
            case 43:
            case 277:
                tipVia = "PM";
                break;
            case 280:
                tipVia = "PD";
                break;
            case 45:
            case 275:
                tipVia = "PJ";
                break;
            case 49:
            case 247:
                tipVia = "PP";
                break;
            case 261:
                tipVia = "PI";
                break;
            case 260:
                tipVia = "PL";
                break;
            case 51:
            case 259:
                tipVia = "PZ";
                break;
            case 281:
                tipVia = "PE";
                break;
            case 262:
                tipVia = "PU";
                break;
            case 60:
            case 248:
                tipVia = "PB";
                break;
            case 44:
            case 264:
                tipVia = "PG";
                break;
            case 267:
                tipVia = "PR";
                break;
            case 48:
                tipVia = "PN";
                break;
            case 78:
            case 285:
                tipVia = "RA";
                break;
            case 53:
            case 289:
                tipVia = "RB";
                break;
            case 61:
            case 286:
                tipVia = "RP";
                break;
            case 290:
                tipVia = "RR";
                break;
            case 62:
            case 294:
                tipVia = "RE";
                break;
            case 292:
                tipVia = "RN";
                break;
            case 54:
            case 291:
                tipVia = "RC";
                break;
            case 77:
            case 295:
                tipVia = "RD";
                break;
            case 303:
            case 306:
                tipVia = "SC";
                break;
            case 65:
            case 308:
                tipVia = "SD";
                break;
            case 307:
                tipVia = "SR";
                break;
            case 68:
            case 301:
                tipVia = "SU";
                break;
            case 70:
                tipVia = "TT";
                break;
            case 318:
                tipVia = "TL";
                break;
            case 313:
            case 314:
                tipVia = "TS";
                break;
            case 71:
            case 317:
                tipVia = "TR";
                break;
            case 72:
            case 319:
            case 321:
                tipVia = "UR";
                break;
            case 327:
                tipVia = "VI";
                break;
            case 333:
                tipVia = "ZO";
            default:
                tipVia = "";
                break;
        }
        return tipVia;
    }
}
