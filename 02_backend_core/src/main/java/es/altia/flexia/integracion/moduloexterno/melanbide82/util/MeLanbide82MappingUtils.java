package es.altia.flexia.integracion.moduloexterno.melanbide82.util;

import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide82.vo.FilaContratacionVO;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MeLanbide82MappingUtils {

    private static MeLanbide82MappingUtils instance = null;

    private MeLanbide82MappingUtils() {
    }

    public static MeLanbide82MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide82MappingUtils.class) {
                instance = new MeLanbide82MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == FilaContratacionVO.class) {
            return mapearFilaContratacionVO(rs);
        } else if (clazz == DesplegableAdmonLocalVO.class) {
            return mapearDesplegableAdmonLocalVO(rs);
        }
        return null;
    }

    /**
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    private Object mapearFilaContratacionVO(ResultSet rs) throws SQLException {
        FilaContratacionVO contratacion = new FilaContratacionVO();

        /*NOMBRES COLUMNAS CONTRATACION
        ID NUM_EXP  PRIORIDAD DENOMPUESTO NIVELCUALIFICACION MODCONTRATO DURCONTRATO GRUPOCOTIZ COSTESALARIAL SUBVSOLICITADA 
         */
        contratacion.setNumExp(rs.getString("NUM_EXP"));
        contratacion.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            contratacion.setId(null);
        }
        contratacion.setPrioridad(rs.getInt("PRIORIDAD") != 0 ? rs.getInt("PRIORIDAD") : null);
        contratacion.setDenomPuesto(rs.getString("DENOMPUESTO"));
        contratacion.setNivelCualificacion(rs.getString("NIVELCUALIFICACION"));
        contratacion.setModContrato(rs.getString("MODCONTRATO"));
        contratacion.setDurContrato(rs.getInt("DURCONTRATO") != 0 ? rs.getInt("DURCONTRATO") : null);
        contratacion.setGrupoCotiz(rs.getString("GRUPOCOTIZ"));
        contratacion.setCostesalarial(rs.getDouble("COSTESALARIAL") != 0 ? rs.getDouble("COSTESALARIAL") : null);
        contratacion.setSubvsolicitada(rs.getDouble("SUBVSOLICITADA") != 0 ? rs.getDouble("SUBVSOLICITADA") : null);
        /* CONTRATACION_INICIO         */
        contratacion.setIdInicio(rs.getInt(11));
        contratacion.setMunicipioInicio(rs.getString("MUNICIPIO2") != null && !rs.getString("MUNICIPIO2").isEmpty() ? rs.getString("MUNICIPIO2") : "");
        contratacion.setNombreInicio(rs.getString("NOMBRE2") != null && !rs.getString("NOMBRE2").isEmpty() ? rs.getString("NOMBRE2") : "");
        contratacion.setApellido1Inicio(rs.getString("APELLIDO12") != null && !rs.getString("APELLIDO12").isEmpty() ? rs.getString("APELLIDO12") : "");
        contratacion.setApellido2Inicio(rs.getString("APELLIDO22") != null && !rs.getString("APELLIDO22").isEmpty() ? rs.getString("APELLIDO22") : "");
        contratacion.setDninieInicio(rs.getString("DNINIE2") != null && !rs.getString("DNINIE2").isEmpty() ? rs.getString("DNINIE2") : "");
        contratacion.setFechanacimientoInicio(rs.getDate("FECHANACIMIENTO2") != null ? rs.getDate("FECHANACIMIENTO2") : null);
        contratacion.setSexoInicio(rs.getString("SEXO2") != null && !rs.getString("SEXO2").isEmpty() ? rs.getString("SEXO2") : "");
        contratacion.setNivelcualificacionInicio(rs.getString("NIVELCUALIFICACION2") != null && !rs.getString("NIVELCUALIFICACION2").isEmpty() ? rs.getString("NIVELCUALIFICACION2") : "");
        contratacion.setPuestotrabajoInicio(rs.getString("PUESTOTRABAJO2") != null && !rs.getString("PUESTOTRABAJO2").isEmpty() ? rs.getString("PUESTOTRABAJO2") : "");
        contratacion.setNofertaInicio(rs.getString("NOFERTA2") != null && !rs.getString("NOFERTA2").isEmpty() ? rs.getString("NOFERTA2") : "");
        contratacion.setGrupocotizInicio(rs.getString("GRUPOCOTIZ2") != null && !rs.getString("GRUPOCOTIZ2").isEmpty() ? rs.getString("GRUPOCOTIZ2") : "");
        contratacion.setDurcontratoInicio(rs.getInt("DURACION2") != 0 ? rs.getInt("DURACION2") : null);
        contratacion.setFechainicioInicio(rs.getDate("FECHAINICIO2") != null ? rs.getDate("FECHAINICIO2") : null);
        contratacion.setEdadInicio(rs.getInt("EDAD2") != 0 ? rs.getInt("EDAD2") : null);
        contratacion.setRetribucionbrutaInicio(rs.getDouble("RETRIBUCIONBRUTA2") != 0 ? rs.getDouble("RETRIBUCIONBRUTA2") : null);
        contratacion.setSistGrantiaJuveIni(rs.getString("SISTGRANTIAJUVE_INI"));
        /* CONTRATACION_FIN         */
        contratacion.setIdFin(rs.getInt(30));
        contratacion.setMunicipioFin(rs.getString("MUNICIPIO3") != null && !rs.getString("MUNICIPIO3").isEmpty() ? rs.getString("MUNICIPIO3") : "");
        contratacion.setNombreFin(rs.getString("NOMBRE3") != null && !rs.getString("NOMBRE3").isEmpty() ? rs.getString("NOMBRE3") : "");
        contratacion.setApellido1Fin(rs.getString("APELLIDO13") != null && !rs.getString("APELLIDO13").isEmpty() ? rs.getString("APELLIDO13") : "");
        contratacion.setApellido2Fin(rs.getString("APELLIDO23") != null && !rs.getString("APELLIDO23").isEmpty() ? rs.getString("APELLIDO23") : "");
        contratacion.setDninieFin(rs.getString("DNINIE3") != null && !rs.getString("DNINIE3").isEmpty() ? rs.getString("DNINIE3") : "");
        contratacion.setSexoFin(rs.getString("SEXO3") != null && !rs.getString("SEXO3").isEmpty() ? rs.getString("SEXO3") : "");
        contratacion.setGrupocotizFin(rs.getString("GRUPOCOTIZ3") != null && !rs.getString("GRUPOCOTIZ3").isEmpty() ? rs.getString("GRUPOCOTIZ3") : "");
        contratacion.setDurcontratoFin(rs.getInt("DURACION3") != 0 ? rs.getInt("DURACION3") : null);
        contratacion.setFechainicioFin((rs.getDate("FECHAINICIO3") != null ? rs.getDate("FECHAINICIO3") : null));
        contratacion.setFechafinFin((rs.getDate("FECHAFIN3") != null ? rs.getDate("FECHAFIN3") : null));
        contratacion.setRetribucionbrutaFin(rs.getDouble("RETRIBUCIONBRUTA3") != 0 ? rs.getDouble("RETRIBUCIONBRUTA3") : null);
        contratacion.setCostesalarialFin(rs.getDouble("COSTESALARIAL3") != 0 ? rs.getDouble("COSTESALARIAL3") : null);
        contratacion.setCostesssFin(rs.getDouble("COSTESSS3") != 0 ? rs.getDouble("COSTESSS3") : null);
        contratacion.setIndemfincontratoFin(rs.getDouble("INDEMFINCONTRATO3") != 0 ? rs.getDouble("INDEMFINCONTRATO3") : null);
        contratacion.setCostetotalrealFin(rs.getDouble("COSTETOTALREAL3") != 0 ? rs.getDouble("COSTETOTALREAL3") : null);
        contratacion.setSubvconcedidalanFin(rs.getDouble("SUBVCONCEDIDALAN3") != 0 ? rs.getDouble("SUBVCONCEDIDALAN3") : null);
        contratacion.setSistGrantiaJuveFin(rs.getString("SISTGRANTIAJUVE_FIN"));

        return contratacion;
    }

    private Object mapearDesplegableAdmonLocalVO(ResultSet rs) throws SQLException {
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
