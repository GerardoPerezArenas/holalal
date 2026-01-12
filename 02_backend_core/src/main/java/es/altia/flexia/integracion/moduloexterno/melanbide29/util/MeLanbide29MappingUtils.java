/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide29.util;

import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.FilaDocumentoContableVO;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author santiagoc
 */
public class MeLanbide29MappingUtils 
{
    
    private static MeLanbide29MappingUtils instance = null;
    
    private MeLanbide29MappingUtils()
    {
        
    }
    
    public static MeLanbide29MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide29MappingUtils.class)
            {
                instance = new MeLanbide29MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == FilaDocumentoContableVO.class)
        {
            return mapearFilaDocumentoContableVO(rs);
        }
        return null;
    }
    
    private FilaDocumentoContableVO mapearFilaDocumentoContableVO(ResultSet rs) throws Exception
    {
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide29.FORMATO_FECHA);
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
}
