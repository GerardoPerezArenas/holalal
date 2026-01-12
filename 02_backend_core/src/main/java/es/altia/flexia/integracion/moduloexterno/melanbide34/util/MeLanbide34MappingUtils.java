/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide34.util;

import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.DatosAviso;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.S75PagosVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 *
 * @author santiagoc
 */
public class MeLanbide34MappingUtils 
{
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    private static MeLanbide34MappingUtils instance = null;
    
    private MeLanbide34MappingUtils()
    {
        
    }
    
    public static MeLanbide34MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide34MappingUtils.class)
            {
                instance = new MeLanbide34MappingUtils();
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
        if(clazz == DatosAviso.class)
        {
            return mapearDatosAviso(rs);
        }
        if (clazz == FilaMinimisVO.class) {
            return mapearMinimisVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        }

        return null;
    }  
    
    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }
    
    private Object mapearMinimisVO(ResultSet rs) throws SQLException {
        FilaMinimisVO minimis = new FilaMinimisVO();

        minimis.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            minimis.setId(null);
        }

        minimis.setNumExp(rs.getString("NUM_EXP"));

        minimis.setEstado(rs.getString("ESTADO"));
        minimis.setOrganismo(rs.getString("ORGANISMO"));
        minimis.setObjeto(rs.getString("OBJETO"));
        Double aux1 = new Double(rs.getDouble("IMPORTE"));
        if (aux1 != 0) {
            minimis.setImporte(aux1.doubleValue());
        }
        if (rs.getDate("FECHA") != null) {
            minimis.setFecha(rs.getDate("FECHA"));
            minimis.setFechaStr(dateFormat.format(rs.getDate("FECHA")));
        }

        return minimis;
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
    
    private DatosAviso mapearDatosAviso(ResultSet rs) throws Exception
    {
        DatosAviso datos = new DatosAviso();
         datos.setNumExp(rs.getString("CRO_NUM"));
         datos.setFecEstudio(rs.getDate("FECESTUDIO"));
         datos.setFecSolicitud(rs.getDate("FECSOLICITUD"));
        
        return datos;
    }
}
