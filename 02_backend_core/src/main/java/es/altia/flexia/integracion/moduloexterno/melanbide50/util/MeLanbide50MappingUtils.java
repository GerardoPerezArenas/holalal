/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide50.util;

import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Domicilio;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.alta.Tercero;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.capacidad.CapacidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.disponibilidad.DisponibilidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.documentos.DocumentosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.dotacion.DotacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.espacios.EspacioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.CerCertificadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.identificacionesp.IdentificacionEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.material.MaterialVO;
import es.altia.flexia.integracion.moduloexterno.melanbide50.vo.servicios.ServiciosVO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author davidg
 */
public class MeLanbide50MappingUtils {
    
      private static MeLanbide50MappingUtils instance = null;
    
    public static MeLanbide50MappingUtils getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide50MappingUtils.class)
            {
                instance = new MeLanbide50MappingUtils();
            }
        }
        return instance;
    }
    
    private MeLanbide50MappingUtils()
    {
        
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
        else if(clazz == Tercero.class)
        {
            return mapearTercero(rs);
        }
        else if(clazz == Domicilio.class)
        {
            return mapearDomicilio(rs);
        }
        else if(clazz == Expediente.class)
        {
            return mapearExpediente(rs);
        }
        else if(clazz == DocumentosVO.class)
        {
            return mapearDocumentos(rs);
        }
         else if(clazz == EspacioVO.class)
        {
            return mapearEspacios(rs);
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
        //capacidad.setSuperficie(rs.getBigDecimal("CAIN_SUP"));
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
    
     private Tercero mapearTercero(ResultSet rs) throws Exception
    {
        Tercero ter = new Tercero();
        ter.setExternalCode(rs.getString("EXTERNAL_CODE"));
        ter.setTerAp1(rs.getString("AP1"));
        ter.setTerAp2(rs.getString("AP2"));
        ter.setTerApl(rs.getBigDecimal("APL"));
        ter.setTerCod(rs.getLong("COD"));
        if(rs.wasNull())
        {
            ter.setTerCod(null);
        }
        ter.setTerDce(rs.getString("DCE"));
        ter.setTerDoc(rs.getString("DOC"));
        ter.setTerDom(rs.getBigDecimal("DOM"));
        ter.setTerFal(rs.getDate("FAL"));
        ter.setTerFbj(rs.getDate("FBJ"));
        ter.setTerNml(rs.getBigDecimal("NML"));
        ter.setTerNoc(rs.getString("NOC"));
        ter.setTerNom(rs.getString("NOM"));
        ter.setTerNve(rs.getBigDecimal("VER"));
        ter.setTerPa1(rs.getString("PA1"));
        ter.setTerPa2(rs.getString("PA2"));
        ter.setTerSit(rs.getString("SIT"));
        ter.setTerTid(rs.getBigDecimal("TID"));
        ter.setTerTlf(rs.getString("TLF"));
        ter.setTerUal(rs.getBigDecimal("UAL"));
        ter.setTerUbj(rs.getBigDecimal("UBJ"));
        ter.setExtRol(rs.getString("ROL"));
        return ter;
    }
    
    private Domicilio mapearDomicilio(ResultSet rs) throws Exception
    {
        Domicilio dom = new Domicilio();
        dom.setBlq(rs.getString("DNN_BLQ"));
        dom.setCpo(rs.getString("DNN_CPO"));
        
        dom.setDmc(rs.getString("DNN_DMC"));
        dom.setDom(rs.getBigDecimal("DNN_DOM"));
        dom.setEsc(rs.getString("DNN_ESC"));
        dom.setEsi(rs.getInt("DNN_ESI"));
        if(rs.wasNull())
        {
            dom.setEsi(null);
        }
        dom.setFal(rs.getString("DNN_FAL"));
        dom.setFbj(rs.getString("DNN_FBJ"));
        dom.setLed(rs.getString("DNN_LED"));
        dom.setLeh(rs.getString("DNN_LEH"));
        dom.setLug(rs.getString("DNN_LUG"));
        dom.setMun(rs.getInt("DNN_MUN"));
        if(rs.wasNull())
        {
            dom.setMun(null);
        }
        dom.setNud(rs.getInt("DNN_NUD"));
        if(rs.wasNull())
        {
            dom.setNud(null);
        }
        dom.setNuh(rs.getInt("DNN_NUH"));
        if(rs.wasNull())
        {
            dom.setNuh(null);
        }
        dom.setPai(rs.getInt("DNN_PAI"));
        if(rs.wasNull())
        {
            dom.setPai(null);
        }
        dom.setPlt(rs.getString("DNN_PLT"));
        dom.setPor(rs.getString("DNN_POR"));
        dom.setPrv(rs.getInt("DNN_PRV"));
        if(rs.wasNull())
        {
            dom.setPrv(null);
        }
        dom.setPta(rs.getString("DNN_PTA"));
        dom.setRca(rs.getString("DNN_RCA"));
        dom.setSit(rs.getString("DNN_SIT"));
        dom.setSmu(rs.getInt("DNN_SMU"));
        if(rs.wasNull())
        {
            dom.setSmu(null);
        }
        dom.setSpa(rs.getInt("DNN_SPA"));
        if(rs.wasNull())
        {
            dom.setSpa(null);
        }
        dom.setSpr(rs.getInt("DNN_SPR"));
        if(rs.wasNull())
        {
            dom.setSpr(null);
        }
        dom.setTvi(rs.getInt("DNN_TVI"));
        if(rs.wasNull())
        {
            dom.setTvi(null);
        }
        dom.setUal(rs.getString("DNN_UAL"));
        dom.setUbj(rs.getString("DNN_UBJ"));
        dom.setVia(rs.getLong("DNN_VIA"));
        if(rs.wasNull())
        {
            dom.setVia(null);
        }
        dom.setNomVia(rs.getString("VIA_NOM"));
        dom.setVmu(rs.getInt("DNN_VMU"));
        if(rs.wasNull())
        {
            dom.setVmu(null);
        }
        dom.setVpa(rs.getInt("DNN_VPA"));
        if(rs.wasNull())
        {
            dom.setVpa(null);
        }
        dom.setVpr(rs.getInt("DNN_VPR"));
        if(rs.wasNull())
        {
            dom.setVpr(null);
        }
        return dom;
    }
    
    private Expediente mapearExpediente(ResultSet rs) throws SQLException
    {
        Expediente exp = new Expediente();
        exp.setExpAsu(rs.getString("ASU"));
        exp.setExpEje(rs.getInt("EJE"));
        if(rs.wasNull())
            exp.setExpEje(null);
        exp.setExpMun(rs.getInt("MUN"));
        if(rs.wasNull())
            exp.setExpMun(null);
        exp.setExpNum(rs.getString("NUM"));
        exp.setExpObs(rs.getString("OBS"));
        exp.setExpPro(rs.getString("PRO"));
        exp.setUorRegistro(rs.getInt("UOR"));
        if(rs.wasNull())
            exp.setUorRegistro(null);
        exp.setExrDep(rs.getInt("DEP"));
        if(rs.wasNull())
            exp.setExrDep(null);
        exp.setResTdo(rs.getInt("TDO"));
        if(rs.wasNull())
            exp.setResTdo(null);
        exp.setResNtr(rs.getString("NTR"));
        exp.setResAut(rs.getString("AUT"));
        return exp;
    }

    private Object mapearDocumentos(ResultSet rs) throws SQLException {
        DocumentosVO documentos=new DocumentosVO();
        documentos.setTIPO_DOCUMENTO(rs.getString("TIPO_DOCUMENTO"));
        documentos.setNOMBRE_DOCUMENTO(rs.getString("NOMBRE_DOCUMENTO"));
        documentos.setORGANO(rs.getString("ORGANO"));
        documentos.setTIPO_DOCUMENTAL(rs.getString("TIPO_DOCUMENTAL"));
        documentos.setFECHA(rs.getString("FECHA"));
          String[] fecha_fin= documentos.getFECHA().split(" ");
          String fecha=fecha_fin[0];
          String[] fecha_f=fecha.split("-");
          String fecha_finalin=fecha_f[2]+"-"+fecha_f[1]+"-"+fecha_f[0];
          documentos.setFECHA(fecha_finalin);
        
          return documentos;
    }

    private Object mapearEspacios(ResultSet rs) throws SQLException {
        EspacioVO espacio = new EspacioVO();
        espacio.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            espacio.setId(null);
        }
        espacio.setNumExp(rs.getString("NUM_EXP"));
        espacio.setIdEspSol(Integer.valueOf(rs.getString("ID_ESPSOL")));
        if (rs.wasNull()) {
            espacio.setIdEspSol(null);
        }
        espacio.setDenominacion(rs.getString("NESP_DENESP"));
        espacio.setNumAlumnos(rs.getInt("NESP_ALUM"));
        if (rs.wasNull()) {
            espacio.setNumAlumnos(null);
        }
        espacio.setEspAcred(rs.getString("NESP_ESPACRED"));
        espacio.setEspAutor(rs.getString("NESP_ESPAUT"));
        espacio.setAlumNuevos(rs.getInt("NESP_ALUMNUEV"));
        if (rs.wasNull()) {
            espacio.setAlumNuevos(null);
        }
        return espacio;
    }
}
