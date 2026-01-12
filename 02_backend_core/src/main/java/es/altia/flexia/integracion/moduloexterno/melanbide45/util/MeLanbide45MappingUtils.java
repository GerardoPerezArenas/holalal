/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide45.util;

import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.espacioform.EspacioFormVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.materialconsu.MaterialConsuVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.moduloform.ModuloFormVO;
import java.sql.ResultSet;

/**
 *
 * @author davidg
 */
public class MeLanbide45MappingUtils {
    
    private static MeLanbide45MappingUtils instance = null;
    
    private MeLanbide45MappingUtils()
    {
        
    }
    
    public static MeLanbide45MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide45MappingUtils.class)
            {
                instance = new MeLanbide45MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == ModuloFormVO.class)
        {
            return mapearModuloFormVO(rs);
        }
        else if(clazz == EspacioFormVO.class)
        {
            return mapearEspacioFormVO(rs);
        }
        else if(clazz == MaterialConsuVO.class)
        {
            return mapearMaterialConsuVO(rs);
        }
        return null;
    }
    
    private ModuloFormVO mapearModuloFormVO(ResultSet rs) throws Exception
    {
        ModuloFormVO moduloForm = new ModuloFormVO();
        moduloForm.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            moduloForm.setId(null);
        }
        moduloForm.setNumExp(rs.getString("MDF_NUM"));
        moduloForm.setDenominacion(rs.getString("MDF_DEN"));
        if(rs.getString("MDF_DUR") != null)
            moduloForm.setDuracion(rs.getBigDecimal("MDF_DUR"));
        moduloForm.setObjetivo(rs.getString("MDF_OBJ"));
        moduloForm.setContenidoTP(rs.getString("MDF_CT"));
        
        moduloForm.setCodMod(rs.getString("MDF_COD"));
        moduloForm.setCodUC(rs.getString("MDF_UC_COD"));
        moduloForm.setDesUC(rs.getString("MDF_UC_DEN"));
        if(rs.getString("MDF_DUR_MAX_TEL") != null)
            moduloForm.setDuracMax(rs.getBigDecimal("MDF_DUR_MAX_TEL"));
        
        moduloForm.setUc_nivel(rs.getString("MDF_UC_NIVEL"));
        moduloForm.setUc_existe(rs.getString("MDF_UC_EXISTENTE"));
        moduloForm.setNivel(rs.getString("MDF_NIVEL"));
        moduloForm.setExiste(rs.getString("MDF_EXISTENTE"));
        
        return moduloForm;
    }
    
    private MaterialConsuVO mapearMaterialConsuVO(ResultSet rs) throws Exception
    {
        MaterialConsuVO moduloForm = new MaterialConsuVO();
        moduloForm.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            moduloForm.setId(null);
        }
        moduloForm.setNumExp(rs.getString("MAC_NUM"));
        //moduloForm.getCodigo(rs.getString("MAC_CANT"));
        moduloForm.setCantidad(rs.getInt("MAC_CANT"));
        moduloForm.setDescripcion(rs.getString("MAC_DET"));
        
        return moduloForm;
    }
    
    private EspacioFormVO mapearEspacioFormVO(ResultSet rs) throws Exception
    {
        EspacioFormVO espacioForm = new EspacioFormVO();
        espacioForm.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            espacioForm.setId(null);
        }
        espacioForm.setNumExp(rs.getString("EPF_NUM"));
        espacioForm.setDescripcion(rs.getString("EPF_DES"));
        espacioForm.setSuperficie(rs.getBigDecimal("EPF_SUP"));
               
        return espacioForm;
    }
    
}
