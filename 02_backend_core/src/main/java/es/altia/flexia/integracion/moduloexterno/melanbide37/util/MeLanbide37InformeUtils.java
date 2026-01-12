/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide37.util;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.flexia.integracion.moduloexterno.melanbide37.i18n.MeLanbide37I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCPVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCompAPAVO;
import es.altia.flexia.integracion.moduloexterno.melanbide37.vo.FilaListadoCompCPVO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import oracle.sql.STRUCT;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 *
 * @author lauras
 */
public class MeLanbide37InformeUtils {
    private static MeLanbide37InformeUtils instance = null;
     //Logger
    private static Logger log = LogManager.getLogger(MeLanbide37InformeUtils.class);
    private MeLanbide37InformeUtils()
    {
        
    }
    
    public static MeLanbide37InformeUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide37InformeUtils.class)
            {
                instance = new MeLanbide37InformeUtils();
            }
        }
        return instance;
    }
    
     public static Integer getEjercicioDeExpediente(String numExpediente)
    {
        try
        {
            String[] datos = numExpediente.split(ConstantesMeLanbide37.BARRA_SEPARADORA);
            return Integer.parseInt(datos[0]);
        }
        catch(Exception ex)
        {
            return null;
        }
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
            filaAct.setDesOrigenGaituz(rs.getString("ORIGEN_GAITUZ"));
            filaAct.setEnviadoSilcoi(rs.getString("ENVIADO_SILCOI"));
            // nuevo inlcuir provincia y municipio 2016-01-19
            filaAct.setCodProvicia(rs.getString("COD_PROVINCIA"));
            filaAct.setProvincia(rs.getString("PROVINCIA"));
            filaAct.setCodMuncipio(rs.getString("COD_MUNICIPIO"));
            filaAct.setMunicipio(rs.getString("MUNICIPIO"));
            filaAct.setTitRecogido(rs.getString("TIT_RECOGIDO"));
            //--
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
        log.error("NoError - extraerDatosListadoCompAPA - begin ");
        List<FilaListadoCompAPAVO> retList = new ArrayList<FilaListadoCompAPAVO>();
        
        String numexp = null;       
        FilaListadoCompAPAVO filaAct = null;
        log.error("NoError - extraerDatosListadoCompAPA - Empezamos a mapear ");
        int numero =0;
        while(rs.next())
        {
            numero++;
            //log.error("NoError - extraerDatosListadoCompAPA - Dato : " + numero);
            numexp = rs.getString("NUMEXP");        
            //log.error("NoError - extraerDatosListadoCompAPA - Expediente : " + numexp);
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
            
            filaAct.setDesOrigenGaituz(rs.getString("ORIGEN_GAITUZ"));
            filaAct.setEnviadoSilcoi(rs.getString("ENVIADO_SILCOI"));
            
            retList.add(filaAct);
        }        
        log.error("NoError - extraerDatosListadoCompAPA - Fin mapeo - END ");
        return retList;
    }
     
     public List<FilaListadoCompAPAVO> extraerDatosListadoCompAPA(Object[] arregloDatos) throws Exception
    {
        log.error("NoError - extraerDatosListadoCompAPA - begin ");
        List<FilaListadoCompAPAVO> retList = new ArrayList<FilaListadoCompAPAVO>();
        
        String numexp = null;       
        FilaListadoCompAPAVO filaAct = null;
        log.error("NoError - extraerDatosListadoCompAPA - Empezamos a mapear ");
        int numero =0;
        for(int i=0;i<arregloDatos.length;i++)
        {
            numero++;
            log.error("NoError - extraerDatosListadoCompAPA - Dato : " + numero);
            STRUCT st = (STRUCT)arregloDatos[i];
            Object[] fila2 = st.getAttributes();
            numexp = fila2[0]!=null?fila2[0].toString():"";        
            log.error("NoError - extraerDatosListadoCompAPA - Expediente : " + numexp);
            filaAct = new FilaListadoCompAPAVO();   
            filaAct.setNumExp(numexp);
            filaAct.setFecSolicitud(fila2[1]!=null?fila2[1].toString():"");
            filaAct.setNifInteresado(fila2[2]!=null?fila2[2].toString():"");
            filaAct.setNomInteresado(fila2[3]!=null?fila2[3].toString():"");
            filaAct.setFecNacimiento(fila2[4]!=null?fila2[4].toString():"");
            filaAct.setValoracionCPAPA(fila2[5]!=null?fila2[5].toString():"");
            filaAct.setCentroExp(fila2[6]!=null?fila2[6].toString():"");
            filaAct.setCodigoCP(fila2[7]!=null?fila2[7].toString():"");
            filaAct.setDenoCpCas(fila2[8]!=null?fila2[8].toString():"");
            filaAct.setDenoCpEus(fila2[9]!=null?fila2[9].toString():"");
            filaAct.setClaveRegistralCP(fila2[10]!=null?fila2[10].toString():"");
            filaAct.setRealDecreto(fila2[11]!=null?fila2[11].toString():"");
            filaAct.setFecRealDecreto(fila2[12]!=null?fila2[12].toString():"");
            filaAct.setRealDecretoModif(fila2[13]!=null?fila2[13].toString():"");
            filaAct.setFecRealDecretoModif(fila2[14]!=null?fila2[14].toString():"");
            filaAct.setCodigoUC(fila2[15]!=null?fila2[15].toString():"");
            filaAct.setDenoUcCas(fila2[16]!=null?fila2[16].toString():"");
            filaAct.setDenoUcEus(fila2[17]!=null?fila2[17].toString():"");
            filaAct.setCentroUc(fila2[18]!=null?fila2[18].toString():"");
            filaAct.setAcreditacion(fila2[19]!=null?fila2[19].toString():"");
            filaAct.setAcreditado(fila2[20]!=null?fila2[20].toString():"");
            filaAct.setOrigenAcreditacion(fila2[21]!=null?fila2[21].toString():"");
            filaAct.setClaveRegistralUC(fila2[22]!=null?fila2[22].toString():"");
            filaAct.setTramiteActivo(fila2[23]!=null?fila2[23].toString():"");
            filaAct.setDesOrigenGaituz(fila2[24]!=null?fila2[24].toString():"");
            filaAct.setEnviadoSilcoi(fila2[25]!=null?fila2[25].toString():"");
            retList.add(filaAct);
        }        
        log.error("NoError - extraerDatosListadoCompAPA - Fin mapeo - END ");
        return retList;
    }
}