package es.altia.flexia.integracion.moduloexterno.melanbide72.util;

import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.AlarmaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA1BCVO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA2VO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.RegistroBatchVO;
//import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaAltVO;
//import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeLanbide72MappingUtils {

    private static MeLanbide72MappingUtils instance = null;

    private MeLanbide72MappingUtils() {
    }
    
    public Object map(ResultSet rs, Class clazz) throws Exception
    {
        if(clazz == AlarmaVO.class)
        {
            return mapearAlarmaVO(rs);
        }else
        return null;        
    }
    private AlarmaVO mapearAlarmaVO(ResultSet rs) throws Exception {
        AlarmaVO alarma = new AlarmaVO();
        return alarma;
    }
   

    public static MeLanbide72MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide72MappingUtils.class) {
                instance = new MeLanbide72MappingUtils();
            }
        }
        return instance;
    }
    
    public Object mapA1(ResultSet rs, Class clazz) throws Exception {
        return mapVersA1(rs, clazz, true);
    }

    public Object mapVersA1(ResultSet rs, Class clazz, boolean completo) throws Exception {
        if (clazz == MedidaA1BCVO.class) {
            return mapearMedidaA1VO(rs);
        }

        return null;
    }

    private Object mapearMedidaA1VO(ResultSet rs) throws SQLException {
        MedidaA1BCVO medidaA1 = new MedidaA1BCVO();

        medidaA1.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            medidaA1.setId(null);
        }
        medidaA1.setNumExp(rs.getString("NUM_EXP"));
        
        medidaA1.setNombre(rs.getString("NOMBRE"));
        medidaA1.setNif(rs.getString("NIF_CIF"));
        Double aux4 = new Double(rs.getDouble("IMPORTE_ANUAL"));
        if (aux4 != 0) {
            medidaA1.setImporteAnual(aux4.doubleValue());
        }
        
        medidaA1.setObjetoContrato(rs.getString("OBJETO_CONTRATO"));
       
        return medidaA1;
    }
    
    public Object mapA2(ResultSet rs, Class clazz) throws Exception {
        return mapVersA2(rs, clazz, true);
    }

    public Object mapVersA2(ResultSet rs, Class clazz, boolean completo) throws Exception {
        if (clazz == MedidaA2VO.class) {
            return mapearMedidaA2VO(rs);
        }

        return null;
    }

    private Object mapearMedidaA2VO(ResultSet rs) throws SQLException {
        MedidaA2VO medidaA2 = new MedidaA2VO();

        medidaA2.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            medidaA2.setId(null);
        }
        medidaA2.setNumExp(rs.getString("NUM_EXP"));
        
        medidaA2.setNombre(rs.getString("NOMBRE"));
        medidaA2.setApellido1(rs.getString("APELLIDO1"));
        medidaA2.setApellido2(rs.getString("APELLIDO2"));
        medidaA2.setTipoDocumento(rs.getString("TIPO_DOCUMENTO"));
        medidaA2.setDni(rs.getString("DNI_NIE"));
        medidaA2.setTfno(rs.getString("TFNO"));
        medidaA2.setEmail(rs.getString("EMAIL"));
        medidaA2.setProvincia(rs.getString("PROVINCIA"));
        medidaA2.setMunicipio(rs.getString("MUNICIPIO"));
        medidaA2.setLocalidad(rs.getString("LOCALIDAD"));
        medidaA2.setDireccion(rs.getString("DIRECCION"));
        medidaA2.setPortal(rs.getString("PORTAL"));
        medidaA2.setPiso(rs.getString("PISO"));
        medidaA2.setLetra(rs.getString("LETRA"));
        medidaA2.setC_postal(rs.getString("C_POSTAL"));
        Double aux4 = new Double(rs.getDouble("IMPORTE_ANUAL"));
        if (aux4 != 0) {
            medidaA2.setImporteAnual(aux4.doubleValue());
        }
        
        medidaA2.setObjetoContrato(rs.getString("OBJETO_CONTRATO"));
       
        return medidaA2;
    }
    
    public Object mapBC(ResultSet rs, Class clazz) throws Exception {
        return mapVersBC(rs, clazz, true);
    }

    public Object mapVersBC(ResultSet rs, Class clazz, boolean completo) throws Exception {
        if (clazz == MedidaA1BCVO.class) {
            return mapearMedidaBCVO(rs);
        }

        return null;
    }

    private Object mapearMedidaBCVO(ResultSet rs) throws SQLException {
        MedidaA1BCVO medidaBC = new MedidaA1BCVO();

        medidaBC.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            medidaBC.setId(null);
        }
        medidaBC.setNumExp(rs.getString("NUM_EXP"));
        
        medidaBC.setNombre(rs.getString("NOMBRE"));
        medidaBC.setNif(rs.getString("NIF_CIF"));
        Double aux4 = new Double(rs.getDouble("IMPORTE_ANUAL"));
        if (aux4 != 0) {
            medidaBC.setImporteAnual(aux4.doubleValue());
        }
       
        return medidaBC;
    }
    
    public RegistroBatchVO mapRegistroBatch(ResultSet rs) throws SQLException {
        RegistroBatchVO fila = new RegistroBatchVO();
        fila.setFecha(rs.getString("CRO_FEF"));
        fila.setUsuario(rs.getInt("CRO_USU"));
        fila.setEjerRegistro(rs.getInt("CRO_EJE"));
        fila.setNumExpediente(rs.getString("CRO_NUM"));
        fila.setUnidadTramitadora(rs.getString("CRO_UTR"));
       // fila.setCodTramite(rs.getInt("COD_TRA"));
        //fila.setOperacion(rs.getString("OPERACION"));
        //fila.setResultado(rs.getString("RESULTADO"));
        //fila.setCodOperacion(rs.getInt("COD_OPERACION"));
       // fila.setRelanzar(rs.getInt("RELANZAR"));
      //  fila.setObservaciones(rs.getString("OBSERVACIONES"));

        return fila;
    }

    /*public Object map(ResultSet rs, Class clazz) throws Exception {
        return mapVers2(rs, clazz, true);
    }

    public Object mapVers2(ResultSet rs, Class clazz, boolean completo) throws Exception {
        if (clazz == MedidaAltVO.class) {
            return mapearMedidaAltVO(rs);
        }

        return null;
    }

    private Object mapearMedidaAltVO(ResultSet rs) throws SQLException {
        MedidaAltVO medidaAlt = new MedidaAltVO();

        medidaAlt.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            medidaAlt.setId(null);
        }
        
        medidaAlt.setNumExp(rs.getString("NUM_EXP"));
        
        Integer aux2 = new Integer(rs.getInt("TIPO_MEDIDA_ALT"));
        medidaAlt.setTipoMedida(aux2.intValue());
        
        Integer aux3 = new Integer(rs.getInt("CONTRATO_CON"));
        medidaAlt.setContratoCon(aux3.intValue());
        
        medidaAlt.setNombre(rs.getString("NOMBRE"));
        
        medidaAlt.setNif(rs.getString("NIF_CIF"));
        
        Double aux4 = new Double(rs.getDouble("IMPORTE_ANUAL"));
        if (aux4 != 0) {
            medidaAlt.setImporteAnual(aux4.doubleValue());
        }
        
        medidaAlt.setObjetoContrato(rs.getString("OBJETO_CONTRATO"));
        
        Integer aux5 = new Integer(rs.getInt("NUM_TRAB_SUSTITUYE"));
        if (aux5 != 0) {
            medidaAlt.setNumTrabSustituye(aux5.intValue());
        }
        
        Integer aux6 = new Integer(rs.getInt("PERIODO_ANIOS"));
        if (aux6 != 0) {
            medidaAlt.setPeriodoAnios(aux6.intValue());
        }
        
        medidaAlt.setApellido1Autonoma(rs.getString("APELLIDO1_AUTONOMA"));
        
        medidaAlt.setApellido2Autonoma(rs.getString("APELLIDO2_AUTONOMA"));
        
        medidaAlt.setConsentimiento(rs.getString("CONSENTIMIENTO"));
        
        Integer aux7 = new Integer(rs.getInt("PERSONAS_DISCA_OCUPA"));
        if (aux7 != 0) {
            medidaAlt.setPersonasDiscaOcupa(aux7.intValue());
        }
        
        return medidaAlt;
    }*/
    
}
