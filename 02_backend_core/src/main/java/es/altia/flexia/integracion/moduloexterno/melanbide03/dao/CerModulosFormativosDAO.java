/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerModuloFormativoVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * @author david.caamano
 * @version 12/09/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 12-09-2012 * #86969 Edición inicial</li>
 * </ol> 
 */
public class CerModulosFormativosDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(CerModulosFormativosDAO.class);
    
    //Instance
    private static CerModulosFormativosDAO instance = null;
    
    /**
     * Devuelve una única instancia de la clase CerCertificadosDao, si no existe la crea.
     * @return CerCertificadosDao
     */
    public static CerModulosFormativosDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if (instance == null) {
            synchronized(CerModulosFormativosDAO.class){
                if(instance == null){
                    instance = new CerModulosFormativosDAO();
                }//if(instance == null)
            }//synchronized(CerModulosFormativosDAO.class)
        }//if (instance == null) 
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    public ArrayList<CerModuloFormativoVO> getModulosFormativos (String codCertificado, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getModulosFormativos() : BEGIN");
        ArrayList<CerModuloFormativoVO> modulosFormativos = new ArrayList<CerModuloFormativoVO>();
        ArrayList<String> codigosModulosFormativos = new ArrayList<String>();
        if(log.isDebugEnabled()) log.debug("Primero recuperamos los códigos de los modulos formativos");
        codigosModulosFormativos = getCodigosModulosFormativos(codCertificado, con);
        if(codigosModulosFormativos.size() > 0){
            if(log.isDebugEnabled()) log.debug("El certificado tiene módulos formativos");
            Statement st = null;
            ResultSet rs = null;
            try{
                String sql = "Select * from " + GlobalNames.ESQUEMA_GENERICO
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide03.CER_MODULOS_FORMATIVOS, 
                        ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " where CODMODULO in ( ";
                        for(int x=0; x<codigosModulosFormativos.size(); x++){
                           sql += "'" + codigosModulosFormativos.get(x) + "'";
                            if(x < codigosModulosFormativos.size()-1){
                                sql += ",";
                            }//if(x >= unidades.size()-1) 
                        }//for(String codigo : codigosModulosFormativos)
                    sql += " )";
                    
                if(log.isDebugEnabled()) log.debug("sql = " + sql);
                st = con.createStatement();
                rs = st.executeQuery(sql);
                
                while(rs.next()){
                    CerModuloFormativoVO modulo = new CerModuloFormativoVO();
                    modulo.setCodModulo(rs.getString("CODMODULO"));
                    modulo.setDesModuloC(rs.getString("DESMODULO_C"));
                    modulo.setDesModuloE(rs.getString("DESMODULO_E"));
                    if(rs.getString("NIVEL") != null){
                        modulo.setNivel(Integer.valueOf(rs.getString("NIVEL")));
                    }//if(rs.getString("NIVEL") != null)
                    if(rs.getString("DURACION") != null){
                        modulo.setDuracion(Integer.valueOf(rs.getString("DURACION")));
                    }//if(rs.getString("DURACION") != null)
                    modulosFormativos.add(modulo);
                }//while(rs.next())
            }catch (Exception e) {
                log.error("Se ha producido un error recuperando los certificados", e);
                throw new Exception(e);
            }finally{
                if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
                if(rs!=null) rs.close();
                if(st!=null) st.close();
            }//try-catch-finally
        }//if(codigosModulosFormativos.size() > 0)
        if(log.isDebugEnabled()) log.debug("getModulosFormativos() : END");
        return modulosFormativos;
    }//getModulosFormativos
    
    private ArrayList<String> getCodigosModulosFormativos(String codCertificado, Connection con) throws Exception{
        if(log.isDebugEnabled()) log.debug("getCodigosModulosFormativos() : BEGIN");
        ArrayList<String> codigosModulosFormativos = new ArrayList<String>();
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "Select * from " + GlobalNames.ESQUEMA_GENERICO
                + ConfigurationParameter.getParameter(ConstantesMeLanbide03.CER_REL_MOD_CERT, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                + " where CODCERTIFICADO = '" + codCertificado + "' and CODMODULO like 'MP%'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while(rs.next()){
                codigosModulosFormativos.add(rs.getString("CODMODULO"));
            }//while(rs.next())
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los certificados", e);
            throw new Exception(e);
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getCodigosModulosFormativos() : END");
        return codigosModulosFormativos;
    }//getCodigosModulosFormativos
    
}//class
