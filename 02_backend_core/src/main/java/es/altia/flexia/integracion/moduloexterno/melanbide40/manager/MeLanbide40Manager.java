/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide40.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide40.dao.CerCertificadosDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.dao.CerModulosFormativosDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.dao.MeLanbide40CertificadoDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.dao.MeLanbide40DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.dao.MeLanbide40ModPractDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.util.ConstantesMeLanbide40;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CerModuloFormativoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.CertificadoMPVO;

import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.S75PagosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.ValoresCalculo;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide40Manager 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide40Manager.class);
    
    //Instancia
    private static MeLanbide40Manager instance = null;
    
    private MeLanbide40Manager()
    {
        
    }
    
    public static MeLanbide40Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide40Manager.class)
            {
                instance = new MeLanbide40Manager();
            }
        }
        return instance;
    }    
    
    /**
     * Comprueba si para un expediente existe un certificado asociado
     * @param certificado
     * @param adaptador
     * @return Boolean
     * @throws Exception 
     */
    public Boolean existeCertificadoExpediente(CerCertificadoVO certificado, AdaptadorSQLBD adaptador) throws Exception{
        if(log.isDebugEnabled()) log.debug("existeCertificadoExpediente() : BEGIN");
        Boolean existe = false;
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide40CertificadoDAO meLanbide40CertificadoDao = MeLanbide40CertificadoDAO.getInstance();
            String codCertificado = meLanbide40CertificadoDao.existeCertificadoExpediente(certificado, con);
            if(codCertificado!=null && !"".equals(codCertificado))
                existe = true;
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD borrando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción borrando los datos del certificado en el expediente =  " + certificado.getNumExpediente(),ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("existeCertificadoExpediente() : END");
        return existe;
    }//existeCertificadoExpediente
    
    /**
     * Recupera la información sobre un certificado para un expediente por el número de expediente
     * @param numExpediente
     * @param codOrganizacion
     * @param adaptador
     * @return CerCertificadoVO
     * @throws Exception 
     */
    public CerCertificadoVO getCertificadoExpediente(String numExpediente, int codOrganizacion, AdaptadorSQLBD adaptador) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCertificadoExpediente() : BEGIN");
        Connection con = null;
        CerCertificadoVO certificado = new CerCertificadoVO();
        String codCertificadoExpediente = new String();
        try{
            con = adaptador.getConnection();
            if(log.isDebugEnabled()) log.debug("Recuperamos el código del certificado asociado al expediente");
            MeLanbide40CertificadoDAO meLanbide40CertificadoDao = MeLanbide40CertificadoDAO.getInstance();
            codCertificadoExpediente = meLanbide40CertificadoDao.getCertificadoExpediente(numExpediente, codOrganizacion, con);
            
            if(codCertificadoExpediente != null){
                certificado.setNumExpediente(numExpediente);
                certificado.setCodOrganizacion(codOrganizacion);
                certificado = getCertificado(codCertificadoExpediente, adaptador);
                /*certificado.setArea(getArea(adaptador, certificado.getCodArea()));
                CerAreaVO area = certificado.getArea();
                area.setFamilia(getFamilia(area.getCodFamilia(), adaptador));
                certificado.setArea(area);*/
            }//if(codCertificadoExpediente != null)
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando el certificado asociado al expediente =  " + certificado.getNumExpediente(), e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando el certificado asociado al expediente =  " + certificado.getNumExpediente(),ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCertificadoExpediente() : END");
        return certificado;
    }//getCertificadoExpediente
    
    /**
     * Devuelve la información sobre un certificado por su código
     * @param codCertificado
     * @param adaptador
     * @return CerCertificadoVO
     * @throws BDException
     * @throws Exception 
     */
    public CerCertificadoVO getCertificado (String codCertificado, AdaptadorSQLBD adaptador) throws BDException, Exception{
        if(log.isDebugEnabled()) log.debug("getCertificado( codCertificado = " + codCertificado + " ) : BEGIN");
        CerCertificadoVO certificado = new CerCertificadoVO();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            CerCertificadosDAO cerCertificadosDao = CerCertificadosDAO.getInstance();
            certificado = cerCertificadosDao.getCertificado(codCertificado, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando el certificado con código = " + codCertificado, e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando el certificado con código = " + codCertificado,ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCertificado() : END");
        return certificado;
    }//getCertificado
    
    /**
     * Devuelve la lista con los códigos y descripciones de los certificados
     * @param adaptador
     * @return ArrayList<CerCertificadoVO> (Solo incluye el código y la descripción)
     * @throws BDException
     * @throws Exception 
     */
    public ArrayList<CerCertificadoVO> getCertificados(AdaptadorSQLBD adaptador) throws BDException, Exception{
        if(log.isDebugEnabled()) log.debug("getCertificados() : BEGIN");
        ArrayList<CerCertificadoVO> listaCertificados = new ArrayList<CerCertificadoVO>();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            CerCertificadosDAO cerCertificadosDao = CerCertificadosDAO.getInstance();
            listaCertificados = cerCertificadosDao.getCertificados(con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los certificados", e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los certificados",ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCertificados() : END");
        return listaCertificados;
    }//getCertificados
    
    /**
     * Recupera los modulos formativos para un certificado
     * 
     * @param codCertificado
     * @param adaptador
     * @return ArrayList<CerModuloFormativoVO>
     * @throws Exception 
     */
    public ArrayList<CerModuloFormativoVO> getModulosFormativos(String codCertificado, AdaptadorSQLBD adaptador) throws Exception{
        if(log.isDebugEnabled()) log.debug("getModulosFormativos() : BEGIN");
        ArrayList<CerModuloFormativoVO> listaModulos = new ArrayList<CerModuloFormativoVO>();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            CerModulosFormativosDAO modulosFormativosDao = CerModulosFormativosDAO.getInstance();
            listaModulos = modulosFormativosDao.getModulosFormativos(codCertificado, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los modulos formativos", e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los modulos formativos",ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getModulosFormativos() : END");
        return listaModulos;
    }//getModulosFormativos
    
    
    /**/
    public String[] getExpedientesCEPAP(String codCertificado, String numExpediente, AdaptadorSQLBD adaptador)throws Exception{
       String[] listaexpedientes =null;
       Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide40DAO meLanbide40DAO = MeLanbide40DAO.getInstance();
            listaexpedientes=meLanbide40DAO.getDatosExpedientesCEPAP(numExpediente,codCertificado, con);
        }catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando terceros del expediente " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando terceros del expediente " + numExpediente, ex);
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
            
       return listaexpedientes;
    }
    
    
    public ArrayList<CerModuloFormativoVO> getInfoExpedientesCEPAP(int codOrganizacion,String codCertificado, String[] expedientes, AdaptadorSQLBD adaptador)throws Exception{
        Connection con = null;
        ArrayList<CerModuloFormativoVO> modulos=new ArrayList<CerModuloFormativoVO>();
        try{
            con = adaptador.getConnection();
            MeLanbide40ModPractDAO modPracDAO =  MeLanbide40ModPractDAO.getInstance();
            
            for (int i=0; i<expedientes.length;i++){
                CerModuloFormativoVO modulo = modPracDAO.getModuloFormativo(expedientes[i], codOrganizacion, con);
                if(modulo!=null && modulo.getNumExpediente()!=null)
                    modulos.add(modulo);
            }
            //modulos = getModulosFormativos (String codCertificado, Connection con) throws Exception{
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los modulos formativos", e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los modulos formativos",ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        return modulos;
    }
    
    
    
    
    
    
    
    
    public BigDecimal getValorCampoNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide40DAO meLanbide40DAO = MeLanbide40DAO.getInstance();
            return meLanbide40DAO.getValorCampoNumerico(codOrganizacion, numExp, ejercicio, codigoCampo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando el valor para el campo suplementario " + codigoCampo, ex);
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
    
        
    public boolean guardarDatos(int codOrganizacion, CerCertificadoVO certificado, CerModuloFormativoVO modulo, String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        boolean transactionStarted = false;
        try
        {
            int result = 0;
            
            con = adaptador.getConnection();
            MeLanbide40CertificadoDAO meLanbide40CertDAO = MeLanbide40CertificadoDAO.getInstance();            
            boolean nuevo = false;
            adaptador.inicioTransaccion(con);
            transactionStarted = true;
           
            
            if (existeCertificadoExpediente(certificado, adaptador)){
                String codCertificadoViejo = meLanbide40CertDAO.existeCertificadoExpediente(certificado, con);
                meLanbide40CertDAO.actualizarCertificadoExpediente(certificado, modulo, codCertificadoViejo ,con);
            }else           
                meLanbide40CertDAO.insertarCertificadoExpediente(certificado, modulo,  con);
            /*if(result != 1)
            {
                adaptador.rollBack(con);
                return false;
            }*/
            
            
            adaptador.finTransaccion(con);
            return true;
        }
        catch(BDException e)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepción en la BBDD guardando datos  para el expediente " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepción en la BBDD guardando datos para el expediente " + numExpediente, ex);
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
    
   
    
     public boolean acreditar(int codOrganizacion, String[] expedientes, String certificado,String modulo,AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        boolean transactionStarted = false;
        try
        {
            int result = 0;            
            con = adaptador.getConnection();
            MeLanbide40CertificadoDAO meLanbide40DAO = MeLanbide40CertificadoDAO.getInstance();            
            boolean nuevo = false;
            adaptador.inicioTransaccion(con);
            transactionStarted = true;           
            
           
            meLanbide40DAO.acreditarModulo(expedientes, certificado, modulo, con);
            
            
            adaptador.finTransaccion(con);
            return true;
        }
        catch(BDException e)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepción en la BBDD guardando acreditado para expedientes " , e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            if(transactionStarted)
            {
                adaptador.rollBack(con);
            }
            log.error("Se ha producido una excepción en la BBDD guardando acreditado para expedientes " , ex);
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
    
    
}
