package es.altia.flexia.integracion.moduloexterno.melanbide63.manager;

import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.flexia.integracion.moduloexterno.melanbide63.dao.MeLanbide63DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide63.exception.CopiarDocumentosTramitacionException;
import es.altia.flexia.integracion.moduloexterno.melanbide63.vo.DocumentoTramitacionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide63Manager {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide63Manager.class);
    
    //Instancia
    private static MeLanbide63Manager instance = null;
    
    private MeLanbide63Manager()
    {
        
    }
    
    public static MeLanbide63Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide63Manager.class)
            {
                instance = new MeLanbide63Manager();
            }
        }
        return instance;
    }
    
    public String copiadoDocumentosTramitacion(int codOrg, int ejerc, String numExp, String codProc, int codTram, int ocurrencia, String operacion, AdaptadorSQLBD adaptador) throws CopiarDocumentosTramitacionException
    {
        Connection con = null;
        ArrayList<DocumentoTramitacionVO> docs = null;
        ResourceBundle properties = ResourceBundle.getBundle("MELANBIDE63");
        String resultado = null;
        String nombreModulo = null;
        String codTramiteOrigen = null;
        String plantillasOrigen = null;
        String plantillasDestino = null;
        String[] pltsOrig = null;
        String[] pltsDest = null;
        HashMap<String,String> mapeoPlts = null;
        
        try
        {
            con = adaptador.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            
            nombreModulo = properties.getString("NOMBRE_MODULO");
            if(nombreModulo!=null && !nombreModulo.equals("")){
                codTramiteOrigen = properties.getString(codOrg+"/MODULO_INTEGRACION/"+nombreModulo+"/"+codProc+"/"+operacion+"/CODIGO_TRAMITE_ORIGEN_COPIAR_DOCS");
                log.info("codTramiteOrigen: "+codTramiteOrigen);
                if(codTramiteOrigen!=null && !codTramiteOrigen.equals("")){
                    int codTramOrig = -1;
                    int ocurrenciaOrig = -1;
                    try {
                        codTramOrig = Integer.parseInt(codTramiteOrigen);
                        log.info("codTramiteOrigen: "+codTramOrig);
                    } catch (NumberFormatException nfe) {
                        log.error("Se ha indicado un código de trámite de destino erróneo.");
                        resultado = "1";
                    }

                    if(codTramOrig!=-1){

                        ocurrenciaOrig = meLanbide63DAO.obtenerUltimaOcurrenciaTramite(codOrg, ejerc, codProc, numExp, codTramOrig, con);
                        //comprobamos si en el properties se especifican códigos de plantilla
                        plantillasOrigen = properties.getString(codOrg+"/MODULO_INTEGRACION/"+nombreModulo+"/"+codProc+"/"+operacion+"/CODIGOS_PLANTILLAS_ORIGEN_COPIAR_DOCS");
                         log.info("plantillasOrigen: "+plantillasOrigen);
                        if(plantillasOrigen!=null && !plantillasOrigen.isEmpty()){
                            mapeoPlts = new HashMap<String,String>();
                            plantillasDestino = properties.getString(codOrg+"/MODULO_INTEGRACION/"+nombreModulo+"/"+codProc+"/"+operacion+"/CODIGOS_PLANTILLAS_DESTINO_COPIAR_DOCS");
                            pltsOrig = plantillasOrigen.split(";");
                            pltsDest = plantillasDestino.split(";");
                            for(int index=0; index<pltsOrig.length; index++){
                                mapeoPlts.put(pltsOrig[index], pltsDest[index]);
                            }
                        }
                        docs = meLanbide63DAO.recuperarDocumentosTramitacion(codOrg, codTramOrig, ocurrenciaOrig, pltsOrig, ejerc, codProc, numExp, con);
                        if(!docs.isEmpty()){
                            int copiado=0;
                            boolean copiaDokusi = false;
                            if(operacion.equals("DOKUSI")) {
                                copiaDokusi = true;
                            }
                            
                            adaptador.inicioTransaccion(con);
                            for(DocumentoTramitacionVO documento : docs){
                                copiado += meLanbide63DAO.insertarDocumentoTramitacion(documento, codOrg, codTram, ocurrencia, mapeoPlts, ejerc, codProc, numExp, copiaDokusi, codProc, con, adaptador);
                            }
                            if(docs.size()==copiado){
                                adaptador.finTransaccion(con);
                                resultado = "0";
                            } else {
                                adaptador.rollBack(con);
                                resultado = "9";
                            }
                        }
                    }
                } else {
                    resultado = "2";
                }
            } else {
                resultado = "3";
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error al obtener la conexión a la BBDD", e);
            resultado = "10";
            throw new CopiarDocumentosTramitacionException(8,"Se ha producido un error al obtener la conexión a la BBDD");
        }
        catch(MissingResourceException e)
        {
            log.error("Se ha producido un error al obtener propiedades de MELANBIDE63.properties", e);
            resultado = "3";
            throw new CopiarDocumentosTramitacionException(10,"Se ha producido un error al obtener propiedades de MELANBIDE63.properties");
        }
        catch(CopiarDocumentosTramitacionException ex)
        {
            log.error("Se ha producido un error al copiar documentos entre trómites", ex);
            resultado = String.valueOf(ex.getCodigo());
            try {
                adaptador.rollBack(con);
                throw ex;
            } catch (BDException ex1) {
                log.error("Error al hacer rollback de la BBDD");
            }
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }
        
        return resultado;
    }

    
    

    public String ExpedienteAsociadoREINT(int codOrganizacion, String numExpediente,AdaptadorSQLBD adaptador) throws CopiarDocumentosTramitacionException, Exception {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.insertarDocumentoTramitacion(numExpediente,con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente "+numExpediente, ex);
            throw new Exception(ex);
        }
        finally
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
        
    }

    public String copiadoDocumentosREINT(int codOrganizacion, int ejercicio, String numExpediente, String codProcedimiento, int codTramite, int ocurrenciaTramite, String operacion, String expedienteAsociadoReint, AdaptadorSQLBD adaptador) throws CopiarDocumentosTramitacionException, Exception {
        log.info("copiadoDocumentosREINT()");
        Connection con = null;
        ArrayList<DocumentoTramitacionVO> docs = null;
        ResourceBundle properties = ResourceBundle.getBundle("MELANBIDE63");
        String resultado = null;
        String nombreModulo = null;
        String codTramiteDestino = null;
        String plantillasOrigen = null;
        String plantillasDestino = null;
        String[] pltsOrig = null;
        String[] pltsDest = null;
        ArrayList<String> listapltsOrig = new ArrayList();
        HashMap<String, String> mapeoPlts = null;

        log.debug("Parametros que recibimos");
        log.debug("Expediente Origen " + numExpediente);
        log.debug("Expediente Destino " + expedienteAsociadoReint);
        log.debug("codProcedimiento " + codProcedimiento);
        log.debug("operacion " + operacion);
        log.debug("codTramite " + codTramite);

        String[] array = expedienteAsociadoReint.split("/");
        String ano = array[0];
        int anon = Integer.parseInt(ano);
        String procedimientoDestino = array[1];
        log.debug("AÑO " + ano);
        log.debug("PROCEDIMIENTO " + procedimientoDestino);

        try {
            con = adaptador.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();

            nombreModulo = properties.getString("NOMBRE_MODULO");
            if (nombreModulo != null && !nombreModulo.isEmpty()) {
                //DIBUJAMOS LA LLAMADA LA REINT PROCEDIMIENTO CON TRAMITE 400

                codTramiteDestino = properties.getString(codOrganizacion + "/MODULO_INTEGRACION/" + nombreModulo + "/" + codProcedimiento + "/" + operacion + "/CODIGO_TRAMITE_DESTINO_COPIAR_DOCS");

                log.info("codTramiteDestino " + codTramiteDestino);

                if (codTramiteDestino != null && !codTramiteDestino.isEmpty()) {
                    int codTramiteDest = -1;
                    int ocurrenciaOrig = -1;
                    try {
                        codTramiteDest = Integer.parseInt(codTramiteDestino);
                        log.debug("codTramiteDestino " + codTramiteDest);
                    } catch (NumberFormatException nfe) {
                        log.error("Se ha indicado un código de trómite de destino erróneo.");
                        resultado = "1";
                    }

                    //Recuperamos el codigo de la plantilla origen
                    if (codTramiteDest != -1) {
                        log.debug("procedimiento que enviamos a ocurrenciaorigen " + procedimientoDestino);
                        ocurrenciaOrig = meLanbide63DAO.obtenerUltimaOcurrenciaTramiteReint(codOrganizacion, ejercicio, procedimientoDestino, expedienteAsociadoReint, codTramite, con);
                        //comprobamos si en el properties se especifican códigos de plantilla

                        //del expediente se recuperan de BD todos los documentos asociados y su plantilla 
                        listapltsOrig = meLanbide63DAO.obtenercodigoPlantillaOrigneReint(codOrganizacion, codTramite, ocurrenciaTramite, ejercicio, codProcedimiento, numExpediente, con);
                        int tope = listapltsOrig.size();
                        //plantillasOrigen = properties.getString(codOrganizacion+"/MODULO_INTEGRACION/"+nombreModulo+"/"+codProcedimiento+"/"+operacion+"/CODIGOS_PLANTILLAS_ORIGEN_COPIAR_DOCS");
                        if (plantillasOrigen != null && !plantillasOrigen.isEmpty()) {
                            mapeoPlts = new HashMap<String, String>();
                            plantillasDestino = properties.getString(codOrganizacion + "/MODULO_INTEGRACION/" + nombreModulo + "/" + codProcedimiento + "/" + operacion + "/CODIGOS_PLANTILLAS_DESTINO_COPIAR_DOCS");
                            //pltsOrig = plantillasOrigen.split(";");

                            pltsDest = plantillasDestino.split(";");
                            for (int index = 0; index < tope; index++) {
                                mapeoPlts.put(listapltsOrig.get(index), pltsDest[index]);

                            }
                        }
                        log.info("Procedimiento que enviamos a recuperar docs " + codProcedimiento);
                        docs = meLanbide63DAO.recuperarDocumentosREINT(codOrganizacion, codTramite, ocurrenciaTramite, pltsOrig, ejercicio, codProcedimiento, numExpediente, con);
                        if (!docs.isEmpty()) {
                            log.info("size docs " + docs.size());
                            int copiado = 0;
                            boolean copiaDokusi = false;
                            if (operacion.equals("DOKUSI")) {
                                copiaDokusi = true;
                            }

                            adaptador.inicioTransaccion(con);
                            for (DocumentoTramitacionVO documento : docs) {
                                log.info("Procedimiento que enviamos a copiado " + procedimientoDestino);
                                copiado += meLanbide63DAO.insertarDocumentoTramitacion(documento, codOrganizacion, codTramiteDest, ocurrenciaTramite, mapeoPlts, anon, procedimientoDestino, expedienteAsociadoReint, copiaDokusi, codProcedimiento, con, adaptador);
                            }
                            if (docs.size() == copiado) {
                                adaptador.finTransaccion(con);
                                resultado = "0";
                            } else {
                                adaptador.rollBack(con);
                                resultado = "9";
                            }
                        }
                    }
                } else {
                    resultado = "2";
                }
            } else {
                resultado = "3";
            }
        } catch (BDException e) {
            log.error("Se ha producido un error al obtener la conexión a la BBDD", e);
            resultado = "10";
            throw new CopiarDocumentosTramitacionException(8, "Se ha producido un error al obtener la conexión a la BBDD");
        } catch (MissingResourceException e) {
            log.error("Se ha producido un error al obtener propiedades de MELANBIDE63.properties", e);
            resultado = "3";
            throw new CopiarDocumentosTramitacionException(10, "Se ha producido un error al obtener propiedades de MELANBIDE63.properties");
        } catch (CopiarDocumentosTramitacionException ex) {
            log.error("Se ha producido un error al copiar documentos entre trámites", ex);
            resultado = String.valueOf(ex.getCodigo());
            try {
                adaptador.rollBack(con);
                throw ex;
            } catch (BDException ex1) {
                log.error("Error al hacer rollback de la BBDD");
            }
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }

        return resultado;
    }

    public Boolean existeExpediente(String numExp, AdaptadorSQLBD adapt) throws Exception {
        log.debug("existeExpediente -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.existeExpediente(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD comprobando si existe expediente en la carga masiva de documentos." + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico comprobando si existe expediente en la carga masiva de documentos." + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean existeProcedimiento(String codProc, AdaptadorSQLBD adapt) throws Exception {
        log.debug("existeProcedimiento -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.existeProcedimiento(codProc, con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD comprobando si existe procedimiento en la carga masiva de documentos." + codProc, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico comprobando si existe procedimiento en la carga masiva de documentos." + codProc, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean existeCodTramiteInternoProcedimiento(String codigoProcIndicadoInterfaz, String codigoInteTramIndicaInterf, AdaptadorSQLBD adapt) throws Exception {
        log.debug("existeCodTramiteInternoProcedimiento -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.existeCodTramiteInternoProcedimiento(codigoProcIndicadoInterfaz,codigoInteTramIndicaInterf,con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD comprobando si existe codigo interno tramite en prodimiento en la carga masiva de documentos." + codigoProcIndicadoInterfaz + "-"+ codigoInteTramIndicaInterf, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico comprobando si existe codigo interno tramite en prodimiento en la carga masiva de documentos." + codigoProcIndicadoInterfaz + "-"+ codigoInteTramIndicaInterf, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
        
    public boolean existeTramiteEnExpediente(String numExp, String codigoInternoTramite, AdaptadorSQLBD adapt) throws Exception {
        log.debug("existeTramiteEnExpediente -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.existeTramiteEnExpediente(numExp,codigoInternoTramite,con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD comprobando si existe codigo interno tramite para el expediente en la carga masiva de documentos." + numExp + "-"+ codigoInternoTramite, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico comprobando si existe codigo interno tramite para el expediente en la carga masiva de documentos." + numExp + "-"+ codigoInternoTramite, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Integer obtenerMaxOcurrenciaTramiteEnExpediente(String numExp, String codigoInternoTramite, AdaptadorSQLBD adapt) throws Exception {
        log.debug("obtenerMaxOcurrenciaTramiteEnExpediente -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.obtenerMaxOcurrenciaTramiteEnExpediente(numExp,codigoInternoTramite,con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD recuperando la maxima correncia del tramite en el expediente en la carga masiva de documentos." + numExp + "-"+ codigoInternoTramite, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico recuperando la maxima correncia del tramite en el expediente  en la carga masiva de documentos." + numExp + "-"+ codigoInternoTramite, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
        
    public Integer obtenerIDNumDocumentoTramiteExpediente(String numExp, Integer codigoInternoTramite, Integer ocurrenciaTramite,AdaptadorSQLBD adapt) throws Exception {
        log.debug("obtenerIDNumDocumentoTramiteExpediente -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.obtenerIDNumDocumentoTramiteExpediente(numExp,codigoInternoTramite,ocurrenciaTramite,con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD recuperando el maximo numero de documento del tramite en el expediente en la carga masiva de documentos." + numExp + "-"+ codigoInternoTramite, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico recuperando el maximo numero de documento del tramite en el expediente  en la carga masiva de documentos." + numExp + "-"+ codigoInternoTramite, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean existeCodPlantillaDocumentoTramiteProcedimiento(String codigoProcIndicadoInterfaz, String codigoInteTramIndicaInterf, String codigoPlantilla, AdaptadorSQLBD adapt) throws Exception {
        log.debug("existeCodPlantillaDocumentoTramiteProcedimiento -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.existeCodPlantillaDocumentoTramiteProcedimiento(codigoProcIndicadoInterfaz, codigoInteTramIndicaInterf,codigoPlantilla, con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD comprobando si existe codigo plantilla tramite para el procedimineto y tramite en la carga masiva de documentos." + codigoProcIndicadoInterfaz + "-" + codigoInteTramIndicaInterf +"-"+codigoPlantilla, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico comprobando si existe codigo plantilla tramite para el procedimineto y tramite  en la carga masiva de documentos." +  codigoProcIndicadoInterfaz + "-" + codigoInteTramIndicaInterf +"-"+codigoPlantilla, ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public String cargarDocumentoResolucionTramiteBBDD(DocumentoGestor documentoGestor, AdaptadorSQLBD adapt) throws Exception {
        log.debug("cargarDocumentoResolucionTramiteBBDD() : BEGIN - manager ");
        Connection con = null;
        String salida = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO daoMelanbide63 = MeLanbide63DAO.getInstance();
            salida = daoMelanbide63.cargarDocumentoResolucionTramiteBBDD(documentoGestor, con);
        } catch (BDException e) {
            log.error("cargarDocumentoResolucionTramiteBBDD - Se ha producido una excepción en la BBDD ", e);
            throw e;
        } catch (Exception ex) {
            log.error("cargarDocumentoResolucionTramiteBBDD - Se ha producido una excepción general", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("cargarDocumentoResolucionTramiteBBDD -Manager- Error al cerrar conexión a la BBDD: " + e.getMessage(), e);
            }//try-catch
        }//try-catch-finally
        log.error("cargarDocumentoResolucionTramiteBBDD() : END - manager " + salida);
        return salida;
    }
    
    public boolean esDokusiActivoProcedimiento(String codProcedimiento, AdaptadorSQLBD adapt) throws Exception {
        log.debug(this.getClass().getName() + ".esDokusiActivoProcedimiento() : BEGIN -  ");
        boolean dokusiActivo = false;
        Connection con = null;
        try {
            con = adapt.getConnection();
            if (codProcedimiento != null && !codProcedimiento.isEmpty()) {
                dokusiActivo = MeLanbide63DAO.getInstance().esDokusiActivoProcedimiento(codProcedimiento, con);
            } else {
                log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento - Retornamos false porque codProcedimiento llega a nul o vacio ");
            }
        } catch (BDException e) {
            log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento - Se ha producido una excepción en la BBDD ", e);
            throw e;
        } catch (Exception ex) {
            log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento - Se ha producido una excepción general", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento -- Error al validar si procedimiento Activo Dokusi: " + e.getMessage(), e);
            }//try-catch
        }//try-catch-finally
        log.error(this.getClass().getName() + ".esDokusiActivoProcedimiento() : END -  " + dokusiActivo);
        return dokusiActivo;
    }

    public List<DocumentoGestor> obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc(Integer codOrganizacion,Integer ejercicio,String codProcedimiento,String numExp, Integer tramite, Integer ocurrencia,Integer documentoPlantilla, String Nombre, AdaptadorSQLBD adapt) throws BDException, Exception {
        log.debug(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc() : BEGIN -  ");
        List<DocumentoGestor> listaDocumentos = new ArrayList<DocumentoGestor>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            listaDocumentos = MeLanbide63DAO.getInstance().obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc(codOrganizacion,ejercicio,codProcedimiento,numExp, tramite, ocurrencia,documentoPlantilla,Nombre, con);
        } catch (BDException e) {
            log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc - Se ha producido una excepción en la BBDD ", e);
            throw e;
        } catch (Exception ex) {
            log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc - Se ha producido una excepción general", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc -- Error al recuperar la lista de documentos : " + e.getMessage(), e);
            }//try-catch
        }//try-catch-finally
        log.error(this.getClass().getName() + ".obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc() : END -  " + listaDocumentos.size());
        return listaDocumentos;
    }
    
    public ArrayList<String> recuperarExpedientesOcurrencia(int numTarea, AdaptadorSQLBD adapt) throws BDException, Exception {
        log.debug(this.getClass().getName() + ".recuperarExpedientesOcurrencia() : BEGIN -  ");
        ArrayList<String> lista = new ArrayList<String>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            lista = MeLanbide63DAO.getInstance().recuperarExpedientesOcurrencia(numTarea, con);
        } catch (BDException e) {
            log.error(this.getClass().getName() + ".recuperarExpedientesOcurrencia - Se ha producido una excepción en la BBDD ", e);
            throw e;
        } catch (Exception ex) {
            log.error(this.getClass().getName() + ".recuperarExpedientesOcurrencia - Se ha producido una excepción general", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error(this.getClass().getName() + ".recuperarExpedientesOcurrencia -- Error al recuperar la lista de expedientes y ocurrencia : " + e.getMessage(), e);
            }//try-catch
        }//try-catch-finally
        log.error(this.getClass().getName() + ".recuperarExpedientesOcurrencia() : END -  " + lista.size());
        return lista;
    }
    
    public boolean procesoLanzado(AdaptadorSQLBD adapt) throws Exception {
        log.info("procesoLanzado -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.procesoLanzado(con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD comprobando si el proceso está lanzado en la carga masiva de documentos.", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico comprobando si el proceso está lanzado en la carga masiva de documentos.", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int lanzarProceso(AdaptadorSQLBD adapt) throws Exception {
        log.info("lanzarProceso -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.lanzarProceso(con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD al intentar registrar que el proceso está lanzado.", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico al intentar registrar que el proceso está lanzado.", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int liberarProceso(AdaptadorSQLBD adapt) throws Exception {
        log.info("liberarProceso -  Begin");
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide63DAO meLanbide63DAO = MeLanbide63DAO.getInstance();
            return meLanbide63DAO.liberarProceso(con);
        } catch (BDException e) {
            log.error("Se ha producido un error BBDD al intentar registrar que el proceso ha terminado.", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error generico al intentar registrar que el proceso ha terminado.", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
}
