/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.util;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide43.i18n.MeLanbide43I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaListadoCPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaListadoCompAPAVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaListadoCompCPVO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author lauras
 */
public class MeLanbide43InformeUtils {
    private static MeLanbide43InformeUtils instance = null;
    
    private MeLanbide43InformeUtils()
    {
        
    }
    
    public static MeLanbide43InformeUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide43InformeUtils.class)
            {
                instance = new MeLanbide43InformeUtils();
            }
        }
        return instance;
    }
    
    public List<FilaListadoCPVO> extraerDatosListadoCP(ResultSet rs) throws Exception
    {
        List<FilaListadoCPVO> retList = new ArrayList<FilaListadoCPVO>();
        
        String numexp = null;       
        FilaListadoCPVO filaAct = null;
        while(rs.next())
        {
            numexp = rs.getString("NUMEXP");            
            filaAct = new FilaListadoCPVO();   
            filaAct.setNumExp(numexp);
            filaAct.setFecSolicitud(rs.getString("FECSOLICITUD"));
            filaAct.setNifInteresado(rs.getString("NIFINTERESADO"));
            filaAct.setNomInteresado(rs.getString("NOMINTERESADO"));
            filaAct.setFecNacimiento(rs.getString("FECNACIMIENTO"));
            filaAct.setTipoAcreditacion(rs.getString("TIPOACREDITACION"));
            filaAct.setValoracionCPAPA(rs.getString("VALORACION"));
            filaAct.setCentroExp(rs.getString("CENTROEXP"));
            filaAct.setCodigoCP(rs.getString("CODIGOCP"));
            filaAct.setDenoCpCas(rs.getString("DESCERTIFICADO_C"));
            filaAct.setDenoCpEus(rs.getString("DESCERTIFICADO_E"));
            filaAct.setClaveRegistralCP(rs.getString("CLAVEREGISTRAL"));
            filaAct.setRealDecreto(rs.getString("DECRETO"));
            filaAct.setFecRealDecreto(rs.getString("FECHA_RD"));
            filaAct.setRealDecretoModif(rs.getString("DECRETO_MOD"));
            filaAct.setFecRealDecretoModif(rs.getString("FECHA_MODIF_RD"));
            filaAct.setTramiteActivo(rs.getString("TRAMITE"));
            filaAct.setCodModuloPrac(rs.getString("COD_MODULO"));
            filaAct.setDesModuloPracCas(rs.getString("DESMODULO_C"));
            filaAct.setDesModuloPracEus(rs.getString("DESMODULO_E"));
            filaAct.setMotivoAcredModuloPrac(rs.getString("MOTIVOACREDITADOMOD"));
            filaAct.setAcreditadoModuloPrac(rs.getString("MODULOACREDITADO"));
            retList.add(filaAct);
            
        }        
        return retList;
    }
    
    public List<FilaListadoCompCPVO> extraerDatosListadoCompCP(ResultSet rs) throws Exception
    {
        List<FilaListadoCompCPVO> retList = new ArrayList<FilaListadoCompCPVO>();
        
        String numexp = null;       
        FilaListadoCompCPVO filaAct = null;
        while(rs.next())
        {
            numexp = rs.getString("NUMEXP");            
            filaAct = new FilaListadoCompCPVO();   
            filaAct.setNumExp(numexp);
            filaAct.setFecSolicitud(rs.getString("FECSOLICITUD"));
            filaAct.setNomInteresado(rs.getString("NOMINTERESADO"));
            filaAct.setFecNacimiento(rs.getString("FECNACIMIENTO"));
            filaAct.setValoracionCPAPA(rs.getString("VALORACION"));
            filaAct.setCentroExp(rs.getString("CENTROEXP"));
            filaAct.setCodigoCP(rs.getString("CODIGOCP"));
            filaAct.setDenoCpCas(rs.getString("DESCERTIFICADO_C"));
            filaAct.setDenoCpEus(rs.getString("DESCERTIFICADO_E"));
            filaAct.setClaveRegistralCP(rs.getString("CLAVEREGISTRAL"));
            filaAct.setRealDecreto(rs.getString("DECRETO"));
            filaAct.setFecRealDecreto(rs.getString("FECHA_RD"));
            filaAct.setRealDecretoModif(rs.getString("DECRETO_MOD"));
            filaAct.setFecRealDecretoModif(rs.getString("FECHA_MODIF_RD"));
            //filaAct.setClaveRegistralUC(rs.getString("CLAVEREGISTRAL_UC"));
            retList.add(filaAct);
            
        }        
        return retList;
    }
    
     public List<FilaListadoCompAPAVO> extraerDatosListadoCompAPA(ResultSet rs) throws Exception
    {
        List<FilaListadoCompAPAVO> retList = new ArrayList<FilaListadoCompAPAVO>();
        
        String numexp = null;       
        FilaListadoCompAPAVO filaAct = null;
        while(rs.next())
        {
            numexp = rs.getString("NUMEXP");            
            filaAct = new FilaListadoCompAPAVO();   
            filaAct.setNumExp(numexp);
            filaAct.setFecSolicitud(rs.getString("FECSOLICITUD"));
            filaAct.setNifInteresado(rs.getString("NIFINTERESADO"));
            filaAct.setNomInteresado(rs.getString("NOMINTERESADO"));
            filaAct.setFecNacimiento(rs.getString("FECNACIMIENTO"));
            filaAct.setValoracionCPAPA(rs.getString("VALORACION"));
            filaAct.setCentroExp(rs.getString("CENTROEXP"));
            filaAct.setCodigoCP(rs.getString("CODIGOCP"));
            filaAct.setDenoCpCas(rs.getString("DESCERTIFICADO_C"));
            filaAct.setDenoCpEus(rs.getString("DESCERTIFICADO_E"));
            filaAct.setClaveRegistralCP(rs.getString("CLAVEREGISTRAL"));
            filaAct.setRealDecreto(rs.getString("DECRETO"));
            filaAct.setFecRealDecreto(rs.getString("FECHA_RD"));
            filaAct.setRealDecretoModif(rs.getString("DECRETO_MOD"));
            filaAct.setFecRealDecretoModif(rs.getString("FECHA_MODIF_RD"));
            filaAct.setClaveRegistralUC(rs.getString("CLAVEREGISTRAL_UC"));
            filaAct.setCodigoUC(rs.getString("UNIDADCOMPETENCIA"));
            filaAct.setDenoUcCas(rs.getString("DESUNIDAD_C"));
            filaAct.setDenoUcEus(rs.getString("DESUNIDAD_E"));
            filaAct.setCentroUc(rs.getString("CENTROUC"));
            filaAct.setAcreditado(rs.getString("CENTRO_ACREDITADO"));
            filaAct.setOrigenAcreditacion(rs.getString("COD_ORIGEN_ACREDITACION"));
            filaAct.setTramiteActivo(rs.getString("TRAMITE"));
            filaAct.setAcreditacion(rs.getString("TIPO"));
            
            /* //modulo practicas
            filaAct.setCodModuloPrac(rs.getString("COD_MODULO"));
            filaAct.setDesModuloPracCas(rs.getString("DESMODULO_C"));
            filaAct.setDesModuloPracEus(rs.getString("DESMODULO_E"));
            filaAct.setMotivoAcredModuloPrac(rs.getString("MOTIVOACREDITADOMOD"));
            filaAct.setAcreditadoModuloPrac(rs.getString("MODULOACREDITADO"));*/
            
            retList.add(filaAct);
            
        }        
        return retList;
    }
}