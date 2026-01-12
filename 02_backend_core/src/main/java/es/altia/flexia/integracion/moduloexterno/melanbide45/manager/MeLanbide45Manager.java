/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide45.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide45.dao.MeLanbide45DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.espacioform.EspacioFormVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.materialconsu.MaterialConsuVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.moduloform.ModuloFormVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide45Manager {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide45Manager.class);    
    //Instancia
    private static MeLanbide45Manager instance = null;
    
    public static MeLanbide45Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide45Manager.class)
            {
                instance = new MeLanbide45Manager();
            }
        }
        return instance;
    }

    public List<ModuloFormVO> getDatosModulosForm(String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try
        {
            con = adapt.getConnection();
            MeLanbide45DAO meLanbide41DAO = MeLanbide45DAO.getInstance();
            return meLanbide41DAO.getDatosModulosForm(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre Modulos Formativos del Expediente: " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Modulos Formativos del Expediente: " + numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<MaterialConsuVO> getDatosMaterialConsu(String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try
        {
            con = adapt.getConnection();
            MeLanbide45DAO meLanbide41DAO = MeLanbide45DAO.getInstance();
            return meLanbide41DAO.getDatosMaterial(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre materiales consumibles del Expediente: " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Modulos Formativos del Expediente: " + numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<EspacioFormVO> getDatosEspaciosForm(String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try
        {
            con = adapt.getConnection();
            MeLanbide45DAO meLanbide41DAO = MeLanbide45DAO.getInstance();
            return meLanbide41DAO.getDatosEspaciosForm(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre Modulos Formativos del Expediente: " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Modulos Formativos del Expediente: " + numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<MaterialConsuVO> getDatosMaterialForm(String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try
        {
            con = adapt.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.getDatosMaterialConsu(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre Modulos Formativos del Expediente: " + numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Modulos Formativos del Expediente: " + numExpediente, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public ModuloFormVO getModuloFormPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.getModuloFormPorCodigo(numExpediente, id, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre un Modulo Formativo por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre un Modulo Formativo por codigo:" + numExpediente + " - " + id, ex);
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
    
    public MaterialConsuVO getMaterialesPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.getMaterialesPorCodigo(numExpediente, id, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre un Modulo Formativo por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre un Modulo Formativo por codigo:" + numExpediente + " - " + id, ex);
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
    
     public EspacioFormVO getEspacioFormPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.getEspacioFormPorCodigo(numExpediente, id, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre un Espacio Formativo por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre un Espacio Formativo por codigo:" + numExpediente + " - " + id, ex);
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
    
    public boolean crearNuevoModuloForm(ModuloFormVO datoNuevo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.crearNuevoModuloForm(datoNuevo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un Excepcion en la BBDD creando  un Modulo Formativo : " + e.getMessage(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD creando Modulo Formativo : " + ex.getMessage(), ex);
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
    
    public boolean crearMaterialConsu(MaterialConsuVO datoNuevo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.crearNuevoMaterial(datoNuevo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un Excepcion en la BBDD creando  un material consumible : " + e.getMessage(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD creando material consumible : " + ex.getMessage(), ex);
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
    
    public boolean modificarModuloForm(ModuloFormVO datoNuevo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.modificarModuloForm(datoNuevo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  un Modulo Formativo : " + e.getMessage(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD Modificando Modulo Formativo : " + ex.getMessage(), ex);
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
    
    public boolean modificarMateriales(MaterialConsuVO datoNuevo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.modificaMaterial(datoNuevo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  un Modulo Formativo : " + e.getMessage(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD Modificando Modulo Formativo : " + ex.getMessage(), ex);
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
    
    public int eliminarModuloForm(String numExp, Integer id, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.eliminarModuloForm(numExp, id, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando Modulo Formativo" + numExp + " - " + id, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando Modulo Formativo " + numExp + " - " + id, ex);
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
    
    public int eliminarMaterialConsu(String numExp, Integer id, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.eliminarMaterialesConsu(numExp, id, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando Modulo Formativo" + numExp + " - " + id, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando Modulo Formativo " + numExp + " - " + id, ex);
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
    
    public boolean crearNuevoEspacioForm(EspacioFormVO datoNuevo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.crearNuevoEspacioForm(datoNuevo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un Excepcion en la BBDD creando  un Espacio Formativo : " + e.getMessage(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD creando Espacio Formativo : " + ex.getMessage(), ex);
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
    
    public boolean modificarEspacioForm(EspacioFormVO datoNuevo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.modificarEspacioForm(datoNuevo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un Excepcion en la BBDD Modificando  un Espacio Formativo : " + e.getMessage(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD Modificando Espacio Formativo : " + ex.getMessage(), ex);
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
    
    public int eliminarEspacioForm(String numExp, Integer id, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.eliminarEspacioForm(numExp, id, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando Espacio Formativo" + numExp + " - " + id, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando Espacio Formativo " + numExp + " - " + id, ex);
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
    
    
    public String getValorCampoDesplegableExternoExpediente(int codOrganizacion,String numExpediente,String codCampo,AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.getValorCampoDesplegableExternoExpediente(codOrganizacion, numExpediente, codCampo, con);
        }
        catch(BDException e){
            log.error("Se ha producido una excepcion en la BBDD al recuperar el valor del campo desplegable " + codCampo + " en el expediente " + numExpediente + ": " + e.getMessage());
            throw new Exception(e);
        }
        catch(Exception ex){
            log.error("Error al recuperar el valor del campo desplegable " + codCampo + " en el expediente " + numExpediente + ": " + ex.getMessage());            
            throw new Exception(ex);
        }
        finally{
            try{
                adaptador.devolverConexion(con);       
            }
            catch(BDException e){
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }    
    
    
    
    public String getValorCodDesplegableExternoExpediente(int codOrganizacion,String numExpediente,String codCampo,AdaptadorSQLBD adaptador) throws Exception {
        
       Connection con = null;
               
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            String codigo = meLanbide45DAO.getValorCodDesplegableExternoExpediente(codOrganizacion,numExpediente,codCampo,con);
                        
            return codigo;
        }
        catch(BDException e){
            log.error("Se ha producido una excepcion en la BBDD al recuperar el código del campo desplegable externo " + codCampo + " en el expediente " + numExpediente + ": " + e.getMessage());
            throw new Exception(e);
        }
        catch(Exception ex){
            log.error("Error al recuperar el código del campo desplegable externo " + codCampo + " en el expediente " + numExpediente + ": " + ex.getMessage());            
            throw new Exception(ex);
        }
        finally{
            try{
                adaptador.devolverConexion(con);       
            }
            catch(BDException e){
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }    
        
    public String getSiguienteCodEsp(int codOrganizacion,String numExpediente,String area, AdaptadorSQLBD adaptador) throws Exception
    {
        // Esta funcion recupera de base de datos el codigo del area y su ultimo registro, devolviendo el que deberia ser el siguiente codigo
     Connection con = null;
     String codEsp = null;          
     String ucExiste = null;
     
        try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            codEsp = meLanbide45DAO.getValorSiguienteCodArea(codOrganizacion, numExpediente, area,  con);            
        }
        catch(BDException e){
            log.error("Se ha producido una excepcion en meLanbide45DAO.getValorSiguienteCodArea, area-> " + area + " en el expediente " + numExpediente + ": " + e.getMessage());
            throw new Exception(e);
        }
        finally{
            try{
                adaptador.devolverConexion(con);       
            }
            catch(BDException e){
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
                      
        
    return codEsp;
    }
    
     public String getSiguienteUC(int codOrganizacion,String numExpediente,String area, String nivel, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        String UC = null;
         try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();            
            UC = meLanbide45DAO.getValorSiguienteUC(codOrganizacion, numExpediente, nivel, con);
        }
        catch(BDException e){
            log.error("Se ha producido una excepcion en meLanbide45DAO.getValorSiguienteUC, nivel-> "  + nivel + " en el expediente " + numExpediente + ": " + e.getMessage());
            throw new Exception(e);
        }
        finally{
            try{
                adaptador.devolverConexion(con);       
            }
            catch(BDException e){
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return UC;
    }
     
    public String getSiguienteMF(int codOrganizacion,String numExpediente,String area, String nivel, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        String MF = null;
         try
        {
            con = adaptador.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();            
            MF = meLanbide45DAO.getValorSiguienteMF(codOrganizacion, numExpediente, nivel, con);
        }
        catch(BDException e){
            log.error("Se ha producido una excepcion en meLanbide45DAO.getValorSiguienteMF, nivel-> "  + nivel + " en el expediente " + numExpediente + ": " + e.getMessage());
            throw new Exception(e);
        }
        finally{
            try{
                adaptador.devolverConexion(con);       
            }
            catch(BDException e){
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return MF;
    }     
        
     /**
     * 
     * Función que llama al dao para obtener el número de censo SILICOI de base de datos
     *
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param corrSubservicio
     * @param adaptador: adapatador
     * @return 
     * @throws java.lang.Exception 
     */
    public String getNumCensoSILICOI(String codCentro, String codUbicacion, String corrSubservicio, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            if (adaptador != null) {
                con = adaptador.getConnection();
            }
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.getNumeroCensoSILICOI(codCentro, codUbicacion, corrSubservicio, con);
        } catch (BDException e) {
            log.error("Error en función getNumCensoSILICOI", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en función getNumCensoSILICOI", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (adaptador != null) {
                    adaptador.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
     
     /**
     * 
     * Función que obtiene el número de censo de un centro, que es el campo LAN_NUM_REG_AUT de la tabla LAN_CENTROS_SERVICIOS
     *
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param adaptador: adapatador
     * @return
     * @throws java.lang.Exception
     */
    public String getNumCensoLanbide(String codCentro, String codUbicacion, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            if (adaptador != null) {
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
                con = adaptador.getConnection();
            }
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.getNumeroCensoLanbide(codCentro, codUbicacion, con);
        } catch (BDException e) {
            log.error("Error en función getNumCensoLanbide", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Error en función getNumCensoLanbide", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (adaptador != null) {
                    adaptador.devolverConexion(con);
                }
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

      public boolean crearCodigoModifEspecialidad(String numExp, int mun, String valor, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try
        {
            con = adapt.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.crearCodigoModifEspecialidad(numExp, mun, valor, con);
        }
        catch(Exception ex)
        {
            log.error("Error en función crearCodigoModifEspecialidad", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

      public boolean modificarMFyUCModuloForm(ModuloFormVO nuevoDato, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try
        {
            con = adapt.getConnection();
            MeLanbide45DAO meLanbide45DAO = MeLanbide45DAO.getInstance();
            return meLanbide45DAO.modificarMFyUCModuloForm(nuevoDato, con);
        }
        catch(Exception ex)
        {
            log.error("Error en función crearCodigoModifEspecialidad", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    /**
     *
     * Metodo para recuperar los codigos corrServicio y corrSubservicio
     *
     * @param codCentro código del centro
     * @param codUbic código de la ubicación del centro
     * @param adaptador adaptador BD
     * @return array de Strings con los 2 códigos
     * @throws Exception
     */
    public String[] getCodigosCorr(String codCentro, String codUbic, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("Entra en getCodigosCorr de " + this.getClass().getSimpleName());
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide45DAO.getInstance().getCodigosCorr(codCentro, codUbic, con);
        } catch (Exception e) {
            log.error("Error en función getCodigosCorr", e);
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    /**
     *
     * @param codigoCentro
     * @param codigoUbicacion
     * @param corrSubservicio
     * @param adaptador
     * @return String con el número de censo del centro en CENFOR
     * @throws java.lang.Exception
     */
    public String getNumeroCensoCenFor(String codigoCentro, String codigoUbicacion, String corrSubservicio, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide45DAO.getInstance().getNumeroCensoCenFor(codigoCentro, codigoUbicacion, corrSubservicio, con);
        } catch (Exception e) {
            log.error("Error en función getNumeroCensoCenFor", e);
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
      
}
