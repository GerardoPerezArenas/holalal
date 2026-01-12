package es.altia.flexia.integracion.moduloexterno.melanbide68.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide68.util.MeLanbide68MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaMetadatoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocDokusiVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocLanbideExcelVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocLanbideProcedimientoExcelVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocLanbideVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocProcVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDokusiVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.GrupoTipDocVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.MetadatoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.ObjetoSGAVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.ProcedimientoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.TipDocLanbideVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.TipDocPorProcedVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.TipDokusiVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide68DAO
{
  private static Logger log = LogManager.getLogger(MeLanbide68DAO.class);
  private static MeLanbide68DAO instance = null;
  
  public static MeLanbide68DAO getInstance()
  {
    if (instance == null) {
      synchronized (MeLanbide68DAO.class)
      {
        instance = new MeLanbide68DAO();
      }
    }
    return instance;
  }
  
  public boolean existeObjetoSGA(String expedienteSGA, Connection con)
    throws Exception
  {
    int eje = Integer.parseInt(expedienteSGA.substring(0, 4));
    int num = Integer.parseInt(expedienteSGA.substring(4, 10));
    Statement st = null;
    ResultSet rs = null;
    try
    {
      String query = null;
      query = "select count(*) as NUM_REGISTRO_RES  from R_RES where RES_EJE = " + eje + " and RES_NUM =" + num + " and RES_TIP ='E'" + " and RES_SGA_EXP is not NULL";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      int numRegistroRes = 0;
      if (rs.next()) {
        numRegistroRes = rs.getInt("NUM_REGISTRO_RES");
      }
      return numRegistroRes > 0;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error comprobando si exite Registro RES con numero " + num, ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (rs != null) {
        rs.close();
      }
      if (st != null) {
        st.close();
      }
    }
  }
  
  public boolean existeRegistroRES(String expedienteSGA, Connection con)
    throws NumberFormatException, Exception
  {
    Statement st = null;
    ResultSet rs = null;
    int num = 0;
    try
    {
      int eje = Integer.parseInt(expedienteSGA.substring(0, 4));
      num = Integer.parseInt(expedienteSGA.substring(4, 10));
      String query = null;
      query = "select count(*) as NUM_REGISTRO_RES  from R_RES where RES_EJE = " + eje + " and RES_NUM =" + num + " and RES_TIP ='E'";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      int numRegistroRes = 0;
      if (rs.next()) {
        numRegistroRes = rs.getInt("NUM_REGISTRO_RES");
      }
      return numRegistroRes > 0;
    }
    catch (NumberFormatException nfe)
    {
      log.error("Las 10 primeras cifras del expediente " + expedienteSGA + " no son número. ", nfe);
      throw nfe;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error comprobando si exite Registro RES con numero " + num, ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (rs != null) {
        rs.close();
      }
      if (st != null) {
        st.close();
      }
    }
  }
  
  public void modificarObjetoSGA(ObjetoSGAVO o, Connection con)
    throws Exception
  {
    int eje = Integer.parseInt(o.getExpedienteSGA().substring(0, 4));
    int num = Integer.parseInt(o.getExpedienteSGA().substring(4, 10));
    PreparedStatement ps = null;
    ResultSet rs = null;
    try
    {
      String query = "UPDATE R_RES SET RES_SGA_COD=?, RES_SGA_EXP=?  WHERE RES_EJE =? and RES_NUM =? and RES_TIP ='E'";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      ps = con.prepareStatement(query);
      
      log.debug("parametro 1 = " + o.getCodigoSGA());
      ps.setString(1, o.getCodigoSGA());
      log.debug("parametro 2 = " + o.getExpedienteSGA());
      ps.setString(2, o.getExpedienteSGA());
      log.debug("parametro 3 = " + eje);
      ps.setInt(3, eje);
      log.debug("parametro 4 = " + num);
      ps.setInt(4, num);
      
      rs = ps.executeQuery();
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error al modificar el registro RES con número " + num, ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (rs != null) {
        rs.close();
      }
      if (ps != null) {
        ps.close();
      }
    }
  }
  
  public int insertarProcesoCargaSGA(String nombreFichero, Date fechaInicioProceso, Connection con)
    throws Exception
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    int idSecuencia;
    try
    {
      idSecuencia = getNextId("SEQ_PROCESO_CARGA_SGA", con).intValue();
      String query = "INSERT into PROCESO_CARGA_SGA (ID,NOMBRE_FICHERO,INICIO,FIN,RESULTADO,REGISTROS_PROCESADOS,DETALLE_ERROR,OBSERVACIONES)values (?,?,?,null,null,null,null,null)";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      ps = con.prepareStatement(query);
      log.debug("parametro 1 = " + idSecuencia);
      ps.setInt(1, idSecuencia);
      log.debug("parametro 2 = " + nombreFichero);
      ps.setString(2, nombreFichero);
      log.debug("parametro 3 = " + formateadorFecha.format(fechaInicioProceso));
      ps.setTimestamp(3, new Timestamp(fechaInicioProceso.getTime()));
      rs = ps.executeQuery();
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error al insertar el proceso de carga SGA ", ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (rs != null) {
        rs.close();
      }
      if (ps != null) {
        ps.close();
      }
    }
    return idSecuencia;
  }
  
  public void modificarProcesoCargaSGA(int idSecuencia, Date fechaFinProceso, String resultadoProceso, int registrosProcesadosInsertados, int registrosProcesadosModificados, int registrosProcesadosNOOK, String mensajeErrorProceso, Connection con)
    throws Exception
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    int registrosProcesados = registrosProcesadosInsertados + registrosProcesadosModificados + registrosProcesadosNOOK;
    String observacionesProceso = "Nº Expedientes tratados: " + registrosProcesados + ". ";
    if (registrosProcesadosInsertados > 0) {
      observacionesProceso = observacionesProceso + "Nº Expedientes insertados: " + registrosProcesadosInsertados + ". ";
    }
    if (registrosProcesadosModificados > 0) {
      observacionesProceso = observacionesProceso + "Nº Expedientes modificados: " + registrosProcesadosModificados + ". ";
    }
    if (registrosProcesadosNOOK > 0) {
      observacionesProceso = observacionesProceso + "Nº Expedientes incorrectos: " + registrosProcesadosNOOK + ". ";
    }
    try
    {
      String query = "UPDATE PROCESO_CARGA_SGA SET FIN=?, RESULTADO=?, REGISTROS_PROCESADOS=?, DETALLE_ERROR=?, OBSERVACIONES=? where ID=?";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      ps = con.prepareStatement(query);
      
      log.debug("parametro 1 = " + formateadorFecha.format(fechaFinProceso));
      ps.setTimestamp(1, new Timestamp(fechaFinProceso.getTime()));
      log.debug("parametro 2 = " + resultadoProceso);
      ps.setString(2, resultadoProceso);
      log.debug("parametro 3 = " + registrosProcesados);
      ps.setInt(3, registrosProcesados);
      log.debug("parametro 4 = " + mensajeErrorProceso);
      ps.setString(4, mensajeErrorProceso);
      log.debug("parametro 5 = " + observacionesProceso);
      ps.setString(5, observacionesProceso);
      log.debug("parametro 6 = " + idSecuencia);
      ps.setInt(6, idSecuencia);
      rs = ps.executeQuery();
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error al modificar el proceso de carga SGA ", ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (rs != null) {
        rs.close();
      }
      if (ps != null) {
        ps.close();
      }
    }
  }
  
  public void insertarRegistroCargaSGA(Integer idSecuenciaProcesoCargaSGA, int filaExcel, String codSGA, Date fechaProceso, int resultado, String detalleError, Connection con)
    throws Exception
  {
    PreparedStatement ps = null;
    ResultSet rs = null;
    SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    try
    {
      int idSecuencia = getNextId("SEQ_REGISTRO_CARGA_SGA", con).intValue();
      String query = "INSERT into REGISTRO_CARGA_SGA (ID, ID_PROCESO, FILA_EXCEL, COD_SGA, FECHA_PROCESO, RESULTADO, ERROR)values (?,?,?,?,?,?,?)";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      ps = con.prepareStatement(query);
      log.debug("parametro 1 = " + idSecuencia);
      ps.setInt(1, idSecuencia);
      log.debug("parametro 2 = " + idSecuenciaProcesoCargaSGA);
      ps.setInt(2, idSecuenciaProcesoCargaSGA.intValue());
      log.debug("parametro 3 = " + filaExcel);
      ps.setInt(3, filaExcel);
      log.debug("parametro 4 = " + codSGA);
      ps.setString(4, codSGA);
      log.debug("parametro 5 = " + formateadorFecha.format(fechaProceso));
      ps.setTimestamp(5, new Timestamp(fechaProceso.getTime()));
      log.debug("parametro 6 = " + resultado);
      ps.setInt(6, resultado);
      log.debug("parametro 7 = " + detalleError);
      ps.setString(7, detalleError);
      
      rs = ps.executeQuery();
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error al insertar el registro de Carga SGA, ", ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (rs != null) {
        rs.close();
      }
      if (ps != null) {
        ps.close();
      }
    }
  }
  
  private Integer getNextId(String seqName, Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    Integer numSec = null;
    try
    {
      String query = null;
      
      query = "select " + seqName + ".nextval from dual";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      if (rs.next())
      {
        numSec = Integer.valueOf(rs.getInt(1));
        if (rs.wasNull()) {
          throw new Exception();
        }
      }
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error generando el siguiente valor de la secuencia " + seqName, ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (rs != null) {
        rs.close();
      }
      if (st != null) {
        st.close();
      }
    }
    return numSec;
  }
  
    public List<FilaTipDocLanbideVO> getListaTiposDocumentales(Connection con)
            throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaTipDocLanbideVO> tiposDocumentales = new ArrayList();
        try {
            String query = null;
            query = "SELECT "
                    + "L.TIPDOC_ID AS TIPDOC_ID, "
                    + "L.CODTIPDOC AS CODTIPDOC, "
                    + "L.COD_GRUPO_TIPDOC AS COD_GRUPO_TIPDOC, "
                    + "L.TIPDOC_LANBIDE_ES AS TIPDOC_LANBIDE_ES, "
                    + "L.TIPDOC_LANBIDE_EU AS TIPDOC_LANBIDE_EU, "
                    + "L.DESCTIPDOC_LANBIDE_ES AS DESCTIPDOC_LANBIDE_ES, "
                    + "L.DESCTIPDOC_LANBIDE_EU AS DESCTIPDOC_LANBIDE_EU, "
                    + "D.TIPDOC_DOKUSI AS TIPDOC_DOKUSI, "
                    + "CASE WHEN M.TIPDOC_ID IS NOT NULL THEN 'S' ELSE 'N' END AS TIENE_METADATO, "
                    + "L.FECHA_BAJA AS FECHA_BAJA "
                    + "FROM MELANBIDE68_TIPDOC_LANBIDE L "
                    + "LEFT JOIN MELANBIDE68_DOKUSI_TIPDOC_LANB D ON L.TIPDOC_ID = D.TIPDOC_ID "
                    + "LEFT JOIN MELANBIDE68_DOKUSI_METADATOS M ON L.TIPDOC_ID = M.TIPDOC_ID AND M.FECHA_BAJA IS NULL "
                    + "ORDER BY CODTIPDOC ASC";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                tiposDocumentales.add((FilaTipDocLanbideVO) MeLanbide68MappingUtils.getInstance().map(rs, FilaTipDocLanbideVO.class));
            }
            return tiposDocumentales;
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando los tipos documentales de Lanbide ", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
  
  public FilaTipDocLanbideVO getTipoDocumentalById(int id, boolean porCodigo, Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    FilaTipDocLanbideVO tipoDocumental = new FilaTipDocLanbideVO();
    try
    {
      String parteWhere = null;  
        if(porCodigo){
            parteWhere ="WHERE CODTIPDOC ="  + id ;
        } else {
            parteWhere ="WHERE TIPDOC_ID =" + id ; 
        }
      String query = "SELECT TIPDOC_ID,CODTIPDOC,COD_GRUPO_TIPDOC,TIPDOC_LANBIDE_ES,TIPDOC_LANBIDE_EU,DESCTIPDOC_LANBIDE_ES,DESCTIPDOC_LANBIDE_EU,' ' AS TIPDOC_DOKUSI, ' ' AS TIENE_METADATO,FECHA_BAJA  FROM MELANBIDE68_TIPDOC_LANBIDE " + parteWhere;
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        tipoDocumental = (FilaTipDocLanbideVO)MeLanbide68MappingUtils.getInstance().map(rs, FilaTipDocLanbideVO.class);
      }
      return tipoDocumental;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando el tipo documental de Lanbide de Id: " + id, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public FilaTipDocLanbideVO getTipoDocumentalByCodigo(int codigo, Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    FilaTipDocLanbideVO tipoDocumental = new FilaTipDocLanbideVO();
    try
    {
      String query = null;
      query = "SELECT TIPDOC_ID,CODTIPDOC,COD_GRUPO_TIPDOC,TIPDOC_LANBIDE_ES,TIPDOC_LANBIDE_EU,DESCTIPDOC_LANBIDE_ES,DESCTIPDOC_LANBIDE_EU,' ' AS TIPDOC_DOKUSI, ' ' AS TIENE_METADATO,FECHA_BAJA  FROM MELANBIDE68_TIPDOC_LANBIDE  WHERE CODTIPDOC =" + codigo;
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        tipoDocumental = (FilaTipDocLanbideVO)MeLanbide68MappingUtils.getInstance().map(rs, FilaTipDocLanbideVO.class);
      }
      return tipoDocumental;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando el tipo documental de Lanbide de código: " + codigo, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public int eliminarTipDocLanbide(FilaTipDocLanbideVO p, Connection con)
    throws SQLException
  {
    Statement st = null;
    try
    {
      String query = null;
      query = "delete from MELANBIDE68_TIPDOC_LANBIDE  WHERE TIPDOC_ID =" + p.getId();
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (SQLException ex)
    {
      log.error("Se ha producido un error eliminando tipo documental  " + (p != null ? Integer.valueOf(p.getTipDocID()) : "tipo documental = null"), ex);
      throw ex;
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (SQLException e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new SQLException(e);
      }
    }
  }
  
  public int deshabilitarTipDocLanbide(FilaTipDocLanbideVO p, Connection con)
    throws SQLException
  {
    Statement st = null;
    try
    {
      String query = null;
      if (p.getDeshabilitado() == "N") {
        query = "UPDATE MELANBIDE68_TIPDOC_LANBIDE SET FECHA_BAJA           = SYSDATE WHERE TIPDOC_ID =" + p.getId();
      } else {
        query = "UPDATE MELANBIDE68_TIPDOC_LANBIDE SET FECHA_BAJA           = '' WHERE TIPDOC_ID =" + p.getId();
      }
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (SQLException ex)
    {
      log.error("Se ha producido un error habilitando/deshabilitando tipo documental  " + (p != null ? Integer.valueOf(p.getTipDocID()) : "tipo documental = null"), ex);
      throw ex;
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (SQLException e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new SQLException(e);
      }
    }
  }
  
  public int modificartipDocLanbide(FilaTipDocLanbideVO p, Connection con)
    throws Exception
  {
    Statement st = null;
    try
    {
      String query = null;
      query = "UPDATE MELANBIDE68_TIPDOC_LANBIDE SET CODTIPDOC = " + p.getTipDocID() + "," + "     COD_GRUPO_TIPDOC = '" + p.getCodGrupo() + "'," + "     TIPDOC_LANBIDE_ES = '" + p.getTipDocLanbide_es() + "'," + "     TIPDOC_LANBIDE_EU = '" + p.getTipDocLanbide_eu() + "'," + "     DESCTIPDOC_LANBIDE_ES = '" + p.getTipDocLanbide_es_L() + "'," + "     DESCTIPDOC_LANBIDE_EU = '" + p.getTipDocLanbide_eu_L() + "'" + " where TIPDOC_ID       = " + p.getId();
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error modificando tipo documental Lanbide  " + (p != null ? Integer.valueOf(p.getTipDocID()) : "tipo documental = null"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public FilaTipDocLanbideVO guardartipDocLanbideVO(FilaTipDocLanbideVO tipDocLanbide, Connection con)
    throws Exception
  {
    Statement st = null;
    try
    {
        String query = null;
        int id = recogerIDInsertar("SEQ_MELANBIDE68_TIPDOC_LANBIDE", con);
        int codTipDoc = recogerCodTipDoc(con);
        query = "insert into MELANBIDE68_TIPDOC_LANBIDE (TIPDOC_ID, CODTIPDOC, COD_GRUPO_TIPDOC, TIPDOC_LANBIDE_ES, TIPDOC_LANBIDE_EU, DESCTIPDOC_LANBIDE_ES, DESCTIPDOC_LANBIDE_EU) "
              + "values(" + id + ", " + codTipDoc + ", '" + tipDocLanbide.getCodGrupo() + "', '" + tipDocLanbide.getTipDocLanbide_es() + "', '" + tipDocLanbide.getTipDocLanbide_eu() + "', '" + tipDocLanbide.getTipDocLanbide_es_L() + "', '" + tipDocLanbide.getTipDocLanbide_eu_L() + "')";
        tipDocLanbide.setId(id);
        tipDocLanbide.setTipDocID(codTipDoc);

        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }

        // Ejecutar la consulta
        st = con.createStatement();
        int res = st.executeUpdate(query);
      FilaTipDocLanbideVO localFilaTipDocLanbideVO;
        if (res > 0) {
            return tipDocLanbide;
        }
        return null;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error guardando los datos del tipo documental " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(tipo documental = null)"), ex);
        throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
}
        if (st != null) {
            st.close();
        }
    }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
}
    }
  }


  public List<FilaTipDocDokusiVO> getListaTiposDocumentalesDokusi(Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    List<FilaTipDocDokusiVO> tiposDocumentalesDokusi = new ArrayList();
    try
    {
      String query = null;
      query = "SELECT D.TIPDOC_ID         TIPDOC_ID,       L.TIPDOC_LANBIDE_ES TIPDOC_LANBIDE,       D.TIPDOC_DOKUSI     TIPDOC_DOKUSI FROM MELANBIDE68_DOKUSI_TIPDOC_LANB D, MELANBIDE68_TIPDOC_LANBIDE L WHERE D.TIPDOC_ID = L.TIPDOC_ID ORDER BY L.TIPDOC_LANBIDE_ES ASC ";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        tiposDocumentalesDokusi.add((FilaTipDocDokusiVO)MeLanbide68MappingUtils.getInstance().map(rs, FilaTipDocDokusiVO.class));
      }
      return tiposDocumentalesDokusi;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando la relación de tipos documentales Lanbide-DOKUSI ", ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public FilaTipDocDokusiVO getTipoDocumentalDokusi(int codTipDoc, Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    FilaTipDocDokusiVO tipoDocumentalDokusi = new FilaTipDocDokusiVO();
    try
    {
      String query = null;
      query = "SELECT D.TIPDOC_ID         TIPDOC_ID,       L.TIPDOC_LANBIDE_ES TIPDOC_LANBIDE,       D.TIPDOC_DOKUSI     TIPDOC_DOKUSI FROM MELANBIDE68_DOKUSI_TIPDOC_LANB D, MELANBIDE68_TIPDOC_LANBIDE L WHERE D.TIPDOC_ID = L.TIPDOC_ID    AND D.TIPDOC_ID =" + codTipDoc;
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        tipoDocumentalDokusi = (FilaTipDocDokusiVO)MeLanbide68MappingUtils.getInstance().map(rs, FilaTipDocDokusiVO.class);
      }
      return tipoDocumentalDokusi;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando la relación de tipo documental Lanbide-DOKUSI " + codTipDoc, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public int eliminarTipDocDokusi(FilaTipDocDokusiVO p, Connection con)
    throws SQLException
  {
    Statement st = null;
    try
    {
      String query = null;
      query = "delete from MELANBIDE68_DOKUSI_TIPDOC_LANB  WHERE TIPDOC_ID =" + p.getCodTipDoc();
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (SQLException ex)
    {
      log.error("Se ha producido un error eliminando la relación de tipo documental Lanbide-DOKUSI  " + (p != null ? Integer.valueOf(p.getCodTipDoc()) : "relación Lanbide-DOKUSI = null"), ex);
      throw ex;
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (SQLException e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new SQLException(e);
      }
    }
  }
  
  public int modificartipDocDokusi(FilaTipDocDokusiVO p, Connection con)
    throws Exception
  {
    Statement st = null;
    try
    {
      String query = null;
      query = "UPDATE MELANBIDE68_DOKUSI_TIPDOC_LANB SET TIPDOC_DOKUSI = '" + p.getTipDocDokusi() + "'" + " where TIPDOC_ID   = " + p.getCodTipDoc();
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error modificando la relación tipo documental Lanbide-DOKUSI  " + (p != null ? Integer.valueOf(p.getCodTipDoc()) : "relación Lanbide-DOKUSI = null"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public FilaTipDocDokusiVO guardartipDocDokusiVO(FilaTipDocDokusiVO tipDocDokusi, Connection con)
    throws SQLException
  {
    Statement st = null;
    try
    {
      String query = null;

      query = "insert into MELANBIDE68_DOKUSI_TIPDOC_LANB (TIPDOC_ID, TIPDOC_DOKUSI) values(" + tipDocDokusi.getCodTipDoc() + ", '" + tipDocDokusi.getTipDocDokusi() + "')";
        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }
        st = con.createStatement();
        int res = st.executeUpdate(query);
      FilaTipDocDokusiVO localFilaTipDocDokusiVO;
        if (res > 0) {
            return tipDocDokusi;
        }
        return null;
    }
    catch (SQLException ex)
    {
      log.error("Se ha producido un error guardando la relación tipo documental Lanbide-DOKUSI  " + (tipDocDokusi != null ? Integer.valueOf(tipDocDokusi.getCodTipDoc()) : "(relación Lanbide-DOKUSI = null)"), ex);
        throw ex;
    }
    finally
    {
      try
      {
            if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
      }
      catch (SQLException e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
            throw new SQLException(e);
        }
    }
}

  public List<TipDocLanbideVO> getTiposDoc(Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    List<TipDocLanbideVO> listaTiposDoc = new ArrayList();
    TipDocLanbideVO tipDoc = new TipDocLanbideVO();
    try
    {
      String query = null;
      query = "SELECT CODTIPDOC, TIPDOC_ID,TIPDOC_LANBIDE_ES  FROM MELANBIDE68_TIPDOC_LANBIDE ORDER BY TIPDOC_LANBIDE_ES ASC ";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next())
      {
        tipDoc = (TipDocLanbideVO)MeLanbide68MappingUtils.getInstance().map(rs, TipDocLanbideVO.class);
        listaTiposDoc.add(tipDoc);
        tipDoc = new TipDocLanbideVO();
      }
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando los tipos documentales Lanbide ", ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (st != null) {
        st.close();
      }
      if (rs != null) {
        rs.close();
      }
    }
    return listaTiposDoc;
  }
  
    
  public List<FilaMetadatoVO> getListaMetadatosporTipodocumental(int codTipDoc, Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    List<FilaMetadatoVO> metadatos = new ArrayList();
    try
    {
      String query = null;
        query
                = "SELECT M.TIPDOC_ID_METADATO, "
                + "       M.TIPDOC_METADATODCTM, "
                + "       M.TIPDOC_OBLIGATORIO, "
                + "       CASE WHEN M.FECHA_BAJA IS NOT NULL THEN 'S' ELSE 'N' END AS DESHABILITADO "
                + "FROM MELANBIDE68_DOKUSI_METADATOS M "
                + "JOIN MELANBIDE68_TIPDOC_LANBIDE L ON M.TIPDOC_ID = L.TIPDOC_ID "
                + "WHERE L.CODTIPDOC = " + codTipDoc + " "
                + "ORDER BY M.TIPDOC_ID_METADATO";
        if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        metadatos.add((FilaMetadatoVO)MeLanbide68MappingUtils.getInstance().map(rs, FilaMetadatoVO.class));
      }
      return metadatos;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando metadatos para tipo documental Lanbide " + codTipDoc, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public MetadatoVO getTipodocumentalyMetadato(int codTipDoc, String codMetadato, Connection con)
    throws Exception
{
    PreparedStatement pst = null;
    ResultSet rs = null;
    MetadatoVO metadato = new MetadatoVO();
    try {
        String query = "SELECT M.TIPDOC_ID, "
                + "       M.TIPDOC_ID_METADATO, "
                + "       M.TIPDOC_METADATODCTM, "
                + "       M.TIPDOC_OBLIGATORIO, "
                + "       M.FECHA_BAJA "
                + "FROM MELANBIDE68_DOKUSI_METADATOS M "
                + "JOIN MELANBIDE68_TIPDOC_LANBIDE L ON M.TIPDOC_ID = L.TIPDOC_ID "
                + "WHERE L.CODTIPDOC = ? AND M.TIPDOC_ID_METADATO = ?";
        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }
        pst = con.prepareStatement(query);
        pst.setInt(1, codTipDoc);
        pst.setString(2, codMetadato);
        // Log de los parámetros
        if (log.isDebugEnabled()) {
            log.debug("Parámetro 1 (CODTIPDOC): " + codTipDoc);
            log.debug("Parámetro 2 (TIPDOC_ID_METADATO): " + codMetadato);
        }

        rs = pst.executeQuery();
        while (rs.next()) {
            metadato = (MetadatoVO) MeLanbide68MappingUtils.getInstance().map(rs, MetadatoVO.class);
        }
        return metadato;
    }
    catch (Exception ex)
    {
        log.error("Se ha producido un error recuperando metadato " + codMetadato + " para el tipo documental " + codTipDoc, ex);
        throw new Exception(ex);
    }
    finally
    {
        try
        {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        catch (Exception e)
        {
            log.error("Se ha producido un error cerrando el statement y el resultset", e);
            throw new Exception(e);
        }
    }
}

  
  public int eliminarMetadato(MetadatoVO p, Connection con)
    throws Exception
  {
    Statement st = null;
    try
    {
      String query = null;
      query = "delete from MELANBIDE68_DOKUSI_METADATOS where TIPDOC_ID          = " + p.getTipoDocumental() + " and   TIPDOC_ID_METADATO = '" + p.getMetadato() + "'";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error eliminando metadato  " + (p != null ? p.getMetadato() : "metadato = null") + " para el tipo documental " + (p != null ? Integer.valueOf(p.getTipoDocumental()) : "metadato = null"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public int deshabilitarMetadato(MetadatoVO p, Connection con)
    throws Exception
  {
    Statement st = null;
    try
    {
      String query = null;
      if (p.getFecha_baja() == null) {
        query = "UPDATE MELANBIDE68_DOKUSI_METADATOS SET FECHA_BAJA           = SYSDATE where TIPDOC_ID          = " + p.getTipoDocumental() + " and   TIPDOC_ID_METADATO = '" + p.getMetadato() + "'";
      } else {
        query = "UPDATE MELANBIDE68_DOKUSI_METADATOS SET FECHA_BAJA           = '' where TIPDOC_ID          = " + p.getTipoDocumental() + " and   TIPDOC_ID_METADATO = '" + p.getMetadato() + "'";
      }
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error habilitando/deshabilitando metadato  " + (p != null ? p.getMetadato() : "metadato = null") + " para el tipo documental " + (p != null ? Integer.valueOf(p.getTipoDocumental()) : "metadato = null"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public int modificarMetadato(MetadatoVO p, Connection con)
    throws Exception
  {
    Statement st = null;
    try
    {
      String query = null;
      query = "UPDATE MELANBIDE68_DOKUSI_METADATOS SET TIPDOC_OBLIGATORIO   = '" + p.getObligatorio() + "'," + "     TIPDOC_METADATODCTM  = '" + p.getMetadatoDCTM() + "'" + " where TIPDOC_ID          = " + p.getTipoDocumental() + " and   TIPDOC_ID_METADATO = '" + p.getMetadato() + "'";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error modificando metadato  " + (p != null ? p.getMetadato() : "metadato = null") + " para el tipo documental " + (p != null ? Integer.valueOf(p.getTipoDocumental()) : "metadato = null"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public MetadatoVO guardarMetadatoVO(MetadatoVO metadato, Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    try
    {      
      String query = null;
      String queryBuscarId = "SELECT TIPDOC_ID FROM MELANBIDE68_TIPDOC_LANBIDE WHERE CODTIPDOC = '" + metadato.getTipoDocumental() + "'";
      int tipDocId= 0;
      // Buscar el TIPDOC_ID correspondiente a coddoc
        if (log.isDebugEnabled()) {
            log.debug("sql buscar TIPDOC_ID = " + queryBuscarId);
        }
        st = con.createStatement();
        rs = st.executeQuery(queryBuscarId);

        if (rs.next()) {
            tipDocId = rs.getInt("TIPDOC_ID");
        } else {
            log.warn("No se encontró TIPDOC_ID para CODTIPDOC: " + metadato.getTipoDocumental());
            return null;
        }
      
      
      query = "insert into MELANBIDE68_DOKUSI_METADATOS (TIPDOC_ID, TIPDOC_ID_METADATO, TIPDOC_OBLIGATORIO, TIPDOC_METADATODCTM) values(" + tipDocId + ", '" + metadato.getMetadato() + "', '" + metadato.getObligatorio() + "', '" + metadato.getMetadatoDCTM() + "')";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      int res = st.executeUpdate(query);
      MetadatoVO localMetadatoVO;
      if (res > 0) {
        return metadato;
      }
      return null;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error guardando los datos del metadato " + (metadato != null ? metadato.getMetadato() : "(metadato = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
public List<FilaTipDokusiVO> getListaTiposDokusi(Connection con) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    List<FilaTipDokusiVO> tiposDokusi = new ArrayList<FilaTipDokusiVO>();
    try {
        String query = "SELECT " +
                       "    D.TIPDOC_DOKUSI, " +
                       "    D.TIPDOC_DOKUSI_PADRE, " +
                       "    D.TIPDOC_DOKUSI_FAMILIA, " +
                       "    D.TIPDOC_DOKUSI_ES, " +
                       "    D.TIPDOC_DOKUSI_EU, " +
                       "    TL.CODTIPDOC " +
                       "FROM MELANBIDE68_TIPDOC_DOKUSI D " +
                       "LEFT JOIN MELANBIDE68_DOKUSI_TIPDOC_LANB L " +
                       "    ON D.TIPDOC_DOKUSI = L.TIPDOC_DOKUSI " +
                       "LEFT JOIN MELANBIDE68_TIPDOC_LANBIDE TL " +
                       "    ON L.TIPDOC_ID = TL.TIPDOC_ID " +
                       "ORDER BY D.TIPDOC_DOKUSI";

        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }
        st = con.createStatement();
        rs = st.executeQuery(query);
        while (rs.next()) {
        tiposDokusi.add((FilaTipDokusiVO)MeLanbide68MappingUtils.getInstance().map(rs, FilaTipDokusiVO.class));
        }
        return tiposDokusi;
    } catch (Exception ex) {
        log.error("Se ha producido un error recuperando los tipos documentales DOKUSI ", ex);
        throw new Exception(ex);
    } finally {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) st.close();
            if (rs != null) rs.close();
        } catch (Exception e) {
            log.error("Se ha producido un error cerrando el statement y el resulset", e);
            throw new Exception(e);
        }
    }
}

  public FilaTipDokusiVO getTipoDokusi(String codTipDokusi, Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    FilaTipDokusiVO tipoDokusi = new FilaTipDokusiVO();
    try
    {
      String query = null;
      query = "SELECT TIPDOC_DOKUSI,TIPDOC_DOKUSI_FAMILIA,TIPDOC_DOKUSI_ES,TIPDOC_DOKUSI_EU, '' AS CODTIPDOC, '' AS TIPDOC_DOKUSI_PADRE   FROM MELANBIDE68_TIPDOC_DOKUSI WHERE TIPDOC_DOKUSI ='" + codTipDokusi + "'";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        tipoDokusi = (FilaTipDokusiVO)MeLanbide68MappingUtils.getInstance().map(rs, FilaTipDokusiVO.class);
      }
      return tipoDokusi;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando el tipo documental Dokusi " + codTipDokusi, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public int eliminarTipDokusi(FilaTipDokusiVO p, Connection con)
    throws SQLException
  {
    Statement st = null;
    try
    {
      String query = null;
      query = "delete from MELANBIDE68_TIPDOC_DOKUSI  WHERE TIPDOC_DOKUSI ='" + p.getCodDokusi() + "'";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (SQLException ex)
    {
      log.error("Se ha producido un error eliminando tipo documental Dokusi  " + (p != null ? p.getCodDokusi() : "tipo documental = null"), ex);
      throw ex;
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (SQLException e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new SQLException(e);
      }
    }
  }
  
  public int modificartipDokusi(FilaTipDokusiVO p, Connection con)
    throws Exception
  {
    Statement st = null;
    try
    {
      String query = null;
      query = "UPDATE MELANBIDE68_TIPDOC_DOKUSI SET TIPDOC_DOKUSI_PADRE = '" + p.getCodDokusiPadre() + "'," + "     TIPDOC_DOKUSI_FAMILIA = " + p.getCodDokusiFamilia() + "," + "     TIPDOC_DOKUSI_ES = '" + p.getDesDokusi_es() + "'," + "     TIPDOC_DOKUSI_EU = '" + p.getDesDokusi_eu() + "'" + " where TIPDOC_DOKUSI   =  '" + p.getCodDokusi() + "'";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error modificando tipo documental Dokusi  " + (p != null ? p.getCodDokusi() : "tipo documental = null"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public FilaTipDokusiVO guardartipDokusiVO(FilaTipDokusiVO tipDokusi, Connection con)
    throws Exception
  {
    Statement st = null;
    try
    {
      String query = null;
      
      query = "insert into MELANBIDE68_TIPDOC_DOKUSI (TIPDOC_DOKUSI, TIPDOC_DOKUSI_PADRE, TIPDOC_DOKUSI_FAMILIA, TIPDOC_DOKUSI_ES, TIPDOC_DOKUSI_EU) values('" + tipDokusi.getCodDokusi() + "', '" + tipDokusi.getCodDokusiPadre() + "', " + tipDokusi.getCodDokusiFamilia() + ", '" + tipDokusi.getDesDokusi_es() + "', '" + tipDokusi.getDesDokusi_eu() + "')";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      int res = st.executeUpdate(query);
      FilaTipDokusiVO localFilaTipDokusiVO;
      if (res > 0) {
        return tipDokusi;
      }
      return null;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error guardando los datos del tipo documental " + (tipDokusi != null ? tipDokusi.getCodDokusi() : "(tipo documental = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public List<TipDokusiVO> getTiposDokusi(Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    List<TipDokusiVO> listaTiposDokusi = new ArrayList();
    TipDokusiVO tipDokusi = new TipDokusiVO();
    try
    {
      String query = null;
      query = "SELECT TIPDOC_DOKUSI,TIPDOC_DOKUSI_ES  FROM MELANBIDE68_TIPDOC_DOKUSI ORDER BY TIPDOC_DOKUSI_ES ASC ";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next())
      {
        tipDokusi = (TipDokusiVO)MeLanbide68MappingUtils.getInstance().map(rs, TipDokusiVO.class);
        listaTiposDokusi.add(tipDokusi);
        tipDokusi = new TipDokusiVO();
      }
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando los tipos documentales Dokusi ", ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (st != null) {
        st.close();
      }
      if (rs != null) {
        rs.close();
      }
    }
    return listaTiposDokusi;
  }
  
  public List<GrupoTipDocVO> getGruposTipDoc(Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    List<GrupoTipDocVO> listaGruposTipDoc = new ArrayList();
    GrupoTipDocVO grupoTipDoc = new GrupoTipDocVO();
    try
    {
      String query = null;
      query = "SELECT DES_VAL_COD,DES_NOM FROM E_DES_VAL WHERE DES_COD='GRTD' AND DES_VAL_ESTADO='A' ORDER BY DES_VAL_COD ";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next())
      {
        grupoTipDoc = (GrupoTipDocVO)MeLanbide68MappingUtils.getInstance().map(rs, GrupoTipDocVO.class);
        listaGruposTipDoc.add(grupoTipDoc);
        grupoTipDoc = new GrupoTipDocVO();
      }
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando los grupos de tipos documentales ", ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (st != null) {
        st.close();
      }
      if (rs != null) {
        rs.close();
      }
    }
    return listaGruposTipDoc;
  }
public List<ProcedimientoVO> getProcedimientos(Connection con) throws Exception {
    return getProcedimientos(con, false);
}
public List<ProcedimientoVO> getProcedimientos(Connection con, boolean soloValidos) throws Exception {
    log.error("Entrando a DAO.getProcedimientos");
    Statement st = null;
    ResultSet rs = null;
    List<ProcedimientoVO> listProc = new ArrayList<ProcedimientoVO>();

    try {
         
        String query = "SELECT pro_cod, pro_des FROM E_PRO ";
        if(soloValidos){
            query += "WHERE PRO_FLH is null ";
        }
        
        query += "ORDER BY PRO_COD";
        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }
        st = con.createStatement();
        rs = st.executeQuery(query);

        // Iterar sobre los resultados de la consulta
        while (rs.next()) {
            ProcedimientoVO procedimiento = (ProcedimientoVO) MeLanbide68MappingUtils
                    .getInstance()
                    .map(rs, ProcedimientoVO.class);
            listProc.add(procedimiento);
        }


    }catch (Exception ex) {
        log.error("Se ha producido un error recuperando Procedimientos", ex);
        throw new Exception(ex);
    } finally {
        if (log.isDebugEnabled()) {
            log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
            st.close();
        }
        if (rs != null) {
            rs.close();
        }
    }
    return listProc;
}

  public List<FilaTipDocProcVO> getListaTiposDocporProcedimiento(String codProc, boolean porCodigo, Connection con)
    throws Exception
{
    Statement st = null;
    ResultSet rs = null;
    List<FilaTipDocProcVO> tiposDoc = new ArrayList();
    try
    {
        porCodigo = false;
        String parteOn = null;
        if (porCodigo) {
            parteOn = " ON MELANBIDE68_TIPDOC_PROC.TIPDOC_ID = MELANBIDE68_TIPDOC_LANBIDE.CODTIPDOC  ";
        } else {
            parteOn = " ON MELANBIDE68_TIPDOC_PROC.TIPDOC_ID = MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_ID  ";
        }
        String query = "SELECT " +
                "MELANBIDE68_TIPDOC_PROC.TIPDOC_ID AS TIP_DOC, " +
                "COALESCE(MELANBIDE68_DOKUSI_TIPDOC_LANB.TIPDOC_DOKUSI, 'Sin Dato') AS TIPDOC_DOKUSI, " +
                "MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES AS TIPDOC_ES, " +
                "MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_EU AS TIPDOC_EU, " +
                "MELANBIDE68_TIPDOC_LANBIDE.CODTIPDOC AS COD_TIPDOC " +
                "FROM MELANBIDE68_TIPDOC_PROC " +
                "INNER JOIN MELANBIDE68_TIPDOC_LANBIDE " + parteOn +
                "LEFT JOIN MELANBIDE68_DOKUSI_TIPDOC_LANB " +
                "ON MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_ID = MELANBIDE68_DOKUSI_TIPDOC_LANB.TIPDOC_ID " +
                "WHERE MELANBIDE68_TIPDOC_PROC.COD_PROC = '" + codProc + "' " +
                "ORDER BY MELANBIDE68_TIPDOC_LANBIDE.CODTIPDOC";
        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }
        st = con.createStatement();
        rs = st.executeQuery(query);
        while (rs.next()) {
            tiposDoc.add((FilaTipDocProcVO) MeLanbide68MappingUtils.getInstance().map(rs, FilaTipDocProcVO.class));
        }
        return tiposDoc;
    }
    catch (Exception ex)
    {
        log.error("Se ha producido un error recuperando tipos documentales Lanbide para el procedimiento " + codProc, ex);
        throw new Exception(ex);
    }
    finally
    {
        try
        {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        catch (Exception e)
        {
            log.error("Se ha producido un error cerrando el statement y el resultset", e);
            throw new Exception(e);
        }
    }
}





  public TipDocPorProcedVO getProcedimientoYTipodocumental(String codProc, int codTipDoc, boolean porCodigo, Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    TipDocPorProcedVO tipoDocporProc = new TipDocPorProcedVO();
    try
    {
      String query = null;
      if(porCodigo) {
          query = "SELECT COD_PROC,       MELANBIDE68_TIPDOC_PROC.TIPDOC_ID,       MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES,        MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_EU FROM  MELANBIDE68_TIPDOC_PROC  INNER JOIN MELANBIDE68_TIPDOC_LANBIDE  ON MELANBIDE68_TIPDOC_PROC.TIPDOC_ID = MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_ID  WHERE COD_PROC = '" + codProc + "'" + " AND   MELANBIDE68_TIPDOC_LANBIDE.CODTIPDOC = " + codTipDoc;
      } else {
        query = "SELECT COD_PROC,       MELANBIDE68_TIPDOC_PROC.TIPDOC_ID,       MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES,        MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_EU FROM  MELANBIDE68_TIPDOC_PROC  INNER JOIN MELANBIDE68_TIPDOC_LANBIDE  ON MELANBIDE68_TIPDOC_PROC.TIPDOC_ID = MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_ID  WHERE COD_PROC = '" + codProc + "'" + " AND   MELANBIDE68_TIPDOC_PROC.TIPDOC_ID = " + codTipDoc;
      }
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        tipoDocporProc = (TipDocPorProcedVO)MeLanbide68MappingUtils.getInstance().map(rs, TipDocPorProcedVO.class);
      }
      return tipoDocporProc;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando el tipo documental " + codTipDoc + " para el procedimiento " + codProc, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public int eliminarTipoDocumental(TipDocPorProcedVO p, Connection con)
    throws Exception
  {
    Statement st = null;
    try
    {
      String query = null;
      query = "DELETE FROM MELANBIDE68_TIPDOC_PROC WHERE COD_PROC  = '" + p.getProcedimiento() + "'" + " AND TIPDOC_ID   = " + p.getTipoDocumental();
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      return st.executeUpdate(query);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error eliminando tipo documental  " + (p != null ? Integer.valueOf(p.getTipoDocumental()) : "tipo documental = null") + " para el procedimiento " + (p != null ? p.getProcedimiento() : "procedimiento = null"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
public TipDocPorProcedVO guardarTipDocPorProcedVO(TipDocPorProcedVO tipdocproced, Connection con) throws Exception {
    PreparedStatement psBuscarId = null;
    PreparedStatement psVerificar = null;
    PreparedStatement psInsertar = null;
    PreparedStatement psBuscarDokusi = null;
    ResultSet rs = null;
    ResultSet rsVerificar = null;
    ResultSet rsDokusi = null;
    
    try {
        // Buscar TIPDOC_ID correspondiente al CODTIPDOC
        String queryBuscarId = "SELECT TIPDOC_ID FROM MELANBIDE68_TIPDOC_LANBIDE WHERE CODTIPDOC = ?";
        if (log.isDebugEnabled()) {
        log.debug("sql = " + queryBuscarId);
      }
        psBuscarId = con.prepareStatement(queryBuscarId);
        psBuscarId.setInt(1, tipdocproced.getTipoDocumental());
        rs = psBuscarId.executeQuery();

        int tipDocId = 0;
        if (rs.next()) {
            tipDocId = rs.getInt("TIPDOC_ID");
        } else {
            log.warn("No se encontró TIPDOC_ID para CODTIPDOC: " + tipdocproced.getTipoDocumental());
            return null;
        }

        // Buscar TIPDOC_DOKUSI correspondiente al TIPDOC_ID
        String queryBuscarDokusi = "SELECT D.TIPDOC_DOKUSI FROM MELANBIDE68_TIPDOC_LANBIDE L " +
                                   "INNER JOIN MELANBIDE68_DOKUSI_TIPDOC_LANB D ON L.TIPDOC_ID = D.TIPDOC_ID " +
                                   "WHERE L.TIPDOC_ID = ?";
        if (log.isDebugEnabled()) {
        log.debug("sql = " + queryBuscarDokusi);
      }
        psBuscarDokusi = con.prepareStatement(queryBuscarDokusi);
        psBuscarDokusi.setInt(1, tipDocId);
        rsDokusi = psBuscarDokusi.executeQuery();

        String tipDocDokusi = null;
        if (rsDokusi.next()) {
            tipDocDokusi = rsDokusi.getString("TIPDOC_DOKUSI");
        } else {
            log.warn("No se encontró TIPDOC_DOKUSI para TIPDOC_ID: " + tipDocId);
            return null;
        }

        // Verificar duplicados
        String queryVerificarDuplicado = "SELECT COUNT(*) AS EXISTE FROM MELANBIDE68_TIPDOC_PROC WHERE COD_PROC = ? AND TIPDOC_ID = ?";
        psVerificar = con.prepareStatement(queryVerificarDuplicado);
        psVerificar.setString(1, tipdocproced.getProcedimiento());
        psVerificar.setInt(2, tipDocId);
        rsVerificar = psVerificar.executeQuery();

        if (rsVerificar.next() && rsVerificar.getInt("EXISTE") > 0) {
            log.warn("El registro ya existe: COD_PROC=" + tipdocproced.getProcedimiento() + ", TIPDOC_ID=" + tipDocId);
            return null; // Indicar a la capa superior que el registro ya existe
        }

        // Insertar nuevo registro
        String queryInsertar = "INSERT INTO MELANBIDE68_TIPDOC_PROC (COD_PROC, TIPDOC_ID) VALUES (?, ?)";
        psInsertar = con.prepareStatement(queryInsertar);
        psInsertar.setString(1, tipdocproced.getProcedimiento());
        psInsertar.setInt(2, tipDocId);
        int res = psInsertar.executeUpdate();

        if (res > 0) {
            log.info("Registro insertado exitosamente: COD_PROC=" + tipdocproced.getProcedimiento() + ", TIPDOC_ID=" + tipDocId);
            return tipdocproced; // Registro insertado correctamente
        }

        return null; // Si no se pudo insertar
    } catch (Exception ex) {
        log.error("Error en guardarTipDocPorProcedVO", ex);
        throw new Exception("Error en la base de datos: " + ex.getMessage(), ex);
    } finally {
        if (rs != null) rs.close();
        if (rsVerificar != null) rsVerificar.close();
        if (psBuscarId != null) psBuscarId.close();
        if (psVerificar != null) psVerificar.close();
        if (psInsertar != null) psInsertar.close();
    }
}

  
  public List<FilaTipDocLanbideExcelVO> getListadoTiposDocumentalesLanbideExcel(Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    List<FilaTipDocLanbideExcelVO> tiposDocumentales = new ArrayList();
    try
    {
      String query = "SELECT L.TIPDOC_ID AS TIPDOC_ID, L.TIPDOC_LANBIDE_ES AS TIPDOC_LANBIDE_ES, L.TIPDOC_LANBIDE_EU AS TIPDOC_LANBIDE_EU, L.FECHA_BAJA AS FECHA_BAJA, L.DESCTIPDOC_LANBIDE_ES AS DESCTIPDOC_LANBIDE_ES, L.DESCTIPDOC_LANBIDE_EU AS DESCTIPDOC_LANBIDE_EU, L.CODTIPDOC AS CODTIPDOC, L.COD_GRUPO_TIPDOC AS COD_GRUPO_TIPDOC, D.TIPDOC_DOKUSI AS TIPDOC_DOKUSI, E.DES_NOM AS FAMILIA FROM MELANBIDE68_TIPDOC_LANBIDE L LEFT JOIN MELANBIDE68_DOKUSI_TIPDOC_LANB D ON D.TIPDOC_ID=L.TIPDOC_ID LEFT JOIN E_DES_VAL E ON E.DES_VAL_COD=L.COD_GRUPO_TIPDOC AND E.DES_COD='GRTD' ORDER BY L.TIPDOC_ID";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        tiposDocumentales.add((FilaTipDocLanbideExcelVO)MeLanbide68MappingUtils.getInstance().map(rs, FilaTipDocLanbideExcelVO.class));
      }
      return tiposDocumentales;
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando los tipos documentales de Lanbide ", ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
  
  public List<ProcedimientoVO> getListaProcedimientosByIdTipoDocumental(Connection con, Integer idTipoDocumental)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    List<ProcedimientoVO> lstProcedimientos = new ArrayList();
    try
    {
      String query = "SELECT E.PRO_COD, E.PRO_DES FROM E_PRO E INNER JOIN MELANBIDE68_TIPDOC_PROC TD ON td.cod_proc = E.PRO_COD WHERE td.tipdoc_id =" + idTipoDocumental;
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      while (rs.next()) {
        lstProcedimientos.add((ProcedimientoVO)MeLanbide68MappingUtils.getInstance().map(rs, ProcedimientoVO.class));
      }
      return lstProcedimientos;
    }
    catch (Exception e)
    {
      log.error("Se ha producido un error recuperando los procedimientos ", e);
      throw new Exception(e);
    }
    finally
    {
      try
      {
        if (log.isDebugEnabled()) {
          log.debug("Procedemos a cerrar el statement y el resultset");
        }
        if (st != null) {
          st.close();
        }
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e)
      {
        log.error("Se ha producido un error cerrando el statement y el resulset", e);
        throw new Exception(e);
      }
    }
  }
 
  private int recogerIDInsertar(String sequence, Connection con)
    throws Exception
  {
    Statement st = null;
    ResultSet rs = null;
    int id = 0;
    try
    {
      String query = "SELECT " + sequence + ".NextVal AS PROXID FROM DUAL ";
      if (log.isDebugEnabled()) {
        log.debug("sql = " + query);
      }
      st = con.createStatement();
      rs = st.executeQuery(query);
      if (rs.next()) {
        id = rs.getInt("PROXID");
      }
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando El numero de ID para insercion al llamar la sequencia " + sequence + " : ", ex);
      throw new Exception(ex);
    }
    finally
    {
      if (log.isDebugEnabled()) {
        log.debug("Procedemos a cerrar el statement y el resultset");
      }
      if (st != null) {
        st.close();
      }
      if (rs != null) {
        rs.close();
      }
    }
    return id;
  }
  
 public int recogerCodTipDoc(Connection con) throws Exception {
    Statement st = null;
    ResultSet rs = null;
    int codTipDoc = 0;

    try {
        // Query para obtener el máximo valor de CODTIPDOC
        String query = "SELECT MAX(CODTIPDOC) AS ultimoCod FROM MELANBIDE68_TIPDOC_LANBIDE WHERE CODTIPDOC < 99998";

        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }

        st = con.createStatement();
        rs = st.executeQuery(query);

        if (rs.next()) {
            codTipDoc = rs.getInt("ultimoCod") + 1; // Incrementar el valor máximo en 1
        } else {
            codTipDoc = 1; // Si no hay registros, empezamos con 1
        }
    } catch (Exception ex) {
        log.error("Error recuperando CODTIPDOC: ", ex);
        throw new Exception(ex);
    } finally {
        if (st != null) st.close();
        if (rs != null) rs.close();
    }

    return codTipDoc;
}

public List<FilaTipDocLanbideProcedimientoExcelVO> getListadoTiposDocumentalesLanbideProcExcel(Connection con)
    throws Exception {
    Statement st = null;
    ResultSet rs = null;
    List<FilaTipDocLanbideProcedimientoExcelVO> tiposDocumentalesPorProcedimientos = new ArrayList<FilaTipDocLanbideProcedimientoExcelVO>();

    try {
        String query = "SELECT " +
                "    P.COD_PROC AS COD_PROC, " +
                "    L.CODTIPDOC AS CODTIPDOC, " +
                "    L.TIPDOC_LANBIDE_ES AS TIPDOC_LANBIDE_ES, " +
                "    L.TIPDOC_LANBIDE_EU AS TIPDOC_LANBIDE_EU, " +
                "    COALESCE(D.TIPDOC_DOKUSI, 'Sin Dato') AS TIPDOC_DOKUSI " +
                "FROM MELANBIDE68_TIPDOC_PROC P " +
                "INNER JOIN MELANBIDE68_TIPDOC_LANBIDE L " +
                "    ON P.TIPDOC_ID = L.TIPDOC_ID " +
                "LEFT JOIN MELANBIDE68_DOKUSI_TIPDOC_LANB D " +
                "    ON L.TIPDOC_ID = D.TIPDOC_ID " +
                "ORDER BY P.COD_PROC, L.CODTIPDOC";

        if (log.isDebugEnabled()) {
            log.debug("sql = " + query);
        }

        st = con.createStatement();
        rs = st.executeQuery(query);

        while (rs.next()) {
            FilaTipDocLanbideProcedimientoExcelVO fila = new FilaTipDocLanbideProcedimientoExcelVO();
            
            fila.setCodProc(rs.getString("COD_PROC"));  // Asignar el procedimiento
            fila.setCodTipDoc(rs.getInt("CODTIPDOC"));
            fila.setTipDocLanbideEs(rs.getString("TIPDOC_LANBIDE_ES"));
            fila.setTipDocLanbideEu(rs.getString("TIPDOC_LANBIDE_EU"));
            fila.setTipDocDokusi(rs.getString("TIPDOC_DOKUSI"));

            tiposDocumentalesPorProcedimientos.add(fila);
        }
        return tiposDocumentalesPorProcedimientos;
    } catch (Exception ex) {
        log.error("Error recuperando tipos documentales de Lanbide por Procedimiento ", ex);
        throw new Exception(ex);
    } finally {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Cerrando el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            log.error("Error cerrando el statement y el resultset", e);
            throw new Exception(e);
        }
    }
}



public List<FilaTipDocProcVO> getListaTiposDocTodos(Connection con) throws Exception {
    if (con == null) {
        throw new IllegalArgumentException("La conexión no puede ser nula");
    }

    Statement st = null;
    ResultSet rs = null;
    List<FilaTipDocProcVO> tiposDoc = new ArrayList<FilaTipDocProcVO>();

    try {
        String query = "SELECT MELANBIDE68_TIPDOC_PROC.TIPDOC_ID AS TIP_DOC, "
                     + "MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_ES AS TIPDOC_ES, "
                     + "MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_LANBIDE_EU AS TIPDOC_EU "
                     + "FROM MELANBIDE68_TIPDOC_PROC "
                     + "INNER JOIN MELANBIDE68_TIPDOC_LANBIDE "
                     + "ON MELANBIDE68_TIPDOC_PROC.TIPDOC_ID = MELANBIDE68_TIPDOC_LANBIDE.TIPDOC_ID "
                     + "ORDER BY MELANBIDE68_TIPDOC_LANBIDE.CODTIPDOC";

        if (log.isDebugEnabled()) {
            log.debug("SQL: " + query);
        }

        st = con.createStatement();
        rs = st.executeQuery(query);

        while (rs.next()) {
            FilaTipDocProcVO nuevaFila = new FilaTipDocProcVO();
            nuevaFila.setCodTipDoc(rs.getInt("TIP_DOC"));
            nuevaFila.setDescTipDoc_es(rs.getString("TIPDOC_ES"));
            nuevaFila.setDescTipDoc_eu(rs.getString("TIPDOC_EU"));

            // Evitar duplicados manualmente
            boolean yaExiste = false;
            for (FilaTipDocProcVO existente : tiposDoc) {
                if (existente.getCodTipDoc() == nuevaFila.getCodTipDoc()) {
                    yaExiste = true;
                    break;
                }
            }

            if (!yaExiste) {
                tiposDoc.add(nuevaFila);
            }
        }
    } catch (Exception ex) {
        log.error("Error al recuperar tipos documentales: ", ex);
        throw new Exception("Error al recuperar tipos documentales", ex);
    } finally {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("Error cerrando ResultSet", e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                log.error("Error cerrando Statement", e);
            }
        }
    }

    return tiposDoc;
}




}
