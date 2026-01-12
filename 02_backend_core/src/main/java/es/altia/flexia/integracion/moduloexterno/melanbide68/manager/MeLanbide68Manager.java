package es.altia.flexia.integracion.moduloexterno.melanbide68.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide68.dao.MeLanbide68DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n;
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
import es.altia.util.ByteArrayInOutStream;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

public class MeLanbide68Manager
{
  private static Logger log = LogManager.getLogger(MeLanbide68Manager.class);
  private static MeLanbide68Manager instance = null;
  
  public static MeLanbide68Manager getInstance()
  {
    if (instance == null) {
      synchronized (MeLanbide68Manager.class)
      {
        instance = new MeLanbide68Manager();
      }
    }
    return instance;
  }
  
  public boolean existeObjetoSGA(String expedienteSGA, Connection con)
    throws Exception
  {
    return MeLanbide68DAO.getInstance().existeObjetoSGA(expedienteSGA, con);
  }
  
  public void modificarObjetoSGA(ObjetoSGAVO o, Connection con)
    throws Exception
  {
    MeLanbide68DAO.getInstance().modificarObjetoSGA(o, con);
  }
  
  public boolean existeRegistroRES(String expedienteSGA, Connection con)
    throws NumberFormatException, Exception
  {
    return MeLanbide68DAO.getInstance().existeRegistroRES(expedienteSGA, con);
  }
  
  public List<FilaTipDocLanbideVO> getListaTiposDocumentales(AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getListaTiposDocumentales(con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando los tipos documentales de Lanbide ", e);
      throw new Exception(e);
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
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public FilaTipDocLanbideVO getTipoDocumentalById(int id, AdaptadorSQLBD adaptador) throws Exception {
  
      return getTipoDocumentalById(id, false, adaptador);
  }
  
  public FilaTipDocLanbideVO getTipoDocumentalById(int id, boolean porCodigo, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getTipoDocumentalById(id, porCodigo, con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando el Tipo Documental " + id, e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando el Tipo Documental " + id, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public FilaTipDocLanbideVO getTipoDocumentalByCodigo(int codigo, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getTipoDocumentalByCodigo(codigo, con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando el Tipo Documental " + codigo, e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando el Tipo Documental " + codigo, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int eliminarTipDocLanbideVO(int codOrganizacion, FilaTipDocLanbideVO tipDocLanbide, AdaptadorSQLBD adaptador)
    throws SQLException, Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (tipDocLanbide != null)
      {
        int eliminados = meLanbide68DAO.eliminarTipDocLanbide(tipDocLanbide, con);
        if (eliminados > 0)
        {
          adaptador.finTransaccion(con);
          return eliminados;
        }
        throw new BDException("No hay nada que borrar");
      }
      throw new BDException("tipo de documento de lanbide  es nulo");
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando tipo documental " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(tipo documental = null)"), e);
      throw new Exception(e);
    }
    catch (SQLException ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando tipo documental " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(tipo documental = null)"), ex);
      throw ex;
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int deshabilitarTipDocLanbideVO(int codOrganizacion, FilaTipDocLanbideVO tipDocLanbide, AdaptadorSQLBD adaptador)
    throws SQLException, Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (tipDocLanbide != null)
      {
        int eliminados = meLanbide68DAO.deshabilitarTipDocLanbide(tipDocLanbide, con);
        if (eliminados > 0)
        {
          adaptador.finTransaccion(con);
          return eliminados;
        }
        throw new BDException("No hay nada que deshabilitar");
      }
      throw new BDException("tipo de documento de lanbide  es nulo");
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD habilitando/deshabilitando tipo documental " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(tipo documental = null)"), e);
      throw new Exception(e);
    }
    catch (SQLException ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD habilitando/deshabilitando tipo documental " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(tipo documental = null)"), ex);
      throw ex;
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int modificarTipDocLanbideVO(int codOrganizacion, FilaTipDocLanbideVO tipDocLanbide, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (tipDocLanbide != null)
      {
        int modificados = meLanbide68DAO.modificartipDocLanbide(tipDocLanbide, con);
        if (modificados > 0)
        {
          adaptador.finTransaccion(con);
          return modificados;
        }
        throw new BDException();
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando Tipo documental Lanbide " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(Tipo documental = null)"), e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando Tipo documental Lanbide " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(Tipo documental = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int modificarTipDocLanbideVO(int codOrganizacion, FilaTipDocLanbideVO tipDocLanbide, FilaTipDocDokusiVO tipoDocDokusi, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if ((tipDocLanbide != null) && (tipoDocDokusi != null))
      {
        int modificados = meLanbide68DAO.modificartipDocLanbide(tipDocLanbide, con);
        int modDokusi = meLanbide68DAO.modificartipDocDokusi(tipoDocDokusi, con);
        if ((modificados > 0) && (modDokusi > 0))
        {
          adaptador.finTransaccion(con);
          return modificados;
        }
        throw new BDException();
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando Tipo documental Lanbide " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(Tipo documental = null)"), e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando Tipo documental Lanbide " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(Tipo documental = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public FilaTipDocLanbideVO guardarTipDocLanbideVO(int codOrganizacion, FilaTipDocLanbideVO tipDocLanbide, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      tipDocLanbide = meLanbide68DAO.guardartipDocLanbideVO(tipDocLanbide, con);
      if (tipDocLanbide != null) {
        return tipDocLanbide;
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando el tipo documental " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(tipo documental = null)"), e);
      adaptador.rollBack(con);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando el tipo documental " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(tipo documental = null)"), ex);
      adaptador.rollBack(con);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
        adaptador.rollBack(con);
      }
    }
  }
  
  public FilaTipDocLanbideVO guardarTipDocLanbideVO(int codOrganizacion, FilaTipDocLanbideVO tipDocLanbide, String tipDocDokusi,  boolean anadirATodos, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
   
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      tipDocLanbide = meLanbide68DAO.guardartipDocLanbideVO(tipDocLanbide, con);
      if (tipDocLanbide == null) {
        throw new BDException();
      }
      FilaTipDocDokusiVO tipoDocDokusi = new FilaTipDocDokusiVO();
      tipoDocDokusi.setCodTipDoc(tipDocLanbide.getId());
      tipoDocDokusi.setTipDocDokusi(tipDocDokusi);
      tipoDocDokusi = meLanbide68DAO.guardartipDocDokusiVO(tipoDocDokusi, con);
      
      if (tipoDocDokusi != null)
      {
             // Añadir a todos los procedimientos si el parámetro es true
        if (anadirATodos) {
            // Buscar todos los procedimientos
            List<ProcedimientoVO> procedimientos = meLanbide68DAO.getProcedimientos(con, true);

            // Iterar sobre los procedimientos y guardar la relación
            for (ProcedimientoVO procedimiento : procedimientos) {
                TipDocPorProcedVO tipdocproced = new TipDocPorProcedVO();
                tipdocproced.setProcedimiento(procedimiento.getCodProc()); // Código del procedimiento
                tipdocproced.setTipoDocumental(tipDocLanbide.getTipDocID()); // ID del tipo documental

                meLanbide68DAO.guardarTipDocPorProcedVO(tipdocproced, con);
            }
        }
        adaptador.finTransaccion(con);
        return tipDocLanbide;
      } else {
        throw new BDException();
      }
    }
    
    catch (Exception ex)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando el tipo documental " + (tipDocLanbide != null ? Integer.valueOf(tipDocLanbide.getTipDocID()) : "(tipo documental = null)"), ex);
      adaptador.rollBack(con);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
        adaptador.rollBack(con);
      }
    }
  }
  public List<String> insertarEnTodos(int codOrganizacion, int codTipDocLanbide, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
   ArrayList<String> mensajes = new ArrayList<String>();
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
            // Buscar todos los procedimientos
            List<ProcedimientoVO> procedimientos = meLanbide68DAO.getProcedimientos(con, true);

            // Iterar sobre los procedimientos y guardar la relación
            for (ProcedimientoVO procedimiento : procedimientos) {
                TipDocPorProcedVO tipdocproced = new TipDocPorProcedVO();
                tipdocproced.setProcedimiento(procedimiento.getCodProc()); // Código del procedimiento
                tipdocproced.setTipoDocumental(codTipDocLanbide); // ID del tipo documental

                TipDocPorProcedVO tipoDocPorProcRetorno = meLanbide68DAO.guardarTipDocPorProcedVO(tipdocproced, con);
                String mensaje = null;
                if(tipoDocPorProcRetorno!=null){
                    mensaje = "Insertado OK para el proc " + procedimiento.getCodProc();
                } else {
                    mensaje = "Insertado KO para el proc " + procedimiento.getCodProc() + ". Consulte al administrador para saber el motivo";
                
                }
                mensajes.add(mensaje);
            }
        
        adaptador.finTransaccion(con);
        return mensajes;
    }
    
    catch (Exception ex)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando el tipo documental " , ex);
      adaptador.rollBack(con);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
        adaptador.rollBack(con);
      }
    }
  }
  
  public List<FilaTipDocDokusiVO> getListaTiposDocumentalesDokusi(AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getListaTiposDocumentalesDokusi(con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando la relación de tipos documentales Lanbide-DOKUSI ", e);
      throw new Exception(e);
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
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public FilaTipDocDokusiVO getTipoDocumentalDokusi(int codTipDoc, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getTipoDocumentalDokusi(codTipDoc, con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando la relación tipo documental Lanbide-DOKUSI " + codTipDoc, e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando la relación tipo documental Lanbide-DOKUSI " + codTipDoc, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int eliminarTipDocDokusiVO(int codOrganizacion, FilaTipDocDokusiVO tipDocDokusi, AdaptadorSQLBD adaptador)
    throws SQLException, Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (tipDocDokusi != null)
      {
        int eliminados = meLanbide68DAO.eliminarTipDocDokusi(tipDocDokusi, con);
        if (eliminados > 0)
        {
          adaptador.finTransaccion(con);
          return eliminados;
        }
        throw new BDException("No hay nada que borrar");
      }
      throw new BDException("tipo de documento DOKUSI  es nulo");
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando la relación tipo documental Lanbide-DOKUSI" + (tipDocDokusi != null ? Integer.valueOf(tipDocDokusi.getCodTipDoc()) : "(relación Lanbide-DOKUSI = null)"), e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando la relación tipo documental Lanbide-DOKUSI " + (tipDocDokusi != null ? Integer.valueOf(tipDocDokusi.getCodTipDoc()) : "(relación Lanbide-DOKUSI = null)"), ex);
      throw ex;
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int modificarTipDocDokusiVO(int codOrganizacion, FilaTipDocDokusiVO tipDocDokusi, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (tipDocDokusi != null)
      {
        int modificados = meLanbide68DAO.modificartipDocDokusi(tipDocDokusi, con);
        if (modificados > 0)
        {
          adaptador.finTransaccion(con);
          return modificados;
        }
        throw new BDException();
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando la relación tipo documental Lanbide-DOKUSI " + (tipDocDokusi != null ? Integer.valueOf(tipDocDokusi.getCodTipDoc()) : "(relación Lanbide-DOKUSI = null)"), e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando la relación tipo documental Lanbide-DOKUSI " + (tipDocDokusi != null ? Integer.valueOf(tipDocDokusi.getCodTipDoc()) : "(relación Lanbide-DOKUSI = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public FilaTipDocDokusiVO guardarTipDocDokusiVO(int codOrganizacion, FilaTipDocDokusiVO tipDocDokusi, AdaptadorSQLBD adaptador)
    throws SQLException, Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      tipDocDokusi = meLanbide68DAO.guardartipDocDokusiVO(tipDocDokusi, con);
      if (tipDocDokusi != null)
      {
        adaptador.finTransaccion(con);
        return tipDocDokusi;
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando la relación tipo documental Lanbide-DOKUSI " + (tipDocDokusi != null ? Integer.valueOf(tipDocDokusi.getCodTipDoc()) : "(relación Lanbide-DOKUSI = null)"), e);
      adaptador.rollBack(con);
      throw new Exception(e);
    }
    catch (SQLException ex)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando la relación tipo documental Lanbide-DOKUSI " + (tipDocDokusi != null ? Integer.valueOf(tipDocDokusi.getCodTipDoc()) : "(relación Lanbide-DOKUSI = null)"), ex);
      adaptador.rollBack(con);
      throw ex;
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
        adaptador.rollBack(con);
      }
    }
  }
  
  public List<TipDocLanbideVO> getTiposDoc(AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      if (adaptador != null)
      {
        log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
        con = adaptador.getConnection();
      }
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getTiposDoc(con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido una excepcion en la BBDD recuperando Tipos documentales Lanbide ", e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido una excepcion en la BBDD recuperando Tipos documentales Lanbide ", ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
    
  public List<FilaMetadatoVO> getListaMetadatosporTipodocumental(int codTipDoc, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getListaMetadatosporTipodocumental(codTipDoc, con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando metadatos para tipo documental Lanbide " + codTipDoc, e);
      throw new Exception(e);
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
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public MetadatoVO getTipodocumentalyMetadato(int codTipDoc, String codMetadato, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getTipodocumentalyMetadato(codTipDoc, codMetadato, con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando metadato para tipo documental " + codTipDoc + " y Metadato " + codMetadato, e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando metadato para tipo documental " + codTipDoc + " y Metadato " + codMetadato, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int eliminarMetadatoVO(int codOrganizacion, MetadatoVO metadato, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (metadato != null)
      {
        int eliminados = meLanbide68DAO.eliminarMetadato(metadato, con);
        if (eliminados > 0)
        {
          adaptador.finTransaccion(con);
          return eliminados;
        }
        throw new BDException();
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando metadato " + (metadato != null ? metadato.getMetadato() : "(metadato = null)") + " para el tipo documental " + (metadato != null ? Integer.valueOf(metadato.getTipoDocumental()) : "(metadato = null)"), e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando metadato " + (metadato != null ? metadato.getMetadato() : "(metadato = null)") + " para el tipo documental " + (metadato != null ? Integer.valueOf(metadato.getTipoDocumental()) : "(metadato = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int deshabilitarMetadatoVO(int codOrganizacion, MetadatoVO metadato, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (metadato != null)
      {
        int deshabilitados = meLanbide68DAO.deshabilitarMetadato(metadato, con);
        if (deshabilitados > 0)
        {
          adaptador.finTransaccion(con);
          return deshabilitados;
        }
        throw new BDException();
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD habilitando/deshabilitando metadato " + (metadato != null ? metadato.getMetadato() : "(metadato = null)") + " para el tipo documental " + (metadato != null ? Integer.valueOf(metadato.getTipoDocumental()) : "(metadato = null)"), e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD habilitando/deshabilitando metadato " + (metadato != null ? metadato.getMetadato() : "(metadato = null)") + " para el tipo documental " + (metadato != null ? Integer.valueOf(metadato.getTipoDocumental()) : "(metadato = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int modificarMetadatoVO(int codOrganizacion, MetadatoVO metadato, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (metadato != null)
      {
        int modificados = meLanbide68DAO.modificarMetadato(metadato, con);
        if (modificados > 0)
        {
          adaptador.finTransaccion(con);
          return modificados;
        }
        throw new BDException();
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando metadato " + (metadato != null ? metadato.getMetadato() : "(metadato = null)") + " para el tipo documental " + (metadato != null ? Integer.valueOf(metadato.getTipoDocumental()) : "(metadato = null)"), e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando metadato " + (metadato != null ? metadato.getMetadato() : "(metadato = null)") + " para el tipo documental " + (metadato != null ? Integer.valueOf(metadato.getTipoDocumental()) : "(metadato = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public MetadatoVO guardarMetadatoVO(int codOrganizacion, MetadatoVO metadato, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      metadato = meLanbide68DAO.guardarMetadatoVO(metadato, con);
      if (metadato != null)
      {
        adaptador.finTransaccion(con);
        return metadato;
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando el metadato " + (metadato != null ? metadato.getMetadato() : "(metadato = null)"), e);
      adaptador.rollBack(con);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando el metadato " + (metadato != null ? metadato.getMetadato() : "(metadato = null)"), ex);
      adaptador.rollBack(con);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
        adaptador.rollBack(con);
      }
    }
  }
  
  public List<FilaTipDokusiVO> getListaTiposDokusi(AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getListaTiposDokusi(con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando los tipos documentales DOKUSI ", e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando los tipos documentales DOKUSI ", ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public FilaTipDokusiVO getTipoDokusi(String codTipDokusi, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getTipoDokusi(codTipDokusi, con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando el Tipo Documental Dokusi " + codTipDokusi, e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando el Tipo Documental Dokusi" + codTipDokusi, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }






  public int eliminarTipDokusiVO(int codOrganizacion, FilaTipDokusiVO tipDokusi, AdaptadorSQLBD adaptador)
    throws SQLException, Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (tipDokusi != null)
      {
        int eliminados = meLanbide68DAO.eliminarTipDokusi(tipDokusi, con);
        if (eliminados > 0)
        {
          adaptador.finTransaccion(con);
          return eliminados;
        }
        throw new BDException("No hay nada que borrar");
      }
      throw new BDException("tipo de documento Dokusi  es nulo");
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando tipo documental Dokusi" + (tipDokusi != null ? tipDokusi.getCodDokusi() : "(tipo documental Dokusi= null)"), e);
      throw new Exception(e);
    }
    catch (SQLException ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando tipo documental Dokusi" + (tipDokusi != null ? tipDokusi.getCodDokusi() : "(tipo documental Dokusi= null)"), ex);
      throw ex;
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int modificarTipDokusiVO(int codOrganizacion, FilaTipDokusiVO tipDokusi, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (tipDokusi != null)
      {
        int modificados = meLanbide68DAO.modificartipDokusi(tipDokusi, con);
        if (modificados > 0)
        {
          adaptador.finTransaccion(con);
          return modificados;
        }
        throw new BDException();
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando Tipo documental Dokusi " + (tipDokusi != null ? tipDokusi.getCodDokusi() : "(Tipo documental = null)"), e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD modificando Tipo documental Dokusi " + (tipDokusi != null ? tipDokusi.getCodDokusi() : "(Tipo documental = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public FilaTipDokusiVO guardarTipDokusiVO(int codOrganizacion, FilaTipDokusiVO tipDokusi, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      tipDokusi = meLanbide68DAO.guardartipDokusiVO(tipDokusi, con);
      if (tipDokusi != null)
      {
        adaptador.finTransaccion(con);
        return tipDokusi;
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando el tipo documental " + (tipDokusi != null ? tipDokusi.getCodDokusi() : "(tipo documental = null)"), e);
      adaptador.rollBack(con);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido una excepcion en la BBDD guardando el tipo documental " + (tipDokusi != null ? tipDokusi.getCodDokusi() : "(tipo documental = null)"), ex);
      adaptador.rollBack(con);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
        adaptador.rollBack(con);
      }
    }
  }
  
  public List<TipDokusiVO> getTiposDokusi(AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      if (adaptador != null)
      {
        log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
        con = adaptador.getConnection();
      }
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getTiposDokusi(con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido una excepcion en la BBDD recuperando Tipos documentales Dokusi ", e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido una excepcion en la BBDD recuperando Tipos documentales Dokusi ", ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public List<GrupoTipDocVO> getGruposTipDoc(AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      if (adaptador != null)
      {
        log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
        con = adaptador.getConnection();
      }
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getGruposTipDoc(con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido una excepcion en la BBDD recuperando Grupos de Tipos documentales ", e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido una excepcion en la BBDD recuperando Grupos de Tipos documentales ", ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public List<ProcedimientoVO> getProcedimientos(AdaptadorSQLBD adaptador)
    throws Exception
  {
    log.error(" Entrando a Manager.GetProcedimientos");
    Connection con = null;
    try
    {
      if (adaptador != null)
      {
        log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
        con = adaptador.getConnection();
      }
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getProcedimientos(con, true);
    }
    catch (BDException e)
    {
      log.error("Se ha producido una excepcion en la BBDD recuperando Datos de Procedimientos ", e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido una excepcion en la BBDD recuperando Procedimientos ", ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public List<FilaTipDocProcVO> getListaTiposDocporProcedimiento(String codProc, AdaptadorSQLBD adaptador)
    throws Exception
  {
      return getListaTiposDocporProcedimiento(codProc, false, adaptador);
  }

public List<FilaTipDocProcVO> getListaTiposDocTodos(AdaptadorSQLBD adaptador) throws Exception {
    Connection con = null;
    try {
        // Obtiene la conexión desde el adaptador
        con = adaptador.getConnection();
        // Llama al método del DAO
        return MeLanbide68DAO.getInstance().getListaTiposDocTodos(con);
    } finally {
        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                // Manejo de errores al cerrar la conexión
                log.error("Error al cerrar la conexión", e);
            }
        }
    }
}

  public List<FilaTipDocProcVO> getListaTiposDocporProcedimiento(String codProc, boolean porCodigo, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getListaTiposDocporProcedimiento(codProc, porCodigo, con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando tipos documentales del Procedimiento: " + codProc, e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando tipos documentales del Procedimiento: " + codProc, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public TipDocPorProcedVO getProcedimientoYTipodocumental(String codProc, int codTipDoc, AdaptadorSQLBD adaptador)
    throws Exception{
      return getProcedimientoYTipodocumental(codProc, codTipDoc, false, adaptador);
  }
  
  public TipDocPorProcedVO getProcedimientoYTipodocumental(String codProc, int codTipDoc, boolean porCodigo, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      return meLanbide68DAO.getProcedimientoYTipodocumental(codProc, codTipDoc, porCodigo, con);
    }
    catch (BDException e)
    {
      log.error("Se ha producido un error recuperando tipo documental para procedimiento " + codProc + " y tipo documental " + codTipDoc, e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      log.error("Se ha producido un error recuperando tipo documental para procedimiento " + codProc + " y tipo documental " + codTipDoc, ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
  public int eliminarTipoDocumental(int codOrganizacion, TipDocPorProcedVO tipoDocporProc, AdaptadorSQLBD adaptador)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
      if (tipoDocporProc != null)
      {
        int eliminados = meLanbide68DAO.eliminarTipoDocumental(tipoDocporProc, con);
        if (eliminados > 0)
        {
          adaptador.finTransaccion(con);
          return eliminados;
        }
        throw new BDException();
      }
      throw new BDException();
    }
    catch (BDException e)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando tipo documental " + (tipoDocporProc != null ? Integer.valueOf(tipoDocporProc.getTipoDocumental()) : "(tipo documental = null)") + " para el procedimiento " + (tipoDocporProc != null ? tipoDocporProc.getProcedimiento() : "(procedimiento = null)"), e);
      throw new Exception(e);
    }
    catch (Exception ex)
    {
      adaptador.rollBack(con);
      log.error("Se ha producido una excepcion en la BBDD eliminando tipo documental " + (tipoDocporProc != null ? Integer.valueOf(tipoDocporProc.getTipoDocumental()) : "(tipo documental = null)") + " para el procedimiento " + (tipoDocporProc != null ? tipoDocporProc.getProcedimiento() : "(procedimiento = null)"), ex);
      throw new Exception(ex);
    }
    finally
    {
      try
      {
        adaptador.devolverConexion(con);
      }
      catch (Exception e)
      {
        adaptador.rollBack(con);
        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
      }
    }
  }
  
public TipDocPorProcedVO guardarTipDocPorProcedVO(int codOrganizacion, TipDocPorProcedVO tipdocproced, AdaptadorSQLBD adaptador) throws Exception {
    Connection con = null;

    try {
        con = adaptador.getConnection();
        adaptador.inicioTransaccion(con);

        MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();

        TipDocPorProcedVO resultado = meLanbide68DAO.guardarTipDocPorProcedVO(tipdocproced, con);

        if (resultado != null) {
            adaptador.finTransaccion(con);
            log.info("Registro procesado correctamente: " + resultado.getProcedimiento());
            return resultado;
        }

        adaptador.rollBack(con);
        log.warn("El registro ya existe o no pudo ser guardado.");
        return null; // Informar al controlador que ya existía o no se insertó

    } catch (Exception ex) {
            adaptador.rollBack(con);
        log.error("Error al guardar tipo documental.", ex);
        throw new Exception("Error al guardar tipo documental: " + ex.getMessage(), ex);
    } finally {
                adaptador.devolverConexion(con);
            }
        }


public HSSFWorkbook descargarExcelTiposLanbide(AdaptadorSQLBD adaptador, int idioma) throws Exception {
    Connection con = null;
    try {
        System.out.println(">>> [descargarExcelTiposLanbide] INICIO con idioma=" + idioma);

        con = adaptador.getConnection();
        adaptador.inicioTransaccion(con);

        MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
        List<FilaTipDocLanbideExcelVO> lstTiposDocumentales = meLanbide68DAO.getListadoTiposDocumentalesLanbideExcel(con);

        System.out.println(">>> [descargarExcelTiposLanbide] Tamaño lista = " 
                            + (lstTiposDocumentales != null ? lstTiposDocumentales.size() : 0));

        // 1) i18n para el nombre de la hoja
        MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();
        String sheetNameRaw = meLanbide68I18n.getMensaje(idioma, "excel.titulo.hoja.tiposLanbide");
        System.out.println(">>> sheetName(i18n) = [" + sheetNameRaw + "]");

        // Fallback si está nulo o vacío
        if (sheetNameRaw == null || sheetNameRaw.trim().isEmpty()) {
            sheetNameRaw = "Tipos Documentales Lanbide";
        }
        sheetNameRaw = sheetNameRaw.trim();
        // POI exige <= 31 chars en HSSFWorkbook
        if (sheetNameRaw.length() > 31) {
            System.out.println(">>> [WARN] sheetName excede 31 chars, recortando...");
            sheetNameRaw = sheetNameRaw.substring(0, 31);
        }
        System.out.println(">>> sheetName final= [" + sheetNameRaw + "] length=" + sheetNameRaw.length());

        // 2) Crear Workbook y hoja
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetNameRaw);

        // 3) Usar i18n para el título principal si quieres
        String tituloPrincipal = meLanbide68I18n.getMensaje(idioma, "excel.titulo.principal.tiposLanbide");
        if (tituloPrincipal == null || tituloPrincipal.trim().isEmpty()) {
            tituloPrincipal = "Documentales Lanbide";
        }
        System.out.println(">>> tituloPrincipal= [" + tituloPrincipal + "]");

        // ========== Estilos y título (fila 0) ==========
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        HSSFFont titleFont = workbook.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setColor(HSSFColor.DARK_RED.index);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(35);
        HSSFCell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(tituloPrincipal);
        titleCell.setCellStyle(titleStyle);

        // Headers (array con i18n)
        // Ej: "excel.header.tipdoc.id", "excel.header.tipdoc.lanbide.es", etc.
        String[] headers = {
            meLanbide68I18n.getMensaje(idioma, "excel.header.tipdoc.id"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.tipdoc.lanb.es"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.tipdoc.lanb.eu"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.fecha.baja"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.desc.tipdoc.es"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.desc.tipdoc.eu"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.codtipdoc"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.cod.grupo.tipdoc"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.tipdoc.dokusi"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.familia")
        };
        System.out.println(">>> HEADERS i18n recuperados:");
        for (int h = 0; h < headers.length; h++) {
            if (headers[h] == null || headers[h].trim().isEmpty()) {
                headers[h] = "Col"+h; // fallback
            }
            System.out.println("    headers["+h+"]: ["+headers[h]+"]");
        }

        // Fusión celdas para el título
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

        // Estilo encabezados
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setColor(HSSFColor.WHITE.index);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(HSSFColor.DARK_RED.index);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

        // Paleta y color gris
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor myCustomGrey = palette.findSimilarColor((byte) 246, (byte) 246, (byte) 246);

        // Estilo para datos
        HSSFCellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFillForegroundColor(myCustomGrey.getIndex());
        dataStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

        // Estilo para fecha
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.cloneStyleFrom(dataStyle);
        short dateFormat = workbook.createDataFormat().getFormat("dd/MM/yyyy");
        dateStyle.setDataFormat(dateFormat);

        // Fila de encabezados (fila 1)
        HSSFRow headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(25);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Llenar datos (fila 2 en adelante)
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (int i = 0; i < lstTiposDocumentales.size(); i++) {
            HSSFRow dataRow = sheet.createRow(i + 2);
            FilaTipDocLanbideExcelVO fila = lstTiposDocumentales.get(i);

            HSSFCell cell0 = dataRow.createCell(0);
            cell0.setCellValue(fila.getTipDocId());
            cell0.setCellStyle(dataStyle);

            HSSFCell cell1 = dataRow.createCell(1);
            cell1.setCellValue(fila.getTipDocLanbideEs());
            cell1.setCellStyle(dataStyle);

            HSSFCell cell2 = dataRow.createCell(2);
            cell2.setCellValue(fila.getTipDocLanbideEu());
            cell2.setCellStyle(dataStyle);

            HSSFCell cell3 = dataRow.createCell(3);
            if (fila.getFechaBaja() != null) {
                cell3.setCellValue(sdf.format(fila.getFechaBaja()));
                cell3.setCellStyle(dateStyle);
            }

            HSSFCell cell4 = dataRow.createCell(4);
            cell4.setCellValue(fila.getDescTipDocLanbideEs());
            cell4.setCellStyle(dataStyle);

            HSSFCell cell5 = dataRow.createCell(5);
            cell5.setCellValue(fila.getDescTipDocLanbideEu());
            cell5.setCellStyle(dataStyle);

            HSSFCell cell6 = dataRow.createCell(6);
            cell6.setCellValue(fila.getCodTipDoc());
            cell6.setCellStyle(dataStyle);

            HSSFCell cell7 = dataRow.createCell(7);
            cell7.setCellValue(fila.getCodGrupoTipDoc());
            cell7.setCellStyle(dataStyle);

            HSSFCell cell8 = dataRow.createCell(8);
            cell8.setCellValue(fila.getTipDocDokusi());
            cell8.setCellStyle(dataStyle);

            HSSFCell cell9 = dataRow.createCell(9);
            cell9.setCellValue(fila.getFamilia());
            cell9.setCellStyle(dataStyle);
        }

        // Ajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        System.out.println(">>> [descargarExcelTiposLanbide] Autoajuste de columnas OK. Devolviendo workbook.");

        return workbook;

    } catch (BDException e) {
        log.error("Se ha producido una excepción en la BBDD recuperando datos antes de imprimir", e);
    } catch (Exception ex) {
        log.error("Se ha producido una excepción en la BBDD guardando tipo documental Lanbide", ex);
        if (con != null) {
            adaptador.rollBack(con);
        }
        throw new Exception(ex);
    } finally {
        try {
            if (con != null) {
                adaptador.devolverConexion(con);
            }
        } catch (Exception e) {
            log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            if (con != null) {
                adaptador.rollBack(con);
            }
        }
    }
    return null;
}


  
  public List<ProcedimientoVO> getProcedimientosByTipoDocumentalLanbide(AdaptadorSQLBD adaptador, Integer codTipoDocumental)
    throws Exception
  {
    Connection con = null;
    try
    {
      con = adaptador.getConnection();
      adaptador.inicioTransaccion(con);
      List<ProcedimientoVO> lstProcedimientos = MeLanbide68DAO.getInstance().getListaProcedimientosByIdTipoDocumental(con, codTipoDocumental);
      
      System.out.println("");
      for (ProcedimientoVO p : lstProcedimientos) {
        System.out.println(p.getDescProc());
      }
      System.out.println("");
      
      return lstProcedimientos;
    }
    catch (BDException e)
    {
      log.error("Se ha producido una excepcion en la BBDD recuperando los datos de Prodedimientos", e);
    }
    return null;
  }

public int generarCodTipDoc(AdaptadorSQLBD adaptador) throws Exception {
    Connection con = null; // Conexión a la base de datos
    int nuevoCodTipDoc = 0; // Variable para almacenar el código generado

    try {
        // Obtén la conexión utilizando el adaptador
        con = adaptador.getConnection();

        // Inicia la transacción (si tu lógica lo requiere)
        adaptador.inicioTransaccion(con);

        // Llama al DAO para generar el nuevo CODTIPDOC
        MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance(); // Obtiene la instancia del DAO
        nuevoCodTipDoc = meLanbide68DAO.getInstance().recogerCodTipDoc(con); // Llama al método del DAO para generar el código

     
    } catch (Exception e) {
        log.error("Se ha producido una excepción en la BBDD durante la operación", e);
        if (con != null) {
            adaptador.rollBack(con); // Revertir la transacción
        }
        throw new Exception("Error al generar CODTIPDOC", e); // Propagar la excepción
    } finally {
        try {
            if (con != null) {
                adaptador.devolverConexion(con); // Devuelve la conexión al pool
            }
        } catch (Exception e) {
            log.error("Error al cerrar la conexión a la BBDD: ", e);
        }
    }

    return nuevoCodTipDoc; // Devuelve el código generado
}
public int recogerCodTipDoc(AdaptadorSQLBD adaptador) throws Exception {
    log.debug("Entramos en obtenerNuevoCodTipDoc desde el Manager.");

    Connection con = null;

    try {
        // Obtener conexión de la base de datos
        con = adaptador.getConnection();
        // Llamada al DAO para obtener el nuevo CODTIPDOC
        return MeLanbide68DAO.getInstance().recogerCodTipDoc(con);
    } catch (Exception e) {
        log.error("Error al obtener un nuevo CODTIPDOC: ", e);
        throw e;
    } finally {
        if (con != null) {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception ex) {
                log.error("Error al devolver la conexión en obtenerNuevoCodTipDoc: ", ex);
            }
        }
    }
}

public HSSFWorkbook descargarExcelTipDocuLanbideProc(AdaptadorSQLBD adaptador, int idioma) throws Exception {
    Connection con = null;

    try {
        System.out.println(">>> [descargarExcelTipDocuLanbideProc] INICIO con idioma=" + idioma);

        // 1) Obtener conexión y comenzar la transacción
        con = adaptador.getConnection();
        adaptador.inicioTransaccion(con);
        System.out.println(">>> Se ha iniciado la transacción con la BBDD.");

        // 2) Instanciar el DAO y recuperar la lista de datos
        MeLanbide68DAO meLanbide68DAO = MeLanbide68DAO.getInstance();
        List<FilaTipDocLanbideProcedimientoExcelVO> tiposDocumentalesPorProcedimientos =
            meLanbide68DAO.getListadoTiposDocumentalesLanbideProcExcel(con);

        System.out.println(">>> tiposDocumentalesPorProcedimientos.size() = " + (tiposDocumentalesPorProcedimientos != null ? tiposDocumentalesPorProcedimientos.size() : 0));

        // 3) Instanciar la clase i18n para usar el idioma
        MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();
        System.out.println(">>> Instanciado MeLanbide68I18n. Revisamos valor de 'excel.titulo.hoja.tipdoc'...");

        // Test i18n con clave "excel.titulo.hoja.tipdoc"
        String testKey = "excel.titulo.hoja.tipdoc";
        String testValue = meLanbide68I18n.getMensaje(idioma, testKey);
        System.out.println(">>> Test i18n: idioma=" + idioma + ", clave=" + testKey + ", valor=[" + testValue + "]");

        // 4) Crear el Workbook y la HOJA usando la clave "excel.titulo.principal.mantenimiento"
        //    Ojo: i18n podría devolver null / "" / >31 chars. Lo controlamos:
        String nombreHojaRaw = meLanbide68I18n.getMensaje(idioma, "excel.titulo.principal.mantenimiento");
        System.out.println(">>> Valor crudo de 'excel.titulo.principal.mantenimiento' = [" + nombreHojaRaw + "]");

        // Fallback si está nulo o vacío
        if (nombreHojaRaw == null || nombreHojaRaw.trim().isEmpty()) {
            nombreHojaRaw = meLanbide68I18n.getMensaje(idioma, "excel.titulo.hoja.tipdoc"); 
        }

        // Quitamos espacios en ambos lados
        nombreHojaRaw = nombreHojaRaw.trim();

        // POI no permite >31 chars en HSSFWorkbook
        if (nombreHojaRaw.length() > 31) {
            System.out.println(">>> [WARN] El nombre de la hoja excede 31 chars. Lo recortamos a 31.");
            nombreHojaRaw = nombreHojaRaw.substring(0, 31);
        }

        System.out.println(">>> Nombre final de la hoja (sheetName) = [" + nombreHojaRaw + "], length=" + nombreHojaRaw.length());

        HSSFWorkbook workbook = new HSSFWorkbook();
        // Aquí podría lanzarse la excepción si es "" o >31
        HSSFSheet sheet = workbook.createSheet(nombreHojaRaw);

        // 5) Definir los encabezados
        //    Si no tienes las claves, podría devolver null / "" en alguno:
        String[] headers = {
            meLanbide68I18n.getMensaje(idioma, "excel.header.cod.proc"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.cod.tipdoc"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.tipdoc.lanb.es"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.tipdoc.lanb.eu"),
            meLanbide68I18n.getMensaje(idioma, "excel.header.tipdoc.dokusi")
        };
        System.out.println(">>> Headers i18n recuperados (podrían ser null o vacíos):");
        for (int h = 0; h < headers.length; h++) {
            System.out.println("    headers[" + h + "] = [" + headers[h] + "]");
        }

        // 6) Estilos
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        HSSFFont titleFont = workbook.createFont();
        titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleFont.setFontHeightInPoints((short) 18);
        titleFont.setColor(HSSFColor.DARK_RED.index);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // Fila de título (fila 0)
        HSSFRow titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(35);
        HSSFCell titleCell = titleRow.createCell(0);

        // Texto principal del Excel
        // Revisamos 'excel.titulo.principal.mantenimiento'
        String textoTituloPrincipal = meLanbide68I18n.getMensaje(idioma, "excel.titulo.principal.TiposDocProc");
        System.out.println(">>> Valor crudo de 'excel.titulo.principal.mantenimiento' para celda de título = [" + textoTituloPrincipal + "]");

        if (textoTituloPrincipal == null || textoTituloPrincipal.trim().isEmpty()) {
            textoTituloPrincipal = "Mantenimiento Tipos Documentales Lanbide Por Procedimiento";
        }
        titleCell.setCellValue(textoTituloPrincipal);
        titleCell.setCellStyle(titleStyle);

        // Combinar celdas para el título
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));

        // Estilo para los encabezados
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setColor(HSSFColor.WHITE.index);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(HSSFColor.DARK_RED.index);
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);

        // Paleta de colores
        HSSFPalette palette = workbook.getCustomPalette();
        HSSFColor myCustomGrey = palette.findSimilarColor((byte) 246, (byte) 246, (byte) 246);

        // Estilo para datos
        HSSFCellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setFillForegroundColor(myCustomGrey.getIndex());
        dataStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        dataStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        dataStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // Estilo centrado (por ejemplo, para COD_TIPDOC)
        HSSFCellStyle centeredStyle = workbook.createCellStyle();
        centeredStyle.cloneStyleFrom(dataStyle);
        centeredStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 7) Crear fila de encabezados (fila 1)
        HSSFRow headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(25);

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = headerRow.createCell(i);
            String encabezado = headers[i];
            if (encabezado == null || encabezado.trim().isEmpty()) {
                // fallback
                encabezado = "Col" + i;
            }
            System.out.println(">>> FilaEncabezado col=" + i + " => [" + encabezado + "]");
            cell.setCellValue(encabezado);
            cell.setCellStyle(headerStyle);
        }

        // 8) Llenar datos en la tabla
        System.out.println(">>> Comienzo a rellenar datos. total filas=" + tiposDocumentalesPorProcedimientos.size());
        for (int i = 0; i < tiposDocumentalesPorProcedimientos.size(); i++) {
            FilaTipDocLanbideProcedimientoExcelVO fila = tiposDocumentalesPorProcedimientos.get(i);
            HSSFRow dataRow = sheet.createRow(i + 2);

            // Columna 0: COD_PROC
            HSSFCell cell0 = dataRow.createCell(0);
            cell0.setCellValue(fila.getCodProc());
            cell0.setCellStyle(centeredStyle);

            // Columna 1: COD_TIPDOC
            HSSFCell cell1 = dataRow.createCell(1);
            cell1.setCellValue(fila.getCodTipDoc());
            cell1.setCellStyle(centeredStyle);

            // Columna 2: TIPDOC_LANBIDE_ES
            HSSFCell cell2 = dataRow.createCell(2);
            cell2.setCellValue(fila.getTipDocLanbideEs());
            cell2.setCellStyle(dataStyle);

            // Columna 3: TIPDOC_LANBIDE_EU
            HSSFCell cell3 = dataRow.createCell(3);
            cell3.setCellValue(fila.getTipDocLanbideEu());
            cell3.setCellStyle(dataStyle);

            // Columna 4: TIPDOC_DOKUSI
            HSSFCell cell4 = dataRow.createCell(4);
            cell4.setCellValue(fila.getTipDocDokusi());
            cell4.setCellStyle(dataStyle);
        }

        // 9) Autoajustar columnas
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        System.out.println(">>> Autoajustadas las columnas.");

        // 10) Retornar el workbook
        System.out.println(">>> Retorno del Workbook final OK.");
        return workbook;

    } catch (BDException e) {
        log.error("Error en la BBDD recuperando los datos antes de imprimir", e);
    } catch (Exception ex) {
        log.error("Error en la BBDD guardando tipos documentales Lanbide por Procedimiento", ex);
        if (con != null) {
            adaptador.rollBack(con);
        }
        throw new Exception(ex);
    } finally {
        try {
            if (con != null) {
                adaptador.devolverConexion(con);
            }
        } catch (Exception e) {
            log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            if (con != null) {
                adaptador.rollBack(con);
            }
        }
    }
    return null; // Si algo falla
}


}
