package es.altia.flexia.integracion.moduloexterno.melanbide37.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide37.dao.MeLanbide37DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.util.ConstantesMeLanbide37;
import es.altia.flexia.integracion.moduloexterno.melanbide37.util.MeLanbide37InformeUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.CerUnidadeCompetencialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCompAPAVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCompCPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoExpedientesEMPNLVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.Melanbide37RelacionExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.ExpCEPAPCriteriosFiltroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.ExpedienteCEPAPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.comun.SelectItem;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class MeLanbide37Manager {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide37Manager.class);
    
    //Instancia
    private static MeLanbide37Manager instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide37Manager, si no existe la crea.
     * @return 
     */
    public static MeLanbide37Manager getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide37Manager.class){
                if(instance == null){
                    instance = new MeLanbide37Manager();
                }//if(instance == null)
            }//synchronized(MeLanbide37Manager.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    
    public boolean guardarClaveCP(int codOrganizacion, String clave, String numExpediente, String ejercicio, String ano, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        boolean transactionStarted = false;
        try
        {
            int result = 0;int result2 = 0;
            Integer anoExp = Integer.parseInt(ejercicio);
           
            con = adaptador.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
            boolean nuevo = false;
            adaptador.inicioTransaccion(con);
            transactionStarted = true;
            
            nuevo = false;
            String clavecp = meLanbide37DAO.getValorCampoTexto(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide37.CAMPO_SUPLEMENTARIO_CLAVE_REGISTRAL_CP, con);
            if(clavecp == null)
            {
                nuevo = true;
            }
            
            if (nuevo){
                result = meLanbide37DAO.guardarValorCampoTexto(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide37.CAMPO_SUPLEMENTARIO_CLAVE_REGISTRAL_CP, clave, nuevo, con);
                if(result != 1)
                {
                    adaptador.rollBack(con);
                    return false;
                }

                String[] datosclave = clave.split("/");
                result2 = meLanbide37DAO.guardarContador(codOrganizacion, ano, "CP", datosclave[2], con);

                 if(result2 != 1)
                {
                    adaptador.rollBack(con);
                    return false;
                }
            } 
            
            adaptador.finTransaccion(con);
            return true;
        }
        catch(BDException e)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepción en la BBDD guardando la clave registral CP para el expediente " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepción en la BBDD guardando la clave registral CP para el expediente " + numExpediente, ex);
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
    /*public boolean guardarClaveAPA(int codOrganizacion, String clave, String numExpediente, String ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        boolean transactionStarted = false;
        try
        {
            int result = 0;
            int result2 = 0;
            Integer anoExp = Integer.parseInt(ejercicio);
           
            con = adaptador.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
            boolean nuevo = false;
            adaptador.inicioTransaccion(con);
            transactionStarted = true;
            
            nuevo = false;                       
            //result = meLanbide37DAO.guardarValorCampoTexto(codOrganizacion, ejercicio, numExpediente, ConstantesMeLanbide37.CAMPO_SUPLEMENTARIO_CLAVE_REGISTRAL_APA, clave, nuevo, con);
            
            if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }           
                        
            adaptador.finTransaccion(con);
            return true;
        }
        catch(BDException e)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepción en la BBDD guardando la clave registral APA para el expediente " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepción en la BBDD guardando la clave registral APA para el expediente " + numExpediente, ex);
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }*/
      
    public String generarClave(String numExpediente, int codOrganizacion, String ano, Connection con) throws SQLException{
        String clave=null;       
        MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
        if(numExpediente!=null && !"".equals(numExpediente)){   
            try{
                String codigo = meLanbide37DAO.getCertificadoExpediente(numExpediente, codOrganizacion, con);
                if (codigo!=null){
                    //se deja el código tal cual para contemplar todos (se va a ampliar a 12)
                    //codigo = codigo.substring(0, 8);
                   // clave = "16/"+ejercicio.substring(2)+"/000000/"+codigo;
                    clave = "16/"+ano.substring(2)+"/";
                    Integer cont = meLanbide37DAO.getNumContador(codOrganizacion, ano, "CP", con);
                    cont = cont+1;
                    String conttxt = String.format ("%08d", cont);
                                                 
                    clave =  clave + conttxt+"/"+codigo ;
                }
            }catch (Exception e){

            }
            finally{                
                if(con != null)
                    con.close();
            }
        }
        return clave;
    }
    
    public String guardarClavesAPA(String numExpediente, int codOrganizacion, AdaptadorSQLBD adaptador) throws Exception{
         //boolean correcto=false;
         String resultado =null;
         String clave=null;                     
         Connection con = null;         
         int result = 0; int result2 = 0;
         boolean transactionStarted = false;
         if(numExpediente!=null && !"".equals(numExpediente)){
            String[] datos          = numExpediente.split("/");
            String ejercicio        = datos[0];   
            Integer ano = obtieneFechaPresentacion(codOrganizacion, numExpediente, adaptador);
            
            try{
                if(ano != null)
                {
                    //String[] dat = fecha.toString().split("/");
                    //ejercicio = ano.toString();
                    con = adaptador.getConnection();
                    MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
                    String codigo = meLanbide37DAO.getCertificadoExpediente(numExpediente, codOrganizacion, con);
                    ArrayList<CerUnidadeCompetencialVO> udsCer = new ArrayList<CerUnidadeCompetencialVO>();
                    if(codigo != null ){
                        if(log.isDebugEnabled()) log.debug("Existe un certificado para el expediente");
                        udsCer = meLanbide37DAO.getCodigosUnidades(codigo, codOrganizacion+"" , numExpediente ,con);
                        if(udsCer.size() > 0){
                            if(log.isDebugEnabled()) log.debug("Existen unidades acreditadas para ese certificado en expediente");

                            adaptador.inicioTransaccion(con);
                            transactionStarted = true;

                            for(CerUnidadeCompetencialVO unidad : udsCer){
                                String numExpedienteAcreditado = unidad.getNumExpediente();
                                String codunidad =unidad.getCodUnidad();
                                clave = "16/"+ano.toString().substring(2)+"/";
                                Integer cont = meLanbide37DAO.getNumContador(codOrganizacion, ano.toString(), "APA", con);
                                cont = cont+1;
                                String conttxt = String.format ("%08d", cont);                           
                                clave =  clave + conttxt+"/"+codigo.substring(0, 8)+"/"+codunidad.substring(2,6);
                                if(log.isDebugEnabled()) log.debug("clave:"+clave);
                                //mirar si acreditado
                                //si ES ACREDITADO GRABAR EN CAMPO CLAVE_REGISTRAL Y ACTUALIZAR CONTADOR/AÑO
                                if (unidad.getCentroAcreditado().equals("0") && (unidad.getClaveRegistral()==null || unidad.getClaveRegistral().equals("null") || unidad.getClaveRegistral().equals(""))) {
                                    if(log.isDebugEnabled()) log.debug("ACTUALIZAR CLAVE ");
                                    result = meLanbide37DAO.actualizarClaveCentro(unidad, clave, con);
                                     if(result != 1){
                                        adaptador.rollBack(con);
                                        return "3";//fallo al actualizar la clave en la tabla
                                     }  
                                     result2 = meLanbide37DAO.guardarContador(codOrganizacion, ano.toString(), "APA", cont+"", con);

                                    if(result2 != 1){
                                       adaptador.rollBack(con);
                                       return "4";//fallo al actualizar cntador
                                   }


                                }


                            }
                            adaptador.finTransaccion(con);
                            resultado="0";
                        } else resultado="5";//no existen unidades

                    }else resultado="1";//no existe certificado
                }
            } catch(BDException e){
                if(transactionStarted)
                {
                    adaptador.rollBack(con);
                }
                log.error("Se ha producido una excepción en la BBDD guardando la clave registral APA para el expediente " + numExpediente, e);
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
                    log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
                }
            }
        }           
         
        return resultado; 
        
     }
    
    public Integer obtieneFechaPresentacion(int codOrganizacion, String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        //boolean existe=false;   
        Connection   con = null;
        Integer fecha = null;
        try{
            con = adaptador.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
            fecha = meLanbide37DAO.getValorCampoFechaPresentacion(codOrganizacion, numExpediente, con);
        } catch(Exception e){               
            log.error("Se ha producido una excepción al consultar la clave CP del expediente " + numExpediente, e);
            throw new Exception(e);
        }
        finally{
            if(con != null)
                con.close();
        }
        return fecha;
    }
    public boolean existeClave(int codOrganizacion, String clave, String numExpediente, String ejercicio, AdaptadorSQLBD adaptador) throws Exception {
        boolean existe=false;   
            Connection   con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
            String clavecp = meLanbide37DAO.getValorCampoTexto(codOrganizacion, numExpediente, ejercicio, ConstantesMeLanbide37.CAMPO_SUPLEMENTARIO_CLAVE_REGISTRAL_CP, con);
            if(clavecp != null)
            {
                existe = true;
            }
        } catch(Exception e){               
            log.error("Se ha producido una excepción al consultar la clave CP del expediente " + numExpediente, e);
            throw new Exception(e);
        }
        finally{
            if(con != null)
                con.close();
        }
        return existe;
    }
     
    public List<SelectItem> getListaDesplegable(String codDesplegable, Integer idioma, AdaptadorSQLBD adaptador)  throws Exception {
         Connection con = null;
        List<SelectItem> lista=null;
        MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
        try{
            con = adaptador.getConnection();
            lista = meLanbide37DAO.getListaDesplegable(codDesplegable, idioma, con);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando lista valores deplegable "+codDesplegable, e);
            throw new Exception(e);
        }catch(Exception e){
            log.debug("Error al obtener lista valores deplegable."+e.getMessage());
            throw new Exception(e);
        }finally
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
        return lista;
    }    
    
    public List<FilaListadoCPVO> getDatosListadoCP(String tipoAcreditacion, String valoracion, String codigoCP, String fecDesde, String fecHasta, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaListadoCPVO> retList = new ArrayList<FilaListadoCPVO>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
            retList = meLanbide37DAO.getDatosListadoCP(tipoAcreditacion,valoracion,codigoCP,fecDesde, fecHasta, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando datos del listado CP", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion recuperando datos del listado CP", ex);
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
        return retList;
    }
    
     public List<FilaListadoCompCPVO> getDatosListadoCompCP(String codigoCP, String fecDesde, String fecHasta, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaListadoCompCPVO> retList = new ArrayList<FilaListadoCompCPVO>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
            retList = meLanbide37DAO.getDatosListadoCompCP(codigoCP,fecDesde, fecHasta, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando datos del listado Comprobación CP", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion recuperando datos del listado Comprobación CP", ex);
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
        return retList;
    }
     
      public List<FilaListadoCompAPAVO> getDatosListadoCompAPA(String codigoCP, String fecDesde, String fecHasta, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaListadoCompAPAVO> retList = new ArrayList<FilaListadoCompAPAVO>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
            retList = meLanbide37DAO.getDatosListadoCompAPA(codigoCP,fecDesde, fecHasta, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando datos del listado Comprobación APA", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion recuperando datos del listado Comprobación APA", ex);
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
        return retList;
    }
      
      public ArrayList<Melanbide37RelacionExpVO> getRelacionExp (AdaptadorSQLBD adaptador){
        if(log.isDebugEnabled()) log.debug("getRelacionExp() : BEGIN");
        Connection con = null;
        ArrayList<Melanbide37RelacionExpVO> listaReports = new ArrayList<Melanbide37RelacionExpVO>();
        try{
            con = adaptador.getConnection();
            MeLanbide37DAO reportDao = MeLanbide37DAO.getInstance();
            listaReports = reportDao.getDatosRelacion(con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los reports", e);
            //throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDDrecuperando los reports",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los recuperando los reports",ex);
            //throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los recuperando los reports",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getRelacionExp() : END");
        return listaReports;
    }
      
      public ArrayList<FilaListadoExpedientesEMPNLVO> listadoExpedientesEMPNL (AdaptadorSQLBD adaptador){
        if(log.isDebugEnabled()) log.debug("listadoExpedientesEMPNL() : BEGIN  - manager ");
        Connection con = null;
        ArrayList<FilaListadoExpedientesEMPNLVO> listaReports = new ArrayList<FilaListadoExpedientesEMPNLVO>();
        try{
            con = adaptador.getConnection();
            MeLanbide37DAO reportDao = MeLanbide37DAO.getInstance();
            listaReports = reportDao.listadoExpedientesEMPNL(con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los reports - listadoExpedientesEMPNL", e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los recuperando los reports  - listadoExpedientesEMPNL",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage() + " - listadoExpedientesEMPNL");
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("listadoExpedientesEMPNL() : END - manager ");
        return listaReports;
    }

    public String actualizaDatosTodasUC_VistaMate(String nombreVistaMaterializada, AdaptadorSQLBD adapt) {
        log.debug("actualizaDatosTodasUC_VistaMate() : BEGIN - manager ");
        Connection con = null;
        String salidaProcedure="";
        try {
            con = adapt.getConnection();
            MeLanbide37DAO daoMelanbide37 = MeLanbide37DAO.getInstance();
            salidaProcedure = daoMelanbide37.actualizaDatosTodasUC_VistaMate(nombreVistaMaterializada,con);
        } catch (BDException e) {
            log.error("actualizaDatosTodasUC_VistaMate - Se ha producido una excepción en la BBDD al actualizar vista materializada " + nombreVistaMaterializada, e);
        } catch (Exception ex) {
            log.error("actualizaDatosTodasUC_VistaMate - Se ha producido una excepción general  al actualizar vista materializada " + nombreVistaMaterializada, ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("actualizaDatosTodasUC_VistaMate -Manager- Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        log.error("actualizaDatosTodasUC_VistaMate() : END - manager ");
        return salidaProcedure;
    }
    
    public List<ExpedienteCEPAPVO> busquedaFiltrandoListaExpedientes(ExpCEPAPCriteriosFiltroVO _criterioBusqueda, AdaptadorSQLBD adapt) throws Exception {
        List<ExpedienteCEPAPVO> lista = new ArrayList<ExpedienteCEPAPVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();

            lista = meLanbide37DAO.busquedaFiltrandoListaExpedientes(_criterioBusqueda, con);
            
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando Datos de lista de expedientes CEPAP - Entrega título", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos de lista de expedientes CEPAP - Entrega título ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error(" CEPAP - Entrega título  - Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public ArrayList<ExpedienteCEPAPVO> getExpedientesOficina(String oficina, AdaptadorSQLBD adapt) throws Exception {
        ArrayList<ExpedienteCEPAPVO> lista = new ArrayList<ExpedienteCEPAPVO>();
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();

            lista = meLanbide37DAO.getExpedientesOficina(oficina, con);
            
            return lista;
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando Datos de lista de expedientes CEPAP - Entrega título", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando datos de lista de expedientes CEPAP - Entrega título ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error(" CEPAP - Entrega título  - Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int marcarTitulosEntregado(int codOrganizacion, int codTramite, String[] listaExpedientesMarcadosStr,String codCampo,String meLanbide37Fecha_busqS, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean transactionStarted = false;
        int numExpedientesMarcados = 0;
        try {
            
            con = adapt.getConnection();
            MeLanbide37DAO meLanbide37DAO = MeLanbide37DAO.getInstance();
            
            adapt.inicioTransaccion(con);
            transactionStarted = true;
            
            for (int i = 0; i < listaExpedientesMarcadosStr.length; i++){
                String numExpediente = listaExpedientesMarcadosStr[i];
                String[] datos = numExpediente.split("/");
                String ejercicio = datos[0];
                int marcado = meLanbide37DAO.marcarTituloEntregado(codOrganizacion, codTramite, ejercicio, numExpediente, con);
                log.info("el valor de ejercicio marcar titulo es : " + ejercicio);
                if (marcado == 1){
                    numExpedientesMarcados++;
                }
               
                try{
                    // meLanbide37DAO.grabarFechaEntregaTitulo(numExpediente, con)
                   meLanbide37DAO.grabarFechaEntregaTitulo(codOrganizacion, numExpediente, ejercicio,meLanbide37Fecha_busqS,con);
                   log.info("el valor de ejercicio grabar Fecha es : " + ejercicio + meLanbide37Fecha_busqS+ numExpediente+codCampo);
                   log.info("el valor de ejercicio grabar Fecha es : " + ejercicio);
                   log.info("el valor de ejercicio grabar Fecha es : " + ejercicio);
                   log.info("el valor de ejercicio grabar Fecha es : " + ejercicio);
                   log.info("el valor de ejercicio grabar Fecha es : " + ejercicio);
                   log.info("el valor de ejercicio grabar Fecha es : " + ejercicio + meLanbide37Fecha_busqS+ numExpediente+codCampo);

                                                                                                              
                   
                }catch (Exception e){
                
                }
            }
            adapt.finTransaccion(con);

            return numExpedientesMarcados;
        } catch (BDException e) {
            log.error("Error al marcar título entregado - CEPAP - Entrega título", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error al marcar título entregado - CEPAP - Entrega título", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error(" CEPAP - Entrega título  - Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
            }
     public String getDescripcionDesplegableByIdioma(int idioma, String descripcion ) {
       log.info("getDescripcionDesplegable : descripcion " + descripcion + "idioma " + idioma);
        String barraIdioma = "\\|" ;
        try {
            if (!descripcion.isEmpty()) {
                String[] descripcionDobleIdioma = descripcion.split(barraIdioma);
                if (descripcionDobleIdioma != null && descripcionDobleIdioma.length >1) {
                    if (idioma == 4) {
                        descripcion = descripcionDobleIdioma[1];
                    } else {
                        // Cogemos la primera posicion que deberia ser castellano
                        descripcion = descripcionDobleIdioma[0];
                    }
                } else{
                    log.info("El tamano del no es valido " + descripcion);
                }
            } else {
                descripcion = "-";
            }
            return descripcion;
        } catch (Exception e) {
            return descripcion;
        }
    }
      
}//class
