/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide57.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide57.dao.Melanbide57DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.dao.MelanbideJob57DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeGeneralVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeInternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeRGIVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.NoCatalogadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.TramiteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide57JobManager
{
    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide57JobManager.class);
    
    //Instancia
    private static MeLanbide57JobManager instance = null;
    
    private MeLanbide57JobManager()
    {
        
    }
    
    public static MeLanbide57JobManager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide57JobManager.class)
            {
                instance = new MeLanbide57JobManager();
            }
        }
        return instance;
    }
        
    public List<NoCatalogadoVO> getListaTramitesEliminar(Connection con) throws Exception
    {
        return MelanbideJob57DAO.getInstance().getListaTramitesEliminar(con);
    }
    
    public int insertarLineasLogJobEliminarCatalogado(AdaptadorSQLBD adaptador,NoCatalogadoVO catalogado) throws Exception {
        log.info("insertarLineasLogJobEliminarCatalogado - Begin () ");
        Integer retorno = 0;     
        Connection con = null;
        try { 
                  con = adaptador.getConnection();
           MelanbideJob57DAO meLanbide57JobDAO = MelanbideJob57DAO.getInstance();
           retorno =  meLanbide57JobDAO.insertarLineaJobEliminarCatalogados(con, catalogado);
        } catch (BDException ex){
            log.error("Se ha producido una excepcion en la BBDD insertando el log insertarLineasLogJobEliminarCatalogado " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            log.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally{
             if (con != null) {
            con.close();
              }
        }
        log.info("insertarLineasLogJobEliminarCatalogado - End () ");
        return retorno;
    }    
    
       public int eliminarCatalogado(AdaptadorSQLBD adaptador,NoCatalogadoVO catalogado) throws Exception {
        log.info("eliminarCatalogado - Begin () ");
        Integer retorno = 0;     
        Connection con = null;
        try { 
           con = adaptador.getConnection();
           MelanbideJob57DAO meLanbide57JobDAO = MelanbideJob57DAO.getInstance();
           retorno =  meLanbide57JobDAO.eliminarCatalogado(con, catalogado);
        } catch (BDException ex){
            log.error("Se ha producido una excepcion en la BBDD eliminando eliminarCatalogado " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            log.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally{
             if (con != null) {
            con.close();
              }
        }
        log.info("eliminarCatalogado - End () ");
        return retorno;
    }   
    
    
    
}
