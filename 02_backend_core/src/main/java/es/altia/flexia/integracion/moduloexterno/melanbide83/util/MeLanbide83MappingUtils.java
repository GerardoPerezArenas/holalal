/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide83.util;

import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InformeAtase_No_AceptadasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.InformeAtaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide83.vo.TerceroVO;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MeLanbide83MappingUtils {

    private static MeLanbide83MappingUtils instance = null;

    private MeLanbide83MappingUtils() {
    }

    public static MeLanbide83MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide83MappingUtils.class) {
                instance = new MeLanbide83MappingUtils();
            }
        }
        return instance;
    }


    public Object map(ResultSet rs, Class clazz) throws Exception {
    if (clazz == InformeAtaseVO.class) {
            return mapearFacturaVO(rs);
    }
    
    if (clazz == InformeAtase_No_AceptadasVO.class) {
            return mapearInformeAtase_No_AceptadasVO(rs);
    }

    if (clazz == TerceroVO.class) {
            return mapearTerceroVO(rs);
    }
       
        return null;
    }

     private Object mapearFacturaVO(ResultSet rs) throws SQLException {
        InformeAtaseVO numero=new InformeAtaseVO();
     
        String impNuevo=String.valueOf(rs.getBigDecimal("IMPORTE"));
        double importeDouble =Double.parseDouble(impNuevo);
        numero.setENTREGADO_JUSTIF(rs.getString("ENTREGADO_JUSTIF"));
        numero.setIMPUTADA(rs.getString("IMPUTADA"));
        numero.setCODIGO_CONCEPTO(rs.getInt("CODIGO_CONCEPTO"));
        numero.setIMPORTE(importeDouble);
        numero.setOBSERVACIONES(rs.getString("OBSERVACIONES"));
        
        return numero;
    }
     
    

    private Object mapearInformeAtase_No_AceptadasVO(ResultSet rs) throws SQLException {
        InformeAtase_No_AceptadasVO numero=new InformeAtase_No_AceptadasVO();
       
        numero.setENTREGADO_JUSTIF(rs.getString("ENTREGADO_JUSTIF"));
        numero.setIMPUTADA(rs.getString("IMPUTADA"));
        numero.setCODIGO_CONCEPTO(rs.getInt("CODIGO_CONCEPTO"));
        numero.setIMPORTE(rs.getDouble("IMPORTE"));
        numero.setOBSERVACIONES(rs.getString("OBSERVACIONES"));
      
        
        return numero;
    }

    /***
     * 
     * @param rs
     * @return
     * @throws SQLException 
            * Name          Null?    Type          
       ------------- -------- ------------- 
       HTE_TER       NOT NULL NUMBER(12)    
       HTE_NVR       NOT NULL NUMBER(3)     
       HTE_DOT                NUMBER(12)    
       HTE_TID       NOT NULL NUMBER(2)     
       HTE_DOC                VARCHAR2(25)  
       HTE_NOM       NOT NULL VARCHAR2(150) 
       HTE_AP1                VARCHAR2(100) 
       HTE_PA1                VARCHAR2(10)  
       HTE_AP2                VARCHAR2(100) 
       HTE_PA2                VARCHAR2(10)  
       HTE_NOC                VARCHAR2(160) 
       HTE_NML       NOT NULL NUMBER(1)     
       HTE_TLF                VARCHAR2(40)  
       HTE_DCE                VARCHAR2(100) 
       HTE_FOP       NOT NULL DATE          
       HTE_USU       NOT NULL NUMBER(4)     
       HTE_APL       NOT NULL NUMBER(3)     
       EXTERNAL_CODE          VARCHAR2(15)
     */
    private Object mapearTerceroVO(ResultSet rs) throws SQLException {
        TerceroVO terceroVO = new TerceroVO();
        terceroVO.setCodTer(String.valueOf(rs.getInt("HTE_TER")));
        if(rs.wasNull()){
            terceroVO.setCodTer(null);
        }
        terceroVO.setVersionTercero(String.valueOf(rs.getInt("HTE_NVR")));
        if(rs.wasNull()){
            terceroVO.setVersionTercero(null);
        }
        terceroVO.setCodRol(String.valueOf(rs.getInt("EXT_ROL")));
        if(rs.wasNull()){
            terceroVO.setCodRol(null);
        }
        terceroVO.setDoc(rs.getString("HTE_DOC"));
        terceroVO.setTipoDoc(String.valueOf(rs.getInt("HTE_TID")));
        if(rs.wasNull()){
            terceroVO.setTipoDoc(null);
        }
        terceroVO.setNombre(rs.getString("HTE_NOM"));
        terceroVO.setApellido1(rs.getString("HTE_AP1"));
        terceroVO.setApellido2(rs.getString("HTE_AP2"));
        terceroVO.setNombreCompleto(rs.getString("HTE_NOC"));
        terceroVO.setTFecNacimiento(rs.getDate("TFecNacimiento"));
        terceroVO.setTNacionTercero(rs.getString("TNacionTercero"));
        terceroVO.setTSexoTercero(rs.getString("TSexoTercero"));
        terceroVO.setEdad(rs.getInt("Edad"));
        return terceroVO;
    }
    
   

}
