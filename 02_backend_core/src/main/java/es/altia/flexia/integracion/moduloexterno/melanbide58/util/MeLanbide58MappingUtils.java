/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide58.util;

import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.AltaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.BajaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.CausaAltaBajaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DiscapacitadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.PlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide58.vo.SMIVO;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author davidg
 */
public class MeLanbide58MappingUtils {

    private static MeLanbide58MappingUtils instance = null;

    private MeLanbide58MappingUtils() {
    }

    public static MeLanbide58MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide58MappingUtils.class) {
                instance = new MeLanbide58MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        return mapVers2(rs, clazz, true);
    }

    public Object mapVers2(ResultSet rs, Class clazz, boolean completo) throws Exception {
        if (clazz == PlantillaVO.class) {
            return mapearPlantillaVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        } else if (clazz == SMIVO.class) {
            return mapearSMIVO(rs, completo);
        } else if (clazz == AltaVO.class) {
            return mapearAltaVO(rs);
        } else if (clazz == BajaVO.class) {
            return mapearBajaVO(rs);
        } else if (clazz == CausaAltaBajaVO.class) {
            return mapearCausaAltaBajaVO(rs);
        } else if (clazz == DiscapacitadoVO.class) {
            return mapearDiscapacitadoVO(rs);
        }
        return null;
    }

    private Object mapearCausaAltaBajaVO(ResultSet rs) throws SQLException {
        CausaAltaBajaVO causa = new CausaAltaBajaVO();

        if (rs.getString("DES_COD") != null) {
            causa.setDesCod(rs.getString("DES_COD"));
        }

        if (rs.getString("DES_VAL_COD") != null) {
            String des_val_cod = rs.getString("DES_VAL_COD");
            String[] codsAlta = des_val_cod.split("ALTA", 4);
            if (codsAlta[0].isEmpty()) {
                des_val_cod = codsAlta[1];
            }
            String[] codsBaja = des_val_cod.split("BAJA", 4);
            if (codsBaja[0].isEmpty()) {
                des_val_cod = codsBaja[1];
            }
            causa.setDesValCod(des_val_cod);
        }

        if (rs.getString("DES_NOM") != null) {
            causa.setDes_nombre(rs.getString("DES_NOM"));
        }
        return causa;
    }

    private Object mapearSMIVO(ResultSet rs, boolean completo) throws SQLException {
        SMIVO smi = new SMIVO();

        smi.setId(rs.getInt("IDSMI"));
        if (rs.wasNull()) {
            smi.setId(null);
        }
        Integer aux = rs.getInt("NUM_LINEA");
        if (aux != 0) {
            smi.setNumLinea(aux);
        }
        smi.setNombre(rs.getString("NOMBRE"));
        smi.setApellidos(rs.getString("APELLIDOS"));
        Double aux1 = rs.getDouble("NUM_DIAS_SIN");
        if (aux1 != 0) {
            smi.setNumDiasSinIncidencias(aux1);
        }
        Double aux2 = rs.getDouble("NUM_DIAS");
        if (aux2 != 0) {
            smi.setNumDiasIncidencia(aux2);
        }
        smi.setCausaIncidencia(rs.getString("CAUSA"));
        if (completo) {
            smi.setDesCausaIncidencia(rs.getString("DES_CAUSA"));
            // smi.setDesCausaIncidencia(rs.getString("DES_NOM"));
        }
        Double aux3 = rs.getDouble("IMPORTE");
        if (aux3 != 0) {
            smi.setImporteSolicitado(aux3);
        }
        Double aux4 = rs.getDouble("IMPORTE_CALCULADO");
        if (aux4 != 0) {
            smi.setImporteRecalculo(aux4);
        }
        if (rs.getDate("FECHA") != null) {
            smi.setFecha(rs.getDate("FECHA"));
        }
        Double aux5 = rs.getDouble("POR_JORNADA");
        if (aux5 != 0) {
            smi.setPorcJornada(aux5);
        }
        Double aux6 = rs.getDouble("POR_REDUCCION");
        //if (aux6 != 0) {
        smi.setPorcReduccion(aux6);
        //}
        smi.setNumExp(rs.getString("NUM_EXP"));

        Double aux7 = rs.getDouble("IMPORTE_LANBIDE");
        if (aux7 != 0) {
            smi.setImporteLanbide(aux7);
        }

        //smi.setObservaciones(rs.getString("OBSERVACIONES")!=null?rs.getString("OBSERVACIONES"):"");
        if (rs.getString("OBSERVACIONES") != null) {
            smi.setObservaciones(rs.getString("OBSERVACIONES").replaceAll("\n", "nnn"));
        } else {
            smi.setObservaciones(rs.getString("OBSERVACIONES"));
        }
        smi.setNif(rs.getString("NIF"));
        
        return smi;
    }

    private Object mapearPlantillaVO(ResultSet rs) throws SQLException {
        PlantillaVO plantilla = new PlantillaVO();

        plantilla.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            plantilla.setId(null);
        }
        plantilla.setNif_Dni(rs.getString("NIF"));
        plantilla.setNombre(rs.getString("NOMBRE"));
        plantilla.setApellidos(rs.getString("APELLIDOS"));
        plantilla.setSexo(rs.getString("SEXO"));
        if (rs.getDate("Fec_Naci") != null) {
            plantilla.setFecNaci(rs.getDate("Fec_Naci"));
        }
        plantilla.setNumSS(rs.getString("Num_Ss"));
        if (rs.getDate("FECHA_CERT") != null) {
            plantilla.setFecCertificado(rs.getDate("FECHA_CERT"));
        }
        Double aux = rs.getDouble("GRADO_DISC");
        if (aux != 0) {
            plantilla.setGrado(aux);
        }
        plantilla.setTipoDis(rs.getString("TIPO_DISC"));
        plantilla.setCodContrato(rs.getString("COD_CONTRATO"));
        Integer aux1 = rs.getInt("NUM_LINEA");
        if (aux1 != 0) {
            plantilla.setNumLinea(aux1);
        }
        plantilla.setNumExp(rs.getString("NUM_EXP"));
//        Double aux2 = rs.getDouble("POR_JORNADA");
//        if (aux2 != 0) {
//            plantilla.setPorcJornada(aux2);
//        }
        // Discapacidad severa
        if (rs.getString("DISC_SEVERA_EMP") != null) {
            plantilla.setDiscSevera(rs.getString("DISC_SEVERA_EMP"));
        }
        if (rs.getString("DISC_SEVERA_LAN") != null) {
            plantilla.setDiscValidada(rs.getString("DISC_SEVERA_LAN"));
        }
        if (rs.getString("DATOS_PENDIENTES") != null){
            plantilla.setDatosPendientes(rs.getString("DATOS_PENDIENTES"));
        }
        if (rs.getString("NUEVA_ALTA") != null){
            plantilla.setNuevaAlta(rs.getString("NUEVA_ALTA"));
        }
        return plantilla;
    }

    private Object mapearAltaVO(ResultSet rs) throws SQLException {
        AltaVO alta = new AltaVO();

        alta.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            alta.setId(null);
        }
        alta.setNif(rs.getString("NIF"));
        alta.setNombre(rs.getString("NOMBRE"));
        alta.setApellidos(rs.getString("APELLIDOS"));
        if (rs.getDate("FECHA_ALTA") != null) {
            alta.setFechaAlta(rs.getDate("FECHA_ALTA"));
        }
        alta.setNumSS(rs.getString("NUM_SS"));
        Integer aux = rs.getInt("NUM_LINEA");
        if (aux != 0) {
            alta.setNumLinea(aux);
        }
        alta.setNumExp(rs.getString("NUM_EXP"));
        alta.setCausa(rs.getString("CAUSA"));

        alta.setCausaDesc(rs.getString("DES_NOM"));
        return alta;
    }

    private Object mapearBajaVO(ResultSet rs) throws SQLException {
        BajaVO baja = new BajaVO();

        baja.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            baja.setId(null);
        }
        baja.setNif(rs.getString("NIF"));
        baja.setNombre(rs.getString("NOMBRE"));
        baja.setApellidos(rs.getString("APELLIDOS"));
        if (rs.getDate("FECHA_BAJA") != null) {
            baja.setFechaBaja(rs.getDate("FECHA_BAJA"));
        }
        baja.setNumSS(rs.getString("NUM_SS"));
        Integer aux = rs.getInt("NUM_LINEA");
        if (aux != 0) {
            baja.setNumLinea(aux);
        }
        baja.setNumExp(rs.getString("NUM_EXP"));
        baja.setCausa(rs.getString("CAUSA"));
        baja.setCausaDesc(rs.getString("DES_NOM"));

        return baja;
    }
    
    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

    private Object mapearDiscapacitadoVO(ResultSet rs) throws SQLException {
        DiscapacitadoVO discapacitado = new DiscapacitadoVO();
        discapacitado.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            discapacitado.setId(null);
        }
        discapacitado.setDni(rs.getString("DNI"));
        discapacitado.setApellidos(rs.getString("APELLIDOS"));
        discapacitado.setNombre(rs.getString("NOMBRE"));
        if (rs.getString("TIPO_DISCP") != null) {
            discapacitado.setTipoDisc(rs.getString("TIPO_DISCP"));
        }
        if (rs.getString("DESC_TIPOD") != null) {
            discapacitado.setDescTipoDisc(rs.getString("DESC_TIPOD"));
        }
        Double auxD = rs.getDouble("PORC_DISCP");
        if (auxD != 0) {
            discapacitado.setPorcDisc(auxD);
        }
        if (rs.getDate("FECH_EMISION") != null) {
            discapacitado.setFechEmision(rs.getDate("FECH_EMISION"));
        }
        if (rs.getDate("FECH_RESOLUCION") != null) {
            discapacitado.setFechResolucion(rs.getDate("FECH_RESOLUCION"));
        }
        if (rs.getString("VALIDEZ") != null) {
            discapacitado.setValidez(rs.getString("VALIDEZ"));
        }
        if (rs.getString("DESC_VALIDEZ") != null) {
            discapacitado.setDescValidez(rs.getString("DESC_VALIDEZ"));
        }
        if (rs.getDate("FECH_CADUCIDAD") != null) {
            discapacitado.setFechCaducidad(rs.getDate("FECH_CADUCIDAD"));
        }
        if (rs.getString("DISC_SEVERA") != null) {
            discapacitado.setDiscSevera(rs.getString("DISC_SEVERA"));
        }
        if (rs.getString("DESC_SEVERA") != null) {
            discapacitado.setDescDiscSevera(rs.getString("DESC_SEVERA"));
        }
        if (rs.getDate("FECH_BAJA") != null) {
            discapacitado.setFechBaja(rs.getDate("FECH_BAJA"));
        }
        if (rs.getDate("FECH_VALIDACION") != null) {
            discapacitado.setFechValidacion(rs.getDate("FECH_VALIDACION"));
        }
        if (rs.getString("OID_CERTIFICADO") != null) {
            discapacitado.setOidCertificado(rs.getString("OID_CERTIFICADO"));
        }
        if (rs.getString("NOMBRE_CERTIFICADO") != null) {
            discapacitado.setNombreCertificado(rs.getString("NOMBRE_CERTIFICADO"));
        }
        if (rs.getString("CENTRO") != null) {
            discapacitado.setCentro(rs.getString("CENTRO"));
        }
        if (rs.getString("TERRITORIO") != null) {
            discapacitado.setTerritorio(rs.getString("TERRITORIO"));
        }
        if (rs.getString("DESC_TERRITORIO") != null) {
            discapacitado.setDescTerritorio(rs.getString("DESC_TERRITORIO"));
        }
        return discapacitado;
    }

}
