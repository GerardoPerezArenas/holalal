/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide36.util;

import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.FilaExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.InfoContactoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.TerceroVO;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class MeLanbide36MappingUtils 
{
    private static MeLanbide36MappingUtils instance = null;
    
    private MeLanbide36MappingUtils()
    {
        
    }
    
    public static MeLanbide36MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide36MappingUtils.class)
            {
                instance = new MeLanbide36MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == FilaExpedienteVO.class)
        {
            return mapearFilaExpedienteVO(rs);
        }
        return null;
    }    
    
    private FilaExpedienteVO mapearFilaExpedienteVO(ResultSet rs) throws Exception
    {
        FilaExpedienteVO fe = new FilaExpedienteVO();
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide36.FORMATO_FECHA);
        String strValor = null;
        Integer intValor = null;
        Double dValor = null;
        Date dateValor = null;
        strValor = rs.getString("NUMEXP_P29");
        fe.setNumExpedienteP29(strValor != null && !strValor.equals("") ? strValor.toUpperCase() : "-");
        strValor = rs.getString("NUM_EXP");
        fe.setNumExpediente(strValor != null && !strValor.equals("") ? strValor.toUpperCase() : "-");
        strValor = rs.getString("TIPACTUSUB");
        fe.setTipActUsub(strValor != null && !strValor.equals("") ? strValor.toUpperCase() : "-");
        strValor = rs.getString("DESTIPACTUSUB");
        fe.setDesTipActUsub(strValor != null && !strValor.equals("") ? strValor.toUpperCase() : "-");
        intValor = rs.getInt("NUMDIAS");
        if(rs.wasNull())
        {
            intValor = 0;
        }
        fe.setNumDias(intValor);
        dValor = rs.getDouble("TOTALABONADO");
        if(rs.wasNull())
        {
            dValor = 0.0;
        }
        fe.setImporteAbonado(dValor);
        strValor = rs.getString("SITUDENECONC");
        fe.setSituacion(strValor != null && !strValor.equals("") ? strValor.toUpperCase() : "-");
        strValor = rs.getString("DESTIPACTUSUB");
        fe.setTipoSubvencion(strValor != null && !strValor.equals("") ? strValor.toUpperCase() : "-");
        dateValor = rs.getDate("FECINIACTCON");
        fe.setFeIni(dateValor != null ? format.format(dateValor) : "-");
        dateValor = rs.getDate("FECFINACTCON");
        fe.setFeFin(dateValor != null ? format.format(dateValor) : "-");
        dateValor = rs.getDate("FECNAPDEPEN");
        fe.setFecNacPersDepen(dateValor != null ? format.format(dateValor) : "-");
        return fe;
    }
    
//    private InfoContactoVO mapearInfoContactoVO(ResultSet rs) throws Exception
//    {
//        InfoContactoVO infContact = new InfoContactoVO();
//
//        infContact.setCodTercero(rs.getString("EXT_TER"));
//        infContact.setVerTercero(rs.getInt("EXT_NVR"));
//
//        return infContact;
//    }
    
//    private TerceroVO mapearTerceroVO(ResultSet rs) throws Exception
//    {
//        TerceroVO tercero = new TerceroVO();
//
//        tercero.setCodTercero(rs.getString("TER_COD"));  
//        tercero.setVerTercero(rs.getInt("TER_NVE"));  
//        tercero.setCodDomicilio(rs.getString("TER_DOM"));
//
//        return tercero;
//    }
}
