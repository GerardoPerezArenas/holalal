/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide43.util;

import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoSeleccionadoV0;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaFaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.TramiteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FaseVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaLlamadasMisGestVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaNotificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Tml;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author javier
 */
public class MeLanbide43MappingUtils {
     private static MeLanbide43MappingUtils instance = null;
    
    private MeLanbide43MappingUtils()
    {
        
    }
    
    public static MeLanbide43MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide43MappingUtils.class)
            {
                instance = new MeLanbide43MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == ProcedimientoVO.class)
        {
            return mapearProcedimientosVO(rs);
        }
        if(clazz == Tml.class){
            return mapearTml(rs);
        }
        if(clazz == FilaFaseVO.class){
            return mapearFilaFasesVO(rs);
        }
        if(clazz == TramiteVO.class){
            return mapearTramitesVO(rs);
        }
        if(clazz == FaseVO.class){
            return mapearFasesVO(rs);
        }
        if(clazz == FilaNotificacionVO.class){
            return mapearFilaNotificacionVO(rs);
        }
         if(clazz == FilaLlamadasMisGestVO.class){
            return mapearFilaLlamadasMisGestVO(rs);
        }
        
        return null;
    }
    
    public ProcedimientoVO mapearProcedimientosVO(ResultSet rs) throws SQLException
    {
        
        ProcedimientoVO fila = new ProcedimientoVO();
        //fila.setApellidos(null);
        fila.setCodProc(rs.getString("PRO_COD"));
        fila.setDescProc(rs.getString("PRO_COD") + " - " + rs.getString("PRO_DES"));
        
        return fila;
    }
    
    public FilaFaseVO mapearFilaFasesVO(ResultSet rs) throws SQLException
    {
        FilaFaseVO fila = new FilaFaseVO();
        fila.setCodTramExt(rs.getString("COD_TRAM_EXT")); 
        fila.setCodTramite(rs.getString("COD_TRAMITE"));        
        fila.setDescTramite(rs.getString("TRAMITE"));
        fila.setCodFase(rs.getString("COD_FASE"));
        fila.setDescFaseCas(rs.getString("DESC_FASE_C"));
        fila.setDescFaseEus(rs.getString("DESC_FASE_E"));
        
        return fila;
    }

    public TramiteVO mapearTramitesVO(ResultSet rs) throws SQLException
    {
        TramiteVO fila = new TramiteVO();
        fila.setCodTramInterno(rs.getString("COD_TRAM_INTERNO")); 
        fila.setDescTramite(rs.getString("COD_TRAM_INTERNO") + " - " + rs.getString("TRAMITE")); 
        return fila;
    }
    
     public FaseVO mapearFasesVO(ResultSet rs) throws SQLException
    {
        FaseVO fila = new FaseVO();
        fila.setCodProcedimiento(rs.getString("COD_PROC"));        
        fila.setCodTramite(rs.getString("COD_TRAMITE"));              
        fila.setCodFase(rs.getString("COD_FASE"));
        fila.setDescFaseCas(rs.getString("DESC_FASE_C"));
        fila.setDescFaseEus(rs.getString("DESC_FASE_E"));
        
        return fila;
    }

     public FilaNotificacionVO mapearFilaNotificacionVO(ResultSet rs) throws SQLException
    {
        FilaNotificacionVO fila = new FilaNotificacionVO();
        fila.setCodNotif(rs.getInt("COD_NOTIF"));
        fila.setNumExped(rs.getString("NUM_EXPED"));
        fila.setCodProced(rs.getString("COD_PROC"));
        fila.setEjerc(rs.getInt("EJERC"));
        fila.setCodMunic(rs.getInt("COD_MUNIC"));
        fila.setCodTram(rs.getInt("COD_TRAM"));
        fila.setTramite(rs.getInt("COD_TRAM") + " - " + rs.getString("DES_TRAM"));
        fila.setOcuTram(rs.getInt("OCU_TRAM"));
        fila.setActNotif(rs.getString("ACT_NOTIF")); 
        fila.setCadNotif(rs.getInt("CAD_NOTIF"));
        fila.setTxtNotif(rs.getString("TXT_NOTIF"));
        fila.setFirmada(rs.getString("FIRMADA"));
        fila.setFecEnvio(rs.getString("FEC_ENVIO"));
        fila.setCodNotifPlatea(rs.getString("COD_NOTIF_PLATEA"));
        fila.setFecSolEnvio(rs.getString("FEC_SOL_ENVIO"));
        fila.setNumIntent(rs.getInt("NUM_INTENT"));
        fila.setRespLlamada(rs.getString("RESP_LLAMADA"));
        fila.setResultado(rs.getString("RESULTADO"));
        fila.setFecAcuse(rs.getString("FEC_ACUSE"));        
        return fila;
    }
    private Object mapearTml(ResultSet rs) throws SQLException {
        Tml t=new Tml();
        t.setTRA_COU(rs.getInt("TRA_COU"));
        t.setTRA_COD(rs.getInt("TRA_COD"));
        t.setTML_VALOR(rs.getString("TML_VALOR"));
        t.setTRA_PRO(rs.getString("TRA_PRO"));
        return t;
    }
    
    public FilaLlamadasMisGestVO mapearFilaLlamadasMisGestVO(ResultSet rs) throws SQLException
    {
        FilaLlamadasMisGestVO fila = new FilaLlamadasMisGestVO();
        fila.setId(rs.getInt("ID"));
        fila.setNumExped(rs.getString("NUM_EXPED"));
        fila.setCodTramiteInicio(rs.getString("COD_TRAMITE_INICIO"));
        fila.setExpTipo(rs.getString("EXP_TIPO"));
        fila.setFechaGenerado(rs.getString("FECHA_GENERADO"));
        fila.setFechaProceso(rs.getString("FECHA_PROCESADO"));
        fila.setFechaTelematico(rs.getString("FECHA_TELEMATICO"));
        fila.setNumIntent(rs.getInt("NUM_INTENT"));
        fila.setRegTelematico(rs.getString("REG_TELEMATICO")); 
        fila.setResEje(rs.getInt("RES_EJE"));
        fila.setResNum(rs.getInt("RES_NUM"));
       // fila.setRespuestaLlamada(rs.getString("RESPUESTA_LLAMADA"));
        //fila.setResultadoProceso(rs.getString("RESULTADO_PROCESO"));
        fila.setTerDoc(rs.getString("TER_DOC"));
        fila.setTerTid(rs.getInt("TER_TID"));
        fila.setTipoOperacion(rs.getString("TIPO_OPERACION"));
            
        return fila;
    }
}
