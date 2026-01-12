/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide06.dao;

import es.altia.util.commons.DateOperations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import sun.security.jca.GetInstance;

/**
 *
 * @author david.caamano
 */
public class MeLanbide06Dao {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide06Dao.class);
    
    //Instance 
    private static MeLanbide06Dao instance = null;
    
    /**
     * Devuelve una unica instancia (Singleton) de la clase MeLanbide06Dao
     * 
     * @return MeLanbide06Dao
     */
    public static MeLanbide06Dao getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide06Dao.class){
                if(instance == null){
                    if(log.isDebugEnabled()) log.debug("Se crea una instancia de la clase");
                    instance = new MeLanbide06Dao();
                }//if(instance == null)
            }//synchronized(MeLanbide06Dao.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Devuelve la descripcion de la UOR un tramite para un expediente
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param codProcedimiento
     * @param numExpediente
     * @param ejercicio
     * @param c
     * @return
     * @throws Exception 
     */
    public String getDescripcionUorTramite (Integer codOrganizacion, Integer codTramite, String codProcedimiento, String numExpediente,
            String ejercicio, Connection c) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionUorTramite() :BEGIN");
        String descripcion = new String();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = "Select UOR_NOM";
                sql +=" From A_UOR uor, E_CRO tramite";
                sql +=" Where uor.uor_cod = tramite.cro_utr";
                sql +=" And tramite.cro_pro = ?";
                sql +=" And tramite.cro_eje = ?";
                sql +=" And tramite.cro_num = ?";
                sql +=" And tramite.cro_tra = ?";
            
            if (log.isDebugEnabled()) log.debug("SQL = " + sql);
            ps = c.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            ps.setInt(2, Integer.valueOf(ejercicio));
            ps.setString(3, numExpediente);
            ps.setInt(4, codTramite);
            
            rs = ps.executeQuery();
            while(rs.next()){
                descripcion = rs.getString("UOR_NOM");
            }//while(rs.next()
        }catch(SQLException e){
            log.error("Se ha producido un error recuperando la descripcion de la UOR del tramite " + e.getMessage());
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Cerramos el prepared statement y el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDescripcionUorTramite() :END");
        return descripcion;
    }//getDescripcionUorTramite
    
    /**
     * Devuelve la descripcion de una unidad organica por su codigo visible
     * 
     * @param uorCodVis
     * @param c
     * @return
     * @throws Exception 
     */
    public String getDescripcionUor (String uorCodVis, Connection c) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionUor() :BEGIN");
        String descripcionUor = new String();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = "Select UOR_NOM ";
                sql +=" From A_UOR uor ";
                sql +=" where uor.uor_cod_vis = ?";
            if (log.isDebugEnabled()) log.debug("SQL = " + sql);
            ps = c.prepareStatement(sql);
            ps.setString(1, uorCodVis);
            
            rs = ps.executeQuery();
            while(rs.next()){
                descripcionUor = rs.getString("UOR_NOM");
            }//while(rs.next()
        }catch(SQLException e){
            log.error("Se ha producido un error recuperando la descripcion" + e.getMessage());
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Cerramos el prepared statement y el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDescripcionUor() :END");
        return descripcionUor;
    }//getDescripcionUor
    
    /**
     * Devuelve la descripcion del tramite
     * 
     * @param codTramite
     * @param c
     * @return
     * @throws Exception 
     */
    public String getDescripcionTramite (int codTramite, String codProcedimiento, Connection c) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionTramite() :BEGIN");
        String descripcionUor = new String();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{

            String sql = "SELECT E_TRA.TRA_COU,E_TRA.TRA_COD,E_TML.TML_VALOR FROM E_TML";
                sql +=" INNER JOIN E_TRA ON TRA_COD=TML_TRA AND TRA_PRO=TML_PRO";
                sql +=" WHERE TML_PRO=? and E_TRA.TRA_COD= ?";
            if (log.isDebugEnabled()) log.debug("SQL = " + sql);
            ps = c.prepareStatement(sql);
            ps.setString(1, codProcedimiento);
            ps.setInt(2, codTramite);
            
            rs = ps.executeQuery();
            while(rs.next()){
                descripcionUor = rs.getString("TML_VALOR");
            }//while(rs.next()
        }catch(SQLException e){
            log.error("Se ha producido un error recuperando la descripcion" + e.getMessage());
            throw e;
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Cerramos el prepared statement y el resultset");
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                log.error("Se ha producido un error cerrando el prepared statement y el resultset " + e.getMessage());
                throw e;
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDescripcionTramite() :END");
        return descripcionUor;
    }//getDescripcionTramite
    
    /**
     * Devuelve el interesado
     * 
     * @param codTramite
     * @param c
     * @return
     * @throws Exception 
     */
    public String getInteresado (String expediente, Connection c) throws Exception{
        if(log.isDebugEnabled()) log.debug("getInteresado() :BEGIN");
        String interesado = new String();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{

            String sql = "SELECT t_hte.hte_nom,t_hte.hte_ap1,t_hte.hte_ap2";
                sql +=" FROM t_hte,e_ext";
                //COGEMOS EL INTERESADO DEL ROL POR DEFECTO
                sql +=" INNER JOIN E_ROL ON ROL_COD=EXT_ROL AND ROL_PRO=EXT_PRO AND ROL_PDE=1 ";
                sql +=" WHERE hte_ter = ext_ter AND hte_nvr = ext_nvr AND ext_num = ?";
            if (log.isDebugEnabled()) log.debug("SQL = " + sql);
            ps = c.prepareStatement(sql);
            ps.setString(1, expediente);
            
            rs = ps.executeQuery();
            while(rs.next()){
                String ape1="";
                String ape2="";
                if(rs.getString("hte_ap1")!=null){
                    ape1=rs.getString("hte_ap1");
                }else{
                    ape1="";
                }
                
                if(rs.getString("hte_ap2")!=null){
                    ape2=rs.getString("hte_ap2");
                }else{
                    ape2="";
                }
                
                interesado = rs.getString("hte_nom")+" "+ ape1 +" "+ape2;
            }//while(rs.next()
        }catch(SQLException e){
            log.error("Se ha producido un error recuperando el interesado" + e.getMessage());
            throw e;
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Cerramos el prepared statement y el resultset");
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                log.error("Se ha producido un error cerrando el prepared statement y el resultset " + e.getMessage());
                throw e;
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getInteresado() :END");
        return interesado;
    }//getInteresado
    
}//class
