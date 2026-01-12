/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide03.manager;

import es.altia.agora.business.util.GeneralValueObject;
import es.altia.flexia.integracion.lanbide.impresion.ImpresionExpedientesLanbideValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide03.dao.*;
import es.altia.flexia.integracion.moduloexterno.melanbide03.exception.MELANBIDE03Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.*;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.reports.Subreport;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.InteresadoExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import es.altia.util.io.IOOperations;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
//import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRPdfExporter;
//import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 13-08-2012 * #53275 Edición inicial</li>
 * </ol> 
 */
public class MeLanbide03Manager {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide03Manager.class);
    
    //Instancia
    private static MeLanbide03Manager instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide03Manager, si no existe la crea.
     * @return 
     */
    public static MeLanbide03Manager getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide03Manager.class){
                if(instance == null){
                    instance = new MeLanbide03Manager();
                }//if(instance == null)
            }//synchronized(MeLanbide03Manager.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
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
     * Devuelve la lista con los códigos y descripciones de los certificados
     * @param adaptador
     * @return ArrayList<CerCertificadoVO> (Solo incluye el código y la descripción)
     * @throws BDException
     * @throws Exception 
     */
    public ArrayList<ValorCampoDesplegableModuloIntegracionVO> getListaCentrosLanF(AdaptadorSQLBD adaptador) throws BDException, Exception{
        if(log.isDebugEnabled()) log.debug("getListaCentrosLanF() : BEGIN");
        ArrayList<ValorCampoDesplegableModuloIntegracionVO> listaCentrosLanF = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide03CertCentroDAO cerCentroDao = MeLanbide03CertCentroDAO.getInstance();
            listaCentrosLanF = cerCentroDao.getComboCentrosLanF(con);
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
        return listaCentrosLanF;
    }//getListaCentrosLanF
    
    /**
     * Devuelve la información sobre un certificado por su código
     * @param codCertificado
     * @param adaptador
     * @return CerCertificadoVO
     * @throws BDException
     * @throws Exception 
     */
    public CerCertificadoVO getCertificado (String codCertificado, AdaptadorSQLBD adaptador) throws BDException, Exception{
        Connection con = null;
        try{
            con = adaptador.getConnection();
            return getCertificado(codCertificado, con);
        }catch(Exception ex){
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
    }
    /**
     * Devuelve la información sobre un certificado por su código
     * @param codCertificado
     * @param con
     * @return CerCertificadoVO
     * @throws BDException
     * @throws Exception 
     */
    public CerCertificadoVO getCertificado (String codCertificado, Connection con) throws BDException, Exception{
        if(log.isDebugEnabled()) log.debug("getCertificado( codCertificado = " + codCertificado + " ) : BEGIN");
        CerCertificadoVO certificado = new CerCertificadoVO();
        try{
            CerCertificadosDAO cerCertificadosDao = CerCertificadosDAO.getInstance();
            certificado = cerCertificadosDao.getCertificado(codCertificado, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando el certificado con código = " + codCertificado, e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando el certificado con código = " + codCertificado,ex);
            throw new Exception(ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getCertificado() : END");
        return certificado;
    }//getCertificado
    
    /**
     * Devuelve el área de un certificado por su código
     * @param adaptador
     * @param codArea
     * @return CerAreaVO
     * @throws Exception 
     */
    public CerAreaVO getArea (AdaptadorSQLBD adaptador, String codArea) throws Exception{
        Connection con = null;
        try{
            con = adaptador.getConnection();
            return getArea(con, codArea);
        }catch(Exception ex){
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
    }
    /**
     * Devuelve el área de un certificado por su código
     * @param con
     * @param codArea
     * @return CerAreaVO
     * @throws Exception 
     */
    public CerAreaVO getArea (Connection con, String codArea) throws Exception{
        if(log.isDebugEnabled()) log.debug("getArea ( codArea = " + codArea + " ) : BEGIN");
        CerAreaVO area = new CerAreaVO();
        try{
            CerAreasDAO cerAreaDao = CerAreasDAO.getInstance();
            area = cerAreaDao.getArea(con, codArea);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando el área con código = " + codArea, e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando el área con código =  " + codArea,ex);
            throw new Exception(ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getArea() : END");
        return area;
    }//getArea
    
    /**
     * Devuelve la familia de un certificado por su código
     * @param codFamilia
     * @param adaptador
     * @return CerFamiliaVO
     * @throws Exception 
     */
    public CerFamiliaVO getFamilia(String codFamilia, AdaptadorSQLBD adaptador) throws Exception{
        Connection con = null;
        try{
            con = adaptador.getConnection();
            return getFamilia(codFamilia, con);
        }catch(Exception ex){
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
    }
    /**
     * Devuelve la familia de un certificado por su código
     * @param codFamilia
     * @param con
     * @return CerFamiliaVO
     * @throws Exception 
     */
    public CerFamiliaVO getFamilia(String codFamilia, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getFamilia ( codFamilia = " + codFamilia + " ) : BEGIN");
        CerFamiliaVO familia = new CerFamiliaVO();
        try{
            CerFamiliaDAO cerFamiliaDao = CerFamiliaDAO.getInstance();
            familia = cerFamiliaDao.getFamilia(codFamilia, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando la familia con código = " + codFamilia, e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando la familia con código =  " + codFamilia,ex);
            throw new Exception(ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getFamilia() : END");
        return familia;
    }//getFamilia
    
    /**
     * Devuelve la lista de unidades competenciales por el código del certificado
     * @param codCertificado
     * @param adaptador
     * @return ArrayList<CerUnidadeCompetencialVO>
     * @throws Exception 
     */
    public ArrayList<CerUnidadeCompetencialVO> getListaUnidadesCompetenciales (String codCertificado, AdaptadorSQLBD adaptador) throws Exception{
        Connection con = null;
        try{
            con = adaptador.getConnection();
            return getListaUnidadesCompetenciales(codCertificado, con);
        }catch(Exception ex){
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
    }
    /**
     * Devuelve la lista de unidades competenciales por el código del certificado
     * @param codCertificado
     * @param con
     * @return ArrayList<CerUnidadeCompetencialVO>
     * @throws Exception 
     */
    public ArrayList<CerUnidadeCompetencialVO> getListaUnidadesCompetenciales (String codCertificado, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getListaUnidadesCompetenciales ( codCertificado = " + codCertificado + " ) : BEGIN");
        ArrayList<CerUnidadeCompetencialVO> listaUnidades = new ArrayList<CerUnidadeCompetencialVO>();
        try{
            CerUnidadesCompetenciaDAO cerUnidadesCompetenciaDao = CerUnidadesCompetenciaDAO.getInstance();
            listaUnidades = cerUnidadesCompetenciaDao.getListaUnidadesCompetencia(codCertificado, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando las unidades de competencia para el certificado con código = " + codCertificado, e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando las unidades de competencia para el certificado con código =  " + codCertificado,ex);
            throw new Exception(ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getListaUnidadesCompetenciales () : END");
        return listaUnidades;
    }//getListaUnidadesCompetenciales
    
    /**
     * Recupera los centros
     * @param adaptador
     * @return ArrayList<CerCentroVO>
     * @throws Exception 
     */
    public ArrayList<CerCentroVO> getCentros (AdaptadorSQLBD adaptador) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCentros() : BEGIN");
        ArrayList<CerCentroVO> listaCentros = new ArrayList<CerCentroVO>();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            CerCentrosDAO cerCentrosDao = CerCentrosDAO.getInstance();
            //listaCentros = cerCentrosDao.getCentros(con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los centros ", e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los centros ",ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCentros() : END");
        return listaCentros;
    }//getCentros
    
    /**
     * Inserta información sobre el certificado, incluye las unidades competenciales
     * @param certificado
     * @param adaptador
     * @throws Exception   */   
    public boolean insertarCertificadoExpediente (CerCertificadoVO certificado, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : BEGIN");
        boolean exito = false;
        Connection con = null;
        
        try{
            con = adaptador.getConnection();
            con.setAutoCommit(false);
            
            MeLanbide03CertificadoDAO meLanbide03CertificadoDao = MeLanbide03CertificadoDAO.getInstance();
            if(log.isDebugEnabled()) log.debug("Comprobamos si existe ya un certificado para el expediente");
            String codCertificadoViejo = meLanbide03CertificadoDao.existeCertificadoExpediente(certificado, con);
            
            if(codCertificadoViejo!=null && !"".equals(codCertificadoViejo)){
                if(log.isDebugEnabled()) log.debug("Existe un registro de certificado para ese expediente, lo actualizamos");
                meLanbide03CertificadoDao.actualizarCertificadoExpediente(certificado,codCertificadoViejo, con);
            }else{
                if(log.isDebugEnabled()) log.debug("No existe registro de certificado para ese expediente, lo creamos");
                meLanbide03CertificadoDao.insertarCertificadoExpediente(certificado, con);
            }
            
            con.commit();
            exito = true;
            
        }catch(BDException e){            
            log.error("Se ha producido una excepción en la BBDD insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);            
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);
        }catch(SQLException ex){
            try{
                con.rollback();
            }catch(SQLException f){
                log.error("Error al realizar un rollback al insertar/actualizar el certificado: " + f.getMessage());
            }
            log.error("Se ha producido una excepción insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(),ex);            
            throw new MELANBIDE03Exception("Se ha producido una excepción insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(),ex);
        }catch(Exception e){            
            log.error("Se ha producido una excepción en la BBDD insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);            
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);
        }finally{
            
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : END");        
        return exito;
        
    }//insertarCertificadoExpediente
    
    
    
    
    public boolean insertarCertificadoExpediente (CerCertificadoVO certificado, boolean existeCertificado, Connection con) throws MELANBIDE03Exception,SQLException{
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : BEGIN");
        boolean exito = false;
                
        try{
            //nuevo
            MeLanbide03CertificadoDAO meLanbide03CertificadoDao = MeLanbide03CertificadoDAO.getInstance();
            if (existeCertificado){
                if(log.isDebugEnabled()) log.debug("Actualizamos el certficado a :"+certificado.getCodCertificado());
                meLanbide03CertificadoDao.actualizarCertificadoExpediente(certificado, con);
            }else {
                if(log.isDebugEnabled()) log.debug("Insertamos certificado "+certificado.getCodCertificado());
                meLanbide03CertificadoDao.insertarCertificadoExpediente(certificado, con);
            }
            
            
           
           /* if(log.isDebugEnabled()) log.debug("Comprobamos si existe ya un certificado para el expediente");
            String codCertificadoViejo = meLanbide03CertificadoDao.existeCertificadoExpediente(certificado, con);
            
            
            if(codCertificadoViejo!=null && !"".equals(codCertificadoViejo)){
                if(log.isDebugEnabled()) log.debug("Existe un registro de certificado para ese expediente, lo actualizamos");
                meLanbide03CertificadoDao.actualizarCertificadoExpediente(certificado,codCertificadoViejo, con);
            }else{
                if(log.isDebugEnabled()) log.debug("No existe registro de certificado para ese expediente, lo creamos");
                meLanbide03CertificadoDao.insertarCertificadoExpediente(certificado, con);
            }*/
            
            
            exito = true;
            
        }catch(BDException e){            
            log.error("Se ha producido una excepción en la BBDD insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);            
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);
        }catch(SQLException ex){        
            log.error("Se ha producido una excepción insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(),ex);            
            throw ex;
        }catch(Exception e){            
            log.error("Se ha producido una excepción en la BBDD insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);            
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);
        }
        if(log.isDebugEnabled()) log.debug("insertarCertificadoExpediente() : END");        
        return exito;
        
    }//insertarCertificadoExpediente
       
       
    
    
    /**
     * Borra la información sobre un certificado asociada a un expediente, incluye las unidades competenciales
     * @param certificado
     * @param adaptador
     * @throws Exception 
     */
    public void borrarCertificadoExpediente (CerCertificadoVO certificado, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("borrarCertificadoExpediente() : BEGIN");
        Connection con = null;
        try{
            con = adaptador.getConnection();
            con.setAutoCommit(false);
            
            MeLanbide03CertificadoDAO meLanbide03CertificadoDao = MeLanbide03CertificadoDAO.getInstance();
            meLanbide03CertificadoDao.borrarCertificadoExpediente(certificado, con);
            con.commit();
            
        }catch(BDException e){
            try{
                con.rollback();
            }catch(SQLException f){
                log.error("Error al realizar un rollback de la operación borrarCertificadoExpediente: " + f.getMessage());
            }
            log.error("Se ha producido una excepción en la BBDD borrando los datos del certificado en el expediente =  " + certificado.getNumExpediente(), e);
            throw new MELANBIDE03Exception("Error al obtener una conexión a la BBDD",e);
            
        }catch(SQLException ex){
            try{
                con.rollback();
            }catch(SQLException f){
                log.error("Error al realizar un rollback de la operación borrarCertificadoExpediente: " + f.getMessage());
            }
            log.error("Se ha producido una excepción borrando los datos del certificado en el expediente =  " + certificado.getNumExpediente(),ex);
            throw new MELANBIDE03Exception("Error al ejecutar la operación  borrarCertificadoExpediente contra la BBDD",ex);
            
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
            
        }
        if(log.isDebugEnabled()) log.debug("borrarCertificadoExpediente() : END");
        
    }//borrarCertificadoExpediente
    
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
            MeLanbide03CertificadoDAO meLanbide03CertificadoDao = MeLanbide03CertificadoDAO.getInstance();
            String codCertificado = meLanbide03CertificadoDao.existeCertificadoExpediente(certificado, con);
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
     * @param con
     * @return CerCertificadoVO
     * @throws Exception 
     */
    public CerCertificadoVO getCertificadoExpediente(String numExpediente, int codOrganizacion, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCertificadoExpediente() : BEGIN");
        CerCertificadoVO certificado = new CerCertificadoVO();
        String codCertificadoExpediente = new String();
        try{
            if(log.isDebugEnabled()) log.debug("Recuperamos el código del certificado asociado al expediente");
            MeLanbide03CertificadoDAO meLanbide03CertificadoDao = MeLanbide03CertificadoDAO.getInstance();
            codCertificadoExpediente = meLanbide03CertificadoDao.getCertificadoExpediente(numExpediente, codOrganizacion, con);
            if(log.isDebugEnabled()) log.debug("Certificado asociado al expediente: "+codCertificadoExpediente);
            if(codCertificadoExpediente != null){
                certificado.setNumExpediente(numExpediente);
                certificado.setCodOrganizacion(codOrganizacion);
                certificado = getCertificado(codCertificadoExpediente, con);
                certificado.setArea(getArea(con, certificado.getCodArea()));
                CerAreaVO area = certificado.getArea();
                area.setFamilia(getFamilia(area.getCodFamilia(), con));
                certificado.setArea(area);
            }//if(codCertificadoExpediente != null)
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando el certificado asociado al expediente =  " + certificado.getNumExpediente(), e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando el certificado asociado al expediente =  " + certificado.getNumExpediente(),ex);
            throw new Exception(ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getCertificadoExpediente() : END");
        return certificado;
    }//getCertificadoExpediente
    /**
     * Recupera la información sobre un certificado para un expediente por el número de expediente
     * @param numExpediente
     * @param codOrganizacion
     * @param adaptador
     * @return CerCertificadoVO
     * @throws Exception 
     */
    public CerCertificadoVO getCertificadoExpediente(String numExpediente, int codOrganizacion, AdaptadorSQLBD adaptador) throws Exception{
        Connection con = null;
        try{
            con = adaptador.getConnection();
            return getCertificadoExpediente(numExpediente, codOrganizacion, con);
        }catch(Exception ex){
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
    }//getCertificadoExpediente
    
    /**
     * Recupera la información sobre los centros de gasto y su acreditación asociados a las unidades competenciales que tenga el certificado
     * seleccionado para el expediente
     * @param numExpediente
     * @param codCertificado
     * @param codOrganizacion
     * @param adaptador
     * @return ArrayList<CerUnidadeCompetencialVO>
     * @throws Exception 
     */
    public ArrayList<CerUnidadeCompetencialVO> getCentrosExpedienteYCertificado (String numExpediente, String codCertificado, String codOrganizacion, AdaptadorSQLBD adaptador) throws Exception{
        Connection con = null;
        try{
            con = adaptador.getConnection();
            return getCentrosExpedienteYCertificado(numExpediente, codCertificado, codOrganizacion, con);
        }catch(Exception ex){
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
    }
    /**
     * Recupera la información sobre los centros de gasto y su acreditación asociados a las unidades competenciales que tenga el certificado
     * seleccionado para el expediente
     * @param numExpediente
     * @param codCertificado
     * @param codOrganizacion
     * @param adaptador
     * @return ArrayList<CerUnidadeCompetencialVO>
     * @throws Exception 
     */
    public ArrayList<CerUnidadeCompetencialVO> getCentrosExpedienteYCertificado (String numExpediente, String codCertificado, String codOrganizacion, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCentrosExpedienteYCertificado() : BEGIN");
        ArrayList<CerUnidadeCompetencialVO> unidades = new ArrayList<CerUnidadeCompetencialVO>();
        try{
            MeLanbide03CertCentroDAO meLanbideCertCentroDao = MeLanbide03CertCentroDAO.getInstance();
            unidades = meLanbideCertCentroDao.getCentrosExpedienteYCertificado(numExpediente, codCertificado, codOrganizacion, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando las unidades competenciales asociadas al expediente =  " + numExpediente, e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando recuperando las unidades competenciales asociadas al expediente =  " + numExpediente,ex);
            throw new Exception(ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getCentrosExpedienteYCertificado() : END");
        return unidades;
    }//getCentrosExpedienteYCertificado
    
    
    
  /***
     * Recupera los documentos de los interesados de un expediente
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param con: Conexión a la BBDD
     * @return  ArrayList<String> 
     */
    public ArrayList<String> getDocumentosInteresadosExpediente(String codOrganizacion,String numExpediente,Connection con){
        ArrayList<String> salida = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try{
            String[] datosExpediente = numExpediente.split("/");
            String ejercicio = datosExpediente[0];
            String codProcedimiento = datosExpediente[1];
            
            String sql = "SELECT TER_DOC FROM E_EXT,T_TER WHERE EXT_EJE=? AND EXT_MUN=? AND EXT_NUM=? AND EXT_PRO=?" + 
                         "AND EXT_TER=TER_COD";
            log.debug(sql);
            
            int i=1;
            ps = con.prepareStatement(sql);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setInt(i++, Integer.parseInt(codOrganizacion));
            ps.setString(i++, numExpediente);
            ps.setString(i++, codProcedimiento);
            
            rs = ps.executeQuery();
            while(rs.next()){
                salida.add(rs.getString("TER_DOC"));
            }
            
            
        }catch(SQLException e){
            log.error("Error : ", e);
        }finally{
            try{
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
                
            }catch(SQLException e){
                log.error("Error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
        }
        return salida;
    }
    
    
    /**
     * Recupera las unidades competenciales que un usuario ya tiene acreditadas
     * 
     * @param unidades
     * @param numExpediente
     * @param codCertificado
     * @param codOrganizacion
     * @param adaptador
     * @return ArrayList<CerUnidadeCompetencialVO>
     * @throws Exception 
     */
    public ArrayList<CerUnidadeCompetencialVO> getUnidadesYaAcreditadasInteresado(ArrayList<CerUnidadeCompetencialVO> unidades, String numExpediente, String codCertificado, String codOrganizacion, AdaptadorSQLBD adaptador) throws Exception{
        Connection con = null;
        try{
            con = adaptador.getConnection();
            return getUnidadesYaAcreditadasInteresado(unidades, numExpediente, codCertificado, codOrganizacion, con);
        }catch(Exception ex){
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
    }
    
    /**
     * Recupera las unidades competenciales que un usuario ya tiene acreditadas
     * 
     * @param unidades
     * @param numExpediente
     * @param codCertificado
     * @param codOrganizacion
     * @param con
     * @return ArrayList<CerUnidadeCompetencialVO>
     * @throws Exception 
     */
    public ArrayList<CerUnidadeCompetencialVO> getUnidadesYaAcreditadasInteresado(ArrayList<CerUnidadeCompetencialVO> unidades, String numExpediente, String codCertificado, String codOrganizacion, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.error("getUnidadesYaAcreditadasInteresado() : BEGIN");
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadasTerceros = new ArrayList<CerUnidadeCompetencialVO>();
        
        Hashtable<String,ArrayList<String>> CACHE_DOCUMENTOS_INTERESADOS = new Hashtable<String, ArrayList<String>>();
                
        try{
            ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadasCertificado = new ArrayList<CerUnidadeCompetencialVO>();
            MeLanbide03CertCentroDAO meLanbideCertCentroDao = MeLanbide03CertCentroDAO.getInstance();
            unidadesAcreditadasCertificado = meLanbideCertCentroDao.getCodigosUnidadesYaAcreditadas(unidades, codCertificado, codOrganizacion, numExpediente, con);
            if(unidadesAcreditadasCertificado.size() > 0){
                if(log.isDebugEnabled()) log.debug("Existen unidades acreditadas para ese certificado en otros expedientes");
                if(log.isDebugEnabled()) log.error("Recuperamos los interesados de nuestro expediente");
                
                if(log.isDebugEnabled()) log.debug("Recorremos el array de unidades acreditadas para recuperar el número del expediente y comprobar"
                        + " si el interesado del expediente seleccionado lo es en alguno de los acreditados");
                String numExpedienteAux = numExpediente;
                String[] numExpedienteAuxSplit = numExpedienteAux.split("/");
                String codProcedimientoAux  = numExpedienteAuxSplit[1];
                String ejercicioAux = numExpedienteAuxSplit[0];
                
                SalidaIntegracionVO salidaExpediente = el.getExpediente(codOrganizacion, numExpedienteAux, codProcedimientoAux, ejercicioAux);
                ExpedienteModuloIntegracionVO expediente = salidaExpediente.getExpediente();
                ArrayList<InteresadoExpedienteModuloIntegracionVO> interesadosExpediente = expediente.getInteresados();
                
                if(interesadosExpediente.size() > 0){
                    if(log.isDebugEnabled()) log.error("El expediente tiene interesados");
                    
                    /** Para las unidades ya acreditadas en otros expediente, se recuperan sus interesados y se comprueban
                        si alguno de ellos coincide del expediente actual **/
                    for(CerUnidadeCompetencialVO unidad : unidadesAcreditadasCertificado){
                        String numExpedienteAcreditado = unidad.getNumExpediente();
                        String[] numExpedienteSplit = numExpedienteAcreditado.split("/");
                              
                        ArrayList<String> documentos = this.getDocumentosInteresadosExpediente(codOrganizacion,numExpedienteAcreditado,con);

                        
                         for(InteresadoExpedienteModuloIntegracionVO interesadoExpediente : interesadosExpediente){
                            for(int i=0;i<documentos.size();i++){
                                if(interesadoExpediente.getDocumento().equalsIgnoreCase(documentos.get(i))){
                                    if(log.isDebugEnabled()) log.error("El interesado coincide "+interesadoExpediente.getDocumento());
                                    if(añadirUnidadCompetencial(unidadesAcreditadasCertificado, unidad)){
                                        unidadesAcreditadasTerceros.add(unidad);log.error("El interesado coincide");
                                    }
                                }
                            }
                        }//for                        
                        
                    }// for
                    
                    
                    
                    /***
                    for(CerUnidadeCompetencialVO unidad : unidadesAcreditadasCertificado){
                        String numExpedienteAcreditado = unidad.getNumExpediente();
                        String[] numExpedienteSplit = numExpedienteAcreditado.split("/");
                        String codProcedimiento  = numExpedienteSplit[1];
                        String ejercicio = numExpedienteSplit[0];

                        SalidaIntegracionVO salida = el.getExpediente(codOrganizacion, numExpedienteAcreditado, codProcedimiento, ejercicio);
                        ExpedienteModuloIntegracionVO expedienteAcreditado = salida.getExpediente();
                        ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados = expedienteAcreditado.getInteresados();

                        if(log.isDebugEnabled()) log.debug("Comprobamos si alguno de los interesados de nuestro expediente está incluido"
                                + " en los interesados de los expedientes asociados a las unidades competenciales del certificado "
                                + "seleccionado");
                        for(InteresadoExpedienteModuloIntegracionVO interesadoExpediente : interesadosExpediente){
                            for(InteresadoExpedienteModuloIntegracionVO interesado : interesados){
                                if(interesadoExpediente.getDocumento().equalsIgnoreCase(interesado.getDocumento())){
                                    if(log.isDebugEnabled()) log.debug("El interesado coincide");
                                    if(añadirUnidadCompetencial(unidadesAcreditadasCertificado, unidad)){
                                        unidadesAcreditadasTerceros.add(unidad);
                                    }
                                }
                            }
                        }//for
                    }//for **/
                    
                    
                }//if
                
                
                
            }//if
            
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando las unidades competenciales acreditadas para el usuario =  " + numExpediente, e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando recuperando las unidades competenciales acreditadas para el usuario =  " + numExpediente,ex);
            throw new Exception(ex);
        }//try-catch
        if(log.isDebugEnabled()) log.error("Número de unidades ya acreditadas = " + unidadesAcreditadasTerceros.size());
        if(log.isDebugEnabled()) log.error("getUnidadesYaAcreditadasInteresado() : END");
        return unidadesAcreditadasTerceros;
    }//getUnidadesYaAcreditadasInteresado
    
    /**
     * Añade una lista de unidades competenciales a un expediente
     * 
     * @param unidadesAcreditadasCertificado
     * @param unidad
     * @return 
     */
    private boolean añadirUnidadCompetencial(ArrayList<CerUnidadeCompetencialVO> unidadesAcreditadasCertificado, CerUnidadeCompetencialVO unidad){
        if(log.isDebugEnabled()) log.debug("añadirUnidadCompetencial() : BEGIN");
        Boolean añadir = false;
        for(CerUnidadeCompetencialVO unidadCertificado : unidadesAcreditadasCertificado){
            log.error("unidadCertificado.getCodUnidad(): " + unidadCertificado.getCodUnidad());
            log.error("unidad.getCodUnidad(): " + unidad.getCodUnidad());
            if(unidadCertificado.getCodUnidad().equalsIgnoreCase(unidad.getCodUnidad())){
                añadir = true;
                break;
            }//if(unidad.getCodUnidad().equalsIgnoreCase(unidad.getCodUnidad()))
        }//for(CerUnidadeCompetencialVO unidad : unidadesAcreditadasCertificado)
        if(log.isDebugEnabled()) log.debug("añadir = " + añadir);
        if(log.isDebugEnabled()) log.debug("añadirUnidadCompetencial() : END");
        return añadir;
    }//añadirUnidadCompetencial
    
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
    
    /**
     * Inserta un modulo de practicas para un expediente
     * 
     * @param modulo
     * @param adaptador
     * @throws MELANBIDE03Exception 
     *
    public void insertarModuloPracticas (CerModuloFormativoVO modulo, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("insertarModuloPracticas() : BEGIN");
        Connection con = null;
        try{
            con = adaptador.getConnection();
            con.setAutoCommit(false);
            MeLanbide03ModPractDAO meLanbide03ModPractDao =  MeLanbide03ModPractDAO.getInstance();
            meLanbide03ModPractDao.insertarModuloPracticas(modulo, con);
            con.commit();
        }catch(SQLException ex){
            try{
                con.rollback();
            }catch(SQLException f){
                log.error("Error al realizar un rollback al insertar/actualizar el módulo formativo: " + f.getMessage());
            }
            log.error("Se ha producido una excepción insertando los datos del módulo formativo",ex);            
            throw new MELANBIDE03Exception("Se ha producido una excepción insertando los datos del modulo formativo",ex);
        }catch(Exception e){            
            log.error("Se ha producido una excepción en la BBDD insertando los datos del modulo formativo",e);            
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando los datos del módulo formativo",e);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("insertarModuloPracticas() : END"); 
    }//insertarModuloPracticas    
    **/
    
    
   public void insertarModuloPracticas (CerModuloFormativoVO modulo, Connection con) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("insertarModuloPracticas() : BEGIN");   
        try{
   
            MeLanbide03ModPractDAO meLanbide03ModPractDao =  MeLanbide03ModPractDAO.getInstance();
            meLanbide03ModPractDao.insertarModuloPracticas(modulo, con);
            
        }catch(SQLException ex){            
            log.error("Se ha producido una excepción insertando los datos del módulo formativo",ex);            
            throw new MELANBIDE03Exception("Se ha producido una excepción insertando los datos del modulo formativo",ex);
        }catch(Exception e){            
            log.error("Se ha producido una excepción en la BBDD insertando los datos del modulo formativo",e);            
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando los datos del módulo formativo",e);
        }
        if(log.isDebugEnabled()) log.debug("insertarModuloPracticas() : END"); 
    }//insertarModuloPracticas
    
    /**
     * Elimina el modulo de practicas de un expediente
     * 
     * @param modulo
     * @param adaptador
     * @throws MELANBIDE03Exception 
     */
    public void eliminarModuloPracticas (CerModuloFormativoVO modulo, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("eliminarModuloPracticas() : BEGIN");
        Connection con = null;
        try{
            con = adaptador.getConnection();
            con.setAutoCommit(false);
            MeLanbide03ModPractDAO meLanbide03ModPractDao =  MeLanbide03ModPractDAO.getInstance();
            meLanbide03ModPractDao.borrarModuloPracticas(modulo, con);
            con.commit();
        }catch(SQLException ex){
            try{
                con.rollback();
            }catch(SQLException f){
                log.error("Error al realizar un rollback al eliminar el módulo formativo: " + f.getMessage());
            }
            log.error("Se ha producido una excepción eliminando los datos del módulo formativo",ex);            
            throw new MELANBIDE03Exception("Se ha producido una excepción insertando los datos del modulo formativo",ex);
        }catch(Exception e){            
            log.error("Se ha producido una excepción en la BBDD eliminando los datos del modulo formativo",e);            
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando los datos del módulo formativo",e);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("eliminarModuloPracticas() : END"); 
    }//insertarModuloPracticas
    
    /**
     * Recupera un modulo de practicas para un expediente
     * 
     * @param numExpediente
     * @param codOrganizacion
     * @param adaptador
     * @return CerModuloFormativoVO
     * @throws MELANBIDE03Exception 
     */
    public CerModuloFormativoVO getModuloPracticas (String numExpediente, Integer codOrganizacion, AdaptadorSQLBD adaptador) throws Exception{
        Connection con = null;
        try{
            con = adaptador.getConnection();
            return getModuloPracticas(numExpediente, codOrganizacion, con);
        }catch(BDException e){
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando los modulos formativos",e);
        }catch(Exception ex){
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los modulos formativos",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
    }
    
    /**
     * Recupera un modulo de practicas para un expediente
     * 
     * @param numExpediente
     * @param codOrganizacion
     * @return CerModuloFormativoVO
     * @throws MELANBIDE03Exception 
     */
    public CerModuloFormativoVO getModuloPracticas (String numExpediente, Integer codOrganizacion, Connection con) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("getModuloPracticas() : BEGIN"); 
        CerModuloFormativoVO modulo = new CerModuloFormativoVO();
        try{
            MeLanbide03ModPractDAO meLanbide03ModPractDao =  MeLanbide03ModPractDAO.getInstance();
            modulo = meLanbide03ModPractDao.getModuloFormativo(numExpediente, codOrganizacion, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los modulos formativos", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando los modulos formativos",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los modulos formativos",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los modulos formativos",ex);
        }//try-catch
        if(log.isDebugEnabled()) log.debug("getModuloPracticas() : END"); 
        return modulo;
    }//getModuloPracticas
    
    public boolean obetenerDatosExpedientesEMPNL (int codOrganizacion, String numExpediente, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception
    {
        boolean hay = false;
        try
        {
            Connection con = null;
            //Creamos la conexion
            con = adaptador.getConnection();
            //Instanciamos el DAO de datos suplementarios del tercero
            MeLanbide03DatosSuplementariosTercerosDao datosSuplementariosTerceroDao = MeLanbide03DatosSuplementariosTercerosDao.getInstance();
            
            hay = datosSuplementariosTerceroDao.obetenerDatosExpedientesEMPNL(codOrganizacion, numExpediente, con);
            
            if(con!=null) con.close();
            
        }catch(Exception ex){
            throw new MELANBIDE03Exception("Error al obtener datos de expedientes EMPNL",ex);
        }   
        return hay;
    }
    
    public ArrayList<Melanbide03ExpedientesVO> obtenerListaExpedientes(int codOrganizacion, String numExpediente, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        ArrayList<Melanbide03ExpedientesVO> exp = new ArrayList<Melanbide03ExpedientesVO>();
        try            
        {
            Connection con = null;
            //Creamos la conexion
            con = adaptador.getConnection();
            //Instanciamos el DAO de datos
            MeLanbide03DatosSuplementariosTercerosDao datosSuplementariosTerceroDao = MeLanbide03DatosSuplementariosTercerosDao.getInstance();
            
            exp = datosSuplementariosTerceroDao.datosExpedientesEMPNL(codOrganizacion, numExpediente, con);
         }catch(Exception ex){
            throw new MELANBIDE03Exception("Error al obtener datos de expedientes EMPNL",ex);
        }   
        return exp;
    
    }
    
    /**
     * Recupera la lista de interesados para un report
     * 
     * @param numExpediente
     * @param codOrganizacion
     * @param adaptador
     * @return ArrayList<MeLanbide03InteresadoGenerateReportVO>
     * @throws MELANBIDE03Exception 
     */
    public ArrayList<MeLanbide03InteresadoGenerateReportVO> getInteresadosReport (String numExpediente, Integer codOrganizacion, 
            AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("getInteresadosReport() : BEGIN");
        Connection con = null;
        ArrayList<MeLanbide03InteresadoGenerateReportVO> listaInteresados = new ArrayList<MeLanbide03InteresadoGenerateReportVO>();
        try{
            //Creamos la conexion
            con = adaptador.getConnection();
            //Instanciamos la factoria para obtener los terceros del expediente.
            IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
            //Instanciamos el DAO de datos suplementarios del tercero
            MeLanbide03DatosSuplementariosTercerosDao datosSuplementariosTerceroDao = MeLanbide03DatosSuplementariosTercerosDao.getInstance();
            //Clase para la conversion de fechas
            MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
            
            //Recuperamos los terceros del expediente
            String[] datosExpediente = numExpediente.split("/");
                String ejercicio = datosExpediente[0];
                String codProcedimiento = datosExpediente[1];
            SalidaIntegracionVO salida = el.getExpediente(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicio);
            ExpedienteModuloIntegracionVO expedienteAcreditado = salida.getExpediente();
            ArrayList<InteresadoExpedienteModuloIntegracionVO> interesados = expedienteAcreditado.getInteresados();
            
            //Recorremos los interesados
            for(InteresadoExpedienteModuloIntegracionVO interesado : interesados){
                MeLanbide03InteresadoGenerateReportVO interesadoReport = new MeLanbide03InteresadoGenerateReportVO();
                Integer codTercero = interesado.getCodigoTercero();
                Date fechaNacimiento = datosSuplementariosTerceroDao.getFechaNacimientoTercero(codTercero, con);
                String codOficina = MeLanbide03CertCentroDAO.getInstance().getCodOficina(codTercero, numExpediente, con);
                //Leire, 25/10/2014
                String nombre = interesado.getNombreCompleto();
                String nombreCom = nombre;
                if(nombre != null && nombre.contains(",")){
                    String [] nom = nombre.split(",");
                    nombreCom = nom[1] + " " + nom[0];
                }
                interesadoReport.setNombre(nombreCom);
                
                interesadoReport.setCodOficina(codOficina);
                interesadoReport.setFechaNacimiento(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, fechaNacimiento));
                interesadoReport.setFechaNacimientoAlt(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, fechaNacimiento));
                
                listaInteresados.add(interesadoReport);
            }//for(InteresadoExpedienteModuloIntegracionVO interesado : interesados)
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los interesados para el report", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando los interesados para el report",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los interesados para el report",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los interesados para el report",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getInteresadosReport() : END");
        return listaInteresados;
    }//getInteresadosReport
    
    /**
     * Recupera los datos necesarios para rellenar las unidades competenciales acreditadas para un report
     * 
     * @param numExpediente
     * @param codOrganizacion
     * @param adaptador
     * @return ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO>
     * @throws MELANBIDE03Exception 
     */
    public ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO> getUnidadesCompetencialesReport (String numExpediente, Integer codOrganizacion, 
            AdaptadorSQLBD adaptador)throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("getInteresadosReport() : BEGIN");
        Connection con = null;
        ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO> listaUnidadesCompetenciales = new ArrayList<MeLanbide03UnidadCompetencialGenerateReportVO>();
        MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
        try{
            //Creamos la conexion
            con = adaptador.getConnection();
            if(log.isDebugEnabled()) log.debug("Recuperamos el certificado seleccionado para el expediente");
            String codCertificado = MeLanbide03CertificadoDAO.getInstance().getCertificadoExpediente(numExpediente, codOrganizacion, con);
            if(codCertificado != null && !"".equalsIgnoreCase(codCertificado)){
                if(log.isDebugEnabled()) log.debug("Existe un certificado para el expediente");
                CerCertificadoVO certificado = CerCertificadosDAO.getInstance().getCertificado(codCertificado, con);
                if(certificado != null){
                    if(log.isDebugEnabled()) log.debug("Se ha recuperado correctamente el certificado del expediente");
                    if(log.isDebugEnabled()) log.debug("Recuperamos las Unidades competenciales acreditadas para el expediente");
                    ArrayList<CerUnidadeCompetencialVO> listaUnidadesAcreditadasExpediente = new ArrayList<CerUnidadeCompetencialVO>();
                    listaUnidadesAcreditadasExpediente = MeLanbide03CertCentroDAO.getInstance().
                        getUnidadesAcreditadasExpediente(String.valueOf(codOrganizacion), numExpediente, con);
                    log.debug("------------------------------------------------------------------------------------------");    
                    log.debug("listaUnidadesAcreditadasExpediente: "+listaUnidadesAcreditadasExpediente.size());
                    log.debug("------------------------------------------------------------------------------------------");    
                    for(CerUnidadeCompetencialVO unidadCompetencialExpediente : listaUnidadesAcreditadasExpediente){
                        MeLanbide03UnidadCompetencialGenerateReportVO unidadReport = new MeLanbide03UnidadCompetencialGenerateReportVO();
                        CerUnidadesCompetenciaDAO.getInstance().getUnidadCompetencia(unidadCompetencialExpediente, con);
                        
                        if(log.isDebugEnabled()) log.debug("Rellenamos el objeto MeLanbide03UnidadCompetencialGenerateReportVO");
                        unidadReport.setCodigoCP(certificado.getCodCertificado());
                        unidadReport.setDescripcionCP_C(certificado.getDescCertificadoC());
                        unidadReport.setDescripcionCP_E(certificado.getDescCertificadoE());
                        unidadReport.setFechaRD_C(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, certificado.getFechaRD()));
                        unidadReport.setFechaRD_E(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, certificado.getFechaRD()));
                        unidadReport.setNumeroRD(certificado.getDecreto());
                        
                        unidadReport.setModificadoRD_C(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, certificado.getFechaRDModif()));
                        unidadReport.setModificadoRD_E(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, certificado.getFechaRDModif()));
                        //unidadReport.setModificadoRD_C(certificado.getDecretoMod());
                        //unidadReport.setModificadoRD_E(certificado.getDecretoMod());
                        
                        unidadReport.setNivel(certificado.getNivel());
                        unidadReport.setDecretoMod(certificado.getDecretoMod());
                        
                        unidadReport.setCodigoUC(unidadCompetencialExpediente.getCodUnidad());
                        unidadReport.setDenominacion_C(unidadCompetencialExpediente.getDescUnidadC());
                        unidadReport.setDenominacion_E(unidadCompetencialExpediente.getDescUnidadE());
                        
                        Date fecha = new Date();
                        //unidadReport.setFechaCreacion(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, fecha));
                        //Leire 23/11/2014
                        if(unidadCompetencialExpediente.getFechaCreacion() != null)
                        {
                            SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
                            fecha = sdf.parse(unidadCompetencialExpediente.getFechaCreacion());
                        }
                        unidadReport.setFechaCreacion(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, fecha));
                        unidadReport.setFechaCreacionAlt(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, fecha));
                        unidadReport.setClaveRegistral(unidadCompetencialExpediente.getClaveRegistral());
                        unidadReport.setCodOficina(unidadCompetencialExpediente.getCodOficina());
                        listaUnidadesCompetenciales.add(unidadReport);
                    }//for(CerUnidadeCompetencialVO unidadCompetencialExpediente : listaUnidadesExpediente)
                    
                    //Tenemos que añadir tambien las unidades preacreditadas para el tercero del expediente.
//                    if(log.isDebugEnabled()) log.debug("Recuperamos las Unidades competenciales acreditadas el tercero en otros expedientes"
//                            + "con el mismo certificado");
//                    ArrayList<CerUnidadeCompetencialVO> listaUnidadesAcreditadasTercero = new ArrayList<CerUnidadeCompetencialVO>();
//                    ArrayList<CerUnidadeCompetencialVO> listaUnidadesExpediente = new ArrayList<CerUnidadeCompetencialVO>();
//                        listaUnidadesExpediente = getListaUnidadesCompetenciales(codCertificado, adaptador);
//                        listaUnidadesAcreditadasTercero = getUnidadesYaAcreditadasInteresado(listaUnidadesExpediente, numExpediente, codCertificado, 
//                                String.valueOf(codOrganizacion), adaptador);
//                    log.debug("------------------------------------------------------------------------------------------");    
//                    log.debug("listaUnidadesAcreditadasTercero: "+listaUnidadesAcreditadasTercero.size());
//                    log.debug("------------------------------------------------------------------------------------------"); 
//                    if(listaUnidadesAcreditadasExpediente.size() > 0){
//                        for(CerUnidadeCompetencialVO unidadCompetencialAcreditadaExpediente : listaUnidadesAcreditadasTercero){
//                            //Filtramos las que ya estan acreditadas por estar aceptadas en el propio expediente
//                            for(CerUnidadeCompetencialVO unidadCompetencialExpediente : listaUnidadesAcreditadasExpediente){
//                                if(!unidadCompetencialExpediente.getCodUnidad().equalsIgnoreCase(unidadCompetencialAcreditadaExpediente.getCodUnidad())){
//                                    MeLanbide03UnidadCompetencialGenerateReportVO unidadReport = new MeLanbide03UnidadCompetencialGenerateReportVO();
//                                    CerUnidadesCompetenciaDAO.getInstance().getUnidadCompetencia(unidadCompetencialAcreditadaExpediente, con);
//
//                                    if(log.isDebugEnabled()) log.debug("Rellenamos el objeto MeLanbide03UnidadCompetencialGenerateReportVO");
//                                    unidadReport.setCodigoCP(certificado.getCodCertificado());
//                                    unidadReport.setDescripcionCP_C(certificado.getDescCertificadoC());
//                                    unidadReport.setDescripcionCP_E(certificado.getDescCertificadoE());
//                                    unidadReport.setFechaRD_C(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, certificado.getFechaRD()));
//                                    unidadReport.setFechaRD_E(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, certificado.getFechaRD()));
//                                    unidadReport.setNumeroRD(certificado.getDecreto());
//                                    
//                                    unidadReport.setModificadoRD_C(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, certificado.getFechaRDModif()));
//                                    unidadReport.setModificadoRD_E(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, certificado.getFechaRDModif()));
//                                    //unidadReport.setModificadoRD_C(certificado.getDecretoMod());
//                                    //unidadReport.setModificadoRD_E(certificado.getDecretoMod());
//                                    
//                                    unidadReport.setNivel(certificado.getNivel());
//                                    unidadReport.setDecretoMod(certificado.getDecretoMod());
//
//                                    unidadReport.setCodigoUC(unidadCompetencialAcreditadaExpediente.getCodUnidad());
//                                    unidadReport.setDenominacion_C(unidadCompetencialAcreditadaExpediente.getDescUnidadC());
//                                    unidadReport.setDenominacion_E(unidadCompetencialAcreditadaExpediente.getDescUnidadE());
//
//                                    Date fecha = new Date();
//                                    unidadReport.setFechaCreacion(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, fecha));
//                                    unidadReport.setFechaCreacionAlt(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, fecha));
//                                    if(unidadReport.getClaveRegistral() != null && !unidadReport.getClaveRegistral().equalsIgnoreCase("")){
//                                        unidadReport.setClaveRegistral(unidadCompetencialAcreditadaExpediente.getClaveRegistral());
//                                    }else{
//                                        unidadReport.setClaveRegistral("");
//                                    }//if(unidadReport.getClaveRegistral() != null && !unidadReport.getClaveRegistral().equalsIgnoreCase(""))
//                                    unidadReport.setCodOficina(unidadCompetencialExpediente.getCodOficina());
//                                    listaUnidadesCompetenciales.add(unidadReport);
//                                }//if(!unidadCompetencialExpediente.getCodUnidad().equalsIgnoreCase(unidadCompetencialAcreditadaExpediente.getCodUnidad()))
//                            }//for(CerUnidadeCompetencialVO unidadCompetencialExpediente : listaUnidadesAcreditadasExpediente)
//                        }//for(CerUnidadeCompetencialVO unidadCompetencialExpediente : listaUnidadesAcreditadasTercero)
//                    }else{
//                        for(CerUnidadeCompetencialVO unidadCompetencialAcreditadaExpediente : listaUnidadesAcreditadasTercero){
//                            MeLanbide03UnidadCompetencialGenerateReportVO unidadReport = new MeLanbide03UnidadCompetencialGenerateReportVO();
//                            CerUnidadesCompetenciaDAO.getInstance().getUnidadCompetencia(unidadCompetencialAcreditadaExpediente, con);
//                            if(log.isDebugEnabled()) log.debug("Rellenamos el objeto MeLanbide03UnidadCompetencialGenerateReportVO");
//                            unidadReport.setCodigoCP(certificado.getCodCertificado());
//                            unidadReport.setDescripcionCP_C(certificado.getDescCertificadoC());
//                            unidadReport.setDescripcionCP_E(certificado.getDescCertificadoE());
//                            unidadReport.setFechaRD_C(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, certificado.getFechaRD()));
//                            unidadReport.setFechaRD_E(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, certificado.getFechaRD()));
//                            unidadReport.setNumeroRD(certificado.getDecreto());
//                            
//                            unidadReport.setModificadoRD_C(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, certificado.getFechaRDModif()));
//                            unidadReport.setModificadoRD_E(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, certificado.getFechaRDModif()));
//                            //unidadReport.setModificadoRD_C(certificado.getDecretoMod());
//                            //unidadReport.setModificadoRD_E(certificado.getDecretoMod());
//
//                            unidadReport.setNivel(certificado.getNivel());
//                            unidadReport.setDecretoMod(certificado.getDecretoMod());
//                            
//                            unidadReport.setCodigoUC(unidadCompetencialAcreditadaExpediente.getCodUnidad());
//                            unidadReport.setDenominacion_C(unidadCompetencialAcreditadaExpediente.getDescUnidadC());
//                            unidadReport.setDenominacion_E(unidadCompetencialAcreditadaExpediente.getDescUnidadE());
//
//                            Date fecha = new Date();
//                            unidadReport.setFechaCreacion(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.LATIN_DATE_FORMAT, fecha));
//                            unidadReport.setFechaCreacionAlt(meLanbide03I18n.getFechaConvertida(MeLanbide03I18n.DESC_DATE_FORMAT_SLASH, fecha));
//                            if(unidadCompetencialAcreditadaExpediente.getClaveRegistral() != null && 
//                                    !unidadCompetencialAcreditadaExpediente.getClaveRegistral().equalsIgnoreCase("")){
//                                unidadReport.setClaveRegistral(unidadCompetencialAcreditadaExpediente.getClaveRegistral());
//                            }else{
//                                unidadReport.setClaveRegistral("");
//                            }
//                            unidadReport.setCodOficina(unidadCompetencialAcreditadaExpediente.getCodOficina());
//                            /*if(unidadCompetencialAcreditadaExpediente.getClaveRegistral() != null && 
//                                    !unidadCompetencialAcreditadaExpediente.getClaveRegistral().equalsIgnoreCase(""))*/
//                            listaUnidadesCompetenciales.add(unidadReport);
//                        }//for(CerUnidadeCompetencialVO unidadCompetencialAcreditadaExpediente : listaUnidadesAcreditadasTercero)
//                    }//if(listaUnidadesAcreditadasExpediente.size() > 0)
                }//if(certificado != null)
            }//if(codCertificado != null && !"".equalsIgnoreCase(codCertificado))
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los interesados para el report", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando los interesados para el report",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los interesados para el report",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los interesados para el report",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getInteresadosReport() : END");
        return listaUnidadesCompetenciales;
    }//getUnidadesCompetencialesReport
    
    public int getAnoFechaPresentacion(String numExpediente, AdaptadorSQLBD adapt) throws MELANBIDE03Exception
    {
        Connection con = null;
        int result = 0;
        try
        {
            con = adapt.getConnection();
            result = MeLanbide03ReportDAO.getInstance().getAnoFechaPresentacion(numExpediente, con);
        }
        catch(Exception e)
        {
            log.error("Error : ", e);
            throw new MELANBIDE03Exception(e.getMessage(), e);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);
            }
            catch(BDException e)
            {
                log.error("Error : ", e);
            }
        }
        return result;
    }
    
    /**
     * Inserta un report en la BBDD
     * 
     * @param reportVO
     * @param adaptador
     * @throws MELANBIDE03Exception 
     */
    public void insertarReport (MeLanbide03ReportVO reportVO, AdaptadorSQLBD adaptador)throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("insertarReport() : BEGIN");
        Connection con = null;
        try{
            con = adaptador.getConnection();
            con.setAutoCommit(false);
            MeLanbide03ReportDAO reportDao = MeLanbide03ReportDAO.getInstance();
            reportDao.insertReport(reportVO, con);
            con.commit();
        }catch(BDException e){
            try{
                con.rollback();
            }catch(SQLException f){
                log.error("Error al realizar un rollback al insertar el report: " + f.getMessage());
            }//try-catch
            log.error("Se ha producido una excepción en la BBDD insertando el report", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando el report",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción nsertando el report",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción nsertando el report",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("insertarReport() : END");
    }//insertarReport
    
    /**
     * Recupera la lista de reports asociados a un expediente (No incluye el contenido en bytes)
     * 
     * @param numExpediente
     * @param codIdioma
     * @param codOrganizacion
     * @param adaptador
     * @return ArrayList<MeLanbide03ReportVO>
     * @throws MELANBIDE03Exception 
     */
    public ArrayList<MeLanbide03ReportVO> getReports (String numExpediente, Integer codIdioma, Integer codOrganizacion, 
            AdaptadorSQLBD adaptador)throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("getReports() : BEGIN");
        Connection con = null;
        ArrayList<MeLanbide03ReportVO> listaReports = new ArrayList<MeLanbide03ReportVO>();
        try{
            con = adaptador.getConnection();
            MeLanbide03ReportDAO reportDao = MeLanbide03ReportDAO.getInstance();
            listaReports = reportDao.getReports(numExpediente, codIdioma, codOrganizacion, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los reports", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDDrecuperando los reports",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los recuperando los reports",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los recuperando los reports",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getReports() : END");
        return listaReports;
    }//getReports

    public ArrayList<MeLanbide03ReportVO> getReportsNoDokusi(Integer codOrganizacion, AdaptadorSQLBD adaptador)
            throws MELANBIDE03Exception{
        if (log.isDebugEnabled()) log.debug("getReportsNoDokusi() : BEGIN");
        Connection con = null;
        ArrayList<MeLanbide03ReportVO> listaReports = new ArrayList();
        try
        {
            con = adaptador.getConnection();
            MeLanbide03ReportDAO reportDao = MeLanbide03ReportDAO.getInstance();
            listaReports = reportDao.getReportsNoDokusi(codOrganizacion, con);
        }
        catch (BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los reports", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDDrecuperando los reports", e);
        }
        catch (Exception ex)
        {
            log.error("Se ha producido una excepción recuperando los recuperando los reports", ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los recuperando los reports", ex);
        }
        finally {
            try {
                adaptador.devolverConexion(con);
            }catch (Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("getReportsNoDokusi() : END");
        return listaReports;
    }

    public ArrayList<MeLanbide03ReportVO> getReportsNoLocalizados(Integer codOrganizacion, AdaptadorSQLBD adaptador)
            throws MELANBIDE03Exception
    {
        if (log.isDebugEnabled()) log.debug("getReportsNoLocalizados() : BEGIN");
        Connection con = null;
        ArrayList<MeLanbide03ReportVO> listaReports = new ArrayList();
        try
        {
            con = adaptador.getConnection();
            MeLanbide03ReportDAO reportDao = MeLanbide03ReportDAO.getInstance();
            listaReports = reportDao.getReportsNoLocalizados(codOrganizacion, con);
        }
        catch (BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los reports", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDDrecuperando los reports", e);
        }
        catch (Exception ex)
        {
            log.error("Se ha producido una excepción recuperando los recuperando los reports", ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los recuperando los reports", ex);
        }
        finally
        {
            try {
                adaptador.devolverConexion(con);
            }catch (Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("getReportsNoLocalizados() : END");
        return listaReports;
    }

    public void updateIdDokusiReport(MeLanbide03ReportVO report, AdaptadorSQLBD adaptador)
            throws MELANBIDE03Exception
    {
        if (log.isDebugEnabled()) log.debug("updateIdDokusiReport() : BEGIN");
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            con.setAutoCommit(false);

            MeLanbide03ReportDAO reportDao = MeLanbide03ReportDAO.getInstance();
            reportDao.updateIdDokusiReport(report, con);
            con.commit();
        }
        catch (SQLException ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException f)
            {
                log.error("Error al realizar un rollback al updateIdDokusiReport el módulo formativo: " + f.getMessage());
            }
            log.error("Se ha producido una excepción updateIdDokusiReport los datos del módulo formativo", ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción updateIdDokusiReport los datos del modulo formativo", ex);
        }
        catch (Exception e)
        {
            log.error("Se ha producido una excepción en la BBDD updateIdDokusiReport los datos del modulo formativo", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD updateIdDokusiReport los datos del módulo formativo", e);
        }
        finally
        {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e)
            {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("updateIdDokusiReport() : END");
    }

    public void updateLocalizadoReport(MeLanbide03ReportVO report, AdaptadorSQLBD adaptador)
            throws MELANBIDE03Exception
    {
        if (log.isDebugEnabled()) log.debug("updateLocalizadoReport() : BEGIN");
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            con.setAutoCommit(false);

            MeLanbide03ReportDAO reportDao = MeLanbide03ReportDAO.getInstance();
            reportDao.updateLocalizadoReport(report, con);
            con.commit();
        }
        catch (SQLException ex)
        {
            try
            {
                con.rollback();
            }
            catch (SQLException f)
            {
                log.error("Error al realizar un rollback al updateLocalizadoReport el módulo formativo: " + f.getMessage());
            }
            log.error("Se ha producido una excepción updateLocalizadoReport los datos del módulo formativo", ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción updateIdDokusiReport los datos del modulo formativo", ex);
        }
        catch (Exception e)
        {
            log.error("Se ha producido una excepción en la BBDD updateLocalizadoReport los datos del modulo formativo", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD updateLocalizadoReport los datos del módulo formativo", e);
        }
        finally
        {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e)
            {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("updateLocalizadoReport() : END");
    }
    
    /**
     * Recupera un report por su Nombre de archivo, expediente y codigo de la organizacion (Incluye el contenido en bytes)
     * 
     * @param nombreReport
     * @param numExpediente
     * @param codOrganizacion
     * @param adaptador
     * @return MeLanbide03ReportVO
     * @throws MELANBIDE03Exception 
     */
    public MeLanbide03ReportVO getReport (String nombreReport, String numExpediente, Integer codOrganizacion, AdaptadorSQLBD adaptador)
        throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("getReport() : BEGIN");
        Connection con = null;
        MeLanbide03ReportVO report = new MeLanbide03ReportVO();
        try{
            con = adaptador.getConnection();
            MeLanbide03ReportDAO reportDao = MeLanbide03ReportDAO.getInstance();
            report = reportDao.getReport(nombreReport, numExpediente, codOrganizacion, con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando el report", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando el report",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción en la BBDD recuperando el report",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los interesados para el report",ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getReport() : END");
        return report;
    }//getReports
    
    public ArrayList<ImpresionExpedientesLanbideValueObject> getListaExpedientesDocumento(int codOrganizacion, String nombreFichero, AdaptadorSQLBD adapt) {
          ArrayList<ImpresionExpedientesLanbideValueObject> salida = null;
          Connection con = null;
          
          try{
              con = adapt.getConnection();
           
              salida = MeLanbide03ReportDAO.getInstance().getListaExpedientesDocumento(codOrganizacion,nombreFichero, con);
              
          }catch(BDException e){
              log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
          } catch (SQLException e) {
              log.error("SQLException: " + e.getMessage());
        }finally{
              try{
                  adapt.devolverConexion(con);
              }catch(BDException e){
                  log.error("Error al cerrar una conexión a la BBDD: " + e.getMessage());
              }
          }          
          return salida;
      }
    
    public List<GeneralValueObject> getExpedientesApaPendientes(AdaptadorSQLBD adaptador) throws MELANBIDE03Exception
    {
        Connection con = null;
        List<GeneralValueObject> listaExpedientesPendientes = new ArrayList<GeneralValueObject>();
        try
        {
            con = adaptador.getConnection();
            listaExpedientesPendientes = MeLanbide03ReportDAO.getInstance().getExpedientesApaPendientes(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando lista expedientes pendientes APA", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando lista expedientes pendientes APA",e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando lista expedientes pendientes APA",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando lista expedientes pendientes APA",ex);
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
        return listaExpedientesPendientes;
    }
    
    public List<GeneralValueObject> getExpedientesOficiosPendientes(AdaptadorSQLBD adaptador) throws MELANBIDE03Exception
    {
        Connection con = null;
        List<GeneralValueObject> listaExpedientesPendientes = new ArrayList<GeneralValueObject>();
        try
        {
            con = adaptador.getConnection();
            listaExpedientesPendientes = MeLanbide03ReportDAO.getInstance().getExpedientesOficiosPendientes(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando lista expedientes con oficios pendientes APA", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando lista expedientes con oficios pendientes APA",e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando lista expedientes con oficios pendientes APA",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando lista expedientes con oficios pendientes APA",ex);
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
        return listaExpedientesPendientes;
    }
    
    

    public List<GeneralValueObject> getFicherosImpresionGenerados(AdaptadorSQLBD adapt) throws MELANBIDE03Exception
    {
        List<GeneralValueObject> lista = new ArrayList<GeneralValueObject>();
        Connection con = null;

        try
        {
            con = adapt.getConnection();
            lista = MeLanbide03ReportDAO.getInstance().getFicherosImpresionGenerados(con);
        }
        catch(Exception e)
        {
            log.error("Error : ", e);
            throw new MELANBIDE03Exception(e.getMessage(), e);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);
            }
            catch(BDException e)
            {
                log.error("Error : ", e);
            }
        }
        return lista;

    }
    
    public List<GeneralValueObject> getFicherosImpresionOficiosGenerados(AdaptadorSQLBD adapt) throws MELANBIDE03Exception
    {
        List<GeneralValueObject> lista = new ArrayList<GeneralValueObject>();
        Connection con = null;

        try
        {
            con = adapt.getConnection();
            lista = MeLanbide03ReportDAO.getInstance().getFicherosImpresionOficiosGenerados(con);
        }
        catch(Exception e)
        {
            log.error("Error : ", e);
            throw new MELANBIDE03Exception(e.getMessage(), e);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);
            }
            catch(BDException e)
            {
                log.error("Error : ", e);
            }
        }
        return lista;

    }
    
    public int guardarPdfApaPendientes(String nombreFichero, byte[] pdf, AdaptadorSQLBD adapt) throws MELANBIDE03Exception
    {
        Connection con = null;
        int result = 0;
        try
        {
            con = adapt.getConnection();
            result = MeLanbide03ReportDAO.getInstance().guardarPdfApaPendientes(nombreFichero, pdf, con);
        }
        catch(Exception e)
        {
            log.error("Error : ", e);
            throw new MELANBIDE03Exception(e.getMessage(), e);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);
            }
            catch(BDException e)
            {
                log.error("Error : ", e);
            }
        }
        return result;
    }
    
    public byte[] getPdfPorNombre(String nombreFichero, AdaptadorSQLBD adapt) throws MELANBIDE03Exception
    {
        Connection con = null;
        byte[] pdf = null;
        try
        {
            con = adapt.getConnection();
            pdf = MeLanbide03ReportDAO.getInstance().getPdfPorNombre(nombreFichero, con);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new MELANBIDE03Exception(ex.getMessage(), ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);
            }
            catch(BDException e)
            {
                log.error("Error : ", e);
            }
        }
        return pdf;
    }
    
    
    
    public String getCodigoInternoTramite(int codOrganizacion, String codProc, String codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide03ReportDAO meLanbide32DAO = MeLanbide03ReportDAO.getInstance();
            return meLanbide32DAO.getCodigoInternoTramite(codOrganizacion, codProc, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el código interno del trámite "+codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el código interno del trámite "+codTramite, ex);
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
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    
    public ArrayList<Melanbide03RelacionExpVO> getRelacionExp (AdaptadorSQLBD adaptador)throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("getRelacionExp() : BEGIN");
        Connection con = null;
        ArrayList<Melanbide03RelacionExpVO> listaReports = new ArrayList<Melanbide03RelacionExpVO>();
        try{
            con = adaptador.getConnection();
            MeLanbide03ReportDAO reportDao = MeLanbide03ReportDAO.getInstance();
            listaReports = reportDao.getDatosRelacion(con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando los reports", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDDrecuperando los reports",e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando los recuperando los reports",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los recuperando los reports",ex);
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
    
    public ArrayList<String> getExpediente(String nombre, AdaptadorSQLBD adapt) {
         Connection con = null;
         ArrayList<String> contenido = new ArrayList<String>();
         try{
             con = adapt.getConnection();
             
             contenido = MeLanbide03ReportDAO.getInstance().getExpediente(nombre, con);
             
         }catch(Exception e){
             log.error("Error al recuperar el contenido del documento de impresión en formato excel: " + e.getMessage());
         }finally{
             try{
                adapt.devolverConexion(con);
             }catch(Exception e){
                 log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
             }
         }
         
         return contenido;
     }
    
    public ArrayList<Participantes> getParticipantes(String expediente, AdaptadorSQLBD adapt) {
         Connection con = null;
         ArrayList<Participantes> contenido = new ArrayList<Participantes>();
         try{
             con = adapt.getConnection();
             
             contenido = MeLanbide03ReportDAO.getInstance().leerDatosParticipantes(expediente, con);
             
         }catch(Exception e){
             log.error("Error en getParticipantes " + e.getMessage());
         }finally{
             try{
                adapt.devolverConexion(con);
             }catch(Exception e){
                 log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
             }
         }
         
         return contenido;
     }
    
    public static String generarXML (String reportVO){
        //if(log.isDebugEnabled()) log.debug("generarXML() : BEGIN ");
        String xml = new String();
        StringBuilder certificados = new StringBuilder();
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        certificados.append("<nombre>Pepita</nombre>");
        
        xml += getDoctype();
        xml += getNodoPrincipal(certificados.toString());
        //if(log.isDebugEnabled()) log.debug("generarXML() : END ");
        return xml;
    }//generarXML
    
    private static String getDoctype(){
        String doctype = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
        return doctype;
    }//getDoctype
    
    private static String getNodoPrincipal(String valor){
        String nodo = "certificados";
        StringBuilder xml = new StringBuilder();
        if(valor!=null && !"".equals(valor) && !"null".equalsIgnoreCase(valor))
            xml.append("<").append(nodo).append(">").append(valor).append("</").append(nodo).append(">");
        else
            xml.append("<").append(nodo).append(">").append(" ").append("</").append(nodo).append(">");
        return xml.toString();
    }//getNodoPrincipal
    
    public static Subreport inicializarSubreport(String subreportName, String xmlReport, String xpathRootExpr, Map<String, Object> subreportParams) throws JRException, ParserConfigurationException, SAXException, IOException
        {
            Subreport subreport = null;
            try
            {
                //String urlSubreport = "/es/altia/flexia/integracion/moduloexterno/melanbide03/reports/etiquetas.jasper";
                String urlSubreport = "/es/altia/flexia/integracion/moduloexterno/melanbide03/reports/"+subreportName+".jasper";
                InputStream jasperReportAsStream = MeLanbide03Manager.class.getResourceAsStream(urlSubreport);
                JasperReport subreportJR = (JasperReport) JRLoader.loadObject(jasperReportAsStream);

                JRXmlDataSource ds = null;
                if(xmlReport != null)
                {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setValidating(false);
                    factory.setNamespaceAware(false);
                    factory.setIgnoringComments(true);
                    DocumentBuilder db = factory.newDocumentBuilder();
                    InputSource inStream = new InputSource(new StringReader(xmlReport));
                    //db.setErrorHandler(new MeLanbide03ReportErrorHandler());
                    Document doc = db.parse(inStream);
                    ds = new JRXmlDataSource(doc, xpathRootExpr);
                }
                subreport = new Subreport(subreportJR, ds, subreportParams);
            }
            catch(Exception ex)
            {
                
                //log.error("Error en inicializarSubreport: " + ex.toString());
            }
            return subreport;
        }
    
    public static byte[] runMasterReportWithSubreports(String masterReportName, Map<String, Object> reportParams, List<Subreport> subreportList, String reportFormat)
        {
            byte[] result = null;
            //String urlMasterReport = "/es/altia/flexia/integracion/moduloexterno/melanbide03/reports/etiquetas.jasper";
            String urlMasterReport = "/es/altia/flexia/integracion/moduloexterno/melanbide03/reports/"+masterReportName+".jasper";

            if(reportParams == null)
            {
                reportParams = new HashMap();
            }

            JasperPrint jasperPrint = null;
            try 
            {
                InputStream jasperReportAsStream = MeLanbide03Manager.class.getResourceAsStream(urlMasterReport);
                System.setProperty("java.awt.headless", "true"); 

                JasperReport masterReport = (JasperReport) JRLoader.loadObject(jasperReportAsStream);

                if(subreportList == null || subreportList.isEmpty())
                {
                    jasperPrint = JasperFillManager.fillReport(masterReport, reportParams, new JREmptyDataSource(1));
                }
                else
                {
                    jasperPrint = JasperFillManager.fillReport(masterReport, reportParams, new JRBeanCollectionDataSource(subreportList));
                }
            } 
            catch (Exception e) 
            {
                log.error("Error : ", e);        	
                jasperPrint = null;

            }

            if (jasperPrint!=null) 
            {
                final ByteArrayOutputStream outAux = new ByteArrayOutputStream();
                try
                {
                    JRAbstractExporter exporter = null;
                    
                    exporter = new JRPdfExporter();
                    log.info("Desactivamos fuentes personalizadas en el report - nueva version jasperrepor 6");
                    /*
                    FontKey keyArial = new FontKey("Arial", false, false);  
                    PdfFont fontArial = new PdfFont("Helvetica","Cp1252",false); 

                    FontKey keyArialBold = new FontKey("Arial", true, false);  
                    PdfFont fontArialBold = new PdfFont("Helvetica-Bold","Cp1252",false); 

                    FontKey keyDialog = new FontKey("Dialog", false, false);  
                    PdfFont fontDialog = new PdfFont("Helvetica","Cp1252",false);

                    FontKey keyDialogBold = new FontKey("Dialog", false, false);  
                    PdfFont fontDialogBold = new PdfFont("Helvetica-Bold","Cp1252",false);


                    Map fontMap = new HashMap();
                    fontMap.put(keyArial,fontArial);
                    fontMap.put(keyArialBold,fontArialBold);
                    fontMap.put(keyDialog,fontDialog);
                    fontMap.put(keyDialogBold,fontDialogBold);
                    
                    exporter.setParameter(JRExporterParameter.FONT_MAP,fontMap);                    
                    */
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, (outAux));
                    exporter.exportReport();
                    result = (outAux.toByteArray());
                } 
                catch(Exception e) 
                {
                     log.error("Error : ", e);
                } 
                finally 
                {
                    IOOperations.closeOutputStreamSilently(outAux);
                }
            }
            return result;
        }
    
    
    public static byte[] runMasterReportWithSubreportsSinXML(String masterReportName, Map<String, Object> reportParams, List<Subreport> subreportList, String reportFormat) throws JRException
        {
            byte[] result = null;
            //String urlMasterReport = "/es/altia/flexia/integracion/moduloexterno/melanbide03/reports/etiquetas.jasper";
            String urlMasterReport = "/es/altia/flexia/integracion/moduloexterno/melanbide03/reports/"+masterReportName+".jasper";

            if(reportParams == null)
            {
                reportParams = new HashMap();
            }
            
            InputStream jasperReportAsStream = MeLanbide03Manager.class.getResourceAsStream(urlMasterReport);
            JasperReport reporte = (JasperReport) JRLoader.loadObject(jasperReportAsStream);
            Map<String, String> parametros = new HashMap<String, String>();
            parametros.put("INTERESADO_SOLICITANTE", "Juan");
            parametros.put("INTERESADO_DomSOLICITANT", "Reporte Participantes");
            JasperPrint jasperPrint = null;
            jasperPrint = JasperFillManager.fillReport(reporte, reportParams, new JRBeanCollectionDataSource(subreportList));
            /*JasperPrint jasperPrint = null;
            try 
            {
                InputStream jasperReportAsStream = MeLanbide03Manager.class.getResourceAsStream(urlMasterReport);
                System.setProperty("java.awt.headless", "true"); 

                JasperReport masterReport = (JasperReport) JRLoader.loadObject(jasperReportAsStream);

                if(subreportList == null || subreportList.isEmpty())
                {
                    jasperPrint = JasperFillManager.fillReport(masterReport, reportParams, new JREmptyDataSource(1));
                }
                else
                {
                    jasperPrint = JasperFillManager.fillReport(masterReport, reportParams, new JRBeanCollectionDataSource(subreportList));
                }
            } 
            catch (Exception e) 
            {
                log.error("Error : ", e);        	
                jasperPrint = null;

            }*/

            if (jasperPrint!=null) 
            {
                final ByteArrayOutputStream outAux = new ByteArrayOutputStream();
                try
                {
                    JRAbstractExporter exporter = null;
                    
                    exporter = new JRPdfExporter();
                    log.info("Desactivamos fuentes personalizadas en el report - nueva version jasperrepor 6");
                    /*
                    FontKey keyArial = new FontKey("Arial", false, false);  
                    PdfFont fontArial = new PdfFont("Helvetica","Cp1252",false); 

                    FontKey keyArialBold = new FontKey("Arial", true, false);  
                    PdfFont fontArialBold = new PdfFont("Helvetica-Bold","Cp1252",false); 

                    FontKey keyDialog = new FontKey("Dialog", false, false);  
                    PdfFont fontDialog = new PdfFont("Helvetica","Cp1252",false);

                    FontKey keyDialogBold = new FontKey("Dialog", false, false);  
                    PdfFont fontDialogBold = new PdfFont("Helvetica-Bold","Cp1252",false);


                    Map fontMap = new HashMap();
                    fontMap.put(keyArial,fontArial);
                    fontMap.put(keyArialBold,fontArialBold);
                    fontMap.put(keyDialog,fontDialog);
                    fontMap.put(keyDialogBold,fontDialogBold);
                    
                    exporter.setParameter(JRExporterParameter.FONT_MAP,fontMap);    
                    */
                    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
                    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, (outAux));
                    exporter.exportReport();
                    result = (outAux.toByteArray());
                } 
                catch(Exception e) 
                {
                     log.error("Error : ", e);
                } 
                finally 
                {
                    IOOperations.closeOutputStreamSilently(outAux);
                }
            }
            return result;
        }

    public String getTipoAcreditacion(int codOrganizacion,String numExpediente,Connection con) throws Exception {
        try
        {
            String tipoAcreditacion = MeLanbide03CertificadoDAO.getInstance().getTipoAcreditacion(numExpediente, codOrganizacion,con);
            
            return tipoAcreditacion;
        }
        catch(Exception ex){
            log.error("Error al recuperar el tipo de acreditación en el expediente " + numExpediente + ": " + ex.getMessage());            
            throw new Exception(ex);
        }
    }   
    
    public String getValoracion(int codOrganizacion,String numExpediente,Connection con) throws Exception {
        try
        {
            String valoracion = MeLanbide03CertificadoDAO.getInstance().getValoracion(numExpediente, codOrganizacion,con);
            
            return valoracion;
        }
        catch(Exception ex){
            log.error("Error al recuperar la valoración en el expediente " + numExpediente + ": " + ex.getMessage());            
            throw new Exception(ex);
        }
    }   
    
    public String compruebaExpedientes(String numExpediente, String certificado, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception
    {
        Connection con = null;
        String hay = "";
        try
        {
            con = adaptador.getConnection();
            MeLanbide03DAO melanbide03dao = new MeLanbide03DAO();
            hay = melanbide03dao.compruebaExpedientes(numExpediente, certificado, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando lista expedientes pendientes APA", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando lista expedientes pendientes APA",e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando lista expedientes pendientes APA",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando lista expedientes pendientes APA",ex);
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
        return hay;
    }

    public ArrayList<String> getCodigoVisibleTramitesActivosExpt(int codOrganizacion, String numExpediente, AdaptadorSQLBD adapt) throws MELANBIDE03Exception {
        log.error("getCodigoVisibleTramitesActivosExpt - manager - BEGIN()");
        Connection con = null;
        ArrayList<String> listaReturn = new ArrayList<String>();
        try {
            con = adapt.getConnection();
            MeLanbide03DAO melanbide03dao = new MeLanbide03DAO();
            listaReturn = melanbide03dao.obtenerCodigoVisibleTramitesActivosExpt(numExpediente,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando lista cod visible de tramites activos expedientes - " + numExpediente, e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando lista cod visible de tramites activos expedientes -  " + numExpediente, e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción egeneral recuperando lista expedientes pendientes", ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción General recuperando lista expedientes pendientes APA", ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD",e);
            }
        }
        log.error("getCodigoVisibleTramitesActivosExpt - manager - BEGIN()");
        return listaReturn;
    }

    public Map<String, String> avanzarExpedienteToEsperaExpedicion(String numExpediente, int codOrganizacion, AdaptadorSQLBD adapt) throws MELANBIDE03Exception {
        log.error("avanzarExpedienteToEsperaExpedicion - manager - BEGIN()");
        Connection con = null;
        Map<String, String> listaReturn = new HashMap<String, String>();
        try {
            con = adapt.getConnection();
            MeLanbide03DAO melanbide03dao = new MeLanbide03DAO();
            listaReturn = melanbide03dao.avanzarExpedienteToEsperaExpedicion(numExpediente, codOrganizacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD al ejecutar el procedure que avanza el expediente a Espera de expedicion - " + numExpediente, e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD al ejecutar el procedure que avanza el expediente a Espera de expedicion -  " + numExpediente, e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al ejecutar el procedure que avanza el expediente a Espera de expedicion", ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción General al ejecutar el procedure que avanza el expediente a espera de expedicion", ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD", e);
            }
        }
        log.error("avanzarExpedienteToEsperaExpedicion - manager - END()");
        return listaReturn;
    }
    
     /**
     * Recupera el tipo de acreditación
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return CerCertificadoVO
     * @throws Exception 
     */
    public String getTipoAcreditacion (String numExp,int codOrganizacion,AdaptadorSQLBD adaptador) throws Exception{
        
        if(log.isDebugEnabled()) log.debug("getTipoAcreditacion() : BEGIN");
        String tipoAcreditacion = "";
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide03CertificadoDAO meLanbide03CertificadoDAO = MeLanbide03CertificadoDAO.getInstance();
            tipoAcreditacion = meLanbide03CertificadoDAO.getTipoAcreditacion(numExp,codOrganizacion,con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando el tipo de acreditacion", e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando el tipo de acreditacion",ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCertificados() : END");
        return tipoAcreditacion;
    }//getTipoAcreditacion
    
    /**
     * Recupera la valoracion
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return CerCertificadoVO
     * @throws Exception 
     */
    public String getValoracion (String numExp,int codOrganizacion,AdaptadorSQLBD adaptador) throws Exception{
        
        if(log.isDebugEnabled()) log.debug("getValoracion() : BEGIN");
        String tipoAcreditacion = "";
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide03CertificadoDAO meLanbide03CertificadoDAO = MeLanbide03CertificadoDAO.getInstance();
            tipoAcreditacion = meLanbide03CertificadoDAO.getValoracion(numExp,codOrganizacion,con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando la valoracion", e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando la valoracion",ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getValoracion() : END");
        return tipoAcreditacion;
    }//getTipoAcreditacion
    
    public ArrayList<CertificacionPositiva> getCertificaciones(String expediente, AdaptadorSQLBD adapt) {
         Connection con = null;
         ArrayList<CertificacionPositiva> contenido = new ArrayList<CertificacionPositiva>();
         try{
             con = adapt.getConnection();
             
             contenido = MeLanbide03ReportDAO.getInstance().leerDatosCertificaciones(expediente, con);
             
         }catch(Exception e){
             log.error("Error en getParticipantes " + e.getMessage());
         }finally{
             try{
                adapt.devolverConexion(con);
             }catch(Exception e){
                 log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
             }
         }
         
         return contenido;
     }
    
    
    /**
     * Devuelve la lista de acreditadas3
     * @param codCertificado
     * @param adaptador
     * @return ArrayList<String>
     * @throws Exception 
     */
    public ArrayList<ArrayList> getListaAcreditadas3(String numExp,AdaptadorSQLBD adaptador) throws BDException, Exception{
        if(log.isDebugEnabled()) log.debug("getListaAcreditadas3() : BEGIN");
        ArrayList<ArrayList> listaAcreditadas3 = new ArrayList<ArrayList>();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide03ReportDAO melanbide03ReportDAO = MeLanbide03ReportDAO.getInstance();
            listaAcreditadas3 = melanbide03ReportDAO.getListaAcreditadas3(numExp,con);
        }catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD recuperando la lista de acreditadas3", e);
            throw new Exception(e);
        }catch(Exception ex){
            log.error("Se ha producido una excepción recuperando la lista de acreditadas3",ex);
            throw new Exception(ex);
        }finally{
            try{
                adaptador.devolverConexion(con);       
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getListaAcreditadas3() : END");
        return listaAcreditadas3;
    }//getCertificados

    public String getFechaPresentacion(String numExpediente, AdaptadorSQLBD adapt) throws MELANBIDE03Exception {
         Connection con = null;
        String result ="";
        try
        {
            con = adapt.getConnection();
            result = MeLanbide03ReportDAO.getInstance().getFechaPresentacion(numExpediente, con);
        }
        catch(Exception e)
        {
            log.error("Error : ", e);
            throw new MELANBIDE03Exception(e.getMessage(), e);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);
            }
            catch(BDException e)
            {
                log.error("Error : ", e);
            }
        }
        return result;
    }

    //devuelve 'S' si el expediente tiene el campo FECHAPRESENTACION y es <= '01/02/23'
    public String esAntiguoAPA(String numExpediente, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception {
        Connection con = null;
        String esAntiguo = "";
        try {
            con = adaptador.getConnection();
            esAntiguo = MeLanbide03ReportDAO.getInstance().esAntiguoAPA(numExpediente, con);
        }
        catch(BDException e) {
            log.error("Se ha producido una excepción en la BBDD recuperando si es antiguo APA", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando si es antiguo APA",e);
        }
        catch(Exception ex) {
            log.error("Se ha producido una excepción en la BBDD recuperando si es antiguo APA",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando si es antiguo APA",ex);
        }
        finally {
            try {
                adaptador.devolverConexion(con);
            }
            catch(Exception e) {
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return esAntiguo;
    }


    public ArrayList<CertRd34VO> getCertificadosRD34(AdaptadorSQLBD adaptador)
            throws MELANBIDE03Exception {
        if (log.isDebugEnabled()) log.debug("getCertificadosRD34() : BEGIN");
        Connection con = null;
        ArrayList<CertRd34VO> listaCertificados = new ArrayList();
        try {
            con = adaptador.getConnection();
            MeLanbide03RD34DAO certificadotDao = MeLanbide03RD34DAO.getInstance();
            listaCertificados = certificadotDao.getCertificadosRD34(con);
        }
        catch (BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los certificados RD34", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD recuperando los certificados RD34", e);
        }
        catch (Exception ex)
        {
            log.error("Se ha producido una excepción recuperando los certificados RD34", ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción recuperando los certificados RD34", ex);
        }
        finally
        {
            try {
                adaptador.devolverConexion(con);
            }catch (Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("getCertificadosRD34() : END");
        return listaCertificados;
    }

    public String crearXML (CertRd34RCabeceraVO datosLlamada, ArrayList<CertRd34VO> certificadosRD34){
        if(log.isDebugEnabled()) log.debug("crearXML() : BEGIN");

        String idUsuario = ConfigurationParameter.getParameter(ConstantesMeLanbide03.ID_USUARIO_RD34,ConstantesMeLanbide03.FICHERO_PROPIEDADES);
        String pass = ConfigurationParameter.getParameter(ConstantesMeLanbide03.PASS_RD34,ConstantesMeLanbide03.FICHERO_PROPIEDADES);

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<servicio>");
        xmlSalida.append("<cabecera>");
        xmlSalida.append("<idEnvio>");xmlSalida.append(datosLlamada.getIdEnvio());xmlSalida.append("</idEnvio>");
        xmlSalida.append("<fechaEnvio>");xmlSalida.append(datosLlamada.getFechaEnvio());xmlSalida.append("</fechaEnvio>");
        xmlSalida.append("<autenticacion>");
        xmlSalida.append("<idUsuario>");xmlSalida.append(idUsuario);xmlSalida.append("</idUsuario>");
        xmlSalida.append("<password>");xmlSalida.append(pass);xmlSalida.append("</password>");
        xmlSalida.append("</autenticacion>");
        xmlSalida.append("<total-certificados>");xmlSalida.append(datosLlamada.getTotal_certificados());xmlSalida.append("</total-certificados>");
        xmlSalida.append("</cabecera>");
        xmlSalida.append("<lista-de-certificados>");
        for(CertRd34VO certificado : certificadosRD34){
            xmlSalida.append("<certificado>");
            xmlSalida.append("<nif>");xmlSalida.append(certificado.getNif());xmlSalida.append("</nif>");
            xmlSalida.append("<nombre>");xmlSalida.append(certificado.getNombre());xmlSalida.append("</nombre>");
            xmlSalida.append("<apellido1>");xmlSalida.append(certificado.getApellido1());xmlSalida.append("</apellido1>");
            xmlSalida.append("<apellido2>");if (certificado.getApellido2() != null) xmlSalida.append(certificado.getApellido2());xmlSalida.append("</apellido2>");
            xmlSalida.append("<fecha-nacimiento>");xmlSalida.append(certificado.getFec_nacimiento());xmlSalida.append("</fecha-nacimiento>");
            xmlSalida.append("<sexo>");xmlSalida.append(certificado.getSexo());xmlSalida.append("</sexo>");
            xmlSalida.append("<nacion>");xmlSalida.append(certificado.getNacion());xmlSalida.append("</nacion>");
            xmlSalida.append("<ccaa>");xmlSalida.append(certificado.getCcaa());xmlSalida.append("</ccaa>");
            xmlSalida.append("<provincia>");xmlSalida.append(certificado.getProvincia());xmlSalida.append("</provincia>");
            xmlSalida.append("<localidad>");xmlSalida.append(certificado.getLocalidad());xmlSalida.append("</localidad>");
            xmlSalida.append("<domicilio>");xmlSalida.append(certificado.getDomicilio());xmlSalida.append("</domicilio>");
            xmlSalida.append("<cp>");xmlSalida.append(certificado.getCp());xmlSalida.append("</cp>");
            xmlSalida.append("<telefono-fijo>");if (certificado.getTelefono_fijo() != null) xmlSalida.append(certificado.getTelefono_fijo());xmlSalida.append("</telefono-fijo>");
            xmlSalida.append("<movil>");if (certificado.getMovil() != null) xmlSalida.append(certificado.getMovil());xmlSalida.append("</movil>");
            xmlSalida.append("<mail>");if (certificado.getMail() != null) xmlSalida.append(certificado.getMail());xmlSalida.append("</mail>");
            xmlSalida.append("<provincia-registro>");xmlSalida.append(certificado.getProvincia_registro());xmlSalida.append("</provincia-registro>");
            xmlSalida.append("<anio-registro>");xmlSalida.append(certificado.getAnio_registro());xmlSalida.append("</anio-registro>");
            xmlSalida.append("<organismo>");xmlSalida.append(certificado.getOrganismo());xmlSalida.append("</organismo>");
            xmlSalida.append("<provincia-otorgamiento>");xmlSalida.append(certificado.getProvincia_otorgamiento());xmlSalida.append("</provincia-otorgamiento>");
            xmlSalida.append("<fecha-otorgamiento>");xmlSalida.append(certificado.getFecha_otorgamiento());xmlSalida.append("</fecha-otorgamiento>");
            xmlSalida.append("<modo-gestion>");xmlSalida.append(certificado.getModo_gestion());xmlSalida.append("</modo-gestion>");
            xmlSalida.append("<codigo-certificado>");xmlSalida.append(certificado.getCodigo_certificado());xmlSalida.append("</codigo-certificado>");
            xmlSalida.append("<convocatoria>");xmlSalida.append("</convocatoria>");
            xmlSalida.append("<fecha-registro>");xmlSalida.append(certificado.getFecha_registro());xmlSalida.append("</fecha-registro>");
            xmlSalida.append("<itinerario>");xmlSalida.append(certificado.getItinerario());xmlSalida.append("</itinerario>");
            xmlSalida.append("<vias>");
            xmlSalida.append("<via>");
            xmlSalida.append("<via-acceso>");xmlSalida.append(certificado.getVia_acceso());xmlSalida.append("</via-acceso>");
            xmlSalida.append("<fecha-acceso>");xmlSalida.append(certificado.getFecha_acceso());xmlSalida.append("</fecha-acceso>");
            xmlSalida.append("<nombre-empresa>");xmlSalida.append("</nombre-empresa>");
            xmlSalida.append("<codigo-ef>");xmlSalida.append("</codigo-ef>");
            xmlSalida.append("<codigo-af>");xmlSalida.append("</codigo-af>");
            xmlSalida.append("<fecha-inicio>");xmlSalida.append("</fecha-inicio>");
            xmlSalida.append("<fecha-final>");xmlSalida.append("</fecha-final>");
            xmlSalida.append("</via>");
            xmlSalida.append("</vias>");
            xmlSalida.append("<unidades-competencia>");
            xmlSalida.append("<unidad-competencia>");
            xmlSalida.append("<uc>");xmlSalida.append("</uc>");
            xmlSalida.append("<modalidad-imparticion>");xmlSalida.append("</modalidad-imparticion>");
            xmlSalida.append("</unidad-competencia>");
            xmlSalida.append("</unidades-competencia>");
            xmlSalida.append("<modulos-practicas>");
            xmlSalida.append("<modulo-practicas>");
            xmlSalida.append("<modulo>");xmlSalida.append("</modulo>");
            xmlSalida.append("<fecha-modulo>");if (certificado.getFecha_modulo() != null) xmlSalida.append(certificado.getFecha_modulo());xmlSalida.append("</fecha-modulo>");
            xmlSalida.append("</modulo-practicas>");
            xmlSalida.append("</modulos-practicas>");
            xmlSalida.append("</certificado>");
        }
        xmlSalida.append("</lista-de-certificados>");
        xmlSalida.append("</servicio>");

        if(log.isDebugEnabled()) log.debug("crearXML() : END");

        return xmlSalida.toString();
    }

    public String getRespuestaRD34 (String xmlRD34)  throws MELANBIDE03Exception {
        if(log.isDebugEnabled()) log.debug("getRespuestaRD34() : BEGIN");

        String endpoint = ConfigurationParameter.getParameter(ConstantesMeLanbide03.URL_RD34,ConstantesMeLanbide03.FICHERO_PROPIEDADES);

        String xmlRespuesta = "";

        try {

            String proxyHost = ConfigurationParameter.getParameter(ConstantesMeLanbide03.PROXY_HOST,ConstantesMeLanbide03.FICHERO_PROPIEDADES);
            int proxyPort = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide03.PROXY_PORT,ConstantesMeLanbide03.FICHERO_PROPIEDADES));
            HttpHost proxy = new HttpHost(proxyHost, proxyPort);

            RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();

            //CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
            HttpPost postRequest = new HttpPost(endpoint);

            // encabezado
            postRequest.setHeader("Content-Type", "application/xml; charset=ISO-8859-1");
            postRequest.setHeader("Accept", "application/xml");

            // XML
            StringEntity xmlEntity = new StringEntity(xmlRD34);
            postRequest.setEntity(xmlEntity);

            // ejecutar + respuesta
            log.info("antes de ejecutar llamada a ws carga de certificados RD34");
            HttpResponse response = client.execute(postRequest);
            log.info("ejecutando llamada a ws carga de certificados RD34...");
            log.info("getStatusLine() : " + response.getStatusLine());
            int code = response.getStatusLine().getStatusCode();
            log.info("getStatusLine().getStatusCode() " + code);
            if (code == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                log.info("Se recibe respuesta de ws carga de RD34");
                xmlRespuesta = EntityUtils.toString(entity);
                //log.info("Respuesta ws carga RD34: " + xmlRespuesta);
            } else {
                xmlRespuesta = "Error getRespuestaRD34 : code <> 200 " + code;
                //log.info("llamada ws carga RD34 rechazada, code = " + code);
            }
        } catch (IOException e) {
            xmlRespuesta = "Error IOException getRespuestaRD34 : error : " + e.toString();
            //log.error("Error IOException llamada WS RD34 : " + e.toString() + "   traza: ");
            e.printStackTrace();
        } catch (Exception ex) {
            xmlRespuesta = "Error Exception getRespuestaRD34 : error : " + ex.toString();
            //log.error("Error llamada WS RD34 : traza: ");
            ex.printStackTrace();
        }

        if(log.isDebugEnabled()) log.debug("getRespuestaRD34() : END");

        return xmlRespuesta;

    }

    public boolean isXML(String inXMLStr) {
        boolean retBool = false;
        Pattern pattern;
        Matcher matcher;
        final String XML_PATTERN_STR = "<(\\S+?)(.*?)>(.*?)</\\1>";
        if (inXMLStr != null && inXMLStr.trim().length() > 0) {
            if (inXMLStr.trim().startsWith("<")) {
                pattern = Pattern.compile(XML_PATTERN_STR,
                        Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);
                matcher = pattern.matcher(inXMLStr);
                retBool = matcher.matches();
            }
        }
        return retBool;
    }

    public CertRd34RCabeceraVO getRespuestaCabeceraRD34(ArrayList<CertRd34VO> certificadosRD34,String xmlRespuesta) throws MELANBIDE03Exception {
        if (log.isDebugEnabled()) log.debug("getRespuestaCabeceraRD34() : BEGIN");

        CertRd34RCabeceraVO certCabecera = new CertRd34RCabeceraVO();
        ArrayList<CertRd34RDetalleVO> certDetalle = new ArrayList<CertRd34RDetalleVO>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ( );
            Document documento = null;

            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xmlRespuesta));

            documento = builder.parse(is);

            NodeList idEnvio=documento.getElementsByTagName("idEnvio");
            NodeList fechaEnvio=documento.getElementsByTagName("fechaEnvio");
            NodeList fechaRespuesta=documento.getElementsByTagName("fechaRespuesta");
            NodeList total_certificados=documento.getElementsByTagName("total-certificados");
            NodeList total_grabados=documento.getElementsByTagName("total-grabados");
            NodeList total_errores=documento.getElementsByTagName("total-errores");

            certCabecera.setIdEnvio(Integer.parseInt(idEnvio.item(0).getTextContent()));
            certCabecera.setFechaEnvio(fechaEnvio.item(0).getTextContent());
            certCabecera.setFechaRespuesta(fechaRespuesta.item(0).getTextContent());
            certCabecera.setTotal_certificados(Integer.parseInt(total_certificados.item(0).getTextContent()));
            certCabecera.setTotal_grabados(Integer.parseInt(total_grabados.item(0).getTextContent()));
            certCabecera.setTotal_errores(Integer.parseInt(total_errores.item(0).getTextContent()));

            NodeList certificado=documento.getElementsByTagName("certificado");
            for (int i=0; i<certificado.getLength(); i++) {

                String numExpedienteLlamada = certificadosRD34.get(i).getNumExpediente();
                String nifLlamada = certificadosRD34.get(i).getNif();
                String codigoCertificadoLlamada = certificadosRD34.get(i).getCodigo_certificado();

                CertRd34RDetalleVO certDetalleUnit = new CertRd34RDetalleVO();
                Node certif = certificado.item(i);
                Element el = (Element)certif.getChildNodes();

                String nif = el.getElementsByTagName("nif").item(0).getTextContent();
                String resultado = el.getElementsByTagName("resultado").item(0).getTextContent();

                String numRegistro = "";
                if (el.getElementsByTagName("numRegistro").getLength() > 0) {
                    numRegistro = el.getElementsByTagName("numRegistro").item(0).getTextContent();
                }

                String ccaa = el.getElementsByTagName("ccaa").item(0).getTextContent();
                String fecha_registro = el.getElementsByTagName("fecha-registro").item(0).getTextContent();

                certDetalleUnit.setNif(nif);
                certDetalleUnit.setResultado(resultado);
                certDetalleUnit.setNumRegistro(numRegistro);
                certDetalleUnit.setCcaa(ccaa);
                certDetalleUnit.setFecha_registro(fecha_registro);

                NodeList unidad_competencia = el.getElementsByTagName("unidad-competencia");
                ArrayList<CertRd34RDetalleUCVO> certDetalleUC = new ArrayList<CertRd34RDetalleUCVO>();
                for (int j=0; j<unidad_competencia.getLength(); j++) {
                    CertRd34RDetalleUCVO certDetalleUCunit = new CertRd34RDetalleUCVO();
                    Node unidc = unidad_competencia.item(j);
                    Element elm = (Element)unidc.getChildNodes();

                    String uc = "";
                    if (el.getElementsByTagName("uc").getLength() > 0) {
                        uc = elm.getElementsByTagName("uc").item(0).getTextContent();
                    }

                    String modalidad_imparticion = "";
                    if (el.getElementsByTagName("modalidad-imparticion").getLength() > 0) {
                        modalidad_imparticion = elm.getElementsByTagName("modalidad-imparticion").item(0).getTextContent();
                    }

                    certDetalleUCunit.setUc(uc);
                    certDetalleUCunit.setModalidad_imparticion(modalidad_imparticion);
                    certDetalleUC.add(certDetalleUCunit);

                }

                certDetalleUnit.setUnidades_competencia(certDetalleUC);

                NodeList error = el.getElementsByTagName("error");
                ArrayList<CertRd34RDetalleErrorVO> certDetalleError = new ArrayList<CertRd34RDetalleErrorVO>();
                for (int j=0; j<error.getLength(); j++) {
                    CertRd34RDetalleErrorVO certDetalleErrorunit = new CertRd34RDetalleErrorVO();
                    Node unidce = error.item(j);
                    Element elm = (Element)unidce.getChildNodes();

                    String codigo = elm.getElementsByTagName("codigo").item(0).getTextContent();
                    String descripcion = elm.getElementsByTagName("descripcion").item(0).getTextContent();

                    certDetalleErrorunit.setCodigo(codigo);
                    certDetalleErrorunit.setDescripcion(descripcion);
                    certDetalleError.add(certDetalleErrorunit);

                }

                certDetalleUnit.setListado_errores(certDetalleError);

                certDetalle.add(certDetalleUnit);

                //se comprobará en el job si el expediente es correcto, que concuerda con nif + codigoCertificado de la respuesta
                certDetalleUnit.setNumExpediente(numExpedienteLlamada);

            }

            certCabecera.setLista_de_certificados(certDetalle);

        }
        catch (Exception ex) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            certCabecera.setError_ws(sw.toString());
            log.error("Se ha producido una excepción convirtiendo cabecera de respuesta RD34", ex);
            ex.printStackTrace();
            throw new MELANBIDE03Exception("Se ha producido una excepción convirtiendo cabecera de respuesta RD34", ex);
        }

        if(log.isDebugEnabled()) log.debug("getRespuestaCabeceraRD34() : END");
        return certCabecera;
    }

    public CertRd34RCabeceraVO checkNumExpedientes(ArrayList<CertRd34VO> listaCertificados, CertRd34RCabeceraVO respuestaCabeceraRD34, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception {
        if (log.isDebugEnabled()) log.debug("checkNumExpedientes() : BEGIN");
        Connection con = null;
        try {
            con = adaptador.getConnection();

            MeLanbide03RD34DAO certificadosDao = MeLanbide03RD34DAO.getInstance();

            for (int i=0; i<listaCertificados.size(); i++) {
                String numExpedienteLlamada = listaCertificados.get(i).getNumExpediente();
                String nifLlamada = listaCertificados.get(i).getNif();
                String codigoCertificadoLlamada = listaCertificados.get(i).getCodigo_certificado();

                String numExpedienteRespuesta = respuestaCabeceraRD34.getLista_de_certificados().get(i).getNumExpediente();
                String nifRespuesta = respuestaCabeceraRD34.getLista_de_certificados().get(i).getNif();
                String codigoCertificadoRespuesta = certificadosDao.checkNumExpediente(respuestaCabeceraRD34.getLista_de_certificados().get(i).getUnidades_competencia(),con);

                if (numExpedienteLlamada.equals(numExpedienteRespuesta) && nifLlamada.equals(nifRespuesta) && codigoCertificadoLlamada.equals(codigoCertificadoRespuesta)) {
                    //log.info ("Coinciden expediente, nif y código de certificado para : " + i);
                } else {
                    /*
                    log.info ("numExpedienteLlamada = " + numExpedienteLlamada);
                    log.info ("numExpedienteRespuesta = " + numExpedienteRespuesta);
                    log.info ("nifLlamada = " + nifLlamada);
                    log.info ("nifRespuesta = " + nifRespuesta);
                    log.info ("codigoCertificadoLlamada = " + codigoCertificadoLlamada);
                    log.info ("codigoCertificadoRespuesta = " + codigoCertificadoRespuesta);
                     */

                    for (int j=0; j<listaCertificados.size(); j++) {
                        if (listaCertificados.get(j).getNif().equals(nifRespuesta) && listaCertificados.get(j).getCodigo_certificado().equals(codigoCertificadoRespuesta)) {
                            respuestaCabeceraRD34.getLista_de_certificados().get(i).setNumExpediente(listaCertificados.get(j).getNumExpediente());
                        }
                    }
                }
            }

        }
        catch (BDException e) {
            log.error("Se ha producido una excepción en la BBDD chequeando los expedientes RD34", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD chequeando los expedientes RD34", e);
        }
        catch (Exception ex) {
            log.error("Se ha producido una excepción chequeando los expedientes RD34", ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción chequeando los expedientes RD34", ex);
        }
        finally {
            try {
                adaptador.devolverConexion(con);
            }catch (Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("checkNumExpedientes() : END");
        return respuestaCabeceraRD34;
    }

    public CertRd34RCabeceraVO insertarDatosLlamadaRD34 (int total_certificados, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("insertarDatosLlamadaRD34() : BEGIN");
        CertRd34RCabeceraVO datosLlamada;
        Connection con = null;

        try{
            con = adaptador.getConnection();
            con.setAutoCommit(false);

            MeLanbide03RD34DAO meLanbide03RD34Dao = MeLanbide03RD34DAO.getInstance();
            datosLlamada = meLanbide03RD34Dao.insertarDatosLlamadaRD34(total_certificados, con);

            con.commit();

        } catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD insertando datos de llamada RD34  : ", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando datos de llamada RD34  : ", e);
        } catch(SQLException ex){
            try {
                con.rollback();
            } catch(SQLException f){
                log.error("Error al realizar un rollback insertando datos de llamada RD34  : " + f.getMessage());
            }
            log.error("Se ha producido una excepción insertando datos de llamada RD34  : ",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción insertando datos de llamada RD34  : ",ex);
        } catch(Exception e){
            log.error("Se ha producido una excepción en la BBDD insertando datos de llamada RD34  : ", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando datos de llamada RD34  : ", e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("insertarDatosLlamadaRD34() : END");
        return datosLlamada;

    }

    public boolean updateDatosCabeceraRD34 (CertRd34RCabeceraVO datosLlamada, CertRd34RCabeceraVO datosRespuesta, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("updateDatosCabeceraRD34() : BEGIN");
        boolean exito;
        Connection con = null;

        try{
            con = adaptador.getConnection();
            con.setAutoCommit(false);

            MeLanbide03RD34DAO meLanbide03RD34Dao = MeLanbide03RD34DAO.getInstance();
            exito = meLanbide03RD34Dao.updateDatosCabeceraRD34(datosLlamada, datosRespuesta, con);

            con.commit();

        } catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD actualizando datos de llamada RD34  : ", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD actualizando datos de llamada RD34  : ", e);
        } catch(SQLException ex){
            try {
                con.rollback();
            } catch(SQLException f){
                log.error("Error al realizar un rollback actualizando datos de llamada RD34  : " + f.getMessage());
            }
            log.error("Se ha producido una excepción actualizando datos de llamada RD34  : ",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción actualizando datos de llamada RD34  : ",ex);
        } catch(Exception e){
            log.error("Se ha producido una excepción en la BBDD actualizando datos de llamada RD34  : ", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD actualizando datos de llamada RD34  : ", e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("updateDatosCabeceraRD34() : END");
        return exito;

    }

    public boolean insertarDatosDetalleRD34 (CertRd34RCabeceraVO datosLlamada, CertRd34RCabeceraVO datosRespuesta, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("insertarDatosDetalleRD34() : BEGIN");
        boolean exito;
        Connection con = null;

        try{
            con = adaptador.getConnection();
            con.setAutoCommit(false);

            MeLanbide03RD34DAO meLanbide03RD34Dao = MeLanbide03RD34DAO.getInstance();
            exito = meLanbide03RD34Dao.insertarDatosDetalleRD34(datosLlamada, datosRespuesta, con);

            con.commit();

        } catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD insertando detalles de llamada RD34  : ", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando detalles de llamada RD34  : ", e);
        } catch(SQLException ex){
            try {
                con.rollback();
            } catch(SQLException f){
                log.error("Error al realizar un rollback insertando detalles de llamada RD34  : " + f.getMessage());
            }
            log.error("Se ha producido una excepción insertando detalles de llamada RD34  : ",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción insertando detalles de llamada RD34  : ",ex);
        } catch(Exception e){
            log.error("Se ha producido una excepción en la BBDD insertando detalles de llamada RD34  : ", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD insertando detalles de llamada RD34  : ", e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("insertarDatosDetalleRD34() : END");
        return exito;

    }

    public boolean updateErrorDatosCabeceraRD34 (CertRd34RCabeceraVO datosLlamada, String error, AdaptadorSQLBD adaptador) throws MELANBIDE03Exception{
        if(log.isDebugEnabled()) log.debug("updateErrorDatosCabeceraRD34() : BEGIN");
        boolean exito;
        Connection con = null;

        try{
            con = adaptador.getConnection();
            con.setAutoCommit(false);

            MeLanbide03RD34DAO meLanbide03RD34Dao = MeLanbide03RD34DAO.getInstance();
            exito = meLanbide03RD34Dao.updateErrorDatosCabeceraRD34(datosLlamada, error, con);

            con.commit();

        } catch(BDException e){
            log.error("Se ha producido una excepción en la BBDD actualizando error RD34  : ", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD actualizando error RD34  : ", e);
        } catch(SQLException ex){
            try {
                con.rollback();
            } catch(SQLException f){
                log.error("Error al realizar un rollback actualizando error RD34  : " + f.getMessage());
            }
            log.error("Se ha producido una excepción actualizando error RD34  : ",ex);
            throw new MELANBIDE03Exception("Se ha producido una excepción actualizando error RD34  : ",ex);
        } catch(Exception e){
            log.error("Se ha producido una excepción en la BBDD actualizando error RD34  : ", e);
            throw new MELANBIDE03Exception("Se ha producido una excepción en la BBDD actualizando error RD34  : ", e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) log.debug("updateErrorDatosCabeceraRD34() : END");
        return exito;

    }

}//class
