package es.altia.flexia.integracion.moduloexterno.melanbide14.util;

import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionSolicitadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class MeLanbide14MappingUtils {

    private static MeLanbide14MappingUtils instance = null;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    private MeLanbide14MappingUtils() {
    }

    public static MeLanbide14MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide14MappingUtils.class) {
                instance = new MeLanbide14MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == OperacionVO.class) {
            return mapearOperacionVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        } else if (clazz == OperacionSolicitadaVO.class) {
            return mapearOperacionPresentada(rs);
        }
        return null;
    }

    private Object mapearOperacionVO(ResultSet rs) throws SQLException {
        OperacionVO operacion = new OperacionVO();

        operacion.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            operacion.setId(null);
        }

        operacion.setNumExp(rs.getString("NUM_EXP"));
        operacion.setNumOper(rs.getInt("NUMOPER"));
        operacion.setNombreOper(rs.getString("NOMBREOPER"));
        operacion.setPrio(rs.getString("PRIOROBJ"));
        operacion.setLin1(rs.getString("LINACT1"));
        operacion.setLin2(rs.getString("LINACT2"));
        operacion.setLin3(rs.getString("LINACT3"));
        operacion.setImpOper(rs.getDouble("IMPOPER"));

        return operacion;
    }

    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

    private Object mapearOperacionPresentada(ResultSet rs) throws Exception {
        OperacionSolicitadaVO opPresentada = new OperacionSolicitadaVO();
        opPresentada.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            opPresentada.setId(null);
        }
        opPresentada.setNumExp(rs.getString("NUM_EXP"));
        opPresentada.setEjeOperacion(rs.getInt("EJEOPERACION"));
        opPresentada.setNumOpePre(rs.getInt("NUMOPEPRE"));
        opPresentada.setPrioridad(rs.getString("PRIORIDAD"));
        opPresentada.setDescPrioridad(rs.getString("DES_PRIORIDAD"));
        opPresentada.setObjetivo(rs.getString("OBJETIVO"));
        opPresentada.setDescObjetivo(rs.getString("DES_OBJETIVO"));
        opPresentada.setTipologia(rs.getString("TIPOLOGIA"));
        opPresentada.setDescTipologia(rs.getString("DES_TIPOLOGIA"));
        opPresentada.setFecInicio(rs.getDate("FECINICIO"));
        if (opPresentada.getFecInicio() != null) {
            opPresentada.setFecInicioStr(formatoFecha.format(opPresentada.getFecInicio()));
        }
        opPresentada.setFecFin(rs.getDate("FECFIN"));
        if (opPresentada.getFecFin()!= null) {
            opPresentada.setFecFinStr(formatoFecha.format(opPresentada.getFecFin()));
        }
        opPresentada.setEntidad(rs.getString("ENTIDAD"));
        opPresentada.setDescEntidad(rs.getString("DES_ENTIDAD"));
        opPresentada.setOrganismo(rs.getString("ORGANISMO"));

        return opPresentada;
    }

}
