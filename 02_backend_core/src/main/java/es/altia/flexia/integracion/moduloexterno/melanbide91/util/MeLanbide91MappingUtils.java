package es.altia.flexia.integracion.moduloexterno.melanbide91.util;
/**
 *
 * @author gerardo.perez
 */
import es.altia.flexia.integracion.moduloexterno.melanbide91.dao.MeLanbide91DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.ContrGenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide91.vo.SubvenSolicVO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

    public class MeLanbide91MappingUtils {

    private static MeLanbide91MappingUtils instance = null;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide91MappingUtils.class);

    private MeLanbide91MappingUtils() {
    }

    public static MeLanbide91MappingUtils getInstance() {
        if (instance == null) {
            synchronized (MeLanbide91MappingUtils.class) {
                instance = new MeLanbide91MappingUtils();
            }
        }
        return instance;
    }

    public Object map(ResultSet rs, Class clazz) throws Exception {
        if (clazz == ContrGenVO.class) {
            return mapearContrGenVO(rs);
        }
        if (clazz == SubvenSolicVO.class) {
            return mapearSubvenSolic(rs);
        }  
        if (clazz == DesplegableVO.class) {
            return mapearDesplegableVO(rs);
        }
        return null;
    }

   

    private Object mapearContrGenVO(ResultSet rs) throws SQLException {
        ContrGenVO acceso = new ContrGenVO();

        acceso.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            acceso.setId(null);
        }

        acceso.setNumExp(rs.getString("NUM_EXP"));
        acceso.setNombre(rs.getString("NOMBRE"));
        acceso.setApellido1(rs.getString("APELLIDO1"));
        acceso.setApellido2(rs.getString("APELLIDO2"));
        acceso.setSexo(rs.getString("SEXO"));
        acceso.setDni(rs.getString("DNI"));
        acceso.setPsiquica(rs.getDouble("PSIQUICA"));
        acceso.setFisica(rs.getDouble("FISICA"));
        acceso.setSensorial(rs.getDouble("SENSORIAL"));
        acceso.setFecIni(rs.getDate("FECINI"));
        if(acceso.getFecIni()!=null){
            acceso.setFecIniStr(dateFormat.format(acceso.getFecIni()));
        }
        acceso.setFecIni(rs.getDate("FECINI"));
        acceso.setJornada(rs.getString("JORNADA"));
       /* acceso.setPorcParcial(rs.getInt("PORCPARCIAL"));*/
        acceso.setPorcParcial(rs.getDouble("PORCPARCIAL"));
        return acceso;
    }

    private Object mapearSubvenSolic(ResultSet rs) throws SQLException {
        SubvenSolicVO acceso = new SubvenSolicVO();

        acceso.setId(rs.getInt("ID"));
        if (rs.wasNull()) {
            acceso.setId(null);
        }
        log.debug("id: " + rs.getInt("ID"));
        log.debug("NUM_EXP: " + rs.getString("NUM_EXP"));
        log.debug("TIPODATOS: " + rs.getString("TIPODATOS"));
        log.debug("TIPO: " + rs.getString("TIPO"));
        log.debug("FECHA: " + rs.getDate("FECHA"));
        log.debug("DESTINO: " + rs.getString("DESTINO"));
        log.debug("COSTE: " + rs.getDouble("COSTE"));
        
        
        acceso.setNumExp(rs.getString("NUM_EXP"));
        acceso.setTipoDatos(rs.getString("TIPODATOS"));
        acceso.setTipo(rs.getString("TIPO"));
        acceso.setFecha(rs.getDate("FECHA"));
        if(acceso.getFecha()!=null){
            acceso.setFechaStr(dateFormat.format(acceso.getFecha()));
        }
        acceso.setFecha(rs.getDate("FECHA"));
        acceso.setDestino(rs.getString("DESTINO"));
        acceso.setCoste(rs.getDouble("COSTE"));
       

        return acceso;
    }
    private Object mapearDesplegableVO(ResultSet rs) throws SQLException {
        DesplegableVO valoresDesplegable = new DesplegableVO();
        valoresDesplegable.setDes_cod(rs.getString("DES_COD"));
        valoresDesplegable.setDes_val_cod(rs.getString("DES_VAL_COD"));
        valoresDesplegable.setDes_nom(rs.getString("DES_NOM"));
        return valoresDesplegable;
    }

}
