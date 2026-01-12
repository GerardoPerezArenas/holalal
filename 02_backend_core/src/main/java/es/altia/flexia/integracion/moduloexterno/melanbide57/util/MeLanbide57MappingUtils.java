package es.altia.flexia.integracion.moduloexterno.melanbide57.util;


import es.altia.flexia.integracion.moduloexterno.melanbide57.manager.util.ConstantesMelanbide57;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeGeneralVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeInternoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.InformeRGIVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.NoCatalogadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide57.vo.TramiteVO;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

/**
 *
 * @author altia
 */
public class MeLanbide57MappingUtils {
    
      private static MeLanbide57MappingUtils instance = null;
    
    private MeLanbide57MappingUtils()
    {
        
    }
    
    public static MeLanbide57MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide57MappingUtils.class)
            {
                instance = new MeLanbide57MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == TramiteVO.class)
        {
            return mapearTramiteVO(rs);
        }else if(clazz == InformeInternoVO.class) {
            return mapearInformeInternoVO(rs);
        }else if(clazz == NoCatalogadoVO.class) {
            return mapearNoCatalogadoVO(rs);
        }else if(clazz == InformeGeneralVO.class) {
            return mapearInformeGeneralVO(rs);
        }else if(clazz == InformeRGIVO.class) {
            return mapearInformeRGIVO(rs);
        } 
                    
        return null;
    }
    
    private InformeRGIVO mapearInformeRGIVO(ResultSet rs) throws Exception
    {
        InformeRGIVO informeRGI = new InformeRGIVO();
        informeRGI.setAsunto(rs.getString("EXP_ASU"));
        if(rs.wasNull())
        {
            informeRGI.setAsunto(null);
        }
        informeRGI.setNumExpediente(rs.getString("EXP_NUM"));
        informeRGI.setProcedimiento(rs.getString("PRO_DES"));
        
        informeRGI.setFechaInicio(rs.getDate("EXP_FEI"));
        if(rs.wasNull())
        {
            informeRGI.setFechaInicio(null);
        }
        informeRGI.setFechaFin(rs.getDate("EXP_FEF"));
        if(rs.wasNull())
        {
            informeRGI.setFechaFin(null);
        }
        informeRGI.setInteresadoAp1(rs.getString("HTE_AP1"));
        if(rs.wasNull())
        {
            informeRGI.setInteresadoAp1("");
        }
        informeRGI.setInteresadoAp2(rs.getString("HTE_AP2"));
        if(rs.wasNull())
        {
            informeRGI.setInteresadoAp2("");
        }
        informeRGI.setInteresadoNombre(rs.getString("HTE_NOM"));
        if(rs.wasNull())
        {
            informeRGI.setInteresadoNombre("");
        }
        informeRGI.setInteresadoNif(rs.getString("HTE_DOC"));
        if(rs.wasNull())
        {
            informeRGI.setInteresadoNif("");
        }
        informeRGI.setUsuInicio(rs.getString("USU_NOM"));
        if(rs.wasNull())
        {
            informeRGI.setUsuInicio("");
        }
        
        
        return informeRGI;
    }
      private NoCatalogadoVO mapearNoCatalogadoVO(ResultSet rs) throws Exception
    {
        NoCatalogadoVO noCatalogadoVO = new NoCatalogadoVO();
        noCatalogadoVO.setRegistroEjercicio(rs.getString("REGISTRO_EJERCICIO"));
        noCatalogadoVO.setRegistroNumero(rs.getString("REGISTRO_NUMERO"));
        noCatalogadoVO.setOidDokusi(rs.getString("OID_DOKUSI"));
        
        
        
        return noCatalogadoVO;
    }
    private TramiteVO mapearTramiteVO(ResultSet rs) throws Exception
    {
        TramiteVO tramite = new TramiteVO();
        tramite.setTramiteNombre(rs.getString("TML_VALOR"));
        if(rs.wasNull())
        {
            tramite.setTramiteNombre(null);
        }
        tramite.setTramiteNombreArea(rs.getString("UOR_NOM"));
        if(rs.wasNull())
        {
            tramite.setTramiteNombreArea(null);
        }
        return tramite;
    }
    
    private InformeInternoVO mapearInformeInternoVO(ResultSet rs) throws Exception
    {
        InformeInternoVO informeInterno = new InformeInternoVO();
        informeInterno.setUoNombre(rs.getString("UOR_NOM"));
        if(rs.wasNull())
        {
            informeInterno.setUoNombre(null);
        }
        informeInterno.setUoCodigo(rs.getString("UOR_COD"));
        if(rs.wasNull())
        {
            informeInterno.setUoCodigo(null);
        }
        informeInterno.setTotalExpedientes(rs.getString("totalExpedientes"));
        if(rs.wasNull())
        {
            informeInterno.setTotalExpedientes(null);
        }
        informeInterno.setMes(rs.getString("mes"));
        if(rs.wasNull())
        {
            informeInterno.setMes(null);
        }
        informeInterno.setAnyo(rs.getString("anyo"));
        if(rs.wasNull())
        {
            informeInterno.setAnyo(null);
        }
        return informeInterno;
    }
    
    private InformeGeneralVO mapearInformeGeneralVO(ResultSet rs) throws Exception
    {
        InformeGeneralVO informeGeneral = new InformeGeneralVO();
        informeGeneral.setDescripcionCampo(rs.getString("descripcion"));
        if(rs.wasNull())
        {
            informeGeneral.setDescripcionCampo(null);
        }
        informeGeneral.setTotalExpedientes(rs.getString("totalExpedientes"));
        if(rs.wasNull())
        {
            informeGeneral.setTotalExpedientes(null);
        }
        informeGeneral.setMes(rs.getString("mes"));
        if(rs.wasNull())
        {
            informeGeneral.setMes(null);
        }
        informeGeneral.setAnyo(rs.getString("anyo"));
        if(rs.wasNull())
        {
            informeGeneral.setAnyo(null);
        }
        return informeGeneral;
    }
        
}
