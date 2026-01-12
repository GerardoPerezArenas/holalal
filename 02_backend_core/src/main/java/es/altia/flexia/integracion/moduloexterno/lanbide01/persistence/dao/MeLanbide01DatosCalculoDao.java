package es.altia.flexia.integracion.moduloexterno.lanbide01.persistence.dao;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.lanbide01.util.MeLanbide01Constantes;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.DatosCalculoVO;
import es.altia.flexia.integracion.moduloexterno.lanbide01.vo.ExpedienteMeLanbide01VO;
import es.altia.util.conexion.AdaptadorSQL;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Objeto de acceso a datos que contiene las operaciones que se lanzan contra la tabla MELANBIDE01_DATOS_CALCULO
 */
public class MeLanbide01DatosCalculoDao {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide01DatosCalculoDao.class);

    //instance
    private static MeLanbide01DatosCalculoDao instance = null;
    
    //Default constructor
    private MeLanbide01DatosCalculoDao(){}

    //getInstance
    public static MeLanbide01DatosCalculoDao getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide01DatosCalculoDao.class){
                instance = new MeLanbide01DatosCalculoDao();
            }//synchronized(MeLanbide01DatosCalculo.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance

    /**
     * 
     * @param datosCalculo
     * @param con
     * @throws SQLException
     * @throws Exception 
     */
    public void insertarDatosCalculo (DatosCalculoVO datosCalculo, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertarDatosCalculo() : BEGIN");
        Statement st = null;
        Integer filasInsertadas = new Integer(0);
        try{
            String nombreTabla = ConfigurationParameter.getParameter(datosCalculo.getCodMunicipio() +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_CALCULO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            /** original
            String sql = "Insert into " + nombreTabla + " (ID, COD_MUNICIPIO, NUM_EXPEDIENTE, EJERCICIO, COD_PROCEDIMIENTO, IMPORTE_SUBVENC, MODULO)"
                    + " values (SEQ_ME_LANBIDE01_DATOS_CALCULO.NEXTVAL, " + datosCalculo.getCodMunicipio() +", '" 
                    + datosCalculo.getNumExpediente() +"', " + datosCalculo.getEjercicio() + ", '" + datosCalculo.getCodProcedimiento() + "', "
                    + datosCalculo.getImporteSubvencionado() + ", '" + datosCalculo.getNombreModulo() + "')";
            **/
            String sql = "Insert into " + nombreTabla + " (ID, COD_MUNICIPIO, NUM_EXPEDIENTE, EJERCICIO, COD_PROCEDIMIENTO, IMPORTE_SUBVENC, MODULO, DESCUENTO, TOTAL_CON_DESCUENTO)"
                    + " values (SEQ_ME_LANBIDE01_DATOS_CALCULO.NEXTVAL, " + datosCalculo.getCodMunicipio() +", '" 
                    + datosCalculo.getNumExpediente() +"', " + datosCalculo.getEjercicio() + ", '" + datosCalculo.getCodProcedimiento() + "', "
                    + datosCalculo.getImporteSubvencionado() + ", '" + datosCalculo.getNombreModulo() + "'," + datosCalculo.getDescuento() + "," + datosCalculo.getTotalConDescuento() + ")";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            filasInsertadas = st.executeUpdate(sql);
            
            if(filasInsertadas != null && filasInsertadas > 0){
                //Recuperamos el id de datos calculo para pasarselo a los periodos
                Integer idDatosCalculo = getIdDatosCalculo(datosCalculo.getNumExpediente(), datosCalculo.getCodMunicipio(), con);
                if(idDatosCalculo != null){
                    MeLanbidePeriodosDao periodosDao = MeLanbidePeriodosDao.getInstance();
                    periodosDao.insertarPeriodos(datosCalculo.getPeriodos(), datosCalculo.getCodMunicipio(), idDatosCalculo, con);
                }//if(idDatosCalculo != null)
            }//if(filasInsertadas != null && filasInsertadas > 0)
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
        if(log.isDebugEnabled()) log.debug("insertarDatosCalculo() : END");
    }//insertarDatosCalculo
    
    /**
     * 
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public Boolean existenDatosCalculo(String numExpediente, String codOrganizacion, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("existenDatosCalculo() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        Boolean existen = false;
        try{
            String nombreTabla = ConfigurationParameter.getParameter(codOrganizacion +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_CALCULO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            String sql = "Select ID "
                    + " FROM " + nombreTabla + " where COD_MUNICIPIO = " + codOrganizacion 
                    + " AND NUM_EXPEDIENTE = '" + numExpediente + "'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
               existen = true; 
            }//while(rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset y el statement");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("existenDatosCalculo() : END");
        return existen;
    }//existenDatosCalculo
    
    /**
     * 
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @throws SQLException
     * @throws Exception 
     */
    public void borrarDatosCalculo(String numExpediente, String codOrganizacion, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("borrarDatosCalculo() : BEGIN");
        Statement st = null;
        Integer filasAfectadas = null;
        try{
            String nombreTabla = ConfigurationParameter.getParameter(codOrganizacion +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_CALCULO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            //Primero comprobamos si existen periodos para borrarlos
            Integer codDatosCalculo = getIdDatosCalculo(numExpediente, codOrganizacion, con);
            if(codDatosCalculo != null){
                MeLanbidePeriodosDao periodosDao = MeLanbidePeriodosDao.getInstance();
                periodosDao.borrarPeriodos(codDatosCalculo, codOrganizacion, con);
            }//if(codDatosCalculo != null)
            
            String sql = "Delete "
                    + " FROM " + nombreTabla + " where COD_MUNICIPIO = " + codOrganizacion 
                    + " AND NUM_EXPEDIENTE = '" + numExpediente + "'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            filasAfectadas = st.executeUpdate(sql);
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset y el statement");
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("borrarDatosCalculo() : END");
    }//existenDatosCalculo
    
    /**
     * 
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public Integer getIdDatosCalculo (String numExpediente, String codOrganizacion, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("getIdDatosCalculo() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        Integer idDatosCalculo = null;
        try{
            String nombreTabla = ConfigurationParameter.getParameter(codOrganizacion +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_CALCULO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            String sql = "Select ID "
                    + " FROM " + nombreTabla + " where COD_MUNICIPIO = " + codOrganizacion 
                    + " AND NUM_EXPEDIENTE = '" + numExpediente + "'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                idDatosCalculo = rs.getInt("ID");
            }//while(rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset y el statement");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getIdDatosCalculo() : END");
        return idDatosCalculo;
    }//getIdDatosCalculo
    
    /**
     * 
     * @param numExpediente
     * @param codOrganizacion
     * @param con
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public DatosCalculoVO getDatosCalculo (String numExpediente, String codOrganizacion, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("getDatosCalculo() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        DatosCalculoVO datosCalculo = new DatosCalculoVO();
        try{
            String nombreTabla = ConfigurationParameter.getParameter(codOrganizacion +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_CALCULO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            String sql = "Select ID, COD_MUNICIPIO, NUM_EXPEDIENTE, EJERCICIO, COD_PROCEDIMIENTO, IMPORTE_SUBVENC, MODULO, DESCUENTO, TOTAL_CON_DESCUENTO "
                    + " FROM " + nombreTabla + " where COD_MUNICIPIO = " + codOrganizacion 
                    + " AND NUM_EXPEDIENTE = '" + numExpediente + "'";
        
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                datosCalculo.setId(rs.getInt("ID"));
                datosCalculo.setCodMunicipio(String.valueOf(rs.getInt("COD_MUNICIPIO")));
                datosCalculo.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                datosCalculo.setEjercicio(String.valueOf(rs.getInt("EJERCICIO")));
                datosCalculo.setCodProcedimiento(rs.getString("COD_PROCEDIMIENTO"));
                
                String gastoTotal = rs.getString("IMPORTE_SUBVENC");
                int descuento = rs.getInt("DESCUENTO");
                datosCalculo.setImporteSubvencionado(gastoTotal);
                datosCalculo.setNombreModulo(rs.getString("MODULO"));
                datosCalculo.setDescuento(descuento);
                
                double totalConDescuento = rs.getDouble("TOTAL_CON_DESCUENTO");
                if(totalConDescuento==0){
                    // Se calcula el total con descuentos restanto al importe subvencionado el importe
                    double dGastoTotal = Double.parseDouble(gastoTotal);
                    double aux = (dGastoTotal * descuento)/100;
                    double resultado = dGastoTotal - aux;
                    datosCalculo.setTotalConDescuento(resultado);
                }else
                    datosCalculo.setTotalConDescuento(totalConDescuento);
                
            }//while(rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset y el statement");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDatosCalculo() : END");
        return datosCalculo;
    }//getDatosCalculo
    
    public Integer numDiasTotalExpediente (String numExpediente, String codOrganizacion, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("numDiasTotalExpediente() : BEGIN");
        Integer numDiasTotalExpediente = new Integer(0);
        Statement st = null;
        ResultSet rs = null;
        try{
            String tablaDatosCalculo = ConfigurationParameter.getParameter(codOrganizacion +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_CALCULO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            String tablaDatosPeriodo = ConfigurationParameter.getParameter(codOrganizacion +
                    MeLanbide01Constantes.MODULO_INTEGRACION + MeLanbide01Constantes.TABLA_DATOS_PERIODO, 
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            
            /** ORIGINAL
            String sql = "Select NUM_DIAS FROM " + tablaDatosCalculo + " melanbide01_datos_calculo inner join " 
                    + tablaDatosPeriodo + " melanbide01_periodo on melanbide01_datos_calculo.id = "
                    + " melanbide01_periodo.id_datos_calculo "
                    + " AND melanbide01_datos_calculo.cod_municipio = " + codOrganizacion + " "
                    + " AND melanbide01_datos_calculo.num_Expediente = '" + numExpediente + "'";
            **/
            /** ORIGINAL 2 07/07/2014
                 String sql = "SELECT SUM(NUM_DIAS) AS NUM_DIAS FROM " + tablaDatosCalculo + " CALCULO," +  tablaDatosPeriodo + " PERIODO "
                    + " WHERE CALCULO.ID = PERIODO.ID_DATOS_CALCULO "                    
                    + " AND CALCULO.COD_MUNICIPIO=" + codOrganizacion + " "
                    + " AND CALCULO.NUM_EXPEDIENTE='" + numExpediente + "'";
            **/
            // MODIFICACION CORRIGE SUMA DIAS SI HAY VARIOS ID CALCULO PARA UN EXP - David 07/07/2014
            String sql = "SELECT SUM(NVL(NUM_DIAS,0)) AS NUM_DIAS FROM " +  tablaDatosPeriodo + " PERIODO "
                    + " INNER JOIN (SELECT " + tablaDatosCalculo + ".*,MAX(id) over (PARTITION BY COD_MUNICIPIO, EJERCICIO, COD_PROCEDIMIENTO, NUM_EXPEDIENTE) AS MAXID " 
                    + "     FROM " + tablaDatosCalculo  +") "
                    + " CALCULO ON  CALCULO.ID = PERIODO.ID_DATOS_CALCULO "
                    + " WHERE CALCULO.MAXID = PERIODO.ID_DATOS_CALCULO "                    
                    + " AND CALCULO.COD_MUNICIPIO=" + codOrganizacion + " "
                    + " AND CALCULO.NUM_EXPEDIENTE='" + numExpediente + "'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                numDiasTotalExpediente = rs.getInt("NUM_DIAS");
            }
            
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando los datos del cálculo", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el resultset y el statement");
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("numDiasTotalExpediente() : END");
        return numDiasTotalExpediente;
    }//numDiasTotalExpediente
    
    
    
    
    /**
     * Recupera los número de expedientes en los que un tercero está como interesado.
     * Los expedientes pertenecen a un procedimiento determinado
     * @param codOrganizacion: Código de la organización
     * @param codProcedimiento: Código del procedimiento
     * @param idDocumento: Tipo de documento del tercero
     * @param documento: Documento del tercero
     * return Objeto de tipo ExpedienteMeLanbide01VO
     */
    public ArrayList<ExpedienteMeLanbide01VO> getExpedientesTercero(String codOrganizacion,String codProcedimiento,String idDocumento,String documento, AdaptadorSQL adapt) throws SQLException{
        ResultSet rs = null;
        Statement st = null;
        Connection con = null;
        ExpedienteMeLanbide01VO exp = null;
        ArrayList<ExpedienteMeLanbide01VO> salida = new ArrayList<ExpedienteMeLanbide01VO>();
                
        String sql = "SELECT EXT_NUM,EXT_EJE,EXT_PRO " 
                + " FROM E_EXT,T_HTE,T_TID,E_ROL "
                + " ,e_exp "
                + " WHERE EXT_MUN= " + codOrganizacion 
                + " AND EXT_PRO = '" + codProcedimiento + "' "
                + " AND exp_mun=ext_mun and exp_eje=ext_eje and exp_pro=ext_pro and exp_num=ext_num " 
                + " AND EXT_TER=HTE_TER AND EXT_NVR=HTE_NVR " 
                + " AND HTE_TID=TID_COD " 
                + " AND EXT_ROL=ROL_COD AND ROL_MUN=" + codOrganizacion + " AND ROL_PRO = '" + codProcedimiento + "' " 
                + " AND HTE_TID = " + idDocumento
                + " AND HTE_DOC = '" + documento + "'"
                + " AND EXP_EST!=1 "  // No tener en cuenta los anulados
                ; 
        log.debug(sql);
        
        try{
            con = adapt.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                exp = new ExpedienteMeLanbide01VO();
                exp.setNumExpediente(rs.getString("EXT_NUM"));
                exp.setEjercicio(rs.getString("EXT_EJE"));
                exp.setCodProcedimiento(rs.getString("EXT_PRO"));
                salida.add(exp);
            }
            
            
        }catch(BDException e){
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
            
        }
        catch(SQLException e){
            log.debug("Error al obtener los expedientes de un deteminado interesado: " + e.getMessage());
            
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
            if(con!=null) con.close();                
        }        
        return salida;        
    }
    
    
    
    
    
    /**
     * Comprueba si asociado a un determinado expediente hay períodos ya grabados en la tabla MELANBIDE01_PERIODOS     
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente     
     * @param adapt: Objeto del tipo AdaptadorSQLBD
     * return True si existen períodos y false en caso contrario
     */
    public boolean existenPeriodosGrabados(int codOrganizacion,String numExpediente, AdaptadorSQL adapt) throws SQLException{
        ResultSet rs = null;
        Statement st = null;
        Connection con = null;
        boolean exito = false;                
        
        String[] datos = numExpediente.split("/");
        String ejercicio = datos[0];
        String codProcedimiento = datos[1];
                
        String sql = "SELECT COUNT(*) AS NUM " +                     
                    "FROM MELANBIDE01_DATOS_CALCULO, MELANBIDE01_PERIODO " +
                    "WHERE MELANBIDE01_DATOS_CALCULO.COD_MUNICIPIO=" + codOrganizacion + " " +
                    "AND MELANBIDE01_DATOS_CALCULO.NUM_EXPEDIENTE='" + numExpediente + "' " + 
                    "AND MELANBIDE01_DATOS_CALCULO.EJERCICIO=" + ejercicio + " "  + 
                    "AND MELANBIDE01_DATOS_CALCULO.COD_PROCEDIMIENTO='" + codProcedimiento + "' " +
                    "AND MELANBIDE01_DATOS_CALCULO.ID = MELANBIDE01_PERIODO.ID_DATOS_CALCULO";                    
        log.debug(sql);
        
        try{
            con = adapt.getConnection();
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            int num = 0;
            while(rs.next()){
                num = rs.getInt("NUM");
            }
            
            if(num>=1) exito = true;
            
        }catch(BDException e){
            e.printStackTrace();
            log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());            
        }
        catch(SQLException e){
            e.printStackTrace();
            log.debug("Error al obtener los expedientes de un deteminado interesado: " + e.getMessage());            
        }finally{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
            if(con!=null) con.close();
        }
        return exito;        
    }

    public void insertarDatosCampoSuplementarioNroTotalDiasConcedidos(int codOrganizacion, Integer ejercicio, String numExpediente, Integer numTotalDias, String codCampoSuplementario, boolean existeDato,Connection con) throws SQLException, Exception {
        log.error("insertarDatosCampoSuplementarioNroTotalDiasConcedidos() - DAO - : BEGIN " + numExpediente);
        Statement st = null;
        int filasInsertadas = 0;
        try {
            String nombreTabla = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.TABLA_GUARDA_CAMPO_NUMEROTOTALDIASCONCEDIDOS,
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);
            String sql ="";
            if(existeDato){
                sql = "UPDATE " + nombreTabla + " SET TNU_VALOR=" + numTotalDias + " "
                       + "WHERE "
                       + "TNU_MUN=" + codOrganizacion + " "
                       + "AND TNU_EJE=" + ejercicio + " "
                       + "AND TNU_NUM='" + numExpediente + "' "
                       + "AND TNU_COD='" + codCampoSuplementario + "'";
            }else{
                sql = "Insert into " + nombreTabla + " (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR)"
                    + " values (" + codOrganizacion + "," + ejercicio + ",'" + numExpediente + "',"
                    + "'"+ codCampoSuplementario + "'," + numTotalDias + ")";
            }

            log.error("sql = " + sql);
            
            st = con.createStatement();
            filasInsertadas = st.executeUpdate(sql);
            if (filasInsertadas > 0) {
                if(existeDato)
                    log.error("Se ha registrado correctamente el campo suplemenario numero de dias concedidos para el expedientes " + numExpediente);
                else
                    log.error("Se ha actualizado correctamente el campo suplemenario numero de dias concedidos para el expedientes " + numExpediente);
            }else{
                if(existeDato)
                    log.error("No se inserto ningun valor en el campo suplemenario numero de dias concedidos para el expedientes " + numExpediente);
                else
                    log.error("No se actualizo ningun valor en el campo suplemenario numero de dias concedidos para el expedientes " + numExpediente);
            }
        } catch (SQLException e) {
            log.error("Se ha producido una SQLException insertando/actualizando el valor del campo suplementario numero de dias concedidos al guardar datos del cálculo", e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido una Exception general insertando/actualizando el valor del campo suplementario numero de dias concedidos al guardar datos del cálculo", e);
            throw e;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el resultset");
            }
            if (st != null) {
                st.close();
            }
        }//try-catch-finally
        log.debug("insertarDatosCampoSuplementarioNroTotalDiasConcedidos() - DAO - : END " + numExpediente);
    }

    public boolean getExisteCampoSuplemenarioNumeroDiasConcedidos(int codOrganizacion, Integer ejercicio, String numExpediente, String CodigoCampoSuplementario, Connection con) throws SQLException {
        ResultSet rs = null;
        Statement st = null;
        boolean exito = false;
        
        String nombreTabla = ConfigurationParameter.getParameter(codOrganizacion + MeLanbide01Constantes.BARRA + MeLanbide01Constantes.TABLA_GUARDA_CAMPO_NUMEROTOTALDIASCONCEDIDOS,
                    MeLanbide01Constantes.FICHERO_CONFIGURACION);

        String sql = "SELECT COUNT(*) AS NUM "
                + "FROM " + nombreTabla + " "
                + "WHERE TNU_MUN=" + codOrganizacion + " "
                + "AND TNU_EJE=" + ejercicio + " "
                + "AND TNU_NUM='" + numExpediente + "' "
                + "AND TNU_COD='" + CodigoCampoSuplementario + "' "
                ;
        log.debug(sql);

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);
            int num = 0;
            while (rs.next()) {
                num = rs.getInt("NUM");
            }
            if (num >= 1) {
                exito = true;
            }

        }catch (SQLException e) {
            log.error("Error al SQLException getExisteCampoSuplemenarioNumeroDiasConcedidos MELANBIDE01 : ", e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return exito;
    }
    
    
   
}//class