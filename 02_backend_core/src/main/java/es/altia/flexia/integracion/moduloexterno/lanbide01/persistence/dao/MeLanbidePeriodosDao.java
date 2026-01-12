package es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.MeLanbide01Constantes;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.DatosPeriodoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**e ha producido un error insertando los datos del cálcul
 * Objeto de acceso a datos que contiene las operaciones que se lanzan contra la tabla MELANBIDE01_PERIODO
 */
public class MeLanbidePeriodosDao {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbidePeriodosDao.class);
    
    //Instace
    private static MeLanbidePeriodosDao instance = null;
    
    //Default constructor
    private MeLanbidePeriodosDao(){}
    
    public static MeLanbidePeriodosDao getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbidePeriodosDao.class){
                instance = new MeLanbidePeriodosDao();
            }//synchronized(MeLanbidePeriodosDao.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    public void insertarPeriodos (ArrayList<DatosPeriodoVO> periodos, String codMunicipio, Integer idDatosCalculo
            , Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertarPeriodos() : BEGIN");
        PreparedStatement ps = null;
        Integer filasInsertadas = new Integer(0);
        try{
            String nombreTabla = ConfigurationParameter.getParameter(codMunicipio +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_PERIODO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            String sql = "Insert into " + nombreTabla + " (ID, FECHA_INICIO, FECHA_FIN, PORC_SUBVENC, NUM_DIAS, BASE_COTIZACION, "
                    + " GASTO, ID_DATOS_CALCULO, REDUC_PERS_SUST, JORN_PERS_SUST, JORN_PERS_CONT, BONIFICACION "
                    + " ,PORC_JORN_REALIZADA, PORC_JORN_SUSTITUCION "
                    + ") "
                    + "values (SEQ_ME_LANBIDE01_PERIODO.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            for(DatosPeriodoVO periodo : periodos){
                ps = con.prepareStatement(sql);
                int x = 1;
                    ps.setDate(x++, new java.sql.Date(periodo.getFechaInicio().getTimeInMillis()));
                    ps.setDate(x++, new java.sql.Date(periodo.getFechaFin().getTimeInMillis()));
                    ps.setDouble(x++, Double.valueOf(periodo.getPorcSubven().replace(",", ".")));
                    ps.setInt(x++, periodo.getNumDias());
                    ps.setDouble(x++, Double.valueOf(periodo.getBaseCotizacion()));
                    ps.setDouble(x++, Double.valueOf(periodo.getGasto()));
                    ps.setInt(x++, idDatosCalculo);
                    ps.setDouble(x++, Double.valueOf(periodo.getReducPerSust().replace(",", ".")));
                    ps.setDouble(x++, Double.valueOf(periodo.getJornPersSust().replace(",", ".")));
                    ps.setDouble(x++, Double.valueOf(periodo.getJornPersCont().replace(",", ".")));
                    ps.setDouble(x++, Double.valueOf(periodo.getBonificacion()));
                    ps.setDouble(x++, periodo.getPorcJornRealizada());
                    ps.setDouble(x++, periodo.getPorcJornSustitucion());
                    ps.executeQuery();
            }//for(DatosPeriodoVO periodo : periodos)
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("insertarPeriodos() : END");
    }//insertarPeriodos
    
    public Boolean existenPeriodos(Integer codDatosCalculo, String codMunicipio, Connection con)throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("existenPeriodos() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        Boolean existen = false;
        try{
            String nombreTabla = ConfigurationParameter.getParameter(codMunicipio +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_PERIODO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            String sql = "Select ID " 
                    + " FROM " + nombreTabla
                    + " where ID_DATOS_CALCULO = " + codDatosCalculo;
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                existen = true;
            }//while(rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("existenPeriodos() : END");
        return existen;
    }//existenPeriodos
    
    public void borrarPeriodos(Integer codDatosCalculo, String codMunicipio, Connection con)throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("borrarPeriodos() : BEGIN");
        Statement st = null;
        Integer filasAfectadas = null;
        try{
            String nombreTabla = ConfigurationParameter.getParameter(codMunicipio +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_PERIODO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            String sql = "Delete " 
                    + " FROM " + nombreTabla
                    + " where ID_DATOS_CALCULO = " + codDatosCalculo;
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            filasAfectadas = st.executeUpdate(sql);
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("borrarPeriodos() : END");
    }//borrarPeriodos
    
    public ArrayList<DatosPeriodoVO> getPeriodos (Integer codDatosCalculo, String codMunicipio,
            Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("getPeriodos() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        ArrayList<DatosPeriodoVO> periodos = new ArrayList<DatosPeriodoVO>();
        try{
            String nombreTabla = ConfigurationParameter.getParameter(codMunicipio +
                MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_PERIODO, 
                MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            String sql = "Select ID, FECHA_INICIO, FECHA_FIN, PORC_SUBVENC, NUM_DIAS, BASE_COTIZACION, "
                    + " GASTO, ID_DATOS_CALCULO, REDUC_PERS_SUST, JORN_PERS_SUST, JORN_PERS_CONT, BONIFICACION"
                    + " ,PORC_JORN_REALIZADA, PORC_JORN_SUSTITUCION "
                    + " FROM " + nombreTabla
                    + " where ID_DATOS_CALCULO = " + codDatosCalculo 
                    + " ORDER BY FECHA_INICIO ASC,FECHA_FIN ASC";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                DatosPeriodoVO periodo = new DatosPeriodoVO();
                    periodo.setId(rs.getInt("ID"));
                    Calendar fechaInicio = Calendar.getInstance();
                        fechaInicio.setTime(rs.getDate("FECHA_INICIO"));
                    periodo.setFechaInicio(fechaInicio);
                    Calendar fechaFin = Calendar.getInstance();
                        fechaFin.setTime(rs.getDate("FECHA_FIN"));
                    periodo.setFechaFin(fechaFin);
                    periodo.setPorcSubven(rs.getString("PORC_SUBVENC"));
                    periodo.setNumDias(rs.getInt("NUM_DIAS"));
                    periodo.setBaseCotizacion(rs.getString("BASE_COTIZACION"));
                    periodo.setGasto(rs.getString("GASTO"));
                    periodo.setIdDatosCalculo(rs.getInt("ID_DATOS_CALCULO"));
                    periodo.setReducPerSust(rs.getString("REDUC_PERS_SUST"));
                    periodo.setJornPersSust(rs.getString("JORN_PERS_SUST"));
                    periodo.setJornPersCont(rs.getString("JORN_PERS_CONT"));
                    periodo.setBonificacion(rs.getString("BONIFICACION"));
                    periodo.setPorcJornRealizada(rs.getDouble("PORC_JORN_REALIZADA"));
                    periodo.setPorcJornSustitucion(rs.getDouble("PORC_JORN_SUSTITUCION"));
                    periodos.add(periodo);
            }//while(rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getPeriodos() : END");
        return periodos;
    }//getPeriodos
    
    
    
   public ArrayList<DatosPeriodoVO> getPeriodos (int codMunicipio, String numExpediente,AdaptadorSQLBD adapt) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("getPeriodos() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        Connection con = null;
        
        ArrayList<DatosPeriodoVO> periodos = new ArrayList<DatosPeriodoVO>();
        try{
            String nombreTabla = ConfigurationParameter.getParameter(codMunicipio +
                MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_PERIODO, 
                MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            /**
            String sql = "Select ID, FECHA_INICIO, FECHA_FIN, PORC_SUBVENC, NUM_DIAS, BASE_COTIZACION, "
                    + " GASTO, ID_DATOS_CALCULO, REDUC_PERS_SUST, JORN_PERS_SUST, JORN_PERS_CONT, BONIFICACION"
                    + " FROM " + nombreTabla 
                    + " where ID_DATOS_CALCULO = " + codDatosCalculo;
            ***/ 
            
            con = adapt.getConnection();
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String codProcedimiento = datos[1];
            
            String sql = "SELECT ID, FECHA_INICIO, FECHA_FIN, PORC_SUBVENC, NUM_DIAS, BASE_COTIZACION, "
                    + " GASTO, ID_DATOS_CALCULO, REDUC_PERS_SUST, JORN_PERS_SUST, JORN_PERS_CONT, BONIFICACION"
                    + " ,PORC_JORN_REALIZADA, PORC_JORN_SUSTITUCION "
                    + " FROM " + nombreTabla + ",MELANBIDE01_DATOS_CALCULO "
                    + " WHERE MELANBIDE01_DATOS_CALCULO.NUM_EXPEDIENTE='" + numExpediente + "' AND MELANBIDE01_DATOS_CALCULO.COD_MUNICIPIO=" + codMunicipio 
                    + " AND MELANBIDE01_DATOS_CALCULO.EJERCICIO=" + ejercicio + " AND MELANBIDE01_DATOS_CALCULO.COD_PROCEDIMIENTO='" + codProcedimiento + "' "  
                    + " AND MELANBIDE01_DATOS_CALCULO.ID= " + nombreTabla + ".ID_DATOS_CALCULO";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                DatosPeriodoVO periodo = new DatosPeriodoVO();
                    periodo.setId(rs.getInt("ID"));
                    Calendar fechaInicio = Calendar.getInstance();
                        fechaInicio.setTime(rs.getDate("FECHA_INICIO"));
                    periodo.setFechaInicio(fechaInicio);
                    Calendar fechaFin = Calendar.getInstance();
                        fechaFin.setTime(rs.getDate("FECHA_FIN"));
                    periodo.setFechaFin(fechaFin);
                    periodo.setPorcSubven(rs.getString("PORC_SUBVENC"));
                    periodo.setNumDias(rs.getInt("NUM_DIAS"));
                    periodo.setBaseCotizacion(rs.getString("BASE_COTIZACION"));
                    periodo.setGasto(rs.getString("GASTO"));
                    periodo.setIdDatosCalculo(rs.getInt("ID_DATOS_CALCULO"));
                    periodo.setReducPerSust(rs.getString("REDUC_PERS_SUST"));
                    periodo.setJornPersSust(rs.getString("JORN_PERS_SUST"));
                    periodo.setJornPersCont(rs.getString("JORN_PERS_CONT"));
                    periodo.setBonificacion(rs.getString("BONIFICACION"));
                    periodo.setPorcJornRealizada(rs.getDouble("PORC_JORN_REALIZADA"));
                    periodo.setPorcJornSustitucion(rs.getDouble("PORC_JORN_SUSTITUCION"));
                    periodos.add(periodo);
            }//while(rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando los datos del cálculo", e);            
            throw e;
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
            if(con!=null) con.close();
        }
        
        if(log.isDebugEnabled()) log.debug("getPeriodos() : END");
        return periodos;
        
    }
    
    
    
}//class
