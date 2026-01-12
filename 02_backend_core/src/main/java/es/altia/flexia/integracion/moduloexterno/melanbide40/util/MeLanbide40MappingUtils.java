/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide40.util;

import es.altia.flexia.integracion.moduloexterno.melanbide40.vo.S75PagosVO;
import java.sql.ResultSet;

/**
 *
 * @author santiagoc
 */
public class MeLanbide40MappingUtils 
{
    private static MeLanbide40MappingUtils instance = null;
    
    private MeLanbide40MappingUtils()
    {
        
    }
    
    public static MeLanbide40MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide40MappingUtils.class)
            {
                instance = new MeLanbide40MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == S75PagosVO.class)
        {
            return mapearS75PagosVO(rs);
        }
        return null;
    }
    
    private S75PagosVO mapearS75PagosVO(ResultSet rs) throws Exception
    {
        S75PagosVO pago = new S75PagosVO();
        pago.setPagAnoPago(rs.getString("PAG_ANOPAGO"));
        pago.setPagConcep(rs.getString("PAG_CONCEP"));
        pago.setPagEje(rs.getLong("PAG_EJE"));
        pago.setPagFecpag(rs.getDate("PAG_FECPAG"));
        pago.setPagFecvcto(rs.getDate("PAG_FECVCTO"));
        pago.setPagImpanu(rs.getBigDecimal("PAG_IMPANU"));
        pago.setPagImpcon(rs.getBigDecimal("PAG_IMPCON"));
        pago.setPagImppag(rs.getBigDecimal("PAG_IMPPAG"));
        pago.setPagImprec(rs.getBigDecimal("PAG_IMPREC"));
        pago.setPagImprei(rs.getBigDecimal("PAG_IMPREI"));
        pago.setPagMun(rs.getLong("PAG_MUN"));
        pago.setPagNum(rs.getString("PAG_NUM"));
        pago.setPagNumpago(rs.getLong("PAG_NUMPAGO"));
        pago.setPagPro(rs.getString("PAG_PRO"));
        return pago;
    }
}
