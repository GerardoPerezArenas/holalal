package es.altia.flexia.integracion.moduloexterno.melanbide68.util;

import es.altia.flexia.integracion.moduloexterno.melanbide68.exception.DatoExcelNoValidoException;
import es.altia.flexia.integracion.moduloexterno.melanbide68.exception.ExcelRowMappingException;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class MeLanbide68MappingUtils
{
    private static MeLanbide68MappingUtils instance = null;
    private static Logger log = LogManager.getLogger(MeLanbide68MappingUtils.class);
    
    public static MeLanbide68MappingUtils getInstance()
    {
    if (instance == null) {
            synchronized(MeLanbide68MappingUtils.class)
            {
                instance = new MeLanbide68MappingUtils();
            }
        }
        return instance;
    }
    
  public Object mapRow(HSSFRow row, Class clazz)
    throws Exception
        {
    if (clazz == ObjetoSGAVO.class) {
            return mapearObjetoSGAVO(row);
        }        
         
        return null;
    }
    
  public Object map(ResultSet rs, Class clazz)
    throws Exception
        {
    if (clazz == FilaTipDocLanbideVO.class) {
            return mapearFilaTipDocLanbideVO(rs);
    }
    if (clazz == FilaTipDocDokusiVO.class) {
            return mapearFilaTipDocDokusiVO(rs);
    }
    if (clazz == TipDocLanbideVO.class) {        
            return mapearTipDocLanbideVO(rs);
    }
    if (clazz == FilaMetadatoVO.class) {
            return mapearFilaMetadatoVO(rs);
    }
    if (clazz == MetadatoVO.class) {
            return mapearMetadatoVO(rs);
    }
    if (clazz == FilaTipDokusiVO.class) {
            return mapearFilaTipDokusiVO(rs);
    }
    if (clazz == TipDokusiVO.class) {
            return mapearTipDokusiVO(rs);
    }
    if (clazz == ProcedimientoVO.class) {
            return mapearProcedimientosVO(rs);
    }
    if (clazz == FilaTipDocProcVO.class) {
            return mapearFilaTipDocProcVO(rs);
    }
    if (clazz == TipDocPorProcedVO.class) {
            return mapearTipDocPorProcedVO(rs);
    }
    if (clazz == GrupoTipDocVO.class) {
            return mapearGrupoTipDocVO(rs);
    }
    if (clazz == FilaTipDocLanbideExcelVO.class) {
            return mapearFilaTipDocLanbideExcelVO(rs);
        }           
    if (clazz == FilaTipDocLanbideProcedimientoExcelVO.class) {
      return mapearFilaTipDocLanbideProcedimientoExcelVO(rs);
    }
        return null;
    }
    
  private ObjetoSGAVO mapearObjetoSGAVO(HSSFRow row)
    throws Exception
    {
        ObjetoSGAVO objSGA = null;
    String campo = "";
    String tipoRequerido = "";
    int longMax = -1;
        String mensajeValidarDatosExcel="";
    if ((row != null) && (!MeLanbide68Utils.esFilaVacia(2, row))) {
            try
            {
                HSSFCell cell = null;
        
                objSGA = new ObjetoSGAVO();

                campo = "1";
        tipoRequerido = "expectedType.string";
                longMax = 12;

                int celdaIni = 0;
                cell = row.getCell((short)celdaIni);
        String valorString = MeLanbide68Utils.getValorCelda(cell, tipoRequerido).trim();
                
                String rdoValidarDatoExcel = MeLanbide68Utils.validarDatoExcel(valorString, tipoRequerido, longMax, false);
                if ("0".equals(rdoValidarDatoExcel))
                {
                    objSGA.setCodigoSGA(valorString);
        }
        else
        {
                    switch (Integer.parseInt(rdoValidarDatoExcel))
                    {
                        case 1:
                            mensajeValidarDatosExcel="El campo CodigoSGA tiene una longitud mayor de la permitida";
                            break;
                        case 2:
                            mensajeValidarDatosExcel="El campo CodigoSGA debe ser de texto";
                            break;
                        case 3:
                            mensajeValidarDatosExcel="El campo CodigoSGA no puede estar vacío"; 
                            break;
                        case 4:
                            mensajeValidarDatosExcel="El campo CodigoSGA debe ser de texto";                        
                    }
                    throw new DatoExcelNoValidoException();
                }
                

                campo = "2";
        tipoRequerido = "expectedType.string";
                longMax = 30;                
                celdaIni++;
                cell = row.getCell((short)celdaIni);
        valorString = MeLanbide68Utils.getValorCelda(cell, tipoRequerido).trim();

                rdoValidarDatoExcel = MeLanbide68Utils.validarDatoExcel(valorString, tipoRequerido, longMax, false);
                mensajeValidarDatosExcel="";
                if ("0".equals(rdoValidarDatoExcel))
                {
                    objSGA.setExpedienteSGA(valorString);
        }
        else
        {
                    switch (Integer.parseInt(rdoValidarDatoExcel))
                    {
                        case 1:
                            mensajeValidarDatosExcel="El campo ExpedienteSGA tiene una longitud mayor de la permitida";
                            break;
                        case 2:
                            mensajeValidarDatosExcel="El campo ExpedienteSGA debe ser de texto";
                            break;
                        case 3:
                            mensajeValidarDatosExcel="El campo ExpedienteSGA no puede estar vacío"; 
                            break;
                        case 4:
                            mensajeValidarDatosExcel="El campo ExpedienteSGA debe ser de texto";                        
                    }
                    throw new DatoExcelNoValidoException();
                } 
            }
            catch(Exception ex)
            {
                ExcelRowMappingException erme = new ExcelRowMappingException(campo);
        erme.setLongMax(Integer.valueOf(longMax));
                erme.setTipo(tipoRequerido);
                erme.setMensaje(mensajeValidarDatosExcel);
                throw erme;
            }
        }
        return objSGA;
    }
  
  private FilaTipDocLanbideVO mapearFilaTipDocLanbideVO(ResultSet rs)
    throws Exception
    {
        FilaTipDocLanbideVO fila = new FilaTipDocLanbideVO();
         
        fila.setId(rs.getInt("TIPDOC_ID"));
        fila.setTipDocID(rs.getInt("CODTIPDOC"));
        fila.setTipDocLanbide_es(rs.getString("TIPDOC_LANBIDE_ES"));
        fila.setTipDocLanbide_eu(rs.getString("TIPDOC_LANBIDE_EU"));
        fila.setTipDocLanbide_es_L(rs.getString("DESCTIPDOC_LANBIDE_ES"));
        fila.setTipDocLanbide_eu_L(rs.getString("DESCTIPDOC_LANBIDE_EU"));
        fila.setTipDocDokusi(rs.getString("TIPDOC_DOKUSI"));
        fila.setTieneMetadato(rs.getString("TIENE_METADATO"));
        fila.setCodGrupo(rs.getString("COD_GRUPO_TIPDOC"));
        if(rs.getString("FECHA_BAJA")==null){
            fila.setDeshabilitado("N");              
        } else {
            fila.setDeshabilitado("S");              
        }        
        
        return fila;
    }

  private FilaTipDocDokusiVO mapearFilaTipDocDokusiVO(ResultSet rs)
    throws Exception
    {
        FilaTipDocDokusiVO fila = new FilaTipDocDokusiVO();
         
    fila.setCodTipDoc(rs.getInt("CODTIPDOC"));
        fila.setDescTipDoc(rs.getString("TIPDOC_LANBIDE"));
        fila.setTipDocDokusi(rs.getString("TIPDOC_DOKUSI"));
        
        return fila;
    }
    
  private TipDocLanbideVO mapearTipDocLanbideVO(ResultSet rs)
    throws Exception
    {
        TipDocLanbideVO fila = new TipDocLanbideVO();
         
    fila.setCodTipDocBBDD(rs.getInt("CODTIPDOC"));
        fila.setCodTipDoc(rs.getInt("TIPDOC_ID"));
        fila.setDescTipDoc(rs.getString("TIPDOC_LANBIDE_ES"));
        
        return fila;
    }
    
  private FilaMetadatoVO mapearFilaMetadatoVO(ResultSet rs)
    throws Exception
    {
        FilaMetadatoVO fila = new FilaMetadatoVO();
         
        fila.setMetadato(rs.getString("TIPDOC_ID_METADATO"));
        fila.setMetadatoDCTM(rs.getString("TIPDOC_METADATODCTM"));
        fila.setObligatorio(rs.getString("TIPDOC_OBLIGATORIO"));
        fila.setDeshabilitado(rs.getString("DESHABILITADO"));        
        
        return fila;
    }

  private MetadatoVO mapearMetadatoVO(ResultSet rs)
    throws Exception
    {
        MetadatoVO fila = new MetadatoVO();
         
        fila.setTipoDocumental(rs.getInt("TIPDOC_ID"));
        fila.setMetadato(rs.getString("TIPDOC_ID_METADATO"));
        fila.setMetadatoDCTM(rs.getString("TIPDOC_METADATODCTM"));
        fila.setObligatorio(rs.getString("TIPDOC_OBLIGATORIO"));
        fila.setFecha_baja(rs.getString("FECHA_BAJA"));         
        
        return fila;
    }

  private FilaTipDokusiVO mapearFilaTipDokusiVO(ResultSet rs)
    throws Exception
    {
        FilaTipDokusiVO fila = new FilaTipDokusiVO();
    fila.setCodTipDoc(rs.getInt("CODTIPDOC"));
        fila.setCodDokusi(rs.getString("TIPDOC_DOKUSI"));
        fila.setCodDokusiPadre(rs.getString("TIPDOC_DOKUSI_PADRE"));     
    Integer iVal = Integer.valueOf(rs.getInt("TIPDOC_DOKUSI_FAMILIA"));
        if (rs.wasNull()) {
            iVal=null;
        }
        fila.setCodDokusiFamilia(iVal);
        fila.setDesDokusi_es(rs.getString("TIPDOC_DOKUSI_ES"));
        fila.setDesDokusi_eu(rs.getString("TIPDOC_DOKUSI_EU"));
        
        return fila;
    }

  private TipDokusiVO mapearTipDokusiVO(ResultSet rs)
    throws Exception
    {
        TipDokusiVO fila = new TipDokusiVO();
         
        fila.setCodDokusi(rs.getString("TIPDOC_DOKUSI"));
        fila.setDesDokusi(rs.getString("TIPDOC_DOKUSI_ES"));
        
        return fila;
    }
    
  public ProcedimientoVO mapearProcedimientosVO(ResultSet rs)
    throws SQLException
    {
        ProcedimientoVO fila = new ProcedimientoVO();

        fila.setCodProc(rs.getString("PRO_COD"));
        fila.setDescProc(rs.getString("PRO_COD") + " - " + rs.getString("PRO_DES"));
        
        return fila;
    }    

  public FilaTipDocProcVO mapearFilaTipDocProcVO(ResultSet rs)
    throws SQLException
    {
        FilaTipDocProcVO fila = new FilaTipDocProcVO();
    fila.setCodTipDocBBDD(rs.getInt("COD_TIPDOC"));
        fila.setCodTipDoc(rs.getInt("TIP_DOC"));
        fila.setDescTipDoc_es(rs.getString("TIPDOC_ES"));
        fila.setDescTipDoc_eu(rs.getString("TIPDOC_EU"));
    fila.setTipDocDokusi(rs.getString("TIPDOC_DOKUSI")); // Mapeo de la nueva columna
        
        return fila;
    }    

  public TipDocPorProcedVO mapearTipDocPorProcedVO(ResultSet rs)
    throws SQLException
    {
        TipDocPorProcedVO fila = new TipDocPorProcedVO();
        fila.setProcedimiento(rs.getString("COD_PROC"));        
        fila.setTipoDocumental(rs.getInt("TIPDOC_ID"));
        fila.setTipoDoc_es(rs.getString("TIPDOC_LANBIDE_ES"));
        fila.setTipoDoc_eu(rs.getString("TIPDOC_LANBIDE_EU"));
        
        return fila;
    } 
    
  public GrupoTipDocVO mapearGrupoTipDocVO(ResultSet rs)
    throws SQLException
    {
        GrupoTipDocVO fila = new GrupoTipDocVO();
        fila.setCodGrupo(rs.getString("DES_VAL_COD"));
        
        String desc_eu_es = rs.getString("DES_NOM");
        String[] desc_eu_es_sep = desc_eu_es.split("[|]");
        
        fila.setDescGrupo_es(desc_eu_es_sep[0]);
        fila.setDescGrupo_eu(desc_eu_es_sep[1]);
        
        return fila;
    } 
    
  private FilaTipDocLanbideExcelVO mapearFilaTipDocLanbideExcelVO(ResultSet rs)
    throws Exception
    {
        FilaTipDocLanbideExcelVO fila = new FilaTipDocLanbideExcelVO();
         
        fila.setTipDocId(rs.getInt("TIPDOC_ID"));
        fila.setTipDocLanbideEs(rs.getString("TIPDOC_LANBIDE_ES"));
        fila.setTipDocLanbideEu(rs.getString("TIPDOC_LANBIDE_EU"));
        fila.setFechaBaja(rs.getDate("FECHA_BAJA"));
        fila.setDescTipDocLanbideEs(rs.getString("DESCTIPDOC_LANBIDE_ES"));
        fila.setDescTipDocLanbideEu(rs.getString("DESCTIPDOC_LANBIDE_EU"));
        fila.setCodTipDoc(rs.getInt("CODTIPDOC"));
        fila.setCodGrupoTipDoc(rs.getString("COD_GRUPO_TIPDOC"));
        fila.setTipDocDokusi(rs.getString("TIPDOC_DOKUSI"));
        fila.setFamilia(rs.getString("FAMILIA"));      
        
        return fila;
    }
   private FilaTipDocLanbideProcedimientoExcelVO mapearFilaTipDocLanbideProcedimientoExcelVO(ResultSet rs)
    throws Exception
  {
    FilaTipDocLanbideProcedimientoExcelVO fila = new FilaTipDocLanbideProcedimientoExcelVO();
    
    fila.setCodTipDoc(rs.getInt("CODTIPDOC"));
    fila.setTipDocLanbideEs(rs.getString("TIPDOC_LANBIDE_ES"));
    fila.setTipDocLanbideEu(rs.getString("TIPDOC_LANBIDE_EU"));
    fila.setTipDocDokusi(rs.getString("TIPDOC_DOKUSI"));
       
    return fila;
  }
}
