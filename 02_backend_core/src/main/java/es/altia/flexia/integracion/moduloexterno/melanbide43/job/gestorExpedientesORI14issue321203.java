/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.MeLanbide43BDUtil;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpTram;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Aportacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6AportacionDoc;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Expediente;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Interesado;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Participacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Tramite;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.servicios.Lan6InformarConsultaServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 *
 * @author INGDGC
 */

public class gestorExpedientesORI14issue321203 implements Job{
    
    protected static Config conf =ConfigServiceHelper.getConfig("notificaciones");    
    
    private Logger log = LogManager.getLogger(gestorExpedientesORI14issue321203.class);
    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        try {
            int contador = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.error("jec.getRefireCount(): " + contador);
            Trigger trigger = jec.getTrigger();

            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);

            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.error("servidor: " + servidor);
            if (servidor.equals(System.getProperty("weblogic.Name"))) {//PARA LOCAL QUITAR
                synchronized (jec) {
                    
                    Connection con = null;
                    boolean dev = false;
                    int numIntentos = 0;
                    String numExpediente = "";
                    int id = 0;
                    String[] params = new String[]{"ORACLE"};
                    try {
                        log.error("Execute lanzado " + System.getProperty("weblogic.Name"));

                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG", ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS",
                                ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                        while (codOrg < 2) {
                            MeLanbide43BDUtil meLanbide43BDUtil = new MeLanbide43BDUtil();
                            AdaptadorSQLBD adaptador = meLanbide43BDUtil.getAdaptSQLBD(String.valueOf(codOrg));
                            con = adaptador.getConnection();
                            log.error("en el while de tokens codOrg: " + codOrg);
                            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
                            
                            
                            List<ExpTram> expedientes = this.getExpedientesCorrecionORI14(con);
                                log.error("numexpedientes:" + expedientes.size());

                            for (Iterator<ExpTram> i = expedientes.iterator(); i.hasNext();) {
                                ExpTram item = i.next();
                                numExpediente = item.getNumExpediente();
                                log.error("numexpediente:" + numExpediente);
                                String[] datos = numExpediente.split("/");
                                //LAMAR A MIS GESTIONES INICIO                            
                                MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();

                                Expediente exp = meLanbide43Manager.obtenerDatosExpedientes(numExpediente,adaptador);
                                Participantes par = meLanbide43Manager.obtenerDatosParticipantes(numExpediente,adaptador);
                                
                                log.error("PARTICIPANTES: " + par);
                                //log.error("fecha: "+fecha); 
                                log.error("tipo id: " + par.getTipoID());
                                if (par.getTipoID() != 0) {

                                    Long codTramiteInicio = meLanbide43DAO.obtenerCodigoTramiteFase(datos[1], "1", con);
                                    log.error("codTramiteInicio: " + codTramiteInicio);
                                    Long codTramitacionCurso =  meLanbide43DAO.obtenerCodigoTramiteFase(datos[1], "2", con);
                                    log.error("codTramitacionCurso: " + codTramitacionCurso);
                                    Long codPagoTramitacion =  meLanbide43DAO.obtenerCodigoTramiteFase(datos[1], "3", con);
                                    log.error("codPagoTramitacion: " + codPagoTramitacion);
                                    Tramite datosTramiteInicio = getDatosTramiteFaseIssue321203(datos[1], String.valueOf(codTramiteInicio), con);
                                    log.error("datosTramiteInicio: " + datosTramiteInicio.getCod()+","+ datosTramiteInicio.getDescripcion()+","+ datosTramiteInicio.getDescripcionEu()+","+ datosTramiteInicio.getFase());
                                    Tramite datosTramitacionCurso =  getDatosTramiteFaseIssue321203(datos[1], String.valueOf(codTramitacionCurso), con);
                                    log.error("datosTramitacionCurso: " + datosTramitacionCurso.getCod()+","+ datosTramitacionCurso.getDescripcion()+","+ datosTramitacionCurso.getDescripcionEu()+","+ datosTramitacionCurso.getFase());
                                    Tramite datosPagoTramitacion =  getDatosTramiteFaseIssue321203(datos[1], String.valueOf(codPagoTramitacion), con);
                                    log.error("datosPagoTramitacion: " + datosPagoTramitacion.getCod()+","+ datosPagoTramitacion.getDescripcion()+","+ datosPagoTramitacion.getDescripcionEu()+","+ datosPagoTramitacion.getFase());
                                    
                                    // Punto 1 : Reiniciar expediente
                                    log.error(numExpediente + " - Paso 1 Alta Expediente/1a Aportacion Docs") ;
                                    String codigoOperacion = "";
                                    try {

//                                        String idProcedimiento = meLanbide43Manager.convierteProcedimiento(datos[1]);
                                        String idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(datos[1]); //convierteProcedimiento(codProcedimiento);

                                        log.error("Cod Procedimiento Recuperado Properties : -" + idProcedimiento);
                                        //Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA,
                                        //      "PROCEDIMIENTO_ID_" + "LAN62_ORI14");
                                        Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(datos[1]);
                                        log.error("Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(" + datos[1] + ");");

                                        Lan6Expediente lan6Expediente = new Lan6Expediente();
                                        lan6Expediente.setNumero(numExpediente);  //"2017/ORI14/00009"
                                        lan6Expediente.setEjercicio(datos[0]);  // "2017"

                                        // Fecha registro de Flexia
                                        Calendar fechaSolicitud = Calendar.getInstance();
                                        fechaSolicitud.setTime(exp.getFechaSolicitud());    // 2017, 8, 14
        
                                        lan6Expediente.setFechaSolicitud(fechaSolicitud);

                                        Calendar fechaApertura = Calendar.getInstance();
                                        fechaApertura.setTime(exp.getFechaApertura());        //2017, 8, 26
                                        lan6Expediente.setFechaApertura(fechaApertura);
                                        log.error("expediente");
                                        log.error("lan6Expediente.setNumero(numExpediente);  \n" + numExpediente
                                                + " lan6Expediente.setEjercicio(datos[0]);\n" + datos[0]
                                                + " lan6Expediente.setFechaSolicitud(fechaSolicitud);\n" + fechaSolicitud
                                                + " lan6Expediente.setFechaApertura(fechaApertura); " + fechaApertura); 

                                        log.error("participacion");
                                        // Participacion
                                        Lan6Participacion lan6participacion = new Lan6Participacion();
                                        lan6participacion.setCanalNotificacion(Lan6Constantes.NOTIF_ELECTRONICA);
                                        lan6participacion.setIdioma(Lan6Constantes.IDIOMA_ES);
                                        List<String> tfnosAvisos = new ArrayList<String>();
                                        tfnosAvisos.add(par.getTlf()); //943323232
                                        lan6participacion.setTfnosAvisos(tfnosAvisos);

                                        List<String> mailsAvisos = new ArrayList<String>();
                                        mailsAvisos.add(par.getMail());    // m.ferreira@prueba.com
                                        lan6participacion.setMailsAvisos(mailsAvisos);
                                        log.error("participacion");
                                        log.error("par.getTlf()" + par.getTlf());
                                        log.error("par.getMail()" + par.getMail());
                                        log.error("interesado");

                                        // Interesados
                                        List<Lan6Interesado> interesados = new ArrayList<Lan6Interesado>();
                                        Lan6Interesado lan6Interesado = new Lan6Interesado();
                                        lan6Interesado.setNumIdentificacion(par.getNif()); //"99999990S"
                                        
                                        if (par.getApe1() == null || par.getApe1().isEmpty() || par.getTipoID() == 4 || par.getTipoID() == 5) {
                                            log.debug("PERSONA_JURIDICA: ");
                                            lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_CIF);
                                            lan6Interesado.setRazonSocial(par.getNombre());
                                        } else {
                                            log.debug("PERSONA_FISICA: ");
                                            lan6Interesado.setNombre(par.getNombre());
                                            lan6Interesado.setApellido1(par.getApe1());
                                            lan6Interesado.setApellido2(par.getApe2() != null ? par.getApe2() : "");
                                            switch (par.getTipoID()) {
                                                case 1:
                                                    lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_NIF);
                                                    break;
                                                case 2:
                                                    lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_PASAPORTE);
                                                    break;
                                                case 3:
                                                    lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_NIE);
                                                    break;
                                                case 8:
                                                    lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_OTRO);
                                                    break;
                                                case 9:
                                                    lan6Interesado.setTipoIdentificacion(Lan6Constantes.TIPO_IDENT_OTRO);
                                                    break;
                                                default:
                                                    log.error("El tipo " + par.getTipoID() + " no tiene correspondencia en PLATEA");
                                            }
                                        }
                                        /*
                                        lan6Interesado.setTipoIdentificacion(Lan6Constantes.PERSONA_FISICA);
                                        lan6Interesado.setNombre("CIUDADANO");
                                        lan6Interesado.setApellido1("FICTICIO");
                                        lan6Interesado.setApellido2("ACTIVO");
                                        */
                                        lan6Interesado.setTipo(Lan6Constantes.TIPO_INTERESADO_TITULAR);
                                        log.error("InteresadoDatos \n" +
                                        lan6Interesado.getNumIdentificacion() + " \n" +
                                        lan6Interesado.getTipoIdentificacion()+ " \n" +
                                        lan6Interesado.getRazonSocial()+ " \n" +
                                        lan6Interesado.getTipoIdentificacion()+ " \n" +
                                        lan6Interesado.getNombre()+ " \n" +
                                        lan6Interesado.getApellido1()+ " \n" +
                                        lan6Interesado.getApellido2()+ " \n" 
                                        );
                                        
                                                
                                        interesados.add(lan6Interesado);

                                        lan6participacion.setInteresados(interesados);
                                        lan6Expediente.setParticipacion(lan6participacion);

                                        ///// TRAMITES
                                        List<Lan6Tramite> listaTramites = new ArrayList<Lan6Tramite>();
                                        
                                        log.error("tramites ");

                                        // Primer tramite de apertura expediente
                                        Lan6Tramite lan6Tramite = new Lan6Tramite();

                                        lan6Tramite.setId(datosTramiteInicio.getCod());  //"1"
                                        lan6Tramite.setDescripcion(datosTramiteInicio.getDescripcion());  //"TRAMITACIÓN INICIADA"
                                        lan6Tramite.setDescripcionEu(datosTramiteInicio.getDescripcionEu());  //"TRAMITACIÓN INICIADA(EUSKERA)"
                                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ABRIR_EXPEDIENTE);
                                        
                                        Date feTemp = new Date();
                                        SimpleDateFormat sdf =new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");

                                        Calendar cal = Calendar.getInstance();
                                        cal = Calendar.getInstance();
                                        feTemp =getFechaGeneradoxExpteTipoOperTram(numExpediente,"I",(datosTramiteInicio.getCod()!=null?Integer.valueOf(datosTramiteInicio.getCod()):0),con);
                                        log.error("FechaActualizacion datosTramiteInicio:" + feTemp);
                                        cal.setTime(feTemp);//cal.set(2017, 8, 26);
                                        lan6Tramite.setFechaActualizacion(cal);

                                        listaTramites.add(lan6Tramite);

                                        lan6Tramite = new Lan6Tramite();
                                        lan6Tramite.setId(datosTramitacionCurso.getCod());        // "2"
                                        lan6Tramite.setDescripcion(datosTramitacionCurso.getDescripcion());                               // "TRAMITACIÓN EN CURSO"
                                        lan6Tramite.setDescripcionEu(datosTramitacionCurso.getDescripcionEu());                             // "TRAMITACIÓN EN CURSO(EUSKERA)"
                                        lan6Tramite.setTipoTramite("-");

                                        feTemp = new Date();
                                        cal = Calendar.getInstance();
                                        feTemp =getFechaGeneradoxExpteTipoOperTram(numExpediente,"A",(datosTramitacionCurso.getCod()!=null?Integer.parseInt(datosTramitacionCurso.getCod()):0),con);
                                        log.error("FechaActualizacion datosTramitacionCurso:" + feTemp);
                                        cal.setTime(feTemp);   //cal.set(2017, 9, 10);
                                        lan6Tramite.setFechaActualizacion(cal);

                                        listaTramites.add(lan6Tramite);

                                        lan6Tramite = new Lan6Tramite();
//                                        lan6Tramite.setId(Lan6Constantes.TRAMITE_APORTACION_DOC);
//                                        lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
//                                        lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                                        lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC"));
                                        lan6Tramite.setDescripcion(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC_DESC"));
                                        lan6Tramite.setDescripcionEu(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC_DESC_EU"));
                                        lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_APORTACION);

                                        feTemp = new Date();
                                        cal = Calendar.getInstance();
                                        // factorPrimeraAport : Determina si se devuelve solo la primera aportacion (1), las demas  (0) o Todas inclusive la 1a (null o vacio)
                                        List<Map<String,String>> listaDatos = getDatosGeneralesEntRegAsociaExptsAportDocumentos(numExpediente,"1","document",con);
                                        Map<String,String> mapaDatosPrimeraAportaDoc = new HashMap<String, String>();
                                        if(listaDatos!=null && listaDatos.size()>0)
                                            mapaDatosPrimeraAportaDoc=listaDatos.get(0);
                                        String fecha = mapaDatosPrimeraAportaDoc.get("RES_FEC");
                                        log.error("FechaActualizacion RES_FEC:" + fecha);
                                        if(fecha!=null && fecha!="")
                                            feTemp = sdf.parse(fecha);
                                        else
                                            log.error("No se ha podido recuperar la fecha de primera aportacion ponemos la actual ");
                                        log.error("FechaActualizacion PrimerAportacionDocumentacion:" + feTemp);
                                        cal.setTime(feTemp);//cal.set(2018, 2, 15); // Fecha de registro de la primera entrada asociada con aportacion documentacion  // 
                                        lan6Tramite.setFechaActualizacion(cal);

                                        listaTramites.add(lan6Tramite);

                                        lan6Expediente.setListaTramites(listaTramites);

                                        List<Lan6Aportacion> aportacionesCiudadano = new ArrayList<Lan6Aportacion>();
                                        List<Lan6AportacionDoc> documentos = new ArrayList<Lan6AportacionDoc>();
                                        
                                        //Documentos Aportados por la entrada
                                        if(listaDatos!=null && listaDatos.size()>0)
                                            documentos = getDocumentosEntradaRegistro(numExpediente,mapaDatosPrimeraAportaDoc.get("RES_EJE"),mapaDatosPrimeraAportaDoc.get("RES_NUM"),con);
                                        else
                                            log.error("No se ha podido recuperar aportaciones Paara el expediente, no intentamos recoger documentos");
                                        

                                        //APORTACION 15/03/2018
                                        // Documento 
                                        /*Lan6AportacionDoc lan6AportacionDoc = new Lan6AportacionDoc();
                                        lan6AportacionDoc.setOid("0901a0fe824a188c");
                                        lan6AportacionDoc.setNombre("Clausulas de confidencialidad.pdf");
                                        lan6AportacionDoc.setDescripcionRol("Clausulas de confidencialidad");
                                        lan6AportacionDoc.setTipo(Lan6Constantes.TIPO_DOC_ELECTRONICO);
                                        documentos.add(lan6AportacionDoc);
                                        */

                                        // // Aportacion
                                        Lan6Aportacion lan6Aportacion = new Lan6Aportacion();
                                        lan6Aportacion.setTipoAportacion(Lan6Constantes.TIPO_APORTACION_ATTACHMENT);
                                        lan6Aportacion.setIdAportacion(mapaDatosPrimeraAportaDoc.get("NUMREGGV"));  // "2018RTE00058255"
                                        lan6Aportacion.setNumRegistro(mapaDatosPrimeraAportaDoc.get("NUMREGGV"));           //"2018RTE00058255"
                                        cal = Calendar.getInstance();
                                        cal.setTime(feTemp);        //cal.set(2018, 02, 15);
                                        lan6Aportacion.setFechaRegistro(cal);

                                        lan6Aportacion.setDocumentos(documentos);

                                        cal = Calendar.getInstance();
                                        cal.setTime(feTemp);        //cal.set(2018, 02, 15);
                                        lan6Aportacion.setFechaAportacion(cal);

                                        aportacionesCiudadano.add(lan6Aportacion);

                                        lan6Expediente.setAportacionesCiudadano(aportacionesCiudadano);

                                        // Llamada metodo
                                        String salida = servicios.iniciarExpediente(lan6Expediente);
                                        log.error("Salida llamada al servicio iniciarExpediente Paso 1" + salida);
                                        // Paso 2 Dos Si ha ido bien el alta de la entrada
                                        // Registramos las aportaciones de tipo documento
                                        log.error(numExpediente + " - Paso 2 Aportaciones de Documentos ");
                                        int contadorTram = 1;
                                        try {
                                            
                                            // factorPrimeraAport : Determina si se devuelve solo la primera aportacion (1), las demas  (0) o Todas inclusive la 1a (null o vacio)
                                            List<Map<String, String>> listaDatosAportaciones = getDatosGeneralesEntRegAsociaExptsAportDocumentos(numExpediente, "0", "document", con);
                                            // Las segunda parte hay que trabajar con las demas aportaciones de docuementos
                                            for (Map<String, String> datosAportacion : listaDatosAportaciones) {

                                                lan6Expediente = new Lan6Expediente();
                                                lan6Expediente.setNumero(numExpediente);  //"2017/ORI14/00009"
                                                lan6Expediente.setEjercicio(datos[0]);

                                                contadorTram = contadorTram + 1;

                                                listaTramites = new ArrayList<Lan6Tramite>();
                                                lan6Tramite = new Lan6Tramite();

//                                                lan6Tramite.setId(Lan6Constantes.TRAMITE_APORTACION_DOC + contadorTram);
//                                                lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
//                                                lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                                                lan6Tramite.setId(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC") + contadorTram);
                                                lan6Tramite.setDescripcion(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC_DESC"));
                                                lan6Tramite.setDescripcionEu(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_APORTACION_DOC_DESC_EU"));
                                                lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_APORTACION);

                                                feTemp = new Date();
                                                cal = Calendar.getInstance();
                                                fecha = datosAportacion.get("RES_FEC");
                                                log.error("FechaActualizacion RES_FEC:" + fecha);
                                                if (fecha != null && fecha != "") {
                                                    feTemp = sdf.parse(fecha);
                                                } else {
                                                    log.error("No se ha podido recuperar la fecha de primera aportacion ponemos la actual ");
                                                }
                                                cal.setTime(feTemp);//cal.set(2018, 4, 04);
                                                lan6Tramite.setFechaActualizacion(cal);
                                                listaTramites.add(lan6Tramite);

                                                lan6Expediente.setListaTramites(listaTramites);

                                                aportacionesCiudadano = new ArrayList<Lan6Aportacion>();
                                                documentos = new ArrayList<Lan6AportacionDoc>();
                                                //Recogemos los documentos de la anotacion de regsitro que estamos tratando
                                                documentos = getDocumentosEntradaRegistro(numExpediente,datosAportacion.get("RES_EJE"),datosAportacion.get("RES_NUM"),con);
                                                
                                                /*
                                                //APORTACION 04/05/2018
                                                documentos = new ArrayList<Lan6AportacionDoc>();
                                                // Documento aportado
                                                Lan6AportacionDoc lan6AportacionDoc = new Lan6AportacionDoc();
                                                lan6AportacionDoc.setOid("0901a0fe8261f8c9");
                                                lan6AportacionDoc.setNombre("Declaración responsable inicio de acciones.pdf");
                                                lan6AportacionDoc.setDescripcionRol("Declaración responsable inicio de acciones");
                                                lan6AportacionDoc.setTipo(Lan6Constantes.TIPO_DOC_ELECTRONICO);
                                                documentos.add(lan6AportacionDoc);

                                                lan6AportacionDoc = new Lan6AportacionDoc();
                                                lan6AportacionDoc.setOid("0901a0fe8261f8d2");
                                                lan6AportacionDoc.setNombre("Aportación de documentos.html");
                                                lan6AportacionDoc.setDescripcionRol("Aportación de documentos");
                                                lan6AportacionDoc.setTipo(Lan6Constantes.TIPO_DOC_ELECTRONICO);
                                                documentos.add(lan6AportacionDoc);
                                                */

                                                // // Aportacion
                                                lan6Aportacion = new Lan6Aportacion();

                                                lan6Aportacion.setIdAportacion(datosAportacion.get("NUMREGGV"));  //"2018RTE00094831"
                                                lan6Aportacion.setNumRegistro(datosAportacion.get("NUMREGGV"));     // "2018RTE00094831"
                                                cal = Calendar.getInstance();
                                                cal.setTime(feTemp);        //cal.set(2018, 04, 05);
                                                lan6Aportacion.setFechaRegistro(cal);

                                                lan6Aportacion.setTipoAportacion(Lan6Constantes.TIPO_APORTACION_ATTACHMENT);

                                                lan6Aportacion.setDocumentos(documentos);
                                                cal = Calendar.getInstance();
                                                cal.setTime(feTemp);        //cal.set(2018, 04, 04);

                                                lan6Aportacion.setFechaAportacion(cal);
                                                lan6Aportacion.setIdEmisor(lan6Interesado.getNumIdentificacion());
                                                aportacionesCiudadano.add(lan6Aportacion);

                                                lan6Expediente.setAportacionesCiudadano(aportacionesCiudadano);
                                                
                                                
                                                //Llamada metodo       	
                                                salida = servicios.actualizarAportacionSolicitante(lan6Expediente);
                                                log.error("Salida llamada al servicio actualizarAportacionSolicitante paso 2 " + salida);
                                            }
                                            //reiniciamos contador para la siguiente fase
                                            contadorTram=0;
                                        } catch (Lan6Excepcion e) {
                                            log.error("Error al ejecutar el segundo Paso - Actualizacion de todas las aportaciones", e);
                                        }
                                        
                                        // Fase tres y tres.uno  3 y 3.1  Cambios de aviso y cambio de notificaciones
                                        // Recorremos las entras que sean de tipo Cambios de aviso y cambio de x notificaciones segun el asunto
                                        log.error(numExpediente + " - Paso 3 Aportaciones como cambios de Aviso");
                                        try {
                                            List<Map<String, String>> listaDatosAportacionesAvisos = getDatosGeneralesEntRegAsociaExptsAportDocumentos(numExpediente, "", "avisos", con);
                                            for (Map<String, String> datosAportacion : listaDatosAportacionesAvisos) {
                                                contadorTram = contadorTram + 1;

                                                lan6Expediente = new Lan6Expediente();
                                                lan6Expediente.setNumero(numExpediente);  //"2017/ORI14/00009"
                                                lan6Expediente.setEjercicio(datos[0]);

                                                listaTramites = new ArrayList<Lan6Tramite>();
                                                lan6Tramite = new Lan6Tramite();

//                                                lan6Tramite.setId((contadorTram> 1 ? Lan6Constantes.TRAMITE_CAMBIO_AVISOS + contadorTram : Lan6Constantes.TRAMITE_CAMBIO_AVISOS));
//                                                lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_CAMBIO_AVISOS_DESC);
//                                                lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_CAMBIO_AVISOS_DESC);
                                                lan6Tramite.setId(contadorTram > 1 ? gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_CAMBIO_AVISOS") + contadorTram : gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_CAMBIO_AVISOS"));
                                                lan6Tramite.setDescripcion(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_CAMBIO_AVISOS_DESC"));
                                                lan6Tramite.setDescripcionEu(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_CAMBIO_AVISOS_DESC_EU"));
                                                lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_APORTACION);

                                                feTemp = new Date();
                                                cal = Calendar.getInstance();
                                                fecha = datosAportacion.get("RES_FEC");
                                                log.error("FechaActualizacion RES_FEC:" + fecha);
                                                if (fecha != null && fecha != "") {
                                                    feTemp = sdf.parse(fecha);
                                                } else {
                                                    log.error("No se ha podido recuperar la fecha de primera aportacion ponemos la actual ");
                                                }
                                                cal.setTime(feTemp);        //cal.set(2018, 4, 06);
                                                lan6Tramite.setFechaActualizacion(cal);

                                                listaTramites.add(lan6Tramite);

                                                lan6Expediente.setListaTramites(listaTramites);

                                                aportacionesCiudadano = new ArrayList<Lan6Aportacion>();
                                                documentos = new ArrayList<Lan6AportacionDoc>();
                                                
                                                //Recogemos los documentos de la anotacion de regsitro que estamos tratando
                                                documentos = getDocumentosEntradaRegistro(numExpediente, datosAportacion.get("RES_EJE"), datosAportacion.get("RES_NUM"), con);
                                                
                                                /*
                                                //APORTACION 04/05/2018
                                                documentos = new ArrayList<Lan6AportacionDoc>();
                                                // Documento aportado
                                                Lan6AportacionDoc lan6AportacionDoc = new Lan6AportacionDoc();
                                                lan6AportacionDoc.setOid("0901a0fe8261f8c9");
                                                lan6AportacionDoc.setNombre("Datos de aviso.html");
                                                lan6AportacionDoc.setDescripcionRol("Datos de aviso");
                                                lan6AportacionDoc.setTipo(Lan6Constantes.TIPO_DOC_ELECTRONICO);
                                                documentos.add(lan6AportacionDoc);
                                                */

                                                // // Aportacion
                                                lan6Aportacion = new Lan6Aportacion();

                                                lan6Aportacion.setIdAportacion(datosAportacion.get("NUMREGGV"));    //"2018RTE00094831"
                                                lan6Aportacion.setNumRegistro(datosAportacion.get("NUMREGGV"));        //2018RTE00094831")
                                                cal = Calendar.getInstance();
                                                cal.setTime(feTemp);        //cal.set(2018, 04, 05);
                                                lan6Aportacion.setFechaRegistro(cal);

                                                lan6Aportacion.setTipoAportacion(Lan6Constantes.TIPO_APORTACION_ATTACHMENT);

                                                lan6Aportacion.setDocumentos(documentos);
                                                cal = Calendar.getInstance();
                                                cal.setTime(feTemp);        //cal.set(2018, 04, 06);

                                                lan6Aportacion.setFechaAportacion(cal);
                                                lan6Aportacion.setIdEmisor(lan6Interesado.getNumIdentificacion());
                                                aportacionesCiudadano.add(lan6Aportacion);

                                                lan6Expediente.setAportacionesCiudadano(aportacionesCiudadano);

                                                //Llamada metodo       	
                                                salida=servicios.actualizarAportacionSolicitante(lan6Expediente);
                                                log.error("Salida llamada al servicio actualizarAportacionSolicitante paso 3 " + salida);
                                                
                                            }
                                            contadorTram=0;

                                        }catch(Lan6Excepcion e) {
                                            log.error("Error al ejecutar el Tercer  Paso - Actualizacion de todas las aportaciones - avisos ", e);
                                        }
                                        log.error(numExpediente + " - Paso 3.1 Aportaciones como Cambio Notificaciones ");
                                        try {
                                            List<Map<String, String>> listaDatosAportacionesnotificaciones = getDatosGeneralesEntRegAsociaExptsAportDocumentos(numExpediente, "", "notificaciones", con);
                                            for (Map<String, String> datosAportacion : listaDatosAportacionesnotificaciones) {
                                                contadorTram=contadorTram+1;
                                                
                                                lan6Expediente = new Lan6Expediente();
                                                lan6Expediente.setNumero(numExpediente);  //"2017/ORI14/00009"
                                                lan6Expediente.setEjercicio(datos[0]);
                                                
                                                // Participacion
                                                lan6participacion = new Lan6Participacion();
                                                lan6participacion.setIdParticipacion(Lan6Constantes.PARTICIPACION_UNICA);

                                                lan6participacion.setCanalNotificacion(Lan6Constantes.NOTIF_ELECTRONICA);
                                                lan6participacion.setIdioma(Lan6Constantes.IDIOMA_ES);
                                                tfnosAvisos = new ArrayList<String>();
                                                tfnosAvisos.add(par.getTlf());      // "943323232"
                                                lan6participacion.setTfnosAvisos(tfnosAvisos);

                                                mailsAvisos = new ArrayList<String>();
                                                mailsAvisos.add(par.getMail());  //"m.ferreira@prueba.com"
                                                lan6participacion.setMailsAvisos(mailsAvisos);

                                                lan6Expediente.setParticipacion(lan6participacion);

                                                listaTramites = new ArrayList<Lan6Tramite>();
                                                lan6Tramite = new Lan6Tramite();

//                                                lan6Tramite.setId((contadorTram> 1 ? Lan6Constantes.TRAMITE_CAMBIO_NOTIF + contadorTram : Lan6Constantes.TRAMITE_CAMBIO_NOTIF));
//                                                lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_CAMBIO_NOTIF_DESC);
//                                                lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_CAMBIO_NOTIF_DESC);
                                                lan6Tramite.setId(contadorTram > 1 ? gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_CAMBIO_NOTIF") + contadorTram : gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_CAMBIO_NOTIF"));
                                                lan6Tramite.setDescripcion(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_CAMBIO_NOTIF_DESC"));
                                                lan6Tramite.setDescripcionEu(gestionAdaptadoresLan6Config.getElementoConfigFicheroAdaptadoresProperties("TRAMITE_CAMBIO_NOTIF_DESC_EU"));
                                                lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_APORTACION);

                                                feTemp = new Date();
                                                cal = Calendar.getInstance();
                                                fecha = datosAportacion.get("RES_FEC");
                                                log.error("FechaActualizacion RES_FEC:" + fecha);
                                                if (fecha != null && fecha != "") {
                                                    feTemp = sdf.parse(fecha);
                                                } else {
                                                    log.error("No se ha podido recuperar la fecha de primera aportacion ponemos la actual ");
                                                }
                                                cal.setTime(feTemp);        //cal.set(2018, 4, 06);
                                                lan6Tramite.setFechaActualizacion(cal);

                                                listaTramites.add(lan6Tramite);

                                                lan6Expediente.setListaTramites(listaTramites);
                                                
                                                aportacionesCiudadano = new ArrayList<Lan6Aportacion>();
                                                documentos = new ArrayList<Lan6AportacionDoc>();

                                                //Recogemos los documentos de la anotacion de regsitro que estamos tratando
                                                documentos = getDocumentosEntradaRegistro(numExpediente, datosAportacion.get("RES_EJE"), datosAportacion.get("RES_NUM"), con);
                                                
                                                // // Aportacion
                                                lan6Aportacion = new Lan6Aportacion();

                                                lan6Aportacion.setIdAportacion(datosAportacion.get("NUMREGGV"));    //"2018RTE00094831"
                                                lan6Aportacion.setNumRegistro(datosAportacion.get("NUMREGGV"));        //2018RTE00094831")
                                                cal = Calendar.getInstance();
                                                cal.setTime(feTemp);        //cal.set(2018, 04, 05);
                                                lan6Aportacion.setFechaRegistro(cal);

                                                lan6Aportacion.setTipoAportacion(Lan6Constantes.TIPO_APORTACION_ATTACHMENT);

                                                lan6Aportacion.setDocumentos(documentos);
                                                cal = Calendar.getInstance();
                                                cal.setTime(feTemp);        //cal.set(2018, 04, 06);

                                                lan6Aportacion.setFechaAportacion(cal);
                                                lan6Aportacion.setIdEmisor(lan6Interesado.getNumIdentificacion());
                                                aportacionesCiudadano.add(lan6Aportacion);

                                                lan6Expediente.setAportacionesCiudadano(aportacionesCiudadano);

                                                // Llamada metodo
                                                //salida=servicios.actualizarAvisosNotificaciones(lan6Expediente);
                                                //llamamos a actualziar aportaciones porque deben tratartse como una aportacion al final se hara una sin documetnos 
                                                salida=servicios.actualizarAportacionSolicitante(lan6Expediente);
                                                log.error("Salida llamada al servicio actualizarAvisosNotificaciones paso 3.1 " + salida);
                                            }
                                            contadorTram=0;
                                        }catch(Lan6Excepcion e) {
                                            log.error("Error al ejecutar el Tercer  Paso - Actualizacion de todas las aportaciones - notificaiciones ", e);
                                        }
                                        
                                        // Fase 4 - actualizar tramites a pago
                                        log.error(numExpediente + " - Paso 4 Avance a Tramite de pago");
                                        try {
                                            lan6Tramite = new Lan6Tramite();
                                            lan6Tramite.setId(datosPagoTramitacion.getCod());   //"5"
                                            lan6Tramite.setDescripcion(datosPagoTramitacion.getDescripcion());      //"PAGO EN TRAMITACION"
                                            lan6Tramite.setDescripcionEu(datosPagoTramitacion.getDescripcionEu());

                                            lan6Tramite.setTipoTramite("general");

                                            cal = Calendar.getInstance();
                                            // eL TIPO DE OPERACION  esta mal en PRO  esta como Inicio "I" debe ser "A" de avance
                                            String tipoOperacion="A";
                                            if ("pargi1_flexia1".equalsIgnoreCase(servidor)) 
                                                tipoOperacion="I";
                                            log.error("tipoOperacion al LLamar para avazar a pago : " + tipoOperacion);
                                            feTemp = getFechaGeneradoxExpteTipoOperTram(numExpediente, tipoOperacion, (datosPagoTramitacion.getCod() != null ? Integer.valueOf(datosPagoTramitacion.getCod()) : 0), con);
                                            log.error("FechaActualizacion datosPagoTramitacion:" + feTemp);
                                            cal.setTime(feTemp);//cal.set(2017, 8, 26);
                                            //cal.set(2018, 04, 29);
                                            lan6Tramite.setFechaActualizacion(cal);

                                            lan6Tramite.setIdExpediente(numExpediente); //"2017/ORI14/00009"
                                            lan6Tramite.setEjercicio(datos[0]);     //2017
                                            List<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                                            tramites.add(lan6Tramite);

                                            //Llamada metodo       	
                                            salida = servicios.actualizarTramites(tramites);
                                            log.error("Salida llamada al servicio actualizarTramites paso 4 " + salida);     
                                            
                                        } catch(Lan6Excepcion e) {
                                            log.error("Error al ejecutar el 4  Paso - Avance a fase de pAgo ", e);
                                        }
                                        
                                        // Paso 5 - Actualizar aviso de notificaciones
                                        log.error(numExpediente + " Paso 5 Actualizar Datos Aviso/Notificaciones");
                                        try {
                                            lan6Expediente = new Lan6Expediente();
                                            lan6Expediente.setNumero(numExpediente);  //"2017/ORI14/00009"
                                            lan6Expediente.setEjercicio(datos[0]);

                                            // Participacion
                                            lan6participacion = new Lan6Participacion();
                                            lan6participacion.setIdParticipacion(Lan6Constantes.PARTICIPACION_UNICA);

                                            lan6participacion.setCanalNotificacion(Lan6Constantes.NOTIF_ELECTRONICA);
                                            lan6participacion.setIdioma(Lan6Constantes.IDIOMA_ES);
                                            tfnosAvisos = new ArrayList<String>();
                                            tfnosAvisos.add(par.getTlf());
                                            lan6participacion.setTfnosAvisos(tfnosAvisos);

                                            mailsAvisos = new ArrayList<String>();
                                            mailsAvisos.add(par.getMail());
                                            lan6participacion.setMailsAvisos(mailsAvisos);

                                            lan6Expediente.setParticipacion(lan6participacion);

                                            // Llamada metodo
                                            salida = servicios.actualizarAvisosNotificaciones(lan6Expediente);
                                            log.error("Salida llamada al servicio actualizar datos notificacion paso 5 " + salida);
                                            
                                        } catch(Lan6Excepcion e) {
                                            log.error("Error al ejecutar el 5  Paso - Actualizar datos notificaciones ", e);
                                        }
                                        

                                    } catch (Lan6Excepcion e) {
                                        ArrayList<String> codes = e.getCodes();
                                        ArrayList<String> messages = e.getMessages();
                                        log.error("Error al llamar servicios.iniciarExpediente(lan6Expediente) " + e.getMessage()
                                        +" --- "
                                        +"\n e.getMensajeExcepcion() : "+e.getMensajeExcepcion()
                                        +"\n e.getCausaExcepcion() : "+e.getCausaExcepcion()
                                        +"\n e.getSistema() : "+e.getSistema()
                                        +"\n ---"
                                        );
                                        String codigos ="";
                                        String mensajes="";
                                        for(String v : codes){
                                            codigos=codigos + v + "|";
                                        }
                                        for(String v : messages){
                                            mensajes=mensajes + v + "|";
                                        }
                                        log.error("codes: " + codigos 
                                                + "\n messages : " + mensajes);                                        
                                    }
                                }

                            }
                            if (dosEntornos) {
                                codOrg++;
                            } else {
                                codOrg = 2;
                            }
                            if (con != null) {
                                con.close();
                            }

                        }
                    } catch (Exception e) {
                        log.error(gestorExpedientesORI14issue321203.class.getName() + " Error en el job : ", e);
                        try {
                            int intentos = numIntentos + 1;
                        } catch (Exception i) {
                            log.error("Error en int intentos = numIntentos + 1 : " + i.getMessage());
                        }
                    } finally {
                        if (con != null) {
                            try {
                                con.close();
                            } catch (SQLException ex) {
                                log.error(gestorExpedientesORI14issue321203.class.getName()+" - Error cerrando BBDD", ex);
                            }
                        }
                    }
                }//para local quitar
            }
        log.error(this.getClass().getName() + ".execute -  Fin " + new Date().toString());
        } catch (Exception ex) {
            log.error(this.getClass().getName()  + " Error: " + ex);
        }
    }
    
    
    private List<ExpTram> getExpedientesCorrecionORI14( Connection con) throws Exception{
       Statement st = null;
       PreparedStatement ps =null;
       ResultSet rs = null;
       String numExp=null;
       int codTram=0;
       List<ExpTram> expedientes = new ArrayList<ExpTram>();
       
       try
        {
            StringBuffer sb = new StringBuffer("SELECT NUM_EXP ");
            sb.append(" FROM CEPAP_MIGRACION_EXCEL   "); 
            sb.append(" WHERE NUM_TAREA=321203 ");
            /*
            sb.append(" WHERE EXP_NUM IN('2017/ORI14/000008'," +
                                        "'2017/ORI14/000009'," +
                                        "'2017/ORI14/000010'," +
                                        "'2017/ORI14/000012'," +
                                        "'2017/ORI14/000013'," +
                                        "'2017/ORI14/000014'," +
                                        "'2017/ORI14/000015'," +
                                        "'2017/ORI14/000016'," +
                                        "'2017/ORI14/000017'," +
                                        "'2017/ORI14/000018'," +
                                        "'2017/ORI14/000025'," +
                                        "'2017/ORI14/000026'," +
                                        "'2017/ORI14/000027'," +
                                        "'2017/ORI14/000031'," +
                                        "'2017/ORI14/000032'," +
                                        "'2017/ORI14/000033'," +
                                        "'2017/ORI14/000034'," +
                                        "'2017/ORI14/000035'," +
                                        "'2017/ORI14/000036'," +
                                        "'2017/ORI14/000039'," +
                                        "'2017/ORI14/000040'," +
                                        "'2017/ORI14/000041'," +
                                        "'2017/ORI14/000042'," +
                                        "'2017/ORI14/000043'," +
                                        "'2017/ORI14/000044'," +
                                        "'2017/ORI14/000046'," +
                                        "'2017/ORI14/000047'," +
                                        "'2017/ORI14/000048'," +
                                        "'2017/ORI14/000049'," +
                                        "'2017/ORI14/000050'," +
                                        "'2017/ORI14/000051'," +
                                        "'2017/ORI14/000052'," +
                                        "'2017/ORI14/000054'," +
                                        "'2017/ORI14/000055'," +
                                        "'2017/ORI14/000056'," +
                                        "'2017/ORI14/000057'," +
                                        "'2017/ORI14/000058'," +
                                        "'2017/ORI14/000059'," +
                                        "'2017/ORI14/000060'," +
                                        "'2017/ORI14/000061'," +
                                        "'2017/ORI14/000063'," +
                                        "'2017/ORI14/000064'," +
                                        "'2017/ORI14/000065')");
            */
             sb.append(" ORDER BY NUM_EXP");
             if(log.isDebugEnabled()) 
                 log.error("sql = " + sb.toString());
             ps = con.prepareStatement(sb.toString());
             rs = ps.executeQuery();

            while(rs.next())
            {
                numExp=rs.getString("NUM_EXP");
                log.error("numExp = " + numExp);
                expedientes.add(new ExpTram(numExp,codTram));
            }
        } catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando los expedientes ORI14 ", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.error("Procedemos a cerrar el statement y el resultset - JOB epts ori14");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset - JOB epts ori14", e);                
            }
        }
        
        return expedientes;       
   }
    
    private java.util.Date getFechaGeneradoxExpteTipoOperTram(String numExp,String tipoOperacion,int tramite,Connection con) throws Exception {
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        java.util.Date fechaGenerado = new java.util.Date();

        try {
            StringBuffer sb = new StringBuffer(" select max(FECHA_GENERADO) FECHA_GENERADO from MELANBIDE43_INTEGMISGEST ");
            sb.append(" WHERE exp_num=? AND TIPO_OPERACION=? AND COD_TRAMITE_INICIO=?");
            if (log.isDebugEnabled()) {
                log.error("sql = " + sb.toString());
            }
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, numExp);
            ps.setString(2, tipoOperacion);
            ps.setInt(3, tramite);
            rs = ps.executeQuery();
            log.error("param: " +numExp+","+tipoOperacion+","+tramite );

            while (rs.next()) {
                fechaGenerado = rs.getDate("FECHA_GENERADO");
                log.error("FECHA_GENERADO = " + fechaGenerado);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando fecha generado operacio mis gestiones casos ori14 ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.error("Procedemos a cerrar el statement y el resultset - recuperando fecha generado operacio mis gestiones casos ori14");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - recuperando fecha generado operacio mis gestiones casos ori14", e);
            }
        }
        log.error("Retornamos Fecha Generado operacion  - fechaGenerado" + fechaGenerado);
        return fechaGenerado;
    }

    /**
     * 
     * @param numExpediente
     * @param factorPrimeraAport : Determina si se devuelve solo la primera aportacion (1), las demas  (0) o Todas inclusive la 1a (null o vacio)
     * @param factorLikeAsunto   : Se usa para recoger el tipo de aportacion : Documentos "document", modificacion notificaciones "notificaciones" o modificacion avisos "avisos"
     * @param con
     * @return
     * @throws Exception 
     */
     
    private List<Map<String,String>> getDatosGeneralesEntRegAsociaExptsAportDocumentos(String numExpediente, String factorPrimeraAport,String factorLikeAsunto, Connection con) throws Exception {
        log.error("getDatosGeneralesEntRegAsociaExptsAportDocumentos - Begin");
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String,String>> listaEntradas = new ArrayList<Map<String,String>>();
        Map<String,String> mapaDatos = new HashMap<String, String>();
        try {
            StringBuffer sb = new StringBuffer("");
            sb.append(" SELECT * FROM (");
            sb.append(" SELECT distinct EXR_EJE, EXR_NUM,  ");
            sb.append(" RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM, to_char(RES_FEC,'dd/mm/yyyy HH24:MI:SS') RES_FEC, to_char(RES_FED,'dd/mm/yyyy HH24:MI:SS') RES_FED, RES_ASU, ASUNTO, REGISTRO_TELEMATICO ");
            sb.append(" ,(CASE WHEN INSTR(RES_ASU,'NumRegGV')>0 THEN SUBSTR(RES_ASU,INSTR(RES_ASU,'NumRegGV')+9,15) ELSE NULL END) NumRegGV ");
            sb.append(" ,ROW_NUMBER() OVER (PARTITION BY EXR_EJE,EXR_NUM ORDER BY EXR_EJE,EXR_NUM,RES_EJE, RES_NUM) ORDER_FILA  ");
            sb.append(" FROM E_EXR ");
            sb.append(" LEFT JOIN R_RES ON EXR_NRE=R_RES.RES_NUM AND EXR_EJR=RES_EJE AND EXR_UOR=RES_UOR AND EXR_DEP=RES_DEP AND EXR_TIP=RES_TIP ");
            sb.append(" WHERE EXR_TIP='E' AND RES_ASU LIKE '%"+factorLikeAsunto+"%' ");
            sb.append(" order by E_EXR.EXR_EJE, E_EXR.EXR_NUM,R_RES.RES_EJE, R_RES.RES_NUM ");
            sb.append( ") WHERE exr_num=?");
            if(factorPrimeraAport!=null && factorPrimeraAport!="" && "1"==(factorPrimeraAport))
                sb.append( " AND ORDER_FILA=1 ");
            else if(factorPrimeraAport!=null && factorPrimeraAport!="" && "0"==(factorPrimeraAport))
                sb.append( " AND ORDER_FILA!=1 ");
                
            if (log.isDebugEnabled()) {
                log.error("sql = " + sb.toString());
            }
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, numExpediente);
            log.error("param: " +numExpediente );
            rs = ps.executeQuery();

            while (rs.next()) {
                mapaDatos.put("EXR_EJE",rs.getString("EXR_EJE"));
                mapaDatos.put("EXR_NUM",rs.getString("EXR_NUM"));
                mapaDatos.put("RES_DEP",rs.getString("RES_DEP"));
                mapaDatos.put("RES_UOR",rs.getString("RES_UOR"));
                mapaDatos.put("RES_TIP",rs.getString("RES_TIP"));
                mapaDatos.put("RES_EJE",rs.getString("RES_EJE"));
                mapaDatos.put("RES_NUM",rs.getString("RES_NUM"));
                mapaDatos.put("RES_FEC",rs.getString("RES_FEC"));
                mapaDatos.put("RES_FED",rs.getString("RES_FED"));
                mapaDatos.put("RES_ASU",rs.getString("RES_ASU"));
                mapaDatos.put("ASUNTO",rs.getString("ASUNTO"));
                mapaDatos.put("REGISTRO_TELEMATICO",rs.getString("REGISTRO_TELEMATICO"));
                mapaDatos.put("NUMREGGV",rs.getString("NUMREGGV"));
                mapaDatos.put("ORDER_FILA",rs.getString("ORDER_FILA"));
                listaEntradas.add(mapaDatos);
                mapaDatos=new HashMap<String, String>();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando fecha entrada registro 1 aportacion operacio mis gestiones casos ori14 ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.error("Procedemos a cerrar el statement y el resultset - recuperando fecha entrada registro 1 aportacionoperacio mis gestiones casos ori14");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - recuperando fecha entrada registro 1 aportacion operacio mis gestiones casos ori14", e);
            }
        }
        log.error("getDatosGeneralesEntRegAsociaExptsAportDocumentos - End " + listaEntradas.size());
        return listaEntradas;
    }

    private List<Lan6AportacionDoc> getDocumentosEntradaRegistro(String numExpediente, String ejercicioAnotacion, String numeroAnotacion, Connection con) throws Exception {
        log.error("getDocumentosEntradaRegistro - Begin");
        Statement st = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Lan6AportacionDoc> listaDocumentos = new ArrayList<Lan6AportacionDoc>();
        Lan6AportacionDoc documento = new Lan6AportacionDoc();
        try {
            StringBuffer sb = new StringBuffer("");
            sb.append(" SELECT distinct EXR_EJE, EXR_NUM ");
            sb.append(" ,RES_DEP, RES_UOR, RES_TIP, RES_EJE, RES_NUM, to_char(RES_FEC,'dd/mm/yyyy HH24:MI:SS') RES_FEC, to_char(RES_FED,'dd/mm/yyyy HH24:MI:SS') RES_FED, RES_ASU, ASUNTO, REGISTRO_TELEMATICO ");
            sb.append(" ,RED_NOM_DOC, to_char(RED_FEC_DOC,'dd/mm/yyyy HH24:MI:SS') RED_FEC_DOC, RED_IDDOC_GESTOR ");
            sb.append(" ,CASE WHEN INSTR(RED_NOM_DOC,'.',-1)> 0 THEN SUBSTR(RED_NOM_DOC,0,(INSTR(RED_NOM_DOC,'.',-1)-1)) ELSE RED_NOM_DOC END  DESCRIPCION_ROL ");
            sb.append(" ,(CASE WHEN INSTR(RES_ASU,'NumRegGV')>0 THEN SUBSTR(RES_ASU,INSTR(RES_ASU,'NumRegGV')+9,15) ELSE NULL END) NumRegGV ");
            sb.append(" FROM E_EXR ");
            sb.append(" LEFT JOIN R_RES ON EXR_NRE=R_RES.RES_NUM AND EXR_EJR=RES_EJE AND EXR_UOR=RES_UOR AND EXR_DEP=RES_DEP AND EXR_TIP=RES_TIP ");
            sb.append(" LEFT JOIN R_red on RES_DEP=REd_DEP and RES_UOR=REd_UOR and RES_TIP=REd_TIP and RES_EJE=REd_EJE and RES_NUM=REd_NUM ");
            sb.append(" WHERE R_RES.RES_TIP='E' ");
            sb.append(" AND  E_EXR.exr_num=? ");
            sb.append(" AND RES_EJE=? AND RES_NUM=? ");
            sb.append("order by E_EXR.EXR_EJE, E_EXR.EXR_NUM,R_RES.RES_EJE, R_RES.RES_NUM,RED_FEC_DOC,RED_IDDOC_GESTOR asc");
            

            if (log.isDebugEnabled()) {
                log.error("sql = " + sb.toString());
            }
            ps = con.prepareStatement(sb.toString());
            ps.setString(1, numExpediente);
            ps.setInt(2, Integer.valueOf(ejercicioAnotacion));
            ps.setInt(3, Integer.valueOf(numeroAnotacion));
            log.error("param: " +numExpediente+","+ejercicioAnotacion+","+numeroAnotacion );
            rs = ps.executeQuery();

            while (rs.next()) {
                // Documento 
                documento.setOid(rs.getString("RED_IDDOC_GESTOR"));
                documento.setNombre(rs.getString("RED_NOM_DOC"));
                documento.setDescripcionRol(rs.getString("DESCRIPCION_ROL"));
                documento.setTipo(Lan6Constantes.TIPO_DOC_ELECTRONICO);
                listaDocumentos.add(documento);
                documento=new Lan6AportacionDoc();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando documentos entrada registro aportacion operacion mis gestiones casos ori14 ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.error("Procedemos a cerrar el statement y el resultset - recuperando documentos entrada registro aportacion mis gestiones casos ori14");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset - recuperando documentos entrada registro aportacion mis gestiones casos ori14", e);
            }
        }
        log.error("getDocumentosEntradaRegistro - End " + listaDocumentos.size());
        return listaDocumentos;
    }
    
    private Tramite getDatosTramiteFaseIssue321203(String proced, String codTra, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int result = 0;
        boolean nuevo = true;
        Tramite exp = new Tramite();
        try {
            String query = null;

            query = "SELECT * FROM MELANBIDE43_FASE "
                    + "WHERE COD_PROC = '" + proced + "' AND COD_TRAMITE = " + codTra;

            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            int id = 0;
            if (rs.next()) {
                exp.setCod(rs.getString("COD_TRAMITE"));
                exp.setFase(rs.getString("COD_FASE"));
                exp.setDescripcion(rs.getString("DESC_FASE_C"));
                exp.setDescripcionEu(rs.getString("DESC_FASE_E"));
            }
        } catch (Exception ex) {
            //log.error("Se ha producido un error grabando el contador ("+tipoContador+")", ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.error("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return exp;
    }

}
