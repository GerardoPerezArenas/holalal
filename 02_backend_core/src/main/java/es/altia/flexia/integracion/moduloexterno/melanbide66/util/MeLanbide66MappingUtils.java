/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide66.util;

import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.DatosSuplementariosExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.InformeApecVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.InformeApec_No_AceptadasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.SocioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide66.vo.TerceroVO;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author davidg
 */
public class MeLanbide66MappingUtils {

    private static MeLanbide66MappingUtils instance = null;

    private MeLanbide66MappingUtils() {
    }

    public static MeLanbide66MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide66MappingUtils.class) {
                instance = new MeLanbide66MappingUtils();
            }
        }
        return instance;
    }

    /*Mapeo para el detalle de errores*/
    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == InformeApecVO.class) {
            return mapearFacturaVO(rs);
        }
        if (clazz == InformeApec_No_AceptadasVO.class) {
            return mapearInformeApec_No_AceptadasVO(rs);
        }
        if (clazz == ExpedienteVO.class) {
            return mapearExpedienteVO(rs);
        }
        if (clazz == DatosEconomicosExpVO.class) {
            return mapearDatosEconomicosVO(rs);
        }
        if (clazz == DatosSuplementariosExpedienteVO.class) {
            return mapearImportesExpedienteVO(rs);
        }
        if (clazz == TerceroVO.class) {
            return mapearTerceroVO(rs);
        }
        if (clazz == SocioVO.class) {
            return mapearSocioVO(rs);
        }
        return null;
    }

     private Object mapearFacturaVO(ResultSet rs) throws SQLException {
         BigDecimal impPagadoNum2=null;
         InformeApecVO numero=new InformeApecVO();
         BigDecimal impteRenunciadoResolucion=BigDecimal.ZERO;
        /*
       IDENTIFICACION,ENTREGADA_FACT,ENTREGADO_JUSTIF,CODIGO_CONCEPTO,IMPORTE
        */
        String impNuevo=String.valueOf(rs.getBigDecimal("IMPORTE"));
        double importeDouble =Double.parseDouble(impNuevo);
        //impPagadoNum2 = impNuevo != null && !impNuevo.equals("") ? new BigDecimal(impNuevo.replaceAll(",", "\\.")) : null;
         
        numero.setIDENTIFICACION(rs.getString("IDENTIFICACION"));
        numero.setENTREGADA_FACT(rs.getString("ENTREGADA_FACT"));
        numero.setENTREGADO_JUSTIF(rs.getString("ENTREGADO_JUSTIF"));
        numero.setCODIGO_SUBCONCEPTO(rs.getInt("CODIGO_SUBCONCEPTO"));
        //BigDecimal num=new BigDecimal(numero.setIMPORTE(rs.getBigDecimal("IMPORTE")));
        
        //impteRenunciadoResolucion=new BigDecimal(MeLanbide66Util.redondearDecimalesString(IMPORTEnUEVO, 2));
        numero.setIMPORTE(importeDouble);
        
      
        
        return numero;
    }
     
    

    private Object mapearInformeApec_No_AceptadasVO(ResultSet rs) throws SQLException {
         InformeApec_No_AceptadasVO numero=new InformeApec_No_AceptadasVO();
        /*
       IDENTIFICACION,ENTREGADA_FACT,ENTREGADO_JUSTIF,CODIGO_CONCEPTO,IMPORTE
        */
        numero.setIDENTIFICACION(rs.getString("IDENTIFICACION"));
        numero.setENTREGADA_FACT(rs.getString("ENTREGADA_FACT"));
        numero.setENTREGADO_JUSTIF(rs.getString("ENTREGADO_JUSTIF"));
        numero.setCODIGO_SUBCONCEPTO(rs.getInt("CODIGO_SUBCONCEPTO"));
        numero.setIMPORTE(rs.getDouble("IMPORTE"));
        numero.setOBSERVACIONES(rs.getString("OBSERVACIONES"));
      
        
        return numero;
    }

    private Object mapearExpedienteVO(ResultSet rs) throws SQLException {
        ExpedienteVO expediente = new ExpedienteVO();
        TerceroVO tercero = new TerceroVO();
        tercero.setTSexoTercero(rs.getString("sexo"));
        tercero.setTFecNacimiento(rs.getDate("fecnacimiento"));
        expediente.setTercero(tercero);
        expediente.setFecPresentacion(rs.getDate("fecPresentacion"));
        expediente.setCifCBSC(rs.getString("cifCBSC"));
        if (rs.getString("esCBSC") != null && rs.getString("esCBSC").equalsIgnoreCase("S")) {
            expediente.setEsCBSC(true);
        } else {
            expediente.setEsCBSC(false);
        }
        return expediente;
    }

    private Object mapearDatosEconomicosVO(ResultSet rs) throws SQLException {
        DatosEconomicosExpVO datosEcon=new DatosEconomicosExpVO();
        datosEcon.setImporteSubvencion(rs.getDouble("SUB_IMPORTE"));
        datosEcon.setPorcentajePrimerPago(rs.getDouble("PLA_PORCENTAJE"));
        
        return datosEcon;
    }

    private Object mapearImportesExpedienteVO(ResultSet rs) throws SQLException {
        DatosSuplementariosExpedienteVO importesVO = new DatosSuplementariosExpedienteVO();
        Object importeOBJ = rs.getObject("IMPPRIMERPAGOG");
        if(importeOBJ!=null){
            importesVO.setImportePrimerPago(((BigDecimal)importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("IMPPRIMERPAGOCBSC");
        if(importeOBJ!=null){
            importesVO.setImportePrimerPagoCBSC(((BigDecimal)importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("IMPSUBVENCIONG");
        if(importeOBJ!=null){
            importesVO.setImporteSubvencion(((BigDecimal)importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("IMPSUBVENCBSC");
        if(importeOBJ!=null){
            importesVO.setImporteSubvencionCBSC(((BigDecimal)importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("IMPSEGUNDOPAGO2");
        if(importeOBJ!=null){
            importesVO.setImporteSegundoPago(((BigDecimal)importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importeOBJ = rs.getObject("CANTREINTEGRAR");
        if(importeOBJ!=null){
            importesVO.setImporteReintegro(((BigDecimal)importeOBJ).doubleValue());//Object porque si es null con getDouble lo convierte a 0.0
        }
        importesVO.setFecPrimerPago(rs.getDate("FECPRIMERPAGO"));
        return importesVO;
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

    private Object mapearSocioVO(ResultSet rs) throws SQLException {
        SocioVO socio = new SocioVO();
        socio.setId(rs.getInt("ID"));
        socio.setNumExp(rs.getString("NUM_EXP"));
        socio.setNombre(rs.getString("NOMBRE"));
        socio.setApellido1(rs.getString("APELLIDO1"));
        socio.setApellido2(rs.getString("APELLIDO2") != null && !rs.getString("APELLIDO2").isEmpty() ? rs.getString("APELLIDO2") : "");
        socio.setDni(rs.getString("DNINIE"));
        return socio;
    }  

}
