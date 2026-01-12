/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide49.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide49.dao.MeLanbide49DAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide49Manager 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide49Manager.class);
    
    //Instancia
    private static MeLanbide49Manager instance = null;
    
   
    public static MeLanbide49Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide49Manager.class)
            {
                instance = new MeLanbide49Manager();
            }
        }
        return instance;
    }     
    
//    public void insertarSecuencia (int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adaptador) throws Exception
//    {
//         log.info("==> ENTRA en insertarSecuencia Manager");
//        Connection con = null;
//        int resultado = 0;
//        try
//        {
//            con = adaptador.getConnection();
//            MeLanbide49DAO meLanbide49DAO = MeLanbide49DAO.getInstance();
//             log.info("Llama a insertarSecuencia DAO");
//          resultado=  meLanbide49DAO.insertarSecuencia(codOrganizacion, numExpediente, codTramite, con);
////           meLanbide49DAO.insertarSecuencia(codOrganizacion, numExpediente, codTramite, con);
//           log.info("insertarSecuencia DAO devuelve :" + resultado);
//        }
//        catch(BDException e)
//        {
//            log.error("Se ha producido una excepcion en la BBDD al generar el histórico con número de expediente "+ numExpediente, e);
//            throw new Exception(e);
//        }
//        catch(Exception ex)
//        {
//            log.error("Se ha producido una excepcion en la BBDD al generar el histórico con número de expediente "+numExpediente, ex);
//            throw new Exception(ex);
//        }
//        finally
//        {
//            try
//            {
//                adaptador.devolverConexion(con);       
//            }
//            catch(Exception e)
//            {
//                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
//            }
//        }
//        log.info("SALE de insertarSecuencia Manager ==>");
//    }
//    
//   
        public boolean insertarSecuencia (int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
         log.info("==> ENTRA en insertarSecuencia Manager");
        Connection con = null;
        boolean insertOK;
        
        try
        {
            con = adaptador.getConnection();
            MeLanbide49DAO meLanbide49DAO = MeLanbide49DAO.getInstance();

          insertOK=  meLanbide49DAO.insertarSecuencia(codOrganizacion, numExpediente, codTramite, con);
//           meLanbide49DAO.insertarSecuencia(codOrganizacion, numExpediente, codTramite, con);
           log.info("insertarSecuencia DAO devuelve :" + insertOK);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD al insertar el numero de agencia con número de expediente "+ numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD al insertar el numero de agencia con número de expediente "+numExpediente, ex);
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
        log.info("SALE de insertarSecuencia Manager ==>");
        return insertOK;
    }
    
   
}
