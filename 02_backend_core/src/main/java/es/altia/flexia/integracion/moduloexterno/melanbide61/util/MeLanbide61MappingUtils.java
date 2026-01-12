/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide61.util;

import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.FilaDocumentoContableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide61.vo.SubSolicVO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class MeLanbide61MappingUtils 
{
    
    private static MeLanbide61MappingUtils instance = null;
    
    private MeLanbide61MappingUtils()
    {
        
    }
    
    public static MeLanbide61MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide61MappingUtils.class)
            {
                instance = new MeLanbide61MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == FilaDocumentoContableVO.class)
        {
            return mapearFilaDocumentoContableVO(rs);
            
        } else if (clazz == DatosEconomicosExpVO.class) {
                return mapearDatosEconomicosVO(rs);
        } else if (clazz == SubSolicVO.class) {
            return mapeaSubSolicVO(rs);
        } else if (clazz == DesplegableVO.class) {
            return mapearDesplegableVO(rs);
        }
        return null;
    }
    
    private FilaDocumentoContableVO mapearFilaDocumentoContableVO(ResultSet rs) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide61.FORMATO_FECHA);
        String valorStr = null;
        Date valorFecha = null;
        FilaDocumentoContableVO fila = new FilaDocumentoContableVO();
        valorStr = rs.getString("IMPORTE");
        fila.setImporte(valorStr != null && !valorStr.equals("") ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("NUMERO_APUNTE");
        fila.setNumApunte(valorStr != null && !valorStr.equals("") ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("NUMERO_MAQUETA");
        fila.setNumMaqueta(valorStr != null && !valorStr.equals("") ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("REFERENCIA");
        fila.setRefIntervencion(valorStr != null && !valorStr.equals("") ? valorStr.toUpperCase() : "-");
        valorStr = rs.getString("TIPO_DOC");
        fila.setTipoDoc(valorStr != null && !valorStr.equals("") ? valorStr.toUpperCase() : "-");
        valorFecha = rs.getDate("FECHA_CONTAB");
        fila.setfContab(valorFecha != null ? format.format(valorFecha) : "-");
        valorFecha = rs.getDate("FECHA_DOCUMENTO");
        fila.setfDocumento(valorFecha != null ? format.format(valorFecha) : "-");
        valorFecha = rs.getDate("FECHA_MAQUETA");
        fila.setfMaqueta(valorFecha != null ? format.format(valorFecha) : "-");
        valorFecha = rs.getDate("FECHA_TRAMITACION");
        fila.setfTramitacion(valorFecha != null ? format.format(valorFecha) : "-");
        return fila;
    }
        private Object mapearDatosEconomicosVO(ResultSet rs) throws SQLException {
        DatosEconomicosExpVO datosEcon=new DatosEconomicosExpVO();
        datosEcon.setImporteSubvencion(rs.getDouble("IMPORTE"));
        datosEcon.setPorcentajePrimerPago(rs.getDouble("PORCENTAJE"));
        
        return datosEcon;
    }
         private Object mapeaSubSolicVO(ResultSet rs) throws SQLException {
        SubSolicVO subSolic = new SubSolicVO();
        subSolic.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            subSolic.setId(null);
        }
        subSolic.setNumExp(rs.getString("NUM_EXP"));
        subSolic.setEstado(rs.getString("ESTADO"));
        subSolic.setOrganismo(rs.getString("ORGANISMO"));
        subSolic.setObjeto(rs.getString("OBJETO"));
        Double aux1 = rs.getDouble("IMPORTE");
        if (aux1 != 0) {
            subSolic.setImporte(aux1);
        }
        if (rs.getDate("FECHA") != null) {
            subSolic.setFecha(rs.getDate("FECHA"));
        }

        return subSolic;
    }
        private Object mapearDesplegableVO(ResultSet rs) throws SQLException {
        DesplegableVO valoresDesplegable = new DesplegableVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }
}
