/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerUnidadeCompetencialVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #86969 Edición inicial</li>
 * </ol> 
 */
public class CerUnidadesCompetenciaDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(CerUnidadesCompetenciaDAO.class);
    
    //Instancia
    private static CerUnidadesCompetenciaDAO instance = null;
    
    /**
     * Devuelve una instancia de CerUnidadesCompetenciaDAO, si no existe la crea.
     * @return CerUnidadesCompetenciaDAO
     */
    public static CerUnidadesCompetenciaDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() :BEGIN");
        if(instance == null){
            synchronized(CerUnidadesCompetenciaDAO.class){
                if(instance == null){
                    instance = new CerUnidadesCompetenciaDAO();
                }//if(instance == null)
            }//synchronized(CerUnidadesCompetenciaDAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Devuelve un array con las unidades de competencia para un certificado identificado por su código
     * @param codCertificado
     * @param con
     * @return ArrayList<CerUnidadeCompetenciaVO>
     * @throws Exception 
     */
    public ArrayList<CerUnidadeCompetencialVO> getListaUnidadesCompetencia (String codCertificado ,Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getListaUnidadesCompetencia() : BEGIN"); 
        ArrayList<CerUnidadeCompetencialVO> listaUnidades = new ArrayList<CerUnidadeCompetencialVO>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select * from " + GlobalNames.ESQUEMA_GENERICO
                + ConfigurationParameter.getParameter(ConstantesMeLanbide03.CER_UNIDADES_COMPETENCIA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                + " where CODUNIDAD in (Select UNIDADCOMPETENCIA from " +  GlobalNames.ESQUEMA_GENERICO 
                + ConfigurationParameter.getParameter(ConstantesMeLanbide03.CER_REL_MOD_CERT, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                + " where CODCERTIFICADO='" + codCertificado + "') order by CODUNIDAD ASC" ;
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                CerUnidadeCompetencialVO cerUnidadeCompetencial = new CerUnidadeCompetencialVO();
                cerUnidadeCompetencial.setCodUnidad(rs.getString("CODUNIDAD"));
                cerUnidadeCompetencial.setDescUnidadC(rs.getString("DESUNIDAD_C"));
                cerUnidadeCompetencial.setDescUnidadE(rs.getString("DESUNIDAD_E"));
                cerUnidadeCompetencial.setNivel(Integer.valueOf(rs.getString("NIVEL")));
                
                listaUnidades.add(cerUnidadeCompetencial);
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando las unidades competenciales", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getListaUnidadesCompetencia() : END"); 
        return listaUnidades;
    }//getListaUnidadesCompetencia
    
    public void getUnidadCompetencia (CerUnidadeCompetencialVO unidad, Connection con)throws Exception{
        if(log.isDebugEnabled()) log.debug("getUnidadCompetencia() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql="Select * from " + GlobalNames.ESQUEMA_GENERICO
                + ConfigurationParameter.getParameter(ConstantesMeLanbide03.CER_UNIDADES_COMPETENCIA, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                + " where CODUNIDAD = '" + unidad.getCodUnidad() + "'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                unidad.setDescUnidadC(rs.getString("DESUNIDAD_C"));
                unidad.setDescUnidadE(rs.getString("DESUNIDAD_E"));
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando la unidad competencial", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getUnidadCompetencia() : END");
    }//getUnidadCompetencia
    
}//class
