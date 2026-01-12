package es.altia.flexia.integracion.moduloexterno.melanbide43.manager;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43Tramite9099DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.i18n.MeLanbide43I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.DatosAvisoCSRegexlan;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaFaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaListadoMisGestiones;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaLlamadasMisGestVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaNotificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Justificante;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoSeleccionadoV0;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite9099CerrarVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tramite9099VO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.TramiteVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.util.ByteArrayInOutStream;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.beans.Lan6Documento;
import es.lanbide.lan6.adaptadoresPlatea.dokusi.servicios.Lan6DokusiServicios;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6InformarConsultaExcepcion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Aportacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6AportacionDoc;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Direccion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Expediente;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Interesado;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Participacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Tramite;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.servicios.Lan6InformarConsultaServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;


public class MeLanbide43Manager {
    
    //Logger
    private static Logger log = Logger.getLogger(MeLanbide43Manager.class);
    private final String BARRA                              = "/";
    SimpleDateFormat formatDateLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    private final MeLanbide43DAO meLanbide43DAOServ = MeLanbide43DAO.getInstance();
    //Instancia
    private static MeLanbide43Manager instance = null;
    
    public String generarMisGestiones (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExp, String evento, AdaptadorSQLBD adaptador) throws Exception{
        if(log.isDebugEnabled()) log.info("generarMisGestiones ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExp = " + numExp  + " evento = " + evento + " ) : BEGIN");
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
        
        String clave= null;
        String codigoOperacion = "0";
        String mensaje = "";
        String id= null; 
        try{           
            if(numExp!=null && !"".equals(numExp)){
                String[] datos = numExp.split(BARRA);
                String ejercicio = datos[0];
                String codProcedimiento = datos[1];
                boolean instancia = expInstanciaParte(numExp, adaptador);
                boolean sinSolicitud = procedimientoSinSolicitud(codProcedimiento);
                log.info("instancia: " + instancia); 
                log.info("sinSolicitud: " + sinSolicitud); 
                if (instancia || sinSolicitud || ( !evento.equals("I") && !instancia))
                {
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String fechaIni = meLanbide43Manager.fechaInicioExpediente(numExp, adaptador);                
                    date = dateFormat.parse(fechaIni);
                    String fecha = meLanbide43Manager.verificarFecha(codProcedimiento, adaptador);
                    Participantes par = meLanbide43Manager.obtenerDatosParticipantes(numExp,adaptador);
                    log.debug("PARTICIPANTES: "+par); 
                    log.info("tipo id: " + par.getTipoID());
                    if(par.getTipoID() != 0){
                        Date fec = null;
                        if(fecha != ""){
                            fec = dateFormat.parse(fecha);
                            if(date.after(fec)){   
                                if(evento.equals("C")){
                                     if(!meLanbide43Manager.expedienteCerrado(numExp,adaptador)){
                                        id = guardarGestiones(numExp, codOrganizacion, adaptador, codTramite, evento);
                                        log.info("Id generado: " + id);
                                        codigoOperacion= avanzarGestiones(id, String.valueOf(codOrganizacion),adaptador);
                                     } else {
                                         log.error("El expediente: " +numExp+" ya ha sido cerrado" );
                                     }
                                } else {
                                    id = guardarGestiones(numExp, codOrganizacion, adaptador, codTramite, evento);
                                    log.info("Id generado: " + id);
                                    codigoOperacion = avanzarGestiones(id, String.valueOf(codOrganizacion),adaptador);
                                }
                                
                            }
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";            
            meLanbide43Manager.borrarProcesado(id, adaptador);
            mensaje = MeLanbide43I18n.getInstance().getMensaje(1, "error.errorGen");
            
            //insertar error en registro de errores           
            log.error("Error en la funci�n generarMisGestiones: ", ex);
            String error = "Error en la funci�n generarMisGestiones: " + ex.getMessage()!=null?ex.getMessage().toString():"null";
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_012");
            errorB.setMensajeError("Error al iniciar un expediente en la funci�n generarMisGestiones");
            errorB.setSituacion("generarMisGestiones");

            MeLanbide43Manager.grabarError(errorB, ex.getMessage()!=null?ex.getMessage().toString():"null", ex.toString(), numExp);
             //fin 
            
            //throw ex;
        }
        if(log.isDebugEnabled()) log.info("generarMisGestiones() : END");
        return codigoOperacion;
    }
       
    
    public boolean comprobarExpGenerado (int codOrganizacion,int codTramite,int ocurrenciaTramite,String numExp, Connection con) throws Exception {
        if(log.isDebugEnabled()) log.info("comprobarExpGenerado ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExp = " + numExp  + " ) : BEGIN");
        MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
                
        if(log.isDebugEnabled()) log.info("comprobarExpGenerado() : END");
        return meLanbide43DAO.getInstance().comprobarExpGenerado(numExp, con);
    }
   
    public String avanzarGestiones(String id, String codOrg, AdaptadorSQLBD adaptador) throws Exception {
        String codOperacion = "0"; 
        // LEIRE       
        ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>(); 
        try
        {
            log.info("ANTES DE LEER GESTIONES");
            misGestiones = obtenerDatosGestiones(id, adaptador);
             if(misGestiones.size() > 0){
                if(log.isDebugEnabled()) {
                    log.info("Existen datos para insertar en pesta�a 'Mis gestiones' de este expediente");
                }
                for(FilaListadoMisGestiones unidad : misGestiones){
                    //String tipo = unidad.getTipoOperacion();
                    if(unidad.getIntentos() < 3)
                    {
                        char[] tipo = unidad.getTipoOperacion().toCharArray();
                        switch (tipo[0])
                        {                        
                            case 'I':   //inicio expediente
                                codOperacion = iniciarExpConsulta(unidad, codOrg, adaptador);                                
                                break;
                            case 'A':    //Avance expediente
                                codOperacion = avanceExpConsulta(unidad, codOrg, adaptador);
                                break;
                            case 'C':   //cierre expediente
                                //if(!meLanbide43Manager.expedienteCerrado(unidad.getNumExp(),getAdaptSQLBD(codOrg))){
                                codOperacion = cierreExpConsulta(unidad, codOrg, adaptador);
                                break;
                            default:
                        }
                    }
                }
            }
            
            log.info("DESPUES DE LEER GESTIONES");
        
        }catch(Exception ex)
        {   
            log.error("Error en la funcion execute: " + ex.getMessage());
            //throw ex;
            codOperacion="2";
        }
        //new MECommonDBUtils().getConnection("0");
        return codOperacion;
    }
    
    public String iniciarExpConsulta(FilaListadoMisGestiones gestion, String codOrg, AdaptadorSQLBD adaptador)throws Exception{
      String codOperacion = "0"; 
      MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();
      String expe = gestion.getNumExp();
      String[] pepe = expe.split("/");
      int id = gestion.getId();
      String nomDoc = "";
      String oid = "";
      Connection con = null;
     try
        {   
            log.info("En funci�n iniciarExpConsulta()");
            
            
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            con = adaptador.getConnection();
            Integer esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(expe, con);
            String idProcedimiento = "";
            if (esDesarrolloCero > 0){
                idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            } else {
                idProcedimiento = convierteProcedimiento(pepe[1]);
            }
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            
            
            Expediente exp = meLanbide43Manager.obtenerDatosExpedientes(gestion.getNumExp(),adaptador);
            Participantes par = meLanbide43Manager.obtenerDatosParticipantes(gestion.getNumExp(),adaptador);
            String info = meLanbide43Manager.obtenerJustificante(gestion.getNumInicio().toString(), gestion.getRegInicio().toString(),adaptador);
            Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(idProcedimiento);
            log.info("new informar consulta servicios - "+idProcedimiento);
            Lan6DokusiServicios dokusi = new Lan6DokusiServicios(idProcedimiento);
            log.info("new idokusi servicios-"+idProcedimiento);
            Lan6Documento doclan6 =null;
            
            
            if(info != null)
            {                
                log.info("hay documento. Datos-: " + info);
                String[] nom = info.split("#");
                oid = nom[0];
                log.info("oid: " + oid);
                nomDoc = nom[1];
                log.info("nomDoc: " + nomDoc);
                doclan6  = dokusi.consultarDocumento(oid);
            }
            Justificante jus = new Justificante();
            log.info("doclan6: " + doclan6);
            if(doclan6 != null){
                log.info("Recogemos el documento de dokusi");
                byte[] bdata = doclan6.getContenido();
                String fichero = new String(bdata);
                jus.setContenido(fichero);
                jus.setNombre(nomDoc);
                log.info("Recogido documento de dokusi");
            
            }
            
            //Flexia
            Lan6Expediente lan6Expediente = new Lan6Expediente();
            lan6Expediente.setNumero(expe);
            lan6Expediente.setEjercicio(pepe[0]);            
            lan6Expediente.setDescripcion(expe);            
            lan6Expediente.setDescripcionEu(expe);            
            String fecha = gestion.getFechaAsun();
            
            log.info("Fecha registro GV: " + fecha);
            Calendar fechaSolicitud = Calendar.getInstance();
            //if(fecha != null && !fecha.equals("")){
            if(fecha.contains("-")){
                String [] partes = fecha.split("_");
                fecha = partes[0].replace("-", "/");
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                format.parse(fecha);
                fechaSolicitud.setTime(format.parse(fecha));
                lan6Expediente.setFechaRegistro(fechaSolicitud);
                log.info("Despues de recoger fecha registro GV");
                //Fecha registro de Flexia
                fechaSolicitud.setTime(exp.getFechaSolicitud());
                lan6Expediente.setFechaSolicitud(fechaSolicitud);
            }else
                lan6Expediente.setFechaSolicitud(fechaSolicitud);

            Calendar fechaApertura = Calendar.getInstance();
            lan6Expediente.setFechaApertura(fechaApertura);
            
            //Expedientes relacionados
            log.info("Expedientes relacionados: " + pepe[1]);
            if(pepe[1].equals("RECUR"))
            {
                log.info("Es recurso. Exp relacionado: " + exp.getNumExpRel());
                if(exp.getNumExpRel() != null && !exp.getNumExpRel().equals(""))
                {
                    log.info("Expediente relacionado: " + exp.getNumExpRel());
                    Lan6Expediente lan6ExpedienteRelacionado = new Lan6Expediente();
                    lan6ExpedienteRelacionado.setNumero(exp.getNumExpRel());
                    lan6ExpedienteRelacionado.setEjercicio(exp.getEjercicioRel().toString());
                    String[] proc = exp.getNumExpRel().split("/");                    
                    
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    //Connection con = adaptador.getConnection();
                    esDesarrolloCero = MeLanbide43Manager.getInstance().esDesarrolloCero(exp.getNumExpRel(), con);
                    if (esDesarrolloCero > 0){
                        idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    } else {
                        idProcedimiento = convierteProcedimiento(proc[1]);
                    }
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    
                    lan6ExpedienteRelacionado.setIdProcedimiento(idProcedimiento);

                    ArrayList<Lan6Expediente> listaExpedientesRelacionados = new ArrayList<Lan6Expediente>();
                    listaExpedientesRelacionados.add(lan6ExpedienteRelacionado);
                    lan6Expediente.setListaExpedientesRelacionados(listaExpedientesRelacionados);
                }     
            }
            
            String nombre = "";            
            log.info("Justificante: "+jus);
            if(doclan6 != null) {
                lan6Expediente.setNumRegistro(gestion.getRegAsun());
                lan6Expediente.setFicheroSolicitudHtml(jus.getContenido());
                nombre = jus.getNombre();
                if(!nombre.equals("")){
                    /*int total = nombre.length();
                    int inicio = nombre.indexOf("-") + 1;
                    log.info("Nombre fichero: " + nombre + " -- Total: " +total + " ; inicio: " + inicio);
                    if(inicio != -1)
                        oid = nombre.substring(inicio, total);

                    log.info("oid: " + oid);*/
                    lan6Expediente.setNmFicherosolicitudHtml(nombre);
                }
            }
            
            log.info("Antes de recoger los datos del tr�mite");
            //tramite - mínimo el de apertura
            ArrayList<Lan6Tramite> listaTramites = new ArrayList<Lan6Tramite>();
            Lan6Tramite lan6Tramite = new Lan6Tramite();
            Tramite tra = new Tramite();
            tra = obtenerDatosTramite(pepe[1], gestion.getTramiteInicio(), adaptador);
            
            lan6Tramite.setId(gestion.getTramiteInicio().toString());
            lan6Tramite.setDescripcion(tra.getDescripcion());
            lan6Tramite.setDescripcionEu(tra.getDescripcionEu());
        	
            lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ABRIR_EXPEDIENTE);            
            
            log.info("Despues de recoger los datos del tr�mite");
            
            listaTramites.add(lan6Tramite);
            
            lan6Expediente.setListaTramites(listaTramites);
            
            // Participacion
            Lan6Participacion lan6participacion = new Lan6Participacion();
            
            ArrayList<String> tfnosAvisos = new ArrayList<String>();
            tfnosAvisos.add(par.getTlf());
            
            ArrayList<String> mailsAvisos = new ArrayList<String>();
            mailsAvisos.add(par.getMail());
            
            Lan6Direccion direccion = new Lan6Direccion();
            direccion.setTipoDireccion(Lan6Constantes.DIRECCION_ESTATAL);
            direccion.setIdPais(par.getIdPais());
            direccion.setNmPais(par.getPais());
            direccion.setIdProvincia(par.getIdProv());
            log.info("direccion.getIdProvincia: " + direccion.getIdProvincia());
            direccion.setNmProvincia(par.getProv());
            log.info("direccion.getNmProvincia: " + direccion.getNmProvincia());
            direccion.setIdMunicipio(par.getIdMuni());
            direccion.setNmMunicipio(par.getMuni());
            direccion.setIdLocalidad(par.getIdMuni());
            direccion.setNmLocalidad(par.getMuni());
            direccion.setIdCalle(par.getIdCalle());
            direccion.setNmCalle(par.getCalle());
            direccion.setPortal(par.getNum());
            direccion.setLetra(par.getLetra());
            
            
            if(doclan6 != null){
                lan6participacion.setCanalNotificacion(Lan6Constantes.NOTIF_ELECTRONICA);
                lan6participacion.setTfnosAvisos(tfnosAvisos);
                lan6participacion.setMailsAvisos(mailsAvisos);
            }else{ 
                lan6participacion.setCanalNotificacion(Lan6Constantes.NOTIF_POSTAL);
                lan6participacion.setDireccion(direccion);
                log.info("Mandamos direccion: " + direccion.getNmCalle());
            }
            lan6participacion.setIdioma(Lan6Constantes.IDIOMA_ES);
            
            
            //log.info("Antes de recoger los participantes");
            //foreach()
            // Interesado
            Lan6Interesado lan6Interesado = new Lan6Interesado();
            lan6Interesado.setNumIdentificacion(par.getNif());
            log.info("Apellido 1 : " + par.getApe1());
            //Si es presencial se recogera el nif de EJIE y no sera persona física
            if(par.getApe1() == null || par.getApe1().equals("") || par.getTipoID() == 4 || par.getTipoID() == 5)
            {
                lan6Interesado.setTipoIdentificacion(Lan6Constantes.PERSONA_JURIDICA);
                lan6Interesado.setRazonSocial(par.getNombre());
            }else if(par.getTipoID() == 1 || par.getTipoID() == 2 || par.getTipoID() == 3
                     || par.getTipoID() == 8 || par.getTipoID() == 9)
            {
                lan6Interesado.setTipoIdentificacion(Lan6Constantes.PERSONA_FISICA);
                lan6Interesado.setNombre(par.getNombre());
                lan6Interesado.setApellido1(par.getApe1());
                lan6Interesado.setApellido2(par.getApe2());
            }
            //String tipo = Lan6Constantes.TIPO_INTERESADO_TITULAR;
            /*if(par.getRol().equals("1"))
                tipo = Lan6Constantes.tipo_in*/
            lan6Interesado.setTipo(Lan6Constantes.TIPO_INTERESADO_TITULAR);

            ArrayList<Lan6Interesado> interesados = new ArrayList<Lan6Interesado>();
            interesados.add(lan6Interesado);
            
            lan6participacion.setInteresados(interesados);

            ArrayList<Lan6Participacion> participaciones = new ArrayList<Lan6Participacion>();
            participaciones.add(lan6participacion);
            
            log.info("Despues de recoger los participantes");
            lan6Expediente.setParticipacion(lan6participacion);

            ArrayList<Lan6AportacionDoc> documentos = new ArrayList<Lan6AportacionDoc>();


            log.info("Antes de recoger los documentos aportados");
            if(doclan6 != null){
                // Documento aportado
//                Lan6AportacionDoc lan6AportacionDoc = new Lan6AportacionDoc();
//                lan6AportacionDoc.setOid(oid);
//                lan6AportacionDoc.setNombre(nombre);
//                lan6AportacionDoc.setDescripcionRol(nombre);
//                lan6AportacionDoc.setTipo(Lan6Constantes.TIPO_DOC_ELECTRONICO);
//
//                documentos.add(lan6AportacionDoc);
//
//                // Aportacion
//                Lan6Aportacion lan6Aportacion = new Lan6Aportacion();
//                lan6Aportacion.setTipoAportacion(Lan6Constantes.TIPO_APORTACION_APPLICANT);
//                lan6Aportacion.setTipoTramite(Lan6Constantes.TRAMITE_ABRIR_EXPEDIENTE);
//                lan6Aportacion.setDocumentos(documentos);
//                lan6Aportacion.setFechaAportacion(fechaApertura);
//                
//                ArrayList<Lan6Aportacion> aportacionesCiudadano = new ArrayList<Lan6Aportacion>();
//                aportacionesCiudadano.add(lan6Aportacion);                
//                lan6Expediente.setAportacionesCiudadano(aportacionesCiudadano);
                Lan6Aportacion lan6Aportacion = new Lan6Aportacion();
                lan6Aportacion.setTipoAportacion(Lan6Constantes.TIPO_APORTACION_APPLICANT);
                lan6Aportacion.setTipoTramite(Lan6Constantes.TRAMITE_ABRIR_EXPEDIENTE);
                lan6Aportacion.setFechaAportacion(fechaApertura);


                ArrayList<Lan6Aportacion> aportacionesCiudadano = new ArrayList<Lan6Aportacion>();
                aportacionesCiudadano.add(lan6Aportacion);
                lan6Expediente.setAportacionesCiudadano(aportacionesCiudadano);

            }
            
            if(null == doclan6)
            {
                //recogemos los documentos aportados por el ciudadano
                ArrayList<String> docAportados = meLanbide43Manager.obtenerDocAportados(gestion.getNumInicio().toString(), gestion.getRegInicio().toString(),adaptador);
                log.info("docAportados.Size(): " + docAportados.size());
                ArrayList<Lan6Aportacion> aportacionesCiudadano = new ArrayList<Lan6Aportacion>();
                if(null != docAportados)
                {
                    log.info("dentro del if");
                    for ( String  aport : docAportados )
                    {       
                        Lan6Documento aportado =null;
                        log.info("hay documento. Datos-: " + aport);
                        String[] nom = aport.split("#");
                        String iid = nom[0];
                        String nombreApor = nom[1];
                        aportado  = dokusi.consultarDocumento(iid);

                        Justificante docu = new Justificante();
                        if(aportado != null){
                            jus.setNombre(nombreApor);
                        }

                        Lan6AportacionDoc lan6AportacionDoc = new Lan6AportacionDoc();
                        lan6AportacionDoc.setOid(iid);
                        lan6AportacionDoc.setNombre(nombreApor);
                        lan6AportacionDoc.setDescripcionRol(nombreApor);
                        lan6AportacionDoc.setDescripcionRolEu(nombreApor);
                        lan6AportacionDoc.setTipo(Lan6Constantes.TIPO_DOC_ELECTRONICO);

                        documentos.add(lan6AportacionDoc);
                    }
                }
                // Aportacion
                Lan6Aportacion lan6Aportacion = new Lan6Aportacion();
                lan6Aportacion.setTipoAportacion(Lan6Constantes.TIPO_APORTACION_APPLICANT);
                lan6Aportacion.setTipoTramite(Lan6Constantes.TRAMITE_ABRIR_EXPEDIENTE);
                log.info("documentos aportados");
                lan6Aportacion.setDocumentos(documentos);
                lan6Aportacion.setFechaAportacion(fechaApertura);
                log.info("fecha solicitud");
                if(null != exp.getFechaSolicitud())
                    fechaApertura.setTime(exp.getFechaSolicitud());
                else 
                    fechaApertura.setTime(new Date());
                lan6Aportacion.setFechaRegistro(fechaApertura);

                aportacionesCiudadano.add(lan6Aportacion);     
                    
                lan6Expediente.setAportacionesCiudadano(aportacionesCiudadano);
            }

            
            log.info("Despues de recoger los documentos aportados");
            
            //datos de lan6expedeinte
            log.debug("getNumero:"+lan6Expediente.getNumero());
            log.debug("getEjercicio:"+lan6Expediente.getEjercicio());
            log.debug("getNumRegistro:"+lan6Expediente.getNumRegistro());
            log.debug("getNumRegistro:"+lan6Expediente.getNumRegistro());
            //log.debug("getFechaRegistro:"+lan6Expediente.getFechaRegistro());
            SimpleDateFormat formatter=new SimpleDateFormat("dd-MMM-yyyy"); 
            if (lan6Expediente.getFechaRegistro()!=null)
                log.debug("getFechaRegistro:"+formatter.format(lan6Expediente.getFechaRegistro().getTime()));
            if (lan6Expediente.getFechaSolicitud()!=null)
                log.debug("getFechaSolicitud:"+formatter.format(lan6Expediente.getFechaSolicitud().getTime()));
             if (lan6Expediente.getFechaApertura()!=null)
                log.debug("getFechaApertura:"+formatter.format(lan6Expediente.getFechaApertura().getTime()));
            //log.debug("getListaExpedientesRelacionados.size:"+lan6Expediente.getListaExpedientesRelacionados().size());
            if (lan6Expediente.getListaExpedientesRelacionados()!=null)
                for (Lan6Expediente expedienteRel : lan6Expediente.getListaExpedientesRelacionados()) {
                        log.debug("---->expedienteRel:num="+expedienteRel.getNumero()+", ejer="+expedienteRel.getEjercicio());                   
                }
            log.debug("getNmFicherosolicitudHtml:"+lan6Expediente.getNmFicherosolicitudHtml());
            //log.debug("getListaTramites.size:"+lan6Expediente.getListaTramites().size());
            if (lan6Expediente.getListaTramites()!=null)
                for (Lan6Tramite tramite : lan6Expediente.getListaTramites()) {
                        log.debug("---->Tramite:id="+tramite.getId()+", desccas="+tramite.getDescripcion()+", desceu="+tramite.getDescripcionEu()+", tipotram="+tramite.getTipoTramite());                   
                }
            log.debug("Participacion:"+lan6Expediente.getParticipacion());
            if (lan6Expediente.getParticipacion()!=null){
                log.debug("---->Participante canal:"+lan6Expediente.getParticipacion().getCanalNotificacion());
                log.debug("---->Participante TfnosAvisos:"+lan6Expediente.getParticipacion().getTfnosAvisos());
                log.debug("---->Participante mailsAvisos:"+lan6Expediente.getParticipacion().getMailsAvisos());
                log.debug("---->Participante direccion:"+lan6Expediente.getParticipacion().getDireccion());
                log.debug("---->Participante idioma:"+lan6Expediente.getParticipacion().getIdioma());                
                log.debug("---->Participante interesado:numiden="+lan6Expediente.getParticipacion().getInteresados().get(0).getNumIdentificacion()+", tipoiden="
                        +lan6Expediente.getParticipacion().getInteresados().get(0).getTipoIdentificacion()+", razon="
                        +lan6Expediente.getParticipacion().getInteresados().get(0).getRazonSocial()+", nombre="
                        +lan6Expediente.getParticipacion().getInteresados().get(0).getNombre()+", ape1="
                        +lan6Expediente.getParticipacion().getInteresados().get(0).getApellido1()+", ape2="
                        +lan6Expediente.getParticipacion().getInteresados().get(0).getApellido2()+", tipo="
                        +lan6Expediente.getParticipacion().getInteresados().get(0).getTipo());               
            }
            if (lan6Expediente.getAportacionesCiudadano()!=null)
                for (Lan6Aportacion aportacion : lan6Expediente.getAportacionesCiudadano()) {
                    if(doclan6 != null){
                        log.debug("---->aportacion ciudadano:tipo="+aportacion.getTipoAportacion()+", tipoTram="+aportacion.getTipoTramite()+", fecaportacion="+formatter.format(aportacion.getFechaAportacion().getTime()));                     
                    }else{
                        log.debug("---->aportacion ciudadano:tipo="+aportacion.getTipoAportacion()+", tipoTram="+aportacion.getTipoTramite());
                        log.info("DOCUMENTOS APORTADOS");
                        for (Lan6AportacionDoc aportaDoc : aportacion.getDocumentos()) {
                                log.debug("docAportado:oid="+aportaDoc.getOid()+", nombre="+aportaDoc.getNombre()+", rol="+aportaDoc.getDescripcionRol()+", tipo="+aportaDoc.getTipo());                   
                        }
                        log.info("FIN DOCUMENTOS APORTADOS");
                                log.debug(", fecAportacion="+formatter.format(aportacion.getFechaAportacion().getTime())
                                +", fecRegistro="+formatter.format(aportacion.getFechaRegistro().getTime()));

                    }                   
                }
            
            //llamada al metodo
            log.info("-------------FECHA REGISTRO: "+ lan6Expediente.getFechaRegistro());
            servicios.iniciarExpediente(lan6Expediente);//.iniciarExpedienteSistemaConsulta(lan6Expediente);
            
            actualizarProcesados(gestion.getId(), adaptador);
            
            
        }catch(Lan6InformarConsultaExcepcion ice){
            codOperacion="2";
            ArrayList<String> codes = ice.getCodes();
            ArrayList<String> messages = ice.getMessages();

            
             StackTraceElement[] stacktrace=ice.getStackTrace();
             String error="";
             String causa = "";//ice.getCausaExcepcion();
                causa = ice.getCausaExcepcion();
             for (int i=0; i<codes.size();i++){
                 error += messages.get(i);
                 //error += codes.get(i);
             }
             
            try{
                int intentos = gestion.getIntentos() + 1;
            actualizarError(id, intentos, error, adaptador);
            }catch(Exception e){
                log.error("Error en la funci�n actualizarError: " + e.getMessage());
             
            }
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_013");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion al inicar un expediente");
            errorB.setSituacion("iniciarExpConsulta");

            MeLanbide43Manager.grabarError(errorB, error, causa, gestion.getNumExp());
            throw ice;
        }catch(Lan6Excepcion ex){ 
            codOperacion="2";
            log.error("Error en la funci�n iniciarExpConsulta: " + ex.getException().getMessage());
            String error = "Error en la funci�n iniciarExpConsulta: " + ex.getException().getMessage();
            
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_012");
            errorB.setMensajeError("Error al inicar un expediente");
            errorB.setSituacion("iniciarExpConsulta");

            MeLanbide43Manager.grabarError(errorB, ex.getException().getMessage()!=null?ex.getException().getMessage():"null", ex.getException().toString(), gestion.getNumExp());
            
            try{
                int intentos = gestion.getIntentos() + 1;
            actualizarError(id,intentos, error, adaptador);
            }catch(Exception e){
                log.error("Error en la funci�n actualizarError: " + e.getMessage());
             
            }
            throw ex;
        }     
        catch(Exception ex)
        {
            codOperacion="2";
            //meLanbide43Manager.insertarError(ex.getMessage().toString(), "iniciarExpConsulta", Integer.parseInt(pepe[2]),null);
            log.error("Error en la funci�n iniciarExpConsulta: ", ex);
            String error = "Error en la funci�n iniciarExpConsulta: " + ex.getMessage();
            
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_012");
            errorB.setMensajeError("Error al inicar un expediente");
            errorB.setSituacion("iniciarExpConsulta");

            grabarError(errorB, ex.getMessage()!=null?ex.getMessage().toString():"null", ex.toString(), gestion.getNumExp());
            
            try{
                int intentos = gestion.getIntentos() + 1;
                actualizarError(id,intentos, error, adaptador);
            }catch(Exception e){
                log.error("Error en la funci�n actualizarError: " + e.getMessage());
             
            }
            throw ex;
        }
        finally{
            try
            {
                if(con!=null)con.close();
            }
            catch(Exception ex){log.error("Error en funcion iniciarExpConsulta: " , ex);}
        }
        return codOperacion;
 } 
    
    public String avanceExpConsulta(FilaListadoMisGestiones gestion, String codOrg, AdaptadorSQLBD adaptador) throws Exception{
      String codOperacion = "0";
      Connection con = null;
      try {
            Calendar hoy = Calendar.getInstance();

            String exp = gestion.getNumExp();
            String[] pepe = exp.split("/");  
            log.info("Leemos los datos del expediente año/numero: " + pepe[0]+"/" + pepe[2]);

            
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            con = adaptador.getConnection();
            Integer esDesarrolloCero = MeLanbide43DAO.getInstance().esDesarrolloCero(exp, con);
            String idProcedimiento = "";
            if (esDesarrolloCero > 0){
                    idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            } else {
                    idProcedimiento = convierteProcedimiento(pepe[1]);
            }
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            
            
            Expediente expe = obtenerDatosExpedientes(gestion.getNumExp(),adaptador);
            log.info("Inicializamos Lan6InformarConsultaServicios");

            Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(idProcedimiento);

            Tramite tra = new Tramite();

            log.info("Recogemos los datos del tr�mite de inicio " + gestion.getTramiteInicio().toString());

            tra = obtenerDatosTramite(pepe[1], gestion.getTramiteInicio(), adaptador);

            //Expediente
            Lan6Expediente lan6Expediente = new Lan6Expediente();

            log.info("Comenzamos a llenar lan6Expediente. Tr�mite: " + tra.getCod().toString());
            lan6Expediente.setNumero(exp);
            lan6Expediente.setDescripcion(exp);
            lan6Expediente.setDescripcionEu(exp);


            lan6Expediente.setEjercicio(pepe[0]); 

            Lan6Tramite   lan6Tramite = new Lan6Tramite();	
            lan6Tramite.setId(gestion.getTramiteInicio());
            lan6Tramite.setDescripcion(tra.getDescripcion());
            lan6Tramite.setDescripcionEu(tra.getDescripcionEu());
            lan6Tramite.setIdExpediente(exp);
            lan6Tramite.setEjercicio(pepe[0]);

            Calendar fecha = Calendar.getInstance();
            //fecha.setTime(Today);

            lan6Tramite.setFechaActualizacion(hoy);            
            
            /*log.info("Expedientes relacionados");
            if(idProcedimiento.equals(Lan6Constantes.ID_PROC_RECUR))
            {
                if(exp.getNumExpRel() != null && !exp.getNumExpRel().equals(""))
                {
                    log.info("Expediente relacionado: " + exp.getNumExpRel());
                    Lan6Expediente lan6ExpedienteRelacionado = new Lan6Expediente();
                    lan6ExpedienteRelacionado.setNumero(exp.getNumExpRel());
                    lan6ExpedienteRelacionado.setEjercicio(exp.getEjercicioRel().toString());
                    lan6ExpedienteRelacionado.setIdProcedimiento(Lan6Constantes.ID_PROC_CONC);

                    ArrayList<Lan6Expediente> listaExpedientesRelacionados = new ArrayList<Lan6Expediente>();
                    listaExpedientesRelacionados.add(lan6ExpedienteRelacionado);
                    lan6Expediente.setListaExpedientesRelacionados(listaExpedientesRelacionados);
                }     
            }*/
            
            log.info("Expedientes relacionados");
            if(idProcedimiento.equals(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_RECUR, ConstantesMeLanbide43.FICHERO_ADAPTADORES)))
            {
                if(expe.getNumExpRel() != null && !expe.getNumExpRel().equals(""))
                {
                    log.info("Expediente relacionado: " + expe.getNumExpRel());
                    
                    Lan6Expediente lan6ExpedienteRelacionado = new Lan6Expediente();                  
                    lan6ExpedienteRelacionado.setNumero(expe.getNumExpRel());
                    lan6ExpedienteRelacionado.setDescripcion(expe.getNumExp() + " ==> " + expe.getNumExpRel());
                    lan6ExpedienteRelacionado.setDescripcionEu(expe.getNumExp() + " ==> " + expe.getNumExpRel());
                    lan6ExpedienteRelacionado.setEjercicio(expe.getEjercicioRel().toString());
                    String[] proc = expe.getNumExpRel().split("/");
                    
                
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    esDesarrolloCero = MeLanbide43DAO.getInstance().esDesarrolloCero(expe.getNumExpRel(), con);
                    if (esDesarrolloCero > 0){
                            idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    } else {
                            idProcedimiento = convierteProcedimiento(proc[1]);
                    }
                    //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
                    
                    
                    lan6ExpedienteRelacionado.setIdProcedimiento(idProcedimiento);
                    
                    ArrayList<Lan6Expediente> listaExpedientesRelacionados = new ArrayList<Lan6Expediente>();
                    listaExpedientesRelacionados.add(lan6ExpedienteRelacionado);
                    lan6Expediente.setListaExpedientesRelacionados(listaExpedientesRelacionados);

                    
                    servicios.relacionarExpedientes(lan6Expediente);
                }     
            }

            ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
            tramites.add(lan6Tramite);	

            //Llamada metodo    
            log.info("Procedemos a volcar la informaci�n de los tr�mites");
            servicios.actualizarTramites(tramites);
            log.info("volcado realizado");
            actualizarProcesados(gestion.getId(), adaptador);
    }
    catch(Lan6InformarConsultaExcepcion ice){
          codOperacion = "2";   
          ArrayList<String> codes = ice.getCodes();
          ArrayList<String> messages = ice.getMessages();

             String causa = "";//ice.getCausaExcepcion();
                causa = ice.getCausaExcepcion();

           StackTraceElement[] stacktrace=ice.getStackTrace();
           String error="";
           for (int i=0; i<codes.size();i++){
               error += messages.get(i);
               //error += codes.get(i);
           }

          try{
              int intentos = gestion.getIntentos() + 1;
          actualizarError(gestion.getId(), intentos, error, adaptador);
          }catch(Exception e){
              log.error("Error en la funci�n avanceExpConsulta: " + e.getMessage());

          }
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion avanzando expediente");
            errorB.setSituacion("avanceExpConsulta");


        MeLanbide43Manager.grabarError(errorB, error, causa, gestion.getNumExp());
        throw ice;
      }catch(Lan6Excepcion ex){ 
            codOperacion = "2";           
            log.error("Error en la funci�n avanceExpConsulta: " + ex.getException().getMessage());
            String error = "Error en la funci�n avanceExpConsulta: " + ex.getException().getMessage();
            
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_014");
            errorB.setMensajeError("Error avanzando expediente");
            errorB.setSituacion("avanceExpConsulta");

            MeLanbide43Manager.grabarError(errorB, ex.getException().getMessage().toString(), ex.getException().toString(), gestion.getNumExp());
            
            try{
                int intentos = gestion.getIntentos() + 1;
            actualizarError(gestion.getId(),intentos, error, adaptador);
            }catch(Exception e){
                log.error("Error en la funci�n actualizarError: " + e.getMessage());
             
            }
            throw ex;
        }   
    catch(Exception ex){
        codOperacion = "2";         
        ex.printStackTrace();
        log.error("Error en avanceExpConsulta - " + ex);
        try{
                int intentos = gestion.getIntentos() + 1;
            actualizarError(gestion.getId(), intentos,  "Error en avanceExpConsulta - " + ex, adaptador);
        }catch(Exception e){
            log.error("Error en la funci�n avanceExpConsulta: " + e.getMessage());
        }
        ErrorBean error = new ErrorBean();
        error.setIdError("MISGEST_014");
        error.setMensajeError("Error avanzando expediente");
        error.setSituacion("avanceExpConsulta");


        MeLanbide43Manager.grabarError(error, ex.getMessage()!=null?ex.getMessage().toString():"null", ex.toString(), gestion.getNumExp());
        throw ex;
    }
    finally{
        try
        {
            if(con!=null)con.close();
        }
        catch(Exception ex){log.error("Error en funcion avanceExpConsulta: " , ex);}
    }  
    return codOperacion;
      
  }
  
  public String cierreExpConsulta(FilaListadoMisGestiones gestion, String codOrg, AdaptadorSQLBD adaptador)throws Exception{
      String codOperacion = "0"; 
      Connection con = null;
      try
        {
            String expe = gestion.getNumExp();
            String[] pepe = expe.split("/");  

            
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            con = adaptador.getConnection();
            Integer esDesarrolloCero = MeLanbide43DAO.getInstance().esDesarrolloCero(expe, con);
            String idProcedimiento = "";
            if (esDesarrolloCero > 0){
                    idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            } else {
                    idProcedimiento = convierteProcedimiento(pepe[1]);
            }
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            
            
            Calendar hoy = Calendar.getInstance();
        	
            Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(idProcedimiento);
            Tramite tra = obtenerDatosTramite(pepe[1], gestion.getTramiteInicio(), adaptador);
        	
            //Expediente
            Expediente exp = obtenerDatosExpedientes(gestion.getNumExp(),adaptador);
            
            //datos expediente
            Lan6Expediente lan6Expediente = new Lan6Expediente();
            lan6Expediente.setNumero(expe);
            //hoy.setTime(exp.getFechaCierre());
            lan6Expediente.setFechaCierre(hoy);
            lan6Expediente.setEjercicio(pepe[0]);
            //lan6Expediente.setObservaciones(exp.getAsunto());
            //#495082S se acorta el asunto a longitud de 450 maximo
            int longMax=450;
            if (exp.getAsunto() != null) {
                String asunto = exp.getAsunto();
                if (asunto.length() > longMax) {
                    asunto = asunto.substring(0, longMax);
                }
                lan6Expediente.setObservaciones(asunto);
            }

            
            //DATOS DE TRAMITES                
            Lan6Tramite lan6Tramite = new Lan6Tramite();

            lan6Tramite.setId(tra.getCod());
            //lan6Tramite.setId("50");
            lan6Tramite.setDescripcion(tra.getDescripcion());
            lan6Tramite.setDescripcionEu(tra.getDescripcionEu());
            lan6Tramite.setIdExpediente(expe);
            lan6Tramite.setEjercicio(pepe[0]);
            lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_CERRAR_EXPEDIENTE);

            lan6Tramite.setFechaActualizacion(hoy);

            ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
            tramites.add(lan6Tramite);	

            lan6Expediente.setListaTramites(tramites);
            
            //Llamada metodo       	
            servicios.cerrarExpediente(lan6Expediente); 
            
            actualizarProcesados(gestion.getId(), adaptador);         
            
        }
     catch(Lan6InformarConsultaExcepcion ice){
          codOperacion = "2";          
          ArrayList<String> codes = ice.getCodes();
          ArrayList<String> messages = ice.getMessages();


             String causa = "";//ice.getCausaExcepcion();
                causa = ice.getCausaExcepcion();
           StackTraceElement[] stacktrace=ice.getStackTrace();
           String error="";
           for (int i=0; i<codes.size();i++){
               error += messages.get(i);
               //error += codes.get(i);
           }

          try{
            int intentos = gestion.getIntentos() + 1;
            error = error.substring(0, 1000);
            log.error("Error en  cierreExpConsulta: " + error);
            actualizarError(gestion.getId(), intentos, error, adaptador);
          }catch(Exception e){
              log.error("Error en la funci�n cierreConsulta: " + e.getMessage());
          }
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_017");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion cerrando expediente");
            errorB.setSituacion("cierreExpConsulta");


        MeLanbide43Manager.grabarError(errorB, error, causa, gestion.getNumExp());        
        if(!error.toString().contains("unique constraint (R02R.KR02R08T00) violated")){
            throw ice;
        }        
      }catch(Lan6Excepcion ex){  
            codOperacion = "2";           
            log.error("Error en la funci�n cierreExpConsulta: " + ex.getException().getMessage());
            String error = "Error en la funci�n cierreExpConsulta: " + ex.getException().getMessage();
            
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_016");
            errorB.setMensajeError("Error cerrando expediente");
            errorB.setSituacion("cierreExpConsulta");

            MeLanbide43Manager.grabarError(errorB, ex.getException().getMessage().toString(), ex.getException().toString(), gestion.getNumExp());
            
            try{
                int intentos = gestion.getIntentos() + 1;
            actualizarError(gestion.getId(),intentos, error, adaptador);
            }catch(Exception e){
                log.error("Error en la funci�n actualizarError: " + e.getMessage());             
            }                    
            throw ex;            
        }  
     catch(Exception ex)
    {
        codOperacion = "2"; 
        log.error("Error en la funci�n cierreExpConsulta: " + ex.getMessage());
        try{
            int intentos = gestion.getIntentos() + 1;
            actualizarError(gestion.getId(), intentos, "Error en cierreConsulta - " + ex, adaptador);
        }catch(Exception e){
            log.error("Error en la funcion cierreConsulta: " + e.getMessage());
        }
        ErrorBean errorB = new ErrorBean();
        errorB.setIdError("MISGEST_016");
        errorB.setMensajeError("Error cerrando expediente");
        errorB.setSituacion("cierreExpConsulta");


        MeLanbide43Manager.grabarError(errorB, ex.getMessage()!=null?ex.getMessage().toString():"null", ex.toString(), gestion.getNumExp());
        throw ex;        
    }
    finally{
        try
        {
            if(con!=null)con.close();
        }
        catch(Exception ex){log.error("Error en funcion cierreExpConsulta: " , ex);}
    }
    return codOperacion;
 }  
    
    
    public String convierteProcedimiento (String proc)
  {
      String idProc ="";
      try
      {       
        log.info("convierteProcedimiento: " + proc);
        if(proc.equals("QUEJA"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_QUEJA, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("CONCM"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CONC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("RGCF") || proc.equals("RGCFM") || proc.equals("RGCFB"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_REGC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("RGEF"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_REGE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("RECUR"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_RECUR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("AACCB"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AACCB, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("AACCR"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AACCR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("AACC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AACC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("CEI"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("SEI"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if (proc.equals("LEI"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("CUOTA") || proc.equals("CUOTS"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CUOTA, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("CEESC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CEECS, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("DISCP"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DISCP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("SUBAF"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SUBAF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("REPLE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_REPLE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("SUENC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SUENC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("UAAP"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_UAAP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);       
        else if(proc.equals("ORI14") || proc.equals("ORI"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ORI14, ConstantesMeLanbide43.FICHERO_ADAPTADORES);  
        else if(proc.equals("COLEC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_COLEC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("CEMP"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CEMP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("LEAUK")||proc.equals("LAK"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LEAUK, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("DECEX"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DECEX, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("COCUR"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_COCUR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("REJUV"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_REJUV, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("DLDUR"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DLDUR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("ECA"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ECA, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("CCEE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CCEE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("LAKCC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LAKCC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("ATASE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ATASE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("APEC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_APEC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("APES"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_APES, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("DISCT"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DISCT, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("ACASE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ACASE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("LPEEL"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LPEEL, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("LPEPE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LPEPE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("LPEAL"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LPEAL, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("GEL"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_GEL, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("AEXCE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AEXCE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("AERTE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AERTE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("LAKOF"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LAKOF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("ENTAP"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ENTAP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("IGCEE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_IGCEE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("SPROS"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SPROS, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("SAFCC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SAFCC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("SMUJF"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SMUJF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("SMUJC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SMUJC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("HEZFE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_HEZFE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("HEZFC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_HEZFC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("EHABE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_EHABE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("SFJBC"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SFJBC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("PEX"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_PEX, ConstantesMeLanbide43.FICHERO_ADAPTADORES);    
        else if(proc.equals("IKER"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_IKER, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("COLVU"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_COLVU, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("TRECO"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_TRECO, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("MCD"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_MCD, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("NNE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_NNE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("MRU"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_MRU, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("APEA"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_APEA, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("APEI"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_APEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("RYU"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_RYU, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("GO"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_GO, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("CEPAP"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CEPAP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("SUOPO"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SUOPO, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("ALDEM"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ALDEM, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("ININ"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ININ, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("OPDE"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_OPDE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("IPACR"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_IPACR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("AGRED"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AGRED, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("TF"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_TF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if(proc.equals("PREST"))
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_PREST, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if (proc.equals("IMV")) 
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_IMV, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if (proc.equals("RGI")) 
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_RGI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if (proc.equals("EADN")) 
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_EADN, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if (proc.equals("DLD50")) 
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DLD50, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if (proc.equals("AAEEF")) 
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AAEEF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
        else if (proc.equals("PRMEM")) 
            idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_PRMEM, ConstantesMeLanbide43.FICHERO_ADAPTADORES);        
        log.info("convierteProcedimiento, devolvemos: " + idProc);
      }
      catch(Exception ex) {}
      
      return idProc;
  }
    
    //metodo comun para aperturas y cierres de esperas
    public Lan6Tramite setLan6TramiteEsperas(String numExp, int codTramite, Lan6Tramite lan6Tramite) {
        
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Boolean conFechaLimitAportacion = false;
            String[] exp = numExp.split(BARRA);
            
            lan6Tramite.setIdExpediente(numExp);
            lan6Tramite.setEjercicio(exp[0]);
            
            lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
            
            //estaba as�, por defecto se toman estos para los que no tienen
            lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION);
            lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION_DESC);
            lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION_DESC);

            if(exp[1].equals("CEI")){
                int tramSeg = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEI_SEGUNDO_PAGO, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                int tramTer = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEI_TERCER_PAGO, ConstantesMeLanbide43.FICHERO_PROPIEDADES));

                log.info("tramSeg: " + tramSeg);
                log.info("tramTer: " + tramTer);
                // Segun tramite
                if(codTramite == tramSeg){//JUSTIFICACION CONTABLE PARA SEGUNDO PAGO
                    log.info("tramite 23");
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO2_LAN68_CEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));// Apertura del tramite para el segundo pago
                    Calendar ultimoDiaAno = Calendar.getInstance();
                    int anio = ultimoDiaAno.get(Calendar.YEAR);
                    ultimoDiaAno.set(anio,11,31);
                    lan6Tramite.setFechaLimiteAportacion(ultimoDiaAno);
                }
                if(codTramite == tramTer){//JUSTIFICACION CONTABLE PARA TERCER PAGO
                    log.info("tramite 54");
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO3_LAN68_CEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));// Apertura del tramite para el tercer pago
                }
            }                   
            else if(exp[1].equals("SEI")){
                if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_PRIMER_PAGO, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO1_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_1SEM_2AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM1_ANU2_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_2SEM_2AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM2_ANU2_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_1SEM_3AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM1_ANU3_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_2SEM_3AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM2_ANU3_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_JUS_FINAL, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_FINAL_ANU3_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_1SEM_4AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM1_ANU4_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_2SEM_4AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_SEM2_ANU4_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_JUS_FIN_4AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO_FINAL_ANU4_LAN68_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }/*else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_SEI_2SEM_2AN, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(Lan6Constantes.ID_INSTANCIA_PAGO1_FOR_SEI);
                }*/
            }
            else if(exp[1].equals("REPLE")){
                int tramRecepDocpago2 = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_REPLE_RECEPDOC_PAGO2, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                int tramRecepDocPlantilla = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_REPLE_RECEP_PLANTILLA, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                if(codTramite == tramRecepDocpago2){// Tramite 399 – Recepcion telematica documentacion 2º pago
                    log.info("tramite 399");
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PAGO2_LAN62_REPLE, ConstantesMeLanbide43.FICHERO_ADAPTADORES));                           
                }
                if(codTramite == tramRecepDocPlantilla){
                    log.info("tramite 499");
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_PLANTILLA_LAN62_REPLE, ConstantesMeLanbide43.FICHERO_ADAPTADORES));                            
                }
            }                    
            else if(exp[1].equals("UAAP")){
                if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_UAAP_DOCU_AVAL, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_DOC_AVAL_LAN62_UAAP, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_UAAP_DOCU_LIQUIDA, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    lan6Tramite.setInstanciaId(ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_DOC_LIQ_LAN62_UAAP, ConstantesMeLanbide43.FICHERO_ADAPTADORES));
                }
            }
            else if(exp[1].equals("ORI14") || exp[1].equals("ORI")){
                //log.info("procedimiento ORI14- tramite:"+codTramite);
                log.info("procedimiento "+exp[1]+"- tramite:"+codTramite);

                if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI14_ESP_RECEP_DOC_SUBS_SOL, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 192:
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_SUBSANACION);
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI14_ESP_APER_DOC_SOL_ALTA, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 400:
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_ALTA_PER_ORI14, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);                        
                } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI14_ESP_APER_DOC_JUSTIF, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
                    //TRAMITE 6: 
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_DOC_TRAM_ORI14, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI14_ESP_RECEP_DOC_INI_ACT, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 51:
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_INI_ACTIVIDAD_ORI14, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);                        
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI14_ESP_RECEP_SOL_DEC_INI_ACT, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 181:
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_SUB_INI_ACTIVIDAD_LAN62_ORI14, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_SUBSANACION);
                    lan6Tramite.setInstanciaId(instancia);                        
                } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI_ESP_RECEP_DOC_ALEG_RES_PROV, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
                    //TRAMITE 53:
                    conFechaLimitAportacion = true;
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_ALEGACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_ALEGACION_DOC_DESC);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_ALEGACION);
    //                            String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_DOC_ALEG_ORI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_DOC_TRAM_ORI14, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setInstanciaId(instancia); 
                    // Fecha Limite Aportacion
                    String fechaStr = ConfigurationParameter.getParameter(exp[1] + BARRA + exp[0]+ BARRA + codTramite, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    log.debug("Fecha limite aportacion: " + fechaStr);
                    Calendar fechaLimiteAportacion = Calendar.getInstance();                            
                    fechaLimiteAportacion.setTime(dateFormat.parse(fechaStr));
                    lan6Tramite.setFechaLimiteAportacion(fechaLimiteAportacion);
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI_ESPERA_PRESENTACION_RECURSOS, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 9099:
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_RECURSO_POTESTATIVO_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_RECURSO_POTESTATIVO_DOC_DESC);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_RECURSO_POTESTATIVO);
                }
            }                
            else if (exp[1].equals("AERTE")){
                // Solo usamos para abrir Presentacion de recursos -Invocacio
                log.info("procedimiento AERTE - tramite:"+codTramite);
                if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_AERTE_ESPERA_PRESENTACION_RECURSOS, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 9099:
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_RECURSO_POTESTATIVO);
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_RECURSO_POTESTATIVO_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_RECURSO_POTESTATIVO_DOC_DESC);
                    //String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_DOC_TRAM_ORI14, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    //lan6Tramite.setInstanciaId(instancia);
                    
                }
            }
            else if(exp[1].equals("CEMP")){
                log.info("procedimiento CEMP- tramite:"+codTramite);

                if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ESP_RECEP_DOC_SUBS_SOL, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 192:
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_SUBSANACION);
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ESP_APER_DOC_SOL_ALTA, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 400:
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_ALTA_PER_CEMP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);                        
                } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ESP_APER_DOC_JUSTIF, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
                    //TRAMITE 6: 
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_DOC_TRAM_CEMP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ESP_RECEP_DOC_INI_ACT, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 51: 
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_INI_ACTIVIDAD_CEMP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);                        
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ESP_RECEP_SOL_DEC_INI_ACT, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 181: 
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_SUB_INI_ACTIVIDAD_LAN62_CEMP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_SUBSANACION);
                    lan6Tramite.setInstanciaId(instancia);                        
                } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ESP_RECEP_DOC_ALEG_RES_PROV, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
                    //TRAMITE 53:
                    conFechaLimitAportacion = true;
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_ALEGACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_ALEGACION_DOC_DESC);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_ALEGACION);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_DOC_TRAM_CEMP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setInstanciaId(instancia); 
                    // Fecha Limite Aportacion
                    String fechaStr = ConfigurationParameter.getParameter(exp[1] + BARRA + exp[0]+ BARRA + codTramite, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    log.debug("Fecha limite aportaci�n: " + fechaStr);
                    Calendar fechaLimiteAportacion = Calendar.getInstance();                            
                    fechaLimiteAportacion.setTime(dateFormat.parse(fechaStr));
                    lan6Tramite.setFechaLimiteAportacion(fechaLimiteAportacion);
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ESPERA_PRESENTACION_RECURSOS, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 9099:
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_RECURSO_POTESTATIVO_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_RECURSO_POTESTATIVO_DOC_DESC);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_RECURSO_POTESTATIVO);                     
                }
            }
            else if(exp[1].equals("COLEC")){
                log.info("procedimiento COLEC- tramite:"+codTramite);

                if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ESP_RECEP_DOC_SUBS_SOL, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 192:
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_SUBSANACION);
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ESP_APER_DOC_SOL_ALTA, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 400:
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_ALTA_PER_COLEC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);                        
                } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ESP_APER_DOC_JUSTIF, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
                    //TRAMITE 6: 
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_DOC_TRAM_COLEC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ESP_RECEP_DOC_INI_ACT, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 51: 
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_APORTACION_DOC_DESC);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_INI_ACTIVIDAD_COLEC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);                        
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ESP_RECEP_SOL_DEC_INI_ACT, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 181: 
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_SUBSANACION_DOC_DESC);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_SUB_INI_ACTIVIDAD_LAN62_COLEC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_SUBSANACION);
                    lan6Tramite.setInstanciaId(instancia);                        
                } else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ESP_RECEP_DOC_ALEG_RES_PROV, ConstantesMeLanbide43.FICHERO_PROPIEDADES))) {
                    //TRAMITE 53:
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_ALEGACION_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_ALEGACION_DOC_DESC);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_ALEGACION);
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_DOC_TRAM_COLEC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setInstanciaId(instancia); 
                    // Fecha Limite Aportacion
                    conFechaLimitAportacion = true;
                    String fechaStr = ConfigurationParameter.getParameter(exp[1] + BARRA + exp[0]+ BARRA + codTramite, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                    log.debug("Fecha limite aportaci�n: " + fechaStr);
                    Calendar fechaLimiteAportacion = Calendar.getInstance();
                    fechaLimiteAportacion.setTime(dateFormat.parse(fechaStr));
                    lan6Tramite.setFechaLimiteAportacion(fechaLimiteAportacion);
                } else if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ESPERA_PRESENTACION_RECURSOS, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //TRAMITE 9099:
                    lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_RECURSO_POTESTATIVO_DOC_DESC);
                    lan6Tramite.setDescripcionEu(Lan6Constantes.TRAMITE_RECURSO_POTESTATIVO_DOC_DESC);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_RECURSO_POTESTATIVO);                       
                }

            }
            else if(exp[1].equals("DECEX")){
                log.info("procedimiento DECEX- tr�mite:" + codTramite);
                if(codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_DECEX_RECEP_MODIF, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //String instancia=Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "ID_INSTANCIA_APOR_MOD_LAN62_DECEX");
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_MOD_LAN62_DECEX, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_DECEX_RECEP_REG_ENT_3M, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //String instancia=Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "ID_INSTANCIA_APOR_3MESES_LAN62_DECEX");
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_3MESES_LAN62_DECEX, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);
                }else if (codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_DECEX_RECEP_REG_ENT_A1, ConstantesMeLanbide43.FICHERO_PROPIEDADES)) || 
                          codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_DECEX_RECEP_REG_ENT_A2, ConstantesMeLanbide43.FICHERO_PROPIEDADES)) ||
                          codTramite == Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_DECEX_RECEP_REG_ENT_A3, ConstantesMeLanbide43.FICHERO_PROPIEDADES))){
                    //String instancia=Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, " ID_INSTANCIA_APOR_ANU1_LAN62_DECEX ");
                    String instancia=ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_INSTANCIA_APOR_ANU1_LAN62_DECEX, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                    lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION+"_"+instancia);
                    lan6Tramite.setInstanciaId(instancia);
                }
            }
            if (conFechaLimitAportacion){
                log.info("fecha limite aportacion : " + lan6Tramite.getFechaLimiteAportacion());
            }
        } catch (Exception e) {
            log.error("Error al llamar a setLan6TramiteEsperas()" , e);
        }
        
        return lan6Tramite;
    }
    
      public String justificantesPagos (int codOrganizacion,int codTramite,String numExp, AdaptadorSQLBD adaptador) throws Exception{
        String mensaje = "0";
        Connection con = null;
       try{
            log.info("justificantesPagos BEGIN: " + numExp);
            
            //String idProcedimiento = Lan6Constantes.ID_PROC_CEI;
            Calendar hoy = Calendar.getInstance();
            String[] exp = numExp.split("/");
            
            
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            con = adaptador.getConnection();
            Integer esDesarrolloCero = MeLanbide43DAO.getInstance().esDesarrolloCero(numExp, con);
            String idProcedimiento = "";
            if (esDesarrolloCero > 0){
                    idProcedimiento = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DESARROLLO_CERO, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            } else {
                    idProcedimiento = MeLanbide43Manager.getInstance().convierteProcedimiento(exp[1]);
            }
            //DESARROLLO 0_______________________________________________________________________________________________________________________DESARROLLO 0
            
            
            //Boolean conFechaLimitAportacion = false;
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String fechaIni = fechaInicioExpediente(numExp, adaptador);                
            date = dateFormat.parse(fechaIni);
            String fecha = MeLanbide43Manager.getInstance().verificarFecha(exp[1], adaptador);
            Date fec = null;
            if(fecha != ""){
                fec = dateFormat.parse(fecha);
                if(date.after(fec)){ 
                    Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(idProcedimiento);
                    
                    Lan6Tramite   lan6Tramite = new Lan6Tramite(); 
                    //lan6Tramite.setId(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION);
                    //lan6Tramite.setDescripcion(Lan6Constantes.TRAMITE_ESPERA_REQUERIMIENTO_APORTACION_DESC);
                    //lan6Tramite.setTipoTramite(Lan6Constantes.TRAMITE_ESPERA);
                    
                    log.info("exp: " + exp[1]);
                    log.info("codTramite: " + codTramite);
                    
                    lan6Tramite = setLan6TramiteEsperas(numExp, codTramite, lan6Tramite);
                    
                    //Datos del expediente
                    //lan6Tramite.setEjercicio(exp[0]);
                    //lan6Tramite.setIdExpediente(numExp);                     
                    lan6Tramite.setFechaActualizacion(hoy);
                    
                    ArrayList<Lan6Tramite> tramites = new ArrayList<Lan6Tramite>();
                    tramites.add(lan6Tramite);     
                    
                    //Llamada metodo     
                    log.info("Antes de llamar a actualizarTramites");
                    //DATOS
                    log.info("ejercicio: "+lan6Tramite.getEjercicio());
                    log.info("expediente: "+lan6Tramite.getIdExpediente());                     
                    log.info("fecha Actualizacion: "+lan6Tramite.getFechaActualizacion().toString());
                    log.info("id: "+lan6Tramite.getId());
                    log.info("tipoTramite:"+lan6Tramite.getTipoTramite());
                    log.info("desc:"+lan6Tramite.getDescripcion());
                    log.info("InstanciaId:"+lan6Tramite.getInstanciaId());
                    /*if (conFechaLimitAportacion){
                        log.info("fecha limite aportacion : "+lan6Tramite.getFechaLimiteAportacion());
                    }*/
                    String res = servicios.actualizarTramites(tramites);
                    

                }
            }
            if(log.isDebugEnabled()) log.debug("justificantesPagos() : END");
        }catch(Lan6InformarConsultaExcepcion ice){
            
            ArrayList<String> codes = ice.getCodes();
            ArrayList<String> messages = ice.getMessages();

            
             StackTraceElement[] stacktrace=ice.getStackTrace();
             String error="";
             String causa = "";//ice.getCausaExcepcion();
                causa = ice.getCausaExcepcion();
             for (int i=0; i<codes.size();i++){
                 error += messages.get(i);
                 //error += codes.get(i);
             }
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error Lan6InformarConsultaExcepcion al actualizar tramite");
            errorB.setSituacion("justificantesPagos");

            MeLanbide43Manager.grabarError(errorB, error, causa, numExp);
            //throw ice;
        }catch(Lan6Excepcion ex){            
            log.error("Error en la funci�n justificantesPagos: " , ex);
            String error = "Error en la funci�n justificantesPagos: " + ex.getException().getMessage().toString();
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error al actualizar tramite");
            errorB.setSituacion("justificantesPagos");

            grabarError(errorB, ex.getException().getMessage().toString(), ex.getException().toString(), numExp);
            
            //throw ex;
        }     
        catch(Exception ex)
        {
            //meLanbide43Manager.insertarError(ex.getMessage().toString(), "iniciarExpConsulta", Integer.parseInt(pepe[2]),null);
            log.error("Error en la funcion justificantesPagos: " , ex);
            
            ErrorBean errorB = new ErrorBean();
            errorB.setIdError("MISGEST_015");
            errorB.setMensajeError("Error al actualizar tramite");
            errorB.setSituacion("justificantesPagos");

            grabarError(errorB, ex.getMessage()!=null?ex.getMessage().toString():"null", ex.toString(), numExp);
            
            throw ex;
        }
//       catch(Exception x)
//       {
//           log.error("Error en justificantesPagos: " + x.toString());
//           mensaje = "2";
//           throw x;
//       }
        finally{
            try
            {
                if(con!=null)con.close();
            }
            catch(Exception ex){log.error("Error en funcion justificantesPagos: " , ex);}
        }
        return mensaje;
    }
   
    public String esperaAportDocPostal (int codOrganizacion,int codTramite,String numExp, AdaptadorSQLBD adaptador) throws Exception{
        String mensaje = "0";      
        log.info("esperaAportDocPostal BEGIN: " + numExp);
         Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            
            boolean esPostal = !melanbide43DAO.comprobarTerceroTramElectronica(numExp,con);
            if(con!=null) {con.close();}
            log.debug("esPostal: " + esPostal);
            if (esPostal){
                mensaje=justificantesPagos (codOrganizacion,codTramite, numExp, adaptador);
            }
        }
        catch (Exception ex){
            throw ex;
        }
        finally{
            try
            {
                if(con!=null)con.close();
            }
            catch(Exception ex){log.error("Error en funcion verificarFecha: " , ex);}
        } 
        return mensaje;
    }
   
    public Boolean notificElec(String numExpediente, AdaptadorSQLBD adaptador)
    {
        Boolean notif = false;
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide41DAO = MeLanbide43DAO.getInstance();   
            notif =  MeLanbide43DAO.getInstance().notificadoElec(numExpediente, con);
        }
        catch(Exception ex){ 
            log.error("Error en funcion notificElec: " + ex);
        }
        
        finally{
            try
            {
                if(con!=null)con.close();
            }
            catch(Exception ex){log.error("Error en funcion notificElec: " , ex);}
        }
        return notif;
    }
    
    public Boolean expedienteCerrado(String numExpediente, AdaptadorSQLBD adaptador)
    {
        Boolean notif = false;
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide41DAO = MeLanbide43DAO.getInstance();   
            notif =  MeLanbide43DAO.getInstance().getExpedienteCerrado(numExpediente, con);
        }
        catch(Exception ex){ 
            log.error("Error en funcion expedienteCerrado: " , ex);
        }
        finally{
            try
            {
                if(con!=null)con.close();
            }
            catch(Exception ex){log.error("Error en funcion expedienteCerrado: " , ex);}
        }
        return notif;
    }
    
    
    public Boolean expInstanciaParte(String numExpediente, AdaptadorSQLBD adaptador)
    {
        Boolean notif = false;
        Connection con = null;
        try {
            con = adaptador.getConnection();
            //MeLanbide43DAO meLanbide41DAO = MeLanbide43DAO.getInstance();   
            notif =  MeLanbide43DAO.getInstance().expInstanciaParte(numExpediente, con);
            
            if(con!=null)con.close();
        }
        catch(Exception ex){ 
            log.error("Error en funcion expInstanciaParte: " + ex);
        }
        finally{
            try
            {
                if(con!=null)con.close();
            }
            catch(Exception ex){log.error("Error en funcion expInstanciaParte: " , ex);}
        }
        return notif;
    }
    
    public ArrayList<FilaListadoMisGestiones> getInfoGestiones(String numExp, int codOrganizacion,AdaptadorSQLBD adaptador, int codTramite, String evento) throws Exception{
        Connection con = null;         
        int result = 0; int result2 = 0;
        boolean transactionStarted = false;
        ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            misGestiones = meLanbide43DAO.getInfoGestiones(numExp, codTramite, evento, con);
            
        } catch(BDException e){
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepci�n en la BBDD en getInfoGestiones para el expediente " + numExp, e);
            throw new Exception(e);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        return misGestiones;
    }
    public String guardarGestiones(String numExp, int codOrganizacion,AdaptadorSQLBD adaptador, int codTramite, String evento) throws Exception{
         //boolean correcto=false;
        log.debug("guardarGestiones BEGIN");
        String resultado =null;
         String clave=null;                     
         Connection con = null;         
         int result = 0; int result2 = 0;
         boolean transactionStarted = false;
         int id = 0;
         ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();
         if(numExp!=null && !"".equals(numExp)){
            String[] datos          = numExp.split("/");
            String ejercicio        = datos[0];        
            try{
                con = adaptador.getConnection();
                MeLanbide43DAO meLanbide41DAO = MeLanbide43DAO.getInstance();                
                result2 = meLanbide41DAO.guardarMiGestion(numExp, codTramite, evento, con);
                if(result2 != 1){
                    adaptador.rollBack(con);
                    return "4";//fallo al actualizar cntador
                }
                id = meLanbide41DAO.getIdGestiones(con);
            } catch(BDException e){
                if(transactionStarted)
                {
                    adaptador.rollBack(con);
                }
                log.error("Se ha producido una excepci�n en la BBDD guardando gestiones para el expediente " + numExp, e);
                //throw new Exception(e);
            }catch (Exception e){
               
                //throw e;
            }finally {
                log.debug("guardarGestiones end");
                try
                {
                    adaptador.devolverConexion(con);       
                }
                catch(Exception e)
                {
                    log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
                }
            }
        }           
         
        return String.valueOf(id); 
        
     }
        
    public String guardarQuartz(AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false;
         String resultado =null;
         String clave=null;                     
         Connection con = null;         
         int result = 0; int result2 = 0;
         boolean transactionStarted = false;
         ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();

            try{
                con = adaptador.getConnection();
                MeLanbide43DAO meLanbide41DAO = MeLanbide43DAO.getInstance();                
                result2 = meLanbide41DAO.guardarConQuartz(con);
                if(result2 != 1){
                    adaptador.rollBack(con);
                    return "4";//fallo al actualizar cntador
                }
            } catch(BDException e){
                if(transactionStarted)
                {
                    adaptador.rollBack(con);
                }
                log.error("Se ha producido una excepci�n en la BBDD guardando la clave registral APA para el expediente ", e);
                throw new Exception(e);
            }catch (Exception e){
               
                throw e;
            }finally {
                try
                {
                    adaptador.devolverConexion(con);       
                }
                catch(Exception e)
                {
                    log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
                }
            }     
         
        return resultado; 
        
     }
    public void borrarProcesado(String id, AdaptadorSQLBD adaptador)
    {
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO misgestDAO = MeLanbide43DAO.getInstance();                
            misgestDAO.borrarProcesado(id, con);
        }catch (Exception e){
            log.error("Error al borrar los procesados: " + e.getMessage());
            //throw e;
        }finally {
            try
            {
                if (con != null) con.close();         
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public ArrayList<FilaListadoMisGestiones> obtenerDatosGestiones(String id, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false;
         String resultado =null;
         String clave=null;                     
         Connection con = null;         
         int result = 0; int result2 = 0;
         boolean transactionStarted = false;
         ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();  
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO misgestDAO = MeLanbide43DAO.getInstance();                
            misGestiones = misgestDAO.selectProcesados(id, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();        
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        return misGestiones; 
    }
    
    public void actualizarProcesados(int id, AdaptadorSQLBD adaptador) throws Exception{           
         Connection con = null;         
         ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();  
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO misgestDAO = MeLanbide43DAO.getInstance();                
            misgestDAO.actualizarProcesados(id, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();         
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public void actualizarError(int id, int intentos,  String error,  AdaptadorSQLBD adaptador) throws Exception{           
         Connection con = null;         
         ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();  
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO misgestDAO = MeLanbide43DAO.getInstance();                
            misgestDAO.actualizarError(id, intentos, error, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();          
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Expediente obtenerDatosExpedientes(String numExpediente, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false         
         Connection con = null;         
         Expediente exp = new Expediente();  
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();                
            exp = melanbide43DAO.leerDatosExp(numExpediente, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();        
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        return exp; 
    }
    
    public void insertarError(String ex, String funcion, int id, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false         
         Connection con = null;         
         Expediente exp = new Expediente();  
        try{
            con = adaptador.getConnection();             
            MeLanbide43DAO.getInstance().insertaError(con, ex, id, funcion);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();          
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Tramite obtenerDatosTramite(String proced, String codTram, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false         
         Connection con = null;         
         Tramite exp = new Tramite();  
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();                
            exp = melanbide43DAO.leerDatosTramite(proced, codTram, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();        
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        return exp; 
    }
    
    public String obtenerJustificante(String numReg, String ano, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false         
         Connection con = null;         
         String oid =""; 
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();                
            oid = melanbide43DAO.leerJustificante(numReg, ano, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();         
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        return oid; 
    }
    
    public ArrayList<String> obtenerDocAportados(String numReg, String ano, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false         
         Connection con = null;         
         ArrayList<String> lista = new ArrayList<String>(); 
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();                
            lista = melanbide43DAO.leerDocumentosAportados(numReg, ano, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();         
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        return lista; 
    }
    
    public ArrayList<String> obtenerDocumentosExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false         
         Connection con = null;         
         ArrayList<String> oid =new ArrayList<String>(); 
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();                
            oid = melanbide43DAO.leerDocumentoExpediente(numExpediente, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();          
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        return oid; 
    }
    
    public Participantes obtenerDatosParticipantes(String numExpediente, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false         
         Connection con = null;         
         Participantes exp = new Participantes();  
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();                
            exp = melanbide43DAO.leerDatosParticipantes(numExpediente, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();          
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        return exp; 
    }
    
    public ArrayList<Participantes> obtenerListaParticipantes(String numExpediente, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false         
         Connection con = null;         
         ArrayList<Participantes>  exp = new ArrayList<Participantes> ();  
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();                
            exp = melanbide43DAO.leerListaParticipantes(numExpediente, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();         
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        return exp; 
    }
    
    public static void grabarError(ErrorBean error, String excepError, String traza, String numExpediente){
        try
        {
            log.error("grabando el error");
            error.setMensajeExcepError(excepError);
            error.setTraza(excepError);
            error.setCausa(traza);
            log.error("causa: "+traza);
            log.error("numExpediente: "+numExpediente);
            if("".equals(numExpediente))
                numExpediente = "0000/ERRMISGEST/000000";
            String valor[] = numExpediente.split("/");
            log.error("valor.length: "+valor.length);
            String idProc = "";
            if(valor.length > 1){
                if(valor[1].equals("QUEJA"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_QUEJA, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("CONCM"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CONC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("RGCF") || valor[1].equals("RGCFM") || valor[1].equals("RGCFB"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_REGC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("RGEF"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_REGE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("RECUR"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_RECUR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("AACC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AACC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("AACCB"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AACCB, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("AACCR"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AACCR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("CEI"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("SEI"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("LEI"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("CUOTA") || valor[1].equals("CUOTS"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CUOTA, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("CEESC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CEECS, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("DISCP"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DISCP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("SUBAF"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SUBAF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("REPLE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_REPLE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("SUENC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SUENC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("UAAP"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_UAAP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);       
                else if(valor[1].equals("ORI14") || valor[1].equals("ORI"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ORI14, ConstantesMeLanbide43.FICHERO_ADAPTADORES);  
                else if(valor[1].equals("COLEC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_COLEC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("CEMP"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CEMP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("LEAUK")||valor[1].equals("LAK"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LEAUK, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("DECEX"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DECEX, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("COCUR"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_COCUR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("REJUV"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_REJUV, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("DLDUR"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DLDUR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("ECA"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ECA, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("CCEE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CCEE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("LAKCC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LAKCC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("ATASE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ATASE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("APEC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_APEC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("APES"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_APES, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("DISCT"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DISCT, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("ACASE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ACASE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("LPEEL"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LPEEL, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("LPEPE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LPEPE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("LPEAL"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LPEAL, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("GEL"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_GEL, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("AEXCE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AEXCE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("AERTE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AERTE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("LAKOF"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_LAKOF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("ENTAP"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ENTAP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("IGCEE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_IGCEE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("SPROS"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SPROS, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("SAFCC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SAFCC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("SMUJF"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SMUJF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("SMUJC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SMUJC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("HEZFE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_HEZFE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("HEZFC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_HEZFC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("EHABE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_EHABE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("SFJBC"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SFJBC, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("PEX"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_PEX, ConstantesMeLanbide43.FICHERO_ADAPTADORES);    
                else if(valor[1].equals("IKER"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_IKER, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("COLVU"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_COLVU, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("TRECO"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_TRECO, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("MCD"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_MCD, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("NNE"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_NNE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("MRU"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_MRU, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("APEA"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_APEA, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if(valor[1].equals("APEI"))
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_APEI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("RYU")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_RYU, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("GO")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_GO, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("CEPAP")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_CEPAP, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("SUOPO")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_SUOPO, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("ALDEM")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ALDEM, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("ININ")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_ININ, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("OPDE")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_OPDE, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("IPACR")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_IPACR, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("AGRED")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AGRED, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("TF")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_TF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("PREST")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_PREST, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("IMV")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_IMV, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("RGI")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_RGI, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("EADN")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_EADN, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("DLD50")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_DLD50, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("AAEEF")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_AAEEF, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
                else if (valor[1].equals("PRMEM")) 
                    idProc = ConfigurationParameter.getParameter(ConstantesMeLanbide43.ID_PROC_PRMEM, ConstantesMeLanbide43.FICHERO_ADAPTADORES);
            }
            log.error("procedimiento: " + idProc);
            error.setIdProcedimiento(idProc);
            error.setIdClave("");
            error.setSistemaOrigen("REGEXLAN");
            error.setErrorLog("flexia_debug");
            error.setIdFlexia(numExpediente);
            log.error("Vamos a registrar el error");
            
            String respuestaGrabarError = RegistroErrores.registroError(error);
            log.error("respuestaGrabarError RegistroErrores.registroError() " + respuestaGrabarError);

            //ErroresDAO.getInstance().insertaRegistroError(error, con);
        }catch(Exception ex)
        {
            log.error("Error al grabarError" + ex);
        }
    }
    
    public String verificarFecha(String procedimiento, AdaptadorSQLBD adaptador)throws Exception{
        String fecha= "";
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            
            fecha = melanbide43DAO.verificarFecha(con, procedimiento);
            if(con!=null) con.close();
        }
        catch (Exception ex){
            throw ex;
        }
        
        finally{
            try
            {
                if(con!=null)con.close();
            }
            catch(Exception ex){log.error("Error en funcion verificarFecha: " , ex);}
        }
        return fecha;
    }
    
    public String obtenerUsuarioUltimoTramite(String numExpediente, AdaptadorSQLBD adaptador)throws Exception{
        String usuario= "";
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            
            usuario = melanbide43DAO.obtenerUsuarioUltimoTramite(con, numExpediente);
            if(con!=null) con.close();
        }
        catch (Exception ex){
            throw ex;
        }
        
        finally{
            try
            {
                if(con!=null)con.close();
            }
            catch(Exception ex){log.error("Error en funcion obtenerUsuarioUltimoTramite: " , ex);}
        }
        return usuario;
    }
    
    public String fechaInicioExpediente(String procedimiento, AdaptadorSQLBD adaptador)throws Exception{
        String fecha= "";
        Connection con = null; 
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            
            fecha = melanbide43DAO.fechaInicioExp(con, procedimiento);
            
        }
        catch (Exception ex){
            throw ex;
        }
        
        finally{
            try
            {
                if(con!=null)con.close();
            }
            catch(Exception ex){log.error("Error en funcion fechaInicioExpediente: " , ex);}
        }
        return fecha;
    }
    /**
     * Devuelve una instancia de MeLanbide43Manager, si no existe la crea.
     * @return 
     */
    public static MeLanbide43Manager getInstance(){
        if(instance == null){
            synchronized(MeLanbide43Manager.class){
                if(instance == null){
                    instance = new MeLanbide43Manager();
                }//if(instance == null)
            }//synchronized(MeLanbide43Manager.class)
        }//if(instance == null)
        return instance;
    }//getInstance

//#233079 Mis gestiones - Mantenimiento tabla de fases - principio
public List<ProcedimientoVO> getProcedimientos(AdaptadorSQLBD adaptador) throws Exception
    {
        log.info(" Entrando a Manager.GetProcedimientos");
        Connection con = null;
        try
        {
            if(adaptador != null){
                log.info(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
                con = adaptador.getConnection();
            }
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            return meLanbide43DAO.getProcedimientos(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos de Procedimientos ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Procedimientos ", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    } 

    public List<FilaFaseVO> getListaFasesporProcedimiento(String codProc, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            return meLanbide43DAO.getListaFasesporProcedimiento(codProc, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando fases del Procedimiento: "+codProc, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando fases del Procedimiento: "+codProc, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public FaseVO getFaseProcedimientoTramiteyFase(String codProc, String codTram, String codFase, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            return meLanbide43DAO.getFaseProcedimientoTramiteyFase(codProc, codTram, codFase, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando fase para Procedimiento "+codProc+ " Tr�mite "+codTram + " y Fase "+codFase, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando fase para Procedimiento "+codProc+ " Tr�mite "+codTram+ " y Fase "+codFase, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
       
    public FaseVO guardarFaseVO(int codOrganizacion, FaseVO fase, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            boolean nuevo = fase != null && fase.getCodFase() != null ? true : false;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            fase = meLanbide43DAO.guardarFaseVO(fase, con);
            if(fase != null)
            {
                if(nuevo)
                {
                adaptador.finTransaccion(con);
                return fase;
                }
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando fase "+(fase != null ? fase.getCodFase() : "(fase = null)")+" para el procedimiento "+(fase != null ? fase.getCodProcedimiento() : "(fase = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando fase "+(fase != null ? fase.getCodFase() : "(fase = null)")+" para el procedimiento "+(fase != null ? fase.getCodProcedimiento() : "(fase = null)"), ex);
            adaptador.rollBack(con);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
        return fase;
    }
    
    public int eliminarFaseVO(int codOrganizacion, FaseVO fase, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            if(fase != null)
            {
                int eliminados = meLanbide43DAO.eliminarFase(fase, con);
                if(eliminados > 0)
                {
                    adaptador.finTransaccion(con);   
                    return eliminados;
                }
                else
                {
                    throw new BDException();
                }
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando fase "+(fase != null ? fase.getCodFase() : "(fase = null)")+" para el procedimiento "+(fase != null ? fase.getCodProcedimiento() : "(fase = null)")+" y el tramite "+(fase != null ? fase.getCodTramite() : "(fase = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando fase "+(fase != null ? fase.getCodFase() : "(fase = null)")+" para el procedimiento "+(fase != null ? fase.getCodProcedimiento() : "(fase = null)")+" y el tramite "+(fase != null ? fase.getCodTramite() : "(fase = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int modificarFaseVO(int codOrganizacion, FaseVO fase, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            if(fase != null)
            {
                int modificados = meLanbide43DAO.modificarFase(fase, con);
                if(modificados > 0)
                {
                    adaptador.finTransaccion(con);   
                    return modificados;
                }
                else
                {
                    throw new BDException();
                }
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD modificando fase "+(fase != null ? fase.getCodFase() : "(fase = null)")+" para el procedimiento "+(fase != null ? fase.getCodProcedimiento() : "(fase = null)")+" y el tramite "+(fase != null ? fase.getCodTramite() : "(fase = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD modificando fase "+(fase != null ? fase.getCodFase() : "(fase = null)")+" para el procedimiento "+(fase != null ? fase.getCodProcedimiento() : "(fase = null)")+" y el tramite "+(fase != null ? fase.getCodTramite() : "(fase = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<TramiteVO> getTramites(String codProc, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            if(adaptador != null){
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
                con = adaptador.getConnection();
            }
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            return meLanbide43DAO.getTramites(codProc, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos de Tramites ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Tramites ", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    } 
    public TramiteVO getDatosTramiteXcodigoVisibleYExpediente(String codProc, String codigoVisibleTramite,String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            if(adaptador != null){
                con = adaptador.getConnection();
            }
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            return meLanbide43DAO.getDatosTramiteXcodigoVisibleYExpediente(codProc,codigoVisibleTramite,numExpediente,con);
        }
        catch(BDException e)
        {
            log.error("getTramiteXcodigoVisible - e ha producido una excepcion en la BBDD recuperando Datos de Tramite ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("getTramiteXcodigoVisible - Se ha producido una excepcion en la BBDD recuperando Tramite ", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    } 
    //#233082 Consulta y reenvío de Notificaciones - inicio  
   
    public List<FilaNotificacionVO> getListaNotificaciones(String codProc, String numExped, String fecDesde, String fecHasta, String resultado, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            return meLanbide43DAO.getListaNotificaciones(codProc, numExped, fecDesde, fecHasta, resultado, con);
        }

        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando notificaciones del Procedimiento: "+codProc, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando notificaciones del Procedimiento: "+codProc, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public FilaNotificacionVO getNotificacion(String codNotif, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            return meLanbide43DAO.getNotificacion(codNotif, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando Notificacion "+codNotif, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando Notificacion "+codNotif, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int reenviarNotificacion(int codOrganizacion, FilaNotificacionVO notif, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            if(notif != null)
            {   
                int reenviado = meLanbide43DAO.reenviarNotificacion(notif, con);
                if(reenviado > 0)
                {
                    adaptador.finTransaccion(con);   
                    return reenviado;
                }
                else
                {
                    throw new BDException();
                }
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD modificando notificacion "+(notif != null ? notif.getCodNotif() : "(notif = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD modificando notificacion  "+(notif != null ? notif.getCodNotif() : "(notif = null)"), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    //#233082 Consulta y reenvío de Notificaciones - fin 
    
    public String getInsertarCodigoInerno(AdaptadorSQLBD adaptador, String codigointernodescrip, String numExpediente) throws Exception {
      Connection con = null;
      
        try
        {
            if(adaptador != null){
                log.info(" num expediente" + numExpediente);
                con = adaptador.getConnection();
            }
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            codigointernodescrip= meLanbide43DAO.getInsertarCodigoInerno(codigointernodescrip,numExpediente, con);
            
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre las Especialides Solicitadas para expediente ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Especialidades solicitadas para expediente ", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        
        return codigointernodescrip;
    }

   public void getInsertarDatosProcedimiento(ProcedimientoSeleccionadoV0 procedimiento, AdaptadorSQLBD adaptador) throws BDException, Exception {
        //enviamos elobjeto a insertar al DAO
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide41DAO = MeLanbide43DAO.getInstance(); 
            String procedimientofinal;
            meLanbide41DAO.CrearNuevoProcedimiento(procedimiento, con);
        }finally {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
        
    }

public void getInsertarDatosProcedimientoFecha(ProcedimientoSeleccionadoV0 procedimiento, String fecha, AdaptadorSQLBD adaptador) throws BDException, Exception {
         //enviamos elobjeto a insertar al DAO
        Connection con = null;
            try{
                con = adaptador.getConnection();
                MeLanbide43DAO meLanbide41DAO = MeLanbide43DAO.getInstance(); 
                String procedimientofinal;
                meLanbide41DAO.CrearNuevoProcedimiento(procedimiento,fecha,con);
               

            }finally {
                try
                {
                    adaptador.devolverConexion(con);       
                }
                catch(Exception e)
                {
                    log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
                }
            }
    }

    public void guardarGestiones(String procedimiento, String codigoint, AdaptadorSQLBD adaptador) throws BDException, Exception {
    //boolean correcto=false;
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide41DAO = MeLanbide43DAO.getInstance(); 
            String procedimientofinal;
            procedimientofinal = meLanbide41DAO.CrearNuevoProcedimiento(codigoint,procedimiento, con);
            //Insertamos el procedimiento recibido
            //id = meLanbide41DAO.getIdGestiones(con);

        }finally {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n a la BBDD: " + e.getMessage());
            }
        }
    }           
    
    public ArrayList<Participantes> leerListaParticipantesRepresentantesRdRxExp(String numExpediente,int codRolrepre, Connection con) throws Exception {
        log.info("leerListaParticipantesRepresentantesRdRxExp - Manager - Begin()");
        ArrayList<Participantes> exp = new ArrayList<Participantes>();
        try {
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            exp = melanbide43DAO.leerListaParticipantesRepresentantesRdRxExp(numExpediente,codRolrepre,con);
        } catch (Exception e) {
            log.error("Error al recuperar los datos de el ROL Representante en el Expediente " + numExpediente, e);
            throw e;
        } finally {
            log.info("leerListaParticipantesRepresentantesRdRxExp - Manager - End()");
        }
        return exp;
    }

    public TercerosValueObject getDatosBasicosterceroRepreRdR(String nif, Connection con) throws Exception {
        log.info("getDatosBasicosterceroRepreRdR - Manager - Begin()");
        try {
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            return melanbide43DAO.getDatosBasicosterceroRepreRdR(nif, con);
        } catch (Exception e) {
            log.error("Error al recuperar los datos basico del tecero en el Expediente. NIF : " + nif, e);
            throw e;
        } finally {
            log.info("getDatosBasicosterceroRepreRdR - Manager - End()");
        }
    }
    public TercerosValueObject getDatosTerceroRdRxNifExpteRol(String numExpediente, String nif,Integer rol, Connection con) throws Exception {
        log.info("getDatosTerceroRdRxNifExpteRol - Manager - Begin()");
        try {
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            return melanbide43DAO.getDatosTerceroRdRxNifExpteRol(numExpediente,nif,rol,con);
        } catch (Exception e) {
            log.error("Error al recuperar los datos tecero en el Expediente NIF/expediente/rol : " + nif + " / " + numExpediente + " / " + rol , e);
            throw e;
        } finally {
            log.info("getDatosTerceroRdRxNifExpteRol - Manager - End()");
        }
    }

    public boolean insertarRepresentanteLegalExpedienteRdR(TercerosValueObject tercero, int codOrganizacion, String numExpediente, String codProcedimiento,int codRolRepreProcedimientoInt,Connection con) throws Exception {
        log.info("insertarRepresentanteLegalExpedienteRdR - Manager - Begin()");
        try {
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            return melanbide43DAO.insertarRepresentanteLegalExpedienteRdR(tercero,codOrganizacion,numExpediente,codProcedimiento,codRolRepreProcedimientoInt,con);
        } catch (Exception e) {
            log.error("Error al crear el rol REPRESENTANTE en el expediente " + (tercero!=null ? tercero.getDocumento() : "") +"/"+numExpediente, e);
            throw e;
        } finally {
            log.info("insertarRepresentanteLegalExpedienteRdR - Manager - End()");
        }
    }
    
    public boolean eliminarRepresentanteLegalExpedienteRdR(TercerosValueObject tercero, int codOrganizacion, String numExpediente, String codProcedimiento,int codRolRepreProcedimientoInt,Connection con) throws Exception {
        log.info("eliminarRepresentanteLegalExpedienteRdR - Manager - Begin()");
        try {
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            return melanbide43DAO.eliminarRepresentanteLegalExpedienteRdR(tercero,codOrganizacion,numExpediente,codProcedimiento,codRolRepreProcedimientoInt,con);
        } catch (Exception e) {
            log.error("Error al eliminar el rol REPRESENTANTE en el expediente " + (tercero!=null ? tercero.getDocumento() : "") +"/"+numExpediente, e);
            throw e;
        } finally {
            log.info("eliminarRepresentanteLegalExpedienteRdR - Manager - End()");
        }
    }

    public boolean existeNIFRepresentanteLegalEnExpedienteRdR(String lan6ApoderadoTypeID, int codOrganizacion, int codRolRepreProcedimientoInt, String codProcedimiento, String numExpediente, Connection con) throws Exception {
        log.info("existeNIFRepresentanteLegalEnExpedienteRdR - Manager - Begin()");
        try {
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            return melanbide43DAO.existeNIFRepresentanteLegalEnExpedienteRdR(lan6ApoderadoTypeID, codOrganizacion, codRolRepreProcedimientoInt, codProcedimiento, numExpediente, con);
        } catch (Exception e) {
            log.error("Error al comprobar si un NIF existe como representante legal de un expediene" + lan6ApoderadoTypeID + "/" + numExpediente, e);
            throw e;
        } finally {
            log.info("existeNIFRepresentanteLegalEnExpedienteRdR - Manager - End()");
        }
    }
    
    public String getDatosNotificacionElectronicaAVISOSxExptexCodCampo(String numExpediente, String codCampo, Connection con) throws Exception {
        log.info("getDatosNotificacionElectronicaAVISOSxExptexCodCampo - Manager - Begin()");
        try {
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            return melanbide43DAO.getDatosNotificacionElectronicaAVISOSxExptexCodCampo(numExpediente,codCampo,con);
        } catch (Exception e) {
            log.error("Error al recuperar los datos de envio de notificacion del expediente : " + numExpediente, e);
            throw e;
        } finally {
            log.info("getDatosNotificacionElectronicaAVISOSxExptexCodCampo - Manager - End()");
        }
    }
    
    public boolean updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(int codOrganizacion, String numExpediente, String codCampo, String nuevoValorCampo, Connection con) throws Exception {
        log.info("updateDatosNotificacionElectronicaAVISOSxExptexCodCampo - Manager - Begin()");
        try {
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            return melanbide43DAO.updateDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion,numExpediente,codCampo,nuevoValorCampo,con);
        } catch (Exception e) {
            log.error("Error al actualizar los datos de envio notificaicon del expediente : " + numExpediente, e);
            throw e;
        } finally {
            log.info("updateDatosNotificacionElectronicaAVISOSxExptexCodCampo - Manager - End()");
        }
    }
    
    public boolean borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(int codOrganizacion, String numExpediente, String codCampo, Connection con) throws Exception {
        log.info("borarDatosNotificacionElectronicaAVISOSxExptexCodCampo - Manager - Begin()");
        try {
            return meLanbide43DAOServ.borarDatosNotificacionElectronicaAVISOSxExptexCodCampo(codOrganizacion,numExpediente,codCampo,con);
        } catch (Exception e) {
            log.error("Error al eliminar los datos de envio notificacion del expediente : " + numExpediente, e);
            throw e;
        } finally {
            log.info("borarDatosNotificacionElectronicaAVISOSxExptexCodCampo - Manager - End()");
        }
    }
    
    public Integer esDesarrolloCero (String numExp, Connection con) throws Exception {
        if(log.isDebugEnabled()) log.info("esDesarrolloCero ( numExp = " + numExp  + " ) : BEGIN");
        MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
                
        if(log.isDebugEnabled()) log.info("esDesarrolloCero() : END");
        return meLanbide43DAO.getInstance().esDesarrolloCero(numExp, con);
    }
	
	public List<FilaLlamadasMisGestVO> getListaLlamadasMisGest(String numExped, String fecDesde, String fecHasta, String dniTercero,String codProcedimiento, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            return meLanbide43DAO.selectLlamadasMisGestiones(numExped, fecDesde, fecHasta, dniTercero,codProcedimiento,con);
        }

        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando llamadas a mis gestiones del expediente: "+numExped, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando llamadas a mis gestiones del expediente: "+numExped, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public FilaLlamadasMisGestVO getLlamada(String numExped, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            return meLanbide43DAO.getLlamada(numExped, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando llamada "+numExped, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando llamada "+numExped, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    //
    public HSSFWorkbook  descargarExcelLlamadasMisGest(String numExped,String fecDesde,String fecHasta,String dniTercero,String codProcedimiento,AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try{
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
            log.info("Recogemos lo datos con los parametros seleccionados ");
            List<FilaLlamadasMisGestVO> lstLlamadas = new ArrayList<FilaLlamadasMisGestVO>();
            lstLlamadas = meLanbide43DAO.selectLlamadasMisGestiones(numExped, fecDesde, fecHasta, dniTercero, codProcedimiento,con);
            log.info("Datos Recogidos para generar el Excel : " + (lstLlamadas!=null ? lstLlamadas.size() : 0));
            int limiteLineasHoja=65535; // Maximo para excel 2003
            try{
                String limite= ConfigurationParameter.getParameter(ConstantesMeLanbide43.MIS_GESTIONES_LIMITE_LINEAS_HOJA_EXCEL, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
                log.info("Limite filas recogido Properties : " + limite);
                limiteLineasHoja=(limite!=null && !limite.isEmpty() && !limite.equalsIgnoreCase("0") ? Integer.valueOf(limite) : limiteLineasHoja);
            }catch(NumberFormatException e){
                log.error("Tipo de dato no valido para el limite del numero de Filas del Excel recogido desde Properties. " + e.getMessage(),e);
            }catch(Exception e){
                log.error("Error al leer la propiedad MIS_GESTIONES_LIMITE_LINEAS_HOJA_EXCEL del properties MELANBIDE43." + e.getMessage(),e);
            }
            log.info("Limite Lineas por hoja : " + limiteLineasHoja);
            String[] headers = new String[]{
                "NRO",
                "NUM. EXP.",
                "TIPO DOC.",
                "DOCUMENTO",
                "PROCEDIMIENTO",
                "TIPO OPE.",
                "COD. TRAMITE",
                "FECHA GENERADO",
                "FECHA PROCESADO",
                "RES. EJE.",
                "RES. NUM.",
                "REG. TELEMATICO",
                "FEC. TELEMATICO",
                "NUM. INTEN."
            };
            HSSFWorkbook workbook = new HSSFWorkbook();
            int sheetIndex=0;
            int sheetRowCounter=0;
            HSSFSheet sheet =null;
            CellStyle stylePar = workbook.createCellStyle();
            stylePar.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            stylePar.setFillPattern(CellStyle.SOLID_FOREGROUND);
            for(int i = 0; i < lstLlamadas.size(); i++){
                log.info("sheetRowCounter  " + sheetRowCounter + " limiteLineasHoja " + limiteLineasHoja);
                if(sheetRowCounter== 0 || sheetRowCounter>limiteLineasHoja){ // Cero para la primera hoja primera linea
                    log.info("Crea nueva hoja...");
                    // Reseteamos el contador de filas
                    sheetRowCounter=0;
                    sheet=crearHojaExceImprimirLlamadasMisGestiones(workbook,sheetIndex);
                    crearCabeceraHojaExceImprimirLlamadasMisGestiones(workbook, sheet, headers);
                    //Incrementamos el contador indice hojas
                    sheetIndex++;
                    // Indicamos que hemos creado la cabecera
                    sheetRowCounter++;
                }
                log.info("sheetRowCounter  " + sheetRowCounter);
                if(sheet!=null){
                    HSSFRow dataRow = sheet.createRow(sheetRowCounter);
                    FilaLlamadasMisGestVO fila = lstLlamadas.get(i);
                    dataRow.createCell(0).setCellValue(fila.getId());
                    dataRow.createCell(1).setCellValue(fila.getNumExped());
                    dataRow.createCell(2).setCellValue(fila.getTerTid());
                    dataRow.createCell(3).setCellValue(fila.getTerDoc());
                    dataRow.createCell(4).setCellValue(fila.getExpTipo());
                    dataRow.createCell(5).setCellValue(fila.getTipoOperacion());
                    dataRow.createCell(6).setCellValue(fila.getCodTramiteInicio());
                    dataRow.createCell(7).setCellValue(fila.getFechaGenerado());
                    dataRow.createCell(8).setCellValue(fila.getFechaProceso());
                    dataRow.createCell(9).setCellValue(fila.getResEje());
                    dataRow.createCell(10).setCellValue(fila.getResNum());
                    dataRow.createCell(11).setCellValue(fila.getRegTelematico());
                    dataRow.createCell(12).setCellValue(fila.getFechaTelematico());
                    dataRow.createCell(13).setCellValue(fila.getNumIntent());
                    if (sheetRowCounter > 0 && sheetRowCounter % 2 == 0) {
                        for (int k = 0; k < dataRow.getLastCellNum(); k++) {
                            dataRow.getCell(k).setCellStyle(stylePar);
                        }
                    }
                    // Aumentamos contador al crear la nueva fila
                    sheetRowCounter++;
                }else{
                    log.error("No se ha poddo crear la hoja en el excel ...");
                }
            }            
            // Ponemos auto-size las columnas en todas las hojas creadas
            /*
            for(int i=0;i<workbook.getActiveSheetIndex();i++){
                for (int j = 0; j < headers.length; i++) {
                    workbook.getSheetAt(i).autoSizeColumn(j);
                }                
            }
            */
            
            ByteArrayOutputStream baos = new ByteArrayInOutStream();
            workbook.write(baos);
            return workbook;
            
        }catch(BDException e){
            log.error("Se ha producido una excepcion en la BBDD recuperando los datos antes de imprimir", e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando llamadas a mis gestiones ", ex );
            adaptador.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
        return null;
    }
    
    private static HSSFSheet crearHojaExceImprimirLlamadasMisGestiones(HSSFWorkbook workbook,int sheetIndex) throws Exception{
        try {
            return workbook.createSheet("LlamadasMisgestiones" + (sheetIndex > 0 ? sheetIndex : ""));
        } catch (Exception e) {
            log.error("Error al tratar de Crear la hoja para el excel de impresion datos de Llamadas mis gestiones", e);
            throw e;
        }
    }
    private static void crearCabeceraHojaExceImprimirLlamadasMisGestiones(HSSFWorkbook workbook, HSSFSheet sheet, String[] headers) {
        log.info("Creamos la cabecera para : " + (sheet!=null?sheet.getSheetName():"Null"));
        try {
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            HSSFRow headerRow = sheet.createRow(0);
            if (headers != null && headers.length > 0) {
                for (int i = 0; i < headers.length; i++) {
                    String header = headers[i];
                    HSSFCell cell = headerRow.createCell(i);
                    cell.setCellStyle(headerStyle);
                    cell.setCellValue(header);
                    sheet.autoSizeColumn(i);
                }
            }
        } catch (Exception e) {
            log.error("Error Crear la Cabecera de la hoja "+(sheet!=null?sheet.getSheetName():"Null")+" para el excel de impresion datos de Llamadas mis gestiones", e);
        }
    }
    
    public List<ProcedimientoVO> getComboProcedimiento(int codOrganizacion, AdaptadorSQLBD adaptador) throws Exception {
        log.info("getComboProcedimiento - Begin () " + formatDateLog.format(new Date()));
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide43DAOServ.getComboProcedimiento(codOrganizacion, con);
        } catch (Exception e) {
            log.error("getComboProcedimiento  " + codOrganizacion + " Error : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
            log.info("getComboProcedimiento - End ()" + formatDateLog.format(new Date()));
        }
    }
    
    public void actualizarProcesadoActInteresados(int id, AdaptadorSQLBD adaptador) throws Exception{           
         Connection con = null;         
         ArrayList<FilaListadoMisGestiones> misGestiones = new ArrayList<FilaListadoMisGestiones>();  
        try{
            con = adaptador.getConnection();
            MeLanbide43DAO misgestDAO = MeLanbide43DAO.getInstance();                
            misgestDAO.actualizarProcesadoActInteresados(id, con);
        }catch (Exception e){

            throw e;
        }finally {
            try
            {
                if (con != null) con.close();         
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi�n la BBDD: " + e.getMessage());
            }
        }
    }
    
    /**
     * Recibiendo Adaptador BD - Obtiene los datos de Aviso de un expediente. Campos Suplementarios: AVISOEMAIL,AVISOEMAILTIT,AVISOSMS,AVISOSMSTIT,AVISOIDCTAINTE,AVISOIDCTAINTETIT,IDIOMAAVISOS
     * @param numExpediente
     * @param adaptador
     * @return Mapa con key/valor (codCampo/valorCampo) : AVISOEMAIL / ejempmlo@ejemplo.com , IDIOMAAVISOS / es, etc.
     */
    public DatosAvisoCSRegexlan getMapaDatosSuplementarioAvisosExpediente(String numExpediente, AdaptadorSQLBD adaptador){
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return this.getMapaDatosSuplementarioAvisosExpediente(numExpediente, con);
        } catch (Exception ex) {
            log.error("Error - getMapaDatosSuplementarioAvisosExpediente ",ex);
            return null;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception ex) {
                log.error("Error al cerrar conexion la BBDD: ",ex);
            }
        }
    }
    
    /**
     * Recibiendo Conexion BD - Obtiene los datos de Aviso de un expediente. Campos Suplementarios: AVISOEMAIL,AVISOEMAILTIT,AVISOSMS,AVISOSMSTIT,AVISOIDCTAINTE,AVISOIDCTAINTETIT,IDIOMAAVISOS
     * @param numExpediente
     * @param con
     * @return Mapa con key/valor (codCampo/valorCampo) : AVISOEMAIL / ejempmlo@ejemplo.com , IDIOMAAVISOS / es, etc.
     */
    public DatosAvisoCSRegexlan getMapaDatosSuplementarioAvisosExpediente(String numExpediente, Connection con){
        DatosAvisoCSRegexlan respuesta = new DatosAvisoCSRegexlan();
        try {
            respuesta.setAvisoIdCuentaInteresadoRepresentante(MeLanbide43Manager.getInstance().getDatosNotificacionElectronicaAVISOSxExptexCodCampo(numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTE, con));
            respuesta.setAvisoEmailRepresentante(MeLanbide43Manager.getInstance().getDatosNotificacionElectronicaAVISOSxExptexCodCampo(numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAIL, con));
            respuesta.setAvisoSmsRepresentante(MeLanbide43Manager.getInstance().getDatosNotificacionElectronicaAVISOSxExptexCodCampo(numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMS, con));
            respuesta.setAvisoIdCuentaInteresadoTitular(MeLanbide43Manager.getInstance().getDatosNotificacionElectronicaAVISOSxExptexCodCampo(numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOIDCTAINTETIT, con));
            respuesta.setAvisoEmailTitular(MeLanbide43Manager.getInstance().getDatosNotificacionElectronicaAVISOSxExptexCodCampo(numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOEMAILTIT, con));
            respuesta.setAvisoSmsTitular(MeLanbide43Manager.getInstance().getDatosNotificacionElectronicaAVISOSxExptexCodCampo(numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_AVISOSMSTIT, con));
            respuesta.setAvisoIdioma(MeLanbide43Manager.getInstance().getDatosNotificacionElectronicaAVISOSxExptexCodCampo(numExpediente, DatosAvisoCSRegexlan.COD_CAMPO_IDIOMAAVISOS, con));
        } catch (Exception ex) {
            log.error("Error - getMapaDatosSuplementarioAvisosExpediente ",ex);
            respuesta = null;
        } finally {
            //La gestion de la conexion en el metodo que invoca
        }
        return respuesta;
    }
    
    public String getValorCampoSuplementarioExpedienteTDESP(String numExpediente, String codCampo, Connection con) throws Exception {
        log.info("getValorCampoSuplementarioExpedienteTDESP - Manager - Begin()");
        try {
            MeLanbide43DAO melanbide43DAO = MeLanbide43DAO.getInstance();
            return melanbide43DAO.getValorCampoSuplementarioExpedienteTDESP(numExpediente,codCampo,con);
        } catch (Exception e) {
            log.error("Error al recuperar los datos de envio de notificacion del expediente : " + numExpediente, e);
            throw e;
        } finally {
            log.info("getValorCampoSuplementarioExpedienteTDESP - Manager - End()");
        }
    }

    private boolean procedimientoSinSolicitud(String codProcedimiento) {
        boolean enLista = false;
        String procedimientos = ConfigurationParameter.getParameter(ConstantesMeLanbide43.PROC_SIN_SOLICITUD, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
        String[] procSinSol = procedimientos.split(BARRA);
        for (String procedimiento : procSinSol) {
            if (codProcedimiento.equalsIgnoreCase(procedimiento)) {
                enLista = true;
                log.info(procedimiento + " es procedimiento sin solicitud.");
                break;
            }
        }
        return enLista;
    }
    
    
    
       public List<Tramite9099VO> getProcedimientosConTramite9099(  AdaptadorSQLBD adaptador) throws Exception {
        log.info("getProcedimientosConTramite9099 - Manager - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide43Tramite9099DAO meLanbide43Tramite9099DAO = MeLanbide43Tramite9099DAO.getInstance();
            return meLanbide43Tramite9099DAO.getProcedimientosConTramite9099(con);                  
        } catch (Exception e) {
            log.error("Error al recuperar los procedimientos con tramite 9099 ", e);
            throw e;
        } finally {
         if (con != null) {
            con.close();
              }
            log.info("getProcedimientosConTramite9099 - Manager - End()");
        }
    }
       
       
       
   public List<Tramite9099CerrarVO> getProcedimientosTramites9099PorProcedimiento(String procedimiento, Integer tramite, AdaptadorSQLBD adaptador) throws Exception {
        log.info("getProcedimientosTramites9099PorProcedimiento - Manager - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide43Tramite9099DAO meLanbide43Tramite9099DAO = MeLanbide43Tramite9099DAO.getInstance();
            return meLanbide43Tramite9099DAO.getProcedimientosTramites9099PorProcedimiento(procedimiento, tramite ,con);
        } catch (Exception e) {
            log.error("Error al recuperar los procedimientos con tramite 9099 ", e);
            throw e;
        } finally {
             if (con != null) {
            con.close();
              }
            log.info("getProcedimientosTramites9099PorProcedimiento - Manager - End()");
        }
    }
   
   
   public Tramite9099CerrarVO getProcedimientosTramites9099cerrar(String procedimiento, Integer tramite,String numExpediente, AdaptadorSQLBD adaptador, Integer ocurrenciaTramite) throws Exception {
        log.info("getProcedimientosTramites9099cerrar - Manager - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide43Tramite9099DAO meLanbide43Tramite9099DAO = MeLanbide43Tramite9099DAO.getInstance();
            return meLanbide43Tramite9099DAO.getProcedimientosTramites9099Cerrar(procedimiento, tramite ,numExpediente,con,ocurrenciaTramite);
        } catch (Exception e) {
            log.error("Error al recuperar los procedimientos con tramite 9099 ", e);
            throw e;
        } finally {
            if (con != null) {
            con.close();
              }
            log.info("getProcedimientosTramites9099cerrar - Manager - End()");
        }
    }
   
       public String cerrarTramite(int codOrganizacion, String numExp, String codTramite,String procedimiento, AdaptadorSQLBD adaptador,  Integer ocurrenciaTramite) throws Exception {
        log.info("cerrarTramite - Begin () ");
        String retorno = null;     
            Connection con = null;
        try { 
             con = adaptador.getConnection();
           MeLanbide43Tramite9099DAO meLanbide43Tramite9099DAO = MeLanbide43Tramite9099DAO.getInstance();
           retorno =  meLanbide43Tramite9099DAO.cerrarTramite(codOrganizacion, numExp, codTramite, procedimiento, con, ocurrenciaTramite);
        } catch (BDException ex){
            log.error("Se ha producido una excepcion en la BBDD cerrando el tramite " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            log.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally{
             if (con != null) {
            con.close();
              }
        }
        log.info("cerrarTramite - End () ");
        return retorno;
    }   

       
    
     public int insertarLineasLogJob9099(AdaptadorSQLBD adaptador, String numeroExpediente, String estado, String mensaje) throws Exception {
        log.info("insertarLineasLogJob - Begin () ");
        Integer retorno = 0;     
        Connection con = null;
        try { 
                  con = adaptador.getConnection();
           MeLanbide43Tramite9099DAO meLanbide43Tramite9099DAO = MeLanbide43Tramite9099DAO.getInstance();
           retorno =  meLanbide43Tramite9099DAO.insertarLineasLogJob(con, numeroExpediente, estado,  mensaje);
        } catch (BDException ex){
            log.error("Se ha producido una excepcion en la BBDD insertando el log insertarLineasLogJobTramite9099 " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            log.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally{
             if (con != null) {
            con.close();
              }
        }
        log.info("insertarLineasLogJob - End () ");
        return retorno;
    }      
     
     
     
public String getTramiteSalida(int codOrganizacion, AdaptadorSQLBD adaptador, String procedimiento, int codigoTramite ) throws Exception {
        log.info("getTramiteSalida - Begin () ");
        String retorno = null;     
Connection con = null;
        try { 
             con = adaptador.getConnection();
           MeLanbide43Tramite9099DAO meLanbide43Tramite9099DAO = MeLanbide43Tramite9099DAO.getInstance();
           retorno =  meLanbide43Tramite9099DAO.getTramiteSalida(codOrganizacion, con, procedimiento, codigoTramite );
        } catch (BDException ex){
            log.error("Se ha producido una excepcion en la BBDD insertando el log insertarLineasLogJobTramite9099 " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            log.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally{
             if (con != null) {
            con.close();
              }
        }
        log.info("getTramiteSalida - End () ");
        return retorno;
    }      


  public String abrirTramite(int codOrganizacion, String numExp, String codTramite,String procedimiento, Integer uor, Integer usuario, AdaptadorSQLBD adaptador) throws Exception {
        log.info("abrirTramite - Begin () ");
        
        String retorno = null;     
Connection con = null;
        try { 
            con = adaptador.getConnection();
              MeLanbide43Tramite9099DAO meLanbide43Tramite9099DAO = MeLanbide43Tramite9099DAO.getInstance();
           retorno =  meLanbide43Tramite9099DAO.abrirTramite(codOrganizacion, numExp, codTramite, procedimiento, uor, usuario, con);
        } catch (BDException ex){
            log.error("Se ha producido una excepcion en la BBDD abriendo el tramite " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            log.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally{
             if (con != null) {
            con.close();
              }
        }
        log.info("abrirTramite - End () ");
        return retorno;
    }   
       
    public Date getValorCampoSuplementarioTramiteE_TFET(String numExpediente, String codCampo, int tramite, int ocurrencia, AdaptadorSQLBD adaptador ) throws Exception {
        log.info("getValorCampoSuplementarioTramiteE_TFET - Manager - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide43DAO.getInstance().getValorCampoSuplementarioTramiteE_TFET(numExpediente,codCampo, tramite, ocurrencia, con);
        } catch (Exception e) {
            log.error("Error al getValorCampoSuplementarioTramiteE_TFET " + e.getMessage(), e);
            throw e;
        } finally {
            if (con != null) {
                con.close();
            }
            log.info("getValorCampoSuplementarioTramiteE_TFET - Manager - End()");
        }
    }

}//class
