/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide41.util;

import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.capacidad.CapacidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.disponibilidad.DisponibilidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.dotacion.DotacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.identificacionesp.IdentificacionEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.material.MaterialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide41.vo.servicios.ServiciosVO;
import java.sql.ResultSet;

/**
 *
 * @author davidg
 */
public class MeLanbide41MappingUtils {
    
      private static MeLanbide41MappingUtils instance = null;
    
    private MeLanbide41MappingUtils()
    {
        
    }
    
    public static MeLanbide41MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide41MappingUtils.class)
            {
                instance = new MeLanbide41MappingUtils();
            }
        }
        return instance;
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == EspecialidadesVO.class)
        {
            return mapearEspecialidadesVO(rs);
        }
        else if(clazz == ServiciosVO.class)
        {
            return mapearServiciosVO(rs);
        }
        else if(clazz == DisponibilidadVO.class)
        {
            return mapearDisponibilidadVO(rs);
        }
        else if(clazz == CapacidadVO.class)
        {
            return mapearCapacidadVO(rs);
        }
        else if(clazz == DotacionVO.class)
        {
            return mapearDotacionVO(rs);
        }
        else if(clazz == MaterialVO.class)
        {
            return mapearMaterialVO(rs);
        }
        else if(clazz == CerCertificadoVO.class)
        {
            return mapearCerCertificadoVO(rs);
        }
        else if(clazz == IdentificacionEspVO.class)
        {
            return mapearIdentificacionEspVO(rs);
        }
            
                    
        return null;
    }
    
    private EspecialidadesVO mapearEspecialidadesVO(ResultSet rs) throws Exception
    {
        EspecialidadesVO especialidad = new EspecialidadesVO();
        especialidad.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            especialidad.setId(null);
        }
        especialidad.setNumExp(rs.getString("ESP_NUM"));
        especialidad.setCodCP(rs.getString("ESP_CODCP"));
        especialidad.setDenominacion(rs.getString("ESP_DENOM"));
        especialidad.setInscripcionPresencial(rs.getInt("ESP_PRESE"));
        if(rs.wasNull())
        {
            especialidad.setInscripcionPresencial(null);
        }
        especialidad.setInscripcionTeleformacion(rs.getInt("ESP_TELEF"));
        if(rs.wasNull())
        {
            especialidad.setInscripcionTeleformacion(null);
        }
        especialidad.setAcreditacion(rs.getInt("ESP_ACRED"));
        if(rs.wasNull())
        {
            especialidad.setAcreditacion(null);
        }
        especialidad.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            especialidad.setId(null);
        }
        
        return especialidad;
    }
    
     private ServiciosVO mapearServiciosVO(ResultSet rs) throws Exception
    {
        ServiciosVO servicio = new ServiciosVO();
        servicio.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            servicio.setId(null);
        }
        servicio.setNumExp(rs.getString("SER_NUM"));
        servicio.setDescripcion(rs.getString("SER_DESC"));
        servicio.setUbicacion(rs.getString("SER_UBIC"));
        servicio.setSuperficie(rs.getBigDecimal("SER_SUPE"));

        return servicio;
    }
     
    private DisponibilidadVO mapearDisponibilidadVO(ResultSet rs) throws Exception
    {
        DisponibilidadVO disponibilidad = new DisponibilidadVO();
        disponibilidad.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            disponibilidad.setId(null);
        }
        disponibilidad.setIdEspSol(rs.getInt("ID_ESPSOL"));
        if(rs.wasNull())
        {
            disponibilidad.setIdEspSol(null);
        }
        disponibilidad.setNumExp(rs.getString("DRE_NUM"));
        disponibilidad.setCodCp(rs.getString("DRE_CODCP"));
        disponibilidad.setPropiedadCedidos(rs.getString("DRE_PRCE"));
        disponibilidad.setSituados(rs.getString("DRE_SIT"));
        disponibilidad.setSupAulas(rs.getString("DRE_AUL"));
        disponibilidad.setSupTaller(rs.getString("DRE_TAL"));
        disponibilidad.setSupAulaTaller(rs.getString("DRE_AUTA"));
        disponibilidad.setSupCampoPract(rs.getString("DRE_CAPR"));
       
        return disponibilidad;
    }
    
    private CapacidadVO mapearCapacidadVO(ResultSet rs) throws Exception
    {
        CapacidadVO capacidad = new CapacidadVO();
        capacidad.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            capacidad.setId(null);
        }
        capacidad.setIdEspSol(rs.getInt("ID_ESPSOL"));
        if(rs.wasNull())
        {
            capacidad.setIdEspSol(null);
        }
        capacidad.setNumExp(rs.getString("CAIN_NUM"));
        capacidad.setIdetificacionEspFor(rs.getString("CAIN_IDEF"));
        capacidad.setUbicacion(rs.getString("CAIN_UBI"));
        capacidad.setSuperficie(rs.getString("CAIN_SUP"));
        return capacidad;
    }
    
    private DotacionVO mapearDotacionVO(ResultSet rs) throws Exception
    {
        DotacionVO dotacion = new DotacionVO();
        dotacion.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            dotacion.setId(null);
        }
        dotacion.setIdEspSol(rs.getInt("ID_ESPSOL"));
        if(rs.wasNull())
        {
            dotacion.setIdEspSol(null);
        }
        dotacion.setNumExp(rs.getString("DOT_NUM"));
        //dotacion.setCantidad(rs.getInt("DOT_CANT"));
        dotacion.setCantidad(rs.getString("DOT_CANT"));
        dotacion.setDenominacionET(rs.getString("DOT_DET"));
        dotacion.setFechaAdq(rs.getString("DOT_FAD"));
        return dotacion;
    }
    
    private MaterialVO mapearMaterialVO(ResultSet rs) throws Exception
    {
        MaterialVO material = new MaterialVO();
        material.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            material.setId(null);
        }
        material.setIdEspSol(rs.getInt("ID_ESPSOL"));
        if(rs.wasNull())
        {
            material.setIdEspSol(null);
        }
        material.setNumExp(rs.getString("MAC_NUM"));
        material.setCantidad(rs.getInt("MAC_CANT"));
        material.setDenominacionET(rs.getString("MAC_DET"));
        return material;
    }
    
    private CerCertificadoVO mapearCerCertificadoVO(ResultSet rs) throws Exception
    {
        CerCertificadoVO certificado = new CerCertificadoVO();
        
        certificado.setCodCertificado(rs.getString("CODESPECIALIDAD"));
        certificado.setDescCertificadoC(rs.getString("DESC_ESPECIALIDAD_C"));
        certificado.setDescCertificadoE(rs.getString("DESC_ESPECIALIDAD_E"));
        
        return certificado;
    }
    
     private IdentificacionEspVO mapearIdentificacionEspVO(ResultSet rs) throws Exception
    {
        IdentificacionEspVO identificacionEsp = new IdentificacionEspVO();
        identificacionEsp.setId(rs.getInt("ID"));
        if(rs.wasNull())
        {
            identificacionEsp.setId(null);
        }
        identificacionEsp.setIdEspSol(rs.getInt("ID_ESPSOL"));
        if(rs.wasNull())
        {
            identificacionEsp.setIdEspSol(null);
        }
        identificacionEsp.setNumExp(rs.getString("IDE_NUM"));
        identificacionEsp.setCodCp(rs.getString("IDE_CODESP"));
        identificacionEsp.setDenomEsp(rs.getString("IDE_DENESP"));
        identificacionEsp.setHoras(rs.getBigDecimal("IDE_HORAS"));
        identificacionEsp.setAlumnos(rs.getBigDecimal("IDE_ALUM"));
        if(rs.getString("IDE_ALUM_AUT") != null)
            identificacionEsp.setAlumnosAut(rs.getBigDecimal("IDE_ALUM_AUT"));
        else 
            identificacionEsp.setAlumnosAut(rs.getBigDecimal("IDE_ALUM"));
        identificacionEsp.setCertPro(rs.getInt("IDE_CERTP"));
        if(rs.wasNull())
        {
            identificacionEsp.setCertPro(null);
        }
        identificacionEsp.setRealDecRegu(rs.getString("IDE_RDER"));
        identificacionEsp.setBoeFecPub(rs.getString("IDE_BOEFP"));
        identificacionEsp.setDescripAdapt(rs.getString("IDE_DESADAP"));
        identificacionEsp.setObservAdapt(rs.getString("IDE_OBSADAP"));
       
        return identificacionEsp;
    }
    
    
}
